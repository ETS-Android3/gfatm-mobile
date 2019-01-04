package com.ihsinformatics.gfatmmobile.childhoodtb;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import com.ihsinformatics.gfatmmobile.custom.MyEditText;
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

public class ChildhoodTbTBTreatmentInitiation extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    Boolean dateChoose = false;

    public static final int THIRD_DATE_DIALOG_ID = 3;
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;

    Snackbar snackbar;
    ScrollView scrollView;
    TitledButton formDate;
    TitledEditText weightAtBaseline;
    TitledRadioGroup moConsultPediatrician;
    TitledEditText nameConsultant;
    TitledEditText reasonConsultation;
    TitledRadioGroup tbDaignosed;

    TitledRadioGroup patientHaveTb;
    TitledButton regDate;

    TitledEditText tbRegisterationNumber;
    TitledCheckBoxes tbType;
    TitledCheckBoxes typeOfDiagnosis;
    TitledRadioGroup histopathologicalEvidence;
    TitledRadioGroup radiologicalEvidence;
    TitledSpinner extraPulmonarySite;
    TitledEditText extraPulmonarySiteOther;
    TitledSpinner patientType;
    TitledEditText otherPatientType;
    TitledCheckBoxes testConfirmingDiagnosis;
    TitledRadioGroup infectionType;
    TitledEditText testConfirmingOthers;
    TitledRadioGroup treatmentInitiated;
    TitledSpinner reasonTreatmentNotIniated;
    TitledEditText otherReasonNotInitiated;
    TitledCheckBoxes initiatingAdditionalTreatment;
    TitledEditText otherAdditionalTreatment;
    TitledRadioGroup patientCategory;
    TitledEditText weight;
    TitledEditText weightPercentileEditText;

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
    TitledRadioGroup followupRequired;


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
    MyTextView moInstruction;
    TitledButton returnVisitDate;


    TitledEditText doctorNotes;
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
        formName = Forms.CHILDHOODTB_TB_TREATMENT_INITIATION;
        form = Forms.childhoodTb_tb_treatment_intiation;

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

        thirdDateCalendar = Calendar.getInstance();
        thirdDateFragment = new SelectDateFragment();

        regDate = new TitledButton(context, "TB Treatment Initiation ", getResources().getString(R.string.fast_registeration_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);

        // first page views...
        formDate = new TitledButton(context, "Section A: TB Treatment Initiation ", getResources().getString(R.string.pet_form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        weightAtBaseline = new TitledEditText(context, null, getResources().getString(R.string.ctb_weight_at_baseline), "", "", 3, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);
        moConsultPediatrician = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_mo_consult_pediatrician), getResources().getStringArray(R.array.yes_no_options),null, App.HORIZONTAL, App.VERTICAL,true);
        nameConsultant = new TitledEditText(context, null, getResources().getString(R.string.ctb_name_consultant), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        reasonConsultation = new TitledEditText(context, null, getResources().getString(R.string.ctb_reason_consultation), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        tbDaignosed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_daignosed), getResources().getStringArray(R.array.yes_no_options),null, App.HORIZONTAL, App.VERTICAL,true);
        tbRegisterationNumber = new TitledEditText(context, null, getResources().getString(R.string.ctb_tb_registration_no), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        patientHaveTb = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_patient_have_tb), getResources().getStringArray(R.array.yes_only),getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL,true);
        MyTextView cnic = new MyTextView(context, getResources().getString(R.string.pet_cnic));
        tbType = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_type_of_tb_name), getResources().getStringArray(R.array.ctb_extra_pulomonary_and_pulmonary), null, App.VERTICAL,App.VERTICAL,true);
        extraPulmonarySite = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_site_of_extra_pulmonary), getResources().getStringArray(R.array.ctb_extra_pulmonary_tb_site), getResources().getString(R.string.ctb_lymph_node), App.VERTICAL,true);
        extraPulmonarySiteOther = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_extra_pulmonary_site), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        patientType = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_patient_type), getResources().getStringArray(R.array.ctb_patient_type_list), getResources().getString(R.string.ctb_new), App.VERTICAL,true);
        otherPatientType = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        testConfirmingDiagnosis = new TitledCheckBoxes(mainContent.getContext(), "", getResources().getString(R.string.ctb_test_confirming_diagnosis), getResources().getStringArray(R.array.ctb_confirming_diagnosis_list), null, App.VERTICAL,App.VERTICAL,true);
        testConfirmingOthers = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        treatmentInitiated = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_are_you_intiating_treatment), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL,true);
        reasonTreatmentNotIniated = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_reason_treatment_not_intiated), getResources().getStringArray(R.array.ctb_reason_treatment_not_intiated_list), getResources().getString(R.string.ctb_patient_refused_treatment), App.VERTICAL,true);
        otherReasonNotInitiated = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        initiatingAdditionalTreatment = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_initiating_additional_treatment), getResources().getStringArray(R.array.ctb_pediasure_vitamin_iron_anthelminthic_calpol), null, App.VERTICAL, App.VERTICAL);
        otherAdditionalTreatment = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        patientCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_patient_category), getResources().getStringArray(R.array.ctb_patient_category3_list), getResources().getString(R.string.ctb_categoryI), App.VERTICAL, App.VERTICAL,true);
        weight = new TitledEditText(context, null, getResources().getString(R.string.ctb_patient_weight), "", "", 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        weightPercentileEditText = new TitledEditText(context, null,getResources().getString(R.string.ctb_weight_percentile), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        treatmentPlan = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_treatment_plan), getResources().getStringArray(R.array.ctb_ti_list), null, App.VERTICAL, App.VERTICAL,true);

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

        typeOfDiagnosis = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_type_of_diagnosis), getResources().getStringArray(R.array.ctb_type_of_diagnosis_list), null, App.VERTICAL, App.VERTICAL);

        histopathologicalEvidence = new TitledRadioGroup(context, "If clinically diagnosed, specify evidence", getResources().getString(R.string.ctb_histopathologiacal_evidence), getResources().getStringArray(R.array.yes_no_options), null, App.VERTICAL, App.VERTICAL);
        radiologicalEvidence = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_radiological_evidence), getResources().getStringArray(R.array.yes_no_options), null, App.VERTICAL, App.VERTICAL);
        infectionType = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_infection_type), getResources().getStringArray(R.array.ctb_dstb_drtb), null, App.VERTICAL, App.VERTICAL);
        patientReferred  = new TitledRadioGroup(context, null, getResources().getString(R.string.refer_patient), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL,true);
        referredTo = new TitledCheckBoxes(context, null, getResources().getString(R.string.refer_patient_to), getResources().getStringArray(R.array.refer_patient_to_option), null, App.VERTICAL, App.VERTICAL, true);
        referalReasonPsychologist = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_psychologist), getResources().getStringArray(R.array.referral_reason_for_psychologist_option), null, App.VERTICAL, App.VERTICAL, true);
        otherReferalReasonPsychologist = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        referalReasonSupervisor = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_supervisor), getResources().getStringArray(R.array.referral_reason_for_supervisor_option), null, App.VERTICAL, App.VERTICAL, true);
        otherReferalReasonSupervisor = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        referalReasonCallCenter = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_center), getResources().getStringArray(R.array.referral_reason_for_call_center_option), null, App.VERTICAL, App.VERTICAL, true);
        otherReferalReasonCallCenter = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        referalReasonClinician = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_clinician), getResources().getStringArray(R.array.referral_reason_for_clinician_option), null, App.VERTICAL, App.VERTICAL, true);
        otherReferalReasonClinician = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        thirdDateCalendar.add(Calendar.DAY_OF_MONTH, 30);
        followupRequired  = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_followup_required), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.HORIZONTAL,true);
        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.ctb_next_appointment_date), DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);
        moInstruction = new MyTextView(context,getResources().getString(R.string.ctb_treatment_initiation_mo_instruction));
        doctorNotes = new TitledEditText(context, null, getResources().getString(R.string.ctb_doctor_notes), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL,false);


        views = new View[]{formDate.getButton(),weightAtBaseline.getEditText(),
                moConsultPediatrician.getRadioGroup(),
                nameConsultant.getEditText(),
                regDate.getButton(),
                reasonConsultation.getEditText(),
                tbDaignosed.getRadioGroup(),testConfirmingOthers.getEditText(),
                patientHaveTb.getRadioGroup(),tbRegisterationNumber.getEditText(),tbType,
                extraPulmonarySite.getSpinner(),extraPulmonarySiteOther.getEditText(),patientType.getSpinner(),treatmentInitiated.getRadioGroup(),
                reasonTreatmentNotIniated.getSpinner(),initiatingAdditionalTreatment,patientCategory.getRadioGroup(),
                weight.getEditText(),                treatmentPlan.getRadioGroup(), intensivePhaseRegimen.getRadioGroup(),typeFixedDosePrescribedIntensive.getSpinner(),currentTabletsofRHZ.getRadioGroup(),currentTabletsofE.getRadioGroup(),
                newTabletsofRHZ.getRadioGroup(),newTabletsofE.getRadioGroup(),adultFormulationofHRZE.getRadioGroup(), continuationPhaseRegimen.getRadioGroup(),typeFixedDosePrescribedContinuation.getSpinner(),
                currentTabletsOfContinuationRH.getRadioGroup(), currentTabletsOfContinuationE.getRadioGroup(), newTabletsOfContinuationRH.getRadioGroup(), newTabletsOfContinuationE.getRadioGroup(),
                adultFormulationOfContinuationRH.getRadioGroup(), adultFormulationOfContinuationRHE.getRadioGroup(),typeOfDiagnosis,histopathologicalEvidence.getRadioGroup(),
                radiologicalEvidence.getRadioGroup(), returnVisitDate.getButton(),
                doctorNotes.getEditText(),testConfirmingDiagnosis,weightPercentileEditText.getEditText(),
                infectionType.getRadioGroup(),otherPatientType.getEditText(),otherReasonNotInitiated.getEditText(),otherAdditionalTreatment.getEditText(),
                followupRequired.getRadioGroup(),patientReferred.getRadioGroup(), referalReasonCallCenter, otherReferalReasonCallCenter.getEditText(), referalReasonClinician, otherReferalReasonClinician.getEditText()};
        viewGroups = new View[][]
                {{
                        formDate,
                        weightAtBaseline,
                        weightPercentileEditText,
                        moConsultPediatrician,
                        nameConsultant,
                        reasonConsultation,
                        tbDaignosed,
                        patientHaveTb,
                        typeOfDiagnosis,
                        histopathologicalEvidence,
                        radiologicalEvidence,
                        testConfirmingDiagnosis,
                        testConfirmingOthers,
                        infectionType,
                        tbType,
                        extraPulmonarySite,
                        extraPulmonarySiteOther,
                        patientType,
                        otherPatientType,
                        patientCategory,
                        regDate,
                        tbRegisterationNumber,
                        treatmentInitiated,
                        reasonTreatmentNotIniated,
                        otherReasonNotInitiated,
                        initiatingAdditionalTreatment,
                        otherAdditionalTreatment,
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
                        followupRequired,
                        returnVisitDate,
                        moInstruction,
                        patientReferred, referredTo, referalReasonPsychologist, otherReferalReasonPsychologist, referalReasonSupervisor, otherReferalReasonSupervisor,
                        referalReasonCallCenter, otherReferalReasonCallCenter, referalReasonClinician, otherReferalReasonClinician,
                        doctorNotes,
                }};
        formDate.getButton().setOnClickListener(this);
        patientHaveTb.getRadioGroup().setOnCheckedChangeListener(this);
        followupRequired.getRadioGroup().setOnCheckedChangeListener(this);
        extraPulmonarySite.getSpinner().setOnItemSelectedListener(this);
        patientType.getSpinner().setOnItemSelectedListener(this);
        treatmentInitiated.getRadioGroup().setOnCheckedChangeListener(this);
        infectionType.getRadioGroup().setOnCheckedChangeListener(this);
        reasonTreatmentNotIniated.getSpinner().setOnItemSelectedListener(this);
        for (CheckBox cb : testConfirmingDiagnosis.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        ArrayList<MyCheckBox> checkBoxList = initiatingAdditionalTreatment.getCheckedBoxes();
        for (CheckBox cb : initiatingAdditionalTreatment.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        for (CheckBox cb : tbType.getCheckedBoxes())
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
        ArrayList<MyCheckBox> checkBoxList2 = typeOfDiagnosis.getCheckedBoxes();
        for (CheckBox cb : typeOfDiagnosis.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        regDate.getButton().setOnClickListener(this);
        histopathologicalEvidence.getRadioGroup().setOnCheckedChangeListener(this);
        radiologicalEvidence.getRadioGroup().setOnCheckedChangeListener(this);
        moConsultPediatrician.getRadioGroup().setOnCheckedChangeListener(this);
        tbDaignosed.getRadioGroup().setOnCheckedChangeListener(this);
        returnVisitDate.getButton().setOnClickListener(this);


        weight.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
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

        weightPercentileEditText.getEditText().setKeyListener(null);

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

            if (requiredDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                thirdDateCalendar.setTime(requiredDate.getTime());
            } else {
                requiredDate.add(Calendar.DATE, 1);
                thirdDateCalendar.setTime(requiredDate.getTime());
            }

        }
        if (!(regDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                regDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                regDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                regDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }

        String nextAppointmentDateString = App.getSqlDate(thirdDateCalendar);
        Date nextAppointmentDate = App.stringToDate(nextAppointmentDateString, "yyyy-MM-dd");

        String formDateString = App.getSqlDate(formDateCalendar);
        Date formStDate = App.stringToDate(formDateString, "yyyy-MM-dd");


        if (!(returnVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString()))) {
            Calendar dateToday = Calendar.getInstance();
            dateToday.add(Calendar.MONTH, 24);

            if (thirdDateCalendar.before(formDateCalendar)) {

                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());

            }
            else if(thirdDateCalendar.after(dateToday)){
                thirdDateCalendar= App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

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
        returnVisitDate.getButton().setEnabled(true);
        regDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = false;
        if(App.get(moConsultPediatrician).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            moConsultPediatrician.getQuestionView().setError(getString(R.string.empty_field));
            moConsultPediatrician.getRadioGroup().requestFocus();
            error = true;
        }
        if(nameConsultant.getVisibility()==View.VISIBLE && App.get(nameConsultant).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            nameConsultant.getEditText().setError(getString(R.string.empty_field));
            nameConsultant.requestFocus();
            error = true;
        }
        if(reasonConsultation.getVisibility()==View.VISIBLE && App.get(reasonConsultation).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reasonConsultation.getEditText().setError(getString(R.string.empty_field));
            reasonConsultation.requestFocus();
            error = true;
        }
        if(tbDaignosed.getVisibility()==View.VISIBLE && App.get(tbDaignosed).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            tbDaignosed.getQuestionView().setError(getString(R.string.empty_field));
            tbDaignosed.getRadioGroup().requestFocus();
            error = true;
        }
        if(typeOfDiagnosis.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : typeOfDiagnosis.getCheckedBoxes()) {
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
                typeOfDiagnosis.getQuestionView().setError(getString(R.string.empty_field));
                typeOfDiagnosis.getQuestionView().requestFocus();
                error = true;
            }
        }
        if(radiologicalEvidence.getVisibility()==View.VISIBLE && App.get(radiologicalEvidence).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            radiologicalEvidence.getQuestionView().setError(getString(R.string.empty_field));
            radiologicalEvidence.getRadioGroup().requestFocus();
            error = true;
        }
        if(histopathologicalEvidence.getVisibility()==View.VISIBLE && App.get(histopathologicalEvidence).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            histopathologicalEvidence.getQuestionView().setError(getString(R.string.empty_field));
            histopathologicalEvidence.getRadioGroup().requestFocus();
            error = true;
        }
        if(testConfirmingDiagnosis.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            Boolean flag2 = false;
            for (CheckBox cb : testConfirmingDiagnosis.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }

                if(App.get(cb).equals(getString(R.string.ctb_other_title))){
                    flag2 = true;
                }
            }
            if(!flag){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                testConfirmingDiagnosis.getQuestionView().setError(getString(R.string.empty_field));
                testConfirmingDiagnosis.getQuestionView().requestFocus();
                error = true;
            }

            if(flag2 && App.get(testConfirmingOthers).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                testConfirmingOthers.getQuestionView().setError(getString(R.string.empty_field));
                testConfirmingOthers.getQuestionView().requestFocus();
                error = true;
            }
        }
        if(tbType.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : tbType.getCheckedBoxes()) {
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
                tbType.getQuestionView().setError(getString(R.string.empty_field));
                tbType.getQuestionView().requestFocus();
                error = true;
            }
        }
        if (extraPulmonarySiteOther.getVisibility() == View.VISIBLE) {
            if(App.get(extraPulmonarySiteOther).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                extraPulmonarySiteOther.getEditText().setError(getString(R.string.empty_field));
                extraPulmonarySiteOther.getEditText().requestFocus();
                error = true;
            }
            else if(App.get(extraPulmonarySiteOther).trim().length() <= 0){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                extraPulmonarySiteOther.getEditText().setError(getString(R.string.ctb_spaces_only));
                extraPulmonarySiteOther.getEditText().requestFocus();
                error = true;
            }
        }
        if(otherPatientType.getVisibility()==View.VISIBLE && App.get(otherPatientType).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherPatientType.getEditText().setError(getString(R.string.empty_field));
            otherPatientType.requestFocus();
            error = true;
        }
        if(patientCategory.getVisibility()==View.VISIBLE && App.get(patientCategory).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            patientCategory.getQuestionView().setError(getString(R.string.empty_field));
            patientCategory.getRadioGroup().requestFocus();
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

        if(treatmentInitiated.getVisibility()==View.VISIBLE && App.get(treatmentInitiated).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            treatmentInitiated.getQuestionView().setError(getString(R.string.empty_field));
            treatmentInitiated.getRadioGroup().requestFocus();
            error = true;
        }
        if(reasonTreatmentNotIniated.getVisibility()==View.VISIBLE && App.get(reasonTreatmentNotIniated).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reasonTreatmentNotIniated.getQuestionView().setError(getString(R.string.empty_field));
            reasonTreatmentNotIniated.requestFocus();
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
        if(weight.getVisibility() == View.VISIBLE) {

            if (App.get(weight).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                weight.getEditText().setError(getString(R.string.empty_field));
                weight.getEditText().requestFocus();
                error = true;
            } else {

                if (App.get(weight).length() == 4 && StringUtils.countMatches(App.get(weight), ".") == 0) {
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    weight.getEditText().setError(getString(R.string.ctb_invalid_value_weight));
                    weight.getEditText().requestFocus();
                    error = true;
                } else if (StringUtils.countMatches(App.get(weight), ".") > 1) {
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    weight.getEditText().setError(getString(R.string.ctb_invalid_value_weight));
                    weight.getEditText().requestFocus();
                    error = true;
                } else {
                    Double w = Double.parseDouble(App.get(weight));
                    if (w < 0.5 || w > 700.0) {
                        weight.getEditText().setError(getString(R.string.pet_invalid_weight_range));
                        gotoFirstPage();
                        error = true;
                        weight.getQuestionView().requestFocus();
                    } else {
                        weight.getEditText().setError(null);
                        weight.getQuestionView().clearFocus();
                    }
                }
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

        View view = null;
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

        if(moConsultPediatrician.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"MO CONSULTED SENIOR PEDIATRICIAN FOR TB DIAGNOSIS", App.get(moConsultPediatrician).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        }
        if(nameConsultant.getVisibility()==View.VISIBLE)
            observations.add(new String[]{"CONSULTANT NAME", App.get(nameConsultant)});
        if(reasonConsultation.getVisibility()==View.VISIBLE)
            observations.add(new String[]{"REASON FOR CONSULTATION", App.get(reasonConsultation)});
        if(tbDaignosed.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"TUBERCULOSIS DIAGNOSED", App.get(tbDaignosed).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        }

        observations.add(new String[]{"PATIENT HAVE TB", App.get(patientHaveTb).toUpperCase()});


        if(typeOfDiagnosis.getVisibility()==View.VISIBLE){
            String typeofDiagnosisString = "";
            for (CheckBox cb : typeOfDiagnosis.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_bacteriologically_confirmed)))
                    typeofDiagnosisString = typeofDiagnosisString + "PRIMARY RESPIRATORY TUBERCULOSIS, CONFIRMED BACTERIOLOGICALLY" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_clinically_diagnosed)))
                    typeofDiagnosisString = typeofDiagnosisString + "CLINICAL SUSPICION" + " ; ";
            }
            observations.add(new String[]{"TUBERCULOSIS DIAGNOSIS METHOD", typeofDiagnosisString});
        }

        if(histopathologicalEvidence.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"HISTOPATHOLOGICAL EVIDENCE", App.get(histopathologicalEvidence).toUpperCase()});
        }

        if(radiologicalEvidence.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"RADIOLOGICAL EVIDENCE", App.get(radiologicalEvidence).toUpperCase()});
        }

        if(testConfirmingDiagnosis.getVisibility()==View.VISIBLE){
            String testConfirmingString = "";
            for (CheckBox cb : testConfirmingDiagnosis.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_chest_xray)))
                    testConfirmingString = testConfirmingString + "REFERRED CHEST X RAY" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_ultrasound)))
                    testConfirmingString = testConfirmingString + "REFERRED ULTRASOUND" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_ct_scan)))
                    testConfirmingString = testConfirmingString + "REFERRED CT SCAN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_gene_xpert)))
                    testConfirmingString = testConfirmingString + "REFERRED GENEXPERT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_mantoux)))
                    testConfirmingString = testConfirmingString + "REFERRED MANTOUX TEST" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_smear_microscopy)))
                    testConfirmingString = testConfirmingString + "REFERRED SMEAR MICROSCOPY" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_histopathology)))
                    testConfirmingString = testConfirmingString + "REFERRED HISTOPATHOLOGY OR FNAC" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_cbc)))
                    testConfirmingString = testConfirmingString + "REFERRED CBC" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_esr)))
                    testConfirmingString = testConfirmingString + "REFERRED ESR TEST" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_drug_sensitivity)))
                    testConfirmingString = testConfirmingString + "REFERRED DRUG SENSITIVITY TEST" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_other_title)))
                    testConfirmingString = testConfirmingString + "OTHER DIAGNOSIS" + " ; ";

            }
            observations.add(new String[]{"CONFIRMED DIAGNOSIS", testConfirmingString});
        }
        if(testConfirmingOthers.getVisibility()==View.VISIBLE)
            observations.add(new String[]{"OTHER DIAGNOSIS", App.get(testConfirmingOthers)});

        if (infectionType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOSIS INFECTION TYPE", App.get(infectionType).equals(getResources().getString(R.string.DSTB)) ? "DRUG-SENSITIVE TUBERCULOSIS INFECTION" : "DRUG-RESISTANT TB"});

        if(tbType.getVisibility()==View.VISIBLE){
            String testConfirmingString = "";
            for (CheckBox cb : tbType.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_extra_pulmonary)))
                    testConfirmingString = testConfirmingString + "EXTRA-PULMONARY TUBERCULOSIS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_pulmonary)))
                    testConfirmingString = testConfirmingString + "PULMONARY TUBERCULOSIS" + " ; ";
            }
            observations.add(new String[]{"SITE OF TUBERCULOSIS DISEASE", testConfirmingString});
        }
        if (extraPulmonarySite.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"EXTRA PULMONARY SITE", App.get(extraPulmonarySite).equals(getResources().getString(R.string.ctb_lymph_node)) ? "LYMPH NODE SARCOIDOSIS" :
                    (App.get(extraPulmonarySite).equals(getResources().getString(R.string.ctb_abdomen)) ? "ABDOMEN" :
                            (App.get(extraPulmonarySite).equals(getResources().getString(R.string.ctb_cns)) ? "ACUTE LYMPHOBLASTIC LEUKEMIA WITH CENTRAL NERVOUS SYSTEM INVOLVEMENT" :
                                    (App.get(extraPulmonarySite).equals(getResources().getString(R.string.ctb_renal)) ? "RENAL DISEASE" :
                                            (App.get(extraPulmonarySite).equals(getResources().getString(R.string.ctb_bones)) ? "TUBERCULOSIS OF BONES AND JOINTS" :
                                                    (App.get(extraPulmonarySite).equals(getResources().getString(R.string.ctb_genitourinary)) ? "GENITOURINARY TUBERCULOSIS" :
                                                            (App.get(extraPulmonarySite).equals(getResources().getString(R.string.ctb_pleural_effusion)) ? "PLEURAL EFFUSION" : "OTHER EXTRA PULMONARY SITE"))))))});
        }

        if(extraPulmonarySiteOther.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"OTHER EXTRA PULMONARY SITE", App.get(extraPulmonarySiteOther)});
        }




        if (patientType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TB PATIENT TYPE", App.get(patientType).equals(getResources().getString(R.string.ctb_new)) ? "NEW TB PATIENT" :
                    (App.get(patientType).equals(getResources().getString(R.string.ctb_relapse)) ? "RELAPSE" :
                            (App.get(patientType).equals(getResources().getString(R.string.ctb_reffered)) ? "PATIENT REFERRED" :
                                    (App.get(patientType).equals(getResources().getString(R.string.ctb_transfer_in)) ? "TRANSFER IN" :
                                        (App.get(patientType).equals(getResources().getString(R.string.ctb_treatment_after_followup)) ? "LOST TO FOLLOW-UP" :
                                            (App.get(patientType).equals(getResources().getString(R.string.ctb_treatment_failure)) ? "TUBERCULOSIS TREATMENT FAILURE" : "OTHER PATIENT TYPE")))))});



        if(otherPatientType.getVisibility()==View.VISIBLE)
            observations.add(new String[]{"OTHER PATIENT TYPE", App.get(otherPatientType)});


        if (patientCategory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TB CATEGORY", App.get(patientCategory).equals(getResources().getString(R.string.ctb_categoryI)) ? "CATEGORY I TUBERCULOSIS" :
                    App.get(patientCategory).equals(getResources().getString(R.string.ctb_categoryII)) ? "CATEGORY II TUBERCULOSIS"
                            : "CATEGORY III TUBERCULOSIS"});

        if (regDate.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REGISTRATION DATE", App.getSqlDateTime(secondDateCalendar)});


        if(tbRegisterationNumber.getVisibility()==View.VISIBLE && !App.get(tbRegisterationNumber).isEmpty())
            observations.add(new String[]{"TB REGISTRATION NUMBER", App.get(tbRegisterationNumber)});

        if (treatmentInitiated.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT INITIATED", App.get(treatmentInitiated).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if (reasonTreatmentNotIniated.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT NOT STARTED", App.get(reasonTreatmentNotIniated).equals(getResources().getString(R.string.ctb_patient_refused_treatment)) ? "REFUSAL OF TREATMENT BY PATIENT" :
                    (App.get(reasonTreatmentNotIniated).equals(getResources().getString(R.string.ctb_patient_loss_to_followup)) ? "LOST TO FOLLOW-UP" :
                            (App.get(reasonTreatmentNotIniated).equals(getResources().getString(R.string.ctb_patient_died)) ? "DECEASED" :
                                    (App.get(reasonTreatmentNotIniated).equals(getResources().getString(R.string.ctb_referral_before_starting_treatment)) ? "PATIENT REFERRED" : "TREATMENT NOT INITIATED OTHER REASON")))});

        if(otherReasonNotInitiated.getVisibility()==View.VISIBLE)
            observations.add(new String[]{"TREATMENT NOT INITIATED OTHER REASON", App.get(otherReasonNotInitiated)});

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

        if(weight.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"WEIGHT (KG)", App.get(weight)});
        }

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

        if(followupRequired.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"FOLLOW UP REQUIRED", App.get(followupRequired).toUpperCase()});
        }

        if(returnVisitDate.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDateTime(thirdDateCalendar)});
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
                    id = serverService.saveFormLocallyTesting("Childhood TB-TB Treatment Initiation", form, formDateCalendar,observations.toArray(new String[][]{}));

                String result = "";

                result = serverService.saveMultiplePersonAttribute(personAttribute, id);
                if (!result.equals("SUCCESS"))
                    return result;

                result = serverService.saveEncounterAndObservationTesting("Childhood TB-TB Treatment Initiation", form, formDateCalendar, observations.toArray(new String[][]{}),id);
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
            } else if (obs[0][0].equals("MO CONSULTED SENIOR PEDIATRICIAN FOR TB DIAGNOSIS")) {
                for (RadioButton rb : moConsultPediatrician.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                moConsultPediatrician.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("CONSULTANT NAME")) {
                nameConsultant.getEditText().setText(obs[0][1]);
                nameConsultant.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("REASON FOR CONSULTATION")) {
                reasonConsultation.getEditText().setText(obs[0][1]);
                reasonConsultation.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("TUBERCULOSIS DIAGNOSED")) {
                for (RadioButton rb : tbDaignosed.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                tbDaignosed.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("PATIENT HAVE TB")) {
                for (RadioButton rb : patientHaveTb.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                patientHaveTb.setVisibility(View.VISIBLE);
            }
            else if(obs[0][0].equals("TUBERCULOSIS DIAGNOSIS METHOD")) {
                for (CheckBox cb : typeOfDiagnosis.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ctb_bacteriologically_confirmed)) && obs[0][1].equals("PRIMARY RESPIRATORY TUBERCULOSIS, CONFIRMED BACTERIOLOGICALLY")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_clinically_diagnosed)) && obs[0][1].equals("CLINICAL SUSPICION")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("HISTOPATHOLOGICAL EVIDENCE")) {
                for (RadioButton rb : histopathologicalEvidence.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                histopathologicalEvidence.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("RADIOLOGICAL EVIDENCE")) {
                for (RadioButton rb : radiologicalEvidence.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                radiologicalEvidence.setVisibility(View.VISIBLE);
            }
            else if(obs[0][0].equals("CONFIRMED DIAGNOSIS")) {
                for (CheckBox cb : testConfirmingDiagnosis.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ctb_chest_xray)) && obs[0][1].equals("REFERRED CHEST X RAY")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_ultrasound)) && obs[0][1].equals("REFERRED ULTRASOUND")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_ct_scan)) && obs[0][1].equals("REFERRED CT SCAN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_gene_xpert)) && obs[0][1].equals("REFERRED GENEXPERT")) {
                        cb.setChecked(true);
                        break;
                    }else if (cb.getText().equals(getResources().getString(R.string.ctb_mantoux)) && obs[0][1].equals("REFERRED MANTOUX TEST")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_smear_microscopy)) && obs[0][1].equals("REFERRED SMEAR MICROSCOPY")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_histopathology)) && obs[0][1].equals("REFERRED HISTOPATHOLOGY OR FNAC")) {
                        cb.setChecked(true);
                        break;
                    }else if (cb.getText().equals(getResources().getString(R.string.ctb_cbc)) && obs[0][1].equals("REFERRED CBC")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_esr)) && obs[0][1].equals("REFERRED ESR TEST")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_drug_sensitivity)) && obs[0][1].equals("REFERRED DRUG SENSITIVITY TEST")) {
                        cb.setChecked(true);
                        break;
                    }
                    else if (cb.getText().equals(getResources().getString(R.string.ctb_other_title)) && obs[0][1].equals("OTHER DIAGNOSIS")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                testConfirmingDiagnosis.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("OTHER DIAGNOSIS")) {
                testConfirmingOthers.getEditText().setText(obs[0][1]);
                testConfirmingOthers.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("TUBERCULOSIS INFECTION TYPE")) {

                for (RadioButton rb : infectionType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.DSTB)) && obs[0][1].equals("DRUG-SENSITIVE TUBERCULOSIS INFECTION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.DRTB)) && obs[0][1].equals("DRUG-RESISTANT TB")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                infectionType.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("SITE OF TUBERCULOSIS DISEASE")) {
                for (CheckBox cb : tbType.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ctb_pulmonary)) && obs[0][1].equals("PULMONARY TUBERCULOSIS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_extra_pulmonary)) && obs[0][1].equals("EXTRA-PULMONARY TUBERCULOSIS")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                tbType.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("EXTRA PULMONARY SITE")) {
                String value = obs[0][1].equals("LYMPH NODE SARCOIDOSIS") ? getResources().getString(R.string.ctb_lymph_node) :
                        (obs[0][1].equals("ABDOMEN") ? getResources().getString(R.string.ctb_abdomen) :
                                (obs[0][1].equals("ACUTE LYMPHOBLASTIC LEUKEMIA WITH CENTRAL NERVOUS SYSTEM INVOLVEMENT") ? getResources().getString(R.string.ctb_cns) :
                                        (obs[0][1].equals("RENAL DISEASE") ? getResources().getString(R.string.ctb_renal) :
                                                (obs[0][1].equals("TUBERCULOSIS OF BONES AND JOINTS") ? getResources().getString(R.string.ctb_bones) :
                                                        (obs[0][1].equals("GENITOURINARY TUBERCULOSIS") ? getResources().getString(R.string.ctb_genitourinary) :
                                                                (obs[0][1].equals("PLEURAL EFFUSION") ? getResources().getString(R.string.ctb_pleural_effusion) :
                                                                        getResources().getString(R.string.ctb_other_title)))))));

                extraPulmonarySite.getSpinner().selectValue(value);
                extraPulmonarySite.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("OTHER EXTRA PULMONARY SITE")) {
                extraPulmonarySiteOther.getEditText().setText(obs[0][1]);
                extraPulmonarySiteOther.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("TB PATIENT TYPE")) {
                String value = obs[0][1].equals("NEW TB PATIENT") ? getResources().getString(R.string.ctb_new) :
                        (obs[0][1].equals("RELAPSE") ? getResources().getString(R.string.ctb_relapse) :
                                (obs[0][1].equals("PATIENT REFERRED") ? getResources().getString(R.string.ctb_reffered) :
                                        (obs[0][1].equals("TRANSFER IN") ? getResources().getString(R.string.ctb_transfer_in) :
                                          (obs[0][1].equals("LOST TO FOLLOW-UP") ? getResources().getString(R.string.ctb_treatment_after_followup) :
                                                (obs[0][1].equals("TUBERCULOSIS TREATMENT FAILURE") ? getResources().getString(R.string.ctb_treatment_failure) :
                                                        getResources().getString(R.string.ctb_other_title))))));

                patientType.getSpinner().selectValue(value);
                patientType.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("OTHER PATIENT TYPE")) {
                otherPatientType.getEditText().setText(obs[0][1]);
                otherPatientType.setVisibility(View.VISIBLE);
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
            }else if (obs[0][0].equals("REGISTRATION DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                regDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                regDate.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("TB REGISTRATION NUMBER")) {
                tbRegisterationNumber.getEditText().setText(obs[0][1]);
                tbRegisterationNumber.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("TREATMENT INITIATED")) {
                for (RadioButton rb : treatmentInitiated.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                treatmentInitiated.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("TREATMENT NOT STARTED")) {
                String value = obs[0][1].equals("REFUSAL OF TREATMENT BY PATIENT") ? getResources().getString(R.string.ctb_patient_refused_treatment) :
                        (obs[0][1].equals("LOST TO FOLLOW-UP") ? getResources().getString(R.string.ctb_patient_loss_to_followup) :
                                (obs[0][1].equals("DECEASED") ? getResources().getString(R.string.ctb_patient_died) :
                                        (obs[0][1].equals("PATIENT REFERRED") ? getResources().getString(R.string.ctb_referral_before_starting_treatment) :
                                                getResources().getString(R.string.ctb_other_title))));

                reasonTreatmentNotIniated.getSpinner().selectValue(value);
                reasonTreatmentNotIniated.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("TREATMENT NOT INITIATED OTHER REASON")) {
                otherReasonNotInitiated.getEditText().setText(obs[0][1]);
                otherReasonNotInitiated.setVisibility(View.VISIBLE);
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

            else if (obs[0][0].equals("WEIGHT (KG)")) {
                weight.getEditText().setText(obs[0][1]);
                weight.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("TREATMENT PLAN")) {
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
            }else if (obs[0][0].equals("REGIMEN") && App.get(treatmentPlan).equals(getResources().getString(R.string.ctb_intensive_phase))) {
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
            }else if (obs[0][0].equals("PAEDIATRIC DOSE COMBINATION")) {
                String value = obs[0][1].equals("CURRENT FORMULATION") ? getResources().getString(R.string.ctb_current_formulation) :
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
            }else if (obs[0][0].equals("REGIMEN") && App.get(treatmentPlan).equals(getResources().getString(R.string.ctb_continuation_phase))) {
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
            }else if (obs[0][0].equals("PAEDIATRIC FIXED DOSE COMBINATION FOR CONTINUATION PHASE")) {
                String value = obs[0][1].equals("CURRENT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_current_formulation_continuation) :
                        (obs[0][1].equals("NEW FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_new_formulation_continuation) :
                                (obs[0][1].equals("ADULT FORMULATION OF TABLETS OF RHE FOR CONTINUATION PHASE") ? getResources().getString(R.string.ctb_adult_formulation_continuation_rhe) :
                                        (obs[0][1].equals("ADULT FORMULATION OF TABLETS OF RH FOR CONTINUATION PHASE UNDER 25KG") ? getResources().getString(R.string.ctb_adult_formulation_rh_20_to_25) :
                                                getResources().getString(R.string.ctb_adult_formulation_continuation_rh)
                                        )));

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
                thirdDateCalendar.setTime(App.stringToDate(forthDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
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
        if (view == regDate.getButton()) {
            regDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
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
        if (spinner == extraPulmonarySite.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                extraPulmonarySiteOther.setVisibility(View.VISIBLE);
            } else {
                extraPulmonarySiteOther.setVisibility(View.GONE);
            }
        }
        else if (spinner == reasonTreatmentNotIniated.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                otherReasonNotInitiated.setVisibility(View.VISIBLE);
            } else {
                otherReasonNotInitiated.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_patient_loss_to_followup)) || parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.patient_died))) {
                Toast.makeText(context, "Please fill End of Followup Form", Toast.LENGTH_SHORT).show();
                otherReasonNotInitiated.setVisibility(View.VISIBLE);
            }
        }

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
        boolean flag=true;
        for (CheckBox cb : typeOfDiagnosis.getCheckedBoxes()) {
            if (App.get(cb).equals(getResources().getString(R.string.ctb_clinically_diagnosed))) {
                if (cb.isChecked()) {
                    histopathologicalEvidence.setVisibility(View.VISIBLE);
                    radiologicalEvidence.setVisibility(View.VISIBLE);
                } else {
                    histopathologicalEvidence.setVisibility(View.GONE);
                    radiologicalEvidence.setVisibility(View.GONE);
                }
            }
            typeOfDiagnosis.getQuestionView().setError(null);
        }

        for (CheckBox cb : tbType.getCheckedBoxes()) {
            if (App.get(cb).equals(getResources().getString(R.string.ctb_extra_pulmonary))) {
                if (cb.isChecked()) {
                    extraPulmonarySite.setVisibility(View.VISIBLE);
                    if (extraPulmonarySite.getSpinner().getSelectedItem().equals(getResources().getString(R.string.ctb_other_title))) {
                        extraPulmonarySiteOther.setVisibility(View.VISIBLE);
                    } else {
                        extraPulmonarySiteOther.setVisibility(View.GONE);
                    }
                } else {
                    extraPulmonarySite.setVisibility(View.GONE);
                    extraPulmonarySiteOther.setVisibility(View.GONE);
                }
            }
            tbType.getQuestionView().setError(null);
        }

        for (CheckBox cb : testConfirmingDiagnosis.getCheckedBoxes()) {
            if (App.get(cb).equals(getResources().getString(R.string.ctb_other_title))) {
                if (cb.isChecked()) {
                    testConfirmingOthers.setVisibility(View.VISIBLE);
                } else {
                    testConfirmingOthers.setVisibility(View.GONE);
                }
            }
            testConfirmingDiagnosis.getQuestionView().setError(null);
        }

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

        returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
        nameConsultant.setVisibility(View.GONE);
        reasonConsultation.setVisibility(View.GONE);
        tbDaignosed.setVisibility(View.GONE);
        radiologicalEvidence.setVisibility(View.GONE);
        histopathologicalEvidence.setVisibility(View.GONE);
        extraPulmonarySite.setVisibility(View.GONE);
        extraPulmonarySiteOther.setVisibility(View.GONE);
        testConfirmingOthers.setVisibility(View.GONE);
        reasonTreatmentNotIniated.setVisibility(View.GONE);
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
        otherPatientType.setVisibility(View.GONE);
        otherReasonNotInitiated.setVisibility(View.GONE);
        returnVisitDate.setVisibility(View.GONE);
        moInstruction.setVisibility(View.GONE);
        patientReferred.setVisibility(View.GONE);
        referredTo.setVisibility(View.GONE);
        referalReasonPsychologist.setVisibility(View.GONE);
        otherReferalReasonPsychologist.setVisibility(View.GONE);
        referalReasonSupervisor.setVisibility(View.GONE);
        otherReferalReasonSupervisor.setVisibility(View.GONE);
        referalReasonCallCenter.setVisibility(View.GONE);
        otherReferalReasonCallCenter.setVisibility(View.GONE);
        referalReasonClinician.setVisibility(View.GONE);
        otherReferalReasonClinician.setVisibility(View.GONE);
        patientCategory.setVisibility(View.GONE);
//        followupRequired.setVisibility(View.GONE);
        updateDisplay();

        String weightString = serverService.getLatestObsValue(App.getPatientId(), Forms.CLINICIAN_EVALUATION_FORM, "WEIGHT (KG)");
        String weightPercentileString = serverService.getLatestObsValue(App.getPatientId(), Forms.CLINICIAN_EVALUATION_FORM, "WEIGHT PERCENTILE GROUP");
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
        if (group == treatmentInitiated.getRadioGroup()) {
            treatmentInitiated.getQuestionView().setError(null);
            if (treatmentInitiated.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.no))) {
                reasonTreatmentNotIniated.setVisibility(View.VISIBLE);
                treatmentPlan.setVisibility(View.GONE);
                if(App.get(reasonTreatmentNotIniated).equals(getString(R.string.ctb_other_title))){
                    otherReasonNotInitiated.setVisibility(View.VISIBLE);
                }

                followupRequired.setVisibility(View.GONE);
                initiatingAdditionalTreatment.setVisibility(View.GONE);
                otherAdditionalTreatment.setVisibility(View.GONE);
                initiatingAdditionalTreatment.setVisibility(View.GONE);
                weight.setVisibility(View.GONE );
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
            else if(treatmentInitiated.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))){
                initiatingAdditionalTreatment.setVisibility(View.VISIBLE);
                weight.setVisibility(View.VISIBLE);
                treatmentPlan.setVisibility(View.VISIBLE);
                followupRequired.setVisibility(View.VISIBLE);
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
                }

                reasonTreatmentNotIniated.setVisibility(View.GONE);
                otherReasonNotInitiated.setVisibility(View.GONE);

            }
        }
        else if (group == patientReferred.getRadioGroup()) {
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

        else if (group == infectionType.getRadioGroup()) {
            infectionType.getQuestionView().setError(null);
            if (infectionType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.DRTB))) {
                treatmentInitiated.getRadioGroup().getButtons().get(1).setChecked(true);
                treatmentInitiated.setRadioGroupEnabled(false);
                reasonTreatmentNotIniated.setVisibility(View.VISIBLE);
                if(App.get(reasonTreatmentNotIniated).equals(getString(R.string.ctb_other_title))){
                    otherReasonNotInitiated.setVisibility(View.VISIBLE);
                }

                Toast.makeText(context, "Please fill Referral Transfer Form", Toast.LENGTH_SHORT).show();

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

            }else{
                treatmentInitiated.setRadioGroupEnabled(true);
                reasonTreatmentNotIniated.setVisibility(View.GONE);
                otherReasonNotInitiated.setVisibility(View.GONE);
            }

            if (infectionType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.DSTB))) {
                patientReferred.setVisibility(View.VISIBLE);
                setReferralViews();
                patientCategory.setVisibility(View.VISIBLE);
                regDate.setVisibility(View.VISIBLE);
                tbRegisterationNumber.setVisibility(View.VISIBLE);
            }else{
                referredTo.setVisibility(View.GONE);
                referalReasonPsychologist.setVisibility(View.GONE);
                otherReferalReasonPsychologist.setVisibility(View.GONE);
                referalReasonSupervisor.setVisibility(View.GONE);
                otherReferalReasonSupervisor.setVisibility(View.GONE);
                referalReasonCallCenter.setVisibility(View.GONE);
                otherReferalReasonCallCenter.setVisibility(View.GONE);
                referalReasonClinician.setVisibility(View.GONE);
                otherReferalReasonClinician.setVisibility(View.GONE);
                patientReferred.setVisibility(View.GONE);
                patientCategory.setVisibility(View.GONE);
                regDate.setVisibility(View.GONE);
                tbRegisterationNumber.setVisibility(View.GONE);
            }
        }



        else if (group == followupRequired.getRadioGroup()) {
            followupRequired.getQuestionView().setError(null);
            if (followupRequired.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                returnVisitDate.setVisibility(View.VISIBLE);
                moInstruction.setVisibility(View.VISIBLE);
            } else {
                returnVisitDate.setVisibility(View.GONE);
                moInstruction.setVisibility(View.GONE);
            }
        }

        else if (group == moConsultPediatrician.getRadioGroup()) {
            moConsultPediatrician.getQuestionView().setError(null);
            if (moConsultPediatrician.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                nameConsultant.setVisibility(View.VISIBLE);
                reasonConsultation.setVisibility(View.VISIBLE);
                tbDaignosed.setVisibility(View.VISIBLE);
            } else {
                nameConsultant.setVisibility(View.GONE);
                reasonConsultation.setVisibility(View.GONE);
                tbDaignosed.setVisibility(View.GONE);
            }
        }
        else if (group == tbDaignosed.getRadioGroup()) {
            tbDaignosed.getQuestionView().setError(null);
        }
        else if(group == treatmentPlan.getRadioGroup()){
            treatmentPlan.getQuestionView().setError(null);
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
            }
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
