package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.entity.po.OrderDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDetailsDao extends BaseMapper<OrderDetails> {
    void batchAddOrderDetails(@Param("list") List<OrderDetails> list, @Param("id") Integer id);
}
