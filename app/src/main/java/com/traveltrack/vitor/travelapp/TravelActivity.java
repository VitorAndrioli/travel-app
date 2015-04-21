package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;


public class TravelActivity extends Activity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

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
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
