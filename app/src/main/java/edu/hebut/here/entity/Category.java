package edu.hebut.here.entity;

public class Category {
    int categoryID;
    String categoryName;
    int userID;

    public Category(int categoryID, String categoryName, int userID) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.userID = userID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryID=" + categoryID +
                ", categoryName='" + categoryName + '\'' +
                ", userID=" + userID +
                '}';
    }
}
