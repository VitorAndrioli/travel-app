package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {
    private static String DATABASE_NAME = "orcamentour.db";
    public final static String DATABASE_PATH = "/data/data/br.com.imabrasil.alergiadrogas/databases/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        if (!checkDataBase()) {
            createDataBase();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", 0);
        long userId = sharedPreferences.getLong("userId", 0);
        final User user = User.findById(User.class, userId);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);

                if (user != null)
                    i = new Intent(SplashActivity.this, TravelIndexActivity.class);

                startActivity(i);
                finish();

            }
        }, 500);
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        boolean exist = false;
        try {
            String dbPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.v("db log", "database does't exist");
        }

        if (checkDB != null) {
            exist = true;
            checkDB.close();
            Log.v("db log", "database exists");
        }
        return exist;
    }

    public void createDataBase() {
        String[] categoryPortNames = {"passagem", "acomodação", "transporte", "alimentação", "laser", "outros"};
        String[] categoryNames = {"passage", "accommodation", "transportation", "alimentation", "leisure", "other"};

        for (int i=0; i< categoryNames.length; i++) {
            String uri = "android.resource://com.traveltrack.vitor.travelapp/drawable/" + categoryNames[i];
            Category category = new Category(categoryNames[i], categoryPortNames[i], uri);
            category.save();
        }

        String[] currencyNames = {"Real", "Dolar", "Euro", "Libra", "Peso Argentino"};
        String[] currencySymbols = {"R$", "U$", "€", "£", "$"};

        for (int i=0; i< currencyNames.length; i++) {
            Currency currency = new Currency(currencyNames[i], currencySymbols[i]);
            currency.save();
        }

    }
}
