package com.movo.rpc.core.server;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.ReferenceCountUtil;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * RPC服务端Netty实现
 * @author Movo
 * @create 2021/4/2 15:44
 */
public class NettyRpcServer extends RpcServer {

    private Channel channel;
    /**
     * @Description:
     * @Author: Movo
     * @Date: 2021/4/2 15:59
     * @Param: corePoolSize – the number of threads to keep in the pool, even if they are idle, unless allowCoreThreadTimeOut is set
     *         maximumPoolSize – the maximum number of threads to allow in the pool
     *         keepAliveTime – when the number of threads is greater than the core, this is the maximum time that excess idle threads will wait for new tasks before terminating.
     *         unit – the time unit for the keepAliveTime argument
     *         workQueue – the queue to use for holding tasks before they are executed. This queue will hold only the Runnable tasks submitted by the execute method.
     *         threadFactory – the factory to use when the executor creates a new thread
     * @Return:
     */
    private static final ExecutorService pool = new ThreadPoolExecutor(
        4,
        8,
        200,
        TimeUnit.SECONDS,
        new LinkedBlockingDeque<>(1000),
        new ThreadFactoryBuilder().setNameFormat("rpcServer-%d").build()
    );

    public NettyRpcServer(int port, String protocol, RequestHandler requestHandler) {
        super(port, protocol, requestHandler);
    }

    @Override
    public void start() {
        final int cores = Runtime.getRuntime().availableProcessors();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(cores);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
        .group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG, 100)
        .handler(new LoggingHandler(LogLevel.INFO))
        .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new ChannelRequestHandler());
            }
        });
        try {
            // 启动服务
            ChannelFuture future = serverBootstrap.bind(port).sync();
            System.out.println("Server started successfully.");
            channel = future.channel();
            // 等待服务通道关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("start netty sever failed,msg:" + e.getMessage());
        } finally {
            // 释放线程组资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {
        channel.close();
    }

    private class ChannelRequestHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            System.out.println("Channel active:" + ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            pool.submit(() -> {
                System.out.println("the server receives message:" + msg);
                ByteBuf byteBuf = (ByteBuf)msg;
                // 消息写入reqData
                byte[] reqData = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(reqData);
                // 手动回收
                ReferenceCountUtil.release(byteBuf);
                try {
                    byte[] respData = requestHandler.handleRequest(reqData);
                    ByteBuf respBuf = Unpooled.buffer(respData.length);
                    respBuf.writeBytes(respData);
                    System.out.println("Send response:" + respBuf);
                    ctx.writeAndFlush(respBuf);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            System.out.println("Exception occurred:" + cause.getMessage());
            ctx.close();
        }
    }
}
