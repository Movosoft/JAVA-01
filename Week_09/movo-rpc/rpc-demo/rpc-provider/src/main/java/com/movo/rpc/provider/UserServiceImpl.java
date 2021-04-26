package com.movo.rpc.provider;

import com.movo.rpc.api.User;
import com.movo.rpc.api.UserService;
import com.movo.rpc.core.annotation.Service;

import java.util.UUID;

/**
 * RPC测试 服务提供方
 * @author Movo
 * @create 2021/4/21 9:52
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(Integer userId) {
        User user = new User();
        user.setUserId(userId);
        user.setUsername(UUID.randomUUID().toString());
        return user;
    }
}
