package com.ihsinformatics.gfatmmobile.childhoodTb;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
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
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
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
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbCXRScreeningTest extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener, View.OnTouchListener {
    Context context;

    boolean isResultForm = false;
    boolean beforeResult = false;
    boolean changeDate = false;
    String finalDate = null;

    TitledButton formDate;
    TitledRadioGroup formType;
    TitledRadioGroup typeOfXRay;
    TitledSpinner monthTreatment;
    TitledEditText testId;
    TitledEditText chestXRayScore;
    TitledRadioGroup radiologicalDiagnosis;
    TitledSpinner abnormalDiagnosis;
    TitledEditText otherRadiologicalDiagnosis;
    TitledRadioGroup diseaseExtent;
    TitledEditText radiologistRemarks;
    ImageView testIdView;

    Snackbar snackbar;
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
        FORM_NAME = Forms.CHILDHOODTB_CXR_SCREENING_TEST;
        FORM = Forms.childhoodTb_cxr_screening_test;

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
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
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
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_type_of_form), getResources().getStringArray(R.array.ctb_type_of_form_list), null, App.HORIZONTAL, App.VERTICAL, true);
        typeOfXRay = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_type_of_xray),getResources().getStringArray(R.array.ctb_type_of_xray_list),getResources().getString(R.string.ctb_chest_xray_other),App.HORIZONTAL,App.VERTICAL,true);
        monthTreatment = new TitledSpinner(context,null,getResources().getString(R.string.ctb_month_treatment),getResources().getStringArray(R.array.ctb_0_to_24),getResources().getString(R.string.ctb_1),App.HORIZONTAL,true);
        updateFollowUpMonth();
        testId = new TitledEditText(context,null,getResources().getString(R.string.ctb_test_id),"","",20,RegexUtil.OTHER_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,true);
        chestXRayScore = new TitledEditText(context,null,getResources().getString(R.string.ctb_chest_xray_cad4tb_score),"","",3,RegexUtil.NUMERIC_FILTER,InputType.TYPE_CLASS_NUMBER,App.HORIZONTAL,true);
        radiologicalDiagnosis = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_radiological_diagnosis),getResources().getStringArray(R.array.ctb_abnormal_list),null,App.VERTICAL,App.VERTICAL,false);
        abnormalDiagnosis = new TitledSpinner(context,null,getResources().getString(R.string.ctb_abnormal_diagnosis),getResources().getStringArray(R.array.ctb_radiological_diagnosis_list),null,App.VERTICAL,true);
        otherRadiologicalDiagnosis = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_specify),"","",50,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,false);
        diseaseExtent = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_extent_disease),getResources().getStringArray(R.array.ctb_disease_extent_list),null,App.VERTICAL,App.VERTICAL);
        radiologistRemarks = new TitledEditText(context,null,getResources().getString(R.string.ctb_radiologist_remark),"","",500,RegexUtil.OTHER_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,false);
        LinearLayout linearLayout = new LinearLayout(context);
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

        views = new View[]{formDate.getButton(),formType.getRadioGroup(),typeOfXRay.getRadioGroup(), abnormalDiagnosis.getSpinner(),diseaseExtent.getRadioGroup(),
                monthTreatment.getSpinner(),testId.getEditText(),chestXRayScore.getEditText(),radiologicalDiagnosis.getRadioGroup(),otherRadiologicalDiagnosis.getEditText(),radiologistRemarks.getEditText(),
        };

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formType, formDate, linearLayout,typeOfXRay,monthTreatment,chestXRayScore, radiologicalDiagnosis, abnormalDiagnosis,otherRadiologicalDiagnosis,diseaseExtent
                ,radiologistRemarks}};

        formDate.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        abnormalDiagnosis.getSpinner().setOnItemSelectedListener(this);
        radiologicalDiagnosis.getRadioGroup().setOnCheckedChangeListener(this);
        typeOfXRay.getRadioGroup().setOnCheckedChangeListener(this);
        diseaseExtent.getRadioGroup().setOnCheckedChangeListener(this);
        monthTreatment.getSpinner().setOnItemSelectedListener(this);

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
                        testId.getEditText().setError(getString(R.string.ctb_test_id_error));
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

    public void updateFollowUpMonth() {

        String treatmentDate = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "REGISTRATION DATE");
        String format = "";
        String[] monthArray;

        if (treatmentDate == null) {
            monthArray = new String[1];
            monthArray[0] = "0";
            monthTreatment.getSpinner().setSpinnerData(monthArray);
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
                monthTreatment.getSpinner().setSpinnerData(monthArray);
            } else {
                monthArray = new String[diffMonth];
                for (int i = 0; i < diffMonth; i++) {
                    monthArray[i] = String.valueOf(i+1);
                }
                monthTreatment.getSpinner().setSpinnerData(monthArray);
            }
        }
    }

    @Override
    public void updateDisplay() {
        Calendar treatDateCalender = null;

        if (snackbar != null)
            snackbar.dismiss();



        if(formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_result))){
            if (beforeResult) {
                Object[][] testIds = serverService.getTestIdByPatientAndEncounterType(App.getPatientId(), "Childhood TB-CXR Screening Test Order");
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
        }
            else if (isResultForm) {
                changeDate = false;
                Object[][] testIds = serverService.getTestIdByPatientAndEncounterType(App.getPatientId(), "Childhood TB-CXR Screening Test Order");
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
            Date date = new Date();
            if(treatmentDate != null){
                treatDateCalender = App.getCalendar(App.stringToDate(treatmentDate, "yyyy-MM-dd"));
            }

            if (formDateCalendar.after(App.getCalendar(date))) {
                changeDate = false;
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            }  else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                changeDate = false;
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            }  else if (treatDateCalender != null) {
                if(formDateCalendar.before(treatDateCalender)) {
                    changeDate = false;
                    formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                    snackbar = Snackbar.make(mainContent, getResources().getString(R.string.ctb_form_date_less_than_treatment_initiation), Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();

                    formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                }
                else {
                    changeDate = false;
                    formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                }
            }
            else {
                changeDate = false;
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            }
        }
        updateFollowUpMonth();
        formDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        boolean error=false;
        Boolean formCheck = false;

        if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_order)) || formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_result))) {

        } else {
            formCheck = true;
            error = true;
        }

        if (App.get(testId).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            testId.getEditText().setError(getString(R.string.empty_field));
            testId.getEditText().requestFocus();
            error = true;
        }
        else{
            testId.getEditText().setError(null);
        }

        if(chestXRayScore.getVisibility()==View.VISIBLE && App.get(chestXRayScore).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            chestXRayScore.getEditText().setError(getString(R.string.empty_field));
            chestXRayScore.getEditText().requestFocus();
            error = true;
        }else {
            chestXRayScore.getEditText().setError(null);
        }

        if(otherRadiologicalDiagnosis.getVisibility()==View.VISIBLE){
            if(App.get(otherRadiologicalDiagnosis).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherRadiologicalDiagnosis.getEditText().setError(getString(R.string.empty_field));
                otherRadiologicalDiagnosis.getEditText().requestFocus();
                error = true;
            }
            else if((App.get(otherRadiologicalDiagnosis).trim().length() <= 0)){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherRadiologicalDiagnosis.getEditText().setError(getString(R.string.ctb_spaces_only));
                otherRadiologicalDiagnosis.getEditText().requestFocus();
                error = true;
            }
        } else {
            otherRadiologicalDiagnosis.getEditText().setError(null);
        }
        if(radiologistRemarks.getVisibility()==View.VISIBLE){
            if(App.get(radiologistRemarks).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                radiologistRemarks.getEditText().setError(getString(R.string.empty_field));
                radiologistRemarks.getEditText().requestFocus();
                error = true;
            }
            else if(App.get(radiologistRemarks).trim().length() <= 0){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                radiologistRemarks.getEditText().setError(getString(R.string.ctb_spaces_only));
                radiologistRemarks.getEditText().requestFocus();
                error = true;
            }
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            if (formCheck) {
                alertDialog.setMessage(getString(R.string.ctb_select_form_type));
            } else {
                alertDialog.setMessage(getString(R.string.form_error));
            }
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
        if (App.get(formType).equals(getResources().getString(R.string.ctb_order))) {
            observations.add(new String[]{"TEST ID", App.get(testId)});
            observations.add(new String[]{"FOLLOW-UP MONTH", App.get(monthTreatment)});
            observations.add(new String[]{"TYPE OF X RAY", App.get(typeOfXRay).equals(getResources().getString(R.string.ctb_chest_xray_cad4tb)) ? "RADIOLOGICAL DIAGNOSIS" :
                     "X-RAY, OTHER"});
        } else if (App.get(formType).equals(getResources().getString(R.string.ctb_result))) {
            observations.add(new String[]{"TEST ID", App.get(testId)});
            if(!App.get(chestXRayScore).isEmpty()) {
                observations.add(new String[]{"CHEST X-RAY SCORE", App.get(chestXRayScore)});
            }
            observations.add(new String[]{"ABNORMAL DETAILED DIAGNOSIS", App.get(abnormalDiagnosis).equals(getResources().getString(R.string.ctb_adenopathy)) ? "ADENOPATHY" :
                    (App.get(abnormalDiagnosis).equals(getResources().getString(R.string.ctb_infiltration)) ? "INFILTRATE" :
                            (App.get(abnormalDiagnosis).equals(getResources().getString(R.string.ctb_consolidation)) ? "CONSOLIDATION" :
                                (App.get(abnormalDiagnosis).equals(getResources().getString(R.string.ctb_effusion)) ? "PLEURAL EFFUSION" :
                                            (App.get(abnormalDiagnosis).equals(getResources().getString(R.string.ctb_cavitation)) ? "CAVIATION" :
                                                    (App.get(abnormalDiagnosis).equals(getResources().getString(R.string.ctb_miliary_tb)) ? "MILIARY" :
                                                            "OTHER ABNORMAL DETAILED DIAGNOSIS")))))});

            observations.add(new String[]{"RADIOLOGICAL DIAGNOSIS", App.get(radiologicalDiagnosis).equals(getResources().getString(R.string.ctb_normal)) ? "NORMAL" :
                    (App.get(radiologicalDiagnosis).equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb)) ?  "ABNORMAL SUGGESTIVE OF TB" : "ABNORMAL NOT SUGGESTIVE OF TB")});

            if(otherRadiologicalDiagnosis.getVisibility()==View.VISIBLE) {
                observations.add(new String[]{"OTHER ABNORMAL DETAILED DIAGNOSIS", App.get(otherRadiologicalDiagnosis)});
            }
            observations.add(new String[]{"EXTENT OF DISEASE", App.get(diseaseExtent).equals(getResources().getString(R.string.ctb_normal)) ? "NORMAL" :
                    (App.get(diseaseExtent).equals(getResources().getString(R.string.ctb_unilateral_disease)) ? "UNILATERAL":
                        (App.get(diseaseExtent).equals(getResources().getString(R.string.ctb_bilateral_disease)) ? "BILATERAL" : "ABNORMAL"))});
            if(!App.get(radiologistRemarks).isEmpty()) {
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

                if (App.get(formType).equals(getResources().getString(R.string.ctb_order))){
                    result = serverService.saveEncounterAndObservation("CXR Screening Test Order", FORM, formDateCalendar, observations.toArray(new String[][]{}),true);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                } else if (App.get(formType).equals(getResources().getString(R.string.ctb_result))) {
                    result = serverService.saveEncounterAndObservation("CXR Screening Test Result", FORM, formDateCalendar, observations.toArray(new String[][]{}),false);
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

        serverService.saveFormLocally(FORM_NAME, FORM, "12345-5", formValues);

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
            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            }else if(fo.getFormName().contains("Order")) {
                formType.getRadioGroup().getButtons().get(0).setChecked(true);
                formType.getRadioGroup().getButtons().get(1).setEnabled(false);
                testIdView.setImageResource(R.drawable.ic_checked_green);
                if (obs[0][0].equals("TEST ID")) {
                    testId.getEditText().setEnabled(false);
                    testIdView.setEnabled(false);
                    testIdView.setImageResource(R.drawable.ic_checked_green);
                    testId.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("TYPE OF X RAY")) {
                    for (RadioButton rb : typeOfXRay.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_chest_xray_cad4tb)) && obs[0][1].equals("RADIOLOGICAL DIAGNOSIS")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_chest_xray_other)) && obs[0][1].equals("X-RAY, OTHER")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    typeOfXRay.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                    monthTreatment.getSpinner().selectValue(obs[0][1]);
                    monthTreatment.setVisibility(View.VISIBLE);
                }
                submitButton.setEnabled(true);
            }else{
                formType.getRadioGroup().getButtons().get(1).setChecked(true);
                formType.getRadioGroup().getButtons().get(0).setEnabled(false);
                if (obs[0][0].equals("TEST ID")) {
                    testId.getEditText().setText(obs[0][1]);
                    testId.getEditText().setEnabled(false);
                    testIdView.setEnabled(false);
                    testIdView.setImageResource(R.drawable.ic_checked);
                    checkTestId();
                } else if (obs[0][0].equals("CHEST X-RAY SCORE")) {
                    chestXRayScore.getEditText().setText(obs[0][1]);
                }
                else if (obs[0][0].equals("RADIOLOGICAL DIAGNOSIS")) {
                    for (RadioButton rb : radiologicalDiagnosis.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_normal)) && obs[0][1].equals("NORMAL")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb)) && obs[0][1].equals("ABNORMAL SUGGESTIVE OF TB")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_abnormal_not_suggestive_tb)) && obs[0][1].equals("ABNORMAL NOT SUGGESTIVE OF TB")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                }
                else if (obs[0][0].equals("ABNORMAL DETAILED DIAGNOSIS")) {
                    String value = obs[0][1].equals("ADENOPATHY") ? getResources().getString(R.string.ctb_adenopathy) :
                            (obs[0][1].equals("INFILTRATE") ? getResources().getString(R.string.ctb_infiltration) :
                                    (obs[0][1].equals("CONSOLIDATION") ? getResources().getString(R.string.ctb_consolidation) :
                                            (obs[0][1].equals("PLEURAL EFFUSION") ? getResources().getString(R.string.ctb_effusion) :
                                                            (obs[0][1].equals("CAVIATION") ? getResources().getString(R.string.ctb_cavitation) :
                                                                    (obs[0][1].equals("MILIARY") ? getResources().getString(R.string.ctb_miliary_tb) :
                                                                            getResources().getString(R.string.ctb_other_title))))));
                    if(value.equalsIgnoreCase(getResources().getString(R.string.ctb_other_title))){
                        otherRadiologicalDiagnosis.setVisibility(View.VISIBLE);
                    }
                    abnormalDiagnosis.getSpinner().selectValue(value);
                }
                else if (obs[0][0].equals("OTHER ABNORMAL DETAILED DIAGNOSIS")) {
                    otherRadiologicalDiagnosis.getEditText().setText(obs[0][1]);
                }else if (obs[0][0].equals("EXTENT OF DISEASE")) {
                    for (RadioButton rb : diseaseExtent.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_normal)) && obs[0][1].equals("NORMAL")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_unilateral_disease)) && obs[0][1].equals("UNILATERAL")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_bilateral_disease)) && obs[0][1].equals("BILATERAL")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_abnormal_extend_not_defined)) && obs[0][1].equals("ABNORMAL")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                }else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
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
        MySpinner spinner = (MySpinner) parent;
        if (spinner == abnormalDiagnosis.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                otherRadiologicalDiagnosis.setVisibility(View.VISIBLE);
            } else {
                otherRadiologicalDiagnosis.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        testId.getEditText().setEnabled(true);
        formType.getRadioGroup().getButtons().get(0).setEnabled(true);
        formType.getRadioGroup().getButtons().get(1).setEnabled(true);
        testIdView.setEnabled(true);
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        formDate.setVisibility(View.GONE);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == formType.getRadioGroup()) {
            formDate.setVisibility(View.VISIBLE);
            testId.setVisibility(View.VISIBLE);
            testId.getEditText().setText("");
            testId.getEditText().setError(null);
            goneVisibility();
            submitButton.setEnabled(false);
        }

    }
    void goneVisibility() {
        typeOfXRay.setVisibility(View.GONE);
        monthTreatment.setVisibility(View.GONE);
        chestXRayScore.setVisibility(View.GONE);
        radiologicalDiagnosis.setVisibility(View.GONE);
        abnormalDiagnosis.setVisibility(View.GONE);
        otherRadiologicalDiagnosis.setVisibility(View.GONE);
        diseaseExtent.setVisibility(View.GONE);
        radiologistRemarks.setVisibility(View.GONE);
    }

    void showTestOrderOrTestResult() {
        if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.ctb_order))) {
            isResultForm = false;
            beforeResult = false;
            formDate.setVisibility(View.VISIBLE);
            monthTreatment.setVisibility(View.VISIBLE);
            typeOfXRay.setVisibility(View.VISIBLE);

            chestXRayScore.setVisibility(View.GONE);
            abnormalDiagnosis.setVisibility(View.GONE);
            radiologicalDiagnosis.setVisibility(View.GONE);
            otherRadiologicalDiagnosis.setVisibility(View.GONE);
            diseaseExtent.setVisibility(View.GONE);
            radiologistRemarks.setVisibility(View.GONE);

        } else {
            isResultForm = true;
            beforeResult = false;
            String typeofXray = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "CXR Screening Test Order", "TYPE OF X RAY");
            if(typeofXray.equalsIgnoreCase("RADIOLOGICAL DIAGNOSIS")){
                chestXRayScore.setVisibility(View.VISIBLE);
            }
            formDate.setVisibility(View.VISIBLE);
            radiologicalDiagnosis.setVisibility(View.VISIBLE);
            abnormalDiagnosis.setVisibility(View.VISIBLE);
            if(App.get(abnormalDiagnosis).equals(getResources().getString(R.string.ctb_other_title))){
                otherRadiologicalDiagnosis.setVisibility(View.VISIBLE);
            }
            diseaseExtent.setVisibility(View.VISIBLE);
            radiologistRemarks.setVisibility(View.VISIBLE);

            monthTreatment.setVisibility(View.GONE);
            typeOfXRay.setVisibility(View.GONE);
        }
    }

    public boolean validateResultDate() {
        updateDisplay();
        return changeDate;
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

                Object[][] testIds = serverService.getTestIdByPatientAndEncounterType(App.getPatientId(), "Childhood TB-CXR Screening Test Order");

                if (testIds == null || testIds.length < 1) {
                    if (App.get(formType).equals(getResources().getString(R.string.ctb_order)))
                        return "SUCCESS";
                    else
                        return "";
                }


                if (App.get(formType).equals(getResources().getString(R.string.ctb_order))) {
                    result = "SUCCESS";
                    for (int i = 0; i < testIds.length; i++) {
                        if (String.valueOf(testIds[i][0]).equals(App.get(testId))) {
                            return "";
                        }
                    }
                }

                if (App.get(formType).equals(getResources().getString(R.string.ctb_result))) {
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
                    if (App.get(formType).equals(getResources().getString(R.string.ctb_order))) {
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
