package com.example.madprj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SignupStep3Activity extends AppCompatActivity {

    private Button btnNextObj;
    private EditText etCaloriesObj, etWaterObj, etSleepObj, etStepsObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_step3);

        // 🔹 Initialize all views
        etCaloriesObj = findViewById(R.id.etCalories);
        etWaterObj = findViewById(R.id.etWater);
        etSleepObj = findViewById(R.id.etSleep);
        etStepsObj = findViewById(R.id.etSteps);
        btnNextObj = findViewById(R.id.btnNext);

        btnNextObj.setOnClickListener(v -> {
            // 🔹 Get values from EditTexts
            String calories = etCaloriesObj.getText().toString().trim();
            String water = etWaterObj.getText().toString().trim();
            String sleep = etSleepObj.getText().toString().trim();
            String steps = etStepsObj.getText().toString().trim();

            // 🔹 Basic validation
            if (calories.isEmpty() || water.isEmpty() || sleep.isEmpty() || steps.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔹 Save to SharedPreferences (same file as before)
            SharedPreferences usersignupdata = getSharedPreferences("usersignupdata", MODE_PRIVATE);
            SharedPreferences.Editor editor = usersignupdata.edit();
            editor.putString("calories_goal", calories);
            editor.putString("water_goal", water);
            editor.putString("sleep_goal", sleep);
            editor.putString("steps_goal", steps);
            editor.apply();

            Toast.makeText(this, "Goals saved successfully", Toast.LENGTH_SHORT).show();

            // 🔹 Move to the next step
            Intent i = new Intent(this, SignupStep4Activity.class);
            startActivity(i);
        });
    }
}