package com.example.mobileterm.ChattingActivity;

public class DataItem {
    private String content;
    private String name;
    private int viewType;
    public DataItem(String content, String name, int viewType){
        this.content = content;
        this.viewType = viewType;
        this.name =name;
    }
    public String getContent(){return content;}
    public String getName(){return name;}
    public int getViewType() {return viewType;}
}
