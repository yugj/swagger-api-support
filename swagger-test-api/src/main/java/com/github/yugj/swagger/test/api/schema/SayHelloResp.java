package com.github.yugj.swagger.test.api.schema;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author yugj
 * @date 2020/7/23 11:43 上午.
 */
public class SayHelloResp  {

    public SayHelloResp() {

    }

    public SayHelloResp(String hi) {
        this.hi = hi;
    }
    @ApiModelProperty("hi demo test")
    private String hi;

    public String getHi() {
        return hi;
    }

    public void setHi(String hi) {
        this.hi = hi;
    }
}
