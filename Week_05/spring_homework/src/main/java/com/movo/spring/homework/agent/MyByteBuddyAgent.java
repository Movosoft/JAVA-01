package com.movo.spring.homework.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

public class MyByteBuddyAgent {
    // Instrumention机制可以让开发者构建一个基于Java编写的Agent来监控或者操作JVM，
    // 它支持的功能在java.lang.instrument.Instrumentation接口中体现，如在类方法执行前后加入某些操作、重新定义类等。
    // ByteBuddy可以更简洁和直观地在java应用程序运行时创建和修改java类。
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("This is a byte buddy agent!");

        AgentBuilder.Transformer transformer = (DynamicType.Builder<?> builder,
                                                TypeDescription typeDescription,
                                                ClassLoader classLoader,
                                                JavaModule javaModule) -> {
            return builder
                    .method(ElementMatchers.any()) // 拦截任意方法
                    .intercept(MethodDelegation.to(TimeInterceptor.class)); // 委托执行
        };

        AgentBuilder.Listener listener = new AgentBuilder.Listener() {
            @Override
            public void onDiscovery(
                    String s,
                    ClassLoader classLoader,
                    JavaModule javaModule,
                    boolean b
            ) {}

            @Override
            public void onTransformation(
                    TypeDescription typeDescription,
                    ClassLoader classLoader,
                    JavaModule javaModule,
                    boolean b,
                    DynamicType dynamicType
            ) {}

            @Override
            public void onIgnored(
                    TypeDescription typeDescription,
                    ClassLoader classLoader,
                    JavaModule javaModule,
                    boolean b
            ) {}

            @Override
            public void onError(
                    String s,
                    ClassLoader classLoader,
                    JavaModule javaModule,
                    boolean b,
                    Throwable throwable

            ) {}

            @Override
            public void onComplete(
                    String s,
                    ClassLoader classLoader,
                    JavaModule javaModule,
                    boolean b
            ) {}
        };
        new AgentBuilder
                .Default()
                .type(ElementMatchers.nameStartsWith("com.movo.spring.homework")) // 指定需要拦截的类
                .transform(transformer)
                .with(listener)
                .installOn(inst);
    }
}
