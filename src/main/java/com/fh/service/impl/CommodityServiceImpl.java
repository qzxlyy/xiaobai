package com.fh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.dao.CommodityDao;
import com.fh.entity.po.Area;
import com.fh.entity.po.Commodity;
import com.fh.entity.vo.CommodityPranr;
import com.fh.service.CommodityService;
import com.fh.utlis.RedisUse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommodityServiceImpl implements CommodityService {
    @Resource
    private CommodityDao commodityDao;

    @Override
    public List<Commodity> queryCommodityIsSell() {

        return commodityDao.queryCommodityIsSell();
    }

    @Override
    public List<CommodityPranr> queryCommodityTypeById(Integer typeId) {

        List<CommodityPranr> commodityPranrsList =commodityDao.queryCommodityTypeById(typeId);
        return commodityPranrsList;
    }

    @Override
    public Commodity queryCommodityById(Integer commodityId) {
        Commodity commodity = commodityDao.selectById(commodityId);
        String[] split = commodity.getArea().split(",");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i <split.length ; i++) {
            String area_qzx = RedisUse.hget("area_qzx", split[i]);
            //将json字符串java对象
            Area area = JSONObject.parseObject(area_qzx, Area.class);
            //进行拼接
            sb.append(area.getName()).append(" ");
        }
        commodity.setArea(sb.toString());
        return commodity;
    }
}
