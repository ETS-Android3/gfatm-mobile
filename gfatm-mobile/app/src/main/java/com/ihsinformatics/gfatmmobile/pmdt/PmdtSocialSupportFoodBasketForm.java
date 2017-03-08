package com.ihsinformatics.gfatmmobile.pmdt;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;

/**
 * Created by Tahira on 3/6/2017.
 */

public class PmdtSocialSupportFoodBasketForm extends AbstractFormActivity {

    Context context;
    TitledButton formDate;
    TitledButton visitDate;
    TitledEditText externalId;
    TitledSpinner typeAssessment;
    TitledEditText otherAssessmentReason;
    TitledEditText treatmentMonth;
    AutoCompleteTextView treatmentFacilityAutoCompleteList;
    TitledEditText otherTreatmentFacility;
    TitledEditText nationalDrTbRegistrationNumber;
    TitledEditText patientOwnNic;
    TitledSpinner cnicOwner;
    TitledEditText otherNicOwner;
    TitledEditText nameCnicOwner;
    TitledEditText primaryPhone;
    TitledEditText alternatePhone;


    @Override
    public void initViews() {

    }

    @Override
    public void updateDisplay() {

    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public boolean submit() {
        return false;
    }

    @Override
    public boolean save() {
        return false;
    }

    @Override
    public void refill(int encounterId) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
