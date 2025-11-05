package com.example.madprj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReportsInsightsActivity extends AppCompatActivity {

    private LinearLayout navHome, navActivity, navReports, navSOS, navProfile;
    EditText queobj; //get que
    Button btn_generate_ai_report_obj; //generate ai report
    TextView tv_ai_generated_report_obj; //show report


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reports_insights);

        SharedPreferences usersignupdata = getSharedPreferences("usersignupdata", MODE_PRIVATE);
        String email = usersignupdata.getString("email", ""); //get logged in user email




        queobj = findViewById(R.id.que);
        btn_generate_ai_report_obj = findViewById(R.id.btn_generate_ai_report);
        tv_ai_generated_report_obj = findViewById(R.id.tv_ai_generated_report);

        btn_generate_ai_report_obj.setOnClickListener(v -> {
            String que = queobj.getText().toString().trim();
            if (que.isEmpty()) {
                tv_ai_generated_report_obj.setText("Please enter a question first.");
                return;
            }

            String url = "https://9rp3msd0-3000.inc1.devtunnels.ms/getUserFullData?email=" + email;

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    response -> {
                        try {
                            if (response.getBoolean("success")) {
                                JSONObject user = response.getJSONObject("user");
                                JSONArray calories = response.getJSONArray("calories");

                                // 🧠 Combine user data + calorie records into one text
                                StringBuilder dataForAI = new StringBuilder();
                                dataForAI.append("User Profile:\n");
                                dataForAI.append("Age: ").append(user.optString("age")).append("\n");
                                dataForAI.append("Weight: ").append(user.optString("weight")).append("\n");
                                dataForAI.append("Height: ").append(user.optString("height")).append("\n\n");

                                dataForAI.append("Recent Calorie Records:\n");
                                for (int i = 0; i < calories.length(); i++) {
                                    JSONObject c = calories.getJSONObject(i);
                                    dataForAI.append("• ")
                                            .append(c.optString("calorie_intake"))
                                            .append(" kcal — ")
                                            .append(c.optString("timestamp"))
                                            .append("\n");
                                }

                                // 🧠 Now send this data + the user's question to AI (later you’ll replace this with actual AI API)
                                String simulatedResponse = "Based on your data, you're maintaining good dietary balance. Keep tracking calories and hydration!";
                                tv_ai_generated_report_obj.setText(simulatedResponse);

                            } else {
                                tv_ai_generated_report_obj.setText("Failed to load user data for AI report.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            tv_ai_generated_report_obj.setText("Error processing data.");
                        }
                    },
                    error -> tv_ai_generated_report_obj.setText("Error fetching user data: " + error.getMessage())
            );

            Volley.newRequestQueue(getApplicationContext()).add(request);
        });













































        // Initialize navbar
        navHome = findViewById(R.id.nav_home);
        navActivity = findViewById(R.id.nav_activity);
        navReports = findViewById(R.id.nav_reports);
        navSOS = findViewById(R.id.nav_sos);
        navProfile = findViewById(R.id.nav_profile);

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(ReportsInsightsActivity.this, HealthDashboardActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        navActivity.setOnClickListener(v -> {
            startActivity(new Intent(ReportsInsightsActivity.this, ExerciseTrackerActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        navReports.setOnClickListener(v -> {
            // Already on ReportsInsightsActivity
        });

        navSOS.setOnClickListener(v -> {
            startActivity(new Intent(ReportsInsightsActivity.this, EmergencyActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(ReportsInsightsActivity.this, ProfileSettingsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }
}
