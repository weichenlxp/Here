package edu.hebut.here.data;

import java.util.Arrays;
import java.util.Date;

public class User {
    private int userID;
    private String username;            //用户名
    private String password;        //密码
    private String gender;
    private byte[] profilePhoto;
    private Date signInTime;

    public User(int userID, String username, String password, String gender, byte[] profilePhoto, Date signInTime) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.profilePhoto = profilePhoto;
        this.signInTime = signInTime;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Date getSignInTime() {
        return signInTime;
    }

    public void setSignInTime(Date signInTime) {
        this.signInTime = signInTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", gender=" + gender +
                ", profilePhoto=" + Arrays.toString(profilePhoto) +
                ", signInTime=" + signInTime +
                '}';
    }
}