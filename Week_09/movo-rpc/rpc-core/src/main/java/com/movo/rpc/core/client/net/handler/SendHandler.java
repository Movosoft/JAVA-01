package com.movo.rpc.core.client.net.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.CountDownLatch;

/**
 * 发送处理类，定义Netty入站处理细则
 * @author Movo
 * @create 2021/4/8 16:11
 */
public class SendHandler extends ChannelInboundHandlerAdapter {
    private CountDownLatch cdl;
    private Object readMsg;
    private byte[] data;

    public SendHandler(byte[] data) {
        cdl = new CountDownLatch(1);
        this.data = data;
    }

    /**
     * 连接服务端成功后发送数据
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Connect to server successfully:" + ctx);
        ByteBuf reqBuf = Unpooled.buffer(data.length);
        reqBuf.writeBytes(data);
        System.out.println("Client sends message:" + reqBuf);
        ctx.writeAndFlush(reqBuf);
    }

    /**
     * 读取数据
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("Client reads message:" + msg);
        ByteBuf msgBuf = (ByteBuf) msg;
        byte[] resp = new byte[msgBuf.readableBytes()];
        // 手动回收
        ReferenceCountUtil.release(msgBuf);
        readMsg = resp;
    }

    /**
     * 数据读取完毕后释放cdl锁,并清空缓冲区
     * @param ctx
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        cdl.countDown();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        System.out.println("Exception occurred:" + cause.getMessage());
        ctx.close();
    }

    public Object respData() throws InterruptedException {
        cdl.await();
        return readMsg;
    }
}
