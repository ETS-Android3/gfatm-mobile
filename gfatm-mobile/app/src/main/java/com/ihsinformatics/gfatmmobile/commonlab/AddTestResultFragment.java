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
import com.ihsinformatics.gfatmmobile.commonlab.MyTitledEditText;
import com.ihsinformatics.gfatmmobile.commonlab.TitledHeader;
import com.ihsinformatics.gfatmmobile.commonlab.MyTitledSearchableSpinner;

public class AddTestResultFragment extends Fragment {

    LinearLayout mainLayout;
    TitledHeader header;
    MyTitledEditText testLabId;
    MyTitledSearchableSpinner mediumType;
    MyTitledEditText otherMediumType;
    MyTitledSearchableSpinner result;

    View[] views;
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

        header = new TitledHeader(getActivity(), "Add Test Result", "AFB Culture");
        testLabId = new MyTitledEditText(getActivity(), "Test Lab ID", true);
        mediumType = new MyTitledSearchableSpinner(getActivity(), "Medium Type", getResources().getStringArray(R.array.dummy_items), null, true);
        otherMediumType = new MyTitledEditText(getActivity(), "Other Medium Type", false);
        result = new MyTitledSearchableSpinner(getActivity(), "Result", getResources().getStringArray(R.array.dummy_items), null, true);
        View attachmentView = getActivity().getLayoutInflater().inflate(R.layout.lab_layout_attachment, null);
        attachmentView.findViewById(R.id.btnAttachFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Attach file", Toast.LENGTH_SHORT).show();
            }
        });

        views = new View[]{header, testLabId, mediumType, otherMediumType, result, attachmentView};
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

}
