package com.movo.rpc.core.client.balance.impl;

import com.movo.rpc.core.client.balance.LoadBalance;
import com.movo.rpc.core.common.constants.RpcConstant;
import com.movo.rpc.core.common.model.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description
 * @auther Movo
 * @create 2021/4/6 15:31
 */
@com.movo.rpc.core.annotation.LoadBalance(RpcConstant.BALANCE_WEIGHT_ROUND)
// 加权轮询算法
public class WeightRoundBalance implements LoadBalance {

    private static volatile AtomicInteger index = new AtomicInteger(0);

    @Override
    public synchronized Service chooseOne(List<Service> services) {
        List<Service> serviceList = new ArrayList<>();
        if(serviceList.isEmpty()) {
            services.stream().forEach(service -> {
                int weight = service.getWeight();
                while(weight-- > 0) {
                    serviceList.add(service);
                }
            });
        }
        if(index.get() == serviceList.size()) {
            index.set(0);
        }
        return serviceList.get(index.getAndIncrement());
    }
}
