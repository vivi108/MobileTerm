package com.example.mobileterm.ChattingActivity;

public class chatDTO {
    private String userName;
    private String message;

    public chatDTO() {}
    public chatDTO(String userName, String message) {
        this.userName = userName;
        this.message = message;
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
}
