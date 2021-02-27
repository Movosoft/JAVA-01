package com.movo.spring.homework;

public class MyByteBuddyAgentTest {
    private void fun1() {
        System.out.println("MyByteBuddyAgentTest fun1.");
    }

    private void fun2() {
        System.out.println("MyByteBuddyAgentTest fun2.");
    }

    // 先在项目目录下执行mvn clean package将代理类打包
    // 再配置执行时的JVM参数 -javaagent:/Volumes/mac资料卷/workspace/java-agent/target/byteBuddy-agent.jar
    /*
        在每个函数执行结束后给出总耗时。
        执行结果如下：
        This is a byte buddy agent!
        MyByteBuddyAgentTest fun1.
        private void com.movo.spring.homework.MyByteBuddyAgentTest.fun1(): cost 0ms.
        MyByteBuddyAgentTest fun2.
        private void com.movo.spring.homework.MyByteBuddyAgentTest.fun2(): cost 0ms.
        public static void com.movo.spring.homework.MyByteBuddyAgentTest.main(java.lang.String[]): cost 0ms.
     */
    public static void main(String[] args) {
        MyByteBuddyAgentTest test = new MyByteBuddyAgentTest();
        test.fun1();
        test.fun2();
    }
}
