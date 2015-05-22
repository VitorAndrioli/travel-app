package com.traveltrack.vitor.travelapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import java.util.List;
import java.util.Random;


public class NewExpenseActivity extends ActionBarActivity {
    Travel currentTravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        Intent intent = getIntent();
        int travel_id = Integer.parseInt(intent.getStringExtra("travel_id"));

        currentTravel = Travel.findById(Travel.class, travel_id);

    }

    public void create(View view) {
        String valueField = ((EditText) findViewById(R.id.value)).getText().toString();
        double value = valueField.isEmpty() ? 0 : Double.parseDouble(valueField);

        String description = ((EditText) findViewById(R.id.description)).getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", 0);
        long userId = sharedPreferences.getLong("user_id", 0);
        User user = User.findById(User.class, userId);

        List<Category> categories = Category.listAll(Category.class);

        Random rand = new Random();
        int randomNum = rand.nextInt(categories.size() + 1);
        Category category;
        if (randomNum < 5)
            category = categories.get(randomNum);
        else
            category = categories.get(0);

        Expense expense = new Expense(value, description, category, user, currentTravel);

        expense.save();

        Intent intent = new Intent(this, TravelActivity.class);
        intent.putExtra("travel_id", currentTravel.getId().toString());
        startActivity(intent);

    }


}
