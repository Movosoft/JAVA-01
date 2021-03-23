package com.movo.dubboserviceapi.service;

import com.movo.dubboserviceapi.dto.InventoryDTO;
import com.movo.dubboserviceapi.entity.Inventory;

import java.util.List;

/**
 * The interface Inventory service.
 *
 */
public interface InventoryService {

    /**
     * 扣减库存操作
     * 这一个tcc接口
     *
     * @param inventoryDTO 库存DTO对象
     * @return true boolean
     */
    Boolean decrease(InventoryDTO inventoryDTO);

    /**
     * Decrease tac boolean.
     *
     * @param inventoryDTO the inventory dto
     * @return the boolean
     */
    Boolean decreaseTAC(InventoryDTO inventoryDTO);

    /**
     * Test in line list.
     *
     * @return the list
     */
    List<InventoryDTO> testInLine();

    /**
     * Test decrease boolean.
     *
     * @param inventoryDTO the inventory dto
     * @return the boolean
     */
    Boolean testDecrease(InventoryDTO inventoryDTO);

    /**
     * 获取商品库存信息
     *
     * @param productId 商品id
     * @return InventoryDO inventory do
     */
    Inventory findByProductId(String productId);

    /**
     * mock扣减库存异常
     *
     * @param inventoryDTO dto对象
     * @return String string
     */
    String mockWithTryException(InventoryDTO inventoryDTO);

    /**
     * mock扣减库存超时
     *
     * @param inventoryDTO dto对象
     * @return String boolean
     */
    Boolean mockWithTryTimeout(InventoryDTO inventoryDTO);

    /**
     * mock 扣减库存confirm超时
     *
     * @param inventoryDTO dto对象
     * @return True boolean
     */
    Boolean mockWithConfirmTimeout(InventoryDTO inventoryDTO);
}
