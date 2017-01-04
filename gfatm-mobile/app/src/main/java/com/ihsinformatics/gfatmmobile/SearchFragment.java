package com.ihsinformatics.gfatmmobile;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rabbia on 11/10/2016.
 */

public class SearchFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    Context context;

    LinearLayout searchFormLayout;
    LinearLayout searchResultLayout;
    Button searchPatientButton;
    Button searchPatientAgainButton;

    Spinner ageRange;

    CheckBox nameCheckBox;
    EditText name;

    CheckBox mobileNumberCheckBox;
    EditText mobileNumber;

    CheckBox patientIdCheckBox;
    EditText patientId;

    CheckBox healthCenterCheckBox;
    Spinner healthCenter;

    CheckBox motherNameCheckBox;
    EditText motherName;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View mainContent = inflater.inflate(R.layout.search_fragment, container, false);

        context = mainContent.getContext();

        searchPatientButton = (Button) mainContent.findViewById(R.id.searchPatient);
        searchPatientAgainButton = (Button) mainContent.findViewById(R.id.searchAgainPatient);
        searchFormLayout = (LinearLayout) mainContent.findViewById(R.id.searchForm);
        searchResultLayout = (LinearLayout) mainContent.findViewById(R.id.searchResult);

        ageRange = (Spinner) mainContent.findViewById(R.id.ageRange);

        List<String> spinnerList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.age_ranges)));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        ageRange.setAdapter(spinnerArrayAdapter);

        nameCheckBox = (CheckBox) mainContent.findViewById(R.id.nameCheckBox);
        name = (EditText) mainContent.findViewById(R.id.name);

        mobileNumberCheckBox = (CheckBox) mainContent.findViewById(R.id.mobileNumberCheckBox);
        mobileNumber = (EditText) mainContent.findViewById(R.id.mobileNumber);

        patientIdCheckBox = (CheckBox) mainContent.findViewById(R.id.patientIdCheckBox);
        patientId = (EditText) mainContent.findViewById(R.id.patientId);

        healthCenterCheckBox = (CheckBox) mainContent.findViewById(R.id.healthCenterCheckBox);
        healthCenter = (Spinner) mainContent.findViewById(R.id.healthCenter);
        healthCenter.setEnabled(false);

        motherNameCheckBox = (CheckBox) mainContent.findViewById(R.id.motherNameCheckBox);
        motherName = (EditText) mainContent.findViewById(R.id.motherName);

        searchPatientButton.setOnClickListener(this);
        searchPatientAgainButton.setOnClickListener(this);

        nameCheckBox.setOnCheckedChangeListener(this);
        mobileNumberCheckBox.setOnCheckedChangeListener(this);
        patientIdCheckBox.setOnCheckedChangeListener(this);
        healthCenterCheckBox.setOnCheckedChangeListener(this);
        motherNameCheckBox.setOnCheckedChangeListener(this);

        return mainContent;
    }

    @Override
    public void onClick(View v) {

        if(v == searchPatientButton){

            if (nameCheckBox.isChecked() || mobileNumberCheckBox.isChecked() || patientIdCheckBox.isChecked() ||
                    healthCenterCheckBox.isChecked() || motherNameCheckBox.isChecked()) {

                if (validate()) {

                    //TODO: Search...

                    searchFormLayout.setVisibility(View.GONE);
                    searchResultLayout.setVisibility(View.VISIBLE);
                }

            } else {

                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage(getString(R.string.form_error));
                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                // DrawableCompat.setTint(clearIcon, color);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_error));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }

        }
        else if (v == searchPatientAgainButton){
            searchFormLayout.setVisibility(View.VISIBLE);
            searchResultLayout.setVisibility(View.GONE);
        }

    }

    public boolean validate() {

        Boolean flag = true;

        if (motherNameCheckBox.isChecked()) {
            if (App.get(motherName).isEmpty()) {
                motherName.setError(getString(R.string.empty_field));
                motherName.requestFocus();
                flag = false;
            }
        }
        if (healthCenterCheckBox.isChecked()) {

        }
        if (patientIdCheckBox.isChecked()) {
            if (App.get(patientId).isEmpty()) {
                patientId.setError(getString(R.string.empty_field));
                patientId.requestFocus();
                flag = false;
            }
        }
        if (mobileNumberCheckBox.isChecked()) {
            if (App.get(mobileNumber).isEmpty()) {
                mobileNumber.setError(getString(R.string.empty_field));
                mobileNumber.requestFocus();
                flag = false;
            }
        }
        if (nameCheckBox.isChecked()) {
            if (App.get(name).isEmpty()) {
                name.setError(getString(R.string.empty_field));
                name.requestFocus();
                flag = false;
            }
        }

        return flag;

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == nameCheckBox) {
            if (isChecked)
                name.setEnabled(true);
            else
                name.setEnabled(false);
        } else if (buttonView == mobileNumberCheckBox) {
            if (isChecked)
                mobileNumber.setEnabled(true);
            else
                mobileNumber.setEnabled(false);
        } else if (buttonView == patientIdCheckBox) {
            if (isChecked)
                patientId.setEnabled(true);
            else
                patientId.setEnabled(false);
        } else if (buttonView == healthCenterCheckBox) {
            if (isChecked)
                healthCenter.setEnabled(true);
            else
                healthCenter.setEnabled(false);
        } else if (buttonView == motherNameCheckBox) {
            if (isChecked)
                motherName.setEnabled(true);
            else
                motherName.setEnabled(false);
        }
    }
}