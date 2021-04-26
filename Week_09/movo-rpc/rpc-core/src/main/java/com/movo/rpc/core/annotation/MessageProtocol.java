package com.movo.rpc.core.annotation;

import java.lang.annotation.*;

/**
 * @Description
 * @auther Movo
 * @create 2021/4/1 11:33
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageProtocol {
    String value() default "";
}
