package com.ihsinformatics.gfatmmobile.pet;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
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

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
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

/**
 * Created by Rabbia on 11/24/2016.
 */

public class PetAdverseEventForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledEditText weight;

    TitledCheckBoxes symptoms;
    TitledEditText otherSideEffects;
    TitledRadioGroup sideeffectsConsistent;
    TitledRadioGroup severity;

    TitledCheckBoxes actionPlan;
    TitledEditText medicationDiscontinueReason;
    TitledEditText medicationDiscontinueDuration;

    TitledRadioGroup petRegimen;
    TitledRadioGroup rifapentineAvailable;
    TitledEditText isoniazidDose;
    TitledEditText rifapentineDose;
    TitledEditText levofloxacinDose;
    TitledEditText ethionamideDose;
    TitledEditText ethambutolDose;
    TitledEditText moxifloxacilinDose;

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

    TitledCheckBoxes ancillaryDrugs;
    TitledEditText ancillaryDrugDuration;
    TitledEditText otherAncillaryDrugs;
    TitledEditText treatmentInterruptedReason;
    TitledEditText newInstruction;
    TitledButton returnVisitDate;

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

    ScrollView scrollView;

    Boolean refillFlag = false;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 3;
        formName = Forms.PET_ADVERSE_EVENTS;
        form = Forms.pet_adverseEvents;

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
        weight = new TitledEditText(context, null, getResources().getString(R.string.pet_weight), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false,"WEIGHT (KG)");

        symptoms = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_symptoms), getResources().getStringArray(R.array.pet_pet_symptoms_list), null, App.VERTICAL, App.VERTICAL, true);

        MyLinearLayout linearLayout1 = new MyLinearLayout(context, getResources().getString(R.string.pet_adverse_events_followup_details), App.VERTICAL);
        otherSideEffects = new TitledEditText(context, null, getResources().getString(R.string.pet_other_side_effects), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        otherSideEffects.getEditText().setSingleLine(false);
        otherSideEffects.getEditText().setMinimumHeight(150);
        sideeffectsConsistent = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_side_effect_consistent), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        severity = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adverse_event_severity), getResources().getStringArray(R.array.pet_adverse_event_severity_list), getResources().getString(R.string.pet_mild), App.VERTICAL, App.VERTICAL);

        linearLayout1.addView(symptoms);
        linearLayout1.addView(otherSideEffects);
        linearLayout1.addView(sideeffectsConsistent);
        linearLayout1.addView(severity);

        MyLinearLayout linearLayout2 = new MyLinearLayout(context, getResources().getString(R.string.pet_symptoms_require), App.VERTICAL);
        actionPlan = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_action_plan), getResources().getStringArray(R.array.pet_action_plan), null, App.VERTICAL, App.VERTICAL, true);
        medicationDiscontinueReason = new TitledEditText(context, null, getResources().getString(R.string.pet_discontinue_medication_reason), "", "", 250, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        medicationDiscontinueReason.getEditText().setSingleLine(false);
        medicationDiscontinueReason.getEditText().setMinimumHeight(150);
        medicationDiscontinueDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_discontinue_medication_duration), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        petRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_regimen), getResources().getStringArray(R.array.pet_regimens), "", App.VERTICAL, App.VERTICAL);
        isoniazidDose = new TitledEditText(context, null, getResources().getString(R.string.pet_isoniazid_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        rifapentineAvailable = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_rifapentine_available), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        rifapentineDose = new TitledEditText(context, null, getResources().getString(R.string.pet_rifapentine_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        levofloxacinDose = new TitledEditText(context, null, getResources().getString(R.string.pet_levofloxacin_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        ethionamideDose = new TitledEditText(context, null, getResources().getString(R.string.pet_ethionamide_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        ethambutolDose = new TitledEditText(context, null, getResources().getString(R.string.pet_ethambutol_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        moxifloxacilinDose = new TitledEditText(context, null, getResources().getString(R.string.pet_moxifloxacilin_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);

        treatmentPlan = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_treatment_plan), getResources().getStringArray(R.array.ctb_ti_list), null, App.VERTICAL, App.VERTICAL,true,"TREATMENT PLAN",new String[]{ "INTENSIVE PHASE" ,"CONTINUE REGIMEN"});

        intensivePhaseRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_regimen), getResources().getStringArray(R.array.ctb_regimen_list), getResources().getString(R.string.ctb_rhz), App.HORIZONTAL, App.VERTICAL,true,"REGIMEN", new String[]{"RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS", "RIFAMPICIN/ISONIAZID/PYRAZINAMIDE"});
        typeFixedDosePrescribedIntensive = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_type_of_fixed_dose), getResources().getStringArray(R.array.ctb_type_of_fixed_dose_list), null, App.VERTICAL,true,"PAEDIATRIC DOSE COMBINATION",new String[]{ "CURRENT FORULATION" , "NEW FORMULATION", "ADULT FORMULATION"});
        currentTabletsofRHZ = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_current_formulation_number_of_tablet_rhz), getResources().getStringArray(R.array.ctb_1_to_5_list), null, App.HORIZONTAL, App.VERTICAL,true, "CURRENT FORMULATION OF TABLETS OF RHZ", getResources().getStringArray(R.array.ctb_1_to_5_list));
        currentTabletsofE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_current_formulation_number_of_tablet_e), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL,true, "CURRENT FORMULATION OF TABLETS OF  E", getResources().getStringArray(R.array.ctb_number_of_tablets));
        newTabletsofRHZ = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_new_formulation_number_of_tablet_rhz), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL,true, "NEW FORMULATION OF TABLETS OF RHZ", getResources().getStringArray(R.array.ctb_number_of_tablets));
        newTabletsofE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_new_formulation_number_of_tablet_e), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL,true, "NEW FORMULATION OF TABLETS OF E", getResources().getStringArray(R.array.ctb_number_of_tablets));
        adultFormulationofHRZE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_adult_formulation), getResources().getStringArray(R.array.ctb_2_to_5_list), null, App.HORIZONTAL, App.VERTICAL,true, "ADULT FORMULATION OF TABLETS OF RHZE", getResources().getStringArray(R.array.ctb_2_to_5_list));
        continuationPhaseRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_continuation_phase_regimen), getResources().getStringArray(R.array.ctb_continuation_phase_regimen_list), null, App.HORIZONTAL, App.VERTICAL,true,"REGIMEN", new String[]{"RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS", "RIFAMPICIN/ISONIAZID/PYRAZINAMIDE"});
        typeFixedDosePrescribedContinuation = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_type_of_fixed_dose), getResources().getStringArray(R.array.ctb_type_of_dose_continuation_list), null, App.VERTICAL,true,"PAEDIATRIC FIXED DOSE COMBINATION FOR CONTINUATION PHASE",new String[]{ "CURRENT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE" , "NEW FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE" ,"ADULT FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE", "ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE" });
        currentTabletsOfContinuationRH = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_current_formulation_continuation_rh), getResources().getStringArray(R.array.ctb_1_to_5_list), null, App.HORIZONTAL, App.VERTICAL,true, "CURRENT FORMULATION OF TABLETS OF RH", getResources().getStringArray(R.array.ctb_1_to_5_list));
        currentTabletsOfContinuationE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_current_formulation_continuation_e), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL,true, "CURRENT FORMULATION OF TABLETS OF E FOR CONTINUATION PHASE", getResources().getStringArray(R.array.ctb_number_of_tablets));
        newTabletsOfContinuationRH = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_new_formulation_continuation_rh), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL,true, "NEW FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE", getResources().getStringArray(R.array.ctb_number_of_tablets));
        newTabletsOfContinuationE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_new_formulation_continuation_e), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL,true, "NEW FORMULATION OF TABLET OF E FOR CONTINUATION PHASE", getResources().getStringArray(R.array.ctb_number_of_tablets));
        adultFormulationOfContinuationRH = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_if_adult_formulation_continuation_rh), getResources().getStringArray(R.array.ctb_1_to_2), null, App.HORIZONTAL, App.VERTICAL,true, "ADULT FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE", getResources().getStringArray(R.array.ctb_1_to_2));
        adultFormulationOfContinuationRHE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_if_adult_formulation_continuation_rhe), getResources().getStringArray(R.array.ctb_2_to_4), null, App.HORIZONTAL, App.VERTICAL,true, "ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE", getResources().getStringArray(R.array.ctb_2_to_4));

        ancillaryDrugs = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_ancillary_drugs), getResources().getStringArray(R.array.pet_ancillary_drugs), null, App.VERTICAL, App.VERTICAL, true);
        ancillaryDrugDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_ancillary_drug_duration_days), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        otherAncillaryDrugs = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        treatmentInterruptedReason = new TitledEditText(context, null, getResources().getString(R.string.pet_treatment_interrupted_reason), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        treatmentInterruptedReason.getEditText().setSingleLine(false);
        treatmentInterruptedReason.getEditText().setMinimumHeight(150);
        newInstruction = new TitledEditText(context, null, getResources().getString(R.string.pet_new_instructions), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        newInstruction.getEditText().setSingleLine(false);
        newInstruction.getEditText().setMinimumHeight(150);
        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.pet_return_visit_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.VERTICAL);
        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.pet_doctor_notes), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        clincianNote.getEditText().setSingleLine(false);
        clincianNote.getEditText().setMinimumHeight(150);

        linearLayout2.addView(actionPlan);
        linearLayout2.addView(medicationDiscontinueReason);
        linearLayout2.addView(medicationDiscontinueDuration);
        linearLayout2.addView(petRegimen);
        linearLayout2.addView(rifapentineAvailable);
        linearLayout2.addView(isoniazidDose);
        linearLayout2.addView(rifapentineDose);
        linearLayout2.addView(levofloxacinDose);
        linearLayout2.addView(ethionamideDose);
        linearLayout2.addView(ethambutolDose);
        linearLayout2.addView(moxifloxacilinDose);

        linearLayout2.addView(treatmentPlan);
        linearLayout2.addView(intensivePhaseRegimen);
        linearLayout2.addView(typeFixedDosePrescribedIntensive);
        linearLayout2.addView(currentTabletsofRHZ);
        linearLayout2.addView(currentTabletsofE);
        linearLayout2.addView(newTabletsofRHZ);
        linearLayout2.addView(newTabletsofE);
        linearLayout2.addView(adultFormulationofHRZE);
        linearLayout2.addView(continuationPhaseRegimen);
        linearLayout2.addView(typeFixedDosePrescribedContinuation);
        linearLayout2.addView(currentTabletsOfContinuationRH);
        linearLayout2.addView(currentTabletsOfContinuationE);
        linearLayout2.addView(newTabletsOfContinuationRH);
        linearLayout2.addView(newTabletsOfContinuationE);
        linearLayout2.addView(adultFormulationOfContinuationRH);
        linearLayout2.addView(adultFormulationOfContinuationRHE);

        linearLayout2.addView(ancillaryDrugs);
        linearLayout2.addView(otherAncillaryDrugs);
        linearLayout2.addView(ancillaryDrugDuration);
        linearLayout2.addView(treatmentInterruptedReason);
        linearLayout2.addView(newInstruction);
        linearLayout2.addView(returnVisitDate);
        linearLayout2.addView(clincianNote);

        patientReferred  = new TitledRadioGroup(context, null, getResources().getString(R.string.refer_patient), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL,true);
        referredTo = new TitledCheckBoxes(context, null, getResources().getString(R.string.refer_patient_to), getResources().getStringArray(R.array.refer_patient_to_option), null, App.VERTICAL, App.VERTICAL, true, "PATIENT REFERRED TO", new String[]{"COUNSELOR", "PSYCHOLOGIST", "CLINICAL OFFICER/DOCTOR", "CALL CENTER", "FIELD SUPERVISOR", "SITE SUPERVISOR"});
        referalReasonPsychologist = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_psychologist), getResources().getStringArray(R.array.referral_reason_for_psychologist_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR PSYCHOLOGIST/COUNSELOR REFERRAL", new String[]{"CHECK FOR TREATMENT ADHERENCE", "PSYCHOLOGICAL EVALUATION", "BEHAVIORAL ISSUES", "REFUSAL OF TREATMENT BY PATIENT", "OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR"});
       otherReferalReasonPsychologist = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR");
        referalReasonSupervisor = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_supervisor), getResources().getStringArray(R.array.referral_reason_for_supervisor_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR SUPERVISOR REFERRAL", new String[]{"CONTACT SCREENING REMINDER", "TREATMENT FOLLOWUP REMINDER", "CHECK FOR TREATMENT ADHERENCE", "INVESTIGATION OF REPORT COLLECTION", "ADVERSE EVENTS", "MEDICINE COLLECTION", "OTHER REFERRAL REASON TO SUPERVISOR"});
        otherReferalReasonSupervisor = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO SUPERVISOR");
        referalReasonCallCenter = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_center), getResources().getStringArray(R.array.referral_reason_for_call_center_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR CALL CENTER REFERRAL", new String[]{"CONTACT SCREENING REMINDER", "TREATMENT FOLLOWUP REMINDER", "CHECK FOR TREATMENT ADHERENCE", "INVESTIGATION OF REPORT COLLECTION", "ADVERSE EVENTS", "MEDICINE COLLECTION", "OTHER REFERRAL REASON TO CALL CENTER"});
        otherReferalReasonCallCenter = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO CALL CENTER");
        referalReasonClinician = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_clinician), getResources().getStringArray(R.array.referral_reason_for_clinician_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR CLINICIAN REFERRAL", new String[]{"EXPERT OPINION", "ADVERSE EVENTS", "OTHER REFERRAL REASON TO CLINICIAN"});
        otherReferalReasonClinician = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO CLINICIAN");

        views = new View[]{formDate.getButton(), weight.getEditText(), otherSideEffects.getEditText(), sideeffectsConsistent.getRadioGroup(),
                actionPlan, medicationDiscontinueReason.getEditText(), medicationDiscontinueDuration.getEditText(),
                petRegimen.getRadioGroup(), isoniazidDose.getEditText(), rifapentineDose.getEditText(), levofloxacinDose.getEditText(), ethionamideDose.getEditText(), ethambutolDose.getEditText(), moxifloxacilinDose.getEditText(), ancillaryDrugs, ancillaryDrugDuration.getEditText(),
                newInstruction.getEditText(), returnVisitDate.getButton(), rifapentineAvailable.getRadioGroup(), clincianNote.getEditText(), symptoms, severity.getRadioGroup(), treatmentInterruptedReason.getEditText(), otherAncillaryDrugs.getEditText(),
                patientReferred.getRadioGroup(), referalReasonCallCenter, otherReferalReasonCallCenter.getEditText(), referalReasonClinician, otherReferalReasonClinician.getEditText(), petRegimen,
                treatmentPlan.getRadioGroup(), intensivePhaseRegimen.getRadioGroup(),typeFixedDosePrescribedIntensive.getSpinner(),currentTabletsofRHZ.getRadioGroup(),currentTabletsofE.getRadioGroup(),
                newTabletsofRHZ.getRadioGroup(),newTabletsofE.getRadioGroup(),adultFormulationofHRZE.getRadioGroup(), continuationPhaseRegimen.getRadioGroup(),typeFixedDosePrescribedContinuation.getSpinner(),
                currentTabletsOfContinuationRH.getRadioGroup(), currentTabletsOfContinuationE.getRadioGroup(), newTabletsOfContinuationRH.getRadioGroup(), newTabletsOfContinuationE.getRadioGroup(),
                adultFormulationOfContinuationRH.getRadioGroup(), adultFormulationOfContinuationRHE.getRadioGroup()
        };

        viewGroups = new View[][]{{formDate, weight, linearLayout1},
                {linearLayout2}, {patientReferred, referredTo, referalReasonPsychologist, otherReferalReasonPsychologist, referalReasonSupervisor, otherReferalReasonSupervisor,
                referalReasonCallCenter, otherReferalReasonCallCenter, referalReasonClinician, otherReferalReasonClinician}
        };

        formDate.getButton().setOnClickListener(this);
        returnVisitDate.getButton().setOnClickListener(this);
        petRegimen.getRadioGroup().setOnCheckedChangeListener(this);
        rifapentineAvailable.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : actionPlan.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : symptoms.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        petRegimen.getRadioGroup().setOnCheckedChangeListener(this);
        for(CheckBox cb: ancillaryDrugs.getCheckedBoxes())
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

        weight.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!App.get(weight).equals("")){
                    Double w = Double.parseDouble(App.get(weight));
                    if(w < 0.5 || w > 700.0)
                        weight.getEditText().setError(getString(R.string.pet_invalid_weight_range));
                    else
                        weight.getEditText().setError(null);
                }

                calculatePetDosages();

                if(s.length()>0 && s.toString().matches("^[0-9]*.[0-9]{0,2}$")){
                    float value = Float.parseFloat(s.toString());

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
                        currentTabletsofE.getRadioGroup().getButtons().get(1).setChecked(true);

                    }
                    else if(value>=15 && value<=19){
                        currentTabletsOfContinuationRH.getRadioGroup().getButtons().get(3).setChecked(true);
                        currentTabletsOfContinuationE.getRadioGroup().getButtons().get(3).setChecked(true);
                        currentTabletsofRHZ.getRadioGroup().getButtons().get(3).setChecked(true);
                        currentTabletsofE.getRadioGroup().getButtons().get(2).setChecked(true);

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
                        adultFormulationofHRZE.getRadioGroup().getButtons().get(0).setChecked(true);
                    }
                    else if(value>=30 && value<=39){
                        adultFormulationOfContinuationRHE.getRadioGroup().getButtons().get(0).setChecked(true);
                        adultFormulationOfContinuationRH.getRadioGroup().getButtons().get(0).setChecked(true);
                        adultFormulationofHRZE.getRadioGroup().getButtons().get(0).setChecked(true);
                    }
                    else if(value>=40 && value<=54){
                        adultFormulationOfContinuationRHE.getRadioGroup().getButtons().get(1).setChecked(true);
                        adultFormulationOfContinuationRH.getRadioGroup().getButtons().get(1).setChecked(true);
                        adultFormulationofHRZE.getRadioGroup().getButtons().get(1).setChecked(true);
                    }
                    else if(value>=55 && value<=70){
                        adultFormulationOfContinuationRHE.getRadioGroup().getButtons().get(2).setChecked(true);
                        adultFormulationOfContinuationRH.getRadioGroup().getButtons().get(2).setChecked(true);
                        adultFormulationofHRZE.getRadioGroup().getButtons().get(2).setChecked(true);
                    }
                    else if(value>70){
                        adultFormulationOfContinuationRHE.getRadioGroup().getButtons().get(2).setChecked(true);
                        adultFormulationOfContinuationRH.getRadioGroup().getButtons().get(2).setChecked(true);
                        adultFormulationofHRZE.getRadioGroup().getButtons().get(3).setChecked(true);
                    }
                }

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
                        }
                        else isoniazidDose.getEditText().setError(null);
                    } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine))) {
                        if (dose > 1000) {
                            isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded_1000));
                            isoniazidDose.getEditText().requestFocus();
                        }
                        else isoniazidDose.getEditText().setError(null);
                    }
                    else isoniazidDose.getEditText().setError(null);
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


        ancillaryDrugDuration.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!App.get(ancillaryDrugDuration).equals("") && ancillaryDrugDuration.getVisibility() == View.VISIBLE) {
                    int val = Integer.parseInt(App.get(ancillaryDrugDuration));
                    if(val > 150)
                        ancillaryDrugDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                    else
                        ancillaryDrugDuration.getEditText().setError(null);
                } else
                    ancillaryDrugDuration.getEditText().setError(null);
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
                if (!App.get(medicationDiscontinueDuration).equals("") && medicationDiscontinueDuration.getVisibility() == View.VISIBLE) {
                    int val = Integer.parseInt(App.get(medicationDiscontinueDuration));
                    if(val > 150)
                        medicationDiscontinueDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                    else
                        medicationDiscontinueDuration.getEditText().setError(null);
                } else
                    medicationDiscontinueDuration.getEditText().setError(null);
            }
        });

        resetViews();

    }

    @Override
    public void updateDisplay() {

        if(refillFlag){
            refillFlag = true;
            return;
        }

        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();
            personDOB = personDOB.substring(0,10);

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

            if(formDateCalendar.after(secondDateCalendar)){

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
            }

            else
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }

        returnVisitDate.getButton().setEnabled(true);
        formDate.getButton().setEnabled(true);
    }


    @Override
    public void resetViews() {
        super.resetViews();

        if(App.getLocation().equals("IBEX-KHI")){
            weight.setVisibility(View.GONE);
        }

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        medicationDiscontinueReason.setVisibility(View.GONE);
        medicationDiscontinueDuration.setVisibility(View.GONE);
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
        otherSideEffects.setVisibility(View.GONE);
        rifapentineAvailable.setVisibility(View.GONE);
        treatmentInterruptedReason.setVisibility(View.GONE);
        otherAncillaryDrugs.setVisibility(View.GONE);
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
        treatmentPlan.setVisibility(View.GONE);
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

        if(!autoFill) {
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
                    String weight = serverService.getLatestObsValue(App.getPatientId(),"WEIGHT (KG)");
                    String petRegimen1 = serverService.getLatestObsValue(App.getPatientId(),"POST-EXPOSURE TREATMENT REGIMEN");

                    String isonoazidDose = "";
                    String rifapentineDose = "";
                    String levofloxacinDose = "";
                    String ethionamideDose = "";
                    String ethambutolDose = "";
                    String moxifloxacilinDose = "";


                    if (petRegimen1 == null)
                        petRegimen1 = "";
                    else {
                        result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen1);

                        if(petRegimen1.equals("ISONIAZID PROPHYLAXIS"))
                            isonoazidDose = serverService.getLatestObsValue(App.getPatientId(), "ISONIAZID DOSE");
                        else if(petRegimen1.equals("ISONIAZID AND RIFAPENTINE")){
                            isonoazidDose = serverService.getLatestObsValue(App.getPatientId(), "ISONIAZID DOSE");
                            rifapentineDose = serverService.getLatestObsValue(App.getPatientId(), "RIFAPENTINE DOSE");
                        }
                        else if(petRegimen1.equals("LEVOFLOXACIN AND ETHIONAMIDE")){
                            levofloxacinDose = serverService.getLatestObsValue(App.getPatientId(), "LEVOFLOXACIN DOSE");
                            ethionamideDose = serverService.getLatestObsValue(App.getPatientId(), "ETHIONAMIDE DOSE");
                        }
                        else if(petRegimen1.equals("LEVOFLOXACIN AND ETHAMBUTOL")){
                            levofloxacinDose = serverService.getLatestObsValue(App.getPatientId(), "LEVOFLOXACIN DOSE");
                            ethambutolDose = serverService.getLatestObsValue(App.getPatientId(), "ETHAMBUTOL DOSE");
                        }
                        else if(petRegimen1.equals("LEVOFLOXACIN AND MOXIFLOXACILIN")){
                            levofloxacinDose = serverService.getLatestObsValue(App.getPatientId(), "LEVOFLOXACIN DOSE");
                            moxifloxacilinDose = serverService.getLatestObsValue(App.getPatientId(), "MOXIFLOXACILIN DOSE");
                        }
                        else if(petRegimen1.equals("ETHIONAMIDE AND ETHAMBUTOL")){
                            ethionamideDose = serverService.getLatestObsValue(App.getPatientId(), "ETHIONAMIDE DOSE");
                            ethambutolDose = serverService.getLatestObsValue(App.getPatientId(), "ETHAMBUTOL DOSE");
                        }
                        else if(petRegimen1.equals("ETHIONAMIDE AND MOXIFLOXACILIN")){
                            ethionamideDose = serverService.getLatestObsValue(App.getPatientId(), "ETHIONAMIDE DOSE");
                            moxifloxacilinDose = serverService.getLatestObsValue(App.getPatientId(), "MOXIFLOXACILIN DOSE");
                        }
                        else if(petRegimen1.equals("MOXIFLOXACILIN AND ETHAMBUTOL")){
                            ethambutolDose = serverService.getLatestObsValue(App.getPatientId(), "ETHAMBUTOL DOSE");
                            moxifloxacilinDose = serverService.getLatestObsValue(App.getPatientId(), "MOXIFLOXACILIN DOSE");
                        }
                    }

                    String treatmentPlan = serverService.getLatestObsValue(App.getPatientId(),"TREATMENT PLAN");
                    String regimen = serverService.getLatestObsValue(App.getPatientId(), "REGIMEN");

                    String paedIntensiveDose = "";
                    String paedContinuousDose = "";

                    String intensiveCurrentTabletRHZ = "";
                    String intensiveCurrentTabletE = "";
                    String intensiveNewTabletE = "";
                    String intensiveNewTabletRHZ = "";
                    String intensiveAdultTabletRHZE = "";

                    String continuousCurrentTabletRH = "";
                    String continuousCurrentTabletE = "";
                    String continuousNewTabletRH = "";
                    String continuousNewTabletE = "";
                    String continuousAdultTabletRH = "";
                    String continuousAdultTabletRHE = "";

                    if(treatmentPlan == null){
                        treatmentPlan = "";
                        regimen = "";
                    } else if(treatmentPlan.equals("INTENSIVE PHASE")){

                        paedIntensiveDose = serverService.getLatestObsValue(App.getPatientId(), "PAEDIATRIC DOSE COMBINATION");
                        intensiveCurrentTabletRHZ = serverService.getLatestObsValue(App.getPatientId(), "CURRENT FORMULATION OF TABLETS OF RHZ");
                        intensiveCurrentTabletE = serverService.getLatestObsValue(App.getPatientId(), "CURRENT FORMULATION OF TABLETS OF  E");
                        intensiveNewTabletE = serverService.getLatestObsValue(App.getPatientId(), "NEW FORMULATION OF TABLETS OF E");
                        intensiveNewTabletRHZ = serverService.getLatestObsValue(App.getPatientId(), "NEW FORMULATION OF TABLETS OF RHZ");
                        intensiveAdultTabletRHZE = serverService.getLatestObsValue(App.getPatientId(), "ADULT FORMULATION OF TABLETS OF RHZE");

                    } else if (treatmentPlan.equals("CONTINUE REGIMEN")){

                        paedContinuousDose = serverService.getLatestObsValue(App.getPatientId(), "PAEDIATRIC FIXED DOSE COMBINATION FOR CONTINUATION PHASE");
                        continuousCurrentTabletRH = serverService.getLatestObsValue(App.getPatientId(), "CURRENT FORMULATION OF TABLETS OF RH");
                        continuousCurrentTabletE = serverService.getLatestObsValue(App.getPatientId(), "CURRENT FORMULATION OF TABLETS OF E FOR CONTINUATION PHASE");
                        continuousNewTabletRH = serverService.getLatestObsValue(App.getPatientId(), "NEW FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE");
                        continuousNewTabletE = serverService.getLatestObsValue(App.getPatientId(), "NEW FORMULATION OF TABLET OF E FOR CONTINUATION PHASE");
                        continuousAdultTabletRH = serverService.getLatestObsValue(App.getPatientId(), "ADULT FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE");
                        continuousAdultTabletRHE = serverService.getLatestObsValue(App.getPatientId(), "ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE");

                    } else {
                        treatmentPlan = "";
                        regimen = "";
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

                    if (ethambutolDose == null)
                        ethambutolDose = "";
                    ethambutolDose = ethambutolDose.replace(".0", "");
                    result.put("ETHAMBUTOL DOSE", ethambutolDose);

                    if (moxifloxacilinDose == null)
                        moxifloxacilinDose = "";
                    moxifloxacilinDose = moxifloxacilinDose.replace(".0", "");
                    result.put("MOXIFLOXACILIN DOSE", moxifloxacilinDose);

                    if(treatmentPlan == null)
                        treatmentPlan = "";
                    result.put("TREATMENT PLAN", treatmentPlan);

                    if(regimen == null)
                        regimen = "";
                    result.put("REGIMEN", regimen);

                    if(paedIntensiveDose == null)
                        paedIntensiveDose = "";
                    result.put("PAEDIATRIC DOSE COMBINATION", paedIntensiveDose);

                    if(intensiveCurrentTabletRHZ == null)
                        intensiveCurrentTabletRHZ = "";
                    result.put("CURRENT FORMULATION OF TABLETS OF RHZ", intensiveCurrentTabletRHZ);

                    if(intensiveCurrentTabletE == null)
                        intensiveCurrentTabletE = "";
                    result.put("CURRENT FORMULATION OF TABLETS OF  E", intensiveCurrentTabletE);

                    if(intensiveNewTabletE == null)
                        intensiveNewTabletE = "";
                    result.put("NEW FORMULATION OF TABLETS OF E", intensiveNewTabletE);

                    if(intensiveNewTabletRHZ == null)
                        intensiveNewTabletRHZ = "";
                    result.put("NEW FORMULATION OF TABLETS OF RHZ", intensiveNewTabletRHZ);

                    if(intensiveAdultTabletRHZE == null)
                        intensiveAdultTabletRHZE = "";
                    result.put("ADULT FORMULATION OF TABLETS OF RHZE", intensiveAdultTabletRHZE);

                    if(paedContinuousDose == null)
                        paedContinuousDose = "";
                    result.put("PAEDIATRIC FIXED DOSE COMBINATION FOR CONTINUATION PHASE", paedContinuousDose);

                    if(continuousCurrentTabletRH == null)
                        continuousCurrentTabletRH = "";
                    result.put("CURRENT FORMULATION OF TABLETS OF RH", continuousCurrentTabletRH);

                    if(continuousCurrentTabletE == null)
                        continuousCurrentTabletE = "";
                    result.put("CURRENT FORMULATION OF TABLETS OF E FOR CONTINUATION PHASE", continuousCurrentTabletE);

                    if(continuousNewTabletRH == null)
                        continuousNewTabletRH = "";
                    result.put("NEW FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE", continuousNewTabletRH);

                    if(continuousNewTabletE == null)
                        continuousNewTabletE = "";
                    result.put("NEW FORMULATION OF TABLET OF E FOR CONTINUATION PHASE", continuousNewTabletE);

                    if(continuousAdultTabletRH == null)
                        continuousAdultTabletRH = "";
                    result.put("ADULT FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE", continuousAdultTabletRH);

                    if(continuousAdultTabletRHE == null)
                        continuousAdultTabletRHE = "";
                    result.put("ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE", continuousAdultTabletRHE);

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

                    weight.getEditText().setText(result.get("WEIGHT (KG)"));

                    if( result.get("POST-EXPOSURE TREATMENT REGIMEN") != null) {
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
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_levofloxacin_ethambutol)) && result.get("POST-EXPOSURE TREATMENT REGIMEN").equals("LEVOFLOXACIN AND ETHAMBUTOL")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_levofloxacin_moxifloxacilin)) && result.get("POST-EXPOSURE TREATMENT REGIMEN").equals("LEVOFLOXACIN AND MOXIFLOXACILIN")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_ethionamide_ethambutol)) && result.get("POST-EXPOSURE TREATMENT REGIMEN").equals("ETHIONAMIDE AND ETHAMBUTOL")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_ethionamide_moxifloxacilin)) && result.get("POST-EXPOSURE TREATMENT REGIMEN").equals("ETHIONAMIDE AND MOXIFLOXACILIN")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_moxifloxacilin_ethambutol)) && result.get("POST-EXPOSURE TREATMENT REGIMEN").equals("MOXIFLOXACILIN AND ETHAMBUTOL")) {
                                rb.setChecked(true);
                                break;
                            }

                        }
                    }

                    if(result.get("ISONIAZID DOSE") != null)
                        isoniazidDose.getEditText().setText(result.get("ISONIAZID DOSE"));
                    if(result.get("RIFAPENTINE DOSE") != null)
                        rifapentineDose.getEditText().setText(result.get("RIFAPENTINE DOSE"));
                    if(result.get("LEVOFLOXACIN DOSE") != null)
                        levofloxacinDose.getEditText().setText(result.get("LEVOFLOXACIN DOSE"));
                    if(result.get("LEVOFLOXACIN DOSE") != null)
                        ethionamideDose.getEditText().setText(result.get("LEVOFLOXACIN DOSE"));
                    if(result.get("ETHAMBUTOL DOSE") != null)
                        ethambutolDose.getEditText().setText(result.get("ETHAMBUTOL DOSE"));
                    if(result.get("MOXIFLOXACILIN DOSE") != null)
                        moxifloxacilinDose.getEditText().setText(result.get("MOXIFLOXACILIN DOSE"));

                    rifapentineAvailable.setVisibility(View.GONE);
                    isoniazidDose.setVisibility(View.GONE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.GONE);
                    ethionamideDose.setVisibility(View.GONE);
                    ethambutolDose.setVisibility(View.GONE);
                    moxifloxacilinDose.setVisibility(View.GONE);

                    for (RadioButton rb : treatmentPlan.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_intensive_phase)) && result.get("TREATMENT PLAN").equals("INTENSIVE PHASE")) {
                            rb.setChecked(true);

                            for (RadioButton rb1 : intensivePhaseRegimen.getRadioGroup().getButtons()) {

                                if (rb1.getText().equals(getResources().getString(R.string.ctb_rhze)) && result.get("REGIMEN").equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL")) {
                                    rb1.setChecked(true);
                                } else if (rb1.getText().equals(getResources().getString(R.string.ctb_rhz)) && result.get("REGIMEN").equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE")) {
                                    rb1.setChecked(true);
                                }

                            }

                            String value = result.get("PAEDIATRIC DOSE COMBINATION").equals("CURRENT FORMULATION") ? getResources().getString(R.string.ctb_current_formulation) :
                                    (result.get("PAEDIATRIC DOSE COMBINATION").equals("NEW FORMULATION") ? getResources().getString(R.string.ctb_new_formulation) :
                                            getResources().getString(R.string.ctb_adult_formulation));

                            typeFixedDosePrescribedIntensive.getSpinner().selectValue(value);

                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_continuation_phase)) && result.get("TREATMENT PLAN").equals("CONTINUE REGIMEN")) {
                            rb.setChecked(true);

                            for (RadioButton rb1 : continuationPhaseRegimen.getRadioGroup().getButtons()) {

                                if (rb1.getText().equals(getResources().getString(R.string.ctb_rh)) && result.get("REGIMEN").equals("RIFAMPICIN AND ISONIAZID")) {
                                    rb1.setChecked(true);
                                } else if (rb1.getText().equals(getResources().getString(R.string.ctb_rhe)) && result.get("REGIMEN").equals("RIFAMPICIN ISONIAZID AND ETHAMBUTOL")) {
                                    rb1.setChecked(true);
                                }

                            }

                            String value = result.get("PAEDIATRIC FIXED DOSE COMBINATION FOR CONTINUATION PHASE").equals("CURRENT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_current_formulation_continuation) :
                                    (result.get("PAEDIATRIC FIXED DOSE COMBINATION FOR CONTINUATION PHASE").equals("NEW FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_new_formulation_continuation) :
                                            (result.get("PAEDIATRIC FIXED DOSE COMBINATION FOR CONTINUATION PHASE").equals("ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_adult_formulation_continuation_rhe) :
                                                    (result.get("PAEDIATRIC FIXED DOSE COMBINATION FOR CONTINUATION PHASE").equals("ADULT FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE UNDER 25KG") ? getResources().getString(R.string.ctb_adult_formulation_rh_20_to_25) :
                                                            getResources().getString(R.string.ctb_adult_formulation_continuation_rh)
                                                    )));

                            typeFixedDosePrescribedContinuation.getSpinner().selectValue(value);
                            break;
                        }
                    }

                    if(result.get("CURRENT FORMULATION OF TABLETS OF RHZ") != null) {
                        for (RadioButton rb : currentTabletsofRHZ.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && result.get("CURRENT FORMULATION OF TABLETS OF RHZ").equals("1")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && result.get("CURRENT FORMULATION OF TABLETS OF RHZ").equals("2")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && result.get("CURRENT FORMULATION OF TABLETS OF RHZ").equals("3")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && result.get("CURRENT FORMULATION OF TABLETS OF RHZ").equals("4")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_5)) && result.get("CURRENT FORMULATION OF TABLETS OF RHZ").equals("5")) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }

                    if(result.get("CURRENT FORMULATION OF TABLETS OF E") != null) {
                        for (RadioButton rb : currentTabletsofE.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && result.get("CURRENT FORMULATION OF TABLETS OF E").equals("1")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && result.get("CURRENT FORMULATION OF TABLETS OF E").equals("2")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && result.get("CURRENT FORMULATION OF TABLETS OF E").equals("3")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && result.get("CURRENT FORMULATION OF TABLETS OF E").equals("4")) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }

                    if(result.get("CURRENT FORMULATION OF TABLETS OF E") != null) {
                        for (RadioButton rb : newTabletsofE.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && result.get("NEW FORMULATION OF TABLETS OF E").equals("1")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && result.get("NEW FORMULATION OF TABLETS OF E").equals("2")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && result.get("NEW FORMULATION OF TABLETS OF E").equals("3")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && result.get("NEW FORMULATION OF TABLETS OF E").equals("4")) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }

                    if(result.get("NEW FORMULATION OF TABLETS OF RHZ") != null) {
                        for (RadioButton rb : newTabletsofRHZ.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && result.get("NEW FORMULATION OF TABLETS OF RHZ").equals("1")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && result.get("NEW FORMULATION OF TABLETS OF RHZ").equals("2")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && result.get("NEW FORMULATION OF TABLETS OF RHZ").equals("3")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && result.get("NEW FORMULATION OF TABLETS OF RHZ").equals("4")) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }

                    if(result.get("ADULT FORMULATION OF TABLETS OF RHZE") != null) {
                        for (RadioButton rb : adultFormulationofHRZE.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && result.get("ADULT FORMULATION OF TABLETS OF RHZE").equals("1")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && result.get("ADULT FORMULATION OF TABLETS OF RHZE").equals("2")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && result.get("ADULT FORMULATION OF TABLETS OF RHZE").equals("3")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && result.get("ADULT FORMULATION OF TABLETS OF RHZE").equals("4")) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }

                    if (result.get("CURRENT FORMULATION OF TABLETS OF RH") != null) {
                        for (RadioButton rb : currentTabletsOfContinuationRH.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && result.get("CURRENT FORMULATION OF TABLETS OF RH").equals("1")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && result.get("CURRENT FORMULATION OF TABLETS OF RH").equals("2")) {
                                rb.setChecked(true);
                                break;
                            }else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && result.get("CURRENT FORMULATION OF TABLETS OF RH").equals("3")) {
                                rb.setChecked(true);
                                break;
                            }else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && result.get("CURRENT FORMULATION OF TABLETS OF RH").equals("4")) {
                                rb.setChecked(true);
                                break;
                            }
                            else if (rb.getText().equals(getResources().getString(R.string.ctb_5)) && result.get("CURRENT FORMULATION OF TABLETS OF RH").equals("5")) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }

                    if (result.get("CURRENT FORMULATION OF TABLETS OF E FOR CONTINUATION PHASE") != null) {
                        for (RadioButton rb : currentTabletsOfContinuationE.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && result.get("CURRENT FORMULATION OF TABLETS OF E FOR CONTINUATION PHASE").equals("1")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && result.get("CURRENT FORMULATION OF TABLETS OF E FOR CONTINUATION PHASE").equals("2")) {
                                rb.setChecked(true);
                                break;
                            }else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && result.get("CURRENT FORMULATION OF TABLETS OF E FOR CONTINUATION PHASE").equals("3")) {
                                rb.setChecked(true);
                                break;
                            }else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && result.get("CURRENT FORMULATION OF TABLETS OF E FOR CONTINUATION PHASE").equals("4")) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }

                    if (result.get("NEW FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE") != null) {
                        for (RadioButton rb : newTabletsOfContinuationRH.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && result.get("NEW FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE").equals("1")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && result.get("NEW FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE").equals("2")) {
                                rb.setChecked(true);
                                break;
                            }else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && result.get("NEW FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE").equals("3")) {
                                rb.setChecked(true);
                                break;
                            }else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && result.get("NEW FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE").equals("4")) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }

                    if (result.get("NEW FORMULATION OF TABLET OF E FOR CONTINUATION PHASE") != null) {
                        for (RadioButton rb : newTabletsOfContinuationE.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && result.get("NEW FORMULATION OF TABLET OF E FOR CONTINUATION PHASE").equals("1")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && result.get("NEW FORMULATION OF TABLET OF E FOR CONTINUATION PHASE").equals("2")) {
                                rb.setChecked(true);
                                break;
                            }else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && result.get("NEW FORMULATION OF TABLET OF E FOR CONTINUATION PHASE").equals("3")) {
                                rb.setChecked(true);
                                break;
                            }else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && result.get("NEW FORMULATION OF TABLET OF E FOR CONTINUATION PHASE").equals("4")) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }

                    if (result.get("ADULT FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE") != null) {
                        for (RadioButton rb : adultFormulationOfContinuationRH.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && result.get("ADULT FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE").equals("1")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_1_and_half)) && result.get("ADULT FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE").equals("1.5")) {
                                rb.setChecked(true);
                                break;
                            }else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && result.get("ADULT FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE").equals("2")) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }

                    if (result.get("ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") != null) {
                        for (RadioButton rb : adultFormulationOfContinuationRHE.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && result.get("ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE").equals("2")) {
                                rb.setChecked(true);
                                break;
                            }else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && result.get("ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE").equals("3")) {
                                rb.setChecked(true);
                                break;
                            }else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && result.get("ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE").equals("4")) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }

                    treatmentPlan.setVisibility(View.GONE);
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

                }
            };
            autopopulateFormTask.execute("");
        }

    }


    @Override
    public boolean validate() {

        View view = null;
        Boolean error = false;

        if (App.get(newInstruction).trim().isEmpty()) {
            newInstruction.getEditText().setError(getString(R.string.empty_field));
            newInstruction.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        } else {
            newInstruction.getEditText().clearFocus();
            newInstruction.getEditText().setError(null);
        }

        if (otherAncillaryDrugs.getVisibility() == View.VISIBLE && App.get(otherAncillaryDrugs).isEmpty()) {
            otherAncillaryDrugs.getEditText().setError(getString(R.string.empty_field));
            otherAncillaryDrugs.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        } else {
            otherAncillaryDrugs.getEditText().clearFocus();
            otherAncillaryDrugs.getEditText().setError(null);
        }

        if (treatmentInterruptedReason.getVisibility() == View.VISIBLE && App.get(treatmentInterruptedReason).isEmpty()) {
            treatmentInterruptedReason.getEditText().setError(getString(R.string.empty_field));
            treatmentInterruptedReason.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        } else {
            treatmentInterruptedReason.getEditText().clearFocus();
            treatmentInterruptedReason.getEditText().setError(null);
        }

        if (ancillaryDrugDuration.getVisibility() == View.VISIBLE && App.get(ancillaryDrugDuration).isEmpty()) {
            ancillaryDrugDuration.getEditText().setError(getString(R.string.empty_field));
            ancillaryDrugDuration.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        } else {

            if (ancillaryDrugDuration.getVisibility() == View.VISIBLE) {
                int val = Integer.parseInt(App.get(ancillaryDrugDuration));
                if(val > 150) {
                    ancillaryDrugDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                    ancillaryDrugDuration.getEditText().requestFocus();
                    error = true;
                    gotoLastPage();
                    view = null;
                }
                else {
                    ancillaryDrugDuration.getEditText().setError(null);
                    ancillaryDrugDuration.getEditText().requestFocus();
                }
            } else {
                ancillaryDrugDuration.getEditText().setError(null);
                ancillaryDrugDuration.getEditText().clearFocus();
            }

        }

        if (ancillaryDrugs.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : ancillaryDrugs.getCheckedBoxes()) {
                if (cb.isChecked())
                    flag = true;
            }
            if (!flag) {
                ancillaryDrugs.getQuestionView().setError(getString(R.string.empty_field));
                ancillaryDrugs.getQuestionView().requestFocus();
                gotoLastPage();
                view = ancillaryDrugs;
                error = true;
            } else {
                ancillaryDrugs.getQuestionView().clearFocus();
                ancillaryDrugs.getQuestionView().setError(null);
            }
        }

        if (petRegimen.getVisibility() == View.VISIBLE && App.get(petRegimen).isEmpty()) {
            petRegimen.getQuestionView().setError(getString(R.string.empty_field));
            petRegimen.getQuestionView().requestFocus();
            view = petRegimen;
            gotoLastPage();
            error = true;
        }  else {
            petRegimen.getQuestionView().clearFocus();
            petRegimen.getQuestionView().setError(null);
        }

        if(moxifloxacilinDose.getVisibility() == View.VISIBLE) {
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

        if(ethambutolDose.getVisibility() == View.VISIBLE) {
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

        if(ethionamideDose.getVisibility() == View.VISIBLE) {
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
        if(levofloxacinDose.getVisibility() == View.VISIBLE) {
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
        if(rifapentineDose.getVisibility() == View.VISIBLE) {
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
                if(App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy))) {
                    if (dose > 300) {
                        isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded_300));
                        isoniazidDose.getEditText().requestFocus();
                        view = null;
                        error = true;
                    }else{
                        isoniazidDose.getEditText().setError(null);
                        isoniazidDose.getEditText().clearFocus();
                    }
                }
                else{
                    if (dose > 1000) {
                        isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded_1000));
                        isoniazidDose.getEditText().requestFocus();
                        view = null;
                        error = true;
                    }else{
                        isoniazidDose.getEditText().setError(null);
                        isoniazidDose.getEditText().clearFocus();
                    }
                }
            }
        }

        if (medicationDiscontinueReason.getVisibility() == View.VISIBLE && App.get(medicationDiscontinueReason).isEmpty()) {
            medicationDiscontinueReason.getEditText().setError(getString(R.string.empty_field));
            medicationDiscontinueReason.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        }else {
            medicationDiscontinueReason.getEditText().clearFocus();
            medicationDiscontinueReason.getEditText().setError(null);
        }

        if (medicationDiscontinueDuration.getVisibility() == View.VISIBLE && App.get(medicationDiscontinueDuration).isEmpty()) {
            medicationDiscontinueDuration.getEditText().setError(getString(R.string.empty_field));
            medicationDiscontinueDuration.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        }else {
            if (medicationDiscontinueDuration.getVisibility() == View.VISIBLE) {
                int val = Integer.parseInt(App.get(medicationDiscontinueDuration));
                if(val > 150) {
                    medicationDiscontinueDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                    medicationDiscontinueDuration.getEditText().requestFocus();
                    error = true;
                    gotoLastPage();
                    view = null;
                }
                else {
                    medicationDiscontinueDuration.getEditText().setError(null);
                    medicationDiscontinueDuration.getEditText().requestFocus();
                }
            } else {
                medicationDiscontinueDuration.getEditText().setError(null);
                medicationDiscontinueDuration.getEditText().clearFocus();
            }
        }

        if(treatmentPlan.getVisibility()==View.VISIBLE) {
            if ((App.get(treatmentPlan).isEmpty())) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                treatmentPlan.getQuestionView().setError(getString(R.string.empty_field));
                treatmentPlan.getRadioGroup().requestFocus();
                error = true;
            }
        }
        if(intensivePhaseRegimen.getVisibility()==View.VISIBLE) {
            if ((App.get(intensivePhaseRegimen).isEmpty())) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                intensivePhaseRegimen.getQuestionView().setError(getString(R.string.empty_field));
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

        Boolean flag = true;
        if (App.get(patientReferred).isEmpty()) {
            patientReferred.getQuestionView().setError(getString(R.string.empty_field));
            patientReferred.getQuestionView().requestFocus();
            view = patientReferred;
            error = true;
            gotoLastPage();
        } else {
            patientReferred.getQuestionView().setError(null);
            if(App.get(patientReferred).equals(getString(R.string.yes))){

                for (CheckBox cb : referredTo.getCheckedBoxes()) {
                    if (cb.isChecked()) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    referredTo.getQuestionView().setError(getString(R.string.empty_field));
                    referredTo.getQuestionView().requestFocus();
                    view = referredTo;
                    gotoLastPage();
                    error = true;
                } else {

                    for (CheckBox cb : referredTo.getCheckedBoxes()) {

                        if (cb.isChecked() && ( cb.getText().equals(getString(R.string.counselor)) || cb.getText().equals(getString(R.string.psychologist)) )) {
                            flag = true;
                            for (CheckBox cb1 : referalReasonPsychologist.getCheckedBoxes()) {
                                if (cb1.isChecked()) {
                                    flag = false;
                                    if(cb1.getText().equals(getString(R.string.other)) && App.get(otherReferalReasonPsychologist).equals("")){
                                        otherReferalReasonPsychologist.getQuestionView().setError(getString(R.string.empty_field));
                                        otherReferalReasonPsychologist.getQuestionView().requestFocus();
                                        view = null;
                                        gotoLastPage();
                                        error = true;
                                    } else otherReferalReasonPsychologist.getQuestionView().setError(null);
                                }
                            }
                            if (flag) {
                                referalReasonPsychologist.getQuestionView().setError(getString(R.string.empty_field));
                                referalReasonPsychologist.getQuestionView().requestFocus();
                                view = referalReasonPsychologist;
                                gotoLastPage();
                                error = true;
                            } else
                                referalReasonPsychologist.getQuestionView().setError(null);

                        } else if (cb.isChecked() && ( cb.getText().equals(getString(R.string.site_supervisor)) || cb.getText().equals(getString(R.string.field_supervisor)) )) {
                            flag = true;
                            for (CheckBox cb1 : referalReasonSupervisor.getCheckedBoxes()) {
                                if (cb1.isChecked()) {
                                    flag = false;
                                    if(cb1.getText().equals(getString(R.string.other)) && App.get(otherReferalReasonSupervisor).equals("")){
                                        otherReferalReasonSupervisor.getQuestionView().setError(getString(R.string.empty_field));
                                        otherReferalReasonSupervisor.getQuestionView().requestFocus();
                                        view = null;
                                        gotoLastPage();
                                        error = true;
                                    } else otherReferalReasonSupervisor.getQuestionView().setError(null);
                                }
                            }
                            if (flag) {
                                referalReasonSupervisor.getQuestionView().setError(getString(R.string.empty_field));
                                referalReasonSupervisor.getQuestionView().requestFocus();
                                view = referalReasonSupervisor;
                                gotoLastPage();
                                error = true;
                            } else
                                referalReasonSupervisor.getQuestionView().setError(null);

                        } else if (cb.isChecked() && cb.getText().equals(getString(R.string.clinician))) {
                            flag = true;
                            for (CheckBox cb1 : referalReasonClinician.getCheckedBoxes()) {
                                if (cb1.isChecked()) {
                                    flag = false;
                                    if(cb1.getText().equals(getString(R.string.other)) && App.get(otherReferalReasonClinician).equals("")){
                                        otherReferalReasonClinician.getQuestionView().setError(getString(R.string.empty_field));
                                        otherReferalReasonClinician.getQuestionView().requestFocus();
                                        view = null;
                                        gotoLastPage();
                                        error = true;
                                    } else otherReferalReasonClinician.getQuestionView().setError(null);
                                }
                            }
                            if (flag) {
                                referalReasonClinician.getQuestionView().setError(getString(R.string.empty_field));
                                referalReasonClinician.getQuestionView().requestFocus();
                                view = referalReasonClinician;
                                gotoLastPage();
                                error = true;
                            } else
                                referalReasonClinician.getQuestionView().setError(null);

                        } else if (cb.isChecked() && cb.getText().equals(getString(R.string.call_center))) {
                            flag = true;
                            for (CheckBox cb1 : referalReasonCallCenter.getCheckedBoxes()) {
                                if (cb1.isChecked()) {
                                    flag = false;
                                    if(cb1.getText().equals(getString(R.string.other)) && App.get(otherReferalReasonCallCenter).equals("")){
                                        otherReferalReasonCallCenter.getQuestionView().setError(getString(R.string.empty_field));
                                        otherReferalReasonCallCenter.getQuestionView().requestFocus();
                                        view = null;
                                        gotoLastPage();
                                        error = true;
                                    } else otherReferalReasonCallCenter.getQuestionView().setError(null);
                                }
                            }
                            if (flag) {
                                referalReasonCallCenter.getQuestionView().setError(getString(R.string.empty_field));
                                referalReasonCallCenter.getQuestionView().requestFocus();
                                view = referalReasonCallCenter;
                                gotoLastPage();
                                error = true;
                            } else
                                referalReasonCallCenter.getQuestionView().setError(null);

                        }
                    }

                }

            }

        }

        /*if (App.get(weight).isEmpty()) {
            weight.getEditText().setError(getString(R.string.empty_field));
            weight.getEditText().requestFocus();
            gotoFirstPage();
            view = null;
            error = true;
        }*/

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
                                        newInstruction.clearFocus();
                                        ancillaryDrugDuration.clearFocus();
                                        isoniazidDose.clearFocus();
                                        rifapentineDose.clearFocus();
                                        levofloxacinDose.clearFocus();
                                        ethionamideDose.clearFocus();
                                        medicationDiscontinueDuration.clearFocus();
                                        medicationDiscontinueReason.clearFocus();
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

        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});
        observations.add(new String[]{"WEIGHT (KG)", App.get(weight)});

        String adverseEventString = "";
        for(CheckBox cb : symptoms.getCheckedBoxes()){
            if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_dizziness)))
                adverseEventString = adverseEventString + "DIZZINESS AND GIDDINESS" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_nausea_vomiting)))
                adverseEventString = adverseEventString + "NAUSEA AND VOMITING" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_abdominal_pain)))
                adverseEventString = adverseEventString + "ABDOMINAL PAIN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_reduced_appetite)))
                adverseEventString = adverseEventString + "LOSS OF APPETITE" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_jaundice)))
                adverseEventString = adverseEventString + "JAUNDICE" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_rash)))
                adverseEventString = adverseEventString + "RASH" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_tendon_pain)))
                adverseEventString = adverseEventString + "TENDON PAIN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_eye_problems)))
                adverseEventString = adverseEventString + "VISION PROBLEM" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_hypersensitivity_reaction)))
                adverseEventString = adverseEventString + "HYPERSENSITIVITY REACTION" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_body_fluid_discoloration)))
                adverseEventString = adverseEventString + "DISCOLORATION OF BODY FLUID" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_diarrohea)))
                adverseEventString = adverseEventString + "DIARRHEA" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_muscle_pain)))
                adverseEventString = adverseEventString + "MUSCLE PAIN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other)))
                adverseEventString = adverseEventString + "OTHER ADVERSE EVENT" + " ; ";
        }
        observations.add(new String[]{"ADVERSE EVENTS", adverseEventString});

        if(otherSideEffects.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER ADVERSE EVENT", App.get(otherSideEffects)});
        observations.add(new String[]{"COMPLAINTS CONSISTENT WITH DRUG SIDE EFFECTS", App.get(sideeffectsConsistent).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        observations.add(new String[]{"SEVERITY OF ADVERSE REACTION", App.get(severity).equals(getResources().getString(R.string.pet_mild)) ? "MILD" :
                (App.get(severity).equals(getResources().getString(R.string.pet_moderate)) ? "MODERATE" : "SEVERE")});

        String actionPlanString = "";
        for (CheckBox cb : actionPlan.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_continue_medication_greater_adherence)))
                actionPlanString = actionPlanString + "CONTINUE MEDICATION (HIGH ADHERENCE)" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_continue_medication_less_adherence)))
                actionPlanString = actionPlanString + "CONTINUE MEDICATION (LOW ADHERENCE)" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_discontinue_medication)))
                actionPlanString = actionPlanString + "DISCONTINUE MEDICATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_begin_cxr_protocol)))
                actionPlanString = actionPlanString + "BEGIN CLINICAL MONITORING PROTOCOL" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_new_medication)))
                actionPlanString = actionPlanString + "GIVE NEW MEDICATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_seek_expert_consultation)))
                actionPlanString = actionPlanString + "SEEK EXPERT CONSULTATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_change_drug_dosage)))
                actionPlanString = actionPlanString + "CHANGE DRUG DOSAGE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_ancillary_drug)))
                actionPlanString = actionPlanString + "GIVE ANCILLARY DRUG" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_treatment_interrupted)))
                actionPlanString = actionPlanString + "PLANNED TREATMENT INTERRUPTION" + " ; ";
        }
        observations.add(new String[]{"ACTION PLAN", actionPlanString});

        if (treatmentInterruptedReason.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT INTERRUPTION REASON", App.get(treatmentInterruptedReason)});
        if (medicationDiscontinueReason.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON TO DISCONTINUE MEDICATION", App.get(medicationDiscontinueReason)});
        if (medicationDiscontinueDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DURATION OF DISCONTINUATION IN DAYS", App.get(medicationDiscontinueDuration)});

        if (petRegimen.getVisibility() == View.VISIBLE)
            if (petRegimen.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"POST-EXPOSURE TREATMENT REGIMEN", App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy)) ? "ISONIAZID PROPHYLAXIS" :
                        (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine)) ? "ISONIAZID AND RIFAPENTINE" :
                                (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethionamide)) ? "LEVOFLOXACIN AND ETHIONAMIDE" :
                                        (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethambutol)) ? "LEVOFLOXACIN AND ETHAMBUTOL" :
                                                (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_moxifloxacilin)) ? "LEVOFLOXACIN AND MOXIFLOXACILIN" :
                                                        (App.get(petRegimen).equals(getResources().getString(R.string.pet_ethionamide_ethambutol)) ? "ETHIONAMIDE AND ETHAMBUTOL" :
                                                                (App.get(petRegimen).equals(getResources().getString(R.string.pet_ethionamide_moxifloxacilin)) ? "ETHIONAMIDE AND MOXIFLOXACILIN" : "MOXIFLOXACILIN AND ETHAMBUTOL"))))))});
        if (isoniazidDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ISONIAZID DOSE", App.get(isoniazidDose)});
        if (rifapentineDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"RIFAPENTINE DOSE", App.get(rifapentineDose)});
        if (levofloxacinDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"LEVOFLOXACIN DOSE", App.get(levofloxacinDose)});
        if (ethionamideDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ETHIONAMIDE DOSE", App.get(ethionamideDose)});
        if (ethambutolDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ETHAMBUTOL DOSE", App.get(ethambutolDose)});
        if (moxifloxacilinDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"MOXIFLOXACILIN DOSE", App.get(moxifloxacilinDose)});

        if(treatmentPlan.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"TREATMENT PLAN", App.get(treatmentPlan).equals(getResources().getString(R.string.ctb_intensive_phase)) ? "INTENSIVE PHASE" :
                    "CONTINUE REGIMEN"});
        }

        if(intensivePhaseRegimen.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"REGIMEN", App.get(intensivePhaseRegimen).equals(getResources().getString(R.string.ctb_rhze)) ? "RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL" : "RIFAMPICIN/ISONIAZID/PYRAZINAMIDE"});
        }

        if(typeFixedDosePrescribedIntensive.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"PAEDIATRIC DOSE COMBINATION", App.get(typeFixedDosePrescribedIntensive).equals(getResources().getString(R.string.ctb_current_formulation)) ? "CURRENT FORMULATION" :
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
            observations.add(new String[]{"REGIMEN", App.get(continuationPhaseRegimen).equals(getResources().getString(R.string.ctb_rh)) ? "RIFAMPICIN AND ISONIAZID" : "RIFAMPICIN ISONIAZID AND ETHAMBUTOL"});
        }
        if(typeFixedDosePrescribedContinuation.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"PAEDIATRIC FIXED DOSE COMBINATION FOR CONTINUATION PHASE", App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_current_formulation_continuation)) ? "CURRENT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE" :
                    App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_new_formulation_continuation)) ? "NEW FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE" :
                            App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_adult_formulation_continuation_rhe)) ? "ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE" :
                                    App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_adult_formulation_rh_20_to_25)) ? "ADULT FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE UNDER 25KG"
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

        if (ancillaryDrugs.getVisibility() == View.VISIBLE) {
            String ancillaryDrugd = "";
            for (CheckBox cb : ancillaryDrugs.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.iron)))
                    ancillaryDrugd = ancillaryDrugd + "IRON" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.multivitamins)))
                    ancillaryDrugd = ancillaryDrugd + "MULTIVITAMIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.anthelmintic)))
                    ancillaryDrugd = ancillaryDrugd + "ANTHELMINTHIC" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pediasure)))
                    ancillaryDrugd = ancillaryDrugd + "PEDIASURE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.vitamin_b_complex)))
                    ancillaryDrugd = ancillaryDrugd + "VITAMIN B COMPLEX" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.calpol)))
                    ancillaryDrugd = ancillaryDrugd + "CALPOL" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.other)))
                    ancillaryDrugd = ancillaryDrugd + "OTHER" + " ; ";
            }
            observations.add(new String[]{"ANCILLARY DRUGS", ancillaryDrugd});
        }

        if(otherAncillaryDrugs.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER ANCILLARY DRUGS", App.get(otherAncillaryDrugs)});

        if (ancillaryDrugDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"MEDICATION DURATION", App.get(ancillaryDrugDuration)});
        observations.add(new String[]{"INSTRUCTIONS TO PATIENT AND/OR FAMILY", App.get(newInstruction)});
        observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDate(secondDateCalendar)});
        observations.add(new String[]{"CLINICIAN NOTES (TEXT)", App.get(clincianNote)});

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
                    id = serverService.saveFormLocally(formName, form, formDateCalendar,observations.toArray(new String[][]{}));

                String result = "";

                result = serverService.saveEncounterAndObservationTesting(formName, form, formDateCalendar, observations.toArray(new String[][]{}),id);
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
            showDateDialog(formDateCalendar,false,true, false);

        } else if (view == returnVisitDate.getButton()) {
            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowFutureDate", true);
            args.putBoolean("allowPastDate", false);
            args.putString("formDate", formDate.getButton().getText().toString());
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");*/
            returnVisitDate.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar,true,false, true);

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
            }else{
                adultFormulationofHRZE.setVisibility(View.GONE);
                newTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
            }
        }else if (spinner == typeFixedDosePrescribedContinuation.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_current_formulation_continuation))) {


                if (App.get(continuationPhaseRegimen).equals(getResources().getString(R.string.ctb_rhe))) {
                    currentTabletsOfContinuationE.setVisibility(View.VISIBLE);
                }
                currentTabletsOfContinuationRH.setVisibility(View.VISIBLE);

                newTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRHE.setVisibility(View.GONE);
            } else if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_new_formulation_continuation))) {
                if (App.get(continuationPhaseRegimen).equals(getResources().getString(R.string.ctb_rhe))) {
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
                if (App.get(continuationPhaseRegimen).equals(getResources().getString(R.string.ctb_rhe))) {
                    adultFormulationOfContinuationRHE.setVisibility(View.VISIBLE);
                }


                currentTabletsOfContinuationE.setVisibility(View.GONE);
                currentTabletsOfContinuationRH.setVisibility(View.GONE);
                newTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRH.setVisibility(View.GONE);
            }else{
                adultFormulationOfContinuationRHE.setVisibility(View.GONE);
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

        for (CheckBox cb : actionPlan.getCheckedBoxes()) {

            if (App.get(cb).equals(getResources().getString(R.string.pet_new_medication))) {
                if(cb.isChecked()){
                    String patientSourceString = serverService.getLatestObsValue(App.getPatientId(), "PATIENT SOURCE");
                    if(patientSourceString.equals("TUBERCULOSIS CONTACT")) {

                        for (RadioButton b : petRegimen.getRadioGroup().getButtons()) {
                            b.setClickable(true);
                        }
                        petRegimen.setVisibility(View.VISIBLE);
                        isoniazidDose.setVisibility(View.GONE);
                        rifapentineDose.setVisibility(View.GONE);
                        levofloxacinDose.setVisibility(View.GONE);
                        ethionamideDose.setVisibility(View.GONE);
                        moxifloxacilinDose.setVisibility(View.GONE);
                        ethambutolDose.setVisibility(View.GONE);
                        if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy)))
                            isoniazidDose.setVisibility(View.VISIBLE);
                        else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine))) {
                            isoniazidDose.setVisibility(View.VISIBLE);
                            rifapentineDose.setVisibility(View.VISIBLE);
                        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethionamide))) {
                            levofloxacinDose.setVisibility(View.VISIBLE);
                            ethionamideDose.setVisibility(View.VISIBLE);
                        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethambutol))) {
                            levofloxacinDose.setVisibility(View.VISIBLE);
                            ethambutolDose.setVisibility(View.VISIBLE);
                        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_moxifloxacilin))) {
                            levofloxacinDose.setVisibility(View.VISIBLE);
                            moxifloxacilinDose.setVisibility(View.VISIBLE);
                        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_ethionamide_ethambutol))) {
                            ethionamideDose.setVisibility(View.VISIBLE);
                            ethambutolDose.setVisibility(View.VISIBLE);
                        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_moxifloxacilin_ethambutol))) {
                            ethambutolDose.setVisibility(View.VISIBLE);
                            moxifloxacilinDose.setVisibility(View.VISIBLE);
                        }
                    } else {

                        for (RadioButton b : treatmentPlan.getRadioGroup().getButtons()) {
                            b.setClickable(true);
                        }
                        for (RadioButton b : intensivePhaseRegimen.getRadioGroup().getButtons()) {
                            b.setClickable(true);
                        }
                        for (RadioButton b : continuationPhaseRegimen.getRadioGroup().getButtons()) {
                            b.setClickable(true);
                        }

                        treatmentPlan.setVisibility(View.VISIBLE);
                        calculatePeadDosages();
                    }
                } else {
                    for (CheckBox cc : actionPlan.getCheckedBoxes()) {
                        if (App.get(cc).equals(getResources().getString(R.string.pet_change_drug_dosage))) {
                            if(!cc.isChecked()) {
                                petRegimen.setVisibility(View.GONE);
                                isoniazidDose.setVisibility(View.GONE);
                                rifapentineDose.setVisibility(View.GONE);
                                levofloxacinDose.setVisibility(View.GONE);
                                ethionamideDose.setVisibility(View.GONE);
                                rifapentineAvailable.setVisibility(View.GONE);
                                treatmentPlan.setVisibility(View.GONE);

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
                            }
                        }
                    }
                    for (RadioButton b : petRegimen.getRadioGroup().getButtons()) {
                        b.setClickable(false);
                    }
                    for (RadioButton b : treatmentPlan.getRadioGroup().getButtons()) {
                        b.setClickable(false);
                    }
                    for (RadioButton b : intensivePhaseRegimen.getRadioGroup().getButtons()) {
                        b.setClickable(false);
                    }
                    for (RadioButton b : continuationPhaseRegimen.getRadioGroup().getButtons()) {
                        b.setClickable(false);
                    }
                }

            }

            if (App.get(cb).equals(getResources().getString(R.string.pet_discontinue_medication))) {
                if (cb.isChecked()) {
                    medicationDiscontinueReason.setVisibility(View.VISIBLE);
                    medicationDiscontinueDuration.setVisibility(View.VISIBLE);
                } else {
                    medicationDiscontinueReason.setVisibility(View.GONE);
                    medicationDiscontinueDuration.setVisibility(View.GONE);
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

                    String patientSourceString = serverService.getLatestObsValue(App.getPatientId(), "PATIENT SOURCE");

                    if(patientSourceString.equals("TUBERCULOSIS CONTACT")) {

                        for (RadioButton b : petRegimen.getRadioGroup().getButtons()) {
                            b.setClickable(false);
                        }
                        for (CheckBox cc : actionPlan.getCheckedBoxes()) {
                            if (App.get(cc).equals(getResources().getString(R.string.pet_new_medication)) && cc.isChecked()) {
                                for (RadioButton b : petRegimen.getRadioGroup().getButtons()) {
                                    b.setClickable(true);
                                }
                            }
                        }

                        petRegimen.setVisibility(View.VISIBLE);
                        isoniazidDose.setVisibility(View.GONE);
                        rifapentineDose.setVisibility(View.GONE);
                        levofloxacinDose.setVisibility(View.GONE);
                        ethionamideDose.setVisibility(View.GONE);
                        moxifloxacilinDose.setVisibility(View.GONE);
                        ethambutolDose.setVisibility(View.GONE);
                        if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy)))
                            isoniazidDose.setVisibility(View.VISIBLE);
                        else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine))) {
                            isoniazidDose.setVisibility(View.VISIBLE);
                            rifapentineDose.setVisibility(View.VISIBLE);
                        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethionamide))) {
                            levofloxacinDose.setVisibility(View.VISIBLE);
                            ethionamideDose.setVisibility(View.VISIBLE);
                        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethambutol))) {
                            levofloxacinDose.setVisibility(View.VISIBLE);
                            ethambutolDose.setVisibility(View.VISIBLE);
                        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_moxifloxacilin))) {
                            levofloxacinDose.setVisibility(View.VISIBLE);
                            moxifloxacilinDose.setVisibility(View.VISIBLE);
                        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_ethionamide_ethambutol))) {
                            ethionamideDose.setVisibility(View.VISIBLE);
                            ethambutolDose.setVisibility(View.VISIBLE);
                        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_moxifloxacilin_ethambutol))) {
                            ethambutolDose.setVisibility(View.VISIBLE);
                            moxifloxacilinDose.setVisibility(View.VISIBLE);
                        }
                    } else {

                        for (RadioButton b : treatmentPlan.getRadioGroup().getButtons()) {
                            b.setClickable(false);
                        }
                        for (RadioButton b : intensivePhaseRegimen.getRadioGroup().getButtons()) {
                            b.setClickable(false);
                        }
                        for (RadioButton b : continuationPhaseRegimen.getRadioGroup().getButtons()) {
                            b.setClickable(false);
                        }

                        for (CheckBox cc : actionPlan.getCheckedBoxes()) {
                            if (App.get(cc).equals(getResources().getString(R.string.pet_new_medication)) && cc.isChecked()) {
                                for (RadioButton b : treatmentPlan.getRadioGroup().getButtons()) {
                                    b.setClickable(true);
                                }
                                for (RadioButton b : intensivePhaseRegimen.getRadioGroup().getButtons()) {
                                    b.setClickable(true);
                                }
                                for (RadioButton b : continuationPhaseRegimen.getRadioGroup().getButtons()) {
                                    b.setClickable(true);
                                }
                            }
                        }

                        treatmentPlan.setVisibility(View.VISIBLE);
                        calculatePeadDosages();

                    }

                } else {
                    for (CheckBox cc : actionPlan.getCheckedBoxes()) {
                        if (App.get(cc).equals(getResources().getString(R.string.pet_new_medication))) {
                            if(!cc.isChecked()) {
                                petRegimen.setVisibility(View.GONE);
                                isoniazidDose.setVisibility(View.GONE);
                                rifapentineDose.setVisibility(View.GONE);
                                levofloxacinDose.setVisibility(View.GONE);
                                ethionamideDose.setVisibility(View.GONE);
                                rifapentineAvailable.setVisibility(View.GONE);

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
                            }
                        }
                    }
                }
            }
        }

        otherAncillaryDrugs.setVisibility(View.GONE);
        for(CheckBox cb: ancillaryDrugs.getCheckedBoxes()){
            if(cb.isChecked())
                ancillaryDrugs.getQuestionView().setError(null);

            if(cb.isChecked() && cb.getText().equals(getString(R.string.other)))
                otherAncillaryDrugs.setVisibility(View.VISIBLE);
        }

        for (CheckBox cb : symptoms.getCheckedBoxes()) {

            if (App.get(cb).equals(getResources().getString(R.string.pet_other))) {
                if (cb.isChecked()) {
                    otherSideEffects.setVisibility(View.VISIBLE);
                } else {
                    otherSideEffects.setVisibility(View.GONE);
                }
            }
        }

        setReferralViews();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (group == petRegimen.getRadioGroup()) {
            calculatePetDosages();
        } else if (group == rifapentineAvailable.getRadioGroup()) {

            int age = App.getPatient().getPerson().getAge();
            Double weightDouble = Double.parseDouble("0");
            if (!App.get(weight).equals("")) {
                weightDouble = Double.parseDouble(App.get(weight));
            }

            Double w = 1.0;

            rifapentineAvailable.setVisibility(View.VISIBLE);
            if (App.get(rifapentineAvailable).equals(getResources().getString(R.string.no))) {

                if(age < 15) {
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

                if(w > 300)
                    isoniazidDose.getEditText().setError(getString(R.string.pet_isoniazid_dose_exceeded_300));

            } else {

                w = 1.0;


                if(age < 2){
                    w = weightDouble * 10f;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));

                    rifapentineDose.getEditText().setHint("Not recommended");
                } else if (age < 12){
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
        } else if (group == patientReferred.getRadioGroup()) {
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
        } else if(group == treatmentPlan.getRadioGroup()){
            calculatePeadDosages();
        }else if (group == intensivePhaseRegimen.getRadioGroup()) {
            intensivePhaseRegimen.getQuestionView().setError(null);
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
            continuationPhaseRegimen.getQuestionView().setError(null);
            if (continuationPhaseRegimen.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_rh))) {
                currentTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationE.setVisibility(View.GONE);
                adultFormulationOfContinuationRHE.setVisibility(View.GONE);
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

    @Override
    public void refill(int encounterId) {

        refillFlag = true;

        OfflineForm fo = serverService.getSavedFormById(encounterId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if(obs[0][0].equals("TIME TAKEN TO FILL FORMs")){
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("WEIGHT (KG)")) {
                weight.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("ADVERSE EVENTS")) {
                for (CheckBox cb : symptoms.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_dizziness)) && obs[0][1].equals("DIZZINESS AND GIDDINESS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_nausea_vomiting)) && obs[0][1].equals("NAUSEA AND VOMITING")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_abdominal_pain)) && obs[0][1].equals("ABDOMINAL PAIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_reduced_appetite)) && obs[0][1].equals("LOSS OF APPETITE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_jaundice)) && obs[0][1].equals("JAUNDICE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_rash)) && obs[0][1].equals("RASH")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_tendon_pain)) && obs[0][1].equals("TENDON PAIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_eye_problems)) && obs[0][1].equals("VISION PROBLEM")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_hypersensitivity_reaction)) && obs[0][1].equals("HYPERSENSITIVITY REACTION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_body_fluid_discoloration)) && obs[0][1].equals("DISCOLORATION OF BODY FLUID")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_diarrohea)) && obs[0][1].equals("DIARRHEA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_muscle_pain)) && obs[0][1].equals("MUSCLE PAIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_other)) && obs[0][1].equals("OTHER ADVERSE EVENT")) {
                        cb.setChecked(true);
                        break;
                    }

                }
                symptoms.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER ADVERSE EVENT")) {
                otherSideEffects.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COMPLAINTS CONSISTENT WITH DRUG SIDE EFFECTS")) {
                for (RadioButton rb : sideeffectsConsistent.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("ACTION PLAN")) {
                for (CheckBox cb : actionPlan.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_continue_medication_greater_adherence)) && obs[0][1].equals("CONTINUE MEDICATION (HIGH ADHERENCE)")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_continue_medication_less_adherence)) && obs[0][1].equals("CONTINUE MEDICATION (LOW ADHERENCE)")) {
                        cb.setChecked(true);
                        break;
                    }
                    if (cb.getText().equals(getResources().getString(R.string.pet_discontinue_medication)) && obs[0][1].equals("DISCONTINUE MEDICATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_begin_cxr_protocol)) && obs[0][1].equals("BEGIN CLINICAL MONITORING PROTOCOL")) {
                        cb.setChecked(true);
                        break;
                    }
                    if (cb.getText().equals(getResources().getString(R.string.pet_new_medication)) && obs[0][1].equals("GIVE NEW MEDICATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_seek_expert_consultation)) && obs[0][1].equals("SEEK EXPERT CONSULTATION")) {
                        cb.setChecked(true);
                        break;
                    }
                    if (cb.getText().equals(getResources().getString(R.string.pet_change_drug_dosage)) && obs[0][1].equals("CHANGE DRUG DOSAGE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_ancillary_drug)) && obs[0][1].equals("GIVE ANCILLARY DRUG")) {
                        cb.setChecked(true);
                        break;
                    }  else if (cb.getText().equals(getResources().getString(R.string.pet_treatment_interrupted)) && obs[0][1].equals("PLANNED TREATMENT INTERRUPTION")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TREATMENT INTERRUPTION REASON")) {
                treatmentInterruptedReason.getEditText().setText(obs[0][1]);
                treatmentInterruptedReason.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REASON TO DISCONTINUE MEDICATION")) {
                medicationDiscontinueReason.getEditText().setText(obs[0][1]);
                medicationDiscontinueReason.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("DURATION OF DISCONTINUATION IN DAYS")) {
                medicationDiscontinueDuration.getEditText().setText(obs[0][1]);
                medicationDiscontinueDuration.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("POST-EXPOSURE TREATMENT REGIMEN")) {
                for (RadioButton rb : petRegimen.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy)) && obs[0][1].equals("ISONIAZID PROPHYLAXIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_isoniazid_rifapentine)) && obs[0][1].equals("ISONIAZID AND RIFAPENTINE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_levofloxacin_ethionamide)) && obs[0][1].equals("LEVOFLOXACIN AND ETHIONAMIDE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_levofloxacin_ethambutol)) && obs[0][1].equals("LEVOFLOXACIN AND ETHAMBUTOL")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_levofloxacin_moxifloxacilin)) && obs[0][1].equals("LEVOFLOXACIN AND MOXIFLOXACILIN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_ethionamide_ethambutol)) && obs[0][1].equals("ETHIONAMIDE AND ETHAMBUTOL")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_ethionamide_moxifloxacilin)) && obs[0][1].equals("ETHIONAMIDE AND MOXIFLOXACILIN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_moxifloxacilin_ethambutol)) && obs[0][1].equals("MOXIFLOXACILIN AND ETHAMBUTOL")) {
                        rb.setChecked(true);
                        break;
                    }

                }
                petRegimen.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ISONIAZID DOSE")) {
                isoniazidDose.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("RIFAPENTINE DOSE")) {
                rifapentineDose.getEditText().setText(obs[0][1]);
                rifapentineAvailable.setVisibility(View.VISIBLE);
                for (RadioButton rb : rifapentineAvailable.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes))) {
                        rb.setChecked(true);
                    }
                }
            } else if (obs[0][0].equals("LEVOFLOXACIN DOSE")) {
                levofloxacinDose.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("ETHIONAMIDE DOSE")) {
                ethionamideDose.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("ETHAMBUTOL DOSE")) {
                ethambutolDose.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("MOXIFLOXACILIN DOSE")) {
                moxifloxacilinDose.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("TREATMENT PLAN")) {
                for (RadioButton rb : treatmentPlan.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_intensive_phase)) && obs[0][1].equals("INTENSIVE PHASE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_continuation_phase)) && obs[0][1].equals("CONTINUE REGIMEN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                treatmentPlan.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REGIMEN") && App.get(treatmentPlan).equals(getResources().getString(R.string.ctb_intensive_phase))) {
                for (RadioButton rb : intensivePhaseRegimen.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_rhze)) && obs[0][1].equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_rhz)) && obs[0][1].equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE")) {
                        rb.setChecked(true);
                        break;
                    }
                    intensivePhaseRegimen.setVisibility(View.VISIBLE);
                }
            } else if (obs[0][0].equals("PAEDIATRIC DOSE COMBINATION")) {
                String value = obs[0][1].equals("CURRENT FORMULATION") ? getResources().getString(R.string.ctb_current_formulation) :
                        (obs[0][1].equals("NEW FORMULATION") ? getResources().getString(R.string.ctb_new_formulation) :
                                getResources().getString(R.string.ctb_adult_formulation));

                typeFixedDosePrescribedIntensive.getSpinner().selectValue(value);
                typeFixedDosePrescribedIntensive.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("CURRENT FORMULATION OF TABLETS OF RHZ")) {
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
            } else if (obs[0][0].equals("CURRENT FORMULATION OF TABLETS OF E")) {
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
            } else if (obs[0][0].equals("NEW FORMULATION OF TABLETS OF E")) {
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
            } else if (obs[0][0].equals("NEW FORMULATION OF TABLETS OF RHZ")) {
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
            } else if (obs[0][0].equals("ADULT FORMULATION OF TABLETS OF RHZE")) {
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
            } else if (obs[0][0].equals("REGIMEN") && App.get(treatmentPlan).equals(getResources().getString(R.string.ctb_continuation_phase))) {
                for (RadioButton rb : continuationPhaseRegimen.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_rh)) && obs[0][1].equals("RIFAMPICIN AND ISONIAZID")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_rhe)) && obs[0][1].equals("RIFAMPICIN ISONIAZID AND ETHAMBUTOL")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                continuationPhaseRegimen.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("PAEDIATRIC FIXED DOSE COMBINATION FOR CONTINUATION PHASE")) {
                String value = obs[0][1].equals("CURRENT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_current_formulation_continuation) :
                        (obs[0][1].equals("NEW FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_new_formulation_continuation) :
                                (obs[0][1].equals("ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_adult_formulation_continuation_rhe) :
                                        (obs[0][1].equals("ADULT FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE UNDER 25KG") ? getResources().getString(R.string.ctb_adult_formulation_rh_20_to_25) :
                                                getResources().getString(R.string.ctb_adult_formulation_continuation_rh)
                                        )));

                typeFixedDosePrescribedContinuation.getSpinner().selectValue(value);
                typeFixedDosePrescribedContinuation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("CURRENT FORMULATION OF TABLETS OF RH")) {
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
            } else if (obs[0][0].equals("CURRENT FORMULATION OF TABLETS OF E FOR CONTINUATION PHASE")) {
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
            } else if (obs[0][0].equals("NEW FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE")) {
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
            } else if (obs[0][0].equals("NEW FORMULATION OF TABLET OF E FOR CONTINUATION PHASE")) {
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
            } else if (obs[0][0].equals("ADULT FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE")) {
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
            } else if (obs[0][0].equals("ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE")) {
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
            } else if (obs[0][0].equals("SEVERITY OF ADVERSE REACTION")) {
                for (RadioButton rb : severity.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_mild)) && obs[0][1].equals("MILD")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_moderate)) && obs[0][1].equals("MODERATE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_severe)) && obs[0][1].equals("SEVERE")) {
                        rb.setChecked(true);
                        break;
                    }

                }
                petRegimen.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ANCILLARY DRUGS")) {
                for (CheckBox cb : ancillaryDrugs.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.iron)) && obs[0][1].equals("IRON")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.multivitamins)) && obs[0][1].equals("MULTIVITAMIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.anthelmintic)) && obs[0][1].equals("ANTHELMINTHIC")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pediasure)) && obs[0][1].equals("PEDIASURE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.vitamin_b_complex)) && obs[0][1].equals("VITAMIN B COMPLEX")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.calpol)) && obs[0][1].equals("CALPOL")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.other)) && obs[0][1].equals("OTHER")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                ancillaryDrugs.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER ANCILLARY DRUGS")) {
                otherAncillaryDrugs.getEditText().setText(obs[0][1]);
                otherAncillaryDrugs.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("MEDICATION DURATION")) {
                ancillaryDrugDuration.getEditText().setText(obs[0][1]);
                ancillaryDrugDuration.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("INSTRUCTIONS TO PATIENT AND/OR FAMILY")) {
                newInstruction.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
                clincianNote.getEditText().setText(obs[0][1]);
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

    public void calculatePeadDosages(){

        if (treatmentPlan.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_intensive_phase))) {
            intensivePhaseRegimen.setVisibility(View.VISIBLE);
            typeFixedDosePrescribedIntensive.setVisibility(View.VISIBLE);

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

            if (App.get(typeFixedDosePrescribedIntensive).equals(getResources().getString(R.string.ctb_current_formulation))) {
                if (!App.get(intensivePhaseRegimen).equals(getResources().getString(R.string.ctb_rhz))) {
                    currentTabletsofE.setVisibility(View.VISIBLE);
                }
                currentTabletsofRHZ.setVisibility(View.VISIBLE);

                newTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                adultFormulationofHRZE.setVisibility(View.GONE);
            } else if (App.get(typeFixedDosePrescribedIntensive).equals(getResources().getString(R.string.ctb_new_formulation))) {
                if (!App.get(intensivePhaseRegimen).equals(getResources().getString(R.string.ctb_rhz))) {
                    newTabletsofE.setVisibility(View.VISIBLE);
                }
                newTabletsofRHZ.setVisibility(View.VISIBLE);

                currentTabletsofE.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
                adultFormulationofHRZE.setVisibility(View.GONE);
            } else if (App.get(typeFixedDosePrescribedIntensive).equals(getResources().getString(R.string.ctb_adult_formulation))) {
                if (!App.get(intensivePhaseRegimen).equals(getResources().getString(R.string.ctb_rhz))) {
                    adultFormulationofHRZE.setVisibility(View.VISIBLE);
                }
                newTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
            }else{
                adultFormulationofHRZE.setVisibility(View.GONE);
                newTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
            }

            continuationPhaseRegimen.setVisibility(View.GONE);
            typeFixedDosePrescribedContinuation.setVisibility(View.GONE);
            currentTabletsOfContinuationRH.setVisibility(View.GONE);
            currentTabletsOfContinuationE.setVisibility(View.GONE);
            newTabletsOfContinuationRH.setVisibility(View.GONE);
            newTabletsOfContinuationE.setVisibility(View.GONE);
            adultFormulationOfContinuationRHE.setVisibility(View.GONE);
            adultFormulationOfContinuationRH.setVisibility(View.GONE);

        }else if(treatmentPlan.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_continuation_phase))) {

            continuationPhaseRegimen.setVisibility(View.VISIBLE);
            typeFixedDosePrescribedContinuation.setVisibility(View.VISIBLE);

            if (continuationPhaseRegimen.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_rh))) {
                currentTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationE.setVisibility(View.GONE);
                adultFormulationOfContinuationRHE.setVisibility(View.GONE);
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

            if (App.get(typeFixedDosePrescribedContinuation).equals(getResources().getString(R.string.ctb_current_formulation_continuation))) {


                if (App.get(continuationPhaseRegimen).equals(getResources().getString(R.string.ctb_rhe))) {
                    currentTabletsOfContinuationE.setVisibility(View.VISIBLE);
                }
                currentTabletsOfContinuationRH.setVisibility(View.VISIBLE);

                newTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRHE.setVisibility(View.GONE);
            } else if (App.get(typeFixedDosePrescribedContinuation).toString().equals(getResources().getString(R.string.ctb_new_formulation_continuation))) {
                if (App.get(continuationPhaseRegimen).equals(getResources().getString(R.string.ctb_rhe))) {
                    newTabletsOfContinuationE.setVisibility(View.VISIBLE);
                }
                newTabletsOfContinuationRH.setVisibility(View.VISIBLE);

                currentTabletsOfContinuationE.setVisibility(View.GONE);
                currentTabletsOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRHE.setVisibility(View.GONE);
            }else if (App.get(typeFixedDosePrescribedContinuation).toString().equals(getResources().getString(R.string.ctb_adult_formulation_continuation_rh))) {
                adultFormulationOfContinuationRH.setVisibility(View.VISIBLE);

                currentTabletsOfContinuationE.setVisibility(View.GONE);
                currentTabletsOfContinuationRH.setVisibility(View.GONE);
                newTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRHE.setVisibility(View.GONE);
            }else if (App.get(typeFixedDosePrescribedContinuation).toString().equals(getResources().getString(R.string.ctb_adult_formulation_continuation_rhe))) {
                if (App.get(continuationPhaseRegimen).equals(getResources().getString(R.string.ctb_rhe))) {
                    adultFormulationOfContinuationRHE.setVisibility(View.VISIBLE);
                }


                currentTabletsOfContinuationE.setVisibility(View.GONE);
                currentTabletsOfContinuationRH.setVisibility(View.GONE);
                newTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRH.setVisibility(View.GONE);
            }else{
                adultFormulationOfContinuationRHE.setVisibility(View.GONE);
                currentTabletsOfContinuationE.setVisibility(View.GONE);
                currentTabletsOfContinuationRH.setVisibility(View.GONE);
                newTabletsOfContinuationE.setVisibility(View.GONE);
                newTabletsOfContinuationRH.setVisibility(View.GONE);
                adultFormulationOfContinuationRH.setVisibility(View.GONE);
            }

            intensivePhaseRegimen.setVisibility(View.GONE);
            typeFixedDosePrescribedIntensive.setVisibility(View.GONE);
            currentTabletsofRHZ.setVisibility(View.GONE);
            currentTabletsofE.setVisibility(View.GONE);
            newTabletsofRHZ.setVisibility(View.GONE);
            newTabletsofE.setVisibility(View.GONE);
            adultFormulationofHRZE.setVisibility(View.GONE);

        }

    }

    public void calculatePetDosages(){

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

            if(age < 15) {
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

            if(w > 300)
                isoniazidDose.getEditText().setError(getString(R.string.pet_isoniazid_dose_exceeded_300));

        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine))) {

            Double w = 1.0;

            rifapentineAvailable.setVisibility(View.VISIBLE);
            if (App.get(rifapentineAvailable).equals(getResources().getString(R.string.no))) {

                if(age < 15) {
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

                if(w > 300)
                    isoniazidDose.getEditText().setError(getString(R.string.pet_isoniazid_dose_exceeded_300));

            } else {

                w = 1.0;


                if(age < 2){
                    w = weightDouble * 10f;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));

                    rifapentineDose.getEditText().setHint("Not recommended");
                } else if (age < 12){
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

            if(weightDouble == 0){
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

                if(weightDouble <= 2){
                    ethambutolDose.getEditText().setText(String.valueOf(""));
                    ethambutolDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 7){
                    ethambutolDose.getEditText().setText(String.valueOf("100"));
                } else if (weightDouble <= 12){
                    ethambutolDose.getEditText().setText(String.valueOf("200"));
                } else if (weightDouble <= 15){
                    ethambutolDose.getEditText().setText(String.valueOf("300"));
                } else if (weightDouble <= 26){
                    ethambutolDose.getEditText().setText(String.valueOf("400"));
                } else if (weightDouble <= 30){
                    ethambutolDose.getEditText().setText(String.valueOf("500"));
                } else if (weightDouble <= 59){
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

            if(weightDouble == 0){
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

                if(weightDouble <= 13){
                    moxifloxacilinDose.getEditText().setText(String.valueOf(""));
                    moxifloxacilinDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 30){
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

            if(weightDouble == 0){
                ethambutolDose.getEditText().setText("");
            } else {

                if(weightDouble <= 2){
                    ethambutolDose.getEditText().setText(String.valueOf(""));
                    ethambutolDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 7){
                    ethambutolDose.getEditText().setText(String.valueOf("100"));
                } else if (weightDouble <= 12){
                    ethambutolDose.getEditText().setText(String.valueOf("200"));
                } else if (weightDouble <= 15){
                    ethambutolDose.getEditText().setText(String.valueOf("300"));
                } else if (weightDouble <= 26){
                    ethambutolDose.getEditText().setText(String.valueOf("400"));
                } else if (weightDouble <= 30){
                    ethambutolDose.getEditText().setText(String.valueOf("500"));
                } else if (weightDouble <= 59){
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

            if(weightDouble == 0){
                moxifloxacilinDose.getEditText().setText("");
            } else {

                if(weightDouble <= 13){
                    moxifloxacilinDose.getEditText().setText(String.valueOf(""));
                    moxifloxacilinDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 30){
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

            if(weightDouble == 0){
                moxifloxacilinDose.getEditText().setText("");
                ethambutolDose.getEditText().setText("");
            } else {

                if(weightDouble <= 2){
                    ethambutolDose.getEditText().setText(String.valueOf(""));
                    ethambutolDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 7){
                    ethambutolDose.getEditText().setText(String.valueOf("100"));
                } else if (weightDouble <= 12){
                    ethambutolDose.getEditText().setText(String.valueOf("200"));
                } else if (weightDouble <= 15){
                    ethambutolDose.getEditText().setText(String.valueOf("300"));
                } else if (weightDouble <= 26){
                    ethambutolDose.getEditText().setText(String.valueOf("400"));
                } else if (weightDouble <= 30){
                    ethambutolDose.getEditText().setText(String.valueOf("500"));
                } else if (weightDouble <= 59){
                    ethambutolDose.getEditText().setText(String.valueOf("1500"));
                } else {
                    ethambutolDose.getEditText().setText(String.valueOf("2000"));
                }

                if(weightDouble <= 13){
                    moxifloxacilinDose.getEditText().setText(String.valueOf(""));
                    moxifloxacilinDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 30){
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
