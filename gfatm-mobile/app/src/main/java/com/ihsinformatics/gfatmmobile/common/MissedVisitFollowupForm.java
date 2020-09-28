package com.ihsinformatics.gfatmmobile.common;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 2/21/2017.
 */

public class MissedVisitFollowupForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    public static final int THIRD_DATE_DIALOG_ID = 3;
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;
    Date diabetesDate;

    Date mentalHealthDate;
    Context context;

    // Views...

    TitledButton missedVisitDate;
    TitledSpinner missed_assessment;
    TitledRadioGroup patient_contacted;
    TitledRadioGroup reason_patient_not_contacted;
    TitledEditText reason_patient_not_contacted_other;
    TitledEditText referral_site;
    TitledSpinner reason_missed_visit;
    TitledEditText reason_patient_refused_treatment;
    TitledEditText reason_missed_visit_other;
    TitledEditText new_location;
    TitledEditText facility_treatment;
    TitledCheckBoxes reason_for_changing_facility;
    TitledButton returnVisitDate;
    TitledEditText cse_comments;
    String latestmisseddate;
    String refrelobs;


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
        formName = Forms.MISSED_VISIT_FOLLOWUP;
        form = Forms.missedVisitFollowup;

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
        thirdDateCalendar = Calendar.getInstance();
        thirdDateFragment = new SelectDateFragment();
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        String columnName = "";
        final Object[][] locations = serverService.getAllLocationsFromLocalDB(columnName);
        String[] locationArray = new String[locations.length + 2];
        locationArray[0] = "";
        int j = 1;
        for (int i = 0; i < locations.length; i++) {
            locationArray[j] = String.valueOf(locations[i][16]);
            j++;
        }
        missedVisitDate = new TitledButton(context, null, getResources().getString(R.string.fast_date_of_missed_visit), "" /*DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()*/, App.HORIZONTAL);
        missedVisitDate.setTag("missedVisitDate");
        missed_assessment = new TitledSpinner(context, "", getResources().getString(R.string.common_missed_assessment), getResources().getStringArray(R.array.common_missed_assessment_options), "", App.HORIZONTAL, false, "MISSED ASSESSMENT", new String[]{"TB TREATMENT FOLLOWUP", "ANTIBIOTIC TRIAL FOLLOWUP", "IPT FOLLOWUP", "PLANNED MONTHLY ASSESSMENT", "TWO WEEK ASSESSMENT", "TREATMENT INITIATED", "POST-TREATMENT ASSESSMENT", "OTHER ASSESSMENT", "CLINICIAN FOLLOW UP", "BASELINE CLINICIAN EVALUATION VISIT"});
        patient_contacted = new TitledRadioGroup(context, null, getResources().getString(R.string.common_patient_contacted), getResources().getStringArray(R.array.common_patient_contacted_options), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL, true, "CONTACT TO THE PATIENT", new String[]{"YES", "NO", "YES, BUT NOT INTERESTED", "DIED", "PATIENT WITHDREW CONSENT FOR CONTACT"});
        reason_patient_not_contacted = new TitledRadioGroup(context, null, getResources().getString(R.string.common_reason_patient_not_contacted), getResources().getStringArray(R.array.common_reason_patient_not_contacted_options), "", App.VERTICAL, App.VERTICAL, true, "UNABLE TO CONTACT THE PATIENT", new String[]{"PHONE SWITCHED OFF", "PATIENT DID NOT RECEIVE CALL", "INCORRECT CONTACT NUMBER", "WRONG NUMBER", "OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT"});
        reason_patient_not_contacted_other = new TitledEditText(context, null, getResources().getString(R.string.common_reason_patient_not_contacted_other_specifiy), "", "", 250, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT");
        referral_site = new TitledEditText(context, null, getResources().getString(R.string.common_referral_site), "", "", 25, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "REFERRING FACILITY NAME");
        reason_missed_visit = new TitledSpinner(context, "", getResources().getString(R.string.common_reason_missed_visit), getResources().getStringArray(R.array.common_reason_missed_visit_options), "", App.HORIZONTAL, false, "REASON FOR MISSED VISIT", new String[]{"PATIENT MOVED", "PATIENT CHOOSE ANOTHER FACILITY", "PATIENT UNABLE TO VISIT HOSPITAL DUE TO PERSONAL REASON", "DIED", "REFUSAL OF TREATMENT BY PATIENT", "PATIENT UNWELL", "SERVICE COMPLAINT", "LACK OF FUNDS TO TRAVEL TO THE FACILITY", "PATIENT OUT OF CITY", "PATIENT ALREADY VISITED FACILITY", "UNABLE TO LOCATE REFERRAL SITE", "WANT TREATMENT AT PARENT SITE", "OTHER REASON TO MISSED VISIT"});
        reason_patient_refused_treatment = new TitledEditText(context, null, getResources().getString(R.string.common_reason_patient_refused_treatment), "", "", 250, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "REASON PATIENT REFUSED TREATMENT");
        reason_missed_visit_other = new TitledEditText(context, null, getResources().getString(R.string.common_reason_missed_visit_other_specify), "", "", 250, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "OTHER REASON TO MISSED VISIT");
        new_location = new TitledEditText(context, null, getResources().getString(R.string.common_new_loc), "", "", 20, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "NEW LOCATION");
        facility_treatment = new TitledEditText(context, null, getResources().getString(R.string.common_facility_treatment), "", "", 20, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "TREATMENT FACILITY");
        reason_for_changing_facility = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_reason_for_changing_facility), getResources().getStringArray(R.array.common_reason_for_changing_facility_options), null, App.VERTICAL, App.VERTICAL, true, "REASON FOR CHANGING FACILITY", new String[]{"RELOCATED PATIENT", "OLD FACILITY TOO FAR", "SERVICE COMPLAINT", "OTHER REASON FOR CHANGING FACILITY"});
        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.common_return_visit_date), DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);
        returnVisitDate.setTag("returnVisitDate");
        cse_comments = new TitledEditText(context, null, getResources().getString(R.string.common_cse_comments), "", "", 250, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "CSE COMMENTS");


        // Used for reset fields...
        views = new View[]{formDate.getButton(), missedVisitDate.getButton(), missed_assessment.getSpinner(), patient_contacted.getRadioGroup(),
                reason_patient_not_contacted.getRadioGroup(), reason_patient_not_contacted_other.getEditText(), referral_site.getEditText(),
                reason_missed_visit.getSpinner(), reason_patient_refused_treatment.getEditText(), reason_missed_visit_other.getEditText(),
                new_location.getEditText(), facility_treatment.getEditText(), reason_for_changing_facility, returnVisitDate.getButton(), cse_comments.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, missedVisitDate, missed_assessment, patient_contacted, reason_patient_not_contacted, reason_patient_not_contacted_other,
                        referral_site, reason_missed_visit, reason_patient_refused_treatment, reason_missed_visit_other, new_location,
                        facility_treatment, reason_for_changing_facility, returnVisitDate, cse_comments}};

        formDate.getButton().setOnClickListener(this);
        patient_contacted.getRadioGroup().setOnCheckedChangeListener(this);
        reason_patient_not_contacted.getRadioGroup().setOnCheckedChangeListener(this);
        reason_missed_visit.getSpinner().setOnItemSelectedListener(this);
        returnVisitDate.getButton().setOnClickListener(this);


        resetViews();
    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();

        String formDa = formDate.getButton().getText().toString();
        String personDOB = App.getPatient().getPerson().getBirthdate();


        Date date = new Date();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {


            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }


        /*if (!missedVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString())) {

            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }
*/

        if (!returnVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString())) {

            if (thirdDateCalendar.after(date)) {

                thirdDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else if (thirdDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
            } else if (thirdDateCalendar.before(formDateCalendar)) {
                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.ctb_date_can_not_be_less_than_form_date), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
            } else
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
        }
        formDate.getButton().setEnabled(true);
        missedVisitDate.getButton().setEnabled(true);
        returnVisitDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = super.validate();


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

        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                Boolean flag = serverService.deleteOfflineForms(encounterId);
                if (!flag) {

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.form_does_not_exist));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    bundle.putBoolean("save", false);
                                    submit();
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.backToMainMenu();
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
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));

                    /*Toast.makeText(context, getString(R.string.form_does_not_exist),
                            Toast.LENGTH_LONG).show();*/

                    return false;
                }
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


        if (missedVisitDate.getVisibility() == View.VISIBLE && latestmisseddate != null)
            observations.add(new String[]{"DATE OF MISSED VISIT", App.getSqlDateTime(secondDateCalendar)});

       /* if (missed_assessment.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"MISSED ASSESSMENT", App.get(missed_assessment).equals(getResources().getString(R.string.common_missed_assessment_tb_treatment)) ? "TB TREATMENT FOLLOWUP" :
                    (App.get(missed_assessment).equals(getResources().getString(R.string.common_missed_assessment_antibiotic)) ? "ANTIBIOTIC TRIAL FOLLOWUP" :
                            (App.get(missed_assessment).equals(getResources().getString(R.string.common_missed_assessment_ipt)) ? "IPT FOLLOWUP" :
                                    (App.get(missed_assessment).equals(getResources().getString(R.string.common_missed_assessment_planned)) ? "PLANNED MONTHLY ASSESSMENT" :
                                            (App.get(missed_assessment).equals(getResources().getString(R.string.common_missed_assessment_two_week)) ? "TWO WEEK ASSESSMENT" :
                                                    (App.get(missed_assessment).equals(getResources().getString(R.string.common_missed_assessment_treatmetn_init)) ? "TREATMENT INITIATED" :
                                                            (App.get(missed_assessment).equals(getResources().getString(R.string.common_missed_assessment_post_treat)) ? "POST-TREATMENT ASSESSMENT" :
                                                                    (App.get(missed_assessment).equals(getResources().getString(R.string.common_missed_assessment_other_ass)) ? "OTHER ASSESSMENT" :
                                                                            (App.get(missed_assessment).equals(getResources().getString(R.string.common_missed_assessment_clinician)) ? "BASELINE CLINICIAN EVALUATION VISIT" : "CLINICIAN FOLLOW UP"))))))))});
*/
        /*if (patient_contacted.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CONTACT TO THE PATIENT", App.get(patient_contacted).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(patient_contacted).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(patient_contacted).equals(getResources().getString(R.string.common_patient_contacted_yes_not_interested)) ? "YES, BUT NOT INTERESTED" :
                                    (App.get(patient_contacted).equals(getResources().getString(R.string.common_patient_contacted_died)) ? "DIED" : "PATIENT WITHDREW CONSENT FOR CONTACT")))});
*/
       /* if (reason_patient_not_contacted.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"UNABLE TO CONTACT THE PATIENT", App.get(reason_patient_not_contacted).equals(getResources().getString(R.string.common_reason_patient_not_contacted_phone_switched_off)) ? "PHONE SWITCHED OFF" :
                    (App.get(reason_patient_not_contacted).equals(getResources().getString(R.string.common_reason_patient_not_contacted_patient_not_resp)) ? "PATIENT DID NOT RECEIVE CALL" :
                            (App.get(reason_patient_not_contacted).equals(getResources().getString(R.string.common_reason_patient_not_contacted_invalid)) ? "INCORRECT CONTACT NUMBER" :
                                    (App.get(reason_patient_not_contacted).equals(getResources().getString(R.string.common_reason_patient_not_contacted_wrong)) ? "WRONG NUMBER" : "OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT")))});
*/
    /*    if (reason_patient_not_contacted_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT", reason_patient_not_contacted_other.getEditText().getText().toString().trim()});

        if (referral_site.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REFERRING FACILITY NAME", referral_site.getEditText().getText().toString().trim()});

*/     /*   if (reason_missed_visit.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON FOR MISSED VISIT", App.get(reason_missed_visit).equals(getResources().getString(R.string.common_reason_missed_visit_patient_relocate)) ? "PATIENT MOVED" :
                    (App.get(reason_missed_visit).equals(getResources().getString(R.string.common_reason_missed_visit_patient_continue_treatment)) ? "PATIENT CHOOSE ANOTHER FACILITY" :
                            (App.get(reason_missed_visit).equals(getResources().getString(R.string.common_reason_missed_visit_unable_visit_hosp)) ? "PATIENT UNABLE TO VISIT HOSPITAL DUE TO PERSONAL REASON" :
                                    (App.get(reason_missed_visit).equals(getResources().getString(R.string.common_reason_missed_visit_died)) ? "DIED" :
                                            (App.get(reason_missed_visit).equals(getResources().getString(R.string.common_reason_missed_visit_refused)) ? "REFUSAL OF TREATMENT BY PATIENT" :
                                                    (App.get(reason_missed_visit).equals(getResources().getString(R.string.common_reason_missed_visit_unwell)) ? "PATIENT UNWELL" :
                                                            (App.get(reason_missed_visit).equals(getResources().getString(R.string.common_reason_missed_visit_complaint)) ? "SERVICE COMPLAINT" :
                                                                    (App.get(reason_missed_visit).equals(getResources().getString(R.string.common_reason_missed_visit_lacks_funds)) ? "LACK OF FUNDS TO TRAVEL TO THE FACILITY" :
                                                                            (App.get(reason_missed_visit).equals(getResources().getString(R.string.common_reason_missed_visit_out_city)) ? "PATIENT OUT OF CITY" :
                                                                                    (App.get(reason_missed_visit).equals(getResources().getString(R.string.common_reason_missed_visit_already_visited)) ? "PATIENT ALREADY VISITED FACILITY" :
                                                                                            (App.get(reason_missed_visit).equals(getResources().getString(R.string.common_reason_missed_visit_unable_locate)) ? "UNABLE TO LOCATE REFERRAL SITE" :
                                                                                                    (App.get(reason_missed_visit).equals(getResources().getString(R.string.common_reason_missed_visit_want_treatment)) ? "WANT TREATMENT AT PARENT SITE" : "OTHER REASON TO MISSED VISIT")))))))))))});
*/
     /*   if (reason_patient_refused_treatment.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON PATIENT REFUSED TREATMENT", reason_patient_refused_treatment.getEditText().getText().toString().trim()});

        if (reason_missed_visit_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON TO MISSED VISIT", reason_missed_visit_other.getEditText().getText().toString().trim()});

        if (new_location.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"NEW LOCATION", new_location.getEditText().getText().toString().trim()});

        if (facility_treatment.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT FACILITY", facility_treatment.getEditText().getText().toString().trim()});
*/
/*
        if (reason_for_changing_facility.getVisibility() == View.VISIBLE) {
            String diabetes_treatmeant_String = "";
            for (CheckBox cb : reason_for_changing_facility.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_reason_for_changing_facility_relocat)))
                    diabetes_treatmeant_String = diabetes_treatmeant_String + "RELOCATED PATIENT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_reason_for_changing_facility_old_far)))
                    diabetes_treatmeant_String = diabetes_treatmeant_String + "OLD FACILITY TOO FAR" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_reason_for_changing_facility_service_complaint)))
                    diabetes_treatmeant_String = diabetes_treatmeant_String + "SERVICE COMPLAINT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.common_reason_for_changing_facility_other)))
                    diabetes_treatmeant_String = diabetes_treatmeant_String + "OTHER REASON FOR CHANGING FACILITY" + " ; ";
            }
            observations.add(new String[]{"REASON FOR CHANGING FACILITY", diabetes_treatmeant_String});
        }
*/

        if (returnVisitDate.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDateTime(thirdDateCalendar)});

       /* if (cse_comments.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CSE COMMENTS", cse_comments.getEditText().getText().toString().trim()});*/


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
                    id = serverService.saveFormLocallyTesting(Forms.MISSED_VISIT_FOLLOWUP, form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = "";


                result = serverService.saveEncounterAndObservationTesting(Forms.MISSED_VISIT_FOLLOWUP, form, formDateCalendar, observations.toArray(new String[][]{}), id);
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

            if (obs[0][0].equals("DATE OF MISSED VISIT")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                missedVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
            } else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String thirddate = obs[0][1];
                thirdDateCalendar.setTime(App.stringToDate(thirddate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
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
        } else if (view == missedVisitDate.getButton()) {
            missedVisitDate.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar, false, true, true);

            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", true);
            args.putString("formDate", formDate.getButtonText());
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");*/
        } else if (view == returnVisitDate.getButton()) {
            returnVisitDate.getButton().setEnabled(false);
            showDateDialog(thirdDateCalendar, true, false, true);

            /*Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", true);
            args.putString("formDate", formDate.getButtonText());
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");*/
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;

        if (spinner == reason_missed_visit.getSpinner()) {

            reason_patient_refused_treatment.setVisibility(View.GONE);
            reason_missed_visit_other.setVisibility(View.GONE);
            new_location.setVisibility(View.GONE);
            facility_treatment.setVisibility(View.GONE);
            reason_for_changing_facility.setVisibility(View.GONE);
            returnVisitDate.setVisibility(View.VISIBLE);


            if (App.get(reason_missed_visit).equals(getString(R.string.common_reason_missed_visit_other))) {
                reason_missed_visit_other.setVisibility(View.VISIBLE);
            } else if (App.get(reason_missed_visit).equals(getString(R.string.common_reason_missed_visit_patient_relocate)) ||
                    App.get(reason_missed_visit).equals(getString(R.string.common_reason_missed_visit_patient_continue_treatment))) {
                new_location.setVisibility(View.VISIBLE);
                facility_treatment.setVisibility(View.VISIBLE);
                returnVisitDate.setVisibility(View.GONE);
                if (App.get(reason_missed_visit).equals(getString(R.string.common_reason_missed_visit_patient_continue_treatment))) {
                    reason_for_changing_facility.setVisibility(View.VISIBLE);
                }
            } else if (App.get(reason_missed_visit).equals(getString(R.string.common_reason_missed_visit_died)) ||
                    App.get(reason_missed_visit).equals(getString(R.string.common_reason_missed_visit_refused))) {
                returnVisitDate.setVisibility(View.GONE);
                if (App.get(reason_missed_visit).equals(getString(R.string.common_reason_missed_visit_refused))) {
                    reason_patient_refused_treatment.setVisibility(View.VISIBLE);
                }

            }

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }


    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());

        reason_patient_not_contacted.setVisibility(View.GONE);
        reason_patient_not_contacted_other.setVisibility(View.GONE);
        reason_patient_refused_treatment.setVisibility(View.GONE);
        reason_missed_visit_other.setVisibility(View.GONE);
        new_location.setVisibility(View.GONE);
        facility_treatment.setVisibility(View.GONE);
        reason_for_changing_facility.setVisibility(View.GONE);
        referral_site.getEditText().setEnabled(false);

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
            final AsyncTask<String, String, String> autopopulateFormTaskDates = new AsyncTask<String, String, String>() {
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

                    latestmisseddate = serverService.getLatestObsValue(App.getPatientId(), "RETURN VISIT DATE");
                    refrelobs = serverService.getLatestObsValue(App.getPatientId(), "Referral and Transfer", "REFERRING FACILITY NAME");
                    return latestmisseddate;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    loading.dismiss();
                    if (result != null) {
                        secondDateCalendar.setTime(App.stringToDate(result, "yyyy-MM-dd"));
                        missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                    }

                    if (refrelobs != null) {
                        referral_site.getEditText().setText(refrelobs);
                    }

                }
            };
            autopopulateFormTaskDates.execute("");
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

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            updateDisplay();
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == patient_contacted.getRadioGroup()) {
            if (patient_contacted.getRadioGroup().getSelectedValue().equals(getString(R.string.common_patient_contacted_yes_not_interested)) ||
                    patient_contacted.getRadioGroup().getSelectedValue().equals(getString(R.string.common_patient_contacted_died)) ||
                    patient_contacted.getRadioGroup().getSelectedValue().equals(getString(R.string.pet_patient_withdrew_consent))) {

                reason_patient_not_contacted.setVisibility(View.GONE);
                reason_patient_not_contacted_other.setVisibility(View.GONE);
                referral_site.setVisibility(View.GONE);
                reason_missed_visit.setVisibility(View.GONE);
                reason_patient_refused_treatment.setVisibility(View.GONE);
                reason_missed_visit_other.setVisibility(View.GONE);
                new_location.setVisibility(View.GONE);
                facility_treatment.setVisibility(View.GONE);
                reason_for_changing_facility.setVisibility(View.GONE);
                returnVisitDate.setVisibility(View.GONE);
                cse_comments.setVisibility(View.VISIBLE);

            } else if (patient_contacted.getRadioGroup().getSelectedValue().equals(getString(R.string.no))) {
                reason_patient_not_contacted.setVisibility(View.VISIBLE);
                reason_missed_visit.setVisibility(View.GONE);
                reason_patient_refused_treatment.setVisibility(View.GONE);
                reason_missed_visit_other.setVisibility(View.GONE);
                new_location.setVisibility(View.GONE);
                facility_treatment.setVisibility(View.GONE);
                reason_for_changing_facility.setVisibility(View.GONE);
                returnVisitDate.setVisibility(View.VISIBLE);
                cse_comments.setVisibility(View.VISIBLE);

            } else if (patient_contacted.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                reason_missed_visit.setVisibility(View.VISIBLE);
                reason_patient_not_contacted.setVisibility(View.GONE);
                reason_missed_visit.getSpinner().setSelection(0);
                new_location.setVisibility(View.VISIBLE);
                facility_treatment.setVisibility(View.VISIBLE);
                returnVisitDate.setVisibility(View.VISIBLE);
                cse_comments.setVisibility(View.VISIBLE);
            }
        }


        if (radioGroup == reason_patient_not_contacted.getRadioGroup()) {
            if (reason_patient_not_contacted.getRadioGroup().getSelectedValue().equals(getString(R.string.common_reason_patient_not_contacted_other))) {
                reason_patient_not_contacted_other.setVisibility(View.VISIBLE);
                returnVisitDate.setVisibility(View.VISIBLE);
                cse_comments.setVisibility(View.VISIBLE);
                referral_site.setVisibility(View.VISIBLE);
            } else if (reason_patient_not_contacted.getRadioGroup().getSelectedValue().equals("")) {
                reason_patient_not_contacted_other.setVisibility(View.GONE);
                returnVisitDate.setVisibility(View.VISIBLE);
                cse_comments.setVisibility(View.VISIBLE);
                referral_site.setVisibility(View.VISIBLE);

            } else {
                reason_patient_not_contacted_other.setVisibility(View.GONE);
                returnVisitDate.setVisibility(View.GONE);
                cse_comments.setVisibility(View.GONE);
                referral_site.setVisibility(View.GONE);

            }
        }
    }

    public static Date least(Date a, Date b) {
        return a == null ? b : (b == null ? a : (a.before(b) ? a : b));
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
