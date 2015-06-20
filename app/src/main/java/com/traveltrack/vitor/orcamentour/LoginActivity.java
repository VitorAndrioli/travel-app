package com.traveltrack.vitor.orcamentour;

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
    private TextView emailField;
    private TextView passwordField;
    private TextView loginError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((LinearLayout) findViewById(R.id.focus_holder)).requestFocus();

        emailField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);
        loginError = (TextView) findViewById(R.id.login_error);

    }

    public void enter(View view) {

        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        List<User> users = User.find(User.class, "email = ?", email);

        if (users.size() == 0 || !password.equals(users.get(0).getPassword())) {
            loginError.setVisibility(View.VISIBLE);
            passwordField.setText("");
        } else {

            User user = users.get(0);

            SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("userId", user.getId());
            editor.commit();

            Intent intent = new Intent(this, TravelIndexActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void register(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
