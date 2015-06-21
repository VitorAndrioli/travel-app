package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class CategoriesActivity extends Activity {
    private Travel currentTravel;
    private long expenseId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Intent intent = getIntent();
        int travelId = Integer.parseInt(intent.getStringExtra("travelId"));
        currentTravel = Travel.findById(Travel.class, travelId);
        String expenseIdString = intent.getStringExtra("expenseId");
        expenseId = (expenseIdString == null) ? 0 : Long.parseLong( expenseIdString );

    }

    public void chooseCategory(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.light_green));
        String categoryName = view.getTag().toString();

        Intent intent = new Intent(this, NewExpenseActivity.class);
        if (expenseId > 0) {
            intent = new Intent(this, EditExpenseActivity.class);
            intent.putExtra("expenseId", String.valueOf( expenseId ));
        }

        intent.putExtra("categoryName", categoryName);
        intent.putExtra("travelId", currentTravel.getId().toString());
        startActivity(intent);
    }

    public void goBack(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.light_green));
        if (expenseId == 0) {
            Intent intent = new Intent(this, TravelActivity.class);
            intent.putExtra("travelId", currentTravel.getId().toString());
            startActivity(intent);
        }
        finish();
    }

    public void viewGraph(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(this, ExpensesGraphActivity.class);
        intent.putExtra("travelId", currentTravel.getId().toString());
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        findViewById(R.id.add_expense).setBackgroundColor(getResources().getColor(R.color.light_green));
        findViewById(R.id.add_expense).setClickable(false);
    }
}
