package com.movo.dubboserviceapi.service;

import com.movo.dubboserviceapi.entity.Order;

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
    void makePayment(Order order);

    /**
     * Make payment for tac.
     *
     * @param order the order
     */
    void makePaymentForTAC(Order order);

    /**
     * Test make payment.
     *
     * @param order the order
     */
    void testMakePayment(Order order);

    /**
     * 订单支付
     *
     * @param order 订单实体
     */
    void makePaymentWithNested(Order order);

    /**
     * Make payment with nested exception.
     *
     * @param order the order
     */
    void makePaymentWithNestedException(Order order);

    /**
     * mock订单支付的时候库存异常
     *
     * @param order 订单实体
     * @return String string
     */
    String mockPaymentInventoryWithTryException(Order order);

    /**
     * Mock tac payment inventory with try exception string.
     *
     * @param order the order
     * @return the string
     */
    String mockTacPaymentInventoryWithTryException(Order order);

    /**
     * mock订单支付的时候库存超时
     *
     * @param order 订单实体
     * @return String string
     */
    String mockPaymentInventoryWithTryTimeout(Order order);

    /**
     * Mock payment account with try exception string.
     *
     * @param order the order
     * @return the string
     */
    String mockPaymentAccountWithTryException(Order order);

    /**
     * Mock payment account with try timeout string.
     *
     * @param order the order
     * @return the string
     */
    String mockPaymentAccountWithTryTimeout(Order order);

    /**
     * mock订单支付的时候库存确认超时
     *
     * @param order 订单实体
     * @return String string
     */
    String mockPaymentInventoryWithConfirmTimeout(Order order);

}
