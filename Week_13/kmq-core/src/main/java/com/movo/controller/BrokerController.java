package com.movo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.movo.kmq.core.KmqBroker;
import com.movo.kmq.core.KmqMessage;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Movo
 * @create 2021/5/11 19:17
 */
@RestController
@RequestMapping("broker")
public class BrokerController {
    private KmqBroker kmqBroker;

    public BrokerController(KmqBroker kmqBroker) {
        this.kmqBroker = kmqBroker;
    }

    @RequestMapping("/createTopic")
    public void createTopic(@RequestBody String name) {
        kmqBroker.createTopic(name);
    }

    @RequestMapping("/poll")
    public KmqMessage poll(@RequestBody Map<String, String> map) {
        return kmqBroker.poll(map.get("topic"), map.get("hashcode"));
    }

    @RequestMapping("/send")
    public void send(@RequestBody Map<String, Object> map) {
        kmqBroker.send((String)map.get("topic"), JSON.parseObject((String) map.get("message"), KmqMessage.class));
    }

    @RequestMapping("/commitConsumerOffset")
    public void commitConsumerOffset(@RequestBody Map<String, Object> map) {
        kmqBroker.commitConsumerOffset((String)map.get("hashcode"), (Integer)map.get("offset"));
    }
}
