package com.example.gamequeue.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamequeue.R;
import com.example.gamequeue.data.model.SharedProfileModel;
import com.example.gamequeue.data.repository.AuthRepository;
import com.example.gamequeue.utils.ApplicationContext;
import com.example.gamequeue.utils.CustomCallback;

public class ProfileActivity extends AppCompatActivity {
    // Variables
    private LinearLayout editProfileContainer;
    private ImageView backBtn, profileImg;
    private EditText usernameField, emailField, oldPasswordField, passwordField, confirmPasswordField;
    private Button editBtn, saveBtn, logoutBtn;
    private boolean isEdit = false;
    private boolean skipFetch = true;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Auth Check before Rendering - Unless Dev Mode
        if(AuthRepository.isLoggedIn()) {
            skipFetch = ApplicationContext.getDevMode();
        } else {
            finish();
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialization
        editProfileContainer = findViewById(R.id.changeProfileContainer);
        backBtn = findViewById(R.id.backButton);
        profileImg = findViewById(R.id.profileImage);
        usernameField = findViewById(R.id.usernameField);
        emailField = findViewById(R.id.emailField);
        oldPasswordField = findViewById(R.id.oldPassword);
        passwordField = findViewById(R.id.passwordField);
        confirmPasswordField = findViewById(R.id.confirmPasswordField);
        editBtn = findViewById(R.id.editProfileButton);
        saveBtn = findViewById(R.id.saveProfileButton);
        logoutBtn = findViewById(R.id.logoutButton);

        // Setup default UI values
        setupView();

        // Setup Listener
        setupListeners();
    }

    private void setupView() {
        // TODO: REMOVE ON PRODUCTION
        // Skip if in dev mode
        if(skipFetch) {
            usernameField.setText("[Dev Mode] My Name");
            emailField.setText("[Dev Mode] My Email");
            return;
        }

        usernameField.setText(SharedProfileModel.getName());
        emailField.setText(SharedProfileModel.getEmail());
        profileImg.setImageURI(SharedProfileModel.getProfileImageUrl());
    }

    private void setupListeners() {
        // Go Back
        backBtn.setOnClickListener(v -> finish());

        // Change UI to edit or normal
        editBtn.setOnClickListener(v -> {
            changeUiMode();
        });

        // Saving Changes
        saveBtn.setOnClickListener(v -> {
            String name, email, oldPassword, newPassword;
            name = usernameField.getText().toString();
            email = emailField.getText().toString();
            oldPassword = oldPasswordField.getText().toString();
            newPassword = passwordField.getText().toString();

            Boolean canSubmit = true;

            if(name.isEmpty()) {
                usernameField.setError("Nama tidak boleh kosong");
                canSubmit = false;
            }

            if(email.isEmpty()) {
                emailField.setError("Email tidak boleh kosong");
                canSubmit = false;
            }

            if(oldPassword.isEmpty()) {
                oldPasswordField.setError("Password lama tidak boleh kosong");
                canSubmit = false;
            }

            if(newPassword.isEmpty()) {
                passwordField.setError("Password baru tidak boleh kosong");
                canSubmit = false;
            }

            if(!newPassword.equals(confirmPasswordField.getText().toString())) {
                passwordField.setError("Password baru tidak sama");
                confirmPasswordField.setError("Password baru tidak sama");
                canSubmit = false;
            }

            if(canSubmit) {
                AuthRepository.updateProfile(name, email, oldPassword, newPassword, new CustomCallback() {
                    @Override
                    public void onSuccess() {
                        SharedProfileModel.removeAll();
                        SharedProfileModel.setAll();
                        setupView();
                        changeUiMode();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

        // Logout
        logoutBtn.setOnClickListener(v -> {
            AuthRepository.logout(new CustomCallback() {
                @Override
                public void onSuccess() {
                    startActivity(new Intent(context, AuthActivity.class));
                    finish();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void changeUiMode() {
        isEdit = !isEdit;
        if(isEdit) {
            editBtn.setText("Batal");
            editBtn.setBackgroundResource(R.drawable.red_button_background);
            editProfileContainer.setVisibility(LinearLayout.VISIBLE);
            usernameField.setEnabled(true);
            emailField.setEnabled(true);
            logoutBtn.setVisibility(Button.GONE);
            return;
        }

        editBtn.setText("Ubah Email & Password");
        editBtn.setBackgroundResource(R.drawable.blue_button_background);
        editProfileContainer.setVisibility(LinearLayout.GONE);
        usernameField.setEnabled(false);
        emailField.setEnabled(false);
        logoutBtn.setVisibility(Button.VISIBLE);
    }
}