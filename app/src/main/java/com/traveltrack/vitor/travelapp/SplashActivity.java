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
    private static String DATABASE_NAME = "travelapp.db";
    public final static String DATABASE_PATH = "/data/data/com.traveltrack.vitor.travelapp/databases/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (!checkDataBase()) {
            DatabaseCreator dbCreator = new DatabaseCreator();
            dbCreator.createDatabase(this);
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

                SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", 0);
                long userId = sharedPreferences.getLong("userId", -1);
                User user = User.findById(User.class, userId);

                if (user != null) {
                    intent = new Intent(SplashActivity.this, TravelIndexActivity.class);
                }

                startActivity(intent);
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
}
