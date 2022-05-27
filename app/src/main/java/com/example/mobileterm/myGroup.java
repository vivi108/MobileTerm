package com.example.mobileterm;

import java.util.ArrayList;

public class myGroup {

    public ArrayList<String> child;
    public String groupName;
    public ArrayList<String> childId;

    myGroup(String name) {
        groupName = name;
        child = new ArrayList<String>();
        childId = new ArrayList<String>();
    }

}
