package top.likamao.likaojbackendquesitonservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@MapperScan("top.likamao.likaojbackendquesitonservice.mapper")
@ComponentScan("top.likamao")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "top.likamao.likaojbackendserviceclient.service")
public class LikaojBackendQuesitonServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LikaojBackendQuesitonServiceApplication.class, args);
    }

}
