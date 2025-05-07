package top.likamao.likaojbackendmodel.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 提交问题的编程语言枚举
 *
 * @author <a href="https://github.com/likaboy">LIKA</a>
 */
@Getter
public enum QuestionSubmitLanguageEnum {

    USER_AVATAR("用户头像", "user_avatar"),

    JAVA("Java", "java"),
    PYTHON("Python", "python"),
    C("C", "c"),
    CPP("C++", "cpp"),
    CSHARP("C#", "csharp"),
    GO("Go", "go"),
    PHP("PHP", "php"),
    RUBY("Ruby", "ruby"),
    OTHER("其他", "other"),
    ;

    private final String text;

    private final String value;

    QuestionSubmitLanguageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 值
     * @return
     */
    public static QuestionSubmitLanguageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QuestionSubmitLanguageEnum anEnum : QuestionSubmitLanguageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

}
