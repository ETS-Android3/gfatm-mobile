package com.ihsinformatics.gfatmmobile.comorbidities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Fawad Jawaid on 24-Jan-17.
 */

public class ComorbiditiesDiabetesTreatmentFollowupForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    protected Calendar secondDateCalendar;
    Context context;
    // Views...
    TitledButton formDate;
    TitledSpinner diabetesFollowupMonthOfVisit;
    TitledEditText diabetesFollowupBodyMassIndex;
    TitledEditText diabetesFollowupWaistHipRatio;
    TitledEditText diabetesFollowupBloodPressureSystolic;
    TitledEditText diabetesFollowupBloodPressureDiastolic;
    TitledEditText diabetesFollowupHba1cTestResult;
    TitledEditText diabetesFollowupRBSTestResult;
    TitledRadioGroup diabetesFollowupHasPrescribedMedication;
    TitledSpinner diabetesFollowupReasonsForNonCompliance;
    TitledEditText diabetesFollowupIfOther;
    TitledRadioGroup diabetesFollowupStatusOfDiabetesCondition;
    TitledRadioGroup diabetesFollowupChangeInRegimen;
    TitledSpinner diabetesFollowupNewPrescribedMedication;
    TitledEditText diabetesFollowupNewPrescribedMedicationDetail;
    TitledSpinner diabetesFollowupDosageStrengthofMetformin;
    TitledEditText diabetesFollowupDosageInsulinN;
    TitledEditText diabetesFollowupDosageInsulinR;
    TitledButton diabetesFollowupNextScheduledVisit;

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

        PAGE_COUNT = 3;
        FORM_NAME = Forms.COMORBIDITIES_DIABETES_TREATMENT_FOLLOWUP_FORM;
        FORM =  Forms.comorbidities_diabetesTreatmentFollowupForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesDiabetesTreatmentFollowupForm.MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(PAGE_COUNT - 1);
        formName.setText(FORM_NAME);

        secondDateCalendar = Calendar.getInstance();

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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        diabetesFollowupMonthOfVisit = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_urinedr_month_of_treatment), getResources().getStringArray(R.array.comorbidities_followup_month), "1", App.HORIZONTAL);
        diabetesFollowupBodyMassIndex = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_bmi), "", getResources().getString(R.string.comorbidities_vitals_bmi_range), 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        //diabetesFollowupBodyMassIndex.getEditText().setFocusable(false);
        diabetesFollowupWaistHipRatio = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_whr), "", getResources().getString(R.string.comorbidities_vitals_waist_hip_whr_range), 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        //diabetesFollowupWaistHipRatio.getEditText().setFocusable(false);
        diabetesFollowupBloodPressureSystolic = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_systolic), "", getResources().getString(R.string.comorbidities_vitals_bp_systolic_diastolic_range), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        diabetesFollowupBloodPressureDiastolic = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_diastolic), "", getResources().getString(R.string.comorbidities_vitals_bp_systolic_diastolic_range), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        diabetesFollowupHba1cTestResult = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_hba1c_test_result), "", getResources().getString(R.string.comorbidities_hba1c_result_range), 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        diabetesFollowupRBSTestResult = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_rbs_test_result), "", getResources().getString(R.string.comorbidities_rbs_result_range), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        diabetesFollowupHasPrescribedMedication = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_prescribed_medication), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        diabetesFollowupReasonsForNonCompliance = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance), getResources().getStringArray(R.array.comorbidities_diabetes_followup_non_compliance_options), getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_side_effects), App.HORIZONTAL);
        diabetesFollowupIfOther = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_if_other), "", "", 1000, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        diabetesFollowupStatusOfDiabetesCondition = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_status_of_diabetes_condition), getResources().getStringArray(R.array.comorbidities_diabetes_followup_status_of_diabetes_condition_options), getResources().getString(R.string.comorbidities_diabetes_followup_status_of_diabetes_condition_option_controlled), App.VERTICAL, App.VERTICAL);
        diabetesFollowupChangeInRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_change_in_regimen), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        diabetesFollowupNewPrescribedMedication = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_diabetes_followup_new_prescribed), getResources().getStringArray(R.array.comorbidities_diabetes_followup_new_prescribed_options), getResources().getString(R.string.comorbidities_diabetes_followup_new_prescribed_options_biguanides), App.HORIZONTAL);
        diabetesFollowupNewPrescribedMedicationDetail = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_new_prescribed_detail), "", "", 1000, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        diabetesFollowupDosageStrengthofMetformin = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_diabetes_followup_metformin), getResources().getStringArray(R.array.comorbidities_diabetes_treatment_initiation_metformin_options), getResources().getString(R.string.comorbidities_diabetes_followup_metformin_options_500), App.HORIZONTAL);
        diabetesFollowupDosageInsulinN = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_isulinN), "", getResources().getString(R.string.comorbidities_vitals_waist_hip_whr_range), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        diabetesFollowupDosageInsulinR = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_isulinR), "", getResources().getString(R.string.comorbidities_vitals_waist_hip_whr_range), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        diabetesFollowupNextScheduledVisit = new TitledButton(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_next_scheduled_visit), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), diabetesFollowupMonthOfVisit.getSpinner(), diabetesFollowupBodyMassIndex.getEditText(),
                diabetesFollowupWaistHipRatio.getEditText(), diabetesFollowupBloodPressureSystolic.getEditText(), diabetesFollowupBloodPressureDiastolic.getEditText(),
                diabetesFollowupHba1cTestResult.getEditText(), diabetesFollowupRBSTestResult.getEditText(), diabetesFollowupHasPrescribedMedication.getRadioGroup(),
                diabetesFollowupReasonsForNonCompliance.getSpinner(), diabetesFollowupIfOther.getEditText(), diabetesFollowupStatusOfDiabetesCondition.getRadioGroup(),
                diabetesFollowupChangeInRegimen.getRadioGroup(), diabetesFollowupNewPrescribedMedication.getSpinner(), diabetesFollowupNewPrescribedMedicationDetail.getEditText(),
                diabetesFollowupDosageStrengthofMetformin.getSpinner(), diabetesFollowupDosageInsulinN.getEditText(), diabetesFollowupDosageInsulinR.getEditText(), diabetesFollowupNextScheduledVisit.getButton()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, diabetesFollowupMonthOfVisit, diabetesFollowupBodyMassIndex, diabetesFollowupWaistHipRatio,
                        diabetesFollowupBloodPressureSystolic, diabetesFollowupBloodPressureDiastolic, diabetesFollowupHba1cTestResult, diabetesFollowupRBSTestResult},
                        {diabetesFollowupHasPrescribedMedication, diabetesFollowupReasonsForNonCompliance, diabetesFollowupIfOther,
                                diabetesFollowupStatusOfDiabetesCondition, diabetesFollowupChangeInRegimen},
                        {diabetesFollowupNewPrescribedMedication, diabetesFollowupNewPrescribedMedicationDetail, diabetesFollowupDosageStrengthofMetformin,
                                diabetesFollowupDosageInsulinN, diabetesFollowupDosageInsulinR, diabetesFollowupNextScheduledVisit}};

        formDate.getButton().setOnClickListener(this);
        diabetesFollowupHasPrescribedMedication.getRadioGroup().setOnCheckedChangeListener(this);

        diabetesFollowupDosageInsulinR.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (diabetesFollowupDosageInsulinR.getEditText().getText().length() > 0) {
                        int num = Integer.parseInt(diabetesFollowupDosageInsulinR.getEditText().getText().toString());
                        if (num < 0 || num > 100) {
                            diabetesFollowupDosageInsulinR.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        diabetesFollowupDosageInsulinN.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (diabetesFollowupDosageInsulinN.getEditText().getText().length() > 0) {
                        int num = Integer.parseInt(diabetesFollowupDosageInsulinN.getEditText().getText().toString());
                        if (num < 0 || num > 100) {
                            diabetesFollowupDosageInsulinN.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        diabetesFollowupRBSTestResult.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (diabetesFollowupRBSTestResult.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(diabetesFollowupRBSTestResult.getEditText().getText().toString());
                        if (num < 0 || num > 300) {
                            diabetesFollowupRBSTestResult.getEditText().setError(getString(R.string.comorbidities_rbs_result_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        diabetesFollowupHba1cTestResult.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (diabetesFollowupHba1cTestResult.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(diabetesFollowupHba1cTestResult.getEditText().getText().toString());
                        if (num < 0 || num > 20) {
                            diabetesFollowupHba1cTestResult.getEditText().setError(getString(R.string.comorbidities_hba1c_result_limit));
                        } else {
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        diabetesFollowupBloodPressureSystolic.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (diabetesFollowupBloodPressureSystolic.getEditText().getText().length() > 0) {
                        int num = Integer.parseInt(diabetesFollowupBloodPressureSystolic.getEditText().getText().toString());
                        if (num < 0 || num > 300) {
                            diabetesFollowupBloodPressureSystolic.getEditText().setError(getString(R.string.comorbidities_vitals_bp_systolic_diastolic_limit));
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        diabetesFollowupBloodPressureDiastolic.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (diabetesFollowupBloodPressureDiastolic.getEditText().getText().length() > 0) {
                        int num = Integer.parseInt(diabetesFollowupBloodPressureDiastolic.getEditText().getText().toString());
                        if (num < 0 || num > 300) {
                            diabetesFollowupBloodPressureDiastolic.getEditText().setError(getString(R.string.comorbidities_vitals_bp_systolic_diastolic_limit));
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        diabetesFollowupReasonsForNonCompliance.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                displayIfOther();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        resetViews();
    }

    @Override
    public void updateDisplay() {

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        secondDateCalendar = formDateCalendar;
        secondDateCalendar.add(Calendar.MONTH, 3);
        diabetesFollowupNextScheduledVisit.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
    }

    @Override
    public boolean validate() {

        Boolean error = false;

        if (App.get(diabetesFollowupDosageInsulinR).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(2);
            diabetesFollowupDosageInsulinR.getEditText().setError(getString(R.string.empty_field));
            diabetesFollowupDosageInsulinR.getEditText().requestFocus();
            error = true;
        } else if (!App.get(diabetesFollowupDosageInsulinR).isEmpty() && Integer.parseInt(App.get(diabetesFollowupDosageInsulinR)) > 100) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(2);
            diabetesFollowupDosageInsulinR.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
            diabetesFollowupDosageInsulinR.getEditText().requestFocus();
            error = true;
        }

        if (App.get(diabetesFollowupDosageInsulinN).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(2);
            diabetesFollowupDosageInsulinN.getEditText().setError(getString(R.string.empty_field));
            diabetesFollowupDosageInsulinN.getEditText().requestFocus();
            error = true;
        } else if (!App.get(diabetesFollowupDosageInsulinN).isEmpty() && Integer.parseInt(App.get(diabetesFollowupDosageInsulinN)) > 100) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(2);
            diabetesFollowupDosageInsulinN.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
            diabetesFollowupDosageInsulinN.getEditText().requestFocus();
            error = true;
        }

        if (diabetesFollowupNewPrescribedMedicationDetail.getVisibility() == View.VISIBLE && App.get(diabetesFollowupNewPrescribedMedicationDetail).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(2);
            diabetesFollowupNewPrescribedMedicationDetail.getEditText().setError(getString(R.string.empty_field));
            diabetesFollowupNewPrescribedMedicationDetail.getEditText().requestFocus();
            error = true;
        }

        if (diabetesFollowupIfOther.getVisibility() == View.VISIBLE && App.get(diabetesFollowupIfOther).isEmpty()) {
            gotoPage(1);
            diabetesFollowupIfOther.getEditText().setError(getString(R.string.empty_field));
            diabetesFollowupIfOther.getEditText().requestFocus();
            error = true;
        }

        if (diabetesFollowupRBSTestResult.getVisibility() == View.VISIBLE && App.get(diabetesFollowupRBSTestResult).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(0);
            diabetesFollowupRBSTestResult.getEditText().setError(getString(R.string.empty_field));
            diabetesFollowupRBSTestResult.getEditText().requestFocus();
            error = true;
        }
        else if (diabetesFollowupRBSTestResult.getVisibility() == View.VISIBLE && !App.get(diabetesFollowupRBSTestResult).isEmpty() && Double.parseDouble(App.get(diabetesFollowupRBSTestResult)) > 300) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(0);
            diabetesFollowupRBSTestResult.getEditText().setError(getString(R.string.comorbidities_rbs_result_limit));
            diabetesFollowupRBSTestResult.getEditText().requestFocus();
            error = true;
        }

        if (diabetesFollowupHba1cTestResult.getVisibility() == View.VISIBLE && App.get(diabetesFollowupHba1cTestResult).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(0);
            diabetesFollowupHba1cTestResult.getEditText().setError(getString(R.string.empty_field));
            diabetesFollowupHba1cTestResult.getEditText().requestFocus();
            error = true;
        }
        else if (diabetesFollowupHba1cTestResult.getVisibility() == View.VISIBLE && !RegexUtil.isNumeric(App.get(diabetesFollowupHba1cTestResult), true)) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(0);
            diabetesFollowupHba1cTestResult.getEditText().setError(getString(R.string.comorbidities_hba1c_not_valid_result_value));
            diabetesFollowupHba1cTestResult.getEditText().requestFocus();
            error = true;
        }
        else if (diabetesFollowupHba1cTestResult.getVisibility() == View.VISIBLE && !App.get(diabetesFollowupHba1cTestResult).isEmpty() && Double.parseDouble(App.get(diabetesFollowupHba1cTestResult)) > 20) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(0);
            diabetesFollowupHba1cTestResult.getEditText().setError(getString(R.string.comorbidities_hba1c_result_limit));
            diabetesFollowupHba1cTestResult.getEditText().requestFocus();
            error = true;
        }

        if (App.get(diabetesFollowupBloodPressureDiastolic).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(0);
            diabetesFollowupBloodPressureDiastolic.getEditText().setError(getString(R.string.empty_field));
            diabetesFollowupBloodPressureDiastolic.getEditText().requestFocus();
            error = true;
        } else if (!App.get(diabetesFollowupBloodPressureDiastolic).isEmpty() && Integer.parseInt(App.get(diabetesFollowupBloodPressureDiastolic)) > 300) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(0);
            diabetesFollowupBloodPressureDiastolic.getEditText().setError(getString(R.string.comorbidities_vitals_bp_systolic_diastolic_limit));
            diabetesFollowupBloodPressureDiastolic.getEditText().requestFocus();
            error = true;
        }

        if (App.get(diabetesFollowupBloodPressureSystolic).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(0);
            diabetesFollowupBloodPressureSystolic.getEditText().setError(getString(R.string.empty_field));
            diabetesFollowupBloodPressureSystolic.getEditText().requestFocus();
            error = true;
        } else if (!App.get(diabetesFollowupBloodPressureSystolic).isEmpty() && Integer.parseInt(App.get(diabetesFollowupBloodPressureSystolic)) > 300) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(0);
            diabetesFollowupBloodPressureSystolic.getEditText().setError(getString(R.string.comorbidities_vitals_bp_systolic_diastolic_limit));
            diabetesFollowupBloodPressureSystolic.getEditText().requestFocus();
            error = true;
        }

        if (App.get(diabetesFollowupWaistHipRatio).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(0);
            diabetesFollowupWaistHipRatio.getEditText().setError(getString(R.string.empty_field));
            diabetesFollowupWaistHipRatio.getEditText().requestFocus();
            error = true;
        }

        if (App.get(diabetesFollowupBodyMassIndex).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(0);
            diabetesFollowupBodyMassIndex.getEditText().setError(getString(R.string.empty_field));
            diabetesFollowupBodyMassIndex.getEditText().requestFocus();
            error = true;
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            DrawableCompat.setTint(clearIcon, color);
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

        if (validate()) {

            resetViews();
        }

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

        secondDateCalendar = formDateCalendar;
        secondDateCalendar.add(Calendar.MONTH, 3);
        diabetesFollowupNextScheduledVisit.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        displayReasonForNonCompliance();
        displayIfOther();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if(radioGroup == diabetesFollowupHasPrescribedMedication.getRadioGroup()) {
            displayReasonForNonCompliance();
            displayIfOther();
        }

    }

    void displayReasonForNonCompliance() {
        if (diabetesFollowupHasPrescribedMedication.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.no))) {
            diabetesFollowupReasonsForNonCompliance.setVisibility(View.VISIBLE);
        } else {
            diabetesFollowupReasonsForNonCompliance.setVisibility(View.GONE);
        }
    }

    void displayIfOther() {

        String text = diabetesFollowupReasonsForNonCompliance.getSpinner().getSelectedItem().toString();
        if (diabetesFollowupHasPrescribedMedication.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.no)) && text.equalsIgnoreCase(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_other))) {
            diabetesFollowupIfOther.setVisibility(View.VISIBLE);
        } else {
            diabetesFollowupIfOther.setVisibility(View.GONE);
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




