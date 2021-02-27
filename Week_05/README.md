# 第五周作业

## 第九课 Java相关框架（1）

1. (选做)使用Java 里的动态代理，实现一个简单的 AOP。

   

3. (**必做**)写代码实现 Spring Bean 的装配，方式越多越好(XML、Annotation 都可以), 提交到 Github。

   [Item02.java](https://github.com/Movosoft/JAVA-01/tree/main/Week_05/spring_homework/src/test/java/com/movo/spring/homework/Item02.java)、[Department.java](https://github.com/Movosoft/JAVA-01/tree/main/Week_05/spring_homework/src/main/java/com/movo/spring/homework/bean/Department.java)、[Corporation.java](https://github.com/Movosoft/JAVA-01/tree/main/Week_05/spring_homework/src/main/java/com/movo/spring/homework/bean/Corporation.java)、[Staff.java](https://github.com/Movosoft/JAVA-01/tree/main/Week_05/spring_homework/src/main/java/com/movo/spring/homework/bean/Staff.java)、[applicationContext.xml](https://github.com/Movosoft/JAVA-01/tree/main/Week_05/spring_homework/src/main/resources/applicationContext.xml)

4. (选做)实现一个 Spring XML 自定义配置，配置一组 Bean，例如: Student/Klass/School。

   

5. (选做，会添加到高手附加题)

   4.1 (挑战)讲网关的 frontend/backend/filter/router 线程池都改造成 Spring 配置方式;

   

   4.2 (挑战)基于 AOP 改造 Netty 网关，filter 和 router 使用 AOP 方式实现;

   

   4.3 (中级挑战)基于前述改造，将网关请求前后端分离，中级使用 JMS 传递消息;

   

   4.4 (中级挑战)尝试使用 ByteBuddy 实现一个简单的基于类的 AOP;

   

   4.5 (超级挑战)尝试使用 ByteBuddy 与 Instrument 实现一个简单 JavaAgent 实现无侵入 下的 AOP。

   [MyByteBuddyAgentTest.java](https://github.com/Movosoft/JAVA-01/tree/main/Week_05/spring_homework/src/test/java/com/movo/spring/homework/MyByteBuddyAgentTest.java)、[MyByteBuddyAgent.java](https://github.com/Movosoft/JAVA-01/tree/main/Week_05/spring_homework/src/main/java/com/movo/spring/homework/agent/MyByteBuddyAgent.java)、[TimeInterceptor.java](https://github.com/Movosoft/JAVA-01/tree/main/Week_05/spring_homework/src/main/java/com/movo/spring/homework/agent/TimeInterceptor.java)、[MANIFEST.MF](https://github.com/Movosoft/JAVA-01/tree/main/Week_05/spring_homework/src/main/resources/META-INF/MANIFEST.MF)

## 第十课 Java相关框架（2）

1. (选做)总结一下，单例的各种写法，比较它们的优劣。

   

2. (选做)maven/spring 的 profile 机制，都有什么用法?

   

3. (**必做**)给前面课程提供的 Student/Klass/School 实现自动配置和 Starter。

    [homework_springboot](https://github.com/Movosoft/JAVA-01/tree/main/Week_05/homework_springboot)

    启动项目访问http://127.0.0.1:8800/showSchool

    网页显示{"klass":{"students":[{"id":100,"name":"张三"},{"id":200,"name":"李四"}]},"student":{"id":500,"name":"王五"}}

    控制台输出School(klass=Klass(students=[Student(id=100, name=张三), Student(id=200, name=李四)]), student=Student(id=500, name=王五))

4. (选做)总结 Hibernate 与 MyBatis 的各方面异同点。

    

5. (选做)学习 MyBatis-generator 的用法和原理，学会自定义 TypeHandler 处理复杂类型。 6. (必做)研究一下 JDBC 接口和数据库连接池，掌握它们的设计和用法:
    1)使用 JDBC 原生接口，实现数据库的增删改查操作。 2)使用事务，PrepareStatement 方式，批处理方式，改进上述操作。 3)配置 Hikari 连接池，改进上述操作。提交代码到 Github。

  

6. 附加题(可以后面上完数据库的课再考虑做):

  1. (挑战)基于 AOP 和自定义注解，实现 @MyCache(60) 对于指定方法返回值缓存60秒。

     

  2. (挑战)自定义实现一个数据库连接池，并整合 Hibernate/Mybatis/Spring/SpringBoot。

     

  3. (挑战)基于 MyBatis 实现一个简单的分库分表+读写分离+分布式 ID 生成方案。

     