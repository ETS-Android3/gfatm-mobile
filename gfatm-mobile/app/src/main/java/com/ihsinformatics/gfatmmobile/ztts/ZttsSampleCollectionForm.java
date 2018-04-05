package com.ihsinformatics.gfatmmobile.ztts;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
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
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
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
 * Created by Haris on 1/5/2017.
 */

public class ZttsSampleCollectionForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    Boolean emptyError = false;


    // Views...
    TitledButton formDate;
    TitledRadioGroup assessment_type;
    MyTextView becteriologicalTestTextView;
    TitledRadioGroup number_samples;
    TitledRadioGroup reason_nosputum_sample;
    TitledRadioGroup sputum_specimen_type_single;
    TitledCheckBoxes sputum_specimen_type_multi;
    TitledCheckBoxes early_morning_sample_test_multi;
    TitledRadioGroup onspot_sample_test_single;
    TitledCheckBoxes onspot_sample_test_multi;

    TitledEditText orderID_gx_onspot;
    TitledEditText orderID_gx_early_morning;

    TitledEditText orderID_afb_onspot;
    TitledEditText orderID_afb_early_morning;


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 1;
        FORM_NAME = Forms.ZTTS_SAMPLE_COLLECTION;
        FORM = Forms.ztts_sampleCollectionForm;

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

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        assessment_type = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_assessment_type), getResources().getStringArray(R.array.ztts_assessment_type_options), getString(R.string.ztts_assessment_type_screening), App.VERTICAL, App.VERTICAL, true);
        number_samples = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_number_samples), getResources().getStringArray(R.array.ztts_number_samples_options), "", App.VERTICAL, App.VERTICAL, true);
        reason_nosputum_sample = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_reason_nosputum_sample), getResources().getStringArray(R.array.ztts_reason_nosputum_sample_options), getResources().getString(R.string.ztts_reason_nosputum_sample_pexpectorate), App.VERTICAL, App.VERTICAL, true);

        sputum_specimen_type_single = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_sputum_specimen_type), getResources().getStringArray(R.array.ztts_sputum_specimen_type_options), getResources().getString(R.string.no), App.VERTICAL, App.VERTICAL, true);
        sputum_specimen_type_multi = new TitledCheckBoxes(context, null, getResources().getString(R.string.ztts_sputum_specimen_type), getResources().getStringArray(R.array.ztts_sputum_specimen_type_options), new Boolean[]{true, true}, App.VERTICAL, App.VERTICAL, true);

        early_morning_sample_test_multi = new TitledCheckBoxes(context, null, getResources().getString(R.string.ztts_early_morning_sample_test), getResources().getStringArray(R.array.ztts_early_morning_sample_test_options), null, App.VERTICAL, App.VERTICAL, true);
        onspot_sample_test_single = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_on_spot_sample_test), getResources().getStringArray(R.array.ztts_early_morning_sample_test_options), getResources().getString(R.string.no), App.VERTICAL, App.VERTICAL, true);
        onspot_sample_test_multi = new TitledCheckBoxes(context, null, getResources().getString(R.string.ztts_on_spot_sample_test), getResources().getStringArray(R.array.ztts_early_morning_sample_test_options), null, App.VERTICAL, App.VERTICAL, true);

        orderID_gx_onspot = new TitledEditText(context, null, getResources().getString(R.string.ztts_orderid_gx), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        orderID_afb_onspot = new TitledEditText(context, null, getResources().getString(R.string.ztts_orderid_afb), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        orderID_gx_early_morning = new TitledEditText(context, null, getResources().getString(R.string.ztts_orderid_gx), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        orderID_afb_early_morning = new TitledEditText(context, null, getResources().getString(R.string.ztts_orderid_afb), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);


        becteriologicalTestTextView = new MyTextView(context, "Bacteriological Tests");
        becteriologicalTestTextView.setTypeface(null, Typeface.BOLD);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), assessment_type.getRadioGroup(), number_samples.getRadioGroup(), reason_nosputum_sample.getRadioGroup(),
                sputum_specimen_type_single.getRadioGroup(), sputum_specimen_type_multi, onspot_sample_test_single.getRadioGroup(), early_morning_sample_test_multi, onspot_sample_test_multi, orderID_afb_early_morning.getEditText(), orderID_gx_early_morning.getEditText(),
                orderID_gx_onspot.getEditText(), orderID_afb_onspot.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, assessment_type, becteriologicalTestTextView, number_samples, reason_nosputum_sample, sputum_specimen_type_single,
                        sputum_specimen_type_multi, early_morning_sample_test_multi, orderID_gx_early_morning, orderID_afb_early_morning, onspot_sample_test_multi, onspot_sample_test_single, orderID_gx_onspot, orderID_afb_onspot}};


        formDate.getButton().setOnClickListener(this);
        assessment_type.getRadioGroup().setOnCheckedChangeListener(this);
        number_samples.getRadioGroup().setOnCheckedChangeListener(this);
        onspot_sample_test_single.getRadioGroup().setOnCheckedChangeListener(this);
        sputum_specimen_type_single.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : sputum_specimen_type_multi.getCheckedBoxes()) {
            cb.setOnCheckedChangeListener(this);
        }
        for (CheckBox cb : early_morning_sample_test_multi.getCheckedBoxes()) {
            cb.setOnCheckedChangeListener(this);
        }
        for (CheckBox cb : onspot_sample_test_multi.getCheckedBoxes()) {
            cb.setOnCheckedChangeListener(this);
        }
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
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        }

        formDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {

        Boolean error = false;


        if (number_samples.getVisibility() == View.VISIBLE && App.get(number_samples).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            number_samples.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            number_samples.getQuestionView().setError(null);
        }

        if (sputum_specimen_type_single.getVisibility() == View.VISIBLE && App.get(sputum_specimen_type_single).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            sputum_specimen_type_single.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            sputum_specimen_type_single.getQuestionView().setError(null);
        }
        if (onspot_sample_test_single.getVisibility() == View.VISIBLE && App.get(onspot_sample_test_single).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            onspot_sample_test_single.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            onspot_sample_test_single.getQuestionView().setError(null);
        }

        if (early_morning_sample_test_multi.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : early_morning_sample_test_multi.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                early_morning_sample_test_multi.getQuestionView().setError(getString(R.string.empty_field));
                early_morning_sample_test_multi.getQuestionView().requestFocus();
                error = true;
            } else {
                early_morning_sample_test_multi.getQuestionView().setError(null);
            }
        }
        if (onspot_sample_test_multi.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : onspot_sample_test_multi.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                onspot_sample_test_multi.getQuestionView().setError(getString(R.string.empty_field));
                onspot_sample_test_multi.getQuestionView().requestFocus();
                error = true;
            } else {
                onspot_sample_test_multi.getQuestionView().setError(null);
            }
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            if (!emptyError)
                alertDialog.setMessage(getString(R.string.form_error));
            else
                alertDialog.setMessage(getString(R.string.fast_required_field_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            //   DrawableCompat.setTint(clearIcon, color);
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


        if (assessment_type.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TYPE OF ASSESSMENT", App.get(assessment_type).equals(getResources().getString(R.string.ztts_assessment_type_screening)) ? "IDENTIFIED PATIENT THROUGH SCREENING" : "FOLLOW UP"});

        if (number_samples.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"NUMBER OF SAMPLE SUBMITTED", App.get(number_samples)});

        if (reason_nosputum_sample.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT DO NOT PRODUCE SPUTUM SAMPLE", App.get(reason_nosputum_sample).equals(getString(R.string.ztts_reason_nosputum_sample_pexpectorate)) ? "UNABLE TO EXPECTORATE" :
                    App.get(reason_nosputum_sample).equals(getString(R.string.ztts_reason_nosputum_sample_refuse)) ? "REFUSED" : "PATIENT NOT AT HOME"});

        if (sputum_specimen_type_single.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"SPUTUM SPECIMEN TYPE", App.get(sputum_specimen_type_single).equals(getString(R.string.ztts_sputum_specimen_type_early_morning)) ? "EARLY MORNING SAMPLE" : "ON SPOT SAMPLE"});
        }
        if (sputum_specimen_type_multi.getVisibility() == View.VISIBLE) {
            String sputum_specimen_type_string = "";
            for (CheckBox cb : sputum_specimen_type_multi.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ztts_sputum_specimen_type_early_morning)))
                    sputum_specimen_type_string = sputum_specimen_type_string + "EARLY MORNING SAMPLE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ztts_sputum_specimen_type_on_spot)))
                    sputum_specimen_type_string = sputum_specimen_type_string + "ON SPOT SAMPLE" + " ; ";
            }
            observations.add(new String[]{"SPUTUM SPECIMEN TYPE", sputum_specimen_type_string});
        }

        if (onspot_sample_test_single.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"TEST REQUESTED FOR ON SPOT SAMPLE", App.get(onspot_sample_test_single).equals(getString(R.string.ztts_early_morning_sample_test_genexpert_ultra)) ? "GENXPERT ULTRA"+" ; " : "CULTURE FOR MYCOBACTERIA"+" ; "});
        }
        if (onspot_sample_test_multi.getVisibility() == View.VISIBLE) {
            String onspot_sample_test_multi_string = "";
            for (CheckBox cb : onspot_sample_test_multi.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ztts_early_morning_sample_test_genexpert_ultra)))
                    onspot_sample_test_multi_string = onspot_sample_test_multi_string + "GENXPERT ULTRA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ztts_early_morning_sample_test_afb_culture)))
                    onspot_sample_test_multi_string = onspot_sample_test_multi_string + "CULTURE FOR MYCOBACTERIA" + " ; ";
            }
            observations.add(new String[]{"TEST REQUESTED FOR ON SPOT SAMPLE", onspot_sample_test_multi_string});
        }
        if (early_morning_sample_test_multi.getVisibility() == View.VISIBLE) {
            String early_morning_sample_test_multi_string = "";
            for (CheckBox cb : early_morning_sample_test_multi.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ztts_early_morning_sample_test_genexpert_ultra)))
                    early_morning_sample_test_multi_string = early_morning_sample_test_multi_string + "GENXPERT ULTRA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ztts_early_morning_sample_test_afb_culture)))
                    early_morning_sample_test_multi_string = early_morning_sample_test_multi_string + "CULTURE FOR MYCOBACTERIA" + " ; ";
            }
            observations.add(new String[]{"TEST REQUESTED FOR EARLY MORNING SAMPLE", early_morning_sample_test_multi_string});
        }
        /*if (orderID_gx_onspot.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"GENEXPERT ORDER ID", App.get(orderID_gx_onspot)});
        }
        if (orderID_afb_onspot.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"AFB CULTURE ORDER ID", App.get(orderID_afb_onspot)});
        }
        if (orderID_gx_early_morning.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"GENEXPERT ORDER ID", App.get(orderID_gx_early_morning)});
        }
        if (orderID_afb_early_morning.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"AFB CULTURE ORDER ID", App.get(orderID_afb_early_morning)});
        }*/


        if (orderID_gx_onspot.getVisibility() == View.VISIBLE && orderID_gx_early_morning.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"GENEXPERT ORDER ID", "GXP_EM_OS_" + App.get(orderID_gx_onspot).split("_")[2]});
        } else if (orderID_gx_onspot.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"GENEXPERT ORDER ID", "GXP_OS_" + App.get(orderID_gx_onspot).split("_")[2]});
        } else if (orderID_gx_early_morning.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"GENEXPERT ORDER ID", "GXP_EM_" + App.get(orderID_gx_early_morning).split("_")[2]});
        }

        if (orderID_afb_onspot.getVisibility() == View.VISIBLE && orderID_afb_early_morning.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"AFB CULTURE ORDER ID", "AFB_EM_OS_" + App.get(orderID_afb_onspot).split("_")[2]});
        } else if (orderID_afb_onspot.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"AFB CULTURE ORDER ID", "AFB_OS_" + App.get(orderID_afb_onspot).split("_")[2]});
        } else if (orderID_afb_early_morning.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"AFB CULTURE ORDER ID", "AFB_EM_" + App.get(orderID_afb_early_morning).split("_")[2]});
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

                String result = serverService.saveEncounterAndObservation(App.getProgram() + "-" + Forms.ZTTS_SAMPLE_COLLECTION, FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
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


        return true;
    }

    @Override
    public void refill(int formId) {

        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("TYPE OF ASSESSMENT")) {
                for (RadioButton rb : assessment_type.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ztts_assessment_type_screening)) && obs[0][1].equals("IDENTIFIED PATIENT THROUGH SCREENING")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_assessment_type_followup)) && obs[0][1].equals("FOLLOW UP")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("NUMBER OF SAMPLE SUBMITTED")) {
                for (RadioButton rb : number_samples.getRadioGroup().getButtons()) {
                    if (rb.getText().equals("0") && obs[0][1].equals("0")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals("1") && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals("2") && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("PATIENT DO NOT PRODUCE SPUTUM SAMPLE")) {
                for (RadioButton rb : reason_nosputum_sample.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ztts_reason_nosputum_sample_pexpectorate)) && obs[0][1].equals("UNABLE TO EXPECTORATE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_reason_nosputum_sample_refuse)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_reason_nosputum_sample_not_home)) && obs[0][1].equals("PATIENT NOT AT HOME")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("SPUTUM SPECIMEN TYPE")) {
                for (RadioButton rb : sputum_specimen_type_single.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ztts_sputum_specimen_type_early_morning)) && obs[0][1].equals("EARLY MORNING SAMPLE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_sputum_specimen_type_on_spot)) && obs[0][1].equals("ON SPOT SAMPLE")) {
                        rb.setChecked(true);
                        break;
                    }
                }

            } else if (obs[0][0].equals("TEST REQUESTED FOR EARLY MORNING SAMPLE")) {
                for (CheckBox cb : early_morning_sample_test_multi.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ztts_early_morning_sample_test_genexpert_ultra)) && obs[0][1].equals("GENXPERT ULTRA")) {
                        cb.setChecked(true);
                    } else if (cb.getText().equals(getResources().getString(R.string.ztts_early_morning_sample_test_afb_culture)) && obs[0][1].equals("CULTURE FOR MYCOBACTERIA")) {
                        cb.setChecked(true);
                    }
                }
                early_morning_sample_test_multi.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TEST REQUESTED FOR ON SPOT SAMPLE") && App.get(number_samples).equals("1")) {
                for (CheckBox cb : onspot_sample_test_multi.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ztts_early_morning_sample_test_genexpert_ultra)) && obs[0][1].equals("GENXPERT ULTRA")) {
                        cb.setChecked(true);
                    } else if (cb.getText().equals(getResources().getString(R.string.ztts_early_morning_sample_test_afb_culture)) && obs[0][1].equals("CULTURE FOR MYCOBACTERIA")) {
                        cb.setChecked(true);
                    }
                }
                onspot_sample_test_multi.setVisibility(View.VISIBLE);
                onspot_sample_test_single.setVisibility(View.GONE);
            } else if (obs[0][0].equals("TEST REQUESTED FOR ON SPOT SAMPLE") && App.get(number_samples).equals("2")) {
                for (RadioButton rb : onspot_sample_test_single.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ztts_early_morning_sample_test_genexpert_ultra)) && obs[0][1].equals("GENXPERT ULTRA")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_early_morning_sample_test_afb_culture)) && obs[0][1].equals("CULTURE FOR MYCOBACTERIA")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                onspot_sample_test_single.setVisibility(View.VISIBLE);
                onspot_sample_test_multi.setVisibility(View.GONE);
            } else if (obs[0][0].equals("GENEXPERT ORDER ID")) {
                orderID_gx_early_morning.setVisibility(View.GONE);
                orderID_gx_onspot.setVisibility(View.GONE);
                if (obs[0][1].contains("OS") && obs[0][1].contains("EM")) {
                    orderID_gx_early_morning.getEditText().setText("GXP_EM_"+obs[0][1].split("_")[3]);
                    orderID_gx_early_morning.getEditText().setFocusable(false);
                    orderID_gx_early_morning.setVisibility(View.VISIBLE);

                    orderID_gx_onspot.getEditText().setText("GXP_OS_"+obs[0][1].split("_")[3]);
                    orderID_gx_onspot.getEditText().setFocusable(false);
                    orderID_gx_onspot.setVisibility(View.VISIBLE);
                } else if (obs[0][1].contains("OS")) {
                    orderID_gx_onspot.getEditText().setText(obs[0][1]);
                    orderID_gx_onspot.getEditText().setFocusable(false);
                    orderID_gx_onspot.setVisibility(View.VISIBLE);
                } else if (obs[0][1].contains("EM")) {
                    orderID_gx_early_morning.getEditText().setText(obs[0][1]);
                    orderID_gx_early_morning.getEditText().setFocusable(false);
                    orderID_gx_early_morning.setVisibility(View.VISIBLE);
                }
            } else if (obs[0][0].equals("AFB CULTURE ORDER ID")) {

                orderID_afb_onspot.setVisibility(View.GONE);
                orderID_afb_early_morning.setVisibility(View.GONE);

                if (obs[0][1].contains("OS") && obs[0][1].contains("EM")) {
                    orderID_afb_early_morning.getEditText().setText("AFB_EM_"+obs[0][1].split("_")[3]);
                    orderID_afb_early_morning.getEditText().setFocusable(false);
                    orderID_afb_early_morning.setVisibility(View.VISIBLE);

                    orderID_afb_onspot.getEditText().setText("AFB_OS_"+obs[0][1].split("_")[3]);
                    orderID_afb_onspot.getEditText().setFocusable(false);
                    orderID_afb_onspot.setVisibility(View.VISIBLE);
                }else if (obs[0][1].contains("OS")) {
                    orderID_afb_onspot.getEditText().setText(obs[0][1]);
                    orderID_afb_onspot.getEditText().setFocusable(false);
                    orderID_afb_onspot.setVisibility(View.VISIBLE);
                } else if (obs[0][1].contains("EM")) {
                    orderID_afb_early_morning.getEditText().setText(obs[0][1]);
                    orderID_afb_early_morning.getEditText().setFocusable(false);
                    orderID_afb_early_morning.setVisibility(View.VISIBLE);
                }
            }

        }
        /*if (!App.get(orderID_afb_onspot).isEmpty()) {
            orderID_afb_onspot.setVisibility(View.VISIBLE);
        }
        if (!App.get(orderID_gx_onspot).isEmpty()) {
            orderID_gx_onspot.setVisibility(View.VISIBLE);
        }*/

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
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;

       /* if (spinner == feverDuration.getSpinner()) {
            feverDuration.getQuestionView().setError(null);
        }*/
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == number_samples.getRadioGroup()) {
            reason_nosputum_sample.setVisibility(View.GONE);
            orderID_gx_onspot.setVisibility(View.GONE);
            orderID_afb_onspot.setVisibility(View.GONE);

            sputum_specimen_type_single.getRadioGroup().clearCheck();

            /////////////////////////////////////////////////////
            sputum_specimen_type_single.setVisibility(View.GONE);
            sputum_specimen_type_multi.setVisibility(View.GONE);


            if (number_samples.getRadioGroup().getSelectedValue().equals("0")) {
                reason_nosputum_sample.setVisibility(View.VISIBLE);

            } else if (number_samples.getRadioGroup().getSelectedValue().equals("1")) {
                sputum_specimen_type_single.setVisibility(View.VISIBLE);

            } else if (number_samples.getRadioGroup().getSelectedValue().equals("2")) {
                sputum_specimen_type_multi.setVisibility(View.VISIBLE);
                early_morning_sample_test_multi.setVisibility(View.VISIBLE);
                onspot_sample_test_single.setVisibility(View.VISIBLE);


            }
        }
        if (radioGroup == sputum_specimen_type_single.getRadioGroup()) {
            onspot_sample_test_multi.setVisibility(View.GONE);
            onspot_sample_test_multi.getCheckBox(0).setChecked(false);
            onspot_sample_test_multi.getCheckBox(1).setChecked(false);

            onspot_sample_test_single.setVisibility(View.GONE);
            onspot_sample_test_single.getRadioGroup().clearCheck();

            early_morning_sample_test_multi.setVisibility(View.GONE);
            early_morning_sample_test_multi.getCheckBox(0).setChecked(false);
            early_morning_sample_test_multi.getCheckBox(1).setChecked(false);

            orderID_gx_early_morning.setVisibility(View.GONE);
            orderID_gx_onspot.setVisibility(View.GONE);

            if (sputum_specimen_type_single.getRadioGroup().getSelectedValue().equals(getString(R.string.ztts_sputum_specimen_type_early_morning))) {
                if (App.get(assessment_type).equals(getString(R.string.ztts_assessment_type_followup))) {
                    orderID_gx_early_morning.setVisibility(View.VISIBLE);
//                    setOrderIdgx_early_morning();
                    updateOrderIDs();

                } else {
                    early_morning_sample_test_multi.setVisibility(View.VISIBLE);
                }
            } else if (sputum_specimen_type_single.getRadioGroup().getSelectedValue().equals(getString(R.string.ztts_sputum_specimen_type_on_spot))) {
                if (App.get(assessment_type).equals(getString(R.string.ztts_assessment_type_followup))) {
                    orderID_gx_onspot.setVisibility(View.VISIBLE);
//                    setOrderIdgx_onspot();
                    updateOrderIDs();

                } else {
                    onspot_sample_test_multi.setVisibility(View.VISIBLE);
                }
            }
        }

        if (radioGroup == assessment_type.getRadioGroup()) {
            number_samples.getRadioGroup().clearCheck();
            sputum_specimen_type_single.getRadioGroup().clearCheck();
            if (assessment_type.getRadioGroup().getSelectedValue().equals(getString(R.string.ztts_assessment_type_screening))) {
                number_samples.getRadioGroup().getButtons().get(2).setVisibility(View.VISIBLE);
            } else if (assessment_type.getRadioGroup().getSelectedValue().equals(getString(R.string.ztts_assessment_type_followup))) {
                number_samples.getRadioGroup().getButtons().get(2).setVisibility(View.GONE);
            }
        }

        if (radioGroup == onspot_sample_test_single.getRadioGroup()) {
            orderID_gx_onspot.setVisibility(View.GONE);
            orderID_afb_onspot.setVisibility(View.GONE);

            if (onspot_sample_test_single.getRadioGroup().getSelectedValue().equals(getString(R.string.ztts_early_morning_sample_test_genexpert_ultra))) {
                orderID_gx_onspot.setVisibility(View.VISIBLE);
//                setOrderIdgx_onspot();
                updateOrderIDs();
            }
            if (onspot_sample_test_single.getRadioGroup().getSelectedValue().equals(getString(R.string.ztts_early_morning_sample_test_afb_culture))) {
                orderID_afb_onspot.setVisibility(View.VISIBLE);
//                setOrderIdafb_onspot();
                updateOrderIDs();

            }
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        for (CheckBox cb : sputum_specimen_type_multi.getCheckedBoxes()) {
            cb.setChecked(true);
        }

        for (CheckBox cb : early_morning_sample_test_multi.getCheckedBoxes()) {

            if (cb.isChecked() && cb.getText().equals(getString(R.string.ztts_early_morning_sample_test_genexpert_ultra))) {
                orderID_gx_early_morning.setVisibility(View.VISIBLE);
//                setOrderIdgx_early_morning();
                updateOrderIDs();

            } else if (!cb.isChecked() && cb.getText().equals(getString(R.string.ztts_early_morning_sample_test_genexpert_ultra))) {
                orderID_gx_early_morning.setVisibility(View.GONE);
            }

            if (cb.isChecked() && cb.getText().equals(getString(R.string.ztts_early_morning_sample_test_afb_culture))) {
                orderID_afb_early_morning.setVisibility(View.VISIBLE);
//                setOrderIdafb_early_morning();
                updateOrderIDs();

            } else if (!cb.isChecked() && cb.getText().equals(getString(R.string.ztts_early_morning_sample_test_afb_culture))) {
                orderID_afb_early_morning.setVisibility(View.GONE);
            }

        }
        for (CheckBox cb : onspot_sample_test_multi.getCheckedBoxes()) {

            if (cb.isChecked() && cb.getText().equals(getString(R.string.ztts_early_morning_sample_test_genexpert_ultra))) {
                orderID_gx_onspot.setVisibility(View.VISIBLE);
//                setOrderIdgx_onspot();
                updateOrderIDs();

            } else if (!cb.isChecked() && cb.getText().equals(getString(R.string.ztts_early_morning_sample_test_genexpert_ultra))) {
                orderID_gx_onspot.setVisibility(View.GONE);
            }

            if (cb.isChecked() && cb.getText().equals(getString(R.string.ztts_early_morning_sample_test_afb_culture))) {
                orderID_afb_onspot.setVisibility(View.VISIBLE);
//                setOrderIdafb_onspot();
                updateOrderIDs();

            } else if (!cb.isChecked() && cb.getText().equals(getString(R.string.ztts_early_morning_sample_test_afb_culture))) {
                orderID_afb_onspot.setVisibility(View.GONE);
            }
        }

        if (onspot_sample_test_single.getVisibility() == View.VISIBLE && App.get(onspot_sample_test_single).equals(getString(R.string.ztts_early_morning_sample_test_genexpert_ultra))) {
            orderID_gx_onspot.setVisibility(View.VISIBLE);
        } else if (onspot_sample_test_single.getVisibility() == View.VISIBLE && App.get(onspot_sample_test_single).equals(getString(R.string.ztts_early_morning_sample_test_afb_culture))) {
            orderID_afb_onspot.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        reason_nosputum_sample.setVisibility(View.GONE);
        sputum_specimen_type_single.setVisibility(View.GONE);
        sputum_specimen_type_multi.setVisibility(View.GONE);
        onspot_sample_test_single.setVisibility(View.GONE);
        early_morning_sample_test_multi.setVisibility(View.GONE);
        onspot_sample_test_multi.setVisibility(View.GONE);

        orderID_afb_onspot.setVisibility(View.GONE);
        orderID_gx_onspot.setVisibility(View.GONE);

        orderID_afb_early_morning.setVisibility(View.GONE);
        orderID_gx_early_morning.setVisibility(View.GONE);

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

    public void setOrderIdgx_onspot() {
        Date nowDate = new Date();
        orderID_gx_onspot.getEditText().setText("GXP_OS_" + App.getSqlDateTime(nowDate));
        orderID_gx_onspot.getEditText().setKeyListener(null);
        orderID_gx_onspot.getEditText().setFocusable(false);
    }

    public void setOrderIdafb_onspot() {
        Date nowDate = new Date();
        orderID_afb_onspot.getEditText().setText("AFB_OS_" + App.getSqlDateTime(nowDate));
        orderID_afb_onspot.getEditText().setKeyListener(null);
        orderID_afb_onspot.getEditText().setFocusable(false);
    }

    public void setOrderIdgx_early_morning() {
        Date nowDate = new Date();
        orderID_gx_early_morning.getEditText().setText("GXP_EM_" + App.getSqlDateTime(nowDate));
        orderID_gx_early_morning.getEditText().setKeyListener(null);
        orderID_gx_early_morning.getEditText().setFocusable(false);
    }

    public void setOrderIdafb_early_morning() {
        Date nowDate = new Date();
        orderID_afb_early_morning.getEditText().setText("AFB_EM_" + App.getSqlDateTime(nowDate));
        orderID_afb_early_morning.getEditText().setKeyListener(null);
        orderID_afb_early_morning.getEditText().setFocusable(false);
    }

    public void updateOrderIDs() {
        setOrderIdgx_onspot();
        setOrderIdgx_early_morning();
        setOrderIdafb_onspot();
        setOrderIdafb_early_morning();
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
