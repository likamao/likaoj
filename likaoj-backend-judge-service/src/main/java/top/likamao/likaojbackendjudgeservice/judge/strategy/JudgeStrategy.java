package top.likamao.likaojbackendjudgeservice.judge.strategy;


import top.likamao.likaojbackendmodel.model.codesendbox.JudgeInfo;

/**
 * 判题代理
 */
public interface JudgeStrategy {

    JudgeInfo doJudge(JudgeContext context);
}
