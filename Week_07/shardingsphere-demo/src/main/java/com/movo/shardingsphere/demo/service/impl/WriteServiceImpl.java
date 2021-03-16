package com.movo.shardingsphere.demo.service.impl;

import com.movo.shardingsphere.demo.dao.WriteDao;
import com.movo.shardingsphere.demo.service.WriteService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * @Description
 * @auther Movo
 * @create 2021/3/16 10:03
 */
@Service
public class WriteServiceImpl implements WriteService {

    private WriteDao writeDao;

    public WriteServiceImpl(final WriteDao writeDao) {
        this.writeDao = writeDao;
    }

    @Override
    public int insertNewsTitle(String newsTitle, int newsUnit) throws SQLException {
        return this.writeDao.updateExecute("insert into tb_news_title values ('" + newsTitle.replaceAll("'", "") + "', 1)");
    }
}
