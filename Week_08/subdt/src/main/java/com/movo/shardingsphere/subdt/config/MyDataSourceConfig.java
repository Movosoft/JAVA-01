package com.movo.shardingsphere.subdt.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Description
 * @auther Movo
 * @create 2021/3/19 18:50
 */
@Configuration
public class MyDataSourceConfig {
    public DataSource onlineRetailer0() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/online_retailer0?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("root");
        return new HikariDataSource(hikariConfig);
    }

    public DataSource onlineRetailer1() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/online_retailer1?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("root");
        return new HikariDataSource(hikariConfig);
    }

    public ShardingTableRuleConfiguration orderTableRuleConfig() {
        ShardingTableRuleConfiguration orderTableRuleConfig = new ShardingTableRuleConfiguration("dt_order", "online_retailer${0..1}.dt_order${0..15}");
        orderTableRuleConfig.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration("order_id", "snowflake"));
        orderTableRuleConfig.setDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("buyer_id", "dbShardingAlgorithm"));
        orderTableRuleConfig.setTableShardingStrategy(new StandardShardingStrategyConfiguration("order_id", "orderTableShardingAlgorithm"));
        return orderTableRuleConfig;
    }

    public ShardingRuleConfiguration shardingRuleConfig() {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTables().add(orderTableRuleConfig());
        // 配置主键生成算法
        Properties keyGenerateStrategyProps = new Properties();
        keyGenerateStrategyProps.setProperty("column", "order_id");
//        Properties workerProps = new Properties();
//        workerProps.put("id", 123);
//        Properties props = new Properties();
//        props.put("worker", workerProps);
//        keyGenerateStrategyProps.put("props", props);
        shardingRuleConfig.getKeyGenerators().put("snowflake", new ShardingSphereAlgorithmConfiguration("MYSNOWFLAKE", keyGenerateStrategyProps));
        // 配置分库算法
        Properties dbShardingAlgorithmProps = new Properties();
        dbShardingAlgorithmProps.setProperty("algorithm-expression", "online_retailer${buyer_id % 2}");
        shardingRuleConfig.getShardingAlgorithms().put("dbShardingAlgorithm", new ShardingSphereAlgorithmConfiguration("inline", dbShardingAlgorithmProps));
        // 配置分表算法
        Properties orderTableShardingAlgorithmProps = new Properties();
        orderTableShardingAlgorithmProps.setProperty("algorithm-expression", "dt_order${order_id % 16}");
        shardingRuleConfig.getShardingAlgorithms().put("orderTableShardingAlgorithm", new ShardingSphereAlgorithmConfiguration("inline", orderTableShardingAlgorithmProps));
        return shardingRuleConfig;
    }

    @Bean
    public DataSource shardingSphereDs() throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("online_retailer0", onlineRetailer0());
        dataSourceMap.put("online_retailer1", onlineRetailer1());
        return ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, Collections.singleton(shardingRuleConfig()), new Properties());
    }
}
