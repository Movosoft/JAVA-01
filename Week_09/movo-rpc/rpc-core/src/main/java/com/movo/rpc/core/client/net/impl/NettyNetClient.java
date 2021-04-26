package com.movo.rpc.core.client.net.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.movo.rpc.core.client.net.NetClient;
import com.movo.rpc.core.client.net.handler.SendDataHandler;
import com.movo.rpc.core.client.net.handler.SendHandler;
import com.movo.rpc.core.common.model.RpcRequest;
import com.movo.rpc.core.common.model.RpcResponse;
import com.movo.rpc.core.common.model.Service;
import com.movo.rpc.core.common.protocol.MessageProtocol;
import com.movo.rpc.core.exception.RpcException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 网络请求客户端Netty实现
 * @author Movo
 * @create 2021/4/8 15:37
 */
public class NettyNetClient implements NetClient {

    private static ExecutorService threadPool = new ThreadPoolExecutor(
        4,
        8,
        200,
        TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(1000),
        new ThreadFactoryBuilder().setNameFormat("rpcClient-%d").build()
    );
    private EventLoopGroup loopGroup = new NioEventLoopGroup(4);

    /**
     * 已连接的服务缓存
     * key: 服务地址,格式: ip:port
     */
    public static Map<String, SendDataHandler> connectedServiceNodes = new ConcurrentHashMap<>();

    private Bootstrap configClient(EventLoopGroup loopGroup, ChannelInboundHandlerAdapter handler) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(loopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(handler);
                    }
                });
        return bootstrap;
    }

    @Override
    public byte[] sendRequest(byte[] data, Service service) {
        String address = service.getAddress();
        String[] addrInfo = address.split(":");
        final String serverAddress = addrInfo[0];
        final String serverPort = addrInfo[1];
        SendHandler sendHandler = new SendHandler(data);

        // 配置客户端
        EventLoopGroup loopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = configClient(loopGroup, sendHandler);
        byte[] respData = null;
        try {
            // 启用客户端连接
            bootstrap.connect(serverAddress, Integer.parseInt(serverPort)).sync();
            respData = (byte[])sendHandler.respData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            loopGroup.shutdownGracefully();
        }
        return respData;
    }

    @Override
    public RpcResponse sendRequest(RpcRequest rpcRequest, Service service, MessageProtocol messageProtocol) {
        String address = service.getAddress();
        synchronized (address) {
            if(connectedServiceNodes.containsKey(address)) {
                SendDataHandler handler = connectedServiceNodes.get(address);
                try {
                    return handler.sendRequest(rpcRequest);
                } catch (RpcException e) {
                    e.printStackTrace();
                }
            } else {
                String[] addrInfo = address.split(":");
                final String serverAddress = addrInfo[0];
                final String serverPort = addrInfo[1];

                final SendDataHandler handler = new SendDataHandler(address, messageProtocol);
                threadPool.submit(() -> {
                    // 配置客户端
                    Bootstrap bootstrap = configClient(loopGroup, handler);
                    // 启用客户端连接
                    ChannelFuture channelFuture = bootstrap.connect(serverAddress, Integer.parseInt(serverPort));
                    channelFuture.addListener((ChannelFutureListener) channelFuture1 -> connectedServiceNodes.put(address, handler));
                });
                try {
                    return handler.sendRequest(rpcRequest);
                } catch (RpcException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
