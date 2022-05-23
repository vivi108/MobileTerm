package com.example.mobileterm.Calendar;

public class iCalendarItem {
    private String str;
    private String date;
    private String isDone;


    public iCalendarItem(String str, String date, String isDone) {
        this.str = str;
        this.date = date;
        this.isDone = isDone;
    }

    public iCalendarItem() {    }


    public String getStr() {
        return str;
    }

    public String getDate() {
        return date;
    }

    public String getIsDone() {
        return isDone;
    }

    public void setStr(String str)
    {
        this.str = str;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIsDone(String isDone) {
        this.isDone = isDone;
    }

}
