package com.example.wechat.model;

public class FriendsModel {

    String date;
    FriendsModel(){

    }

    public FriendsModel(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
