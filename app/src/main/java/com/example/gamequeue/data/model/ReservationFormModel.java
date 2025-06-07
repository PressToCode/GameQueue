package com.example.gamequeue.data.model;

import android.net.Uri;

public class ReservationFormModel {
    private String consoleName;
    private String date;
    private String time;
    private String lenderName;
    private String lenderNIM;
    private String lenderPhone;
    private String lenderProdi;
    private Uri document;

    public String getConsoleName() {
        return consoleName;
    }

    public void setConsoleName(String consoleName) {
        this.consoleName = consoleName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLenderName() {
        return lenderName;
    }

    public void setLenderName(String lenderName) {
        this.lenderName = lenderName;
    }

    public String getLenderNIM() {
        return lenderNIM;
    }

    public void setLenderNIM(String lenderNIM) {
        this.lenderNIM = lenderNIM;
    }

    public String getLenderPhone() {
        return lenderPhone;
    }

    public void setLenderPhone(String lenderPhone) {
        this.lenderPhone = lenderPhone;
    }

    public String getLenderProdi() {
        return lenderProdi;
    }

    public void setLenderProdi(String lenderProdi) {
        this.lenderProdi = lenderProdi;
    }

    public Uri getDocument() {
        return document;
    }

    public void setDocument(Uri document) {
        this.document = document;
    }
}
