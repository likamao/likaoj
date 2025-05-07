package top.likamao.likaojbackendserviceclient.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.likamao.likaojbackendmodel.model.entity.QuestionSubmit;

@FeignClient(name = "likaoj-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {

    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") Long questionSubmitId);
}
