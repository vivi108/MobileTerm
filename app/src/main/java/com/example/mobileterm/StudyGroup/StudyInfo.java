package com.example.mobileterm.StudyGroup;

import java.util.ArrayList;

public class StudyInfo {
    private String description;
    private String leader;
    private long maxNumPeople;
    private ArrayList<String> memberList;
    private boolean isOpened;
    private String studyName;
    private String tags;
    private String password;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public long getMaxNumPeople() {
        return maxNumPeople;
    }

    public void setMaxNumPeople(long maxNumPeople) {
        this.maxNumPeople = maxNumPeople;
    }

    public ArrayList<String> getMemberList() {
        return memberList;
    }

    public void setMemberList(ArrayList<String> memberList) {
        this.memberList = memberList;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public StudyInfo() {
    }

    public StudyInfo(String description, String leader, long maxNumPeople, boolean isOpened, String studyName, String tags, String password) {
        this.description = description;
        this.leader = leader;
        this.maxNumPeople = maxNumPeople;
        this.memberList = new ArrayList<String>();
        this.memberList.add(leader);
        this.isOpened = isOpened;
        this.studyName = studyName;
        this.tags = tags;
        this.password = password;
    }
}
