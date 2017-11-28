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
 * Created by Haris on 2/17/2017.
 */

public class FastMissedFollowupForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    public static final int THIRD_DATE_DIALOG_ID = 3;
    public static final int daysRange = 30;
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;

    Boolean dateChoose = false;
    Context context;

    // Views...
    TitledButton formDate;
    TitledButton missedVisitDate;
    TitledRadioGroup patientContacted;
    TitledRadioGroup reasonPatientNotContacted;
    TitledEditText reasonPatientNotContactedOther;
    TitledRadioGroup referralTransfer;
    TitledEditText referralTransferSite;
    TitledSpinner reasonMissedVisit;
    TitledEditText newLocation;
    TitledEditText facilityTreatment;
    TitledEditText reasonMissedVisitOther;
    TitledRadioGroup reasonForChangingFacility;
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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        missedVisitDate = new TitledButton(context, null, getResources().getString(R.string.fast_date_of_missed_visit), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        patientContacted = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_have_you_been_able_to_contact_the_patient), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        reasonPatientNotContacted = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_why_were_you_unable_to_contact_the_patient), getResources().getStringArray(R.array.fast_reason_patient_not_contacted_list), getResources().getString(R.string.fast_other_title), App.VERTICAL, App.VERTICAL);
        reasonPatientNotContactedOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 250, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        referralTransfer = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_has_this_patient_referred_or_transferred_out), getResources().getStringArray(R.array.fast_referral_transfer_array), getResources().getString(R.string.fast_referral), App.VERTICAL, App.VERTICAL);
        referralTransferSite = new TitledEditText(context, null, getResources().getString(R.string.fast_location_for_referral_transfer), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        reasonMissedVisit = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_why_missed_visit), getResources().getStringArray(R.array.fast_reason_missed_visit_list), getResources().getString(R.string.fast_patient_moved), App.VERTICAL);
        reasonMissedVisitOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 250, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        newLocation = new TitledEditText(context, null, getResources().getString(R.string.fast_new_location), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        facilityTreatment = new TitledEditText(context, null, getResources().getString(R.string.fast_new_treatment_facility_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        reasonForChangingFacility = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_reason_for_changing_to_new_facility), getResources().getStringArray(R.array.fast_reason_for_changing_facility_array), getResources().getString(R.string.fast_relocated), App.VERTICAL, App.VERTICAL);
        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.fast_date_of_next_visit), DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), missedVisitDate.getButton(), patientContacted.getRadioGroup(), reasonPatientNotContacted.getRadioGroup(),
                reasonPatientNotContactedOther.getEditText(), referralTransfer.getRadioGroup(), referralTransferSite.getEditText(), reasonMissedVisit.getSpinner(), reasonMissedVisitOther.getEditText(),
                newLocation.getEditText(), facilityTreatment.getEditText(), reasonForChangingFacility.getRadioGroup(),returnVisitDate.getButton()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, missedVisitDate, patientContacted, reasonPatientNotContacted, reasonPatientNotContactedOther, referralTransfer, referralTransferSite, reasonMissedVisit,
                        reasonMissedVisitOther, newLocation, facilityTreatment, reasonForChangingFacility, returnVisitDate}};

        formDate.getButton().setOnClickListener(this);
        referralTransfer.getRadioGroup().setOnCheckedChangeListener(this);
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
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            }

            else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        }


        if (!(missedVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = missedVisitDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }

            else if (secondDateCalendar.before(formDateCalendar)){
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_missed_visit_date_cannot_be_before_form_date), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            }
            else
                missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }

        if (!dateChoose) {
            Calendar requiredDate = formDateCalendar.getInstance();
            requiredDate.setTime(formDateCalendar.getTime());
            requiredDate.add(Calendar.DATE, 30);

            if (requiredDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                thirdDateCalendar.setTime(requiredDate.getTime());
            } else {
                requiredDate.add(Calendar.DATE, 1);
                thirdDateCalendar.setTime(requiredDate.getTime());
            }
        }

        String nextAppointmentDateString = App.getSqlDate(thirdDateCalendar);
        Date nextAppointmentDate = App.stringToDate(nextAppointmentDateString, "yyyy-MM-dd");

        String formDateString = App.getSqlDate(formDateCalendar);
        Date formStDate = App.stringToDate(formDateString, "yyyy-MM-dd");

        String missedVisitDateString = App.getSqlDate(secondDateCalendar);
        Date missedVisitDt = App.stringToDate(missedVisitDateString, "yyyy-MM-dd");


        if (!(returnVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString()))) {
            Calendar dateToday = Calendar.getInstance();
            dateToday.add(Calendar.MONTH, 24);

            String formDa = returnVisitDate.getButton().getText().toString();

            if (thirdDateCalendar.before(formDateCalendar)) {

                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());

            }
            else if(thirdDateCalendar.after(dateToday)){
                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_date_cant_be_greater_than_24_months), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
            }

            else if(thirdDateCalendar.before(secondDateCalendar)){
                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_date_of_next_visit_cant_be_before_missed_visit_date), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
            }

            else if(nextAppointmentDate.compareTo(formStDate) == 0){
                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_start_date_and_next_visit_date_cant_be_same), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
            }

            else if(nextAppointmentDate.compareTo(missedVisitDt) == 0){
                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_missed_visit_date_and_next_visit_date_cant_be_same), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
            }

            else
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());

        }
        dateChoose = false;
        formDate.getButton().setEnabled(true);
        missedVisitDate.getButton().setEnabled(true);
        returnVisitDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = false;
        if (reasonPatientNotContactedOther.getVisibility() == View.VISIBLE && reasonPatientNotContactedOther.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reasonPatientNotContactedOther.getEditText().setError(getString(R.string.empty_field));
            reasonPatientNotContactedOther.getEditText().requestFocus();
            error = true;
        }

        if (reasonMissedVisitOther.getVisibility() == View.VISIBLE && reasonMissedVisitOther.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reasonMissedVisitOther.getEditText().setError(getString(R.string.empty_field));
            reasonMissedVisitOther.getEditText().requestFocus();
            error = true;
        }

        if (referralTransferSite.getVisibility() == View.VISIBLE && referralTransferSite.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            referralTransferSite.getEditText().setError(getString(R.string.empty_field));
            referralTransferSite.getEditText().requestFocus();
            error = true;
        }

        if (newLocation.getVisibility() == View.VISIBLE && newLocation.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            newLocation.getEditText().setError(getString(R.string.empty_field));
            newLocation.getEditText().requestFocus();
            error = true;
        }

        if (facilityTreatment.getVisibility() == View.VISIBLE && facilityTreatment.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            facilityTreatment.getEditText().setError(getString(R.string.empty_field));
            facilityTreatment.getEditText().requestFocus();
            error = true;
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
        observations.add(new String[]{"DATE OF MISSED VISIT", App.getSqlDateTime(secondDateCalendar)});

        if (patientContacted.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CONTACT TO THE PATIENT", App.get(patientContacted).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});

        if (reasonPatientNotContacted.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"UNABLE TO CONTACT THE PATIENT", App.get(reasonPatientNotContacted).equals(getResources().getString(R.string.fast_phone_switched_off)) ? "PHONE SWITCHED OFF" :
                    (App.get(reasonPatientNotContacted).equals(getResources().getString(R.string.fast_patient_not_responding)) ? "PATIENT DID NOT RECEIVE CALL" :
                            (App.get(reasonPatientNotContacted).equals(getResources().getString(R.string.fast_incorrect_contact_number)) ? "INCORRECT CONTACT NUMBER" :
                                    (App.get(reasonPatientNotContacted).equals(getResources().getString(R.string.fast_wrong_number)) ? "WRONG NUMBER" :
                                            "OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT")))});

        if (reasonPatientNotContactedOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT", App.get(reasonPatientNotContactedOther)});

        if (referralTransferSite.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"LOCATION OF REFERRAL OR TRANSFER", App.get(referralTransferSite)});


        if (referralTransfer.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT BEING REFEREED OUT OR TRANSFERRED OUT", App.get(referralTransfer).equals(getResources().getString(R.string.fast_referral)) ? "PATIENT REFERRED" :
                    (App.get(referralTransfer).equals(getResources().getString(R.string.fast_transfer)) ? "PATIENT TRANSFERRED OUT" : "NOT APPLICABLE")});


        if (reasonMissedVisit.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON FOR MISSED VISIT", App.get(reasonMissedVisit).equals(getResources().getString(R.string.fast_patient_moved)) ? "PATIENT MOVED" :
                    (App.get(reasonMissedVisit).equals(getResources().getString(R.string.fast_patient_continuing_treatment_at_another_location)) ? "PATIENT CHOOSE ANOTHER FACILITY" :
                            (App.get(reasonMissedVisit).equals(getResources().getString(R.string.fast_patient_unable_to_visit_hospital)) ? "PATIENT UNABLE TO VISIT HOSPITAL DUE TO PERSONAL REASON" :
                                    (App.get(reasonMissedVisit).equals(getResources().getString(R.string.fast_patient_died)) ? "DIED" :
                                            (App.get(reasonMissedVisit).equals(getResources().getString(R.string.fast_patient_refused_treatment)) ? "REFUSAL OF TREATMENT BY PATIENT" :
                                                    (App.get(reasonMissedVisit).equals(getResources().getString(R.string.fast_patient_unwell)) ? "PATIENT UNWELL" :
                                                            (App.get(reasonMissedVisit).equals(getResources().getString(R.string.fast_service_complaint)) ? "SERVICE COMPLAINT" :
                                                                    (App.get(reasonMissedVisit).equals(getResources().getString(R.string.fast_lack_of_funds_to_travel)) ? "PATIENT UNWELL" :
                                                                            (App.get(reasonMissedVisit).equals(getResources().getString(R.string.fast_unable_to_relocate)) ? "UNABLE TO RELOCATE REFERRAL SITE" :
                                                                                    (App.get(reasonMissedVisit).equals(getResources().getString(R.string.fast_want_treatment_at_parent_site)) ? "WANT TREATMENT AT PARENT SITE" :"OTHER REASON TO MISSED VISIT")))))))))});


        if (reasonMissedVisitOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON TO MISSED VISIT", App.get(reasonMissedVisitOther)});

        if (newLocation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"NEW LOCATION", App.get(newLocation)});

        if (facilityTreatment.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT FACILITY", App.get(facilityTreatment)});

        if (reasonForChangingFacility.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON FOR CHANGING FACILITY", App.get(reasonForChangingFacility).equals(getResources().getString(R.string.fast_relocated)) ? "RELOCATED PATIENT" :
                    (App.get(reasonForChangingFacility).equals(getResources().getString(R.string.fast_old_facility_too_far)) ? "OLD FACILITY TOO FAR" :
                            (App.get(reasonForChangingFacility).equals(getResources().getString(R.string.fast_service_complaint)) ? "SERVICE COMPLAINT" : "OTHER REASON FOR CHANGING FACILITY"))});

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
            } else if (obs[0][0].equals("DATE OF MISSED VISIT")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                missedVisitDate.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("CONTACT TO THE PATIENT")) {
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
            } else if (obs[0][0].equals("UNABLE TO CONTACT THE PATIENT")) {
                for (RadioButton rb : reasonPatientNotContacted.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_phone_switched_off)) && obs[0][1].equals("PHONE SWITCHED OFF")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_patient_not_responding)) && obs[0][1].equals("PATIENT DID NOT RECEIVE CALL")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_incorrect_contact_number)) && obs[0][1].equals("INCORRECT CONTACT NUMBER")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.fast_wrong_number)) && obs[0][1].equals("WRONG NUMBER")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.fast_other_title)) && obs[0][1].equals("OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                reasonPatientNotContacted.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT")) {
                reasonPatientNotContactedOther.getEditText().setText(obs[0][1]);
                reasonPatientNotContactedOther.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("PATIENT BEING REFEREED OUT OR TRANSFERRED OUT")) {
                for (RadioButton rb : referralTransfer.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_referral)) && obs[0][1].equals("PATIENT REFERRED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_transfer)) && obs[0][1].equals("PATIENT TRANSFERRED OUT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_not_applicable)) && obs[0][1].equals("NOT APPLICABLE")) {
                        rb.setChecked(true);
                        break;

                    }
                    referralTransfer.setVisibility(View.VISIBLE);
                }
            }

            else if (obs[0][0].equals("LOCATION OF REFERRAL OR TRANSFER")) {
                referralTransferSite.getEditText().setText(obs[0][1]);
                referralTransferSite.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("REASON FOR MISSED VISIT")) {
                String value = obs[0][1].equals("PATIENT MOVED") ? getResources().getString(R.string.fast_patient_moved) :
                        (obs[0][1].equals("PATIENT CHOOSE ANOTHER FACILITY") ? getResources().getString(R.string.fast_patient_continuing_treatment_at_another_location) :
                                (obs[0][1].equals("PATIENT UNABLE TO VISIT HOSPITAL DUE TO PERSONAL REASON") ? getResources().getString(R.string.fast_patient_unable_to_visit_hospital) :
                                        (obs[0][1].equals("DIED") ? getResources().getString(R.string.fast_patient_died) :
                                                (obs[0][1].equals("REFUSAL OF TREATMENT BY PATIENT") ? getResources().getString(R.string.fast_patient_refused_treatment) :
                                                        (obs[0][1].equals("PATIENT UNWELL") ? getResources().getString(R.string.fast_patient_unwell) :
                                                                (obs[0][1].equals("SERVICE COMPLAINT") ? getResources().getString(R.string.fast_service_complaint) :
                                                                        (obs[0][1].equals("LACK OF FUNDS TO TRAVEL TO THE FACILITY") ? getResources().getString(R.string.fast_lack_of_funds_to_travel) :
                                                                                (obs[0][1].equals("UNABLE TO RELOCATE REFERRAL SITE") ? getResources().getString(R.string.fast_unable_to_relocate) :
                                                                                        (obs[0][1].equals("WANT TREATMENT AT PARENT SITE") ? getResources().getString(R.string.fast_want_treatment_at_parent_site) :
                                                                                                getResources().getString(R.string.fast_other_title))))))))));

                reasonMissedVisit.getSpinner().selectValue(value);
                reasonMissedVisit.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER REASON TO MISSED VISIT")) {
                reasonMissedVisitOther.getEditText().setText(obs[0][1]);
                reasonMissedVisitOther.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("NEW LOCATION")) {
                newLocation.getEditText().setText(obs[0][1]);
                newLocation.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("TREATMENT FACILITY")) {
                facilityTreatment.getEditText().setText(obs[0][1]);
                facilityTreatment.setVisibility(View.VISIBLE);
            }


            else if (obs[0][0].equals("REASON FOR CHANGING FACILITY")) {
                for (RadioButton rb : reasonForChangingFacility.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_relocated)) && obs[0][1].equals("RELOCATED PATIENT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_old_facility_too_far)) && obs[0][1].equals("OLD FACILITY TOO FAR")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_service_complaint_title)) && obs[0][1].equals("SERVICE COMPLAINT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_other_title)) && obs[0][1].equals("OTHER REASON FOR CHANGING FACILITY")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                reasonForChangingFacility.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String secondDate = obs[0][1];
                thirdDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
                returnVisitDate.setVisibility(View.VISIBLE);
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

        if (view == missedVisitDate.getButton()) {
            missedVisitDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
        }

        if (view == returnVisitDate.getButton()) {
            returnVisitDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
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
        if (spinner == reasonMissedVisit.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                reasonMissedVisitOther.setVisibility(View.VISIBLE);
            } else {
                reasonMissedVisitOther.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_patient_moved)) ||
                parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_patient_continuing_treatment_at_another_location))){
                newLocation.setVisibility(View.VISIBLE);
                facilityTreatment.setVisibility(View.VISIBLE);
            } else {
                newLocation.setVisibility(View.GONE);
                facilityTreatment.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_patient_continuing_treatment_at_another_location))){
                reasonForChangingFacility.setVisibility(View.VISIBLE);
            } else {
                reasonForChangingFacility.setVisibility(View.GONE);
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
        } else if(radioGroup == referralTransfer.getRadioGroup()){
            if(referralTransfer.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_not_applicable))){
                referralTransferSite.setVisibility(View.VISIBLE);
            }
            else{
                referralTransferSite.setVisibility(View.GONE);
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
        missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        returnVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
        reasonPatientNotContacted.setVisibility(View.GONE);
        reasonPatientNotContactedOther.setVisibility(View.GONE);
        reasonMissedVisitOther.setVisibility(View.GONE);
        referralTransferSite.setVisibility(View.GONE);
        reasonForChangingFacility.setVisibility(View.GONE);
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

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
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
