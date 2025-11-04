package com.example.madprj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SignupStep4Activity extends AppCompatActivity {

    private Button btnNextObj;
    private EditText etBloodTypeObj, etAllergiesObj, etConditionObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_step4);

        // 🔹 Initialize Views
        etBloodTypeObj = findViewById(R.id.etBloodType);
        etAllergiesObj = findViewById(R.id.etAllergies);
        etConditionObj = findViewById(R.id.etCondition);
        btnNextObj = findViewById(R.id.btnSignup);

        btnNextObj.setOnClickListener(v -> {
            // 🔹 Get values
            String bloodType = etBloodTypeObj.getText().toString().trim();
            String allergies = etAllergiesObj.getText().toString().trim();
            String condition = etConditionObj.getText().toString().trim();

            // 🔹 Validation
            if (bloodType.isEmpty()) {
                Toast.makeText(this, "Please enter your blood type", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔹 Save to SharedPreferences
            SharedPreferences usersignupdata = getSharedPreferences("usersignupdata", MODE_PRIVATE);
            SharedPreferences.Editor editor = usersignupdata.edit();
            editor.putString("blood_type", bloodType);
            editor.putString("allergies", allergies);
            editor.putString("medical_condition", condition);
            editor.apply();

            Toast.makeText(this, "All details saved successfully!", Toast.LENGTH_SHORT).show();

            // 🔹 (Optional) Retrieve all data for checking/logging
            String name = usersignupdata.getString("name", "");
            String email = usersignupdata.getString("email", "");
            String password = usersignupdata.getString("password", "");
            String age = usersignupdata.getString("age", "");
            String gender = usersignupdata.getString("gender", "");
            String weight = usersignupdata.getString("weight", "");
            String height = usersignupdata.getString("height", "");
            String calories = usersignupdata.getString("calories_goal", "");
            String water = usersignupdata.getString("water_goal", "");
            String sleep = usersignupdata.getString("sleep_goal", "");
            String steps = usersignupdata.getString("steps_goal", "");

            // ✅ You can log or send these to server/db if needed
            // Log.d("SignupData", name + " " + email + " " + gender + " " + weight + ...);

            // 🔹 Go to Dashboard
            Intent i = new Intent(this, HealthDashboardActivity.class);
            startActivity(i);
            finish(); // optional: closes signup steps
        });
    }
}