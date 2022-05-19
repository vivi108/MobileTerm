package com.example.mobileterm.BulletinBoard;

public class LikedBoardItem {
    String title;
    String did;

    public LikedBoardItem(String title, String did) {
        this.title = title;
        this.did = did;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public LikedBoardItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
