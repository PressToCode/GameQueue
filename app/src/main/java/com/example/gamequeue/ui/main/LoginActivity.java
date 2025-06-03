package com.example.gamequeue.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.gamequeue.R;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_login);

        // Mencari tombol login di layout
        Button login = findViewById(R.id.btn_login);

        // Menambahkan event listener untuk tombol login
        login.setOnClickListener(v -> {
            // Mengarahkan pengguna ke MainActivity setelah tombol login diklik
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Menutup LoginActivity agar tidak bisa kembali ke halaman login
        });
    }
}
