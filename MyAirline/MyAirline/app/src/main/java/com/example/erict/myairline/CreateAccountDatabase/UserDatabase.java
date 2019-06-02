package com.example.erict.myairline.CreateAccountDatabase;

import android.service.autofill.UserData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UserDatabase {

    private UUID mUserId;
    private String mUsername;
    private String mPassword;
    private Date mDate;

    public UserDatabase(){
        this(UUID.randomUUID());
    }

    public UserDatabase(UUID id){
        mUserId = id;
        mDate = new Date();
    }

    public UUID getUserId() {
        return mUserId;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public Date getDate(){
        return mDate;
    }

    public void setDate(Date newDate){
        mDate = newDate;
    }

    public String toString(){
        return "UUID: " + mUserId.toString() +
               "\nDate: " + mDate +
               "\nName: " + mUsername +
               "\nPassword: " + mPassword;


    }
}
