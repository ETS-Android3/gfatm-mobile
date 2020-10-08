package com.ihsinformatics.gfatmmobile.common;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
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
import android.view.inputmethod.EditorInfo;
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
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
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
 * Created by Haris on 2/21/2017.
 */

public class BaselineCounselingForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...

    TitledRadioGroup family_structure;
    TitledEditText family_size;
    TitledEditText earning_members;
    TitledEditText monthly_household_income;
    TitledRadioGroup income_class;
    TitledRadioGroup residence_type;
    TitledEditText number_rooms_house;
    TitledRadioGroup education_level;
    TitledRadioGroup marital_status;
    TitledRadioGroup children;
    TitledEditText children_number;
    TitledRadioGroup counselling;
    TextView heading_disease_info;
    TitledRadioGroup tb_infection_type;
    TitledRadioGroup tb_type;
    TitledEditText extra_pulmonary_site;
    TitledEditText diagnosis_type;
    TitledEditText drug_resistance_profile;
    TitledCheckBoxes medical_condition;
    TitledEditText other_disease;
    TextView heading_contact_info;
    TitledRadioGroup drug_abuse_history;
    TitledCheckBoxes substance_abuse;
    TitledEditText drug_substance_type_other;
    TitledEditText past_drug_abuse_age;
    TitledEditText akuads_score;
    TextView heading_psychotic_features_screening;
    TitledRadioGroup psychotic_symptom_in_past;
    TitledRadioGroup hallucination;
    TitledEditText hallucination_type;
    TitledRadioGroup delusion;
    TitledEditText delusion_type;
    TextView heading_patient_awareness_about_tb;
    TitledCheckBoxes counseling_provided_for;
    TitledCheckBoxes patient_behaviour;
    TitledRadioGroup counsel_next_followup;
    TitledEditText counselor_comments;
    /////////////////////
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

    TitledButton returnVisitDate;


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
        formName = Forms.BASELINE_COUNSELING;
        form = Forms.baselineCounseling;

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
                ScrollView scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
                ScrollView scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        String columnName = "";
        final Object[][] locations = serverService.getAllLocationsFromLocalDB(columnName);
        String[] locationArray = new String[locations.length + 2];
        locationArray[0] = "";
        int j = 1;
        for (int i = 0; i < locations.length; i++) {
            locationArray[j] = String.valueOf(locations[i][16]);
            j++;
        }

        family_structure = new TitledRadioGroup(context, null, getResources().getString(R.string.common_family_structure), getResources().getStringArray(R.array.common_family_structure_options), getResources().getString(R.string.common_family_structure_joint), App.VERTICAL, App.VERTICAL, true, "FAMILY STRUCTURE", new String[]{"NUCLEAR FAMILY", "JOINT FAMILY", "SINGLE"});
        family_size = new TitledEditText(context, null, getResources().getString(R.string.common_family_size), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false, "FAMILY SIZE");
        earning_members = new TitledEditText(context, null, getResources().getString(R.string.common_earning_members), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false, "EARNING FAMILY MEMBERS");
        monthly_household_income = new TitledEditText(context, null, getResources().getString(R.string.common_monthly_household_income), "", "", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false, "TOTAL MONTHLY HOUSEHOLD INCOME");
        income_class = new TitledRadioGroup(context, null, getResources().getString(R.string.common_income_class), getResources().getStringArray(R.array.common_income_class_options), null, App.VERTICAL, App.VERTICAL, true, "INCOME CLASS", new String[]{"NONE", "LOWER INCOME CLASS", "LOWER MIDDLE INCOME CLASS", "MIDDLE INCOME CLASS", "UPPER MIDDLE INCOME CLASS", "UPPER INCOME CLASS"});
        residence_type = new TitledRadioGroup(context, null, getResources().getString(R.string.common_residence_type), getResources().getStringArray(R.array.common_residence_type_options), getString(R.string.common_residence_type_rent), App.VERTICAL, App.VERTICAL, true, "HOUSE TYPE", new String[]{"RENTED", "OWNED", "SHARED"});
        number_rooms_house = new TitledEditText(context, null, getResources().getString(R.string.common_number_rooms_house), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false, "NUMBER OF ROOMS (IN HOUSE)");
        education_level = new TitledRadioGroup(context, null, getResources().getString(R.string.common_education_level), getResources().getStringArray(R.array.common_education_level_options), getString(R.string.common_education_level_secondary), App.VERTICAL, App.VERTICAL, true, "HIGHEST EDUCATION LEVEL", new String[]{"ELEMENTARY EDUCATION", "PRIMARY EDUCATION", "SECONDARY EDUCATION", "INTERMEDIATE EDUCATION", "UNDERGRADUATE EDUCATION", "GRADUATE EDUCATION", "DOCTORATE EDUCATION", "POLYTECHNIC EDUCATION", "SPECIAL EDUCATION RECEIVED", "RELIGIOUS EDUCATION", "NO FORMAL EDUCATION", "ENTREPRENEUR"});
        marital_status = new TitledRadioGroup(context, null, getResources().getString(R.string.common_marital_status), getResources().getStringArray(R.array.common_marital_status_options), null, App.VERTICAL, App.VERTICAL, true, "MARITAL STATUS", new String[]{"SINGLE", "ENGAGED", "MARRIED", "SEPARATED", "DIVORCED", "WIDOWED", "UNKNOWN", "REFUSED"});
        children = new TitledRadioGroup(context, null, getResources().getString(R.string.common_children), getResources().getStringArray(R.array.common_children_options), getString(R.string.yes), App.VERTICAL, App.VERTICAL, true, "CHILDREN", getResources().getStringArray(R.array.yes_no_list_concept));
        children_number = new TitledEditText(context, null, getResources().getString(R.string.common_children_number), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true, "TOTAL NUMBER OF CHILDREN");
        counselling = new TitledRadioGroup(context, null, getResources().getString(R.string.common_counselling), getResources().getStringArray(R.array.common_counselling_options), getResources().getString(R.string.common_counselling_self), App.VERTICAL, App.VERTICAL, true, "FAMILY MEMBERS COUNSELLED", new String[]{"SELF", "PARENT", "GUARDIAN", "MOTHER", "FATHER", "MATERNAL GRANDMOTHER", "MATERNAL GRANDFATHER", "PATERNAL GRANDMOTHER", "PATERNAL GRANDFATHER", "BROTHER", "SISTER", "SON", "DAUGHTER", "SPOUSE", "AUNT", "NEIGHBOR", "UNCLE", "FRIEND", "COUSIN", "IN-LAWS"});
        tb_infection_type = new TitledRadioGroup(context, null, getResources().getString(R.string.common_tb_infection_type), getResources().getStringArray(R.array.common_tb_infection_type_options), null, App.VERTICAL, App.VERTICAL, false);
        tb_type = new TitledRadioGroup(context, null, getResources().getString(R.string.common_tb_type), getResources().getStringArray(R.array.common_tb_type_options), null, App.VERTICAL, App.VERTICAL, false);
        extra_pulmonary_site = new TitledEditText(context, null, getResources().getString(R.string.common_extra_pulmonary_site), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        diagnosis_type = new TitledEditText(context, null, getResources().getString(R.string.common_diagnosis_type), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        drug_resistance_profile = new TitledEditText(context, null, getResources().getString(R.string.common_drug_resistance_profile), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        medical_condition = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_medical_condition), getResources().getStringArray(R.array.common_medical_condition_options), new Boolean[]{true}, App.VERTICAL, App.VERTICAL, true, "GENERAL MEDICAL CONDITION", new String[]{"DIABETES MELLITUS", "HYPERTENSION", "EPILEPSY", "INTELLECTUAL FUNCTIONING DISABILITY", "PHYSICALLY DISABLE", "HUMAN IMMUNODEFICIENCY VIRUS", "HEPATITIS C VIRUS INFECTION", "OTHER DISEASE", "NONE"});
        other_disease = new TitledEditText(context, null, getResources().getString(R.string.common_medical_condition_specify_other), "", "", 25, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER DISEASE");
        drug_abuse_history = new TitledRadioGroup(context, null, getResources().getString(R.string.common_drug_abuse_history), getResources().getStringArray(R.array.common_drug_abuse_history_options), null, App.VERTICAL, App.VERTICAL, false, "HISTORY OF DRUG ABUSE", new String[]{"YES", "NO", "PAST", "REFUSED"});
        substance_abuse = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_substance_abuse), getResources().getStringArray(R.array.common_substance_abuset_options), null, App.VERTICAL, App.VERTICAL, false, "SUBSTANCE ABUSE", new String[]{"SMOKING", "ALCOHOL ABUSE", "DRUGS/SUBSTANCES", "ALL", "OTHER DRUG / SUBSTANCE TYPE"});
        drug_substance_type_other = new TitledEditText(context, null, getResources().getString(R.string.common_drug_substance_type_other), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER DRUG / SUBSTANCE TYPE");
        past_drug_abuse_age = new TitledEditText(context, null, getResources().getString(R.string.common_past_drug_abuse_age), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true, "INITIAL AGE OF SUBSTANCE ABUSE");
        akuads_score = new TitledEditText(context, null, getResources().getString(R.string.common_akuads_score), "", "", 2, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false, "AKUADS SCORE");

        psychotic_symptom_in_past = new TitledRadioGroup(context, null, getResources().getString(R.string.common_psychotic_symptom_in_past), getResources().getStringArray(R.array.yes_no_options), getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "PSYCHOTIC SYMPTOM IN PAST", getResources().getStringArray(R.array.yes_no_list_concept));

        hallucination = new TitledRadioGroup(context, null, getResources().getString(R.string.common_hallucination), getResources().getStringArray(R.array.common_hallucination_options), getString(R.string.yes), App.VERTICAL, App.VERTICAL, true, "HALLUCINATION", new String[]{"YES", "NO", "UNKNOWN"});
        hallucination_type = new TitledEditText(context, null, getResources().getString(R.string.common_hallucination_type), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "HALLUCINATION TYPE");
        delusion = new TitledRadioGroup(context, null, getResources().getString(R.string.common_delusion), getResources().getStringArray(R.array.common_delusion_options), getString(R.string.yes), App.VERTICAL, App.VERTICAL, true, "DELUSION", new String[]{"YES", "NO", "UNKNOWN"});
        delusion_type = new TitledEditText(context, null, getResources().getString(R.string.common_delusion_type), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "DELUSION TYPE");
        counseling_provided_for = new TitledCheckBoxes(context, null, getResources().getString(R.string.counseling_provided_for), getResources().getStringArray(R.array.counseling_provided_for_options), new Boolean[]{true}, App.VERTICAL, App.VERTICAL, true, "COUNSELING PROVIDED FOR", new String[]{"TYPES OF TB", "TB SYMPTOMS", "IMPORTANCE OF NUTRITION", "DURATION OF TB TREATMENT", "TREATMENT PROCEDURE", "PREVENTIVE TREATMENT (IPT)", "ADVERSE EVENTS AND THEIR MANAGEMENT", "INFECTION CONTROL", "TRANSMISSION OF TB", "IMPORTANCE OF TREATMENT ADHERENCE", "IMPORTANCE OF CONTACT SCREENING", "IMPORTANCE OF REGULAR MONTHLY FOLLOW UP", "TB HELPLINE NUMBER", "PREGNANCY", "IMPORTANCE OF SMOKING CESSATION", "ROLE OF TREATMENT COORDINATOR", "BREASTFEEDING"});
        patient_behaviour = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_patient_behaviour), getResources().getStringArray(R.array.common_patient_behaviour_options), new Boolean[]{true}, App.VERTICAL, App.VERTICAL, true, "BEHAVIOUR", new String[]{"NORMAL", "IRRITABILITY", "STUBBORN BEHAVIOUR", "INTROVERTED PERSONALITY", "AGGRESSIVE BEHAVIOUR", "ARGUMENTATIVE BEHAVIOUR", "NON COMPLIANT BEHAVIOUR", "COMPLIANT BEHAVIOUR", "COOPERATIVE BEHAVIOUR", "NON-COOPERATIVE BEHAVIOUR"});
        counsel_next_followup = new TitledRadioGroup(context, null, getResources().getString(R.string.common_counsel_next_followup), getResources().getStringArray(R.array.common_counsel_next_followup_options), getString(R.string.yes), App.VERTICAL, App.VERTICAL, true, "COUNSELING REQUIRED ON NEXT FOLLOW UP", getResources().getStringArray(R.array.yes_no_list_concept));
        counselor_comments = new TitledEditText(context, null, getResources().getString(R.string.common_counselor_comments), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "COUNSELOR COMMENTS");
/////////////////
        patientReferred = new TitledRadioGroup(context, null, getResources().getString(R.string.refer_patient), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL, true, "PATIENT REFERRED", getResources().getStringArray(R.array.yes_no_list_concept));
        referredTo = new TitledCheckBoxes(context, null, getResources().getString(R.string.refer_patient_to), getResources().getStringArray(R.array.refer_patient_to_option), null, App.VERTICAL, App.VERTICAL, true, "PATIENT REFERRED TO", new String[]{"COUNSELOR", "PSYCHOLOGIST", "CLINICAL OFFICER/DOCTOR", "CALL CENTER", "FIELD SUPERVISOR", "SITE SUPERVISOR"});
        referalReasonPsychologist = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_psychologist), getResources().getStringArray(R.array.referral_reason_for_psychologist_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR PSYCHOLOGIST/COUNSELOR REFERRAL", new String[]{"CHECK FOR TREATMENT ADHERENCE", "PSYCHOLOGICAL EVALUATION", "BEHAVIORAL ISSUES", "REFUSAL OF TREATMENT BY PATIENT", "OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR"});
        otherReferalReasonPsychologist = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR");
        referalReasonSupervisor = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_supervisor), getResources().getStringArray(R.array.referral_reason_for_supervisor_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR SUPERVISOR REFERRAL", new String[]{"CONTACT SCREENING REMINDER", "TREATMENT FOLLOWUP REMINDER", "CHECK FOR TREATMENT ADHERENCE", "INVESTIGATION OF REPORT COLLECTION", "ADVERSE EVENTS", "MEDICINE COLLECTION", "OTHER REFERRAL REASON TO SUPERVISOR"});
        otherReferalReasonSupervisor = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO SUPERVISOR");
        referalReasonCallCenter = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_center), getResources().getStringArray(R.array.referral_reason_for_call_center_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR CALL CENTER REFERRAL", new String[]{"CONTACT SCREENING REMINDER", "TREATMENT FOLLOWUP REMINDER", "CHECK FOR TREATMENT ADHERENCE", "INVESTIGATION OF REPORT COLLECTION", "ADVERSE EVENTS", "MEDICINE COLLECTION", "OTHER REFERRAL REASON TO CALL CENTER"});
        otherReferalReasonCallCenter = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO CALL CENTER");
        referalReasonClinician = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_clinician), getResources().getStringArray(R.array.referral_reason_for_clinician_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR CLINICIAN REFERRAL", new String[]{"EXPERT OPINION", "ADVERSE EVENTS", "OTHER REFERRAL REASON TO CLINICIAN"});
        otherReferalReasonClinician = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO CLINICIAN");

        heading_disease_info = new TextView(context);
        heading_contact_info = new TextView(context);
        heading_psychotic_features_screening = new TextView(context);
        heading_patient_awareness_about_tb = new TextView(context);

        heading_disease_info.setText("Disease Information");
        heading_contact_info.setText("Substance Information");
        heading_psychotic_features_screening.setText("Psycho analysis");
        heading_patient_awareness_about_tb.setText("Patient awareness about TB");

        heading_disease_info.setTypeface(null, Typeface.BOLD);
        heading_contact_info.setTypeface(null, Typeface.BOLD);
        heading_psychotic_features_screening.setTypeface(null, Typeface.BOLD);
        heading_patient_awareness_about_tb.setTypeface(null, Typeface.BOLD);

        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.pet_family_visit_date), "", App.VERTICAL);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), family_structure.getRadioGroup(), family_size.getEditText(), earning_members.getEditText(), monthly_household_income.getEditText(), income_class.getRadioGroup(),
                residence_type.getRadioGroup(), number_rooms_house.getEditText(), education_level.getRadioGroup(),
                marital_status.getRadioGroup(), children.getRadioGroup(), children_number.getEditText(), counselling.getRadioGroup(), heading_disease_info, tb_infection_type.getRadioGroup(),
                tb_type.getRadioGroup(), extra_pulmonary_site.getEditText(), diagnosis_type.getEditText(), drug_resistance_profile.getEditText(),
                medical_condition, other_disease.getEditText(), heading_contact_info, drug_abuse_history.getRadioGroup(), substance_abuse, drug_substance_type_other.getEditText(),
                past_drug_abuse_age.getEditText(), akuads_score.getEditText(), heading_psychotic_features_screening, psychotic_symptom_in_past.getRadioGroup(), hallucination.getRadioGroup(), hallucination_type.getEditText(), delusion.getRadioGroup(), delusion_type.getEditText(), heading_patient_awareness_about_tb,
                counseling_provided_for, patient_behaviour,
                counsel_next_followup.getRadioGroup(), counselor_comments.getEditText(), patientReferred.getRadioGroup(), referredTo, referalReasonPsychologist, otherReferalReasonPsychologist.getEditText(), referalReasonSupervisor, otherReferalReasonSupervisor.getEditText(),
                referalReasonCallCenter, otherReferalReasonCallCenter.getEditText(), referalReasonClinician, otherReferalReasonClinician.getEditText(), returnVisitDate.getButton()};

        // Array used to display views accordingly....
        viewGroups = new View[][]{{formDate, family_structure, family_size, earning_members, monthly_household_income, income_class, residence_type, number_rooms_house, education_level,
                marital_status, children, children_number, counselling, heading_disease_info, tb_infection_type, tb_type, extra_pulmonary_site, diagnosis_type, drug_resistance_profile,
                medical_condition, other_disease, heading_contact_info, drug_abuse_history, substance_abuse, drug_substance_type_other, past_drug_abuse_age,
                akuads_score, heading_psychotic_features_screening, psychotic_symptom_in_past, hallucination, hallucination_type, delusion, delusion_type, patient_behaviour, heading_patient_awareness_about_tb,
                counseling_provided_for, counsel_next_followup, counselor_comments,
                patientReferred, referredTo, referalReasonPsychologist, otherReferalReasonPsychologist, referalReasonSupervisor, otherReferalReasonSupervisor,
                referalReasonCallCenter, otherReferalReasonCallCenter, referalReasonClinician, otherReferalReasonClinician, returnVisitDate},};

        formDate.getButton().setOnClickListener(this);
        residence_type.getRadioGroup().setOnCheckedChangeListener(this);
        education_level.getRadioGroup().setOnCheckedChangeListener(this);
        marital_status.getRadioGroup().setOnCheckedChangeListener(this);
        children.getRadioGroup().setOnCheckedChangeListener(this);
        counselling.getRadioGroup().setOnCheckedChangeListener(this);
        tb_type.getRadioGroup().setOnCheckedChangeListener(this);
        psychotic_symptom_in_past.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : medical_condition.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        drug_abuse_history.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : substance_abuse.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        hallucination.getRadioGroup().setOnCheckedChangeListener(this);
        delusion.getRadioGroup().setOnCheckedChangeListener(this);
        returnVisitDate.getButton().setOnClickListener(this);

        counsel_next_followup.getRadioGroup().setOnCheckedChangeListener(this);
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

        monthly_household_income.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int income = 0;
                try {
                    income = Integer.parseInt(monthly_household_income.getEditText().getText().toString()) / Integer.parseInt(family_size.getEditText().getText().toString());
                } catch (Exception e) {
                    income = 0;
                }
                if (income == 0) {
                    income_class.getRadioGroup().getButtons().get(0).setChecked(true);
                } else if (income >= 1 && income <= 700) {
                    income_class.getRadioGroup().getButtons().get(1).setChecked(true);
                } else if (income >= 7001 && income <= 35000) {
                    income_class.getRadioGroup().getButtons().get(2).setChecked(true);
                } else if (income >= 35001 && income <= 87000) {
                    income_class.getRadioGroup().getButtons().get(3).setChecked(true);
                } else if (income >= 87001 && income <= 173000) {
                    income_class.getRadioGroup().getButtons().get(4).setChecked(true);
                } else if (income >= 173001) {
                    income_class.getRadioGroup().getButtons().get(5).setChecked(true);
                }

            }
        });
        family_size.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int income = 0;
                try {
                    income = Integer.parseInt(monthly_household_income.getEditText().getText().toString()) / Integer.parseInt(family_size.getEditText().getText().toString());
                } catch (Exception e) {
                    income = 0;
                }
                if (income == 0) {
                    income_class.getRadioGroup().getButtons().get(0).setChecked(true);
                } else if (income >= 1 && income <= 700) {
                    income_class.getRadioGroup().getButtons().get(1).setChecked(true);
                } else if (income >= 7001 && income <= 35000) {
                    income_class.getRadioGroup().getButtons().get(2).setChecked(true);
                } else if (income >= 35001 && income <= 87000) {
                    income_class.getRadioGroup().getButtons().get(3).setChecked(true);
                } else if (income >= 87001 && income <= 173000) {
                    income_class.getRadioGroup().getButtons().get(4).setChecked(true);
                } else if (income >= 173001) {
                    income_class.getRadioGroup().getButtons().get(5).setChecked(true);
                }

            }
        });


        diagnosis_type.getEditText().setSingleLine(false);
        diagnosis_type.getEditText().setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);


        resetViews();
    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();

        String formDa = formDate.getButton().getText().toString();
        String personDOB = App.getPatient().getPerson().getBirthdate();


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
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());


        }

        String nextAppointmentDateString = App.getSqlDate(secondDateCalendar);
        Date nextAppointmentDate = App.stringToDate(nextAppointmentDateString, "yyyy-MM-dd");

        String formDateString = App.getSqlDate(formDateCalendar);
        Date formStDate = App.stringToDate(formDateString, "yyyy-MM-dd");


        if (!(returnVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {
            Calendar dateToday = Calendar.getInstance();
            dateToday.add(Calendar.MONTH, 24);

            if (secondDateCalendar.before(formDateCalendar)) {

                if (!formDa.equals(""))
                    secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                if (!formDa.equals(""))
                    returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                else
                    returnVisitDate.getButton().setText("");

            } else if (nextAppointmentDate.compareTo(formStDate) == 0) {
                if (!formDa.equals(""))
                    secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                if (!formDa.equals(""))
                    returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                else
                    returnVisitDate.getButton().setText("");
            } else
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        }
        returnVisitDate.getButton().setEnabled(true);
        formDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = super.validate();
        View view = null;


        if (!family_size.equals("")) {
            try {
                if (family_size.getVisibility() == View.VISIBLE && Integer.parseInt(earning_members.getEditText().getText().toString().trim()) > 50) {
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    family_size.getEditText().setError("Value should be 0-75");
                    error = true;
                }
            } catch (Exception e) {
            }


        }

        if (!earning_members.equals("")) {
            try {
                if (earning_members.getVisibility() == View.VISIBLE && Integer.parseInt(earning_members.getEditText().getText().toString().trim()) > 50) {
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    earning_members.getEditText().setError("Value should be 0-75");
                    error = true;
                }
            } catch (Exception c) {
            }


        } else if (children_number.getVisibility() == View.VISIBLE && Integer.parseInt(children_number.getEditText().getText().toString().trim()) > 11) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            children_number.getEditText().setError("Value should be less then 12");
            error = true;
        } else if (akuads_score.getVisibility() == View.VISIBLE && Integer.parseInt(akuads_score.getEditText().getText().toString().trim()) > 75) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            akuads_score.getEditText().setError("Value should be 0-75");
            error = true;
        }


        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            //  DrawableCompat.setTint(clearIcon, color);
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

        final ArrayList<String[]> observations = getObservations();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
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

/*
        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});

        if (family_structure.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FAMILY STRUCTURE", App.get(family_structure).equals(getResources().getString(R.string.common_family_structure_nuclear)) ? "NUCLEAR FAMILY" :
                    App.get(family_structure).equals(getResources().getString(R.string.common_family_structure_joint)) ? "JOINT FAMILY" : "SINGLE"});
*/

   /*     if (family_size.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FAMILY SIZE", family_size.getEditText().getText().toString()});

        if (earning_members.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"EARNING FAMILY MEMBERS", earning_members.getEditText().getText().toString()});

        if (monthly_household_income.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TOTAL MONTHLY HOUSEHOLD INCOME", App.get(monthly_household_income)});
*/
/*        if (income_class.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"INCOME CLASS", App.get(income_class).equals(getResources().getString(R.string.common_income_class_none)) ? "NONE" :
                    App.get(income_class).equals(getResources().getString(R.string.common_income_class_lower)) ? "LOWER INCOME CLASS" :
                            App.get(income_class).equals(getResources().getString(R.string.common_income_class_lower_middle)) ? "LOWER MIDDLE INCOME CLASS" :
                                    App.get(income_class).equals(getResources().getString(R.string.common_income_class_middle)) ? "MIDDLE INCOME CLASS" :
                                            App.get(income_class).equals(getResources().getString(R.string.common_income_class_upper_middle)) ? "UPPER MIDDLE INCOME CLASS" : "UPPER INCOME CLASS"});

        if (residence_type.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HOUSE TYPE", App.get(residence_type).equals(getResources().getString(R.string.common_residence_type_own)) ? "OWNED" :
                    App.get(residence_type).equals(getResources().getString(R.string.common_residence_type_rent)) ? "RENTED" : "SHARED"});*/


      /*  if (number_rooms_house.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"NUMBER OF ROOMS (IN HOUSE)", App.get(number_rooms_house)});

        if (education_level.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HIGHEST EDUCATION LEVEL", App.get(education_level).equals(getResources().getString(R.string.common_education_level_elementery)) ? "ELEMENTARY EDUCATION" :
                    App.get(education_level).equals(getResources().getString(R.string.common_education_level_primary)) ? "PRIMARY EDUCATION" :
                            App.get(education_level).equals(getResources().getString(R.string.common_education_level_secondary)) ? "SECONDARY EDUCATION" :
                                    App.get(education_level).equals(getResources().getString(R.string.common_education_level_inter)) ? "INTERMEDIATE EDUCATION" :
                                            App.get(education_level).equals(getResources().getString(R.string.common_education_level_undergraduate)) ? "UNDERGRADUATE EDUCATION" :
                                                    App.get(education_level).equals(getResources().getString(R.string.common_education_level_graduate)) ? "GRADUATE EDUCATION" :
                                                            App.get(education_level).equals(getResources().getString(R.string.common_education_level_doctorate)) ? "DOCTORATE EDUCATION" :
                                                                    App.get(education_level).equals(getResources().getString(R.string.common_education_level_polytechnic)) ? "POLYTECHNIC EDUCATION" :
                                                                            App.get(education_level).equals(getResources().getString(R.string.common_education_level_special_education)) ? "SPECIAL EDUCATION RECEIVED" :
                                                                                    App.get(education_level).equals(getResources().getString(R.string.common_education_level_religious)) ? "RELIGIOUS EDUCATION" :
                                                                                            App.get(education_level).equals(getResources().getString(R.string.common_education_level_no_formal)) ? "NO FORMAL EDUCATION" : "ENTREPRENEUR"});
*/
/*
        if (marital_status.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"MARITAL STATUS", App.get(marital_status).equals(getResources().getString(R.string.common_marital_status_single)) ? "SINGLE" :
                    App.get(marital_status).equals(getResources().getString(R.string.common_marital_status_engaged)) ? "ENGAGED" :
                            App.get(marital_status).equals(getResources().getString(R.string.common_marital_status_married)) ? "MARRIED" :
                                    App.get(marital_status).equals(getResources().getString(R.string.common_marital_status_separated)) ? "SEPARATED" :
                                            App.get(marital_status).equals(getResources().getString(R.string.common_marital_status_divorced)) ? "DIVORCED" :
                                                    App.get(marital_status).equals(getResources().getString(R.string.common_marital_status_unknown)) ? "UNKNOWN" :
                                                            App.get(marital_status).equals(getResources().getString(R.string.common_marital_status_refused)) ? "REFUSED" : "WIDOWED"});

*/

   /*     if (children.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CHILDREN", App.get(children).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (psychotic_symptom_in_past.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PSYCHOTIC SYMPTOM IN PAST", App.get(psychotic_symptom_in_past).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
*/
     /*   if (children_number.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TOTAL NUMBER OF CHILDREN", App.get(children_number)});

        if (counselling.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FAMILY MEMBERS COUNSELLED", (App.get(counselling).equals(getResources().getString(R.string.common_counselling_self))) ? "SELF" :
                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_parent)) ? "PARENT" :
                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_guardian)) ? "GUARDIAN" :
                                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_mother)) ? "MOTHER" :
                                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_father)) ? "FATHER" :
                                                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_maternal_grand_mother)) ? "MATERNAL GRANDMOTHER" :
                                                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_maternal_grand_father)) ? "MATERNAL GRANDFATHER" :
                                                                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_paternal_grand_mother)) ? "PATERNAL GRANDMOTHER" :
                                                                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_maternal_grand_father)) ? "PATERNAL GRANDFATHER" :
                                                                                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_brother)) ? "BROTHER" :
                                                                                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_sister)) ? "SISTER" :
                                                                                                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_son)) ? "SON" :
                                                                                                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_daughter)) ? "DAUGHTER" :
                                                                                                                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_spouse)) ? "SPOUSE" :
                                                                                                                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_aunt)) ? "AUNT" :
                                                                                                                                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_uncle)) ? "UNCLE" :
                                                                                                                                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_neighbor)) ? "NEIGHBOR" :
                                                                                                                                                    (App.get(counselling).equals(getResources().getString(R.string.common_counselling_friend)) ? "FRIEND" :
                                                                                                                                                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_cousin)) ? "COUSIN" : "IN-LAWS"))))))))))))))))))});

*/

        /*commented before the refactoring*/
     /*  if (tb_infection_type.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOSIS INFECTION TYPE", App.get(tb_infection_type).equals(getResources().getString(R.string.common_tb_infection_type_drtb)) ? "DRUG-RESISTANT TB" : "DRUG-SENSITIVE TUBERCULOSIS INFECTION"});

       if (tb_type.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SITE OF TUBERCULOSIS DISEASE", App.get(tb_type).equals(getResources().getString(R.string.common_tb_type_ptb)) ? "PULMONARY TUBERCULOSIS" : "EXTRA-PULMONARY TUBERCULOSIS"});

        if (extra_pulmonary_site.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"EXTRA PULMONARY SITE", App.get(extra_pulmonary_site)});

        if (drug_resistance_profile.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DRUG RESISTANCE PROFILE", App.get(drug_resistance_profile)});

        if (drug_resistant_profile_class.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SUB-CLASSIFICATION FOR DRUG RESISTANT CASES", App.get(drug_resistant_profile_class)});
        if (report_comorbidity.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COMORBIDITIES REPORTED", App.get(report_comorbidity).equals(getResources().getString(R.string.yes)) ? "YES" :
                    App.get(report_comorbidity).equals(getResources().getString(R.string.no)) ? "NO" : "UNKNOWN"});
*/
/*        if (medical_condition.getVisibility() == View.VISIBLE) {
            String referredToString = "";
            for (CheckBox cb : medical_condition.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_medical_condition_diabetes)))
                    referredToString = referredToString + "DIABETES MELLITUS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_medical_condition_hyper)))
                    referredToString = referredToString + "HYPERTENSION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_medical_condition_epilepsy)))
                    referredToString = referredToString + "EPILEPSY" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_medical_condition_intellectual)))
                    referredToString = referredToString + "INTELLECTUAL FUNCTIONING DISABILITY" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_medical_condition_physically)))
                    referredToString = referredToString + "PHYSICALLY DISABLE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_medical_condition_hiv)))
                    referredToString = referredToString + "HUMAN IMMUNODEFICIENCY VIRUS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_medical_condition_hcv)))
                    referredToString = referredToString + "HEPATITIS C VIRUS INFECTION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_medical_condition_others)))
                    referredToString = referredToString + "OTHER DISEASE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_medical_condition_none)))
                    referredToString = referredToString + "NONE" + " ; ";
            }
            observations.add(new String[]{"GENERAL MEDICAL CONDITION", referredToString});
        }*/


      /*  if (other_disease.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER DISEASE", App.get(other_disease)});

        if (drug_abuse_history.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HISTORY OF DRUG ABUSE", App.get(drug_abuse_history).equals(getResources().getString(R.string.yes)) ? "YES" :
                    App.get(drug_abuse_history).equals(getResources().getString(R.string.no)) ? "NO" :
                            App.get(drug_abuse_history).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "PAST"});
*/
/*        if (substance_abuse.getVisibility() == View.VISIBLE) {
            String referredToString = "";
            for (CheckBox cb : substance_abuse.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_substance_abuset_smoking)))
                    referredToString = referredToString + "SMOKING" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_substance_abuset_alcohol)))
                    referredToString = referredToString + "ALCOHOL ABUSE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_substance_abuset_drugs)))
                    referredToString = referredToString + "DRUGS/SUBSTANCES" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_substance_abuset_all)))
                    referredToString = referredToString + "ALL" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_substance_abuset_others)))
                    referredToString = referredToString + "OTHER DRUG / SUBSTANCE TYPE" + " ; ";
            }
            observations.add(new String[]{"SUBSTANCE ABUSE", referredToString});
        }*/

/*        if (drug_substance_type_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER DRUG / SUBSTANCE TYPE", App.get(other_disease)});

        if (past_drug_abuse_age.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"INITIAL AGE OF SUBSTANCE ABUSE", App.get(past_drug_abuse_age)});

        if (akuads_score.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"AKUADS SCORE", App.get(akuads_score)});*/

       /* if (hallucination.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HALLUCINATION", App.get(hallucination).equals(getResources().getString(R.string.yes)) ? "YES" :
                    App.get(hallucination).equals(getResources().getString(R.string.no)) ? "NO" : "UNKNOWN"});

        if (hallucination_type.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HALLUCINATION TYPE", App.get(hallucination_type)});

        if (delusion.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DELUSION", App.get(delusion).equals(getResources().getString(R.string.yes)) ? "YES" :
                    App.get(delusion).equals(getResources().getString(R.string.no)) ? "NO" : "UNKNOWN"});

        if (delusion_type.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DELUSION TYPE", App.get(delusion_type)});
*/

      /*  if (counseling_provided_for.getVisibility() == View.VISIBLE) {
            String referredToString = "";
            for (CheckBox cb : counseling_provided_for.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counseling_provided_for_type)))
                    referredToString = referredToString + "TYPES OF TB" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counseling_provided_for_important_nutrition)))
                    referredToString = referredToString + "IMPORTANCE OF NUTRITION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counseling_provided_for_duration)))
                    referredToString = referredToString + "DURATION OF TB TREATMENT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counseling_provided_for_treatment)))
                    referredToString = referredToString + "TREATMENT PROCEDURE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counseling_provided_for_preventive)))
                    referredToString = referredToString + "PREVENTIVE TREATMENT (IPT)" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counseling_provided_for_adverse)))
                    referredToString = referredToString + "ADVERSE EVENTS AND THEIR MANAGEMENT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counseling_provided_for_infection)))
                    referredToString = referredToString + "INFECTION CONTROL" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counseling_provided_for_transmission)))
                    referredToString = referredToString + "TRANSMISSION OF TB" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counseling_provided_for_important_of_treatment)))
                    referredToString = referredToString + "IMPORTANCE OF TREATMENT ADHERENCE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counseling_provided_for_purpose)))
                    referredToString = referredToString + "IMPORTANCE OF CONTACT SCREENING" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counseling_provided_for_important_of_regular)))
                    referredToString = referredToString + "IMPORTANCE OF REGULAR MONTHLY FOLLOW UP" + " ; ";
                    else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counseling_provided_for_tb_helpline)))
                    referredToString = referredToString + "TB HELPLINE NUMBER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counseling_provided_for_tb_sysmptoms)))
                    referredToString = referredToString + "TB SYMPTOMS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counseling_provided_for_tb_pregnancy)))
                    referredToString = referredToString + "PREGNANCY" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counseling_provided_for_tb_importacne)))
                    referredToString = referredToString + "IMPORTANCE OF SMOKING CESSATION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counseling_provided_for_tb_role)))
                    referredToString = referredToString + "ROLE OF TREATMENT COORDINATOR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counseling_provided_for_tb_breasfeed)))
                    referredToString = referredToString + "BREASTFEEDING" + " ; ";
            }
            observations.add(new String[]{"COUNSELING PROVIDED FOR", referredToString});
        }
*/
/*        if (patient_behaviour.getVisibility() == View.VISIBLE) {
            String referredToString = "";
            for (CheckBox cb : patient_behaviour.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_normal)))
                    referredToString = referredToString + "NORMAL" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_irritable)))
                    referredToString = referredToString + "IRRITABILITY" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_stubborn)))
                    referredToString = referredToString + "STUBBORN BEHAVIOUR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_shy)))
                    referredToString = referredToString + "INTROVERTED PERSONALITY" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_aggressive)))
                    referredToString = referredToString + "AGGRESSIVE BEHAVIOUR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_argument)))
                    referredToString = referredToString + "ARGUMENTATIVE BEHAVIOUR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_non_compliant)))
                    referredToString = referredToString + "NON COMPLIANT BEHAVIOUR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_compliant)))
                    referredToString = referredToString + "COMPLIANT BEHAVIOUR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_cooperative)))
                    referredToString = referredToString + "COOPERATIVE BEHAVIOUR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_non_cooprerative)))
                    referredToString = referredToString + "NON-COOPERATIVE BEHAVIOUR" + " ; ";
            }
            observations.add(new String[]{"BEHAVIOUR", referredToString});
        }*/

      /*  if (counsel_next_followup.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELING REQUIRED ON NEXT FOLLOW UP", App.get(counsel_next_followup).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});


        if (counselor_comments.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELOR COMMENTS", App.get(counselor_comments)});
*/
   /*     observations.add(new String[]{"PATIENT REFERRED", App.get(patientReferred).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if (referredTo.getVisibility() == View.VISIBLE) {

            String referredToString = "";
            for (CheckBox cb : referredTo.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.counselor)))
                    referredToString = referredToString + "COUNSELOR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.psychologist)))
                    referredToString = referredToString + "PSYCHOLOGIST" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.clinician)))
                    referredToString = referredToString + "CLINICAL OFFICER/DOCTOR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.call_center)))
                    referredToString = referredToString + "CALL CENTER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.field_supervisor)))
                    referredToString = referredToString + "FIELD SUPERVISOR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.site_supervisor)))
                    referredToString = referredToString + "SITE SUPERVISOR" + " ; ";
            }
            observations.add(new String[]{"PATIENT REFERRED TO", referredToString});

        }*/
      /*  if (referalReasonPsychologist.getVisibility() == View.VISIBLE) {

            String string = "";
            for (CheckBox cb : referalReasonPsychologist.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.check_treatment_adherence)))
                    string = string + "CHECK FOR TREATMENT ADHERENCE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.psychological_issue)))
                    string = string + "PSYCHOLOGICAL EVALUATION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.behavioral_issue)))
                    string = string + "BEHAVIORAL ISSUES" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.refusal)))
                    string = string + "REFUSAL OF TREATMENT BY PATIENT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.other)))
                    string = string + "OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR" + " ; ";
            }
            observations.add(new String[]{"REASON FOR PSYCHOLOGIST/COUNSELOR REFERRAL", string});

        }
        if (otherReferalReasonPsychologist.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR", App.get(otherReferalReasonPsychologist)});

        if (referalReasonSupervisor.getVisibility() == View.VISIBLE) {

            String string = "";
            for (CheckBox cb : referalReasonSupervisor.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.contact_screening_reminder)))
                    string = string + "CONTACT SCREENING REMINDER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.treatment_followup_reminder)))
                    string = string + "TREATMENT FOLLOWUP REMINDER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.check_treatment_adherence)))
                    string = string + "CHECK FOR TREATMENT ADHERENCE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.investigation_report_collection)))
                    string = string + "INVESTIGATION OF REPORT COLLECTION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.adverse_events)))
                    string = string + "ADVERSE EVENTS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.medicine_collection)))
                    string = string + "MEDICINE COLLECTION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.other)))
                    string = string + "OTHER REFERRAL REASON TO SUPERVISOR" + " ; ";
            }
            observations.add(new String[]{"REASON FOR SUPERVISOR REFERRAL", string});

        }*/
/*
        if (otherReferalReasonSupervisor.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REFERRAL REASON TO SUPERVISOR", App.get(otherReferalReasonSupervisor)});

        if (referalReasonCallCenter.getVisibility() == View.VISIBLE) {

            String string = "";
            for (CheckBox cb : referalReasonCallCenter.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.contact_screening_reminder)))
                    string = string + "CONTACT SCREENING REMINDER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.treatment_followup_reminder)))
                    string = string + "TREATMENT FOLLOWUP REMINDER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.check_treatment_adherence)))
                    string = string + "CHECK FOR TREATMENT ADHERENCE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.investigation_report_collection)))
                    string = string + "INVESTIGATION OF REPORT COLLECTION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.adverse_events)))
                    string = string + "ADVERSE EVENTS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.medicine_collection)))
                    string = string + "MEDICINE COLLECTION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.other)))
                    string = string + "OTHER REFERRAL REASON TO CALL CENTER" + " ; ";
            }
            observations.add(new String[]{"REASON FOR CALL CENTER REFERRAL", string});

        }
        if (otherReferalReasonCallCenter.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REFERRAL REASON TO CALL CENTER", App.get(otherReferalReasonCallCenter)});
*/
/*
        if (referalReasonClinician.getVisibility() == View.VISIBLE) {

            String string = "";
            for (CheckBox cb : referalReasonClinician.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.expert_opinion)))
                    string = string + "EXPERT OPINION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.adverse_events)))
                    string = string + "ADVERSE EVENTS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.other)))
                    string = string + "OTHER REFERRAL REASON TO CLINICIAN" + " ; ";
            }
            observations.add(new String[]{"REASON FOR CLINICIAN REFERRAL", string});

        }

        if (otherReferalReasonClinician.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REFERRAL REASON TO CLINICIAN", App.get(otherReferalReasonClinician)});
     */

        if (returnVisitDate.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FAMILY VISIT DATE", App.getSqlDate(secondDateCalendar)});


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
                    id = serverService.saveFormLocallyTesting(formName, form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = "";


                result = serverService.saveEncounterAndObservationTesting(formName, form, formDateCalendar, observations.toArray(new String[][]{}), id);
                if (!result.contains("SUCCESS"))
                    return result;

                return "SUCCESS";
            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

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

        HashMap<String, String> formValues = new HashMap<String, String>();

      /*  formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        formValues.put(lastName.getTag(), App.get(lastName));
        formValues.put(husbandName.getTag(), App.get(husbandName));
        formValues.put(gender.getTag(), App.get(gender));

        serverService.saveFormLocally(formName, "12345-5", formValues);*/

        return true;
    }

    @Override
    public void refill(int formId) {
        super.refill(formId);
        OfflineForm fo = serverService.getSavedFormById(formId);
        ArrayList<String[][]> obsValue = fo.getObsValue();

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("FAMILY VISIT DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }

        }
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {

            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar, false, true, false);

            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");*/
        } else if (view == returnVisitDate.getButton()) {
            returnVisitDate.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar, true, false, true);

            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", true);*/
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
    public void onCheckedChanged(CompoundButton group, boolean isChecked) {
        if (medical_condition.getVisibility() == View.VISIBLE)
            for (CheckBox cb : medical_condition.getCheckedBoxes()) {
                if (cb.getText().equals(getResources().getString(R.string.common_medical_condition_others)) && cb.isChecked()) {
                    other_disease.setVisibility(View.VISIBLE);
                    break;
                } else {
                    other_disease.setVisibility(View.GONE);
                }
            }
        if (substance_abuse.getVisibility() == View.VISIBLE)
            for (CheckBox cb : substance_abuse.getCheckedBoxes()) {
                if (cb.getText().equals(getResources().getString(R.string.common_substance_abuset_others)) && cb.isChecked()) {
                    drug_substance_type_other.setVisibility(View.VISIBLE);
                    break;
                } else {
                    drug_substance_type_other.setVisibility(View.GONE);
                }
            }
        if (App.get(patientReferred).equals(getResources().getString(R.string.yes))) {
            for (CheckBox cb : referredTo.getCheckedBoxes()) {
                //referredTo.getQuestionView().setError(null);
                setReferralViews();
            }
            for (CheckBox cb : referalReasonPsychologist.getCheckedBoxes()) {
                //referalReasonPsychologist.getQuestionView().setError(null);
                setReferralViews();
            }
            for (CheckBox cb : referalReasonSupervisor.getCheckedBoxes()) {
                if (referalReasonCallCenter.getQuestionView().getError() != null)
                    //referalReasonSupervisor.getQuestionView().setError(null);
                    setReferralViews();
            }
            for (CheckBox cb : referalReasonCallCenter.getCheckedBoxes()) {
                //referalReasonCallCenter.getQuestionView().setError(null);
                setReferralViews();
            }
            for (CheckBox cb : referalReasonClinician.getCheckedBoxes()) {
//            referalReasonClinician.getQuestionView().setError(null);
                setReferralViews();
            }
        }
    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        extra_pulmonary_site.getEditText().setEnabled(false);
        drug_resistance_profile.getEditText().setEnabled(false);
        income_class.setRadioGroupEnabled(false);
        tb_type.setRadioGroupEnabled(false);
        tb_infection_type.setRadioGroupEnabled(false);
        diagnosis_type.getEditText().setEnabled(false);
        hallucination_type.setVisibility(View.GONE);
        delusion_type.setVisibility(View.GONE);
        other_disease.setVisibility(View.GONE);
        children_number.setVisibility(View.GONE);

        referredTo.setVisibility(View.GONE);
        referalReasonPsychologist.setVisibility(View.GONE);
        otherReferalReasonPsychologist.setVisibility(View.GONE);
        referalReasonSupervisor.setVisibility(View.GONE);
        otherReferalReasonSupervisor.setVisibility(View.GONE);
        referalReasonCallCenter.setVisibility(View.GONE);
        otherReferalReasonCallCenter.setVisibility(View.GONE);
        referalReasonClinician.setVisibility(View.GONE);
        otherReferalReasonClinician.setVisibility(View.GONE);
        if (App.getPatient().getPerson().getAge() < 14) {
            marital_status.setVisibility(View.GONE);
        } else {
            marital_status.setVisibility(View.VISIBLE);
        }
        Boolean flag = true;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);

                refill(formId);
                flag = false;

            } else bundle.putBoolean("save", false);

        }
        //if (flag) {
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

                String s_tb_infection_type = serverService.getLatestObsValue(App.getPatientId(), "TUBERCULOSIS INFECTION TYPE");
                String s_tb_type = serverService.getLatestObsValue(App.getPatientId(), "SITE OF TUBERCULOSIS DISEASE");
                String s_diagnosis_type = serverService.getLatestObsValue(App.getPatientId(), "TUBERCULOSIS DIAGNOSIS METHOD");
                String s_extra_pulmonary_site = serverService.getLatestObsValue(App.getPatientId(), "EXTRA PULMONARY SITE");
                String s_drug_resistance_profile = serverService.getLatestObsValue(App.getPatientId(), "DRUG RESISTANCE PROFILE");
                String s_drug_resistance_profile_class = serverService.getLatestObsValue(App.getPatientId(), "SUB-CLASSIFICATION FOR DRUG RESISTANT CASES");
                String s_akuads_score = serverService.getLatestObsValue(App.getPatientId(), "AKUADS SCORE");
                result.put("s_tb_infection_type", s_tb_infection_type);
                result.put("s_tb_type", s_tb_type);
                result.put("s_diagnosis_type", s_diagnosis_type);
                result.put("s_extra_pulmonary_site", s_extra_pulmonary_site);
                result.put("s_drug_resistance_profile", s_drug_resistance_profile);
                result.put("s_drug_resistance_profile_class", s_drug_resistance_profile_class);
                result.put("s_akuads_score", s_akuads_score);
                return result;
            }

            @Override
            protected void onProgressUpdate(String... values) {
            }


            @Override
            protected void onPostExecute(HashMap<String, String> result) {
                super.onPostExecute(result);
                loading.dismiss();
                if (result.get("s_tb_infection_type") != null)
                    if (result.get("s_tb_infection_type").equalsIgnoreCase("DRUG-SENSITIVE TUBERCULOSIS INFECTION")) {
                        tb_infection_type.getRadioGroup().getButtons().get(0).setChecked(true);
                    } else if (result.get("s_tb_infection_type").equalsIgnoreCase("DRUG-RESISTANT TB")) {
                        tb_infection_type.getRadioGroup().getButtons().get(1).setChecked(true);
                    }
                if (result.get("s_tb_type") != null)
                    if (result.get("s_tb_type").equalsIgnoreCase("PULMONARY TUBERCULOSIS")) {
                        tb_type.getRadioGroup().getButtons().get(0).setChecked(true);
                    } else if (result.get("s_tb_type").equalsIgnoreCase("EXTRA-PULMONARY TUBERCULOSIS")) {
                        tb_type.getRadioGroup().getButtons().get(1).setChecked(true);
                    }

                if (result.get("s_diagnosis_type") != null)
                    diagnosis_type.getEditText().setText(result.get("s_diagnosis_type").toString());
                if (result.get("s_extra_pulmonary_site") != null)
                    extra_pulmonary_site.getEditText().setText(result.get("s_extra_pulmonary_site").toString());
                if (result.get("s_drug_resistance_profile") != null)
                    drug_resistance_profile.getEditText().setText(result.get("s_drug_resistance_profile").toString());
                if (result.get("s_akuads_score") != null) {
                    akuads_score.getEditText().setText(result.get("s_akuads_score").toString());
                    akuads_score.getEditText().setEnabled(false);
                } else {
                    akuads_score.getEditText().setEnabled(true);
                }

            }
        };
        autopopulateFormTask.execute("");
        //}
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
    public void onCheckedChanged(RadioGroup radioGroup, int i) {


        if (radioGroup == marital_status.getRadioGroup()) {
            children.setVisibility(View.GONE);
            children_number.setVisibility(View.GONE);
            if (marital_status.getRadioGroup().getSelectedValue().equals(getString(R.string.common_marital_status_married)) ||
                    marital_status.getRadioGroup().getSelectedValue().equals(getString(R.string.common_marital_status_separated)) ||
                    marital_status.getRadioGroup().getSelectedValue().equals(getString(R.string.common_marital_status_divorced)) ||
                    marital_status.getRadioGroup().getSelectedValue().equals(getString(R.string.common_marital_status_widow))) {
                children.getRadioGroup().clearCheck();
                children.setVisibility(View.VISIBLE);
            }
        }
        if (radioGroup == children.getRadioGroup()) {
            children_number.setVisibility(View.GONE);
            if (children.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                children_number.setVisibility(View.VISIBLE);
            }
        }

        if (radioGroup == tb_type.getRadioGroup()) {
            extra_pulmonary_site.setVisibility(View.GONE);
            if (tb_type.getRadioGroup().getSelectedValue().equals(getString(R.string.common_tb_type_eptb))) {
                extra_pulmonary_site.setVisibility(View.VISIBLE);
            }
        }

        if (radioGroup == drug_abuse_history.getRadioGroup()) {
            substance_abuse.setVisibility(View.GONE);
            drug_substance_type_other.setVisibility(View.GONE);
            past_drug_abuse_age.setVisibility(View.GONE);
            if (drug_abuse_history.getRadioGroup().getSelectedValue().equals(getString(R.string.yes)) || drug_abuse_history.getRadioGroup().getSelectedValue().equals(getString(R.string.common_drug_abuse_history_past))) {
                for (CheckBox cb : substance_abuse.getCheckedBoxes()) {
                    cb.setChecked(false);
                    substance_abuse.setVisibility(View.VISIBLE);
                    past_drug_abuse_age.setVisibility(View.VISIBLE);

                }
            }

        }
        if (radioGroup == psychotic_symptom_in_past.getRadioGroup()) {
            hallucination.setVisibility(View.GONE);
            delusion.setVisibility(View.GONE);
            hallucination_type.setVisibility(View.GONE);
            delusion_type.setVisibility(View.GONE);
            if (psychotic_symptom_in_past.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                hallucination.setVisibility(View.VISIBLE);
                delusion.setVisibility(View.VISIBLE);
                if (hallucination.getRadioGroup().getSelectedValue().equals(getString(R.string.yes)))
                    hallucination_type.setVisibility(View.VISIBLE);
                if (delusion.getRadioGroup().getSelectedValue().equals(getString(R.string.yes)))
                    delusion_type.setVisibility(View.VISIBLE);
            }
        }
        if (radioGroup == hallucination.getRadioGroup()) {
            hallucination_type.setVisibility(View.GONE);
            if (hallucination.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                hallucination_type.setVisibility(View.VISIBLE);
            }
        }
        if (radioGroup == delusion.getRadioGroup()) {
            delusion_type.setVisibility(View.GONE);
            if (delusion.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                delusion_type.setVisibility(View.VISIBLE);
            }
        }


        if (radioGroup == counsel_next_followup.getRadioGroup()) {

        }

        if (radioGroup == patientReferred.getRadioGroup()) {
            patientReferred.getQuestionView().setError(null);
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
