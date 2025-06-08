package com.example.gamequeue.data.model;

import android.net.Uri;

import com.example.gamequeue.data.repository.AuthRepository;

public class SharedProfileModel {
    private static String uid;
    private static String name;
    private static String email;
    private static Uri profileImageUrl;

    public SharedProfileModel() {
        // Default constructor required for Firebase
    }

    // For faster cleaner call
    public static void setAll() {
        setUid();
        setName();
        setEmail();
        setProfileImageUrl();
    }

    public static void removeAll() {
        uid = null;
        name = null;
        email = null;
        profileImageUrl = null;
    }

    // Setter
    public static void setUid() {
        uid = AuthRepository.getFirebaseAuthUserUid();
    }

    public static void setName() {
        // Get from firebase auth first (case: google sign-in)
        name = AuthRepository.getFirebaseAuthUserName();

        // Fallback 1 - get name from email
        if (name == null || name.isEmpty()) {
            name = AuthRepository.getFirebaseAuthUserEmail().split("@")[0];
        }

        // Last Fallback - if firebase failed to get email / offline
        if (name == null || name.isEmpty()) {
            name = "Guest";
        }
    }

    public static void setEmail() {
        email = AuthRepository.getFirebaseAuthUserEmail();
    }

    public static void setProfileImageUrl() {
        // Can be null - Setup later in profile activity
        profileImageUrl = AuthRepository.getFirebaseAuthUserPhotoUrl();
    }

    // Getter
    public static String getUid() {
        return uid;
    }

    public static String getName() {
        return name;
    }

    public static String getEmail() {
        return email;
    }

    public static Uri getProfileImageUrl() {
        return profileImageUrl;
    }
}