package com.traveltrack.vitor.travelapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class TravelFooterFragment extends Fragment {
    public TravelFooterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel_footer, container, false);

        return view;
    }

    @Override
    public void onStop() {
        ((ImageButton) getActivity().findViewById(R.id.back)).setBackgroundColor(getResources().getColor(R.color.dark_blue));
        ((ImageButton) getActivity().findViewById(R.id.view_graph)).setBackgroundColor(getResources().getColor(R.color.dark_blue));
        ((ImageButton) getActivity().findViewById(R.id.add_expense)).setBackgroundColor(getResources().getColor(R.color.dark_blue));

        super.onStop();
    }



}
