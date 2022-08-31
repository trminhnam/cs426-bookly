//    int profile, postImage, save;
package com.example.bookly.Model;

import java.util.Objects;

public class Post {
    private String postID;
    private String postImage;
    private String postedBy;
    private String postContent;
    private long postedAt;
    private int postLike;
    private int commentCount;

    private double cur_lat = 0.0, cur_lng = 0.0;
    private String address="unknown", city="unknown", state="unknown", country="unknown";

    public Post(String postID, String postImage, String postedBy, String postContent, long postedAt) {
        this.postID = postID;
        this.postImage = postImage;
        this.postedBy = postedBy;
        this.postContent = postContent;
        this.postedAt = postedAt;
    }

    public Post() {

    }

    public boolean isEqual(Post new_post){
        return  Objects.equals(this.postLike, new_post.postLike)
                && Objects.equals(this.postID, new_post.postID)
                && Objects.equals(this.postImage, new_post.postImage)
                && Objects.equals(this.postContent, new_post.postContent);
    }

    public void setLocation(double lat, double lng) {
        this.cur_lat = lat;
        this.cur_lng = lng;
    }

    public double getLat() {
        return cur_lat;
    }

    public void setLat(double lat) {
        this.cur_lat = lat;
    }

    public double getLng() {
        return cur_lng;
    }

    public void setLng(double lng) {
        this.cur_lng = lng;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }

    public int getPostLike() {
        return postLike;
    }

    public void setPostLike(int postLike) {
        this.postLike = postLike;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    //    String name, about, like, comment, share;
//
//    public DashboardModel(int profile, int postImage, int save, String name, String about, String like, String comment, String share) {
//        this.profile = profile;
//        this.postImage = postImage;
//        this.save = save;
//        this.name = name;
//        this.about = about;
//        this.like = like;
//        this.comment = comment;
//        this.share = share;
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
//    public int getPostImage() {
//        return postImage;
//    }
//
//    public void setPostImage(int postImage) {
//        this.postImage = postImage;
//    }
//
//    public int getSave() {
//        return save;
//    }
//
//    public void setSave(int save) {
//        this.save = save;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getAbout() {
//        return about;
//    }
//
//    public void setAbout(String about) {
//        this.about = about;
//    }
//
//    public String getLike() {
//        return like;
//    }
//
//    public void setLike(String like) {
//        this.like = like;
//    }
//
//    public String getComment() {
//        return comment;
//    }
//
//    public void setComment(String comment) {
//        this.comment = comment;
//    }
//
//    public String getShare() {
//        return share;
//    }
//
//    public void setShare(String share) {
//        this.share = share;
//    }
}
