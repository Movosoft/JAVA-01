package com.movo.rpc.core.common.protocol;

import com.movo.rpc.core.common.model.RpcRequest;
import com.movo.rpc.core.common.model.RpcResponse;

import java.io.IOException;

/**
 * @Description 消息协议：定义编组请求、解组请求、编组响应、解组响应的规范
 * @author Movo
 * @create 2021/4/1 9:28
 */
public interface MessageProtocol {

    /**
     * 编组请求
     * @param request
     * @return
     * @throws IOException
     */
    byte[] marshallingRequest(RpcRequest request) throws IOException;

    /**
     * 解组请求
     * @param data
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    RpcRequest unmarshallingRequest(byte[] data) throws IOException, ClassNotFoundException;

    /**
     * 编组响应
     * @param response
     * @return
     * @throws IOException
     */
    byte[] marshallingResponse(RpcResponse response) throws IOException;

    /**
     * 解组响应
     * @param data
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    RpcResponse unmarshallingResponse(byte[] data) throws IOException, ClassNotFoundException;
}
