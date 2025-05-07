package top.likamao.likaojbackendmodel.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题信息错误枚举
 *
 * @author <a href="https://github.com/likaboy">LIKA</a>
 * 
 */
@Getter
public enum JudgeInfoMessageEnum {

    ACCEPTED("Accepted", "AC"),
    PRESENTATION_ERROR("Presentation Error", "PE"),
    TIME_LIMIT_EXCEEDED("Time Limit Exceeded", "TLE"),
    MEMORY_LIMIT_EXCEEDED("Memory Limit Exceeded", "MLE"),
    OUTPUT_LIMIT_EXCEEDED("Output Limit Exceeded", "OLE"),
    RUNTIME_ERROR("Runtime Error", "RE"),
    WRONG_ANSWER("Wrong Answer", "WA"),
    WAITING("Waiting", "WT"),
    CHECK_FAILED("Check Failed", "CF"),
    COMPILE_ERROR("Compile Error", "CE"),
    SYSTEM_ERROR("System Error", "SE"),
    PENDING("Pending", "PD"),
    JUDGING("Judging", "JD"),
    JUDGED("Judged", "JD"),
    JUDGE_ERROR("Judge Error", "JE"),
    JUDGE_TIMEOUT("Judge Timeout", "JT"),
    JUDGE_MEMORY_LIMIT_EXCEEDED("Judge Memory Limit Exceeded", "JMLE"),
    JUDGE_OUTPUT_LIMIT_EXCEEDED("Judge Output Limit Exceeded", "JOLE"),
    JUDGE_RUNTIME_ERROR("Judge Runtime Error", "JRE"),
    JUDGE_COMPILE_ERROR("Judge Compile Error", "JCE"),
    JUDGE_SYSTEM_ERROR("Judge System Error", "JSE"),
    JUDGE_OTHER_ERROR("Judge Other Error", "JOE"),
    JUDGE_NOT_START("Judge Not Start", "JNS"),
    JUDGE_NOT_SUBMIT("Judge Not Submit", "JNS"),
    JUDGE_NOT_FOUND("Judge Not Found", "JNF"),
    JUDGE_NOT_SUPPORT("Judge Not Support", "JNS"),
    JUDGE_OTHER_ERROR_MSG("Judge Other Error", "JOE"),
    JUDGE_NOT_START_MSG("Judge Not Start", "JNS"),
    JUDGE_NOT_SUBMIT_MSG("Judge Not Submit", "JNS"),
    JUDGE_NOT_FOUND_MSG("Judge Not Found", "JNF"),
    JUDGE_NOT_SUPPORT_MSG("Judge Not Support", "JNS"),


    ;

    private final String text;

    private final String value;

    JudgeInfoMessageEnum(String text, String value) {
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
     * @param value
     * @return
     */
    public static JudgeInfoMessageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeInfoMessageEnum anEnum : JudgeInfoMessageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
