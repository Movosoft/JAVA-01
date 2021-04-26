package com.movo.account.service.impl;

import com.movo.account.api.contants.AccountConstant;
import com.movo.account.api.model.ResponseResult;
import com.movo.account.api.service.IAccountService;
import com.movo.account.api.service.IDubboAccountService;
import com.movo.account.api.to.SwitchAccountRequest;
import com.movo.account.mapper.FreezeAccountMapper;
import org.apache.dubbo.config.annotation.DubboService;

import java.math.BigDecimal;

/**
 * @author Movo
 * @create 2021/4/25 11:33
 */
@DubboService(version = "1.0.0")
public class DubboAccountServiceImpl implements IDubboAccountService {

    private IAccountService accountService;

    public DubboAccountServiceImpl(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public ResponseResult switchAccount(SwitchAccountRequest switchAccountRequest) {
        String sourceType = switchAccountRequest.getSourceType();
        int sourceUserId = switchAccountRequest.getSourceUserId();
        if(AccountConstant.CNY.equalsIgnoreCase(sourceType)) {
            BigDecimal freezeAmountCny = switchAccountRequest.getSourceAmount();
            Integer freezeId = accountService.createAccountFreezeInfo(AccountConstant.CNY, sourceUserId, freezeAmountCny);
            if(freezeId == null) {
                return ResponseResult.fail("创建账户冻结信息失败！");
            }
            switchAccountRequest.setFreezeId(freezeId);
            accountService.switchAccountPre(switchAccountRequest);
        } else if(AccountConstant.USD.equalsIgnoreCase(sourceType)) {
            BigDecimal freezeAmountUsd = switchAccountRequest.getSourceAmount();
            Integer freezeId = accountService.createAccountFreezeInfo(AccountConstant.USD, sourceUserId, freezeAmountUsd);
            if(freezeId == null) {
                return ResponseResult.fail("创建账户冻结信息失败！");
            }
            switchAccountRequest.setFreezeId(freezeId);
            accountService.switchAccountPre(switchAccountRequest);
        }
        return ResponseResult.ok();
    }
}
