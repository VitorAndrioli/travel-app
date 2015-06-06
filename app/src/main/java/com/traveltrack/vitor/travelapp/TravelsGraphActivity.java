package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class TravelsGraphActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels_graph);

        findViewById(R.id.view_graph).setBackgroundColor(getResources().getColor(R.color.light_green));
        findViewById(R.id.view_graph).setClickable(false);

    }

    public void goBack(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(this, TravelIndexActivity.class);
        startActivity(intent);
        finish();
    }

}
