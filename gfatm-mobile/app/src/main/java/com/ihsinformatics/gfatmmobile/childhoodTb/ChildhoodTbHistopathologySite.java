package com.ihsinformatics.gfatmmobile.childhoodTb;

import android.annotation.SuppressLint;
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
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
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

public class ChildhoodTbHistopathologySite extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    TitledButton formDate;
    TitledRadioGroup formType;
    TitledEditText orderId;
    TitledRadioGroup pointTestBeingDone;
    TitledSpinner monthTreatment;
    TitledEditText histopathologySite;
    TitledSpinner orderIds;
    TitledEditText testId;
    TitledRadioGroup histopathologyResult;


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
        FORM_NAME = Forms.CHILDHOODTB_HISTOPATHOLOGY_TEST;
        FORM = Forms.childhoodTb_histopathology_order_and_result;

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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        orderId = new TitledEditText(context,getResources().getString(R.string.ctb_histopathology_order),getResources().getString(R.string.order_id),"","",20,RegexUtil.OTHER_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,true);
        formType = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_type_of_form),getResources().getStringArray(R.array.ctb_type_of_form_list),null,App.HORIZONTAL,App.VERTICAL,true);
        pointTestBeingDone = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_point_test_being_done),getResources().getStringArray(R.array.ctb_ultrasound_test_point_list),getResources().getString(R.string.ctb_diagnostic),App.VERTICAL,App.VERTICAL,true);
        monthTreatment= new TitledSpinner(context,null,getResources().getString(R.string.ctb_month_treatment),getResources().getStringArray(R.array.ctb_0_to_24),null,App.HORIZONTAL,true);
        updateFollowUpMonth();
        histopathologySite = new TitledEditText(context,null,getResources().getString(R.string.ctb_histopathology_site),"","",50,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,false);
        histopathologyResult = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_histopathology_result),getResources().getStringArray(R.array.ctb_suggestive_tb_normal),getResources().getString(R.string.ctb_suggestive_tb),App.VERTICAL,App.VERTICAL,true);


        orderIds = new TitledSpinner(context, getResources().getString(R.string.ctb_histopathology_result), getResources().getString(R.string.order_id), getResources().getStringArray(R.array.pet_empty_array), "", App.HORIZONTAL);
        testId = new TitledEditText(context,null,getResources().getString(R.string.ctb_test_id),"","",20,RegexUtil.OTHER_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,false);

        views = new View[]{formDate.getButton(),formType.getRadioGroup(), orderId.getEditText(),pointTestBeingDone.getRadioGroup()
                ,histopathologyResult.getRadioGroup(),orderIds.getSpinner(),testId.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formType,formDate, orderId,pointTestBeingDone,monthTreatment,histopathologySite,orderIds,testId,histopathologyResult}};

        formDate.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        pointTestBeingDone.getRadioGroup().setOnCheckedChangeListener(this);
        monthTreatment.getSpinner().setOnItemSelectedListener(this);
        histopathologyResult.getRadioGroup().setOnCheckedChangeListener(this);
        orderIds.getSpinner().setOnItemSelectedListener(this);

        resetViews();

    }

    public void updateFollowUpMonth() {

        String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "REGISTRATION DATE");
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

                if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.ctb_result))) {

                    if (!App.get(orderIds).equals("")) {
                        String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), App.getProgram() + "-" + "Histopathology Test Order", "ORDER ID", App.get(orderIds));

                        String format = "";
                        if (encounterDateTime.contains("/")) {
                            format = "dd/MM/yyyy";
                        } else {
                            format = "yyyy-MM-dd";
                        }

                        Date orderDate = App.stringToDate(encounterDateTime, format);

                        if (formDateCalendar.before(App.getCalendar(orderDate))) {

                            Date dDate = App.stringToDate(formDa, "EEEE, MMM dd,yyyy");
                            if (dDate.before(orderDate)) {
                                formDateCalendar = Calendar.getInstance();
                                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                            } else {
                                formDateCalendar = App.getCalendar(dDate);
                                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                            }

                            snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_result_date_cannot_be_before_order_date), Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();

                        }

                    }
                } else if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.ctb_order))) {
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
            }

        } else{
            String formDa = formDate.getButton().getText().toString();

            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.ctb_result))) {

                if (!App.get(orderIds).equals("")) {
                    String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), App.getProgram() + "-" + "Histopathology Test Order", "ORDER ID", App.get(orderIds));

                    String format = "";
                    if (encounterDateTime.contains("/")) {
                        format = "dd/MM/yyyy";
                    } else {
                        format = "yyyy-MM-dd";
                    }

                    Date orderDate = App.stringToDate(encounterDateTime, format);

                    if (formDateCalendar.before(App.getCalendar(orderDate))) {

                        Date dDate = App.stringToDate(formDa, "EEEE, MMM dd,yyyy");
                        if (dDate.before(orderDate)) {
                            formDateCalendar = Calendar.getInstance();
                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        } else {
                            formDateCalendar = App.getCalendar(dDate);
                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        }

                        snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_result_date_cannot_be_before_order_date), Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();

                    }

                }
            }
        }
        formDate.getButton().setEnabled(true);
        updateFollowUpMonth();

    }

    @Override
    public boolean validate() {
        boolean error=false;
        Boolean formCheck = false;

        if (App.get(formType).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            formType.getRadioGroup().getButtons().get(1).setError(getString(R.string.empty_field));
            formType.getRadioGroup().requestFocus();
            error = true;
        }
        else{
            formType.getRadioGroup().getButtons().get(1).setError(null);
        }
        if(histopathologySite.getVisibility()==View.VISIBLE){
            if(!App.get(histopathologySite).isEmpty()) {
                if (App.get(histopathologySite).trim().length() <= 0) {
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    histopathologySite.getEditText().setError(getString(R.string.ctb_spaces_only));
                    histopathologySite.getEditText().requestFocus();
                    error = true;
                }
            }
        }

        Boolean flag = true;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            if (saveFlag) {
                flag = false;
            }else {
                flag = true;
            }
        }


        if(orderIds.getVisibility()==View.VISIBLE && flag){
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "Histopathology Test Result", "ORDER ID");
            if(resultTestIds != null){
            for(String id : resultTestIds) {

                if (id.equals(App.get(orderIds))) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.ctb_order_result_found_error) + App.get(orderIds));
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

                    return false;
                }
              }
            }
        }

        if(testId.getVisibility() == View.VISIBLE && flag){
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "Histopathology Test Result", "TEST ID");
            if(resultTestIds != null) {
                for (String id : resultTestIds) {
                    if (id.equals(App.get(testId))) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        alertDialog.setMessage(getResources().getString(R.string.ctb_test_result_found_error) + App.get(testId));
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

                        return false;
                    }

                }
            }

        }


        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            if (formCheck) {
                alertDialog.setMessage(getString(R.string.ctb_select_form_type));
            } else {
                alertDialog.setMessage(getString(R.string.form_error));
            }
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
        if (App.get(formType).equals(getResources().getString(R.string.ctb_order))) {
            observations.add(new String[]{"ORDER ID", App.get(orderId)});

            observations.add(new String[]{"TEST CONTEXT STATUS", App.get(pointTestBeingDone).equals(getResources().getString(R.string.ctb_diagnostic)) ? "DIAGNOSTIC TESTING" :
                            "REGULAR FOLLOW UP"});
            if(monthTreatment.getVisibility()==View.VISIBLE){
                observations.add(new String[]{"FOLLOW-UP MONTH", App.get(monthTreatment)});
            }
            observations.add(new String[]{"HISTOPATHOLOGY SITE", App.get(histopathologySite)});

        } else if (App.get(formType).equals(getResources().getString(R.string.ctb_result))) {
            observations.add(new String[]{"ORDER ID", App.get(orderIds)});
            if(!App.get(testId).isEmpty()) {
                observations.add(new String[]{"TEST ID", App.get(testId)});
            }
            observations.add(new String[]{"HISTOPATHOLOGY RESULT", App.get(histopathologyResult).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" : "NORMAL"});
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

                if (App.get(formType).equals(getResources().getString(R.string.ctb_order))){
                    result = serverService.saveEncounterAndObservation("Histopathology Test Order", FORM, formDateCalendar, observations.toArray(new String[][]{}),true);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                } else if (App.get(formType).equals(getResources().getString(R.string.ctb_result))) {
                    result = serverService.saveEncounterAndObservation("Histopathology Test Result", FORM, formDateCalendar, observations.toArray(new String[][]{}),false);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                }

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
                    alertDialog.setMessage(getResources().getString(R.string.insert_error));
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
    public void refill(int encounterId) {
            OfflineForm fo = serverService.getOfflineFormById(encounterId);
            String date = fo.getFormDate();
            ArrayList<String[][]> obsValue = fo.getObsValue();

            formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());


            for (int i = 0; i < obsValue.size(); i++) {
                String[][] obs = obsValue.get(i);
                if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                    timeTakeToFill = obs[0][1];
                }else if(fo.getFormName().contains("Order")) {
                    formType.getRadioGroup().getButtons().get(0).setChecked(true);
                    formType.getRadioGroup().getButtons().get(1).setEnabled(false);
                    if (obs[0][0].equals("ORDER ID")) {
                        orderId.getEditText().setKeyListener(null);
                        orderId.getEditText().setText(obs[0][1]);
                    }else if (obs[0][0].equals("TEST CONTEXT STATUS")) {
                        for (RadioButton rb : pointTestBeingDone.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.ctb_diagnostic)) && obs[0][1].equals("DIAGNOSTIC TESTING")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_followup)) && obs[0][1].equals("REGULAR FOLLOW UP")) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                        pointTestBeingDone.setVisibility(View.VISIBLE);
                    }else if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                        monthTreatment.getSpinner().selectValue(obs[0][1]);
                    }else if (obs[0][0].equals("HISTOPATHOLOGY SITE")) {
                        histopathologySite.getEditText().setText(obs[0][1]);
                    }
                    submitButton.setEnabled(true);
                }else{
                    formType.getRadioGroup().getButtons().get(1).setChecked(true);
                    formType.getRadioGroup().getButtons().get(0).setEnabled(false);
                    if (obs[0][0].equals("ORDER ID")) {
                        orderIds.getSpinner().selectValue(obs[0][1]);
                    }
                    else if (obs[0][0].equals("TEST ID")) {
                        testId.getEditText().setText(obs[0][1]);
                    }
                    else if (obs[0][0].equals("HISTOPATHOLOGY RESULT")) {
                        for (RadioButton rb : histopathologyResult.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.ctb_normal)) && obs[0][1].equals("NORMAL")) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                        histopathologyResult.setVisibility(View.VISIBLE);
                    }
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

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;

        if (spinner == orderIds.getSpinner()) {
            updateDisplay();
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.setEnabled(true);

        if (snackbar != null)
            snackbar.dismiss();
        orderId.getEditText().setKeyListener(null);
        formType.getRadioGroup().getButtons().get(0).setEnabled(true);
        formType.getRadioGroup().getButtons().get(1).setEnabled(true);
        orderId.setVisibility(View.GONE);
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        formDate.setVisibility(View.GONE);
        goneVisibility();
        submitButton.setEnabled(false);

        String[] testIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "Histopathology Test Order", "ORDER ID");
        if(testIds != null) {
            orderIds.getSpinner().setSpinnerData(testIds);
        }

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

    void goneVisibility(){
        pointTestBeingDone.setVisibility(View.GONE);
        monthTreatment.setVisibility(View.GONE);
        histopathologySite.setVisibility(View.GONE);
        histopathologyResult.setVisibility(View.GONE);
        orderIds.setVisibility(View.GONE);
        orderId.setVisibility(View.GONE);
        testId.setVisibility(View.GONE);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == formType.getRadioGroup()) {
            formDate.setVisibility(View.VISIBLE);
            submitButton.setEnabled(true);
            showTestOrderOrTestResult();
        }
        if (group == pointTestBeingDone.getRadioGroup()) {
            if (pointTestBeingDone.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_followup))) {
                monthTreatment.setVisibility(View.VISIBLE);

            } else {
                monthTreatment.setVisibility(View.GONE);
            }
        }

    }

    void showTestOrderOrTestResult() {
        if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.ctb_order))) {
            formDate.setVisibility(View.VISIBLE);
            pointTestBeingDone.setVisibility(View.VISIBLE);
            pointTestBeingDone.getRadioGroup().selectDefaultValue();
            if(App.get(pointTestBeingDone).equals(getResources().getString(R.string.ctb_followup))){
                monthTreatment.setVisibility(View.VISIBLE);
                updateFollowUpMonth();
            }
            histopathologySite.setVisibility(View.VISIBLE);
            histopathologySite.getEditText().setDefaultValue();
            orderId.setVisibility(View.VISIBLE);
            Date nowDate = new Date();
            orderId.getEditText().setText(App.getSqlDateTime(nowDate));

            testId.setVisibility(View.GONE);
            orderIds.setVisibility(View.GONE);
            histopathologyResult.setVisibility(View.GONE);
        } else if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.ctb_result))) {

            orderId.setVisibility(View.GONE);
            pointTestBeingDone.setVisibility(View.GONE);
            histopathologySite.setVisibility(View.GONE);
            monthTreatment.setVisibility(View.GONE);

            formDate.setVisibility(View.VISIBLE);
            formDate.setDefaultValue();
            histopathologyResult.setVisibility(View.VISIBLE);
            histopathologyResult.getRadioGroup().selectDefaultValue();

            orderIds.setVisibility(View.VISIBLE);
            testId.setVisibility(View.VISIBLE);
            testId.getEditText().setDefaultValue();
            String[] testIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "Histopathology Test Order", "ORDER ID");
            if(testIds == null || testIds.length == 0){
                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage(getResources().getString(R.string.ctb_no_histopathology_order_found));
                submitButton.setEnabled(false);
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
                return;
            }

            if(testIds != null) {
                orderIds.getSpinner().setSpinnerData(testIds);
            }




           /*String value =  serverService.getObsValueByObs(App.getPatientId(), App.getProgram() + "-" + "Histopathology Test Order", "ORDER ID", App.get(orderIds),"TEST CONTEXT STATUS");*/

        }
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
            updateDisplay();

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
