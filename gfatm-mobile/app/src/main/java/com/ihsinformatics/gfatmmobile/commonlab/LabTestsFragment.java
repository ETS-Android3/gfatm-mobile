package com.ihsinformatics.gfatmmobile.commonlab;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ihsinformatics.gfatmmobile.MyLabInterface;
import com.ihsinformatics.gfatmmobile.R;

public class LabTestsFragment extends Fragment implements MyLabInterface {

    String testType;
    LabTestAdapter adapter;
    RecyclerView rvTests;
    MyLabInterface myLabInterface;

    public void onAttachToParentFragment(Fragment fragment) {
        myLabInterface = (MyLabInterface) fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mainContent = inflater.inflate(R.layout.lab_tests_fragment, container, false);
        testType = getArguments().getString(getResources().getString(R.string.key_test_type));
        rvTests = mainContent.findViewById(R.id.rvTests);
        return mainContent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTests.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new LabTestAdapter(getActivity(), testType, this);
        rvTests.setAdapter(adapter);
    }

    @Override
    public void onAddResultButtonClick() {
        myLabInterface.onAddResultButtonClick();
    }

    @Override
    public void onCancelButtonClick() {
    }

    @Override
    public String getEncounterName() {
        return null;
    }
}
