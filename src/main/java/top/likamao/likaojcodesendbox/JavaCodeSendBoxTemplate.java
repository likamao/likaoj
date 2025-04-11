package top.likamao.likaojcodesendbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import top.likamao.likaojcodesendbox.controller.CodeSendBox;
import top.likamao.likaojcodesendbox.model.ExecuteCodeRequest;
import top.likamao.likaojcodesendbox.model.ExecuteCodeResponse;
import top.likamao.likaojcodesendbox.model.ExecuteMessage;
import top.likamao.likaojcodesendbox.model.JudgeInfo;
import top.likamao.likaojcodesendbox.utils.ProcessUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class JavaCodeSendBoxTemplate implements CodeSendBox {

    private static final String GLOBAL_CODE_PATH_NAME = "tempclass";

    // 默认编译后的文件名（与类型保持一致）
    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    public static final Long TIME_OUT = 10000L;

    public static final String globalCodePath;

    public static final String currentUseCodesPath;

    static {
        String userDir = System.getProperty("user.dir");
        globalCodePath = userDir + File.separator + GLOBAL_CODE_PATH_NAME;
        currentUseCodesPath = globalCodePath + File.separator + System.currentTimeMillis() + UUID.randomUUID();
    }

    /**
     * 读取用户的代码，写入到本地文件中
     *
     * @param code 用户代码
     * @return 用户代码文件
     */
    File readAndWriteUserCodeFile(String code) {
        // 获取项目根目录


        // 全局代码存放路径
        if (!FileUtil.exist(globalCodePath)) {
            FileUtil.mkdir(globalCodePath);
        }

        // 当前用户源文件和编译后的class文件存放路径
        String currentUserSourceFilePath = currentUseCodesPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        return FileUtil.writeString(code, currentUserSourceFilePath, StandardCharsets.UTF_8);
    }

    public ExecuteMessage compileFile(File userCodeFile) {
        String compileCMD = String.format("javac -encoding UTF-8 %s", userCodeFile.getAbsolutePath());
        Process compileProcess = null;
        ExecuteMessage executeMessage = null;
        try {
            compileProcess = Runtime.getRuntime().exec(compileCMD);
            executeMessage = ProcessUtils.runCommand(compileProcess, "compile");
            log.info("编译结果：{}", executeMessage);
            if (executeMessage != null && executeMessage.getExitValue() != 0) {
                throw new RuntimeException("编译错误：" + executeMessage.getErrorMessage());
            }
            return executeMessage;
        } catch (IOException e) {
            log.error("编译错误：{}", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (compileProcess != null) {
                compileProcess.destroy();
            }
        }
    }

    /**
     * 运行code
     *
     * @param compiledCodeFile 编译代码文件
     * @param inputList        获取用户输入文本列表
     * @return
     */
    public List<ExecuteMessage> runCode(File compiledCodeFile, List<String> inputList) {
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        try {
            for (String inputArgs : inputList) {
                String runCMD = String.format("java -Xmx256m -cp %s Main %s", currentUseCodesPath, inputArgs);
                log.info("执行命令：{}", runCMD);
                Process runProcess = Runtime.getRuntime().exec(runCMD);
                new Thread(() -> {
                    try {
                        Thread.sleep(TIME_OUT);
                        System.out.println("执行超时");
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                ExecuteMessage executeMessage = ProcessUtils.runCommand(runProcess, "run");
                executeMessageList.add(executeMessage);
//                ExecuteMessage executeMessage = ProcessUtils.runInterCommand(runProcess, "run", inputArgs);
                log.info("模板返回执行结果：{}", executeMessage);
            }
        } catch (IOException e) {
            throw new RuntimeException("执行错误", e);
        }
        return executeMessageList;
    }

    /**
     * 4. 获取输出结果
     *
     * @param executeMessageList 执行信息列表
     */
    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> executeMessageList) {
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
        judgeInfo.setTime(maxExecuteTime);

        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }

    /**
     * 清理文件
     *
     * @param userCodeFile 用户代码文件
     */
    public void cleanFile(File userCodeFile) {
        if (userCodeFile.getParentFile().exists() && userCodeFile.exists()) {
            boolean delStatus = FileUtil.del(currentUseCodesPath);
            log.info("删除临时文件：{}，状态：{}", currentUseCodesPath, (delStatus ? "成功" : "失败" + "文件路径: " + userCodeFile.getAbsolutePath()));
        }
    }


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        List<String> inputList = executeCodeRequest.getInputList();
        String codeLanguage = executeCodeRequest.getLanguage();
        String code = executeCodeRequest.getCode();

        // 1.读取并保存用户的代码到文件中
        File userCodeFile = readAndWriteUserCodeFile(code);

        // 2.编译当前用户源文件
        ExecuteMessage executeMessage = compileFile(userCodeFile);

        // 3.执行程序
        List<ExecuteMessage> executeMessageList = runCode(userCodeFile, inputList);

        // 4. 封装响应信息
        ExecuteCodeResponse executeCodeResponse = getOutputResponse(executeMessageList);


        // 5. 清理临时文件
//        cleanFile(userCodeFile);

        return executeCodeResponse;
    }

    /**
     * 获取错误响应
     *
     * @param e 异常
     * @return * @return 结果对象
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
