package com.zeoharlem.gads.pepperedrice.models;

public class UserProfile {
    private String uid, name, phoneNumber, profileImage, userType, token;

    public UserProfile(String uid, String name, String phoneNumber, String profileImage) {
        this.uid            = uid;
        this.name           = name;
        this.phoneNumber    = phoneNumber;
        this.profileImage   = profileImage;
    }

    public UserProfile(String uid, String name, String phoneNumber, String profileImage, String userType) {
        this(uid, name, phoneNumber, profileImage);
        this.userType       = userType;
    }

    public UserProfile(String uid, String name, String phoneNumber, String profileImage, String userType, String token) {
        this(uid, name, phoneNumber, profileImage, userType);
        this.token  = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
