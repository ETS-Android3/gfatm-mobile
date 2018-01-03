package com.ihsinformatics.gfatmmobile.pet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Fawad Jawaid on 16-Dec-16.
 */

public class PETHomeVisitForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledCheckBoxes purposeOfHomeVisit;
    TitledEditText otherPurposeOfHomeVisit;
    TitledEditText numberOfVisits;
    TitledEditText numberOfCalls;
    TitledCheckBoxes visitMadeOnTheRequestOf;
    TitledEditText otherRequestForVisit;


    Boolean refillFlag = false;

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
        FORM_NAME = Forms.PET_HOME_VISIT;
        FORM = Forms.pet_home_visit_form;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new PETHomeVisitForm.MyAdapter());
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
        formDate.setTag("formDate");
        purposeOfHomeVisit = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_purpose_of_home_visit), getResources().getStringArray(R.array.pet_purpose_of_home_visit_array), null, App.VERTICAL, App.VERTICAL, true);
        otherPurposeOfHomeVisit = new TitledEditText(context, null, getResources().getString(R.string.pet_other_purpose_of_home_visit), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        numberOfVisits = new TitledEditText(context, null, getResources().getString(R.string.pet_number_of_visits), "", "", 25, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        numberOfCalls = new TitledEditText(context, null, getResources().getString(R.string.pet_number_of_calls), "", "", 50, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        visitMadeOnTheRequestOf = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_visit_made_on_the_request_of), getResources().getStringArray(R.array.pet_visit_made_on_the_request_of_array), null, App.VERTICAL, App.VERTICAL, true);
        otherRequestForVisit = new TitledEditText(context, null, getResources().getString(R.string.pet_other_request_for_visit), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), purposeOfHomeVisit, otherPurposeOfHomeVisit.getEditText(), numberOfVisits.getEditText(), numberOfCalls.getEditText(), visitMadeOnTheRequestOf, otherRequestForVisit.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, purposeOfHomeVisit, otherPurposeOfHomeVisit, numberOfVisits, numberOfCalls, visitMadeOnTheRequestOf, otherRequestForVisit}};

        formDate.getButton().setOnClickListener(this);

        for (CheckBox cb : purposeOfHomeVisit.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : visitMadeOnTheRequestOf.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        resetViews();
    }

    @Override
    public void updateDisplay() {

        if (refillFlag) {
            refillFlag = true;
            return;
        }

        formDate.getButton().setEnabled(true);


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

        View view = null;
        Boolean error = false;

        if (App.get(otherPurposeOfHomeVisit).isEmpty() && otherPurposeOfHomeVisit.getVisibility() == View.VISIBLE) {
            otherPurposeOfHomeVisit.getEditText().setError(getResources().getString(R.string.mandatory_field));
            otherPurposeOfHomeVisit.getEditText().requestFocus();
            gotoLastPage();
            error = true;
        }
        if (App.get(numberOfVisits).isEmpty() && numberOfVisits.getVisibility() == View.VISIBLE) {
            numberOfVisits.getEditText().setError(getResources().getString(R.string.mandatory_field));
            numberOfVisits.getEditText().requestFocus();
            gotoLastPage();
            error = true;
        }
        if (App.get(numberOfCalls).isEmpty() && numberOfCalls.getVisibility() == View.VISIBLE) {
            numberOfCalls.getEditText().setError(getResources().getString(R.string.mandatory_field));
            numberOfCalls.getEditText().requestFocus();
            gotoLastPage();
            error = true;
        }
        if (App.get(otherRequestForVisit).isEmpty() && otherRequestForVisit.getVisibility() == View.VISIBLE) {
            otherRequestForVisit.getEditText().setError(getResources().getString(R.string.mandatory_field));
            otherRequestForVisit.getEditText().requestFocus();
            gotoLastPage();
            error = true;
        }


        Boolean flagPurposeOfHomeVisit = false;
        for (CheckBox cb : purposeOfHomeVisit.getCheckedBoxes()) {
            if (cb.isChecked()) {
                flagPurposeOfHomeVisit = true;
                break;
            }
        }
        if (!flagPurposeOfHomeVisit) {
            purposeOfHomeVisit.getQuestionView().setError(getResources().getString(R.string.mandatory_field));
            purposeOfHomeVisit.getQuestionView().requestFocus();
            gotoPage(1);
            error = true;
        }

        Boolean flagVisitMadeOnTheRequestOf = false;
        for (CheckBox cb : visitMadeOnTheRequestOf.getCheckedBoxes()) {
            if (cb.isChecked()) {
                flagVisitMadeOnTheRequestOf = true;
                break;
            }
        }
        if (!flagVisitMadeOnTheRequestOf) {
            visitMadeOnTheRequestOf.getQuestionView().setError(getResources().getString(R.string.mandatory_field));
            visitMadeOnTheRequestOf.getQuestionView().requestFocus();
            gotoPage(1);
            error = true;
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
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

        observations.add(new String[]{"NUMBER OF VISITS", App.get(numberOfVisits)});
        observations.add(new String[]{"NUMBER OF CALLS", App.get(numberOfCalls)});

        String purposeOfHomeVisitString = "";
        for (CheckBox cb : purposeOfHomeVisit.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_verbal_symptom_screening_1)))
                purposeOfHomeVisitString = purposeOfHomeVisitString + "VERBAL SYMPTOM SCREENING" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_reminder_for_investigations)))
                purposeOfHomeVisitString = purposeOfHomeVisitString + "REMINDER FOR INVESTIGATIONS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_reminder_for_PET_initiation)))
                purposeOfHomeVisitString = purposeOfHomeVisitString + "REMINDER FOR PET INITIATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_reminder_for_followup_visit)))
                purposeOfHomeVisitString = purposeOfHomeVisitString + "REMINDER FOR FOLLOWUP VISIT" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_counseling_to_revert_refusal_for_screening)))
                purposeOfHomeVisitString = purposeOfHomeVisitString + "COUNSELING TO REVERT REFUSAL FOR SCREENING" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_counseling_to_revert_refusal_for_investigation)))
                purposeOfHomeVisitString = purposeOfHomeVisitString + "COUNSELING TO REVERT REFUSAL FOR INVESTIGATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_counseling_to_revert_refusal_for_PET_initiation)))
                purposeOfHomeVisitString = purposeOfHomeVisitString + "COUNSELING TO REVERT REFUSAL FOR PET INITIATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_counseling_to_revrt_refusal_for_PET_continuation)))
                purposeOfHomeVisitString = purposeOfHomeVisitString + "COUNSELING TO REVERT REFUSAL FOR PET CONTINUATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_to_check_for_treatment_adherence)))
                purposeOfHomeVisitString = purposeOfHomeVisitString + "CHECK FOR TREATMENT ADHERENCE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_to_help_in_the_management_of_Adverse_Events)))
                purposeOfHomeVisitString = purposeOfHomeVisitString + "HELP IN MANAGEMENT OF ADVERSE EVENTS" + " ; ";
        }
        observations.add(new String[]{"REASON FOR HOME VISIT", purposeOfHomeVisitString});
        if (otherPurposeOfHomeVisit.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON FOR HOME VISIT", App.get(otherPurposeOfHomeVisit)});


        String visitMadeOnTheRequestOfString = "";
        for (CheckBox cb : visitMadeOnTheRequestOf.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_Clinician)))
                visitMadeOnTheRequestOfString = visitMadeOnTheRequestOfString + "CLINICAL OFFICER/DOCTOR" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_Counselor)))
                visitMadeOnTheRequestOfString = visitMadeOnTheRequestOfString + "COUNSELOR" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_RA_or_M_and_E_officer)))
                visitMadeOnTheRequestOfString = visitMadeOnTheRequestOfString + "RA/M&E officer" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_Patients_request)))
                visitMadeOnTheRequestOfString = visitMadeOnTheRequestOfString + "PATIENT REQUEST" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_Field_supervisor)))
                visitMadeOnTheRequestOfString = visitMadeOnTheRequestOfString + "FIELD SUPERVISOR" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_health_workers_discretion)))
                visitMadeOnTheRequestOfString = visitMadeOnTheRequestOfString + "HEALTH WORKER DISCRETION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_Monitoring_visit)))
                visitMadeOnTheRequestOfString = visitMadeOnTheRequestOfString + "MONITORING VISIT" + " ; ";
        }
        observations.add(new String[]{"VISIT REQUEST BY", visitMadeOnTheRequestOfString});
        if (otherRequestForVisit.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER VISIT REQUEST BY", App.get(otherRequestForVisit)});


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
                result = serverService.saveEncounterAndObservation(App.getProgram()+"-"+FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
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
        //formValues.put(firstName.getTag(), App.get(firstName));

        //serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int formId) {

        refillFlag = true;

        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {
            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("NUMBER OF VISITS")) {
                numberOfVisits.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("NUMBER OF CALLS")) {
                numberOfCalls.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("OTHER REASON FOR HOME VISIT")) {
                for (CheckBox cb : purposeOfHomeVisit.getCheckedBoxes()) {
                    if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other_1))) {
                        cb.setChecked(true);
                    }
                }
                otherPurposeOfHomeVisit.getEditText().setText(obs[0][1]);
                otherPurposeOfHomeVisit.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER VISIT REQUEST BY")) {
                for (CheckBox cb : purposeOfHomeVisit.getCheckedBoxes()) {
                    if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_others))) {
                        cb.setChecked(true);
                    }
                }
                otherRequestForVisit.getEditText().setText(obs[0][1]);
                otherRequestForVisit.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REASON FOR HOME VISIT")) {

                for (CheckBox cb : purposeOfHomeVisit.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_verbal_symptom_screening_1)) && obs[0][1].equals("VERBAL SYMPTOM SCREENING")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_reminder_for_investigations)) && obs[0][1].equals("REMINDER FOR INVESTIGATIONS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_reminder_for_PET_initiation)) && obs[0][1].equals("REMINDER FOR PET INITIATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_reminder_for_followup_visit)) && obs[0][1].equals("REMINDER FOR FOLLOWUP VISIT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_counseling_to_revert_refusal_for_screening)) && obs[0][1].equals("COUNSELING TO REVERT REFUSAL FOR SCREENING")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_counseling_to_revert_refusal_for_investigation)) && obs[0][1].equals("COUNSELING TO REVERT REFUSAL FOR INVESTIGATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_counseling_to_revert_refusal_for_PET_initiation)) && obs[0][1].equals("COUNSELING TO REVERT REFUSAL FOR PET INITIATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_counseling_to_revrt_refusal_for_PET_continuation)) && obs[0][1].equals("COUNSELING TO REVERT REFUSAL FOR PET CONTINUATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_to_check_for_treatment_adherence)) && obs[0][1].equals("CHECK FOR TREATMENT ADHERENCE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_to_help_in_the_management_of_Adverse_Events)) && obs[0][1].equals("HELP IN MANAGEMENT OF ADVERSE EVENTS")) {
                        cb.setChecked(true);
                        break;
                    }

                }
            } else if (obs[0][0].equals("VISIT REQUEST BY")) {
                for (CheckBox cb : visitMadeOnTheRequestOf.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_Clinician)) && obs[0][1].equals("CLINICAL OFFICER/DOCTOR")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_Counselor)) && obs[0][1].equals("COUNSELOR")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_RA_or_M_and_E_officer)) && obs[0][1].equals("RA/M&E officer")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_Patients_request)) && obs[0][1].equals("PATIENT REQUEST")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_Field_supervisor)) && obs[0][1].equals("FIELD SUPERVISOR")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_health_workers_discretion)) && obs[0][1].equals("HEALTH WORKER DISCRETION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_Monitoring_visit)) && obs[0][1].equals("MONITORING VISIT")) {
                        cb.setChecked(true);
                        break;
                    }

                }
            }

        }
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


        for (CheckBox cb : purposeOfHomeVisit.getCheckedBoxes()) {
            if (cb.isChecked()) {
                purposeOfHomeVisit.getQuestionView().setError(null);
            }
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other_1))) {
                otherPurposeOfHomeVisit.setVisibility(View.VISIBLE);
            } else
                otherPurposeOfHomeVisit.setVisibility(View.GONE);
        }

        for (CheckBox cb : visitMadeOnTheRequestOf.getCheckedBoxes()) {
            if (cb.isChecked()) {
                visitMadeOnTheRequestOf.getQuestionView().setError(null);
            }
            if (cb.getText().equals(getResources().getString(R.string.pet_others))) {
                if (cb.isChecked()) {
                    otherRequestForVisit.setVisibility(View.VISIBLE);
                    otherRequestForVisit.requestFocus();
                } else
                    otherRequestForVisit.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        otherPurposeOfHomeVisit.setVisibility(View.GONE);
        otherRequestForVisit.setVisibility(View.GONE);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }


    /**
     * Goto view at given location in the pager
     */
    @Override
    protected void gotoPage(int pageNo) {

        pager.setCurrentItem(pageNo);
        navigationSeekbar.setProgress(pageNo);

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