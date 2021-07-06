package com.hargrove.beans;

import jakarta.ws.rs.QueryParam;

public class CheckingFilterBean {
    private @QueryParam("userID") String userID;
    private @QueryParam("balance") String balance;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
