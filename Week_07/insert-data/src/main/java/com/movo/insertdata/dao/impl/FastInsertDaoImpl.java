package com.movo.insertdata.dao.impl;

import com.movo.insertdata.dao.FastInsertDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

@Repository
public class FastInsertDaoImpl implements FastInsertDao {

    @Qualifier("writeDataSource")
    private DataSource writeDataSource;

    public FastInsertDaoImpl(final DataSource writeDataSource) {
        this.writeDataSource = writeDataSource;
    }

    @Override
    public void batchExecute(long startNum, long opNum) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = writeDataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement("select 1");

            // 设置一条insert插入的行数
            int rowsForOneInsert = 30;
            String sql;
            Random random = new Random();
            for(int i = 1;i <= opNum;) {
                sql = "insert into dt_order (order_code,buyer_id,order_status,order_price_i,order_price_d,pay_price_i,pay_price_d,pay_complete_time,order_complete_time,create_time,last_update_time,delete_tag) values ";
                for(int k = 1;k <= rowsForOneInsert;k++) {
                    sql += "('" + String.format("%020d", startNum + i) + "'," + (random.nextInt(100) + 1) + "," + random.nextInt(2) + ",";
                    sql += random.nextInt(10000) + "," + random.nextInt(100) + "," + random.nextInt(10000) + "," + random.nextInt(100) + ",sysdate(),sysdate(),sysdate(),sysdate(),";
                    sql += "0)";
                    i++;
                    if(k == rowsForOneInsert || (i - 1) == opNum) {
                        sql += ";";
                        break;
                    } else {
                        sql += ",";
                    }
                }
                pstmt.addBatch(sql);
            }
            pstmt.executeBatch();
            conn.commit();
        } finally {
            if(pstmt != null) {
                pstmt.close();
            }
            if(conn != null) {
                conn.close();
            }
        }
    }
}
