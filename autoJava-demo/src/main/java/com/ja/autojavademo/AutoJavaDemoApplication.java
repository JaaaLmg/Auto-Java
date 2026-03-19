package com.ja.autojavademo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.ja.autojavademo.mappers"})
public class AutoJavaDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutoJavaDemoApplication.class, args);
    }

}
