package com.movo.rpc.core.client.discovery;

import com.movo.rpc.core.client.cache.ServerDiscoveryCache;
import org.I0Itec.zkclient.IZkChildListener;

import java.util.List;

/**
 * 子节点事件监听处理类
 * @author Movo
 * @create 2021/4/19 16:05
 */
public class ZkChildListenerImpl implements IZkChildListener {

    /**
     *
     * @param parentPath
     * @param childList
     * @throws Exception
     */
    @Override
    public void handleChildChange(String parentPath, List<String> childList) throws Exception {
        System.out.println("Child change parentPath:[{" + parentPath + "}] -- childList:[{" + childList + "}]");
        // 只要子节点有改动就清空缓存
        String[] arr = parentPath.split("/");
        ServerDiscoveryCache.removeAll(arr[2]);
    }
}
