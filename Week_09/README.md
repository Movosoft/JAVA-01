# 第九周作业

## 第十七课 分布式服务-RPC与分布式服务化

1. （选做）实现简单的Protocol Buffer/Thrift/gRPC(选任一个)远程调用demo。

2. （选做）实现简单的WebService-Axis2/CXF远程调用demo。

3. （必做）改造自定义RPC的程序，提交到github：

   1）尝试将服务端写死查找接口实现类变成泛型和反射
   2）尝试将客户端动态代理改成AOP，添加异常处理
   3）尝试使用Netty+HTTP作为client端传输方式

   ```
   ServiceRegister - 服务注册器（接口），注册服务、获取服务
       DefaultServiceRegister实现
       	把服务持有对象（ServiceObject）存在本地单例对象的map中。
       ZookeeperServiceRegister实现
       	继承DefaultServiceRegister，并将服务注册至Zookeeper。
   MessageProtocol - 消息协议：定义编组请求、解组请求、编组响应、解组响应的规范
   LoadBalance - 负载均衡算法
   RequestHandler - 请求处理者，提供解组请求、编组响应等操作
       过来的请求是编组后的，因此要先解组请求消息，解组后得到RpcRequest，通过服务名查找到服务，然后利用反射调用服务相关方法，得到结果，编组响应并返回。
   RpcServer - RPC服务端抽象类，提供rpc服务端启动功能接口
       NettyRpcServer实现 - RPC服务端Netty实现
   ServerDiscovery - 服务发现抽象类，根据服务名称发现服务列表
       ZookeeperServiceDiscovery实现 - 服务发现zookeeper实现，使用Zookeeper客户端，通过服务名获取服务列表，服务名格式: 接口全路径。
   RpcFuture - 异步获取响应结果
   SendHandler - 发送处理类，发送编组后的RPC请求,并获取响应结果
   SendDataHandler - 发送处理类，发送未编组的RPC请求,并获取响应结果
   NetClient - 网络请求客户端，定义请求规范，客户端发送RPC请求
   	NettyNetClient实现 - 发送网络请求，并接收响应结果
   ClientProxyFactory - 客户端代理工厂：用于创建远程服务代理类、封装编组请求、请求发送、编组响应等操作
   DefaultRpcProcessor - RPC处理者，支持服务启动暴露，自动注入Service
           
   调用过程
   服务提供方 - P
   服务调用方 - C
   SPI的全名为Service Provider Interface.
   
   C的spring boot在启动时，会检查全部包下的META-INF/spring.factories文件，这样就会检测到rpc-core中resources/META-INF下的spring.factories，并自动装配org.springframework.boot.autoconfigure.EnableAutoConfiguration指定的类RpcAutoConfig。这些类包括RpcProperty、ServiceRegister、RequestHandler、RpcServer、ClientProxyFactory、DefaultRpcProcessor。
   C中被注解为@InjectService的属性会被DefaultRpcProcessor中的injectService方法找到，并用ClientProxyFactory将RPC代理类实例注入为此属性的值。
   
   P的spring boot在启动时也会向C的启动时一样自动装配RpcAutoConfig中的类。P中被注解为@Service的类会被DefaultRpcProcessor中的startService方法找到，注册到zookeeper中。
   ```

4. （选做☆☆）升级自定义RPC的程序：
   1）尝试使用压测并分析优化RPC性能
   2）尝试使用Netty+TCP作为两端传输方式
   3）尝试自定义二进制序列化
   4）尝试压测改进后的RPC并分析优化，有问题欢迎群里讨论
   5）尝试将fastjson改成xstream
   6）尝试使用字节码生成方式代替服务端反射

## 第十八课 分布式服务-Dubbo技术详解

1. （选做）按课程第二部分练习各个技术点的应用。 

2. （选做）按dubbo-samples项目的各个demo学习具体功能使用。 

3. （必做）结合dubbo+hmily，实现一个TCC外汇交易处理，代码提交到github： 

   1）用户A的美元账户和人民币账户都在A库，A使用1美元兑换7人民币； 

   2）用户B的美元账户和人民币账户都在B库，B使用7人民币兑换1美元； 

   3）设计账户表，冻结资产表，实现上述两个本地事务的分布式事务。 

   

4. （挑战☆☆）尝试扩展Dubbo 

   1）基于上次作业的自定义序列化，实现Dubbo的序列化扩展； 

   2）基于上次作业的自定义RPC，实现Dubbo的RPC扩展； 

   3）在Dubbo的filter机制上，实现REST权限控制，可参考dubbox； 

   4）实现一个自定义Dubbo的Cluster/Loadbalance扩展，如果一分钟内调用某个服务/ 提供者超过10次，则拒绝提供服务直到下一分钟； 

   5）整合Dubbo+Sentinel，实现限流功能； 

   6）整合Dubbo与Skywalking，实现全链路性能监控。