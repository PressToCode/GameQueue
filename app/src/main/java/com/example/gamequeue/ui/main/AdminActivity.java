package com.example.gamequeue.ui.main;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.gamequeue.R;
import com.example.gamequeue.data.sharedViewModel.ConsoleSharedViewModel;
import com.example.gamequeue.data.sharedViewModel.RequestSharedViewModel;
import com.example.gamequeue.data.repository.AuthRepository;
import com.example.gamequeue.ui.fragment.AdminHomeFragment;
import com.example.gamequeue.ui.fragment.ReservationFragment;
import com.example.gamequeue.utils.ApplicationContext;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminActivity extends AppCompatActivity {
    // Variables
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabReservasi;
    private ProgressBar loadingIndicator;
    private ConsoleSharedViewModel consoleSharedViewModel;
    private RequestSharedViewModel requestSharedViewModel;

    // Tags
    private static final String TAG_HOME = "home_fragment";
    private static final String TAG_CONSOLES = "console_fragment";

    // Fragments
    private final AdminHomeFragment homeFragment = new AdminHomeFragment();
    private final ReservationFragment consolesFragment = new ReservationFragment();
    private Fragment activeFragment = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Guard
        // Make sure that it is Logged in and IS an admin
        if(!AuthRepository.isLoggedIn() || !ApplicationContext.getAdminMode()) {
            finish();
        }

        // Initialization
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        loadingIndicator = findViewById(R.id.loading_indicator);
        fabReservasi = findViewById(R.id.fab_reservasi);

        // Remove Fab Reservasi
        fabReservasi.setVisibility(android.view.View.GONE);

        // Change BottomNav Menu
        bottomNavigationView.getMenu().clear();
        bottomNavigationView.inflateMenu(R.menu.admin_bottom_nav_menu);

        // Import Consoles Data and Request Data
        consoleSharedViewModel = new ViewModelProvider(this).get(ConsoleSharedViewModel.class);
        requestSharedViewModel = new ViewModelProvider(this).get(RequestSharedViewModel.class);

        // Setup Fragments
        setupFragments(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check Auth Status
        if(!AuthRepository.isLoggedIn() || !ApplicationContext.getAdminMode()) {
            finish();
        }
    }

    private void setupFragments(Bundle savedInstanceState) {
        consoleSharedViewModel.getConsoleListLive().observe(this, consoleModels -> {
            if(consoleModels == null || consoleModels.isEmpty()) {
                return;
            }

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

            // Add ConsoleFragment (and hide it)
            if (fm.findFragmentByTag(TAG_CONSOLES) == null) {
                ft.add(R.id.fragment_container, consolesFragment, TAG_CONSOLES).hide(consolesFragment);
            }

            // Determine which fragment to show
            if (activeTagRestored != null) {
                if (activeTagRestored.equals(TAG_HOME)) activeFragment = homeFragment;
                else if (activeTagRestored.equals(TAG_CONSOLES)) activeFragment = consolesFragment;
                else activeFragment = homeFragment;
            } else {
                activeFragment = homeFragment;
            }

            // Hide all fragments first to handle cases where multiple might have been visible
            // due to incorrect previous state or prior to this logic being implemented.
            // This is a defensive measure.
            if (homeFragment.isAdded()) ft.hide(homeFragment);
            if (consolesFragment.isAdded()) ft.hide(consolesFragment);

            // Then show the active one
            ft.show(activeFragment);
            ft.commit();

            // Update BottomNavigationView selection based on the active fragment
            if (activeFragment == homeFragment) {
                bottomNavigationView.setSelectedItemId(R.id.nav_home_admin);
            } else if (activeFragment == consolesFragment) {
                bottomNavigationView.setSelectedItemId(R.id.nav_console_admin);
            } else {
                bottomNavigationView.setSelectedItemId(R.id.nav_home_admin);
            }

            // Setup Listener
            setupListeners();

            // Remove Loading
            loadingIndicator.setVisibility(android.view.View.GONE);

            // Remove Observer
            consoleSharedViewModel.getConsoleListLive().removeObservers(this);
        });
    }

    private void setupListeners() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            String selectedTag = null;

            if (item.getItemId() == R.id.nav_home_admin) {
                selectedFragment = homeFragment;
                selectedTag = TAG_HOME;
            } else if (item.getItemId() == R.id.nav_console_admin) {
                selectedFragment = consolesFragment;
                selectedTag = TAG_CONSOLES;
            }

            if (selectedFragment != null && selectedFragment != activeFragment) {
                switchFragment(selectedFragment);
            }
            return true;
        });
    }

    private void switchFragment(Fragment targetFragment) {
        if (targetFragment == activeFragment) return;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (activeFragment != null && activeFragment.isAdded()) {
            ft.hide(activeFragment);
        }

        if (targetFragment.isAdded()) {
            ft.show(targetFragment);
        } else {
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
        } else if (activeFragment == homeFragment) {
            outState.putString("active_fragment_tag", TAG_HOME);
        }
    }
}