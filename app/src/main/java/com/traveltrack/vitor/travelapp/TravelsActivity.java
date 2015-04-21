package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;


public class TravelsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels);

        SharedPreferences shared_preferences = getSharedPreferences("USER_DATA", 0);
        long user_id = shared_preferences.getLong("user_id", 0);
        User user = User.findById(User.class, user_id);

        if (user != null)
            ((TextView) findViewById(R.id.user_name)).setText(user.name);

        List<TravelUser> travels = user.getTravels();

        View linear_layout = findViewById(R.id.travel_list);
        LinearLayout text_view = (LinearLayout)getLayoutInflater().inflate(R.layout.travel, null);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        text_view.setLayoutParams(lp);

        View row = null;
        LayoutInflater inflater = this.getLayoutInflater();

        for (int i=0; i< travels.size(); i++) {


            row = inflater.inflate(R.layout.travel, null);

            TextView tv = (TextView) row.findViewById(R.id.name);
            tv.setText(travels.get(i).travel.name);

            ((LinearLayout) linear_layout).addView(row);


        }

    }

    public void openTravel(View view) {
        LinearLayout ll = (LinearLayout) view;

        String travel_name = ((TextView) ll.getChildAt(0)).getText().toString();
        Travel travel = Travel.find(Travel.class, "name = ?", travel_name).get(0);
        Intent i = new Intent(TravelsActivity.this, TravelActivity.class);
        i.putExtra("travel_id", travel.getId().toString());
        startActivity(i);

    }

    public void addTravel(View view) {
        Intent i = new Intent(TravelsActivity.this, NewTravelActivity.class);
        startActivity(i);

    }

    public void logout(View v) {
        SharedPreferences shared_preferences = getSharedPreferences("USER_DATA", 0);
        SharedPreferences.Editor editor = shared_preferences.edit();
        editor.remove("user_id");
        editor.commit();

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}
