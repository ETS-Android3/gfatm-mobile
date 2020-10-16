package com.ihsinformatics.gfatmmobile.pet;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.Barcode;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
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
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class PetBaselineScreeningForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    Boolean dateChoose = false;

    // Views...
    public static final int THIRD_DATE_DIALOG_ID = 3;
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;


    TitledEditText indexPatientId;
    Button scanQRCode;
    TitledRadioGroup treatmentStatus;
    TitledRadioGroup tbCurrentTreatmentType;
    TitledButton treatmentInitiationDate;
    TitledRadioGroup contactRegistered;
    TitledRadioGroup tbHistory;
    TitledRadioGroup tbHistoryTreatmentType;
    TitledSpinner relationship;
    TitledEditText otherRelation;

    TitledRadioGroup entryLocation;

    TitledRadioGroup cough;
    TitledRadioGroup coughDuration;
    TitledRadioGroup haemoptysis;
    TitledRadioGroup fever;
    TitledRadioGroup weightLoss;
    TitledRadioGroup reduceAppetite;
    TitledRadioGroup reduceActivity;
    TitledRadioGroup nightSweats;
    TitledRadioGroup swelling;
    TitledRadioGroup pregnancyHistory;

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
    TitledEditText clincianNote;

    TitledButton returnVisitDate;

    MyLinearLayout linearLayout;

    ScrollView scrollView;
    Boolean refillFlag = false;

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

        pageCount = 2;
        formName = Forms.PET_BASELINE_SCREENING;
        form = Forms.pet_baselineScreening;

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
                LinearLayout layout = new LinearLayout(mainContent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        } else {
            for (int i = 0; i < pageCount; i++) {
                LinearLayout layout = new LinearLayout(mainContent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        indexPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_id), "", "", RegexUtil.idLength, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, App.HORIZONTAL, false, "PATIENT ID OF INDEX CASE");
        scanQRCode = new Button(context);
        scanQRCode.setText("Scan QR Code");
        treatmentStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_tb_treatment_status), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "TUBERCULOSIS TREATMENT STATUS", getResources().getStringArray(R.array.yes_no_list_concept));
        tbCurrentTreatmentType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_type_tb_treatment), getResources().getStringArray(R.array.pet_types_tb_treatment), "", App.HORIZONTAL, App.VERTICAL, false, "CONTACT CURRENT TB TREATMENT TYPE", new String[]{"DRUG-SENSITIVE TUBERCULOSIS INFECTION", "DRUG-RESISTANT TB", "UNKNOWN"});
        treatmentInitiationDate = new TitledButton(context, null, getResources().getString(R.string.pet_treatment_initiation_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.VERTICAL);
        contactRegistered = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_contact_registered), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "REGISTERED IN ZERO TB", getResources().getStringArray(R.array.yes_no_list_concept));
        tbHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_tb_history), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "HISTORY OF TUBERCULOSIS", getResources().getStringArray(R.array.yes_no_list_concept));
        tbHistoryTreatmentType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_type_tb_treatment), getResources().getStringArray(R.array.pet_types_tb_treatment), "", App.HORIZONTAL, App.VERTICAL, false, "CONTACT PAST TB TREATMENT TYPE", new String[]{"DRUG-SENSITIVE TUBERCULOSIS INFECTION", "DRUG-RESISTANT TB", "UNKNOWN"});
        relationship = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_relationship), getResources().getStringArray(R.array.pet_household_heads), "", App.VERTICAL, true, "FAMILY MEMBER", new String[]{"MOTHER", "FATHER", "MATERNAL GRANDMOTHER", "MATERNAL GRANDFATHER", "PATERNAL GRANDMOTHER", "PATERNAL GRANDFATHER", "BROTHER", "SISTER", "SON", "DAUGHTER", "SPOUSE", "AUNT", "UNCLE", "OTHER FAMILY MEMBER"});
        otherRelation = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 15, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER FAMILY MEMBER");
        entryLocation = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_event_location), getResources().getStringArray(R.array.pet_locations_of_entry), getResources().getString(R.string.pet_contact_home), App.HORIZONTAL, App.VERTICAL, true, "LOCATION OF EVENT", new String[]{"HEALTH FACILITY", "HOME"});

        // second page views...
        linearLayout = new MyLinearLayout(context, getResources().getString(R.string.pet_contact_verbal_screening), App.VERTICAL);
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_has_cough), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "COUGH", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        coughDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_cough_duration), getResources().getStringArray(R.array.pet_cough_durations), getResources().getString(R.string.pet_less_than_2_weeks), App.VERTICAL, App.VERTICAL, true, "COUGH DURATION", new String[]{"COUGH LASTING LESS THAN 2 WEEKS", "COUGH LASTING MORE THAN 2 WEEKS", "COUGH LASTING MORE THAN 3 WEEKS", "UNKNOWN", "REFUSED"});
        haemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_haemoptysis), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "HEMOPTYSIS", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_has_fever), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "FEVER", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_have_weight_loss), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "WEIGHT LOSS", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        reduceAppetite = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_reduced_appetite), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "LOSS OF APPETITE", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        reduceActivity = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_reduced_activity), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "REDUCED MOBILITY", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_have_night_sweats), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "NIGHT SWEATS", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        swelling = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_swelling), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "SWELLING", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        pregnancyHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_pregnancy_history), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "PREGNANCY STATUS", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});

        linearLayout.addView(cough);
        linearLayout.addView(coughDuration);
        linearLayout.addView(haemoptysis);
        linearLayout.addView(fever);
        linearLayout.addView(weightLoss);
        linearLayout.addView(reduceAppetite);
        linearLayout.addView(reduceActivity);
        linearLayout.addView(nightSweats);
        linearLayout.addView(swelling);
        linearLayout.addView(pregnancyHistory);

        patientReferred = new TitledRadioGroup(context, null, getResources().getString(R.string.refer_patient), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL, true, "PATIENT REFERRED", getResources().getStringArray(R.array.yes_no_list_concept));
        referredTo = new TitledCheckBoxes(context, null, getResources().getString(R.string.refer_patient_to), getResources().getStringArray(R.array.refer_patient_to_option), null, App.VERTICAL, App.VERTICAL, true, "PATIENT REFERRED TO", new String[]{"COUNSELOR", "PSYCHOLOGIST", "CLINICAL OFFICER/DOCTOR", "CALL CENTER", "FIELD SUPERVISOR", "SITE SUPERVISOR"});
        referalReasonPsychologist = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_psychologist), getResources().getStringArray(R.array.referral_reason_for_psychologist_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR PSYCHOLOGIST/COUNSELOR REFERRAL", new String[]{"CHECK FOR TREATMENT ADHERENCE", "PSYCHOLOGICAL EVALUATION", "BEHAVIORAL ISSUES", "REFUSAL OF TREATMENT BY PATIENT", "OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR"});
        otherReferalReasonPsychologist = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR");
        referalReasonSupervisor = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_supervisor), getResources().getStringArray(R.array.referral_reason_for_supervisor_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR SUPERVISOR REFERRAL", new String[]{"CONTACT SCREENING REMINDER", "TREATMENT FOLLOWUP REMINDER", "CHECK FOR TREATMENT ADHERENCE", "INVESTIGATION OF REPORT COLLECTION", "ADVERSE EVENTS", "MEDICINE COLLECTION", "OTHER REFERRAL REASON TO SUPERVISOR"});
        otherReferalReasonSupervisor = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO SUPERVISOR");
        referalReasonCallCenter = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_center), getResources().getStringArray(R.array.referral_reason_for_call_center_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR CALL CENTER REFERRAL", new String[]{"CONTACT SCREENING REMINDER", "TREATMENT FOLLOWUP REMINDER", "CHECK FOR TREATMENT ADHERENCE", "INVESTIGATION OF REPORT COLLECTION", "ADVERSE EVENTS", "MEDICINE COLLECTION", "OTHER REFERRAL REASON TO CALL CENTER"});
        otherReferalReasonCallCenter = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO CALL CENTER");
        referalReasonClinician = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_clinician), getResources().getStringArray(R.array.referral_reason_for_clinician_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR CLINICIAN REFERRAL", new String[]{"EXPERT OPINION", "ADVERSE EVENTS", "OTHER REFERRAL REASON TO CLINICIAN"});
        otherReferalReasonClinician = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO CLINICIAN");
        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.pet_doctor_notes), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "CLINICIAN NOTES (TEXT)");
        clincianNote.getEditText().setSingleLine(false);
        clincianNote.getEditText().setMinimumHeight(150);

        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.pet_facility_visit_date), "", App.VERTICAL, true);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), indexPatientId.getEditText(), treatmentStatus.getRadioGroup(), tbCurrentTreatmentType.getRadioGroup(), contactRegistered.getRadioGroup(), tbHistory.getRadioGroup(), tbHistoryTreatmentType.getRadioGroup(), relationship.getSpinner(), otherRelation.getEditText()
                , entryLocation.getRadioGroup(),
                cough.getRadioGroup(), coughDuration.getRadioGroup(), haemoptysis.getRadioGroup(), fever.getRadioGroup(), weightLoss.getRadioGroup(), reduceAppetite.getRadioGroup(), reduceActivity.getRadioGroup(),
                nightSweats.getRadioGroup(), nightSweats.getRadioGroup(), swelling.getRadioGroup(), treatmentInitiationDate.getButton(), clincianNote.getEditText(), pregnancyHistory.getRadioGroup(),
                patientReferred.getRadioGroup(), referredTo, referalReasonPsychologist, otherReferalReasonPsychologist.getEditText(), referalReasonSupervisor, otherReferalReasonSupervisor.getEditText(),
                referalReasonCallCenter, otherReferalReasonCallCenter.getEditText(), referalReasonClinician, otherReferalReasonClinician.getEditText(), returnVisitDate.getButton()
        };

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, indexPatientId, scanQRCode, treatmentStatus, tbCurrentTreatmentType, treatmentInitiationDate, contactRegistered, tbHistory, tbHistoryTreatmentType, relationship, otherRelation,
                        /*citizenship, cnicLayout, cnicOwner, otherCnicOwner, otherNIC, phone1Layout, phone2Layout, address1, addressLayout, province, district, city, addressType, landmark,*/ entryLocation, linearLayout},
                        {patientReferred, referredTo, referalReasonPsychologist, otherReferalReasonPsychologist, referalReasonSupervisor, otherReferalReasonSupervisor,
                                referalReasonCallCenter, otherReferalReasonCallCenter, referalReasonClinician, otherReferalReasonClinician, clincianNote, returnVisitDate}
                };

        formDate.getButton().setOnClickListener(this);
        returnVisitDate.getButton().setOnClickListener(this);
        treatmentInitiationDate.getButton().setOnClickListener(this);
        scanQRCode.setOnClickListener(this);
        cough.getRadioGroup().setOnCheckedChangeListener(this);
        relationship.getSpinner().setOnItemSelectedListener(this);
        treatmentStatus.getRadioGroup().setOnCheckedChangeListener(this);
        tbHistory.getRadioGroup().setOnCheckedChangeListener(this);
        patientReferred.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : referredTo.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : referalReasonPsychologist.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : referalReasonSupervisor.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : referalReasonClinician.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : referalReasonCallCenter.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        resetViews();


        indexPatientId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (start == 5 && s.length() == 5) {
                    int i = indexPatientId.getEditText().getSelectionStart();
                    if (i == 5) {
                        indexPatientId.getEditText().setText(indexPatientId.getEditText().getText().toString().substring(0, 4));
                        indexPatientId.getEditText().setSelection(4);
                    }
                } else if (s.length() == 5 && !s.toString().contains("-")) {
                    indexPatientId.getEditText().setText(s + "-");
                    indexPatientId.getEditText().setSelection(6);
                } else if (s.length() == 7 && !RegexUtil.isValidId(App.get(indexPatientId)))
                    indexPatientId.getEditText().setError(getString(R.string.invalid_id));
                else
                    indexPatientId.getEditText().setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void updateDisplay() {

        if (refillFlag) {
            refillFlag = false;
            return;
        }

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
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }
        if (!(treatmentInitiationDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = treatmentInitiationDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                treatmentInitiationDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                treatmentInitiationDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                treatmentInitiationDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }

        if (!dateChoose) {
            Calendar requiredDate = formDateCalendar.getInstance();
            requiredDate.setTime(formDateCalendar.getTime());
            requiredDate.add(Calendar.DATE, 30);

            if (requiredDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                thirdDateCalendar.setTime(requiredDate.getTime());
            } else {
                requiredDate.add(Calendar.DATE, -1);
                thirdDateCalendar.setTime(requiredDate.getTime());
            }
        }


        String nextAppointmentDateString = App.getSqlDate(thirdDateCalendar);
        Date nextAppointmentDate = App.stringToDate(nextAppointmentDateString, "yyyy-MM-dd");

        String treatStartDateString = App.getSqlDate(secondDateCalendar);
        Date treatmentStDate = App.stringToDate(treatStartDateString, "yyyy-MM-dd");


        if (!(returnVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString()))) {

            String formDa = returnVisitDate.getButton().getText().toString();

            //Date date = new Date();
            if (thirdDateCalendar.before(formDateCalendar)) {

                if (!formDa.equals(""))
                    thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                if (!formDa.equals(""))
                    returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
                else
                    returnVisitDate.getButton().setText("");

            } else if (thirdDateCalendar.before(secondDateCalendar)) {

                if (!formDa.equals(""))
                    thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                if (!formDa.equals(""))
                    returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
                else
                    returnVisitDate.getButton().setText("");

            } else if (nextAppointmentDate.compareTo(treatmentStDate) == 0) {
                if (!formDa.equals(""))
                    thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                if (!formDa.equals(""))
                    returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
                else
                    returnVisitDate.getButton().setText("");
            } else
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
        }
        formDate.getButton().setEnabled(true);
        treatmentInitiationDate.getButton().setEnabled(true);
        returnVisitDate.getButton().setEnabled(true);

    }

    @Override
    public boolean validate() {

        Boolean error = super.validate();
        View view = null;


        if (!App.get(indexPatientId).equals("")) {
            if (!RegexUtil.isValidId(App.get(indexPatientId))) {
                indexPatientId.getEditText().setError(getResources().getString(R.string.invalid_id));
                indexPatientId.getEditText().requestFocus();
                error = true;
            } else if (App.getPatient().getPatientId().equals(App.get(indexPatientId))) {
                indexPatientId.getEditText().setError(getResources().getString(R.string.pet_index_contact_id_same_error));
                indexPatientId.getEditText().requestFocus();
                error = true;
            } else {
                indexPatientId.getEditText().setError(null);
                indexPatientId.getEditText().clearFocus();
            }
        }

        if (error) {

            // int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            // DrawableCompat.setTint(clearIcon, color);
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
                                        otherRelation.getEditText().clearFocus();
                                        indexPatientId.getEditText().clearFocus();
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
        } else
            return true;
    }

    @Override
    public boolean submit() {
        final HashMap<String, String> personAttribute = new HashMap<String, String>();
        final ArrayList<String[]> observations = getObservations();

        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                Boolean flag = serverService.deleteOfflineForms(encounterId);
                if (!flag) {
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

                    /*Toast.makeText(context, getString(R.string.form_does_not_exist),
                            Toast.LENGTH_LONG).show();*/

                    return false;
                }
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

        if (treatmentInitiationDate.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT START DATE", App.getSqlDate(secondDateCalendar)});


        observations.add(new String[]{"PATIENT SOURCE", "TUBERCULOSIS CONTACT"});

        if (returnVisitDate.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FACILITY VISIT DATE", App.getSqlDate(secondDateCalendar)});

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
                if (App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocally(formName, form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = "";
                result = serverService.saveMultiplePersonAttribute(personAttribute, id);
                if (!result.equals("SUCCESS"))
                    return result;

                if (!App.get(indexPatientId).equals("")) {
                    if (serverService.getLatestEncounterDateTime(App.getPatientId(), "PET-Baseline Screening") == null && serverService.getLatestEncounterDateTime(App.getPatientId(), "Clinician Evaluation") == null && serverService.getLatestObsValue(App.getPatientId(), "INDEX CONTACT RELATIONSHIP UUID") == null) {
                        result = serverService.saveContactIndexRelationship(App.get(indexPatientId), App.getPatient().getPatientId(), null, id);
                        if (!result.contains("SUCCESS"))
                            return result;
                        else {

                            String[] array = result.split(";;;");
                            observations.add(new String[]{"INDEX CONTACT RELATIONSHIP UUID", array[1]});

                        }

                    }
                }

                result = serverService.saveEncounterAndObservationTesting(formName, form, formDateCalendar, observations.toArray(new String[][]{}), id);
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

        return true;
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");*/
            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar, false, true, false);
        } else if (view == treatmentInitiationDate.getButton()) {
            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowFutureDate", false);
            args.putBoolean("allowPastDate", true);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");*/
            treatmentInitiationDate.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar, false, true, true);

        } else if (view == scanQRCode) {
            try {
                Intent intent = new Intent(Barcode.BARCODE_INTENT);
                if (App.isCallable(context, intent)) {
                    intent.putExtra(Barcode.SCAN_MODE, Barcode.QR_MODE);
                    startActivityForResult(intent, Barcode.BARCODE_RESULT);
                } else {
                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getString(R.string.barcode_scanner_missing));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    //DrawableCompat.setTint(clearIcon, color);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            } catch (ActivityNotFoundException e) {
                //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage(getString(R.string.barcode_scanner_missing));
                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                //DrawableCompat.setTint(clearIcon, color);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_error));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        } else if (view == returnVisitDate.getButton()) {
            returnVisitDate.getButton().setEnabled(false);
            /*Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", true);*/
            showDateDialog(thirdDateCalendar, true, false, true);
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

        setReferralViews();

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDateCalendar = Calendar.getInstance();
        secondDateCalendar = Calendar.getInstance();
        thirdDateCalendar = Calendar.getInstance();

        Object[][] towns = serverService.getAllTowns();
        String[] townList = new String[towns.length];

        for (int i = 0; i < towns.length; i++) {
            townList[i] = String.valueOf(towns[i][1]);
        }


        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        coughDuration.setVisibility(View.GONE);
        haemoptysis.setVisibility(View.GONE);

        otherRelation.setVisibility(View.GONE);
        treatmentInitiationDate.setVisibility(View.GONE);
        contactRegistered.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);

        tbCurrentTreatmentType.setVisibility(View.GONE);
        tbHistoryTreatmentType.setVisibility(View.GONE);
        referredTo.setVisibility(View.GONE);
        referalReasonPsychologist.setVisibility(View.GONE);
        otherReferalReasonPsychologist.setVisibility(View.GONE);
        referalReasonSupervisor.setVisibility(View.GONE);
        otherReferalReasonSupervisor.setVisibility(View.GONE);
        referalReasonCallCenter.setVisibility(View.GONE);
        otherReferalReasonCallCenter.setVisibility(View.GONE);
        referalReasonClinician.setVisibility(View.GONE);
        otherReferalReasonClinician.setVisibility(View.GONE);


        if (App.getPatient().getPerson().getGender().equals("female") || App.getPatient().getPerson().getGender().equals("F"))
            pregnancyHistory.setVisibility(View.VISIBLE);
        else
            pregnancyHistory.setVisibility(View.GONE);

        Bundle bundle = this.getArguments();
        Boolean autoFill = false;
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);

                autoFill = true;
                refill(formId);

            } else bundle.putBoolean("save", false);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (group == cough.getRadioGroup()) {
            if (App.get(cough).equals(getResources().getString(R.string.yes))) {
                coughDuration.setVisibility(View.VISIBLE);
                haemoptysis.setVisibility(View.VISIBLE);
            } else {
                coughDuration.setVisibility(View.GONE);
                haemoptysis.setVisibility(View.GONE);
            }
        } else if (group == treatmentStatus.getRadioGroup()) {
            if (App.get(treatmentStatus).equals(getResources().getString(R.string.yes))) {
                contactRegistered.setVisibility(View.VISIBLE);
                treatmentInitiationDate.setVisibility(View.VISIBLE);
                tbCurrentTreatmentType.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            } else {
                contactRegistered.setVisibility(View.GONE);
                treatmentInitiationDate.setVisibility(View.GONE);
                tbCurrentTreatmentType.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        } else if (group == tbHistory.getRadioGroup()) {
            if (App.get(tbHistory).equals(getResources().getString(R.string.yes)))
                tbHistoryTreatmentType.setVisibility(View.VISIBLE);
            else
                tbHistoryTreatmentType.setVisibility(View.GONE);
        } else if (group == patientReferred.getRadioGroup()) {
            if (App.get(patientReferred).equals(getResources().getString(R.string.yes))) {
                referredTo.setVisibility(View.VISIBLE);
                setReferralViews();
            } else {
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
    }

    public void setReferralViews() {

        referalReasonPsychologist.setVisibility(View.GONE);
        otherReferalReasonPsychologist.setVisibility(View.GONE);
        referalReasonSupervisor.setVisibility(View.GONE);
        otherReferalReasonSupervisor.setVisibility(View.GONE);
        referalReasonCallCenter.setVisibility(View.GONE);
        otherReferalReasonCallCenter.setVisibility(View.GONE);
        referalReasonClinician.setVisibility(View.GONE);
        otherReferalReasonClinician.setVisibility(View.GONE);

        for (CheckBox cb : referredTo.getCheckedBoxes()) {

            if (cb.getText().equals(getString(R.string.counselor)) || cb.getText().equals(getString(R.string.psychologist))) {
                if (cb.isChecked()) {
                    referalReasonPsychologist.setVisibility(View.VISIBLE);
                    for (CheckBox cb1 : referalReasonPsychologist.getCheckedBoxes()) {
                        if (cb1.isChecked()) {
                            referalReasonPsychologist.getQuestionView().setError(null);
                            if (cb1.getText().equals(getString(R.string.other)))
                                otherReferalReasonPsychologist.setVisibility(View.VISIBLE);
                        }
                    }
                    referredTo.getQuestionView().setError(null);
                }
            } else if (cb.getText().equals(getString(R.string.site_supervisor)) || cb.getText().equals(getString(R.string.field_supervisor))) {
                if (cb.isChecked()) {
                    referalReasonSupervisor.setVisibility(View.VISIBLE);
                    for (CheckBox cb1 : referalReasonSupervisor.getCheckedBoxes()) {
                        if (cb1.isChecked()) {
                            referalReasonSupervisor.getQuestionView().setError(null);
                            if (cb1.getText().equals(getString(R.string.other)))
                                otherReferalReasonSupervisor.setVisibility(View.VISIBLE);
                        }
                    }
                    referredTo.getQuestionView().setError(null);
                }
            } else if (cb.getText().equals(getString(R.string.call_center))) {
                if (cb.isChecked()) {
                    referalReasonCallCenter.setVisibility(View.VISIBLE);
                    for (CheckBox cb1 : referalReasonCallCenter.getCheckedBoxes()) {
                        if (cb1.isChecked()) {
                            referalReasonCallCenter.getQuestionView().setError(null);
                            if (cb1.getText().equals(getString(R.string.other)))
                                otherReferalReasonCallCenter.setVisibility(View.VISIBLE);
                        }
                    }
                    referredTo.getQuestionView().setError(null);
                }
            } else if (cb.getText().equals(getString(R.string.clinician))) {
                if (cb.isChecked()) {
                    referalReasonClinician.setVisibility(View.VISIBLE);
                    for (CheckBox cb1 : referalReasonClinician.getCheckedBoxes()) {
                        if (cb1.isChecked()) {
                            referalReasonClinician.getQuestionView().setError(null);
                            if (cb1.getText().equals(getString(R.string.other)))
                                otherReferalReasonClinician.setVisibility(View.VISIBLE);
                        }
                    }
                    referredTo.getQuestionView().setError(null);
                }
            }

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Retrieve barcode scan results
        if (requestCode == Barcode.BARCODE_RESULT) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra(Barcode.SCAN_RESULT);
                // Check for valid Id
                if (RegexUtil.isValidId(str)) {
                    indexPatientId.getEditText().setText(str);
                    indexPatientId.getEditText().requestFocus();
                } else {

                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);

                    indexPatientId.getEditText().setText("");
                    indexPatientId.getEditText().requestFocus();

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getString(R.string.invalid_scanned_id));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    //DrawableCompat.setTint(clearIcon, color);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
            } else if (resultCode == RESULT_CANCELED) {

                int color = App.getColor(context, R.attr.colorAccent);

                indexPatientId.getEditText().setText("");
                indexPatientId.getEditText().requestFocus();

                /*final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this).create();
                alertDialog.setMessage(getString(R.string.warning_before_clear));
                Drawable clearIcon = getResources().getDrawable(R.drawable.ic_clear);
                DrawableCompat.setTint(clearIcon, color);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_clear));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();*/

            }
            // Set the locale again, since the Barcode app restores system's
            // locale because of orientation
            Locale.setDefault(App.getCurrentLocale());
            Configuration config = new Configuration();
            config.locale = App.getCurrentLocale();
            context.getResources().updateConfiguration(config, null);
        }
    }

    @Override
    public void refill(int formId) {
        super.refill(formId);
        refillFlag = true;

        OfflineForm fo = serverService.getSavedFormById(formId);

        ArrayList<String[][]> obsValue = fo.getObsValue();


        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("TREATMENT START DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                treatmentInitiationDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
                treatmentInitiationDate.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("FACILITY VISIT DATE")) {
                String secondDate = obs[0][1];
                thirdDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
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
            else if (((int) view.getTag()) == THIRD_DATE_DIALOG_ID)
                thirdDateCalendar.set(yy, mm, dd);
            updateDisplay();
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            updateDisplay();
        }
    }


}