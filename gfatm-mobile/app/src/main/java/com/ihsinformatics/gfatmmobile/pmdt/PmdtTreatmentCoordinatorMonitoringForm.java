package com.ihsinformatics.gfatmmobile.pmdt;

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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tahira on 3/1/2017.
 */

public class PmdtTreatmentCoordinatorMonitoringForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledEditText treatmentSupporterId;
    TitledEditText treatmentSupporterFirstName;
    TitledEditText treatmentSupporterLastName;
    TitledButton visitDate;
    TitledRadioGroup patientUnderstandTbRegimen;
    TitledRadioGroup patientKnowSideEffects;
    TitledRadioGroup treatmentSupporterProvideDot;
    TitledSpinner treatmentSupporterProvideDotIrregularly;
    TitledRadioGroup placeDot;
    TitledEditText otherPlaceDot;
    TitledRadioGroup dotLastPrescribedDay;
    TitledRadioGroup administerInjections;
    TitledRadioGroup missedDose;
    TitledSpinner reasonMissedDose;
    TitledEditText otherReasonMissedDose;
    TitledSpinner practiceInfectionControl;
    TitledSpinner familyPracticeInfectionControl;
    TitledSpinner waitAfterAdministerDrug;
    TitledRadioGroup registerHouseholdMembers;
    TitledButton contactScreeningPerformedDate;
    TitledRadioGroup patientSatisfied;
    TitledRadioGroup sputumSubmittedLastVisit;
    TitledRadioGroup reasonNotSubmittedSample;
    TitledEditText otherReasonNotSubmittedSample;
    TitledRadioGroup foodBasketIncentiveLastMonth;
    TitledRadioGroup adverseEventLastVisit;
    TitledCheckBoxes actionAdverseEvent;
    TitledEditText otherActionAdverseEvent;
    TitledRadioGroup addressSocialProblems;
    TitledRadioGroup referCounseling;
    TitledCheckBoxes counselingType;
    TitledEditText otherCounselingType;
    TitledEditText treatmentCoordinatorNotes;

    ScrollView scrollView;

    public static final int THIRD_DATE_DIALOG_ID = 3;
    // Extra Views for date ...
    Calendar thirdDateCalendar;
    DialogFragment thirdDateFragment;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PAGE_COUNT = 4;
        FORM_NAME = Forms.PMDT_TREATMENT_COORDINATOR_MONITORING;
        FORM = Forms.pmdtTreatmentCoordinatorcMonitoring;
        thirdDateCalendar = Calendar.getInstance();
        thirdDateFragment = new SelectDateFragment();

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
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }
        return mainContent;
    }

    @Override
    public void initViews() {
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        treatmentSupporterId = new TitledEditText(context, "", getResources().getString(R.string.pmdt_treatment_supporter_id), "", "", 10, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        treatmentSupporterFirstName = new TitledEditText(context, "", getResources().getString(R.string.pmdt_treatment_supporter_first_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        treatmentSupporterFirstName.setFocusableInTouchMode(true);
        treatmentSupporterLastName = new TitledEditText(context, "", getResources().getString(R.string.pmdt_treatment_supporter_last_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        treatmentSupporterLastName.setFocusableInTouchMode(true);
        visitDate = new TitledButton(context, null, getResources().getString(R.string.pmdt_visit_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        patientUnderstandTbRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_patient_understand_tb_regimen), getResources().getStringArray(R.array.yes_no_unknown_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        patientKnowSideEffects = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_patient_know_side_effects), getResources().getStringArray(R.array.yes_no_unknown_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        // second page views...
        treatmentSupporterProvideDot = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_treatment_supporter_provide_dot), getResources().getStringArray(R.array.pmdt_how_often_provide_dot), getResources().getString(R.string.pmdt_regularly), App.VERTICAL, App.VERTICAL);
        treatmentSupporterProvideDotIrregularly = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_treatment_supporter_provide_dot_irregularly), getResources().getStringArray(R.array.irregular_dot_types), getResources().getString(R.string.pmdt_six_days_per_week), App.VERTICAL, false);
        placeDot = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_place_dot), getResources().getStringArray(R.array.pmdt_places_dot_provided), getResources().getString(R.string.pmdt_patient_home), App.VERTICAL, App.VERTICAL);
        otherPlaceDot = new TitledEditText(context, null, getResources().getString(R.string.pmdt_other_place_dot), "", "", 100, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        dotLastPrescribedDay = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_dot_last_prescribed_day), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        administerInjections = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_administer_injections), getResources().getStringArray(R.array.pmdt_yes_no_not_applicable), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        missedDose = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_missed_dose), getResources().getStringArray(R.array.pmdt_missed_drug_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        reasonMissedDose = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_reason_missed_dose), getResources().getStringArray(R.array.pmdt_missed_drug_reasons), getResources().getString(R.string.pmdt_adverse_event), App.VERTICAL);
        otherReasonMissedDose = new TitledEditText(context, null, getResources().getString(R.string.pmdt_other_reason_missed_dose), "", "", 100, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        // third page views...
        practiceInfectionControl = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_practice_infection_control), getResources().getStringArray(R.array.pmdt_infection_control_frequency), getResources().getString(R.string.pmdt_always), App.VERTICAL, false);
        familyPracticeInfectionControl = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_family_practice_infection_control), getResources().getStringArray(R.array.pmdt_infection_control_frequency), getResources().getString(R.string.pmdt_always), App.VERTICAL, false);
        waitAfterAdministerDrug = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_wait_after_administer_drug), getResources().getStringArray(R.array.pmdt_wait_after_drug_time_range), getResources().getString(R.string.pmdt_twenty_to_thirty_mins), App.VERTICAL, false);
        registerHouseholdMembers = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_register_household_members), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        contactScreeningPerformedDate = new TitledButton(context, null, getResources().getString(R.string.pmdt_contact_screening_performed_date), DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString(), App.VERTICAL);
        patientSatisfied = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_patient_satisfied), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        sputumSubmittedLastVisit = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_sputum_submitted_last_visit), getResources().getStringArray(R.array.pmdt_yes_no_not_applicable), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        reasonNotSubmittedSample = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_reason_not_submitted_sputum), getResources().getStringArray(R.array.pmdt_reasons_sputums_not_submitted), getResources().getString(R.string.pmdt_could_not_produce_sputum), App.VERTICAL, App.VERTICAL);
        otherReasonNotSubmittedSample = new TitledEditText(context, null, getResources().getString(R.string.pmdt_other_reason_not_submitted_sputum), "", "", 100, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        // fourth and last page views...
        foodBasketIncentiveLastMonth = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_food_basket_incentice_last_month), getResources().getStringArray(R.array.pmdt_yes_no_refused), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        adverseEventLastVisit = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_adverse_event_last_visit), getResources().getStringArray(R.array.pmdt_yes_no_refused), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        actionAdverseEvent = new TitledCheckBoxes(context, null, getResources().getString(R.string.pmdt_action_adverse_event), getResources().getStringArray(R.array.pmdt_adverse_actions_treatment_supporter), new Boolean[]{true, false, false, false, false}, App.VERTICAL, App.VERTICAL, true);
        otherActionAdverseEvent = new TitledEditText(context, null, getResources().getString(R.string.pmdt_other_action_adverse_event), "", "", 100, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        addressSocialProblems = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_address_social_problems), getResources().getStringArray(R.array.pmdt_yes_no_refused), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        referCounseling = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_refer_counseling), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        counselingType = new TitledCheckBoxes(context, null, getResources().getString(R.string.pmdt_counseling_type), getResources().getStringArray(R.array.pmdt_counseling_types), new Boolean[]{true, false, false, false, false, false}, App.VERTICAL, App.VERTICAL);
        otherCounselingType = new TitledEditText(context, null, getResources().getString(R.string.pmdt_other_counseling_type), "", "", 100, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        treatmentCoordinatorNotes = new TitledEditText(context, null, getResources().getString(R.string.pmdt_treatment_coordinator_notes), "", "", 1024, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        treatmentCoordinatorNotes.getEditText().setSingleLine(false);
        treatmentCoordinatorNotes.getEditText().setMinimumHeight(150);

        views = new View[]{formDate.getButton(), treatmentSupporterId.getEditText(), treatmentSupporterFirstName.getEditText(), treatmentSupporterLastName.getEditText(), visitDate.getButton(), patientUnderstandTbRegimen.getRadioGroup(), patientKnowSideEffects.getRadioGroup(), treatmentSupporterProvideDot.getRadioGroup(),
                treatmentSupporterProvideDotIrregularly.getSpinner(), placeDot.getRadioGroup(), otherPlaceDot.getEditText(), dotLastPrescribedDay.getRadioGroup(), administerInjections.getRadioGroup(), missedDose.getRadioGroup(), reasonMissedDose.getSpinner(), otherReasonMissedDose.getEditText(), practiceInfectionControl.getSpinner(),
                familyPracticeInfectionControl.getSpinner(), waitAfterAdministerDrug.getSpinner(), registerHouseholdMembers.getRadioGroup(), contactScreeningPerformedDate.getButton(), patientSatisfied.getRadioGroup(), sputumSubmittedLastVisit.getRadioGroup(), reasonNotSubmittedSample.getRadioGroup(), otherReasonNotSubmittedSample.getEditText(),
                foodBasketIncentiveLastMonth.getRadioGroup(), adverseEventLastVisit.getRadioGroup(), actionAdverseEvent, otherActionAdverseEvent.getEditText(), addressSocialProblems.getRadioGroup(), referCounseling.getRadioGroup(), counselingType, otherCounselingType.getEditText(), treatmentCoordinatorNotes.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, treatmentSupporterId, treatmentSupporterFirstName, treatmentSupporterLastName, visitDate, patientUnderstandTbRegimen, patientKnowSideEffects},
                        {treatmentSupporterProvideDot, treatmentSupporterProvideDotIrregularly, placeDot, otherPlaceDot, dotLastPrescribedDay, administerInjections, missedDose, reasonMissedDose, otherReasonMissedDose},
                        {practiceInfectionControl, familyPracticeInfectionControl, waitAfterAdministerDrug, registerHouseholdMembers, contactScreeningPerformedDate, patientSatisfied, sputumSubmittedLastVisit, reasonNotSubmittedSample, otherReasonNotSubmittedSample},
                        {foodBasketIncentiveLastMonth, adverseEventLastVisit, actionAdverseEvent, otherActionAdverseEvent, addressSocialProblems, referCounseling, counselingType, otherCounselingType, treatmentCoordinatorNotes}};


        treatmentSupporterId.getEditText().setKeyListener(null);
        treatmentSupporterFirstName.getEditText().setKeyListener(null);
        treatmentSupporterLastName.getEditText().setKeyListener(null);

        formDate.getButton().setOnClickListener(this);
        visitDate.getButton().setOnClickListener(this);
        contactScreeningPerformedDate.getButton().setOnClickListener(this);
        treatmentSupporterProvideDot.getRadioGroup().setOnCheckedChangeListener(this);
        placeDot.getRadioGroup().setOnCheckedChangeListener(this);
        missedDose.getRadioGroup().setOnCheckedChangeListener(this);
        reasonMissedDose.getSpinner().setOnItemSelectedListener(this);
        registerHouseholdMembers.getRadioGroup().setOnCheckedChangeListener(this);
        sputumSubmittedLastVisit.getRadioGroup().setOnCheckedChangeListener(this);
        reasonNotSubmittedSample.getRadioGroup().setOnCheckedChangeListener(this);
        adverseEventLastVisit.getRadioGroup().setOnCheckedChangeListener(this);
        referCounseling.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : actionAdverseEvent.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : counselingType.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        resetViews();
    }


    @Override
    public void updateDisplay() {

        contactScreeningPerformedDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());

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
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }

        if (secondDateCalendar.after(formDateCalendar)) {
            visitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            Date date1 = App.stringToDate(formDate.getButton().getText().toString(), "dd-MMM-yyyy");
            secondDateCalendar = App.getCalendar(date1);
        } else
            visitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());

    }

    @Override
    public boolean validate() {

        Boolean error = false;

        for (CheckBox cb : counselingType.getCheckedBoxes()) {

            if (App.get(cb).equals(getResources().getString(R.string.pmdt_other))) {
                if (cb.isChecked() && otherCounselingType.getVisibility() == View.VISIBLE && App.get(otherCounselingType).isEmpty()) {
                    otherCounselingType.getEditText().setError(getResources().getString(R.string.mandatory_field));
                    otherCounselingType.getEditText().requestFocus();
                    error = true;
                }
            }
        }

        for (CheckBox cb : actionAdverseEvent.getCheckedBoxes()) {

            if (App.get(cb).equals(getResources().getString(R.string.pmdt_other))) {
                if (cb.isChecked() && otherActionAdverseEvent.getVisibility() == View.VISIBLE && App.get(otherActionAdverseEvent).isEmpty()) {
                    otherActionAdverseEvent.getEditText().setError(getResources().getString(R.string.mandatory_field));
                    otherActionAdverseEvent.getEditText().requestFocus();
                    error = true;
                }

            }
        }

        if (otherReasonNotSubmittedSample.getVisibility() == View.VISIBLE && App.get(otherReasonNotSubmittedSample).isEmpty()) {
            otherReasonNotSubmittedSample.getEditText().setError(getResources().getString(R.string.mandatory_field));
            otherReasonNotSubmittedSample.getEditText().requestFocus();
            error = true;
        }

        if (otherReasonMissedDose.getVisibility() == View.VISIBLE && App.get(otherReasonMissedDose).isEmpty()) {
            otherReasonMissedDose.getEditText().setError(getResources().getString(R.string.mandatory_field));
            otherReasonMissedDose.getEditText().requestFocus();
            error = true;
        }

        if (otherPlaceDot.getVisibility() == View.VISIBLE && App.get(otherPlaceDot).isEmpty()) {
            otherPlaceDot.getEditText().setError(getResources().getString(R.string.mandatory_field));
            otherPlaceDot.getEditText().requestFocus();
            error = true;
        }

        if (error) {

            // int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.form_error));
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

        observations.add(new String[]{"VISIT DATE", App.getSqlDate(secondDateCalendar)});

        observations.add(new String[]{"PATIENT UNDERSTAND TB REGIMEN", App.get(patientUnderstandTbRegimen).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(patientUnderstandTbRegimen).equals(getResources().getString(R.string.no)) ? "NO" : "UNKNOWN")});

        observations.add(new String[]{"PATIENT KNOW COMMON SIDE EFFECTS", App.get(patientKnowSideEffects).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(patientKnowSideEffects).equals(getResources().getString(R.string.no)) ? "NO" : "UNKNOWN")});

        observations.add(new String[]{"TREATMENT SUPPORTER PROVIDE DOT", App.get(treatmentSupporterProvideDot).equals(getResources().getString(R.string.pmdt_not_at_all)) ? "NOT AT ALL" :
                (App.get(treatmentSupporterProvideDot).equals(getResources().getString(R.string.pmdt_regularly)) ? "REGULARLY" :
                        (App.get(treatmentSupporterProvideDot).equals(getResources().getString(R.string.pmdt_irregularly)) ? "IRREGULARLY" : "UNKNOWN"))});

        if (treatmentSupporterProvideDotIrregularly.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT SUPPORTER PROVIDE DOT IRREGULARLY", App.get(treatmentSupporterProvideDotIrregularly).equals(getResources().getString(R.string.pmdt_six_days_per_week)) ? "6 DAYS PER WEEK" :
                    (App.get(treatmentSupporterProvideDotIrregularly).equals(getResources().getString(R.string.pmdt_every_other_day)) ? "EVERY OTHER DAY" :
                            (App.get(treatmentSupporterProvideDotIrregularly).equals(getResources().getString(R.string.pmdt_three_days_per_week)) ? "THRICE A WEEK" :
                                    (App.get(treatmentSupporterProvideDotIrregularly).equals(getResources().getString(R.string.pmdt_four_five_days_a_week)) ? "4-5 DAYS PER WEEK" :
                                            (App.get(treatmentSupporterProvideDotIrregularly).equals(getResources().getString(R.string.pmdt_two_three_days_a_week)) ? "2-3 DAYS A WEEK" :
                                                    (App.get(treatmentSupporterProvideDotIrregularly).equals(getResources().getString(R.string.pmdt_once_a_week)) ? "WEEKLY" :
                                                            (App.get(treatmentSupporterProvideDotIrregularly).equals(getResources().getString(R.string.pmdt_every_two_weeks)) ? "EVERY TWO WEEKS" : "MONTHLY"))))))});

        if (placeDot.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PLACE OF DOT", App.get(placeDot).equals(getResources().getString(R.string.pmdt_patient_home)) ? "PATIENT HOME" :
                    (App.get(placeDot).equals(getResources().getString(R.string.pmdt_treatment_supporter_workplace)) ? "TREATMENT SUPPORTER WORKPLACE" :
                            (App.get(placeDot).equals(getResources().getString(R.string.pmdt_patient_workplace)) ? "PATIENT WORKPLACE" : "OTHER PLACE OF DOT"))});

        if (otherPlaceDot.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER PLACE OF DOT", App.get(otherPlaceDot)});

        if (dotLastPrescribedDay.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DOT AT LAST PRESCRIBED DAY", App.get(dotLastPrescribedDay).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if (administerInjections.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT SUPPORTER ADMINISTER INJECTION", App.get(administerInjections).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(administerInjections).equals(getResources().getString(R.string.no)) ? "NO" : "NOT APPLICABLE")});

        if (missedDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT MISSED DOSE", App.get(missedDose).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(missedDose).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(missedDose).equals(getResources().getString(R.string.pmdt_refused)) ? "REFUSED" : "UNKNOWN"))});

        if (reasonMissedDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON MISSED DOSE", App.get(reasonMissedDose).equals(getResources().getString(R.string.pmdt_adverse_events)) ? "ADVERSE EVENTS" :
                    (App.get(reasonMissedDose).equals(getResources().getString(R.string.pmdt_drug_not_available)) ? "DRUG NOT AVAILABLE" :
                            (App.get(reasonMissedDose).equals(getResources().getString(R.string.pmdt_treatment_supporter_did_not_come)) ? "TREATMENT SUPPORTER DID NOT COME" :
                                    (App.get(reasonMissedDose).equals(getResources().getString(R.string.pmdt_refused)) ? "REFUSED" :
                                            (App.get(reasonMissedDose).equals(getResources().getString(R.string.pmdt_unknown)) ? "UNKNOWN" : "OTHER REASON MISSED DOSE"))))});

        if (otherReasonMissedDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON MISSED DOSE", App.get(otherReasonMissedDose)});

        observations.add(new String[]{"TREATMENT SUPPORTER PRACTICE INFECTION CONTROL MEASURES", App.get(practiceInfectionControl).equals(getResources().getString(R.string.pmdt_always)) ? "ALWAYS" :
                (App.get(practiceInfectionControl).equals(getResources().getString(R.string.pmdt_sometimes)) ? "SOMETIMES" :
                        (App.get(practiceInfectionControl).equals(getResources().getString(R.string.pmdt_rarely)) ? "RARELY" :
                                (App.get(practiceInfectionControl).equals(getResources().getString(R.string.pmdt_never)) ? "NEVER" : "UNKNOWN")))});


        observations.add(new String[]{"FAMILY PRACTICE INFECTION CONTROL MEASURES", App.get(familyPracticeInfectionControl).equals(getResources().getString(R.string.pmdt_always)) ? "ALWAYS" :
                (App.get(familyPracticeInfectionControl).equals(getResources().getString(R.string.pmdt_sometimes)) ? "SOMETIMES" :
                        (App.get(familyPracticeInfectionControl).equals(getResources().getString(R.string.pmdt_rarely)) ? "RARELY" :
                                (App.get(familyPracticeInfectionControl).equals(getResources().getString(R.string.pmdt_never)) ? "NEVER" : "UNKNOWN")))});

        observations.add(new String[]{"TREATMENT SUPPORTER WAIT AFTER ADMINISTERING DRUGS", App.get(waitAfterAdministerDrug).equals(getResources().getString(R.string.pmdt_does_not_wait)) ? "DOES NOT WAIT" :
                (App.get(waitAfterAdministerDrug).equals(getResources().getString(R.string.pmdt_less_than_ten_mins)) ? "LESS THAN 10 MINUTES" :
                        (App.get(waitAfterAdministerDrug).equals(getResources().getString(R.string.pmdt_ten_to_twenty_mins)) ? "10 TO 20 MINUTES" :
                                (App.get(waitAfterAdministerDrug).equals(getResources().getString(R.string.pmdt_twenty_to_thirty_mins)) ? "20 TO 30 MINUTES" :
                                        (App.get(waitAfterAdministerDrug).equals(getResources().getString(R.string.pmdt_thirty_mins_an_hour)) ? "30 MINUTES TO ONE HOUR" : "MORE THAN AN HOUR"))))});

        observations.add(new String[]{"TREATMENT SUPPORTER CONTACT SCREENING", App.get(registerHouseholdMembers).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if (contactScreeningPerformedDate.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"LAST CONTACT SCREENING PERFORMED DATE", App.getSqlDate(thirdDateCalendar)});

        observations.add(new String[]{"PATIENT SATISFIED WITH TREATMENT SUPPORTER", App.get(patientSatisfied).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        observations.add(new String[]{"PATIENT SUBMITTED SPUTUM LAST VISIT", App.get(sputumSubmittedLastVisit).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(sputumSubmittedLastVisit).equals(getResources().getString(R.string.no)) ? "NO" : "NOT APPLICABLE")});

        if (reasonNotSubmittedSample.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON NOT SUBMITTED SPUTUM", App.get(sputumSubmittedLastVisit).equals(getResources().getString(R.string.pmdt_could_not_produce_sputum)) ? "COULD NOT PRODUCE SAMPLE" :
                    (App.get(sputumSubmittedLastVisit).equals(getResources().getString(R.string.pmdt_doctor_did_not_request)) ? "DOCTOR DID NOT REQUEST" : "OTHER REASON NOT SUBMITTED SPUTUM")});

        if (otherReasonNotSubmittedSample.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON NOT SUBMITTED SPUTUM", App.get(otherReasonNotSubmittedSample)});

        observations.add(new String[]{"PATIENT FOOD BASKET INCENTIVE", App.get(foodBasketIncentiveLastMonth).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(foodBasketIncentiveLastMonth).equals(getResources().getString(R.string.no)) ? "NO" : "REFUSED")});


        observations.add(new String[]{"ADVERSE EVENT REPORTED", App.get(adverseEventLastVisit).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(adverseEventLastVisit).equals(getResources().getString(R.string.no)) ? "NO" : "REFUSED")});

        //actionAdverseEvent
        if (actionAdverseEvent.getVisibility() == View.VISIBLE) {
            String actionAdverseEventString = "";
            for (CheckBox cb : actionAdverseEvent.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_arranged_follow_up)))
                    actionAdverseEventString = actionAdverseEventString + "ARRANGED FOLLOW UP NEXT DAY FOR CLINICAL REVIEW" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_consulted_and_advised)))
                    actionAdverseEventString = actionAdverseEventString + "CONSULTED ON PHONE WITH TB DOCTOR AND ADVISED ANCILLARY MEDICATIONS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_consulted_and_arranged_transfer)))
                    actionAdverseEventString = actionAdverseEventString + "CONSULTED ON PHONE WITH TB DOCTOR AND ARRANGED TRANSFER TO REFERRAL HOSPITAL" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_no_action_required)))
                    actionAdverseEventString = actionAdverseEventString + "NO ACTION WAS REQUIRED" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_other)))
                    actionAdverseEventString = actionAdverseEventString + "OTHER ACTION ADVERSE EVENT" + " ; ";
            }
            observations.add(new String[]{"ACTION ADVERSE EVENT", actionAdverseEventString});
        }

        if (otherActionAdverseEvent.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER ACTION ADVERSE EVENT", App.get(otherActionAdverseEvent)});

        observations.add(new String[]{"TREATMENT SUPPORTER ADDRESS SOCIAL PROBLEMS", App.get(addressSocialProblems).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(addressSocialProblems).equals(getResources().getString(R.string.no)) ? "NO" : "REFUSED")});


        observations.add(new String[]{"REFER FOR COUNSELING", App.get(referCounseling).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if (counselingType.getVisibility() == View.VISIBLE) {
            String conselingTypeString = "";
            for (CheckBox cb : counselingType.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_adherence)))
                    conselingTypeString = conselingTypeString + "ADHERENCE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_infection_control)))
                    conselingTypeString = conselingTypeString + "INFECTION CONTROL COUNSELLING" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_adverse_events)))
                    conselingTypeString = conselingTypeString + "ADVERSE EVENTS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_psychosocial_issues)))
                    conselingTypeString = conselingTypeString + "PSYCHOSOCIAL COUNSELING" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_substance_abuse)))
                    conselingTypeString = conselingTypeString + "SUBSTANCE ABUSE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_other)))
                    conselingTypeString = conselingTypeString + "OTHER COUNSELING TYPE" + " ; ";
            }
            observations.add(new String[]{"COUNSELING TYPE", conselingTypeString});
        }

        if (otherCounselingType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER COUNSELING TYPE", App.get(otherCounselingType)});

        if (!App.get(treatmentCoordinatorNotes).isEmpty())
            observations.add(new String[]{"TREATMENT COORDINATOR MONITORING NOTES", App.get(treatmentCoordinatorNotes)});

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

                String result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
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
        return false;
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
        } else if (view == visitDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        } else if (view == contactScreeningPerformedDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");
        }
    }

    @Override
    public void refill(int encounterId) {

        OfflineForm offlineForm = serverService.getOfflineFormById(encounterId);
        String date = offlineForm.getFormDate();
        ArrayList<String[][]> obsValue = offlineForm.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("TREATMENT SUPPORTER ID")) {
                treatmentSupporterId.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("TREATMENT SUPPORTER FIRST NAME")) {
                treatmentSupporterLastName.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("TREATMENT SUPPORTER LAST NAME")) {
                treatmentSupporterLastName.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("VISIT DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                visitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());

            } else if (obs[0][0].equals("PATIENT UNDERSTAND TB REGIMEN")) {
                for (RadioButton rb : patientUnderstandTbRegimen.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("PATIENT KNOW COMMON SIDE EFFECTS")) {
                for (RadioButton rb : patientKnowSideEffects.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TREATMENT SUPPORTER PROVIDE DOT")) {
                for (RadioButton rb : treatmentSupporterProvideDot.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pmdt_not_at_all)) && obs[0][1].equals("NOT AT ALL")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_regularly)) && obs[0][1].equals("REGULARLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_irregularly)) && obs[0][1].equals("IRREGULARLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TREATMENT SUPPORTER PROVIDE DOT IRREGULARLY")) {
                String value = obs[0][1].equals("6 DAYS PER WEEK") ? getResources().getString(R.string.pmdt_six_days_per_week) :
                        (obs[0][1].equals("EVERY OTHER DAY") ? getResources().getString(R.string.pmdt_every_other_day) :
                                (obs[0][1].equals("THRICE A WEEK") ? getResources().getString(R.string.pmdt_three_days_per_week) :
                                        (obs[0][1].equals("4-5 DAYS PER WEEK") ? getResources().getString(R.string.pmdt_four_five_days_a_week) :
                                                (obs[0][1].equals("2-3 DAYS A WEEK") ? getResources().getString(R.string.pmdt_two_three_days_a_week) :
                                                        (obs[0][1].equals("WEEKLY") ? getResources().getString(R.string.pmdt_once_a_week) :
                                                                (obs[0][1].equals("EVERY TWO WEEKS") ? getResources().getString(R.string.pmdt_every_two_weeks) : getResources().getString(R.string.pmdt_once_a_month)))))));
                treatmentSupporterProvideDotIrregularly.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("PLACE OF DOT")) {
                for (RadioButton rb : placeDot.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pmdt_patient_home)) && obs[0][1].equals("PATIENT HOME")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_treatment_supporter_workplace)) && obs[0][1].equals("TREATMENT SUPPORTER WORKPLACE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_other)) && obs[0][1].equals("OTHER PLACE OF DOT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_patient_workplace)) && obs[0][1].equals("PATIENT WORKPLACE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER PLACE OF DOT")) {
                otherPlaceDot.getEditText().setText(obs[0][1]);
                otherPlaceDot.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("DOT AT LAST PRESCRIBED DAY")) {
                for (RadioButton rb : dotLastPrescribedDay.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TREATMENT SUPPORTER ADMINISTER INJECTION")) {
                for (RadioButton rb : administerInjections.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_not_applicable)) && obs[0][1].equals("NOT APPLICABLE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("PATIENT MISSED DOSE")) {
                for (RadioButton rb : missedDose.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("REASON MISSED DOSE")) {

                String value = obs[0][1].equals("ADVERSE EVENTS") ? getResources().getString(R.string.pmdt_adverse_events) :
                        (obs[0][1].equals("DRUG NOT AVAILABLE") ? getResources().getString(R.string.pmdt_drug_not_available) :
                                (obs[0][1].equals("TREATMENT SUPPORTER DID NOT COME") ? getResources().getString(R.string.pmdt_treatment_supporter_did_not_come) :
                                        (obs[0][1].equals("REFUSED") ? getResources().getString(R.string.pmdt_refused) :
                                                (obs[0][1].equals("UNKNOWN") ? getResources().getString(R.string.pmdt_unknown) : getResources().getString(R.string.pmdt_other)))));

                reasonMissedDose.getSpinner().selectValue(value);

            } else if (obs[0][0].equals("OTHER REASON MISSED DOSE")) {
                otherReasonMissedDose.getEditText().setText(obs[0][1]);
                otherReasonMissedDose.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT SUPPORTER PRACTICE INFECTION CONTROL MEASURES")) {

                String value = obs[0][1].equals("ALWAYS") ? getResources().getString(R.string.pmdt_always) :
                        (obs[0][1].equals("SOMETIMES") ? getResources().getString(R.string.pmdt_sometimes) :
                                (obs[0][1].equals("RARELY") ? getResources().getString(R.string.pmdt_rarely) :
                                        (obs[0][1].equals("NEVER") ? getResources().getString(R.string.pmdt_never) : getResources().getString(R.string.pmdt_unknown))));

                practiceInfectionControl.getSpinner().selectValue(value);

            } else if (obs[0][0].equals("FAMILY PRACTICE INFECTION CONTROL MEASURES")) {

                String value = obs[0][1].equals("ALWAYS") ? getResources().getString(R.string.pmdt_always) :
                        (obs[0][1].equals("SOMETIMES") ? getResources().getString(R.string.pmdt_sometimes) :
                                (obs[0][1].equals("RARELY") ? getResources().getString(R.string.pmdt_rarely) :
                                        (obs[0][1].equals("NEVER") ? getResources().getString(R.string.pmdt_never) : getResources().getString(R.string.pmdt_unknown))));

                familyPracticeInfectionControl.getSpinner().selectValue(value);

            } else if (obs[0][0].equals("TREATMENT SUPPORTER WAIT AFTER ADMINISTERING DRUGS")) {

                String value = obs[0][1].equals("DOES NOT WAIT") ? getResources().getString(R.string.pmdt_does_not_wait) :
                        (obs[0][1].equals("LESS THAN 10 MINUTES") ? getResources().getString(R.string.pmdt_less_than_ten_mins) :
                                (obs[0][1].equals("10 TO 20 MINUTES") ? getResources().getString(R.string.pmdt_ten_to_twenty_mins) :
                                        (obs[0][1].equals("20 TO 30 MINUTES") ? getResources().getString(R.string.pmdt_twenty_to_thirty_mins) :
                                                (obs[0][1].equals("30 MINUTES TO ONE HOUR") ? getResources().getString(R.string.pmdt_thirty_mins_an_hour) : getResources().getString(R.string.pmdt_more_than_an_hour)))));

                waitAfterAdministerDrug.getSpinner().selectValue(value);

            } else if (obs[0][0].equals("TREATMENT SUPPORTER CONTACT SCREENING")) {
                for (RadioButton rb : registerHouseholdMembers.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("LAST CONTACT SCREENING PERFORMED DATE")) {
                String secondDate = obs[0][1];
                thirdDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                contactScreeningPerformedDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());

            } else if (obs[0][0].equals("PATIENT SATISFIED WITH TREATMENT SUPPORTER")) {
                for (RadioButton rb : patientSatisfied.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("PATIENT SUBMITTED SPUTUM LAST VISIT")) {
                for (RadioButton rb : sputumSubmittedLastVisit.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_not_applicable)) && obs[0][1].equals("NOT APPLICABLE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("REASON NOT SUBMITTED SPUTUM")) {
                for (RadioButton rb : reasonNotSubmittedSample.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pmdt_could_not_produce_sputum)) && obs[0][1].equals("COULD NOT PRODUCE SAMPLE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_doctor_did_not_request)) && obs[0][1].equals("DOCTOR DID NOT REQUEST")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_other)) && obs[0][1].equals("OTHER REASON NOT SUBMITTED SPUTUM")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER REASON NOT SUBMITTED SPUTUM")) {
                otherReasonNotSubmittedSample.getEditText().setText(obs[0][1]);
                otherReasonNotSubmittedSample.setVisibility(View.VISIBLE);

            } else if (obs[0][0].equals("PATIENT FOOD BASKET INCENTIVE")) {
                for (RadioButton rb : foodBasketIncentiveLastMonth.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("ADVERSE EVENT REPORTED")) {
                for (RadioButton rb : adverseEventLastVisit.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("ACTION ADVERSE EVENT")) {
                for (CheckBox cb : actionAdverseEvent.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pmdt_arranged_follow_up)) && obs[0][1].equals("ARRANGED FOLLOW UP NEXT DAY FOR CLINICAL REVIEW")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pmdt_consulted_and_advised)) && obs[0][1].equals("CONSULTED ON PHONE WITH TB DOCTOR AND ADVISED ANCILLARY MEDICATIONS")) {
                        cb.setChecked(true);
                        break;
                    }
                    if (cb.getText().equals(getResources().getString(R.string.pmdt_consulted_and_arranged_transfer)) && obs[0][1].equals("CONSULTED ON PHONE WITH TB DOCTOR AND ARRANGED TRANSFER TO REFERRAL HOSPITAL")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pmdt_no_action_required)) && obs[0][1].equals("NO ACTION WAS REQUIRED")) {
                        cb.setChecked(true);
                        break;
                    }
                    if (cb.getText().equals(getResources().getString(R.string.pmdt_other)) && obs[0][1].equals("OTHER ACTION ADVERSE EVENT")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER ACTION ADVERSE EVENT")) {
                otherActionAdverseEvent.getEditText().setText(obs[0][1]);
                otherActionAdverseEvent.setVisibility(View.VISIBLE);

            } else if (obs[0][0].equals("TREATMENT SUPPORTER ADDRESS SOCIAL PROBLEMS")) {
                for (RadioButton rb : addressSocialProblems.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("REFER FOR COUNSELING")) {
                for (RadioButton rb : referCounseling.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUNSELING TYPE")) {
                for (CheckBox cb : counselingType.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pmdt_adherence)) && obs[0][1].equals("ADHERENCE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pmdt_infection_control)) && obs[0][1].equals("INFECTION CONTROL COUNSELLING")) {
                        cb.setChecked(true);
                        break;
                    }
                    if (cb.getText().equals(getResources().getString(R.string.pmdt_adverse_events)) && obs[0][1].equals("ADVERSE EVENTS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pmdt_psychosocial_issues)) && obs[0][1].equals("PSYCHOSOCIAL COUNSELING")) {
                        cb.setChecked(true);
                        break;
                    }
                    if (cb.getText().equals(getResources().getString(R.string.pmdt_substance_abuse)) && obs[0][1].equals("SUBSTANCE ABUSE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pmdt_other)) && obs[0][1].equals("OTHER COUNSELING TYPE")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                counselingType.setVisibility(View.VISIBLE);

            } else if (obs[0][0].equals("OTHER COUNSELING TYPE")) {
                otherCounselingType.getEditText().setText(obs[0][1]);
                otherCounselingType.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT COORDINATOR MONITORING NOTES")) {
                treatmentCoordinatorNotes.getEditText().setText(obs[0][1]);
                treatmentCoordinatorNotes.setVisibility(View.VISIBLE);
            }
        }
        submitButton.setEnabled(true);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        MySpinner spinner = (MySpinner) parent;
        if (spinner == reasonMissedDose.getSpinner()) {
            if (App.get(reasonMissedDose).equals(getResources().getString(R.string.pmdt_other)))
                otherReasonMissedDose.setVisibility(View.VISIBLE);
            else
                otherReasonMissedDose.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton checkBoxGroup, boolean isChecked) {

        for (CheckBox cb : actionAdverseEvent.getCheckedBoxes()) {

            if (App.get(cb).equals(getResources().getString(R.string.pmdt_other))) {
                if (cb.isChecked()) {
                    otherActionAdverseEvent.setVisibility(View.VISIBLE);
                } else {
                    otherActionAdverseEvent.setVisibility(View.GONE);
                }
            }
        }

        for (CheckBox cb : counselingType.getCheckedBoxes()) {

            if (App.get(cb).equals(getResources().getString(R.string.pmdt_other))) {
                if (cb.isChecked()) {
                    otherCounselingType.setVisibility(View.VISIBLE);
                } else {
                    otherCounselingType.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        visitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        contactScreeningPerformedDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int encounterId = Integer.valueOf(id);

                refill(encounterId);

            } else bundle.putBoolean("save", false);
        }

        if (App.get(treatmentSupporterId).equals("")) {
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
                    String treatmentSupporterId = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PMDT_PATIENT_ASSIGNMENT, "TREATMENT SUPPORTER ID");
                    String treatmentSupporterFirstName = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PMDT_PATIENT_ASSIGNMENT, "TREATMENT SUPPORTER FIRST NAME");
                    String treatmentSupporterLastName = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PMDT_PATIENT_ASSIGNMENT, "TREATMENT SUPPORTER LAST NAME");


                    if (treatmentSupporterId != null && !treatmentSupporterId.isEmpty())
                        result.put("TREATMENT SUPPORTER ID", treatmentSupporterId);

                    if (treatmentSupporterFirstName != null && !treatmentSupporterFirstName.isEmpty())
                        result.put("TREATMENT SUPPORTER FIRST NAME", treatmentSupporterFirstName);

                    if (treatmentSupporterLastName != null && !treatmentSupporterLastName.isEmpty())
                        result.put("TREATMENT SUPPORTER LAST NAME", treatmentSupporterLastName);

                    return result;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                }


                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();

                    if (result.get("TREATMENT SUPPORTER ID") == null || result.get("TREATMENT SUPPORTER ID").isEmpty()) {

                        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        alertDialog.setMessage(getResources().getString(R.string.pmdt_patient_assignment_missing));
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

                        submitButton.setEnabled(false);

                        return;
                    } else
                        submitButton.setEnabled(true);

                    treatmentSupporterId.getEditText().setText(result.get("TREATMENT SUPPORTER ID"));
                    treatmentSupporterFirstName.getEditText().setText(result.get("TREATMENT SUPPORTER FIRST NAME"));
                    treatmentSupporterLastName.getEditText().setText(result.get("TREATMENT SUPPORTER LAST NAME"));

                }
            };
            autopopulateFormTask.execute("");

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (group == treatmentSupporterProvideDot.getRadioGroup()) {

            if (App.get(treatmentSupporterProvideDot).equals(getResources().getString(R.string.pmdt_irregularly))) {
                treatmentSupporterProvideDotIrregularly.setVisibility(View.VISIBLE);
                dotLastPrescribedDay.setVisibility(View.VISIBLE);
                placeDot.setVisibility(View.VISIBLE);
                if (App.get(placeDot).equals(getResources().getString(R.string.pmdt_other)))
                    otherPlaceDot.setVisibility(View.VISIBLE);

            } else if (App.get(treatmentSupporterProvideDot).equals(getResources().getString(R.string.pmdt_regularly))) {
                treatmentSupporterProvideDotIrregularly.setVisibility(View.GONE);
                dotLastPrescribedDay.setVisibility(View.VISIBLE);
                placeDot.setVisibility(View.VISIBLE);
                if (App.get(placeDot).equals(getResources().getString(R.string.pmdt_other)))
                    otherPlaceDot.setVisibility(View.VISIBLE);

            } else if (App.get(treatmentSupporterProvideDot).equals(getResources().getString(R.string.pmdt_unknown))) {
                treatmentSupporterProvideDotIrregularly.setVisibility(View.GONE);
                placeDot.setVisibility(View.GONE);
                otherPlaceDot.setVisibility(View.GONE);
                dotLastPrescribedDay.setVisibility(View.VISIBLE);

            } else {
                placeDot.setVisibility(View.GONE);
                otherPlaceDot.setVisibility(View.GONE);
                treatmentSupporterProvideDotIrregularly.setVisibility(View.GONE);
                dotLastPrescribedDay.setVisibility(View.GONE);
            }

        } else if (group == placeDot.getRadioGroup()) {
            if (App.get(placeDot).equals(getResources().getString(R.string.pmdt_other)))
                otherPlaceDot.setVisibility(View.VISIBLE);
            else
                otherPlaceDot.setVisibility(View.GONE);

        } else if (group == missedDose.getRadioGroup()) {
            if (App.get(missedDose).equals(getResources().getString(R.string.yes))) {
                reasonMissedDose.setVisibility(View.VISIBLE);
                if (App.get(reasonMissedDose).equals(getResources().getString(R.string.pmdt_other)))
                    otherReasonMissedDose.setVisibility(View.VISIBLE);
            } else {
                reasonMissedDose.setVisibility(View.GONE);
                otherReasonMissedDose.setVisibility(View.GONE);
            }

        } else if (group == registerHouseholdMembers.getRadioGroup()) {
            if (App.get(registerHouseholdMembers).equals(getResources().getString(R.string.yes)))
                contactScreeningPerformedDate.setVisibility(View.VISIBLE);
            else
                contactScreeningPerformedDate.setVisibility(View.GONE);

        } else if (group == sputumSubmittedLastVisit.getRadioGroup()) {
            if (App.get(sputumSubmittedLastVisit).equals(getResources().getString(R.string.no))) {
                reasonNotSubmittedSample.setVisibility(View.VISIBLE);
                if (App.get(reasonNotSubmittedSample).equals(getResources().getString(R.string.pmdt_other)))
                    otherReasonNotSubmittedSample.setVisibility(View.VISIBLE);
            } else {
                reasonNotSubmittedSample.setVisibility(View.GONE);
                otherReasonNotSubmittedSample.setVisibility(View.GONE);
            }

        } else if (group == reasonNotSubmittedSample.getRadioGroup()) {
            if (App.get(reasonNotSubmittedSample).equals(getResources().getString(R.string.pmdt_other)))
                otherReasonNotSubmittedSample.setVisibility(View.VISIBLE);
            else
                otherReasonNotSubmittedSample.setVisibility(View.GONE);

        } else if (group == adverseEventLastVisit.getRadioGroup()) {
            if (App.get(adverseEventLastVisit).equals(getResources().getString(R.string.yes)))
                actionAdverseEvent.setVisibility(View.VISIBLE);
            else {
                actionAdverseEvent.setVisibility(View.GONE);
                otherActionAdverseEvent.setVisibility(View.GONE);
            }

        } else if (group == referCounseling.getRadioGroup()) {
            if (App.get(referCounseling).equals(getResources().getString(R.string.yes))) {
                counselingType.setVisibility(View.VISIBLE);
                for (CheckBox cb : counselingType.getCheckedBoxes()) {

                    if (App.get(cb).equals(getResources().getString(R.string.pmdt_other))) {
                        if (cb.isChecked()) {
                            otherCounselingType.setVisibility(View.VISIBLE);
                        } else {
                            otherCounselingType.setVisibility(View.GONE);
                        }
                    }
                }
            } else {
                counselingType.setVisibility(View.GONE);
                otherCounselingType.setVisibility(View.GONE);
            }
        }
        submitButton.setEnabled(true);
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

    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar;
            if (getArguments().getInt("type") == THIRD_DATE_DIALOG_ID)
                calendar = thirdDateCalendar;
//            else if (getArguments().getInt("type") == SECOND_DATE_DIALOG_ID)
//                calendar = secondDateCalendar;
            else
                return null;

            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setTag(getArguments().getInt("type"));
            if (!getArguments().getBoolean("allowFutureDate", false)) {
                Date date = new Date();
                date.setHours(24);
                date.setSeconds(60);
                dialog.getDatePicker().setMaxDate(date.getTime());
            }
            if (!getArguments().getBoolean("allowPastDate", false))
                dialog.getDatePicker().setMinDate(new Date().getTime());
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            if (((int) view.getTag()) == THIRD_DATE_DIALOG_ID)
                thirdDateCalendar.set(yy, mm, dd);
            updateDisplay();
        }
    }
}
