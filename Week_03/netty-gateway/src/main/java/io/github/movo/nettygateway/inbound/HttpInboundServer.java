package io.github.movo.nettygateway.inbound;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.List;

/**
 * @Description
 * @auther Movo
 * @create 2021/1/25 14:13
 */
public class HttpInboundServer {

    private int port;
    private List<String> proxyServers;

    public HttpInboundServer(final int port, final List<String> proxyServers) {
        this.port = port;
        this.proxyServers = proxyServers;
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(16);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128) // 服务端可连接队列长度
                           .childOption(ChannelOption.TCP_NODELAY, true) // 禁止使用Nagle算法，使用于小数据即时传输
                           .childOption(ChannelOption.SO_KEEPALIVE, true) // 周期性测试连接是否仍存活
                           .childOption(ChannelOption.SO_REUSEADDR, true) // 允许重用本地地址
                           .childOption(ChannelOption.SO_RCVBUF, 32 * 1024) // 接收缓冲区大小
                           .childOption(ChannelOption.SO_SNDBUF, 32 * 1024) // 发送缓冲区大小
                           .childOption(EpollChannelOption.SO_REUSEPORT, true) // 允许重用本地端口
                           .childOption(ChannelOption.SO_KEEPALIVE, true) // 周期性测试连接是否仍存活
                           .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT); // Netty4使用对象池，重用缓冲区
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpInboundInitializer(proxyServers));
            Channel ch = serverBootstrap.bind(port).sync().channel();
            System.out.println("开启netty http服务器，监听地址和端口为 http://127.0.0.1:" + port + '/');
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
