package com.movo.insertdata.dao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.movo.insertdata.dao.ReadDataDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class ReadDataDaoImpl implements ReadDataDao {
    @Qualifier("read1DataSource")
    private DataSource read1DataSource;

    public ReadDataDaoImpl(final DataSource read1DataSource) {
        this.read1DataSource = read1DataSource;
    }

    @Override
    public JSONArray queryForList(String sql) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = read1DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int colNum = rsmd.getColumnCount();
            JSONObject jsonObject;
            while(rs.next()) {
                jsonObject = new JSONObject();
                for(int i = 1;i <= colNum;i++) {
                    jsonObject.put(rsmd.getColumnName(i), rs.getString(i));
                }
                jsonArray.add(jsonObject);
            }
            return jsonArray;
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

    @Override
    public JSONObject queryForObject(String sql) throws SQLException {
        JSONObject jsonObject = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = read1DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int colNum = rsmd.getColumnCount();
            if(rs.next()) {
                jsonObject = new JSONObject();
                for(int i = 1;i <= colNum;i++) {
                    jsonObject.put(rsmd.getColumnName(i), rs.getString(i));
                }
            }
            return jsonObject;
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
