package com.movo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.movo.kmq.core.KmqMessage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Movo
 */
@SpringBootApplication
public class KmqCoreApplication {

    static {
        ParserConfig.getGlobalInstance().addAccept("com.movo");
    }

    public static void main(String[] args) {
        SpringApplication.run(KmqCoreApplication.class, args);
        String topic = "movo-topic";
        createTopic(topic);
        execute((httpClient) -> {
            providerSend(topic, httpClient);
        });
        execute((httpClient) -> {
            consumerPoll(topic, httpClient);
        });
    }

    private static void execute(Consumer<CloseableHttpClient> consumer) {
        new Thread(()->{
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                while (true) {
                    consumer.accept(httpClient);
                    Thread.sleep(3000);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void createTopic(String topic) {
        HttpPost post = new HttpPost("http://127.0.0.1:8608/broker/createTopic");
        StringEntity entity = new StringEntity(topic, "UTF-8");
        entity.setContentType("application/json;charset=utf-8");
        post.setEntity(entity);
        try(CloseableHttpClient httpClient = HttpClients.createDefault();CloseableHttpResponse response = httpClient.execute(post)) {
            System.out.println("创建Topic!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void providerSend(String topic, CloseableHttpClient httpClient) {
        HttpPost post = new HttpPost("http://127.0.0.1:8608/broker/send");
        Map<String, Object> map = new HashMap<>(2);
        map.put("topic", topic);
        map.put("message", JSON.toJSONString(new KmqMessage(null, System.currentTimeMillis())));
        StringEntity entity = new StringEntity(JSON.toJSONString(map), "UTF-8");
        entity.setContentType("application/json;charset=utf-8");
        post.setEntity(entity);
        try(CloseableHttpResponse response = httpClient.execute(post)) {
            System.out.println("生产者发送信息！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void consumerPoll(String topic, CloseableHttpClient httpClient) {
        int hashCode = Thread.currentThread().hashCode();
        HttpPost pollPost = new HttpPost("http://127.0.0.1:8608/broker/poll");
        Map<String, Object> map = new HashMap<>(2);
        map.put("topic", topic);
        map.put("hashcode", hashCode);
        StringEntity entity = new StringEntity(JSON.toJSONString(map), "UTF-8");
        entity.setContentType("application/json;charset=utf-8");
        pollPost.setEntity(entity);
        try(CloseableHttpResponse response = httpClient.execute(pollPost)) {
            String responseStr = EntityUtils.toString(response.getEntity());
            KmqMessage message = JSON.parseObject(responseStr, KmqMessage.class);
            if(message != null) {
                System.out.println("消费者接收信息:" + message.getBody());
                // commitConsumerOffset
                Integer offset = (Integer)message.getHeaders().get("offset");
                HttpPost offsetPost = new HttpPost("http://127.0.0.1:8608/broker/commitConsumerOffset");
                map = new HashMap<>(2);
                map.put("hashcode", String.valueOf(hashCode));
                map.put("offset", offset + 1);
                entity = new StringEntity(JSON.toJSONString(map), "UTF-8");
                entity.setContentType("application/json; charset=utf-8");
                offsetPost.setEntity(entity);
                try (CloseableHttpResponse httpResponse = httpClient.execute(offsetPost)) {
                    System.out.println("消费者提交offset!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
