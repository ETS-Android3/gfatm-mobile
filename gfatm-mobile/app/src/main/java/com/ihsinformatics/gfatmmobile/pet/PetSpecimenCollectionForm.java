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

public class PetSpecimenCollectionForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledButton sampleSubmitDate;
    TitledRadioGroup baselineRepeatFollowup;
    TitledSpinner monthTreatment;
    TitledEditText orderId;
    TitledRadioGroup patientCategory;
    TitledRadioGroup reasonBaselineRepeat;
    TitledEditText reasonBaselineRepeatOther;
    TitledRadioGroup specimenType;
    TitledRadioGroup specimenComeFrom;
    TitledEditText otherSpecimentComeFrom;

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
        FORM_NAME = Forms.PET_SPECIMEN_COLLECTION_FORM;
        FORM = Forms.pet_gxp_specimen_form;

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

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        orderId = new TitledEditText(context,null,getResources().getString(R.string.order_id),"","",20,RegexUtil.OTHER_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,true);
        sampleSubmitDate = new TitledButton(context, null, getResources().getString(R.string.ctb_sample_submitted), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.VERTICAL);
        sampleSubmitDate.setTag("sampleSubmitDate");
        baselineRepeatFollowup = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_baseline_repeat_followup), getResources().getStringArray(R.array.ctb_ctb_baseline_repeat_followup_list), null, App.VERTICAL,App.VERTICAL,true);
        monthTreatment = new TitledSpinner(context, null, getResources().getString(R.string.ctb_month_treatment), getResources().getStringArray(R.array.ctb_0_to_24), null, App.HORIZONTAL);
        updateFollowUpMonth();
        patientCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_patient_category), getResources().getStringArray(R.array.ctb_patient_category_list), null, App.HORIZONTAL, App.VERTICAL);
        reasonBaselineRepeat = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_reason_for_repeating), getResources().getStringArray(R.array.ctb_reason_for_repeating_list), null, App.VERTICAL,App.VERTICAL,false);
        specimenType = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_specimen_type), getResources().getStringArray(R.array.ctb_specimen_type_list), null, App.HORIZONTAL, App.VERTICAL,true);
        specimenComeFrom = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_speciment_route), getResources().getStringArray(R.array.ctb_speciment_route_list), null, App.HORIZONTAL,App.VERTICAL,true);
        otherSpecimentComeFrom = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        reasonBaselineRepeatOther = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);


        views = new View[]{formDate.getButton(), sampleSubmitDate.getButton(), baselineRepeatFollowup.getRadioGroup(), patientCategory.getRadioGroup(), reasonBaselineRepeat.getRadioGroup(),
                specimenType.getRadioGroup(),monthTreatment.getSpinner(), specimenComeFrom.getRadioGroup(),
                otherSpecimentComeFrom.getEditText(), reasonBaselineRepeatOther.getEditText(), orderId.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate,orderId, sampleSubmitDate, baselineRepeatFollowup,monthTreatment, patientCategory, reasonBaselineRepeat,reasonBaselineRepeatOther,specimenType, specimenComeFrom, otherSpecimentComeFrom}};

        formDate.getButton().setOnClickListener(this);
        sampleSubmitDate.getButton().setOnClickListener(this);
        baselineRepeatFollowup.getRadioGroup().setOnCheckedChangeListener(this);
        patientCategory.getRadioGroup().setOnCheckedChangeListener(this);
        reasonBaselineRepeat.getRadioGroup().setOnCheckedChangeListener(this);
        specimenType.getRadioGroup().setOnCheckedChangeListener(this);
        specimenComeFrom.getRadioGroup().setOnCheckedChangeListener(this);
        monthTreatment.getSpinner().setOnItemSelectedListener(this);

        resetViews();

    }

    public void updateFollowUpMonth() {

        String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "TREATMENT START DATE");
        String format = "";
        String[] monthArray;

        if (treatmentDate == null) {
            monthArray = new String[1];
            monthArray[0] = "0";
            monthTreatment.getSpinner().setSpinnerData(monthArray);
        } else {
            if (treatmentDate.contains("/")) {
                format = "dd/MM/yyyy";
            } else {
                format = "yyyy-MM-dd";
            }
            Date convertedDate = App.stringToDate(treatmentDate, format);
            Calendar treatmentDateCalender = App.getCalendar(convertedDate);
            int diffYear = formDateCalendar.get(Calendar.YEAR) - treatmentDateCalender.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + formDateCalendar.get(Calendar.MONTH) - treatmentDateCalender.get(Calendar.MONTH);

            if (diffMonth == 0) {
                monthArray = new String[1];
                monthArray[0] = "1";
                monthTreatment.getSpinner().setSpinnerData(monthArray);
            } else {
                monthArray = new String[diffMonth];
                for (int i = 0; i < diffMonth; i++) {
                    monthArray[i] = String.valueOf(i+1);
                }
                monthTreatment.getSpinner().setSpinnerData(monthArray);
            }
        }
    }


    @Override
    public void updateDisplay() {
        Calendar treatDateCalender = null;

        if (snackbar != null)
            snackbar.dismiss();

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
            } else {
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                 String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "REGISTRATION DATE");
                    if(treatmentDate != null){
                        treatDateCalender = App.getCalendar(App.stringToDate(treatmentDate, "yyyy-MM-dd"));
                        if(formDateCalendar.before(treatDateCalender)) {
                            formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                            snackbar = Snackbar.make(mainContent, getResources().getString(R.string.ctb_form_date_less_than_treatment_initiation), Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();

                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        }
                        else {
                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        }
                    }

                }

        } else{
            String formDa = formDate.getButton().getText().toString();

            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }
        formDate.getButton().setEnabled(true);
        sampleSubmitDate.getButton().setEnabled(true);
        updateFollowUpMonth();
    }

    @Override
    public boolean validate() {
        boolean error = false;
        if (patientCategory.getVisibility() == View.VISIBLE && App.get(patientCategory).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            patientCategory.getQuestionView().setError(getString(R.string.empty_field));
            patientCategory.requestFocus();
            error = true;
        }
        if (specimenType.getVisibility() == View.VISIBLE && App.get(specimenType).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            specimenType.getQuestionView().setError(getString(R.string.empty_field));
            specimenType.requestFocus();
            error = true;
        } else {
            specimenType.getRadioGroup().getButtons().get(1).setError(null);
        }
        if (otherSpecimentComeFrom.getVisibility() == View.VISIBLE ) {
            if(!App.get(otherSpecimentComeFrom).isEmpty()) {
                if (App.get(otherSpecimentComeFrom).trim().length() <= 0) {
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    otherSpecimentComeFrom.getEditText().setError(getString(R.string.ctb_spaces_only));
                    otherSpecimentComeFrom.getEditText().requestFocus();
                    error = true;
                }
            }
        }
        if(App.get(baselineRepeatFollowup).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            baselineRepeatFollowup.getQuestionView().setError(getString(R.string.empty_field));
            baselineRepeatFollowup.getQuestionView().requestFocus();
            error = true;
        }
        if(reasonBaselineRepeatOther.getVisibility()==View.VISIBLE){
            if (App.get(reasonBaselineRepeatOther).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                reasonBaselineRepeatOther.getEditText().setError(getString(R.string.empty_field));
                reasonBaselineRepeatOther.getEditText().requestFocus();
                error = true;
            } else if (App.get(reasonBaselineRepeatOther).trim().length() <= 0) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                reasonBaselineRepeatOther.getEditText().setError(getString(R.string.ctb_spaces_only));
                reasonBaselineRepeatOther.getEditText().requestFocus();
                error = true;
            }
        }
        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage("Form date can not be less than Sample submit Date");
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
        observations.add(new String[]{"ORDER ID", App.get(orderId)});
        observations.add(new String[]{"SPECIMEN SUBMISSION DATE", App.getSqlDateTime(secondDateCalendar)});
        observations.add(new String[]{"TEST CONTEXT STATUS", App.get(baselineRepeatFollowup).equals(getResources().getString(R.string.ctb_baseline)) ? "BASELINE" :
                (App.get(baselineRepeatFollowup).equals(getResources().getString(R.string.ctb_baseline_repeat)) ? "BASELINE REPEAT" :
                        "REGULAR FOLLOW UP")});
        if(monthTreatment.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"FOLLOW-UP MONTH", App.get(monthTreatment)});
        }
        if (patientCategory.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"TB CATEGORY", App.get(patientCategory).equals(getResources().getString(R.string.ctb_categoryI)) ? "CATEGORY I TUBERCULOSIS" :
                    "CATEGORY II TUBERCULOSIS"});
        }

        if (reasonBaselineRepeat.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"REASON FOR BASELINE REPEAT TEST", App.get(reasonBaselineRepeat).equals(getResources().getString(R.string.ctb_rr_positive)) ? "RIF RESISTANT POSITIVE" :
                    (App.get(reasonBaselineRepeat).equals(getResources().getString(R.string.ctb_error_invalid_no_result)) ? "INVALID" :
                            (App.get(reasonBaselineRepeat).equals(getResources().getString(R.string.ctb_indeterminate)) ? "INDETERMINATE" :
                                "OTHER REASON FOR REPEATING TEST"))});
        }

        if (reasonBaselineRepeatOther.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"OTHER REASON FOR REPEATING TEST", App.get(reasonBaselineRepeatOther)});
        }


        observations.add(new String[]{"SPECIMEN TYPE", App.get(specimenType).equals(getResources().getString(R.string.ctb_sputum)) ? "SPUTUM" :
                "EXTRA-PULMONARY TUBERCULOSIS"});

        if (specimenComeFrom.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"SPECIMEN SOURCE", App.get(specimenComeFrom).equals(getResources().getString(R.string.ctb_lymph)) ? "LYMPHOCYTES" :
                    (App.get(specimenComeFrom).equals(getResources().getString(R.string.ctb_pleural_fluid)) ? "PLEURAL EFFUSION" :
                            (App.get(specimenComeFrom).equals(getResources().getString(R.string.ctb_pus)) ? "PUS" :
                                    "OTHER SPECIMEN SOURCE"))});
        }

        if (otherSpecimentComeFrom.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"OTHER SPECIMEN SOURCE", App.get(otherSpecimentComeFrom)});
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

                String result = serverService.saveEncounterAndObservation(App.getProgram()+"-"+"GXP Specimen Collection", FORM, formDateCalendar, observations.toArray(new String[][]{}),false);
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

        serverService.saveFormLocally(FORM_NAME, FORM, "12345-5", formValues);

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
            else if (obs[0][0].equals("ORDER ID")) {
                orderId.getEditText().setKeyListener(null);
                orderId.getEditText().setText(obs[0][1]);
            }else if (obs[0][0].equals("SPECIMEN SUBMISSION DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                sampleSubmitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
            } else if (obs[0][0].equals("TEST CONTEXT STATUS")) {
                for (RadioButton rb : baselineRepeatFollowup.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_baseline)) && obs[0][1].equals("BASELINE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_baseline_repeat)) && obs[0][1].equals("BASELINE REPEAT")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_follow_up_test)) && obs[0][1].equals("REGULAR FOLLOW UP")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TB CATEGORY")) {
                for (RadioButton rb : patientCategory.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_categoryI)) && obs[0][1].equals("CATEGORY I TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_categoryII)) && obs[0][1].equals("CATEGORY II TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("REASON FOR BASELINE REPEAT TEST")) {
                for (RadioButton rb : reasonBaselineRepeat.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_rr_positive)) && obs[0][1].equals("RIF RESISTANT POSITIVE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_error_invalid_no_result)) && obs[0][1].equals("INVALID")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_other_title)) && obs[0][1].equals("OTHER REASON FOR REPEATING TEST")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }
            else if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                monthTreatment.getSpinner().selectValue(obs[0][1]);
            }
            else if (obs[0][0].equals("SPECIMEN TYPE")) {
                for (RadioButton rb : specimenType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_sputum)) && obs[0][1].equals("SPUTUM")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_extra_pulmonary)) && obs[0][1].equals("EXTRA-PULMONARY TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("SPECIMEN SOURCE")) {
                for (RadioButton rb : specimenComeFrom.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_lymph)) && obs[0][1].equals("LYMPHOCYTES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_pleural_fluid)) && obs[0][1].equals("PLEURAL EFFUSION")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_pus)) && obs[0][1].equals("PUS")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_other_title)) && obs[0][1].equals("OTHER SPECIMEN SOURCE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER SPECIMEN SOURCE")) {
                otherSpecimentComeFrom.getEditText().setText(obs[0][1]);
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
        if (view == sampleSubmitDate.getButton()) {
            sampleSubmitDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
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

    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();
        formDate.setEnabled(true);
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        sampleSubmitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        reasonBaselineRepeat.setVisibility(View.GONE);
        specimenComeFrom.setVisibility(View.GONE);
        otherSpecimentComeFrom.setVisibility(View.GONE);
        reasonBaselineRepeatOther.setVisibility(View.GONE);
        monthTreatment.setVisibility(View.GONE);
        orderId.getEditText().setKeyListener(null);
        Date nowDate = new Date();
        orderId.getEditText().setText(App.getSqlDateTime(nowDate));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

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
        if (group == specimenType.getRadioGroup()) {
            specimenType.getQuestionView().setError(null);
            if (specimenType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_extra_pulmonary))) {
                specimenComeFrom.setVisibility(View.VISIBLE);

            } else {
                specimenComeFrom.setVisibility(View.GONE);
                otherSpecimentComeFrom.setVisibility(View.GONE);
            }
        }
        if (group == baselineRepeatFollowup.getRadioGroup()) {
            baselineRepeatFollowup.getQuestionView().setError(null);
            if (baselineRepeatFollowup.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_baseline))) {
                patientCategory.setVisibility(View.VISIBLE);
            }else{
                patientCategory.setVisibility(View.GONE);
                reasonBaselineRepeatOther.setVisibility(View.GONE);
            }
            if(baselineRepeatFollowup.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_baseline_repeat))){
                reasonBaselineRepeat.setVisibility(View.VISIBLE);
                if(App.get(reasonBaselineRepeat).equals(getResources().getString(R.string.ctb_other_title))){
                    reasonBaselineRepeatOther.setVisibility(View.VISIBLE);
                }
            }else{
                reasonBaselineRepeat.setVisibility(View.GONE);
                reasonBaselineRepeatOther.setVisibility(View.GONE);
            }
            if (baselineRepeatFollowup.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_follow_up_test))) {
                monthTreatment.setVisibility(View.VISIBLE);
            } else {
                monthTreatment.setVisibility(View.GONE);
            }
        }
        if(group == specimenComeFrom.getRadioGroup()){
            if (specimenComeFrom.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_other_title))) {
                otherSpecimentComeFrom.setVisibility(View.VISIBLE);
            }else{
                otherSpecimentComeFrom.setVisibility(View.GONE);
            }
        }
        if(group == reasonBaselineRepeat.getRadioGroup()){
            if (reasonBaselineRepeat.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_other_title))) {
                reasonBaselineRepeatOther.setVisibility(View.VISIBLE);
            }else{
                reasonBaselineRepeatOther.setVisibility(View.GONE);
            }
        }
        if(group == patientCategory.getRadioGroup()){
           patientCategory.getQuestionView().setError(null);
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
