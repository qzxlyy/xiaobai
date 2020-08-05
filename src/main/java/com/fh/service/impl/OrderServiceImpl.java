package com.fh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.enums.PayStatusEnum;
import com.fh.common.exception.CountException;
import com.fh.dao.CommodityDao;
import com.fh.dao.OrderDao;
import com.fh.dao.OrderDetailsDao;
import com.fh.entity.po.Commodity;
import com.fh.entity.po.Order;
import com.fh.entity.po.OrderDetails;
import com.fh.entity.po.Vip;
import com.fh.entity.vo.CommodityCart;
import com.fh.service.OrderService;
import com.fh.utlis.RedisUse;
import com.github.wxpay.sdk.FeiConfig;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService  {
    @Resource
    private OrderDao orderDao;
    @Autowired
    private HttpServletRequest request;

    @Resource
    private CommodityDao commodityDao;
    @Resource
    private OrderDetailsDao orderDetailsDao;

    @Override
    public Map addOrder(Integer addressId, Integer payentId) throws CountException {
        //将需要返回的数据放入map
        Map map = new HashMap();
        List<OrderDetails> list = new ArrayList<>();
        Order order =new Order();
        order.setAddressId(addressId);
        order.setCreateDate(new Date());
        order.setPayType(payentId);
        order.setPayStatus(PayStatusEnum.PAY_STATUS_INIT.getStatus());
        //设置商品清单个数 商品清单怎么获取 库存足够才可以拿
        Integer typeCount = 0;
        //设置总金额
        BigDecimal totalMoney = new BigDecimal(0);
        //获取购物车的key 登录信息
        Vip login_user = (Vip) request.getAttribute("login_user");
        long vipPhone = login_user.getVipPhone();
        //将vip放到order中
        //因为字段类型问题 需要将long类型的vipPhone转换为string类型才能放入
        order.setVipId(String.valueOf(vipPhone));
        //获取购物车的全部信息并且是被选中的商品
        List<String> commodityStr = RedisUse.hvals("cart_" + vipPhone + "_qzx");
        for (int i = 0; i <commodityStr.size() ; i++) {
            //将字符串转换为javabean
            CommodityCart commodityCart = JSONObject.parseObject(commodityStr.get(i), CommodityCart.class);
            //判断是否是选中的商品
            if(commodityCart.isCheck()==true){
                //查询数据库判断库存是否足够
                Commodity commodity = commodityDao.selectById(commodityCart.getCommodityId());
                if(commodity.getStock()>=commodityCart.getCount()){//库存足够 库存的数量大于购物车的数量
                    //第一  order的商品个数加1  钱数计算
                    typeCount++;
                    //计算的钱数
                    totalMoney=totalMoney.add(commodityCart.getMoney());
                    //维护订单详情表
                    OrderDetails orderDetails = new OrderDetails();
                    orderDetails.setCount(commodityCart.getCount());
                    orderDetails.setCommodityId(commodityCart.getCommodityId());
                    list.add(orderDetails);//现在还没有订单的id 因为订单的id是交给AUTO自动生成的
                    //第二生成订单减少库存
                    //减去数据库的库存 使用数据库的锁 来保证不会超卖   update能返回一个值
                    int update1  =  commodityDao.updateCommodityStock(commodityCart.getCount(),commodity.getCommodityId());
                    if(update1==0){//如果等于0就是超卖了
                        throw  new CountException("商品名为："+commodityCart.getCommodityName()+"的库存不足 当前库存为："+commodity.getStock());
                    }
                }else {
                    //再库存不足的情况
                    throw  new CountException("商品名为："+commodityCart.getCommodityName()+"的库存不足 当前库存为："+commodity.getStock());
                }
            }
        }
        //商品的类型数量
        order.setCommodityCount(typeCount);
        //价格
        order.setTotalMoney(totalMoney);
        orderDao.insert(order);
        //保存订单详情表
        orderDetailsDao.batchAddOrderDetails(list,order.getId());
        //删除redis的数据 购物车的商品已经结算 需要移除
        for (int i = 0; i <commodityStr.size() ; i++) {
            //将字符串转换为javabean
            CommodityCart commodityCart = JSONObject.parseObject(commodityStr.get(i), CommodityCart.class);
            if(commodityCart.isCheck()==true){
                RedisUse.hdel("cart_" + vipPhone + "_qzx",commodityCart.getCommodityId()+"");

            }
        }
        map.put("orderId",order.getId());
        map.put("totalMoney",totalMoney);
        return map;
    }

    @Override
    public Integer queryCode2(Integer id) throws Exception {
        FeiConfig config = new FeiConfig();
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no","weixin1_order_0909"+id);
        // 查询支付状态
        Map<String, String> resp = wxpay.orderQuery(data);
        System.out.println("查询结果："+JSONObject.toJSONString(resp));
        if("SUCCESS".equalsIgnoreCase(resp.get("return_code"))&&"SUCCESS".equalsIgnoreCase(resp.get("result_code"))){
            if("SUCCESS".equalsIgnoreCase(resp.get("trade_state"))){//支付成功
                //更新订单状态
                Order order=new Order();
                order.setId(id);
                order.setPayStatus(PayStatusEnum.PAY_STATUS_SUCCESS.getStatus());
                orderDao.updateByIds(order);
                return 1;
            }else if("NOTPAY".equalsIgnoreCase(resp.get("trade_state"))){
                return 3;
            }else if("USERPAYING".equalsIgnoreCase(resp.get("trade_state"))){
                return 2;
            }
        }
        return 0;
    }

    @Override
    public Map createCode2(Integer id) throws Exception {
        Order order = orderDao.selectById(id);
        Map map = new HashMap();
        // 微信支付  natvie   商户生成二维码
        //配置配置信息
        FeiConfig config = new FeiConfig();
        //得到微信支付对象
        WXPay wxpay = new WXPay(config);
        //设置请求参数
        Map<String, String> data = new HashMap<String, String>();
        //对订单信息描述
        data.put("body", "飞狐电商666-订单支付");
        //String payId = System.currentTimeMillis()+"";
        //设置订单号 （保证唯一 ）
        data.put("out_trade_no","weixin1_order_0909"+id);
        //设置币种
        data.put("fee_type", "CNY");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        Date d=new Date();
        String dateStr = sdf.format(new Date(d.getTime() + 120000000));
        //设置二维码的失效时间
        data.put("time_expire", dateStr);
        //设置订单金额   单位分

        //正确写法data.put("total_fee",order.getTotalMoney()+"");
        data.put("total_fee","1");
        data.put("notify_url", "http://www.example.com/wxpay/notify");
        //设置支付方式
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
        // 统一下单
        Map<String, String> resp = wxpay.unifiedOrder(data);
        //这一块必须用log4j 做记录的
        System.out.println(id+"下订单结果为:"+ JSONObject.toJSONString(resp));
        if("SUCCESS".equalsIgnoreCase(resp.get("return_code"))&&"SUCCESS".equalsIgnoreCase(resp.get("result_code"))){
            map.put("code",200);
            map.put("url",resp.get("code_url"));
            //更新订单状态
            Order ss = new Order();
            ss.setPayStatus(PayStatusEnum.PAY_STATUS_ING.getStatus());
            ss.setId(order.getId());
            orderDao.updateById(ss);
        }else {
            map.put("code",600);
            map.put("info",resp.get("return_msg"));
        }
        return resp;
    }

    @Override
    public void updatePayStatus(Integer id) {
        orderDao.updatePayStatus(id);
    }

    @Override
    public List<Order> orderService() {
        Vip login_user = (Vip) request.getAttribute("login_user");
        long vipPhone = login_user.getVipPhone();
        List orderList = new ArrayList();
        List<Order> list = orderDao.selectList(null);
        for (int i = 0; i <list.size() ; i++) {
            Order order = list.get(i);
            String vipId = order.getVipId();
            String s = String.valueOf(vipPhone);
            if(vipId.equals(s)){
                orderList.add(list.get(i));
            }
        }

        return orderList;
    }

    @Override
    public void delOrder(Integer id) {
        orderDao.deleteById(id);
    }
}
