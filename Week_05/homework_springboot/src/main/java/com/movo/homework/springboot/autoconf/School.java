package com.movo.homework.springboot.autoconf;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties(prefix = "school")
@Getter
@Setter
@ToString
public class School implements ISchool {

    private Klass klass;
    private Student student;

    @Override
    public void ding() {
        System.out.println(this);
    }
}
