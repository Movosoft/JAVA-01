package com.movo.rpc.core.config;

import com.movo.rpc.core.client.balance.LoadBalance;
import com.movo.rpc.core.client.discovery.ServiceDiscovery;
import com.movo.rpc.core.client.discovery.ZookeeperServiceDiscovery;
import com.movo.rpc.core.client.net.ClientProxyFactory;
import com.movo.rpc.core.client.net.NetClient;
import com.movo.rpc.core.client.net.impl.NettyNetClient;
import com.movo.rpc.core.common.protocol.MessageProtocol;
import com.movo.rpc.core.config.properties.RpcProperty;
import com.movo.rpc.core.exception.RpcException;
import com.movo.rpc.core.server.NettyRpcServer;
import com.movo.rpc.core.server.RequestHandler;
import com.movo.rpc.core.server.RpcServer;
import com.movo.rpc.core.server.register.DefaultRpcProcessor;
import com.movo.rpc.core.server.register.ServiceRegister;
import com.movo.rpc.core.server.register.ZookeeperServiceRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @description 自动装配类
 * @author Movo
 * @create 2021/3/31 14:22
 */
@Configuration
@EnableConfigurationProperties(RpcProperty.class)
public class RpcAutoConfig {

    @Bean
    public RpcProperty rpcProperty() {
        return new RpcProperty();
    }

    @Bean
    public ServiceRegister serverRegister(@Autowired RpcProperty rpcProperty) {
        return new ZookeeperServiceRegister(
                rpcProperty.getRegisterAddress(),
                rpcProperty.getServerPort(),
                rpcProperty.getProtocol(),
                rpcProperty.getWeight());
    }

    @Bean
    public RequestHandler requestHandler(@Autowired ServiceRegister serviceRegister, @Autowired RpcProperty rpcProperty) throws RpcException {
        return new RequestHandler(getMessageProtocol(rpcProperty.getProtocol()), serviceRegister);
    }

    private MessageProtocol getMessageProtocol(String name) throws RpcException {
        ServiceLoader<MessageProtocol> loader = ServiceLoader.load(MessageProtocol.class);
        Iterator<MessageProtocol> iterator = loader.iterator();
        MessageProtocol messageProtocol;
        while(iterator.hasNext()) {
            messageProtocol = iterator.next();
            com.movo.rpc.core.annotation.MessageProtocol ano = messageProtocol.getClass().getAnnotation(com.movo.rpc.core.annotation.MessageProtocol.class);
            Assert.notNull(ano, "rpc消息协议名称不能为空!");
            if(name.equals(ano.value())) {
                return messageProtocol;
            }
        }
        throw new RpcException("无效的消息协议配置");
    }

    @Bean
    public RpcServer rpcServer(@Autowired RequestHandler requestHandler, @Autowired RpcProperty rpcProperty) {
        return new NettyRpcServer(rpcProperty.getServerPort(), rpcProperty.getProtocol(), requestHandler);
    }

    @Bean
    public ClientProxyFactory proxyFactory(@Autowired RpcProperty rpcProperty) throws RpcException {
        // 设置服务发现者
        ServiceDiscovery serviceDiscovery = new ZookeeperServiceDiscovery(rpcProperty.getRegisterAddress());
        // 设置支持的协议
        Map<String, MessageProtocol> supportMessageProtocols = buildSupportMessageProtocols();
        // 设置负载均衡算法
        LoadBalance loadBalance = getLoadBalance(rpcProperty.getLoadBalance());
        // 设置网络层实现
        NetClient netClient = new NettyNetClient();

        ClientProxyFactory clientProxyFactory = new ClientProxyFactory(serviceDiscovery, netClient, supportMessageProtocols, loadBalance);
        return clientProxyFactory;
    }

    private Map<String, MessageProtocol> buildSupportMessageProtocols() {
        Map<String, MessageProtocol> supportMessageProtocols = new HashMap<>();
        ServiceLoader<MessageProtocol> loader = ServiceLoader.load(MessageProtocol.class);
        Iterator<MessageProtocol> iterator = loader.iterator();
        MessageProtocol messageProtocol;
        while(iterator.hasNext()) {
            messageProtocol = iterator.next();
            com.movo.rpc.core.annotation.MessageProtocol ano = messageProtocol.getClass().getAnnotation(com.movo.rpc.core.annotation.MessageProtocol.class);
            Assert.notNull(ano, "消息协议名称不能为空！");
            supportMessageProtocols.put(ano.value(), messageProtocol);
        }
        return supportMessageProtocols;
    }

    private LoadBalance getLoadBalance(String name) throws RpcException {
        ServiceLoader<LoadBalance> loader = ServiceLoader.load(LoadBalance.class);
        Iterator<LoadBalance> iterator = loader.iterator();
        LoadBalance loadBalance;
        while(iterator.hasNext()) {
            loadBalance = iterator.next();
            com.movo.rpc.core.annotation.LoadBalance ano = loadBalance.getClass().getAnnotation(com.movo.rpc.core.annotation.LoadBalance.class);
            Assert.notNull(ano, "负载均衡名称不能为空");
            if(name.equals(ano.value())) {
                return loadBalance;
            }
        }
        throw new RpcException("无效的负载均衡配置！");
    }

    @Bean
    public DefaultRpcProcessor rpcProcessor(
        @Autowired ClientProxyFactory clientProxyFactory,
        @Autowired ServiceRegister serviceRegister,
        @Autowired RpcServer rpcServer
    ) {
        return new DefaultRpcProcessor(clientProxyFactory, serviceRegister, rpcServer);
    }
}
