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
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
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

    Snackbar snackbar;
    ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 3;
        FORM_NAME = Forms.PET_BASELINE_COUNSELLING;
        FORM = Forms.pet_baselineCounselling;

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
            for (int i = 0; i < PAGE_COUNT; i++) {
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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        familyStructure = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_family_structure), getResources().getStringArray(R.array.pet_family_structures), getResources().getString(R.string.pet_joint_family), App.HORIZONTAL, App.HORIZONTAL);
        numberOfFamilyMembers = new TitledEditText(context, null, getResources().getString(R.string.pet_members_number), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        earningMembers = new TitledEditText(context, null, getResources().getString(R.string.pet_earning_members), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        contactIncome = new TitledEditText(context, null, getResources().getString(R.string.pet_contact_income), "0", "", 20, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        contactIncomeGroup = new TitledEditText(context, null, getResources().getString(R.string.pet_contact_income_group), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        residenceType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_residence_type), getResources().getStringArray(R.array.pet_residence_types), getResources().getString(R.string.pet_rent), App.HORIZONTAL, App.VERTICAL);
        noOfRooms = new TitledEditText(context, null, getResources().getString(R.string.pet_room_nos), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        medicalCondition = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_contact_psychiatric_medical_condition), getResources().getStringArray(R.array.pet_contact_psychiatric_medical_conditions), null, App.VERTICAL, App.VERTICAL);
        otherMedicalCondition = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        // second page views...
        MyLinearLayout linearLayout = new MyLinearLayout(context, getResources().getString(R.string.pet_counselling_procedure), App.VERTICAL);
        counsellingProvidedTo = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_counselling_to), getResources().getStringArray(R.array.pet_cnic_owners), null, App.VERTICAL, App.VERTICAL);
        counsellingProvidedToOther = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        linearLayout.addView(counsellingProvidedTo);
        linearLayout.addView(counsellingProvidedToOther);

        // third page views...
        MyLinearLayout linearLayout1 = new MyLinearLayout(context, getResources().getString(R.string.pet_information_during_counselling_sessions), App.VERTICAL);
        procedure = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_procedure), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        procedureExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        procedureExplanation.getEditText().setSingleLine(false);
        procedureExplanation.getEditText().setMinimumHeight(150);
        purpose = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_purpose), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        purposeExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        purposeExplanation.getEditText().setSingleLine(false);
        purposeExplanation.getEditText().setMinimumHeight(150);
        durationOfPet = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_duration_of_pet), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        durationOfPetExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        durationOfPetExplanation.getEditText().setSingleLine(false);
        durationOfPetExplanation.getEditText().setMinimumHeight(150);
        infectionControlMeasures = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_infection_control_measures), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        infectionControlMeasuresExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        infectionControlMeasuresExplanation.getEditText().setSingleLine(false);
        infectionControlMeasuresExplanation.getEditText().setMinimumHeight(150);
        transmission = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_transmission), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        transmissionExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        transmissionExplanation.getEditText().setSingleLine(false);
        transmissionExplanation.getEditText().setMinimumHeight(150);
        adherence = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adherence), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        adherenceExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        adherenceExplanation.getEditText().setSingleLine(false);
        adherenceExplanation.getEditText().setMinimumHeight(150);
        nutrition = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_nutrition), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        nutritionExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        nutritionExplanation.getEditText().setSingleLine(false);
        nutritionExplanation.getEditText().setMinimumHeight(150);
        commonAdverseEffects = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_common_adverse_effects), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        commonAdverseEffectsExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        commonAdverseEffectsExplanation.getEditText().setSingleLine(false);
        commonAdverseEffectsExplanation.getEditText().setMinimumHeight(150);
        misconception = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_misconception), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        misconceptionExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        misconceptionExplanation.getEditText().setSingleLine(false);
        misconceptionExplanation.getEditText().setMinimumHeight(150);
        followup = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_followup), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        followupExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        followupExplanation.getEditText().setSingleLine(false);
        followupExplanation.getEditText().setMinimumHeight(150);
        questionsByContact = new TitledEditText(context, null, getResources().getString(R.string.pet_question_by_contact), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        questionsByContact.getEditText().setSingleLine(false);
        questionsByContact.getEditText().setMinimumHeight(150);
        psycologistAnswer = new TitledEditText(context, null, getResources().getString(R.string.pet_psychologist_comment), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        psycologistAnswer.getEditText().setSingleLine(false);
        psycologistAnswer.getEditText().setMinimumHeight(150);
        contactBehavior = new TitledSpinner(context, "", getResources().getString(R.string.pet_contact_behaviour), getResources().getStringArray(R.array.pet_contact_behaviours), "", App.VERTICAL);
        otherBehavior = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        caretakerComments = new TitledEditText(context, null, getResources().getString(R.string.pet_caretaker_comments), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        caretakerComments.getEditText().setSingleLine(false);
        caretakerComments.getEditText().setMinimumHeight(150);
        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.pet_doctor_notes), "", "", 250, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
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

        // Used for reset fields...
        views = new View[]{formDate.getButton(), familyStructure.getRadioGroup(), numberOfFamilyMembers.getEditText(), earningMembers.getEditText(), contactIncomeGroup.getEditText(),
                residenceType.getRadioGroup(), noOfRooms.getEditText(), medicalCondition, procedure.getRadioGroup(), procedureExplanation.getEditText(), purpose.getRadioGroup(), purposeExplanation.getEditText(),
                durationOfPet.getRadioGroup(), durationOfPetExplanation.getEditText(), infectionControlMeasures.getRadioGroup(), infectionControlMeasuresExplanation.getEditText(),
                transmission.getRadioGroup(), transmissionExplanation.getEditText(), adherence.getRadioGroup(), adherenceExplanation.getEditText(), nutrition.getRadioGroup(), nutritionExplanation.getEditText(),
                commonAdverseEffects.getRadioGroup(), commonAdverseEffectsExplanation.getEditText(), misconception.getRadioGroup(), misconceptionExplanation.getEditText(), followup.getRadioGroup(), followupExplanation.getEditText(),
                questionsByContact.getEditText(), psycologistAnswer.getEditText(), contactBehavior.getSpinner(), otherBehavior.getEditText(), contactIncome.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, familyStructure, numberOfFamilyMembers, earningMembers, contactIncome, contactIncomeGroup, residenceType, noOfRooms, medicalCondition, otherMedicalCondition},
                        {linearLayout},
                        {linearLayout1}};

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
        otherBehavior.setVisibility(View.GONE);
        if (App.getPatient().getPerson().getAge() < 15)
            contactBehavior.setVisibility(View.VISIBLE);
        else
            contactBehavior.setVisibility(View.GONE);

        for (CheckBox cb : medicalCondition.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : counsellingProvidedTo.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

    }

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

        if (!formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString())) {

            if (formDateCalendar.after(new Date())) {

                Date date = App.stringToDate(formDate.getButton().getText().toString(), "dd-MMM-yyyy");
                formDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }

    }


    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
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

        if (App.getPatient().getPerson().getAge() < 15)
            contactBehavior.setVisibility(View.VISIBLE);
        else
            contactBehavior.setVisibility(View.GONE);

    }


    @Override
    public boolean validate() {

        View view = null;
        Boolean error = false;

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

        Boolean flag = false;
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
        }

        if (App.get(numberOfFamilyMembers).isEmpty() && numberOfFamilyMembers.getVisibility() == View.VISIBLE) {
            numberOfFamilyMembers.getEditText().setError(getResources().getString(R.string.mandatory_field));
            numberOfFamilyMembers.getEditText().requestFocus();
            gotoFirstPage();
            error = true;
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
        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        /*observations.add (new String[] {"LONGITUDE (DEGREES)", String.valueOf(longitude)});
        observations.add (new String[] {"LATITUDE (DEGREES)", String.valueOf(latitude)});*/

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
                medicalCoditionsString = medicalCoditionsString + "DIABETES" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_none)))
                medicalCoditionsString = medicalCoditionsString + "NONE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other)))
                medicalCoditionsString = medicalCoditionsString + "OTHER DISEASE" + " ; ";
        }
        observations.add(new String[]{"GENERAL MEDICAL CONDITION", medicalCoditionsString});
        if (otherMedicalCondition.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"GENERAL MEDICAL CONDITION", App.get(otherMedicalCondition)});
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
        observations.add(new String[]{"FAMILY MEMBERS COUNSELLED", medicalCoditionsString});
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
                                                (App.get(contactBehavior).equals(getResources().getString(R.string.pet_non_complaint)) ? "NON COMPLAINT BEHAVIOUR" :
                                                        (App.get(contactBehavior).equals(getResources().getString(R.string.pet_responsible)) ? "RESPONSIBLE PERSONALITY" :
                                                                (App.get(contactBehavior).equals(getResources().getString(R.string.pet_cooperative)) ? "COOPERATIVE BEHAVIOUR" :
                                                                        (App.get(contactBehavior).equals(getResources().getString(R.string.pet_noncooperative)) ? "NON-COOPERATIVE BEHAVIOUR" : "OTHER PERSONALITY TYPE"))))))))});
        if (otherBehavior.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER PERSONALITY TYPE", App.get(otherBehavior)});
        observations.add(new String[]{"CARETAKER COMMENTS", App.get(caretakerComments)});
        observations.add(new String[]{"CLINICIAN NOTES (TEXT)", App.get(clincianNote)});

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

                String result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}));
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
        }
    }

    @Override
    public void refill(int encounterId) {
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
