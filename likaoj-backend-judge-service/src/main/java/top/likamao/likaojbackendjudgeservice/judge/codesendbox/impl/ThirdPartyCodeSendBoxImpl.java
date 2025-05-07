package top.likamao.likaojbackendjudgeservice.judge.codesendbox.impl;


import top.likamao.likaojbackendjudgeservice.judge.codesendbox.CodeSendBoxInterface;
import top.likamao.likaojbackendmodel.model.codesendbox.ExecuteCodeRequest;
import top.likamao.likaojbackendmodel.model.codesendbox.ExecuteCodeResponse;

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
