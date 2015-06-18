package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.net.Uri.fromFile;


public class EditTravelActivity extends Activity {
    private User user;
    private Travel travel;
    private Uri selectedImageUri;
    private Date start;
    private Date end;
    private Currency currency;
    private String name;
    private TravelUser travelUser;
    private double budget;


    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_form);

        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", 0);
        long userId = sharedPreferences.getLong("userId", 0);
        user = User.findById(User.class, userId);

        Intent intent = getIntent();
        int travelId = Integer.parseInt( intent.getStringExtra("travelId") );
        travel = Travel.findById(Travel.class, travelId);

        travelUser = TravelUser.find(TravelUser.class, "user = ? and travel = ?", user.getId().toString(), travel.getId().toString()).get(0);

        name = travel.getName();
        budget = travelUser.getBudget();
        start = travel.getStart();
        end = travel.getEnd();
        currency = travel.getCurrency();
        selectedImageUri =  ( selectedImageUri == null ) ? null : Uri.parse( travel.getImageURI() );

        if ( !name.isEmpty() )
            ((EditText) findViewById(R.id.travel_name)).setText( name );

        ((EditText) findViewById(R.id.budget)).setText( String.valueOf( budget ) );

        ((TextView) findViewById(R.id.currency)).setText( currency.getCode() );

        if (start != null)
            ((TextView) findViewById(R.id.beginning)).setText( (sdf.format(start)) );

        if (end != null)
            ((TextView) findViewById(R.id.end)).setText( (sdf.format(end)) );

        if (selectedImageUri != null)
            ((ImageView) findViewById(R.id.picture)).setImageURI(selectedImageUri);

        ((Button) findViewById(R.id.submit)).setText(getResources().getString(R.string.update));
    }

    public void selectCurrency(View view) {
        final ArrayAdapter<Currency> adapter = new ArrayAdapter<Currency>(this, android.R.layout.simple_list_item_1);

        List<Currency> currencies = Currency.listAll(Currency.class);
        for (int i=0; i<currencies.size(); i++) {
            adapter.add(currencies.get(i));
        }

        new AlertDialog.Builder(this)
                .setTitle("Moeda")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String currencyCode = adapter.getItem(which).getCode();
                        currency = adapter.getItem(which);

                        ((TextView) findViewById(R.id.currency)).setText(currencyCode);

                        dialog.dismiss();
                    }
                }).create().show();
    }

    public void submit(View view) {
        name = ((EditText) findViewById(R.id.travel_name)).getText().toString();
        if (name.isEmpty()) {
            return;
        }

        String budgetText = ((EditText) findViewById(R.id.budget)).getText().toString();
        double budget = budgetText.isEmpty() ? 0 : Double.parseDouble(budgetText);

        travel.update(name,
                selectedImageUri == null ? null : selectedImageUri.toString(),
                start,
                end,
                currency, 1);

        Intent intent = new Intent(EditTravelActivity.this, TravelActivity.class);
        intent.putExtra("travelId", travel.getId().toString());
        startActivity(intent);
        finish();
    }

    public void getDate(final View view) {
        EditText myEditText = (EditText) findViewById(R.id.travel_name);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker dateView, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(view);
            }

        };

        new DatePickerDialog(EditTravelActivity.this, date,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel(View view) {
        Date date = myCalendar.getTime();
        TextView dateView = (TextView) ((LinearLayout) view).getChildAt(1);

        dateView.setText(sdf.format(date));

        if (dateView.getId() == R.id.beginning)
            start = date;
        else
            end = date;
    }

    public void addPicture(View v) {
        openImageIntent();
    }


    static final int SELECT_PICTURE = 1;
    private Uri outputFileUri;
    private void openImageIntent() {

        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String file_name = "img_"+ System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, file_name);
        outputFileUri = fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);

        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(pickIntent, "Select Image");
        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onSaveInstanceState(new Bundle());

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                if (isCamera) {
                    selectedImageUri =  Uri.parse(getPath(outputFileUri));
                } else {
                    selectedImageUri = Uri.parse(getPath(data.getData()));
                }
            }
        }

        if (selectedImageUri != null) {
            ((ImageView) findViewById(R.id.picture)).setImageURI(selectedImageUri);
        }

    }

    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    public void goBack(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(this, TravelActivity.class);
        intent.putExtra("travelId", travel.getId().toString());
        startActivity(intent);
        finish();
    }

    public void onStart() {
        super.onStart();
        findViewById(R.id.add_travel).setBackgroundColor(getResources().getColor(R.color.light_green));
        findViewById(R.id.add_travel).setClickable(false);
    }
}
