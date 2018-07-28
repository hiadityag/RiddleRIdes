package com.example.aditya.rideapp;

/**
 * Created by aditya Gupta  on 10/24/2017.
 */

public class ListOfRiderModel {
    private String nameRider;
    private String sourceText;
    private String destText;
    private String time;
    private String Persons;
    private String Begs;
    private String SourceLat;
    private String SourceLong;

    public ListOfRiderModel(){}

    public ListOfRiderModel(String nameRider, String sourceText, String destText, String time) {
        this.nameRider = nameRider;
        this.sourceText = sourceText;
        this.destText = destText;
        this.time = time;
    }


    public String getNameRider() {
        return nameRider;
    }

    public void setNameRider(String nameRider) {
        this.nameRider = nameRider;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public String getDestText() {
        return destText;
    }

    public void setDestText(String destText) {
        this.destText = destText;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
