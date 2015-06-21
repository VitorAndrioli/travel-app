package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class TravelIndexActivity extends Activity {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.US);
    private RelativeLayout warning;
    private Travel travelToRemove;
    private RelativeLayout travelField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_index);

        findViewById(R.id.back).setVisibility(View.GONE);


        SharedPreferences shared_preferences = getSharedPreferences("USER_DATA", 0);
        long userId = shared_preferences.getLong("userId", 0);
        User user = User.findById(User.class, userId);

        warning = (RelativeLayout) findViewById(R.id.warning);

        List<Travel> travels = user.getTravels();

        LinearLayout travelsList = (LinearLayout) findViewById(R.id.travel_list);

        View row;
        LayoutInflater inflater = this.getLayoutInflater();

        for (int i=0; i< travels.size(); i++) {

            Travel travel = travels.get(i);
            row = inflater.inflate(R.layout.travel, null);
            row.setTag(travel.getId());

            TextView name = (TextView) row.findViewById(R.id.name);
            TextView date = (TextView) row.findViewById(R.id.date);
            ImageView picture = (ImageView) row.findViewById(R.id.picture);

            name.setText(travel.getName());

            if (travel.getStart() != null && travel.getEnd() != null)
                date.setText(sdf.format( travel.getStart() ) + " - " + sdf.format( travel.getEnd() ));
            else
                date.setVisibility(View.GONE);


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inSampleSize = 1;
            BitmapFactory.decodeFile(travel.getImageURI(), options);

            if (travel.getImageURI() != null) {
                picture.setImageBitmap(decodeSampledBitmapFromResource(travel.getImageURI(), 300, 90));
            }

            travelsList.addView(row);
        }

    }

    public static Bitmap decodeSampledBitmapFromResource(String string_uri, int reqWidth, int reqHeight) {

        Uri uri = Uri.parse(string_uri);
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(string_uri, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(string_uri, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void openTravel(View view) {
        long travelId = Long.valueOf( view.getTag().toString() );
        Travel travel = Travel.findById(Travel.class, travelId);
        Intent intent = new Intent(this, TravelActivity.class);
        intent.putExtra("travelId", travel.getId().toString());
        startActivity(intent);

    }

    public void removeTravel(View view) {
        warning.setVisibility(View.VISIBLE);

        travelField = (RelativeLayout) ((ImageView) view).getParent().getParent();
        long travelId = Long.valueOf(travelField.getTag().toString() );
        travelToRemove = Travel.findById(Travel.class, travelId);

    }

    public void confirmRemoval(View view) {

        LinearLayout travelList = (LinearLayout) findViewById(R.id.travel_list);
        travelList.removeView( travelField );

        travelToRemove.delete();
        warning.setVisibility(View.GONE);

    }

    public void cancelRemoval(View view) {
        travelToRemove = null;
        warning.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        ((ImageButton) findViewById(R.id.add_travel)).setBackgroundColor(getResources().getColor(R.color.dark_blue));
        ((ImageButton) findViewById(R.id.view_graph)).setBackgroundColor(getResources().getColor(R.color.dark_blue));
        super.onStop();
    }
}
