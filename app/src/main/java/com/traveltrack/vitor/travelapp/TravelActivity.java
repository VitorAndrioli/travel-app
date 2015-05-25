package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
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
            ((TextView) findViewById(R.id.budget)).setText(String.valueOf(currentTravel.getTotalExpenses()) + " / " + String.valueOf( travel_user.budget));
        else
            ((TextView) findViewById(R.id.budget)).setText(String.valueOf(currentTravel.getTotalExpenses()));

        if (currentTravel.beginning != null && currentTravel.end != null)
            ((TextView) findViewById(R.id.date)).setText(sdf.format(currentTravel.beginning) + " - " + sdf.format(currentTravel.end));


        if (currentTravel.imageURI != null)
            ((ImageView) findViewById(R.id.picture)).setImageURI(parse(currentTravel.imageURI));


        List<Expense> expenses = currentTravel.getExpenses();

        View expensesList = findViewById(R.id.expenses_list);
        LinearLayout expenseField = (LinearLayout)getLayoutInflater().inflate(R.layout.expense, null);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        expenseField.setLayoutParams(lp);


        for (int i=0; i< expenses.size(); i++) {
            View row = null;
            LayoutInflater inflater = this.getLayoutInflater();

            Expense expense = expenses.get(i);
            row = inflater.inflate(R.layout.expense, null);

            TextView value = (TextView) row.findViewById(R.id.value);
            TextView description = (TextView) row.findViewById(R.id.description);
            ImageView category = (ImageView) row.findViewById(R.id.category);

            value.setText(String.valueOf( expense.value ));
            description.setText(String.valueOf(expense.description));
            category.setImageURI(Uri.parse(expense.category.uri));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 90);
            params.setMargins(0, 5, 0, 5);
            row.setLayoutParams(params);

            ((LinearLayout) expensesList).addView(row);
        }
    }

    public void goBack(View view) {
        ((ImageButton) view).setBackgroundColor(getResources().getColor(R.color.light_green));

        Intent intent = new Intent(this, TravelsActivity.class);
        startActivity(intent);
        finish();
    }

    public void addExpense(View view) {
        ((ImageButton) view).setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent i = new Intent(this, CategoriesActivity.class);
        i.putExtra("travel_id", currentTravel.getId().toString());
        startActivity(i);
    }

    public void viewGraph(View view) {
        ((ImageButton) view).setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra( "travel_id", currentTravel.getId().toString() );
        startActivity(intent);
    }

    @Override
    public void onStop() {
        ((ImageButton) findViewById(R.id.back)).setBackgroundColor(getResources().getColor(R.color.dark_blue));
        ((ImageButton) findViewById(R.id.view_graph)).setBackgroundColor(getResources().getColor(R.color.dark_blue));
        ((ImageButton) findViewById(R.id.add_expense)).setBackgroundColor(getResources().getColor(R.color.dark_blue));

        super.onStop();
    }

}
