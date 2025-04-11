package top.likamao.likaoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.likamao.likaoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import top.likamao.likaoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import top.likamao.likaoj.model.entity.Question;
import top.likamao.likaoj.model.entity.QuestionSubmit;
import top.likamao.likaoj.model.entity.User;
import top.likamao.likaoj.model.vo.QuestionSubmitVO;

/**
 * @author echo
 * @description 针对表【question_submit(题目提交表)】的数据库操作Service
 * @createDate 2025-02-25 10:13:17
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 提交题目（内部服务）
     *
     * @param userId
     * @param questionId
     * @return
     */
    int doQuestionSubmitInner(long userId, long questionId);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);


    /**
     * 获取题目封装
     *
     * @param questionSubmit 提交对象
     * @param loginUser      登录用户
     * @return 题目封装
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);


    /**
     * 分页获取题目封装
     *
     * @param questionPage 分页对象
     * @param request      请求对象
     * @return 分页题目封装
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionPage, User loginUser);

}
