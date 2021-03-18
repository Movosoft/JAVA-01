package com.movo.shardingsphere.subdt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;

@SpringBootApplication(exclude = JtaAutoConfiguration.class)
public class SubdtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubdtApplication.class, args);
    }

}
