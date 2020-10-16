package com.ihsinformatics.gfatmmobile.pmdt;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Tahira on 12/21/2018.
 */

public class FieldTeamMonitoring extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;


    TitledRadioGroup patientCategory;
    TitledRadioGroup visitPurpose;
    TitledEditText treatmentSupporterCompleteName;

    TitledRadioGroup patientRegimenKnowledge;
    TitledRadioGroup sideEffectsKnowledge;
    TitledRadioGroup frequencyDot;
    TitledRadioGroup onInjectables;
    TitledRadioGroup administerInjections;

    TitledRadioGroup missedDose;
    TitledRadioGroup reasonMissedDose;
    TitledEditText reasonMissedDoseOther;
    TitledRadioGroup practiceInfectionControl;

    TitledEditText contactsCount;
    TitledRadioGroup filledContactRegistry;
    TitledEditText collectedSampleCount;

    TitledRadioGroup filledBaselineScreening;
    TitledRadioGroup patientSatisfactionTs;

    TitledRadioGroup reportedAdverseEvent;
    TitledCheckBoxes actionAdverseEvents;
    TitledEditText actionAdverseEventOther;

    TitledRadioGroup nutritionAdequate;
    TitledRadioGroup psychologicalIssue;
    TitledRadioGroup needCounsellingWeekly;
    TitledCheckBoxes counsellingTypes;
    TitledEditText counsellingTypeOther;

    TitledRadioGroup needMentalHealthCounselling;
    TitledRadioGroup remindedNextFollowup;
    TitledCheckBoxes patientCounsellingTypes;

    TitledEditText homeVisitSummary;


    TitledEditText weight;

    TitledRadioGroup cough;
    TitledRadioGroup coughDuration;
    TitledRadioGroup haemoptysis;
    TitledRadioGroup difficultyBreathing;
    TitledRadioGroup fever;
    TitledRadioGroup feverDuration;
    TitledRadioGroup weightLoss;
    TitledRadioGroup nightSweats;
    TitledRadioGroup lethargy;
    TitledRadioGroup swollenJoints;
    TitledRadioGroup backPain;
    TitledRadioGroup adenopathy;
    TitledRadioGroup vomiting;
    TitledRadioGroup giSymptoms;
    TitledRadioGroup lossInterestInActivity;

    TitledRadioGroup dizziness;
    TitledRadioGroup nausea;
    TitledRadioGroup abdominalPain;
    TitledRadioGroup lossOfAppetite;
    TitledRadioGroup jaundice;
    TitledRadioGroup rash;
    TitledRadioGroup tendonPain;
    TitledRadioGroup eyeProblem;
    TitledEditText otherSideEffects;
    TitledRadioGroup sideeffectsConsistent;

    TitledEditText missedDosage;
    TitledCheckBoxes actionPlan;
    TitledEditText medicationDiscontinueReason;
    TitledEditText medicationDiscontinueDuration;
    TitledEditText newMedication;
    TitledEditText newMedicationDuration;
    TitledRadioGroup petRegimen;
    TitledRadioGroup rifapentineAvailable;
    TitledEditText isoniazidDose;
    TitledEditText rifapentineDose;
    TitledEditText levofloxacinDose;
    TitledEditText ethionamideDose;
    TitledEditText ethambutolDose;
    TitledEditText moxifloxacilinDose;
    TitledCheckBoxes ancillaryDrugs;
    TitledEditText ancillaryDrugDuration;
    TitledEditText otherAncillaryDrugs;
    TitledEditText treatmentInterruptedReason;
    TitledEditText newInstruction;

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

    TitledRadioGroup followupRequired;

    TitledEditText clincianNote;

    ScrollView scrollView;

    Boolean refillFlag = false;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 4;
        formName = Forms.PMDT_FIELD_TEAM_MONITORING_FORM;
        form = Forms.pmdt_Field_Team_Monitoring;

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

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);

        patientCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_patient_category), getResources().getStringArray(R.array.pmdt_patient_category_array), getResources().getString(R.string.pmdt_high_risk), App.HORIZONTAL, App.VERTICAL, false, "PATIENT RISK CATEGORY", new String[]{"HIGH RISK", "STANDARD"});
        visitPurpose = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_visit_purpose), getResources().getStringArray(R.array.pmdt_purpose_visit_array), getResources().getString(R.string.pmdt_new_enrollment), App.VERTICAL, App.VERTICAL, false, "VISIT PURPOSE", new String[]{"NEW ENROLLMENT", "FOLLOW-UP VISIT", "MISSED FOLLOW-UP", "LTFU RETRIEVAL VISIT", "EMERGENCY VISIT"});
        treatmentSupporterCompleteName = new TitledEditText(context, null, getResources().getString(R.string.pmdt_ts_complete_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        patientRegimenKnowledge = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_regimen_knowledge), getResources().getStringArray(R.array.pmdt_regimen_knowledge_array), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, false, "PATIENT UNDERSTAND TB REGIMEN", getResources().getStringArray(R.array.yes_no_list_concept));
        sideEffectsKnowledge = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_side_effects_knowledge), getResources().getStringArray(R.array.pmdt_patient_know_side_effects_array), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, false, "PATIENT KNOW COMMON SIDE EFFECTS", getResources().getStringArray(R.array.yes_no_list_concept));
        frequencyDot = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_frequenct_dot), getResources().getStringArray(R.array.pmdt_frequenct_dot_array), getResources().getString(R.string.pmdt_everyday), App.VERTICAL, App.VERTICAL, false, "TREATMENT SUPPORTER PROVIDE DOT", new String[]{"EVERY DAY", "WEEKLY", "MONTHLY", "NOT AT ALL"});
        onInjectables = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_on_injectables), getResources().getStringArray(R.array.pmdt_on_injectables_array), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, false, "PATIENT ON INJECTABLES", getResources().getStringArray(R.array.yes_no_list_concept));
        administerInjections = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_administer_injections), getResources().getStringArray(R.array.pmdt_administer_injections_array), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL, false, "TREATMENT SUPPORTER ADMINISTER INJECTION", new String[]{"YES", "NO", "NOT APPLICABLE"});
        missedDose = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_missed_dose), getResources().getStringArray(R.array.pmdt_missed_dose_array), getResources().getString(R.string.no), App.VERTICAL, App.VERTICAL, false, "PATIENT MISSED DOSE", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        reasonMissedDose = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_reason_missed_dose), getResources().getStringArray(R.array.pmdt_reason_missed_dose_array), getResources().getString(R.string.pmdt_adverse_event), App.VERTICAL, App.VERTICAL, false, "REASON MISSED DOSE", new String[]{"ADVERSE EVENTS", "DRUG NOT AVAILABLE", "TREATMENT SUPPORTER DID NOT COME", "REFUSED", "UNKNOWN", "OTHER REASON MISSED DOSE"});
        reasonMissedDoseOther = new TitledEditText(context, null, getResources().getString(R.string.pmdt_other_reason_missed_dose), "", "", 100, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "OTHER REASON MISSED DOSE");
        practiceInfectionControl = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_practice_infection_control), getResources().getStringArray(R.array.pmdt_practice_infection_control_array), getResources().getString(R.string.pmdt_unknown), App.VERTICAL, App.VERTICAL, false, "FAMILY PRACTICE INFECTION CONTROL MEASURES", new String[]{"ALWAYS", "SOMETIMES", "RARELY", "NEVER", "UNKNOWN"});
        contactsCount = new TitledEditText(context, null, getResources().getString(R.string.pmdt_contacts_count), "", "", 2, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false, "NUMBER OF CONTACTS");
        filledContactRegistry = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_filled_contact_registry), getResources().getStringArray(R.array.pmdt_filled_contact_registry_array), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, false, "FILLED CONTACT REGISTRY", getResources().getStringArray(R.array.yes_no_list_concept));
        collectedSampleCount = new TitledEditText(context, null, getResources().getString(R.string.pmdt_collected_sample_count), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true, "COLLECTED SPUTUM SAMPLES COUNT");
        filledBaselineScreening = new TitledRadioGroup(context, getResources().getString(R.string.pmdt_filled_baseline_screening_note), getResources().getString(R.string.pmdt_filled_baseline_screening), getResources().getStringArray(R.array.pmdt_filled_baseline_screening_array), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, false, "BASELINE SCREENING FOR FAMILY", new String[]{"YES", "NO", "UNKNOWN"});
        patientSatisfactionTs = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_patient_satified_with_ts), getResources().getStringArray(R.array.pmdt_patient_satisfied_array), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, false, "PATIENT SATISFIED WITH TREATMENT SUPPORTER", getResources().getStringArray(R.array.yes_no_list_concept));
        reportedAdverseEvent = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_reported_adverse_event), getResources().getStringArray(R.array.pmdt_reported_adverse_event_array), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, false, "ADVERSE EVENT REPORTED", new String[]{"YES", "NO", "REFUSED"});
        actionAdverseEvents = new TitledCheckBoxes(context, null, getResources().getString(R.string.pmdt_action_adverse_event), getResources().getStringArray(R.array.pmdt_action_adverse_event_array), null, App.VERTICAL, App.VERTICAL, false, "ACTION ADVERSE EVENT", new String[]{"ARRANGED FOLLOW UP NEXT DAY FOR CLINICAL REVIEW", "CONSULTED ON PHONE WITH TB DOCTOR AND ADVISED ANCILLARY MEDICATIONS", "CONSULTED ON PHONE WITH TB DOCTOR AND ARRANGED TRANSFER TO REFERRAL HOSPITAL", "NO ACTION WAS REQUIRED", "OTHER ACTION ADVERSE EVENT"});

        actionAdverseEventOther = new TitledEditText(context, null, getResources().getString(R.string.pmdt_action_adverse_event_other), "", "", 100, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "OTHER ACTION ADVERSE EVENT");

        nutritionAdequate = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_nutrition_adequate), getResources().getStringArray(R.array.pmdt_nutrition_adequate_array), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, false, "ADEQUATE NUTRITION", getResources().getStringArray(R.array.yes_no_list_concept));
        psychologicalIssue = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_psychological_issue), getResources().getStringArray(R.array.pmdt_psychological_issue_array), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, false, "PATIENT PSYCHOLOGICAL ISSUE", getResources().getStringArray(R.array.yes_no_list_concept));
        needCounsellingWeekly = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_need_weekly_counselling), getResources().getStringArray(R.array.pmdt_need_counselling_calls_array), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, false, "NEED WEEKLY COUNSELING CALLS", getResources().getStringArray(R.array.yes_no_list_concept));
        counsellingTypes = new TitledCheckBoxes(context, null, getResources().getString(R.string.pmdt_counselling_type), getResources().getStringArray(R.array.pmdt_counselling_types_array), null, App.VERTICAL, App.VERTICAL, false, "COUNSELING TYPE", new String[]{"ADHERENCE", "INFECTION CONTROL COUNSELLING", "ADVERSE EVENTS", "PSYCHOSOCIAL COUNSELING", "SUBSTANCE ABUSE", "OTHER COUNSELING TYPE"});
        counsellingTypeOther = new TitledEditText(context, null, getResources().getString(R.string.pmdt_counselling_type_other), "", "", 100, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "OTHER COUNSELING TYPE");

        needMentalHealthCounselling = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_need_mental_health_counselling), getResources().getStringArray(R.array.pmdt_need_mental_health_counselling_array), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, false, "NEED MENTAL HEALTH COUNSELING", getResources().getStringArray(R.array.yes_no_list_concept));
        remindedNextFollowup = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_reminded_followup), getResources().getStringArray(R.array.pmdt_reminded_follow_up_array), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, false, "REMINDER FOR FOLLOWUP VISIT", getResources().getStringArray(R.array.yes_no_list_concept));
        patientCounsellingTypes = new TitledCheckBoxes(context, null, getResources().getString(R.string.pmdt_patient_counselling_types), getResources().getStringArray(R.array.pmdt_patient_counselling_types_array), null, App.VERTICAL, App.VERTICAL, true, "COUNSELING PROVIDED FOR", new String[]{"TREATMENT ADHERENCE", "MEDICATION INTAKE", "INFECTION CONTROL", "NUTRITIONAL NEEDS", "FAMILY PLANNING", "CONTACT SCREENING", "SMOKING CESSATION", "SUBSTANCE ABUSE", "IMPORTANCE OF REGULAR MONTHLY FOLLOW UP", "SPUTUM SAMPLE SUBMISSION ON EACH VISIT", "MENTAL HEALTH WELL BEING", "ADVERSE EVENTS AND THEIR MANAGEMENT", "AAO TB MITAO HELPLINE USE", "EDUCATIONAL MATERIAL FOR PATIENT"});

        homeVisitSummary = new TitledEditText(context, null, getResources().getString(R.string.pmdt_home_visit_summary), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "HOME VISIT SUMMARY");


        weight = new TitledEditText(context, null, getResources().getString(R.string.pet_weight), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false,"WEIGHT (KG)");

        MyLinearLayout linearLayout1 = new MyLinearLayout(context, getResources().getString(R.string.pet_tb_symptoms), App.VERTICAL);
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_cough), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL,true,"COUGH",new String[]{ "YES" , "NO" });
        coughDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_cough_duration), getResources().getStringArray(R.array.pet_cough_durations), getResources().getString(R.string.pet_less_than_2_weeks), App.VERTICAL, App.VERTICAL,true,"COUGH DURATION",new String[]{ "COUGH LASTING LESS THAN 2 WEEKS" , "COUGH LASTING MORE THAN 2 WEEKS" , "COUGH LASTING MORE THAN 3 WEEKS" , "UNKNOWN" , "REFUSED"});
        haemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_haemoptysis), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL,true,"HEMOPTYSIS",new String[]{ "YES" , "NO" });
        difficultyBreathing = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_difficulty_breathing), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL,true,"DYSPNEA",new String[]{ "YES" , "NO" });
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_has_fever), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL,true,"FEVER",new String[]{ "YES" , "NO" });
        feverDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_fever_duration), getResources().getStringArray(R.array.pet_cough_durations), getResources().getString(R.string.pet_less_than_2_weeks), App.VERTICAL, App.VERTICAL,true,"FEVER DURATION",new String[]{ "COUGH LASTING LESS THAN 2 WEEKS" , "COUGH LASTING MORE THAN 2 WEEKS" , "COUGH LASTING MORE THAN 3 WEEKS" , "UNKNOWN" , "REFUSED"});
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_have_weight_loss), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL,true,"WEIGHT LOSS",new String[]{ "YES" , "NO" });
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_have_night_sweats), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL,true,"NIGHT SWEATS",new String[]{ "YES" , "NO" });
        lethargy = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_lethargy), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL,true,"LETHARGY",new String[]{ "YES" , "NO" , "REFUSED" , "UNKNOWN"});
        swollenJoints = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_swollen_joints), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL,true,"JOINT SWELLING",new String[]{ "YES" , "NO" , "REFUSED" , "UNKNOWN"});
        backPain = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_back_pain), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL,true,"BACK PAIN",new String[]{ "YES" , "NO" , "REFUSED" , "UNKNOWN"});
        adenopathy = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adenopathy), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL,true,"ADENOPATHY",new String[]{ "YES" , "NO" , "REFUSED" , "UNKNOWN"});
        vomiting = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_vomiting_without_gi_symptoms), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL,true,"VOMITING",new String[]{ "YES" , "NO" , "REFUSED" , "UNKNOWN"});
        giSymptoms = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_gi_symptoms), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL,true,"GASTROINTESTINAL SYMPTOM",new String[]{ "YES" , "NO" , "REFUSED" , "UNKNOWN"});
        lossInterestInActivity = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_lost_activity_interest), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL,true,"LOSS OF INTEREST",new String[]{ "YES" , "NO" , "REFUSED" , "UNKNOWN"});

        linearLayout1.addView(cough);
        linearLayout1.addView(coughDuration);
        linearLayout1.addView(haemoptysis);
        linearLayout1.addView(difficultyBreathing);
        linearLayout1.addView(fever);
        linearLayout1.addView(feverDuration);
        linearLayout1.addView(weightLoss);
        linearLayout1.addView(nightSweats);
        linearLayout1.addView(lethargy);
        linearLayout1.addView(swollenJoints);
        linearLayout1.addView(backPain);
        linearLayout1.addView(adenopathy);
        linearLayout1.addView(vomiting);
        linearLayout1.addView(giSymptoms);
        linearLayout1.addView(lossInterestInActivity);

        MyLinearLayout linearLayout2 = new MyLinearLayout(context, getResources().getString(R.string.pet_medication_side_effects), App.VERTICAL);
        dizziness = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_dizziness), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        nausea = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_nausea_vomiting), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        abdominalPain = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_abdominal_pain), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        lossOfAppetite = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_loss_of_appetite), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        jaundice = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_jaundice), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        rash = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_rash), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        tendonPain = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_tendon_pain), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        eyeProblem = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_eye_problems), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        otherSideEffects = new TitledEditText(context, null, getResources().getString(R.string.pet_other_side_effects), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        otherSideEffects.getEditText().setSingleLine(false);
        otherSideEffects.getEditText().setMinimumHeight(150);
        sideeffectsConsistent = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_side_effect_consistent), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);

        linearLayout2.addView(dizziness);
        linearLayout2.addView(nausea);
        linearLayout2.addView(abdominalPain);
        linearLayout2.addView(lossOfAppetite);
        linearLayout2.addView(jaundice);
        linearLayout2.addView(rash);
        linearLayout2.addView(tendonPain);
        linearLayout2.addView(eyeProblem);
        linearLayout2.addView(otherSideEffects);
        linearLayout2.addView(sideeffectsConsistent);

        MyLinearLayout linearLayout3 = new MyLinearLayout(context, getResources().getString(R.string.pet_clinician_review), App.VERTICAL);
        missedDosage = new TitledEditText(context, null, getResources().getString(R.string.pet_missed_dosed), "0", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false,"NUMBER OF MISSED MEDICATION DOSES IN LAST MONTH");
        actionPlan = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_action_plan), getResources().getStringArray(R.array.pet_action_plan), null, App.VERTICAL, App.VERTICAL);
        medicationDiscontinueReason = new TitledEditText(context, null, getResources().getString(R.string.pet_discontinue_medication), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        medicationDiscontinueReason.getEditText().setSingleLine(false);
        medicationDiscontinueReason.getEditText().setMinimumHeight(150);
        medicationDiscontinueDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_discontinue_medication_duration), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        newMedication = new TitledEditText(context, null, getResources().getString(R.string.pet_new_medication), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        newMedication.getEditText().setSingleLine(false);
        newMedication.getEditText().setMinimumHeight(150);
        newMedicationDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_new_medication_duration), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        petRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_regimen), getResources().getStringArray(R.array.pet_regimens), "", App.VERTICAL, App.VERTICAL,true,"POST-EXPOSURE TREATMENT REGIMEN",new String[]{ "ISONIAZID PROPHYLAXIS" , "ISONIAZID AND RIFAPENTINE" , "LEVOFLOXACIN AND ETHIONAMIDE" , "LEVOFLOXACIN AND ETHAMBUTOL" , "LEVOFLOXACIN AND MOXIFLOXACILIN" , "ETHIONAMIDE AND ETHAMBUTOL" , "ETHIONAMIDE AND MOXIFLOXACILIN" , "MOXIFLOXACILIN AND ETHAMBUTOL"});
        isoniazidDose = new TitledEditText(context, null, getResources().getString(R.string.pet_isoniazid_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true,"ISONIAZID DOSE");
        rifapentineAvailable = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_rifapentine_available), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        rifapentineDose = new TitledEditText(context, null, getResources().getString(R.string.pet_rifapentine_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true,"RIFAPENTINE DOSE");
        levofloxacinDose = new TitledEditText(context, null, getResources().getString(R.string.pet_levofloxacin_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true,"LEVOFLOXACIN DOSE");
        ethionamideDose = new TitledEditText(context, null, getResources().getString(R.string.pet_ethionamide_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "ETHIONAMIDE DOSE");
        ethambutolDose = new TitledEditText(context, null, getResources().getString(R.string.pet_ethambutol_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "ETHAMBUTOL DOSE");
        moxifloxacilinDose = new TitledEditText(context, null, getResources().getString(R.string.pet_moxifloxacilin_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "MOXIFLOXACILIN DOSE");
        ancillaryDrugs = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_ancillary_drugs), getResources().getStringArray(R.array.pet_ancillary_drugs), null, App.VERTICAL, App.VERTICAL);
        ancillaryDrugDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_ancillary_drug_duration_days), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        otherAncillaryDrugs = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true,"OTHER ANCILLARY DRUGS");
        treatmentInterruptedReason = new TitledEditText(context, null, getResources().getString(R.string.pet_treatment_interrupted_reason), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        treatmentInterruptedReason.getEditText().setSingleLine(false);
        treatmentInterruptedReason.getEditText().setMinimumHeight(150);
        newInstruction = new TitledEditText(context, null, getResources().getString(R.string.pet_new_instructions), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        newInstruction.getEditText().setSingleLine(false);
        newInstruction.getEditText().setMinimumHeight(150);

        patientReferred = new TitledRadioGroup(context, null, getResources().getString(R.string.refer_patient), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL, true);
        referredTo = new TitledCheckBoxes(context, null, getResources().getString(R.string.refer_patient_to), getResources().getStringArray(R.array.refer_patient_to_option), null, App.VERTICAL, App.VERTICAL, true, "PATIENT REFERRED TO", new String[]{"COUNSELOR", "PSYCHOLOGIST", "CLINICAL OFFICER/DOCTOR", "CALL CENTER", "FIELD SUPERVISOR", "SITE SUPERVISOR"});
        referalReasonPsychologist = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_psychologist), getResources().getStringArray(R.array.referral_reason_for_psychologist_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR PSYCHOLOGIST/COUNSELOR REFERRAL", new String[]{"CHECK FOR TREATMENT ADHERENCE", "PSYCHOLOGICAL EVALUATION", "BEHAVIORAL ISSUES", "REFUSAL OF TREATMENT BY PATIENT", "OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR"});
        otherReferalReasonPsychologist = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"OTHER REFERRAL REASON TO PSYCHOLOGIST/COUNSELOR");
        referalReasonSupervisor = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_supervisor), getResources().getStringArray(R.array.referral_reason_for_supervisor_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR SUPERVISOR REFERRAL", new String[]{"CONTACT SCREENING REMINDER", "TREATMENT FOLLOWUP REMINDER", "CHECK FOR TREATMENT ADHERENCE", "INVESTIGATION OF REPORT COLLECTION", "ADVERSE EVENTS", "MEDICINE COLLECTION", "OTHER REFERRAL REASON TO SUPERVISOR"});
        otherReferalReasonSupervisor = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO SUPERVISOR");
        referalReasonCallCenter = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_center), getResources().getStringArray(R.array.referral_reason_for_call_center_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR CALL CENTER REFERRAL", new String[]{"CONTACT SCREENING REMINDER", "TREATMENT FOLLOWUP REMINDER", "CHECK FOR TREATMENT ADHERENCE", "INVESTIGATION OF REPORT COLLECTION", "ADVERSE EVENTS", "MEDICINE COLLECTION", "OTHER REFERRAL REASON TO CALL CENTER"});
        otherReferalReasonCallCenter = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO CALL CENTER");
        referalReasonClinician = new TitledCheckBoxes(context, null, getResources().getString(R.string.referral_reason_for_call_clinician), getResources().getStringArray(R.array.referral_reason_for_clinician_option), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR CLINICIAN REFERRAL", new String[]{"EXPERT OPINION", "ADVERSE EVENTS", "OTHER REFERRAL REASON TO CLINICIAN"});
        otherReferalReasonClinician = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REFERRAL REASON TO CLINICIAN");

        followupRequired = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_followup_required), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL, true,"CLINICAL FOLLOWUP NEEDED",getResources().getStringArray(R.array.yes_no_list_concept));
        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.pet_doctor_notes), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "CLINICIAN NOTES (TEXT)");
        clincianNote.getEditText().setSingleLine(false);
        clincianNote.getEditText().setMinimumHeight(150);

        linearLayout3.addView(missedDosage);
        linearLayout3.addView(actionPlan);
        linearLayout3.addView(medicationDiscontinueReason);
        linearLayout3.addView(medicationDiscontinueDuration);
        linearLayout3.addView(newMedication);
        linearLayout3.addView(newMedicationDuration);
        linearLayout3.addView(petRegimen);
        linearLayout3.addView(rifapentineAvailable);
        linearLayout3.addView(isoniazidDose);
        linearLayout3.addView(rifapentineDose);
        linearLayout3.addView(levofloxacinDose);
        linearLayout3.addView(ethionamideDose);
        linearLayout3.addView(ethambutolDose);
        linearLayout3.addView(moxifloxacilinDose);
        linearLayout3.addView(ancillaryDrugs);
        linearLayout3.addView(otherAncillaryDrugs);
        linearLayout3.addView(ancillaryDrugDuration);
        linearLayout3.addView(treatmentInterruptedReason);
        linearLayout3.addView(newInstruction);

        views = new View[]{formDate.getButton(), patientCategory.getRadioGroup(), visitPurpose.getRadioGroup(), treatmentSupporterCompleteName.getEditText(), patientRegimenKnowledge.getRadioGroup(), sideEffectsKnowledge.getRadioGroup(),
                frequencyDot.getRadioGroup(), onInjectables.getRadioGroup(), administerInjections.getRadioGroup(), missedDose.getRadioGroup(), reasonMissedDose.getRadioGroup(), reasonMissedDoseOther.getEditText(), practiceInfectionControl.getRadioGroup(),
                contactsCount.getEditText(), filledContactRegistry.getRadioGroup(), collectedSampleCount.getEditText(), filledBaselineScreening.getRadioGroup(), patientSatisfactionTs.getRadioGroup(), reportedAdverseEvent.getRadioGroup(), actionAdverseEvents,
                actionAdverseEventOther.getEditText(), nutritionAdequate.getRadioGroup(), psychologicalIssue.getRadioGroup(), needCounsellingWeekly.getRadioGroup(), counsellingTypes, counsellingTypeOther.getEditText(), needMentalHealthCounselling.getRadioGroup(),
                remindedNextFollowup.getRadioGroup(), patientCounsellingTypes, homeVisitSummary.getEditText()
        };


        viewGroups = new View[][]{{formDate, patientCategory, visitPurpose, treatmentSupporterCompleteName, patientRegimenKnowledge, sideEffectsKnowledge},
                {frequencyDot, onInjectables, administerInjections, missedDose, reasonMissedDose, reasonMissedDoseOther,},
                {practiceInfectionControl, contactsCount, filledContactRegistry, collectedSampleCount, filledBaselineScreening, patientSatisfactionTs, reportedAdverseEvent, actionAdverseEvents, actionAdverseEventOther},
                {nutritionAdequate, psychologicalIssue, needCounsellingWeekly, counsellingTypes, counsellingTypeOther, needMentalHealthCounselling, remindedNextFollowup, patientCounsellingTypes, homeVisitSummary}};

        formDate.getButton().setOnClickListener(this);
        missedDose.getRadioGroup().setOnCheckedChangeListener(this);
        reasonMissedDose.getRadioGroup().setOnCheckedChangeListener(this);
        reportedAdverseEvent.getRadioGroup().setOnCheckedChangeListener(this);
        needCounsellingWeekly.getRadioGroup().setOnCheckedChangeListener(this);

        for (CheckBox cb : actionAdverseEvents.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        for (CheckBox cb : counsellingTypes.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

//        for (CheckBox cb : patientCounsellingTypes.getCheckedBoxes())
//            cb.setOnCheckedChangeListener(this);


        /*newMedicationDuration.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!App.get(newMedicationDuration).equals("")){
                    int val = Integer.parseInt(App.get(newMedicationDuration));
                    if(val > 150) {
                        newMedicationDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                    }
                    else {
                        newMedicationDuration.getEditText().setError(null);
                    }
                }

                calculateDosages();

            }
        });*/

        /*medicationDiscontinueDuration.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!App.get(medicationDiscontinueDuration).equals("")){
                    int val = Integer.parseInt(App.get(medicationDiscontinueDuration));
                    if(val > 150) {
                        medicationDiscontinueDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                    }
                    else {
                        medicationDiscontinueDuration.getEditText().setError(null);
                    }
                }


            }
        });*/

        /*weight.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!App.get(weight).equals("")){
                    Double w = Double.parseDouble(App.get(weight));
                    if(w < 0.5 || w > 700.0)
                        weight.getEditText().setError(getString(R.string.pet_invalid_weight_range));
                    else
                        weight.getEditText().setError(null);
                }

            }
        });*/


        resetViews();

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("EEEE, MMMM dd,yyyy", formDateCalendar).toString());

        reasonMissedDose.setVisibility(View.GONE);
        reasonMissedDoseOther.setVisibility(View.GONE);
        actionAdverseEvents.setVisibility(View.GONE);
        actionAdverseEventOther.setVisibility(View.GONE);

        counsellingTypes.setVisibility(View.GONE);
        counsellingTypeOther.setVisibility(View.GONE);


        Bundle bundle = this.getArguments();
        Boolean autoFill = false;
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);

                autoFill = true;
                refill(formId);

            } else bundle.putBoolean("save", false);

        }

        if (!autoFill) {
            final AsyncTask<String, String, HashMap<String, String>> autopopulateFormTask = new AsyncTask<String, String, HashMap<String, String>>() {
                @Override
                protected HashMap<String, String> doInBackground(String... strings) {
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

                    String relationshipUuid = serverService.getLatestObsValue(App.getPatientId(), "PMDT-Treatment Supporter Assignment", "RELATIONSHIP UUID");
                    String bPersonName = serverService.getRelationshipBPersonName(relationshipUuid);

                    String contactsCount = serverService.getLatestObsValue(App.getPatientId(), Forms.CONTACT_REGISTRY, "NUMBER OF CONTACTS");
                    if (contactsCount != null)
                        contactsCount = contactsCount.replace(".0", "");

                    result.put("TREATMENT SUPPORTER NAME", bPersonName);
                    result.put("TOTAL CONTACTS", contactsCount);

                    return result;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    super.onProgressUpdate(values);
                }

                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();


                    if (result.get("TREATMENT SUPPORTER NAME") != null)
                        if (!result.get("TREATMENT SUPPORTER NAME").equals("")) {
                            treatmentSupporterCompleteName.getEditText().setText(result.get("TREATMENT SUPPORTER NAME"));
                        }

                    if (result.get("TOTAL CONTACTS") != null)
                        if (!result.get("TOTAL CONTACTS").equals("")) {
                            contactsCount.getEditText().setText(result.get("TOTAL CONTACTS"));
                        }
                }


            };
            autopopulateFormTask.execute("");
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

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();
            personDOB = personDOB.substring(0, 10);

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMMM dd,yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMMM dd,yyyy", formDateCalendar).toString());

            if (formDateCalendar.after(secondDateCalendar)) {

                secondDateCalendar.set(formDateCalendar.get(Calendar.YEAR), formDateCalendar.get(Calendar.MONTH), formDateCalendar.get(Calendar.DAY_OF_MONTH));
            }

        }

        formDate.getButton().setEnabled(true);

    }


    @Override
    public boolean validate() {

        View view = null;
        Boolean error = super.validate();

        /*Boolean flag = true;

        for (CheckBox cb : patientCounsellingTypes.getCheckedBoxes()) {
            if (cb.isChecked()) {
                flag = false;
                break;
            }
        }
        if (flag) {
            patientCounsellingTypes.getQuestionView().setError(getString(R.string.empty_field));
            patientCounsellingTypes.getQuestionView().requestFocus();
            view = patientCounsellingTypes;
            if (App.isLanguageRTL())
                gotoFirstPage();
            else
                gotoLastPage();
            error = true;
        } else {
            patientCounsellingTypes.getQuestionView().setError(null);
        }

        if (counsellingTypeOther.getVisibility() == View.VISIBLE && App.get(counsellingTypeOther).equals("")) {
            counsellingTypeOther.getQuestionView().setError(getString(R.string.empty_field));
            counsellingTypeOther.getQuestionView().requestFocus();
            view = null;
            if (App.isLanguageRTL())
                gotoFirstPage();
            else
                gotoLastPage();
            error = true;
        } else {
            counsellingTypeOther.getQuestionView().setError(null);
            counsellingTypeOther.getEditText().clearFocus();
        }

        flag = true;

        if (counsellingTypes.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : counsellingTypes.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                counsellingTypes.getQuestionView().setError(getString(R.string.empty_field));
                counsellingTypes.getQuestionView().requestFocus();
                view = counsellingTypes;
                if (App.isLanguageRTL())
                    gotoFirstPage();
                else
                    gotoLastPage();
                error = true;
            } else {
                counsellingTypes.getQuestionView().setError(null);
            }
        }

        if (actionAdverseEventOther.getVisibility() == View.VISIBLE && App.get(actionAdverseEventOther).equals("")) {
            actionAdverseEventOther.getQuestionView().setError(getString(R.string.empty_field));
            actionAdverseEventOther.getQuestionView().requestFocus();
            view = null;
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(2);
            error = true;
        } else {
            actionAdverseEventOther.getQuestionView().setError(null);
            actionAdverseEventOther.getEditText().clearFocus();
        }

        if (actionAdverseEvents.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : actionAdverseEvents.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                actionAdverseEvents.getQuestionView().setError(getString(R.string.empty_field));
                actionAdverseEvents.getQuestionView().requestFocus();
                view = actionAdverseEvents;
                if (App.isLanguageRTL())
                    gotoPage(1);
                else
                    gotoPage(2);
                error = true;
            } else {
                actionAdverseEvents.getQuestionView().setError(null);
            }
        }


        if (App.get(collectedSampleCount).equals("")) {
            collectedSampleCount.getQuestionView().setError(getString(R.string.empty_field));
            collectedSampleCount.getQuestionView().requestFocus();
            view = null;
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(2);
            error = true;
        } else {
            collectedSampleCount.getQuestionView().setError(null);
            collectedSampleCount.getEditText().clearFocus();
        }

        if (App.get(reasonMissedDoseOther).equals("")) {
            reasonMissedDoseOther.getQuestionView().setError(getString(R.string.empty_field));
            reasonMissedDoseOther.getQuestionView().requestFocus();
            view = null;
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(1);
            error = true;
        } else {
            reasonMissedDoseOther.getQuestionView().setError(null);
            reasonMissedDoseOther.getEditText().clearFocus();
        }*/


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
                                        missedDosage.getEditText().clearFocus();
                                        medicationDiscontinueReason.getEditText().clearFocus();
                                        medicationDiscontinueDuration.getEditText().clearFocus();
                                        newMedicationDuration.getEditText().clearFocus();
                                        newMedication.getEditText().clearFocus();
                                        ethionamideDose.getEditText().clearFocus();
                                        levofloxacinDose.getEditText().clearFocus();
                                        rifapentineDose.getEditText().clearFocus();
                                        isoniazidDose.getEditText().clearFocus();
                                        ancillaryDrugDuration.getEditText().clearFocus();
                                        newInstruction.getEditText().clearFocus();
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

/*
          observations.add(new String[]{"PATIENT RISK CATEGORY", App.get(patientCategory).equals(getResources().getString(R.string.pmdt_high_risk)) ? "HIGH RISK" : "STANDARD"});
        observations.add(new String[]{"VISIT PURPOSE", App.get(visitPurpose).equals(getResources().getString(R.string.pmdt_new_enrollment)) ? "NEW ENROLLMENT" :
                (App.get(visitPurpose).equals(getResources().getString(R.string.pmdt_follow_up_visit)) ? "FOLLOW-UP VISIT" :
                        (App.get(visitPurpose).equals(getResources().getString(R.string.pmdt_missed_follow_up)) ? "MISSED FOLLOW-UP" :
                                (App.get(visitPurpose).equals(getResources().getString(R.string.pmdt_ltfu_retrieval_visit)) ? "LTFU RETRIEVAL VISIT" : "EMERGENCY VISIT")))});

        observations.add(new String[]{"PATIENT UNDERSTAND TB REGIMEN", App.get(patientRegimenKnowledge).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"PATIENT KNOW COMMON SIDE EFFECTS", App.get(sideEffectsKnowledge).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
*/

/*        observations.add(new String[]{"TREATMENT SUPPORTER PROVIDE DOT", App.get(frequencyDot).equals(getResources().getString(R.string.pmdt_everyday)) ? "EVERY DAY" :
                (App.get(frequencyDot).equals(getResources().getString(R.string.pmdt_atleast_weekly)) ? "WEEKLY" :
                        (App.get(frequencyDot).equals(getResources().getString(R.string.pmdt_atleast_monthly)) ? "MONTHLY" : "NOT AT ALL"))});

        observations.add(new String[]{"PATIENT ON INJECTABLES", App.get(onInjectables).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        observations.add(new String[]{"TREATMENT SUPPORTER ADMINISTER INJECTION", App.get(administerInjections).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(administerInjections).equals(getResources().getString(R.string.no)) ? "NO" : "NOT APPLICABLE")});

        observations.add(new String[]{"PATIENT MISSED DOSE", App.get(missedDose).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(missedDose).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(missedDose).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});*/

/*
        if (reasonMissedDose.getVisibility() == View.VISIBLE)
        observations.add(new String[]{"REASON MISSED DOSE", App.get(reasonMissedDose).equals(getResources().getString(R.string.pmdt_adverse_event)) ? "ADVERSE EVENTS" :
                (App.get(reasonMissedDose).equals(getResources().getString(R.string.pmdt_drug_not_available)) ? "DRUG NOT AVAILABLE" :
                        (App.get(reasonMissedDose).equals(getResources().getString(R.string.pmdt_treatment_supporter_did_not_come)) ? "TREATMENT SUPPORTER DID NOT COME" :
                                (App.get(reasonMissedDose).equals(getResources().getString(R.string.refused)) ? "REFUSED" :
                                    (App.get(reasonMissedDose).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "OTHER REASON MISSED DOSE"))))});

        if (reasonMissedDoseOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON MISSED DOSE", App.get(reasonMissedDoseOther)});

        observations.add(new String[]{"FAMILY PRACTICE INFECTION CONTROL MEASURES", App.get(practiceInfectionControl).equals(getResources().getString(R.string.pmdt_always)) ? "ALWAYS" :
                (App.get(practiceInfectionControl).equals(getResources().getString(R.string.pmdt_sometimes)) ? "SOMETIMES" :
                        (App.get(practiceInfectionControl).equals(getResources().getString(R.string.pmdt_rarely)) ? "RARELY" :
                                (App.get(practiceInfectionControl).equals(getResources().getString(R.string.unknown)) ? "NEVER" : "UNKNOWN")))});


        observations.add(new String[]{"NUMBER OF CONTACTS", App.get(contactsCount)});

        observations.add(new String[]{"FILLED CONTACT REGISTRY", App.get(filledContactRegistry).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
*/
/*
        observations.add(new String[]{"COLLECTED SPUTUM SAMPLES COUNT", App.get(collectedSampleCount)});

        observations.add(new String[]{"BASELINE SCREENING FOR FAMILY", App.get(filledBaselineScreening).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(filledBaselineScreening).equals(getResources().getString(R.string.no)) ? "NO" : "UNKNOWN")});

        observations.add(new String[]{"PATIENT SATISFIED WITH TREATMENT SUPPORTER", App.get(patientSatisfactionTs).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        observations.add(new String[]{"ADVERSE EVENT REPORTED", App.get(reportedAdverseEvent).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(reportedAdverseEvent).equals(getResources().getString(R.string.no)) ? "NO" : "REFUSED")});*/

/*
        String actionAdverseEventString = "";
        for (CheckBox cb : actionAdverseEvents.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_arranged_followup)))
                actionAdverseEventString = actionAdverseEventString + "ARRANGED FOLLOW UP NEXT DAY FOR CLINICAL REVIEW" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_consulted_advised_medications)))
                actionAdverseEventString = actionAdverseEventString + "CONSULTED ON PHONE WITH TB DOCTOR AND ADVISED ANCILLARY MEDICATIONS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_consulted_arranged_transfer)))
                actionAdverseEventString = actionAdverseEventString + "CONSULTED ON PHONE WITH TB DOCTOR AND ARRANGED TRANSFER TO REFERRAL HOSPITAL" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_no_action_required)))
                actionAdverseEventString = actionAdverseEventString + "NO ACTION WAS REQUIRED" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_other)))
                actionAdverseEventString = actionAdverseEventString + "OTHER ACTION ADVERSE EVENT" + " ; ";

        }
        observations.add(new String[]{"ACTION ADVERSE EVENT", actionAdverseEventString});


        if (actionAdverseEventOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER ACTION ADVERSE EVENT", App.get(actionAdverseEventOther)});

        observations.add(new String[]{"ADEQUATE NUTRITION", App.get(nutritionAdequate).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        observations.add(new String[]{"PATIENT PSYCHOLOGICAL ISSUE", App.get(psychologicalIssue).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        observations.add(new String[]{"NEED WEEKLY COUNSELING CALLS", App.get(needCounsellingWeekly).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
*/

   /*     String counselingTypeString = "";
        for (CheckBox cb : counsellingTypes.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_adherence)))
                counselingTypeString = counselingTypeString + "ADHERENCE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_infection_control)))
                counselingTypeString = counselingTypeString + "INFECTION CONTROL COUNSELLING" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_adverse_events)))
                counselingTypeString = counselingTypeString + "ADVERSE EVENTS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_psychosocial_issues)))
                counselingTypeString = counselingTypeString + "PSYCHOSOCIAL COUNSELING" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_substance_abuse)))
                counselingTypeString = counselingTypeString + "SUBSTANCE ABUSE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_other)))
                counselingTypeString = counselingTypeString + "OTHER COUNSELING TYPE" + " ; ";

        }
        observations.add(new String[]{"COUNSELING TYPE", counselingTypeString});

        if (counsellingTypeOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER COUNSELING TYPE", App.get(counsellingTypeOther)});

        observations.add(new String[]{"NEED MENTAL HEALTH COUNSELING", App.get(needMentalHealthCounselling).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        observations.add(new String[]{"REMINDER FOR FOLLOWUP VISIT", App.get(remindedNextFollowup).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        String counselingProvidedString = "";
        for (CheckBox cb : patientCounsellingTypes.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_treatment_adherence)))
                counselingProvidedString = counselingProvidedString + "TREATMENT ADHERENCE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_medication_intake)))
                counselingProvidedString = counselingProvidedString + "MEDICATION INTAKE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_infection_control)))
                counselingProvidedString = counselingProvidedString + "INFECTION CONTROL" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_nutritional_needs)))
                counselingProvidedString = counselingProvidedString + "NUTRITIONAL NEEDS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_family_planning)))
                counselingProvidedString = counselingProvidedString + "FAMILY PLANNING" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_contact_screening)))
                counselingProvidedString = counselingProvidedString + "CONTACT SCREENING" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_smoking_cessation)))
                counselingProvidedString = counselingProvidedString + "SMOKING CESSATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_substance_abuse)))
                counselingProvidedString = counselingProvidedString + "SUBSTANCE ABUSE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_importance_followup_visits)))
                counselingProvidedString = counselingProvidedString+ "IMPORTANCE OF REGULAR MONTHLY FOLLOW UP" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_sputum_sample_submission_each_visit)))
                counselingProvidedString = counselingProvidedString + "SPUTUM SAMPLE SUBMISSION ON EACH VISIT" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_mental_health_well_being)))
                counselingProvidedString = counselingProvidedString + "MENTAL HEALTH WELL BEING" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_adverse_event_management)))
                counselingProvidedString = counselingProvidedString + "ADVERSE EVENTS AND THEIR MANAGEMENT" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_aao_tb_mitao_helpline_use)))
                counselingProvidedString = counselingProvidedString + "AAO TB MITAO HELPLINE USE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_educational_material_to_patient)))
                counselingProvidedString = counselingProvidedString + "EDUCATIONAL MATERIAL FOR PATIENT" + " ; ";

        }
        observations.add(new String[]{"COUNSELING PROVIDED FOR", counselingProvidedString});

        observations.add(new String[]{"HOME VISIT SUMMARY", App.get(homeVisitSummary)});*/

//        personAttribute.put("Health Center",serverService.getLocationUuid(App.getLocation()));

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

//                    if(App.get(followupRequired).equals(getString(R.string.no))) {
                    if (snackbar != null) snackbar.dismiss();
                    snackbar = Snackbar.make(mainContent, getResources().getString(R.string.pmdt_gene_xpert_alert), Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
//                    }

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

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        for (CheckBox cb : actionAdverseEvents.getCheckedBoxes()) {

            if (App.get(cb).equals(getResources().getString(R.string.pmdt_other))) {
                if (cb.isChecked()) {
                    actionAdverseEventOther.setVisibility(View.VISIBLE);
                } else {
                    actionAdverseEventOther.setVisibility(View.GONE);
                }
            }
        }

        for (CheckBox cb : counsellingTypes.getCheckedBoxes()) {
            if (App.get(cb).equals(getResources().getString(R.string.pmdt_other))) {
                if (cb.isChecked()) {
                    counsellingTypeOther.setVisibility(View.VISIBLE);
                } else {
                    counsellingTypeOther.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (group == missedDose.getRadioGroup()) {
            if (App.get(missedDose).equals(getResources().getString(R.string.yes))) {
                reasonMissedDose.setVisibility(View.VISIBLE);

                if (App.get(reasonMissedDose).equals(getResources().getString(R.string.pmdt_other)))
                    reasonMissedDoseOther.setVisibility(View.VISIBLE);

            } else {
                reasonMissedDose.setVisibility(View.GONE);
                reasonMissedDoseOther.setVisibility(View.GONE);
            }
        } else if (group == reasonMissedDose.getRadioGroup()) {
            if (App.get(reasonMissedDose).equals(getResources().getString(R.string.pmdt_other)))
                reasonMissedDoseOther.setVisibility(View.VISIBLE);
            else
                reasonMissedDoseOther.setVisibility(View.GONE);

        } else if (group == reportedAdverseEvent.getRadioGroup()) {
            if (App.get(reportedAdverseEvent).equals(getResources().getString(R.string.yes))) {
                actionAdverseEvents.setVisibility(View.VISIBLE);

            } else {
                actionAdverseEvents.setVisibility(View.GONE);
                actionAdverseEventOther.setVisibility(View.GONE);
            }
        } else if (group == needCounsellingWeekly.getRadioGroup()) {
            if (App.get(needCounsellingWeekly).equals(getResources().getString(R.string.yes))) {
                counsellingTypes.setVisibility(View.VISIBLE);

            } else {
                counsellingTypes.setVisibility(View.GONE);
                counsellingTypeOther.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public void refill(int encounterId) {
        super.refill(encounterId);
        refillFlag = true;
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
