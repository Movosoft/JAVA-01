package com.movo.homework.springboot.controller;

import com.movo.homework.springboot.autoconf.School;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SchoolController {
    @Resource
    private School school;

    @RequestMapping("/showSchool")
    School showSchool() {
        school.ding();
        return school;
    }
}
