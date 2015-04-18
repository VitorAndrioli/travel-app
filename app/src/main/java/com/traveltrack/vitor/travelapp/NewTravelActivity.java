package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
        float budget = Float.parseFloat(((EditText) findViewById(R.id.budget)).getText().toString());

        Travel travel = new Travel(name, budget);
        travel.save();

        Intent i = new Intent(NewTravelActivity.this, TravelsActivity.class);
        startActivity(i);

    }



}
