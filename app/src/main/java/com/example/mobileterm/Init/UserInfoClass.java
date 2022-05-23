package com.example.mobileterm.Init;

public class UserInfoClass {
    private String name;
    private String phone;
    private String date;
    private String email;
    private String token;
    private String nickname;
    private String regDate;
    private String profileImageURL;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserInfoClass(String name, String phone, String date, String email, String nickname, String regDate, String address) {
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.email = email;
        this.nickname = nickname;
        this.regDate = regDate;
        this.address = address;
        this.token = "0";
        this.profileImageURL = "ic_profile_gray";
    }

    public UserInfoClass() { }

//    public UserInfoClass(String name, String date, String phone, String email, String nickname, String regDate) {
//        this.name = name;
//        this.phone = phone;
//        this.date = date;
//        this.email = email;
//        this.nickname = nickname;
//        this.token = "0";
//        this.profileImageURL = "ic_profile_gray";
//        this.regDate = regDate;
//    }

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

    public String getToken() { return token;}

    public void setToken(String token) {this.token = token;}

    public String getNickname() { return nickname; }

    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getProfileImageURL() { return profileImageURL; }

    public void setProfileImageURL(String profileImageURL) { this.profileImageURL = profileImageURL; }

    public String getRegDate() { return regDate ;}

    public void setRegDate(String regDate) { this.regDate = regDate ;}
}
