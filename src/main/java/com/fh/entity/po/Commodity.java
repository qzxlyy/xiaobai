package com.fh.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@TableName("shop_commodity")
public class Commodity {
    @TableId(value = "commodityId",type = IdType.AUTO)
    private  Integer commodityId;
    @TableField(value = "commodityName")
    private  String commodityName;
    @TableField(value = "imgPath")
    private  String imgPath;
    @TableField(value = "price")
    private  int price;
    @TableField(value = "isup")
    private  int isup;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField(value = "produDate")
    private Date produDate;
    @TableField(value = "isSell")
    private  int isSell;
    @TableField(value = "area")
    private  String area;
    @TableField(value = "types")
    private  String types;
    @TableField(value = "stock")
    private  Integer stock;


    public Integer getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Integer commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getIsup() {
        return isup;
    }

    public void setIsup(int isup) {
        this.isup = isup;
    }

    public Date getProduDate() {
        return produDate;
    }

    public void setProduDate(Date produDate) {
        this.produDate = produDate;
    }

    public int getIsSell() {
        return isSell;
    }

    public void setIsSell(int isSell) {
        this.isSell = isSell;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
