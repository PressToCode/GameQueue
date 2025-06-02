package com.example.gamequeue.ui.main;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gamequeue.R;
import com.example.gamequeue.ui.fragment.HomeFragment;
import com.example.gamequeue.ui.fragment.ReservationFragment;
import com.example.gamequeue.ui.fragment.StatusFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    // Variables
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabReservasi;
    private static final String TAG_HOME = "home_fragment";
    private static final String TAG_RESERVATION = "reservation_fragment";
    private static final String TAG_STATUS = "status_fragment";
    private final HomeFragment homeFragment = new HomeFragment();
    private final ReservationFragment reservationFragment = new ReservationFragment();
    private final StatusFragment statusFragment = new StatusFragment();
    private Fragment activeFragment = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, 0);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialization
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fabReservasi = findViewById(R.id.fab_reservasi);

        // Setup all fragments and listener
        setupFragments(savedInstanceState);
        setupListeners();
    }

    private void setupFragments(Bundle savedInstanceState) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // Restore active fragment tag if available
        String activeTagRestored = null;
        if (savedInstanceState != null) {
            activeTagRestored = savedInstanceState.getString("active_fragment_tag");
        }

        // Add HomeFragment
        if (fm.findFragmentByTag(TAG_HOME) == null) {
            ft.add(R.id.fragment_container, homeFragment, TAG_HOME);
        }
        // Add ReservationFragment (and hide it)
        if (fm.findFragmentByTag(TAG_RESERVATION) == null) {
            ft.add(R.id.fragment_container, reservationFragment, TAG_RESERVATION).hide(reservationFragment);
        }
        // Add StatusFragment (and hide it)
        if (fm.findFragmentByTag(TAG_STATUS) == null) {
            ft.add(R.id.fragment_container, statusFragment, TAG_STATUS).hide(statusFragment);
        }

        // Determine which fragment to show
        if (activeTagRestored != null) {
            if (activeTagRestored.equals(TAG_RESERVATION)) activeFragment = reservationFragment;
            else if (activeTagRestored.equals(TAG_STATUS)) activeFragment = statusFragment;
            else activeFragment = homeFragment; // Default or TAG_HOME
        } else {
            activeFragment = homeFragment; // Default on first creation
        }

        // Hide all fragments first to handle cases where multiple might have been visible
        // due to incorrect previous state or prior to this logic being implemented.
        // This is a defensive measure.
        if (homeFragment.isAdded()) ft.hide(homeFragment);
        if (reservationFragment.isAdded()) ft.hide(reservationFragment);
        if (statusFragment.isAdded()) ft.hide(statusFragment);

        // Then show the active one
        ft.show(activeFragment);
        ft.commit();

        // Update BottomNavigationView selection based on the active fragment
        if (activeFragment == reservationFragment) {
            bottomNavigationView.setSelectedItemId(R.id.nav_reservation);
        } else if (activeFragment == statusFragment) {
            bottomNavigationView.setSelectedItemId(R.id.nav_status);
        } else { // homeFragment is active
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    private void setupListeners() {
        fabReservasi.setOnClickListener(v -> {
            // Simply select the item, the listener will handle the fragment switch
            bottomNavigationView.setSelectedItemId(R.id.nav_reservation);
            // NO need to call switchFragment or loadFragment here directly
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            String selectedTag = null; // For consistency, though not strictly used in this switchFragment

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = homeFragment;
                selectedTag = TAG_HOME;
            } else if (item.getItemId() == R.id.nav_reservation) {
                selectedFragment = reservationFragment;
                selectedTag = TAG_RESERVATION;
            } else if (item.getItemId() == R.id.nav_status) {
                selectedFragment = statusFragment;
                selectedTag = TAG_STATUS;
            }

            if (selectedFragment != null && selectedFragment != activeFragment) {
                switchFragment(selectedFragment); // Pass the selected fragment instance
            }
            return true; // Return true to display the item as selected
        });
    }

    // --- MODIFIED loadFragment to switchFragment using show/hide ---
    private void switchFragment(Fragment targetFragment) {
        if (targetFragment == activeFragment) return; // No action if already active

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // Optional: Add animations
        // ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

        if (activeFragment != null && activeFragment.isAdded()) {
            ft.hide(activeFragment);
        }

        // The fragments should have been added in setupFragments.
        // If not, this is a problem, but typically show expects it to be added.
        if (targetFragment.isAdded()) {
            ft.show(targetFragment);
        } else {
            // This case should ideally not happen if setupFragments is correct
            // For robustness, you could add it here, but it implies an issue in initial setup.
            // String tag = "";
            // if (targetFragment == homeFragment) tag = TAG_HOME;
            // else if (targetFragment == reservationFragment) tag = TAG_RESERVATION;
            // else if (targetFragment == statusFragment) tag = TAG_STATUS;
            // ft.add(R.id.fragment_container, targetFragment, tag).show(targetFragment);
            // Fallback or error handling
            return;
        }

        ft.setPrimaryNavigationFragment(targetFragment);
        ft.setReorderingAllowed(true);
        ft.commit();

        activeFragment = targetFragment;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (activeFragment != null && activeFragment.getTag() != null) {
            outState.putString("active_fragment_tag", activeFragment.getTag());
        } else if (activeFragment == homeFragment) { // Fallback if tag was not set/found
            outState.putString("active_fragment_tag", TAG_HOME);
        } // Add similar fallbacks for other fragments if needed
    }
}