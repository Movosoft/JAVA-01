package com.movo.rpc.core.common.protocol.impl;

import com.movo.rpc.core.common.constants.RpcConstant;
import com.movo.rpc.core.common.model.RpcRequest;
import com.movo.rpc.core.common.model.RpcResponse;
import com.movo.rpc.core.common.protocol.MessageProtocol;
import com.movo.rpc.core.util.SerializingUtil;

/**
 * Protobuf序列化协议
 * @author Movo
 * @create 2021/4/2 11:24
 */
@com.movo.rpc.core.annotation.MessageProtocol(RpcConstant.PROTOCOL_PROTOBUF)
public class ProtoBufMessageProtocol implements MessageProtocol {
    @Override
    public byte[] marshallingRequest(RpcRequest request) {
        return SerializingUtil.serializeByProtostuff(request);
    }

    @Override
    public RpcRequest unmarshallingRequest(byte[] data) {
        return SerializingUtil.deserializeByProtostuff(data, RpcRequest.class);
    }

    @Override
    public byte[] marshallingResponse(RpcResponse response) {
        return SerializingUtil.serializeByProtostuff(response);
    }

    @Override
    public RpcResponse unmarshallingResponse(byte[] data) {
        return SerializingUtil.deserializeByProtostuff(data, RpcResponse.class);
    }
}
