package com.example.madprj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class WaterTrackerActivity extends AppCompatActivity {

    // Declare UI elements

    private TextView tvGlasses, tvEncourage, tvRemaining;
    private ProgressBar progressBar;
    private Button btnAddGlass, btnMinus;

    private int glassesDrunk = 0; // Start from 1 glass
    private  int TOTAL_GLASSES ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_water_tracker);

        // --- Initialize all UI components ---
        tvGlasses = findViewById(R.id.tv_glasses);
        tvEncourage = findViewById(R.id.tv_encourage);
        tvRemaining = findViewById(R.id.tv_remaining);
        progressBar = findViewById(R.id.progressBar);
        btnAddGlass = findViewById(R.id.btn_add_glass);
        btnMinus = findViewById(R.id.btn_minus);

        SharedPreferences usersignupdata = getSharedPreferences("usersignupdata", MODE_PRIVATE);
        String water = usersignupdata.getString("water_goal", "");
        int num = Integer.parseInt(water);
        TOTAL_GLASSES = (num/1000)*4;

        // Toolbar Back button
        ImageView backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(v -> finish());

        // --- Button Listeners ---
        btnAddGlass.setOnClickListener(v -> addGlass());
        btnMinus.setOnClickListener(v -> removeGlass());

        // --- Bottom Navigation ---
        setupBottomNavigation();

        // --- Initial UI update ---
        updateUI();
    }

    // Increase glass count
    private void addGlass() {
        if (glassesDrunk < TOTAL_GLASSES) {
            glassesDrunk++;
            HealthDashboardActivity.changewatercount(glassesDrunk);
            updateUI();
        } else {
            Toast.makeText(this, "You've reached your daily goal! 🎉", Toast.LENGTH_SHORT).show();
        }
    }

    // Decrease glass count
    private void removeGlass() {
        if (glassesDrunk > 0) {
            glassesDrunk--;
            updateUI();
        } else {
            Toast.makeText(this, "No glasses to remove!", Toast.LENGTH_SHORT).show();
        }
    }

    // Update progress bar, text, and water drops
    private void updateUI() {
        // Update text
        tvGlasses.setText(glassesDrunk + " / " + TOTAL_GLASSES + " glasses");

        // Update remaining glasses text
        int remaining = TOTAL_GLASSES - glassesDrunk;
        if (remaining > 0) {
            tvRemaining.setText(remaining + " more glasses to go!");
        } else {
            tvRemaining.setText("Goal achieved! 🏆");
        }

        // Update progress bar
        int progress = (int) (((double) glassesDrunk / TOTAL_GLASSES) * 100);
        progressBar.setProgress(progress);

        // Update encouragement message
        if (glassesDrunk == TOTAL_GLASSES) {
            tvEncourage.setText("Excellent! You hit your goal! 💧🏆");
        } else if (glassesDrunk > TOTAL_GLASSES / 2) {
            tvEncourage.setText("Almost there! Keep sipping 💪");
        } else if (glassesDrunk > 0) {
            tvEncourage.setText("Good start! Stay hydrated 🌊");
        } else {
            tvEncourage.setText("Time to drink your first glass 💦");
        }




    }

    // Navigation to other activities
    private void setupBottomNavigation() {
        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout navActivity = findViewById(R.id.nav_activity);
        LinearLayout navReports = findViewById(R.id.nav_reports);
        LinearLayout navSOS = findViewById(R.id.nav_sos);
        LinearLayout navProfile = findViewById(R.id.nav_profile);

        navHome.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        navActivity.setOnClickListener(v -> startActivity(new Intent(this, ExerciseTrackerActivity.class)));
        navReports.setOnClickListener(v -> startActivity(new Intent(this, ReportsInsightsActivity.class)));
        navSOS.setOnClickListener(v -> startActivity(new Intent(this, EmergencyActivity.class)));
        navProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileSettingsActivity.class)));
    }
}