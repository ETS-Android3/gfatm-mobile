package com.ihsinformatics.gfatmmobile.pmdt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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

public class PmdtSocialSupportFoodBasketForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

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
    TitledRadioGroup patientAccompanied;
    TitledEditText treatmentSupporterId;        // include title: Treatment Supporter Information
    TitledEditText treatmentSupporterFirstName;
    TitledEditText treatmentSupporterLastName;
    LinearLayout treatmentSupporterCnicLayout;
    TitledEditText treatmentSupporterCnic1;
    TitledEditText treatmentSupporterCnic2;
    TitledEditText treatmentSupporterCnic3;
    TitledRadioGroup treatmentSupporterOwnCnic;
    TitledSpinner treatmentSupporterCnicOwner;
    TitledEditText otherTreatmentSupporterCnicOwner;
    TitledEditText nameTreatmentSupporterCnicOwner;
    LinearLayout treatmentSupporterPrimaryPhoneLayout;
    TitledEditText treatmentSupporterPrimaryPhone1a;
    TitledEditText treatmentSupporterPrimaryPhone1b;
    LinearLayout treatmentSupporterAlternatePhoneLayout;
    TitledEditText treatmentSupporterAlternatePhone1a;
    TitledEditText treatmentSupporterAlternatePhone1b;
    TitledRadioGroup patientSubmitSputumSample;         // include title: Sample submission information
    TitledEditText sputumSampleId;
    TitledRadioGroup reasonNotSubmittedSample;
    TitledEditText otherReasonNotSubmittedSample;
    TitledRadioGroup visitedDoctor;
    TitledRadioGroup foodBasketAmount;
    TitledEditText foodBasketVoucherBookNumber;
    TitledEditText foodBasketVoucherNumber;
    TitledButton validityVoucherDate;

    ScrollView scrollView;

    public static final int THIRD_DATE_DIALOG_ID = 3;
    // Extra Views for date ...
    Calendar thirdDateCalendar;
    DialogFragment thirdDateFragment;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PAGE_COUNT = 4;
        FORM_NAME = Forms.PMDT_SOCIAL_SUPPORT_FOOD_BASKET;
        FORM = Forms.pmdtSocialSupportFoodBasket;
        thirdDateCalendar = Calendar.getInstance();
        thirdDateFragment = new SelectDateFragment();

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
        externalId = new TitledEditText(context, null, getResources().getString(R.string.pmdt_external_id), "", "", 11, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        typeAssessment = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_assessment_type), getResources().getStringArray(R.array.pmdt_types_of_assessment), getResources().getString(R.string.pmdt_baseline_assessment), App.HORIZONTAL, true);
        otherAssessmentReason = new TitledEditText(context, null, getResources().getString(R.string.pmdt_other_assessment_reason), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        treatmentMonth = new TitledEditText(context, null, getResources().getString(R.string.pmdt_treatment_month), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

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
        final ArrayAdapter<String> autoCompleteLocationAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, locationArray);
        treatmentFacilityAutoCompleteList.setAdapter(autoCompleteLocationAdapter);
        treatmentFacilityAutoCompleteList.setHint("Enter facility");
        facilityLinearLayout = new LinearLayout(context);
        facilityLinearLayout.setOrientation(LinearLayout.VERTICAL);
        facilityLinearLayout.addView(requiredTreatmentFacilityLayout);
        facilityLinearLayout.addView(treatmentFacilityAutoCompleteList);

        nationalDrTbRegistrationNumber = new TitledEditText(context, null, getResources().getString(R.string.pmdt_national_dr_tb_registration_number), "", "", 25, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        // cnic layouts
        patientCnicLayout = new LinearLayout(context);
        patientCnicQuestionLayout = new LinearLayout(context);
        patientCnicLayout.setOrientation(LinearLayout.HORIZONTAL);
        patientCnicQuestionLayout.setOrientation(LinearLayout.HORIZONTAL);

        MyLinearLayout linearLayout1 = new MyLinearLayout(context, getResources().getString(R.string.pmdt_title_cnic_patient), App.VERTICAL);
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
        namePatientCnicOwner = new TitledEditText(context, null, getResources().getString(R.string.pmdt_cnic_owner_name), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        patientPrimaryPhoneLayout = new LinearLayout(context);
        patientPrimaryPhoneLayout.setOrientation(LinearLayout.HORIZONTAL);
        patientPrimaryPhone1a = new TitledEditText(context, null, getResources().getString(R.string.pmdt_patient_primary_phone_number), "", "XXXX", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        patientPrimaryPhoneLayout.addView(patientPrimaryPhone1a);
        patientPrimaryPhone1b = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        patientPrimaryPhoneLayout.addView(patientPrimaryPhone1b);
        patientAlternatePhoneLayout = new LinearLayout(context);
        patientAlternatePhoneLayout.setOrientation(LinearLayout.HORIZONTAL);
        patientAlternatePhone1a = new TitledEditText(context, null, getResources().getString(R.string.pmdt_patient_alternate_phone_number), "", "XXXX", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        patientAlternatePhoneLayout.addView(patientAlternatePhone1a);
        patientAlternatePhone1b = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        patientAlternatePhoneLayout.addView(patientAlternatePhone1b);

        // Visit information
        patientAccompanied = new TitledRadioGroup(context, getResources().getString(R.string.pmdt_title_treatment_supporter_information), getResources().getString(R.string.pmdt_patient_accompanied), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        treatmentSupporterId = new TitledEditText(context, null, getResources().getString(R.string.pmdt_treatment_supporter_id), "", "", 10, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        treatmentSupporterFirstName = new TitledEditText(context, null, getResources().getString(R.string.pmdt_treatment_supporter_first_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        treatmentSupporterLastName = new TitledEditText(context, null, getResources().getString(R.string.pmdt_treatment_supporter_last_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        treatmentSupporterCnicLayout = new LinearLayout(context);
        treatmentSupporterCnicLayout.setOrientation(LinearLayout.HORIZONTAL);
        treatmentSupporterCnic1 = new TitledEditText(context, null, getResources().getString(R.string.pmdt_treatment_supporter_cnic_provided), "", "XXXXX", 5, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        treatmentSupporterCnicLayout.addView(treatmentSupporterCnic1);
        treatmentSupporterCnic2 = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        treatmentSupporterCnicLayout.addView(treatmentSupporterCnic2);
        treatmentSupporterCnic3 = new TitledEditText(context, null, "-", "", "X", 1, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        treatmentSupporterCnicLayout.addView(treatmentSupporterCnic3);

        treatmentSupporterOwnCnic = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_treatment_supporter_own_cnic), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, false);
        treatmentSupporterCnicOwner = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_cnic_owner), getResources().getStringArray(R.array.pmdt_cnic_owners), getResources().getString(R.string.pmdt_father), App.VERTICAL, false);
        otherTreatmentSupporterCnicOwner = new TitledEditText(context, null, getResources().getString(R.string.pmdt_other_cnic_owner), "", "", 25, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        nameTreatmentSupporterCnicOwner = new TitledEditText(context, null, getResources().getString(R.string.pmdt_cnic_owner_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        treatmentSupporterPrimaryPhoneLayout = new LinearLayout(context);
        treatmentSupporterPrimaryPhoneLayout.setOrientation(LinearLayout.HORIZONTAL);
        treatmentSupporterPrimaryPhone1a = new TitledEditText(context, null, getResources().getString(R.string.pmdt_patient_primary_phone_number), "", "XXXX", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        treatmentSupporterPrimaryPhoneLayout.addView(treatmentSupporterPrimaryPhone1a);
        treatmentSupporterPrimaryPhone1b = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        treatmentSupporterPrimaryPhoneLayout.addView(treatmentSupporterPrimaryPhone1b);
        treatmentSupporterAlternatePhoneLayout = new LinearLayout(context);
        treatmentSupporterAlternatePhoneLayout.setOrientation(LinearLayout.HORIZONTAL);
        treatmentSupporterAlternatePhone1a = new TitledEditText(context, null, getResources().getString(R.string.pmdt_patient_alternate_phone_number), "", "XXXX", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        treatmentSupporterAlternatePhoneLayout.addView(treatmentSupporterAlternatePhone1a);
        treatmentSupporterAlternatePhone1b = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        treatmentSupporterAlternatePhoneLayout.addView(treatmentSupporterAlternatePhone1b);

        // Sample submission information
        patientSubmitSputumSample = new TitledRadioGroup(context, getResources().getString(R.string.pmdt_title_sample_submission_information), getResources().getString(R.string.pmdt_sputum_sample_submitted), getResources().getStringArray(R.array.pmdt_yes_no_not_applicable), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, false);
        sputumSampleId = new TitledEditText(context, null, getResources().getString(R.string.pmdt_sputum_sample_id), "", "", 25, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        reasonNotSubmittedSample = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_reason_not_submitted_sputum), getResources().getStringArray(R.array.pmdt_reasons_sputums_not_submitted), getResources().getString(R.string.pmdt_could_not_produce_sputum), App.VERTICAL, App.VERTICAL);
        otherReasonNotSubmittedSample = new TitledEditText(context, null, getResources().getString(R.string.pmdt_other_reason_not_submitted_sputum), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        visitedDoctor = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_patient_visited_doctor), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, false);
        foodBasketAmount = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_food_baskets_amount), getResources().getStringArray(R.array.pmdt_number_food_baskets), getResources().getString(R.string.pmdt_one), App.HORIZONTAL, App.VERTICAL, false);
        foodBasketVoucherBookNumber = new TitledEditText(context, null, getResources().getString(R.string.pmdt_food_basket_voucher_book_number), "", "", 20, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.VERTICAL, true);
        foodBasketVoucherNumber = new TitledEditText(context, null, getResources().getString(R.string.pmdt_food_basket_voucher_number), "", "", 20, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.VERTICAL, true);
        validityVoucherDate = new TitledButton(context, null, getResources().getString(R.string.pmdt_validity_voucher_date), DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), visitDate.getButton(), externalId.getEditText(), typeAssessment.getSpinner(), otherAssessmentReason.getEditText(), treatmentMonth.getEditText(), treatmentFacilityAutoCompleteList,
                nationalDrTbRegistrationNumber.getEditText(), patientCnic1.getEditText(), patientCnic2.getEditText(), patientCnic3.getEditText(), patientOwnCnic.getRadioGroup(), patientCnicOwner.getSpinner(),
                otherPatientCnicOwner.getEditText(), namePatientCnicOwner.getEditText(), patientPrimaryPhone1a.getEditText(), patientPrimaryPhone1b.getEditText(), patientAlternatePhone1a.getEditText(), patientAlternatePhone1b.getEditText(),
                patientAccompanied.getRadioGroup(), treatmentSupporterId.getEditText(), treatmentSupporterFirstName.getEditText(), treatmentSupporterLastName.getEditText(), treatmentSupporterCnic1.getEditText(), treatmentSupporterCnic2.getEditText(),
                treatmentSupporterCnic3.getEditText(), treatmentSupporterOwnCnic.getRadioGroup(), treatmentSupporterCnicOwner.getSpinner(), otherTreatmentSupporterCnicOwner.getEditText(), nameTreatmentSupporterCnicOwner.getEditText(),
                treatmentSupporterPrimaryPhone1a.getEditText(), treatmentSupporterPrimaryPhone1b.getEditText(), treatmentSupporterAlternatePhone1a.getEditText(), treatmentSupporterAlternatePhone1b.getEditText(), patientSubmitSputumSample.getRadioGroup(),
                sputumSampleId.getEditText(), reasonNotSubmittedSample.getRadioGroup(), otherReasonNotSubmittedSample.getEditText(), visitedDoctor.getRadioGroup(), foodBasketAmount.getRadioGroup(), foodBasketVoucherBookNumber.getEditText(),
                foodBasketVoucherNumber.getEditText(), validityVoucherDate.getButton()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, visitDate, externalId, typeAssessment, otherAssessmentReason, treatmentMonth, facilityLinearLayout, nationalDrTbRegistrationNumber},
                        {linearLayout1, patientOwnCnic, patientCnicOwner, otherPatientCnicOwner, namePatientCnicOwner, patientPrimaryPhoneLayout, patientAlternatePhoneLayout},
                        {patientAccompanied, treatmentSupporterId, treatmentSupporterFirstName, treatmentSupporterLastName, treatmentSupporterCnicLayout, treatmentSupporterOwnCnic, treatmentSupporterCnicOwner,
                                otherTreatmentSupporterCnicOwner, nameTreatmentSupporterCnicOwner, treatmentSupporterPrimaryPhoneLayout, treatmentSupporterAlternatePhoneLayout},
                        {patientSubmitSputumSample, sputumSampleId, reasonNotSubmittedSample, otherReasonNotSubmittedSample, visitedDoctor,
                                foodBasketAmount, foodBasketVoucherBookNumber, foodBasketVoucherNumber, validityVoucherDate
                        }};

        formDate.getButton().setOnClickListener(this);
        visitDate.getButton().setOnClickListener(this);
        validityVoucherDate.getButton().setOnClickListener(this);
        typeAssessment.getSpinner().setOnItemSelectedListener(this);
        patientOwnCnic.getRadioGroup().setOnCheckedChangeListener(this);
        patientCnicOwner.getSpinner().setOnItemSelectedListener(this);
        patientAccompanied.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentSupporterOwnCnic.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentSupporterCnicOwner.getSpinner().setOnItemSelectedListener(this);
        patientSubmitSputumSample.getRadioGroup().setOnCheckedChangeListener(this);
        reasonNotSubmittedSample.getRadioGroup().setOnCheckedChangeListener(this);
        visitedDoctor.getRadioGroup().setOnCheckedChangeListener(this);
        foodBasketAmount.getRadioGroup().setOnCheckedChangeListener(this);

    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        visitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        validityVoucherDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
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
        } else if (view == validityVoucherDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");
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
        validityVoucherDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
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


    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar;
            if (getArguments().getInt("type") == THIRD_DATE_DIALOG_ID)
                calendar = thirdDateCalendar;
//            else if (getArguments().getInt("type") == SECOND_DATE_DIALOG_ID)
//                calendar = secondDateCalendar;
            else
                return null;

            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setTag(getArguments().getInt("type"));
            if (!getArguments().getBoolean("allowFutureDate", false)) {
                Date date = new Date();
                date.setHours(24);
                date.setSeconds(60);
                dialog.getDatePicker().setMaxDate(date.getTime());
            }
            if (!getArguments().getBoolean("allowPastDate", false))
                dialog.getDatePicker().setMinDate(new Date().getTime());
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            if (((int) view.getTag()) == THIRD_DATE_DIALOG_ID)
                thirdDateCalendar.set(yy, mm, dd);
            updateDisplay();
        }
    }

}
