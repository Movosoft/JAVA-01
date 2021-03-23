package com.movo.shardingsphere.subdt.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @Description
 * @auther Movo
 * @create 2021/3/19 9:58
 */
@Getter
@Setter
public class Order {
    private Long orderId;
    private String orderCode;
    private Integer buyerId;
    private Integer orderStatus;
    private Integer orderPriceI;
    private Integer orderPriceD;
    private Integer payPriceI;
    private Integer payPriceD;
    private Timestamp payCompleteTime;
    private Timestamp orderCompleteTime;
    private Timestamp createTime;
    private Timestamp lastUpdateTime;
    private Boolean deleteTag;
}
