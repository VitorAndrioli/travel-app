package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class TravelsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels);
    }

    public void addTravel(View view) {
        Intent i = new Intent(TravelsActivity.this, TravelActivity.class);
        startActivity(i);

    }

}
