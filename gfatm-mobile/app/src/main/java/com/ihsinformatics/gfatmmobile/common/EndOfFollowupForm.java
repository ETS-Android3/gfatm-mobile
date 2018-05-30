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
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 2/21/2017.
 */

public class EndOfFollowupForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    // Views...
    TitledButton formDate;
    TitledSpinner treatmentOutcome;
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
        treatmentOutcome = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_treatment_outcome), getResources().getStringArray(R.array.treatment_outcome_list), getResources().getString(R.string.fast_cured), App.VERTICAL);
        String columnName = "";
        final Object[][] locations = serverService.getAllLocationsFromLocalDB(columnName);
        String[] locationArray = new String[locations.length + 2];
        locationArray[0] = "";
        int j = 1;
        for (int i = 0; i < locations.length; i++) {
            locationArray[j] = String.valueOf(locations[i][16]);
            j++;
        }
        transferOutLocations = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_location_of_transfer_out), locationArray, "", App.VERTICAL, true);
        remarks = new TitledEditText(context, null, getResources().getString(R.string.fast_other_reason_remarks), "", "", 250, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        treatmentInitiatedReferralSite = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_treatment_initiated_at_transfer_referral_site), getResources().getStringArray(R.array.fast_yes_no_unknown_list), getResources().getString(R.string.fast_dont_know_title), App.VERTICAL, App.VERTICAL);
        treatmentNotInitiatedReferralSite = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_reason_treatment_not_initiated_at_referral_site), getResources().getStringArray(R.array.fast_reason_treatment_not_initiated_referral_site_list), getResources().getString(R.string.fast_patient_could_not_be_contacted), App.VERTICAL);
        treatmentNotInitiatedReferralSiteOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        drConfirmation = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_dr_confirmation), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        enrsId = new TitledEditText(context, null, getResources().getString(R.string.fast_enrs_number), "", "", 20, RegexUtil.ERNS_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        endFollowupInstruction = new MyTextView(context, getResources().getString(R.string.fast_end_followup_instruction));
        endFollowupInstruction.setTextColor(Color.BLACK);
        endFollowupInstruction.setTypeface(null, Typeface.NORMAL);

        firstName = new TitledEditText(context, null, getResources().getString(R.string.fast_first_name), "", "", 40, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        lastName = new TitledEditText(context, null, getResources().getString(R.string.fast_last_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

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
        mobile1 = new MyEditText(context,"", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        mobile1.setHint("0XXX");
        mobileNumberPart.addView(mobile1);
        MyTextView mobileNumberDash = new MyTextView(context, " - ");
        mobileNumberPart.addView(mobileNumberDash);
        mobile2 = new MyEditText(context,"",  7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        mobile2.setHint("XXXXXXX");
        mobileNumberPart.addView(mobile2);
        mobileLinearLayout.addView(mobileNumberPart);

        deathDate = new TitledButton(context, null, getResources().getString(R.string.date_of_death), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.VERTICAL);
        deathReason = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.primary_cause_of_death), getResources().getStringArray(R.array.cause_of_death_list), getResources().getString(R.string.unknown), App.VERTICAL);
        otherDeathReason = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        reasonForFailure = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.reason_for_failure), getResources().getStringArray(R.array.reason_for_failure_list), getResources().getString(R.string.lack_of_conversion), App.VERTICAL);
        reasonForFailureOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        reasonForLossOfFollowup = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.reason_treatment_interruted), getResources().getStringArray(R.array.reason_treatment_interruted_list), getResources().getString(R.string.patient_refused_followup), App.VERTICAL);
        reasonForLossOfFollowupOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 255, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        patientEvaluated = new TitledRadioGroup(context, null, getResources().getString(R.string.was_patient_tranferred_out), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        patientNotEvaluatedReason = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 255, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), treatmentOutcome.getSpinner(), transferOutLocations.getSpinner(), remarks.getEditText()
                , treatmentInitiatedReferralSite.getRadioGroup(), treatmentNotInitiatedReferralSite.getSpinner(), treatmentNotInitiatedReferralSiteOther.getEditText(),
                drConfirmation.getRadioGroup(), enrsId.getEditText(), firstName.getEditText(), lastName.getEditText(), mobile1, mobile2, deathDate.getButton(), deathReason.getSpinner(),
                otherDeathReason.getEditText(), reasonForFailure.getSpinner(), reasonForFailureOther.getEditText(), reasonForLossOfFollowup.getSpinner(), reasonForLossOfFollowupOther.getEditText(),
                patientEvaluated.getRadioGroup(), patientNotEvaluatedReason.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, treatmentOutcome, transferOutLocations, remarks, treatmentInitiatedReferralSite, treatmentNotInitiatedReferralSite
                        , treatmentNotInitiatedReferralSiteOther, drConfirmation, enrsId, endFollowupInstruction,firstName, lastName, mobileLinearLayout,
                        deathDate, deathReason, otherDeathReason, reasonForFailure, reasonForFailureOther, reasonForLossOfFollowup, reasonForLossOfFollowupOther,
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
                if(s.length()==0){
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
                if(s.length()==4){
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
        }

        if (!(deathDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = deathDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                deathDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                deathDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else if (secondDateCalendar.before(formDateCalendar)) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.death_date_cannot_be_before_form_date), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                deathDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                deathDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }


        formDate.getButton().setEnabled(true);

    }

    @Override
    public boolean validate() {
        Boolean error = false;

        if (treatmentNotInitiatedReferralSiteOther.getVisibility() == View.VISIBLE && treatmentNotInitiatedReferralSiteOther.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            treatmentNotInitiatedReferralSiteOther.getEditText().setError(getString(R.string.empty_field));
            treatmentNotInitiatedReferralSiteOther.getEditText().requestFocus();
            error = true;
        }

        if (enrsId.getVisibility() == View.VISIBLE && enrsId.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            enrsId.getEditText().setError(getString(R.string.empty_field));
            enrsId.getEditText().requestFocus();
            error = true;
        }

        if (firstName.getVisibility() == View.VISIBLE && firstName.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            firstName.getEditText().setError(getString(R.string.empty_field));
            firstName.getEditText().requestFocus();
            error = true;
        } else if (firstName.getVisibility() == View.VISIBLE && App.get(firstName).length() == 1) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            firstName.getEditText().setError(getString(R.string.fast_name_less_than_2_characters));
            firstName.getEditText().requestFocus();
            error = true;
        }

        if (lastName.getVisibility() == View.VISIBLE && lastName.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            lastName.getEditText().setError(getString(R.string.empty_field));
            lastName.getEditText().requestFocus();
            error = true;
        } else if (lastName.getVisibility() == View.VISIBLE && App.get(lastName).length() == 1) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            lastName.getEditText().setError(getString(R.string.fast_name_less_than_2_characters));
            lastName.getEditText().requestFocus();
            error = true;
        }

        if(mobileLinearLayout.getVisibility() == View.VISIBLE) {
            if (App.get(mobile1).equals("") && App.get(mobile2).equals("")) {

                mobile2.setError(null);
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

        if (otherDeathReason.getVisibility() == View.VISIBLE && otherDeathReason.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherDeathReason.getEditText().setError(getString(R.string.empty_field));
            otherDeathReason.getEditText().requestFocus();
            error = true;
        }

        if (reasonForFailureOther.getVisibility() == View.VISIBLE && reasonForFailureOther.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reasonForFailureOther.getEditText().setError(getString(R.string.empty_field));
            reasonForFailureOther.getEditText().requestFocus();
            error = true;
        }

        if (reasonForLossOfFollowupOther.getVisibility() == View.VISIBLE && reasonForLossOfFollowupOther.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reasonForLossOfFollowupOther.getEditText().setError(getString(R.string.empty_field));
            reasonForLossOfFollowupOther.getEditText().requestFocus();
            error = true;
        }

        if (patientNotEvaluatedReason.getVisibility() == View.VISIBLE && patientNotEvaluatedReason.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            patientNotEvaluatedReason.getEditText().setError(getString(R.string.empty_field));
            patientNotEvaluatedReason.getEditText().requestFocus();
            error = true;
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
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

        final ArrayList<String[]> observations = new ArrayList<String[]>();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
                observations.add(new String[]{"TIME TAKEN TO FILL FORM", timeTakeToFill});
            }else {
                endTime = new Date();
                observations.add(new String[]{"TIME TAKEN TO FILL FORM", String.valueOf(App.getTimeDurationBetween(startTime, endTime))});
            }
            bundle.putBoolean("save", false);
        } else {
            endTime = new Date();
            observations.add(new String[]{"TIME TAKEN TO FILL FORM", String.valueOf(App.getTimeDurationBetween(startTime, endTime))});
        }

        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});

        final String mobileNumber = mobile1.getText().toString() + "-" + mobile2.getText().toString();

        if (treatmentOutcome.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOUS TREATMENT OUTCOME", App.get(treatmentOutcome).equals(getResources().getString(R.string.fast_cured)) ? "CURE, OUTCOME" :
                    (App.get(treatmentOutcome).equals(getResources().getString(R.string.fast_treatment_completed)) ? "TREATMENT COMPLETE" :
                            (App.get(treatmentOutcome).equals(getResources().getString(R.string.fast_treatment_failure)) ? "TUBERCULOSIS TREATMENT FAILURE" :
                                    (App.get(treatmentOutcome).equals(getResources().getString(R.string.fast_died)) ? "DIED" :
                                            (App.get(treatmentOutcome).equals(getResources().getString(R.string.fast_transfer_out)) ? "TRANSFERRED OUT" :
                                                    (App.get(treatmentOutcome).equals(getResources().getString(R.string.fast_referral)) ? "PATIENT REFERRED" :
                                                            (App.get(treatmentOutcome).equals(getResources().getString(R.string.fast_treatment_after_loss_to_follow_up)) ? "LOST TO FOLLOW-UP" :
                                                                    (App.get(treatmentOutcome).equals(getResources().getString(R.string.fast_clinically_evaluated_no_tb)) ? "CLINICALLY EVALUATED, NO TB" :
                                                                            (App.get(treatmentOutcome).equals(getResources().getString(R.string.fast_antibiotic_complete)) ? "ANTIBIOTIC COMPLETE - NO TB" :
                                                                                    (App.get(treatmentOutcome).equals(getResources().getString(R.string.not_evaluated)) ? "NOT EVALUATED" :
                                                                                            (App.get(treatmentOutcome).equals(getResources().getString(R.string.treatment_adapted)) ? "TREATMENT ADAPTED" : "OTHER TREATMENT OUTCOME"))))))))))});

        if (transferOutLocations.getVisibility() == View.VISIBLE) {
            if(App.get(transferOutLocations).equals(getString(R.string.fast_other_title)))
                observations.add(new String[]{"TRANSFER OUT LOCATION", App.get(transferOutLocations)});
            else{
                String location = serverService.getLocationNameFromDescription(App.get(transferOutLocations));
                observations.add(new String[]{"TRANSFER OUT LOCATION", location});
            }

        }

        if (remarks.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON TO END FOLLOW UP", App.get(remarks)});

        if (treatmentInitiatedReferralSite.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT INITIATED AT REFERRAL OR TRANSFER SITE", App.get(treatmentInitiatedReferralSite).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(treatmentInitiatedReferralSite).equals(getResources().getString(R.string.fast_no_title)) ? "NO" : "UNKNOWN")});


        if (treatmentNotInitiatedReferralSite.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT NOT INITIATED AT REFERRAL OR TRANSFER SITE", App.get(treatmentNotInitiatedReferralSite).equals(getResources().getString(R.string.fast_patient_could_not_be_contacted)) ? "PATIENT COULD NOT BE CONTACTED" :
                    (App.get(treatmentNotInitiatedReferralSite).equals(getResources().getString(R.string.fast_patient_left_the_city)) ? "PATIENT LEFT THE CITY" :
                            (App.get(treatmentNotInitiatedReferralSite).equals(getResources().getString(R.string.fast_patient_refused_treatment)) ? "REFUSAL OF TREATMENT BY PATIENT" :
                                    (App.get(treatmentNotInitiatedReferralSite).equals(getResources().getString(R.string.fast_patient_died)) ? "DIED" :
                                            (App.get(treatmentNotInitiatedReferralSite).equals(getResources().getString(R.string.fast_dr_not_confirmed_by_baseline_repeat_test)) ? "DR NOT CONFIRMED BY BASELINE REPEAT TEST" : "OTHER REASON FOR TREATMENT NOT INITIATED"))))});


        if (treatmentNotInitiatedReferralSiteOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON FOR TREATMENT NOT INITIATED", App.get(treatmentNotInitiatedReferralSiteOther)});

        if (drConfirmation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DRUG RESISTANCE CONFIRMATION", App.get(drConfirmation).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});

        if (firstName.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REFERRAL CONTACT FIRST NAME", App.get(firstName)});

        if (lastName.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REFERRAL CONTACT LAST NAME", App.get(lastName)});

        if (mobileLinearLayout.getVisibility() == View.VISIBLE)
        observations.add(new String[]{"REFERRAL CONTACT NUMBER", mobileNumber});

        if (deathDate.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DATE OF DEATH", App.getSqlDateTime(secondDateCalendar)});

        if (deathReason.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CAUSE OF DEATH", App.get(deathReason).equals(getResources().getString(R.string.tb_immediate_cause_of_death)) ? "TB IMMEDIATE CAUSE OF DEATH" :
                    (App.get(deathReason).equals(getResources().getString(R.string.cause_related_to_tb_treatment)) ? "CAUSE RELATED TO TB TREATMENT" :
                            (App.get(deathReason).equals(getResources().getString(R.string.tb_contributing_to_death)) ? "TB CONTRIBUTING TO DEATH" :
                                    (App.get(deathReason).equals(getResources().getString(R.string.surgery_related_death)) ? "SURGERY RELATED DEATH" :
                                            (App.get(deathReason).equals(getResources().getString(R.string.cause_other_than_tb)) ? "CAUSE OTHER THAN TB" : "UNKNOWN"))))});

        if (otherDeathReason.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CAUSE OTHER THAN TB", App.get(otherDeathReason)});

        if (reasonForFailure.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON FOR TREATMENT FAILURE", App.get(reasonForFailure).equals(getResources().getString(R.string.lack_of_conversion)) ? "LACK OF CONVERSION" :
                    (App.get(reasonForFailure).equals(getResources().getString(R.string.bacteriological_reversion)) ? "BACTERIOLOGICAL REVERSION" :
                            (App.get(reasonForFailure).equals(getResources().getString(R.string.resistance_to_ffq_injectables)) ? "RESISTANCE TO FLUOROQUINOLONES AND INJECTABLES" :
                                    (App.get(reasonForFailure).equals(getResources().getString(R.string.adverse_drug_reaction)) ? "ADVERSE DRUG REACTION" : "OTHER REASON FOR TREATMENT FAILURE")))});

        if (reasonForFailure.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON FOR TREATMENT FAILURE", App.get(reasonForFailure)});

        if (reasonForLossOfFollowup.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON FOR LOST TO FOLLOW UP", App.get(reasonForLossOfFollowup).equals(getResources().getString(R.string.patient_refused_followup)) ? "PATIENT REFUSED FOLLOW-UP" :
                    (App.get(reasonForLossOfFollowup).equals(getResources().getString(R.string.substance_abuse)) ? "SUBSTANCE ABUSE" :
                            (App.get(reasonForLossOfFollowup).equals(getResources().getString(R.string.social_problem)) ? "SOCIAL PROBLEM" :
                                    (App.get(reasonForLossOfFollowup).equals(getResources().getString(R.string.left_region_coutry)) ? "LEFT REGION/COUNTRY" :
                                            (App.get(reasonForLossOfFollowup).equals(getResources().getString(R.string.adverse_event)) ? "ADVERSE EVENTS" :
                                                    (App.get(reasonForLossOfFollowup).equals(getResources().getString(R.string.no_confidence_in_treatment)) ? "NO CONFIDENCE IN TREATMENT" :
                                                            (App.get(reasonForLossOfFollowup).equals(getResources().getString(R.string.other)) ? "OTHER REASON TO END FOLLOW UP" : "UNKNOWN"))))))});

        if (reasonForLossOfFollowupOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON TO END FOLLOW UP", App.get(reasonForLossOfFollowupOther)});

        if (patientEvaluated.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT TRANSFERRED OUT", App.get(patientEvaluated).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if (patientNotEvaluatedReason.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON FOR NOT EVALUATED", App.get(patientNotEvaluatedReason)});


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
                if(App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocallyTesting("End of Followup", form, formDateCalendar,observations.toArray(new String[][]{}));

                String result = "";

                if (getEnrsVisibility()) {
                    result = serverService.saveIdentifier("ENRS", App.get(enrsId), id);
                    if (!result.equals("SUCCESS"))
                        return result;
                }

                result = serverService.saveEncounterAndObservationTesting("End of Followup", form, formDateCalendar, observations.toArray(new String[][]{}), id);
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

        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if(obs[0][0].equals("TIME TAKEN TO FILL form")){
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("TUBERCULOUS TREATMENT OUTCOME")) {
                String value = obs[0][1].equals("CURE, OUTCOME") ? getResources().getString(R.string.fast_cured) :
                        (obs[0][1].equals("TREATMENT COMPLETE") ? getResources().getString(R.string.fast_treatment_completed) :
                                (obs[0][1].equals("TUBERCULOSIS TREATMENT FAILURE") ? getResources().getString(R.string.fast_treatment_failure) :
                                        (obs[0][1].equals("DIED") ? getResources().getString(R.string.fast_died) :
                                                (obs[0][1].equals("TRANSFERRED OUT") ? getResources().getString(R.string.fast_transfer_out) :
                                                        (obs[0][1].equals("PATIENT REFERRED") ? getResources().getString(R.string.fast_referral) :
                                                                (obs[0][1].equals("LOST TO FOLLOW-UP") ? getResources().getString(R.string.fast_treatment_after_loss_to_follow_up) :
                                                                        (obs[0][1].equals("CLINICALLY EVALUATED, NO TB") ? getResources().getString(R.string.fast_clinically_evaluated_no_tb) :
                                                                                (obs[0][1].equals("ANTIBIOTIC COMPLETE - NO TB") ? getResources().getString(R.string.fast_antibiotic_complete) :
                                                                        getResources().getString(R.string.fast_other_title)))))))));

                treatmentOutcome.getSpinner().selectValue(value);
                treatmentOutcome.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TRANSFER OUT LOCATION")) {
                transferOutLocations.getSpinner().selectValue(obs[0][1]);
                transferOutLocations.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER REASON TO END FOLLOW UP")) {
                remarks.getEditText().setText(obs[0][1]);
                remarks.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT INITIATED AT REFERRAL OR TRANSFER SITE")) {
                for (RadioButton rb : treatmentInitiatedReferralSite.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_dont_know_title)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                treatmentInitiatedReferralSite.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT NOT INITIATED AT REFERRAL OR TRANSFER SITE")) {
                String value = obs[0][1].equals("PATIENT COULD NOT BE CONTACTED") ? getResources().getString(R.string.fast_cured) :
                        (obs[0][1].equals("PATIENT LEFT THE CITY") ? getResources().getString(R.string.fast_treatment_completed) :
                                (obs[0][1].equals("REFUSAL OF TREATMENT BY PATIENT") ? getResources().getString(R.string.fast_treatment_failure) :
                                        (obs[0][1].equals("DIED") ? getResources().getString(R.string.fast_died) :
                                                (obs[0][1].equals("DR NOT CONFIRMED BY BASELINE REPEAT TEST") ? getResources().getString(R.string.fast_transfer_out) :
                                                        getResources().getString(R.string.fast_other_title)))));

                treatmentNotInitiatedReferralSite.getSpinner().selectValue(value);
                treatmentNotInitiatedReferralSite.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER REASON FOR TREATMENT NOT INITIATED")) {
                treatmentNotInitiatedReferralSiteOther.getEditText().setText(obs[0][1]);
                treatmentNotInitiatedReferralSiteOther.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("DRUG RESISTANCE CONFIRMATION")) {
                for (RadioButton rb : drConfirmation.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                drConfirmation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ENRS")) {
                enrsId.getEditText().setText(obs[0][1]);
                enrsId.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REFERRAL CONTACT FIRST NAME")) {
                firstName.getEditText().setText(obs[0][1]);
                firstName.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REFERRAL CONTACT LAST NAME")) {
                lastName.getEditText().setText(obs[0][1]);
                lastName.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REFERRAL CONTACT NUMBER")) {
                String mobileNumArr[] = obs[0][1].split("-");
                mobile1.setText(mobileNumArr[0]);
                mobile2.setText(mobileNumArr[1]);
                mobile1.setVisibility(View.VISIBLE);
                mobile2.setVisibility(View.VISIBLE);
                mobileLinearLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            formDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
        }

        if (view == deathDate.getButton()) {
            deathDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == treatmentOutcome.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_transfer_out))) {
                transferOutLocations.setVisibility(View.VISIBLE);
            } else {
                transferOutLocations.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title)) ||
                    parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_referral_new)) ||
                    parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_loss_to_follow_up))) {
                remarks.setVisibility(View.VISIBLE);

            } else {
                remarks.setVisibility(View.GONE);
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
                if(App.get(reasonForFailure).equals(getString(R.string.other)))
                    reasonForFailureOther.setVisibility(View.VISIBLE);
                else
                    reasonForFailureOther.setVisibility(View.GONE);
            } else {
                reasonForFailure.setVisibility(View.GONE);
                reasonForFailureOther.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_loss_to_follow_up))) {
                reasonForLossOfFollowup.setVisibility(View.VISIBLE);
                if(App.get(reasonForLossOfFollowup).equals(getString(R.string.other)))
                    reasonForLossOfFollowupOther.setVisibility(View.VISIBLE);
                else
                    reasonForLossOfFollowupOther.setVisibility(View.GONE);
            } else {
                reasonForLossOfFollowup.setVisibility(View.GONE);
                reasonForLossOfFollowupOther.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.not_evaluated))) {
                patientEvaluated.setVisibility(View.VISIBLE);
                if(App.get(patientEvaluated).equals(getString(R.string.no)))
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
        } else if (spinner == deathReason.getSpinner()){
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.cause_other_than_tb))) {
                otherDeathReason.setVisibility(View.VISIBLE);
            } else {
                otherDeathReason.setVisibility(View.GONE);
            }
        } else if (spinner == reasonForFailure.getSpinner()){
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.other))) {
                reasonForFailureOther.setVisibility(View.VISIBLE);
            } else {
                reasonForFailureOther.setVisibility(View.GONE);
            }
        }  else if (spinner == reasonForLossOfFollowup.getSpinner()){
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

    private boolean getEnrsVisibility(){
      if(enrsId.getVisibility() == View.VISIBLE){
          return true;
      }
        return false;
    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
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
                if(App.get(treatmentNotInitiatedReferralSite).equals(getString(R.string.fast_other_title)))
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
        }  else if (radioGroup == patientEvaluated.getRadioGroup()) {
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
