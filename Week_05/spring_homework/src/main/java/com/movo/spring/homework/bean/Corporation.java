package com.movo.spring.homework.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Corporation {
    private String corporationCode;
    private String corporationName;
    private List<Department> departs;

    public void print() {
        System.out.println(this);
    }
}
