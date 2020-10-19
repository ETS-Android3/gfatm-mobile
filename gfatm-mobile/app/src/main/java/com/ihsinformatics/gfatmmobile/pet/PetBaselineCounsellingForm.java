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
    TitledRadioGroup contactIncomeGroup;
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

        ethinicity = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_ethnicity), getResources().getStringArray(R.array.pet_ethnicities), "", App.VERTICAL, true, "ETHNICITY", new String[]{"URDU SPEAKING", "SINDHI", "PASHTUN", "PUNJABI", "BALOCHI", "BIHARI", "MEMON", "GUJRATI", "BROHI", "HINDKO", "GILGITI", "SARAIKI", "BANGALI", "AFGHAN", "HAZARA", "OTHER ETHNICITY", "UNKNOWN", "REFUSED"});
        otherEthinicity = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER ETHNICITY");
        contactEducationLevel = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_contact_education_level), getResources().getStringArray(R.array.pet_contact_education_levels), getResources().getString(R.string.pet_intermediate), App.VERTICAL, true, "HIGHEST EDUCATION LEVEL", new String[]{"ELEMENTARY EDUCATION", "PRIMARY EDUCATION", "SECONDARY EDUCATION", "INTERMEDIATE EDUCATION", "UNDERGRADUATE EDUCATION", "GRADUATE EDUCATION", "DOCTORATE EDUCATION", "RELIGIOUS EDUCATION", "POLYTECHNIC EDUCATION", "SPECIAL EDUCATION RECEIVED", "NO FORMAL EDUCATION", "OTHER", "UNKNOWN", "REFUSED"});
        otherContactEducationLevel = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER EDUCATION LEVEL");
        maritalStatus = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_martial_status), getResources().getStringArray(R.array.pet_martial_statuses), getResources().getString(R.string.pet_single), App.VERTICAL, true, "MARITAL STATUS", new String[]{"SINGLE", "ENGAGED", "MARRIED", "SEPARATED", "DIVORCED", "WIDOWED", "OTHER", "REFUSE", "UNKNOWN"});
        emloyementStatus = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_employement_status), getResources().getStringArray(R.array.pet_employement_statuses), getResources().getString(R.string.pet_employed), App.VERTICAL, true, "EMPLOYMENT STATUS", new String[]{"EMPLOYED", "UNABLE TO WORK", "STUDENT", "UNEMPLOYED", "HOUSEWORK", "RETIRED", "REFUSE", "UNKNOWN"});
        occupation = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_occupation), getResources().getStringArray(R.array.pet_occupations), getString(R.string.pet_artist), App.VERTICAL, true, "OCCUPATION", new String[]{"ARTIST", "BEGGAR", "CARPENTER", "CASUAL LABOR", "CHILD LABOR", "CLERK", "MEDICAL OFFICER/DOCTOR", "DRIVER", "ENGINEER", "FARMER", "FISHERMAN", "FOOD VENDOR", "FORESTRY WORKER", "HOUSEWORK", "MACHINE OPERATOR", "MINER", "PILOT", "PLUMBER", "PROFESSIONAL", "REPORTER", "SALES REPRESENTATIVE", "SECURITY OFFICER", "SHEPHERD", "SLUM WORKER", "SMALL BUSINESS OWNER", "TAILOR", "TRADER", "TEACHER", "VEGETABLE SELLER", "WAITER", "OTHER", "UNKNOWN"});
        otherOccupation = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER OCCUPATION");

        familyStructure = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_family_structure), getResources().getStringArray(R.array.pet_family_structures), getResources().getString(R.string.pet_joint_family), App.HORIZONTAL, App.HORIZONTAL, true, "FAMILY STRUCTURE", new String[]{"JOINT FAMILY", "NUCLEAR FAMILY"});
        numberOfFamilyMembers = new TitledEditText(context, null, getResources().getString(R.string.pet_members_number), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "FAMILY SIZE");
        earningMembers = new TitledEditText(context, null, getResources().getString(R.string.pet_earning_members), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "EARNING FAMILY MEMBERS");
        contactIncome = new TitledEditText(context, null, getResources().getString(R.string.pet_contact_income), "0", "", 9, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "MONTHLY INCOME");
        contactIncomeGroup = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_contact_income_group), getResources().getStringArray(R.array.pet_income_group), "", App.HORIZONTAL, App.VERTICAL, true, "INCOME CLASS", new String[]{"NONE", "LOWER INCOME CLASS", "LOWER MIDDLE INCOME CLASS", "MIDDLE INCOME CLASS", "UPPER MIDDLE INCOME CLASS", "UPPER INCOME CLASS"});
        residenceType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_residence_type), getResources().getStringArray(R.array.pet_residence_types), getResources().getString(R.string.pet_rent), App.HORIZONTAL, App.VERTICAL, true, "HOUSE TYPE", new String[]{"RENTED", "OWNED", "UNKNOWN"});
        noOfRooms = new TitledEditText(context, null, getResources().getString(R.string.pet_room_nos), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "NUMBER OF ROOMS (IN HOUSE)");
        medicalCondition = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_contact_psychiatric_medical_condition), getResources().getStringArray(R.array.pet_contact_psychiatric_medical_conditions), null, App.VERTICAL, App.VERTICAL, true, "GENERAL MEDICAL CONDITION", new String[]{"EPILEPSY", "MENTAL RETARDATION", "DISABILITY", "HUMAN IMMUNODEFICIENCY VIRUS", "HEPATITIS C VIRUS INFECTION", "DIABETES MELLITUS", "NONE", "OTHER DISEASE"});
        otherMedicalCondition = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER DISEASE");

        // second page views...

        counsellingProvidedTo = new TitledCheckBoxes(context, getResources().getString(R.string.pet_counselling_procedure), getResources().getString(R.string.pet_counselling_to), getResources().getStringArray(R.array.pet_cnic_owners), null, App.VERTICAL, App.VERTICAL, true, "FAMILY MEMBERS COUNSELLED", new String[]{"SELF", "MOTHER", "FATHER", "MATERNAL GRANDMOTHER", "MATERNAL GRANDFATHER", "PATERNAL GRANDMOTHER", "PATERNAL GRANDFATHER", "BROTHER", "SISTER", "SON", "DAUGHTER", "SPOUSE", "AUNT", "UNCLE", "OTHER"});
        counsellingProvidedToOther = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER FAMILY MEMBER");


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
        procedureExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_procedure_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "COUNSELLING PROCEDURE (TEXT)");
        procedureExplanation.getEditText().setSingleLine(false);
        procedureExplanation.getEditText().setMinimumHeight(150);
        //purpose = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_purpose), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        purposeExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_purpose_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "COUNSELLING PURPOSE (TEXT)");
        purposeExplanation.getEditText().setSingleLine(false);
        purposeExplanation.getEditText().setMinimumHeight(150);
        //durationOfPet = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_duration_of_pet), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        durationOfPetExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_duration_of_pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "COUNSELLING DURATION (TEXT)");
        durationOfPetExplanation.getEditText().setSingleLine(false);
        durationOfPetExplanation.getEditText().setMinimumHeight(150);
        //infectionControlMeasures = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_infection_control_measures), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        infectionControlMeasuresExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_infection_control_measures_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "COUNSELLING CONTROL MEASURES (TEXT)");
        infectionControlMeasuresExplanation.getEditText().setSingleLine(false);
        infectionControlMeasuresExplanation.getEditText().setMinimumHeight(150);
        //transmission = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_transmission), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        transmissionExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_transmission_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "COUNSELLING TRANSMISSION (TEXT)");
        transmissionExplanation.getEditText().setSingleLine(false);
        transmissionExplanation.getEditText().setMinimumHeight(150);
        //adherence = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adherence), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        adherenceExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_adherence_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "COUNSELLING ADHERENCE (TEXT)");
        adherenceExplanation.getEditText().setSingleLine(false);
        adherenceExplanation.getEditText().setMinimumHeight(150);
        //nutrition = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_nutrition), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        nutritionExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_nutrition_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "NUTRITION COUNSELING (TEXT)");
        nutritionExplanation.getEditText().setSingleLine(false);
        nutritionExplanation.getEditText().setMinimumHeight(150);
        //commonAdverseEffects = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_common_adverse_effects), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        commonAdverseEffectsExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_common_adverse_effects_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "COUNSELLING ADVERSE EVENTS (TEXT)");
        commonAdverseEffectsExplanation.getEditText().setSingleLine(false);
        commonAdverseEffectsExplanation.getEditText().setMinimumHeight(150);
        //misconception = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_misconception), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        misconceptionExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_misconception_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "COUNSELLING MISCONCEPTION (TEXT)");
        misconceptionExplanation.getEditText().setSingleLine(false);
        misconceptionExplanation.getEditText().setMinimumHeight(150);
        //followup = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_followup), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.HORIZONTAL);
        followupExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_followup_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "COUNSELLING FOLLOW-UP (TEXT)");
        followupExplanation.getEditText().setSingleLine(false);
        followupExplanation.getEditText().setMinimumHeight(150);
        questionsByContact = new TitledEditText(context, null, getResources().getString(R.string.pet_question_by_contact), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "PATIENT QUESTIONS");
        questionsByContact.getEditText().setSingleLine(false);
        questionsByContact.getEditText().setMinimumHeight(150);
        psycologistAnswer = new TitledEditText(context, null, getResources().getString(R.string.pet_psychologist_answer), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "PSYCHOLOGIST COUNSELLING EXPLANATION");
        psycologistAnswer.getEditText().setSingleLine(false);
        psycologistAnswer.getEditText().setMinimumHeight(150);
        contactBehavior = new TitledSpinner(context, "", getResources().getString(R.string.pet_contact_behaviour), getResources().getStringArray(R.array.pet_contact_behaviours), "", App.VERTICAL, true, "BEHAVIOUR", new String[]{"IRRITABILITY", "STUBBORN BEHAVIOUR", "INTROVERTED PERSONALITY", "AGGRESSIVE BEHAVIOUR", "ARGUMENTATIVE BEHAVIOUR", "NON COMPLIANT BEHAVIOUR", "COMPLIANT BEHAVIOUR", "RESPONSIBLE PERSONALITY", "COOPERATIVE BEHAVIOUR", "NON-COOPERATIVE BEHAVIOUR", "OTHER PERSONALITY TYPE"});
        otherBehavior = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 70, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER PERSONALITY TYPE");
        caretakerComments = new TitledEditText(context, null, getResources().getString(R.string.pet_caretaker_comments), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "CARETAKER COMMENTS");
        caretakerComments.getEditText().setSingleLine(false);
        caretakerComments.getEditText().setMinimumHeight(150);
        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.pet_psychologist_comment), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "CLINICIAN NOTES (TEXT)");
        clincianNote.getEditText().setSingleLine(false);
        clincianNote.getEditText().setMinimumHeight(150);

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

        // Used for reset fields...
        views = new View[]{formDate.getButton(), familyStructure.getRadioGroup(), numberOfFamilyMembers.getEditText(), earningMembers.getEditText(), contactIncomeGroup.getRadioGroup(), counsellingProvidedTo, counsellingProvidedToOther.getEditText(),
                residenceType.getRadioGroup(), noOfRooms.getEditText(), medicalCondition, procedureExplanation.getEditText(), purposeExplanation.getEditText(),
                durationOfPetExplanation.getEditText(), infectionControlMeasuresExplanation.getEditText(), counsellingRegarding,
                transmissionExplanation.getEditText(), adherenceExplanation.getEditText(), nutritionExplanation.getEditText(),
                commonAdverseEffectsExplanation.getEditText(), misconceptionExplanation.getEditText(), followupExplanation.getEditText(),
                questionsByContact.getEditText(), psycologistAnswer.getEditText(), contactBehavior.getSpinner(), otherBehavior.getEditText(), contactIncome.getEditText(), clincianNote.getEditText(), caretakerComments.getEditText(),
                ethinicity.getSpinner(), otherEthinicity.getEditText(), contactEducationLevel.getSpinner(), maritalStatus.getSpinner(), emloyementStatus.getSpinner(), occupation.getSpinner(), referredTo, referalReasonPsychologist, otherReferalReasonPsychologist.getEditText(), referalReasonSupervisor, otherReferalReasonSupervisor.getEditText(),
                referalReasonCallCenter, otherReferalReasonCallCenter.getEditText(), referalReasonClinician, otherReferalReasonClinician.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, ethinicity, otherEthinicity, contactEducationLevel, otherContactEducationLevel, maritalStatus, emloyementStatus, occupation, otherOccupation, familyStructure, numberOfFamilyMembers, earningMembers, contactIncome, contactIncomeGroup, residenceType, noOfRooms, medicalCondition, otherMedicalCondition},
                        {counsellingProvidedTo,counsellingProvidedToOther},
                        {counsellingRegarding, procedureExplanation, purposeExplanation, durationOfPetExplanation, infectionControlMeasuresExplanation, transmissionExplanation, adherenceExplanation, nutritionExplanation, commonAdverseEffectsExplanation, misconceptionExplanation, followupExplanation},
                        {questionsByContact, psycologistAnswer, contactBehavior, otherBehavior, caretakerComments, clincianNote, patientReferred, referredTo, referalReasonPsychologist, otherReferalReasonPsychologist, referalReasonSupervisor, otherReferalReasonSupervisor,
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
                    if (no > 50)
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
                    if (no > 15)
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
                    contactIncomeGroup.getRadioGroup().selectValue(getResources().getString(R.string.pet_none));
                else {
                    Float income = Float.parseFloat(App.get(contactIncome));
                    if (income == 0)
                        contactIncomeGroup.getRadioGroup().selectValue(getResources().getString(R.string.pet_none));
                    else if (income >= 1 && income <= 7000)
                        contactIncomeGroup.getRadioGroup().selectValue(getResources().getString(R.string.pet_lower_class));
                    else if (income >= 7001 && income <= 35000)
                        contactIncomeGroup.getRadioGroup().selectValue(getResources().getString(R.string.pet_lower_middle_class));
                    else if (income >= 35001 && income <= 87000)
                        contactIncomeGroup.getRadioGroup().selectValue(getResources().getString((R.string.pet_middle_class)));
                    else if (income >= 87001 && income <= 173000)
                        contactIncomeGroup.getRadioGroup().selectValue(getResources().getString(R.string.pet_upper_middle_class));
                    else
                        contactIncomeGroup.getRadioGroup().selectValue(getResources().getString(R.string.pet_upper_class));
                }


            }
        });
        //contactIncomeGroup.getEditText().setKeyListener(null);

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

        resetViews();
    }

    @Override
    public void updateDisplay() {

        if (refillFlag) {
            refillFlag = true;
            return;
        }

        if (snackbar != null)
            snackbar.dismiss();

        formDate.setEnabled(true);

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
        otherBehavior.setVisibility(View.GONE);

        if (App.getPatient().getPerson().getAge() < 15)
            contactBehavior.setVisibility(View.VISIBLE);
        else {
            contactBehavior.setVisibility(View.GONE);
            otherBehavior.setVisibility(View.GONE);
        }

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
        Boolean error = super.validate();

        Boolean flag = true;


        if (!App.get(numberOfFamilyMembers).isEmpty() && numberOfFamilyMembers.getVisibility() == View.VISIBLE) {


            int no = Integer.parseInt(String.valueOf(App.get(numberOfFamilyMembers)));
            if (no > 50) {
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


        String[][] concept = serverService.getConceptUuidAndDataType(ethinicity.getConceptAnswers()[ethinicity.getSpinner().getSelectedItemPosition()]);
        personAttribute.put("Ethnicity", concept[0][0]);

        concept = serverService.getConceptUuidAndDataType(contactEducationLevel.getConceptAnswers()[contactEducationLevel.getSpinner().getSelectedItemPosition()]);
        personAttribute.put("Education Level", concept[0][0]);

        concept = serverService.getConceptUuidAndDataType(maritalStatus.getConceptAnswers()[maritalStatus.getSpinner().getSelectedItemPosition()]);
        personAttribute.put("Marital Status", concept[0][0]);

        concept = serverService.getConceptUuidAndDataType(emloyementStatus.getConceptAnswers()[emloyementStatus.getSpinner().getSelectedItemPosition()]);
        personAttribute.put("Employment Status", concept[0][0]);

        concept = serverService.getConceptUuidAndDataType(occupation.getConceptAnswers()[occupation.getSpinner().getSelectedItemPosition()]);
        personAttribute.put("Occupation", concept[0][0]);


        if (procedure.isChecked())
            observations.add(new String[]{"COUNSELLING PROCEDURE", "YES"});
        else
            observations.add(new String[]{"COUNSELLING PROCEDURE", "NO"});

        if (purpose.isChecked())
            observations.add(new String[]{"COUNSELLING PURPOSE", "YES"});
        else
            observations.add(new String[]{"COUNSELLING PURPOSE", "NO"});

        if (durationOfPet.isChecked())
            observations.add(new String[]{"COUNSELLING DURATION", "YES"});
        else
            observations.add(new String[]{"COUNSELLING DURATION", "NO"});

        if (infectionControlMeasures.isChecked())
            observations.add(new String[]{"COUNSELLING CONTROL MEASURES", "YES"});
        else
            observations.add(new String[]{"COUNSELLING CONTROL MEASURES", "NO"});

        if (transmission.isChecked())
            observations.add(new String[]{"COUNSELLING TRANSMISSION", "YES"});
        else
            observations.add(new String[]{"COUNSELLING TRANSMISSION", "NO"});

        if (adherence.isChecked())
            observations.add(new String[]{"COUNSELLING ADHERENCE", "YES"});
        else
            observations.add(new String[]{"COUNSELLING ADHERENCE", "NO"});

        if (nutrition.isChecked())
            observations.add(new String[]{"NUTRITION COUNSELING", "YES"});
        else
            observations.add(new String[]{"NUTRITION COUNSELING", "NO"});

        if (commonAdverseEffects.isChecked())
            observations.add(new String[]{"COUNSELLING ADVERSE EVENTS", "YES"});
        else
            observations.add(new String[]{"COUNSELLING ADVERSE EVENTS", "NO"});

        if (misconception.isChecked())
            observations.add(new String[]{"COUNSELLING MISCONCEPTION", "YES"});
        else
            observations.add(new String[]{"COUNSELLING MISCONCEPTION", "NO"});

        if (followup.isChecked())
            observations.add(new String[]{"COUNSELLING FOLLOW-UP", "YES"});
        else
            observations.add(new String[]{"COUNSELLING FOLLOW-UP", "NO"});


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

        if (buttonView == procedure) {
            if (isChecked)
                procedureExplanation.setVisibility(View.VISIBLE);
            else
                procedureExplanation.setVisibility(View.GONE);
        } else if (buttonView == purpose) {
            if (isChecked)
                purposeExplanation.setVisibility(View.VISIBLE);
            else
                purposeExplanation.setVisibility(View.GONE);
        } else if (buttonView == durationOfPet) {
            if (isChecked)
                durationOfPetExplanation.setVisibility(View.VISIBLE);
            else
                durationOfPetExplanation.setVisibility(View.GONE);
        } else if (buttonView == infectionControlMeasures) {
            if (isChecked)
                infectionControlMeasuresExplanation.setVisibility(View.VISIBLE);
            else
                infectionControlMeasuresExplanation.setVisibility(View.GONE);
        } else if (buttonView == transmission) {
            if (isChecked)
                transmissionExplanation.setVisibility(View.VISIBLE);
            else
                transmissionExplanation.setVisibility(View.GONE);
        } else if (buttonView == adherence) {
            if (isChecked)
                adherenceExplanation.setVisibility(View.VISIBLE);
            else
                adherenceExplanation.setVisibility(View.GONE);
        } else if (buttonView == nutrition) {
            if (isChecked)
                nutritionExplanation.setVisibility(View.VISIBLE);
            else
                nutritionExplanation.setVisibility(View.GONE);
        } else if (buttonView == commonAdverseEffects) {
            if (isChecked)
                commonAdverseEffectsExplanation.setVisibility(View.VISIBLE);
            else
                commonAdverseEffectsExplanation.setVisibility(View.GONE);
        } else if (buttonView == misconception) {
            if (isChecked)
                misconceptionExplanation.setVisibility(View.VISIBLE);
            else
                misconceptionExplanation.setVisibility(View.GONE);
        } else if (buttonView == followup) {
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
        refillFlag = true;

        OfflineForm fo = serverService.getSavedFormById(encounterId);

        ArrayList<String[][]> obsValue = fo.getObsValue();


        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("COUNSELLING PROCEDURE") && obs[0][1].equals("YES")) {
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
