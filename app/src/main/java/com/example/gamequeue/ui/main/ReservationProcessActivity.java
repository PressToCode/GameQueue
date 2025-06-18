package com.example.gamequeue.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.badoualy.stepperindicator.StepperIndicator;
import com.example.gamequeue.R;
import com.example.gamequeue.data.sharedViewModel.ReservationFormSharedViewModel;
import com.example.gamequeue.data.repository.AuthRepository;
import com.example.gamequeue.data.repository.DatabaseRepository;
import com.example.gamequeue.ui.adapter.ReservationPagerAdapter;
import com.example.gamequeue.utils.ApplicationContext;
import com.example.gamequeue.utils.CustomCallbackWithType;
import com.example.gamequeue.widgets.NonSwipeableViewPager;

public class ReservationProcessActivity extends AppCompatActivity {
    // Variables
    private ImageButton backBtn;
    private Button continueBtn;
    private StepperIndicator indicator;
    private NonSwipeableViewPager viewPager;
    private ReservationPagerAdapter pagerAdapter;
    private ReservationFormSharedViewModel sharedViewModel;
    private Context context = this;

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

        // Auth Check before Rendering - Unless Dev Mode
        if(!AuthRepository.isLoggedIn()) {
            finish();
        }

        // Initialization
        backBtn = findViewById(R.id.reservationProcessBackButton);
        continueBtn = findViewById(R.id.reservationProcessContinueButton);
        viewPager = findViewById(R.id.reservationProcessViewPager);
        pagerAdapter = new ReservationPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        indicator = findViewById(R.id.reservationProcessIndicator);
        indicator.setViewPager(viewPager);
        sharedViewModel = new ViewModelProvider(this).get(ReservationFormSharedViewModel.class);

        // Observe Change
        pagerObserver();

        // Set OnClick Listener
        buttonSetup();
        observeCurrentFragmentValidity(viewPager.getCurrentItem());
    }

    private void pagerObserver() {
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
    }

    private void buttonSetup() {
        backBtn.setOnClickListener(v -> {
            // Invalidate Reservation Form First to avoid Accidental Data Passing
            invalidateForm();

            // Remove Activity
            finish();
        });

        continueBtn.setOnClickListener(v -> {
            int currentStep = indicator.getCurrentStep();

            if(currentStep != indicator.getStepCount() - 1) {
                indicator.setCurrentStep(currentStep + 1);
                viewPager.setCurrentItem(currentStep + 1);
                return;
            }

            // Prevent Admin from Submitting
            if (ApplicationContext.getAdminMode()) {
                finish();
                return;
            }

            // Last Check and Send
            // Note: Default Value is false so NO NULL POINTER EXCEPTION POSSIBLE
            if (sharedViewModel.getFormOneFilled().getValue()
                    && sharedViewModel.getFormTwoFilled().getValue()
                    && sharedViewModel.getFormThreeFilled().getValue()
                    && sharedViewModel.getReservationForm().getValue() != null) {
                // Submit Form to Database
                DatabaseRepository.submitForm(sharedViewModel.getReservationForm().getValue(), new CustomCallbackWithType<>() {
                    @Override
                    public void onSuccess(String reservationId) {
                        // Create Intent
                        Intent intent = new Intent(context, ReservationDetailActivity.class);
                        intent.putExtra("id", reservationId);
                        intent.putExtra("console_name", getIntent().getStringExtra("title"));

                        // Invalidate Form
                        invalidateForm();

                        // Start Detail Reservation - Fetch From Database in THERE
                        startActivity(intent);

                        // Remove this page
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
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

    private void invalidateForm() {
        sharedViewModel.setReservationForm(null);
        sharedViewModel.setFormOneFilled(false);
        sharedViewModel.setFormTwoFilled(false);
        sharedViewModel.setFormThreeFilled(false);
    }
}