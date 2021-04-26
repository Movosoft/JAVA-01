package com.movo.rpc.core.client.cache;

import com.movo.rpc.core.common.model.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Description
 * @auther Movo
 * @create 2021/4/6 11:46
 */
// 本地缓存服务发现的结果
public class ServerDiscoveryCache {
    private static final Map<String, List<Service>> SERVER_MAP = new ConcurrentHashMap<>();
    public static final List<String> SERVICE_CLASS_NAMES = new ArrayList<>();
    public static void put(String serviceName, List<Service> serviceList) {
        SERVER_MAP.put(serviceName, serviceList);
    }

    public static List<Service> get(String serviceName) {
        return SERVER_MAP.get(serviceName);
    }

    // 去除指定的值
    public static void remove(String serviceName, Service service) {
        SERVER_MAP.computeIfPresent(
            serviceName,
            (key, value) -> value.stream().filter(o -> !o.toString().equals(service.toString())).collect(Collectors.toList())
        );
    }

    public static void removeAll(String serviceName) {
        SERVER_MAP.remove(serviceName);
    }

    public static boolean isEmpty(String serviceName) {
        List<Service> serviceList = SERVER_MAP.get(serviceName);
        return serviceList == null || serviceList.isEmpty();
    }
}
