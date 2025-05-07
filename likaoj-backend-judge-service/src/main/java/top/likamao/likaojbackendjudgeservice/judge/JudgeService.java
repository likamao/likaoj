package top.likamao.likaojbackendjudgeservice.judge;


import top.likamao.likaojbackendmodel.model.entity.QuestionSubmit;

public interface JudgeService {

    QuestionSubmit doJudge(Long questionSubmitId);
}
