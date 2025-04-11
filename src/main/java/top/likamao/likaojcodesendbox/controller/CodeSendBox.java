package top.likamao.likaojcodesendbox.controller;


import top.likamao.likaojcodesendbox.model.ExecuteCodeRequest;
import top.likamao.likaojcodesendbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口
 */
public interface CodeSendBox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);

}
