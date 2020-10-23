package com.ihsinformatics.gfatmmobile.pet;

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

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
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
        followupMonth = new TitledEditText(context, null, getResources().getString(R.string.pet_followup_month), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "FOLLOW-UP MONTH");
        referralComplain = new TitledEditText(context, null, getResources().getString(R.string.pet_referral_complain), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "REFERRAL COMPLAINT");
        referralComplain.getEditText().setSingleLine(false);
        referralComplain.getEditText().setMinimumHeight(150);
        missedDosage = new TitledEditText(context, null, getResources().getString(R.string.pet_missed_dosed), "0", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false, "NUMBER OF MISSED MEDICATION DOSES IN LAST MONTH");
        adherentToPet = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adherent), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.HORIZONTAL, true, "PATIENT ADHERENT TO TRETMENT", getResources().getStringArray(R.array.yes_no_list_concept));
        reasonForNonAdherent = new TitledEditText(context, null, getResources().getString(R.string.pet_reason_for_non_adherent), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "NON-ADHERENCE REASON (TEXT)");

        adverseEventReport = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adverse_event_report), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "ADVERSE EVENTS REPORTED", getResources().getStringArray(R.array.yes_no_list_concept));
        adverseEffectsLayout = new LinearLayout(context);
        adverseEffectsLayout.setOrientation(LinearLayout.HORIZONTAL);
        adverseEffects1 = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_adverse_effects), getResources().getStringArray(R.array.pet_adverse_effects_1), null, App.VERTICAL, App.VERTICAL, true, "ADVERSE EVENTS", new String[]{"JOINT PAIN", "HEADACHE", "RASH", "NAUSEA", "DIZZINESS AND GIDDINESS"});
        adverseEffectsLayout.addView(adverseEffects1);
        adverseEffects2 = new TitledCheckBoxes(context, null, "", getResources().getStringArray(R.array.pet_adverse_effects_2), null, App.VERTICAL, App.VERTICAL, true, "ADVERSE EVENTS", new String[]{"VOMITING", "ABDOMINAL PAIN", "LOSS OF APPETITE", "VISUAL IMPAIRMENT", "OTHER ADVERSE EVENT"});
        adverseEffectsLayout.addView(adverseEffects2);
        otherEffects = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER ADVERSE EVENT");

        treatmentSuppoterRelation = new TitledSpinner(context, "", getResources().getString(R.string.pet_treatment_support), getResources().getStringArray(R.array.pet_cnic_owners), "", App.VERTICAL, true, "TREATMENT SUPPORTER RELATIONSHIP TO PATIENT", new String[]{"SELF", "MOTHER", "FATHER", "MATERNAL GRANDMOTHER", "MATERNAL GRANDFATHER", "PATERNAL GRANDMOTHER", "PATERNAL GRANDFATHER", "BROTHER", "SISTER", "SON", "DAUGHTER", "SPOUSE", "AUNT", "UNCLE", "OTHER"});
        otherTreatmentSuppoterRelation = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER TREATMENT SUPPORTER RELATIONSHIP TO PATIENT");
        behavioralComplaint = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_behavioural_complain), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "BEHAVIORAL COMPLAINS AFTER TREATMENT", getResources().getStringArray(R.array.yes_no_list_concept));
        behaviouralComplaintType = new TitledSpinner(context, "", getResources().getString(R.string.pet_behavioural_complain), getResources().getStringArray(R.array.pet_contact_behaviours), "", App.VERTICAL, true, "BEHAVIOUR", new String[]{"IRRITABILITY", "STUBBORN BEHAVIOUR", "INTROVERTED PERSONALITY", "AGGRESSIVE BEHAVIOUR", "ARGUMENTATIVE BEHAVIOUR", "NON COMPLIANT BEHAVIOUR", "COMPLIANT BEHAVIOUR", "RESPONSIBLE PERSONALITY", "COOPERATIVE BEHAVIOUR", "NON-COOPERATIVE BEHAVIOUR", "OTHER PERSONALITY TYPE"});
        other = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER PERSONALITY TYPE");

        treatmentSupportNegligence = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_caretaker_negligence), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "TREATMENT SUPPORTER NEGLIGENCE", getResources().getStringArray(R.array.yes_no_list_concept));
        treatmentSupportNegligenceReason = new TitledEditText(context, null, getResources().getString(R.string.pet_reason_caretaker_negligence), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "TREATMENT SUPPORTER NEGLIGENCE REASON");
        misconceptionInPet = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_misconcepions_after_pet_initiation), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "TREATMENT MISCONCEPTIONS", getResources().getStringArray(R.array.yes_no_list_concept));
        misconception = new TitledEditText(context, null, getResources().getString(R.string.pet_misconception), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "TREATMENT MISCONCEPTION (TEXT)");
        infectionControllFollowing = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_infection_control_following), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true, "INFECTION CONTROL MEASURES FOLLOWED", getResources().getStringArray(R.array.yes_no_list_concept));
        infectionControlCounselling = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_infection_control_counselling), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true, "INFECTION CONTROL COUNSELLING", getResources().getStringArray(R.array.yes_no_list_concept));
        patientFacingProblem = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_patient_problem), getResources().getStringArray(R.array.pet_patient_problem), null, App.VERTICAL, App.VERTICAL, true, "SOCIAL PROBLEM", new String[]{"ACADEMIC PROBLEM", "ECONOMIC PROBLEM", "PERSONAL BARRIER TO HEALTH CARE", "REFUSED", "UNKNOWN", "OTHER"});
        otherProblem = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER");

        contactComments = new TitledEditText(context, null, getResources().getString(R.string.pet_contact_comments), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "CARETAKER COMMENTS");
        contactComments.getEditText().setSingleLine(false);
        contactComments.getEditText().setMinimumHeight(150);
        psychologistComments = new TitledEditText(context, null, getResources().getString(R.string.pet_comment_psychologist), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "CLINICIAN NOTES (TEXT)");
        psychologistComments.getEditText().setSingleLine(false);
        psychologistComments.getEditText().setMinimumHeight(150);

        followupRequired = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_followup_required), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL, true, "CLINICAL FOLLOWUP NEEDED", getResources().getStringArray(R.array.yes_no_list_concept));
        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.pet_return_visit_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.VERTICAL);

        patientReferred = new TitledRadioGroup(context, null, getResources().getString(R.string.refer_patient), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL, true, "PATIENT REFERRED", getResources().getStringArray(R.array.yes_no_list_concept));
        referredTo = new TitledCheckBoxes(context, null, getResources().getString(R.string.refer_patient_to), getResources().getStringArray(R.array.refer_patient_to_option), null, App.VERTICAL, App.VERTICAL, true, "PATIENT REFERRED TO", new String[]{"COUNSELOR", "PSYCHOLOGIST", "CLINICAL OFFICER/DOCTOR", "CALL CENTER", "FIELD SUPERVISOR", "SITE SUPERVISOR"});
        referalReasonPsychologist = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_psychologist), getResources().getStringArray(R.array.referral_reason_for_psychologist_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR PSYCHOLOGIST/COUNSELOR REFERRAL", new String[]{"CHECK FOR TREATMENT ADHERENCE", "PSYCHOLOGICAL EVALUATION", "BEHAVIORAL ISSUES", "REFUSAL OF TREATMENT BY PATIENT", "OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR"});
        otherReferalReasonPsychologist = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR");
        referalReasonSupervisor = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_supervisor), getResources().getStringArray(R.array.referral_reason_for_supervisor_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR SUPERVISOR REFERRAL", new String[]{"CONTACT SCREENING REMINDER", "TREATMENT FOLLOWUP REMINDER", "CHECK FOR TREATMENT ADHERENCE", "INVESTIGATION OF REPORT COLLECTION", "ADVERSE EVENTS", "MEDICINE COLLECTION", "OTHER REFERRAL REASON TO SUPERVISOR"});
        otherReferalReasonSupervisor = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO SUPERVISOR");
        referalReasonCallCenter = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_center), getResources().getStringArray(R.array.referral_reason_for_call_center_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR CALL CENTER REFERRAL", new String[]{"CONTACT SCREENING REMINDER", "TREATMENT FOLLOWUP REMINDER", "CHECK FOR TREATMENT ADHERENCE", "INVESTIGATION OF REPORT COLLECTION", "ADVERSE EVENTS", "MEDICINE COLLECTION", "OTHER REFERRAL REASON TO CALL CENTER"});
        otherReferalReasonCallCenter = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO CALL CENTER");
        referalReasonClinician = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_clinician), getResources().getStringArray(R.array.referral_reason_for_clinician_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR CLINICIAN REFERRAL", new String[]{"EXPERT OPINION", "ADVERSE EVENTS", "OTHER REFERRAL REASON TO CLINICIAN"});
        otherReferalReasonClinician = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO CLINICIAN");

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
        followupRequired.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : adverseEffects2.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        for (CheckBox cb : patientFacingProblem.getCheckedBoxes()) {
            cb.setOnCheckedChangeListener(this);
        }

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

        if (refillFlag) {
            refillFlag = false;
            return;
        }

        if (snackbar != null)
            snackbar.dismiss();


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

            if (formDateCalendar.after(secondDateCalendar)) {

                secondDateCalendar.set(formDateCalendar.get(Calendar.YEAR), formDateCalendar.get(Calendar.MONTH), formDateCalendar.get(Calendar.DAY_OF_MONTH));
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }

        }

        if (!(returnVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = returnVisitDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }

        formDate.getButton().setEnabled(true);
        returnVisitDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {

        Boolean error = super.validate();

        View view = null;


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


        if (returnVisitDate.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDate(secondDateCalendar)});


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

        HashMap<String, String> formValues = new HashMap<String, String>();

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));

        return true;
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

        } else if (view == returnVisitDate.getButton()) {
            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowFutureDate", true);
            args.putBoolean("allowPastDate", false);
            args.putString("formDate", formDate.getButtonText());
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");*/
            returnVisitDate.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar, true, false, true);

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
        } else if (group == patientReferred.getRadioGroup()) {
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
        } else if (group == followupRequired.getRadioGroup()) {
            if (App.get(followupRequired).equals(getResources().getString(R.string.yes)))
                returnVisitDate.setVisibility(View.VISIBLE);
            else {
                returnVisitDate.setVisibility(View.GONE);
                /*snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fill_end_of_followup), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();*/
            }
            followupRequired.getQuestionView().setError(null);
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

          if (obs[0][0].equals("RETURN VISIT DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
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
