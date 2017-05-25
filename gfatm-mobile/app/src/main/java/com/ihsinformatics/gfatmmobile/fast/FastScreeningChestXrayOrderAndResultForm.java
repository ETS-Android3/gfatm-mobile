package com.ihsinformatics.gfatmmobile.fast;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Haris on 1/24/2017.
 */

public class FastScreeningChestXrayOrderAndResultForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener, View.OnTouchListener {
    Context context;
    boolean isResultForm = false;
    boolean beforeResult = false;
    boolean changeDate = false;
    String finalDate = null;

    // Views...
    TitledButton formDate;
    TitledRadioGroup pastXray;
    TitledRadioGroup pregnancyHistory;
    MyTextView cxrResultTitle;
    MyTextView cxrOrderTitle;
    TitledRadioGroup screenXrayType;
    TitledRadioGroup formType;
    TitledSpinner monthOfTreatment;
    //  TitledButton testDate;
    TitledEditText testId;
    TitledEditText cat4tbScore;
    TitledRadioGroup radiologicalDiagnosis;
    TitledCheckBoxes abnormalDetailedDiagnosis;
    TitledEditText abnormalDetailedDiagnosisOther;
    TitledRadioGroup extentOfDisease;
    TitledEditText radiologistRemarks;
    ImageView testIdView;
    LinearLayout linearLayout;


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
        FORM_NAME = Forms.FAST_SCREENING_CHEST_XRAY_ORDER_AND_RESULT_FORM;
        FORM = Forms.fastScreeningChestXrayOrderAndResultForm;

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
        pastXray = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_past_xray), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        pregnancyHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_pregnancy_history), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        testId = new TitledEditText(context, null, getResources().getString(R.string.fast_test_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_select_form_type), getResources().getStringArray(R.array.fast_order_and_result_list), "", App.HORIZONTAL, App.HORIZONTAL);
        cxrOrderTitle = new MyTextView(context, getResources().getString(R.string.fast_cxr_order_title));
        cxrOrderTitle.setTypeface(null, Typeface.BOLD);
        screenXrayType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_what_type_of_xray_is_this), getResources().getStringArray(R.array.fast_type_of_xray_is_this_list), getResources().getString(R.string.fast_chest_xray_other), App.VERTICAL, App.VERTICAL);
        monthOfTreatment = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_month_of_treatment), getResources().getStringArray(R.array.fast_number_list), "", App.VERTICAL);
        updateFollowUpMonth();
        //   testDate = new TitledButton(context, null, getResources().getString(R.string.fast_test_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        cxrResultTitle = new MyTextView(context, getResources().getString(R.string.fast_cxr_result_title));
        cxrResultTitle.setTypeface(null, Typeface.BOLD);
        cat4tbScore = new TitledEditText(context, null, getResources().getString(R.string.fast_chest_xray_cad4tb_score), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);
        radiologicalDiagnosis = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_radiologica_diagnosis), getResources().getStringArray(R.array.fast_radiological_diagonosis_list), getResources().getString(R.string.fast_normal), App.VERTICAL, App.VERTICAL);
        abnormalDetailedDiagnosis = new TitledCheckBoxes(context, null, getResources().getString(R.string.fast_if_abnormal_detailed_diagnosis), getResources().getStringArray(R.array.fast_abnormal_detailed_diagnosis_list), new Boolean[]{true, false, false, false, false, false, false}, App.VERTICAL, App.VERTICAL, false);
        abnormalDetailedDiagnosisOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        extentOfDisease = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_extent_of_desease), getResources().getStringArray(R.array.fast_extent_of_disease_list), getResources().getString(R.string.fast_normal), App.VERTICAL, App.VERTICAL);
        radiologistRemarks = new TitledEditText(context, null, getResources().getString(R.string.fast_radiologist_remarks), "", "", 500, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.9f
        );
        testId.setLayoutParams(param);
        linearLayout.addView(testId);
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


        // Used for reset fields...
        views = new View[]{formDate.getButton(), formType.getRadioGroup(), testId.getEditText(), screenXrayType.getRadioGroup(),
                monthOfTreatment.getSpinner(), radiologicalDiagnosis.getRadioGroup(), cat4tbScore.getEditText(), abnormalDetailedDiagnosis,
                abnormalDetailedDiagnosisOther.getEditText(), extentOfDisease.getRadioGroup(), radiologistRemarks.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formType, formDate, pastXray, pregnancyHistory, linearLayout, cxrOrderTitle, screenXrayType, monthOfTreatment, cxrResultTitle, cat4tbScore, radiologicalDiagnosis,
                        abnormalDetailedDiagnosis, abnormalDetailedDiagnosisOther, extentOfDisease, radiologistRemarks}};

        formDate.getButton().setOnClickListener(this);
        // testDate.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        radiologicalDiagnosis.getRadioGroup().setOnCheckedChangeListener(this);
        pastXray.getRadioGroup().setOnCheckedChangeListener(this);
        pregnancyHistory.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : abnormalDetailedDiagnosis.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        testId.getEditText().addTextChangedListener(new TextWatcher() {

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
                isResultForm = false;
                try {
                    if (testId.getEditText().getText().length() > 0) {
                        testIdView.setVisibility(View.VISIBLE);
                        testIdView.setImageResource(R.drawable.ic_checked);
                    } else {
                        testId.getEditText().setError(getString(R.string.fast_test_id_error));
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
        Calendar treatDateCalender = null;
        if (snackbar != null)
            snackbar.dismiss();

        if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_result))) {
            if (beforeResult) {
                Object[][] testIds = serverService.getTestIdByPatientAndEncounterType(App.getPatientId(), "FAST-Screening CXR Test Order");
                String format = "";
                String formDa = formDate.getButton().getText().toString();

                for (int i = 0; i < testIds.length; i++) {
                    if (testIds[i][0].equals(testId.getEditText().getText().toString())) {
                        String date = testIds[i][1].toString();
                        if (date.contains("/")) {
                            format = "dd/MM/yyyy";
                        } else {
                            format = "yyyy-MM-dd";
                        }

                        Date orderDate = App.stringToDate(date, format);
                        Date orderDateForValidation = App.stringToDate(date, format);

                        Calendar dateCalendar = Calendar.getInstance();
                        dateCalendar.setTime(orderDateForValidation);
                       // dateCalendar.add(Calendar.DATE, 1);
                        SimpleDateFormat newFormat = new SimpleDateFormat("EEEE, MMM dd,yyyy");
                        finalDate = newFormat.format(dateCalendar.getTime());

                        if (formDateCalendar.before(App.getCalendar(orderDate))) {
                            //formDateCalendar = App.getCalendar(App.stringToDate(finalDate, "EEEE, MMM dd,yyyy"));
                            changeDate = true;

                            break;
                        } else {
                            changeDate = false;
                            if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

                                String personDOB = App.getPatient().getPerson().getBirthdate();

                                Date date1 = new Date();
                                if (formDateCalendar.after(App.getCalendar(date1))) {
                                    changeDate = false;
                                    formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                                    snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                                    snackbar.show();

                                    formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                                    break;

                                } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                                    changeDate = false;
                                    formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                                    snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                                    TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                                    tv.setMaxLines(2);
                                    snackbar.show();
                                    formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                                    break;
                                } else{
                                    changeDate = false;
                                    formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                                }
                            }
                        }
                    }
                }
            } else if (isResultForm) {
                changeDate = false;
                Object[][] testIds = serverService.getTestIdByPatientAndEncounterType(App.getPatientId(), "FAST-Screening CXR Test Order");
                String format = "";
                String formDa = formDate.getButton().getText().toString();

                for (int i = 0; i < testIds.length; i++) {
                    if (testIds[i][0].equals(testId.getEditText().getText().toString())) {
                        String date = testIds[i][1].toString();
                        if (date.contains("/")) {
                            format = "dd/MM/yyyy";
                        } else {
                            format = "yyyy-MM-dd";
                        }

                        Date orderDate = App.stringToDate(date, format);

                        if (formDateCalendar.before(App.getCalendar(orderDate))) {
                            formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                            snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_result_date_cannot_be_before_order_date), Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();

                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                            break;
                        } else {
                            if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

                                String personDOB = App.getPatient().getPerson().getBirthdate();

                                Date date1 = new Date();
                                if (formDateCalendar.after(App.getCalendar(date1))) {

                                    formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                                    snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                                    snackbar.show();

                                    formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                                    break;

                                } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                                    formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                                    snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                                    TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                                    tv.setMaxLines(2);
                                    snackbar.show();
                                    formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                                    break;
                                } else
                                    formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                            }
                        }
                    }
                }
            }
        }
        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();


            String treatmentDate = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "REGISTRATION DATE");

            if (treatmentDate != null) {
                treatDateCalender = App.getCalendar(App.stringToDate(treatmentDate, "yyyy-MM-dd"));
            }

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {
                changeDate = false;
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                changeDate = false;
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyyy", formDateCalendar).toString());
            } else if (treatDateCalender != null) {
                if (formDateCalendar.before(treatDateCalender)) {
                    changeDate = false;
                    formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                    snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_cannot_be_before_treatment_initiation_form), Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();

                    formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                } else {
                    changeDate = false;
                    formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                }
            } else{
                changeDate = false;
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
             }
        }

        /*if (!(testDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = testDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                testDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                testDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                testDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }*/

        updateFollowUpMonth();
        formDate.getButton().setEnabled(true);
    }

    public void updateFollowUpMonth() {

        String treatmentDate = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "REGISTRATION DATE");
        String format = "";
        String[] monthArray;

        if (treatmentDate == null) {
            monthArray = new String[1];
            monthArray[0] = "0";
            monthOfTreatment.getSpinner().setSpinnerData(monthArray);
        } else {
            if (treatmentDate.contains("/")) {
                format = "dd/MM/yyyy";
            } else {
                format = "yyyy-MM-dd";
            }
            Date convertedDate = App.stringToDate(treatmentDate, format);
            Calendar treatmentDateCalender = App.getCalendar(convertedDate);
            int diffYear = formDateCalendar.get(Calendar.YEAR) - treatmentDateCalender.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + formDateCalendar.get(Calendar.MONTH) - treatmentDateCalender.get(Calendar.MONTH);

            if (diffMonth == 0) {
                monthArray = new String[1];
                monthArray[0] = "1";
                monthOfTreatment.getSpinner().setSpinnerData(monthArray);
            } else if (diffMonth > 24) {
                monthArray = new String[24];
                for (int i = 0; i < 24; i++) {
                    monthArray[i] = String.valueOf(i + 1);
                }
                monthOfTreatment.getSpinner().setSpinnerData(monthArray);
            } else {
                monthArray = new String[diffMonth];
                for (int i = 0; i < diffMonth; i++) {
                    monthArray[i] = String.valueOf(i + 1);
                }
                monthOfTreatment.getSpinner().setSpinnerData(monthArray);
            }
        }
    }

    @Override
    public boolean validate() {
        Boolean error = false;
        Boolean formCheck = false;

        if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order)) || formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_result))) {

        } else {
            formCheck = true;
            error = true;
        }


        if (testId.getVisibility() == View.VISIBLE && App.get(testId).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            testId.getEditText().setError(getString(R.string.empty_field));
            testId.getEditText().requestFocus();
            error = true;
        }

      /*  if (cat4tbScore.getVisibility() == View.VISIBLE && cat4tbScore.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cat4tbScore.getEditText().setError(getString(R.string.empty_field));
            cat4tbScore.getEditText().requestFocus();
            error = true;
        }*/

        if (abnormalDetailedDiagnosisOther.getVisibility() == View.VISIBLE && abnormalDetailedDiagnosisOther.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            abnormalDetailedDiagnosisOther.getEditText().setError(getString(R.string.empty_field));
            abnormalDetailedDiagnosisOther.getEditText().requestFocus();
            error = true;
        }

        if (radiologistRemarks.getVisibility() == View.VISIBLE && App.get(radiologistRemarks).length() > 0 && radiologistRemarks.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            radiologistRemarks.getEditText().setError(getString(R.string.empty_field));
            radiologistRemarks.getEditText().requestFocus();
            error = true;
        }


        if (error) {
            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            if (formCheck) {
                alertDialog.setMessage(getString(R.string.fast_please_select_form_type));
            } else {
                alertDialog.setMessage(getString(R.string.form_error));
            }
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

        observations.add(new String[]{"TEST ID", App.get(testId)});

        if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))) {
            if (pastXray.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"X RAY IN PAST 6 MONTHS", App.get(pastXray).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});

            if (pregnancyHistory.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"PREGNANCY STATUS", App.get(pregnancyHistory).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});


            if (screenXrayType.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"TYPE OF X RAY", App.get(screenXrayType).equals(getResources().getString(R.string.fast_chest_xray_cad4tb)) ? "RADIOLOGICAL DIAGNOSIS" : "X-RAY, OTHER"});
            if (monthOfTreatment.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"FOLLOW-UP MONTH", monthOfTreatment.getSpinner().getSelectedItem().toString()});
            if (formDate.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"DATE TEST ORDERED", App.getSqlDateTime(formDateCalendar)});
        } else {
            observations.add(new String[]{"DATE OF  TEST RESULT RECEIVED", App.getSqlDateTime(formDateCalendar)});
            if (cat4tbScore.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"CHEST X-RAY SCORE", App.get(cat4tbScore)});
            }

            if (radiologicalDiagnosis.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"RADIOLOGICAL DIAGNOSIS", App.get(radiologicalDiagnosis).equals(getResources().getString(R.string.fast_normal)) ? "NORMAL" :
                        (App.get(radiologicalDiagnosis).equals(getResources().getString(R.string.fast_abnormal_suggestive_of_tb)) ? "ABNORMAL SUGGESTIVE OF TB" : "ABNORMAL NOT SUGGESTIVE OF TB")});


            if (abnormalDetailedDiagnosis.getVisibility() == View.VISIBLE) {

                String abnormalDetailedDiagnosisString = "";
                for (CheckBox cb : abnormalDetailedDiagnosis.getCheckedBoxes()) {
                    if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.fast_adenopathy)))
                        abnormalDetailedDiagnosisString = abnormalDetailedDiagnosisString + "ADENOPATHY" + " ; ";
                    else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.fast_infiltration)))
                        abnormalDetailedDiagnosisString = abnormalDetailedDiagnosisString + "INFILTRATE" + " ; ";
                    else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.fast_consolidation)))
                        abnormalDetailedDiagnosisString = abnormalDetailedDiagnosisString + "CONSOLIDATION" + " ; ";
                    else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.fast_pleural_effusion)))
                        abnormalDetailedDiagnosisString = abnormalDetailedDiagnosisString + "PLEURAL EFFUSION" + " ; ";
                    else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.fast_cavitation)))
                        abnormalDetailedDiagnosisString = abnormalDetailedDiagnosisString + "CAVIATION" + " ; ";
                    else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.fast_miliary_tb)))
                        abnormalDetailedDiagnosisString = abnormalDetailedDiagnosisString + "MILIARY" + " ; ";
                    else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.fast_others)))
                        abnormalDetailedDiagnosisString = abnormalDetailedDiagnosisString + "OTHER ABNORMAL DETAILED DIAGNOSIS" + " ; ";
                }
                observations.add(new String[]{"ABNORMAL DETAILED DIAGNOSIS", abnormalDetailedDiagnosisString});
            }


            if (abnormalDetailedDiagnosisOther.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"OTHER ABNORMAL DETAILED DIAGNOSIS", App.get(abnormalDetailedDiagnosisOther)});
            }
            observations.add(new String[]{"EXTENT OF DISEASE", App.get(extentOfDisease).equals(getResources().getString(R.string.fast_normal)) ? "NORMAL" :
                    (App.get(extentOfDisease).equals(getResources().getString(R.string.fast_unilateral_disease)) ? "UNILATERAL" :
                            (App.get(extentOfDisease).equals(getResources().getString(R.string.fast_bilateral_disease)) ? "BILATERAL" : "ABNORMAL"))});
            if (!App.get(radiologistRemarks).isEmpty()) {
                observations.add(new String[]{"CLINICIAN NOTES (TEXT)", App.get(radiologistRemarks)});
            }
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

                if (App.get(formType).equals(getResources().getString(R.string.fast_order))) {
                    result = serverService.saveEncounterAndObservation("Screening CXR Test Order", FORM, formDateCalendar, observations.toArray(new String[][]{}), true);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                } else if (App.get(formType).equals(getResources().getString(R.string.fast_result))) {
                    result = serverService.saveEncounterAndObservation("Screening CXR Test Result", FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                }

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

      /*  formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        formValues.put(lastName.getTag(), App.get(lastName));
        formValues.put(husbandName.getTag(), App.get(husbandName));
        formValues.put(gender.getTag(), App.get(gender));

        serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);*/

        return true;
    }


    public boolean validateResultDate() {
        updateDisplay();
        return changeDate;
    }


    void showTestOrderOrTestResult() {
        //formDate.setVisibility(View.VISIBLE);
        if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))) {
            isResultForm = false;
            beforeResult = false;
            cxrOrderTitle.setVisibility(View.VISIBLE);
           // formDate.getQuestionView().setText(getResources().getString(R.string.fast_test_date));
            screenXrayType.setVisibility(View.VISIBLE);
            monthOfTreatment.setVisibility(View.VISIBLE);
            //   testDate.setVisibility(View.VISIBLE);

            cxrResultTitle.setVisibility(View.GONE);
            cat4tbScore.setVisibility(View.GONE);
            radiologicalDiagnosis.setVisibility(View.GONE);
            abnormalDetailedDiagnosis.setVisibility(View.GONE);
            abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
            extentOfDisease.setVisibility(View.GONE);
            radiologistRemarks.setVisibility(View.GONE);
        } else {
            cxrOrderTitle.setVisibility(View.GONE);
           // formDate.getQuestionView().setText(getResources().getString(R.string.fast_date_of_result_recieved));
            isResultForm = true;
            beforeResult = false;
         /*   String typeofXray = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Screening CXR Test Order", "TYPE OF X RAY");

            if(typeofXray == null){
                cat4tbScore.setVisibility(View.GONE);
            }
            else if(typeofXray.equalsIgnoreCase("RADIOLOGICAL DIAGNOSIS")){
                cat4tbScore.setVisibility(View.VISIBLE);
            }
            else{
                cat4tbScore.setVisibility(View.GONE);
            }*/

            screenXrayType.setVisibility(View.GONE);
            monthOfTreatment.setVisibility(View.GONE);
            //  testDate.setVisibility(View.GONE);

            cxrResultTitle.setVisibility(View.VISIBLE);

            radiologicalDiagnosis.setVisibility(View.VISIBLE);

            if (radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_suggestive_of_tb)) || radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_not_suggestive_of_tb))) {
                abnormalDetailedDiagnosis.setVisibility(View.VISIBLE);
                for (CheckBox cb : abnormalDetailedDiagnosis.getCheckedBoxes()) {
                    if (App.get(cb).equals(getResources().getString(R.string.fast_others))) {
                        if (cb.isChecked()) {
                            abnormalDetailedDiagnosisOther.setVisibility(View.VISIBLE);
                        } else {
                            abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
                        }
                    }
                }
            }
            cat4tbScore.setVisibility(View.VISIBLE);
            extentOfDisease.setVisibility(View.VISIBLE);
            radiologistRemarks.setVisibility(View.VISIBLE);
        }
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

                Object[][] testIds = serverService.getTestIdByPatientAndEncounterType(App.getPatientId(), "FAST-Screening CXR Test Order");

                if (testIds == null || testIds.length < 1) {
                    if (App.get(formType).equals(getResources().getString(R.string.fast_order)))
                        return "SUCCESS";
                    else
                        return "";
                }


                if (App.get(formType).equals(getResources().getString(R.string.fast_order))) {
                    result = "SUCCESS";
                    for (int i = 0; i < testIds.length; i++) {
                        if (String.valueOf(testIds[i][0]).equals(App.get(testId))) {
                            return "";
                        }
                    }
                }

                if (App.get(formType).equals(getResources().getString(R.string.fast_result))) {
                    result = "";
                    for (int i = 0; i < testIds.length; i++) {
                        if (String.valueOf(testIds[i][0]).equals(App.get(testId))) {
                            if(!isResultForm)
                                beforeResult = true;
                            else
                                beforeResult = false;
                            if (!validateResultDate())
                                return "SUCCESS";
                            return "FAIL";
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
                    testIdView.setTag(R.drawable.ic_checked_green);
                    showTestOrderOrTestResult();
                    submitButton.setEnabled(true);

                }

                else if(result.equals("FAIL")){
                    if (snackbar != null)
                        snackbar.dismiss();

                    snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_result_date_cannot_be_before_order_date), Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                    formDateCalendar = App.getCalendar(App.stringToDate(finalDate, "EEEE, MMM dd,yyyy"));
                    formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                }


                else {

                    if (App.get(formType).equals(getResources().getString(R.string.fast_order))) {
                        testId.getEditText().setError("Test Id already used.");
                    } else {
                        testId.getEditText().setError("No order form found for the test id for patient");
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

    @Override
    public void refill(int encounterId) {

        OfflineForm fo = serverService.getOfflineFormById(encounterId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();

        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());


        for (int i = 0; i < obsValue.size(); i++) {
            formDate.setVisibility(View.VISIBLE);
            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            }
            if (fo.getFormName().contains("Order")) {
                formType.getRadioGroup().getButtons().get(0).setChecked(true);
                formType.getRadioGroup().getButtons().get(1).setEnabled(false);
                testIdView.setImageResource(R.drawable.ic_checked_green);
                if (obs[0][0].equals("TEST ID")) {
                    testId.getEditText().setEnabled(false);
                    testIdView.setEnabled(false);
                    testIdView.setImageResource(R.drawable.ic_checked_green);
                    testId.getEditText().setText(obs[0][1]);
                }

                else if (obs[0][0].equals("X RAY IN PAST 6 MONTHS")) {
                    for (RadioButton rb : pastXray.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                            testId.getEditText().setEnabled(true);
                            testIdView.setEnabled(true);
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    pastXray.setVisibility(View.VISIBLE);
                }

                else if (obs[0][0].equals("PREGNANCY STATUS")) {
                    for (RadioButton rb : pregnancyHistory.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                            testId.getEditText().setEnabled(true);
                            testIdView.setEnabled(true);
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    pregnancyHistory.setVisibility(View.VISIBLE);
                }

                else if (obs[0][0].equals("TYPE OF X RAY")) {
                    for (RadioButton rb : screenXrayType.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_chest_xray_cad4tb)) && obs[0][1].equals("RADIOLOGICAL DIAGNOSIS")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_chest_xray_other)) && obs[0][1].equals("X-RAY, OTHER")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    screenXrayType.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                    monthOfTreatment.getSpinner().selectValue(obs[0][1]);
                    monthOfTreatment.setVisibility(View.VISIBLE);
                }
                submitButton.setEnabled(true);
            } else {
                formType.getRadioGroup().getButtons().get(1).setChecked(true);
                formType.getRadioGroup().getButtons().get(0).setEnabled(false);
                if (obs[0][0].equals("TEST ID")) {
                    testId.getEditText().setText(obs[0][1]);
                    testId.getEditText().setEnabled(false);
                    testIdView.setEnabled(false);
                    testIdView.setImageResource(R.drawable.ic_checked);
                    checkTestId();
                } else if (obs[0][0].equals("CHEST X-RAY SCORE")) {
                    cat4tbScore.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("RADIOLOGICAL DIAGNOSIS")) {
                    for (RadioButton rb : radiologicalDiagnosis.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_normal)) && obs[0][1].equals("NORMAL")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_abnormal_suggestive_of_tb)) && obs[0][1].equals("ABNORMAL SUGGESTIVE OF TB")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_abnormal_not_suggestive_of_tb)) && obs[0][1].equals("ABNORMAL NOT SUGGESTIVE OF TB")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    radiologicalDiagnosis.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("ABNORMAL DETAILED DIAGNOSIS")) {
                    for (CheckBox cb : abnormalDetailedDiagnosis.getCheckedBoxes()) {
                        if (cb.getText().equals(getResources().getString(R.string.fast_adenopathy)) && obs[0][1].equals("ADENOPATHY")) {
                            cb.setChecked(true);
                            break;
                        } else if (cb.getText().equals(getResources().getString(R.string.fast_infiltration)) && obs[0][1].equals("INFILTRATE")) {
                            cb.setChecked(true);
                            break;
                        } else if (cb.getText().equals(getResources().getString(R.string.fast_consolidation)) && obs[0][1].equals("CONSOLIDATION")) {
                            cb.setChecked(true);
                            break;
                        } else if (cb.getText().equals(getResources().getString(R.string.fast_pleural_effusion)) && obs[0][1].equals("PLEURAL EFFUSION")) {
                            cb.setChecked(true);
                            break;
                        } else if (cb.getText().equals(getResources().getString(R.string.fast_cavitation)) && obs[0][1].equals("CAVIATION")) {
                            cb.setChecked(true);
                            break;
                        } else if (cb.getText().equals(getResources().getString(R.string.fast_miliary_tb)) && obs[0][1].equals("MILIARY")) {
                            cb.setChecked(true);
                            break;
                        } else if (cb.getText().equals(getResources().getString(R.string.fast_others)) && obs[0][1].equals("OTHER ABNORMAL DETAILED DIAGNOSIS")) {
                            cb.setChecked(true);
                            break;
                        }
                    }
                    abnormalDetailedDiagnosis.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("OTHER ABNORMAL DETAILED DIAGNOSIS")) {
                    abnormalDetailedDiagnosisOther.getEditText().setText(obs[0][1]);
                    abnormalDetailedDiagnosisOther.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("EXTENT OF DISEASE")) {
                    for (RadioButton rb : extentOfDisease.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_normal)) && obs[0][1].equals("NORMAL")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_unilateral_disease)) && obs[0][1].equals("UNILATERAL")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_bilateral_disease)) && obs[0][1].equals("BILATERAL")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_abnormal_but_extent_not_defined)) && obs[0][1].equals("ABNORMAL")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    extentOfDisease.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
                    radiologistRemarks.getEditText().setText(obs[0][1]);
                }
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

       /* if (view == testDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
        }*/
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        for (CheckBox cb : abnormalDetailedDiagnosis.getCheckedBoxes()) {
            if (App.get(cb).equals(getResources().getString(R.string.fast_others))) {
                if (cb.isChecked()) {
                    abnormalDetailedDiagnosisOther.setVisibility(View.VISIBLE);
                } else {
                    abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        formDate.setVisibility(View.GONE);
        pastXray.setVisibility(View.GONE);
        pregnancyHistory.setVisibility(View.GONE);

        testIdView.setVisibility(View.GONE);
        testId.setVisibility(View.GONE);
        testIdView.setImageResource(R.drawable.ic_checked);
        goneVisibility();
        submitButton.setEnabled(false);

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

    void goneVisibility() {
        // formDate.setVisibility(View.GONE);
        cxrOrderTitle.setVisibility(View.GONE);
        screenXrayType.setVisibility(View.GONE);
        monthOfTreatment.setVisibility(View.GONE);
        // testDate.setVisibility(View.GONE);
        cxrResultTitle.setVisibility(View.GONE);
        cat4tbScore.setVisibility(View.GONE);
        radiologicalDiagnosis.setVisibility(View.GONE);
        abnormalDetailedDiagnosis.setVisibility(View.GONE);
        abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
        extentOfDisease.setVisibility(View.GONE);
        radiologistRemarks.setVisibility(View.GONE);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == formType.getRadioGroup()) {

            if(formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))){
                formDate.setVisibility(View.VISIBLE);
                pastXray.setVisibility(View.VISIBLE);
                if(App.getPatient().getPerson().getGender().equals("female") || App.getPatient().getPerson().getGender().equals("F")){
                    pregnancyHistory.setVisibility(View.VISIBLE);
                }
                if(pastXray.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))
                        && pregnancyHistory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))){

                    formDate.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    testId.setVisibility(View.VISIBLE);
                    testId.getEditText().setText("");
                    testId.getEditText().setError(null);
                    goneVisibility();
                    submitButton.setEnabled(false);
                }
                else{
                    linearLayout.setVisibility(View.GONE);
                    testId.setVisibility(View.GONE);
                    cxrOrderTitle.setVisibility(View.GONE);
                    screenXrayType.setVisibility(View.GONE);
                    monthOfTreatment.setVisibility(View.GONE);
                    submitButton.setEnabled(true);
                }

                if(pregnancyHistory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))
                        && pastXray.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))){
                    formDate.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    testId.setVisibility(View.VISIBLE);
                    testId.getEditText().setText("");
                    testId.getEditText().setError(null);
                    goneVisibility();
                    submitButton.setEnabled(false);
                }
                else{
                    linearLayout.setVisibility(View.GONE);
                    testId.setVisibility(View.GONE);
                    cxrOrderTitle.setVisibility(View.GONE);
                    screenXrayType.setVisibility(View.GONE);
                    monthOfTreatment.setVisibility(View.GONE);
                    submitButton.setEnabled(true);
                }

            }

            else {
                formDate.setVisibility(View.VISIBLE);
                pastXray.setVisibility(View.GONE);
                pregnancyHistory.setVisibility(View.GONE);
                testId.setVisibility(View.VISIBLE);
                testId.getEditText().setText("");
                testId.getEditText().setError(null);
                goneVisibility();
                submitButton.setEnabled(false);
            }
        } else if (radioGroup == radiologicalDiagnosis.getRadioGroup()) {
            if (radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_suggestive_of_tb)) || radiologicalDiagnosis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_abnormal_not_suggestive_of_tb))) {
                abnormalDetailedDiagnosis.setVisibility(View.VISIBLE);
                for (CheckBox cb : abnormalDetailedDiagnosis.getCheckedBoxes()) {
                    if (App.get(cb).equals(getResources().getString(R.string.fast_others))) {
                        if (cb.isChecked()) {
                            abnormalDetailedDiagnosisOther.setVisibility(View.VISIBLE);
                        } else {
                            abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
                        }
                    }
                }
            } else {
                abnormalDetailedDiagnosis.setVisibility(View.GONE);
                abnormalDetailedDiagnosisOther.setVisibility(View.GONE);
            }
        }

        else if(radioGroup == pastXray.getRadioGroup()){
            if(pastXray.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))
                    && pregnancyHistory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title)) && pregnancyHistory.getVisibility() == View.VISIBLE){
                formDate.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                testId.setVisibility(View.VISIBLE);
                testId.getEditText().setText("");
                testId.getEditText().setError(null);
                goneVisibility();
                submitButton.setEnabled(false);
            }

            else if(pastXray.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title)) && pregnancyHistory.getVisibility() == View.GONE){
                formDate.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                testId.setVisibility(View.VISIBLE);
                testId.getEditText().setText("");
                testId.getEditText().setError(null);
                goneVisibility();
                submitButton.setEnabled(false);
            }
            else{
                linearLayout.setVisibility(View.GONE);
                testId.setVisibility(View.GONE);
                cxrOrderTitle.setVisibility(View.GONE);
                screenXrayType.setVisibility(View.GONE);
                monthOfTreatment.setVisibility(View.GONE);
                submitButton.setEnabled(true);
            }
              /*  linearLayout.setVisibility(View.VISIBLE);
                submitButton.setEnabled(false);
                Integer resource = (Integer)testIdView.getTag();
                if(resource == R.drawable.ic_checked_green){
                    cxrOrderTitle.setVisibility(View.VISIBLE);
                    screenXrayType.setVisibility(View.VISIBLE);
                    monthOfTreatment.setVisibility(View.VISIBLE);
                    submitButton.setEnabled(true);
                }
                else{
                    cxrOrderTitle.setVisibility(View.GONE);
                    screenXrayType.setVisibility(View.GONE);
                    monthOfTreatment.setVisibility(View.GONE);
                    submitButton.setEnabled(false);
                }
            }*/
        }

        else if(radioGroup == pregnancyHistory.getRadioGroup()){
            if(pregnancyHistory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))
                    && pastXray.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title)) && pregnancyHistory.getVisibility() == View.VISIBLE){
                formDate.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                testId.setVisibility(View.VISIBLE);
                testId.getEditText().setText("");
                testId.getEditText().setError(null);
                goneVisibility();
                submitButton.setEnabled(false);
            }
            else if(pastXray.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title)) && pregnancyHistory.getVisibility() == View.GONE){
                formDate.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                testId.setVisibility(View.VISIBLE);
                testId.getEditText().setText("");
                testId.getEditText().setError(null);
                goneVisibility();
                submitButton.setEnabled(false);
            }
            else{
                linearLayout.setVisibility(View.GONE);
                testId.setVisibility(View.GONE);
                cxrOrderTitle.setVisibility(View.GONE);
                screenXrayType.setVisibility(View.GONE);
                monthOfTreatment.setVisibility(View.GONE);
                submitButton.setEnabled(true);
            }
              /*  linearLayout.setVisibility(View.VISIBLE);
                submitButton.setEnabled(false);
                Integer resource = (Integer)testIdView.getTag();
                if(resource == R.drawable.ic_checked_green){
                    cxrOrderTitle.setVisibility(View.VISIBLE);
                    screenXrayType.setVisibility(View.VISIBLE);
                    monthOfTreatment.setVisibility(View.VISIBLE);
                    submitButton.setEnabled(true);
                }
                else{
                    cxrOrderTitle.setVisibility(View.GONE);
                    screenXrayType.setVisibility(View.GONE);
                    monthOfTreatment.setVisibility(View.GONE);
                    submitButton.setEnabled(false);
                }
            }*/
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
