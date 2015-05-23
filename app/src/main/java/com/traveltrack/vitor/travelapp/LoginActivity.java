package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((LinearLayout) findViewById(R.id.focus_holder)).requestFocus();

    }

    public void enter(View view) {

        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        List<User> users = User.find(User.class, "email = ?", email);
        User user;

        if (users.size() == 0 || !password.equals(users.get(0).password)) {
            ((TextView) findViewById(R.id.login_error)).setVisibility(View.VISIBLE);
            ((EditText) findViewById(R.id.password)).setText("");
            return;
        }

        user = users.get(0);

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
