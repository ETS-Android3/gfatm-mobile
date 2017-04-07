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
 * Created by Haris on 2/17/2017.
 */

public class FastMissedFollowupForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    public static final int THIRD_DATE_DIALOG_ID = 3;
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;
    Context context;

    // Views...
    TitledButton formDate;
    TitledButton missedVisitDate;
    TitledRadioGroup patientContacted;
    TitledRadioGroup reasonPatientNotContacted;
    TitledEditText reasonPatientNotContactedOther;
    TitledSpinner reasonMissedVisit;
    TitledEditText reasonMissedVisitOther;
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
        FORM_NAME = Forms.FAST_MISSED_VISIT_FOLLOWUP_FORM;
        FORM = Forms.fastMissedVisitFollowupForm;

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
        missedVisitDate = new TitledButton(context, null, getResources().getString(R.string.fast_date_of_missed_visit), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        patientContacted = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_have_you_been_able_to_contact_the_patient), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        reasonPatientNotContacted = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_why_were_you_unable_to_contact_the_patient), getResources().getStringArray(R.array.fast_reason_patient_not_contacted_list), getResources().getString(R.string.fast_phone_switched_off), App.VERTICAL, App.VERTICAL);
        reasonPatientNotContactedOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 250, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        reasonMissedVisit = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_why_missed_visit), getResources().getStringArray(R.array.fast_reason_missed_visit_list), getResources().getString(R.string.fast_patient_moved), App.VERTICAL);
        reasonMissedVisitOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 250, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.fast_date_of_next_visit), DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), missedVisitDate.getButton(), patientContacted.getRadioGroup(), reasonPatientNotContacted.getRadioGroup(),
                reasonPatientNotContactedOther.getEditText(), reasonMissedVisit.getSpinner(), reasonMissedVisitOther.getEditText(), returnVisitDate.getButton()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, missedVisitDate, patientContacted, reasonPatientNotContacted, reasonPatientNotContactedOther, reasonMissedVisit,
                        reasonMissedVisitOther, returnVisitDate}};

        formDate.getButton().setOnClickListener(this);
        missedVisitDate.getButton().setOnClickListener(this);
        returnVisitDate.getButton().setOnClickListener(this);
        patientContacted.getRadioGroup().setOnCheckedChangeListener(this);
        reasonPatientNotContacted.getRadioGroup().setOnCheckedChangeListener(this);
        reasonMissedVisit.getSpinner().setOnItemSelectedListener(this);

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

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        }


        if (!(missedVisitDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString()))) {

            String formDa = missedVisitDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                missedVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                missedVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
            } else
                missedVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
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

    }

    @Override
    public boolean validate() {
        Boolean error = false;
        if (reasonPatientNotContactedOther.getVisibility() == View.VISIBLE && App.get(reasonPatientNotContactedOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reasonPatientNotContactedOther.getEditText().setError(getString(R.string.empty_field));
            reasonPatientNotContactedOther.getEditText().requestFocus();
            error = true;
        }

        if (reasonMissedVisitOther.getVisibility() == View.VISIBLE && App.get(reasonMissedVisitOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reasonMissedVisitOther.getEditText().setError(getString(R.string.empty_field));
            reasonMissedVisitOther.getEditText().requestFocus();
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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
            }
            bundle.putBoolean("save", false);
        }

        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});
        observations.add(new String[]{"DATE OF MISSED VISIT", App.getSqlDateTime(secondDateCalendar)});

        if (patientContacted.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CONTACT TO THE PATIENT", App.get(patientContacted).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});

        if (reasonPatientNotContacted.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"UNABLE TO CONTACT THE PATIENT", App.get(reasonPatientNotContacted).equals(getResources().getString(R.string.fast_phone_switched_off)) ? "PHONE SWITCHED OFF" :
                    (App.get(reasonPatientNotContacted).equals(getResources().getString(R.string.fast_patient_did_not_recieve_call)) ? "PATIENT DID NOT RECEIVE CALL" :
                            (App.get(reasonPatientNotContacted).equals(getResources().getString(R.string.fast_incorrect_contact_number)) ? "INCORRECT CONTACT NUMBER" : "OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT"))});

        if (reasonPatientNotContactedOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT", App.get(reasonPatientNotContactedOther)});

        if (reasonMissedVisit.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON FOR MISSED VISIT", App.get(reasonMissedVisit).equals(getResources().getString(R.string.fast_patient_moved)) ? "PATIENT MOVED" :
                    (App.get(reasonMissedVisit).equals(getResources().getString(R.string.fast_patient_continuing_treatment_at_another_location)) ? "PATIENT CHOOSE ANOTHER FACILITY" :
                            (App.get(reasonMissedVisit).equals(getResources().getString(R.string.fast_patient_unable_to_visit_hospital)) ? "PATIENT UNABLE TO VISIT HOSPITAL DUE TO PERSONAL REASON" :
                                    (App.get(reasonMissedVisit).equals(getResources().getString(R.string.fast_patient_died)) ? "DIED" :
                                            (App.get(reasonMissedVisit).equals(getResources().getString(R.string.fast_patient_refused_treatment)) ? "REFUSAL OF TREATMENT BY PATIENT" :
                                                    (App.get(reasonMissedVisit).equals(getResources().getString(R.string.fast_patient_unwell)) ? "PATIENT UNWELL" : "OTHER REASON TO MISSED VISIT")))))});


        if (reasonMissedVisitOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON TO MISSED VISIT", App.get(reasonMissedVisitOther)});

        if (returnVisitDate.getVisibility() == View.VISIBLE)
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

                String result = serverService.saveEncounterAndObservation("Missed Visit Followup", FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
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
            if (obs[0][0].equals("FORM START TIME")) {
                startTime = App.stringToDate(obs[0][1], "yyyy-MM-dd hh:mm:ss");
            }

            else if (obs[0][0].equals("DATE OF MISSED VISIT")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                missedVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
                missedVisitDate.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("CONTACT TO THE PATIENT")) {
                for (RadioButton rb : patientContacted.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                patientContacted.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("UNABLE TO CONTACT THE PATIENT")) {
                for (RadioButton rb : reasonPatientNotContacted.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_phone_switched_off)) && obs[0][1].equals("PHONE SWITCHED OFF")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_patient_did_not_recieve_call)) && obs[0][1].equals("PATIENT DID NOT RECEIVE CALL")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.fast_incorrect_contact_number)) && obs[0][1].equals("INCORRECT CONTACT NUMBER")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.fast_other_title)) && obs[0][1].equals("OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                reasonPatientNotContacted.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT")) {
                reasonPatientNotContactedOther.getEditText().setText(obs[0][1]);
                reasonPatientNotContactedOther.setVisibility(View.VISIBLE);
            }


            else if (obs[0][0].equals("REASON FOR MISSED VISIT")) {
                String value = obs[0][1].equals("PATIENT MOVED") ? getResources().getString(R.string.fast_patient_moved) :
                        (obs[0][1].equals("PATIENT CHOOSE ANOTHER FACILITY") ? getResources().getString(R.string.fast_patient_continuing_treatment_at_another_location) :
                                (obs[0][1].equals("PATIENT UNABLE TO VISIT HOSPITAL DUE TO PERSONAL REASON") ? getResources().getString(R.string.fast_patient_unable_to_visit_hospital) :
                                        (obs[0][1].equals("DIED") ? getResources().getString(R.string.fast_patient_died) :
                                                (obs[0][1].equals("REFUSAL OF TREATMENT BY PATIENT") ? getResources().getString(R.string.fast_patient_refused_treatment) :
                                                        (obs[0][1].equals("PATIENT UNWELL") ? getResources().getString(R.string.fast_patient_unwell) :
                                                                getResources().getString(R.string.fast_other_title))))));

                reasonMissedVisit.getSpinner().selectValue(value);
                reasonMissedVisit.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("OTHER REASON TO MISSED VISIT")) {
                reasonMissedVisitOther.getEditText().setText(obs[0][1]);
                reasonMissedVisitOther.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("RETURN VISIT DATE")) {
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

        if (view == missedVisitDate.getButton()) {
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
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == reasonMissedVisit.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                reasonMissedVisitOther.setVisibility(View.VISIBLE);
            } else {
                reasonMissedVisitOther.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == patientContacted.getRadioGroup()) {
            if (patientContacted.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                returnVisitDate.setVisibility(View.GONE);
                reasonPatientNotContacted.setVisibility(View.VISIBLE);
                if (reasonPatientNotContacted.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_other_title))) {
                    reasonPatientNotContactedOther.setVisibility(View.VISIBLE);
                } else {
                    reasonPatientNotContactedOther.setVisibility(View.GONE);
                }
            } else {
                returnVisitDate.setVisibility(View.VISIBLE);
                reasonPatientNotContacted.setVisibility(View.GONE);
                reasonPatientNotContactedOther.setVisibility(View.GONE);
            }
        } else if (radioGroup == reasonPatientNotContacted.getRadioGroup()) {
            if (reasonPatientNotContacted.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_other_title))) {
                reasonPatientNotContactedOther.setVisibility(View.VISIBLE);
            } else {
                reasonPatientNotContactedOther.setVisibility(View.GONE);
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
        missedVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
        reasonPatientNotContacted.setVisibility(View.GONE);
        reasonPatientNotContactedOther.setVisibility(View.GONE);
        reasonMissedVisitOther.setVisibility(View.GONE);
        updateDisplay();

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
