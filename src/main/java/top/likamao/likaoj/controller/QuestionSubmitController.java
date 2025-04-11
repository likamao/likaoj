package top.likamao.likaoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.likamao.likaoj.annotation.AuthCheck;
import top.likamao.likaoj.common.BaseResponse;
import top.likamao.likaoj.common.ErrorCode;
import top.likamao.likaoj.common.ResultUtils;
import top.likamao.likaoj.constant.UserConstant;
import top.likamao.likaoj.exception.BusinessException;
import top.likamao.likaoj.model.dto.question.QuestionQueryRequest;
import top.likamao.likaoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import top.likamao.likaoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import top.likamao.likaoj.model.entity.Question;
import top.likamao.likaoj.model.entity.QuestionSubmit;
import top.likamao.likaoj.model.entity.User;
import top.likamao.likaoj.model.vo.QuestionSubmitVO;
import top.likamao.likaoj.service.QuestionSubmitService;
import top.likamao.likaoj.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子点赞接口
 *
 * @author <a href="https://github.com/likaboy">LIKA</a>
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
@Deprecated
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Long> dosSubmitQuestion(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest, HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        Long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(questionSubmitId);
    }

    /**
     * 分页获取提交列表（除管理员外，其他用户只能看到非答案，提交代码等公开信息）
     *
     * @param requestQO
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, null));
    }

}
