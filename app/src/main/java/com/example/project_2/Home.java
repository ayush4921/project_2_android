package com.example.project_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();

        // Retrieve the username passed from the MainActivity
        String username = intent.getStringExtra("USERNAME_KEY");
        TextView usernameTextView = findViewById(R.id.textViewName);
        usernameTextView.setText("Hello, " + username);

        Button dailyDiary = findViewById(R.id.btnDailyDiary);
        Button myNotes = findViewById(R.id.btnMyNotes);
        Button tracker = findViewById(R.id.btnTracker);
        Button links = findViewById(R.id.btnHelpfulLinks);

        dailyDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDailyDiary = new Intent(Home.this, DailyDiary.class);
                intentDailyDiary.putExtra("USERNAME_KEY", username); // Pass the username to AccountSettings
                startActivity(intentDailyDiary);
            }
        });


        myNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMyNotes = new Intent(Home.this, MyNotes.class);
                startActivity(intentMyNotes);
            }
        });

        tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTracker = new Intent(Home.this, Tracker.class);
                startActivity(intentTracker);
            }
        });

        links.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLinks = new Intent(Home.this, HelpfulLinks.class);
                startActivity(intentLinks);
            }
        });

    }
    @Override
    public void onBackPressed() {

//        super.onBackPressed();
    }
}