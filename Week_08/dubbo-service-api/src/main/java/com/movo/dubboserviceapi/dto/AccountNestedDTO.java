package com.movo.dubboserviceprovider.account.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The type Account nested dto.
 *
 */
@Data
public class AccountNestedDTO implements Serializable {

    /**
     * 用户id.
     */
    private String userId;

    /**
     * 扣款金额.
     */
    private BigDecimal amount;

    /**
     * productId.
     */
    private String productId;

    /**
     * count.
     */
    private Integer count;
}
