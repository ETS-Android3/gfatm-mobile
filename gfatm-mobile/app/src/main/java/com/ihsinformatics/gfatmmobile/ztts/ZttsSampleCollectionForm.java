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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
    TitledRadioGroup requested_genxpert;
    TitledRadioGroup sample_g_x;
    TitledRadioGroup requested_afb;
    TitledCheckBoxes sample_afb_culture;
    TitledEditText orderID_gx;
    TitledEditText orderID_afb;


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
        requested_genxpert = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_requested_genxpert), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.VERTICAL, App.VERTICAL, true);
        orderID_gx = new TitledEditText(context, null, getResources().getString(R.string.ztts_orderid_gx), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        sample_g_x = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_sample_g_x), getResources().getStringArray(R.array.ztts_sample_g_x_options), "", App.VERTICAL, App.VERTICAL, true);
        requested_afb = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_requested_afb), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.VERTICAL, App.VERTICAL, true);
        orderID_afb = new TitledEditText(context, null, getResources().getString(R.string.ztts_orderid_afb), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        sample_afb_culture = new TitledCheckBoxes(context, null, getResources().getString(R.string.ztts_sample_afb), getResources().getStringArray(R.array.ztts_sample_afb_options), null, App.VERTICAL, App.VERTICAL, true);


        becteriologicalTestTextView = new MyTextView(context, "Bacteriological Tests");
        becteriologicalTestTextView.setTypeface(null, Typeface.BOLD);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), assessment_type.getRadioGroup(), number_samples.getRadioGroup(), reason_nosputum_sample.getRadioGroup(), requested_genxpert.getRadioGroup(),
                orderID_gx.getEditText(), sample_g_x.getRadioGroup(), requested_afb.getRadioGroup(), orderID_afb.getEditText(), sample_afb_culture};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, assessment_type, becteriologicalTestTextView, number_samples, reason_nosputum_sample, requested_genxpert, orderID_gx, sample_g_x, requested_afb, orderID_afb, sample_afb_culture}};


        formDate.getButton().setOnClickListener(this);
        number_samples.getRadioGroup().setOnCheckedChangeListener(this);
        requested_genxpert.getRadioGroup().setOnCheckedChangeListener(this);
        requested_afb.getRadioGroup().setOnCheckedChangeListener(this);

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
        if (sample_g_x.getVisibility() == View.VISIBLE && App.get(sample_g_x).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            sample_g_x.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            sample_g_x.getQuestionView().setError(null);
        }
        boolean flag = false;
        if (sample_afb_culture.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : sample_afb_culture.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                sample_afb_culture.getQuestionView().setError(getString(R.string.empty_field));
                error = true;
            } else {
                sample_afb_culture.getQuestionView().setError(null);
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
            observations.add(new String[]{"PATIENT DO NOT PRODUCE SPUTUM SAMPLE", App.get(reason_nosputum_sample).equals(getString(R.string.ztts_reason_nosputum_sample_pexpectorate)) ? "UNABLE TO EXPECTORATE" : App.get(number_samples).equals(getString(R.string.ztts_reason_nosputum_sample_refuse)) ? "REFUSED" : "PATIENT NOT AT HOME"});

        if (requested_genxpert.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TEST REQUESTED GENXPERT MTB/RIF", App.get(requested_genxpert).equals(getString(R.string.yes)) ? "YES" : "NO"});

        if (sample_g_x.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SAMPLE COLLECTION FOR GENXPERT", App.get(sample_g_x).equals(getString(R.string.ztts_sample_g_x_spot)) ? "1ST ON SPOT SAMPLE" : "EARLY MORNING"});

        if (requested_afb.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TEST REQUESTED AFB CULTURE", App.get(requested_afb).equals(getString(R.string.yes)) ? "YES" : "NO"});

        if (sample_afb_culture.getVisibility() == View.VISIBLE) {
            String sample_afb_culture_String = "";
            for (CheckBox cb : sample_afb_culture.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ztts_sample_g_x_early)))
                    sample_afb_culture_String = sample_afb_culture_String + "EARLY MORNING" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ztts_sample_afb_secon_spot)))
                    sample_afb_culture_String = sample_afb_culture_String + "2ND ON SPOT SAMPLE" + " ; ";
            }
            observations.add(new String[]{"SAMPLE COLLECTION FOR AFB CULTURE", sample_afb_culture_String});
        }

        if (orderID_gx.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"GENEXPERT ORDER ID", App.get(orderID_gx)});
        }
        if (orderID_afb.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"AFB CULTURE ORDER ID", App.get(orderID_afb)});
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
            } else if (obs[0][0].equals("TEST REQUESTED GENXPERT MTB/RIF")) {
                for (RadioButton rb : requested_genxpert.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("SAMPLE COLLECTION FOR GENXPERT")) {
                for (RadioButton rb : sample_g_x.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ztts_sample_g_x_early)) && obs[0][1].equals("EARLY MORNING")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_sample_g_x_spot)) && obs[0][1].equals("1ST ON SPOT SAMPLE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TEST REQUESTED AFB CULTURE")) {
                for (RadioButton rb : requested_afb.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("SAMPLE COLLECTION FOR AFB CULTURE")) {
                for (CheckBox cb : sample_afb_culture.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ztts_sample_g_x_early)) && obs[0][1].equals("EARLY MORNING")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ztts_sample_afb_secon_spot)) && obs[0][1].equals("2ND ON SPOT SAMPLE")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            }else  if (obs[0][0].equals("GENEXPERT ORDER ID")) {
                orderID_gx.getEditText().setText(obs[0][1]);
                orderID_gx.getEditText().setFocusable(false);
            } else if (obs[0][0].equals("AFB CULTURE ORDER ID")) {
                orderID_afb.getEditText().setText(obs[0][1]);
                orderID_afb.getEditText().setFocusable(false);
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
            if (number_samples.getRadioGroup().getSelectedValue().equals("0")) {
                reason_nosputum_sample.setVisibility(View.VISIBLE);

                requested_genxpert.setVisibility(View.GONE);
                requested_afb.setVisibility(View.GONE);
                sample_g_x.setVisibility(View.GONE);
                sample_afb_culture.setVisibility(View.GONE);
                orderID_gx.setVisibility(View.GONE);
                orderID_afb.setVisibility(View.GONE);

            } else {
                reason_nosputum_sample.setVisibility(View.GONE);

                requested_genxpert.setVisibility(View.VISIBLE);
                if (requested_genxpert.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                    sample_g_x.setVisibility(View.VISIBLE);
                    orderID_gx.setVisibility(View.VISIBLE);
                }
                requested_afb.setVisibility(View.VISIBLE);
                if (requested_afb.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                    sample_afb_culture.setVisibility(View.VISIBLE);
                    orderID_afb.setVisibility(View.VISIBLE);
                }
            }
        }

        if (radioGroup == requested_genxpert.getRadioGroup()) {
            if (requested_genxpert.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                sample_g_x.setVisibility(View.VISIBLE);
                orderID_gx.setVisibility(View.VISIBLE);
                setOrderIdgx();

            } else {
                sample_g_x.setVisibility(View.GONE);
                orderID_gx.setVisibility(View.GONE);
            }
        }

        if (radioGroup == requested_afb.getRadioGroup()) {
            if (requested_afb.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                sample_afb_culture.setVisibility(View.VISIBLE);
                orderID_afb.setVisibility(View.VISIBLE);
                setOrderIdafb();
            } else {
                sample_afb_culture.setVisibility(View.GONE);
                orderID_afb.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        reason_nosputum_sample.setVisibility(View.GONE);
        requested_genxpert.setVisibility(View.GONE);
        requested_afb.setVisibility(View.GONE);

        orderID_afb.setVisibility(View.GONE);
        orderID_gx.setVisibility(View.GONE);
        sample_g_x.setVisibility(View.GONE);
        sample_afb_culture.setVisibility(View.GONE);

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

    public void setOrderIdgx() {
        Date nowDate = new Date();
        orderID_gx.getEditText().setText(App.getSqlDateTime(nowDate));
        orderID_gx.getEditText().setKeyListener(null);
        orderID_gx.getEditText().setFocusable(false);
    }

    public void setOrderIdafb() {
        Date nowDate = new Date();
        orderID_afb.getEditText().setText(App.getSqlDateTime(nowDate));
        orderID_afb.getEditText().setKeyListener(null);
        orderID_afb.getEditText().setFocusable(false);
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
