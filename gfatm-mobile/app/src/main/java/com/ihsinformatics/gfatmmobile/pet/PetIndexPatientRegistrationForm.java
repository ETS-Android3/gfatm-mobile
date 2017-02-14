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

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
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

public class PetIndexPatientRegistrationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledEditText husbandName;
    TitledEditText indexExternalPatientId;
    TitledEditText ernsNumber;
    TitledRadioGroup tbType;
    TitledRadioGroup infectionType;
    TitledRadioGroup dstAvailable;
    TitledRadioGroup resistanceType;
    TitledSpinner patientType;
    TitledCheckBoxes dstPattern;
    LinearLayout regimenlinearLayout;
    TitledCheckBoxes treatmentRegimen1;
    TitledCheckBoxes treatmentRegimen2;
    TitledButton treatmentEnrollmentDate;

    Snackbar snackbar;
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

        PAGE_COUNT = 1;
        FORM_NAME = Forms.PET_INDEX_PATIENT_REGISTRATION;
        FORM = Forms.pet_indexPatientRegistration;

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
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(context);
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        } else {
            for (int i = 0; i < PAGE_COUNT; i++) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(context);
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
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
        formDate.setTag("formDate");
        husbandName = new TitledEditText(context, null, getResources().getString(R.string.pet_father_husband_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        husbandName.setTag("husbandName");
        husbandName.setFocusableInTouchMode(true);
        indexExternalPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_external_id), "", "", 20, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        indexExternalPatientId.setFocusableInTouchMode(true);
        ernsNumber = new TitledEditText(context, null, getResources().getString(R.string.pet_erns_number), "", "", RegexUtil.idLength, RegexUtil.ERNS_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        ernsNumber.setFocusableInTouchMode(true);
        tbType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_tb_type), getResources().getStringArray(R.array.pet_tb_types), getResources().getString(R.string.pet_ptb), App.HORIZONTAL, App.VERTICAL);
        infectionType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_infection_type), getResources().getStringArray(R.array.pet_infection_types), getResources().getString(R.string.pet_dstb), App.HORIZONTAL, App.VERTICAL);
        dstAvailable = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_dst_available), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        resistanceType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_resistance_Type), getResources().getStringArray(R.array.pet_resistance_types), getResources().getString(R.string.pet_rr_tb), App.VERTICAL, App.VERTICAL);
        patientType = new TitledSpinner(context, "", getResources().getString(R.string.pet_patient_Type), getResources().getStringArray(R.array.pet_patient_types), getResources().getString(R.string.pet_new), App.VERTICAL);
        dstPattern = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_dst_pattern), getResources().getStringArray(R.array.pet_dst_patterns), null, App.VERTICAL, App.VERTICAL, true);
        regimenlinearLayout = new LinearLayout(context);
        regimenlinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        treatmentRegimen1 = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_treatment_regimen), getResources().getStringArray(R.array.pet_treatment_regimens_1), null, App.VERTICAL, App.VERTICAL, true);
        regimenlinearLayout.addView(treatmentRegimen1);
        treatmentRegimen2 = new TitledCheckBoxes(context, null, "", getResources().getStringArray(R.array.pet_treatment_regimens_2), null, App.VERTICAL, App.VERTICAL);
        regimenlinearLayout.addView(treatmentRegimen2);
        treatmentEnrollmentDate = new TitledButton(context, null, getResources().getString(R.string.pet_treatment_enrollment), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.VERTICAL);

        views = new View[]{formDate.getButton(), husbandName.getEditText(), indexExternalPatientId.getEditText(), ernsNumber.getEditText(),
                tbType.getRadioGroup(), infectionType.getRadioGroup(), dstAvailable.getRadioGroup(), resistanceType.getRadioGroup(),
                patientType.getSpinner(), dstPattern, treatmentRegimen1, treatmentRegimen2};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, husbandName, indexExternalPatientId, ernsNumber, tbType, infectionType, dstAvailable, resistanceType,
                        patientType, dstPattern, regimenlinearLayout, treatmentEnrollmentDate}};

        formDate.getButton().setOnClickListener(this);
        treatmentEnrollmentDate.getButton().setOnClickListener(this);
        dstAvailable.getRadioGroup().setOnCheckedChangeListener(this);
        infectionType.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : treatmentRegimen1.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : treatmentRegimen2.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : dstPattern.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        resetViews();

    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();

        if (!formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString())) {

            Date date = App.stringToDate(formDate.getButton().getText().toString(), "dd-MMM-yyyy");

            if (formDateCalendar.after(date)) {

                formDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }

        if (secondDateCalendar.after(formDateCalendar)) {
            treatmentEnrollmentDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            Date date = App.stringToDate(formDate.getButton().getText().toString(), "dd-MMM-yyyy");
            secondDateCalendar = App.getCalendar(date);

        }

        if (!treatmentEnrollmentDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString())) {

            Date date = App.stringToDate(formDate.getButton().getText().toString(), "dd-MMM-yyyy");

            if (secondDateCalendar.after(date)) {

                Date secondDate = App.stringToDate(treatmentEnrollmentDate.getButton().getText().toString(), "dd-MMM-yyyy");
                secondDateCalendar = App.getCalendar(secondDate);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.pet_enrollment_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                treatmentEnrollmentDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());

        }

    }

    @Override
    public boolean validate() {

        Boolean error = false;
        View view = null;

        Boolean flag = false;
        for (CheckBox cb : treatmentRegimen1.getCheckedBoxes()) {
            if (cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            for (CheckBox cb : treatmentRegimen2.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
        }
        if (!flag) {
            treatmentRegimen1.getQuestionView().setError(getString(R.string.empty_field));
            treatmentRegimen1.getQuestionView().requestFocus();
            view = regimenlinearLayout;
            error = true;
        }


        flag = false;
        if (dstPattern.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : dstPattern.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                dstPattern.getQuestionView().setError(getString(R.string.empty_field));
                dstPattern.getQuestionView().requestFocus();
                view = dstPattern;
                error = true;
            }
        }

        if (App.get(ernsNumber).isEmpty()) {
            ernsNumber.getEditText().setError(getString(R.string.empty_field));
            ernsNumber.getEditText().requestFocus();
            view = null;
            error = true;
        } else if (!RegexUtil.isValidErnsNumber(App.get(ernsNumber))) {
            ernsNumber.getEditText().setError(getString(R.string.invalid_value));
            ernsNumber.getEditText().requestFocus();
            view = null;
            error = true;
        }
            ernsNumber.clearFocus();

        if (App.get(husbandName).isEmpty()) {
            husbandName.getEditText().setError(getString(R.string.empty_field));
            husbandName.getEditText().requestFocus();
            view = null;
            error = true;
        } else if (App.get(husbandName).length() <= 2) {
            husbandName.getEditText().setError(getString(R.string.invalid_value));
            husbandName.getEditText().requestFocus();
            error = true;
            view = null;
        } else husbandName.clearFocus();

        indexExternalPatientId.clearFocus();

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
                                        husbandName.clearFocus();
                                        indexExternalPatientId.clearFocus();
                                        ernsNumber.clearFocus();
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

        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        /*observations.add (new String[] {"LONGITUDE (DEGREES)", String.valueOf(longitude)});
        observations.add (new String[] {"LATITUDE (DEGREES)", String.valueOf(latitude)});*/
        observations.add(new String[]{"SITE OF TUBERCULOSIS DISEASE", App.get(tbType).equals(getResources().getString(R.string.pet_ptb)) ? "PULMONARY TUBERCULOSIS" : "EXTRA-PULMONARY TUBERCULOSIS"});
        observations.add(new String[]{"TUBERCULOSIS INFECTION TYPE", App.get(infectionType).equals(getResources().getString(R.string.pet_dstb)) ? "DRUG-SENSITIVE TUBERCULOSIS INFECTION" : "DRUG-RESISTANT TUBERCULOSIS INFECTION"});
        if (dstAvailable.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DST RESULT AVAILABLE", App.get(dstAvailable).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (resistanceType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOSIS DRUG RESISTANCE TYPE", App.get(resistanceType).equals(getResources().getString(R.string.pet_rr_tb)) ? "RIFAMPICIN RESISTANT TUBERCULOSIS INFECTION" :
                (App.get(resistanceType).equals(getResources().getString(R.string.pet_dr_tb)) ? "MONO DRUG RESISTANT TUBERCULOSIS" :
                        (App.get(resistanceType).equals(getResources().getString(R.string.pet_pdr_tb)) ? "PANDRUG RESISTANT TUBERCULOSIS" :
                                (App.get(resistanceType).equals(getResources().getString(R.string.pet_mdr_tb))) ? "MULTI-DRUG RESISTANT TUBERCULOSIS INFECTION" : "EXTREMELY DRUG-RESISTANT TUBERCULOSIS INFECTION"))});
        observations.add(new String[]{"TB PATIENT TYPE", App.get(patientType).equals(getResources().getString(R.string.pet_new)) ? "NEW TB PATIENT" :
                (App.get(patientType).equals(getResources().getString(R.string.pet_relapse)) ? "RELAPSE" :
                        (App.get(patientType).equals(getResources().getString(R.string.pet_cat1)) ? "FAILURE OF CATEGORY I TREATMENT" :
                                (App.get(patientType).equals(getResources().getString(R.string.pet_cat2))) ? "FAILURE OF CATEGORY II TREATMENT" :
                                        (App.get(patientType).equals(getResources().getString(R.string.pet_mdr))) ? "FAILURE OF MDR-TB TREATMENT" :
                                                (App.get(patientType).equals(getResources().getString(R.string.pet_treatment_failure))) ? "FAILURE OF PREVIOUS TREATMENT" :
                                                        (App.get(patientType).equals(getResources().getString(R.string.pet_loss_of_followup_type))) ? "LOST TO FOLLOW-UP" :
                                                                (App.get(patientType).equals(getResources().getString(R.string.unknown))) ? "UNKNOWN" : "OTHER"))});
        if (dstPattern.getVisibility() == View.VISIBLE) {
            String dstPatternString = "";
            for(CheckBox cb : dstPattern.getCheckedBoxes()){
                if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_isoniazid)))
                    dstPatternString = dstPatternString + "ISONIAZID" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_rifampicin)))
                    dstPatternString = dstPatternString + "RIFAMPICIN" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_amikacin)))
                    dstPatternString = dstPatternString + "AMIKACIN" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_capreomycin)))
                    dstPatternString = dstPatternString + "CAPREOMYCIN" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_streptpmycin)))
                    dstPatternString = dstPatternString + "STREPTOMYCIN" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_ofloxacin)))
                    dstPatternString = dstPatternString + "OFLOXACIN" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_moxifloxacin)))
                    dstPatternString = dstPatternString + "MOXIFLOXACIN" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_ethambutol)))
                    dstPatternString = dstPatternString + "ETHAMBUTOL" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_ethionamide)))
                    dstPatternString = dstPatternString + "ETHIONAMIDE" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_pyrazinamide)))
                    dstPatternString = dstPatternString + "PYRAZINAMIDE" + " ; ";
            }
            observations.add(new String[]{"RESISTANT TO ANTI-TUBERCULOSIS DRUGS", dstPatternString});
        }
        String treatmentRegimenString = "";
        for(CheckBox cb : treatmentRegimen1.getCheckedBoxes()){
            if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_amikacin)))
                treatmentRegimenString = treatmentRegimenString + "AMIKACIN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_bedaquiline)))
                treatmentRegimenString = treatmentRegimenString + "BEDAQUILINE" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_clofazimine)))
                treatmentRegimenString = treatmentRegimenString + "CLOFAZIMINE" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_delamanid)))
                treatmentRegimenString = treatmentRegimenString + "DELAMANID" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_ethionamide)))
                treatmentRegimenString = treatmentRegimenString + "ETHIONAMIDE" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_high_dosed_isoniazid)))
                treatmentRegimenString = treatmentRegimenString + "HIGH DOSE ISONIAZID" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_isoniazid)))
                treatmentRegimenString = treatmentRegimenString + "ISONIAZID" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_levofloxacin)))
                treatmentRegimenString = treatmentRegimenString + "LEVOFLOXACIN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_meropenem)))
                treatmentRegimenString = treatmentRegimenString + "MEROPENEM" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_p_aminosalicylic_acid)))
                treatmentRegimenString = treatmentRegimenString + "P-AMINOSALICYLIC ACID MONOSODIUM" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_pyrazinamide)))
                treatmentRegimenString = treatmentRegimenString + "PYRAZINAMIDE" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_streptpmycin)))
                treatmentRegimenString = treatmentRegimenString + "STREPTOMYCIN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_thioacetazone)))
                treatmentRegimenString = treatmentRegimenString + "THIOACETAZONE" + " ; ";
        }
        for(CheckBox cb : treatmentRegimen2.getCheckedBoxes()){
            if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_capreomycin)))
                treatmentRegimenString = treatmentRegimenString + "CAPREOMYCIN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_cycloserine)))
                treatmentRegimenString = treatmentRegimenString + "CYCLOSERINE" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_ethambutol)))
                treatmentRegimenString = treatmentRegimenString + "ETHAMBUTOL" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_gatifloxacin)))
                treatmentRegimenString = treatmentRegimenString + "GATIFLOXACIN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_ethionamide)))
                treatmentRegimenString = treatmentRegimenString + "ETHIONAMIDE" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_imipenem_cilastatin)))
                treatmentRegimenString = treatmentRegimenString + "IMIPENEM AND CILASTATIN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_kanamycin)))
                treatmentRegimenString = treatmentRegimenString + "KANAMYCIN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_linezolid)))
                treatmentRegimenString = treatmentRegimenString + "LINEZOLID" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_moxifloxacin)))
                treatmentRegimenString = treatmentRegimenString + "MOXIFLOXACIN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_prothionamide)))
                treatmentRegimenString = treatmentRegimenString + "PROTHIONAMIDE" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_rifampicin)))
                treatmentRegimenString = treatmentRegimenString + "RIFAMPICIN" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_terizidone)))
                treatmentRegimenString = treatmentRegimenString + "TERIZIDONE" + " ; ";
        }
        observations.add(new String[]{"TUBERCULOSIS DRUGS", treatmentRegimenString});

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

                String result = "";
                result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}));
                if (!result.contains("SUCCESS"))
                    return result;
                else {

                    String encounterId = "";

                    if (result.contains("_")) {
                        String[] successArray = result.split("_");
                        encounterId = successArray[1];
                    }

                    if (App.hasKeyListener(ernsNumber)) {
                        result = serverService.saveIdentifier("ENRS", App.get(ernsNumber), encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }

                    if (!App.get(indexExternalPatientId).isEmpty() && App.hasKeyListener(indexExternalPatientId)) {
                        result = serverService.saveIdentifier("External ID", App.get(indexExternalPatientId), encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }

                    if (App.hasKeyListener(husbandName)) {
                        result = serverService.savePersonAttributeType("Guardian Name", App.get(husbandName), encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }
                }

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
        formValues.put(husbandName.getTag(), App.get(husbandName));

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
        } else if (view == treatmentEnrollmentDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowFutureDate", false);
            args.putBoolean("allowPastDate", true);
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

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        treatmentEnrollmentDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        ArrayList<RadioButton> rbs = resistanceType.getRadioGroup().getButtons();

        for (RadioButton rb : rbs) {
            if (rb.getText().equals(getResources().getString(R.string.pet_rr_tb)))
                rb.setVisibility(View.VISIBLE);
            else {
                rb.setChecked(false);
                rb.setVisibility(View.GONE);
            }
        }
        dstPattern.setVisibility(View.GONE);
        dstAvailable.setVisibility(View.GONE);
        resistanceType.setVisibility(View.GONE);

        husbandName.getEditText().requestFocus();

        final AsyncTask<String, String, String> autopopulateFormTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
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

                String husbandNameString = serverService.getPersonAttribute("Guardian Name");
                return husbandNameString;

            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            ;

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();

                String enrsId = App.getPatient().getEnrs();
                if (enrsId.equals("")) {
                    ernsNumber.getEditText().setText("");
                } else {
                    ernsNumber.getEditText().setText(enrsId);
                    ernsNumber.getEditText().setKeyListener(null);
                }

                String externalId = App.getPatient().getExternalId();
                if (externalId.equals("")) {
                    indexExternalPatientId.getEditText().setText("");
                } else {
                    indexExternalPatientId.getEditText().setText(externalId);
                    indexExternalPatientId.getEditText().setKeyListener(null);
                }

                if (result.equals("")) {
                    husbandName.getEditText().setText("");
                } else {
                    husbandName.getEditText().setText(result);
                    husbandName.getEditText().setKeyListener(null);
                }
            }
        };
        autopopulateFormTask.execute("");

    }

    /*@Override
    public void onPageSelected(int pageNo) {

        if (!App.isLanguageRTL()) {
            if (pageNo == 3 && dstAvailable.getRadioGroup().getSelectedValue().equalsIgnoreCase(getString(R.string.no))) {

                if (getCurrentPageNo() == 3)
                    pageNo = 4;
                else if (getCurrentPageNo() == 5)
                    pageNo = 2;
            }
        } else {
            if (pageNo == 2 && dstAvailable.getRadioGroup().getSelectedValue().equalsIgnoreCase(getString(R.string.no))) {

                if (getCurrentPageNo() == 3)
                    pageNo = 1;
                else if (getCurrentPageNo() == 5)
                    pageNo = 3;
            }
        }


        gotoPage(pageNo);

    }*/

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (group == infectionType.getRadioGroup()) {

            ArrayList<RadioButton> rbs = resistanceType.getRadioGroup().getButtons();
            String value = App.get(infectionType);
            if (value.equals(getResources().getString(R.string.pet_dstb))) {
                dstAvailable.setVisibility(View.GONE);
                resistanceType.setVisibility(View.GONE);
                dstPattern.setVisibility(View.GONE);
            } else {

                dstAvailable.setVisibility(View.VISIBLE);
                value = App.get(dstAvailable);
                resistanceType.setVisibility(View.VISIBLE);
                if (value.equals(getResources().getString(R.string.no))) {

                    for (RadioButton rb : rbs) {

                        if (rb.getText().equals(getResources().getString(R.string.pet_rr_tb))) {
                            rb.setVisibility(View.VISIBLE);
                            rb.setChecked(true);
                        } else {
                            rb.setChecked(false);
                            rb.setVisibility(View.GONE);
                        }

                    }

                    dstPattern.setVisibility(View.GONE);

                } else {

                    for (RadioButton rb : rbs) {

                        rb.setVisibility(View.VISIBLE);

                    }

                    dstPattern.setVisibility(View.VISIBLE);

                }

            }

        } else if (group == dstAvailable.getRadioGroup()) {

            ArrayList<RadioButton> rbs = resistanceType.getRadioGroup().getButtons();
            String value = App.get(dstAvailable);
            if (value.equals(getResources().getString(R.string.no))) {

                for (RadioButton rb : rbs) {

                    if (rb.getText().equals(getResources().getString(R.string.pet_rr_tb))) {
                        rb.setVisibility(View.VISIBLE);
                        rb.setChecked(true);
                    }
                    else {
                        rb.setChecked(false);
                        rb.setVisibility(View.GONE);
                    }

                }

                dstPattern.setVisibility(View.GONE);

            } else {

                for (RadioButton rb : rbs) {

                    rb.setVisibility(View.VISIBLE);

                }

                dstPattern.setVisibility(View.VISIBLE);

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
