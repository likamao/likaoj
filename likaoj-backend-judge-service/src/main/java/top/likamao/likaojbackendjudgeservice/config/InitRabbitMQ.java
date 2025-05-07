package top.likamao.likaojbackendjudgeservice.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class InitRabbitMQ {

    @PostConstruct
    public void init() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("guest");
            factory.setPassword("guest");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            String EXCHANGE_NAME = "likaojbackendjudgeservice";
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String QUEUE_NAME = "judge_queue";
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "judge_routingkey");
            connection.close();
        } catch (IOException | TimeoutException e) {
            log.error("RabbitMQ init error: {}", e.getMessage());
        }
        log.info("RabbitMQ init success.");
    }
}
