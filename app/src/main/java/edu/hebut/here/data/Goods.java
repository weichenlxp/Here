package edu.hebut.here.data;

import java.util.Date;

public class Goods {
    private int goodsID;
    private String goodsName;
    private int furnitureID;
    private int categoryID;
    private int containerID;
    private int goodsNum;
    private byte[] goodsPhoto1;
    private byte[] goodsPhoto2;
    private byte[] goodsPhoto3;
    private Date buyTime;
    private Date manufactureDate;
    private int qualityGuaranteePeriod;
    private Date overtime;
    private boolean packed;

    public Goods(int goodsID, String goodsName, int furnitureID, int categoryID, int containerID, int goodsNum, byte[] goodsPhoto1, byte[] goodsPhoto2, byte[] goodsPhoto3, Date buyTime, Date manufactureDate, int qualityGuaranteePeriod, Date overtime, boolean packed) {
        this.goodsID = goodsID;
        this.goodsName = goodsName;
        this.furnitureID = furnitureID;
        this.categoryID = categoryID;
        this.containerID = containerID;
        this.goodsNum = goodsNum;
        this.goodsPhoto1 = goodsPhoto1;
        this.goodsPhoto2 = goodsPhoto2;
        this.goodsPhoto3 = goodsPhoto3;
        this.buyTime = buyTime;
        this.manufactureDate = manufactureDate;
        this.qualityGuaranteePeriod = qualityGuaranteePeriod;
        this.overtime = overtime;
        this.packed = packed;
    }

    public int getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(int goodsID) {
        this.goodsID = goodsID;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getFurnitureID() {
        return furnitureID;
    }

    public void setFurnitureID(int furnitureID) {
        this.furnitureID = furnitureID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getContainerID() {
        return containerID;
    }

    public void setContainerID(int containerID) {
        this.containerID = containerID;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public byte[] getGoodsPhoto1() {
        return goodsPhoto1;
    }

    public void setGoodsPhoto1(byte[] goodsPhoto1) {
        this.goodsPhoto1 = goodsPhoto1;
    }

    public byte[] getGoodsPhoto2() {
        return goodsPhoto2;
    }

    public void setGoodsPhoto2(byte[] goodsPhoto2) {
        this.goodsPhoto2 = goodsPhoto2;
    }

    public byte[] getGoodsPhoto3() {
        return goodsPhoto3;
    }

    public void setGoodsPhoto3(byte[] goodsPhoto3) {
        this.goodsPhoto3 = goodsPhoto3;
    }

    public Date getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Date buyTime) {
        this.buyTime = buyTime;
    }

    public Date getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public int getQualityGuaranteePeriod() {
        return qualityGuaranteePeriod;
    }

    public void setQualityGuaranteePeriod(int qualityGuaranteePeriod) {
        this.qualityGuaranteePeriod = qualityGuaranteePeriod;
    }

    public Date getOvertime() {
        return overtime;
    }

    public void setOvertime(Date overtime) {
        this.overtime = overtime;
    }

    public boolean isPacked() {
        return packed;
    }

    public void setPacked(boolean packed) {
        this.packed = packed;
    }


}