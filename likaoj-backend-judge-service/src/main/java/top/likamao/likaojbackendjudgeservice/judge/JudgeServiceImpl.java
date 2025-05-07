package top.likamao.likaojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.likamao.likaojbackendcommon.common.ErrorCode;
import top.likamao.likaojbackendcommon.exception.BusinessException;
import top.likamao.likaojbackendjudgeservice.judge.codesendbox.CodeSendBoxFactory;
import top.likamao.likaojbackendjudgeservice.judge.codesendbox.CodeSendBoxInterface;
import top.likamao.likaojbackendjudgeservice.judge.codesendbox.CodeSendBoxInterfaceProxy;
import top.likamao.likaojbackendjudgeservice.judge.strategy.JudgeContext;
import top.likamao.likaojbackendmodel.model.codesendbox.ExecuteCodeRequest;
import top.likamao.likaojbackendmodel.model.codesendbox.ExecuteCodeResponse;
import top.likamao.likaojbackendmodel.model.codesendbox.JudgeInfo;
import top.likamao.likaojbackendmodel.model.dto.question.JudgeCase;
import top.likamao.likaojbackendmodel.model.entity.Question;
import top.likamao.likaojbackendmodel.model.entity.QuestionSubmit;
import top.likamao.likaojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import top.likamao.likaojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import top.likamao.likaojbackendserviceclient.service.QuestionFeignClient;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Value("${codesendbox.type:example}")
    private String codeSendBoxType;

    @Resource
    private QuestionFeignClient questionFeignClient;

    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {

        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (Objects.isNull(questionSubmit)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "提交记录不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (Objects.isNull(question)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目不存在");
        }
        // 如果题目不存在，则直接返回
        // 如果不是等待中的提交，则直接返回
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            questionSubmit.setStatus(QuestionSubmitStatusEnum.ACCEPTED.getValue());
            questionFeignClient.updateQuestionSubmitById(questionSubmit);
            return questionSubmit;
        }

        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmit.getId());
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUBMITTED.getValue());
        questionSubmitUpdate.setUpdateTime(new Date());


        boolean updateStatus = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
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
        boolean judgeInfoUpdateResult = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);

        if (!judgeInfoUpdateResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新判题结果失败");
        }

        return questionFeignClient.getQuestionSubmitById(questionSubmitId);
    }
}
