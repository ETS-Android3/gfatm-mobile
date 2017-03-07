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

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
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
    TitledEditText weight;
    TitledEditText indexPatientId;
    TitledEditText tbType;
    TitledEditText infectionType;
    TitledEditText resistanceType;
    TitledEditText dstPattern;

    TitledButton treatmentInitiationDate;
    TitledRadioGroup petRegimen;
    TitledRadioGroup rifapentineAvailable;
    TitledEditText isoniazidDose;
    TitledEditText rifapentineDose;
    TitledEditText levofloxacinDose;
    TitledEditText ethionamideDose;

    TitledRadioGroup ancillaryNeed;
    TitledCheckBoxes ancillaryDrugs;
    TitledEditText ancillaryDrugDuration;

    TitledEditText nameTreatmentSupporter;
    LinearLayout contactNumberTreatmentSupporter;
    TitledEditText phone1a;
    TitledEditText phone1b;
    TitledRadioGroup typeTreatmentSupporter;
    TitledSpinner relationshipTreatmentSuppoter;
    TitledEditText other;

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

    /**
     * Initializes all views and ArrayList and Views Array
     */
    public void initViews() {

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        weight = new TitledEditText(context, null, getResources().getString(R.string.pet_weight), "", "", 5, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        indexPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_id), "", "", RegexUtil.idLength, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        tbType = new TitledEditText(context, null, getResources().getString(R.string.pet_tb_type), "", "", 100, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        tbType.getEditText().setKeyListener(null);
        infectionType = new TitledEditText(context, null, getResources().getString(R.string.pet_infection_type), "", "", 100, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        infectionType.getEditText().setKeyListener(null);
        resistanceType = new TitledEditText(context, null, getResources().getString(R.string.pet_resistance_Type), "", "", 100, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        resistanceType.getEditText().setKeyListener(null);
        dstPattern = new TitledEditText(context, null, getResources().getString(R.string.pet_dst_pattern), "", "", 100, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        dstPattern.getEditText().setKeyListener(null);

        // second page view
        treatmentInitiationDate = new TitledButton(context, null, getResources().getString(R.string.pet_treatment_initiation_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.VERTICAL);
        petRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_regimen), getResources().getStringArray(R.array.pet_regimens), "", App.VERTICAL, App.VERTICAL);
        isoniazidDose = new TitledEditText(context, null, getResources().getString(R.string.pet_isoniazid_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        rifapentineAvailable = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_rifapentine_available), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        rifapentineDose = new TitledEditText(context, null, getResources().getString(R.string.pet_rifapentine_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        levofloxacinDose = new TitledEditText(context, null, getResources().getString(R.string.pet_levofloxacin_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        ethionamideDose = new TitledEditText(context, null, getResources().getString(R.string.pet_ethionamide_dose), "", "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);

        // third page view
        ancillaryNeed = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_ancillary_drugs_needed), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        ancillaryDrugs = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_ancillary_drugs), getResources().getStringArray(R.array.pet_ancillary_drugs), null, App.VERTICAL, App.VERTICAL, true);
        ancillaryDrugDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_ancillary_drug_duration_days), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);

        // fourth page view
        nameTreatmentSupporter = new TitledEditText(context, null, getResources().getString(R.string.pet_treatment_supporter_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        contactNumberTreatmentSupporter = new LinearLayout(context);
        contactNumberTreatmentSupporter.setOrientation(LinearLayout.HORIZONTAL);
        phone1a = new TitledEditText(context, null, getResources().getString(R.string.pet_treatment_supporter_contact_number), "", "XXXX", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        contactNumberTreatmentSupporter.addView(phone1a);
        phone1b = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        contactNumberTreatmentSupporter.addView(phone1b);
        typeTreatmentSupporter = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_treatment_supporter_type), getResources().getStringArray(R.array.pet_treatment_supporter_type), getResources().getString(R.string.pet_family_treatment_supporter), App.VERTICAL, App.VERTICAL);
        relationshipTreatmentSuppoter = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_treatment_supporter_relationship), getResources().getStringArray(R.array.pet_household_heads), "", App.VERTICAL);
        other = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), weight.getEditText(), indexPatientId.getEditText(), tbType.getEditText(), infectionType.getEditText(), dstPattern.getEditText(), resistanceType.getEditText(),
                treatmentInitiationDate.getButton(), petRegimen.getRadioGroup(), isoniazidDose.getEditText(), rifapentineDose.getEditText(), levofloxacinDose.getEditText(), ethionamideDose.getEditText(),
                ancillaryNeed.getRadioGroup(), ancillaryDrugs, ancillaryDrugDuration.getEditText(), ancillaryDrugDuration.getEditText(),
                nameTreatmentSupporter.getEditText(), phone1a.getEditText(), phone1b.getEditText(), typeTreatmentSupporter.getRadioGroup(), relationshipTreatmentSuppoter.getSpinner(), other.getEditText(), rifapentineAvailable.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, weight, indexPatientId, tbType, infectionType, resistanceType, dstPattern},
                        {treatmentInitiationDate, petRegimen, rifapentineAvailable, isoniazidDose, rifapentineDose, levofloxacinDose, ethionamideDose, ancillaryNeed, ancillaryDrugs, ancillaryDrugDuration, nameTreatmentSupporter, contactNumberTreatmentSupporter, typeTreatmentSupporter, relationshipTreatmentSuppoter, other},
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

                        rifapentineDose.getEditText().setText("");
                        if (age < 12)
                            rifapentineDose.getEditText().setHint("300 - 450 mg");
                        else
                            rifapentineDose.getEditText().setHint("450 - 900 mg");

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
                    int dose = Integer.parseInt(App.get(isoniazidDose));
                    if (dose > 300)
                        isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded));
                    else
                        isoniazidDose.getEditText().setError(null);
                }
            }
        });

        resetViews();
    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        isoniazidDose.setVisibility(View.GONE);
        rifapentineDose.setVisibility(View.GONE);
        levofloxacinDose.setVisibility(View.GONE);
        ethionamideDose.setVisibility(View.GONE);
        ancillaryDrugs.setVisibility(View.GONE);
        ancillaryDrugDuration.setVisibility(View.GONE);
        rifapentineAvailable.setVisibility(View.GONE);
        relationshipTreatmentSuppoter.setVisibility(View.VISIBLE);

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
                String weight = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_CLINICIAN_CONTACT_SCREENING, "WEIGHT (KG)");
                String indexId = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_BASELINE_SCREENING, "PATIENT ID OF INDEX CASE");

                String tbType = "";
                String infectionType = "";
                String resistanceType = "";
                String dstbType = "";

                if (!(indexId == null || indexId.equals(""))) {
                    String id = serverService.getPatientSystemIdByIdentifierLocalDB(indexId);
                    tbType = serverService.getObsValue(id, App.getProgram() + "-" + Forms.PET_INDEX_PATIENT_REGISTRATION, "SITE OF TUBERCULOSIS DISEASE");
                    infectionType = serverService.getObsValue(id, App.getProgram() + "-" + Forms.PET_INDEX_PATIENT_REGISTRATION, "TUBERCULOSIS INFECTION TYPE");
                    resistanceType = serverService.getObsValue(id, App.getProgram() + "-" + Forms.PET_INDEX_PATIENT_REGISTRATION, "TUBERCULOSIS DRUG RESISTANCE TYPE");
                    dstbType = serverService.getObsValue(id, App.getProgram() + "-" + Forms.PET_INDEX_PATIENT_REGISTRATION, "RESISTANT TO ANTI-TUBERCULOSIS DRUGS");
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

                weight.getEditText().setText(result.get("WEIGHT (KG)"));
                indexPatientId.getEditText().setText(result.get("PATIENT ID OF INDEX CASE"));
                tbType.getEditText().setText(result.get("SITE OF TUBERCULOSIS DISEASE"));
                infectionType.getEditText().setText(result.get("TUBERCULOSIS INFECTION TYPE"));
                resistanceType.getEditText().setText(result.get("TUBERCULOSIS DRUG RESISTANCE TYPE"));
                dstPattern.getEditText().setText(result.get("RESISTANT TO ANTI-TUBERCULOSIS DRUGS"));
                if (App.get(infectionType).equals(getResources().getString(R.string.pet_drtb))) {
                    resistanceType.setVisibility(View.VISIBLE);
                    dstPattern.setVisibility(View.VISIBLE);

                    for (RadioButton rb : petRegimen.getRadioGroup().getButtons()) {

                        if (rb.getText().equals(getResources().getString(R.string.pet_levofloxacin_ethionamide)))
                            rb.setChecked(true);
                        else
                            rb.setChecked(false);

                    }

                } else {
                    resistanceType.setVisibility(View.GONE);
                    dstPattern.setVisibility(View.GONE);

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

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }
        treatmentInitiationDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());

    }

    @Override
    public boolean validate() {

        Boolean error = false;
        View view = null;

        if (other.getVisibility() == View.VISIBLE) {
            if (App.get(other).isEmpty()) {
                other.getQuestionView().setError(getString(R.string.empty_field));
                other.getQuestionView().requestFocus();
                view = null;
                error = true;
            }
        }
        if (App.get(phone1a).isEmpty()) {
            phone1a.getEditText().setError(getResources().getString(R.string.mandatory_field));
            phone1a.getEditText().requestFocus();
            error = true;
        } else if (App.get(phone1b).isEmpty()) {
            phone1b.getEditText().setError(getResources().getString(R.string.mandatory_field));
            phone1b.getEditText().requestFocus();
            error = true;
        } else if (!RegexUtil.isContactNumber(App.get(phone1a) + App.get(phone1b))) {
            phone1b.getEditText().setError(getResources().getString(R.string.invalid_value));
            phone1b.getEditText().requestFocus();
            error = true;
        }
        if (App.get(nameTreatmentSupporter).isEmpty()) {
            nameTreatmentSupporter.getQuestionView().setError(getString(R.string.empty_field));
            nameTreatmentSupporter.getQuestionView().requestFocus();
            view = null;
            error = true;
        }
        if (ancillaryDrugDuration.getVisibility() == View.VISIBLE) {
            if (App.get(ancillaryDrugDuration).isEmpty()) {
                ancillaryDrugDuration.getQuestionView().setError(getString(R.string.empty_field));
                ancillaryDrugDuration.getQuestionView().requestFocus();
                view = null;
                error = true;
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
            }
        }
        if (ethionamideDose.getVisibility() == View.VISIBLE) {
            if (App.get(ethionamideDose).isEmpty()) {
                ethionamideDose.getQuestionView().setError(getString(R.string.empty_field));
                ethionamideDose.getQuestionView().requestFocus();
                view = null;
                error = true;
            }
        }
        if (levofloxacinDose.getVisibility() == View.VISIBLE) {
            if (App.get(levofloxacinDose).isEmpty()) {
                levofloxacinDose.getQuestionView().setError(getString(R.string.empty_field));
                levofloxacinDose.getQuestionView().requestFocus();
                view = null;
                error = true;
            }
        }
        if (rifapentineDose.getVisibility() == View.VISIBLE) {
            if (App.get(rifapentineDose).isEmpty()) {
                rifapentineDose.getQuestionView().setError(getString(R.string.empty_field));
                rifapentineDose.getQuestionView().requestFocus();
                view = null;
                error = true;
            } else {
                int dose = Integer.parseInt(App.get(rifapentineDose));
                String hint = rifapentineDose.getEditText().getHint().toString();

                if (hint.equals("300 - 450 mg")) {
                    if (dose < 300 || dose > 450) {
                        rifapentineDose.getEditText().setError(getResources().getString(R.string.pet_rifapentine_out_of_range_1));
                        rifapentineDose.getQuestionView().requestFocus();
                        view = null;
                        error = true;
                    }
                } else if (hint.equals("450 - 900 mg")) {
                    if (dose < 450 || dose > 900) {
                        rifapentineDose.getEditText().setError(getResources().getString(R.string.pet_rifapentine_out_of_range_2));
                        rifapentineDose.getQuestionView().requestFocus();
                        view = null;
                        error = true;
                    }
                }
            }
        }
        if (isoniazidDose.getVisibility() == View.VISIBLE) {
            if (App.get(isoniazidDose).isEmpty()) {
                isoniazidDose.getQuestionView().setError(getString(R.string.empty_field));
                isoniazidDose.getQuestionView().requestFocus();
                view = null;
                error = true;
            } else {
                int dose = Integer.parseInt(App.get(isoniazidDose));
                if (dose > 300) {
                    isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_isoniazid_dose_exceeded));
                    isoniazidDose.getQuestionView().requestFocus();
                    view = null;
                    error = true;
                }
            }
        }


        return true;
    }

    @Override
    public boolean submit() {

        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        /*observations.add (new String[] {"LONGITUDE (DEGREES)", String.valueOf(longitude)});
        observations.add (new String[] {"LATITUDE (DEGREES)", String.valueOf(latitude)});*/
        observations.add(new String[]{"WEIGHT (KG)", App.get(weight)});
        observations.add(new String[]{"SITE OF TUBERCULOSIS DISEASE", App.get(tbType).equals(getResources().getString(R.string.pet_ptb)) ? "PULMONARY TUBERCULOSIS" : "EXTRA-PULMONARY TUBERCULOSIS"});
        observations.add(new String[]{"TUBERCULOSIS INFECTION TYPE", App.get(infectionType).equals(getResources().getString(R.string.pet_dstb)) ? "DRUG-SENSITIVE TUBERCULOSIS INFECTION" : "DRUG-RESISTANT TUBERCULOSIS INFECTION"});
        if (resistanceType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOSIS DRUG RESISTANCE TYPE", App.get(resistanceType).equals(getResources().getString(R.string.pet_rr_tb)) ? "RIFAMPICIN RESISTANT TUBERCULOSIS INFECTION" :
                    (App.get(resistanceType).equals(getResources().getString(R.string.pet_dr_tb)) ? "MONO DRUG RESISTANT TUBERCULOSIS" :
                            (App.get(resistanceType).equals(getResources().getString(R.string.pet_pdr_tb)) ? "PANDRUG RESISTANT TUBERCULOSIS" :
                                    (App.get(resistanceType).equals(getResources().getString(R.string.pet_mdr_tb))) ? "MULTI-DRUG RESISTANT TUBERCULOSIS INFECTION" : "EXTREMELY DRUG-RESISTANT TUBERCULOSIS INFECTION"))});
        if (dstPattern.getVisibility() == View.VISIBLE) {
            String dstPatternString = App.get(dstPattern).replace(", ", " ; ");
            observations.add(new String[]{"RESISTANT TO ANTI-TUBERCULOSIS DRUGS", dstPatternString});
        }
        observations.add(new String[]{"TREATMENT START DATE", App.getSqlDate(secondDateCalendar)});
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
        observations.add(new String[]{"TREATMENT SUPPORTER CONTACT NUMBER", App.get(phone1a) + App.get(phone1b)});
        observations.add(new String[]{"TREATMENT SUPPORTER TYPE", App.get(typeTreatmentSupporter).equals(getResources().getString(R.string.pet_family_treatment_supporter)) ? "FAMILY MEMBER" : "NON-FAMILY MEMBER"});
        if (relationshipTreatmentSuppoter.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT SUPPORTER RELATIONSHIP TO PATIENT", (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_mother))) ? "MOTHER" :
                    (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_father)) ? "FATHER" :
                            (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                    (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                            (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                    (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                            (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_brother)) ? "BROTHER" :
                                                                    (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_sister)) ? "SISTER" :
                                                                            (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_son)) ? "SON" :
                                                                                    (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_daughter)) ? "SPOUSE" :
                                                                                            (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_aunt)) ? "AUNT" :
                                                                                                    (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_uncle)) ? "UNCLE" : "OTHER FAMILY MEMBER")))))))))))});
        if (other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FAMILY MEMBER", App.get(other)});

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

                String result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}));
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
        } else if (view == treatmentInitiationDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
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

                    rifapentineDose.getEditText().setText("");
                    if (age < 12)
                        rifapentineDose.getEditText().setHint("300 - 450 mg");
                    else
                        rifapentineDose.getEditText().setHint("450 - 900 mg");

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

                rifapentineDose.getEditText().setText("");
                if (age < 12)
                    rifapentineDose.getEditText().setHint("300 - 450 mg");
                else
                    rifapentineDose.getEditText().setHint("450 - 900 mg");

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
