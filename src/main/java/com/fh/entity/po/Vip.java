package com.fh.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@TableName(value = "shop_vip")
public class Vip {
    @TableId(value = "vipId",type = IdType.AUTO)
    private Integer vipId;
    @TableField(value = "vipName")
    private String  vipName;
    @TableField(value = "vipPhone")
    private long vipPhone;
    @TableField(value = "imgPath")
    private String  imgPath;
    @TableField(value = "area")
    private String  area;
    @TableField(value = "createDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;


    public Integer getVipId() {
        return vipId;
    }

    public void setVipId(Integer vipId) {
        this.vipId = vipId;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public long getVipPhone() {
        return vipPhone;
    }

    public void setVipPhone(long vipPhone) {
        this.vipPhone = vipPhone;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
