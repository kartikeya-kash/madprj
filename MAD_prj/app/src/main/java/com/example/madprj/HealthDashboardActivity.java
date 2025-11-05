package com.example.madprj;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class HealthDashboardActivity extends AppCompatActivity implements SensorEventListener {

    private LinearLayout navHome, navActivity, navReports, navSOS, navProfile;
    private LinearLayout cardCalories, cardWater, cardSleep, cardExercise, cardVitals, cardReports;
    private Button btnLogWater, btnAddMeal;
    TextView summary_steps_value_obj,summary_cal_value_obj,sub_cal_obj,percent_cal,sub_sleep,percent_sleep;
    float caloriesBurned;

    // ✅ Global variable to store user steps
    public static int userTotalSteps = 0;

    // ✅ Sensor components
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private boolean isSensorPresent = false;
    String weight,height,calories,water,sleep,steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_health_dashboard);

        SharedPreferences usersignupdata = getSharedPreferences("usersignupdata", MODE_PRIVATE);
        String age = usersignupdata.getString("age", "");
         weight = usersignupdata.getString("weight", "");
         height = usersignupdata.getString("height", "");
         calories = usersignupdata.getString("calories_goal", "");
         water = usersignupdata.getString("water_goal", "");
         sleep = usersignupdata.getString("sleep_goal", "");
         steps = usersignupdata.getString("steps_goal", "");

        summary_steps_value_obj = findViewById(R.id.summary_steps_value);
        summary_cal_value_obj = findViewById(R.id.summary_cal_value);
        sub_cal_obj=findViewById(R.id.sub_cal);
        percent_cal=findViewById(R.id.percent_cal);
        sub_sleep=findViewById(R.id.sub_sleep);
        percent_sleep=findViewById(R.id.percent_sleep);


        // ------------------- NAVBAR ------------------- //
        navHome = findViewById(R.id.nav_home);
        navActivity = findViewById(R.id.nav_activity);
        navReports = findViewById(R.id.nav_reports);
        navSOS = findViewById(R.id.nav_sos);
        navProfile = findViewById(R.id.nav_profile);

        // ------------------- DASHBOARD CARDS ------------------- //
        cardCalories = findViewById(R.id.card_calories);
        cardWater = findViewById(R.id.card_water);
        cardSleep = findViewById(R.id.card_sleep);
        cardExercise = findViewById(R.id.card_exercise);
        cardVitals = findViewById(R.id.card_vitals);
        cardReports = findViewById(R.id.card_reports);

        // ------------------- QUICK ACTION BUTTONS ------------------- //
        btnLogWater = findViewById(R.id.btn_log_water);
        btnAddMeal = findViewById(R.id.btn_add_meal);

        // ✅ Initialize step counter sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null && sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isSensorPresent = true;
        } else {
            isSensorPresent = false;
        }

        // ✅ Request permission for activity recognition (Android 10+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
            }
        }

        // ------------------- NAVBAR NAVIGATION ------------------- //
        navHome.setOnClickListener(v ->
                startActivity(new Intent(this, HealthDashboardActivity.class))
        );

        navActivity.setOnClickListener(v ->
                startActivity(new Intent(this, ExerciseTrackerActivity.class))
        );

        navReports.setOnClickListener(v ->
                startActivity(new Intent(this, ReportsInsightsActivity.class))
        );

        navSOS.setOnClickListener(v ->
                startActivity(new Intent(this, EmergencyActivity.class))
        );

        navProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileSettingsActivity.class))
        );

        // ------------------- DASHBOARD CARD NAVIGATION ------------------- //
        cardCalories.setOnClickListener(v ->
                startActivity(new Intent(this, CalorieTrackerActivity.class))
        );

        cardWater.setOnClickListener(v ->
                startActivity(new Intent(this, WaterTrackerActivity.class))
        );

        cardSleep.setOnClickListener(v ->
                startActivity(new Intent(this, SleepTrackerActivity.class))
        );

        cardExercise.setOnClickListener(v ->
                startActivity(new Intent(this, ExerciseTrackerActivity.class))
        );

        cardVitals.setOnClickListener(v ->
                startActivity(new Intent(this, HealthVitalsActivity.class))
        );

        cardReports.setOnClickListener(v ->
                startActivity(new Intent(this, ReportsInsightsActivity.class))
        );

        // ------------------- QUICK ACTION NAVIGATION ------------------- //
        btnLogWater.setOnClickListener(v -> {
            Intent intent = new Intent(this, WaterTrackerActivity.class);
            startActivity(intent);
        });

        btnAddMeal.setOnClickListener(v -> {
            Intent intent = new Intent(this, CalorieTrackerActivity.class);
            startActivity(intent);
        });
    }

    // ✅ Start listening to the step sensor
    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorPresent) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    // ✅ Stop listening when not in use
    @Override
    protected void onPause() {
        super.onPause();
        if (isSensorPresent) {
            sensorManager.unregisterListener(this);
        }
    }

    // ✅ Update global step variable whenever sensor changes
    @Override
    public void onSensorChanged(android.hardware.SensorEvent event) {
        userTotalSteps = (int) event.values[0];

        float weightKg = 0f;
        float heightCm = 0f;
        try {
            weightKg = Float.parseFloat(weight);
            heightCm = Float.parseFloat(height);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // ✅ Calculate stride length and distance
        float stride = heightCm * 0.415f / 100f; // meters per step
        float distanceKm = (userTotalSteps * stride) / 1000f;

        // ✅ Calculate calories using walking MET = 3.5
        int caloriesBurned = (int) (3.5f * weightKg * (distanceKm / 5f));

        summary_cal_value_obj.setText(String.valueOf(caloriesBurned)); //set calories in head
        summary_steps_value_obj.setText(String.valueOf(userTotalSteps)); //set steps in head

        sub_cal_obj.setText(caloriesBurned+" / "+calories+" kcal"); // set calories in cards
        int calint = Integer.parseInt(calories);
        int calper = (int) ((caloriesBurned * 100) / calint);
        percent_cal.setText(calper+"% complete"); // set calories in cards

        sub_sleep.setText(sleep+"hours / 8 hours"); //sleep card
        int sleepint = Integer.parseInt(sleep);
        int perofsleep = (int) ((sleepint * 100) / 8);
        percent_sleep.setText(perofsleep+"% complete");
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {}
}