package com.movo.redis.service;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @author Movo
 */
public interface DistributedLocker {
    /**
     * 加锁
     * @param lockKey
     * @return RLock
     */
    RLock lock(String lockKey);
    /**
     * 带超时的锁
     * @param lockKey
     * @param timeout 超时时间,单位:秒
     * @return RLock
     */
    RLock lock(String lockKey, int timeout);
    /**
     * 带超时的锁，可设置超时时间单位
     * @param lockKey
     * @param unit 时间单位
     * @param timeout 超时时间
     * @return RLock
     */
    RLock lock(String lockKey, TimeUnit unit, int timeout);
    /**
     * 尝试获取锁
     * @param lockKey
     * @param unit
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);
    /**
     * 用lockKey释放锁
     * @param lockKey
     */
    void unlock(String lockKey);
    /**
     *传入RLock对象释放锁
     * @param lock
     */
    void unlock(RLock lock);
}
