package com.fh.service;

import com.fh.entity.po.Commodity;
import com.fh.entity.vo.CommodityPranr;

import java.util.List;

public interface CommodityService {
    List<Commodity> queryCommodityIsSell();

    List<CommodityPranr> queryCommodityTypeById(Integer typeId);

    Commodity queryCommodityById(Integer commodityId);
}
