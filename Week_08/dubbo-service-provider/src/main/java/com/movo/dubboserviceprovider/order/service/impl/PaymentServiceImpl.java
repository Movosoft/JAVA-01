package com.movo.dubboserviceprovider.order.service.impl;

import com.movo.dubboserviceapi.dto.InventoryDTO;
import com.movo.dubboserviceapi.enmus.OrderStatusEnum;
import com.movo.dubboserviceapi.entity.Order;
import com.movo.dubboserviceapi.service.AccountService;
import com.movo.dubboserviceapi.service.InventoryService;
import com.movo.dubboserviceapi.service.PaymentService;
import com.movo.dubboserviceprovider.account.dto.AccountDTO;
import com.movo.dubboserviceprovider.account.dto.AccountNestedDTO;
import com.movo.dubboserviceprovider.account.entity.Account;
import com.movo.dubboserviceprovider.order.mapper.OrderMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DubboService(version = "${spring.application.service.version}")
public class PaymentServiceImpl implements PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final OrderMapper orderMapper;

    private final AccountService accountService;

    private final InventoryService inventoryService;

    @Autowired(required = false)
    public PaymentServiceImpl(OrderMapper orderMapper,
                              AccountService accountService,
                              InventoryService inventoryService) {
        this.orderMapper = orderMapper;
        this.accountService = accountService;
        this.inventoryService = inventoryService;
    }

    @Override
//    @HmilyTCC(confirmMethod = "confirmOrderStatus", cancelMethod = "cancelOrderStatus")
    public void makePayment(Order order) {
        updateOrderStatus(order, OrderStatusEnum.PAYING);
        //做库存和资金账户的检验工作 这里只是demo 。。。
       /* final AccountDO accountDO = accountService.findByUserId(order.getUserId());
        if (accountDO.getBalance().compareTo(order.getTotalAmount()) <= 0) {
            throw new HmilyRuntimeException("余额不足！");
        }
        final InventoryDO inventory = inventoryService.findByProductId(order.getProductId());

        if (inventory.getTotalInventory() < order.getCount()) {
            throw new HmilyRuntimeException("库存不足！");
        }*/
        //扣除用户余额
        accountService.payment(buildAccountDTO(order));
        //进入扣减库存操作
        inventoryService.decrease(buildInventoryDTO(order));
    }

    @Override
//    @HmilyTAC
    public void makePaymentForTAC(Order order) {
        updateOrderStatus(order, OrderStatusEnum.PAY_SUCCESS);
        //扣除用户余额
        accountService.paymentTAC(buildAccountDTO(order));
        //进入扣减库存操作
        inventoryService.decreaseTAC(buildInventoryDTO(order));
    }

    @Override
    public void testMakePayment(Order order) {
        updateOrderStatus(order, OrderStatusEnum.PAYING);
        //扣除用户余额
        accountService.testPayment(buildAccountDTO(order));
        //进入扣减库存操作
        inventoryService.testDecrease(buildInventoryDTO(order));
    }

    /**
     * 订单支付
     *
     * @param order 订单实体
     */
    @Override
//    @HmilyTCC(confirmMethod = "confirmOrderStatus", cancelMethod = "cancelOrderStatus")
    public void makePaymentWithNested(Order order) {
        updateOrderStatus(order, OrderStatusEnum.PAYING);
        final Account account = accountService.findByUserId(order.getUserId());
        if (account.getBalance().compareTo(order.getTotalAmount()) <= 0) {
//            throw new HmilyRuntimeException("余额不足！");
        }
        //扣除用户余额
        accountService.paymentWithNested(buildAccountNestedDTO(order));
    }

    @Override
//    @HmilyTCC(confirmMethod = "confirmOrderStatus", cancelMethod = "cancelOrderStatus")
    public void makePaymentWithNestedException(Order order) {
        updateOrderStatus(order, OrderStatusEnum.PAYING);
        final Account account = accountService.findByUserId(order.getUserId());
        if (account.getBalance().compareTo(order.getTotalAmount()) <= 0) {
//            throw new HmilyRuntimeException("余额不足！");
        }
        //扣除用户余额
        accountService.paymentWithNestedException(buildAccountNestedDTO(order));
    }

    @Override
//    @HmilyTCC(confirmMethod = "confirmOrderStatus", cancelMethod = "cancelOrderStatus")
    public String mockPaymentInventoryWithTryException(Order order) {
        updateOrderStatus(order, OrderStatusEnum.PAYING);
        //扣除用户余额
        accountService.payment(buildAccountDTO(order));
        inventoryService.mockWithTryException(buildInventoryDTO(order));
        return "success";
    }

    @Override
//    @HmilyTAC
    @Transactional
    public String mockTacPaymentInventoryWithTryException(Order order) {
        updateOrderStatus(order, OrderStatusEnum.PAY_SUCCESS);
        accountService.paymentTAC(buildAccountDTO(order));
        throw new RuntimeException("");
    }

    @Override
//    @HmilyTCC(confirmMethod = "confirmOrderStatus", cancelMethod = "cancelOrderStatus")
    public String mockPaymentInventoryWithTryTimeout(Order order) {
        updateOrderStatus(order, OrderStatusEnum.PAYING);
        //扣除用户余额
        accountService.payment(buildAccountDTO(order));
        inventoryService.mockWithTryTimeout(buildInventoryDTO(order));
        return "success";
    }

    @Override
//    @HmilyTCC(confirmMethod = "confirmOrderStatus", cancelMethod = "cancelOrderStatus")
    public String mockPaymentAccountWithTryException(Order order) {
        updateOrderStatus(order, OrderStatusEnum.PAYING);
        accountService.mockTryPaymentException(buildAccountDTO(order));
        return "success";
    }

    @Override
//    @HmilyTCC(confirmMethod = "confirmOrderStatus", cancelMethod = "cancelOrderStatus")
    public String mockPaymentAccountWithTryTimeout(Order order) {
        updateOrderStatus(order, OrderStatusEnum.PAYING);
        accountService.mockTryPaymentTimeout(buildAccountDTO(order));
        return "success";
    }

    @Override
//    @HmilyTCC(confirmMethod = "confirmOrderStatus", cancelMethod = "cancelOrderStatus")
    public String mockPaymentInventoryWithConfirmTimeout(Order order) {
        updateOrderStatus(order, OrderStatusEnum.PAYING);
        //扣除用户余额
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAmount(order.getTotalAmount());
        accountDTO.setUserId(order.getUserId());
        accountService.payment(accountDTO);
        inventoryService.mockWithConfirmTimeout(buildInventoryDTO(order));
        return "success";
    }

    public void confirmOrderStatus(Order order) {
        updateOrderStatus(order, OrderStatusEnum.PAY_SUCCESS);
        LOGGER.info("=========进行订单confirm操作完成================");
    }

    public void cancelOrderStatus(Order order) {
        updateOrderStatus(order, OrderStatusEnum.PAY_FAIL);
        LOGGER.info("=========进行订单cancel操作完成================");
    }

    private void updateOrderStatus(Order order, OrderStatusEnum orderStatus) {
        order.setStatus(orderStatus.getCode());
        orderMapper.update(order);
    }

    private AccountDTO buildAccountDTO(Order order) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAmount(order.getTotalAmount());
        accountDTO.setUserId(order.getUserId());
        return accountDTO;
    }

    private AccountNestedDTO buildAccountNestedDTO(Order order) {
        AccountNestedDTO nestedDTO = new AccountNestedDTO();
        nestedDTO.setAmount(order.getTotalAmount());
        nestedDTO.setUserId(order.getUserId());
        nestedDTO.setProductId(order.getProductId());
        nestedDTO.setCount(order.getCount());
        return nestedDTO;
    }

    private InventoryDTO buildInventoryDTO(Order order) {
        InventoryDTO inventoryDTO = new InventoryDTO();
        inventoryDTO.setCount(order.getCount());
        inventoryDTO.setProductId(order.getProductId());
        return inventoryDTO;
    }
}
