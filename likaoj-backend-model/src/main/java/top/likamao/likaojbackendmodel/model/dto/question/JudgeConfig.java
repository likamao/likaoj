package top.likamao.likaojbackendmodel.model.dto.question;

import lombok.Data;

/**
 * 题目用例
 */
@Data
public class JudgeConfig {

    /**
     * 时间限制
     */
    private Long timeLimit;

    /**
     * 内存限制
     */
    private Long memoryLimit;

    /**
     * 栈空间限制
     */
    private  Long stackLimit;
}
