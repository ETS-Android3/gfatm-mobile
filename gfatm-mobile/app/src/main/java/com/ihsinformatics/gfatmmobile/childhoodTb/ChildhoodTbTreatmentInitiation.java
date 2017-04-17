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
import android.service.voice.VoiceInteractionService;
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

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyCheckBox;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import org.openmrs.api.impl.VisitServiceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbTreatmentInitiation extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    public static final int THIRD_DATE_DIALOG_ID = 3;
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;

    public static final int FORTH_DATE_DIALOG_ID = 4;
    protected  Calendar forthDateCalender;
    protected DialogFragment forthDateFragment;

    Snackbar snackbar;
    ScrollView scrollView;
    TitledButton formDate;
    TitledEditText weightAtBaseline;
    TitledRadioGroup patientHaveTb;
    TitledButton regDate;
    LinearLayout cnicLinearLayout;
    TitledEditText cnic1;
    TitledEditText cnic2;
    TitledEditText cnic3;
    TitledSpinner cnicOwner;
    TitledEditText cnicOwnerOther;


    //TB TREATMENT INITATION
    TitledEditText tbRegisterationNumber;
    TitledRadioGroup tbType;
    TitledSpinner extraPulmonarySite;
    TitledEditText extraPulmonarySiteOther;
    TitledSpinner patientType;
    TitledRadioGroup treatmentInitiated;
    TitledSpinner reasonTreatmentNotIniated;
    TitledCheckBoxes initiatingAdditionalTreatment;
    TitledRadioGroup patientCategory;
    TitledEditText weight;
    TitledRadioGroup regimen;
    TitledSpinner typeFixedDosePrescribed;
    TitledRadioGroup currentTabletsofRHZ;
    TitledRadioGroup currentTabletsofE;
    TitledRadioGroup newTabletsofRHZ;
    TitledRadioGroup newTabletsofE;
    TitledRadioGroup adultFormulationofHRZE;
    TitledCheckBoxes typeOfDiagnosis;
    TitledRadioGroup histopathologicalEvidence;
    TitledRadioGroup radiologicalEvidence;
    TitledEditText nameOfSupporter;
    LinearLayout mobileLinearLayout;
    TitledEditText mobileNumber1;
    TitledEditText mobileNumber2;
    TitledSpinner closeContactTypeTreatmentSupport;
    TitledEditText otherCloseContactSupporterType;


    //IPT INTIATION
    TitledRadioGroup bcgScar;
    TitledRadioGroup tbHistoryIn2Years;
    TitledRadioGroup eligibleForIpt;
    TitledButton iptStartDate;
    TitledRadioGroup iptDose;
    TitledCheckBoxes initiatingAdditionalTreatmentIpt;


    //ANTIBIOTIC TRIAL
    TitledRadioGroup precribingAntibioticTrial;
    TitledRadioGroup precribingFurthertest;
    TitledCheckBoxes initiatingAdditionalTreatmentAntibiotic;

    //Next Appointment
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
        FORM_NAME = Forms.CHILDHOODTB_TREATMENT_INITIATION;
        FORM = Forms.childhoodTb_treatment_intiation;

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

        forthDateCalender = Calendar.getInstance();
        forthDateFragment = new SelectDateFragment();
        // first page views...
        formDate = new TitledButton(context, "Section A: TB Treatment Initiation ", getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        weightAtBaseline = new TitledEditText(context, null, getResources().getString(R.string.ctb_weight_at_baseline), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        patientHaveTb = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_patient_have_tb), getResources().getStringArray(R.array.ctb_patient_have_tb_list),null, App.HORIZONTAL, App.VERTICAL);
        regDate = new TitledButton(context, null, getResources().getString(R.string.ctb_registration_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        cnicLinearLayout = new LinearLayout(context);
        cnicLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        cnic1 = new TitledEditText(context, null, getResources().getString(R.string.ctb_cnic_number), "", "#####", 5, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        cnicLinearLayout.addView(cnic1);
        cnic2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLinearLayout.addView(cnic2);
        cnic3 = new TitledEditText(context, null, "-", "", "#", 1, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLinearLayout.addView(cnic3);
        cnicOwner = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_cnic_owner), getResources().getStringArray(R.array.ctb_close_contact_type_list), null, App.VERTICAL);
        cnicOwnerOther = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        tbRegisterationNumber = new TitledEditText(context, null, getResources().getString(R.string.ctb_tb_registration_no), "", "", 11, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        tbType = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_type_of_tb_name), getResources().getStringArray(R.array.ctb_extra_pulomonary_and_pulmonary), getResources().getString(R.string.ctb_pulmonary), App.HORIZONTAL, App.VERTICAL,true);
        extraPulmonarySite = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_site_of_extra_pulmonary), getResources().getStringArray(R.array.ctb_extra_pulmonary_tb_site), getResources().getString(R.string.ctb_lymph_node), App.VERTICAL,true);
        extraPulmonarySiteOther = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_extra_pulmonary_site), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        patientType = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_patient_type), getResources().getStringArray(R.array.ctb_patient_type_list), getResources().getString(R.string.ctb_new), App.VERTICAL,true);
        treatmentInitiated = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_are_you_intiating_treatment), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL,true);
        reasonTreatmentNotIniated = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_reason_treatment_not_intiated), getResources().getStringArray(R.array.ctb_reason_treatment_not_intiated_list), getResources().getString(R.string.ctb_patient_refused_treatment), App.VERTICAL,true);
        initiatingAdditionalTreatment = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_initiating_additional_treatment), getResources().getStringArray(R.array.ctb_pediasure_vitamin_iron_anthelminthic), null, App.VERTICAL, App.VERTICAL);
        patientCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_patient_category), getResources().getStringArray(R.array.ctb_patient_category3_list), getResources().getString(R.string.ctb_categoryI), App.VERTICAL, App.VERTICAL);
        weight = new TitledEditText(context, null, getResources().getString(R.string.ctb_patient_weight), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        regimen = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_regimen), getResources().getStringArray(R.array.ctb_regimen_list), getResources().getString(R.string.ctb_rhz), App.HORIZONTAL, App.VERTICAL);
        typeFixedDosePrescribed = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_type_of_fixed_dose), getResources().getStringArray(R.array.ctb_type_of_fixed_dose_list), null, App.VERTICAL);
        currentTabletsofRHZ = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_current_formulation_number_of_tablet_rhz), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL);
        currentTabletsofE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_current_formulation_number_of_tablet_e), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL);
        newTabletsofRHZ = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_new_formulation_number_of_tablet_rhz), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL);
        newTabletsofE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_new_formulation_number_of_tablet_e), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL);
        adultFormulationofHRZE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_adult_formulation), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL);
        typeOfDiagnosis = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_type_of_diagnosis), getResources().getStringArray(R.array.ctb_type_of_diagnosis_list), null, App.VERTICAL, App.VERTICAL);

        histopathologicalEvidence = new TitledRadioGroup(context, "If clinically diagnosed, specify evidence", getResources().getString(R.string.ctb_histopathologiacal_evidence), getResources().getStringArray(R.array.yes_no_options), null, App.VERTICAL, App.VERTICAL);
        radiologicalEvidence = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_radiological_evidence), getResources().getStringArray(R.array.yes_no_options), null, App.VERTICAL, App.VERTICAL);
        nameOfSupporter = new TitledEditText(context, null, getResources().getString(R.string.ctb_treatment_supporter), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL,false);
        mobileLinearLayout = new LinearLayout(context);
        mobileLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mobileNumber1 = new TitledEditText(context, null, getResources().getString(R.string.ctb_treatment_supporter_phone_number), "", "####", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        mobileLinearLayout.addView(mobileNumber1);
        mobileNumber2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        mobileLinearLayout.addView(mobileNumber2);
        closeContactTypeTreatmentSupport = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_close_contact_supporter_with_patient), getResources().getStringArray(R.array.ctb_close_contact_type_list), null, App.VERTICAL);
        otherCloseContactSupporterType = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL,false);

        // SECTION B : IPT INITIATION

        bcgScar = new TitledRadioGroup(context, "Section B: IPT Initiation", getResources().getString(R.string.ctb_bcg_scar), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL,true);
        tbHistoryIn2Years = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_history_2years), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL);
        eligibleForIpt = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_eligible_for_ipt), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL,true);
        iptStartDate = new TitledButton(context, null, getResources().getString(R.string.ctb_ipt_start_date), DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);
        iptDose = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ipt_dose), getResources().getStringArray(R.array.ctb_dose_list), null, App.VERTICAL, App.VERTICAL);
        initiatingAdditionalTreatmentIpt = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_initiating_additional_treatment), getResources().getStringArray(R.array.ctb_iron_multivitamins_anthelmintic), null, App.VERTICAL, App.VERTICAL);

        // SECTION C : ANTIBIOTIC INITIATION

        precribingAntibioticTrial = new TitledRadioGroup(context, "Section C: Inconclusive TB Patient: Antibiotic Trial", getResources().getString(R.string.ctb_prescribing_antibiotic_trial), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL);
        precribingFurthertest = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_prescribing_further_tests), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL);
        eligibleForIpt = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_eligible_for_ipt), getResources().getStringArray(R.array.yes_no_options), null, App.VERTICAL, App.VERTICAL,true);
        initiatingAdditionalTreatmentAntibiotic= new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_initiating_additional_treatment), getResources().getStringArray(R.array.ctb_iron_multivitamins_anthelmintic_other_none), null, App.VERTICAL, App.VERTICAL);

        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.ctb_next_appointment_date), DateFormat.format("dd-MMM-yyyy", forthDateCalender).toString(), App.HORIZONTAL);
        doctorNotes = new TitledEditText(context, null, getResources().getString(R.string.ctb_doctor_notes), "", "", 1000, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL,false);

        thirdDateCalendar.set(Calendar.YEAR, secondDateCalendar.get(Calendar.YEAR));
        thirdDateCalendar.set(Calendar.DAY_OF_MONTH, secondDateCalendar.get(Calendar.DAY_OF_MONTH));
        thirdDateCalendar.set(Calendar.MONTH, secondDateCalendar.get(Calendar.MONTH));
        thirdDateCalendar.add(Calendar.DAY_OF_MONTH, 30);

        views = new View[]{formDate.getButton(),weightAtBaseline.getEditText(),patientHaveTb.getRadioGroup(),regDate.getButton(),cnicLinearLayout,cnic1.getEditText(),cnic2.getEditText(),cnic3.getEditText(),
                cnicOwner.getSpinner(),cnicOwnerOther.getEditText(),tbRegisterationNumber.getEditText(),tbType.getRadioGroup(),
                extraPulmonarySite.getSpinner(),extraPulmonarySiteOther.getEditText(),patientType.getSpinner(),treatmentInitiated.getRadioGroup(),
                reasonTreatmentNotIniated.getSpinner(),initiatingAdditionalTreatment,patientCategory.getRadioGroup(),
                weight.getEditText(),regimen.getRadioGroup(),typeFixedDosePrescribed.getSpinner(),currentTabletsofRHZ.getRadioGroup(),currentTabletsofE.getRadioGroup(),
                newTabletsofRHZ.getRadioGroup(),newTabletsofE.getRadioGroup(),adultFormulationofHRZE.getRadioGroup(),typeOfDiagnosis,histopathologicalEvidence.getRadioGroup(),
                radiologicalEvidence.getRadioGroup(),nameOfSupporter.getEditText(),mobileLinearLayout,mobileNumber1.getEditText(),mobileNumber2.getEditText(),
                closeContactTypeTreatmentSupport.getSpinner(),otherCloseContactSupporterType.getEditText(),bcgScar.getRadioGroup(),
                tbHistoryIn2Years.getRadioGroup(),eligibleForIpt.getRadioGroup(),iptStartDate.getButton(),
                iptDose.getRadioGroup(),initiatingAdditionalTreatmentIpt,precribingAntibioticTrial.getRadioGroup(),
                precribingFurthertest.getRadioGroup(),initiatingAdditionalTreatmentAntibiotic,returnVisitDate.getButton(),
                doctorNotes.getEditText()};
        viewGroups = new View[][]
                {{
                        formDate,
                        weightAtBaseline,
                        patientHaveTb,
                        regDate,
                        cnicLinearLayout,
                        cnicOwner,
                        cnicOwnerOther,
                        tbRegisterationNumber,
                        tbType,
                        extraPulmonarySite,
                        extraPulmonarySiteOther,
                        patientType,
                        treatmentInitiated,
                        reasonTreatmentNotIniated,
                        initiatingAdditionalTreatment,
                        patientCategory,
                        weight,
                        regimen,
                        typeFixedDosePrescribed,
                        currentTabletsofRHZ,
                        currentTabletsofE,
                        newTabletsofRHZ,
                        newTabletsofE,
                        adultFormulationofHRZE,
                        typeOfDiagnosis,
                        histopathologicalEvidence,
                        radiologicalEvidence,
                        nameOfSupporter,
                        mobileLinearLayout,
                        closeContactTypeTreatmentSupport,
                        otherCloseContactSupporterType,
                        bcgScar,
                        tbHistoryIn2Years,
                        eligibleForIpt,
                        iptStartDate,
                        iptDose,
                        initiatingAdditionalTreatmentIpt,
                        precribingAntibioticTrial,
                        precribingFurthertest,
                        initiatingAdditionalTreatmentAntibiotic,
                        returnVisitDate,
                        doctorNotes,
                }};
        formDate.getButton().setOnClickListener(this);
        patientHaveTb.getRadioGroup().setOnCheckedChangeListener(this);
        regDate.getButton().setOnClickListener(this);
        cnicOwner.getSpinner().setOnItemSelectedListener(this);
        tbType.getRadioGroup().setOnCheckedChangeListener(this);
        extraPulmonarySite.getSpinner().setOnItemSelectedListener(this);
        patientType.getSpinner().setOnItemSelectedListener(this);
        treatmentInitiated.getRadioGroup().setOnCheckedChangeListener(this);
        reasonTreatmentNotIniated.getSpinner().setOnItemSelectedListener(this);
        ArrayList<MyCheckBox> checkBoxList = initiatingAdditionalTreatment.getCheckedBoxes();
        for (CheckBox cb : initiatingAdditionalTreatment.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        patientCategory.getRadioGroup().setOnCheckedChangeListener(this);
        regimen.getRadioGroup().setOnCheckedChangeListener(this);
        typeFixedDosePrescribed.getSpinner().setOnItemSelectedListener(this);
        currentTabletsofRHZ.getRadioGroup().setOnCheckedChangeListener(this);
        currentTabletsofE.getRadioGroup().setOnCheckedChangeListener(this);
        newTabletsofRHZ.getRadioGroup().setOnCheckedChangeListener(this);
        newTabletsofE.getRadioGroup().setOnCheckedChangeListener(this);
        adultFormulationofHRZE.getRadioGroup().setOnCheckedChangeListener(this);
        ArrayList<MyCheckBox> checkBoxList2 = typeOfDiagnosis.getCheckedBoxes();
        for (CheckBox cb : typeOfDiagnosis.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        histopathologicalEvidence.getRadioGroup().setOnCheckedChangeListener(this);
        radiologicalEvidence.getRadioGroup().setOnCheckedChangeListener(this);
        closeContactTypeTreatmentSupport.getSpinner().setOnItemSelectedListener(this);
        bcgScar.getRadioGroup().setOnCheckedChangeListener(this);
        tbHistoryIn2Years.getRadioGroup().setOnCheckedChangeListener(this);
        eligibleForIpt.getRadioGroup().setOnCheckedChangeListener(this);
        iptStartDate.getButton().setOnClickListener(this);
        iptDose.getRadioGroup().setOnCheckedChangeListener(this);
        ArrayList<MyCheckBox> checkBoxList3 = initiatingAdditionalTreatmentIpt.getCheckedBoxes();
        for (CheckBox cb : initiatingAdditionalTreatmentIpt.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        precribingAntibioticTrial.getRadioGroup().setOnCheckedChangeListener(this);
        precribingFurthertest.getRadioGroup().setOnCheckedChangeListener(this);
        ArrayList<MyCheckBox> checkBoxList4 = initiatingAdditionalTreatmentAntibiotic.getCheckedBoxes();
        for (CheckBox cb : initiatingAdditionalTreatmentAntibiotic.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        returnVisitDate.getButton().setOnClickListener(this);

        cnic1.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cnicString = s + "-" + App.get(cnic2) + "-" + App.get(cnic3);
                if(RegexUtil.isValidNIC(cnicString) && cnicLinearLayout.getVisibility()==View.VISIBLE){
                    cnicOwner.setVisibility(View.VISIBLE);
                }else{
                    cnicOwner.setVisibility(View.GONE);
                    cnicOwnerOther.setVisibility(View.GONE);
                }
            }
        });

        cnic2.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cnicString = App.get(cnic1) + "-" + s + "-" + App.get(cnic3);
                if(RegexUtil.isValidNIC(cnicString) && cnicLinearLayout.getVisibility()==View.VISIBLE){
                    cnicOwner.setVisibility(View.VISIBLE);
                }else{
                    cnicOwner.setVisibility(View.GONE);
                    cnicOwnerOther.setVisibility(View.GONE);
                }
            }
        });

        cnic3.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cnicString = App.get(cnic1) + "-" + App.get(cnic2) + "-" + s;
                if(RegexUtil.isValidNIC(cnicString) && cnicLinearLayout.getVisibility()==View.VISIBLE){
                    cnicOwner.setVisibility(View.VISIBLE);
                }else{
                    cnicOwner.setVisibility(View.GONE);
                    cnicOwnerOther.setVisibility(View.GONE);
                }
            }
        });


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
                    int weightString = Integer.parseInt(s.toString());
                    if(weightString>25){
                        typeFixedDosePrescribed.getSpinner().selectValue(getResources().getString(R.string.ctb_adult_formulation));
                    }
                }
            }
        });

        nameOfSupporter.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0){
                    mobileLinearLayout.setVisibility(View.GONE);
                }else{
                    mobileLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        resetViews();

    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();
        Date date = new Date();
        if (!(formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();

            String personDOB = App.getPatient().getPerson().getBirthdate();


            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            }  else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.ctb_form_date_less_than_patient_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            }else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        } if (!regDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString())) {

            //
            // +Date date = App.stringToDate(sampleSubmitDate.getButton().getText().toString(), "dd-MMM-yyyy");

            if (secondDateCalendar.after(date)) {

                secondDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                regDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        }
        if (!iptStartDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString())) {
            if (thirdDateCalendar.after(date)) {

                thirdDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                iptStartDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
        }
        if (!returnVisitDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", forthDateCalender).toString())) {
            if (forthDateCalender.after(date)) {

                forthDateCalender = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", forthDateCalender).toString());
        }
    }

    @Override
    public boolean validate() {
        Boolean error = false;
        if(App.get(patientHaveTb).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            patientHaveTb.getRadioGroup().getButtons().get(2).setError(getString(R.string.empty_field));
            patientHaveTb.getRadioGroup().requestFocus();
            error = true;
        }
        if(cnicLinearLayout.getVisibility()==View.VISIBLE) {
            if (App.get(cnic1).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cnic1.getEditText().setError(getString(R.string.empty_field));
                cnic1.getEditText().requestFocus();
                error = true;
            }

            if (App.get(cnic2).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cnic2.getEditText().setError(getString(R.string.empty_field));
                cnic2.getEditText().requestFocus();
                error = true;
            }

            if (App.get(cnic3).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cnic3.getEditText().setError(getString(R.string.empty_field));
                cnic3.getEditText().requestFocus();
                error = true;
            }

            if (App.get(cnic1).length() != 5) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cnic1.getEditText().setError(getString(R.string.length_message));
                cnic1.getEditText().requestFocus();
                error = true;
            }

            if (App.get(cnic2).length() != 7) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cnic2.getEditText().setError(getString(R.string.length_message));
                cnic2.getEditText().requestFocus();
                error = true;
            }

            if (App.get(cnic3).length() != 1) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cnic3.getEditText().setError(getString(R.string.length_message));
                cnic3.getEditText().requestFocus();
                error = true;
            }

        }
        if (cnicOwnerOther.getVisibility() == View.VISIBLE && App.get(cnicOwnerOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnicOwnerOther.getEditText().setError(getString(R.string.empty_field));
            cnicOwnerOther.getEditText().requestFocus();
            error = true;
        }


        if (tbRegisterationNumber.getVisibility() == View.VISIBLE && App.get(tbRegisterationNumber).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            tbRegisterationNumber.getEditText().setError(getString(R.string.empty_field));
            tbRegisterationNumber.getEditText().requestFocus();
            error = true;
        }


        if (extraPulmonarySiteOther.getVisibility() == View.VISIBLE && App.get(extraPulmonarySiteOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            extraPulmonarySiteOther.getEditText().setError(getString(R.string.empty_field));
            extraPulmonarySiteOther.getEditText().requestFocus();
            error = true;
        }
        if (weight.getVisibility() == View.VISIBLE && App.get(weight).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            weight.getEditText().setError(getString(R.string.empty_field));
            weight.getEditText().requestFocus();
            error = true;
        }

        if(nameOfSupporter.getVisibility()==View.VISIBLE){
            if(App.get(nameOfSupporter).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                nameOfSupporter.getEditText().setError(getString(R.string.empty_field));
                nameOfSupporter.getEditText().requestFocus();
                error = true;
            }else if(App.get(nameOfSupporter).trim().length() <= 0){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                nameOfSupporter.getEditText().setError(getString(R.string.ctb_spaces_only));
                nameOfSupporter.getEditText().requestFocus();
                error = true;
            }
        }

        if(mobileLinearLayout.getVisibility()==View.VISIBLE){
            if(App.get(mobileNumber1).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                mobileNumber1.getEditText().setError(getString(R.string.empty_field));
                mobileNumber1.getEditText().requestFocus();
                error = true;
            }
            if(App.get(mobileNumber2).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                mobileNumber2.getEditText().setError(getString(R.string.empty_field));
                mobileNumber2.getEditText().requestFocus();
                error = true;
            }
            if(!App.get(mobileNumber1).isEmpty() && !App.get(mobileNumber2).isEmpty()){
                String mobileNumber = App.get(mobileNumber1) + App.get(mobileNumber2);
                if(!RegexUtil.isMobileNumber(mobileNumber)){
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    mobileNumber2.getEditText().setError(getString(R.string.ctb_invalid_number));
                    mobileNumber2.getEditText().requestFocus();
                    error = true;
                }
            }
            if(bcgScar.getVisibility()==View.VISIBLE && App.get(bcgScar).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                bcgScar.getRadioGroup().getButtons().get(3).setError(getString(R.string.empty_field));
                bcgScar.getRadioGroup().requestFocus();
                error = true;
            }
            if(eligibleForIpt.getVisibility()==View.VISIBLE && App.get(eligibleForIpt).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                eligibleForIpt.getRadioGroup().getButtons().get(1).setError(getString(R.string.empty_field));
                eligibleForIpt.getRadioGroup().requestFocus();
                error = true;
            }
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
        observations.add(new String[]{"PATIENT HAVE TB", App.get(patientHaveTb).toUpperCase()});

        if(regDate.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"REGISTRATION DATE", App.getSqlDateTime(secondDateCalendar)});
        }
        if(cnicLinearLayout.getVisibility()==View.VISIBLE){
            String cnicNumber = App.get(cnic1) +"-"+ App.get(cnic2) +"-"+ App.get(cnic3);
            observations.add(new String[]{"NATIONAL IDENTIFICATION NUMBER", cnicNumber});
        }
        if (cnicOwner.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COMPUTERIZED NATIONAL IDENTIFICATION OWNER", (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_mother)) ? "MOTHER" :
                            (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_father)) ? "FATHER" :
                                    (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_sister)) ? "SISTER" :
                                            (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_brother)) ? "BROTHER" :
                                                    (       (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_uncle)) ? "UNCLE" :
                                                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_aunt)) ? "AUNT" :
                                                                                                             "OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER")))))))))))});

        if(cnicOwnerOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER", App.get(cnicOwnerOther)});

        if(tbRegisterationNumber.getVisibility()==View.VISIBLE)
            observations.add(new String[]{"TB REGISTRATION NUMBER", App.get(tbRegisterationNumber)});
        if(tbType.getVisibility()==View.VISIBLE)
            observations.add(new String[]{"SITE OF TUBERCULOSIS DISEASE", App.get(tbType).equals(getResources().getString(R.string.ctb_extra_pulmonary)) ? "EXTRA-PULMONARY TUBERCULOSIS" :
                            "PULMONARY TUBERCULOSIS"});
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
                            (App.get(patientType).equals(getResources().getString(R.string.ctb_referred_transferred)) ? "PATIENT REFERRED" :
                                    (App.get(patientType).equals(getResources().getString(R.string.ctb_treatment_after_followup)) ? "LOST TO FOLLOW-UP" :
                                            (App.get(patientType).equals(getResources().getString(R.string.ctb_treatment_failure)) ? "TUBERCULOSIS TREATMENT FAILURE" : "OTHER PATIENT TYPE"))))});

        if (treatmentInitiated.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT INITIATED", App.get(treatmentInitiated).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (reasonTreatmentNotIniated.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT NOT STARTED", App.get(reasonTreatmentNotIniated).equals(getResources().getString(R.string.ctb_patient_refused_treatment)) ? "REFUSAL OF TREATMENT BY PATIENT" :
                    (App.get(reasonTreatmentNotIniated).equals(getResources().getString(R.string.ctb_patient_loss_to_followup)) ? "LOST TO FOLLOW-UP" :
                            (App.get(reasonTreatmentNotIniated).equals(getResources().getString(R.string.ctb_patient_died)) ? "DECEASED" :
                                    (App.get(reasonTreatmentNotIniated).equals(getResources().getString(R.string.ctb_referral_before_starting_treatment)) ? "PATIENT REFERRED" : "TREATMENT NOT INITIATED OTHER REASON")))});

        if(initiatingAdditionalTreatment.getVisibility()==View.VISIBLE){
            String additionalTreatmentString = "";
            for (CheckBox cb : initiatingAdditionalTreatment.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_pediasure)))
                    additionalTreatmentString = additionalTreatmentString + "PEDIASURE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_iron)))
                    additionalTreatmentString = additionalTreatmentString + "IRON" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_vitamin_B_complex)))
                    additionalTreatmentString = additionalTreatmentString + "VITAMIN B COMPLEX" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_anthelminthic)))
                    additionalTreatmentString = additionalTreatmentString + "ANTHELMINTHIC" + " ; ";
            }
            observations.add(new String[]{"ADDITIONAL TREATMENT TO TB PATIENT", additionalTreatmentString});
        }


        if (patientCategory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TB CATEGORY", App.get(patientCategory).equals(getResources().getString(R.string.ctb_categoryI)) ? "CATEGORY I TUBERCULOSIS" :
                    App.get(patientCategory).equals(getResources().getString(R.string.ctb_categoryII)) ? "CATEGORY II TUBERCULOSIS"
                            : "CATEGORY III TUBERCULOSIS"});

        if(weight.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"WEIGHT (KG)", App.get(weight)});
        }

        if(regimen.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"REGIMEN", App.get(regimen).equals(getResources().getString(R.string.ctb_rhze)) ? "RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS" : "RIFAMPICIN/ISONIAZID/PYRAZINAMIDE"});
        }

        if(typeFixedDosePrescribed.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"PAEDIATRIC DOSE COMBINATION", App.get(typeFixedDosePrescribed).equals(getResources().getString(R.string.ctb_current_formulation)) ? "CURRENT FORULATION" :
                    App.get(typeFixedDosePrescribed).equals(getResources().getString(R.string.ctb_new_formulation)) ? "NEW FORMULATION"
                            : "ADULT FORMULATION"});
        }
        if(currentTabletsofRHZ.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"CURRENT FORMULATION OF TABLETS OF RHZ", App.get(currentTabletsofRHZ)});
        }
        if(currentTabletsofE.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"CURRENT FORMULATION OF TABLETS OF E", App.get(currentTabletsofE)});
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

        if(nameOfSupporter.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"NAME OF TREATMENT SUPPORTER", App.get(nameOfSupporter)});
        }
        if(mobileLinearLayout.getVisibility()==View.VISIBLE){
            String mobileNumber = App.get(mobileNumber1) + "-" + App.get(mobileNumber2);
            observations.add(new String[]{"TREATMENT SUPPORTER CONTACT NUMBER", mobileNumber});
        }

        if(closeContactTypeTreatmentSupport.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"TREATMENT SUPPORTER RELATIONSHIP TO PATIENT", App.get(closeContactTypeTreatmentSupport).equals(getResources().getString(R.string.ctb_mother)) ? "MOTHER" :
                    (App.get(closeContactTypeTreatmentSupport).equals(getResources().getString(R.string.ctb_father)) ? "FATHER" :
                            (App.get(closeContactTypeTreatmentSupport).equals(getResources().getString(R.string.ctb_brother)) ? "BROTHER" :
                                    (App.get(closeContactTypeTreatmentSupport).equals(getResources().getString(R.string.ctb_sister)) ? "SISTER" :
                                            (App.get(closeContactTypeTreatmentSupport).equals(getResources().getString(R.string.ctb_paternal_grandfather)) ? "PATERNAL GRANDFATHER":
                                                    (App.get(closeContactTypeTreatmentSupport).equals(getResources().getString(R.string.ctb_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                            (App.get(closeContactTypeTreatmentSupport).equals(getResources().getString(R.string.ctb_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                                    (App.get(closeContactTypeTreatmentSupport).equals(getResources().getString(R.string.ctb_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                                                            (App.get(closeContactTypeTreatmentSupport).equals(getResources().getString(R.string.ctb_uncle)) ? "UNCLE":
                                                                                    (App.get(closeContactTypeTreatmentSupport).equals(getResources().getString(R.string.ctb_aunt)) ? "AUNT" : "OTHER FAMILY MEMBER")))))))))});
        }

        if(otherCloseContactSupporterType.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"OTHER FAMILY MEMBER", App.get(otherCloseContactSupporterType)});
        }

        if(bcgScar.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"BACILLUS CALMETTE–GUÉRIN VACCINE", App.get(bcgScar).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(bcgScar).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(bcgScar).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        }

        if(tbHistoryIn2Years.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"PATIENT IS CONTACT OF KNOWN OR SUSPECTED SUSPICIOUS CASE IN PAST 2 YEARS", App.get(tbHistoryIn2Years).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(tbHistoryIn2Years).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(tbHistoryIn2Years).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        }

        if(eligibleForIpt.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"ELIGIBLE FOR IPT", App.get(eligibleForIpt).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        }
        if(iptStartDate.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"IPT START DATE", App.getSqlDateTime(thirdDateCalendar)});
        }
        if(iptDose.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"IPT DOSE", App.get(iptDose).equals(getResources().getString(R.string.ctb_quater_per_day)) ? "1/4 TAB ONCE ADAY" :
                    (App.get(iptDose).equals(getResources().getString(R.string.ctb_half_per_day)) ? "1/2 TAB ONCE A DAY" :
                            "1 TAB ONCE A DAY")});
        }

        if(initiatingAdditionalTreatmentIpt.getVisibility()==View.VISIBLE){
            String additionalTreatmentString = "";
            for (CheckBox cb : initiatingAdditionalTreatmentIpt.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_iron)))
                    additionalTreatmentString = additionalTreatmentString + "IRON" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_multivitamins)))
                    additionalTreatmentString = additionalTreatmentString + "MULTIVITAMIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_anthelmintic_albendazole)))
                    additionalTreatmentString = additionalTreatmentString + "ANTHELMINTHIC" + " ; ";
            }
            observations.add(new String[]{"ADDITIONAL TREATMENT IPT PATIENT", additionalTreatmentString});
        }

        if(precribingAntibioticTrial.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"ANTIBIOTIC GIVEN", App.get(precribingAntibioticTrial).toUpperCase()});
        }

        if(precribingFurthertest.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"PRESCRIBE FURTHER TESTS", App.get(precribingFurthertest).toUpperCase()});
        }
        if(initiatingAdditionalTreatmentAntibiotic.getVisibility()==View.VISIBLE){
            String additionalTreatmentString = "";
            for (CheckBox cb : initiatingAdditionalTreatmentAntibiotic.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_iron)))
                    additionalTreatmentString = additionalTreatmentString + "IRON" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_multivitamins)))
                    additionalTreatmentString = additionalTreatmentString + "MULTIVITAMIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_anthelminthic)))
                    additionalTreatmentString = additionalTreatmentString + "ANTHELMINTHIC" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_other_title)))
                    additionalTreatmentString = additionalTreatmentString + "OTHER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_none)))
                    additionalTreatmentString = additionalTreatmentString + "NONE" + " ; ";
            }
            observations.add(new String[]{"ADDITIONAL TREATMENT FOR INCONCLUSIVE PATIENT", additionalTreatmentString});
        }

        observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDateTime(forthDateCalender)});
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

                String result = serverService.saveEncounterAndObservation("Treatment Initiation", FORM, formDateCalendar, observations.toArray(new String[][]{}));
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
                    resetViews();

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
            } else if (obs[0][0].equals("PATIENT HAVE TB")) {
                for (RadioButton rb : patientHaveTb.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_inconclusive)) && obs[0][1].equals("INCONCLUSIVE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                patientHaveTb.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("REGISTRATION DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                regDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
                regDate.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("NATIONAL IDENTIFICATION NUMBER")) {
                String cnicString[] = obs[0][1].split("-");
                cnic1.getEditText().setText(cnicString[0]);
                cnic2.getEditText().setText(cnicString[1]);
                cnic3.getEditText().setText(cnicString[2]);
            } else if (obs[0][0].equals("COMPUTERIZED NATIONAL IDENTIFICATION OWNER")) {
                String value = (obs[0][1].equals("MOTHER") ? getResources().getString(R.string.ctb_mother) :
                                (obs[0][1].equals("FATHER") ? getResources().getString(R.string.ctb_father) :
                                        (obs[0][1].equals("SISTER") ? getResources().getString(R.string.ctb_sister) :
                                                (obs[0][1].equals("BROTHER") ? getResources().getString(R.string.ctb_brother) :
                                                        (obs[0][1].equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.ctb_paternal_grandfather) :
                                                                        (obs[0][1].equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.ctb_paternal_grandmother) :
                                                                                (obs[0][1].equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.ctb_maternal_grandfather) :
                                                                                        (obs[0][1].equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.ctb_maternal_grandmother) :
                                                                                                (obs[0][1].equals("UNCLE") ? getResources().getString(R.string.ctb_uncle) :
                                                                                                        (obs[0][1].equals("AUNT") ? getResources().getString(R.string.ctb_aunt) :
                                                                                                                                getResources().getString(R.string.ctb_other_title)))))))))));


                cnicOwner.getSpinner().selectValue(value);
                cnicOwner.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER")) {
                cnicOwnerOther.getEditText().setText(obs[0][1]);
                cnicOwnerOther.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("TB REGISTRATION NUMBER")) {
                tbRegisterationNumber.getEditText().setText(obs[0][1]);
                tbRegisterationNumber.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("SITE OF TUBERCULOSIS DISEASE")) {

                for (RadioButton rb : tbType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_pulmonary)) && obs[0][1].equals("PULMONARY TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_extra_pulmonary)) && obs[0][1].equals("EXTRA-PULMONARY TUBERCULOSIS")) {
                        rb.setChecked(true);
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
            }else if (obs[0][0].equals("TB PATIENT TYPE")) {
                String value = obs[0][1].equals("NEW TB PATIENT") ? getResources().getString(R.string.ctb_new) :
                        (obs[0][1].equals("RELAPSE") ? getResources().getString(R.string.ctb_relapse) :
                                (obs[0][1].equals("PATIENT REFERRED") ? getResources().getString(R.string.ctb_referred_transferred) :
                                        (obs[0][1].equals("LOST TO FOLLOW-UP") ? getResources().getString(R.string.ctb_treatment_after_followup) :
                                                (obs[0][1].equals("TUBERCULOSIS TREATMENT FAILURE") ? getResources().getString(R.string.ctb_treatment_failure) :
                                                        getResources().getString(R.string.ctb_other_title)))));

                patientType.getSpinner().selectValue(value);
                patientType.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("TREATMENT INITIATED")) {

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
            }else if(obs[0][0].equals("ADDITIONAL TREATMENT TO TB PATIENT")) {
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
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_anthelminthic)) && obs[0][1].equals("ANTHELMINTHIC")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                initiatingAdditionalTreatment.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TB CATEGORY")) {

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
            }else if (obs[0][0].equals("REGIMEN")) {
                for (RadioButton rb : regimen.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_rhze)) && obs[0][1].equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL PROPHYLAXIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_rhz)) && obs[0][1].equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                regimen.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("PAEDIATRIC DOSE COMBINATION")) {
                String value = obs[0][1].equals("CURRENT FORULATION") ? getResources().getString(R.string.ctb_current_formulation) :
                        (obs[0][1].equals("NEW FORMULATION") ? getResources().getString(R.string.ctb_new_formulation) :
                                getResources().getString(R.string.ctb_adult_formulation));

                typeFixedDosePrescribed.getSpinner().selectValue(value);
                typeFixedDosePrescribed.setVisibility(View.VISIBLE);
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
                adultFormulationofHRZE.setVisibility(View.VISIBLE);
            }else if(obs[0][0].equals("TUBERCULOSIS DIAGNOSIS METHOD")) {
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
            }else if (obs[0][0].equals("NAME OF TREATMENT SUPPORTER")) {
                nameOfSupporter.getEditText().setText(obs[0][1]);
                nameOfSupporter.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("TREATMENT SUPPORTER CONTACT NUMBER")) {
                String mobileNumber[] = obs[0][1].split("-");
                mobileNumber1.getEditText().setText(mobileNumber[0]);
                mobileNumber2.getEditText().setText(mobileNumber[1]);
            } else if (obs[0][0].equals("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT")) {
                String value = obs[0][1].equals("SELF") ? getResources().getString(R.string.ctb_self) :
                        (obs[0][1].equals("MOTHER") ? getResources().getString(R.string.ctb_mother) :
                                (obs[0][1].equals("FATHER") ? getResources().getString(R.string.ctb_father) :
                                        (obs[0][1].equals("SISTER") ? getResources().getString(R.string.ctb_sister) :
                                                (obs[0][1].equals("BROTHER") ? getResources().getString(R.string.ctb_brother) :
                                                        (obs[0][1].equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.ctb_paternal_grandfather) :
                                                                (obs[0][1].equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.ctb_paternal_grandmother) :
                                                                        (obs[0][1].equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.ctb_maternal_grandfather) :
                                                                                (obs[0][1].equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.ctb_maternal_grandmother) :
                                                                                        (obs[0][1].equals("UNCLE") ? getResources().getString(R.string.ctb_uncle) :
                                                                                                (obs[0][1].equals("AUNT") ? getResources().getString(R.string.ctb_aunt) :
                                                                                                        getResources().getString(R.string.ctb_other_title)))))))))));
                closeContactTypeTreatmentSupport.getSpinner().selectValue(value);
                closeContactTypeTreatmentSupport.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("OTHER FAMILY MEMBER")) {
                otherCloseContactSupporterType.getEditText().setText(obs[0][1]);
                otherCloseContactSupporterType.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("BACILLUS CALMETTE–GUÉRIN VACCINE")) {
                for (RadioButton rb : bcgScar.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                bcgScar.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("PATIENT IS CONTACT OF KNOWN OR SUSPECTED SUSPICIOUS CASE IN PAST 2 YEARS")) {
                for (RadioButton rb : tbHistoryIn2Years.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                tbHistoryIn2Years.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("ELIGIBLE FOR IPT")) {
                for (RadioButton rb : eligibleForIpt.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                eligibleForIpt.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("IPT START DATE")) {
                String thirdDate = obs[0][1];
                thirdDateCalendar.setTime(App.stringToDate(thirdDate, "yyyy-MM-dd"));
                iptStartDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
                iptStartDate.setVisibility(View.VISIBLE);
            }  else if (obs[0][0].equals("IPT DOSE")) {
                for (RadioButton rb : iptDose.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_quater_per_day)) && obs[0][1].equals("1/4 TAB ONCE ADAY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_half_per_day)) && obs[0][1].equals("1/2 TAB ONCE A DAY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_per_day)) && obs[0][1].equals("1 TAB ONCE A DAY")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if(obs[0][0].equals("ADDITIONAL TREATMENT IPT PATIENT")) {
                for (CheckBox cb : initiatingAdditionalTreatmentIpt.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ctb_iron)) && obs[0][1].equals("IRON")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_multivitamins)) && obs[0][1].equals("MULTIVITAMIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_anthelmintic_albendazole)) && obs[0][1].equals("ANTHELMINTHIC")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                initiatingAdditionalTreatmentIpt.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ANTIBIOTIC GIVEN")) {
                for (RadioButton rb : precribingAntibioticTrial.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                precribingAntibioticTrial.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("PRESCRIBE FURTHER TESTS")) {
                for (RadioButton rb : precribingFurthertest.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                precribingFurthertest.setVisibility(View.VISIBLE);
            }else if(obs[0][0].equals("ADDITIONAL TREATMENT FOR INCONCLUSIVE PATIENT")) {
                for (CheckBox cb : initiatingAdditionalTreatmentAntibiotic.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ctb_iron)) && obs[0][1].equals("IRON")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_multivitamins)) && obs[0][1].equals("MULTIVITAMIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_anthelminthic)) && obs[0][1].equals("ANTHELMINTHIC")) {
                        cb.setChecked(true);
                        break;
                    }else if (cb.getText().equals(getResources().getString(R.string.ctb_other_title)) && obs[0][1].equals("OTHER")) {
                        cb.setChecked(true);
                        break;
                    }else if (cb.getText().equals(getResources().getString(R.string.ctb_none)) && obs[0][1].equals("NONE")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                initiatingAdditionalTreatmentAntibiotic.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String forthDate = obs[0][1];
                forthDateCalender.setTime(App.stringToDate(forthDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", forthDateCalender).toString());
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
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
        }
        if (view == regDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        }
        if (view == iptStartDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");
        }
        if (view == returnVisitDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", FORTH_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            forthDateFragment.setArguments(args);
            forthDateFragment.show(getFragmentManager(), "DatePicker");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if(spinner == cnicOwner.getSpinner()){
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                cnicOwnerOther.setVisibility(View.VISIBLE);
            } else {
                cnicOwnerOther.setVisibility(View.GONE);
            }
        }else if (spinner == extraPulmonarySite.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                extraPulmonarySiteOther.setVisibility(View.VISIBLE);
            } else {
                extraPulmonarySiteOther.setVisibility(View.GONE);
            }
        }else if (spinner == typeFixedDosePrescribed.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_current_formulation))) {
                if(!App.get(regimen).equals(getResources().getString(R.string.ctb_rhz))) {
                    currentTabletsofE.setVisibility(View.VISIBLE);
                }
                currentTabletsofRHZ.setVisibility(View.VISIBLE);

                newTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                adultFormulationofHRZE.setVisibility(View.GONE);
            }else if(parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_new_formulation))){
                if(!App.get(regimen).equals(getResources().getString(R.string.ctb_rhz))) {
                    newTabletsofE.setVisibility(View.VISIBLE);
                }
                newTabletsofRHZ.setVisibility(View.VISIBLE);

                currentTabletsofE.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
                adultFormulationofHRZE.setVisibility(View.GONE);
            }else if(parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_adult_formulation))){
                if(!App.get(regimen).equals(getResources().getString(R.string.ctb_rhz))) {
                    adultFormulationofHRZE.setVisibility(View.VISIBLE);
                }
                newTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
            }
        }else if (spinner == closeContactTypeTreatmentSupport.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                otherCloseContactSupporterType.setVisibility(View.VISIBLE);
            } else {
                otherCloseContactSupporterType.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
        }
    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        regDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        iptStartDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
        returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", forthDateCalender).toString());
        regDate.setVisibility(View.GONE);
        cnicLinearLayout.setVisibility(View.GONE);
        cnicOwner.setVisibility(View.GONE);
        cnicOwnerOther.setVisibility(View.GONE);
        tbRegisterationNumber.setVisibility(View.GONE);
        tbType.setVisibility(View.GONE);
        extraPulmonarySite.setVisibility(View.GONE);
        extraPulmonarySiteOther.setVisibility(View.GONE);
        patientType.setVisibility(View.GONE);
        treatmentInitiated.setVisibility(View.GONE);
        reasonTreatmentNotIniated.setVisibility(View.GONE);
        patientCategory.setVisibility(View.GONE);
        weight.setVisibility(View.GONE);
        regimen.setVisibility(View.GONE);
        typeFixedDosePrescribed.setVisibility(View.GONE);
        initiatingAdditionalTreatment.setVisibility(View.GONE);
        currentTabletsofRHZ.setVisibility(View.GONE);
        currentTabletsofE.setVisibility(View.GONE);
        newTabletsofRHZ.setVisibility(View.GONE);
        newTabletsofE.setVisibility(View.GONE);
        adultFormulationofHRZE.setVisibility(View.GONE);
        typeOfDiagnosis.setVisibility(View.GONE);
        histopathologicalEvidence.setVisibility(View.GONE);
        radiologicalEvidence.setVisibility(View.GONE);
        nameOfSupporter.setVisibility(View.GONE);
        mobileLinearLayout.setVisibility(View.GONE);
        closeContactTypeTreatmentSupport.setVisibility(View.GONE);
        otherCloseContactSupporterType.setVisibility(View.GONE);
        bcgScar.setVisibility(View.GONE);
        tbHistoryIn2Years.setVisibility(View.GONE);
        eligibleForIpt.setVisibility(View.GONE);
        iptStartDate.setVisibility(View.GONE);
        iptDose.setVisibility(View.GONE);
        initiatingAdditionalTreatmentIpt.setVisibility(View.GONE);
        precribingAntibioticTrial.setVisibility(View.GONE);
        precribingFurthertest.setVisibility(View.GONE);
        initiatingAdditionalTreatmentAntibiotic.setVisibility(View.GONE);
        updateDisplay();

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
                String cnic1 = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Patient Registration", "NATIONAL IDENTIFICATION NUMBER");
                String cnicowner1 = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Patient Registration", "COMPUTERIZED NATIONAL IDENTIFICATION OWNER");
                String cnicownerother1 = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Patient Registration", "OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER");
                String regDate = serverService.getEncounterDateTime(App.getPatientId(), App.getProgram() + "-" + "Patient Registration");

                if (cnic1 != null)
                    result.put("NATIONAL IDENTIFICATION NUMBER", cnic1);
                if (cnicowner1 != null) {
                    result.put("COMPUTERIZED NATIONAL IDENTIFICATION OWNER", cnicowner1.equals("SELF") ? getResources().getString(R.string.ctb_self) :
                            (cnicowner1.equals("MOTHER") ? getResources().getString(R.string.ctb_mother) :
                                    (cnicowner1.equals("FATHER") ? getResources().getString(R.string.ctb_father) :
                                            (cnicowner1.equals("SISTER") ? getResources().getString(R.string.ctb_sister) :
                                                    (cnicowner1.equals("BROTHER") ? getResources().getString(R.string.ctb_brother) :
                                                                    (cnicowner1.equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.ctb_paternal_grandfather) :
                                                                            (cnicowner1.equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.ctb_paternal_grandmother) :
                                                                                    (cnicowner1.equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.ctb_maternal_grandfather) :
                                                                                            (cnicowner1.equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.ctb_maternal_grandmother) :
                                                                                                    (cnicowner1.equals("UNCLE") ? getResources().getString(R.string.ctb_uncle) :
                                                                                                            (cnicowner1.equals("AUNT") ? getResources().getString(R.string.ctb_aunt) :
                                                                                                                    getResources().getString(R.string.ctb_other_title))))))))))));

                }

                if (cnicownerother1 != null)
                    result.put("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER", cnicownerother1);

                if (regDate != null)
                    result.put("FORM DATE", regDate);

                return result;
            }

            @Override
            protected void onProgressUpdate(String... values) {
            }


            @Override
            protected void onPostExecute(HashMap<String, String> result) {
                super.onPostExecute(result);
                loading.dismiss();

                if (result.get("NATIONAL IDENTIFICATION NUMBER") != null) {
                    String value = result.get("NATIONAL IDENTIFICATION NUMBER");
                    cnic1.getEditText().setText(value.substring(0, 5));
                    cnic1.getEditText().setKeyListener(null);
                    cnic2.getEditText().setText(value.substring(6, 13));
                    cnic2.getEditText().setKeyListener(null);
                    cnic3.getEditText().setText(value.substring(14));
                    cnic3.getEditText().setKeyListener(null);
                }

                if (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER") != null) {
                    cnicOwner.getSpinner().selectValue(result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER"));
                    cnicOwner.getSpinner().setEnabled(false);
                }

                if (result.get("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER") != null) {
                    cnicOwnerOther.getEditText().setText(result.get("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER"));
                    cnicOwnerOther.getEditText().setKeyListener(null);
                }

                if (result.get("FORM DATE") != null) {
                    String format = "";
                    String registerationDate = result.get("FORM DATE");
                    if(registerationDate.contains("/")){
                        format = "dd/MM/yyyy";
                    }
                    else{
                        format = "yyyy-MM-dd";
                    }
                    secondDateCalendar.setTime(App.stringToDate(registerationDate, format));
                    regDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
                    regDate.getButton().setClickable(false);
                }
            }
        };
        autopopulateFormTask.execute("");
        String referralTransferLocation = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Presumptive Case Confirmation", "WEIGHT (KG)");
        if(referralTransferLocation!=null){
            String weight = referralTransferLocation.split("\\.")[0];
            weightAtBaseline.getEditText().setText(weight);

        }
        weightAtBaseline.getEditText().setKeyListener(null);
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

            else if (getArguments().getInt("type") ==FORTH_DATE_DIALOG_ID)
                calendar = formDateCalendar;
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
            else if(((int) view.getTag()) == FORTH_DATE_DIALOG_ID)
                forthDateCalender.set(yy, mm, dd);
            updateDisplay();

        }
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == patientHaveTb.getRadioGroup()) {
            int patientAge = App.getPatient().getPerson().getAge();
            if (patientHaveTb.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                regDate.setVisibility(View.VISIBLE);
                cnicLinearLayout.setVisibility(View.VISIBLE);
                String cnicString = App.get(cnic1) + "-" + App.get(cnic2) + "-" + App.get(cnic3);
                if(RegexUtil.isValidNIC(cnicString)){
                    cnicOwner.setVisibility(View.VISIBLE);
                    if(App.get(cnicOwner).equalsIgnoreCase(getResources().getString(R.string.ctb_other_title))){
                        cnicOwnerOther.setVisibility(View.VISIBLE);
                    }
                }else{
                    cnicOwner.setVisibility(View.GONE);
                    cnicOwnerOther.setVisibility(View.GONE);
                }
                tbRegisterationNumber.setVisibility(View.VISIBLE);
                tbType.setVisibility(View.VISIBLE);
                patientType.setVisibility(View.VISIBLE);
                treatmentInitiated.setVisibility(View.VISIBLE);
                patientCategory.setVisibility(View.VISIBLE);
                weight.setVisibility(View.VISIBLE);
                regimen.setVisibility(View.VISIBLE);
                typeFixedDosePrescribed.setVisibility(View.VISIBLE);
                initiatingAdditionalTreatment.setVisibility(View.VISIBLE);
                typeOfDiagnosis.setVisibility(View.VISIBLE);
                nameOfSupporter.setVisibility(View.VISIBLE);
                closeContactTypeTreatmentSupport.setVisibility(View.VISIBLE);

                bcgScar.setVisibility(View.GONE);
                tbHistoryIn2Years.setVisibility(View.GONE);
                eligibleForIpt.setVisibility(View.GONE);
                iptStartDate.setVisibility(View.GONE);
                iptDose.setVisibility(View.GONE);
                initiatingAdditionalTreatmentIpt.setVisibility(View.GONE);
                precribingAntibioticTrial.setVisibility(View.GONE);
                precribingFurthertest.setVisibility(View.GONE);
                initiatingAdditionalTreatmentAntibiotic.setVisibility(View.GONE);
            }else if(patientHaveTb.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.no))){
                if(patientAge <=5){
                    String bcgScarValue = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Presumptive Case Confirmation", "BACILLUS CALMETTE–GUÉRIN VACCINE");
                    if(bcgScarValue!=null) {
                        for (RadioButton rb : bcgScar.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.yes)) && bcgScarValue.equalsIgnoreCase("YES")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.no)) && bcgScarValue.equalsIgnoreCase("NO")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && bcgScarValue.equalsIgnoreCase("UNKNOWN")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && bcgScarValue.equalsIgnoreCase("REFUSED")) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }
                    bcgScar.setVisibility(View.VISIBLE);
                    String contactHistory2Year = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Presumptive Case Confirmation", "PATIENT IS CONTACT OF KNOWN OR SUSPECTED SUSPICIOUS CASE IN PAST 2 YEARS");
                    if(contactHistory2Year!=null) {
                        for (RadioButton rb : tbHistoryIn2Years.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.yes)) && contactHistory2Year.equalsIgnoreCase("YES")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.no)) && contactHistory2Year.equalsIgnoreCase("NO")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && contactHistory2Year.equalsIgnoreCase("UNKNOWN")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && contactHistory2Year.equalsIgnoreCase("REFUSED")) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }
                    tbHistoryIn2Years.setVisibility(View.VISIBLE);
                    eligibleForIpt.setVisibility(View.VISIBLE);
                    iptStartDate.setVisibility(View.VISIBLE);
                    if(!App.get(weightAtBaseline).isEmpty()){
                        int weightInt = Integer.parseInt(App.get(weightAtBaseline));
                        if(weightInt<2.5){
                            iptDose.getRadioGroup().getButtons().get(0).setChecked(true);
                        }else if(weightInt>2.5 && weightInt<5){
                            iptDose.getRadioGroup().getButtons().get(1).setChecked(true);
                        }else if(weightInt>5 && weightInt<10){
                            iptDose.getRadioGroup().getButtons().get(2).setChecked(true);
                        }
                    }
                    iptDose.setVisibility(View.VISIBLE);
                    initiatingAdditionalTreatmentIpt.setVisibility(View.VISIBLE);

                }
                else{
                    bcgScar.setVisibility(View.GONE);
                    tbHistoryIn2Years.setVisibility(View.GONE);
                    eligibleForIpt.setVisibility(View.GONE);
                    iptStartDate.setVisibility(View.GONE);
                    iptDose.setVisibility(View.GONE);
                    initiatingAdditionalTreatmentIpt.setVisibility(View.GONE);
                }
                regDate.setVisibility(View.GONE);
                cnicLinearLayout.setVisibility(View.GONE);
                cnicOwner.setVisibility(View.GONE);
                cnicOwnerOther.setVisibility(View.GONE);
                tbRegisterationNumber.setVisibility(View.GONE);
                nameOfSupporter.setVisibility(View.GONE);
                tbType.setVisibility(View.GONE);
                extraPulmonarySite.setVisibility(View.GONE);
                extraPulmonarySiteOther.setVisibility(View.GONE);
                patientType.setVisibility(View.GONE);
                treatmentInitiated.setVisibility(View.GONE);
                initiatingAdditionalTreatment.setVisibility(View.GONE);
                reasonTreatmentNotIniated.setVisibility(View.GONE);
                patientCategory.setVisibility(View.GONE);
                weight.setVisibility(View.GONE);
                regimen.setVisibility(View.GONE);
                typeFixedDosePrescribed.setVisibility(View.GONE);
                typeOfDiagnosis.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
                newTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                adultFormulationofHRZE.setVisibility(View.GONE);
                histopathologicalEvidence.setVisibility(View.GONE);
                radiologicalEvidence.setVisibility(View.GONE);
                closeContactTypeTreatmentSupport.setVisibility(View.GONE);
                precribingAntibioticTrial.setVisibility(View.GONE);
                precribingFurthertest.setVisibility(View.GONE);
                initiatingAdditionalTreatmentAntibiotic.setVisibility(View.GONE);

            }
            else if(patientHaveTb.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_inconclusive))){
                precribingAntibioticTrial.setVisibility(View.VISIBLE);
                precribingFurthertest.setVisibility(View.VISIBLE);
                initiatingAdditionalTreatmentAntibiotic.setVisibility(View.VISIBLE);

                regDate.setVisibility(View.GONE);
                cnicLinearLayout.setVisibility(View.GONE);
                tbRegisterationNumber.setVisibility(View.GONE);
                cnicOwner.setVisibility(View.GONE);
                cnicOwnerOther.setVisibility(View.GONE);
                tbType.setVisibility(View.GONE);
                extraPulmonarySite.setVisibility(View.GONE);
                extraPulmonarySiteOther.setVisibility(View.GONE);
                patientType.setVisibility(View.GONE);
                treatmentInitiated.setVisibility(View.GONE);
                reasonTreatmentNotIniated.setVisibility(View.GONE);
                patientCategory.setVisibility(View.GONE);
                weight.setVisibility(View.GONE);
                regimen.setVisibility(View.GONE);
                typeFixedDosePrescribed.setVisibility(View.GONE);
                typeOfDiagnosis.setVisibility(View.GONE);
                nameOfSupporter.setVisibility(View.GONE);
                closeContactTypeTreatmentSupport.setVisibility(View.GONE);
                bcgScar.setVisibility(View.GONE);
                tbHistoryIn2Years.setVisibility(View.GONE);
                eligibleForIpt.setVisibility(View.GONE);
                iptStartDate.setVisibility(View.GONE);
                iptDose.setVisibility(View.GONE);
                initiatingAdditionalTreatmentIpt.setVisibility(View.GONE);
            }
        } else if (group == tbType.getRadioGroup()) {
            if (tbType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_extra_pulmonary))) {
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
        }else if (group == treatmentInitiated.getRadioGroup()) {
            if (treatmentInitiated.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.no))) {
                reasonTreatmentNotIniated.setVisibility(View.VISIBLE);
            } else {
                reasonTreatmentNotIniated.setVisibility(View.GONE);
            }
            if(treatmentInitiated.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))){
                initiatingAdditionalTreatment.setVisibility(View.VISIBLE);
            }else{
                initiatingAdditionalTreatment.setVisibility(View.GONE);
            }
        }else if (group == patientCategory.getRadioGroup()) {
            if (patientCategory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_category_3))) {
                regimen.getRadioGroup().getButtons().get(1).setChecked(true);
            }
        }else if (group == regimen.getRadioGroup()) {
            if (regimen.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_rhz))) {
                adultFormulationofHRZE.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                newTabletsofE.setVisibility(View.GONE);
            }
            else{
                if(App.get(typeFixedDosePrescribed).equals(getResources().getString(R.string.ctb_adult_formulation))) {
                    adultFormulationofHRZE.setVisibility(View.VISIBLE);
                }
                else if(App.get(typeFixedDosePrescribed).equals(getResources().getString(R.string.ctb_current_formulation))) {
                    currentTabletsofE.setVisibility(View.VISIBLE);
                }
                else if(App.get(typeFixedDosePrescribed).equals(getResources().getString(R.string.ctb_new_formulation))) {
                    newTabletsofE.setVisibility(View.VISIBLE);
                }
            }
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
