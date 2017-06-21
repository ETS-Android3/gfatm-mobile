package com.ihsinformatics.gfatmmobile.childhoodTb;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbTreatmentFollowup extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    public static final int THIRD_DATE_DIALOG_ID = 3;
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;

    Snackbar snackbar;
    ScrollView scrollView;
    TitledButton formDate;

    TitledSpinner patientType;
    TitledEditText tbRegisterationNumber;
    TitledButton treatmentInitiationDate;
    TitledSpinner monthTreatment;
    TitledRadioGroup patientCategory;
    TitledEditText weight;
    TitledRadioGroup treatmentPlan;
    TitledRadioGroup intensivePhaseRegimen;
    TitledSpinner typeFixedDosePrescribedIntensive;
    TitledRadioGroup currentTabletsofRHZ;
    TitledRadioGroup currentTabletsofE;
    TitledRadioGroup newTabletsofRHZ;
    TitledRadioGroup newTabletsofE;
    TitledRadioGroup adultFormulationofHRZE;

    TitledRadioGroup continuationPhaseRegimen;
    TitledSpinner typeFixedDosePrescribedContinuation;
    TitledRadioGroup currentTabletsOfContinuationRH;
    TitledRadioGroup currentTabletsOfContinuationE;
    TitledRadioGroup newTabletsOfContinuationRH;
    TitledRadioGroup newTabletsOfContinuationE;
    TitledRadioGroup adultFormulationOfContinuationRH;
    TitledRadioGroup adultFormulationOfContinuationRHE;
    TitledRadioGroup conclusionOfTreatment;
    TitledButton returnVisitDate;
    TitledEditText doctorNotes;
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
        FORM_NAME = Forms.CHILDHOODTB_TB_TREATMENT_FOLLOWUP;
        FORM = Forms.childhoodTb_tb_treatment_followup;

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
        thirdDateCalendar = Calendar.getInstance();
        thirdDateFragment = new SelectDateFragment();


        // first page views...
        formDate = new TitledButton(context,null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");

        patientType = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_patient_type), getResources().getStringArray(R.array.ctb_patient_type_list), getResources().getString(R.string.ctb_new), App.VERTICAL,true);
        tbRegisterationNumber = new TitledEditText(context, null, getResources().getString(R.string.ctb_tb_registration_no), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        treatmentInitiationDate = new TitledButton(context, null, getResources().getString(R.string.ctb_treatment_initiated_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        monthTreatment = new TitledSpinner(context, null, getResources().getString(R.string.ctb_month_treatment), getResources().getStringArray(R.array.ctb_0_to_24), null, App.HORIZONTAL);
        updateFollowUpMonth();
        patientCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_patient_category), getResources().getStringArray(R.array.ctb_patient_category3_list), getResources().getString(R.string.ctb_categoryI), App.VERTICAL, App.VERTICAL,true);
        weight = new TitledEditText(context, null, getResources().getString(R.string.ctb_patient_weight), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        treatmentPlan = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_treatment_plan), getResources().getStringArray(R.array.ctb_treatment_plan_list), null, App.VERTICAL, App.VERTICAL,true);

        intensivePhaseRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_regimen), getResources().getStringArray(R.array.ctb_regimen_list), getResources().getString(R.string.ctb_rhz), App.HORIZONTAL, App.VERTICAL,true);
        typeFixedDosePrescribedIntensive = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_type_of_fixed_dose), getResources().getStringArray(R.array.ctb_type_of_fixed_dose_list), null, App.VERTICAL,true);
        currentTabletsofRHZ = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_current_formulation_number_of_tablet_rhz), getResources().getStringArray(R.array.ctb_1_to_5_list), null, App.HORIZONTAL, App.VERTICAL,true);
        currentTabletsofE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_current_formulation_number_of_tablet_e), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL,true);
        newTabletsofRHZ = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_new_formulation_number_of_tablet_rhz), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL,true);
        newTabletsofE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_new_formulation_number_of_tablet_e), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL,true);
        adultFormulationofHRZE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_adult_formulation), getResources().getStringArray(R.array.ctb_2_to_5_list), null, App.HORIZONTAL, App.VERTICAL,true);
        continuationPhaseRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_continuation_phase_regimen), getResources().getStringArray(R.array.ctb_continuation_phase_regimen_list), null, App.HORIZONTAL, App.VERTICAL,true);
        typeFixedDosePrescribedContinuation = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_type_of_fixed_dose), getResources().getStringArray(R.array.ctb_type_of_dose_continuation_list), null, App.VERTICAL,true);
        currentTabletsOfContinuationRH = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_current_formulation_continuation_rh), getResources().getStringArray(R.array.ctb_1_to_5_list), null, App.HORIZONTAL, App.VERTICAL,true);
        currentTabletsOfContinuationE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_current_formulation_continuation_e), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL,true);
        newTabletsOfContinuationRH = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_new_formulation_continuation_rh), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL,true);
        newTabletsOfContinuationE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_new_formulation_continuation_e), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL,true);
        adultFormulationOfContinuationRH = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_if_adult_formulation_continuation_rh), getResources().getStringArray(R.array.ctb_1_to_2), null, App.HORIZONTAL, App.VERTICAL,true);
        adultFormulationOfContinuationRHE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_if_adult_formulation_continuation_rhe), getResources().getStringArray(R.array.ctb_2_to_4), null, App.HORIZONTAL, App.VERTICAL,true);
        conclusionOfTreatment = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_conclusion_of_treatment), getResources().getStringArray(R.array.ctb_improve_no_improvement), null, App.HORIZONTAL, App.VERTICAL,true);
        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.ctb_next_appointment_date), DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);
        doctorNotes = new TitledEditText(context, null, getResources().getString(R.string.ctb_doctor_notes), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL,false);

        thirdDateCalendar.set(Calendar.YEAR, secondDateCalendar.get(Calendar.YEAR));
        thirdDateCalendar.set(Calendar.DAY_OF_MONTH, secondDateCalendar.get(Calendar.DAY_OF_MONTH));
        thirdDateCalendar.set(Calendar.MONTH, secondDateCalendar.get(Calendar.MONTH));
        thirdDateCalendar.add(Calendar.DAY_OF_MONTH, 30);

        views = new View[]{
                formDate.getButton(),patientType.getSpinner(),tbRegisterationNumber.getEditText(),treatmentInitiationDate.getButton(),monthTreatment.getSpinner(),patientCategory.getRadioGroup(),weight.getEditText(),
                treatmentPlan.getRadioGroup(), intensivePhaseRegimen.getRadioGroup(),typeFixedDosePrescribedIntensive.getSpinner(),currentTabletsofRHZ.getRadioGroup(),currentTabletsofE.getRadioGroup(),
                newTabletsofRHZ.getRadioGroup(),newTabletsofE.getRadioGroup(),adultFormulationofHRZE.getRadioGroup(), continuationPhaseRegimen.getRadioGroup(),typeFixedDosePrescribedContinuation.getSpinner(),
                currentTabletsOfContinuationRH.getRadioGroup(), currentTabletsOfContinuationE.getRadioGroup(), newTabletsOfContinuationRH.getRadioGroup(), newTabletsOfContinuationE.getRadioGroup(),
                adultFormulationOfContinuationRH.getRadioGroup(), adultFormulationOfContinuationRHE.getRadioGroup(),conclusionOfTreatment.getRadioGroup(),returnVisitDate.getButton(),doctorNotes.getEditText()};
        viewGroups = new View[][]
                {{
                        formDate,
                        patientType,tbRegisterationNumber,
                        treatmentInitiationDate,
                        monthTreatment,
                        patientCategory,
                        weight,
                        treatmentPlan,
                        intensivePhaseRegimen,
                        typeFixedDosePrescribedIntensive,
                        currentTabletsofRHZ,
                        currentTabletsofE,
                        newTabletsofRHZ,
                        newTabletsofE,
                        adultFormulationofHRZE,
                        continuationPhaseRegimen,
                        typeFixedDosePrescribedContinuation,
                        currentTabletsOfContinuationRH,
                        currentTabletsOfContinuationE,
                        newTabletsOfContinuationRH,
                        newTabletsOfContinuationE,
                        adultFormulationOfContinuationRH,
                        adultFormulationOfContinuationRHE,
                        conclusionOfTreatment,
                        returnVisitDate,
                        doctorNotes,
                }};
        formDate.getButton().setOnClickListener(this);


        patientCategory.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentPlan.getRadioGroup().setOnCheckedChangeListener(this);
        intensivePhaseRegimen.getRadioGroup().setOnCheckedChangeListener(this);
        typeFixedDosePrescribedIntensive.getSpinner().setOnItemSelectedListener(this);
        currentTabletsofRHZ.getRadioGroup().setOnCheckedChangeListener(this);
        currentTabletsofE.getRadioGroup().setOnCheckedChangeListener(this);
        newTabletsofRHZ.getRadioGroup().setOnCheckedChangeListener(this);
        newTabletsofE.getRadioGroup().setOnCheckedChangeListener(this);
        adultFormulationofHRZE.getRadioGroup().setOnCheckedChangeListener(this);
        continuationPhaseRegimen.getRadioGroup().setOnCheckedChangeListener(this);
        typeFixedDosePrescribedContinuation.getSpinner().setOnItemSelectedListener(this);
        currentTabletsOfContinuationRH.getRadioGroup().setOnCheckedChangeListener(this);
        currentTabletsOfContinuationE.getRadioGroup().setOnCheckedChangeListener(this);
        newTabletsOfContinuationRH.getRadioGroup().setOnCheckedChangeListener(this);
        newTabletsOfContinuationE.getRadioGroup().setOnCheckedChangeListener(this);
        adultFormulationOfContinuationRH.getRadioGroup().setOnCheckedChangeListener(this);
        adultFormulationOfContinuationRHE.getRadioGroup().setOnCheckedChangeListener(this);
        conclusionOfTreatment.getRadioGroup().setOnCheckedChangeListener(this);
        returnVisitDate.getButton().setOnClickListener(this);
        treatmentInitiationDate.getButton().setOnClickListener(this);


        weight.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    int value = Integer.parseInt(s.toString());

                    //CURRENT FORMULATION
                    if(value>=4 && value<=6){
                        currentTabletsOfContinuationRH.getRadioGroup().getButtons().get(0).setChecked(true);
                        currentTabletsOfContinuationE.getRadioGroup().getButtons().get(0).setChecked(true);
                        currentTabletsofRHZ.getRadioGroup().getButtons().get(0).setChecked(true);
                        currentTabletsofE.getRadioGroup().getButtons().get(0).setChecked(true);
                    }
                    else if(value>=7 && value<=10){
                        currentTabletsOfContinuationRH.getRadioGroup().getButtons().get(1).setChecked(true);
                        currentTabletsOfContinuationE.getRadioGroup().getButtons().get(1).setChecked(true);
                        currentTabletsofRHZ.getRadioGroup().getButtons().get(1).setChecked(true);
                        currentTabletsofE.getRadioGroup().getButtons().get(1).setChecked(true);

                    }
                    else if(value>=11 && value<=14){
                        currentTabletsOfContinuationRH.getRadioGroup().getButtons().get(2).setChecked(true);
                        currentTabletsOfContinuationE.getRadioGroup().getButtons().get(2).setChecked(true);
                        currentTabletsofRHZ.getRadioGroup().getButtons().get(2).setChecked(true);
                        currentTabletsofE.getRadioGroup().getButtons().get(2).setChecked(true);

                    }
                    else if(value>=15 && value<=19){
                        currentTabletsOfContinuationRH.getRadioGroup().getButtons().get(3).setChecked(true);
                        currentTabletsOfContinuationE.getRadioGroup().getButtons().get(3).setChecked(true);
                        currentTabletsofRHZ.getRadioGroup().getButtons().get(3).setChecked(true);
                        currentTabletsofE.getRadioGroup().getButtons().get(3).setChecked(true);

                    }
                    else if(value>=20 && value<=24){
                        currentTabletsOfContinuationRH.getRadioGroup().getButtons().get(4).setChecked(true);
                        currentTabletsOfContinuationE.getRadioGroup().getButtons().get(3).setChecked(true);
                        currentTabletsofRHZ.getRadioGroup().getButtons().get(4).setChecked(true);
                        currentTabletsofE.getRadioGroup().getButtons().get(3).setChecked(true);
                    }
                    else if(value>=25){
                        typeFixedDosePrescribedIntensive.getSpinner().selectValue(getResources().getString(R.string.ctb_adult_formulation));
                        typeFixedDosePrescribedContinuation.getSpinner().selectValue(getResources().getString(R.string.ctb_adult_formulation_continuation_rh));
                    }


                    //NEW FORMULATION
                    if(value>=4 && value<=7){
                        newTabletsofRHZ.getRadioGroup().getButtons().get(0).setChecked(true);
                        newTabletsofE.getRadioGroup().getButtons().get(0).setChecked(true);
                        newTabletsOfContinuationRH.getRadioGroup().getButtons().get(0).setChecked(true);
                        newTabletsOfContinuationE.getRadioGroup().getButtons().get(0).setChecked(true);
                    }
                    else if(value>=8 && value<=11){
                        newTabletsofRHZ.getRadioGroup().getButtons().get(1).setChecked(true);
                        newTabletsofE.getRadioGroup().getButtons().get(1).setChecked(true);
                        newTabletsOfContinuationRH.getRadioGroup().getButtons().get(1).setChecked(true);
                        newTabletsOfContinuationE.getRadioGroup().getButtons().get(1).setChecked(true);
                    }
                    else if(value>=12 && value<=15){
                        newTabletsofRHZ.getRadioGroup().getButtons().get(2).setChecked(true);
                        newTabletsofE.getRadioGroup().getButtons().get(2).setChecked(true);
                        newTabletsOfContinuationRH.getRadioGroup().getButtons().get(2).setChecked(true);
                        newTabletsOfContinuationE.getRadioGroup().getButtons().get(2).setChecked(true);
                    }
                    else if(value>=16 && value<=24){
                        newTabletsofRHZ.getRadioGroup().getButtons().get(3).setChecked(true);
                        newTabletsofE.getRadioGroup().getButtons().get(3).setChecked(true);
                        newTabletsOfContinuationRH.getRadioGroup().getButtons().get(3).setChecked(true);
                        newTabletsOfContinuationE.getRadioGroup().getButtons().get(3).setChecked(true);
                    }
                    else if(value>=25){
                        typeFixedDosePrescribedIntensive.getSpinner().selectValue(getResources().getString(R.string.ctb_adult_formulation));
                        typeFixedDosePrescribedContinuation.getSpinner().selectValue(getResources().getString(R.string.ctb_adult_formulation_continuation_rh));
                    }



                    //ADULT FORMULATION
                    if(value>=26 && value<=29){
                        adultFormulationofHRZE.getRadioGroup().getButtons().get(1).setChecked(true);
                    }
                    else if(value>=30 && value<=39){
                        adultFormulationOfContinuationRHE.getRadioGroup().getButtons().get(0).setChecked(true);
                        adultFormulationOfContinuationRH.getRadioGroup().getButtons().get(0).setChecked(true);
                        adultFormulationofHRZE.getRadioGroup().getButtons().get(1).setChecked(true);
                    }
                    else if(value>=40 && value<=54){
                        adultFormulationOfContinuationRHE.getRadioGroup().getButtons().get(1).setChecked(true);
                        adultFormulationOfContinuationRH.getRadioGroup().getButtons().get(1).setChecked(true);
                        adultFormulationofHRZE.getRadioGroup().getButtons().get(2).setChecked(true);
                    }
                    else if(value>=55 && value<=70){
                        adultFormulationOfContinuationRHE.getRadioGroup().getButtons().get(2).setChecked(true);
                        adultFormulationOfContinuationRH.getRadioGroup().getButtons().get(2).setChecked(true);
                        adultFormulationofHRZE.getRadioGroup().getButtons().get(3).setChecked(true);
                    }
                    else if(value>70){
                        adultFormulationOfContinuationRHE.getRadioGroup().getButtons().get(2).setChecked(true);
                        adultFormulationOfContinuationRH.getRadioGroup().getButtons().get(2).setChecked(true);
                        adultFormulationofHRZE.getRadioGroup().getButtons().get(4).setChecked(true);
                    }
                }
            }
        });

        resetViews();

    }

    public void updateFollowUpMonth() {

        String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "REGISTRATION DATE");
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

        Calendar maxDateCalender = formDateCalendar.getInstance();
        maxDateCalender.setTime(formDateCalendar.getTime());
        maxDateCalender.add(Calendar.YEAR, 2);

        String personDOB = App.getPatient().getPerson().getBirthdate();
        String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "REGISTRATION DATE");
        if(treatmentDate != null){
            treatDateCalender = App.getCalendar(App.stringToDate(treatmentDate, "yyyy-MM-dd"));
        }
        Date date = new Date();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();

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
            }else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            Calendar requiredDate = formDateCalendar.getInstance();
            requiredDate.setTime(formDateCalendar.getTime());
            requiredDate.add(Calendar.DATE, 30);

            if (requiredDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                thirdDateCalendar.setTime(requiredDate.getTime());
            } else {
                requiredDate.add(Calendar.DATE, 1);
                thirdDateCalendar.setTime(requiredDate.getTime());
            }

        } if (!treatmentInitiationDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString())) {


            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else {
                treatmentInitiationDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }
        }
        String nextAppointmentDateString = App.getSqlDate(thirdDateCalendar);
        Date nextAppointmentDate = App.stringToDate(nextAppointmentDateString, "yyyy-MM-dd");

        String formDateString = App.getSqlDate(formDateCalendar);
        Date formStDate = App.stringToDate(formDateString, "yyyy-MM-dd");


        if (!(returnVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString()))) {
            Calendar dateToday = Calendar.getInstance();
            dateToday.add(Calendar.MONTH, 24);

            String formDa = returnVisitDate.getButton().getText().toString();

            if (thirdDateCalendar.before(formDateCalendar)) {

                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());

            }
            else if(thirdDateCalendar.after(dateToday)){
                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_date_cant_be_greater_than_24_months), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
            }

            else if(nextAppointmentDate.compareTo(formStDate) == 0){
                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_start_date_and_next_visit_date_cant_be_same), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
            }
            else
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());

        }
        formDate.getButton().setEnabled(true);
        treatmentInitiationDate.getButton().setEnabled(true);
        returnVisitDate.getButton().setEnabled(true);
        updateFollowUpMonth();

    }

    @Override
    public boolean validate() {
        Boolean error = false;
        if (tbRegisterationNumber.getVisibility() == View.VISIBLE) {
            if(App.get(tbRegisterationNumber).isEmpty() ){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                tbRegisterationNumber.getEditText().setError(getString(R.string.empty_field));
                tbRegisterationNumber.getEditText().requestFocus();
                error = true;
            }
            else if(App.get(tbRegisterationNumber).trim().length() <= 0){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                tbRegisterationNumber.getEditText().setError(getString(R.string.ctb_spaces_only));
                tbRegisterationNumber.getEditText().requestFocus();
                error = true;
            }
        }
        if(intensivePhaseRegimen.getVisibility()==View.VISIBLE) {
            if ((App.get(intensivePhaseRegimen).isEmpty())) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                intensivePhaseRegimen.getRadioGroup().getButtons().get(1).setError(getString(R.string.empty_field));
                intensivePhaseRegimen.getRadioGroup().requestFocus();
                error = true;
            }
        }
        if(currentTabletsofRHZ.getVisibility()==View.VISIBLE) {
            if ((App.get(currentTabletsofRHZ).isEmpty())) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                currentTabletsofRHZ.getQuestionView().setError(getString(R.string.empty_field));
                currentTabletsofRHZ.getRadioGroup().requestFocus();
                error = true;
            }
        }
        if(currentTabletsofE.getVisibility()==View.VISIBLE) {
            if ((App.get(currentTabletsofE).isEmpty())) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                currentTabletsofE.getQuestionView().setError(getString(R.string.empty_field));
                currentTabletsofE.getRadioGroup().requestFocus();
                error = true;
            }
        }
        if(newTabletsofRHZ.getVisibility()==View.VISIBLE) {
            if ((App.get(newTabletsofRHZ).isEmpty())) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                newTabletsofRHZ.getQuestionView().setError(getString(R.string.empty_field));
                newTabletsofRHZ.getRadioGroup().requestFocus();
                error = true;
            }
        }
        if(newTabletsofE.getVisibility()==View.VISIBLE) {
            if ((App.get(newTabletsofE).isEmpty())) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                newTabletsofE.getQuestionView().setError(getString(R.string.empty_field));
                newTabletsofE.getRadioGroup().requestFocus();
                error = true;
            }
        }
        if(adultFormulationofHRZE.getVisibility()==View.VISIBLE) {
            if ((App.get(adultFormulationofHRZE).isEmpty())) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                adultFormulationofHRZE.getQuestionView().setError(getString(R.string.empty_field));
                adultFormulationofHRZE.getRadioGroup().requestFocus();
                error = true;
            }
        }
        if(continuationPhaseRegimen.getVisibility()==View.VISIBLE) {
            if ((App.get(continuationPhaseRegimen).isEmpty())) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                continuationPhaseRegimen.getQuestionView().setError(getString(R.string.empty_field));
                continuationPhaseRegimen.getRadioGroup().requestFocus();
                error = true;
            }
        }
        if(currentTabletsOfContinuationRH.getVisibility()==View.VISIBLE) {
            if ((App.get(currentTabletsOfContinuationRH).isEmpty())) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                currentTabletsOfContinuationRH.getQuestionView().setError(getString(R.string.empty_field));
                currentTabletsOfContinuationRH.getRadioGroup().requestFocus();
                error = true;
            }
        }
        if(currentTabletsOfContinuationE.getVisibility()==View.VISIBLE) {
            if ((App.get(currentTabletsOfContinuationE).isEmpty())) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                currentTabletsOfContinuationE.getQuestionView().setError(getString(R.string.empty_field));
                currentTabletsOfContinuationE.getRadioGroup().requestFocus();
                error = true;
            }
        }
        if(newTabletsOfContinuationRH.getVisibility()==View.VISIBLE) {
            if ((App.get(newTabletsOfContinuationRH).isEmpty())) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                newTabletsOfContinuationRH.getQuestionView().setError(getString(R.string.empty_field));
                newTabletsOfContinuationRH.getRadioGroup().requestFocus();
                error = true;
            }
        }
        if(newTabletsOfContinuationE.getVisibility()==View.VISIBLE) {
            if ((App.get(newTabletsOfContinuationE).isEmpty())) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                newTabletsOfContinuationE.getQuestionView().setError(getString(R.string.empty_field));
                newTabletsOfContinuationE.getRadioGroup().requestFocus();
                error = true;
            }
        }
        if(adultFormulationOfContinuationRHE.getVisibility()==View.VISIBLE) {
            if ((App.get(adultFormulationOfContinuationRHE).isEmpty())) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                adultFormulationOfContinuationRHE.getQuestionView().setError(getString(R.string.empty_field));
                adultFormulationOfContinuationRHE.getRadioGroup().requestFocus();
                error = true;
            }
        }
        if(adultFormulationOfContinuationRH.getVisibility()==View.VISIBLE) {
            if ((App.get(adultFormulationOfContinuationRH).isEmpty())) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                adultFormulationOfContinuationRH.getQuestionView().setError(getString(R.string.empty_field));
                adultFormulationOfContinuationRH.getRadioGroup().requestFocus();
                error = true;
            }
        }
        if((App.get(patientCategory).isEmpty())){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            patientCategory.getQuestionView().setError(getString(R.string.empty_field));
            patientCategory.getRadioGroup().requestFocus();
            error = true;
        }
        if((App.get(treatmentPlan).isEmpty())){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            treatmentPlan.getQuestionView().setError(getString(R.string.empty_field));
            treatmentPlan.getRadioGroup().requestFocus();
            error = true;
        }
        if((App.get(conclusionOfTreatment).isEmpty())){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            conclusionOfTreatment.getQuestionView().setError(getString(R.string.empty_field));
            conclusionOfTreatment.getRadioGroup().requestFocus();
            error = true;
        }
        if((App.get(weight).isEmpty())){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            weight.getEditText().setError(getString(R.string.empty_field));
            weight.getEditText().requestFocus();
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
        if (patientType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TB PATIENT TYPE", App.get(patientType).equals(getResources().getString(R.string.ctb_new)) ? "NEW TB PATIENT" :
                    (App.get(patientType).equals(getResources().getString(R.string.ctb_relapse)) ? "RELAPSE" :
                            (App.get(patientType).equals(getResources().getString(R.string.ctb_referred_transferred)) ? "PATIENT REFERRED" :
                                    (App.get(patientType).equals(getResources().getString(R.string.ctb_treatment_after_followup)) ? "LOST TO FOLLOW-UP" :
                                            (App.get(patientType).equals(getResources().getString(R.string.ctb_treatment_failure)) ? "TUBERCULOSIS TREATMENT FAILURE" : "OTHER PATIENT TYPE"))))});

        if(App.hasKeyListener(tbRegisterationNumber)) {
            observations.add(new String[]{"TB REGISTRATION NUMBER", App.get(tbRegisterationNumber)});
        }
        observations.add(new String[]{"TREATMENT START DATE", App.getSqlDateTime(secondDateCalendar)});
        observations.add(new String[]{"FOLLOW-UP MONTH", App.get(monthTreatment)});

        if (patientCategory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TB CATEGORY", App.get(patientCategory).equals(getResources().getString(R.string.ctb_categoryI)) ? "CATEGORY I TUBERCULOSIS" :
                    App.get(patientCategory).equals(getResources().getString(R.string.ctb_categoryII)) ? "CATEGORY II TUBERCULOSIS"
                            : "CATEGORY III TUBERCULOSIS"});



        if(weight.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"WEIGHT (KG)", App.get(weight)});
        }

        observations.add(new String[]{"TREATMENT PLAN", App.get(treatmentPlan).equals(getResources().getString(R.string.ctb_intensive_phase)) ? "INTENSIVE PHASE" :
                App.get(treatmentPlan).equals(getResources().getString(R.string.ctb_continuation_phase)) ? "CONTINUE REGIMEN"
                        : "TREATMENT COMPLETE"});

        if(intensivePhaseRegimen.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"REGIMEN", App.get(intensivePhaseRegimen).equals(getResources().getString(R.string.ctb_rhze)) ? "RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS" : "RIFAMPICIN/ISONIAZID/PYRAZINAMIDE"});
        }

        if(typeFixedDosePrescribedIntensive.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"PAEDIATRIC DOSE COMBINATION", App.get(typeFixedDosePrescribedIntensive).equals(getResources().getString(R.string.ctb_current_formulation)) ? "CURRENT FORULATION" :
                    App.get(typeFixedDosePrescribedIntensive).equals(getResources().getString(R.string.ctb_new_formulation)) ? "NEW FORMULATION"
                            : "ADULT FORMULATION"});
        }
        if(currentTabletsofRHZ.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"CURRENT FORMULATION OF TABLETS OF RHZ", App.get(currentTabletsofRHZ)});
        }
        if(currentTabletsofE.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"CURRENT FORMULATION OF TABLETS OF  E", App.get(currentTabletsofE)});
        }
        if(newTabletsofE.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"NEW FORMULATION OF TABLETS OF E", App.get(newTabletsofE)});
        }
        if(newTabletsofRHZ.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"NEW FORMULATION OF TABLETS OF RHZ", App.get(newTabletsofRHZ)});
        }
        if(adultFormulationofHRZE.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"ADULT FORMULATION OF TABLETS OF RHZE", App.get(adultFormulationofHRZE)});
        }
        if(continuationPhaseRegimen.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"REGIMEN", App.get(intensivePhaseRegimen).equals(getResources().getString(R.string.ctb_rh)) ? "RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS" : "RIFAMPICIN/ISONIAZID/PYRAZINAMIDE"});
        }
        if(typeFixedDosePrescribedContinuation.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"PAEDIATRIC FIXED DOSE COMBINATION FOR CONTINUATION PHASE", App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_current_formulation_continuation)) ? "CURRENT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE" :
                    App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_new_formulation_continuation)) ? "NEW FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE" :
                    App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_adult_formulation_continuation_rhe)) ? "ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE"
                            : "ADULT FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE"});
        }
        if(currentTabletsOfContinuationRH.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"CURRENT FORMULATION OF TABLETS OF RH", App.get(currentTabletsOfContinuationRH)});
        }
        if(currentTabletsOfContinuationE.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"CURRENT FORMULATION OF TABLETS OF E FOR CONTINUATION PHASE", App.get(currentTabletsOfContinuationE)});
        }
        if(newTabletsOfContinuationRH.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"NEW FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE", App.get(newTabletsOfContinuationRH)});
        }
        if(newTabletsOfContinuationE.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"NEW FORMULATION OF TABLET OF E FOR CONTINUATION PHASE", App.get(newTabletsOfContinuationE)});
        }
        if(adultFormulationOfContinuationRH.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"ADULT FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE", App.get(adultFormulationOfContinuationRH)});
        }
        if(adultFormulationOfContinuationRHE.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE", App.get(adultFormulationOfContinuationRHE)});
        }
        observations.add(new String[]{"CONCLUSION OF TREATMENT FOLLOW UP", App.get(conclusionOfTreatment).toUpperCase()});
        observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDateTime(thirdDateCalendar)});
        if(!App.get(doctorNotes).isEmpty()){
            observations.add(new String[]{"CLINICIAN NOTES (TEXT)", App.get(doctorNotes)});
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

                String result = serverService.saveEncounterAndObservation("TB Treatment Followup", FORM, formDateCalendar, observations.toArray(new String[][]{}),false);
                if (result.contains("SUCCESS"))
                    return "SUCCESS";

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
    public void refill(int formId) {
        OfflineForm fo = serverService.getOfflineFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {
            String[][] obs = obsValue.get(i);
            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("TB PATIENT TYPE")) {
                String value = obs[0][1].equals("NEW TB PATIENT") ? getResources().getString(R.string.ctb_new) :
                        (obs[0][1].equals("RELAPSE") ? getResources().getString(R.string.ctb_relapse) :
                                (obs[0][1].equals("PATIENT REFERRED") ? getResources().getString(R.string.ctb_referred_transferred) :
                                        (obs[0][1].equals("LOST TO FOLLOW-UP") ? getResources().getString(R.string.ctb_treatment_after_followup) :
                                                (obs[0][1].equals("TUBERCULOSIS TREATMENT FAILURE") ? getResources().getString(R.string.ctb_treatment_failure) :
                                                        getResources().getString(R.string.ctb_other_title)))));

                patientType.getSpinner().selectValue(value);
                patientType.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TB REGISTRATION NUMBER")) {
                tbRegisterationNumber.getEditText().setText(obs[0][1]);
                tbRegisterationNumber.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("TREATMENT START DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                treatmentInitiationDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
                treatmentInitiationDate.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                monthTreatment.getSpinner().selectValue(obs[0][1]);
                monthTreatment.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("TB CATEGORY")) {

                for (RadioButton rb : patientCategory.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_categoryI)) && obs[0][1].equals("CATEGORY I TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_categoryII)) && obs[0][1].equals("CATEGORY II TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_category_3)) && obs[0][1].equals("CATEGORY III TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                patientCategory.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("WEIGHT (KG)")) {
                weight.getEditText().setText(obs[0][1]);
                weight.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("TREATMENT PLAN")) {
                for (RadioButton rb : treatmentPlan.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_intensive_phase)) && obs[0][1].equals("INTENSIVE PHASE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_continuation_phase)) && obs[0][1].equals("CONTINUE REGIMEN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_end_treatment)) && obs[0][1].equals("TREATMENT COMPLETE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                treatmentPlan.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("REGIMEN")) {
                if(App.get(treatmentPlan).equals(getResources().getString(R.string.ctb_intensive_phase))) {
                    for (RadioButton rb : intensivePhaseRegimen.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_rhze)) && obs[0][1].equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_rhz)) && obs[0][1].equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    intensivePhaseRegimen.setVisibility(View.VISIBLE);
                }
            }else if (obs[0][0].equals("PAEDIATRIC DOSE COMBINATION")) {
                String value = obs[0][1].equals("CURRENT FORULATION") ? getResources().getString(R.string.ctb_current_formulation) :
                        (obs[0][1].equals("NEW FORMULATION") ? getResources().getString(R.string.ctb_new_formulation) :
                                getResources().getString(R.string.ctb_adult_formulation));

                typeFixedDosePrescribedIntensive.getSpinner().selectValue(value);
                typeFixedDosePrescribedIntensive.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("CURRENT FORMULATION OF TABLETS OF RHZ")) {
                for (RadioButton rb : currentTabletsofRHZ.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && obs[0][1].equals("3")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && obs[0][1].equals("4")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_5)) && obs[0][1].equals("5")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                currentTabletsofRHZ.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("CURRENT FORMULATION OF TABLETS OF E")) {
                for (RadioButton rb : currentTabletsofE.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && obs[0][1].equals("3")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && obs[0][1].equals("4")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                currentTabletsofE.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("NEW FORMULATION OF TABLETS OF E")) {
                for (RadioButton rb : newTabletsofE.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && obs[0][1].equals("3")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && obs[0][1].equals("4")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                newTabletsofE.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("NEW FORMULATION OF TABLETS OF RHZ")) {
                for (RadioButton rb : newTabletsofRHZ.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && obs[0][1].equals("3")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && obs[0][1].equals("4")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                newTabletsofRHZ.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("ADULT FORMULATION OF TABLETS OF RHZE")) {
                for (RadioButton rb : adultFormulationofHRZE.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && obs[0][1].equals("3")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && obs[0][1].equals("4")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_5)) && obs[0][1].equals("5")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                adultFormulationofHRZE.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("REGIMEN")) {
                if(App.get(treatmentPlan).equals(getResources().getString(R.string.ctb_continuation_phase))) {
                    for (RadioButton rb : continuationPhaseRegimen.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_rh)) && obs[0][1].equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_rhe)) && obs[0][1].equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    continuationPhaseRegimen.setVisibility(View.VISIBLE);
                }
            }else if (obs[0][0].equals("PAEDIATRIC FIXED DOSE COMBINATION FOR CONTINUATION PHASE")) {
                String value = obs[0][1].equals("CURRENT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_current_formulation_continuation) :
                        (obs[0][1].equals("NEW FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_new_formulation_continuation) :
                                (obs[0][1].equals("ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_adult_formulation_continuation_rhe) :
                                    getResources().getString(R.string.ctb_adult_formulation_continuation_rh)));

                typeFixedDosePrescribedContinuation.getSpinner().selectValue(value);
                typeFixedDosePrescribedContinuation.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("CURRENT FORMULATION OF TABLETS OF RH")) {
                for (RadioButton rb : currentTabletsOfContinuationRH.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && obs[0][1].equals("3")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && obs[0][1].equals("4")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_5)) && obs[0][1].equals("5")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                currentTabletsOfContinuationRH.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("CURRENT FORMULATION OF TABLETS OF E FOR CONTINUATION PHASE")) {
                for (RadioButton rb : currentTabletsOfContinuationE.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && obs[0][1].equals("3")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && obs[0][1].equals("4")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                currentTabletsOfContinuationE.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("NEW FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE")) {
                for (RadioButton rb : newTabletsOfContinuationRH.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && obs[0][1].equals("3")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && obs[0][1].equals("4")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                newTabletsOfContinuationRH.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("NEW FORMULATION OF TABLET OF E FOR CONTINUATION PHASE")) {
                for (RadioButton rb : newTabletsOfContinuationE.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && obs[0][1].equals("3")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && obs[0][1].equals("4")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                newTabletsOfContinuationE.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("ADULT FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE")) {
                for (RadioButton rb : adultFormulationOfContinuationRH.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_1_and_half)) && obs[0][1].equals("1.5")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                adultFormulationOfContinuationRH.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE")) {
                for (RadioButton rb : adultFormulationOfContinuationRHE.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && obs[0][1].equals("3")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && obs[0][1].equals("4")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                adultFormulationOfContinuationRHE.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("CONCLUSION OF TREATMENT FOLLOW UP")) {
                for (RadioButton rb : conclusionOfTreatment.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_improved)) && obs[0][1].equals("IMPROVED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no_improvement)) && obs[0][1].equals("NO IMPROVEMENT")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                conclusionOfTreatment.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String thirdDate = obs[0][1];
                thirdDateCalendar.setTime(App.stringToDate(thirdDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
                returnVisitDate.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
                doctorNotes.getEditText().setText(obs[0][1]);
                doctorNotes.setVisibility(View.VISIBLE);
            }}
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
        if (view == treatmentInitiationDate.getButton()) {
            treatmentInitiationDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        }
        if (view == returnVisitDate.getButton()) {
            returnVisitDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", false);
            args.putBoolean("allowFutureDate", true);
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
        MySpinner spinner = (MySpinner) parent;
        if (spinner == typeFixedDosePrescribedIntensive.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_current_formulation))) {
                if (!App.get(intensivePhaseRegimen).equals(getResources().getString(R.string.ctb_rhz))) {
                    currentTabletsofE.setVisibility(View.VISIBLE);
                }
                currentTabletsofRHZ.setVisibility(View.VISIBLE);

                newTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                adultFormulationofHRZE.setVisibility(View.GONE);
            } else if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_new_formulation))) {
                if (!App.get(intensivePhaseRegimen).equals(getResources().getString(R.string.ctb_rhz))) {
                    newTabletsofE.setVisibility(View.VISIBLE);
                }
                newTabletsofRHZ.setVisibility(View.VISIBLE);

                currentTabletsofE.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
                adultFormulationofHRZE.setVisibility(View.GONE);
            } else if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_adult_formulation))) {
                if (!App.get(intensivePhaseRegimen).equals(getResources().getString(R.string.ctb_rhz))) {
                    adultFormulationofHRZE.setVisibility(View.VISIBLE);
                }
                newTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
            }
        }else if (spinner == typeFixedDosePrescribedContinuation.getSpinner()) {
                if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_current_formulation_continuation))) {
                    if (App.get(continuationPhaseRegimen).equals(getResources().getString(R.string.ctb_rhe)) || App.get(continuationPhaseRegimen).isEmpty()) {
                        currentTabletsOfContinuationE.setVisibility(View.VISIBLE);
                    }
                    currentTabletsOfContinuationRH.setVisibility(View.VISIBLE);

                    newTabletsOfContinuationE.setVisibility(View.GONE);
                    newTabletsOfContinuationRH.setVisibility(View.GONE);
                    adultFormulationOfContinuationRH.setVisibility(View.GONE);
                    adultFormulationOfContinuationRHE.setVisibility(View.GONE);
                } else if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_new_formulation_continuation))) {
                    if (App.get(continuationPhaseRegimen).equals(getResources().getString(R.string.ctb_rhe)) || App.get(continuationPhaseRegimen).isEmpty()) {
                        newTabletsOfContinuationE.setVisibility(View.VISIBLE);
                    }
                    newTabletsOfContinuationRH.setVisibility(View.VISIBLE);

                    currentTabletsOfContinuationE.setVisibility(View.GONE);
                    currentTabletsOfContinuationRH.setVisibility(View.GONE);
                    adultFormulationOfContinuationRH.setVisibility(View.GONE);
                    adultFormulationOfContinuationRHE.setVisibility(View.GONE);
                }else if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_adult_formulation_continuation_rh))) {
                    adultFormulationOfContinuationRH.setVisibility(View.VISIBLE);

                    currentTabletsOfContinuationE.setVisibility(View.GONE);
                    currentTabletsOfContinuationRH.setVisibility(View.GONE);
                    newTabletsOfContinuationE.setVisibility(View.GONE);
                    newTabletsOfContinuationRH.setVisibility(View.GONE);
                    adultFormulationOfContinuationRHE.setVisibility(View.GONE);
                }else if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_adult_formulation_continuation_rhe))) {
                    adultFormulationOfContinuationRHE.setVisibility(View.VISIBLE);

                    currentTabletsOfContinuationE.setVisibility(View.GONE);
                    currentTabletsOfContinuationRH.setVisibility(View.GONE);
                    newTabletsOfContinuationE.setVisibility(View.GONE);
                    newTabletsOfContinuationRH.setVisibility(View.GONE);
                    adultFormulationOfContinuationRH.setVisibility(View.GONE);
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

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        treatmentInitiationDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        intensivePhaseRegimen.setVisibility(View.GONE);
        typeFixedDosePrescribedIntensive.setVisibility(View.GONE);
        currentTabletsofRHZ.setVisibility(View.GONE);
        currentTabletsofE.setVisibility(View.GONE);
        newTabletsofRHZ.setVisibility(View.GONE);
        newTabletsofE.setVisibility(View.GONE);
        adultFormulationofHRZE.setVisibility(View.GONE);
        continuationPhaseRegimen.setVisibility(View.GONE);
        typeFixedDosePrescribedContinuation.setVisibility(View.GONE);
        currentTabletsOfContinuationRH.setVisibility(View.GONE);
        currentTabletsOfContinuationE.setVisibility(View.GONE);
        newTabletsOfContinuationRH.setVisibility(View.GONE);
        newTabletsOfContinuationE.setVisibility(View.GONE);
        adultFormulationOfContinuationRH.setVisibility(View.GONE);
        adultFormulationOfContinuationRHE.setVisibility(View.GONE);

        String tbRegistrationNumber = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "TB REGISTRATION NUMBER");
        if(tbRegistrationNumber!=null){
            tbRegisterationNumber.getEditText().setKeyListener(null);
            tbRegisterationNumber.getEditText().setText(tbRegistrationNumber);
        }
        String patientTypeString = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "TB PATIENT TYPE");

        if(patientTypeString!=null) {
            if (patientTypeString.equals("NEW TB PATIENT")) {
                patientType.getSpinner().selectValue(getResources().getString(R.string.ctb_new));
            } else if (patientTypeString.equals("RELAPSE")) {
                patientType.getSpinner().selectValue(getResources().getString(R.string.ctb_relapse));
            } else if (patientTypeString.equals("PATIENT REFERRED")) {
                patientType.getSpinner().selectValue(getResources().getString(R.string.ctb_referred_transferred));
            } else if (patientTypeString.equals("LOST TO FOLLOW-UP")) {
                patientType.getSpinner().selectValue(getResources().getString(R.string.ctb_treatment_after_followup));
            } else if (patientTypeString.equals("TUBERCULOSIS TREATMENT FAILURE")) {
                patientType.getSpinner().selectValue(getResources().getString(R.string.ctb_treatment_failure));
            } else if (patientTypeString.equals("OTHER PATIENT TYPE")) {
                patientType.getSpinner().selectValue(getResources().getString(R.string.ctb_other_title));
            }
        }
        String startDate = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "IPT START DATE");
        String format = "";
        if(startDate!=null) {
            if (startDate.contains("/")) {
                format = "dd/MM/yyyy";
            } else {
                format = "yyyy-MM-dd";
            }
            secondDateCalendar.setTime(App.stringToDate(startDate, format));
            thirdDateCalendar.set(Calendar.YEAR, secondDateCalendar.get(Calendar.YEAR));
            thirdDateCalendar.set(Calendar.DAY_OF_MONTH, secondDateCalendar.get(Calendar.DAY_OF_MONTH));
            thirdDateCalendar.set(Calendar.MONTH, secondDateCalendar.get(Calendar.MONTH));
            thirdDateCalendar.add(Calendar.DAY_OF_MONTH, 30);
            treatmentInitiationDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
            returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());


        }else{
            treatmentInitiationDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
            thirdDateCalendar.set(Calendar.YEAR, secondDateCalendar.get(Calendar.YEAR));
            thirdDateCalendar.set(Calendar.DAY_OF_MONTH, secondDateCalendar.get(Calendar.DAY_OF_MONTH));
            thirdDateCalendar.set(Calendar.MONTH, secondDateCalendar.get(Calendar.MONTH));
            thirdDateCalendar.add(Calendar.DAY_OF_MONTH, 30);
            returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
        }
        String patientCategoryString = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "TB CATEGORY");
        if(patientCategoryString!=null) {
            for (RadioButton rb : patientCategory.getRadioGroup().getButtons()) {
                if (rb.getText().equals(getResources().getString(R.string.ctb_categoryI)) && patientCategoryString.equals("CATEGORY I TUBERCULOSIS")) {
                    rb.setChecked(true);
                    break;
                } else if (rb.getText().equals(getResources().getString(R.string.ctb_categoryII)) && patientCategoryString.equals("CATEGORY II TUBERCULOSIS")) {
                    rb.setChecked(true);
                    break;
                } else if (rb.getText().equals(getResources().getString(R.string.ctb_category_3)) && patientCategoryString.equals("CATEGORY III TUBERCULOSIS")) {
                    rb.setChecked(true);
                    break;
                }
            }
        }
        thirdDateCalendar.set(Calendar.YEAR, formDateCalendar.get(Calendar.YEAR));
        thirdDateCalendar.set(Calendar.DAY_OF_MONTH, formDateCalendar.get(Calendar.DAY_OF_MONTH));
        thirdDateCalendar.set(Calendar.MONTH, formDateCalendar.get(Calendar.MONTH));
        thirdDateCalendar.add(Calendar.DAY_OF_MONTH, 30);
        returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());


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

    @SuppressLint("ValidFragment")
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
            if (!getArguments().getBoolean("allowFutureDate", false))
                dialog.getDatePicker().setMaxDate(new Date().getTime());
            if (!getArguments().getBoolean("allowPastDate", false))
                dialog.getDatePicker().setMinDate(new Date().getTime());
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            if (((int) view.getTag()) == DATE_DIALOG_ID)
                formDateCalendar.set(yy, mm, dd);
            else if (((int) view.getTag()) == SECOND_DATE_DIALOG_ID)
                secondDateCalendar.set(yy, mm, dd);
            else if(((int) view.getTag()) == THIRD_DATE_DIALOG_ID)
                thirdDateCalendar.set(yy, mm, dd);
            updateDisplay();

        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            updateDisplay();
        }

    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == patientCategory.getRadioGroup()) {
            patientCategory.getRadioGroup().getButtons().get(2).setError(null);
            if (patientCategory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_category_3))) {
                intensivePhaseRegimen.getRadioGroup().getButtons().get(1).setChecked(true);
            }
        }else if(group == treatmentPlan.getRadioGroup()){
            treatmentPlan.getRadioGroup().getButtons().get(2).setError(null);
            if (treatmentPlan.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_intensive_phase))) {
                intensivePhaseRegimen.setVisibility(View.VISIBLE);
                intensivePhaseRegimen.getRadioGroup().clearCheck();
                typeFixedDosePrescribedIntensive.setVisibility(View.VISIBLE);
                typeFixedDosePrescribedIntensive.getSpinner().selectValue(getResources().getString(R.string.ctb_current_formulation));
                currentTabletsofE.setVisibility(View.VISIBLE);
                currentTabletsofRHZ.setVisibility(View.VISIBLE);

                continuationPhaseRegimen.setVisibility(View.GONE);
                typeFixedDosePrescribedContinuation.setVisibility(View.GONE);
                currentTabletsOfContinuationRH.setVisibility(View.GONE);
                currentTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationRH.setVisibility(View.GONE);
                newTabletsOfContinuationE.setVisibility(View.GONE);
                adultFormulationOfContinuationRHE.setVisibility(View.GONE);
                adultFormulationOfContinuationRH.setVisibility(View.GONE);
            }else if(treatmentPlan.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_continuation_phase))){
                continuationPhaseRegimen.setVisibility(View.VISIBLE);
                continuationPhaseRegimen.getRadioGroup().clearCheck();
                typeFixedDosePrescribedContinuation.setVisibility(View.VISIBLE);
                typeFixedDosePrescribedContinuation.getSpinner().selectValue(getResources().getString(R.string.ctb_current_formulation_continuation));
                currentTabletsOfContinuationRH.setVisibility(View.VISIBLE);
                currentTabletsOfContinuationE.setVisibility(View.VISIBLE);

                intensivePhaseRegimen.setVisibility(View.GONE);
                typeFixedDosePrescribedIntensive.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                newTabletsofE.setVisibility(View.GONE);
                adultFormulationofHRZE.setVisibility(View.GONE);
            }else if(treatmentPlan.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_end_treatment))){
                intensivePhaseRegimen.setVisibility(View.GONE);
                typeFixedDosePrescribedIntensive.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                newTabletsofE.setVisibility(View.GONE);
                adultFormulationofHRZE.setVisibility(View.GONE);
                continuationPhaseRegimen.setVisibility(View.GONE);
                typeFixedDosePrescribedContinuation.setVisibility(View.GONE);
                currentTabletsOfContinuationRH.setVisibility(View.GONE);
                currentTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationRH.setVisibility(View.GONE);
                newTabletsOfContinuationE.setVisibility(View.GONE);
                adultFormulationOfContinuationRHE.setVisibility(View.GONE);
                adultFormulationOfContinuationRH.setVisibility(View.GONE);
            }
        }else if (group == intensivePhaseRegimen.getRadioGroup()) {
            intensivePhaseRegimen.getRadioGroup().getButtons().get(1).setError(null);
            if (intensivePhaseRegimen.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_rhz))) {
                adultFormulationofHRZE.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                newTabletsofE.setVisibility(View.GONE);
            }
            else{
                if(App.get(typeFixedDosePrescribedIntensive).equals(getResources().getString(R.string.ctb_adult_formulation))) {
                    adultFormulationofHRZE.setVisibility(View.VISIBLE);
                }
                else if(App.get(typeFixedDosePrescribedIntensive).equals(getResources().getString(R.string.ctb_current_formulation))) {
                    currentTabletsofE.setVisibility(View.VISIBLE);
                }
                else if(App.get(typeFixedDosePrescribedIntensive).equals(getResources().getString(R.string.ctb_new_formulation))) {
                    newTabletsofE.setVisibility(View.VISIBLE);
                }
            }
        }else if (group == continuationPhaseRegimen.getRadioGroup()) {
            continuationPhaseRegimen.getRadioGroup().getButtons().get(1).setError(null);
            if (continuationPhaseRegimen.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_rh))) {
                currentTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationE.setVisibility(View.GONE);
            }
            else{
                if(App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_current_formulation_continuation))) {
                    currentTabletsOfContinuationE.setVisibility(View.VISIBLE);
                }
                else if(App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_new_formulation_continuation))) {
                    newTabletsOfContinuationE.setVisibility(View.VISIBLE);
                } else if(App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_adult_formulation_continuation_rh))) {
                    adultFormulationOfContinuationRH.setVisibility(View.VISIBLE);
                }  else if(App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_adult_formulation_continuation_rhe))) {
                    adultFormulationOfContinuationRHE.setVisibility(View.VISIBLE);
                }
            }
        }
        else if (group == conclusionOfTreatment.getRadioGroup()) {
            conclusionOfTreatment.getQuestionView().setError(null);
        }
        else if (group == currentTabletsofRHZ.getRadioGroup()) {
            currentTabletsofRHZ.getQuestionView().setError(null);
        }
        else if (group == currentTabletsofE.getRadioGroup()) {
            currentTabletsofE.getQuestionView().setError(null);
        }
        else if (group == newTabletsofRHZ.getRadioGroup()) {
            newTabletsofRHZ.getQuestionView().setError(null);
        }
        else if (group == newTabletsofE.getRadioGroup()) {
            newTabletsofE.getQuestionView().setError(null);
        }
        else if (group == adultFormulationofHRZE.getRadioGroup()) {
            adultFormulationofHRZE.getQuestionView().setError(null);
        }

        else if (group == currentTabletsOfContinuationRH.getRadioGroup()) {
            currentTabletsOfContinuationRH.getQuestionView().setError(null);
        }
        else if (group == currentTabletsOfContinuationE.getRadioGroup()) {
            currentTabletsOfContinuationE.getQuestionView().setError(null);
        }
        else if (group == newTabletsOfContinuationRH.getRadioGroup()) {
            newTabletsOfContinuationRH.getQuestionView().setError(null);
        }
        else if (group == newTabletsOfContinuationE.getRadioGroup()) {
            newTabletsOfContinuationE.getQuestionView().setError(null);
        }
        else if (group == adultFormulationOfContinuationRHE.getRadioGroup()) {
            adultFormulationOfContinuationRHE.getQuestionView().setError(null);
        }
        else if (group == adultFormulationOfContinuationRH.getRadioGroup()) {
            adultFormulationOfContinuationRH.getQuestionView().setError(null);
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
