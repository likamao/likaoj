package top.likamao.likaojcodesendbox.controller;

import cn.hutool.core.util.ArrayUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import org.springframework.util.StopWatch;
import top.likamao.likaojcodesendbox.JavaCodeSendBoxTemplate;
import top.likamao.likaojcodesendbox.model.ExecuteMessage;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JavaDockerCodeSendBox extends JavaCodeSendBoxTemplate {

    private static final Boolean DOCKER_INIT_FLAG = false;

    /**
     * 3. 采用docker 生成默认的方法
     *
     * @param compiledCodeFile 编译代码文件
     * @param inputList        获取用户输入文本列表
     * @return
     */
    @Override
    public List<ExecuteMessage> runCode(File compiledCodeFile, List<String> inputList) {
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
        for (String input : inputList) {
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

            ExecuteMessage executeMessage = new ExecuteMessage();
            executeMessage.setMessage(message[0]);
            executeMessage.setErrorMessage(errorMessage[0]);
            executeMessage.setExecuteTime(time);
            executeMessage.setMemory(maxMemory[0]);
            executeMessageList.add(executeMessage);

        }
        return executeMessageList;
    }
}
