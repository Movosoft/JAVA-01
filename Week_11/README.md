# 第十一周作业

## 第二十一课 分布式缓存-缓存技术

1. （选做）按照课程内容，动手验证Hibernate和Mybatis缓存。 

2. （选做）使用spring或guava cache，实现业务数据的查询缓存。

3. （挑战☆）编写代码，模拟缓存穿透，击穿，雪崩。

4. （挑战☆☆）自己动手设计一个简单的cache，实现过期策略。

## 第二十二课 分布式缓存-Redis详解

1. （选做）命令行下练习操作Redis的各种基本数据结构和命令。 

2. （选做）分别基于jedis，RedisTemplate，Lettuce，Redission实现redis基本操作 

   的demo，可以使用spring-boot集成上述工具。 

3. （选做）spring集成练习: 

   1）实现update方法，配合@CachePut 

   2）实现delete方法，配合@CacheEvict 

   3）将示例中的spring集成Lettuce改成jedis或redisson。 

4. （必做）基于Redis封装分布式数据操作： 

   1）在Java中实现一个简单的分布式锁； 

   2）在Java中实现一个分布式计数器，模拟减库存。 

   [redis-demo](https://github.com/Movosoft/JAVA-01/tree/main/Week_11/redis-demo)

5. 基于Redis的PubSub实现订单异步处理

   1、（挑战☆）基于其他各类场景，设计并在示例代码中实现简单demo： 

   ​	1）实现分数排名或者排行榜； 

   ​	2）实现全局ID生成； 

   ​	3）基于Bitmap实现id去重； 

   ​	4）基于HLL实现点击量计数。 

   ​	5）以redis作为数据库，模拟使用lua脚本实现前面课程的外汇交易事务。 

   2、（挑战☆☆）升级改造项目： 

   ​	1）实现guava cache的spring cache适配； 

   ​	2）替换jackson序列化为fastjson或者fst，kryo； 

   ​	3）对项目进行分析和性能调优。 

   3、（挑战☆☆☆）以redis作为基础实现上个模块的自定义rpc的注册中心。