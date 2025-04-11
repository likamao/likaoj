package top.likamao.likaoj.judge.codesendbox;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import top.likamao.likaoj.judge.codesendbox.impl.ExampleCodeSendBoxImpl;
import top.likamao.likaoj.judge.codesendbox.model.ExecuteCodeRequest;
import top.likamao.likaoj.judge.codesendbox.model.ExecuteCodeResponse;
import top.likamao.likaoj.model.enums.QuestionSubmitLanguageEnum;

import java.util.Arrays;
@Slf4j
@SpringBootTest
@Import(value = {ExampleCodeSendBoxImpl.class,CodeSendBoxProperties.class})
class CodeSendBoxInterfaceTest {

    @Value("${codesendbox.type}")
    private String codeSendBoxType;

    @Test
    void executeCode() {
        final String code = "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        int a = Integer.parseInt(args[0]);\n" +
                "        int b = Integer.parseInt(args[1]);\n" +
                "        int c = a + b;\n" +
                "        System.out.println(\"Sum of \" + a + \" and \" + b + \" is \" + c);\n" +
                "    }\n" +
                "}";
        CodeSendBoxInterface codeSendBoxInterface = CodeSendBoxFactory.newInstance(codeSendBoxType);

        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest
                .builder()
                .code(code)
                .language(QuestionSubmitLanguageEnum.JAVA.getValue())
                .inputList(Arrays.asList("1 2", "3 4")).build();

        CodeSendBoxInterface codeSendBoxInterfaceProxy = new CodeSendBoxInterfaceProxy(codeSendBoxInterface);
        ExecuteCodeResponse executeCodeResponse = codeSendBoxInterfaceProxy.executeCode(executeCodeRequest);

        log.info(executeCodeResponse.toString());
        Assertions.assertNotNull(executeCodeResponse);
    }
}