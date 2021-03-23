package com.movo.dubboserviceprovider.order.service.impl;

import com.movo.dubboserviceapi.enmus.OrderStatusEnum;
import com.movo.dubboserviceapi.entity.Order;
import com.movo.dubboserviceapi.service.OrderService;
import com.movo.dubboserviceapi.service.PaymentService;
import com.movo.dubboserviceprovider.order.mapper.OrderMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


@DubboService(version = "${spring.application.service.version}")
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    private final PaymentService paymentService;

    @Autowired(required = false)
    public OrderServiceImpl(OrderMapper orderMapper, PaymentService paymentService) {
        this.orderMapper = orderMapper;
        this.paymentService = paymentService;
    }

    @Override
    public String orderPay(Integer count, BigDecimal amount) {
        Order order = saveOrder(count, amount);
        long start = System.currentTimeMillis();
        paymentService.makePayment(order);
        System.out.println("切面耗时：" + (System.currentTimeMillis() - start));
        return "success";
    }

    @Override
    public String saveOrderForTAC(Integer count, BigDecimal amount) {
        Order order = saveOrder(count, amount);;
        final long start = System.currentTimeMillis();
        paymentService.makePaymentForTAC(order);
        System.out.println("切面耗时：" + (System.currentTimeMillis() - start));
        return "success";
    }

    @Override
    public String testOrderPay(Integer count, BigDecimal amount) {
        Order order = saveOrder(count, amount);
        final long start = System.currentTimeMillis();
        paymentService.testMakePayment(order);
        System.out.println("方法耗时：" + (System.currentTimeMillis() - start));
        return "success";
    }

    /**
     * 创建订单并且进行扣除账户余额支付，并进行库存扣减操作
     * in this  Inventory nested in account.
     *
     * @param count  购买数量
     * @param amount 支付金额
     * @return string
     */
    @Override
    public String orderPayWithNested(Integer count, BigDecimal amount) {
        Order order = saveOrder(count, amount);
        paymentService.makePaymentWithNested(order);
        return "success";
    }

    @Override
    public String orderPayWithNestedException(Integer count, BigDecimal amount) {
        Order order = saveOrder(count, amount);
        paymentService.makePaymentWithNestedException(order);
        return "success";
    }

    /**
     * 模拟在订单支付操作中，库存在try阶段中的库存异常
     *
     * @param count  购买数量
     * @param amount 支付金额
     * @return string
     */
    @Override
    public String mockInventoryWithTryException(Integer count, BigDecimal amount) {
        Order order = saveOrder(count, amount);
        return paymentService.mockPaymentInventoryWithTryException(order);
    }

    @Override
    public String mockTacInventoryWithTryException(Integer count, BigDecimal amount) {
        Order order = saveOrder(count, amount);
        return paymentService.mockTacPaymentInventoryWithTryException(order);
    }

    /**
     * 模拟在订单支付操作中，库存在try阶段中的timeout
     *
     * @param count  购买数量
     * @param amount 支付金额
     * @return string
     */
    @Override
    @Transactional
    public String mockInventoryWithTryTimeout(Integer count, BigDecimal amount) {
        Order order = saveOrder(count, amount);
        paymentService.mockPaymentInventoryWithTryTimeout(order);
        return "success";
    }

    @Override
    public String mockAccountWithTryException(Integer count, BigDecimal amount) {
        Order order = saveOrder(count, amount);
        paymentService.mockPaymentAccountWithTryException(order);
        return "success";
    }

    @Override
    public String mockAccountWithTryTimeout(Integer count, BigDecimal amount) {
        Order order = saveOrder(count, amount);
        paymentService.mockPaymentAccountWithTryTimeout(order);
        return "success";
    }


    /**
     * 模拟在订单支付操作中，库存在Confirm阶段中的timeout
     *
     * @param count  购买数量
     * @param amount 支付金额
     * @return string
     */
    @Override
    public String mockInventoryWithConfirmTimeout(Integer count, BigDecimal amount) {
        Order order = saveOrder(count, amount);
        paymentService.mockPaymentInventoryWithConfirmTimeout(order);
        return "success";
    }

    @Override
    public boolean updateOrderStatus(Order order) {
        return orderMapper.update(order) > 0;
    }

    private Order saveOrder(Integer count, BigDecimal amount) {
        final Order order = buildOrder(count, amount);
        orderMapper.save(order);
        return order;
    }

    private Order buildOrder(Integer count, BigDecimal amount) {
        Order order = new Order();
        order.setCreateTime(new Date());
        order.setNumber(UUID.randomUUID().toString());
        //demo中的表里只有商品id为1的数据
        order.setProductId("1");
        order.setStatus(OrderStatusEnum.NOT_PAY.getCode());
        order.setTotalAmount(amount);
        order.setCount(count);
        //demo中 表里面存的用户id为10000
        order.setUserId("10000");
        return order;
    }

    private Order buildTestOrder(Integer count, BigDecimal amount) {
        Order order = new Order();
        order.setCreateTime(new Date());
        order.setNumber(UUID.randomUUID().toString());
        //demo中的表里只有商品id为1的数据
        order.setProductId("1");
        order.setStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
        order.setTotalAmount(amount);
        order.setCount(count);
        //demo中 表里面存的用户id为10000
        order.setUserId("10000");
        return order;
    }
}
