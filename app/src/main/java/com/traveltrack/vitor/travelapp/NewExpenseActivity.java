package com.traveltrack.vitor.travelapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class NewExpenseActivity extends ActionBarActivity {
    Travel currentTravel;
    Category category;
    Date expenseDate;

    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        findViewById(R.id.add_expense).setBackgroundColor(getResources().getColor(R.color.light_green));
        findViewById(R.id.add_expense).setClickable(false);

        Intent intent = getIntent();
        int travelId = Integer.parseInt(intent.getStringExtra("travelId"));
        String categoryName = intent.getStringExtra("categoryName");
        category = Category.find(Category.class, "name = ?", categoryName).get(0);

        currentTravel = Travel.findById(Travel.class, travelId);

        expenseDate = new Date();
        ((TextView) findViewById(R.id.date)).setText(sdf.format(expenseDate));
        Uri imageUri = Uri.parse("android.resource://com.traveltrack.vitor.travelapp/drawable/" + categoryName + "_big");
        ((ImageView) findViewById(R.id.picture)).setImageURI(imageUri);

    }

    public void submit(View view) {
        String valueField = ((EditText) findViewById(R.id.value)).getText().toString();
        double value = valueField.isEmpty() ? 0 : Double.parseDouble(valueField);

        String description = ((EditText) findViewById(R.id.description)).getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", 0);
        long userId = sharedPreferences.getLong("userId", 0);
        User user = User.findById(User.class, userId);

        Expense expense = new Expense(value, description, category, user, currentTravel, expenseDate);

        expense.save();

        Intent intent = new Intent(this, TravelActivity.class);
        intent.putExtra("travelId", currentTravel.getId().toString());
        startActivity(intent);

    }

    public void getDate(final View view) {
        EditText myEditText = (EditText) findViewById(R.id.value);
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker dateView, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                expenseDate = myCalendar.getTime();

                ((TextView) findViewById(R.id.date)).setText(sdf.format(expenseDate));

            }

        };

        new DatePickerDialog(this, date,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void goBack(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.putExtra("travelId", currentTravel.getId().toString());
        startActivity(intent);
        finish();
    }

    public void viewGraph(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(this, ExpensesGraphActivity.class);
        intent.putExtra("travelId", currentTravel.getId().toString());
        startActivity(intent);
    }



}
