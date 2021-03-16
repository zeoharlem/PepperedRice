package com.zeoharlem.gads.pepperedrice.models;

import android.util.Patterns;

public class LoginUser {
    private String username;
    private String password;
    private String userId;
    private boolean isLoggedIn;

    public LoginUser(){

    }

    public LoginUser(String username){
        this.username   = username;
    }

    public LoginUser(String username, String password) {
        this.username   = username;
        this.password   = password;
    }

    public LoginUser(String username, String userId, boolean isLoggedIn) {
        this.username = username;
        this.userId = userId;
        this.isLoggedIn = isLoggedIn;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public boolean isEmailValid(){
        return Patterns.EMAIL_ADDRESS.matcher(getUsername()).matches();
    }
}
