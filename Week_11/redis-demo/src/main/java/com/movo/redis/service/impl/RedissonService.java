package com.movo.redis.service.impl;

import com.movo.redis.service.RedisService;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author Movo
 * @create 2021/4/30 16:20
 */
@Service
public class RedissonService extends RedissonDistributedLocker implements RedisService {
    public RedissonService(RedissonClient redisson) {
        super(redisson);
    }

    @Override
    public void set(String key, Object value) {
        redisson.getBucket(key).set(value);
    }

    @Override
    public void set(String key, Object value, int timeout) {
        redisson.getBucket(key).set(value, timeout, TimeUnit.SECONDS);
    }

    @Override
    public Object get(String key) {
        return redisson.getBucket(key).get();
    }

}
