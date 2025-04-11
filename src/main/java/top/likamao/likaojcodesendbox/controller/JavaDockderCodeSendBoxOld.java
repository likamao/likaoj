package top.likamao.likaojcodesendbox.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import top.likamao.likaojcodesendbox.model.ExecuteCodeRequest;
import top.likamao.likaojcodesendbox.model.ExecuteCodeResponse;
import top.likamao.likaojcodesendbox.model.ExecuteMessage;
import top.likamao.likaojcodesendbox.model.JudgeInfo;
import top.likamao.likaojcodesendbox.utils.ProcessUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JavaDockderCodeSendBoxOld implements CodeSendBox {

    // 编译后的class文件存放路径
    private static final String GLOBAL_CODE_PATH_NAME = "tempclass";

    // 默认编译后的文件名（与类型保持一致）
    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private static final Boolean DOCKER_INIT_FLAG = false;

    private static final Long TIME_OUT = 5000L;


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        // 1.获取用户输入，初始化各类变量
        List<String> inuptList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();

        // 获取项目根目录
        String userDir = System.getProperty("user.dir");

        // 全局代码存放路径
        String globalCodePath = userDir + File.separator + GLOBAL_CODE_PATH_NAME;
        if (!FileUtil.exist(globalCodePath)) {
            FileUtil.mkdir(globalCodePath);
        }

        // 当前用户源文件和编译后的class文件存放路径
        String currentUseCodesPath = globalCodePath + File.separator + System.currentTimeMillis() + UUID.randomUUID();
        String currentUserSourceFilePath = currentUseCodesPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        File userCodeFile = FileUtil.writeString(code, currentUserSourceFilePath, StandardCharsets.UTF_8);

        // 2.编译当前用户源文件
        String compileCMD = String.format("javac -encoding UTF-8 %s", userCodeFile.getAbsolutePath());
        Process compileProcess = null;
        ExecuteMessage executeMessage = null;
        try {
            compileProcess = Runtime.getRuntime().exec(compileCMD);
            executeMessage = ProcessUtils.runCommand(compileProcess, "compile");

        } catch (IOException e) {
            log.error(executeMessage.getErrorMessage());
            return compileError(e);
        } finally {
            compileProcess.destroy();
        }


        DockerClient dockerClient = DockerClientBuilder.getInstance().build();

        String imageName = "openjdk:17-alpine";

        if (!DOCKER_INIT_FLAG) {
            try {
                //拉取镜像
                PullImageCmd pullImageCmd = dockerClient.pullImageCmd(imageName);
                PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
                    @Override
                    public void onNext(PullResponseItem item) {
                        System.out.println("下载镜像：" + item.getStatus());
                        super.onNext(item);
                    }
                };
                pullImageCmd.exec(pullImageResultCallback).awaitCompletion();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //创建容器
        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(imageName);
        HostConfig hostConfig = new HostConfig();
        // 内存限制，减少内存向硬盘的写入
        hostConfig.withMemorySwap(0L);
        hostConfig.withCpuCount(1L);
        hostConfig.withSecurityOpts(Arrays.asList("seccomp={安全管理配置}"));
        // 限制内存
        hostConfig.withMemory(100 * 1000 * 1000L);
        //  制定Volume 绑定目录,将本地文件同步到容器中
        hostConfig.setBinds(new Bind(currentUseCodesPath, new Volume("/app")));
        CreateContainerResponse createContainerResponse = createContainerCmd
                .withNetworkDisabled(true)
                .withHostConfig(hostConfig)
                .withAttachStdin(true)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withTty(true)
                .exec();
        System.out.println(createContainerResponse);
        String containerId = createContainerCmd.exec().getId();
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String input : inuptList) {
            StopWatch stopWatch = new StopWatch();
            String[] params = input.split(" ");
            String[] cmdArr = ArrayUtil.append(new String[]{"java", "-cp", "/app", "Main"}, params);
            InspectContainerResponse containerInfo = dockerClient.inspectContainerCmd(containerId).exec();
            if (Boolean.FALSE.equals(containerInfo.getState().getRunning())) {
                dockerClient.startContainerCmd(containerId).exec();
            }

            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                    .withCmd(cmdArr)
                    .withAttachStderr(true)
                    .withAttachStdout(true)
                    .withAttachStdin(true)
                    .exec();
            System.out.println("执行命令");
            String id = execCreateCmdResponse.getId();
            // 执行成功返回信息
            final String[] message = {null};
            // 执行失败返回信息
            final String[] errorMessage = {null};
            long time = 0L;
            final boolean[] timeOut = {true};
            class ExecStartCallback extends ResultCallback.Adapter<Frame> {


                @Override
                public void onComplete() {
                    // 如果执行完，表示没有超时
                    super.onComplete();
                    timeOut[0] = false;
                }

                @Override
                public void onNext(Frame item) {
                    super.onNext(item);
                    StreamType streamType = item.getStreamType();
                    if (streamType == StreamType.STDOUT) {
                        message[0] = new String(item.getPayload());
                        System.out.println("执行输出结果：" + new String(item.getPayload(), StandardCharsets.UTF_8));
                    } else if (streamType == StreamType.STDERR) {
                        errorMessage[0] = new String(item.getPayload());
                        System.out.println("执行错误：" + new String(item.getPayload(), StandardCharsets.UTF_8));
                    }
                }
            }

            // 启动内存监控
            long[] maxMemory = {0};

            // 获取容器状态数据中的内存
            StatsCmd statsCmd = dockerClient.statsCmd(imageName);

            statsCmd.exec(new ResultCallback<Statistics>() {
                @Override
                public void close() throws IOException {

                }

                @Override
                public void onStart(Closeable closeable) {
                }

                @Override
                public void onNext(Statistics statistics) {
                    System.out.println("内存" + statistics.getMemoryStats().getUsage());
                    maxMemory[0] = Math.max(statistics.getMemoryStats().getUsage(), maxMemory[0]);
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {


                }
            }).onComplete();


            try {
                stopWatch.start();
                dockerClient.execStartCmd(id).exec(new ExecStartCallback())
                        .awaitCompletion(TIME_OUT, TimeUnit.MICROSECONDS);
                stopWatch.stop();
                time = stopWatch.getTotalTimeMillis();
                statsCmd.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            executeMessage = new ExecuteMessage();
            executeMessage.setMessage(message[0]);
            executeMessage.setErrorMessage(errorMessage[0]);
            executeMessage.setExecuteTime(time);
            executeMessage.setMemory(maxMemory[0]);
            executeMessageList.add(executeMessage);

        }

        // 4. 封装响应信息
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        long maxExecuteTime = 0L;
        if (!executeMessageList.isEmpty()) {

            for (ExecuteMessage message : executeMessageList) {
                if (StrUtil.isNotBlank(message.getErrorMessage())) {
                    executeCodeResponse.setMessage(message.getErrorMessage());
                    // 代码执行中出现错误
                    executeCodeResponse.setStatus(3);
                    break;
                }

                if (message.getExecuteTime() > maxExecuteTime) {
                    maxExecuteTime = message.getExecuteTime();
                }
                outputList.add(message.getMessage());
            }
        }

        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(0);
        }

        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
//        judgeInfo.setMemory();
        judgeInfo.setTime(maxExecuteTime);

        executeCodeResponse.setJudgeInfo(judgeInfo);

        // 5. 清理临时文件
        if (userCodeFile.getParentFile().exists() && userCodeFile.exists()) {
            boolean delStatus = FileUtil.del(currentUseCodesPath);
            System.out.printf("删除临时文件：%s，状态：%s%n", currentUseCodesPath, (delStatus ? "成功" : "失败"));
        }

        // 删除容器
        return executeCodeResponse;
    }

    /**
     * 编译错误
     *
     * @param e
     * @return
     */
    private ExecuteCodeResponse compileError(Exception e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setMessage(e.getMessage());
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }
}
