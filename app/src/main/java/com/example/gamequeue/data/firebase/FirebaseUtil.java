package com.example.gamequeue.data.firebase;

import static com.example.gamequeue.utils.FirebaseConst.DATABASE_URL;
import static com.example.gamequeue.utils.FirebaseConst.FIREBASE_WEB_CLIENT_ID;

import android.content.Context;

import androidx.credentials.ClearCredentialStateRequest;
import androidx.credentials.CredentialManager;
import androidx.credentials.GetCredentialRequest;

import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtil {
    // Authentication
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();

    // Database References
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance(DATABASE_URL);
    public static final DatabaseReference profilesRef = database.getReference("profiles");
    public static final DatabaseReference gamesRef = database.getReference("games");
    public static final DatabaseReference reservationsRef = database.getReference("reservations");

    // Google Sign In Credential Manager Object
    private static CredentialManager credentialManager;
    private static ClearCredentialStateRequest clearCredentialStateRequest;
    private static GetCredentialRequest request;

    // Prevent Data gone
    static {
        database.setPersistenceEnabled(true);
    }

    // Prevent instantiation
    private FirebaseUtil() {}

    // RUN THIS ONCE TO LET GOOGLE SIGN-IN (Preferably in Splash or Login)
    public void setup(Context context) {
        // Create Google Request Object for Credential Manager
        request = new GetCredentialRequest.Builder()
                .addCredentialOption(
                        new GetSignInWithGoogleOption
                        .Builder(FIREBASE_WEB_CLIENT_ID)
                        .build()
                )
                .build();

        // Create Clear Credential State Request Object
        clearCredentialStateRequest = new ClearCredentialStateRequest(ClearCredentialStateRequest.TYPE_CLEAR_CREDENTIAL_STATE);

        // Create CredentialManager
        credentialManager = CredentialManager.create(context);
    }

    // Getter
    public static DatabaseReference getProfilesRef() { return profilesRef; }
    public static DatabaseReference getGamesRef() { return gamesRef; }
    public static DatabaseReference getReservationsRef() { return reservationsRef; }
    public static FirebaseAuth getAuth() { return auth; }
    public static CredentialManager getCredentialManager() { return credentialManager; }
    public static ClearCredentialStateRequest getClearCredentialStateRequest() { return clearCredentialStateRequest; }
    public static GetCredentialRequest getRequest() { return request; }
}
