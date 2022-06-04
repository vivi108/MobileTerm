package com.example.mobileterm.ChattingActivity;

import java.util.Date;

public class DataItem {
    private String content;
    private String name;
    private int viewType;
    private Date time;
    public DataItem(String content, String name, int viewType, Date t){
        this.content = content;
        this.viewType = viewType;
        this.name =name;
        this.time =t;
    }
    public String getContent(){return content;}
    public String getName(){return name;}
    public int getViewType() {return viewType;}
    public Date getTime() {return time;}
}
