package com.ihsinformatics.gfatmmobile.common;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
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

public class MRIScanOrderAndResultForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    private Context context;
    private TitledRadioGroup formType;
    protected TitledButton formDate;
    private TitledButton dateOfSubmission;
    private TitledEditText testId;
    private TitledRadioGroup mriScanSite;
    private TitledEditText otherMriScanSite;
    private TitledSpinner monthOfTreatment;
    private TitledEditText orderId;
    private TitledSpinner orderIds;
    private TitledEditText testIdResult;
    private TitledRadioGroup chestInterpretation;
    private TitledRadioGroup chestSuggestiveOfTB;
    private TitledRadioGroup abdomenInterPretation;
    private TitledRadioGroup abdomenSuggestiveOfTB;
    private TitledRadioGroup brainInterpretation;
    private TitledRadioGroup brainSuggestiveOfTB;
    private TitledRadioGroup boneSuggestiveOfTB;
    private TitledRadioGroup spineSuggestiveOfTB;
    private TitledRadioGroup scanOutcome;
    private TitledEditText notes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pageCount = 1;
        formName = Forms.AFB_SMEAR_ORDER_AND_RESULT_FORM;
        form = Forms.afbSmearOrderAndResultForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new MRIScanOrderAndResultForm.MyAdapter());
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

    @Override
    public void initViews() {
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_select_form_type), getResources().getStringArray(R.array.fast_order_and_result_list), "", App.HORIZONTAL, App.HORIZONTAL);

        testId = new TitledEditText(context, "MRI Order", getResources().getString(R.string.ctb_test_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "TEST ID");
        dateOfSubmission = new TitledButton(context, null, getResources().getString(R.string.ctb_test_order_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        mriScanSite = new TitledRadioGroup(context, null, getResources().getString(R.string.mri_scan_site), getResources().getStringArray(R.array.mri_scan_site_list), null, App.VERTICAL, App.VERTICAL, true, "MRI SCAN SITE", new String[]{"MRI SCAN, CHEST", "MRI SCAN, ABDOMEN", "BONE SCAN", "MRI SCAN, SPINE", "MRI SCAN, BRAIN", "OTHER TEST SITE"});
        otherMriScanSite = new TitledEditText(context, null, getResources().getString(R.string.specify_other), "", "", 256, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER TEST SITE");
        monthOfTreatment = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_month_of_treatment), getResources().getStringArray(R.array.fast_number_list), "", App.VERTICAL, true, "FOLLOW-UP MONTH", getResources().getStringArray(R.array.fast_number_list));
        updateFollowUpMonth();
        orderId = new TitledEditText(context, null, getResources().getString(R.string.order_id), "", "", 40, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "ORDER ID");


        orderIds = new TitledSpinner(context, "MRI Result", getResources().getString(R.string.order_id), getResources().getStringArray(R.array.pet_empty_array), "", App.HORIZONTAL);
        testIdResult = new TitledEditText(context, null, getResources().getString(R.string.ctb_test_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "TEST ID");
        chestInterpretation = new TitledRadioGroup(context, null, getResources().getString(R.string.mri_chest_interpretation), getResources().getStringArray(R.array.suggestive_not_sugg_normal), null, App.VERTICAL, App.VERTICAL, true, "MRI CHEST INTERPRETATION", new String[]{"NORMAL", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        chestSuggestiveOfTB = new TitledRadioGroup(context, null, getResources().getString(R.string.mri_chest_suggestive_tb), getResources().getStringArray(R.array.mri_chest_suggestive_tb_list), null, App.VERTICAL, App.VERTICAL, true, "MRI CHEST SUGGESTIVE OF TB", new String[]{"CONSOLIDATION", "ADENOPATHY", "PLEURAL EFFUSION", "MILIARY TUBERCULOSIS"});
        abdomenInterPretation = new TitledRadioGroup(context, null, getResources().getString(R.string.mri_chest_interpretation), getResources().getStringArray(R.array.suggestive_not_sugg_normal), null, App.VERTICAL, App.VERTICAL, true, "MRI ABDOMEN INTERPRETATION", new String[]{"NORMAL", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        abdomenSuggestiveOfTB = new TitledRadioGroup(context, null, getResources().getString(R.string.mri_abdomen_suggestive_tb), getResources().getStringArray(R.array.ctb_ct_abdomen_suggestive_tb_list), null, App.VERTICAL, App.VERTICAL, true, "MRI ABDOMEN SUGGESTIVE OF TB", new String[]{"ADENOPATHY", "INTESTINAL WALL THICKENING", "ASCITES"});
        brainInterpretation = new TitledRadioGroup(context, null, getResources().getString(R.string.mri_brain_interpretation), getResources().getStringArray(R.array.suggestive_not_sugg_normal), null, App.VERTICAL, App.VERTICAL, true, "MRI BRAIN INTERPRETATION", new String[]{"NORMAL", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        brainSuggestiveOfTB = new TitledRadioGroup(context, null, getResources().getString(R.string.mri_brain_suggestive_tb), getResources().getStringArray(R.array.ctb_meningeal_tuberculomas_other), null, App.VERTICAL, App.VERTICAL, true, "MRI BAIN SUGGESTIVE OF TB", new String[]{"LEPTOMENINGEAL GLIONEURONAL HETEROTOPIA", "TUBERCULOMA BRAIN, UNSPEC", "OTHER"});
        boneSuggestiveOfTB = new TitledRadioGroup(context, null, getResources().getString(R.string.mri_bone_suggestive_tb), getResources().getStringArray(R.array.suggestive_not_sugg_normal), null, App.VERTICAL, App.VERTICAL, true, "MRI BONE INTERPRETATION", new String[]{"NORMAL", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        spineSuggestiveOfTB = new TitledRadioGroup(context, null, getResources().getString(R.string.mri_spine_suggestive_tb), getResources().getStringArray(R.array.suggestive_not_sugg_normal), null, App.VERTICAL, App.VERTICAL, true, "MRI SPINE INTERPRETATION", new String[]{"NORMAL", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});
        scanOutcome = new TitledRadioGroup(context, null, getResources().getString(R.string.mri_scan_outcome), getResources().getStringArray(R.array.suggestive_not_sugg_normal), null, App.VERTICAL, App.VERTICAL, true, "MRI SCAN OUTCOME", new String[]{"NORMAL", "ABNORMAL SUGGESTIVE OF TB", "ABNORMAL NOT SUGGESTIVE OF TB"});

        notes = new TitledEditText(context, null, getResources().getString(R.string.common_doctor_notes), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "CLINICIAN NOTES (TEXT)");
        notes.getEditText().setSingleLine(false);
        notes.getEditText().setMinimumHeight(150);

        views = new View[]{formDate.getButton(), formType.getRadioGroup(), dateOfSubmission.getButton()
                , testId.getEditText(), mriScanSite.getRadioGroup(), otherMriScanSite.getEditText()
                , monthOfTreatment.getSpinner(), orderId.getEditText(), orderIds.getSpinner()
                , testIdResult.getEditText(), chestInterpretation.getRadioGroup(), chestSuggestiveOfTB.getRadioGroup(),
                abdomenInterPretation.getRadioGroup(),
                abdomenSuggestiveOfTB.getRadioGroup(), brainInterpretation.getRadioGroup(), brainSuggestiveOfTB.getRadioGroup()
                , boneSuggestiveOfTB.getRadioGroup(),
                spineSuggestiveOfTB.getRadioGroup(), scanOutcome.getRadioGroup(), notes.getEditText()

        };
        viewGroups = new View[][]
                {{formType, formDate, testId, dateOfSubmission, mriScanSite, otherMriScanSite, monthOfTreatment, orderId
                        , orderIds, testIdResult, chestInterpretation, chestSuggestiveOfTB, abdomenInterPretation,
                        abdomenSuggestiveOfTB, brainInterpretation, brainSuggestiveOfTB, boneSuggestiveOfTB,
                        spineSuggestiveOfTB, scanOutcome, notes
                }};

        formDate.getButton().setOnClickListener(this);
        dateOfSubmission.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        mriScanSite.getRadioGroup().setOnCheckedChangeListener(this);
        orderIds.getSpinner().setOnItemSelectedListener(this);

        chestInterpretation.getRadioGroup().setOnCheckedChangeListener(this);
        chestSuggestiveOfTB.getRadioGroup().setOnCheckedChangeListener(this);
        abdomenInterPretation.getRadioGroup().setOnCheckedChangeListener(this);
        abdomenSuggestiveOfTB.getRadioGroup().setOnCheckedChangeListener(this);
        abdomenSuggestiveOfTB.getRadioGroup().setOnCheckedChangeListener(this);
        brainInterpretation.getRadioGroup().setOnCheckedChangeListener(this);
        brainSuggestiveOfTB.getRadioGroup().setOnCheckedChangeListener(this);
        boneSuggestiveOfTB.getRadioGroup().setOnCheckedChangeListener(this);
        spineSuggestiveOfTB.getRadioGroup().setOnCheckedChangeListener(this);


        resetViews();

    }


    void showTestOrderOrTestResult() {
        //   formDate.setVisibility(View.VISIBLE);
        if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))) {
            dateOfSubmission.setVisibility(View.VISIBLE);

            mriScanSite.setVisibility(View.VISIBLE);
            monthOfTreatment.setVisibility(View.VISIBLE);
            mriScanSite.getRadioGroup().selectDefaultValue();
            orderId.setVisibility(View.VISIBLE);
            testId.setVisibility(View.VISIBLE);
            orderId.getEditText().setKeyListener(null);
            orderId.getEditText().setFocusable(false);

            testIdResult.setVisibility(View.GONE);
            orderIds.setVisibility(View.GONE);
            chestInterpretation.setVisibility(View.GONE);
            chestSuggestiveOfTB.setVisibility(View.GONE);
            abdomenInterPretation.setVisibility(View.GONE);
            abdomenSuggestiveOfTB.setVisibility(View.GONE);
            abdomenSuggestiveOfTB.setVisibility(View.GONE);
            brainInterpretation.setVisibility(View.GONE);
            brainSuggestiveOfTB.setVisibility(View.GONE);
            boneSuggestiveOfTB.setVisibility(View.GONE);
            spineSuggestiveOfTB.setVisibility(View.GONE);
            scanOutcome.setVisibility(View.GONE);
            notes.setVisibility(View.GONE);


        } else if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_result))) {
            dateOfSubmission.setVisibility(View.GONE);
            mriScanSite.setVisibility(View.GONE);
            monthOfTreatment.setVisibility(View.GONE);
            otherMriScanSite.setVisibility(View.GONE);
            testId.setVisibility(View.GONE);
            orderId.setVisibility(View.GONE);

            testIdResult.setVisibility(View.VISIBLE);
            orderIds.setVisibility(View.VISIBLE);
            chestInterpretation.setVisibility(View.VISIBLE);
            chestSuggestiveOfTB.setVisibility(View.VISIBLE);
            abdomenInterPretation.setVisibility(View.VISIBLE);
            abdomenSuggestiveOfTB.setVisibility(View.VISIBLE);
            abdomenSuggestiveOfTB.setVisibility(View.VISIBLE);
            brainInterpretation.setVisibility(View.VISIBLE);
            brainSuggestiveOfTB.setVisibility(View.VISIBLE);
            boneSuggestiveOfTB.setVisibility(View.VISIBLE);
            spineSuggestiveOfTB.setVisibility(View.VISIBLE);
            scanOutcome.setVisibility(View.VISIBLE);
            notes.setVisibility(View.VISIBLE);


            String[] testIds = serverService.getAllObsValues(App.getPatientId(), "MRI Scan Test Order", "ORDER ID"); /*FIXME add encounter type*/

            if (testIds == null || testIds.length == 0) {
                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage(getResources().getString(R.string.fast_no_order_found_for_the_patient));
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
                submitButton.setEnabled(false);
                return;
            }

            if (testIds != null) {
                orderIds.getSpinner().setSpinnerData(testIds);
            }
        }
    }


    @Override
    public void resetViews() {
        super.resetViews();
        formDateCalendar = Calendar.getInstance();
        secondDateCalendar = Calendar.getInstance();

        mriScanSite.setVisibility(View.GONE);
        otherMriScanSite.setVisibility(View.GONE);
        monthOfTreatment.setVisibility(View.GONE);
        orderId.setVisibility(View.GONE);
        testId.setVisibility(View.GONE);
        testIdResult.setVisibility(View.GONE);
        chestInterpretation.setVisibility(View.GONE);
        chestSuggestiveOfTB.setVisibility(View.GONE);
        abdomenInterPretation.setVisibility(View.GONE);
        abdomenSuggestiveOfTB.setVisibility(View.GONE);
        abdomenSuggestiveOfTB.setVisibility(View.GONE);
        brainInterpretation.setVisibility(View.GONE);
        brainSuggestiveOfTB.setVisibility(View.GONE);
        boneSuggestiveOfTB.setVisibility(View.GONE);
        spineSuggestiveOfTB.setVisibility(View.GONE);
        scanOutcome.setVisibility(View.GONE);
        notes.setVisibility(View.GONE);


        String[] testIds = serverService.getAllObsValues(App.getPatientId(), "MRI Scan Test Order", "ORDER ID");
        if (testIds != null) {
            orderIds.getSpinner().setSpinnerData(testIds);
        }

        dateOfSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());


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

                if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.fast_result))) {

                    if (!App.get(orderIds).equals("")) {
                        String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), "AFB Smear Test Order", "ORDER ID", App.get(orderIds));

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
        }
        if (!(dateOfSubmission.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = dateOfSubmission.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                dateOfSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                dateOfSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                dateOfSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }

        updateFollowUpMonth();
        formDate.getButton().setEnabled(true);
        dateOfSubmission.getButton().setEnabled(true);

    }


    @Override
    public boolean validate() {

        Boolean error = super.validate();
        Boolean formCheck = false;

        if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order)) || formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_result))) {

        } else {
            formCheck = true;
            error = true;
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
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), "AFB Smear Test Result", "ORDER ID");
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
        if (testIdResult.getVisibility() == View.VISIBLE && flag) {
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), "MRI Scan Test Result", "TEST ID");
            if (resultTestIds != null) {
                for (String id : resultTestIds) {
                    if (id.equals(App.get(testIdResult))) {
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


        return true;

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

    @Override
    public void refill(int encounterId) {
        super.refill(encounterId);
        OfflineForm fo = serverService.getSavedFormById(encounterId);
        ArrayList<String[][]> obsValue = fo.getObsValue();


        for (int i = 0; i < obsValue.size(); i++) {

            formDate.setVisibility(View.VISIBLE);
            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            }
            if (fo.getFormName().contains("Order")) {

                if (obs[0][0].equals("DATE TEST ORDERED")) {
                    String secondDate = obs[0][1];
                    secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                    dateOfSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                    dateOfSubmission.setVisibility(View.VISIBLE);
                }

                formType.getRadioGroup().getButtons().get(0).setChecked(true);
                formType.getRadioGroup().getButtons().get(1).setEnabled(false);

            } else {
                formType.getRadioGroup().getButtons().get(1).setChecked(true);
                formType.getRadioGroup().getButtons().get(0).setEnabled(false);

            }
        }
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


        if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))) {

            if (dateOfSubmission.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"DATE TEST ORDERED", App.getSqlDateTime(secondDateCalendar)});

        } else {

            observations.add(new String[]{"ORDER ID", App.get(orderIds)});

            if (formDate.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"DATE OF  TEST RESULT RECEIVED", App.getSqlDateTime(formDateCalendar)});
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

                if (App.get(formType).equals(getResources().getString(R.string.fast_order))) {

                    String id = null;
                    if (App.getMode().equalsIgnoreCase("OFFLINE"))
                        id = serverService.saveFormLocally("MRI Scan Test Order", form, formDateCalendar, observations.toArray(new String[][]{}));

                    result = serverService.saveEncounterAndObservationTesting("MRI Scan Test Order", form, formDateCalendar, observations.toArray(new String[][]{}), id);
                    if (!result.contains("SUCCESS"))
                        return result;

                    return "SUCCESS";

                } else if (App.get(formType).equals(getResources().getString(R.string.fast_result))) {

                    String id = null;
                    if (App.getMode().equalsIgnoreCase("OFFLINE"))
                        id = serverService.saveFormLocally("MRI Scan Test Result", form, formDateCalendar, observations.toArray(new String[][]{}));

                    result = serverService.saveEncounterAndObservationTesting("MRI Scan Test Result", form, formDateCalendar, observations.toArray(new String[][]{}), id);
                    if (!result.contains("SUCCESS"))
                        return result;

                    return "SUCCESS";
                }

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
        return false;
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

        MySpinner spinner = (MySpinner) parent;

        if (spinner == orderIds.getSpinner()) {
            updateDisplay();
            String mriScan = null;
            if (orderIds.getSpinner().getCount() > 0) {
                mriScan = serverService.getObsValueByObs(App.getPatientId(), "MRI Scan Test Order", "ORDER ID", App.get(orderIds), "MRI SCAN SITE");
            }
            if (mriScan != null) {
                if (mriScan.equalsIgnoreCase("MRI SCAN, CHEST")) {
                    chestInterpretation.setVisibility(View.VISIBLE);
                    if (App.get(chestInterpretation).equalsIgnoreCase(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                        chestSuggestiveOfTB.setVisibility(View.VISIBLE);
                    }

                    abdomenInterPretation.setVisibility(View.GONE);
                    abdomenSuggestiveOfTB.setVisibility(View.GONE);
                    abdomenSuggestiveOfTB.setVisibility(View.GONE);
                    brainInterpretation.setVisibility(View.GONE);
                    brainSuggestiveOfTB.setVisibility(View.GONE);
                    boneSuggestiveOfTB.setVisibility(View.GONE);
                    spineSuggestiveOfTB.setVisibility(View.GONE);
                } else if (mriScan.equalsIgnoreCase("MRI SCAN, ABDOMEN")) {
                    abdomenInterPretation.setVisibility(View.VISIBLE);
                    abdomenSuggestiveOfTB.setVisibility(View.GONE);

                    if (App.get(abdomenInterPretation).equalsIgnoreCase(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                        abdomenSuggestiveOfTB.setVisibility(View.VISIBLE);
                    }

                    chestInterpretation.setVisibility(View.GONE);
                    chestSuggestiveOfTB.setVisibility(View.GONE);
                    abdomenSuggestiveOfTB.setVisibility(View.GONE);
                    brainInterpretation.setVisibility(View.GONE);
                    brainSuggestiveOfTB.setVisibility(View.GONE);
                    boneSuggestiveOfTB.setVisibility(View.GONE);
                    spineSuggestiveOfTB.setVisibility(View.GONE);

                } else if (mriScan.equalsIgnoreCase("MRI SCAN, SPINE")) {
                    brainInterpretation.setVisibility(View.VISIBLE);
                    if (App.get(brainInterpretation).equalsIgnoreCase(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                        brainSuggestiveOfTB.setVisibility(View.VISIBLE);
                    }

                    chestInterpretation.setVisibility(View.GONE);
                    chestSuggestiveOfTB.setVisibility(View.GONE);
                    abdomenInterPretation.setVisibility(View.GONE);
                    abdomenSuggestiveOfTB.setVisibility(View.GONE);
                    abdomenSuggestiveOfTB.setVisibility(View.GONE);
                    boneSuggestiveOfTB.setVisibility(View.GONE);
                    spineSuggestiveOfTB.setVisibility(View.GONE);


                } else if (mriScan.equalsIgnoreCase("BONE SCAN")) {
                    chestInterpretation.setVisibility(View.GONE);
                    chestSuggestiveOfTB.setVisibility(View.GONE);
                    abdomenInterPretation.setVisibility(View.GONE);
                    abdomenSuggestiveOfTB.setVisibility(View.GONE);
                    abdomenSuggestiveOfTB.setVisibility(View.GONE);
                    brainInterpretation.setVisibility(View.GONE);
                    brainSuggestiveOfTB.setVisibility(View.GONE);
                    boneSuggestiveOfTB.setVisibility(View.VISIBLE);
                    spineSuggestiveOfTB.setVisibility(View.GONE);
                } else if (mriScan.equalsIgnoreCase("MRI SCAN, BRAIN")) {
                    chestInterpretation.setVisibility(View.GONE);
                    chestSuggestiveOfTB.setVisibility(View.GONE);
                    abdomenInterPretation.setVisibility(View.GONE);
                    abdomenSuggestiveOfTB.setVisibility(View.GONE);
                    abdomenSuggestiveOfTB.setVisibility(View.GONE);
                    brainInterpretation.setVisibility(View.GONE);
                    brainSuggestiveOfTB.setVisibility(View.GONE);
                    boneSuggestiveOfTB.setVisibility(View.GONE);
                    spineSuggestiveOfTB.setVisibility(View.VISIBLE);
                } else {

                    chestInterpretation.setVisibility(View.GONE);
                    chestSuggestiveOfTB.setVisibility(View.GONE);
                    abdomenInterPretation.setVisibility(View.GONE);
                    abdomenSuggestiveOfTB.setVisibility(View.GONE);
                    abdomenSuggestiveOfTB.setVisibility(View.GONE);
                    brainInterpretation.setVisibility(View.GONE);
                    brainSuggestiveOfTB.setVisibility(View.GONE);
                    boneSuggestiveOfTB.setVisibility(View.GONE);
                    spineSuggestiveOfTB.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == formType.getRadioGroup()) {

            submitButton.setEnabled(true);
            formDate.setVisibility(View.VISIBLE);
            showTestOrderOrTestResult();

        } else if (radioGroup == mriScanSite.getRadioGroup()) {
            if (App.get(mriScanSite).equals(getResources().getString(R.string.other))) {
                otherMriScanSite.setVisibility(View.VISIBLE);
            } else {
                otherMriScanSite.setVisibility(View.GONE);
            }
        }

        if (radioGroup == chestInterpretation.getRadioGroup()) {
            if (chestInterpretation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                mriScanSite.getRadioGroup().getButtons().get(1).setChecked(true);
                chestSuggestiveOfTB.setVisibility(View.VISIBLE);
            } else {
                chestSuggestiveOfTB.setVisibility(View.GONE);
                mriScanSite.getRadioGroup().getButtons().get(2).setChecked(true);
            }
        }

        if (radioGroup == abdomenInterPretation.getRadioGroup()) {
            if (abdomenInterPretation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                mriScanSite.getRadioGroup().getButtons().get(1).setChecked(true);
                abdomenSuggestiveOfTB.setVisibility(View.VISIBLE);
            } else {
                abdomenSuggestiveOfTB.setVisibility(View.GONE);
                mriScanSite.getRadioGroup().getButtons().get(2).setChecked(true);
            }
        }

        if (radioGroup == brainInterpretation.getRadioGroup()) {
            if (brainInterpretation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                mriScanSite.getRadioGroup().getButtons().get(1).setChecked(true);
                brainSuggestiveOfTB.setVisibility(View.VISIBLE);
            } else {
                brainSuggestiveOfTB.setVisibility(View.GONE);
                mriScanSite.getRadioGroup().getButtons().get(2).setChecked(true);
            }
        }
        if (radioGroup == boneSuggestiveOfTB.getRadioGroup()) {
            if (boneSuggestiveOfTB.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                mriScanSite.getRadioGroup().getButtons().get(1).setChecked(true);
            } else {
                mriScanSite.getRadioGroup().getButtons().get(2).setChecked(true);
            }
        }
        if (radioGroup == spineSuggestiveOfTB.getRadioGroup()) {
            if (spineSuggestiveOfTB.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_abnormal_suggestive_tb))) {
                mriScanSite.getRadioGroup().getButtons().get(1).setChecked(true);
            } else {
                mriScanSite.getRadioGroup().getButtons().get(2).setChecked(true);
            }
        }

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);

        if (view == formDate.getButton()) {

            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar, false, true, false);

        }
        if (view == dateOfSubmission.getButton()) {
            dateOfSubmission.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar, false, true, true);

        }
    }

    private class MyAdapter extends PagerAdapter {
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
