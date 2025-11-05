package com.example.madprj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

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

            // 🔹 Retrieve all stored signup data
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

            Toast.makeText(this,name,Toast.LENGTH_SHORT).show();

            // 🔹 Prepare data for API
            try {
                JSONObject userData = new JSONObject();
                userData.put("name", name);
                userData.put("email", email);
                userData.put("password", password);
                userData.put("age", age);
                userData.put("weight", weight);
                userData.put("gender", gender);
                userData.put("height", height);
                userData.put("HG_DailyCal", calories);
                userData.put("HG_Sleep", sleep);
                userData.put("HG_Water", water);
                userData.put("HG_Steps", steps);
                userData.put("MI_BloodType", bloodType);
                userData.put("MI_Allergies", allergies);
                userData.put("MI_MedicalCondition", condition);

                sendSignupDataToServer(userData);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error preparing signup data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 🔹 Function to send data to Node.js backend
    private void sendSignupDataToServer(JSONObject userData) {
        String url = "http://10.0.2.2:3000/signup"; // ⚠️ Replace with your system IP

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, userData,
                response -> {
                    Toast.makeText(this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                },
                error -> {
                    Toast.makeText(this, "Signup Failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
        );

        queue.add(request);
    }
}