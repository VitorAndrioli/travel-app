package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class CategoriesActivity extends Activity {
    int travelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Intent intent = getIntent();
        travelId = Integer.parseInt(intent.getStringExtra("travel_id"));

    }

    public void addExpense(View view) {
        String categoryName = view.getTag().toString();

        Intent intent = new Intent(this, NewExpenseActivity.class);
        intent.putExtra("categoryName", categoryName);
        intent.putExtra("travelId", String.valueOf(travelId));
        startActivity(intent);
    }


}
