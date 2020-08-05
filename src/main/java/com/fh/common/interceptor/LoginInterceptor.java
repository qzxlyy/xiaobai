package com.fh.common.interceptor;

import com.fh.common.exception.NoLogException;
import com.fh.entity.po.Vip;
import com.fh.utlis.JWT;
import com.fh.utlis.RedisUse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            throw  new   NoLogException("没有登录");
        }
        //解密 字节数组
        byte[] decode = Base64.getDecoder().decode(token);
        //将解密处理的数组进行转换字符串
        String signToken = new String(decode);
        String[] split = signToken.split(",");
        //判断数据是否被篡改
        if(split.length!=2){
            throw  new  NoLogException("没有登录");
        }
        String  iphone =  split[0];
        //密钥
        String  sign = split[1];
        Vip user = JWT.unsign(sign, Vip.class);
        if(user==null){
            throw new  NoLogException("没有登录");
        }
        if (user!=null){//jwt通过验证
            String sign_redis = RedisUse.get("token_" + iphone);
            if(!sign.equals(sign_redis)){//判断是否是最新的密钥
                //不是就返回一个字符串
                throw  new  NoLogException("验证码过期  请重新登录");
            }
        }
        //签名的逻辑通过之后 将redis设置为有效时间30分钟
        RedisUse.set("token"+user.getVipPhone(),sign,60*30);
        //将用户信息放到redis中方便后续使用
        request.setAttribute("login_user",user);
        return true;
    }
}
