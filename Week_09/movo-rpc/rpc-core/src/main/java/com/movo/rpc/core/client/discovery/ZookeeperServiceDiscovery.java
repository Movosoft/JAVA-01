package com.movo.rpc.core.client.discovery;

import com.alibaba.fastjson.JSON;
import com.movo.rpc.core.common.constants.RpcConstant;
import com.movo.rpc.core.common.model.Service;
import com.movo.rpc.core.common.serializer.ZookeeperSerializer;
import lombok.Getter;
import org.I0Itec.zkclient.ZkClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 服务发现zookeeper实现
 * @author Movo
 * @create 2021/4/6 14:39
 */
@Getter
public class ZookeeperServiceDiscovery implements ServiceDiscovery {

    private ZkClient zkClient;

    public ZookeeperServiceDiscovery(String zkAddress) {
        zkClient = new ZkClient(zkAddress);
        zkClient.setZkSerializer(new ZookeeperSerializer());
    }

    /**
     * 使用Zookeeper客户端，通过服务名获取服务列表
     * 服务名格式: 接口全路径
     * @param name
     * @return
     */
    @Override
    public List<Service> findServiceList(String name) {
        String servicePath = RpcConstant.ZK_SERVICE_PATH + RpcConstant.PATH_DELIMITER + name + RpcConstant.PATH_DELIMITER + "service";
        List<String> children = zkClient.getChildren(servicePath);

        return Optional.ofNullable(children).orElse(new ArrayList<>()).stream().map(
            str -> {
                String serviceStr;
                try {
                    serviceStr = URLDecoder.decode(str, RpcConstant.UTF_8);
                    return JSON.parseObject(serviceStr, Service.class);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        ).collect(Collectors.toList());
    }
}
