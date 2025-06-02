package com.example.gamequeue.data.model;

import android.graphics.Color;

import com.example.gamequeue.R;

public class ConsoleModel {
    private String title = "NO TITLE";
    private String status = "NO STATUS";
    private String date = "NO DATE";
    private String time = "NO TIME";
    private String specificationOne = "NO SPECIFICATION";
    private String specificationTwo = "NO SPECIFICATION";
    private String specificationThree = "NO SPECIFICATION";

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

    public ConsoleModel(String title, int status, String specificationOne, String specificationTwo, String specificationThree) {
        this.title = title;
        if(status == 0) {
            this.status = "Pending";
        } else if (status == 1) {
            this.status = "Confirmed";
        } else {
            this.status = "Rejected";
        }
        this.specificationOne = specificationOne;
        this.specificationTwo = specificationTwo;
        this.specificationThree = specificationThree;
    }

    // Getter
    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getSpecificationOne() {
        return specificationOne;
    }

    public String getSpecificationTwo() {
        return specificationTwo;
    }

    public String getSpecificationThree() {
        return specificationThree;
    }
}
