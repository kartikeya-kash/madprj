package com.example.madprj;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AiTipsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ai_tips);

        Button btnNext = findViewById(R.id.btn_next);

        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(AiTipsActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
