package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.entity.po.Commodity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartDao extends BaseMapper<Commodity> {

}
