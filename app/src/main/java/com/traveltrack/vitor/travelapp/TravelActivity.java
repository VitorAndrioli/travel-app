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
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.US);
    private Travel currentTravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        SharedPreferences shared_preferences = getSharedPreferences("USER_DATA", 0);
        long userId = shared_preferences.getLong("userId", 0);
        User user = User.findById(User.class, userId);

        Intent intent = getIntent();
        int travelId = Integer.parseInt(intent.getStringExtra("travelId"));

        currentTravel = Travel.findById(Travel.class, travelId);

        TravelUser travel_user = TravelUser.find(TravelUser.class, "travel = ? and user = ?", currentTravel.getId().toString(), user.getId().toString()).get(0);

        ((TextView) findViewById(R.id.name)).setText(currentTravel.getName());

        if (travel_user.getBudget() > 0)
            ((TextView) findViewById(R.id.budget)).setText(String.valueOf(currentTravel.getTotalExpenses()) + " / " + String.valueOf( travel_user.getBudget()));
        else
            ((TextView) findViewById(R.id.budget)).setText(String.valueOf(currentTravel.getTotalExpenses()));

        if (currentTravel.getStart() != null && currentTravel.getEnd() != null)
            ((TextView) findViewById(R.id.date)).setText(sdf.format(currentTravel.getStart()) + " - " + sdf.format(currentTravel.getEnd()));


        if (currentTravel.getImageURI() != null)
            ((ImageView) findViewById(R.id.picture)).setImageURI(parse(currentTravel.getImageURI()));


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

            value.setText(String.valueOf( expense.getValue() ));
            description.setText(String.valueOf(expense.getDescription()));
            category.setImageURI(Uri.parse(expense.getCategory().getURI()));

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
        i.putExtra("travelId", currentTravel.getId().toString());
        startActivity(i);
    }

    public void viewGraph(View view) {
        ((ImageButton) view).setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra( "travelId", currentTravel.getId().toString() );
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
