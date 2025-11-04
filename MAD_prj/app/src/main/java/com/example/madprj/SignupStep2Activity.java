package com.example.madprj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SignupStep2Activity extends AppCompatActivity {

    private Button btnNextObj;
    private EditText etAgeObj, etGenderObj, etWeightObj, etHeightObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_step2);

        // 🔹 Initialize views
        etAgeObj = findViewById(R.id.etAge);
        etGenderObj = findViewById(R.id.etGender);
        etWeightObj = findViewById(R.id.etWeight);
        etHeightObj = findViewById(R.id.etHeight);
        btnNextObj = findViewById(R.id.btnNext);

        btnNextObj.setOnClickListener(v -> {
            // 🔹 Get values from EditTexts
            String age = etAgeObj.getText().toString().trim();
            String gender = etGenderObj.getText().toString().trim();
            String weight = etWeightObj.getText().toString().trim();
            String height = etHeightObj.getText().toString().trim();

            // 🔹 Check for empty fields
            if (age.isEmpty() || gender.isEmpty() || weight.isEmpty() || height.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔹 Store data in SharedPreferences (same file as step 1)
            SharedPreferences usersignupdata = getSharedPreferences("usersignupdata", MODE_PRIVATE);
            String name = usersignupdata.getString("name", "");
            SharedPreferences.Editor editor = usersignupdata.edit();
            editor.putString("age", age);
            editor.putString("gender", gender);
            editor.putString("weight", weight);
            editor.putString("height", height);
            editor.apply(); // ✅ saves asynchronously

            Toast.makeText(this,name,Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Details saved successfully", Toast.LENGTH_SHORT).show();

            // 🔹 Move to next step
            Intent i = new Intent(this, SignupStep3Activity.class);
            startActivity(i);
        });
    }
}