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

@Deprecated
public class ChildhoodTbTreatmentInitiation extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    Boolean dateChoose = false;

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
    TitledRadioGroup moConsultPediatrician;
    TitledEditText nameConsultant;
    TitledEditText reasonConsultation;
    TitledRadioGroup tbDaignosed;

    TitledRadioGroup patientHaveTb;
    TitledButton regDate;
    LinearLayout cnicLinearLayout;
    MyEditText cnic1;
    MyEditText cnic2;
    MyEditText cnic3;
    TitledSpinner cnicOwner;
    TitledEditText cnicOwnerOther;

    //TB TREATMENT INITATION
    TitledEditText tbRegisterationNumber;
    TitledCheckBoxes tbType;
    TitledCheckBoxes typeOfDiagnosis;
    TitledRadioGroup histopathologicalEvidence;
    TitledRadioGroup radiologicalEvidence;
    TitledSpinner extraPulmonarySite;
    TitledEditText extraPulmonarySiteOther;
    TitledSpinner patientType;
    TitledCheckBoxes testConfirmingDiagnosis;
    TitledEditText testConfirmingOthers;
    TitledRadioGroup treatmentInitiated;
    TitledSpinner reasonTreatmentNotIniated;
    TitledCheckBoxes initiatingAdditionalTreatment;
    TitledRadioGroup patientCategory;
    TitledEditText weight;
    TitledEditText weightPercentileEditText;
    TitledRadioGroup regimen;
    TitledSpinner typeFixedDosePrescribed;
    TitledRadioGroup currentTabletsofRHZ;
    TitledRadioGroup currentTabletsofE;
    TitledRadioGroup newTabletsofRHZ;
    TitledRadioGroup newTabletsofE;
    TitledRadioGroup adultFormulationofHRZE;
    TitledEditText nameOfSupporter;
    LinearLayout mobileLinearLayout;
    MyEditText mobileNumber1;
    MyEditText mobileNumber2;
    TitledSpinner closeContactTypeTreatmentSupport;
    TitledEditText otherCloseContactSupporterType;

    //IPT INTIATION
    TitledRadioGroup bcgScar;
    TitledRadioGroup tbHistoryIn2Years;
    TitledRadioGroup eligibleForIpt;
    TitledRadioGroup acceptanceToIpt;
    TitledButton iptStartDate;
    TitledEditText iptRegNo;
    TitledRadioGroup iptDose;
    TitledCheckBoxes initiatingAdditionalTreatmentIpt;

    //ANTIBIOTIC TRIAL
    TitledRadioGroup precribingAntibioticTrial;
    TitledRadioGroup precribingFurthertest;
    TitledCheckBoxes initiatingAdditionalTreatmentAntibiotic;

    //Next Appointment
    TitledButton returnVisitDate;

    MyTextView moInstruction;
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
        formName = Forms.CHILDHOODTB_TREATMENT_INITIATION;
        form = Forms.childhoodTb_treatment_intiation;

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
        thirdDateCalendar = Calendar.getInstance();
        thirdDateFragment = new SelectDateFragment();

        forthDateCalender = Calendar.getInstance();
        forthDateFragment = new SelectDateFragment();
        // first page views...
        formDate = new TitledButton(context, "Section A: TB Treatment Initiation ", getResources().getString(R.string.pet_form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        weightAtBaseline = new TitledEditText(context, null, getResources().getString(R.string.ctb_weight_at_baseline), "", "", 3, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        moConsultPediatrician = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_mo_consult_pediatrician), getResources().getStringArray(R.array.yes_no_options),null, App.HORIZONTAL, App.VERTICAL,true);
        nameConsultant = new TitledEditText(context, null, getResources().getString(R.string.ctb_name_consultant), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        reasonConsultation = new TitledEditText(context, null, getResources().getString(R.string.ctb_reason_consultation), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        tbDaignosed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_daignosed), getResources().getStringArray(R.array.yes_no_options),null, App.HORIZONTAL, App.VERTICAL,true);
        patientHaveTb = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_patient_have_tb), getResources().getStringArray(R.array.ctb_patient_have_tb_list),null, App.HORIZONTAL, App.VERTICAL,true);
        regDate = new TitledButton(context, null, getResources().getString(R.string.ctb_registration_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        cnicLinearLayout = new LinearLayout(context);
        cnicLinearLayout.setOrientation(LinearLayout.VERTICAL);
        MyTextView cnic = new MyTextView(context, getResources().getString(R.string.pet_cnic));
        cnicLinearLayout.addView(cnic);
        LinearLayout cnicPartLayout = new LinearLayout(context);
        cnicPartLayout.setOrientation(LinearLayout.HORIZONTAL);
        cnic1 = new MyEditText(context, "", 5, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE);
        cnic1.setHint("XXXXX");
        cnicPartLayout.addView(cnic1);
        MyTextView cnicDash = new MyTextView(context, " - ");
        cnicPartLayout.addView(cnicDash);
        cnic2 = new MyEditText(context, "", 7, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE);
        cnic2.setHint("XXXXXXX");
        cnicPartLayout.addView(cnic2);
        MyTextView cnicDash2 = new MyTextView(context, " - ");
        cnicPartLayout.addView(cnicDash2);
        cnic3 = new MyEditText(context, "", 1, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE);
        cnic3.setHint("X");
        cnicPartLayout.addView(cnic3);
        cnicLinearLayout.addView(cnicPartLayout);
        cnicOwner = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_cnic_owner), getResources().getStringArray(R.array.ctb_close_contact_type_list), null, App.VERTICAL,true, "CLOSE CONTACT WITH PATIENT", new String[]{"SELF", "MOTHER", "FATHER", "BROTHER", "SISTER", "PATERNAL GRANDFATHER", "PATERNAL GRANDMOTHER", "MATERNAL GRANDFATHER", "MATERNAL GRANDMOTHER", "UNCLE", "AUNT", "OTHER CONTACT TYPE"});
        cnicOwnerOther = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        tbRegisterationNumber = new TitledEditText(context, null, getResources().getString(R.string.ctb_tb_registration_no), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        tbType = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_type_of_tb_name), getResources().getStringArray(R.array.ctb_extra_pulomonary_and_pulmonary), null, App.VERTICAL,App.VERTICAL,true);
        extraPulmonarySite = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_site_of_extra_pulmonary), getResources().getStringArray(R.array.ctb_extra_pulmonary_tb_site), getResources().getString(R.string.ctb_lymph_node), App.VERTICAL,true,"EXTRA PULMONARY SITE",new String[]{ "LYMPH NODE SARCOIDOSIS" , "ABDOMEN" , "ACUTE LYMPHOBLASTIC LEUKEMIA WITH CENTRAL NERVOUS SYSTEM INVOLVEMENT" , "RENAL DISEASE" , "TUBERCULOSIS OF BONES AND JOINTS" , "GENITOURINARY TUBERCULOSIS" , "PLEURAL EFFUSION" , "OTHER EXTRA PULMONARY SITE"});
        extraPulmonarySiteOther = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_extra_pulmonary_site), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        patientType = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_patient_type), getResources().getStringArray(R.array.ctb_patient_type_list), getResources().getString(R.string.ctb_new), App.VERTICAL,true,"TB PATIENT TYPE",new String[]{ "NEW TB PATIENT" , "RELAPSE" , "PATIENT REFERRED" , "TRANSFER IN" , "LOST TO FOLLOW-UP" , "TUBERCULOSIS TREATMENT FAILURE" , "OTHER PATIENT TYPE"});
        testConfirmingDiagnosis = new TitledCheckBoxes(mainContent.getContext(), "", getResources().getString(R.string.ctb_test_confirming_diagnosis), getResources().getStringArray(R.array.ctb_confirming_diagnosis_list), null, App.VERTICAL,App.VERTICAL,true);
        testConfirmingOthers = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        treatmentInitiated = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_are_you_intiating_treatment), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL,true);
        reasonTreatmentNotIniated = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_reason_treatment_not_intiated), getResources().getStringArray(R.array.ctb_reason_treatment_not_intiated_list), getResources().getString(R.string.ctb_patient_refused_treatment), App.VERTICAL,true,"TREATMENT NOT STARTED",new String[]{ "REFUSAL OF TREATMENT BY PATIENT" , "LOST TO FOLLOW-UP" , "DECEASED" , "PATIENT REFERRED" , "TREATMENT NOT INITIATED OTHER REASON"});
        initiatingAdditionalTreatment = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_initiating_additional_treatment), getResources().getStringArray(R.array.ctb_pediasure_vitamin_iron_anthelminthic_calpol), null, App.VERTICAL, App.VERTICAL, false, "ADDITIONAL TREATMENT TO TB PATIENT", new String[]{"IRON", "MULTIVITAMIN", "ANTHELMINTHIC", "PEDIASURE", "VITAMIN B COMPLEX", "CALPOL", "OTHER ADDITIONAL TREATMENT", "NONE"});
        patientCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_patient_category), getResources().getStringArray(R.array.ctb_patient_category3_list), getResources().getString(R.string.ctb_categoryI), App.VERTICAL, App.VERTICAL,true,"TB PATIENT TYPE",new String[]{ "CATEGORY I TUBERCULOSIS" , "CATEGORY II TUBERCULOSIS", "CATEGORY III TUBERCULOSIS"});
        weight = new TitledEditText(context, null, getResources().getString(R.string.ctb_patient_weight), "", "", 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true,"WEIGHT (KG)");
        weightPercentileEditText = new TitledEditText(context, null,getResources().getString(R.string.ctb_weight_percentile), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false,"WEIGHT PERCENTILE GROUP");
        regimen = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_regimen), getResources().getStringArray(R.array.ctb_regimen_list), getResources().getString(R.string.ctb_rhz), App.HORIZONTAL, App.VERTICAL,true,"REGIMEN", new String[]{"RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL", "RIFAMPICIN/ISONIAZID/PYRAZINAMIDE"});
        typeFixedDosePrescribed = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_type_of_fixed_dose), getResources().getStringArray(R.array.ctb_type_of_fixed_dose_list), null, App.VERTICAL,true,"PAEDIATRIC DOSE COMBINATION",new String[]{ "CURRENT FORMULATION" , "NEW FORMULATION", "ADULT FORMULATION"});
        currentTabletsofRHZ = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_current_formulation_number_of_tablet_rhz), getResources().getStringArray(R.array.ctb_1_to_5_list), null, App.HORIZONTAL, App.VERTICAL,true, "CURRENT FORMULATION OF TABLETS OF RHZ", getResources().getStringArray(R.array.ctb_1_to_5_list));
        currentTabletsofE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_current_formulation_number_of_tablet_e), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL,true, "CURRENT FORMULATION OF TABLETS OF  E", getResources().getStringArray(R.array.ctb_number_of_tablets));
        newTabletsofRHZ = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_new_formulation_number_of_tablet_rhz), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL,true, "NEW FORMULATION OF TABLETS OF RHZ", getResources().getStringArray(R.array.ctb_number_of_tablets));
        newTabletsofE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_new_formulation_number_of_tablet_e), getResources().getStringArray(R.array.ctb_number_of_tablets), null, App.HORIZONTAL, App.VERTICAL,true, "NEW FORMULATION OF TABLETS OF E", getResources().getStringArray(R.array.ctb_number_of_tablets));
        adultFormulationofHRZE = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_adult_formulation), getResources().getStringArray(R.array.ctb_2_to_5_list), null, App.HORIZONTAL, App.VERTICAL,true, "ADULT FORMULATION OF TABLETS OF RHZE", getResources().getStringArray(R.array.ctb_2_to_5_list));
        typeOfDiagnosis = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_type_of_diagnosis), getResources().getStringArray(R.array.ctb_type_of_diagnosis_list), null, App.VERTICAL, App.VERTICAL);

        histopathologicalEvidence = new TitledRadioGroup(context, "If clinically diagnosed, specify evidence", getResources().getString(R.string.ctb_histopathologiacal_evidence), getResources().getStringArray(R.array.yes_no_options), null, App.VERTICAL, App.VERTICAL);
        radiologicalEvidence = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_radiological_evidence), getResources().getStringArray(R.array.yes_no_options), null, App.VERTICAL, App.VERTICAL);
        nameOfSupporter = new TitledEditText(context, null, getResources().getString(R.string.ctb_treatment_supporter), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL,true);
        mobileLinearLayout = new LinearLayout(context);
        mobileLinearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout mobileQuestion = new LinearLayout(context);
        mobileQuestion.setOrientation(LinearLayout.HORIZONTAL);
        MyTextView mobileNumberLabel = new MyTextView(context, getResources().getString(R.string.ctb_mobile_number));
        mobileQuestion.addView(mobileNumberLabel);
        TextView mandatorySign = new TextView(context);
        mandatorySign.setText("*");
        mandatorySign.setTextColor(Color.parseColor("#ff0000"));
        mobileQuestion.addView(mandatorySign);
        mobileLinearLayout.addView(mobileQuestion);
        LinearLayout mobileNumberPart = new LinearLayout(context);
        mobileNumberPart.setOrientation(LinearLayout.HORIZONTAL);
        mobileNumber1 = new MyEditText(context,"", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        mobileNumber1.setHint("03XX");
        mobileNumberPart.addView(mobileNumber1);
        MyTextView mobileNumberDash = new MyTextView(context, " - ");
        mobileNumberPart.addView(mobileNumberDash);
        mobileNumber2 = new MyEditText(context,"",  7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        mobileNumber2.setHint("XXXXXXX");
        mobileNumberPart.addView(mobileNumber2);

        mobileLinearLayout.addView(mobileNumberPart);
        closeContactTypeTreatmentSupport = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ctb_close_contact_supporter_with_patient), getResources().getStringArray(R.array.ctb_close_contact_type_list), null, App.VERTICAL,true, "CLOSE CONTACT WITH PATIENT", new String[]{"SELF", "MOTHER", "FATHER", "BROTHER", "SISTER", "PATERNAL GRANDFATHER", "PATERNAL GRANDMOTHER", "MATERNAL GRANDFATHER", "MATERNAL GRANDMOTHER", "UNCLE", "AUNT", "OTHER CONTACT TYPE"});
        otherCloseContactSupporterType = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL,false);

        // SECTION B : IPT INITIATION

        bcgScar = new TitledRadioGroup(context, "Section B: IPT Initiation", getResources().getString(R.string.ctb_bcg_scar), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL,true);
        tbHistoryIn2Years = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_history_2years), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL);
        eligibleForIpt = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_eligible_for_ipt), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL,true);
        acceptanceToIpt = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_patient_acceptance_ipt), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL,true);
        iptStartDate = new TitledButton(context, null, getResources().getString(R.string.ctb_ipt_start_date), DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);
        iptRegNo = new TitledEditText(context, null, getResources().getString(R.string.ctb_ipt_reg_no), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL,false);
        iptDose = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ipt_dose), getResources().getStringArray(R.array.ctb_dose_list), null, App.VERTICAL, App.VERTICAL,true,"IPT DOSE",new String[]{ "1/4 TAB ONCE A DAY" , "1/2 TAB ONCE A DAY" ,"1 TAB ONCE A DAY"});
        initiatingAdditionalTreatmentIpt = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_initiating_additional_treatment), getResources().getStringArray(R.array.ctb_iron_multivitamins_anthelmintic), null, App.VERTICAL, App.VERTICAL);

        // SECTION C : ANTIBIOTIC INITIATION

        precribingAntibioticTrial = new TitledRadioGroup(context, "Section C: Inconclusive TB Patient: Antibiotic Trial", getResources().getString(R.string.ctb_prescribing_antibiotic_trial), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL);
        precribingFurthertest = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_prescribing_further_tests), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL);
        eligibleForIpt = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_eligible_for_ipt), getResources().getStringArray(R.array.yes_no_options), null, App.VERTICAL, App.VERTICAL,true);
        initiatingAdditionalTreatmentAntibiotic= new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_initiating_additional_treatment), getResources().getStringArray(R.array.ctb_iron_multivitamins_anthelmintic_other_none), null, App.VERTICAL, App.VERTICAL);

        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.ctb_next_appointment_date), DateFormat.format("dd-MMM-yyyy", forthDateCalender).toString(), App.HORIZONTAL);
        moInstruction = new MyTextView(context,getResources().getString(R.string.ctb_treatment_initiation_mo_instruction));
        doctorNotes = new TitledEditText(context, null, getResources().getString(R.string.ctb_doctor_notes), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL,false,"CLINICIAN NOTES (TEXT)");

        forthDateCalender.add(Calendar.DAY_OF_MONTH, 30);

        views = new View[]{formDate.getButton(),weightAtBaseline.getEditText(),
                moConsultPediatrician.getRadioGroup(),
                nameConsultant.getEditText(),
                reasonConsultation.getEditText(),
                tbDaignosed.getRadioGroup(),testConfirmingOthers.getEditText(),
                patientHaveTb.getRadioGroup(),regDate.getButton(),cnicLinearLayout,cnic1,cnic2,cnic3,
                cnicOwner.getSpinner(),cnicOwnerOther.getEditText(),tbRegisterationNumber.getEditText(),tbType,
                extraPulmonarySite.getSpinner(),extraPulmonarySiteOther.getEditText(),patientType.getSpinner(),treatmentInitiated.getRadioGroup(),
                reasonTreatmentNotIniated.getSpinner(),initiatingAdditionalTreatment,patientCategory.getRadioGroup(),
                weight.getEditText(),regimen.getRadioGroup(),typeFixedDosePrescribed.getSpinner(),currentTabletsofRHZ.getRadioGroup(),currentTabletsofE.getRadioGroup(),
                newTabletsofRHZ.getRadioGroup(),newTabletsofE.getRadioGroup(),adultFormulationofHRZE.getRadioGroup(),typeOfDiagnosis,histopathologicalEvidence.getRadioGroup(),
                radiologicalEvidence.getRadioGroup(),nameOfSupporter.getEditText(),mobileLinearLayout,mobileNumber1,mobileNumber2,
                closeContactTypeTreatmentSupport.getSpinner(),otherCloseContactSupporterType.getEditText(),bcgScar.getRadioGroup(),
                tbHistoryIn2Years.getRadioGroup(),eligibleForIpt.getRadioGroup(),acceptanceToIpt.getRadioGroup(),iptStartDate.getButton(),
                iptDose.getRadioGroup(),initiatingAdditionalTreatmentIpt,precribingAntibioticTrial.getRadioGroup(),
                precribingFurthertest.getRadioGroup(),initiatingAdditionalTreatmentAntibiotic,returnVisitDate.getButton(),
                doctorNotes.getEditText(),testConfirmingDiagnosis,iptRegNo.getEditText(),weightPercentileEditText.getEditText()};
        viewGroups = new View[][]
                {{
                        formDate,
                        weightAtBaseline,
                        moConsultPediatrician,
                        nameConsultant,
                        reasonConsultation,
                        tbDaignosed,
                        patientHaveTb,
                        typeOfDiagnosis,
                        histopathologicalEvidence,
                        radiologicalEvidence,
                        regDate,
                        cnicLinearLayout,
                        cnicOwner,
                        cnicOwnerOther,
                        tbRegisterationNumber,
                        tbType,
                        extraPulmonarySite,
                        extraPulmonarySiteOther,
                        patientType,
                        testConfirmingDiagnosis,
                        testConfirmingOthers,
                        treatmentInitiated,
                        reasonTreatmentNotIniated,
                        initiatingAdditionalTreatment,
                        patientCategory,
                        weight,
                        weightPercentileEditText,
                        regimen,
                        typeFixedDosePrescribed,
                        currentTabletsofRHZ,
                        currentTabletsofE,
                        newTabletsofRHZ,
                        newTabletsofE,
                        adultFormulationofHRZE,
                        nameOfSupporter,
                        mobileLinearLayout,
                        closeContactTypeTreatmentSupport,
                        otherCloseContactSupporterType,
                        bcgScar,
                        tbHistoryIn2Years,
                        eligibleForIpt,
                        acceptanceToIpt,
                        iptStartDate,
                        iptRegNo,
                        iptDose,
                        initiatingAdditionalTreatmentIpt,
                        precribingAntibioticTrial,
                        precribingFurthertest,
                        initiatingAdditionalTreatmentAntibiotic,
                        returnVisitDate,
                        moInstruction,
                        doctorNotes,
                }};
        formDate.getButton().setOnClickListener(this);
        patientHaveTb.getRadioGroup().setOnCheckedChangeListener(this);
        regDate.getButton().setOnClickListener(this);
        cnicOwner.getSpinner().setOnItemSelectedListener(this);

        extraPulmonarySite.getSpinner().setOnItemSelectedListener(this);
        patientType.getSpinner().setOnItemSelectedListener(this);
        treatmentInitiated.getRadioGroup().setOnCheckedChangeListener(this);
        reasonTreatmentNotIniated.getSpinner().setOnItemSelectedListener(this);
        for (CheckBox cb : testConfirmingDiagnosis.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        ArrayList<MyCheckBox> checkBoxList = initiatingAdditionalTreatment.getCheckedBoxes();
        for (CheckBox cb : initiatingAdditionalTreatment.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        for (CheckBox cb : tbType.getCheckedBoxes())
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
        acceptanceToIpt.getRadioGroup().setOnCheckedChangeListener(this);
        iptStartDate.getButton().setOnClickListener(this);
        iptDose.getRadioGroup().setOnCheckedChangeListener(this);
        moConsultPediatrician.getRadioGroup().setOnCheckedChangeListener(this);
        tbDaignosed.getRadioGroup().setOnCheckedChangeListener(this);
        ArrayList<MyCheckBox> checkBoxList3 = initiatingAdditionalTreatmentIpt.getCheckedBoxes();
        for (CheckBox cb : initiatingAdditionalTreatmentIpt.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        precribingAntibioticTrial.getRadioGroup().setOnCheckedChangeListener(this);
        precribingFurthertest.getRadioGroup().setOnCheckedChangeListener(this);
        ArrayList<MyCheckBox> checkBoxList4 = initiatingAdditionalTreatmentAntibiotic.getCheckedBoxes();
        for (CheckBox cb : initiatingAdditionalTreatmentAntibiotic.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        returnVisitDate.getButton().setOnClickListener(this);

        cnic1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==5){
                    cnic2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String cnic = cnic1.getText().toString() + "-" + cnic2.getText().toString() + "-" + cnic3.getText().toString();
                if (RegexUtil.isValidNIC(cnic) && App.get(patientHaveTb).equals(getResources().getString(R.string.yes))) {
                    cnicOwner.setVisibility(View.VISIBLE);
                }else{
                    cnicOwner.setVisibility(View.GONE);
                }
            }
        });


        cnic2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==7){
                    cnic3.requestFocus();
                }

                if(s.length()==0){
                    cnic1.requestFocus();
                    cnic1.setSelection(cnic1.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String cnic = cnic1.getText().toString() + "-" + cnic2.getText().toString() + "-" + cnic3.getText().toString();
                if (RegexUtil.isValidNIC(cnic) && App.get(patientHaveTb).equals(getResources().getString(R.string.yes))) {
                    cnicOwner.setVisibility(View.VISIBLE);
                }
                else{
                    cnicOwner.setVisibility(View.GONE);
                }


            }
        });

        cnic3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    cnic2.requestFocus();
                    cnic2.setSelection(cnic2.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                String cnic = cnic1.getText().toString() + "-" + cnic2.getText().toString() + "-" + cnic3.getText().toString();
                if (RegexUtil.isValidNIC(cnic) && App.get(patientHaveTb).equals(getResources().getString(R.string.yes))) {
                    cnicOwner.setVisibility(View.VISIBLE);
                }else{
                    cnicOwner.setVisibility(View.GONE);
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

                    Double w = Double.parseDouble(App.get(weight));
                    if(w < 0.5 || w > 700.0)
                        weight.getEditText().setError(getString(R.string.pet_invalid_weight_range));
                    else
                        weight.getEditText().setError(null);

                    float value = Float.parseFloat(s.toString());
                    String percentile = serverService.getPercentile(App.get(weight));
                    weightPercentileEditText.getEditText().setText(percentile);

                    //CURRENT FORMULATION
                    if(value>=4 && value<=6){
                        currentTabletsofRHZ.getRadioGroup().getButtons().get(0).setChecked(true);
                        currentTabletsofE.getRadioGroup().getButtons().get(0).setChecked(true);
                    }
                    else if(value>=7 && value<=10){
                        currentTabletsofRHZ.getRadioGroup().getButtons().get(1).setChecked(true);
                        currentTabletsofE.getRadioGroup().getButtons().get(1).setChecked(true);

                    }
                    else if(value>=11 && value<=14){
                        currentTabletsofRHZ.getRadioGroup().getButtons().get(2).setChecked(true);
                        currentTabletsofE.getRadioGroup().getButtons().get(2).setChecked(true);

                    }
                    else if(value>=15 && value<=19){
                        currentTabletsofRHZ.getRadioGroup().getButtons().get(3).setChecked(true);
                        currentTabletsofE.getRadioGroup().getButtons().get(3).setChecked(true);

                    }
                    else if(value>=20 && value<=24){
                        currentTabletsofRHZ.getRadioGroup().getButtons().get(4).setChecked(true);
                        currentTabletsofE.getRadioGroup().getButtons().get(3).setChecked(true);
                    }
                    else if(value>=25){
                        typeFixedDosePrescribed.getSpinner().selectValue(getResources().getString(R.string.ctb_adult_formulation));
                    }


                    //NEW FORMULATION
                    if(value>=4 && value<=7){
                        newTabletsofRHZ.getRadioGroup().getButtons().get(0).setChecked(true);
                        newTabletsofE.getRadioGroup().getButtons().get(0).setChecked(true);
                    }
                    else if(value>=8 && value<=11){
                        newTabletsofRHZ.getRadioGroup().getButtons().get(1).setChecked(true);
                        newTabletsofE.getRadioGroup().getButtons().get(1).setChecked(true);
                    }
                    else if(value>=12 && value<=15){
                        newTabletsofRHZ.getRadioGroup().getButtons().get(2).setChecked(true);
                        newTabletsofE.getRadioGroup().getButtons().get(2).setChecked(true);
                    }
                    else if(value>=16 && value<=24){
                        newTabletsofRHZ.getRadioGroup().getButtons().get(3).setChecked(true);
                        newTabletsofE.getRadioGroup().getButtons().get(3).setChecked(true);
                    }
                    else if(value>=25){
                        typeFixedDosePrescribed.getSpinner().selectValue(getResources().getString(R.string.ctb_adult_formulation));
                    }



                    //ADULT FORMULATION
                    if(value>=26 && value<=29){
                        adultFormulationofHRZE.getRadioGroup().getButtons().get(0).setChecked(true);
                    }
                    else if(value>=30 && value<=39){
                        adultFormulationofHRZE.getRadioGroup().getButtons().get(0).setChecked(true);
                    }
                    else if(value>=40 && value<=54){
                        adultFormulationofHRZE.getRadioGroup().getButtons().get(1).setChecked(true);
                    }
                    else if(value>=55 && value<=70){
                        adultFormulationofHRZE.getRadioGroup().getButtons().get(2).setChecked(true);
                    }
                    else if(value>70){
                        adultFormulationofHRZE.getRadioGroup().getButtons().get(3).setChecked(true);
                    }
                }else{
                    currentTabletsofE.getRadioGroup().clearCheck();
                    currentTabletsofRHZ.getRadioGroup().clearCheck();
                    newTabletsofRHZ.getRadioGroup().clearCheck();
                    newTabletsofE.getRadioGroup().clearCheck();
                    adultFormulationofHRZE.getRadioGroup().clearCheck();
                    weightPercentileEditText.getEditText().setText("");
                }
            }
        });

        weightPercentileEditText.getEditText().setKeyListener(null);

        resetViews();

        mobileNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==4){
                    mobileNumber2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobileNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    mobileNumber1.requestFocus();
                    mobileNumber1.setSelection(mobileNumber1.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void updateDisplay() {

        String personDOB = App.getPatient().getPerson().getBirthdate();
        Calendar maxDateCalender = formDateCalendar.getInstance();
        maxDateCalender.setTime(formDateCalendar.getTime());
        maxDateCalender.add(Calendar.YEAR, 2);

        if (snackbar != null)
            snackbar.dismiss();
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
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            }else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            Calendar requiredDate = formDateCalendar.getInstance();
            requiredDate.setTime(formDateCalendar.getTime());
            requiredDate.add(Calendar.DATE, 30);

            if (requiredDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                forthDateCalender.setTime(requiredDate.getTime());
            } else {
                requiredDate.add(Calendar.DATE, 1);
                forthDateCalender.setTime(requiredDate.getTime());
            }

        } if (!regDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString())) {

            //
            // +Date date = App.stringToDate(sampleSubmitDate.getButton().getText().toString(), "dd-MMM-yyyy");

            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                regDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }
        if (!iptStartDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString())) {
            String formDa = formDate.getButton().getText().toString();
            if (thirdDateCalendar.after(App.getCalendar(date))) {

                thirdDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else if (thirdDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {

                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.ctb_form_date_less_than_patient_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                iptStartDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
            }else
                iptStartDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
        }
        String nextAppointmentDateString = App.getSqlDate(forthDateCalender);
        Date nextAppointmentDate = App.stringToDate(nextAppointmentDateString, "yyyy-MM-dd");

        String formDateString = App.getSqlDate(formDateCalendar);
        Date formStDate = App.stringToDate(formDateString, "yyyy-MM-dd");


        if (!(returnVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", forthDateCalender).toString()))) {
            Calendar dateToday = Calendar.getInstance();
            dateToday.add(Calendar.MONTH, 24);

            String formDa = returnVisitDate.getButton().getText().toString();

            if (forthDateCalender.before(formDateCalendar)) {

                forthDateCalender = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", forthDateCalender).toString());

            }
            else if(forthDateCalender.after(dateToday)){
                forthDateCalender = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_date_cant_be_greater_than_24_months), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", forthDateCalender).toString());
            }

            else if(nextAppointmentDate.compareTo(formStDate) == 0){
                forthDateCalender = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_start_date_and_next_visit_date_cant_be_same), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", forthDateCalender).toString());
            }
            else
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", forthDateCalender).toString());

        }
        formDate.getButton().setEnabled(true);
        regDate.getButton().setEnabled(true);
        iptStartDate.getButton().setEnabled(true);
        returnVisitDate.getButton().setEnabled(true);
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
                cnic1.setError(getString(R.string.empty_field));
                cnic1.requestFocus();
                error = true;
            }

            if (App.get(cnic2).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cnic2.setError(getString(R.string.empty_field));
                cnic2.requestFocus();
                error = true;
            }

            if (App.get(cnic3).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cnic3.setError(getString(R.string.empty_field));
                cnic3.requestFocus();
                error = true;
            }

            if (App.get(cnic1).length() != 5) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cnic1.setError(getString(R.string.length_message));
                cnic1.requestFocus();
                error = true;
            }

            if (App.get(cnic2).length() != 7) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cnic2.setError(getString(R.string.length_message));
                cnic2.requestFocus();
                error = true;
            }

            if (App.get(cnic3).length() != 1) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cnic3.setError(getString(R.string.length_message));
                cnic3.requestFocus();
                error = true;
            }

        }
        if (cnicOwnerOther.getVisibility() == View.VISIBLE) {
            if(App.get(cnicOwnerOther).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cnicOwnerOther.getEditText().setError(getString(R.string.empty_field));
                cnicOwnerOther.getEditText().requestFocus();
                error = true;
            }
            else if(App.get(cnicOwnerOther).trim().length() <= 0){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cnicOwnerOther.getEditText().setError(getString(R.string.ctb_spaces_only));
                cnicOwnerOther.getEditText().requestFocus();
                error = true;
            }
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
        if (tbRegisterationNumber.getVisibility() == View.VISIBLE) {
            if(!App.get(tbRegisterationNumber).isEmpty() && App.get(tbRegisterationNumber).trim().length() <= 0){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                tbRegisterationNumber.getEditText().setError(getString(R.string.ctb_spaces_only));
                tbRegisterationNumber.getEditText().requestFocus();
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
                mobileNumber1.setError(getString(R.string.empty_field));
                mobileNumber1.requestFocus();
                error = true;
            }
            if(App.get(mobileNumber2).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                mobileNumber2.setError(getString(R.string.empty_field));
                mobileNumber2.requestFocus();
                error = true;
            }
            if(!App.get(mobileNumber1).isEmpty() && !App.get(mobileNumber2).isEmpty()){
                String mobileNumber = App.get(mobileNumber1) + App.get(mobileNumber2);
                if(!RegexUtil.isMobileNumber(mobileNumber)){
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    mobileNumber2.setError(getString(R.string.ctb_invalid_number));
                    mobileNumber2.requestFocus();
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
        if(!App.get(doctorNotes).isEmpty() && App.get(doctorNotes).trim().length() <= 0){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            doctorNotes.getEditText().setError(getString(R.string.ctb_spaces_only));
            doctorNotes.getEditText().requestFocus();
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

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(),R.style.dialog).create();
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

                    /*Toast.makeText(context, getString(R.string.form_does_not_exist),
                            Toast.LENGTH_LONG).show();*/

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

        observations.add(new String[]{"PATIENT HAVE TB", App.get(patientHaveTb).toUpperCase()});

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

        if(regDate.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"REGISTRATION DATE", App.getSqlDateTime(secondDateCalendar)});
        }
        if(cnicLinearLayout.getVisibility()==View.VISIBLE){
            String cnicNumber = App.get(cnic1) +"-"+ App.get(cnic2) +"-"+ App.get(cnic3);
            if(RegexUtil.isValidNIC(cnicNumber)) {
                observations.add(new String[]{"NATIONAL IDENTIFICATION NUMBER", cnicNumber});

                personAttribute.put("National ID",cnicNumber);
            }
        }
        if (cnicOwner.getVisibility() == View.VISIBLE) {
            String ownerString = App.get(cnicOwner).equals(getResources().getString(R.string.ctb_mother)) ? "MOTHER" :
                    (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_father)) ? "FATHER" :
                            (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_brother)) ? "BROTHER" :
                                    (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_sister)) ? "SISTER" :
                                            (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_uncle)) ? "UNCLE" :
                                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_aunt)) ? "AUNT" : "OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER")))))))));

            observations.add(new String[]{"COMPUTERIZED NATIONAL IDENTIFICATION OWNER", ownerString});
            if(cnicOwner.isEnabled()) {
                String[][] cnicOwnerConcept = serverService.getConceptUuidAndDataType(ownerString);
                personAttribute.put("National ID Owner", cnicOwnerConcept[0][0]);
            }
        }
        if(cnicOwnerOther.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER", App.get(cnicOwnerOther)});
        }

        if(tbRegisterationNumber.getVisibility()==View.VISIBLE)
            observations.add(new String[]{"TB REGISTRATION NUMBER", App.get(tbRegisterationNumber)});

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
                            (App.get(patientType).equals(getResources().getString(R.string.ctb_referred_transferred)) ? "PATIENT REFERRED" :
                                    (App.get(patientType).equals(getResources().getString(R.string.ctb_treatment_after_followup)) ? "LOST TO FOLLOW-UP" :
                                            (App.get(patientType).equals(getResources().getString(R.string.ctb_treatment_failure)) ? "TUBERCULOSIS TREATMENT FAILURE" : "OTHER PATIENT TYPE"))))});


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

        if(testConfirmingOthers.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"OTHER DIAGNOSIS", App.get(testConfirmingOthers)});
        }

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
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_calpol)))
                    additionalTreatmentString = additionalTreatmentString + "CALPOL" + " ; ";
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
            observations.add(new String[]{"REGIMEN", App.get(regimen).equals(getResources().getString(R.string.ctb_rhze)) ? "RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL" : "RIFAMPICIN/ISONIAZID/PYRAZINAMIDE"});
        }

        if(typeFixedDosePrescribed.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"PAEDIATRIC DOSE COMBINATION", App.get(typeFixedDosePrescribed).equals(getResources().getString(R.string.ctb_current_formulation)) ? "CURRENT FORMULATION" :
                    App.get(typeFixedDosePrescribed).equals(getResources().getString(R.string.ctb_new_formulation)) ? "NEW FORMULATION"
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
        if(acceptanceToIpt.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"PATIENT ACCEPTANCE TO IPT", App.get(acceptanceToIpt).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        }
        if(iptStartDate.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"IPT START DATE", App.getSqlDateTime(thirdDateCalendar)});
        }
        if(iptDose.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"IPT DOSE", App.get(iptDose).equals(getResources().getString(R.string.ctb_quater_per_day)) ? "1/4 TAB ONCE A DAY" :
                    (App.get(iptDose).equals(getResources().getString(R.string.ctb_half_per_day)) ? "1/2 TAB ONCE A DAY" :
                            "1 TAB ONCE A DAY")});
        }

        if(iptRegNo.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"IPT REGISTRATION NUMBER", App.get(iptRegNo)});
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
                    id = serverService.saveFormLocally("Childhood TB-Treatment Initiation", form, formDateCalendar,observations.toArray(new String[][]{}));

                String result = "";

                result = serverService.saveMultiplePersonAttribute(personAttribute, id);
                if (!result.equals("SUCCESS"))
                    return result;

                result = serverService.saveEncounterAndObservationTesting("Childhood TB-Treatment Initiation", form, formDateCalendar, observations.toArray(new String[][]{}),id);
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
                cnic1.setText(cnicString[0]);
                cnic2.setText(cnicString[1]);
                cnic3.setText(cnicString[2]);
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
            }else if (obs[0][0].equals("TB PATIENT TYPE")) {
                String value = obs[0][1].equals("NEW TB PATIENT") ? getResources().getString(R.string.ctb_new) :
                        (obs[0][1].equals("RELAPSE") ? getResources().getString(R.string.ctb_relapse) :
                                (obs[0][1].equals("PATIENT REFERRED") ? getResources().getString(R.string.ctb_referred_transferred) :
                                        (obs[0][1].equals("LOST TO FOLLOW-UP") ? getResources().getString(R.string.ctb_treatment_after_followup) :
                                                (obs[0][1].equals("TUBERCULOSIS TREATMENT FAILURE") ? getResources().getString(R.string.ctb_treatment_failure) :
                                                        getResources().getString(R.string.ctb_other_title)))));

                patientType.getSpinner().selectValue(value);
                patientType.setVisibility(View.VISIBLE);
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
                    }else if (cb.getText().equals(getResources().getString(R.string.ctb_calpol)) && obs[0][1].equals("CALPOL")) {
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
            }
            else if (obs[0][0].equals("WEIGHT PERCENTILE GROUP")) {
                weightPercentileEditText.getEditText().setText(obs[0][1]);
            }else if (obs[0][0].equals("REGIMEN")) {
                for (RadioButton rb : regimen.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_rhze)) && obs[0][1].equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE/ETHAMBUTOL")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_rhz)) && obs[0][1].equals("RIFAMPICIN/ISONIAZID/PYRAZINAMIDE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                regimen.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("PAEDIATRIC DOSE COMBINATION")) {
                String value = obs[0][1].equals("CURRENT FORMULATION") ? getResources().getString(R.string.ctb_current_formulation) :
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
                mobileNumber1.setText(mobileNumber[0]);
                mobileNumber2.setText(mobileNumber[1]);
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
            }
            else if (obs[0][0].equals("PATIENT ACCEPTANCE TO IPT")) {
                for (RadioButton rb : acceptanceToIpt.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                acceptanceToIpt.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("IPT START DATE")) {
                String thirdDate = obs[0][1];
                thirdDateCalendar.setTime(App.stringToDate(thirdDate, "yyyy-MM-dd"));
                iptStartDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
                iptStartDate.setVisibility(View.VISIBLE);
            }  else if (obs[0][0].equals("IPT DOSE")) {
                for (RadioButton rb : iptDose.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_quater_per_day)) && obs[0][1].equals("1/4 TAB ONCE A DAY")) {
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
            }
            else if (obs[0][0].equals("IPT REGISTRATION NUMBER")) {
                iptRegNo.getEditText().setText(obs[0][1]);
                iptRegNo.setVisibility(View.VISIBLE);
            }
            else if(obs[0][0].equals("ADDITIONAL TREATMENT IPT PATIENT")) {
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
            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar,false,true, false);
            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");*/
        }
        if (view == regDate.getButton()) {
            regDate.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar,false,true, true);
            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");*/
        }
        if (view == iptStartDate.getButton()) {
            iptStartDate.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar,false,true, true);
            /*Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");*/
        }
        if (view == returnVisitDate.getButton()) {
            returnVisitDate.getButton().setEnabled(false);
            showDateDialog(forthDateCalender,true,false, true);
            /*Bundle args = new Bundle();
            args.putInt("type", FORTH_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", false);
            args.putBoolean("allowFutureDate", true);
            forthDateFragment.setArguments(args);
            forthDateFragment.show(getFragmentManager(), "DatePicker");*/
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
        else if (spinner == patientType.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_referred_transferred))) {
                treatmentInitiated.setVisibility(View.GONE);
                initiatingAdditionalTreatment.setVisibility(View.GONE);
                reasonTreatmentNotIniated.setVisibility(View.GONE);
                patientCategory.setVisibility(View.GONE);
                weight.setVisibility(View.GONE);
                regimen.setVisibility(View.GONE);
                typeFixedDosePrescribed.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
                newTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                adultFormulationofHRZE.setVisibility(View.GONE);
                nameOfSupporter.setVisibility(View.GONE);
                mobileLinearLayout.setVisibility(View.GONE);
                closeContactTypeTreatmentSupport.setVisibility(View.GONE);
                otherCloseContactSupporterType.setVisibility(View.GONE);
            } else {
                treatmentInitiated.setVisibility(View.VISIBLE);
                initiatingAdditionalTreatment.setVisibility(View.VISIBLE);
                if(App.get(treatmentInitiated).equals(getResources().getString(R.string.no))) {
                    reasonTreatmentNotIniated.setVisibility(View.VISIBLE);
                }
                patientCategory.setVisibility(View.VISIBLE);
                weight.setVisibility(View.VISIBLE);
                regimen.setVisibility(View.VISIBLE);
                typeFixedDosePrescribed.setVisibility(View.VISIBLE);
                currentTabletsofE.setVisibility(View.VISIBLE);
                currentTabletsofRHZ.setVisibility(View.VISIBLE);
                newTabletsofE.setVisibility(View.VISIBLE);
                newTabletsofRHZ.setVisibility(View.VISIBLE);
                adultFormulationofHRZE.setVisibility(View.VISIBLE);
                nameOfSupporter.setVisibility(View.VISIBLE);
                if(!App.get(nameOfSupporter).isEmpty()) {
                    mobileLinearLayout.setVisibility(View.VISIBLE);
                }
                closeContactTypeTreatmentSupport.setVisibility(View.VISIBLE);
                if(App.get(closeContactTypeTreatmentSupport).equals(getResources().getString(R.string.ctb_other_title))){
                    otherCloseContactSupporterType.setVisibility(View.VISIBLE);
                }
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
            typeOfDiagnosis.getQuestionView().setError(null);
        }


    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        iptStartDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
        iptStartDate.setVisibility(View.GONE);
        returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", forthDateCalender).toString());
        nameConsultant.setVisibility(View.GONE);
        reasonConsultation.setVisibility(View.GONE);
        tbDaignosed.setVisibility(View.GONE);
        typeOfDiagnosis.setVisibility(View.GONE);
        radiologicalEvidence.setVisibility(View.GONE);
        histopathologicalEvidence.setVisibility(View.GONE);
        regDate.setVisibility(View.GONE);
        cnicLinearLayout.setVisibility(View.GONE);
        cnicOwner.setVisibility(View.GONE);
        cnicOwnerOther.setVisibility(View.GONE);
        tbRegisterationNumber.setVisibility(View.GONE);
        tbType.setVisibility(View.GONE);
        extraPulmonarySite.setVisibility(View.GONE);
        extraPulmonarySiteOther.setVisibility(View.GONE);
        patientType.setVisibility(View.GONE);
        testConfirmingDiagnosis.setVisibility(View.GONE);
        testConfirmingOthers.setVisibility(View.GONE);
        treatmentInitiated.setVisibility(View.GONE);
        reasonTreatmentNotIniated.setVisibility(View.GONE);
        patientCategory.setVisibility(View.GONE);
        weight.setVisibility(View.GONE);
        weightPercentileEditText.setVisibility(View.GONE);
        regimen.setVisibility(View.GONE);
        typeFixedDosePrescribed.setVisibility(View.GONE);
        initiatingAdditionalTreatment.setVisibility(View.GONE);
        currentTabletsofRHZ.setVisibility(View.GONE);
        currentTabletsofE.setVisibility(View.GONE);
        newTabletsofRHZ.setVisibility(View.GONE);
        newTabletsofE.setVisibility(View.GONE);
        adultFormulationofHRZE.setVisibility(View.GONE);
        iptRegNo.setVisibility(View.GONE);
        nameOfSupporter.setVisibility(View.GONE);
        mobileLinearLayout.setVisibility(View.GONE);
        closeContactTypeTreatmentSupport.setVisibility(View.GONE);
        otherCloseContactSupporterType.setVisibility(View.GONE);
        bcgScar.setVisibility(View.GONE);
        tbHistoryIn2Years.setVisibility(View.GONE);
        eligibleForIpt.setVisibility(View.GONE);
        acceptanceToIpt.setVisibility(View.GONE);
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
                String cnic1 = serverService.getLatestObsValue(App.getPatientId(), "Patient Information", "NATIONAL IDENTIFICATION NUMBER");
                String cnicowner1 = serverService.getLatestObsValue(App.getPatientId(), "Patient Information", "COMPUTERIZED NATIONAL IDENTIFICATION OWNER");
                String cnicownerother1 = serverService.getLatestObsValue(App.getPatientId(), "Patient Information", "OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER");

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
                return result;
            }

            @Override
            protected void onProgressUpdate(String... values) {
            }


            @Override
            protected void onPostExecute(HashMap<String, String> result) {
                super.onPostExecute(result);
                loading.dismiss();

                if (result.get("NATIONAL IDENTIFICATION NUMBER") != null && RegexUtil.isValidNIC(result.get("NATIONAL IDENTIFICATION NUMBER"))) {
                    String value = result.get("NATIONAL IDENTIFICATION NUMBER");
                    cnic1.setText(value.substring(0, 5));
                    cnic1.setKeyListener(null);
                    cnic2.setText(value.substring(6, 13));
                    cnic2.setKeyListener(null);
                    cnic3.setText(value.substring(14));
                    cnic3.setKeyListener(null);
                }

                if (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER") != null) {
                    cnicOwner.getSpinner().selectValue(result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER"));
                    cnicOwner.getSpinner().setEnabled(false);
                }

                if (result.get("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER") != null) {
                    cnicOwnerOther.getEditText().setText(result.get("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER"));
                    cnicOwnerOther.getEditText().setKeyListener(null);
                }
            }
        };
        autopopulateFormTask.execute("");
        String referralTransferLocation = serverService.getLatestObsValue(App.getPatientId(), "Clinician Evaluation", "WEIGHT (KG)");
        if(referralTransferLocation!=null){
            weightAtBaseline.getEditText().setText(referralTransferLocation);
            double weightValue = Double.parseDouble(referralTransferLocation);
            if(weightValue<2.5){
                iptDose.getRadioGroup().getButtons().get(0).setChecked(true);
            }else if(weightValue>2.5 && weightValue<5.0){
                iptDose.getRadioGroup().getButtons().get(1).setChecked(true);
            }else if(weightValue>5.0){
                iptDose.getRadioGroup().getButtons().get(2).setChecked(true);
            }
        }
        weightAtBaseline.getEditText().setKeyListener(null);

        String startDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Patient Information");
        String format = "";
        if(startDate!=null) {
            if (startDate.contains("/")) {
                format = "dd/MM/yyyy";
            } else {
                format = "yyyy-MM-dd";
            }
            secondDateCalendar.setTime(App.stringToDate(startDate, format));
            regDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }



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

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            updateDisplay();
        }

    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == patientHaveTb.getRadioGroup()) {
            patientHaveTb.getRadioGroup().getButtons().get(2).setError(null);
            int patientAge = App.getPatient().getPerson().getAge();
            if (patientHaveTb.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                typeOfDiagnosis.setVisibility(View.VISIBLE);
                for (CheckBox cb : typeOfDiagnosis.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ctb_clinically_diagnosed))) {
                        if (cb.isChecked())
                        {
                            histopathologicalEvidence.setVisibility(View.VISIBLE);
                            radiologicalEvidence.setVisibility(View.VISIBLE);
                        }
                    }
                }
                regDate.setVisibility(View.VISIBLE);
                cnicLinearLayout.setVisibility(View.VISIBLE);
                String cnicString = App.get(cnic1) + "-" + App.get(cnic2) + "-" + App.get(cnic3);
                if(RegexUtil.isValidNIC(cnicString)){
                    if(App.get(patientHaveTb).equals(getResources().getString(R.string.yes))) {
                        cnicOwner.setVisibility(View.VISIBLE);
                        if (App.get(cnicOwner).equalsIgnoreCase(getResources().getString(R.string.ctb_other_title))) {
                            cnicOwnerOther.setVisibility(View.VISIBLE);
                        }
                    }
                }else{
                    cnicOwner.setVisibility(View.GONE);
                    cnicOwnerOther.setVisibility(View.GONE);
                }
                tbRegisterationNumber.setVisibility(View.VISIBLE);
                tbType.setVisibility(View.VISIBLE);
                patientType.setVisibility(View.VISIBLE);
                testConfirmingDiagnosis.setVisibility(View.VISIBLE);
                for (CheckBox cb : testConfirmingDiagnosis.getCheckedBoxes()) {
                    if (App.get(cb).equals(getResources().getString(R.string.ctb_other_title))) {
                        if (cb.isChecked()) {
                            testConfirmingOthers.setVisibility(View.VISIBLE);
                        } else {
                            testConfirmingOthers.setVisibility(View.GONE);
                        }
                    }
                    typeOfDiagnosis.getQuestionView().setError(null);
                }
                treatmentInitiated.setVisibility(View.VISIBLE);
                if(App.get(treatmentInitiated).equals(getResources().getString(R.string.no))){
                    reasonTreatmentNotIniated.setVisibility(View.VISIBLE);
                }
                patientCategory.setVisibility(View.VISIBLE);
                weight.setVisibility(View.VISIBLE);
                weightPercentileEditText.setVisibility(View.VISIBLE);
                regimen.setVisibility(View.VISIBLE);
                typeFixedDosePrescribed.setVisibility(View.VISIBLE);
                currentTabletsofRHZ.setVisibility(View.VISIBLE);
                initiatingAdditionalTreatment.setVisibility(View.VISIBLE);
                nameOfSupporter.setVisibility(View.VISIBLE);
                closeContactTypeTreatmentSupport.setVisibility(View.VISIBLE);

                bcgScar.setVisibility(View.GONE);
                tbHistoryIn2Years.setVisibility(View.GONE);
                eligibleForIpt.setVisibility(View.GONE);
                iptStartDate.setVisibility(View.GONE);
                iptRegNo.setVisibility(View.GONE);
                iptDose.setVisibility(View.GONE);
                initiatingAdditionalTreatmentIpt.setVisibility(View.GONE);
                precribingAntibioticTrial.setVisibility(View.GONE);
                precribingFurthertest.setVisibility(View.GONE);
                initiatingAdditionalTreatmentAntibiotic.setVisibility(View.GONE);
            }else if(patientHaveTb.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.no))){
                if(patientAge <=5){
                    String bcgScarValue = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-Presumptive Case Confirmation", "BACILLUS CALMETTE–GUÉRIN VACCINE");
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
                    String contactHistory2Year = serverService.getLatestObsValue(App.getPatientId(),"Childhood TB-Presumptive Case Confirmation", "PATIENT IS CONTACT OF KNOWN OR SUSPECTED SUSPICIOUS CASE IN PAST 2 YEARS");
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
                    iptRegNo.setVisibility(View.VISIBLE);
                    if(!App.get(weightAtBaseline).isEmpty()){
                        double weightInt = Double.parseDouble(App.get(weightAtBaseline));
                        if(weightInt<2.5){
                            iptDose.getRadioGroup().getButtons().get(0).setChecked(true);
                        }else if(weightInt>2.5 && weightInt<5){
                            iptDose.getRadioGroup().getButtons().get(1).setChecked(true);
                        }else if(weightInt>5 && weightInt<10){
                            iptDose.getRadioGroup().getButtons().get(2).setChecked(true);
                        }
                    }
                    initiatingAdditionalTreatmentIpt.setVisibility(View.VISIBLE);
                }
                else{
                    bcgScar.setVisibility(View.GONE);
                    tbHistoryIn2Years.setVisibility(View.GONE);
                    eligibleForIpt.setVisibility(View.GONE);
                    iptStartDate.setVisibility(View.GONE);
                    iptDose.setVisibility(View.GONE);
                    iptRegNo.setVisibility(View.GONE);
                    initiatingAdditionalTreatmentIpt.setVisibility(View.GONE);
                }
                regDate.setVisibility(View.GONE);
                typeOfDiagnosis.setVisibility(View.GONE);
                radiologicalEvidence.setVisibility(View.GONE);
                histopathologicalEvidence.setVisibility(View.GONE);
                cnicLinearLayout.setVisibility(View.GONE);
                cnicOwner.setVisibility(View.GONE);
                cnicOwnerOther.setVisibility(View.GONE);
                tbRegisterationNumber.setVisibility(View.GONE);
                nameOfSupporter.setVisibility(View.GONE);
                tbType.setVisibility(View.GONE);
                extraPulmonarySite.setVisibility(View.GONE);
                extraPulmonarySiteOther.setVisibility(View.GONE);
                patientType.setVisibility(View.GONE);
                testConfirmingDiagnosis.setVisibility(View.GONE);
                testConfirmingOthers.setVisibility(View.GONE);
                treatmentInitiated.setVisibility(View.GONE);
                initiatingAdditionalTreatment.setVisibility(View.GONE);
                reasonTreatmentNotIniated.setVisibility(View.GONE);
                patientCategory.setVisibility(View.GONE);
                weight.setVisibility(View.GONE);
                weightPercentileEditText.setVisibility(View.GONE);
                regimen.setVisibility(View.GONE);
                typeFixedDosePrescribed.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
                newTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                adultFormulationofHRZE.setVisibility(View.GONE);
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
                typeOfDiagnosis.setVisibility(View.GONE);
                radiologicalEvidence.setVisibility(View.GONE);
                histopathologicalEvidence.setVisibility(View.GONE);
                cnicLinearLayout.setVisibility(View.GONE);
                tbRegisterationNumber.setVisibility(View.GONE);
                cnicOwner.setVisibility(View.GONE);
                cnicOwnerOther.setVisibility(View.GONE);
                tbType.setVisibility(View.GONE);
                extraPulmonarySite.setVisibility(View.GONE);
                extraPulmonarySiteOther.setVisibility(View.GONE);
                patientType.setVisibility(View.GONE);
                testConfirmingDiagnosis.setVisibility(View.GONE);
                testConfirmingOthers.setVisibility(View.GONE);
                treatmentInitiated.setVisibility(View.GONE);
                reasonTreatmentNotIniated.setVisibility(View.GONE);
                patientCategory.setVisibility(View.GONE);
                weight.setVisibility(View.GONE);
                weightPercentileEditText.setVisibility(View.GONE);
                regimen.setVisibility(View.GONE);
                typeFixedDosePrescribed.setVisibility(View.GONE);
                currentTabletsofE.setVisibility(View.GONE);
                currentTabletsofRHZ.setVisibility(View.GONE);
                newTabletsofE.setVisibility(View.GONE);
                newTabletsofRHZ.setVisibility(View.GONE);
                adultFormulationofHRZE.setVisibility(View.GONE);
                initiatingAdditionalTreatment.setVisibility(View.GONE);
                nameOfSupporter.setVisibility(View.GONE);
                closeContactTypeTreatmentSupport.setVisibility(View.GONE);
                bcgScar.setVisibility(View.GONE);
                tbHistoryIn2Years.setVisibility(View.GONE);
                eligibleForIpt.setVisibility(View.GONE);
                iptStartDate.setVisibility(View.GONE);
                iptDose.setVisibility(View.GONE);
                iptRegNo.setVisibility(View.GONE);
                initiatingAdditionalTreatmentIpt.setVisibility(View.GONE);
            }
        } else if (group == treatmentInitiated.getRadioGroup()) {
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
        }
        else if (group == eligibleForIpt.getRadioGroup()) {
            if (eligibleForIpt.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                acceptanceToIpt.setVisibility(View.VISIBLE);
                if(App.get(acceptanceToIpt).equals(getResources().getString(R.string.yes))){
                    iptStartDate.setVisibility(View.VISIBLE);
                    iptDose.setVisibility(View.VISIBLE);
                }
            } else {
                acceptanceToIpt.setVisibility(View.GONE);
                iptStartDate.setVisibility(View.GONE);
                iptDose.setVisibility(View.GONE);
            }
        }
        else if (group == acceptanceToIpt.getRadioGroup()) {
            if (acceptanceToIpt.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                iptStartDate.setVisibility(View.VISIBLE);
                iptDose.setVisibility(View.VISIBLE);
            } else {
                iptStartDate.setVisibility(View.GONE);
                iptDose.setVisibility(View.GONE);
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
        else if (group == patientCategory.getRadioGroup()) {
            if (patientCategory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_category_3))) {
                regimen.getRadioGroup().getButtons().get(1).setChecked(true);
            }
        }

        else if (group == regimen.getRadioGroup()) {
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
