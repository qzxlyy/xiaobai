package com.fh.service;

import com.fh.common.exception.CountException;
import com.fh.entity.po.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Map addOrder(Integer addressId, Integer payentId) throws CountException;

    Integer queryCode2(Integer id) throws Exception;

    Map createCode2(Integer id) throws Exception;

    void updatePayStatus(Integer id);

    List<Order> orderService();

    void delOrder(Integer id);
}
