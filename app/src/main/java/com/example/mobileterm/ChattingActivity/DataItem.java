package com.example.mobileterm.ChattingActivity;

public class DataItem {
    private String message;
    private String name;
    private int viewType;
    public DataItem(String content, String name, int viewType){
        this.message = content;
        this.viewType = viewType;
        this.name =name;
    }
    public String getContent(){return message;}
    public String getName(){return name;}
    public int getViewType() {return viewType;}
}
