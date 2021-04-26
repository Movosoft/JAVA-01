package com.movo.account.consumer;

import com.movo.account.api.contants.AccountConstant;
import com.movo.account.api.model.ResponseResult;
import com.movo.account.api.service.IDubboAccountService;
import com.movo.account.api.to.SwitchAccountRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author Movo
 * @create 2021/4/25 11:51
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    @DubboReference(version = "${spring.application.service.version}", url = "${spring.application.service.url}")
    private IDubboAccountService dubboAccountService;

    @RequestMapping("/transferAccountsDemo")
    public ResponseResult transferAccountsDemo() {
        SwitchAccountRequest switchAccountRequest = new SwitchAccountRequest();
        switchAccountRequest.setSourceUserId(0);
        switchAccountRequest.setSourceType(AccountConstant.CNY);
        switchAccountRequest.setSourceAmount(new BigDecimal(2100));
        switchAccountRequest.setTargetUserId(0);
        switchAccountRequest.setTargetType(AccountConstant.USD);
        // 参数正确性验证 略去
        return dubboAccountService.switchAccount(switchAccountRequest);
    }
}
