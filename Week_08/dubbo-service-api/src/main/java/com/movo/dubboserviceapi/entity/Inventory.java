package com.movo.dubboserviceapi.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @auther Movo
 * @create 2021/3/22 11:28
 */
@Data
public class Inventory implements Serializable {
    private Integer id;

    /**
     * 商品id.
     */
    private String productId;

    /**
     * 总库存.
     */
    private Integer totalInventory;

    /**
     * 锁定库存.
     */
    private Integer lockInventory;
}
