package top.likamao.likaojcodesendbox.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.likamao.likaojcodesendbox.common.SystemConstant;
import top.likamao.likaojcodesendbox.config.CommonConfigurationProperties;
import top.likamao.likaojcodesendbox.model.ExecuteCodeRequest;
import top.likamao.likaojcodesendbox.model.ExecuteCodeResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/v1/java")
public class CodeSendBoxController {

    @Resource
    private JavaNativeCodeSendBox javaNativeCodeSendBox;

    @PostMapping("/executeCode")
    public ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest, HttpServletRequest request, HttpServletResponse response) {
        if (!request.getHeader("Authorization").equals(SystemConstant.SYSTEM_AUTH)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        return javaNativeCodeSendBox.executeCode(executeCodeRequest);
    }
}