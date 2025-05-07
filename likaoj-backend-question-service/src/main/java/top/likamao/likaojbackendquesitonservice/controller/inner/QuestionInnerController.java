package top.likamao.likaojbackendquesitonservice.controller.inner;

import org.springframework.web.bind.annotation.*;
import top.likamao.likaojbackendmodel.model.entity.Question;
import top.likamao.likaojbackendmodel.model.entity.QuestionSubmit;
import top.likamao.likaojbackendquesitonservice.service.QuestionService;
import top.likamao.likaojbackendquesitonservice.service.QuestionSubmitService;
import top.likamao.likaojbackendserviceclient.service.QuestionFeignClient;

import javax.annotation.Resource;

/**
 * note: 内部服务接口，用于服务间调用
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Override
    // 根据id查询题目
    @GetMapping("/get/id")
    public Question getQuestionById(@RequestParam("questionId") Long questionId) {
        return questionService.getById(questionId);
    }

    /**
     * 根据id查询题目提交
     *
     * @param questionId
     * @return
     */
    @Override
    @GetMapping("/question_submit/get/id")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") Long questionId) {
        return questionSubmitService.getById(questionId);
    }

    /**
     * 提交题目
     *
     * @param questionSubmit
     * @return
     */
    @Override
    @PostMapping("/question_submit/update")
    public Boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }
}