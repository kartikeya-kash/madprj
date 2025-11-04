package com.example.madprj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TrackCaloriesActivity extends AppCompatActivity {

    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_calories); // your XML file

        btnNext = findViewById(R.id.btn_next);

        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(TrackCaloriesActivity.this, LogSleepActivity.class);
            startActivity(intent);
        });
    }
}

