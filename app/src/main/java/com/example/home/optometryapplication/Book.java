package com.example.home.optometryapplication;

public class Book {


    private String chapterName;
    private String chapterInfo;
    private int chapterNumber;
    private String mImageName;
    private String imageUrl;


    public Book() {
        //empty constructor
    }


    public Book(String chapterName, String chapterInfo, int chapterNumber, String imageUrl) {
        this.chapterName = chapterName;
        this.chapterInfo = chapterInfo;
        this.chapterNumber = chapterNumber;
        this.imageUrl = imageUrl;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public String getChapterName() {
        return chapterName;
    }

    public String getChapterInfo() {
        return chapterInfo;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public void setChapterInfo(String chapterInfo) {
        this.chapterInfo = chapterInfo;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

