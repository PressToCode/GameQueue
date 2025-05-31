package com.example.gamequeue.ui.main;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamequeue.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    // Variables
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabReservasi;

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
//        setup();
    }

    private void setup() {
        fabReservasi.setOnClickListener(v -> {
            bottomNavigationView.setSelectedItemId(R.id.nav_reservation);
        });
    }
}