package com.movo.shardingsphere.demo.controller;

import com.movo.shardingsphere.demo.service.WriteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

/**
 * @Description
 * @auther Movo
 * @create 2021/3/16 10:15
 */
@RestController
public class WriteController {
    private WriteService writeService;

    public WriteController(final WriteService writeService) {
        this.writeService = writeService;
    }

    @RequestMapping("/insertNewsTitle")
    public int insertNewsTitle(String newsTitle, Integer newsUnit) {
        if(newsTitle != null && newsUnit != null) {
            newsTitle = newsTitle.replaceAll("'", "");
            if(newsTitle != null) {
                try {
                    return writeService.insertNewsTitle(newsTitle, newsUnit);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }
}
