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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by New User on 29-Dec-16.
 */

public class ComorbiditiesVitalsForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledEditText vitalsMonthOfVisit;
    TitledEditText vitalsWeight;
    TitledEditText vitalsHeight;
    MyTextView vitalsHeightInMeters;
    TitledEditText vitalsBodyMassIndex;
    TitledEditText vitalsWaistCircumference;
    TitledEditText vitalsHipCircumference;
    TitledEditText vitalsWaistHipRatio;
    TitledEditText vitalsBloodPressureSystolic;
    TitledEditText vitalsBloodPressureDiastolic;

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
        FORM_NAME = Forms.COMORBIDITIES_VITALS_FORM;
        FORM = Forms.comorbidities_vitalsForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesVitalsForm.MyAdapter());
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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        vitalsMonthOfVisit = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_vitals_month_of_visit), "", getResources().getString(R.string.comorbidities_vitals_month_of_visit_range), 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        vitalsWeight = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_vitals_weight), "", getResources().getString(R.string.comorbidities_vitals_weight_range), 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        vitalsHeight = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_vitals_height), "", getResources().getString(R.string.comorbidities_vitals_height_range), 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        vitalsHeightInMeters = new MyTextView(context, "");
        vitalsHeightInMeters.setVisibility(View.GONE);
        vitalsBodyMassIndex = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_vitals_bmi), "", getResources().getString(R.string.comorbidities_vitals_bmi_range), 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        //vitalsBodyMassIndex.getEditText().setFocusable(false);
        vitalsWaistCircumference = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_vitals_waist_circumference), "", getResources().getString(R.string.comorbidities_vitals_waist_hip_whr_range), 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        vitalsHipCircumference = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_vitals_hip_circumference), "", getResources().getString(R.string.comorbidities_vitals_waist_hip_whr_range), 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        vitalsWaistHipRatio = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_vitals_whr), "", getResources().getString(R.string.comorbidities_vitals_waist_hip_whr_range), 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        vitalsWaistHipRatio.getEditText().setFocusable(false);
        vitalsBloodPressureSystolic = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_vitals_bp_systolic), "", getResources().getString(R.string.comorbidities_vitals_bp_systolic_range1), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        vitalsBloodPressureDiastolic = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_vitals_bp_diastolic), "", getResources().getString(R.string.comorbidities_vitals_bp_diastolic_range1), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), vitalsMonthOfVisit.getEditText(), vitalsWeight.getEditText(), vitalsHeight.getEditText(), vitalsBodyMassIndex.getEditText(), vitalsWaistCircumference.getEditText(),
                vitalsHipCircumference.getEditText(), vitalsWaistHipRatio.getEditText(), vitalsBloodPressureSystolic.getEditText(), vitalsBloodPressureDiastolic.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, vitalsMonthOfVisit, vitalsWeight, vitalsHeight, vitalsHeightInMeters, vitalsBodyMassIndex, vitalsWaistCircumference, vitalsHipCircumference, vitalsWaistHipRatio, vitalsBloodPressureSystolic, vitalsBloodPressureDiastolic}};

        formDate.getButton().setOnClickListener(this);

        vitalsMonthOfVisit.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (vitalsMonthOfVisit.getEditText().getText().length() > 0) {
                        int num = Integer.parseInt(vitalsMonthOfVisit.getEditText().getText().toString());
                        if (num < 0 || num > 24) {
                            vitalsMonthOfVisit.getEditText().setError(getString(R.string.comorbidities_vitals_month_of_visit_limit));
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        vitalsWeight.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (vitalsWeight.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(vitalsWeight.getEditText().getText().toString());
                        if (num < 0.5 || num > 700) {
                            vitalsWeight.getEditText().setError(getString(R.string.comorbidities_vitals_weight_limit));
                        } else {
                            if (!App.get(vitalsWeight).isEmpty() && !App.get(vitalsHeight).isEmpty()) {

                                if(calculateBMI() < 0 || calculateBMI() > 200) {

                                    //For Invalid BMI
                                    vitalsBodyMassIndex.getEditText().setError(getString(R.string.comorbidities_vitals_bmi_invalid));

                                    int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

                                    final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
                                    alertDialog.setMessage(getString(R.string.comorbidities_vitals_bmi_invalid));
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
                                }
                                else {
                                    //For Valid BMI
                                    vitalsBodyMassIndex.getEditText().setText(String.valueOf(calculateBMI()));
                                    vitalsBodyMassIndex.getEditText().setError(null);
                                }
                            }
                        }
                    }
                    else if (vitalsWeight.getEditText().getText().length() == 0) {
                        vitalsBodyMassIndex.getEditText().setText("");
                        vitalsBodyMassIndex.getEditText().setError(null);
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        vitalsHeight.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (vitalsHeight.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(vitalsHeight.getEditText().getText().toString());
                        if (num < 30 || num > 260) {
                            vitalsHeight.getEditText().setError(getString(R.string.comorbidities_vitals_height_limit));
                        } else {
                            vitalsHeightInMeters.setText(getResources().getString(R.string.comorbidities_vitals_height_metres) + String.valueOf(convertCentimeterToMeter(Double.parseDouble(vitalsHeight.getEditText().getText().toString()))) + " m");
                            vitalsHeightInMeters.setVisibility(View.VISIBLE);
                            if (!App.get(vitalsWeight).isEmpty() && !App.get(vitalsHeight).isEmpty()) {

                                if(calculateBMI() < 0 || calculateBMI() > 200) {

                                    //For Invalid BMI
                                    vitalsBodyMassIndex.getEditText().setError(getString(R.string.comorbidities_vitals_bmi_invalid));

                                    int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

                                    final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
                                    alertDialog.setMessage(getString(R.string.comorbidities_vitals_bmi_invalid));
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
                                }
                                else {

                                    //For Valid BMI
                                    vitalsBodyMassIndex.getEditText().setText(String.valueOf(calculateBMI()));
                                    vitalsBodyMassIndex.getEditText().setError(null);
                                }
                            }
                        }
                    } else if (vitalsHeight.getEditText().getText().length() == 0) {
                        vitalsHeightInMeters.setVisibility(View.GONE);
                        vitalsBodyMassIndex.getEditText().setText("");
                        vitalsBodyMassIndex.getEditText().setError(null);
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        vitalsWaistCircumference.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (vitalsWaistCircumference.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(vitalsWaistCircumference.getEditText().getText().toString());
                        if (num < 0 || num > 100) {
                            vitalsWaistCircumference.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
                        } else {
                            if (!App.get(vitalsWaistCircumference).isEmpty() && !App.get(vitalsHipCircumference).isEmpty()) {
                                //vitalsWaistHipRatio.getEditText().setText(String.valueOf(calculateWHR()));
                                //vitalsWaistHipRatio.getEditText().setError(null);

                                if(calculateWHR() < 0 || calculateWHR() > 100) {

                                    //For Invalid WHR
                                    vitalsWaistHipRatio.getEditText().setError(getString(R.string.comorbidities_vitals_whr_invalid));

                                    int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

                                    final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
                                    alertDialog.setMessage(getString(R.string.comorbidities_vitals_whr_invalid));
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
                                }
                                else {

                                    //For Valid WHR
                                    vitalsWaistHipRatio.getEditText().setText(String.valueOf(calculateWHR()));
                                    vitalsWaistHipRatio.getEditText().setError(null);
                                }

                            }
                        }
                    } else if (vitalsWaistCircumference.getEditText().getText().length() == 0) {
                        vitalsWaistHipRatio.getEditText().setText("");
                        vitalsWaistHipRatio.getEditText().setError(null);
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        vitalsHipCircumference.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (vitalsHipCircumference.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(vitalsHipCircumference.getEditText().getText().toString());
                        if (num < 0 || num > 100) {
                            vitalsHipCircumference.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
                        } else {
                            if (!App.get(vitalsWaistCircumference).isEmpty() && !App.get(vitalsHipCircumference).isEmpty()) {
                                //vitalsWaistHipRatio.getEditText().setText(String.valueOf(calculateWHR()));
                                //vitalsWaistHipRatio.getEditText().setError(null);

                                if(calculateWHR() < 0 || calculateWHR() > 100) {

                                    //For Invalid WHR
                                    vitalsWaistHipRatio.getEditText().setError(getString(R.string.comorbidities_vitals_whr_invalid));

                                    int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

                                    final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
                                    alertDialog.setMessage(getString(R.string.comorbidities_vitals_whr_invalid));
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
                                }
                                else {

                                    //For Valid WHR
                                    vitalsWaistHipRatio.getEditText().setText(String.valueOf(calculateWHR()));
                                    vitalsWaistHipRatio.getEditText().setError(null);
                                }
                            }
                        }
                    }  else if (vitalsHipCircumference.getEditText().getText().length() == 0) {
                        vitalsWaistHipRatio.getEditText().setText("");
                        vitalsWaistHipRatio.getEditText().setError(null);
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        vitalsBloodPressureSystolic.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (vitalsBloodPressureSystolic.getEditText().getText().length() > 0) {
                        int num = Integer.parseInt(vitalsBloodPressureSystolic.getEditText().getText().toString());
                        if (num < 0 || num > 250) {
                            vitalsBloodPressureSystolic.getEditText().setError(getString(R.string.comorbidities_vitals_bp_systolic_limit1));
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        vitalsBloodPressureDiastolic.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (vitalsBloodPressureDiastolic.getEditText().getText().length() > 0) {
                        int num = Integer.parseInt(vitalsBloodPressureDiastolic.getEditText().getText().toString());
                        if (num < 0 || num > 150) {
                            vitalsBloodPressureDiastolic.getEditText().setError(getString(R.string.comorbidities_vitals_bp_diastolic_limit1));
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

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
    }

    @Override
    public boolean validate() {

        Boolean error = false;

        if (App.get(vitalsBloodPressureDiastolic).isEmpty()) {
            gotoFirstPage();
            vitalsBloodPressureDiastolic.getEditText().setError(getString(R.string.empty_field));
            vitalsBloodPressureDiastolic.getEditText().requestFocus();
            error = true;
        } else if (!App.get(vitalsBloodPressureDiastolic).isEmpty() && Integer.parseInt(App.get(vitalsBloodPressureDiastolic)) > 150) {
            gotoFirstPage();
            vitalsBloodPressureDiastolic.getEditText().setError(getString(R.string.comorbidities_vitals_bp_diastolic_limit1));
            vitalsBloodPressureDiastolic.getEditText().requestFocus();
            error = true;
        }

        if (App.get(vitalsBloodPressureSystolic).isEmpty()) {
            gotoFirstPage();
            vitalsBloodPressureSystolic.getEditText().setError(getString(R.string.empty_field));
            vitalsBloodPressureSystolic.getEditText().requestFocus();
            error = true;
        } else if (!App.get(vitalsBloodPressureSystolic).isEmpty() && Integer.parseInt(App.get(vitalsBloodPressureSystolic)) > 250) {
            gotoFirstPage();
            vitalsBloodPressureSystolic.getEditText().setError(getString(R.string.comorbidities_vitals_bp_systolic_limit1));
            vitalsBloodPressureSystolic.getEditText().requestFocus();
            error = true;
        }

        if (App.get(vitalsWaistHipRatio).isEmpty()) {
            gotoFirstPage();
            vitalsWaistHipRatio.getEditText().setError(getString(R.string.empty_field));
            vitalsWaistHipRatio.getEditText().requestFocus();
            error = true;
        }

        if (App.get(vitalsHipCircumference).isEmpty()) {
            gotoFirstPage();
            vitalsHipCircumference.getEditText().setError(getString(R.string.empty_field));
            vitalsHipCircumference.getEditText().requestFocus();
            error = true;
        } else if (!RegexUtil.isNumeric(App.get(vitalsHipCircumference), true)) {
            gotoFirstPage();
            vitalsHipCircumference.getEditText().setError(getString(R.string.comorbidities_hba1c_not_valid_result_value));
            vitalsHipCircumference.getEditText().requestFocus();
            error = true;
        } else if (!App.get(vitalsHipCircumference).isEmpty() && Double.parseDouble(App.get(vitalsHipCircumference)) > 100) {
            gotoFirstPage();
            vitalsHipCircumference.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
            vitalsHipCircumference.getEditText().requestFocus();
            error = true;
        }

        if (App.get(vitalsWaistCircumference).isEmpty()) {
            gotoFirstPage();
            vitalsWaistCircumference.getEditText().setError(getString(R.string.empty_field));
            vitalsWaistCircumference.getEditText().requestFocus();
            error = true;
        } else if (!RegexUtil.isNumeric(App.get(vitalsWaistCircumference), true)) {
            gotoFirstPage();
            vitalsWaistCircumference.getEditText().setError(getString(R.string.comorbidities_hba1c_not_valid_result_value));
            vitalsWaistCircumference.getEditText().requestFocus();
            error = true;
        } else if (!App.get(vitalsWaistCircumference).isEmpty() && Double.parseDouble(App.get(vitalsWaistCircumference)) > 100) {
            gotoFirstPage();
            vitalsWaistCircumference.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
            vitalsWaistCircumference.getEditText().requestFocus();
            error = true;
        }

        if (App.get(vitalsBodyMassIndex).isEmpty()) {
            gotoFirstPage();
            vitalsBodyMassIndex.getEditText().setError(getString(R.string.empty_field));
            vitalsBodyMassIndex.getEditText().requestFocus();
            error = true;
        }

        if (App.get(vitalsHeight).isEmpty()) {
            gotoFirstPage();
            vitalsHeight.getEditText().setError(getString(R.string.empty_field));
            vitalsHeight.getEditText().requestFocus();
            error = true;
        } else if (!RegexUtil.isNumeric(App.get(vitalsHeight), true)) {
            gotoFirstPage();
            vitalsHeight.getEditText().setError(getString(R.string.comorbidities_hba1c_not_valid_result_value));
            vitalsHeight.getEditText().requestFocus();
            error = true;
        } else if (!App.get(vitalsHeight).isEmpty() && Double.parseDouble(App.get(vitalsHeight)) > 260) {
            gotoFirstPage();
            vitalsHeight.getEditText().setError(getString(R.string.comorbidities_vitals_height_limit));
            vitalsHeight.getEditText().requestFocus();
            error = true;
        }

        if (App.get(vitalsWeight).isEmpty()) {
            gotoFirstPage();
            vitalsWeight.getEditText().setError(getString(R.string.empty_field));
            vitalsWeight.getEditText().requestFocus();
            error = true;
        } else if (!RegexUtil.isNumeric(App.get(vitalsWeight), true)) {
            gotoFirstPage();
            vitalsWeight.getEditText().setError(getString(R.string.comorbidities_hba1c_not_valid_result_value));
            vitalsWeight.getEditText().requestFocus();
            error = true;
        } else if (!App.get(vitalsWeight).isEmpty() && Double.parseDouble(App.get(vitalsWeight)) > 700) {
            gotoFirstPage();
            vitalsWeight.getEditText().setError(getString(R.string.comorbidities_vitals_weight_limit));
            vitalsWeight.getEditText().requestFocus();
            error = true;
        }

        if (App.get(vitalsMonthOfVisit).isEmpty()) {
            gotoFirstPage();
            vitalsMonthOfVisit.getEditText().setError(getString(R.string.empty_field));
            vitalsMonthOfVisit.getEditText().requestFocus();
            error = true;
        } else if (!App.get(vitalsMonthOfVisit).isEmpty() && Integer.parseInt(App.get(vitalsMonthOfVisit)) > 24) {
            gotoFirstPage();
            vitalsMonthOfVisit.getEditText().setError(getString(R.string.comorbidities_vitals_month_of_visit_limit));
            vitalsMonthOfVisit.getEditText().requestFocus();
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
        observations.add(new String[]{"FOLLOW-UP MONTH", App.get(vitalsMonthOfVisit)});
        observations.add(new String[]{"WEIGHT (KG)", App.get(vitalsWeight)});
        observations.add(new String[]{"HEIGHT (CM)", App.get(vitalsHeight)});
        observations.add(new String[]{"BODY MASS INDEX", App.get(vitalsBodyMassIndex)});
        observations.add(new String[]{"WAIST CIRCUMFERENCE (CM)", App.get(vitalsWaistCircumference)});
        observations.add(new String[]{"HIP CIRCUMFERENCE (CM)", App.get(vitalsHipCircumference)});
        observations.add(new String[]{"WAIST-HIP RATIO", App.get(vitalsWaistHipRatio)});
        observations.add(new String[]{"SYSTOLIC BLOOD PRESSURE", App.get(vitalsBloodPressureSystolic)});
        observations.add(new String[]{"DIASTOLIC BLOOD PRESSURE", App.get(vitalsBloodPressureDiastolic)});

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

            if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                vitalsMonthOfVisit.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("WEIGHT (KG)")) {
                vitalsWeight.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("HEIGHT (CM)")) {
                vitalsHeight.getEditText().setText(obs[0][1]);
            }  else if (obs[0][0].equals("BODY MASS INDEX")) {
                vitalsBodyMassIndex.getEditText().setText(obs[0][1]);
            }  else if (obs[0][0].equals("WAIST CIRCUMFERENCE (CM)")) {
                vitalsWaistCircumference.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("HIP CIRCUMFERENCE (CM)")) {
                vitalsHipCircumference.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("WAIST-HIP RATIO")) {
                vitalsWaistHipRatio.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("SYSTOLIC BLOOD PRESSURE")) {
                vitalsBloodPressureSystolic.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("DIASTOLIC BLOOD PRESSURE")) {
                vitalsBloodPressureDiastolic.getEditText().setText(obs[0][1]);
            }
        }
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

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
    }

    double convertCentimeterToMeter(double cm) {
        return cm / 100;
    }

    double calculateBMI() {

        double weight = Double.parseDouble(vitalsWeight.getEditText().getText().toString());

        double meters = convertCentimeterToMeter(Double.parseDouble(vitalsHeight.getEditText().getText().toString()));

        return weight / (meters * meters);
    }

    double calculateWHR() {

        double waistCircumference = Double.parseDouble(vitalsWaistCircumference.getEditText().getText().toString());

        double hipCircumference = convertCentimeterToMeter(Double.parseDouble(vitalsHipCircumference.getEditText().getText().toString()));

        return (waistCircumference / hipCircumference) / 100;
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



