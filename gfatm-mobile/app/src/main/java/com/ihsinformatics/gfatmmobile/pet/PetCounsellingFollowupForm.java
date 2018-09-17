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
import android.text.InputType;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class PetCounsellingFollowupForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledEditText followupMonth;
    TitledEditText referralComplain;
    TitledEditText missedDosage;
    TitledRadioGroup adherentToPet;
    TitledEditText reasonForNonAdherent;

    TitledRadioGroup adverseEventReport;
    LinearLayout adverseEffectsLayout;
    TitledCheckBoxes adverseEffects1;
    TitledCheckBoxes adverseEffects2;
    TitledEditText otherEffects;

    TitledSpinner treatmentSuppoterRelation;
    TitledEditText otherTreatmentSuppoterRelation;
    TitledRadioGroup behavioralComplaint;
    TitledSpinner behaviouralComplaintType;
    TitledEditText other;

    TitledRadioGroup treatmentSupportNegligence;
    TitledEditText treatmentSupportNegligenceReason;
    TitledRadioGroup misconceptionInPet;
    TitledEditText misconception;
    TitledRadioGroup infectionControllFollowing;
    TitledRadioGroup infectionControlCounselling;
    TitledCheckBoxes patientFacingProblem;
    TitledEditText otherProblem;

    TitledEditText contactComments;
    TitledEditText psychologistComments;

    TitledRadioGroup followupRequired;
    TitledButton returnVisitDate;

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
        formName = Forms.PET_COUNSELLING_FOLLOWUP;
        form = Forms.pet_counsellingFollowup;

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

    /**
     * Initializes all views and ArrayList and Views Array
     */
    public void initViews() {

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        followupMonth = new TitledEditText(context, null, getResources().getString(R.string.pet_followup_month), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        referralComplain = new TitledEditText(context, null, getResources().getString(R.string.pet_referral_complain), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        referralComplain.getEditText().setSingleLine(false);
        referralComplain.getEditText().setMinimumHeight(150);
        missedDosage = new TitledEditText(context, null, getResources().getString(R.string.pet_missed_dosed), "0", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);
        adherentToPet = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adherent), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.HORIZONTAL);
        reasonForNonAdherent = new TitledEditText(context, null, getResources().getString(R.string.pet_reason_for_non_adherent), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        adverseEventReport = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adverse_event_report), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        adverseEffectsLayout = new LinearLayout(context);
        adverseEffectsLayout.setOrientation(LinearLayout.HORIZONTAL);
        adverseEffects1 = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_adverse_effects), getResources().getStringArray(R.array.pet_adverse_effects_1), null, App.VERTICAL, App.VERTICAL, true);
        adverseEffectsLayout.addView(adverseEffects1);
        adverseEffects2 = new TitledCheckBoxes(context, null, "", getResources().getStringArray(R.array.pet_adverse_effects_2), null, App.VERTICAL, App.VERTICAL);
        adverseEffectsLayout.addView(adverseEffects2);
        otherEffects = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        treatmentSuppoterRelation = new TitledSpinner(context, "", getResources().getString(R.string.pet_treatment_support), getResources().getStringArray(R.array.pet_cnic_owners), "", App.VERTICAL);
        otherTreatmentSuppoterRelation = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        behavioralComplaint = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_behavioural_complain), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        behaviouralComplaintType = new TitledSpinner(context, "", getResources().getString(R.string.pet_behavioural_complain), getResources().getStringArray(R.array.pet_contact_behaviours), "", App.VERTICAL);
        other = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 250, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        treatmentSupportNegligence = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_caretaker_negligence), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        treatmentSupportNegligenceReason = new TitledEditText(context, null, getResources().getString(R.string.pet_reason_caretaker_negligence), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        misconceptionInPet = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_misconcepions_after_pet_initiation), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        misconception = new TitledEditText(context, null, getResources().getString(R.string.pet_misconception), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        infectionControllFollowing = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_infection_control_following), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        infectionControlCounselling = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_infection_control_counselling), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        patientFacingProblem = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_patient_problem), getResources().getStringArray(R.array.pet_patient_problem), null, App.VERTICAL, App.VERTICAL);
        otherProblem = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        contactComments = new TitledEditText(context, null, getResources().getString(R.string.pet_contact_comments), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        contactComments.getEditText().setSingleLine(false);
        contactComments.getEditText().setMinimumHeight(150);
        psychologistComments = new TitledEditText(context, null, getResources().getString(R.string.pet_comment_psychologist), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        psychologistComments.getEditText().setSingleLine(false);
        psychologistComments.getEditText().setMinimumHeight(150);

        followupRequired = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_followup_required), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL, true);
        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.pet_return_visit_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.VERTICAL);

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
        views = new View[]{formDate.getButton(), followupMonth.getEditText(), missedDosage.getEditText(), adherentToPet.getRadioGroup(), reasonForNonAdherent.getEditText(),
                adverseEventReport.getRadioGroup(), adverseEffects1, adverseEffects2, otherEffects.getEditText(), treatmentSuppoterRelation.getSpinner(),
                treatmentSuppoterRelation.getSpinner(), behaviouralComplaintType.getSpinner(), other.getEditText(), treatmentSupportNegligence.getRadioGroup(),
                treatmentSupportNegligenceReason.getEditText(), misconceptionInPet.getRadioGroup(), misconception.getEditText(),
                infectionControllFollowing.getRadioGroup(), infectionControlCounselling.getRadioGroup(), patientFacingProblem, otherProblem.getEditText(),
                contactComments.getEditText(), psychologistComments.getEditText(), behavioralComplaint.getRadioGroup(), returnVisitDate.getButton(),
                patientReferred.getRadioGroup(), referredTo, referalReasonPsychologist, otherReferalReasonPsychologist.getEditText(), referalReasonSupervisor, otherReferalReasonSupervisor.getEditText(),
                referalReasonCallCenter, otherReferalReasonCallCenter.getEditText(), referalReasonClinician, otherReferalReasonClinician.getEditText(), followupRequired.getRadioGroup()
        };

        // Array used to display views accordingly...
        viewGroups = new View[][]{{formDate, followupMonth, referralComplain, missedDosage, adherentToPet, reasonForNonAdherent, adverseEventReport, adverseEffectsLayout, otherEffects,
                treatmentSuppoterRelation, behavioralComplaint, behaviouralComplaintType, other, treatmentSupportNegligence, treatmentSupportNegligenceReason, misconceptionInPet, misconception,
                infectionControllFollowing, infectionControlCounselling, patientFacingProblem, otherProblem, contactComments, psychologistComments, followupRequired, returnVisitDate,
                patientReferred, referredTo, referalReasonPsychologist, otherReferalReasonPsychologist, referalReasonSupervisor, otherReferalReasonSupervisor,
                referalReasonCallCenter, otherReferalReasonCallCenter, referalReasonClinician, otherReferalReasonClinician}
        };

        formDate.getButton().setOnClickListener(this);
        adherentToPet.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentSupportNegligence.getRadioGroup().setOnCheckedChangeListener(this);
        infectionControllFollowing.getRadioGroup().setOnCheckedChangeListener(this);
        misconceptionInPet.getRadioGroup().setOnCheckedChangeListener(this);
        adverseEventReport.getRadioGroup().setOnCheckedChangeListener(this);
        behavioralComplaint.getRadioGroup().setOnCheckedChangeListener(this);
        behaviouralComplaintType.getSpinner().setOnItemSelectedListener(this);
        treatmentSuppoterRelation.getSpinner().setOnItemSelectedListener(this);
        returnVisitDate.getButton().setOnClickListener(this);
        for (CheckBox cb : adverseEffects2.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        for (CheckBox cb : patientFacingProblem.getCheckedBoxes()) {
            cb.setOnCheckedChangeListener(this);
        }

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
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        reasonForNonAdherent.setVisibility(View.GONE);
        adverseEffectsLayout.setVisibility(View.GONE);
        otherEffects.setVisibility(View.GONE);
        behaviouralComplaintType.setVisibility(View.GONE);
        other.setVisibility(View.GONE);
        treatmentSupportNegligenceReason.setVisibility(View.GONE);
        misconception.setVisibility(View.GONE);
        infectionControlCounselling.setVisibility(View.GONE);
        misconception.setVisibility(View.GONE);
        otherTreatmentSuppoterRelation.setVisibility(View.GONE);
        otherProblem.setVisibility(View.GONE);
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
    public void updateDisplay() {

        if(refillFlag){
            refillFlag = false;
            return;
        }

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setEnabled(true);
        returnVisitDate.getButton().setEnabled(true);

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

            if(formDateCalendar.after(secondDateCalendar)){

                secondDateCalendar.set(formDateCalendar.get(Calendar.YEAR), formDateCalendar.get(Calendar.MONTH), formDateCalendar.get(Calendar.DAY_OF_MONTH));
                secondDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }

        }


        if (!(returnVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = returnVisitDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }

            else
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }
    }

    @Override
    public boolean validate() {

        Boolean error = false;

        View view = null;

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

        if (App.get(otherProblem).isEmpty() && otherProblem.getVisibility() == View.VISIBLE) {
            otherProblem.getEditText().setError(getString(R.string.empty_field));
            otherProblem.getEditText().requestFocus();
            error = true;
        } else
            otherProblem.clearFocus();

        if (App.get(misconception).isEmpty() && misconception.getVisibility() == View.VISIBLE) {
            misconception.getEditText().setError(getString(R.string.empty_field));
            misconception.getEditText().requestFocus();
            error = true;
        } else
            misconception.clearFocus();

        if (App.get(treatmentSupportNegligenceReason).isEmpty() && treatmentSupportNegligenceReason.getVisibility() == View.VISIBLE) {
            treatmentSupportNegligenceReason.getEditText().setError(getString(R.string.empty_field));
            treatmentSupportNegligenceReason.getEditText().requestFocus();
            error = true;
        } else
            treatmentSupportNegligenceReason.clearFocus();

        if (App.get(other).isEmpty() && other.getVisibility() == View.VISIBLE) {
            other.getEditText().setError(getString(R.string.empty_field));
            other.getEditText().requestFocus();
            error = true;
        } else
            other.clearFocus();

        if (App.get(otherTreatmentSuppoterRelation).isEmpty() && otherTreatmentSuppoterRelation.getVisibility() == View.VISIBLE) {
            otherTreatmentSuppoterRelation.getEditText().setError(getString(R.string.empty_field));
            otherTreatmentSuppoterRelation.getEditText().requestFocus();
            error = true;
        } else
            otherTreatmentSuppoterRelation.clearFocus();

        if (App.get(otherEffects).isEmpty() && otherEffects.getVisibility() == View.VISIBLE) {
            otherEffects.getEditText().setError(getString(R.string.empty_field));
            otherEffects.getEditText().requestFocus();
            error = true;
        } else
            otherEffects.clearFocus();

        if (adverseEffectsLayout.getVisibility() == View.VISIBLE) {
            flag = false;
            for (CheckBox cb : adverseEffects1.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                for (CheckBox cb : adverseEffects2.getCheckedBoxes()) {
                    if (cb.isChecked()) {
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag) {
                adverseEffects1.getQuestionView().setError(getString(R.string.empty_field));
                adverseEffects1.getQuestionView().requestFocus();
                view = adverseEffects1;
                gotoLastPage();
                error = true;
            }
        }

        if (App.get(reasonForNonAdherent).isEmpty() && reasonForNonAdherent.getVisibility() == View.VISIBLE) {
            reasonForNonAdherent.getEditText().setError(getString(R.string.empty_field));
            reasonForNonAdherent.getEditText().requestFocus();
            error = true;
            view = null;
        } else
            reasonForNonAdherent.clearFocus();

        if (App.get(missedDosage).isEmpty() && missedDosage.getVisibility() == View.VISIBLE) {
            missedDosage.getEditText().setError(getString(R.string.empty_field));
            missedDosage.getEditText().requestFocus();
            error = true;
            view = null;
        } else
            missedDosage.clearFocus();

        if (App.get(followupMonth).isEmpty() && followupMonth.getVisibility() == View.VISIBLE) {
            followupMonth.getEditText().setError(getString(R.string.empty_field));
            followupMonth.getEditText().requestFocus();
            error = true;
            view = null;
        } else
            followupMonth.clearFocus();

        if (error) {

            // int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.form_error));
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
        observations.add(new String[]{"FOLLOW-UP MONTH", App.get(followupMonth)});
        observations.add(new String[]{"REFERRAL COMPLAINT", App.get(referralComplain)});
        observations.add(new String[]{"NUMBER OF MISSED MEDICATION DOSES IN LAST MONTH", App.get(missedDosage)});
        observations.add(new String[]{"PATIENT ADHERENT TO TRETMENT", App.get(adherentToPet).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (reasonForNonAdherent.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"NON-ADHERENCE REASON (TEXT)", App.get(reasonForNonAdherent)});
        observations.add(new String[]{"ADVERSE EVENTS REPORTED", App.get(adverseEventReport).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (adverseEffectsLayout.getVisibility() == View.VISIBLE) {
            String adverseEffects = "";
            for (CheckBox cb : adverseEffects1.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_joint_pain)))
                    adverseEffects = adverseEffects + "JOINT PAIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_headache)))
                    adverseEffects = adverseEffects + "HEADACHE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_skin_rash)))
                    adverseEffects = adverseEffects + "RASH" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_nausea)))
                    adverseEffects = adverseEffects + "NAUSEA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_dizziness)))
                    adverseEffects = adverseEffects + "DIZZINESS AND GIDDINESS" + " ; ";
            }
            for (CheckBox cb : adverseEffects2.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_vomiting)))
                    adverseEffects = adverseEffects + "VOMITING" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_abdominal_pain)))
                    adverseEffects = adverseEffects + "ABDOMINAL PAIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_loss_of_appetite)))
                    adverseEffects = adverseEffects + "LOSS OF APPETITE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_visual_impairment)))
                    adverseEffects = adverseEffects + "VISUAL IMPAIRMENT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other)))
                    adverseEffects = adverseEffects + "OTHER ADVERSE EVENT" + " ; ";
            }
            observations.add(new String[]{"ADVERSE EVENTS", adverseEffects});
        }
        if (otherEffects.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER ADVERSE EVENT", App.get(otherEffects)});
        observations.add(new String[]{"TREATMENT SUPPORTER RELATIONSHIP TO PATIENT", (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_mother))) ? "MOTHER" :
                (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_self)) ? "SELF" :
                    (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_father)) ? "FATHER" :
                        (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                        (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                        (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_brother)) ? "BROTHER" :
                                                                (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_sister)) ? "SISTER" :
                                                                        (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_son)) ? "SON" :
                                                                                (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_daughter)) ? "SPOUSE" :
                                                                                        (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_aunt)) ? "AUNT" :
                                                                                                (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_uncle)) ? "UNCLE" : "OTHER FAMILY MEMBER"))))))))))))});
        if(otherTreatmentSuppoterRelation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER TREATMENT SUPPORTER RELATIONSHIP TO PATIENT", App.get(otherTreatmentSuppoterRelation)});
        observations.add(new String[]{"BEHAVIORAL COMPLAINS AFTER TREATMENT", App.get(behavioralComplaint).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (behaviouralComplaintType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"BEHAVIOUR", App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_irritable)) ? "IRRITABILITY" :
                    (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_stubborn)) ? "STUBBORN BEHAVIOUR" :
                            (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_shy)) ? "INTROVERTED PERSONALITY" :
                                    (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_aggressive)) ? "AGGRESSIVE BEHAVIOUR" :
                                            (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_argumentative)) ? "ARGUMENTATIVE BEHAVIOUR" :
                                                    (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_non_complaint)) ? "NON COMPLAINT BEHAVIOUR" :
                                                            (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_responsible)) ? "RESPONSIBLE PERSONALITY" :
                                                                    (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_cooperative)) ? "COOPERATIVE BEHAVIOUR" :
                                                                            (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_noncooperative)) ? "NON-COOPERATIVE BEHAVIOUR" : "OTHER PERSONALITY TYPE"))))))))});
        if (other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER PERSONALITY TYPE", App.get(other)});
        observations.add(new String[]{"TREATMENT SUPPORTER NEGLIGENCE", App.get(treatmentSupportNegligence).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (treatmentSupportNegligenceReason.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT SUPPORTER NEGLIGENCE REASON", App.get(treatmentSupportNegligenceReason)});
        observations.add(new String[]{"TREATMENT MISCONCEPTIONS", App.get(misconceptionInPet).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (misconception.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT MISCONCEPTION (TEXT)", App.get(misconception)});
        observations.add(new String[]{"INFECTION CONTROL MEASURES FOLLOWED", App.get(infectionControllFollowing).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (infectionControlCounselling.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"INFECTION CONTROL COUNSELLING", App.get(infectionControlCounselling).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        String scoialProblem = "";
        for (CheckBox cb : patientFacingProblem.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_academic_problem)))
                scoialProblem = scoialProblem + "ACADEMIC PROBLEM" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_economic_problem)))
                scoialProblem = scoialProblem + "ECONOMIC PROBLEM" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_health_problem)))
                scoialProblem = scoialProblem + "PERSONAL BARRIER TO HEALTH CARE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_unknown)))
                scoialProblem = scoialProblem + "UNKNOWN" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.refused)))
                scoialProblem = scoialProblem + "REFUSED" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other)))
                scoialProblem = scoialProblem + "OTHER" + " ; ";
        }
        observations.add(new String[]{"SOCIAL PROBLEM", scoialProblem});
        if (otherProblem.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER", App.get(otherProblem)});
        observations.add(new String[]{"CARETAKER COMMENTS", App.get(contactComments)});
        observations.add(new String[]{"CLINICIAN NOTES (TEXT)", App.get(psychologistComments)});
        observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDate(secondDateCalendar)});

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

        HashMap<String, String> formValues = new HashMap<String, String>();

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));

        serverService.saveFormLocally(formName, form, "12345-5", formValues);

        return true;
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
            formDate.getButton().setEnabled(false);

        }else if (view == returnVisitDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowFutureDate", true);
            args.putBoolean("allowPastDate", false);
            args.putString("formDate", formDate.getButtonText());
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            returnVisitDate.getButton().setEnabled(false);
        }

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == behaviouralComplaintType.getSpinner()) {
            if (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_other)))
                other.setVisibility(View.VISIBLE);
            else
                other.setVisibility(View.GONE);
        } else if (spinner == treatmentSuppoterRelation.getSpinner()) {
            if (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_other)))
                otherTreatmentSuppoterRelation.setVisibility(View.VISIBLE);
            else
                otherTreatmentSuppoterRelation.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        boolean flag = false;
        for (CheckBox cb : adverseEffects2.getCheckedBoxes()) {
            if (cb.getText().equals(getString(R.string.pet_other)) && cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (flag)
            otherEffects.setVisibility(View.VISIBLE);
        else
            otherEffects.setVisibility(View.GONE);

        flag = false;
        for (CheckBox cb : patientFacingProblem.getCheckedBoxes()) {
            if (cb.getText().equals(getString(R.string.pet_other)) && cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (flag)
            otherProblem.setVisibility(View.VISIBLE);
        else
            otherProblem.setVisibility(View.GONE);

        setReferralViews();

    }

    @Override
    public void onPageSelected(int pageNo) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (group == adherentToPet.getRadioGroup()) {
            if (App.get(adherentToPet).equals(getResources().getString(R.string.no)))
                reasonForNonAdherent.setVisibility(View.VISIBLE);
            else
                reasonForNonAdherent.setVisibility(View.GONE);
        } else if (group == adverseEventReport.getRadioGroup()) {
            if (App.get(adverseEventReport).equals(getResources().getString(R.string.no))) {
                adverseEffectsLayout.setVisibility(View.GONE);
                otherEffects.setVisibility(View.GONE);
            } else {
                adverseEffectsLayout.setVisibility(View.VISIBLE);
                boolean flag = false;
                for (CheckBox cb : adverseEffects2.getCheckedBoxes()) {
                    if (cb.getText().equals(getString(R.string.pet_other)) && cb.isChecked()) {
                        flag = true;
                        break;
                    }
                }
                if (flag)
                    otherEffects.setVisibility(View.VISIBLE);
                else
                    otherEffects.setVisibility(View.GONE);
            }
        } else if (group == behavioralComplaint.getRadioGroup()) {
            if (App.get(behavioralComplaint).equals(getResources().getString(R.string.no))) {
                behaviouralComplaintType.setVisibility(View.GONE);
                other.setVisibility(View.GONE);
            } else {
                behaviouralComplaintType.setVisibility(View.VISIBLE);
                if (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_other)))
                    other.setVisibility(View.VISIBLE);
                else
                    other.setVisibility(View.GONE);
            }
        } else if (group == treatmentSupportNegligence.getRadioGroup()) {
            if (App.get(treatmentSupportNegligence).equals(getResources().getString(R.string.yes)))
                treatmentSupportNegligenceReason.setVisibility(View.VISIBLE);
            else
                treatmentSupportNegligenceReason.setVisibility(View.GONE);
        } else if (group == misconceptionInPet.getRadioGroup()) {
            if (App.get(misconceptionInPet).equals(getResources().getString(R.string.yes)))
                misconception.setVisibility(View.VISIBLE);
            else
                misconception.setVisibility(View.GONE);
        } else if (group == infectionControllFollowing.getRadioGroup()) {
            if (App.get(infectionControllFollowing).equals(getResources().getString(R.string.yes)))
                infectionControlCounselling.setVisibility(View.GONE);
            else
                infectionControlCounselling.setVisibility(View.VISIBLE);
        }  else if (group == patientReferred.getRadioGroup()) {
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

            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                followupMonth.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("REFERRAL COMPLAINT")) {
                referralComplain.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("NUMBER OF MISSED MEDICATION DOSES IN LAST MONTH")) {
                missedDosage.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("PATIENT ADHERENT TO TRETMENT")) {
                for (RadioButton rb : adherentToPet.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("NON-ADHERENCE REASON (TEXT)")) {
                reasonForNonAdherent.getEditText().setText(obs[0][1]);
                reasonForNonAdherent.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ADVERSE EVENTS REPORTED")) {
                for (RadioButton rb : adverseEventReport.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("ADVERSE EVENTS")) {
                for (CheckBox cb : adverseEffects1.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_joint_pain)) && obs[0][1].equals("JOINT PAIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_headache)) && obs[0][1].equals("HEADACHE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_skin_rash)) && obs[0][1].equals("RASH")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_nausea)) && obs[0][1].equals("NAUSEA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_dizziness)) && obs[0][1].equals("DIZZINESS AND GIDDINESS")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                for (CheckBox cb : adverseEffects2.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_vomiting)) && obs[0][1].equals("VOMITING")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_abdominal_pain)) && obs[0][1].equals("ABDOMINAL PAIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_loss_of_appetite)) && obs[0][1].equals("LOSS OF APPETITE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_visual_impairment)) && obs[0][1].equals("VISUAL IMPAIRMENT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_other)) && obs[0][1].equals("OTHER ADVERSE EVENT")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                adverseEffectsLayout.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER ADVERSE EVENT")) {
                otherEffects.getEditText().setText(obs[0][1]);
                otherEffects.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT")) {
                String value = obs[0][1].equals("SELF") ? getResources().getString(R.string.pet_self) :
                        (obs[0][1].equals("MOTHER") ? getResources().getString(R.string.pet_mother) :
                            (obs[0][1].equals("FATHER") ? getResources().getString(R.string.pet_father) :
                                (obs[0][1].equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.pet_maternal_grandmother) :
                                        (obs[0][1].equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.pet_maternal_grandfather) :
                                                (obs[0][1].equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.pet_paternal_grandmother) :
                                                        (obs[0][1].equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.pet_paternal_grandfather) :
                                                                (obs[0][1].equals("BROTHER") ? getResources().getString(R.string.pet_brother) :
                                                                        (obs[0][1].equals("SISTER") ? getResources().getString(R.string.pet_sister) :
                                                                                (obs[0][1].equals("SON") ? getResources().getString(R.string.pet_son) :
                                                                                        obs[0][1].equals("DAUGHTER") ? getResources().getString(R.string.pet_daughter) :
                                                                                                obs[0][1].equals("SPOUSE") ? getResources().getString(R.string.pet_spouse) :
                                                                                                        obs[0][1].equals("AUNT") ? getResources().getString(R.string.pet_aunt) :
                                                                                                                obs[0][1].equals("UNCLE") ? getResources().getString(R.string.pet_uncle) : getResources().getString(R.string.pet_other))))))))));
                treatmentSuppoterRelation.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("OTHER TREATMENT SUPPORTER RELATIONSHIP TO PATIENT")) {
                otherTreatmentSuppoterRelation.getEditText().setText(obs[0][1]);
                otherTreatmentSuppoterRelation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("BEHAVIORAL COMPLAINS AFTER TREATMENT")) {
                for (RadioButton rb : behavioralComplaint.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    }
                }
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
                behaviouralComplaintType.getSpinner().selectValue(value);
                behaviouralComplaintType.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER PERSONALITY TYPE")) {
                other.getEditText().setText(obs[0][1]);
                other.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT SUPPORTER NEGLIGENCE")) {
                for (RadioButton rb : treatmentSupportNegligence.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TREATMENT SUPPORTER NEGLIGENCE REASON")) {
                treatmentSupportNegligenceReason.getEditText().setText(obs[0][1]);
                treatmentSupportNegligenceReason.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT MISCONCEPTIONS")) {
                for (RadioButton rb : misconceptionInPet.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TREATMENT MISCONCEPTION (TEXT)")) {
                misconception.getEditText().setText(obs[0][1]);
                misconception.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("INFECTION CONTROL MEASURES FOLLOWED")) {
                for (RadioButton rb : infectionControllFollowing.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INFECTION CONTROL COUNSELLING")) {
                for (RadioButton rb : infectionControlCounselling.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                infectionControlCounselling.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("SOCIAL PROBLEM")) {
                for (CheckBox cb : patientFacingProblem.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_academic_problem)) && obs[0][1].equals("ACADEMIC PROBLEM")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_economic_problem)) && obs[0][1].equals("ECONOMIC PROBLEM")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_health_problem)) && obs[0][1].equals("PERSONAL BARRIER TO HEALTH CARE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_other)) && obs[0][1].equals("OTHER")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER")) {
                otherProblem.getEditText().setText(obs[0][1]);
                otherProblem.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("CARETAKER COMMENTS")) {
                contactComments.getEditText().setText(obs[0][1]);
                contactComments.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
                psychologistComments.getEditText().setText(obs[0][1]);
                psychologistComments.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }  else if (obs[0][0].equals("PATIENT REFERRED")) {
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
