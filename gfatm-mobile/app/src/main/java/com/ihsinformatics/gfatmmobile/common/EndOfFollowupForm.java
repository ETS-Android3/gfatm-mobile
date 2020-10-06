package com.ihsinformatics.gfatmmobile.common;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyEditText;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 2/21/2017.
 */

public class EndOfFollowupForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    // Views...

    TitledSpinner treatmentOutcome;
    TitledEditText tbRegisterationNumber;
    TitledSpinner transferOutLocations;
    TitledEditText remarks;
    TitledRadioGroup treatmentInitiatedReferralSite;
    TitledSpinner treatmentNotInitiatedReferralSite;
    TitledEditText treatmentNotInitiatedReferralSiteOther;
    TitledRadioGroup drConfirmation;
    TitledEditText enrsId;
    MyTextView endFollowupInstruction;
    TitledEditText firstName;
    TitledEditText lastName;
    LinearLayout mobileLinearLayout;
    MyEditText mobile1;
    MyEditText mobile2;

    TitledButton deathDate;
    TitledSpinner deathReason;
    TitledEditText otherDeathReason;

    TitledSpinner reasonForFailure;
    TitledEditText reasonForFailureOther;

    TitledSpinner reasonForLossOfFollowup;
    TitledEditText reasonForLossOfFollowupOther;

    TitledRadioGroup patientEvaluated;
    TitledEditText patientNotEvaluatedReason;

    /**
     * CHANGE pageCount and formName Variable only...
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 1;
        formName = Forms.END_OF_FOLLOWUP;
        form = Forms.endOfFollowup;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(pageCount - 1);
        formNameView.setText(formName);

        initViews();

        groups = new ArrayList<ViewGroup>();

        if (App.isLanguageRTL()) {
            for (int i = pageCount - 1; i >= 0; i--) {
                LinearLayout layout = new LinearLayout(mainContent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                ScrollView scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        } else {
            for (int i = 0; i < pageCount; i++) {
                LinearLayout layout = new LinearLayout(mainContent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                ScrollView scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }

        gotoFirstPage();
        return mainContent;
    }

    /**
     * Initializes all views and ArrayList and Views Array
     */
    public void initViews() {

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);

        String patientSource = serverService.getLatestEncounterDateTime(App.getPatientId(), "PET-Baseline Screening");
        if (patientSource != null)
            treatmentOutcome = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_treatment_outcome), getResources().getStringArray(R.array.treatment_outcome_list_contact), getResources().getString(R.string.fast_cured), App.VERTICAL,false,"TREATMENT OUTCOME",new String[]{ "CURE, OUTCOME" , "TREATMENT COMPLETE" , "TUBERCULOSIS TREATMENT FAILURE" , "DIED" , "TRANSFERRED OUT" , "PATIENT REFERRED" , "LOST TO FOLLOW-UP" , "CLINICALLY EVALUATED, NO TB" , "ANTIBIOTIC COMPLETE - NO TB" , "NOT EVALUATED" , "TREATMENT ADAPTED" , "CONTACT DIAGNOSED WITH TB" , "REFUSAL OF TREATMENT BY PATIENT","PATIENT REFUSED TREATMENT AFTER STARTING" , "REFUSED SCREENING" , "PATIENT MOVED" , "TREATMENT STOPPED BY DOCTOR" , "TEST DONE, NO TB" , "OTHER TREATMENT OUTCOME"});
        else {

            patientSource = serverService.getLatestEncounterDateTime(App.getPatientId(), "PET-Clinician Contact Screening");
            if (patientSource != null)
                treatmentOutcome = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_treatment_outcome), getResources().getStringArray(R.array.treatment_outcome_list_contact), getResources().getString(R.string.fast_cured), App.VERTICAL,false,"TREATMENT OUTCOME",new String[]{ "CURE, OUTCOME" , "TREATMENT COMPLETE" , "TUBERCULOSIS TREATMENT FAILURE" , "DIED" , "TRANSFERRED OUT" , "PATIENT REFERRED" , "LOST TO FOLLOW-UP" , "CLINICALLY EVALUATED, NO TB" , "ANTIBIOTIC COMPLETE - NO TB" , "NOT EVALUATED" , "TREATMENT ADAPTED" , "CONTACT DIAGNOSED WITH TB" , "REFUSAL OF TREATMENT BY PATIENT","PATIENT REFUSED TREATMENT AFTER STARTING" , "REFUSED SCREENING" , "PATIENT MOVED" , "TREATMENT STOPPED BY DOCTOR" , "TEST DONE, NO TB" , "OTHER TREATMENT OUTCOME"});
            else
                treatmentOutcome = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_treatment_outcome), getResources().getStringArray(R.array.treatment_outcome_list), getResources().getString(R.string.fast_cured), App.VERTICAL,false,"TREATMENT OUTCOME",new String[]{ "CURE, OUTCOME" , "TREATMENT COMPLETE" , "TUBERCULOSIS TREATMENT FAILURE" , "DIED" , "TRANSFERRED OUT" , "PATIENT REFERRED" , "LOST TO FOLLOW-UP" , "CLINICALLY EVALUATED, NO TB" , "ANTIBIOTIC COMPLETE - NO TB" , "NOT EVALUATED" , "TREATMENT ADAPTED" ,  "PATIENT MOVED" , "TREATMENT STOPPED BY DOCTOR" , "TEST DONE, NO TB" , "OTHER TREATMENT OUTCOME"});
        }

        String columnName = "";
        final Object[][] locations = serverService.getAllLocationsFromLocalDB(columnName);
        String[] locationArray = new String[locations.length + 2];
        locationArray[0] = "";
        int j = 1;
        for (int i = 0; i < locations.length; i++) {
            if (locations[i][16] != null) {
                locationArray[j] = String.valueOf(locations[i][16]);
                ++j;
            }
        }
        locationArray[j] = "Other";

        tbRegisterationNumber = new TitledEditText(context, null, getResources().getString(R.string.fast_tb_registeration_no), "", "", 20, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false,"TB REGISTRATION NUMBER");
        transferOutLocations = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_location_of_transfer_out), locationArray, "", App.VERTICAL, true);
        remarks = new TitledEditText(context, null, getResources().getString(R.string.fast_other_reason_remarks), "", "", 250, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false,"OTHER TREATMENT OUTCOME");
        treatmentInitiatedReferralSite = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_treatment_initiated_at_transfer_referral_site), getResources().getStringArray(R.array.fast_yes_no_unknown_list), getResources().getString(R.string.fast_dont_know_title), App.VERTICAL, App.VERTICAL,false,"TREATMENT INITIATED AT REFERRAL OR TRANSFER SITE",new String[]{ "YES" , "NO" , "UNKNOWN"});
        treatmentNotInitiatedReferralSite = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_reason_treatment_not_initiated_at_referral_site), getResources().getStringArray(R.array.fast_reason_treatment_not_initiated_referral_site_list), getResources().getString(R.string.fast_patient_could_not_be_contacted), App.VERTICAL,false,"TREATMENT NOT INITIATED AT REFERRAL OR TRANSFER SITE",new String[]{ "PATIENT COULD NOT BE CONTACTED" , "PATIENT LEFT THE CITY" , "REFUSAL OF TREATMENT BY PATIENT" , "DIED" , "DR NOT CONFIRMED BY BASELINE REPEAT TEST" , "OTHER REASON FOR TREATMENT NOT INITIATED"});
        treatmentNotInitiatedReferralSiteOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"OTHER REASON FOR TREATMENT NOT INITIATED");
        drConfirmation = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_dr_confirmation), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL,false,"DRUG RESISTANCE CONFIRMATION",getResources().getStringArray(R.array.yes_no_list_concept));
        enrsId = new TitledEditText(context, null, getResources().getString(R.string.fast_enrs_number), "", "", 20, RegexUtil.ERNS_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        endFollowupInstruction = new MyTextView(context, getResources().getString(R.string.fast_end_followup_instruction));
        endFollowupInstruction.setTextColor(Color.BLACK);
        endFollowupInstruction.setTypeface(null, Typeface.NORMAL);

        firstName = new TitledEditText(context, null, getResources().getString(R.string.fast_first_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true,"REFERRAL CONTACT FIRST NAME");
        lastName = new TitledEditText(context, null, getResources().getString(R.string.fast_last_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true,"REFERRAL CONTACT LAST NAME");

        mobileLinearLayout = new LinearLayout(context);
        mobileLinearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout mobileQuestion = new LinearLayout(context);
        mobileQuestion.setOrientation(LinearLayout.HORIZONTAL);
        MyTextView mobileNumberLabel = new MyTextView(context, getResources().getString(R.string.fast_mobile_number));
        mobileQuestion.addView(mobileNumberLabel);
        TextView mandatorySign = new TextView(context);
        mandatorySign.setText("*");
        mandatorySign.setTextColor(Color.parseColor("#ff0000"));
        mobileQuestion.addView(mandatorySign);
        mobileLinearLayout.addView(mobileQuestion);
        LinearLayout mobileNumberPart = new LinearLayout(context);
        mobileNumberPart.setOrientation(LinearLayout.HORIZONTAL);
        mobile1 = new MyEditText(context, "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        mobile1.setHint("0XXX");
        mobileNumberPart.addView(mobile1);
        MyTextView mobileNumberDash = new MyTextView(context, " - ");
        mobileNumberPart.addView(mobileNumberDash);
        mobile2 = new MyEditText(context, "", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        mobile2.setHint("XXXXXXX");
        mobileNumberPart.addView(mobile2);
        mobileLinearLayout.addView(mobileNumberPart);

        deathDate = new TitledButton(context, null, getResources().getString(R.string.date_of_death), "", App.VERTICAL);
        deathReason = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.primary_cause_of_death), getResources().getStringArray(R.array.cause_of_death_list), getResources().getString(R.string.unknown), App.VERTICAL,false,"CAUSE OF DEATH",new String[]{ "TB IMMEDIATE CAUSE OF DEATH" , "CAUSE RELATED TO TB TREATMENT" , "TB CONTRIBUTING TO DEATH" , "SURGERY RELATED DEATH" , "CAUSE OTHER THAN TB" , "UNKNOWN"});
        otherDeathReason = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"CAUSE OTHER THAN TB");

        reasonForFailure = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.reason_for_failure), getResources().getStringArray(R.array.reason_for_failure_list), getResources().getString(R.string.lack_of_conversion), App.VERTICAL,false,"REASON FOR TREATMENT FAILURE",new String[]{ "LACK OF CONVERSION" , "BACTERIOLOGICAL REVERSION" , "RESISTANCE TO FLUOROQUINOLONES AND INJECTABLES" , "ADVERSE DRUG REACTION" , "OTHER REASON FOR TREATMENT FAILURE"});
        reasonForFailureOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"OTHER REASON FOR TREATMENT FAILURE");

        String patientSource1 = serverService.getLatestEncounterDateTime(App.getPatientId(), "PET-Baseline Screening");
        if (patientSource1 != null)
            reasonForLossOfFollowup = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.reason_treatment_interruted), getResources().getStringArray(R.array.reason_treatment_interruted_list_contact), getResources().getString(R.string.relocated), App.VERTICAL,false,"REASON FOR LOST TO FOLLOW UP",new String[]{ "PATIENT REFUSED FOLLOW-UP" , "SUBSTANCE ABUSE" , "SOCIAL PROBLEM" , "LEFT REGION/COUNTRY" , "ADVERSE EVENTS" , "NO CONFIDENCE IN TREATMENT" , "CONTACT NOT ESTABLISHED" , "INDEX PATIENT REFUSED TREATMENT" , "INDEX PATIENT LOST TO FOLLOW UP" , "OTHER REASON TO END FOLLOW UP" , "UNKNOWN"});
        else {

            patientSource1 = serverService.getLatestEncounterDateTime(App.getPatientId(), "PET-Clinician Contact Screening");
            if (patientSource1 != null)
                reasonForLossOfFollowup = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.reason_treatment_interruted), getResources().getStringArray(R.array.reason_treatment_interruted_list_contact), getResources().getString(R.string.relocated), App.VERTICAL,false,"REASON FOR LOST TO FOLLOW UP",new String[]{ "PATIENT REFUSED FOLLOW-UP" , "SUBSTANCE ABUSE" , "SOCIAL PROBLEM" , "LEFT REGION/COUNTRY" , "ADVERSE EVENTS" , "NO CONFIDENCE IN TREATMENT" , "CONTACT NOT ESTABLISHED" , "INDEX PATIENT REFUSED TREATMENT" , "INDEX PATIENT LOST TO FOLLOW UP" , "OTHER REASON TO END FOLLOW UP" , "UNKNOWN"});
            else
                reasonForLossOfFollowup = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.reason_treatment_interruted), getResources().getStringArray(R.array.reason_treatment_interruted_list), getResources().getString(R.string.patient_refused_followup), App.VERTICAL,false,"REASON FOR LOST TO FOLLOW UP",new String[]{ "PATIENT REFUSED FOLLOW-UP" , "SUBSTANCE ABUSE" , "SOCIAL PROBLEM" , "LEFT REGION/COUNTRY" , "ADVERSE EVENTS" , "NO CONFIDENCE IN TREATMENT" , "OTHER REASON TO END FOLLOW UP" , "UNKNOWN"});
        }
        reasonForLossOfFollowupOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 255, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"OTHER REASON TO END FOLLOW UP");

        patientEvaluated = new TitledRadioGroup(context, null, getResources().getString(R.string.was_patient_tranferred_out), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL,false,"PATIENT TRANSFERRED OUT",getResources().getStringArray(R.array.yes_no_list_concept));
        patientNotEvaluatedReason = new TitledEditText(context, null, getResources().getString(R.string.reason_for_outcome), "", "", 255, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"OTHER REASON FOR NOT EVALUATED");


        // Used for reset fields...
        views = new View[]{formDate.getButton(), treatmentOutcome.getSpinner(), tbRegisterationNumber.getEditText(), transferOutLocations.getSpinner(), remarks.getEditText()
                , treatmentInitiatedReferralSite.getRadioGroup(), treatmentNotInitiatedReferralSite.getSpinner(), treatmentNotInitiatedReferralSiteOther.getEditText(),
                drConfirmation.getRadioGroup(), enrsId.getEditText(), firstName.getEditText(), lastName.getEditText(), mobile1, mobile2, deathDate.getButton(), deathReason.getSpinner(),
                otherDeathReason.getEditText(), reasonForFailure.getSpinner(), reasonForFailureOther.getEditText(), reasonForLossOfFollowup.getSpinner(), reasonForLossOfFollowupOther.getEditText(),
                patientEvaluated.getRadioGroup(), patientNotEvaluatedReason.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, treatmentOutcome, tbRegisterationNumber, reasonForLossOfFollowup, transferOutLocations, remarks, treatmentInitiatedReferralSite, treatmentNotInitiatedReferralSite
                        , treatmentNotInitiatedReferralSiteOther, drConfirmation, enrsId, endFollowupInstruction, firstName, lastName, mobileLinearLayout,
                        deathDate, deathReason, otherDeathReason, reasonForFailure, reasonForFailureOther, reasonForLossOfFollowupOther,
                        patientEvaluated, patientNotEvaluatedReason}};

        formDate.getButton().setOnClickListener(this);
        treatmentOutcome.getSpinner().setOnItemSelectedListener(this);
        treatmentNotInitiatedReferralSite.getSpinner().setOnItemSelectedListener(this);
        treatmentInitiatedReferralSite.getRadioGroup().setOnCheckedChangeListener(this);
        drConfirmation.getRadioGroup().setOnCheckedChangeListener(this);
        deathDate.getButton().setOnClickListener(this);
        deathReason.getSpinner().setOnItemSelectedListener(this);
        reasonForFailure.getSpinner().setOnItemSelectedListener(this);
        reasonForLossOfFollowup.getSpinner().setOnItemSelectedListener(this);
        patientEvaluated.getRadioGroup().setOnCheckedChangeListener(this);

        resetViews();

        mobile2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mobile1.requestFocus();
                    mobile1.setSelection(mobile1.getText().length());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mobile1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    mobile2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());


            if (secondDateCalendar.after(formDateCalendar) && !deathDate.getButton().getText().equals("") && deathDate.getVisibility() == View.VISIBLE) {

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.death_date_cannot_be_before_form_date), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                deathDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                secondDateCalendar = (Calendar) formDateCalendar.clone();

            }

        }

        if (!(deathDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString())) && deathDate.getVisibility() == View.VISIBLE) {

            String formDa = deathDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                if (!formDa.equals(""))
                    secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                if (!formDa.equals(""))
                    deathDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else if (secondDateCalendar.after(formDateCalendar)) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.death_date_cannot_be_before_form_date), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                if (!formDa.equals(""))
                    deathDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else
                deathDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }


        formDate.getButton().setEnabled(true);
        deathDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = super.validate();


        if (!App.get(transferOutLocations).equals("Other")) {
            if (!firstName.getEditText().getText().toString().trim().isEmpty() && firstName.getVisibility() == View.VISIBLE && App.get(firstName).length() == 1) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                firstName.getEditText().setError(getString(R.string.fast_name_less_than_2_characters));
                firstName.getEditText().requestFocus();
                error = true;
            }

          if (!lastName.getEditText().getText().toString().trim().isEmpty() && lastName.getVisibility() == View.VISIBLE && App.get(lastName).length() == 1) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                lastName.getEditText().setError(getString(R.string.fast_name_less_than_2_characters));
                lastName.getEditText().requestFocus();
                error = true;
            }

            if (mobileLinearLayout.getVisibility() == View.VISIBLE) {
                if (App.get(mobile1).equals("") && App.get(mobile2).equals("")) {
                    mobile2.setError(getString(R.string.empty_field));
                    mobile2.requestFocus();
                    error = true;
                    gotoLastPage();
                } else {

                    String mobile = App.get(mobile1) + App.get(mobile2);
                    if (!RegexUtil.isLandlineNumber(mobile)) {
                        mobile2.setError(getString(R.string.ctb_invalid_number));
                        mobile2.requestFocus();
                        error = true;
                        gotoLastPage();
                    }
                }
            }
        }


        if (tbRegisterationNumber.getEditText().getText().toString().length() > 0 && tbRegisterationNumber.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            tbRegisterationNumber.getEditText().setError(getString(R.string.invalid_value));
            tbRegisterationNumber.getEditText().requestFocus();
            error = true;
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(),R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            //  DrawableCompat.setTint(clearIcon, color);
            alertDialog.setIcon(clearIcon);
            alertDialog.setTitle(getResources().getString(R.string.title_error));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                InputMethodManager imm = (InputMethodManager) mainContent.getContext().getSystemService(mainContent.getContext().INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            return false;
        }
        return true;
    }

    @Override
    public boolean submit() {

        final ArrayList<String[]> observations =getObservations();

        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                Boolean flag = serverService.deleteOfflineForms(encounterId);
                if (!flag) {

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.form_does_not_exist));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    bundle.putBoolean("save", false);
                                    submit();
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.backToMainMenu();
                                    try {
                                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));

                    /*Toast.makeText(context, getString(R.string.form_does_not_exist),
                            Toast.LENGTH_LONG).show();*/

                    return false;
                }
                observations.add(new String[]{"TIME TAKEN TO FILL FORM", timeTakeToFill});
            } else {
                endTime = new Date();
                observations.add(new String[]{"TIME TAKEN TO FILL FORM", String.valueOf(App.getTimeDurationBetween(startTime, endTime))});
            }
            bundle.putBoolean("save", false);
        } else {
            endTime = new Date();
            observations.add(new String[]{"TIME TAKEN TO FILL FORM", String.valueOf(App.getTimeDurationBetween(startTime, endTime))});
        }


              if (transferOutLocations.getVisibility() == View.VISIBLE) {
            if (App.get(transferOutLocations).equals(getString(R.string.fast_other_title)))
                observations.add(new String[]{"TRANSFER OUT LOCATION", App.get(transferOutLocations)});
            else {
                String location = serverService.getLocationNameFromDescription(App.get(transferOutLocations));
                observations.add(new String[]{"TRANSFER OUT LOCATION", location});
            }

        }

        final String mobileNumber = mobile1.getText().toString() + "-" + mobile2.getText().toString();


        if (mobileLinearLayout.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REFERRAL CONTACT NUMBER", mobileNumber});

        if (deathDate.getVisibility() == View.VISIBLE && !deathDate.getButton().getText().equals(""))
            observations.add(new String[]{"DATE OF DEATH", App.getSqlDateTime(secondDateCalendar)});



        AsyncTask<String, String, String> submissionFormTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.submitting_form));
                        loading.show();
                    }
                });

                String id = null;
                if (App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocallyTesting(formName, form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = "";

                if (getEnrsVisibility()) {
                    result = serverService.saveIdentifier("ENRS", App.get(enrsId), id);
                    if (!result.equals("SUCCESS"))
                        return result;
                }

                result = serverService.saveEncounterAndObservationTesting(formName, form, formDateCalendar, observations.toArray(new String[][]{}), id);
                if (!result.contains("SUCCESS"))
                    return result;

                return "SUCCESS";
            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();

                if (result.equals("SUCCESS")) {
                    MainActivity.backToMainMenu();
                    try {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.form_submitted));
                    Drawable submitIcon = getResources().getDrawable(R.drawable.ic_submit);
                    alertDialog.setIcon(submitIcon);
                    int color = App.getColor(context, R.attr.colorAccent);
                    DrawableCompat.setTint(submitIcon, color);
                    alertDialog.setTitle(getResources().getString(R.string.title_completed));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else if (result.equals("CONNECTION_ERROR")) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.data_connection_error) + "\n\n (" + result + ")");
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    String message = getResources().getString(R.string.insert_error) + "\n\n (" + result + ")";
                    alertDialog.setMessage(message);
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

            }
        };
        submissionFormTask.execute("");

        return false;
    }

    @Override
    public boolean save() {

        HashMap<String, String> formValues = new HashMap<String, String>();

      /*  formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        formValues.put(lastName.getTag(), App.get(lastName));
        formValues.put(husbandName.getTag(), App.get(husbandName));
        formValues.put(gender.getTag(), App.get(gender));

        serverService.saveFormLocally(formName, "12345-5", formValues);*/

        return true;
    }

    @Override
    public void refill(int formId) {
        super.refill(formId);
        OfflineForm fo = serverService.getSavedFormById(formId);

        ArrayList<String[][]> obsValue = fo.getObsValue();
        enrsId.getEditText().setText(App.getPatient().getEnrs());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

           if (obs[0][0].equals("TRANSFER OUT LOCATION")) {
                transferOutLocations.getSpinner().selectValue(obs[0][1]);
                transferOutLocations.setVisibility(View.VISIBLE);
            } if (obs[0][0].equals("REFERRAL CONTACT NUMBER")) {
                String mobileNumArr[] = obs[0][1].split("-");
                mobile1.setText(mobileNumArr[0]);
                mobile2.setText(mobileNumArr[1]);
                mobile1.setVisibility(View.VISIBLE);
                mobile2.setVisibility(View.VISIBLE);
                mobileLinearLayout.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("DATE OF DEATH")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                deathDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                deathDate.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {

            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar,false,true, false);

            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);*/
        }

        if (view == deathDate.getButton()) {
            deathDate.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar,false,true, true);

            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            args.putString("formDate", formDate.getButtonText());*/
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == transferOutLocations.getSpinner()) {
            if (App.get(transferOutLocations).equals("Other")) {
                firstName.setVisibility(View.GONE);
                lastName.setVisibility(View.GONE);
                mobileLinearLayout.setVisibility(View.GONE);
            }

        }
        if (spinner == treatmentOutcome.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_transfer_out))) {
                transferOutLocations.setVisibility(View.VISIBLE);
            } else {
                transferOutLocations.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                remarks.setVisibility(View.VISIBLE);

            } else {
                remarks.setVisibility(View.GONE);
            }
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_referral_new))) {
                tbRegisterationNumber.setVisibility(View.VISIBLE);

            } else {
                tbRegisterationNumber.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_transfer_out)) ||
                    parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_referral_new))) {
                treatmentInitiatedReferralSite.setVisibility(View.VISIBLE);
                if (treatmentInitiatedReferralSite.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                    treatmentNotInitiatedReferralSite.setVisibility(View.VISIBLE);
                    if (treatmentNotInitiatedReferralSite.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                        treatmentNotInitiatedReferralSiteOther.setVisibility(View.VISIBLE);
                    } else {
                        treatmentNotInitiatedReferralSiteOther.setVisibility(View.GONE);
                    }

                } else {
                    treatmentNotInitiatedReferralSite.setVisibility(View.GONE);
                }
                endFollowupInstruction.setVisibility(View.VISIBLE);
                firstName.setVisibility(View.VISIBLE);
                lastName.setVisibility(View.VISIBLE);
                mobileLinearLayout.setVisibility(View.VISIBLE);
            } else {
                treatmentInitiatedReferralSite.setVisibility(View.GONE);
                treatmentNotInitiatedReferralSite.setVisibility(View.GONE);
                treatmentNotInitiatedReferralSiteOther.setVisibility(View.GONE);
                endFollowupInstruction.setVisibility(View.GONE);
                firstName.setVisibility(View.GONE);
                lastName.setVisibility(View.GONE);
                mobileLinearLayout.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_died))) {
                deathDate.setVisibility(View.VISIBLE);
                deathReason.setVisibility(View.VISIBLE);

                if (App.get(deathReason).equals(getResources().getString(R.string.cause_other_than_tb))) {
                    otherDeathReason.setVisibility(View.VISIBLE);
                } else {
                    otherDeathReason.setVisibility(View.GONE);
                }
            } else {
                deathDate.setVisibility(View.GONE);
                deathReason.setVisibility(View.GONE);
                otherDeathReason.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_treatment_failure))) {
                reasonForFailure.setVisibility(View.VISIBLE);
                if (App.get(reasonForFailure).equals(getString(R.string.other)))
                    reasonForFailureOther.setVisibility(View.VISIBLE);
                else
                    reasonForFailureOther.setVisibility(View.GONE);
                drConfirmation.setVisibility(View.VISIBLE);
                if (App.get(drConfirmation).equals(getResources().getString(R.string.yes)))
                    enrsId.setVisibility(View.VISIBLE);
                else
                    enrsId.setVisibility(View.GONE);
            } else {
                drConfirmation.setVisibility(View.GONE);
                enrsId.setVisibility(View.GONE);
                reasonForFailure.setVisibility(View.GONE);
                reasonForFailureOther.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_loss_to_follow_up))) {
                reasonForLossOfFollowup.setVisibility(View.VISIBLE);
                if (App.get(reasonForLossOfFollowup).equals(getString(R.string.other)))
                    reasonForLossOfFollowupOther.setVisibility(View.VISIBLE);
                else
                    reasonForLossOfFollowupOther.setVisibility(View.GONE);
            } else {
                reasonForLossOfFollowup.setVisibility(View.GONE);
                reasonForLossOfFollowupOther.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.not_evaluated))) {
                patientEvaluated.setVisibility(View.VISIBLE);
                if (App.get(patientEvaluated).equals(getString(R.string.no)))
                    patientNotEvaluatedReason.setVisibility(View.VISIBLE);
                else
                    patientNotEvaluatedReason.setVisibility(View.GONE);
            } else {
                patientEvaluated.setVisibility(View.GONE);
                patientNotEvaluatedReason.setVisibility(View.GONE);
            }


        } else if (spinner == treatmentNotInitiatedReferralSite.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                treatmentNotInitiatedReferralSiteOther.setVisibility(View.VISIBLE);
            } else {
                treatmentNotInitiatedReferralSiteOther.setVisibility(View.GONE);
            }
        } else if (spinner == deathReason.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.cause_other_than_tb))) {
                otherDeathReason.setVisibility(View.VISIBLE);
            } else {
                otherDeathReason.setVisibility(View.GONE);
            }
        } else if (spinner == reasonForFailure.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.other))) {
                reasonForFailureOther.setVisibility(View.VISIBLE);
            } else {
                reasonForFailureOther.setVisibility(View.GONE);
            }
        } else if (spinner == reasonForLossOfFollowup.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.other))) {
                reasonForLossOfFollowupOther.setVisibility(View.VISIBLE);
            } else {
                reasonForLossOfFollowupOther.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    private boolean getEnrsVisibility() {
        if (enrsId.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        deathDate.getButton().setText("");
        transferOutLocations.setVisibility(View.GONE);
        remarks.setVisibility(View.GONE);
        treatmentInitiatedReferralSite.setVisibility(View.GONE);
        treatmentNotInitiatedReferralSite.setVisibility(View.GONE);
        treatmentNotInitiatedReferralSiteOther.setVisibility(View.GONE);
        drConfirmation.setVisibility(View.GONE);
        endFollowupInstruction.setVisibility(View.GONE);
        firstName.setVisibility(View.GONE);
        lastName.setVisibility(View.GONE);
        mobileLinearLayout.setVisibility(View.GONE);
        enrsId.setVisibility(View.GONE);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);

                refill(formId);

            } else bundle.putBoolean("save", false);

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == treatmentInitiatedReferralSite.getRadioGroup()) {
            if (treatmentInitiatedReferralSite.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                treatmentNotInitiatedReferralSite.setVisibility(View.VISIBLE);
                if (App.get(treatmentNotInitiatedReferralSite).equals(getString(R.string.fast_other_title)))
                    treatmentNotInitiatedReferralSiteOther.setVisibility(View.VISIBLE);
                else
                    treatmentNotInitiatedReferralSiteOther.setVisibility(View.GONE);
            } else {
                treatmentNotInitiatedReferralSite.setVisibility(View.GONE);
                treatmentNotInitiatedReferralSiteOther.setVisibility(View.GONE);
            }
        } else if (radioGroup == drConfirmation.getRadioGroup()) {
            if (drConfirmation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                enrsId.setVisibility(View.VISIBLE);
            } else {
                enrsId.setVisibility(View.GONE);
            }
        } else if (radioGroup == patientEvaluated.getRadioGroup()) {
            if (patientEvaluated.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                patientNotEvaluatedReason.setVisibility(View.VISIBLE);
            } else {
                patientNotEvaluatedReason.setVisibility(View.GONE);
            }
        }
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageCount;
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
