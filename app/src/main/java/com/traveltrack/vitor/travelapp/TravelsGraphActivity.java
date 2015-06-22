package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;


public class TravelsGraphActivity extends Activity {
    private BarChart mChart;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels_graph);

        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", 0);
        long userId = sharedPreferences.getLong("userId", 0);
        user = User.findById(User.class, userId);


        mChart = (BarChart) findViewById(R.id.chart);
        mChart.setDescription("");
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawValuesForWholeStack(false);

        setData();

        mChart.animateY(1000, Easing.EasingOption.EaseInOutQuad);

        setLegend();

    }

    public void setLegend() {

        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        int colorCodes[] = legend.getColors();

        LinearLayout legendContainer = (LinearLayout) findViewById(R.id.legend_container);
        View row = null;
        LayoutInflater inflater = this.getLayoutInflater();

        for (int i=0; i<colorCodes.length-1; i++) {
            if (i%2 == 0) {
                row = inflater.inflate(R.layout.legend_row, null);
            }

            View legendField = inflater.inflate(R.layout.legend, null);

            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            legendField.setLayoutParams(lp);

            ((TextView) legendField.findViewById(R.id.label)).setText( legend.getLabel(i) );
            (legendField.findViewById(R.id.color)).setBackgroundColor( colorCodes[i] );


            ((LinearLayout) row).addView(legendField);

            if (i%2 == 0) {
                legendContainer.addView(row);
            }

        }

    }


    private void setData() {

        List<Travel> travels = user.getTravels();
        List<Category> categories = Category.listAll(Category.class);
        String[] categoryLabels = new String[categories.size()];

        for (int i=0; i<categories.size(); i++) {
            categoryLabels[i] = categories.get(i).getPortName();

        }

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i<travels.size(); i++) {
            xVals.add(String.valueOf( travels.get(i).getName() ));
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < travels.size(); i++) {
            float[] values = new float[categories.size()];
            for (int j=0; j<categories.size(); j++) {
                values[j] = (float) travels.get(i).getTotalExpensesByCategoryInDefaultCurrency(categories.get(j));
            }
            yVals1.add(new BarEntry(values, i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "Despesas");
        set1.setColors(new int[] {R.color.pie_chart_1, R.color.pie_chart_2, R.color.pie_chart_3,
                R.color.pie_chart_4, R.color.pie_chart_5, R.color.pie_chart_6}, this);

        set1.setStackLabels(categoryLabels);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueFormatter(new DataValueFormater());
        data.setValueFormatter(new BarChartValueFormatter(user.getDefaultCurrency()));
        mChart.setData(data);
        mChart.invalidate();

    }




    public void goBack(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(this, TravelIndexActivity.class);
        startActivity(intent);
        finish();
    }

    public void onStart() {
        super.onStart();
        findViewById(R.id.view_graph).setBackgroundColor(getResources().getColor(R.color.light_green));
        findViewById(R.id.view_graph).setClickable(false);
    }
}
