package com.movo.kafka.demo;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Movo
 * @create 2021/5/11 15:11
 */
@Service
@EnableScheduling
public class Producer {

    private KafkaTemplate kafkaTemplate;

    public Producer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }

    @Scheduled(cron="*/5 * * * * ?")
    private void sendMessage() {
        sendMessage("movo-test", String.valueOf(System.currentTimeMillis()));
    }
}
