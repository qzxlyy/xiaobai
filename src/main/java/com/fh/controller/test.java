package com.fh.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "测试接口")
@RestController
@RequestMapping("test")
public class test {

    @RequestMapping("test")
    @ApiOperation("测试方法")
    public String test(){
        System.out.println("大傻逼");
        return "你是";
    }
}

