package com.movo.rpc.core.client.net;

import com.movo.rpc.core.common.model.RpcRequest;
import com.movo.rpc.core.common.model.RpcResponse;
import com.movo.rpc.core.common.model.Service;
import com.movo.rpc.core.common.protocol.MessageProtocol;

/**
 * 网络请求客户端，定义请求规范
 * @author Movo
 * @date 2021/4/20 15:28
 */
public interface NetClient {
    /**
     * 客户端发送编组后的RPC请求,并获取响应结果
     * @param data
     * @param service
     * @return
     */
    byte[] sendRequest(byte[] data, Service service);

    /**
     * 客户端发送未编组的RPC请求,并获取响应结果
     * @param rpcRequest
     * @param service
     * @param messageProtocol
     * @return
     */
    RpcResponse sendRequest(RpcRequest rpcRequest, Service service, MessageProtocol messageProtocol);
}
