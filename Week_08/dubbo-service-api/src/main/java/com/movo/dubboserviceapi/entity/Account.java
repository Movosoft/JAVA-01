package com.movo.dubboserviceapi.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @auther Movo
 * @create 2021/3/22 11:03
 */
@Data
public class Account implements Serializable {
    private Integer id;

    private String userId;

    private BigDecimal balance;

    private BigDecimal freezeAmount;

    private Date createTime;

    private Date updateTime;
}
