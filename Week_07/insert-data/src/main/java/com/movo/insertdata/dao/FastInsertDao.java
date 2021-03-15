package com.movo.insertdata.dao;

import java.sql.SQLException;

public interface FastInsertDao {
    void batchExecute(long startNum, long opNum) throws SQLException;
}
