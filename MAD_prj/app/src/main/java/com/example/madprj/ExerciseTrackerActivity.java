package com.example.madprj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class ExerciseTrackerActivity extends AppCompatActivity {

    private LinearLayout navHome, navActivity, navReports, navSOS, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_tracker); // your layout file

        // Initialize bottom navbar items
        navHome = findViewById(R.id.nav_home);
        navActivity = findViewById(R.id.nav_activity);
        navReports = findViewById(R.id.nav_reports);
        navSOS = findViewById(R.id.nav_sos);
        navProfile = findViewById(R.id.nav_profile);

        // Set click listeners for navbar
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to HealthDashboardActivity
                Intent intent = new Intent(ExerciseTrackerActivity.this, HealthDashboardActivity.class);
                startActivity(intent);
            }
        });

        navActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Already on ExerciseTrackerActivity, optionally refresh
                Intent intent = new Intent(ExerciseTrackerActivity.this, ExerciseTrackerActivity.class);
                startActivity(intent);
            }
        });

        navReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ReportsInsightsActivity
                Intent intent = new Intent(ExerciseTrackerActivity.this, ReportsInsightsActivity.class);
                startActivity(intent);
            }
        });

        navSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to EmergencyActivity
                Intent intent = new Intent(ExerciseTrackerActivity.this, EmergencyActivity.class);
                startActivity(intent);
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ProfileSettingsActivity
                Intent intent = new Intent(ExerciseTrackerActivity.this, ProfileSettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
