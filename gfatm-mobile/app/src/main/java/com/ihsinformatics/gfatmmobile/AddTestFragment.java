package com.ihsinformatics.gfatmmobile;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.custom.SectionLabTest;
import com.ihsinformatics.gfatmmobile.custom.TitledHeader;
import com.ihsinformatics.gfatmmobile.custom.TitledSearchableSpinner;
import com.ihsinformatics.gfatmmobile.model.Encounter;

public class AddTestFragment extends Fragment {

    LinearLayout mainLayout;
    View[] views;
    TitledSearchableSpinner encounterSpinner;
    TitledHeader header;
    SectionLabTest bacteriologySection;
    SectionLabTest biochemistrySection;
    SectionLabTest cardiologySection;
    SectionLabTest hematologySection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mainContent = inflater.inflate(R.layout.add_test_fragment, container, false);
        mainLayout = mainContent.findViewById(R.id.mainLayout);
        return mainContent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        header = new TitledHeader(getActivity(), "Add Tests");
        encounterSpinner = new TitledSearchableSpinner(getActivity(), "Encounter", getResources().getStringArray(R.array.test_encounters), null, true);

        String[][] data = {{"", "", ""}, {"", "", ""}, {"", "", ""}};
        bacteriologySection = new SectionLabTest(getActivity(), "BACTERIOLOGY", data);
        biochemistrySection = new SectionLabTest(getActivity(), "BIOCHEMISTRY", data);
        cardiologySection = new SectionLabTest(getActivity(), "CARDIOLOGY", data);
        hematologySection = new SectionLabTest(getActivity(), "HEMATOLOGY", data);

        views = new View[]{header, encounterSpinner, bacteriologySection, biochemistrySection, cardiologySection, hematologySection};
        for (View v : views)
            mainLayout.addView(v);

    }

}
