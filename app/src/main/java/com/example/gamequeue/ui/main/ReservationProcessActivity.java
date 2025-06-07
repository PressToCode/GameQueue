package com.example.gamequeue.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.badoualy.stepperindicator.StepperIndicator;
import com.example.gamequeue.R;
import com.example.gamequeue.data.model.ReservationSharedViewModel;
import com.example.gamequeue.ui.adapter.ReservationPagerAdapter;
import com.example.gamequeue.widgets.NonSwipeableViewPager;

public class ReservationProcessActivity extends AppCompatActivity {
    // Variables
    private ImageButton backBtn;
    private Button continueBtn;
    private StepperIndicator indicator;
    private NonSwipeableViewPager viewPager;
    private ReservationPagerAdapter pagerAdapter;
    private ReservationSharedViewModel sharedViewModel;

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
        pagerAdapter = new ReservationPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        indicator = findViewById(R.id.reservationProcessIndicator);
        indicator.setViewPager(viewPager);
        sharedViewModel = new ViewModelProvider(this).get(ReservationSharedViewModel.class);

        // Observe Change
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                observeCurrentFragmentValidity(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        // Set OnClick Listener
        buttonSetup();
        observeCurrentFragmentValidity(viewPager.getCurrentItem());
    }

    private void buttonSetup() {
        // TODO: Replace with fragment navigation and check before removing activity
        backBtn.setOnClickListener(v -> finish());

        continueBtn.setOnClickListener(v -> {
            int currentStep = indicator.getCurrentStep();

            if(currentStep != indicator.getStepCount() - 1) {
                indicator.setCurrentStep(currentStep + 1);
                viewPager.setCurrentItem(currentStep + 1);
                return;
            }

            // TODO: Submit Form data & Go To Status Page
            finish();
        });

        // Only Allow going back
        indicator.addOnStepClickListener(step -> {
            if (step <= indicator.getCurrentStep()) {
                indicator.setCurrentStep(step);
                viewPager.setCurrentItem(step, true);
            }
        });
    }

    private void observeCurrentFragmentValidity(int position) {
        LiveData<Boolean> currentFormValidity = null;
        switch (position) {
            case 0:
                currentFormValidity = sharedViewModel.getFormOneFilled();
                break;
            case 1:
                currentFormValidity = sharedViewModel.getFormTwoFilled();
                break;
            case 2:
                currentFormValidity = sharedViewModel.getFormThreeFilled();
                break;
            default:
                break;
        }

        if(currentFormValidity != null) {
            currentFormValidity.observe(this, isValid -> {
                if(viewPager.getCurrentItem() == position) {
                    continueBtn.setEnabled(isValid);
                }
            });
        } else {
            continueBtn.setEnabled(false);
        }
    }
}