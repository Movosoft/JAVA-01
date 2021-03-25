package com.movo.dubboserviceapi.service;

import com.movo.dubboserviceapi.entity.Order;
import org.dromara.hmily.annotation.Hmily;

/**
 * The interface Payment service.
 *
 */
public interface PaymentService {

    /**
     * 订单支付
     *
     * @param order 订单实体
     */
    @Hmily
    void makePayment(Order order);

    /**
     * Make payment for tac.
     *
     * @param order the order
     */
    @Hmily
    void makePaymentForTAC(Order order);

    /**
     * Test make payment.
     *
     * @param order the order
     */
    @Hmily
    void testMakePayment(Order order);

    /**
     * 订单支付
     *
     * @param order 订单实体
     */
    @Hmily
    void makePaymentWithNested(Order order);

    /**
     * Make payment with nested exception.
     *
     * @param order the order
     */
    @Hmily
    void makePaymentWithNestedException(Order order);

    /**
     * mock订单支付的时候库存异常
     *
     * @param order 订单实体
     * @return String string
     */
    @Hmily
    String mockPaymentInventoryWithTryException(Order order);

    /**
     * Mock tac payment inventory with try exception string.
     *
     * @param order the order
     * @return the string
     */
    @Hmily
    String mockTacPaymentInventoryWithTryException(Order order);

    /**
     * mock订单支付的时候库存超时
     *
     * @param order 订单实体
     * @return String string
     */
    @Hmily
    String mockPaymentInventoryWithTryTimeout(Order order);

    /**
     * Mock payment account with try exception string.
     *
     * @param order the order
     * @return the string
     */
    @Hmily
    String mockPaymentAccountWithTryException(Order order);

    /**
     * Mock payment account with try timeout string.
     *
     * @param order the order
     * @return the string
     */
    @Hmily
    String mockPaymentAccountWithTryTimeout(Order order);

    /**
     * mock订单支付的时候库存确认超时
     *
     * @param order 订单实体
     * @return String string
     */
    @Hmily
    String mockPaymentInventoryWithConfirmTimeout(Order order);

}
