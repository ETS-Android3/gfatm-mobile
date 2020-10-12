package com.ihsinformatics.gfatmmobile.common;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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
 * Created by Haris on 1/19/2017.
 */

public class GpxSpecimenCollectionForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;
    Boolean emptyError = false;

    // Views...

    TitledButton sampleSubmissionDate;
    TitledRadioGroup testContextStatus;
    TitledSpinner monthOfTreatment;
    TitledRadioGroup tbCategory;
    TitledRadioGroup baselineRepeatReason;
    TitledEditText baselineRepeatReasonOther;
    TitledRadioGroup sampleType;
    TitledRadioGroup pulmonaryType;
    TitledRadioGroup specimenSource;
    TitledEditText specimenSourceOther;
    TitledEditText orderId;
    TitledRadioGroup sampleCollectedFrom;
    TitledSpinner outsideSpecify1;

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
        formName = Forms.GXP_SPECIMEN_COLLECTION_FORM;
        form = Forms.gxpSpecimenCollectionForm;

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
        String columnName = "";
        final Object[][] locations = serverService.getAllLocationsFromLocalDB(columnName);
        String[] locationArray = new String[locations.length + 2];
        locationArray[0] = "";
        int j = 1;
        for (int i = 0; i < locations.length; i++) {
            if (locations[i][16] != null) {
                locationArray[j] = String.valueOf(locations[i][16]);
                ++j;
            }
        }
        locationArray[j] = "Other";
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        sampleCollectedFrom = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_where_was_the_sample_collected_from), getResources().getStringArray(R.array.fast_within_outside_site_list), "", App.VERTICAL, App.VERTICAL, true,"SAMPLE COLLECTED FROM", new String[]{"WITHIN THE SITE", "OUTSIDE THE SITE"});
        sampleSubmissionDate = new TitledButton(context, null, getResources().getString(R.string.fast_day_was_the_sample_submitted), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.VERTICAL);
        testContextStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_point_test_being_done), getResources().getStringArray(R.array.fast_test_being_done_list), getResources().getString(R.string.fast_baseline_new), App.VERTICAL, App.VERTICAL,false,"TEST CONTEXT STATUS",new String[]{ "BASELINE" , "BASELINE REPEAT" , "REGULAR FOLLOW UP"});
        monthOfTreatment = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_at_what_point_test_being_done), getResources().getStringArray(R.array.fast_number_list), "", App.HORIZONTAL,false,"FOLLOW-UP MONTH", getResources().getStringArray(R.array.fast_number_list));
        outsideSpecify1 = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_specify_outside), locationArray, "", App.VERTICAL, true);
        updateFollowUpMonth();
        tbCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_baseline_test_specify_patient_category), getResources().getStringArray(R.array.fast_patient_category_list), getResources().getString(R.string.fast_category_1), App.VERTICAL, App.VERTICAL,false,"TB CATEGORY",new String[]{ "CATEGORY I TUBERCULOSIS" , "CATEGORY II TUBERCULOSIS" , "RELAPSE"});
        baselineRepeatReason = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_repeating_test_if_baseline_repeat), getResources().getStringArray(R.array.fast_repeating_test_if_baseline_repeat_list), getResources().getString(R.string.fast_rif_resistant), App.VERTICAL, App.VERTICAL,false,"REASON FOR BASELINE REPEAT TEST",new String[]{ "RIF RESISTANT POSITIVE" , "INVALID" , "INDETERMINATE" ,"OTHER REASON FOR REPEATING TEST"});
        baselineRepeatReasonOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"OTHER REASON FOR REPEATING TEST");
        baselineRepeatReasonOther.setVisibility(View.GONE);
        sampleType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_specimen_type), getResources().getStringArray(R.array.fast_specimen_type_list), getResources().getString(R.string.fast_pulmonary), App.VERTICAL, App.VERTICAL,false,"SPECIMEN TYPE", new String[]{"PULMONARY" , "EXTRA-PULMONARY"});
        pulmonaryType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_pulmonary), getResources().getStringArray(R.array.fast_pulmonary_list), getResources().getString(R.string.fast_pulmonary), App.HORIZONTAL, App.VERTICAL,false,"PULMONARY SPECIMEN TYPE", new String[]{"SPUTUM", "GASTRIC ASPIRATE"});
        specimenSource = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_specimen_come_from), getResources().getStringArray(R.array.fast_specimen_come_from_list_updated), getResources().getString(R.string.fast_gastric_aspirate), App.VERTICAL, App.VERTICAL,false,"SPECIMEN SOURCE",new String[]{ "GASTRIC ASPIRATE" , "PLEURAL EFFUSION" , "PUS" , "OTHER SPECIMEN SOURCE"});
        specimenSourceOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"OTHER SPECIMEN SOURCE");
        // cartridgeId = new TitledEditText(context, null, getResources().getString(R.string.fast_test_id), "", "", 20,RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        orderId = new TitledEditText(context, null, getResources().getString(R.string.order_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true,"ORDER ID");
        // orderId.setLongClickable(false);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), sampleSubmissionDate.getButton(), testContextStatus.getRadioGroup(), monthOfTreatment.getSpinner(), tbCategory.getRadioGroup(),
                baselineRepeatReason.getRadioGroup(), baselineRepeatReasonOther.getEditText(), sampleType.getRadioGroup(), specimenSource.getRadioGroup(), specimenSourceOther.getEditText(), orderId.getEditText(), sampleCollectedFrom.getRadioGroup(), pulmonaryType.getRadioGroup(),
                outsideSpecify1.getSpinner()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, sampleCollectedFrom, outsideSpecify1, sampleSubmissionDate, testContextStatus, monthOfTreatment, tbCategory, baselineRepeatReason, baselineRepeatReasonOther, sampleType, pulmonaryType, specimenSource, specimenSourceOther, orderId}};

        formDate.getButton().setOnClickListener(this);
        sampleSubmissionDate.getButton().setOnClickListener(this);
        testContextStatus.getRadioGroup().setOnCheckedChangeListener(this);
        tbCategory.getRadioGroup().setOnCheckedChangeListener(this);
        baselineRepeatReason.getRadioGroup().setOnCheckedChangeListener(this);
        sampleType.getRadioGroup().setOnCheckedChangeListener(this);
        specimenSource.getRadioGroup().setOnCheckedChangeListener(this);
        sampleCollectedFrom.getRadioGroup().setOnCheckedChangeListener(this);
        pulmonaryType.getRadioGroup().setOnCheckedChangeListener(this);

        resetViews();

    }

    public void updateFollowUpMonth() {

        String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-" + "Treatment Initiation", "REGISTRATION DATE");
        String format = "";
        String[] monthArray;

        if (treatmentDate == null) {
            monthArray = new String[1];
            monthArray[0] = "0";
            monthOfTreatment.getSpinner().setSpinnerData(monthArray);
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
                monthOfTreatment.getSpinner().setSpinnerData(monthArray);
            } else if (diffMonth > 24) {
                monthArray = new String[24];
                for (int i = 0; i < 24; i++) {
                    monthArray[i] = String.valueOf(i + 1);
                }
                monthOfTreatment.getSpinner().setSpinnerData(monthArray);
            } else {
                monthArray = new String[diffMonth];
                for (int i = 0; i < diffMonth; i++) {
                    monthArray[i] = String.valueOf(i + 1);
                }
                monthOfTreatment.getSpinner().setSpinnerData(monthArray);
            }
        }
    }

    public void updateDisplay() {
        Calendar treatDateCalender = null;
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-" + "Treatment Initiation", "REGISTRATION DATE");

            if (treatmentDate != null) {
                treatDateCalender = App.getCalendar(App.stringToDate(treatmentDate, "yyyy-MM-dd"));
            }

            personDOB = personDOB.substring(0, 10);

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else if (treatDateCalender != null) {
                if (formDateCalendar.before(treatDateCalender)) {
                    formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                    snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_cannot_be_before_treatment_initiation_form), Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();

                    formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                } else {
                    formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                }
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }

        if (!(sampleSubmissionDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = sampleSubmissionDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                sampleSubmissionDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                sampleSubmissionDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else if (secondDateCalendar.before(formDateCalendar)) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_sample_date_cannot_be_before_form_date), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                sampleSubmissionDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                sampleSubmissionDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }

        updateFollowUpMonth();
        formDate.getButton().setEnabled(true);
        sampleSubmissionDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = super.validate();


        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(),R.style.dialog).create();
            if (!emptyError)
                alertDialog.setMessage(getString(R.string.form_error));
            else
                alertDialog.setMessage(getString(R.string.fast_required_field_error));
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




        if (outsideSpecify1.getVisibility() == View.VISIBLE) {
            if (App.get(outsideSpecify1).equals(getString(R.string.fast_other_title)))
                observations.add(new String[]{"SAMPLE COLLECTED OUTSIDE THE SITE OTHER", App.get(outsideSpecify1)});
            else {
                String location = serverService.getLocationNameFromDescription(App.get(outsideSpecify1));
                observations.add(new String[]{"SAMPLE COLLECTED OUTSIDE THE SITE OTHER", location});
            }

        }

        observations.add(new String[]{"SPECIMEN SUBMISSION DATE", App.getSqlDateTime(secondDateCalendar)});



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
                if(App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocally(formName, form, formDateCalendar,observations.toArray(new String[][]{}));

                String result = serverService.saveEncounterAndObservationTesting(formName, form, formDateCalendar, observations.toArray(new String[][]{}),id);
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
             if (obs[0][0].equals("SAMPLE COLLECTED OUTSIDE THE SITE OTHER")) {
                outsideSpecify1.getSpinner().selectValue(obs[0][1]);
                outsideSpecify1.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("SPECIMEN SUBMISSION DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                sampleSubmissionDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                sampleSubmissionDate.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {

            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar,false,true, false);

            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);*/
        }

        if (view == sampleSubmissionDate.getButton()) {
            sampleSubmissionDate.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar,false,true, true);

            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);*/
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        sampleSubmissionDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        baselineRepeatReason.setVisibility(View.GONE);
        monthOfTreatment.setVisibility(View.GONE);
        specimenSource.setVisibility(View.GONE);
        specimenSourceOther.setVisibility(View.GONE);
        if (App.get(sampleType).equals(R.string.fast_pulmonary)) {
            pulmonaryType.setVisibility(View.VISIBLE);
        }

        if (App.get(sampleCollectedFrom).equals(getResources().getString(R.string.fast_outside_site))) {
            outsideSpecify1.setVisibility(View.VISIBLE);
        }
        Date nowDate = new Date();
        orderId.getEditText().setText(App.getSqlDateTime(nowDate));
        orderId.getEditText().setKeyListener(null);
        orderId.getEditText().setFocusable(false);


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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if (radioGroup == testContextStatus.getRadioGroup()) {
            if (testContextStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_baseline_new))) {
                tbCategory.setVisibility(View.VISIBLE);
            } else {
                tbCategory.setVisibility(View.GONE);
            }

            if (testContextStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_baseline_repeat))) {
                baselineRepeatReason.setVisibility(View.VISIBLE);
                if (baselineRepeatReason.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_others))) {
                    baselineRepeatReasonOther.setVisibility(View.VISIBLE);
                } else {
                    baselineRepeatReasonOther.setVisibility(View.GONE);
                }

            } else {
                baselineRepeatReason.setVisibility(View.GONE);
                baselineRepeatReasonOther.setVisibility(View.GONE);
            }

            if (testContextStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_followup_test))) {
                monthOfTreatment.setVisibility(View.VISIBLE);
            } else {
                monthOfTreatment.setVisibility(View.GONE);
            }
        } else if (radioGroup == sampleType.getRadioGroup()) {
            if (sampleType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_extra_pulmonary))) {
                specimenSource.setVisibility(View.VISIBLE);
                if (specimenSource.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_other_title))) {
                    specimenSourceOther.setVisibility(View.VISIBLE);
                }
                pulmonaryType.setVisibility(View.GONE);
            } else if (sampleType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_pulmonary))) {
                pulmonaryType.setVisibility(View.VISIBLE);

                specimenSource.setVisibility(View.GONE);
                specimenSourceOther.setVisibility(View.GONE);
            } else {
                pulmonaryType.setVisibility(View.GONE);
                specimenSource.setVisibility(View.GONE);
                specimenSourceOther.setVisibility(View.GONE);
            }
        } else if (radioGroup == specimenSource.getRadioGroup()) {
            if (specimenSource.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_other_title))) {
                specimenSourceOther.setVisibility(View.VISIBLE);
            } else {
                specimenSourceOther.setVisibility(View.GONE);
            }
        } else if (radioGroup == baselineRepeatReason.getRadioGroup()) {
            if (baselineRepeatReason.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_others))) {
                baselineRepeatReasonOther.setVisibility(View.VISIBLE);
            } else {
                baselineRepeatReasonOther.setVisibility(View.GONE);
            }
        } else if (radioGroup == sampleCollectedFrom.getRadioGroup()) {
            sampleCollectedFrom.getQuestionView().setError(null);
            if (sampleCollectedFrom.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_outside_site))) {
                outsideSpecify1.setVisibility(View.VISIBLE);
            } else {
                outsideSpecify1.setVisibility(View.GONE);
            }
        } else if (radioGroup == pulmonaryType.getRadioGroup()) {
            pulmonaryType.getQuestionView().setError(null);
        }

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
