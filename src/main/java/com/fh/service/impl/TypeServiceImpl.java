package com.fh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.dao.TypeDao;
import com.fh.entity.po.Type;
import com.fh.service.TypeService;
import com.fh.utlis.RedisUse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {
    @Resource
    private TypeDao typeDao;


    @Override
    public List<Type> queryType1() {
            List<Type> types = typeDao.selectList(null);
        return types;
    }
}
