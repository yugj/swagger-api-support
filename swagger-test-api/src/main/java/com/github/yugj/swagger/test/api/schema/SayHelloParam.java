package com.github.yugj.swagger.test.api.schema;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @author yugj
 * @date 2020/7/23 11:42 上午.
 */
public class SayHelloParam {

    @NotNull
    @ApiModelProperty("姓名")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
