package edu.hebut.here.data;

public class Furniture {
    private int furnitureID;
    private String furnitureName;
    private int roomID;

    public Furniture(int furnitureID, String furnitureName, int roomID) {
        this.furnitureID = furnitureID;
        this.furnitureName = furnitureName;
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
                ", roomID=" + roomID +
                '}';
    }
}