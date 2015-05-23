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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        RelativeLayout travel = (RelativeLayout)getLayoutInflater().inflate(R.layout.travel, null);

        for (int i=0; i< travels.size(); i++) {
            View row = null;
            LayoutInflater inflater = this.getLayoutInflater();

            row = inflater.inflate(R.layout.travel, null);

            TextView name = (TextView) row.findViewById(R.id.name);
            TextView date = (TextView) row.findViewById(R.id.date);
            ImageView picture = (ImageView) row.findViewById(R.id.picture);

            name.setText(travels.get(i).travel.name);

            if (travels.get(i).travel.beginning != null && travels.get(i).travel.end != null)
                date.setText(sdf.format( travels.get(i).travel.beginning ) + " - " + sdf.format( travels.get(i).travel.end ));


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inSampleSize = 1;
            BitmapFactory.decodeFile(travels.get(i).travel.imageURI, options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            String imageType = options.outMimeType;

            picture.setImageBitmap(decodeSampledBitmapFromResource(travels.get(i).travel.imageURI, 300, 90));


            ((LinearLayout) travelsList).addView(row);
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
        LinearLayout nameContainerParent = (LinearLayout) ((RelativeLayout) view).getChildAt(1);
        LinearLayout nameContainer = (LinearLayout) nameContainerParent.getChildAt(0);

        String travelName = ((TextView) nameContainer.getChildAt(0)).getText().toString();

        Travel travel = Travel.find(Travel.class, "name = ?", travelName).get(0);
        Intent i = new Intent(TravelsActivity.this, TravelActivity.class);
        i.putExtra("travel_id", travel.getId().toString());
        startActivity(i);

    }

    public void addTravel(View view) {
        Intent i = new Intent(TravelsActivity.this, NewTravelActivity.class);
        startActivity(i);

    }

    public void remove(View view) {
        RelativeLayout travelField = (RelativeLayout) ((ImageView) view).getParent().getParent();
        LinearLayout travelList = (LinearLayout) travelField.getParent();
        travelList.removeView( travelField );

        LinearLayout nameContainerParent = (LinearLayout) travelField.getChildAt(1);
        LinearLayout nameContainer = (LinearLayout) nameContainerParent.getChildAt(0);

        String travelName = ((TextView) nameContainer.getChildAt(0)).getText().toString();

        Travel travel = Travel.find(Travel.class, "name = ?", travelName).get(0);

        List<TravelUser> users = travel.getParticipants();

        for (int i=0; i<users.size(); i++) {
            TravelUser travelUser = TravelUser.find(TravelUser.class,
                    "travel = ? and user = ?",
                    travel.getId().toString(),
                    users.get(i).user.getId().toString()).get(0);
            travelUser.delete();
        }

        travel.delete();

    }
}
