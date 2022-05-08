package com.example.mobileterm.BulletinBoard;

public class BoardInfo {
    private String title;
    private String tags;
    private String content;
    private String name;
    public BoardInfo(String title, String tags, String content, String name){
        this.title = title;
        this.tags = tags;
        this.content = content;
        this.name = name;
    }
    public String getTitle() { return this.title;}

    public String getTags() { return this.tags;}

    public String getContent() { return this.content; }

    public String getName(){ return this.name;}

    public void setTitle(String title){ this.title = title;}

    public void setTags(String tags) { this.tags = tags; }

    public void setContent(String content) {this.content = content;}

    public void setName(String name) {this.name = name;}


}
