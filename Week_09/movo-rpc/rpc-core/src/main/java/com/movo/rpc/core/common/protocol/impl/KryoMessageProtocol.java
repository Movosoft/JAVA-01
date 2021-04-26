package com.movo.rpc.core.common.protocol.impl;

import com.esotericsoftware.kryo.Kryo;
import com.movo.rpc.core.common.constants.RpcConstant;
import com.movo.rpc.core.common.model.RpcRequest;
import com.movo.rpc.core.common.model.RpcResponse;
import com.movo.rpc.core.common.protocol.MessageProtocol;
import com.movo.rpc.core.util.SerializingUtil;
import org.objenesis.strategy.StdInstantiatorStrategy;

/**
 * kryo实现序列化和反序列化,kryo不是线程安全的
 * @author Movo
 * @create 2021/4/2 14:13
 */
@com.movo.rpc.core.annotation.MessageProtocol(RpcConstant.PROTOCOL_KRYO)
public class KryoMessageProtocol implements MessageProtocol {

    private static final ThreadLocal<Kryo> kryoLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(RpcRequest.class);
        kryo.register(RpcResponse.class);
        Kryo.DefaultInstantiatorStrategy strategy = (Kryo.DefaultInstantiatorStrategy)kryo.getInstantiatorStrategy();
        strategy.setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
        return kryo;
    });

    public static Kryo getInstance() {
        return kryoLocal.get();
    }

    @Override
    public byte[] marshallingRequest(RpcRequest request) {
        return SerializingUtil.serializeByKryo(request, getInstance());
    }

    @Override
    public RpcRequest unmarshallingRequest(byte[] data) {
        return SerializingUtil.deserializeByKryo(data, getInstance());
    }

    @Override
    public byte[] marshallingResponse(RpcResponse response) {
        return SerializingUtil.serializeByKryo(response, getInstance());
    }

    @Override
    public RpcResponse unmarshallingResponse(byte[] data) {
        return SerializingUtil.deserializeByKryo(data, getInstance());
    }
}
