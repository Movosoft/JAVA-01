package com.movo.shardingsphere.demo.service.impl;

import com.movo.shardingsphere.demo.dao.ReadDao;
import com.movo.shardingsphere.demo.service.ReadService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @auther Movo
 * @create 2021/3/16 8:33
 */
@Service
public class ReadServiceImpl implements ReadService {

    private ReadDao readDao;

    public ReadServiceImpl(final ReadDao readDao) {
        this.readDao = readDao;
    }

    @Override
    public List<Map<String, Object>> newsTitlePage(Integer pageNo, Integer pageSize) throws SQLException {
        if(pageNo == null || pageNo < 1) {
            pageNo = 1;
        }
        if(pageSize == null || pageSize < 1) {
            pageSize = 20;
        }
        return readDao.executeQuery("select news_title from tb_news_title limit " + (pageNo - 1) * pageSize + "," + pageSize);
    }
}
