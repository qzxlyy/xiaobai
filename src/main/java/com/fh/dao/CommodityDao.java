package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.entity.po.Commodity;
import com.fh.entity.vo.CommodityCart;
import com.fh.entity.vo.CommodityPranr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommodityDao extends BaseMapper<Commodity> {
    List<Commodity> queryCommodityIsSell();

    List<CommodityPranr> queryCommodityTypeById(Integer typeId);

    CommodityCart queryCommodityCartById(Integer commodityId);

    int updateCommodityStock(@Param("count") Integer count, @Param("commodityId") Integer commodityId);
}
