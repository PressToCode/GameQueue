package com.example.gamequeue.data.model;

// Used as base model for cards
public class ConsoleModel {
    private String title = "NO TITLE";
    private String status = "NO STATUS";
    private String date = "NO DATE";
    private String time = "NO TIME";
    private String specificationOne = "NO SPECIFICATION";
    private String specificationTwo = "NO SPECIFICATION";
    private String specificationThree = "NO SPECIFICATION";

    // Default Constructor
    public ConsoleModel() {
        // Required for Firebase
    }

    // Constructor
    public ConsoleModel(String title, int status, String date, String time, String specificationOne, String specificationTwo, String specificationThree) {
        this.title = title;
        this.status = statusConverter(status);
        this.date = date;
        this.time = time;
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

    // Utility
    private String statusConverter(int status) {
        if(status == 0) {
            return "Pending";
        } else if (status == 1) {
            return "Confirmed";
        } else if (status == 2) {
            return "Rejected";
        } else {
            return "NULL";
        }
    }
}
