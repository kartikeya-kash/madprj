package com.example.madprj;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExerciseTrackerActivity extends AppCompatActivity {

    private LinearLayout workoutsContainer;
    private Button btnAdd;
    private TextView tvCalValue, tvActiveValue, tvStepsValue;

    private SharedPreferences prefs;
    private static final String PREFS_NAME = "ExercisePrefs";
    private LinearLayout navHome, navActivity, navReports, navSOS, navProfile;

    private static final String KEY_EXERCISES = "exercise_list";
    private static final String KEY_TOTAL_CAL = "total_calories";
    private static final String KEY_TOTAL_MIN = "total_minutes";
    private static final String KEY_TOTAL_STEPS = "total_steps";

    private int totalCalories = 0;
    private int totalMinutes = 0;
    private int totalSteps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_tracker);
        EdgeToEdge.enable(this);

        // Initialize UI
        workoutsContainer = findViewById(R.id.workouts_container);
        btnAdd = findViewById(R.id.btn_add);
        tvCalValue = findViewById(R.id.tv_cal_value);
        tvActiveValue = findViewById(R.id.tv_active_value);
        tvStepsValue = findViewById(R.id.tv_steps_value);

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // ------------------- NAVBAR ------------------- //
        navHome = findViewById(R.id.nav_home);
        navActivity = findViewById(R.id.nav_activity);
        navReports = findViewById(R.id.nav_reports);
        navSOS = findViewById(R.id.nav_sos);
        navProfile = findViewById(R.id.nav_profile);

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


        // Load saved data
        loadExercises();
        updateTopStats();

        // Add new exercise
        btnAdd.setOnClickListener(v -> showAddExerciseDialog());
    }

    private void showAddExerciseDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_exercise, null);

        EditText etName = dialogView.findViewById(R.id.et_exercise_name);
        EditText etDuration = dialogView.findViewById(R.id.et_exercise_duration);
        EditText etCalories = dialogView.findViewById(R.id.et_exercise_calories);
        EditText etTime = dialogView.findViewById(R.id.et_exercise_time);
        EditText etSteps = dialogView.findViewById(R.id.et_exercise_steps);
        EditText etType = dialogView.findViewById(R.id.et_exercise_type);

        new AlertDialog.Builder(this)
                .setTitle("Add Exercise")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String duration = etDuration.getText().toString().trim();
                    String calories = etCalories.getText().toString().trim();
                    String time = etTime.getText().toString().trim();
                    String steps = etSteps.getText().toString().trim();
                    String type = etType.getText().toString().trim();

                    if (name.isEmpty() || duration.isEmpty() || calories.isEmpty() || time.isEmpty()) {
                        Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    addExerciseCard(name, duration, calories, time, steps, type);
                    saveExercise(name, duration, calories, time, steps, type);

                    int stepCount = steps.isEmpty() ? 0 : Integer.parseInt(steps);
                    updateStats(Integer.parseInt(calories), Integer.parseInt(duration), stepCount);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void addExerciseCard(String name, String duration, String calories, String time, String steps, String type) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View cardView = inflater.inflate(R.layout.item_exercise_card, workoutsContainer, false);

        TextView tvName = cardView.findViewById(R.id.tv_exercise_name);
        TextView tvDetails = cardView.findViewById(R.id.tv_exercise_details);
        TextView tvTime = cardView.findViewById(R.id.tv_exercise_time);
        TextView tvTag = cardView.findViewById(R.id.tv_exercise_tag);

        tvName.setText(name);
        String details = duration + " mins • " + calories + " cal";
        if (!steps.isEmpty()) details += " • " + steps + " steps";
        tvDetails.setText(details);
        tvTime.setText(time);
        tvTag.setText(type);

        workoutsContainer.addView(cardView);
    }

    private void saveExercise(String name, String duration, String calories, String time, String steps, String type) {
        try {
            JSONArray array = new JSONArray(prefs.getString(KEY_EXERCISES, "[]"));
            JSONObject obj = new JSONObject();
            obj.put("name", name);
            obj.put("duration", duration);
            obj.put("calories", calories);
            obj.put("time", time);
            obj.put("steps", steps);
            obj.put("type", type);
            array.put(obj);
            prefs.edit().putString(KEY_EXERCISES, array.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadExercises() {
        try {
            JSONArray array = new JSONArray(prefs.getString(KEY_EXERCISES, "[]"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                addExerciseCard(
                        obj.getString("name"),
                        obj.getString("duration"),
                        obj.getString("calories"),
                        obj.getString("time"),
                        obj.optString("steps", ""),
                        obj.optString("type", "")
                );
            }
            totalCalories = prefs.getInt(KEY_TOTAL_CAL, 0);
            totalMinutes = prefs.getInt(KEY_TOTAL_MIN, 0);
            totalSteps = prefs.getInt(KEY_TOTAL_STEPS, 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateStats(int calories, int minutes, int steps) {
        totalCalories += calories;
        totalMinutes += minutes;
        totalSteps += steps;

        prefs.edit()
                .putInt(KEY_TOTAL_CAL, totalCalories)
                .putInt(KEY_TOTAL_MIN, totalMinutes)
                .putInt(KEY_TOTAL_STEPS, totalSteps)
                .apply();

        updateTopStats();
    }

    private void updateTopStats() {
        tvCalValue.setText(String.valueOf(totalCalories));
        tvActiveValue.setText(String.valueOf(totalMinutes));
        tvStepsValue.setText(String.valueOf(totalSteps));
    }
}