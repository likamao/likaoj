package top.likamao.likaojbackendserviceclient.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.likamao.likaojbackendmodel.model.entity.Question;
import top.likamao.likaojbackendmodel.model.entity.QuestionSubmit;

/**
 * @author echo
 * @description 针对表【question(题目)】的数据库操作Service
 * @createDate 2025-02-25 10:13:17
 */
@FeignClient(value = "likaoj-backend-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {

    // 根据id查询题目
    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") Long questionId);

    /**
     * 根据id查询题目提交
     *
     * @param questionId
     * @return
     */
    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") Long questionId);

    /**
     * 提交题目
     *
     * @param questionSubmit
     * @return
     */
    @PostMapping("/question_submit/update")
    Boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);
}
