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

/**
 * Created by Haris on 1/24/2017.
 */

public class FastScreeningChestXrayOrderAndResultForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener{
    Context context;


    boolean emptyError = false;


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
  //  ImageView testIdView;
  //  LinearLayout linearLayout;

    TitledSpinner orderIds;
    TitledEditText orderId;


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
        pastXray = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_past_xray), getResources().getStringArray(R.array.fast_yes_no_list),"", App.VERTICAL, App.VERTICAL, true);
        pregnancyHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_pregnancy_history), getResources().getStringArray(R.array.fast_yes_no_list), "", App.VERTICAL, App.VERTICAL, true);
        testId = new TitledEditText(context, null, getResources().getString(R.string.fast_test_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_select_form_type), getResources().getStringArray(R.array.fast_order_and_result_list), "", App.HORIZONTAL, App.HORIZONTAL);
        cxrOrderTitle = new MyTextView(context, getResources().getString(R.string.fast_cxr_order_title));
        cxrOrderTitle.setTypeface(null, Typeface.BOLD);
        screenXrayType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_what_type_of_xray_is_this), getResources().getStringArray(R.array.fast_type_of_xray_is_this_list),"", App.VERTICAL, App.VERTICAL, true);
        monthOfTreatment = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_month_of_treatment), getResources().getStringArray(R.array.fast_number_list), "", App.VERTICAL);
        updateFollowUpMonth();
        //   testDate = new TitledButton(context, null, getResources().getString(R.string.fast_test_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        cxrResultTitle = new MyTextView(context, getResources().getString(R.string.fast_cxr_result_title));
        cxrResultTitle.setTypeface(null, Typeface.BOLD);
        cat4tbScore = new TitledEditText(context, null, getResources().getString(R.string.fast_chest_xray_cad4tb_score), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);
        radiologicalDiagnosis = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_radiologica_diagnosis), getResources().getStringArray(R.array.fast_radiological_diagonosis_list), "", App.VERTICAL, App.VERTICAL, true);
        abnormalDetailedDiagnosis = new TitledCheckBoxes(context, null, getResources().getString(R.string.fast_if_abnormal_detailed_diagnosis), getResources().getStringArray(R.array.fast_abnormal_detailed_diagnosis_list), new Boolean[]{true, false, false, false, false, false, false}, App.VERTICAL, App.VERTICAL, true);
        abnormalDetailedDiagnosisOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        extentOfDisease = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_extent_of_desease), getResources().getStringArray(R.array.fast_extent_of_disease_list), getResources().getString(R.string.fast_normal), App.VERTICAL, App.VERTICAL, true);
        radiologistRemarks = new TitledEditText(context, null, getResources().getString(R.string.fast_radiologist_remarks), "", "", 500, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        orderId = new TitledEditText(context, null, getResources().getString(R.string.order_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        orderIds = new TitledSpinner(context, "", getResources().getString(R.string.order_id), getResources().getStringArray(R.array.pet_empty_array), "", App.HORIZONTAL);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), formType.getRadioGroup(), testId.getEditText(), screenXrayType.getRadioGroup(),
                monthOfTreatment.getSpinner(), radiologicalDiagnosis.getRadioGroup(), cat4tbScore.getEditText(), abnormalDetailedDiagnosis,
                abnormalDetailedDiagnosisOther.getEditText(), extentOfDisease.getRadioGroup(), radiologistRemarks.getEditText()
        , orderId.getEditText(), orderIds.getSpinner()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formType, formDate, cxrOrderTitle,  pastXray, pregnancyHistory, orderId,  screenXrayType, monthOfTreatment, cxrResultTitle, orderIds, testId, cat4tbScore, radiologicalDiagnosis,
                        abnormalDetailedDiagnosis, abnormalDetailedDiagnosisOther, extentOfDisease, radiologistRemarks}};

        formDate.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        radiologicalDiagnosis.getRadioGroup().setOnCheckedChangeListener(this);
        pastXray.getRadioGroup().setOnCheckedChangeListener(this);
        pregnancyHistory.getRadioGroup().setOnCheckedChangeListener(this);
        orderIds.getSpinner().setOnItemSelectedListener(this);
        for (CheckBox cb : abnormalDetailedDiagnosis.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {
        Calendar treatDateCalender = null;
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();
            personDOB = personDOB.substring(0, 10);

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
            } else {
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

                if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.fast_result))) {

                    if (!App.get(orderIds).equals("")) {
                        String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), App.getProgram() + "-" + "Screening CXR Test Order", "ORDER ID", App.get(orderIds));

                        String format = "";
                        if (encounterDateTime.contains("/")) {
                            format = "dd/MM/yyyy";
                        } else {
                            format = "yyyy-MM-dd";
                        }

                        Date orderDate = App.stringToDate(encounterDateTime, format);

                        if (formDateCalendar.before(App.getCalendar(orderDate))) {

                            Date dDate = App.stringToDate(formDa, "EEEE, MMM dd,yyyy");
                            if (dDate.before(orderDate)) {
                                formDateCalendar = Calendar.getInstance();
                                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                            } else {
                                formDateCalendar = App.getCalendar(dDate);
                                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                            }

                            snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_result_date_cannot_be_before_order_date), Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();

                        }

                    }
                } else if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.fast_order))) {
                    String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "REGISTRATION DATE");
                    if (treatmentDate != null) {
                        treatDateCalender = App.getCalendar(App.stringToDate(treatmentDate, "yyyy-MM-dd"));
                        if (formDateCalendar.before(treatDateCalender)) {
                            formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                            snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_cannot_be_before_treatment_initiation_form), Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();

                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        } else {
                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        }
                    }

                }
            }

        } else {
            String formDa = formDate.getButton().getText().toString();

            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.fast_result))) {

                if (!App.get(orderIds).equals("")) {
                    String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), App.getProgram() + "-" + "Screening CXR Test Order", "ORDER ID", App.get(orderIds));

                    String format = "";
                    if (encounterDateTime.contains("/")) {
                        format = "dd/MM/yyyy";
                    } else {
                        format = "yyyy-MM-dd";
                    }

                    Date orderDate = App.stringToDate(encounterDateTime, format);

                    if (formDateCalendar.before(App.getCalendar(orderDate))) {

                        Date dDate = App.stringToDate(formDa, "EEEE, MMM dd,yyyy");
                        if (dDate.before(orderDate)) {
                            formDateCalendar = Calendar.getInstance();
                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        } else {
                            formDateCalendar = App.getCalendar(dDate);
                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        }

                        snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_result_date_cannot_be_before_order_date), Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();

                    }

                }
            }
        }

        updateFollowUpMonth();
        formDate.getButton().setEnabled(true);
    }

    public void updateFollowUpMonth() {

        String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "REGISTRATION DATE");
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
        boolean isChecked = false;

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

        if (pastXray.getVisibility() == View.VISIBLE && App.get(pastXray).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);

            emptyError = true;
            error = true;
        }

        if (pregnancyHistory.getVisibility() == View.VISIBLE && App.get(pregnancyHistory).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            emptyError = true;
            error = true;
        }

        if (screenXrayType.getVisibility() == View.VISIBLE && App.get(screenXrayType).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            emptyError = true;
            error = true;
        }

        if (radiologicalDiagnosis.getVisibility() == View.VISIBLE && App.get(radiologicalDiagnosis).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            emptyError = true;
            error = true;
        }

        if (abnormalDetailedDiagnosis.getVisibility() == View.VISIBLE) {
            for(CheckBox cb : abnormalDetailedDiagnosis.getCheckedBoxes()){
                if(cb.isChecked()){
                    isChecked = true;
                }
            }
        }

        if(abnormalDetailedDiagnosis.getVisibility() == View.VISIBLE && !isChecked){
            emptyError = true;
            error = true;
        }

        if (testId.getVisibility() == View.VISIBLE && testId.getEditText().getText().toString().length() > 0 && testId.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            testId.getEditText().setError(getString(R.string.empty_field));
            testId.getEditText().requestFocus();
            error = true;
        }

        if (orderIds.getVisibility() == View.VISIBLE) {
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "Screening CXR Test Result", "ORDER ID");
            if (resultTestIds != null) {
                for (String id : resultTestIds) {

                    if (id.equals(App.get(orderIds))) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        alertDialog.setMessage(getResources().getString(R.string.ctb_order_result_found_error) + App.get(orderIds));
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

                        return false;
                    }
                }
            }
        }

        if (testId.getVisibility() == View.VISIBLE) {
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "Screening CXR Test Result", "TEST ID");
            if (resultTestIds != null) {
                for (String id : resultTestIds) {
                    if (id.equals(App.get(testId))) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        alertDialog.setMessage(getResources().getString(R.string.ctb_test_result_found_error) + App.get(testId));
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

                        return false;
                    }

                }
            }

        }


        if (error) {
            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            if(!emptyError)
                alertDialog.setMessage(getString(R.string.form_error));
            else
                alertDialog.setMessage(getString(R.string.fast_required_field_error));
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

    public void setOrderId(){
        Date nowDate = new Date();
        orderId.getEditText().setText(App.getSqlDateTime(nowDate));
        orderId.setOnKeyListener(null);
        orderId.getEditText().setFocusable(false);
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


        if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))) {

            if(orderId.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"ORDER ID", App.get(orderId)});

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
            observations.add(new String[]{"ORDER ID", App.get(orderIds)});

            if (testId.getVisibility() == View.VISIBLE && !App.get(testId).isEmpty())
                observations.add(new String[]{"TEST ID", App.get(testId)});

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

            if (!App.get(testId).isEmpty()) {
                observations.add(new String[]{"TEST ID", App.get(testId)});
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



    void showTestOrderOrTestResult() {
        //formDate.setVisibility(View.VISIBLE);
        if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))) {

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

            orderId.setVisibility(View.VISIBLE);
            Date nowDate = new Date();
            orderId.getEditText().setText(App.getSqlDateTime(nowDate));
            orderIds.setVisibility(View.GONE);
            testId.setVisibility(View.GONE);
            orderId.setOnKeyListener(null);
            orderId.getEditText().setFocusable(false);

            screenXrayType.getRadioGroup().selectDefaultValue();
            monthOfTreatment.getSpinner().selectDefaultValue();
            updateFollowUpMonth();

        }  else if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_result))) {
            cxrOrderTitle.setVisibility(View.GONE);
            screenXrayType.setVisibility(View.GONE);
            monthOfTreatment.setVisibility(View.GONE);
            pastXray.setVisibility(View.GONE);
            pregnancyHistory.setVisibility(View.GONE);
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

            orderId.setVisibility(View.GONE);
            orderIds.setVisibility(View.VISIBLE);
            testId.setVisibility(View.VISIBLE);

            testId.getEditText().setDefaultValue();
            radiologicalDiagnosis.getRadioGroup().selectDefaultValue();
            cat4tbScore.getEditText().setDefaultValue();
            extentOfDisease.getRadioGroup().selectDefaultValue();
            radiologistRemarks.getEditText().setDefaultValue();
            abnormalDetailedDiagnosis.selectDefaultValue();

            String[] testIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "Screening CXR Test Order", "ORDER ID");

            if (testIds == null || testIds.length == 0) {
                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage(getResources().getString(R.string.fast_no_order_found_for_the_patient));
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
                submitButton.setEnabled(false);
                return;
            }

            if(testIds != null) {
                orderIds.getSpinner().setSpinnerData(testIds);
            }
        }
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
              //  testIdView.setImageResource(R.drawable.ic_checked_green);
                if (obs[0][0].equals("TEST ID")) {
                    testId.getEditText().setEnabled(false);
                  //  testIdView.setEnabled(false);
                 //   testIdView.setImageResource(R.drawable.ic_checked_green);
                    testId.getEditText().setText(obs[0][1]);
                }

                else if (obs[0][0].equals("X RAY IN PAST 6 MONTHS")) {
                    for (RadioButton rb : pastXray.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                            testId.getEditText().setEnabled(true);
                       //     testIdView.setEnabled(true);
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
                          //  testIdView.setEnabled(true);
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

                if (obs[0][0].equals("ORDER ID")) {
                    orderIds.getSpinner().selectValue(obs[0][1]);
                    orderIds.getSpinner().setEnabled(false);
                }


                if (obs[0][0].equals("TEST ID")) {
                    testId.getEditText().setText(obs[0][1]);
                    testId.getEditText().setEnabled(false);
                //    testIdView.setEnabled(false);
                //    testIdView.setImageResource(R.drawable.ic_checked);
                //    checkTestId();
                }

                else if (obs[0][0].equals("CHEST X-RAY SCORE")) {
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

      //  testIdView.setVisibility(View.GONE);
        testId.setVisibility(View.GONE);
     //   testIdView.setImageResource(R.drawable.ic_checked);
        goneVisibility();
        submitButton.setEnabled(false);

        String[] testIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "Screening CXR Test Order", "ORDER ID");
        if(testIds != null) {
            orderIds.getSpinner().setSpinnerData(testIds);
        }

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

        orderIds.setVisibility(View.GONE);
        orderId.setVisibility(View.GONE);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == orderIds.getSpinner()) {
            updateDisplay();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if (radioGroup == formType.getRadioGroup()) {
            if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))) {
                submitButton.setEnabled(true);
                formDate.setVisibility(View.VISIBLE);
                setOrderId();
                goneVisibility();
                testId.setVisibility(View.GONE);
                cxrOrderTitle.setVisibility(View.VISIBLE);
                pastXray.setVisibility(View.VISIBLE);

                pastXray.getRadioGroup().selectDefaultValue();
                pregnancyHistory.getRadioGroup().selectDefaultValue();
                screenXrayType.getRadioGroup().selectDefaultValue();
                monthOfTreatment.getSpinner().selectDefaultValue();

                if (App.getPatient().getPerson().getGender().equals("female") || App.getPatient().getPerson().getGender().equals("F")) {
                    pregnancyHistory.setVisibility(View.VISIBLE);
                }


                if(pastXray.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))
                        && pregnancyHistory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))){

                    screenXrayType.setVisibility(View.VISIBLE);
                    monthOfTreatment.setVisibility(View.VISIBLE);
                    orderId.setVisibility(View.VISIBLE);
                }
                else{
                    screenXrayType.setVisibility(View.GONE);
                    monthOfTreatment.setVisibility(View.GONE);
                    orderId.setVisibility(View.GONE);
                }

                if(pregnancyHistory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))
                        && pastXray.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))){
                    screenXrayType.setVisibility(View.VISIBLE);
                    monthOfTreatment.setVisibility(View.VISIBLE);
                    orderId.setVisibility(View.VISIBLE);
                }
                else{
                    screenXrayType.setVisibility(View.GONE);
                    monthOfTreatment.setVisibility(View.GONE);
                    orderId.setVisibility(View.GONE);
                }

                //showTestOrderOrTestResult();
            } else {
                submitButton.setEnabled(true);
                formDate.setVisibility(View.VISIBLE);
                showTestOrderOrTestResult();
            }

            // if (radioGroup == formType.getRadioGroup()) {

         /*   if(formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))){
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
                linearLayout.setVisibility(View.VISIBLE);
                testId.setVisibility(View.VISIBLE);
                testId.getEditText().setText("");
                testId.getEditText().setError(null);
                goneVisibility();
                submitButton.setEnabled(false);
            }*/
        }
         else if (radioGroup == radiologicalDiagnosis.getRadioGroup()) {
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
                screenXrayType.setVisibility(View.VISIBLE);
                monthOfTreatment.setVisibility(View.VISIBLE);
                orderId.setVisibility(View.VISIBLE);
            }

            else if(pastXray.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title)) && pregnancyHistory.getVisibility() == View.GONE){
                formDate.setVisibility(View.VISIBLE);
                screenXrayType.setVisibility(View.VISIBLE);
                monthOfTreatment.setVisibility(View.VISIBLE);
                orderId.setVisibility(View.VISIBLE);
            }
            else{
                screenXrayType.setVisibility(View.GONE);
                monthOfTreatment.setVisibility(View.GONE);
                orderId.setVisibility(View.GONE);
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
                screenXrayType.setVisibility(View.VISIBLE);
                monthOfTreatment.setVisibility(View.VISIBLE);
                orderId.setVisibility(View.VISIBLE);
            }
            else if(pastXray.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title)) && pregnancyHistory.getVisibility() == View.GONE){
                formDate.setVisibility(View.VISIBLE);
                screenXrayType.setVisibility(View.VISIBLE);
                monthOfTreatment.setVisibility(View.VISIBLE);
                orderId.setVisibility(View.VISIBLE);
            }
            else{
                screenXrayType.setVisibility(View.GONE);
                monthOfTreatment.setVisibility(View.GONE);
                orderId.setVisibility(View.GONE);
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
