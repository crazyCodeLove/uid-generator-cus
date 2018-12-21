package com.sse;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author pczhao
 * @email
 * @date 2018-12-20 15:36
 */

@SpringBootApplication
@MapperScan(basePackages = "com.sse.**.mapper")
public class UidGenApp {

    public static void main(String[] args) {
        SpringApplication.run(UidGenApp.class, args);
    }
}
