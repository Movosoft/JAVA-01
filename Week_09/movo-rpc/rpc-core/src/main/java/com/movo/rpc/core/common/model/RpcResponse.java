package com.movo.rpc.core.common.model;

import com.movo.rpc.core.common.constants.RpcStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装rpc响应，并支持序列化、反序列化
 * @author Movo
 * @create 2021/4/1 9:50
 */
@Getter
@Setter
public class RpcResponse implements Serializable {
    private String requestId;
    /**
     * rpc消息响应头
     */
    private Map<String, String> headers = new HashMap<>();
    /**
     * rpc响应返回结果
     */
    private Object returnValue;
    /**
     * rpc响应异常信息
     */
    private Exception exception;
    /**
     * rpc响应状态
     */
    private RpcStatusEnum rpcStatus;

    public RpcResponse(RpcStatusEnum rpcStatus) {
        this.rpcStatus = rpcStatus;
    }
}
