package com.example.bookly.Model;

public class Notification {

    private String notificationBy;
    private long notificationAt;
    private String type;
    private String postID;
    private String postedBy;
    private boolean isOpened;

    private String notificationID;

    public Notification() {
    }

    public Notification(String notificationBy, long notificationAt, String type, String postID, String postedBy, boolean isOpened) {
        this.notificationBy = notificationBy;
        this.notificationAt = notificationAt;
        this.type = type;
        this.postID = postID;
        this.postedBy = postedBy;
        this.isOpened = isOpened;
    }

    public String getNotificationBy() {
        return notificationBy;
    }

    public void setNotificationBy(String notificationBy) {
        this.notificationBy = notificationBy;
    }

    public long getNotificationAt() {
        return notificationAt;
    }

    public void setNotificationAt(long notificationAt) {
        this.notificationAt = notificationAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    //    int profile;
//    String notification, time;
//
//
//    public Notification(int profile, String notification, String time) {
//        this.profile = profile;
//        this.notification = notification;
//        this.time = time;
//    }
//
//    public int getProfile() {
//        return profile;
//    }
//
//    public void setProfile(int profile) {
//        this.profile = profile;
//    }
//
//    public String getNotification() {
//        return notification;
//    }
//
//    public void setNotification(String notification) {
//        this.notification = notification;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
}
