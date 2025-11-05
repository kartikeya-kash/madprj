package com.example.madprj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class TrackCaloriesActivity extends AppCompatActivity {

    Button btnNext;
    MaterialCardView card1, card2, card3;
    int click = 0;

    // ✅ Added references for dots and text
    ImageView dot1, dot2, dot3;
    TextView tvPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_calories); // your XML file

        btnNext = findViewById(R.id.btn_next);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);

        // ✅ Initialize the dots and page number
        tvPage = findViewById(R.id.tv_page);
        dot1 = (ImageView) ((android.widget.LinearLayout) findViewById(R.id.dots)).getChildAt(0);
        dot2 = (ImageView) ((android.widget.LinearLayout) findViewById(R.id.dots)).getChildAt(1);
        dot3 = (ImageView) ((android.widget.LinearLayout) findViewById(R.id.dots)).getChildAt(2);

        btnNext.setOnClickListener(v -> {
            if (click == 0) {
                click++;
                animateCardChange(card1, card2);
                updateProgress(2);
            } else if (click == 1) {
                click++;
                animateCardChange(card2, card3);
                updateProgress(3);
            } else if (click == 2) {
                Intent intent = new Intent(TrackCaloriesActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // ✅ Fade animation for smooth transition
    private void animateCardChange(MaterialCardView oldCard, MaterialCardView newCard) {
        if (oldCard == null || newCard == null) return;

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(300);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(300);

        oldCard.startAnimation(fadeOut);
        oldCard.setVisibility(View.GONE);

        newCard.setVisibility(View.VISIBLE);
        newCard.startAnimation(fadeIn);
    }

    // ✅ Updates dots and "x of 3" label
    private void updateProgress(int step) {
        if (tvPage != null) tvPage.setText(step + " of 3");

        if (dot1 != null && dot2 != null && dot3 != null) {
            if (step == 1) {
                dot1.setImageResource(R.drawable.active_dot);
                dot2.setImageResource(R.drawable.inactive_dot);
                dot3.setImageResource(R.drawable.inactive_dot);
            } else if (step == 2) {
                dot1.setImageResource(R.drawable.inactive_dot);
                dot2.setImageResource(R.drawable.active_dot);
                dot3.setImageResource(R.drawable.inactive_dot);
            } else if (step == 3) {
                dot1.setImageResource(R.drawable.inactive_dot);
                dot2.setImageResource(R.drawable.inactive_dot);
                dot3.setImageResource(R.drawable.active_dot);
            }
        }
    }
}