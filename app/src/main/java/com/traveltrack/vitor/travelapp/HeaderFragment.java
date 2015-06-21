package com.traveltrack.vitor.travelapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HeaderFragment extends Fragment {

    private LinearLayout userNameContainer;
    private LinearLayout userNameContainerActive;
    private LinearLayout userMenu;
    private TextView userName;
    private TextView userNameActive;
    private TextView profile;
    private TextView logout;
    private ImageView logo;
    private User user;
    private ImageView userPicture;
    private ImageView userPictureActive;

    public HeaderFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_header, container, false);

        SharedPreferences shared_preferences = getActivity().getSharedPreferences("USER_DATA", 0);
        long userId = shared_preferences.getLong("userId", 0);
        user = User.findById(User.class, userId);

        userName = (TextView) view.findViewById(R.id.user_name);
        userNameActive = (TextView) view.findViewById(R.id.user_name_active);
        userNameContainer = (LinearLayout) view.findViewById(R.id.user_name_container);
        userNameContainerActive = (LinearLayout) view.findViewById(R.id.user_name_container_active);
        userMenu = (LinearLayout) view.findViewById(R.id.user_menu);
        profile = (TextView) view.findViewById(R.id.profile);
        logout = (TextView) view.findViewById(R.id.logout);
        logo = (ImageView) view.findViewById(R.id.logo);
        userPicture = (ImageView) view.findViewById(R.id.user_picture);
        userPictureActive = (ImageView) view.findViewById(R.id.user_picture_active);


        if (user.getImageURI() != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inSampleSize = 1;
            BitmapFactory.decodeFile(user.getImageURI(), options);

            Bitmap bitmap = decodeSampledBitmapFromResource(user.getImageURI(), 81, 92);

            userPicture.setImageBitmap( bitmap );
            userPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);


            userPictureActive.setImageBitmap( bitmap );
            userPictureActive.setScaleType(ImageView.ScaleType.CENTER_CROP);

        }

        userName.setText(user.getName());
        userNameActive.setText(user.getName());
        userNameContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameContainer.setVisibility(View.GONE);
                userMenu.setVisibility(View.VISIBLE);
            }
        });

        userNameContainerActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameContainer.setVisibility(View.VISIBLE);
                userMenu.setVisibility(View.GONE);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile();
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome();
            }
        });

        return view;
    }

    public void goToHome() {
        Intent intent = new Intent(getActivity(), TravelIndexActivity.class);
        startActivity(intent);
        getActivity().finish();

    }

    public void goToProfile() {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        startActivity(intent);
    }


    public void logout() {
        SharedPreferences shared_preferences = getActivity().getSharedPreferences("USER_DATA", 0);
        SharedPreferences.Editor editor = shared_preferences.edit();
        editor.remove("user_id");
        editor.commit();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        userNameContainer.setVisibility(View.VISIBLE);
        userMenu.setVisibility(View.GONE);
        super.onPause();
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


}
