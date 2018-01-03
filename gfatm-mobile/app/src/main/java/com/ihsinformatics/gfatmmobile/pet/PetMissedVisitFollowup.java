package com.ihsinformatics.gfatmmobile.pet;

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
import android.view.ViewGroup.LayoutParams;
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
 * Created by Babar on 31/1/2017.
 */

public class PetMissedVisitFollowup extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    public static final int THIRD_DIALOG_ID = 3;
    protected Calendar thirdDateCalender;
    protected DialogFragment thirdDateFragment;
    Context context;
    TitledButton formDate;
    TitledButton missedVisitDate;
    TitledRadioGroup ableToContact;
    TitledRadioGroup whyUnableToContact;
    TitledEditText otherUnableToContact;
    TitledRadioGroup patientReferredTransfer;
    TitledSpinner referralTransferLocation;
    TitledSpinner missedVisitReason;
    TitledEditText otherMissedVisitReason;
    TitledEditText newLocation;
    TitledEditText newTreatmentFacilityName;
    TitledRadioGroup reasonChangingNewFacility;
    TitledButton nextVisitDate;

    Snackbar snackbar;
    ScrollView scrollView;

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
        FORM_NAME = Forms.PET_MISSED_VISIT_FOLLOWUP;
        FORM = Forms.pet_missed_visit_followup;

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
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
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
        thirdDateCalender = Calendar.getInstance();
        thirdDateFragment = new SelectDateFragment();


        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        missedVisitDate = new TitledButton(context, null,  getResources().getString(R.string.ctb_missed_visit_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        missedVisitDate.setTag("missedVisitDate");
        ableToContact = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_able_to_contact_patient), getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL, true);
        whyUnableToContact = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_why_unable_to_contact), getResources().getStringArray(R.array.ctb_unable_to_contact_list),null, App.VERTICAL, App.VERTICAL);
        otherUnableToContact = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 250, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        patientReferredTransfer = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_patient_referred_transfered), getResources().getStringArray(R.array.ctb_patient_referred_transfered_list),null, App.VERTICAL, App.VERTICAL);
        String columnName = "";
        if (App.getProgram().equals(getResources().getString(R.string.pet)))
            columnName = "pet_location";
        else if (App.getProgram().equals(getResources().getString(R.string.fast)))
            columnName = "fast_location";
        else if (App.getProgram().equals(getResources().getString(R.string.comorbidities)))
            columnName = "comorbidities_location";
        else if (App.getProgram().equals(getResources().getString(R.string.pmdt)))
            columnName = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.childhood_tb)))
            columnName = "childhood_tb_location";

        final Object[][] locations = serverService.getAllLocationsFromLocalDB(columnName);
        String[] locationArray = new String[locations.length+1];
        for (int i = 0; i < locations.length; i++) {
            Object objLoc = locations[i][1];
            locationArray[i] = objLoc.toString();
        }
        locationArray[locations.length] = "Other";

        referralTransferLocation = new TitledSpinner(context, null, getResources().getString(R.string.ctb_location_referral_transfer), locationArray,null, App.VERTICAL, true);

        missedVisitReason = new TitledSpinner(context, null, getResources().getString(R.string.ctb_reason_why_missed), getResources().getStringArray(R.array.ctb_why_missed_reason_list),null, App.VERTICAL, true);
        otherMissedVisitReason = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 250, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        newLocation = new TitledEditText(context, null, getResources().getString(R.string.ctb_new_location), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        newTreatmentFacilityName = new TitledEditText(context, null, getResources().getString(R.string.ctb_new_facility_name), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        reasonChangingNewFacility =new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_reason_changing_facility), getResources().getStringArray(R.array.ctb_reason_changing_facility_list),null, App.VERTICAL, App.VERTICAL);
        thirdDateCalender.set(Calendar.YEAR, formDateCalendar.get(Calendar.YEAR));
        thirdDateCalender.set(Calendar.DAY_OF_MONTH, formDateCalendar.get(Calendar.DAY_OF_MONTH));
        thirdDateCalender.set(Calendar.MONTH, formDateCalendar.get(Calendar.MONTH));
        thirdDateCalender.add(Calendar.DAY_OF_MONTH, 30);

        nextVisitDate = new TitledButton(context, null,  getResources().getString(R.string.ctb_next_visit_date), DateFormat.format("dd-MMM-yyyy", thirdDateCalender).toString(), App.HORIZONTAL);
        nextVisitDate.setTag("nextVisitDate");
        views = new View[]{formDate.getButton(), missedVisitDate.getButton(),  nextVisitDate.getButton(),
                ableToContact.getRadioGroup(),whyUnableToContact.getRadioGroup(),missedVisitReason.getSpinner(),
                otherUnableToContact.getEditText(),otherMissedVisitReason.getEditText(),patientReferredTransfer.getRadioGroup(),referralTransferLocation.getSpinner(),
                newLocation.getEditText(),newTreatmentFacilityName.getEditText(),reasonChangingNewFacility.getRadioGroup()
        };

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate,missedVisitDate,ableToContact,whyUnableToContact,otherUnableToContact,patientReferredTransfer,referralTransferLocation,missedVisitReason,otherMissedVisitReason,newLocation,
                    newTreatmentFacilityName,reasonChangingNewFacility,nextVisitDate
        }};

        formDate.getButton().setOnClickListener(this);
        missedVisitDate.getButton().setOnClickListener(this);
        nextVisitDate.getButton().setOnClickListener(this);
        ableToContact.getRadioGroup().setOnCheckedChangeListener(this);
        patientReferredTransfer.getRadioGroup().setOnCheckedChangeListener(this);
        whyUnableToContact.getRadioGroup().setOnCheckedChangeListener(this);
        missedVisitReason.getSpinner().setOnItemSelectedListener(this);
        referralTransferLocation.getSpinner().setOnItemSelectedListener(this);
        reasonChangingNewFacility.getRadioGroup().setOnCheckedChangeListener(this);
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
                Calendar requiredDate = formDateCalendar.getInstance();
                requiredDate.setTime(formDateCalendar.getTime());
                requiredDate.add(Calendar.DATE, 30);

                if (requiredDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    thirdDateCalender.setTime(requiredDate.getTime());
                } else {
                    requiredDate.add(Calendar.DATE, 1);
                    thirdDateCalender.setTime(requiredDate.getTime());
                }
        }


        if (!(missedVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {
            String missedVisit = missedVisitDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(App.stringToDate(missedVisit, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(missedVisit, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }
            else
                missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }



        String nextAppointmentDateString = App.getSqlDate(thirdDateCalender);
        Date nextAppointmentDate = App.stringToDate(nextAppointmentDateString, "yyyy-MM-dd");

        String formDateString = App.getSqlDate(formDateCalendar);
        Date formStDate = App.stringToDate(formDateString, "yyyy-MM-dd");

        String missedVisitDateString = App.getSqlDate(secondDateCalendar);
        Date missedVisitDt = App.stringToDate(missedVisitDateString, "yyyy-MM-dd");


        if (!(nextVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalender).toString()))) {
            Calendar dateToday = Calendar.getInstance();
            dateToday.add(Calendar.MONTH, 24);

            String formDa = nextVisitDate.getButton().getText().toString();

            if (thirdDateCalender.before(formDateCalendar)) {

                thirdDateCalender = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                nextVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalender).toString());

            }
            else if(thirdDateCalender.after(dateToday)){
                thirdDateCalender = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_date_cant_be_greater_than_24_months), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                nextVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalender).toString());
            }

            else if(nextAppointmentDate.compareTo(formStDate) == 0){
                thirdDateCalender = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_start_date_and_next_visit_date_cant_be_same), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                nextVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalender).toString());
            }
            else
                nextVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalender).toString());

        }
        formDate.getButton().setEnabled(true);
        missedVisitDate.getButton().setEnabled(true);
        nextVisitDate.getButton().setEnabled(true);
      }

    @Override
    public boolean validate() {
        boolean error = false;
        if(otherUnableToContact.getVisibility()==View.VISIBLE){
            if(App.get(otherUnableToContact).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherUnableToContact.getEditText().setError(getString(R.string.empty_field));
                otherUnableToContact.getEditText().requestFocus();
                error = true;
            }
            if(!App.get(otherUnableToContact).isEmpty()){
                if (App.get(otherUnableToContact).trim().length() <= 0) {
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    otherUnableToContact.getEditText().setError(getString(R.string.ctb_spaces_only));
                    otherUnableToContact.getEditText().requestFocus();
                    error = true;
                }
            }
        }
        if(otherMissedVisitReason.getVisibility()==View.VISIBLE){
            if(App.get(otherMissedVisitReason).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherMissedVisitReason.getEditText().setError(getString(R.string.empty_field));
                otherMissedVisitReason.getEditText().requestFocus();
                error = true;
            }
            if(!App.get(otherMissedVisitReason).isEmpty()){
                if (App.get(otherMissedVisitReason).trim().length() <= 0) {
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    otherMissedVisitReason.getEditText().setError(getString(R.string.ctb_spaces_only));
                    otherMissedVisitReason.getEditText().requestFocus();
                    error = true;
                }
            }
        }
        if(App.get(patientReferredTransfer).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            patientReferredTransfer.getQuestionView().setError(getString(R.string.empty_field));
            patientReferredTransfer.requestFocus();
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
        observations.add(new String[]{"DATE OF MISSED VISIT", App.getSqlDateTime(secondDateCalendar)});

        observations.add(new String[]{"CONTACT TO THE PATIENT", App.get(ableToContact).toUpperCase()});

        if(whyUnableToContact.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"UNABLE TO CONTACT THE PATIENT", App.get(whyUnableToContact).equals(getResources().getString(R.string.ctb_phone_switched_off)) ? "PHONE SWITCHED OFF" :
                    (App.get(whyUnableToContact).equals(getResources().getString(R.string.ctb_patient_did_not_recieve_call)) ? "PATIENT DID NOT RECEIVE CALL" :
                            (App.get(whyUnableToContact).equals(getResources().getString(R.string.ctb_incorrect_contact_number)) ? "INCORRECT CONTACT NUMBER" :
                                    (App.get(whyUnableToContact).equals(getResources().getString(R.string.ctb_wrong_number)) ? "WRONG NUMBER" :
                                            "OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT")))});

        }
        if(otherUnableToContact.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT", App.get(otherUnableToContact).toUpperCase()});
        }

        observations.add(new String[]{"PATIENT BEING REFEREED OUT OR TRANSFERRED OUT", App.get(patientReferredTransfer).equals(getResources().getString(R.string.ctb_referral_before_starting_treatment)) ? "PATIENT REFERRED" :
                (App.get(patientReferredTransfer).equals(getResources().getString(R.string.ctb_transfer_after_starting_treatment)) ? "PATIENT TRANSFERRED OUT" :
                                "NOT APPLICABLE")});


        observations.add(new String[]{"REFERRING FACILITY NAME", App.get(referralTransferLocation)});

        observations.add(new String[]{"REASON FOR MISSED VISIT", App.get(missedVisitReason).equals(getResources().getString(R.string.ctb_pateint_moved)) ? "PATIENT MOVED" :
                (App.get(missedVisitReason).equals(getResources().getString(R.string.ctb_pateint_continue_another_location)) ? "PATIENT CHOOSE ANOTHER FACILITY" :
                            (App.get(missedVisitReason).equals(getResources().getString(R.string.ctb_patient_unable_to_visit_due_to_reason)) ? "PATIENT UNABLE TO VISIT HOSPITAL DUE TO PERSONAL REASON" :
                                    (App.get(missedVisitReason).equals(getResources().getString(R.string.ctb_patient_died)) ? "DIED":
                                            (App.get(missedVisitReason).equals(getResources().getString(R.string.ctb_patient_service_complain)) ? "SERVICE COMPLAINT" :
                                                    (App.get(missedVisitReason).equals(getResources().getString(R.string.ctb_lack_fund_travel)) ? "LACK OF FUNDS TO TRAVEL TO THE FACILITY" :
                                                            (App.get(missedVisitReason).equals(getResources().getString(R.string.ctb_unable_to_locate_referral)) ? "UNABLE TO LOCATE REFERRAL SITE" :
                                                                    (App.get(missedVisitReason).equals(getResources().getString(R.string.ctb_treatment_at_parent_side)) ? "WANT TREATMENT AT PARENT SITE":
                                                                                                                                                            "OTHER REASON TO MISSED VISIT")))))))});

        if (otherMissedVisitReason.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"OTHER REASON TO MISSED VISIT", App.get(otherMissedVisitReason)});
        }


        if (newLocation.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"NEW LOCATION", App.get(newLocation)});
        }

        if (newTreatmentFacilityName.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"TREATMENT FACILITY", App.get(newTreatmentFacilityName)});
        }

        if (reasonChangingNewFacility.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"REASON FOR CHANGING FACILITY", App.get(reasonChangingNewFacility).equals(getResources().getString(R.string.ctb_relocated)) ? "RELOCATED PATIENT" :
                    (App.get(reasonChangingNewFacility).equals(getResources().getString(R.string.ctb_old_facility_far)) ? "OLD FACILITY TOO FAR" :
                            (App.get(reasonChangingNewFacility).equals(getResources().getString(R.string.ctb_service_complain)) ? "SERVICE COMPLAINT" :
                                    "OTHER REASON FOR CHANGING FACILITY"))});
        }


        if (nextVisitDate.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDateTime(thirdDateCalender).toUpperCase()});
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

                String result = serverService.saveEncounterAndObservation(App.getProgram()+"-"+FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}),false);
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

        serverService.saveFormLocally(FORM_NAME, FORM, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int formId) {
        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {
            String[][] obs = obsValue.get(i);
            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            }else if (obs[0][0].equals("DATE OF MISSED VISIT")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                missedVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
            } else if (obs[0][0].equals("CONTACT TO THE PATIENT")) {
                for (RadioButton rb : ableToContact.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("UNABLE TO CONTACT THE PATIENT")) {
                for (RadioButton rb : whyUnableToContact.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_phone_switched_off)) && obs[0][1].equals("PHONE SWITCHED OFF")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_patient_did_not_recieve_call)) && obs[0][1].equals("PATIENT DID NOT RECEIVE CALL")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_incorrect_contact_number)) && obs[0][1].equals("INCORRECT CONTACT NUMBER")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_wrong_number)) && obs[0][1].equals("WRONG NUMBER")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_other_title)) && obs[0][1].equals("OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT")) {
                otherUnableToContact.getEditText().setText(obs[0][1]);
            }
            else if (obs[0][0].equals("PATIENT BEING REFEREED OUT OR TRANSFERRED OUT")) {
                for (RadioButton rb : patientReferredTransfer.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_referral_before_starting_treatment)) && obs[0][1].equals("PATIENT REFERRED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_transfer_after_starting_treatment)) && obs[0][1].equals("PATIENT TRANSFERRED OUT")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_not_applicable)) && obs[0][1].equals("NOT APPLICABLE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }
            else if (obs[0][0].equals("REFERRING FACILITY NAME")) {
                referralTransferLocation.getSpinner().selectValue(obs[0][1]);
            }


            else if (obs[0][0].equals("REASON FOR MISSED VISIT")) {
                String value = obs[0][1].equals("PATIENT MOVED") ? getResources().getString(R.string.ctb_pateint_moved) :
                        (obs[0][1].equals("PATIENT CHOOSE ANOTHER FACILITY") ? getResources().getString(R.string.ctb_pateint_continue_another_location) :
                                (obs[0][1].equals("PATIENT UNABLE TO VISIT HOSPITAL DUE TO PERSONAL REASON") ? getResources().getString(R.string.ctb_patient_unable_to_visit_due_to_reason) :
                                        (obs[0][1].equals("DIED") ? getResources().getString(R.string.ctb_patient_died) :
                                                (obs[0][1].equals("SERVICE COMPLAINT") ? getResources().getString(R.string.ctb_patient_service_complain) :
                                                        (obs[0][1].equals("LACK OF FUNDS TO TRAVEL TO THE FACILITY") ? getResources().getString(R.string.ctb_lack_fund_travel) :
                                                                (obs[0][1].equals("UNABLE TO LOCATE REFERRAL SITE") ? getResources().getString(R.string.ctb_unable_to_locate_referral) :
                                                                        (obs[0][1].equals("WANT TREATMENT AT PARENT SITE") ? getResources().getString(R.string.ctb_treatment_at_parent_side) :
                                                                                 getResources().getString(R.string.ctb_other_title))))))));
                missedVisitReason.getSpinner().selectValue(value);
            }
            else if (obs[0][0].equals("OTHER REASON TO MISSED VISIT")) {
                otherMissedVisitReason.getEditText().setText(obs[0][1]);
            }
            else if (obs[0][0].equals("NEW LOCATION")) {
                newLocation.getEditText().setText(obs[0][1]);
            }
            else if (obs[0][0].equals("TREATMENT FACILITY")) {
                newTreatmentFacilityName.getEditText().setText(obs[0][1]);
            }

            else if (obs[0][0].equals("REASON FOR CHANGING FACILITY")) {
                for (RadioButton rb : reasonChangingNewFacility.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_relocated)) && obs[0][1].equals("RELOCATED PATIENT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_old_facility_far)) && obs[0][1].equals("OLD FACILITY TOO FAR")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_service_complain)) && obs[0][1].equals("SERVICE COMPLAINT")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_other_title)) && obs[0][1].equals("OTHER REASON FOR CHANGING FACILITY")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }
            else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String secondDate = obs[0][1];
                thirdDateCalender.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                nextVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalender).toString());
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
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
        }
        if (view == missedVisitDate.getButton()) {
            missedVisitDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        }
        if (view == nextVisitDate.getButton()) {
            nextVisitDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", THIRD_DIALOG_ID);
            args.putBoolean("allowPastDate", false);
            args.putBoolean("allowFutureDate", true);
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");
        }
       }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == missedVisitReason.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                otherMissedVisitReason.setVisibility(View.VISIBLE);
            } else {
                otherMissedVisitReason.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_pateint_moved))) {
                newLocation.setVisibility(View.VISIBLE);
                newTreatmentFacilityName.setVisibility(View.VISIBLE);
            }
            else if(parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_pateint_continue_another_location))){
                newLocation.setVisibility(View.VISIBLE);
                newTreatmentFacilityName.setVisibility(View.VISIBLE);
                reasonChangingNewFacility.setVisibility(View.VISIBLE);
            } else {
                newLocation.setVisibility(View.GONE);
                newTreatmentFacilityName.setVisibility(View.GONE);
                reasonChangingNewFacility.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        thirdDateCalender = Calendar.getInstance();
        thirdDateCalender.set(Calendar.YEAR, formDateCalendar.get(Calendar.YEAR));
        thirdDateCalender.set(Calendar.DAY_OF_MONTH, formDateCalendar.get(Calendar.DAY_OF_MONTH));
        thirdDateCalender.set(Calendar.MONTH, formDateCalendar.get(Calendar.MONTH));
        thirdDateCalender.add(Calendar.DAY_OF_MONTH, 30);
        nextVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalender).toString());
        whyUnableToContact.setVisibility(View.GONE);
        otherUnableToContact.setVisibility(View.GONE);
        otherMissedVisitReason.setVisibility(View.GONE);
        referralTransferLocation.setVisibility(View.GONE);
        reasonChangingNewFacility.setVisibility(View.GONE);
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == ableToContact.getRadioGroup()) {
            if (ableToContact.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.no))) {
                whyUnableToContact.setVisibility(View.VISIBLE);
                if(App.get(whyUnableToContact).equals(getResources().getString(R.string.ctb_other_title))){
                    otherUnableToContact.setVisibility(View.VISIBLE);
                }
                nextVisitDate.setVisibility(View.GONE);
            } else {
                whyUnableToContact.setVisibility(View.GONE);
                otherUnableToContact.setVisibility(View.GONE);
                nextVisitDate.setVisibility(View.VISIBLE);
            }
        }
        else if(group == whyUnableToContact.getRadioGroup()){
            if (whyUnableToContact.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_other_title))) {
                otherUnableToContact.setVisibility(View.VISIBLE);
            }
            else{
                otherUnableToContact.setVisibility(View.GONE);
            }
        }
        else if(group == patientReferredTransfer.getRadioGroup()){
            patientReferredTransfer.getQuestionView().setError(null);
            if (patientReferredTransfer.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_not_applicable))) {
                referralTransferLocation.setVisibility(View.VISIBLE);
            }
            else{
                referralTransferLocation.setVisibility(View.GONE);
            }
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
            else if (getArguments().getInt("type") == THIRD_DIALOG_ID)
                calendar = thirdDateCalender;

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
            else if (((int) view.getTag()) == THIRD_DIALOG_ID)
                thirdDateCalender.set(yy, mm, dd);

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
