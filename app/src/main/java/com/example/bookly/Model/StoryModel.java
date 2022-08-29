package com.example.bookly.Model;

import java.util.ArrayList;

public class StoryModel {
    int story, storyType, profile;
    String name;

    private String postedBy;
    private long storyAt;

    ArrayList<UserStory> stories;


    public StoryModel() {

    }

    public StoryModel(int story, int storyType, int profile, String name) {
        this.story = story;
        this.storyType = storyType;
        this.profile = profile;
        this.name = name;
    }

    public String getStoryBy() {
        return postedBy;
    }

    public void setStoryBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public int getStory() {
        return story;
    }

    public void setStory(int story) {
        this.story = story;
    }

    public int getStoryType() {
        return storyType;
    }

    public void setStoryType(int storyType) {
        this.storyType = storyType;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(long storyAt) {
        this.storyAt = storyAt;
    }

    public ArrayList<UserStory> getStories() {
        return stories;
    }

    public void setStories(ArrayList<UserStory> stories) {
        this.stories = stories;
    }
}
