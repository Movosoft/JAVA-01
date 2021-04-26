package com.movo.rpc.core.annotation;

import java.lang.annotation.*;

/**
 * 该注解用于注入远程服务
 * @author Movo
 * @create 2021/4/19 15:38
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectService {
}
