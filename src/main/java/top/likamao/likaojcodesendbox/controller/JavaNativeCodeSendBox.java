package top.likamao.likaojcodesendbox.controller;

import org.springframework.stereotype.Component;
import top.likamao.likaojcodesendbox.JavaCodeSendBoxTemplate;
import top.likamao.likaojcodesendbox.model.ExecuteCodeRequest;
import top.likamao.likaojcodesendbox.model.ExecuteCodeResponse;

/**
 * JAVA 原生实现
 */
@Component
public class JavaNativeCodeSendBox extends JavaCodeSendBoxTemplate {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}
