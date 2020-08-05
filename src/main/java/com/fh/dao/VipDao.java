package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.entity.po.Vip;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VipDao extends BaseMapper<Vip> {
    Vip queryByPhone(long vipPhone);

    Vip queryByName(String vipName);
}
