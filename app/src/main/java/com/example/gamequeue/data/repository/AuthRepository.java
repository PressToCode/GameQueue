package com.example.gamequeue.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.gamequeue.data.firebase.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class AuthRepository {
    private static final String PREF_NAME = "GameQueuePrefs";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";

    private SharedPreferences sharedPreferences;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public AuthRepository(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseUtil.getDatabaseInstance().getReference();
    }

    // Save user data to SharedPreferences
    public void saveUserData(String userName, String userEmail) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.apply();
    }

    // Get user name from SharedPreferences
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    // Get user email from SharedPreferences
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }

    // Get current Firebase user
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    // Update user profile in Firebase
    public void updateUserProfile(String userName, String email, UpdateProfileCallback callback) {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            // Update email
            user.updateEmail(email).addOnCompleteListener(emailTask -> {
                if (emailTask.isSuccessful()) {
                    // Update name in Firebase Database
                    databaseReference.child("users").child(user.getUid())
                            .child("name").setValue(userName)
                            .addOnCompleteListener(nameTask -> {
                                if (nameTask.isSuccessful()) {
                                    // Save to local storage
                                    saveUserData(userName, email);
                                    callback.onSuccess();
                                } else {
                                    callback.onError("Failed to update name");
                                }
                            });
                } else {
                    callback.onError("Failed to update email");
                }
            });
        } else {
            callback.onError("User not authenticated");
        }
    }

    // Update password
    public void updatePassword(String newPassword, UpdatePasswordCallback callback) {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Failed to update password");
                }
            });
        } else {
            callback.onError("User not authenticated");
        }
    }

    // Logout
    public void logout() {
        firebaseAuth.signOut();
        // Clear local data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    // Callback interfaces
    public interface UpdateProfileCallback {
        void onSuccess();
        void onError(String error);
    }

    public interface UpdatePasswordCallback {
        void onSuccess();
        void onError(String error);
    }
}