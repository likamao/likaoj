package top.likamao.likaojbackendmodel.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

@Getter
public enum QuestionSubmitStatusEnum {

    WAITING("等待提交", 0),
    SUBMITTED("已提交", 1),
    ACCEPTED("已通过", 2),
    REJECTED("已拒绝", 3);

    private final String text;
    private final Integer value;

    QuestionSubmitStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static QuestionSubmitStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            throw new IllegalArgumentException("The provided value is null or empty.");
        }
        for (QuestionSubmitStatusEnum anEnum : QuestionSubmitStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        throw new IllegalArgumentException("No enum constant with the specified value: " + value);
    }
}

