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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
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
 * Created by Haris on 2/21/2017.
 */

public class TreatmentAdherenceForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledRadioGroup patient_contacted;
    TitledRadioGroup reason_patient_not_contacted;
    TitledEditText reason_patient_not_contacted_other;
    TitledRadioGroup taking_medication;
    TitledEditText medication_missed_days;
    TitledRadioGroup reason_missed_dose;
    TitledEditText reason_missed_dose_other;
    TitledRadioGroup adverse_events_reported;
    TitledCheckBoxes adverse_events;
    TitledEditText other_adverse_event;
    TitledEditText patient_comments;
    TitledEditText doctor_notes;
    TitledEditText treatment_plan;
    TitledRadioGroup clinician_informed;
    TitledRadioGroup facility_visit_scheduled;
    TitledButton facility_visit_date;
    TitledSpinner facility_scheduled;


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

        pageCount = 1;
        formName = Forms.TREATMENT_ADHERENCE;
        form = Forms.treatmentAdherence;

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
                ScrollView scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
        String id = serverService.getLocationTagId("Treatment(TB)");
        String query = "";
        if(query != null){

            query = " and tags LIKE '%," + id + ",%'";
        }

        final Object[][] locations = serverService.getAllLocationsFromLocalDBByQuery("(location_type = 'Clinic' or location_type = 'Hospital')" + query);
        String[] locationArray = new String[locations.length + 1];
        locationArray[0] = "";
        int j = 1;
        for (int i = 0; i < locations.length; i++) {
            locationArray[j] = String.valueOf(locations[i][16]);
            j++;
        }
        patient_contacted = new TitledRadioGroup(context, null, getResources().getString(R.string.common_patient_contacted), getResources().getStringArray(R.array.common_patient_contacted_options), null, App.VERTICAL, App.VERTICAL, true);
        reason_patient_not_contacted = new TitledRadioGroup(context, null, getResources().getString(R.string.common_reason_patient_not_contacted), getResources().getStringArray(R.array.common_reason_patient_not_contacted_options), "", App.VERTICAL, App.VERTICAL, true);
        reason_patient_not_contacted_other = new TitledEditText(context, null, getResources().getString(R.string.common_reason_patient_not_contacted_other_specifiy), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        taking_medication = new TitledRadioGroup(context, null, getResources().getString(R.string.common_taking_medication), getResources().getStringArray(R.array.common_taking_medication_options), "", App.VERTICAL, App.VERTICAL, true);
        medication_missed_days = new TitledEditText(context, null, getResources().getString(R.string.common_medication_missed_days), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        reason_missed_dose = new TitledRadioGroup(context, null, getResources().getString(R.string.common_reason_missed_dose), getResources().getStringArray(R.array.common_reason_missed_dose_options), "", App.VERTICAL, App.VERTICAL, true);
        reason_missed_dose_other = new TitledEditText(context, null, getResources().getString(R.string.common_reason_missed_dose_other_specify), "", "", 500, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        adverse_events_reported = new TitledRadioGroup(context, null, getResources().getString(R.string.common_adverse_events_reported), getResources().getStringArray(R.array.common_adverse_events_reported_options), "", App.VERTICAL, App.VERTICAL, true);
        adverse_events = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_adverse_events), getResources().getStringArray(R.array.common_adverse_events_options), null, App.VERTICAL, App.VERTICAL, true);
        other_adverse_event = new TitledEditText(context, null, getResources().getString(R.string.common_other_adverse_event_specify), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        patient_comments = new TitledEditText(context, null, getResources().getString(R.string.common_patient_comments), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        doctor_notes = new TitledEditText(context, null, getResources().getString(R.string.common_doctor_notes), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        treatment_plan = new TitledEditText(context, null, getResources().getString(R.string.common_treatment_plan), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        clinician_informed = new TitledRadioGroup(context, null, getResources().getString(R.string.common_clinician_informed), getResources().getStringArray(R.array.common_clinician_informed_options), getString(R.string.no), App.VERTICAL, App.VERTICAL, true);
        facility_visit_scheduled = new TitledRadioGroup(context, null, getResources().getString(R.string.common_facility_visit_scheduled), getResources().getStringArray(R.array.common_facility_visit_scheduled_options), getString(R.string.no), App.VERTICAL, App.VERTICAL, true);
        facility_visit_date = new TitledButton(context, null, getResources().getString(R.string.common_facility_visit_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        facility_visit_date.setTag("facilityVisitDate");
        facility_scheduled = new TitledSpinner(context, "", getResources().getString(R.string.common_facility_scheduled), locationArray, "", App.HORIZONTAL);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), patient_contacted.getRadioGroup(), reason_patient_not_contacted.getRadioGroup(), reason_patient_not_contacted_other.getEditText(),
                taking_medication.getRadioGroup(), medication_missed_days.getEditText(), reason_missed_dose.getRadioGroup(), reason_missed_dose_other.getEditText(),
                adverse_events_reported.getRadioGroup(), adverse_events, other_adverse_event.getEditText(), patient_comments.getEditText(), doctor_notes.getEditText(), treatment_plan.getEditText(),
                clinician_informed.getRadioGroup(), facility_visit_scheduled.getRadioGroup(), facility_visit_date.getButton(), facility_scheduled.getSpinner()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, patient_contacted, reason_patient_not_contacted, reason_patient_not_contacted_other
                        , taking_medication, medication_missed_days, reason_missed_dose, reason_missed_dose_other, adverse_events_reported, adverse_events, other_adverse_event,
                        patient_comments, doctor_notes, treatment_plan, clinician_informed, facility_visit_scheduled, facility_visit_date, facility_scheduled}};

        formDate.getButton().setOnClickListener(this);
        patient_contacted.getRadioGroup().setOnCheckedChangeListener(this);
        reason_patient_not_contacted.getRadioGroup().setOnCheckedChangeListener(this);
        taking_medication.getRadioGroup().setOnCheckedChangeListener(this);
        reason_missed_dose.getRadioGroup().setOnCheckedChangeListener(this);
        adverse_events_reported.getRadioGroup().setOnCheckedChangeListener(this);
        facility_visit_scheduled.getRadioGroup().setOnCheckedChangeListener(this);
        facility_visit_date.getButton().setOnClickListener(this);
        for (CheckBox cb : adverse_events.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        resetViews();
        medication_missed_days.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int missedDays = 0;
                try {
                    missedDays = Integer.parseInt(medication_missed_days.getEditText().getText().toString());
                } catch (Exception e) {
                    missedDays = 0;
                }
                if (missedDays == 0) {
                    medication_missed_days.getEditText().setError(getString(R.string.non_zero));
                }

            }
        });

    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();
        String formDa = formDate.getButton().getText().toString();
        String personDOB = App.getPatient().getPerson().getBirthdate();
        personDOB = personDOB.substring(0, 10);

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {


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

        if (secondDateCalendar.after(formDateCalendar)) {
            facility_visit_date.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            Date date1 = App.stringToDate(formDate.getButton().getText().toString(), "EEEE, MMM dd,yyyy");
            secondDateCalendar = App.getCalendar(date1);
        } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
            secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
            snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
            TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            tv.setMaxLines(2);
            snackbar.show();
            facility_visit_date.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        } else
            facility_visit_date.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        formDate.getButton().setEnabled(true);
        facility_visit_date.getButton().setEnabled(true);

    }

    @Override
    public boolean validate() {
        Boolean error = false;
        View view = null;
        if (patient_contacted.getVisibility() == View.VISIBLE && App.get(patient_contacted).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            patient_contacted.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            patient_contacted.getQuestionView().setError(null);
        }

        if (reason_patient_not_contacted.getVisibility() == View.VISIBLE && App.get(reason_patient_not_contacted).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reason_patient_not_contacted.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            reason_patient_not_contacted.getQuestionView().setError(null);
        }
        if (reason_patient_not_contacted_other.getVisibility() == View.VISIBLE && reason_patient_not_contacted_other.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reason_patient_not_contacted_other.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }
        if (taking_medication.getVisibility() == View.VISIBLE && App.get(taking_medication).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            taking_medication.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            taking_medication.getQuestionView().setError(null);
        }

        if (medication_missed_days.getVisibility() == View.VISIBLE && medication_missed_days.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            medication_missed_days.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }

        if (reason_missed_dose.getVisibility() == View.VISIBLE && App.get(reason_missed_dose).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reason_missed_dose.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            reason_missed_dose.getQuestionView().setError(null);
        }
        if (reason_missed_dose_other.getVisibility() == View.VISIBLE && reason_missed_dose_other.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reason_missed_dose_other.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }

        if (adverse_events_reported.getVisibility() == View.VISIBLE && App.get(adverse_events_reported).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            adverse_events_reported.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            adverse_events_reported.getQuestionView().setError(null);
        }
        Boolean flag = false;
        if (adverse_events.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : adverse_events.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                adverse_events.getQuestionView().setError(getString(R.string.empty_field));
                adverse_events.getQuestionView().requestFocus();
                view = adverse_events;
                error = true;
            } else {
                adverse_events.getQuestionView().setError(null);
            }
        }


        if (other_adverse_event.getVisibility() == View.VISIBLE && other_adverse_event.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            other_adverse_event.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }


        if (patient_comments.getVisibility() == View.VISIBLE && patient_comments.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            patient_comments.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }


        if (doctor_notes.getVisibility() == View.VISIBLE && doctor_notes.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            doctor_notes.getEditText().setError(getString(R.string.empty_field));
            error = true;
        }


        if (clinician_informed.getVisibility() == View.VISIBLE && App.get(clinician_informed).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            clinician_informed.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            clinician_informed.getQuestionView().setError(null);
        }

        if (facility_visit_scheduled.getVisibility() == View.VISIBLE && App.get(facility_visit_scheduled).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            facility_visit_scheduled.getQuestionView().setError(getResources().getString(R.string.empty_field));
            error = true;
        } else {
            facility_visit_scheduled.getQuestionView().setError(null);
        }


        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            //  DrawableCompat.setTint(clearIcon, color);
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


        if (patient_contacted.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CONTACT TO THE PATIENT", App.get(patient_contacted).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(patient_contacted).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(patient_contacted).equals(getResources().getString(R.string.common_patient_contacted_yes_not_interested)) ? "YES, BUT NOT INTERESTED" : "DIED"))});

        if (reason_patient_not_contacted.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"UNABLE TO CONTACT THE PATIENT", App.get(reason_patient_not_contacted).equals(getResources().getString(R.string.common_reason_patient_not_contacted_phone_switched_off)) ? "PHONE SWITCHED OFF" :
                    (App.get(reason_patient_not_contacted).equals(getResources().getString(R.string.common_reason_patient_not_contacted_patient_not_resp)) ? "PATIENT DID NOT RECEIVE CALL" :
                            (App.get(reason_patient_not_contacted).equals(getResources().getString(R.string.common_reason_patient_not_contacted_invalid)) ? "INCORRECT CONTACT NUMBER" :
                                    (App.get(reason_patient_not_contacted).equals(getResources().getString(R.string.common_reason_patient_not_contacted_wrong)) ? "WRONG NUMBER" : "OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT")))});

        if (reason_patient_not_contacted_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT", reason_patient_not_contacted_other.getEditText().getText().toString().trim()});

        if (taking_medication.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CURRENTLY TAKING MEDICATION", App.get(taking_medication).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if (medication_missed_days.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"NUMBER OF DAYS MEDICATION WAS MISSED", medication_missed_days.getEditText().getText().toString().trim()});


        if (reason_missed_dose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON MISSED DOSE", App.get(reason_missed_dose).equals(getResources().getString(R.string.common_reason_missed_dose_adverse)) ? "ADVERSE EVENTS" :
                    (App.get(reason_missed_dose).equals(getResources().getString(R.string.common_reason_missed_dose_dont_money)) ? "LACK OF MONEY FOR TRANSPORT" :
                            (App.get(reason_missed_dose).equals(getResources().getString(R.string.common_reason_missed_dose_out_city)) ? "OUT OF CITY" :
                                    (App.get(reason_missed_dose).equals(getResources().getString(R.string.common_reason_missed_dose_misdiagnose)) ? "PATIENT OR ATTENDANT CLAIMS MISDIAGNOSED" :
                                            (App.get(reason_missed_dose).equals(getResources().getString(R.string.common_reason_missed_dose_complete)) ? "PATIENT OR ATTENDANT CLAIMS TREATMENT COMPLETE" : "OTHER REASON MISSED DOSE"))))});

        if (reason_missed_dose_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON MISSED DOSE", reason_missed_dose_other.getEditText().getText().toString().trim()});

        if (adverse_events_reported.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ADVERSE EVENTS REPORTED", App.get(adverse_events_reported).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (adverse_events.getVisibility() == View.VISIBLE) {
            String diabetes_treatmeant_String = "";
            for (CheckBox cb : adverse_events.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_joint)))
                    diabetes_treatmeant_String = diabetes_treatmeant_String + "JOINT PAIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_headache)))
                    diabetes_treatmeant_String = diabetes_treatmeant_String + "HEADACHE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_skin)))
                    diabetes_treatmeant_String = diabetes_treatmeant_String + "RASH" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_nausea)))
                    diabetes_treatmeant_String = diabetes_treatmeant_String + "NAUSEA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_dizziness)))
                    diabetes_treatmeant_String = diabetes_treatmeant_String + "DIZZINESS AND GIDDINESS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_vomit)))
                    diabetes_treatmeant_String = diabetes_treatmeant_String + "VOMITING" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_abdominal)))
                    diabetes_treatmeant_String = diabetes_treatmeant_String + "ABDOMINAL PAIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_appettie)))
                    diabetes_treatmeant_String = diabetes_treatmeant_String + "LOSS OF APPETITE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_visual)))
                    diabetes_treatmeant_String = diabetes_treatmeant_String + "VISUAL IMPAIRMENT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_adverse_events_others)))
                    diabetes_treatmeant_String = diabetes_treatmeant_String + "OTHER ADVERSE EVENT" + " ; ";

            }
            observations.add(new String[]{"ADVERSE EVENTS", diabetes_treatmeant_String});
        }
        if (other_adverse_event.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER ADVERSE EVENT", other_adverse_event.getEditText().getText().toString().trim()});

        if (patient_comments.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CARETAKER COMMENTS", patient_comments.getEditText().getText().toString()});

        if (doctor_notes.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CLINICIAN NOTES (TEXT)", doctor_notes.getEditText().getText().toString()});

        if (treatment_plan.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT PLAN (TEXT)", treatment_plan.getEditText().getText().toString()});

        if (clinician_informed.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CLINICIAN INFORMED", App.get(clinician_informed).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if (facility_visit_scheduled.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FACILITY VISIT SCHEDULED", App.get(facility_visit_scheduled).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(facility_visit_scheduled).equals(getResources().getString(R.string.no)) ? "NO" : "UNABLE TO CONVINCE")});

        if (facility_visit_date.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FACILITY VISIT DATE", App.getSqlDateTime(secondDateCalendar)});

        if (facility_visit_date.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FACILITY SCHEDULED", facility_scheduled.getSpinner().getSelectedItem().toString()});


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
                if (App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocallyTesting(Forms.TREATMENT_ADHERENCE, form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = "";


                result = serverService.saveEncounterAndObservationTesting(Forms.TREATMENT_ADHERENCE, form, formDateCalendar, observations.toArray(new String[][]{}), id);
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

        serverService.saveFormLocally(formName, "12345-5", formValues);*/

        return true;
    }

    @Override
    public void refill(int formId) {

        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("TIME TAKEN TO FILL form")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("CONTACT TO THE PATIENT")) {
                for (RadioButton rb : patient_contacted.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_patient_contacted_yes_not_interested)) && obs[0][1].equals("YES, BUT NOT INTERESTED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_patient_contacted_died)) && obs[0][1].equals("DIED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("UNABLE TO CONTACT THE PATIENT")) {
                for (RadioButton rb : reason_patient_not_contacted.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.common_reason_patient_not_contacted_phone_switched_off)) && obs[0][1].equals("PHONE SWITCHED OFF")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_reason_patient_not_contacted_patient_not_resp)) && obs[0][1].equals("PATIENT DID NOT RECEIVE CALL")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_reason_patient_not_contacted_invalid)) && obs[0][1].equals("INCORRECT CONTACT NUMBER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_reason_patient_not_contacted_wrong)) && obs[0][1].equals("WRONG NUMBER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_reason_patient_not_contacted_other)) && obs[0][1].equals("OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT")) {
                reason_patient_not_contacted_other.getEditText().setText(obs[0][1]);

            } else if (obs[0][0].equals("CURRENTLY TAKING MEDICATION")) {
                for (RadioButton rb : taking_medication.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("NUMBER OF DAYS MEDICATION WAS MISSED")) {
                medication_missed_days.getEditText().setText(obs[0][1]);

            } else if (obs[0][0].equals("REASON MISSED DOSE")) {
                for (RadioButton rb : reason_missed_dose.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.common_reason_missed_dose_adverse)) && obs[0][1].equals("ADVERSE EVENTS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_reason_missed_dose_dont_money)) && obs[0][1].equals("LACK OF MONEY FOR TRANSPORT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_reason_missed_dose_out_city)) && obs[0][1].equals("OUT OF CITY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_reason_missed_dose_misdiagnose)) && obs[0][1].equals("PATIENT OR ATTENDANT CLAIMS MISDIAGNOSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_reason_missed_dose_complete)) && obs[0][1].equals("PATIENT OR ATTENDANT CLAIMS TREATMENT COMPLETE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_reason_missed_dose_other)) && obs[0][1].equals("OTHER REASON MISSED DOSE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER REASON MISSED DOSE")) {
                reason_missed_dose_other.getEditText().setText(obs[0][1]);

            } else if (obs[0][0].equals("ADVERSE EVENTS REPORTED")) {
                for (RadioButton rb : adverse_events_reported.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("ADVERSE EVENTS")) {
                for (CheckBox cb : adverse_events.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_joint)) && obs[0][1].equals("JOINT PAIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_headache)) && obs[0][1].equals("HEADACHE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_skin)) && obs[0][1].equals("RASH")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_nausea)) && obs[0][1].equals("NAUSEA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_dizziness)) && obs[0][1].equals("DIZZINESS AND GIDDINESS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_vomit)) && obs[0][1].equals("VOMITING")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_abdominal)) && obs[0][1].equals("ABDOMINAL PAIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_appettie)) && obs[0][1].equals("LOSS OF APPETITE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_visual)) && obs[0][1].equals("VISUAL IMPAIRMENT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.common_adverse_events_others)) && obs[0][1].equals("OTHER ADVERSE EVENT")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER ADVERSE EVENT")) {
                other_adverse_event.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("CARETAKER COMMENTS")) {
                patient_comments.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
                doctor_notes.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("TREATMENT PLAN (TEXT)")) {
                treatment_plan.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("CLINICIAN INFORMED")) {
                for (RadioButton rb : clinician_informed.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("FACILITY VISIT SCHEDULED")) {
                for (RadioButton rb : facility_visit_scheduled.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.common_facility_visit_scheduled_unable)) && obs[0][1].equals("UNABLE TO CONVINCE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("FACILITY VISIT DATE")) {
                String thirddate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(thirddate, "yyyy-MM-dd"));
                facility_visit_date.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
            } else if (obs[0][0].equals("FACILITY SCHEDULED")) {
                facility_scheduled.getSpinner().selectValue(obs[0][1]);
            }

        }
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar,false,true, false);

            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");*/
        } else if (view == facility_visit_date.getButton()) {
            facility_visit_date.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar,true,false, true);

            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", true);
            args.putString("formDate", formDate.getButtonText());
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");*/
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getText().equals(getResources().getString(R.string.common_adverse_events_others)) && isChecked) {
            other_adverse_event.setVisibility(View.VISIBLE);
        } else {
            other_adverse_event.setVisibility(View.GONE);

        }
    }


    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        reason_patient_not_contacted.setVisibility(View.GONE);
        reason_patient_not_contacted_other.setVisibility(View.GONE);
        taking_medication.setVisibility(View.GONE);
        medication_missed_days.setVisibility(View.GONE);
        reason_missed_dose.setVisibility(View.GONE);
        reason_missed_dose_other.setVisibility(View.GONE);
        adverse_events_reported.setVisibility(View.GONE);
        adverse_events.setVisibility(View.GONE);
        other_adverse_event.setVisibility(View.GONE);
        patient_comments.setVisibility(View.GONE);
        doctor_notes.setVisibility(View.GONE);
        treatment_plan.setVisibility(View.GONE);
        clinician_informed.setVisibility(View.GONE);
        facility_visit_scheduled.setVisibility(View.GONE);
        facility_visit_date.setVisibility(View.GONE);
        facility_scheduled.setVisibility(View.GONE);
        Boolean flag = true;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);

                refill(formId);
                flag = false;

            } else bundle.putBoolean("save", false);

        }
        if (flag) {
            //HERE FOR AUTOPOPULATING OBS
//            final AsyncTask<String, String, String> autopopulateFormTaskDates = new AsyncTask<String, String, String>() {
//                @Override
//                protected String doInBackground(String... params) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            loading.setInverseBackgroundForced(true);
//                            loading.setIndeterminate(true);
//                            loading.setCancelable(false);
//                            loading.setMessage(getResources().getString(R.string.fetching_data));
//                            loading.show();
//                        }
//                    });
//
//                    latestmisseddate = serverService.getLatestObsValue(App.getPatientId(), "RETURN VISIT DATE");
//                    refrelobs = serverService.getLatestObsValue(App.getPatientId(), "Referral and Transfer", "REFERRING FACILITY NAME");
//                    return latestmisseddate;
//                }
//
//                @Override
//                protected void onProgressUpdate(String... values) {
//                }
//
//                @Override
//                protected void onPostExecute(String result) {
//                    super.onPostExecute(result);
//                    loading.dismiss();
//                    if (result != null) {
//                        secondDateCalendar.setTime(App.stringToDate(result, "yyyy-MM-dd"));
//                        missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
//                    }
//
//                    if (refrelobs != null) {
//                        health_centre.getEditText().setText(refrelobs);
//                    }
//
//                }
//            };
//            autopopulateFormTaskDates.execute("");
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == patient_contacted.getRadioGroup()) {
            if (patient_contacted.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {

                reason_patient_not_contacted.setVisibility(View.GONE);
                reason_patient_not_contacted_other.setVisibility(View.GONE);
                taking_medication.setVisibility(View.VISIBLE);
                taking_medication.getRadioGroup().clearCheck();
                medication_missed_days.setVisibility(View.GONE);
                reason_missed_dose.setVisibility(View.GONE);
                reason_missed_dose.getRadioGroup().clearCheck();
                reason_missed_dose_other.setVisibility(View.GONE);
                adverse_events_reported.setVisibility(View.VISIBLE);
                adverse_events_reported.getRadioGroup().clearCheck();
                adverse_events.setVisibility(View.GONE);
                other_adverse_event.setVisibility(View.GONE);
                patient_comments.setVisibility(View.GONE);
                doctor_notes.setVisibility(View.GONE);
                treatment_plan.setVisibility(View.VISIBLE);
                clinician_informed.setVisibility(View.GONE);
                clinician_informed.getRadioGroup().getButtons().get(1).setSelected(true);
                facility_visit_scheduled.setVisibility(View.GONE);
                facility_visit_scheduled.getRadioGroup().getButtons().get(1).setSelected(true);
                facility_visit_date.setVisibility(View.GONE);
                facility_scheduled.setVisibility(View.GONE);



   /*             reason_patient_not_contacted.setVisibility(View.GONE);
                reason_patient_not_contacted_other.setVisibility(View.GONE);
                taking_medication.setVisibility(View.VISIBLE);
                if (taking_medication.getRadioGroup().getSelectedValue().equals(getString(R.string.no))) {
                    medication_missed_days.setVisibility(View.VISIBLE);
                    reason_missed_dose.setVisibility(View.VISIBLE);
                    if (reason_missed_dose.getRadioGroup().getSelectedValue().equals(getString(R.string.common_reason_missed_dose_other))) {
                        reason_missed_dose_other.setVisibility(View.VISIBLE);

                    } else {
                        reason_missed_dose_other.setVisibility(View.GONE);
                    }

                } else {
                    medication_missed_days.setVisibility(View.GONE);
                    reason_missed_dose.setVisibility(View.GONE);
                    reason_missed_dose_other.setVisibility(View.GONE);

                }
                adverse_events_reported.setVisibility(View.VISIBLE);
                if (reason_missed_dose.getRadioGroup().getSelectedValue().equals(getString(R.string.common_reason_missed_dose_adverse)) ||
                        adverse_events_reported.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                    adverse_events.setVisibility(View.VISIBLE);
                    clinician_informed.setVisibility(View.VISIBLE);

                } else {
                    adverse_events.setVisibility(View.GONE);
                    other_adverse_event.setVisibility(View.GONE);
                    clinician_informed.setVisibility(View.GONE);
                }
                patient_comments.setVisibility(View.GONE);
                doctor_notes.setVisibility(View.GONE);
                treatment_plan.setVisibility(View.VISIBLE);
                if (taking_medication.getRadioGroup().getSelectedValue().equals(getString(R.string.no))) {
                    facility_visit_scheduled.setVisibility(View.VISIBLE);
                    if (facility_visit_scheduled.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                        facility_visit_date.setVisibility(View.VISIBLE);
                        facility_scheduled.setVisibility(View.VISIBLE);
                    } else {
                        facility_visit_date.setVisibility(View.GONE);
                        facility_scheduled.setVisibility(View.GONE);
                    }
                } else {
                    facility_visit_scheduled.setVisibility(View.GONE);
                    facility_visit_date.setVisibility(View.GONE);
                    facility_scheduled.setVisibility(View.GONE);
                }*/

            } else if (patient_contacted.getRadioGroup().getSelectedValue().equals(getString(R.string.common_patient_contacted_yes_not_interested)) ||
                    patient_contacted.getRadioGroup().getSelectedValue().equals(getString(R.string.common_patient_contacted_died)) || patient_contacted.getRadioGroup().getSelectedValue().equals(getString(R.string.pet_patient_withdrew_consent))) {
                reason_patient_not_contacted.setVisibility(View.GONE);
                reason_patient_not_contacted_other.setVisibility(View.GONE);
                taking_medication.setVisibility(View.GONE);
                medication_missed_days.setVisibility(View.GONE);
                reason_missed_dose.setVisibility(View.GONE);
                reason_missed_dose_other.setVisibility(View.GONE);
                adverse_events_reported.setVisibility(View.GONE);
                adverse_events.setVisibility(View.GONE);
                other_adverse_event.setVisibility(View.GONE);
                patient_comments.setVisibility(View.VISIBLE);
                doctor_notes.setVisibility(View.VISIBLE);
                treatment_plan.setVisibility(View.GONE);
                clinician_informed.setVisibility(View.GONE);
                facility_visit_scheduled.setVisibility(View.GONE);
                facility_visit_date.setVisibility(View.GONE);
                facility_scheduled.setVisibility(View.GONE);
            } else {
                reason_patient_not_contacted.setVisibility(View.VISIBLE);
                if (reason_patient_not_contacted.getRadioGroup().getSelectedValue().equals(getString(R.string.common_reason_patient_not_contacted_other))) {
                    reason_patient_not_contacted_other.setVisibility(View.VISIBLE);
                } else {
                    reason_patient_not_contacted_other.setVisibility(View.GONE);
                }
                taking_medication.setVisibility(View.GONE);
                medication_missed_days.setVisibility(View.GONE);
                reason_missed_dose.setVisibility(View.GONE);
                reason_missed_dose_other.setVisibility(View.GONE);
                adverse_events_reported.setVisibility(View.GONE);
                adverse_events.setVisibility(View.GONE);
                other_adverse_event.setVisibility(View.GONE);
                patient_comments.setVisibility(View.GONE);
                doctor_notes.setVisibility(View.GONE);
                treatment_plan.setVisibility(View.GONE);
                clinician_informed.setVisibility(View.GONE);
                facility_visit_scheduled.setVisibility(View.GONE);
                facility_visit_date.setVisibility(View.GONE);
                facility_scheduled.setVisibility(View.GONE);
            }

        }

        if (radioGroup == reason_patient_not_contacted.getRadioGroup()) {
            if (reason_patient_not_contacted.getRadioGroup().getSelectedValue().equals(getString(R.string.common_reason_patient_not_contacted_other))) {
                reason_patient_not_contacted_other.setVisibility(View.VISIBLE);
                taking_medication.setVisibility(View.GONE);
                medication_missed_days.setVisibility(View.GONE);
                reason_missed_dose.setVisibility(View.GONE);
                reason_missed_dose_other.setVisibility(View.GONE);
                adverse_events_reported.setVisibility(View.GONE);
                adverse_events.setVisibility(View.GONE);
                other_adverse_event.setVisibility(View.GONE);
                patient_comments.setVisibility(View.GONE);
                doctor_notes.setVisibility(View.GONE);
                treatment_plan.setVisibility(View.GONE);
                clinician_informed.setVisibility(View.GONE);
                facility_visit_scheduled.setVisibility(View.GONE);
                facility_visit_date.setVisibility(View.GONE);
                facility_scheduled.setVisibility(View.GONE);
            } else {
                reason_patient_not_contacted_other.setVisibility(View.GONE);
                taking_medication.setVisibility(View.GONE);
                medication_missed_days.setVisibility(View.GONE);
                reason_missed_dose.setVisibility(View.GONE);
                reason_missed_dose_other.setVisibility(View.GONE);
                adverse_events_reported.setVisibility(View.GONE);
                adverse_events.setVisibility(View.GONE);
                other_adverse_event.setVisibility(View.GONE);
                patient_comments.setVisibility(View.GONE);
                doctor_notes.setVisibility(View.GONE);
                treatment_plan.setVisibility(View.GONE);
                clinician_informed.setVisibility(View.GONE);
                facility_visit_scheduled.setVisibility(View.GONE);
                facility_visit_date.setVisibility(View.GONE);
                facility_scheduled.setVisibility(View.GONE);

            }
        }

        if (radioGroup == taking_medication.getRadioGroup()) {
            if (taking_medication.getRadioGroup().getSelectedValue().equals(getString(R.string.no))) {
                medication_missed_days.setVisibility(View.VISIBLE);
                reason_missed_dose.setVisibility(View.VISIBLE);
                reason_missed_dose.getRadioGroup().clearCheck();
                facility_visit_scheduled.setVisibility(View.VISIBLE);
                facility_visit_scheduled.getRadioGroup().clearCheck();

            } else {
                medication_missed_days.setVisibility(View.GONE);
                reason_missed_dose.setVisibility(View.GONE);
                reason_missed_dose_other.setVisibility(View.GONE);
                facility_visit_scheduled.setVisibility(View.GONE);
                facility_visit_date.setVisibility(View.GONE);
                facility_scheduled.setVisibility(View.GONE);


            }
        }
        if (radioGroup == reason_missed_dose.getRadioGroup()) {
            adverse_events.setVisibility(View.GONE);
            reason_missed_dose_other.setVisibility(View.GONE);

            if (reason_missed_dose.getRadioGroup().getSelectedValue().equals(getString(R.string.common_reason_missed_dose_adverse)) || adverse_events_reported.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                adverse_events.setVisibility(View.VISIBLE);
            }

            if (reason_missed_dose.getRadioGroup().getSelectedValue().equals(getString(R.string.common_reason_missed_dose_other))) {
                reason_missed_dose_other.setVisibility(View.VISIBLE);

            }
        }
        if (radioGroup == adverse_events_reported.getRadioGroup()) {
            if (adverse_events_reported.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                adverse_events.setVisibility(View.VISIBLE);
                for (CheckBox cb : adverse_events.getCheckedBoxes()) {
                    cb.setChecked(false);
                }
                clinician_informed.setVisibility(View.VISIBLE);

            } else {
                if (adverse_events_reported.getRadioGroup().getSelectedValue().equals(getString(R.string.no)) && !reason_missed_dose.getRadioGroup().getSelectedValue().equals(getString(R.string.common_reason_missed_dose_adverse))) {
                    adverse_events.setVisibility(View.GONE);
                    other_adverse_event.setVisibility(View.GONE);
                }
                clinician_informed.setVisibility(View.GONE);

            }
        }
        if (radioGroup == facility_visit_scheduled.getRadioGroup()) {
            if (facility_visit_scheduled.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                facility_visit_date.setVisibility(View.VISIBLE);
                facility_scheduled.setVisibility(View.VISIBLE);
            } else {
                facility_visit_date.setVisibility(View.GONE);
                facility_scheduled.setVisibility(View.GONE);
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
}
