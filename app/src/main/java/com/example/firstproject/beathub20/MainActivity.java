package com.example.firstproject.beathub20;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView loginId , register , skips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loginId = findViewById(R.id.loginInid);
        register = findViewById(R.id.registerid);
        skips = findViewById(R.id.skipsid);

        loginId.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this , loginpage.class);
            startActivity(i);
        });

        register.setOnClickListener(v -> {
           Intent i = new Intent(MainActivity.this , registerpage.class);
           startActivity(i);
        });
        skips.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this , HomePage.class);
            startActivity(i);
        });


    }
}