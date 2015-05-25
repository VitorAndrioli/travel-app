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
        long userId = sharedPreferences.getLong("user_id", 0);
        final User user = User.findById(User.class, userId);

        /*final ImageView logo = (ImageView) findViewById(R.id.logo);

        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, -100.0f, 0.0f);
        animation.setInterpolator();
        //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(1000);  // animation duration
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                logo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);

                if (user != null)
                    i = new Intent(SplashActivity.this, TravelsActivity.class);

                startActivity(i);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        logo.startAnimation(animation);*/

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
