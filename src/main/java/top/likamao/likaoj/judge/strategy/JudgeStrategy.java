package top.likamao.likaoj.judge.strategy;

import top.likamao.likaoj.model.dto.questionsubmit.JudgeInfo;

/**
 * 判题代理
 */
public interface JudgeStrategy {

    JudgeInfo doJudge(JudgeContext context);
}
