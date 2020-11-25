package com.ihsinformatics.gfatmmobile.commonlab;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ihsinformatics.gfatmmobile.MyLabInterface;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.commonlab.MyTitledEditText;
import com.ihsinformatics.gfatmmobile.commonlab.TitledHeader;
import com.ihsinformatics.gfatmmobile.commonlab.MyTitledSearchableSpinner;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Attribute;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestOrder;

import java.util.List;

public class AddTestResultFragment extends Fragment {

    private LinearLayout mainLayout;
    private TitledHeader header;
    private MyTitledEditText testLabId;
    private MyTitledSearchableSpinner mediumType;
    private MyTitledEditText otherMediumType;
    private MyTitledSearchableSpinner result;
    private TestOrder testOrder;

    View[] views;
    Button btnCancel;
    Button btnSubmit;

    public void setTestOrder(TestOrder testOrder) {
        this.testOrder = testOrder;
    }

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
        String testTitle = testOrder==null?"Test Title": testOrder.getLabTestType().getName();
        header = new TitledHeader(getActivity(), "Add Test Result", testOrder.getLabTestType().getName());
        setListeners();

        List<Attribute> attributes = testOrder.getAttributes();
        views = new View[attributes.size()+1];
        views[0] = header;
        int i = 1;
        for(Attribute a: attributes) {
            if(a.getAttributeType().getDatatypeClassname().contains("FreeTextDatatype")) {
                views[i] = new MyTitledEditText(getActivity(), a.getAttributeType().getName(), true);
            } else if(a.getAttributeType().getDatatypeClassname().contains("Concept")) {
                views[i] = new MyTitledSearchableSpinner(getActivity(), a.getAttributeType().getName(), getResources().getStringArray(R.array.dummy_items), null, true);
            }
            i++;
        }

        View attachmentView = getActivity().getLayoutInflater().inflate(R.layout.lab_layout_attachment, null);
        attachmentView.findViewById(R.id.btnAttachFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Attach file", Toast.LENGTH_SHORT).show();
            }
        });

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
