package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Category.count(Category.class) == 0) {
            String[] categories = {"passage", "accommodation", "transportation", "alimentation", "leisure", "other"};

            for (int i=0; i< categories.length; i++) {
                String uri = "android.resource://com.traveltrack.vitor.travelapp/drawable/" + categories[i];
                Category category = new Category(categories[i], uri);
                category.save();
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", 0);
        long userId = sharedPreferences.getLong("userId", 0);
        final User user = User.findById(User.class, userId);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);

                if (user != null)
                    i = new Intent(SplashActivity.this, TravelsActivity.class);

                startActivity(i);
                finish();

            }
        }, 500);
    }
}
