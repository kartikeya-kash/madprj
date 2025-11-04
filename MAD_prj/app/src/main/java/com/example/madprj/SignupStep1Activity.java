package com.example.madprj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SignupStep1Activity extends AppCompatActivity {

    private Button btnNextObj,gotologinobj;
    private EditText etNameObj, etEmailObj, etPasswordObj, etConfirmPasswordObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_step1);

        etNameObj = findViewById(R.id.etName);
        etEmailObj = findViewById(R.id.etEmail);
        etPasswordObj = findViewById(R.id.etPassword);
        etConfirmPasswordObj = findViewById(R.id.etConfirmPassword);
        btnNextObj = findViewById(R.id.btnNext);
        gotologinobj = findViewById(R.id.gotologin);
        gotologinobj.setOnClickListener(v->{
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        });

        btnNextObj.setOnClickListener(v -> {
            String name = etNameObj.getText().toString().trim();
            String email = etEmailObj.getText().toString().trim();
            String password = etPasswordObj.getText().toString().trim();
            String confirmPassword = etConfirmPasswordObj.getText().toString().trim();

            // --- Validation checks ---
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!confirmPassword.equals(password)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ If all good, go to next step and pass data forward
            SharedPreferences usersignupdata = getSharedPreferences("usersignupdata", MODE_PRIVATE);
            SharedPreferences.Editor editor = usersignupdata.edit();
            editor.putString("name", name);
            editor.putString("email", email);
            editor.putString("password", password);

            Intent i = new Intent(this, SignupStep2Activity.class);
            startActivity(i);
        });
    }
}