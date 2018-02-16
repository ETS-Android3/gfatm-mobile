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
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyEditText;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
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
import java.util.HashMap;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class PetTreatmentInitiationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledRadioGroup intervention;
    TitledEditText weight;
    TitledEditText indexPatientId;
    TitledRadioGroup tbType;
    TitledRadioGroup infectionType;
    TitledRadioGroup resistanceType;
    TitledCheckBoxes dstPattern;

    TitledButton treatmentInitiationDate;
    TitledRadioGroup petRegimen;
    TitledRadioGroup rifapentineAvailable;
    TitledEditText isoniazidDose;
    TitledEditText rifapentineDose;
    TitledEditText levofloxacinDose;
    TitledEditText ethionamideDose;
    TitledEditText ethambutolDose;
    TitledEditText moxifloxacilinDose;

    TitledRadioGroup ancillaryNeed;
    TitledCheckBoxes ancillaryDrugs;
    TitledEditText ancillaryDrugDuration;

    TitledEditText nameTreatmentSupporter;
    LinearLayout contactNumberTreatmentSupporter;
    MyEditText phone1a;
    MyEditText phone1b;
    TitledRadioGroup typeTreatmentSupporter;
    TitledSpinner relationshipTreatmentSuppoter;
    TitledEditText other;

    TitledEditText clincianNote;

    Boolean refillFlag = false;

    ScrollView scrollView;

    /**
     * CHANGE PAGE_COUNT and FORM_NAME Variable only...
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 2;
        FORM_NAME = Forms.PET_TREATMENT_INITIATION;
        FORM = Forms.pet_treatmentInitiation;

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
                scrollView = new ScrollView(mainContent.getContext());
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
        intervention = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_intervention), getResources().getStringArray(R.array.pet_interventions), "", App.HORIZONTAL, App.VERTICAL);
        weight = new TitledEditText(context, null, getResources().getString(R.string.pet_weight), "", "", 5, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        indexPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_id), "", "", RegexUtil.idLength, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        indexPatientId.getEditText().setKeyListener(null);
        tbType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_tb_type), getResources().getStringArray(R.array.pet_tb_types), "", App.HORIZONTAL, App.VERTICAL);
        tbType.setRadioGroupEnabled(false);
        infectionType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_infection_type), getResources().getStringArray(R.array.pet_infection_types), "", App.HORIZONTAL, App.VERTICAL);
        infectionType.setRadioGroupEnabled(false);
        resistanceType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_resistance_Type), getResources().getStringArray(R.array.pet_resistance_types), "", App.VERTICAL, App.VERTICAL);
        resistanceType.setRadioGroupEnabled(false);
        dstPattern = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_dst_pattern), getResources().getStringArray(R.array.pet_dst_patterns), null, App.VERTICAL, App.VERTICAL, true);
        dstPattern.setCheckedBoxesEnabled(false);

        // second page view
        treatmentInitiationDate = new TitledButton(context, null, getResources().getString(R.string.pet_treatment_initiation_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.VERTICAL);
        petRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_regimen), getResources().getStringArray(R.array.pet_regimens), "", App.VERTICAL, App.VERTICAL);
        isoniazidDose = new TitledEditText(context, null, getResources().getString(R.string.pet_isoniazid_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        rifapentineAvailable = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_rifapentine_available), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        rifapentineDose = new TitledEditText(context, null, getResources().getString(R.string.pet_rifapentine_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        levofloxacinDose = new TitledEditText(context, null, getResources().getString(R.string.pet_levofloxacin_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        ethionamideDose = new TitledEditText(context, null, getResources().getString(R.string.pet_ethionamide_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        ethambutolDose = new TitledEditText(context, null, getResources().getString(R.string.pet_ethambutol_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        moxifloxacilinDose = new TitledEditText(context, null, getResources().getString(R.string.pet_moxifloxacilin_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);

        // third page view
        ancillaryNeed = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_ancillary_drugs_needed), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        ancillaryDrugs = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_ancillary_drugs), getResources().getStringArray(R.array.pet_ancillary_drugs), null, App.VERTICAL, App.VERTICAL, true);
        ancillaryDrugDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_ancillary_drug_duration_days), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);

        // fourth page view
        nameTreatmentSupporter = new TitledEditText(context, null, getResources().getString(R.string.pet_treatment_supporter_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        contactNumberTreatmentSupporter = new LinearLayout(context);
        contactNumberTreatmentSupporter.setOrientation(LinearLayout.VERTICAL);
        LinearLayout phone1QuestionLayout = new LinearLayout(context);
        phone1QuestionLayout.setOrientation(LinearLayout.HORIZONTAL);
        MyTextView phone1Text = new MyTextView(context, getResources().getString(R.string.pet_treatment_supporter_contact_number));
        phone1QuestionLayout.addView(phone1Text);
        contactNumberTreatmentSupporter.addView(phone1QuestionLayout);
        LinearLayout phone1PartLayout = new LinearLayout(context);
        phone1PartLayout.setOrientation(LinearLayout.HORIZONTAL);
        phone1a = new MyEditText(context,"", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        phone1a.setHint("0XXX");
        phone1PartLayout.addView(phone1a);
        MyTextView phone1Dash = new MyTextView(context, " - ");
        phone1PartLayout.addView(phone1Dash);
        phone1b = new MyEditText(context,"",  7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        phone1b.setHint("XXXXXXX");
        phone1PartLayout.addView(phone1b);
        contactNumberTreatmentSupporter.addView(phone1PartLayout);
        typeTreatmentSupporter = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_treatment_supporter_type), getResources().getStringArray(R.array.pet_treatment_supporter_type), "", App.VERTICAL, App.VERTICAL);
        relationshipTreatmentSuppoter = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_treatment_supporter_relationship), getResources().getStringArray(R.array.pet_household_heads_required), "", App.VERTICAL);
        other = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.pet_doctor_notes), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        clincianNote.getEditText().setSingleLine(false);
        clincianNote.getEditText().setMinimumHeight(150);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), weight.getEditText(), indexPatientId.getEditText(), tbType.getRadioGroup(), infectionType.getRadioGroup(), dstPattern, resistanceType.getRadioGroup(),
                treatmentInitiationDate.getButton(), petRegimen.getRadioGroup(), isoniazidDose.getEditText(), rifapentineDose.getEditText(), levofloxacinDose.getEditText(), ethionamideDose.getEditText(), ethambutolDose.getEditText(), moxifloxacilinDose.getEditText(),
                ancillaryNeed.getRadioGroup(), ancillaryDrugs, ancillaryDrugDuration.getEditText(), ancillaryDrugDuration.getEditText(), clincianNote.getEditText(), intervention.getRadioGroup(),
                nameTreatmentSupporter.getEditText(), phone1a, phone1b, typeTreatmentSupporter.getRadioGroup(), relationshipTreatmentSuppoter.getSpinner(), other.getEditText(), rifapentineAvailable.getRadioGroup(),
                intervention};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, intervention, weight, indexPatientId, tbType, infectionType, resistanceType, dstPattern},
                        {treatmentInitiationDate, petRegimen, rifapentineAvailable, isoniazidDose, rifapentineDose, levofloxacinDose, ethionamideDose, ethambutolDose, moxifloxacilinDose, ancillaryNeed, ancillaryDrugs, ancillaryDrugDuration, nameTreatmentSupporter, contactNumberTreatmentSupporter, typeTreatmentSupporter, relationshipTreatmentSuppoter, other, clincianNote},
                };

        formDate.getButton().setOnClickListener(this);
        treatmentInitiationDate.getButton().setOnClickListener(this);
        petRegimen.getRadioGroup().setOnCheckedChangeListener(this);
        ancillaryNeed.getRadioGroup().setOnCheckedChangeListener(this);
        typeTreatmentSupporter.getRadioGroup().setOnCheckedChangeListener(this);
        relationshipTreatmentSuppoter.getSpinner().setOnItemSelectedListener(this);
        rifapentineAvailable.getRadioGroup().setOnCheckedChangeListener(this);

        weight.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                isoniazidDose.getEditText().setHint("");
                rifapentineDose.getEditText().setHint("");
                levofloxacinDose.getEditText().setHint("");
                ethionamideDose.getEditText().setHint("");
                ethambutolDose.getEditText().setHint("");
                moxifloxacilinDose.getEditText().setHint("");

                isoniazidDose.getEditText().setText("");
                rifapentineDose.getEditText().setText("");
                levofloxacinDose.getEditText().setText("");
                ethionamideDose.getEditText().setText("");
                ethambutolDose.getEditText().setText("");
                moxifloxacilinDose.getEditText().setText("");


                int age = App.getPatient().getPerson().getAge();
                int weightInt = 0;
                if (!App.get(weight).equals("")) {
                    weightInt = Integer.parseInt(App.get(weight));
                }

                if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy))) {

                    int w = 1;
                    w = weightInt * 10;

                    isoniazidDose.getEditText().setText(String.valueOf(w));

                    isoniazidDose.setVisibility(View.VISIBLE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.GONE);
                    ethionamideDose.setVisibility(View.GONE);
                    rifapentineAvailable.setVisibility(View.GONE);
                    ethambutolDose.setVisibility(View.GONE);
                    moxifloxacilinDose.setVisibility(View.GONE);

                } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine))) {

                    int w = 1;

                    rifapentineAvailable.setVisibility(View.VISIBLE);
                    if (App.get(rifapentineAvailable).equals(getResources().getString(R.string.no))) {

                        isoniazidDose.setVisibility(View.VISIBLE);
                        rifapentineDose.setVisibility(View.GONE);
                        levofloxacinDose.setVisibility(View.GONE);
                        ethionamideDose.setVisibility(View.GONE);
                        ethambutolDose.setVisibility(View.GONE);
                        moxifloxacilinDose.setVisibility(View.GONE);

                        if (age < 12) w = weightInt * 10;
                        else if (age < 15) w = weightInt * 10;
                        else w = weightInt * 5;

                        isoniazidDose.getEditText().setText(String.valueOf(w));

                    } else {

                        w = 1;
                        w = weightInt * 15;
                        isoniazidDose.getEditText().setText(String.valueOf(w));

                        /*rifapentineDose.getEditText().setText("");
                        if (age < 12)
                            rifapentineDose.getEditText().setHint("300 - 450 mg");
                        else
                            rifapentineDose.getEditText().setHint("450 - 900 mg");*/

                        isoniazidDose.setVisibility(View.VISIBLE);
                        rifapentineDose.setVisibility(View.VISIBLE);
                        levofloxacinDose.setVisibility(View.GONE);
                        ethionamideDose.setVisibility(View.GONE);
                        ethambutolDose.setVisibility(View.GONE);
                        moxifloxacilinDose.setVisibility(View.GONE);

                    }

                } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethionamide))) {

                    int w = 1;

                    if (age < 15) {
                        ethionamideDose.getEditText().setText("");
                        ethionamideDose.getEditText().setHint("15 - 20 mg/kg");
                    } else {
                        ethionamideDose.getEditText().setText("");
                        ethionamideDose.getEditText().setHint("500 - 1000 mg");
                    }

                    Double ww = 1.0;
                    if (age < 2) {
                        ww = weightInt * 15.0;
                        int i = (int) Math.round(ww);
                        levofloxacinDose.getEditText().setText(String.valueOf(i));
                    } else if (age < 15) {
                        levofloxacinDose.getEditText().setText(String.valueOf(""));
                        levofloxacinDose.getEditText().setHint("7.5 - 10 mg/kg");
                    } else {
                        levofloxacinDose.getEditText().setText(String.valueOf(""));
                        levofloxacinDose.getEditText().setHint("750 - 1000 mg");
                    }

                    isoniazidDose.setVisibility(View.GONE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.VISIBLE);
                    ethionamideDose.setVisibility(View.VISIBLE);
                    rifapentineAvailable.setVisibility(View.GONE);
                    ethambutolDose.setVisibility(View.GONE);
                    moxifloxacilinDose.setVisibility(View.GONE);

                } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethionamide))) {

                    int w = 1;

                    if (age < 15) {
                        ethionamideDose.getEditText().setText("");
                        ethionamideDose.getEditText().setHint("15 - 20 mg/kg");
                    } else {
                        ethionamideDose.getEditText().setText("");
                        ethionamideDose.getEditText().setHint("500 - 1000 mg");
                    }

                    Double ww = 1.0;
                    if (age < 2) {
                        ww = weightInt * 15.0;
                        int i = (int) Math.round(ww);
                        levofloxacinDose.getEditText().setText(String.valueOf(i));
                    } else if (age < 15) {
                        levofloxacinDose.getEditText().setText(String.valueOf(""));
                        levofloxacinDose.getEditText().setHint("7.5 - 10 mg/kg");
                    } else {
                        levofloxacinDose.getEditText().setText(String.valueOf(""));
                        levofloxacinDose.getEditText().setHint("750 - 1000 mg");
                    }

                    isoniazidDose.setVisibility(View.GONE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.VISIBLE);
                    ethionamideDose.setVisibility(View.VISIBLE);
                    rifapentineAvailable.setVisibility(View.GONE);

                } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethambutol))) {

                    if(weightInt == 0){
                        levofloxacinDose.getEditText().setText("");
                        ethambutolDose.getEditText().setText("");
                    } else {

                        Double ww = 1.0;
                        if (age < 2) {
                            ww = weightInt * 15.0;
                            int i = (int) Math.round(ww);
                            levofloxacinDose.getEditText().setText(String.valueOf(i));
                        } else if (age < 15) {
                            levofloxacinDose.getEditText().setText(String.valueOf(""));
                            levofloxacinDose.getEditText().setHint("7.5 - 10 mg/kg");
                        } else {
                            levofloxacinDose.getEditText().setText(String.valueOf(""));
                            levofloxacinDose.getEditText().setHint("750 - 1000 mg");
                        }

                        if(weightInt <= 2){
                            ethambutolDose.getEditText().setText(String.valueOf(""));
                            ethambutolDose.getEditText().setHint("Not recommended");
                        } else if (weightInt <= 7){
                            ethambutolDose.getEditText().setText(String.valueOf("100"));
                        } else if (weightInt <= 12){
                            ethambutolDose.getEditText().setText(String.valueOf("200"));
                        } else if (weightInt <= 15){
                            ethambutolDose.getEditText().setText(String.valueOf("300"));
                        } else if (weightInt <= 26){
                            ethambutolDose.getEditText().setText(String.valueOf("400"));
                        } else if (weightInt <= 30){
                            ethambutolDose.getEditText().setText(String.valueOf("500"));
                        } else if (weightInt <= 59){
                            ethambutolDose.getEditText().setText(String.valueOf("1500"));
                        } else {
                            ethambutolDose.getEditText().setText(String.valueOf("2000"));
                        }

                    }

                    isoniazidDose.setVisibility(View.GONE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.VISIBLE);
                    ethionamideDose.setVisibility(View.GONE);
                    rifapentineAvailable.setVisibility(View.GONE);
                    ethambutolDose.setVisibility(View.VISIBLE);
                    moxifloxacilinDose.setVisibility(View.GONE);


                } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_moxifloxacilin))) {

                    if(weightInt == 0){
                        levofloxacinDose.getEditText().setText("");
                        moxifloxacilinDose.getEditText().setText("");
                    } else {

                        Double ww = 1.0;
                        if (age < 2) {
                            ww = weightInt * 15.0;
                            int i = (int) Math.round(ww);
                            levofloxacinDose.getEditText().setText(String.valueOf(i));
                        } else if (age < 15) {
                            levofloxacinDose.getEditText().setText(String.valueOf(""));
                            levofloxacinDose.getEditText().setHint("7.5 - 10 mg/kg");
                        } else {
                            levofloxacinDose.getEditText().setText(String.valueOf(""));
                            levofloxacinDose.getEditText().setHint("750 - 1000 mg");
                        }

                        if(weightInt <= 13){
                            moxifloxacilinDose.getEditText().setText(String.valueOf(""));
                            moxifloxacilinDose.getEditText().setHint("Not recommended");
                        } else if (weightInt <= 30){
                            moxifloxacilinDose.getEditText().setText(String.valueOf("200"));
                        } else {
                            moxifloxacilinDose.getEditText().setText(String.valueOf("400"));
                        }

                    }

                    isoniazidDose.setVisibility(View.GONE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.VISIBLE);
                    ethionamideDose.setVisibility(View.GONE);
                    rifapentineAvailable.setVisibility(View.GONE);
                    ethambutolDose.setVisibility(View.GONE);
                    moxifloxacilinDose.setVisibility(View.VISIBLE);

                } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_ethionamide_ethambutol))) {

                    if (age < 15) {
                        ethionamideDose.getEditText().setText("");
                        ethionamideDose.getEditText().setHint("15 - 20 mg/kg");
                    } else {
                        ethionamideDose.getEditText().setText("");
                        ethionamideDose.getEditText().setHint("500 - 1000 mg");
                    }

                    if(weightInt == 0){
                        ethambutolDose.getEditText().setText("");
                    } else {

                        if(weightInt <= 2){
                            ethambutolDose.getEditText().setText(String.valueOf(""));
                            ethambutolDose.getEditText().setHint("Not recommended");
                        } else if (weightInt <= 7){
                            ethambutolDose.getEditText().setText(String.valueOf("100"));
                        } else if (weightInt <= 12){
                            ethambutolDose.getEditText().setText(String.valueOf("200"));
                        } else if (weightInt <= 15){
                            ethambutolDose.getEditText().setText(String.valueOf("300"));
                        } else if (weightInt <= 26){
                            ethambutolDose.getEditText().setText(String.valueOf("400"));
                        } else if (weightInt <= 30){
                            ethambutolDose.getEditText().setText(String.valueOf("500"));
                        } else if (weightInt <= 59){
                            ethambutolDose.getEditText().setText(String.valueOf("1500"));
                        } else {
                            ethambutolDose.getEditText().setText(String.valueOf("2000"));
                        }

                    }

                    isoniazidDose.setVisibility(View.GONE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.GONE);
                    ethionamideDose.setVisibility(View.VISIBLE);
                    rifapentineAvailable.setVisibility(View.GONE);
                    ethambutolDose.setVisibility(View.VISIBLE);
                    moxifloxacilinDose.setVisibility(View.GONE);

                } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_ethionamide_moxifloxacilin))) {

                    if (age < 15) {
                        ethionamideDose.getEditText().setText("");
                        ethionamideDose.getEditText().setHint("15 - 20 mg/kg");
                    } else {
                        ethionamideDose.getEditText().setText("");
                        ethionamideDose.getEditText().setHint("500 - 1000 mg");
                    }

                    if(weightInt == 0){
                        moxifloxacilinDose.getEditText().setText("");
                    } else {

                        if(weightInt <= 13){
                            moxifloxacilinDose.getEditText().setText(String.valueOf(""));
                            moxifloxacilinDose.getEditText().setHint("Not recommended");
                        } else if (weightInt <= 30){
                            moxifloxacilinDose.getEditText().setText(String.valueOf("200"));
                        } else {
                            moxifloxacilinDose.getEditText().setText(String.valueOf("400"));
                        }

                    }

                    isoniazidDose.setVisibility(View.GONE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.GONE);
                    ethionamideDose.setVisibility(View.VISIBLE);
                    rifapentineAvailable.setVisibility(View.GONE);
                    ethambutolDose.setVisibility(View.GONE);
                    moxifloxacilinDose.setVisibility(View.VISIBLE);

                } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_moxifloxacilin_ethambutol))) {

                    if(weightInt == 0){
                        moxifloxacilinDose.getEditText().setText("");
                        ethambutolDose.getEditText().setText("");
                    } else {

                        if(weightInt <= 2){
                            ethambutolDose.getEditText().setText(String.valueOf(""));
                            ethambutolDose.getEditText().setHint("Not recommended");
                        } else if (weightInt <= 7){
                            ethambutolDose.getEditText().setText(String.valueOf("100"));
                        } else if (weightInt <= 12){
                            ethambutolDose.getEditText().setText(String.valueOf("200"));
                        } else if (weightInt <= 15){
                            ethambutolDose.getEditText().setText(String.valueOf("300"));
                        } else if (weightInt <= 26){
                            ethambutolDose.getEditText().setText(String.valueOf("400"));
                        } else if (weightInt <= 30){
                            ethambutolDose.getEditText().setText(String.valueOf("500"));
                        } else if (weightInt <= 59){
                            ethambutolDose.getEditText().setText(String.valueOf("1500"));
                        } else {
                            ethambutolDose.getEditText().setText(String.valueOf("2000"));
                        }

                        if(weightInt <= 13){
                            moxifloxacilinDose.getEditText().setText(String.valueOf(""));
                            moxifloxacilinDose.getEditText().setHint("Not recommended");
                        } else if (weightInt <= 30){
                            moxifloxacilinDose.getEditText().setText(String.valueOf("200"));
                        } else {
                            moxifloxacilinDose.getEditText().setText(String.valueOf("400"));
                        }

                    }

                    isoniazidDose.setVisibility(View.GONE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.GONE);
                    ethionamideDose.setVisibility(View.GONE);
                    rifapentineAvailable.setVisibility(View.GONE);
                    ethambutolDose.setVisibility(View.VISIBLE);
                    moxifloxacilinDose.setVisibility(View.VISIBLE);

                }

            }
        });


        /*rifapentineDose.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!App.get(rifapentineDose).equals("")) {
                    int dose = Integer.parseInt(App.get(rifapentineDose));
                    String hint = rifapentineDose.getEditText().getHint().toString();

                    if (hint.equals("300 - 450 mg")) {
                        if (dose < 300 || dose > 450)
                            rifapentineDose.getEditText().setError(getResources().getString(R.string.pet_rifapentine_out_of_range_1));
                        else
                            rifapentineDose.getEditText().setError(null);
                    } else if (hint.equals("450 - 900 mg")) {
                        if (dose < 450 || dose > 900)
                            rifapentineDose.getEditText().setError(getResources().getString(R.string.pet_rifapentine_out_of_range_2));
                        else
                            rifapentineDose.getEditText().setError(null);
                    }
                }
            }
        });*/

        isoniazidDose.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!App.get(isoniazidDose).equals("")) {
                    int dose = Integer.parseInt(App.get(isoniazidDose));

                    if(App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy))){
                        if (dose > 300) {
                            isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded_300));
                            isoniazidDose.getEditText().requestFocus();
                        }
                        else
                            isoniazidDose.getEditText().setError(null);
                    }
                    else{

                        if (dose > 2000) {
                            isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded_2000));
                            isoniazidDose.getEditText().requestFocus();
                        }
                        else
                            isoniazidDose.getEditText().setError(null);

                    }

                }
            }
        });

        phone1a.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==4){
                    phone1b.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phone1b.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==0){
                    phone1a.requestFocus();
                    phone1a.setSelection(phone1a.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        resetViews();
    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        resistanceType.setVisibility(View.GONE);
        dstPattern.setVisibility(View.GONE);
        isoniazidDose.setVisibility(View.GONE);
        rifapentineDose.setVisibility(View.GONE);
        levofloxacinDose.setVisibility(View.GONE);
        ethionamideDose.setVisibility(View.GONE);
        ethambutolDose.setVisibility(View.GONE);
        moxifloxacilinDose.setVisibility(View.GONE);
        ancillaryDrugs.setVisibility(View.GONE);
        ancillaryDrugDuration.setVisibility(View.GONE);
        rifapentineAvailable.setVisibility(View.GONE);
        other.setVisibility(View.GONE);
        relationshipTreatmentSuppoter.setVisibility(View.VISIBLE);

        indexPatientId.setEnabled(false);
        tbType.setEnabled(false);
        infectionType.setEnabled(false);
        resistanceType.setEnabled(false);
        dstPattern.setEnabled(false);

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

        if(!autoFill) {
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
                    String weight = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_CLINICIAN_CONTACT_SCREENING, "WEIGHT (KG)");
                    String indexId = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_BASELINE_SCREENING, "PATIENT ID OF INDEX CASE");
                    String intervention = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_BASELINE_SCREENING, "INTERVENTION");

                    String tbType = "";
                    String infectionType = "";
                    String resistanceType = "";
                    String dstbType = "";

                    serverService.getPatient(indexId, false);

                    if (!(indexId == null || indexId.equals(""))) {
                        String id = serverService.getPatientSystemIdByIdentifierLocalDB(indexId);
                        tbType = serverService.getLatestObsValue(id, App.getProgram() + "-" + Forms.PET_INDEX_PATIENT_REGISTRATION, "SITE OF TUBERCULOSIS DISEASE");
                        infectionType = serverService.getLatestObsValue(id, App.getProgram() + "-" + Forms.PET_INDEX_PATIENT_REGISTRATION, "TUBERCULOSIS INFECTION TYPE");
                        resistanceType = serverService.getLatestObsValue(id, App.getProgram() + "-" + Forms.PET_INDEX_PATIENT_REGISTRATION, "TUBERCULOSIS DRUG RESISTANCE TYPE");
                        dstbType = serverService.getLatestObsValue(id, App.getProgram() + "-" + Forms.PET_INDEX_PATIENT_REGISTRATION, "RESISTANT TO ANTI-TUBERCULOSIS DRUGS");
                    }

                    if (weight != null)
                        weight = weight.replace(".0", "");

                    if (weight != null)
                        if (!weight.equals(""))
                            result.put("WEIGHT (KG)", weight);
                    if (indexId != null)
                        if (!indexId.equals(""))
                            result.put("PATIENT ID OF INDEX CASE", indexId);
                    if (tbType != null)
                        if (!tbType.equals(""))
                            result.put("SITE OF TUBERCULOSIS DISEASE", tbType.equals("PULMONARY TUBERCULOSIS") ? getResources().getString(R.string.pet_ptb) : getResources().getString(R.string.pet_eptb));
                    if (infectionType != null)
                        if (!infectionType.equals(""))
                            result.put("TUBERCULOSIS INFECTION TYPE", infectionType.equals("DRUG-SENSITIVE TUBERCULOSIS INFECTION") ? getResources().getString(R.string.pet_dstb) : getResources().getString(R.string.pet_drtb));
                    if (resistanceType != null)
                        if (!resistanceType.equals(""))
                            result.put("TUBERCULOSIS DRUG RESISTANCE TYPE", resistanceType.equals("RIFAMPICIN RESISTANT TUBERCULOSIS INFECTION") ? getResources().getString(R.string.pet_rr_tb) :
                                    (resistanceType.equals("MONO DRUG RESISTANT TUBERCULOSIS") ? getResources().getString(R.string.pet_dr_tb) :
                                            (resistanceType.equals("PANDRUG RESISTANT TUBERCULOSIS") ? getResources().getString(R.string.pet_pdr_tb) :
                                                    (resistanceType.equals("MULTI-DRUG RESISTANT TUBERCULOSIS INFECTION") ? getResources().getString(R.string.pet_mdr_tb) : getResources().getString(R.string.pet_xdr_tb)))));
                    if (dstbType != null)
                        if (!dstbType.equals(""))
                            result.put("RESISTANT TO ANTI-TUBERCULOSIS DRUGS", dstbType);

                    if(intervention == null)
                        intervention = "";
                    result.put("INTERVENTION", intervention);

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

                    for (RadioButton rb : intervention.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.pet)) && result.get("INTERVENTION").equals("PET")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.sci)) && result.get("INTERVENTION").equals("SCI")) {
                            rb.setChecked(true);
                            break;
                        }
                    }

                    if (result.get("PATIENT ID OF INDEX CASE") == null || result.get("PATIENT ID OF INDEX CASE").equals("")) {

                        /*final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        alertDialog.setMessage(getResources().getString(R.string.baseline_screening_missing));
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
                        alertDialog.show();*/

                        return;
                    } else if (result.get("TUBERCULOSIS INFECTION TYPE") == null || result.get("TUBERCULOSIS INFECTION TYPE").equals("")) {

                        /*final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        String indexPatientMissing = getResources().getString(R.string.index_patient_info_missing).replace("()", "(" + result.get("PATIENT ID OF INDEX CASE") + ")");
                        alertDialog.setMessage(indexPatientMissing);
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
                        alertDialog.show();*/
                        return;
                    }

                    weight.getEditText().setText(result.get("WEIGHT (KG)"));
                    indexPatientId.getEditText().setText(result.get("PATIENT ID OF INDEX CASE"));
                    for (RadioButton rb : tbType.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.pet_ptb)) && result.get("SITE OF TUBERCULOSIS DISEASE").equals(getResources().getString(R.string.pet_ptb))) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.pet_eptb)) && result.get("SITE OF TUBERCULOSIS DISEASE").equals(getResources().getString(R.string.pet_eptb))) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    for (RadioButton rb : infectionType.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.pet_dstb)) && result.get("TUBERCULOSIS INFECTION TYPE").equals(getResources().getString(R.string.pet_dstb))) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.pet_drtb)) && result.get("TUBERCULOSIS INFECTION TYPE").equals(getResources().getString(R.string.pet_drtb))) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    if (result.get("TUBERCULOSIS DRUG RESISTANCE TYPE") != null) {
                        for (RadioButton rb : resistanceType.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.pet_rr_tb)) && result.get("TUBERCULOSIS DRUG RESISTANCE TYPE").equals(getResources().getString(R.string.pet_rr_tb))) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_dr_tb)) && result.get("TUBERCULOSIS DRUG RESISTANCE TYPE").equals(getResources().getString(R.string.pet_dr_tb))) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_pdr_tb)) && result.get("TUBERCULOSIS DRUG RESISTANCE TYPE").equals(getResources().getString(R.string.pet_pdr_tb))) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_mdr_tb)) && result.get("TUBERCULOSIS DRUG RESISTANCE TYPE").equals(getResources().getString(R.string.pet_mdr_tb))) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_xdr_tb)) && result.get("TUBERCULOSIS DRUG RESISTANCE TYPE").equals(getResources().getString(R.string.pet_xdr_tb))) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                        if (!result.get("TUBERCULOSIS DRUG RESISTANCE TYPE").equals(""))
                            resistanceType.setVisibility(View.VISIBLE);
                    }
                    if (result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS") != null) {
                        for (CheckBox cb : dstPattern.getCheckedBoxes()) {
                            if (cb.getText().equals(getResources().getString(R.string.pet_isoniazid)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("ISONIAZID")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_rifampicin)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("RIFAMPICIN")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_amikacin)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("AMIKACIN")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_capreomycin)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("CAPREOMYCIN")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_streptpmycin)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("STREPTOMYCIN")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_ofloxacin)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("OFLOXACIN")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_moxifloxacin)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("MOXIFLOXACIN")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_ethambutol)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("ETHAMBUTOL")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_ethionamide)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("ETHIONAMIDE")) {
                                cb.setChecked(true);
                            }
                            if (cb.getText().equals(getResources().getString(R.string.pet_pyrazinamide)) && result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").contains("PYRAZINAMIDE")) {
                                cb.setChecked(true);
                            }

                        }
                        if (!result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS").equals(""))
                            dstPattern.setVisibility(View.VISIBLE);
                    }

                    if (App.get(infectionType).equals(getResources().getString(R.string.pet_drtb))) {

                        for (RadioButton rb : petRegimen.getRadioGroup().getButtons()) {

                            if (rb.getText().equals(getResources().getString(R.string.pet_levofloxacin_ethionamide)))
                                rb.setChecked(true);
                            else
                                rb.setChecked(false);

                        }

                    } else {

                        if (App.getPatient().getPerson().getAge() < 2) {
                            for (RadioButton rb : petRegimen.getRadioGroup().getButtons()) {

                                if (rb.getText().equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy)))
                                    rb.setChecked(true);
                                else
                                    rb.setChecked(false);

                            }
                        } else {
                            for (RadioButton rb : petRegimen.getRadioGroup().getButtons()) {

                                if (rb.getText().equals(getResources().getString(R.string.pet_isoniazid_rifapentine)))
                                    rb.setChecked(true);
                                else
                                    rb.setChecked(false);


                            }
                        }

                    }

                }
            };
            autopopulateFormTask.execute("");
        }
    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();

        treatmentInitiationDate.getButton().setEnabled(true);
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
        treatmentInitiationDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());

    }

    @Override
    public boolean validate() {

        /*if (App.get(indexPatientId).equals("")) {

            final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
            alertDialog.setMessage(getResources().getString(R.string.baseline_screening_missing));
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
            gotoFirstPage();
            return false;
        } else if (App.get(tbType) == null || App.get(tbType).equals("")) {

            final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
            String indexPatientMissing = getResources().getString(R.string.index_patient_info_missing).replace("()", "(" + App.get(indexPatientId) + ")");
            alertDialog.setMessage(indexPatientMissing);
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
            gotoFirstPage();
            return false;
        }*/

        Boolean error = false;
        View view = null;

        if (other.getVisibility() == View.VISIBLE) {
            if (App.get(other).isEmpty()) {
                other.getEditText().setError(getString(R.string.empty_field));
                other.getEditText().requestFocus();
                view = null;
                error = true;
                gotoLastPage();
            } else {
                other.getEditText().setError(null);
                other.getEditText().clearFocus();
            }
        }
        /*if (App.get(phone1a).isEmpty()) {
            phone1a.setError(getResources().getString(R.string.mandatory_field));
            phone1a.requestFocus();
            error = true;
            gotoLastPage();
        } else if (App.get(phone1b).isEmpty()) {
            phone1b.setError(getResources().getString(R.string.mandatory_field));
            phone1b.requestFocus();
            error = true;
            gotoLastPage();
        } else */
        if(!App.get(phone1a).equals("") || !App.get(phone1b).equals(""))
        if (!RegexUtil.isContactNumber(App.get(phone1a) + App.get(phone1b))) {
            phone1b.setError(getResources().getString(R.string.invalid_value));
            phone1b.requestFocus();
            error = true;
            gotoLastPage();
        }else {
            phone1b.setError(null);
            phone1b.clearFocus();
        }
       /* if (App.get(nameTreatmentSupporter).isEmpty()) {
            nameTreatmentSupporter.getQuestionView().setError(getString(R.string.empty_field));
            nameTreatmentSupporter.getQuestionView().requestFocus();
            view = null;
            error = true;
            gotoLastPage();
        }*/
        if (ancillaryDrugDuration.getVisibility() == View.VISIBLE) {
            if (App.get(ancillaryDrugDuration).isEmpty()) {
                ancillaryDrugDuration.getEditText().setError(getString(R.string.empty_field));
                ancillaryDrugDuration.getEditText().requestFocus();
                view = null;
                error = true;
                gotoLastPage();
            }
            else{
                ancillaryDrugDuration.getEditText().setError(null);
                ancillaryDrugDuration.getEditText().clearFocus();
            }
        }
        Boolean flag = false;
        if (ancillaryDrugs.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : ancillaryDrugs.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                ancillaryDrugs.getQuestionView().setError(getString(R.string.empty_field));
                ancillaryDrugs.getQuestionView().requestFocus();
                view = ancillaryDrugs;
                error = true;
                gotoLastPage();
            }else{
                ancillaryDrugs.getQuestionView().setError(null);
                ancillaryDrugs.getQuestionView().clearFocus();
            }
        }

        if (moxifloxacilinDose.getVisibility() == View.VISIBLE) {
            if (App.get(moxifloxacilinDose).isEmpty()) {
                moxifloxacilinDose.getEditText().setError(getString(R.string.empty_field));
                moxifloxacilinDose.getEditText().requestFocus();
                view = null;
                error = true;
                gotoLastPage();
            }else{
                moxifloxacilinDose.getEditText().setError(null);
                moxifloxacilinDose.getEditText().clearFocus();
            }
        }

        if (ethambutolDose.getVisibility() == View.VISIBLE) {
            if (App.get(ethambutolDose).isEmpty()) {
                ethambutolDose.getEditText().setError(getString(R.string.empty_field));
                ethambutolDose.getEditText().requestFocus();
                view = null;
                error = true;
                gotoLastPage();
            }else{
                ethambutolDose.getEditText().setError(null);
                ethambutolDose.getEditText().clearFocus();
            }
        }

        if (ethionamideDose.getVisibility() == View.VISIBLE) {
            if (App.get(ethionamideDose).isEmpty()) {
                ethionamideDose.getEditText().setError(getString(R.string.empty_field));
                ethionamideDose.getEditText().requestFocus();
                view = null;
                error = true;
                gotoLastPage();
            }else{
                ethionamideDose.getEditText().setError(null);
                ethionamideDose.getEditText().clearFocus();
            }
        }
        if (levofloxacinDose.getVisibility() == View.VISIBLE) {
            if (App.get(levofloxacinDose).isEmpty()) {
                levofloxacinDose.getEditText().setError(getString(R.string.empty_field));
                levofloxacinDose.getEditText().requestFocus();
                view = null;
                error = true;
                gotoLastPage();
            }else{
                levofloxacinDose.getEditText().setError(null);
                levofloxacinDose.getEditText().clearFocus();
            }
        }
        if (rifapentineDose.getVisibility() == View.VISIBLE) {
            if (App.get(rifapentineDose).isEmpty()) {
                rifapentineDose.getEditText().setError(getString(R.string.empty_field));
                rifapentineDose.getEditText().requestFocus();
                view = null;
                error = true;
                gotoLastPage();
            } else{
                rifapentineDose.getEditText().setError(null);
                rifapentineDose.getEditText().clearFocus();
            }/*else {
                int dose = Integer.parseInt(App.get(rifapentineDose));
                String hint = rifapentineDose.getEditText().getHint().toString();

                if (hint.equals("300 - 450 mg")) {
                    if (dose < 300 || dose > 450) {
                        rifapentineDose.getEditText().setError(getResources().getString(R.string.pet_rifapentine_out_of_range_1));
                        rifapentineDose.getEditText().requestFocus();
                        view = null;
                        error = true;
                        gotoLastPage();
                    }
                } else if (hint.equals("450 - 900 mg")) {
                    if (dose < 450 || dose > 900) {
                        rifapentineDose.getEditText().setError(getResources().getString(R.string.pet_rifapentine_out_of_range_2));
                        rifapentineDose.getEditText().requestFocus();
                        view = null;
                        error = true;
                        gotoLastPage();
                    }
                }
            }*/
        }
        if (isoniazidDose.getVisibility() == View.VISIBLE) {
            if (App.get(isoniazidDose).isEmpty()) {
                isoniazidDose.getEditText().setError(getString(R.string.empty_field));
                isoniazidDose.getEditText().requestFocus();
                view = null;
                error = true;
                gotoLastPage();
            } else {
                int dose = Integer.parseInt(App.get(isoniazidDose));
                if(App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy))) {
                    if (dose > 300) {
                        isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded_300));
                        isoniazidDose.getEditText().requestFocus();
                        view = null;
                        error = true;
                    }else{
                        isoniazidDose.getEditText().setError(null);
                        isoniazidDose.getEditText().clearFocus();
                    }
                }
                else{
                    if (dose > 2000) {
                        isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded_2000));
                        isoniazidDose.getEditText().requestFocus();
                        view = null;
                        error = true;
                    }else{
                        isoniazidDose.getEditText().setError(null);
                        isoniazidDose.getEditText().clearFocus();
                    }
                }
            }
        }

        if (intervention.getVisibility() == View.VISIBLE && App.get(intervention).isEmpty()) {
            intervention.getQuestionView().setError(getString(R.string.empty_field));
            intervention.getQuestionView().requestFocus();
            gotoFirstPage();
            view = intervention;
            error = true;
        }else{
            intervention.getQuestionView().setError(null);
            intervention.getQuestionView().clearFocus();
        }

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
                                        other.getEditText().clearFocus();
                                        phone1b.clearFocus();
                                        ancillaryDrugDuration.getEditText().clearFocus();
                                        ethionamideDose.getEditText().clearFocus();
                                        levofloxacinDose.getEditText().clearFocus();
                                        rifapentineDose.getEditText().clearFocus();
                                        isoniazidDose.getEditText().clearFocus();
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
        final HashMap<String, String> personAttribute = new HashMap<String, String>();
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
        observations.add(new String[]{"INTERVENTION", App.get(intervention).equals(getResources().getString(R.string.pet)) ? "PET" : "SCI"});
        observations.add(new String[]{"WEIGHT (KG)", App.get(weight)});
        observations.add(new String[]{"PATIENT ID OF INDEX CASE", App.get(indexPatientId)});
        observations.add(new String[]{"SITE OF TUBERCULOSIS DISEASE", App.get(tbType).equals(getResources().getString(R.string.pet_ptb)) ? "PULMONARY TUBERCULOSIS" : "EXTRA-PULMONARY TUBERCULOSIS"});
        observations.add(new String[]{"TUBERCULOSIS INFECTION TYPE", App.get(infectionType).equals(getResources().getString(R.string.pet_dstb)) ? "DRUG-SENSITIVE TUBERCULOSIS INFECTION" : "DRUG-RESISTANT TB"});
        if (resistanceType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOSIS DRUG RESISTANCE TYPE", App.get(resistanceType).equals(getResources().getString(R.string.pet_rr_tb)) ? "RIFAMPICIN RESISTANT TUBERCULOSIS INFECTION" :
                    (App.get(resistanceType).equals(getResources().getString(R.string.pet_dr_tb)) ? "MONO DRUG RESISTANT TUBERCULOSIS" :
                            (App.get(resistanceType).equals(getResources().getString(R.string.pet_pdr_tb)) ? "PANDRUG RESISTANT TUBERCULOSIS" :
                                    (App.get(resistanceType).equals(getResources().getString(R.string.pet_mdr_tb))) ? "MULTI-DRUG RESISTANT TUBERCULOSIS INFECTION" : "EXTREMELY DRUG-RESISTANT TUBERCULOSIS INFECTION"))});
        if (dstPattern.getVisibility() == View.VISIBLE) {
            String dstPatternString = "";
            for (CheckBox cb : dstPattern.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_isoniazid)))
                    dstPatternString = dstPatternString + "ISONIAZID" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_rifampicin)))
                    dstPatternString = dstPatternString + "RIFAMPICIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_amikacin)))
                    dstPatternString = dstPatternString + "AMIKACIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_capreomycin)))
                    dstPatternString = dstPatternString + "CAPREOMYCIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_streptpmycin)))
                    dstPatternString = dstPatternString + "STREPTOMYCIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_ofloxacin)))
                    dstPatternString = dstPatternString + "OFLOXACIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_moxifloxacin)))
                    dstPatternString = dstPatternString + "MOXIFLOXACIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_ethambutol)))
                    dstPatternString = dstPatternString + "ETHAMBUTOL" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_ethionamide)))
                    dstPatternString = dstPatternString + "ETHIONAMIDE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_pyrazinamide)))
                    dstPatternString = dstPatternString + "PYRAZINAMIDE" + " ; ";
            }
            observations.add(new String[]{"RESISTANT TO ANTI-TUBERCULOSIS DRUGS", dstPatternString});
        }
        observations.add(new String[]{"TREATMENT START DATE", App.getSqlDate(secondDateCalendar)});
        observations.add(new String[]{"POST-EXPOSURE TREATMENT REGIMEN", App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy)) ? "ISONIAZID PROPHYLAXIS" :
                (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine)) ? "ISONIAZID AND RIFAPENTINE" :
                        (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethionamide)) ? "LEVOFLOXACIN AND ETHIONAMIDE" :
                                (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethambutol)) ? "LEVOFLOXACIN AND ETHAMBUTOL" :
                                        (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_moxifloxacilin)) ? "LEVOFLOXACIN AND MOXIFLOXACILIN" :
                                                (App.get(petRegimen).equals(getResources().getString(R.string.pet_ethionamide_ethambutol)) ? "ETHIONAMIDE AND ETHAMBUTOL" :
                                                        (App.get(petRegimen).equals(getResources().getString(R.string.pet_ethionamide_moxifloxacilin)) ? "ETHIONAMIDE AND MOXIFLOXACILIN" : "MOXIFLOXACILIN AND ETHAMBUTOL"))))))});
        if (isoniazidDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ISONIAZID DOSE", App.get(isoniazidDose)});
        if (rifapentineDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"RIFAPENTINE DOSE", App.get(rifapentineDose)});
        if (levofloxacinDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"LEVOFLOXACIN DOSE", App.get(levofloxacinDose)});
        if (ethionamideDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ETHIONAMIDE DOSE", App.get(ethionamideDose)});
        if (ethambutolDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ETHAMBUTOL DOSE", App.get(ethambutolDose)});
        if (moxifloxacilinDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"MOXIFLOXACILIN DOSE", App.get(moxifloxacilinDose)});
        if (ancillaryNeed.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ANCILLARY MEDICATION NEEDED", App.get(ancillaryNeed).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (ancillaryDrugs.getVisibility() == View.VISIBLE) {
            String ancillaryDrugs = "";
            for (CheckBox cb : this.ancillaryDrugs.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_iron_deficiency_prtocol)))
                    ancillaryDrugs = ancillaryDrugs + "IRON" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_vitamin_d_protocol)))
                    ancillaryDrugs = ancillaryDrugs + "VITAMIN D" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_pcm_protocol)))
                    ancillaryDrugs = ancillaryDrugs + "PHENYLEPHRINE, CHLORPHENIRAMINE, AND METHSCOPOLAMINE" + " ; ";
            }

            observations.add(new String[]{"ANCILLARY DRUGS", ancillaryDrugs});
        }
        if (ancillaryDrugDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"MEDICATION DURATION", App.get(ancillaryDrugDuration)});
        observations.add(new String[]{"NAME OF TREATMENT SUPPORTER", App.get(nameTreatmentSupporter)});
        if (!App.get(phone1a).equals(""))
            observations.add(new String[]{"TREATMENT SUPPORTER CONTACT NUMBER", App.get(phone1a) + "-" + App.get(phone1b)});
        if (!App.get(typeTreatmentSupporter).equals(""))
         observations.add(new String[]{"TREATMENT SUPPORTER TYPE", App.get(typeTreatmentSupporter).equals(getResources().getString(R.string.pet_family_treatment_supporter)) ? "FAMILY MEMBER" : "NON-FAMILY MEMBER"});
        if (relationshipTreatmentSuppoter.getVisibility() == View.VISIBLE && !App.get(relationshipTreatmentSuppoter).equals(""))
            observations.add(new String[]{"TREATMENT SUPPORTER RELATIONSHIP TO PATIENT", (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_mother))) ? "MOTHER" :
                    (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_father)) ? "FATHER" :
                            (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                    (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                            (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                    (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                            (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_brother)) ? "BROTHER" :
                                                                    (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_sister)) ? "SISTER" :
                                                                            (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_son)) ? "SON" :
                                                                                    (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_daughter)) ? "DAUGHTER" :
                                                                                            (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_spouse)) ? "SPOUSE" :
                                                                                            (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_aunt)) ? "AUNT" :
                                                                                                    (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_uncle)) ? "UNCLE" : "OTHER FAMILY MEMBER"))))))))))))});
        if (other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FAMILY MEMBER", App.get(other)});
        observations.add(new String[]{"CLINICIAN NOTES (TEXT)", App.get(clincianNote)});

        personAttribute.put("Health Center",serverService.getLocationUuid(App.getLocation()));

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

                String result = serverService.saveEncounterAndObservation(App.getProgram()+"-"+FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
                if (!result.contains("SUCCESS"))
                    return result;
                else {

                    String encounterId = "";

                    if (result.contains("_")) {
                        String[] successArray = result.split("_");
                        encounterId = successArray[1];
                    }

                    result = serverService.saveMultiplePersonAttribute(personAttribute, encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                }

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

        serverService.saveFormLocally(FORM_NAME, FORM, "12345-5", formValues);

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
        } else if (view == treatmentInitiationDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            treatmentInitiationDate.getButton().setEnabled(false);
        }

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == relationshipTreatmentSuppoter.getSpinner()) {
            if (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_other)))
                other.setVisibility(View.VISIBLE);
            else
                other.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == petRegimen.getRadioGroup()) {

            isoniazidDose.getEditText().setHint("");
            rifapentineDose.getEditText().setHint("");
            levofloxacinDose.getEditText().setHint("");
            ethionamideDose.getEditText().setHint("");

            isoniazidDose.getEditText().setText("");
            rifapentineDose.getEditText().setText("");
            levofloxacinDose.getEditText().setText("");
            ethionamideDose.getEditText().setText("");

            int age = App.getPatient().getPerson().getAge();
            int weightInt = 0;
            if (!App.get(weight).equals("")) {
                weightInt = Integer.parseInt(App.get(weight));
            }

            if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy))) {

                int w = 1;
                w = weightInt * 10;

                isoniazidDose.getEditText().setText(String.valueOf(w));

                isoniazidDose.setVisibility(View.VISIBLE);
                rifapentineDose.setVisibility(View.GONE);
                levofloxacinDose.setVisibility(View.GONE);
                ethionamideDose.setVisibility(View.GONE);
                rifapentineAvailable.setVisibility(View.GONE);

            } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine))) {

                int w = 1;

                rifapentineAvailable.setVisibility(View.VISIBLE);
                if (App.get(rifapentineAvailable).equals(getResources().getString(R.string.no))) {

                    isoniazidDose.setVisibility(View.VISIBLE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.GONE);
                    ethionamideDose.setVisibility(View.GONE);

                    if (age < 12) w = weightInt * 10;
                    else if (age < 15) w = weightInt * 10;
                    else w = weightInt * 5;

                    isoniazidDose.getEditText().setText(String.valueOf(w));

                } else {

                    w = 1;
                    w = weightInt * 15;
                    isoniazidDose.getEditText().setText(String.valueOf(w));

                    /*rifapentineDose.getEditText().setText("");
                    if (age < 12)
                        rifapentineDose.getEditText().setHint("300 - 450 mg");
                    else
                        rifapentineDose.getEditText().setHint("450 - 900 mg");*/

                    isoniazidDose.setVisibility(View.VISIBLE);
                    rifapentineDose.setVisibility(View.VISIBLE);
                    levofloxacinDose.setVisibility(View.GONE);
                    ethionamideDose.setVisibility(View.GONE);

                }

            } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethionamide))) {

                int w = 1;

                if (age < 15) {
                    ethionamideDose.getEditText().setText("");
                    ethionamideDose.getEditText().setHint("15 - 20 mg/kg");
                } else {
                    ethionamideDose.getEditText().setText("");
                    ethionamideDose.getEditText().setHint("500 - 1000 mg");
                }

                Double ww = 1.0;
                if (age < 2) {
                    ww = weightInt * 15.0;
                    int i = (int) Math.round(ww);
                    levofloxacinDose.getEditText().setText(String.valueOf(i));
                } else if (age < 15) {
                    levofloxacinDose.getEditText().setText(String.valueOf(""));
                    levofloxacinDose.getEditText().setHint("7.5 - 10 mg/kg");
                } else {
                    levofloxacinDose.getEditText().setText(String.valueOf(""));
                    levofloxacinDose.getEditText().setHint("750 - 1000 mg");
                }

                isoniazidDose.setVisibility(View.GONE);
                rifapentineDose.setVisibility(View.GONE);
                levofloxacinDose.setVisibility(View.VISIBLE);
                ethionamideDose.setVisibility(View.VISIBLE);
                rifapentineAvailable.setVisibility(View.GONE);

            } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethambutol))) {

                if(weightInt == 0){
                    levofloxacinDose.getEditText().setText("");
                    ethambutolDose.getEditText().setText("");
                } else {

                    Double ww = 1.0;
                    if (age < 2) {
                        ww = weightInt * 15.0;
                        int i = (int) Math.round(ww);
                        levofloxacinDose.getEditText().setText(String.valueOf(i));
                    } else if (age < 15) {
                        levofloxacinDose.getEditText().setText(String.valueOf(""));
                        levofloxacinDose.getEditText().setHint("7.5 - 10 mg/kg");
                    } else {
                        levofloxacinDose.getEditText().setText(String.valueOf(""));
                        levofloxacinDose.getEditText().setHint("750 - 1000 mg");
                    }

                    if(weightInt <= 2){
                        ethambutolDose.getEditText().setText(String.valueOf(""));
                        ethambutolDose.getEditText().setHint("Not recommended");
                    } else if (weightInt <= 7){
                        ethambutolDose.getEditText().setText(String.valueOf("100"));
                    } else if (weightInt <= 12){
                        ethambutolDose.getEditText().setText(String.valueOf("200"));
                    } else if (weightInt <= 15){
                        ethambutolDose.getEditText().setText(String.valueOf("300"));
                    } else if (weightInt <= 26){
                        ethambutolDose.getEditText().setText(String.valueOf("400"));
                    } else if (weightInt <= 30){
                        ethambutolDose.getEditText().setText(String.valueOf("500"));
                    } else if (weightInt <= 59){
                        ethambutolDose.getEditText().setText(String.valueOf("1500"));
                    } else {
                        ethambutolDose.getEditText().setText(String.valueOf("2000"));
                    }

                }

                isoniazidDose.setVisibility(View.GONE);
                rifapentineDose.setVisibility(View.GONE);
                levofloxacinDose.setVisibility(View.VISIBLE);
                ethionamideDose.setVisibility(View.GONE);
                rifapentineAvailable.setVisibility(View.GONE);
                ethambutolDose.setVisibility(View.VISIBLE);
                moxifloxacilinDose.setVisibility(View.GONE);


            } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_moxifloxacilin))) {

                if(weightInt == 0){
                    levofloxacinDose.getEditText().setText("");
                    moxifloxacilinDose.getEditText().setText("");
                } else {

                    Double ww = 1.0;
                    if (age < 2) {
                        ww = weightInt * 15.0;
                        int i = (int) Math.round(ww);
                        levofloxacinDose.getEditText().setText(String.valueOf(i));
                    } else if (age < 15) {
                        levofloxacinDose.getEditText().setText(String.valueOf(""));
                        levofloxacinDose.getEditText().setHint("7.5 - 10 mg/kg");
                    } else {
                        levofloxacinDose.getEditText().setText(String.valueOf(""));
                        levofloxacinDose.getEditText().setHint("750 - 1000 mg");
                    }

                    if(weightInt <= 13){
                        moxifloxacilinDose.getEditText().setText(String.valueOf(""));
                        moxifloxacilinDose.getEditText().setHint("Not recommended");
                    } else if (weightInt <= 30){
                        moxifloxacilinDose.getEditText().setText(String.valueOf("200"));
                    } else {
                        moxifloxacilinDose.getEditText().setText(String.valueOf("400"));
                    }

                }

                isoniazidDose.setVisibility(View.GONE);
                rifapentineDose.setVisibility(View.GONE);
                levofloxacinDose.setVisibility(View.VISIBLE);
                ethionamideDose.setVisibility(View.GONE);
                rifapentineAvailable.setVisibility(View.GONE);
                ethambutolDose.setVisibility(View.GONE);
                moxifloxacilinDose.setVisibility(View.VISIBLE);

            } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_ethionamide_ethambutol))) {

                if (age < 15) {
                    ethionamideDose.getEditText().setText("");
                    ethionamideDose.getEditText().setHint("15 - 20 mg/kg");
                } else {
                    ethionamideDose.getEditText().setText("");
                    ethionamideDose.getEditText().setHint("500 - 1000 mg");
                }

                if(weightInt == 0){
                    ethambutolDose.getEditText().setText("");
                } else {

                    if(weightInt <= 2){
                        ethambutolDose.getEditText().setText(String.valueOf(""));
                        ethambutolDose.getEditText().setHint("Not recommended");
                    } else if (weightInt <= 7){
                        ethambutolDose.getEditText().setText(String.valueOf("100"));
                    } else if (weightInt <= 12){
                        ethambutolDose.getEditText().setText(String.valueOf("200"));
                    } else if (weightInt <= 15){
                        ethambutolDose.getEditText().setText(String.valueOf("300"));
                    } else if (weightInt <= 26){
                        ethambutolDose.getEditText().setText(String.valueOf("400"));
                    } else if (weightInt <= 30){
                        ethambutolDose.getEditText().setText(String.valueOf("500"));
                    } else if (weightInt <= 59){
                        ethambutolDose.getEditText().setText(String.valueOf("1500"));
                    } else {
                        ethambutolDose.getEditText().setText(String.valueOf("2000"));
                    }

                }

                isoniazidDose.setVisibility(View.GONE);
                rifapentineDose.setVisibility(View.GONE);
                levofloxacinDose.setVisibility(View.GONE);
                ethionamideDose.setVisibility(View.VISIBLE);
                rifapentineAvailable.setVisibility(View.GONE);
                ethambutolDose.setVisibility(View.VISIBLE);
                moxifloxacilinDose.setVisibility(View.GONE);

            } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_ethionamide_moxifloxacilin))) {

                if (age < 15) {
                    ethionamideDose.getEditText().setText("");
                    ethionamideDose.getEditText().setHint("15 - 20 mg/kg");
                } else {
                    ethionamideDose.getEditText().setText("");
                    ethionamideDose.getEditText().setHint("500 - 1000 mg");
                }

                if(weightInt == 0){
                    moxifloxacilinDose.getEditText().setText("");
                } else {

                    if(weightInt <= 13){
                        moxifloxacilinDose.getEditText().setText(String.valueOf(""));
                        moxifloxacilinDose.getEditText().setHint("Not recommended");
                    } else if (weightInt <= 30){
                        moxifloxacilinDose.getEditText().setText(String.valueOf("200"));
                    } else {
                        moxifloxacilinDose.getEditText().setText(String.valueOf("400"));
                    }

                }

                isoniazidDose.setVisibility(View.GONE);
                rifapentineDose.setVisibility(View.GONE);
                levofloxacinDose.setVisibility(View.GONE);
                ethionamideDose.setVisibility(View.VISIBLE);
                rifapentineAvailable.setVisibility(View.GONE);
                ethambutolDose.setVisibility(View.GONE);
                moxifloxacilinDose.setVisibility(View.VISIBLE);

            } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_moxifloxacilin_ethambutol))) {

                if(weightInt == 0){
                    moxifloxacilinDose.getEditText().setText("");
                    ethambutolDose.getEditText().setText("");
                } else {

                    if(weightInt <= 2){
                        ethambutolDose.getEditText().setText(String.valueOf(""));
                        ethambutolDose.getEditText().setHint("Not recommended");
                    } else if (weightInt <= 7){
                        ethambutolDose.getEditText().setText(String.valueOf("100"));
                    } else if (weightInt <= 12){
                        ethambutolDose.getEditText().setText(String.valueOf("200"));
                    } else if (weightInt <= 15){
                        ethambutolDose.getEditText().setText(String.valueOf("300"));
                    } else if (weightInt <= 26){
                        ethambutolDose.getEditText().setText(String.valueOf("400"));
                    } else if (weightInt <= 30){
                        ethambutolDose.getEditText().setText(String.valueOf("500"));
                    } else if (weightInt <= 59){
                        ethambutolDose.getEditText().setText(String.valueOf("1500"));
                    } else {
                        ethambutolDose.getEditText().setText(String.valueOf("2000"));
                    }

                    if(weightInt <= 13){
                        moxifloxacilinDose.getEditText().setText(String.valueOf(""));
                        moxifloxacilinDose.getEditText().setHint("Not recommended");
                    } else if (weightInt <= 30){
                        moxifloxacilinDose.getEditText().setText(String.valueOf("200"));
                    } else {
                        moxifloxacilinDose.getEditText().setText(String.valueOf("400"));
                    }

                }

                isoniazidDose.setVisibility(View.GONE);
                rifapentineDose.setVisibility(View.GONE);
                levofloxacinDose.setVisibility(View.GONE);
                ethionamideDose.setVisibility(View.GONE);
                rifapentineAvailable.setVisibility(View.GONE);
                ethambutolDose.setVisibility(View.VISIBLE);
                moxifloxacilinDose.setVisibility(View.VISIBLE);

            }
        } else if (group == rifapentineAvailable.getRadioGroup()) {

            isoniazidDose.getEditText().setHint("");
            rifapentineDose.getEditText().setHint("");
            levofloxacinDose.getEditText().setHint("");
            ethionamideDose.getEditText().setHint("");

            isoniazidDose.getEditText().setText("");
            rifapentineDose.getEditText().setText("");
            levofloxacinDose.getEditText().setText("");
            ethionamideDose.getEditText().setText("");

            int age = App.getPatient().getPerson().getAge();
            int weightInt = 0;
            if (!App.get(weight).equals("")) {
                weightInt = Integer.parseInt(App.get(weight));
            }
            int w = 1;

            if (App.get(rifapentineAvailable).equals(getResources().getString(R.string.no))) {

                isoniazidDose.setVisibility(View.VISIBLE);
                rifapentineDose.setVisibility(View.GONE);
                levofloxacinDose.setVisibility(View.GONE);
                ethionamideDose.setVisibility(View.GONE);


                if (age < 12) w = weightInt * 10;
                else if (age < 15) w = weightInt * 10;
                else w = weightInt * 5;

                isoniazidDose.getEditText().setText(String.valueOf(w));

            } else {

                w = 1;
                w = weightInt * 15;
                isoniazidDose.getEditText().setText(String.valueOf(w));

                /*rifapentineDose.getEditText().setText("");
                if (age < 12)
                    rifapentineDose.getEditText().setHint("300 - 450 mg");
                else
                    rifapentineDose.getEditText().setHint("450 - 900 mg");*/

                isoniazidDose.setVisibility(View.VISIBLE);
                rifapentineDose.setVisibility(View.VISIBLE);
                levofloxacinDose.setVisibility(View.GONE);
                ethionamideDose.setVisibility(View.GONE);

            }
        } else if (group == ancillaryNeed.getRadioGroup()) {
            if (App.get(ancillaryNeed).equals(getResources().getString(R.string.yes))) {
                ancillaryDrugs.setVisibility(View.VISIBLE);
                ancillaryDrugDuration.setVisibility(View.VISIBLE);
            } else {
                ancillaryDrugs.setVisibility(View.GONE);
                ancillaryDrugDuration.setVisibility(View.GONE);
            }
        } else if (group == typeTreatmentSupporter.getRadioGroup()) {
            if (App.get(typeTreatmentSupporter).equals(getResources().getString(R.string.pet_family_treatment_supporter))) {
                relationshipTreatmentSuppoter.setVisibility(View.VISIBLE);
                if (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_other)))
                    other.setVisibility(View.VISIBLE);
                else
                    other.setVisibility(View.GONE);
            } else {
                relationshipTreatmentSuppoter.setVisibility(View.GONE);
                other.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void refill(int encounterId) {

        if(refillFlag){
            refillFlag = true;
            return;
        }

        Boolean refillFlag = false;

        OfflineForm fo = serverService.getSavedFormById(encounterId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("FORM START TIME")) {
                startTime = App.stringToDate(obs[0][1], "yyyy-MM-dd hh:mm:ss");
            } else if (obs[0][0].equals("WEIGHT (KG)")) {
                String weightValue = obs[0][1].replace(".0", "");
                weight.getEditText().setText(weightValue);
            } else if (obs[0][0].equals("INTERVENTION")) {
                for (RadioButton rb : intervention.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet)) && obs[0][1].equals("PET")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.sci)) && obs[0][1].equals("SCI")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("PATIENT ID OF INDEX CASE")) {
                indexPatientId.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("SITE OF TUBERCULOSIS DISEASE")) {

                for (RadioButton rb : tbType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_ptb)) && obs[0][1].equals("PULMONARY TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_eptb)) && obs[0][1].equals("EXTRA-PULMONARY TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    }
                }

            } else if (obs[0][0].equals("TUBERCULOSIS INFECTION TYPE")) {
                for (RadioButton rb : infectionType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_dstb)) && obs[0][1].equals("DRUG-SENSITIVE TUBERCULOSIS INFECTION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_drtb)) && obs[0][1].equals("DRUG-RESISTANT TB")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TUBERCULOSIS DRUG RESISTANCE TYPE")) {
                for (RadioButton rb : resistanceType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_rr_tb)) && obs[0][1].equals("RIFAMPICIN RESISTANT TUBERCULOSIS INFECTION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_dr_tb)) && obs[0][1].equals("MONO DRUG RESISTANT TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_pdr_tb)) && obs[0][1].equals("PANDRUG RESISTANT TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mdr_tb)) && obs[0][1].equals("MULTI-DRUG RESISTANT TUBERCULOSIS INFECTION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_xdr_tb)) && obs[0][1].equals("EXTREMELY DRUG-RESISTANT TUBERCULOSIS INFECTION")) {
                        rb.setChecked(true);
                        break;
                    }

                }
                resistanceType.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("RESISTANT TO ANTI-TUBERCULOSIS DRUGS")) {
                for (CheckBox cb : dstPattern.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_isoniazid)) && obs[0][1].equals("ISONIAZID")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_rifampicin)) && obs[0][1].equals("RIFAMPICIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_amikacin)) && obs[0][1].equals("AMIKACIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_capreomycin)) && obs[0][1].equals("CAPREOMYCIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_streptpmycin)) && obs[0][1].equals("STREPTOMYCIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_ofloxacin)) && obs[0][1].equals("OFLOXACIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_moxifloxacin)) && obs[0][1].equals("MOXIFLOXACIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_ethambutol)) && obs[0][1].equals("ETHAMBUTOL")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_ethionamide)) && obs[0][1].equals("ETHIONAMIDE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_pyrazinamide)) && obs[0][1].equals("PYRAZINAMIDE")) {
                        cb.setChecked(true);
                        break;
                    }

                }
                dstPattern.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT START DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                treatmentInitiationDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
            } else if (obs[0][0].equals("POST-EXPOSURE TREATMENT REGIMEN")) {
                for (RadioButton rb : petRegimen.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy)) && obs[0][1].equals("ISONIAZID PROPHYLAXIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_isoniazid_rifapentine)) && obs[0][1].equals("ISONIAZID AND RIFAPENTINE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_levofloxacin_ethionamide)) && obs[0][1].equals("LEVOFLOXACIN AND ETHIONAMIDE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_levofloxacin_ethambutol)) && obs[0][1].equals("LEVOFLOXACIN AND ETHAMBUTOL")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_levofloxacin_moxifloxacilin)) && obs[0][1].equals("LEVOFLOXACIN AND MOXIFLOXACILIN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_ethionamide_ethambutol)) && obs[0][1].equals("ETHIONAMIDE AND ETHAMBUTOL")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_ethionamide_moxifloxacilin)) && obs[0][1].equals("ETHIONAMIDE AND MOXIFLOXACILIN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_moxifloxacilin_ethambutol)) && obs[0][1].equals("MOXIFLOXACILIN AND ETHAMBUTOL")) {
                        rb.setChecked(true);
                        break;
                    }

                }
                petRegimen.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ISONIAZID DOSE")) {
                isoniazidDose.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("RIFAPENTINE DOSE")) {
                rifapentineDose.getEditText().setText(obs[0][1]);
                rifapentineAvailable.setVisibility(View.VISIBLE);
                for (RadioButton rb : rifapentineAvailable.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes))) {
                        rb.setChecked(true);
                    }
                }
            } else if (obs[0][0].equals("LEVOFLOXACIN DOSE")) {
                levofloxacinDose.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("ETHIONAMIDE DOSE")) {
                ethionamideDose.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("ETHAMBUTOL DOSE")) {
                ethambutolDose.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("MOXIFLOXACILIN DOSE")) {
                moxifloxacilinDose.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("ANCILLARY MEDICATION NEEDED")) {
                for (RadioButton rb : ancillaryNeed.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }

                }
                ancillaryNeed.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ANCILLARY DRUGS")) {
                for (CheckBox cb : ancillaryDrugs.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_iron_deficiency_prtocol)) && obs[0][1].equals("IRON")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_vitamin_d_protocol)) && obs[0][1].equals("VITAMIN D")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_pcm_protocol)) && obs[0][1].equals("PHENYLEPHRINE, CHLORPHENIRAMINE, AND METHSCOPOLAMINE")) {
                        cb.setChecked(true);
                        break;
                    }

                }
                ancillaryDrugs.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("MEDICATION DURATION")) {
                ancillaryDrugDuration.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("NAME OF TREATMENT SUPPORTER")) {
                nameTreatmentSupporter.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("TREATMENT SUPPORTER CONTACT NUMBER")) {
                String[] phoneArray = obs[0][1].split("-");
                phone1a.setText(phoneArray[0]);
                phone1b.setText(phoneArray[1]);
            } else if (obs[0][0].equals("TREATMENT SUPPORTER TYPE")) {
                for (RadioButton rb : typeTreatmentSupporter.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_family_treatment_supporter)) && obs[0][1].equals("FAMILY MEMBER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_non_family_treatment_supporter)) && obs[0][1].equals("NON-FAMILY MEMBER")) {
                        rb.setChecked(true);
                        break;
                    }

                }
            } else if (obs[0][0].equals("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT")) {
                String value = obs[0][1].equals("MOTHER") ? getResources().getString(R.string.pet_mother) :
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
                                                                                                                obs[0][1].equals("UNCLE") ? getResources().getString(R.string.pet_uncle) : getResources().getString(R.string.pet_other)))))))));
                relationshipTreatmentSuppoter.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("OTHER FAMILY MEMBER")) {
                other.getEditText().setText(obs[0][1]);
                other.setVisibility(View.VISIBLE);
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
