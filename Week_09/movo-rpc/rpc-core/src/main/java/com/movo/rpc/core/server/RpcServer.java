package com.movo.rpc.core.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * rpc服务端抽象类
 * @author Movo
 * @create 2021/4/2 15:40
 */
@AllArgsConstructor
@Getter
@Setter
public abstract class RpcServer {
    /**
     * 服务端口
     */
    protected int port;
    /**
     * 服务协议
     */
    protected String protocol;
    /**
     * 请求处理者
     */
    protected RequestHandler requestHandler;
    /**
     * 开启服务
     */
    public abstract void start();
    /**
     * 关闭服务
     */
    public abstract void stop();
}
