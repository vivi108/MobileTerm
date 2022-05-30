package com.example.mobileterm.StudyGroup.vo;

public class PostVo {
    private String Writer;
    private String UploadDate;
    private String PostName;
    private String content;
    private String tag;
    private String fileName;
    private String commentSize;
    private String likeNum;
    private String downLoad;
    private float rating;
    private String starRate;

    public PostVo(String Writer, String UploadDate, String PostName, String content, String tag,
                  String fileName, String commentSize, String likeNum,
                  String downLoad, float rating, String starRate){
        this.Writer = Writer;
        this.UploadDate = UploadDate;
        this.PostName = PostName;
        this.content = content;
        this.tag = tag;
        this.fileName = fileName;
        this.commentSize = commentSize;
        this.likeNum = likeNum;
        this.downLoad = downLoad;
        this.rating = rating;
        this.starRate = starRate;
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        Writer = writer;
    }

    public String getUploadDate() {
        return UploadDate;
    }

    public void setUploadDate(String uploadDate) {
        UploadDate = uploadDate;
    }

    public String getPostName() {
        return PostName;
    }

    public void setPostName(String postName) {
        PostName = postName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(String commentSize) {
        this.commentSize = commentSize;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    public String getDownLoad() {
        return downLoad;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setDownLoad(String downLoad) {
        this.downLoad = downLoad;
    }

    public String getStarRate() {
        return starRate;
    }

    public void setStarRate(String starRate) {
        this.starRate = starRate;
    }
}
