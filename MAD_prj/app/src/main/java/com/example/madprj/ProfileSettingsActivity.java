package com.example.madprj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

public class ProfileSettingsActivity extends AppCompatActivity {

    private LinearLayout navHome, navActivity, navReports, navSOS, navProfile;
    TextView showemailobj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_settings);

        showemailobj = findViewById(R.id.showemail);

        // Initialize navbar
        navHome = findViewById(R.id.nav_home);
        navActivity = findViewById(R.id.nav_activity);
        navReports = findViewById(R.id.nav_reports);
        navSOS = findViewById(R.id.nav_sos);
        navProfile = findViewById(R.id.nav_profile);

        SharedPreferences prefs = getSharedPreferences("userdata", MODE_PRIVATE);
        String email = prefs.getString("email", "");
        showemailobj.setText(email);

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(ProfileSettingsActivity.this, HealthDashboardActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        navActivity.setOnClickListener(v -> {
            startActivity(new Intent(ProfileSettingsActivity.this, ExerciseTrackerActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        navReports.setOnClickListener(v -> {
            startActivity(new Intent(ProfileSettingsActivity.this, ReportsInsightsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        navSOS.setOnClickListener(v -> {
            startActivity(new Intent(ProfileSettingsActivity.this, EmergencyActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        navProfile.setOnClickListener(v -> {
            // Already on ProfileSettingsActivity
        });
    }
}
