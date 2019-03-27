package com.ihsinformatics.gfatmmobile.pet;

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

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.Barcode;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class PetClinicianContactScreeningForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    TitledButton formDate;
    TitledEditText indexPatientId;
    Button scanQRCode;
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
    TitledRadioGroup lethargy;
    TitledRadioGroup swollenJoints;
    TitledRadioGroup backPain;
    TitledRadioGroup adenopathy;
    TitledRadioGroup vomiting;
    TitledRadioGroup giSymptoms;
    TitledRadioGroup lossInterestInActivity;
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
    TitledRadioGroup examOutcome;
    TitledCheckBoxes comorbidCondition;
    CheckBox otherComorbidCondition;
    TitledEditText otherCondition;
    TitledRadioGroup smokingHistory;
    TitledEditText dailyCigarettesIntake;
    TitledEditText smokingDuration;
    TitledEditText packYears;

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

    TitledEditText clincianNote;

    Boolean refillFlag = false;
    ScrollView scrollView;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 4;
        formName = Forms.PET_CLINICIAN_CONTACT_SCREENING;
        form = Forms.pet_clinicianContactScreening;

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

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        indexPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_id), "", "", RegexUtil.idLength, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, App.HORIZONTAL, true);
        scanQRCode = new Button(context);
        scanQRCode.setText("Scan QR Code");

        externalPatientId = new TitledEditText(context, null, getResources().getString(R.string.external_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        weight = new TitledEditText(context, null, getResources().getString(R.string.pet_weight), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        height = new TitledEditText(context, null, getResources().getString(R.string.pet_height), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        bmi = new TitledEditText(context, null, getResources().getString(R.string.pet_bmi), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        muac = new TitledEditText(context, null, getResources().getString(R.string.pet_muac), "", "", 3, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        weightPercentileEditText = new TitledEditText(context, null, getResources().getString(R.string.pet_weight_percentile), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        //weightPercentile = new TitledSpinner(context, null, getResources().getString(R.string.pet_weight_percentile), getResources().getStringArray(R.array.pet_weight_percentiles), getResources().getString(R.string.pet_less_third_percentile), App.VERTICAL);
        MyLinearLayout linearLayout1 = new MyLinearLayout(context, getResources().getString(R.string.pet_contact_symptom_screen), App.VERTICAL);
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_cough), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        coughDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_cough_duration), getResources().getStringArray(R.array.pet_cough_durations), getResources().getString(R.string.pet_less_than_2_weeks), App.VERTICAL, App.VERTICAL);
        haemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_haemoptysis), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        difficultyBreathing = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_difficulty_breathing), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_fever), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        feverDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_fever_duration), getResources().getStringArray(R.array.pet_cough_durations), getResources().getString(R.string.no), App.VERTICAL, App.VERTICAL);
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_weight_loss), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_night_sweats), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        lethargy = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_lethargy), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        swollenJoints = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_swollen_joints), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        backPain = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_back_pain), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        adenopathy = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adenopathy), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        vomiting = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_vomiting_without_gi_symptoms), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        giSymptoms = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_gi_symptoms), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        lossInterestInActivity = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_lost_activity_interest), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);

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

        MyLinearLayout linearLayout2 = new MyLinearLayout(context, "", App.VERTICAL);
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

        MyLinearLayout linearLayout2a = new MyLinearLayout(context, getResources().getString(R.string.pet_physical_examination), App.VERTICAL);

        generalAppearence = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_general_appearance), getResources().getStringArray(R.array.pet_examination_note), "", App.HORIZONTAL, App.VERTICAL);
        generalAppearenceExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        generalAppearenceExplanation.getEditText().setSingleLine(false);
        generalAppearenceExplanation.getEditText().setMinimumHeight(150);
        heent = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_heent), getResources().getStringArray(R.array.pet_examination_note), "", App.HORIZONTAL, App.VERTICAL);
        heentExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        heentExplanation.getEditText().setSingleLine(false);
        heentExplanation.getEditText().setMinimumHeight(150);
        lymphnode = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_lymphnode), getResources().getStringArray(R.array.pet_examination_note),"", App.HORIZONTAL, App.VERTICAL);
        lymphnodeExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        lymphnodeExplanation.getEditText().setSingleLine(false);
        lymphnodeExplanation.getEditText().setMinimumHeight(150);
        spine = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_spine), getResources().getStringArray(R.array.pet_examination_note), "", App.HORIZONTAL, App.VERTICAL);
        spineExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        spineExplanation.getEditText().setSingleLine(false);
        spineExplanation.getEditText().setMinimumHeight(150);
        joints = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_joints), getResources().getStringArray(R.array.pet_examination_note), "", App.HORIZONTAL, App.VERTICAL);
        jointsExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        jointsExplanation.getEditText().setSingleLine(false);
        jointsExplanation.getEditText().setMinimumHeight(150);
        skin = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_skin), getResources().getStringArray(R.array.pet_examination_note), "", App.HORIZONTAL, App.VERTICAL);
        skinExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        skinExplanation.getEditText().setSingleLine(false);
        skinExplanation.getEditText().setMinimumHeight(150);
        chest = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_chest_examination), getResources().getStringArray(R.array.pet_examination_note), "", App.HORIZONTAL, App.VERTICAL);
        chestExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        chestExplanation.getEditText().setSingleLine(false);
        chestExplanation.getEditText().setMinimumHeight(150);
        abdominal = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_abdominal_examination), getResources().getStringArray(R.array.pet_examination_note), "", App.HORIZONTAL, App.VERTICAL);
        abdominalExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        abdominalExplanation.getEditText().setSingleLine(false);
        abdominalExplanation.getEditText().setMinimumHeight(150);
        others = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        others.getEditText().setSingleLine(false);
        others.getEditText().setMinimumHeight(150);
        examOutcome = new TitledRadioGroup(mainContent.getContext(), "", getResources().getString(R.string.pet_tb_suggestive), getResources().getStringArray(R.array.pet_tb_suggestives), "", App.VERTICAL, App.VERTICAL);

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
        linearLayout2a.addView(examOutcome);

        MyLinearLayout linearLayout3 = new MyLinearLayout(context, "", App.VERTICAL);
        comorbidCondition = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_comorbid_condition), getResources().getStringArray(R.array.pet_comorbid_conditions), null, App.VERTICAL, App.VERTICAL);
        otherCondition = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 15, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        smokingHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_smoking_history), getResources().getStringArray(R.array.yes_no_unknown_options), getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        dailyCigarettesIntake  = new TitledEditText(context, null, getResources().getString(R.string.pet_daily_cigarettes_cosumption), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        smokingDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_smoking_duration), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        packYears = new TitledEditText(context, null, getResources().getString(R.string.pet_pack_years), "", "", 6, null, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);


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

        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.pet_doctor_notes), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        clincianNote.getEditText().setSingleLine(false);
        clincianNote.getEditText().setMinimumHeight(150);

        linearLayout3.addView(comorbidCondition);
        linearLayout3.addView(otherCondition);
        linearLayout3.addView(smokingHistory);
        linearLayout3.addView(dailyCigarettesIntake);
        linearLayout3.addView(smokingDuration);
        linearLayout3.addView(packYears);

        linearLayout3.addView(patientReferred);
        linearLayout3.addView(referredTo);
        linearLayout3.addView(referalReasonPsychologist);
        linearLayout3.addView(otherReferalReasonPsychologist);
        linearLayout3.addView(referalReasonSupervisor);
        linearLayout3.addView(otherReferalReasonSupervisor);
        linearLayout3.addView(referalReasonCallCenter);
        linearLayout3.addView(otherReferalReasonCallCenter);
        linearLayout3.addView(referalReasonClinician);
        linearLayout3.addView(otherReferalReasonClinician);

        linearLayout3.addView(clincianNote);

        views = new View[]{formDate.getButton(), indexPatientId.getEditText(), externalPatientId.getEditText(), weight.getEditText(), height.getEditText(), bmi.getEditText(), muac.getEditText(),
                cough.getRadioGroup(), coughDuration.getRadioGroup(), haemoptysis.getRadioGroup(), difficultyBreathing.getRadioGroup(), fever.getRadioGroup(), feverDuration.getRadioGroup(),
                weightLoss.getRadioGroup(), nightSweats.getRadioGroup(), lethargy.getRadioGroup(), swollenJoints.getRadioGroup(), backPain.getRadioGroup(), adenopathy.getRadioGroup(),
                vomiting.getRadioGroup(), giSymptoms.getRadioGroup(), lossInterestInActivity.getRadioGroup(), exposurePoint1.getRadioGroup(), exposurePoint2.getRadioGroup(), exposurePoint3.getRadioGroup(),
                exposurePoint4.getRadioGroup(), exposurePoint5.getRadioGroup(), exposurePoint6.getRadioGroup(), exposurePoint7.getRadioGroup(), exposurePoint8.getRadioGroup(), exposurePoint9.getRadioGroup(),
                exposurePoint10.getRadioGroup(), exposureScore.getEditText(),
                generalAppearence.getRadioGroup(), generalAppearenceExplanation.getEditText(), heent.getRadioGroup(), heentExplanation.getEditText(), lymphnode.getRadioGroup(), lymphnodeExplanation.getEditText(),
                spine.getRadioGroup(), spineExplanation.getEditText(), joints.getRadioGroup(), jointsExplanation.getEditText(), jointsExplanation.getEditText(), skin.getRadioGroup(), skinExplanation.getEditText(),
                chest.getRadioGroup(), chestExplanation.getEditText(), abdominal.getRadioGroup(), abdominal.getRadioGroup(), examOutcome.getRadioGroup(), comorbidCondition,
                otherCondition.getEditText(), clincianNote.getEditText(), weightPercentileEditText.getEditText(),smokingHistory.getRadioGroup(),
                dailyCigarettesIntake.getEditText(), smokingDuration.getEditText(), packYears.getEditText(),
                patientReferred.getRadioGroup(), referredTo, referalReasonPsychologist, otherReferalReasonPsychologist.getEditText(), referalReasonSupervisor, otherReferalReasonSupervisor.getEditText(),
                referalReasonCallCenter, otherReferalReasonCallCenter.getEditText(), referalReasonClinician, otherReferalReasonClinician.getEditText()};

        viewGroups = new View[][]{{formDate, indexPatientId, scanQRCode, externalPatientId, weight, height, bmi, muac, weightPercentileEditText},
                {linearLayout1},
                {linearLayout2, linearLayout2a},
                {linearLayout3}};

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
        dailyCigarettesIntake.getEditText().addTextChangedListener(new TextWatcher() {
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

                if (App.get(dailyCigarettesIntake).equals("") || App.get(smokingDuration).equals(""))
                    packYears.getEditText().setText("");
                else {

                    int dailyIntake = Integer.parseInt(App.get(dailyCigarettesIntake));
                    int smokingDur = Integer.parseInt(App.get(smokingDuration));

                    Double packY =  (dailyIntake * 1.0) / smokingDur;

                    packYears.getEditText().setText(String.valueOf(packY));
                }


            }
        });
        smokingDuration.getEditText().addTextChangedListener(new TextWatcher() {
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

                if (App.get(dailyCigarettesIntake).equals("") || App.get(smokingDuration).equals(""))
                    packYears.getEditText().setText("");
                else {

                    int dailyIntake = Integer.parseInt(App.get(dailyCigarettesIntake));
                    int smokingDur = Integer.parseInt(App.get(smokingDuration));

                    Double packY =  (dailyIntake * 1.0) / smokingDur;

                    packYears.getEditText().setText(String.valueOf(packY));
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

        packYears.getEditText().setKeyListener(null);
        bmi.getEditText().setKeyListener(null);
        exposureScore.getEditText().setKeyListener(null);
        weightPercentileEditText.getEditText().setKeyListener(null);

        View listenerViewer[] = new View[]{formDate, cough, fever, exposurePoint1, exposurePoint2, exposurePoint3, exposurePoint4, exposurePoint5,
                exposurePoint6, exposurePoint7, exposurePoint8, exposurePoint9, exposurePoint10, abdominal, chest,
                skin, joints, spine, lymphnode, heent, generalAppearence, smokingHistory};
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
        smokingDuration.setVisibility(View.GONE);
        packYears.setVisibility(View.GONE);

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

        if (App.getPatient().getPerson().getAge() <= 14){

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

        if (App.get(smokingDuration).isEmpty() && smokingDuration.getVisibility() == View.VISIBLE) {
            smokingDuration.getEditText().setError(getString(R.string.empty_field));
            smokingDuration.getEditText().requestFocus();
            error = true;
            gotoLastPage();
        } else{
            smokingDuration.getEditText().setError(null);
            smokingDuration.getEditText().clearFocus();
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
                                        smokingDuration.getEditText().clearFocus();
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

        observations.add(new String[]{"PATIENT ID OF INDEX CASE", App.get(indexPatientId)});

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
        observations.add(new String[]{"LOSS OF INTEREST", App.get(lossInterestInActivity).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(lossInterestInActivity).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(lossInterestInActivity).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

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

        if (generalAppearenceExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"GENERAL APPEARANCE EXPLANATION", App.get(generalAppearenceExplanation)});
        if (heentExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HEAD, EARS, EYES, NOSE AND THROAT DESCRIPTION", App.get(heentExplanation)});
        if (spineExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SPINAL PHYSICAL EXAMINATION (TEXT)", App.get(spineExplanation)});
        if (lymphnodeExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"LYMPH NODE EXAMIMATION OF NECK, AXILLA AND GORIN", App.get(lymphnodeExplanation)});
        if (jointsExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"JOINTS PHYSICAL EXAMINATION (TEXT)", App.get(jointsExplanation)});
        if (skinExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SKIN EXAMINATION (TEXT)", App.get(skinExplanation)});
        if (chestExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CHEST EXAMINATION (TEXT)", App.get(chestExplanation)});
        if (abdominalExplanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ABDOMINAL EXAMINATION (TEXT)", App.get(abdominalExplanation)});
        observations.add(new String[]{"FREE TEXT COMMENT", App.get(others)});
        observations.add(new String[]{"TUBERCULOSIS PHYSICAL EXAM OUTCOME", App.get(examOutcome).equals(getResources().getString(R.string.pet_not_tb_suggestive)) ? "NO TB INDICATION" :
                (App.get(examOutcome).equals(getResources().getString(R.string.pet_strongly_tb_suggestive)) ? "STRONGLY SUGGESTIVE OF TB" : "SUGGESTIVE OF TB")});
        String comorbidString = "";
        for (CheckBox cb : comorbidCondition.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_diabetes)))
                comorbidString = comorbidString + "DIABETES MELLITUS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_hypertension)))
                comorbidString = comorbidString + "HYPERTENSION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_chronic_renal_disease)))
                comorbidString = comorbidString + "CHRONIC RENAL DISEASE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_cardiovascular_disease)))
                comorbidString = comorbidString + "CARDIOVASCULAR DISEASE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_asthma)))
                comorbidString = comorbidString + "ASTHMA" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_emphysema)))
                comorbidString = comorbidString + "EMPHYSEMA" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_copd)))
                comorbidString = comorbidString + "CHRONIC OBSTRUCTIVE PULMONARY DISEASE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other)))
                comorbidString = comorbidString + "OTHER" + " ; ";
        }
        observations.add(new String[]{"CO-MORBID CONDITIONS", comorbidString});
        if (otherCondition.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER DISEASE", App.get(otherCondition)});
        observations.add(new String[]{"SMOKING HISTORY", App.get(smokingHistory).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(smokingHistory).equals(getResources().getString(R.string.no)) ? "NO" : "UNKNOWN")});
        if(dailyCigarettesIntake.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DAILY CIGARETTE USE", App.get(dailyCigarettesIntake)});
        if(smokingDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DURATION OF SMOKING (IN YEARS)", App.get(smokingDuration)});
        if(packYears.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PACK YEARS", App.get(packYears)});

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
            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");*/
            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar,false,true, false);

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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView == otherComorbidCondition) {
            if (otherComorbidCondition.isChecked())
                otherCondition.setVisibility(View.VISIBLE);
            else
                otherCondition.setVisibility(View.GONE);
        } else {

            setReferralViews();

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

            if (App.get(generalAppearence).equals(getResources().getString(R.string.pet_remarkable)))
                generalAppearenceExplanation.setVisibility(View.VISIBLE);
            else
                generalAppearenceExplanation.setVisibility(View.GONE);
        } else if (group == heent.getRadioGroup()) {

            if (App.get(heent).equals(getResources().getString(R.string.pet_remarkable)))
                heentExplanation.setVisibility(View.VISIBLE);
            else
                heentExplanation.setVisibility(View.GONE);
        } else if (group == lymphnode.getRadioGroup()) {

            if (App.get(lymphnode).equals(getResources().getString(R.string.pet_remarkable)))
                lymphnodeExplanation.setVisibility(View.VISIBLE);
            else
                lymphnodeExplanation.setVisibility(View.GONE);
        } else if (group == spine.getRadioGroup()) {

            if (App.get(spine).equals(getResources().getString(R.string.pet_remarkable)))
                spineExplanation.setVisibility(View.VISIBLE);
            else
                spineExplanation.setVisibility(View.GONE);
        } else if (group == joints.getRadioGroup()) {

            if (App.get(joints).equals(getResources().getString(R.string.pet_remarkable)))
                jointsExplanation.setVisibility(View.VISIBLE);
            else
                jointsExplanation.setVisibility(View.GONE);
        } else if (group == skin.getRadioGroup()) {

            if (App.get(skin).equals(getResources().getString(R.string.pet_remarkable)))
                skinExplanation.setVisibility(View.VISIBLE);
            else
                skinExplanation.setVisibility(View.GONE);
        } else if (group == chest.getRadioGroup()) {

            if (App.get(chest).equals(getResources().getString(R.string.pet_remarkable)))
                chestExplanation.setVisibility(View.VISIBLE);
            else
                chestExplanation.setVisibility(View.GONE);
        } else if (group == abdominal.getRadioGroup()) {

            if (App.get(abdominal).equals(getResources().getString(R.string.pet_remarkable)))
                abdominalExplanation.setVisibility(View.VISIBLE);
            else
                abdominalExplanation.setVisibility(View.GONE);
        }  else if (group == smokingHistory.getRadioGroup()) {
            if (App.get(smokingHistory).equals(getResources().getString(R.string.yes))) {
                smokingDuration.setVisibility(View.VISIBLE);
                dailyCigarettesIntake.setVisibility(View.VISIBLE);
                packYears.setVisibility(View.VISIBLE);
            }
            else {
                smokingDuration.setVisibility(View.GONE);
                dailyCigarettesIntake.setVisibility(View.GONE);
                packYears.setVisibility(View.GONE);
            }
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
            } else if (obs[0][0].equals("PATIENT ID OF INDEX CASE")) {
                indexPatientId.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("WEIGHT (KG)")) {
                weight.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("HEIGHT (CM)")) {
                height.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("MID-UPPER ARM CIRCUMFERENCE")) {
                muac.getEditText().setText(obs[0][1]);
                muac.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("WEIGHT PERCENTILE GROUP")) {
                weightPercentileEditText.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUGH")) {
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
            } else if (obs[0][0].equals("LOSS OF INTEREST")) {
                for (RadioButton rb : lossInterestInActivity.getRadioGroup().getButtons()) {
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
            } else if (obs[0][0].equals("GENERAL APPEARANCE EXPLANATION")) {
                generalAppearenceExplanation.getEditText().setText(obs[0][1]);
                generalAppearenceExplanation.setVisibility(View.VISIBLE);
                for (RadioButton rb : generalAppearence.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_remarkable)))
                        rb.setChecked(true);
                }
            } else if (obs[0][0].equals("HEAD, EARS, EYES, NOSE AND THROAT DESCRIPTION")) {
                heentExplanation.getEditText().setText(obs[0][1]);
                heentExplanation.setVisibility(View.VISIBLE);
                for (RadioButton rb : heent.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_remarkable))) {
                        rb.setChecked(true);
                    }
                }
            } else if (obs[0][0].equals("SPINAL PHYSICAL EXAMINATION (TEXT)")) {
                spineExplanation.getEditText().setText(obs[0][1]);
                spineExplanation.setVisibility(View.VISIBLE);
                for (RadioButton rb : spine.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_remarkable))) {
                        rb.setChecked(true);
                    }
                }
            } else if (obs[0][0].equals("LYMPH NODE EXAMIMATION OF NECK, AXILLA AND GORIN")) {
                lymphnodeExplanation.getEditText().setText(obs[0][1]);
                lymphnodeExplanation.setVisibility(View.VISIBLE);
                for (RadioButton rb : lymphnode.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_remarkable))) {
                        rb.setChecked(true);
                    }
                }
            } else if (obs[0][0].equals("JOINTS PHYSICAL EXAMINATION (TEXT)")) {
                jointsExplanation.getEditText().setText(obs[0][1]);
                jointsExplanation.setVisibility(View.VISIBLE);
                for (RadioButton rb : joints.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_remarkable))) {
                        rb.setChecked(true);
                    }
                }
            } else if (obs[0][0].equals("SKIN EXAMINATION (TEXT)")) {
                skinExplanation.getEditText().setText(obs[0][1]);
                skinExplanation.setVisibility(View.VISIBLE);
                for (RadioButton rb : skin.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_remarkable))) {
                        rb.setChecked(true);
                    }
                }
            } else if (obs[0][0].equals("CHEST EXAMINATION (TEXT)")) {
                chestExplanation.getEditText().setText(obs[0][1]);
                chestExplanation.setVisibility(View.VISIBLE);
                for (RadioButton rb : chest.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_remarkable))) {
                        rb.setChecked(true);
                    }
                }
            } else if (obs[0][0].equals("ABDOMINAL EXAMINATION (TEXT)")) {
                abdominalExplanation.getEditText().setText(obs[0][1]);
                abdominalExplanation.setVisibility(View.VISIBLE);
                for (RadioButton rb : abdominal.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_remarkable))) {
                        rb.setChecked(true);
                    }
                }
            } else if (obs[0][0].equals("FREE TEXT COMMENT")) {
                others.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("TUBERCULOSIS PHYSICAL EXAM OUTCOME")) {
                for (RadioButton rb : examOutcome.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_not_tb_suggestive)) && obs[0][1].equals("NO TB INDICATION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_strongly_tb_suggestive)) && obs[0][1].equals("STRONGLY SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_tb_suggestive)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("CO-MORBID CONDITIONS")) {
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
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_asthma)) && obs[0][1].equals("ASTHMA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_emphysema)) && obs[0][1].equals("EMPHYSEMA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_copd)) && obs[0][1].equals("CHRONIC OBSTRUCTIVE PULMONARY DISEASE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_other)) && obs[0][1].equals("OTHER")) {
                        cb.setChecked(true);
                        break;
                    }

                }
            } else if (obs[0][0].equals("OTHER DISEASE")) {
                otherCondition.getEditText().setText(obs[0][1]);
                otherCondition.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("SMOKING HISTORY")) {
                for (RadioButton rb : smokingHistory.getRadioGroup().getButtons()) {
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
            } else if (obs[0][0].equals("DAILY CIGARETTE USE")) {
                dailyCigarettesIntake.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("DURATION OF SMOKING (IN YEARS)")) {
                smokingDuration.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("PACK YEARS")) {
                packYears.getEditText().setText(obs[0][1]);
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
            } else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
                clincianNote.getEditText().setText(obs[0][1]);
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
