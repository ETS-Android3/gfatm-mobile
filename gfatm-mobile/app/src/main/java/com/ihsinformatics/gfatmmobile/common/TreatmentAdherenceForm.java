package com.ihsinformatics.gfatmmobile.common;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 2/21/2017.
 */

public class TreatmentAdherenceForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...

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
        if (query != null) {

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
        patient_contacted = new TitledRadioGroup(context, null, getResources().getString(R.string.common_patient_contacted), getResources().getStringArray(R.array.common_patient_contacted_options), null, App.VERTICAL, App.VERTICAL, true, "CONTACT TO THE PATIENT", new String[]{"YES", "NO", "YES, BUT NOT INTERESTED", "DIED", "PATIENT WITHDREW CONSENT FOR CONTACT"});
        reason_patient_not_contacted = new TitledRadioGroup(context, null, getResources().getString(R.string.common_reason_patient_not_contacted), getResources().getStringArray(R.array.common_reason_patient_not_contacted_options), "", App.VERTICAL, App.VERTICAL, true, "UNABLE TO CONTACT THE PATIENT", new String[]{"PHONE SWITCHED OFF", "PATIENT DID NOT RECEIVE CALL", "INCORRECT CONTACT NUMBER", "WRONG NUMBER", "OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT"});
        reason_patient_not_contacted_other = new TitledEditText(context, null, getResources().getString(R.string.common_reason_patient_not_contacted_other_specifiy), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT");
        taking_medication = new TitledRadioGroup(context, null, getResources().getString(R.string.common_taking_medication), getResources().getStringArray(R.array.common_taking_medication_options), "", App.VERTICAL, App.VERTICAL, true, "CURRENTLY TAKING MEDICATION", getResources().getStringArray(R.array.yes_no_list_concept));
        medication_missed_days = new TitledEditText(context, null, getResources().getString(R.string.common_medication_missed_days), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true, "NUMBER OF DAYS MEDICATION WAS MISSED");
        reason_missed_dose = new TitledRadioGroup(context, null, getResources().getString(R.string.common_reason_missed_dose), getResources().getStringArray(R.array.common_reason_missed_dose_options), "", App.VERTICAL, App.VERTICAL, true, "REASON MISSED DOSE", new String[]{"ADVERSE EVENTS", "LACK OF MONEY FOR TRANSPORT", "OUT OF CITY", "PATIENT OR ATTENDANT CLAIMS MISDIAGNOSED", "PATIENT OR ATTENDANT CLAIMS TREATMENT COMPLETE", "OTHER REASON MISSED DOSE"});
        reason_missed_dose_other = new TitledEditText(context, null, getResources().getString(R.string.common_reason_missed_dose_other_specify), "", "", 500, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REASON MISSED DOSE");
        adverse_events_reported = new TitledRadioGroup(context, null, getResources().getString(R.string.common_adverse_events_reported), getResources().getStringArray(R.array.common_adverse_events_reported_options), "", App.VERTICAL, App.VERTICAL, true, "ADVERSE EVENTS REPORTED", getResources().getStringArray(R.array.yes_no_list_concept));
        adverse_events = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_adverse_events), getResources().getStringArray(R.array.common_adverse_events_options), null, App.VERTICAL, App.VERTICAL, true, "ADVERSE EVENTS", new String[]{"JOINT PAIN", "HEADACHE", "RASH", "NAUSEA", "DIZZINESS AND GIDDINESS", "VOMITING", "ABDOMINAL PAIN", "LOSS OF APPETITE", "VISUAL IMPAIRMENT", "OTHER ADVERSE EVENT"});
        other_adverse_event = new TitledEditText(context, null, getResources().getString(R.string.common_other_adverse_event_specify), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER ADVERSE EVENT");
        patient_comments = new TitledEditText(context, null, getResources().getString(R.string.common_patient_comments), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "CARETAKER COMMENTS");
        doctor_notes = new TitledEditText(context, null, getResources().getString(R.string.common_doctor_notes), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "CLINICIAN NOTES (TEXT)");
        treatment_plan = new TitledEditText(context, null, getResources().getString(R.string.common_treatment_plan), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "TREATMENT PLAN (TEXT)");
        clinician_informed = new TitledRadioGroup(context, null, getResources().getString(R.string.common_clinician_informed), getResources().getStringArray(R.array.common_clinician_informed_options), getString(R.string.no), App.VERTICAL, App.VERTICAL, true, "CLINICIAN INFORMED", getResources().getStringArray(R.array.yes_no_list_concept));
        facility_visit_scheduled = new TitledRadioGroup(context, null, getResources().getString(R.string.common_facility_visit_scheduled), getResources().getStringArray(R.array.common_facility_visit_scheduled_options), getString(R.string.no), App.VERTICAL, App.VERTICAL, true, "FACILITY VISIT SCHEDULED", new String[]{"YES", "NO", "UNABLE TO CONVINCE"});
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
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_current_date), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }

        if (secondDateCalendar.after(formDateCalendar)) {
            facility_visit_date.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            Date date1 = App.stringToDate(formDate.getButton().getText().toString(), "EEEE, MMM dd,yyyy");
            secondDateCalendar = App.getCalendar(date1);
        } else if (secondDateCalendar.before(App.getCalendar(Calendar.getInstance().getTime()))) {
            secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
            snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
            TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
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
        Boolean error = super.validate();
        View view = null;


        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
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

        final ArrayList<String[]> observations = getObservations();

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
                    id = serverService.saveFormLocally(Forms.TREATMENT_ADHERENCE, form, formDateCalendar, observations.toArray(new String[][]{}));

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

        super.refill(formId);
        OfflineForm fo = serverService.getSavedFormById(formId);
        ArrayList<String[][]> obsValue = fo.getObsValue();


        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("FACILITY VISIT DATE")) {
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
            showDateDialog(formDateCalendar, false, true, false);

            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");*/
        } else if (view == facility_visit_date.getButton()) {
            facility_visit_date.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar, true, true, true);

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
