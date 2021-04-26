package com.movo.account.api.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 美元账户
 * @author Movo
 * @create 2021/4/23 11:07
 */
@Getter
@Setter
@ToString
public class AccountUsd implements Serializable {
    private int userId;
    private BigDecimal amountUsd;
    private int state;
}
