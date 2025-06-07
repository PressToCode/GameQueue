package com.example.gamequeue.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamequeue.R;

public class AuthActivity extends AppCompatActivity {
    // Variables
    private LinearLayout authNameContainer;
    private TextView authTitle, authSubtitle, authNameField, authEmailField, authPasswordField, authBottomText, authBottomLink;
    private Button authMainButton, authLoginGoogle;
    private boolean isRegistering = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

        // Setup Listener
        setupListeners();
    }

    private void setupListeners() {
        authMainButton.setOnClickListener(v -> {
            // TODO: Add Authentication Logic from Auth Repository + callback
            startActivity(new Intent(this, MainActivity.class));

//            if (isRegistering) {
//                // TODO: Add Register Logic from Auth Repository + callback
//            } else {
//                // TODO: Add Login Logic from Auth Repository + callback
//            }
        });

        authLoginGoogle.setOnClickListener(v -> {
            // TODO: Add Authentication Logic from Auth Repository + callback
            startActivity(new Intent(this, MainActivity.class));
        });

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
}