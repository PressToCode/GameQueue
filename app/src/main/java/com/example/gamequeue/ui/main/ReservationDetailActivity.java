package com.example.gamequeue.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamequeue.R;
import com.example.gamequeue.data.firebase.FirebaseUtil;
import com.example.gamequeue.data.model.ReservationModel;
import com.example.gamequeue.data.repository.DatabaseRepository;
import com.example.gamequeue.utils.CustomCallbackWithType;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ReservationDetailActivity extends AppCompatActivity {

    // UI Components
    private ImageView statusIcon;
    private TextView statusTitle, verificationCode, confirmationTime, reservationId, reservationPlace, reservationDate, reservationTime, rentalLimit, infoText;
    private LinearLayout additionalInfoContainer, rentalLimitContainer;
    private Button backButton;

    // Data variables
    private ReservationModel reservation;
    private String reservationStatus = "PENDING"; // PENDING, APPROVED, REJECTED
    private CountDownTimer countDownTimer;
    private Context context = this;
    private ValueEventListener statusListener;

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

        // Guard - Must have id passed along
        if (getIntent().getStringExtra("id") == null || getIntent().getStringExtra("console_name") == null) {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initialization
        initializeViews();

        // Fetch Data
        loadData();

        // Setup Listener
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

    private void loadData() {
        // Not null - Guard would've caught it
        String reservationId = getIntent().getStringExtra("id");

        // Fetch data from database
        DatabaseRepository.getUserReservationById(reservationId, null, new CustomCallbackWithType<>() {
            @Override
            public void onSuccess(ReservationModel reservationModel) {
                reservation = reservationModel;
                reservation.setId(reservationId);
                setupUI();
                setupUIBasedOnStatus();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }
        });

        // Initial
        if (reservationStatus == null) reservationStatus = "PENDING";
    }

    private void setupUI() {
        // Set Data
        attachStatusListener(reservation.getId());

        reservationId.setText(reservation.getId());
        reservationPlace.setText(getIntent().getStringExtra("console_name"));
        reservationDate.setText(reservation.getDate());
        reservationTime.setText(reservation.getTime() + " WIB");
        verificationCode.setText(reservation.getVerificationCode());

        // Date Formatter - Assuming Rental Time is 1 hour
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        rentalLimit.setText(LocalTime.parse(reservation.getTime(), formatter).plusHours(1).toString());
    }

    private void attachStatusListener(String id) {
        statusListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reservationStatus = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                reservationStatus = "PENDING";
                Toast.makeText(context, "Listener Went Wrong..", Toast.LENGTH_SHORT).show();
            }
        };

        FirebaseUtil.getReservationsRef().child(FirebaseUtil.getAuth().getCurrentUser().getUid()).child(id).child("status").addValueEventListener(statusListener);
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

        if (statusListener != null) {
            FirebaseUtil.getReservationsRef().child(FirebaseUtil.getAuth().getCurrentUser().getUid()).child(reservation.getId()).removeEventListener(statusListener);
        }
    }
}