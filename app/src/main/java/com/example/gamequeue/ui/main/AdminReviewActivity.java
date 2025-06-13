package com.example.gamequeue.ui.main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamequeue.R;
import com.example.gamequeue.data.repository.DatabaseRepository;
import com.example.gamequeue.utils.CustomCallback;
import com.example.gamequeue.utils.WidgetModifier;

public class AdminReviewActivity extends AppCompatActivity {
    // Variables
    private ImageButton backButton;
    private Button allowButton, rejectButton;
    private TextView username, status, email, phone, date, time, consoleName, specificationOne, specificationTwo, specificationThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_review);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialization
        backButton = findViewById(R.id.adminReviewBackButton);
        allowButton = findViewById(R.id.adminReviewAllowButton);
        rejectButton = findViewById(R.id.adminReviewRejectButton);
        username = findViewById(R.id.adminReviewUsername);
        status = findViewById(R.id.adminReviewStatus);
        email = findViewById(R.id.adminReviewEmail);
        phone = findViewById(R.id.adminReviewPhone);
        date = findViewById(R.id.adminReviewDate);
        time = findViewById(R.id.adminReviewTime);
        consoleName = findViewById(R.id.adminReviewConsoleName);
        specificationOne = findViewById(R.id.adminReviewSpecificationOne);
        specificationTwo = findViewById(R.id.adminReviewSpecificationTwo);
        specificationThree = findViewById(R.id.adminReviewSpecificationThree);

        // Load Data and Change UI
        loadData();

        // Add Listeners
        setupListeners();
    }

    private void loadData() {
        Bundle extras = getIntent().getExtras();

        // Avoid Non-complete Information or Data
        if (extras == null) { finish(); }

        username.setText(extras.getString("username"));
        status.setText(extras.getString("status"));
        email.setText(extras.getString("email"));
        phone.setText(extras.getString("phone"));
        date.setText(WidgetModifier.convertDateToIndonesianLocale(extras.getString("date"), 1));
        time.setText(extras.getString("time") + " WIB");
        consoleName.setText(extras.getString("consoleName"));
        specificationOne.setText(extras.getString("consoleSpecOne"));
        specificationTwo.setText(extras.getString("consoleSpecTwo"));
        specificationThree.setText(extras.getString("consoleSpecThree"));

        WidgetModifier.statusChanger(status);
    }

    private void setupListeners() {
        String userId, reservationId, consoleId;
        userId = getIntent().getExtras().getString("userId");
        reservationId = getIntent().getExtras().getString("reservationId");
        consoleId = getIntent().getExtras().getString("consoleId");

        // Back Button
        backButton.setOnClickListener(v -> finish());

        // Accept Reservation Request
        allowButton.setOnClickListener(v -> {
            DatabaseRepository.updateReservationRequest(true, reservationId, userId, consoleId);
            finish();
        });

        // Reject Reservation Request
        rejectButton.setOnClickListener(v -> {
            DatabaseRepository.updateReservationRequest(false, reservationId, userId, consoleId);
            finish();
        });
    }
}