package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.net.Uri.parse;


public class TravelActivity extends Activity {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        SharedPreferences shared_preferences = getSharedPreferences("USER_DATA", 0);
        long user_id = shared_preferences.getLong("user_id", 0);
        User user = User.findById(User.class, user_id);

        Intent i = getIntent();
        int travel_id = Integer.parseInt(i.getStringExtra("travel_id"));

        Travel travel = Travel.findById(Travel.class, travel_id);

        TravelUser travel_user = TravelUser.find(TravelUser.class, "travel = ? and user = ?", travel.getId().toString(), user.getId().toString()).get(0);

        ((TextView) findViewById(R.id.name)).setText(travel.name);
        ((TextView) findViewById(R.id.budget)).setText(String.valueOf(travel_user.budget));
        ((TextView) findViewById(R.id.beginning)).setText(sdf.format(travel.beginning));
        ((TextView) findViewById(R.id.end)).setText(sdf.format(travel.end));
        if (travel.imageURI != null)
            ((ImageView) findViewById(R.id.image)).setImageURI(parse(travel.imageURI));

    }

    public void addExpense(View v) {
        Intent i = new Intent(TravelActivity.this, NewExpenseActivity.class);

        startActivity(i);
    }

}
