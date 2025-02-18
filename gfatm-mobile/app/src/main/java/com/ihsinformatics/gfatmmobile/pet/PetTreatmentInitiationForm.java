package com.ihsinformatics.gfatmmobile.pet;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
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
 * Created by Rabbia on 11/24/2016.
 */

public class PetTreatmentInitiationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    public static final int THIRD_DATE_DIALOG_ID = 3;
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;

    // Views...

    TitledEditText weight;
    TitledSpinner patientSource;
    TitledEditText otherPatientSource;
    TitledEditText indexPatientId;
    TitledRadioGroup tbType;
    TitledRadioGroup infectionType;
    TitledRadioGroup resistanceType;
    TitledRadioGroup patientType;
    TitledCheckBoxes dstPattern;

    TitledButton treatmentInitiationDate;
    TitledRadioGroup bcgScar;
    TitledRadioGroup petRegimen;
    //TitledRadioGroup rifapentineAvailable;
    TitledEditText isoniazidDose;
    TitledEditText rifapentineDose;
    TitledEditText levofloxacinDose;
    TitledEditText rifampicinDose;
    TitledEditText ethionamideDose;
    TitledEditText ethambutolDose;
    TitledEditText moxifloxacilinDose;
    TitledEditText iptRegNo;

    TitledRadioGroup ancillaryDrugsNeeded;
    TitledCheckBoxes ancillaryDrugs;
    TitledEditText durationOfAncillaryDrugs;
    TitledEditText otherAncillaryDrugs;

    TitledEditText clincianNote;

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

    TitledRadioGroup followupRequired;
    TitledButton returnVisitDate;

    Boolean refillFlag = false;

    ScrollView scrollView;

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

        pageCount = 3;
        formName = Forms.PET_TREATMENT_INITIATION;
        form = Forms.pet_treatmentInitiation;

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
        thirdDateFragment = new PetTreatmentInitiationForm.SelectDateFragment();

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        weight = new TitledEditText(context, null, getResources().getString(R.string.pet_weight), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true, "WEIGHT (KG)");
        patientSource = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.patient_source), getResources().getStringArray(R.array.patient_source_options), "", App.HORIZONTAL, true, "PATIENT SOURCE", new String[]{"","IDENTIFIED PATIENT THROUGH SCREENING", "PATIENT REFERRED", "TUBERCULOSIS CONTACT", "WALK IN", "OTHER PATIENT SOURCE"});
        otherPatientSource = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER PATIENT SOURCE");
        indexPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_id), "", "", RegexUtil.idLength, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "PATIENT ID OF INDEX CASE");
        indexPatientId.getEditText().setKeyListener(null);
        tbType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_tb_type), getResources().getStringArray(R.array.pet_tb_types), "", App.HORIZONTAL, App.VERTICAL, true, "SITE OF TUBERCULOSIS DISEASE", new String[]{"PULMONARY TUBERCULOSIS", "EXTRA-PULMONARY TUBERCULOSIS"});
        infectionType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_infection_type), getResources().getStringArray(R.array.pet_infection_types), "", App.HORIZONTAL, App.VERTICAL, true, "TUBERCULOSIS INFECTION TYPE", new String[]{"DRUG-SENSITIVE TUBERCULOSIS INFECTION", "DRUG-RESISTANT TB"});
        resistanceType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_resistance_Type), getResources().getStringArray(R.array.pet_resistance_types), "", App.VERTICAL, App.VERTICAL, true, "TUBERCULOSIS DRUG RESISTANCE TYPE", new String[]{"RIFAMPICIN RESISTANT TUBERCULOSIS INFECTION", "MONO DRUG RESISTANT TUBERCULOSIS", "PANDRUG RESISTANT TUBERCULOSIS", "MULTI-DRUG RESISTANT TUBERCULOSIS INFECTION", "EXTREMELY DRUG-RESISTANT TUBERCULOSIS INFECTION", "Pre-XDR - FQ", "Pre-XDR - INJ"});
        patientType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_patient_Type), getResources().getStringArray(R.array.tb_category), "", App.VERTICAL, App.VERTICAL, true, "TB CATEGORY", new String[]{"CATEGORY I TUBERCULOSIS", "CATEGORY II TUBERCULOSIS"});
        dstPattern = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_dst_pattern), getResources().getStringArray(R.array.pet_dst_patterns), null, App.VERTICAL, App.VERTICAL, true, "RESISTANT TO ANTI-TUBERCULOSIS DRUGS", new String[]{"ISONIAZID", "RIFAMPICIN", "AMIKACIN", "CAPREOMYCIN", "STREPTOMYCIN", "OFLOXACIN", "MOXIFLOXACIN", "ETHAMBUTOL", "ETHIONAMIDE", "PYRAZINAMIDE"});

        // second page view
        treatmentInitiationDate = new TitledButton(context, null, getResources().getString(R.string.tpt_treatment_initiation_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.VERTICAL);
        bcgScar = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_bcg_scar), getResources().getStringArray(R.array.yes_no_unknown_refused_options), "", App.VERTICAL, App.VERTICAL, false, "BACILLUS CALMETTE–GUÉRIN VACCINE", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        petRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.tpt_regimen), getResources().getStringArray(R.array.tpt_regimens), "", App.VERTICAL, App.VERTICAL, true, "POST-EXPOSURE TREATMENT REGIMEN", new String[]{"ISONIAZID PROPHYLAXIS", "ISONIAZID AND RIFAPENTINE", "LEVOFLOXACIN AND ETHIONAMIDE", "LEVOFLOXACIN AND ETHAMBUTOL", "LEVOFLOXACIN AND MOXIFLOXACILIN", "ETHIONAMIDE AND ETHAMBUTOL", "ETHIONAMIDE AND MOXIFLOXACILIN", "MOXIFLOXACILIN AND ETHAMBUTOL", "1HP", "3HR", "4R"});
        isoniazidDose = new TitledEditText(context, null, getResources().getString(R.string.pet_isoniazid_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "ISONIAZID DOSE");
        //rifapentineAvailable = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_rifapentine_available), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        rifapentineDose = new TitledEditText(context, null, getResources().getString(R.string.pet_rifapentine_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "RIFAPENTINE DOSE");
        levofloxacinDose = new TitledEditText(context, null, getResources().getString(R.string.pet_levofloxacin_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "LEVOFLOXACIN DOSE");
        rifampicinDose = new TitledEditText(context, null, getResources().getString(R.string.pet_rifampicin_dose), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "RIFAMPICIN DOSE");
        ethionamideDose = new TitledEditText(context, null, getResources().getString(R.string.pet_ethionamide_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "ETHIONAMIDE DOSE");
        ethambutolDose = new TitledEditText(context, null, getResources().getString(R.string.pet_ethambutol_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "ETHAMBUTOL DOSE");
        moxifloxacilinDose = new TitledEditText(context, null, getResources().getString(R.string.pet_moxifloxacilin_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "MOXIFLOXACILIN DOSE");
        iptRegNo = new TitledEditText(context, null, getResources().getString(R.string.ctb_ipt_reg_no), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "IPT REGISTRATION NUMBER");

        ancillaryDrugsNeeded = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_ancillary_drugs_needed), getResources().getStringArray(R.array.yes_no_options), getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true, "ANCILLARY MEDICATION NEEDED", getResources().getStringArray(R.array.yes_no_list_concept));
        ancillaryDrugs = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_ancillary_drugs), getResources().getStringArray(R.array.pet_ancillary_drugs), null, App.VERTICAL, App.VERTICAL, true, "ANCILLARY DRUGS", new String[]{"IRON", "MULTIVITAMIN", "ANTHELMINTHIC", "PEDIASURE", "VITAMIN B COMPLEX", "CALPOL", "OTHER"});
        durationOfAncillaryDrugs = new TitledEditText(context, null, getResources().getString(R.string.pet_ancillary_drug_duration), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "MEDICATION DURATION");
        otherAncillaryDrugs = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER ANCILLARY DRUGS");

        patientReferred = new TitledRadioGroup(context, null, getResources().getString(R.string.refer_patient), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL, true, "PATIENT REFERRED", getResources().getStringArray(R.array.yes_no_list_concept));
        referredTo = new TitledCheckBoxes(context, null, getResources().getString(R.string.refer_patient_to), getResources().getStringArray(R.array.refer_patient_to_option), null, App.VERTICAL, App.VERTICAL, true, "PATIENT REFERRED TO", new String[]{"COUNSELOR", "PSYCHOLOGIST", "CLINICAL OFFICER/DOCTOR", "CALL CENTER", "FIELD SUPERVISOR", "SITE SUPERVISOR"});
        referalReasonPsychologist = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_psychologist), getResources().getStringArray(R.array.referral_reason_for_psychologist_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR PSYCHOLOGIST/COUNSELOR REFERRAL", new String[]{"CHECK FOR TREATMENT ADHERENCE", "PSYCHOLOGICAL EVALUATION", "BEHAVIORAL ISSUES", "REFUSAL OF TREATMENT BY PATIENT", "OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR"});
        otherReferalReasonPsychologist = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR");
        referalReasonSupervisor = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_supervisor), getResources().getStringArray(R.array.referral_reason_for_supervisor_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR SUPERVISOR REFERRAL", new String[]{"CONTACT SCREENING REMINDER", "TREATMENT FOLLOWUP REMINDER", "CHECK FOR TREATMENT ADHERENCE", "INVESTIGATION OF REPORT COLLECTION", "ADVERSE EVENTS", "MEDICINE COLLECTION", "OTHER REFERRAL REASON TO SUPERVISOR"});
        otherReferalReasonSupervisor = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO SUPERVISOR");
        referalReasonCallCenter = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_center), getResources().getStringArray(R.array.referral_reason_for_call_center_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR CALL CENTER REFERRAL", new String[]{"CONTACT SCREENING REMINDER", "TREATMENT FOLLOWUP REMINDER", "CHECK FOR TREATMENT ADHERENCE", "INVESTIGATION OF REPORT COLLECTION", "ADVERSE EVENTS", "MEDICINE COLLECTION", "OTHER REFERRAL REASON TO CALL CENTER"});
        otherReferalReasonCallCenter = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO CALL CENTER");
        referalReasonClinician = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_clinician), getResources().getStringArray(R.array.referral_reason_for_clinician_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR CLINICIAN REFERRAL", new String[]{"EXPERT OPINION", "ADVERSE EVENTS", "OTHER REFERRAL REASON TO CLINICIAN"});
        otherReferalReasonClinician = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO CLINICIAN");

        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.pet_doctor_notes), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "CLINICIAN NOTES (TEXT)");
        clincianNote.getEditText().setSingleLine(false);
        clincianNote.getEditText().setMinimumHeight(150);

        followupRequired = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_followup_required), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL, true, "CLINICAL FOLLOWUP NEEDED", getResources().getStringArray(R.array.yes_no_list_concept));

        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.pet_return_visit_date), DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), weight.getEditText(), patientSource.getSpinner(), otherPatientSource.getEditText(), indexPatientId.getEditText(), tbType.getRadioGroup(), infectionType.getRadioGroup(), patientType.getRadioGroup(), dstPattern, resistanceType.getRadioGroup(),
                treatmentInitiationDate.getButton(), petRegimen.getRadioGroup(), isoniazidDose.getEditText(), rifapentineDose.getEditText(), levofloxacinDose.getEditText(), ethionamideDose.getEditText(), ethambutolDose.getEditText(), moxifloxacilinDose.getEditText(),
                clincianNote.getEditText(), returnVisitDate.getButton(), bcgScar.getRadioGroup(), iptRegNo.getEditText(),
                followupRequired.getRadioGroup(), ancillaryDrugsNeeded.getRadioGroup(), ancillaryDrugs, durationOfAncillaryDrugs.getEditText(), patientReferred.getRadioGroup(), referredTo, referalReasonPsychologist, otherReferalReasonPsychologist.getEditText(), referalReasonSupervisor, otherReferalReasonSupervisor.getEditText(),
                referalReasonCallCenter, rifampicinDose.getEditText(), otherReferalReasonCallCenter.getEditText(), referalReasonClinician, otherReferalReasonClinician.getEditText(), otherAncillaryDrugs.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, weight, patientSource, otherPatientSource, indexPatientId, tbType, infectionType, resistanceType, patientType/*, dstPattern*/},
                        {treatmentInitiationDate, bcgScar, petRegimen, isoniazidDose, rifapentineDose, rifampicinDose, levofloxacinDose, ethionamideDose, ethambutolDose, moxifloxacilinDose, iptRegNo},
                        {ancillaryDrugsNeeded, ancillaryDrugs, otherAncillaryDrugs, durationOfAncillaryDrugs, clincianNote, patientReferred,
                                referredTo, referalReasonPsychologist, otherReferalReasonPsychologist, referalReasonSupervisor, otherReferalReasonSupervisor,
                                referalReasonCallCenter, otherReferalReasonCallCenter, referalReasonClinician, otherReferalReasonClinician, followupRequired, returnVisitDate},
                };

        infectionType.getRadioGroup().setOnCheckedChangeListener(this);
        patientSource.getSpinner().setOnItemSelectedListener(this);
        formDate.getButton().setOnClickListener(this);
        treatmentInitiationDate.getButton().setOnClickListener(this);
        petRegimen.getRadioGroup().setOnCheckedChangeListener(this);
        returnVisitDate.getButton().setOnClickListener(this);
        ancillaryDrugsNeeded.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : ancillaryDrugs.getCheckedBoxes()) {
            cb.setOnCheckedChangeListener(this);
        }
        followupRequired.getRadioGroup().setOnCheckedChangeListener(this);
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

        weight.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!App.get(weight).equals("")) {
                    Double w = Double.parseDouble(App.get(weight));
                    if (w < 0.5 || w > 700.0)
                        weight.getEditText().setError(getString(R.string.pet_invalid_weight_range));
                    else
                        weight.getEditText().setError(null);
                }

                calculateDosages();

            }
        });


        levofloxacinDose.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!App.get(levofloxacinDose).equals("")) {
                    int dose = Integer.parseInt(App.get(levofloxacinDose));
                    if (dose > 1000)
                        levofloxacinDose.getEditText().setError(getResources().getString(R.string.pet_levofloxacin_dose_exceeded_1000));
                    else
                        levofloxacinDose.getEditText().setError(null);
                }
            }
        });

        ethionamideDose.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!App.get(ethionamideDose).equals("")) {
                    int dose = Integer.parseInt(App.get(ethionamideDose));
                    if (dose > 1000)
                        ethionamideDose.getEditText().setError(getResources().getString(R.string.pet_ethionamide_dose_exceeded_1000));
                    else
                        ethionamideDose.getEditText().setError(null);
                }
            }
        });

        rifapentineDose.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!App.get(rifapentineDose).equals("")) {
                    int dose = Integer.parseInt(App.get(rifapentineDose));
                    if (dose > 900)
                        rifapentineDose.getEditText().setError(getResources().getString(R.string.pet_rifapentine_dose_exceeded_900));
                    else
                        rifapentineDose.getEditText().setError(null);
                }
            }
        });

        isoniazidDose.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!App.get(isoniazidDose).equals("")) {
                    Double dose = Double.parseDouble(App.get(isoniazidDose));
                    if (App.get(petRegimen).equals(getResources().getString(R.string.pet_ipt_6h))) {
                        if (dose > 300) {
                            isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded_1000));
                            isoniazidDose.getEditText().requestFocus();
                        } else isoniazidDose.getEditText().setError(null);
                    } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_3hp))) {
                        if (dose > 1000) {
                            isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded_1000));
                            isoniazidDose.getEditText().requestFocus();
                        } else isoniazidDose.getEditText().setError(null);
                    } else isoniazidDose.getEditText().setError(null);
                }
            }
        });

        ethambutolDose.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!App.get(ethambutolDose).equals("")) {
                    int dose = Integer.parseInt(App.get(ethambutolDose));
                    if (dose > 2000)
                        ethambutolDose.getEditText().setError(getResources().getString(R.string.pet_ethambutol_dose_exceeded_2000));
                    else
                        ethambutolDose.getEditText().setError(null);
                }
            }
        });

        moxifloxacilinDose.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!App.get(moxifloxacilinDose).equals("")) {
                    int dose = Integer.parseInt(App.get(moxifloxacilinDose));
                    if (dose > 400)
                        moxifloxacilinDose.getEditText().setError(getResources().getString(R.string.pet_moxifloxacilin_dose_exceeded_400));
                    else
                        moxifloxacilinDose.getEditText().setError(null);
                }
            }
        });

        rifampicinDose.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!App.get(rifampicinDose).equals("")) {
                    int dose = Integer.parseInt(App.get(rifampicinDose));
                    if (App.get(petRegimen).equals(getResources().getString(R.string.pet_4r))) {
                        if (dose > 600)
                            rifampicinDose.getEditText().setError(getResources().getString(R.string.pet_rifampicin_dose_exceeded_600));
                        else
                            rifampicinDose.getEditText().setError(null);
                    } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_3hr))) {
                        if (dose > 900)
                            rifampicinDose.getEditText().setError(getResources().getString(R.string.pet_rifampicin_dose_exceeded_900));
                        else
                            rifampicinDose.getEditText().setError(null);
                    }

                }
            }
        });

        resetViews();
    }

    @Override
    public void resetViews() {

        super.resetViews();

        if (App.getPatient().getPerson().getAge() >= 15)
            bcgScar.setVisibility(View.GONE);

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        // treatmentInitiationDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());

        indexPatientId.setVisibility(View.GONE);
        tbType.setVisibility(View.GONE);
        infectionType.setVisibility(View.GONE);
        resistanceType.setVisibility(View.GONE);
        patientType.setVisibility(View.GONE);
        otherPatientSource.setVisibility(View.GONE);
        dstPattern.setVisibility(View.GONE);
        isoniazidDose.setVisibility(View.GONE);
        rifapentineDose.setVisibility(View.GONE);
        levofloxacinDose.setVisibility(View.GONE);
        ethionamideDose.setVisibility(View.GONE);
        ethambutolDose.setVisibility(View.GONE);
        moxifloxacilinDose.setVisibility(View.GONE);
        rifampicinDose.setVisibility(View.GONE);
        returnVisitDate.setVisibility(View.GONE);
        referredTo.setVisibility(View.GONE);
        referalReasonPsychologist.setVisibility(View.GONE);
        otherReferalReasonPsychologist.setVisibility(View.GONE);
        referalReasonSupervisor.setVisibility(View.GONE);
        otherReferalReasonSupervisor.setVisibility(View.GONE);
        referalReasonCallCenter.setVisibility(View.GONE);
        otherReferalReasonCallCenter.setVisibility(View.GONE);
        referalReasonClinician.setVisibility(View.GONE);
        otherReferalReasonClinician.setVisibility(View.GONE);
        otherAncillaryDrugs.setVisibility(View.GONE);

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

        if (!autoFill) {
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

                    String weight = serverService.getLatestObsValue(App.getPatientId(), "Clinician Evaluation", "WEIGHT (KG)");
                    String indexId = serverService.getLatestObsValue(App.getPatientId(), "PATIENT ID OF INDEX CASE");
                    String intervention = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_BASELINE_SCREENING, "INTERVENTION");
                    String bcgScar = serverService.getLatestObsValue(App.getPatientId(), "Clinician Evaluation", "BACILLUS CALMETTE–GUÉRIN VACCINE");
                    String patientSource = serverService.getLatestObsValue(App.getPatientId(), "PATIENT SOURCE");



                    String tbType = "";
                    String infectionType = "";
                    String resistanceType = "";
                    String dstbType = "";
                    String patientType = "";

                    serverService.getPatient(indexId, false);

                    if (!(indexId == null || indexId.equals(""))) {
                        String id = serverService.getPatientSystemIdByIdentifierLocalDB(indexId);
                        tbType = serverService.getLatestObsValue(id, "SITE OF TUBERCULOSIS DISEASE");
                        infectionType = serverService.getLatestObsValue(id, "TUBERCULOSIS INFECTION TYPE");
                        resistanceType = serverService.getLatestObsValue(id, "TUBERCULOSIS DRUG RESISTANCE TYPE");
                        dstbType = serverService.getLatestObsValue(id, "RESISTANT TO ANTI-TUBERCULOSIS DRUGS");
                        patientType = serverService.getLatestObsValue(id, "TB CATEGORY");

                    }

                    if (weight != null)
                        weight = weight.replace(".0", "");

                    if (weight != null)
                        if (!weight.equals(""))
                            result.put("WEIGHT (KG)", weight);
                    if (indexId != null)
                        if (!indexId.equals(""))
                            result.put("PATIENT ID OF INDEX CASE", indexId);
                    if (tbType != null)
                        if (!tbType.equals(""))
                            result.put("SITE OF TUBERCULOSIS DISEASE", tbType.equals("PULMONARY TUBERCULOSIS") ? getResources().getString(R.string.pet_ptb) : getResources().getString(R.string.pet_eptb));
                    if (infectionType != null)
                        if (!infectionType.equals(""))
                            result.put("TUBERCULOSIS INFECTION TYPE", infectionType.equals("DRUG-SENSITIVE TUBERCULOSIS INFECTION") ? getResources().getString(R.string.pet_dstb) : getResources().getString(R.string.pet_drtb));
                    if (resistanceType != null)
                        if (!resistanceType.equals(""))
                            result.put("TUBERCULOSIS DRUG RESISTANCE TYPE", resistanceType.equals("RIFAMPICIN RESISTANT TUBERCULOSIS INFECTION") ? getResources().getString(R.string.pet_rr_tb) :
                                    (resistanceType.equals("MONO DRUG RESISTANT TUBERCULOSIS") ? getResources().getString(R.string.pet_dr_tb) :
                                            (resistanceType.equals("PANDRUG RESISTANT TUBERCULOSIS") ? getResources().getString(R.string.pet_pdr_tb) :
                                                    (resistanceType.equals("MULTI-DRUG RESISTANT TUBERCULOSIS INFECTION") ? getResources().getString(R.string.pet_mdr_tb) :
                                                            (resistanceType.equals("EXTREMELY DRUG-RESISTANT TUBERCULOSIS INFECTION") ? getResources().getString(R.string.pet_xdr_tb) :
                                                                    (resistanceType.equals("Pre-XDR - FQ") ? getResources().getString(R.string.pet_pre_xdr_fq) : getResources().getString(R.string.pet_pre_xdr_inj)))))));
                    if (dstbType != null)
                        if (!dstbType.equals(""))
                            result.put("RESISTANT TO ANTI-TUBERCULOSIS DRUGS", dstbType);

                    if (patientType != null) {
                        if (!patientType.equals(""))
                            result.put("TB CATEGORY", patientType.equals("CATEGORY I TUBERCULOSIS") ? getResources().getString(R.string.pet_cat_1) :
                                    (patientType.equals("CATEGORY II TUBERCULOSIS") ? getResources().getString(R.string.pet_cat_2) : ""));

                    }

                    if (intervention == null)
                        intervention = "";
                    result.put("INTERVENTION", intervention);

                    if (bcgScar != null)
                        result.put("BCG SCAR", bcgScar);

                    if (patientSource != null) {
                        result.put("PATIENT SOURCE", patientSource);
                    } else if (indexId != null) {
                        result.put("PATIENT SOURCE", "TUBERCULOSIS CONTACT");
                    }

                    return result;

                }

                @Override
                protected void onProgressUpdate(String... values) {
                }

                ;

                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();


                    if (result.get("PATIENT SOURCE") == null) {
                        if (snackbar != null)
                            snackbar.dismiss();
                        snackbar = Snackbar.make(mainContent, "Please fill Patient Information form", Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();
                        return;
                    } else if (result.get("PATIENT SOURCE").equals("TUBERCULOSIS CONTACT") && !result.containsKey("PATIENT ID OF INDEX CASE")) {



                        /*final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        alertDialog.setMessage(getResources().getString(R.string.baseline_screening_missing));
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
                        alertDialog.show();*/

                        if (snackbar != null)
                            snackbar.dismiss();
                        snackbar = Snackbar.make(mainContent, "Please fill Clinician Evaluation form", Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();
                        return;

                    }

                    if (result.get("WEIGHT (KG)") != null) {
                        weight.getEditText().setText(result.get("WEIGHT (KG)"));
                    }

                    String patientSourceString = result.get("PATIENT SOURCE");
                    if (patientSourceString != null) {
                        String val = patientSourceString.equals("IDENTIFIED PATIENT THROUGH SCREENING") ? getResources().getString(R.string.screening) :
                                patientSourceString.equals("PATIENT REFERRED") ? getResources().getString(R.string.referred) :
                                        patientSourceString.equals("TUBERCULOSIS CONTACT") ? getResources().getString(R.string.contact_patient) :
                                                (patientSourceString.equals("WALK IN") ? getResources().getString(R.string.walkin) : getResources().getString(R.string.ctb_other_title));

                        patientSource.getSpinner().selectValue(val);
                        patientSource.getSpinner().setEnabled(false);

                        if (patientSourceString.equals(getResources().getString(R.string.contact_patient))) {

                            indexPatientId.setVisibility(View.GONE);
                            tbType.setVisibility(View.GONE);
                            infectionType.setVisibility(View.GONE);
                            resistanceType.setVisibility(View.GONE);
                            patientType.setVisibility(View.GONE);
                            dstPattern.setVisibility(View.GONE);

                        }
                    }

                    if (result.get("PATIENT ID OF INDEX CASE") != null && result.get("PATIENT SOURCE").equals("TUBERCULOSIS CONTACT")) {
                        indexPatientId.getEditText().setText(result.get("PATIENT ID OF INDEX CASE"));
                    }

                    if (result.get("SITE OF TUBERCULOSIS DISEASE") != null && result.get("PATIENT SOURCE").equals("TUBERCULOSIS CONTACT")) {
                        for (RadioButton rb : tbType.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.pet_ptb)) && result.get("SITE OF TUBERCULOSIS DISEASE").equals(getResources().getString(R.string.pet_ptb))) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_eptb)) && result.get("SITE OF TUBERCULOSIS DISEASE").equals(getResources().getString(R.string.pet_eptb))) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                        tbType.setRadioGroupEnabled(false);
                    }
                    if (result.get("TUBERCULOSIS INFECTION TYPE") != null && result.get("PATIENT SOURCE").equals("TUBERCULOSIS CONTACT")) {
                        for (RadioButton rb : infectionType.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.pet_dstb)) && result.get("TUBERCULOSIS INFECTION TYPE").equals(getResources().getString(R.string.pet_dstb))) {
                                rb.setChecked(true);
                                patientType.setVisibility(View.VISIBLE);
                                dstPattern.setVisibility(View.GONE);
                                resistanceType.setVisibility(View.GONE);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_drtb)) && result.get("TUBERCULOSIS INFECTION TYPE").equals(getResources().getString(R.string.pet_drtb))) {
                                rb.setChecked(true);
                                patientType.setVisibility(View.GONE);
                                dstPattern.setVisibility(View.VISIBLE);
                                resistanceType.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                        infectionType.setRadioGroupEnabled(false);
                    }
                    if (result.get("TUBERCULOSIS DRUG RESISTANCE TYPE") != null && result.get("PATIENT SOURCE").equals("TUBERCULOSIS CONTACT")) {
                        for (RadioButton rb : resistanceType.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.pet_rr_tb)) && result.get("TUBERCULOSIS DRUG RESISTANCE TYPE").equals(getResources().getString(R.string.pet_rr_tb))) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_dr_tb)) && result.get("TUBERCULOSIS DRUG RESISTANCE TYPE").equals(getResources().getString(R.string.pet_dr_tb))) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_pdr_tb)) && result.get("TUBERCULOSIS DRUG RESISTANCE TYPE").equals(getResources().getString(R.string.pet_pdr_tb))) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_mdr_tb)) && result.get("TUBERCULOSIS DRUG RESISTANCE TYPE").equals(getResources().getString(R.string.pet_mdr_tb))) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_xdr_tb)) && result.get("TUBERCULOSIS DRUG RESISTANCE TYPE").equals(getResources().getString(R.string.pet_xdr_tb))) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_pre_xdr_fq)) && result.get("TUBERCULOSIS DRUG RESISTANCE TYPE").equals(getResources().getString(R.string.pet_pre_xdr_fq))) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_pre_xdr_inj)) && result.get("TUBERCULOSIS DRUG RESISTANCE TYPE").equals(getResources().getString(R.string.pet_pre_xdr_inj))) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                        resistanceType.setRadioGroupEnabled(false);
                        if (!result.get("TUBERCULOSIS DRUG RESISTANCE TYPE").equals(""))
                            resistanceType.setVisibility(View.VISIBLE);
                    }

                    if (result.get("TB CATEGORY") != null && result.get("PATIENT SOURCE").equals("TUBERCULOSIS CONTACT")) {
                        for (RadioButton rb : patientType.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.pet_cat_1)) && result.get("TB CATEGORY").equals(getResources().getString(R.string.pet_ptb))) {
                                rb.setChecked(true);
                                patientType.setRadioGroupEnabled(false);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_cat_2)) && result.get("TB CATEGORY").equals(getResources().getString(R.string.pet_eptb))) {
                                rb.setChecked(true);
                                patientType.setRadioGroupEnabled(false);
                                break;
                            }
                        }
                    }

                    if (result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS") != null && result.get("PATIENT SOURCE").equals("TUBERCULOSIS CONTACT")) {
                        for (CheckBox cb : dstPattern.getCheckedBoxes()) {
                            if (cb.getText().equals(getResources().getString(R.string.pet_isoniazid)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("ISONIAZID")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_rifampicin)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("RIFAMPICIN")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_amikacin)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("AMIKACIN")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_capreomycin)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("CAPREOMYCIN")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_streptpmycin)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("STREPTOMYCIN")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_ofloxacin)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("OFLOXACIN")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_moxifloxacin)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("MOXIFLOXACIN")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_ethambutol)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("ETHAMBUTOL")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_ethionamide)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("ETHIONAMIDE")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_pyrazinamide)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("PYRAZINAMIDE")) {
                                cb.setChecked(true);
                            }

                        }
                        dstPattern.setCheckedBoxesEnabled(false);
                        if (result.get("TUBERCULOSIS INFECTION TYPE").equals(""))
                            dstPattern.setVisibility(View.VISIBLE);
                    }
                    if (result.get("BCG SCAR") != null) {
                        for (RadioButton rb : bcgScar.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.yes)) && result.get("BCG SCAR").equals(getResources().getString(R.string.yes))) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.no)) && result.get("BCG SCAR").equals(getResources().getString(R.string.no))) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.refused)) && result.get("BCG SCAR").equals(getResources().getString(R.string.refused))) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && result.get("BCG SCAR").equals(getResources().getString(R.string.unknown))) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }

                  /*  if (App.get(infectionType).equals(getResources().getString(R.string.pet_drtb))) {

                        for (RadioButton rb : petRegimen.getRadioGroup().getButtons()) {

                            if (rb.getText().equals(getResources().getString(R.string.pet_levofloxacin_ethionamide)))
                                rb.setChecked(true);
                            else
                                rb.setChecked(false);

                        }

                    } else {

                        if (App.getPatient().getPerson().getAge() < 2) {
                            for (RadioButton rb : petRegimen.getRadioGroup().getButtons()) {

                                if (rb.getText().equals(getResources().getString(R.string.pet_ipt_6h)))
                                    rb.setChecked(true);
                                else
                                    rb.setChecked(false);

                            }
                        } else {
                            for (RadioButton rb : petRegimen.getRadioGroup().getButtons()) {

                                if (rb.getText().equals(getResources().getString(R.string.pet_3hp)))
                                    rb.setChecked(true);
                                else
                                    rb.setChecked(false);


                            }
                        }

                    }*/

                }
            };
            autopopulateFormTask.execute("");
        }

    }

    @Override
    public void updateDisplay() {

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
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }


        if (!(treatmentInitiationDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {
            String missedVisit = treatmentInitiationDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(App.stringToDate(missedVisit, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                treatmentInitiationDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(missedVisit, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                treatmentInitiationDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else {
                treatmentInitiationDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                Calendar requiredDate = secondDateCalendar.getInstance();
                requiredDate.setTime(secondDateCalendar.getTime());
                requiredDate.add(Calendar.DATE, 30);

                if (requiredDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    thirdDateCalendar.setTime(requiredDate.getTime());
                } else {
                    requiredDate.add(Calendar.DATE, 1);
                    thirdDateCalendar.setTime(requiredDate.getTime());
                }
            }
        }


        String nextAppointmentDateString = App.getSqlDate(thirdDateCalendar);
        Date nextAppointmentDate = App.stringToDate(nextAppointmentDateString, "yyyy-MM-dd");

        String formDateString = App.getSqlDate(formDateCalendar);
        Date formStDate = App.stringToDate(formDateString, "yyyy-MM-dd");

        String missedVisitDateString = App.getSqlDate(secondDateCalendar);
        Date missedVisitDt = App.stringToDate(missedVisitDateString, "yyyy-MM-dd");


        if (!(returnVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString()))) {
            Calendar dateToday = Calendar.getInstance();
            dateToday.add(Calendar.MONTH, 24);

            String formDa = returnVisitDate.getButton().getText().toString();

            if (thirdDateCalendar.before(formDateCalendar)) {

                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());

            } else if (thirdDateCalendar.after(dateToday)) {
                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_date_cant_be_greater_than_24_months), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
            } else if (nextAppointmentDate.compareTo(formStDate) == 0) {
                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_start_date_and_next_visit_date_cant_be_same), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
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

        Boolean flag = true;

        if (moxifloxacilinDose.getVisibility() == View.VISIBLE) {
            if (App.get(moxifloxacilinDose).isEmpty()) {
                moxifloxacilinDose.getEditText().setError(getString(R.string.empty_field));
                moxifloxacilinDose.getEditText().requestFocus();
                gotoLastPage();
                view = null;
                error = true;
            } else {
                int dose = Integer.parseInt(App.get(moxifloxacilinDose));
                if (dose > 400) {
                    moxifloxacilinDose.getEditText().setError(getResources().getString(R.string.pet_moxifloxacilin_dose_exceeded_400));
                    moxifloxacilinDose.getEditText().requestFocus();
                    gotoLastPage();
                } else {
                    moxifloxacilinDose.getEditText().clearFocus();
                    moxifloxacilinDose.getEditText().setError(null);
                }
            }
        }

        if (ethambutolDose.getVisibility() == View.VISIBLE) {
            if (App.get(ethambutolDose).isEmpty()) {
                ethambutolDose.getEditText().setError(getString(R.string.empty_field));
                ethambutolDose.getEditText().requestFocus();
                gotoLastPage();
                view = null;
                error = true;
            } else {
                int dose = Integer.parseInt(App.get(ethambutolDose));
                if (dose > 2000) {
                    ethambutolDose.getEditText().setError(getResources().getString(R.string.pet_ethambutol_dose_exceeded_2000));
                    ethambutolDose.getEditText().requestFocus();
                    gotoLastPage();
                } else {
                    ethambutolDose.getEditText().clearFocus();
                    ethambutolDose.getEditText().setError(null);
                }
            }
        }

        if (ethionamideDose.getVisibility() == View.VISIBLE) {
            if (App.get(ethionamideDose).isEmpty()) {
                ethionamideDose.getEditText().setError(getString(R.string.empty_field));
                ethionamideDose.getEditText().requestFocus();
                gotoLastPage();
                view = null;
                error = true;
            } else {
                int dose = Integer.parseInt(App.get(ethionamideDose));
                if (dose > 1000) {
                    ethionamideDose.getEditText().setError(getResources().getString(R.string.pet_ethionamide_dose_exceeded_1000));
                    ethionamideDose.getEditText().requestFocus();
                    gotoLastPage();
                } else {
                    ethionamideDose.getEditText().clearFocus();
                    ethionamideDose.getEditText().setError(null);
                }
            }
        }
        if (levofloxacinDose.getVisibility() == View.VISIBLE) {
            if (App.get(levofloxacinDose).isEmpty()) {
                levofloxacinDose.getEditText().setError(getString(R.string.empty_field));
                levofloxacinDose.getEditText().requestFocus();
                gotoLastPage();
                view = null;
                error = true;
            } else {
                int dose = Integer.parseInt(App.get(levofloxacinDose));
                if (dose > 1000) {
                    levofloxacinDose.getEditText().setError(getResources().getString(R.string.pet_levofloxacin_dose_exceeded_1000));
                    levofloxacinDose.getEditText().requestFocus();
                    gotoLastPage();
                } else {
                    levofloxacinDose.getEditText().clearFocus();
                    levofloxacinDose.getEditText().setError(null);
                }
            }
        }
        if (rifapentineDose.getVisibility() == View.VISIBLE) {
            if (App.get(rifapentineDose).isEmpty()) {
                rifapentineDose.getEditText().setError(getString(R.string.empty_field));
                rifapentineDose.getEditText().requestFocus();
                gotoLastPage();
                view = null;
                error = true;
            } else {
                int dose = Integer.parseInt(App.get(rifapentineDose));

                if (App.get(petRegimen).equals(getResources().getString(R.string.pet_1hp)) ) {
                    if (dose > 600) {
                        rifapentineDose.getEditText().setError(getResources().getString(R.string.pet_rifapentine_dose_exceeded_600));
                        rifapentineDose.getEditText().requestFocus();
                        gotoLastPage();
                    } else {
                        rifapentineDose.getEditText().clearFocus();
                        rifapentineDose.getEditText().setError(null);
                    }
                }
                else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_3hp)) ) {
                    if (dose > 900) {
                        rifapentineDose.getEditText().setError(getResources().getString(R.string.pet_rifapentine_dose_exceeded_900));
                        rifapentineDose.getEditText().requestFocus();
                        gotoLastPage();
                    } else {
                        rifapentineDose.getEditText().clearFocus();
                        rifapentineDose.getEditText().setError(null);
                    }
                }

            }
        }

        if (rifampicinDose.getVisibility() == View.VISIBLE) {
            if (App.get(rifampicinDose).isEmpty()) {
                rifampicinDose.getEditText().setError(getString(R.string.empty_field));
                rifampicinDose.getEditText().requestFocus();
                gotoLastPage();
                view = null;
                error = true;
            } else {
                int dose = Integer.parseInt(App.get(rifampicinDose));

                if (App.get(petRegimen).equals(getResources().getString(R.string.pet_4r)) ) {
                    if (dose > 600) {
                        rifampicinDose.getEditText().setError(getResources().getString(R.string.pet_rifampicin_dose_exceeded_600));
                        rifampicinDose.getEditText().requestFocus();
                        gotoLastPage();
                    } else {
                        rifampicinDose.getEditText().clearFocus();
                        rifampicinDose.getEditText().setError(null);
                    }
                }
                else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_3hr)) ) {
                    if (dose > 900) {
                        rifampicinDose.getEditText().setError(getResources().getString(R.string.pet_rifampicin_dose_exceeded_900));
                        rifampicinDose.getEditText().requestFocus();
                        gotoLastPage();
                    } else {
                        rifampicinDose.getEditText().clearFocus();
                        rifampicinDose.getEditText().setError(null);
                    }
                }

            }
        }
        if (isoniazidDose.getVisibility() == View.VISIBLE) {
            if (App.get(isoniazidDose).isEmpty()) {
                isoniazidDose.getEditText().setError(getString(R.string.empty_field));
                isoniazidDose.getEditText().requestFocus();
                view = null;
                error = true;
                gotoLastPage();
            } else {
                Double dose = Double.parseDouble(App.get(isoniazidDose));
                if (App.get(petRegimen).equals(getResources().getString(R.string.pet_ipt_6h)) || App.get(petRegimen).equals(getResources().getString(R.string.pet_1hp))) {
                    if (dose > 300) {
                        isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded_300));
                        isoniazidDose.getEditText().requestFocus();
                        view = null;
                        error = true;
                    } else {
                        isoniazidDose.getEditText().setError(null);
                        isoniazidDose.getEditText().clearFocus();
                    }
                }
                else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_3hp))) {
                    if (dose > 900) {
                        isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded_900));
                        isoniazidDose.getEditText().requestFocus();
                        view = null;
                        error = true;
                    } else {
                        isoniazidDose.getEditText().setError(null);
                        isoniazidDose.getEditText().clearFocus();
                    }
                }
                else {
                    if (dose > 1000) {
                        isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded_1000));
                        isoniazidDose.getEditText().requestFocus();
                        view = null;
                        error = true;
                    } else {
                        isoniazidDose.getEditText().setError(null);
                        isoniazidDose.getEditText().clearFocus();
                    }
                }
            }
        }

        flag = true;
        for (CheckBox cb : dstPattern.getCheckedBoxes()) {

            if (cb.isChecked())
                flag = false;

        }
        if (!flag) {
            dstPattern.getQuestionView().setError(getString(R.string.empty_field));
            gotoFirstPage();
            view = dstPattern;
            error = true;
        } else {
            dstPattern.getQuestionView().setError(null);
        }

        if (patientType.getVisibility() == View.VISIBLE && App.get(patientType).isEmpty()) {
            patientType.getQuestionView().setError(getString(R.string.empty_field));
            gotoFirstPage();
            view = patientType;
            error = true;
        } else {
            patientType.getQuestionView().setError(null);
        }

        if (resistanceType.getVisibility() == View.VISIBLE && App.get(resistanceType).isEmpty()) {
            resistanceType.getQuestionView().setError(getString(R.string.empty_field));
            gotoFirstPage();
            view = resistanceType;
            error = true;
        } else {
            resistanceType.getQuestionView().setError(null);
        }

        if (infectionType.getVisibility() == View.VISIBLE && App.get(infectionType).isEmpty()) {
            infectionType.getQuestionView().setError(getString(R.string.empty_field));
            gotoFirstPage();
            view = infectionType;
            error = true;
        } else {
            infectionType.getQuestionView().setError(null);
        }

        if (tbType.getVisibility() == View.VISIBLE && App.get(tbType).isEmpty()) {
            tbType.getQuestionView().setError(getString(R.string.empty_field));
            gotoFirstPage();
            view = tbType;
            error = true;
        } else {
            tbType.getQuestionView().setError(null);
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
                                        ethionamideDose.getEditText().clearFocus();
                                        levofloxacinDose.getEditText().clearFocus();
                                        rifapentineDose.getEditText().clearFocus();
                                        isoniazidDose.getEditText().clearFocus();
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


        observations.add(new String[]{"TREATMENT START DATE", App.getSqlDate(secondDateCalendar)});

        personAttribute.put("Health Center", serverService.getLocationUuid(App.getLocation()));

        if (returnVisitDate.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDate(thirdDateCalendar)});


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

                    if (App.get(followupRequired).equals(getString(R.string.no))) {
                        if (snackbar != null) snackbar.dismiss();
                        snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fill_followup_form), Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        });
                        snackbar.show();
                    }

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
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            args.putString("formDate", formDate.getButtonText());
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");*/
            treatmentInitiationDate.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar, false, true, true);

        } else if (view == returnVisitDate.getButton()) {
            returnVisitDate.getButton().setEnabled(false);
            showDateDialog(thirdDateCalendar, true, false, true);

            /*Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            args.putBoolean("allowFutureDate", true);
            args.putBoolean("allowPastDate", false);
            args.putString("formDate", formDate.getButtonText());
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");
            returnVisitDate.getButton().setEnabled(false);*/

        }

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        MySpinner spinner = (MySpinner) parent;

        if (spinner == patientSource.getSpinner()) {

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.other))) {

                otherPatientSource.setVisibility(View.VISIBLE);
                indexPatientId.setVisibility(View.GONE);
                tbType.setVisibility(View.GONE);
                infectionType.setVisibility(View.GONE);
                resistanceType.setVisibility(View.GONE);
                dstPattern.setVisibility(View.GONE);
                patientType.setVisibility(View.GONE);

            } else if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.contact_patient))) {

                indexPatientId.setVisibility(View.VISIBLE);
                tbType.setVisibility(View.VISIBLE);
                infectionType.setVisibility(View.VISIBLE);
                if (App.get(infectionType).equals(getResources().getString(R.string.pet_dstb))) {
                    dstPattern.setVisibility(View.GONE);
                    patientType.setVisibility(View.VISIBLE);
                    resistanceType.setVisibility(View.GONE);
                } else if (App.get(infectionType).equals(getResources().getString(R.string.pet_drtb))) {
                    dstPattern.setVisibility(View.VISIBLE);
                    patientType.setVisibility(View.GONE);
                    resistanceType.setVisibility(View.VISIBLE);
                }
                otherPatientSource.setVisibility(View.GONE);

            } else {

                indexPatientId.setVisibility(View.GONE);
                tbType.setVisibility(View.GONE);
                infectionType.setVisibility(View.GONE);
                resistanceType.setVisibility(View.GONE);
                dstPattern.setVisibility(View.GONE);
                patientType.setVisibility(View.GONE);
                otherPatientSource.setVisibility(View.GONE);

            }
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        otherAncillaryDrugs.setVisibility(View.GONE);
        for (CheckBox cb : ancillaryDrugs.getCheckedBoxes()) {
            if (cb.isChecked())
                ancillaryDrugs.getQuestionView().setError(null);

            if (cb.isChecked() && cb.getText().equals(getString(R.string.other)))
                otherAncillaryDrugs.setVisibility(View.VISIBLE);
        }

        setReferralViews();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (group == ancillaryDrugsNeeded.getRadioGroup()) {

            if (App.get(ancillaryDrugsNeeded).equals(getResources().getString(R.string.yes))) {
                ancillaryDrugs.setVisibility(View.VISIBLE);
                durationOfAncillaryDrugs.setVisibility(View.VISIBLE);
            } else {
                ancillaryDrugs.setVisibility(View.GONE);
                durationOfAncillaryDrugs.setVisibility(View.GONE);
            }

        } else if (group == petRegimen.getRadioGroup()) {

            calculateDosages();

        } else if (group == followupRequired.getRadioGroup()) {
            if (App.get(followupRequired).equals(getResources().getString(R.string.yes)))
                returnVisitDate.setVisibility(View.VISIBLE);
            else {
                returnVisitDate.setVisibility(View.GONE);
                /*snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fill_end_of_followup), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();*/
            }
            followupRequired.getQuestionView().setError(null);
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
        } else if (group == infectionType.getRadioGroup()) {
            if (App.get(infectionType).equals(getResources().getString(R.string.pet_dstb))) {
                dstPattern.setVisibility(View.GONE);
                patientType.setVisibility(View.VISIBLE);
                resistanceType.setVisibility(View.GONE);
            } else if (App.get(infectionType).equals(getResources().getString(R.string.pet_drtb))) {
                dstPattern.setVisibility(View.VISIBLE);
                patientType.setVisibility(View.VISIBLE);
                resistanceType.setVisibility(View.VISIBLE);
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
    public void refill(int encounterId) {
        super.refill(encounterId);


        if (refillFlag) {
            refillFlag = true;
            return;
        }

        Boolean refillFlag = false;

        OfflineForm fo = serverService.getSavedFormById(encounterId);

        ArrayList<String[][]> obsValue = fo.getObsValue();

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("TREATMENT START DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                treatmentInitiationDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());

            } else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String secondDate = obs[0][1];
                thirdDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
                returnVisitDate.setVisibility(View.VISIBLE);
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

            Calendar today = Calendar.getInstance();
            String d = getArguments().getString("formDate", null);
            if (d != null)
                today = App.getCalendar(App.stringToDate(d, "EEEE, MMM dd,yyyy"));

            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setTag(getArguments().getInt("type"));
            if (!getArguments().getBoolean("allowFutureDate", false)) {
                dialog.getDatePicker().setMaxDate(today.getTime().getTime());
            }
            if (!getArguments().getBoolean("allowPastDate", false))
                dialog.getDatePicker().setMinDate(today.getTime().getTime());
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


    public void calculateDosages() {

        isoniazidDose.getEditText().setHint("");
        rifapentineDose.getEditText().setHint("");
        levofloxacinDose.getEditText().setHint("");
        ethionamideDose.getEditText().setHint("");
        ethambutolDose.getEditText().setHint("");
        moxifloxacilinDose.getEditText().setHint("");

        isoniazidDose.getEditText().setText("");
        rifapentineDose.getEditText().setText("");
        levofloxacinDose.getEditText().setText("");
        ethionamideDose.getEditText().setText("");
        ethambutolDose.getEditText().setText("");
        moxifloxacilinDose.getEditText().setText("");
        rifampicinDose.getEditText().setText("");

        int age = App.getPatient().getPerson().getAge();
        Double weightDouble = Double.parseDouble("0");
        if (!App.get(weight).equals("")) {
            weightDouble = Double.parseDouble(App.get(weight));
        }

        if (App.get(petRegimen).equals(getResources().getString(R.string.pet_ipt_6h))) {

            Double w = 1.0;

            if (age < 15) {
                w = weightDouble * 10f;
                int i = (int) Math.round(w);
                isoniazidDose.getEditText().setText(String.valueOf(i));
            } else {
                w = weightDouble * 5f;
                int i = (int) Math.round(w);
                isoniazidDose.getEditText().setText(String.valueOf(i));
            }

            isoniazidDose.setVisibility(View.VISIBLE);
            rifapentineDose.setVisibility(View.GONE);
            levofloxacinDose.setVisibility(View.GONE);
            ethionamideDose.setVisibility(View.GONE);
            ethambutolDose.setVisibility(View.GONE);
            moxifloxacilinDose.setVisibility(View.GONE);
            rifampicinDose.setVisibility(View.GONE);

            if (w > 300)
                isoniazidDose.getEditText().setError(getString(R.string.pet_isoniazid_dose_exceeded_300));

        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_3hp))) {

            Double w = 1.0;

            w = 1.0;

            if (age < 2) {
                w = weightDouble * 10f;
                int i = (int) Math.round(w);
                isoniazidDose.getEditText().setText(String.valueOf(i));
                rifapentineDose.getEditText().setHint("Not recommended");

                if (w > 900)
                    isoniazidDose.getEditText().setError(getString(R.string.pet_isoniazid_dose_exceeded_900));
                else
                    isoniazidDose.getEditText().setError(null);
            } else if (age > 2) {
                w = weightDouble * 15;
                int i = (int) Math.round(w);
                isoniazidDose.getEditText().setText(String.valueOf(i));
                rifapentineDose.getEditText().setHint("300 - 450 mg");

                if (w > 900) {
                    isoniazidDose.getEditText().setError(getString(R.string.pet_isoniazid_dose_exceeded_900));
                } else {
                    isoniazidDose.getEditText().setError(null);
                }
            }




            if (age > 2) {
                if (weightDouble > 10.0 && weightDouble <= 14.0) {
                    rifapentineDose.getEditText().setText("300");
                } else if (weightDouble >= 14.1 && weightDouble <= 25.0) {
                    rifapentineDose.getEditText().setText("450");
                } else if (weightDouble >= 25.1 && weightDouble <= 32.0) {
                    rifapentineDose.getEditText().setText("600");
                } else if (weightDouble >= 32.1 && weightDouble <= 49.9) {
                    rifapentineDose.getEditText().setText("750");
                } else if (weightDouble >= 50) {
                    rifapentineDose.getEditText().setText("900");
                }
            }

            isoniazidDose.setVisibility(View.VISIBLE);
            rifapentineDose.setVisibility(View.VISIBLE);
            levofloxacinDose.setVisibility(View.GONE);
            ethionamideDose.setVisibility(View.GONE);
            ethambutolDose.setVisibility(View.GONE);
            moxifloxacilinDose.setVisibility(View.GONE);
            rifampicinDose.setVisibility(View.GONE);

        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethionamide))) {

            Double w = 1.0;

            if (age < 15) {
                ethionamideDose.getEditText().setText("");
                ethionamideDose.getEditText().setHint("15 - 20 mg/kg");
            } else {
                ethionamideDose.getEditText().setText("");
                ethionamideDose.getEditText().setHint("500 - 1000 mg");
            }

            Double ww = 1.0;
            if (age < 2) {
                ww = weightDouble * 15f;
                int i = (int) Math.round(ww);
                levofloxacinDose.getEditText().setText(String.valueOf(i));
            } else if (age < 15) {
                levofloxacinDose.getEditText().setText(String.valueOf(""));
                levofloxacinDose.getEditText().setHint("7.5 - 10 mg/kg");
            } else {
                levofloxacinDose.getEditText().setText(String.valueOf(""));
                levofloxacinDose.getEditText().setHint("750 - 1000 mg");
            }

            isoniazidDose.setVisibility(View.GONE);
            rifapentineDose.setVisibility(View.GONE);
            levofloxacinDose.setVisibility(View.VISIBLE);
            ethionamideDose.setVisibility(View.VISIBLE);
            ethambutolDose.setVisibility(View.GONE);
            moxifloxacilinDose.setVisibility(View.GONE);

        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_1hp))) {

            Double w = 1.0;


            if (age >= 2 && age < 15) {
                w = weightDouble * 10f;
                int i = (int) Math.round(w);
                isoniazidDose.getEditText().setText(String.valueOf(i));
            }
            if (weightDouble <= 35.0) {
                rifapentineDose.getEditText().setText("300");
            } else if (weightDouble >= 36.0 && weightDouble <= 45.0) {
                rifapentineDose.getEditText().setText("450");
            } else if (weightDouble > 45.0) {
                rifapentineDose.getEditText().setText("600");
            }

            isoniazidDose.setVisibility(View.VISIBLE);
            rifapentineDose.setVisibility(View.VISIBLE);
            levofloxacinDose.setVisibility(View.GONE);
            ethionamideDose.setVisibility(View.GONE);
            ethambutolDose.setVisibility(View.GONE);
            moxifloxacilinDose.setVisibility(View.GONE);
            rifampicinDose.setVisibility(View.GONE);

        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_3hr))) {

            Double w = 1.0;


            if (age < 15) {
                w = weightDouble * 10f;
                int i = (int) Math.round(w);
                isoniazidDose.getEditText().setText(String.valueOf(i));
            } else {
                w = weightDouble * 5f;
                int i = (int) Math.round(w);
                isoniazidDose.getEditText().setText(String.valueOf(i));
            }

            w = weightDouble * 10f;
            int i = (int) Math.round(w);
            rifampicinDose.getEditText().setText(String.valueOf(i));


            isoniazidDose.setVisibility(View.VISIBLE);
            rifapentineDose.setVisibility(View.GONE);
            levofloxacinDose.setVisibility(View.GONE);
            ethionamideDose.setVisibility(View.GONE);
            ethambutolDose.setVisibility(View.GONE);
            moxifloxacilinDose.setVisibility(View.GONE);
            rifampicinDose.setVisibility(View.VISIBLE);


        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_4r))) {

            Double w = 1.0;


            w = weightDouble * 10f;
            int i = (int) Math.round(w);
            rifampicinDose.getEditText().setText(String.valueOf(i));


            isoniazidDose.setVisibility(View.GONE);
            rifapentineDose.setVisibility(View.GONE);
            levofloxacinDose.setVisibility(View.GONE);
            ethionamideDose.setVisibility(View.GONE);
            ethambutolDose.setVisibility(View.GONE);
            moxifloxacilinDose.setVisibility(View.GONE);
            rifampicinDose.setVisibility(View.VISIBLE);

        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethambutol))) {

            if (weightDouble == 0) {
                levofloxacinDose.getEditText().setText("");
                ethambutolDose.getEditText().setText("");
            } else {

                Double ww = 1.0;
                if (age < 2) {
                    ww = weightDouble * 15f;
                    int i = (int) Math.round(ww);
                    levofloxacinDose.getEditText().setText(String.valueOf(i));
                } else if (age < 15) {
                    levofloxacinDose.getEditText().setText(String.valueOf(""));
                    levofloxacinDose.getEditText().setHint("7.5 - 10 mg/kg");
                } else {
                    levofloxacinDose.getEditText().setText(String.valueOf(""));
                    levofloxacinDose.getEditText().setHint("750 - 1000 mg");
                }

                if (weightDouble <= 2) {
                    ethambutolDose.getEditText().setText(String.valueOf(""));
                    ethambutolDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 7) {
                    ethambutolDose.getEditText().setText(String.valueOf("100"));
                } else if (weightDouble <= 12) {
                    ethambutolDose.getEditText().setText(String.valueOf("200"));
                } else if (weightDouble <= 15) {
                    ethambutolDose.getEditText().setText(String.valueOf("300"));
                } else if (weightDouble <= 26) {
                    ethambutolDose.getEditText().setText(String.valueOf("400"));
                } else if (weightDouble <= 30) {
                    ethambutolDose.getEditText().setText(String.valueOf("500"));
                } else if (weightDouble <= 59) {
                    ethambutolDose.getEditText().setText(String.valueOf("1500"));
                } else {
                    ethambutolDose.getEditText().setText(String.valueOf("2000"));
                }

            }

            isoniazidDose.setVisibility(View.GONE);
            rifapentineDose.setVisibility(View.GONE);
            levofloxacinDose.setVisibility(View.VISIBLE);
            ethionamideDose.setVisibility(View.GONE);
            ethambutolDose.setVisibility(View.VISIBLE);
            moxifloxacilinDose.setVisibility(View.GONE);
            rifampicinDose.setVisibility(View.GONE);


        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_moxifloxacilin))) {

            if (weightDouble == 0) {
                levofloxacinDose.getEditText().setText("");
                moxifloxacilinDose.getEditText().setText("");
            } else {

                Double ww = 1.0;
                if (age < 2) {
                    ww = weightDouble * 15f;
                    int i = (int) Math.round(ww);
                    levofloxacinDose.getEditText().setText(String.valueOf(i));
                } else if (age < 15) {
                    levofloxacinDose.getEditText().setText(String.valueOf(""));
                    levofloxacinDose.getEditText().setHint("7.5 - 10 mg/kg");
                } else {
                    levofloxacinDose.getEditText().setText(String.valueOf(""));
                    levofloxacinDose.getEditText().setHint("750 - 1000 mg");
                }

                if (weightDouble <= 13) {
                    moxifloxacilinDose.getEditText().setText(String.valueOf(""));
                    moxifloxacilinDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 30) {
                    moxifloxacilinDose.getEditText().setText(String.valueOf("200"));
                } else {
                    moxifloxacilinDose.getEditText().setText(String.valueOf("400"));
                }

            }

            isoniazidDose.setVisibility(View.GONE);
            rifapentineDose.setVisibility(View.GONE);
            levofloxacinDose.setVisibility(View.VISIBLE);
            ethionamideDose.setVisibility(View.GONE);
            ethambutolDose.setVisibility(View.GONE);
            moxifloxacilinDose.setVisibility(View.VISIBLE);
            rifampicinDose.setVisibility(View.GONE);

        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_ethionamide_ethambutol))) {

            if (age < 15) {
                ethionamideDose.getEditText().setText("");
                ethionamideDose.getEditText().setHint("15 - 20 mg/kg");
            } else {
                ethionamideDose.getEditText().setText("");
                ethionamideDose.getEditText().setHint("500 - 1000 mg");
            }

            if (weightDouble == 0) {
                ethambutolDose.getEditText().setText("");
            } else {

                if (weightDouble <= 2) {
                    ethambutolDose.getEditText().setText(String.valueOf(""));
                    ethambutolDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 7) {
                    ethambutolDose.getEditText().setText(String.valueOf("100"));
                } else if (weightDouble <= 12) {
                    ethambutolDose.getEditText().setText(String.valueOf("200"));
                } else if (weightDouble <= 15) {
                    ethambutolDose.getEditText().setText(String.valueOf("300"));
                } else if (weightDouble <= 26) {
                    ethambutolDose.getEditText().setText(String.valueOf("400"));
                } else if (weightDouble <= 30) {
                    ethambutolDose.getEditText().setText(String.valueOf("500"));
                } else if (weightDouble <= 59) {
                    ethambutolDose.getEditText().setText(String.valueOf("1500"));
                } else {
                    ethambutolDose.getEditText().setText(String.valueOf("2000"));
                }

            }

            isoniazidDose.setVisibility(View.GONE);
            rifapentineDose.setVisibility(View.GONE);
            levofloxacinDose.setVisibility(View.GONE);
            ethionamideDose.setVisibility(View.VISIBLE);
            ethambutolDose.setVisibility(View.VISIBLE);
            moxifloxacilinDose.setVisibility(View.GONE);
            rifampicinDose.setVisibility(View.GONE);

        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_ethionamide_moxifloxacilin))) {

            if (age < 15) {
                ethionamideDose.getEditText().setText("");
                ethionamideDose.getEditText().setHint("15 - 20 mg/kg");
            } else {
                ethionamideDose.getEditText().setText("");
                ethionamideDose.getEditText().setHint("500 - 1000 mg");
            }

            if (weightDouble == 0) {
                moxifloxacilinDose.getEditText().setText("");
            } else {

                if (weightDouble <= 13) {
                    moxifloxacilinDose.getEditText().setText(String.valueOf(""));
                    moxifloxacilinDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 30) {
                    moxifloxacilinDose.getEditText().setText(String.valueOf("200"));
                } else {
                    moxifloxacilinDose.getEditText().setText(String.valueOf("400"));
                }

            }

            isoniazidDose.setVisibility(View.GONE);
            rifapentineDose.setVisibility(View.GONE);
            levofloxacinDose.setVisibility(View.GONE);
            ethionamideDose.setVisibility(View.VISIBLE);
            ethambutolDose.setVisibility(View.GONE);
            moxifloxacilinDose.setVisibility(View.VISIBLE);
            rifampicinDose.setVisibility(View.GONE);

        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_moxifloxacilin_ethambutol))) {

            if (weightDouble == 0) {
                moxifloxacilinDose.getEditText().setText("");
                ethambutolDose.getEditText().setText("");
            } else {

                if (weightDouble <= 2) {
                    ethambutolDose.getEditText().setText(String.valueOf(""));
                    ethambutolDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 7) {
                    ethambutolDose.getEditText().setText(String.valueOf("100"));
                } else if (weightDouble <= 12) {
                    ethambutolDose.getEditText().setText(String.valueOf("200"));
                } else if (weightDouble <= 15) {
                    ethambutolDose.getEditText().setText(String.valueOf("300"));
                } else if (weightDouble <= 26) {
                    ethambutolDose.getEditText().setText(String.valueOf("400"));
                } else if (weightDouble <= 30) {
                    ethambutolDose.getEditText().setText(String.valueOf("500"));
                } else if (weightDouble <= 59) {
                    ethambutolDose.getEditText().setText(String.valueOf("1500"));
                } else {
                    ethambutolDose.getEditText().setText(String.valueOf("2000"));
                }

                if (weightDouble <= 13) {
                    moxifloxacilinDose.getEditText().setText(String.valueOf(""));
                    moxifloxacilinDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 30) {
                    moxifloxacilinDose.getEditText().setText(String.valueOf("200"));
                } else {
                    moxifloxacilinDose.getEditText().setText(String.valueOf("400"));
                }

            }

            isoniazidDose.setVisibility(View.GONE);
            rifapentineDose.setVisibility(View.GONE);
            levofloxacinDose.setVisibility(View.GONE);
            ethionamideDose.setVisibility(View.GONE);
            ethambutolDose.setVisibility(View.VISIBLE);
            moxifloxacilinDose.setVisibility(View.VISIBLE);
            rifampicinDose.setVisibility(View.GONE);

        }


    }


}
