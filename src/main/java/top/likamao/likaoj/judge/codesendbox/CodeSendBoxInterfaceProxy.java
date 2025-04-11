package top.likamao.likaoj.judge.codesendbox;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.likamao.likaoj.judge.codesendbox.model.ExecuteCodeRequest;
import top.likamao.likaoj.judge.codesendbox.model.ExecuteCodeResponse;

import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class CodeSendBoxInterfaceProxy implements CodeSendBoxInterface {

    private final CodeSendBoxInterface target;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        log.info("代码沙箱执行中...，当前语言为 {}", executeCodeRequest.getLanguage());
        ExecuteCodeResponse executeCodeResponse = target.executeCode(executeCodeRequest);
        log.info("代码沙箱执结束...，执行状态{}", Objects.isNull(executeCodeResponse) ? "失败" : executeCodeResponse.getJudgeInfo());

        return executeCodeResponse;
    }
}
