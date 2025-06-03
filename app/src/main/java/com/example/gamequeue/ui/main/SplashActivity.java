package com.example.gamequeue.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.gamequeue.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    // Variables
    private ImageView logo;
    private TextView textGame, textQueue;
    private Animation rotate, fadein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // Initialization
        logo = findViewById(R.id.splashLogo);
        textGame = findViewById(R.id.txtGame);
        textQueue = findViewById(R.id.txtQueue);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(rotate);
        textGame.startAnimation(fadein);
        textQueue.startAnimation(fadein);

        // Async to Login Activity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(this, LoginActivity.class));
            overridePendingTransition(0, 0);
            finish();
        }, 1700);
    }
}