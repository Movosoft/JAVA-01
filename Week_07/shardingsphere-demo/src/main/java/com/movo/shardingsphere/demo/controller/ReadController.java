package com.movo.shardingsphere.demo.controller;

import com.movo.shardingsphere.demo.service.ReadService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @auther Movo
 * @create 2021/3/16 8:42
 */
@RestController
public class ReadController {

    private ReadService readService;

    public ReadController(final ReadService readService) {
        this.readService = readService;
    }

    @RequestMapping("/newsTitlePage")
    public List<Map<String, Object>> newsTitlePage(Integer pageNo, Integer pageSize) {
        try {
            return readService.newsTitlePage(pageNo, pageSize);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
