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
    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText passwordConfirmationField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ((LinearLayout) findViewById(R.id.focus_holder)).requestFocus();

        nameField = (EditText) findViewById(R.id.name);
        emailField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);
        passwordConfirmationField = (EditText) findViewById(R.id.password_confirmation);

    }

    public void send(View v) {
        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String passwordConfirmation = passwordConfirmationField.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()) {
            ((TextView) findViewById(R.id.empty_field_error)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.password_error)).setVisibility(View.INVISIBLE);

            if (name.isEmpty()) {
                nameField.setBackgroundResource(R.drawable.red_border);
            } else {
                nameField.setBackgroundResource(R.drawable.light_green_border);
            }

            if (email.isEmpty()) {
                emailField.setBackgroundResource(R.drawable.red_border);
            } else {
                emailField.setBackgroundResource(R.drawable.light_green_border);
            }

            if (password.isEmpty()) {
                passwordField.setBackgroundResource(R.drawable.red_border);
            }

            if (passwordConfirmation.isEmpty()) {
                passwordConfirmationField.setBackgroundResource(R.drawable.red_border);
            }

            passwordField.setText("");
            passwordConfirmationField.setText("");

        } else if ( !password.equals( passwordConfirmation ) ) {
            ((TextView) findViewById(R.id.password_error)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.empty_field_error)).setVisibility(View.INVISIBLE);
            if (name.isEmpty()) {
                nameField.setBackgroundResource(R.drawable.red_border);
            } else {
                nameField.setBackgroundResource(R.drawable.light_green_border);
            }

            if (email.isEmpty()) {
                emailField.setBackgroundResource(R.drawable.red_border);
            } else {
                emailField.setBackgroundResource(R.drawable.light_green_border);
            }

            passwordField.setBackgroundResource(R.drawable.red_border);
            passwordField.setText("");
            passwordConfirmationField.setBackgroundResource(R.drawable.red_border);
            passwordConfirmationField.setText("");
        } else {
            User user = new User(name, email, password);
            user.save();

            SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("userId", user.getId());
            editor.commit();

            Intent intent = new Intent(this, TravelIndexActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void cancel(View v) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}
