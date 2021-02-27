package io.github.movo.nettygateway.outbound.httpclient4;

import io.github.movo.nettygateway.filter.HeaderHttpResponseFilter;
import io.github.movo.nettygateway.filter.HttpRequestFilter;
import io.github.movo.nettygateway.filter.HttpResponseFilter;
import io.github.movo.nettygateway.router.HttpEndpointRouter;
import io.github.movo.nettygateway.router.RandomHttpEndpointRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.protocol.HTTP;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @Description
 * @auther Movo
 * @create 2021/1/25 7:57
 */
public class HttpOutboundHandler {
    private CloseableHttpAsyncClient httpClient;
    private List<String> backendUrls;

    private List<HttpResponseFilter> postFilters;
    private HttpEndpointRouter router;

    private String formatUrl(String backend) {
        if(backend.endsWith("/")) {
            int index = backend.lastIndexOf("/");
            if(index != -1) {
                if(index == 0) {
                    backend = "";
                } else {
                    backend.substring(0, index - 1);
                }
            }
        }
        return backend;
    }

    public HttpOutboundHandler(final List<String> backends) {
        postFilters = Arrays.asList(new HeaderHttpResponseFilter());
        router = new RandomHttpEndpointRouter();
        backendUrls = backends.stream().map(this::formatUrl).collect(Collectors.toList());

        int cores = Runtime.getRuntime().availableProcessors();
        int connectTimeout = 5000;
        int socketTimeout = 2000;
        int rcvBufferSize = 32 * 1024;
        IOReactorConfig ioConfig = IOReactorConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSoTimeout(socketTimeout)
                .setRcvBufSize(rcvBufferSize)
                .setIoThreadCount(cores)
                .build();

        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    // don't check
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    // don't check
                }
            }
        };
        PoolingNHttpClientConnectionManager pool = null;
        try {
            SSLContext sslContext = null;
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, null);
            SSLIOSessionStrategy sslioSessionStrategy = new SSLIOSessionStrategy(sslContext, NoopHostnameVerifier.INSTANCE);
            Registry<SchemeIOSessionStrategy> registry = RegistryBuilder.<SchemeIOSessionStrategy>create()
                    .register("http", NoopIOSessionStrategy.INSTANCE)
                    .register("https", sslioSessionStrategy)
                    .build();
            ConnectingIOReactor connectingIOReactor = new DefaultConnectingIOReactor(ioConfig);
            pool = new PoolingNHttpClientConnectionManager(connectingIOReactor, registry);
        } catch (NoSuchAlgorithmException | KeyManagementException | IOReactorException e) {
            e.printStackTrace();
        }

        int maxConnTotal = 40;
        int maxConnPerRoute = 8;
        int keepAliveTime = 6000;
        httpClient = HttpAsyncClients.custom()
                .setConnectionManager(pool)
                .setMaxConnTotal(maxConnTotal) // maxConnTotal是同时间正在使用的最多的连接数
                .setMaxConnPerRoute(maxConnPerRoute) // maxConnPerRoute是针对一个域名同时间正在使用的最多的连接数
                .setKeepAliveStrategy((response, context) -> keepAliveTime)
                .build();
        httpClient.start();
    }

    public void handle(FullHttpRequest request, ChannelHandlerContext ctx, List<HttpRequestFilter> preFilters) {
        preFilters.forEach((filter) -> {
            filter.filter(request, ctx);
        });
        System.out.println(request.headers().get("Corporation"));
        String backendUrl = router.route(backendUrls);
        String newUrl = null;
        String method = null;
        if("http://10.0.3.15:8890".equals(backendUrl)) {
            method = request.method().toString();
            if ("POST".equalsIgnoreCase(method)) {
                Map<String, String> requestParams = paramsForPost(request);
                newUrl = requestParams.get("url");
            } else if ("GET".equalsIgnoreCase(method)) {
                Map<String, String> requestParams = paramsForGet(request);
                newUrl = requestParams.get("url");
            }
        } else {
            method = "GET";
            newUrl = backendUrl;
        }
        if(newUrl != null) {
            fetch(request, ctx, method, newUrl);
        } else {
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            ctx.write(response);
            ctx.flush();
            ctx.close();
        }
//        fetchGet(request, ctx, url);
    }

    private Map<String, String> paramsForGet(final FullHttpRequest request) {
        Map<String, String> requestParams = new HashMap<>();
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        decoder.parameters().entrySet().forEach(entry -> {
            // entry.getValue()是一个List, 只取第一个元素
            requestParams.put(entry.getKey(), entry.getValue().get(0));
        });
        return requestParams;
    }

    private Map<String, String> paramsForPost(final FullHttpRequest request) {
        Map<String, String> requestParams = new HashMap<>();
        HttpPostRequestDecoder requestDecoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
        List<InterfaceHttpData> postData = requestDecoder.getBodyHttpDatas();
        for(InterfaceHttpData data : postData) {
            if(data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                requestParams.put(attribute.getName(), attribute.getValue());
            }
        }
        return requestParams;
    }

    private void fetch(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String method, final String url) {
        final HttpRequestBase httpRequestBase;
        if("post".equalsIgnoreCase(method)) {
            httpRequestBase = new HttpPost(url);
        } else if("get".equalsIgnoreCase(method)) {
            httpRequestBase = new HttpGet(url);
        } else {
            httpRequestBase = null;
        }
        if(httpRequestBase != null) {
            httpRequestBase.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
            httpClient.execute(httpRequestBase, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(final HttpResponse endpointResponse) {
                    handleResponse(inbound, ctx, endpointResponse);
                }

                @Override
                public void failed(Exception e) {
                    httpRequestBase.abort();
                    e.printStackTrace();
                }

                @Override
                public void cancelled() {
                    httpRequestBase.abort();
                }
            });
        }
    }

    private void handleResponse(final FullHttpRequest request, final ChannelHandlerContext ctx, final HttpResponse endpointResponse) {
        FullHttpResponse response = null;
        try {
            HttpEntity httpEntity = endpointResponse.getEntity();
            if(httpEntity != null) {
                String content = new BufferedReader(new InputStreamReader(httpEntity.getContent())).lines().collect(Collectors.joining(System.lineSeparator()));
                byte[] body = content.getBytes();
                response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                response.headers().set("Content-Type", httpEntity.getContentType().getValue());
                response.headers().set("Content-Length", body.length);
                response.headers().set("Content", body);
            } else {
                response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            }
            response.headers().set("Access-Control-Allow-Methods", "GET, POST");
            response.headers().set("Access-Control-Allow-Origin", "*");
        } catch (IOException e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            for(HttpResponseFilter filter : postFilters) {
                filter.filter(response);
            }
            if(request != null) {
                if(HttpUtil.isKeepAlive(request)) {
                    ctx.write(response);
                } else {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                }
            }
            ctx.flush();
            ctx.close();
        }
    }

    private void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
