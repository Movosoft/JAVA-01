package com.movo.insertdata.controller;

import com.movo.insertdata.service.FastInsertService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FastInsertController {

    private FastInsertService fastInsertService;

    public FastInsertController(final FastInsertService fastInsertService) {
        this.fastInsertService = fastInsertService;
    }

    @RequestMapping("/fastInsert")
    public void fastInsert() {
        System.out.println(System.currentTimeMillis());
        fastInsertService.insert(1000000);
    }
}
