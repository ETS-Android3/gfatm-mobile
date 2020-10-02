package com.ihsinformatics.gfatmmobile;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.custom.SectionLabTest;
import com.ihsinformatics.gfatmmobile.custom.TitledHeader;
import com.ihsinformatics.gfatmmobile.custom.TitledSearchableSpinner;
import com.ihsinformatics.gfatmmobile.model.Encounter;

public class AddTestFragment extends Fragment implements SectionLabTest.MyValuesInterface {

    LinearLayout mainLayout;
    View[] views;
    TitledSearchableSpinner encounterSpinner;
    TitledHeader header;
    SectionLabTest bacteriologySection;
    SectionLabTest biochemistrySection;
    SectionLabTest cardiologySection;
    SectionLabTest hematologySection;
    Button btnCancel;

    MyButtonsInterface myButtonsInterface;

    public void onAttachToParentFragment(Fragment fragment){
        myButtonsInterface = (MyButtonsInterface) fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mainContent = inflater.inflate(R.layout.add_test_fragment, container, false);
        mainLayout = mainContent.findViewById(R.id.mainLayout);
        btnCancel = mainContent.findViewById(R.id.btnCancel);

        return mainContent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        header = new TitledHeader(getActivity(), "Add Tests", null);
        encounterSpinner = new TitledSearchableSpinner(getActivity(), "Encounter", getResources().getStringArray(R.array.test_encounters), null, true);

        String[][] tests = {{"AFB Culture", "2020-09-15 10:33:35"},
                {"AFB Culture2", "2020-09-15 10:33:35"},
                {"AFB Culture3", "2020-09-15 10:33:35"}};
        bacteriologySection = new SectionLabTest(getActivity(), "BACTERIOLOGY", tests,  (Fragment) this );
        biochemistrySection = new SectionLabTest(getActivity(), "BIOCHEMISTRY", tests, (Fragment) this);
        cardiologySection = new SectionLabTest(getActivity(), "CARDIOLOGY", tests, (Fragment) this);
        hematologySection = new SectionLabTest(getActivity(), "HEMATOLOGY", tests, (Fragment) this);

        views = new View[]{header, encounterSpinner, bacteriologySection, biochemistrySection, cardiologySection, hematologySection};
        for (View v : views)
            mainLayout.addView(v);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myButtonsInterface.cancel();
            }
        });

    }

    @Override
    public String getEncounterName() {
        return encounterSpinner.getSpinnerSelectedItem();
    }

    public interface MyButtonsInterface {
        void cancel();
    }

}
