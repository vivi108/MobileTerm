package com.example.mobileterm.Calendar;

public class iCalendarItem { //schedule 싹다 str로 바꿈 getSche이거말고
    private String str;
    private String date;
    private String isDone;


    public iCalendarItem(String str, String date, String isDone) {
        this.str = str;
        this.date = date;
        this.isDone = isDone;
    }

    public iCalendarItem(String str, String isDone) {
        this.str = str;
        this.isDone = isDone;
    }


    public iCalendarItem(String str) { this.str = str; } //일단 이걸로 테스ㅡ트

    public iCalendarItem() {    }


    public String getSchedule() {
        return str;
    }

    public String getDate() {
        return date;
    }

    public String getIsDone() { return isDone;  }

    public void setSchedule(String str)
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
