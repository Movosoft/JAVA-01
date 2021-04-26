package com.movo.account.api.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 人民币账户
 * @author Movo
 * @create 2021/4/23 11:03
 */
@Getter
@Setter
@ToString
public class AccountCny implements Serializable {
    private int userId;
    private BigDecimal amountCny;
    private int state;
}
