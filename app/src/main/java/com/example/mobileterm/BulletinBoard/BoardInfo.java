package com.example.mobileterm.BulletinBoard;

public class BoardInfo {
    private String title;
    private String content;
    private String name;
    private String did;
    private String writtenTime;
    private Long likedCount;

    public Long getLikeCount() {
        return likedCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likedCount = likeCount;
    }

    public BoardInfo(String title, String content, String name, String did, String writtenTime, Long likeCount) {
        this.title = title;
        this.content = content;
        this.name = name;
        this.did = did;
        this.writtenTime = writtenTime;
        this.likedCount = likeCount;
    }

    public String getWrittenTime() {
        return writtenTime;
    }

    public void setWrittenTime(String writtenTime) {
        this.writtenTime = writtenTime;
    }

    public BoardInfo(String title, String content, String name, String did, String writtenTime) {
        this.title = title;
        this.content = content;
        this.name = name;
        this.did = did;
        this.writtenTime = writtenTime;
    }

    public BoardInfo(String title, String content, String name, String did) {
        this.title = title;
        this.content = content;
        this.name = name;
        this.did = did;
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

}
