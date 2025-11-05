package com.example.madprj;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CalorieTrackerActivity extends AppCompatActivity {

    EditText etMealNameobj;
    Button btn_cal_calories_aiobj,btn_add_calories;
    ProgressBar loader;
    TextView loadingMessage,calorie_eaten;
    String ai_response_cleaned;
    String cal_intake_db;
    int cal_consumed=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calorie_tracker);
        calorie_eaten=findViewById(R.id.calorie_eaten);

        SharedPreferences usersignupdata = getSharedPreferences("usersignupdata", MODE_PRIVATE);
        String email = usersignupdata.getString("email", ""); //for storing in the db

        // UI references
        etMealNameobj = findViewById(R.id.etMealName);
        btn_cal_calories_aiobj = findViewById(R.id.btn_cal_calories_ai);
        loader = findViewById(R.id.progress_loader);
        loadingMessage = findViewById(R.id.loading_message);
        btn_add_calories = findViewById(R.id.btn_add_calories);
        btn_add_calories.setOnClickListener(v -> {
            AlertDialog.Builder abd = new AlertDialog.Builder(this);
            abd.setTitle("Add "+ai_response_cleaned+" calories");
            abd.setMessage("Are you sure");
            abd.setPositiveButton("Yes", (dialog, which) -> {
                cal_consumed+=Integer.parseInt(ai_response_cleaned);
                Toast.makeText(this, "Calories added", Toast.LENGTH_SHORT).show();
                calorie_eaten.setText(cal_consumed+" kcal consumed");
                storeindb(email,cal_intake_db);
            });
            abd.setNegativeButton("No", (dialog, which) -> {
                Toast.makeText(this, "Calories not added", Toast.LENGTH_SHORT).show();
            });
            abd.show();
            // ai_response_cleaned variable
        });


        // Hide loader initially
        loader.setVisibility(View.GONE);
        loadingMessage.setVisibility(View.GONE);

        // Handle button click
        btn_cal_calories_aiobj.setOnClickListener(v -> {
            String user_promt = etMealNameobj.getText().toString().trim();

            if (user_promt.isEmpty()) {
                Toast.makeText(this, "Please enter a meal name!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Build AI prompt
            String ai_rule = "Reply only with a number (no text). How many calories in "
                    + user_promt + "? Only number output like 250.";

            // Show loader and disable button
            loader.setVisibility(View.VISIBLE);
            loadingMessage.setVisibility(View.VISIBLE);
            btn_cal_calories_aiobj.setEnabled(false);

            // Run AI call in background
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {
                String rawResponse = getOllamaResponse(ai_rule);
                 ai_response_cleaned = rawResponse.replaceAll("[^0-9]", ""); // keep only digits

                // Switch back to main thread for UI updates
                handler.post(() -> {
                    loader.setVisibility(View.GONE);
                    loadingMessage.setVisibility(View.GONE);
                    btn_cal_calories_aiobj.setEnabled(true);

                    if (ai_response_cleaned.isEmpty()) {
                        Toast.makeText(this, "No response from AI", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, user_promt + " ≈ " + ai_response_cleaned + " kcal", Toast.LENGTH_LONG).show();
                        cal_intake_db=user_promt+" ≈ "+ai_response_cleaned+" kcal";
                    }
                });
            });
        });
    }

    private String getOllamaResponse(String prompt) {
        HttpURLConnection connection = null;
        try {
            // ✅ Use 10.0.2.2 for Android Emulator.
            // If you're using a real phone, replace with your Mac's IP (e.g., 192.168.x.x)
            URL url = new URL("http://10.0.2.2:5001/api/generate");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Build JSON body
            String body = new JSONObject()
                    .put("model", "llama3.2:3b")
                    .put("prompt", prompt)
                    .put("stream", false)
                    .toString();

            // Send the body
            try (OutputStream os = connection.getOutputStream()) {
                os.write(body.getBytes());
                os.flush();
            }

            // Read response
            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            // Parse JSON and return only the response text
            JSONObject json = new JSONObject(response.toString());
            return json.optString("response", "").trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    public void storeindb(String email, String cal_intake_db) {
        String url = "https://9rp3msd0-3000.inc1.devtunnels.ms/addCalorieIntake";  // Replace with your Node server IP (e.g., 192.168.1.10)

        try {
            JSONObject postData = new JSONObject();
            postData.put("email", email);
            postData.put("calorie_intake", cal_intake_db);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    postData,
                    response -> {
                        try {
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
            );

            Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}