package com.movo.rpc.core.client.balance.impl;

import com.movo.rpc.core.client.balance.LoadBalance;
import com.movo.rpc.core.common.constants.RpcConstant;
import com.movo.rpc.core.common.model.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description
 * @auther Movo
 * @create 2021/4/6 14:59
 */
@com.movo.rpc.core.annotation.LoadBalance(RpcConstant.BALANCE_ROUND)
// 轮询算法
public class FullRoundBalance implements LoadBalance {

    private static volatile AtomicInteger index = new AtomicInteger(0);

    @Override
    public synchronized Service chooseOne(List<Service> services) {
        if (index.get() == services.size()) {
            index.set(0);
        }
        return services.get(index.getAndDecrement());
    }
}
