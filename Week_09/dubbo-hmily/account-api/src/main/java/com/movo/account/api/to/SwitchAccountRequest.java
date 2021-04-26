package com.movo.account.api.to;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 账户间资金往来请求信息
 * @author Movo
 * @create 2021/4/23 11:37
 */
@Getter
@Setter
@ToString
public class SwitchAccountRequest implements Serializable {
    /**
     * 转出方用户ID
     */
    private int sourceUserId;
    /**
     * 转出方货币类型 CNY 或 USD
     */
    private String sourceType;
    /**
     * 转出金额
     */
    private BigDecimal sourceAmount;
    /**
     * 冻结资金id
     */
    private int freezeId;
    /**
     * 转入方用户ID
     */
    private int targetUserId;
    /**
     * 转入方货币类型 CNY 或 USD
     */
    private String targetType;
}
