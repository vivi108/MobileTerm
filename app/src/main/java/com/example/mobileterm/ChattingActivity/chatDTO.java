package com.example.mobileterm.ChattingActivity;

import java.util.Date;

public class chatDTO {
    private String userName;
    private String message;
    private Date time;
    public chatDTO() {}
    public chatDTO(String userName, String message, Date t) {
        this.userName = userName;
        this.message = message;
        this.time = t;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }
    public Date getTime() {return time;}
}
