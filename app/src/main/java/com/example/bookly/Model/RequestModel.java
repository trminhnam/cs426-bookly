package com.example.bookly.Model;

public class RequestModel {
    int profile;
    String categories, time;


    public RequestModel(int profile, String categories, String time) {
        this.profile = profile;
        this.categories = categories;
        this.time = time;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
