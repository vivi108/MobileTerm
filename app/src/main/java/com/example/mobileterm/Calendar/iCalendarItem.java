package com.example.mobileterm.Calendar;

public class iCalendarItem {
    private String schedule;
    private String date;
    private String isDone;


    public iCalendarItem(String schedule, String date, String isDone) {
        this.schedule = schedule;
        this.date = date;
        this.isDone = isDone;
    }

    public iCalendarItem(String schedule, String isDone) {
        this.schedule = schedule;
        this.isDone = isDone;
    }

    public iCalendarItem(String schedule) { this.schedule = schedule; } //일단 이걸로 테스ㅡ트

    public iCalendarItem() {    }


    public String getSchedule() {
        return schedule;
    }

    public String getDate() {
        return date;
    }

    public String getIsDone() {
        return isDone;
    }

    public void setSchedule(String schedule)
    {
        this.schedule = schedule;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIsDone(String isDone) {
        this.isDone = isDone;
    }

}
