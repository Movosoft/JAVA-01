package com.movo.kmq.core;

/**
 * @author Movo
 * @create 2021/5/11 18:52
 */
public class Kmq {
    private String topic;
    private int capacity;
    private int offset;
    private KmqMessage[] messages;

    public Kmq(String topic, int capacity) {
        this.topic = topic;
        this.capacity = capacity;
        this.messages = new KmqMessage[capacity];
    }

    public void send(KmqMessage message) {
        messages[offset] = message;
        offset++;
    }

    public KmqMessage poll(int offset) {
        return messages[offset];
    }
}
