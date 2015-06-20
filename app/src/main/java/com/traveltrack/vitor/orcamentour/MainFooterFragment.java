package com.traveltrack.vitor.orcamentour;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class MainFooterFragment extends Fragment {
    private ImageButton viewGraph, addTravel;

    public MainFooterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_footer, container, false);

        viewGraph = (ImageButton) view.findViewById(R.id.view_graph);
        addTravel = (ImageButton) view.findViewById(R.id.add_travel);

        viewGraph.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewGraph();
            }
        });

        addTravel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTravel();
            }
        });

        return view;
    }


    public void viewGraph() {
        viewGraph.setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(getActivity(), TravelsGraphActivity.class);
        startActivity(intent);
    }

    public void addTravel() {
        addTravel.setBackgroundColor(getResources().getColor(R.color.light_green));
        Intent intent = new Intent(getActivity(), NewTravelActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        ((ImageButton) getActivity().findViewById(R.id.back)).setBackgroundColor(getResources().getColor(R.color.dark_blue));
        ((ImageButton) getActivity().findViewById(R.id.view_graph)).setBackgroundColor(getResources().getColor(R.color.dark_blue));
        ((ImageButton) getActivity().findViewById(R.id.add_travel)).setBackgroundColor(getResources().getColor(R.color.dark_blue));

        super.onStop();
    }
}
