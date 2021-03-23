package com.movo.dubboserviceapi.dto;

import lombok.Data;

/**
 * @Description
 * @auther Movo
 * @create 2021/3/22 11:34
 */
@Data
public class InventoryDTO {
    /**
     * 商品id.
     */
    private String productId;

    /**
     * 数量.
     */
    private Integer count;
}
