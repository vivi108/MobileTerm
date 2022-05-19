package com.example.mobileterm.studygroup;

import java.util.ArrayList;

public class JoinedStudyItem {

    private String joined_study_name;
    private String joined_study_member;
    private String[] tag;

    public String getJoined_study_name() {
        return joined_study_name;
    }

    public void setJoined_study_name(String joined_study_name) {
        this.joined_study_name = joined_study_name;
    }

    public String getJoined_study_member() {
        return joined_study_member;
    }

    public void setJoined_study_member(String joined_study_member) {
        this.joined_study_member = joined_study_member;
    }

    public String getTag(int position) {
        return tag[position];
    }

    public void setTag(String tag, int position) {
        this.tag[position] = tag;
    }
}
