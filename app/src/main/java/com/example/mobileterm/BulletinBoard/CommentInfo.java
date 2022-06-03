package com.example.mobileterm.BulletinBoard;

public class CommentInfo {
    private String content;
    private String name;
    private String writtenTime;
    private boolean isSecret;

    public String getWrittenTime() {
        return writtenTime;
    }

    public void setWrittenTime(String writtenTime) {
        this.writtenTime = writtenTime;
    }

    public boolean isSecret() {
        return isSecret;
    }

    public void setSecret(boolean secret) {
        isSecret = secret;
    }

    public CommentInfo(String content, String name, String writtenTime) {
        this.content = content;
        this.name = name;
        this.writtenTime = writtenTime;
        this.isSecret = false;
    }

    public CommentInfo(String content, String name) {
        this.content = content;
        this.name = name;
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
}
