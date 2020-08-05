package com.fh.common.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class KuaYuInterceptor extends HandlerInterceptorAdapter {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求的域名
        String header = request.getHeader("Origin");
        //允许跨域访问的域名：若有端口需写全（协议+域名+端口），若没有端口末尾不用加然后就可以解决跨域问题
        response.setHeader("Access-Control-Allow-Origin",header);

        //允许session //是否支持cookie跨域
        response.setHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers","x-requested-with");
        response.setHeader("Access-Control-Allow-Credentials","true");

        //当客户端修改了头信息 发送了两个请求 第一个是预请求 options (是否允许修改头信息) 然后就是发起真正的请求
        String method = request.getMethod();
        if(method.equalsIgnoreCase("options")){
            //允许修改头信息添加一个name属性
            response.setHeader("Access-Control-Allow-Headers","token");

            return false;
        }
        //从handel中获取数据
        request.getHeader("login_token");

        return true;
    }
}
