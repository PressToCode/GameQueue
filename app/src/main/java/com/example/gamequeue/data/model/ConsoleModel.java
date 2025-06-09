package com.example.gamequeue.data.model;

// Used as base model for cards
public class ConsoleModel {
    private int id = -1;
    private String title = "NO TITLE";
    private int rawStatus = -1;
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
    public ConsoleModel(int id, String title, int status, String date, String time, String specificationOne, String specificationTwo, String specificationThree) {
        // ID SHOULD BE GET FROM FIREBASE REALTIME DATABASE
        this.id = id;
        this.title = title;
        this.rawStatus = status;
        this.status = statusConverter(status);
        this.date = date;
        this.time = time;
        this.specificationOne = specificationOne;
        this.specificationTwo = specificationTwo;
        this.specificationThree = specificationThree;
    }

    // Getter
    public int getId() { return id; }

    public String getTitle() {
        return title;
    }

    public int getRawStatus() { return rawStatus; }

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
