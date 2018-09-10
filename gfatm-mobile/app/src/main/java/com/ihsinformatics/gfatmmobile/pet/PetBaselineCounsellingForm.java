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

/**
 * Created by Rabbia on 11/24/2016.
 */

public class PetBaselineCounsellingForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
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

    TitledRadioGroup procedure;
    TitledEditText procedureExplanation;
    TitledRadioGroup purpose;
    TitledEditText purposeExplanation;
    TitledRadioGroup durationOfPet;
    TitledEditText durationOfPetExplanation;
    TitledRadioGroup infectionControlMeasures;
    TitledEditText infectionControlMeasuresExplanation;
    TitledRadioGroup transmission;
    TitledEditText transmissionExplanation;
    TitledRadioGroup adherence;
    TitledEditText adherenceExplanation;
    TitledRadioGroup nutrition;
    TitledEditText nutritionExplanation;
    TitledRadioGroup commonAdverseEffects;
    TitledEditText commonAdverseEffectsExplanation;
    TitledRadioGroup misconception;
    TitledEditText misconceptionExplanation;
    TitledRadioGroup followup;
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

        pageCount = 3;
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
        MyLinearLayout linearLayout1 = new MyLinearLayout(context, getResources().getString(R.string.pet_information_during_counselling_sessions), App.VERTICAL);
        procedure = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_procedure), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        procedureExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        procedureExplanation.getEditText().setSingleLine(false);
        procedureExplanation.getEditText().setMinimumHeight(150);
        purpose = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_purpose), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        purposeExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        purposeExplanation.getEditText().setSingleLine(false);
        purposeExplanation.getEditText().setMinimumHeight(150);
        durationOfPet = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_duration_of_pet), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        durationOfPetExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        durationOfPetExplanation.getEditText().setSingleLine(false);
        durationOfPetExplanation.getEditText().setMinimumHeight(150);
        infectionControlMeasures = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_infection_control_measures), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        infectionControlMeasuresExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        infectionControlMeasuresExplanation.getEditText().setSingleLine(false);
        infectionControlMeasuresExplanation.getEditText().setMinimumHeight(150);
        transmission = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_transmission), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        transmissionExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        transmissionExplanation.getEditText().setSingleLine(false);
        transmissionExplanation.getEditText().setMinimumHeight(150);
        adherence = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adherence), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        adherenceExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        adherenceExplanation.getEditText().setSingleLine(false);
        adherenceExplanation.getEditText().setMinimumHeight(150);
        nutrition = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_nutrition), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        nutritionExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        nutritionExplanation.getEditText().setSingleLine(false);
        nutritionExplanation.getEditText().setMinimumHeight(150);
        commonAdverseEffects = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_common_adverse_effects), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        commonAdverseEffectsExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        commonAdverseEffectsExplanation.getEditText().setSingleLine(false);
        commonAdverseEffectsExplanation.getEditText().setMinimumHeight(150);
        misconception = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_misconception), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        misconceptionExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        misconceptionExplanation.getEditText().setSingleLine(false);
        misconceptionExplanation.getEditText().setMinimumHeight(150);
        followup = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_followup), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        followupExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
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

        linearLayout1.addView(procedure);
        linearLayout1.addView(procedureExplanation);
        linearLayout1.addView(purpose);
        linearLayout1.addView(purposeExplanation);
        linearLayout1.addView(durationOfPet);
        linearLayout1.addView(durationOfPetExplanation);
        linearLayout1.addView(infectionControlMeasures);
        linearLayout1.addView(infectionControlMeasuresExplanation);
        linearLayout1.addView(transmission);
        linearLayout1.addView(transmissionExplanation);
        linearLayout1.addView(adherence);
        linearLayout1.addView(adherenceExplanation);
        linearLayout1.addView(nutrition);
        linearLayout1.addView(nutritionExplanation);
        linearLayout1.addView(commonAdverseEffects);
        linearLayout1.addView(commonAdverseEffectsExplanation);
        linearLayout1.addView(misconception);
        linearLayout1.addView(misconceptionExplanation);
        linearLayout1.addView(followup);
        linearLayout1.addView(followupExplanation);
        linearLayout1.addView(questionsByContact);
        linearLayout1.addView(psycologistAnswer);
        linearLayout1.addView(contactBehavior);
        linearLayout1.addView(otherBehavior);
        linearLayout1.addView(caretakerComments);
        linearLayout1.addView(clincianNote);

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
                residenceType.getRadioGroup(), noOfRooms.getEditText(), medicalCondition, procedure.getRadioGroup(), procedureExplanation.getEditText(), purpose.getRadioGroup(), purposeExplanation.getEditText(),
                durationOfPet.getRadioGroup(), durationOfPetExplanation.getEditText(), infectionControlMeasures.getRadioGroup(), infectionControlMeasuresExplanation.getEditText(),
                transmission.getRadioGroup(), transmissionExplanation.getEditText(), adherence.getRadioGroup(), adherenceExplanation.getEditText(), nutrition.getRadioGroup(), nutritionExplanation.getEditText(),
                commonAdverseEffects.getRadioGroup(), commonAdverseEffectsExplanation.getEditText(), misconception.getRadioGroup(), misconceptionExplanation.getEditText(), followup.getRadioGroup(), followupExplanation.getEditText(),
                questionsByContact.getEditText(), psycologistAnswer.getEditText(), contactBehavior.getSpinner(), otherBehavior.getEditText(), contactIncome.getEditText(), clincianNote.getEditText(), caretakerComments.getEditText(),
                patientReferred.getRadioGroup(), referredTo, referalReasonPsychologist, otherReferalReasonPsychologist.getEditText(), referalReasonSupervisor, otherReferalReasonSupervisor.getEditText(),
                referalReasonCallCenter, otherReferalReasonCallCenter.getEditText(), referalReasonClinician, otherReferalReasonClinician.getEditText()
        };

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, familyStructure, numberOfFamilyMembers, earningMembers, contactIncome, contactIncomeGroup, residenceType, noOfRooms, medicalCondition, otherMedicalCondition},
                        {linearLayout},
                        {linearLayout1, patientReferred, referredTo, referalReasonPsychologist, otherReferalReasonPsychologist, referalReasonSupervisor, otherReferalReasonSupervisor,
                                referalReasonCallCenter, otherReferalReasonCallCenter, referalReasonClinician, otherReferalReasonClinician, clincianNote}
                };

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
        procedure.getRadioGroup().setOnCheckedChangeListener(this);
        purpose.getRadioGroup().setOnCheckedChangeListener(this);
        durationOfPet.getRadioGroup().setOnCheckedChangeListener(this);
        infectionControlMeasures.getRadioGroup().setOnCheckedChangeListener(this);
        transmission.getRadioGroup().setOnCheckedChangeListener(this);
        adherence.getRadioGroup().setOnCheckedChangeListener(this);
        nutrition.getRadioGroup().setOnCheckedChangeListener(this);
        commonAdverseEffects.getRadioGroup().setOnCheckedChangeListener(this);
        misconception.getRadioGroup().setOnCheckedChangeListener(this);
        followup.getRadioGroup().setOnCheckedChangeListener(this);
        contactBehavior.getSpinner().setOnItemSelectedListener(this);

        for (CheckBox cb : medicalCondition.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : counsellingProvidedTo.getCheckedBoxes())
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

        purposeExplanation.setVisibility(View.GONE);
        durationOfPetExplanation.setVisibility(View.GONE);
        infectionControlMeasuresExplanation.setVisibility(View.GONE);
        transmissionExplanation.setVisibility(View.GONE);
        adherenceExplanation.setVisibility(View.GONE);
        nutritionExplanation.setVisibility(View.GONE);
        commonAdverseEffectsExplanation.setVisibility(View.GONE);
        misconceptionExplanation.setVisibility(View.GONE);
        followupExplanation.setVisibility(View.GONE);
        referredTo.setVisibility(View.GONE);
        referalReasonPsychologist.setVisibility(View.GONE);
        otherReferalReasonPsychologist.setVisibility(View.GONE);
        referalReasonSupervisor.setVisibility(View.GONE);
        otherReferalReasonSupervisor.setVisibility(View.GONE);
        referalReasonCallCenter.setVisibility(View.GONE);
        otherReferalReasonCallCenter.setVisibility(View.GONE);
        referalReasonClinician.setVisibility(View.GONE);
        otherReferalReasonClinician.setVisibility(View.GONE);

        if (App.getPatient().getPerson().getAge() < 15)
            contactBehavior.setVisibility(View.VISIBLE);
        else
            contactBehavior.setVisibility(View.GONE);

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
        if (counsellingProvidedToOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FAMILY MEMBER", App.get(counsellingProvidedToOther)});
        observations.add(new String[]{"COUNSELLING PROCEDURE", App.get(procedure).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (procedureExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELLING PROCEDURE (TEXT)", App.get(procedureExplanation)});
        observations.add(new String[]{"COUNSELLING PURPOSE", App.get(purpose).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (purposeExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELLING PURPOSE (TEXT)", App.get(purposeExplanation)});
        observations.add(new String[]{"COUNSELLING DURATION", App.get(durationOfPet).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (durationOfPetExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELLING DURATION (TEXT)", App.get(durationOfPetExplanation)});
        observations.add(new String[]{"COUNSELLING CONTROL MEASURES", App.get(infectionControlMeasures).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (infectionControlMeasuresExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELLING CONTROL MEASURES (TEXT)", App.get(infectionControlMeasuresExplanation)});
        observations.add(new String[]{"COUNSELLING TRANSMISSION", App.get(transmission).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (transmissionExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELLING TRANSMISSION (TEXT)", App.get(transmissionExplanation)});
        observations.add(new String[]{"COUNSELLING ADHERENCE", App.get(adherence).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (adherenceExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELLING ADHERENCE (TEXT)", App.get(adherenceExplanation)});
        observations.add(new String[]{"NUTRITION COUNSELING", App.get(nutrition).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (nutritionExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"NUTRITION COUNSELING (TEXT)", App.get(nutritionExplanation)});
        observations.add(new String[]{"COUNSELLING ADVERSE EVENTS", App.get(commonAdverseEffects).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (commonAdverseEffectsExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELLING ADVERSE EVENTS (TEXT)", App.get(commonAdverseEffectsExplanation)});
        observations.add(new String[]{"COUNSELLING MISCONCEPTION", App.get(misconception).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (misconceptionExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELLING MISCONCEPTION (TEXT)", App.get(misconceptionExplanation)});
        observations.add(new String[]{"COUNSELLING FOLLOW-UP", App.get(followup).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
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

                String result = serverService.saveEncounterAndObservation(formName, form, formDateCalendar, observations.toArray(new String[][]{}), false);
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

        setReferralViews();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (group == procedure.getRadioGroup()) {
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
            } else if (obs[0][0].equals("COUNSELLING PROCEDURE")) {
                for (RadioButton rb : procedure.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        procedureExplanation.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUNSELLING PROCEDURE (TEXT)")) {
                procedureExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUNSELLING PURPOSE")) {
                for (RadioButton rb : purpose.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        purposeExplanation.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUNSELLING PURPOSE (TEXT)")) {
                purposeExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUNSELLING DURATION")) {
                for (RadioButton rb : durationOfPet.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        durationOfPetExplanation.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUNSELLING DURATION (TEXT)")) {
                durationOfPetExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUNSELLING CONTROL MEASURES")) {
                for (RadioButton rb : infectionControlMeasures.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        infectionControlMeasuresExplanation.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUNSELLING CONTROL MEASURES (TEXT)")) {
                infectionControlMeasuresExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUNSELLING TRANSMISSION")) {
                for (RadioButton rb : transmission.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        transmissionExplanation.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUNSELLING TRANSMISSION (TEXT)")) {
                transmissionExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUNSELLING ADHERENCE")) {
                for (RadioButton rb : adherence.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        adherenceExplanation.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUNSELLING ADHERENCE (TEXT)")) {
                adherenceExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("NUTRITION COUNSELING")) {
                for (RadioButton rb : nutrition.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        nutritionExplanation.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("NUTRITION COUNSELING (TEXT)")) {
                nutritionExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUNSELLING ADVERSE EVENTS")) {
                for (RadioButton rb : commonAdverseEffects.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        commonAdverseEffectsExplanation.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUNSELLING ADVERSE EVENTS (TEXT)")) {
                commonAdverseEffectsExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUNSELLING MISCONCEPTION")) {
                for (RadioButton rb : misconception.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        misconceptionExplanation.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUNSELLING MISCONCEPTION (TEXT)")) {
                misconceptionExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUNSELLING FOLLOW-UP")) {
                for (RadioButton rb : followup.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        followupExplanation.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
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
                    } else if (cb.getText().equals(getResources().getString(R.string.adverse_event)) && obs[0][1].equals("ADVERSE EVENTS")) {
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
