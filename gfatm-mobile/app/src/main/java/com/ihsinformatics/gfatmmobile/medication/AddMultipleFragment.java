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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.commonlab.MyTitledSearchableSpinner;
import com.ihsinformatics.gfatmmobile.commonlab.TitledHeader;

import java.util.ArrayList;
import java.util.List;

public class AddMultipleFragment extends Fragment implements DrugAdapter.MyDrugInterface {

    private LinearLayout mainLayout;
    View[] views;
    Button btnCancel;
    Button btnSubmit;
    TitledHeader headerDrugSelection;
    MyTitledSearchableSpinner encounter;
    MyTitledSearchableSpinner drugSet;
    MyTitledSearchableSpinner drugsList;
    View getDrugsFrom;
    View addDrug;
    TitledHeader headerDrugList;
    List drugs = new ArrayList<Drug>();

    RecyclerView rvDrugs;
    DrugAdapter adapter;

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
        drugsList = new MyTitledSearchableSpinner(getActivity(), "Drugs", getResources().getStringArray(R.array.dummy_items_2), null, true);
        addDrug = getActivity().getLayoutInflater().inflate(R.layout.layout_button_add_drug, null);

        headerDrugList = new TitledHeader(getActivity(), "Drugs List", null);
        rvDrugs = new RecyclerView(getActivity());

        views = new View[]{headerDrugSelection, getDrugsFrom, encounter, drugSet, drugsList, addDrug, headerDrugList, rvDrugs};
        for (View v : views)
            mainLayout.addView(v);

        rvDrugs.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DrugAdapter(getActivity(), drugs);
        adapter.onAttachToParentFragment(AddMultipleFragment.this);
        rvDrugs.setAdapter(adapter);

        addDrug.findViewById(R.id.btnAddDrug).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drug newDrug = new Drug();
                newDrug.setName(drugsList.getSpinnerSelectedItem());
                drugs.add(newDrug);
                Toast.makeText(getActivity(), drugsList.getSpinnerSelectedItem() + " drug added.", Toast.LENGTH_SHORT).show();
                updateDrugList();
            }
        });

        updateDrugList();

    }

    void updateDrugList() {
        headerDrugList.setVisibility(drugs.size() > 0 ? View.VISIBLE : View.GONE);
        rvDrugs.setVisibility(drugs.size() > 0 ? View.VISIBLE : View.GONE);
        adapter.notifyDataSetChanged();
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

    @Override
    public void updateDrugsList() {
        updateDrugList();
    }
}