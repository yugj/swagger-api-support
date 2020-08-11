package com.github.yugj.swagger.api.support.demo;//package cn.migudm.mgmall.bfm.api;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.github.yugj.swagger.api.support.core.debug.MultiEnvClient;
import com.github.yugj.swagger.api.support.core.registry.Interface2BeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author yugj
 * @date 2019/9/17 上午9:33.
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerAutoConfiguration {

    private static final String API_SCAN_PACKAGE = "com.github.yugj.swagger.test.api";

    //默认server url
    private static final String SERVER_URL = "http://localhost:9002";

    @Bean
    public MultiEnvClient multiEnvClient() {
        //default server 4 debug online
        return new MultiEnvClient(SERVER_URL);
    }

    /**
     * 接口转bean空实现自动化配置类
     * @return
     */
    @Bean
    public Interface2BeanDefinitionRegistry interface2BeanDefinitionRegistry() {
        return new Interface2BeanDefinitionRegistry(API_SCAN_PACKAGE);
    }

    @Bean
    public Docket operationDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("用户管理")
                .apiInfo(apiInfo("用户管理接口文档"))
                .select()
                .apis(RequestHandlerSelectors.basePackage(API_SCAN_PACKAGE))
                .paths(PathSelectors.regex("/user/v1.*"))
                .build();
    }


    private ApiInfo apiInfo(String title) {
        return new ApiInfoBuilder()
                .title(title)
                .version("1.0")
                .build();
    }
}
