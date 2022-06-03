package com.example.mobileterm.StudyGroup;

public class LikedStudyInfo {
    String sid;
    String name;

    public LikedStudyInfo(String sid, String name) {
        this.sid = sid;
        this.name = name;
    }

    public LikedStudyInfo() {
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
