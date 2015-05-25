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

    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        Intent intent = getIntent();
        int travel_id = Integer.parseInt(intent.getStringExtra("travelId"));
        String categoryName = intent.getStringExtra("categoryName");
        category = Category.find(Category.class, "name = ?", categoryName).get(0);

        currentTravel = Travel.findById(Travel.class, travel_id);

        ((TextView) findViewById(R.id.date)).setText(sdf.format(new Date()));
        Uri imageUri = Uri.parse("android.resource://com.traveltrack.vitor.travelapp/drawable/" + categoryName + "_big");
        ((ImageView) findViewById(R.id.picture)).setImageURI(imageUri);

    }

    public void create(View view) {
        String valueField = ((EditText) findViewById(R.id.value)).getText().toString();
        double value = valueField.isEmpty() ? 0 : Double.parseDouble(valueField);

        String description = ((EditText) findViewById(R.id.description)).getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", 0);
        long userId = sharedPreferences.getLong("user_id", 0);
        User user = User.findById(User.class, userId);

        Expense expense = new Expense(value, description, category, user, currentTravel, new Date());

        expense.save();

        Intent intent = new Intent(this, TravelActivity.class);
        intent.putExtra("travel_id", currentTravel.getId().toString());
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

                Date date = myCalendar.getTime();

                ((TextView) findViewById(R.id.date)).setText(sdf.format(date));

            }

        };

        new DatePickerDialog(this, date,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

}
