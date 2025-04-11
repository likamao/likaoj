package top.likamao.likaojcodesendbox.model;

import lombok.Data;

@Data
public class ExecuteMessage {

    private Integer exitValue;

    private String message;

    private String errorMessage;

    private Long executeTime;

    private Long memory;
}
