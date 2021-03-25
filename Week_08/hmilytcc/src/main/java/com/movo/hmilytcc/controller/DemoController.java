package com.movo.hmilytcc.controller;

import com.movo.dubboserviceapi.entity.Order;
import com.movo.dubboserviceapi.service.AccountService;
import com.movo.dubboserviceapi.service.PaymentService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @Description
 * @auther Movo
 * @create 2021/3/22 17:14
 */
@RestController
public class DemoController {
    @DubboReference(version = "${spring.application.service.version}", url = "${spring.application.service.url}")
    private AccountService accountService;
    @DubboReference(version = "${spring.application.service.version}", url = "${spring.application.service.url}")
    private PaymentService paymentService;

    @RequestMapping("/test")
    public void test() {
        Order order = new Order();
        order.setId(1);
        order.setUserId("1");
        order.setCount(5);
        order.setNumber("5000");
        order.setProductId("1");
        order.setTotalAmount(new BigDecimal(666));
        paymentService.makePayment(order);
    }
}
