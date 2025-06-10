package com.example.gamequeue.data.model;

import com.google.firebase.database.Exclude;

// Used as base model for cards
public class ConsoleModel {
    private String id = "-1";
    private String title = "NO TITLE";
    private String specificationOne = "NO SPECIFICATION";
    private String specificationTwo = "NO SPECIFICATION";
    private String specificationThree = "NO SPECIFICATION";
    private Boolean lendingStatus;
    private String lenderUid;

    // Default Constructor
    public ConsoleModel() {
        // Required for Firebase
    }

    // Constructor - ONLY WHEN POPULATING THE FIREBASE DATABASE
    public ConsoleModel(String title, String specificationOne, String specificationTwo, String specificationThree) {
        // ID SHOULD BE GET FROM FIREBASE REALTIME DATABASE
        this.title = title;
        this.specificationOne = specificationOne;
        this.specificationTwo = specificationTwo;
        this.specificationThree = specificationThree;
    }

    // Getter
    @Exclude // ! IMPORTANT - PREVENT STORING REDUNDANT ID FIELD
    public String getId() { return id; }

    public void setId(String id) { this.id = id; }
    public String getTitle() {
        return title;
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

    public void setLending(Boolean lendingStatus, String lenderUid) {
        this.lendingStatus = lendingStatus;
        this.lenderUid = lenderUid;
    }
    public Boolean getLendingStatus() { return lendingStatus; }
    public String getLenderUid() { return lenderUid; }
}
