package com.example.gamequeue.data.firebase;

import androidx.credentials.ClearCredentialStateRequest;
import androidx.credentials.CredentialManager;
import androidx.credentials.GetCredentialRequest;

import com.example.gamequeue.R;
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
    private static final DatabaseReference consolesRef;
    private static final DatabaseReference reservationsRef;
    private static final DatabaseReference requestRef;
    private static final DatabaseReference adminsRef;
    private static final DatabaseReference slotRef;

    // Google Sign In Credential Manager Object
    private static final CredentialManager credentialManager;
    private static final ClearCredentialStateRequest clearCredentialStateRequest;
    private static final GetCredentialRequest request;

    // Initialize ONCE upon any method call
    static {
        // Initialize Firebase Objects
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(ApplicationContext.getAppContext().getString(R.string.firebase_database_url));

        // Set Data Persistence in case of offline - MUST BE CALLED BEFORE DB OPERATION
        database.setPersistenceEnabled(true);

        // Get References
        consolesRef = database.getReference("consoles");
        reservationsRef = database.getReference("reservations");
        requestRef = database.getReference("requests");
        adminsRef = database.getReference("admins");
        slotRef = database.getReference("slot");

        // Create Google Request Object for Credential Manager
        request = new GetCredentialRequest.Builder()
                .addCredentialOption(
                        new GetSignInWithGoogleOption
                                .Builder(ApplicationContext.getAppContext().getString(R.string.firebase_web_client_id))
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
    public static DatabaseReference getConsolesRef() { return consolesRef; }
    public static DatabaseReference getReservationsRef() { return reservationsRef; }
    public static DatabaseReference getRequestRef() { return requestRef; }
    public static DatabaseReference getAdminsRef() { return adminsRef; }
    public static DatabaseReference getSlotRef() { return slotRef; }
    public static FirebaseAuth getAuth() { return auth; }
    public static CredentialManager getCredentialManager() { return credentialManager; }
    public static ClearCredentialStateRequest getClearCredentialStateRequest() { return clearCredentialStateRequest; }
    public static GetCredentialRequest getRequest() { return request; }
}
