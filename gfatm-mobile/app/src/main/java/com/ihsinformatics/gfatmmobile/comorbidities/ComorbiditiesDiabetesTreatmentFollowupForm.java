package com.ihsinformatics.gfatmmobile.comorbidities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Fawad Jawaid on 24-Jan-17.
 */

public class ComorbiditiesDiabetesTreatmentFollowupForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    //protected Calendar secondDateCalendar;
    Context context;
    // Views...
    TitledButton formDate;
    TitledEditText diabetesFollowupMonthOfVisit;
    TitledRadioGroup patientAgreeToContinueTreatment;
    TitledEditText reasonForDiscontinuation;
    TitledEditText diabetesFollowupBodyMassIndex;
    TitledEditText diabetesFollowupWaistHipRatio;
    TitledEditText diabetesFollowupBloodPressureSystolic;
    TitledEditText diabetesFollowupBloodPressureDiastolic;
    TitledEditText diabetesFollowupHba1cTestResult;
    TitledEditText diabetesFollowupRBSTestResult;
    TitledRadioGroup diabetesFollowupHasPrescribedMedication;
    TitledCheckBoxes diabetesFollowupReasonsForNonComplianceCB;
    TitledEditText diabetesFollowupIfOther;
    TitledRadioGroup diabetesFollowupStatusOfDiabetesCondition;
    TitledRadioGroup diabetesFollowupChangeInRegimen;
    //TitledSpinner diabetesFollowupNewPrescribedMedication;
    TitledCheckBoxes diabetesFollowupNewPrescribedMedication;
    TitledRadioGroup diabetesFollowupDosageStrengthofMetformin;
    TitledEditText diabetesTreatmentInitiationTabletsPerDay;
    TitledEditText diabetesTreatmentInitiationMetforminPrescribed;
    TitledEditText diabetesFollowupDosageInsulinN;
    TitledEditText diabetesFollowupDosageInsulinR;
    TitledButton diabetesFollowupNextScheduledVisit;

    Boolean dateChoose = false;
    Boolean refillFlag = false;

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

        PAGE_COUNT = 1;
        FORM_NAME = Forms.COMORBIDITIES_DIABETES_TREATMENT_FOLLOWUP_FORM;
        FORM = Forms.comorbidities_diabetesTreatmentFollowupForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesDiabetesTreatmentFollowupForm.MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(PAGE_COUNT - 1);
        formName.setText(FORM_NAME);

        //secondDateCalendar = Calendar.getInstance();

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
        //diabetesFollowupMonthOfVisit = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_urinedr_month_of_treatment), getResources().getStringArray(R.array.comorbidities_followup_month), "1", App.HORIZONTAL);
        patientAgreeToContinueTreatment = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_diabetes_patient_agree_to_continue_treatment), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.comorbidities_diabetes_family_history_yes), App.HORIZONTAL, App.VERTICAL);
        reasonForDiscontinuation = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_patient_reason_for_discontinuation), "", "", 200, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        diabetesFollowupMonthOfVisit = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_month_of_treatment), "", getResources().getString(R.string.comorbidities_vitals_month_of_visit_range), 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        diabetesFollowupBodyMassIndex = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_bmi), "", getResources().getString(R.string.comorbidities_vitals_bmi_range), 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        //diabetesFollowupBodyMassIndex.getEditText().setFocusable(false);
        diabetesFollowupWaistHipRatio = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_whr), "", getResources().getString(R.string.comorbidities_vitals_waist_hip_whr_range), 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        //diabetesFollowupWaistHipRatio.getEditText().setFocusable(false);
        diabetesFollowupBloodPressureSystolic = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_systolic), "", getResources().getString(R.string.comorbidities_vitals_bp_systolic_diastolic_range), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        diabetesFollowupBloodPressureDiastolic = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_diastolic), "", getResources().getString(R.string.comorbidities_vitals_bp_systolic_diastolic_range), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        diabetesFollowupHba1cTestResult = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_hba1c_test_result), "", getResources().getString(R.string.comorbidities_hba1c_result_range), 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        diabetesFollowupRBSTestResult = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_rbs_test_result), "", getResources().getString(R.string.comorbidities_rbs_result_range1), 3, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        diabetesFollowupHasPrescribedMedication = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_prescribed_medication), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        diabetesFollowupReasonsForNonComplianceCB = new TitledCheckBoxes(mainContent.getContext(), null, getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance), getResources().getStringArray(R.array.comorbidities_diabetes_followup_non_compliance_options), null, App.VERTICAL, App.VERTICAL);
        diabetesFollowupIfOther = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_if_other), "", "", 1000, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        diabetesFollowupStatusOfDiabetesCondition = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_status_of_diabetes_condition), getResources().getStringArray(R.array.comorbidities_diabetes_followup_status_of_diabetes_condition_options), getResources().getString(R.string.comorbidities_diabetes_followup_status_of_diabetes_condition_option_controlled), App.VERTICAL, App.VERTICAL);
        diabetesFollowupChangeInRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_change_in_regimen), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        //diabetesFollowupNewPrescribedMedication = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_diabetes_followup_new_prescribed), getResources().getStringArray(R.array.comorbidities_diabetes_followup_new_prescribed_options), getResources().getString(R.string.comorbidities_diabetes_followup_new_prescribed_options_biguanides), App.HORIZONTAL);
        diabetesFollowupNewPrescribedMedication = new TitledCheckBoxes(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_new_prescribed), getResources().getStringArray(R.array.comorbidities_diabetes_treatment_initiation_options), new Boolean[]{true, false, false, false, false, false, false, false}, App.VERTICAL, App.VERTICAL);
        //diabetesFollowupDosageStrengthofMetformin = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_diabetes_followup_metformin), getResources().getStringArray(R.array.comorbidities_diabetes_treatment_initiation_metformin_options), getResources().getString(R.string.comorbidities_diabetes_followup_metformin_options_500), App.HORIZONTAL);
        diabetesTreatmentInitiationTabletsPerDay = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_per_day), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        diabetesTreatmentInitiationMetforminPrescribed = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_prescribed), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        diabetesFollowupDosageStrengthofMetformin = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_metformin), getResources().getStringArray(R.array.comorbidities_diabetes_treatment_initiation_metformin_options), getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_500), App.HORIZONTAL, App.VERTICAL);
        diabetesFollowupDosageInsulinN = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_isulinN), "", getResources().getString(R.string.comorbidities_vitals_waist_hip_whr_range), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        diabetesFollowupDosageInsulinR = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_isulinR), "", getResources().getString(R.string.comorbidities_vitals_waist_hip_whr_range), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        displayChangeInRegimenOrNot();
        diabetesFollowupNextScheduledVisit = new TitledButton(context, null, getResources().getString(R.string.comorbidities_diabetes_followup_next_scheduled_visit), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), diabetesFollowupMonthOfVisit.getEditText(), reasonForDiscontinuation.getEditText(), patientAgreeToContinueTreatment.getRadioGroup(), diabetesFollowupBodyMassIndex.getEditText(),
                diabetesFollowupWaistHipRatio.getEditText(), diabetesFollowupBloodPressureSystolic.getEditText(), diabetesFollowupBloodPressureDiastolic.getEditText(),
                diabetesFollowupHba1cTestResult.getEditText(), diabetesFollowupRBSTestResult.getEditText(), diabetesFollowupHasPrescribedMedication.getRadioGroup(), diabetesFollowupReasonsForNonComplianceCB, diabetesFollowupIfOther.getEditText(), diabetesFollowupStatusOfDiabetesCondition.getRadioGroup(),
                diabetesFollowupChangeInRegimen.getRadioGroup(), diabetesFollowupNewPrescribedMedication,
                diabetesTreatmentInitiationTabletsPerDay.getEditText(), diabetesTreatmentInitiationMetforminPrescribed.getEditText(), diabetesFollowupDosageStrengthofMetformin.getRadioGroup(), diabetesFollowupDosageInsulinN.getEditText(), diabetesFollowupDosageInsulinR.getEditText(), diabetesFollowupNextScheduledVisit.getButton()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, patientAgreeToContinueTreatment, reasonForDiscontinuation, diabetesFollowupMonthOfVisit, diabetesFollowupBodyMassIndex, diabetesFollowupWaistHipRatio,
                        diabetesFollowupBloodPressureSystolic, diabetesFollowupBloodPressureDiastolic, diabetesFollowupHba1cTestResult, diabetesFollowupRBSTestResult,
                        diabetesFollowupHasPrescribedMedication, diabetesFollowupReasonsForNonComplianceCB, diabetesFollowupIfOther,
                        diabetesFollowupStatusOfDiabetesCondition, diabetesFollowupChangeInRegimen,
                        diabetesFollowupNewPrescribedMedication, diabetesTreatmentInitiationTabletsPerDay, diabetesTreatmentInitiationMetforminPrescribed, diabetesFollowupDosageStrengthofMetformin,
                        diabetesFollowupDosageInsulinN, diabetesFollowupDosageInsulinR, diabetesFollowupNextScheduledVisit}};

        formDate.getButton().setOnClickListener(this);
        diabetesFollowupNextScheduledVisit.getButton().setOnClickListener(this);
        diabetesFollowupHasPrescribedMedication.getRadioGroup().setOnCheckedChangeListener(this);
        patientAgreeToContinueTreatment.getRadioGroup().setOnCheckedChangeListener(this);

        diabetesFollowupChangeInRegimen.getRadioGroup().setOnCheckedChangeListener(this);

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
                        if (num < 0 || num > 500) {
                            diabetesFollowupRBSTestResult.getEditText().setError(getString(R.string.comorbidities_rbs_result_limit1));
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

        diabetesFollowupBodyMassIndex.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (diabetesFollowupBodyMassIndex.getEditText().getText().length() > 0) {
                        int num = Integer.parseInt(diabetesFollowupBodyMassIndex.getEditText().getText().toString());
                        if (num < 0 || num > 200) {
                            diabetesFollowupBodyMassIndex.getEditText().setError(getString(R.string.comorbidities_vitals_bmi_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        diabetesFollowupWaistHipRatio.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (diabetesFollowupWaistHipRatio.getEditText().getText().length() > 0) {
                        int num = Integer.parseInt(diabetesFollowupWaistHipRatio.getEditText().getText().toString());
                        if (num < 0 || num > 100) {
                            diabetesFollowupWaistHipRatio.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        diabetesFollowupMonthOfVisit.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (diabetesFollowupWaistHipRatio.getEditText().getText().length() > 0) {
                        int num = Integer.parseInt(diabetesFollowupMonthOfVisit.getEditText().getText().toString());
                        if (num < 0 || num > 24) {
                            diabetesFollowupMonthOfVisit.getEditText().setError(getString(R.string.comorbidities_vitals_month_of_visit_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        for (CheckBox cb : diabetesFollowupNewPrescribedMedication.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : diabetesFollowupReasonsForNonComplianceCB.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {

        if (refillFlag) {
            refillFlag = false;
            return;
        }

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

        //secondDateCalendar = formDateCalendar;
        if (!dateChoose) {
            secondDateCalendar.set(Calendar.YEAR, formDateCalendar.get(Calendar.YEAR));
            secondDateCalendar.set(Calendar.MONTH, formDateCalendar.get(Calendar.MONTH));
            secondDateCalendar.set(Calendar.DAY_OF_MONTH, formDateCalendar.get(Calendar.DAY_OF_MONTH));
            secondDateCalendar.add(Calendar.MONTH, 3);
            diabetesFollowupNextScheduledVisit.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }
        if (!(diabetesFollowupNextScheduledVisit.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = diabetesFollowupNextScheduledVisit.getButton().getText().toString();
            String formDa1 = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            //Date date = new Date();
            if (secondDateCalendar.before(formDateCalendar/*App.getCalendar(App.stringToDate(formDa1, "yyyy-MM-dd"))*/)) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.next_visit_date_cannot_before_form_date), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                diabetesFollowupNextScheduledVisit.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                diabetesFollowupNextScheduledVisit.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                diabetesFollowupNextScheduledVisit.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        }
        dateChoose = false;
        formDate.getButton().setEnabled(true);
        diabetesFollowupNextScheduledVisit.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {

        Boolean error = false;
        View view = null;

        if (diabetesFollowupDosageInsulinR.getVisibility() == View.VISIBLE && App.get(diabetesFollowupDosageInsulinR).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(1);
            diabetesFollowupDosageInsulinR.getEditText().setError(getString(R.string.empty_field));
            diabetesFollowupDosageInsulinR.getEditText().requestFocus();
            error = true;
        } else if (diabetesFollowupDosageInsulinR.getVisibility() == View.VISIBLE && !App.get(diabetesFollowupDosageInsulinR).isEmpty() && Integer.parseInt(App.get(diabetesFollowupDosageInsulinR)) > 100) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(1);
            diabetesFollowupDosageInsulinR.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
            diabetesFollowupDosageInsulinR.getEditText().requestFocus();
            error = true;
        }

        if (diabetesFollowupDosageInsulinN.getVisibility() == View.VISIBLE && App.get(diabetesFollowupDosageInsulinN).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(1);
            diabetesFollowupDosageInsulinN.getEditText().setError(getString(R.string.empty_field));
            diabetesFollowupDosageInsulinN.getEditText().requestFocus();
            error = true;
        } else if (diabetesFollowupDosageInsulinN.getVisibility() == View.VISIBLE && !App.get(diabetesFollowupDosageInsulinN).isEmpty() && Integer.parseInt(App.get(diabetesFollowupDosageInsulinN)) > 100) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(1);
            diabetesFollowupDosageInsulinN.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
            diabetesFollowupDosageInsulinN.getEditText().requestFocus();
            error = true;
        }
        if (diabetesTreatmentInitiationMetforminPrescribed.getVisibility() == View.VISIBLE && App.get(diabetesTreatmentInitiationMetforminPrescribed).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(1);
            diabetesTreatmentInitiationMetforminPrescribed.getEditText().setError(getString(R.string.empty_field));
            diabetesTreatmentInitiationMetforminPrescribed.getEditText().requestFocus();
            error = true;
        }

        if (diabetesTreatmentInitiationTabletsPerDay.getVisibility() == View.VISIBLE && App.get(diabetesTreatmentInitiationTabletsPerDay).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(1);
            diabetesTreatmentInitiationTabletsPerDay.getEditText().setError(getString(R.string.empty_field));
            diabetesTreatmentInitiationTabletsPerDay.getEditText().requestFocus();
            error = true;
        }

        Boolean flag = false;
        if (diabetesFollowupNewPrescribedMedication.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : diabetesFollowupNewPrescribedMedication.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(1);
                diabetesFollowupNewPrescribedMedication.getQuestionView().setError(getString(R.string.empty_field));
                diabetesFollowupNewPrescribedMedication.getQuestionView().requestFocus();
                view = diabetesFollowupNewPrescribedMedication;
                error = true;
            }
        }

        if (diabetesFollowupIfOther.getVisibility() == View.VISIBLE && App.get(diabetesFollowupIfOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(1);
            diabetesFollowupIfOther.getEditText().setError(getString(R.string.empty_field));
            diabetesFollowupIfOther.getEditText().requestFocus();
            error = true;
        }

        if (diabetesFollowupRBSTestResult.getVisibility() == View.VISIBLE && App.get(diabetesFollowupRBSTestResult).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(0);
            diabetesFollowupRBSTestResult.getEditText().setError(getString(R.string.empty_field));
            diabetesFollowupRBSTestResult.getEditText().requestFocus();
            error = true;
        } else if (diabetesFollowupRBSTestResult.getVisibility() == View.VISIBLE && !App.get(diabetesFollowupRBSTestResult).isEmpty() && Double.parseDouble(App.get(diabetesFollowupRBSTestResult)) > 300) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(0);
            diabetesFollowupRBSTestResult.getEditText().setError(getString(R.string.comorbidities_rbs_result_limit));
            diabetesFollowupRBSTestResult.getEditText().requestFocus();
            error = true;
        }

        if (diabetesFollowupHba1cTestResult.getVisibility() == View.VISIBLE && App.get(diabetesFollowupHba1cTestResult).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(0);
            diabetesFollowupHba1cTestResult.getEditText().setError(getString(R.string.empty_field));
            diabetesFollowupHba1cTestResult.getEditText().requestFocus();
            error = true;
        } else if (diabetesFollowupHba1cTestResult.getVisibility() == View.VISIBLE && !RegexUtil.isNumeric(App.get(diabetesFollowupHba1cTestResult), true)) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(0);
            diabetesFollowupHba1cTestResult.getEditText().setError(getString(R.string.comorbidities_hba1c_not_valid_result_value));
            diabetesFollowupHba1cTestResult.getEditText().requestFocus();
            error = true;
        } else if (diabetesFollowupHba1cTestResult.getVisibility() == View.VISIBLE && !App.get(diabetesFollowupHba1cTestResult).isEmpty() && Double.parseDouble(App.get(diabetesFollowupHba1cTestResult)) > 20) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(0);
            diabetesFollowupHba1cTestResult.getEditText().setError(getString(R.string.comorbidities_hba1c_result_limit));
            diabetesFollowupHba1cTestResult.getEditText().requestFocus();
            error = true;
        }

//        if (App.get(diabetesFollowupBloodPressureDiastolic).isEmpty()) {
//            if (App.isLanguageRTL())
//                gotoPage(1);
//            else
//                gotoPage(0);
//            diabetesFollowupBloodPressureDiastolic.getEditText().setError(getString(R.string.empty_field));
//            diabetesFollowupBloodPressureDiastolic.getEditText().requestFocus();
//            error = true;
//        } else if (!App.get(diabetesFollowupBloodPressureDiastolic).isEmpty() && Integer.parseInt(App.get(diabetesFollowupBloodPressureDiastolic)) > 300) {
//            if (App.isLanguageRTL())
//                gotoPage(1);
//            else
//                gotoPage(0);
//            diabetesFollowupBloodPressureDiastolic.getEditText().setError(getString(R.string.comorbidities_vitals_bp_systolic_diastolic_limit));
//            diabetesFollowupBloodPressureDiastolic.getEditText().requestFocus();
//            error = true;
//        }
//
//        if (App.get(diabetesFollowupBloodPressureSystolic).isEmpty()) {
//            if (App.isLanguageRTL())
//                gotoPage(1);
//            else
//                gotoPage(0);
//            diabetesFollowupBloodPressureSystolic.getEditText().setError(getString(R.string.empty_field));
//            diabetesFollowupBloodPressureSystolic.getEditText().requestFocus();
//            error = true;
//        } else if (!App.get(diabetesFollowupBloodPressureSystolic).isEmpty() && Integer.parseInt(App.get(diabetesFollowupBloodPressureSystolic)) > 300) {
//            if (App.isLanguageRTL())
//                gotoPage(1);
//            else
//                gotoPage(0);
//            diabetesFollowupBloodPressureSystolic.getEditText().setError(getString(R.string.comorbidities_vitals_bp_systolic_diastolic_limit));
//            diabetesFollowupBloodPressureSystolic.getEditText().requestFocus();
//            error = true;
//        }

//        if (App.get(diabetesFollowupWaistHipRatio).isEmpty()) {
//            if (App.isLanguageRTL())
//                gotoPage(1);
//            else
//                gotoPage(0);
//            diabetesFollowupWaistHipRatio.getEditText().setError(getString(R.string.empty_field));
//            diabetesFollowupWaistHipRatio.getEditText().requestFocus();
//            error = true;
//        } else if (!App.get(diabetesFollowupWaistHipRatio).isEmpty() && Double.parseDouble(App.get(diabetesFollowupWaistHipRatio)) > 100) {
//            if (App.isLanguageRTL())
//                gotoPage(1);
//            else
//                gotoPage(0);
//            diabetesFollowupWaistHipRatio.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
//            diabetesFollowupWaistHipRatio.getEditText().requestFocus();
//            error = true;
//        }

//        if (App.get(diabetesFollowupBodyMassIndex).isEmpty()) {
//            if (App.isLanguageRTL())
//                gotoPage(1);
//            else
//                gotoPage(0);
//            diabetesFollowupBodyMassIndex.getEditText().setError(getString(R.string.empty_field));
//            diabetesFollowupBodyMassIndex.getEditText().requestFocus();
//            error = true;
//        } else if (!App.get(diabetesFollowupBodyMassIndex).isEmpty() && Double.parseDouble(App.get(diabetesFollowupBodyMassIndex)) > 200) {
//            if (App.isLanguageRTL())
//                gotoPage(1);
//            else
//                gotoPage(0);
//            diabetesFollowupBodyMassIndex.getEditText().setError(getString(R.string.comorbidities_vitals_bmi_limit));
//            diabetesFollowupBodyMassIndex.getEditText().requestFocus();
//            error = true;
//        }

//        if (App.get(diabetesFollowupMonthOfVisit).isEmpty()) {
//            if (App.isLanguageRTL())
//                gotoPage(1);
//            else
//                gotoPage(0);
//            diabetesFollowupMonthOfVisit.getEditText().setError(getString(R.string.empty_field));
//            diabetesFollowupMonthOfVisit.getEditText().requestFocus();
//            error = true;
//        }

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
                                        diabetesFollowupBodyMassIndex.clearFocus();
                                        diabetesFollowupWaistHipRatio.clearFocus();
                                        diabetesFollowupBloodPressureSystolic.clearFocus();
                                        diabetesFollowupBloodPressureDiastolic.clearFocus();
                                        diabetesFollowupHba1cTestResult.clearFocus();
                                        diabetesFollowupRBSTestResult.clearFocus();
                                        diabetesFollowupIfOther.clearFocus();
                                        diabetesFollowupDosageInsulinN.clearFocus();
                                        diabetesFollowupDosageInsulinR.clearFocus();
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
            } else {
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
        if (diabetesFollowupMonthOfVisit.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FOLLOW-UP MONTH", App.get(diabetesFollowupMonthOfVisit)});
        if (reasonForDiscontinuation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON FOR DISCONTINUATION", App.get(reasonForDiscontinuation)});
        observations.add(new String[]{"PATIENT CONSENT TO CONTINUE TREATMENT FOR DIABETES", App.get(patientAgreeToContinueTreatment).equals(getResources().getString(R.string.comorbidities_diabetes_family_history_yes)) ? "YES" : "NO"});
        if (diabetesFollowupBodyMassIndex.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"BODY MASS INDEX", App.get(diabetesFollowupBodyMassIndex)});
        if (diabetesFollowupWaistHipRatio.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"WAIST-HIP RATIO", App.get(diabetesFollowupWaistHipRatio)});
        if (diabetesFollowupBloodPressureSystolic.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SYSTOLIC BLOOD PRESSURE", App.get(diabetesFollowupBloodPressureSystolic)});
        if (diabetesFollowupBloodPressureDiastolic.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DIASTOLIC BLOOD PRESSURE", App.get(diabetesFollowupBloodPressureDiastolic)});
        if (diabetesFollowupHba1cTestResult.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HBA1C RESULT", App.get(diabetesFollowupHba1cTestResult)});
        if (diabetesFollowupRBSTestResult.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"RANDOM BLOOD SUGAR", App.get(diabetesFollowupRBSTestResult)});
        if (diabetesFollowupHasPrescribedMedication.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT ADHERENT TO TRETMENT", App.get(diabetesFollowupHasPrescribedMedication).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if (diabetesFollowupReasonsForNonComplianceCB.getVisibility() == View.VISIBLE) {
            String diabetesFollowupReasonsForNonComplianceString = "";
            for (CheckBox cb : diabetesFollowupReasonsForNonComplianceCB.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_side_effects)))
                    diabetesFollowupReasonsForNonComplianceString = diabetesFollowupReasonsForNonComplianceString + "SIDE EFFECTS (TEXT)" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_misunderstandings)))
                    diabetesFollowupReasonsForNonComplianceString = diabetesFollowupReasonsForNonComplianceString + "UNABLE TO READ PRESCRIPTION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_alcohol)))
                    diabetesFollowupReasonsForNonComplianceString = diabetesFollowupReasonsForNonComplianceString + "ALCOHOL ABUSE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_depression)))
                    diabetesFollowupReasonsForNonComplianceString = diabetesFollowupReasonsForNonComplianceString + "DEPRESSION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_medication)))
                    diabetesFollowupReasonsForNonComplianceString = diabetesFollowupReasonsForNonComplianceString + "MEDICATIONS UNAVAILABLE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_fear)))
                    diabetesFollowupReasonsForNonComplianceString = diabetesFollowupReasonsForNonComplianceString + "TREATMENT OR PROCEDURE NOT CARRIED OUT DUE TO FEAR OF SIDE EFFECTS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_not_affordable)))
                    diabetesFollowupReasonsForNonComplianceString = diabetesFollowupReasonsForNonComplianceString + "CANNOT AFFORD TREATMENT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_lost_medication)))
                    diabetesFollowupReasonsForNonComplianceString = diabetesFollowupReasonsForNonComplianceString + "LOST OR RAN OUT OF MEDICATION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_feeling_ill)))
                    diabetesFollowupReasonsForNonComplianceString = diabetesFollowupReasonsForNonComplianceString + "FELT TOO ILL TO TAKE MEDICATION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_feeling_better)))
                    diabetesFollowupReasonsForNonComplianceString = diabetesFollowupReasonsForNonComplianceString + "FELT BETTER AND STOPPED TAKING MEDICATION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_forgot)))
                    diabetesFollowupReasonsForNonComplianceString = diabetesFollowupReasonsForNonComplianceString + "FORGOT TO TAKE MEDICATION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_concerned)))
                    diabetesFollowupReasonsForNonComplianceString = diabetesFollowupReasonsForNonComplianceString + "CONCERNED ABOUT PRIVACY/STIGMA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_transport_problems)))
                    diabetesFollowupReasonsForNonComplianceString = diabetesFollowupReasonsForNonComplianceString + "TRANSPORT PROBLEMS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_other)))
                    diabetesFollowupReasonsForNonComplianceString = diabetesFollowupReasonsForNonComplianceString + "NON-ADHERENCE REASON (TEXT)" + " ; ";
            }

            observations.add(new String[]{"REASON FOR POOR TREATMENT ADHERENCE", diabetesFollowupReasonsForNonComplianceString});
        }

        if (diabetesFollowupIfOther.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"NON-ADHERENCE REASON (TEXT)", App.get(diabetesFollowupIfOther)});
        }
        if (diabetesFollowupStatusOfDiabetesCondition.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DIABETES CONTROL STATUS", App.get(diabetesFollowupStatusOfDiabetesCondition).equals(getResources().getString(R.string.comorbidities_diabetes_followup_status_of_diabetes_condition_option_controlled)) ? "CONTROLLED DIABETES MELLITUS" : "UNCONTROLLED DIABETES MELLITUS"});
        if (diabetesFollowupChangeInRegimen.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CHANGE REGIMEN", App.get(diabetesFollowupChangeInRegimen).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if (diabetesFollowupNewPrescribedMedication.getVisibility() == View.VISIBLE) {
            String diabetesTreatmentInitiationString = "";
            for (CheckBox cb : diabetesFollowupNewPrescribedMedication.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_option)))
                    diabetesTreatmentInitiationString = diabetesTreatmentInitiationString + "METFORMIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_sulfonlyurea_option)))
                    diabetesTreatmentInitiationString = diabetesTreatmentInitiationString + "SULFONLYUREAS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_tzd_option)))
                    diabetesTreatmentInitiationString = diabetesTreatmentInitiationString + "PIOGLITAZONE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_dpp4i_option)))
                    diabetesTreatmentInitiationString = diabetesTreatmentInitiationString + "DIPEPTIDYL PEPTIDASE 4 INHIBITOR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulinN_option)))
                    diabetesTreatmentInitiationString = diabetesTreatmentInitiationString + "INSULIN, ISOPHANE, HUMAN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulinR_option)))
                    diabetesTreatmentInitiationString = diabetesTreatmentInitiationString + "INSULIN, REGULAR, HUMAN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulin_mix_option)))
                    diabetesTreatmentInitiationString = diabetesTreatmentInitiationString + "INSULIN 70/30" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_other_option)))
                    diabetesTreatmentInitiationString = diabetesTreatmentInitiationString + "OTHER DRUG NAME" + " ; ";
            }
            observations.add(new String[]{"DIABETES MEDICATIONS", diabetesTreatmentInitiationString});
        }
        if (diabetesFollowupDosageStrengthofMetformin.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"METFORMIN DOSE", App.get(diabetesFollowupDosageStrengthofMetformin)});
        if (diabetesTreatmentInitiationTabletsPerDay.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"METFORMIN TABLET PER DAY", App.get(diabetesTreatmentInitiationTabletsPerDay)});
        if (diabetesTreatmentInitiationMetforminPrescribed.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"METFORMIN PRESCRIBED (FOR DAYS)", App.get(diabetesTreatmentInitiationMetforminPrescribed)});
        if (diabetesFollowupDosageInsulinN.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"INSULIN N DOSAGE", App.get(diabetesFollowupDosageInsulinN)});
        if (diabetesFollowupDosageInsulinR.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"INSULIN R DOSAGE", App.get(diabetesFollowupDosageInsulinR)});
        if (diabetesFollowupNextScheduledVisit.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDate(secondDateCalendar)});

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
                result = serverService.saveEncounterAndObservation(App.getProgram()+"-"+FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
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

        refillFlag = true;

        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            }

            if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                diabetesFollowupMonthOfVisit.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("BODY MASS INDEX")) {
                diabetesFollowupBodyMassIndex.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("REASON FOR DISCONTINUATION")) {
                reasonForDiscontinuation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("PATIENT CONSENT TO CONTINUE TREATMENT FOR DIABETES")) {
                for (RadioButton rb : patientAgreeToContinueTreatment.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        reasonForDiscontinuation.setVisibility(View.GONE);
                        showHideOnAgreeDisagree(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        reasonForDiscontinuation.setVisibility(View.VISIBLE);
                        showHideOnAgreeDisagree(View.GONE);
                        break;
                    }
                }
            } else if (obs[0][0].equals("WAIST-HIP RATIO")) {
                diabetesFollowupWaistHipRatio.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("SYSTOLIC BLOOD PRESSURE")) {
                diabetesFollowupBloodPressureSystolic.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("DIASTOLIC BLOOD PRESSURE")) {
                diabetesFollowupBloodPressureDiastolic.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("HBA1C RESULT")) {
                diabetesFollowupHba1cTestResult.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("RANDOM BLOOD SUGAR")) {
                diabetesFollowupRBSTestResult.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("PATIENT ADHERENT TO TRETMENT")) {
                for (RadioButton rb : diabetesFollowupHasPrescribedMedication.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("REASON FOR POOR TREATMENT ADHERENCE")) {
                for (CheckBox cb : diabetesFollowupReasonsForNonComplianceCB.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_side_effects)) && obs[0][1].equals("SIDE EFFECTS (TEXT)")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_misunderstandings)) && obs[0][1].equals("UNABLE TO READ PRESCRIPTION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_alcohol)) && obs[0][1].equals("ALCOHOL ABUSE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_depression)) && obs[0][1].equals("DEPRESSION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_medication)) && obs[0][1].equals("MEDICATIONS UNAVAILABLE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_fear)) && obs[0][1].equals("TREATMENT OR PROCEDURE NOT CARRIED OUT DUE TO FEAR OF SIDE EFFECTS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_not_affordable)) && obs[0][1].equals("CANNOT AFFORD TREATMENT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_lost_medication)) && obs[0][1].equals("LOST OR RAN OUT OF MEDICATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_feeling_ill)) && obs[0][1].equals("FELT TOO ILL TO TAKE MEDICATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_feeling_better)) && obs[0][1].equals("FELT BETTER AND STOPPED TAKING MEDICATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_forgot)) && obs[0][1].equals("FORGOT TO TAKE MEDICATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_concerned)) && obs[0][1].equals("CONCERNED ABOUT PRIVACY/STIGMA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_transport_problems)) && obs[0][1].equals("TRANSPORT PROBLEMS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_other)) && obs[0][1].equals("NON-ADHERENCE REASON (TEXT)")) {
                        cb.setChecked(true);
                        displayIfOther();
                        break;
                    }
                }
            } else if (obs[0][0].equals("NON-ADHERENCE REASON (TEXT)")) {
                diabetesFollowupIfOther.getEditText().setText(obs[0][1]);
                diabetesFollowupIfOther.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("DIABETES CONTROL STATUS")) {
                for (RadioButton rb : diabetesFollowupStatusOfDiabetesCondition.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_status_of_diabetes_condition_option_controlled)) && obs[0][1].equals("CONTROLLED DIABETES MELLITUS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_followup_status_of_diabetes_condition_option_uncontrolled)) && obs[0][1].equals("UNCONTROLLED DIABETES MELLITUS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("CHANGE REGIMEN")) {
                for (RadioButton rb : diabetesFollowupChangeInRegimen.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("DIABETES MEDICATIONS")) {
                for (CheckBox cb : diabetesFollowupNewPrescribedMedication.getCheckedBoxes()) {
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
            } else if (obs[0][0].equals("METFORMIN DOSE")) {
                for (RadioButton rb : diabetesFollowupDosageStrengthofMetformin.getRadioGroup().getButtons()) {
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

            } else if (obs[0][0].equals("METFORMIN TABLET PER DAY")) {
                diabetesTreatmentInitiationTabletsPerDay.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("METFORMIN PRESCRIBED (FOR DAYS)")) {
                diabetesTreatmentInitiationMetforminPrescribed.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("INSULIN N DOSAGE")) {
                diabetesFollowupDosageInsulinN.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("INSULIN R DOSAGE")) {
                diabetesFollowupDosageInsulinR.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                diabetesFollowupNextScheduledVisit.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
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

        if (view == diabetesFollowupNextScheduledVisit.getButton()) {
            diabetesFollowupNextScheduledVisit.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", false);
            args.putBoolean("allowFutureDate", true);
            args.putString("formDate", formDate.getButtonText());
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            dateChoose = true;
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

        for (CheckBox cb : diabetesFollowupNewPrescribedMedication.getCheckedBoxes()) {
            if (cb.isChecked()) {
                diabetesFollowupNewPrescribedMedication.getQuestionView().setError(null);
                break;
            }
        }

        for (CheckBox cb : diabetesFollowupReasonsForNonComplianceCB.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getString(R.string.comorbidities_diabetes_followup_non_compliance_options_other))) {
                displayIfOther();
            } else {
                displayIfOther();
            }
        }
    }

    @Override
    public void resetViews() {
        super.resetViews();

        Boolean flag = true;

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        if (!dateChoose) {
            secondDateCalendar.set(Calendar.YEAR, formDateCalendar.get(Calendar.YEAR));
            secondDateCalendar.set(Calendar.MONTH, formDateCalendar.get(Calendar.MONTH));
            secondDateCalendar.set(Calendar.DAY_OF_MONTH, formDateCalendar.get(Calendar.DAY_OF_MONTH));
            secondDateCalendar.add(Calendar.MONTH, 3);
            diabetesFollowupNextScheduledVisit.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }
        displayReasonForNonCompliance();
        displayIfOther();
        showMetforminPerDayAndPrescribed();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);

                refill(formId);
                flag = false;

            } else bundle.putBoolean("save", false);

        }

        if (flag) {
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
                    String monthOfTreatment = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.COMORBIDITIES_VITALS_FORM, "FOLLOW-UP MONTH");
                    String bmi = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.COMORBIDITIES_VITALS_FORM, "BODY MASS INDEX");
                    String whr = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.COMORBIDITIES_VITALS_FORM, "WAIST-HIP RATIO");
                    String systolicBP = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.COMORBIDITIES_VITALS_FORM, "SYSTOLIC BLOOD PRESSURE");
                    String diastolicBP = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.COMORBIDITIES_VITALS_FORM, "DIASTOLIC BLOOD PRESSURE");
                    String hba1cResult = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + "HbA1C Test Result", "HBA1C RESULT");
                    String bloodSugarResult = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + "Blood Sugar Test Result", "RANDOM BLOOD SUGAR");

                    if (monthOfTreatment != null && !monthOfTreatment.equals(""))
                        monthOfTreatment = monthOfTreatment.replace(".0", "");

                    if (monthOfTreatment != null)
                        if (!monthOfTreatment.equals(""))
                            result.put("FOLLOW-UP MONTH", monthOfTreatment);
                    if (bmi != null)
                        if (!bmi.equals(""))
                            result.put("BODY MASS INDEX", bmi);
                    if (whr != null)
                        if (!whr.equals(""))
                            result.put("WAIST-HIP RATIO", whr);
                    if (systolicBP != null)
                        if (!systolicBP.equals(""))
                            result.put("SYSTOLIC BLOOD PRESSURE", systolicBP);
                    if (diastolicBP != null)
                        if (!diastolicBP.equals(""))
                            result.put("DIASTOLIC BLOOD PRESSURE", diastolicBP);
                    if (hba1cResult != null)
                        if (!hba1cResult.equals(""))
                            result.put("HBA1C RESULT", hba1cResult);
                    if (bloodSugarResult != null && !bloodSugarResult.equals(""))
                        bloodSugarResult = bloodSugarResult.replace(".0", "");
                    if (bloodSugarResult != null)
                        if (!bloodSugarResult.equals(""))
                            result.put("RANDOM BLOOD SUGAR", bloodSugarResult);

                    return result;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                }

                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();

                    diabetesFollowupMonthOfVisit.getEditText().setText(result.get("FOLLOW-UP MONTH"));
                    diabetesFollowupBodyMassIndex.getEditText().setText(result.get("BODY MASS INDEX"));
                    diabetesFollowupWaistHipRatio.getEditText().setText(result.get("WAIST-HIP RATIO"));
                    diabetesFollowupBloodPressureSystolic.getEditText().setText(result.get("SYSTOLIC BLOOD PRESSURE"));
                    diabetesFollowupBloodPressureDiastolic.getEditText().setText(result.get("DIASTOLIC BLOOD PRESSURE"));
                    diabetesFollowupHba1cTestResult.getEditText().setText(result.get("HBA1C RESULT"));
                    diabetesFollowupRBSTestResult.getEditText().setText(result.get("RANDOM BLOOD SUGAR"));
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

        if (radioGroup == diabetesFollowupHasPrescribedMedication.getRadioGroup()) {
            displayReasonForNonCompliance();
            displayIfOther();
        }

        if (radioGroup == diabetesFollowupChangeInRegimen.getRadioGroup()) {
            displayChangeInRegimenOrNot();
        }
        if (radioGroup == patientAgreeToContinueTreatment.getRadioGroup()) {
            if (patientAgreeToContinueTreatment.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.yes))) {
                reasonForDiscontinuation.setVisibility(View.GONE);
                showHideOnAgreeDisagree(View.VISIBLE);
            } else {
                reasonForDiscontinuation.setVisibility(View.VISIBLE);
                showHideOnAgreeDisagree(View.GONE);

            }
        }
    }

    void displayReasonForNonCompliance() {
        if (diabetesFollowupHasPrescribedMedication.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.no))) {
            diabetesFollowupReasonsForNonComplianceCB.setVisibility(View.VISIBLE);
        } else {
            diabetesFollowupReasonsForNonComplianceCB.setVisibility(View.GONE);
        }
    }

    void displayIfOther() {
        for (CheckBox cb : diabetesFollowupReasonsForNonComplianceCB.getCheckedBoxes()) {
            if (diabetesFollowupHasPrescribedMedication.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.no)) && cb.isChecked() && cb.getText().equals(getString(R.string.comorbidities_diabetes_followup_non_compliance_options_other))) {
                diabetesFollowupIfOther.setVisibility(View.VISIBLE);
            } else {
                diabetesFollowupIfOther.setVisibility(View.GONE);
            }
        }
    }

    void showMetforminPerDayAndPrescribed() {

        Boolean flag = false;
        for (CheckBox cb : diabetesFollowupNewPrescribedMedication.getCheckedBoxes()) {
            if (cb.getText().equals(getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_option)) && cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (flag) {
            diabetesTreatmentInitiationTabletsPerDay.setVisibility(View.VISIBLE);
            diabetesTreatmentInitiationMetforminPrescribed.setVisibility(View.VISIBLE);
        } else {
            diabetesTreatmentInitiationTabletsPerDay.setVisibility(View.GONE);
            diabetesTreatmentInitiationMetforminPrescribed.setVisibility(View.GONE);
        }
    }

    void showHideOnAgreeDisagree(int visibility) {
        diabetesFollowupMonthOfVisit.setVisibility(visibility);
        diabetesFollowupBodyMassIndex.setVisibility(visibility);
        diabetesFollowupWaistHipRatio.setVisibility(visibility);
        diabetesFollowupBloodPressureSystolic.setVisibility(visibility);
        diabetesFollowupBloodPressureDiastolic.setVisibility(visibility);
        diabetesFollowupHba1cTestResult.setVisibility(visibility);
        diabetesFollowupRBSTestResult.setVisibility(visibility);
        diabetesFollowupHasPrescribedMedication.setVisibility(visibility);
        diabetesFollowupReasonsForNonComplianceCB.setVisibility(visibility);
        diabetesFollowupIfOther.setVisibility(visibility);
        diabetesFollowupStatusOfDiabetesCondition.setVisibility(visibility);
        diabetesFollowupChangeInRegimen.setVisibility(visibility);
        diabetesFollowupNewPrescribedMedication.setVisibility(visibility);
        diabetesTreatmentInitiationTabletsPerDay.setVisibility(visibility);
        diabetesTreatmentInitiationMetforminPrescribed.setVisibility(visibility);
        diabetesFollowupDosageStrengthofMetformin.setVisibility(visibility);
        diabetesFollowupDosageInsulinN.setVisibility(visibility);
        diabetesFollowupDosageInsulinR.setVisibility(visibility);
        diabetesFollowupNextScheduledVisit.setVisibility(visibility);

        if (visibility == View.VISIBLE) {
            displayReasonForNonCompliance();
            showMetforminPerDayAndPrescribed();
            displayIfOther();
            displayChangeInRegimenOrNot();
        }
    }

    void displayChangeInRegimenOrNot() {
        if (diabetesFollowupChangeInRegimen.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.yes))) {
            diabetesFollowupNewPrescribedMedication.setVisibility(View.VISIBLE);
            diabetesFollowupDosageStrengthofMetformin.setVisibility(View.VISIBLE);
            diabetesFollowupDosageInsulinN.setVisibility(View.VISIBLE);
            diabetesFollowupDosageInsulinR.setVisibility(View.VISIBLE);
        } else {
            diabetesFollowupNewPrescribedMedication.setVisibility(View.GONE);
            diabetesFollowupDosageStrengthofMetformin.setVisibility(View.GONE);
            diabetesFollowupDosageInsulinN.setVisibility(View.GONE);
            diabetesFollowupDosageInsulinR.setVisibility(View.GONE);
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




