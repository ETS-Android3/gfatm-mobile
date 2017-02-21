package com.ihsinformatics.gfatmmobile.fast;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 2/15/2017.
 */

public class FastTreatmentInitiationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    public static final int THIRD_DATE_DIALOG_ID = 3;
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;
    Boolean dateChoose = false;
    Context context;

    // Views...
    TitledButton formDate;
    TitledButton regDate;
    LinearLayout cnicLinearLayout;
    TitledEditText cnic1;
    TitledEditText cnic2;
    TitledEditText cnic3;
    TitledSpinner cnicOwner;

    TitledEditText tbRegisterationNumber;
    TitledRadioGroup diagonosisType;
    TitledRadioGroup tbType;
    TitledSpinner extraPulmonarySite;
    TitledEditText extraPulmonarySiteOther;
    TitledSpinner patientType;
    TitledRadioGroup treatmentInitiated;
    TitledSpinner reasonTreatmentNotIniated;
    TitledEditText reasonTreatmentNotInitiatedOther;
    TitledRadioGroup tbCategory;
    TitledRadioGroup historyCategory;
    TitledSpinner outcomePreviousCategory;
    TitledEditText weight;
    TitledButton returnVisitDate;

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
        FORM_NAME = Forms.FAST_TREATMENT_INITIATION_FORM;
        FORM = Forms.fastTreatmentInitiationForm;

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
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
        thirdDateFragment = new SelectDateFragment();
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        regDate = new TitledButton(context, null, getResources().getString(R.string.fast_registeration_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        cnicLinearLayout = new LinearLayout(context);
        cnicLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        cnic1 = new TitledEditText(context, null, getResources().getString(R.string.fast_nic_number), "", "#####", 5, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLinearLayout.addView(cnic1);
        cnic2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLinearLayout.addView(cnic2);
        cnic3 = new TitledEditText(context, null, "-", "", "#", 1, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLinearLayout.addView(cnic3);
        cnicOwner = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_cnic), getResources().getStringArray(R.array.fast_whose_nic_is_this_list), getResources().getString(R.string.fast_self), App.VERTICAL);
        tbRegisterationNumber = new TitledEditText(context, null, getResources().getString(R.string.fast_tb_registeration_no), "", "", 11, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        diagonosisType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_type_of_diagnosis), getResources().getStringArray(R.array.fast_diagonosis_type_list), getResources().getString(R.string.fast_bactoriologically_confirmed), App.VERTICAL, App.VERTICAL);
        tbType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_what_type_of_tb), getResources().getStringArray(R.array.fast_tb_type_list), getResources().getString(R.string.fast_pulmonary), App.VERTICAL, App.VERTICAL);
        extraPulmonarySite = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_site_of_extra_pulmonary), getResources().getStringArray(R.array.fast_extra_pulmonary_site_list), getResources().getString(R.string.fast_lymph_node), App.VERTICAL);
        extraPulmonarySiteOther = new TitledEditText(context, null, getResources().getString(R.string.other_extra_pulmonary_tb_site), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        patientType = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_patient_type), getResources().getStringArray(R.array.fast_patient_type_list), getResources().getString(R.string.fast_new), App.VERTICAL);
        treatmentInitiated = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_was_treatment_initiated), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        reasonTreatmentNotIniated = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_reason_the_treatment_was_not_iniated), getResources().getStringArray(R.array.fast_reason_treatment_notinitiated_list), getResources().getString(R.string.fast_patient_refused_treatment), App.VERTICAL);
        reasonTreatmentNotInitiatedOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        tbCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_patient_category), getResources().getStringArray(R.array.fast_tb_category_list), getResources().getString(R.string.fast_category1), App.VERTICAL, App.VERTICAL);
        historyCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_if_category_2_history_of_previous), getResources().getStringArray(R.array.fast_history_category_2_list), getResources().getString(R.string.fast_cat_1), App.VERTICAL, App.VERTICAL);
        outcomePreviousCategory = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_if_category_2_outcome_of_previous), getResources().getStringArray(R.array.fast_outcome_previous_category_list), getResources().getString(R.string.fast_cured), App.VERTICAL);
        weight = new TitledEditText(context, null, getResources().getString(R.string.fast_patient_weight), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);
        thirdDateCalendar.set(Calendar.YEAR, secondDateCalendar.get(Calendar.YEAR));
        thirdDateCalendar.set(Calendar.DAY_OF_MONTH, secondDateCalendar.get(Calendar.DAY_OF_MONTH));
        thirdDateCalendar.set(Calendar.MONTH, secondDateCalendar.get(Calendar.MONTH));
        thirdDateCalendar.add(Calendar.DAY_OF_MONTH, 30);
        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.fast_next_appointment_date), DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), regDate.getButton(), cnic1.getEditText(), cnic2.getEditText(), cnic3.getEditText(),
                cnicOwner.getSpinner(), tbRegisterationNumber.getEditText(), diagonosisType.getRadioGroup(), tbType.getRadioGroup(),
                extraPulmonarySite.getSpinner(), extraPulmonarySiteOther.getEditText(), patientType.getSpinner(), treatmentInitiated.getRadioGroup(),
                reasonTreatmentNotIniated.getSpinner(), reasonTreatmentNotInitiatedOther.getEditText(), tbCategory.getRadioGroup(),
                historyCategory.getRadioGroup(), outcomePreviousCategory.getSpinner(), weight.getEditText(), returnVisitDate.getButton()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, regDate, cnicLinearLayout, cnicOwner, tbRegisterationNumber, diagonosisType, tbType, extraPulmonarySite,
                        extraPulmonarySiteOther, patientType, treatmentInitiated, reasonTreatmentNotIniated, reasonTreatmentNotInitiatedOther
                        , tbCategory, historyCategory, outcomePreviousCategory, weight, returnVisitDate}};
        formDate.getButton().setOnClickListener(this);
        regDate.getButton().setOnClickListener(this);
        returnVisitDate.getButton().setOnClickListener(this);
        extraPulmonarySite.getSpinner().setOnItemSelectedListener(this);
        tbType.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentInitiated.getRadioGroup().setOnCheckedChangeListener(this);
        reasonTreatmentNotIniated.getSpinner().setOnItemSelectedListener(this);
        tbCategory.getRadioGroup().setOnCheckedChangeListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        regDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        if (!dateChoose) {
            thirdDateCalendar.set(Calendar.YEAR, secondDateCalendar.get(Calendar.YEAR));
            thirdDateCalendar.set(Calendar.DAY_OF_MONTH, secondDateCalendar.get(Calendar.DAY_OF_MONTH));
            thirdDateCalendar.set(Calendar.MONTH, secondDateCalendar.get(Calendar.MONTH));
            thirdDateCalendar.add(Calendar.DAY_OF_MONTH, 30);
        }
        returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
        dateChoose = false;
    }

    @Override
    public boolean validate() {
        Boolean error = false;

        if (App.get(cnic1).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic1.getEditText().setError(getString(R.string.empty_field));
            cnic1.getEditText().requestFocus();
            error = true;
        }

        if (App.get(cnic2).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic2.getEditText().setError(getString(R.string.empty_field));
            cnic2.getEditText().requestFocus();
            error = true;
        }

        if (App.get(cnic3).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic3.getEditText().setError(getString(R.string.empty_field));
            cnic3.getEditText().requestFocus();
            error = true;
        }

        if (App.get(cnic1).length() != 5) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic1.getEditText().setError(getString(R.string.length_message));
            cnic1.getEditText().requestFocus();
            error = true;
        }

        if (App.get(cnic2).length() != 7) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic2.getEditText().setError(getString(R.string.length_message));
            cnic2.getEditText().requestFocus();
            error = true;
        }

        if (App.get(cnic3).length() != 1) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic3.getEditText().setError(getString(R.string.length_message));
            cnic3.getEditText().requestFocus();
            error = true;
        }


        if (tbRegisterationNumber.getVisibility() == View.VISIBLE && App.get(tbRegisterationNumber).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            tbRegisterationNumber.getEditText().setError(getString(R.string.empty_field));
            tbRegisterationNumber.getEditText().requestFocus();
            error = true;
        }


        if (extraPulmonarySiteOther.getVisibility() == View.VISIBLE && App.get(extraPulmonarySiteOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            extraPulmonarySiteOther.getEditText().setError(getString(R.string.empty_field));
            extraPulmonarySiteOther.getEditText().requestFocus();
            error = true;
        }


        if (reasonTreatmentNotInitiatedOther.getVisibility() == View.VISIBLE && App.get(reasonTreatmentNotInitiatedOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reasonTreatmentNotInitiatedOther.getEditText().setError(getString(R.string.empty_field));
            reasonTreatmentNotInitiatedOther.getEditText().requestFocus();
            error = true;
        }


        if (weight.getVisibility() == View.VISIBLE && App.get(weight).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            weight.getEditText().setError(getString(R.string.empty_field));
            weight.getEditText().requestFocus();
            error = true;
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
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

        endTime = new Date();
        String cnicNumber = cnic1.getEditText().toString() + cnic2.getEditText().toString() + cnic3.getEditText().toString();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
       /* observations.add (new String[] {"LONGITUDE (DEGREES)", String.valueOf(longitude)});
        observations.add (new String[] {"LATITUDE (DEGREES)", String.valueOf(latitude)});*/
        observations.add(new String[]{"REGISTRATION DATE", App.getSqlDateTime(secondDateCalendar)});
        observations.add(new String[]{"NATIONAL IDENTIFICATION NUMBER", cnicNumber});

        if (cnicOwner.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COMPUTERIZED NATIONAL IDENTIFICATION OWNER", App.get(cnicOwner).equals(getResources().getString(R.string.fast_self)) ? "SELF" :
                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_mother)) ? "MOTHER" :
                            (App.get(cnicOwner).equals(getResources().getString(R.string.fast_father)) ? "FATHER" :
                                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_sister)) ? "SISTER" :
                                            (App.get(cnicOwner).equals(getResources().getString(R.string.fast_brother)) ? "BROTHER" :
                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_spouse)) ? "SPOUSE" :
                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.fast_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.fast_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.fast_uncle)) ? "UNCLE" :
                                                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_aunt)) ? "AUNT" :
                                                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.fast_son)) ? "SON" :
                                                                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_daughter)) ? "DAUGHTER" : "OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER")))))))))))))});

        if (tbRegisterationNumber.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TB REGISTRATION NUMBER", App.get(tbRegisterationNumber)});

        if (diagonosisType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOSIS DIAGNOSIS METHOD", App.get(diagonosisType).equals(getResources().getString(R.string.fast_bactoriologically_confirmed)) ? "PRIMARY RESPIRATORY TUBERCULOSIS, CONFIRMED BACTERIOLOGICALLY AND HISTOLOGICALLY" : "CLINICAL SUSPICION"});

        if (tbType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SITE OF TUBERCULOSIS DISEASE", App.get(tbType).equals(getResources().getString(R.string.fast_pulmonary)) ? "PULMONARY TUBERCULOSIS" : "EXTRA-PULMONARY TUBERCULOSIS"});


        if (extraPulmonarySite.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"EXTRA PULMONARY SITE", App.get(extraPulmonarySite).equals(getResources().getString(R.string.fast_lymph_node)) ? "LYMPH NODE SARCOIDOSIS" :
                    (App.get(extraPulmonarySite).equals(getResources().getString(R.string.fast_abdomen)) ? "ABDOMEN" :
                            (App.get(extraPulmonarySite).equals(getResources().getString(R.string.fast_CNS)) ? "ACUTE LYMPHOBLASTIC LEUKEMIA WITH CENTRAL NERVOUS SYSTEM INVOLVEMENT" :
                                    (App.get(extraPulmonarySite).equals(getResources().getString(R.string.fast_renal)) ? "RENAL DISEASE" :
                                            (App.get(extraPulmonarySite).equals(getResources().getString(R.string.fast_bones)) ? "TUBERCULOSIS OF BONES AND JOINTS" :
                                                    (App.get(extraPulmonarySite).equals(getResources().getString(R.string.fast_genitourinary)) ? "GENITOURINARY TUBERCULOSIS" :
                                                            (App.get(extraPulmonarySite).equals(getResources().getString(R.string.fast_pleural_effusion)) ? "PLEURAL EFFUSION" : "OTHER EXTRA PULMONARY SITE" ))))))});


        if (extraPulmonarySiteOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER EXTRA PULMONARY SITE", App.get(extraPulmonarySiteOther)});


        if (patientType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TB PATIENT TYPE", App.get(patientType).equals(getResources().getString(R.string.fast_new)) ? "NEW TB PATIENT" :
                    (App.get(patientType).equals(getResources().getString(R.string.fast_relapse)) ? "RELAPSE" :
                            (App.get(patientType).equals(getResources().getString(R.string.fast_referred_transferred_in)) ? "PATIENT REFERRED" :
                                    (App.get(patientType).equals(getResources().getString(R.string.fast_treatment_after_loss_to_follow_up)) ? "LOST TO FOLLOW-UP" :
                                            (App.get(patientType).equals(getResources().getString(R.string.fast_treatment_failure)) ? "TUBERCULOSIS TREATMENT FAILURE" : "OTHER" ))))});


        if (treatmentInitiated.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT INITIATED", App.get(treatmentInitiated).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});

        if (reasonTreatmentNotIniated.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT NOT STARTED", App.get(reasonTreatmentNotIniated).equals(getResources().getString(R.string.fast_patient_refused_treatment)) ? "REFUSAL OF TREATMENT BY PATIENT" :
                    (App.get(reasonTreatmentNotIniated).equals(getResources().getString(R.string.fast_patient_loss_to_follow_up)) ? "LOST TO FOLLOW-UP" :
                            (App.get(reasonTreatmentNotIniated).equals(getResources().getString(R.string.fast_patient_died)) ? "DECEASED" :
                                    (App.get(reasonTreatmentNotIniated).equals(getResources().getString(R.string.fast_referred_before_start_of_treatment)) ? "PATIENT REFERRED" : "TREATMENT NOT INITIATED OTHER REASON" )))});

        if (reasonTreatmentNotInitiatedOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT NOT INITIATED OTHER REASON", App.get(reasonTreatmentNotInitiatedOther)});

        if (tbCategory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TB CATEGORY", App.get(tbCategory).equals(getResources().getString(R.string.fast_category1)) ? "CATEGORY I TUBERCULOSIS" : "CATEGORY II TUBERCULOSIS"});

       if (historyCategory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HISTORY OF PREVIOUSLY ANTI TUBERCULOSIS TREATMENT", App.get(historyCategory).equals(getResources().getString(R.string.fast_cat_1)) ? "CATEGORY I TUBERCULOSIS" :
                    (App.get(historyCategory).equals(getResources().getString(R.string.fast_cat_2)) ? "CATEGORY II TUBERCULOSIS" :
                            (App.get(historyCategory).equals(getResources().getString(R.string.fast_cat1_cat2)) ? "CAT I & II TUBERCULOSIS" : "OTHER" ))});


        if (outcomePreviousCategory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OUTCOME OF PREVIOUS CAT II TUBERCULOSIS TREATMENT", App.get(outcomePreviousCategory).equals(getResources().getString(R.string.fast_cured)) ? "CURE, OUTCOME" :
                    (App.get(outcomePreviousCategory).equals(getResources().getString(R.string.fast_treatment_completed)) ? "TREATMENT COMPLETE" :
                            (App.get(outcomePreviousCategory).equals(getResources().getString(R.string.fast_treatment_failure)) ? "TUBERCULOSIS TREATMENT FAILURE" :
                                    (App.get(outcomePreviousCategory).equals(getResources().getString(R.string.fast_transfer_out)) ? "TRANSFERRED OUT":
                                        (App.get(outcomePreviousCategory).equals(getResources().getString(R.string.fast_loss_to_follow_up_default)) ? "LOST TO FOLLOW-UP" : "OTHER" ))))});

        if (weight.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"WEIGHT (KG)", App.get(weight)});

        observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDateTime(thirdDateCalendar)});

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

                String result = serverService.saveEncounterAndObservation("Treatment Initiation", FORM, formDateCalendar, observations.toArray(new String[][]{}));
                if (result.contains("SUCCESS"))
                    return "SUCCESS";

                return result;

            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

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

      /*  formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        formValues.put(lastName.getTag(), App.get(lastName));
        formValues.put(husbandName.getTag(), App.get(husbandName));
        formValues.put(gender.getTag(), App.get(gender));

        serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);*/

        return true;
    }

    @Override
    public void refill(int encounterId) {

    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
        }

        if (view == regDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
        }

        if (view == returnVisitDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", false);
            args.putBoolean("allowFutureDate", true);
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

        if (spinner == extraPulmonarySite.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                extraPulmonarySiteOther.setVisibility(View.VISIBLE);
            } else {
                extraPulmonarySiteOther.setVisibility(View.GONE);
            }
        } else if (spinner == reasonTreatmentNotIniated.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                reasonTreatmentNotInitiatedOther.setVisibility(View.VISIBLE);
            } else {
                reasonTreatmentNotInitiatedOther.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == tbType.getRadioGroup()) {
            if (tbType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_extra_pulmonary))) {
                extraPulmonarySite.setVisibility(View.VISIBLE);
                if (extraPulmonarySite.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                    extraPulmonarySiteOther.setVisibility(View.VISIBLE);
                } else {
                    extraPulmonarySiteOther.setVisibility(View.GONE);
                }
            } else {
                extraPulmonarySite.setVisibility(View.GONE);
                extraPulmonarySiteOther.setVisibility(View.GONE);
            }
        } else if (radioGroup == treatmentInitiated.getRadioGroup()) {
            if (treatmentInitiated.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                reasonTreatmentNotIniated.setVisibility(View.VISIBLE);
                if (reasonTreatmentNotIniated.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                    reasonTreatmentNotInitiatedOther.setVisibility(View.VISIBLE);
                } else {
                    reasonTreatmentNotInitiatedOther.setVisibility(View.GONE);
                }
            } else {
                reasonTreatmentNotIniated.setVisibility(View.GONE);
                reasonTreatmentNotInitiatedOther.setVisibility(View.GONE);
            }
        } else if (radioGroup == tbCategory.getRadioGroup()) {
            if (tbCategory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_category2))) {
                historyCategory.setVisibility(View.VISIBLE);
                outcomePreviousCategory.setVisibility(View.VISIBLE);
            } else {
                historyCategory.setVisibility(View.GONE);
                outcomePreviousCategory.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        regDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
        extraPulmonarySite.setVisibility(View.GONE);
        extraPulmonarySiteOther.setVisibility(View.GONE);
        reasonTreatmentNotIniated.setVisibility(View.GONE);
        reasonTreatmentNotInitiatedOther.setVisibility(View.GONE);
        historyCategory.setVisibility(View.GONE);
        outcomePreviousCategory.setVisibility(View.GONE);
        updateDisplay();

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
                String cnic1 = serverService.getObsValue(App.getPatient().getPatientId(), App.getProgram() + "-" + "Presumptive Information", "NATIONAL IDENTIFICATION NUMBER");
                String cnicowner1 = serverService.getObsValue(App.getPatient().getPatientId(), App.getProgram() + "-" + "Presumptive Information", "COMPUTERIZED NATIONAL IDENTIFICATION OWNER");

                if (cnic1 != null)
                    result.put("NATIONAL IDENTIFICATION NUMBER", cnic1);
                if (cnicowner1 != null) {
                    result.put("COMPUTERIZED NATIONAL IDENTIFICATION OWNER", cnicowner1.equals("SELF") ? getResources().getString(R.string.fast_self) :
                            (cnicowner1.equals("MOTHER") ? getResources().getString(R.string.fast_mother) :
                                    (cnicowner1.equals("FATHER") ? getResources().getString(R.string.fast_father) :
                                            (cnicowner1.equals("SISTER") ? getResources().getString(R.string.fast_sister) :
                                                    (cnicowner1.equals("BROTHER") ? getResources().getString(R.string.fast_brother) :
                                                            (cnicowner1.equals("SPOUSE") ? getResources().getString(R.string.fast_spouse) :
                                                                    (cnicowner1.equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.fast_paternal_grandfather) :
                                                                            (cnicowner1.equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.fast_paternal_grandmother) :
                                                                                    (cnicowner1.equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.fast_maternal_grandfather) :
                                                                                            (cnicowner1.equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.fast_maternal_grandmother) :
                                                                                                    (cnicowner1.equals("UNCLE") ? getResources().getString(R.string.fast_uncle) :
                                                                                                            (cnicowner1.equals("AUNT") ? getResources().getString(R.string.fast_aunt) :
                                                                                                                    (cnicowner1.equals("SON") ? getResources().getString(R.string.fast_son) :
                                                                                                                            (cnicowner1.equals("DAUGHTER") ? getResources().getString(R.string.fast_daughter) : getResources().getString(R.string.fast_other_title)))))))))))))));

                }

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

                if (result.get("NATIONAL IDENTIFICATION NUMBER") != null) {
                    String value = result.get("NATIONAL IDENTIFICATION NUMBER");
                    cnic1.getEditText().setText(value.substring(0, 5));
                    cnic2.getEditText().setText(value.substring(5, 12));
                    cnic3.getEditText().setText(value.substring(12));
                }

                if (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER") != null) {
                    cnicOwner.getSpinner().selectValue(result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER"));
                }
            }
        };
        autopopulateFormTask.execute("");

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

            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setTag(getArguments().getInt("type"));
            if (!getArguments().getBoolean("allowFutureDate", false))
                dialog.getDatePicker().setMaxDate(new Date().getTime());
            if (!getArguments().getBoolean("allowPastDate", false))
                dialog.getDatePicker().setMinDate(new Date().getTime());
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