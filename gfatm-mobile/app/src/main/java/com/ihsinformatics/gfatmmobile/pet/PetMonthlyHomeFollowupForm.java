package com.ihsinformatics.gfatmmobile.pet;

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
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class PetMonthlyHomeFollowupForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledRadioGroup intervention;
    TitledRadioGroup cough;
    TitledRadioGroup coughDuration;
    TitledRadioGroup haemoptysis;
    TitledRadioGroup fever;
    TitledRadioGroup weightLoss;
    TitledRadioGroup reduceAppetite;
    TitledRadioGroup reduceActivity;
    TitledRadioGroup nightSweats;
    TitledRadioGroup swelling;
    TitledEditText missedDosage;
    TitledRadioGroup adverseEventReport;
    LinearLayout adverseEffectsLayout;
    TitledCheckBoxes adverseEffects1;
    TitledCheckBoxes adverseEffects2;
    TitledEditText otherEffects;
    TitledRadioGroup clinicianInformed;
    TitledRadioGroup visitSuggested;
    TitledRadioGroup clinicalReferral;

    ScrollView scrollView;

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
        FORM_NAME = Forms.PET_HOME_FOLLOWUP;
        FORM = Forms.pet_homeFollowup;

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
                scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        intervention = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_intervention), getResources().getStringArray(R.array.pet_interventions), "", App.HORIZONTAL, App.VERTICAL);

        MyLinearLayout linearLayout = new MyLinearLayout(context, getResources().getString(R.string.pet_followup_details), App.VERTICAL);
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_has_cough), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        coughDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_cough_duration), getResources().getStringArray(R.array.pet_cough_durations), getResources().getString(R.string.pet_less_than_2_weeks), App.VERTICAL, App.VERTICAL);
        haemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_haemoptysis), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_has_fever), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_have_weight_loss), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        reduceAppetite = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_reduced_appetite), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        reduceActivity = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_reduced_activity), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_have_night_sweats), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        swelling = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_swelling), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        missedDosage = new TitledEditText(context, null, getResources().getString(R.string.pet_missed_dosed), "0", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);
        adverseEventReport = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adverse_event_report), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        adverseEffectsLayout = new LinearLayout(context);
        adverseEffectsLayout.setOrientation(LinearLayout.HORIZONTAL);
        adverseEffects1 = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_adverse_effects), getResources().getStringArray(R.array.pet_adverse_effects_1), null, App.VERTICAL, App.VERTICAL, true);
        adverseEffectsLayout.addView(adverseEffects1);
        adverseEffects2 = new TitledCheckBoxes(context, null, "", getResources().getStringArray(R.array.pet_adverse_effects_2), null, App.VERTICAL, App.VERTICAL);
        adverseEffectsLayout.addView(adverseEffects2);
        otherEffects = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        clinicianInformed = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_clinician_informed), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        visitSuggested = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_visit_suggested), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        clinicalReferral = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_clinical_referrals), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);

        linearLayout.addView(cough);
        linearLayout.addView(coughDuration);
        linearLayout.addView(haemoptysis);
        linearLayout.addView(fever);
        linearLayout.addView(weightLoss);
        linearLayout.addView(reduceAppetite);
        linearLayout.addView(reduceActivity);
        linearLayout.addView(nightSweats);
        linearLayout.addView(swelling);
        linearLayout.addView(missedDosage);
        linearLayout.addView(adverseEventReport);
        linearLayout.addView(adverseEffectsLayout);
        linearLayout.addView(otherEffects);
        linearLayout.addView(clinicianInformed);
        linearLayout.addView(visitSuggested);
        linearLayout.addView(clinicalReferral);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), cough.getRadioGroup(), coughDuration.getRadioGroup(), fever.getRadioGroup(), weightLoss.getRadioGroup(), reduceAppetite.getRadioGroup(),
                reduceActivity.getRadioGroup(), nightSweats.getRadioGroup(), swelling.getRadioGroup(), adverseEventReport.getRadioGroup(), intervention.getRadioGroup(),
                adverseEffects1, adverseEffects2, clinicianInformed.getRadioGroup(), visitSuggested.getRadioGroup(), clinicalReferral.getRadioGroup(), missedDosage.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, intervention, linearLayout}};

        formDate.getButton().setOnClickListener(this);
        cough.getRadioGroup().setOnCheckedChangeListener(this);
        adverseEventReport.getRadioGroup().setOnCheckedChangeListener(this);

        coughDuration.setVisibility(View.GONE);
        haemoptysis.setVisibility(View.GONE);
        adverseEffectsLayout.setVisibility(View.GONE);
        otherEffects.setVisibility(View.GONE);
        for (CheckBox cb : adverseEffects2.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        resetViews();

    }


    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        coughDuration.setVisibility(View.GONE);
        haemoptysis.setVisibility(View.GONE);
        adverseEffectsLayout.setVisibility(View.GONE);
        otherEffects.setVisibility(View.GONE);

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
    public void updateDisplay() {

        if(refillFlag){
            refillFlag = true;
            return;
        }

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setEnabled(true);

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();
            personDOB = personDOB.substring(0,10);

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

        Boolean error = false;
        View view = null;

        if (App.get(otherEffects).isEmpty() && otherEffects.getVisibility() == View.VISIBLE) {
            otherEffects.getEditText().setError(getString(R.string.empty_field));
            otherEffects.getEditText().requestFocus();
            error = true;
        } else
            otherEffects.clearFocus();

        if (adverseEffectsLayout.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : adverseEffects1.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                for (CheckBox cb : adverseEffects2.getCheckedBoxes()) {
                    if (cb.isChecked()) {
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag) {
                adverseEffects1.getQuestionView().setError(getString(R.string.empty_field));
                adverseEffects1.getQuestionView().requestFocus();
                view = adverseEffects1;
                gotoLastPage();
                error = true;
            }
        }
        if (App.get(missedDosage).isEmpty()) {
            missedDosage.getEditText().setError(getString(R.string.empty_field));
            missedDosage.getEditText().requestFocus();
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
        } else
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

        observations.add(new String[]{"INTERVENTION", App.get(intervention).equals(getResources().getString(R.string.pet)) ? "PET" : "SCI"});
        observations.add(new String[]{"COUGH", App.get(cough).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(cough).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(cough).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        if (coughDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUGH DURATION", App.get(coughDuration).equals(getResources().getString(R.string.pet_less_than_2_weeks)) ? "COUGH LASTING LESS THAN 2 WEEKS" :
                    (App.get(coughDuration).equals(getResources().getString(R.string.pet_two_three_weeks)) ? "COUGH LASTING MORE THAN 2 WEEKS" :
                            (App.get(coughDuration).equals(getResources().getString(R.string.pet_more_than_3_weeks)) ? "COUGH LASTING MORE THAN 3 WEEKS" :
                                    (App.get(coughDuration).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSED")))});
        if (haemoptysis.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HEMOPTYSIS", App.get(haemoptysis).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(haemoptysis).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(haemoptysis).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        observations.add(new String[]{"FEVER", App.get(fever).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(fever).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(fever).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        observations.add(new String[]{"WEIGHT LOSS", App.get(weightLoss).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(weightLoss).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(weightLoss).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        observations.add(new String[]{"LOSS OF APPETITE", App.get(reduceAppetite).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(reduceAppetite).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(reduceAppetite).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        observations.add(new String[]{"REDUCED MOBILITY", App.get(reduceActivity).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(reduceActivity).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(reduceActivity).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        observations.add(new String[]{"NIGHT SWEATS", App.get(nightSweats).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(nightSweats).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(nightSweats).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        observations.add(new String[]{"SWELLING", App.get(swelling).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(swelling).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(swelling).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        observations.add(new String[]{"NUMBER OF MISSED MEDICATION DOSES IN LAST MONTH", App.get(missedDosage)});
        observations.add(new String[]{"ADVERSE EVENTS REPORTED", App.get(adverseEventReport).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (adverseEffectsLayout.getVisibility() == View.VISIBLE) {
            String adverseEventString = "";
            for (CheckBox cb : adverseEffects1.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_joint_pain)))
                    adverseEventString = adverseEventString + "JOINT PAIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_headache)))
                    adverseEventString = adverseEventString + "HEADACHE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_skin_rash)))
                    adverseEventString = adverseEventString + "RASH" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_nausea)))
                    adverseEventString = adverseEventString + "NAUSEA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_dizziness)))
                    adverseEventString = adverseEventString + "DIZZINESS AND GIDDINESS" + " ; ";
            }
            for (CheckBox cb : adverseEffects2.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_vomiting)))
                    adverseEventString = adverseEventString + "VOMITING" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_abdominal_pain)))
                    adverseEventString = adverseEventString + "ABDOMINAL PAIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_loss_of_appetite)))
                    adverseEventString = adverseEventString + "LOSS OF APPETITE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_visual_impairment)))
                    adverseEventString = adverseEventString + "VISUAL IMPAIRMENT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other)))
                    adverseEventString = adverseEventString + "OTHER ADVERSE EVENT" + " ; ";
            }
            observations.add(new String[]{"ADVERSE EVENTS", adverseEventString});
        }
        if (otherEffects.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER ADVERSE EVENT", App.get(otherEffects)});
        observations.add(new String[]{"CLINICIAN INFORMED", App.get(clinicianInformed).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"CLINICAL VISIT SUGGESTED", App.get(visitSuggested).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"PATIENT REFERRED", App.get(clinicalReferral).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

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

        resetViews();
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
        boolean flag = false;
        for (CheckBox cb : adverseEffects2.getCheckedBoxes()) {
            if (cb.getText().equals(getString(R.string.pet_other)) && cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (flag)
            otherEffects.setVisibility(View.VISIBLE);
        else
            otherEffects.setVisibility(View.GONE);
    }

    @Override
    public void onPageSelected(int pageNo) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == cough.getRadioGroup()) {
            if (App.get(cough).equals(getResources().getString(R.string.yes))) {
                coughDuration.setVisibility(View.VISIBLE);
                haemoptysis.setVisibility(View.VISIBLE);
            } else {
                coughDuration.setVisibility(View.GONE);
                haemoptysis.setVisibility(View.GONE);
            }
        }
        if (group == adverseEventReport.getRadioGroup()) {
            if (App.get(adverseEventReport).equals(getResources().getString(R.string.no))) {
                adverseEffectsLayout.setVisibility(View.GONE);
                otherEffects.setVisibility(View.GONE);
            } else {
                adverseEffectsLayout.setVisibility(View.VISIBLE);
                boolean flag = false;
                for (CheckBox cb : adverseEffects2.getCheckedBoxes()) {
                    if (cb.getText().equals(getString(R.string.pet_other)) && cb.isChecked()) {
                        flag = true;
                        break;
                    }
                }
                if (flag)
                    otherEffects.setVisibility(View.VISIBLE);
                else
                    otherEffects.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void refill(int encounterId) {

        Boolean refillFlag = false;

        OfflineForm fo = serverService.getOfflineFormById(encounterId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            }  else if (obs[0][0].equals("INTERVENTION")) {
                for (RadioButton rb : intervention.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet)) && obs[0][1].equals("PET")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.sci)) && obs[0][1].equals("SCI")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUGH")) {
                for (RadioButton rb : cough.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUGH DURATION")) {
                for (RadioButton rb : coughDuration.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_less_than_2_weeks)) && obs[0][1].equals("COUGH LASTING LESS THAN 2 WEEKS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_two_three_weeks)) && obs[0][1].equals("COUGH LASTING MORE THAN 2 WEEKS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_more_than_3_weeks)) && obs[0][1].equals("COUGH LASTING MORE THAN 3 WEEKS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                coughDuration.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("HEMOPTYSIS")) {
                for (RadioButton rb : haemoptysis.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("FEVER")) {
                for (RadioButton rb : fever.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("WEIGHT LOSS")) {
                for (RadioButton rb : weightLoss.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("LOSS OF APPETITE")) {
                for (RadioButton rb : reduceAppetite.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("REDUCED MOBILITY")) {
                for (RadioButton rb : reduceActivity.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("NIGHT SWEATS")) {
                for (RadioButton rb : nightSweats.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("SWELLING")) {
                for (RadioButton rb : swelling.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("NUMBER OF MISSED MEDICATION DOSES IN LAST MONTH")) {
                missedDosage.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("SWELLING")) {
                for (RadioButton rb : swelling.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("ADVERSE EVENTS REPORTED")) {
                for (RadioButton rb : adverseEventReport.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("ADVERSE EVENTS")) {
                for (CheckBox cb : adverseEffects1.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_joint_pain)) && obs[0][1].equals("JOINT PAIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_headache)) && obs[0][1].equals("HEADACHE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_skin_rash)) && obs[0][1].equals("RASH")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_nausea)) && obs[0][1].equals("NAUSEA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_dizziness)) && obs[0][1].equals("DIZZINESS AND GIDDINESS")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                for (CheckBox cb : adverseEffects2.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_vomiting)) && obs[0][1].equals("VOMITING")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_abdominal_pain)) && obs[0][1].equals("ABDOMINAL PAIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_loss_of_appetite)) && obs[0][1].equals("LOSS OF APPETITE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_visual_impairment)) && obs[0][1].equals("VISUAL IMPAIRMENT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_other)) && obs[0][1].equals("OTHER ADVERSE EVENT")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                adverseEffectsLayout.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER ADVERSE EVENT")) {
                otherEffects.getEditText().setText(obs[0][1]);
                otherEffects.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("CLINICIAN INFORMED")) {
                for (RadioButton rb : clinicianInformed.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("CLINICAL VISIT SUGGESTED")) {
                for (RadioButton rb : visitSuggested.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("PATIENT REFERRED")) {
                for (RadioButton rb : clinicalReferral.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
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
