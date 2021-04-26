package com.movo.rpc.core.client.balance.impl;

import com.movo.rpc.core.client.balance.LoadBalance;
import com.movo.rpc.core.common.constants.RpcConstant;
import com.movo.rpc.core.common.model.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 加权轮询算法
 * @author Movo
 * @create 2021/4/6 15:31
 */
@com.movo.rpc.core.annotation.LoadBalance(RpcConstant.BALANCE_SMOOTH_WEIGHT_ROUND)
public class SmoothWeightRoundBalance implements LoadBalance {

    private static final Map<String, Integer> weightMap = new HashMap<>();
    private static int allWeight = 0;
    private ReentrantLock lock = new ReentrantLock() ;

    @Override
    public Service chooseOne(List<Service> services) {
        lock.lock();
        try {
            AtomicInteger maxWeight = new AtomicInteger(0);
            AtomicInteger curWeight = new AtomicInteger(0);
            AtomicReference<Service> maxWeightServer = new AtomicReference<>();
            services.forEach(service -> {
                curWeight.set(service.getWeight());
                weightMap.computeIfAbsent(service.toString(), key -> curWeight.get());
                allWeight += curWeight.get();
                if(maxWeight.get() == 0 || (curWeight.get() > maxWeight.get())) {
                    maxWeight.set(curWeight.get());
                    maxWeightServer.set(service);
                }
            });
            weightMap.put(maxWeightServer.get().toString(), maxWeightServer.get().getWeight() - allWeight);
            services.forEach(service -> weightMap.put(service.toString(), service.getWeight() + weightMap.get(service.toString())));
            return maxWeightServer.get();
        } finally {
            lock.unlock();
        }
    }
}
