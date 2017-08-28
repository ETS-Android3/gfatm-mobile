package com.ihsinformatics.gfatmmobile.comorbidities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.CheckBox;
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
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Fawad Jawaid on 03-Jan-17.
 */

public class ComorbiditiesDiabetesTreatmentInitiationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    MyTextView previousHistory;
    TitledRadioGroup diabetesFamilyHistory;
    TitledCheckBoxes diabetesFamilyHistorySpecify;
    TitledRadioGroup previousDiabetesTreatmentHistory;
    //TitledRadioGroup selfReportedDiabetesTreatmentHistory;
    TitledCheckBoxes selfReportedDiabetesTreatmentHistory;
    TitledRadioGroup typeDiabetes;
    TitledRadioGroup statusDiabetes;
    TitledRadioGroup hxHypoglycemia;
    TitledRadioGroup hxDiabeticRetinopathy;
    TitledRadioGroup hxDiabeticNeuropathy;
    TitledRadioGroup hxDiabeticNephropathy;
    TitledCheckBoxes hxDiabeticInfections;
    TitledRadioGroup hxDiabeticPvd;
    TitledRadioGroup hxDiabeticCad;
    TitledRadioGroup hxDiabeticHypertension;
    TitledRadioGroup hxDiabeticGestationalDiabetes;
    TitledRadioGroup hxDiabeticBirth;

    MyTextView treatmentInitiation;
    TitledCheckBoxes diabetesTreatmentInitiation;
    TitledEditText diabetesTreatmentDetail;
    TitledRadioGroup diabetesTreatmentInitiationMetformin;
    TitledEditText diabetesTreatmentInitiationTabletsPerDay;
    TitledEditText diabetesTreatmentInitiationMetforminPrescribed;
    TitledEditText diabetesTreatmentInitiationInsulinN;
    TitledEditText diabetesTreatmentInitiationInsulinR;
    TitledEditText diabetesTreatmentInitiationInsulinMix;
    TitledButton diabetesNextScheduledVisit;

    ScrollView scrollView;

    /**
     * CHANGE PAGE_COUNT and FORM_NAME Variable only...
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 7;
        FORM_NAME = Forms.COMORBIDITIES_DIABETES_TREATMENT_INITIATION;
        FORM =  Forms.comorbidities_diabetesTreatmentInitiationForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesDiabetesTreatmentInitiationForm.MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(PAGE_COUNT - 1);
        formName.setText(FORM_NAME);

        initViews();

        groups = new ArrayList<ViewGroup>();

        if (App.isLanguageRTL()) {
            for (int i = PAGE_COUNT - 1; i >= 0; i--) {
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
            for (int i = 0; i < PAGE_COUNT; i++) {
                LinearLayout layout = new LinearLayout(mainContent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(mainContent.getContext());
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
        formDate.setTag("formDate");
        previousHistory = new MyTextView(context, getResources().getString(R.string.comorbidities_diabetes_previous_history));
        previousHistory.setTypeface(null, Typeface.BOLD);
        diabetesFamilyHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_diabetes_family_history), getResources().getStringArray(R.array.comorbidities_diabetes_family_history_options), getResources().getString(R.string.comorbidities_diabetes_family_history_no), App.HORIZONTAL, App.VERTICAL);
        diabetesFamilyHistorySpecify = new TitledCheckBoxes(context, null, getResources().getString(R.string.comorbidities_diabetes_family_history_specify), getResources().getStringArray(R.array.comorbidities_diabetes_family_history_specify_options1), new Boolean[]{true, false, false, false, false, false, false, false, false, false, false, false, false, false}, App.VERTICAL, App.VERTICAL);
        previousDiabetesTreatmentHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_previous_diabetes_treatment_history), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        showDiabetesFamilyHistorySpecify();
        //selfReportedDiabetesTreatmentHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_self_reported_diabetes_treatment_history), getResources().getStringArray(R.array.comorbidities_self_reported_diabetes_treatment_history_options), getResources().getString(R.string.comorbidities_self_reported_diabetes_treatment_history_both), App.HORIZONTAL, App.VERTICAL);
        selfReportedDiabetesTreatmentHistory = new TitledCheckBoxes(context, null, getResources().getString(R.string.comorbidities_self_reported_diabetes_treatment_history), getResources().getStringArray(R.array.comorbidities_self_reported_diabetes_treatment_history_options), new Boolean[]{true, true}, App.VERTICAL, App.VERTICAL);
        showSelfReportedDiabetesTreatmentHistory();
        typeDiabetes = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_type_diabetes), getResources().getStringArray(R.array.comorbidities_type_diabetes_types), getResources().getString(R.string.comorbidities_type_diabetes_type2), App.HORIZONTAL, App.VERTICAL);
        showDiabetesType();
        statusDiabetes = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_status_diabetes), getResources().getStringArray(R.array.comorbidities_status_diabetes_options), getResources().getString(R.string.comorbidities_status_diabetes_uncontrolled), App.HORIZONTAL, App.VERTICAL);
        hxHypoglycemia = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_hypoglycemia), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no), App.HORIZONTAL, App.VERTICAL);
        hxDiabeticRetinopathy = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_retinopathy), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no), App.HORIZONTAL, App.VERTICAL);
        hxDiabeticNeuropathy = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_neuropathy), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no), App.HORIZONTAL, App.VERTICAL);
        hxDiabeticNephropathy = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_nephropathy), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no), App.HORIZONTAL, App.VERTICAL);
        hxDiabeticInfections = new TitledCheckBoxes(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_infections), getResources().getStringArray(R.array.comorbidities_hx_diabetic_infections_options), new Boolean[]{true, false, false, false, false, false, false}, App.VERTICAL, App.VERTICAL);
        hxDiabeticPvd = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_pvd), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no), App.HORIZONTAL, App.VERTICAL);
        hxDiabeticCad = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_cad), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no), App.HORIZONTAL, App.VERTICAL);
        hxDiabeticHypertension = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_hypertension), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no), App.HORIZONTAL, App.VERTICAL);
        hxDiabeticGestationalDiabetes = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_gestational_diabetes), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no), App.HORIZONTAL, App.VERTICAL);
        showGestationalDiabetesDuringPregnancy();
        hxDiabeticBirth = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_birth), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no), App.HORIZONTAL, App.VERTICAL);
        showHxDiabeticBirth();

        treatmentInitiation = new MyTextView(context, getResources().getString(R.string.comorbidities_treatment_initiation));
        treatmentInitiation.setTypeface(null, Typeface.BOLD);
        diabetesTreatmentInitiation = new TitledCheckBoxes(context, null, getResources().getString(R.string.comorbidities_diabetes_treatment_initiation), getResources().getStringArray(R.array.comorbidities_diabetes_treatment_initiation_options), new Boolean[]{true, false, false, false, false, false, false, false}, App.VERTICAL, App.VERTICAL);
        diabetesTreatmentDetail = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_treatment_detail), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        diabetesTreatmentDetail.getEditText().setSingleLine(false);
        diabetesTreatmentDetail.getEditText().setMinimumHeight(100);
        diabetesTreatmentInitiationMetformin = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin), getResources().getStringArray(R.array.comorbidities_diabetes_treatment_initiation_metformin_options), getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_500), App.HORIZONTAL, App.VERTICAL);
        diabetesTreatmentInitiationTabletsPerDay = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_per_day), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        diabetesTreatmentInitiationMetforminPrescribed = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_prescribed), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        diabetesTreatmentInitiationInsulinN = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulinN), "", getResources().getString(R.string.comorbidities_vitals_waist_hip_whr_range), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        diabetesTreatmentInitiationInsulinR = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulinR), "", getResources().getString(R.string.comorbidities_vitals_waist_hip_whr_range), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        diabetesTreatmentInitiationInsulinMix = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulin_mix), "", getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulin_mix_range), 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        diabetesNextScheduledVisit = new TitledButton(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_next_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), diabetesFamilyHistory.getRadioGroup(), diabetesFamilyHistorySpecify,
                previousDiabetesTreatmentHistory.getRadioGroup(), selfReportedDiabetesTreatmentHistory, typeDiabetes.getRadioGroup(), statusDiabetes.getRadioGroup(),
                hxHypoglycemia.getRadioGroup(), hxDiabeticRetinopathy.getRadioGroup(), hxDiabeticNeuropathy.getRadioGroup(), hxDiabeticNephropathy.getRadioGroup(),
                hxDiabeticInfections, hxDiabeticPvd.getRadioGroup(),
                hxDiabeticCad.getRadioGroup(), hxDiabeticHypertension.getRadioGroup(), hxDiabeticGestationalDiabetes.getRadioGroup(), hxDiabeticBirth.getRadioGroup(),
                treatmentInitiation, diabetesTreatmentInitiation, diabetesTreatmentInitiationTabletsPerDay.getEditText(), diabetesTreatmentInitiationMetforminPrescribed.getEditText(), diabetesTreatmentDetail.getEditText(),
                diabetesTreatmentInitiationMetformin.getRadioGroup(), diabetesTreatmentInitiationInsulinN.getEditText(), diabetesTreatmentInitiationInsulinR.getEditText(), diabetesTreatmentInitiationInsulinMix.getEditText(), diabetesNextScheduledVisit.getButton()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, previousHistory, diabetesFamilyHistory, diabetesFamilyHistorySpecify},
                        {previousDiabetesTreatmentHistory, selfReportedDiabetesTreatmentHistory, typeDiabetes, statusDiabetes},
                        {hxHypoglycemia, hxDiabeticRetinopathy, hxDiabeticNeuropathy, hxDiabeticNephropathy},
                        {hxDiabeticInfections, hxDiabeticPvd},
                        {hxDiabeticCad, hxDiabeticHypertension, hxDiabeticGestationalDiabetes, hxDiabeticBirth},
                        {treatmentInitiation, diabetesTreatmentInitiation, diabetesTreatmentInitiationTabletsPerDay, diabetesTreatmentInitiationMetforminPrescribed},
                        {diabetesTreatmentDetail, diabetesTreatmentInitiationMetformin, diabetesTreatmentInitiationInsulinN, diabetesTreatmentInitiationInsulinR, diabetesTreatmentInitiationInsulinMix, diabetesNextScheduledVisit}};

        diabetesTreatmentInitiationMetformin.setVisibility(View.GONE);

        diabetesTreatmentInitiationInsulinN.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (diabetesTreatmentInitiationInsulinN.getEditText().getText().length() > 0) {
                        double num = Integer.parseInt(diabetesTreatmentInitiationInsulinN.getEditText().getText().toString());
                        if (num < 0 || num > 100) {
                            diabetesTreatmentInitiationInsulinN.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        diabetesTreatmentInitiationInsulinR.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (diabetesTreatmentInitiationInsulinR.getEditText().getText().length() > 0) {
                        double num = Integer.parseInt(diabetesTreatmentInitiationInsulinR.getEditText().getText().toString());
                        if (num < 0 || num > 100) {
                            diabetesTreatmentInitiationInsulinR.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        diabetesTreatmentInitiationInsulinMix.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (diabetesTreatmentInitiationInsulinMix.getEditText().getText().length() > 0) {
                        double num = Integer.parseInt(diabetesTreatmentInitiationInsulinMix.getEditText().getText().toString());
                        if (num < 0 || num > 1000) {
                            diabetesTreatmentInitiationInsulinMix.getEditText().setError(getString(R.string.comorbidities_diabetes_treatment_initiation_insulin_mix_limit));
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        formDate.getButton().setOnClickListener(this);
        diabetesNextScheduledVisit.getButton().setOnClickListener(this);
        diabetesFamilyHistory.getRadioGroup().setOnCheckedChangeListener(this);
        previousDiabetesTreatmentHistory.getRadioGroup().setOnCheckedChangeListener(this);
        hxDiabeticGestationalDiabetes.getRadioGroup().setOnCheckedChangeListener(this);

        for (CheckBox cb : diabetesFamilyHistorySpecify.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        for (CheckBox cb : hxDiabeticInfections.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        for (CheckBox cb : diabetesTreatmentInitiation.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        for (CheckBox cb : selfReportedDiabetesTreatmentHistory.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {

        //formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
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
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }

        if (!(diabetesNextScheduledVisit.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = diabetesNextScheduledVisit.getButton().getText().toString();
            String formDa1 = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            //Date date = new Date();
            if (secondDateCalendar.before(formDateCalendar/*App.getCalendar(App.stringToDate(formDa1, "yyyy-MM-dd"))*/)) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.next_visit_date_cannot_before_form_date), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                diabetesNextScheduledVisit.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                diabetesNextScheduledVisit.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                diabetesNextScheduledVisit.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        }

        formDate.getButton().setEnabled(true);
        diabetesNextScheduledVisit.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {

        Boolean error = false;
        View view = null;

        if (diabetesTreatmentInitiationInsulinMix.getVisibility() == View.VISIBLE && App.get(diabetesTreatmentInitiationInsulinMix).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(6);
            diabetesTreatmentInitiationInsulinMix.getEditText().setError(getString(R.string.empty_field));
            diabetesTreatmentInitiationInsulinMix.getEditText().requestFocus();
            error = true;
        }else if (diabetesTreatmentInitiationInsulinMix.getVisibility() == View.VISIBLE && !App.get(diabetesTreatmentInitiationInsulinMix).isEmpty() && Integer.parseInt(App.get(diabetesTreatmentInitiationInsulinMix)) > 1000) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(6);
            diabetesTreatmentInitiationInsulinMix.getEditText().setError(getString(R.string.comorbidities_diabetes_treatment_initiation_insulin_mix_limit));
            diabetesTreatmentInitiationInsulinMix.getEditText().requestFocus();
            error = true;
        }

        if (diabetesTreatmentInitiationInsulinR.getVisibility() == View.VISIBLE && App.get(diabetesTreatmentInitiationInsulinR).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(6);
            diabetesTreatmentInitiationInsulinR.getEditText().setError(getString(R.string.empty_field));
            diabetesTreatmentInitiationInsulinR.getEditText().requestFocus();
            error = true;
        } else if (diabetesTreatmentInitiationInsulinR.getVisibility() == View.VISIBLE && !App.get(diabetesTreatmentInitiationInsulinR).isEmpty() && Integer.parseInt(App.get(diabetesTreatmentInitiationInsulinR)) > 100) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(6);
            diabetesTreatmentInitiationInsulinR.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
            diabetesTreatmentInitiationInsulinR.getEditText().requestFocus();
            error = true;
        }

        if (diabetesTreatmentInitiationInsulinN.getVisibility() == View.VISIBLE && App.get(diabetesTreatmentInitiationInsulinN).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(6);
            diabetesTreatmentInitiationInsulinN.getEditText().setError(getString(R.string.empty_field));
            diabetesTreatmentInitiationInsulinN.getEditText().requestFocus();
            error = true;
        }else if (diabetesTreatmentInitiationInsulinN.getVisibility() == View.VISIBLE && !App.get(diabetesTreatmentInitiationInsulinN).isEmpty() && Integer.parseInt(App.get(diabetesTreatmentInitiationInsulinN)) > 100) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(6);
            diabetesTreatmentInitiationInsulinN.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
            diabetesTreatmentInitiationInsulinN.getEditText().requestFocus();
            error = true;
        }

        if (diabetesTreatmentInitiationMetforminPrescribed.getVisibility() == View.VISIBLE && App.get(diabetesTreatmentInitiationMetforminPrescribed).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(5);
            diabetesTreatmentInitiationMetforminPrescribed.getEditText().setError(getString(R.string.empty_field));
            diabetesTreatmentInitiationMetforminPrescribed.getEditText().requestFocus();
            error = true;
        }

        if (diabetesTreatmentInitiationTabletsPerDay.getVisibility() == View.VISIBLE && App.get(diabetesTreatmentInitiationTabletsPerDay).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(5);
            diabetesTreatmentInitiationTabletsPerDay.getEditText().setError(getString(R.string.empty_field));
            diabetesTreatmentInitiationTabletsPerDay.getEditText().requestFocus();
            error = true;
        }

        /*if (App.get(diabetesTreatmentDetail).trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(5);
            diabetesTreatmentDetail.getEditText().setError(getString(R.string.empty_field));
            diabetesTreatmentDetail.getEditText().requestFocus();
            error = true;
        }*/

        Boolean flag = false;
        if (diabetesTreatmentInitiation.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : diabetesTreatmentInitiation.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (App.isLanguageRTL())
                    gotoPage(1);
                else
                    gotoPage(5);
                diabetesTreatmentInitiation.getQuestionView().setError(getString(R.string.empty_field));
                diabetesTreatmentInitiation.getQuestionView().requestFocus();
                view = diabetesTreatmentInitiation;
                error = true;
            }
        }

        /*flag = false;
        if (hxDiabeticInfections.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : hxDiabeticInfections.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (App.isLanguageRTL())
                    gotoPage(3);
                else
                    gotoPage(3);
                hxDiabeticInfections.getQuestionView().setError(getString(R.string.empty_field));
                hxDiabeticInfections.getQuestionView().requestFocus();
                view = hxDiabeticInfections;
                error = true;
            }
        }*/

        flag = false;
        if (selfReportedDiabetesTreatmentHistory.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : selfReportedDiabetesTreatmentHistory.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (App.isLanguageRTL())
                    gotoPage(5);
                else
                    gotoPage(1);
                selfReportedDiabetesTreatmentHistory.getQuestionView().setError(getString(R.string.empty_field));
                selfReportedDiabetesTreatmentHistory.getQuestionView().requestFocus();
                view = selfReportedDiabetesTreatmentHistory;
                error = true;
            }
        }

        flag = false;
        if (diabetesFamilyHistorySpecify.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : diabetesFamilyHistorySpecify.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (App.isLanguageRTL())
                    gotoPage(6);
                else
                    gotoPage(0);
                diabetesFamilyHistorySpecify.getQuestionView().setError(getString(R.string.empty_field));
                diabetesFamilyHistorySpecify.getQuestionView().requestFocus();
                view = diabetesFamilyHistorySpecify;
                error = true;
            }
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            DrawableCompat.setTint(clearIcon, color);
            alertDialog.setIcon(clearIcon);
            alertDialog.setTitle(getResources().getString(R.string.title_error));
            final View finalView = view;
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            scrollView.post(new Runnable() {
                                public void run() {
                                    if (finalView != null) {
                                        scrollView.scrollTo(0, finalView.getTop());
                                        diabetesTreatmentDetail.clearFocus();
                                        diabetesTreatmentInitiationInsulinN.clearFocus();
                                        diabetesTreatmentInitiationInsulinR.clearFocus();
                                        diabetesTreatmentInitiationInsulinMix.clearFocus();
                                    }
                                }
                            });
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
        observations.add(new String[]{"FAMILY HISTORY OF DIABETES MELLITUS", App.get(diabetesFamilyHistory).equals(getResources().getString(R.string.comorbidities_diabetes_family_history_yes)) ? "YES" :
                (App.get(diabetesFamilyHistory).equals(getResources().getString(R.string.comorbidities_diabetes_family_history_no)) ? "NO" :
                        App.get(diabetesFamilyHistory).equals(getResources().getString(R.string.comorbidities_diabetes_family_history_refused)) ? "REFUSED" : "UNKNOWN")});

        if(diabetesFamilyHistorySpecify.getVisibility() == View.VISIBLE) {
            String diabetesFamilyHistoryString = "";
            for (CheckBox cb : diabetesFamilyHistorySpecify.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_mother)))
                    diabetesFamilyHistoryString = diabetesFamilyHistoryString + "MOTHER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_father)))
                    diabetesFamilyHistoryString = diabetesFamilyHistoryString + "FATHER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_maternalgm)))
                    diabetesFamilyHistoryString = diabetesFamilyHistoryString + "MATERNAL GRANDMOTHER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_maternalgf)))
                    diabetesFamilyHistoryString = diabetesFamilyHistoryString + "MATERNAL GRANDFATHER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_paternalgm)))
                    diabetesFamilyHistoryString = diabetesFamilyHistoryString + "PATERNAL GRANDMOTHER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_paternalgf)))
                    diabetesFamilyHistoryString = diabetesFamilyHistoryString + "PATERNAL GRANDFATHER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_brother)))
                    diabetesFamilyHistoryString = diabetesFamilyHistoryString + "BROTHER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_sister)))
                    diabetesFamilyHistoryString = diabetesFamilyHistoryString + "SISTER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_son)))
                    diabetesFamilyHistoryString = diabetesFamilyHistoryString + "SON" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_daughter)))
                    diabetesFamilyHistoryString = diabetesFamilyHistoryString + "DAUGHTER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_spouse)))
                    diabetesFamilyHistoryString = diabetesFamilyHistoryString + "SPOUSE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_aunt)))
                    diabetesFamilyHistoryString = diabetesFamilyHistoryString + "AUNT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_uncle)))
                    diabetesFamilyHistoryString = diabetesFamilyHistoryString + "UNCLE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_other1)))
                    diabetesFamilyHistoryString = diabetesFamilyHistoryString + "OTHER FAMILY MEMBER" + " ; ";
            }
            observations.add(new String[]{"FAMILY MEMBERS WITH DIABETES", diabetesFamilyHistoryString});
        }

        observations.add(new String[]{"DIABETES MELLITUS MEDICATION HISTORY", App.get(previousDiabetesTreatmentHistory).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        /*if(selfReportedDiabetesTreatmentHistory.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"SELF REPORTED DIABETES MEDICATION", App.get(selfReportedDiabetesTreatmentHistory).equals(getResources().getString(R.string.comorbidities_self_reported_diabetes_treatment_history_oha)) ? "ORAL ANTIHYPERCLYCEMIC MEDICATION" : "INSULIN"});
        }*/

        if(selfReportedDiabetesTreatmentHistory.getVisibility() == View.VISIBLE) {
            String diabetesFamilyHistoryString = "";
            for (CheckBox cb : selfReportedDiabetesTreatmentHistory.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_self_reported_diabetes_treatment_history_oha)))
                    diabetesFamilyHistoryString = diabetesFamilyHistoryString + "ORAL ANTIHYPERCLYCEMIC MEDICATION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_self_reported_diabetes_treatment_history_insulin)))
                    diabetesFamilyHistoryString = diabetesFamilyHistoryString + "INSULIN" + " ; ";
            }
            observations.add(new String[]{"SELF REPORTED DIABETES MEDICATION", diabetesFamilyHistoryString});
        }

        if(typeDiabetes.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"TYPE OF DIABETES MELLITUS", App.get(typeDiabetes).equals(getResources().getString(R.string.comorbidities_type_diabetes_type1)) ? "DIABETES MELLITUS TYPE 1" : "DIABETES MELLITUS TYPE II"});
        }

        observations.add(new String[]{"DIABETES CONTROL STATUS", App.get(statusDiabetes).equals(getResources().getString(R.string.comorbidities_status_diabetes_controlled)) ? "CONTROLLED DIABETES MELLITUS" : "UNCONTROLLED DIABETES MELLITUS"});
        observations.add(new String[]{"DIABETIC HYPOGLYCEMIA", App.get(hxHypoglycemia).equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes)) ? "YES" :
                (App.get(hxHypoglycemia).equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no)) ? "NO" : "UNKNOWN")});
        observations.add(new String[]{"DIABETIC RETINOPATHY", App.get(hxDiabeticRetinopathy).equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes)) ? "YES" :
                (App.get(hxDiabeticRetinopathy).equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no)) ? "NO" : "UNKNOWN")});
        observations.add(new String[]{"DIABETIC AUTONOMIC NEUROPATHY", App.get(hxDiabeticNeuropathy).equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes)) ? "YES" :
                (App.get(hxDiabeticNeuropathy).equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no)) ? "NO" : "UNKNOWN")});
        observations.add(new String[]{"DIABETIC NEPHROPATHY", App.get(hxDiabeticNephropathy).equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes)) ? "YES" :
                (App.get(hxDiabeticNephropathy).equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no)) ? "NO" : "UNKNOWN")});

        String hxDiabeticInfectionsString = "";
        for(CheckBox cb : hxDiabeticInfections.getCheckedBoxes()){
            if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_infections_skin)))
                hxDiabeticInfectionsString = hxDiabeticInfectionsString + "INFECTION OF SKIN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_infections_foot)))
                hxDiabeticInfectionsString = hxDiabeticInfectionsString + "FOOT INFECTION" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_infections_dental)))
                hxDiabeticInfectionsString = hxDiabeticInfectionsString + "INFECTED DENTAL CARIES" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_infections_gu)))
                hxDiabeticInfectionsString = hxDiabeticInfectionsString + "GENITOURINARY SYMPTOMS" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_infections_pelvic)))
                hxDiabeticInfectionsString = hxDiabeticInfectionsString + "GENITAL INFECTION" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_infections_upper_respiratory)))
                hxDiabeticInfectionsString = hxDiabeticInfectionsString + "UPPER RESPIRATORY INFECTION" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_infections_other)))
                hxDiabeticInfectionsString = hxDiabeticInfectionsString + "OTHER INFECTION" + " ; ";
        }
        observations.add(new String[]{"HISTORY OF INFECTIONS", hxDiabeticInfectionsString});

        observations.add(new String[]{"PERIPHERAL VASCULAR DISEASE", App.get(hxDiabeticPvd).equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes)) ? "YES" :
                (App.get(hxDiabeticPvd).equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no)) ? "NO" : "UNKNOWN")});
        observations.add(new String[]{"CORONARY HEART DISEASE", App.get(hxDiabeticCad).equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes)) ? "YES" :
                (App.get(hxDiabeticCad).equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no)) ? "NO" : "UNKNOWN")});

        if(hxDiabeticHypertension.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"GESTATIONAL DIABETES DURING PREGNENCY", App.get(hxDiabeticHypertension).equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes)) ? "YES" :
                    (App.get(hxDiabeticHypertension).equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no)) ? "NO" : "UNKNOWN")});
        }

        if(hxDiabeticGestationalDiabetes.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"GESTATIONAL DIABETES CONTINUED AFTER DELIVERY", App.get(hxDiabeticGestationalDiabetes).equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes)) ? "YES" :
                    (App.get(hxDiabeticGestationalDiabetes).equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no)) ? "NO" : "UNKNOWN")});
        }

        String diabetesTreatmentInitiationString = "";
        for(CheckBox cb : diabetesTreatmentInitiation.getCheckedBoxes()){
            if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_option)))
                diabetesTreatmentInitiationString = diabetesTreatmentInitiationString + "METFORMIN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_sulfonlyurea_option)))
                diabetesTreatmentInitiationString = diabetesTreatmentInitiationString + "SULFONLYUREAS" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_tzd_option)))
                diabetesTreatmentInitiationString = diabetesTreatmentInitiationString + "PIOGLITAZONE" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_dpp4i_option)))
                diabetesTreatmentInitiationString = diabetesTreatmentInitiationString + "DIPEPTIDYL PEPTIDASE 4 INHIBITOR" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulinN_option)))
                diabetesTreatmentInitiationString = diabetesTreatmentInitiationString + "INSULIN, ISOPHANE, HUMAN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulinR_option)))
                diabetesTreatmentInitiationString = diabetesTreatmentInitiationString + "INSULIN, REGULAR, HUMAN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulin_mix_option)))
                diabetesTreatmentInitiationString = diabetesTreatmentInitiationString + "INSULIN 70/30" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_other_option)))
                diabetesTreatmentInitiationString = diabetesTreatmentInitiationString + "OTHER DRUG NAME" + " ; ";
        }
        observations.add(new String[]{"DIABETES MEDICATIONS", diabetesTreatmentInitiationString});

        observations.add(new String[]{"CLINICIAN NOTES (TEXT)", App.get(diabetesTreatmentDetail).trim()});
        //observations.add(new String[]{"METFORMIN DOSE", App.get(diabetesTreatmentInitiationMetformin)});
        observations.add(new String[]{"METFORMIN TABLET PER DAY", App.get(diabetesTreatmentInitiationTabletsPerDay)});
        observations.add(new String[]{"METFORMIN PRESCRIBED (FOR DAYS)", App.get(diabetesTreatmentInitiationMetforminPrescribed)});
        observations.add(new String[]{"INSULIN N DOSAGE", App.get(diabetesTreatmentInitiationInsulinN)});
        observations.add(new String[]{"INSULIN R DOSAGE", App.get(diabetesTreatmentInitiationInsulinR)});
        observations.add(new String[]{"INSULIN 70/30 DOSAGE", App.get(diabetesTreatmentInitiationInsulinMix)});
        observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDateTime(secondDateCalendar)});

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

                String result = "";
                result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
                if (result.contains("SUCCESS"))
                    return "SUCCESS";

                return result;
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

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));

        //serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int formId) {
        OfflineForm fo = serverService.getOfflineFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {
            String[][] obs = obsValue.get(i);

            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            }

            if (obs[0][0].equals("FAMILY HISTORY OF DIABETES MELLITUS")) {
                for (RadioButton rb : diabetesFamilyHistory.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_dont_know)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("FAMILY MEMBERS WITH DIABETES")) {
                for (CheckBox cb : diabetesFamilyHistorySpecify.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_mother)) && obs[0][1].equals("MOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_father)) && obs[0][1].equals("FATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_maternalgm)) && obs[0][1].equals("MATERNAL GRANDMOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_maternalgf)) && obs[0][1].equals("MATERNAL GRANDFATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_paternalgm)) && obs[0][1].equals("PATERNAL GRANDMOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_paternalgf)) && obs[0][1].equals("PATERNAL GRANDFATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_brother)) && obs[0][1].equals("BROTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_sister)) && obs[0][1].equals("SISTER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_son)) && obs[0][1].equals("SON")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_daughter)) && obs[0][1].equals("DAUGHTER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_spouse)) && obs[0][1].equals("SPOUSE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_aunt)) && obs[0][1].equals("AUNT")) {
                        cb.setChecked(true);
                        break;
                    }  else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_uncle)) && obs[0][1].equals("UNCLE")) {
                        cb.setChecked(true);
                        break;
                    }  else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_family_history_specify_other1)) && obs[0][1].equals("OTHER FAMILY MEMBER")) {
                        cb.setChecked(true);
                        break;
                    }

                }
                diabetesFamilyHistorySpecify.setVisibility(View.VISIBLE);
            } else  if (obs[0][0].equals("DIABETES MELLITUS MEDICATION HISTORY")) {
                for (RadioButton rb : previousDiabetesTreatmentHistory.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } /*else  if (obs[0][0].equals("SELF REPORTED DIABETES MEDICATION")) {
                for (RadioButton rb : selfReportedDiabetesTreatmentHistory.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_self_reported_diabetes_treatment_history_oha)) && obs[0][1].equals("ORAL ANTIHYPERCLYCEMIC MEDICATION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_self_reported_diabetes_treatment_history_insulin)) && obs[0][1].equals("INSULIN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }*/ else if (obs[0][0].equals("SELF REPORTED DIABETES MEDICATION")) {
                for (CheckBox cb : selfReportedDiabetesTreatmentHistory.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.comorbidities_self_reported_diabetes_treatment_history_oha)) && obs[0][1].equals("ORAL ANTIHYPERCLYCEMIC MEDICATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_self_reported_diabetes_treatment_history_insulin)) && obs[0][1].equals("INSULIN")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else  if (obs[0][0].equals("TYPE OF DIABETES MELLITUS")) {
                for (RadioButton rb : typeDiabetes.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_type_diabetes_type1)) && obs[0][1].equals("DIABETES MELLITUS TYPE 1")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_type_diabetes_type2)) && obs[0][1].equals("DIABETES MELLITUS TYPE II")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                typeDiabetes.setVisibility(View.VISIBLE);
            } else  if (obs[0][0].equals("DIABETES CONTROL STATUS")) {
                for (RadioButton rb : statusDiabetes.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_status_diabetes_controlled)) && obs[0][1].equals("CONTROLLED DIABETES MELLITUS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_status_diabetes_uncontrolled)) && obs[0][1].equals("UNCONTROLLED DIABETES MELLITUS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else  if (obs[0][0].equals("DIABETIC HYPOGLYCEMIA")) {
                for (RadioButton rb : hxHypoglycemia.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else  if (obs[0][0].equals("DIABETIC RETINOPATHY")) {
                for (RadioButton rb : hxDiabeticRetinopathy.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else  if (obs[0][0].equals("\"DIABETIC AUTONOMIC NEUROPATHY")) {
                for (RadioButton rb : hxDiabeticNeuropathy.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else  if (obs[0][0].equals("DIABETIC NEPHROPATHY")) {
                for (RadioButton rb : hxDiabeticNephropathy.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("HISTORY OF INFECTIONS")) {
                for (CheckBox cb : hxDiabeticInfections.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_infections_skin)) && obs[0][1].equals("INFECTION OF SKIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_infections_foot)) && obs[0][1].equals("FOOT INFECTION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_infections_dental)) && obs[0][1].equals("INFECTED DENTAL CARIES")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_infections_gu)) && obs[0][1].equals("GENITOURINARY SYMPTOMS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_infections_pelvic)) && obs[0][1].equals("GENITAL INFECTION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_infections_upper_respiratory)) && obs[0][1].equals("UPPER RESPIRATORY INFECTION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_infections_other)) && obs[0][1].equals("OTHER INFECTION")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                hxDiabeticInfections.setVisibility(View.VISIBLE);
            } else  if (obs[0][0].equals("PERIPHERAL VASCULAR DISEASE")) {
                for (RadioButton rb : hxDiabeticPvd.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else  if (obs[0][0].equals("CORONARY HEART DISEASE")) {
                for (RadioButton rb : hxDiabeticCad.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("GESTATIONAL DIABETES DURING PREGNENCY")) {
                for (RadioButton rb : hxDiabeticHypertension.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                hxDiabeticHypertension.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("GESTATIONAL DIABETES CONTINUED AFTER DELIVERY")) {
                for (RadioButton rb : hxDiabeticGestationalDiabetes.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                hxDiabeticHypertension.setVisibility(View.VISIBLE);
            }  else if (obs[0][0].equals("DIABETES MEDICATIONS")) {
                for (CheckBox cb : diabetesTreatmentInitiation.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_option)) && obs[0][1].equals("METFORMIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_sulfonlyurea_option)) && obs[0][1].equals("SULFONLYUREAS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_tzd_option)) && obs[0][1].equals("PIOGLITAZONE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_dpp4i_option)) && obs[0][1].equals("DIPEPTIDYL PEPTIDASE 4 INHIBITOR")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulinN_option)) && obs[0][1].equals("INSULIN, ISOPHANE, HUMAN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulinR_option)) && obs[0][1].equals("INSULIN, REGULAR, HUMAN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulin_mix_option)) && obs[0][1].equals("INSULIN 70/30")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_other_option)) && obs[0][1].equals("OTHER DRUG NAME")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
                diabetesTreatmentDetail.getEditText().setText(obs[0][1]);
            } /*else if (obs[0][0].equals("METFORMIN DOSE")) {
                for (RadioButton rb : diabetesTreatmentInitiationMetformin.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_500)) && obs[0][1].equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_500))) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_1000)) && obs[0][1].equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_1000))) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_1500)) && obs[0][1].equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_1500))) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_2000)) && obs[0][1].equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_2000))) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_2500)) && obs[0][1].equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_2500))) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } */
            else if (obs[0][0].equals("METFORMIN TABLET PER DAY")) {
                diabetesTreatmentInitiationTabletsPerDay.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("METFORMIN PRESCRIBED (FOR DAYS)")) {
                diabetesTreatmentInitiationMetforminPrescribed.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("INSULIN N DOSAGE")) {
                diabetesTreatmentInitiationInsulinN.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("INSULIN R DOSAGE")) {
                diabetesTreatmentInitiationInsulinR.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("INSULIN 70/30 DOSAGE")) {
                diabetesTreatmentInitiationInsulinMix.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                diabetesNextScheduledVisit.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
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
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
        }
        else if (view == diabetesNextScheduledVisit.getButton()) {
            diabetesNextScheduledVisit.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", false);
            args.putBoolean("allowFutureDate", true);
            args.putString("formDate", formDate.getButtonText());
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        }
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

        showMetforminPerDayAndPrescribed();
        showInsulinN();
        showInsulinR();
        showInsulinMix();

        for (CheckBox cb : diabetesFamilyHistorySpecify.getCheckedBoxes()) {
            if (cb.isChecked()) {
                diabetesFamilyHistorySpecify.getQuestionView().setError(null);
                break;
            }
        }

        for (CheckBox cb : hxDiabeticInfections.getCheckedBoxes()) {
            if (cb.isChecked()) {
                hxDiabeticInfections.getQuestionView().setError(null);
                break;
            }
        }

        for (CheckBox cb : diabetesTreatmentInitiation.getCheckedBoxes()) {
            if (cb.isChecked()) {
                diabetesTreatmentInitiation.getQuestionView().setError(null);
                break;
            }
        }

        for (CheckBox cb : selfReportedDiabetesTreatmentHistory.getCheckedBoxes()) {
            if (cb.isChecked()) {
                selfReportedDiabetesTreatmentHistory.getQuestionView().setError(null);
                break;
            }
        }
    }

    @Override
    public void resetViews() {
        super.resetViews();

        showMetforminPerDayAndPrescribed();
        showInsulinN();
        showInsulinR();
        showInsulinMix();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        diabetesNextScheduledVisit.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

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

        if(App.get(diabetesTreatmentInitiationInsulinN).equals("")) {
            //HERE FOR AUTOPOPULATING OBS
            final AsyncTask<String, String, HashMap<String, String>> autopopulateFormTask = new AsyncTask<String, String, HashMap<String, String>>() {
                @Override
                protected HashMap<String, String> doInBackground(String... params) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading.setInverseBackgroundForced(true);
                            loading.setIndeterminate(true);
                            loading.setCancelable(false);
                            loading.setMessage(getResources().getString(R.string.fetching_data));
                            loading.show();
                        }
                    });

                    HashMap<String, String> result = new HashMap<String, String>();
                    String nextAppointDate = serverService.getLatestObsValue(App.getPatientId(), "FAST" + "-" + "Treatment Followup", "RETURN VISIT DATE");

                    //Fetching Next Appointment Date of FAST Treatment Followup
                    if (nextAppointDate != null)
                        if (!nextAppointDate.equals(""))
                            result.put("RETURN VISIT DATE", nextAppointDate);

                    return result;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                }

                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();

                    String secondDate = result.get("RETURN VISIT DATE");
                    if (secondDate != null) {
                        secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                        diabetesNextScheduledVisit.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                    }
                }
            };
            autopopulateFormTask.execute("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == diabetesFamilyHistory.getRadioGroup()) {
            showDiabetesFamilyHistorySpecify();
        }

        if (radioGroup ==  previousDiabetesTreatmentHistory.getRadioGroup()) {
            showSelfReportedDiabetesTreatmentHistory();
            showDiabetesType();
        }

        if(hxDiabeticGestationalDiabetes.getVisibility() == View.VISIBLE) {
            if (radioGroup == hxDiabeticGestationalDiabetes.getRadioGroup()) {
                showHxDiabeticBirth();
            }
        }
    }

    void showDiabetesFamilyHistorySpecify() {
        if (diabetesFamilyHistory.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_diabetes_family_history_yes))) {
            diabetesFamilyHistorySpecify.setVisibility(View.VISIBLE);
        } else {
            diabetesFamilyHistorySpecify.setVisibility(View.GONE);
        }
    }

    void showSelfReportedDiabetesTreatmentHistory() {
        if (previousDiabetesTreatmentHistory.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.yes))) {
            selfReportedDiabetesTreatmentHistory.setVisibility(View.VISIBLE);
        } else {
            selfReportedDiabetesTreatmentHistory.setVisibility(View.GONE);
        }
    }

    void showDiabetesType() {
        if (previousDiabetesTreatmentHistory.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.no))) {
            typeDiabetes.setVisibility(View.VISIBLE);
        } else {
            typeDiabetes.setVisibility(View.GONE);
        }
    }

    void showGestationalDiabetesDuringPregnancy() {
        if (App.getPatient().getPerson().getGender().equalsIgnoreCase("F")) {
            hxDiabeticGestationalDiabetes.setVisibility(View.VISIBLE);
        } else {
            hxDiabeticGestationalDiabetes.setVisibility(View.GONE);
        }
    }

    void showHxDiabeticBirth() {
        if (hxDiabeticGestationalDiabetes.getVisibility() == View.VISIBLE && hxDiabeticGestationalDiabetes.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes))) {
            hxDiabeticBirth.setVisibility(View.VISIBLE);
        } else {
            hxDiabeticBirth.setVisibility(View.GONE);
        }
    }

    void showMetforminPerDayAndPrescribed() {

        Boolean flag = false;
        for (CheckBox cb : diabetesTreatmentInitiation.getCheckedBoxes()) {
            if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_option)) && cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (flag) {
            diabetesTreatmentInitiationTabletsPerDay.setVisibility(View.VISIBLE);
            diabetesTreatmentInitiationMetforminPrescribed.setVisibility(View.VISIBLE);
        }
        else {
            diabetesTreatmentInitiationTabletsPerDay.setVisibility(View.GONE);
            diabetesTreatmentInitiationMetforminPrescribed.setVisibility(View.GONE);
        }
    }

    void showInsulinN() {

        Boolean flag = false;
        for (CheckBox cb : diabetesTreatmentInitiation.getCheckedBoxes()) {
            if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulinN_option)) && cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (flag) {
            diabetesTreatmentInitiationInsulinN.setVisibility(View.VISIBLE);
        }
        else {
            diabetesTreatmentInitiationInsulinN.setVisibility(View.GONE);
        }
    }

    void showInsulinR() {

        Boolean flag = false;
        for (CheckBox cb : diabetesTreatmentInitiation.getCheckedBoxes()) {
            if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulinR_option)) && cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (flag) {
            diabetesTreatmentInitiationInsulinR.setVisibility(View.VISIBLE);
        }
        else {
            diabetesTreatmentInitiationInsulinR.setVisibility(View.GONE);
        }
    }

    void showInsulinMix() {

        Boolean flag = false;
        for (CheckBox cb : diabetesTreatmentInitiation.getCheckedBoxes()) {
            if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulin_mix_option)) && cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (flag) {
            diabetesTreatmentInitiationInsulinMix.setVisibility(View.VISIBLE);
        }
        else {
            diabetesTreatmentInitiationInsulinMix.setVisibility(View.GONE);
        }
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


