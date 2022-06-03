package com.example.mobileterm.Calendar;


public class gCalendarItem {
    private String schedule;
    private String date;
    private String time;
    private String docA;
    private String place;


    public gCalendarItem(String schedule, String date, String time, String docA, String place) {
        this.schedule = schedule;
        this.date = date;
        this.time = time;
        this.docA = docA;
        this.place = place;
    }



    public gCalendarItem(String str) { this.schedule = str; } //일단 이걸로 테스ㅡ트

    public gCalendarItem() {    }

    public String getDocA() {
        return docA;
    }

    public String getDate() {
        return date;
    }

    public String getTime() { return time;  }

    public String getSchedule() {
        return schedule;
    }

    public String getPlace() {return place;}

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDocA(String docA) {
        this.docA = docA;
    }

    public void setPlace(String place) { this.place = place;}

}
