package com.example.gamequeue.data.model;

public class RequestModel {
    private String userId;
    private String reservationId;

    public RequestModel() {
        // Default Constructor
    }

    public RequestModel(String userId, String reservationId) {
        this.userId = userId;
        this.reservationId = reservationId;
    }

    // Getter & Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }
}
