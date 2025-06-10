package com.example.gamequeue.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamequeue.R;

public class ReservationDetailActivity extends AppCompatActivity {

    // UI Components
    private ImageView statusIcon;
    private TextView statusTitle;
    private LinearLayout additionalInfoContainer;
    private TextView verificationCode;
    private TextView confirmationTime;
    private TextView reservationId;
    private TextView reservationPlace;
    private TextView reservationDate;
    private TextView reservationTime;
    private LinearLayout rentalLimitContainer;
    private TextView rentalLimit;
    private TextView infoText;
    private Button backButton;

    // Data variables
    private String reservationStatus = "PENDING"; // PENDING, APPROVED, REJECTED
    private CountDownTimer countDownTimer;

    // Intent keys
    public static final String EXTRA_RESERVATION_ID = "reservation_id";
    public static final String EXTRA_RESERVATION_STATUS = "reservation_status";
    public static final String EXTRA_CONSOLE_NAME = "console_name";
    public static final String EXTRA_RESERVATION_DATE = "reservation_date";
    public static final String EXTRA_RESERVATION_TIME = "reservation_time";
    public static final String EXTRA_VERIFICATION_CODE = "verification_code";
    public static final String EXTRA_RENTAL_LIMIT = "rental_limit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reservation_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        initializeViews();

        // Get data from intent
        getIntentData();

        // Setup UI based on status
        setupUIBasedOnStatus();

        // Setup listeners
        setupListeners();
    }

    private void initializeViews() {
        statusIcon = findViewById(R.id.statusIcon);
        statusTitle = findViewById(R.id.statusTitle);
        additionalInfoContainer = findViewById(R.id.additionalInfoContainer);
        verificationCode = findViewById(R.id.verificationCode);
        confirmationTime = findViewById(R.id.confirmationTime);
        reservationId = findViewById(R.id.reservationId);
        reservationPlace = findViewById(R.id.reservationPlace);
        reservationDate = findViewById(R.id.reservationDate);
        reservationTime = findViewById(R.id.reservationTime);
        rentalLimitContainer = findViewById(R.id.rentalLimitContainer);
        rentalLimit = findViewById(R.id.rentalLimit);
        infoText = findViewById(R.id.infoText);
        backButton = findViewById(R.id.backButton);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            reservationStatus = intent.getStringExtra(EXTRA_RESERVATION_STATUS);
            if (reservationStatus == null) reservationStatus = "PENDING";

            String resId = intent.getStringExtra(EXTRA_RESERVATION_ID);
            String consoleName = intent.getStringExtra(EXTRA_CONSOLE_NAME);
            String resDate = intent.getStringExtra(EXTRA_RESERVATION_DATE);
            String resTime = intent.getStringExtra(EXTRA_RESERVATION_TIME);
            String verCode = intent.getStringExtra(EXTRA_VERIFICATION_CODE);
            String rentalLimitTime = intent.getStringExtra(EXTRA_RENTAL_LIMIT);

            // Set data with fallback values
            reservationId.setText(resId != null ? resId : "000085752257");
            reservationPlace.setText(consoleName != null ? consoleName : "Xbox III");
            reservationDate.setText(resDate != null ? resDate : "Mar 22, 2023");
            reservationTime.setText(resTime != null ? resTime : "07:30 AM");
            verificationCode.setText(verCode != null ? verCode : "5oLFt8");
            rentalLimit.setText(rentalLimitTime != null ? rentalLimitTime : "10:00 AM");
        } else {
            // Set default values
            reservationId.setText("000085752257");
            reservationPlace.setText("Xbox III");
            reservationDate.setText("Mar 22, 2023");
            reservationTime.setText("07:30 AM");
            verificationCode.setText("5oLFt8");
            rentalLimit.setText("10:00 AM");
        }
    }

    private void setupUIBasedOnStatus() {
        switch (reservationStatus.toUpperCase()) {
            case "PENDING":
                setupPendingStatus();
                break;
            case "APPROVED":
                setupApprovedStatus();
                break;
            case "REJECTED":
                setupRejectedStatus();
                break;
            default:
                setupPendingStatus();
                break;
        }
    }

    private void setupPendingStatus() {
        // Set status icon and title
        statusIcon.setImageResource(R.drawable.ic_confirm); // You'll need to create this
        statusTitle.setText("Reservasi Ditinjau");

        // Hide additional info
        additionalInfoContainer.setVisibility(View.GONE);
        rentalLimitContainer.setVisibility(View.GONE);

        // Set info text
        infoText.setText("Mohon ditunggu admin akan melakukan verifikasi permintaan");
        infoText.setBackgroundResource(R.drawable.info_text_background);
        infoText.getBackground().setTint(getResources().getColor(R.color.pastel_blue));
    }

    private void setupApprovedStatus() {
        // Set status icon and title
        statusIcon.setImageResource(R.drawable.ic_confirm);
        statusTitle.setText("Reservasi Disetujui");

        // Show additional info
        additionalInfoContainer.setVisibility(View.VISIBLE);
        rentalLimitContainer.setVisibility(View.VISIBLE);

        // Start countdown timer (example: 10 minutes)
        startConfirmationTimer(10 * 60 * 1000); // 10 minutes in milliseconds

        // Set info text
        infoText.setText("Tunjukkan kode verifikasi ini kepada admin untuk konfirmasi");
        infoText.setBackgroundResource(R.drawable.info_text_background);
        infoText.getBackground().setTint(getResources().getColor(R.color.light_green_bg));
    }

    private void setupRejectedStatus() {
        // Set status icon and title
        statusIcon.setImageResource(R.drawable.ic_rejected); // You'll need to create this
        statusTitle.setText("Reservasi Ditolak");

        // Hide additional info
        additionalInfoContainer.setVisibility(View.GONE);
        rentalLimitContainer.setVisibility(View.GONE);

        // Set info text
        infoText.setText("Maaf, reservasi Anda ditolak. Silakan coba lagi atau hubungi admin");
        infoText.setBackgroundResource(R.drawable.info_text_background);
        infoText.getBackground().setTint(getResources().getColor(R.color.pastel_red));
    }

    private void startConfirmationTimer(long milliseconds) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                String timeLeft = String.format("Konfirmasi dalam: %02d:%02d", minutes, seconds);
                confirmationTime.setText(timeLeft);
            }

            @Override
            public void onFinish() {
                confirmationTime.setText("Waktu konfirmasi habis");
                confirmationTime.setTextColor(getResources().getColor(R.color.blind_red));
            }
        };

        countDownTimer.start();
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    // Helper method to create intent for this activity
    public static Intent createIntent(android.content.Context context,
                                      String reservationId,
                                      String status,
                                      String consoleName,
                                      String date,
                                      String time,
                                      String verificationCode,
                                      String rentalLimit) {
        Intent intent = new Intent(context, ReservationDetailActivity.class);
        intent.putExtra(EXTRA_RESERVATION_ID, reservationId);
        intent.putExtra(EXTRA_RESERVATION_STATUS, status);
        intent.putExtra(EXTRA_CONSOLE_NAME, consoleName);
        intent.putExtra(EXTRA_RESERVATION_DATE, date);
        intent.putExtra(EXTRA_RESERVATION_TIME, time);
        intent.putExtra(EXTRA_VERIFICATION_CODE, verificationCode);
        intent.putExtra(EXTRA_RENTAL_LIMIT, rentalLimit);
        return intent;
    }
}