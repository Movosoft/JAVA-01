package com.movo.dubboserviceapi.service;

import com.movo.dubboserviceapi.entity.Order;
import org.dromara.hmily.annotation.Hmily;

import java.math.BigDecimal;

/**
 * The interface Order service.
 *
 */
public interface OrderService {

    /**
     * 创建订单并且进行扣除账户余额支付，并进行库存扣减操作
     *
     * @param count  购买数量
     * @param amount 支付金额
     * @return string string
     */
    @Hmily
    String orderPay(Integer count, BigDecimal amount);

    /**
     * Save order for tac string.
     *
     * @param count  the count
     * @param amount the amount
     * @return the string
     */
    @Hmily
    String saveOrderForTAC(Integer count, BigDecimal amount);

    /**
     * Test order pay string.
     *
     * @param count  the count
     * @param amount the amount
     * @return the string
     */
    @Hmily
    String testOrderPay(Integer count, BigDecimal amount);

    /**
     * 创建订单并且进行扣除账户余额支付，并进行库存扣减操作
     * in this  Inventory nested in account.
     *
     * @param count  购买数量
     * @param amount 支付金额
     * @return string string
     */
    @Hmily
    String orderPayWithNested(Integer count, BigDecimal amount);

    /**
     * Order pay with nested exception string.
     *
     * @param count  the count
     * @param amount the amount
     * @return the string
     */
    @Hmily
    String orderPayWithNestedException(Integer count, BigDecimal amount);

    /**
     * 模拟在订单支付操作中，库存在try阶段中的库存异常
     *
     * @param count  购买数量
     * @param amount 支付金额
     * @return string string
     */
    @Hmily
    String mockInventoryWithTryException(Integer count, BigDecimal amount);

    /**
     * Mock tac inventory with try exception string.
     *
     * @param count  the count
     * @param amount the amount
     * @return the string
     */
    @Hmily
    String mockTacInventoryWithTryException(Integer count, BigDecimal amount);

    /**
     * 模拟在订单支付操作中，库存在try阶段中的timeout
     *
     * @param count  购买数量
     * @param amount 支付金额
     * @return string string
     */
    @Hmily
    String mockInventoryWithTryTimeout(Integer count, BigDecimal amount);

    /**
     * 模拟在订单支付操作中，账户在try阶段中的异常
     *
     * @param count  购买数量
     * @param amount 支付金额
     * @return string string
     */
    @Hmily
    String mockAccountWithTryException(Integer count, BigDecimal amount);

    /**
     * 模拟在订单支付操作中，账户在try阶段中的timeout超时异常（最后自身成功）
     *
     * @param count  购买数量
     * @param amount 支付金额
     * @return string string
     */
    @Hmily
    String mockAccountWithTryTimeout(Integer count, BigDecimal amount);

    /**
     * 模拟在订单支付操作中，库存在Confirm阶段中的timeout
     *
     * @param count  购买数量
     * @param amount 支付金额
     * @return string string
     */
    @Hmily
    String mockInventoryWithConfirmTimeout(Integer count, BigDecimal amount);

    /**
     * 更新订单状态
     *
     * @param order 订单实体类
     */
    @Hmily
    boolean updateOrderStatus(Order order);
}
