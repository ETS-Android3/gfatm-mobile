package com.ihsinformatics.gfatmmobile.pmdt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

        formDate.getButton().setOnClickListener(this);
        visitDate.getButton().setOnClickListener(this);
        contactScreeningPerformedDate.getButton().setOnClickListener(this);
        patientUnderstandTbRegimen.getRadioGroup().setOnCheckedChangeListener(this);
        patientKnowSideEffects.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentSupporterProvideDot.getRadioGroup().setOnCheckedChangeListener(this);
        placeDot.getRadioGroup().setOnCheckedChangeListener(this);
        dotLastPrescribedDay.getRadioGroup().setOnCheckedChangeListener(this);
//        administerInjections.getRadioGroup().setOnCheckedChangeListener(this);
        missedDose.getRadioGroup().setOnCheckedChangeListener(this);
        reasonMissedDose.getSpinner().setOnItemSelectedListener(this);
        registerHouseholdMembers.getRadioGroup().setOnCheckedChangeListener(this);
        patientSatisfied.getRadioGroup().setOnCheckedChangeListener(this);
        sputumSubmittedLastVisit.getRadioGroup().setOnCheckedChangeListener(this);
        reasonNotSubmittedSample.getRadioGroup().setOnCheckedChangeListener(this);
        foodBasketIncentiveLastMonth.getRadioGroup().setOnCheckedChangeListener(this);
        adverseEventLastVisit.getRadioGroup().setOnCheckedChangeListener(this);
        addressSocialProblems.getRadioGroup().setOnCheckedChangeListener(this);
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
        }

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
            if (App.get(referCounseling).equals(getResources().getString(R.string.yes)))
                counselingType.setVisibility(View.VISIBLE);
            else {
                counselingType.setVisibility(View.GONE);
                otherCounselingType.setVisibility(View.GONE);
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
