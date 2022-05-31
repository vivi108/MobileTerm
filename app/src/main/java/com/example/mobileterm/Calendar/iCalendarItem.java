package com.example.mobileterm.Calendar;

public class iCalendarItem {
    private String schedule;
    private String date;
    private String isDone;
    private String docA;


    public iCalendarItem(String schedule, String date, String isDone, String docA) {
        this.schedule = schedule;
        this.date = date;
        this.isDone = isDone;
        this.docA = docA;

    }


    public iCalendarItem(String str) { this.schedule = str; } //일단 이걸로 테스ㅡ트

    public iCalendarItem() {    }

    public String getDocA() {
        return docA;
    }

    public void setDocA(String docA) {
        this.docA = docA;
    }

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
