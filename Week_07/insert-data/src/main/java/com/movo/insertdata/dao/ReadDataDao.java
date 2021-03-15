package com.movo.insertdata.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.sql.SQLException;

public interface ReadDataDao {
    JSONArray queryForList(String sql) throws SQLException;
    JSONObject queryForObject(String sql) throws SQLException;
}
