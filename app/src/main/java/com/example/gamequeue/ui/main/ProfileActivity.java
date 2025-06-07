package com.example.gamequeue.ui.main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamequeue.R;

public class ProfileActivity extends AppCompatActivity {
    // Variables
    private LinearLayout editProfileContainer;
    private ImageView backBtn, profileImg;
    private EditText usernameField, emailField, passwordField, confirmPasswordField;
    private Button editBtn, saveBtn, logoutBtn;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        // TODO: Implement Actual User Fetch later
        usernameField.setText("Wedanta");
        emailField.setText("Wedanta@gmail.com");
    }

    private void setupListeners() {
        backBtn.setOnClickListener(v -> finish());

        editBtn.setOnClickListener(v -> {
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
        });

        logoutBtn.setOnClickListener(v -> {
            // TODO: Implement logout after login-flow done
            finish();
        });
    }
}