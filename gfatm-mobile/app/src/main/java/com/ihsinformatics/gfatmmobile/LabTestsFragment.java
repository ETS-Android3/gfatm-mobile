package com.ihsinformatics.gfatmmobile;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class LabTestsFragment extends Fragment {

    String testType;
    LabTestAdapter adapter;

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

        RecyclerView recyclerView = view.findViewById(R.id.rvTests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new LabTestAdapter(getActivity(), testType);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

}
