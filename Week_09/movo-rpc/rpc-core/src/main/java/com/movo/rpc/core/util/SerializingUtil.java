package com.movo.rpc.core.util;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Description
 * @auther Movo
 * @create 2021/4/2 11:34
 */
// 序列化反序列化工具类
public class SerializingUtil {
    // 将目标序列化为byte数组
    public static <T> byte[] serializeByProtostuff(T source) {
        Schema<T> schema = RuntimeSchema.getSchema((Class<T>)source.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        final byte[] result;
        try {
            result = ProtobufIOUtil.toByteArray(source, schema, buffer);
        } finally {
            buffer.clear();
        }
        return result;
    }

    // 将byte数组序列化为目标类
    public static <T> T deserializeByProtostuff(byte[] source, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T obj = schema.newMessage();
        ProtobufIOUtil.mergeFrom(source, obj, schema);
        return obj;
    }

    public static <T> byte[] serializeByKryo(T source, Kryo kryo) {
        ByteArrayOutputStream bout = null;
        Output output = null;
        try {
            bout = new ByteArrayOutputStream();
            output = new Output(bout);
            kryo.writeClassAndObject(output, source);
            byte[] bytes = output.toBytes();
            output.flush();
            return bytes;
        } finally {
            if(output != null) {
                output.close();
            }
            if(bout != null) {
                try {
                    bout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static <T> T deserializeByKryo(byte[] data, Kryo kryo) {
        ByteArrayInputStream bin = null;
        Input input = null;
        try {
            bin = new ByteArrayInputStream(data);
            input = new Input(bin);
            T obj = (T)kryo.readClassAndObject(input);
            return obj;
        } finally {
            if(input != null) {
                input.close();
            }
            if(bin != null) {
                try {
                    bin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
