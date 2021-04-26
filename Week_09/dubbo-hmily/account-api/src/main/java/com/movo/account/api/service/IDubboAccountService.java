package com.movo.account.api.service;

import com.movo.account.api.model.ResponseResult;
import com.movo.account.api.to.SwitchAccountRequest;

/**
 * @author Movo
 */
public interface IDubboAccountService {
    /**
     * 账户转账
     * @param switchAccountRequest
     * @return
     */
    ResponseResult switchAccount(SwitchAccountRequest switchAccountRequest);
}
