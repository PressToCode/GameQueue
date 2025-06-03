package com.example.gamequeue.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.gamequeue.R;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_register);

        // Mencari tombol login di layout
        Button login = findViewById(R.id.btn_register);

        // Menambahkan event listener untuk tombol login
        login.setOnClickListener(v -> {
            // Mengarahkan pengguna ke MainActivity setelah tombol login diklik
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Menutup LoginActivity agar tidak bisa kembali ke halaman login
        });
    }
}
