package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static android.net.Uri.parse;


public class TravelActivity extends Activity {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.US);
    Travel currentTravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        SharedPreferences shared_preferences = getSharedPreferences("USER_DATA", 0);
        long user_id = shared_preferences.getLong("user_id", 0);
        User user = User.findById(User.class, user_id);

        Intent intent = getIntent();
        int travel_id = Integer.parseInt(intent.getStringExtra("travel_id"));

        currentTravel = Travel.findById(Travel.class, travel_id);

        TravelUser travel_user = TravelUser.find(TravelUser.class, "travel = ? and user = ?", currentTravel.getId().toString(), user.getId().toString()).get(0);

        ((TextView) findViewById(R.id.name)).setText(currentTravel.name);

        if (travel_user.budget > 0)
            ((TextView) findViewById(R.id.budget)).setText(String.valueOf(travel_user.budget));

        if (currentTravel.beginning != null)
            ((TextView) findViewById(R.id.beginning)).setText(sdf.format(currentTravel.beginning));

        if (currentTravel.end != null)
            ((TextView) findViewById(R.id.end)).setText(sdf.format(currentTravel.end));
        if (currentTravel.imageURI != null)
            ((ImageView) findViewById(R.id.image)).setImageURI(parse(currentTravel.imageURI));


        List<Expense> expenses = currentTravel.getExpenses();

        View expensesList = findViewById(R.id.expenses_list);
        LinearLayout expenseField = (LinearLayout)getLayoutInflater().inflate(R.layout.expense, null);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        expenseField.setLayoutParams(lp);

        View row = null;
        LayoutInflater inflater = this.getLayoutInflater();

        for (int i=0; i< expenses.size(); i++) {
            Expense expense = expenses.get(i);
            row = inflater.inflate(R.layout.expense, null);

            TextView value = (TextView) row.findViewById(R.id.value);
            TextView description = (TextView) row.findViewById(R.id.description);
            ImageView category = (ImageView) row.findViewById(R.id.category);

            Log.d("Debug", "--------------------- " + expense.value);

            value.setText(String.valueOf( expense.value ));
            description.setText(String.valueOf(expense.description));
            category.setImageURI(Uri.parse(expense.category.uri));

            ((LinearLayout) expensesList).addView(row);
        }
    }

    public void addExpense(View view) {
        Intent i = new Intent(TravelActivity.this, NewExpenseActivity.class);
        i.putExtra("travel_id", currentTravel.getId().toString());
        startActivity(i);
    }

    public void openGraph(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra( "travel_id", currentTravel.getId().toString() );
        startActivity(intent);


    }

}
