package top.likamao.likaoj.judge.codesendbox.impl;

import top.likamao.likaoj.judge.codesendbox.CodeSendBoxInterface;
import top.likamao.likaoj.judge.codesendbox.model.ExecuteCodeRequest;
import top.likamao.likaoj.judge.codesendbox.model.ExecuteCodeResponse;

/**
 * 第三方代码沙箱接口实现类
 */
public class ThirdPartyCodeSendBoxImpl implements CodeSendBoxInterface {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        System.out.println("第三方代码沙箱");
        return null;
    }
}
