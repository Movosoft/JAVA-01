# 第十二周作业

## 第二十三课 分布式缓存-Redis高可用/Redisson/Hazelcast

1. （**必做**）配置redis的主从复制，sentinel高可用，Cluster集群。 

   主从复制

   ```
   6011.conf主
   
   bind 10.0.3.35
   daemonize yes
   requirepass 20121223
   protected-mode yes
   appendonly yes
   pidfile redis-6011.pid
   
   6012.conf从
   
   bind 10.0.3.35
   daemonize yes
   requirepass 32212102_1
   protected-mode yes
   appendonly yes
   pidfile redis-6012.pid
   
   slaveof 10.0.3.35 6011
   masterauth 20121223
   slave-read-only yes
   
   6013.conf从
   
   bind 10.0.3.35
   daemonize yes
   requirepass 32212102_2
   protected-mode yes
   appendonly yes
   pidfile redis-6013.pid
   
   slaveof 10.0.3.35 6011
   masterauth 20121223
   slave-read-only yes
   ```

   sentinel高可用

   ```
   6051.conf sentinel01
   
   bind 10.0.3.35
   daemonize yes
   requirepass 20216051
   protected-mode yes
   appendonly yes
   pidfile redis-6051.pid
   
   # 当前Sentinel节点监控 10.0.3.35:6011 这个主节点
   # 2代表判断主节点失败至少需要2个Sentinel节点节点同意
   # mymaster是主节点的别名
   sentinel monitor mymaster 10.0.3.35 6011 2
   # 每个Sentinel节点都要定期ping命令来判断Redis数据节点和其余Sentinel节点是否可达，如果超过60000ms且没有回复，则判定不可达
   sentinel down-after-milliseconds mymaster 60000
   # failover(主从切换)过期时间，当failover开始后，在此时间内仍然没有触发任何failover操作，当前sentinel将会认为此次failoer失败。 
   sentinel failover-timeout mymaster 180000
   # 当Sentinel节点集合对主节点故障判定达成一致时，Sentinel领导者节点会做故障转移操作，选出新的主节点，原来的从节点会向新的主节点发起复制操作，限制每次向新的主节点发起复制操作的从节点个数为1
   sentinel parallel-syncs mymaster 1
   ```

   Cluster集群

   ```
   #reids01/redis.conf
   
   bind 0.0.0.0
   port 6379
   requirepass movoredis
   masterauth movoredis
   protected-mode yes
   appendonly yes
   
   pidfile redis-6011.pid
   
   cluster-enabled yes
   cluster-config-file nodes-6011.conf
   cluster-node-timeout 5000
   
   #reids02/redis.conf
   
   bind 0.0.0.0
   port 6379
   requirepass movoredis
   masterauth movoredis
   protected-mode yes
   appendonly yes
   
   pidfile redis-6012.pid
   
   cluster-enabled yes
   cluster-config-file nodes-6012.conf
   cluster-node-timeout 5000
   
   以此类推...
   
   最后以redis-cli --cluster create  10.0.3.35:6011 10.0.3.35:6012 10.0.3.35:6013 10.0.3.35:6014 10.0.3.35:6015 10.0.3.35:6016  --cluster-replicas 1 -a movoredis命令建立集群
   ```

   

2. （选做）练习示例代码里下列类中的作业题： 

   08cache/redis/src/main/java/io/kimmking/cache/RedisApplication.java 

3. （选做☆）练习redission的各种功能； 

4. （选做☆☆）练习hazelcast的各种功能； 

5. （选做☆☆☆）搭建hazelcast 3节点集群，写入100万数据到一个map，模拟和演 

   示高可用；

## 第二十四课 分布式消息-消息队列基础

1. （**必做**）搭建ActiveMQ服务，基于JMS，写代码分别实现对于queue和topic的消息 

生产和消费，代码提交到github。 

​	

2. （选做）基于数据库的订单表，模拟消息队列处理订单： 

   1）一个程序往表里写新订单，标记状态为未处理(status=0); 

   2）另一个程序每隔100ms定时从表里读取所有status=0的订单，打印一下订单数据， 

   ​	  然后改成完成status=1； 

   3）（挑战☆）考虑失败重试策略，考虑多个消费程序如何协作。 

3. （选做）将上述订单处理场景，改成使用ActiveMQ发送消息处理模式。 

4. （选做）使用java代码，创建一个ActiveMQ Broker Server，并测试它。

5. （挑战☆☆）搭建ActiveMQ的network集群和master-slave主从结构。 

6. （挑战☆☆☆）基于ActiveMQ的MQTT实现简单的聊天功能或者Android消息推送。 

7. （挑战☆）创建一个RabbitMQ，用Java代码实现简单的AMQP协议操作。 

8. （挑战☆☆）搭建RabbitMQ集群，重新实现前面的订单处理。 

9. （挑战☆☆☆）使用Apache Camel打通上述ActiveMQ集群和RabbitMQ集群，实 

   现所有写入到ActiveMQ上的一个队列q24的消息，自动转发到RabbitMQ。 

10. （挑战☆☆☆）压测ActiveMQ和RabbitMQ的性能。