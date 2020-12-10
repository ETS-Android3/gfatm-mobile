package com.ihsinformatics.gfatmmobile.common;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.Barcode;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class ClinicianEvaluation extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    Boolean dateChoose = false;

    TitledSpinner patientSource;
    TitledEditText otherPatientSource;
    TitledEditText indexPatientId;
    Button scanQRCode;
    TitledRadioGroup childDiagnosedPresumptive;
    TitledEditText externalPatientId;
    TitledEditText weight;
    TitledEditText height;
    TitledEditText bmi;
    TitledEditText muac;
    TitledEditText weightPercentileEditText;
    //TitledSpinner weightPercentile;

    TitledRadioGroup cough;
    TitledRadioGroup coughDuration;
    TitledRadioGroup haemoptysis;
    TitledRadioGroup difficultyBreathing;
    TitledRadioGroup fever;
    TitledRadioGroup feverDuration;
    TitledRadioGroup weightLoss;
    TitledRadioGroup nightSweats;
    TitledRadioGroup appetite;
    TitledRadioGroup lethargy;
    TitledRadioGroup swollenJoints;
    TitledRadioGroup backPain;
    TitledRadioGroup adenopathy;
    TitledRadioGroup vomiting;
    TitledRadioGroup giSymptoms;
    TitledRadioGroup chronicTwoWeeks;
    TitledRadioGroup abdominalPainTwoWeeks;
    TitledEditText otherGISymptoms;
    TitledRadioGroup alteredLevelConscious;
    TitledRadioGroup performedPhysicalExamination;
    TitledCheckBoxes systemsExamined;


    TitledRadioGroup closeContact;
    TitledCheckBoxes closeContactType;
    TitledEditText otherContactType;
    TitledRadioGroup exposurePoint1;
    TitledRadioGroup exposurePoint2;
    TitledRadioGroup exposurePoint3;
    TitledRadioGroup exposurePoint4;
    TitledRadioGroup exposurePoint5;
    TitledRadioGroup exposurePoint6;
    TitledRadioGroup exposurePoint7;
    TitledRadioGroup exposurePoint8;
    TitledRadioGroup exposurePoint9;
    TitledRadioGroup exposurePoint10;
    TitledEditText exposureScore;
    TitledRadioGroup patientVisitFacility;
    TitledRadioGroup generalAppearence;
    TitledEditText generalAppearenceExplanation;
    TitledRadioGroup heent;
    TitledEditText heentExplanation;
    TitledRadioGroup lymphnode;
    TitledEditText lymphnodeExplanation;
    TitledRadioGroup spine;
    TitledEditText spineExplanation;
    TitledRadioGroup joints;
    TitledEditText jointsExplanation;
    TitledRadioGroup skin;
    TitledEditText skinExplanation;
    TitledRadioGroup chest;
    TitledEditText chestExplanation;
    TitledRadioGroup abdominal;
    TitledEditText abdominalExplanation;
    TitledEditText others;
    TitledRadioGroup bcg;
    TitledRadioGroup tbHistory;
    TitledRadioGroup tbMedication;

    TitledCheckBoxes comorbidCondition;
    CheckBox otherComorbidCondition;
    TitledEditText otherCondition;
    TitledRadioGroup smokingHistory;
    TitledEditText dailyCigarettesIntake;
    TitledRadioGroup conclusion;
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


    //Next Appointment
    TitledButton returnVisitDate;
    MyAdapter adapter;
    MyLinearLayout linearLayout2;
    MyLinearLayout linearLayout2a;
    MyLinearLayout linearLayout1;
    MyLinearLayout linearLayout3;

    Boolean refillFlag = false;
    ScrollView scrollView;
    private TitledRadioGroup skinFinding;
    private String bmiResult;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 5;
        formName = Forms.CLINICIAN_EVALUATION_FORM;
        form = Forms.clinicianEvaluationForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        adapter = new MyAdapter();
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(pageCount - 1);
        formNameView.setText(formName);

        initViews();

        groups = new ArrayList<ViewGroup>();

        pageCount = 5;
        adapter.notifyDataSetChanged();

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

        if (App.get(childDiagnosedPresumptive).equals(getResources().getString(R.string.no))) {
            pageCount = 1;
            adapter.notifyDataSetChanged();
        } else {
            pageCount = 5;
            adapter.notifyDataSetChanged();
        }
        gotoFirstPage();

        return mainContent;
    }

    @Override
    public void initViews() {

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        patientSource = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.patient_source), getResources().getStringArray(R.array.patient_source_options), "", App.HORIZONTAL, true, "PATIENT SOURCE", new String[]{"", "IDENTIFIED PATIENT THROUGH SCREENING", "PATIENT REFERRED", "TUBERCULOSIS CONTACT", "WALK IN", "OTHER PATIENT SOURCE"});
        otherPatientSource = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER PATIENT SOURCE");
        indexPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_id), "", "", RegexUtil.idLength, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, App.HORIZONTAL, true, "PATIENT ID OF INDEX CASE");
        scanQRCode = new Button(context);
        scanQRCode.setText("Scan QR Code");
        childDiagnosedPresumptive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_mo_think_child_presumptive), getResources().getStringArray(R.array.yes_no_options), null, App.VERTICAL, App.VERTICAL, true, "CHILD DIAGNOSED PRESUMPTIVE BY MO", getResources().getStringArray(R.array.yes_no_list_concept));
        externalPatientId = new TitledEditText(context, null, getResources().getString(R.string.external_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        weight = new TitledEditText(context, null, getResources().getString(R.string.pet_weight), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true, "WEIGHT (KG)");
        height = new TitledEditText(context, null, getResources().getString(R.string.pet_height), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true, "HEIGHT (CM)");
        bmi = new TitledEditText(context, null, getResources().getString(R.string.pet_bmi), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        muac = new TitledEditText(context, null, getResources().getString(R.string.pet_muac), "", "", 3, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false, "MID-UPPER ARM CIRCUMFERENCE");
        weightPercentileEditText = new TitledEditText(context, null, getResources().getString(R.string.pet_weight_percentile), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "WEIGHT PERCENTILE GROUP");
        linearLayout1 = new MyLinearLayout(context, getResources().getString(R.string.symptom_screen), App.VERTICAL);
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_cough), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "COUGH", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        coughDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_cough_duration), getResources().getStringArray(R.array.pet_cough_durations), getResources().getString(R.string.pet_less_than_2_weeks), App.VERTICAL, App.VERTICAL, true, "COUGH DURATION", new String[]{"COUGH LASTING LESS THAN 2 WEEKS", "COUGH LASTING MORE THAN 2 WEEKS", "COUGH LASTING MORE THAN 3 WEEKS", "UNKNOWN", "REFUSED"});
        haemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_haemoptysis), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "HEMOPTYSIS", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        difficultyBreathing = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_difficulty_breathing), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "DYSPNEA", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_fever), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "FEVER", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        feverDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_fever_duration), getResources().getStringArray(R.array.pet_cough_durations), getResources().getString(R.string.pet_less_than_2_weeks), App.VERTICAL, App.VERTICAL, true, "FEVER DURATION", new String[]{"COUGH LASTING LESS THAN 2 WEEKS", "COUGH LASTING MORE THAN 2 WEEKS", "COUGH LASTING MORE THAN 3 WEEKS", "UNKNOWN", "REFUSED"});
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_weight_loss), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "WEIGHT LOSS", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_night_sweats), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "NIGHT SWEATS", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        appetite = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_child_appetite), getResources().getStringArray(R.array.ctb_appetite_list), getResources().getString(R.string.unknown), App.HORIZONTAL, App.VERTICAL, true, "APPETITE", new String[]{"POOR APPETITE", "OK", "REFUSED", "UNKNOWN"});
        lethargy = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_lethargy), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "LETHARGY", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        swollenJoints = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_swollen_joints), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "JOINT SWELLING", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        backPain = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_back_pain), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "BACK PAIN", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        adenopathy = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adenopathy), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "ADENOPATHY", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        vomiting = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_vomiting_without_gi_symptoms), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "VOMITING", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        giSymptoms = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_gi_symptoms), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "GASTROINTESTINAL SYMPTOM", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        chronicTwoWeeks = new TitledRadioGroup(context, null, getResources().getString(R.string.chronic_diarrhea_two_weeks), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "CHRONIC DIARRHEA LONGER THAN 2 WEEKS", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        abdominalPainTwoWeeks = new TitledRadioGroup(context, null, getResources().getString(R.string.abdominal_pain_longer_than_2_weeks), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "ABDOMINAL PAIN LONGER THAN 2 WEEKS", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        alteredLevelConscious = new TitledRadioGroup(context, null, getResources().getString(R.string.altered_level_conscious), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "ALTERATION OF CONSCIOUSNESS / IRRITABILITY / SEIZURES", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        otherGISymptoms = new TitledEditText(context, null, getResources().getString(R.string.gi_other_symptoms), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHERS GASTROINTESTINAL SYMPTOM");
        skinFinding = new TitledRadioGroup(context, null, getResources().getString(R.string.common_skin_finding), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "SKIN / SUBCUTANEOUS FINDINGS", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});


        linearLayout1.addView(cough);
        linearLayout1.addView(coughDuration);
        linearLayout1.addView(haemoptysis);
        linearLayout1.addView(difficultyBreathing);
        linearLayout1.addView(fever);
        linearLayout1.addView(feverDuration);
        linearLayout1.addView(weightLoss);
        linearLayout1.addView(nightSweats);
        linearLayout1.addView(appetite);
        linearLayout1.addView(lethargy);
        linearLayout1.addView(swollenJoints);
        linearLayout1.addView(backPain);
        linearLayout1.addView(adenopathy);
        linearLayout1.addView(vomiting);
        linearLayout1.addView(giSymptoms);
        linearLayout1.addView(chronicTwoWeeks);
        linearLayout1.addView(abdominalPainTwoWeeks);
        linearLayout1.addView(otherGISymptoms);
        linearLayout1.addView(alteredLevelConscious);
        linearLayout1.addView(skinFinding);

        linearLayout2 = new MyLinearLayout(context, "Family History", App.VERTICAL);
        closeContact = new TitledRadioGroup(context, null, getResources().getString(R.string.close_contact), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true, "CLOSE CONTACT WITH TB PATIENT", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        closeContactType = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_close_contact_type), getResources().getStringArray(R.array.close_contact_type_list_sd), null, App.VERTICAL, App.VERTICAL, true,"CLOSE CONTACT WITH PATIENT", new String[]{"MOTHER", "FATHER", "BROTHER", "SISTER", "SON", "DAUGHTER", "PATERNAL GRANDFATHER", "PATERNAL GRANDMOTHER", "MATERNAL GRANDFATHER", "MATERNAL GRANDMOTHER", "UNCLE", "AUNT", "OTHER"});
        otherContactType = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_title), "", "", 20, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "OTHER CONTACT TYPE");
        exposurePoint1 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_1), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "INDEX CASE MOTHER OF CONTACT", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        exposurePoint2 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_2), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "INDEX CASE PRIMARY CARETAKER OF CONTACT", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        exposurePoint3 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_3), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "INDEX CASE SHARES BED WITH CONTACT", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        exposurePoint4 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_4), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "INDEX CASE SHARES BEDROOM WITH CONTACT", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        exposurePoint5 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_5), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "INDEX CASE LIVES WITH CONTACT", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        exposurePoint6 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_6), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "INDEX CASE MEETS CONTACT DAILY", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        exposurePoint7 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_7), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "INDEX CASE COUGHING", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        exposurePoint8 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_8), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "INDEX CASE WITH P-TB", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        exposurePoint9 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_9), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "INDEX CASE SMEAR POSITIVE", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        exposurePoint10 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_10), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "MULTIPLE INDEX CASES IN HOUSEHOLD", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        exposureScore = new TitledEditText(context, null, getResources().getString(R.string.pet_exposure_score), "0", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_NUMBER_FLAG_DECIMAL, App.HORIZONTAL, false);
        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.pet_doctor_notes), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        clincianNote.getEditText().setSingleLine(false);
        clincianNote.getEditText().setMinimumHeight(150);
        conclusion = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_conclusion), getResources().getStringArray(R.array.ctb_conclusion_list), null, App.HORIZONTAL, App.VERTICAL, true, "CONCLUSION", new String[]{"TB PRESUMPTIVE CONFIRMED", "NOT A TB PRESUMPTIVE"});

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


        patientVisitFacility = new TitledRadioGroup(context, null, getResources().getString(R.string.patient_visit_facility), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL, true, "CLINICAL FOLLOWUP NEEDED", getResources().getStringArray(R.array.yes_no_list_concept));
        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.return_visit_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);

        linearLayout2.addView(closeContact);
        linearLayout2.addView(closeContactType);
        linearLayout2.addView(otherContactType);
        linearLayout2.addView(exposurePoint1);
        linearLayout2.addView(exposurePoint2);
        linearLayout2.addView(exposurePoint3);
        linearLayout2.addView(exposurePoint4);
        linearLayout2.addView(exposurePoint5);
        linearLayout2.addView(exposurePoint6);
        linearLayout2.addView(exposurePoint7);
        linearLayout2.addView(exposurePoint8);
        linearLayout2.addView(exposurePoint9);
        linearLayout2.addView(exposurePoint10);
        linearLayout2.addView(exposureScore);
        linearLayout2.addView(conclusion);
        linearLayout2.addView(clincianNote);
        linearLayout2.addView(patientReferred);
        linearLayout2.addView(referredTo);
        linearLayout2.addView(referalReasonPsychologist);
        linearLayout2.addView(otherReferalReasonPsychologist);
        linearLayout2.addView(referalReasonSupervisor);
        linearLayout2.addView(otherReferalReasonSupervisor);
        linearLayout2.addView(referalReasonCallCenter);
        linearLayout2.addView(otherReferalReasonCallCenter);
        linearLayout2.addView(referalReasonClinician);
        linearLayout2.addView(otherReferalReasonClinician);
        linearLayout2.addView(patientVisitFacility);
        linearLayout2.addView(returnVisitDate);

        linearLayout2a = new MyLinearLayout(context, getResources().getString(R.string.pet_physical_examination), App.VERTICAL);

        performedPhysicalExamination = new TitledRadioGroup(context, null, getResources().getString(R.string.performed_physical_examination), getResources().getStringArray(R.array.performed_physical_exam_list), getResources().getString(R.string.not_performed), App.HORIZONTAL, App.VERTICAL, true, "PHYSICAL EXAMINATION PERFORMED", new String[]{"PERFORMED", "NOT ASSESSED"});
        systemsExamined = new TitledCheckBoxes(context, null, getResources().getString(R.string.systems_were_examined), getResources().getStringArray(R.array.system_examined_list), null, App.VERTICAL, App.VERTICAL, true, "SYSTEM EXAMINED", new String[]{"GENERAL APPEARANCE", "HEAD, EARS, EYES, NOSE AND THROAT", "LYMPH NODE EXAMIMATION OF NECK, AXILLA AND GORIN", "SPINE", "JOINTS", "SKIN", "CHEST EXAMINATION (TEXT)", "ABDOMINAL EXAMINATION (TEXT)"});
        generalAppearence = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_general_appearance), getResources().getStringArray(R.array.suggestive_not_sugg_unremarkable), getResources().getString(R.string.normal_unremarkable), App.VERTICAL, App.VERTICAL, true, "GENERAL APPEARANCE", new String[]{"NORMAL/UNREMARKABLE", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        generalAppearenceExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "GENERAL APPEARANCE EXPLANATION");
        generalAppearenceExplanation.getEditText().setSingleLine(false);
        generalAppearenceExplanation.getEditText().setMinimumHeight(150);
        heent = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_heent), getResources().getStringArray(R.array.suggestive_not_sugg_unremarkable), getResources().getString(R.string.normal_unremarkable), App.VERTICAL, App.VERTICAL, true, "HEAD, EARS, EYES, NOSE AND THROAT INTERPRETATION", new String[]{"NORMAL/UNREMARKABLE", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        heentExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "HEAD, EARS, EYES, NOSE AND THROAT DESCRIPTION");
        heentExplanation.getEditText().setSingleLine(false);
        heentExplanation.getEditText().setMinimumHeight(150);
        lymphnode = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_lymphnode), getResources().getStringArray(R.array.suggestive_not_sugg_unremarkable), getResources().getString(R.string.normal_unremarkable), App.VERTICAL, App.VERTICAL, true, "LYMPH NODE EXAMIMATION OF NECK, AXILLA AND GORIN INTERPRETATION", new String[]{"NORMAL/UNREMARKABLE", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        lymphnodeExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "LYMPH NODE EXAMIMATION OF NECK, AXILLA AND GORIN");
        lymphnodeExplanation.getEditText().setSingleLine(false);
        lymphnodeExplanation.getEditText().setMinimumHeight(150);
        spine = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_spine), getResources().getStringArray(R.array.suggestive_not_sugg_unremarkable), getResources().getString(R.string.normal_unremarkable), App.VERTICAL, App.VERTICAL, true, "SPINAL INTERPRETATION", new String[]{"NORMAL/UNREMARKABLE", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        spineExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "SPINAL PHYSICAL EXAMINATION (TEXT)");
        spineExplanation.getEditText().setSingleLine(false);
        spineExplanation.getEditText().setMinimumHeight(150);
        joints = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_joints), getResources().getStringArray(R.array.suggestive_not_sugg_unremarkable), getResources().getString(R.string.normal_unremarkable), App.VERTICAL, App.VERTICAL, true, "JOINTS INTERPRETATION", new String[]{"NORMAL/UNREMARKABLE", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        jointsExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "JOINTS PHYSICAL EXAMINATION (TEXT)");
        jointsExplanation.getEditText().setSingleLine(false);
        jointsExplanation.getEditText().setMinimumHeight(150);
        skin = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_skin), getResources().getStringArray(R.array.suggestive_not_sugg_unremarkable), getResources().getString(R.string.normal_unremarkable), App.VERTICAL, App.VERTICAL, true, "SKIN INTERPRETATION", new String[]{"NORMAL/UNREMARKABLE", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        skinExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "SKIN EXAMINATION (TEXT)");
        skinExplanation.getEditText().setSingleLine(false);
        skinExplanation.getEditText().setMinimumHeight(150);
        chest = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_chest_examination), getResources().getStringArray(R.array.suggestive_not_sugg_unremarkable), getResources().getString(R.string.normal_unremarkable), App.VERTICAL, App.VERTICAL, true, "CHEST EXAMINATION INTERPRETATION", new String[]{"NORMAL/UNREMARKABLE", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        chestExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "CHEST EXAMINATION (TEXT)");
        chestExplanation.getEditText().setSingleLine(false);
        chestExplanation.getEditText().setMinimumHeight(150);
        abdominal = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_abdominal_examination), getResources().getStringArray(R.array.suggestive_not_sugg_unremarkable), getResources().getString(R.string.normal_unremarkable), App.VERTICAL, App.VERTICAL, true, "ABDOMEN INTERPRETATION", new String[]{"NORMAL/UNREMARKABLE", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        abdominalExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "ABDOMINAL EXAMINATION (TEXT)");
        abdominalExplanation.getEditText().setSingleLine(false);
        abdominalExplanation.getEditText().setMinimumHeight(150);
        others = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "FREE TEXT COMMENT");
        bcg = new TitledRadioGroup(mainContent.getContext(), "", getResources().getString(R.string.ctb_bcg), getResources().getStringArray(R.array.yes_no_unknown_refused_options), "", App.VERTICAL, App.VERTICAL, true, "BACILLUS CALMETTE–GUÉRIN VACCINE", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        tbHistory = new TitledRadioGroup(mainContent.getContext(), "", getResources().getString(R.string.tb_history), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL, true, "HISTORY OF TUBERCULOSIS", getResources().getStringArray(R.array.yes_no_list_concept));
        tbMedication = new TitledRadioGroup(mainContent.getContext(), "", getResources().getString(R.string.tb_medication), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL, true, "PATIENT TAKEN TB MEDICATION BEFORE", getResources().getStringArray(R.array.yes_no_list_concept));

        linearLayout2a.addView(performedPhysicalExamination);
        linearLayout2a.addView(systemsExamined);

        linearLayout2a.addView(generalAppearence);
        linearLayout2a.addView(generalAppearenceExplanation);
        linearLayout2a.addView(heent);
        linearLayout2a.addView(heentExplanation);
        linearLayout2a.addView(lymphnode);
        linearLayout2a.addView(lymphnodeExplanation);
        linearLayout2a.addView(spine);
        linearLayout2a.addView(spineExplanation);
        linearLayout2a.addView(joints);
        linearLayout2a.addView(jointsExplanation);
        linearLayout2a.addView(skin);
        linearLayout2a.addView(skinExplanation);
        linearLayout2a.addView(chest);
        linearLayout2a.addView(chestExplanation);
        linearLayout2a.addView(abdominal);
        linearLayout2a.addView(abdominalExplanation);
        linearLayout2a.addView(others);
        linearLayout2a.addView(bcg);
        linearLayout2a.addView(tbHistory);
        linearLayout2a.addView(tbMedication);

        linearLayout3 = new MyLinearLayout(context, "Medical History", App.VERTICAL);
        comorbidCondition = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_comorbid_condition), getResources().getStringArray(R.array.pet_comorbid_conditions), null, App.VERTICAL, App.VERTICAL, true, "CO-MORBID CONDITIONS", new String[]{"DIABETES MELLITUS", "HYPERTENSION", "CHRONIC RENAL DISEASE", "CARDIOVASCULAR DISEASE", "CONGENITAL DISORDERS", "CIRRHOSIS AND CHRONIC LIVER DISEASE", "ASTHMA", "EMPHYSEMA", "CHRONIC OBSTRUCTIVE PULMONARY DISEASE", "HUMAN IMMUNODEFICIENCY VIRUS", "OTHER", "NOT APPLICABLE"});
        otherCondition = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 15, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER DISEASE");
        smokingHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_smoking_history), getResources().getStringArray(R.array.yes_no_unknown_options), getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true, "SMOKING HISTORY", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        dailyCigarettesIntake = new TitledEditText(context, null, getResources().getString(R.string.pet_daily_cigarettes_cosumption), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true, "DAILY CIGARETTE USE");


        linearLayout3.addView(comorbidCondition);
        linearLayout3.addView(otherCondition);
        linearLayout3.addView(smokingHistory);
        linearLayout3.addView(dailyCigarettesIntake);
//       linearLayout3.addView(clincianNote);

        views = new View[]{formDate.getButton(), patientSource.getSpinner(), otherPatientSource.getEditText(), indexPatientId.getEditText(), externalPatientId.getEditText(), weight.getEditText(), height.getEditText(), bmi.getEditText(), muac.getEditText(),
                childDiagnosedPresumptive.getRadioGroup(), muac.getEditText(),
                cough.getRadioGroup(), coughDuration.getRadioGroup(), haemoptysis.getRadioGroup(), difficultyBreathing.getRadioGroup(), fever.getRadioGroup(), feverDuration.getRadioGroup(),
                weightLoss.getRadioGroup(), nightSweats.getRadioGroup(), lethargy.getRadioGroup(), swollenJoints.getRadioGroup(), backPain.getRadioGroup(), adenopathy.getRadioGroup(),
                vomiting.getRadioGroup(), giSymptoms.getRadioGroup(), chronicTwoWeeks.getRadioGroup(), abdominalPainTwoWeeks.getRadioGroup(), otherGISymptoms.getEditText(), alteredLevelConscious.getRadioGroup(), exposurePoint1.getRadioGroup(), exposurePoint2.getRadioGroup(), exposurePoint3.getRadioGroup(),
                exposurePoint4.getRadioGroup(), exposurePoint5.getRadioGroup(), exposurePoint6.getRadioGroup(), exposurePoint7.getRadioGroup(), exposurePoint8.getRadioGroup(), exposurePoint9.getRadioGroup(),
                exposurePoint10.getRadioGroup(), exposureScore.getEditText(),
                generalAppearence.getRadioGroup(), generalAppearenceExplanation.getEditText(), heent.getRadioGroup(), heentExplanation.getEditText(), lymphnode.getRadioGroup(), lymphnodeExplanation.getEditText(),
                spine.getRadioGroup(), spineExplanation.getEditText(), joints.getRadioGroup(), jointsExplanation.getEditText(), jointsExplanation.getEditText(), skin.getRadioGroup(), skinExplanation.getEditText(),
                chest.getRadioGroup(), chestExplanation.getEditText(), abdominal.getRadioGroup(), abdominal.getRadioGroup(), bcg.getRadioGroup(), comorbidCondition,
                otherCondition.getEditText(), clincianNote.getEditText(), weightPercentileEditText.getEditText(), smokingHistory.getRadioGroup(),
                dailyCigarettesIntake.getEditText(), systemsExamined, performedPhysicalExamination.getRadioGroup(),
                conclusion.getRadioGroup(), patientVisitFacility.getRadioGroup(), skinFinding.getRadioGroup(),
                patientReferred.getRadioGroup(), referredTo, referalReasonPsychologist, otherReferalReasonPsychologist.getEditText(), referalReasonSupervisor, otherReferalReasonSupervisor.getEditText(),
                referalReasonCallCenter, otherReferalReasonCallCenter.getEditText(), referalReasonClinician, otherReferalReasonClinician.getEditText(), tbHistory.getRadioGroup(), tbMedication.getRadioGroup()};

        viewGroups = new View[][]{{formDate, patientSource, otherPatientSource, externalPatientId, indexPatientId, scanQRCode, childDiagnosedPresumptive, weight, height, bmi, muac, weightPercentileEditText},
                {linearLayout1},
                {linearLayout2a},
                {linearLayout3},
                {linearLayout2}};

        scanQRCode.setOnClickListener(this);

        weight.getEditText().addTextChangedListener(new TextWatcher() {
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

                if (!App.get(weight).equals("")) {
                    Double w = Double.parseDouble(App.get(weight));
                    if (w < 0.5 || w > 700.0)
                        weight.getEditText().setError(getString(R.string.pet_invalid_weight_range));
                    else
                        weight.getEditText().setError(null);
                }

                if (App.get(weight).equals("") || App.get(height).equals(""))
                    bmi.getEditText().setText("");
                else {

                    Double w = Double.parseDouble(App.get(weight));
                    Double h = Double.parseDouble(App.get(height));

                    Double heightInMeter = h * 1.0 / 100;

                    Double BMI = w * 1.0 / (heightInMeter * heightInMeter);
                    String result = String.format("%.2f", BMI);

                    String bmiCategory = "";

                    if (BMI < 18.5)
                        bmiCategory = getResources().getString(R.string.pet_underweight);
                    else if (BMI >= 18.5 && BMI <= 24.9)
                        bmiCategory = getResources().getString(R.string.pet_normal);
                    else if (BMI >= 25 && BMI <= 29.9)
                        bmiCategory = getResources().getString(R.string.pet_overweight);
                    else if (BMI >= 30 && BMI <= 39.9)
                        bmiCategory = getResources().getString(R.string.pet_obese);
                    else if (BMI >= 40)
                        bmiCategory = getResources().getString(R.string.pet_very_obese);

                    if (BMI > 200) {
                        bmi.getEditText().setError(getString(R.string.pet_invalid_bmi));
                        bmiCategory = "Invalid";
                    } else bmi.getEditText().setError(null);

                    bmi.getEditText().setText(result + "   -   " + bmiCategory);

                }

                if (weightPercentileEditText.getVisibility() == View.VISIBLE && !App.get(weight).equals("")) {
                    String percentile = serverService.getPercentile(App.get(weight));
                    weightPercentileEditText.getEditText().setText(percentile);

                } else {
                    weightPercentileEditText.getEditText().setText("");
                }


            }
        });
        height.getEditText().addTextChangedListener(new TextWatcher() {
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

                if (!App.get(height).equals("")) {
                    Double h = Double.parseDouble(App.get(height));
                    if (h < 10.0 || h > 272.0)
                        height.getEditText().setError(getString(R.string.pet_invalid_height_range));
                    else
                        height.getEditText().setError(null);
                }

                if (App.get(weight).equals("") || App.get(height).equals(""))
                    bmi.getEditText().setText("");
                else {

                    Double w = Double.parseDouble(App.get(weight));
                    Double h = Double.parseDouble(App.get(height));

                    Double heightInMeter = h * 1.0 / 100;

                    Double BMI = w * 1.0 / (heightInMeter * heightInMeter);
                    bmiResult = String.format("%.2f", BMI);

                    String bmiCategory = "";

                    if (BMI < 18.5)
                        bmiCategory = getResources().getString(R.string.pet_underweight);
                    else if (BMI >= 18.5 && BMI <= 24.9)
                        bmiCategory = getResources().getString(R.string.pet_normal);
                    else if (BMI >= 25 && BMI <= 29.9)
                        bmiCategory = getResources().getString(R.string.pet_overweight);
                    else if (BMI >= 30 && BMI <= 39.9)
                        bmiCategory = getResources().getString(R.string.pet_obese);
                    else if (BMI >= 40)
                        bmiCategory = getResources().getString(R.string.pet_very_obese);

                    if (BMI > 200) {
                        bmi.getEditText().setError(getString(R.string.pet_invalid_bmi));
                        bmiCategory = "Invalid";
                    } else bmi.getEditText().setError(null);

                    bmi.getEditText().setText(bmiResult + "   -   " + bmiCategory);

                }


            }
        });
        indexPatientId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (start == 5 && s.length() == 5) {
                    int i = indexPatientId.getEditText().getSelectionStart();
                    if (i == 5) {
                        indexPatientId.getEditText().setText(indexPatientId.getEditText().getText().toString().substring(0, 4));
                        indexPatientId.getEditText().setSelection(4);
                    }
                } else if (s.length() == 5 && !s.toString().contains("-")) {
                    indexPatientId.getEditText().setText(s + "-");
                    indexPatientId.getEditText().setSelection(6);
                } else if (s.length() == 7 && !RegexUtil.isValidId(App.get(indexPatientId)))
                    indexPatientId.getEditText().setError(getString(R.string.invalid_id));
                else
                    indexPatientId.getEditText().setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        weight.getEditText().addTextChangedListener(new TextWatcher() {
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

                if (!App.get(weight).equals("")) {

                    Double w = Double.parseDouble(App.get(weight));
                    if (w < 0.5 || w > 700.0)
                        weight.getEditText().setError(getString(R.string.pet_invalid_weight_range));
                    else
                        weight.getEditText().setError(null);

                    String percentile = serverService.getPercentile(App.get(weight));
                    weightPercentileEditText.getEditText().setText(percentile);

                } else {
                    weightPercentileEditText.getEditText().setText("");
                }

            }
        });
        weightPercentileEditText.getEditText().setKeyListener(null);


        bmi.getEditText().setKeyListener(null);
        exposureScore.getEditText().setKeyListener(null);
        weightPercentileEditText.getEditText().setKeyListener(null);
        patientSource.getSpinner().setOnItemSelectedListener(this);
        patientReferred.getRadioGroup().setOnCheckedChangeListener(this);
        conclusion.getRadioGroup().setOnCheckedChangeListener(this);


        for (CheckBox cb : systemsExamined.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        for (CheckBox cb2 : closeContactType.getCheckedBoxes())
            cb2.setOnCheckedChangeListener(this);

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

        returnVisitDate.getButton().setOnClickListener(this);

        View listenerViewer[] = new View[]{formDate, childDiagnosedPresumptive, cough, fever, exposurePoint1, exposurePoint2, exposurePoint3, exposurePoint4, exposurePoint5,
                exposurePoint6, exposurePoint7, exposurePoint8, exposurePoint9, exposurePoint10, abdominal, chest, giSymptoms, appetite,
                skin, joints, spine, lymphnode, heent, generalAppearence, smokingHistory, performedPhysicalExamination, patientVisitFacility, tbHistory, tbMedication, bcg, closeContact,
        };
        for (View v : listenerViewer) {

            if (v instanceof TitledButton)
                ((TitledButton) v).getButton().setOnClickListener(this);
            else if (v instanceof TitledRadioGroup)
                ((TitledRadioGroup) v).getRadioGroup().setOnCheckedChangeListener(this);

        }

        for (CheckBox checkBox : comorbidCondition.getCheckedBoxes()) {

            if (checkBox.getText().equals(getResources().getString(R.string.pet_other))) {
                otherComorbidCondition = checkBox;
                otherComorbidCondition.setOnCheckedChangeListener(this);
            }

        }


        resetViews();

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        otherPatientSource.setVisibility(View.GONE);
        coughDuration.setVisibility(View.GONE);
        haemoptysis.setVisibility(View.GONE);
        feverDuration.setVisibility(View.GONE);
        generalAppearenceExplanation.setVisibility(View.GONE);
        heentExplanation.setVisibility(View.GONE);
        lymphnodeExplanation.setVisibility(View.GONE);
        spineExplanation.setVisibility(View.GONE);
        jointsExplanation.setVisibility(View.GONE);
        skinExplanation.setVisibility(View.GONE);
        chestExplanation.setVisibility(View.GONE);
        abdominalExplanation.setVisibility(View.GONE);
        otherCondition.setVisibility(View.GONE);
        dailyCigarettesIntake.setVisibility(View.GONE);
        chronicTwoWeeks.setVisibility(View.GONE);
        abdominalPainTwoWeeks.setVisibility(View.GONE);
        otherGISymptoms.setVisibility(View.GONE);
        generalAppearence.setVisibility(View.GONE);
        lymphnode.setVisibility(View.GONE);
        heent.setVisibility(View.GONE);
        spine.setVisibility(View.GONE);
        joints.setVisibility(View.GONE);
        skin.setVisibility(View.GONE);
        chest.setVisibility(View.GONE);
        abdominal.setVisibility(View.GONE);
        systemsExamined.setVisibility(View.GONE);
        others.setVisibility(View.GONE);
        otherContactType.setVisibility(View.GONE);
        patientVisitFacility.setVisibility(View.GONE);
        returnVisitDate.setVisibility(View.GONE);
        indexPatientId.setVisibility(View.GONE);
        scanQRCode.setVisibility(View.GONE);
        closeContactType.setVisibility(View.GONE);
        referredTo.setVisibility(View.GONE);
        referalReasonPsychologist.setVisibility(View.GONE);
        otherReferalReasonPsychologist.setVisibility(View.GONE);
        referalReasonSupervisor.setVisibility(View.GONE);
        otherReferalReasonSupervisor.setVisibility(View.GONE);
        referalReasonCallCenter.setVisibility(View.GONE);
        otherReferalReasonCallCenter.setVisibility(View.GONE);
        referalReasonClinician.setVisibility(View.GONE);
        otherReferalReasonClinician.setVisibility(View.GONE);
        tbMedication.setVisibility(View.GONE);

        secondDateCalendar.set(Calendar.YEAR, formDateCalendar.get(Calendar.YEAR));
        secondDateCalendar.set(Calendar.DAY_OF_MONTH, formDateCalendar.get(Calendar.DAY_OF_MONTH));
        secondDateCalendar.set(Calendar.MONTH, formDateCalendar.get(Calendar.MONTH));
        secondDateCalendar.add(Calendar.DAY_OF_MONTH, 30);


        Calendar requiredDate = formDateCalendar.getInstance();
        requiredDate.setTime(formDateCalendar.getTime());
        requiredDate.add(Calendar.DATE, 30);
        if (requiredDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            secondDateCalendar.setTime(requiredDate.getTime());
        } else {
            requiredDate.add(Calendar.DATE, -1);
            secondDateCalendar.setTime(requiredDate.getTime());
        }

        returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        if (App.getPatient().getPerson().getAge() < 6)
            muac.setVisibility(View.VISIBLE);
        else
            muac.setVisibility(View.GONE);

        if (App.getPatient().getPerson().getAge() < 18) {
            weightPercentileEditText.setVisibility(View.VISIBLE);
        } else {
            weightPercentileEditText.setVisibility(View.GONE);
        }

        if (App.getPatient().getPerson().getAge() < 14 && !App.get(patientSource).equals(getResources().getString(R.string.contact_patient)))
            bcg.setVisibility(View.VISIBLE);
        else bcg.setVisibility(View.GONE);

        if (App.getPatient().getPerson().getAge() <= 15 && App.get(closeContact).equals(getResources().getString(R.string.yes))) {

            exposurePoint1.setVisibility(View.VISIBLE);
            exposurePoint2.setVisibility(View.VISIBLE);
            exposurePoint3.setVisibility(View.VISIBLE);
            exposurePoint4.setVisibility(View.VISIBLE);
            exposurePoint5.setVisibility(View.VISIBLE);
            exposurePoint6.setVisibility(View.VISIBLE);
            exposurePoint7.setVisibility(View.VISIBLE);
            exposurePoint8.setVisibility(View.VISIBLE);
            exposurePoint9.setVisibility(View.VISIBLE);
            exposurePoint10.setVisibility(View.VISIBLE);
            exposureScore.setVisibility(View.VISIBLE);

        } else {

            exposurePoint1.setVisibility(View.GONE);
            exposurePoint2.setVisibility(View.GONE);
            exposurePoint3.setVisibility(View.GONE);
            exposurePoint4.setVisibility(View.GONE);
            exposurePoint5.setVisibility(View.GONE);
            exposurePoint6.setVisibility(View.GONE);
            exposurePoint7.setVisibility(View.GONE);
            exposurePoint8.setVisibility(View.GONE);
            exposurePoint9.setVisibility(View.GONE);
            exposurePoint10.setVisibility(View.GONE);
            exposureScore.setVisibility(View.GONE);

        }

        String patientSourceString = serverService.getLatestObsValue(App.getPatientId(), "PATIENT SOURCE");
        if (patientSourceString != null) {
            String val = patientSourceString.equals("IDENTIFIED PATIENT THROUGH SCREENING") ? getResources().getString(R.string.screening) :
                    patientSourceString.equals("PATIENT REFERRED") ? getResources().getString(R.string.referred) :
                            patientSourceString.equals("TUBERCULOSIS CONTACT") ? getResources().getString(R.string.contact_patient) :
                                    (patientSourceString.equals("WALK IN") ? getResources().getString(R.string.walkin) : getResources().getString(R.string.ctb_other_title));
            if (val.equals(getResources().getString(R.string.ctb_other_title))) {
                otherPatientSource.setVisibility(View.VISIBLE);
            }
            patientSource.getSpinner().selectValue(val);
            patientSource.getSpinner().setEnabled(false);
        }


        String indexIDString = serverService.getLatestObsValue(App.getPatientId(), "PATIENT ID OF INDEX CASE");
        if (indexIDString != null) {
            indexPatientId.getEditText().setText(indexIDString);
            indexPatientId.setEnabled(false);
        } else {
            indexPatientId.setEnabled(true);
        }

        Boolean flag = false;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);

                refill(formId);

                flag = true;

            } else bundle.putBoolean("save", false);
        }

        if (!flag) {
            externalPatientId.getEditText().setText(App.getPatient().getExternalId());

            String indexId = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_BASELINE_SCREENING, "PATIENT ID OF INDEX CASE");
            if (indexId != null) indexPatientId.getEditText().setText(indexId);

        }

        if (!App.get(externalPatientId).equals("")) {
            externalPatientId.getEditText().setKeyListener(null);
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
    public void updateDisplay() {

        if (refillFlag) {
            refillFlag = true;
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

        }

        if (!dateChoose) {
            Calendar requiredDate = formDateCalendar.getInstance();
            requiredDate.setTime(formDateCalendar.getTime());
            requiredDate.add(Calendar.DATE, 30);

            if (requiredDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                secondDateCalendar.setTime(requiredDate.getTime());
            } else {
                requiredDate.add(Calendar.DATE, -1);
                secondDateCalendar.setTime(requiredDate.getTime());
            }
        }


        if (!(returnVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {
            Calendar dateToday = Calendar.getInstance();
            dateToday.add(Calendar.MONTH, 1);

            String formDa = returnVisitDate.getButton().getText().toString();

            if (secondDateCalendar.before(formDateCalendar)) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }

        formDate.getButton().setEnabled(true);
        returnVisitDate.getButton().setEnabled(true);
        dateChoose = false;

    }


    @Override
    public boolean validate() {

        View view = null;
        Boolean error = super.validate();


        if (!App.get(height).equals("")) {
            Double h = Double.parseDouble(App.get(height));
            if (h < 10.0 || h > 272.0) {
                height.getEditText().setError(getString(R.string.pet_invalid_height_range));
                gotoFirstPage();
                error = true;
                height.getEditText().requestFocus();
            } else {
                height.getEditText().setError(null);
                height.getQuestionView().clearFocus();
            }
        }

        if (!App.get(weight).equals("")) {
            Double w = Double.parseDouble(App.get(weight));
            if (w < 0.5 || w > 700.0) {
                weight.getEditText().setError(getString(R.string.pet_invalid_weight_range));
                gotoFirstPage();
                error = true;
                weight.getEditText().requestFocus();
            } else {
                weight.getEditText().setError(null);
                weight.getQuestionView().clearFocus();
            }
        }

        if (!App.get(bmi).equals("")) {
            if (App.get(bmi).contains("Invalid")) {

                bmi.getEditText().setError(getString(R.string.pet_invalid_bmi));
                gotoFirstPage();
                error = true;
                bmi.getEditText().requestFocus();

            } else {
                bmi.getEditText().setError(null);
                bmi.getQuestionView().clearFocus();
            }
        }

        if (indexPatientId.getVisibility() == View.VISIBLE) {
            if (App.get(indexPatientId).isEmpty()) {
                indexPatientId.getEditText().setError(getResources().getString(R.string.mandatory_field));
                indexPatientId.getEditText().requestFocus();
                error = true;
            } else if (!RegexUtil.isValidId(App.get(indexPatientId))) {
                indexPatientId.getEditText().setError(getResources().getString(R.string.invalid_id));
                indexPatientId.getEditText().requestFocus();
                error = true;
            } else if (App.getPatient().getPatientId().equals(App.get(indexPatientId))) {
                indexPatientId.getEditText().setError(getResources().getString(R.string.pet_index_contact_id_same_error));
                indexPatientId.getEditText().requestFocus();
                error = true;
            } else {
                indexPatientId.getEditText().setError(null);
                indexPatientId.getEditText().clearFocus();
            }
        }


        if (dailyCigarettesIntake.getVisibility() == View.VISIBLE) {
            if (App.get(dailyCigarettesIntake).isEmpty()) {
                dailyCigarettesIntake.getEditText().setError(getString(R.string.empty_field));
                dailyCigarettesIntake.getEditText().requestFocus();
                gotoPage(3);
                error = true;
            } else if (App.get(dailyCigarettesIntake).equals("0")) {
                dailyCigarettesIntake.getEditText().setError(getString(R.string.non_zero));
                dailyCigarettesIntake.getEditText().requestFocus();
                gotoPage(3);
                error = true;
            }

        }


        if (error) {

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


        if (!App.get(childDiagnosedPresumptive).equals(getResources().getString(R.string.no))) {


            if (App.getPatient().getPerson().getAge() <= 15 && App.get(closeContact).equals(getResources().getString(R.string.yes))) {
                observations.add(new String[]{"EXPOSURE SCORE", App.get(exposureScore)});
            }


            if (returnVisitDate.getVisibility() == View.VISIBLE) {
            }

            if (bmi.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"BODY MASS INDEX", bmiResult});
            }

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
                    id = serverService.saveFormLocally(formName, form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = "";
                if (App.get(patientSource).equals(getResources().getString(R.string.contact_patient))) {
                    if (serverService.getLatestEncounterDateTime(App.getPatientId(), "PET-Baseline Screening") == null && serverService.getLatestEncounterDateTime(App.getPatientId(), "Clinician Evaluation") == null && serverService.getLatestObsValue(App.getPatientId(), "INDEX CONTACT RELATIONSHIP UUID") == null) {
                        result = serverService.saveContactIndexRelationship(App.get(indexPatientId), App.getPatient().getPatientId(), null, id);
                        if (!result.contains("SUCCESS"))
                            return result;
                        else {

                            String[] array = result.split(";;;");
                            observations.add(new String[]{"INDEX CONTACT RELATIONSHIP UUID", array[1]});

                        }

                    } else if (serverService.getLatestObsValue(App.getPatientId(), "INDEX CONTACT RELATIONSHIP UUID") != null) {

                        result = serverService.updateContactIndexRelationship(formDateCalendar.getTime(), serverService.getLatestObsValue(App.getPatientId(), "INDEX CONTACT RELATIONSHIP UUID"), id);
                        if (!result.contains("SUCCESS"))
                            return result;
                    }
                }

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

        } else if (view == scanQRCode) {
            try {
                Intent intent = new Intent(Barcode.BARCODE_INTENT);
                if (App.isCallable(context, intent)) {
                    intent.putExtra(Barcode.SCAN_MODE, Barcode.QR_MODE);
                    startActivityForResult(intent, Barcode.BARCODE_RESULT);
                } else {
                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getString(R.string.barcode_scanner_missing));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    //DrawableCompat.setTint(clearIcon, color);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            } catch (ActivityNotFoundException e) {
                //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage(getString(R.string.barcode_scanner_missing));
                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                //DrawableCompat.setTint(clearIcon, color);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_error));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
        if (view == returnVisitDate.getButton()) {
            returnVisitDate.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar, true, false, true);
            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", false);
            args.putBoolean("allowFutureDate", true);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");*/
            dateChoose = true;
        }

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;

        if (spinner == patientSource.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.other))) {
                otherPatientSource.setVisibility(View.VISIBLE);
            } else {
                otherPatientSource.setVisibility(View.GONE);
            }

            if (!parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.contact_patient)) && App.getPatient().getPerson().getAge() < 14)
                bcg.setVisibility(View.VISIBLE);
            else
                bcg.setVisibility(View.GONE);

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.contact_patient))) {
                closeContact.getRadioGroup().getButtons().get(0).setChecked(true);
                closeContact.setRadioGroupEnabled(false);
                if (App.getPatient().getPerson().getAge() <= 15) {
                    exposurePoint1.setVisibility(View.VISIBLE);
                    exposurePoint2.setVisibility(View.VISIBLE);
                    exposurePoint3.setVisibility(View.VISIBLE);
                    exposurePoint4.setVisibility(View.VISIBLE);
                    exposurePoint5.setVisibility(View.VISIBLE);
                    exposurePoint6.setVisibility(View.VISIBLE);
                    exposurePoint7.setVisibility(View.VISIBLE);
                    exposurePoint8.setVisibility(View.VISIBLE);
                    exposurePoint9.setVisibility(View.VISIBLE);
                    exposurePoint10.setVisibility(View.VISIBLE);
                    exposureScore.setVisibility(View.VISIBLE);
                } else {
                    exposurePoint1.setVisibility(View.GONE);
                    exposurePoint2.setVisibility(View.GONE);
                    exposurePoint3.setVisibility(View.GONE);
                    exposurePoint4.setVisibility(View.GONE);
                    exposurePoint5.setVisibility(View.GONE);
                    exposurePoint6.setVisibility(View.GONE);
                    exposurePoint7.setVisibility(View.GONE);
                    exposurePoint8.setVisibility(View.GONE);
                    exposurePoint9.setVisibility(View.GONE);
                    exposurePoint10.setVisibility(View.GONE);
                    exposureScore.setVisibility(View.GONE);

                }
                indexPatientId.setVisibility(View.VISIBLE);
                scanQRCode.setVisibility(View.VISIBLE);
                closeContactType.setVisibility(View.VISIBLE);
                childDiagnosedPresumptive.setVisibility(View.GONE);
                childDiagnosedPresumptive.getRadioGroup().getButtons().get(0).setChecked(true);
                //if(App.get(closeContactType))
            } else {
                childDiagnosedPresumptive.setVisibility(View.VISIBLE);
                closeContact.setRadioGroupEnabled(true);
//                if(!childDiagnosedPresumptive.equals(getResources().getString(R.string.no))){
//                    weight.setVisibility(View.VISIBLE);
//                    height.setVisibility(View.VISIBLE);
//                    bmi.setVisibility(View.VISIBLE);
//                    if (App.getPatient().getPerson().getAge() < 6)
//                        muac.setVisibility(View.VISIBLE);
//                    else
//                        muac.setVisibility(View.GONE);
//
//                    if (App.getPatient().getPerson().getAge() < 18) {
//                        weightPercentileEditText.setVisibility(View.VISIBLE);
//                    }
//                    else {
//                        weightPercentileEditText.setVisibility(View.GONE);
//                    }
//
//                }

                indexPatientId.setVisibility(View.GONE);
                scanQRCode.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == otherComorbidCondition) {
            if (otherComorbidCondition.isChecked())
                otherCondition.setVisibility(View.VISIBLE);
            else
                otherCondition.setVisibility(View.GONE);
        }


        for (CheckBox cb : systemsExamined.getCheckedBoxes()) {
//            systemsExamined.getQuestionView().setError(null);
            if (App.get(cb).equals(getResources().getString(R.string.ctb_general_appearance))) {
                if (cb.isChecked()) {
                    generalAppearence.setVisibility(View.VISIBLE);
                    if (App.get(generalAppearence).equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                        generalAppearenceExplanation.setVisibility(View.VISIBLE);
                    }
                } else {
                    generalAppearence.setVisibility(View.GONE);
                    generalAppearenceExplanation.setVisibility(View.GONE);
                }
            }
            if (App.get(cb).equals(getResources().getString(R.string.ctb_head_eye_ear_nose_throat))) {
                if (cb.isChecked()) {
                    heent.setVisibility(View.VISIBLE);
                    if (App.get(heent).equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                        heentExplanation.setVisibility(View.VISIBLE);
                    }
                } else {
                    heent.setVisibility(View.GONE);
                    heentExplanation.setVisibility(View.GONE);
                }
            }
            if (App.get(cb).equals(getResources().getString(R.string.lymph_node_examination))) {
                if (cb.isChecked()) {
                    lymphnode.setVisibility(View.VISIBLE);
                    if (App.get(lymphnode).equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                        lymphnodeExplanation.setVisibility(View.VISIBLE);
                    }
                } else {
                    lymphnode.setVisibility(View.GONE);
                    lymphnodeExplanation.setVisibility(View.GONE);
                }
            }
            if (App.get(cb).equals(getResources().getString(R.string.spine))) {
                if (cb.isChecked()) {
                    spine.setVisibility(View.VISIBLE);
                    if (App.get(spine).equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                        spineExplanation.setVisibility(View.VISIBLE);
                    }
                } else {
                    spine.setVisibility(View.GONE);
                    spineExplanation.setVisibility(View.GONE);
                }
            }
            if (App.get(cb).equals(getResources().getString(R.string.joints))) {
                if (cb.isChecked()) {
                    joints.setVisibility(View.VISIBLE);
                    if (App.get(joints).equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                        jointsExplanation.setVisibility(View.VISIBLE);
                    }
                } else {
                    joints.setVisibility(View.GONE);
                    jointsExplanation.setVisibility(View.GONE);
                }
            }
            if (App.get(cb).equals(getResources().getString(R.string.skin))) {
                if (cb.isChecked()) {
                    skin.setVisibility(View.VISIBLE);
                    if (App.get(skin).equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                        skinExplanation.setVisibility(View.VISIBLE);
                    }
                } else {
                    skin.setVisibility(View.GONE);
                    skinExplanation.setVisibility(View.GONE);
                }
            }
            if (App.get(cb).equals(getResources().getString(R.string.pet_chest_examination))) {
                if (cb.isChecked()) {
                    chest.setVisibility(View.VISIBLE);
                    if (App.get(chest).equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                        chestExplanation.setVisibility(View.VISIBLE);
                    }
                } else {
                    chest.setVisibility(View.GONE);
                    chestExplanation.setVisibility(View.GONE);
                }
            }
            if (App.get(cb).equals(getResources().getString(R.string.pet_abdominal_examination))) {
                if (cb.isChecked()) {
                    abdominal.setVisibility(View.VISIBLE);
                    if (App.get(abdominal).equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                        abdominalExplanation.setVisibility(View.VISIBLE);
                    }
                } else {
                    abdominal.setVisibility(View.GONE);
                    abdominalExplanation.setVisibility(View.GONE);
                }
            }

            systemsExamined.getQuestionView().setError(null);
        }
        for (CheckBox cb : closeContactType.getCheckedBoxes()) {
            if (App.get(cb).equals(getResources().getString(R.string.ctb_mother))) {
                if (cb.isChecked()) {
                    exposurePoint1.getRadioGroup().getButtons().get(0).setChecked(true);
                    for (RadioButton rb : exposurePoint1.getRadioGroup().getButtons()) {
                        rb.setClickable(false);
                    }
                } else {
                    exposurePoint1.getRadioGroup().getButtons().get(1).setChecked(true);
                    for (RadioButton rb : exposurePoint1.getRadioGroup().getButtons()) {
                        rb.setClickable(true);
                    }

                }
            }
            if (App.get(cb).equals(getResources().getString(R.string.ctb_other_title))) {
                if (cb.isChecked()) {
                    otherContactType.setVisibility(View.VISIBLE);
                } else {
                    otherContactType.setVisibility(View.GONE);
                }
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (group == cough.getRadioGroup()) {
            if (App.get(cough).equals(getResources().getString(R.string.yes))) {
                coughDuration.setVisibility(View.VISIBLE);
                haemoptysis.setVisibility(View.VISIBLE);
            } else {
                coughDuration.setVisibility(View.GONE);
                haemoptysis.setVisibility(View.GONE);
            }
        } else if (group == appetite.getRadioGroup()) {
            appetite.getQuestionView().setError(null);
        } else if (group == bcg.getRadioGroup()) {
            bcg.getQuestionView().setError(null);
        } else if (group == fever.getRadioGroup()) {
            if (App.get(fever).equals(getResources().getString(R.string.yes)))
                feverDuration.setVisibility(View.VISIBLE);
            else
                feverDuration.setVisibility(View.GONE);
        } else if (group == exposurePoint1.getRadioGroup() || group == exposurePoint2.getRadioGroup() || group == exposurePoint3.getRadioGroup()
                || group == exposurePoint4.getRadioGroup() || group == exposurePoint5.getRadioGroup() || group == exposurePoint6.getRadioGroup()
                || group == exposurePoint7.getRadioGroup() || group == exposurePoint8.getRadioGroup() || group == exposurePoint9.getRadioGroup()
                || group == exposurePoint10.getRadioGroup()) {

            int score = 0;

            if (App.get(exposurePoint1).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint2).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint3).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint4).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint5).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint6).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint7).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint8).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint9).equals(getResources().getString(R.string.yes)))
                score++;
            if (App.get(exposurePoint10).equals(getResources().getString(R.string.yes)))
                score++;

            exposureScore.getEditText().setText(String.valueOf(score));

        } else if (group == tbHistory.getRadioGroup()) {
            if (App.get(tbHistory).equals(getResources().getString(R.string.yes)))
                tbMedication.setVisibility(View.VISIBLE);
            else
                tbMedication.setVisibility(View.GONE);
        } else if (group == generalAppearence.getRadioGroup()) {
            if (App.get(generalAppearence).equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb)))
                generalAppearenceExplanation.setVisibility(View.VISIBLE);
            else
                generalAppearenceExplanation.setVisibility(View.GONE);
        } else if (group == heent.getRadioGroup()) {

            if (App.get(heent).equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb)))
                heentExplanation.setVisibility(View.VISIBLE);
            else
                heentExplanation.setVisibility(View.GONE);
        } else if (group == lymphnode.getRadioGroup()) {

            if (App.get(lymphnode).equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb)))
                lymphnodeExplanation.setVisibility(View.VISIBLE);
            else
                lymphnodeExplanation.setVisibility(View.GONE);
        } else if (group == spine.getRadioGroup()) {

            if (App.get(spine).equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb)))
                spineExplanation.setVisibility(View.VISIBLE);
            else
                spineExplanation.setVisibility(View.GONE);
        } else if (group == joints.getRadioGroup()) {

            if (App.get(joints).equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb)))
                jointsExplanation.setVisibility(View.VISIBLE);
            else
                jointsExplanation.setVisibility(View.GONE);
        } else if (group == skin.getRadioGroup()) {

            if (App.get(skin).equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb)))
                skinExplanation.setVisibility(View.VISIBLE);
            else
                skinExplanation.setVisibility(View.GONE);
        } else if (group == chest.getRadioGroup()) {

            if (App.get(chest).equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb)))
                chestExplanation.setVisibility(View.VISIBLE);
            else
                chestExplanation.setVisibility(View.GONE);
        } else if (group == abdominal.getRadioGroup()) {

            if (App.get(abdominal).equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb)))
                abdominalExplanation.setVisibility(View.VISIBLE);
            else
                abdominalExplanation.setVisibility(View.GONE);
        } else if (group == smokingHistory.getRadioGroup()) {
            if (App.get(smokingHistory).equals(getResources().getString(R.string.yes))) {
                dailyCigarettesIntake.setVisibility(View.VISIBLE);
            } else {
                dailyCigarettesIntake.setVisibility(View.GONE);
            }
        } else if (group == giSymptoms.getRadioGroup()) {
            if (App.get(giSymptoms).equals(getResources().getString(R.string.yes))) {
                chronicTwoWeeks.setVisibility(View.VISIBLE);
                otherGISymptoms.setVisibility(View.VISIBLE);
                abdominalPainTwoWeeks.setVisibility(View.VISIBLE);
            } else {
                chronicTwoWeeks.setVisibility(View.GONE);
                otherGISymptoms.setVisibility(View.GONE);
                abdominalPainTwoWeeks.setVisibility(View.GONE);
            }
        } else if (group == performedPhysicalExamination.getRadioGroup()) {
            performedPhysicalExamination.getQuestionView().setError(null);
            if (App.get(performedPhysicalExamination).equals(getResources().getString(R.string.performed))) {
                systemsExamined.setVisibility(View.VISIBLE);
                others.setVisibility(View.VISIBLE);
            } else {
                for (CheckBox cb : systemsExamined.getCheckedBoxes()) {
                    cb.setChecked(false);
                }
                systemsExamined.setVisibility(View.GONE);
                others.setVisibility(View.GONE);
                generalAppearence.setVisibility(View.GONE);
                generalAppearenceExplanation.setVisibility(View.GONE);
                heent.setVisibility(View.GONE);
                heentExplanation.setVisibility(View.GONE);
                lymphnode.setVisibility(View.GONE);
                lymphnodeExplanation.setVisibility(View.GONE);
                spine.setVisibility(View.GONE);
                spineExplanation.setVisibility(View.GONE);
                joints.setVisibility(View.GONE);
                jointsExplanation.setVisibility(View.GONE);
                skin.setVisibility(View.GONE);
                skinExplanation.setVisibility(View.GONE);
                chest.setVisibility(View.GONE);
                chestExplanation.setVisibility(View.GONE);
                abdominal.setVisibility(View.GONE);
                abdominalExplanation.setVisibility(View.GONE);
            }
        } else if (group == conclusion.getRadioGroup()) {
            conclusion.getQuestionView().setError(null);
            if (App.get(conclusion).equals(getResources().getString(R.string.ctb_tb_presumptive_confirmed))) {
                patientVisitFacility.setVisibility(View.VISIBLE);
                Toast.makeText(context, getResources().getString(R.string.fill_eof), Toast.LENGTH_SHORT).show();
                returnVisitDate.setVisibility(View.VISIBLE);
                String val = App.get(patientVisitFacility);
                if (App.get(patientVisitFacility).equals(getResources().getString(R.string.yes))) {
                    returnVisitDate.setVisibility(View.VISIBLE);
                } else
                    returnVisitDate.setVisibility(View.GONE);
//                }

            } else {
                patientVisitFacility.setVisibility(View.GONE);
                returnVisitDate.setVisibility(View.GONE);
            }
        } else if (group == patientVisitFacility.getRadioGroup()) {
            if (App.get(patientVisitFacility).equals(getResources().getString(R.string.yes))) {
                returnVisitDate.setVisibility(View.VISIBLE);
            } else {
                returnVisitDate.setVisibility(View.GONE);
            }
        } else if (group == childDiagnosedPresumptive.getRadioGroup()) {
            childDiagnosedPresumptive.getQuestionView().setError(null);
            if (App.get(childDiagnosedPresumptive).equals(getResources().getString(R.string.no))) {
                weight.setVisibility(View.GONE);
                height.setVisibility(View.GONE);
                bmi.setVisibility(View.GONE);
                muac.setVisibility(View.GONE);
                weightPercentileEditText.setVisibility(View.GONE);
                pageCount = 1;

//                for(int i=4; i>=1; i--){
//                    pageCount = i;
//                    groups.remove(i);
//                }


//                pageCount = 4;
//                groups.remove(4); // ArrayList<ItemFragment>
//
//                pageCount = 3;
//                groups.remove(3); // ArrayList<ItemFragment>
//
//                pageCount = 2;
//                groups.remove(2); // ArrayList<ItemFragment>
//
//
//                pageCount = 1;
//                groups.remove(1); // ArrayList<ItemFragment>

//                linearLayout1.setVisibility(View.GONE);
//                linearLayout2.setVisibility(View.GONE);
//                linearLayout2a.setVisibility(View.GONE);
//                linearLayout3.setVisibility(View.GONE);
                adapter.notifyDataSetChanged(); // MyFragmentAdapter
            } else {
                if (pageCount == 1) {
                    pageCount = 5;
                    adapter.notifyDataSetChanged();
                    weight.setVisibility(View.VISIBLE);
                    height.setVisibility(View.VISIBLE);
                    bmi.setVisibility(View.VISIBLE);
                    if (App.getPatient().getPerson().getAge() < 6)
                        muac.setVisibility(View.VISIBLE);
                    else
                        muac.setVisibility(View.GONE);

                    if (App.getPatient().getPerson().getAge() < 18) {
                        weightPercentileEditText.setVisibility(View.VISIBLE);
                    } else {
                        weightPercentileEditText.setVisibility(View.GONE);
                    }
                    // MyFragmentAdapter
//                    ScrollView scrollView = new ScrollView(mainContent.getContext());
//                    scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                    ((ViewGroup)linearLayout1.getParent()).removeView((ViewGroup)linearLayout1);
//                    scrollView.addView(linearLayout1);

//                    groups.add(scrollView);
//                    adapter.notifyDataSetChanged(); // MyFragmentAdapter

//                    groups.add(linearLayout1);
//                    groups.add(linearLayout2);
//                    groups.add(linearLayout2a);
//                    groups.add(linearLayout3);
                }
            }
        } else if (group == closeContact.getRadioGroup()) {
            closeContact.getQuestionView().setError(null);
            if (App.get(closeContact).equals(getResources().getString(R.string.yes))) {
                closeContactType.setVisibility(View.VISIBLE);
                if (App.getPatient().getPerson().getAge() <= 15 && App.get(closeContact).equals(getResources().getString(R.string.yes))) {
                    exposurePoint1.setVisibility(View.VISIBLE);
                    exposurePoint2.setVisibility(View.VISIBLE);
                    exposurePoint3.setVisibility(View.VISIBLE);
                    exposurePoint4.setVisibility(View.VISIBLE);
                    exposurePoint5.setVisibility(View.VISIBLE);
                    exposurePoint6.setVisibility(View.VISIBLE);
                    exposurePoint7.setVisibility(View.VISIBLE);
                    exposurePoint8.setVisibility(View.VISIBLE);
                    exposurePoint9.setVisibility(View.VISIBLE);
                    exposurePoint10.setVisibility(View.VISIBLE);
                    exposureScore.setVisibility(View.VISIBLE);

                } else {
                    exposurePoint1.setVisibility(View.GONE);
                    exposurePoint2.setVisibility(View.GONE);
                    exposurePoint3.setVisibility(View.GONE);
                    exposurePoint4.setVisibility(View.GONE);
                    exposurePoint5.setVisibility(View.GONE);
                    exposurePoint6.setVisibility(View.GONE);
                    exposurePoint7.setVisibility(View.GONE);
                    exposurePoint8.setVisibility(View.GONE);
                    exposurePoint9.setVisibility(View.GONE);
                    exposurePoint10.setVisibility(View.GONE);
                    exposureScore.setVisibility(View.GONE);

                }
                for (CheckBox cb : closeContactType.getCheckedBoxes()) {
                    if (App.get(cb).equals(getResources().getString(R.string.ctb_other_title))) {
                        if (cb.isChecked()) {
                            otherContactType.setVisibility(View.VISIBLE);
                        } else {
                            otherContactType.setVisibility(View.GONE);
                        }
                    }
                    if (App.get(cb).equals(getResources().getString(R.string.ctb_mother))) {
                        if (cb.isChecked()) {
                            exposurePoint1.getRadioGroup().getButtons().get(0).setChecked(true);
                            for (RadioButton rb : exposurePoint1.getRadioGroup().getButtons()) {
                                rb.setClickable(false);
                            }
                            exposurePoint1.setRadioGroupEnabled(false);
                        }
                    }
                }
            } else {
                for (RadioButton rb : exposurePoint1.getRadioGroup().getButtons()) {
                    rb.setClickable(true);
                }
                exposurePoint1.setRadioGroupEnabled(true);
                closeContactType.setVisibility(View.GONE);
                otherContactType.setVisibility(View.GONE);
                exposurePoint1.setVisibility(View.GONE);
                exposurePoint2.setVisibility(View.GONE);
                exposurePoint3.setVisibility(View.GONE);
                exposurePoint4.setVisibility(View.GONE);
                exposurePoint5.setVisibility(View.GONE);
                exposurePoint6.setVisibility(View.GONE);
                exposurePoint7.setVisibility(View.GONE);
                exposurePoint8.setVisibility(View.GONE);
                exposurePoint9.setVisibility(View.GONE);
                exposurePoint10.setVisibility(View.GONE);
                exposureScore.setVisibility(View.GONE);
            }
        } else if (group == patientReferred.getRadioGroup()) {
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

    @Override
    public void refill(int encounterId) {
        super.refill(encounterId);
        refillFlag = true;

        OfflineForm fo = serverService.getSavedFormById(encounterId);

        ArrayList<String[][]> obsValue = fo.getObsValue();


        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("EXPOSURE SCORE")) {
                exposureScore.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String forthDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(forthDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Retrieve barcode scan results
        if (requestCode == Barcode.BARCODE_RESULT) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra(Barcode.SCAN_RESULT);
                // Check for valid Id
                if (RegexUtil.isValidId(str)) {
                    indexPatientId.getEditText().setText(str);
                    indexPatientId.getEditText().requestFocus();
                } else {

                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);

                    indexPatientId.getEditText().setText("");
                    indexPatientId.getEditText().requestFocus();

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getString(R.string.invalid_scanned_id));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    //DrawableCompat.setTint(clearIcon, color);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
            } else if (resultCode == RESULT_CANCELED) {

                int color = App.getColor(context, R.attr.colorAccent);

                indexPatientId.getEditText().setText("");
                indexPatientId.getEditText().requestFocus();

                /*final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this).create();
                alertDialog.setMessage(getString(R.string.warning_before_clear));
                Drawable clearIcon = getResources().getDrawable(R.drawable.ic_clear);
                DrawableCompat.setTint(clearIcon, color);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_clear));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();*/

            }
            // Set the locale again, since the Barcode app restores system's
            // locale because of orientation
            Locale.setDefault(App.getCurrentLocale());
            Configuration config = new Configuration();
            config.locale = App.getCurrentLocale();
            context.getResources().updateConfiguration(config, null);
        }
    }

}
