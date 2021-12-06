package edu.hebut.here.entity;

public class Room {
    int roomID;
    String roomName;
    int userID;
    int houseID;

    public Room(int roomID, String roomName, int userID, int houseID) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.userID = userID;
        this.houseID = houseID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
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

    @Override
    public String toString() {
        return "Room{" +
                "roomID=" + roomID +
                ", roomName='" + roomName + '\'' +
                ", userID=" + userID +
                ", houseID=" + houseID +
                '}';
    }
}
