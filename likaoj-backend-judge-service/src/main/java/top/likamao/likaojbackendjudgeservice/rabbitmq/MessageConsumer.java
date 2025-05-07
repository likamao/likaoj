package top.likamao.likaojbackendjudgeservice.rabbitmq;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import top.likamao.likaojbackendjudgeservice.judge.JudgeService;

import javax.annotation.Resource;

/**
 * 消费者代码
 */
@Slf4j
@Component
public class MessageConsumer {
    @Resource
    private JudgeService judgeService;

    /**
     * 消费者方法，用于接收消息并处理
     *
     * @param message     接收到的消息
     * @param channel     RabbitMQ通道
     * @param deliveryTag 消息的唯一标识符
     */
    @SneakyThrows
    @RabbitListener(queues = {"judge_queue"}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("Received message: {}", message);
        Long questionSubmitId = Long.parseLong(message);
        try {
            judgeService.doJudge(questionSubmitId);
            channel.basicAck(deliveryTag, false); // 确认消息已被消费
        } catch (Exception e) {
            log.error("Judge failed: {}", e.getMessage());
            e.printStackTrace();
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
