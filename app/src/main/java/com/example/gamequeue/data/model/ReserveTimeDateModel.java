package com.example.gamequeue.data.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class ReserveTimeDateModel {
    private String dayName;
    private String date;

    public ReserveTimeDateModel() {
        // Required for Firebase
    }

    public ReserveTimeDateModel(String dayName, String date) {
        this.dayName = dayName;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    @Exclude
    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }
}
