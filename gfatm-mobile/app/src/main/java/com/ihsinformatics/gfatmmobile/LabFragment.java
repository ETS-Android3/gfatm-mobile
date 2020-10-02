package com.ihsinformatics.gfatmmobile;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class LabFragment extends Fragment implements View.OnClickListener, AddTestFragment.MyButtonsInterface, LabTestsFragment.MyButtonsInterface , AddTestResultFragment.MyButtonsInterface{

    public LabTestsFragment fragmentIncompleteTests;
    public LabTestsFragment fragmentCompleteTests;
    Button incompleteTestsButton;
    Button completeTestsButton;
    ImageButton ibAddTest;
    Bundle bundle;
    public AddTestFragment fragmentAddTest;
    public AddTestResultFragment fragmentAddTestResult;
    View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lab_fragment, container, false);
        incompleteTestsButton = view.findViewById(R.id.incompleteButton);
        completeTestsButton = view.findViewById(R.id.completeButton);
        ibAddTest = view.findViewById(R.id.ibAddTest);

        fragmentIncompleteTests = new LabTestsFragment();
        fragmentIncompleteTests.onAttachToParentFragment(this);
        bundle = new Bundle();
        bundle.putString("test_type", "INCOMPLETE");
        fragmentIncompleteTests.setArguments(bundle);

        fragmentCompleteTests = new LabTestsFragment();
        fragmentCompleteTests.onAttachToParentFragment(this);
        bundle = new Bundle();
        bundle.putString("test_type", "COMPLETE");
        fragmentCompleteTests.setArguments(bundle);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        incompleteTestsButton.setOnClickListener(this);
        completeTestsButton.setOnClickListener(this);
        ibAddTest.setOnClickListener(this);

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_place_lab_tests, fragmentIncompleteTests, "INCOMPLETE_TESTS");
        fragmentTransaction.add(R.id.fragment_place_lab_tests, fragmentCompleteTests, "COMPLETE_TESTS");
        fragmentTransaction.commit();

        showIncompleteTestsFragment();
    }

    public boolean isAddTestScreenVisible() {
        if (fragmentAddTest != null)
            return fragmentAddTest.isVisible();
        return false;
    }

    public void toggleMainPageVisibility(boolean visibility) {
        view.findViewById(R.id.llMainLabPage).setVisibility(visibility ? View.VISIBLE : View.GONE);
        if(visibility)
            fragmentAddTest = null;
    }

    @Override
    public void onClick(View v) {
        if (v == incompleteTestsButton)
            showIncompleteTestsFragment();
        else if (v == completeTestsButton)
            showCompleteTestsFragment();
        else if (v == ibAddTest) {
            try {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentAddTest = AddTestFragment.class.newInstance();
                fragmentAddTest.onAttachToParentFragment(LabFragment.this);
                fragmentTransaction.replace(R.id.fragment_lab, (Fragment) fragmentAddTest);
                fragmentTransaction.commit();
                toggleMainPageVisibility(false);
            } catch (IllegalAccessException | java.lang.InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    public void showIncompleteTestsFragment() {
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

    public void showCompleteTestsFragment() {
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

    @Override
    public void cancel() {
        int color = App.getColor(getActivity(), R.attr.colorAccent);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.dialog).create();
        alertDialog.setMessage(getString(R.string.warning_before_close_adding_test));
        Drawable backIcon = getResources().getDrawable(R.drawable.ic_back);
        backIcon.setAutoMirrored(true);
        DrawableCompat.setTint(backIcon, color);
        alertDialog.setIcon(backIcon);
        alertDialog.setTitle(getResources().getString(R.string.back_to_lab_menu));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        toggleMainPageVisibility(true);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));
    }

    @Override
    public void onAddResultClick() {
        try {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentAddTestResult = AddTestResultFragment.class.newInstance();
            fragmentAddTestResult.onAttachToParentFragment(LabFragment.this);
            fragmentTransaction.replace(R.id.fragment_lab, (Fragment) fragmentAddTestResult);
            fragmentTransaction.commit();
            toggleMainPageVisibility(false);
        } catch (IllegalAccessException | java.lang.InstantiationException e) {
            e.printStackTrace();
        }
    }
}