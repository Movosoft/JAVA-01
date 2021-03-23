package com.movo.dubboserviceprovider.account.service.impl;

import com.movo.dubboserviceapi.dto.InventoryDTO;
import com.movo.dubboserviceapi.service.AccountService;
import com.movo.dubboserviceapi.service.InventoryService;
import com.movo.dubboserviceprovider.account.dto.AccountDTO;
import com.movo.dubboserviceprovider.account.dto.AccountNestedDTO;
import com.movo.dubboserviceprovider.account.entity.Account;
import com.movo.dubboserviceprovider.account.mapper.AccountMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The type Account service.
 *
 */
@DubboService(version = "${spring.application.service.version}")
public class AccountServiceImpl implements AccountService {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    /**
     * The Confrim count.
     */
    private static AtomicInteger confirmCount = new AtomicInteger(0);

    private final AccountMapper accountMapper;

    private final InventoryService inventoryService;

    /**
     * Instantiates a new Account service.
     *
     * @param accountMapper the account mapper
     */
    @Autowired(required = false)
    public AccountServiceImpl(final AccountMapper accountMapper,
                              final InventoryService inventoryService) {
        this.accountMapper = accountMapper;
        this.inventoryService = inventoryService;
    }

    @Override
//    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean payment(AccountDTO accountDTO) {
        int count =  accountMapper.update(accountDTO);
        if (count > 0) {
            return true;
        } else {
//            throw new HmilyRuntimeException("账户扣减异常！");
            return false;
        }
    }

    @Override
//    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean mockTryPaymentException(AccountDTO accountDTO) {
//        throw new HmilyRuntimeException("账户扣减异常！");
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
//    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean mockTryPaymentTimeout(AccountDTO accountDTO) {
        try {
            //模拟延迟 当前线程暂停10秒
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final int decrease = accountMapper.update(accountDTO);
        if (decrease != 1) {
//            throw new HmilyRuntimeException("库存不足");
            return false;
        }
        return true;
    }

    @Override
//    @HmilyTAC
    public boolean paymentTAC(AccountDTO accountDTO) {
        return accountMapper.updateTAC(accountDTO) > 0;
    }

    @Override
    public boolean testPayment(AccountDTO accountDTO) {
        accountMapper.testUpdate(accountDTO);
        return Boolean.TRUE;
    }

    @Override
//    @HmilyTCC(confirmMethod = "confirmNested", cancelMethod = "cancelNested")
    @Transactional(rollbackFor = Exception.class)
    public boolean paymentWithNested(AccountNestedDTO accountNestedDTO) {
        AccountDTO dto = new AccountDTO();
        dto.setAmount(accountNestedDTO.getAmount());
        dto.setUserId(accountNestedDTO.getUserId());
        accountMapper.update(dto);
        InventoryDTO inventoryDTO = new InventoryDTO();
        inventoryDTO.setCount(accountNestedDTO.getCount());
        inventoryDTO.setProductId(accountNestedDTO.getProductId());
        inventoryService.decrease(inventoryDTO);
        return Boolean.TRUE;
    }

    @Override
//    @HmilyTCC(confirmMethod = "confirmNested", cancelMethod = "cancelNested")
    @Transactional(rollbackFor = Exception.class)
    public boolean paymentWithNestedException(AccountNestedDTO accountNestedDTO) {
        AccountDTO dto = new AccountDTO();
        dto.setAmount(accountNestedDTO.getAmount());
        dto.setUserId(accountNestedDTO.getUserId());
        accountMapper.update(dto);
        InventoryDTO inventoryDTO = new InventoryDTO();
        inventoryDTO.setCount(accountNestedDTO.getCount());
        inventoryDTO.setProductId(accountNestedDTO.getProductId());
        inventoryService.decrease(inventoryDTO);
        //下面这个且套服务异常
        inventoryService.mockWithTryException(inventoryDTO);
        return Boolean.TRUE;
    }

    @Override
    public Account findByUserId(String userId) {
        return accountMapper.findByUserId(userId);
    }

    /**
     * Confirm nested boolean.
     *
     * @param accountNestedDTO the account nested dto
     * @return the boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmNested(AccountNestedDTO accountNestedDTO) {
        LOGGER.debug("============dubbo tcc 执行确认付款接口===============");
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserId(accountNestedDTO.getUserId());
        accountDTO.setAmount(accountNestedDTO.getAmount());
        accountMapper.confirm(accountDTO);
        return Boolean.TRUE;
    }

    /**
     * Cancel nested boolean.
     *
     * @param accountNestedDTO the account nested dto
     * @return the boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelNested(AccountNestedDTO accountNestedDTO) {
        LOGGER.debug("============ dubbo tcc 执行取消付款接口===============");
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserId(accountNestedDTO.getUserId());
        accountDTO.setAmount(accountNestedDTO.getAmount());
        accountMapper.cancel(accountDTO);
        return Boolean.TRUE;
    }

    /**
     * Confirm boolean.
     *
     * @param accountDTO the account dto
     * @return the boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean confirm(AccountDTO accountDTO) {
        LOGGER.info("============dubbo tcc 执行确认付款接口===============");
        accountMapper.confirm(accountDTO);
        final int i = confirmCount.incrementAndGet();
        LOGGER.info("调用了account confirm " + i + " 次");
        return Boolean.TRUE;
    }

    /**
     * Cancel boolean.
     *
     * @param accountDTO the account dto
     * @return the boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(AccountDTO accountDTO) {
        LOGGER.info("============ dubbo tcc 执行取消付款接口===============");
        final Account accountDO = accountMapper.findByUserId(accountDTO.getUserId());
        accountMapper.cancel(accountDTO);
        return Boolean.TRUE;
    }
}
