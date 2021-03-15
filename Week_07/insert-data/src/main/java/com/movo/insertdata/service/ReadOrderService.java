package com.movo.insertdata.service;

import com.alibaba.fastjson.JSONObject;

public interface ReadOrderService {
    JSONObject getOrderList(Integer pageNo, Integer pageSize);
}
