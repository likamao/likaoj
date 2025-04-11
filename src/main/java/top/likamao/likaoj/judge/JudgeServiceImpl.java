package top.likamao.likaoj.judge;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.likamao.likaoj.common.ErrorCode;
import top.likamao.likaoj.exception.BusinessException;
import top.likamao.likaoj.judge.codesendbox.CodeSendBoxFactory;
import top.likamao.likaoj.judge.codesendbox.CodeSendBoxInterface;
import top.likamao.likaoj.judge.codesendbox.CodeSendBoxInterfaceProxy;
import top.likamao.likaoj.judge.codesendbox.model.ExecuteCodeRequest;
import top.likamao.likaoj.judge.codesendbox.model.ExecuteCodeResponse;
import top.likamao.likaoj.judge.strategy.JudgeContext;
import top.likamao.likaoj.judge.strategy.JudgeManager;
import top.likamao.likaoj.model.dto.question.JudgeCase;
import top.likamao.likaoj.model.dto.questionsubmit.JudgeInfo;
import top.likamao.likaoj.model.entity.Question;
import top.likamao.likaoj.model.entity.QuestionSubmit;
import top.likamao.likaoj.model.enums.QuestionSubmitLanguageEnum;
import top.likamao.likaoj.model.enums.QuestionSubmitStatusEnum;
import top.likamao.likaoj.service.QuestionService;
import top.likamao.likaoj.service.QuestionSubmitService;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JudgeServiceImpl implements JudgeService {

    @Value("${codesendbox.type:example}")
    private String codeSendBoxType;

    private final QuestionService questionService;
    private final QuestionSubmitService questionSubmitService;
    private final JudgeManager judgeManager;

    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {

        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (Objects.isNull(questionSubmit)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "提交记录不存在");
        }

        Question question = questionService.getById(questionSubmit.getQuestionId());
        if (Objects.isNull(question)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目不存在");
        }
        // 如果题目不存在，则直接返回
        // 如果不是等待中的提交，则直接返回
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            questionSubmit.setStatus(QuestionSubmitStatusEnum.ACCEPTED.getValue());
            questionSubmitService.updateById(questionSubmit);
            return questionSubmit;
        }

        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmit.getId());
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUBMITTED.getValue());
        questionSubmitUpdate.setUpdateTime(new Date());


        boolean updateStatus = questionSubmitService.updateById(questionSubmitUpdate);
        if (!updateStatus) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新提交状态失败");
        }

        // 获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());

        // 判题执行
        String code = questionSubmit.getCode();
        CodeSendBoxInterface codeSendBoxInterface = CodeSendBoxFactory.newInstance(codeSendBoxType);
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder().code(code).language(QuestionSubmitLanguageEnum.JAVA.getValue()).inputList(inputList).build();
        CodeSendBoxInterface codeSendBoxInterfaceProxy = new CodeSendBoxInterfaceProxy(codeSendBoxInterface);
        ExecuteCodeResponse executeCodeResponse = codeSendBoxInterfaceProxy.executeCode(executeCodeRequest);

        // 判题结果处理
        JudgeContext judgeContext = JudgeContext.builder()
                .judgeInfo(executeCodeResponse.getJudgeInfo())
                .outputList(executeCodeResponse.getOutputList())
                .inputList(inputList)
                .judgeCaseList(judgeCaseList)
                .question(question)
                .questionSubmit(questionSubmit)
                .build();
        JudgeInfo judgeInfoResult = judgeManager.doJudge(judgeContext);

        // 更新判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmit.getId());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfoResult));
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.ACCEPTED.getValue());
        questionSubmitUpdate.setUpdateTime(new Date());
        boolean judgeInfoUpdateResult = questionSubmitService.updateById(questionSubmitUpdate);

        if (!judgeInfoUpdateResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新判题结果失败");
        }

        return questionSubmitService.getById(questionSubmitId);
    }
}
