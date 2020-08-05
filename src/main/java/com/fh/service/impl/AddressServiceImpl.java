package com.fh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.dao.AddressDao;
import com.fh.entity.po.Address;
import com.fh.entity.po.Vip;
import com.fh.service.AddressService;
import com.fh.utlis.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Resource
    private AddressDao addressDao;

    @Autowired
    private HttpServletRequest request;

    @Override
    public List<Address> queryAddress() {
        Vip login_user = (Vip) request.getAttribute("login_user");
        long vipPhone = login_user.getVipPhone();
        //根据登陆人查询到收货地址
        QueryWrapper qw= new QueryWrapper();
        qw.eq("vipId",vipPhone);
        //查询数据库
        List<Address> addresses = addressDao.selectList(qw);
        //将地区转换成汉字
        List list =new ArrayList();
        for (int i = 0; i <addresses.size() ; i++) {
            Address address = addresses.get(i);
            String area = address.getAreaIds();
            String areaName = RedisUse.getAreaName(area);
            address.setAreaIdsDetail(areaName+address.getDetailAdd());
            list.add(address);
        }
        return list;
    }

    @Override
    public void addAddress(Address address) {
        Vip login_user = (Vip) request.getAttribute("login_user");
        long vipPhone = login_user.getVipPhone();
        address.setVipId(vipPhone);
        address.setCreateDate(new Date());
        addressDao.insert(address);
    }
}
