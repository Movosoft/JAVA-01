package com.movo.dubboserviceapi.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @auther Movo
 * @create 2021/3/22 11:36
 */
@Data
public class Order {
    private Integer id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 订单编号
     */
    private String number;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 商品id
     */
    private String productId;

    /**
     * 付款金额
     */
    private BigDecimal totalAmount;

    /**
     * 购买数量
     */
    private Integer count;

    /**
     * 购买人
     */
    private String userId;
}
