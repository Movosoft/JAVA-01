package com.movo.rpc.consumer;

import com.movo.rpc.api.User;
import com.movo.rpc.api.UserService;
import com.movo.rpc.core.annotation.InjectService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Movo
 * @create 2021/4/21 10:10
 */
@RestController
@RequestMapping("/rpc")
public class RpcController {

    @InjectService
    private UserService userService;

    @RequestMapping("/getUser")
    public User getUser(@RequestParam(name = "userId") Integer userId) {
        return userService.getUser(userId);
    }
}
