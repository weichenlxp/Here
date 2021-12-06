package edu.hebut.here.entity;

public class LoginUser {
    String username;
    String password;
    int userID;
    int houseID;
    String houseName;

    public LoginUser() {

    }

    public LoginUser(String username, String password, int userID, int houseID, String houseName) {
        this.username = username;
        this.password = password;
        this.userID = userID;
        this.houseID = houseID;
        this.houseName = houseName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userID=" + userID +
                ", houseID=" + houseID +
                ", houseName='" + houseName + '\'' +
                '}';
    }
}
