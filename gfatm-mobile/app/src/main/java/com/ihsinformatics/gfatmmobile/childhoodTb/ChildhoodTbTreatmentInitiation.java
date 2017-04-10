package com.ihsinformatics.gfatmmobile.childhoodTb;

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
import android.widget.LinearLayout;
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
import com.ihsinformatics.gfatmmobile.fast.FastTreatmentInitiationForm;
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
        thirdDateFragment = new ChildhoodTbTreatmentInitiation.SelectDateFragment();

        forthDateCalender = Calendar.getInstance();
        forthDateFragment = new ChildhoodTbTreatmentInitiation.SelectDateFragment();
        // first page views...
        formDate = new TitledButton(context, "Section A: TB Treatment Initiation ", getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        weightAtBaseline = new TitledEditText(context, null, getResources().getString(R.string.ctb_weight_at_baseline), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        patientHaveTb = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_patient_have_tb), getResources().getStringArray(R.array.ctb_patient_have_tb_list),null, App.HORIZONTAL, App.VERTICAL);
        regDate = new TitledButton(context, null, getResources().getString(R.string.fast_registeration_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        cnicLinearLayout = new LinearLayout(context);
        cnicLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        cnic1 = new TitledEditText(context, null, getResources().getString(R.string.fast_nic_number), "", "#####", 5, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
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
        treatmentInitiated = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_treatment_initiated), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL,true);
        reasonTreatmentNotIniated = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_reason_treatment_not_intiated), getResources().getStringArray(R.array.ctb_reason_not_intiated_list), getResources().getString(R.string.ctb_patient_refused_treatment), App.VERTICAL,true);
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
        doctorNotes = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 1000, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL,false);

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
        resetViews();

    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();

            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            }  else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            }else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }



    }

    @Override
    public boolean validate() {
        boolean error = false;

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
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
            }
            bundle.putBoolean("save", false);
        }
        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"TIME TAKEN TO FILL FORM", String.valueOf(App.getTimeDurationBetween(startTime, endTime))});
        observations.add (new String[] {"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add (new String[] {"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});
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

                String result = serverService.saveEncounterAndObservation("PPA Score", FORM, formDateCalendar, observations.toArray(new String[][]{}));
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
            if (obs[0][0].equals("FORM START TIME")) {
                startTime = App.stringToDate(obs[0][1], "yyyy-MM-dd hh:mm:ss");
            }
        }
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

    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        regDate.setVisibility(View.GONE);
        cnicLinearLayout.setVisibility(View.GONE);
        cnicOwner.setVisibility(View.GONE);
        cnicOwnerOther.setVisibility(View.GONE);
        tbRegisterationNumber.setVisibility(View.GONE);
        tbType.setVisibility(View.GONE);
        patientType.setVisibility(View.GONE);
        treatmentInitiated.setVisibility(View.GONE);
        patientCategory.setVisibility(View.GONE);
        weight.setVisibility(View.GONE);
        regimen.setVisibility(View.GONE);
        typeFixedDosePrescribed.setVisibility(View.GONE);
        typeOfDiagnosis.setVisibility(View.GONE);
        nameOfSupporter.setVisibility(View.GONE);
        closeContactTypeTreatmentSupport.setVisibility(View.GONE);
        precribingAntibioticTrial.setVisibility(View.GONE);
        precribingFurthertest.setVisibility(View.GONE);
        initiatingAdditionalTreatmentAntibiotic.setVisibility(View.GONE);
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
        if (group == patientHaveTb.getRadioGroup()) {
            if (patientHaveTb.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                regDate.setVisibility(View.VISIBLE);
                cnicLinearLayout.setVisibility(View.VISIBLE);
                tbRegisterationNumber.setVisibility(View.VISIBLE);
                tbType.setVisibility(View.VISIBLE);
                patientType.setVisibility(View.VISIBLE);
                treatmentInitiated.setVisibility(View.VISIBLE);
                patientCategory.setVisibility(View.VISIBLE);
                weight.setVisibility(View.VISIBLE);
                regimen.setVisibility(View.VISIBLE);
                typeFixedDosePrescribed.setVisibility(View.VISIBLE);
                typeOfDiagnosis.setVisibility(View.VISIBLE);
                nameOfSupporter.setVisibility(View.VISIBLE);
                closeContactTypeTreatmentSupport.setVisibility(View.VISIBLE);
            }else if(patientHaveTb.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.no))){

            }
            else if(patientHaveTb.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_inconclusive))){
                precribingAntibioticTrial.setVisibility(View.VISIBLE);
                precribingFurthertest.setVisibility(View.VISIBLE);
                initiatingAdditionalTreatmentAntibiotic.setVisibility(View.VISIBLE);
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
