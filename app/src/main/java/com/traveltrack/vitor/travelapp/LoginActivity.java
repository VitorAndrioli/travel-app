package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.List;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void enter(View view) {

        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        List<User> users = User.find(User.class, "email = ?", email);
        User user;

        if (users.size() > 0) {
            user = users.get(0);
        } else {
            user = new User("usuario", email);
            user.save();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("user_id", user.getId());
        editor.commit();

        Intent i = new Intent(this, TravelsActivity.class);
        startActivity(i);
        finish();
    }

    public void register(View v) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}
