package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static android.net.Uri.parse;


public class TravelActivity extends Activity {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.US);
    private Travel currentTravel;
    private RelativeLayout warning;
    private Expense expenseToRemove;
    private LinearLayout expenseField;
    private TravelUser travel_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        SharedPreferences shared_preferences = getSharedPreferences("USER_DATA", 0);
        long userId = shared_preferences.getLong("userId", 0);
        User user = User.findById(User.class, userId);

        warning = (RelativeLayout) findViewById(R.id.warning);

        Intent intent = getIntent();
        int travelId = Integer.parseInt(intent.getStringExtra("travelId"));

        currentTravel = Travel.findById(Travel.class, travelId);

        travel_user = TravelUser.find(TravelUser.class, "travel = ? and user = ?", currentTravel.getId().toString(), user.getId().toString()).get(0);

        ((TextView) findViewById(R.id.name)).setText(currentTravel.getName());

        updateExpenses();

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
            row.setTag(expense.getId());

            TextView value = (TextView) row.findViewById(R.id.value);
            TextView date = (TextView) row.findViewById(R.id.date);
            TextView description = (TextView) row.findViewById(R.id.description);
            ImageView category = (ImageView) row.findViewById(R.id.category);

            value.setText(String.valueOf( expense.toString() ));
            date.setText(sdf.format(expense.getDate()));
            if (expense.getDescription().isEmpty()) {
                row.findViewById(R.id.border).setVisibility(View.GONE);
            } else
                description.setText(String.valueOf(expense.getDescription()));
            category.setImageURI(Uri.parse(expense.getCategory().getURI()));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 90);
            params.setMargins(0, 10, 0, 0);
            row.setLayoutParams(params);

            ((LinearLayout) expensesList).addView(row);
        }
    }

    public void updateExpenses() {
        DecimalFormat df = new DecimalFormat("#,###.00");
        String totalExpenses = currentTravel.getCurrency().getSymbol() + " " + df.format(currentTravel.getTotalExpenses());

        if (travel_user.getBudget() > 0) {
            String budget = currentTravel.getCurrency().getSymbol() + " " + df.format(travel_user.getBudget());
            ((TextView) findViewById(R.id.budget)).setText(totalExpenses + " / " + budget);

            if(currentTravel.getTotalExpenses() > travel_user.getBudget()) {
                ((TextView) findViewById(R.id.budget)).setTextColor(getResources().getColor(R.color.error));
            } else {
                ((TextView) findViewById(R.id.budget)).setTextColor(getResources().getColor(R.color.dark_blue));
            }

        } else {
            ((TextView) findViewById(R.id.budget)).setText(totalExpenses);
        }


    }

    public void goBack(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(this, TravelIndexActivity.class);
        startActivity(intent);
        finish();
    }

    public void viewGraph(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(this, ExpensesGraphActivity.class);
        intent.putExtra("travelId", currentTravel.getId().toString());
        startActivity(intent);
    }

    public void addExpense(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.putExtra("travelId", currentTravel.getId().toString());
        startActivity(intent);
    }

    public void removeExpense(View view) {
        warning.setVisibility(View.VISIBLE);
        expenseField = (LinearLayout) ((ImageView) view).getParent();
        long expenseId = Long.valueOf(expenseField.getTag().toString());
        expenseToRemove = Expense.findById(Expense.class, expenseId);
    }

    public void confirmRemoval(View view) {
        LinearLayout expenseList = (LinearLayout) findViewById(R.id.expenses_list);
        expenseList.removeView( expenseField );
        expenseToRemove.delete();
        warning.setVisibility(View.GONE);
        updateExpenses();
    }

    public void cancelRemoval(View view) {
        expenseToRemove = null;
        warning.setVisibility(View.GONE);
    }
}
