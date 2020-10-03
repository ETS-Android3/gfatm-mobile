package com.ihsinformatics.gfatmmobile;

import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.widget.Toast;

public class LabFragment extends Fragment implements View.OnClickListener, MyLabInterface {

    private Button btnIncompleteTests;
    private Button btnCompleteTests;
    private ImageButton ibAddTest;
    private ImageButton ibSearchTests;
    private View view;
    private LabTestsFragment fragmentIncompleteTests;
    private LabTestsFragment fragmentCompleteTests;
    private AddTestFragment fragmentAddTest;
    private AddTestResultFragment fragmentAddTestResult;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lab_fragment, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        btnIncompleteTests = view.findViewById(R.id.btnIncompleteTab);
        btnCompleteTests = view.findViewById(R.id.btnCompleteTab);
        ibAddTest = view.findViewById(R.id.ibAddTest);
        ibSearchTests = view.findViewById(R.id.ibSearchTests);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initFragments();
        setListeners();
        showIncompleteTestsFragment();
    }

    private void initFragments() {
        fragmentIncompleteTests = new LabTestsFragment();
        fragmentIncompleteTests.onAttachToParentFragment(this);
        Bundle bundle = new Bundle();
        bundle.putString(getResources().getString(R.string.key_test_type), getResources().getString(R.string.incomplete));
        fragmentIncompleteTests.setArguments(bundle);

        fragmentCompleteTests = new LabTestsFragment();
        fragmentCompleteTests.onAttachToParentFragment(this);
        bundle = new Bundle();
        bundle.putString(getResources().getString(R.string.key_test_type), getResources().getString(R.string.complete));
        fragmentCompleteTests.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_place_lab_tests, fragmentIncompleteTests, getResources().getString(R.string.incomplete_tests_tag));
        fragmentTransaction.add(R.id.fragment_place_lab_tests, fragmentCompleteTests, getResources().getString(R.string.complete_tests_tag));
        fragmentTransaction.commit();
    }

    private void setListeners() {
        btnIncompleteTests.setOnClickListener(this);
        btnCompleteTests.setOnClickListener(this);
        ibAddTest.setOnClickListener(this);
        ibSearchTests.setOnClickListener(this);
    }

    public boolean isAddTestScreenVisible() {
        if (fragmentAddTest != null)
            return fragmentAddTest.isVisible();
        return false;
    }

    public boolean isAddTestResultsScreenVisible() {
        if (fragmentAddTestResult != null)
            return fragmentAddTestResult.isVisible();
        return false;
    }

    public void toggleMainPageVisibility(boolean visibility) {
        view.findViewById(R.id.layoutLabMain).setVisibility(visibility ? View.VISIBLE : View.GONE);
        if (visibility) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            if (fragmentAddTest != null)
                fragmentTransaction.remove(fragmentAddTest);
            if (fragmentAddTestResult != null)
                fragmentTransaction.remove(fragmentAddTestResult);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnIncompleteTests)
            showIncompleteTestsFragment();
        else if (v == btnCompleteTests)
            showCompleteTestsFragment();
        else if (v == ibAddTest)
            showAddTestFragment();
        else if (v == ibSearchTests) {
            Toast.makeText(getActivity(), "Search", Toast.LENGTH_SHORT).show();
        }
    }

    public void showAddTestFragment() {
        try {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentAddTest = AddTestFragment.class.newInstance();
            fragmentAddTest.onAttachToParentFragment(LabFragment.this);
            fragmentTransaction.replace(R.id.my_lab_fragment, (Fragment) fragmentAddTest);
            fragmentTransaction.commit();
            toggleMainPageVisibility(false);
        } catch (IllegalAccessException | java.lang.InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void showIncompleteTestsFragment() {

        btnIncompleteTests.setTextColor(App.getColor(getActivity(), R.attr.colorPrimaryDark));
        btnIncompleteTests.setBackgroundResource(R.drawable.selected_border_button);

        btnCompleteTests.setTextColor(getResources().getColor(R.color.dark_grey));
        btnCompleteTests.setBackgroundResource(R.drawable.border_button);

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.show(fragmentIncompleteTests);
        fragmentTransaction.hide(fragmentCompleteTests);
        fragmentTransaction.commit();
    }

    public void showCompleteTestsFragment() {

        btnIncompleteTests.setTextColor(getResources().getColor(R.color.dark_grey));
        btnIncompleteTests.setBackgroundResource(R.drawable.border_button);

        btnCompleteTests.setTextColor(App.getColor(getActivity(), R.attr.colorPrimaryDark));
        btnCompleteTests.setBackgroundResource(R.drawable.selected_border_button);

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragmentIncompleteTests);
        fragmentTransaction.show(fragmentCompleteTests);
        fragmentTransaction.commit();
    }

    @Override
    public void onCancelButtonClick() {
        int color = App.getColor(getActivity(), R.attr.colorAccent);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.dialog).create();
        alertDialog.setMessage(getString(R.string.warning_before_close_adding_test));
        Drawable backIcon = getResources().getDrawable(R.drawable.ic_back);
        backIcon.setAutoMirrored(true);
        DrawableCompat.setTint(backIcon, color);
        alertDialog.setIcon(backIcon);
        alertDialog.setTitle(getResources().getString(R.string.back_to_lab_tests));
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
    public String getEncounterName() {
        return null;
    }

    @Override
    public void onAddResultButtonClick() {
        try {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentAddTestResult = AddTestResultFragment.class.newInstance();
            fragmentAddTestResult.onAttachToParentFragment(LabFragment.this);
            fragmentTransaction.replace(R.id.my_lab_fragment, (Fragment) fragmentAddTestResult);
            fragmentTransaction.commit();
            toggleMainPageVisibility(false);
        } catch (IllegalAccessException | java.lang.InstantiationException e) {
            e.printStackTrace();
        }
    }
}