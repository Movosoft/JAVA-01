package com.movo.rpc.core.common.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装rpc请求，并支持序列化、反序列化
 * @author Movo
 * @create 2021/4/1 9:45
 */
@Getter
@Setter
public class RpcRequest implements Serializable {
    private String requestId;
    /**
     * 请求的服务名
     */
    private String serviceName;
    /**
     * 请求调用的方法名
     */
    private String method;
    /**
     * 请求调用的方法参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 请求调用的方法参数值
     */
    private Object[] parameters;
    /**
     * 请求头信息
     */
    private Map<String, String> headers = new HashMap<>();
}
