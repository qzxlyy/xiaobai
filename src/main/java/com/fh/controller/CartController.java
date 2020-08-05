package com.fh.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.JsonData;
import com.fh.entity.po.Vip;
import com.fh.entity.vo.CommodityCart;
import com.fh.service.CartService;
import com.fh.utlis.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping("addCart")
    public JsonData addCart(Integer commodityId,Integer count){
       Integer count_type =  cartService.addCommodityToCart(commodityId,count);
       return  JsonData.getJsonSuccess(count_type);
    }


    @RequestMapping("queryCartCommodity")
    public  JsonData queryCartCommodity(){
        Vip login_user = (Vip) request.getAttribute("login_user");
        long vipPhone = login_user.getVipPhone();
        List<String> hvals = RedisUse.hvals("cart_" + vipPhone + "_qzx");
        List<CommodityCart> list = new ArrayList<>();
        for (int i = 0; i <hvals.size() ; i++) {
            CommodityCart commodityCart = JSONObject.parseObject(hvals.get(i), CommodityCart.class);
            list.add(commodityCart);
        }
        return JsonData.getJsonSuccess(list);
    }


    @RequestMapping("checkedCommodityStatus")
    public  JsonData checkedCommodityStatus(String checkedId){
        cartService.checkedCommodityStatus(checkedId);
        return  JsonData.getJsonSuccess("成功");
    }
    @RequestMapping("deleteCommodityCart")
    public  JsonData deleteCommodityCart(Integer commodityId){
        cartService.deleteCommodityCart(commodityId);
        return  JsonData.getJsonSuccess("删除成功");
    }
    @RequestMapping("deleteCommodityCartAll")
    public JsonData deleteCommodityCartAll(@RequestParam("arr[]") List<Integer> commodityId){
        cartService.deleteCommodityCartAll(commodityId);
        return  JsonData.getJsonSuccess("删除成功");
    }
    @RequestMapping("queryCheckCommodity")
    public  JsonData queryCheckCommodity(){
      List<CommodityCart> queryCommodityCartCheck =   cartService.queryCheckCommodity();
      return  JsonData.getJsonSuccess(queryCommodityCartCheck);
    }


}
