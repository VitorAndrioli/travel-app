package com.traveltrack.vitor.orcamentour;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class EditExpenseActivity extends Activity {
    private Expense expense;
    private Travel currentTravel;
    private Category category;
    private User user;
    private Date expenseDate;
    private double value;
    private String description;

    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_form);

        Intent intent = getIntent();
        int expenseId = Integer.parseInt(intent.getStringExtra("expenseId"));
        expense = Expense.findById(Expense.class, expenseId);

        currentTravel = expense.getTravel();
        category = expense.getCategory();
        user = expense.getUser();

        value = expense.getValue();
        expenseDate = expense.getDate();
        description = expense.getDescription();

        if (value > 0)
            ((EditText) findViewById(R.id.value)).setText( String.valueOf( value ) );
        ((TextView) findViewById(R.id.date)).setText(sdf.format(expenseDate));
        ((EditText) findViewById(R.id.description)).setText( description );

        Uri imageUri = Uri.parse("android.resource://com.traveltrack.vitor.travelapp/drawable/" + category.getName() + "_big");
        ((ImageView) findViewById(R.id.picture)).setImageURI(imageUri);

        ((Button) findViewById(R.id.submit)).setText(getResources().getString(R.string.update));

    }

    public void submit(View view) {
        String valueField = ((EditText) findViewById(R.id.value)).getText().toString();
        value = valueField.isEmpty() ? 0 : Double.parseDouble(valueField);

        description = ((EditText) findViewById(R.id.description)).getText().toString();

        expense.update(value, description, category, user, currentTravel, expenseDate);

        Intent intent = new Intent(this, TravelActivity.class);
        intent.putExtra("travelId", currentTravel.getId().toString());
        startActivity(intent);
        finish();

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
        Intent intent = new Intent(this, TravelActivity.class);
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

    public void onStart() {
        super.onStart();
        findViewById(R.id.add_expense).setBackgroundColor(getResources().getColor(R.color.light_green));
        findViewById(R.id.add_expense).setClickable(false);
    }
}
