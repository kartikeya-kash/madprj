package com.example.madprj;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CalorieTrackerActivity extends AppCompatActivity {

    EditText etMealNameobj;
    Button btn_cal_calories_aiobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calorie_tracker);

        etMealNameobj = findViewById(R.id.etMealName);
        btn_cal_calories_aiobj = findViewById(R.id.btn_cal_calories_ai);

        // Temporarily allow network on main thread (not recommended for production)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // When button is clicked
        btn_cal_calories_aiobj.setOnClickListener(v -> {
            String user_promt = etMealNameobj.getText().toString().trim();

            if (user_promt.isEmpty()) {
                Toast.makeText(this, "Please enter a meal name!", Toast.LENGTH_SHORT).show();
                return;
            }

            String ai_rule = "Reply only with a number (no text). How many calories in "
                    + user_promt + "? Only number output like 250.";

            String ai_response = getOllamaResponse(ai_rule);

            ai_response = ai_response.replaceAll("[^0-9]", ""); // remove non-numeric chars

            if (ai_response.isEmpty()) {
                Toast.makeText(this, "No response from AI", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, user_promt + " ≈ " + ai_response + " kcal", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getOllamaResponse(String prompt) {
        try {
            URL url = new URL("http://10.0.2.2:5001/api/generate"); // emulator
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Include "stream": false to get one single JSON object
            String body = new JSONObject()
                    .put("model", "llama3.2:3b")
                    .put("prompt", prompt)
                    .put("stream", false)
                    .toString();

            try (OutputStream os = connection.getOutputStream()) {
                os.write(body.getBytes());
                os.flush();
            }

            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            // Parse JSON and return response text
            JSONObject json = new JSONObject(response.toString());
            return json.optString("response", "").trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}