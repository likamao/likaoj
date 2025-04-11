package top.likamao.likaoj.judge.strategy;

import org.springframework.stereotype.Component;
import top.likamao.likaoj.model.dto.questionsubmit.JudgeInfo;
import top.likamao.likaoj.model.enums.QuestionSubmitLanguageEnum;

@Component
public class JudgeManager {

    public JudgeInfo doJudge(JudgeContext context) {
        String language = context.getQuestionSubmit().getLanguage();
        JudgeInfo judgeInfoResult = null;
        if (language.equals(QuestionSubmitLanguageEnum.JAVA.getValue())) {
            judgeInfoResult = new JavaJudgeStrategy().doJudge(context);
        }
        return judgeInfoResult;
    }
}
