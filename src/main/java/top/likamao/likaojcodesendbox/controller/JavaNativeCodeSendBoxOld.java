package top.likamao.likaojcodesendbox.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
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

public class JavaNativeCodeSendBoxOld implements CodeSendBox {

    // 编译后的class文件存放路径
    private static final String GLOBAL_CODE_PATH_NAME = "tempclass";

    // 默认编译后的文件名（与类型保持一致）
    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private static final Long TIME_OUT = 10000L;

    private static final WordTree wordTree;

    static {
        final List<String> blackList = List.of("Files", "exec");
        wordTree = new WordTree();
        wordTree.addWords(blackList);
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        // 1.获取用户输入，初始化各类变量
        List<String> inuptList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();


        FoundWord foundWord = wordTree.matchWord(code);
        if (foundWord != null && foundWord.getFoundWord() != null) {
            System.out.println("存在敏感词：" + foundWord.getFoundWord());
            return compileError(new Exception("存在敏感词：" + foundWord.getFoundWord()));
        }

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
        try {
            compileProcess = Runtime.getRuntime().exec(compileCMD);
            ExecuteMessage executeMessage = ProcessUtils.runCommand(compileProcess, "compile");
            System.out.println(executeMessage);

        } catch (IOException e) {
            return compileError(e);
        } finally {
            compileProcess.destroy();
        }

        // 3.执行程序
        ExecuteMessage executeMessage;
        List<ExecuteMessage> messageList = new ArrayList<>();
        List<String> outputList = new ArrayList<>();
        try {
            for (String inputArgs : inuptList) {
                String runCMD = String.format("java -Xmx256m -cp %s Main %s", currentUseCodesPath, inputArgs);
                Process runProcess = Runtime.getRuntime().exec(runCMD);
                new Thread(() -> {
                    try {
                        Thread.sleep(TIME_OUT);
                        System.out.println("");
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                executeMessage = ProcessUtils.runCommand(runProcess, "run");
                messageList.add(executeMessage);
//                ExecuteMessage executeMessage = ProcessUtils.runInterCommand(runProcess, "run", inputArgs);
                System.out.println(executeMessage);
            }
        } catch (IOException e) {
            return compileError(e);
        }

        // 4. 封装响应信息
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        long maxExecuteTime = 0L;
        if (!messageList.isEmpty()) {

            for (ExecuteMessage message : messageList) {
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

        if (outputList.size() == messageList.size()) {
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
            System.out.println(String.format("删除临时文件：%s，状态：%s", currentUseCodesPath, (delStatus ? "成功" : "失败")));
        }
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
