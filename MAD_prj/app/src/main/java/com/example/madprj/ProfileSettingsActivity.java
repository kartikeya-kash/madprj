package com.example.madprj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProfileSettingsActivity extends AppCompatActivity {

    private LinearLayout navHome, navActivity, navReports, navSOS, navProfile;
    TextView shownameobj, showemailobj,showbmiobj,showbmistatusobj,showageobj,showweightobj,showgenderobj,showheightobj,showcalobj,showsleepobj,showwaterobj,showstepsobj,showbloodobj,showallergiesobj,showmedicalconditionobj;

    // 🧠 Variables for user data
    String name, email, gender, bloodType, allergies, medicalCondition;
    int id, age, dailyCal, steps;
    float weight, height, sleep, water;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_settings);

        shownameobj = findViewById(R.id.showname);
        showemailobj = findViewById(R.id.showemail);
        showbmiobj = findViewById(R.id.showbmi);
        showbmistatusobj = findViewById(R.id.showbmistatus);
        showageobj = findViewById(R.id.showage);
        showweightobj = findViewById(R.id.showweight);
        showgenderobj = findViewById(R.id.showgender);
        showheightobj = findViewById(R.id.showheight);
        showcalobj  = findViewById(R.id.showcal);
        showsleepobj = findViewById(R.id.showsleep);
        showwaterobj = findViewById(R.id.showwater);
        showstepsobj = findViewById(R.id.showsstep);
        showbloodobj = findViewById(R.id.showblood);
        showallergiesobj = findViewById(R.id.showallergies);
        showmedicalconditionobj = findViewById(R.id.showmedicalcondition);



        // Navbar initialization
        navHome = findViewById(R.id.nav_home);
        navActivity = findViewById(R.id.nav_activity);
        navReports = findViewById(R.id.nav_reports);
        navSOS = findViewById(R.id.nav_sos);
        navProfile = findViewById(R.id.nav_profile);

        SharedPreferences prefs = getSharedPreferences("userdata", MODE_PRIVATE);
        email = prefs.getString("email", "");

        // Allow network access (temporary for testing)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (!email.isEmpty()) {
            fetchUserData(email);
        }

        // Navbar navigation
        navHome.setOnClickListener(v -> startActivity(new Intent(this, HealthDashboardActivity.class)));
        navActivity.setOnClickListener(v -> startActivity(new Intent(this, ExerciseTrackerActivity.class)));
        navReports.setOnClickListener(v -> startActivity(new Intent(this, ReportsInsightsActivity.class)));
        navSOS.setOnClickListener(v -> startActivity(new Intent(this, EmergencyActivity.class)));
        navProfile.setOnClickListener(v -> {});
    }

    private void fetchUserData(String email) {
        try {
            URL url = new URL("http://10.0.2.2:3000/getUserData?email=" + email);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());

            if (jsonResponse.getBoolean("success")) {
                JSONObject user = jsonResponse.getJSONObject("user");

                // ✅ Store data in Java variables
                id = user.getInt("id");
                name = user.optString("name", "");
                this.email = user.optString("email", "");
                age = user.optInt("age", 0);
                weight = (float) user.optDouble("weight", 0);
                gender = user.optString("gender", "");
                height = (float) user.optDouble("height", 0);
                dailyCal = user.optInt("HG_DailyCal", 0);
                sleep = (float) user.optDouble("HG_Sleep", 0);
                water = (float) user.optDouble("HG_Water", 0);
                steps = user.optInt("HG_Steps", 0);
                bloodType = user.optString("MI_BloodType", "");
                allergies = user.optString("MI_Allergies", "");
                medicalCondition = user.optString("MI_MedicalCondition", "");
                double BMI = weight / ((height / 100) * (height / 100));


                // ✅ Save data locally for other activities
                SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_PRIVATE).edit();
                editor.putString("name", name);
                editor.putInt("age", age);
                editor.putFloat("weight", weight);
                editor.putString("gender", gender);
                editor.putFloat("height", height);
                editor.putInt("dailyCal", dailyCal);
                editor.putFloat("sleep", sleep);
                editor.putFloat("water", water);
                editor.putInt("steps", steps);
                editor.putString("bloodType", bloodType);
                editor.putString("allergies", allergies);
                editor.putString("medicalCondition", medicalCondition);
                editor.apply();

                // ✅ Show data on screen
                runOnUiThread(() -> {
                    shownameobj.setText(name);
                    showemailobj.setText(email);
                    showbmiobj.setText(String.format("%.2f", BMI));

                    showageobj.setText(String.valueOf(age));
                    showweightobj.setText(String.valueOf(weight));
                    showgenderobj.setText(gender);
                    showheightobj.setText(String.valueOf(height));
                    showcalobj.setText(String.valueOf(dailyCal));
                    showsleepobj.setText(String.valueOf(sleep));
                    showwaterobj.setText(String.valueOf(water));
                    showstepsobj.setText(String.valueOf(steps));
                    showbloodobj.setText(bloodType);
                    showallergiesobj.setText(allergies);
                    showmedicalconditionobj.setText(medicalCondition);

                });
            } else {
                runOnUiThread(() -> showemailobj.setText("User not found"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() -> showemailobj.setText("❌ Error: " + e.getMessage()));
        }
    }
}