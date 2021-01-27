package io.github.movo.nettygateway.filter;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @Description
 * @auther Movo
 * @create 2021/1/25 11:13
 */
public class HeaderHttpResponseFilter implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse response) {
        response.headers().set("author", "Movo");
    }
}
