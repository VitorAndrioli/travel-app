package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class CategoriesActivity extends Activity {
    private int travelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Intent intent = getIntent();
        travelId = Integer.parseInt(intent.getStringExtra("travelId"));

    }

    public void addExpense(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.light_green));
        String categoryName = view.getTag().toString();

        Intent intent = new Intent(this, NewExpenseActivity.class);
        intent.putExtra("categoryName", categoryName);
        intent.putExtra("travelId", String.valueOf(travelId));
        startActivity(intent);
    }

    public void goBack(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(this, TravelActivity.class);
        intent.putExtra("travelId", String.valueOf(travelId));
        startActivity(intent);
        finish();
    }

}
