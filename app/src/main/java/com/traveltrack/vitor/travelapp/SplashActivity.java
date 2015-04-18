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

        if (Category.listAll(Category.class).size() <= 0) {
            String[] categories = {"passagem", "hospedagem", "transporte", "alimentação", "lazer"};
            for (int i=0; i< categories.length; i++) {
                Category category = new Category(categories[i]);
                category.save();
            }
        }

        SharedPreferences shared_preferences = getSharedPreferences("USER_DATA", 0);
        long user_id = shared_preferences.getLong("user_id", 0);
        final User user = User.findById(User.class, user_id);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);

                if (user != null)
                    i = new Intent(SplashActivity.this, TravelsActivity.class);

                startActivity(i);

            }
        }, 500);


    }
}
