package com.example.madprj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReportsInsightsActivity extends AppCompatActivity {

    private LinearLayout navHome, navActivity, navReports, navSOS, navProfile;
    EditText queobj;
    Button btn_generate_ai_report_obj;
    TextView tv_ai_generated_report_obj;
    LinearLayout aiLoaderContainer;
    ProgressBar aiLoader;
    TextView aiLoaderText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reports_insights);
        aiLoaderContainer = findViewById(R.id.ai_loader_container);
        aiLoader = findViewById(R.id.ai_loader);
        aiLoaderText = findViewById(R.id.ai_loader_text);

        SharedPreferences usersignupdata = getSharedPreferences("usersignupdata", MODE_PRIVATE);
        String email = usersignupdata.getString("email", "");

        queobj = findViewById(R.id.que);
        btn_generate_ai_report_obj = findViewById(R.id.btn_generate_ai_report);
        tv_ai_generated_report_obj = findViewById(R.id.tv_ai_generated_report);

        btn_generate_ai_report_obj.setOnClickListener(v -> {
            String que = queobj.getText().toString().trim();
            if (que.isEmpty()) {
                tv_ai_generated_report_obj.setText("Please enter a question first.");
                return;
            }

            // 🔹 Step 1: Fetch user data from your backend
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

                                // 🧠 Build the AI context
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

                                if (dataForAI.length() > 2000) {
                                    dataForAI.setLength(2000); // keep first 2000 chars
                                }

                                // 🔹 Step 2: Build AI prompt
                                String prompt = "Generate a simple fitness and health report summary for this user data. "
                                        + "Don't ask questions, just describe insights clearly. "
                                        + "Assume light exercise daily. Data:\n\n" + dataForAI.toString();

                                // 🔹 Step 3: Call Ollama AI using Volley
                                callOllamaAI(prompt);

                            } else {
                                tv_ai_generated_report_obj.setText("Failed to load user data for AI report.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            tv_ai_generated_report_obj.setText("Error processing user data.");
                        }
                    },
                    error -> tv_ai_generated_report_obj.setText("Error fetching user data: " + error.getMessage())
            );

            Volley.newRequestQueue(getApplicationContext()).add(request);
        });

        // Navbar setup
        navHome = findViewById(R.id.nav_home);
        navActivity = findViewById(R.id.nav_activity);
        navReports = findViewById(R.id.nav_reports);
        navSOS = findViewById(R.id.nav_sos);
        navProfile = findViewById(R.id.nav_profile);

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, HealthDashboardActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        navActivity.setOnClickListener(v -> {
            startActivity(new Intent(this, ExerciseTrackerActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        navReports.setOnClickListener(v -> {
            // Already here
        });

        navSOS.setOnClickListener(v -> {
            startActivity(new Intent(this, EmergencyActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileSettingsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }

    // 🔹 Async Ollama AI call using Volley (no blocking)
    private void callOllamaAI(String prompt) {
        try {
            String url = "http://10.0.2.2:5001/api/generate";

            JSONObject body = new JSONObject();
            body.put("model", "llama3.2:3b"); // your main model
            body.put("prompt", prompt);
            body.put("stream", false);

            // 🔹 Show loader
            aiLoaderContainer.setVisibility(View.VISIBLE);
            tv_ai_generated_report_obj.setText("");

            com.android.volley.toolbox.StringRequest request =
                    new com.android.volley.toolbox.StringRequest(
                            Request.Method.POST,
                            url,
                            response -> {
                                aiLoaderContainer.setVisibility(View.GONE); // hide loader
                                try {
                                    JSONObject json = new JSONObject(response);
                                    String aiResponse = json.optString("response", "").trim();

                                    if (aiResponse.isEmpty())
                                        tv_ai_generated_report_obj.setText("⚠️ AI returned empty response");
                                    else
                                        tv_ai_generated_report_obj.setText(aiResponse);

                                } catch (Exception e) {
                                    tv_ai_generated_report_obj.setText("Raw response: " + response);
                                }
                            },
                            error -> {
                                aiLoaderContainer.setVisibility(View.GONE); // hide loader
                                String msg;
                                if (error.networkResponse != null)
                                    msg = "AI Error " + error.networkResponse.statusCode + ": " +
                                            new String(error.networkResponse.data);
                                else
                                    msg = "AI Error: " + (error.getMessage() != null ? error.getMessage() : "Unknown");
                                tv_ai_generated_report_obj.setText(msg);
                            }) {

                        @Override
                        public byte[] getBody() {
                            return body.toString().getBytes();
                        }

                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }
                    };

            request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                    30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            Volley.newRequestQueue(getApplicationContext()).add(request);

        } catch (Exception e) {
            aiLoaderContainer.setVisibility(View.GONE);
            e.printStackTrace();
            tv_ai_generated_report_obj.setText("Error: " + e.getMessage());
        }
    }
}