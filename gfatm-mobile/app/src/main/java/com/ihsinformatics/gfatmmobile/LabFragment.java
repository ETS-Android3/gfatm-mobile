package com.ihsinformatics.gfatmmobile;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LabFragment extends Fragment implements View.OnClickListener {

    private View mainview;
    public LabTestsFragment fragmentIncompleteTests;
    public LabTestsFragment fragmentCompleteTests;
    Button incompleteTestsButton;
    Button completeTestsButton;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mainview = inflater.inflate(R.layout.lab_fragment, container, false);
        incompleteTestsButton = mainview.findViewById(R.id.incompleteButton);
        completeTestsButton = mainview.findViewById(R.id.completeButton);

        fragmentIncompleteTests = new LabTestsFragment();
        bundle = new Bundle();
        bundle.putString("test_type", "INCOMPLETE");
        fragmentIncompleteTests.setArguments(bundle);

        fragmentCompleteTests = new LabTestsFragment();
        bundle = new Bundle();
        bundle.putString("test_type", "COMPLETE");
        fragmentCompleteTests.setArguments(bundle);

        return mainview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        incompleteTestsButton.setOnClickListener(this);
        completeTestsButton.setOnClickListener(this);

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_place, fragmentIncompleteTests, "INCOMPLETE_TESTS");
        fragmentTransaction.add(R.id.fragment_place, fragmentCompleteTests, "COMPLETE_TESTS");
        fragmentTransaction.commit();

        showIncompleteTestsFragment();
    }

    @Override
    public void onClick(View v) {
        if (v == incompleteTestsButton)
            showIncompleteTestsFragment();
        else if (v == completeTestsButton)
            showCompleteTestsFragment();
    }

    public void showIncompleteTestsFragment(){
        int color = App.getColor(getActivity(), R.attr.colorPrimaryDark);

        incompleteTestsButton.setTextColor(color);
        incompleteTestsButton.setBackgroundResource(R.drawable.selected_border_button);

        completeTestsButton.setTextColor(getResources().getColor(R.color.dark_grey));
        completeTestsButton.setBackgroundResource(R.drawable.border_button);

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.show(fragmentIncompleteTests);
        fragmentTransaction.hide(fragmentCompleteTests);
        fragmentTransaction.commit();
    }

    public void showCompleteTestsFragment(){
        int color = App.getColor(getActivity(), R.attr.colorPrimaryDark);

        incompleteTestsButton.setTextColor(getResources().getColor(R.color.dark_grey));
        incompleteTestsButton.setBackgroundResource(R.drawable.border_button);

        completeTestsButton.setTextColor(color);
        completeTestsButton.setBackgroundResource(R.drawable.selected_border_button);

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragmentIncompleteTests);
        fragmentTransaction.show(fragmentCompleteTests);
        fragmentTransaction.commit();
    }
}