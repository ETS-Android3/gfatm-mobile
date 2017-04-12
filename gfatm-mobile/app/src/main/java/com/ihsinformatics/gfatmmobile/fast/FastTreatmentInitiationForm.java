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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
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
    TitledEditText cnicOwnerOther;

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

    Snackbar snackbar;

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
        cnic1 = new TitledEditText(context, null, getResources().getString(R.string.fast_nic_number), "", "#####", 5, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        cnicLinearLayout.addView(cnic1);
        cnic2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLinearLayout.addView(cnic2);
        cnic3 = new TitledEditText(context, null, "-", "", "#", 1, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLinearLayout.addView(cnic3);
        cnicOwner = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_cnic), getResources().getStringArray(R.array.fast_whose_nic_is_this_list), getResources().getString(R.string.fast_self), App.VERTICAL);
        cnicOwnerOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        tbRegisterationNumber = new TitledEditText(context, null, getResources().getString(R.string.fast_tb_registeration_no), "", "", 11, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        diagonosisType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_type_of_diagnosis), getResources().getStringArray(R.array.fast_diagonosis_type_list), getResources().getString(R.string.fast_bactoriologically_confirmed), App.VERTICAL, App.VERTICAL);
        tbType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_what_type_of_tb), getResources().getStringArray(R.array.fast_tb_type_list), getResources().getString(R.string.fast_pulmonary), App.VERTICAL, App.VERTICAL);
        extraPulmonarySite = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_site_of_extra_pulmonary), getResources().getStringArray(R.array.fast_extra_pulmonary_site_list), getResources().getString(R.string.fast_lymph_node), App.VERTICAL);
        extraPulmonarySiteOther = new TitledEditText(context, null, getResources().getString(R.string.other_extra_pulmonary_tb_site), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        patientType = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_patient_type), getResources().getStringArray(R.array.fast_patient_type_list), getResources().getString(R.string.fast_new), App.VERTICAL);
        treatmentInitiated = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_was_treatment_initiated), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        reasonTreatmentNotIniated = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_reason_the_treatment_was_not_iniated), getResources().getStringArray(R.array.fast_reason_treatment_notinitiated_list), getResources().getString(R.string.fast_patient_refused_treatment), App.VERTICAL);
        reasonTreatmentNotInitiatedOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        tbCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_patient_category), getResources().getStringArray(R.array.fast_tb_category_list), getResources().getString(R.string.fast_category1), App.VERTICAL, App.VERTICAL);
        historyCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_if_category_2_history_of_previous), getResources().getStringArray(R.array.fast_history_category_2_list), getResources().getString(R.string.fast_cat_1), App.VERTICAL, App.VERTICAL);
        outcomePreviousCategory = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_if_category_2_outcome_of_previous), getResources().getStringArray(R.array.fast_outcome_previous_category_list), getResources().getString(R.string.fast_cured), App.VERTICAL);
        weight = new TitledEditText(context, null, getResources().getString(R.string.fast_patient_weight), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        thirdDateCalendar.set(Calendar.YEAR, secondDateCalendar.get(Calendar.YEAR));
        thirdDateCalendar.set(Calendar.DAY_OF_MONTH, secondDateCalendar.get(Calendar.DAY_OF_MONTH));
        thirdDateCalendar.set(Calendar.MONTH, secondDateCalendar.get(Calendar.MONTH));
        thirdDateCalendar.add(Calendar.DAY_OF_MONTH, 30);
        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.fast_next_appointment_date), DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), regDate.getButton(), cnic1.getEditText(), cnic2.getEditText(), cnic3.getEditText(),
                cnicOwner.getSpinner(), cnicOwnerOther.getEditText(), tbRegisterationNumber.getEditText(), diagonosisType.getRadioGroup(), tbType.getRadioGroup(),
                extraPulmonarySite.getSpinner(), extraPulmonarySiteOther.getEditText(), patientType.getSpinner(), treatmentInitiated.getRadioGroup(),
                reasonTreatmentNotIniated.getSpinner(), reasonTreatmentNotInitiatedOther.getEditText(), tbCategory.getRadioGroup(),
                historyCategory.getRadioGroup(), outcomePreviousCategory.getSpinner(), weight.getEditText(), returnVisitDate.getButton()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, regDate, cnicLinearLayout, cnicOwner, cnicOwnerOther, diagonosisType, tbType, extraPulmonarySite,
                        extraPulmonarySiteOther, patientType, treatmentInitiated, reasonTreatmentNotIniated, reasonTreatmentNotInitiatedOther,
                        tbRegisterationNumber, tbCategory, historyCategory, outcomePreviousCategory, weight, returnVisitDate}};
        formDate.getButton().setOnClickListener(this);
        regDate.getButton().setOnClickListener(this);
        returnVisitDate.getButton().setOnClickListener(this);
        extraPulmonarySite.getSpinner().setOnItemSelectedListener(this);
        tbType.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentInitiated.getRadioGroup().setOnCheckedChangeListener(this);
        reasonTreatmentNotIniated.getSpinner().setOnItemSelectedListener(this);
        tbCategory.getRadioGroup().setOnCheckedChangeListener(this);
        cnicOwner.getSpinner().setOnItemSelectedListener(this);

        resetViews();
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

            }else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            }
            else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        }

        if (!(regDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString()))) {

            String formDa = regDate.getButton().getText().toString();

            Date date = new Date();
            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                regDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());

            } else
                regDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        }

        if (!dateChoose) {
            thirdDateCalendar.set(Calendar.YEAR, secondDateCalendar.get(Calendar.YEAR));
            thirdDateCalendar.set(Calendar.DAY_OF_MONTH, secondDateCalendar.get(Calendar.DAY_OF_MONTH));
            thirdDateCalendar.set(Calendar.MONTH, secondDateCalendar.get(Calendar.MONTH));
            thirdDateCalendar.add(Calendar.DAY_OF_MONTH, 30);
        }
        if (!(returnVisitDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString()))) {

            String formDa = returnVisitDate.getButton().getText().toString();

            Date date = new Date();
            if (thirdDateCalendar.before(App.getCalendar(date))) {

                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());

            } else
                returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
        }
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

        if (cnicOwnerOther.getVisibility() == View.VISIBLE && App.get(cnicOwnerOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnicOwnerOther.getEditText().setError(getString(R.string.empty_field));
            cnicOwnerOther.getEditText().requestFocus();
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
        final ArrayList<String[]> observations = new ArrayList<String[]>();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
                observations.add(new String[]{"TIME TAKEN TO FILL FORM", timeTakeToFill});
            }else {
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


        String cnicNumber = cnic1.getEditText().toString() +"-"+ cnic2.getEditText().toString() +"-"+ cnic3.getEditText().toString();

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

        if(cnicOwnerOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER", App.get(cnicOwnerOther)});


        if (tbRegisterationNumber.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TB REGISTRATION NUMBER", App.get(tbRegisterationNumber)});

        if (diagonosisType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOSIS DIAGNOSIS METHOD", App.get(diagonosisType).equals(getResources().getString(R.string.fast_bactoriologically_confirmed)) ? "PRIMARY RESPIRATORY TUBERCULOSIS, CONFIRMED BACTERIOLOGICALLY" : "CLINICAL SUSPICION"});

        if (tbType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SITE OF TUBERCULOSIS DISEASE", App.get(tbType).equals(getResources().getString(R.string.fast_pulmonary)) ? "PULMONARY TUBERCULOSIS" : "EXTRA-PULMONARY TUBERCULOSIS"});


        if (extraPulmonarySite.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"EXTRA PULMONARY SITE", App.get(extraPulmonarySite).equals(getResources().getString(R.string.fast_lymph_node)) ? "LYMPH NODE SARCOIDOSIS" :
                    (App.get(extraPulmonarySite).equals(getResources().getString(R.string.fast_abdomen)) ? "ABDOMEN" :
                            (App.get(extraPulmonarySite).equals(getResources().getString(R.string.fast_CNS)) ? "ACUTE LYMPHOBLASTIC LEUKEMIA WITH CENTRAL NERVOUS SYSTEM INVOLVEMENT" :
                                    (App.get(extraPulmonarySite).equals(getResources().getString(R.string.fast_renal)) ? "RENAL DISEASE" :
                                            (App.get(extraPulmonarySite).equals(getResources().getString(R.string.fast_bones)) ? "TUBERCULOSIS OF BONES AND JOINTS" :
                                                    (App.get(extraPulmonarySite).equals(getResources().getString(R.string.fast_genitourinary)) ? "GENITOURINARY TUBERCULOSIS" :
                                                            (App.get(extraPulmonarySite).equals(getResources().getString(R.string.fast_pleural_effusion)) ? "PLEURAL EFFUSION" : "OTHER EXTRA PULMONARY SITE"))))))});


        if (extraPulmonarySiteOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER EXTRA PULMONARY SITE", App.get(extraPulmonarySiteOther)});


        if (patientType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TB PATIENT TYPE", App.get(patientType).equals(getResources().getString(R.string.fast_new)) ? "NEW TB PATIENT" :
                    (App.get(patientType).equals(getResources().getString(R.string.fast_relapse)) ? "RELAPSE" :
                            (App.get(patientType).equals(getResources().getString(R.string.fast_referred_transferred_in)) ? "PATIENT REFERRED" :
                                    (App.get(patientType).equals(getResources().getString(R.string.fast_treatment_after_loss_to_follow_up)) ? "LOST TO FOLLOW-UP" :
                                            (App.get(patientType).equals(getResources().getString(R.string.fast_treatment_failure)) ? "TUBERCULOSIS TREATMENT FAILURE" : "OTHER PATIENT TYPE"))))});


        if (treatmentInitiated.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT INITIATED", App.get(treatmentInitiated).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});

        if (reasonTreatmentNotIniated.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT NOT STARTED", App.get(reasonTreatmentNotIniated).equals(getResources().getString(R.string.fast_patient_refused_treatment)) ? "REFUSAL OF TREATMENT BY PATIENT" :
                    (App.get(reasonTreatmentNotIniated).equals(getResources().getString(R.string.fast_patient_loss_to_follow_up)) ? "LOST TO FOLLOW-UP" :
                            (App.get(reasonTreatmentNotIniated).equals(getResources().getString(R.string.fast_patient_died)) ? "DIED" :
                                    (App.get(reasonTreatmentNotIniated).equals(getResources().getString(R.string.fast_referred_before_start_of_treatment)) ? "PATIENT REFERRED" : "TREATMENT NOT INITIATED OTHER REASON")))});

        if (reasonTreatmentNotInitiatedOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT NOT INITIATED OTHER REASON", App.get(reasonTreatmentNotInitiatedOther)});

        if (tbCategory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TB CATEGORY", App.get(tbCategory).equals(getResources().getString(R.string.fast_category1)) ? "CATEGORY I TUBERCULOSIS" : "CATEGORY II TUBERCULOSIS"});

        if (historyCategory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HISTORY OF PREVIOUSLY ANTI TUBERCULOSIS TREATMENT", App.get(historyCategory).equals(getResources().getString(R.string.fast_cat_1)) ? "CATEGORY I TUBERCULOSIS" :
                    (App.get(historyCategory).equals(getResources().getString(R.string.fast_cat_2)) ? "CATEGORY II TUBERCULOSIS" :
                            (App.get(historyCategory).equals(getResources().getString(R.string.fast_cat1_cat2)) ? "CAT I & II TUBERCULOSIS" : "OTHER TUBERCULOSIS CATEGORY"))});


        if (outcomePreviousCategory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OUTCOME OF PREVIOUS CAT II TUBERCULOSIS TREATMENT", App.get(outcomePreviousCategory).equals(getResources().getString(R.string.fast_cured)) ? "CURE, OUTCOME" :
                    (App.get(outcomePreviousCategory).equals(getResources().getString(R.string.fast_treatment_completed)) ? "TREATMENT COMPLETE" :
                            (App.get(outcomePreviousCategory).equals(getResources().getString(R.string.fast_treatment_failure)) ? "TUBERCULOSIS TREATMENT FAILURE" :
                                    (App.get(outcomePreviousCategory).equals(getResources().getString(R.string.fast_transfer_out)) ? "TRANSFERRED OUT" :
                                            (App.get(outcomePreviousCategory).equals(getResources().getString(R.string.fast_loss_to_follow_up_default)) ? "LOST TO FOLLOW-UP" : "OTHER TREATMENT OUTCOME"))))});

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

                String result = serverService.saveEncounterAndObservation("Treatment Initiation", FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
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
    public void refill(int formId) {

        OfflineForm fo = serverService.getOfflineFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("REGISTRATION DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                regDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
                regDate.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("NATIONAL IDENTIFICATION NUMBER")) {
                String data = obs[0][1];
                cnic1.getEditText().setText(data.substring(0, 5));
                cnic2.getEditText().setText(data.substring(6, 13));
                cnic3.getEditText().setText(data.substring(14));
            } else if (obs[0][0].equals("COMPUTERIZED NATIONAL IDENTIFICATION OWNER")) {
                String value = obs[0][1].equals("SELF") ? getResources().getString(R.string.fast_self) :
                        (obs[0][1].equals("MOTHER") ? getResources().getString(R.string.fast_mother) :
                                (obs[0][1].equals("FATHER") ? getResources().getString(R.string.fast_father) :
                                        (obs[0][1].equals("SISTER") ? getResources().getString(R.string.fast_sister) :
                                                (obs[0][1].equals("BROTHER") ? getResources().getString(R.string.fast_brother) :
                                                        (obs[0][1].equals("SPOUSE") ? getResources().getString(R.string.fast_spouse) :
                                                                (obs[0][1].equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.fast_paternal_grandfather) :
                                                                        (obs[0][1].equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.fast_paternal_grandmother) :
                                                                                (obs[0][1].equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.fast_maternal_grandfather) :
                                                                                        (obs[0][1].equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.fast_maternal_grandmother) :
                                                                                                (obs[0][1].equals("UNCLE") ? getResources().getString(R.string.fast_uncle) :
                                                                                                        (obs[0][1].equals("AUNT") ? getResources().getString(R.string.fast_aunt) :
                                                                                                                (obs[0][1].equals("SON") ? getResources().getString(R.string.fast_son) :
                                                                                                                        (obs[0][1].equals("DAUGHTER") ? getResources().getString(R.string.fast_daughter) :
                                                                                                                                getResources().getString(R.string.fast_other_title))))))))))))));


                cnicOwner.getSpinner().selectValue(value);
                cnicOwner.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER")) {
                cnicOwnerOther.getEditText().setText(obs[0][1]);
                cnicOwnerOther.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("TB REGISTRATION NUMBER")) {
                tbRegisterationNumber.getEditText().setText(obs[0][1]);
                tbRegisterationNumber.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TUBERCULOSIS DIAGNOSIS METHOD")) {

                for (RadioButton rb : diagonosisType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_bactoriologically_confirmed)) && obs[0][1].equals("PRIMARY RESPIRATORY TUBERCULOSIS, CONFIRMED BACTERIOLOGICALLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_clinically_diagnosed)) && obs[0][1].equals("CLINICAL SUSPICION")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                diagonosisType.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("SITE OF TUBERCULOSIS DISEASE")) {

                for (RadioButton rb : tbType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_pulmonary)) && obs[0][1].equals("PULMONARY TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_extra_pulmonary)) && obs[0][1].equals("EXTRA-PULMONARY TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                tbType.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("EXTRA PULMONARY SITE")) {
                String value = obs[0][1].equals("LYMPH NODE SARCOIDOSIS") ? getResources().getString(R.string.fast_lymph_node) :
                        (obs[0][1].equals("ABDOMEN") ? getResources().getString(R.string.fast_abdomen) :
                                (obs[0][1].equals("ACUTE LYMPHOBLASTIC LEUKEMIA WITH CENTRAL NERVOUS SYSTEM INVOLVEMENT") ? getResources().getString(R.string.fast_CNS) :
                                        (obs[0][1].equals("RENAL DISEASE") ? getResources().getString(R.string.fast_renal) :
                                                (obs[0][1].equals("TUBERCULOSIS OF BONES AND JOINTS") ? getResources().getString(R.string.fast_bones) :
                                                        (obs[0][1].equals("GENITOURINARY TUBERCULOSIS") ? getResources().getString(R.string.fast_genitourinary) :
                                                                (obs[0][1].equals("PLEURAL EFFUSION") ? getResources().getString(R.string.fast_pleural_effusion) :
                                                                        getResources().getString(R.string.fast_other_title)))))));

                extraPulmonarySite.getSpinner().selectValue(value);
                extraPulmonarySite.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER EXTRA PULMONARY SITE")) {
                extraPulmonarySiteOther.getEditText().setText(obs[0][1]);
                extraPulmonarySiteOther.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TB PATIENT TYPE")) {
                String value = obs[0][1].equals("NEW TB PATIENT") ? getResources().getString(R.string.fast_new) :
                        (obs[0][1].equals("RELAPSE") ? getResources().getString(R.string.fast_relapse) :
                                (obs[0][1].equals("PATIENT REFERRED") ? getResources().getString(R.string.fast_referred_transferred_in) :
                                        (obs[0][1].equals("LOST TO FOLLOW-UP") ? getResources().getString(R.string.fast_treatment_after_loss_to_follow_up) :
                                                (obs[0][1].equals("TUBERCULOSIS TREATMENT FAILURE") ? getResources().getString(R.string.fast_treatment_failure) :
                                                        getResources().getString(R.string.fast_other_title)))));

                patientType.getSpinner().selectValue(value);
                patientType.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT INITIATED")) {

                for (RadioButton rb : treatmentInitiated.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                treatmentInitiated.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT NOT STARTED")) {
                String value = obs[0][1].equals("REFUSAL OF TREATMENT BY PATIENT") ? getResources().getString(R.string.fast_patient_refused_treatment) :
                        (obs[0][1].equals("LOST TO FOLLOW-UP") ? getResources().getString(R.string.fast_patient_loss_to_follow_up) :
                                (obs[0][1].equals("DIED") ? getResources().getString(R.string.fast_patient_died) :
                                        (obs[0][1].equals("PATIENT REFERRED") ? getResources().getString(R.string.fast_referral) :
                                                getResources().getString(R.string.fast_other_title))));

                reasonTreatmentNotIniated.getSpinner().selectValue(value);
                reasonTreatmentNotIniated.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT NOT INITIATED OTHER REASON")) {
                reasonTreatmentNotInitiatedOther.getEditText().setText(obs[0][1]);
                reasonTreatmentNotInitiatedOther.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TB CATEGORY")) {

                for (RadioButton rb : tbCategory.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_category1)) && obs[0][1].equals("CATEGORY I TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_category2)) && obs[0][1].equals("CATEGORY II TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                tbCategory.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("HISTORY OF PREVIOUSLY ANTI TUBERCULOSIS TREATMENT")) {

                for (RadioButton rb : historyCategory.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_cat_1)) && obs[0][1].equals("CATEGORY I TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_cat_2)) && obs[0][1].equals("CATEGORY II TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_cat1_cat2)) && obs[0][1].equals("CAT I & II TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_others)) && obs[0][1].equals("OTHER")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                historyCategory.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OUTCOME OF PREVIOUS CAT II TUBERCULOSIS TREATMENT")) {
                String value = obs[0][1].equals("CURE, OUTCOME") ? getResources().getString(R.string.fast_cured) :
                        (obs[0][1].equals("TREATMENT COMPLETE") ? getResources().getString(R.string.fast_treatment_completed) :
                                (obs[0][1].equals("TUBERCULOSIS TREATMENT FAILURE") ? getResources().getString(R.string.fast_treatment_failure) :
                                        (obs[0][1].equals("TRANSFERRED OUT") ? getResources().getString(R.string.fast_transfer_out) :
                                                (obs[0][1].equals("LOST TO FOLLOW-UP") ? getResources().getString(R.string.fast_loss_to_follow_up_default) :
                                                        getResources().getString(R.string.fast_other_title)))));

                outcomePreviousCategory.getSpinner().selectValue(value);
                outcomePreviousCategory.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("WEIGHT (KG)")) {
                weight.getEditText().setText(obs[0][1]);
                weight.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String secondDate = obs[0][1];
                thirdDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
                returnVisitDate.setVisibility(View.VISIBLE);
            }
        }
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
        else if(spinner == cnicOwner.getSpinner()){
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                cnicOwnerOther.setVisibility(View.VISIBLE);
            } else {
                cnicOwnerOther.setVisibility(View.GONE);
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
        cnicOwnerOther.setVisibility(View.GONE);
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
                String cnic1 = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Presumptive Information", "NATIONAL IDENTIFICATION NUMBER");
                String cnicowner1 = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Presumptive Information", "COMPUTERIZED NATIONAL IDENTIFICATION OWNER");
                String cnicownerother1 = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Presumptive Information", "OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER");
                String regDate = serverService.getEncounterDateTime(App.getPatientId(), App.getProgram() + "-" + "Presumptive Information");

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

                if (cnicownerother1 != null)
                    result.put("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER", cnicownerother1);

                if (regDate != null)
                    result.put("FORM DATE", regDate);

                return result;
            }

            @Override
            protected void onProgressUpdate(String... values) {
            }


            @Override
            protected void onPostExecute(HashMap<String, String> result) {
                super.onPostExecute(result);
                loading.dismiss();

                if (result.get("NATIONAL IDENTIFICATION NUMBER") != null) {
                    String value = result.get("NATIONAL IDENTIFICATION NUMBER");
                    cnic1.getEditText().setText(value.substring(0, 5));
                    cnic2.getEditText().setText(value.substring(6, 13));
                    cnic3.getEditText().setText(value.substring(14));
                }

                if (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER") != null) {
                    cnicOwner.getSpinner().selectValue(result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER"));
                }

                if (result.get("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER") != null) {
                    cnicOwnerOther.getEditText().setText(result.get("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER"));
                }

                if (result.get("FORM DATE") != null) {
                    String format = "";
                    String registerationDate = result.get("FORM DATE");
                    if(registerationDate.contains("/")){
                        format = "dd/MM/yyyy";
                    }
                    else{
                        format = "yyyy-MM-dd";
                    }
                    secondDateCalendar.setTime(App.stringToDate(registerationDate, format));
                    regDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
                }
            }
        };
        autopopulateFormTask.execute("");

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
