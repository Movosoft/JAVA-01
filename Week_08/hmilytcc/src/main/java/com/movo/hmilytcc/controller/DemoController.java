package com.movo.hmilytcc.controller;

import com.movo.dubboserviceapi.service.AccountService;
import com.movo.dubboserviceprovider.account.entity.Account;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @auther Movo
 * @create 2021/3/22 17:14
 */
@RestController
public class DemoController {
    @DubboReference(version = "${spring.application.service.version}", url = "${spring.application.service.url}")
    private AccountService accountService;

    @RequestMapping("/test")
    public Account test() {
        return accountService.findByUserId("1");
    }
}
