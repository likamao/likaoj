package top.likamao.likaojbackendjudgeservice.judge.codesendbox;

import top.likamao.likaojbackendjudgeservice.judge.codesendbox.impl.ExampleCodeSendBoxImpl;
import top.likamao.likaojbackendjudgeservice.judge.codesendbox.impl.RemoteCodeSendBoxImpl;
import top.likamao.likaojbackendjudgeservice.judge.codesendbox.impl.ThirdPartyCodeSendBoxImpl;

/**
 * 代码沙箱静态工厂(用于返回)
 */
public class CodeSendBoxFactory {

    /**
     * 返回代码沙箱实例
     *
     * @param sendBoxType
     * @return
     */
    public static CodeSendBoxInterface newInstance(String sendBoxType) {
        if (sendBoxType == null) {
            throw new IllegalArgumentException("sendBoxType cannot be null");
        }

        return switch (sendBoxType) {
            case "third" -> new ThirdPartyCodeSendBoxImpl();
            case "remote" -> new RemoteCodeSendBoxImpl();
            case "example" -> new ExampleCodeSendBoxImpl();
            default -> throw new IllegalArgumentException("Invalid sendBoxType: " + sendBoxType);
        };
    }
}
