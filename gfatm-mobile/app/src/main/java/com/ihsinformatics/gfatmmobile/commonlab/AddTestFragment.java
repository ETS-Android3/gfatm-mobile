package com.ihsinformatics.gfatmmobile.commonlab;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.MyLabInterface;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.commonlab.ExpandableLayout;
import com.ihsinformatics.gfatmmobile.commonlab.TitledHeader;
import com.ihsinformatics.gfatmmobile.commonlab.MyTitledSearchableSpinner;

public class AddTestFragment extends Fragment implements MyLabInterface {

    LinearLayout mainLayout;
    View[] views;
    MyTitledSearchableSpinner encounterSpinner;
    TitledHeader header;
    ExpandableLayout bacteriologySection;
    ExpandableLayout biochemistrySection;
    ExpandableLayout cardiologySection;
    ExpandableLayout hematologySection;
    Button btnCancel;
    Button btnSubmit;

    MyLabInterface myLabInterface;

    public void onAttachToParentFragment(Fragment fragment) {
        myLabInterface = (MyLabInterface) fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mainContent = inflater.inflate(R.layout.lab_add_test_fragment, container, false);
        mainLayout = mainContent.findViewById(R.id.mainLayout);
        btnCancel = mainContent.findViewById(R.id.btnCancel);
        btnSubmit = mainContent.findViewById(R.id.btnSubmit);

        return mainContent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListeners();

        header = new TitledHeader(getActivity(), getString(R.string.add_test), null);
        encounterSpinner = new MyTitledSearchableSpinner(getActivity(), getString(R.string.encounter), getResources().getStringArray(R.array.dummy_items), null, true);

        String[][] tests = {{"AFB Culture", "2020-09-15 10:33:35"},
                {"AFB Culture2", "2020-09-15 10:33:35"},
                {"AFB Culture3", "2020-09-15 10:33:35"}};
        bacteriologySection = new ExpandableLayout(getActivity(), "BACTERIOLOGY", tests, (Fragment) this);
        biochemistrySection = new ExpandableLayout(getActivity(), "BIOCHEMISTRY", tests, (Fragment) this);
        cardiologySection = new ExpandableLayout(getActivity(), "CARDIOLOGY", tests, (Fragment) this);
        hematologySection = new ExpandableLayout(getActivity(), "HEMATOLOGY", tests, (Fragment) this);

        views = new View[]{header, encounterSpinner, bacteriologySection, biochemistrySection, cardiologySection, hematologySection};
        for (View v : views)
            mainLayout.addView(v);
    }

    public void setListeners() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLabInterface.onCancelButtonClick();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Submit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAddResultButtonClick() {
    }

    @Override
    public void onCancelButtonClick() {
    }

    @Override
    public String getEncounterName() {
        return encounterSpinner.getSpinnerSelectedItem();
    }
}
