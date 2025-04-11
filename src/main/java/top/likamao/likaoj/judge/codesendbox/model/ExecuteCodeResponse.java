package top.likamao.likaoj.judge.codesendbox.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.likamao.likaoj.model.dto.questionsubmit.JudgeInfo;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteCodeResponse {

    /**
     * 执行结果
     */
    private List<String> outputList;

    /**
     * 执行状态
     */
    private String status;

    /**
     * 接口信息
     */
    private String message;


    /**
     * 执行信息
     */
    private JudgeInfo judgeInfo;
}
