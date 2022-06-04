package com.example.mobileterm.StudyGroup;

public class GScheduleInfo {
    String studyName;
    String scheduleName;
    String place;
    String meetingTime;
    String meetingDay;

    public String getMeetingDay() {
        return meetingDay;
    }

    public void setMeetingDay(String meetingDay) {
        this.meetingDay = meetingDay;
    }

    public GScheduleInfo(String studyName, String scheduleName, String place, String meetingTime, String meetingDay) {
        this.studyName = studyName;
        this.scheduleName = scheduleName;
        this.place = place;
        this.meetingTime = meetingTime;
        this.meetingDay = meetingDay;
    }

    public GScheduleInfo() {
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }
}
