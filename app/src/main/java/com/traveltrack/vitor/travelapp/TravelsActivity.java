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


public class TravelsActivity extends Activity {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels);

        SharedPreferences shared_preferences = getSharedPreferences("USER_DATA", 0);
        long userId = shared_preferences.getLong("userId", 0);
        User user = User.findById(User.class, userId);

        List<TravelUser> travels = user.getTravels();

        LinearLayout travelsList = (LinearLayout) findViewById(R.id.travel_list);

        View row;
        LayoutInflater inflater = this.getLayoutInflater();

        for (int i=0; i< travels.size(); i++) {

            row = inflater.inflate(R.layout.travel, null);

            TextView name = (TextView) row.findViewById(R.id.name);
            TextView date = (TextView) row.findViewById(R.id.date);
            ImageView picture = (ImageView) row.findViewById(R.id.picture);
            Travel travel = travels.get(i).getTravel();

            name.setText(travel.getName());

            if (travel.getStart() != null && travel.getEnd() != null)
                date.setText(sdf.format( travel.getStart() ) + " - " + sdf.format( travel.getEnd() ));


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inSampleSize = 1;
            BitmapFactory.decodeFile(travel.getImageURI(), options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            String imageType = options.outMimeType;

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
        LinearLayout nameContainerParent = (LinearLayout) ((RelativeLayout) view).getChildAt(1);
        LinearLayout nameContainer = (LinearLayout) nameContainerParent.getChildAt(2);

        String travelName = ((TextView) nameContainer.getChildAt(0)).getText().toString();

        Travel travel = Travel.find(Travel.class, "name = ?", travelName).get(0);
        Intent intent = new Intent(this, TravelActivity.class);
        intent.putExtra("travelId", travel.getId().toString());
        startActivity(intent);

    }

    public void addTravel(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(this, NewTravelActivity.class);
        startActivity(intent);

    }

    public void viewGraph(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(this, TravelsGraphActivity.class);
        startActivity(intent);

    }

    public void remove(View view) {
        RelativeLayout travelField = (RelativeLayout) ((ImageView) view).getParent().getParent();
        LinearLayout travelList = (LinearLayout) travelField.getParent();
        travelList.removeView( travelField );

        LinearLayout nameContainerParent = (LinearLayout) travelField.getChildAt(1);
        LinearLayout nameContainer = (LinearLayout) nameContainerParent.getChildAt(2);

        String travelName = ((TextView) nameContainer.getChildAt(0)).getText().toString();

        Travel travel = Travel.find(Travel.class, "name = ?", travelName).get(0);

        List<TravelUser> users = travel.getParticipants();

        for (int i=0; i<users.size(); i++) {
            TravelUser travelUser = TravelUser.find(TravelUser.class,
                    "travel = ? and user = ?",
                    travel.getId().toString(),
                    users.get(i).getUser().getId().toString()).get(0);
            travelUser.delete();
        }

        List<Expense> expenses = travel.getExpenses();
        for (int i=0; i<expenses.size(); i++) {
            expenses.get(i).delete();
        }
        travel.delete();

    }

    @Override
    public void onStop() {
        ((ImageButton) findViewById(R.id.add_travel)).setBackgroundColor(getResources().getColor(R.color.dark_blue));
        ((ImageButton) findViewById(R.id.view_graph)).setBackgroundColor(getResources().getColor(R.color.dark_blue));
        super.onStop();
    }
}
