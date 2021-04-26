package com.movo.account.api.entity;

import lombok.*;

import java.math.BigDecimal;

/**
 * 人民币账户资产冻结信息
 * @author Movo
 * @create 2021/4/23 13:41
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCnyFreeze {
    private int id;
    private int userId;
    private BigDecimal freezeAmountCny;
    /**
     * 0 - 已失效
     * 1 - 已冻结
     */
    private int freezeState;
}
