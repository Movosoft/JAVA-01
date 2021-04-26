package com.movo.rpc.core.server.register;

import com.movo.rpc.core.annotation.InjectService;
import com.movo.rpc.core.annotation.Service;
import com.movo.rpc.core.client.cache.ServerDiscoveryCache;
import com.movo.rpc.core.client.discovery.ZkChildListenerImpl;
import com.movo.rpc.core.client.discovery.ZookeeperServiceDiscovery;
import com.movo.rpc.core.client.net.ClientProxyFactory;
import com.movo.rpc.core.common.constants.RpcConstant;
import com.movo.rpc.core.server.RpcServer;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Objects;

/**
 * RPC处理者，支持服务启动暴露，自动注入Service
 * @author Movo
 * @create 2021/4/19 10:19
 */
public class DefaultRpcProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private ClientProxyFactory clientProxyFactory;
    private ServiceRegister serviceRegister;
    private RpcServer rpcServer;

    public DefaultRpcProcessor(ClientProxyFactory clientProxyFactory, ServiceRegister serviceRegister, RpcServer rpcServer) {
        this.clientProxyFactory = clientProxyFactory;
        this.serviceRegister = serviceRegister;
        this.rpcServer = rpcServer;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Spring启动完毕过后会受到一个实践通知
        if(Objects.isNull(event.getApplicationContext().getParent())) {
            ApplicationContext context = event.getApplicationContext();
            // 开启服务
            startService(context);
            // 注入Service
            injectService(context);
        }
    }

    private void startService(ApplicationContext context) {
        Map<String, Object> beans = context.getBeansWithAnnotation(Service.class);
        if(beans.size() > 0) {
            boolean startServiceFlag = true;
            Class<?> clazz;
            Class<?>[] interfaces;
            ServiceObject serviceObject = null;
            Class<?> tempClazz;
            Service tempService;
            String tempServiceName;
            for(Object obj : beans.values()) {
                clazz = obj.getClass();
                interfaces = clazz.getInterfaces();
                /**
                 * 如果只实现了一个接口就用接口的className作为服务名
                 * 如果该类实现了多个接口，则用注解里的value作为服务名
                 */
                int intLen = interfaces.length;
                if(intLen == 1) {
                    tempClazz = interfaces[0];
                    serviceObject = new ServiceObject(tempClazz.getName(), tempClazz, obj);
                } else if (intLen > 1){
                    tempService = clazz.getAnnotation(Service.class);
                    tempServiceName = tempService.value();
                    if("".equals(tempServiceName)) {
                        startServiceFlag = false;
                        System.out.println("没有开放的RPC接口 '" + obj.getClass().getName() + "'");
                        break;
                    }
                    try {
                        serviceObject = new ServiceObject(tempServiceName, Class.forName(tempServiceName), obj);
                    } catch (ClassNotFoundException e) {
                        e.getMessage();
                        startServiceFlag = false;
                    }
                }
                try {
                    serviceRegister.register(serviceObject);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    startServiceFlag = false;
                }
            }
            if(startServiceFlag) {
                rpcServer.start();
            }
        }
    }

    private void injectService(ApplicationContext context) {
        // 从应用程序上下文中取出所有bean的名称
        String[] names = context.getBeanDefinitionNames();
        Class<?> clazz;
        Class<?> fieldClass;
        Object tempObj;
        for(String name : names) {
            // 根据bean的名称找出其对应的类
            clazz = context.getType(name);
            if(Objects.isNull(clazz)) {
                continue;
            }
            Field[] declaredFields = clazz.getDeclaredFields();
            for(Field field : declaredFields) {
                // 找出标记了InjectService注解的属性
                InjectService injectService = field.getAnnotation(InjectService.class);
                if(injectService == null) {
                    continue;
                }
                // 找出被标记属性对应的类
                fieldClass = field.getType();
                // 找出包含此属性的bean的实例
                tempObj = context.getBean(name);
                // 设置允许通过反射方法访问这个被标记的私有字段
                field.setAccessible(true);
                try {
                    // 把标记为InjectService注解的属性值set为RPC代理类
                    field.set(tempObj, clientProxyFactory.getProxy(fieldClass));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                ServerDiscoveryCache.SERVICE_CLASS_NAMES.add(fieldClass.getName());
            }
        }
        // 注册子节点监听
        if(clientProxyFactory.getServiceDiscovery() instanceof ZookeeperServiceDiscovery) {
            ZookeeperServiceDiscovery serverDiscovery = (ZookeeperServiceDiscovery)clientProxyFactory.getServiceDiscovery();
            ZkClient zkClient = serverDiscovery.getZkClient();
            ServerDiscoveryCache.SERVICE_CLASS_NAMES.forEach(name -> {
                String servicePath = RpcConstant.ZK_SERVICE_PATH + RpcConstant.PATH_DELIMITER + name + RpcConstant.PATH_DELIMITER + "service";
                zkClient.subscribeChildChanges(servicePath, new ZkChildListenerImpl());
            });
        }
    }
}
