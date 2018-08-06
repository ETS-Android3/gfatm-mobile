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
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
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
 * Created by Rabbia on 11/24/2016.
 */

public class PetAdverseEventForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    TitledButton formDate;
    TitledEditText weight;

    TitledCheckBoxes symptoms;
    TitledEditText otherSideEffects;
    TitledRadioGroup sideeffectsConsistent;
    TitledRadioGroup severity;

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
    TitledCheckBoxes ancillaryDrugs;
    TitledEditText ancillaryDrugDuration;
    TitledEditText newInstruction;
    TitledButton returnVisitDate;

    TitledEditText clincianNote;

    ScrollView scrollView;

    Boolean refillFlag = false;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 2;
        formName = Forms.PET_ADVERSE_EVENTS;
        form = Forms.pet_adverseEvents;

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
        weight = new TitledEditText(context, null, getResources().getString(R.string.pet_weight), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);

        symptoms = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_symptoms), getResources().getStringArray(R.array.pet_pet_symptoms_list), null, App.VERTICAL, App.VERTICAL, true);

        MyLinearLayout linearLayout1 = new MyLinearLayout(context, getResources().getString(R.string.pet_adverse_events_followup_details), App.VERTICAL);
        otherSideEffects = new TitledEditText(context, null, getResources().getString(R.string.pet_other_side_effects), "", "", 1000, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        otherSideEffects.getEditText().setSingleLine(false);
        otherSideEffects.getEditText().setMinimumHeight(150);
        sideeffectsConsistent = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_side_effect_consistent), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        severity = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adverse_event_severity), getResources().getStringArray(R.array.pet_adverse_event_severity_list), getResources().getString(R.string.pet_mild), App.VERTICAL, App.VERTICAL);

        linearLayout1.addView(symptoms);
        linearLayout1.addView(otherSideEffects);
        linearLayout1.addView(sideeffectsConsistent);
        linearLayout1.addView(severity);

        MyLinearLayout linearLayout2 = new MyLinearLayout(context, getResources().getString(R.string.pet_symptoms_require), App.VERTICAL);
        actionPlan = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_action_plan), getResources().getStringArray(R.array.pet_action_plan), null, App.VERTICAL, App.VERTICAL, true);
        medicationDiscontinueReason = new TitledEditText(context, null, getResources().getString(R.string.pet_discontinue_medication_reason), "", "", 250, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        medicationDiscontinueReason.getEditText().setSingleLine(false);
        medicationDiscontinueReason.getEditText().setMinimumHeight(150);
        medicationDiscontinueDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_discontinue_medication_duration), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        newMedication = new TitledEditText(context, null, getResources().getString(R.string.pet_new_medication_reason), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        newMedication.getEditText().setSingleLine(false);
        newMedication.getEditText().setMinimumHeight(150);
        newMedicationDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_new_medication_duration), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        petRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_regimen), getResources().getStringArray(R.array.pet_regimens_temp), "", App.VERTICAL, App.VERTICAL);
        rifapentineAvailable = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_rifapentine_available), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        isoniazidDose = new TitledEditText(context, null, getResources().getString(R.string.pet_isoniazid_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        rifapentineDose = new TitledEditText(context, null, getResources().getString(R.string.pet_rifapentine_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        levofloxacinDose = new TitledEditText(context, null, getResources().getString(R.string.pet_levofloxacin_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        ethionamideDose = new TitledEditText(context, null, getResources().getString(R.string.pet_ethionamide_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        ancillaryDrugs = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_ancillary_drugs), getResources().getStringArray(R.array.pet_ancillary_drugs), null, App.VERTICAL, App.VERTICAL, true);
        ancillaryDrugDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_ancillary_drug_duration_days), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        newInstruction = new TitledEditText(context, null, getResources().getString(R.string.pet_new_instructions), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        newInstruction.getEditText().setSingleLine(false);
        newInstruction.getEditText().setMinimumHeight(150);
        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.pet_return_visit_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.VERTICAL);
        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.pet_doctor_notes), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        clincianNote.getEditText().setSingleLine(false);
        clincianNote.getEditText().setMinimumHeight(150);

        linearLayout2.addView(actionPlan);
        linearLayout2.addView(medicationDiscontinueReason);
        linearLayout2.addView(medicationDiscontinueDuration);
        linearLayout2.addView(newMedication);
        linearLayout2.addView(newMedicationDuration);
        linearLayout2.addView(petRegimen);
        linearLayout2.addView(rifapentineAvailable);
        linearLayout2.addView(isoniazidDose);
        linearLayout2.addView(rifapentineDose);
        linearLayout2.addView(levofloxacinDose);
        linearLayout2.addView(ethionamideDose);
        linearLayout2.addView(ancillaryDrugs);
        linearLayout2.addView(ancillaryDrugDuration);
        linearLayout2.addView(newInstruction);
        linearLayout2.addView(returnVisitDate);
        linearLayout2.addView(clincianNote);

        views = new View[]{formDate.getButton(), weight.getEditText(), otherSideEffects.getEditText(), sideeffectsConsistent.getRadioGroup(),
                actionPlan, medicationDiscontinueReason.getEditText(), medicationDiscontinueDuration.getEditText(), newMedication.getEditText(), newMedicationDuration.getEditText(),
                petRegimen.getRadioGroup(), isoniazidDose.getEditText(), rifapentineDose.getEditText(), levofloxacinDose.getEditText(), ethionamideDose.getEditText(), ancillaryDrugs, ancillaryDrugDuration.getEditText(),
                newInstruction.getEditText(), returnVisitDate.getButton(), rifapentineAvailable.getRadioGroup(), clincianNote.getEditText(), symptoms, severity.getRadioGroup(),

                petRegimen
        };

        viewGroups = new View[][]{{formDate, weight, linearLayout1},
                {linearLayout2}};

        formDate.getButton().setOnClickListener(this);
        returnVisitDate.getButton().setOnClickListener(this);
        petRegimen.getRadioGroup().setOnCheckedChangeListener(this);
        rifapentineAvailable.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : actionPlan.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : symptoms.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        petRegimen.getRadioGroup().setOnCheckedChangeListener(this);

        weight.getEditText().addTextChangedListener(new TextWatcher() {
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

                calculateDosages();

            }
        });

        levofloxacinDose.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!App.get(levofloxacinDose).equals("")) {
                    int dose = Integer.parseInt(App.get(levofloxacinDose));
                    if (dose > 2000)
                        levofloxacinDose.getEditText().setError(getResources().getString(R.string.pet_dose_exceeded_2000));
                    else
                        levofloxacinDose.getEditText().setError(null);
                }
            }
        });

        ethionamideDose.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!App.get(ethionamideDose).equals("")) {
                    int dose = Integer.parseInt(App.get(ethionamideDose));
                    if (dose > 2000)
                        ethionamideDose.getEditText().setError(getResources().getString(R.string.pet_dose_exceeded_2000));
                    else
                        ethionamideDose.getEditText().setError(null);
                }
            }
        });

        rifapentineDose.getEditText().addTextChangedListener(new TextWatcher() {
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
                    if (dose > 2000)
                        rifapentineDose.getEditText().setError(getResources().getString(R.string.pet_dose_exceeded_2000));
                    else
                        rifapentineDose.getEditText().setError(null);
                }
            }
        });

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
                    Double dose = Double.parseDouble(App.get(isoniazidDose));
                    if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy))) {
                        if (dose > 300) {
                            isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded_300));
                            isoniazidDose.getEditText().requestFocus();
                        }
                    } else {
                        if (dose > 2000) {
                            isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_dose_exceeded_2000));
                            isoniazidDose.getEditText().requestFocus();
                        }
                    }
                }
            }
        });


        ancillaryDrugDuration.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!App.get(ancillaryDrugDuration).equals("") && ancillaryDrugDuration.getVisibility() == View.VISIBLE) {
                    int val = Integer.parseInt(App.get(ancillaryDrugDuration));
                    if(val > 150)
                        ancillaryDrugDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                    else
                        ancillaryDrugDuration.getEditText().setError(null);
                } else
                    ancillaryDrugDuration.getEditText().setError(null);
            }
        });

        newMedicationDuration.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!App.get(newMedicationDuration).equals("") && newMedicationDuration.getVisibility() == View.VISIBLE) {
                    int val = Integer.parseInt(App.get(newMedicationDuration));
                    if(val > 150)
                        newMedicationDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                    else
                        newMedicationDuration.getEditText().setError(null);
                } else
                    newMedicationDuration.getEditText().setError(null);
            }
        });

        medicationDiscontinueDuration.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!App.get(medicationDiscontinueDuration).equals("") && medicationDiscontinueDuration.getVisibility() == View.VISIBLE) {
                    int val = Integer.parseInt(App.get(medicationDiscontinueDuration));
                    if(val > 150)
                        medicationDiscontinueDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                    else
                        medicationDiscontinueDuration.getEditText().setError(null);
                } else
                    medicationDiscontinueDuration.getEditText().setError(null);
            }
        });

        resetViews();

    }

    @Override
    public void updateDisplay() {

        if(refillFlag){
            refillFlag = true;
            return;
        }

        if (snackbar != null)
            snackbar.dismiss();

        returnVisitDate.getButton().setEnabled(true);
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
    public void resetViews() {
        super.resetViews();

        if(App.getLocation().equals("IBEX-KHI")){
            weight.setVisibility(View.GONE);
        }

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        medicationDiscontinueReason.setVisibility(View.GONE);
        medicationDiscontinueDuration.setVisibility(View.GONE);
        newMedication.setVisibility(View.GONE);
        newMedicationDuration.setVisibility(View.GONE);
        petRegimen.setVisibility(View.GONE);
        isoniazidDose.setVisibility(View.GONE);
        rifapentineDose.setVisibility(View.GONE);
        levofloxacinDose.setVisibility(View.GONE);
        ethionamideDose.setVisibility(View.GONE);
        ancillaryDrugs.setVisibility(View.GONE);
        ancillaryDrugDuration.setVisibility(View.GONE);
        otherSideEffects.setVisibility(View.GONE);
        rifapentineAvailable.setVisibility(View.GONE);

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
                    String weight = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "WEIGHT (KG)");
                    String intervention = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_BASELINE_SCREENING, "INTERVENTION");

                    String date1 = "";
                    String date2 = "";
                    String date3 = "";
                    String petRegimen1 = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "POST-EXPOSURE TREATMENT REGIMEN");
                    if (!(petRegimen1 == null || petRegimen1.equals("")))
                        date1 = serverService.getLatestEncounterDateTime(App.getPatientId(), Forms.PET_ADVERSE_EVENTS);
                    String petRegimen2 = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "POST-EXPOSURE TREATMENT REGIMEN");
                    if (!(petRegimen2 == null || petRegimen2.equals("")))
                        date2 = serverService.getLatestEncounterDateTime(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP);
                    String petRegimen3 = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "POST-EXPOSURE TREATMENT REGIMEN");
                    if (!(petRegimen3 == null || petRegimen3.equals("")))
                        date3 = serverService.getLatestEncounterDateTime(App.getPatientId(), Forms.PET_TREATMENT_INITIATION);


                    String isonoazidDose = "";
                    String rifapentineDose = "";
                    String levofloxacinDose = "";
                    String ethionamideDose = "";

                    if ((date2 == null || date2.equals("")) && (date3 == null || date3.equals(""))) {
                        if (petRegimen1 == null)
                            petRegimen1 = "";
                        result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen1);
                        isonoazidDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "ISONIAZID DOSE");
                        rifapentineDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "RIFAPENTINE DOSE");
                        levofloxacinDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "LEVOFLOXACIN DOSE");
                        ethionamideDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "ETHIONAMIDE DOSE");
                    } else {
                        if (date2 == null || date2.equals("")) {
                            if (petRegimen3 == null)
                                petRegimen3 = "";
                            result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen3);
                            isonoazidDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "ISONIAZID DOSE");
                            rifapentineDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "RIFAPENTINE DOSE");
                            levofloxacinDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "LEVOFLOXACIN DOSE");
                            ethionamideDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "ETHIONAMIDE DOSE");
                        } else if (date3 == null || date3.equals("")) {
                            if (petRegimen2 == null)
                                petRegimen2 = "";
                            result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen2);
                            isonoazidDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "ISONIAZID DOSE");
                            rifapentineDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "RIFAPENTINE DOSE");
                            levofloxacinDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "LEVOFLOXACIN DOSE");
                            ethionamideDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "ETHIONAMIDE DOSE");
                        } else {

                            Date d2 = null;
                            Date d3 = null;
                            if (date2.contains("/")) {
                                d2 = App.stringToDate(date2, "dd/MM/yyyy");
                            } else {
                                d2 = App.stringToDate(date2, "yyyy-MM-dd");
                            }

                            if (date3.contains("/")) {
                                d3 = App.stringToDate(date3, "dd/MM/yyyy");
                            } else {
                                d3 = App.stringToDate(date3, "yyyy-MM-dd");
                            }

                            if (d2.equals(d3)) {
                                if (petRegimen3 == null)
                                    petRegimen3 = "";
                                result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen3);
                                isonoazidDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "ISONIAZID DOSE");
                                rifapentineDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "RIFAPENTINE DOSE");
                                levofloxacinDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "LEVOFLOXACIN DOSE");
                                ethionamideDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "ETHIONAMIDE DOSE");
                            } else if (d2.after(d3)) {
                                if (petRegimen2 == null)
                                    petRegimen2 = "";
                                result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen2);
                                isonoazidDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "ISONIAZID DOSE");
                                rifapentineDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "RIFAPENTINE DOSE");
                                levofloxacinDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "LEVOFLOXACIN DOSE");
                                ethionamideDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "ETHIONAMIDE DOSE");
                            } else {
                                petRegimen3 = "";
                                result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen3);
                                isonoazidDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "ISONIAZID DOSE");
                                rifapentineDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "RIFAPENTINE DOSE");
                                levofloxacinDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "LEVOFLOXACIN DOSE");
                                ethionamideDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "ETHIONAMIDE DOSE");
                            }

                        }
                    }
                    if (weight == null)
                        weight = "";
                    else
                        weight = weight.replace(".0", "");

                    result.put("WEIGHT (KG)", weight);

                    if (isonoazidDose == null)
                        isonoazidDose = "";
                    isonoazidDose = isonoazidDose.replace(".0", "");
                    result.put("ISONIAZID DOSE", isonoazidDose);

                    if (rifapentineDose == null)
                        rifapentineDose = "";
                    rifapentineDose = rifapentineDose.replace(".0", "");
                    result.put("RIFAPENTINE DOSE", rifapentineDose);

                    if (levofloxacinDose == null)
                        levofloxacinDose = "";
                    levofloxacinDose = levofloxacinDose.replace(".0", "");
                    result.put("LEVOFLOXACIN DOSE", levofloxacinDose);

                    if (ethionamideDose == null)
                        ethionamideDose = "";
                    ethionamideDose = ethionamideDose.replace(".0", "");
                    result.put("ETHIONAMIDE DOSE", ethionamideDose);

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

                    if (result.get("POST-EXPOSURE TREATMENT REGIMEN") == null || result.get("POST-EXPOSURE TREATMENT REGIMEN").equals("")) {
                        /*final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        alertDialog.setMessage(getResources().getString(R.string.treatment_initiation_missing));
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
                        submitButton.setEnabled(false);*/
                        return;
                    } else submitButton.setEnabled(true);

                    weight.getEditText().setText(result.get("WEIGHT (KG)"));
                    isoniazidDose.getEditText().setText(result.get("ISONIAZID DOSE"));
                    rifapentineDose.getEditText().setText(result.get("RIFAPENTINE DOSE"));
                    levofloxacinDose.getEditText().setText(result.get("LEVOFLOXACIN DOSE"));
                    ethionamideDose.getEditText().setText(result.get("ETHIONAMIDE DOSE"));

                    for (RadioButton rb : petRegimen.getRadioGroup().getButtons()) {

                        if (rb.getText().equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy)) && result.get("POST-EXPOSURE TREATMENT REGIMEN").equals("ISONIAZID PROPHYLAXIS")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.pet_isoniazid_rifapentine)) && result.get("POST-EXPOSURE TREATMENT REGIMEN").equals("ISONIAZID AND RIFAPENTINE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.pet_levofloxacin_ethionamide)) && result.get("POST-EXPOSURE TREATMENT REGIMEN").equals("LEVOFLOXACIN AND ETHIONAMIDE")) {
                            rb.setChecked(true);
                            break;
                        }

                    }

                    rifapentineAvailable.setVisibility(View.GONE);
                    isoniazidDose.setVisibility(View.GONE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.GONE);
                    ethionamideDose.setVisibility(View.GONE);

                }
            };
            autopopulateFormTask.execute("");
        }

    }


    @Override
    public boolean validate() {

        View view = null;
        Boolean error = false;

        if (App.get(newInstruction).trim().isEmpty()) {
            newInstruction.getEditText().setError(getString(R.string.empty_field));
            newInstruction.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        } else {
            newInstruction.getEditText().clearFocus();
            newInstruction.getEditText().setError(null);
        }

        if (ancillaryDrugDuration.getVisibility() == View.VISIBLE && App.get(ancillaryDrugDuration).isEmpty()) {
            ancillaryDrugDuration.getEditText().setError(getString(R.string.empty_field));
            ancillaryDrugDuration.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        } else {

            if (ancillaryDrugDuration.getVisibility() == View.VISIBLE) {
                int val = Integer.parseInt(App.get(ancillaryDrugDuration));
                if(val > 150) {
                    ancillaryDrugDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                    ancillaryDrugDuration.getEditText().requestFocus();
                    error = true;
                    gotoLastPage();
                    view = null;
                }
                else {
                    ancillaryDrugDuration.getEditText().setError(null);
                    ancillaryDrugDuration.getEditText().requestFocus();
                }
            } else {
                ancillaryDrugDuration.getEditText().setError(null);
                ancillaryDrugDuration.getEditText().clearFocus();
            }

        }

        if (ancillaryDrugs.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : ancillaryDrugs.getCheckedBoxes()) {
                if (cb.isChecked())
                    flag = true;
            }
            if (!flag) {
                ancillaryDrugs.getQuestionView().setError(getString(R.string.empty_field));
                ancillaryDrugs.getQuestionView().requestFocus();
                gotoLastPage();
                view = ancillaryDrugs;
                error = true;
            } else {
                ancillaryDrugs.getQuestionView().clearFocus();
                ancillaryDrugs.getQuestionView().setError(null);
            }
        }

        if (petRegimen.getVisibility() == View.VISIBLE && App.get(petRegimen).isEmpty()) {
            petRegimen.getQuestionView().setError(getString(R.string.empty_field));
            petRegimen.getQuestionView().requestFocus();
            view = petRegimen;
            gotoLastPage();
            error = true;
        }  else {
            petRegimen.getQuestionView().clearFocus();
            petRegimen.getQuestionView().setError(null);
        }

        if (isoniazidDose.getVisibility() == View.VISIBLE) {
            if (App.get(isoniazidDose).isEmpty()) {
                isoniazidDose.getEditText().setError(getString(R.string.empty_field));
                isoniazidDose.getEditText().requestFocus();
                view = null;
                error = true;
            } else {
                Double dose = Double.parseDouble(App.get(isoniazidDose));
                if(App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy))) {
                    if (dose > 300) {
                        isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded_300));
                        isoniazidDose.getEditText().requestFocus();
                        view = null;
                        error = true;
                    }else {
                        isoniazidDose.getEditText().clearFocus();
                        isoniazidDose.getEditText().setError(null);
                    }
                }
                else{
                    if (dose > 2000) {
                        isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_dose_exceeded_2000));
                        isoniazidDose.getEditText().requestFocus();
                        view = null;
                        error = true;
                    }
                    else {
                        isoniazidDose.getEditText().clearFocus();
                        isoniazidDose.getEditText().setError(null);
                    }
                }
            }
        }
        if (rifapentineDose.getVisibility() == View.VISIBLE) {
            if (App.get(rifapentineDose).isEmpty()) {
                rifapentineDose.getEditText().setError(getString(R.string.empty_field));
                rifapentineDose.getEditText().requestFocus();
                view = null;
                error = true;
            }else {

                int dose = Integer.parseInt(App.get(rifapentineDose));
                if (dose > 2000) {
                    rifapentineDose.getEditText().setError(getResources().getString(R.string.pet_dose_exceeded_2000));
                    rifapentineDose.getEditText().requestFocus();
                    gotoLastPage();
                } else {
                    rifapentineDose.getEditText().clearFocus();
                    rifapentineDose.getEditText().setError(null);
                }
            }
        }
        if(levofloxacinDose.getVisibility() == View.VISIBLE ) {
            if (App.get(levofloxacinDose).isEmpty()) {
                levofloxacinDose.getEditText().setError(getString(R.string.empty_field));
                levofloxacinDose.getEditText().requestFocus();
                gotoLastPage();
                view = null;
                error = true;
            } else {
                int dose = Integer.parseInt(App.get(levofloxacinDose));
                if (dose > 2000) {
                    levofloxacinDose.getEditText().setError(getResources().getString(R.string.pet_dose_exceeded_2000));
                    levofloxacinDose.getEditText().requestFocus();
                    gotoLastPage();
                } else {
                    levofloxacinDose.getEditText().clearFocus();
                    levofloxacinDose.getEditText().setError(null);
                }
            }
        }

        if(ethionamideDose.getVisibility() == View.VISIBLE) {
            if (App.get(ethionamideDose).isEmpty()) {
                ethionamideDose.getEditText().setError(getString(R.string.empty_field));
                ethionamideDose.getEditText().requestFocus();
                gotoLastPage();
                view = null;
                error = true;
            } else {
                int dose = Integer.parseInt(App.get(ethionamideDose));
                if (dose > 2000) {
                    ethionamideDose.getEditText().setError(getResources().getString(R.string.pet_dose_exceeded_2000));
                    ethionamideDose.getEditText().requestFocus();
                    gotoLastPage();
                } else {
                    ethionamideDose.getEditText().clearFocus();
                    ethionamideDose.getEditText().setError(null);
                }
            }
        }

        if (newMedication.getVisibility() == View.VISIBLE && App.get(newMedication).isEmpty()) {
            newMedication.getEditText().setError(getString(R.string.empty_field));
            newMedication.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        }else {
            newMedication.getEditText().clearFocus();
            newMedication.getEditText().setError(null);
        }

        if (newMedicationDuration.getVisibility() == View.VISIBLE && App.get(newMedicationDuration).isEmpty()) {
            newMedicationDuration.getEditText().setError(getString(R.string.empty_field));
            newMedicationDuration.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        }else {
            if (newMedicationDuration.getVisibility() == View.VISIBLE) {
                int val = Integer.parseInt(App.get(newMedicationDuration));
                if(val > 150) {
                    newMedicationDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                    newMedicationDuration.getEditText().requestFocus();
                    error = true;
                    gotoLastPage();
                    view = null;
                }
                else {
                    newMedicationDuration.getEditText().setError(null);
                    newMedicationDuration.getEditText().requestFocus();
                }
            } else {
                newMedicationDuration.getEditText().setError(null);
                newMedicationDuration.getEditText().clearFocus();
            }
        }

        if (medicationDiscontinueReason.getVisibility() == View.VISIBLE && App.get(medicationDiscontinueReason).isEmpty()) {
            medicationDiscontinueReason.getEditText().setError(getString(R.string.empty_field));
            medicationDiscontinueReason.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        }else {
            medicationDiscontinueReason.getEditText().clearFocus();
            medicationDiscontinueReason.getEditText().setError(null);
        }

        if (medicationDiscontinueDuration.getVisibility() == View.VISIBLE && App.get(medicationDiscontinueDuration).isEmpty()) {
            medicationDiscontinueDuration.getEditText().setError(getString(R.string.empty_field));
            medicationDiscontinueDuration.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        }else {
            if (medicationDiscontinueDuration.getVisibility() == View.VISIBLE) {
                int val = Integer.parseInt(App.get(medicationDiscontinueDuration));
                if(val > 150) {
                    medicationDiscontinueDuration.getEditText().setError(getString(R.string.pet_valid_range_150));
                    medicationDiscontinueDuration.getEditText().requestFocus();
                    error = true;
                    gotoLastPage();
                    view = null;
                }
                else {
                    medicationDiscontinueDuration.getEditText().setError(null);
                    medicationDiscontinueDuration.getEditText().requestFocus();
                }
            } else {
                medicationDiscontinueDuration.getEditText().setError(null);
                medicationDiscontinueDuration.getEditText().clearFocus();
            }
        }

        /*if (App.get(weight).isEmpty()) {
            weight.getEditText().setError(getString(R.string.empty_field));
            weight.getEditText().requestFocus();
            gotoFirstPage();
            view = null;
            error = true;
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
                                        newInstruction.clearFocus();
                                        ancillaryDrugDuration.clearFocus();
                                        isoniazidDose.clearFocus();
                                        rifapentineDose.clearFocus();
                                        levofloxacinDose.clearFocus();
                                        ethionamideDose.clearFocus();
                                        newMedication.clearFocus();
                                        newMedicationDuration.clearFocus();
                                        medicationDiscontinueDuration.clearFocus();
                                        medicationDiscontinueReason.clearFocus();
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
        observations.add(new String[]{"WEIGHT (KG)", App.get(weight)});

        String adverseEventString = "";
        for(CheckBox cb : symptoms.getCheckedBoxes()){
            if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_dizziness)))
                adverseEventString = adverseEventString + "DIZZINESS AND GIDDINESS" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_nausea_vomiting)))
                adverseEventString = adverseEventString + "NAUSEA AND VOMITING" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_abdominal_pain)))
                adverseEventString = adverseEventString + "ABDOMINAL PAIN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_reduced_appetite)))
                adverseEventString = adverseEventString + "LOSS OF APPETITE" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_jaundice)))
                adverseEventString = adverseEventString + "JAUNDICE" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_rash)))
                adverseEventString = adverseEventString + "RASH" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_tendon_pain)))
                adverseEventString = adverseEventString + "TENDON PAIN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_eye_problems)))
                adverseEventString = adverseEventString + "VISION PROBLEM" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_hypersensitivity_reaction)))
                adverseEventString = adverseEventString + "HYPERSENSITIVITY REACTION" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_body_fluid_discoloration)))
                adverseEventString = adverseEventString + "DISCOLORATION OF BODY FLUID" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_diarrohea)))
                adverseEventString = adverseEventString + "DIARRHEA" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_muscle_pain)))
                adverseEventString = adverseEventString + "MUSCLE PAIN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other)))
                adverseEventString = adverseEventString + "OTHER ADVERSE EVENT" + " ; ";
        }
        observations.add(new String[]{"ADVERSE EVENTS", adverseEventString});

        if(otherSideEffects.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER ADVERSE EVENT", App.get(otherSideEffects)});
        observations.add(new String[]{"COMPLAINTS CONSISTENT WITH DRUG SIDE EFFECTS", App.get(sideeffectsConsistent).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        observations.add(new String[]{"SEVERITY OF ADVERSE REACTION", App.get(severity).equals(getResources().getString(R.string.pet_mild)) ? "MILD" :
                (App.get(severity).equals(getResources().getString(R.string.pet_moderate)) ? "MODERATE" : "SEVERE")});

        String actionPlanString = "";
        for (CheckBox cb : actionPlan.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_continue_medication_greater_adherence)))
                actionPlanString = actionPlanString + "CONTINUE MEDICATION (HIGH ADHERENCE)" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_continue_medication_less_adherence)))
                actionPlanString = actionPlanString + "CONTINUE MEDICATION (LOW ADHERENCE)" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_discontinue_medication)))
                actionPlanString = actionPlanString + "DISCONTINUE MEDICATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_begin_cxr_protocol)))
                actionPlanString = actionPlanString + "BEGIN CLINICAL MONITORING PROTOCOL" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_new_medication)))
                actionPlanString = actionPlanString + "GIVE NEW MEDICATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_seek_expert_consultation)))
                actionPlanString = actionPlanString + "SEEK EXPERT CONSULTATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_change_drug_dosage)))
                actionPlanString = actionPlanString + "CHANGE DRUG DOSAGE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_ancillary_drug)))
                actionPlanString = actionPlanString + "GIVE ANCILLARY DRUG" + " ; ";
        }
        observations.add(new String[]{"ACTION PLAN", actionPlanString});
        if (medicationDiscontinueReason.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON TO DISCONTINUE MEDICATION", App.get(medicationDiscontinueReason)});
        if (medicationDiscontinueDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DURATION OF DISCONTINUATION IN DAYS", App.get(medicationDiscontinueDuration)});
        if (newMedication.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"NEW MEDICATION", App.get(newMedication)});
        if (newMedicationDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DURATION OF NEW MEDICATION IN DAYS", App.get(newMedicationDuration)});

        if (petRegimen.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"POST-EXPOSURE TREATMENT REGIMEN", App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy)) ? "ISONIAZID PROPHYLAXIS" :
                    (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine)) ? "ISONIAZID AND RIFAPENTINE" : "LEVOFLOXACIN AND ETHIONAMIDE")});

        if (isoniazidDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ISONIAZID DOSE", App.get(isoniazidDose)});
        if (rifapentineDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"RIFAPENTINE DOSE", App.get(rifapentineDose)});
        if (levofloxacinDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"LEVOFLOXACIN DOSE", App.get(levofloxacinDose)});
        if (ethionamideDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ETHIONAMIDE DOSE", App.get(ethionamideDose)});
        if (ancillaryDrugs.getVisibility() == View.VISIBLE) {
            String ancillaryDrugString = "";
            for (CheckBox cb : ancillaryDrugs.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_iron_deficiency_prtocol)))
                    ancillaryDrugString = ancillaryDrugString + "IRON" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_vitamin_d_protocol)))
                    ancillaryDrugString = ancillaryDrugString + "VITAMIN D" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_pcm_protocol)))
                    ancillaryDrugString = ancillaryDrugString + "PHENYLEPHRINE, CHLORPHENIRAMINE, AND METHSCOPOLAMINE" + " ; ";
            }
            observations.add(new String[]{"ANCILLARY DRUGS", ancillaryDrugString});
        }
        if (ancillaryDrugDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"MEDICATION DURATION", App.get(ancillaryDrugDuration)});
        observations.add(new String[]{"INSTRUCTIONS TO PATIENT AND/OR FAMILY", App.get(newInstruction)});
        observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDate(secondDateCalendar)});
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

        } else if (view == returnVisitDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowFutureDate", true);
            args.putBoolean("allowPastDate", false);
            args.putString("formDate", formDate.getButton().getText().toString());
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

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        for (CheckBox cb : actionPlan.getCheckedBoxes()) {

            if (App.get(cb).equals(getResources().getString(R.string.pet_discontinue_medication))) {
                if (cb.isChecked()) {
                    medicationDiscontinueReason.setVisibility(View.VISIBLE);
                    medicationDiscontinueDuration.setVisibility(View.VISIBLE);
                } else {
                    medicationDiscontinueReason.setVisibility(View.GONE);
                    medicationDiscontinueDuration.setVisibility(View.GONE);
                }
            }

            if (App.get(cb).equals(getResources().getString(R.string.pet_new_medication))) {
                if (cb.isChecked()) {
                    newMedication.setVisibility(View.VISIBLE);
                    newMedicationDuration.setVisibility(View.VISIBLE);
                } else {
                    newMedication.setVisibility(View.GONE);
                    newMedicationDuration.setVisibility(View.GONE);
                }
            }

            if (App.get(cb).equals(getResources().getString(R.string.pet_ancillary_drug))) {
                if (cb.isChecked()) {
                    ancillaryDrugs.setVisibility(View.VISIBLE);
                    ancillaryDrugDuration.setVisibility(View.VISIBLE);
                } else {
                    ancillaryDrugs.setVisibility(View.GONE);
                    ancillaryDrugDuration.setVisibility(View.GONE);
                }
            }

            if (App.get(cb).equals(getResources().getString(R.string.pet_change_drug_dosage))) {
                if (cb.isChecked()) {
                    petRegimen.setVisibility(View.VISIBLE);
                    isoniazidDose.setVisibility(View.GONE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.GONE);
                    ethionamideDose.setVisibility(View.GONE);
                    if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy)))
                        isoniazidDose.setVisibility(View.VISIBLE);
                    else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine))) {
                        isoniazidDose.setVisibility(View.VISIBLE);
                        rifapentineDose.setVisibility(View.VISIBLE);
                    } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethionamide))) {
                        levofloxacinDose.setVisibility(View.VISIBLE);
                        ethionamideDose.setVisibility(View.VISIBLE);
                    }

                } else {
                    petRegimen.setVisibility(View.GONE);
                    isoniazidDose.setVisibility(View.GONE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.GONE);
                    ethionamideDose.setVisibility(View.GONE);
                    rifapentineAvailable.setVisibility(View.GONE);
                }
            }
        }

        for (CheckBox cb : symptoms.getCheckedBoxes()) {

            if (App.get(cb).equals(getResources().getString(R.string.pet_other))) {
                if (cb.isChecked()) {
                    otherSideEffects.setVisibility(View.VISIBLE);
                } else {
                    otherSideEffects.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (group == petRegimen.getRadioGroup()) {
            calculateDosages();
        } else if (group == rifapentineAvailable.getRadioGroup()) {

            int age = App.getPatient().getPerson().getAge();
            Double weightDouble = Double.parseDouble("0");
            if (!App.get(weight).equals("")) {
                weightDouble = Double.parseDouble(App.get(weight));
            }

            Double w = 1.0;

            rifapentineAvailable.setVisibility(View.VISIBLE);
            if (App.get(rifapentineAvailable).equals(getResources().getString(R.string.no))) {

                if(age < 15) {
                    w = weightDouble * 10f;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));
                } else {
                    w = weightDouble * 5f;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));
                }

                isoniazidDose.setVisibility(View.VISIBLE);
                rifapentineDose.setVisibility(View.GONE);
                levofloxacinDose.setVisibility(View.GONE);
                ethionamideDose.setVisibility(View.GONE);

                if(w > 300)
                    isoniazidDose.getEditText().setError(getString(R.string.pet_isoniazid_dose_exceeded_300));

            } else {

                w = 1.0;


                if(age < 2){
                    w = weightDouble * 10f;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));

                    rifapentineDose.getEditText().setHint("Not recommended");
                } else if (age < 12){
                    w = weightDouble * 15;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));

                    rifapentineDose.getEditText().setHint("300 - 450 mg");
                } else {
                    w = weightDouble * 25;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));

                    rifapentineDose.getEditText().setHint("450 - 900 mg");
                }

                isoniazidDose.setVisibility(View.VISIBLE);
                rifapentineDose.setVisibility(View.VISIBLE);
                levofloxacinDose.setVisibility(View.GONE);
                ethionamideDose.setVisibility(View.GONE);

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

            if(obs[0][0].equals("TIME TAKEN TO FILL FORMs")){
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("WEIGHT (KG)")) {
                weight.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("ADVERSE EVENTS")) {
                for (CheckBox cb : symptoms.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_dizziness)) && obs[0][1].equals("DIZZINESS AND GIDDINESS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_nausea_vomiting)) && obs[0][1].equals("NAUSEA AND VOMITING")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_abdominal_pain)) && obs[0][1].equals("ABDOMINAL PAIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_reduced_appetite)) && obs[0][1].equals("LOSS OF APPETITE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_jaundice)) && obs[0][1].equals("JAUNDICE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_rash)) && obs[0][1].equals("RASH")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_tendon_pain)) && obs[0][1].equals("TENDON PAIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_eye_problems)) && obs[0][1].equals("VISION PROBLEM")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_hypersensitivity_reaction)) && obs[0][1].equals("HYPERSENSITIVITY REACTION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_body_fluid_discoloration)) && obs[0][1].equals("DISCOLORATION OF BODY FLUID")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_diarrohea)) && obs[0][1].equals("DIARRHEA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_muscle_pain)) && obs[0][1].equals("MUSCLE PAIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_other)) && obs[0][1].equals("OTHER ADVERSE EVENT")) {
                        cb.setChecked(true);
                        break;
                    }

                }
                symptoms.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER ADVERSE EVENT")) {
                otherSideEffects.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COMPLAINTS CONSISTENT WITH DRUG SIDE EFFECTS")) {
                for (RadioButton rb : sideeffectsConsistent.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("ACTION PLAN")) {
                for (CheckBox cb : actionPlan.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_continue_medication_greater_adherence)) && obs[0][1].equals("CONTINUE MEDICATION (HIGH ADHERENCE)")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_continue_medication_less_adherence)) && obs[0][1].equals("CONTINUE MEDICATION (LOW ADHERENCE)")) {
                        cb.setChecked(true);
                        break;
                    }
                    if (cb.getText().equals(getResources().getString(R.string.pet_discontinue_medication)) && obs[0][1].equals("DISCONTINUE MEDICATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_begin_cxr_protocol)) && obs[0][1].equals("BEGIN CLINICAL MONITORING PROTOCOL")) {
                        cb.setChecked(true);
                        break;
                    }
                    if (cb.getText().equals(getResources().getString(R.string.pet_new_medication)) && obs[0][1].equals("GIVE NEW MEDICATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_seek_expert_consultation)) && obs[0][1].equals("SEEK EXPERT CONSULTATION")) {
                        cb.setChecked(true);
                        break;
                    }
                    if (cb.getText().equals(getResources().getString(R.string.pet_change_drug_dosage)) && obs[0][1].equals("CHANGE DRUG DOSAGE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_ancillary_drug)) && obs[0][1].equals("GIVE ANCILLARY DRUG")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("REASON TO DISCONTINUE MEDICATION")) {
                medicationDiscontinueReason.getEditText().setText(obs[0][1]);
                medicationDiscontinueReason.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("DURATION OF DISCONTINUATION IN DAYS")) {
                medicationDiscontinueDuration.getEditText().setText(obs[0][1]);
                medicationDiscontinueDuration.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("NEW MEDICATION")) {
                newMedication.getEditText().setText(obs[0][1]);
                newMedication.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("DURATION OF NEW MEDICATION IN DAYS")) {
                newMedicationDuration.getEditText().setText(obs[0][1]);
                newMedicationDuration.setVisibility(View.VISIBLE);
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
                    }

                }
                petRegimen.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("SEVERITY OF ADVERSE REACTION")) {
                for (RadioButton rb : severity.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_mild)) && obs[0][1].equals("MILD")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_moderate)) && obs[0][1].equals("MODERATE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_severe)) && obs[0][1].equals("SEVERE")) {
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
            } else if (obs[0][0].equals("ANCILLARY DRUGS")) {
                for (CheckBox cb : ancillaryDrugs.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_iron_deficiency_prtocol)) && obs[0][1].equals("IRON")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_vitamin_d_protocol)) && obs[0][1].equals("VITAMIN D")) {
                        cb.setChecked(true);
                        break;
                    }
                    if (cb.getText().equals(getResources().getString(R.string.pet_pcm_protocol)) && obs[0][1].equals("PHENYLEPHRINE, CHLORPHENIRAMINE, AND METHSCOPOLAMINE")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                ancillaryDrugs.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("MEDICATION DURATION")) {
                ancillaryDrugDuration.getEditText().setText(obs[0][1]);
                ancillaryDrugDuration.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("INSTRUCTIONS TO PATIENT AND/OR FAMILY")) {
                newInstruction.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
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

    public void calculateDosages(){

        isoniazidDose.getEditText().setHint("");
        rifapentineDose.getEditText().setHint("");
        levofloxacinDose.getEditText().setHint("");
        ethionamideDose.getEditText().setHint("");

        isoniazidDose.getEditText().setText("");
        rifapentineDose.getEditText().setText("");
        levofloxacinDose.getEditText().setText("");
        ethionamideDose.getEditText().setText("");

        int age = App.getPatient().getPerson().getAge();
        Double weightDouble = Double.parseDouble("0");
        if (!App.get(weight).equals("")) {
            weightDouble = Double.parseDouble(App.get(weight));
        }

        if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy))) {

            Double w = 1.0;

            if(age < 15) {
                w = weightDouble * 10f;
                int i = (int) Math.round(w);
                isoniazidDose.getEditText().setText(String.valueOf(i));
            } else {
                w = weightDouble * 5f;
                int i = (int) Math.round(w);
                isoniazidDose.getEditText().setText(String.valueOf(i));
            }

            isoniazidDose.setVisibility(View.VISIBLE);
            rifapentineDose.setVisibility(View.GONE);
            levofloxacinDose.setVisibility(View.GONE);
            ethionamideDose.setVisibility(View.GONE);
            rifapentineAvailable.setVisibility(View.GONE);

            if(w > 300)
                isoniazidDose.getEditText().setError(getString(R.string.pet_isoniazid_dose_exceeded_300));

        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine))) {

            Double w = 1.0;

            rifapentineAvailable.setVisibility(View.VISIBLE);
            if (App.get(rifapentineAvailable).equals(getResources().getString(R.string.no))) {

                if(age < 15) {
                    w = weightDouble * 10f;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));
                } else {
                    w = weightDouble * 5f;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));
                }

                isoniazidDose.setVisibility(View.VISIBLE);
                rifapentineDose.setVisibility(View.GONE);
                levofloxacinDose.setVisibility(View.GONE);
                ethionamideDose.setVisibility(View.GONE);

                if(w > 300)
                    isoniazidDose.getEditText().setError(getString(R.string.pet_isoniazid_dose_exceeded_300));

            } else {

                w = 1.0;


                if(age < 2){
                    w = weightDouble * 10f;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));

                    rifapentineDose.getEditText().setHint("Not recommended");
                } else if (age < 12){
                    w = weightDouble * 15;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));

                    rifapentineDose.getEditText().setHint("300 - 450 mg");
                } else {
                    w = weightDouble * 25;
                    int i = (int) Math.round(w);
                    isoniazidDose.getEditText().setText(String.valueOf(i));

                    rifapentineDose.getEditText().setHint("450 - 900 mg");
                }

                isoniazidDose.setVisibility(View.VISIBLE);
                rifapentineDose.setVisibility(View.VISIBLE);
                levofloxacinDose.setVisibility(View.GONE);
                ethionamideDose.setVisibility(View.GONE);

                if(w > 2000)
                    isoniazidDose.getEditText().setError(getString(R.string.pet_dose_exceeded_2000));
            }

        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethionamide))) {

            Double w = 1.0;

            if (age < 15) {
                ethionamideDose.getEditText().setText("");
                ethionamideDose.getEditText().setHint("15 - 20 mg/kg");
            } else {
                ethionamideDose.getEditText().setText("");
                ethionamideDose.getEditText().setHint("500 - 1000 mg");
            }

            Double ww = 1.0;
            if (age < 2) {
                ww = weightDouble * 15f;
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

        }


    }

}
