package top.likamao.likaoj.judge.codesendbox.impl;

import top.likamao.likaoj.judge.codesendbox.CodeSendBoxInterface;
import top.likamao.likaoj.judge.codesendbox.model.ExecuteCodeRequest;
import top.likamao.likaoj.judge.codesendbox.model.ExecuteCodeResponse;

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
