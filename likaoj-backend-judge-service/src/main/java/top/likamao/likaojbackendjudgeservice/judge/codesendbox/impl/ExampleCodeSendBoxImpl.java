package top.likamao.likaojbackendjudgeservice.judge.codesendbox.impl;


import top.likamao.likaojbackendjudgeservice.judge.codesendbox.CodeSendBoxInterface;
import top.likamao.likaojbackendmodel.model.codesendbox.ExecuteCodeRequest;
import top.likamao.likaojbackendmodel.model.codesendbox.ExecuteCodeResponse;

/**
 * 示例代码沙箱实现类
 */
public class ExampleCodeSendBoxImpl implements CodeSendBoxInterface {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("示例代码沙箱");
        return null;
    }
}
