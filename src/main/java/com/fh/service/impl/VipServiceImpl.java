package com.fh.service.impl;

import com.fh.dao.VipDao;
import com.fh.entity.po.Vip;
import com.fh.service.VipService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VipServiceImpl implements VipService {
    @Resource
    private VipDao vipDao;

    @Override
    public void addVip(Vip vip) {
        vipDao.insert(vip);
    }

    @Override
    public Vip queryByPhone(long vipPhone) {

        return vipDao.queryByPhone(vipPhone);
    }

    @Override
    public Vip queryByName(String vipName) {
        return vipDao.queryByName(vipName);
    }
}
