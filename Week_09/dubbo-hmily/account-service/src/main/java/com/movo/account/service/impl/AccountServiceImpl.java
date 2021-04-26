package com.movo.account.service.impl;

import com.movo.account.api.contants.AccountConstant;
import com.movo.account.api.entity.AccountCnyFreeze;
import com.movo.account.api.entity.AccountUsdFreeze;
import com.movo.account.api.model.ResponseResult;
import com.movo.account.api.service.IAccountService;
import com.movo.account.api.to.SwitchAccountRequest;
import com.movo.account.mapper.AccountMapper;
import com.movo.account.mapper.FreezeAccountMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.ibatis.builder.ParameterExpression;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author Movo
 * @create 2021/4/25 8:45
 */
@Service
public class AccountServiceImpl implements IAccountService {

    private AccountMapper accountMapper;
    private FreezeAccountMapper freezeAccountMapper;

    private final String CNY = AccountConstant.CNY;
    private final String USD = AccountConstant.USD;
    private final int USD_CNY_RATIO = AccountConstant.USD_CNY_RATIO;

    public AccountServiceImpl(AccountMapper accountMapper, FreezeAccountMapper freezeAccountMapper) {
        this.accountMapper = accountMapper;
        this.freezeAccountMapper = freezeAccountMapper;
    }

    @Override
    @HmilyTCC(confirmMethod = "createAccountFreezeInfoConfirm", cancelMethod = "createAccountFreezeInfoCancel")
    public Integer createAccountFreezeInfo(String accountType, int userId, BigDecimal freezeAmount) {
        if(CNY.equalsIgnoreCase(accountType)) {
            AccountCnyFreeze accountCnyFreeze = AccountCnyFreeze.builder().userId(userId).freezeAmountCny(freezeAmount).build();
            freezeAccountMapper.addAccountCnyFreeze(accountCnyFreeze);
            return accountCnyFreeze.getId();
        } else if(USD.equalsIgnoreCase(accountType)) {
            AccountUsdFreeze accountUsdFreeze = AccountUsdFreeze.builder().userId(userId).freezeAmountUsd(freezeAmount).build();
            freezeAccountMapper.addAccountUsdFreeze(accountUsdFreeze);
            return accountUsdFreeze.getId();
        }
        return null;
    }

    @Override
    public void createAccountFreezeInfoConfirm(String accountType, int userId, BigDecimal freezeAmount) {
    }

    @Override
    public void createAccountFreezeInfoCancel(String accountType, int userId, BigDecimal freezeAmount) {
    }

    @Override
    @HmilyTCC(confirmMethod = "switchAccountConfirm", cancelMethod = "switchAccountCancel")
    public ResponseResult switchAccountPre(SwitchAccountRequest switchAccountRequest) {
        String sourceType = switchAccountRequest.getSourceType();
        int sourceUserId = switchAccountRequest.getSourceUserId();
        BigDecimal sourceAmount = switchAccountRequest.getSourceAmount();
        if(CNY.equalsIgnoreCase(sourceType)) {
            accountMapper.decreaseCny(sourceUserId, sourceAmount);
        } else if(USD.equalsIgnoreCase(sourceType)) {
            accountMapper.decreaseUsd(sourceUserId, sourceAmount);
        }
        return ResponseResult.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult switchAccountConfirm(SwitchAccountRequest switchAccountRequest) {
        int freezeId = switchAccountRequest.getFreezeId();
        String sourceType = switchAccountRequest.getSourceType();
        String targetType = switchAccountRequest.getTargetType();
        int targetUserId = switchAccountRequest.getTargetUserId();
        if (CNY.equalsIgnoreCase(sourceType)) {
            if (CNY.equalsIgnoreCase(targetType)) {
                BigDecimal amount = freezeAccountMapper.getAccountCnyFreezeAmount(freezeId);
                accountMapper.increaseCny(targetUserId, amount);
                freezeAccountMapper.invalidAccountCnyFreeze(freezeId);
            }else if (USD.equalsIgnoreCase(targetType)) {
                BigDecimal amount = freezeAccountMapper.getAccountCnyFreezeAmount(freezeId).divide(new BigDecimal(USD_CNY_RATIO));
                accountMapper.increaseUsd(targetUserId, amount);
                freezeAccountMapper.invalidAccountCnyFreeze(freezeId);
            }
        } else if (USD.equalsIgnoreCase(sourceType)) {
            if (CNY.equalsIgnoreCase(targetType)) {
                BigDecimal amount = freezeAccountMapper.getAccountUsdFreezeAmount(freezeId).multiply(new BigDecimal(USD_CNY_RATIO));
                accountMapper.increaseCny(targetUserId, amount);
                freezeAccountMapper.invalidAccountUsdFreeze(freezeId);
            }else if (USD.equalsIgnoreCase(targetType)) {
                BigDecimal amount = freezeAccountMapper.getAccountUsdFreezeAmount(freezeId);
                accountMapper.increaseUsd(targetUserId, amount);
                freezeAccountMapper.invalidAccountUsdFreeze(freezeId);
            }
        }
        return ResponseResult.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult switchAccountCancel(SwitchAccountRequest switchAccountRequest) {
        int freezeId = switchAccountRequest.getFreezeId();
        String sourceType = switchAccountRequest.getSourceType();
        int sourceUserId = switchAccountRequest.getSourceUserId();
        if (CNY.equalsIgnoreCase(sourceType)) {
            BigDecimal amount = freezeAccountMapper.getAccountCnyFreezeAmount(freezeId);
            accountMapper.increaseCny(sourceUserId, amount);
            freezeAccountMapper.invalidAccountCnyFreeze(freezeId);
        } else if (USD.equalsIgnoreCase(sourceType)) {
            BigDecimal amount = freezeAccountMapper.getAccountUsdFreezeAmount(freezeId);
            accountMapper.increaseUsd(sourceUserId, amount);
            freezeAccountMapper.invalidAccountUsdFreeze(freezeId);
        }
        return ResponseResult.fail("tcc cancel！");
    }
}
