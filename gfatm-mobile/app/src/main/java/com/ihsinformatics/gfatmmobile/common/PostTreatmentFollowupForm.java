package com.ihsinformatics.gfatmmobile.common;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class PostTreatmentFollowupForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    private Context context;

    protected Calendar thirdDateCalendar;
    protected Calendar fourthDateCalendar;


    TitledRadioGroup post_followup_type;
    TitledEditText treatment_outcome;
    TitledButton date_treatment_outcome;
    TitledRadioGroup tpt_refusal_reason;
    TitledEditText other_tpt_refusal_reason;
    TitledRadioGroup post_treatment_outcome;
    TitledEditText other_post_tx_outcome;
    TitledButton date_death;
    TitledRadioGroup primary_cause_death;
    TitledEditText cause_death_surgery;
    TitledEditText cause_death_other;
    TitledRadioGroup reason_lost_followup;
    TitledRadioGroup tb_infection_type;
    TitledButton tb_treatment_date;
    TitledRadioGroup cough;
    TitledRadioGroup cough_duration;
    TitledRadioGroup haemoptysis;

    TitledRadioGroup breathing_difficulty;
    TitledRadioGroup fever;
    TitledRadioGroup fever_duration;
    TitledRadioGroup weight_loss;
    TitledRadioGroup night_sweats;
    TitledRadioGroup lethargy;
    TitledRadioGroup joint_swelling;
    TitledRadioGroup back_pain;
    TitledRadioGroup vomiting;
    TitledRadioGroup gi_symptoms;
    TitledRadioGroup interest_loss;

    TitledEditText post_followup_notes;

    String treatmentOutcome;
    String dateTreatmentOutcome;


    @Override
    public void initViews() {


        thirdDateCalendar = Calendar.getInstance();
        fourthDateCalendar = Calendar.getInstance();

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);


        post_followup_type = new TitledRadioGroup(context, null, getResources().getString(R.string.common_followup_type), getResources().getStringArray(R.array.common_followup_type_options), "", App.VERTICAL, App.VERTICAL, true, "TYPE OF POST FOLLOW UP", new String[]{"TPT", "TB TREATMENT DS", "TB TREATMENT DR", "PREVIOUSLY REFUSAL FOR TPT"});
        treatment_outcome = new TitledEditText(context, null, getResources().getString(R.string.common_post_treatment_outcome), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "TREATMENT OUTCOME");
        date_treatment_outcome = new TitledButton(context, null, getResources().getString(R.string.common_date_treatment_outcome),DateFormat.format("EEEE, MMM dd,yyyy", fourthDateCalendar).toString(), App.HORIZONTAL);
        tpt_refusal_reason = new TitledRadioGroup(context, null, getResources().getString(R.string.common_tpt_refusal_reason), getResources().getStringArray(R.array.common_tpt_refusal_reason_options), "", App.VERTICAL, App.VERTICAL, true, "TPT REFUSAL REASON", new String[]{"ADVERSE EVENT TO OTHER HH MEMBERS", "OTHER COMORBIDITIES", "INDEX PATIENT REFUSED TREATMENT", "HEAD OF FAMILY REFUSED TPT", "IN GOOD HEALTH", "NOT NEEDING TREATMENT", "INDEX PATIENT DIED", "UNKNOWN", "OTHER REFUSAL REASON"});
        other_tpt_refusal_reason = new TitledEditText(context, null, getResources().getString(R.string.common_other_tpt_refusal_reason), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFUSAL REASON");
        post_treatment_outcome = new TitledRadioGroup(context, null, getResources().getString(R.string.common_post_treatment_outcome_post), getResources().getStringArray(R.array.common_post_treatment_outcome_options), "", App.VERTICAL, App.VERTICAL, true, "POST TREATMENT OUTCOME", new String[]{"NO CHANGE IN OUTCOME SINCE END OF TREATMENT", "DIED POST TREATMENT", "RELAPSE OR RECURRENCE", "LOST TO FOLLOW-UP POST TREATMENT", "NOT EVALUATED", "CONTACT DIAGNOSED WITH TB", "OTHER POST TREATMENT OUTCOME"});
        other_post_tx_outcome = new TitledEditText(context, null, getResources().getString(R.string.common_other_post_tx_outcome), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER POST TREATMENT OUTCOME");
        date_death = new TitledButton(context, null, getResources().getString(R.string.common_date_death),DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);
        primary_cause_death = new TitledRadioGroup(context, null, getResources().getString(R.string.common_primary_cause_death), getResources().getStringArray(R.array.common_primary_cause_death_options), "", App.VERTICAL, App.VERTICAL, true, "CAUSE OF DEATH", new String[]{"TB IMMEDIATE CAUSE OF DEATH", "CAUSE RELATED TO TB TREATMENT", "TB CONTRIBUTING TO DEATH", "SURGERY RELATED DEATH", "CAUSE OTHER THAN TB", "UNKNOWN"});
        cause_death_surgery = new TitledEditText(context, null, getResources().getString(R.string.common_cause_death_surgery), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "DEATH AFTER SURGERY");
        cause_death_other = new TitledEditText(context, null, getResources().getString(R.string.common_cause_death_other), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "DEATH AFTER SURGERY");
        reason_lost_followup = new TitledRadioGroup(context, null, getResources().getString(R.string.common_reason_lost_followup), getResources().getStringArray(R.array.common_reason_lost_followup_options), "", App.VERTICAL, App.VERTICAL, true, "REASON FOR LOST TO FOLLOW UP", new String[]{"PATIENT REFUSED FOLLOW-UP", "SUBSTANCE ABUSE", "SOCIAL PROBLEM", "LEFT REGION/COUNTRY", "PATIENT UNWILLING TO VISIT", "CONTACT NOT ESTABLISHED", "OTHER REASON TO END FOLLOW UP", "UNKNOWN"});
        tb_infection_type = new TitledRadioGroup(context, null, getResources().getString(R.string.common_tb_infection_type_post), getResources().getStringArray(R.array.common_tb_infection_type_post_options), "", App.VERTICAL, App.VERTICAL, true, "TUBERCULOSIS INFECTION TYPE", new String[]{"DRUG-SUSCEPTIBLE TB", "DRUG-RESISTANT TB"});
        tb_treatment_date = new TitledButton(context, null, getResources().getString(R.string.common_tb_treatment_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);

        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.common_cough), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL, true, "COUGH", new String[]{"YES", "NO"});
        cough_duration = new TitledRadioGroup(context, null, getResources().getString(R.string.common_cough_duration), getResources().getStringArray(R.array.common_cough_duration_options), "", App.VERTICAL, App.VERTICAL, true, "COUGH DURATION", new String[]{"COUGH LASTING LESS THAN 2 WEEKS", "COUGH LASTING MORE THAN 2 WEEKS", "COUGH LASTING MORE THAN 3 WEEKS", "UNKNOWN", "REFUSED"});
        haemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.common_haemoptysis), getResources().getStringArray(R.array.yes_no_unknown_refused_options), "", App.VERTICAL, App.VERTICAL, true, "HEMOPTYSIS", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        breathing_difficulty = new TitledRadioGroup(context, null, getResources().getString(R.string.common_breathing_difficulty), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL, true, "DYSPNEA", new String[]{"YES", "NO"});
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.common_fever), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL, true, "FEVER", new String[]{"YES", "NO"});
        fever_duration = new TitledRadioGroup(context, null, getResources().getString(R.string.common_fever_duration), getResources().getStringArray(R.array.common_cough_duration_options), "", App.VERTICAL, App.VERTICAL, true, "FEVER DURATION", new String[]{"FEVER LASTING LESS THAN TWO WEEKS", "FEVER LASTING MORE THAN TWO WEEKS", "FEVER LASTING MORE THAN THREE WEEKS", "UNKNOWN", "REFUSED"});
        weight_loss = new TitledRadioGroup(context, null, getResources().getString(R.string.common_weight_loss), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL, true, "WEIGHT LOSS", new String[]{"YES", "NO"});
        night_sweats = new TitledRadioGroup(context, null, getResources().getString(R.string.common_night_sweats), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL, true, "NIGHT SWEATS", new String[]{"YES", "NO"});
        lethargy = new TitledRadioGroup(context, null, getResources().getString(R.string.common_lethargy), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL, true, "LETHARGY", new String[]{"YES", "NO"});
        joint_swelling = new TitledRadioGroup(context, null, getResources().getString(R.string.common_joint_swelling), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL, true, "JOINT SWELLING", new String[]{"YES", "NO"});
        back_pain = new TitledRadioGroup(context, null, getResources().getString(R.string.common_back_pain), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL, true, "BACK PAIN", new String[]{"YES", "NO"});
        vomiting = new TitledRadioGroup(context, null, getResources().getString(R.string.common_vomiting), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL, true, "VOMITING", new String[]{"YES", "NO"});
        gi_symptoms = new TitledRadioGroup(context, null, getResources().getString(R.string.common_gi_symptoms), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL, true, "GASTROINTESTINAL SYMPTOM", new String[]{"YES", "NO"});
        interest_loss = new TitledRadioGroup(context, null, getResources().getString(R.string.common_interest_loss), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL, true, "LOSS OF INTEREST", new String[]{"YES", "NO"});

        post_followup_notes = new TitledEditText(context, null, getResources().getString(R.string.ctscan_notes), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "POST FOLLOWUP NOTES");


        views = new View[]{formDate.getButton(), post_followup_type.getRadioGroup(), treatment_outcome.getEditText(), date_treatment_outcome.getButton(),
                tpt_refusal_reason.getRadioGroup(), other_tpt_refusal_reason.getEditText(), post_treatment_outcome.getRadioGroup(), other_post_tx_outcome.getEditText(),
                date_death.getButton(), primary_cause_death.getRadioGroup(), cause_death_surgery.getEditText(), cause_death_other.getEditText(), reason_lost_followup.getRadioGroup(),
                tb_infection_type.getRadioGroup(), tb_treatment_date.getButton(), cough.getRadioGroup(), cough_duration.getRadioGroup(), haemoptysis.getRadioGroup(), breathing_difficulty.getRadioGroup(),
                fever.getRadioGroup(), fever_duration.getRadioGroup(), weight_loss.getRadioGroup(), night_sweats.getRadioGroup(), lethargy.getRadioGroup(), joint_swelling.getRadioGroup(), back_pain.getRadioGroup(),
                vomiting.getRadioGroup(), gi_symptoms.getRadioGroup(), interest_loss.getRadioGroup(), post_followup_notes.getEditText()
        };

        viewGroups = new View[][]
                {{formDate, post_followup_type, treatment_outcome, date_treatment_outcome, tpt_refusal_reason, other_tpt_refusal_reason, post_treatment_outcome,
                        other_post_tx_outcome, date_death, primary_cause_death, cause_death_surgery, cause_death_other, reason_lost_followup, tb_infection_type,
                        tb_treatment_date, cough, cough_duration, haemoptysis, breathing_difficulty, fever, fever_duration, weight_loss, night_sweats,
                        lethargy, joint_swelling, back_pain, vomiting, gi_symptoms, interest_loss, post_followup_notes
                }};

        post_followup_type.getRadioGroup().setOnCheckedChangeListener(this);
        tpt_refusal_reason.getRadioGroup().setOnCheckedChangeListener(this);
        post_treatment_outcome.getRadioGroup().setOnCheckedChangeListener(this);
        primary_cause_death.getRadioGroup().setOnCheckedChangeListener(this);
        cough.getRadioGroup().setOnCheckedChangeListener(this);
        fever.getRadioGroup().setOnCheckedChangeListener(this);


        formDate.getButton().setOnClickListener(this);
        tb_treatment_date.getButton().setOnClickListener(this);
        date_death.getButton().setOnClickListener(this);
        //date_treatment_outcome.getButton().setOnClickListener(this);

        resetViews();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == post_followup_type.getRadioGroup()) {
            if (!post_followup_type.getRadioGroup().getSelectedValue().equals(getString(R.string.common_followup_type_previous))) {
                treatment_outcome.setVisibility(View.VISIBLE);
                date_treatment_outcome.setVisibility(View.VISIBLE);
                tpt_refusal_reason.setVisibility(View.GONE);
            } else {
                treatment_outcome.setVisibility(View.GONE);
                date_treatment_outcome.setVisibility(View.GONE);
                tpt_refusal_reason.setVisibility(View.VISIBLE);
            }
        }
        else if (radioGroup == tpt_refusal_reason.getRadioGroup()) {
            if (tpt_refusal_reason.getRadioGroup().getSelectedValue().equals(getString(R.string.common_tpt_refusal_reason_other))) {
                other_tpt_refusal_reason.setVisibility(View.VISIBLE);
            }else{
                other_tpt_refusal_reason.setVisibility(View.GONE);
            }
        }
        else if (radioGroup == post_treatment_outcome.getRadioGroup()) {
            if (post_treatment_outcome.getRadioGroup().getSelectedValue().equals(getString(R.string.common_post_treatment_outcome_other))) {
                other_post_tx_outcome.setVisibility(View.VISIBLE);
            }else{
                other_post_tx_outcome.setVisibility(View.GONE);
            }

            if (post_treatment_outcome.getRadioGroup().getSelectedValue().equals(getString(R.string.common_post_treatment_outcome_died))) {
                date_death.setVisibility(View.VISIBLE);
                primary_cause_death.setVisibility(View.VISIBLE);
                cough.setVisibility(View.GONE);
                cough_duration.setVisibility(View.GONE);
                haemoptysis.setVisibility(View.GONE);
                breathing_difficulty.setVisibility(View.GONE);
                fever.setVisibility(View.GONE);
                fever_duration.setVisibility(View.GONE);
                weight_loss.setVisibility(View.GONE);
                night_sweats.setVisibility(View.GONE);
                lethargy.setVisibility(View.GONE);
                joint_swelling.setVisibility(View.GONE);
                back_pain.setVisibility(View.GONE);
                vomiting.setVisibility(View.GONE);
                gi_symptoms.setVisibility(View.GONE);
                interest_loss.setVisibility(View.GONE);
            }else{
                date_death.setVisibility(View.GONE);
                primary_cause_death.setVisibility(View.GONE);
                cough.setVisibility(View.VISIBLE);
                cough_duration.setVisibility(View.VISIBLE);
                haemoptysis.setVisibility(View.VISIBLE);
                breathing_difficulty.setVisibility(View.VISIBLE);
                fever.setVisibility(View.VISIBLE);
                fever_duration.setVisibility(View.VISIBLE);
                weight_loss.setVisibility(View.VISIBLE);
                night_sweats.setVisibility(View.VISIBLE);
                lethargy.setVisibility(View.VISIBLE);
                joint_swelling.setVisibility(View.VISIBLE);
                back_pain.setVisibility(View.VISIBLE);
                vomiting.setVisibility(View.VISIBLE);
                gi_symptoms.setVisibility(View.VISIBLE);
                interest_loss.setVisibility(View.VISIBLE);
            }

            if (post_treatment_outcome.getRadioGroup().getSelectedValue().equals(getString(R.string.common_post_treatment_outcome_relapse))) {
                tb_infection_type.setVisibility(View.VISIBLE);
                tb_treatment_date.setVisibility(View.VISIBLE);
            }else{
                tb_infection_type.setVisibility(View.GONE);
                tb_treatment_date.setVisibility(View.GONE);
            }
        }
        else if (radioGroup == primary_cause_death.getRadioGroup()) {
            if (primary_cause_death.getRadioGroup().getSelectedValue().equals(getString(R.string.common_primary_cause_death_surgery))) {
                cause_death_surgery.setVisibility(View.VISIBLE);
            }else{
                cause_death_surgery.setVisibility(View.GONE);
            }
            if (primary_cause_death.getRadioGroup().getSelectedValue().equals(getString(R.string.common_primary_cause_death_cause))) {
                cause_death_other.setVisibility(View.VISIBLE);
            }else{
                cause_death_other.setVisibility(View.GONE);
            }
        }
        else if (radioGroup == cough.getRadioGroup()) {
            if (cough.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                cough_duration.setVisibility(View.VISIBLE);
                haemoptysis.setVisibility(View.VISIBLE);
            }else{
                cough_duration.setVisibility(View.GONE);
                haemoptysis.setVisibility(View.GONE);
            }

        }
        else if (radioGroup == fever.getRadioGroup()) {
            if (fever.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                fever_duration.setVisibility(View.VISIBLE);
            }else{
                fever_duration.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void resetViews() {
        super.resetViews();

        //Post Treatment Outcome
        //String value = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Order", "TREATMENT OUTCOME", App.get(orderIds), "TYPE OF X RAY");


        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        //date_treatment_outcome.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", fourthDateCalendar).toString());
        date_treatment_outcome.getButton().setEnabled(false);
        treatment_outcome.getEditText().setEnabled(false);
        date_death.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
        tb_treatment_date.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        treatment_outcome.setVisibility(View.GONE);
        date_treatment_outcome.setVisibility(View.GONE);
        tpt_refusal_reason.setVisibility(View.GONE);
        other_post_tx_outcome.setVisibility(View.GONE);
        other_tpt_refusal_reason.setVisibility(View.GONE);
        date_death.setVisibility(View.GONE);
        primary_cause_death.setVisibility(View.GONE);
        cause_death_surgery.setVisibility(View.GONE);
        cause_death_other.setVisibility(View.GONE);
        //reason_lost_followup.setVisibility(View.GONE);
        tb_infection_type.setVisibility(View.GONE);
        tb_treatment_date.setVisibility(View.GONE);
        cough.setVisibility(View.GONE);
        cough_duration.setVisibility(View.GONE);
        haemoptysis.setVisibility(View.GONE);
        breathing_difficulty.setVisibility(View.GONE);
        fever.setVisibility(View.GONE);
        fever_duration.setVisibility(View.GONE);
        weight_loss.setVisibility(View.GONE);
        night_sweats.setVisibility(View.GONE);
        lethargy.setVisibility(View.GONE);
        joint_swelling.setVisibility(View.GONE);
        back_pain.setVisibility(View.GONE);
        vomiting.setVisibility(View.GONE);
        gi_symptoms.setVisibility(View.GONE);
        interest_loss.setVisibility(View.GONE);
        cough_duration.setVisibility(View.GONE);
        fever_duration.setVisibility(View.GONE);
        /*if(treatment_outcome.getEditText().getValue().equalsIgnoreCase("Lost to follow-up post treatment")){
            reason_lost_followup.setVisibility(View.VISIBLE);
        }else{
            reason_lost_followup.setVisibility(View.GONE);
        }*/

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
                treatmentOutcome = serverService.getLatestObsValue(App.getPatientId(), "TREATMENT OUTCOME");

                if (treatmentOutcome != null && !treatmentOutcome.equals("")) {
                    serverService.getPatient(treatmentOutcome, false);
                    result.put("treatmentOutcome", treatmentOutcome);
                } else result.put("treatmentOutcome", "");

                dateTreatmentOutcome = serverService.getLatestEncounterDateTime(App.getPatientId(), "End of Followup");

                if (dateTreatmentOutcome != null && !dateTreatmentOutcome.equals("")) {
                    serverService.getPatient(dateTreatmentOutcome, false);
                    result.put("dateTreatmentOutcome", dateTreatmentOutcome);
                } else result.put("dateTreatmentOutcome", "");

                return result;

            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            ;

            @Override
            protected void onPostExecute(HashMap<String, String> result) {
                super.onPostExecute(result);
                loading.dismiss();

                treatment_outcome.getEditText().setText(result.get("treatmentOutcome"));
                if(result.get("treatmentOutcome").equals("LOST TO FOLLOW-UP")){
                    reason_lost_followup.setVisibility(View.VISIBLE);
                }else{
                    reason_lost_followup.setVisibility(View.GONE);
                }

                if(!(result.get("dateTreatmentOutcome").isEmpty())){
                    Calendar dateTreatmentOutcomeCalendar = App.getCalendar(App.stringToDate(result.get("dateTreatmentOutcome"), "yyyy-MM-dd"));
                    date_treatment_outcome.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", dateTreatmentOutcomeCalendar).toString());
                }else{
                    date_treatment_outcome.getButton().setText("");
                }
            }
        };
        autopopulateFormTask.execute("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pageCount = 1;
        formName = Forms.POST_TREATMENT_FOLLOWUP_FORM;
        form = Forms.postTreatmentFollowupForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new PostTreatmentFollowupForm.MyAdapter());
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

    @Override
    public boolean validate() {

        if(treatmentOutcome == null || treatmentOutcome.isEmpty() || dateTreatmentOutcome == null || dateTreatmentOutcome.isEmpty()){
            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);
            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
            alertDialog.setMessage("Please submit End of Followup form first.");
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            DrawableCompat.setTint(clearIcon, color);
            alertDialog.setIcon(clearIcon);
            alertDialog.setTitle(getResources().getString(R.string.title_alert));
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

        Boolean error = super.validate();

        if(error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
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
    public void updateDisplay() {

        String personDOB = App.getPatient().getPerson().getBirthdate();
        Calendar maxDateCalender = formDateCalendar.getInstance();
        maxDateCalender.setTime(formDateCalendar.getTime());
        maxDateCalender.add(Calendar.YEAR, 2);
        String formDa = formDate.getButton().getText().toString();

        if (snackbar != null)
            snackbar.dismiss();
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
            Calendar requiredDate = formDateCalendar.getInstance();
            requiredDate.setTime(formDateCalendar.getTime());
            requiredDate.add(Calendar.DATE, 30);
        }

        String nextAppointmentDateString = App.getSqlDate(secondDateCalendar);
        Date nextAppointmentDate = App.stringToDate(nextAppointmentDateString, "yyyy-MM-dd");

        String formDateString = App.getSqlDate(formDateCalendar);
        Date formStDate = App.stringToDate(formDateString, "yyyy-MM-dd");

        date_death.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());

        //date_treatment_outcome.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", fourthDateCalendar).toString());

        tb_treatment_date.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        formDate.getButton().setEnabled(true);
        tb_treatment_date.getButton().setEnabled(true);
        date_death.getButton().setEnabled(true);
        //date_treatment_outcome.getButton().setEnabled(true);



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


        if (tb_treatment_date.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"TB TREATMENT DATE", App.getSqlDateTime(secondDateCalendar)});
        }

        if (date_death.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"DATE OF DEATH", App.getSqlDateTime(thirdDateCalendar)});
        }

        if (date_treatment_outcome.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"OUTCOME DATE, TUBERCULOSIS TREATMENT", App.getSqlDateTime(fourthDateCalendar)});
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

                String id = null;
                if (App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocally("Post Treatment Outcome", form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = "";


                result = serverService.saveEncounterAndObservationTesting("Post Treatment Outcome", form, formDateCalendar, observations.toArray(new String[][]{}), id);
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
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar, false, true, false);

        }
        /*if (view == date_treatment_outcome.getButton()) {
            date_treatment_outcome.getButton().setEnabled(false);
            showDateDialog(fourthDateCalendar, false, true, false);

        }*/
        if (view == date_death.getButton()) {
            date_death.getButton().setEnabled(false);
            showDateDialog(thirdDateCalendar, false, true, false);
        }
        if (view == tb_treatment_date.getButton()) {
            tb_treatment_date.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar, false, true, false);
        }
    }

    @Override
    public boolean save() {
        return false;
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    private class MyAdapter extends PagerAdapter {
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
