package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void send(View v) {
        String name, email, password, passwordConfirmation;

        name = ((EditText) findViewById(R.id.name)).getText().toString();
        email = ((EditText) findViewById(R.id.email)).getText().toString();
        password = ((EditText) findViewById(R.id.password)).getText().toString();
        passwordConfirmation = ((EditText) findViewById(R.id.password_confirmation)).getText().toString();

        if ( password.equals( passwordConfirmation ) ) {
            User user = new User(name, email, password);
            user.save();

            SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("user_id", user.getId());
            editor.commit();

            Intent i = new Intent(this, TravelsActivity.class);
            startActivity(i);
            finish();
        } else {
            EditText passwordField = (EditText) findViewById(R.id.password);
            EditText passwordConfirmationField = (EditText) findViewById(R.id.password_confirmation);

            ((TextView) findViewById(R.id.password_error)).setVisibility(View.VISIBLE);
            passwordField.setBackgroundColor(getResources().getColor( R.color.red ));
            passwordField.setText("");
            passwordConfirmationField.setBackgroundColor(getResources().getColor( R.color.red ));
            passwordConfirmationField.setText("");

        }

    }

    public void cancel(View v) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }


}
