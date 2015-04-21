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

import java.util.Iterator;
import java.util.List;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void enter(View view) {

        String name = ((EditText) findViewById(R.id.name)).getText().toString();
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        List<User> users = User.find(User.class, "name = ?", name);
        User user;

        for (int i=0; i<users.size(); i++)
            Log.d("debug", users.get(i).name);

        if (users.size() > 0) {
            user = users.get(0);
        } else {
            Log.d("debug", "novo usuario");
            user = new User(name, email);
            user.save();
        }

        SharedPreferences shared_preferences = getSharedPreferences("USER_DATA", 0);
        SharedPreferences.Editor editor = shared_preferences.edit();
        editor.putLong("user_id", user.getId());
        editor.commit();

        Intent i = new Intent(LoginActivity.this, TravelsActivity.class);
        startActivity(i);
        finish();
    }
}
