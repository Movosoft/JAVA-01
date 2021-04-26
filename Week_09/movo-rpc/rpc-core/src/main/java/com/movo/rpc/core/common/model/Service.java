package com.movo.rpc.core.common.model;

import lombok.*;

/**
 * 存放服务的基本信息
 * @author Movo
 * @create 2021/4/20 9:27
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    /**
     * 服务名称
     */
    private String name;
    /**
     * 服务的消息协议
     */
    private String protocol;
    /**
     * 服务地址，格式：ip:port
     */
    private String address;
    /**
     * 服务权重，越大优先级越高
     */
    private Integer weight;
}
