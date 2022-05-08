package com.example.mobileterm.Init;

public class UserInfoClass {
    private String name;
    private String phone;
    private String date;
    private String email;

    public UserInfoClass() { }

    public UserInfoClass(String name, String date, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() { return email;}

    public void setEmail(String email) { this.email = email; }
}
