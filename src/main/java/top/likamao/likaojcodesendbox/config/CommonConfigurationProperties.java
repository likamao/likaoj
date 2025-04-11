package top.likamao.likaojcodesendbox.config;

import cn.hutool.crypto.digest.DigestUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import top.likamao.likaojcodesendbox.common.SystemConstant;

import javax.annotation.PostConstruct;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "system")
public class CommonConfigurationProperties {

    private String auth;

    @PostConstruct
    void init() {
        log.info("初始化系统认证密钥：{}", this.auth);
        SystemConstant.SYSTEM_AUTH = DigestUtil.md5Hex(this.auth);
    }
}
