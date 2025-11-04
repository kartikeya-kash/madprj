package com.example.madprj;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignupStep2Activity extends AppCompatActivity {
    private Button btnNextObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_step2);
        btnNextObj = findViewById(R.id.btnNext);
        btnNextObj.setOnClickListener(v->{
            Intent i = new Intent(this, SignupStep3Activity.class);
            startActivity(i);
        });

    }
}