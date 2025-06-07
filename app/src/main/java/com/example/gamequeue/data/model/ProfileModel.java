package com.example.gamequeue.data.model;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.example.gamequeue.data.repository.AuthRepository;
import com.example.gamequeue.data.repository.DatabaseRepository;
import com.example.gamequeue.utils.CustomCallbackWithString;

public class ProfileModel extends ViewModel {
    private static String uid;
    private static String name;
    private static String email;
    private static Uri profileImageUrl;

    public ProfileModel() {
        // Default constructor required for Firebase
    }

    // For faster cleaner call
    public static void setAll() {
        setUid();
        setName();
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

        // fallback 1 - Get username from database
        if(name == null || name.isEmpty()) {
            DatabaseRepository.getFirebaseDatabaseUserName(new CustomCallbackWithString() {
                @Override
                public void onSuccess(String message) {
                    name = message;
                }

                @Override
                public void onError(String error) {
                    // Fallback 2 - get name from email
                    name = AuthRepository.getFirebaseAuthUserEmail().split("@")[0];

                    // Last Fallback - if firebase failed to get email / offline
                    if (name == null || name.isEmpty()) {
                        name = "Guest";
                    }
                }
            });
        }
    }

    public static void setEmail() {
        email = AuthRepository.getFirebaseAuthUserEmail();
    }

    public static void setProfileImageUrl() {
        // Can be null - Setup later in profile activity
        profileImageUrl = AuthRepository.getFirebaseAuthUserPhotoUrl();
    }
}