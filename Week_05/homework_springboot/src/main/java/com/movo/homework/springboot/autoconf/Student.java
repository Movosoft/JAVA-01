package com.movo.homework.springboot.autoconf;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Student {
    private int id;
    private String name;

    public void println() {
        System.out.println(this);
    }
}
