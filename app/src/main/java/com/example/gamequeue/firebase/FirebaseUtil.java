package com.example.gamequeue.firebase;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtil {
    // Since we need to specify database that is NOT in US Central
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance("https://gamequeue-pam-default-rtdb.asia-southeast1.firebasedatabase.app/");

    // Prevent instantiation
    private FirebaseUtil() {}

    public static FirebaseDatabase getDatabaseInstance() {
        return database;
    }
}
