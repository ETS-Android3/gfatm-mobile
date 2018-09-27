package com.ihsinformatics.gfatmmobile.common;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
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
import android.view.inputmethod.EditorInfo;
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
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 2/21/2017.
 */

public class BaselineCounselingForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledRadioGroup family_structure;
    TitledEditText earning_members;
    TitledEditText monthly_household_income;
    TitledRadioGroup income_class;
    TitledRadioGroup residence_type;
    TitledEditText residence_type_other;
    TitledEditText number_rooms_house;
    TitledRadioGroup education_level;
    TitledEditText other_education;
    TitledRadioGroup marital_status;
    TitledRadioGroup children;
    TitledEditText children_number;
    TitledRadioGroup counselling;
    TitledEditText other_family_member;
    TextView heading_disease_info;
    TitledRadioGroup tb_infection_type;
    TitledRadioGroup tb_type;
    TitledEditText extra_pulmonary_site;
    TitledEditText diagnosis_type;
    TitledEditText drug_resistance_profile;
    TitledEditText drug_resistant_profile_class;
    TitledRadioGroup report_comorbidity;
    TitledCheckBoxes medical_condition;
    TitledEditText other_disease;
    TextView heading_contact_info;
    TitledRadioGroup drug_abuse_history;
    TitledCheckBoxes substance_abuse;
    TitledEditText drug_substance_type_other;
    TitledEditText past_drug_abuse_age;
    TitledEditText akuads_score;
    TextView heading_psychotic_features_screening;
    TitledRadioGroup hallucination;
    TitledEditText hallucination_type;
    TitledRadioGroup delusion;
    TitledEditText delusion_type;
    TextView heading_patient_awareness_about_tb;
    TitledRadioGroup know_symptom_tb;
    TitledCheckBoxes knowledge_type_symptom_tb;
    TitledRadioGroup know_transmission_tb;
    TitledCheckBoxes knowledge_type_transmission_tb;
    TitledCheckBoxes counseling_provided_for;
    TitledCheckBoxes patient_behaviour;
    TitledRadioGroup counsel_next_followup;
    TitledRadioGroup person_counsel_next_followup;
    TitledEditText counseling_relationship_other;
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

        family_structure = new TitledRadioGroup(context, null, getResources().getString(R.string.common_family_structure), getResources().getStringArray(R.array.common_family_structure_options), getResources().getString(R.string.common_family_structure_joint), App.VERTICAL, App.VERTICAL, true);
        earning_members = new TitledEditText(context, null, getResources().getString(R.string.common_earning_members), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);
        monthly_household_income = new TitledEditText(context, null, getResources().getString(R.string.common_monthly_household_income), "", "", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);
        income_class = new TitledRadioGroup(context, null, getResources().getString(R.string.common_income_class), getResources().getStringArray(R.array.common_income_class_options), null, App.VERTICAL, App.VERTICAL, true);
        residence_type = new TitledRadioGroup(context, null, getResources().getString(R.string.common_residence_type), getResources().getStringArray(R.array.common_residence_type_options), getString(R.string.common_residence_type_rent), App.VERTICAL, App.VERTICAL, true);
        residence_type_other = new TitledEditText(context, null, getResources().getString(R.string.common_residence_type_specify_other), "", "", 25, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        number_rooms_house = new TitledEditText(context, null, getResources().getString(R.string.common_number_rooms_house), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);
        education_level = new TitledRadioGroup(context, null, getResources().getString(R.string.common_education_level), getResources().getStringArray(R.array.common_education_level_options), getString(R.string.common_education_level_secondary), App.VERTICAL, App.VERTICAL, true);
        other_education = new TitledEditText(context, null, getResources().getString(R.string.common_other_education), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        marital_status = new TitledRadioGroup(context, null, getResources().getString(R.string.common_marital_status), getResources().getStringArray(R.array.common_marital_status_options), null, App.VERTICAL, App.VERTICAL, true);
        children = new TitledRadioGroup(context, null, getResources().getString(R.string.common_children), getResources().getStringArray(R.array.common_children_options), getString(R.string.yes), App.VERTICAL, App.VERTICAL, true);
        children_number = new TitledEditText(context, null, getResources().getString(R.string.common_children_number), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        counselling = new TitledRadioGroup(context, null, getResources().getString(R.string.common_counselling), getResources().getStringArray(R.array.common_counselling_options), getResources().getString(R.string.common_counselling_self), App.VERTICAL, App.VERTICAL, true);
        other_family_member = new TitledEditText(context, null, getResources().getString(R.string.common_counselling_specify_other), "", "", 25, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        tb_infection_type = new TitledRadioGroup(context, null, getResources().getString(R.string.common_tb_infection_type), getResources().getStringArray(R.array.common_tb_infection_type_options), null, App.VERTICAL, App.VERTICAL, false);
        tb_type = new TitledRadioGroup(context, null, getResources().getString(R.string.common_tb_type), getResources().getStringArray(R.array.common_tb_type_options), null, App.VERTICAL, App.VERTICAL, false);
        extra_pulmonary_site = new TitledEditText(context, null, getResources().getString(R.string.common_extra_pulmonary_site), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        diagnosis_type = new TitledEditText(context, null, getResources().getString(R.string.common_diagnosis_type), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        drug_resistance_profile = new TitledEditText(context, null, getResources().getString(R.string.common_drug_resistance_profile), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        drug_resistant_profile_class = new TitledEditText(context, null, getResources().getString(R.string.common_drug_resistant_profile_class), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        report_comorbidity = new TitledRadioGroup(context, null, getResources().getString(R.string.common_report_comorbidity), getResources().getStringArray(R.array.common_report_comorbidity_options), getString(R.string.no), App.VERTICAL, App.VERTICAL, true);
        medical_condition = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_medical_condition), getResources().getStringArray(R.array.common_medical_condition_options), new Boolean[]{true}, App.VERTICAL, App.VERTICAL, true);
        other_disease = new TitledEditText(context, null, getResources().getString(R.string.common_medical_condition_specify_other), "", "", 25, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        drug_abuse_history = new TitledRadioGroup(context, null, getResources().getString(R.string.common_drug_abuse_history), getResources().getStringArray(R.array.common_drug_abuse_history_options), null, App.VERTICAL, App.VERTICAL, false);
        substance_abuse = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_substance_abuse), getResources().getStringArray(R.array.common_substance_abuset_options), null, App.VERTICAL, App.VERTICAL, false);
        drug_substance_type_other = new TitledEditText(context, null, getResources().getString(R.string.common_drug_substance_type_other), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        past_drug_abuse_age = new TitledEditText(context, null, getResources().getString(R.string.common_past_drug_abuse_age), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        akuads_score = new TitledEditText(context, null, getResources().getString(R.string.common_akuads_score), "", "", 2, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);
        hallucination = new TitledRadioGroup(context, null, getResources().getString(R.string.common_hallucination), getResources().getStringArray(R.array.common_hallucination_options), getString(R.string.yes), App.VERTICAL, App.VERTICAL, true);
        hallucination_type = new TitledEditText(context, null, getResources().getString(R.string.common_hallucination_type), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        delusion = new TitledRadioGroup(context, null, getResources().getString(R.string.common_delusion), getResources().getStringArray(R.array.common_delusion_options), getString(R.string.yes), App.VERTICAL, App.VERTICAL, true);
        delusion_type = new TitledEditText(context, null, getResources().getString(R.string.common_delusion_type), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        know_symptom_tb = new TitledRadioGroup(context, null, getResources().getString(R.string.common_know_symptom_tb), getResources().getStringArray(R.array.common_know_symptom_tb_options), getString(R.string.yes), App.VERTICAL, App.VERTICAL, true);
        knowledge_type_symptom_tb = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_knowledge_type_symptom_tb), getResources().getStringArray(R.array.common_knowledge_type_symptom_tb_options), new Boolean[]{true}, App.VERTICAL, App.VERTICAL, true);
        know_transmission_tb = new TitledRadioGroup(context, null, getResources().getString(R.string.common_know_transmission_tb), getResources().getStringArray(R.array.common_know_transmission_tb_options), getString(R.string.yes), App.VERTICAL, App.VERTICAL, true);
        knowledge_type_transmission_tb = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_knowledge_type_transmission_tb), getResources().getStringArray(R.array.common_knowledge_type_transmission_tb_options), null, App.VERTICAL, App.VERTICAL, true);
        counseling_provided_for = new TitledCheckBoxes(context, null, getResources().getString(R.string.counseling_provided_for), getResources().getStringArray(R.array.counseling_provided_for_options), new Boolean[]{true}, App.VERTICAL, App.VERTICAL, true);
        patient_behaviour = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_patient_behaviour), getResources().getStringArray(R.array.common_patient_behaviour_options), new Boolean[]{true}, App.VERTICAL, App.VERTICAL, true);
        counsel_next_followup = new TitledRadioGroup(context, null, getResources().getString(R.string.common_counsel_next_followup), getResources().getStringArray(R.array.common_counsel_next_followup_options), getString(R.string.yes), App.VERTICAL, App.VERTICAL, true);
        person_counsel_next_followup = new TitledRadioGroup(context, null, getResources().getString(R.string.common_person_counsel_next_followup), getResources().getStringArray(R.array.common_counselling_options), getString(R.string.common_counselling_parent), App.VERTICAL, App.VERTICAL, true);
        counseling_relationship_other = new TitledEditText(context, null, getResources().getString(R.string.common_counseling_relationship_other), "", "", 25, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        counselor_comments = new TitledEditText(context, null, getResources().getString(R.string.common_counselor_comments), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
/////////////////
        patientReferred = new TitledRadioGroup(context, null, getResources().getString(R.string.refer_patient), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL, true);
        referredTo = new TitledCheckBoxes(context, null, getResources().getString(R.string.refer_patient_to), getResources().getStringArray(R.array.refer_patient_to_option), null, App.VERTICAL, App.VERTICAL, true);
        referalReasonPsychologist = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_psychologist), getResources().getStringArray(R.array.referral_reason_for_psychologist_option), null, App.VERTICAL, App.VERTICAL, true);
        otherReferalReasonPsychologist = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        referalReasonSupervisor = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_supervisor), getResources().getStringArray(R.array.referral_reason_for_supervisor_option), null, App.VERTICAL, App.VERTICAL, true);
        otherReferalReasonSupervisor = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        referalReasonCallCenter = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_center), getResources().getStringArray(R.array.referral_reason_for_call_center_option), null, App.VERTICAL, App.VERTICAL, true);
        otherReferalReasonCallCenter = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        referalReasonClinician = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_clinician), getResources().getStringArray(R.array.referral_reason_for_clinician_option), null, App.VERTICAL, App.VERTICAL, true);
        otherReferalReasonClinician = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        heading_disease_info = new TextView(context);
        heading_contact_info = new TextView(context);
        heading_psychotic_features_screening = new TextView(context);
        heading_patient_awareness_about_tb = new TextView(context);

        heading_disease_info.setText("Disease Information");
        heading_contact_info.setText("Contact Screening Information");
        heading_psychotic_features_screening.setText("Psychotic features screening");
        heading_patient_awareness_about_tb.setText("Patient awareness about TB");

        heading_disease_info.setTypeface(null, Typeface.BOLD);
        heading_contact_info.setTypeface(null, Typeface.BOLD);
        heading_psychotic_features_screening.setTypeface(null, Typeface.BOLD);
        heading_patient_awareness_about_tb.setTypeface(null, Typeface.BOLD);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), family_structure.getRadioGroup(), earning_members.getEditText(), monthly_household_income.getEditText(), income_class.getRadioGroup(),
                residence_type.getRadioGroup(), residence_type_other.getEditText(), number_rooms_house.getEditText(), education_level.getRadioGroup(), other_education.getEditText(),
                marital_status.getRadioGroup(), children.getRadioGroup(), children_number.getEditText(), counselling.getRadioGroup(), other_family_member.getEditText(), heading_disease_info, tb_infection_type.getRadioGroup(),
                tb_type.getRadioGroup(), extra_pulmonary_site.getEditText(), diagnosis_type.getEditText(), drug_resistance_profile.getEditText(), drug_resistant_profile_class.getEditText(),
                report_comorbidity.getRadioGroup(), medical_condition, other_disease.getEditText(), heading_contact_info, drug_abuse_history.getRadioGroup(), substance_abuse, drug_substance_type_other.getEditText(),
                past_drug_abuse_age.getEditText(), akuads_score.getEditText(), heading_psychotic_features_screening, hallucination.getRadioGroup(), hallucination_type.getEditText(), delusion.getRadioGroup(), delusion_type.getEditText(), heading_patient_awareness_about_tb,
                know_symptom_tb.getRadioGroup(), knowledge_type_symptom_tb, know_transmission_tb.getRadioGroup(), knowledge_type_transmission_tb, counseling_provided_for, patient_behaviour,
                counsel_next_followup.getRadioGroup(), person_counsel_next_followup.getRadioGroup(), counseling_relationship_other.getEditText(), counselor_comments.getEditText(), patientReferred.getRadioGroup(), referredTo, referalReasonPsychologist, otherReferalReasonPsychologist.getEditText(), referalReasonSupervisor, otherReferalReasonSupervisor.getEditText(),
                referalReasonCallCenter, otherReferalReasonCallCenter.getEditText(), referalReasonClinician, otherReferalReasonClinician.getEditText()};

        // Array used to display views accordingly....
        viewGroups = new View[][]{{formDate, family_structure, earning_members, monthly_household_income, income_class, residence_type, residence_type_other, number_rooms_house, education_level,
                other_education, marital_status, children, children_number, counselling, other_family_member, heading_disease_info, tb_infection_type, tb_type, extra_pulmonary_site, diagnosis_type, drug_resistance_profile,
                drug_resistant_profile_class, report_comorbidity, medical_condition, other_disease, heading_contact_info, drug_abuse_history, substance_abuse, drug_substance_type_other, past_drug_abuse_age,
                akuads_score, heading_psychotic_features_screening, hallucination, hallucination_type, delusion, delusion_type, heading_patient_awareness_about_tb, know_symptom_tb, knowledge_type_symptom_tb, know_transmission_tb, knowledge_type_transmission_tb,
                counseling_provided_for, patient_behaviour, counsel_next_followup, person_counsel_next_followup, counseling_relationship_other, counselor_comments,
                patientReferred, referredTo, referalReasonPsychologist, otherReferalReasonPsychologist, referalReasonSupervisor, otherReferalReasonSupervisor,
                referalReasonCallCenter, otherReferalReasonCallCenter, referalReasonClinician, otherReferalReasonClinician},};

        formDate.getButton().setOnClickListener(this);
        residence_type.getRadioGroup().setOnCheckedChangeListener(this);
        education_level.getRadioGroup().setOnCheckedChangeListener(this);
        marital_status.getRadioGroup().setOnCheckedChangeListener(this);
        children.getRadioGroup().setOnCheckedChangeListener(this);
        counselling.getRadioGroup().setOnCheckedChangeListener(this);
        tb_type.getRadioGroup().setOnCheckedChangeListener(this);
        report_comorbidity.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : medical_condition.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        drug_abuse_history.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : substance_abuse.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        hallucination.getRadioGroup().setOnCheckedChangeListener(this);
        delusion.getRadioGroup().setOnCheckedChangeListener(this);
        know_symptom_tb.getRadioGroup().setOnCheckedChangeListener(this);
        know_transmission_tb.getRadioGroup().setOnCheckedChangeListener(this);
        counsel_next_followup.getRadioGroup().setOnCheckedChangeListener(this);
        person_counsel_next_followup.getRadioGroup().setOnCheckedChangeListener(this);
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
                    income = Integer.parseInt(monthly_household_income.getEditText().getText().toString());
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
        drug_resistant_profile_class.getEditText().setSingleLine(false);
        drug_resistant_profile_class.getEditText().setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
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
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());


        }


        formDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = false;
        View view = null;
        if (family_structure.getVisibility() == View.VISIBLE && App.get(family_structure).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            family_structure.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            family_structure.getQuestionView().setError(null);
        }
        if (income_class.getVisibility() == View.VISIBLE && App.get(income_class).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            income_class.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            income_class.getQuestionView().setError(null);
        }
        if (residence_type.getVisibility() == View.VISIBLE && App.get(residence_type).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            residence_type.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            residence_type.getQuestionView().setError(null);
        }
        if (residence_type_other.getVisibility() == View.VISIBLE && residence_type_other.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            residence_type_other.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }
        if (education_level.getVisibility() == View.VISIBLE && App.get(education_level).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            education_level.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            education_level.getQuestionView().setError(null);
        }
        if (other_education.getVisibility() == View.VISIBLE && other_education.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            other_education.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }
        if (marital_status.getVisibility() == View.VISIBLE && App.get(marital_status).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            marital_status.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            marital_status.getQuestionView().setError(null);
        }
        if (children.getVisibility() == View.VISIBLE && App.get(children).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            children.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            children.getQuestionView().setError(null);
        }
        if (children_number.getVisibility() == View.VISIBLE && children_number.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            children_number.getEditText().setError(getString(R.string.empty_field));
            error = true;
        } else if (children_number.getVisibility() == View.VISIBLE && Integer.parseInt(children_number.getEditText().getText().toString().trim()) > 11) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            children_number.getEditText().setError("Value should be less then 12");
            error = true;
        }
        if (counselling.getVisibility() == View.VISIBLE && App.get(counselling).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            counselling.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            counselling.getQuestionView().setError(null);
        }
        if (other_family_member.getVisibility() == View.VISIBLE && other_family_member.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            other_family_member.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }
       /* if (tb_infection_type.getVisibility() == View.VISIBLE && App.get(tb_infection_type).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            tb_infection_type.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            tb_infection_type.getQuestionView().setError(null);
        }*/
       /* if (tb_type.getVisibility() == View.VISIBLE && App.get(tb_type).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            tb_type.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            tb_type.getQuestionView().setError(null);
        }
        if (extra_pulmonary_site.getVisibility() == View.VISIBLE && extra_pulmonary_site.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            extra_pulmonary_site.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }
        if (diagnosis_type.getVisibility() == View.VISIBLE && App.get(diagnosis_type).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            diagnosis_type.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            diagnosis_type.getQuestionView().setError(null);
        }
        if (drug_resistance_profile.getVisibility() == View.VISIBLE && drug_resistance_profile.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            drug_resistance_profile.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }
        if (drug_resistant_profile_class.getVisibility() == View.VISIBLE && drug_resistant_profile_class.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            drug_resistant_profile_class.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }*/
        if (report_comorbidity.getVisibility() == View.VISIBLE && App.get(report_comorbidity).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            report_comorbidity.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            report_comorbidity.getQuestionView().setError(null);
        }
        if (medical_condition.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : medical_condition.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                medical_condition.getQuestionView().setError(getString(R.string.empty_field));
                medical_condition.getQuestionView().requestFocus();
                view = medical_condition;
                error = true;
            } else {
                medical_condition.getQuestionView().setError(null);
            }
        }
        if (other_disease.getVisibility() == View.VISIBLE && other_disease.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            other_disease.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }
       /* if (drug_abuse_history.getVisibility() == View.VISIBLE && App.get(drug_abuse_history).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            drug_abuse_history.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            drug_abuse_history.getQuestionView().setError(null);
        }
        if (substance_abuse.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : substance_abuse.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                substance_abuse.getQuestionView().setError(getString(R.string.empty_field));
                substance_abuse.getQuestionView().requestFocus();
                view = substance_abuse;
                error = true;
            } else {
                substance_abuse.getQuestionView().setError(null);
            }
        }*/
        if (drug_substance_type_other.getVisibility() == View.VISIBLE && drug_substance_type_other.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            drug_substance_type_other.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }
        if (past_drug_abuse_age.getVisibility() == View.VISIBLE && past_drug_abuse_age.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            past_drug_abuse_age.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }
        if (akuads_score.getVisibility() == View.VISIBLE && akuads_score.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            akuads_score.getEditText().setError(getString(R.string.empty_field));
            error = true;
        } else if (akuads_score.getVisibility() == View.VISIBLE && Integer.parseInt(akuads_score.getEditText().getText().toString().trim()) > 75) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            akuads_score.getEditText().setError("Value should be 0-75");
            error = true;
        }

        if (hallucination.getVisibility() == View.VISIBLE && App.get(hallucination).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            hallucination.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            hallucination.getQuestionView().setError(null);
        }
        if (hallucination_type.getVisibility() == View.VISIBLE && hallucination_type.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            hallucination_type.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }

        if (delusion.getVisibility() == View.VISIBLE && App.get(delusion).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            delusion.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            delusion.getQuestionView().setError(null);
        }
        if (delusion_type.getVisibility() == View.VISIBLE && delusion_type.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            delusion_type.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }
        if (know_symptom_tb.getVisibility() == View.VISIBLE && App.get(know_symptom_tb).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            know_symptom_tb.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            know_symptom_tb.getQuestionView().setError(null);
        }
        if (knowledge_type_symptom_tb.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : knowledge_type_symptom_tb.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                knowledge_type_symptom_tb.getQuestionView().setError(getString(R.string.empty_field));
                knowledge_type_symptom_tb.getQuestionView().requestFocus();
                view = knowledge_type_symptom_tb;
                error = true;
            } else {
                knowledge_type_symptom_tb.getQuestionView().setError(null);
            }
        }
        if (know_transmission_tb.getVisibility() == View.VISIBLE && App.get(know_transmission_tb).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            know_transmission_tb.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            know_transmission_tb.getQuestionView().setError(null);
        }
        if (knowledge_type_transmission_tb.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : knowledge_type_transmission_tb.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                knowledge_type_transmission_tb.getQuestionView().setError(getString(R.string.empty_field));
                knowledge_type_transmission_tb.getQuestionView().requestFocus();
                view = knowledge_type_transmission_tb;
                error = true;
            } else {
                knowledge_type_transmission_tb.getQuestionView().setError(null);
            }
        }
        if (counseling_provided_for.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : counseling_provided_for.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                counseling_provided_for.getQuestionView().setError(getString(R.string.empty_field));
                counseling_provided_for.getQuestionView().requestFocus();
                view = counseling_provided_for;
                error = true;
            } else {
                counseling_provided_for.getQuestionView().setError(null);
            }
        }
        if (patient_behaviour.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : patient_behaviour.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                patient_behaviour.getQuestionView().setError(getString(R.string.empty_field));
                patient_behaviour.getQuestionView().requestFocus();
                view = patient_behaviour;
                error = true;
            } else {
                patient_behaviour.getQuestionView().setError(null);
            }
        }
        if (counsel_next_followup.getVisibility() == View.VISIBLE && App.get(counsel_next_followup).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            counsel_next_followup.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            counsel_next_followup.getQuestionView().setError(null);
        }
        if (person_counsel_next_followup.getVisibility() == View.VISIBLE && App.get(person_counsel_next_followup).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            person_counsel_next_followup.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            person_counsel_next_followup.getQuestionView().setError(null);
        }
        if (counseling_relationship_other.getVisibility() == View.VISIBLE && counseling_relationship_other.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            counseling_relationship_other.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }
        if (App.get(patientReferred).isEmpty()) {
            gotoPage(4);
            patientReferred.getQuestionView().setError(getString(R.string.empty_field));
            patientReferred.requestFocus();
            error = true;
        }


        if (referredTo.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : referredTo.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (App.isLanguageRTL())
                    gotoPage(4);
                else
                    gotoPage(4);
                referredTo.getQuestionView().setError(getString(R.string.empty_field));
                referredTo.getQuestionView().requestFocus();
                error = true;
            } else {
                referredTo.getQuestionView().setError(null);
            }
        }


        if (referalReasonPsychologist.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : referalReasonPsychologist.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (App.isLanguageRTL())
                    gotoPage(4);
                else
                    gotoPage(4);
                referalReasonPsychologist.getQuestionView().setError(getString(R.string.empty_field));
                referalReasonPsychologist.getQuestionView().requestFocus();
                error = true;
            } else {
                referalReasonPsychologist.getQuestionView().setError(null);
            }
        }

        if (otherReferalReasonPsychologist.getVisibility() == View.VISIBLE && App.get(otherReferalReasonPsychologist).isEmpty()) {
            otherReferalReasonPsychologist.getEditText().setError(getString(R.string.empty_field));
            otherReferalReasonPsychologist.getEditText().requestFocus();
            gotoPage(4);
            error = true;
        }


        if (referalReasonClinician.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : referalReasonClinician.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (App.isLanguageRTL())
                    gotoPage(4);
                else
                    gotoPage(4);
                referalReasonClinician.getQuestionView().setError(getString(R.string.empty_field));
                referalReasonClinician.getQuestionView().requestFocus();
                error = true;
            } else {
                referalReasonClinician.getQuestionView().setError(null);
            }
        }

        if (otherReferalReasonClinician.getVisibility() == View.VISIBLE && App.get(otherReferalReasonClinician).isEmpty()) {
            otherReferalReasonClinician.getEditText().setError(getString(R.string.empty_field));
            otherReferalReasonClinician.getEditText().requestFocus();
            gotoPage(4);
            error = true;
        }

        if (referalReasonCallCenter.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : referalReasonCallCenter.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (App.isLanguageRTL())
                    gotoPage(4);
                else
                    gotoPage(4);
                referalReasonCallCenter.getQuestionView().setError(getString(R.string.empty_field));
                referalReasonCallCenter.getQuestionView().requestFocus();
                error = true;
            } else {
                referalReasonCallCenter.getQuestionView().setError(null);
            }
        }

        if (otherReferalReasonCallCenter.getVisibility() == View.VISIBLE && App.get(otherReferalReasonCallCenter).isEmpty()) {
            otherReferalReasonCallCenter.getEditText().setError(getString(R.string.empty_field));
            otherReferalReasonCallCenter.getEditText().requestFocus();
            gotoPage(4);
            error = true;
        }

        if (referalReasonSupervisor.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : referalReasonSupervisor.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (App.isLanguageRTL())
                    gotoPage(4);
                else
                    gotoPage(4);
                referalReasonSupervisor.getQuestionView().setError(getString(R.string.empty_field));
                referalReasonSupervisor.getQuestionView().requestFocus();
                error = true;
            } else {
                referalReasonSupervisor.getQuestionView().setError(null);
            }
        }

        if (otherReferalReasonSupervisor.getVisibility() == View.VISIBLE && App.get(otherReferalReasonSupervisor).isEmpty()) {
            otherReferalReasonSupervisor.getEditText().setError(getString(R.string.empty_field));
            otherReferalReasonSupervisor.getEditText().requestFocus();
            gotoPage(4);
            error = true;
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
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

        final ArrayList<String[]> observations = new ArrayList<String[]>();

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

        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});

        if (family_structure.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FAMILY STRUCTURE", App.get(family_structure).equals(getResources().getString(R.string.common_family_structure_nuclear)) ? "NUCLEAR FAMILY" : "JOINT FAMILY"});

        if (earning_members.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"EARNING FAMILY MEMBERS", earning_members.getEditText().getText().toString()});

        if (monthly_household_income.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TOTAL MONTHLY HOUSEHOLD INCOME", App.get(monthly_household_income)});

        if (income_class.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"INCOME CLASS", App.get(income_class).equals(getResources().getString(R.string.common_income_class_none)) ? "NONE" :
                    App.get(income_class).equals(getResources().getString(R.string.common_income_class_lower)) ? "LOWER INCOME CLASS" :
                            App.get(income_class).equals(getResources().getString(R.string.common_income_class_lower_middle)) ? "LOWER MIDDLE INCOME CLASS" :
                                    App.get(income_class).equals(getResources().getString(R.string.common_income_class_middle)) ? "MIDDLE INCOME CLASS" :
                                            App.get(income_class).equals(getResources().getString(R.string.common_income_class_upper_middle)) ? "UPPER MIDDLE INCOME CLASS" : "UPPER INCOME CLASS"});

        if (residence_type.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HOUSE TYPE", App.get(residence_type).equals(getResources().getString(R.string.common_residence_type_own)) ? "OWNED" :
                    App.get(residence_type).equals(getResources().getString(R.string.common_residence_type_rent)) ? "RENTED" : "OTHER RESIDENCE TYPE"});

        if (residence_type_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER RESIDENCE TYPE", App.get(residence_type_other)});

        if (number_rooms_house.getVisibility() == View.VISIBLE)
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
                                                                                            App.get(education_level).equals(getResources().getString(R.string.common_education_level_no_formal)) ? "NO FORMAL EDUCATION" : "OTHER EDUCATION LEVEL"});
        if (other_education.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER EDUCATION LEVEL", App.get(other_education)});

        if (marital_status.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"MARITAL STATUS", App.get(marital_status).equals(getResources().getString(R.string.common_marital_status_single)) ? "SINGLE" :
                    App.get(marital_status).equals(getResources().getString(R.string.common_marital_status_engaged)) ? "ENGAGED" :
                            App.get(marital_status).equals(getResources().getString(R.string.common_marital_status_married)) ? "MARRIED" :
                                    App.get(marital_status).equals(getResources().getString(R.string.common_marital_status_separated)) ? "SEPARATED" :
                                            App.get(marital_status).equals(getResources().getString(R.string.common_marital_status_divorced)) ? "DIVORCED" :
                                                    App.get(marital_status).equals(getResources().getString(R.string.common_marital_status_unknown)) ? "UNKNOWN" :
                                                            App.get(marital_status).equals(getResources().getString(R.string.common_marital_status_refused)) ? "REFUSED" : "WIDOWED"});


        if (children.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CHILDREN", App.get(children).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if (children_number.getVisibility() == View.VISIBLE)
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
                                                                                                                                            (App.get(counselling).equals(getResources().getString(R.string.common_counselling_neighbor)) ? "NEIGHBOR" : "OTHER FAMILY MEMBER"))))))))))))))))});
        if (other_family_member.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FAMILY MEMBER", App.get(other_family_member)});

       /* if (tb_infection_type.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOSIS INFECTION TYPE", App.get(tb_infection_type).equals(getResources().getString(R.string.common_tb_infection_type_drtb)) ? "DRUG-RESISTANT TB" : "DRUG-SENSITIVE TUBERCULOSIS INFECTION"});
*/
      /*  if (tb_type.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SITE OF TUBERCULOSIS DISEASE", App.get(tb_type).equals(getResources().getString(R.string.common_tb_type_ptb)) ? "PULMONARY TUBERCULOSIS" : "EXTRA-PULMONARY TUBERCULOSIS"});

        if (extra_pulmonary_site.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"EXTRA PULMONARY SITE", App.get(extra_pulmonary_site)});

        if (drug_resistance_profile.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DRUG RESISTANCE PROFILE", App.get(drug_resistance_profile)});

        if (drug_resistant_profile_class.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SUB-CLASSIFICATION FOR DRUG RESISTANT CASES", App.get(drug_resistant_profile_class)});
        if (report_comorbidity.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COMORBIDITIES REPORTED", App.get(report_comorbidity).equals(getResources().getString(R.string.yes)) ? "YES" :
                    App.get(report_comorbidity).equals(getResources().getString(R.string.no)) ? "NO" : "UNKNOWN"});*/

        if (medical_condition.getVisibility() == View.VISIBLE) {
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
            }
            observations.add(new String[]{"GENERAL MEDICAL CONDITION", referredToString});
        }

        if (report_comorbidity.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"COMORBIDITIES REPORTED", App.get(report_comorbidity).equals(getResources().getString(R.string.yes)) ? "YES" :
                    App.get(report_comorbidity).equals(getResources().getString(R.string.no)) ? "NO" : "UNKNOWN"});
        }

        if (other_disease.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER DISEASE", App.get(other_disease)});

        if (drug_abuse_history.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HISTORY OF DRUG ABUSE", App.get(drug_abuse_history).equals(getResources().getString(R.string.yes)) ? "YES" :
                    App.get(drug_abuse_history).equals(getResources().getString(R.string.no)) ? "NO" :
                            App.get(drug_abuse_history).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "PAST"});

        if (substance_abuse.getVisibility() == View.VISIBLE) {
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
        }

        if (drug_substance_type_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER DRUG / SUBSTANCE TYPE", App.get(other_disease)});

        if (past_drug_abuse_age.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"INITIAL AGE OF SUBSTANCE ABUSE", App.get(past_drug_abuse_age)});

        if (akuads_score.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"AKUADS SCORE", App.get(akuads_score)});

        if (hallucination.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HALLUCINATION", App.get(hallucination).equals(getResources().getString(R.string.yes)) ? "YES" :
                    App.get(hallucination).equals(getResources().getString(R.string.no)) ? "NO" : "UNKNOWN"});

        if (hallucination_type.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HALLUCINATION TYPE", App.get(hallucination_type)});

        if (delusion.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DELUSION", App.get(delusion).equals(getResources().getString(R.string.yes)) ? "YES" :
                    App.get(delusion).equals(getResources().getString(R.string.no)) ? "NO" : "UNKNOWN"});

        if (delusion_type.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DELUSION TYPE", App.get(delusion_type)});

        if (know_symptom_tb.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"AWARE OF TB SYMPTOMS", App.get(know_symptom_tb).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if (knowledge_type_symptom_tb.getVisibility() == View.VISIBLE) {
            String referredToString = "";
            for (CheckBox cb : knowledge_type_symptom_tb.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_knowledge_type_symptom_tb_cough)))
                    referredToString = referredToString + "COUGH LASTING MORE THAN 3 WEEKS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_knowledge_type_symptom_tb_hemptysis)))
                    referredToString = referredToString + "HEMOPTYSIS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_knowledge_type_symptom_tb_fever)))
                    referredToString = referredToString + "FEVER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_knowledge_type_symptom_tb_weight)))
                    referredToString = referredToString + "WEIGHT LOSS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_knowledge_type_symptom_tb_night)))
                    referredToString = referredToString + "NIGHT SWEATS" + " ; ";
            }
            observations.add(new String[]{"KNOWLEDGE ABOUT TB SYMPTOMS", referredToString});
        }

        if (know_transmission_tb.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"AWARE OF TB TRANSMISSION CHANNELS", App.get(know_transmission_tb).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if (knowledge_type_transmission_tb.getVisibility() == View.VISIBLE) {
            String referredToString = "";
            for (CheckBox cb : knowledge_type_transmission_tb.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_knowledge_type_transmission_tb_through)))
                    referredToString = referredToString + "THROUGH GERMS PRESENT IN AIR EXPELLED IN THE COUGH" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_knowledge_type_transmission_tb_utensils)))
                    referredToString = referredToString + "UTENSILS SHARING WITH AN INFECTED PERSON" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_knowledge_type_transmission_tb_physical)))
                    referredToString = referredToString + "DIRECT PHYSICAL CONTACT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_knowledge_type_transmission_tb_genetics)))
                    referredToString = referredToString + "GENETICS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_knowledge_type_transmission_tb_breast)))
                    referredToString = referredToString + "BREASTFEEDING" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_knowledge_type_transmission_tb_other)))
                    referredToString = referredToString + "OTHER" + " ; ";
            }
            observations.add(new String[]{"KNOWLEDGE ABOUT TB TRANSMISSION", referredToString});
        }

        if (counseling_provided_for.getVisibility() == View.VISIBLE) {
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
            }
            observations.add(new String[]{"COUNSELING PROVIDED FOR", referredToString});
        }

        if (patient_behaviour.getVisibility() == View.VISIBLE) {
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
        }

        if (counsel_next_followup.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELING REQUIRED ON NEXT FOLLOW UP", App.get(counsel_next_followup).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if (person_counsel_next_followup.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELING REQUIRED FOR WHOM", (App.get(person_counsel_next_followup).equals(getResources().getString(R.string.common_counselling_self))) ? "SELF" :
                    (App.get(person_counsel_next_followup).equals(getResources().getString(R.string.common_counselling_parent)) ? "PARENT" :
                            (App.get(person_counsel_next_followup).equals(getResources().getString(R.string.common_counselling_guardian)) ? "GUARDIAN" :
                                    (App.get(person_counsel_next_followup).equals(getResources().getString(R.string.common_counselling_mother)) ? "MOTHER" :
                                            (App.get(person_counsel_next_followup).equals(getResources().getString(R.string.common_counselling_father)) ? "FATHER" :
                                                    (App.get(person_counsel_next_followup).equals(getResources().getString(R.string.common_counselling_maternal_grand_mother)) ? "MATERNAL GRANDMOTHER" :
                                                            (App.get(person_counsel_next_followup).equals(getResources().getString(R.string.common_counselling_maternal_grand_father)) ? "MATERNAL GRANDFATHER" :
                                                                    (App.get(person_counsel_next_followup).equals(getResources().getString(R.string.common_counselling_paternal_grand_mother)) ? "PATERNAL GRANDMOTHER" :
                                                                            (App.get(person_counsel_next_followup).equals(getResources().getString(R.string.common_counselling_maternal_grand_father)) ? "PATERNAL GRANDFATHER" :
                                                                                    (App.get(person_counsel_next_followup).equals(getResources().getString(R.string.common_counselling_brother)) ? "BROTHER" :
                                                                                            (App.get(person_counsel_next_followup).equals(getResources().getString(R.string.common_counselling_sister)) ? "SISTER" :
                                                                                                    (App.get(person_counsel_next_followup).equals(getResources().getString(R.string.common_counselling_son)) ? "SON" :
                                                                                                            (App.get(person_counsel_next_followup).equals(getResources().getString(R.string.common_counselling_daughter)) ? "DAUGHTER" :
                                                                                                                    (App.get(person_counsel_next_followup).equals(getResources().getString(R.string.common_counselling_spouse)) ? "SPOUSE" :
                                                                                                                            (App.get(person_counsel_next_followup).equals(getResources().getString(R.string.common_counselling_aunt)) ? "AUNT" :
                                                                                                                                    (App.get(person_counsel_next_followup).equals(getResources().getString(R.string.common_counselling_uncle)) ? "UNCLE" :
                                                                                                                                            (App.get(person_counsel_next_followup).equals(getResources().getString(R.string.common_counselling_neighbor)) ? "NEIGHBOR" : "COUNSELING RELATIONSHIP OTHER"))))))))))))))))});


        if (counseling_relationship_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELING RELATIONSHIP OTHER", App.get(counseling_relationship_other)});

        if (counselor_comments.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUNSELOR COMMENTS", App.get(counselor_comments)});

        observations.add(new String[]{"PATIENT REFERRED", App.get(patientReferred).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
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

        }
        if (referalReasonPsychologist.getVisibility() == View.VISIBLE) {

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

        }
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

        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("TIME TAKEN TO FILL form")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("FAMILY STRUCTURE")) {
                for (RadioButton rb : family_structure.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("EARNING FAMILY MEMBERS")) {
                earning_members.getEditText().setText(obs[0][1]);
                earning_members.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TOTAL MONTHLY HOUSEHOLD INCOME")) {
                monthly_household_income.getEditText().setText(obs[0][1]);
                monthly_household_income.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("INCOME CLASS")) {
                for (RadioButton rb : income_class.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.common_income_class_none)) && obs[0][1].equals("NONE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_income_class_lower)) && obs[0][1].equals("LOWER INCOME CLASS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_income_class_lower_middle)) && obs[0][1].equals("LOWER MIDDLE INCOME CLASS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_income_class_middle)) && obs[0][1].equals("MIDDLE INCOME CLASS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_income_class_upper_middle)) && obs[0][1].equals("UPPER MIDDLE INCOME CLASS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_income_class_uper)) && obs[0][1].equals("UPPER INCOME CLASS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("HOUSE TYPE")) {
                for (RadioButton rb : residence_type.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.common_residence_type_own)) && obs[0][1].equals("OWNED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_residence_type_rent)) && obs[0][1].equals("RENTED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_residence_type_other)) && obs[0][1].equals("OTHER RESIDENCE TYPE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER RESIDENCE TYPE")) {
                residence_type_other.getEditText().setText(obs[0][1]);
                residence_type_other.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("NUMBER OF ROOMS (IN HOUSE)")) {
                number_rooms_house.getEditText().setText(obs[0][1]);
                number_rooms_house.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("HIGHEST EDUCATION LEVEL")) {
                for (RadioButton rb : education_level.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.common_education_level_elementery)) && obs[0][1].equals("ELEMENTARY EDUCATION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_education_level_primary)) && obs[0][1].equals("PRIMARY EDUCATION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_education_level_secondary)) && obs[0][1].equals("SECONDARY EDUCATION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_education_level_inter)) && obs[0][1].equals("INTERMEDIATE EDUCATION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_education_level_undergraduate)) && obs[0][1].equals("UNDERGRADUATE EDUCATION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_education_level_graduate)) && obs[0][1].equals("GRADUATE EDUCATION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_education_level_doctorate)) && obs[0][1].equals("DOCTORATE EDUCATION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_education_level_polytechnic)) && obs[0][1].equals("POLYTECHNIC EDUCATION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_education_level_special_education)) && obs[0][1].equals("SPECIAL EDUCATION RECEIVED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_education_level_religious)) && obs[0][1].equals("RELIGIOUS EDUCATION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_education_level_no_formal)) && obs[0][1].equals("NO FORMAL EDUCATION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_education_level_other)) && obs[0][1].equals("OTHER EDUCATION LEVEL")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER EDUCATION LEVEL")) {
                other_education.getEditText().setText(obs[0][1]);
                other_education.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("MARITAL STATUS")) {
                for (RadioButton rb : marital_status.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.common_marital_status_single)) && obs[0][1].equals("SINGLE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_marital_status_engaged)) && obs[0][1].equals("ENGAGED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_marital_status_married)) && obs[0][1].equals("MARRIED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_marital_status_separated)) && obs[0][1].equals("SEPARATED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_marital_status_divorced)) && obs[0][1].equals("DIVORCED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_marital_status_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_marital_status_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_marital_status_widow)) && obs[0][1].equals("WIDOWED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("CHILDREN")) {
                for (RadioButton rb : children.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TOTAL NUMBER OF CHILDREN")) {
                children_number.getEditText().setText(obs[0][1]);
                children_number.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("FAMILY MEMBERS COUNSELLED")) {
                for (RadioButton rb : counselling.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.common_counselling_self)) && obs[0][1].equals("SELF")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_parent)) && obs[0][1].equals("PARENT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_guardian)) && obs[0][1].equals("GUARDIAN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_mother)) && obs[0][1].equals("MOTHER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_father)) && obs[0][1].equals("FATHER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_maternal_grand_mother)) && obs[0][1].equals("MATERNAL GRANDMOTHER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_maternal_grand_father)) && obs[0][1].equals("MATERNAL GRANDFATHER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_paternal_grand_mother)) && obs[0][1].equals("PATERNAL GRANDMOTHER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_paternal_grand_father)) && obs[0][1].equals("PATERNAL GRANDFATHER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_brother)) && obs[0][1].equals("BROTHER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_sister)) && obs[0][1].equals("SISTER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_son)) && obs[0][1].equals("SON")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_daughter)) && obs[0][1].equals("DAUGHTER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_spouse)) && obs[0][1].equals("SPOUSE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_aunt)) && obs[0][1].equals("AUNT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_uncle)) && obs[0][1].equals("UNCLE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_neighbor)) && obs[0][1].equals("NEIGHBOR")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_other)) && obs[0][1].equals("OTHER FAMILY MEMBER")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER FAMILY MEMBER")) {
                other_family_member.getEditText().setText(obs[0][1]);
                other_family_member.setVisibility(View.VISIBLE);
            } /*else if (obs[0][0].equals("TUBERCULOSIS INFECTION TYPE")) {
                for (RadioButton rb : tb_infection_type.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.common_tb_infection_type_drtb)) && obs[0][1].equals("DRUG-RESISTANT TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_tb_infection_type_dstb)) && obs[0][1].equals("DRUG-SENSITIVE TUBERCULOSIS INFECTION")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } *//*else if (obs[0][0].equals("SITE OF TUBERCULOSIS DISEASE")) {
                for (RadioButton rb : tb_type.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.common_tb_type_ptb)) && obs[0][1].equals("PULMONARY TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_tb_type_eptb)) && obs[0][1].equals("EXTRA-PULMONARY TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("EXTRA PULMONARY SITE")) {
                extra_pulmonary_site.getEditText().setText(obs[0][1]);
                extra_pulmonary_site.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("SITE OF TUBERCULOSIS DISEASE")) {
                for (RadioButton rb : diagnosis_type.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.common_diagnosis_type_bacterio)) && obs[0][1].equalsIgnoreCase("Bacteriologically confirmed")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_diagnosis_type_clinically)) && obs[0][1].equalsIgnoreCase("Clinically diagnosed")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("DRUG RESISTANCE PROFILE")) {
                drug_resistance_profile.getEditText().setText(obs[0][1]);
                drug_resistance_profile.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("SUB-CLASSIFICATION FOR DRUG RESISTANT CASES")) {
                drug_resistant_profile_class.getEditText().setText(obs[0][1]);
                drug_resistant_profile_class.setVisibility(View.VISIBLE);
            }*/ else if (obs[0][0].equals("COMORBIDITIES REPORTED")) {
                for (RadioButton rb : report_comorbidity.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("GENERAL MEDICAL CONDITION")) {
                for (CheckBox cb : medical_condition.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.common_medical_condition_diabetes)) && obs[0][1].equals("DIABETES MELLITUS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_medical_condition_hyper)) && obs[0][1].equals("HYPERTENSION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_medical_condition_epilepsy)) && obs[0][1].equals("EPILEPSY")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_medical_condition_intellectual)) && obs[0][1].equals("INTELLECTUAL FUNCTIONING DISABILITY")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_medical_condition_physically)) && obs[0][1].equals("PHYSICALLY DISABLE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_medical_condition_hiv)) && obs[0][1].equals("HUMAN IMMUNODEFICIENCY VIRUS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_medical_condition_hcv)) && obs[0][1].equals("HEPATITIS C VIRUS INFECTION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_medical_condition_others)) && obs[0][1].equals("OTHER DISEASE")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER DISEASE")) {
                other_disease.getEditText().setText(obs[0][1]);
                other_disease.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("HISTORY OF DRUG ABUSE")) {
                for (RadioButton rb : drug_abuse_history.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_drug_abuse_history_past)) && obs[0][1].equals("PAST")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("SUBSTANCE ABUSE")) {
                for (CheckBox cb : substance_abuse.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.common_substance_abuset_smoking)) && obs[0][1].equals("SMOKING")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_substance_abuset_alcohol)) && obs[0][1].equals("ALCOHOL ABUSE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_substance_abuset_drugs)) && obs[0][1].equals("DRUGS/SUBSTANCES")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_substance_abuset_all)) && obs[0][1].equals("ALL")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_substance_abuset_others)) && obs[0][1].equals("OTHER DRUG / SUBSTANCE TYPE")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER DRUG / SUBSTANCE TYPE")) {
                drug_substance_type_other.getEditText().setText(obs[0][1]);
                drug_substance_type_other.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("INITIAL AGE OF SUBSTANCE ABUSE")) {
                past_drug_abuse_age.getEditText().setText(obs[0][1]);
                past_drug_abuse_age.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("AKUADS SCORE")) {
                akuads_score.getEditText().setText(obs[0][1]);
                akuads_score.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("HALLUCINATION")) {
                for (RadioButton rb : hallucination.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("HALLUCINATION TYPE")) {
                hallucination_type.getEditText().setText(obs[0][1]);
                hallucination_type.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("DELUSION")) {
                for (RadioButton rb : delusion.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("DELUSION TYPE")) {
                delusion_type.getEditText().setText(obs[0][1]);
                delusion_type.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("AWARE OF TB SYMPTOMS")) {
                for (RadioButton rb : know_symptom_tb.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("KNOWLEDGE ABOUT TB SYMPTOMS")) {
                for (CheckBox cb : knowledge_type_symptom_tb.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.common_knowledge_type_symptom_tb_cough)) && obs[0][1].equals("COUGH LASTING MORE THAN 3 WEEKS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_knowledge_type_symptom_tb_hemptysis)) && obs[0][1].equals("HEMOPTYSIS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_knowledge_type_symptom_tb_fever)) && obs[0][1].equals("FEVER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_knowledge_type_symptom_tb_weight)) && obs[0][1].equals("WEIGHT LOSS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_knowledge_type_symptom_tb_night)) && obs[0][1].equals("NIGHT SWEATS")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("AWARE OF TB TRANSMISSION CHANNELS")) {
                for (RadioButton rb : know_transmission_tb.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("KNOWLEDGE ABOUT TB TRANSMISSION")) {
                for (CheckBox cb : knowledge_type_transmission_tb.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.common_knowledge_type_transmission_tb_through)) && obs[0][1].equals("THROUGH GERMS PRESENT IN AIR EXPELLED IN THE COUGH")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_knowledge_type_transmission_tb_utensils)) && obs[0][1].equals("UTENSILS SHARING WITH AN INFECTED PERSON")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_knowledge_type_transmission_tb_physical)) && obs[0][1].equals("DIRECT PHYSICAL CONTACT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_knowledge_type_transmission_tb_genetics)) && obs[0][1].equals("GENETICS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_knowledge_type_transmission_tb_breast)) && obs[0][1].equals("BREASTFEEDING")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_knowledge_type_transmission_tb_other)) && obs[0][1].equals("OTHER")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUNSELING PROVIDED FOR")) {
                for (CheckBox cb : counseling_provided_for.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.counseling_provided_for_type)) && obs[0][1].equals("TYPES OF TB")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.counseling_provided_for_important_nutrition)) && obs[0][1].equals("IMPORTANCE OF NUTRITION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.counseling_provided_for_duration)) && obs[0][1].equals("DURATION OF TB TREATMENT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.counseling_provided_for_treatment)) && obs[0][1].equals("TREATMENT PROCEDURE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.counseling_provided_for_preventive)) && obs[0][1].equals("PREVENTIVE TREATMENT (IPT)")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.counseling_provided_for_adverse)) && obs[0][1].equals("ADVERSE EVENTS AND THEIR MANAGEMENT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.counseling_provided_for_infection)) && obs[0][1].equals("INFECTION CONTROL")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.counseling_provided_for_transmission)) && obs[0][1].equals("TRANSMISSION OF TB")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.counseling_provided_for_important_of_treatment)) && obs[0][1].equals("IMPORTANCE OF TREATMENT ADHERENCE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.counseling_provided_for_purpose)) && obs[0][1].equals("IMPORTANCE OF CONTACT SCREENING")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.counseling_provided_for_important_of_regular)) && obs[0][1].equals("IMPORTANCE OF REGULAR MONTHLY FOLLOW UP")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.counseling_provided_for_tb_helpline)) && obs[0][1].equals("TB HELPLINE NUMBER")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("BEHAVIOUR")) {
                for (CheckBox cb : patient_behaviour.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_normal)) && obs[0][1].equals("NORMAL")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_irritable)) && obs[0][1].equals("IRRITABILITY")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_stubborn)) && obs[0][1].equals("STUBBORN BEHAVIOUR")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_shy)) && obs[0][1].equals("INTROVERTED PERSONALITY")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_aggressive)) && obs[0][1].equals("AGGRESSIVE BEHAVIOUR")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_argument)) && obs[0][1].equals("ARGUMENTATIVE BEHAVIOUR")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_non_compliant)) && obs[0][1].equals("NON COMPLIANT BEHAVIOUR")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_compliant)) && obs[0][1].equals("COMPLIANT BEHAVIOUR")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_cooperative)) && obs[0][1].equals("COOPERATIVE BEHAVIOUR")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_patient_behaviour_non_cooprerative)) && obs[0][1].equals("NON-COOPERATIVE BEHAVIOUR")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUNSELING REQUIRED ON NEXT FOLLOW UP")) {
                for (RadioButton rb : counsel_next_followup.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUNSELING REQUIRED FOR WHOM")) {
                for (RadioButton rb : counselling.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.common_counselling_self)) && obs[0][1].equals("SELF")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_parent)) && obs[0][1].equals("PARENT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_guardian)) && obs[0][1].equals("GUARDIAN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_mother)) && obs[0][1].equals("MOTHER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_father)) && obs[0][1].equals("FATHER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_maternal_grand_mother)) && obs[0][1].equals("MATERNAL GRANDMOTHER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_maternal_grand_father)) && obs[0][1].equals("MATERNAL GRANDFATHER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_paternal_grand_mother)) && obs[0][1].equals("PATERNAL GRANDMOTHER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_paternal_grand_father)) && obs[0][1].equals("PATERNAL GRANDFATHER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_brother)) && obs[0][1].equals("BROTHER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_sister)) && obs[0][1].equals("SISTER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_son)) && obs[0][1].equals("SON")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_daughter)) && obs[0][1].equals("DAUGHTER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_spouse)) && obs[0][1].equals("SPOUSE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_aunt)) && obs[0][1].equals("AUNT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_uncle)) && obs[0][1].equals("UNCLE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_neighbor)) && obs[0][1].equals("NEIGHBOR")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_counselling_other)) && obs[0][1].equals("COUNSELING RELATIONSHIP OTHER")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUNSELING RELATIONSHIP OTHER")) {
                counseling_relationship_other.getEditText().setText(obs[0][1]);
                counseling_relationship_other.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("COUNSELOR COMMENTS")) {
                counselor_comments.getEditText().setText(obs[0][1]);
                counselor_comments.setVisibility(View.VISIBLE);
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
        drug_resistant_profile_class.getEditText().setEnabled(false);
        income_class.setRadioGroupEnabled(false);
        tb_type.setRadioGroupEnabled(false);
        tb_infection_type.setRadioGroupEnabled(false);
        diagnosis_type.getEditText().setEnabled(false);

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
        if (flag) {
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
                    if (result.get("s_drug_resistance_profile_class") != null)
                        drug_resistant_profile_class.getEditText().setText(result.get("s_drug_resistance_profile_class").toString());
                    if (result.get("s_akuads_score") != null) {
                        akuads_score.getEditText().setText(result.get("s_akuads_score").toString());
                        akuads_score.getEditText().setEnabled(false);
                    } else {
                        akuads_score.setEnabled(true);
                    }

                }
            };
            autopopulateFormTask.execute("");
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
                        if (cb1.isChecked() && cb1.getText().equals(getString(R.string.other)))
                            otherReferalReasonPsychologist.setVisibility(View.VISIBLE);
                        otherReferalReasonPsychologist.getEditText().requestFocus();
                    }
                }
            } else if (cb.getText().equals(getString(R.string.site_supervisor)) || cb.getText().equals(getString(R.string.field_supervisor))) {
                if (cb.isChecked()) {
                    referalReasonSupervisor.setVisibility(View.VISIBLE);
                    for (CheckBox cb1 : referalReasonSupervisor.getCheckedBoxes()) {
                        if (cb1.isChecked() && cb1.getText().equals(getString(R.string.other))) {
                            otherReferalReasonSupervisor.setVisibility(View.VISIBLE);
                            otherReferalReasonSupervisor.getEditText().requestFocus();
                        }
                    }
                }
            } else if (cb.getText().equals(getString(R.string.call_center))) {
                if (cb.isChecked()) {
                    referalReasonCallCenter.setVisibility(View.VISIBLE);
                    for (CheckBox cb1 : referalReasonCallCenter.getCheckedBoxes()) {
                        if (cb1.isChecked() && cb1.getText().equals(getString(R.string.other))) {
                            otherReferalReasonCallCenter.setVisibility(View.VISIBLE);
                            otherReferalReasonCallCenter.getEditText().requestFocus();
                        }
                    }
                }
            } else if (cb.getText().equals(getString(R.string.clinician))) {
                if (cb.isChecked()) {
                    referalReasonClinician.setVisibility(View.VISIBLE);
                    for (CheckBox cb1 : referalReasonClinician.getCheckedBoxes()) {
                        if (cb1.isChecked() && cb1.getText().equals(getString(R.string.other))) {
                            otherReferalReasonClinician.setVisibility(View.VISIBLE);
                            otherReferalReasonClinician.getEditText().requestFocus();
                        }
                    }
                }
            }

        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == residence_type.getRadioGroup()) {
            residence_type_other.setVisibility(View.GONE);
            if (residence_type.getRadioGroup().getSelectedValue().equals(getString(R.string.common_residence_type_other))) {
                residence_type_other.setVisibility(View.VISIBLE);
            }
        }
        if (radioGroup == education_level.getRadioGroup()) {
            other_education.setVisibility(View.GONE);
            if (education_level.getRadioGroup().getSelectedValue().equals(getString(R.string.common_education_level_other))) {
                other_education.setVisibility(View.VISIBLE);
            }
        }
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
        if (radioGroup == counselling.getRadioGroup()) {
            other_family_member.setVisibility(View.GONE);
            if (counselling.getRadioGroup().getSelectedValue().equals(getString(R.string.common_counselling_other))) {
                other_family_member.setVisibility(View.VISIBLE);
            }
        }
        if (radioGroup == tb_type.getRadioGroup()) {
            extra_pulmonary_site.setVisibility(View.GONE);
            if (tb_type.getRadioGroup().getSelectedValue().equals(getString(R.string.common_tb_type_eptb))) {
                extra_pulmonary_site.setVisibility(View.VISIBLE);
            }
        }
        if (radioGroup == report_comorbidity.getRadioGroup()) {
            medical_condition.setVisibility(View.GONE);
            other_disease.setVisibility(View.GONE);
            if (report_comorbidity.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                for (CheckBox cb : medical_condition.getCheckedBoxes()) {
                    cb.setChecked(false);
                    medical_condition.setVisibility(View.VISIBLE);
                    medical_condition.getCheckedBoxes().get(0).setChecked(true);
                }
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
        if (radioGroup == know_symptom_tb.getRadioGroup()) {
            knowledge_type_symptom_tb.setVisibility(View.GONE);
            if (know_symptom_tb.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                knowledge_type_symptom_tb.getCheckedBoxes().get(0).setChecked(true);
                knowledge_type_symptom_tb.setVisibility(View.VISIBLE);
            }
        }
        if (radioGroup == know_transmission_tb.getRadioGroup()) {
            knowledge_type_transmission_tb.setVisibility(View.GONE);
            if (know_transmission_tb.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                knowledge_type_transmission_tb.getCheckedBoxes().get(0).setChecked(true);
                knowledge_type_transmission_tb.setVisibility(View.VISIBLE);
            }
        }
        if (radioGroup == counsel_next_followup.getRadioGroup()) {
            person_counsel_next_followup.setVisibility(View.GONE);
            counseling_relationship_other.setVisibility(View.GONE);
            if (counsel_next_followup.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                person_counsel_next_followup.getRadioGroup().clearCheck();
                person_counsel_next_followup.setVisibility(View.VISIBLE);
            }
        }
        if (radioGroup == person_counsel_next_followup.getRadioGroup()) {
            counseling_relationship_other.setVisibility(View.GONE);
            if (person_counsel_next_followup.getRadioGroup().getSelectedValue().equals(getString(R.string.common_counselling_other))) {
                counseling_relationship_other.setVisibility(View.VISIBLE);
            }
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
