package com.example.mobileterm.BulletinBoard;

public class CommentInfo {
    private String content;
    private String name;

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
