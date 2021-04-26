package com.movo.rpc.core.server.register;

import java.net.UnknownHostException;

/**
 * @description 服务注册器，定义服务注册规范
 * @author Movo
 * @create 2021/4/20 9:00
 */
public interface ServiceRegister {
    /**
     * 服务注册
     * @param serviceObject
     * @throws IllegalArgumentException
     * @throws UnknownHostException
     */
    void register(ServiceObject serviceObject) throws IllegalArgumentException, UnknownHostException;

    /**
     * 根据服务名获取服务持有对象
     * @param name
     * @return
     */
    ServiceObject getServerObject(String name);
}
