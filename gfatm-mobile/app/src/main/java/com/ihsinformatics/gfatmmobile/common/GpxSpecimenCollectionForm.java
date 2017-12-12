package com.ihsinformatics.gfatmmobile.common;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
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
 * Created by Haris on 1/19/2017.
 */

public class GpxSpecimenCollectionForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;
    Boolean emptyError = false;

    // Views...
    TitledButton formDate;
    TitledButton sampleSubmissionDate;
    TitledRadioGroup testContextStatus;
    TitledSpinner monthOfTreatment;
    TitledRadioGroup tbCategory;
    TitledRadioGroup baselineRepeatReason;
    TitledEditText baselineRepeatReasonOther;
    TitledRadioGroup sampleType;
    TitledRadioGroup pulmonaryType;
    TitledRadioGroup specimenSource;
    TitledEditText specimenSourceOther;
    TitledEditText orderId;
    TitledRadioGroup sampleCollectedFrom;
    TitledEditText outsideSpecify;

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
        FORM_NAME = Forms.FAST_GXP_SPECIMEN_COLLECTION_FORM;
        FORM = Forms.fastGpxSpecimenCollectionForm;

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

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        sampleCollectedFrom = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_where_was_the_sample_collected_from), getResources().getStringArray(R.array.fast_within_outside_site_list), "", App.VERTICAL, App.VERTICAL, true);
        outsideSpecify = new TitledEditText(context, null, getResources().getString(R.string.fast_specify_outside), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        sampleSubmissionDate = new TitledButton(context, null, getResources().getString(R.string.fast_day_was_the_sample_submitted), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.VERTICAL);
        testContextStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_point_test_being_done), getResources().getStringArray(R.array.fast_test_being_done_list), getResources().getString(R.string.fast_baseline_new), App.VERTICAL, App.VERTICAL);
        monthOfTreatment = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_at_what_point_test_being_done), getResources().getStringArray(R.array.fast_number_list), "", App.HORIZONTAL);
        updateFollowUpMonth();
        tbCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_baseline_test_specify_patient_category), getResources().getStringArray(R.array.fast_patient_category_list), getResources().getString(R.string.fast_category_1), App.VERTICAL, App.VERTICAL);
        baselineRepeatReason = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_repeating_test_if_baseline_repeat), getResources().getStringArray(R.array.fast_repeating_test_if_baseline_repeat_list), getResources().getString(R.string.fast_rif_resistant), App.VERTICAL, App.VERTICAL);
        baselineRepeatReasonOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        baselineRepeatReasonOther.setVisibility(View.GONE);
        sampleType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_specimen_type), getResources().getStringArray(R.array.fast_specimen_type_list), getResources().getString(R.string.fast_pulmonary), App.VERTICAL, App.VERTICAL);
        pulmonaryType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_pulmonary), getResources().getStringArray(R.array.fast_pulmonary_list), getResources().getString(R.string.fast_pulmonary), App.HORIZONTAL, App.VERTICAL);
        specimenSource = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_specimen_come_from), getResources().getStringArray(R.array.fast_specimen_come_from_list_updated), getResources().getString(R.string.fast_gastric_aspirate), App.VERTICAL, App.VERTICAL);
        specimenSourceOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        // cartridgeId = new TitledEditText(context, null, getResources().getString(R.string.fast_test_id), "", "", 20,RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        orderId = new TitledEditText(context, null, getResources().getString(R.string.order_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        // orderId.setLongClickable(false);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), sampleSubmissionDate.getButton(), testContextStatus.getRadioGroup(), monthOfTreatment.getSpinner(), tbCategory.getRadioGroup(),
                baselineRepeatReason.getRadioGroup(), baselineRepeatReasonOther.getEditText(), sampleType.getRadioGroup(), specimenSource.getRadioGroup(), specimenSourceOther.getEditText(), orderId.getEditText(), sampleCollectedFrom.getRadioGroup(),pulmonaryType.getRadioGroup(),
                outsideSpecify.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, sampleCollectedFrom, outsideSpecify ,sampleSubmissionDate, testContextStatus, monthOfTreatment, tbCategory, baselineRepeatReason, baselineRepeatReasonOther, sampleType,pulmonaryType,specimenSource, specimenSourceOther, orderId}};

        formDate.getButton().setOnClickListener(this);
        sampleSubmissionDate.getButton().setOnClickListener(this);
        testContextStatus.getRadioGroup().setOnCheckedChangeListener(this);
        tbCategory.getRadioGroup().setOnCheckedChangeListener(this);
        baselineRepeatReason.getRadioGroup().setOnCheckedChangeListener(this);
        sampleType.getRadioGroup().setOnCheckedChangeListener(this);
        specimenSource.getRadioGroup().setOnCheckedChangeListener(this);
        sampleCollectedFrom.getRadioGroup().setOnCheckedChangeListener(this);
        pulmonaryType.getRadioGroup().setOnCheckedChangeListener(this);

        resetViews();

    }

    public void updateFollowUpMonth() {

        String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-" + "Treatment Initiation", "REGISTRATION DATE");
        String format = "";
        String[] monthArray;

        if (treatmentDate == null) {
            monthArray = new String[1];
            monthArray[0] = "0";
            monthOfTreatment.getSpinner().setSpinnerData(monthArray);
        } else {
            if (treatmentDate.contains("/")) {
                format = "dd/MM/yyyy";
            } else {
                format = "yyyy-MM-dd";
            }
            Date convertedDate = App.stringToDate(treatmentDate, format);
            Calendar treatmentDateCalender = App.getCalendar(convertedDate);
            int diffYear = formDateCalendar.get(Calendar.YEAR) - treatmentDateCalender.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + formDateCalendar.get(Calendar.MONTH) - treatmentDateCalender.get(Calendar.MONTH);

            if (diffMonth == 0) {
                monthArray = new String[1];
                monthArray[0] = "1";
                monthOfTreatment.getSpinner().setSpinnerData(monthArray);
            } else if (diffMonth > 24) {
                monthArray = new String[24];
                for (int i = 0; i < 24; i++) {
                    monthArray[i] = String.valueOf(i + 1);
                }
                monthOfTreatment.getSpinner().setSpinnerData(monthArray);
            } else {
                monthArray = new String[diffMonth];
                for (int i = 0; i < diffMonth; i++) {
                    monthArray[i] = String.valueOf(i + 1);
                }
                monthOfTreatment.getSpinner().setSpinnerData(monthArray);
            }
        }
    }

    public void updateDisplay() {
        Calendar treatDateCalender = null;
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-" + "Treatment Initiation", "REGISTRATION DATE");

            if (treatmentDate != null) {
                treatDateCalender = App.getCalendar(App.stringToDate(treatmentDate, "yyyy-MM-dd"));
            }

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
            } else if (treatDateCalender != null) {
                if (formDateCalendar.before(treatDateCalender)) {
                    formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                    snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_cannot_be_before_treatment_initiation_form), Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();

                    formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                } else {
                    formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                }
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }

        if (!(sampleSubmissionDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = sampleSubmissionDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                sampleSubmissionDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                sampleSubmissionDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else if (secondDateCalendar.before(formDateCalendar)) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_sample_date_cannot_be_before_form_date), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                sampleSubmissionDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                sampleSubmissionDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }

        updateFollowUpMonth();
        formDate.getButton().setEnabled(true);
        sampleSubmissionDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = false;

        if (specimenSourceOther.getVisibility() == View.VISIBLE && specimenSourceOther.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            specimenSourceOther.getEditText().setError(getString(R.string.empty_field));
            specimenSourceOther.getEditText().requestFocus();
            error = true;
        }
        if (outsideSpecify.getVisibility() == View.VISIBLE && outsideSpecify.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            outsideSpecify.getEditText().setError(getString(R.string.empty_field));
            outsideSpecify.getEditText().requestFocus();
            error = true;
        }
        if (pulmonaryType.getVisibility() == View.VISIBLE && App.get(pulmonaryType).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            pulmonaryType.getQuestionView().setError(getString(R.string.empty_field));
            pulmonaryType.getQuestionView().requestFocus();
            error = true;
        }
        if (sampleCollectedFrom.getVisibility() == View.VISIBLE && App.get(sampleCollectedFrom).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            sampleCollectedFrom.getQuestionView().setError(getString(R.string.empty_field));
            sampleCollectedFrom.getQuestionView().requestFocus();
            emptyError = true;
            error = true;
        }


      /*  if (cartridgeId.getVisibility() == View.VISIBLE && App.get(cartridgeId).length()!=10) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cartridgeId.getEditText().setError(getString(R.string.invalid_value));
            cartridgeId.getEditText().requestFocus();
            error = true;
        }*/

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            if (!emptyError)
                alertDialog.setMessage(getString(R.string.form_error));
            else
                alertDialog.setMessage(getString(R.string.fast_required_field_error));
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

        if (sampleCollectedFrom.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SAMPLE COLLECTED FROM", App.get(sampleCollectedFrom).equals(getResources().getString(R.string.fast_within_site)) ? "WITHIN THE SITE" : "OUTSIDE THE SITE"});

        if (outsideSpecify.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SAMPLE COLLECTED OUTSIDE THE SITE OTHER", App.get(outsideSpecify)});

        observations.add(new String[]{"SPECIMEN SUBMISSION DATE", App.getSqlDateTime(secondDateCalendar)});


        if (testContextStatus.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TEST CONTEXT STATUS", App.get(testContextStatus).equals(getResources().getString(R.string.fast_baseline_new)) ? "BASELINE" :
                    (App.get(testContextStatus).equals(getResources().getString(R.string.fast_baseline_repeat)) ? "BASELINE REPEAT" : "REGULAR FOLLOW UP")});

        if (monthOfTreatment.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FOLLOW-UP MONTH", monthOfTreatment.getSpinner().getSelectedItem().toString()});


        if (tbCategory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TB CATEGORY", App.get(tbCategory).equals(getResources().getString(R.string.fast_category_1)) ? "CATEGORY I TUBERCULOSIS" : "CATEGORY II TUBERCULOSIS"});

        if (baselineRepeatReason.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON FOR BASELINE REPEAT TEST", App.get(baselineRepeatReason).equals(getResources().getString(R.string.fast_rif_resistant)) ? "RIF RESISTANT POSITIVE" :
                    (App.get(baselineRepeatReason).equals(getResources().getString(R.string.fast_error_invalid_no_result)) ? "INVALID" :
                            (App.get(baselineRepeatReason).equals(getResources().getString(R.string.fast_indeterminate)) ? "INDETERMINATE" :
                                    "OTHER REASON FOR REPEATING TEST"))});

        if (baselineRepeatReasonOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON FOR REPEATING TEST", App.get(baselineRepeatReasonOther)});


        if (sampleType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SPECIMEN TYPE", App.get(sampleType).equals(getResources().getString(R.string.fast_pulmonary)) ? "PULMONARY" : "EXTRA-PULMONARY TUBERCULOSIS"});

        if (specimenSource.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SPECIMEN SOURCE", App.get(specimenSource).equals(getResources().getString(R.string.fast_gastric_aspirate)) ? "GASTRIC ASPIRATE" :
                    (App.get(specimenSource).equals(getResources().getString(R.string.fast_pleural_fluid)) ? "PLEURAL EFFUSION" :
                            (App.get(specimenSource).equals(getResources().getString(R.string.fast_pus)) ? "PUS" : "OTHER SPECIMEN SOURCE"))});

        if (specimenSourceOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER SPECIMEN SOURCE", App.get(specimenSourceOther)});

        observations.add(new String[]{"ORDER ID", App.get(orderId)});

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

                String result = serverService.saveEncounterAndObservation("FAST-GXP Specimen Collection", FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
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
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {
            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("ORDER ID")) {
                orderId.getEditText().setText(obs[0][1]);
                orderId.getEditText().setKeyListener(null);
                orderId.getEditText().setFocusable(false);
            }

            else if (obs[0][0].equals("SAMPLE COLLECTED FROM")) {

                for (RadioButton rb : sampleCollectedFrom.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_within_site)) && obs[0][1].equals("WITHIN THE SITE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_outside_site)) && obs[0][1].equals("OUTSIDE THE SITE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                sampleCollectedFrom.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("SAMPLE COLLECTED OUTSIDE THE SITE OTHER")) {
                outsideSpecify.getEditText().setText(obs[0][1]);
                outsideSpecify.setVisibility(View.VISIBLE);
            }  else if (obs[0][0].equals("SPECIMEN SUBMISSION DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                sampleSubmissionDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                sampleSubmissionDate.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TEST CONTEXT STATUS")) {

                for (RadioButton rb : testContextStatus.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_baseline_new)) && obs[0][1].equals("BASELINE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_baseline_repeat)) && obs[0][1].equals("BASELINE REPEAT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_followup_test)) && obs[0][1].equals("REGULAR FOLLOW UP")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                testContextStatus.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                monthOfTreatment.getSpinner().selectValue(obs[0][1]);
            } else if (obs[0][0].equals("TB CATEGORY")) {

                for (RadioButton rb : tbCategory.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_category_1)) && obs[0][1].equals("CATEGORY I TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_category_2)) && obs[0][1].equals("CATEGORY II TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                tbCategory.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REASON FOR BASELINE REPEAT TEST")) {

                for (RadioButton rb : baselineRepeatReason.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_rif_resistant)) && obs[0][1].equals("RIF RESISTANT POSITIVE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_error_invalid_no_result)) && obs[0][1].equals("INVALID")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.fast_other_title)) && obs[0][1].equals("OTHER REASON FOR REPEATING TEST")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                baselineRepeatReason.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER REASON FOR REPEATING TEST")) {
                baselineRepeatReasonOther.getEditText().setText(obs[0][1]);
                baselineRepeatReasonOther.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("SPECIMEN TYPE")) {

                for (RadioButton rb : sampleType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_pulmonary)) && obs[0][1].equals("PULMONARY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_extra_pulmonary)) && obs[0][1].equals("EXTRA-PULMONARY TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                sampleType.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("SPECIMEN SOURCE")) {

                for (RadioButton rb : specimenSource.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_gastric_aspirate)) && obs[0][1].equals("GASTRIC ASPIRATE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_pleural_fluid)) && obs[0][1].equals("PLEURAL EFFUSION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_pus)) && obs[0][1].equals("PUS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_other_title)) && obs[0][1].equals("OTHER SPECIMEN SOURCE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                specimenSource.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER SPECIMEN SOURCE")) {
                specimenSourceOther.getEditText().setText(obs[0][1]);
                specimenSourceOther.setVisibility(View.VISIBLE);
            }

        }

    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            formDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
        }

        if (view == sampleSubmissionDate.getButton()) {
            sampleSubmissionDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        sampleSubmissionDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        baselineRepeatReason.setVisibility(View.GONE);
        monthOfTreatment.setVisibility(View.GONE);
        specimenSource.setVisibility(View.GONE);
        specimenSourceOther.setVisibility(View.GONE);
        if(App.get(sampleType).equals(R.string.fast_pulmonary)){
            pulmonaryType.setVisibility(View.VISIBLE);
        }

        if(App.get(sampleCollectedFrom).equals(getResources().getString(R.string.fast_outside_site))){
            outsideSpecify.setVisibility(View.VISIBLE);
        }
        Date nowDate = new Date();
        orderId.getEditText().setText(App.getSqlDateTime(nowDate));
        orderId.getEditText().setKeyListener(null);
        orderId.getEditText().setFocusable(false);


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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if (radioGroup == testContextStatus.getRadioGroup()) {
            if (testContextStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_baseline_new))) {
                tbCategory.setVisibility(View.VISIBLE);
            } else {
                tbCategory.setVisibility(View.GONE);
            }

            if (testContextStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_baseline_repeat))) {
                baselineRepeatReason.setVisibility(View.VISIBLE);
                if (baselineRepeatReason.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_others))) {
                    baselineRepeatReasonOther.setVisibility(View.VISIBLE);
                } else {
                    baselineRepeatReasonOther.setVisibility(View.GONE);
                }

            } else {
                baselineRepeatReason.setVisibility(View.GONE);
                baselineRepeatReasonOther.setVisibility(View.GONE);
            }

            if (testContextStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_followup_test))) {
                monthOfTreatment.setVisibility(View.VISIBLE);
            } else {
                monthOfTreatment.setVisibility(View.GONE);
            }
        } else if (radioGroup == sampleType.getRadioGroup()) {
            if (sampleType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_extra_pulmonary))) {
                specimenSource.setVisibility(View.VISIBLE);
                if (specimenSource.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_other_title))) {
                    specimenSourceOther.setVisibility(View.VISIBLE);
                }
                pulmonaryType.setVisibility(View.GONE);
            } else if (sampleType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_pulmonary))) {
                pulmonaryType.setVisibility(View.VISIBLE);

                specimenSource.setVisibility(View.GONE);
                specimenSourceOther.setVisibility(View.GONE);
            } else {
                pulmonaryType.setVisibility(View.GONE);
                specimenSource.setVisibility(View.GONE);
                specimenSourceOther.setVisibility(View.GONE);
            }
        } else if (radioGroup == specimenSource.getRadioGroup()) {
            if (specimenSource.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_other_title))) {
                specimenSourceOther.setVisibility(View.VISIBLE);
            } else {
                specimenSourceOther.setVisibility(View.GONE);
            }
        } else if (radioGroup == baselineRepeatReason.getRadioGroup()) {
            if (baselineRepeatReason.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_others))) {
                baselineRepeatReasonOther.setVisibility(View.VISIBLE);
            } else {
                baselineRepeatReasonOther.setVisibility(View.GONE);
            }
        } else if (radioGroup == sampleCollectedFrom.getRadioGroup()) {
            sampleCollectedFrom.getQuestionView().setError(null);
            if (sampleCollectedFrom.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_outside_site))) {
                outsideSpecify.setVisibility(View.VISIBLE);
            } else {
                outsideSpecify.setVisibility(View.GONE);
            }
        }
        else if (radioGroup == pulmonaryType.getRadioGroup()) {
            pulmonaryType.getQuestionView().setError(null);
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
