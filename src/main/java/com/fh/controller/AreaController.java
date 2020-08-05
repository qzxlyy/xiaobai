package com.fh.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.JsonData;
import com.fh.entity.po.Area;
import com.fh.service.AreaService;
import com.fh.utlis.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("area")
public class AreaController {
    @Autowired
    private AreaService areaService;

    @RequestMapping("queryArea")
    public JsonData queryArea(){
       List<Area> list =  areaService.queryArea();
       List list1 = new ArrayList();
        for (int i = 0; i <list.size() ; i++) {
            Area area = list.get(i);
            //将地区对象转为json字符串
            String s = JSONObject.toJSONString(area);
            RedisUse.hset("area_qzx",area.getId()+"",s);
            String area_qzx = RedisUse.hget("area_qzx", area.getId() + "");
            Area area1 = JSONObject.parseObject(area_qzx, Area.class);
            list1.add(area1);
        }
       return  JsonData.getJsonSuccess(list);
    }

}
