package com.movo.kafka.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author Movo
 * @create 2021/5/11 15:21
 */
@Service
public class Consumer {
    @KafkaListener(topics = {"movo-test"})
    public void receiveMessage(ConsumerRecord record) {
        System.out.printf("topic is: %s,value is: %s\n", record.topic(), record.value());
    }
}
