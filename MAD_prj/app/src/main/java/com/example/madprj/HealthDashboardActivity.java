package com.example.madprj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class HealthDashboardActivity extends AppCompatActivity {
    private LinearLayout navHome, navActivity, navReports, navSOS, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_health_dashboard);

        navHome = findViewById(R.id.nav_home);
        navActivity = findViewById(R.id.nav_activity);
        navReports = findViewById(R.id.nav_reports);
        navSOS = findViewById(R.id.nav_sos);
        navProfile = findViewById(R.id.nav_profile);

        // Set click listeners
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Already on HealthDashboardActivity, optionally refresh
                Intent intent = new Intent(HealthDashboardActivity.this, HealthDashboardActivity.class);
                startActivity(intent);
            }
        });

        navActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to ExerciseTrackerActivity
                Intent intent = new Intent(HealthDashboardActivity.this, ExerciseTrackerActivity.class);
                startActivity(intent);
            }
        });

        navReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to ReportsInsightsActivity
                Intent intent = new Intent(HealthDashboardActivity.this, ReportsInsightsActivity.class);
                startActivity(intent);
            }
        });

        navSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to EmergencyActivity
                Intent intent = new Intent(HealthDashboardActivity.this, EmergencyActivity.class);
                startActivity(intent);
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to ProfileSettingsActivity
                Intent intent = new Intent(HealthDashboardActivity.this, ProfileSettingsActivity.class);
                startActivity(intent);
            }
        });


    }
}
