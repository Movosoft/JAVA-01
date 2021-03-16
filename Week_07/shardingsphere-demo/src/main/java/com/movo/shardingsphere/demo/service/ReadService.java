package com.movo.shardingsphere.demo.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ReadService {
    List<Map<String, Object>> newsTitlePage(Integer pageNo, Integer pageSize) throws SQLException;
}
