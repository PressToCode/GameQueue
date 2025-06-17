package com.example.gamequeue.data.model;

import com.google.firebase.database.Exclude;

public class ReserveTimeModel {
    private String time;
    private String userId;
    private String reservationId;
    private String userEmail;

    public ReserveTimeModel() {
        // Required for Firebase
    }

    public ReserveTimeModel(String time, String usedId, String reservationId, String userEmail) {
        this.time = time;
        this.userId = usedId;
        this.reservationId = reservationId;
        this.userEmail = userEmail;
    }

    @Exclude
    public String getTime() {
        return time;
    }
    public void setTime(String time) { this.time = time; }

    public String getUserId() {
        return userId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
