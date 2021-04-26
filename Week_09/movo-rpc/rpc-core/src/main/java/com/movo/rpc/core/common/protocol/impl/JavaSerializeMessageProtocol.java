package com.movo.rpc.core.common.protocol.impl;

import com.movo.rpc.core.common.constants.RpcConstant;
import com.movo.rpc.core.common.model.RpcRequest;
import com.movo.rpc.core.common.model.RpcResponse;
import com.movo.rpc.core.common.protocol.MessageProtocol;

import java.io.*;

/**
 * java序列化消息协议
 * @author Movo
 * @create 2021/4/1 17:07
 */
@com.movo.rpc.core.annotation.MessageProtocol(RpcConstant.PROTOCOL_JAVA)
public class JavaSerializeMessageProtocol implements MessageProtocol {

    private byte[] serialize(Object o) throws IOException {
        ByteArrayOutputStream bout = null;
        ObjectOutputStream out = null;
        try {
            bout = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bout);
            out.writeObject(o);
            return bout.toByteArray();
        } finally {
            if(out != null) {
                out.close();
            }
            if(bout != null) {
                bout.close();
            }
        }
    }

    @Override
    public byte[] marshallingRequest(RpcRequest request) throws IOException {
        return this.serialize(request);
    }

    @Override
    public RpcRequest unmarshallingRequest(byte[] data) throws IOException, ClassNotFoundException {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new ByteArrayInputStream(data));
            return (RpcRequest) in.readObject();
        } finally {
            if(in != null) {
                in.close();
            }
        }
    }

    @Override
    public byte[] marshallingResponse(RpcResponse response) throws IOException {
        return this.serialize(response);
    }

    @Override
    public RpcResponse unmarshallingResponse(byte[] data) throws IOException, ClassNotFoundException {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new ByteArrayInputStream(data));
            return (RpcResponse) in.readObject();
        } finally {
            if(in != null) {
                in.close();
            }
        }
    }
}
