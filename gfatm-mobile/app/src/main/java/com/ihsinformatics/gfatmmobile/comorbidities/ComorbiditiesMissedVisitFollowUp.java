package com.ihsinformatics.gfatmmobile.comorbidities;


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
import android.widget.RadioButton;
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
 * Created by MUHAMMAD WAQAS on 10/8/2017.
 */

public class ComorbiditiesMissedVisitFollowUp extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledSpinner diabetesEyeScreeningMonthOfTreatment;
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

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        diabetesEyeScreeningMonthOfTreatment = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_urinedr_month_of_treatment), getResources().getStringArray(R.array.comorbidities_followup_month), "0", App.HORIZONTAL);
        missedVisitTreatmentRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_missed_visit_for_treatment_regimen), getResources().getStringArray(R.array.comorbidities_missed_visit_for_treatment_regimen_array), getResources().getString(R.string.comorbidities_missed_visit_for_treatment_regimen_diabetes), App.VERTICAL, App.VERTICAL);
        missedVisitDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_missed_visit_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_return_visit_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        patientContacted = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_patient_contacted), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        ReasonPatientNotContacted = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_reason_patient_not_contacted), getResources().getStringArray(R.array.comorbidities_reason_patient_not_contacted_array), getResources().getString(R.string.comorbidities_reason_patient_not_contacted_phone_off), App.VERTICAL, App.VERTICAL);
        referralTransfer = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_referral_transfer), getResources().getStringArray(R.array.comorbidities_referral_transfer_array), getResources().getString(R.string.comorbidities_referral_transfer_options_refrell), App.VERTICAL, App.VERTICAL);
        reasonForChangingFacilityCheckBoxes = new TitledCheckBoxes(context, null, getResources().getString(R.string.comorbidities_reason_for_changing_facility), getResources().getStringArray(R.array.comorbidities_reason_for_changing_facility_array), new Boolean[]{true, false, false, false}, App.VERTICAL, App.VERTICAL);
        ReasonPatientNotContactedOther = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_reason_patient_not_contacted_other), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        referralTransferSite = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_referral_transfer_site), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        phoneCounsellingConsent = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_phone_counselling_consent_2), getResources().getStringArray(R.array.comorbidities_phone_counselling_consent_2_array), getResources().getString(R.string.comorbidities_phone_counselling_consent_2_monthly), App.VERTICAL, App.VERTICAL);
        reasonMissedVisit = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_reason_missed_visit), getResources().getStringArray(R.array.comorbidities_reason_missed_visit_array), getResources().getString(R.string.comorbidities_reason_missed_visit_patient_relocated), App.VERTICAL, App.VERTICAL);
        ReasonMissedVisitOther = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_reason_missed_visit_other), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        newLocation = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_new_location), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        facilityTreatment = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_facility_treatment), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), missedVisitTreatmentRegimen.getRadioGroup(), missedVisitDate.getButton(), patientContacted.getRadioGroup(), ReasonPatientNotContacted.getRadioGroup(), ReasonPatientNotContactedOther.getEditText(), referralTransfer.getRadioGroup(), referralTransferSite.getEditText(), phoneCounsellingConsent.getRadioGroup(), reasonMissedVisit.getRadioGroup(), ReasonMissedVisitOther.getEditText(), newLocation.getEditText(), facilityTreatment.getEditText(), reasonForChangingFacilityCheckBoxes, returnVisitDate.getButton(),};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, missedVisitTreatmentRegimen, missedVisitDate, patientContacted, ReasonPatientNotContacted, ReasonPatientNotContactedOther, referralTransfer, referralTransferSite, phoneCounsellingConsent, reasonMissedVisit, ReasonMissedVisitOther, newLocation, facilityTreatment, reasonForChangingFacilityCheckBoxes, returnVisitDate,}};


        formDate.getButton().setOnClickListener(this);
        missedVisitDate.getButton().setOnClickListener(this);
        returnVisitDate.getButton().setOnClickListener(this);
        missedVisitTreatmentRegimen.getRadioGroup().setOnCheckedChangeListener(this);
        referralTransfer.getRadioGroup().setOnCheckedChangeListener(this);
        patientContacted.getRadioGroup().setOnCheckedChangeListener(this);
        ReasonPatientNotContacted.getRadioGroup().setOnCheckedChangeListener(this);
        phoneCounsellingConsent.getRadioGroup().setOnCheckedChangeListener(this);
        reasonMissedVisit.getRadioGroup().setOnCheckedChangeListener(this);

        for (CheckBox cb : reasonForChangingFacilityCheckBoxes.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

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
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }
        if (!(missedVisitDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = missedVisitDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (secondDateCalendar.before(formDateCalendar)/*secondDateCalendar.before(App.getCalendar(date))*/) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                //snackbar = Snackbar.make(mainContent, getResources().getString(R.string.next_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.next_visit_date_cannot_before_form_date), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        }
        formDate.getButton().setEnabled(true);
        missedVisitDate.getButton().setEnabled(true);
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

        Boolean flag = false;
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
        }

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
        observations.add(new String[]{"FOLLOW-UP MONTH", App.get(diabetesEyeScreeningMonthOfTreatment)});
        observations.add(new String[]{"RIGHT EYE EXAMINATION", App.get(missedVisitTreatmentRegimen).equals(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_yes)) ? "YES" :
                (App.get(missedVisitTreatmentRegimen).equals(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_no_deformity)) ? "EYE DEFORMITY" : "PROSTHETIC EYE")});
        observations.add(new String[]{"LEFT EYE EXAMINATION", App.get(referralTransfer).equals(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_yes)) ? "YES" :
                (App.get(referralTransfer).equals(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_no_deformity)) ? "EYE DEFORMITY" : "PROSTHETIC EYE")});

        if (App.get(missedVisitTreatmentRegimen).equalsIgnoreCase(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_yes)) ||
                App.get(referralTransfer).equalsIgnoreCase(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_yes))) {
            String diabetesEyeScreeningEvidenceEyeString = "";
            for (CheckBox cb : reasonForChangingFacilityCheckBoxes.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_evidence_eye_options_cataract)))
                    diabetesEyeScreeningEvidenceEyeString = diabetesEyeScreeningEvidenceEyeString + "CATARACT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_evidence_eye_options_glaucoma)))
                    diabetesEyeScreeningEvidenceEyeString = diabetesEyeScreeningEvidenceEyeString + "GLAUCOMA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_evidence_eye_options_other)))
                    diabetesEyeScreeningEvidenceEyeString = diabetesEyeScreeningEvidenceEyeString + "OTHER EYE PROBLEMS" + " ; ";
            }
            observations.add(new String[]{"EYE EXAMINATION FINDINGS", diabetesEyeScreeningEvidenceEyeString});

            observations.add(new String[]{"OTHER EYE PROBLEMS", App.get(ReasonPatientNotContactedOther)});
            observations.add(new String[]{"DIABETIC RETINOPATHY", App.get(patientContacted).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

            final String diabetesEyeScreeningMildDiabetecRetinopathyString = App.get(ReasonPatientNotContacted).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_none)) ? "NONE" :
                    (App.get(ReasonPatientNotContacted).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_right)) ? "IN RIGHT EYE" :
                            (App.get(ReasonPatientNotContacted).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_left)) ? "IN LEFT EYE" : "IN BOTH EYES"));
            observations.add(new String[]{"MILD NON PROLIFERATIVE DIABETIC RETINOPATHY", diabetesEyeScreeningMildDiabetecRetinopathyString});

            final String diabetesEyeScreeningModerateDiabetecRetinopathyString = App.get(phoneCounsellingConsent).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_none)) ? "NONE" :
                    (App.get(phoneCounsellingConsent).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_right)) ? "IN RIGHT EYE" :
                            (App.get(phoneCounsellingConsent).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_left)) ? "IN LEFT EYE" : "IN BOTH EYES"));
            observations.add(new String[]{"MODERATE NON PROLIFERATIVE DIABETIC RETINOPATHY", diabetesEyeScreeningModerateDiabetecRetinopathyString});

            final String diabetesEyeScreeningSevereDiabetecRetinopathyString = App.get(reasonMissedVisit).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_none)) ? "NONE" :
                    (App.get(reasonMissedVisit).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_right)) ? "IN RIGHT EYE" :
                            (App.get(reasonMissedVisit).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_left)) ? "IN LEFT EYE" : "IN BOTH EYES"));
            observations.add(new String[]{"SEVERE NON PROLIFERATIVE DIABETIC RETINOPATHY", diabetesEyeScreeningSevereDiabetecRetinopathyString});


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

            if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                diabetesEyeScreeningMonthOfTreatment.getSpinner().selectValue(obs[0][1]);
            } else if (obs[0][0].equals("RIGHT EYE EXAMINATION")) {
                for (RadioButton rb : missedVisitTreatmentRegimen.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_no_deformity)) && obs[0][1].equals("EYE DEFORMITY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_no_prosthetic)) && obs[0][1].equals("PROSTHETIC EYE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("LEFT EYE EXAMINATION")) {
                for (RadioButton rb : referralTransfer.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_no_deformity)) && obs[0][1].equals("EYE DEFORMITY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_no_prosthetic)) && obs[0][1].equals("PROSTHETIC EYE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("EYE EXAMINATION FINDINGS")) {
                for (CheckBox cb : reasonForChangingFacilityCheckBoxes.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_evidence_eye_options_cataract)) && obs[0][1].equals("CATARACT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_evidence_eye_options_glaucoma)) && obs[0][1].equals("GLAUCOMA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_evidence_eye_options_other)) && obs[0][1].equals("OTHER EYE PROBLEMS")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                reasonForChangingFacilityCheckBoxes.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER EYE PROBLEMS")) {
                ReasonPatientNotContactedOther.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("DIABETIC RETINOPATHY")) {
                for (RadioButton rb : patientContacted.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                patientContacted.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("MILD NON PROLIFERATIVE DIABETIC RETINOPATHY")) {
                for (RadioButton rb : ReasonPatientNotContacted.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_none)) && obs[0][1].equals("NONE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_right)) && obs[0][1].equals("IN RIGHT EYE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_left)) && obs[0][1].equals("IN LEFT EYE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_both)) && obs[0][1].equals("IN BOTH EYES")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                ReasonPatientNotContacted.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("MODERATE NON PROLIFERATIVE DIABETIC RETINOPATHY")) {
                for (RadioButton rb : phoneCounsellingConsent.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_none)) && obs[0][1].equals("NONE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_right)) && obs[0][1].equals("IN RIGHT EYE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_left)) && obs[0][1].equals("IN LEFT EYE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_both)) && obs[0][1].equals("IN BOTH EYES")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                phoneCounsellingConsent.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("SEVERE NON PROLIFERATIVE DIABETIC RETINOPATHY")) {
                for (RadioButton rb : reasonMissedVisit.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_none)) && obs[0][1].equals("NONE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_right)) && obs[0][1].equals("IN RIGHT EYE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_left)) && obs[0][1].equals("IN LEFT EYE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_both)) && obs[0][1].equals("IN BOTH EYES")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                reasonMissedVisit.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("PROLIFERATIVE DIABETIC RETINOPATHY")) {

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
            args.putBoolean("allowPastDate", false);
            args.putBoolean("allowFutureDate", true);
            args.putString("formDate", formDate.getButtonText());
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        } else if (view == returnVisitDate.getButton()) {
            returnVisitDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", false);
            args.putBoolean("allowFutureDate", true);
            args.putString("formDate", formDate.getButtonText());
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
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
        ReasonPatientNotContacted.setVisibility(View.GONE);

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
                    String monthOfTreatment = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.COMORBIDITIES_VITALS_FORM, "FOLLOW-UP MONTH");

                    if (monthOfTreatment != null && !monthOfTreatment.equals(""))
                        monthOfTreatment = monthOfTreatment.replace(".0", "");

                    if (monthOfTreatment != null)
                        if (!monthOfTreatment.equals(""))
                            result.put("FOLLOW-UP MONTH", monthOfTreatment);

                    return result;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                }

                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();

                    if (result.get("FOLLOW-UP MONTH") != null)
                        diabetesEyeScreeningMonthOfTreatment.getSpinner().selectValue(result.get("FOLLOW-UP MONTH"));

                }
            };
            autopopulateFormTask.execute("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    void showHideReasonPatientNotContacted() {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == patientContacted.getRadioGroup()) {
            if (patientContacted.getRadioGroupSelectedValue().equals(getResources().getString(R.string.yes))) {
                ReasonPatientNotContacted.setVisibility(View.GONE);
            } else if (patientContacted.getRadioGroupSelectedValue().equals(getResources().getString(R.string.yes))) {
                ReasonPatientNotContacted.setVisibility(View.VISIBLE);

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




