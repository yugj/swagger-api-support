package com.github.yugj.swagger.test.api;

import com.github.yugj.swagger.test.api.schema.SayHelloParam;
import com.github.yugj.swagger.test.api.schema.SayHelloResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户接口实现继承及swagger文档接口
 * @author yugj
 * @date 2020/7/23 11:40 上午.
 */
@Api(tags = "用户管理demo")
@RequestMapping("/user/v1")
@RestController
public interface UserApi {

    @ApiOperation("say hello 问候")
    @PostMapping("/sayHello")
    SayHelloResp sayHello(@Valid @RequestBody SayHelloParam param);

}
