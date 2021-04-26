package com.movo.account.mapper;

import com.movo.account.api.entity.AccountCnyFreeze;
import com.movo.account.api.entity.AccountUsdFreeze;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

/**
 * @author Movo
 */
@Mapper
public interface FreezeAccountMapper {
    /**
     * 插入人民币账户冻结信息
     * @param accountCnyFreeze
     * @return int
     */
    @Insert("insert into dt_account_cny_freeze values (null, #{userId}, #{freezeAmountCny}, 1)")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void addAccountCnyFreeze(AccountCnyFreeze accountCnyFreeze);

    /**
     * 根据id获取冻结人民币金额
     * @param id
     * @return
     */
    @Select("select freeze_amount_cny from dt_account_cny_freeze where id=#{id} and freeze_state=1")
    BigDecimal getAccountCnyFreezeAmount(int id);

    /**
     * 失效指定id的冻结人民币账户信息
     * @param id
     */
    @Update("update dt_account_cny_freeze set freeze_state=0 where id=#{id}")
    void invalidAccountCnyFreeze(int id);

    /**
     * 插入美元账户冻结信息
     * @param accountUsdFreeze
     * @return int
     */
    @Insert("insert into dt_account_usd_freeze values (null, #{userId}, #{freezeAmountUsd}, 1)")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void addAccountUsdFreeze(AccountUsdFreeze accountUsdFreeze);

    /**
     * 根据id获取冻结美元金额
     * @param id
     * @return
     */
    @Select("select freezeAmountUsd from dt_account_usd_freeze where id=#{id} and freeze_state=1")
    BigDecimal getAccountUsdFreezeAmount(int id);

    /**
     * 失效指定id的冻结美元账户信息
     * @param id
     */
    @Update("update dt_account_usd_freeze set freeze_state=0 where id=#{id}")
    void invalidAccountUsdFreeze(int id);

}
