package com.fh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.dao.CartDao;
import com.fh.dao.CommodityDao;
import com.fh.entity.po.Commodity;
import com.fh.entity.po.Vip;
import com.fh.entity.vo.CommodityCart;
import com.fh.service.CartService;
import com.fh.utlis.RedisUse;
import com.fh.utlis.RedisUtlis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Resource
    private   CartDao cartDao;

    @Resource
    private CommodityDao commodityDao;

    @Autowired
    private HttpServletRequest request;

    @Override
    public Integer addCommodityToCart(Integer commodityId, Integer count) {

        //将数据存入到redis中 hash key 用户的唯一标识 filed 商品id value 商品信息
        //判断库存够不够
        if(count>0){
            //根据id查出数据
            Commodity commodity = commodityDao.selectById(commodityId);
            if(count>commodity.getStock()){
                return commodity.getStock()-count;
            }
        }
        Vip login_user = (Vip) request.getAttribute("login_user");
        long vipPhone = login_user.getVipPhone();
        //获取购物车的指定的商品信息
        String commodityIdFo = RedisUse.hget("cart_" + vipPhone + "_qzx", commodityId + "");
        //判断用户是否存在此商品
        if(StringUtils.isEmpty(commodityIdFo)){
            //查询商品信息
            CommodityCart commodityCart = commodityDao.queryCommodityCartById(commodityId);
            commodityCart.setCount(count);
            commodityCart.setCheck(true);
            //将查询出来的库存存入定义的commodityCart中
            //计算小计
            BigDecimal money = commodityCart.getPrice().multiply(new BigDecimal(count));
            commodityCart.setMoney(money);
            //将商品转换为json字符串
            String commodityCartString = JSONObject.toJSONString(commodityCart);
            //将数据放入redis中
            RedisUse.hset("cart_" + vipPhone + "_qzx",commodityId+"",commodityCartString);
        }else {//购物车存在此商品 只需要修改数量小计
            //先将字符串转换为JavaBean
            CommodityCart commodityCart = JSONObject.parseObject(commodityIdFo, CommodityCart.class);
            commodityCart.setCount(commodityCart.getCount()+count);
            if(count==-1&&commodityCart.getCount()<0){
                return commodityCart.getCount();
            }

            //验证库存是否足够
            Commodity commodity = commodityDao.selectById(commodityId);
            if(commodityCart.getCount()>commodity.getStock()){
                return commodity.getStock()-commodityCart.getCount();
            }
            //修改小计
            BigDecimal money = commodityCart.getPrice().multiply(new BigDecimal(count));
            commodityCart.setMoney(money);
            //将商品转换为json字符串
            String s = JSONObject.toJSONString(commodityCart);
            //将数据放入到redis中
            RedisUse.hset("cart_" + vipPhone + "_qzx",commodityId+"",s);
        }
        long hlen = RedisUse.hlen("cart_" + vipPhone + "_qzx");
        return (int) hlen;
    }

    @Override
    public void checkedCommodityStatus(String checkedId) {
        Vip login_user = (Vip) request.getAttribute("login_user");
        long vipPhone = login_user.getVipPhone();
        //取出购物车的所有数据
        List<String> hvals = RedisUse.hvals("cart_" + vipPhone + "_qzx");
        for (int i = 0; i <hvals.size() ; i++) {
            String s = hvals.get(i);
            CommodityCart commodityCart = JSONObject.parseObject(s, CommodityCart.class);
            Integer commodityId = commodityCart.getCommodityId();
            //判断穿过了的id是否存在redis中
            if((","+checkedId).contains(","+commodityId+",")!=true){
                commodityCart.setCheck(false);
                RedisUse.hset("cart_" + vipPhone + "_qzx",commodityCart.getCommodityId()+"",JSONObject.toJSONString(commodityCart));
            }else {
                commodityCart.setCheck(true);
                RedisUse.hset("cart_" + vipPhone + "_qzx",commodityCart.getCommodityId()+"",JSONObject.toJSONString(commodityCart));
            }
        }
    }




    @Override
    public void deleteCommodityCart(Integer commodityId) {
        Vip login_user = (Vip) request.getAttribute("login_user");
        long vipPhone = login_user.getVipPhone();
        Jedis jedis = RedisUtlis.getJedis();
        jedis.hdel("cart_" + vipPhone + "_qzx",commodityId+"");
    }
    @Override
    public void deleteCommodityCartAll(List<Integer> commodityId) {
        Vip login_user = (Vip) request.getAttribute("login_user");
        long vipPhone = login_user.getVipPhone();
        Jedis jedis = RedisUtlis.getJedis();
        for (int i = 0; i <commodityId.size() ; i++) {
            jedis.hdel("cart_" + vipPhone + "_qzx",commodityId.get(i)+"");
        }
    }

    @Override
    public List<CommodityCart> queryCheckCommodity() {

        Vip login_user = (Vip) request.getAttribute("login_user");
        long vipPhone = login_user.getVipPhone();
        //获取购物车的数据
        List<String> hvals = RedisUse.hvals("cart_" + vipPhone + "_qzx");
        //将我们实际需要的数据放入list中
        List list = new ArrayList();
        for (int i = 0; i <hvals.size() ; i++) {
            String s = hvals.get(i);
            //将字符串转换为JavaBean
            CommodityCart commodityCart = JSONObject.parseObject(s, CommodityCart.class);
            if(commodityCart.isCheck()==true){
                list.add(commodityCart);
            }
        }


        return list;
    }


}
