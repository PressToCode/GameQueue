package com.example.gamequeue.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamequeue.R;
import com.example.gamequeue.data.repository.AuthRepository;

public class EditProfileActivity extends AppCompatActivity {
    // Variables
    private ImageView backButton, profileImage;
    private EditText usernameField, emailField, passwordField, confirmPasswordField;
    private Button saveButton;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize repository
        authRepository = new AuthRepository(this);

        // Initialize views
        initViews();

        // Setup listeners
        setupListeners();

        // Load existing data
        loadExistingData();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        profileImage = findViewById(R.id.profileImage);
        usernameField = findViewById(R.id.usernameField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        confirmPasswordField = findViewById(R.id.confirmPasswordField);
        saveButton = findViewById(R.id.saveButton);
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());

        saveButton.setOnClickListener(v -> saveProfile());
    }

    private void loadExistingData() {
        Intent intent = getIntent();
        if (intent != null) {
            String userName = intent.getStringExtra("userName");
            String userEmail = intent.getStringExtra("userEmail");

            if (userName != null) usernameField.setText(userName);
            if (userEmail != null) emailField.setText(userEmail);
        }
    }

    private void saveProfile() {
        String username = usernameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(username)) {
            usernameField.setError("Username tidak boleh kosong");
            usernameField.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailField.setError("Email tidak boleh kosong");
            emailField.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Format email tidak valid");
            emailField.requestFocus();
            return;
        }

        // Password validation (only if user wants to change password)
        if (!TextUtils.isEmpty(password)) {
            if (password.length() < 6) {
                passwordField.setError("Password minimal 6 karakter");
                passwordField.requestFocus();
                return;
            }

            if (!password.equals(confirmPassword)) {
                confirmPasswordField.setError("Konfirmasi password tidak sesuai");
                confirmPasswordField.requestFocus();
                return;
            }
        }

        // Save logic here (SharedPreferences, Firebase, etc.)
        saveToStorage(username, email, password);
    }

    private void saveToStorage(String username, String email, String password) {
        // Update user profile
        authRepository.updateUserProfile(username, email, new AuthRepository.UpdateProfileCallback() {
            @Override
            public void onSuccess() {
                // If password is provided, update it
                if (!TextUtils.isEmpty(password)) {
                    authRepository.updatePassword(password, new AuthRepository.UpdatePasswordCallback() {
                        @Override
                        public void onSuccess() {
                            runOnUiThread(() -> {
                                Toast.makeText(EditProfileActivity.this, "Profile dan password berhasil diperbarui", Toast.LENGTH_SHORT).show();
                                finishWithResult(username, email);
                            });
                        }

                        @Override
                        public void onError(String error) {
                            runOnUiThread(() -> {
                                Toast.makeText(EditProfileActivity.this, "Profile berhasil diperbarui, tapi gagal update password: " + error, Toast.LENGTH_LONG).show();
                                finishWithResult(username, email);
                            });
                        }
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(EditProfileActivity.this, "Profile berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        finishWithResult(username, email);
                    });
                }
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(EditProfileActivity.this, "Gagal memperbarui profile: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void finishWithResult(String username, String email) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("userName", username);
        resultIntent.putExtra("userEmail", email);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}