package edu.hebut.here.entity;

public class Goods {
    int goodsID;
    String goodsName;
    int userID;
    int houseID;
    int roomID;
    int furnitureID;
    int categoryID;
    int containerID;
    int goodsNum;
    String goodsPhoto1;
    String goodsPhoto2;
    String goodsPhoto3;
    String buyTime;
    String manufactureDate;
    int qualityGuaranteePeriod;
    String qualityGuaranteePeriodType;
    String overtime;
    boolean isOvertime;
    boolean isCloseOvertime;
    boolean packed;
    String remark;

    public Goods(int goodsID, String goodsName, int userID, int houseID, int roomID, int furnitureID, int categoryID, int containerID, int goodsNum, String goodsPhoto1, String goodsPhoto2, String goodsPhoto3, String buyTime, String manufactureDate, int qualityGuaranteePeriod, String qualityGuaranteePeriodType, String overtime, boolean isOvertime, boolean isCloseOvertime, boolean packed, String remark) {
        this.goodsID = goodsID;
        this.goodsName = goodsName;
        this.userID = userID;
        this.houseID = houseID;
        this.roomID = roomID;
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
        this.qualityGuaranteePeriodType = qualityGuaranteePeriodType;
        this.overtime = overtime;
        this.isOvertime = isOvertime;
        this.isCloseOvertime = isCloseOvertime;
        this.packed = packed;
        this.remark = remark;
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
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

    public String getGoodsPhoto1() {
        return goodsPhoto1;
    }

    public void setGoodsPhoto1(String goodsPhoto1) {
        this.goodsPhoto1 = goodsPhoto1;
    }

    public String getGoodsPhoto2() {
        return goodsPhoto2;
    }

    public void setGoodsPhoto2(String goodsPhoto2) {
        this.goodsPhoto2 = goodsPhoto2;
    }

    public String getGoodsPhoto3() {
        return goodsPhoto3;
    }

    public void setGoodsPhoto3(String goodsPhoto3) {
        this.goodsPhoto3 = goodsPhoto3;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public int getQualityGuaranteePeriod() {
        return qualityGuaranteePeriod;
    }

    public void setQualityGuaranteePeriod(int qualityGuaranteePeriod) {
        this.qualityGuaranteePeriod = qualityGuaranteePeriod;
    }

    public String getQualityGuaranteePeriodType() {
        return qualityGuaranteePeriodType;
    }

    public void setQualityGuaranteePeriodType(String qualityGuaranteePeriodType) {
        this.qualityGuaranteePeriodType = qualityGuaranteePeriodType;
    }

    public String getOvertime() {
        return overtime;
    }

    public void setOvertime(String overtime) {
        this.overtime = overtime;
    }

    public boolean isOvertime() {
        return isOvertime;
    }

    public void setOvertime(boolean overtime) {
        isOvertime = overtime;
    }

    public boolean isCloseOvertime() {
        return isCloseOvertime;
    }

    public void setCloseOvertime(boolean closeOvertime) {
        isCloseOvertime = closeOvertime;
    }

    public boolean getPacked() {
        return packed;
    }

    public void setPacked(boolean packed) {
        this.packed = packed;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "goodsID=" + goodsID +
                ", goodsName='" + goodsName + '\'' +
                ", userID=" + userID +
                ", houseID=" + houseID +
                ", roomID=" + roomID +
                ", furnitureID=" + furnitureID +
                ", categoryID=" + categoryID +
                ", containerID=" + containerID +
                ", goodsNum=" + goodsNum +
                ", goodsPhoto1='" + goodsPhoto1 + '\'' +
                ", goodsPhoto2='" + goodsPhoto2 + '\'' +
                ", goodsPhoto3='" + goodsPhoto3 + '\'' +
                ", buyTime='" + buyTime + '\'' +
                ", manufactureDate='" + manufactureDate + '\'' +
                ", qualityGuaranteePeriod=" + qualityGuaranteePeriod +
                ", qualityGuaranteePeriodType='" + qualityGuaranteePeriodType + '\'' +
                ", overtime='" + overtime + '\'' +
                ", isOvertime=" + isOvertime +
                ", isCloseOvertime=" + isCloseOvertime +
                ", packed=" + packed +
                ", remark='" + remark + '\'' +
                '}';
    }
}

