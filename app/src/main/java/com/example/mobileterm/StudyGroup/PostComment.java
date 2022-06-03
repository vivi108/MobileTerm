package com.example.mobileterm.StudyGroup;

public class PostComment {
    String name;
    String content;

    public PostComment(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public PostComment() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
