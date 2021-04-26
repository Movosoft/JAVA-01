package com.movo.rpc.core.client.balance;

import com.movo.rpc.core.common.model.Service;

import java.util.List;

/**
 * 负载均衡算法
 * @author Movo
 * @create 2021/4/20 9:20
 */
public interface LoadBalance {
    /**
     * 按照配置的负载均衡算法获取一个服务
     * @param services - 服务列表
     * @return Service
     */
    Service chooseOne(List<Service> services);
}
