package com.movo.spring.homework.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Getter
@Setter
@ToString
public class Staff {
    private String staffNum;
    private String staffName;
    @Autowired
    @Qualifier("devDepart")
    private Department belongDepart;

    public void print() {
        System.out.println(this);
    }
}
