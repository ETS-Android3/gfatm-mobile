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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
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
 * Created by Fawad Jawaid on 14-Feb-17.
 */

public class ComorbiditiesEndOfTreatmentMentalHealthForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledEditText numberOfSessionsConducted;
    TitledEditText akuadsRescreeningScore;
    TitledSpinner reasonForDiscontinuation;
    TitledRadioGroup feelingBetterReason;
    TitledRadioGroup lossToFollowup;
    //TitledRadioGroup referredTo;
    TitledEditText referredTo;
    //TitledEditText ifOther;
    //TitledRadioGroup reasonForReferral;
    TitledEditText reasonForReferral;
    //TitledEditText otherSevereMentalIllness;
    TitledEditText remarks;

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
        FORM_NAME = Forms.COMORBIDITIES_END_OF_TREATMENT_MENTAL_HEALTH;
        FORM = Forms.comorbidities_endOfTreatmentFormMH;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesEndOfTreatmentMentalHealthForm.MyAdapter());
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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        numberOfSessionsConducted = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_end_treatment_MH_number_of_sessions), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        akuadsRescreeningScore = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_end_treatment_MH_akuads_score), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        reasonForDiscontinuation = new TitledSpinner(mainContent.getContext(), null, getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation), getResources().getStringArray(R.array.comorbidities_end_treatment_MH_reason_of_discontinuation_options), getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation_options_feeling_better), App.VERTICAL, true);
        feelingBetterReason = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_end_treatment_MH_feeling_better), getResources().getStringArray(R.array.comorbidities_end_treatment_MH_feeling_better_options), getResources().getString(R.string.comorbidities_end_treatment_MH_feeling_better_options_therapy_completed), App.VERTICAL, App.VERTICAL);
        lossToFollowup = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_end_treatment_MH_loss_to_followup), getResources().getStringArray(R.array.comorbidities_end_treatment_MH_loss_to_followup_options), getResources().getString(R.string.comorbidities_end_treatment_MH_loss_to_followup_options_unreachable), App.VERTICAL, App.VERTICAL);
        //referredTo = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_end_treatment_MH_referred_to), getResources().getStringArray(R.array.comorbidities_end_treatment_MH_referred_to_options), "", App.VERTICAL, App.VERTICAL);
        referredTo = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_end_treatment_MH_referred_to), getResources().getString(R.string.comorbidities_end_treatment_MH_referred_to_options_referred), "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        //reasonForReferral = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_end_treatment_MH_referral_reason), getResources().getStringArray(R.array.comorbidities_end_treatment_MH_referral_reason_options), "", App.VERTICAL, App.VERTICAL);
        reasonForReferral = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_end_treatment_MH_referral_reason), getResources().getString(R.string.comorbidities_end_treatment_MH_referral_reason_options_depression), "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        //ifOther = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_end_treatment_MH_if_other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        //otherSevereMentalIllness =  new TitledEditText(context, null, getResources().getString(R.string.comorbidities_end_treatment_MH_describe_illness), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        remarks = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_end_treatment_MH_comments_remarks), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        displayFeelingBetterReason();

        // Used for reset fields...
        views = new View[]{formDate.getButton(), numberOfSessionsConducted.getEditText(), akuadsRescreeningScore.getEditText(), reasonForDiscontinuation.getSpinner(), feelingBetterReason.getRadioGroup(),
                lossToFollowup.getRadioGroup(), referredTo.getEditText(), reasonForReferral.getEditText(), /*ifOther.getEditText(), otherSevereMentalIllness.getEditText(),*/ remarks.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, numberOfSessionsConducted, akuadsRescreeningScore, reasonForDiscontinuation, feelingBetterReason,
                        lossToFollowup, referredTo, reasonForReferral, /*ifOther, otherSevereMentalIllness,*/ remarks}};

        formDate.getButton().setOnClickListener(this);
        feelingBetterReason.getRadioGroup().setOnCheckedChangeListener(this);
        lossToFollowup.getRadioGroup().setOnCheckedChangeListener(this);
        //referredTo.getRadioGroup().setOnCheckedChangeListener(this);
        //reasonForReferral.getRadioGroup().setOnCheckedChangeListener(this);

        reasonForDiscontinuation.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                displayFeelingBetterReason();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        resetViews();
    }

    @Override
    public void updateDisplay() {

        //formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
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
    }

    @Override
    public boolean validate() {

        Boolean error = false;

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
        observations.add(new String[]{"TOTAL NUMBER OF SESSIONS", App.get(numberOfSessionsConducted)});
        observations.add(new String[]{"AKUADS SCORE", App.get(akuadsRescreeningScore)});

        final String reasonForDiscontinuationString = App.get(reasonForDiscontinuation).equals(getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation_options_feeling_better)) ? "FEELING BETTER" :
                (App.get(reasonForDiscontinuation).equals(getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation_options_doesnot_time)) ? "TIME CONSTRAINT" :
                        (App.get(reasonForDiscontinuation).equals(getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation_options_family_refused)) ? "FAMILY REFUSED TREATMENT" :
                                (App.get(reasonForDiscontinuation).equals(getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation_options_language_barrier)) ? "SPEECH AND LANGUAGE DEFICITS" :
                                        (App.get(reasonForDiscontinuation).equals(getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation_options_think_no_depression)) ? "PATIENT THINKS HE HAS NO DEPRESSION" :
                                                (App.get(reasonForDiscontinuation).equals(getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation_options_lost_followup)) ? "LOST TO FOLLOW-UP" :
                                                        (App.get(reasonForDiscontinuation).equals(getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation_options_referred)) ? "PATIENT REFERRED" : "REASON FOR DISCONTINUING SERVICE (TEXT)"))))));
        observations.add(new String[]{"REASON FOR DISCONTINUATION OF PROGRAM", reasonForDiscontinuationString});

        if (feelingBetterReason.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"REASON FOR FEELING BETTER", App.get(feelingBetterReason).equals(getResources().getString(R.string.comorbidities_end_treatment_MH_feeling_better_options_self_reported)) ? "1 to 3 sessions conducted and no Akuads filled (SELF REPORTED IMPROVEMENT)" : "4 or more sessions conducted and AKUADS filled (SUCCESFULLY COMPLETED THERAPY WITH IMPROVED OUTCOMES)"});
        }

        observations.add(new String[]{"REASON FOR LOST TO FOLLOW UP", App.get(lossToFollowup).equals(getResources().getString(R.string.comorbidities_end_treatment_MH_loss_to_followup_options_unreachable)) ? "PATIENT UNREACHABLE" :
                (App.get(lossToFollowup).equals(getResources().getString(R.string.comorbidities_end_treatment_MH_loss_to_followup_options_moved)) ? "PATIENT MOVED" : "OTHER REASON TO END FOLLOW UP")});


        observations.add(new String[]{"REFERRING FACILITY NAME", App.get(referredTo)});
        observations.add(new String[]{"OTHER TRANSFER OR REFERRAL REASON", App.get(reasonForReferral)});
        observations.add(new String[]{"FREE TEXT COMMENT", App.get(remarks)});

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
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            }

            if (obs[0][0].equals("TOTAL NUMBER OF SESSIONS")) {
                numberOfSessionsConducted.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("AKUADS SCORE")) {
                akuadsRescreeningScore.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("REASON FOR DISCONTINUATION OF PROGRAM")) {
                String value = obs[0][1].equals("FEELING BETTER") ? getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation_options_feeling_better) :
                        (obs[0][1].equals("TIME CONSTRAINT") ? getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation_options_doesnot_time) :
                                (obs[0][1].equals("FAMILY REFUSED TREATMENT") ? getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation_options_family_refused) :
                                        (obs[0][1].equals("SPEECH AND LANGUAGE DEFICITS") ? getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation_options_language_barrier) :
                                                (obs[0][1].equals("PATIENT THINKS HE HAS NO DEPRESSION") ? getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation_options_think_no_depression) :
                                                        (obs[0][1].equals("LOST TO FOLLOW-UP") ? getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation_options_lost_followup) :
                                                                (obs[0][1].equals("PATIENT REFERRED") ? getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation_options_referred) : getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation_options_other)))))));
                reasonForDiscontinuation.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("REASON FOR FEELING BETTER")) {
                for (RadioButton rb : feelingBetterReason.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_end_treatment_MH_feeling_better_options_self_reported)) && obs[0][1].equals("1 to 3 sessions conducted and no Akuads filled (SELF REPORTED IMPROVEMENT)")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_end_treatment_MH_feeling_better_options_therapy_completed)) && obs[0][1].equals("4 or more sessions conducted and AKUADS filled (SUCCESFULLY COMPLETED THERAPY WITH IMPROVED OUTCOMES)")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                feelingBetterReason.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REASON FOR LOST TO FOLLOW UP")) {
                for (RadioButton rb : feelingBetterReason.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_end_treatment_MH_loss_to_followup_options_unreachable)) && obs[0][1].equals("PATIENT UNREACHABLE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_end_treatment_MH_loss_to_followup_options_moved)) && obs[0][1].equals("PATIENT MOVED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_end_treatment_MH_loss_to_followup_options_other)) && obs[0][1].equals("OTHER REASON TO END FOLLOW UP")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                lossToFollowup.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REFERRING FACILITY NAME")) {
                referredTo.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("OTHER TRANSFER OR REFERRAL REASON")) {
                reasonForReferral.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("FREE TEXT COMMENT")) {
                remarks.getEditText().setText(obs[0][1]);
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

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        displayFeelingBetterReason();

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
                String akuadsScore = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.COMORBIDITIES_ASSESSMENT_FORM_MENTAL_HEALTH, "AKUADS SCORE");
                String sessionNumber = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.COMORBIDITIES_TREATMENT_FOLLOWUP_MENTAL_HEALTH_FORM, "SESSION NUMBER");

                if (akuadsScore != null)
                    if (!akuadsScore.equals(""))
                        result.put("AKUADS SCORE", akuadsScore);
                if (sessionNumber != null && !sessionNumber.equals("")) {
                    sessionNumber = sessionNumber.replace(".0", "");
                }
                if (sessionNumber != null)
                    if (!sessionNumber.equals(""))
                        result.put("SESSION NUMBER", sessionNumber);

                return result;
            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            @Override
            protected void onPostExecute(HashMap<String, String> result) {
                super.onPostExecute(result);
                loading.dismiss();

                akuadsRescreeningScore.getEditText().setText(result.get("AKUADS SCORE"));
                numberOfSessionsConducted.getEditText().setText(result.get("SESSION NUMBER"));
            }
        };
        autopopulateFormTask.execute("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }

    void displayFeelingBetterReason() {
        if (reasonForDiscontinuation.getSpinner().getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.comorbidities_end_treatment_MH_reason_of_discontinuation_options_feeling_better))) {
            feelingBetterReason.setVisibility(View.VISIBLE);
        } else {
            feelingBetterReason.setVisibility(View.GONE);
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







