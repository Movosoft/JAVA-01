package com.movo.rpc.core.client.discovery;

import com.movo.rpc.core.common.model.Service;

import java.util.List;

/**
 * 服务发现抽象类
 * @author Movo
 * @date 2021/4/20 15:20
 */
public interface ServiceDiscovery {
    /**
     * 根据服务名称发现服务列表
     * @param name
     * @return
     */
    List<Service> findServiceList(String name);
}
