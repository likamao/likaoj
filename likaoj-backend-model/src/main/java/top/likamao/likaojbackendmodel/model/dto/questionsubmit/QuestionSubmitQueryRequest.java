package top.likamao.likaojbackendmodel.model.dto.questionsubmit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.likamao.likaojbackendcommon.common.PageRequest;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 语言
     */
    private String language;

    /**
     * 判题状态
     */
    private Integer status;

    /**
     * 问题ID
     */
    private Long questionId;

    /**
     * 用户ID
     */
    private Long userId;

}