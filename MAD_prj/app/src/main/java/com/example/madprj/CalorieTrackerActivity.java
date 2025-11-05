package com.example.madprj;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CalorieTrackerActivity extends AppCompatActivity {

    String ai_rule, user_promt, ai_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calorie_tracker);

        // Example input
        user_promt = "2 banana";

        ai_rule = "reply only with a number (no text). How many calories in "+user_promt+"? Only number output like 250.";

        // Temporarily allow network on main thread (not recommended for production)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Send request to Ollama
        ai_response = getOllamaResponse(ai_rule);

        // Show result
        Toast.makeText(this, "Calories: " + ai_response, Toast.LENGTH_LONG).show();
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

            // Parse as JSON
            JSONObject json = new JSONObject(response.toString());
            return json.optString("response", "No response").trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}