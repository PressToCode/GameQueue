package com.example.gamequeue.data.firebase;

import static com.example.gamequeue.utils.FirebaseConst.DATABASE_URL;
import static com.example.gamequeue.utils.FirebaseConst.FIREBASE_WEB_CLIENT_ID;

import androidx.credentials.ClearCredentialStateRequest;
import androidx.credentials.CredentialManager;
import androidx.credentials.GetCredentialRequest;

import com.example.gamequeue.utils.ApplicationContext;
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtil {
    // Authentication
    private static final FirebaseAuth auth;

    // Database References
    private static final FirebaseDatabase database;
    public static final DatabaseReference gamesRef;
    public static final DatabaseReference reservationsRef;

    // Google Sign In Credential Manager Object
    private static final CredentialManager credentialManager;
    private static final ClearCredentialStateRequest clearCredentialStateRequest;
    private static final GetCredentialRequest request;

    // Initialize ONCE upon any method call
    static {
        // Initialize Firebase Objects
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(DATABASE_URL);

        // Set Data Persistence in case of offline - MUST BE CALLED BEFORE DB OPERATION
        database.setPersistenceEnabled(true);

        // Get References
        gamesRef = database.getReference("games");
        reservationsRef = database.getReference("reservations");

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
        credentialManager = CredentialManager.create(ApplicationContext.getAppContext());
    }

    // Prevent instantiation
    private FirebaseUtil() {}

    // Getter
    public static DatabaseReference getGamesRef() { return gamesRef; }
    public static DatabaseReference getReservationsRef() { return reservationsRef; }
    public static FirebaseAuth getAuth() { return auth; }
    public static CredentialManager getCredentialManager() { return credentialManager; }
    public static ClearCredentialStateRequest getClearCredentialStateRequest() { return clearCredentialStateRequest; }
    public static GetCredentialRequest getRequest() { return request; }
}
