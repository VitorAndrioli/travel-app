package com.traveltrack.vitor.orcamentour;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
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

}
