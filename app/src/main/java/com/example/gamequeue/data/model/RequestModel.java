package com.example.gamequeue.data.model;

import com.google.firebase.database.Exclude;

public class RequestModel {
    private String userId;
    private String reservationId;
    private String consoleId;

    public RequestModel() {
        // Default Constructor
    }

    public RequestModel(String userId, String consoleId) {
        this.userId = userId;
        this.consoleId = consoleId;
    }

    // Getter & Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Exclude
    public String getReservationId() {
        return reservationId;
    }

    @Exclude
    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getConsoleId() {
        return consoleId;
    }

    public void setConsoleId(String consoleId) {
        this.consoleId = consoleId;
    }
}
