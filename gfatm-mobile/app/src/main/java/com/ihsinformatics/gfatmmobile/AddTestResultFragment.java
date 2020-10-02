package com.ihsinformatics.gfatmmobile;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.custom.MyTitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledHeader;
import com.ihsinformatics.gfatmmobile.custom.TitledSearchableSpinner;

public class AddTestResultFragment extends Fragment {

    LinearLayout mainLayout;
    TitledHeader header;
    MyTitledEditText testLabId;
    TitledSearchableSpinner mediumType;
    MyTitledEditText otherMediumType;
    TitledSearchableSpinner result;

    View[] views;
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

        header = new TitledHeader(getActivity(), "Add Test Result", "AFB Culture");
        testLabId = new MyTitledEditText(getActivity(), "Test Lab ID", true);
        mediumType = new TitledSearchableSpinner(getActivity(), "Medium Type", getResources().getStringArray(R.array.test_encounters), null, true);
        otherMediumType = new MyTitledEditText(getActivity(), "Other Medium Type", false);
        result = new TitledSearchableSpinner(getActivity(), "Result", getResources().getStringArray(R.array.test_encounters), null, true);
        View attachmentView = getActivity().getLayoutInflater().inflate(R.layout.layout_attachment, null);

        views = new View[]{header, testLabId, mediumType, otherMediumType, result, attachmentView};
        for (View v : views)
            mainLayout.addView(v);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myButtonsInterface.cancel();
            }
        });
    }

    public interface MyButtonsInterface {
        void cancel();
    }

}
