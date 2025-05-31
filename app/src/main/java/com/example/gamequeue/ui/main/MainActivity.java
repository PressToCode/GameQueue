package com.example.gamequeue.ui.main;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
    private final int NAV_HOME = R.id.nav_home;
    private final int NAV_RESERVATION = R.id.nav_reservation;
    private final int NAV_STATUS = R.id.nav_status;

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
        setup();
    }

    private void setup() {
        // Set Default Fragment
        loadFragment(new HomeFragment());

        // Set FAB Button Listener
        fabReservasi.setOnClickListener(v -> {
            bottomNavigationView.setSelectedItemId(NAV_RESERVATION);
            loadFragment(new HomeFragment());
        });

        // Set Bottom Navigation View Listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == NAV_HOME) {
                loadFragment(new HomeFragment());
                return true;
            } else if(item.getItemId() == NAV_RESERVATION) {
                loadFragment(new ReservationFragment());
                return true;
            } else if(item.getItemId() == NAV_STATUS) {
                loadFragment(new StatusFragment());
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}