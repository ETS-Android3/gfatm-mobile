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
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 2/21/2017.
 */

public class FollowupCounselingForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledRadioGroup followup_visit_type;
    TitledEditText followup_reason_other;
    TitledEditText followup_month;
    TitledRadioGroup treatment_outcome;
    TitledRadioGroup tb_treatment_phase;
    TitledRadioGroup counselling;
    TitledEditText counselling_other;
    TextView heading_disease_info;
    TitledRadioGroup reason_followup_counseling;
    TitledEditText referral_complaint_by_field_team;
    TitledEditText other_referral_complaint_by_field_team;
    TitledEditText akuads_score;
    TitledRadioGroup adverse_event_last_visit;
    TitledCheckBoxes adverse_events;
    TitledEditText adverse_event_other;
    TitledCheckBoxes occupational_problem;
    TitledCheckBoxes relation_problem;
    TitledEditText relation_problem_other;
    TitledCheckBoxes psych_environmental_problem;
    TitledEditText psych_environmental_problem_other;
    TitledCheckBoxes patient_behaviour;
    TitledEditText counselor_comments;
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
        formName = Forms.FOLLOWUP_COUNSELING;
        form = Forms.followupCounseling;

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

        followup_visit_type = new TitledRadioGroup(context, null, getResources().getString(R.string.common_followup_visit_type), getResources().getStringArray(R.array.common_followup_visit_type_options), getResources().getString(R.string.common_followup_visit_type_planned), App.VERTICAL, App.VERTICAL, true);
        followup_reason_other = new TitledEditText(context, null, getResources().getString(R.string.common_followup_visit_type_specify_other), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        followup_month = new TitledEditText(context, null, getResources().getString(R.string.common_followup_month), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        treatment_outcome = new TitledRadioGroup(context, null, getResources().getString(R.string.common_treatment_outcome), getResources().getStringArray(R.array.common_treatment_outcome_options), getResources().getString(R.string.common_tb_treatment_phase_intensive), App.VERTICAL, App.VERTICAL, true);
        tb_treatment_phase = new TitledRadioGroup(context, null, getResources().getString(R.string.common_tb_treatment_phase), getResources().getStringArray(R.array.common_tb_treatment_phase_options), getResources().getString(R.string.common_tb_treatment_phase_intensive), App.VERTICAL, App.VERTICAL, true);
        counselling = new TitledRadioGroup(context, null, getResources().getString(R.string.common_counselling), getResources().getStringArray(R.array.common_counselling_options), getResources().getString(R.string.common_counselling_self), App.VERTICAL, App.VERTICAL, true);
        counselling_other = new TitledEditText(context, null, getResources().getString(R.string.common_counselling_specify_other), "", "", 25, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        reason_followup_counseling = new TitledRadioGroup(context, null, getResources().getString(R.string.common_reason_followup_counseling), getResources().getStringArray(R.array.common_reason_followup_counseling_options), null, App.VERTICAL, App.VERTICAL, true);
        referral_complaint_by_field_team = new TitledEditText(context, null, getResources().getString(R.string.common_referral_complaint_by_field_team), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        other_referral_complaint_by_field_team = new TitledEditText(context, null, getResources().getString(R.string.common_other_referral_complaint_by_field_team), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        akuads_score = new TitledEditText(context, null, getResources().getString(R.string.common_akuads_score), "", "", 2, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        adverse_event_last_visit = new TitledRadioGroup(context, null, getResources().getString(R.string.common_adverse_event_last_visit), getResources().getStringArray(R.array.common_adverse_event_last_visit_options), null, App.VERTICAL, App.VERTICAL, true);
        adverse_events = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_adverse_events_2), getResources().getStringArray(R.array.common_adverse_events_2_options), new Boolean[]{true}, App.VERTICAL, App.VERTICAL, true);
        adverse_event_other = new TitledEditText(context, null, getResources().getString(R.string.common_adverse_events_2_specify_other), "", "", 25, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        occupational_problem = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_occupational_problem), getResources().getStringArray(R.array.common_occupational_problem_options), null, App.VERTICAL, App.VERTICAL, true);
        relation_problem = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_relation_problem), getResources().getStringArray(R.array.common_relation_problem_options), null, App.VERTICAL, App.VERTICAL, false);
        relation_problem_other = new TitledEditText(context, null, getResources().getString(R.string.common_relation_problem_specify_others), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        psych_environmental_problem = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_psych_environmental_problem), getResources().getStringArray(R.array.common_psych_environmental_problem_options), new Boolean[]{true}, App.VERTICAL, App.VERTICAL, true);
        psych_environmental_problem_other = new TitledEditText(context, null, getResources().getString(R.string.common_psych_environmental_problem_specify_other), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        patient_behaviour = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_patient_behaviour), getResources().getStringArray(R.array.common_patient_behaviour_options), null, App.VERTICAL, App.VERTICAL, true);
        counselor_comments = new TitledEditText(context, null, getResources().getString(R.string.common_counselor_comments), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
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
        heading_disease_info.setText("Disease Information");
        heading_disease_info.setTypeface(null, Typeface.BOLD);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), followup_visit_type.getRadioGroup(), followup_reason_other.getEditText(), followup_month.getEditText(),
                treatment_outcome.getRadioGroup(), tb_treatment_phase.getRadioGroup(), counselling.getRadioGroup(), counselling_other.getEditText(), reason_followup_counseling.getRadioGroup(),
                referral_complaint_by_field_team.getEditText(), other_referral_complaint_by_field_team.getEditText(), akuads_score.getEditText(), adverse_event_last_visit.getRadioGroup(),
                adverse_events, adverse_event_other.getEditText(), occupational_problem, relation_problem, relation_problem_other.getEditText(), psych_environmental_problem, psych_environmental_problem_other.getEditText(),
                patient_behaviour, counselor_comments.getEditText(), patientReferred.getRadioGroup(), referredTo, referalReasonPsychologist, otherReferalReasonPsychologist.getEditText(), referalReasonSupervisor, otherReferalReasonSupervisor.getEditText(),
                referalReasonCallCenter, otherReferalReasonCallCenter.getEditText(), referalReasonClinician, otherReferalReasonClinician.getEditText()
        };

        // Array used to display views accordingly...
        viewGroups = new View[][]{{formDate, followup_visit_type, followup_reason_other, followup_month, treatment_outcome, tb_treatment_phase,
                counselling, counselling_other, heading_disease_info, reason_followup_counseling, referral_complaint_by_field_team, other_referral_complaint_by_field_team, akuads_score,
                adverse_event_last_visit, adverse_events, adverse_event_other, occupational_problem, relation_problem, relation_problem_other, psych_environmental_problem, psych_environmental_problem_other,
                patient_behaviour, counselor_comments, patientReferred, referredTo, referalReasonPsychologist, otherReferalReasonPsychologist, referalReasonSupervisor, otherReferalReasonSupervisor,
                referalReasonCallCenter, otherReferalReasonCallCenter, referalReasonClinician, otherReferalReasonClinician
        },};

        formDate.getButton().setOnClickListener(this);
        followup_visit_type.getRadioGroup().setOnCheckedChangeListener(this);
        counselling.getRadioGroup().setOnCheckedChangeListener(this);
        reason_followup_counseling.getRadioGroup().setOnCheckedChangeListener(this);
        adverse_event_last_visit.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : adverse_events.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : relation_problem.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : psych_environmental_problem.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
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
        if (followup_visit_type.getVisibility() == View.VISIBLE && App.get(followup_visit_type).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            followup_visit_type.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            followup_visit_type.getQuestionView().setError(null);
        }
        if (followup_reason_other.getVisibility() == View.VISIBLE && followup_reason_other.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            followup_reason_other.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }
        if (followup_month.getVisibility() == View.VISIBLE && followup_month.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            followup_month.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }
        if (treatment_outcome.getVisibility() == View.VISIBLE && App.get(treatment_outcome).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            treatment_outcome.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            treatment_outcome.getQuestionView().setError(null);
        }
        if (tb_treatment_phase.getVisibility() == View.VISIBLE && App.get(tb_treatment_phase).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            tb_treatment_phase.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            tb_treatment_phase.getQuestionView().setError(null);
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
        if (counselling_other.getVisibility() == View.VISIBLE && counselling_other.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            counselling_other.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }
        if (reason_followup_counseling.getVisibility() == View.VISIBLE && App.get(reason_followup_counseling).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reason_followup_counseling.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            reason_followup_counseling.getQuestionView().setError(null);
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
        if (adverse_event_last_visit.getVisibility() == View.VISIBLE && App.get(adverse_event_last_visit).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            adverse_event_last_visit.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            adverse_event_last_visit.getQuestionView().setError(null);
        }

        if (adverse_events.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : adverse_events.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                adverse_events.getQuestionView().setError(getString(R.string.empty_field));
                adverse_events.getQuestionView().requestFocus();
                view = adverse_events;
                error = true;
            } else {
                adverse_events.getQuestionView().setError(null);
            }
        }
        if (adverse_event_other.getVisibility() == View.VISIBLE && adverse_event_other.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            adverse_event_other.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }

        if (occupational_problem.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : occupational_problem.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                occupational_problem.getQuestionView().setError(getString(R.string.empty_field));
                occupational_problem.getQuestionView().requestFocus();
                view = occupational_problem;
                error = true;
            } else {
                occupational_problem.getQuestionView().setError(null);
            }
        }
        /*if (relation_problem.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : relation_problem.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                relation_problem.getQuestionView().setError(getString(R.string.empty_field));
                relation_problem.getQuestionView().requestFocus();
                view = relation_problem;
                error = true;
            } else {
                relation_problem.getQuestionView().setError(null);
            }
        }*/

        if (relation_problem_other.getVisibility() == View.VISIBLE && relation_problem_other.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            relation_problem_other.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }
        if (psych_environmental_problem.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : psych_environmental_problem.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                psych_environmental_problem.getQuestionView().setError(getString(R.string.empty_field));
                psych_environmental_problem.getQuestionView().requestFocus();
                view = psych_environmental_problem;
                error = true;
            } else {
                psych_environmental_problem.getQuestionView().setError(null);
            }
        }
        if (psych_environmental_problem_other.getVisibility() == View.VISIBLE && psych_environmental_problem_other.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            psych_environmental_problem_other.getEditText().setError(getString(R.string.empty_field));
            error = true;
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

        if (followup_visit_type.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FOLLOW-UP VISIT TYPE", App.get(followup_visit_type).equals(getResources().getString(R.string.common_followup_visit_type_two_weeks)) ? "TWO WEEK FOLLOW UP" :
                    App.get(followup_visit_type).equals(getResources().getString(R.string.common_followup_visit_type_planned)) ? "PLANNED MONTHLY FOLLOW UP" :
                            App.get(followup_visit_type).equals(getResources().getString(R.string.common_followup_visit_type_end)) ? "END OF TREATMENT FOLLOW UP" :
                                    App.get(followup_visit_type).equals(getResources().getString(R.string.common_followup_visit_type_post)) ? "POST-TREATMENT FOLLOW UP" : "OTHER FOLLOW UP REASON"});

        if (followup_reason_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FOLLOW UP REASON", App.get(followup_reason_other)});

        if (followup_month.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FOLLOW-UP MONTH", App.get(followup_month)});

        if (treatment_outcome.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT OUTCOME", (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_cured))) ? "CURE, OUTCOME" :
                    (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_treatment_completed)) ? "TREATMENT COMPLETE" :
                            (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_treatment_failure)) ? "TUBERCULOSIS TREATMENT FAILURE" :
                                    (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_died)) ? "DIED" :
                                            (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_transferout)) ? "TRANSFERRED OUT" :
                                                    (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_referral)) ? "PATIENT REFERRED" :
                                                            (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_lost_to_follow)) ? "LOST TO FOLLOW-UP" :
                                                                    (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_clinically_evaluated)) ? "CLINICALLY EVALUATED, NO TB" :
                                                                            (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_antibiotic)) ? "ANTIBIOTIC COMPLETE - NO TB" :
                                                                                    (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_not_evaluated)) ? "NOT EVALUATED" :
                                                                                            (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_treatment_adapted)) ? "TREATMENT ADAPTED" :
                                                                                                    (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_contact_diagnosed)) ? "CONTACT DIAGNOSED WITH TB" :
                                                                                                            (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_refused_to_take_treatment)) ? "REFUSAL OF TREATMENT BY PATIENT" :
                                                                                                                    (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_refused_after_starting)) ? "PATIENT REFUSED TREATMENT AFTER STARTING" :
                                                                                                                            (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_refused_screening)) ? "REFUSED SCREENING" :
                                                                                                                                    (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_still_on_treatment)) ? "STILL ON TREATMENT" :
                                                                                                                                            (App.get(treatment_outcome).equals(getResources().getString(R.string.common_treatment_outcome_not_evaluated_shift_ltr)) ? "NOT EVALUATED (SHIFT TO LTR)" : "OTHER TREATMENT OUTCOME"))))))))))))))))});


        if (tb_treatment_phase.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOSIS TREATMENT PHASE", App.get(tb_treatment_phase).equals(getResources().getString(R.string.common_tb_treatment_phase_intensive)) ? "INTENSIVE PHASE" :
                    App.get(tb_treatment_phase).equals(getResources().getString(R.string.common_tb_treatment_phase_continuation)) ? "CONTINUATION PHASE, TUBERCULOSIS TREATMENT" : "NOT APPLICABLE"});

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
        if (counselling_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FAMILY MEMBER", App.get(counselling_other)});


        if (reason_followup_counseling.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON FOR COUNSELING", App.get(reason_followup_counseling).equals(getResources().getString(R.string.common_reason_followup_counseling_monthly)) ? "SCHEDULED COUNSELING" : "PATIENT REFERRED FOR COUNSELING"});

        if (akuads_score.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"AKUADS SCORE", App.get(akuads_score)});

        if (adverse_event_last_visit.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ADVERSE EVENT REPORTED", App.get(adverse_event_last_visit).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if (adverse_events.getVisibility() == View.VISIBLE) {

            String referredToString = "";
            for (CheckBox cb : adverse_events.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_vomit)))
                    referredToString = referredToString + "VOMITING" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_headache)))
                    referredToString = referredToString + "HEADACHE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_diarhea)))
                    referredToString = referredToString + "DIARRHEA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_behavioural)))
                    referredToString = referredToString + "BEHAVIOURAL CHANGE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_arthralgia)))
                    referredToString = referredToString + "JOINT PAIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_delusion)))
                    referredToString = referredToString + "DELUSION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_hearing)))
                    referredToString = referredToString + "HEARING DISORDER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_loss)))
                    referredToString = referredToString + "LOSS OF APPETITE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_skin)))
                    referredToString = referredToString + "ALLERGIC DERMATITIS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_nausea)))
                    referredToString = referredToString + "NAUSEA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_tinnitus)))
                    referredToString = referredToString + "TINNITUS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_hallucination)))
                    referredToString = referredToString + "HALLUCINATION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_weakness)))
                    referredToString = referredToString + "WEAKNESS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_depression)))
                    referredToString = referredToString + "DEPRESSION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_insomnia)))
                    referredToString = referredToString + "INSOMNIA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_deafness)))
                    referredToString = referredToString + "HEARING DISORDER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_fever)))
                    referredToString = referredToString + "FEVER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_vertigo)))
                    referredToString = referredToString + "VERTIGO" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_anxiety)))
                    referredToString = referredToString + "ANXIETY" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_weight)))
                    referredToString = referredToString + "WEIGHT LOSS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_lethargy)))
                    referredToString = referredToString + "PERIORAL NUMBNESS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_visual)))
                    referredToString = referredToString + "VISUAL IMPAIRMENT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_blur)))
                    referredToString = referredToString + "BLURRED VISION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_jaundice)))
                    referredToString = referredToString + "JAUNDICE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_numbness)))
                    referredToString = referredToString + "PERIORAL NUMBNESS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_other)))
                    referredToString = referredToString + "OTHER ADVERSE EVENT" + " ; ";
            }
            observations.add(new String[]{"ADVERSE EVENTS", referredToString});
        }

        if (occupational_problem.getVisibility() == View.VISIBLE) {

            String referredToString = "";
            for (CheckBox cb : occupational_problem.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_occupational_problem_none)))
                    referredToString = referredToString + "NONE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_occupational_problem_loss)))
                    referredToString = referredToString + "LOSS OF JOB DUE TO CONTINUOUS SICKNESS AND ABSENCE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_occupational_problem_termination)))
                    referredToString = referredToString + "TERMINATION OF JOB DUE TO TB" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_occupational_problem_difficulty)))
                    referredToString = referredToString + "DIFFICULTY USING MASK AT WORK" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_occupational_problem_social)))
                    referredToString = referredToString + "SOCIAL STIGMA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_occupational_problem_other)))
                    referredToString = referredToString + "OTHER" + " ; ";
            }
            observations.add(new String[]{"OCCUPATIONAL PROBLEMS", referredToString});

        }

        if (relation_problem.getVisibility() == View.VISIBLE) {

            String referredToString = "";
            for (CheckBox cb : relation_problem.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_relation_problem_none)))
                    referredToString = referredToString + "NONE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_relation_problem_marital)))
                    referredToString = referredToString + "MARITAL PROBLEMS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_relation_problem_issues)))
                    referredToString = referredToString + "ISSUES WITH IN-LAWS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_relation_problem_divorce)))
                    referredToString = referredToString + "DIVORCE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_relation_problem_separation)))
                    referredToString = referredToString + "SEPARATION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_relation_problem_others)))
                    referredToString = referredToString + "OTHER RELATION PROBLEM" + " ; ";
            }
            observations.add(new String[]{"RELATIONSHIP PROBLEMS", referredToString});

        }
        if (relation_problem_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER RELATION PROBLEM", App.get(relation_problem_other)});

        if (psych_environmental_problem.getVisibility() == View.VISIBLE) {

            String referredToString = "";
            for (CheckBox cb : psych_environmental_problem.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_none)))
                    referredToString = referredToString + "NONE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_self)))
                    referredToString = referredToString + "SELF NEGLIGENCE TOWARDS HEALTH" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_family)))
                    referredToString = referredToString + "FAMILY NEGLIGENCE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_parental)))
                    referredToString = referredToString + "PARENTAL CONFLICTS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_sexual)))
                    referredToString = referredToString + "SEXUAL, PHYSICAL, VERBAL ABUSE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_verbal)))
                    referredToString = referredToString + "VERBAL ABUSE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_academic)))
                    referredToString = referredToString + "ACADEMIC PROBLEM" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_financial)))
                    referredToString = referredToString + "ECONOMIC PROBLEM" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_other)))
                    referredToString = referredToString + "OTHER PSYCHOSOCIAL AND ENVIRONMENT PROBLEMS" + " ; ";
            }
            observations.add(new String[]{"PSYCHOSOCIAL AND ENVIROMENT PROBLEMS", referredToString});

        }
        if (psych_environmental_problem_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER PSYCHOSOCIAL AND ENVIRONMENT PROBLEMS", App.get(psych_environmental_problem_other)});


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
                    id = serverService.saveFormLocallyTesting(Forms.FOLLOWUP_COUNSELING, form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = "";


                result = serverService.saveEncounterAndObservationTesting(Forms.FOLLOWUP_COUNSELING, form, formDateCalendar, observations.toArray(new String[][]{}), id);
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
            } else if (obs[0][0].equals("FOLLOW-UP VISIT TYPE")) {
                for (RadioButton rb : followup_visit_type.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.common_followup_visit_type_two_weeks)) && obs[0][1].equals("TWO WEEK FOLLOW UP")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_followup_visit_type_planned)) && obs[0][1].equals("PLANNED MONTHLY FOLLOW UP")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_followup_visit_type_end)) && obs[0][1].equals("END OF TREATMENT FOLLOW UP")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_followup_visit_type_post)) && obs[0][1].equals("POST-TREATMENT FOLLOW UP")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_followup_visit_type_other)) && obs[0][1].equals("OTHER FOLLOW UP REASON")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER FOLLOW UP REASON")) {
                followup_reason_other.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                followup_month.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("TREATMENT OUTCOME")) {
                for (RadioButton rb : treatment_outcome.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_cured)) && obs[0][1].equals("CURE, OUTCOME")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_treatment_completed)) && obs[0][1].equals("TREATMENT COMPLETE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_treatment_failure)) && obs[0][1].equals("TUBERCULOSIS TREATMENT FAILURE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_died)) && obs[0][1].equals("DIED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_transferout)) && obs[0][1].equals("TRANSFERRED OUT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_referral)) && obs[0][1].equals("PATIENT REFERRED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_lost_to_follow)) && obs[0][1].equals("LOST TO FOLLOW-UP")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_clinically_evaluated)) && obs[0][1].equals("CLINICALLY EVALUATED, NO TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_antibiotic)) && obs[0][1].equals("ANTIBIOTIC COMPLETE - NO TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_not_evaluated)) && obs[0][1].equals("NOT EVALUATED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_treatment_adapted)) && obs[0][1].equals("TREATMENT ADAPTED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_contact_diagnosed)) && obs[0][1].equals("CONTACT DIAGNOSED WITH TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_refused_to_take_treatment)) && obs[0][1].equals("REFUSAL OF TREATMENT BY PATIENT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_refused_after_starting)) && obs[0][1].equals("PATIENT REFUSED TREATMENT AFTER STARTING")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_refused_screening)) && obs[0][1].equals("REFUSED SCREENING")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_still_on_treatment)) && obs[0][1].equals("STILL ON TREATMENT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_not_evaluated_shift_ltr)) && obs[0][1].equals("NOT EVALUATED (SHIFT TO LTR)")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_treatment_outcome_other)) && obs[0][1].equals("OTHER TREATMENT OUTCOME")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TUBERCULOSIS TREATMENT PHASE")) {
                for (RadioButton rb : tb_treatment_phase.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.common_tb_treatment_phase_continuation)) && obs[0][1].equals("CONTINUATION PHASE, TUBERCULOSIS TREATMENT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_tb_treatment_phase_intensive)) && obs[0][1].equals("INTENSIVE PHASE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_tb_treatment_phase_not_aplicable)) && obs[0][1].equals("NOT APPLICABLE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
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
                counselling_other.getEditText().setText(obs[0][1]);
                counselling_other.setVisibility(View.VISIBLE);

            } else if (obs[0][0].equals("REASON FOR COUNSELING")) {
                for (RadioButton rb : reason_followup_counseling.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.common_reason_followup_counseling_monthly)) && obs[0][1].equals("SCHEDULED COUNSELING")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_reason_followup_counseling_patient_refused)) && obs[0][1].equals("PATIENT REFERRED FOR COUNSELING")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }/* else if (obs[0][0].equals("REFERRAL COMPLAINT BY FIELD TEAM")) {
                referral_complaint_by_field_team.getEditText().setText(obs[0][1]);
                referral_complaint_by_field_team.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER REFERRAL COMPLAINT BY FIELD TEAM")) {
                other_referral_complaint_by_field_team.getEditText().setText(obs[0][1]);
                other_referral_complaint_by_field_team.setVisibility(View.VISIBLE);
            } */ else if (obs[0][0].equals("AKUADS SCORE")) {
                akuads_score.getEditText().setText(obs[0][1]);
                akuads_score.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ADVERSE EVENT REPORTED")) {
                for (RadioButton rb : adverse_event_last_visit.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("ADVERSE EVENTS")) {
                for (CheckBox cb : adverse_events.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_vomit)) && obs[0][1].equals("VOMITING")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_headache)) && obs[0][1].equals("HEADACHE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_diarhea)) && obs[0][1].equals("DIARRHEA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_behavioural)) && obs[0][1].equals("BEHAVIOURAL CHANGE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_arthralgia)) && obs[0][1].equals("JOINT PAIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_delusion)) && obs[0][1].equals("DELUSION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_hearing)) && obs[0][1].equals("HEARING DISORDER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_loss)) && obs[0][1].equals("LOSS OF APPETITE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_skin)) && obs[0][1].equals("ALLERGIC DERMATITIS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_nausea)) && obs[0][1].equals("NAUSEA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_tinnitus)) && obs[0][1].equals("TINNITUS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_hallucination)) && obs[0][1].equals("HALLUCINATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_weakness)) && obs[0][1].equals("WEAKNESS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_depression)) && obs[0][1].equals("DEPRESSION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_insomnia)) && obs[0][1].equals("INSOMNIA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_deafness)) && obs[0][1].equals("HEARING DISORDER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_fever)) && obs[0][1].equals("FEVER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_vertigo)) && obs[0][1].equals("VERTIGO")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_anxiety)) && obs[0][1].equals("ANXIETY")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_weight)) && obs[0][1].equals("WEIGHT LOSS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_lethargy)) && obs[0][1].equals("PERIORAL NUMBNESS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_blur)) && obs[0][1].equals("BLURRED VISION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_jaundice)) && obs[0][1].equals("JAUNDICE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_numbness)) && obs[0][1].equals("PERIORAL NUMBNESS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_other)) && obs[0][1].equals("OTHER ADVERSE EVENT")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER ADVERSE EVENT")) {
                adverse_event_other.getEditText().setText(obs[0][1]);
                adverse_event_other.setVisibility(View.VISIBLE);

            } else if (obs[0][0].equals("OCCUPATIONAL PROBLEMS")) {
                for (CheckBox cb : occupational_problem.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.common_occupational_problem_none)) && obs[0][1].equals("NONE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_occupational_problem_loss)) && obs[0][1].equals("LOSS OF JOB DUE TO CONTINUOUS SICKNESS AND ABSENCE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_occupational_problem_termination)) && obs[0][1].equals("TERMINATION OF JOB DUE TO TB")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_occupational_problem_difficulty)) && obs[0][1].equals("DIFFICULTY USING MASK AT WORK")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_occupational_problem_social)) && obs[0][1].equals("SOCIAL STIGMA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_occupational_problem_other)) && obs[0][1].equals("OTHER")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("RELATIONSHIP PROBLEMS")) {
                for (CheckBox cb : relation_problem.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.common_relation_problem_none)) && obs[0][1].equals("NONE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_relation_problem_marital)) && obs[0][1].equals("MARITAL PROBLEMS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_relation_problem_issues)) && obs[0][1].equals("ISSUES WITH IN-LAWS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_relation_problem_divorce)) && obs[0][1].equals("DIVORCE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_relation_problem_separation)) && obs[0][1].equals("SEPARATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_relation_problem_others)) && obs[0][1].equals("OTHER RELATION PROBLEM")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER RELATION PROBLEM")) {
                relation_problem_other.getEditText().setText(obs[0][1]);
                relation_problem_other.setVisibility(View.VISIBLE);

            } else if (obs[0][0].equals("PSYCHOSOCIAL AND ENVIROMENT PROBLEMS")) {
                for (CheckBox cb : psych_environmental_problem.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_none)) && obs[0][1].equals("NONE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_self)) && obs[0][1].equals("SELF NEGLIGENCE TOWARDS HEALTH")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_family)) && obs[0][1].equals("FAMILY NEGLIGENCE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_parental)) && obs[0][1].equals("PARENTAL CONFLICTS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_sexual)) && obs[0][1].equals("SEXUAL, PHYSICAL, VERBAL ABUSE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_verbal)) && obs[0][1].equals("VERBAL ABUSE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_academic)) && obs[0][1].equals("ACADEMIC PROBLEM")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_financial)) && obs[0][1].equals("ECONOMIC PROBLEM")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_other)) && obs[0][1].equals("OTHER PSYCHOSOCIAL AND ENVIRONMENT PROBLEMS")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER PSYCHOSOCIAL AND ENVIRONMENT PROBLEMS")) {
                psych_environmental_problem_other.getEditText().setText(obs[0][1]);
                psych_environmental_problem_other.setVisibility(View.VISIBLE);

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
        for (CheckBox cb : adverse_events.getCheckedBoxes()) {
            if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_2_other)) && cb.isChecked()) {
                adverse_event_other.setVisibility(View.VISIBLE);
                break;
            } else {
                adverse_event_other.setVisibility(View.GONE);
            }
        }
        for (CheckBox cb : relation_problem.getCheckedBoxes()) {
            if (cb.getText().equals(getResources().getString(R.string.common_relation_problem_others)) && cb.isChecked()) {
                relation_problem_other.setVisibility(View.VISIBLE);
                break;
            } else {
                relation_problem_other.setVisibility(View.GONE);
            }
        }
        for (CheckBox cb : psych_environmental_problem.getCheckedBoxes()) {
            if (cb.getText().equals(getResources().getString(R.string.common_psych_environmental_problem_other)) && cb.isChecked()) {
                psych_environmental_problem_other.setVisibility(View.VISIBLE);
                break;
            } else {
                psych_environmental_problem_other.setVisibility(View.GONE);
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
        referredTo.setVisibility(View.GONE);
        referalReasonPsychologist.setVisibility(View.GONE);
        otherReferalReasonPsychologist.setVisibility(View.GONE);
        referalReasonSupervisor.setVisibility(View.GONE);
        otherReferalReasonSupervisor.setVisibility(View.GONE);
        referalReasonCallCenter.setVisibility(View.GONE);
        otherReferalReasonCallCenter.setVisibility(View.GONE);
        referalReasonClinician.setVisibility(View.GONE);
        otherReferalReasonClinician.setVisibility(View.GONE);
        if (App.getPatient().getPerson().getAge() < 15) {
            counselling.setVisibility(View.VISIBLE);
        } else {
            counselling.setVisibility(View.GONE);
            counselling_other.setVisibility(View.GONE);
        }

        referral_complaint_by_field_team.getEditText().setEnabled(false);
        other_referral_complaint_by_field_team.getEditText().setEnabled(false);

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
            updateFollowUpMonth();
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

                    String s_tb_treatment_outcome = serverService.getLatestObsValue(App.getPatientId(), "TREATMENT OUTCOME");
                    result.put("s_tb_treatment_outcome", s_tb_treatment_outcome);
                    return result;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                }


                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();
                    if (result.get("s_tb_treatment_outcome") != null) {
                        if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("CURE, OUTCOME")) {
                            treatment_outcome.getRadioGroup().getButtons().get(0).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("TREATMENT COMPLETE")) {
                            treatment_outcome.getRadioGroup().getButtons().get(1).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("TUBERCULOSIS TREATMENT FAILURE")) {
                            treatment_outcome.getRadioGroup().getButtons().get(2).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("DIED")) {
                            treatment_outcome.getRadioGroup().getButtons().get(3).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("TRANSFERRED OUT")) {
                            treatment_outcome.getRadioGroup().getButtons().get(4).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("PATIENT REFERRED")) {
                            treatment_outcome.getRadioGroup().getButtons().get(5).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("LOST TO FOLLOW-UP")) {
                            treatment_outcome.getRadioGroup().getButtons().get(6).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("CLINICALLY EVALUATED, NO TB")) {
                            treatment_outcome.getRadioGroup().getButtons().get(7).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("ANTIBIOTIC COMPLETE - NO TB")) {
                            treatment_outcome.getRadioGroup().getButtons().get(8).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("NOT EVALUATED")) {
                            treatment_outcome.getRadioGroup().getButtons().get(9).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("TREATMENT ADAPTED")) {
                            treatment_outcome.getRadioGroup().getButtons().get(10).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("CONTACT DIAGNOSED WITH TB")) {
                            treatment_outcome.getRadioGroup().getButtons().get(11).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("REFUSAL OF TREATMENT BY PATIENT")) {
                            treatment_outcome.getRadioGroup().getButtons().get(12).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("PATIENT REFUSED TREATMENT AFTER STARTING")) {
                            treatment_outcome.getRadioGroup().getButtons().get(13).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("REFUSED SCREENING")) {
                            treatment_outcome.getRadioGroup().getButtons().get(14).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("STILL ON TREATMENT")) {
                            treatment_outcome.getRadioGroup().getButtons().get(15).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("NOT EVALUATED (SHIFT TO LTR)")) {
                            treatment_outcome.getRadioGroup().getButtons().get(16).setChecked(true);
                        } else if (result.get("s_tb_treatment_outcome").equalsIgnoreCase("OTHER TREATMENT OUTCOME")) {
                            treatment_outcome.getRadioGroup().getButtons().get(17).setChecked(true);
                        }
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

    public void updateFollowUpMonth() {

        String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-" + "Treatment Initiation", "REGISTRATION DATE");

        if (treatmentDate == null) {
            treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-" + "Treatment Initiation", "REGISTRATION DATE");
            if (treatmentDate == null)
                treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "PET-" + "Treatment Initiation", "TREATMENT START DATE");
        }

        String format = "";
        String[] monthArray;

        if (treatmentDate == null) {
            monthArray = new String[1];
            monthArray[0] = "0";
            followup_month.getEditText().setText("" + monthArray[0]);
        } else {
            if (treatmentDate.contains("/")) {
                format = "dd/MM/yyyy";
            } else {
                format = "yyyy-MM-dd";
            }
            Date convertedDate = App.stringToDate(treatmentDate, format);
            Calendar treatmentDateCalender = App.getCalendar(convertedDate);
            int diffYear = formDateCalendar.get(Calendar.YEAR) - treatmentDateCalender.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + formDateCalendar.get(Calendar.MONTH) - treatmentDateCalender.get(Calendar.MONTH);
            followup_month.getEditText().setText("" + diffMonth);


        }
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {


        if (radioGroup == followup_visit_type.getRadioGroup()) {
            followup_reason_other.setVisibility(View.GONE);
            followup_month.setVisibility(View.GONE);
            treatment_outcome.setVisibility(View.GONE);
            tb_treatment_phase.setVisibility(View.GONE);

            if (followup_visit_type.getRadioGroup().getSelectedValue().equals(getString(R.string.common_followup_visit_type_other))) {
                followup_reason_other.setVisibility(View.VISIBLE);
            } else if (followup_visit_type.getRadioGroup().getSelectedValue().equals(getString(R.string.common_followup_visit_type_planned))) {
                tb_treatment_phase.setVisibility(View.VISIBLE);
                followup_month.setVisibility(View.VISIBLE);
            } else if (followup_visit_type.getRadioGroup().getSelectedValue().equals(getString(R.string.common_followup_visit_type_end))) {
                treatment_outcome.setVisibility(View.VISIBLE);
            }
        }

        if (radioGroup == counselling.getRadioGroup()) {
            counselling_other.setVisibility(View.GONE);

            if (counselling.getRadioGroup().getSelectedValue().equals(getString(R.string.common_counselling_other))) {
                counselling_other.setVisibility(View.VISIBLE);
            }
        }

        if (radioGroup == reason_followup_counseling.getRadioGroup()) {
            referral_complaint_by_field_team.setVisibility(View.GONE);

            if (reason_followup_counseling.getRadioGroup().getSelectedValue().equals(getString(R.string.common_reason_followup_counseling_patient_refused))) {
                referral_complaint_by_field_team.setVisibility(View.VISIBLE);
            }
        }

        if (radioGroup == adverse_event_last_visit.getRadioGroup()) {
            adverse_events.setVisibility(View.GONE);

            if (adverse_event_last_visit.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                adverse_events.setVisibility(View.VISIBLE);
                for (CheckBox cb : adverse_events.getCheckedBoxes()) {
                    cb.setChecked(false);
                }
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
