package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class TravelsActivity extends Activity {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels);

        SharedPreferences shared_preferences = getSharedPreferences("USER_DATA", 0);
        long userId = shared_preferences.getLong("user_id", 0);
        User user = User.findById(User.class, userId);

        List<TravelUser> travels = user.getTravels();

        View travelsList = findViewById(R.id.travel_list);
        LinearLayout travel = (LinearLayout)getLayoutInflater().inflate(R.layout.travel, null);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        travel.setLayoutParams(lp);

        View row = null;
        LayoutInflater inflater = this.getLayoutInflater();

        for (int i=0; i< travels.size(); i++) {
            row = inflater.inflate(R.layout.travel, null);

            TextView name = (TextView) row.findViewById(R.id.name);
            TextView date = (TextView) row.findViewById(R.id.date);
            ImageView picture = (ImageView) row.findViewById(R.id.picture);

            name.setText(travels.get(i).travel.name);
            date.setText(sdf.format( travels.get(i).travel.beginning ) + " - " + sdf.format( travels.get(i).travel.end ));
            picture.setImageURI(Uri.parse(travels.get(i).travel.imageURI));

            ((LinearLayout) travelsList).addView(row);
        }

    }

    public void openTravel(View view) {
        LinearLayout travelField = (LinearLayout) ((LinearLayout) view).getChildAt(1);


        String travelName = ((TextView) travelField.getChildAt(0)).getText().toString();

        Travel travel = Travel.find(Travel.class, "name = ?", travelName).get(0);
        Intent i = new Intent(TravelsActivity.this, TravelActivity.class);
        i.putExtra("travel_id", travel.getId().toString());
        startActivity(i);

    }

    public void addTravel(View view) {
        Intent i = new Intent(TravelsActivity.this, NewTravelActivity.class);
        startActivity(i);

    }

    public void logout(View v) {
        SharedPreferences shared_preferences = getSharedPreferences("USER_DATA", 0);
        SharedPreferences.Editor editor = shared_preferences.edit();
        editor.remove("user_id");
        editor.commit();

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}
