package top.likamao.likaojbackendcommon.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import top.likamao.likaojbackendcommon.common.CodeSendBoxConfigCommon;

import javax.annotation.PostConstruct;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "codesendbox.remote")
public class CodeSendBoxProperties {

    private String url;

    private String api;

    private String auth;

    @PostConstruct
    void init() {
        if (this.url == null || this.api == null || this.auth == null) {
            return;
        }
        CodeSendBoxConfigCommon.url = this.url;
        CodeSendBoxConfigCommon.api = this.api;
        CodeSendBoxConfigCommon.auth = DigestUtils.md5Hex(this.auth);
    }
}
