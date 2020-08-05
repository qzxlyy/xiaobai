package com.fh.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@TableName("shop_address")
public class Address {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer   id;
    @TableField("vipId")
    private long   vipId;
    @TableField("name")
    private String    name;
    @TableField("detailAdd")
    private String    detailAdd;
    @TableField("areaIds")
    private String    areaIds;
    @TableField("iphone")
    private String    iphone;
    @TableField("isCheck")
    private Integer   isCheck;
    @TableField("createDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    @TableField("areaIdsDetail")
    private String  areaIdsDetail;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getVipId() {
        return vipId;
    }

    public void setVipId(long vipId) {
        this.vipId = vipId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetailAdd() {
        return detailAdd;
    }

    public void setDetailAdd(String detailAdd) {
        this.detailAdd = detailAdd;
    }

    public String getAreaIds() {
        return areaIds;
    }

    public void setAreaIds(String areaIds) {
        this.areaIds = areaIds;
    }

    public String getIphone() {
        return iphone;
    }

    public void setIphone(String iphone) {
        this.iphone = iphone;
    }

    public Integer getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Integer isCheck) {
        this.isCheck = isCheck;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAreaIdsDetail() {
        return areaIdsDetail;
    }

    public void setAreaIdsDetail(String areaIdsDetail) {
        this.areaIdsDetail = areaIdsDetail;
    }
}
