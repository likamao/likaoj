package top.likamao.likaojbackendjudgeservice.judge.codesendbox;

import top.likamao.likaojbackendmodel.model.codesendbox.ExecuteCodeRequest;
import top.likamao.likaojbackendmodel.model.codesendbox.ExecuteCodeResponse;

/**
 * 代码沙箱接口
 */
public interface CodeSendBoxInterface {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);

}
