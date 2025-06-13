/*
 * We do NOT need to store data
 * our app is ONLINE-only
 * due to the nature of data-fetching
 * real-time reservation, and authentication
 *
 * DO NOT STORE DATA LOCALLY
 */

package com.example.gamequeue.data.repository;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.ClearCredentialException;
import androidx.credentials.exceptions.GetCredentialException;

import com.example.gamequeue.data.firebase.FirebaseUtil;
import com.example.gamequeue.data.model.SharedProfileModel;
import com.example.gamequeue.utils.ApplicationContext;
import com.example.gamequeue.utils.CustomCallback;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class AuthRepository {
    // Firebase Utilities Singletons Alias
    private static final FirebaseAuth auth;

    // Executor for Async Call
    private static final Executor executor;

    // Static Constructor - ensure Singleton
    static {
        auth = FirebaseUtil.getAuth();
        executor = Executors.newSingleThreadExecutor();
    }

    // Firebase Authenticated User Metadata
    public static String getFirebaseAuthUserUid() { return auth.getCurrentUser().getUid(); }
    public static String getFirebaseAuthUserName() { return auth.getCurrentUser().getDisplayName(); }
    public static String getFirebaseAuthUserEmail() { return auth.getCurrentUser().getEmail(); }
    public static Uri getFirebaseAuthUserPhotoUrl() { return auth.getCurrentUser().getPhotoUrl(); }

    // Firebase Update Name Only
    public static void updateName(String name) {
        // Create Profile Update Request
        UserProfileChangeRequest nameUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        auth.getCurrentUser().updateProfile(nameUpdate);
    }

    // Firebase Authenticated User Modify
    public static void updateProfile(String name, String email, String oldPassword, String newPassword, CustomCallback callback) {
        // Track All Steps Success Update
        AtomicReference<Boolean> success = new AtomicReference<>(true);

        // Create Profile Update Request
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        // Update DisplayName
        auth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                return;
            }

            callback.onError(task.getException().getMessage());
            success.set(false);
        });

        // Stop if error occurs
        if(!success.get()) {
            return;
        }

        // Re-Authenticate
        // Needed to update email and password
        reAuthenticate(oldPassword, new CustomCallback() {
            @Override
            public void onSuccess() {}
            @Override
            public void onError(String message) {
                callback.onError(message);
                success.set(false);
            }
        });

        // Stop if error occurs
        if(!success.get()) {
            return;
        }

        // Update Email
        auth.getCurrentUser().updateEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                return;
            }

            callback.onError(task.getException().getMessage());
            success.set(false);
        });

        // Stop if error occurs
        if(!success.get()) {
            return;
        }

        // Update Password
        auth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                return;
            }

            callback.onError(task.getException().getMessage());
            success.set(false);
        });

        // Re-Authenticate
        // After successful change
        reAuthenticate(newPassword, new CustomCallback() {
            @Override
            public void onSuccess() { callback.onSuccess(); }
            @Override
            public void onError(String message) {
                callback.onError(message);
                success.set(false);
            }
        });
    }

    // Re-authenticate for profile change
    public static void reAuthenticate(String oldPassword, CustomCallback callback) {
        AuthCredential credential = EmailAuthProvider.getCredential(FirebaseUtil.getAuth().getCurrentUser().getEmail(), oldPassword);

        auth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                callback.onSuccess();
                return;
            }

            callback.onError(task.getException().getMessage());
        });
    }

    // Check Authentication Status
    public static boolean isLoggedIn() {
        // TODO: REMOVE ON PRODUCTION
        if (ApplicationContext.getDevMode()) {
            return true;
        }
        return auth.getCurrentUser() != null;
    }

    // Register Using Email & Password
    public static void registerWithEmailAndPassword(String name, String email, String password, CustomCallback callback) {
        updateName(name);

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                callback.onSuccess();
                return;
            }

            callback.onError("Register Failed");
        });
    }

    // Login Using Email & Password
    public static void loginWithEmailAndPassword(String email, String password, CustomCallback callback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                SharedProfileModel.setAll();
                callback.onSuccess();
                return;
            }

            callback.onError("Login Failed");
        });
    }

    // Login with Google -------------------------
    public static void loginWithGoogle(Context context, CustomCallback callback) {
        FirebaseUtil.getCredentialManager()
                .getCredentialAsync(context, FirebaseUtil.getRequest(), null, executor, new CredentialManagerCallback<>() {
                    @Override
                    public void onResult(GetCredentialResponse getCredentialResponse) {
                        handleSignIn(getCredentialResponse.getCredential(), callback);
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        callback.onError(e.getMessage());
                    }
                });
    }

    private static void handleSignIn(Credential credential, CustomCallback callback) {
        if (credential instanceof CustomCredential && credential.getType().equals(GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {

            AuthCredential authCredential = GoogleAuthProvider.getCredential(GoogleIdTokenCredential.createFrom(credential.getData()).getIdToken(), null);
            auth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    SharedProfileModel.setAll();
                    callback.onSuccess();
                    return;
                }

                callback.onError("Login Failed");
            });
        } else {
            callback.onError("Mismatched Credential");
        }
    }

    // End of Login with Google -----------------


    // Logout - Async to prevent UI Thread Stuck
    public static void logout(CustomCallback callback) {
        // TODO: REMOVE ON PRODUCTION
        if (ApplicationContext.getDevMode()) {
            callback.onSuccess();
            return;
        }

        FirebaseUtil.getCredentialManager()
                .clearCredentialStateAsync(
                        FirebaseUtil.getClearCredentialStateRequest(),
                        null,
                        executor,
                        new CredentialManagerCallback<>() {
                            @Override
                            public void onResult(Void unused) {
                                SharedProfileModel.removeAll();
                                auth.signOut();
                                callback.onSuccess();
                            }

                            @Override
                            public void onError(@NonNull ClearCredentialException e) {
                                callback.onError(e.getMessage());
                            }
                        }
                );
    }
}
