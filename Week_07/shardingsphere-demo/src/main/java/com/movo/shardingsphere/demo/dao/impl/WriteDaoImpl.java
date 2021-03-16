package com.movo.shardingsphere.demo.dao.impl;

import com.movo.shardingsphere.demo.dao.WriteDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @auther Movo
 * @create 2021/3/15 17:02
 */
@Repository
public class WriteDaoImpl implements WriteDao {

    @Qualifier("movo")
    private DataSource dataSource;

    public WriteDaoImpl(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int updateExecute(String sql) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            return pstmt.executeUpdate();
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
