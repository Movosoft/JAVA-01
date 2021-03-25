package com.movo.dubboserviceprovider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.movo.dubboserviceprovider.*.mapper")
public class DubboServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboServiceProviderApplication.class, args);
    }

}
