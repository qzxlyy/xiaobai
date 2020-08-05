package com.fh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.dao.AreaDao;
import com.fh.entity.po.Area;
import com.fh.service.AreaService;
import com.fh.utlis.RedisUse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AreaSeviceImpl implements AreaService {
    @Resource
    private AreaDao areaDao;

    @Override
    public List<Area> queryArea() {
        List<Area> areas = areaDao.selectList(null);
        return areas;
    }
}
