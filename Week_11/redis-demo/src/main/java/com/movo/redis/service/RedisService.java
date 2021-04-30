package com.movo.redis.service;

/**
 * @author Movo
 */
public interface RedisService extends DistributedLocker {
    /**
     * 存储键值对
     * @param key
     * @param value
     */
    void set(String key, Object value);

    /**
     * 存储键值对
     * @param key
     * @param value
     * @param timeout 超时时间,单位:秒
     */
    void set(String key, Object value, int timeout);

    /**
     * 根据key获取value
     * @param key
     * @return
     */
    Object get(String key);
}
