package com.movo.insertdata.controller;

import com.alibaba.fastjson.JSONObject;
import com.movo.insertdata.service.ReadOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReadDataController {
    private ReadOrderService readOrderService;

    public ReadDataController(final ReadOrderService readOrderService) {
        this.readOrderService = readOrderService;
    }

    @RequestMapping("/getOrderList")
    public JSONObject getOrderList(Integer pageNo, Integer pageSize) {
        return readOrderService.getOrderList(100, 20);
    }
}
