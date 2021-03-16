package com.movo.shardingsphere.demo.service;

import java.sql.SQLException;

public interface WriteService {
    int insertNewsTitle(String newsTitle, int newsUnit) throws SQLException;
}
