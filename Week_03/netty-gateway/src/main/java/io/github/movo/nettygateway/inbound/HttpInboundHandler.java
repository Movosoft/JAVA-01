package io.github.movo.nettygateway.inbound;

import io.github.movo.nettygateway.filter.HeaderHttpRequestFilter;
import io.github.movo.nettygateway.filter.HttpRequestFilter;
import io.github.movo.nettygateway.outbound.httpclient4.HttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Arrays;
import java.util.List;

/**
 * @Description
 * @auther Movo
 * @create 2021/1/25 15:07
 */
public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private final List<String> proxyServer;
    private HttpOutboundHandler handler;
    private List<HttpRequestFilter> preFilters;

    public HttpInboundHandler(final List<String> proxyServer) {
        this.proxyServer = proxyServer;
        this.handler = new HttpOutboundHandler(this.proxyServer);
        this.preFilters = Arrays.asList(new HeaderHttpRequestFilter());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest)msg;
        handler.handle(request, ctx, preFilters);
    }
}
