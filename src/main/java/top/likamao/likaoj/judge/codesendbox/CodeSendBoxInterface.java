package top.likamao.likaoj.judge.codesendbox;

import top.likamao.likaoj.judge.codesendbox.model.ExecuteCodeRequest;
import top.likamao.likaoj.judge.codesendbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口
 */
public interface CodeSendBoxInterface {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);

}
