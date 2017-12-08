package com.ihsinformatics.gfatmmobile.comorbidities;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbTreatmentInitiation;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by MUHAMMAD WAQAS on 10/8/2017.
 */

public class ComorbiditiesMissedVisitFollowUp extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    public static final int THIRD_DATE_DIALOG_ID = 3;
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;
    Date diabetesDate;

    Date mentalHealthDate;
    // Views...
    TitledButton formDate;
    //    TitledSpinner diabetesEyeScreeningMonthOfTreatment;
    TitledRadioGroup missedVisitTreatmentRegimen;
    TitledButton missedVisitDate;
    TitledButton returnVisitDate;
    TitledRadioGroup patientContacted;
    TitledRadioGroup ReasonPatientNotContacted;
    TitledEditText ReasonPatientNotContactedOther;
    TitledRadioGroup referralTransfer;
    TitledEditText referralTransferSite;
    TitledRadioGroup phoneCounsellingConsent;
    TitledRadioGroup reasonMissedVisit;
    TitledEditText ReasonMissedVisitOther;
    TitledEditText newLocation;
    TitledEditText facilityTreatment;
    TitledCheckBoxes reasonForChangingFacilityCheckBoxes;


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
        FORM_NAME = Forms.COMORBIDITIES_MISSED_VISIT_FOLLOW_UP;
        FORM = Forms.comorbidities_missedVisitFollowUpForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesMissedVisitFollowUp.MyAdapter());
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
        thirdDateCalendar = Calendar.getInstance();
        thirdDateFragment = new SelectDateFragment();
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
//        diabetesEyeScreeningMonthOfTreatment = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_urinedr_month_of_treatment), getResources().getStringArray(R.array.comorbidities_followup_month), "0", App.HORIZONTAL);
        missedVisitTreatmentRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_missed_visit_for_treatment_regimen), getResources().getStringArray(R.array.comorbidities_missed_visit_for_treatment_regimen_array), null, App.VERTICAL, App.VERTICAL);
        missedVisitDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_missed_visit_date),/* DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()*/"", App.HORIZONTAL);

        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_return_visit_date), DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);
        patientContacted = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_patient_contacted), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        ReasonPatientNotContacted = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_reason_patient_not_contacted), getResources().getStringArray(R.array.comorbidities_reason_patient_not_contacted_array), getResources().getString(R.string.comorbidities_reason_patient_not_contacted_other_2), App.VERTICAL, App.VERTICAL);
        referralTransfer = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_referral_transfer), getResources().getStringArray(R.array.comorbidities_referral_transfer_array), getResources().getString(R.string.comorbidities_referral_transfer_options_refrell), App.VERTICAL, App.VERTICAL);
        reasonForChangingFacilityCheckBoxes = new TitledCheckBoxes(context, null, getResources().getString(R.string.comorbidities_reason_for_changing_facility), getResources().getStringArray(R.array.comorbidities_reason_for_changing_facility_array), new Boolean[]{true, false, false, false}, App.VERTICAL, App.VERTICAL);
        ReasonPatientNotContactedOther = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_reason_patient_not_contacted_other), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        referralTransferSite = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_referral_transfer_site), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        phoneCounsellingConsent = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_phone_counselling_consent_2), getResources().getStringArray(R.array.comorbidities_phone_counselling_consent_2_array), null, App.VERTICAL, App.VERTICAL);
        reasonMissedVisit = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_reason_missed_visit), getResources().getStringArray(R.array.comorbidities_reason_missed_visit_array), null, App.VERTICAL, App.VERTICAL);
        ReasonMissedVisitOther = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_reason_missed_visit_other), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        newLocation = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_new_location), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        facilityTreatment = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_facility_treatment), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), missedVisitTreatmentRegimen.getRadioGroup(), missedVisitDate.getButton(), patientContacted.getRadioGroup(), ReasonPatientNotContacted.getRadioGroup(), ReasonPatientNotContactedOther.getEditText(), referralTransfer.getRadioGroup(), referralTransferSite.getEditText(), phoneCounsellingConsent.getRadioGroup(), reasonMissedVisit.getRadioGroup(), ReasonMissedVisitOther.getEditText(), newLocation.getEditText(), facilityTreatment.getEditText(), reasonForChangingFacilityCheckBoxes, returnVisitDate.getButton(),};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, missedVisitTreatmentRegimen, missedVisitDate, patientContacted, ReasonPatientNotContacted, ReasonPatientNotContactedOther, referralTransfer, referralTransferSite, phoneCounsellingConsent, reasonMissedVisit, ReasonMissedVisitOther, newLocation, facilityTreatment, reasonForChangingFacilityCheckBoxes, returnVisitDate,}};


        formDate.getButton().setOnClickListener(this);
//        missedVisitDate.getButton().setOnClickListener(this);
        returnVisitDate.getButton().setOnClickListener(this);
        missedVisitTreatmentRegimen.getRadioGroup().setOnCheckedChangeListener(this);
        referralTransfer.getRadioGroup().setOnCheckedChangeListener(this);
        patientContacted.getRadioGroup().setOnCheckedChangeListener(this);
        ReasonPatientNotContacted.getRadioGroup().setOnCheckedChangeListener(this);
        phoneCounsellingConsent.getRadioGroup().setOnCheckedChangeListener(this);
        reasonMissedVisit.getRadioGroup().setOnCheckedChangeListener(this);

        for (int i = 0; i < referralTransfer.getRadioGroup().getChildCount(); i++) {
            referralTransfer.getRadioGroup().getChildAt(i).setClickable(false);
        }
        for (CheckBox cb : reasonForChangingFacilityCheckBoxes.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        referralTransferSite.getEditText().setFocusable(false);
        referralTransferSite.getEditText().setClickable(true);


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


        if (!missedVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString())) {

            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }

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

        Boolean error = false;
        View view = null;

        if (ReasonPatientNotContactedOther.getVisibility() == View.VISIBLE && App.get(ReasonPatientNotContactedOther).isEmpty()) {
            ReasonPatientNotContactedOther.getEditText().setError(getString(R.string.empty_field));
            ReasonPatientNotContactedOther.getEditText().requestFocus();
            error = true;
        }

        /*Boolean flag = false;
        if (reasonForChangingFacilityCheckBoxes.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : reasonForChangingFacilityCheckBoxes.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                reasonForChangingFacilityCheckBoxes.getQuestionView().setError(getString(R.string.empty_field));
                reasonForChangingFacilityCheckBoxes.getQuestionView().requestFocus();
                view = reasonForChangingFacilityCheckBoxes;
                error = true;
            }
        }*/

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
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
        if (missedVisitTreatmentRegimen.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"MISSED VISIT FOR TREATMENT REGIMEN", App.get(missedVisitTreatmentRegimen).equals(getResources().getString(R.string.comorbidities_missed_visit_for_treatment_regimen_diabetes)) ? "DIABETES MELLITUS" :
                    (App.get(missedVisitTreatmentRegimen).equals(getResources().getString(R.string.comorbidities_missed_visit_for_treatment_regimen_mental_health)) ? "MENTAL HEALTH SERVICES" : "BOTH")});
        if (missedVisitDate.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DATE OF MISSED VISIT", App.getSqlDateTime(secondDateCalendar)});
        if (patientContacted.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CONTACT TO THE PATIENT", App.get(patientContacted).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (ReasonPatientNotContacted.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"UNABLE TO CONTACT THE PATIENT",
                    App.get(ReasonPatientNotContacted).equals(getResources().getString(R.string.comorbidities_reason_patient_not_contacted_phone_off)) ? "PHONE SWITCHED OFF" :
                            (App.get(ReasonPatientNotContacted).equals(getResources().getString(R.string.comorbidities_reason_patient_not_contacted_not_responding)) ? "PATIENT DID NOT RECEIVE CALL" :
                                    (App.get(ReasonPatientNotContacted).equals(getResources().getString(R.string.comorbidities_reason_patient_not_contacted_invalid_number)) ? "INCORRECT CONTACT NUMBER" :
                                            (App.get(ReasonPatientNotContacted).equals(getResources().getString(R.string.comorbidities_reason_patient_not_contacted_wrong_number)) ? "WRONG NUMBER" : "OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT")))});
        if (ReasonPatientNotContactedOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT", App.get(ReasonPatientNotContactedOther)});
        if (referralTransfer.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT BEING REFEREED OUT OR TRANSFERRED OUT",
                    App.get(referralTransfer).equals(getResources().getString(R.string.comorbidities_referral_transfer_options_refrell)) ? "PATIENT REFERRED" :
                            (App.get(referralTransfer).equals(getResources().getString(R.string.comorbidities_referral_transfer_options_transfer)) ? "PATIENT TRANSFERRED OUT" : "NOT APPLICABLE")});
        if (referralTransferSite.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REFERRING FACILITY NAME", App.get(referralTransferSite)});
        if (phoneCounsellingConsent.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PHONE COUNSELLING CONSENT",
                    App.get(phoneCounsellingConsent).equals(getResources().getString(R.string.comorbidities_phone_counselling_consent_2_weekly)) ? "WEEKLY" :
                            (App.get(phoneCounsellingConsent).equals(getResources().getString(R.string.comorbidities_phone_counselling_consent_2_monthly)) ? "MONTHLY" :
                                    (App.get(phoneCounsellingConsent).equals(getResources().getString(R.string.comorbidities_phone_counselling_consent_2_both)) ? "BOTH" : "NONE"))});
        if (reasonMissedVisit.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON FOR MISSED VISIT",
                    App.get(reasonMissedVisit).equals(getResources().getString(R.string.comorbidities_reason_missed_visit_patient_relocated)) ? "PATIENT MOVED" :
                            (App.get(reasonMissedVisit).equals(getResources().getString(R.string.comorbidities_reason_missed_visit_treatment_another_location)) ? "PATIENT CHOOSE ANOTHER FACILITY" :
                                    (App.get(reasonMissedVisit).equals(getResources().getString(R.string.comorbidities_reason_missed_visit_personal_reason)) ? "PATIENT UNABLE TO VISIT HOSPITAL DUE TO PERSONAL REASON" :
                                            (App.get(reasonMissedVisit).equals(getResources().getString(R.string.comorbidities_reason_missed_visit_Died)) ? "DIED" :
                                                    (App.get(reasonMissedVisit).equals(getResources().getString(R.string.comorbidities_reason_missed_visit_refuse)) ? "REFUSAL OF TREATMENT BY PATIENT" :
                                                            (App.get(reasonMissedVisit).equals(getResources().getString(R.string.comorbidities_reason_missed_visit_unwell)) ? "PATIENT UNWELL" :
                                                                    (App.get(reasonMissedVisit).equals(getResources().getString(R.string.comorbidities_reason_missed_visit_service_complaint)) ? "SERVICE COMPLAINT" :
                                                                            (App.get(reasonMissedVisit).equals(getResources().getString(R.string.comorbidities_reason_missed_visit_lack_fund)) ? "LACK OF FUNDS TO TRAVEL TO THE FACILITY" :
                                                                                    (App.get(reasonMissedVisit).equals(getResources().getString(R.string.comorbidities_reason_missed_visit_unable_to_locate)) ? "UNABLE TO LOCATE REFERRAL SITE" :
                                                                                            (App.get(reasonMissedVisit).equals(getResources().getString(R.string.comorbidities_reason_missed_visit_want_treatment_at_parent_site)) ? "WANT TREATMENT AT PARENT SITE" : "OTHER REASON TO MISSED VISIT")))))))))});
        if (ReasonMissedVisitOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON TO MISSED VISIT", App.get(ReasonMissedVisitOther)});
        if (newLocation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"NEW LOCATION", App.get(newLocation)});
        if (facilityTreatment.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT FACILITY", App.get(facilityTreatment)});

        String reasonForChangingFacilityCheckBoxesString = "";
        for (CheckBox cb : reasonForChangingFacilityCheckBoxes.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_reason_for_changing_facility_relocated)))
                reasonForChangingFacilityCheckBoxesString = reasonForChangingFacilityCheckBoxesString + "RELOCATED PATIENT" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_reason_for_changing_facility_too_far)))
                reasonForChangingFacilityCheckBoxesString = reasonForChangingFacilityCheckBoxesString + "OLD FACILITY TOO FAR" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_reason_for_changing_facility_complaint)))
                reasonForChangingFacilityCheckBoxesString = reasonForChangingFacilityCheckBoxesString + "SERVICE COMPLAINT" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_reason_for_changing_facility_ohter)))
                reasonForChangingFacilityCheckBoxesString = reasonForChangingFacilityCheckBoxesString + "OTHER REASON FOR CHANGING FACILITY" + " ; ";
        }
        if (reasonForChangingFacilityCheckBoxes.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON FOR CHANGING FACILITY", reasonForChangingFacilityCheckBoxesString});
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

                String result = "";
                result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
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

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));

        //serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);

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
            }

            if (obs[0][0].equals("MISSED VISIT FOR TREATMENT REGIMEN")) {
                for (RadioButton rb : missedVisitTreatmentRegimen.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_missed_visit_for_treatment_regimen_diabetes)) && obs[0][1].equals("DIABETES MELLITUS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_missed_visit_for_treatment_regimen_mental_health)) && obs[0][1].equals("MENTAL HEALTH SERVICES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_missed_visit_for_treatment_regimen_mental_both)) && obs[0][1].equals("BOTH")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("DATE OF MISSED VISIT")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                missedVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
            } else if (obs[0][0].equals("CONTACT TO THE PATIENT")) {
                for (RadioButton rb : patientContacted.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("UNABLE TO CONTACT THE PATIENT")) {
                for (RadioButton rb : ReasonPatientNotContacted.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_reason_patient_not_contacted_phone_off)) && obs[0][1].equals("PHONE SWITCHED OFF")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_reason_patient_not_contacted_not_responding)) && obs[0][1].equals("PATIENT DID NOT RECEIVE CALL")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_reason_patient_not_contacted_invalid_number)) && obs[0][1].equals("INCORRECT CONTACT NUMBER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_reason_patient_not_contacted_wrong_number)) && obs[0][1].equals("WRONG NUMBER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_reason_patient_not_contacted_other_2)) && obs[0][1].equals("OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT")) {
                        rb.setChecked(true);
                        break;
                    }
                }

            } else if (obs[0][0].equals("OTHER  REASON TO NOT CONTACTED WITH THE THE PATIENT")) {
                ReasonPatientNotContactedOther.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("PATIENT BEING REFEREED OUT OR TRANSFERRED OUT")) {
                for (RadioButton rb : referralTransfer.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_referral_transfer_options_refrell)) && obs[0][1].equals("PATIENT REFERRED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_referral_transfer_options_transfer)) && obs[0][1].equals("PATIENT TRANSFERRED OUT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_referral_transfer_options_not_applicable)) && obs[0][1].equals("NOT APPLICABLE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("REFERRING FACILITY NAME")) {
                referralTransferSite.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("PHONE COUNSELLING CONSENT")) {
                for (RadioButton rb : phoneCounsellingConsent.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_phone_counselling_consent_2_weekly)) && obs[0][1].equals("WEEKLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_phone_counselling_consent_2_monthly)) && obs[0][1].equals("MONTHLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_phone_counselling_consent_2_both)) && obs[0][1].equals("BOTH")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_phone_counselling_consent_2_none)) && obs[0][1].equals("NONE")) {
                        rb.setChecked(true);
                        break;
                    }
                }

            } else if (obs[0][0].equals("REASON FOR MISSED VISIT")) {
                for (RadioButton rb : reasonMissedVisit.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_patient_relocated)) && obs[0][1].equals("PATIENT MOVED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_treatment_another_location)) && obs[0][1].equals("PATIENT CHOOSE ANOTHER FACILITY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_personal_reason)) && obs[0][1].equals("PATIENT UNABLE TO VISIT HOSPITAL DUE TO PERSONAL REASON")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_Died)) && obs[0][1].equals("DIED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_refuse)) && obs[0][1].equals("REFUSAL OF TREATMENT BY PATIENT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_unwell)) && obs[0][1].equals("PATIENT UNWELL")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_service_complaint)) && obs[0][1].equals("SERVICE COMPLAINT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_lack_fund)) && obs[0][1].equals("LACK OF FUNDS TO TRAVEL TO THE FACILITY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_unable_to_locate)) && obs[0][1].equals("UNABLE TO LOCATE REFERRAL SITE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_want_treatment_at_parent_site)) && obs[0][1].equals("WANT TREATMENT AT PARENT SITE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_other_2)) && obs[0][1].equals("OTHER REASON TO MISSED VISIT")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER REASON TO MISSED VISIT")) {
                ReasonMissedVisitOther.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("NEW LOCATION")) {
                newLocation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("TREATMENT FACILITY")) {
                facilityTreatment.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String thirddate = obs[0][1];
                thirdDateCalendar.setTime(App.stringToDate(thirddate, "yyyy-MM-dd"));
                returnVisitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
            } else if (obs[0][0].equals("REASON FOR CHANGING FACILITY")) {
                reasonForChangingFacilityCheckBoxes.getCheckedBoxes().get(0).setChecked(false);
                for (CheckBox cb : reasonForChangingFacilityCheckBoxes.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.comorbidities_reason_for_changing_facility_relocated)) && obs[0][1].equals("RELOCATED PATIENT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_reason_for_changing_facility_too_far)) && obs[0][1].equals("OLD FACILITY TOO FAR")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_reason_for_changing_facility_complaint)) && obs[0][1].equals("SERVICE COMPLAINT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_reason_for_changing_facility_ohter)) && obs[0][1].equals("OTHER  REASON FOR CHANGING FACILITY")) {
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
        } else if (view == missedVisitDate.getButton()) {
            missedVisitDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", true);
            args.putString("formDate", formDate.getButtonText());
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        } else if (view == returnVisitDate.getButton()) {
            returnVisitDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", true);
            args.putString("formDate", formDate.getButtonText());
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

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        Boolean flag = false;
        for (CheckBox cb : reasonForChangingFacilityCheckBoxes.getCheckedBoxes()) {
            if (cb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_evidence_eye_options_other)) && cb.isChecked()) {
                flag = true;
                break;
            }
        }


        for (CheckBox cb : reasonForChangingFacilityCheckBoxes.getCheckedBoxes()) {
            if (cb.isChecked()) {
                reasonForChangingFacilityCheckBoxes.getQuestionView().setError(null);
                break;
            }
        }
    }

    public static Date least(Date a, Date b) {
        return a == null ? b : (b == null ? a : (a.before(b) ? a : b));
    }

    @Override
    public void resetViews() {
        super.resetViews();


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
                    String refferalTransfer = serverService.getLatestObsValue(App.getPatientId(), "FAST-Referral Form", "PATIENT BEING REFEREED OUT OR TRANSFERRED OUT");

                    if (refferalTransfer != null)
                        if (!refferalTransfer.equals(""))
                            result.put("PATIENT BEING REFEREED OUT OR TRANSFERRED OUT", refferalTransfer);

                    return result;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                }

                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();
                    if (result.get("PATIENT BEING REFEREED OUT OR TRANSFERRED OUT") != null) {
                        if (result.get("PATIENT BEING REFEREED OUT OR TRANSFERRED OUT").equals("PATIENT REFERRED")) {
                            referralTransfer.getRadioGroup().getButtons().get(0).setChecked(true);
                        } else if (result.get("PATIENT BEING REFEREED OUT OR TRANSFERRED OUT").equals("PATIENT TRANSFERRED OUT")) {
                            referralTransfer.getRadioGroup().getButtons().get(1).setChecked(true);
                        }
                    } else {
                        referralTransfer.getRadioGroup().getButtons().get(2).setChecked(true);
                    }
                }
            };
            autopopulateFormTask.execute("");

            final AsyncTask<String, String, HashMap<String, String>> autopopulateFormTask2 = new AsyncTask<String, String, HashMap<String, String>>() {
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
                    String refferalTransfer = serverService.getLatestObsValue(App.getPatientId(), "FAST-Referral Form", "REFERRING FACILITY NAME");

                    if (refferalTransfer != null)
                        if (!refferalTransfer.equals(""))
                            result.put("REFERRING FACILITY NAME", refferalTransfer);

                    return result;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                }

                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();
                    if (result.get("REFERRING FACILITY NAME") != null) {
                        referralTransferSite.getEditText().setText(result.get("REFERRING FACILITY NAME"));
                    }
                }
            };
            autopopulateFormTask2.execute("");
            final AsyncTask<String, String, HashMap<String, String>> autopopulateFormTaskDates = new AsyncTask<String, String, HashMap<String, String>>() {
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
                    String date1 = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.COMORBIDITIES_DIABETES_TREATMENT_INITIATION, "RETURN VISIT DATE");
                    String date2 = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.COMORBIDITIES_DIABETES_TREATMENT_FOLLOWUP_FORM, "RETURN VISIT DATE");
                    diabetesDate = least(App.stringToDate(date1, "yyyy-MM-dd"), App.stringToDate(date2, "yyyy-MM-dd"));

                    String datea = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.COMORBIDITIES_MENTAL_HEALTH_SCREENING_FORM, "RETURN VISIT DATE");
                    String dateb = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.COMORBIDITIES_TREATMENT_FOLLOWUP_MENTAL_HEALTH_FORM, "RETURN VISIT DATE");
                    mentalHealthDate = least(App.stringToDate(datea, "yyyy-MM-dd"), App.stringToDate(dateb, "yyyy-MM-dd"));
                    return result;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                }

                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();
                    if (diabetesDate != null) {
                        secondDateCalendar.setTime(diabetesDate);
                       // missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                    } else {
                        secondDateCalendar.setTime(formDateCalendar.getTime());
                        //missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                    }
                }
            };
            autopopulateFormTaskDates.execute("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        if (radioGroup == patientContacted.getRadioGroup()) {
            ReasonPatientNotContacted.getRadioGroup().getButtons().get(4).setChecked(true);
            if (patientContacted.getRadioGroupSelectedValue().equals(getResources().getString(R.string.yes))) {
                ReasonPatientNotContacted.setVisibility(View.GONE);
                ReasonPatientNotContactedOther.setVisibility(View.GONE);
            } else if (patientContacted.getRadioGroupSelectedValue().equals(getResources().getString(R.string.no))) {
                ReasonPatientNotContacted.setVisibility(View.VISIBLE);
                ReasonPatientNotContactedOther.setVisibility(View.VISIBLE);


            }

        } else if (radioGroup == ReasonPatientNotContacted.getRadioGroup()) {
            if (ReasonPatientNotContacted.getRadioGroupSelectedValue().equals(getResources().getString(R.string.comorbidities_reason_patient_not_contacted_other_2)) && patientContacted.getRadioGroupSelectedValue().equals(getResources().getString(R.string.no))) {
                ReasonPatientNotContactedOther.setVisibility(View.VISIBLE);
            } else {
                ReasonPatientNotContactedOther.setVisibility(View.GONE);
            }
        } else if (radioGroup == reasonMissedVisit.getRadioGroup()) {
            ReasonMissedVisitOther.setVisibility(View.GONE);
            newLocation.setVisibility(View.GONE);
            facilityTreatment.setVisibility(View.GONE);
            reasonForChangingFacilityCheckBoxes.setVisibility(View.GONE);
            returnVisitDate.setVisibility(View.GONE);

            if (reasonMissedVisit.getRadioGroupSelectedValue().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_patient_relocated))) {
                newLocation.setVisibility(View.VISIBLE);
                facilityTreatment.setVisibility(View.VISIBLE);
                returnVisitDate.setVisibility(View.VISIBLE);
            } else if (reasonMissedVisit.getRadioGroupSelectedValue().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_treatment_another_location))) {
                newLocation.setVisibility(View.VISIBLE);
                facilityTreatment.setVisibility(View.VISIBLE);
                reasonForChangingFacilityCheckBoxes.setVisibility(View.VISIBLE);
                returnVisitDate.setVisibility(View.VISIBLE);
            } else if (reasonMissedVisit.getRadioGroupSelectedValue().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_personal_reason))) {
            } else if (reasonMissedVisit.getRadioGroupSelectedValue().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_Died))) {
                returnVisitDate.setVisibility(View.VISIBLE);
            } else if (reasonMissedVisit.getRadioGroupSelectedValue().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_refuse))) {
                returnVisitDate.setVisibility(View.VISIBLE);
            } else if (reasonMissedVisit.getRadioGroupSelectedValue().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_unwell))) {
            } else if (reasonMissedVisit.getRadioGroupSelectedValue().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_service_complaint))) {
            } else if (reasonMissedVisit.getRadioGroupSelectedValue().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_lack_fund))) {
            } else if (reasonMissedVisit.getRadioGroupSelectedValue().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_unable_to_locate))) {
            } else if (reasonMissedVisit.getRadioGroupSelectedValue().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_want_treatment_at_parent_site))) {
            } else if (reasonMissedVisit.getRadioGroupSelectedValue().equals(getResources().getString(R.string.comorbidities_reason_missed_visit_other_2))) {
                ReasonMissedVisitOther.setVisibility(View.VISIBLE);
            }
        } else if (radioGroup == referralTransfer.getRadioGroup()) {
            if (referralTransfer.getRadioGroupSelectedValue().equals(getResources().getString(R.string.comorbidities_referral_transfer_options_not_applicable))) {
                referralTransferSite.setVisibility(View.GONE);
            } else {
                referralTransferSite.setVisibility(View.VISIBLE);
            }
        } else if (radioGroup == missedVisitTreatmentRegimen.getRadioGroup()) {

            if (missedVisitTreatmentRegimen.getRadioGroupSelectedValue().equals(getResources().getString(R.string.comorbidities_missed_visit_for_treatment_regimen_diabetes))) {
                if (diabetesDate != null) {
                    secondDateCalendar.setTime(diabetesDate);
                    missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                }
            } else if (missedVisitTreatmentRegimen.getRadioGroupSelectedValue().equals(getResources().getString(R.string.comorbidities_missed_visit_for_treatment_regimen_mental_health))) {
                if (mentalHealthDate != null) {
                    secondDateCalendar.setTime(mentalHealthDate);
                    missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                }
            } else if (missedVisitTreatmentRegimen.getRadioGroupSelectedValue().equals(getResources().getString(R.string.comorbidities_missed_visit_for_treatment_regimen_mental_both))) {
                if (mentalHealthDate != null) {
                    secondDateCalendar.setTime(mentalHealthDate);
                    missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                }
            }

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




