package top.likamao.likaojbackendjudgeservice.judge;

import org.springframework.stereotype.Component;
import top.likamao.likaojbackendjudgeservice.judge.strategy.JavaJudgeStrategy;
import top.likamao.likaojbackendjudgeservice.judge.strategy.JudgeContext;
import top.likamao.likaojbackendmodel.model.codesendbox.JudgeInfo;
import top.likamao.likaojbackendmodel.model.enums.QuestionSubmitLanguageEnum;


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
