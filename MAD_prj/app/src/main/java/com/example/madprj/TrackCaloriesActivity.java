package com.example.madprj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class TrackCaloriesActivity extends AppCompatActivity {

    Button btnNext;
    MaterialCardView card1, card2, card3;
    int click=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_calories); // your XML file

        btnNext = findViewById(R.id.btn_next);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);


        btnNext.setOnClickListener(v -> {
         if(click==0){
             click++;
             card1.setVisibility(View.GONE);
             card2.setVisibility(View.VISIBLE);
         } else if (click==1) {
             click++;
             card2.setVisibility(View.GONE);
             card3.setVisibility(View.VISIBLE);
         }else if (click==2){
             Intent intent = new Intent(TrackCaloriesActivity.this, LoginActivity.class);
             startActivity(intent);
         }

        });
    }
}

