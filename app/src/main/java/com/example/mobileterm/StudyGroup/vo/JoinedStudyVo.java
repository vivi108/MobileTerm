package com.example.mobileterm.StudyGroup.vo;

import java.util.ArrayList;

public class JoinedStudyVo {
    private String[] members;
    private String studyName;
    private String tags;
    private String studyCapacity;
    private String description;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JoinedStudyVo( String studyName, String studyCapacity,String[] members, String tags,  String description, String address) {
        this.members = members;
        this.studyName = studyName;
        this.tags = tags;
        this.studyCapacity = studyCapacity;
        this.description = description;
        this.address = address;
    }

    public JoinedStudyVo(String studyName, String studyCapacity, String[] members, String tags) {
        this.studyName = studyName;
        this.studyCapacity = studyCapacity;
        this.members = members;
        this.tags = tags;
    }

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStudyCapacity() {
        return studyCapacity;
    }

    public void setStudyCapacity(String studyCapacity) {
        this.studyCapacity = studyCapacity;
    }
}
