package com.example.madprj;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        int waterdrunk=0;
        SharedPreferences waterdrunkdata = getSharedPreferences("waterdrunkdata", MODE_PRIVATE);
        SharedPreferences.Editor editor = waterdrunkdata.edit();
        editor.putInt("waterdrunk", waterdrunk);
        editor.apply();


        // Find the "Get Started" button
        Button btnGetStarted = findViewById(R.id.btnGetStarted);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
            }
        }

        // Set click listener to open TrackCaloriesActivity
        btnGetStarted.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TrackCaloriesActivity.class);
            startActivity(intent);
        });
    }
}
