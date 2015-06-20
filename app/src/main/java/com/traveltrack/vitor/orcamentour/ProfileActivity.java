package com.traveltrack.vitor.orcamentour;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class ProfileActivity extends Activity {
    private EditText nameField;
    private EditText emailField;
    private EditText currentPasswordField;
    private EditText passwordField;
    private EditText passwordConfirmationField;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", 0);
        long userId = sharedPreferences.getLong("userId", 0);
        user = User.findById(User.class, userId);

        nameField = (EditText) findViewById(R.id.user_name);
        emailField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);
        passwordConfirmationField = (EditText) findViewById(R.id.password_confirmation);
        currentPasswordField = (EditText) findViewById(R.id.current_password);

        nameField.setText( user.getName() );
        emailField.setText( user.getEmail() );
    }

    public void submit(View view) {
        if (user != null) {
            user.setName( nameField.getText().toString() );
            user.setEmail( emailField.getText().toString() );

            String password = currentPasswordField.getText().toString();
            String newPassword = passwordField.getText().toString();
            String passwordConfirmation = passwordConfirmationField.getText().toString();

            if ( password.equals( user.getPassword() ) && newPassword.equals( passwordConfirmation ) ) {
                user.setPassword( newPassword );
            }

            user.save();
        }

        finish();
        startActivity( getIntent() );
    }

    public void goBack(View view) {
        finish();
    }
}
