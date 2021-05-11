package com.movo.activemq.demo;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;

/**
 * @author Movo
 * @create 2021/5/11 11:07
 */
public class ArtemisDemo {
    /**
     * 点对点的队列消息传递模型
     * @param name
     * @return
     */
    private static ActiveMQDestination createQueueDest(String name) {
        return new ActiveMQQueue(name);
    }

    /**
     * 发布/订阅的主题消息传递模型
     * @param name
     * @return
     */
    private static ActiveMQDestination createTopicDest(String name) {
        return new ActiveMQTopic(name);
    }

    /**
     * 创建ActiveMQ连接工厂类
     * @param address
     * @return
     */
    private ActiveMQConnectionFactory createFactory(String address) {
        return  new ActiveMQConnectionFactory(address);
    }

    private static void operator(ActiveMQConnectionFactory factory, ActiveMQDestination dest) {
        ActiveMQConnection conn = null;
        Session session = null;
        try {
            conn = (ActiveMQConnection)factory.createConnection();
            conn.start();
            //创建一个session
            //第一个参数:是否支持事务，如果为true，则会忽略第二个参数，被jms服务器设置为SESSION_TRANSACTED
            //第一个参数为false时，paramB的值可为Session.AUTO_ACKNOWLEDGE，Session.CLIENT_ACKNOWLEDGE，DUPS_OK_ACKNOWLEDGE其中一个。
            //Session.AUTO_ACKNOWLEDGE为自动确认，客户端发送和接收消息不需要做额外的工作。哪怕是接收端发生异常，也会被当作正常发送成功。
            //Session.CLIENT_ACKNOWLEDGE为客户端确认。客户端接收到消息后，必须调用javax.jms.Message的acknowledge方法。jms服务器才会当作发送成功，并删除消息。
            //DUPS_OK_ACKNOWLEDGE允许副本的确认模式。一旦接收方应用程序的方法调用从处理消息处返回，会话对象就会确认消息的接收；而且允许重复确认。
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(dest);
            MessageConsumer consumer = session.createConsumer(dest);
            MessageListener listener = new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        System.out.println(dest.getPhysicalName() + "[" + dest.getDestinationTypeAsString() + "]收到消息:" + ((TextMessage)message).getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            };
            consumer.setMessageListener(listener);
            while(true) {
                producer.send(session.createTextMessage("来自Movo的消息-" + System.currentTimeMillis()));
                Thread.sleep(5000);
            }
        } catch (JMSException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if(session != null) {
                    session.close();
                }
                if(conn != null) {
                    conn.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        ActiveMQDestination queueDest = createQueueDest("queueDemo");
        ActiveMQDestination topicDest = createTopicDest("topicDemo");
        new Thread(() -> {
            operator(factory, queueDest);
        }).start();
        new Thread(() -> {
            operator(factory, topicDest);
        }).start();
    }
}
