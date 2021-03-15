package com.movo.insertdata.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.movo.insertdata.dao.ReadDataDao;
import com.movo.insertdata.service.ReadOrderService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class ReadOrderServiceImpl implements ReadOrderService {

    private ReadDataDao readDataDao;

    public ReadOrderServiceImpl(final ReadDataDao readDataDao) {
        this.readDataDao = readDataDao;
    }

    @Override
    public JSONObject getOrderList(Integer pageNo, Integer pageSize) {
        JSONObject jsonObject = new JSONObject();
        try {
            if(pageNo == null || pageNo < 1) {
                pageNo = 1;
            }
            if(pageSize == null || pageSize < 1) {
                pageSize = 20;
            }
            JSONObject totalJson = readDataDao.queryForObject("select count(1) as total from dt_order");
            long total = totalJson.getLongValue("total");
            jsonObject.put("total", total);
            int totalPage = (int)(total + pageSize - 1) / pageSize;
            if(pageNo.intValue() > totalPage) {
                pageNo = totalPage;
            }
            jsonObject.put("totalPage", totalPage);
            jsonObject.put("pageNo", pageNo);
            jsonObject.put("pageSize", pageSize);
            JSONArray rows = readDataDao.queryForList("select order_code,pay_price_i,pay_price_d from dt_order limit " + (pageNo - 1) * pageSize + "," + pageSize);
            jsonObject.put("rows", rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
