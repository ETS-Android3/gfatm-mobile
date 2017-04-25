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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class PetClinicianContactScreeningForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    TitledButton formDate;
    TitledEditText weight;
    TitledEditText height;
    TitledEditText bmi;
    TitledEditText muac;
    TitledSpinner weightPercentile;

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
    TitledRadioGroup referral;
    TitledEditText clincianNote;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 4;
        FORM_NAME = Forms.PET_CLINICIAN_CONTACT_SCREENING;
        FORM = Forms.pet_clinicianContactScreening;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(PAGE_COUNT - 1);
        formName.setText(FORM_NAME);

        initViews();

        groups = new ArrayList<ViewGroup>();

        if (App.isLanguageRTL()) {
            for (int i = PAGE_COUNT - 1; i >= 0; i--) {
                LinearLayout layout = new LinearLayout(mainContent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                ScrollView scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        } else {
            for (int i = 0; i < PAGE_COUNT; i++) {
                LinearLayout layout = new LinearLayout(mainContent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                ScrollView scrollView = new ScrollView(mainContent.getContext());
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

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        weight = new TitledEditText(context, null, getResources().getString(R.string.pet_weight), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        height = new TitledEditText(context, null, getResources().getString(R.string.pet_height), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        bmi = new TitledEditText(context, null, getResources().getString(R.string.pet_bmi), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        muac = new TitledEditText(context, null, getResources().getString(R.string.pet_muac), "", "", 3, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        weightPercentile = new TitledSpinner(context, null, getResources().getString(R.string.pet_weight_percentile), getResources().getStringArray(R.array.pet_weight_percentiles), getResources().getString(R.string.pet_less_third_percentile), App.VERTICAL);
        MyLinearLayout linearLayout1 = new MyLinearLayout(context, getResources().getString(R.string.pet_contact_symptom_screen), App.VERTICAL);
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_cough), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        coughDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_cough_duration), getResources().getStringArray(R.array.pet_cough_durations), getResources().getString(R.string.pet_less_than_2_weeks), App.VERTICAL, App.VERTICAL, true);
        haemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_haemoptysis), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        difficultyBreathing = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_difficulty_breathing), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_fever), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        feverDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_fever_duration), getResources().getStringArray(R.array.pet_cough_durations), getResources().getString(R.string.no), App.VERTICAL, App.VERTICAL, true);
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_weight_loss), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_night_sweats), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        lethargy = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_lethargy), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        swollenJoints = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_swollen_joints), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        backPain = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_back_pain), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        adenopathy = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adenopathy), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        vomiting = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_vomiting_without_gi_symptoms), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        giSymptoms = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_gi_symptoms), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        lossInterestInActivity = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_lost_activity_interest), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);

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
        exposurePoint1 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_1), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint2 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_2), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint3 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_3), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint4 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_4), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint5 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_5), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint6 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_6), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint7 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_7), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint8 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_8), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint9 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_9), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposurePoint10 = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_exposure_point_10), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        exposureScore = new TitledEditText(context, null, getResources().getString(R.string.pet_exposure_score), "0", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_NUMBER_FLAG_DECIMAL, App.HORIZONTAL, true);
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

        MyLinearLayout linearLayout3 = new MyLinearLayout(context, getResources().getString(R.string.pet_physical_examination), App.VERTICAL);
        generalAppearence = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_general_appearance), getResources().getStringArray(R.array.pet_examination_note), getResources().getString(R.string.pet_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        generalAppearenceExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        generalAppearenceExplanation.getEditText().setSingleLine(false);
        generalAppearenceExplanation.getEditText().setMinimumHeight(150);
        heent = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_heent), getResources().getStringArray(R.array.pet_examination_note), getResources().getString(R.string.pet_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        heentExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        heentExplanation.getEditText().setSingleLine(false);
        heentExplanation.getEditText().setMinimumHeight(150);
        lymphnode = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_lymphnode), getResources().getStringArray(R.array.pet_examination_note), getResources().getString(R.string.pet_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        lymphnodeExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        lymphnodeExplanation.getEditText().setSingleLine(false);
        lymphnodeExplanation.getEditText().setMinimumHeight(150);
        spine = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_spine), getResources().getStringArray(R.array.pet_examination_note), getResources().getString(R.string.pet_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        spineExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        spineExplanation.getEditText().setSingleLine(false);
        spineExplanation.getEditText().setMinimumHeight(150);
        joints = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_joints), getResources().getStringArray(R.array.pet_examination_note), getResources().getString(R.string.pet_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        jointsExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        jointsExplanation.getEditText().setSingleLine(false);
        jointsExplanation.getEditText().setMinimumHeight(150);
        skin = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_skin), getResources().getStringArray(R.array.pet_examination_note), getResources().getString(R.string.pet_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        skinExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        skinExplanation.getEditText().setSingleLine(false);
        skinExplanation.getEditText().setMinimumHeight(150);
        chest = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_chest_examination), getResources().getStringArray(R.array.pet_examination_note), getResources().getString(R.string.pet_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        chestExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        chestExplanation.getEditText().setSingleLine(false);
        chestExplanation.getEditText().setMinimumHeight(150);
        abdominal = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_abdominal_examination), getResources().getStringArray(R.array.pet_examination_note), getResources().getString(R.string.pet_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        abdominalExplanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        abdominalExplanation.getEditText().setSingleLine(false);
        abdominalExplanation.getEditText().setMinimumHeight(150);
        others = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        others.getEditText().setSingleLine(false);
        others.getEditText().setMinimumHeight(150);
        examOutcome = new TitledRadioGroup(mainContent.getContext(), "", getResources().getString(R.string.pet_tb_suggestive), getResources().getStringArray(R.array.pet_tb_suggestives), getResources().getString(R.string.pet_not_tb_suggestive), App.VERTICAL, App.VERTICAL);
        comorbidCondition = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_comorbid_condition), getResources().getStringArray(R.array.pet_comorbid_conditions), null, App.VERTICAL, App.VERTICAL);
        otherCondition = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 15, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        referral = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_referral_needed), getResources().getStringArray(R.array.yes_no_unknown_options), getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.pet_doctor_notes), "", "", 250, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        clincianNote.getEditText().setSingleLine(false);
        clincianNote.getEditText().setMinimumHeight(150);

        linearLayout3.addView(generalAppearence);
        linearLayout3.addView(generalAppearenceExplanation);
        linearLayout3.addView(heent);
        linearLayout3.addView(heentExplanation);
        linearLayout3.addView(lymphnode);
        linearLayout3.addView(lymphnodeExplanation);
        linearLayout3.addView(spine);
        linearLayout3.addView(spineExplanation);
        linearLayout3.addView(joints);
        linearLayout3.addView(jointsExplanation);
        linearLayout3.addView(skin);
        linearLayout3.addView(skinExplanation);
        linearLayout3.addView(chest);
        linearLayout3.addView(chestExplanation);
        linearLayout3.addView(abdominal);
        linearLayout3.addView(abdominalExplanation);
        linearLayout3.addView(others);
        linearLayout3.addView(examOutcome);
        linearLayout3.addView(comorbidCondition);
        linearLayout3.addView(otherCondition);
        linearLayout3.addView(referral);
        linearLayout3.addView(clincianNote);

        views = new View[]{formDate.getButton(), weight.getEditText(), height.getEditText(), bmi.getEditText(), muac.getEditText(), weightPercentile.getSpinner(),
                cough.getRadioGroup(), coughDuration.getRadioGroup(), haemoptysis.getRadioGroup(), difficultyBreathing.getRadioGroup(), fever.getRadioGroup(), feverDuration.getRadioGroup(),
                weightLoss.getRadioGroup(), nightSweats.getRadioGroup(), lethargy.getRadioGroup(), swollenJoints.getRadioGroup(), backPain.getRadioGroup(), adenopathy.getRadioGroup(),
                vomiting.getRadioGroup(), giSymptoms.getRadioGroup(), lossInterestInActivity.getRadioGroup(), exposurePoint1.getRadioGroup(), exposurePoint2.getRadioGroup(), exposurePoint3.getRadioGroup(),
                exposurePoint4.getRadioGroup(), exposurePoint5.getRadioGroup(), exposurePoint6.getRadioGroup(), exposurePoint7.getRadioGroup(), exposurePoint8.getRadioGroup(), exposurePoint9.getRadioGroup(),
                exposurePoint10.getRadioGroup(), exposureScore.getEditText(),
                generalAppearence.getRadioGroup(), generalAppearenceExplanation.getEditText(), heent.getRadioGroup(), heentExplanation.getEditText(), lymphnode.getRadioGroup(), lymphnodeExplanation.getEditText(),
                spine.getRadioGroup(), spineExplanation.getEditText(), joints.getRadioGroup(), jointsExplanation.getEditText(), jointsExplanation.getEditText(), skin.getRadioGroup(), skinExplanation.getEditText(),
                chest.getRadioGroup(), chestExplanation.getEditText(), abdominal.getRadioGroup(), abdominal.getRadioGroup(), examOutcome.getRadioGroup(), comorbidCondition,
                otherCondition.getEditText(), referral.getRadioGroup(), clincianNote.getEditText()};

        viewGroups = new View[][]{{formDate, weight, height, bmi, muac, weightPercentile},
                {linearLayout1},
                {linearLayout2},
                {linearLayout3}};

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

                if (App.get(weight).equals("") || App.get(height).equals(""))
                    bmi.getEditText().setText("");
                else {

                    int w = Integer.parseInt(App.get(weight));
                    int h = Integer.parseInt(App.get(height));

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

                    bmi.getEditText().setText(result + "   -   " + bmiCategory);

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

                if (App.get(weight).equals("") || App.get(height).equals(""))
                    bmi.getEditText().setText("");
                else {

                    int w = Integer.parseInt(App.get(weight));
                    int h = Integer.parseInt(App.get(height));

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

                    bmi.getEditText().setText(result + "   -   " + bmiCategory);
                }


            }
        });
        bmi.getEditText().setKeyListener(null);
        exposureScore.getEditText().setKeyListener(null);

        View listenerViewer[] = new View[]{formDate, cough, fever, exposurePoint1, exposurePoint2, exposurePoint3, exposurePoint4, exposurePoint5,
                exposurePoint6, exposurePoint7, exposurePoint8, exposurePoint9, exposurePoint10, abdominal, chest,
                skin, joints, spine, lymphnode, heent, generalAppearence};
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

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
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

        if (App.getPatient().getPerson().getAge() < 6)
            muac.setVisibility(View.VISIBLE);
        else
            muac.setVisibility(View.GONE);

        if (App.getPatient().getPerson().getAge() < 18)
            weightPercentile.setVisibility(View.VISIBLE);
        else
            weightPercentile.setVisibility(View.GONE);

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
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }
    }


    @Override
    public boolean validate() {

        Boolean error = false;

        if (App.get(abdominalExplanation).isEmpty() && abdominalExplanation.getVisibility() == View.VISIBLE) {
            abdominalExplanation.getEditText().setError(getString(R.string.empty_field));
            abdominalExplanation.getEditText().requestFocus();
            error = true;
            gotoLastPage();
        }

        if (App.get(chestExplanation).isEmpty() && chestExplanation.getVisibility() == View.VISIBLE) {
            chestExplanation.getEditText().setError(getString(R.string.empty_field));
            chestExplanation.getEditText().requestFocus();
            error = true;
            gotoLastPage();
        }

        if (App.get(skinExplanation).isEmpty() && skinExplanation.getVisibility() == View.VISIBLE) {
            skinExplanation.getEditText().setError(getString(R.string.empty_field));
            skinExplanation.getEditText().requestFocus();
            error = true;
            gotoLastPage();
        }

        if (App.get(spineExplanation).isEmpty() && spineExplanation.getVisibility() == View.VISIBLE) {
            gotoLastPage();
            spineExplanation.getEditText().setError(getString(R.string.empty_field));
            spineExplanation.getEditText().requestFocus();
            error = true;
        }

        if (App.get(lymphnodeExplanation).isEmpty() && lymphnodeExplanation.getVisibility() == View.VISIBLE) {
            gotoLastPage();
            lymphnodeExplanation.getEditText().setError(getString(R.string.empty_field));
            lymphnodeExplanation.getEditText().requestFocus();
            error = true;
        }

        if (App.get(heentExplanation).isEmpty() && heentExplanation.getVisibility() == View.VISIBLE) {
            gotoLastPage();
            heentExplanation.getEditText().setError(getString(R.string.empty_field));
            heentExplanation.getEditText().requestFocus();
            error = true;
        }

        if (App.get(generalAppearenceExplanation).isEmpty() && generalAppearenceExplanation.getVisibility() == View.VISIBLE) {
            gotoLastPage();
            generalAppearenceExplanation.getEditText().setError(getString(R.string.empty_field));
            generalAppearenceExplanation.getEditText().requestFocus();
            error = true;
        }

        if (App.get(muac).isEmpty() && muac.getVisibility() == View.VISIBLE) {
            gotoFirstPage();
            muac.getEditText().setError(getString(R.string.empty_field));
            muac.getEditText().requestFocus();
            error = true;
        }

        if (App.get(height).isEmpty()) {
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
        }

        if (error) {

            final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
            alertDialog.setMessage(getResources().getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            // DrawableCompat.setTint(clearIcon, color);
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
            return false;
        } else
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

        observations.add(new String[]{"WEIGHT (KG)", App.get(weight)});
        observations.add(new String[]{"HEIGHT (CM)", App.get(height)});
        String[] bmiString = App.get(bmi).split(" - ");
        observations.add(new String[]{"BODY MASS INDEX", bmiString[0]});
        if (muac.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"MID-UPPER ARM CIRCUMFERENCE", App.get(muac)});
        if (weightPercentile.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"WEIGHT PERCENTILE GROUP", App.get(weightPercentile).equals(getResources().getString(R.string.pet_less_third_percentile)) ? "<3rd Centile" :
                (App.get(weightPercentile).equals(getResources().getString(R.string.pet_third_percentile)) ? "3rd Centile" :
                        (App.get(weightPercentile).equals(getResources().getString(R.string.pet_fifth_percentile)) ? "5th Centile" :
                                (App.get(weightPercentile).equals(getResources().getString(R.string.pet_tenth_percentile)) ? "10th Centile" :
                                        (App.get(weightPercentile).equals(getResources().getString(R.string.pet_tenth_twenty_percentile)) ? "between 10-25th Centile" :
                                                (App.get(weightPercentile).equals(getResources().getString(R.string.pet_twenty_fifth_percentile)) ? "25th Centile" :
                                                        (App.get(weightPercentile).equals(getResources().getString(R.string.pet_twenty_fitfy_percentile)) ? "between 25-50th Centile" :
                                                                (App.get(weightPercentile).equals(getResources().getString(R.string.pet_fifty_percentile)) ? "50th Centile" :
                                                                        (App.get(weightPercentile).equals(getResources().getString(R.string.pet_fifth_sevety_percentile)) ? "between 50-75th Centile" :
                                                                                (App.get(weightPercentile).equals(getResources().getString(R.string.pet_seventy_fifth_percentile)) ? "75th Centile" :
                                                                                        (App.get(weightPercentile).equals(getResources().getString(R.string.pet_seventy_hundredth_percentile)) ? "between 75-100th Centile" :
                                                                                                (App.get(weightPercentile).equals(getResources().getString(R.string.pet_hundreth_percentile)) ? "100th Centile" : ">100th Centile")))))))))))});

        observations.add(new String[]{"COUGH", App.get(cough).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(cough).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(cough).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        if (coughDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUGH DURATION", App.get(coughDuration).equals(getResources().getString(R.string.pet_less_than_2_weeks)) ? "FEVER LASTING LESS THAN TWO WEEKS" :
                    (App.get(coughDuration).equals(getResources().getString(R.string.pet_two_three_weeks)) ? "FEVER LASTING MORE THAN TWO WEEKS" :
                            (App.get(coughDuration).equals(getResources().getString(R.string.pet_more_than_3_weeks)) ? "COUGH LASTING MORE THAN 3 WEEKS" :
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
        observations.add(new String[]{"PATIENT REFERRED", App.get(referral).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(referral).equals(getResources().getString(R.string.yes)) ? "NO" : "UNKNOWN")});
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

                String result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
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
                    resetViews();

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

        if (buttonView == otherComorbidCondition) {
            if (otherComorbidCondition.isChecked())
                otherCondition.setVisibility(View.VISIBLE);
            else
                otherCondition.setVisibility(View.GONE);
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
        }

    }

    @Override
    public void refill(int encounterId) {


        OfflineForm fo = serverService.getOfflineFormById(encounterId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("FORM START TIME")) {
                startTime = App.stringToDate(obs[0][1], "yyyy-MM-dd hh:mm:ss");
            } else if (obs[0][0].equals("WEIGHT (KG)")) {
                weight.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("HEIGHT (CM)")) {
                height.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("MID-UPPER ARM CIRCUMFERENCE")) {
                muac.getEditText().setText(obs[0][1]);
                muac.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("WEIGHT PERCENTILE GROUP")) {
                weightPercentile.getSpinner().selectValue(obs[0][1]);
                weightPercentile.setVisibility(View.VISIBLE);
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
            } else if (obs[0][0].equals("PATIENT REFERRED")) {
                for (RadioButton rb : referral.getRadioGroup().getButtons()) {
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
            } else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
                clincianNote.getEditText().setText(obs[0][1]);
            }
        }
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return PAGE_COUNT;
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
