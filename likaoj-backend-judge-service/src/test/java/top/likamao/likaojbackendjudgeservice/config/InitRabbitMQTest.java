package top.likamao.likaojbackendjudgeservice.config;

import org.junit.jupiter.api.Test;

class InitRabbitMQTest {

    @Test
    void init() {
        InitRabbitMQ initRabbitMQ = new InitRabbitMQ();
        initRabbitMQ.init();
    }
}