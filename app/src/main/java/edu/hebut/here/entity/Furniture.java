package edu.hebut.here.entity;

public class Furniture {
    int furnitureID;
    String furnitureName;
    int userID;
    int houseID;
    int roomID;

    public Furniture(int furnitureID, String furnitureName, int userID, int houseID, int roomID) {
        this.furnitureID = furnitureID;
        this.furnitureName = furnitureName;
        this.userID = userID;
        this.houseID = houseID;
        this.roomID = roomID;
    }

    public int getFurnitureID() {
        return furnitureID;
    }

    public void setFurnitureID(int furnitureID) {
        this.furnitureID = furnitureID;
    }

    public String getFurnitureName() {
        return furnitureName;
    }

    public void setFurnitureName(String furnitureName) {
        this.furnitureName = furnitureName;
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

    @Override
    public String toString() {
        return "Furniture{" +
                "furnitureID=" + furnitureID +
                ", furnitureName='" + furnitureName + '\'' +
                ", userID=" + userID +
                ", houseID=" + houseID +
                ", roomID=" + roomID +
                '}';
    }
}
