package top.likamao.likaoj.judge;

import top.likamao.likaoj.model.entity.QuestionSubmit;

public interface JudgeService {

    QuestionSubmit doJudge(Long questionSubmitId);
}
