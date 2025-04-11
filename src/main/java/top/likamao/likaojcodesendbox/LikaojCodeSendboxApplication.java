package top.likamao.likaojcodesendbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import top.likamao.likaojcodesendbox.config.CommonConfigurationProperties;

@SpringBootApplication
public class LikaojCodeSendboxApplication {

    public static void main(String[] args) {
        SpringApplication.run(LikaojCodeSendboxApplication.class, args);
    }

}
