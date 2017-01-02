package com.ihsinformatics.gfatmmobile;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Rabbia on 11/10/2016.
 */

public class SearchFragment extends Fragment implements View.OnClickListener {

    LinearLayout searchFormLayout;
    LinearLayout searchResultLayout;
    Button searchPatientButton;
    Button searchPatientAgainButton;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment

        View mainContent = inflater.inflate(R.layout.search_fragment, container, false);
        searchPatientButton = (Button) mainContent.findViewById(R.id.searchPatient);
        searchPatientAgainButton = (Button) mainContent.findViewById(R.id.searchAgainPatient);
        searchFormLayout = (LinearLayout) mainContent.findViewById(R.id.searchForm);
        searchResultLayout = (LinearLayout) mainContent.findViewById(R.id.searchResult);

        searchPatientButton.setOnClickListener(this);
        searchPatientAgainButton.setOnClickListener(this);

        return mainContent;
    }

    @Override
    public void onClick(View v) {

        if(v == searchPatientButton){
            searchFormLayout.setVisibility(View.GONE);
            searchResultLayout.setVisibility(View.VISIBLE);
        }
        else if (v == searchPatientAgainButton){
            searchFormLayout.setVisibility(View.VISIBLE);
            searchResultLayout.setVisibility(View.GONE);
        }

    }
}