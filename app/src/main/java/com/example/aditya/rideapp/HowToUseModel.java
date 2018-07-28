package com.example.aditya.rideapp;

/**
 * Created by aditya gupta on 10/27/2017.
 */

public class HowToUseModel {
    private String index;
    private String title;
    private String descImg;
    private String descText;

    public HowToUseModel(){}

    public HowToUseModel(String index, String title, String descImg, String descText) {
        this.index = index;
        this.title = title;
        this.descImg = descImg;
        this.descText = descText;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescImg() {
        return descImg;
    }

    public void setDescImg(String descImg) {
        this.descImg = descImg;
    }

    public String getDescText() {
        return descText;
    }

    public void setDescText(String descText) {
        this.descText = descText;
    }
}
