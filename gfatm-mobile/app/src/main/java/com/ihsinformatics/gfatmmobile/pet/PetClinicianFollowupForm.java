package com.ihsinformatics.gfatmmobile.pet;

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
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
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
 * Created by Rabbia on 11/24/2016.
 */

public class PetClinicianFollowupForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;


    TitledEditText weight;

    TitledRadioGroup cough;
    TitledRadioGroup coughDuration;
    TitledRadioGroup haemoptysis;
    TitledRadioGroup difficultyBreathing;
    TitledRadioGroup fever;
    TitledRadioGroup feverDuration;
    TitledRadioGroup weightLoss;
    TitledRadioGroup nightSweats;
    TitledRadioGroup lethargy;
    TitledRadioGroup swollenJoints;
    TitledRadioGroup backPain;
    TitledRadioGroup adenopathy;
    TitledRadioGroup vomiting;
    TitledRadioGroup giSymptoms;
    TitledRadioGroup lossInterestInActivity;

    TitledRadioGroup dizziness;
    TitledRadioGroup nausea;
    TitledRadioGroup abdominalPain;
    TitledRadioGroup lossOfAppetite;
    TitledRadioGroup jaundice;
    TitledRadioGroup rash;
    TitledRadioGroup tendonPain;
    TitledRadioGroup eyeProblem;
    TitledEditText otherSideEffects;
    TitledRadioGroup sideeffectsConsistent;

    TitledEditText missedDosage;
    TitledCheckBoxes actionPlan;
    TitledEditText medicationDiscontinueReason;
    TitledEditText medicationDiscontinueDuration;
    TitledEditText newMedication;
    TitledEditText newMedicationDuration;
    TitledRadioGroup petRegimen;
    TitledRadioGroup rifapentineAvailable;
    TitledEditText isoniazidDose;
    TitledEditText rifapentineDose;
    TitledEditText levofloxacinDose;
    TitledEditText ethionamideDose;
    TitledEditText ethambutolDose;
    TitledEditText moxifloxacilinDose;
    TitledCheckBoxes ancillaryDrugs;
    TitledEditText ancillaryDrugDuration;
    TitledEditText otherAncillaryDrugs;
    TitledEditText treatmentInterruptedReason;
    TitledEditText newInstruction;

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

    TitledEditText clincianNote;

    ScrollView scrollView;

    Boolean refillFlag = false;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 4;
        formName = Forms.PET_CLINICIAN_FOLLOWUP;
        form = Forms.pet_clinicianFollowup;

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


    @Override
    public void initViews() {

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        weight = new TitledEditText(context, null, getResources().getString(R.string.pet_weight), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false, "WEIGHT (KG)");

        MyLinearLayout linearLayout1 = new MyLinearLayout(context, getResources().getString(R.string.pet_tb_symptoms), App.VERTICAL);
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_cough), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "COUGH", new String[]{"YES", "NO"});
        coughDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_cough_duration), getResources().getStringArray(R.array.pet_cough_durations), getResources().getString(R.string.pet_less_than_2_weeks), App.VERTICAL, App.VERTICAL, true, "COUGH DURATION", new String[]{"COUGH LASTING LESS THAN 2 WEEKS", "COUGH LASTING MORE THAN 2 WEEKS", "COUGH LASTING MORE THAN 3 WEEKS", "UNKNOWN", "REFUSED"});
        haemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_haemoptysis), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "HEMOPTYSIS", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        difficultyBreathing = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_difficulty_breathing), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "DYSPNEA", new String[]{"YES", "NO"});
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_has_fever), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "FEVER", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        feverDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_fever_duration), getResources().getStringArray(R.array.pet_cough_durations), getResources().getString(R.string.pet_less_than_2_weeks), App.VERTICAL, App.VERTICAL, true, "FEVER DURATION", new String[]{"COUGH LASTING LESS THAN 2 WEEKS", "COUGH LASTING MORE THAN 2 WEEKS", "COUGH LASTING MORE THAN 3 WEEKS", "UNKNOWN", "REFUSED"});
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_have_weight_loss), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "WEIGHT LOSS", new String[]{"YES", "NO"});
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_have_night_sweats), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "NIGHT SWEATS", new String[]{"YES", "NO"});
        lethargy = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_lethargy), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "LETHARGY", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        swollenJoints = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_swollen_joints), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "JOINT SWELLING", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        backPain = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_back_pain), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "BACK PAIN", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        adenopathy = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adenopathy), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "ADENOPATHY", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        vomiting = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_vomiting_without_gi_symptoms), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "VOMITING", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        giSymptoms = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_gi_symptoms), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "GASTROINTESTINAL SYMPTOM", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        lossInterestInActivity = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_lost_activity_interest), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "LOSS OF INTEREST", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});

        linearLayout1.addView(cough);
        linearLayout1.addView(coughDuration);
        linearLayout1.addView(haemoptysis);
        linearLayout1.addView(difficultyBreathing);
        linearLayout1.addView(fever);
        linearLayout1.addView(feverDuration);
        linearLayout1.addView(weightLoss);
        linearLayout1.addView(nightSweats);
        linearLayout1.addView(lethargy);
        linearLayout1.addView(swollenJoints);
        linearLayout1.addView(backPain);
        linearLayout1.addView(adenopathy);
        linearLayout1.addView(vomiting);
        linearLayout1.addView(giSymptoms);
        linearLayout1.addView(lossInterestInActivity);

        MyLinearLayout linearLayout2 = new MyLinearLayout(context, getResources().getString(R.string.pet_medication_side_effects), App.VERTICAL);
        dizziness = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_dizziness), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "DIZZINESS AND GIDDINESS", getResources().getStringArray(R.array.yes_no_list_concept));
        nausea = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_nausea_vomiting), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "NAUSEA AND VOMITING", getResources().getStringArray(R.array.yes_no_list_concept));
        abdominalPain = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_abdominal_pain), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "ABDOMINAL PAIN", getResources().getStringArray(R.array.yes_no_list_concept));
        lossOfAppetite = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_loss_of_appetite), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "LOSS OF APPETITE", getResources().getStringArray(R.array.yes_no_list_concept));
        jaundice = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_jaundice), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "JAUNDICE", getResources().getStringArray(R.array.yes_no_list_concept));
        rash = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_rash), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "RASH", getResources().getStringArray(R.array.yes_no_list_concept));
        tendonPain = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_tendon_pain), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "MUSCLE PAIN", getResources().getStringArray(R.array.yes_no_list_concept));
        eyeProblem = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_eye_problems), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "VISION PROBLEM", getResources().getStringArray(R.array.yes_no_list_concept));
        otherSideEffects = new TitledEditText(context, null, getResources().getString(R.string.pet_other_side_effects), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "OTHER ADVERSE EVENT");
        otherSideEffects.getEditText().setSingleLine(false);
        otherSideEffects.getEditText().setMinimumHeight(150);
        sideeffectsConsistent = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_side_effect_consistent), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "COMPLAINTS CONSISTENT WITH DRUG SIDE EFFECTS", getResources().getStringArray(R.array.yes_no_list_concept));

        linearLayout2.addView(dizziness);
        linearLayout2.addView(nausea);
        linearLayout2.addView(abdominalPain);
        linearLayout2.addView(lossOfAppetite);
        linearLayout2.addView(jaundice);
        linearLayout2.addView(rash);
        linearLayout2.addView(tendonPain);
        linearLayout2.addView(eyeProblem);
        linearLayout2.addView(otherSideEffects);
        linearLayout2.addView(sideeffectsConsistent);

        MyLinearLayout linearLayout3 = new MyLinearLayout(context, getResources().getString(R.string.pet_clinician_review), App.VERTICAL);
        missedDosage = new TitledEditText(context, null, getResources().getString(R.string.pet_missed_dosed), "0", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false, "NUMBER OF MISSED MEDICATION DOSES IN LAST MONTH");
        actionPlan = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_action_plan), getResources().getStringArray(R.array.pet_action_plan), null, App.VERTICAL, App.VERTICAL, true, "ACTION PLAN" , new String[]{"CONTINUE MEDICATION (HIGH ADHERENCE)", "CONTINUE MEDICATION (LOW ADHERENCE)", "DISCONTINUE MEDICATION", "BEGIN CLINICAL MONITORING PROTOCOL", "GIVE NEW MEDICATION", "SEEK EXPERT CONSULTATION", "CHANGE DRUG DOSAGE", "GIVE ANCILLARY DRUG", "PLANNED TREATMENT INTERRUPTION"});
        medicationDiscontinueReason = new TitledEditText(context, null, getResources().getString(R.string.pet_discontinue_medication), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "REASON TO DISCONTINUE MEDICATION");
        medicationDiscontinueReason.getEditText().setSingleLine(false);
        medicationDiscontinueReason.getEditText().setMinimumHeight(150);
        medicationDiscontinueDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_discontinue_medication_duration), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true, "DURATION OF DISCONTINUATION IN DAYS");
        newMedication = new TitledEditText(context, null, getResources().getString(R.string.pet_new_medication), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "NEW MEDICATION");
        newMedication.getEditText().setSingleLine(false);
        newMedication.getEditText().setMinimumHeight(150);
        newMedicationDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_new_medication_duration), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true, "DURATION OF NEW MEDICATION IN DAYS");
        petRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.tpt_regimen), getResources().getStringArray(R.array.tpt_regimens), "", App.VERTICAL, App.VERTICAL, true, "POST-EXPOSURE TREATMENT REGIMEN", new String[]{"ISONIAZID PROPHYLAXIS", "ISONIAZID AND RIFAPENTINE", "LEVOFLOXACIN AND ETHIONAMIDE", "LEVOFLOXACIN AND ETHAMBUTOL", "LEVOFLOXACIN AND MOXIFLOXACILIN", "ETHIONAMIDE AND ETHAMBUTOL", "ETHIONAMIDE AND MOXIFLOXACILIN", "MOXIFLOXACILIN AND ETHAMBUTOL","1HP","3HR","4R"});
        isoniazidDose = new TitledEditText(context, null, getResources().getString(R.string.pet_isoniazid_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "ISONIAZID DOSE");
        rifapentineAvailable = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_rifapentine_available), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        rifapentineDose = new TitledEditText(context, null, getResources().getString(R.string.pet_rifapentine_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "RIFAPENTINE DOSE");
        levofloxacinDose = new TitledEditText(context, null, getResources().getString(R.string.pet_levofloxacin_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "LEVOFLOXACIN DOSE");
        ethionamideDose = new TitledEditText(context, null, getResources().getString(R.string.pet_ethionamide_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "ETHIONAMIDE DOSE");
        ethambutolDose = new TitledEditText(context, null, getResources().getString(R.string.pet_ethambutol_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "ETHAMBUTOL DOSE");
        moxifloxacilinDose = new TitledEditText(context, null, getResources().getString(R.string.pet_moxifloxacilin_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "MOXIFLOXACILIN DOSE");
        ancillaryDrugs = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_ancillary_drugs), getResources().getStringArray(R.array.pet_ancillary_drugs), null, App.VERTICAL, App.VERTICAL, true, "ANCILLARY DRUGS", new String[]{"IRON", "MULTIVITAMIN", "ANTHELMINTHIC", "PEDIASURE", "VITAMIN B COMPLEX", "CALPOL", "OTHER"});
        ancillaryDrugDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_ancillary_drug_duration_days), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "MEDICATION DURATION");
        otherAncillaryDrugs = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER ANCILLARY DRUGS");
        treatmentInterruptedReason = new TitledEditText(context, null, getResources().getString(R.string.pet_treatment_interrupted_reason), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "TREATMENT INTERRUPTION REASON");
        treatmentInterruptedReason.getEditText().setSingleLine(false);
        treatmentInterruptedReason.getEditText().setMinimumHeight(150);
        newInstruction = new TitledEditText(context, null, getResources().getString(R.string.pet_new_instructions), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "INSTRUCTIONS TO PATIENT AND/OR FAMILY");
        newInstruction.getEditText().setSingleLine(false);
        newInstruction.getEditText().setMinimumHeight(150);

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

        followupRequired = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_followup_required), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL, true, "CLINICAL FOLLOWUP NEEDED", getResources().getStringArray(R.array.yes_no_list_concept));
        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.pet_return_visit_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.VERTICAL);
        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.pet_doctor_notes), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "CLINICIAN NOTES (TEXT)");
        clincianNote.getEditText().setSingleLine(false);
        clincianNote.getEditText().setMinimumHeight(150);

        linearLayout3.addView(missedDosage);
        linearLayout3.addView(actionPlan);
        linearLayout3.addView(medicationDiscontinueReason);
        linearLayout3.addView(medicationDiscontinueDuration);
        linearLayout3.addView(newMedication);
        linearLayout3.addView(newMedicationDuration);
        linearLayout3.addView(petRegimen);
        linearLayout3.addView(rifapentineAvailable);
        linearLayout3.addView(isoniazidDose);
        linearLayout3.addView(rifapentineDose);
        linearLayout3.addView(levofloxacinDose);
        linearLayout3.addView(ethionamideDose);
        linearLayout3.addView(ethambutolDose);
        linearLayout3.addView(moxifloxacilinDose);
        linearLayout3.addView(ancillaryDrugs);
        linearLayout3.addView(otherAncillaryDrugs);
        linearLayout3.addView(ancillaryDrugDuration);
        linearLayout3.addView(treatmentInterruptedReason);
        linearLayout3.addView(newInstruction);

        views = new View[]{formDate.getButton(), weight.getEditText(), cough.getRadioGroup(), coughDuration.getRadioGroup(), haemoptysis.getRadioGroup(), difficultyBreathing.getRadioGroup(), fever.getRadioGroup(), feverDuration.getRadioGroup(),
                weightLoss.getRadioGroup(), nightSweats.getRadioGroup(), lethargy.getRadioGroup(), swollenJoints.getRadioGroup(), backPain.getRadioGroup(), adenopathy.getRadioGroup(),
                vomiting.getRadioGroup(), giSymptoms.getRadioGroup(), lossInterestInActivity.getRadioGroup(),
                dizziness.getRadioGroup(), nausea.getRadioGroup(), abdominalPain.getRadioGroup(), lossOfAppetite.getRadioGroup(), jaundice.getRadioGroup(), jaundice.getRadioGroup(), rash.getRadioGroup(),
                tendonPain.getRadioGroup(), eyeProblem.getRadioGroup(), otherSideEffects.getEditText(), sideeffectsConsistent.getRadioGroup(),
                missedDosage.getEditText(), actionPlan, medicationDiscontinueReason.getEditText(), medicationDiscontinueDuration.getEditText(), newMedication.getEditText(), newMedicationDuration.getEditText(), rifapentineAvailable.getRadioGroup(),
                petRegimen.getRadioGroup(), isoniazidDose.getEditText(), rifapentineDose.getEditText(), levofloxacinDose.getEditText(), ethionamideDose.getEditText(), ethambutolDose.getEditText(), moxifloxacilinDose.getEditText(), ancillaryDrugs, ancillaryDrugDuration.getEditText(), otherAncillaryDrugs.getEditText(),
                newInstruction.getEditText(), returnVisitDate.getButton(), rifapentineAvailable.getRadioGroup(), clincianNote.getEditText(), treatmentInterruptedReason.getEditText(),
                patientReferred.getRadioGroup(), followupRequired.getRadioGroup(), referalReasonCallCenter, otherReferalReasonCallCenter.getEditText(), referalReasonClinician, otherReferalReasonClinician.getEditText()
        };

        viewGroups = new View[][]{{formDate, weight, linearLayout1},
                {linearLayout2},
                {linearLayout3},
                {patientReferred, referredTo, referalReasonPsychologist, otherReferalReasonPsychologist, referalReasonSupervisor, otherReferalReasonSupervisor,
                        referalReasonCallCenter, otherReferalReasonCallCenter, referalReasonClinician, otherReferalReasonClinician, followupRequired, returnVisitDate}};

        formDate.getButton().setOnClickListener(this);
        returnVisitDate.getButton().setOnClickListener(this);
        cough.getRadioGroup().setOnCheckedChangeListener(this);
        fever.getRadioGroup().setOnCheckedChangeListener(this);
        petRegimen.getRadioGroup().setOnCheckedChangeListener(this);
        rifapentineAvailable.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : actionPlan.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        petRegimen.getRadioGroup().setOnCheckedChangeListener(this);
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

        for (CheckBox cb : actionPlan.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : ancillaryDrugs.getCheckedBoxes()) {
            cb.setOnCheckedChangeListener(this);
        }

        newMedicationDuration.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!App.get(newMedicationDuration).equals("")) {
                    int val = Integer.parseInt(App.get(newMedicationDuration));
                    if (val > 150) {
                        newMedicationDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                    } else {
                        newMedicationDuration.getEditText().setError(null);
                    }
                }

                calculateDosages();

            }
        });

        medicationDiscontinueDuration.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!App.get(medicationDiscontinueDuration).equals("")) {
                    int val = Integer.parseInt(App.get(medicationDiscontinueDuration));
                    if (val > 150) {
                        medicationDiscontinueDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                    } else {
                        medicationDiscontinueDuration.getEditText().setError(null);
                    }
                }

                calculateDosages();

            }
        });

        ancillaryDrugDuration.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!App.get(ancillaryDrugDuration).equals("")) {
                    int val = Integer.parseInt(App.get(ancillaryDrugDuration));
                    if (val > 150) {
                        ancillaryDrugDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                    } else {
                        ancillaryDrugDuration.getEditText().setError(null);
                    }
                }

                calculateDosages();

            }
        });

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
                    if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy))) {
                        if (dose > 300) {
                            isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded_1000));
                            isoniazidDose.getEditText().requestFocus();
                        } else isoniazidDose.getEditText().setError(null);
                    } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine))) {
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
                    if (dose > 1500)
                        ethambutolDose.getEditText().setError(getResources().getString(R.string.pet_ethambutol_dose_exceeded_1500));
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

        resetViews();

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        coughDuration.setVisibility(View.GONE);
        haemoptysis.setVisibility(View.GONE);
        feverDuration.setVisibility(View.GONE);

        if (App.getPatient().getPerson().getAge() > 5)
            weightLoss.setVisibility(View.GONE);
        else
            weightLoss.setVisibility(View.VISIBLE);

        medicationDiscontinueReason.setVisibility(View.GONE);
        medicationDiscontinueDuration.setVisibility(View.GONE);
        newMedication.setVisibility(View.GONE);
        newMedicationDuration.setVisibility(View.GONE);
        petRegimen.setVisibility(View.GONE);
        isoniazidDose.setVisibility(View.GONE);
        rifapentineAvailable.setVisibility(View.GONE);
        rifapentineDose.setVisibility(View.GONE);
        levofloxacinDose.setVisibility(View.GONE);
        ethionamideDose.setVisibility(View.GONE);
        ethambutolDose.setVisibility(View.GONE);
        moxifloxacilinDose.setVisibility(View.GONE);
        ancillaryDrugs.setVisibility(View.GONE);
        ancillaryDrugDuration.setVisibility(View.GONE);
        treatmentInterruptedReason.setVisibility(View.GONE);
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
                    String weight = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "WEIGHT (KG)");
                    String intervention = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_BASELINE_SCREENING, "INTERVENTION");

                    String date1 = "";
                    String date2 = "";
                    String date3 = "";
                    String petRegimen1 = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "POST-EXPOSURE TREATMENT REGIMEN");
                    if (!(petRegimen1 == null || petRegimen1.equals("")))
                        date1 = serverService.getLatestEncounterDateTime(App.getPatientId(), Forms.PET_ADVERSE_EVENTS);
                    String petRegimen2 = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "POST-EXPOSURE TREATMENT REGIMEN");
                    if (!(petRegimen2 == null || petRegimen2.equals("")))
                        date2 = serverService.getLatestEncounterDateTime(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP);
                    String petRegimen3 = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "POST-EXPOSURE TREATMENT REGIMEN");
                    if (!(petRegimen3 == null || petRegimen3.equals("")))
                        date3 = serverService.getLatestEncounterDateTime(App.getPatientId(), Forms.PET_TREATMENT_INITIATION);


                    String isonoazidDose = "";
                    String rifapentineDose = "";
                    String levofloxacinDose = "";
                    String ethionamideDose = "";

                    if ((date2 == null || date2.equals("")) && (date3 == null || date3.equals(""))) {
                        if (petRegimen1 == null)
                            petRegimen1 = "";
                        result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen1);
                        isonoazidDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "ISONIAZID DOSE");
                        rifapentineDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "RIFAPENTINE DOSE");
                        levofloxacinDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "LEVOFLOXACIN DOSE");
                        ethionamideDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "ETHIONAMIDE DOSE");
                    } else {
                        if (date2 == null || date2.equals("")) {
                            if (petRegimen3 == null)
                                petRegimen3 = "";
                            result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen3);
                            isonoazidDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "ISONIAZID DOSE");
                            rifapentineDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "RIFAPENTINE DOSE");
                            levofloxacinDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "LEVOFLOXACIN DOSE");
                            ethionamideDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "ETHIONAMIDE DOSE");
                        } else if (date3 == null || date3.equals("")) {
                            if (petRegimen2 == null)
                                petRegimen2 = "";
                            result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen2);
                            isonoazidDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "ISONIAZID DOSE");
                            rifapentineDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "RIFAPENTINE DOSE");
                            levofloxacinDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "LEVOFLOXACIN DOSE");
                            ethionamideDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "ETHIONAMIDE DOSE");
                        } else {

                            Date d2 = null;
                            Date d3 = null;
                            if (date2.contains("/")) {
                                d2 = App.stringToDate(date2, "dd/MM/yyyy");
                            } else {
                                d2 = App.stringToDate(date2, "yyyy-MM-dd");
                            }

                            if (date3.contains("/")) {
                                d3 = App.stringToDate(date3, "dd/MM/yyyy");
                            } else {
                                d3 = App.stringToDate(date3, "yyyy-MM-dd");
                            }

                            if (d2.equals(d3)) {
                                if (petRegimen3 == null)
                                    petRegimen3 = "";
                                result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen3);
                                isonoazidDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "ISONIAZID DOSE");
                                rifapentineDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "RIFAPENTINE DOSE");
                                levofloxacinDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "LEVOFLOXACIN DOSE");
                                ethionamideDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "ETHIONAMIDE DOSE");
                            } else if (d2.after(d3)) {
                                if (petRegimen2 == null)
                                    petRegimen2 = "";
                                result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen2);
                                isonoazidDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "ISONIAZID DOSE");
                                rifapentineDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "RIFAPENTINE DOSE");
                                levofloxacinDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "LEVOFLOXACIN DOSE");
                                ethionamideDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "ETHIONAMIDE DOSE");
                            } else {
                                petRegimen3 = "";
                                result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen3);
                                isonoazidDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "ISONIAZID DOSE");
                                rifapentineDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "RIFAPENTINE DOSE");
                                levofloxacinDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "LEVOFLOXACIN DOSE");
                                ethionamideDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "ETHIONAMIDE DOSE");
                            }

                        }
                    }

                    if (weight == null)
                        weight = "";
                    else
                        weight = weight.replace(".0", "");

                    result.put("WEIGHT (KG)", weight);

                    if (isonoazidDose == null)
                        isonoazidDose = "";
                    isonoazidDose = isonoazidDose.replace(".0", "");
                    result.put("ISONIAZID DOSE", isonoazidDose);

                    if (rifapentineDose == null)
                        rifapentineDose = "";
                    rifapentineDose = rifapentineDose.replace(".0", "");
                    result.put("RIFAPENTINE DOSE", rifapentineDose);

                    if (levofloxacinDose == null)
                        levofloxacinDose = "";
                    levofloxacinDose = levofloxacinDose.replace(".0", "");
                    result.put("LEVOFLOXACIN DOSE", levofloxacinDose);

                    if (ethionamideDose == null)
                        ethionamideDose = "";
                    ethionamideDose = ethionamideDose.replace(".0", "");
                    result.put("ETHIONAMIDE DOSE", ethionamideDose);

                    if (intervention == null)
                        intervention = "";
                    result.put("INTERVENTION", intervention);

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

                    if (result.get("POST-EXPOSURE TREATMENT REGIMEN") == null || result.get("POST-EXPOSURE TREATMENT REGIMEN").equals("")) {
                       /* final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        alertDialog.setMessage(getResources().getString(R.string.treatment_initiation_missing));
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
                        submitButton.setEnabled(false);*/
                        return;
                    } else submitButton.setEnabled(true);

                    weight.getEditText().setText(result.get("WEIGHT (KG)"));
                    isoniazidDose.getEditText().setText(result.get("ISONIAZID DOSE"));
                    rifapentineDose.getEditText().setText(result.get("RIFAPENTINE DOSE"));
                    levofloxacinDose.getEditText().setText(result.get("LEVOFLOXACIN DOSE"));
                    ethionamideDose.getEditText().setText(result.get("ETHIONAMIDE DOSE"));

                    for (RadioButton rb : petRegimen.getRadioGroup().getButtons()) {

                        if (rb.getText().equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy)) && result.get("POST-EXPOSURE TREATMENT REGIMEN").equals("ISONIAZID PROPHYLAXIS")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.pet_isoniazid_rifapentine)) && result.get("POST-EXPOSURE TREATMENT REGIMEN").equals("ISONIAZID AND RIFAPENTINE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.pet_levofloxacin_ethionamide)) && result.get("POST-EXPOSURE TREATMENT REGIMEN").equals("LEVOFLOXACIN AND ETHIONAMIDE")) {
                            rb.setChecked(true);
                            break;
                        }

                    }

                    rifapentineAvailable.setVisibility(View.GONE);
                    isoniazidDose.setVisibility(View.GONE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.GONE);
                    ethionamideDose.setVisibility(View.GONE);
                    ethambutolDose.setVisibility(View.GONE);
                    moxifloxacilinDose.setVisibility(View.GONE);

                }
            };
            autopopulateFormTask.execute("");
        }

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

            if (formDateCalendar.after(secondDateCalendar)) {

                secondDateCalendar.set(formDateCalendar.get(Calendar.YEAR), formDateCalendar.get(Calendar.MONTH), formDateCalendar.get(Calendar.DAY_OF_MONTH));
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }

        }
        if (!(returnVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = returnVisitDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }

        formDate.getButton().setEnabled(true);
        returnVisitDate.getButton().setEnabled(true);
    }


    @Override
    public boolean validate() {

        View view = null;
        Boolean error = super.validate();


        if (ancillaryDrugDuration.getVisibility() == View.VISIBLE) {
            int val = Integer.parseInt(App.get(ancillaryDrugDuration));
            if (val > 150) {
                ancillaryDrugDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                ancillaryDrugDuration.getEditText().requestFocus();
                error = true;
                gotoLastPage();
                view = null;
            } else {
                ancillaryDrugDuration.getEditText().setError(null);
                ancillaryDrugDuration.getEditText().requestFocus();
            }
        } else {
            ancillaryDrugDuration.getEditText().setError(null);
            ancillaryDrugDuration.getEditText().clearFocus();
        }



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
                if (dose > 1500) {
                    ethambutolDose.getEditText().setError(getResources().getString(R.string.pet_ethambutol_dose_exceeded_1500));
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
        if (isoniazidDose.getVisibility() == View.VISIBLE) {
            if (App.get(isoniazidDose).isEmpty()) {
                isoniazidDose.getEditText().setError(getString(R.string.empty_field));
                isoniazidDose.getEditText().requestFocus();
                view = null;
                error = true;
                gotoLastPage();
            } else {
                Double dose = Double.parseDouble(App.get(isoniazidDose));
                if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy))) {
                    if (dose > 300) {
                        isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded_300));
                        isoniazidDose.getEditText().requestFocus();
                        view = null;
                        error = true;
                    } else {
                        isoniazidDose.getEditText().setError(null);
                        isoniazidDose.getEditText().clearFocus();
                    }
                } else {
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


        if (newMedicationDuration.getVisibility() == View.VISIBLE && App.get(newMedicationDuration).isEmpty()) {
            newMedicationDuration.getEditText().setError(getString(R.string.empty_field));
            newMedicationDuration.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        } else {

            if (newMedicationDuration.getVisibility() == View.VISIBLE) {
                int val = Integer.parseInt(App.get(newMedicationDuration));
                if (val > 150) {
                    newMedicationDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                    newMedicationDuration.getEditText().requestFocus();
                    error = true;
                    gotoLastPage();
                    view = null;
                } else {
                    newMedicationDuration.getEditText().setError(null);
                    newMedicationDuration.getEditText().requestFocus();
                }
            } else {
                newMedicationDuration.getEditText().setError(null);
                newMedicationDuration.getEditText().clearFocus();
            }

        }
        if (medicationDiscontinueDuration.getVisibility() == View.VISIBLE && App.get(medicationDiscontinueDuration).isEmpty()) {
            medicationDiscontinueDuration.getEditText().setError(getString(R.string.empty_field));
            medicationDiscontinueDuration.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        } else {
            if (medicationDiscontinueDuration.getVisibility() == View.VISIBLE) {
                int val = Integer.parseInt(App.get(medicationDiscontinueDuration));
                if (val > 150) {
                    medicationDiscontinueDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                    medicationDiscontinueDuration.getEditText().requestFocus();
                    error = true;
                    gotoLastPage();
                    view = null;
                } else {
                    medicationDiscontinueDuration.getEditText().setError(null);
                    medicationDiscontinueDuration.getEditText().requestFocus();
                }
            } else {
                medicationDiscontinueDuration.getEditText().setError(null);
                medicationDiscontinueDuration.getEditText().clearFocus();
            }
        }


        if (error) {

            final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
            alertDialog.setMessage(getResources().getString(R.string.form_error));
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
                                        missedDosage.getEditText().clearFocus();
                                        medicationDiscontinueReason.getEditText().clearFocus();
                                        medicationDiscontinueDuration.getEditText().clearFocus();
                                        newMedicationDuration.getEditText().clearFocus();
                                        newMedication.getEditText().clearFocus();
                                        ethionamideDose.getEditText().clearFocus();
                                        levofloxacinDose.getEditText().clearFocus();
                                        rifapentineDose.getEditText().clearFocus();
                                        isoniazidDose.getEditText().clearFocus();
                                        ancillaryDrugDuration.getEditText().clearFocus();
                                        newInstruction.getEditText().clearFocus();
                                    }
                                }
                            });
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

        if (returnVisitDate.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDate(secondDateCalendar)});


        personAttribute.put("Health Center", serverService.getLocationUuid(App.getLocation()));

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
        return false;
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
        } else if (view == returnVisitDate.getButton()) {
            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowFutureDate", true);
            args.putBoolean("allowPastDate", false);
            args.putString("formDate", formDate.getButtonText());
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");*/
            returnVisitDate.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar, true, false, true);

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

        for (CheckBox cb : actionPlan.getCheckedBoxes()) {

            if (App.get(cb).equals(getResources().getString(R.string.pet_discontinue_medication))) {
                if (cb.isChecked()) {
                    medicationDiscontinueReason.setVisibility(View.VISIBLE);
                    medicationDiscontinueDuration.setVisibility(View.VISIBLE);
                } else {
                    medicationDiscontinueReason.setVisibility(View.GONE);
                    medicationDiscontinueDuration.setVisibility(View.GONE);
                }
            }

            if (App.get(cb).equals(getResources().getString(R.string.pet_new_medication))) {
                if (cb.isChecked()) {
                    newMedication.setVisibility(View.VISIBLE);
                    newMedicationDuration.setVisibility(View.VISIBLE);
                } else {
                    newMedication.setVisibility(View.GONE);
                    newMedicationDuration.setVisibility(View.GONE);
                }
            }

            if (App.get(cb).equals(getResources().getString(R.string.pet_ancillary_drug))) {
                if (cb.isChecked()) {
                    ancillaryDrugs.setVisibility(View.VISIBLE);
                    ancillaryDrugDuration.setVisibility(View.VISIBLE);
                } else {
                    ancillaryDrugs.setVisibility(View.GONE);
                    ancillaryDrugDuration.setVisibility(View.GONE);
                    otherAncillaryDrugs.setVisibility(View.GONE);
                }
            }

            if (App.get(cb).equals(getResources().getString(R.string.pet_treatment_interrupted))) {
                if (cb.isChecked()) {
                    treatmentInterruptedReason.setVisibility(View.VISIBLE);
                } else {
                    treatmentInterruptedReason.setVisibility(View.GONE);
                }
            }


            if (App.get(cb).equals(getResources().getString(R.string.pet_change_drug_dosage))) {
                if (cb.isChecked()) {
                    petRegimen.setVisibility(View.VISIBLE);
                    isoniazidDose.setVisibility(View.GONE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.GONE);
                    ethionamideDose.setVisibility(View.GONE);
                    if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy)))
                        isoniazidDose.setVisibility(View.VISIBLE);
                    else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine))) {
                        isoniazidDose.setVisibility(View.VISIBLE);
                        rifapentineDose.setVisibility(View.VISIBLE);
                    } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethionamide))) {
                        levofloxacinDose.setVisibility(View.VISIBLE);
                        ethionamideDose.setVisibility(View.VISIBLE);
                    }

                } else {
                    petRegimen.setVisibility(View.GONE);
                    isoniazidDose.setVisibility(View.GONE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.GONE);
                    ethionamideDose.setVisibility(View.GONE);
                    rifapentineAvailable.setVisibility(View.GONE);
                }
            }
        }

        otherAncillaryDrugs.setVisibility(View.GONE);
        for (CheckBox cb : ancillaryDrugs.getCheckedBoxes()) {
            if (cb.isChecked())
                ancillaryDrugs.getQuestionView().setError(null);

            if (cb.isChecked() && cb.getText().equals(getString(R.string.other)))
                otherAncillaryDrugs.setVisibility(View.VISIBLE);
        }

        setReferralViews();

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
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (group == cough.getRadioGroup()) {
            if (App.get(cough).equals(getResources().getString(R.string.yes))) {
                coughDuration.setVisibility(View.VISIBLE);
                haemoptysis.setVisibility(View.VISIBLE);
            } else {
                coughDuration.setVisibility(View.GONE);
                haemoptysis.setVisibility(View.GONE);
            }
        } else if (group == fever.getRadioGroup()) {
            if (App.get(fever).equals(getResources().getString(R.string.yes))) {
                feverDuration.setVisibility(View.VISIBLE);
            } else {
                feverDuration.setVisibility(View.GONE);
            }
        } else if (group == petRegimen.getRadioGroup()) {

            calculateDosages();
        } else if (group == rifapentineAvailable.getRadioGroup()) {

            int age = App.getPatient().getPerson().getAge();
            Double weightDouble = Double.parseDouble("0");
            if (!App.get(weight).equals("")) {
                weightDouble = Double.parseDouble(App.get(weight));
            }

            Double w = 1.0;

            rifapentineAvailable.setVisibility(View.VISIBLE);
            if (App.get(rifapentineAvailable).equals(getResources().getString(R.string.no))) {

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

                if (w > 300)
                    isoniazidDose.getEditText().setError(getString(R.string.pet_isoniazid_dose_exceeded_300));

            } else {

                w = 1.0;


                if (age < 2) {
                    w = weightDouble * 10f;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));

                    rifapentineDose.getEditText().setHint("Not recommended");
                } else if (age < 12) {
                    w = weightDouble * 15;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));

                    rifapentineDose.getEditText().setHint("300 - 450 mg");
                } else {
                    w = weightDouble * 25;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));

                    rifapentineDose.getEditText().setHint("450 - 900 mg");
                }

                isoniazidDose.setVisibility(View.VISIBLE);
                rifapentineDose.setVisibility(View.VISIBLE);
                levofloxacinDose.setVisibility(View.GONE);
                ethionamideDose.setVisibility(View.GONE);

            }
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
        }

    }

    @Override
    public void refill(int encounterId) {
        super.refill(encounterId);
        refillFlag = true;

        OfflineForm fo = serverService.getSavedFormById(encounterId);

        ArrayList<String[][]> obsValue = fo.getObsValue();


        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("RIFAPENTINE DOSE")) {
                rifapentineDose.getEditText().setText(obs[0][1]);
                rifapentineAvailable.setVisibility(View.VISIBLE);
                for (RadioButton rb : rifapentineAvailable.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes))) {
                        rb.setChecked(true);
                    }
                }
            }
            if (obs[0][0].equals("RETURN VISIT DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
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

        int age = App.getPatient().getPerson().getAge();
        Double weightDouble = Double.parseDouble("0");
        if (!App.get(weight).equals("")) {
            weightDouble = Double.parseDouble(App.get(weight));
        }

        if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy))) {

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
            rifapentineAvailable.setVisibility(View.GONE);
            ethambutolDose.setVisibility(View.GONE);
            moxifloxacilinDose.setVisibility(View.GONE);

            if (w > 300)
                isoniazidDose.getEditText().setError(getString(R.string.pet_isoniazid_dose_exceeded_300));

        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine))) {

            Double w = 1.0;

            rifapentineAvailable.setVisibility(View.VISIBLE);
            if (App.get(rifapentineAvailable).equals(getResources().getString(R.string.no))) {

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

                if (w > 300)
                    isoniazidDose.getEditText().setError(getString(R.string.pet_isoniazid_dose_exceeded_300));

            } else {

                w = 1.0;


                if (age < 2) {
                    w = weightDouble * 10f;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));

                    rifapentineDose.getEditText().setHint("Not recommended");
                } else if (age < 12) {
                    w = weightDouble * 15;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));

                    rifapentineDose.getEditText().setHint("300 - 450 mg");
                } else {
                    w = weightDouble * 25;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));

                    rifapentineDose.getEditText().setHint("450 - 900 mg");
                }

                isoniazidDose.setVisibility(View.VISIBLE);
                rifapentineDose.setVisibility(View.VISIBLE);
                levofloxacinDose.setVisibility(View.GONE);
                ethionamideDose.setVisibility(View.GONE);
                ethambutolDose.setVisibility(View.GONE);
                moxifloxacilinDose.setVisibility(View.GONE);

            }

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
            rifapentineAvailable.setVisibility(View.GONE);
            ethambutolDose.setVisibility(View.GONE);
            moxifloxacilinDose.setVisibility(View.GONE);

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
            rifapentineAvailable.setVisibility(View.GONE);
            ethambutolDose.setVisibility(View.VISIBLE);
            moxifloxacilinDose.setVisibility(View.GONE);


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
            rifapentineAvailable.setVisibility(View.GONE);
            ethambutolDose.setVisibility(View.GONE);
            moxifloxacilinDose.setVisibility(View.VISIBLE);

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
            rifapentineAvailable.setVisibility(View.GONE);
            ethambutolDose.setVisibility(View.VISIBLE);
            moxifloxacilinDose.setVisibility(View.GONE);

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
            rifapentineAvailable.setVisibility(View.GONE);
            ethambutolDose.setVisibility(View.GONE);
            moxifloxacilinDose.setVisibility(View.VISIBLE);

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
            rifapentineAvailable.setVisibility(View.GONE);
            ethambutolDose.setVisibility(View.VISIBLE);
            moxifloxacilinDose.setVisibility(View.VISIBLE);

        }


    }

}
