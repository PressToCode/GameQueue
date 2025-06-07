package com.example.gamequeue.ui.main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.badoualy.stepperindicator.StepperIndicator;
import com.example.gamequeue.R;
import com.example.gamequeue.ui.adapter.ReservationPagerAdapter;
import com.example.gamequeue.widgets.NonSwipeableViewPager;

public class ReservationProcessActivity extends AppCompatActivity {
    // Variables
    private ImageButton backBtn;
    private Button continueBtn;
    private StepperIndicator indicator;
    private NonSwipeableViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reservation_process);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialization
        backBtn = findViewById(R.id.reservationProcessBackButton);
        continueBtn = findViewById(R.id.reservationProcessContinueButton);
        viewPager = findViewById(R.id.reservationProcessViewPager);
        viewPager.setAdapter(new ReservationPagerAdapter(getSupportFragmentManager()));
        indicator = findViewById(R.id.reservationProcessIndicator);
        indicator.setViewPager(viewPager);

        // Set OnClick Listener
        buttonSetup();
    }

    private void buttonSetup() {
        // TODO: Replace with fragment navigation and check before removing activity
        backBtn.setOnClickListener(v -> finish());

        continueBtn.setOnClickListener(v -> {
            int currentStep = indicator.getCurrentStep();

            if(currentStep != indicator.getStepCount()) {
                indicator.setCurrentStep(currentStep + 1);
                viewPager.setCurrentItem(currentStep + 1);
            }
        });

        indicator.addOnStepClickListener(step -> {
            indicator.setCurrentStep(step);
            viewPager.setCurrentItem(step, true);
        });
    }
}