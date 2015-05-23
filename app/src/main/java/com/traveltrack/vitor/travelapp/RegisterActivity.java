package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ((LinearLayout) findViewById(R.id.focus_holder)).requestFocus();
    }

    public void send(View v) {
        EditText nameField = (EditText) findViewById(R.id.name),
                 emailField = (EditText) findViewById(R.id.email),
                 passwordField = (EditText) findViewById(R.id.password),
                 passwordConfirmationField = (EditText) findViewById(R.id.password_confirmation);

        String name = nameField.getText().toString(),
               email = emailField.getText().toString(),
               password = passwordField.getText().toString(),
               passwordConfirmation = passwordConfirmationField.getText().toString();


        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()) {
            ((TextView) findViewById(R.id.empty_field_error)).setVisibility(View.VISIBLE);

            if (name.isEmpty()) {
                nameField.setBackgroundResource( R.drawable.light_green_border_error );
            }

            if (email.isEmpty()) {
                emailField.setBackgroundResource( R.drawable.light_green_border_error );
            }

            if (password.isEmpty()) {
                passwordField.setBackgroundResource( R.drawable.light_green_border_error );
            }

            if (passwordConfirmation.isEmpty()) {
                passwordConfirmationField.setBackgroundResource( R.drawable.light_green_border_error );
            }

            passwordField.setText("");
            passwordConfirmationField.setText("");
            return;
        }

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

            ((TextView) findViewById(R.id.password_error)).setVisibility(View.VISIBLE);
            passwordField.setBackgroundResource( R.drawable.light_green_border_error );
            passwordField.setText("");
            passwordConfirmationField.setBackgroundResource( R.drawable.light_green_border_error );
            passwordConfirmationField.setText("");

        }

    }

    public void cancel(View v) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }


}
