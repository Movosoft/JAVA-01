package com.movo.shardingsphere.demo.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ReadDao {
    List<Map<String, Object>> executeQuery(String sql) throws SQLException;
}
