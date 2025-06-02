package com.example.gamequeue.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamequeue.R;
import com.example.gamequeue.data.repository.AuthRepository;

public class UserProfileActivity extends AppCompatActivity {
    // Variables
    private ImageView backButton, profileImage;
    private TextView userName, userEmail;
    private Button editButton, logoutButton;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);

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

        // Load user data
        loadUserData();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        profileImage = findViewById(R.id.profileImage);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        editButton = findViewById(R.id.editButton);
        logoutButton = findViewById(R.id.logoutButton);
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());

        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra("userName", userName.getText().toString());
            intent.putExtra("userEmail", userEmail.getText().toString());
            startActivityForResult(intent, 100);
        });

        logoutButton.setOnClickListener(v -> {
            // Implement logout logic here
            // For now, just finish the activity
            showLogoutDialog();
        });
    }

    private void loadUserData() {
        // Load user data from repository
        String savedUserName = authRepository.getUserName();
        String savedUserEmail = authRepository.getUserEmail();

        if (!savedUserName.isEmpty()) {
            userName.setText(savedUserName);
        } else {
            userName.setText("Olfat"); // Default fallback
        }

        if (!savedUserEmail.isEmpty()) {
            userEmail.setText(savedUserEmail);
        } else {
            userEmail.setText("Olfatfalz@natha.com"); // Default fallback
        }
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Apakah Anda yakin ingin logout?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    authRepository.logout();
                    // Navigate to login activity or finish
                    // For now, just finish the activity
                    finish();
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Refresh user data if edited
            if (data != null) {
                String newUserName = data.getStringExtra("userName");
                String newUserEmail = data.getStringExtra("userEmail");
                if (newUserName != null) userName.setText(newUserName);
                if (newUserEmail != null) userEmail.setText(newUserEmail);
            }
        }
    }
}