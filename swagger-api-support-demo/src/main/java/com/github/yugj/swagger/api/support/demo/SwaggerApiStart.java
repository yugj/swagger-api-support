package com.github.yugj.swagger.api.support.demo;//package cn.migudm.mgmall.bfm.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * @author yugj
 * @date 2019/9/16 下午2:55.
 */
@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class
})
public class SwaggerApiStart {

    /**
     * doc index
     * http://localhost:9001/doc.html
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(SwaggerApiStart.class, args).start();
    }

}
