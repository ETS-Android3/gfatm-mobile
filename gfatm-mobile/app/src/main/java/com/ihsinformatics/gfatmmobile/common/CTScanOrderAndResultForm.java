package com.ihsinformatics.gfatmmobile.common;

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

public class CTScanOrderAndResultForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    TitledButton formDate;
    TitledRadioGroup formType;
    TitledEditText orderId;
    TitledSpinner ctScanSite;
    TitledSpinner monthTreatment;


    TitledSpinner orderIds;
    TitledEditText testId;
    TitledRadioGroup ctChestTbSuggestive;
    TitledRadioGroup ctChestInterpretation;
    TitledRadioGroup ctAbdomenTbSuggestive;
    TitledRadioGroup ctAbdomenInterpretation;
    TitledRadioGroup ctBrainTbSuggestive;
    TitledRadioGroup ctBrainInterpretation;
    TitledRadioGroup ctBoneSTbSuggestive;
    TitledRadioGroup ctSpineTbSuggestive;
    TitledRadioGroup ctScanOutcome;
    TitledEditText clincianNote;


    Snackbar snackbar;
    ScrollView scrollView;

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
        formName = Forms.CT_SCAN_TEST;
        form = Forms.ct_scan_order_and_result;

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
            for (int i = 0; i < pageCount; i++) {
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
        orderId = new TitledEditText(context, getResources().getString(R.string.ctb_ct_scan_order), getResources().getString(R.string.order_id), "", "", 40, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_type_of_form), getResources().getStringArray(R.array.ctb_type_of_form_list), null, App.HORIZONTAL, App.VERTICAL, true);
        ctScanSite = new TitledSpinner(context, null, getResources().getString(R.string.ctb_ct_scan_site), getResources().getStringArray(R.array.ctb_ct_scan_site_list), null, App.VERTICAL);
        monthTreatment = new TitledSpinner(context, null, getResources().getString(R.string.ctb_month_treatment), getResources().getStringArray(R.array.ctb_0_to_24), null, App.HORIZONTAL);
        updateFollowUpMonth();


        orderIds = new TitledSpinner(context, getResources().getString(R.string.ctb_ct_scan_results), getResources().getString(R.string.order_id), getResources().getStringArray(R.array.pet_empty_array), "", App.HORIZONTAL);
        testId = new TitledEditText(context, null, getResources().getString(R.string.ctb_test_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        ctChestTbSuggestive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_chest_suggestive_tb), getResources().getStringArray(R.array.ctb_ct_chest_suggestive_tb_list), getResources().getString(R.string.ctb_adenopathy), App.VERTICAL, App.VERTICAL, true);
        ctChestInterpretation = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_chest_interpretation), getResources().getStringArray(R.array.ctb_suggestive_not_suggestive), null, App.VERTICAL, App.VERTICAL, true);
        ctAbdomenTbSuggestive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_abdomen_suggestive_tb), getResources().getStringArray(R.array.ctb_ct_abdomen_suggestive_tb_list), getResources().getString(R.string.ctb_adenopathy), App.VERTICAL, App.VERTICAL, true);
        ctAbdomenInterpretation = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_abdomen_interpretation), getResources().getStringArray(R.array.ctb_suggestive_not_suggestive), null, App.VERTICAL, App.VERTICAL, true);
        ctBrainTbSuggestive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_brain_suggestive_tb), getResources().getStringArray(R.array.ctb_meningeal_tuberculomas), null, App.VERTICAL, App.VERTICAL, true);
        ctBrainInterpretation = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_brain_interpretation), getResources().getStringArray(R.array.ctb_suggestive_not_suggestive), null, App.VERTICAL, App.VERTICAL, true);
        ctBoneSTbSuggestive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_bone_suggestive_tb), getResources().getStringArray(R.array.ctb_suggestive_not_suggestive), null, App.VERTICAL, App.VERTICAL, true);
        ctSpineTbSuggestive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_spine_suggestive_tb), getResources().getStringArray(R.array.ctb_suggestive_not_suggestive), null, App.VERTICAL, App.VERTICAL, true);
        ctScanOutcome = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_scan_outcome), getResources().getStringArray(R.array.ctb_ct_scan_outcome), null, App.VERTICAL, App.VERTICAL, true);
        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.ctscan_notes), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        clincianNote.getEditText().setSingleLine(false);
        clincianNote.getEditText().setMinimumHeight(150);

        views = new View[]{formDate.getButton(), formType.getRadioGroup(), ctScanSite.getSpinner(), ctChestTbSuggestive.getRadioGroup(), ctChestInterpretation.getRadioGroup(),
                ctAbdomenTbSuggestive.getRadioGroup(), ctAbdomenInterpretation.getRadioGroup(), ctBrainTbSuggestive.getRadioGroup(),
                ctBrainInterpretation.getRadioGroup(), ctBoneSTbSuggestive.getRadioGroup(), ctSpineTbSuggestive.getRadioGroup(), testId.getEditText(),
                monthTreatment.getSpinner(), orderId.getEditText(), orderIds.getSpinner(), ctScanOutcome.getRadioGroup(), clincianNote.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formType, formDate, orderId, ctScanSite, monthTreatment, orderIds, testId,
                        ctChestInterpretation,
                        ctChestTbSuggestive,
                        ctAbdomenInterpretation,
                        ctAbdomenTbSuggestive,
                        ctBrainInterpretation,
                        ctBrainTbSuggestive,
                        ctBoneSTbSuggestive,
                        ctSpineTbSuggestive, ctScanOutcome, clincianNote}};

        formDate.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        ctScanSite.getSpinner().setOnItemSelectedListener(this);
        monthTreatment.getSpinner().setOnItemSelectedListener(this);
        ctChestTbSuggestive.getRadioGroup().setOnCheckedChangeListener(this);
        ctChestInterpretation.getRadioGroup().setOnCheckedChangeListener(this);
        ctAbdomenTbSuggestive.getRadioGroup().setOnCheckedChangeListener(this);
        ctAbdomenInterpretation.getRadioGroup().setOnCheckedChangeListener(this);
        ctBrainTbSuggestive.getRadioGroup().setOnCheckedChangeListener(this);
        ctBrainInterpretation.getRadioGroup().setOnCheckedChangeListener(this);
        ctBoneSTbSuggestive.getRadioGroup().setOnCheckedChangeListener(this);
        ctSpineTbSuggestive.getRadioGroup().setOnCheckedChangeListener(this);
        orderIds.getSpinner().setOnItemSelectedListener(this);
        ctScanOutcome.getRadioGroup().setOnCheckedChangeListener(this);

        resetViews();
    }

    public void updateFollowUpMonth() {

        String treatmentDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST" + "-" + "Treatment Initiation");
        if (treatmentDate == null)
            treatmentDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB" + "-" + "TB Treatment Initiation");
        if (treatmentDate == null)
            treatmentDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "PET" + "-" + "Treatment Initiation");

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
                    monthArray[i] = String.valueOf(i + 1);
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
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else {
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

                if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.ctb_result))) {

                    if (!App.get(orderIds).equals("")) {
                        String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), "CT Scan Test Order", "ORDER ID", App.get(orderIds));

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

                    String treatmentDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST" + "-" + "Treatment Initiation");
                    if (treatmentDate == null)
                        treatmentDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB" + "-" + "Treatment Initiation");
                    if (treatmentDate == null)
                        treatmentDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "PET" + "-" + "Treatment Initiation");

                    if (treatmentDate != null) {
                        treatDateCalender = App.getCalendar(App.stringToDate(treatmentDate, "yyyy-MM-dd"));
                        if (formDateCalendar.before(treatDateCalender)) {
                            formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                            snackbar = Snackbar.make(mainContent, getResources().getString(R.string.ctb_form_date_less_than_treatment_initiation), Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();

                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        } else {
                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        }
                    }

                }
            }

        } else {
            String formDa = formDate.getButton().getText().toString();

            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.ctb_result))) {

                if (!App.get(orderIds).equals("")) {
                    String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), "CT Scan Test Order", "ORDER ID", App.get(orderIds));

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

        updateFollowUpMonth();
        formDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        boolean error = false;
        Boolean formCheck = false;

        if (App.get(formType).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            formType.getRadioGroup().getButtons().get(1).setError(getString(R.string.empty_field));
            formType.getRadioGroup().requestFocus();
            error = true;
        } else {
            formType.getRadioGroup().getButtons().get(1).setError(null);
        }

        Boolean flag = true;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            if (saveFlag) {
                flag = false;
            } else {
                flag = true;
            }
        }


        if (orderIds.getVisibility() == View.VISIBLE && flag) {
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), "CT Scan Test Result", "ORDER ID");
            if (resultTestIds != null) {
                for (String id : resultTestIds) {

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

        if (testId.getVisibility() == View.VISIBLE && flag) {
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), "CT Scan Test Result", "TEST ID");
            if (resultTestIds != null) {
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

        if (ctChestTbSuggestive.getVisibility() == View.VISIBLE && App.get(ctChestTbSuggestive).isEmpty()) {
            ctChestTbSuggestive.getQuestionView().setError(getString(R.string.empty_field));
            ctChestTbSuggestive.getQuestionView().requestFocus();
            error = true;
        } else ctChestTbSuggestive.getQuestionView().setError(null);

        if (ctChestInterpretation.getVisibility() == View.VISIBLE && App.get(ctChestInterpretation).isEmpty()) {
            ctChestInterpretation.getQuestionView().setError(getString(R.string.empty_field));
            ctChestInterpretation.getQuestionView().requestFocus();
            error = true;
        } else ctChestInterpretation.getQuestionView().setError(null);

        if (ctAbdomenTbSuggestive.getVisibility() == View.VISIBLE && App.get(ctAbdomenTbSuggestive).isEmpty()) {
            ctAbdomenTbSuggestive.getQuestionView().setError(getString(R.string.empty_field));
            ctAbdomenTbSuggestive.getQuestionView().requestFocus();
            error = true;
        } else ctAbdomenTbSuggestive.getQuestionView().setError(null);

        if (ctAbdomenInterpretation.getVisibility() == View.VISIBLE && App.get(ctAbdomenInterpretation).isEmpty()) {
            ctAbdomenInterpretation.getQuestionView().setError(getString(R.string.empty_field));
            ctAbdomenInterpretation.getQuestionView().requestFocus();
            error = true;
        } else ctAbdomenInterpretation.getQuestionView().setError(null);

        if (ctBrainTbSuggestive.getVisibility() == View.VISIBLE && App.get(ctBrainTbSuggestive).isEmpty()) {
            ctBrainTbSuggestive.getQuestionView().setError(getString(R.string.empty_field));
            ctBrainTbSuggestive.getQuestionView().requestFocus();
            error = true;
        } else ctBrainTbSuggestive.getQuestionView().setError(null);

        if (ctBrainInterpretation.getVisibility() == View.VISIBLE && App.get(ctBrainInterpretation).isEmpty()) {
            ctBrainInterpretation.getQuestionView().setError(getString(R.string.empty_field));
            ctBrainInterpretation.getQuestionView().requestFocus();
            error = true;
        } else ctBrainInterpretation.getQuestionView().setError(null);

        if (ctBoneSTbSuggestive.getVisibility() == View.VISIBLE && App.get(ctBoneSTbSuggestive).isEmpty()) {
            ctBoneSTbSuggestive.getQuestionView().setError(getString(R.string.empty_field));
            ctBoneSTbSuggestive.getQuestionView().requestFocus();
            error = true;
        } else ctBoneSTbSuggestive.getQuestionView().setError(null);

        if (ctSpineTbSuggestive.getVisibility() == View.VISIBLE && App.get(ctSpineTbSuggestive).isEmpty()) {
            ctSpineTbSuggestive.getQuestionView().setError(getString(R.string.empty_field));
            ctSpineTbSuggestive.getQuestionView().requestFocus();
            error = true;
        } else ctSpineTbSuggestive.getQuestionView().setError(null);

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

        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});
        if (App.get(formType).equals(getResources().getString(R.string.ctb_order))) {
            observations.add(new String[]{"ORDER ID", App.get(orderId)});
            observations.add(new String[]{"CT SCAN SITE", App.get(ctScanSite).equals(getResources().getString(R.string.ctb_chest_name)) ? "CT SCAN, CHEST" :
                    (App.get(ctScanSite).equals(getResources().getString(R.string.ctb_abdomen)) ? "COMPUTED TOMOGRAPHY OF ABDOMEN WITH CONTRAST" :
                            (App.get(ctScanSite).equals(getResources().getString(R.string.ctb_joint_bone)) ? "BONE SCAN" :
                                    (App.get(ctScanSite).equals(getResources().getString(R.string.ctb_brain)) ? "BRAIN CT SCAN" :
                                            (App.get(ctScanSite).equals(getResources().getString(R.string.ctb_spine_name)) ? "SPINE CT SCAN" :
                                                    "OTHER TEST SITE"))))});
            observations.add(new String[]{"FOLLOW-UP MONTH", App.get(monthTreatment)});

        } else if (App.get(formType).equals(getResources().getString(R.string.ctb_result))) {
            /*observations.add(new String[]{"ORDER ID", App.get(orderIds)});
            if(!App.get(testId).isEmpty()) {
                observations.add(new String[]{"TEST ID", App.get(testId)});
            }*/

            observations.clear();

            if (ctChestTbSuggestive.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"CT Chest Suggestive of TB", App.get(ctChestTbSuggestive).equals(getResources().getString(R.string.ctb_consolidation)) ? "CONSOLIDATION" :
                        (App.get(ctChestTbSuggestive).equals(getResources().getString(R.string.ctb_adenopathy)) ? "ADENOPATHY" :
                                (App.get(ctChestTbSuggestive).equals(getResources().getString(R.string.ctb_pleural_effusion)) ? "PLEURAL EFFUSION" :
                                        "MILIARY TUBERCULOSIS"))});
            }

            if (ctChestInterpretation.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"CT Chest Interpretation", App.get(ctChestInterpretation).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                        (App.get(ctChestInterpretation).equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) ? "NOT SUGGESTIVE OF TB" : "NORMAL")});
            }

            if (ctAbdomenTbSuggestive.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"CT Abdomen Suggestive of TB", App.get(ctAbdomenTbSuggestive).equals(getResources().getString(R.string.ctb_adenopathy)) ? "ADENOPATHY" :
                        (App.get(ctAbdomenTbSuggestive).equals(getResources().getString(R.string.ctb_intestinal_wall_thickening)) ? "INTESTINAL WALL THICKENING" :
                                "ASCITES")});
            }

            if (ctAbdomenInterpretation.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"CT Abdomen Interpretation", App.get(ctAbdomenInterpretation).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                        (App.get(ctAbdomenInterpretation).equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) ? "NOT SUGGESTIVE OF TB" : "NORMAL")});
            }

            if (ctBrainTbSuggestive.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"CT Brain Suggestive of TB", App.get(ctBrainTbSuggestive).equals(getResources().getString(R.string.ctb_meningeal_enhancement)) ? "LEPTOMENINGEAL GLIONEURONAL HETEROTOPIA" :
                        "TUBERCULOMA BRAIN, UNSPEC"});
            }

            if (ctBrainInterpretation.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"CT Brain Interpretation", App.get(ctBrainInterpretation).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                        (App.get(ctBrainInterpretation).equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) ? "NOT SUGGESTIVE OF TB" : "NORMAL")});

            }

            if (ctBoneSTbSuggestive.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"CT Bone Suggestive of TB", App.get(ctBoneSTbSuggestive).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                        (App.get(ctBoneSTbSuggestive).equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) ? "NOT SUGGESTIVE OF TB" : "NORMAL")});

            }

            if (ctSpineTbSuggestive.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"CT Spine Suggestive of TB", App.get(ctSpineTbSuggestive).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                        (App.get(ctBoneSTbSuggestive).equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) ? "NOT SUGGESTIVE OF TB" : "NORMAL")});

            }

            if (ctScanOutcome.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"CT Scan Outcome", App.get(ctScanOutcome).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                        "NO TB INDICATION"});
            }

            if (clincianNote.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"Notes", App.get(clincianNote)});

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

                if (App.get(formType).equals(getResources().getString(R.string.ctb_order))) {
                    String id = null;
                    if (App.getMode().equalsIgnoreCase("OFFLINE"))
                        id = serverService.saveFormLocallyTesting("CT Scan Test Order", form, formDateCalendar, observations.toArray(new String[][]{}));

                    result = serverService.saveEncounterAndObservationTesting("CT Scan Test Order", form, formDateCalendar, observations.toArray(new String[][]{}), id);
                    if (!result.contains("SUCCESS"))
                        return result;

                    String uuidEncounter = result.split("_")[1];

                    result = serverService.saveLabTestOrder(uuidEncounter,"ct_scan", App.get(orderId), formDateCalendar, id, null, null);
                    if (!result.contains("SUCCESS"))
                        return result;

                    return "SUCCESS";

                } else if (App.get(formType).equals(getResources().getString(R.string.ctb_result))) {
                   /* result = serverService.saveEncounterAndObservation("CT Scan Test Result", form, formDateCalendar, observations.toArray(new String[][]{}),false);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";*/

                    String id = null;
                    if (App.getMode().equalsIgnoreCase("OFFLINE"))
                        id = serverService.saveFormLocallyTesting("CT Scan Test Result", form, formDateCalendar, observations.toArray(new String[][]{}));

                    //String orderUuid = serverService.getObsValueByObs(App.getPatientId(), "CT Scan Test Order", "ORDER ID", App.get(orderIds), "LAB ORDER UUID");

                    String orderUuid = serverService.getOrderUuidByLabTestId(App.getPatientId(), "CT Scan", App.get(orderIds));

                    result = serverService.saveLabTestResult( "ct_scan", App.get(orderIds), orderUuid, observations.toArray(new String[][]{}), id);
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

        return true;
    }

    @Override
    public void refill(int encounterId) {
        OfflineForm fo = serverService.getSavedFormById(encounterId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();

        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());


        for (int i = 0; i < obsValue.size(); i++) {
            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (fo.getFormName().contains("Order")) {
                if (obs[0][0].equals("ORDER ID")) {
                    orderId.getEditText().setKeyListener(null);
                    orderId.getEditText().setText(obs[0][1]);
                }
                formType.getRadioGroup().getButtons().get(0).setChecked(true);
                formType.getRadioGroup().getButtons().get(1).setEnabled(false);
                if (obs[0][0].equals("CT SCAN SITE")) {
                    String value = obs[0][1].equals("CT SCAN, CHEST") ? getResources().getString(R.string.ctb_chest_name) :
                            (obs[0][1].equals("COMPUTED TOMOGRAPHY OF ABDOMEN WITH CONTRAST") ? getResources().getString(R.string.ctb_abdomen) :
                                    (obs[0][1].equals("BONE SCAN") ? getResources().getString(R.string.ctb_joint_bone) :
                                            (obs[0][1].equals("BRAIN CT SCAN") ? getResources().getString(R.string.ctb_brain) :
                                                    (obs[0][1].equals("SPINE CT SCAN") ? getResources().getString(R.string.ctb_spine_name) :
                                                            getResources().getString(R.string.ctb_other_title)))));
                    ctScanSite.getSpinner().selectValue(value);
                } else if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                    monthTreatment.getSpinner().selectValue(obs[0][1]);
                }
                submitButton.setEnabled(true);
            } else {
                formType.getRadioGroup().getButtons().get(1).setChecked(true);
                formType.getRadioGroup().getButtons().get(0).setEnabled(false);
               /* if (obs[0][0].equals("ORDER ID")) {
                    orderIds.getSpinner().selectValue(obs[0][1]);
                    orderIds.getSpinner().setClickable(false);
                } else if (obs[0][0].equals("TEST ID")) {
                    testId.getEditText().setText(obs[0][1]);
                } else */if (obs[0][0].equals("CT Chest Suggestive of TB")) {
                    for (RadioButton rb : ctChestTbSuggestive.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_consolidation)) && obs[0][1].equals("CONSOLIDATION")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_adenopathy)) && obs[0][1].equals("ADENOPATHY")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_pleural_effusion)) && obs[0][1].equals("PLEURAL EFFUSION")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_miliary)) && obs[0][1].equals("MILIARY TUBERCULOSIS")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    ctChestTbSuggestive.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("CT Chest Interpretation")) {
                    for (RadioButton rb : ctChestInterpretation.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) && obs[0][1].equals("NO TB INDICATION")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_normal)) && obs[0][1].equals("NORMAL")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    ctChestInterpretation.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("CT Abdomen Suggestive of TB")) {

                    for (RadioButton rb : ctAbdomenTbSuggestive.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_adenopathy)) && obs[0][1].equals("ADENOPATHY")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_intestinal_wall_thickening)) && obs[0][1].equals("INTESTINAL WALL THICKENING")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_ascites)) && obs[0][1].equals("ASCITES")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    ctAbdomenTbSuggestive.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("CT Abdomen Interpretation")) {
                    for (RadioButton rb : ctAbdomenInterpretation.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) && obs[0][1].equals("NO TB INDICATION")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_normal)) && obs[0][1].equals("NORMAL")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    ctAbdomenInterpretation.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("CT Brain Suggestive of TB")) {
                    for (RadioButton rb : ctBrainTbSuggestive.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_meningeal_enhancement)) && obs[0][1].equals("LEPTOMENINGEAL GLIONEURONAL HETEROTOPIA")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_tuberculomas)) && obs[0][1].equals("TUBERCULOMA BRAIN, UNSPEC")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    ctBrainTbSuggestive.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("CT Brain Interpretation")) {
                    for (RadioButton rb : ctBrainInterpretation.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) && obs[0][1].equals("NO TB INDICATION")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_normal)) && obs[0][1].equals("NORMAL")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    ctBrainInterpretation.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("CT Bone Suggestive of TB")) {
                    for (RadioButton rb : ctBoneSTbSuggestive.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) && obs[0][1].equals("NO TB INDICATION")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_normal)) && obs[0][1].equals("NORMAL")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    ctBoneSTbSuggestive.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("CT Spine Suggestive of TB")) {
                    for (RadioButton rb : ctSpineTbSuggestive.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) && obs[0][1].equals("NO TB INDICATION")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_normal)) && obs[0][1].equals("NORMAL")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    ctSpineTbSuggestive.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("CT Scan Outcome")) {
                    for (RadioButton rb : ctScanOutcome.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) && obs[0][1].equals("NO TB INDICATION")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    ctScanOutcome.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("Notes")) {
                    clincianNote.getEditText().setText(obs[0][1]);
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
            String ctScan = null;
            if (orderIds.getSpinner().getCount() > 0) {
                ctScan = serverService.getObsValueByObs(App.getPatientId(), "CT Scan Test Order", "ORDER ID", App.get(orderIds), "CT SCAN SITE");
            }
            if (ctScan != null) {
                if (ctScan.equalsIgnoreCase("CT SCAN, CHEST")) {
                    ctChestInterpretation.setVisibility(View.VISIBLE);
                    if (App.get(ctChestInterpretation).equalsIgnoreCase(getResources().getString(R.string.ctb_suggestive_tb))) {
                        ctChestTbSuggestive.setVisibility(View.VISIBLE);
                    }

                    ctAbdomenTbSuggestive.setVisibility(View.GONE);
                    ctAbdomenInterpretation.setVisibility(View.GONE);
                    ctBrainTbSuggestive.setVisibility(View.GONE);
                    ctBrainInterpretation.setVisibility(View.GONE);
                    ctBoneSTbSuggestive.setVisibility(View.GONE);
                    ctSpineTbSuggestive.setVisibility(View.GONE);
                } else if (ctScan.equalsIgnoreCase("COMPUTED TOMOGRAPHY OF ABDOMEN WITH CONTRAST")) {
                    ctAbdomenInterpretation.setVisibility(View.VISIBLE);
                    if (App.get(ctAbdomenInterpretation).equalsIgnoreCase(getResources().getString(R.string.ctb_suggestive_tb))) {
                        ctAbdomenTbSuggestive.setVisibility(View.VISIBLE);
                    }

                    ctChestTbSuggestive.setVisibility(View.GONE);
                    ctChestInterpretation.setVisibility(View.GONE);
                    ctBrainTbSuggestive.setVisibility(View.GONE);
                    ctBrainInterpretation.setVisibility(View.GONE);
                    ctBoneSTbSuggestive.setVisibility(View.GONE);
                    ctSpineTbSuggestive.setVisibility(View.GONE);
                } else if (ctScan.equalsIgnoreCase("BRAIN CT SCAN")) {
                    ctBrainInterpretation.setVisibility(View.VISIBLE);
                    if (App.get(ctBrainInterpretation).equalsIgnoreCase(getResources().getString(R.string.ctb_suggestive_tb))) {
                        ctBrainTbSuggestive.setVisibility(View.VISIBLE);
                    }


                    ctAbdomenTbSuggestive.setVisibility(View.GONE);
                    ctAbdomenInterpretation.setVisibility(View.GONE);
                    ctChestTbSuggestive.setVisibility(View.GONE);
                    ctChestInterpretation.setVisibility(View.GONE);
                    ctBoneSTbSuggestive.setVisibility(View.GONE);
                    ctSpineTbSuggestive.setVisibility(View.GONE);
                } else if (ctScan.equalsIgnoreCase("BONE SCAN")) {
                    ctBoneSTbSuggestive.setVisibility(View.VISIBLE);

                    ctAbdomenTbSuggestive.setVisibility(View.GONE);
                    ctAbdomenInterpretation.setVisibility(View.GONE);
                    ctChestTbSuggestive.setVisibility(View.GONE);
                    ctChestInterpretation.setVisibility(View.GONE);
                    ctBrainTbSuggestive.setVisibility(View.GONE);
                    ctBrainInterpretation.setVisibility(View.GONE);
                    ctSpineTbSuggestive.setVisibility(View.GONE);
                } else if (ctScan.equalsIgnoreCase("SPINE CT SCAN")) {
                    ctSpineTbSuggestive.setVisibility(View.VISIBLE);

                    ctAbdomenTbSuggestive.setVisibility(View.GONE);
                    ctAbdomenInterpretation.setVisibility(View.GONE);
                    ctChestTbSuggestive.setVisibility(View.GONE);
                    ctChestInterpretation.setVisibility(View.GONE);
                    ctBrainTbSuggestive.setVisibility(View.GONE);
                    ctBrainInterpretation.setVisibility(View.GONE);
                    ctBoneSTbSuggestive.setVisibility(View.GONE);
                } else {

                    ctSpineTbSuggestive.setVisibility(View.GONE);
                    ctAbdomenTbSuggestive.setVisibility(View.GONE);
                    ctAbdomenInterpretation.setVisibility(View.GONE);
                    ctChestTbSuggestive.setVisibility(View.GONE);
                    ctChestInterpretation.setVisibility(View.GONE);
                    ctBrainTbSuggestive.setVisibility(View.GONE);
                    ctBrainInterpretation.setVisibility(View.GONE);
                    ctBoneSTbSuggestive.setVisibility(View.GONE);
                }
            }
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
        formType.getRadioGroup().getButtons().get(0).setEnabled(true);
        formType.getRadioGroup().getButtons().get(1).setEnabled(true);
        formDate.setVisibility(View.GONE);
        testId.setVisibility(View.GONE);
        orderId.getEditText().setKeyListener(null);
        orderId.setVisibility(View.GONE);
        ctScanOutcome.getRadioGroup().setClickable(false);
        ctScanOutcome.getRadioGroup().setEnabled(false);
        goneVisibility();
        submitButton.setEnabled(false);

        String[] testIds = serverService.getAllTestsIds(App.getPatientId(), "CT Scan");
        if (testIds != null) {
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

    void goneVisibility() {
        ctScanSite.setVisibility(View.GONE);
        monthTreatment.setVisibility(View.GONE);
        ctChestTbSuggestive.setVisibility(View.GONE);
        ctChestInterpretation.setVisibility(View.GONE);
        ctAbdomenTbSuggestive.setVisibility(View.GONE);
        ctAbdomenInterpretation.setVisibility(View.GONE);
        ctBrainTbSuggestive.setVisibility(View.GONE);
        ctBrainInterpretation.setVisibility(View.GONE);
        ctBoneSTbSuggestive.setVisibility(View.GONE);
        ctSpineTbSuggestive.setVisibility(View.GONE);
        ctScanOutcome.setVisibility(View.GONE);
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
        if (group == ctChestInterpretation.getRadioGroup()) {
            if (ctChestInterpretation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_suggestive_tb))) {
                ctScanOutcome.getRadioGroup().getButtons().get(0).setChecked(true);
                ctChestTbSuggestive.setVisibility(View.VISIBLE);
            } else {
                ctChestTbSuggestive.setVisibility(View.GONE);
                ctScanOutcome.getRadioGroup().getButtons().get(1).setChecked(true);
            }
        }
        if (group == ctAbdomenInterpretation.getRadioGroup()) {
            if (ctAbdomenInterpretation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_suggestive_tb))) {
                ctScanOutcome.getRadioGroup().getButtons().get(0).setChecked(true);
                ctAbdomenTbSuggestive.setVisibility(View.VISIBLE);
            } else {
                ctScanOutcome.getRadioGroup().getButtons().get(1).setChecked(true);
                ctAbdomenTbSuggestive.setVisibility(View.GONE);
            }
        }
        if (group == ctBrainInterpretation.getRadioGroup()) {
            if (ctBrainInterpretation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_suggestive_tb))) {
                ctScanOutcome.getRadioGroup().getButtons().get(0).setChecked(true);
                ctBrainTbSuggestive.setVisibility(View.VISIBLE);
            } else {
                ctScanOutcome.getRadioGroup().getButtons().get(1).setChecked(true);
                ctBrainTbSuggestive.setVisibility(View.GONE);
            }
        }
        if (group == ctBoneSTbSuggestive.getRadioGroup()) {
            if (ctBoneSTbSuggestive.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_suggestive_tb))) {
                ctScanOutcome.getRadioGroup().getButtons().get(0).setChecked(true);
            } else {
                ctScanOutcome.getRadioGroup().getButtons().get(1).setChecked(true);
            }
        }
        if (group == ctSpineTbSuggestive.getRadioGroup()) {
            if (ctSpineTbSuggestive.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_suggestive_tb))) {
                ctScanOutcome.getRadioGroup().getButtons().get(0).setChecked(true);
            } else {
                ctScanOutcome.getRadioGroup().getButtons().get(1).setChecked(true);
            }
        }
    }


    void showTestOrderOrTestResult() {
        if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.ctb_order))) {

            formDate.setVisibility(View.VISIBLE);
            ctScanSite.setVisibility(View.VISIBLE);
            monthTreatment.setVisibility(View.VISIBLE);
            orderId.setVisibility(View.VISIBLE);
            Date nowDate = new Date();
            orderId.getEditText().setText(App.getSqlDateTime(nowDate));

            testId.setVisibility(View.GONE);
            orderIds.setVisibility(View.GONE);
            ctChestTbSuggestive.setVisibility(View.GONE);
            ctChestInterpretation.setVisibility(View.GONE);
            ctAbdomenTbSuggestive.setVisibility(View.GONE);
            ctAbdomenInterpretation.setVisibility(View.GONE);
            ctBrainTbSuggestive.setVisibility(View.GONE);
            ctBrainInterpretation.setVisibility(View.GONE);
            ctBoneSTbSuggestive.setVisibility(View.GONE);
            ctSpineTbSuggestive.setVisibility(View.GONE);
            ctScanOutcome.setVisibility(View.GONE);
        } else if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.ctb_result))) {
            testId.setVisibility(View.VISIBLE);
            formDate.setVisibility(View.VISIBLE);
            orderIds.setVisibility(View.VISIBLE);
            String ctScan = null;
            if (orderIds.getSpinner().getCount() > 0) {
                ctScan = serverService.getObsValueByObs(App.getPatientId(), "CT Scan Test Order", "ORDER ID", App.get(orderIds), "CT SCAN SITE");
            }
            if (ctScan != null) {
                if (ctScan.equalsIgnoreCase("CT SCAN, CHEST")) {
                    ctChestInterpretation.setVisibility(View.VISIBLE);
                } else if (ctScan.equalsIgnoreCase("COMPUTED TOMOGRAPHY OF ABDOMEN WITH CONTRAST")) {
                    ctAbdomenInterpretation.setVisibility(View.VISIBLE);
                } else if (ctScan.equalsIgnoreCase("BRAIN CT SCAN")) {
                    ctBrainInterpretation.setVisibility(View.VISIBLE);
                } else if (ctScan.equalsIgnoreCase("BONE SCAN")) {
                    ctBoneSTbSuggestive.setVisibility(View.VISIBLE);
                } else if (ctScan.equalsIgnoreCase("SPINE CT SCAN")) {
                    ctSpineTbSuggestive.setVisibility(View.VISIBLE);
                }
            }
            ctScanOutcome.setVisibility(View.VISIBLE);


            ctScanSite.setVisibility(View.GONE);
            monthTreatment.setVisibility(View.GONE);
            orderId.setVisibility(View.GONE);

            String[] testIds = serverService.getAllTestsIds(App.getPatientId(), "CT Scan");
            if (testIds == null || testIds.length == 0) {
                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage(getResources().getString(R.string.ctb_no_ct_scan_order_found));
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

            if (testIds != null) {
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
