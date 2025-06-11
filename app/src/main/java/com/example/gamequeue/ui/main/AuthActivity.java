package com.example.gamequeue.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamequeue.R;
import com.example.gamequeue.data.firebase.FirebaseUtil;
import com.example.gamequeue.data.model.SharedProfileModel;
import com.example.gamequeue.data.repository.AuthRepository;
import com.example.gamequeue.data.repository.DatabaseRepository;
import com.example.gamequeue.utils.ApplicationContext;
import com.example.gamequeue.utils.CustomCallback;

public class AuthActivity extends AppCompatActivity {
    // Variables
    private LinearLayout authNameContainer;
    private TextView authTitle, authSubtitle, authNameField, authEmailField, authPasswordField, authBottomText, authBottomLink;
    private Button authMainButton, authLoginGoogle, devModeBtn, resetDb;
    private boolean isRegistering = false;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, 0);

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Check for Actual Auth
        // TODO: CHANGE AFTER DEV MODE IS REMOVED TO AUTH REPOSITORY METHOD
        if(FirebaseUtil.getAuth().getCurrentUser() != null) {
            SharedProfileModel.setAll();
            startActivity(new Intent(context, MainActivity.class));
            finish();
        }

        // Initialization
        authNameContainer = findViewById(R.id.authNameContainer);
        authTitle = findViewById(R.id.authTitle);
        authSubtitle = findViewById(R.id.authSubtitle);
        authNameField = findViewById(R.id.authNameField);
        authEmailField = findViewById(R.id.authEmailField);
        authPasswordField = findViewById(R.id.authPasswordField);
        authBottomText = findViewById(R.id.authBottomText);
        authBottomLink = findViewById(R.id.authBottomLink);
        authMainButton = findViewById(R.id.authMainButton);
        authLoginGoogle = findViewById(R.id.authLoginGoogle);
        devModeBtn = findViewById(R.id.devModeButton);
        resetDb = findViewById(R.id.resetDb);

        // Setup Listener
        setupListeners();

        // Ensure Dev Mode is not active
        // TODO: REMOVE ON PRODUCTION
        ApplicationContext.setDevMode(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: REMOVE ON PRODUCTION
        ApplicationContext.setDevMode(false);
    }

    private void setupListeners() {
        // TODO: Remove dev mode on Production
        // Skips authentication directly
        devModeBtn.setOnClickListener(v -> {
            ApplicationContext.setDevMode(true);
            startActivity(new Intent(context, MainActivity.class));
//            DatabaseRepository.addConsolesDataToFirebase(new CustomCallback() {
//                @Override
//                public void onSuccess() {
//                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onError(String error) {}
//            });
        });

        resetDb.setOnClickListener(v -> {
            DatabaseRepository.resetAll(new CustomCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Normal Authentication
        authMainButton.setOnClickListener(v -> {
            // Get field values
            String name = authNameField.getText().toString();
            String email = authEmailField.getText().toString();
            String password = authPasswordField.getText().toString();

            // Check if field is empty
            if (isRegistering && name.isEmpty()) {
                authNameField.setError("Nama tidak boleh kosong");
            }

            if (email.isEmpty()) {
                authEmailField.setError("Email tidak boleh kosong");
            }

            if (password.isEmpty()) {
                Toast.makeText(context, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(context, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show();
            }

            // Guard, Stop login or Register Attempt
            if ((isRegistering && name.isEmpty())
                    || email.isEmpty()
                    || password.isEmpty()
                    || password.length() < 6
            ) {
                return;
            }

            // Auth Call
            if (isRegistering) {
                AuthRepository.registerWithEmailAndPassword(name, email, password, new CustomCallback() {
                    @Override
                    public void onSuccess() {
                        loginWrapper(email, password);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }

            loginWrapper(email, password);
        });

        // Google Authentication
        authLoginGoogle.setOnClickListener(v -> {
            AuthRepository.loginWithGoogle(context, new CustomCallback() {
                @Override
                public void onSuccess() {
                    startActivity(new Intent(context, MainActivity.class));
                }

                @Override
                public void onError(String error) {
                    // It might not be obvious, but we need to put this in UI Thread
                    // since google oauth is configured to run async
                    runOnUiThread(() -> {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });

        // Change Mode between Registering or Log-in
        authBottomLink.setOnClickListener(v -> {
            isRegistering = !isRegistering;

            if (isRegistering) {
                authNameContainer.setVisibility(LinearLayout.VISIBLE);
                authTitle.setText("Daftar");
                authSubtitle.setText("Buat akun baru Anda dan mulai mencari hal baru!");
                authMainButton.setText("Daftar");
                authBottomText.setText("Sudah Punya Akun?");
                authBottomLink.setText("Masuk");
                return;
            }

            authNameContainer.setVisibility(LinearLayout.GONE);
            authTitle.setText("Masuk");
            authSubtitle.setText("Masuk ke akun Anda untuk melanjutkan aktivitas");
            authMainButton.setText("Masuk");
            authBottomText.setText("Belum Punya Akun?");
            authBottomLink.setText("Daftar");
        });
    }

    // Wrapper to prevent code duplication
    private void loginWrapper(String email, String password) {
        AuthRepository.loginWithEmailAndPassword(email, password, new CustomCallback() {
            @Override
            public void onSuccess() {
                startActivity(new Intent(context, MainActivity.class));
                finish();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}