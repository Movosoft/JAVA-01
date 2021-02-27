package com.movo.spring.homework.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Department {
    private String departCode;
    private String departName;

    public void print() {
        System.out.println(this);
    }
}
