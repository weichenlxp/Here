package edu.hebut.here.entity;

public class Account {
    int accountID;
    String accountName;
    String accountValue;
    int houseID;

    public Account(int accountID, String accountName, String accountValue, int houseID) {
        this.accountID = accountID;
        this.accountName = accountName;
        this.accountValue = accountValue;
        this.houseID = houseID;
    }

    public Account() {

    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountValue() {
        return accountValue;
    }

    public void setAccountValue(String accountValue) {
        this.accountValue = accountValue;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountID=" + accountID +
                ", accountName='" + accountName + '\'' +
                ", accountValue='" + accountValue + '\'' +
                ", houseID=" + houseID +
                '}';
    }
}
