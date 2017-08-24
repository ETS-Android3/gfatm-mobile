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

public class ChildhoodTbAFBSmearTest extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    TitledButton formDate;
    TitledRadioGroup formType;
    TitledEditText orderId;
    TitledButton dateSubmission;
    TitledRadioGroup pointTestBeingDone;
    TitledSpinner monthTreatment;
    TitledRadioGroup specimenType;
    TitledSpinner specimenComeFrom;
    TitledEditText otherSpecimentComeFrom;
    TitledSpinner orderIds;
    TitledEditText testId;
    TitledSpinner smearResult;
    TitledEditText afbSeenOneField;

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
        FORM_NAME = Forms.CHILDHOODTB_AFB_SMEAR_ORDER_AND_RESULT;
        FORM = Forms.childhoodTb_afb_smear_order_and_result;

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
        orderId = new TitledEditText(context,null,getResources().getString(R.string.order_id),"","",20,RegexUtil.OTHER_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,true);
        formType = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_type_of_form),getResources().getStringArray(R.array.ctb_type_of_form_list),null,App.HORIZONTAL,App.VERTICAL,true);
        dateSubmission = new TitledButton(context, null, getResources().getString(R.string.ctb_date_submission), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        dateSubmission.setTag("dateSubmission");
        pointTestBeingDone = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_point_test_being_done),getResources().getStringArray(R.array.ctb_point_test_being_done_list),getResources().getString(R.string.ctb_baseline),App.VERTICAL,App.VERTICAL,true);
        monthTreatment= new TitledSpinner(context,null,getResources().getString(R.string.ctb_month_treatment),getResources().getStringArray(R.array.ctb_0_to_24),null,App.HORIZONTAL,true);
        updateFollowUpMonth();
        specimenType = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_specimen_type),getResources().getStringArray(R.array.ctb_specimen_type_list),null,App.HORIZONTAL,App.VERTICAL,true);
        specimenComeFrom = new TitledSpinner(context,null,getResources().getString(R.string.ctb_speciment_route),getResources().getStringArray(R.array.ctb_speciment_route_list),null,App.VERTICAL,true);
        otherSpecimentComeFrom = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_specify),"","",50,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,false);


        orderIds = new TitledSpinner(context, "", getResources().getString(R.string.order_id), getResources().getStringArray(R.array.pet_empty_array), "", App.HORIZONTAL);
        testId = new TitledEditText(context,null,getResources().getString(R.string.ctb_test_id),"","",20,RegexUtil.OTHER_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,false);
        smearResult = new TitledSpinner(context,null,getResources().getString(R.string.ctb_smear_result),getResources().getStringArray(R.array.fast_smear_result_list),getResources().getString(R.string.ctb_negative),App.VERTICAL);
        afbSeenOneField = new TitledEditText(context,null,getResources().getString(R.string.ctb_afb_seen_in_one_field),"","",4,RegexUtil.NUMERIC_FILTER,InputType.TYPE_CLASS_NUMBER,App.HORIZONTAL,false);

        views = new View[]{formDate.getButton(),formType.getRadioGroup(),dateSubmission.getButton(),pointTestBeingDone.getRadioGroup(),
                specimenType.getRadioGroup(),specimenComeFrom.getSpinner(),smearResult.getSpinner(),testId.getEditText(),
                monthTreatment.getSpinner(), otherSpecimentComeFrom.getEditText(), afbSeenOneField.getEditText()
                ,orderIds.getSpinner(), orderId.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formType,formDate,orderId,dateSubmission,pointTestBeingDone,monthTreatment,specimenType,specimenComeFrom,otherSpecimentComeFrom
                        ,orderIds,testId,smearResult,afbSeenOneField}};

        formDate.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        monthTreatment.getSpinner().setOnItemSelectedListener(this);
        dateSubmission.getButton().setOnClickListener(this);
        pointTestBeingDone.getRadioGroup().setOnCheckedChangeListener(this);
        specimenType.getRadioGroup().setOnCheckedChangeListener(this);
        specimenComeFrom.getSpinner().setOnItemSelectedListener(this);
        smearResult.getSpinner().setOnItemSelectedListener(this);
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
                        String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), App.getProgram() + "-" + "AFB Smear Test Order", "ORDER ID", App.get(orderIds));

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
                    String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), App.getProgram() + "-" + "AFB Smear Test Order", "ORDER ID", App.get(orderIds));

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

        if (!dateSubmission.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString())) {
            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();
            Date date = new Date();

            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            }else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                dateSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }
            else if (secondDateCalendar.before(formDateCalendar)) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.ctb_submission_can_not_be_less_than_form_date), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                dateSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }else
                dateSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }
        updateFollowUpMonth();
        formDate.getButton().setEnabled(true);
        dateSubmission.getButton().setEnabled(true);
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

        if (specimenType.getVisibility() == View.VISIBLE && App.get(specimenType).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            specimenType.getRadioGroup().getButtons().get(1).setError(getString(R.string.empty_field));
            specimenType.getRadioGroup().requestFocus();
            error = true;
        }
        else{
            specimenType.getRadioGroup().getButtons().get(1).setError(null);
        }
        if (otherSpecimentComeFrom.getVisibility() == View.VISIBLE ) {
            if(App.get(otherSpecimentComeFrom).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherSpecimentComeFrom.getEditText().setError(getString(R.string.empty_field));
                otherSpecimentComeFrom.getEditText().requestFocus();
                error = true;
            }
            else if(App.get(otherSpecimentComeFrom).trim().length() <= 0){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherSpecimentComeFrom.getEditText().setError(getString(R.string.ctb_spaces_only));
                otherSpecimentComeFrom.getEditText().requestFocus();
                error = true;
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
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "AFB Smear Test Result", "ORDER ID");
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
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "AFB Smear Test Result", "TEST ID");
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
            observations.add(new String[]{"SPECIMEN SUBMISSION DATE", App.getSqlDateTime(secondDateCalendar)});
            observations.add(new String[]{"TEST CONTEXT STATUS", App.get(pointTestBeingDone).equals(getResources().getString(R.string.ctb_baseline)) ? "BASELINE" :
                    App.get(pointTestBeingDone).equals(getResources().getString(R.string.ctb_baseline_repeat)) ? "BASELINE REPEAT" :
                        "REGULAR FOLLOW UP"});
            if(monthTreatment.getVisibility()==View.VISIBLE){
                observations.add(new String[]{"FOLLOW-UP MONTH", App.get(monthTreatment)});
            }

            observations.add(new String[]{"SPECIMEN TYPE", App.get(specimenType).equals(getResources().getString(R.string.ctb_sputum)) ? "SPUTUM" :
                    "EXTRA-PULMONARY TUBERCULOSIS"});

            if(specimenComeFrom.getVisibility()==View.VISIBLE){
                observations.add(new String[]{"SPECIMEN SOURCE", App.get(specimenComeFrom).equals(getResources().getString(R.string.ctb_lymph)) ? "LYMPHOCYTES" :
                        (App.get(specimenComeFrom).equals(getResources().getString(R.string.ctb_pleural_fluid)) ? "PLEURAL EFFUSION" :
                                (App.get(specimenComeFrom).equals(getResources().getString(R.string.ctb_pus)) ? "PUS" :
                                        "OTHER SPECIMEN SOURCE"))});
            }
            if(otherSpecimentComeFrom.getVisibility()==View.VISIBLE){
                observations.add(new String[]{"OTHER SPECIMEN SOURCE", App.get(otherSpecimentComeFrom)});
            }
        } else if (App.get(formType).equals(getResources().getString(R.string.ctb_result))) {
            observations.add(new String[]{"ORDER ID", App.get(orderIds)});
            if(!App.get(testId).isEmpty()) {
                observations.add(new String[]{"TEST ID", App.get(testId)});
            }
            observations.add(new String[]{"SPUTUM FOR ACID FAST BACILLI", App.get(smearResult).equals(getResources().getString(R.string.ctb_negative)) ? "NEGATIVE" :
                    (App.get(smearResult).equals(getResources().getString(R.string.ctb_scanty_3_24)) ? "SCANTY 3 - 24" :
                            (App.get(smearResult).equals(getResources().getString(R.string.ctb_1_positive)) ? "ONE PLUS" :
                                    (App.get(smearResult).equals(getResources().getString(R.string.ctb_2_positive)) ? "TWO PLUS" :
                                            "THREE PLUS")))});

            if(afbSeenOneField.getVisibility()==View.VISIBLE) {
                observations.add(new String[]{"AFB COUNT", App.get(afbSeenOneField)});
            }
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
                    result = serverService.saveEncounterAndObservation("AFB Smear Test Order", FORM, formDateCalendar, observations.toArray(new String[][]{}),true);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                } else if (App.get(formType).equals(getResources().getString(R.string.ctb_result))) {
                    result = serverService.saveEncounterAndObservation("AFB Smear Test Result", FORM, formDateCalendar, observations.toArray(new String[][]{}),false);
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
                } else if (obs[0][0].equals("SPECIMEN SUBMISSION DATE")) {
                    String secondDate = obs[0][1];
                    secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                    dateSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                    dateSubmission.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("TEST CONTEXT STATUS")) {
                    for (RadioButton rb : pointTestBeingDone.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_baseline)) && obs[0][1].equals("BASELINE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_baseline_repeat)) && obs[0][1].equals("BASELINE REPEAT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_followup)) && obs[0][1].equals("REGULAR FOLLOW UP")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    pointTestBeingDone.setVisibility(View.VISIBLE);
                }  else if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                    monthTreatment.getSpinner().selectValue(obs[0][1]);
                }else if (obs[0][0].equals("SPECIMEN TYPE")) {
                    for (RadioButton rb : specimenType.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_sputum)) && obs[0][1].equals("SPUTUM")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_extra_pulmonary)) && obs[0][1].equals("EXTRA-PULMONARY TUBERCULOSIS")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    specimenType.setVisibility(View.VISIBLE);
                }
                else if (obs[0][0].equals("SPECIMEN SOURCE")) {
                    String value = obs[0][1].equals("LYMPHOCYTES") ? getResources().getString(R.string.ctb_lymph) :
                            (obs[0][1].equals("PLEURAL EFFUSION") ? getResources().getString(R.string.ctb_pleural_fluid) :
                                    (obs[0][1].equals("PUS") ? getResources().getString(R.string.ctb_pus) :
                                        getResources().getString(R.string.ctb_other_title)));
                    if(value.equalsIgnoreCase(getResources().getString(R.string.ctb_other_title))){
                        otherSpecimentComeFrom.setVisibility(View.VISIBLE);
                    }
                    specimenComeFrom.getSpinner().selectValue(value);
                    specimenComeFrom.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("OTHER SPECIMEN SOURCE")) {
                    otherSpecimentComeFrom.getEditText().setText(obs[0][1]);
                }
                submitButton.setEnabled(true);
            }else{
                formType.getRadioGroup().getButtons().get(1).setChecked(true);
                formType.getRadioGroup().getButtons().get(0).setEnabled(false);
                if (obs[0][0].equals("ORDER ID")) {
                    orderIds.getSpinner().selectValue(obs[0][1]);
                    orderIds.getSpinner().setClickable(false);
                }
                else if (obs[0][0].equals("TEST ID")) {
                    testId.getEditText().setText(obs[0][1]);
                }
                else if (obs[0][0].equals("SPUTUM FOR ACID FAST BACILLI")) {
                    String value = obs[0][1].equals("NEGATIVE") ? getResources().getString(R.string.ctb_negative) :
                            (obs[0][1].equals("SCANTY 3 - 24") ? getResources().getString(R.string.ctb_scanty_3_24) :
                                    (obs[0][1].equals("ONE PLUS") ? getResources().getString(R.string.ctb_1_positive) :
                                            (obs[0][1].equals("TWO PLUS") ? getResources().getString(R.string.ctb_2_positive) :
                                                    getResources().getString(R.string.ctb_3_positive))));
                    if(value.equalsIgnoreCase(getResources().getString(R.string.ctb_scanty_3_24))){
                        afbSeenOneField.setVisibility(View.VISIBLE);
                    }
                    smearResult.getSpinner().selectValue(value);

                } else if (obs[0][0].equals("AFB COUNT")) {
                    afbSeenOneField.getEditText().setText(obs[0][1]);
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
        if (view == dateSubmission.getButton()) {
            dateSubmission.getButton().setEnabled(false);
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
        MySpinner spinner = (MySpinner) parent;
        if (spinner == specimenComeFrom.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                otherSpecimentComeFrom.setVisibility(View.VISIBLE);
            } else {
                otherSpecimentComeFrom.setVisibility(View.GONE);
            }
        }
        if (spinner == smearResult.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_scanty_3_24))) {
                String value = parent.getItemAtPosition(position).toString();
                afbSeenOneField.setVisibility(View.VISIBLE);
            } else {
                afbSeenOneField.setVisibility(View.GONE);
            }
        }
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

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        dateSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        formDate.setVisibility(View.GONE);
        orderId.getEditText().setKeyListener(null);
        formType.getRadioGroup().getButtons().get(0).setEnabled(true);
        formType.getRadioGroup().getButtons().get(1).setEnabled(true);
        orderId.setVisibility(View.GONE);

        goneVisibility();
        submitButton.setEnabled(false);

        String[] testIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "AFB Smear Test Order", "ORDER ID");
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == formType.getRadioGroup()) {
            if (group == formType.getRadioGroup()) {
                formDate.setVisibility(View.VISIBLE);
                submitButton.setEnabled(true);
                showTestOrderOrTestResult();
            }
        }

        if (group == specimenType.getRadioGroup()) {
            if (specimenType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_extra_pulmonary))) {
                specimenComeFrom.setVisibility(View.VISIBLE);
                if(App.get(specimenComeFrom).equals(getResources().getString(R.string.ctb_other_title))){
                    otherSpecimentComeFrom.setVisibility(View.VISIBLE);
                }
            } else {
                specimenComeFrom.setVisibility(View.GONE);
                otherSpecimentComeFrom.setVisibility(View.GONE);
            }
        }
        if (group == pointTestBeingDone.getRadioGroup()) {
            if (pointTestBeingDone.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_followup))) {
                monthTreatment.setVisibility(View.VISIBLE);

            } else {
                monthTreatment.setVisibility(View.GONE);
            }
        }

    }

    void goneVisibility() {
        dateSubmission.setVisibility(View.GONE);
        pointTestBeingDone.setVisibility(View.GONE);
        monthTreatment.setVisibility(View.GONE);
        specimenType.setVisibility(View.GONE);
        specimenComeFrom.setVisibility(View.GONE);
        otherSpecimentComeFrom.setVisibility(View.GONE);

        smearResult.setVisibility(View.GONE);
        afbSeenOneField.setVisibility(View.GONE);

        orderIds.setVisibility(View.GONE);
        orderId.setVisibility(View.GONE);
        testId.setVisibility(View.GONE);

    }
    void showTestOrderOrTestResult() {
        if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.ctb_order))) {
            formDate.setVisibility(View.VISIBLE);
            dateSubmission.setVisibility(View.VISIBLE);
            pointTestBeingDone.setVisibility(View.VISIBLE);
            if(App.get(pointTestBeingDone).equals(getResources().getString(R.string.ctb_followup))){
                monthTreatment.setVisibility(View.VISIBLE);
            }
            specimenType.setVisibility(View.VISIBLE);
            if(App.get(specimenType).equals(getResources().getString(R.string.ctb_extra_pulmonary))){
                specimenComeFrom.setVisibility(View.VISIBLE);
                if(App.get(specimenComeFrom).equals(getResources().getString(R.string.ctb_other_title))){
                    otherSpecimentComeFrom.setVisibility(View.VISIBLE);
                }
            }
            orderId.setVisibility(View.VISIBLE);
            Date nowDate = new Date();
            orderId.getEditText().setText(App.getSqlDateTime(nowDate));

            testId.setVisibility(View.GONE);
            smearResult.setVisibility(View.GONE);
            afbSeenOneField.setVisibility(View.GONE);
            orderIds.setVisibility(View.GONE);

        } else if(formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.ctb_result))){
            dateSubmission.setVisibility(View.GONE);
            pointTestBeingDone.setVisibility(View.GONE);
            monthTreatment.setVisibility(View.GONE);
            specimenType.setVisibility(View.GONE);
            specimenComeFrom.setVisibility(View.GONE);
            otherSpecimentComeFrom.setVisibility(View.GONE);
            orderId.setVisibility(View.GONE);


            testId.setVisibility(View.VISIBLE);
            formDate.setVisibility(View.VISIBLE);
            smearResult.setVisibility(View.VISIBLE);
            orderIds.setVisibility(View.VISIBLE);
            if(App.get(smearResult).equals(getResources().getString(R.string.ctb_scanty_3_24))){
                afbSeenOneField.setVisibility(View.VISIBLE);
            }
            smearResult.getSpinner().selectDefaultValue();


            String[] testIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "AFB Smear Test Order", "ORDER ID");
            if(testIds == null || testIds.length == 0){
                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage(getResources().getString(R.string.ctb_no_afb_order_found));
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
