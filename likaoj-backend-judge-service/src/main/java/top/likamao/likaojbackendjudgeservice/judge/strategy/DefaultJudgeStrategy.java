package top.likamao.likaojbackendjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import top.likamao.likaojbackendcommon.common.ErrorCode;
import top.likamao.likaojbackendmodel.model.codesendbox.JudgeInfo;
import top.likamao.likaojbackendmodel.model.dto.question.JudgeCase;
import top.likamao.likaojbackendmodel.model.dto.question.JudgeConfig;
import top.likamao.likaojbackendmodel.model.entity.Question;
import top.likamao.likaojbackendmodel.model.enums.JudgeInfoMessageEnum;

import java.util.List;

@Slf4j
public class DefaultJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeContext context) {
        List<String> inputList = context.getInputList();
        List<String> outputList = context.getOutputList();
        List<JudgeCase> judgeCaseList = context.getJudgeCaseList();
        JudgeInfo judgeInfo = context.getJudgeInfo();
        Question question = context.getQuestion();

        Long memory = judgeInfo.getMemory();
        Long time = judgeInfo.getTime();

        JudgeInfo judgeInfoResult = new JudgeInfo();
        judgeInfoResult.setMemory(memory);
        judgeInfoResult.setTime(time);

        try {
            // 判断输入输出列表大小是否一致
            if (outputList.size() != inputList.size()) {
                judgeInfoResult.setMessage(JudgeInfoMessageEnum.WRONG_ANSWER.getText());
                return judgeInfoResult;
            }

            // 判断题目的预期输出是否符合要求
            for (int i = 0; i < judgeCaseList.size(); i++) {
                if (!judgeCaseList.get(i).getOutput().equals(outputList.get(i))) {
                    judgeInfoResult.setMessage(JudgeInfoMessageEnum.WRONG_ANSWER.getText());
                    return judgeInfoResult;
                }
            }

            // 解析题目配置
            JudgeConfig judgeConfig = JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class);

            // 判断内存使用是否超过限制
            if (memory > judgeConfig.getMemoryLimit()) {
                judgeInfoResult.setMessage(JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED.getText());
                return judgeInfoResult;
            }

            // 判断时间使用是否超过限制
            if (time > judgeConfig.getTimeLimit()) {
                judgeInfoResult.setMessage(JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED.getText());
                return judgeInfoResult;
            }

            // 所有检查都通过，返回接受状态
            judgeInfoResult.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());

        } catch (Exception e) {
            // 捕获并处理任何可能的异常
            judgeInfoResult.setMessage(JudgeInfoMessageEnum.SYSTEM_ERROR.getText());
            log.error(ErrorCode.SYSTEM_ERROR.getMessage(), e);
        }

        return judgeInfoResult;
    }
}
