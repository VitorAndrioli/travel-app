package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class NewTravelActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_travel);
    }


    public void create(View view) {
        String name = ((EditText) findViewById(R.id.name)).getText().toString();
        double budget = 0.0;

        String budget_text = ((EditText) findViewById(R.id.budget)).getText().toString();

        if (budget_text != null || !budget_text.isEmpty())
            budget = Double.parseDouble(budget_text);

        SharedPreferences shared_preferences = getSharedPreferences("USER_DATA", 0);
        long user_id = shared_preferences.getLong("user_id", 0);
        User user = User.findById(User.class, user_id);


        Travel travel = new Travel(name);
        travel.save();

        TravelUser travel_user = new TravelUser(travel, user, 50.0);
        travel_user.save();

        Intent i = new Intent(NewTravelActivity.this, TravelsActivity.class);
        startActivity(i);

    }



}
