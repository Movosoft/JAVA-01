package com.movo.rpc.core.server;

import com.movo.rpc.core.common.constants.RpcStatusEnum;
import com.movo.rpc.core.common.model.RpcRequest;
import com.movo.rpc.core.common.model.RpcResponse;
import com.movo.rpc.core.common.protocol.MessageProtocol;
import com.movo.rpc.core.server.register.ServiceObject;
import com.movo.rpc.core.server.register.ServiceRegister;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @description 请求处理者，提供解组请求、编组响应等操作
 * @author Movo
 * @create 2021/4/1 9:27
 */
public class RequestHandler {
    private MessageProtocol protocol;
    private ServiceRegister serviceRegister;

    public RequestHandler(MessageProtocol protocol, ServiceRegister serviceRegister) {
        this.protocol = protocol;
        this.serviceRegister = serviceRegister;
    }

    public byte[] handleRequest(byte[] data) throws IOException, ClassNotFoundException {
        // 解组请求消息
        RpcRequest request = protocol.unmarshallingRequest(data);
        // 查找服务
        ServiceObject serverObject = serviceRegister.getServerObject(request.getServiceName());
        RpcResponse response;
        if(serverObject == null) {
            response = new RpcResponse(RpcStatusEnum.NOT_FOUND);
        } else {
            try {
                Method method = serverObject.getClazz().getMethod(request.getMethod(), request.getParameterTypes());
                Object returnValue = method.invoke(serverObject.getObj(), request.getParameters());
                response = new RpcResponse(RpcStatusEnum.SUCCESS);
                response.setReturnValue(returnValue);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                response = new RpcResponse(RpcStatusEnum.ERROR);
                response.setException(e);
            }
        }
        response.setRequestId(request.getRequestId());
        // 编组响应消息
        return this.protocol.marshallingResponse(response);
    }
}
