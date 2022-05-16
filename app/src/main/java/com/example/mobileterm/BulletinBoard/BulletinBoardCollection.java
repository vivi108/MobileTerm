package com.example.mobileterm.BulletinBoard;

public class BulletinBoardCollection {
    String content;
    String name;
    String studyName;
    String title;

    public BulletinBoardCollection(String content, String name, String studyName, String title) {
        this.content = content;
        this.name = name;
        this.studyName = studyName;
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

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
