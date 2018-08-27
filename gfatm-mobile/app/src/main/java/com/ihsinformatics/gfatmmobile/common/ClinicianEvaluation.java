package com.ihsinformatics.gfatmmobile.common;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.Barcode;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyCheckBox;
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

    TitledButton formDate;
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
    TitledRadioGroup followupRequiredCallCenter;
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
    TitledCheckBoxes comorbidCondition;
    CheckBox otherComorbidCondition;
    TitledEditText otherCondition;
    TitledRadioGroup smokingHistory;
    TitledEditText dailyCigarettesIntake;
    TitledRadioGroup conclusion;
    TitledEditText clincianNote;
    //Next Appointment
    TitledButton returnVisitDate;
    MyAdapter adapter;
    MyLinearLayout linearLayout2;
    MyLinearLayout linearLayout2a;
    MyLinearLayout linearLayout1;
    MyLinearLayout linearLayout3;

    Boolean refillFlag = false;
    ScrollView scrollView;


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

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        patientSource = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.patient_source), getResources().getStringArray(R.array.patient_source_options), "", App.HORIZONTAL, true);
        otherPatientSource = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        indexPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_id), "", "", RegexUtil.idLength, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, App.HORIZONTAL, true);
        scanQRCode = new Button(context);
        scanQRCode.setText("Scan QR Code");
        childDiagnosedPresumptive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_mo_think_child_presumptive), getResources().getStringArray(R.array.yes_no_options), null, App.VERTICAL, App.VERTICAL);
        externalPatientId = new TitledEditText(context, null, getResources().getString(R.string.external_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        weight = new TitledEditText(context, null, getResources().getString(R.string.pet_weight), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        height = new TitledEditText(context, null, getResources().getString(R.string.pet_height), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        bmi = new TitledEditText(context, null, getResources().getString(R.string.pet_bmi), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        muac = new TitledEditText(context, null, getResources().getString(R.string.pet_muac), "", "", 3, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        weightPercentileEditText = new TitledEditText(context, null, getResources().getString(R.string.pet_weight_percentile), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        linearLayout1 = new MyLinearLayout(context, getResources().getString(R.string.symptom_screen), App.VERTICAL);
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_cough), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        coughDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_cough_duration), getResources().getStringArray(R.array.pet_cough_durations), getResources().getString(R.string.pet_less_than_2_weeks), App.VERTICAL, App.VERTICAL);
        haemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_haemoptysis), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        difficultyBreathing = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_difficulty_breathing), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_fever), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        feverDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_fever_duration), getResources().getStringArray(R.array.pet_cough_durations), getResources().getString(R.string.no), App.VERTICAL, App.VERTICAL);
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_weight_loss), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_night_sweats), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        appetite = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_child_appetite), getResources().getStringArray(R.array.ctb_appetite_list), null, App.HORIZONTAL, App.VERTICAL, true);
        lethargy = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_lethargy), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        swollenJoints = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_swollen_joints), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        backPain = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_back_pain), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        adenopathy = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adenopathy), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        vomiting = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_vomiting_without_gi_symptoms), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        giSymptoms = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_gi_symptoms), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        chronicTwoWeeks = new TitledRadioGroup(context, null, getResources().getString(R.string.chronic_diarrhea_two_weeks), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        abdominalPainTwoWeeks = new TitledRadioGroup(context, null, getResources().getString(R.string.abdominal_pain_longer_than_2_weeks), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        alteredLevelConscious = new TitledRadioGroup(context, null, getResources().getString(R.string.altered_level_conscious), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        otherGISymptoms = new TitledEditText(context, null, getResources().getString(R.string.gi_other_symptoms), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

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

        linearLayout2 = new MyLinearLayout(context, "Family History", App.VERTICAL);
        closeContact = new TitledRadioGroup(context, null, getResources().getString(R.string.close_contact), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL,true);
        closeContactType = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_close_contact_type), getResources().getStringArray(R.array.close_contact_type_list_sd), null, App.VERTICAL, App.VERTICAL, true);
        otherContactType = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_title), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        exposurePoint1 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_1), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        exposurePoint2 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_2), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        exposurePoint3 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_3), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        exposurePoint4 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_4), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        exposurePoint5 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_5), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        exposurePoint6 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_6), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        exposurePoint7 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_7), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        exposurePoint8 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_8), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        exposurePoint9 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_9), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        exposurePoint10 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_10), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        exposureScore = new TitledEditText(context, null, getResources().getString(R.string.pet_exposure_score), "0", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_NUMBER_FLAG_DECIMAL, App.HORIZONTAL, false);
        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.pet_doctor_notes), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        clincianNote.getEditText().setSingleLine(false);
        clincianNote.getEditText().setMinimumHeight(150);
        conclusion = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_conclusion), getResources().getStringArray(R.array.ctb_conclusion_list), null, App.HORIZONTAL, App.VERTICAL, true);
        followupRequiredCallCenter = new TitledRadioGroup(context, null, getResources().getString(R.string.followup_by_call_center), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL,true);
        patientVisitFacility = new TitledRadioGroup(context, null, getResources().getString(R.string.patient_visit_facility), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL,true);
        secondDateCalendar.add(Calendar.DAY_OF_MONTH, 30);
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
        linearLayout2.addView(followupRequiredCallCenter);
        linearLayout2.addView(patientVisitFacility);
        linearLayout2.addView(returnVisitDate);

        linearLayout2a = new MyLinearLayout(context, getResources().getString(R.string.pet_physical_examination), App.VERTICAL);

        performedPhysicalExamination = new TitledRadioGroup(context, null, getResources().getString(R.string.performed_physical_examination), getResources().getStringArray(R.array.performed_physical_exam_list), getResources().getString(R.string.normal_unremarkable), App.HORIZONTAL, App.VERTICAL,true);
        systemsExamined = new TitledCheckBoxes(context, null, getResources().getString(R.string.systems_were_examined), getResources().getStringArray(R.array.system_examined_list), null, App.VERTICAL,App.VERTICAL,true);
        generalAppearence = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_general_appearance), getResources().getStringArray(R.array.suggestive_not_sugg_unremarkable), getResources().getString(R.string.normal_unremarkable), App.VERTICAL, App.VERTICAL,true);
        generalAppearenceExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        generalAppearenceExplanation.getEditText().setSingleLine(false);
        generalAppearenceExplanation.getEditText().setMinimumHeight(150);
        heent = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_heent), getResources().getStringArray(R.array.suggestive_not_sugg_unremarkable), getResources().getString(R.string.normal_unremarkable), App.VERTICAL, App.VERTICAL,true);
        heentExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        heentExplanation.getEditText().setSingleLine(false);
        heentExplanation.getEditText().setMinimumHeight(150);
        lymphnode = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_lymphnode), getResources().getStringArray(R.array.suggestive_not_sugg_unremarkable),getResources().getString(R.string.normal_unremarkable), App.VERTICAL, App.VERTICAL,true);
        lymphnodeExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        lymphnodeExplanation.getEditText().setSingleLine(false);
        lymphnodeExplanation.getEditText().setMinimumHeight(150);
        spine = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_spine), getResources().getStringArray(R.array.suggestive_not_sugg_unremarkable), getResources().getString(R.string.normal_unremarkable), App.VERTICAL, App.VERTICAL,true);
        spineExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        spineExplanation.getEditText().setSingleLine(false);
        spineExplanation.getEditText().setMinimumHeight(150);
        joints = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_joints), getResources().getStringArray(R.array.suggestive_not_sugg_unremarkable), getResources().getString(R.string.normal_unremarkable), App.VERTICAL, App.VERTICAL,true);
        jointsExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        jointsExplanation.getEditText().setSingleLine(false);
        jointsExplanation.getEditText().setMinimumHeight(150);
        skin = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_skin), getResources().getStringArray(R.array.suggestive_not_sugg_unremarkable), getResources().getString(R.string.normal_unremarkable), App.VERTICAL, App.VERTICAL,true);
        skinExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        skinExplanation.getEditText().setSingleLine(false);
        skinExplanation.getEditText().setMinimumHeight(150);
        chest = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_chest_examination), getResources().getStringArray(R.array.suggestive_not_sugg_unremarkable), getResources().getString(R.string.normal_unremarkable), App.VERTICAL, App.VERTICAL,true);
        chestExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        chestExplanation.getEditText().setSingleLine(false);
        chestExplanation.getEditText().setMinimumHeight(150);
        abdominal = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_abdominal_examination), getResources().getStringArray(R.array.suggestive_not_sugg_unremarkable), getResources().getString(R.string.normal_unremarkable), App.VERTICAL, App.VERTICAL,true);
        abdominalExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        abdominalExplanation.getEditText().setSingleLine(false);
        abdominalExplanation.getEditText().setMinimumHeight(150);
        others = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        bcg = new TitledRadioGroup(mainContent.getContext(), "", getResources().getString(R.string.ctb_bcg), getResources().getStringArray(R.array.yes_no_unknown_refused_options), "", App.VERTICAL, App.VERTICAL,true);

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

        linearLayout3 = new MyLinearLayout(context, "Medical History", App.VERTICAL);
        comorbidCondition = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_comorbid_condition), getResources().getStringArray(R.array.pet_comorbid_conditions), null, App.VERTICAL, App.VERTICAL);
        otherCondition = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 15, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        smokingHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_smoking_history), getResources().getStringArray(R.array.yes_no_unknown_options), getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        dailyCigarettesIntake  = new TitledEditText(context, null, getResources().getString(R.string.pet_daily_cigarettes_cosumption), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);


        linearLayout3.addView(comorbidCondition);
        linearLayout3.addView(otherCondition);
        linearLayout3.addView(smokingHistory);
        linearLayout3.addView(dailyCigarettesIntake);
//       linearLayout3.addView(clincianNote);

        views = new View[]{formDate.getButton(), patientSource.getSpinner(),otherPatientSource.getEditText(),indexPatientId.getEditText(), externalPatientId.getEditText(), weight.getEditText(), height.getEditText(), bmi.getEditText(), muac.getEditText(),
                childDiagnosedPresumptive.getRadioGroup(),muac.getEditText(),
                cough.getRadioGroup(), coughDuration.getRadioGroup(), haemoptysis.getRadioGroup(), difficultyBreathing.getRadioGroup(), fever.getRadioGroup(), feverDuration.getRadioGroup(),
                weightLoss.getRadioGroup(), nightSweats.getRadioGroup(), lethargy.getRadioGroup(), swollenJoints.getRadioGroup(), backPain.getRadioGroup(), adenopathy.getRadioGroup(),
                vomiting.getRadioGroup(), giSymptoms.getRadioGroup(), chronicTwoWeeks.getRadioGroup(),abdominalPainTwoWeeks.getRadioGroup(),otherGISymptoms.getEditText(),alteredLevelConscious.getRadioGroup(), exposurePoint1.getRadioGroup(), exposurePoint2.getRadioGroup(), exposurePoint3.getRadioGroup(),
                exposurePoint4.getRadioGroup(), exposurePoint5.getRadioGroup(), exposurePoint6.getRadioGroup(), exposurePoint7.getRadioGroup(), exposurePoint8.getRadioGroup(), exposurePoint9.getRadioGroup(),
                exposurePoint10.getRadioGroup(), exposureScore.getEditText(),
                generalAppearence.getRadioGroup(), generalAppearenceExplanation.getEditText(), heent.getRadioGroup(), heentExplanation.getEditText(), lymphnode.getRadioGroup(), lymphnodeExplanation.getEditText(),
                spine.getRadioGroup(), spineExplanation.getEditText(), joints.getRadioGroup(), jointsExplanation.getEditText(), jointsExplanation.getEditText(), skin.getRadioGroup(), skinExplanation.getEditText(),
                chest.getRadioGroup(), chestExplanation.getEditText(), abdominal.getRadioGroup(), abdominal.getRadioGroup(), bcg.getRadioGroup(), comorbidCondition,
                otherCondition.getEditText(), clincianNote.getEditText(), weightPercentileEditText.getEditText(),smokingHistory.getRadioGroup(),
                dailyCigarettesIntake.getEditText(), systemsExamined,performedPhysicalExamination.getRadioGroup(),
                conclusion.getRadioGroup(),patientVisitFacility.getRadioGroup(),followupRequiredCallCenter.getRadioGroup()};

        viewGroups = new View[][]{{formDate, patientSource,otherPatientSource,externalPatientId ,indexPatientId, scanQRCode, childDiagnosedPresumptive,weight, height, bmi, muac, weightPercentileEditText},
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

                if (!App.get(weight).equals("")){
                    Double w = Double.parseDouble(App.get(weight));
                    if(w < 0.5 || w > 700.0)
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

                    if(BMI > 200){
                        bmi.getEditText().setError(getString(R.string.pet_invalid_bmi));
                        bmiCategory = "Invalid";
                    } else bmi.getEditText().setError(null);

                    bmi.getEditText().setText(result + "   -   " + bmiCategory);

                }

                if (weightPercentileEditText.getVisibility() == View.VISIBLE && !App.get(weight).equals("")){
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

                if (!App.get(height).equals("")){
                    Double h = Double.parseDouble(App.get(height));
                    if(h < 10.0 || h > 272.0)
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

                    if(BMI > 200){
                        bmi.getEditText().setError(getString(R.string.pet_invalid_bmi));
                        bmiCategory = "Invalid";
                    } else bmi.getEditText().setError(null);

                    bmi.getEditText().setText(result + "   -   " + bmiCategory);

                }


            }
        });
        indexPatientId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(start == 5 && s.length()==5){
                    int i = indexPatientId.getEditText().getSelectionStart();
                    if (i == 5){
                        indexPatientId.getEditText().setText(indexPatientId.getEditText().getText().toString().substring(0,4));
                        indexPatientId.getEditText().setSelection(4);
                    }
                }
                else if(s.length()==5 && !s.toString().contains("-")){
                    indexPatientId.getEditText().setText(s + "-");
                    indexPatientId.getEditText().setSelection(6);
                } else if(s.length()==7 && !RegexUtil.isValidId(App.get(indexPatientId)))
                    indexPatientId.getEditText().setError(getString(R.string.invalid_id));
                else
                    indexPatientId.getEditText().setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bmi.getEditText().setKeyListener(null);
        exposureScore.getEditText().setKeyListener(null);
        weightPercentileEditText.getEditText().setKeyListener(null);
        patientSource.getSpinner().setOnItemSelectedListener(this);


        for (CheckBox cb : systemsExamined.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        for (CheckBox cb2 : closeContactType.getCheckedBoxes())
            cb2.setOnCheckedChangeListener(this);


        View listenerViewer[] = new View[]{formDate,childDiagnosedPresumptive, cough, fever, exposurePoint1, exposurePoint2, exposurePoint3, exposurePoint4, exposurePoint5,
                exposurePoint6, exposurePoint7, exposurePoint8, exposurePoint9, exposurePoint10, abdominal, chest,giSymptoms,
                skin, joints, spine, lymphnode, heent, generalAppearence, smokingHistory,performedPhysicalExamination,conclusion,patientVisitFacility,followupRequiredCallCenter,closeContact};
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
        otherContactType.setVisibility(View.GONE);
        followupRequiredCallCenter.setVisibility(View.GONE);
        patientVisitFacility.setVisibility(View.GONE);
        returnVisitDate.setVisibility(View.GONE);
        indexPatientId.setVisibility(View.GONE);
        scanQRCode.setVisibility(View.GONE);
        childDiagnosedPresumptive.setVisibility(View.GONE);
        closeContactType.setVisibility(View.GONE);

        if (App.getPatient().getPerson().getAge() < 6)
            muac.setVisibility(View.VISIBLE);
        else
            muac.setVisibility(View.GONE);

        if (App.getPatient().getPerson().getAge() < 18) {
            weightPercentileEditText.setVisibility(View.VISIBLE);
        }
        else {
            weightPercentileEditText.setVisibility(View.GONE);
        }

        if (App.getPatient().getPerson().getAge() <= 15){

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

        }else {

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

        String patientSourceString = serverService.getLatestObsValue(App.getPatientId(), "Patient Information", "PATIENT SOURCE");
        System.out.println(patientSourceString);
        if(patientSourceString!=null ){
            String val = patientSourceString.equals("IDENTIFIED PATIENT THROUGH SCREENING") ? getResources().getString(R.string.screening) :
                    patientSourceString.equals("PATIENT REFERRED") ?  getResources().getString(R.string.referred) :
                            patientSourceString.equals("TUBERCULOSIS CONTACT") ? getResources().getString(R.string.contact_patient) :
                                    (patientSourceString.equals("WALK IN") ? getResources().getString(R.string.walkin) : getResources().getString(R.string.ctb_other_title) );
            if(val.equals(getResources().getString(R.string.ctb_other_title))){
                otherPatientSource.setVisibility(View.VISIBLE);
            }
            patientSource.getSpinner().selectValue(val);
            patientSource.getSpinner().setEnabled(false);
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

        if(!flag) {
            externalPatientId.getEditText().setText(App.getPatient().getExternalId());

            String indexId = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_BASELINE_SCREENING, "PATIENT ID OF INDEX CASE");
            if(indexId != null) indexPatientId.getEditText().setText(indexId);

        }

        if(!App.get(externalPatientId).equals("")){
            externalPatientId.getEditText().setKeyListener(null);
        }

    }

    @Override
    public void updateDisplay() {

        if(refillFlag){
            refillFlag = true;
            return;
        }

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setEnabled(true);

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

        }

    }


    @Override
    public boolean validate() {

        View view = null;
        Boolean error = false;

        if (App.get(patientSource).equals("")) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            patientSource.getQuestionView().setError(getString(R.string.invalid_value));
            patientSource.getSpinner().requestFocus();
            error = true;
        } else {

            patientSource.getQuestionView().setError(null);
        }

        if (otherPatientSource.getVisibility() == View.VISIBLE && App.get(otherPatientSource).equals("")) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherPatientSource.getEditText().setError(getString(R.string.invalid_value));
            otherPatientSource.getEditText().requestFocus();
            error = true;
        }else {
            otherPatientSource.getEditText().setError(null);
        }

        if (App.get(dailyCigarettesIntake).isEmpty() && dailyCigarettesIntake.getVisibility() == View.VISIBLE) {
            dailyCigarettesIntake.getEditText().setError(getString(R.string.empty_field));
            dailyCigarettesIntake.getEditText().requestFocus();
            error = true;
            gotoLastPage();
        } else{
            dailyCigarettesIntake.getEditText().setError(null);
            dailyCigarettesIntake.getEditText().clearFocus();
        }

        if (App.get(abdominalExplanation).isEmpty() && abdominalExplanation.getVisibility() == View.VISIBLE) {
            abdominalExplanation.getEditText().setError(getString(R.string.empty_field));
            abdominalExplanation.getEditText().requestFocus();
            error = true;
            gotoLastPage();
        } else{
            abdominalExplanation.getEditText().setError(null);
            abdominalExplanation.getEditText().clearFocus();
        }

        if (App.get(chestExplanation).isEmpty() && chestExplanation.getVisibility() == View.VISIBLE) {
            chestExplanation.getEditText().setError(getString(R.string.empty_field));
            chestExplanation.getEditText().requestFocus();
            error = true;
            gotoLastPage();
        } else{
            chestExplanation.getEditText().setError(null);
            chestExplanation.getEditText().clearFocus();
        }

        if (App.get(skinExplanation).isEmpty() && skinExplanation.getVisibility() == View.VISIBLE) {
            skinExplanation.getEditText().setError(getString(R.string.empty_field));
            skinExplanation.getEditText().requestFocus();
            error = true;
            gotoLastPage();
        } else{
            skinExplanation.getEditText().setError(null);
            skinExplanation.getEditText().clearFocus();
        }

        if (App.get(spineExplanation).isEmpty() && spineExplanation.getVisibility() == View.VISIBLE) {
            gotoLastPage();
            spineExplanation.getEditText().setError(getString(R.string.empty_field));
            spineExplanation.getEditText().requestFocus();
            error = true;
        } else{
            spineExplanation.getEditText().setError(null);
            spineExplanation.getEditText().clearFocus();
        }

        if (App.get(lymphnodeExplanation).isEmpty() && lymphnodeExplanation.getVisibility() == View.VISIBLE) {
            gotoLastPage();
            lymphnodeExplanation.getEditText().setError(getString(R.string.empty_field));
            lymphnodeExplanation.getEditText().requestFocus();
            error = true;
        } else{
            lymphnodeExplanation.getEditText().setError(null);
            lymphnodeExplanation.getEditText().clearFocus();
        }

        if (App.get(heentExplanation).isEmpty() && heentExplanation.getVisibility() == View.VISIBLE) {
            gotoLastPage();
            heentExplanation.getEditText().setError(getString(R.string.empty_field));
            heentExplanation.getEditText().requestFocus();
            error = true;
        }else{
            heentExplanation.getEditText().setError(null);
            heentExplanation.getEditText().clearFocus();
        }

        if (App.get(generalAppearenceExplanation).isEmpty() && generalAppearenceExplanation.getVisibility() == View.VISIBLE) {
            gotoLastPage();
            generalAppearenceExplanation.getEditText().setError(getString(R.string.empty_field));
            generalAppearenceExplanation.getEditText().requestFocus();
            error = true;
        }else{
            generalAppearenceExplanation.getEditText().setError(null);
            generalAppearenceExplanation.getEditText().clearFocus();
        }

//        if (App.get(muac).isEmpty() && muac.getVisibility() == View.VISIBLE) {
//            gotoFirstPage();
//            muac.getEditText().setError(getString(R.string.empty_field));
//            muac.getEditText().requestFocus();
//            error = true;
//        }

       /* if (App.get(height).isEmpty()) {
            gotoFirstPage();
            height.getEditText().setError(getString(R.string.empty_field));
            height.getEditText().requestFocus();
            error = true;
        }

        if (App.get(weight).isEmpty()) {
            gotoFirstPage();
            weight.getEditText().setError(getString(R.string.empty_field));
            weight.getEditText().requestFocus();
            error = true;
        }*/

        if (!App.get(height).equals("")){
            Double h = Double.parseDouble(App.get(height));
            if(h < 10.0 || h > 272.0) {
                height.getEditText().setError(getString(R.string.pet_invalid_height_range));
                gotoFirstPage();
                error = true;
                height.getQuestionView().requestFocus();
            }
            else {
                height.getEditText().setError(null);
                height.getQuestionView().clearFocus();
            }
        }

        if (!App.get(weight).equals("")){
            Double w = Double.parseDouble(App.get(weight));
            if(w < 0.5 || w > 700.0) {
                weight.getEditText().setError(getString(R.string.pet_invalid_weight_range));
                gotoFirstPage();
                error = true;
                weight.getQuestionView().requestFocus();
            } else {
                weight.getEditText().setError(null);
                weight.getQuestionView().clearFocus();
            }
        }

        if (!App.get(bmi).equals("")){
           if(App.get(bmi).contains("Invalid")){

               bmi.getEditText().setError(getString(R.string.pet_invalid_bmi));
               gotoFirstPage();
               error = true;
               bmi.getQuestionView().requestFocus();

           }else {
               bmi.getEditText().setError(null);
               bmi.getQuestionView().clearFocus();
           }
        }

        if (App.get(indexPatientId).isEmpty() && indexPatientId.getVisibility() == View.VISIBLE) {
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
        } else{
            indexPatientId.getEditText().setError(null);
            indexPatientId.getEditText().clearFocus();
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
                                        generalAppearenceExplanation.getEditText().clearFocus();
                                        heentExplanation.getEditText().clearFocus();
                                        lymphnodeExplanation.getEditText().clearFocus();
                                        spineExplanation.getEditText().clearFocus();
                                        skinExplanation.getEditText().clearFocus();
                                        chestExplanation.getEditText().clearFocus();
                                        abdominalExplanation.getEditText().clearFocus();
                                        dailyCigarettesIntake.getEditText().clearFocus();
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

        observations.add(new String[]{"CONTACT EXTERNAL ID", App.get(externalPatientId)});

        observations.add(new String[]{"PATIENT SOURCE", App.get(patientSource).equals(getResources().getString(R.string.screening)) ? "IDENTIFIED PATIENT THROUGH SCREENING" :
                (App.get(patientSource).equals(getResources().getString(R.string.ctb_reffered)) ? "PATIENT REFERRED" :
                    (App.get(patientSource).equals(getResources().getString(R.string.contact_patient)) ? "TUBERCULOSIS CONTACT" :
                            (App.get(patientSource).equals(getResources().getString(R.string.walkin)) ? "WALK IN" : "OTHER PATIENT SOURCE")))});
        if(App.get(patientSource).equals(getResources().getString(R.string.ctb_other_title))){
            observations.add(new String[]{"OTHER PATIENT SOURCE", App.get(otherPatientSource)});
        }
        observations.add(new String[]{"PATIENT ID OF INDEX CASE", App.get(indexPatientId)});
        observations.add(new String[]{"CHILD DIAGNOSED PRESUMPTIVE BY MO", App.get(childDiagnosedPresumptive).equals(getResources().getString(R.string.yes)) ? "YES" :
                "NO"});

        if(!App.get(childDiagnosedPresumptive).equals(getResources().getString(R.string.no))){
            if(!App.get(weight).equals(""))
                observations.add(new String[]{"WEIGHT (KG)", App.get(weight)});
            if(!App.get(height).equals(""))
                observations.add(new String[]{"HEIGHT (CM)", App.get(height)});
            if(!App.get(bmi).equals("")) {
                String[] bmiString = App.get(bmi).split(" - ");
                observations.add(new String[]{"BODY MASS INDEX", bmiString[0]});
            }
            if (muac.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"MID-UPPER ARM CIRCUMFERENCE", App.get(muac)});
            if (weightPercentileEditText.getVisibility() == View.VISIBLE && !weightPercentileEditText.getEditText().getText().toString().equals(""))
                observations.add(new String[]{"WEIGHT PERCENTILE GROUP", weightPercentileEditText.getEditText().getText().toString()});



                             // SYMPTOM SCREEN SUBMISSIONS

            observations.add(new String[]{"COUGH", App.get(cough).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(cough).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(cough).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

            if (coughDuration.getVisibility() == View.VISIBLE && !App.get(coughDuration).equals(getResources().getString(R.string.ctb_empty)))
                observations.add(new String[]{"COUGH DURATION", App.get(coughDuration).equals(getResources().getString(R.string.ctb_less_than_2_weeks)) ? "COUGH LASTING LESS THAN 2 WEEKS" :
                        (App.get(coughDuration).equals(getResources().getString(R.string.ctb_2_to_3_weeks)) ? "COUGH LASTING MORE THAN 2 WEEKS" :
                                (App.get(coughDuration).equals(getResources().getString(R.string.ctb_more_than_3_weeks)) ? "COUGH LASTING MORE THAN 3 WEEKS" :
                                        (App.get(coughDuration).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSED")))});

            if (haemoptysis.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"HEMOPTYSIS", App.get(haemoptysis).equals(getResources().getString(R.string.yes)) ? "YES" :
                        (App.get(haemoptysis).equals(getResources().getString(R.string.no)) ? "NO" :
                                (App.get(haemoptysis).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
            observations.add(new String[]{"DYSPNEA", App.get(difficultyBreathing).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(difficultyBreathing).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(difficultyBreathing).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

            observations.add(new String[]{"FEVER", App.get(fever).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(fever).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(fever).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

            if (feverDuration.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"FEVER DURATION", App.get(feverDuration).equals(getResources().getString(R.string.pet_less_than_2_weeks)) ? "FEVER LASTING LESS THAN TWO WEEKS" :
                        (App.get(feverDuration).equals(getResources().getString(R.string.pet_two_three_weeks)) ? "FEVER LASTING MORE THAN TWO WEEKS" :
                                (App.get(feverDuration).equals(getResources().getString(R.string.pet_more_than_3_weeks)) ? "FEVER LASTING MORE THAN THREE WEEKS" :
                                        (App.get(feverDuration).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSED")))});

            if (weightLoss.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"WEIGHT LOSS", App.get(weightLoss).equals(getResources().getString(R.string.yes)) ? "YES" :
                        (App.get(weightLoss).equals(getResources().getString(R.string.no)) ? "NO" :
                                (App.get(weightLoss).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

            observations.add(new String[]{"NIGHT SWEATS", App.get(nightSweats).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(nightSweats).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(nightSweats).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

            observations.add(new String[]{"APPETITE", App.get(appetite).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(appetite).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(appetite).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

            observations.add(new String[]{"LETHARGY", App.get(lethargy).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(lethargy).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(lethargy).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

            observations.add(new String[]{"JOINT SWELLING", App.get(swollenJoints).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(swollenJoints).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(swollenJoints).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

            observations.add(new String[]{"BACK PAIN", App.get(backPain).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(backPain).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(backPain).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

            observations.add(new String[]{"ADENOPATHY", App.get(adenopathy).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(adenopathy).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(adenopathy).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

            observations.add(new String[]{"VOMITING", App.get(vomiting).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(vomiting).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(vomiting).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

            observations.add(new String[]{"GASTROINTESTINAL SYMPTOM", App.get(giSymptoms).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(giSymptoms).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(giSymptoms).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});


            if(App.get(giSymptoms).equals(getResources().getString(R.string.yes))){
                observations.add(new String[]{"CHRONIC DIARRHEA LONGER THAN 2 WEEKS", App.get(chronicTwoWeeks).equals(getResources().getString(R.string.yes)) ? "YES" :
                        (App.get(chronicTwoWeeks).equals(getResources().getString(R.string.no)) ? "NO" :
                                (App.get(chronicTwoWeeks).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});


                observations.add(new String[]{"ABDOMINAL PAIN LONGER THAN 2 WEEKS", App.get(abdominalPainTwoWeeks).equals(getResources().getString(R.string.yes)) ? "YES" :
                        (App.get(abdominalPainTwoWeeks).equals(getResources().getString(R.string.no)) ? "NO" :
                                (App.get(abdominalPainTwoWeeks).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});


                observations.add(new String[]{"OTHERS GASTROINTESTINAL SYMPTOM", App.get(otherGISymptoms)});
            }

            observations.add(new String[]{"ALTERATION OF CONSCIOUSNESS / IRRITABILITY / SEIZURES", App.get(alteredLevelConscious).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(alteredLevelConscious).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(alteredLevelConscious).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

            observations.add(new String[]{"PHYSICAL EXAMINATION PERFORMED", App.get(performedPhysicalExamination).equals(getResources().getString(R.string.performed)) ? "PERFORMED" :
                    "NOT ASSESSED"});


            String systemExaminedString = "";
            for (CheckBox cb : closeContactType.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_general_appearance)))
                    systemExaminedString = systemExaminedString + "GENERAL APPEARANCE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_head_eye_ear_nose_throat)))
                    systemExaminedString = systemExaminedString + "HEAD, EARS, EYES, NOSE AND THROAT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.lymph_node_examination)))
                    systemExaminedString = systemExaminedString + "LYMPH NODE EXAMIMATION OF NECK, AXILLA AND GORIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.spine)))
                    systemExaminedString = systemExaminedString + "SPINE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.joints)))
                    systemExaminedString = systemExaminedString + "JOINTS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.skin)))
                    systemExaminedString = systemExaminedString + "SKIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_chest_examination)))
                    systemExaminedString = systemExaminedString + "CHEST EXAMINATION (TEXT)" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_abdominal_examination)))
                    systemExaminedString = systemExaminedString + "ABDOMINAL EXAMINATION (TEXT)" + " ; ";
            }
            if(pageCount!=1) {
                observations.add(new String[]{"SYSTEM EXAMINED", systemExaminedString});
            }

            observations.add(new String[]{"GENERAL APPEARANCE", App.get(generalAppearence).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                    (App.get(generalAppearence).equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) ? "NOT SUGGESTIVE OF TB" : "NORMAL/UNREMARKABLE")});

            if(generalAppearenceExplanation.getVisibility()==View.VISIBLE){
                observations.add(new String[]{"GENERAL APPEARANCE EXPLANATION", App.get(generalAppearenceExplanation)});
            }

            observations.add(new String[]{"HEAD, EARS, EYES, NOSE AND THROAT INTERPRETATION", App.get(heent).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                    (App.get(heent).equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) ? "NOT SUGGESTIVE OF TB" : "NORMAL/UNREMARKABLE")});

            if(heentExplanation.getVisibility()==View.VISIBLE){
                observations.add(new String[]{"HEAD, EARS, EYES, NOSE AND THROAT SECTION TEXT", App.get(heentExplanation)});
            }

            observations.add(new String[]{"LYMPH NODE EXAMIMATION OF NECK, AXILLA AND GORIN INTERPRETATION", App.get(lymphnode).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                    (App.get(lymphnode).equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) ? "NOT SUGGESTIVE OF TB" : "NORMAL/UNREMARKABLE")});

            if(lymphnodeExplanation.getVisibility()==View.VISIBLE){
                observations.add(new String[]{"LYMPH NODE EXAMIMATION OF NECK, AXILLA AND GORIN", App.get(lymphnodeExplanation)});
            }

            observations.add(new String[]{"SPINAL INTERPRETATION", App.get(spine).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                    (App.get(spine).equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) ? "NOT SUGGESTIVE OF TB" : "NORMAL/UNREMARKABLE")});

            if(spineExplanation.getVisibility()==View.VISIBLE){
                observations.add(new String[]{"SPINAL PHYSICAL EXAMINATION (TEXT)", App.get(spineExplanation)});
            }

            observations.add(new String[]{"JOINTS INTERPRETATION", App.get(joints).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                    (App.get(joints).equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) ? "NOT SUGGESTIVE OF TB" : "NORMAL/UNREMARKABLE")});

            if(jointsExplanation.getVisibility()==View.VISIBLE){
                observations.add(new String[]{"JOINTS PHYSICAL EXAMINATION (TEXT)", App.get(jointsExplanation)});
            }

            observations.add(new String[]{"SKIN INTERPRETATION", App.get(skin).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                    (App.get(skin).equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) ? "NOT SUGGESTIVE OF TB" : "NORMAL/UNREMARKABLE")});

            if(skinExplanation.getVisibility()==View.VISIBLE){
                observations.add(new String[]{"SKIN EXAMINATION (TEXT)", App.get(skinExplanation)});
            }

            observations.add(new String[]{"CHEST EXAMINATION INTERPRETATION", App.get(chest).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                    (App.get(chest).equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) ? "NOT SUGGESTIVE OF TB" : "NORMAL/UNREMARKABLE")});

            if(chestExplanation.getVisibility()==View.VISIBLE){
                observations.add(new String[]{"CHEST EXAMINATION (TEXT)", App.get(chestExplanation)});
            }

            observations.add(new String[]{"ABDOMEN INTERPRETATION", App.get(abdominal).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                    (App.get(abdominal).equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) ? "NOT SUGGESTIVE OF TB" : "NORMAL/UNREMARKABLE")});

            if(abdominalExplanation.getVisibility()==View.VISIBLE){
                observations.add(new String[]{"ABDOMINAL EXAMINATION (TEXT)", App.get(abdominalExplanation)});
            }

            if(!App.get(others).isEmpty()){
                observations.add(new String[]{"FREE TEXT COMMENT", App.get(others)});
            }

            observations.add(new String[]{"BACILLUS CALMETTE–GUÉRIN VACCINE", App.get(bcg).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(bcg).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(bcg).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});


            String comorbidCondString = "";
            for (CheckBox cb : closeContactType.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_diabetes)))
                    comorbidCondString = comorbidCondString + "DIABETES MELLITUS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_hypertension)))
                    comorbidCondString = comorbidCondString + "HYPERTENSION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_chronic_renal_disease)))
                    comorbidCondString = comorbidCondString + "CHRONIC RENAL DISEASE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_cardiovascular_disease)))
                    comorbidCondString = comorbidCondString + "CARDIOVASCULAR DISEASE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_congenital_anomalies)))
                    comorbidCondString = comorbidCondString + "CONGENITAL DISORDERS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_chronic_liver_disease)))
                    comorbidCondString = comorbidCondString + "CIRRHOSIS AND CHRONIC LIVER DISEASE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_asthma)))
                    comorbidCondString = comorbidCondString + "ASTHMA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_emphysema)))
                    comorbidCondString = comorbidCondString + "EMPHYSEMA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_copd)))
                    comorbidCondString = comorbidCondString + "CHRONIC OBSTRUCTIVE PULMONARY DISEASE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_hiv)))
                    comorbidCondString = comorbidCondString + "HUMAN IMMUNODEFICIENCY VIRUS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other)))
                    comorbidCondString = comorbidCondString + "OTHER DISEASE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_not_applicable)))
                    comorbidCondString = comorbidCondString + "NOT APPLICABLE" + " ; ";
            }

            if(pageCount!=1) {
                observations.add(new String[]{"CO-MORBID CONDITIONS", comorbidCondString});
            }

            if(otherCondition.getVisibility()==View.VISIBLE){
                observations.add(new String[]{"OTHER DISEASE", App.get(otherCondition)});
            }


            observations.add(new String[]{"SMOKING HISTORY", App.get(smokingHistory).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(smokingHistory).equals(getResources().getString(R.string.no)) ? "NO" :"UNKNOWN")});

            if(dailyCigarettesIntake.getVisibility()==View.VISIBLE)
            {
                observations.add(new String[]{"DAILY CIGARETTE USE", App.get(dailyCigarettesIntake)});
            }

            observations.add(new String[]{"CLOSE CONTACT WITH TB PATIENT", App.get(closeContact).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(closeContact).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(closeContact).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

            String closeContactString = "";
            for (CheckBox cb : closeContactType.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_mother)))
                    closeContactString = closeContactString + "MOTHER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_father)))
                    closeContactString = closeContactString + "FATHER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_brother)))
                    closeContactString = closeContactString + "BROTHER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_sister)))
                    closeContactString = closeContactString + "SISTER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.son)))
                    closeContactString = closeContactString + "SON" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.Daughter)))
                    closeContactString = closeContactString + "DAUGHTER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_paternal_grandfather)))
                    closeContactString = closeContactString + "PATERNAL GRANDFATHER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_paternal_grandmother)))
                    closeContactString = closeContactString + "PATERNAL GRANDMOTHER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_maternal_grandfather)))
                    closeContactString = closeContactString + "MATERNAL GRANDFATHER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_maternal_grandmother)))
                    closeContactString = closeContactString + "MATERNAL GRANDMOTHER" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_other_title)))
                    closeContactString = closeContactString + "OTHER CONTACT TYPE" + " ; ";
            }
            observations.add(new String[]{"CLOSE CONTACT WITH PATIENT", closeContactString});
            if (otherContactType.getVisibility() == View.VISIBLE){
                observations.add(new String[]{"OTHER CONTACT TYPE", App.get(otherContactType)});
            }


            observations.add(new String[]{"INDEX CASE MOTHER OF CONTACT", App.get(exposurePoint1).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(exposurePoint1).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(exposurePoint1).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
            observations.add(new String[]{"INDEX CASE PRIMARY CARETAKER OF CONTACT", App.get(exposurePoint2).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(exposurePoint2).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(exposurePoint2).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
            observations.add(new String[]{"INDEX CASE SHARES BED WITH CONTACT", App.get(exposurePoint3).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(exposurePoint3).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(exposurePoint3).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
            observations.add(new String[]{"INDEX CASE SHARES BEDROOM WITH CONTACT", App.get(exposurePoint4).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(exposurePoint4).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(exposurePoint4).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
            observations.add(new String[]{"INDEX CASE LIVES WITH CONTACT", App.get(exposurePoint5).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(exposurePoint5).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(exposurePoint5).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
            observations.add(new String[]{"INDEX CASE MEETS CONTACT DAILY", App.get(exposurePoint6).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(exposurePoint6).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(exposurePoint6).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
            observations.add(new String[]{"INDEX CASE COUGHING", App.get(exposurePoint7).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(exposurePoint7).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(exposurePoint7).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
            observations.add(new String[]{"INDEX CASE WITH P-TB", App.get(exposurePoint8).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(exposurePoint8).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(exposurePoint8).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
            observations.add(new String[]{"INDEX CASE SMEAR POSITIVE", App.get(exposurePoint9).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(exposurePoint9).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(exposurePoint9).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
            observations.add(new String[]{"MULTIPLE INDEX CASES IN HOUSEHOLD", App.get(exposurePoint10).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(exposurePoint10).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(exposurePoint10).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
            observations.add(new String[]{"EXPOSURE SCORE", App.get(exposureScore)});

            if(conclusion.getVisibility()==View.VISIBLE) {
                observations.add(new String[]{"CONCLUSION", App.get(conclusion).equals(getResources().getString(R.string.ctb_tb_presumptive_confirmed)) ? "TB PRESUMPTIVE CONFIRMED" : "NOT A TB PRESUMPTIVE"});
            }
            if (!App.get(clincianNote).isEmpty()){
                observations.add(new String[]{"CLINICIAN NOTES (TEXT)", App.get(clincianNote)});
            }
            if(followupRequiredCallCenter.getVisibility()==View.VISIBLE) {
                observations.add(new String[]{"CALL CENTRE FOLLOW UP NEEDED", App.get(followupRequiredCallCenter).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
            }
            if(patientVisitFacility.getVisibility()==View.VISIBLE) {
                observations.add(new String[]{"CLINICAL FOLLOWUP NEEDED", App.get(patientVisitFacility).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
            }

            observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDateTime(secondDateCalendar)});


        }



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

                String id = null;
                if(App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocallyTesting(formName, form, formDateCalendar,observations.toArray(new String[][]{}));

                String result = "";
                if(serverService.getLatestEncounterDateTime(App.getPatientId(),"PET-Baseline Screening") == null && serverService.getLatestEncounterDateTime(App.getPatientId(),"PET-Clinician Contact Screening") == null) {
                    result = serverService.saveContactIndexRelationship(App.get(indexPatientId), App.getPatient().getPatientId(), null, id);
                    if (!result.contains("SUCCESS"))
                        return result;
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
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            formDate.getButton().setEnabled(false);

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

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.contact_patient))) {
                closeContact.getRadioGroup().getButtons().get(0).setChecked(true);
                closeContact.setRadioGroupEnabled(false);
                indexPatientId.setVisibility(View.VISIBLE);
                scanQRCode.setVisibility(View.VISIBLE);
                childDiagnosedPresumptive.setVisibility(View.VISIBLE);
                closeContactType.setVisibility(View.VISIBLE);
                //if(App.get(closeContactType))
            } else {
                indexPatientId.setVisibility(View.GONE);
                scanQRCode.setVisibility(View.GONE);
                childDiagnosedPresumptive.setVisibility(View.GONE);
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
            if (App.get(cb).equals(getResources().getString(R.string.ctb_general_appearance))) {
                if (cb.isChecked()) {
                    generalAppearence.setVisibility(View.VISIBLE);
                } else {
                    generalAppearence.setVisibility(View.GONE);
                    generalAppearenceExplanation.setVisibility(View.GONE);
                }
            } if (App.get(cb).equals(getResources().getString(R.string.ctb_head_eye_ear_nose_throat))) {
                if (cb.isChecked()) {
                    heent.setVisibility(View.VISIBLE);
                } else {
                    heent.setVisibility(View.GONE);
                    heentExplanation.setVisibility(View.GONE);
                }
            }if (App.get(cb).equals(getResources().getString(R.string.lymph_node_examination))) {
                if (cb.isChecked()) {
                    lymphnode.setVisibility(View.VISIBLE);
                } else {
                    lymphnode.setVisibility(View.GONE);
                    lymphnodeExplanation.setVisibility(View.GONE);
                }
            }if (App.get(cb).equals(getResources().getString(R.string.spine))) {
                if (cb.isChecked()) {
                    spine.setVisibility(View.VISIBLE);
                } else {
                    spine.setVisibility(View.GONE);
                    spineExplanation.setVisibility(View.GONE);
                }
            }if (App.get(cb).equals(getResources().getString(R.string.joints))) {
                if (cb.isChecked()) {
                    joints.setVisibility(View.VISIBLE);
                } else {
                    joints.setVisibility(View.GONE);
                    jointsExplanation.setVisibility(View.GONE);
                }
            }if (App.get(cb).equals(getResources().getString(R.string.skin))) {
                if (cb.isChecked()) {
                    skin.setVisibility(View.VISIBLE);
                } else {
                    skin.setVisibility(View.GONE);
                    skinExplanation.setVisibility(View.GONE);
                }
            }if (App.get(cb).equals(getResources().getString(R.string.pet_chest_examination))) {
                if (cb.isChecked()) {
                    chest.setVisibility(View.VISIBLE);
                } else {
                    chest.setVisibility(View.GONE);
                    chestExplanation.setVisibility(View.GONE);
                }
            }if (App.get(cb).equals(getResources().getString(R.string.pet_abdominal_examination))) {
                if (cb.isChecked()) {
                    abdominal.setVisibility(View.VISIBLE);
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
                }
            }
            if (App.get(cb).equals(getResources().getString(R.string.ctb_other_title))) {
                if (cb.isChecked()) {
                    otherContactType.setVisibility(View.VISIBLE);
                }else{
                    otherContactType.setVisibility(View.GONE);
                }
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

        } else if (group == generalAppearence.getRadioGroup()) {
            if (App.get(generalAppearence).equals(getResources().getString(R.string.ctb_suggestive_tb)))
                generalAppearenceExplanation.setVisibility(View.VISIBLE);
            else
                generalAppearenceExplanation.setVisibility(View.GONE);
        } else if (group == heent.getRadioGroup()) {

            if (App.get(heent).equals(getResources().getString(R.string.ctb_suggestive_tb)))
                heentExplanation.setVisibility(View.VISIBLE);
            else
                heentExplanation.setVisibility(View.GONE);
        } else if (group == lymphnode.getRadioGroup()) {

            if (App.get(lymphnode).equals(getResources().getString(R.string.ctb_suggestive_tb)))
                lymphnodeExplanation.setVisibility(View.VISIBLE);
            else
                lymphnodeExplanation.setVisibility(View.GONE);
        } else if (group == spine.getRadioGroup()) {

            if (App.get(spine).equals(getResources().getString(R.string.ctb_suggestive_tb)))
                spineExplanation.setVisibility(View.VISIBLE);
            else
                spineExplanation.setVisibility(View.GONE);
        } else if (group == joints.getRadioGroup()) {

            if (App.get(joints).equals(getResources().getString(R.string.ctb_suggestive_tb)))
                jointsExplanation.setVisibility(View.VISIBLE);
            else
                jointsExplanation.setVisibility(View.GONE);
        } else if (group == skin.getRadioGroup()) {

            if (App.get(skin).equals(getResources().getString(R.string.ctb_suggestive_tb)))
                skinExplanation.setVisibility(View.VISIBLE);
            else
                skinExplanation.setVisibility(View.GONE);
        } else if (group == chest.getRadioGroup()) {

            if (App.get(chest).equals(getResources().getString(R.string.ctb_suggestive_tb)))
                chestExplanation.setVisibility(View.VISIBLE);
            else
                chestExplanation.setVisibility(View.GONE);
        } else if (group == abdominal.getRadioGroup()) {

            if (App.get(abdominal).equals(getResources().getString(R.string.ctb_suggestive_tb)))
                abdominalExplanation.setVisibility(View.VISIBLE);
            else
                abdominalExplanation.setVisibility(View.GONE);
        }  else if (group == smokingHistory.getRadioGroup()) {
            if (App.get(smokingHistory).equals(getResources().getString(R.string.yes))) {
                dailyCigarettesIntake.setVisibility(View.VISIBLE);
            } else {
                dailyCigarettesIntake.setVisibility(View.GONE);
            }
        }else if (group == giSymptoms.getRadioGroup()) {
            if (App.get(giSymptoms).equals(getResources().getString(R.string.yes))) {
                chronicTwoWeeks.setVisibility(View.VISIBLE);
                otherGISymptoms.setVisibility(View.VISIBLE);
                abdominalPainTwoWeeks.setVisibility(View.VISIBLE);
            }
            else {
                chronicTwoWeeks.setVisibility(View.GONE);
                otherGISymptoms.setVisibility(View.GONE);
                abdominalPainTwoWeeks.setVisibility(View.GONE);
            }
        }else if (group == performedPhysicalExamination.getRadioGroup()) {
            if (App.get(performedPhysicalExamination).equals(getResources().getString(R.string.performed))) {
                systemsExamined.setVisibility(View.VISIBLE);
            }
            else {
                systemsExamined.setVisibility(View.GONE);
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
        }else if (group == conclusion.getRadioGroup()) {
            if (App.get(conclusion).equals(getResources().getString(R.string.ctb_tb_presumptive_confirmed))) {
                patientVisitFacility.setVisibility(View.VISIBLE);
                followupRequiredCallCenter.setVisibility(View.VISIBLE);

                Toast.makeText(context, getResources().getString(R.string.fill_eof), Toast.LENGTH_SHORT).show();
            }
            else {
                patientVisitFacility.setVisibility(View.GONE);
                followupRequiredCallCenter.setVisibility(View.GONE);
            }
        }else if (group == patientVisitFacility.getRadioGroup()) {
            if (App.get(patientVisitFacility).equals(getResources().getString(R.string.yes))) {
                returnVisitDate.setVisibility(View.VISIBLE);
            }
            else {
                returnVisitDate.setVisibility(View.GONE);
            }
        }else if (group == childDiagnosedPresumptive.getRadioGroup()) {
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
            }
            else {
                if(pageCount==1){
                    pageCount = 5;
                    adapter.notifyDataSetChanged();

                    weight.setVisibility(View.VISIBLE);
                    height.setVisibility(View.VISIBLE);
                    bmi.setVisibility(View.VISIBLE);
                    muac.setVisibility(View.VISIBLE);
                    weightPercentileEditText.setVisibility(View.VISIBLE);
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
                returnVisitDate.setVisibility(View.GONE);
            }
        }else if (group == closeContact.getRadioGroup()) {
            if (App.get(closeContact).equals(getResources().getString(R.string.yes))) {
                closeContactType.setVisibility(View.VISIBLE);
                for (CheckBox cb : closeContactType.getCheckedBoxes()) {
                    if (App.get(cb).equals(getResources().getString(R.string.ctb_other_title))) {
                        if (cb.isChecked()) {
                            otherContactType.setVisibility(View.VISIBLE);
                        } else {
                            otherContactType.setVisibility(View.GONE);
                        }
                    }
                }
            }
            else {
                closeContactType.setVisibility(View.GONE);
                otherContactType.setVisibility(View.GONE);
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

            if(obs[0][0].equals("TIME TAKEN TO FILL form")){
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("CONTACT EXTERNAL ID")) {
                externalPatientId.getEditText().setText(obs[0][1]);
            }else if (obs[0][0].equals("PATIENT SOURCE")) {
                String value = (obs[0][1].equals("IDENTIFIED PATIENT THROUGH SCREENING") ? getResources().getString(R.string.screening) :
                        (obs[0][1].equals("PATIENT REFERRED") ? getResources().getString(R.string.ctb_reffered) :
                                (obs[0][1].equals("TUBERCULOSIS CONTACT") ? getResources().getString(R.string.contact_patient) :
                                        (obs[0][1].equals("WALK IN") ? getResources().getString(R.string.walkin) :
                                                getResources().getString(R.string.ctb_other_title)))));


                patientSource.getSpinner().selectValue(value);
                patientSource.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("OTHER PATIENT SOURCE")) {
                otherPatientSource.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("PATIENT ID OF INDEX CASE")) {
                indexPatientId.getEditText().setText(obs[0][1]);
            }else if (obs[0][0].equals("CHILD DIAGNOSED PRESUMPTIVE BY MO")) {
                for (RadioButton rb : childDiagnosedPresumptive.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("WEIGHT (KG)")) {
                weight.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("HEIGHT (CM)")) {
                height.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("BODY MASS INDEX")) {
                bmi.getEditText().setText(obs[0][1]);
                bmi.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("MID-UPPER ARM CIRCUMFERENCE")) {
                muac.getEditText().setText(obs[0][1]);
                muac.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("WEIGHT PERCENTILE GROUP")) {
                weightPercentileEditText.getEditText().setText(obs[0][1]);
            }



            else if (obs[0][0].equals("COUGH")) {
                for (RadioButton rb : cough.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUGH DURATION")) {
                for (RadioButton rb : coughDuration.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_less_than_2_weeks)) && obs[0][1].equals("COUGH LASTING LESS THAN 2 WEEKS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_two_three_weeks)) && obs[0][1].equals("COUGH LASTING MORE THAN 2 WEEKS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_more_than_3_weeks)) && obs[0][1].equals("COUGH LASTING MORE THAN 3 WEEKS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                coughDuration.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("HEMOPTYSIS")) {
                for (RadioButton rb : haemoptysis.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                haemoptysis.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("DYSPNEA")) {
                for (RadioButton rb : difficultyBreathing.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                difficultyBreathing.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("FEVER")) {
                for (RadioButton rb : fever.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("FEVER DURATION")) {
                for (RadioButton rb : feverDuration.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_less_than_2_weeks)) && obs[0][1].equals("FEVER LASTING LESS THAN TWO WEEKS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_two_three_weeks)) && obs[0][1].equals("FEVER LASTING MORE THAN TWO WEEKS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_more_than_3_weeks)) && obs[0][1].equals("FEVER LASTING MORE THAN THREE WEEKS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                feverDuration.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("WEIGHT LOSS")) {
                for (RadioButton rb : weightLoss.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                weightLoss.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("NIGHT SWEATS")) {
                for (RadioButton rb : nightSweats.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("APPETITE")) {
                for (RadioButton rb : appetite.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("LETHARGY")) {
                for (RadioButton rb : lethargy.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("JOINT SWELLING")) {
                for (RadioButton rb : swollenJoints.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("BACK PAIN")) {
                for (RadioButton rb : backPain.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("ADENOPATHY")) {
                for (RadioButton rb : adenopathy.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("VOMITING")) {
                for (RadioButton rb : vomiting.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("GASTROINTESTINAL SYMPTOM")) {
                for (RadioButton rb : giSymptoms.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }

            } else if (obs[0][0].equals("CHRONIC DIARRHEA LONGER THAN 2 WEEKS")) {
                for (RadioButton rb : chronicTwoWeeks.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }

            }else if (obs[0][0].equals("ABDOMINAL PAIN LONGER THAN 2 WEEKS")) {
                for (RadioButton rb : abdominalPainTwoWeeks.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHERS GASTROINTESTINAL SYMPTOM")) {
                otherGISymptoms.getEditText().setText(obs[0][1]);
            }else if (obs[0][0].equals("ALTERATION OF CONSCIOUSNESS / IRRITABILITY / SEIZURES")) {
                for (RadioButton rb : alteredLevelConscious.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("PHYSICAL EXAMINATION PERFORMED")) {
                for (RadioButton rb : alteredLevelConscious.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.performed)) && obs[0][1].equals("PERFORMED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.not_performed)) && obs[0][1].equals("NOT ASSESSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("SYSTEM EXAMINED")) {
                for (CheckBox cb : closeContactType.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ctb_general_appearance)) && obs[0][1].equals("GENERAL APPEARANCE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_head_eye_ear_nose_throat)) && obs[0][1].equals("HEAD, EARS, EYES, NOSE AND THROAT")) {
                        cb.setChecked(true);
                        break;
                    }else if (cb.getText().equals(getResources().getString(R.string.lymph_node_examination)) && obs[0][1].equals("LYMPH NODE EXAMIMATION OF NECK, AXILLA AND GORIN")) {
                        cb.setChecked(true);
                        break;
                    }else if (cb.getText().equals(getResources().getString(R.string.spine)) && obs[0][1].equals("SPINE")) {
                        cb.setChecked(true);
                        break;
                    }else if (cb.getText().equals(getResources().getString(R.string.joints)) && obs[0][1].equals("JOINTS")) {
                        cb.setChecked(true);
                        break;
                    }else if (cb.getText().equals(getResources().getString(R.string.skin)) && obs[0][1].equals("SKIN")) {
                        cb.setChecked(true);
                        break;
                    }else if (cb.getText().equals(getResources().getString(R.string.pet_chest_examination)) && obs[0][1].equals("CHEST EXAMINATION (TEXT)")) {
                        cb.setChecked(true);
                        break;
                    }else if (cb.getText().equals(getResources().getString(R.string.pet_abdominal_examination)) && obs[0][1].equals("ABDOMINAL EXAMINATION (TEXT)")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                closeContactType.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("GENERAL APPEARANCE")) {
                for (RadioButton rb : generalAppearence.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) && obs[0][1].equals("NOT SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.normal_unremarkable)) && obs[0][1].equals("NORMAL/UNREMARKABLE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("GENERAL APPEARANCE EXPLANATION")) {
                generalAppearenceExplanation.getEditText().setText(obs[0][1]);
                generalAppearenceExplanation.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("HEAD, EARS, EYES, NOSE AND THROAT INTERPRETATION")) {
                for (RadioButton rb : heent.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) && obs[0][1].equals("NOT SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.normal_unremarkable)) && obs[0][1].equals("NORMAL/UNREMARKABLE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("HEAD, EARS, EYES, NOSE AND THROAT SECTION TEXT")) {
                heentExplanation.getEditText().setText(obs[0][1]);
                heentExplanation.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("LYMPH NODE EXAMIMATION OF NECK, AXILLA AND GORIN INTERPRETATION")) {
                for (RadioButton rb : lymphnode.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) && obs[0][1].equals("NOT SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.normal_unremarkable)) && obs[0][1].equals("NORMAL/UNREMARKABLE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("LYMPH NODE EXAMIMATION OF NECK, AXILLA AND GORIN")) {
                lymphnodeExplanation.getEditText().setText(obs[0][1]);
                lymphnodeExplanation.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("SPINAL INTERPRETATION")) {
                for (RadioButton rb : spine.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) && obs[0][1].equals("NOT SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.normal_unremarkable)) && obs[0][1].equals("NORMAL/UNREMARKABLE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("SPINAL PHYSICAL EXAMINATION (TEXT)")) {
                spineExplanation.getEditText().setText(obs[0][1]);
                spineExplanation.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("JOINTS INTERPRETATION")) {
                for (RadioButton rb : joints.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) && obs[0][1].equals("NOT SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.normal_unremarkable)) && obs[0][1].equals("NORMAL/UNREMARKABLE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("JOINTS PHYSICAL EXAMINATION (TEXT)")) {
                jointsExplanation.getEditText().setText(obs[0][1]);
                jointsExplanation.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("SKIN INTERPRETATION")) {
                for (RadioButton rb : skin.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) && obs[0][1].equals("NOT SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.normal_unremarkable)) && obs[0][1].equals("NORMAL/UNREMARKABLE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("SKIN EXAMINATION (TEXT)")) {
                skinExplanation.getEditText().setText(obs[0][1]);
                skinExplanation.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("CHEST EXAMINATION INTERPRETATION")) {
                for (RadioButton rb : chest.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) && obs[0][1].equals("NOT SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.normal_unremarkable)) && obs[0][1].equals("NORMAL/UNREMARKABLE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("CHEST EXAMINATION (TEXT)")) {
                chestExplanation.getEditText().setText(obs[0][1]);
                chestExplanation.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("ABDOMEN INTERPRETATION")) {
                for (RadioButton rb : abdominal.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) && obs[0][1].equals("NOT SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.normal_unremarkable)) && obs[0][1].equals("NORMAL/UNREMARKABLE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("ABDOMINAL EXAMINATION (TEXT)")) {
                abdominalExplanation.getEditText().setText(obs[0][1]);
                abdominalExplanation.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("FREE TEXT COMMENT")) {
                others.getEditText().setText(obs[0][1]);
                others.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("BACILLUS CALMETTE–GUÉRIN VACCINE")) {
                for (RadioButton rb : bcg.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }
            else if (obs[0][0].equals("CO-MORBID CONDITIONS")) {
                for (CheckBox cb : comorbidCondition.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_diabetes)) && obs[0][1].equals("DIABETES MELLITUS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_hypertension)) && obs[0][1].equals("HYPERTENSION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_chronic_renal_disease)) && obs[0][1].equals("CHRONIC RENAL DISEASE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_cardiovascular_disease)) && obs[0][1].equals("CARDIOVASCULAR DISEASE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_congenital_anomalies)) && obs[0][1].equals("CONGENITAL DISORDERS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_chronic_liver_disease)) && obs[0][1].equals("CIRRHOSIS AND CHRONIC LIVER DISEASE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_asthma)) && obs[0][1].equals("ASTHMA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_emphysema)) && obs[0][1].equals("EMPHYSEMA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_copd)) && obs[0][1].equals("CHRONIC OBSTRUCTIVE PULMONARY DISEASE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_hiv)) && obs[0][1].equals("HUMAN IMMUNODEFICIENCY VIRUS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_other)) && obs[0][1].equals("OTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_not_applicable)) && obs[0][1].equals("NOT APPLICABLE")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("OTHER DISEASE")) {
                otherCondition.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("SMOKING HISTORY")) {
                for (RadioButton rb : smokingHistory.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    }  else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("DAILY CIGARETTE USE")) {
                dailyCigarettesIntake.getEditText().setText(obs[0][1]);
                dailyCigarettesIntake.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("CLOSE CONTACT WITH TB PATIENT")) {
                for (RadioButton rb : closeContact.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }
            else if (obs[0][0].equals("CLOSE CONTACT WITH PATIENT")) {
                for (CheckBox cb : closeContactType.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ctb_mother)) && obs[0][1].equals("MOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_father)) && obs[0][1].equals("FATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_brother)) && obs[0][1].equals("BROTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_sister)) && obs[0][1].equals("SISTER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.son)) && obs[0][1].equals("SON")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.Daughter)) && obs[0][1].equals("DAUGHTER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_paternal_grandfather)) && obs[0][1].equals("PATERNAL GRANDFATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_paternal_grandmother)) && obs[0][1].equals("PATERNAL GRANDMOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_maternal_grandfather)) && obs[0][1].equals("MATERNAL GRANDFATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_maternal_grandmother)) && obs[0][1].equals("MATERNAL GRANDMOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_other_title)) && obs[0][1].equals("OTHER CONTACT TYPE")) {
                        cb.setChecked(true);
                        otherContactType.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
            else if (obs[0][0].equals("OTHER CONTACT TYPE")) {
                otherContactType.getEditText().setText(obs[0][1]);
                otherContactType.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("INDEX CASE MOTHER OF CONTACT")) {
                for (RadioButton rb : exposurePoint1.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INDEX CASE PRIMARY CARETAKER OF CONTACT")) {
                for (RadioButton rb : exposurePoint2.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INDEX CASE SHARES BED WITH CONTACT")) {
                for (RadioButton rb : exposurePoint3.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INDEX CASE SHARES BEDROOM WITH CONTACT")) {
                for (RadioButton rb : exposurePoint4.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INDEX CASE LIVES WITH CONTACT")) {
                for (RadioButton rb : exposurePoint5.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INDEX CASE MEETS CONTACT DAILY")) {
                for (RadioButton rb : exposurePoint6.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INDEX CASE COUGHING")) {
                for (RadioButton rb : exposurePoint7.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INDEX CASE WITH P-TB")) {
                for (RadioButton rb : exposurePoint8.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INDEX CASE SMEAR POSITIVE")) {
                for (RadioButton rb : exposurePoint9.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("MULTIPLE INDEX CASES IN HOUSEHOLD")) {
                for (RadioButton rb : exposurePoint10.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("EXPOSURE SCORE")) {
                exposureScore.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("CONCLUSION")) {
                for (RadioButton rb : conclusion.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_tb_presumptive_confirmed)) && obs[0][1].equals("TB PRESUMPTIVE CONFIRMED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_not_tb_presumptive)) && obs[0][1].equals("NOT A TB PRESUMPTIVE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }
            else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
                clincianNote.getEditText().setText(obs[0][1]);
            }

            else if (obs[0][0].equals("CALL CENTRE FOLLOW UP NEEDED")) {
                for (RadioButton rb : followupRequiredCallCenter.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }
            else if (obs[0][0].equals("CLINICAL FOLLOWUP NEEDED")) {
                for (RadioButton rb : followupRequiredCallCenter.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String forthDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(forthDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
                returnVisitDate.setVisibility(View.VISIBLE);
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
