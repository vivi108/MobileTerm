package com.example.mobileterm.StudyGroup;

import java.util.ArrayList;

public class StudyPostInfo {
    private String title;
    private String content;
    private String name;
    private String did;
    private String writtenTime;
    private Long likedCount;
    private Long downLoad;
    private float rating;
    private String tags;
    private ArrayList<String> likedUser;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public ArrayList<String> getLikedUser() {
        return likedUser;
    }

    public void setLikedUser(ArrayList<String> likedUser) {
        this.likedUser = likedUser;
    }

    public StudyPostInfo(String title, String content, String name, String did, String writtenTime, Long likedCount, Long downLoad, float rating, String tags) {
        this.title = title;
        this.content = content;
        this.name = name;
        this.did = did;
        this.writtenTime = writtenTime;
        this.likedCount = likedCount;
        this.downLoad = downLoad;
        this.rating = rating;
        this.tags = tags;
        this.likedUser = new ArrayList<>();
    }

    public StudyPostInfo() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getWrittenTime() {
        return writtenTime;
    }

    public void setWrittenTime(String writtenTime) {
        this.writtenTime = writtenTime;
    }

    public Long getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(Long likedCount) {
        this.likedCount = likedCount;
    }

    public Long getDownLoad() {
        return downLoad;
    }

    public void setDownLoad(Long downLoad) {
        this.downLoad = downLoad;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


}
