package com.movo.rpc.core.server.register;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Movo
 * @create 2021/3/31 14:55
 */
public class DefaultServiceRegister implements ServiceRegister {

    private Map<String, ServiceObject> serviceMap = new HashMap<>();

    protected String protocol;
    protected Integer port;
    protected Integer weight;

    @Override
    public void register(ServiceObject serviceObject) throws IllegalArgumentException, UnknownHostException {
        if(serviceObject == null) {
            throw new IllegalArgumentException("参数serverObject不能为空");
        }
        serviceMap.put(serviceObject.getName(), serviceObject);
    }

    @Override
    public ServiceObject getServerObject(String name) {
        return serviceMap.get(name);
    }
}
