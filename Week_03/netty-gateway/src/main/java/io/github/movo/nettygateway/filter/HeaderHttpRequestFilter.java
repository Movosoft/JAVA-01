package io.github.movo.nettygateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @Description
 * @auther Movo
 * @create 2021/1/25 11:15
 */
public class HeaderHttpRequestFilter implements HttpRequestFilter {
    @Override
    public void filter(FullHttpRequest request, ChannelHandlerContext ctx) {
        request.headers().set("Corporation", "Movosoft");
    }
}
