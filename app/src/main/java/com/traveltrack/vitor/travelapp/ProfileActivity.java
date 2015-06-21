package com.traveltrack.vitor.travelapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.net.Uri.fromFile;
import static android.net.Uri.parse;


public class ProfileActivity extends Activity {
    private EditText nameField;
    private EditText emailField;
    private EditText currentPasswordField;
    private EditText passwordField;
    private EditText passwordConfirmationField;
    private User user;
    private Uri selectedImageUri;
    private Currency defaultCurrency;
    private TextView defaultCurrencyField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", 0);
        long userId = sharedPreferences.getLong("userId", 0);
        user = User.findById(User.class, userId);
        defaultCurrency = user.getDefaultCurrency();
        if ( user.getImageURI() != null )
            selectedImageUri = Uri.parse( user.getImageURI() );

        nameField = (EditText) findViewById(R.id.user_name);
        emailField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);
        passwordConfirmationField = (EditText) findViewById(R.id.password_confirmation);
        currentPasswordField = (EditText) findViewById(R.id.current_password);
        defaultCurrencyField = (TextView) findViewById(R.id.currency);

        nameField.setText( user.getName() );
        emailField.setText( user.getEmail() );
        if (user.getImageURI() != null) {
            ((ImageView) findViewById(R.id.picture)).setImageURI(parse(user.getImageURI()));
            ((ImageView) findViewById(R.id.picture)).setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        defaultCurrencyField.setText( getResources().getString(R.string.default_currency) + ": " + defaultCurrency.getCode() );

    }

    public void submit(View view) {
        if (user != null) {

            user.update(
                    nameField.getText().toString(),
                    emailField.getText().toString(),
                    String.valueOf( selectedImageUri ),
                    defaultCurrency
            );

            String password = currentPasswordField.getText().toString();
            String newPassword = passwordField.getText().toString();
            String passwordConfirmation = passwordConfirmationField.getText().toString();

            if ( password.equals( user.getPassword() ) && newPassword.equals( passwordConfirmation ) ) {
                user.setPassword( newPassword );
                user.save();
            }

        }

        finish();
        startActivity( getIntent() );
    }

    public void changePicture(View v) {
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
            ((ImageView) findViewById(R.id.picture)).setScaleType(ImageView.ScaleType.CENTER_CROP);
            ((ImageView) findViewById(R.id.picture)).setImageURI(selectedImageUri);
        }

    }

    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
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

                        defaultCurrency = adapter.getItem(which);

                        defaultCurrencyField.setText( getResources().getString(R.string.default_currency) + ": " + defaultCurrency.getCode() );

                        dialog.dismiss();
                    }
                }).create().show();
    }



    public void goBack(View view) {
        Intent intent = new Intent(this, TravelIndexActivity.class);
        startActivity(intent);
        finish();
    }
}
