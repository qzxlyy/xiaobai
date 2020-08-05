package com.fh.service;

import com.fh.entity.vo.CommodityCart;

import java.util.List;

public interface CartService {
    Integer addCommodityToCart(Integer commodityId, Integer count);

    void checkedCommodityStatus(String checkedId);

    void deleteCommodityCart(Integer commodityId);

    void deleteCommodityCartAll(List<Integer> commodityId);

    List<CommodityCart> queryCheckCommodity();

}
