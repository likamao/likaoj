package top.likamao.likaojbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

// todo 如需开启 Redis，须移除 exclude 中的内容 @SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@SpringBootApplication()
@MapperScan("top.likamao.likaojbackenduserservice.mapper")
@ComponentScan("top.likamao")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "top.likamao.likaojbackendserviceclient.service")
public class LikaojBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LikaojBackendUserServiceApplication.class, args);
    }

}
