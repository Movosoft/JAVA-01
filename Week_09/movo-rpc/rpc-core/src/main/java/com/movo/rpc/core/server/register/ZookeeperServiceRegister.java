package com.movo.rpc.core.server.register;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.movo.rpc.core.common.constants.RpcConstant;
import com.movo.rpc.core.common.model.Service;
import com.movo.rpc.core.common.serializer.ZookeeperSerializer;
import org.I0Itec.zkclient.ZkClient;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;

/**
 * zk服务注册器，有提供服务注册、服务暴露的能力
 * @author Movo
 * @create 2021/3/31 15:03
 */
public class ZookeeperServiceRegister extends DefaultServiceRegister {

    static {
        ParserConfig.getGlobalInstance().addAccept("com.movo");
    }

    private ZkClient zkClient;

    public ZookeeperServiceRegister(String zkAddress, Integer servicePort, String protocol, Integer weight) {
        zkClient = new ZkClient(zkAddress);
        zkClient.setZkSerializer(new ZookeeperSerializer());
        this.port = servicePort;
        this.protocol = protocol;
        this.weight = weight;
    }

    @Override
    public void register(ServiceObject serviceObject) throws IllegalArgumentException, UnknownHostException {
        super.register(serviceObject);
        String host = InetAddress.getLocalHost().getHostAddress();
        String address = host + ":" + port;
        String name = serviceObject.getName();
        Service service = Service.builder().name(name).address(address).protocol(protocol).weight(weight).build();
        exportService(service);
    }

    /**
     * 服务暴露
     * @param service
     */
    private void exportService(Service service) {
        String serviceName = service.getName();
        String uri = JSON.toJSONString(service);
        try {
            uri = URLEncoder.encode(uri, RpcConstant.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String servicePath = RpcConstant.ZK_SERVICE_PATH + RpcConstant.PATH_DELIMITER + serviceName + RpcConstant.PATH_DELIMITER + "service";
        if(!zkClient.exists(servicePath)) {
            // 没有该节点就创建
            zkClient.createPersistent(servicePath, true);
        }
        String uriPath = servicePath + RpcConstant.PATH_DELIMITER + uri;
        if(zkClient.exists(uriPath)) {
            // 删除之前的节点
            zkClient.delete(uriPath);
        }
        // 创建一个临时节点，会话失效即被清理
        zkClient.createEphemeral(uriPath);
    }
}
