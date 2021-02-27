package com.movo.homework.springboot.autoconf;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@ToString
@Service
public class Klass {
    private List<Student> students;

    public void println() {
        System.out.println(this);
    }
}
