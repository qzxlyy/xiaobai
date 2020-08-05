package com.fh.controller;

import com.fh.common.JsonData;
import com.fh.common.exception.CountException;
import com.fh.entity.po.Order;
import com.fh.service.OrderService;
import com.fh.utlis.RedisUse;
import com.fh.utlis.WeiXiPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("addOrder")
    public JsonData addOrder(Integer addressId , Integer payentId,String sendFlag) throws CountException {
        //处理幂等性同一个请求发送多次 只能通过一个
        boolean exists = RedisUse.exists(sendFlag);
        if(exists==true){//如果相等就是同一个
            return  JsonData.getJsonEror(300,"请求处理中");
        }else {
            RedisUse.set(sendFlag,"",10);
        }
        Map map =   orderService.addOrder(addressId,payentId);
        return JsonData.getJsonSuccess(map);
    }
    @RequestMapping("createCode")
    public  JsonData createCode(String orderId,String totalMoney) throws Exception {
        Map map =new HashMap();
        Map code = WeiXiPay.createCode(orderId,totalMoney);
        map.put("code",code);
        map.put("totalMoney",totalMoney);
        map.put("orderId",orderId);
        System.out.println(code);
        return JsonData.getJsonSuccess(map);
    }
    @RequestMapping("createCode2")
    public  JsonData createCode2(Integer id) throws Exception {
        Map order =   orderService.createCode2(id);
        return JsonData.getJsonSuccess(order);
    }
    //1 支付成功 2 支付中 3 未支付
    @RequestMapping("queryCode")
    public  JsonData queryCode(String orderId) throws Exception {
        Map code = WeiXiPay.queryCode(orderId);
        return JsonData.getJsonSuccess(code);
    }
    //1 支付成功 2 支付中 3 未支付
    @RequestMapping("queryCode2")
    public  JsonData queryCode2(Integer id) throws Exception {
        Integer status = orderService.queryCode2(id);
        return JsonData.getJsonSuccess(status);
    }
    @RequestMapping("updatePayStatus")
    public JsonData updatePayStatus(Integer id){
        orderService.updatePayStatus(id);
        return JsonData.getJsonSuccess("修改支付状态成功");
    }
    @RequestMapping("queryOrder")
    public  JsonData queryOrder(){
        List<Order> list =  orderService.orderService();
        return JsonData.getJsonSuccess(list);
    }
    @RequestMapping("delOrder")
    public JsonData delOrder(Integer id){
        orderService.delOrder(id);
        return  JsonData.getJsonSuccess("成功");
    }




}
