package com.example.madprj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class LoginActivity extends AppCompatActivity {

    EditText etEmailObj, etPasswordObj;
    Button btnSignInObj ;
    TextView tvSignupobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        etEmailObj = findViewById(R.id.etEmail);
        etPasswordObj = findViewById(R.id.etPassword);
        btnSignInObj = findViewById(R.id.btnSignIn);
        tvSignupobj = findViewById(R.id.tvSignup);
        tvSignupobj.setOnClickListener(v->{
            Intent i = new Intent(this,SignupStep1Activity.class);
            startActivity(i);
        });



        btnSignInObj.setOnClickListener(v -> signin());
    }

    public void signin() {
        String email = etEmailObj.getText().toString().trim();
        String password = etPasswordObj.getText().toString().trim();
        SharedPreferences usersignupdata = getSharedPreferences("usersignupdata", MODE_PRIVATE);
        SharedPreferences.Editor ed=usersignupdata.edit();
        ed.putString("email",email);
        ed.apply();


        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                // Emulator -> Localhost = 10.0.2.2
                URL url = new URL("https://9rp3msd0-3000.inc1.devtunnels.ms/login"); //http://10.0.2.2:3000/login
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                // ✅ Correct headers
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                // ✅ Create JSON body
                JSONObject json = new JSONObject();
                json.put("email", email);
                json.put("password", password);

                // Debug log (visible in Logcat)
                System.out.println("Sending JSON: " + json.toString());

                // ✅ Send data
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(json.toString().getBytes("UTF-8"));
                    os.flush();
                }

                // ✅ Read response
                Scanner sc = new Scanner(conn.getInputStream());
                StringBuilder response = new StringBuilder();
                while (sc.hasNext()) {
                    response.append(sc.nextLine());
                }
                sc.close();

                JSONObject res = new JSONObject(response.toString());
                boolean success = res.getBoolean("success");
                String message = res.getString("message");

                runOnUiThread(() -> {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                    SharedPreferences prefs = getSharedPreferences("userdata", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.apply();

                    Intent intent = new Intent(this, HealthDashboardActivity.class);
                    startActivity(intent);
                });

                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}