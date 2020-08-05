package com.fh.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.JsonData;
import com.fh.entity.po.Commodity;
import com.fh.entity.vo.CommodityPranr;
import com.fh.service.CommodityService;
import com.fh.utlis.RedisUse;
import com.fh.utlis.RedisUtlis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.List;

@RestController
@RequestMapping("commodity")
public class CommodityController {
    @Autowired
    private CommodityService commodityService;
    @RequestMapping("queryCommodityIsSells")
    public JsonData queryCommodityIsSells(){
        Jedis jedis = RedisUtlis.getJedis();
        String sellCommodity_qzx = jedis.get("sellCommodity_qzx");
        if(StringUtils.isEmpty(sellCommodity_qzx)==true){
            List<Commodity> queryCommodityIsSell =    commodityService.queryCommodityIsSell();
            sellCommodity_qzx = JSONObject.toJSONString(queryCommodityIsSell);
            jedis.set("sellCommodity_qzx",sellCommodity_qzx);
        }
        RedisUtlis.returnJedis(jedis);
        return JsonData.getJsonSuccess(sellCommodity_qzx);
    }
    @RequestMapping("queryCommodityIsSell")
    public JsonData queryCommodityIsSell(){
        String sellCommodity_qzx = RedisUse.get("sellCommodity_qzx");
        return JsonData.getJsonSuccess(sellCommodity_qzx);
    }
    @RequestMapping("queryCommodityTypeById")
    public  JsonData queryCommodityTypeById(Integer typeId){
        List<CommodityPranr> queryCommodityTypeById =  commodityService.queryCommodityTypeById(typeId);
        return JsonData.getJsonSuccess(queryCommodityTypeById);
    }
    @RequestMapping("queryCommodityById")
    public JsonData queryCommodityById(Integer commodityId){
        Commodity commodity =commodityService.queryCommodityById(commodityId);
        return  JsonData.getJsonSuccess(commodity);
    }

}
