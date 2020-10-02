package com.ihsinformatics.gfatmmobile;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class LabTestsFragment extends Fragment implements LabTestAdapter.MyButtonInterface {

    String testType;
    LabTestAdapter adapter;

    public interface MyButtonsInterface {
        void onAddResultClick();
    }

    MyButtonsInterface myButtonsInterface;

    public void onAttachToParentFragment(Fragment fragment){
        myButtonsInterface = (MyButtonsInterface) fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mainContent = inflater.inflate(R.layout.lab_tests_fragment, container, false);
        testType = getArguments().getString("test_type");
        return mainContent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvTests = view.findViewById(R.id.rvTests);
        rvTests.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new LabTestAdapter(getActivity(), testType, this);
        rvTests.setAdapter(adapter);
    }

    @Override
    public void onAddResultClick() {
        myButtonsInterface.onAddResultClick();
    }
}
