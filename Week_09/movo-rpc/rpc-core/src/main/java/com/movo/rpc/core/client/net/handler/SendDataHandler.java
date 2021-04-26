package com.movo.rpc.core.client.net.handler;

import com.movo.rpc.core.client.net.RpcFuture;
import com.movo.rpc.core.client.net.impl.NettyNetClient;
import com.movo.rpc.core.common.model.RpcRequest;
import com.movo.rpc.core.common.model.RpcResponse;
import com.movo.rpc.core.common.protocol.MessageProtocol;
import com.movo.rpc.core.exception.RpcException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 发送处理类升级版，定义Netty入站处理细则
 *  ChannelInboundHandlerAdapter生命周期
 *      handlerAdded: handler被添加到channel的pipeline
 *      channelRegistered: channel注册到NioEventLoop
 *      channelActive: channel准备就绪
 *      channelRead: channel中有可读的数据
 *      channelReadComplete: channel读数据完成
 *      channelInactive: channel被关闭
 *      channelUnregistered: channel取消和NioEventLoop的绑定
 *      handlerRemoved: handler从channel的pipeline中移除
 * @author Movo
 * @create 2021/4/9 9:31
 */
public class SendDataHandler extends ChannelInboundHandlerAdapter {
    /**
     * 等待通道建立的最大时间
     */
    static final int CHANNEL_WAIT_TIME = 4;
    /**
     * 等待响应的最大时间
     */
    static final int RESPONSE_WAIT_TIME = 8;
    private static Map<String, RpcFuture<RpcResponse>> requestMap = new ConcurrentHashMap<>();
    private volatile Channel channel;
    private String remoteAddress;
    private MessageProtocol messageProtocol;
    private CountDownLatch cdl = new CountDownLatch(1);

    public SendDataHandler(String remoteAddress, MessageProtocol messageProtocol) {
        this.remoteAddress = remoteAddress;
        this.messageProtocol = messageProtocol;
    }

    /**
     * 1
     * @param ctx
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        this.channel = ctx.channel();
        cdl.countDown();
    }

    /**
     * 2
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Connect to server successfully:" + ctx);
    }

    /**
     * 4
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        System.out.println("Exception occurred:" + cause.getMessage());
        ctx.close();
    }

    /**
     * 5
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("channel inactive with remoteAddress:[" + remoteAddress + "]");
        NettyNetClient.connectedServiceNodes.remove(remoteAddress);
    }

    /**
     * 3
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Client reads message:" + msg);
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] resp = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(resp);
        // 手动回收
        ReferenceCountUtil.release(byteBuf);
        RpcResponse response = messageProtocol.unmarshallingResponse(resp);
        RpcFuture<RpcResponse> future = requestMap.get(response.getRequestId());
        future.setResponse(response);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    public RpcResponse sendRequest(RpcRequest request) throws RpcException {
        RpcResponse response;
        RpcFuture<RpcResponse> future = new RpcFuture<>();
        requestMap.put(request.getRequestId(), future);
        try {
            byte[] data = messageProtocol.marshallingRequest(request);
            ByteBuf reqBuf = Unpooled.buffer(data.length);
            reqBuf.writeBytes(data);
            if (cdl.await(CHANNEL_WAIT_TIME, TimeUnit.SECONDS)){
                channel.writeAndFlush(reqBuf);
                // 等待响应
                response = future.get(RESPONSE_WAIT_TIME, TimeUnit.SECONDS);
            }else {
                throw new RpcException("establish channel time out");
            }
        } catch (Exception e) {
            throw new RpcException(e.getMessage());
        } finally {
            requestMap.remove(request.getRequestId());
        }
        return response;
    }
}
