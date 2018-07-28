package com.example.shubhampratap.rideapp;

public class AllRidesModel {
    private String RiderName;
    private String TakerName;
    private String sourceText;
    private String destText;
    private String Date;
    private String Status;


    public AllRidesModel() {}
    public AllRidesModel(String riderName, String takerName, String sourceText, String destText, String date, String status) {
        RiderName = riderName;
        TakerName = takerName;
        this.sourceText = sourceText;
        this.destText = destText;
        Date = date;
        Status = status;
    }

    public String getRiderName() {
        return RiderName;
    }

    public void setRiderName(String riderName) {
        RiderName = riderName;
    }

    public String getTakerName() {
        return TakerName;
    }

    public void setTakerName(String takerName) {
        TakerName = takerName;
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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}

