package edu.hebut.here.data;

public class Room {
    private int roomID;
    private String roomName;
    private int houseID;

    public Room(int roomID, String roomName, int houseID) {
        this.roomID = roomID;
        this.roomName = roomName;
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
                ", houseID=" + houseID +
                '}';
    }
}