package top.likamao.likaojcodesendbox.controller;

import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import top.likamao.likaojcodesendbox.config.CommonConfigurationProperties;
import top.likamao.likaojcodesendbox.model.ExecuteCodeRequest;
import top.likamao.likaojcodesendbox.model.ExecuteCodeResponse;

import java.util.Arrays;

@SpringBootTest
@EnableConfigurationProperties(CommonConfigurationProperties.class)
class JavaNativeCodeSendBoxTest {

    @Test
    void executeCode() {
//        JavaNativeCodeSendBox javaNativeCodeSendBox = new JavaNativeCodeSendBox();
        JavaDockerCodeSendBox javaDockerCodeSendBox = new JavaDockerCodeSendBox();
        String code = ResourceUtil.readUtf8Str("testCode/simpleComputeArgs/Main.java");
//        String code = ResourceUtil.readUtf8Str("testCode/unsafe/ReadFileError.java");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .inputList(Arrays.asList("1 2", "3 4"))
                .language("java").build();

        ExecuteCodeResponse executeCodeResponse = javaDockerCodeSendBox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }
}