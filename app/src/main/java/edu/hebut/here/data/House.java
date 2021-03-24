package edu.hebut.here.data;

public class House {
    private int houseID;
    private String houseName;
    private int userID;

    public House(int houseID, String houseName, int userID) {
        this.houseID = houseID;
        this.houseName = houseName;
        this.userID = userID;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "House{" +
                "houseID=" + houseID +
                ", houseName='" + houseName + '\'' +
                ", userID=" + userID +
                '}';
    }
}