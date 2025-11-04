package com.example.madprj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etEmailObj, etPasswordObj;
    Button btnSignInObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        etEmailObj = findViewById(R.id.etEmail);
        etPasswordObj = findViewById(R.id.etPassword);
        btnSignInObj = findViewById(R.id.btnSignIn);
        btnSignInObj.setOnClickListener(v->signin());

        Button btnSkip = findViewById(R.id.btnSkip);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, HealthDashboardActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    public void signin() {
        String email = etEmailObj.getText().toString().trim();
        String password = etPasswordObj.getText().toString().trim();

        // Email regex pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // Validate email
        if (!email.matches(emailPattern)) {
            etEmailObj.setError("Invalid email address");
            etEmailObj.requestFocus();
            return;
        }

        // Validate password (example: at least 6 chars)
        if (password.isEmpty() || password.length() < 6) {
            etPasswordObj.setError("Password must be at least 6 characters");
            etPasswordObj.requestFocus();
            return;
        }
        // If both valid
        Toast.makeText(this, "Validation Passed ✅", Toast.LENGTH_SHORT).show();


    }
}
