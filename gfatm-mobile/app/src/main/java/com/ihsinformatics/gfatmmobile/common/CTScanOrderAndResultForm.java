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
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
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

    Snackbar snackbar;
    ScrollView scrollView;
    private TitledEditText ctScanSiteOther;
    private TitledEditText notes;

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
        orderId = new TitledEditText(context, getResources().getString(R.string.ctb_ct_scan_order), getResources().getString(R.string.order_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "ORDER ID");
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_type_of_form), getResources().getStringArray(R.array.ctb_type_of_form_list), null, App.HORIZONTAL, App.VERTICAL, true);
        ctScanSite = new TitledSpinner(context, null, getResources().getString(R.string.ctb_ct_scan_site), getResources().getStringArray(R.array.ctb_ct_scan_site_list), null, App.VERTICAL, false, "CT SCAN SITE", new String[]{"CT SCAN, CHEST", "COMPUTED TOMOGRAPHY OF ABDOMEN WITH CONTRAST", "BONE SCAN", "SPINE CT SCAN", "BRAIN CT SCAN", "OTHER TEST SITE"});
        ctScanSiteOther = new TitledEditText(context, null, getResources().getString(R.string.specify_other), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "OTHER TEST SITE");

        monthTreatment = new TitledSpinner(context, null, getResources().getString(R.string.ctb_month_treatment), getResources().getStringArray(R.array.ctb_0_to_24), null, App.HORIZONTAL, false, "FOLLOW-UP MONTH", getResources().getStringArray(R.array.ctb_0_to_24));
        updateFollowUpMonth();


        orderIds = new TitledSpinner(context, getResources().getString(R.string.ctb_ct_scan_results), getResources().getString(R.string.order_id), getResources().getStringArray(R.array.pet_empty_array), "", App.HORIZONTAL);
        testId = new TitledEditText(context, null, getResources().getString(R.string.ctb_test_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "TEST ID");
        ctChestTbSuggestive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_chest_suggestive_tb), getResources().getStringArray(R.array.ctb_ct_chest_suggestive_tb_list), getResources().getString(R.string.ctb_adenopathy), App.VERTICAL, App.VERTICAL, true, "CT CHEST SUGGESTIVE OF TB", new String[]{"CONSOLIDATION", "ADENOPATHY", "PLEURAL EFFUSION", "MILIARY TUBERCULOSIS"});
        ctChestInterpretation = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_chest_interpretation), getResources().getStringArray(R.array.suggestive_not_sugg_normal), null, App.VERTICAL, App.VERTICAL, true, "CT CHEST INTERPRETATION", new String[]{"NORMAL", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"}); //new String[]{"NORMAL", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"}
        ctAbdomenTbSuggestive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_abdomen_suggestive_tb), getResources().getStringArray(R.array.ctb_ct_abdomen_suggestive_tb_list), getResources().getString(R.string.ctb_adenopathy), App.VERTICAL, App.VERTICAL, true, "CT ABDOMEN SUGGESTIVE OF TB", new String[]{"ADENOPATHY", "INTESTINAL WALL THICKENING", "ASCITES"});
        ctAbdomenInterpretation = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_abdomen_interpretation), getResources().getStringArray(R.array.suggestive_not_sugg_normal), null, App.VERTICAL, App.VERTICAL, true, "CT ABDOMEN INTERPRETATION", new String[]{"NORMAL", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        ctBrainTbSuggestive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_brain_suggestive_tb), getResources().getStringArray(R.array.ctb_meningeal_tuberculomas), null, App.VERTICAL, App.VERTICAL, true, "CT BRAIN SUGGESTIVE OF TB", new String[]{"LEPTOMENINGEAL GLIONEURONAL HETEROTOPIA", "TUBERCULOMA BRAIN, UNSPEC"});
        ctBrainInterpretation = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_brain_interpretation), getResources().getStringArray(R.array.suggestive_not_sugg_normal), null, App.VERTICAL, App.VERTICAL, true, "CT BRAIN INTERPRETATION", new String[]{"NORMAL", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        ctBoneSTbSuggestive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_bone_suggestive_tb), getResources().getStringArray(R.array.suggestive_not_sugg_normal), null, App.VERTICAL, App.VERTICAL, true, "CT BONES INTERPRETATION", new String[]{"NORMAL", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        ctSpineTbSuggestive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_spine_suggestive_tb), getResources().getStringArray(R.array.suggestive_not_sugg_normal), null, App.VERTICAL, App.VERTICAL, true, "CT SPINE INTERPRETATION", new String[]{"NORMAL", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        ctScanOutcome = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_scan_outcome), getResources().getStringArray(R.array.suggestive_not_sugg_normal), null, App.VERTICAL, App.VERTICAL, true, "CT SCAN OUTCOME", new String[]{"SUGGESTIVE OF TB", "NO TB INDICATION"});
        notes = new TitledEditText(context, null, getResources().getString(R.string.common_doctor_notes), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "CLINICIAN NOTES (TEXT)");
        notes.getEditText().setSingleLine(false);
        notes.getEditText().setMinimumHeight(150);


        views = new View[]{formDate.getButton(), formType.getRadioGroup(), ctScanSite.getSpinner(), ctScanSiteOther.getEditText(), ctChestTbSuggestive.getRadioGroup(), ctChestInterpretation.getRadioGroup(),
                ctAbdomenTbSuggestive.getRadioGroup(), ctAbdomenInterpretation.getRadioGroup(), ctBrainTbSuggestive.getRadioGroup(),
                ctBrainInterpretation.getRadioGroup(), ctBoneSTbSuggestive.getRadioGroup(), ctSpineTbSuggestive.getRadioGroup(), testId.getEditText(),
                monthTreatment.getSpinner(), orderId.getEditText(), orderIds.getSpinner(), ctScanOutcome.getRadioGroup(), notes.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formType, formDate, orderId, ctScanSite, ctScanSiteOther, monthTreatment, orderIds, testId,
                        ctChestInterpretation,
                        ctChestTbSuggestive,
                        ctAbdomenInterpretation,
                        ctAbdomenTbSuggestive,
                        ctBrainInterpretation,
                        ctBrainTbSuggestive,
                        ctBoneSTbSuggestive,
                        ctSpineTbSuggestive, ctScanOutcome, notes}};

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
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
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
        boolean error = super.validate();
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


        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
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


        if (App.get(formType).equals(getResources().getString(R.string.ctb_result))) {
            observations.add(new String[]{"ORDER ID", App.get(orderIds)});

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
                        id = serverService.saveFormLocally("CT Scan Test Order", form, formDateCalendar, observations.toArray(new String[][]{}));

                    result = serverService.saveEncounterAndObservationTesting("CT Scan Test Order", form, formDateCalendar, observations.toArray(new String[][]{}), id);
                    if (!result.contains("SUCCESS"))
                        return result;


                    /*result = serverService.saveLabTestOrder("ct_scan", App.get(orderId), formDateCalendar, "CT Scan Test Order", id);
                    if (!result.contains("SUCCESS"))
                        return result;*/


                    return "SUCCESS";
                   /* result = serverService.saveEncounterAndObservation("CT Scan Test Order", form, formDateCalendar, observations.toArray(new String[][]{}),true);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";*/
                } else if (App.get(formType).equals(getResources().getString(R.string.ctb_result))) {
                    result = serverService.saveEncounterAndObservation("CT Scan Test Result", form, formDateCalendar, observations.toArray(new String[][]{}), false);
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
        super.refill(encounterId);

        OfflineForm fo = serverService.getSavedFormById(encounterId);
        ArrayList<String[][]> obsValue = fo.getObsValue();

        for (int i = 0; i < obsValue.size(); i++) {
            String[][] obs = obsValue.get(i);
            if (fo.getFormName().contains("Order")) {
                if (obs[0][0].equals("ORDER ID")) {
                    orderId.getEditText().setKeyListener(null);
                    orderId.getEditText().setText(obs[0][1]);
                }
                formType.getRadioGroup().getButtons().get(0).setChecked(true);
                formType.getRadioGroup().getButtons().get(1).setEnabled(false);

                submitButton.setEnabled(true);
            } else {
                formType.getRadioGroup().getButtons().get(1).setChecked(true);
                formType.getRadioGroup().getButtons().get(0).setEnabled(false);
                if (obs[0][0].equals("ORDER ID")) {
                    orderIds.getSpinner().selectValue(obs[0][1]);
                    orderIds.getSpinner().setClickable(false);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {

            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar, false, true, false);

            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");*/
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
                    if (App.get(ctChestInterpretation).equalsIgnoreCase(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
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
                    if (App.get(ctAbdomenInterpretation).equalsIgnoreCase(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
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
                    if (App.get(ctBrainInterpretation).equalsIgnoreCase(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
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
        } else if (spinner == ctScanSite.getSpinner()) {
            if (App.get(ctScanSite).equals(getResources().getString(R.string.ctb_other_title))) {
                ctScanSiteOther.setVisibility(View.VISIBLE);
            } else {
                ctScanSiteOther.setVisibility(View.GONE);
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

        String[] testIds = serverService.getAllObsValues(App.getPatientId(), "CT Scan Test Order", "ORDER ID");
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
        ctScanSiteOther.setVisibility(View.GONE);
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
            if (ctChestInterpretation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                ctScanOutcome.getRadioGroup().getButtons().get(1).setChecked(true);
                ctChestTbSuggestive.setVisibility(View.VISIBLE);
            } else {
                ctChestTbSuggestive.setVisibility(View.GONE);
                ctScanOutcome.getRadioGroup().getButtons().get(2).setChecked(true);
            }
        }
        if (group == ctAbdomenInterpretation.getRadioGroup()) {
            if (ctAbdomenInterpretation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                ctScanOutcome.getRadioGroup().getButtons().get(1).setChecked(true);
                ctAbdomenTbSuggestive.setVisibility(View.VISIBLE);
            } else {
                ctScanOutcome.getRadioGroup().getButtons().get(2).setChecked(true);
                ctAbdomenTbSuggestive.setVisibility(View.GONE);
            }
        }
        if (group == ctBrainInterpretation.getRadioGroup()) {
            if (ctBrainInterpretation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                ctScanOutcome.getRadioGroup().getButtons().get(1).setChecked(true);
                ctBrainTbSuggestive.setVisibility(View.VISIBLE);
            } else {
                ctScanOutcome.getRadioGroup().getButtons().get(2).setChecked(true);
                ctBrainTbSuggestive.setVisibility(View.GONE);
            }
        }
        if (group == ctBoneSTbSuggestive.getRadioGroup()) {
            if (ctBoneSTbSuggestive.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                ctScanOutcome.getRadioGroup().getButtons().get(1).setChecked(true);
            } else {
                ctScanOutcome.getRadioGroup().getButtons().get(2).setChecked(true);
            }
        }
        if (group == ctSpineTbSuggestive.getRadioGroup()) {
            if (ctSpineTbSuggestive.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                ctScanOutcome.getRadioGroup().getButtons().get(1).setChecked(true);
            } else {
                ctScanOutcome.getRadioGroup().getButtons().get(2).setChecked(true);
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

            String[] testIds = serverService.getAllObsValues(App.getPatientId(), "CT Scan Test Order", "ORDER ID");
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