package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void enter(View view) {
        Intent i = new Intent(LoginActivity.this, TravelsActivity.class);
        startActivity(i);
        finish();
    }


}
