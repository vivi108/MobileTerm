package com.example.mobileterm.StudyGroup;

public class PostComment {
    String name;
    String content;
    String wtime;

    public String getWtime() {
        return wtime;
    }

    public void setWtime(String wtime) {
        this.wtime = wtime;
    }

    public PostComment(String name, String content, String wtime) {
        this.name = name;
        this.content = content;
        this.wtime = wtime;
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
