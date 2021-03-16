package com.movo.shardingsphere.demo.dao.impl;

import com.movo.shardingsphere.demo.dao.ReadDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * @Description
 * @auther Movo
 * @create 2021/3/15 17:02
 */
@Repository
public class ReadDaoImpl implements ReadDao {

    @Qualifier("movo1")
    private DataSource dataSource;

    public ReadDaoImpl(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Map<String, Object>> executeQuery(String sql) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            Map<String, Object> map;
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            while(rs.next()) {
                map = new HashMap<>();
                for(int i = 1;i <= colCount;i++) {
                    map.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                list.add(map);
            }
            return list;
        } finally {
            if(rs != null) {
                 rs.close();
            }
            if(pstmt != null) {
                pstmt.close();
            }
            if(conn != null) {
                conn.close();
            }
        }
    }
}
