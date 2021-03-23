package com.movo.shardingsphere.subdt;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SpringBootTest(classes = SubdtApplication.class)
@RunWith(SpringRunner.class)
class SubdtApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Test
    public void test() {
        System.out.println(dataSource);
    }

    @Test
    public void insertData() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Random ran = new Random();
        int max = 1000;
        int count = 0;
        String sql;
        for(int i = 0;i < max;i++) {
            sql = "insert into dt_order values ";
            sql += "(sysdate()," + ran.nextInt(50) + "," + ran.nextInt(2) + "," + ran.nextInt(100000) + "," + ran.nextInt(100) + "," + ran.nextInt(100000) + "," + ran.nextInt(100) + ",sysdate(),sysdate(),sysdate(),sysdate()," + ran.nextInt(2) + ")";
            count += jdbcTemplate.update(sql);
        }
        System.out.println(count);
    }

    @Test
    public void selectData() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String, Object>> orderList = jdbcTemplate.queryForList("select order_id,order_code,buyer_id,order_status,order_price_i,order_price_d,pay_price_i,pay_price_d,pay_complete_time,order_complete_time,create_time,last_update_time,delete_tag from dt_order limit 0,100");
        System.out.println(orderList);
    }

    @Test
    public void updateData() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        int count = jdbcTemplate.update("update dt_order set delete_tag=1 where order_id=580230631632404487");
        System.out.println(count);
    }

    @Test
    public void deleteData() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        int count = jdbcTemplate.update("delete from dt_order");
        System.out.println(count);
    }
}
