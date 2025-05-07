package top.likamao.likaojbackendjudgeservice.controller.inner;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.likamao.likaojbackendjudgeservice.judge.JudgeService;
import top.likamao.likaojbackendmodel.model.entity.QuestionSubmit;
import top.likamao.likaojbackendserviceclient.service.JudgeFeignClient;

import javax.annotation.Resource;

/**
 * note: 内部服务接口，用于服务间调用
 */
@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {
    @Resource
    private JudgeService judgeService;

    @Override
    @PostMapping("/do")
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") Long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}