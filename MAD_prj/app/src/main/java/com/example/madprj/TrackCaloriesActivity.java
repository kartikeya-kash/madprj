package com.example.madprj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TrackCaloriesActivity extends AppCompatActivity {

    // Top Bar
    ImageView backButton;

    // Meal Section
    EditText etMealName;
    Button btnIncreaseQty, btnDecreaseQty, btnCalCaloriesAI, btnAddCalories;
    TextView tvQuantity;
    Spinner spinnerUnit;
    int quantity = 1;

    // Bottom Navbar
    LinearLayout navHome, navActivity, navReports, navSos, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_tracker); // XML filename

        // 🔹 Top Bar
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // 🔹 Meal Section
        etMealName = findViewById(R.id.etMealName);
        btnIncreaseQty = findViewById(R.id.btnIncreaseQty);
        btnDecreaseQty = findViewById(R.id.btnDecreaseQty);
        tvQuantity = findViewById(R.id.tvQuantity);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        btnCalCaloriesAI = findViewById(R.id.btn_cal_calories_ai);
        btnAddCalories = findViewById(R.id.btn_add_calories);

        // Increment quantity
        btnIncreaseQty.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        // Decrement quantity (min 1)
        btnDecreaseQty.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            } else {
                Toast.makeText(this, "Minimum quantity is 1", Toast.LENGTH_SHORT).show();
            }
        });

        // AI Calculate Calories button
        btnCalCaloriesAI.setOnClickListener(v -> {
            String mealName = etMealName.getText().toString().trim();
            if (mealName.isEmpty()) {
                Toast.makeText(this, "Please enter a meal name", Toast.LENGTH_SHORT).show();
            } else {
                String unit = spinnerUnit.getSelectedItem().toString();
                Toast.makeText(this,
                        "Calculating calories for " + quantity + " " + unit + " of " + mealName + "...",
                        Toast.LENGTH_SHORT).show();

                // 👉 Replace this with your AI logic or next screen
            }
        });

        // Quick Add Calories button
        btnAddCalories.setOnClickListener(v -> {
            Toast.makeText(this, "Quick Add Calories clicked", Toast.LENGTH_SHORT).show();
            // Example: navigate to manual calorie entry screen
            // startActivity(new Intent(this, ManualAddCaloriesActivity.class));
        });

        // 🔹 Bottom Navbar
        navHome = findViewById(R.id.nav_home);
        navActivity = findViewById(R.id.nav_activity);
        navReports = findViewById(R.id.nav_reports);
        navSos = findViewById(R.id.nav_sos);
        navProfile = findViewById(R.id.nav_profile);

        // ✅ Bottom Navbar Navigation

        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(TrackCaloriesActivity.this, TrackCaloriesActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // no animation
        });

        navActivity.setOnClickListener(v -> {
            Intent intent = new Intent(TrackCaloriesActivity.this, ExerciseTrackerActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        navReports.setOnClickListener(v -> {
            Intent intent = new Intent(TrackCaloriesActivity.this, ReportsInsightsActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        navSos.setOnClickListener(v -> {
            Intent intent = new Intent(TrackCaloriesActivity.this, EmergencyActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(TrackCaloriesActivity.this, ProfileSettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });



    }
}