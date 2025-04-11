package top.likamao.likaojcodesendbox.model;

import lombok.Data;

@Data
public class JudgeInfo {

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 程序执行消耗内存
     */
    private Long memory;

    /**
     * 程序执行时间
     */
    private Long time;

}
