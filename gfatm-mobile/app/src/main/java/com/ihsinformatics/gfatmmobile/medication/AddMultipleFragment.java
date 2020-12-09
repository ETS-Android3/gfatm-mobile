package com.ihsinformatics.gfatmmobile.medication;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.commonlab.MyTitledEditText;
import com.ihsinformatics.gfatmmobile.commonlab.MyTitledSearchableSpinner;
import com.ihsinformatics.gfatmmobile.commonlab.TitledHeader;

public class AddMultipleFragment extends Fragment {

    private LinearLayout mainLayout;
    View[] views;
    Button btnCancel;
    Button btnSubmit;
    TitledHeader headerDrugSelection;
    MyTitledSearchableSpinner encounter;
    MyTitledSearchableSpinner drugSet;
    MyTitledEditText drugs;
    View getDrugsFrom;
    TitledHeader headerDrugList;

    MyMedicationInterface myMedicationInterface;

    public void onAttachToParentFragment(Fragment fragment) {
        myMedicationInterface = (MyMedicationInterface) fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainContent = inflater.inflate(R.layout.medicine_fragment, container, false);
        mainLayout = mainContent.findViewById(R.id.mainLayout);
        btnCancel = mainContent.findViewById(R.id.btnCancel);
        btnSubmit = mainContent.findViewById(R.id.btnSubmit);

        return mainContent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListeners();

        headerDrugSelection = new TitledHeader(getActivity(), "Drug Selection", "Add Multiple");
        getDrugsFrom = getActivity().getLayoutInflater().inflate(R.layout.layout_drugs_from, null);
        encounter = new MyTitledSearchableSpinner(getActivity(), "Encounter", getResources().getStringArray(R.array.dummy_items), null, false);
        drugSet = new MyTitledSearchableSpinner(getActivity(), "Drug Set", getResources().getStringArray(R.array.dummy_items), null, false);
        drugs = new MyTitledEditText(getActivity(), "Drugs", true);
        headerDrugList = new TitledHeader(getActivity(), "Drugs List", null);

        views = new View[]{headerDrugSelection, getDrugsFrom, encounter, drugSet, drugs, headerDrugList};
        for (View v: views)
            mainLayout.addView(v);
    }

    public void setListeners() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMedicationInterface.onCancelButtonClick();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Submit", Toast.LENGTH_SHORT).show();
            }
        });
    }
}