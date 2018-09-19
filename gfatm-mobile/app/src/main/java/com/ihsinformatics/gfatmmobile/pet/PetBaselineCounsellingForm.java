package com.ihsinformatics.gfatmmobile.pet;

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
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class PetBaselineCounsellingForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;

    TitledSpinner ethinicity;
    TitledEditText otherEthinicity;
    TitledSpinner contactEducationLevel;
    TitledEditText otherContactEducationLevel;
    TitledSpinner maritalStatus;
    TitledSpinner emloyementStatus;
    TitledSpinner occupation;
    TitledEditText otherOccupation;

    TitledRadioGroup familyStructure;
    TitledEditText numberOfFamilyMembers;
    TitledEditText earningMembers;
    TitledEditText contactIncome;
    TitledEditText contactIncomeGroup;
    TitledRadioGroup residenceType;
    TitledEditText noOfRooms;
    TitledCheckBoxes medicalCondition;
    TitledEditText otherMedicalCondition;

    TitledCheckBoxes counsellingProvidedTo;
    TitledEditText counsellingProvidedToOther;

    TitledCheckBoxes counsellingRegarding;
    CheckBox procedure;
    TitledEditText procedureExplanation;
    CheckBox purpose;
    TitledEditText purposeExplanation;
    CheckBox durationOfPet;
    TitledEditText durationOfPetExplanation;
    CheckBox infectionControlMeasures;
    TitledEditText infectionControlMeasuresExplanation;
    CheckBox transmission;
    TitledEditText transmissionExplanation;
    CheckBox adherence;
    TitledEditText adherenceExplanation;
    CheckBox nutrition;
    TitledEditText nutritionExplanation;
    CheckBox commonAdverseEffects;
    TitledEditText commonAdverseEffectsExplanation;
    CheckBox misconception;
    TitledEditText misconceptionExplanation;
    CheckBox followup;
    TitledEditText followupExplanation;
    TitledEditText questionsByContact;
    TitledEditText psycologistAnswer;
    TitledSpinner contactBehavior;
    TitledEditText otherBehavior;
    TitledEditText caretakerComments;
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

        pageCount = 4;
        formName = Forms.PET_BASELINE_COUNSELLING;
        form = Forms.pet_baselineCounselling;

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

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");

        ethinicity = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_ethnicity), getResources().getStringArray(R.array.pet_ethnicities), "", App.VERTICAL);
        otherEthinicity = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        contactEducationLevel = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_contact_education_level), getResources().getStringArray(R.array.pet_contact_education_levels), getResources().getString(R.string.pet_intermediate), App.VERTICAL);
        otherContactEducationLevel = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        maritalStatus = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_martial_status), getResources().getStringArray(R.array.pet_martial_statuses), getResources().getString(R.string.pet_single), App.VERTICAL);
        emloyementStatus = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_employement_status), getResources().getStringArray(R.array.pet_employement_statuses), getResources().getString(R.string.pet_employed), App.VERTICAL);
        occupation = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_occupation), getResources().getStringArray(R.array.pet_occupations), getString(R.string.pet_artist), App.VERTICAL);
        otherOccupation = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        familyStructure = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_family_structure), getResources().getStringArray(R.array.pet_family_structures), getResources().getString(R.string.pet_joint_family), App.HORIZONTAL, App.HORIZONTAL);
        numberOfFamilyMembers = new TitledEditText(context, null, getResources().getString(R.string.pet_members_number), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        earningMembers = new TitledEditText(context, null, getResources().getString(R.string.pet_earning_members), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        contactIncome = new TitledEditText(context, null, getResources().getString(R.string.pet_contact_income), "0", "", 9, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        contactIncomeGroup = new TitledEditText(context, null, getResources().getString(R.string.pet_contact_income_group), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        residenceType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_residence_type), getResources().getStringArray(R.array.pet_residence_types), getResources().getString(R.string.pet_rent), App.HORIZONTAL, App.VERTICAL);
        noOfRooms = new TitledEditText(context, null, getResources().getString(R.string.pet_room_nos), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        medicalCondition = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_contact_psychiatric_medical_condition), getResources().getStringArray(R.array.pet_contact_psychiatric_medical_conditions), null, App.VERTICAL, App.VERTICAL);
        otherMedicalCondition = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        // second page views...
        MyLinearLayout linearLayout = new MyLinearLayout(context, getResources().getString(R.string.pet_counselling_procedure), App.VERTICAL);
        counsellingProvidedTo = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_counselling_to), getResources().getStringArray(R.array.pet_cnic_owners), null, App.VERTICAL, App.VERTICAL);
        counsellingProvidedToOther = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        linearLayout.addView(counsellingProvidedTo);
        linearLayout.addView(counsellingProvidedToOther);

        // third page views...
        counsellingRegarding = new TitledCheckBoxes(context, null, getResources().getString(R.string.information_provided_during_counseling), getResources().getStringArray(R.array.information_provided_during_counseling_options), null, App.VERTICAL, App.VERTICAL);
        procedure = counsellingRegarding.getCheckBox(0);
        purpose = counsellingRegarding.getCheckBox(1);
        durationOfPet = counsellingRegarding.getCheckBox(2);
        infectionControlMeasures = counsellingRegarding.getCheckBox(3);
        transmission = counsellingRegarding.getCheckBox(4);
        adherence = counsellingRegarding.getCheckBox(5);
        nutrition = counsellingRegarding.getCheckBox(6);
        commonAdverseEffects = counsellingRegarding.getCheckBox(7);
        misconception = counsellingRegarding.getCheckBox(8);
        followup = counsellingRegarding.getCheckBox(9);

        //procedure = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_procedure), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        procedureExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_procedure_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        procedureExplanation.getEditText().setSingleLine(false);
        procedureExplanation.getEditText().setMinimumHeight(150);
        //purpose = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_purpose), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        purposeExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_purpose_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        purposeExplanation.getEditText().setSingleLine(false);
        purposeExplanation.getEditText().setMinimumHeight(150);
        //durationOfPet = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_duration_of_pet), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        durationOfPetExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_duration_of_pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        durationOfPetExplanation.getEditText().setSingleLine(false);
        durationOfPetExplanation.getEditText().setMinimumHeight(150);
        //infectionControlMeasures = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_infection_control_measures), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        infectionControlMeasuresExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_infection_control_measures_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        infectionControlMeasuresExplanation.getEditText().setSingleLine(false);
        infectionControlMeasuresExplanation.getEditText().setMinimumHeight(150);
        //transmission = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_transmission), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        transmissionExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_transmission_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        transmissionExplanation.getEditText().setSingleLine(false);
        transmissionExplanation.getEditText().setMinimumHeight(150);
        //adherence = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adherence), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        adherenceExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_adherence_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        adherenceExplanation.getEditText().setSingleLine(false);
        adherenceExplanation.getEditText().setMinimumHeight(150);
        //nutrition = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_nutrition), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        nutritionExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_nutrition_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        nutritionExplanation.getEditText().setSingleLine(false);
        nutritionExplanation.getEditText().setMinimumHeight(150);
        //commonAdverseEffects = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_common_adverse_effects), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        commonAdverseEffectsExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_common_adverse_effects_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        commonAdverseEffectsExplanation.getEditText().setSingleLine(false);
        commonAdverseEffectsExplanation.getEditText().setMinimumHeight(150);
        //misconception = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_misconception), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        misconceptionExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_misconception_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        misconceptionExplanation.getEditText().setSingleLine(false);
        misconceptionExplanation.getEditText().setMinimumHeight(150);
        //followup = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_followup), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        followupExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_followup_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        followupExplanation.getEditText().setSingleLine(false);
        followupExplanation.getEditText().setMinimumHeight(150);
        questionsByContact = new TitledEditText(context, null, getResources().getString(R.string.pet_question_by_contact), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        questionsByContact.getEditText().setSingleLine(false);
        questionsByContact.getEditText().setMinimumHeight(150);
        psycologistAnswer = new TitledEditText(context, null, getResources().getString(R.string.pet_psychologist_answer), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        psycologistAnswer.getEditText().setSingleLine(false);
        psycologistAnswer.getEditText().setMinimumHeight(150);
        contactBehavior = new TitledSpinner(context, "", getResources().getString(R.string.pet_contact_behaviour), getResources().getStringArray(R.array.pet_contact_behaviours), "", App.VERTICAL);
        otherBehavior = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 70, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        caretakerComments = new TitledEditText(context, null, getResources().getString(R.string.pet_caretaker_comments), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        caretakerComments.getEditText().setSingleLine(false);
        caretakerComments.getEditText().setMinimumHeight(150);
        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.pet_psychologist_comment), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        clincianNote.getEditText().setSingleLine(false);
        clincianNote.getEditText().setMinimumHeight(150);

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

        // Used for reset fields...
        views = new View[]{formDate.getButton(), familyStructure.getRadioGroup(), numberOfFamilyMembers.getEditText(), earningMembers.getEditText(), contactIncomeGroup.getEditText(), counsellingProvidedTo, counsellingProvidedToOther.getEditText(),
                residenceType.getRadioGroup(), noOfRooms.getEditText(), medicalCondition,  procedureExplanation.getEditText(),  purposeExplanation.getEditText(),
                durationOfPetExplanation.getEditText(), infectionControlMeasuresExplanation.getEditText(), counsellingRegarding,
                transmissionExplanation.getEditText(),  adherenceExplanation.getEditText(),  nutritionExplanation.getEditText(),
                commonAdverseEffectsExplanation.getEditText(), misconceptionExplanation.getEditText(), followupExplanation.getEditText(),
                questionsByContact.getEditText(), psycologistAnswer.getEditText(), contactBehavior.getSpinner(), otherBehavior.getEditText(), contactIncome.getEditText(), clincianNote.getEditText(), caretakerComments.getEditText(),
                ethinicity.getSpinner(), otherEthinicity.getEditText(), contactEducationLevel.getSpinner(), maritalStatus.getSpinner(), emloyementStatus.getSpinner(), occupation.getSpinner(), referredTo, referalReasonPsychologist, otherReferalReasonPsychologist.getEditText(), referalReasonSupervisor, otherReferalReasonSupervisor.getEditText(),
                referalReasonCallCenter, otherReferalReasonCallCenter.getEditText(), referalReasonClinician, otherReferalReasonClinician.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate,  ethinicity, otherEthinicity, contactEducationLevel, otherContactEducationLevel, maritalStatus, emloyementStatus, occupation, otherOccupation, familyStructure, numberOfFamilyMembers, earningMembers, contactIncome, contactIncomeGroup, residenceType, noOfRooms, medicalCondition, otherMedicalCondition},
                        {linearLayout},
                        {counsellingRegarding, procedureExplanation, purposeExplanation, durationOfPetExplanation, infectionControlMeasuresExplanation,transmissionExplanation,adherenceExplanation, nutritionExplanation, commonAdverseEffectsExplanation, misconceptionExplanation, followupExplanation },
                        {questionsByContact,psycologistAnswer,contactBehavior,otherBehavior, caretakerComments, clincianNote, patientReferred, referredTo, referalReasonPsychologist, otherReferalReasonPsychologist, referalReasonSupervisor, otherReferalReasonSupervisor,
                                referalReasonCallCenter, otherReferalReasonCallCenter, referalReasonClinician, otherReferalReasonClinician}};

        numberOfFamilyMembers.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (App.get(numberOfFamilyMembers).equals(""))
                    numberOfFamilyMembers.getEditText().setError(null);
                else {
                    int no = Integer.parseInt(String.valueOf(App.get(numberOfFamilyMembers)));
                    if(no > 50)
                        numberOfFamilyMembers.getEditText().setError(getString(R.string.pet_invalid_member_number));
                    else
                        numberOfFamilyMembers.getEditText().setError(null);
                }


            }
        });

        earningMembers.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (App.get(earningMembers).equals(""))
                    earningMembers.getEditText().setError(null);
                else {
                    int no = Integer.parseInt(String.valueOf(App.get(earningMembers)));
                    if(no > 15)
                        earningMembers.getEditText().setError(getString(R.string.pet_invalid_earning_members));
                    else
                        earningMembers.getEditText().setError(null);
                }


            }
        });


        contactIncome.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (App.get(contactIncome).equals(""))
                    contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_none));
                else {
                    Float income = Float.parseFloat(App.get(contactIncome));
                    if (income == 0)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_none));
                    else if (income >= 1 && income <= 7000)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_lower_class));
                    else if (income >= 7001 && income <= 35000)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_lower_middle_class));
                    else if (income >= 35001 && income <= 87000)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_middle_class));
                    else if (income >= 87001 && income <= 173000)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_upper_middle_class));
                    else
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_upper_class));
                }


            }
        });
        contactIncomeGroup.getEditText().setKeyListener(null);

        formDate.getButton().setOnClickListener(this);
        contactBehavior.getSpinner().setOnItemSelectedListener(this);

        for (CheckBox cb : medicalCondition.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : counsellingProvidedTo.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        purpose.setOnCheckedChangeListener(this);
        procedure.setOnCheckedChangeListener(this);
        durationOfPet.setOnCheckedChangeListener(this);
        infectionControlMeasures.setOnCheckedChangeListener(this);
        transmission.setOnCheckedChangeListener(this);
        adherence.setOnCheckedChangeListener(this);
        nutrition.setOnCheckedChangeListener(this);
        commonAdverseEffects.setOnCheckedChangeListener(this);
        misconception.setOnCheckedChangeListener(this);
        followup.setOnCheckedChangeListener(this);
        ethinicity.getSpinner().setOnItemSelectedListener(this);
        occupation.getSpinner().setOnItemSelectedListener(this);
        contactEducationLevel.getSpinner().setOnItemSelectedListener(this);
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

        formDate.setEnabled(true);

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
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }
    }


    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        otherMedicalCondition.setVisibility(View.GONE);
        counsellingProvidedToOther.setVisibility(View.GONE);

        procedureExplanation.setVisibility(View.GONE);
        purposeExplanation.setVisibility(View.GONE);
        durationOfPetExplanation.setVisibility(View.GONE);
        infectionControlMeasuresExplanation.setVisibility(View.GONE);
        transmissionExplanation.setVisibility(View.GONE);
        adherenceExplanation.setVisibility(View.GONE);
        nutritionExplanation.setVisibility(View.GONE);
        commonAdverseEffectsExplanation.setVisibility(View.GONE);
        misconceptionExplanation.setVisibility(View.GONE);
        followupExplanation.setVisibility(View.GONE);
        otherEthinicity.setVisibility(View.GONE);
        otherContactEducationLevel.setVisibility(View.GONE);
        otherOccupation.setVisibility(View.GONE);

        if (App.getPatient().getPerson().getAge() < 15)
            contactBehavior.setVisibility(View.VISIBLE);
        else
            contactBehavior.setVisibility(View.GONE);

        referredTo.setVisibility(View.GONE);
        referalReasonPsychologist.setVisibility(View.GONE);
        otherReferalReasonPsychologist.setVisibility(View.GONE);
        referalReasonSupervisor.setVisibility(View.GONE);
        otherReferalReasonSupervisor.setVisibility(View.GONE);
        referalReasonCallCenter.setVisibility(View.GONE);
        otherReferalReasonCallCenter.setVisibility(View.GONE);
        referalReasonClinician.setVisibility(View.GONE);
        otherReferalReasonClinician.setVisibility(View.GONE);

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
    public boolean validate() {

        View view = null;
        Boolean error = false;

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

        if (App.get(clincianNote).isEmpty() && clincianNote.getVisibility() == View.VISIBLE) {
            clincianNote.getEditText().setError(getResources().getString(R.string.mandatory_field));
            clincianNote.getEditText().requestFocus();
            gotoLastPage();
            error = true;
        }

        if (App.get(psycologistAnswer).isEmpty() && psycologistAnswer.getVisibility() == View.VISIBLE) {
            psycologistAnswer.getEditText().setError(getResources().getString(R.string.mandatory_field));
            psycologistAnswer.getEditText().requestFocus();
            gotoLastPage();
            error = true;
        }

        if (App.get(questionsByContact).isEmpty() && questionsByContact.getVisibility() == View.VISIBLE) {
            questionsByContact.getEditText().setError(getResources().getString(R.string.mandatory_field));
            questionsByContact.getEditText().requestFocus();
            gotoLastPage();
            error = true;
        }

        if (App.get(otherBehavior).isEmpty() && otherBehavior.getVisibility() == View.VISIBLE) {
            otherBehavior.getEditText().setError(getResources().getString(R.string.mandatory_field));
            otherBehavior.getEditText().requestFocus();
            gotoLastPage();
            error = true;
        }

        if (App.get(counsellingProvidedToOther).isEmpty() && counsellingProvidedToOther.getVisibility() == View.VISIBLE) {
            counsellingProvidedToOther.getEditText().setError(getResources().getString(R.string.mandatory_field));
            counsellingProvidedToOther.getEditText().requestFocus();
            gotoPage(1);
            error = true;
        }

        flag = false;
        for (CheckBox cb : counsellingProvidedTo.getCheckedBoxes()) {
            if (cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            counsellingProvidedTo.getQuestionView().setError(getResources().getString(R.string.mandatory_field));
            counsellingProvidedToOther.getQuestionView().requestFocus();
            view = counsellingProvidedTo.getQuestionView();
            gotoPage(1);
            error = true;
        }

        if (App.get(noOfRooms).isEmpty() && noOfRooms.getVisibility() == View.VISIBLE) {
            noOfRooms.getEditText().setError(getResources().getString(R.string.mandatory_field));
            noOfRooms.getEditText().requestFocus();
            gotoFirstPage();
            error = true;
        }

        if (App.get(contactIncome).isEmpty() && contactIncome.getVisibility() == View.VISIBLE) {
            contactIncome.getEditText().setError(getResources().getString(R.string.mandatory_field));
            contactIncome.getEditText().requestFocus();
            gotoFirstPage();
            error = true;
        }

        if (App.get(earningMembers).isEmpty() && earningMembers.getVisibility() == View.VISIBLE) {
            earningMembers.getEditText().setError(getResources().getString(R.string.mandatory_field));
            earningMembers.getEditText().requestFocus();
            gotoFirstPage();
            error = true;
        }else{

            int no = Integer.parseInt(String.valueOf(App.get(earningMembers)));
            if(no > 15) {
                earningMembers.getEditText().setError(getString(R.string.pet_invalid_earning_members));
                earningMembers.getEditText().requestFocus();
                gotoFirstPage();
                error = true;
            }
        }

        if (App.get(numberOfFamilyMembers).isEmpty() && numberOfFamilyMembers.getVisibility() == View.VISIBLE) {
            numberOfFamilyMembers.getEditText().setError(getResources().getString(R.string.mandatory_field));
            numberOfFamilyMembers.getEditText().requestFocus();
            gotoFirstPage();
            error = true;
        }else {

            int no = Integer.parseInt(String.valueOf(App.get(numberOfFamilyMembers)));
            if(no > 50) {
                numberOfFamilyMembers.getEditText().setError(getString(R.string.pet_invalid_member_number));
                numberOfFamilyMembers.getEditText().requestFocus();
                gotoFirstPage();
                error = true;
            }
        }

        if (App.get(otherEthinicity).isEmpty() && otherEthinicity.getVisibility() == View.VISIBLE) {
            otherEthinicity.getEditText().setError(getString(R.string.empty_field));
            otherEthinicity.getEditText().requestFocus();
            error = true;
        }else{
            otherEthinicity.getEditText().setError(null);
            otherEthinicity.getEditText().requestFocus();
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

        final String ethnicityString = App.get(ethinicity).equals(getResources().getString(R.string.pet_urdu_speaking)) ? "URDU SPEAKING" :
                (App.get(ethinicity).equals(getResources().getString(R.string.pet_sindhi)) ? "SINDHI" :
                        (App.get(ethinicity).equals(getResources().getString(R.string.pet_pakhtun)) ? "PASHTUN" :
                                (App.get(ethinicity).equals(getResources().getString(R.string.pet_punjabi)) ? "PUNJABI" :
                                        (App.get(ethinicity).equals(getResources().getString(R.string.pet_balochi)) ? "BALOCHI" :
                                                (App.get(ethinicity).equals(getResources().getString(R.string.pet_bihari)) ? "BIHARI" :
                                                        (App.get(ethinicity).equals(getResources().getString(R.string.pet_memon)) ? "MEMON" :
                                                                (App.get(ethinicity).equals(getResources().getString(R.string.pet_gujrati)) ? "GUJRATI" :
                                                                        (App.get(ethinicity).equals(getResources().getString(R.string.pet_brohi)) ? "BROHI" :
                                                                                (App.get(ethinicity).equals(getResources().getString(R.string.pet_hindko)) ? "HINDKO" :
                                                                                        (App.get(ethinicity).equals(getResources().getString(R.string.pet_gilgiti_kashmiri)) ? "GILGITI" :
                                                                                                (App.get(ethinicity).equals(getResources().getString(R.string.pet_siraiki)) ? "SARAIKI" :
                                                                                                        (App.get(ethinicity).equals(getResources().getString(R.string.pet_benagli)) ? "BANGALI" :
                                                                                                                (App.get(ethinicity).equals(getResources().getString(R.string.pet_afghani)) ? "AFGHAN" :
                                                                                                                        (App.get(ethinicity).equals(getResources().getString(R.string.pet_hazara)) ? "HAZARA" :
                                                                                                                                (App.get(ethinicity).equals(getResources().getString(R.string.pet_other_ethnicity)) ? "OTHER ETHNICITY" :
                                                                                                                                        (App.get(ethinicity).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSED"))))))))))))))));

        observations.add(new String[]{"ETHNICITY", ethnicityString});
        String[][] concept = serverService.getConceptUuidAndDataType(ethnicityString);
        personAttribute.put("Ethnicity", concept[0][0]);

        if (otherEthinicity.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER ETHNICITY", App.get(otherEthinicity)});

        final String contactEducationLevelString = App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_elementary)) ? "ELEMENTARY EDUCATION" :
                (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_primary)) ? "PRIMARY EDUCATION" :
                        (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_secondary)) ? "SECONDARY EDUCATION" :
                                (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_intermediate)) ? "INTERMEDIATE EDUCATION" :
                                        (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_undergraduate)) ? "UNDERGRADUATE EDUCATION" :
                                                (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_graduate)) ? "GRADUATE EDUCATION" :
                                                        (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_doctorate)) ? "DOCTORATE EDUCATION" :
                                                                (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_religious)) ? "RELIGIOUS EDUCATION" :
                                                                        (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_polytechnic)) ? "POLYTECHNIC EDUCATION" :
                                                                                (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_special_education)) ? "SPECIAL EDUCATION RECEIVED" :
                                                                                        (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_no_formal_education)) ? "NO FORMAL EDUCATION" :
                                                                                                (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_other)) ? "OTHER" :
                                                                                                        (App.get(contactEducationLevel).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSED"))))))))))));
        observations.add(new String[]{"HIGHEST EDUCATION LEVEL", contactEducationLevelString});
        concept = serverService.getConceptUuidAndDataType(contactEducationLevelString);
        personAttribute.put("Education Level", concept[0][0]);

        if (otherContactEducationLevel.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER EDUCATION LEVEL", App.get(otherContactEducationLevel)});

        final String maritalStatusString = App.get(maritalStatus).equals(getResources().getString(R.string.pet_single)) ? "SINGLE" :
                (App.get(maritalStatus).equals(getResources().getString(R.string.pet_engaged)) ? "ENGAGED" :
                        (App.get(maritalStatus).equals(getResources().getString(R.string.pet_married)) ? "MARRIED" :
                                (App.get(maritalStatus).equals(getResources().getString(R.string.pet_separated)) ? "SEPARATED" :
                                        (App.get(maritalStatus).equals(getResources().getString(R.string.pet_divorced)) ? "DIVORCED" :
                                                (App.get(maritalStatus).equals(getResources().getString(R.string.pet_widower)) ? "WIDOWED" :
                                                        (App.get(maritalStatus).equals(getResources().getString(R.string.pet_other)) ? "OTHER" :
                                                                (App.get(maritalStatus).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSE")))))));
        observations.add(new String[]{"MARITAL STATUS", maritalStatusString});
        concept = serverService.getConceptUuidAndDataType(maritalStatusString);
        personAttribute.put("Marital Status", concept[0][0]);

        final String employementStatusString = App.get(emloyementStatus).equals(getResources().getString(R.string.pet_employed)) ? "EMPLOYED" :
                (App.get(emloyementStatus).equals(getResources().getString(R.string.pet_unable_to_work)) ? "UNABLE TO WORK" :
                        (App.get(emloyementStatus).equals(getResources().getString(R.string.pet_student)) ? "STUDENT" :
                                (App.get(emloyementStatus).equals(getResources().getString(R.string.pet_unemployed)) ? "UNEMPLOYED" :
                                        (App.get(emloyementStatus).equals(getResources().getString(R.string.pet_housework)) ? "HOUSEWORK" :
                                                (App.get(emloyementStatus).equals(getResources().getString(R.string.pet_retired)) ? "RETIRED" :
                                                        (App.get(emloyementStatus).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSE"))))));
        observations.add(new String[]{"EMPLOYMENT STATUS", employementStatusString});
        concept = serverService.getConceptUuidAndDataType(employementStatusString);
        personAttribute.put("Employment Status", concept[0][0]);

        final String occupationString = App.get(occupation).equals(getResources().getString(R.string.pet_artist)) ? "ARTIST" :
                (App.get(occupation).equals(getResources().getString(R.string.pet_beggar)) ? "BEGGAR" :
                        (App.get(occupation).equals(getResources().getString(R.string.pet_carpenter)) ? "CARPENTER" :
                                (App.get(occupation).equals(getResources().getString(R.string.pet_casual_labor)) ? "CASUAL LABOR" :
                                        (App.get(occupation).equals(getResources().getString(R.string.pet_child_labor)) ? "CHILD LABOR" :
                                                (App.get(occupation).equals(getResources().getString(R.string.pet_clerk)) ? "CLERK" :
                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_doctor)) ? "MEDICAL OFFICER/DOCTOR" :
                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_driver)) ? "DRIVER" :
                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_engineer)) ? "ENGINEER" :
                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_farmer)) ? "FARMER" :
                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_fisherman)) ? "FISHERMAN" :
                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_food_vendor)) ? "FOOD VENDOR" :
                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_forestry_worker)) ? "FORESTRY WORKER" :
                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_housework)) ? "HOUSEWORK" :
                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_machine_operator)) ? "MACHINE OPERATOR" :
                                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_miner)) ? "MINER" :
                                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_pilot)) ? "PILOT" :
                                                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_plumber)) ? "PLUMBER" :
                                                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_professional)) ? "PROFESSIONAL" :
                                                                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_reporter)) ? "REPORTER" :
                                                                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_sales_representative)) ? "SALES REPRESENTATIVE" :
                                                                                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_security_officer)) ? "SECURITY OFFICER" :
                                                                                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_shepherd)) ? "SHEPHERD" :
                                                                                                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_slum_worker)) ? "SLUM WORKER" :
                                                                                                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_small_bussiness_owner)) ? "SMALL BUSINESS OWNER" :
                                                                                                                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_tailor)) ? "TAILOR" :
                                                                                                                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_trader)) ? "TRADER" :
                                                                                                                                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_teacher)) ? "TEACHER" :
                                                                                                                                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_vegetable_fruit_Seller)) ? "VEGETABLE SELLER" :
                                                                                                                                                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_waiter)) ? "WAITER" :
                                                                                                                                                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_other)) ? "OTHER" : "UNKNOWN"))))))))))))))))))))))))))))));
        observations.add(new String[]{"OCCUPATION", occupationString});
        concept = serverService.getConceptUuidAndDataType(occupationString);
        personAttribute.put("Occupation", concept[0][0]);

        if (otherOccupation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER OCCUPATION", App.get(otherOccupation)});

        observations.add(new String[]{"FAMILY STRUCTURE", App.get(familyStructure).equals(getResources().getString(R.string.pet_joint_family)) ? "JOINT FAMILY" : "NUCLEAR FAMILY"});
        observations.add(new String[]{"FAMILY SIZE", App.get(numberOfFamilyMembers)});
        observations.add(new String[]{"EARNING FAMILY MEMBERS", App.get(earningMembers)});
        observations.add(new String[]{"MONTHLY INCOME", App.get(contactIncome)});
        final String incomeClassString = App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_none)) ? "NONE" :
                (App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_lower_class)) ? "LOWER INCOME CLASS" :
                        (App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_lower_middle_class)) ? "LOWER MIDDLE INCOME CLASS" :
                                (App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_middle_class)) ? "MIDDLE INCOME CLASS" :
                                        (App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_upper_middle_class)) ? "UPPER MIDDLE INCOME CLASS" :
                                                (App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_upper_class)) ? "UPPER INCOME CLASS" : "UNKNOWN")))));
        observations.add(new String[]{"INCOME CLASS", incomeClassString});
        observations.add(new String[]{"HOUSE TYPE", App.get(residenceType).equals(getResources().getString(R.string.pet_rent)) ? "RENTED" :
                (App.get(residenceType).equals(getResources().getString(R.string.pet_own)) ? "OWNED" : "UNKNOWN")});
        observations.add(new String[]{"NUMBER OF ROOMS (IN HOUSE)", App.get(noOfRooms)});
        String medicalCoditionsString = "";
        for (CheckBox cb : medicalCondition.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_epilepsy)))
                medicalCoditionsString = medicalCoditionsString + "EPILEPSY" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_mental_retardation)))
                medicalCoditionsString = medicalCoditionsString + "MENTAL RETARDATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_physically_disable)))
                medicalCoditionsString = medicalCoditionsString + "DISABILITY" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_hiv)))
                medicalCoditionsString = medicalCoditionsString + "HUMAN IMMUNODEFICIENCY VIRUS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_hcv)))
                medicalCoditionsString = medicalCoditionsString + "HEPATITIS C VIRUS INFECTION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_diabetes)))
                medicalCoditionsString = medicalCoditionsString + "DIABETES MELLITUS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_none)))
                medicalCoditionsString = medicalCoditionsString + "NONE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other)))
                medicalCoditionsString = medicalCoditionsString + "OTHER DISEASE" + " ; ";
        }
        observations.add(new String[]{"GENERAL MEDICAL CONDITION", medicalCoditionsString});
        if (otherMedicalCondition.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER DISEASE", App.get(otherMedicalCondition)});
        String counsellingProvidedToString = "";
        for (CheckBox cb : counsellingProvidedTo.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_self)))
                counsellingProvidedToString = counsellingProvidedToString + "SELF" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_mother)))
                counsellingProvidedToString = counsellingProvidedToString + "MOTHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_father)))
                counsellingProvidedToString = counsellingProvidedToString + "FATHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_maternal_grandmother)))
                counsellingProvidedToString = counsellingProvidedToString + "MATERNAL GRANDMOTHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_maternal_grandfather)))
                counsellingProvidedToString = counsellingProvidedToString + "MATERNAL GRANDFATHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_paternal_grandmother)))
                counsellingProvidedToString = counsellingProvidedToString + "PATERNAL GRANDMOTHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_paternal_grandfather)))
                counsellingProvidedToString = counsellingProvidedToString + "PATERNAL GRANDFATHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_brother)))
                counsellingProvidedToString = counsellingProvidedToString + "BROTHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_sister)))
                counsellingProvidedToString = counsellingProvidedToString + "SISTER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_son)))
                counsellingProvidedToString = counsellingProvidedToString + "SON" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_daughter)))
                counsellingProvidedToString = counsellingProvidedToString + "DAUGHTER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_spouse)))
                counsellingProvidedToString = counsellingProvidedToString + "SPOUSE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_aunt)))
                counsellingProvidedToString = counsellingProvidedToString + "AUNT" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_uncle)))
                counsellingProvidedToString = counsellingProvidedToString + "UNCLE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other)))
                counsellingProvidedToString = counsellingProvidedToString + "OTHER" + " ; ";
        }
        observations.add(new String[]{"FAMILY MEMBERS COUNSELLED", counsellingProvidedToString});

        if(procedure.isChecked())
            observations.add(new String[]{"COUNSELLING PROCEDURE", "YES"});
        else
            observations.add(new String[]{"COUNSELLING PROCEDURE", "NO"});

        if(purpose.isChecked())
            observations.add(new String[]{"COUNSELLING PURPOSE", "YES"});
        else
            observations.add(new String[]{"COUNSELLING PURPOSE", "NO"});

        if(durationOfPet.isChecked())
            observations.add(new String[]{"COUNSELLING DURATION", "YES"});
        else
            observations.add(new String[]{"COUNSELLING DURATION", "NO"});

        if(infectionControlMeasures.isChecked())
            observations.add(new String[]{"COUNSELLING CONTROL MEASURES", "YES"});
        else
            observations.add(new String[]{"COUNSELLING CONTROL MEASURES", "NO"});

        if(transmission.isChecked())
            observations.add(new String[]{"COUNSELLING TRANSMISSION", "YES"});
        else
            observations.add(new String[]{"COUNSELLING TRANSMISSION", "NO"});

        if(adherence.isChecked())
            observations.add(new String[]{"COUNSELLING ADHERENCE", "YES"});
        else
            observations.add(new String[]{"COUNSELLING ADHERENCE", "NO"});

        if(nutrition.isChecked())
            observations.add(new String[]{"NUTRITION COUNSELING", "YES"});
        else
            observations.add(new String[]{"NUTRITION COUNSELING", "NO"});

        if(commonAdverseEffects.isChecked())
            observations.add(new String[]{"COUNSELLING ADVERSE EVENTS", "YES"});
        else
            observations.add(new String[]{"COUNSELLING ADVERSE EVENTS", "NO"});

        if(misconception.isChecked())
            observations.add(new String[]{"COUNSELLING MISCONCEPTION", "YES"});
        else
            observations.add(new String[]{"COUNSELLING MISCONCEPTION", "NO"});

        if(followup.isChecked())
            observations.add(new String[]{"COUNSELLING FOLLOW-UP", "YES"});
        else
            observations.add(new String[]{"COUNSELLING FOLLOW-UP", "NO"});

        if (counsellingProvidedToOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FAMILY MEMBER", App.get(counsellingProvidedToOther)});
        //observations.add(new String[]{"COUNSELLING PROCEDURE", App.get(procedure).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (procedureExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELLING PROCEDURE (TEXT)", App.get(procedureExplanation)});
        //observations.add(new String[]{"COUNSELLING PURPOSE", App.get(purpose).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (purposeExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELLING PURPOSE (TEXT)", App.get(purposeExplanation)});
        //observations.add(new String[]{"COUNSELLING DURATION", App.get(durationOfPet).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (durationOfPetExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELLING DURATION (TEXT)", App.get(durationOfPetExplanation)});
        //observations.add(new String[]{"COUNSELLING CONTROL MEASURES", App.get(infectionControlMeasures).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (infectionControlMeasuresExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELLING CONTROL MEASURES (TEXT)", App.get(infectionControlMeasuresExplanation)});
        //observations.add(new String[]{"COUNSELLING TRANSMISSION", App.get(transmission).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (transmissionExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELLING TRANSMISSION (TEXT)", App.get(transmissionExplanation)});
        //observations.add(new String[]{"COUNSELLING ADHERENCE", App.get(adherence).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (adherenceExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELLING ADHERENCE (TEXT)", App.get(adherenceExplanation)});
        //observations.add(new String[]{"NUTRITION COUNSELING", App.get(nutrition).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (nutritionExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"NUTRITION COUNSELING (TEXT)", App.get(nutritionExplanation)});
        //observations.add(new String[]{"COUNSELLING ADVERSE EVENTS", App.get(commonAdverseEffects).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (commonAdverseEffectsExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELLING ADVERSE EVENTS (TEXT)", App.get(commonAdverseEffectsExplanation)});
        //observations.add(new String[]{"COUNSELLING MISCONCEPTION", App.get(misconception).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (misconceptionExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELLING MISCONCEPTION (TEXT)", App.get(misconceptionExplanation)});
        //observations.add(new String[]{"COUNSELLING FOLLOW-UP", App.get(followup).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (followupExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELLING FOLLOW-UP (TEXT)", App.get(followupExplanation)});
        observations.add(new String[]{"PATIENT QUESTIONS", App.get(questionsByContact)});
        observations.add(new String[]{"PSYCHOLOGIST COUNSELLING EXPLANATION", App.get(psycologistAnswer)});
        observations.add(new String[]{"BEHAVIOUR", App.get(contactBehavior).equals(getResources().getString(R.string.pet_irritable)) ? "IRRITABILITY" :
                (App.get(contactBehavior).equals(getResources().getString(R.string.pet_stubborn)) ? "STUBBORN BEHAVIOUR" :
                        (App.get(contactBehavior).equals(getResources().getString(R.string.pet_shy)) ? "INTROVERTED PERSONALITY" :
                                (App.get(contactBehavior).equals(getResources().getString(R.string.pet_aggressive)) ? "AGGRESSIVE BEHAVIOUR" :
                                        (App.get(contactBehavior).equals(getResources().getString(R.string.pet_argumentative)) ? "ARGUMENTATIVE BEHAVIOUR" :
                                                (App.get(contactBehavior).equals(getResources().getString(R.string.pet_complaint)) ? "COMPLIANT BEHAVIOUR" :
                                                        (App.get(contactBehavior).equals(getResources().getString(R.string.pet_non_complaint)) ? "NON COMPLIANT BEHAVIOUR" :
                                                                (App.get(contactBehavior).equals(getResources().getString(R.string.pet_responsible)) ? "RESPONSIBLE PERSONALITY" :
                                                                        (App.get(contactBehavior).equals(getResources().getString(R.string.pet_cooperative)) ? "COOPERATIVE BEHAVIOUR" :
                                                                                (App.get(contactBehavior).equals(getResources().getString(R.string.pet_noncooperative)) ? "NON-COOPERATIVE BEHAVIOUR" : "OTHER PERSONALITY TYPE")))))))))});
        if (otherBehavior.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER PERSONALITY TYPE", App.get(otherBehavior)});
        observations.add(new String[]{"CARETAKER COMMENTS", App.get(caretakerComments)});
        observations.add(new String[]{"CLINICIAN NOTES (TEXT)", App.get(clincianNote)});

        observations.add(new String[]{"PATIENT REFERRED", App.get(patientReferred).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
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
                    id = serverService.saveFormLocallyTesting(formName, form, formDateCalendar,observations.toArray(new String[][]{}));

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
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            formDate.setEnabled(false);
        }

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == contactBehavior.getSpinner()) {
            if (App.get(contactBehavior).equals(getResources().getString(R.string.pet_other)))
                otherBehavior.setVisibility(View.VISIBLE);
            else
                otherBehavior.setVisibility(View.GONE);
        } else if (spinner == ethinicity.getSpinner()) {
            if (App.get(ethinicity).equals(getResources().getString(R.string.pet_other_ethnicity)))
                otherEthinicity.setVisibility(View.VISIBLE);
            else
                otherEthinicity.setVisibility(View.GONE);
        } else if (spinner == occupation.getSpinner()) {
            if (App.get(occupation).equals(getResources().getString(R.string.pet_other)))
                otherOccupation.setVisibility(View.VISIBLE);
            else
                otherOccupation.setVisibility(View.GONE);
        } else if (spinner == contactEducationLevel.getSpinner()) {
            if (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_other)))
                otherContactEducationLevel.setVisibility(View.VISIBLE);
            else
                otherContactEducationLevel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        Boolean flag = false;
        for (CheckBox cb : medicalCondition.getCheckedBoxes()) {
            if (cb.getText().equals(getResources().getString(R.string.pet_other)) && cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (flag)
            otherMedicalCondition.setVisibility(View.VISIBLE);
        else
            otherMedicalCondition.setVisibility(View.GONE);

        flag = false;
        for (CheckBox cb : counsellingProvidedTo.getCheckedBoxes()) {
            if (cb.getText().equals(getResources().getString(R.string.pet_other)) && cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (flag)
            counsellingProvidedToOther.setVisibility(View.VISIBLE);
        else
            counsellingProvidedToOther.setVisibility(View.GONE);

        if(buttonView == procedure){
            if (isChecked)
                procedureExplanation.setVisibility(View.VISIBLE);
            else
                procedureExplanation.setVisibility(View.GONE);
        } else if(buttonView == purpose){
            if (isChecked)
                purposeExplanation.setVisibility(View.VISIBLE);
            else
                purposeExplanation.setVisibility(View.GONE);
        } else if(buttonView == durationOfPet){
            if (isChecked)
                durationOfPetExplanation.setVisibility(View.VISIBLE);
            else
                durationOfPetExplanation.setVisibility(View.GONE);
        } else if(buttonView == infectionControlMeasures){
            if (isChecked)
                infectionControlMeasuresExplanation.setVisibility(View.VISIBLE);
            else
                infectionControlMeasuresExplanation.setVisibility(View.GONE);
        } else if(buttonView == transmission){
            if (isChecked)
                transmissionExplanation.setVisibility(View.VISIBLE);
            else
                transmissionExplanation.setVisibility(View.GONE);
        } else if(buttonView == adherence){
            if (isChecked)
                adherenceExplanation.setVisibility(View.VISIBLE);
            else
                adherenceExplanation.setVisibility(View.GONE);
        } else if(buttonView == nutrition){
            if (isChecked)
                nutritionExplanation.setVisibility(View.VISIBLE);
            else
                nutritionExplanation.setVisibility(View.GONE);
        } else if(buttonView == commonAdverseEffects){
            if (isChecked)
                commonAdverseEffectsExplanation.setVisibility(View.VISIBLE);
            else
                commonAdverseEffectsExplanation.setVisibility(View.GONE);
        } else if(buttonView == misconception){
            if (isChecked)
                misconceptionExplanation.setVisibility(View.VISIBLE);
            else
                misconceptionExplanation.setVisibility(View.GONE);
        } else if(buttonView == followup){
            if (isChecked)
                followupExplanation.setVisibility(View.VISIBLE);
            else
                followupExplanation.setVisibility(View.GONE);
        }

        setReferralViews();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        /*if (group == procedure.getRadioGroup()) {
            if (App.get(procedure).equals(getResources().getString(R.string.yes)))
                procedureExplanation.setVisibility(View.VISIBLE);
            else
                procedureExplanation.setVisibility(View.GONE);
        } else if (group == purpose.getRadioGroup()) {
            if (App.get(purpose).equals(getResources().getString(R.string.yes)))
                purposeExplanation.setVisibility(View.VISIBLE);
            else
                purposeExplanation.setVisibility(View.GONE);
        } else if (group == durationOfPet.getRadioGroup()) {
            if (App.get(durationOfPet).equals(getResources().getString(R.string.yes)))
                durationOfPetExplanation.setVisibility(View.VISIBLE);
            else
                durationOfPetExplanation.setVisibility(View.GONE);
        } else if (group == infectionControlMeasures.getRadioGroup()) {
            if (App.get(infectionControlMeasures).equals(getResources().getString(R.string.yes)))
                infectionControlMeasuresExplanation.setVisibility(View.VISIBLE);
            else
                infectionControlMeasuresExplanation.setVisibility(View.GONE);
        } else if (group == transmission.getRadioGroup()) {
            if (App.get(transmission).equals(getResources().getString(R.string.yes)))
                transmissionExplanation.setVisibility(View.VISIBLE);
            else
                transmissionExplanation.setVisibility(View.GONE);
        } else if (group == adherence.getRadioGroup()) {
            if (App.get(adherence).equals(getResources().getString(R.string.yes)))
                adherenceExplanation.setVisibility(View.VISIBLE);
            else
                adherenceExplanation.setVisibility(View.GONE);
        } else if (group == nutrition.getRadioGroup()) {
            if (App.get(nutrition).equals(getResources().getString(R.string.yes)))
                nutritionExplanation.setVisibility(View.VISIBLE);
            else
                nutritionExplanation.setVisibility(View.GONE);
        } else if (group == commonAdverseEffects.getRadioGroup()) {
            if (App.get(commonAdverseEffects).equals(getResources().getString(R.string.yes)))
                commonAdverseEffectsExplanation.setVisibility(View.VISIBLE);
            else
                commonAdverseEffectsExplanation.setVisibility(View.GONE);
        } else if (group == misconception.getRadioGroup()) {
            if (App.get(misconception).equals(getResources().getString(R.string.yes)))
                misconceptionExplanation.setVisibility(View.VISIBLE);
            else
                misconceptionExplanation.setVisibility(View.GONE);
        } else if (group == followup.getRadioGroup()) {
            if (App.get(followup).equals(getResources().getString(R.string.yes)))
                followupExplanation.setVisibility(View.VISIBLE);
            else
                followupExplanation.setVisibility(View.GONE);
        }*/

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

            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("ETHNICITY")) {
                String value = obs[0][1].equals("URDU SPEAKING") ? getResources().getString(R.string.pet_urdu_speaking) :
                        (obs[0][1].equals("SINDHI") ? getResources().getString(R.string.pet_sindhi) :
                                (obs[0][1].equals("PASHTUN") ? getResources().getString(R.string.pet_pakhtun) :
                                        (obs[0][1].equals("PUNJABI") ? getResources().getString(R.string.pet_punjabi) :
                                                (obs[0][1].equals("BALOCHI") ? getResources().getString(R.string.pet_balochi) :
                                                        (obs[0][1].equals("BIHARI") ? getResources().getString(R.string.pet_bihari) :
                                                                (obs[0][1].equals("MEMON") ? getResources().getString(R.string.pet_memon) :
                                                                        (obs[0][1].equals("GUJRATI") ? getResources().getString(R.string.pet_gujrati) :
                                                                                (obs[0][1].equals("BROHI") ? getResources().getString(R.string.pet_brohi) :
                                                                                        (obs[0][1].equals("HINDKO") ? getResources().getString(R.string.pet_hindko) :
                                                                                                (obs[0][1].equals("GILGITI") ? getResources().getString(R.string.pet_gilgiti_kashmiri) :
                                                                                                        (obs[0][1].equals("SARAIKI") ? getResources().getString(R.string.pet_siraiki) :
                                                                                                                (obs[0][1].equals("BANGALI") ? getResources().getString(R.string.pet_benagli) :
                                                                                                                        (obs[0][1].equals("AFGHAN") ? getResources().getString(R.string.pet_afghani) :
                                                                                                                                (obs[0][1].equals("HAZARA") ? getResources().getString(R.string.pet_hazara) :
                                                                                                                                        (obs[0][1].equals("HAZARA") ? getResources().getString(R.string.pet_hazara) :
                                                                                                                                                (obs[0][1].equals("OTHER ETHNICITY") ? getResources().getString(R.string.pet_other_ethnicity) :
                                                                                                                                                        (obs[0][1].equals("UNKNOWN") ? getResources().getString(R.string.pet_unknown) : getResources().getString(R.string.refused))))))))))))))))));
                ethinicity.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("OTHER ETHNICITY")) {
                otherEthinicity.getEditText().setText(obs[0][1]);
                otherEthinicity.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("HIGHEST EDUCATION LEVEL")) {
                String value = obs[0][1].equals("ELEMENTARY EDUCATION") ? getResources().getString(R.string.pet_elementary) :
                        (obs[0][1].equals("PRIMARY EDUCATION") ? getResources().getString(R.string.pet_primary) :
                                (obs[0][1].equals("SECONDARY EDUCATION") ? getResources().getString(R.string.pet_secondary) :
                                        (obs[0][1].equals("INTERMEDIATE EDUCATION") ? getResources().getString(R.string.pet_intermediate) :
                                                (obs[0][1].equals("UNDERGRADUATE EDUCATION") ? getResources().getString(R.string.pet_undergraduate) :
                                                        (obs[0][1].equals("GRADUATE EDUCATION") ? getResources().getString(R.string.pet_graduate) :
                                                                (obs[0][1].equals("DOCTORATE EDUCATION") ? getResources().getString(R.string.pet_doctorate) :
                                                                        (obs[0][1].equals("RELIGIOUS EDUCATION") ? getResources().getString(R.string.pet_religious) :
                                                                                (obs[0][1].equals("POLYTECHNIC EDUCATION") ? getResources().getString(R.string.pet_polytechnic) :
                                                                                        (obs[0][1].equals("SPECIAL EDUCATION RECEIVED") ? getResources().getString(R.string.pet_special_education) :
                                                                                                (obs[0][1].equals("NO FORMAL EDUCATION") ? getResources().getString(R.string.pet_no_formal_education) :
                                                                                                        (obs[0][1].equals("OTHER") ? getResources().getString(R.string.pet_other) :
                                                                                                                (obs[0][1].equals("UNKNOWN") ? getResources().getString(R.string.unknown) : getResources().getString(R.string.refused)))))))))))));
                contactEducationLevel.getSpinner().selectValue(value);
            }else if (obs[0][0].equals("OTHER EDUCATION LEVEL")) {
                otherContactEducationLevel.getEditText().setText(obs[0][1]);
                otherContactEducationLevel.setVisibility(View.VISIBLE);
            }  else if (obs[0][0].equals("MARITAL STATUS")) {
                String value = obs[0][1].equals("SINGLE") ? getResources().getString(R.string.pet_single) :
                        (obs[0][1].equals("ENGAGED") ? getResources().getString(R.string.pet_engaged) :
                                (obs[0][1].equals("MARRIED") ? getResources().getString(R.string.pet_married) :
                                        (obs[0][1].equals("SEPARATED") ? getResources().getString(R.string.pet_separated) :
                                                (obs[0][1].equals("DIVORCED") ? getResources().getString(R.string.pet_divorced) :
                                                        (obs[0][1].equals("WIDOWED") ? getResources().getString(R.string.pet_widower) :
                                                                (obs[0][1].equals("OTHER") ? getResources().getString(R.string.pet_other) :
                                                                        (obs[0][1].equals("UNKNOWN") ? getResources().getString(R.string.unknown) : getResources().getString(R.string.refused))))))));
                maritalStatus.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("EMPLOYMENT STATUS")) {
                String value = obs[0][1].equals("EMPLOYED") ? getResources().getString(R.string.pet_employed) :
                        (obs[0][1].equals("UNABLE TO WORK") ? getResources().getString(R.string.pet_unable_to_work) :
                                (obs[0][1].equals("STUDENT") ? getResources().getString(R.string.pet_student) :
                                        (obs[0][1].equals("UNEMPLOYED") ? getResources().getString(R.string.pet_unemployed) :
                                                (obs[0][1].equals("HOUSEWORK") ? getResources().getString(R.string.pet_housework) :
                                                        (obs[0][1].equals("RETIRED") ? getResources().getString(R.string.pet_retired) :
                                                                (obs[0][1].equals("UNKNOWN") ? getResources().getString(R.string.unknown) : getResources().getString(R.string.refused)))))));
                emloyementStatus.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("OCCUPATION")) {
                String value = obs[0][1].equals("ARTIST") ? getResources().getString(R.string.pet_artist) :
                        (obs[0][1].equals("BEGGAR") ? getResources().getString(R.string.pet_beggar) :
                                (obs[0][1].equals("CARPENTER") ? getResources().getString(R.string.pet_carpenter) :
                                        (obs[0][1].equals("CASUAL LABOR") ? getResources().getString(R.string.pet_casual_labor) :
                                                (obs[0][1].equals("CHILD LABOR") ? getResources().getString(R.string.pet_child_labor) :
                                                        (obs[0][1].equals("CLERK") ? getResources().getString(R.string.pet_clerk) :
                                                                (obs[0][1].equals("MEDICAL OFFICER/DOCTOR") ? getResources().getString(R.string.pet_doctor) :
                                                                        (obs[0][1].equals("DRIVER") ? getResources().getString(R.string.pet_driver) :
                                                                                (obs[0][1].equals("ENGINEER") ? getResources().getString(R.string.pet_engineer) :
                                                                                        (obs[0][1].equals("FARMER") ? getResources().getString(R.string.pet_farmer) :
                                                                                                (obs[0][1].equals("FISHERMAN") ? getResources().getString(R.string.pet_fisherman) :
                                                                                                        (obs[0][1].equals("FOOD VENDOR") ? getResources().getString(R.string.pet_food_vendor) :
                                                                                                                (obs[0][1].equals("FORESTRY WORKER") ? getResources().getString(R.string.pet_forestry_worker) :
                                                                                                                        (obs[0][1].equals("HOUSEWORK") ? getResources().getString(R.string.pet_housework) :
                                                                                                                                (obs[0][1].equals("MACHINE OPERATOR") ? getResources().getString(R.string.pet_machine_operator) :
                                                                                                                                        (obs[0][1].equals("MINER") ? getResources().getString(R.string.pet_miner) :
                                                                                                                                                (obs[0][1].equals("PILOT") ? getResources().getString(R.string.pet_pilot) :
                                                                                                                                                        (obs[0][1].equals("PLUMBER") ? getResources().getString(R.string.pet_plumber) :
                                                                                                                                                                (obs[0][1].equals("PROFESSIONAL") ? getResources().getString(R.string.pet_professional) :
                                                                                                                                                                        (obs[0][1].equals("REPORTER") ? getResources().getString(R.string.pet_reporter) :
                                                                                                                                                                                (obs[0][1].equals("SALES REPRESENTATIVE") ? getResources().getString(R.string.pet_sales_representative) :
                                                                                                                                                                                        (obs[0][1].equals("SECURITY OFFICER") ? getResources().getString(R.string.pet_security_officer) :
                                                                                                                                                                                                (obs[0][1].equals("SHEPHERD") ? getResources().getString(R.string.pet_shepherd) :
                                                                                                                                                                                                        (obs[0][1].equals("SLUM WORKER") ? getResources().getString(R.string.pet_slum_worker) :
                                                                                                                                                                                                                (obs[0][1].equals("SMALL BUSINESS OWNER") ? getResources().getString(R.string.pet_small_bussiness_owner) :
                                                                                                                                                                                                                        (obs[0][1].equals("TAILOR") ? getResources().getString(R.string.pet_tailor) :
                                                                                                                                                                                                                                (obs[0][1].equals("TRADER") ? getResources().getString(R.string.pet_trader) :
                                                                                                                                                                                                                                        (obs[0][1].equals("TEACHER") ? getResources().getString(R.string.pet_teacher) :
                                                                                                                                                                                                                                                (obs[0][1].equals("VEGETABLE SELLER") ? getResources().getString(R.string.pet_vegetable_fruit_Seller) :
                                                                                                                                                                                                                                                        (obs[0][1].equals("WAITER") ? getResources().getString(R.string.pet_waiter) :
                                                                                                                                                                                                                                                                (obs[0][1].equals("OTHER") ? getResources().getString(R.string.pet_other) : getResources().getString(R.string.unknown)))))))))))))))))))))))))))))));
                occupation.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("OTHER OCCUPATION")) {
                otherOccupation.getEditText().setText(obs[0][1]);
                otherOccupation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("FAMILY STRUCTURE")) {
                for (RadioButton rb : familyStructure.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_joint_family)) && obs[0][1].equals("JOINT FAMILY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_nuclear)) && obs[0][1].equals("NUCLEAR FAMILY")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("FAMILY SIZE")) {
                numberOfFamilyMembers.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("EARNING FAMILY MEMBERS")) {
                earningMembers.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("MONTHLY INCOME")) {
                contactIncome.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("HOUSE TYPE")) {
                for (RadioButton rb : familyStructure.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_own)) && obs[0][1].equals("OWNED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_rent)) && obs[0][1].equals("RENTED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("NUMBER OF ROOMS (IN HOUSE)")) {
                noOfRooms.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("GENERAL MEDICAL CONDITION")) {
                for (CheckBox cb : medicalCondition.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_epilepsy)) && obs[0][1].equals("EPILEPSY")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_mental_retardation)) && obs[0][1].equals("MENTAL RETARDATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_physically_disable)) && obs[0][1].equals("DISABILITY")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_hiv)) && obs[0][1].equals("HUMAN IMMUNODEFICIENCY VIRUS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_hcv)) && obs[0][1].equals("HEPATITIS C VIRUS INFECTION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_diabetes)) && obs[0][1].equals("DIABETES")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_none)) && obs[0][1].equals("NONE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_other)) && obs[0][1].equals("OTHER DISEASE")) {
                        cb.setChecked(true);
                        break;
                    }

                }
            } else if (obs[0][0].equals("OTHER DISEASE")) {
                otherMedicalCondition.getEditText().setText(obs[0][1]);
                otherMedicalCondition.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("FAMILY MEMBERS COUNSELLED")) {
                for (CheckBox cb : counsellingProvidedTo.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_self)) && obs[0][1].equals("SELF")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_mother)) && obs[0][1].equals("MOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_father)) && obs[0][1].equals("FATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_maternal_grandmother)) && obs[0][1].equals("MATERNAL GRANDMOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_maternal_grandfather)) && obs[0][1].equals("MATERNAL GRANDFATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_paternal_grandmother)) && obs[0][1].equals("PATERNAL GRANDMOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_paternal_grandfather)) && obs[0][1].equals("PATERNAL GRANDFATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_brother)) && obs[0][1].equals("BROTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_sister)) && obs[0][1].equals("SISTER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_son)) && obs[0][1].equals("SON")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_daughter)) && obs[0][1].equals("DAUGHTER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_spouse)) && obs[0][1].equals("SPOUSE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_aunt)) && obs[0][1].equals("AUNT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_uncle)) && obs[0][1].equals("UNCLE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_other)) && obs[0][1].equals("OTHER")) {
                        cb.setChecked(true);
                        break;
                    }

                }
            } else if (obs[0][0].equals("OTHER FAMILY MEMBER")) {
                counsellingProvidedToOther.getEditText().setText(obs[0][1]);
                counsellingProvidedToOther.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("COUNSELLING PROCEDURE") && obs[0][1].equals("YES")) {
                procedure.setChecked(true);
                procedureExplanation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("COUNSELLING PROCEDURE (TEXT)")) {
                procedureExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUNSELLING PURPOSE") && obs[0][1].equals("YES")) {
                purpose.setChecked(true);
                purposeExplanation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("COUNSELLING PURPOSE (TEXT)")) {
                purposeExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUNSELLING DURATION") && obs[0][1].equals("YES")) {
                durationOfPet.setChecked(true);
                durationOfPetExplanation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("COUNSELLING DURATION (TEXT)")) {
                durationOfPetExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUNSELLING CONTROL MEASURES") && obs[0][1].equals("YES")) {
                infectionControlMeasures.setChecked(true);
                infectionControlMeasuresExplanation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("COUNSELLING CONTROL MEASURES (TEXT)")) {
                infectionControlMeasuresExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUNSELLING TRANSMISSION") && obs[0][1].equals("YES")) {
                transmission.setChecked(true);
                transmissionExplanation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("COUNSELLING TRANSMISSION (TEXT)")) {
                transmissionExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUNSELLING ADHERENCE") && obs[0][1].equals("YES")) {
                adherence.setChecked(true);
                adherenceExplanation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("COUNSELLING ADHERENCE (TEXT)")) {
                adherenceExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("NUTRITION COUNSELING") && obs[0][1].equals("YES")) {
                nutrition.setChecked(true);
                nutritionExplanation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("NUTRITION COUNSELING (TEXT)")) {
                nutritionExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUNSELLING ADVERSE EVENTS") && obs[0][1].equals("YES")) {
                commonAdverseEffects.setChecked(true);
                commonAdverseEffectsExplanation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("COUNSELLING ADVERSE EVENTS (TEXT)")) {
                commonAdverseEffectsExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUNSELLING MISCONCEPTION") && obs[0][1].equals("YES")) {
                misconception.setChecked(true);
                misconceptionExplanation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("COUNSELLING MISCONCEPTION (TEXT)")) {
                misconceptionExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUNSELLING FOLLOW-UP") && obs[0][1].equals("YES")) {
                followup.setChecked(true);
                followupExplanation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("COUNSELLING FOLLOW-UP (TEXT)")) {
                followupExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("PATIENT QUESTIONS")) {
                questionsByContact.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("PSYCHOLOGIST COUNSELLING EXPLANATION")) {
                psycologistAnswer.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("BEHAVIOUR")) {
                String value = obs[0][1].equals("IRRITABILITY") ? getResources().getString(R.string.pet_irritable) :
                        (obs[0][1].equals("STUBBORN BEHAVIOUR") ? getResources().getString(R.string.pet_stubborn) :
                                (obs[0][1].equals("INTROVERTED PERSONALITY") ? getResources().getString(R.string.pet_shy) :
                                        (obs[0][1].equals("AGGRESSIVE BEHAVIOUR") ? getResources().getString(R.string.pet_aggressive) :
                                                (obs[0][1].equals("ARGUMENTATIVE BEHAVIOUR") ? getResources().getString(R.string.pet_argumentative) :
                                                        (obs[0][1].equals("NON COMPLIANT BEHAVIOUR") ? getResources().getString(R.string.pet_non_complaint) :
                                                                (obs[0][1].equals("COMPLIANT BEHAVIOUR") ? getResources().getString(R.string.pet_complaint) :
                                                                        (obs[0][1].equals("RESPONSIBLE PERSONALITY") ? getResources().getString(R.string.pet_responsible) :
                                                                                (obs[0][1].equals("COOPERATIVE BEHAVIOUR") ? getResources().getString(R.string.pet_cooperative) :
                                                                                        (obs[0][1].equals("NON-COOPERATIVE BEHAVIOUR") ? getResources().getString(R.string.pet_noncooperative) : getResources().getString(R.string.pet_other))))))))));
                contactBehavior.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("OTHER PERSONALITY TYPE")) {
                otherBehavior.getEditText().setText(obs[0][1]);
                otherBehavior.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("CARETAKER COMMENTS")) {
                caretakerComments.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
                clincianNote.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("PATIENT REFERRED")) {
                for (RadioButton rb : patientReferred.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("PATIENT REFERRED TO")) {
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
