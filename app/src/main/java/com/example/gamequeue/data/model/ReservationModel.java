package com.example.gamequeue.data.model;

import android.net.Uri;

import com.google.firebase.database.Exclude;

public class ReservationModel {
    private String id;
    private String consoleId;
    private String consoleName;
    private String dayName;
    private String date;
    private String time;
    private String lenderEmail;
    private String lenderName;
    private String lenderNIM;
    private String lenderPhone;
    private String lenderProdi;
    private Uri document;
    private String verificationCode;
    private String status;

    public ReservationModel() {
        // Default Constructor
    }

    @Exclude
    public String getId() { return id; }

    @Exclude
    public void setId(String id) { this.id = id; }

    public String getConsoleId() { return consoleId; }

    public void setConsoleId(String consoleId) { this.consoleId = consoleId; }

    public String getConsoleName() { return consoleName; }

    public void setConsoleName(String consoleName) { this.consoleName = consoleName; }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
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

    public String getLenderEmail() { return lenderEmail; }

    public void setLenderEmail(String lenderEmail) { this.lenderEmail = lenderEmail; }

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

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getVerificationCode() { return verificationCode; }

    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }
}
