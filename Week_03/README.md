# 第三周作业

## 第三课 Netty原理与API网关

1. （**必做**）整合你上次作业的 httpclient/okhttp；

2. （**选做**）使用 netty 实现后端 http 访问（代替上一步骤）

## 第四课 Java并发编程(1)

1. （**必做**）实现过滤器。
2. （**选做**）实现路由。
3. （**选做**）跑一跑课上的各个例子，加深对多线程的理解
4. （**选做**）完善网关的例子，试着调整其中的线程池参数

根据上述要求最终完成代码，实现功能如下：

1. 监听本地8890端口，接收系统GET或POST的请求，其它请求均忽略。
2. 接收到请求http://127.0.0.1:8890?url=https://www.baidu.com，会将url后的地址在request头上加自定义Corporation属性，接着将url后的地址在response头上加自定义Author属性，最后忽略https请求证书，将url地址对应的内容作为响应转发出去。
3. 随机路由，或将url地址对应的内容作为响应转发出去，或将proxyServers中非http://127.0.0.1:8890地址内容解析并作为响应转发出去。
4. 使用org.apache.http.impl.nio.client.CloseableHttpAsyncClient,其自带线程池，设置io线程数为机器最大核心数，连接超时时间（建立连接的超时时间）5秒，socket超时时间（等待服务端响应数据的超时时间）2秒，接收缓冲区大小32 * 1024字节。

   [作业代码](https://github.com/Movosoft/JAVA-01/tree/main/Week_03/netty-gateway)

