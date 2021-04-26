package com.movo.account.mapper;

import com.movo.account.api.entity.AccountCny;
import com.movo.account.api.entity.AccountUsd;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * @author Movo
 */
@Mapper
public interface AccountMapper {
    /**
     * 通过userId查找对应人员人民币账户
     * @param userId
     * @return
     */
    @Select("select * from dt_account_cny where user_id=#{userId}")
    AccountCny findCnyAccount(int userId);

    /**
     * 增加人民币账户金额
     * @param userId
     * @param amount
     * @return
     */
    @Update("update dt_account_cny set amount_cny=amount_cny+#{amount} where user_id=#{userId}")
    int increaseCny(int userId, BigDecimal amount);

    /**
     * 减少人民币账户金额
     * @param userId
     * @param amount
     * @return
     */
    @Update("update dt_account_cny set amount_cny=amount_cny-#{amount} where user_id=#{userId}")
    int decreaseCny(int userId, BigDecimal amount);

    /**
     * 通过userId查找对应人员美元账户
     * @param userId
     * @return
     */
    @Select("select * from dt_account_usd where user_id=#{userId}")
    AccountUsd findUsdAccount(int userId);

    /**
     * 增加美元账户金额
     * @param userId
     * @param amount
     * @return
     */
    @Update("update dt_account_usd set amount_usd=amount_usd+#{amount} where user_id=#{userId}")
    int increaseUsd(int userId, BigDecimal amount);

    /**
     * 减少人美元币账户金额
     * @param userId
     * @param amount
     * @return
     */
    @Update("update dt_account_usd set amount_usd=amount_usd-#{amount} where user_id=#{userId}")
    int decreaseUsd(int userId, BigDecimal amount);
}
