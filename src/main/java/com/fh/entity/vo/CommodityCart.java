package com.fh.entity.vo;

import java.math.BigDecimal;

public class CommodityCart {

    private  Integer commodityId;
    private  String  commodityName;
    private BigDecimal price;
    private boolean isCheck;
    private String  imgPath;
    private Integer count;
    private BigDecimal money;
    private Integer sotock;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getSotock() {
        return sotock;
    }

    public void setSotock(Integer sotock) {
        this.sotock = sotock;
    }
}
