package com.example.gamequeue.data.model;

import android.graphics.Color;

import com.example.gamequeue.R;

public class ConsoleModel {
    private String title;
    private String status;
    private String date;
    private String time;

    // Constructor
    public ConsoleModel(String title, int status, String date, String time) {
        this.title = title;
        if(status == 0) {
            this.status = "Pending";
        } else if (status == 1) {
            this.status = "Confirmed";
        } else {
            this.status = "Rejected";
        }
        this.date = date;
        this.time = time;
    }
}
