package com.movo.dubboserviceapi.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The type Account dto.
 *
 */
@Data
public class AccountDTO implements Serializable {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 扣款金额
     */
    private BigDecimal amount;

}
