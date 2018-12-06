package com.ihsinformatics.gfatmmobile.childhoodtb;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyCheckBox;
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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbAntibioticTrial extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    Boolean dateChoose = false;

    Snackbar snackbar;
    ScrollView scrollView;
    TitledButton formDate;
    TitledEditText weightAtBaseline;
    TitledEditText weightPercentileEditText;
    TitledRadioGroup prescribeAntibiotic;
    TitledEditText antibioticDuration;
    TitledEditText antibioticName;
    TitledEditText doseAntibiotic;
    TitledRadioGroup unitDose;
    TitledRadioGroup prescribingFurtherTest;
    TitledCheckBoxes initiatingAdditionalTreatment;
    TitledEditText otherAdditionalTreatment;
    TitledRadioGroup followupRequired;
    MyTextView moInstruction;
    TitledButton returnVisitDate;


    TitledEditText doctorNotes;


    TitledRadioGroup patientReferred;
    TitledCheckBoxes referredTo;
    TitledCheckBoxes referalReasonPsychologist;
    TitledEditText otherReferalReasonPsychologist;
    TitledCheckBoxes referalReasonSupervisor;
    TitledEditText otherReferalReasonSupervisor;
    TitledCheckBoxes referalReasonCallCenter;
    TitledEditText otherReferalReasonCallCenter;
    TitledCheckBoxes referalReasonClinician;
    TitledEditText otherReferalReasonClinician;


    //Next Appointment


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
        formName = Forms.CHILDHOODTB_ANTIBIOTIC_TRIAL;
        form = Forms.childhoodTb_antibiotic_trial;

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
            for (int i = 0; i < pageCount; i++) {
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

        Toast toast=new Toast(context);

        // first page views...
        formDate = new TitledButton(context, "Section A: TB Treatment Initiation ", getResources().getString(R.string.pet_form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        weightAtBaseline = new TitledEditText(context, null, getResources().getString(R.string.ctb_weight_at_baseline), "", "", 3, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);

        weightPercentileEditText = new TitledEditText(context, null,getResources().getString(R.string.ctb_weight_percentile), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        prescribeAntibiotic = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_prescribing_antibiotic_trial), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL,false);
        antibioticDuration =  new TitledEditText(context, null,getResources().getString(R.string.antibiotic_days), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        antibioticName = new TitledEditText(context, null,getResources().getString(R.string.name_antibiotic), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        doseAntibiotic =  new TitledEditText(context, null,getResources().getString(R.string.dose_antibiotic), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        unitDose = new TitledRadioGroup(context, null, getResources().getString(R.string.unit_dose), getResources().getStringArray(R.array.mg_ml_list), "", App.HORIZONTAL, App.VERTICAL,true);

        prescribingFurtherTest = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_prescribing_further_tests), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL,false);

        initiatingAdditionalTreatment = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_initiating_additional_treatment), getResources().getStringArray(R.array.ctb_pediasure_vitamin_iron_anthelminthic_calpol), null, App.VERTICAL, App.VERTICAL);
        otherAdditionalTreatment = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        followupRequired  = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_followup_required), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.HORIZONTAL,true);
        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.ctb_next_appointment_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        moInstruction = new MyTextView(context,getResources().getString(R.string.ctb_treatment_initiation_mo_instruction));


        patientReferred  = new TitledRadioGroup(context, null, getResources().getString(R.string.refer_patient), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL,false);
        referredTo = new TitledCheckBoxes(context, null, getResources().getString(R.string.refer_patient_to), getResources().getStringArray(R.array.refer_patient_to_option), null, App.VERTICAL, App.VERTICAL, false);
        referalReasonPsychologist = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_psychologist), getResources().getStringArray(R.array.referral_reason_for_psychologist_option), null, App.VERTICAL, App.VERTICAL, false);
        otherReferalReasonPsychologist = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        referalReasonSupervisor = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_supervisor), getResources().getStringArray(R.array.referral_reason_for_supervisor_option), null, App.VERTICAL, App.VERTICAL, false);
        otherReferalReasonSupervisor = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        referalReasonCallCenter = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_center), getResources().getStringArray(R.array.referral_reason_for_call_center_option), null, App.VERTICAL, App.VERTICAL, false);
        otherReferalReasonCallCenter = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        referalReasonClinician = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_clinician), getResources().getStringArray(R.array.referral_reason_for_clinician_option), null, App.VERTICAL, App.VERTICAL, false);
        otherReferalReasonClinician = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);

       doctorNotes = new TitledEditText(context, null, getResources().getString(R.string.ctb_doctor_notes), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL,false);


        views = new View[]{formDate.getButton(),weightAtBaseline.getEditText(),
                initiatingAdditionalTreatment, returnVisitDate.getButton(),
                doctorNotes.getEditText(),weightPercentileEditText.getEditText(),
                otherAdditionalTreatment.getEditText(),
                followupRequired.getRadioGroup(),patientReferred.getRadioGroup(), referalReasonCallCenter, otherReferalReasonCallCenter.getEditText(), referalReasonClinician, otherReferalReasonClinician.getEditText(),
                prescribeAntibiotic.getRadioGroup(),
                antibioticDuration.getEditText(),
                antibioticName.getEditText(),
                doseAntibiotic.getEditText(),
                unitDose.getRadioGroup(),
                prescribingFurtherTest
        };
        viewGroups = new View[][]
                {{
                        formDate,
                        weightAtBaseline,
                        weightPercentileEditText,
                        prescribeAntibiotic,
                        antibioticDuration,
                        antibioticName,
                        doseAntibiotic,
                        unitDose,
                        prescribingFurtherTest,
                        initiatingAdditionalTreatment,
                        otherAdditionalTreatment,
                        followupRequired,
                        returnVisitDate,
                        moInstruction,
                        patientReferred, referredTo, referalReasonPsychologist, otherReferalReasonPsychologist, referalReasonSupervisor, otherReferalReasonSupervisor,
                        referalReasonCallCenter, otherReferalReasonCallCenter, referalReasonClinician, otherReferalReasonClinician,
                        doctorNotes,
                }};
        formDate.getButton().setOnClickListener(this);
        followupRequired.getRadioGroup().setOnCheckedChangeListener(this);
        unitDose.getRadioGroup().setOnCheckedChangeListener(this);

        ArrayList<MyCheckBox> checkBoxList = initiatingAdditionalTreatment.getCheckedBoxes();
        for (CheckBox cb : initiatingAdditionalTreatment.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);


        patientReferred.getRadioGroup().setOnCheckedChangeListener(this);
        for(CheckBox cb: referredTo.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for(CheckBox cb: referalReasonPsychologist.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for(CheckBox cb: referalReasonSupervisor.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for(CheckBox cb: referalReasonClinician.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for(CheckBox cb: referalReasonCallCenter.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        prescribeAntibiotic.setRadioGroupEnabled(false);


        returnVisitDate.getButton().setOnClickListener(this);

        resetViews();

        }

    @Override
    public void updateDisplay() {

        String personDOB = App.getPatient().getPerson().getBirthdate();
        Calendar maxDateCalender = formDateCalendar.getInstance();
        maxDateCalender.setTime(formDateCalendar.getTime());
        maxDateCalender.add(Calendar.YEAR, 2);
        String formDa = formDate.getButton().getText().toString();

        if (snackbar != null)
            snackbar.dismiss();
        Date date = new Date();
        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {


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
        }

        String nextAppointmentDateString = App.getSqlDate(secondDateCalendar);
        Date nextAppointmentDate = App.stringToDate(nextAppointmentDateString, "yyyy-MM-dd");

        String formDateString = App.getSqlDate(formDateCalendar);
        Date formStDate = App.stringToDate(formDateString, "yyyy-MM-dd");


        if (!(returnVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {
            Calendar dateToday = Calendar.getInstance();
            dateToday.add(Calendar.MONTH, 24);

            if (secondDateCalendar.before(formDateCalendar)) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            }
            else if(secondDateCalendar.after(dateToday)){
                secondDateCalendar= App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_date_cant_be_greater_than_24_months), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }

            else if(nextAppointmentDate.compareTo(formStDate) == 0){
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_start_date_and_next_visit_date_cant_be_same), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }
            else
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        }
        formDate.getButton().setEnabled(true);
        returnVisitDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = false;

        if(App.get(unitDose).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            unitDose.getQuestionView().setError(getString(R.string.empty_field));
            unitDose.getRadioGroup().requestFocus();
            error = true;
        }
        if(App.get(antibioticName).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            antibioticName.getEditText().setError(getString(R.string.empty_field));
            antibioticName.requestFocus();
            error = true;
        }
        if(App.get(doseAntibiotic).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            doseAntibiotic.getEditText().setError(getString(R.string.empty_field));
            doseAntibiotic.requestFocus();
            error = true;
        }

        if(followupRequired.getVisibility()==View.VISIBLE && App.get(followupRequired).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            followupRequired.getQuestionView().setError(getString(R.string.empty_field));
            followupRequired.getRadioGroup().requestFocus();
            error = true;
        }


        if(initiatingAdditionalTreatment.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : initiatingAdditionalTreatment.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if(!flag){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                initiatingAdditionalTreatment.getQuestionView().setError(getString(R.string.empty_field));
                initiatingAdditionalTreatment.getQuestionView().requestFocus();
                error = true;
            }
        }
        if(otherAdditionalTreatment.getVisibility()==View.VISIBLE && App.get(otherAdditionalTreatment).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherAdditionalTreatment.getEditText().setError(getString(R.string.empty_field));
            otherAdditionalTreatment.getEditText().requestFocus();
            error = true;
        }
        if(!App.get(doctorNotes).isEmpty() && App.get(doctorNotes).trim().length() <= 0){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            doctorNotes.getEditText().setError(getString(R.string.ctb_spaces_only));
            doctorNotes.getEditText().requestFocus();
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
        final HashMap<String, String> personAttribute = new HashMap<String, String>();
        final ArrayList<String[]> observations = new ArrayList<String[]>();

        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                Boolean flag = serverService.deleteOfflineForms(encounterId);
                if(!flag){

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.form_does_not_exist));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    bundle.putBoolean("save", false);
                                    submit();
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.backToMainMenu();
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
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));

                    return false;
                }
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
        observations.add(new String[]{"WEIGHT PERCENTILE GROUP", App.get(weightPercentileEditText)});

        if(prescribeAntibiotic.getVisibility()==View.VISIBLE && !App.get(prescribeAntibiotic).isEmpty()){
            observations.add(new String[]{"ANTIBIOTIC GIVEN", App.get(prescribeAntibiotic).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"}); }

        if(!App.get(antibioticDuration).isEmpty()){
            observations.add(new String[]{"ANTIBIOTIC TRIAL DURATION IN DAYS", App.get(antibioticDuration)});
        }
        if(!App.get(antibioticName).isEmpty()){
            observations.add(new String[]{"NAME OF ANTIBIOTIC", App.get(antibioticName)});
        }

        if(!App.get(doseAntibiotic).isEmpty()){
            observations.add(new String[]{"ANTIBIOTIC DOSE", App.get(doseAntibiotic)});
        }

        if(unitDose.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"DOSING UNIT", App.get(unitDose).equals(getResources().getString(R.string.mg)) ? "MILLIGRAM" : "MILLILITER"});
        }

        if(prescribingFurtherTest.getVisibility()==View.VISIBLE && !App.get(prescribingFurtherTest).isEmpty()){
            observations.add(new String[]{"PRESCRIBE FURTHER TESTS", App.get(prescribingFurtherTest).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"}); }


        if(initiatingAdditionalTreatment.getVisibility()==View.VISIBLE){
            String additionalTreatmentString = "";
            for (CheckBox cb : initiatingAdditionalTreatment.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_pediasure)))
                    additionalTreatmentString = additionalTreatmentString + "PEDIASURE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_iron)))
                    additionalTreatmentString = additionalTreatmentString + "IRON" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_vitamin_B_complex)))
                    additionalTreatmentString = additionalTreatmentString + "VITAMIN B COMPLEX" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.anthelmintic)))
                    additionalTreatmentString = additionalTreatmentString + "ANTHELMINTHIC" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_calpol)))
                    additionalTreatmentString = additionalTreatmentString + "CALPOL" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_multivitamins)))
                    additionalTreatmentString = additionalTreatmentString + "MULTIVITAMIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_other_title)))
                    additionalTreatmentString = additionalTreatmentString + "OTHER ADDITIONAL TREATMENT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.none)))
                    additionalTreatmentString = additionalTreatmentString + "NONE" + " ; ";
            }
            observations.add(new String[]{"ADDITIONAL TREATMENT TO TB PATIENT", additionalTreatmentString});
        }

        if(otherAdditionalTreatment.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"OTHER ADDITIONAL TREATMENT", App.get(otherAdditionalTreatment)});
        }

        if(followupRequired.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"FOLLOW UP REQUIRED", App.get(followupRequired).toUpperCase()});
        }

        if(returnVisitDate.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDateTime(secondDateCalendar)});
        }

        if(patientReferred.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"PATIENT REFERRED", App.get(patientReferred).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"}); }

        if(referredTo.getVisibility() == View.VISIBLE){

            String referredToString = "";
            for(CheckBox cb : referredTo.getCheckedBoxes()){
                if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counselor)))
                    referredToString = referredToString + "COUNSELOR" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.psychologist)))
                    referredToString = referredToString + "PSYCHOLOGIST" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.clinician)))
                    referredToString = referredToString + "CLINICAL OFFICER/DOCTOR" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.call_center)))
                    referredToString = referredToString + "CALL CENTER" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.field_supervisor)))
                    referredToString = referredToString + "FIELD SUPERVISOR" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.site_supervisor)))
                    referredToString = referredToString + "SITE SUPERVISOR" + " ; ";
            }
            observations.add(new String[]{"PATIENT REFERRED TO", referredToString});

        }
        if(referalReasonPsychologist.getVisibility() == View.VISIBLE){

            String string = "";
            for(CheckBox cb : referalReasonPsychologist.getCheckedBoxes()){
                if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.check_treatment_adherence)))
                    string = string + "CHECK FOR TREATMENT ADHERENCE" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.psychological_issue)))
                    string = string + "PSYCHOLOGICAL EVALUATION" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.behavioral_issue)))
                    string = string + "BEHAVIORAL ISSUES" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.refusal)))
                    string = string + "REFUSAL OF TREATMENT BY PATIENT" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.other)))
                    string = string + "OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR" + " ; ";
            }
            observations.add(new String[]{"REASON FOR PSYCHOLOGIST/COUNSELOR REFERRAL", string});

        }
        if(otherReferalReasonPsychologist.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR", App.get(otherReferalReasonPsychologist)});

        if(referalReasonSupervisor.getVisibility() == View.VISIBLE){

            String string = "";
            for(CheckBox cb : referalReasonSupervisor.getCheckedBoxes()){
                if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.contact_screening_reminder)))
                    string = string + "CONTACT SCREENING REMINDER" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.treatment_followup_reminder)))
                    string = string + "TREATMENT FOLLOWUP REMINDER" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.check_treatment_adherence)))
                    string = string + "CHECK FOR TREATMENT ADHERENCE" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.investigation_report_collection)))
                    string = string + "INVESTIGATION OF REPORT COLLECTION" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.adverse_events)))
                    string = string + "ADVERSE EVENTS" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.medicine_collection)))
                    string = string + "MEDICINE COLLECTION" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.other)))
                    string = string + "OTHER REFERRAL REASON TO SUPERVISOR" + " ; ";
            }
            observations.add(new String[]{"REASON FOR SUPERVISOR REFERRAL", string});

        }
        if(otherReferalReasonSupervisor.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REFERRAL REASON TO SUPERVISOR", App.get(otherReferalReasonSupervisor)});

        if(referalReasonCallCenter.getVisibility() == View.VISIBLE){

            String string = "";
            for(CheckBox cb : referalReasonCallCenter.getCheckedBoxes()){
                if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.contact_screening_reminder)))
                    string = string + "CONTACT SCREENING REMINDER" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.treatment_followup_reminder)))
                    string = string + "TREATMENT FOLLOWUP REMINDER" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.check_treatment_adherence)))
                    string = string + "CHECK FOR TREATMENT ADHERENCE" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.investigation_report_collection)))
                    string = string + "INVESTIGATION OF REPORT COLLECTION" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.adverse_events)))
                    string = string + "ADVERSE EVENTS" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.medicine_collection)))
                    string = string + "MEDICINE COLLECTION" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.other)))
                    string = string + "OTHER REFERRAL REASON TO CALL CENTER" + " ; ";
            }
            observations.add(new String[]{"REASON FOR CALL CENTER REFERRAL", string});

        }
        if(otherReferalReasonCallCenter.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REFERRAL REASON TO CALL CENTER", App.get(otherReferalReasonCallCenter)});

        if(referalReasonClinician.getVisibility() == View.VISIBLE){

            String string = "";
            for(CheckBox cb : referalReasonClinician.getCheckedBoxes()){
                if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.expert_opinion)))
                    string = string + "EXPERT OPINION" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.adverse_events)))
                    string = string + "ADVERSE EVENTS" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.other)))
                    string = string + "OTHER REFERRAL REASON TO CLINICIAN" + " ; ";
            }
            observations.add(new String[]{"REASON FOR CLINICIAN REFERRAL", string});

        }
        if(otherReferalReasonClinician.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REFERRAL REASON TO CLINICIAN", App.get(otherReferalReasonClinician)});


        if(!App.get(doctorNotes).isEmpty()){
            observations.add(new String[]{"CLINICIAN NOTES (TEXT)", App.get(doctorNotes)});
        }

        personAttribute.put("Health Center",serverService.getLocationUuid(App.getLocation()));

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
                    id = serverService.saveFormLocallyTesting("Childhood TB-Antibiotic Trial Initiation", form, formDateCalendar,observations.toArray(new String[][]{}));

                String result = "";

                result = serverService.saveMultiplePersonAttribute(personAttribute, id);
                if (!result.equals("SUCCESS"))
                    return result;

                result = serverService.saveEncounterAndObservationTesting("Childhood TB-Antibiotic Trial Initiation", form, formDateCalendar, observations.toArray(new String[][]{}),id);
                if (!result.contains("SUCCESS"))
                    return result;

                return "SUCCESS";
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

        serverService.saveFormLocally(formName, form, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int formId) {
        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {
            String[][] obs = obsValue.get(i);
            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            }
            else if (obs[0][0].equals("WEIGHT PERCENTILE GROUP")) {
                weightPercentileEditText.getEditText().setText(obs[0][1]);
                weightPercentileEditText.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("ANTIBIOTIC GIVEN")) {
                for (RadioButton cb : prescribeAntibiotic.getRadioGroup().getButtons()) {
                    if (cb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                prescribeAntibiotic.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("ANTIBIOTIC TRIAL DURATION IN DAYS")) {
                antibioticDuration.getEditText().setText(obs[0][1]);
                antibioticDuration.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("NAME OF ANTIBIOTIC")) {
                antibioticName.getEditText().setText(obs[0][1]);
                antibioticName.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("ANTIBIOTIC DOSE")) {
                doseAntibiotic.getEditText().setText(obs[0][1]);
                doseAntibiotic.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("DOSING UNIT")) {
                for (RadioButton cb : unitDose.getRadioGroup().getButtons()) {
                    if (cb.getText().equals(getResources().getString(R.string.mg)) && obs[0][1].equals("MILLIGRAM")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ml)) && obs[0][1].equals("MILLILITER")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                unitDose.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("PRESCRIBE FURTHER TESTS")) {
                for (RadioButton cb : prescribingFurtherTest.getRadioGroup().getButtons()) {
                    if (cb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                prescribingFurtherTest.setVisibility(View.VISIBLE);
            }
            else if(obs[0][0].equals("ADDITIONAL TREATMENT TO TB PATIENT")) {
                for (CheckBox cb : initiatingAdditionalTreatment.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ctb_pediasure)) && obs[0][1].equals("PEDIASURE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_iron)) && obs[0][1].equals("IRON")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_vitamin_B_complex)) && obs[0][1].equals("VITAMIN B COMPLEX")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.anthelmintic)) && obs[0][1].equals("ANTHELMINTHIC")) {
                        cb.setChecked(true);
                        break;
                    }else if (cb.getText().equals(getResources().getString(R.string.ctb_calpol)) && obs[0][1].equals("CALPOL")) {
                        cb.setChecked(true);
                        break;
                    }else if (cb.getText().equals(getResources().getString(R.string.ctb_multivitamins)) && obs[0][1].equals("MULTIVITAMIN")) {
                        cb.setChecked(true);
                        break;
                    }else if (cb.getText().equals(getResources().getString(R.string.ctb_other_title)) && obs[0][1].equals("OTHER ADDITIONAL TREATMENT")) {
                        cb.setChecked(true);
                        break;
                    }else if (cb.getText().equals(getResources().getString(R.string.none)) && obs[0][1].equals("NONE")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                initiatingAdditionalTreatment.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("OTHER ADDITIONAL TREATMENT")) {
                otherAdditionalTreatment.getEditText().setText(obs[0][1]);
                otherAdditionalTreatment.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("FOLLOW UP REQUIRED")) {
                for (RadioButton cb : followupRequired.getRadioGroup().getButtons()) {
                    if (cb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                followupRequired.setVisibility(View.VISIBLE);
            }

         else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String forthDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(forthDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
                returnVisitDate.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("PATIENT REFERRED")) {
                for (RadioButton rb : patientReferred.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }


            else if (obs[0][0].equals("PATIENT REFERRED TO")) {
                for (CheckBox cb : referredTo.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.counselor)) && obs[0][1].equals("COUNSELOR")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.psychologist)) && obs[0][1].equals("PSYCHOLOGIST")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.clinician)) && obs[0][1].equals("CLINICAL OFFICER/DOCTOR")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.call_center)) && obs[0][1].equals("CALL CENTER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.field_supervisor)) && obs[0][1].equals("FIELD SUPERVISOR")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.site_supervisor)) && obs[0][1].equals("SITE SUPERVISOR")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                referredTo.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REASON FOR PSYCHOLOGIST/COUNSELOR REFERRAL")) {
                for (CheckBox cb : referalReasonPsychologist.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.check_treatment_adherence)) && obs[0][1].equals("CHECK FOR TREATMENT ADHERENCE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.psychological_issue)) && obs[0][1].equals("PSYCHOLOGICAL EVALUATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.behavioral_issue)) && obs[0][1].equals("BEHAVIORAL ISSUES")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.refusal)) && obs[0][1].equals("REFUSAL OF TREATMENT BY PATIENT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.other)) && obs[0][1].equals("OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                referalReasonPsychologist.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR")) {
                otherReferalReasonPsychologist.getEditText().setText(obs[0][1]);
                otherReferalReasonPsychologist.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REASON FOR SUPERVISOR REFERRAL")) {
                for (CheckBox cb : referalReasonSupervisor.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.contact_screening_reminder)) && obs[0][1].equals("CONTACT SCREENING REMINDER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.treatment_followup_reminder)) && obs[0][1].equals("TREATMENT FOLLOWUP REMINDER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.check_treatment_adherence)) && obs[0][1].equals("CHECK FOR TREATMENT ADHERENCE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.investigation_report_collection)) && obs[0][1].equals("INVESTIGATION OF REPORT COLLECTION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.adverse_events)) && obs[0][1].equals("ADVERSE EVENTS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.medicine_collection)) && obs[0][1].equals("MEDICINE COLLECTION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.other)) && obs[0][1].equals("OTHER REFERRAL REASON TO SUPERVISOR")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                referalReasonSupervisor.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER REFERRAL REASON TO SUPERVISOR")) {
                otherReferalReasonSupervisor.getEditText().setText(obs[0][1]);
                otherReferalReasonSupervisor.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REASON FOR CALL CENTER REFERRAL")) {
                for (CheckBox cb : referalReasonCallCenter.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.contact_screening_reminder)) && obs[0][1].equals("CONTACT SCREENING REMINDER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.treatment_followup_reminder)) && obs[0][1].equals("TREATMENT FOLLOWUP REMINDER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.check_treatment_adherence)) && obs[0][1].equals("CHECK FOR TREATMENT ADHERENCE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.investigation_report_collection)) && obs[0][1].equals("INVESTIGATION OF REPORT COLLECTION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.adverse_events)) && obs[0][1].equals("ADVERSE EVENTS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.medicine_collection)) && obs[0][1].equals("MEDICINE COLLECTION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.other)) && obs[0][1].equals("OTHER REFERRAL REASON TO CALL CENTER")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                referalReasonCallCenter.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER REFERRAL REASON TO CALL CENTER")) {
                otherReferalReasonCallCenter.getEditText().setText(obs[0][1]);
                otherReferalReasonCallCenter.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REASON FOR CLINICIAN REFERRAL")) {
                for (CheckBox cb : referalReasonClinician.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.expert_opinion)) && obs[0][1].equals("EXPERT OPINION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.adverse_events)) && obs[0][1].equals("ADVERSE EVENTS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.other)) && obs[0][1].equals("OTHER REFERRAL REASON TO CLINICIAN")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                referalReasonClinician.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER REFERRAL REASON TO CLINICIAN")) {
                otherReferalReasonClinician.getEditText().setText(obs[0][1]);
                otherReferalReasonClinician.setVisibility(View.VISIBLE);
            }


            else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
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
        if (view == returnVisitDate.getButton()) {
            returnVisitDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", false);
            args.putBoolean("allowFutureDate", true);
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
        MySpinner spinner = (MySpinner) parent;


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        boolean flag=true;


        for (CheckBox cb : initiatingAdditionalTreatment.getCheckedBoxes()) {
            if(cb.isChecked()){
                flag = false;
            }
            if (App.get(cb).equals(getResources().getString(R.string.ctb_other_title))) {
                if (cb.isChecked()) {
                    otherAdditionalTreatment.setVisibility(View.VISIBLE);
                } else {
                    otherAdditionalTreatment.setVisibility(View.GONE);
                }
            }

        }

        if(!flag){
            initiatingAdditionalTreatment.getQuestionView().setError(null);
        }
        setReferralViews();

    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        otherAdditionalTreatment.setVisibility(View.GONE);
        returnVisitDate.setVisibility(View.GONE);
        moInstruction.setVisibility(View.GONE);
        referredTo.setVisibility(View.GONE);
        referalReasonPsychologist.setVisibility(View.GONE);
        otherReferalReasonPsychologist.setVisibility(View.GONE);
        referalReasonSupervisor.setVisibility(View.GONE);
        otherReferalReasonSupervisor.setVisibility(View.GONE);
        referalReasonCallCenter.setVisibility(View.GONE);
        otherReferalReasonCallCenter.setVisibility(View.GONE);
        referalReasonClinician.setVisibility(View.GONE);
        otherReferalReasonClinician.setVisibility(View.GONE);
        updateDisplay();

        String weightString = serverService.getLatestObsValue(App.getPatientId(), Forms.CLINICIAN_EVALUATION_FORM, "WEIGHT (KG)");
        String weightPercentileString = serverService.getLatestObsValue(App.getPatientId(), Forms.PATIENT_INFORMATION_FORM, "WEIGHT PERCENTILE GROUP");
        weightAtBaseline.getEditText().setText(weightString);
        weightPercentileEditText.getEditText().setText(weightPercentileString);
        weightAtBaseline.getEditText().setKeyListener(null);
        weightPercentileEditText.getEditText().setKeyListener(null);

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



        if (group == patientReferred.getRadioGroup()) {
            if (App.get(patientReferred).equals(getResources().getString(R.string.yes))) {
                referredTo.setVisibility(View.VISIBLE);
                setReferralViews();
            }
            else {
                referredTo.setVisibility(View.GONE);
                referalReasonPsychologist.setVisibility(View.GONE);
                otherReferalReasonPsychologist.setVisibility(View.GONE);
                referalReasonSupervisor.setVisibility(View.GONE);
                otherReferalReasonSupervisor.setVisibility(View.GONE);
                referalReasonCallCenter.setVisibility(View.GONE);
                otherReferalReasonCallCenter.setVisibility(View.GONE);
                referalReasonClinician.setVisibility(View.GONE);
                otherReferalReasonClinician.setVisibility(View.GONE);
            }
        }
        else if (group == followupRequired.getRadioGroup()) {
            followupRequired.getQuestionView().setError(null);
            if (followupRequired.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                returnVisitDate.setVisibility(View.VISIBLE);
                moInstruction.setVisibility(View.VISIBLE);
            } else if (followupRequired.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.no))){
                Toast.makeText(context, "Please fill End of Followup Form", Toast.LENGTH_SHORT).show();
                returnVisitDate.setVisibility(View.GONE);
                moInstruction.setVisibility(View.GONE);
            }
        }
        else if (group == prescribingFurtherTest.getRadioGroup()) {

            if (prescribingFurtherTest.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                Toast.makeText(context, "Please fill Test Indication Form", Toast.LENGTH_SHORT).show();
            }
        }
        else if (group == unitDose.getRadioGroup()) {
            unitDose.getQuestionView().setError(null);
        }


    }

    public void setReferralViews(){

        referalReasonPsychologist.setVisibility(View.GONE);
        otherReferalReasonPsychologist.setVisibility(View.GONE);
        referalReasonSupervisor.setVisibility(View.GONE);
        otherReferalReasonSupervisor.setVisibility(View.GONE);
        referalReasonCallCenter.setVisibility(View.GONE);
        otherReferalReasonCallCenter.setVisibility(View.GONE);
        referalReasonClinician.setVisibility(View.GONE);
        otherReferalReasonClinician.setVisibility(View.GONE);

        for(CheckBox cb:referredTo.getCheckedBoxes()){

            if(cb.getText().equals(getString(R.string.counselor)) || cb.getText().equals(getString(R.string.psychologist))){
                if(cb.isChecked()){
                    referalReasonPsychologist.setVisibility(View.VISIBLE);
                    for(CheckBox cb1:referalReasonPsychologist.getCheckedBoxes()){
                        if(cb1.isChecked()) {
                            referalReasonPsychologist.getQuestionView().setError(null);
                            if(cb1.getText().equals(getString(R.string.other)))
                                otherReferalReasonPsychologist.setVisibility(View.VISIBLE);
                        }
                    }
                    referredTo.getQuestionView().setError(null);
                }
            } else if(cb.getText().equals(getString(R.string.site_supervisor)) || cb.getText().equals(getString(R.string.field_supervisor))){
                if(cb.isChecked()){
                    referalReasonSupervisor.setVisibility(View.VISIBLE);
                    for(CheckBox cb1:referalReasonSupervisor.getCheckedBoxes()){
                        if(cb1.isChecked()) {
                            referalReasonSupervisor.getQuestionView().setError(null);
                            if(cb1.getText().equals(getString(R.string.other)))
                                otherReferalReasonSupervisor.setVisibility(View.VISIBLE);
                        }
                    }
                    referredTo.getQuestionView().setError(null);
                }
            } else if(cb.getText().equals(getString(R.string.call_center))){
                if(cb.isChecked()){
                    referalReasonCallCenter.setVisibility(View.VISIBLE);
                    for(CheckBox cb1:referalReasonCallCenter.getCheckedBoxes()){
                        if(cb1.isChecked()) {
                            referalReasonCallCenter.getQuestionView().setError(null);
                            if(cb1.getText().equals(getString(R.string.other)))
                                otherReferalReasonCallCenter.setVisibility(View.VISIBLE);
                        }
                    }
                    referredTo.getQuestionView().setError(null);
                }
            } else if(cb.getText().equals(getString(R.string.clinician))){
                if(cb.isChecked()){
                    referalReasonClinician.setVisibility(View.VISIBLE);
                    for(CheckBox cb1:referalReasonClinician.getCheckedBoxes()){
                        if(cb1.isChecked()) {
                            referalReasonClinician.getQuestionView().setError(null);
                            if(cb1.getText().equals(getString(R.string.other)))
                                otherReferalReasonClinician.setVisibility(View.VISIBLE);
                        }
                    }
                    referredTo.getQuestionView().setError(null);
                }
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
