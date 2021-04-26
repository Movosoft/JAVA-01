package com.movo.rpc.core.client.balance.impl;

import com.movo.rpc.core.client.balance.LoadBalance;
import com.movo.rpc.core.common.constants.RpcConstant;
import com.movo.rpc.core.common.model.Service;

import java.util.List;
import java.util.Random;

/**
 * @Description
 * @auther Movo
 * @create 2021/4/6 15:28
 */
@com.movo.rpc.core.annotation.LoadBalance(RpcConstant.BALANCE_RANDOM)
// 随机算法
public class RandomBalance implements LoadBalance {

    private static Random random = new Random();

    @Override
    public Service chooseOne(List<Service> services) {
        return services.get(random.nextInt(services.size()));
    }
}
