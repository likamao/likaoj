package top.likamao.likaojcodesendbox.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExecuteCodeRequest {

    private List<String> inputList;

    private String language;

    private String code;
}
