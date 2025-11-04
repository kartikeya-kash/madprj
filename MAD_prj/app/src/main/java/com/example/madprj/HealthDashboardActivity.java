package com.example.madprj;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class HealthDashboardActivity extends AppCompatActivity {
    private LinearLayout navHome, navActivity, navReports, navSOS, navProfile;
    private LinearLayout cardCalories, cardWater, cardSleep, cardExercise, cardVitals, cardReports;
    private Button btnLogWater, btnAddMeal;  // ✅ Add these buttons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_health_dashboard);

        // ------------------- NAVBAR ------------------- //
        navHome = findViewById(R.id.nav_home);
        navActivity = findViewById(R.id.nav_activity);
        navReports = findViewById(R.id.nav_reports);
        navSOS = findViewById(R.id.nav_sos);
        navProfile = findViewById(R.id.nav_profile);

        // ------------------- DASHBOARD CARDS ------------------- //
        cardCalories = findViewById(R.id.card_calories);
        cardWater = findViewById(R.id.card_water);
        cardSleep = findViewById(R.id.card_sleep);
        cardExercise = findViewById(R.id.card_exercise);
        cardVitals = findViewById(R.id.card_vitals);
        cardReports = findViewById(R.id.card_reports);

        // ------------------- QUICK ACTION BUTTONS ------------------- //
        btnLogWater = findViewById(R.id.btn_log_water);
        btnAddMeal = findViewById(R.id.btn_add_meal);

        // ------------------- NAVBAR NAVIGATION ------------------- //
        navHome.setOnClickListener(v ->
                startActivity(new Intent(this, HealthDashboardActivity.class))
        );

        navActivity.setOnClickListener(v ->
                startActivity(new Intent(this, ExerciseTrackerActivity.class))
        );

        navReports.setOnClickListener(v ->
                startActivity(new Intent(this, ReportsInsightsActivity.class))
        );

        navSOS.setOnClickListener(v ->
                startActivity(new Intent(this, EmergencyActivity.class))
        );

        navProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileSettingsActivity.class))
        );

        // ------------------- DASHBOARD CARD NAVIGATION ------------------- //
        cardCalories.setOnClickListener(v ->
                startActivity(new Intent(this, CalorieTrackerActivity.class))
        );

        cardWater.setOnClickListener(v ->
                startActivity(new Intent(this, WaterTrackerActivity.class))
        );

        cardSleep.setOnClickListener(v ->
                startActivity(new Intent(this, SleepTrackerActivity.class))
        );

        cardExercise.setOnClickListener(v ->
                startActivity(new Intent(this, ExerciseTrackerActivity.class))
        );

        cardVitals.setOnClickListener(v ->
                startActivity(new Intent(this, HealthVitalsActivity.class))
        );

        cardReports.setOnClickListener(v ->
                startActivity(new Intent(this, ReportsInsightsActivity.class))
        );

        // ------------------- QUICK ACTION NAVIGATION ------------------- //
        btnLogWater.setOnClickListener(v -> {
            Intent intent = new Intent(this, WaterTrackerActivity.class);
            startActivity(intent);
        });

        btnAddMeal.setOnClickListener(v -> {
            Intent intent = new Intent(this, CalorieTrackerActivity.class);
            startActivity(intent);
        });
    }
}