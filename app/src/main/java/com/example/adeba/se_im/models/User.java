package com.example.adeba.se_im.models;

/**
 *
 */
public class User {
    public String uid;
    public String email;
    public String displayName;
    public String displayPicture;
    public String firebaseToken;

    public User(){

    }

    public User(String uid, String email, String displayName, String displayPicture, String firebaseToken){
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.displayPicture = displayPicture;
        this.firebaseToken = firebaseToken;
    }
}
