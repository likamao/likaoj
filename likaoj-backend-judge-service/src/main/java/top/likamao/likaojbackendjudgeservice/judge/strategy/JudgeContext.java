package top.likamao.likaojbackendjudgeservice.judge.strategy;

import lombok.Builder;
import lombok.Data;
import top.likamao.likaojbackendmodel.model.codesendbox.JudgeInfo;
import top.likamao.likaojbackendmodel.model.dto.question.JudgeCase;
import top.likamao.likaojbackendmodel.model.entity.Question;
import top.likamao.likaojbackendmodel.model.entity.QuestionSubmit;

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
