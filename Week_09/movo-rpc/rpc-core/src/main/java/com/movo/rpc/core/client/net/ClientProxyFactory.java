package com.movo.rpc.core.client.net;

import com.movo.rpc.core.client.balance.LoadBalance;
import com.movo.rpc.core.client.discovery.ServiceDiscovery;
import com.movo.rpc.core.client.cache.ServerDiscoveryCache;
import com.movo.rpc.core.common.model.RpcRequest;
import com.movo.rpc.core.common.model.RpcResponse;
import com.movo.rpc.core.common.model.Service;
import com.movo.rpc.core.common.protocol.MessageProtocol;
import com.movo.rpc.core.exception.RpcException;
import lombok.Getter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 客户端代理工厂：用于创建远程服务代理类
 * 封装编组请求、请求发送、编组响应等操作
 * @author Movo
 * @create 2021/4/6 11:27
 */
public class ClientProxyFactory {
    private final Map<Class<?>, Object> objectCache = new HashMap<>();
    @Getter
    private ServiceDiscovery serviceDiscovery;
    private Map<String, MessageProtocol> supportMessageProtocols;
    private LoadBalance loadBalance;
    private NetClient netClient;

    public ClientProxyFactory(final ServiceDiscovery serviceDiscovery, final NetClient netClient, final Map<String, MessageProtocol> supportMessageProtocols, final LoadBalance loadBalance) {
        this.serviceDiscovery = serviceDiscovery;
        this.netClient = netClient;
        this.supportMessageProtocols = supportMessageProtocols;
        this.loadBalance = loadBalance;
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T)objectCache.computeIfAbsent(
            clazz,
            clz -> Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{ clz }, new ClientInvocationHandler(clz))
        );
    }

    /**
     * 根据服务名获取可用的服务基本信息列表
     * @param serviceName
     * @return
     * @throws RpcException
     */
    private List<Service> getServiceList(String serviceName) throws RpcException {
        List<Service> services;
        synchronized (serviceName) {
            if(ServerDiscoveryCache.isEmpty(serviceName)) {
                services = serviceDiscovery.findServiceList(serviceName);
                if(services == null || services.size() == 0) {
                    throw new RpcException("No provider available!");
                }
                ServerDiscoveryCache.put(serviceName, services);
            } else {
                services = ServerDiscoveryCache.get(serviceName);
            }
        }
        return services;
    }

    private class ClientInvocationHandler implements InvocationHandler {

        private Class<?> clazz;

        public ClientInvocationHandler(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws RpcException {
            if(method.getName().equals("toString")) {
                return proxy.toString();
            }
            if(method.getName().equals("hashCode")) {
                return 0;
            }
            // 获取服务信息
            String serviceName = clazz.getName();
            List<Service> services = getServiceList(serviceName);
            Service service = loadBalance.chooseOne(services);
            // 构造request对象
            RpcRequest request = new RpcRequest();
            request.setRequestId(UUID.randomUUID().toString());
            request.setServiceName(service.getName());
            request.setMethod(method.getName());
            request.setParameters(args);
            request.setParameterTypes(method.getParameterTypes());
            // 发送请求
            MessageProtocol messageProtocol = supportMessageProtocols.get(service.getProtocol());
            RpcResponse response = netClient.sendRequest(request, service, messageProtocol);
            if(response == null) {
                throw new RpcException("the response is null");
            }
            // 处理结果
            if(response.getException() != null) {
                return response.getException();
            }
            return response.getReturnValue();
        }
    }
}
