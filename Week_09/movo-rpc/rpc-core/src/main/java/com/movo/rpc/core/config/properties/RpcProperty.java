package com.movo.rpc.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RPC服务基本配置属性
 * @author Movo
 * @create 2021/3/31 14:09
 */
@ConfigurationProperties(prefix = "movo.rpc")
@Getter
@Setter
public class RpcProperty {

    /**
     * 服务注册中心地址
     */
    private String registerAddress = "10.0.3.15:2181";

    /**
     * 服务暴露端口
     */
    private Integer serverPort = 333;

    /**
     * 服务消息协议
     */
    private String protocol = "java";

    /**
     * 负载均衡算法
     */
    private String loadBalance = "smoothWeightRound";

    /**
     * 权重，默认为1
     */
    private Integer weight = 1;
}
