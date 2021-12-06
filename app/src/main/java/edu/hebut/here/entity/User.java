package edu.hebut.here.entity;

public class User {
    int userID;
    String username;
    String password;
    String gender;
    String profilePhoto;
    int reminderTime;

    public User() {

    }

    public User(int userID, String username, String password, String gender, String profilePhoto, int reminderTime) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.profilePhoto = profilePhoto;
        this.reminderTime = reminderTime;
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

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public int getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(int reminderTime) {
        this.reminderTime = reminderTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", reminderTime=" + reminderTime +
                '}';
    }
}
