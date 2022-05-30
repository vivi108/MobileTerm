package com.example.mobileterm.Calendar;

public class iCalendarItem { //schedule 싹다 str로 바꿈 getSche이거말고
    private String schedule;
    private String date;
    private String isDone;


    public iCalendarItem(String schedule, String date, String isDone) {
        this.schedule = schedule;
        this.date = date;
        this.isDone = isDone;
    }

    public iCalendarItem(String str, String isDone) {
        this.schedule = str;
        this.isDone = isDone;
    }


    public iCalendarItem(String str) { this.schedule = str; } //일단 이걸로 테스ㅡ트

    public iCalendarItem() {    }


    public String getDate() {
        return date;
    }

    public String getIsDone() { return isDone;  }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIsDone(String isDone) {
        this.isDone = isDone;
    }

}
