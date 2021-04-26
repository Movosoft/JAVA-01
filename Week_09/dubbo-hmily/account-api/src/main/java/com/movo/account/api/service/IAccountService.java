package com.movo.account.api.service;

import com.movo.account.api.model.ResponseResult;
import com.movo.account.api.to.SwitchAccountRequest;
import org.dromara.hmily.annotation.Hmily;

import java.math.BigDecimal;

/**
 * 账户交易相关业务
 * @author Movo
 * @create 2021/4/23 11:19
 */
public interface IAccountService {

    /**
     * 创建冻结记录并返回主键id
     * @param accountType
     * @param userId
     * @param freezeAmount
     * @return int
     */
    @Hmily
    Integer createAccountFreezeInfo(String accountType, int userId, BigDecimal freezeAmount);

    /**
     * 什么都不需要做
     * @param accountType
     * @param userId
     * @param freezeAmount
     * @return
     */
    void createAccountFreezeInfoConfirm(String accountType, int userId, BigDecimal freezeAmount);

    /**
     * 什么都不需要做
     * @param accountType
     * @param userId
     * @param freezeAmount
     */
    void createAccountFreezeInfoCancel(String accountType, int userId, BigDecimal freezeAmount);

    /**
     * 找到转出账户，将该账户资金扣减指定金额，在冻结记录中新增指定金额的冻结记录
     * @param request
     * @return ResponseResult
     */
    @Hmily
    ResponseResult switchAccountPre(SwitchAccountRequest request);

    /**
     * 找到转出账户及相应冻结资金记录，然后将指定金额转移到转入账户中，冻结记录失效
     * @param request
     * @return ResponseResult
     */
    ResponseResult switchAccountConfirm(SwitchAccountRequest request);

    /**
     * 找到转出账户及相应冻结资金记录，将金额加回到转出账户中，冻结记录失效
     * @param request
     * @return ResponseResult
     */
    ResponseResult switchAccountCancel(SwitchAccountRequest request);
}
