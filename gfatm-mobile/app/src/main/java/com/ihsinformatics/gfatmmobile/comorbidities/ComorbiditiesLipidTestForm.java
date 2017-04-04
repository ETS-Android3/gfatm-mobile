package com.ihsinformatics.gfatmmobile.comorbidities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.text.TextWatcher;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Fawad Jawaid on 23-Jan-17.
 */

public class ComorbiditiesLipidTestForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener, View.OnTouchListener {

    public static final int THIRD_DATE_DIALOG_ID = 3;
    // Extra Views for date ...
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;
    protected Calendar fourthDateCalendar;
    Context context;
    // Views...
    TitledButton formDate;
    TitledRadioGroup formType;
    TitledEditText lipidTestID;
    //Views for Test Order
    MyTextView testOrderLipid;
    TitledSpinner lipidMonthOfVisit;
    TitledButton lipidTestOrderDate;
    ImageView testIdView;
    //Views for Test Result
    MyTextView testResultLipid;
    TitledButton lipidTestResultDate;
    TitledEditText lipidTotalCholestrol;
    TitledEditText lipidTriglycerides;
    TitledEditText lipidHDL;
    TitledEditText lipidLDL;
    TitledEditText lipidVLDL;
    TitledEditText lipidNonHDLCholestrol;
    TitledButton nextLipidTestDate;

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
        FORM_NAME = Forms.COMORBIDITIES_LIPID_TEST_FORM;
        FORM = Forms.comorbidities_lipidTestForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesLipidTestForm.MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(PAGE_COUNT - 1);
        formName.setText(FORM_NAME);

        thirdDateCalendar = Calendar.getInstance();
        thirdDateFragment = new ComorbiditiesLipidTestForm.SelectDateFragment();

        fourthDateCalendar = Calendar.getInstance();

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
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_testorder_testresult_form_type), getResources().getStringArray(R.array.comorbidities_testorder_testresult_form_type_options), "", App.HORIZONTAL, App.VERTICAL);
        testOrderLipid = new MyTextView(context, getResources().getString(R.string.comorbidities_lipid_test_order));
        testOrderLipid.setTypeface(null, Typeface.BOLD);
        lipidMonthOfVisit = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_urinedr_month_of_treatment), getResources().getStringArray(R.array.comorbidities_followup_month), "1", App.HORIZONTAL);
        lipidTestOrderDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_hba1cdate_test_order), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        LinearLayout linearLayout = new LinearLayout(context);
        lipidTestID = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_hhba1c_testid), "", "", 20, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.9f
        );
        lipidTestID.setLayoutParams(param);
        linearLayout.addView(lipidTestID);
        testIdView = new ImageView(context);
        testIdView.setImageResource(R.drawable.ic_checked);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.1f
        );
        testIdView.setLayoutParams(param1);
        testIdView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        testIdView.setPadding(0, 5, 0, 0);

        linearLayout.addView(testIdView);

        //second page views...
        testResultLipid = new MyTextView(context, getResources().getString(R.string.comorbidities_lipid_test_result));
        testResultLipid.setTypeface(null, Typeface.BOLD);
        lipidTestResultDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_hba1c_resultdate), DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);
        lipidTotalCholestrol = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_lipid_test_cholestrol), "", getResources().getString(R.string.comorbidities_lipid_test_total_cholestrol_range), 7, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        lipidTriglycerides = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_lipid_test_triglycerides), "", getResources().getString(R.string.comorbidities_lipid_test_triglycerides_range), 7, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        lipidHDL = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_lipid_test_hdl), "", getResources().getString(R.string.comorbidities_lipid_test_hdl_range), 7, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        lipidLDL = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_lipid_test_ldl), "", getResources().getString(R.string.comorbidities_lipid_test_ldl_range), 7, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        lipidVLDL = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_lipid_test_vldl), "", getResources().getString(R.string.comorbidities_lipid_test_vldl_range), 7, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        lipidNonHDLCholestrol = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_lipid_test_non_hdl), "", getResources().getString(R.string.comorbidities_lipid_test_non_hdl_range), 7, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        nextLipidTestDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_urinedr_nexttestdate), DateFormat.format("dd-MMM-yyyy", fourthDateCalendar).toString(), App.HORIZONTAL);
        //nextLipidTestDate.setVisibility(View.GONE);
        goneVisibility();

        // Used for reset fields...
        views = new View[]{formDate.getButton(), lipidTestID.getEditText(), formType.getRadioGroup(), testOrderLipid, lipidMonthOfVisit.getSpinner(), lipidTestOrderDate.getButton(), testResultLipid, lipidTestResultDate.getButton(),
                lipidTotalCholestrol.getEditText(), lipidTriglycerides.getEditText(), lipidHDL.getEditText(), lipidLDL.getEditText(), lipidVLDL.getEditText(), lipidNonHDLCholestrol.getEditText(), nextLipidTestDate.getButton()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, formType, linearLayout, testOrderLipid, lipidMonthOfVisit, lipidTestOrderDate, testResultLipid, lipidTestResultDate,
                        lipidTotalCholestrol, lipidTriglycerides, lipidHDL, lipidLDL, lipidVLDL, lipidNonHDLCholestrol, nextLipidTestDate}};

        formDate.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);

        lipidTestOrderDate.getButton().setOnClickListener(this);
        lipidTestResultDate.getButton().setOnClickListener(this);
        nextLipidTestDate.getButton().setOnClickListener(this);

        /*lipidTestID.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    try {
                        if (lipidTestID.getEditText().getText().length() > 0) {
                            if (lipidTestID.getEditText().getText().length() < 9) {
                                lipidTestID.getEditText().setError(getString(R.string.comorbidities_hhba1c_testid_format_error1));
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        //Exception: User might be entering " " (empty) value
                    }
                }
            }
        });*/

        lipidTotalCholestrol.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    try {
                        if (lipidTotalCholestrol.getEditText().getText().length() > 0) {
                            double num = Double.parseDouble(lipidTotalCholestrol.getEditText().getText().toString());
                            if (num < 100 || num > 500) {
                                lipidTotalCholestrol.getEditText().setError(getString(R.string.comorbidities_lipid_test_total_cholestrol_limit));
                            } else {
                                //Correct value
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        //Exception: User might be entering " " (empty) value
                    }
                }
            }
        });

        lipidTriglycerides.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    try {
                        if (lipidTriglycerides.getEditText().getText().length() > 0) {
                            double num = Double.parseDouble(lipidTriglycerides.getEditText().getText().toString());
                            if (num < 1 || num > 700) {
                                lipidTriglycerides.getEditText().setError(getString(R.string.comorbidities_lipid_test_triglycerides_range));
                            } else {
                                //Correct value
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        //Exception: User might be entering " " (empty) value
                    }
                }
            }
        });

        lipidHDL.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    try {
                        if (lipidHDL.getEditText().getText().length() > 0) {
                            double num = Double.parseDouble(lipidHDL.getEditText().getText().toString());
                            if (num < 10 || num > 200) {
                                lipidHDL.getEditText().setError(getString(R.string.comorbidities_lipid_test_hdl_limit));
                            } else {
                                //Correct value
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        //Exception: User might be entering " " (empty) value
                    }
                }
            }
        });

        lipidLDL.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    try {
                        if (lipidLDL.getEditText().getText().length() > 0) {
                            double num = Double.parseDouble(lipidLDL.getEditText().getText().toString());
                            if (num < 20 || num > 400) {
                                lipidLDL.getEditText().setError(getString(R.string.comorbidities_lipid_test_ldl_limit));
                            } else {
                                //Correct value
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        //Exception: User might be entering " " (empty) value
                    }
                }
            }
        });

        lipidVLDL.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    try {
                        if (lipidVLDL.getEditText().getText().length() > 0) {
                            double num = Double.parseDouble(lipidVLDL.getEditText().getText().toString());
                            if (num < 30 || num > 400) {
                                lipidVLDL.getEditText().setError(getString(R.string.comorbidities_lipid_test_vldl_limit));
                            } else {
                                //Correct value
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        //Exception: User might be entering " " (empty) value
                    }
                }
            }
        });

        /*lipidNonHDLCholestrol.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    try {
                        if (lipidNonHDLCholestrol.getEditText().getText().length() > 0) {
                            double num = Double.parseDouble(lipidNonHDLCholestrol.getEditText().getText().toString());
                            if (num < 1 || num > 300) {
                                lipidNonHDLCholestrol.getEditText().setError(getString(R.string.comorbidities_lipid_test_non_hdl_limit));
                            } else {
                                //Correct value
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        //Exception: User might be entering " " (empty) value
                    }
                }
            }
        });

        /*lipidTotalCholestrol.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (lipidTotalCholestrol.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(lipidTotalCholestrol.getEditText().getText().toString());
                        if (num < 0 || num > 10) {
                            lipidTotalCholestrol.getEditText().setError(getString(R.string.comorbidities_urinedr_spgravity_ph_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        lipidTriglycerides.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (lipidTriglycerides.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(lipidTriglycerides.getEditText().getText().toString());
                        if (num < 0 || num > 10) {
                            lipidTriglycerides.getEditText().setError(getString(R.string.comorbidities_urinedr_spgravity_ph_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        lipidHDL.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (lipidHDL.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(lipidHDL.getEditText().getText().toString());
                        if (num < 0 || num > 100) {
                            lipidHDL.getEditText().setError(getString(R.string.comorbidities_vitals_waist_hip_whr_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        lipidLDL.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (lipidLDL.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(lipidLDL.getEditText().getText().toString());
                        if (num < 0 || num > 400) {
                            lipidLDL.getEditText().setError(getString(R.string.comorbidities_urinedr_sugar_ketones_uro_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        lipidVLDL.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (lipidVLDL.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(lipidVLDL.getEditText().getText().toString());
                        if (num < 0 || num > 400) {
                            lipidVLDL.getEditText().setError(getString(R.string.comorbidities_urinedr_sugar_ketones_uro_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });*/

        lipidNonHDLCholestrol.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (lipidNonHDLCholestrol.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(lipidNonHDLCholestrol.getEditText().getText().toString());
                        if (num < 1 || num > 300) {
                            lipidNonHDLCholestrol.getEditText().setError(getString(R.string.comorbidities_lipid_test_non_hdl_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        lipidTestID.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (lipidTestID.getEditText().getText().length() > 0) {
                     /*   if (lipidTestID.getEditText().getText().length() < 11) {
                            lipidTestID.getEditText().setError(getString(R.string.comorbidities_blood_sugar_testid_format_error));
                        }
                    }*/
                    testIdView.setVisibility(View.VISIBLE);
                    testIdView.setImageResource(R.drawable.ic_checked);
                } else {
                    testIdView.setVisibility(View.INVISIBLE);
                }
                goneVisibility();
                submitButton.setEnabled(false);
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });
        testIdView.setOnTouchListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        lipidTestOrderDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        lipidTestResultDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
        //fourthDateCalendar = thirdDateCalendar;
        fourthDateCalendar.set(Calendar.YEAR, secondDateCalendar.get(Calendar.YEAR));
        fourthDateCalendar.set(Calendar.MONTH, secondDateCalendar.get(Calendar.MONTH));
        fourthDateCalendar.set(Calendar.DAY_OF_MONTH, secondDateCalendar.get(Calendar.DAY_OF_MONTH));
        fourthDateCalendar.add(Calendar.MONTH, 2);
        fourthDateCalendar.add(Calendar.DAY_OF_MONTH, 20);
        nextLipidTestDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", fourthDateCalendar).toString());
    }

    @Override
    public boolean validate() {

        Boolean error = false;

        if (lipidNonHDLCholestrol.getVisibility() == View.VISIBLE && App.get(lipidNonHDLCholestrol).isEmpty()) {
            gotoLastPage();
            lipidNonHDLCholestrol.getEditText().setError(getString(R.string.empty_field));
            lipidNonHDLCholestrol.getEditText().requestFocus();
            error = true;
        } else if (lipidNonHDLCholestrol.getVisibility() == View.VISIBLE && !RegexUtil.isNumericForThreeDecimalPlaces(App.get(lipidNonHDLCholestrol), true)) {
            gotoLastPage();
            lipidNonHDLCholestrol.getEditText().setError(getString(R.string.comorbidities_upto_three_decimal_places_error));
            lipidNonHDLCholestrol.getEditText().requestFocus();
            error = true;
        } else if (lipidNonHDLCholestrol.getVisibility() == View.VISIBLE && !App.get(lipidNonHDLCholestrol).isEmpty() && (Double.parseDouble(App.get(lipidNonHDLCholestrol)) < 100 && Double.parseDouble(App.get(lipidNonHDLCholestrol)) > 500)) {
            gotoLastPage();
            lipidNonHDLCholestrol.getEditText().setError(getString(R.string.comorbidities_lipid_test_non_hdl_limit));
            lipidNonHDLCholestrol.getEditText().requestFocus();
            error = true;
        }

        if (lipidVLDL.getVisibility() == View.VISIBLE && App.get(lipidVLDL).isEmpty()) {
            gotoLastPage();
            lipidVLDL.getEditText().setError(getString(R.string.empty_field));
            lipidVLDL.getEditText().requestFocus();
            error = true;
        } else if (lipidVLDL.getVisibility() == View.VISIBLE && !RegexUtil.isNumericForThreeDecimalPlaces(App.get(lipidVLDL), true)) {
            gotoLastPage();
            lipidVLDL.getEditText().setError(getString(R.string.comorbidities_upto_three_decimal_places_error));
            lipidVLDL.getEditText().requestFocus();
            error = true;
        } else if (lipidVLDL.getVisibility() == View.VISIBLE && !App.get(lipidVLDL).isEmpty() && (Double.parseDouble(App.get(lipidVLDL)) < 1 && Double.parseDouble(App.get(lipidVLDL)) > 700)) {
            gotoLastPage();
            lipidVLDL.getEditText().setError(getString(R.string.comorbidities_lipid_test_vldl_limit));
            lipidVLDL.getEditText().requestFocus();
            error = true;
        }

        if (lipidLDL.getVisibility() == View.VISIBLE && App.get(lipidLDL).isEmpty()) {
            gotoLastPage();
            lipidLDL.getEditText().setError(getString(R.string.empty_field));
            lipidLDL.getEditText().requestFocus();
            error = true;
        } else if (lipidLDL.getVisibility() == View.VISIBLE && !RegexUtil.isNumericForThreeDecimalPlaces(App.get(lipidLDL), true)) {
            gotoLastPage();
            lipidLDL.getEditText().setError(getString(R.string.comorbidities_upto_three_decimal_places_error));
            lipidLDL.getEditText().requestFocus();
            error = true;
        } else if (lipidLDL.getVisibility() == View.VISIBLE && !App.get(lipidLDL).isEmpty() && (Double.parseDouble(App.get(lipidLDL)) < 20 && Double.parseDouble(App.get(lipidLDL)) > 400)) {
            gotoLastPage();
            lipidLDL.getEditText().setError(getString(R.string.comorbidities_lipid_test_ldl_limit));
            lipidLDL.getEditText().requestFocus();
            error = true;
        }

        if (lipidHDL.getVisibility() == View.VISIBLE && App.get(lipidHDL).isEmpty()) {
            gotoLastPage();
            lipidHDL.getEditText().setError(getString(R.string.empty_field));
            lipidHDL.getEditText().requestFocus();
            error = true;
        } else if (lipidHDL.getVisibility() == View.VISIBLE && !RegexUtil.isNumericForThreeDecimalPlaces(App.get(lipidHDL), true)) {
            gotoLastPage();
            lipidHDL.getEditText().setError(getString(R.string.comorbidities_upto_three_decimal_places_error));
            lipidHDL.getEditText().requestFocus();
            error = true;
        } else if (lipidHDL.getVisibility() == View.VISIBLE && !App.get(lipidHDL).isEmpty() && (Double.parseDouble(App.get(lipidHDL)) < 10 && Double.parseDouble(App.get(lipidHDL)) > 200)) {
            gotoLastPage();
            lipidHDL.getEditText().setError(getString(R.string.comorbidities_lipid_test_hdl_limit));
            lipidHDL.getEditText().requestFocus();
            error = true;
        }

        if (lipidTriglycerides.getVisibility() == View.VISIBLE && App.get(lipidTriglycerides).isEmpty()) {
            gotoLastPage();
            lipidTriglycerides.getEditText().setError(getString(R.string.empty_field));
            lipidTriglycerides.getEditText().requestFocus();
            error = true;
        } else if (lipidTriglycerides.getVisibility() == View.VISIBLE && !RegexUtil.isNumericForThreeDecimalPlaces(App.get(lipidTriglycerides), true)) {
            gotoLastPage();
            lipidTriglycerides.getEditText().setError(getString(R.string.comorbidities_upto_three_decimal_places_error));
            lipidTriglycerides.getEditText().requestFocus();
            error = true;
        } else if (lipidTriglycerides.getVisibility() == View.VISIBLE && !App.get(lipidTriglycerides).isEmpty() && (Double.parseDouble(App.get(lipidTriglycerides)) < 1 && Double.parseDouble(App.get(lipidTriglycerides)) > 700)) {
            gotoLastPage();
            lipidTriglycerides.getEditText().setError(getString(R.string.comorbidities_lipid_test_hdl_limit));
            lipidTriglycerides.getEditText().requestFocus();
            error = true;
        }

        if (lipidTotalCholestrol.getVisibility() == View.VISIBLE && App.get(lipidTotalCholestrol).isEmpty()) {
            gotoLastPage();
            lipidTotalCholestrol.getEditText().setError(getString(R.string.empty_field));
            lipidTotalCholestrol.getEditText().requestFocus();
            error = true;
        } else if (lipidTotalCholestrol.getVisibility() == View.VISIBLE && !RegexUtil.isNumericForThreeDecimalPlaces(App.get(lipidTotalCholestrol), true)) {
            gotoLastPage();
            lipidTotalCholestrol.getEditText().setError(getString(R.string.comorbidities_upto_three_decimal_places_error));
            lipidTotalCholestrol.getEditText().requestFocus();
            error = true;
        } else if (lipidTotalCholestrol.getVisibility() == View.VISIBLE && !App.get(lipidTotalCholestrol).isEmpty() && (Double.parseDouble(App.get(lipidTotalCholestrol)) < 100 && Double.parseDouble(App.get(lipidTotalCholestrol)) > 500)) {
            gotoLastPage();
            lipidTotalCholestrol.getEditText().setError(getString(R.string.comorbidities_lipid_test_total_cholestrol_limit));
            lipidTotalCholestrol.getEditText().requestFocus();
            error = true;
        }

        if (App.get(lipidTestID).isEmpty()) {
            gotoFirstPage();
            lipidTestID.getEditText().setError(getString(R.string.empty_field));
            lipidTestID.getEditText().requestFocus();
            error = true;
        } /*else if (!App.get(lipidTestID).isEmpty() && App.get(lipidTestID).length() < 9) {
            gotoFirstPage();
            lipidTestID.getEditText().setError(getString(R.string.comorbidities_hhba1c_testid_format_error1));
            lipidTestID.getEditText().requestFocus();
            error = true;
        } else if (!RegexUtil.isCorrectTestID(App.get(lipidTestID))) {
            gotoLastPage();
            lipidTestID.getEditText().setError(getString(R.string.comorbidities_hhba1c_testid_format_dasherror));
            lipidTestID.getEditText().requestFocus();
            error = true;
        }*/

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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
            }
            bundle.putBoolean("save", false);
        }

        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});
        observations.add(new String[]{"TEST ID", App.get(lipidTestID)});

        if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder))) {
            observations.add(new String[]{"FOLLOW-UP MONTH", App.get(lipidMonthOfVisit)});
            observations.add(new String[]{"DATE TEST ORDERED", App.getSqlDateTime(secondDateCalendar)});
        } else if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testresult))) {
            observations.add(new String[]{"TEST RESULT DATE", App.getSqlDateTime(thirdDateCalendar)});
            observations.add(new String[]{"TOTAL CHOLESTEROL", App.get(lipidTotalCholestrol)});
            observations.add(new String[]{"TRIGLYCERIDES", App.get(lipidTriglycerides)});
            observations.add(new String[]{"HIGH-DENSITY LIPOPROTEIN CHOLESTEROL", App.get(lipidHDL)});
            observations.add(new String[]{"LOW-DENSITY LIPOPROTEIN CHOLESTEROL", App.get(lipidLDL)});
            observations.add(new String[]{"VERY LOW DENSITY LIPOPROTEIN", App.get(lipidVLDL)});
            observations.add(new String[]{"NON HIGH-DENSITY LIPOPROTEIN CHOLESTEROL", App.get(lipidNonHDLCholestrol)});
            observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDateTime(fourthDateCalendar)});
        }

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
                if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder))) {
                    result = serverService.saveEncounterAndObservation("Lipid Test Order", FORM, formDateCalendar, observations.toArray(new String[][]{}), true);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                } else if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testresult))) {
                    result = serverService.saveEncounterAndObservation("Lipid Test Result", FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                }

                return result;
            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            ;

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();

                if (result.equals("SUCCESS")) {
                    resetViews();

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
                    alertDialog.setMessage(getResources().getString(R.string.insert_error));
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

        //Send NEXT Lipid Test to the database

        return true;
    }

    @Override
    public void refill(int encounterId) {

        OfflineForm fo = serverService.getOfflineFormById(encounterId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (fo.getFormName().contains("Order")) {
                formType.getRadioGroup().getButtons().get(0).setChecked(true);
                formType.getRadioGroup().getButtons().get(1).setEnabled(false);
                if (obs[0][0].equals("TEST ID")) {
                    lipidTestID.getEditText().setText(obs[0][1]);
                    lipidTestID.getEditText().setEnabled(false);
                    testIdView.setEnabled(false);
                    testIdView.setImageResource(R.drawable.ic_checked_green);
                    //checkTestId();
                } else if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                    lipidMonthOfVisit.getSpinner().selectValue(obs[0][1]);
                    lipidMonthOfVisit.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("DATE TEST ORDERED")) {
                    String secondDate = obs[0][1];
                    secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                    lipidTestOrderDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
                    lipidTestOrderDate.setVisibility(View.VISIBLE);
                }
                submitButton.setEnabled(true);
            } else {
                formType.getRadioGroup().getButtons().get(1).setChecked(true);
                formType.getRadioGroup().getButtons().get(0).setEnabled(false);
                if (obs[0][0].equals("TEST ID")) {
                    lipidTestID.getEditText().setText(obs[0][1]);
                    checkTestId();
                    lipidTestID.getEditText().setEnabled(false);
                    testIdView.setEnabled(false);
                } else if (obs[0][0].equals("TEST RESULT DATE")) {
                    String secondDate = obs[0][1];
                    thirdDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                    lipidTestResultDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
                } else if (obs[0][0].equals("TOTAL CHOLESTEROL")) {
                    lipidTotalCholestrol.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("TRIGLYCERIDES")) {
                    lipidTriglycerides.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("HIGH-DENSITY LIPOPROTEIN CHOLESTEROL")) {
                    lipidHDL.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("LOW-DENSITY LIPOPROTEIN CHOLESTEROL")) {
                    lipidLDL.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("VERY LOW DENSITY LIPOPROTEIN")) {
                    lipidVLDL.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("NON HIGH-DENSITY LIPOPROTEIN CHOLESTEROL")) {
                    lipidNonHDLCholestrol.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("RETURN VISIT DATE")) {
                    String secondDate = obs[0][1];
                    fourthDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                    nextLipidTestDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", fourthDateCalendar).toString());
                }
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
        } else if (view == lipidTestOrderDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        } else if (view == lipidTestResultDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");
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

        lipidTestID.getEditText().setEnabled(true);
        testIdView.setEnabled(true);
        formType.getRadioGroup().getButtons().get(0).setEnabled(true);
        formType.getRadioGroup().getButtons().get(1).setEnabled(true);

        thirdDateCalendar = Calendar.getInstance();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        lipidTestOrderDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        lipidTestResultDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
        fourthDateCalendar.set(Calendar.YEAR, secondDateCalendar.get(Calendar.YEAR));
        fourthDateCalendar.set(Calendar.MONTH, secondDateCalendar.get(Calendar.MONTH));
        fourthDateCalendar.set(Calendar.DAY_OF_MONTH, secondDateCalendar.get(Calendar.DAY_OF_MONTH));
        fourthDateCalendar.add(Calendar.MONTH, 2);
        fourthDateCalendar.add(Calendar.DAY_OF_MONTH, 20);
        nextLipidTestDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", fourthDateCalendar).toString());

        submitButton.setEnabled(false);

        testIdView.setVisibility(View.GONE);
        lipidTestID.setVisibility(View.GONE);
        testIdView.setImageResource(R.drawable.ic_checked);

        goneVisibility();

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
        if (radioGroup == formType.getRadioGroup()) {
            //showTestOrderOrTestResult();
            formDate.setVisibility(View.VISIBLE);
            lipidTestID.setVisibility(View.VISIBLE);
            lipidTestID.getEditText().setText("");
            lipidTestID.getEditText().setError(null);
            goneVisibility();
            submitButton.setEnabled(false);
        }
    }

    void goneVisibility() {
        testOrderLipid.setVisibility(View.GONE);
        lipidMonthOfVisit.setVisibility(View.GONE);
        lipidTestOrderDate.setVisibility(View.GONE);

        //second page views...
        testResultLipid.setVisibility(View.GONE);
        lipidTestResultDate.setVisibility(View.GONE);
        lipidTotalCholestrol.setVisibility(View.GONE);
        lipidTriglycerides.setVisibility(View.GONE);
        lipidHDL.setVisibility(View.GONE);
        lipidLDL.setVisibility(View.GONE);
        lipidVLDL.setVisibility(View.GONE);
        lipidNonHDLCholestrol.setVisibility(View.GONE);
        nextLipidTestDate.setVisibility(View.GONE);

    }

    void showTestOrderOrTestResult() {
        if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder))) {
            testOrderLipid.setVisibility(View.VISIBLE);
            lipidMonthOfVisit.setVisibility(View.VISIBLE);
            lipidTestOrderDate.setVisibility(View.VISIBLE);

            //second page views...
            testResultLipid.setVisibility(View.GONE);
            lipidTestResultDate.setVisibility(View.GONE);
            lipidTotalCholestrol.setVisibility(View.GONE);
            lipidTriglycerides.setVisibility(View.GONE);
            lipidHDL.setVisibility(View.GONE);
            lipidLDL.setVisibility(View.GONE);
            lipidVLDL.setVisibility(View.GONE);
            lipidNonHDLCholestrol.setVisibility(View.GONE);
            nextLipidTestDate.setVisibility(View.GONE);

        } else {
            testOrderLipid.setVisibility(View.GONE);
            lipidMonthOfVisit.setVisibility(View.GONE);
            lipidTestOrderDate.setVisibility(View.GONE);

            //second page views...
            testResultLipid.setVisibility(View.VISIBLE);
            lipidTestResultDate.setVisibility(View.VISIBLE);
            lipidTotalCholestrol.setVisibility(View.VISIBLE);
            lipidTriglycerides.setVisibility(View.VISIBLE);
            lipidHDL.setVisibility(View.VISIBLE);
            lipidLDL.setVisibility(View.VISIBLE);
            lipidVLDL.setVisibility(View.VISIBLE);
            lipidNonHDLCholestrol.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageView view = (ImageView) v;
                //overlay is black with transparency of 0x77 (119)
                view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                view.invalidate();

                Boolean error = false;

                checkTestId();


                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                ImageView view = (ImageView) v;
                //clear the overlay
                view.getDrawable().clearColorFilter();
                view.invalidate();
                break;
            }
        }
        return true;
    }

    private void checkTestId() {
        AsyncTask<String, String, String> submissionFormTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.verifying_test_id));
                        loading.show();
                    }
                });

                String result = "";

                Object[][] testIds = serverService.getTestIdByPatientAndEncounterType(App.getPatientId(), "Comorbidities-Lipid Test Order");

                Log.d("TEST_IDS_L", Arrays.deepToString(testIds));

                if (testIds == null || testIds.length < 1) {
                    if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder)))
                        return "SUCCESS";
                    else
                        return "";
                }

                if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder))) {
                    result = "SUCCESS";
                    for (int i = 0; i < testIds.length; i++) {
                        if (String.valueOf(testIds[i][0]).equals(App.get(lipidTestID))) {
                            return "";
                        }
                    }
                }

                if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testresult))) {
                    result = "";
                    for (int i = 0; i < testIds.length; i++) {
                        if (String.valueOf(testIds[i][0]).equals(App.get(lipidTestID))) {
                            return "SUCCESS";
                        }
                    }
                }

                return result;
            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            ;

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();

                if (result.equals("SUCCESS")) {

                    testIdView.setImageResource(R.drawable.ic_checked_green);
                    showTestOrderOrTestResult();
                    submitButton.setEnabled(true);

                } else {

                    if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder))) {
                        lipidTestID.getEditText().setError("Test Id already used.");
                    } else {
                        lipidTestID.getEditText().setError("No order form found for the test id for patient");
                    }

                }

                try {
                    InputMethodManager imm = (InputMethodManager) mainContent.getContext().getSystemService(mainContent.getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        };
        submissionFormTask.execute("");

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
            if (getArguments().getInt("type") == DATE_DIALOG_ID)
                calendar = formDateCalendar;
            else if (getArguments().getInt("type") == SECOND_DATE_DIALOG_ID)
                calendar = secondDateCalendar;
            else if (getArguments().getInt("type") == THIRD_DATE_DIALOG_ID)
                calendar = thirdDateCalendar;
            else
                return null;

            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setTag(getArguments().getInt("type"));
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            if (((int) view.getTag()) == DATE_DIALOG_ID)
                formDateCalendar.set(yy, mm, dd);
            else if (((int) view.getTag()) == SECOND_DATE_DIALOG_ID)
                secondDateCalendar.set(yy, mm, dd);
            else if (((int) view.getTag()) == THIRD_DATE_DIALOG_ID)
                thirdDateCalendar.set(yy, mm, dd);

            updateDisplay();
        }
    }

}




