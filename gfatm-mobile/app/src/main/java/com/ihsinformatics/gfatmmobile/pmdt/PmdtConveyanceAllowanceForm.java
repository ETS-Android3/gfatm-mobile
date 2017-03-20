package com.ihsinformatics.gfatmmobile.pmdt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tahira on 3/6/2017.
 */

public class PmdtConveyanceAllowanceForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledButton visitDate;
    TitledEditText externalId;
    TitledSpinner typeAssessment;
    TitledEditText otherAssessmentReason;
    TitledEditText treatmentMonth;
    LinearLayout facilityLinearLayout;
    TextView treatmentFacilityText;
    AutoCompleteTextView treatmentFacilityAutoCompleteList;
    TitledEditText nationalDrTbRegistrationNumber;

    TitledRadioGroup registerdOutstationFacility;
    LinearLayout registerdOutstationFacilityLinearLayout;
    TextView registerdOutstationFacilityText;
    AutoCompleteTextView registerdOutstationFacilityAutoCompleteList;

    LinearLayout patientCnicLayout;
    LinearLayout patientCnicQuestionLayout;
    MyTextView patientCnicQuestionRequired;
    MyTextView patientCnicQuestion;
    TitledEditText patientCnic1;
    TitledEditText patientCnic2;
    TitledEditText patientCnic3;
    TitledRadioGroup patientOwnCnic;
    TitledSpinner patientCnicOwner;
    TitledEditText otherPatientCnicOwner;
    TitledEditText namePatientCnicOwner;
    LinearLayout patientPrimaryPhoneLayout;
    TitledEditText patientPrimaryPhone1a;
    TitledEditText patientPrimaryPhone1b;
    LinearLayout patientAlternatePhoneLayout;
    TitledEditText patientAlternatePhone1a;
    TitledEditText patientAlternatePhone1b;

    TitledRadioGroup patientSubmitSputumSample;         // include title: Sample submission information
    TitledEditText sputumSampleId;
    TitledRadioGroup reasonNotSubmittedSample;
    TitledEditText otherReasonNotSubmittedSample;
    TitledRadioGroup visitedDoctor;
    TitledEditText conveyanceVoucherBookNumber;
    TitledEditText conveyanceVoucherNumber;
    TitledEditText amountTransferred;
    TitledEditText amountTransferredInWords;

    ScrollView scrollView;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PAGE_COUNT = 3;
        FORM_NAME = Forms.PMDT_CONVEYANCE_ALLOWANCE;
        FORM = Forms.conveyanceAllowance;

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
        visitDate = new TitledButton(context, null, getResources().getString(R.string.pmdt_visit_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        externalId = new TitledEditText(context, null, getResources().getString(R.string.pmdt_external_id), "", "", 11, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        typeAssessment = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_assessment_type), getResources().getStringArray(R.array.pmdt_types_of_assessment), getResources().getString(R.string.pmdt_baseline_assessment), App.HORIZONTAL, true);
        otherAssessmentReason = new TitledEditText(context, null, getResources().getString(R.string.pmdt_other_assessment_reason), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        treatmentMonth = new TitledEditText(context, null, getResources().getString(R.string.pmdt_treatment_month), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        // Fetching PMDT Locations
        String program = "";
        if (App.getProgram().equals(getResources().getString(R.string.pet)))
            program = "pet_location";
        else if (App.getProgram().equals(getResources().getString(R.string.fast)))
            program = "fast_location";
        else if (App.getProgram().equals(getResources().getString(R.string.comorbidities)))
            program = "comorbidities_location";
        else if (App.getProgram().equals(getResources().getString(R.string.pmdt)))
            program = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.childhood_tb)))
            program = "childhood_tb_location";

        final Object[][] locations = serverService.getAllLocations(program);
        String[] locationArray = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][1]);
        }

        treatmentFacilityText = new TextView(context);
        treatmentFacilityText.setText(getResources().getString(R.string.pmdt_treatment_facility));
        LinearLayout requiredTreatmentFacilityLayout = new LinearLayout(context);
        MyTextView treatmentFacilityQuestionRequired = new MyTextView(context, "*");
        int color1 = App.getColor(context, R.attr.colorAccent);
        treatmentFacilityQuestionRequired.setTextColor(color1);
        requiredTreatmentFacilityLayout.setOrientation(LinearLayout.HORIZONTAL);
        requiredTreatmentFacilityLayout.addView(treatmentFacilityQuestionRequired);
        requiredTreatmentFacilityLayout.addView(treatmentFacilityText);
        treatmentFacilityAutoCompleteList = new AutoCompleteTextView(context);
        final ArrayAdapter<String> autoCompleteFacilityAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, locationArray);
        treatmentFacilityAutoCompleteList.setAdapter(autoCompleteFacilityAdapter);
        treatmentFacilityAutoCompleteList.setHint("Enter facility");
        facilityLinearLayout = new LinearLayout(context);
        facilityLinearLayout.setOrientation(LinearLayout.VERTICAL);
        facilityLinearLayout.addView(requiredTreatmentFacilityLayout);
        facilityLinearLayout.addView(treatmentFacilityAutoCompleteList);

        nationalDrTbRegistrationNumber = new TitledEditText(context, null, getResources().getString(R.string.pmdt_national_dr_tb_registration_number), "", "", 25, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        registerdOutstationFacility = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_patient_registered_outstation), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        registerdOutstationFacilityText = new TextView(context);
        registerdOutstationFacilityText.setText(getResources().getString(R.string.pmdt_registered_outstation_facility));
        registerdOutstationFacilityAutoCompleteList = new AutoCompleteTextView(context);
        registerdOutstationFacilityAutoCompleteList.setAdapter(autoCompleteFacilityAdapter);
        registerdOutstationFacilityAutoCompleteList.setHint("Enter facility");
        registerdOutstationFacilityLinearLayout = new LinearLayout(context);
        registerdOutstationFacilityLinearLayout.setOrientation(LinearLayout.VERTICAL);
        registerdOutstationFacilityLinearLayout.addView(registerdOutstationFacilityText);
        registerdOutstationFacilityLinearLayout.addView(registerdOutstationFacilityAutoCompleteList);

        // cnic layouts
        patientCnicLayout = new LinearLayout(context);
        patientCnicQuestionLayout = new LinearLayout(context);
        patientCnicLayout.setOrientation(LinearLayout.HORIZONTAL);
        patientCnicQuestionLayout.setOrientation(LinearLayout.HORIZONTAL);

        MyLinearLayout linearLayout1 = new MyLinearLayout(context, null, App.VERTICAL);
        patientCnicQuestionRequired = new MyTextView(context, "*");
        int color = App.getColor(context, R.attr.colorAccent);
        patientCnicQuestionRequired.setTextColor(color);
        patientCnicQuestion = new MyTextView(context, getResources().getString(R.string.pmdt_patient_cnic_provided));
        patientCnic1 = new TitledEditText(context, null, "", "", "XXXXX", 5, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        patientCnicLayout.addView(patientCnic1);
        patientCnic2 = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        patientCnicLayout.addView(patientCnic2);
        patientCnic3 = new TitledEditText(context, null, "-", "", "X", 1, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        patientCnicLayout.addView(patientCnic3);
        // setting question layout
        patientCnicQuestionLayout.addView(patientCnicQuestionRequired);
        patientCnicQuestionLayout.addView(patientCnicQuestion);
        // adding to parent titled cnic section layout
        linearLayout1.addView(patientCnicQuestionLayout);
        linearLayout1.addView(patientCnicLayout);

        patientOwnCnic = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_patient_own_cnic), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, false);
        patientCnicOwner = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_cnic_owner), getResources().getStringArray(R.array.pmdt_cnic_owners), getResources().getString(R.string.pmdt_father), App.HORIZONTAL, false);
        otherPatientCnicOwner = new TitledEditText(context, null, getResources().getString(R.string.pmdt_other_cnic_owner), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        namePatientCnicOwner = new TitledEditText(context, null, getResources().getString(R.string.pmdt_cnic_owner_name), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        patientPrimaryPhoneLayout = new LinearLayout(context);
        patientPrimaryPhoneLayout.setOrientation(LinearLayout.HORIZONTAL);
        patientPrimaryPhone1a = new TitledEditText(context, null, getResources().getString(R.string.pmdt_patient_primary_phone_number), "", "XXXX", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        patientPrimaryPhoneLayout.addView(patientPrimaryPhone1a);
        patientPrimaryPhone1b = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        patientPrimaryPhoneLayout.addView(patientPrimaryPhone1b);
        patientAlternatePhoneLayout = new LinearLayout(context);
        patientAlternatePhoneLayout.setOrientation(LinearLayout.HORIZONTAL);
        patientAlternatePhone1a = new TitledEditText(context, null, getResources().getString(R.string.pmdt_patient_alternate_phone_number), "", "XXXX", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        patientAlternatePhoneLayout.addView(patientAlternatePhone1a);
        patientAlternatePhone1b = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        patientAlternatePhoneLayout.addView(patientAlternatePhone1b);

        // Sample submission information
        patientSubmitSputumSample = new TitledRadioGroup(context, getResources().getString(R.string.pmdt_title_sample_submission_information), getResources().getString(R.string.pmdt_sputum_sample_submitted), getResources().getStringArray(R.array.pmdt_yes_no_not_applicable), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, false);
        sputumSampleId = new TitledEditText(context, null, getResources().getString(R.string.pmdt_sputum_sample_id), "", "", 25, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        reasonNotSubmittedSample = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_reason_not_submitted_sputum), getResources().getStringArray(R.array.pmdt_reasons_sputums_not_submitted), getResources().getString(R.string.pmdt_could_not_produce_sputum), App.VERTICAL, App.VERTICAL);
        otherReasonNotSubmittedSample = new TitledEditText(context, null, getResources().getString(R.string.pmdt_other_reason_not_submitted_sputum), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        visitedDoctor = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_patient_visited_doctor), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, false);
        conveyanceVoucherBookNumber = new TitledEditText(context, null, getResources().getString(R.string.pmdt_conveyance_voucher_book_number), "", "", 20, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.VERTICAL, true);
        conveyanceVoucherNumber = new TitledEditText(context, null, getResources().getString(R.string.pmdt_conveyance_voucher_number), "", "", 20, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.VERTICAL, true);
        amountTransferred = new TitledEditText(context, null, getResources().getString(R.string.pmdt_amount_transferred), "", "", 5, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        amountTransferredInWords = new TitledEditText(context, null, getResources().getString(R.string.pmdt_amount_in_words), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), visitDate.getButton(), externalId.getEditText(), typeAssessment.getSpinner(), otherAssessmentReason.getEditText(), treatmentMonth.getEditText(), treatmentFacilityAutoCompleteList,
                nationalDrTbRegistrationNumber.getEditText(), registerdOutstationFacility.getRadioGroup(), registerdOutstationFacilityAutoCompleteList, patientCnic1.getEditText(), patientCnic2.getEditText(), patientCnic3.getEditText(), patientOwnCnic.getRadioGroup(), patientCnicOwner.getSpinner(),
                otherPatientCnicOwner.getEditText(), namePatientCnicOwner.getEditText(), patientPrimaryPhone1a.getEditText(), patientPrimaryPhone1b.getEditText(), patientAlternatePhone1a.getEditText(), patientAlternatePhone1b.getEditText(), patientSubmitSputumSample.getRadioGroup(),
                sputumSampleId.getEditText(), reasonNotSubmittedSample.getRadioGroup(), otherReasonNotSubmittedSample.getEditText(), visitedDoctor.getRadioGroup(), conveyanceVoucherBookNumber.getEditText(),
                conveyanceVoucherNumber.getEditText(), amountTransferred.getEditText(), amountTransferredInWords.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, visitDate, externalId, typeAssessment, otherAssessmentReason, treatmentMonth, facilityLinearLayout,
                        nationalDrTbRegistrationNumber, registerdOutstationFacility, registerdOutstationFacilityLinearLayout},
                        {linearLayout1, patientOwnCnic, patientCnicOwner, otherPatientCnicOwner, namePatientCnicOwner, patientPrimaryPhoneLayout, patientAlternatePhoneLayout},
                        {patientSubmitSputumSample, sputumSampleId, reasonNotSubmittedSample, otherReasonNotSubmittedSample, visitedDoctor,
                                conveyanceVoucherBookNumber, conveyanceVoucherNumber, amountTransferred, amountTransferredInWords }};

        formDate.getButton().setOnClickListener(this);
        visitDate.getButton().setOnClickListener(this);
        typeAssessment.getSpinner().setOnItemSelectedListener(this);
        registerdOutstationFacility.getRadioGroup().setOnCheckedChangeListener(this);
        patientOwnCnic.getRadioGroup().setOnCheckedChangeListener(this);
        patientCnicOwner.getSpinner().setOnItemSelectedListener(this);
        patientSubmitSputumSample.getRadioGroup().setOnCheckedChangeListener(this);
        reasonNotSubmittedSample.getRadioGroup().setOnCheckedChangeListener(this);
        visitedDoctor.getRadioGroup().setOnCheckedChangeListener(this);

    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        visitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
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
        } else if (view == visitDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
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
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        visitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

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