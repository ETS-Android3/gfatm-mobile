package com.ihsinformatics.gfatmmobile.pmdt;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tahira on 3/6/2017.
 */

public class PmdtConveyanceAllowanceForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledEditText externalId;
    TitledSpinner typeAssessment;
    TitledEditText otherAssessmentType;
    TitledSpinner treatmentMonth;

    TitledSpinner treatmentFacility;
    TitledEditText otherTreatmentFacility;

    TitledEditText nationalDrTbRegistrationNumber;
    TitledRadioGroup isRegistered;

    TitledSpinner registeredOutstationFacility;

    LinearLayout patientCnicLayout;
    LinearLayout patientCnicQuestionLayout;
    MyTextView patientCnicQuestionRequired;
    MyTextView patientCnicQuestion;
    TitledEditText patientCnic1;
    TitledEditText patientCnic2;
    TitledEditText patientCnic3;
    TitledRadioGroup patientOwnCnic;
    TitledSpinner patientCnicOwner;
    TitledEditText otherPatientCnicOwner;
    TitledEditText namePatientCnicOwner;
    LinearLayout patientPrimaryPhoneLayout;
    TitledEditText patientPrimaryPhone1a;
    TitledEditText patientPrimaryPhone1b;
    LinearLayout patientAlternatePhoneLayout;
    TitledEditText patientAlternatePhone1a;
    TitledEditText patientAlternatePhone1b;

    TitledRadioGroup patientSubmitSputumSample;         // include title: Sample submission information
    TitledEditText sputumSampleId;
    TitledRadioGroup reasonNotSubmittedSample;
    TitledEditText otherReasonNotSubmittedSample;
    TitledRadioGroup visitedDoctor;
    TitledEditText conveyanceVoucherBookNumber;
    TitledEditText conveyanceVoucherNumber;
    TitledEditText amountTransferred;
    TitledEditText amountTransferredInWords;

    ScrollView scrollView;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PAGE_COUNT = 3;
        FORM_NAME = Forms.PMDT_CONVEYANCE_ALLOWANCE;
        FORM = Forms.pmdtConveyanceAllowance;

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
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }
        return mainContent;
    }

    @Override
    public void initViews() {
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pmdt_visit_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        externalId = new TitledEditText(context, null, getResources().getString(R.string.pmdt_external_id), "", "", 11, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        typeAssessment = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_assessment_type), getResources().getStringArray(R.array.pmdt_types_of_assessment), getResources().getString(R.string.pmdt_baseline_assessment), App.HORIZONTAL, true);
        otherAssessmentType = new TitledEditText(context, null, getResources().getString(R.string.pmdt_other_assessment_reason), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        treatmentMonth = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_treatment_month), getResources().getStringArray(R.array.pmdt_treatment_month), getResources().getString(R.string.fast_zero), App.HORIZONTAL);

        // Fetching PMDT Locations
        String program = "";
        if (App.getProgram().equals(getResources().getString(R.string.pet)))
            program = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.fast)))
            program = "fast_location";
        else if (App.getProgram().equals(getResources().getString(R.string.comorbidities)))
            program = "comorbidities_location";
        else if (App.getProgram().equals(getResources().getString(R.string.pmdt)))
            program = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.childhood_tb)))
            program = "childhood_tb_location";

        final Object[][] locations = serverService.getAllLocations(program);
        String[] locationArray = new String[locations.length + 1];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][1]);
        }
        locationArray[locationArray.length - 1] = getResources().getString(R.string.pmdt_other);

        treatmentFacility = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_treatment_facility), locationArray, "", App.VERTICAL);
        otherTreatmentFacility = new TitledEditText(context, "", getResources().getString(R.string.pmdt_other_treatment_facility), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);


        nationalDrTbRegistrationNumber = new TitledEditText(context, null, getResources().getString(R.string.pmdt_national_dr_tb_registration_number), "", "", 25, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        isRegistered = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_patient_registered_outstation), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);

        registeredOutstationFacility = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_registered_outstation_facility), locationArray, "", App.VERTICAL);

        // cnic layouts
        patientCnicLayout = new LinearLayout(context);
        patientCnicQuestionLayout = new LinearLayout(context);
        patientCnicLayout.setOrientation(LinearLayout.HORIZONTAL);
        patientCnicQuestionLayout.setOrientation(LinearLayout.HORIZONTAL);

        MyLinearLayout linearLayout1 = new MyLinearLayout(context, null, App.VERTICAL);
        patientCnicQuestionRequired = new MyTextView(context, "*");
        int color = App.getColor(context, R.attr.colorAccent);
        patientCnicQuestionRequired.setTextColor(color);
        patientCnicQuestion = new MyTextView(context, getResources().getString(R.string.pmdt_patient_cnic_provided));
        patientCnic1 = new TitledEditText(context, null, "", "", "XXXXX", 5, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        patientCnicLayout.addView(patientCnic1);
        patientCnic2 = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        patientCnicLayout.addView(patientCnic2);
        patientCnic3 = new TitledEditText(context, null, "-", "", "X", 1, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        patientCnicLayout.addView(patientCnic3);
        // setting question layout
        patientCnicQuestionLayout.addView(patientCnicQuestionRequired);
        patientCnicQuestionLayout.addView(patientCnicQuestion);
        // adding to parent titled cnic section layout
        linearLayout1.addView(patientCnicQuestionLayout);
        linearLayout1.addView(patientCnicLayout);

        patientOwnCnic = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_patient_own_cnic), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, false);
        patientCnicOwner = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_cnic_owner), getResources().getStringArray(R.array.pmdt_cnic_owners), getResources().getString(R.string.pmdt_father), App.HORIZONTAL, false);
        otherPatientCnicOwner = new TitledEditText(context, null, getResources().getString(R.string.pmdt_other_cnic_owner), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        namePatientCnicOwner = new TitledEditText(context, null, getResources().getString(R.string.pmdt_cnic_owner_name), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        patientPrimaryPhoneLayout = new LinearLayout(context);
        patientPrimaryPhoneLayout.setOrientation(LinearLayout.HORIZONTAL);
        patientPrimaryPhone1a = new TitledEditText(context, null, getResources().getString(R.string.pmdt_patient_primary_phone_number), "", "XXXX", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        patientPrimaryPhoneLayout.addView(patientPrimaryPhone1a);
        patientPrimaryPhone1b = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        patientPrimaryPhoneLayout.addView(patientPrimaryPhone1b);
        patientAlternatePhoneLayout = new LinearLayout(context);
        patientAlternatePhoneLayout.setOrientation(LinearLayout.HORIZONTAL);
        patientAlternatePhone1a = new TitledEditText(context, null, getResources().getString(R.string.pmdt_patient_alternate_phone_number), "", "XXXX", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        patientAlternatePhoneLayout.addView(patientAlternatePhone1a);
        patientAlternatePhone1b = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        patientAlternatePhoneLayout.addView(patientAlternatePhone1b);

        // Sample submission information
        patientSubmitSputumSample = new TitledRadioGroup(context, getResources().getString(R.string.pmdt_title_sample_submission_information), getResources().getString(R.string.pmdt_sputum_sample_submitted), getResources().getStringArray(R.array.pmdt_yes_no_not_applicable), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, false);
        sputumSampleId = new TitledEditText(context, null, getResources().getString(R.string.pmdt_sputum_sample_id), "", "", 25, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        reasonNotSubmittedSample = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_reason_not_submitted_sputum), getResources().getStringArray(R.array.pmdt_reasons_sputums_not_submitted), getResources().getString(R.string.pmdt_could_not_produce_sputum), App.VERTICAL, App.VERTICAL);
        otherReasonNotSubmittedSample = new TitledEditText(context, null, getResources().getString(R.string.pmdt_other_reason_not_submitted_sputum), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        visitedDoctor = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_patient_visited_doctor), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, false);
        conveyanceVoucherBookNumber = new TitledEditText(context, null, getResources().getString(R.string.pmdt_conveyance_voucher_book_number), "", "", 20, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.VERTICAL, true);
        conveyanceVoucherNumber = new TitledEditText(context, null, getResources().getString(R.string.pmdt_conveyance_voucher_number), "", "", 20, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.VERTICAL, true);
        amountTransferred = new TitledEditText(context, null, getResources().getString(R.string.pmdt_amount_transferred), "", "", 5, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        amountTransferredInWords = new TitledEditText(context, null, getResources().getString(R.string.pmdt_amount_in_words), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), externalId.getEditText(), typeAssessment.getSpinner(), otherAssessmentType.getEditText(), treatmentMonth.getSpinner(), treatmentFacility.getSpinner(), otherTreatmentFacility.getEditText(),
                nationalDrTbRegistrationNumber.getEditText(), isRegistered.getRadioGroup(), registeredOutstationFacility.getSpinner(), patientCnic1.getEditText(), patientCnic2.getEditText(), patientCnic3.getEditText(), patientOwnCnic.getRadioGroup(), patientCnicOwner.getSpinner(),
                otherPatientCnicOwner.getEditText(), namePatientCnicOwner.getEditText(), patientPrimaryPhone1a.getEditText(), patientPrimaryPhone1b.getEditText(), patientAlternatePhone1a.getEditText(), patientAlternatePhone1b.getEditText(), patientSubmitSputumSample.getRadioGroup(),
                sputumSampleId.getEditText(), reasonNotSubmittedSample.getRadioGroup(), otherReasonNotSubmittedSample.getEditText(), visitedDoctor.getRadioGroup(), conveyanceVoucherBookNumber.getEditText(),
                conveyanceVoucherNumber.getEditText(), amountTransferred.getEditText(), amountTransferredInWords.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, externalId, typeAssessment, otherAssessmentType, treatmentMonth, treatmentFacility, otherTreatmentFacility,
                        nationalDrTbRegistrationNumber, isRegistered, registeredOutstationFacility},
                        {linearLayout1, patientOwnCnic, patientCnicOwner, otherPatientCnicOwner, namePatientCnicOwner, patientPrimaryPhoneLayout, patientAlternatePhoneLayout},
                        {patientSubmitSputumSample, sputumSampleId, reasonNotSubmittedSample, otherReasonNotSubmittedSample, visitedDoctor,
                                conveyanceVoucherBookNumber, conveyanceVoucherNumber, amountTransferred, amountTransferredInWords}};

        formDate.getButton().setOnClickListener(this);
        typeAssessment.getSpinner().setOnItemSelectedListener(this);
        isRegistered.getRadioGroup().setOnCheckedChangeListener(this);
        patientOwnCnic.getRadioGroup().setOnCheckedChangeListener(this);
        patientCnicOwner.getSpinner().setOnItemSelectedListener(this);
        patientSubmitSputumSample.getRadioGroup().setOnCheckedChangeListener(this);
        reasonNotSubmittedSample.getRadioGroup().setOnCheckedChangeListener(this);
        visitedDoctor.getRadioGroup().setOnCheckedChangeListener(this);

    }

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();
            personDOB = personDOB.substring(0, 10);

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

        Boolean error = false;

        if (App.get(externalId).isEmpty()) {
            otherReasonNotSubmittedSample.getEditText().setError(getResources().getString(R.string.mandatory_field));
            otherReasonNotSubmittedSample.getEditText().requestFocus();
            error = true;
        }

        if (otherReasonNotSubmittedSample.getVisibility() == View.VISIBLE && App.get(otherReasonNotSubmittedSample).isEmpty()) {
            otherReasonNotSubmittedSample.getEditText().setError(getResources().getString(R.string.mandatory_field));
            otherReasonNotSubmittedSample.getEditText().requestFocus();
            error = true;
        }


        if (error) {

            // int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            // DrawableCompat.setTint(clearIcon, color);
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

        observations.add(new String[]{"TYPE OF ASSESSMENT", App.get(typeAssessment).equals(getResources().getString(R.string.pmdt_assessment_type)) ? "BASELINE ASSESSMENT" :
                (App.get(typeAssessment).equals(getResources().getString(R.string.pmdt_two_week_assessment)) ? "2 WEEK ASSESSMENT" :
                        (App.get(typeAssessment).equals(getResources().getString(R.string.pmdt_planened_monthly_assessment)) ? "PLANNED MONTHLY ASSESSMENT" :
                                (App.get(typeAssessment).equals(getResources().getString(R.string.pmdt_treatment_initiation)) ? "TREATMENT INITIATION" :
                                        (App.get(typeAssessment).equals(getResources().getString(R.string.pmdt_end_treatment_assessment)) ? "END OF TREATMENT ASSESSMENT" :
                                                (App.get(typeAssessment).equals(getResources().getString(R.string.pmdt_post_treatment_assessment)) ? "POST-TREATMENT ASSESSMENT" :
                                                        (App.get(typeAssessment).equals(getResources().getString(R.string.pmdt_adverse_event_assessment)) ? "ADVERSE EVENT ASSESSMENT" : "OTHER ASSESSMENT"))))))});


        if (otherAssessmentType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER ASSESSMENT", App.get(otherAssessmentType)});

        observations.add(new String[]{"FOLLOW-UP MONTH", App.get(treatmentMonth)});

        observations.add(new String[]{"TREATMENT FACILITY", App.get(treatmentFacility)});

        if(otherTreatmentFacility.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"TREATMENT FACILITY OTHER", App.get(otherTreatmentFacility)});
        }




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
                if (!result.contains("SUCCESS"))
                    return result;

                return "SUCCESS";
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
    public void refill(int encounterId) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        MySpinner spinner = (MySpinner) parent;

        if (spinner == typeAssessment.getSpinner()) {

            if (App.get(typeAssessment).equals(getResources().getString(R.string.pmdt_other)))
                otherAssessmentType.setVisibility(View.VISIBLE);
            else
                otherAssessmentType.setVisibility(View.GONE);

            if (App.get(typeAssessment).equals(getResources().getString(R.string.pmdt_planened_monthly_assessment)))
                treatmentMonth.setVisibility(View.VISIBLE);
            else
                treatmentMonth.setVisibility(View.GONE);

        } else if (spinner == treatmentFacility.getSpinner()) {

            if (App.get(treatmentFacility).equals(getResources().getString(R.string.pmdt_other)))
                otherTreatmentFacility.setVisibility(View.VISIBLE);
            else
                otherTreatmentFacility.setVisibility(View.GONE);

        } else if (spinner == patientCnicOwner.getSpinner()) {

            if (App.get(patientCnicOwner).equals(getResources().getString(R.string.pmdt_other))) {
                otherPatientCnicOwner.setVisibility(View.VISIBLE);
                namePatientCnicOwner.setVisibility(View.VISIBLE);
            } else {
                otherPatientCnicOwner.setVisibility(View.GONE);
                namePatientCnicOwner.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == patientSubmitSputumSample.getRadioGroup()) {

            if (App.get(patientSubmitSputumSample).equals(getResources().getString(R.string.no))) {
                reasonNotSubmittedSample.setVisibility(View.VISIBLE);
            } else {
                reasonNotSubmittedSample.setVisibility(View.GONE);
            }
        } else if (group == reasonNotSubmittedSample.getRadioGroup()) {

            if (App.get(reasonNotSubmittedSample).equals(getResources().getString(R.string.pmdt_other))) {
                otherReasonNotSubmittedSample.setVisibility(View.VISIBLE);
            } else {
                otherReasonNotSubmittedSample.setVisibility(View.GONE);
            }
        } else if (group == patientOwnCnic.getRadioGroup()) {

            if (App.get(patientOwnCnic).equals(getResources().getString(R.string.no))) {
                patientCnicOwner.setVisibility(View.VISIBLE);

                if (App.get(patientCnicOwner).equals(getResources().getString(R.string.pmdt_other)))
                    otherPatientCnicOwner.setVisibility(View.VISIBLE);
                else
                    otherPatientCnicOwner.setVisibility(View.GONE);

            } else {
                patientCnicOwner.setVisibility(View.GONE);
                otherPatientCnicOwner.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        autopopulateData();


    }

    public void autopopulateData(){

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
                String treatmentDate = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PMDT_TREATMENT_INITIATION, "TREATMENT START DATE");
//                String nationalDrTbNumber = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PMDT_TREATMENT_INITIATION, "NATIONAL DR-TB TREATMENT REGISTRATION NUMBER");

                if (treatmentDate != null && !treatmentDate.isEmpty())
                    result.put("TREATMENT START DATE", treatmentDate);
//                if (nationalDrTbNumber != null && !nationalDrTbNumber.isEmpty())
//                    result.put("NATIONAL DR-TB NUMBER", nationalDrTbNumber);

                return result;
            }

            @Override
            protected void onProgressUpdate(String... values) {
            }


            @Override
            protected void onPostExecute(HashMap<String, String> result) {
                super.onPostExecute(result);
                loading.dismiss();

                if (result.get("TREATMENT START DATE") == null || result.get("TREATMENT START DATE").isEmpty()) {

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.pmdt_treatment_initiation_missing));
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

                    return;
                }

                else if (result.get("TREATMENT START DATE") != null && !result.get("TREATMENT START DATE").isEmpty()) {

                    String format = "";
                    String treatmentStartDate = result.get("TREATMENT START DATE");

                    if (treatmentStartDate.contains("/")) {
                        format = "dd/MM/yyyy";
                    } else {
                        format = "yyyy-MM-dd";
                    }
                    Date convertedDate = App.stringToDate(treatmentStartDate, format);
                    Calendar treatmentDateCalender = App.getCalendar(convertedDate);
                    int diffYear = formDateCalendar.get(Calendar.YEAR) - treatmentDateCalender.get(Calendar.YEAR);
                    int diffMonth = diffYear * 12 + formDateCalendar.get(Calendar.MONTH) - treatmentDateCalender.get(Calendar.MONTH);

                    String [] monthArray = new String[diffMonth + 1];

                    for(int i =0 ; i <= diffMonth ; i++){
                        monthArray[i] = String.valueOf(i);
                    }

                    treatmentMonth.getSpinner().setSpinnerData(monthArray);
                }
            }
        };
        autopopulateFormTask.execute("");
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