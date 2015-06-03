package com.traveltrack.vitor.travelapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HeaderFragment extends Fragment {
public HeaderFragment() {
        // Required empty public constructor
    }
    private LinearLayout userNameContainer, userNameContainerActive, userMenu;
    private TextView userName, userNameActive, profile, logout;
    private User user;

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

            }
        });

        return view;
    }

    public void logout() {
        SharedPreferences shared_preferences = getActivity().getSharedPreferences("USER_DATA", 0);
        SharedPreferences.Editor editor = shared_preferences.edit();
        editor.remove("user_id");
        editor.commit();

        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }

    @Override
    public void onPause() {
        userNameContainer.setVisibility(View.VISIBLE);
        userMenu.setVisibility(View.GONE);
        super.onPause();
    }

}
