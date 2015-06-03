package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;
import java.util.List;


public class ExpensesGraphActivity extends Activity {
    private PieChart mChart;
    private Travel travel;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_graph);

        Intent intent = getIntent();
        int travelId = Integer.parseInt(intent.getStringExtra("travelId"));

        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", 0);
        long userId = sharedPreferences.getLong("userId", 0);
        user = User.findById(User.class, userId);
        travel = Travel.findById(Travel.class, travelId);

        mChart = (PieChart) findViewById(R.id.chart);
        mChart.setUsePercentValues(false);
        mChart.setDescription("");
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawHoleEnabled(false);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(false);

        setData();

        mChart.animateY(1000, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);

    }

    private void setData() {

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        List<Category> categories = Category.listAll(Category.class);

        for (int i=0; i<categories.size(); i++) {
            Category category = categories.get(i);
            float total = category.getExpenses(user, travel);

            if (total > 0) {
                xVals.add(category.getPortName());
                yVals1.add( new Entry( total, i ) );
            }
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(new int[] {R.color.pie_chart_1, R.color.pie_chart_2, R.color.pie_chart_3,
                R.color.pie_chart_4, R.color.pie_chart_5, R.color.pie_chart_6}, this);

        PieData data = new PieData(xVals, dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    public void goBack(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(this, TravelActivity.class);
        intent.putExtra("travelId", travel.getId().toString());
        startActivity(intent);
        finish();
    }

    public void addExpense(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.putExtra("travelId", travel.getId().toString());
        startActivity(intent);
        finish();
    }



}
