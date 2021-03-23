package com.movo.dubboserviceapi.service;

import com.movo.dubboserviceprovider.account.dto.AccountDTO;
import com.movo.dubboserviceprovider.account.dto.AccountNestedDTO;
import com.movo.dubboserviceprovider.account.entity.Account;

/**
 * The interface Account service.
 *
 */

public interface AccountService {

    /**
     * 扣款支付
     *
     * @param accountDTO 参数dto
     */
    boolean payment(AccountDTO accountDTO);

    /**
     * Mock try payment exception.
     *
     * @param accountDTO the account dto
     */
    boolean mockTryPaymentException(AccountDTO accountDTO);

    /**
     * Mock try payment timeout.
     *
     * @param accountDTO the account dto
     */
    boolean mockTryPaymentTimeout(AccountDTO accountDTO);

    /**
     * Payment tac boolean.
     *
     * @param accountDTO the account dto
     * @return the boolean
     */
    boolean paymentTAC(AccountDTO accountDTO);

    /**
     * Test payment boolean.
     *
     * @param accountDTO the account dto
     * @return the boolean
     */
    boolean testPayment(AccountDTO accountDTO);

    /**
     * 扣款支付
     *
     * @param accountNestedDTO 参数dto
     * @return true boolean
     */
    boolean paymentWithNested(AccountNestedDTO accountNestedDTO);

    /**
     * Payment with nested exception boolean.
     *
     * @param accountNestedDTO the account nested dto
     * @return the boolean
     */
    boolean paymentWithNestedException(AccountNestedDTO accountNestedDTO);

    /**
     * 获取用户账户信息
     *
     * @param userId 用户id
     * @return AccountDO account do
     */
    Account findByUserId(String userId);
}
