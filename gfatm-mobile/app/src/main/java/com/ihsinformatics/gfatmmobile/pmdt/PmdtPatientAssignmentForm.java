package com.ihsinformatics.gfatmmobile.pmdt;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;

/**
 * Created by Tahira on 2/28/2017.
 */

public class PmdtPatientAssignmentForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledEditText treatmentSupporterId;
    TitledEditText treatmentSupporterFirstName;
    TitledEditText treatmentSupporterLastName;

    TitledEditText address1;
    TitledEditText address2;
    TitledSpinner addressCityDistrict;  // List - To be decided by Program Team later
    TitledSpinner addressTownTaluka;       // Towns - To be decided by Program Team later
    TitledEditText addressLandmark;

    TitledRadioGroup treatmentSupporterHouseholdMember;
    TitledSpinner treatmentSupporterPatientRelationship;
    TitledEditText otherRelationship;
    TitledRadioGroup reasonTreatmentSupporterHouseholdMember;
    TitledEditText otherReasonTreatmentSupporterHouseholdMember;

    ScrollView scrollView;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PAGE_COUNT = 1;
        FORM_NAME = Forms.PMDT_PATIENT_ASSIGNMENT;
        FORM = Forms.pmdtPatientAssignment;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(PAGE_COUNT - 1);
        formName.setText(FORM_NAME);

        initViews();

        groups = new ArrayList<ViewGroup>();

        if (App.isLanguageRTL()) {
            for (int i = PAGE_COUNT - 1; i >= 0; i--) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(context);
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        } else {
            for (int i = 0; i < PAGE_COUNT; i++) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(context);
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }
        return mainContent;
    }

    @Override
    public void initViews() {
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        treatmentSupporterId = new TitledEditText(context, "", getResources().getString(R.string.pmdt_treatment_supporter_id), "", "", 10, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        treatmentSupporterFirstName = new TitledEditText(context, "", getResources().getString(R.string.pmdt_treatment_supporter_first_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        treatmentSupporterFirstName.setFocusableInTouchMode(true);
        treatmentSupporterLastName = new TitledEditText(context, "", getResources().getString(R.string.pmdt_treatment_supporter_last_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        treatmentSupporterLastName.setFocusableInTouchMode(true);
        address1 = new TitledEditText(context, "", getResources().getString(R.string.pmdt_current_address_1), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        address2 = new TitledEditText(context, "", getResources().getString(R.string.pmdt_current_address_2), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        addressCityDistrict = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_current_address_city), getResources().getStringArray(R.array.pmdt_cities), getResources().getString(R.string.pmdt_karachi), App.HORIZONTAL);
        // Taluka to be considered
        addressLandmark = new TitledEditText(context, "", getResources().getString(R.string.pmdt_current_address_landmark), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        treatmentSupporterHouseholdMember = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_treatment_supporter_household_member), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        treatmentSupporterPatientRelationship = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_treatment_supporter_patient_relationship), getResources().getStringArray(R.array.pmdt_treatment_supporter_patient_relation), getResources().getString(R.string.pmdt_father), App.VERTICAL);
        otherRelationship = new TitledEditText(context, "", getResources().getString(R.string.pmdt_other_relationship), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        reasonTreatmentSupporterHouseholdMember = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_reason_having_treatment_supporter), getResources().getStringArray(R.array.pmdt_reasons_for_having_treatment_supporter), getResources().getString(R.string.pmdt_family_refused_treatment_supporter), App.VERTICAL, App.VERTICAL);
        otherReasonTreatmentSupporterHouseholdMember = new TitledEditText(context, "", getResources().getString(R.string.pmdt_other_reason_having_treatment_supporter), "", "", 255, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);

        views = new View[]{formDate.getButton(), treatmentSupporterId.getEditText(), treatmentSupporterFirstName.getEditText(), treatmentSupporterLastName.getEditText(),
                address1.getEditText(), address2.getEditText(), addressCityDistrict.getSpinner(), /* Taluka, if required*/ addressLandmark.getEditText(),
                treatmentSupporterHouseholdMember.getRadioGroup(), treatmentSupporterPatientRelationship.getSpinner(), otherRelationship.getEditText(),
                otherReasonTreatmentSupporterHouseholdMember.getEditText(), otherReasonTreatmentSupporterHouseholdMember.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, treatmentSupporterId, treatmentSupporterFirstName, treatmentSupporterLastName, address1, address2,
                        addressCityDistrict, addressLandmark, treatmentSupporterHouseholdMember, treatmentSupporterPatientRelationship,
                        otherRelationship, reasonTreatmentSupporterHouseholdMember, otherReasonTreatmentSupporterHouseholdMember}};

        formDate.getButton().setOnClickListener(this);
        treatmentSupporterHouseholdMember.getRadioGroup().setOnCheckedChangeListener(this);
        reasonTreatmentSupporterHouseholdMember.getRadioGroup().setOnCheckedChangeListener(this);
        resetViews();
    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
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
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
        }
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Object instantiateItem(View container, int position) {

            ViewGroup viewGroup = groups.get(position);
            ((ViewPager) container).addView(viewGroup, 0);

            return viewGroup;
        }

        @Override
        public void destroyItem(View container, int position, Object obj) {
            ((ViewPager) container).removeView((View) obj);
        }

        @Override
        public boolean isViewFromObject(View container, Object obj) {
            return container == obj;
        }

    }
}
