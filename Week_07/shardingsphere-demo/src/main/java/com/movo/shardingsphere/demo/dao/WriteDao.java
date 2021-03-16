package com.movo.shardingsphere.demo.dao;

import java.sql.SQLException;

public interface WriteDao {
    int updateExecute(String sql) throws SQLException;
}
