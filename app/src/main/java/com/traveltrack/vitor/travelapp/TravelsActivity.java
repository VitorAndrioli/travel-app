package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Iterator;
import java.util.List;


public class TravelsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels);

        List<Travel> travels = Travel.listAll(Travel.class);

        Log.d("debug", Category.listAll(Category.class).size() + " --------------");


    }

    public void addTravel(View view) {
        Intent i = new Intent(TravelsActivity.this, NewTravelActivity.class);
        startActivity(i);

    }

}
