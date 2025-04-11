package top.likamao.likaoj.judge.strategy;

import lombok.Builder;
import lombok.Data;
import top.likamao.likaoj.model.dto.question.JudgeCase;
import top.likamao.likaoj.model.dto.questionsubmit.JudgeInfo;
import top.likamao.likaoj.model.entity.Question;
import top.likamao.likaoj.model.entity.QuestionSubmit;

import java.util.List;

/**
 * 判题执行上下文
 */
@Builder
@Data
public class JudgeContext {

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private JudgeInfo judgeInfo;

    private Question question;

    private QuestionSubmit questionSubmit;
}
