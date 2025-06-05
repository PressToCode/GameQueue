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
import com.example.gamequeue.data.model.ProfileModel;
import com.example.gamequeue.utils.CustomCallback;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AuthRepository {
    // Firebase Utilities Singletons Alias
    private static final FirebaseAuth auth = FirebaseUtil.getAuth();
    private static final FirebaseUser user = auth.getCurrentUser();

    // Executor for Async Call
    private static final Executor executor = Executors.newSingleThreadExecutor();

    // Firebase Authenticated User Metadata
    public static String getFirebaseAuthUserUid() { return user.getUid(); }
    public static String getFirebaseAuthUserName() { return user.getDisplayName(); }
    public static String getFirebaseAuthUserEmail() { return user.getEmail(); }
    public static Uri getFirebaseAuthUserPhotoUrl() { return user.getPhotoUrl(); }

    // Check Authentication Status
    public static boolean isLoggedIn() { return auth.getCurrentUser() != null; }

    // Register Using Email & Password
    public static void registerWithEmailAndPassword(String email, String password, CustomCallback callback) {
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
                ProfileModel.setAll();
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
                    ProfileModel.setAll();
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
        FirebaseUtil.getCredentialManager()
                .clearCredentialStateAsync(
                        FirebaseUtil.getClearCredentialStateRequest(),
                        null,
                        executor,
                        new CredentialManagerCallback<>() {
                            @Override
                            public void onResult(Void unused) {
                                ProfileModel.removeAll();
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
