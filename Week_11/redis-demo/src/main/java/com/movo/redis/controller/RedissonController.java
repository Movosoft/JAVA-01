package com.movo.redis.controller;

import com.movo.redis.service.RedisService;
import org.redisson.api.RLock;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Movo
 * @create 2021/4/27 9:34
 */
@RestController
@RequestMapping("/redisson")
public class RedissonController {
    private int counter = 0;
    private RedisService redisService;

    public RedissonController(RedisService redisService) {
        this.redisService = redisService;
    }

    @RequestMapping("/countDemo")
    public int countDemo() {
        String key = "counter";
        String lockKey = UUID.randomUUID().toString();
        boolean lockStatus = redisService.tryLock(lockKey, TimeUnit.SECONDS, 10, 1);
        if(lockStatus) {
            try {
                int num = Optional.ofNullable((Integer) redisService.get(key)).orElseGet(() -> counter);
                redisService.set(key, ++num);
                return num;
            } finally {
                redisService.unlock(lockKey);
            }
        } else {
            return -1;
        }
    }
}
