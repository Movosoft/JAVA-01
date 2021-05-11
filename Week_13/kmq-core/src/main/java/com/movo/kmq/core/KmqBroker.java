package com.movo.kmq.core;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Movo
 * @create 2021/5/11 19:00
 */
@Service
public class KmqBroker {
    private static final int CAPACITY = 10000;
    private final Map<String, Kmq> kmqMap = new ConcurrentHashMap<>(64);
    private final Map<String, Integer> consumerOffsetMap = new ConcurrentHashMap<>();

    public void createTopic(String name) {
        kmqMap.putIfAbsent(name, new Kmq(name, CAPACITY));
    }

    public Kmq findKmq(String topic) {
        return kmqMap.get(topic);
    }

    public void send(String topic, KmqMessage message) {
        Kmq kmq = findKmq(topic);
        if(kmq != null) {
            kmq.send(message);
        } else {
            throw new RuntimeException("Topic[" + topic + "] doesn't exist!");
        }
    }

    public void commitConsumerOffset(String hashcode, Integer offset) {
        consumerOffsetMap.put(hashcode, offset);
    }

    public KmqMessage poll(String topic, String hashcode) {
        int offset = consumerOffsetMap.get(hashcode) == null ? 0 : consumerOffsetMap.get(hashcode);
        Kmq kmq = findKmq(topic);
        KmqMessage message = kmq.poll(offset);
        if(message != null) {
            if(message.getHeaders() == null) {
                message.setHeaders(new HashMap<>(1));
            }
            message.getHeaders().put("offset", offset);
        }
        return message;
    }
}
