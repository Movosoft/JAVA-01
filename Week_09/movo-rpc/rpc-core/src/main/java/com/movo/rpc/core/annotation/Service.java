package com.movo.rpc.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Description: 被该注解编辑的服务可提供RPC功能
 * @Author: Movo
 * @Date: 2021/4/19 15:05
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface Service {
    String value() default "";
}
