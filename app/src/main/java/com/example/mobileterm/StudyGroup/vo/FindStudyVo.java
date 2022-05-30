package com.example.mobileterm.StudyGroup.vo;

import com.example.mobileterm.R;

public class FindStudyVo {
    private String[] members;
    private String studyName;
    private String tags;
    private String studyCapacity;
    private String description;
    private String isOpened;
    private String isLiked;
    private String pw;

    public FindStudyVo(String studyName, String studyCapacity, String[] members, String tags,
                       String description, String isOpened, String isLiked) {
        this.studyName = studyName;
        this.members = members;
        this.studyCapacity = members.length + "/" + studyCapacity;
        this.tags = tags;
        this.description = description;
        this.isOpened = isOpened;
        this.isLiked = isLiked;
        this.pw = null;
    }

    public FindStudyVo(String studyName, String studyCapacity, String[] members, String tags,
                       String description, String isOpened, String isLiked, String pw) {
        this.studyName = studyName;
        this.members = members;
        this.studyCapacity = members.length + "/" + studyCapacity;
        this.tags = tags;
        this.description = description;
        this.isOpened = isOpened;
        this.isLiked = isLiked;
        this.pw = pw;
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

    public void setStudyCapacity(String studyCapacity, String[] members) {
        this.studyCapacity = members.length + "/" + studyCapacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsOpened() {
        return isOpened;
    }

    public void setIsOpened(String isOpened) {
        this.isOpened = isOpened;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}
