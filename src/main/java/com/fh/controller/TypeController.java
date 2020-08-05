package com.fh.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.JsonData;
import com.fh.entity.po.Type;
import com.fh.service.TypeService;
import com.fh.utlis.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("type")
public class TypeController {
    @Autowired
    private TypeService typeService;

    @RequestMapping("queryType1")
    public JsonData queryType1(){
        String type_qzx = RedisUse.get("type_qzx");
        if(StringUtils.isEmpty(type_qzx)==true){
            List<Type> types = typeService.queryType1();
            type_qzx= JSONObject.toJSONString(types);
            RedisUse.set("type_qzx",type_qzx);
        }
        return  JsonData.getJsonSuccess(type_qzx);
    }

}
