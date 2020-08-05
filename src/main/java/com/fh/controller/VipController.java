package com.fh.controller;

import com.fh.common.JsonData;
import com.fh.entity.po.Vip;
import com.fh.service.VipService;
import com.fh.utlis.JWT;
import com.fh.utlis.OSSUtils;
import com.fh.utlis.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("vip")
public class VipController {
    @Autowired
    private VipService vipService;

    @RequestMapping("addVip")
    public JsonData addVip(Vip vip){
        vip.setCreateDate( new Date());
        vipService.addVip(vip);
        return JsonData.getJsonSuccess("注册会员成功");
    }
    @RequestMapping("uploadFIle")
    public Map uploadFIle(@RequestParam("img") MultipartFile img, HttpServletRequest request) throws Exception {
        InputStream ins = img.getInputStream();
        File toFile = new File(img.getOriginalFilename());
        inputStreamToFile(ins,toFile);
        String s = OSSUtils.uploadFile(toFile);
        Map map=new HashMap();
        map.put("imgPath",s);
        return  map;
    }
    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("queryByPhone")
    public Map queryByPhone(long vipPhone){
        Vip vip =    vipService.queryByPhone(vipPhone);
        Map map =new HashMap();
        if(vip==null){
            map.put("valid",true);
        }else {
            map.put("valid",false);
        }
        return map;
    }
    @RequestMapping("queryByName")
    public Map queryByName(String vipName){
        Vip vip =    vipService.queryByName(vipName);
        Map map =new HashMap();
        if(vip==null){
            map.put("valid",true);
        }else {
            map.put("valid",false);
        }
        return map;
    }
    @RequestMapping("sendMessage")
    public JsonData sendMessage(String iphone){
        //发送短信   阿里提供的短信服务
        String code="1111";
        //存redis
        RedisUse.set(iphone+"_qzx",code , 60*5);
        return JsonData.getJsonSuccess("短信发送成功");
    }

    @RequestMapping("login")
    public  JsonData login(Long iphone ,String code,HttpServletRequest request) {
        Map logMap = new HashMap();
        String redisCode = RedisUse.get(iphone + "_qzx");
        if (redisCode != null && redisCode.equals(code)) {
            Vip vip = new Vip();
            vip.setVipPhone(iphone);
            //使用jwt进行加密生成密钥
            String sign = JWT.sign(vip, 1000 * 60 * 60 * 24);
            //使用base64生成签名
            String token = Base64.getEncoder().encodeToString((iphone + "," + sign).getBytes());
            //将最新的密钥存入到redis中
            RedisUse.set("token_" + iphone, sign, 60 * 30);
            logMap.put("status", "200");
            logMap.put("message", "登录成功");
            logMap.put("token", token);
        } else {
            logMap.put("status", "300");
            logMap.put("message", "用户验证码错误 或 用户不存在");
        }

        return JsonData.getJsonSuccess(logMap);

    }



}
