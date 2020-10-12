package com.ihsinformatics.gfatmmobile.comorbidities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
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
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Fawad Jawaid on 08-Feb-17.
 */

public class ComorbiditiesMentalHealthTreatmentFollowupForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...

    TitledEditText treatmentFollowupMHSessionNumber;
    TitledSpinner treatmentFollowupMHConditionBeforeSession;
    //TitledSpinner treatmentFollowupMHClientsComplaint;
    TitledCheckBoxes treatmentFollowupMHClientsComplaint;
    TitledEditText otherComplaint;
    TitledRadioGroup treatmentFollowupMHCooperation;
    TitledRadioGroup treatmentFollowupMHDefensiveness;
    TitledSpinner treatmentFollowupMHDistress;
    TitledSpinner treatmentFollowupMHConditionAfterSession;
    TitledRadioGroup treatmentFollowupMHImprovedStatus;
    TitledEditText treatmentFollowupMHAdviceForClient;
    TitledRadioGroup treatmentFollowupMHContinuationStatus;
    TitledSpinner preferredTherapyLocationSpinner;
    TitledButton treatmentFollowupMHNextAppointmentDate;

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

        pageCount = 1;
        formName = Forms.COMORBIDITIES_TREATMENT_FOLLOWUP_MENTAL_HEALTH_FORM;
        form = Forms.comorbidities_treatmentFollowupMHForm;
        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesMentalHealthTreatmentFollowupForm.MyAdapter());
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
                scrollView = new ScrollView(mainContent.getContext());
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
        treatmentFollowupMHSessionNumber = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_session_number), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "SESSION NUMBER");
        //treatmentFollowupMHConditionBeforeSession = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_before_session), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_condition_before_session_options), getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_before_session_options_very_bad), App.VERTICAL, App.VERTICAL);
        treatmentFollowupMHConditionBeforeSession = new TitledSpinner(context, "", getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_before_session), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_condition_before_session_options), getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_before_session_options_very_bad), App.VERTICAL, false, "CONDITION BEFORE SESSION", new String[]{"VERY BAD", "POOR", "AVERAGE", "GOOD", "VERY GOOD"});
        //treatmentFollowupMHClientsComplaint = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_client_complaint_options), getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint_options_financial), App.HORIZONTAL);
        treatmentFollowupMHClientsComplaint = new TitledCheckBoxes(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_client_complaint_options1), new Boolean[]{false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false}, App.VERTICAL, App.VERTICAL, false, "CHIEF COMPLAINT", new String[]{"SUICIDAL IDEATION", "HOMICIDAL IDEATION", "MANIA WITH PSYCHOTIC SYMPTOMS", "ANXIETY ATTACK", "MEDICAL ADHERENCE", "DEPRESSION", "VERBAL ABUSE", "RELATIONSHIP PROBLEMS", "ILLNESS", "DAILY ACTIVITY", "SUBSTANCE ABUSE", "ECONOMIC PROBLEM", "PHYSICAL ABUSE", "SEXUAL PROBLEM", "SEXUAL ABUSE", "OTHER COMPLAINT"});
        otherComplaint = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_client_other_complaint), "", "", 200, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER COMPLAINT");
        treatmentFollowupMHCooperation = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_cooperation), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_cooperation_options), getResources().getString(R.string.comorbidities_treatment_followup_MH_cooperation_options_complaint), App.VERTICAL, App.VERTICAL, false, "CO-OPERATION", new String[]{"COOPERATIVE BEHAVIOUR", "UNCOMFORTABLE", "NON-COOPERATIVE BEHAVIOUR"});
        treatmentFollowupMHDefensiveness = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_defensiveness), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_defensiveness_options), getResources().getString(R.string.comorbidities_treatment_followup_MH_defensiveness_options_reserved), App.VERTICAL, App.VERTICAL, false, "DEFENSIVE", new String[]{"RESERVED BEHAVIOUR", "AGGRESSIVE BEHAVIOUR", "NORMAL", "OPEN BEHAVIOUR"});
        //treatmentFollowupMHDistress = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_distress), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_distress_options), getResources().getString(R.string.comorbidities_treatment_followup_MH_distress_options_severly), App.VERTICAL, App.VERTICAL);
        treatmentFollowupMHDistress = new TitledSpinner(context, "", getResources().getString(R.string.comorbidities_treatment_followup_MH_distress), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_distress_options), getResources().getString(R.string.comorbidities_treatment_followup_MH_distress_options_severly), App.VERTICAL, false, "MENTAL DISTRESS", new String[]{"SEVERE", "MODERATE", "MILD", "RARELY", "NONE"});
        //treatmentFollowupMHConditionAfterSession = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_after_session), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_condition_after_session_options), getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_after_session_options_very_bad), App.VERTICAL, App.VERTICAL);
        treatmentFollowupMHConditionAfterSession = new TitledSpinner(context, "", getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_after_session), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_condition_after_session_options), getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_after_session_options_very_bad), App.VERTICAL, false, "CONDITION AFTER SESSION", new String[]{"VERY BAD", "POOR", "AVERAGE", "GOOD", "VERY GOOD"});
        treatmentFollowupMHImprovedStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_improved), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL, false, "IMPROVED", getResources().getStringArray(R.array.yes_no_list_concept));
        treatmentFollowupMHAdviceForClient = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_advice), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "MEDICAL ADVICE GIVEN");
        treatmentFollowupMHContinuationStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_continuation_status), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_continuation_status_options), getResources().getString(R.string.comorbidities_treatment_followup_MH_continuation_status_options_continue), App.VERTICAL, App.VERTICAL, false, "CONTINUATION STATUS", new String[]{"EXERCISE THERAPY", "END OF THERAPY", "PATIENT REFERRED", "OTHER CONTINUATION STATUS"});
        treatmentFollowupMHNextAppointmentDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_assessment_form_MH_appointment_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);

        //For getting the comorbidities location
        String columnName = "comorbidities_location";

        final Object[][] locations = serverService.getAllLocationsFromLocalDB(columnName);
        String[] locationArray = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][1]);
        }

        preferredTherapyLocationSpinner = new TitledSpinner(mainContent.getContext(), null, getResources().getString(R.string.comorbidities_treatment_followup_MH_preferred_location), locationArray, "", App.VERTICAL, true);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), treatmentFollowupMHSessionNumber.getEditText(), treatmentFollowupMHConditionBeforeSession.getSpinner(),
                treatmentFollowupMHClientsComplaint, otherComplaint.getEditText(), treatmentFollowupMHCooperation.getRadioGroup(), treatmentFollowupMHDefensiveness.getRadioGroup(),
                treatmentFollowupMHDistress.getSpinner(), treatmentFollowupMHConditionAfterSession.getSpinner(), treatmentFollowupMHImprovedStatus.getRadioGroup(),
                treatmentFollowupMHAdviceForClient.getEditText(), treatmentFollowupMHContinuationStatus.getRadioGroup(), preferredTherapyLocationSpinner.getSpinner(), treatmentFollowupMHNextAppointmentDate.getButton()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, treatmentFollowupMHSessionNumber, treatmentFollowupMHConditionBeforeSession,
                        treatmentFollowupMHClientsComplaint, otherComplaint, treatmentFollowupMHCooperation, treatmentFollowupMHDefensiveness,
                        treatmentFollowupMHDistress, treatmentFollowupMHConditionAfterSession, treatmentFollowupMHImprovedStatus,
                        treatmentFollowupMHAdviceForClient, treatmentFollowupMHContinuationStatus, preferredTherapyLocationSpinner, treatmentFollowupMHNextAppointmentDate}};

        formDate.getButton().setOnClickListener(this);
        treatmentFollowupMHNextAppointmentDate.getButton().setOnClickListener(this);
        //treatmentFollowupMHConditionBeforeSession.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentFollowupMHCooperation.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentFollowupMHDefensiveness.getRadioGroup().setOnCheckedChangeListener(this);
        //treatmentFollowupMHDistress.getRadioGroup().setOnCheckedChangeListener(this);
        //treatmentFollowupMHConditionAfterSession.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentFollowupMHImprovedStatus.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentFollowupMHContinuationStatus.getRadioGroup().setOnCheckedChangeListener(this);

        for (CheckBox cb : treatmentFollowupMHClientsComplaint.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {

        //formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
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
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }

        //missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        if (!(treatmentFollowupMHNextAppointmentDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = treatmentFollowupMHNextAppointmentDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (secondDateCalendar.before(formDateCalendar)/*secondDateCalendar.before(App.getCalendar(date))*/) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                //snackbar = Snackbar.make(mainContent, getResources().getString(R.string.next_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.next_visit_date_cannot_before_form_date), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                treatmentFollowupMHNextAppointmentDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                treatmentFollowupMHNextAppointmentDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                treatmentFollowupMHNextAppointmentDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        }
        formDate.getButton().setEnabled(true);
        treatmentFollowupMHNextAppointmentDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {

        Boolean error = super.validate();
        View view = null;


        if (otherComplaint.getEditText().getText().toString().length() > 0 && otherComplaint.getEditText().getText().toString().trim().isEmpty()) {
            otherComplaint.getEditText().setError(getString(R.string.comorbidities_patient_information_father_name_error));
            otherComplaint.getEditText().requestFocus();
            error = true;
        }
        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(),R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            DrawableCompat.setTint(clearIcon, color);
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
                                        treatmentFollowupMHAdviceForClient.clearFocus();
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


   /*     observations.add(new String[]{"SESSION NUMBER", App.get(treatmentFollowupMHSessionNumber)});

        final String treatmentFollowupMHConditionBeforeSessionString = App.get(treatmentFollowupMHConditionBeforeSession).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_before_session_options_very_bad)) ? "VERY BAD" :
                (App.get(treatmentFollowupMHConditionBeforeSession).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_before_session_options_bad)) ? "POOR" :
                        (App.get(treatmentFollowupMHConditionBeforeSession).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_before_session_options_neutral)) ? "AVERAGE" :
                                (App.get(treatmentFollowupMHConditionBeforeSession).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_before_session_options_good)) ? "GOOD" : "VERY GOOD")));
        observations.add(new String[]{"CONDITION BEFORE SESSION", treatmentFollowupMHConditionBeforeSessionString});
*/
  /*      String treatmentFollowupMHClientsComplaintString = "";
        for (CheckBox cb : treatmentFollowupMHClientsComplaint.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint_options1_suicidal)))
                treatmentFollowupMHClientsComplaintString = treatmentFollowupMHClientsComplaintString + "SUICIDAL IDEATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint_options1_homicidal)))
                treatmentFollowupMHClientsComplaintString = treatmentFollowupMHClientsComplaintString + "HOMICIDAL IDEATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint_options1_psychotic)))
                treatmentFollowupMHClientsComplaintString = treatmentFollowupMHClientsComplaintString + "MANIA WITH PSYCHOTIC SYMPTOMS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint_options1_panic)))
                treatmentFollowupMHClientsComplaintString = treatmentFollowupMHClientsComplaintString + "ANXIETY ATTACK" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint_options1_medical)))
                treatmentFollowupMHClientsComplaintString = treatmentFollowupMHClientsComplaintString + "MEDICAL ADHERENCE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint_options1_depression)))
                treatmentFollowupMHClientsComplaintString = treatmentFollowupMHClientsComplaintString + "DEPRESSION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint_options1_verbal)))
                treatmentFollowupMHClientsComplaintString = treatmentFollowupMHClientsComplaintString + "VERBAL ABUSE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint_options1_relationship)))
                treatmentFollowupMHClientsComplaintString = treatmentFollowupMHClientsComplaintString + "RELATIONSHIP PROBLEMS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint_options1_illness)))
                treatmentFollowupMHClientsComplaintString = treatmentFollowupMHClientsComplaintString + "ILLNESS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint_options1_daily)))
                treatmentFollowupMHClientsComplaintString = treatmentFollowupMHClientsComplaintString + "DAILY ACTIVITY" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint_options1_substance)))
                treatmentFollowupMHClientsComplaintString = treatmentFollowupMHClientsComplaintString + "SUBSTANCE ABUSE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint_options1_economic)))
                treatmentFollowupMHClientsComplaintString = treatmentFollowupMHClientsComplaintString + "ECONOMIC PROBLEM" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint_options1_physical)))
                treatmentFollowupMHClientsComplaintString = treatmentFollowupMHClientsComplaintString + "PHYSICAL ABUSE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint_options1_sexual_problem)))
                treatmentFollowupMHClientsComplaintString = treatmentFollowupMHClientsComplaintString + "SEXUAL PROBLEM" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint_options1_sexual_abuse)))
                treatmentFollowupMHClientsComplaintString = treatmentFollowupMHClientsComplaintString + "SEXUAL ABUSE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint_options1_other_complaint)))
                treatmentFollowupMHClientsComplaintString = treatmentFollowupMHClientsComplaintString + "OTHER COMPLAINT" + " ; ";
        }
        observations.add(new String[]{"CHIEF COMPLAINT", treatmentFollowupMHClientsComplaintString});*/

/*        if (otherComplaint.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER COMPLAINT", App.get(otherComplaint)});

        observations.add(new String[]{"CO-OPERATION", App.get(treatmentFollowupMHCooperation).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_cooperation_options_complaint)) ? "COOPERATIVE BEHAVIOUR" :
                (App.get(treatmentFollowupMHCooperation).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_cooperation_options_uncomfortable)) ? "UNCOMFORTABLE" : "NON-COOPERATIVE BEHAVIOUR")});

        final String treatmentFollowupMHDefensivenessString = App.get(treatmentFollowupMHDefensiveness).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_defensiveness_options_reserved)) ? "RESERVED BEHAVIOUR" :
                (App.get(treatmentFollowupMHDefensiveness).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_defensiveness_options_aggressive)) ? "AGGRESSIVE BEHAVIOUR" :
                        (App.get(treatmentFollowupMHDefensiveness).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_defensiveness_options_normal)) ? "NORMAL" : "OPEN BEHAVIOUR"));
        observations.add(new String[]{"DEFENSIVE", treatmentFollowupMHDefensivenessString});

        final String treatmentFollowupMHDistressString = App.get(treatmentFollowupMHDistress).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_distress_options_severly)) ? "SEVERE" :
                (App.get(treatmentFollowupMHDistress).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_distress_options_moderately)) ? "MODERATE" :
                        (App.get(treatmentFollowupMHDistress).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_distress_options_mildly)) ? "MILD" :
                                (App.get(treatmentFollowupMHDistress).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_distress_options_rarely)) ? "RARELY" : "NONE")));
        observations.add(new String[]{"MENTAL DISTRESS", treatmentFollowupMHDistressString});*/

/*
        final String treatmentFollowupMHConditionAfterSessionString = App.get(treatmentFollowupMHConditionAfterSession).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_before_session_options_very_bad)) ? "VERY BAD" :
                (App.get(treatmentFollowupMHConditionAfterSession).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_before_session_options_bad)) ? "POOR" :
                        (App.get(treatmentFollowupMHConditionAfterSession).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_before_session_options_neutral)) ? "AVERAGE" :
                                (App.get(treatmentFollowupMHConditionAfterSession).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_before_session_options_good)) ? "GOOD" : "VERY GOOD")));
        observations.add(new String[]{"CONDITION AFTER SESSION", treatmentFollowupMHConditionAfterSessionString});

        observations.add(new String[]{"IMPROVED", App.get(treatmentFollowupMHImprovedStatus).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"MEDICAL ADVICE GIVEN", App.get(treatmentFollowupMHAdviceForClient).trim()});

        final String treatmentFollowupMHContinuationStatusString = App.get(treatmentFollowupMHContinuationStatus).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_continuation_status_options_continue)) ? "EXERCISE THERAPY" :
                (App.get(treatmentFollowupMHContinuationStatus).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_continuation_status_options_last)) ? "END OF THERAPY" :
                        (App.get(treatmentFollowupMHContinuationStatus).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_continuation_status_options_referred)) ? "PATIENT REFERRED" : "OTHER CONTINUATION STATUS"));
        observations.add(new String[]{"CONTINUATION STATUS", treatmentFollowupMHContinuationStatusString});
*/

        if (App.get(treatmentFollowupMHContinuationStatus).equalsIgnoreCase(getResources().getString(R.string.comorbidities_treatment_followup_MH_continuation_status_options_continue))) {
            observations.add(new String[]{"PREFERRED HEALTH FACILITY", App.get(preferredTherapyLocationSpinner)});
            observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDate(secondDateCalendar)});
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

                String id = null;
                if (App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocally("Comorbidities-Treatment Followup Mental Health", form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = serverService.saveEncounterAndObservationTesting("Comorbidities-Treatment Followup Mental Health", form, formDateCalendar, observations.toArray(new String[][]{}), id);
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

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));

        //serverService.saveFormLocally(formName, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int formId) {
        super.refill(formId);
        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("PREFERRED HEALTH FACILITY")) {
                preferredTherapyLocationSpinner.getSpinner().selectValue(obs[0][1]);
                preferredTherapyLocationSpinner.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                treatmentFollowupMHNextAppointmentDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
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
        } else if (view == treatmentFollowupMHNextAppointmentDate.getButton()) {
            treatmentFollowupMHNextAppointmentDate.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar, true, false, true);

            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", false);
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

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Boolean flag = false;
        for (CheckBox cb : treatmentFollowupMHClientsComplaint.getCheckedBoxes()) {
            if (cb.getText().equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_client_other_complaint)) && cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (flag)
            otherComplaint.setVisibility(View.VISIBLE);
        else
            otherComplaint.setVisibility(View.GONE);

        for (CheckBox cb : treatmentFollowupMHClientsComplaint.getCheckedBoxes()) {
            if (cb.isChecked()) {
                treatmentFollowupMHClientsComplaint.getQuestionView().setError(null);
                break;
            }
        }

    }

    @Override
    public void resetViews() {
        super.resetViews();

        otherComplaint.setVisibility(View.GONE);
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        treatmentFollowupMHNextAppointmentDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        displayPreferredLocationAndNextAppointmentDate();

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
                    //String gpClinic = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.COMORBIDITIES_PATIENT_INFORMATION_FORM, "HEALTH CLINIC/POST");
                    String sessionNumber = serverService.getLatestObsValue(App.getPatientId(), "Comorbidities-Treatment Followup Mental Health", "SESSION NUMBER");

                    /*if (gpClinic != null)
                        if (!gpClinic.equals(""))
                            result.put("HEALTH CLINIC/POST", gpClinic);*/
                    if (sessionNumber != null && !sessionNumber.equals("")) {
                        sessionNumber = sessionNumber.replace(".0", "");
                        result.put("SESSION NUMBER", String.valueOf(Integer.parseInt(sessionNumber) + 1));
                    } else if (sessionNumber != null && sessionNumber.equals("")) {
                        result.put("SESSION NUMBER", "2");
                    } else if (sessionNumber == null) {
                        result.put("SESSION NUMBER", "2");
                    }
                /*if (sessionNumber != null)
                    if (!sessionNumber .equals(""))
                        result.put("SESSION NUMBER", sessionNumber);*/

                    return result;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                }

                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();

                    treatmentFollowupMHSessionNumber.getEditText().setText(result.get("SESSION NUMBER"));
                    preferredTherapyLocationSpinner.getSpinner().selectValue(App.getLocation());
                }
            };
            autopopulateFormTask.execute("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == treatmentFollowupMHContinuationStatus.getRadioGroup()) {
            displayPreferredLocationAndNextAppointmentDate();
        }
    }


    void displayPreferredLocationAndNextAppointmentDate() {

        if (treatmentFollowupMHContinuationStatus.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_treatment_followup_MH_continuation_status_options_continue))) {
            preferredTherapyLocationSpinner.setVisibility(View.VISIBLE);
            treatmentFollowupMHNextAppointmentDate.setVisibility(View.VISIBLE);
        } else {
            preferredTherapyLocationSpinner.setVisibility(View.GONE);
            treatmentFollowupMHNextAppointmentDate.setVisibility(View.GONE);
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




