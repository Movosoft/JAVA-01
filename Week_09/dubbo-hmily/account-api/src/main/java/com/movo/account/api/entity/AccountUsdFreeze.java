package com.movo.account.api.entity;

import lombok.*;

import java.math.BigDecimal;

/**
 * 美元账户资产冻结信息
 * @author Movo
 * @create 2021/4/23 13:41
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountUsdFreeze {
    private int id;
    private int userId;
    private BigDecimal freezeAmountUsd;
    /**
     * 0 - 已失效
     * 1 - 已冻结
     */
    private int freezeState;
}
