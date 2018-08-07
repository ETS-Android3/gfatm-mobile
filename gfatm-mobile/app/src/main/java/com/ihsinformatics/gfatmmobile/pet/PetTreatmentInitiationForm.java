package com.ihsinformatics.gfatmmobile.pet;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class PetTreatmentInitiationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    public static final int THIRD_DATE_DIALOG_ID = 3;
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;

    // Views...
    TitledButton formDate;
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

    TitledEditText clincianNote;
    TitledButton returnVisitDate;

    Boolean refillFlag = false;

    ScrollView scrollView;

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

        pageCount = 2;
        formName = Forms.PET_TREATMENT_INITIATION;
        form = Forms.pet_treatmentInitiation;

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

        thirdDateCalendar = Calendar.getInstance();
        thirdDateFragment = new PetTreatmentInitiationForm.SelectDateFragment();

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        weight = new TitledEditText(context, null, getResources().getString(R.string.pet_weight), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
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

        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.pet_doctor_notes), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        clincianNote.getEditText().setSingleLine(false);
        clincianNote.getEditText().setMinimumHeight(150);

        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.pet_return_visit_date), DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), weight.getEditText(), indexPatientId.getEditText(), tbType.getRadioGroup(), infectionType.getRadioGroup(), dstPattern, resistanceType.getRadioGroup(),
                treatmentInitiationDate.getButton(), petRegimen.getRadioGroup(), isoniazidDose.getEditText(), rifapentineDose.getEditText(), levofloxacinDose.getEditText(), ethionamideDose.getEditText(), ethambutolDose.getEditText(), moxifloxacilinDose.getEditText(),
                clincianNote.getEditText(), rifapentineAvailable.getRadioGroup(),returnVisitDate.getButton()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, weight, indexPatientId, tbType, infectionType, resistanceType, dstPattern},
                        {treatmentInitiationDate, petRegimen, rifapentineAvailable, isoniazidDose, rifapentineDose, levofloxacinDose, ethionamideDose, ethambutolDose, moxifloxacilinDose, clincianNote, returnVisitDate},
                };

        formDate.getButton().setOnClickListener(this);
        treatmentInitiationDate.getButton().setOnClickListener(this);
        petRegimen.getRadioGroup().setOnCheckedChangeListener(this);
        rifapentineAvailable.getRadioGroup().setOnCheckedChangeListener(this);
        returnVisitDate.getButton().setOnClickListener(this);

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

        ethambutolDose.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!App.get(ethambutolDose).equals("")) {
                    int dose = Integer.parseInt(App.get(ethambutolDose));
                    if (dose > 2000)
                        ethambutolDose.getEditText().setError(getResources().getString(R.string.pet_dose_exceeded_2000));
                    else
                        ethambutolDose.getEditText().setError(null);
                }
            }
        });

        moxifloxacilinDose.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!App.get(moxifloxacilinDose).equals("")) {
                    int dose = Integer.parseInt(App.get(moxifloxacilinDose));
                    if (dose > 2000)
                        moxifloxacilinDose.getEditText().setError(getResources().getString(R.string.pet_dose_exceeded_2000));
                    else
                        moxifloxacilinDose.getEditText().setError(null);
                }
            }
        });

        resetViews();
    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        treatmentInitiationDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());

        resistanceType.setVisibility(View.GONE);
        dstPattern.setVisibility(View.GONE);
        isoniazidDose.setVisibility(View.GONE);
        rifapentineDose.setVisibility(View.GONE);
        levofloxacinDose.setVisibility(View.GONE);
        ethionamideDose.setVisibility(View.GONE);
        ethambutolDose.setVisibility(View.GONE);
        moxifloxacilinDose.setVisibility(View.GONE);
        rifapentineAvailable.setVisibility(View.GONE);

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
                    String weight = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_CONTACT_SCREENING, "WEIGHT (KG)");
                    String indexId = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_BASELINE_SCREENING, "PATIENT ID OF INDEX CASE");
                    String intervention = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_BASELINE_SCREENING, "INTERVENTION");

                    String tbType = "";
                    String infectionType = "";
                    String resistanceType = "";
                    String dstbType = "";

                    serverService.getPatient(indexId, false);

                    if (!(indexId == null || indexId.equals(""))) {
                        String id = serverService.getPatientSystemIdByIdentifierLocalDB(indexId);
                        tbType = serverService.getLatestObsValue(id, Forms.PET_INDEX_PATIENT_REGISTRATION, "SITE OF TUBERCULOSIS DISEASE");
                        infectionType = serverService.getLatestObsValue(id, Forms.PET_INDEX_PATIENT_REGISTRATION, "TUBERCULOSIS INFECTION TYPE");
                        resistanceType = serverService.getLatestObsValue(id, Forms.PET_INDEX_PATIENT_REGISTRATION, "TUBERCULOSIS DRUG RESISTANCE TYPE");
                        dstbType = serverService.getLatestObsValue(id, Forms.PET_INDEX_PATIENT_REGISTRATION, "RESISTANT TO ANTI-TUBERCULOSIS DRUGS");
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
        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            }
            else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }


        if (!(treatmentInitiationDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {
            String missedVisit = treatmentInitiationDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(App.stringToDate(missedVisit, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                treatmentInitiationDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(missedVisit, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                treatmentInitiationDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }
            else {
                treatmentInitiationDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                Calendar requiredDate = secondDateCalendar.getInstance();
                requiredDate.setTime(secondDateCalendar.getTime());
                requiredDate.add(Calendar.DATE, 30);

                if (requiredDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    thirdDateCalendar.setTime(requiredDate.getTime());
                } else {
                    requiredDate.add(Calendar.DATE, 1);
                    thirdDateCalendar.setTime(requiredDate.getTime());
                }
            }
        }


        String nextAppointmentDateString = App.getSqlDate(thirdDateCalendar);
        Date nextAppointmentDate = App.stringToDate(nextAppointmentDateString, "yyyy-MM-dd");

        String formDateString = App.getSqlDate(formDateCalendar);
        Date formStDate = App.stringToDate(formDateString, "yyyy-MM-dd");

        String missedVisitDateString = App.getSqlDate(secondDateCalendar);
        Date missedVisitDt = App.stringToDate(missedVisitDateString, "yyyy-MM-dd");


        if (!(returnVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString()))) {
            Calendar dateToday = Calendar.getInstance();
            dateToday.add(Calendar.MONTH, 24);

            String formDa = returnVisitDate.getButton().getText().toString();

            if (thirdDateCalendar.before(formDateCalendar)) {

                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());

            }
            else if(thirdDateCalendar.after(dateToday)){
                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_date_cant_be_greater_than_24_months), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
            }

            else if(nextAppointmentDate.compareTo(formStDate) == 0){
                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_start_date_and_next_visit_date_cant_be_same), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
            }
            else
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());

        }
        formDate.getButton().setEnabled(true);
        treatmentInitiationDate.getButton().setEnabled(true);
        returnVisitDate.getButton().setEnabled(true);
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

        if(moxifloxacilinDose.getVisibility() == View.VISIBLE) {
            if (App.get(moxifloxacilinDose).isEmpty()) {
                moxifloxacilinDose.getEditText().setError(getString(R.string.empty_field));
                moxifloxacilinDose.getEditText().requestFocus();
                gotoLastPage();
                view = null;
                error = true;
            } else {
                int dose = Integer.parseInt(App.get(moxifloxacilinDose));
                if (dose > 2000) {
                    moxifloxacilinDose.getEditText().setError(getResources().getString(R.string.pet_dose_exceeded_2000));
                    moxifloxacilinDose.getEditText().requestFocus();
                    gotoLastPage();
                } else {
                    moxifloxacilinDose.getEditText().clearFocus();
                    moxifloxacilinDose.getEditText().setError(null);
                }
            }
        }

        if(ethambutolDose.getVisibility() == View.VISIBLE) {
            if (App.get(ethambutolDose).isEmpty()) {
                ethambutolDose.getEditText().setError(getString(R.string.empty_field));
                ethambutolDose.getEditText().requestFocus();
                gotoLastPage();
                view = null;
                error = true;
            } else {
                int dose = Integer.parseInt(App.get(ethambutolDose));
                if (dose > 2000) {
                    ethambutolDose.getEditText().setError(getResources().getString(R.string.pet_dose_exceeded_2000));
                    ethambutolDose.getEditText().requestFocus();
                    gotoLastPage();
                } else {
                    ethambutolDose.getEditText().clearFocus();
                    ethambutolDose.getEditText().setError(null);
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
        if(levofloxacinDose.getVisibility() == View.VISIBLE) {
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
        if(rifapentineDose.getVisibility() == View.VISIBLE) {
            if (App.get(rifapentineDose).isEmpty()) {
                rifapentineDose.getEditText().setError(getString(R.string.empty_field));
                rifapentineDose.getEditText().requestFocus();
                gotoLastPage();
                view = null;
                error = true;
            } else {
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
        if (isoniazidDose.getVisibility() == View.VISIBLE) {
            if (App.get(isoniazidDose).isEmpty()) {
                isoniazidDose.getEditText().setError(getString(R.string.empty_field));
                isoniazidDose.getEditText().requestFocus();
                view = null;
                error = true;
                gotoLastPage();
            } else {
                Double dose = Double.parseDouble(App.get(isoniazidDose));
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
                        isoniazidDose.getEditText().setError(getResources().getString(R.string.pet_dose_exceeded_2000));
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

        observations.add(new String[]{"CLINICIAN NOTES (TEXT)", App.get(clincianNote)});

        personAttribute.put("Health Center",serverService.getLocationUuid(App.getLocation()));
        observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDate(thirdDateCalendar)});


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
        } else if (view == treatmentInitiationDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            args.putString("formDate", formDate.getButtonText());
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            treatmentInitiationDate.getButton().setEnabled(false);
        } else if (view == returnVisitDate.getButton()) {
            returnVisitDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            args.putBoolean("allowFutureDate", true);
            args.putBoolean("allowPastDate", false);
            args.putString("formDate", formDate.getButtonText());
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");
            returnVisitDate.getButton().setEnabled(false);
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
                ethambutolDose.setVisibility(View.GONE);
                moxifloxacilinDose.setVisibility(View.GONE);

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
                ethambutolDose.setVisibility(View.GONE);
                moxifloxacilinDose.setVisibility(View.GONE);

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

            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("WEIGHT (KG)")) {
                String weightValue = obs[0][1].replace(".0", "");
                weight.getEditText().setText(weightValue);
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
            } else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
                clincianNote.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String secondDate = obs[0][1];
                thirdDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
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

    @SuppressLint("ValidFragment")
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar;
            if (getArguments().getInt("type") == DATE_DIALOG_ID)
                calendar = formDateCalendar;
            else if (getArguments().getInt("type") == SECOND_DATE_DIALOG_ID)
                calendar = secondDateCalendar;
            else if (getArguments().getInt("type") == THIRD_DATE_DIALOG_ID)
                calendar = thirdDateCalendar;
            else
                return null;

            Calendar today = Calendar.getInstance();
            String d = getArguments().getString("formDate",null);
            if(d != null)
                today = App.getCalendar(App.stringToDate(d, "EEEE, MMM dd,yyyy"));

            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setTag(getArguments().getInt("type"));
            if (!getArguments().getBoolean("allowFutureDate", false)) {
                dialog.getDatePicker().setMaxDate(today.getTime().getTime());
            }
            if (!getArguments().getBoolean("allowPastDate", false))
                dialog.getDatePicker().setMinDate(today.getTime().getTime());
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            if (((int) view.getTag()) == DATE_DIALOG_ID)
                formDateCalendar.set(yy, mm, dd);
            else if (((int) view.getTag()) == SECOND_DATE_DIALOG_ID)
                secondDateCalendar.set(yy, mm, dd);
            else if (((int) view.getTag()) == THIRD_DATE_DIALOG_ID)
                thirdDateCalendar.set(yy, mm, dd);
            updateDisplay();
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            updateDisplay();
        }
    }


    public void calculateDosages(){

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
            ethambutolDose.setVisibility(View.GONE);
            moxifloxacilinDose.setVisibility(View.GONE);

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
                ethambutolDose.setVisibility(View.GONE);
                moxifloxacilinDose.setVisibility(View.GONE);

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
                ethambutolDose.setVisibility(View.GONE);
                moxifloxacilinDose.setVisibility(View.GONE);

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
            ethambutolDose.setVisibility(View.GONE);
            moxifloxacilinDose.setVisibility(View.GONE);

        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethambutol))) {

            if(weightDouble == 0){
                levofloxacinDose.getEditText().setText("");
                ethambutolDose.getEditText().setText("");
            } else {

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

                if(weightDouble <= 2){
                    ethambutolDose.getEditText().setText(String.valueOf(""));
                    ethambutolDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 7){
                    ethambutolDose.getEditText().setText(String.valueOf("100"));
                } else if (weightDouble <= 12){
                    ethambutolDose.getEditText().setText(String.valueOf("200"));
                } else if (weightDouble <= 15){
                    ethambutolDose.getEditText().setText(String.valueOf("300"));
                } else if (weightDouble <= 26){
                    ethambutolDose.getEditText().setText(String.valueOf("400"));
                } else if (weightDouble <= 30){
                    ethambutolDose.getEditText().setText(String.valueOf("500"));
                } else if (weightDouble <= 59){
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

            if(weightDouble == 0){
                levofloxacinDose.getEditText().setText("");
                moxifloxacilinDose.getEditText().setText("");
            } else {

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

                if(weightDouble <= 13){
                    moxifloxacilinDose.getEditText().setText(String.valueOf(""));
                    moxifloxacilinDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 30){
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

            if(weightDouble == 0){
                ethambutolDose.getEditText().setText("");
            } else {

                if(weightDouble <= 2){
                    ethambutolDose.getEditText().setText(String.valueOf(""));
                    ethambutolDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 7){
                    ethambutolDose.getEditText().setText(String.valueOf("100"));
                } else if (weightDouble <= 12){
                    ethambutolDose.getEditText().setText(String.valueOf("200"));
                } else if (weightDouble <= 15){
                    ethambutolDose.getEditText().setText(String.valueOf("300"));
                } else if (weightDouble <= 26){
                    ethambutolDose.getEditText().setText(String.valueOf("400"));
                } else if (weightDouble <= 30){
                    ethambutolDose.getEditText().setText(String.valueOf("500"));
                } else if (weightDouble <= 59){
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

            if(weightDouble == 0){
                moxifloxacilinDose.getEditText().setText("");
            } else {

                if(weightDouble <= 13){
                    moxifloxacilinDose.getEditText().setText(String.valueOf(""));
                    moxifloxacilinDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 30){
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

            if(weightDouble == 0){
                moxifloxacilinDose.getEditText().setText("");
                ethambutolDose.getEditText().setText("");
            } else {

                if(weightDouble <= 2){
                    ethambutolDose.getEditText().setText(String.valueOf(""));
                    ethambutolDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 7){
                    ethambutolDose.getEditText().setText(String.valueOf("100"));
                } else if (weightDouble <= 12){
                    ethambutolDose.getEditText().setText(String.valueOf("200"));
                } else if (weightDouble <= 15){
                    ethambutolDose.getEditText().setText(String.valueOf("300"));
                } else if (weightDouble <= 26){
                    ethambutolDose.getEditText().setText(String.valueOf("400"));
                } else if (weightDouble <= 30){
                    ethambutolDose.getEditText().setText(String.valueOf("500"));
                } else if (weightDouble <= 59){
                    ethambutolDose.getEditText().setText(String.valueOf("1500"));
                } else {
                    ethambutolDose.getEditText().setText(String.valueOf("2000"));
                }

                if(weightDouble <= 13){
                    moxifloxacilinDose.getEditText().setText(String.valueOf(""));
                    moxifloxacilinDose.getEditText().setHint("Not recommended");
                } else if (weightDouble <= 30){
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


}
