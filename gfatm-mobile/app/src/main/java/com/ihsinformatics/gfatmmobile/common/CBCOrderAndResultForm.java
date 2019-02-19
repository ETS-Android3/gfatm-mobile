package com.ihsinformatics.gfatmmobile.common;

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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
 * Created by Haris on 2/21/2017.
 */

public class CBCOrderAndResultForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    // Views...
    TitledRadioGroup formType;

    TitledButton formDate;

    //orderView
    TitledRadioGroup assessment_type;
    TitledEditText monthOfTreatment;
    TitledEditText orderId;
    TitledEditText doctor_notes;


    //resultView
    TitledSpinner orderIds;
    TitledEditText sampleId;
    TitledEditText hb_value;
    TitledSpinner hemoglobin_result_unit;
    TitledEditText hematocrit_value;
    TitledSpinner hematocrit_unit;
    TitledEditText platelet_value;
    TitledSpinner platelet_unit;
    TitledEditText rbc_value;
    TitledSpinner rbc_unit;
    TitledEditText wbc_value;
    TitledSpinner wbc_unit;
    TitledEditText anc_value;
    TitledSpinner anc_unit;
    TitledEditText percent_neutrophil;
    TitledEditText monocytes_value;
    TitledSpinner monocytes_unit;
    TitledEditText lymphocytes_value;
    TitledSpinner lymphocytes_unit;
    TitledEditText mcv_value;
    TitledSpinner mcv_unit;
    TitledEditText mch_value;
    TitledSpinner mch_unit;
    TitledEditText mch_concentration_value;
    TitledSpinner mch_concentration_unit;
    TitledEditText rdw_cv_value;
    TitledSpinner rdw_cv_unit;
    TitledEditText other_comments;

    TitledButton date_end;


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
        formName = Forms.CBC_ORDER_AND_RESULT;
        form = Forms.cbcOrderAndResult;

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
        // first page views...
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_select_form_type), getResources().getStringArray(R.array.fast_order_and_result_list), "", App.HORIZONTAL, App.HORIZONTAL);
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        String columnName = "";
        final Object[][] locations = serverService.getAllLocationsFromLocalDB(columnName);
        String[] locationArray = new String[locations.length + 2];
        locationArray[0] = "";
        int j = 1;
        for (int i = 0; i < locations.length; i++) {
            locationArray[j] = String.valueOf(locations[i][16]);
            j++;
        }

        assessment_type = new TitledRadioGroup(context, null, getResources().getString(R.string.common_cbc_assessment_type), getResources().getStringArray(R.array.common_cbc_assessment_type_options), getString(R.string.common_cbc_assessment_type_baseline), App.VERTICAL, App.VERTICAL, true);
        monthOfTreatment = new TitledEditText(context, null, getResources().getString(R.string.fast_month_of_treatment), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        orderId = new TitledEditText(context, null, getResources().getString(R.string.order_id), "", "", 60, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        doctor_notes = new TitledEditText(context, null, "Notes", "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        /////////////////////
        orderIds = new TitledSpinner(context, "", getResources().getString(R.string.order_id), getResources().getStringArray(R.array.ztts_empty_array), "", App.HORIZONTAL, true);
        sampleId = new TitledEditText(context, null, getResources().getString(R.string.common_cbc_sample_id), "", "", 25, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        hb_value = new TitledEditText(context, null, getResources().getString(R.string.common_cbc_hb_value), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        hemoglobin_result_unit = new TitledSpinner(context, "", getResources().getString(R.string.common_cbc_hemoglobin_result_unit), getResources().getStringArray(R.array.common_cbc_hemoglobin_result_unit_option), getResources().getString(R.string.common_cbc_hemoglobin_result_unit_4), App.HORIZONTAL, true);

        hematocrit_value = new TitledEditText(context, null, getResources().getString(R.string.common_cbc_hematocrit_value), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        hematocrit_unit = new TitledSpinner(context, "", getResources().getString(R.string.common_cbc_hematocrit_unit), getResources().getStringArray(R.array.common_cbc_hematocrit_unit_option), "", App.HORIZONTAL, true);

        platelet_value = new TitledEditText(context, null, getResources().getString(R.string.common_cbc_platelet_value), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        platelet_unit = new TitledSpinner(context, "", getResources().getString(R.string.common_cbc_platelet_unit), getResources().getStringArray(R.array.common_cbc_platelet_unit_option), getResources().getString(R.string.common_cbc_unit_3), App.HORIZONTAL, true);

        rbc_value = new TitledEditText(context, null, getResources().getString(R.string.common_cbc_rbc_value), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        rbc_unit = new TitledSpinner(context, "", getResources().getString(R.string.common_cbc_rbc_unit), getResources().getStringArray(R.array.common_cbc_rbc_unit_option), getResources().getString(R.string.common_cbc_unit_4), App.HORIZONTAL, true);

        wbc_value = new TitledEditText(context, null, getResources().getString(R.string.common_cbc_wbc_value), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        wbc_unit = new TitledSpinner(context, "", getResources().getString(R.string.common_cbc_wbc_unit), getResources().getStringArray(R.array.common_cbc_wbc_unit_option), getResources().getString(R.string.common_cbc_unit_3), App.HORIZONTAL, true);

        anc_value = new TitledEditText(context, null, getResources().getString(R.string.common_cbc_anc_value), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        anc_unit = new TitledSpinner(context, "", getResources().getString(R.string.common_cbc_anc_unit), getResources().getStringArray(R.array.common_cbc_anc_unit_option), getResources().getString(R.string.common_cbc_unit_9), App.HORIZONTAL, true);

        percent_neutrophil = new TitledEditText(context, null, getResources().getString(R.string.common_cbc_percent_neutrophil), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        monocytes_value = new TitledEditText(context, null, getResources().getString(R.string.common_cbc_monocytes_value), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        monocytes_unit = new TitledSpinner(context, "", getResources().getString(R.string.common_cbc_monocytes_unit), getResources().getStringArray(R.array.common_cbc_monocytes_unit_option), getResources().getString(R.string.common_cbc_unit_10), App.HORIZONTAL, true);

        lymphocytes_value = new TitledEditText(context, null, getResources().getString(R.string.common_cbc_Lymphocytes_value), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        lymphocytes_unit = new TitledSpinner(context, "", getResources().getString(R.string.common_cbc_Lymphocytes_unit), getResources().getStringArray(R.array.common_cbc_Lymphocytes_unit_option), getResources().getString(R.string.common_cbc_unit_10), App.HORIZONTAL, true);

        mcv_value = new TitledEditText(context, null, getResources().getString(R.string.common_cbc_mcv_value), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        mcv_unit = new TitledSpinner(context, "", getResources().getString(R.string.common_cbc_mcv_unit), getResources().getStringArray(R.array.common_cbc_mcv_unit_option), "", App.HORIZONTAL, true);

        mch_value = new TitledEditText(context, null, getResources().getString(R.string.common_cbc_mch_value), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        mch_unit = new TitledSpinner(context, "", getResources().getString(R.string.common_cbc_mch_unit), getResources().getStringArray(R.array.common_cbc_mch_unit_option), "", App.HORIZONTAL, true);

        mch_concentration_value = new TitledEditText(context, null, getResources().getString(R.string.common_cbc_mch_concentration_value), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        mch_concentration_unit = new TitledSpinner(context, "", getResources().getString(R.string.common_cbc_mch_concentration_unit), getResources().getStringArray(R.array.common_cbc_mch_concentration_unit_option), "", App.HORIZONTAL, true);

        rdw_cv_value = new TitledEditText(context, null, getResources().getString(R.string.common_cbc_rdw_cv_value), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        rdw_cv_unit = new TitledSpinner(context, "", getResources().getString(R.string.common_cbc_rdw_cv_unit), getResources().getStringArray(R.array.common_cbc_rdw_cv_unit_option), "", App.HORIZONTAL, true);

        other_comments = new TitledEditText(context, null, getResources().getString(R.string.common_cbc_other_comments), "", "", 1000, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        date_end = new TitledButton(context, null, getResources().getString(R.string.common_cbc_date_end), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        date_end.setTag("date_end");


        // Used for reset fields...
        views = new View[]{formType.getRadioGroup(), formDate.getButton(), assessment_type.getRadioGroup(), monthOfTreatment, orderId, doctor_notes, orderIds.getSpinner(), sampleId.getEditText(), hb_value.getEditText(), hemoglobin_result_unit.getSpinner(), hematocrit_value.getEditText(), hematocrit_unit.getSpinner(),
                platelet_value.getEditText(), platelet_unit.getSpinner(), rbc_value.getEditText(), rbc_unit.getSpinner(), wbc_value.getEditText(), wbc_unit.getSpinner(), anc_value.getEditText(), anc_unit.getSpinner(), percent_neutrophil.getEditText(),
                monocytes_value.getEditText(), monocytes_unit.getSpinner(), lymphocytes_value.getEditText(), lymphocytes_unit.getSpinner(), mcv_value.getEditText(), mcv_unit.getSpinner(), mch_value.getEditText(), mch_unit.getSpinner(),
                mch_concentration_value.getEditText(), mch_concentration_unit.getSpinner(), rdw_cv_value.getEditText(), rdw_cv_unit.getSpinner(), other_comments.getEditText(), date_end.getButton(),};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formType, formDate, assessment_type, monthOfTreatment, orderId, doctor_notes, orderIds, sampleId, hb_value, hemoglobin_result_unit, hematocrit_value, hematocrit_unit, platelet_value, platelet_unit, rbc_value, rbc_unit, wbc_value, wbc_unit, anc_value, anc_unit, percent_neutrophil,
                        monocytes_value, monocytes_unit, lymphocytes_value, lymphocytes_unit, mcv_value, mcv_unit, mch_value, mch_unit, mch_concentration_value, mch_concentration_unit, rdw_cv_value, rdw_cv_unit, other_comments, date_end}};

        formDate.getButton().setOnClickListener(this);
        orderIds.getSpinner().setOnItemSelectedListener(this);
        date_end.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        assessment_type.getRadioGroup().setOnCheckedChangeListener(this);
        monthOfTreatment.getEditText().setEnabled(false);

        hematocrit_value.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = hematocrit_value.getEditText().getText().toString();
                if (text != null)
                    if (text.length() > 0) {
                        hematocrit_unit.setVisibility(View.VISIBLE);
                    } else {
                        hematocrit_unit.setVisibility(View.GONE);
                    }
            }
        });
        platelet_value.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = platelet_value.getEditText().getText().toString();
                if (text != null)
                    if (text.length() > 0) {
                        platelet_unit.setVisibility(View.VISIBLE);
                    } else {
                        platelet_unit.setVisibility(View.GONE);
                    }
            }
        });
        rbc_value.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = rbc_value.getEditText().getText().toString();
                if (text != null)
                    if (text.length() > 0) {
                        rbc_unit.setVisibility(View.VISIBLE);
                    } else {
                        rbc_unit.setVisibility(View.GONE);
                    }
            }
        });
        wbc_value.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = wbc_value.getEditText().getText().toString();
                if (text != null)
                    if (text.length() > 0) {
                        wbc_unit.setVisibility(View.VISIBLE);
                    } else {
                        wbc_unit.setVisibility(View.GONE);
                    }
            }
        });
        anc_value.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = anc_value.getEditText().getText().toString();
                if (text != null)
                    if (text.length() > 0) {
                        anc_unit.setVisibility(View.VISIBLE);
                    } else {
                        anc_unit.setVisibility(View.GONE);
                    }
            }
        });
        monocytes_value.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = monocytes_value.getEditText().getText().toString();
                if (text != null)
                    if (text.length() > 0) {
                        monocytes_unit.setVisibility(View.VISIBLE);
                    } else {
                        monocytes_unit.setVisibility(View.GONE);
                    }
            }
        });
        lymphocytes_value.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = lymphocytes_value.getEditText().getText().toString();
                if (text != null)
                    if (text.length() > 0) {
                        lymphocytes_unit.setVisibility(View.VISIBLE);
                    } else {
                        lymphocytes_unit.setVisibility(View.GONE);
                    }
            }
        });
        mcv_value.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = mcv_value.getEditText().getText().toString();
                if (text != null)
                    if (text.length() > 0) {
                        mcv_unit.setVisibility(View.VISIBLE);
                    } else {
                        mcv_unit.setVisibility(View.GONE);
                    }
            }
        });


        resetViews();
    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();

        String formDa = formDate.getButton().getText().toString();
        String personDOB = App.getPatient().getPerson().getBirthdate();


        Date date = new Date();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {


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
        if (!(date_end.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            if (secondDateCalendar.after(App.getCalendar(date))) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                date_end.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                date_end.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else if (secondDateCalendar.before(formDateCalendar)) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_sample_date_cannot_be_before_form_date), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                date_end.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                date_end.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }


        formDate.getButton().setEnabled(true);
        date_end.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = false;
        View view = null;
        if (sampleId.getVisibility() == View.VISIBLE && sampleId.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            sampleId.getEditText().setError(getString(R.string.empty_field));
            sampleId.getEditText().requestFocus();
            error = true;
        }

        if (hb_value.getVisibility() == View.VISIBLE && hb_value.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            hb_value.getEditText().setError(getString(R.string.empty_field));
            hb_value.getEditText().requestFocus();
            error = true;
        }

        if (hematocrit_value.getVisibility() == View.VISIBLE && hematocrit_value.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            hematocrit_value.getEditText().setError(getString(R.string.empty_field));
            hematocrit_value.getEditText().requestFocus();
            error = true;
        }

        if (platelet_value.getVisibility() == View.VISIBLE && platelet_value.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            platelet_value.getEditText().setError(getString(R.string.empty_field));
            platelet_value.getEditText().requestFocus();
            error = true;
        }

        if (rbc_value.getVisibility() == View.VISIBLE && rbc_value.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            rbc_value.getEditText().setError(getString(R.string.empty_field));
            rbc_value.getEditText().requestFocus();
            error = true;
        }

        if (wbc_value.getVisibility() == View.VISIBLE && wbc_value.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            wbc_value.getEditText().setError(getString(R.string.empty_field));
            wbc_value.getEditText().requestFocus();
            error = true;
        }

        if (anc_value.getVisibility() == View.VISIBLE && anc_value.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            anc_value.getEditText().setError(getString(R.string.empty_field));
            anc_value.getEditText().requestFocus();
            error = true;
        }

        if (percent_neutrophil.getVisibility() == View.VISIBLE && percent_neutrophil.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            percent_neutrophil.getEditText().setError(getString(R.string.empty_field));
            percent_neutrophil.getEditText().requestFocus();
            error = true;
        }

        if (lymphocytes_value.getVisibility() == View.VISIBLE && lymphocytes_value.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            lymphocytes_value.getEditText().setError(getString(R.string.empty_field));
            lymphocytes_value.getEditText().requestFocus();
            error = true;
        }

        if (mcv_value.getVisibility() == View.VISIBLE && mcv_value.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mcv_value.getEditText().setError(getString(R.string.empty_field));
            mcv_value.getEditText().requestFocus();
            error = true;
        }

        if (mch_value.getVisibility() == View.VISIBLE && mch_value.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mch_value.getEditText().setError(getString(R.string.empty_field));
            mch_value.getEditText().requestFocus();
            error = true;
        }

        if (mch_concentration_value.getVisibility() == View.VISIBLE && mch_concentration_value.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mch_concentration_value.getEditText().setError(getString(R.string.empty_field));
            mch_concentration_value.getEditText().requestFocus();
            error = true;
        }

        if (rdw_cv_value.getVisibility() == View.VISIBLE && rdw_cv_value.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            rdw_cv_value.getEditText().setError(getString(R.string.empty_field));
            rdw_cv_value.getEditText().requestFocus();
            error = true;
        }

        if (other_comments.getVisibility() == View.VISIBLE && other_comments.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            other_comments.getEditText().setError(getString(R.string.empty_field));
            other_comments.getEditText().requestFocus();
            error = true;
        }


        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            //  DrawableCompat.setTint(clearIcon, color);
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

        if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))) {

            if (assessment_type.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"TYPE OF ASSESSMENT", App.get(assessment_type).equals(getResources().getString(R.string.common_cbc_assessment_type_baseline)) ? "BASELINE ASSESSMENT" :
                        (App.get(assessment_type).equals(getResources().getString(R.string.common_cbc_assessment_type_treatment)) ? "TREATMENT INITIATION" :
                                (App.get(assessment_type).equals(getResources().getString(R.string.common_cbc_assessment_type_folowup)) ? "FOLLOW UP" :
                                        (App.get(assessment_type).equals(getResources().getString(R.string.common_cbc_assessment_type_end)) ? "END OF TREATMENT ASSESSMENT" : "POST-TREATMENT ASSESSMENT")))});

            if (monthOfTreatment.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"FOLLOW-UP MONTH", monthOfTreatment.getEditText().getText().toString()});

            if (orderId.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"ORDER ID", App.get(orderId)});

            if (doctor_notes.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"CLINICIAN NOTES (TEXT)", App.get(doctor_notes)});

        /*    if (date_end.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDate(secondDateCalendar)});*/

        } else if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_result))) {

            observations.add(new String[]{"ORDER ID", App.get(orderIds)});

            if (sampleId.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"SPECIMEN ID", sampleId.getEditText().getText().toString()});
            if (hb_value.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"HEMOGLOBIN RESULT VALUE", hb_value.getEditText().getText().toString()});
            if (hemoglobin_result_unit.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"HAEMOGLOBIN RESULT UNIT", App.get(hemoglobin_result_unit).equals(getResources().getString(R.string.common_cbc_hemoglobin_result_unit_1)) ? "mIU/L" :
                        (App.get(hemoglobin_result_unit).equals(getResources().getString(R.string.common_cbc_hemoglobin_result_unit_2)) ? "mEq/L" :
                                (App.get(hemoglobin_result_unit).equals(getResources().getString(R.string.common_cbc_hemoglobin_result_unit_3)) ? "mg/dL" :
                                        (App.get(hemoglobin_result_unit).equals(getResources().getString(R.string.common_cbc_hemoglobin_result_unit_4)) ? "gm/dL" :
                                                (App.get(hemoglobin_result_unit).equals(getResources().getString(R.string.common_cbc_hemoglobin_result_unit_5)) ? "gm/L" :
                                                        (App.get(hemoglobin_result_unit).equals(getResources().getString(R.string.common_cbc_hemoglobin_result_unit_6)) ? "mg/L" :
                                                                (App.get(hemoglobin_result_unit).equals(getResources().getString(R.string.common_cbc_hemoglobin_result_unit_7)) ? "IU/L" :
                                                                        (App.get(hemoglobin_result_unit).equals(getResources().getString(R.string.common_cbc_hemoglobin_result_unit_8)) ? "U/L" :
                                                                                (App.get(hemoglobin_result_unit).equals(getResources().getString(R.string.common_cbc_hemoglobin_result_unit_9)) ? "IU/mL" :
                                                                                        (App.get(hemoglobin_result_unit).equals(getResources().getString(R.string.common_cbc_hemoglobin_result_unit_10)) ? "µL/mL" : "umol/L")))))))))});
            if (hematocrit_value.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"HEMATOCRIT RESULT VALUE", hematocrit_value.getEditText().getText().toString()});
            if (hematocrit_unit.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"HEMATOCRIT RESULT UNIT", App.get(hematocrit_unit).equals(getResources().getString(R.string.common_cbc_hematocrit_unit_1)) ? "%" : ""});
            if (platelet_value.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"PLATELETS", platelet_value.getEditText().getText().toString()});
            if (platelet_unit.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"PLATELET COUNT RESULT UNIT", App.get(platelet_unit).equals(getResources().getString(R.string.common_cbc_unit_1)) ? "X 10³/L" :
                        (App.get(platelet_unit).equals(getResources().getString(R.string.common_cbc_unit_2)) ? "X 10⁶/L" :
                                (App.get(platelet_unit).equals(getResources().getString(R.string.common_cbc_unit_3)) ? "X 10⁹/L" :
                                        (App.get(platelet_unit).equals(getResources().getString(R.string.common_cbc_unit_4)) ? "X 10¹²/L" :
                                                (App.get(platelet_unit).equals(getResources().getString(R.string.common_cbc_unit_5)) ? "X 10³/µL" :
                                                        (App.get(platelet_unit).equals(getResources().getString(R.string.common_cbc_unit_6)) ? "X 10⁶/µL" :
                                                                (App.get(platelet_unit).equals(getResources().getString(R.string.common_cbc_unit_7)) ? "X 10⁹/µL" : "X 10¹²/µL"))))))});
            if (rbc_value.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"RED BLOOD CELLS", rbc_value.getEditText().getText().toString()});
            if (rbc_unit.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"RBC COUNT RESULT UNIT", App.get(rbc_unit).equals(getResources().getString(R.string.common_cbc_unit_1)) ? "X 10³/L" :
                        (App.get(rbc_unit).equals(getResources().getString(R.string.common_cbc_unit_2)) ? "X 10⁶/L" :
                                (App.get(rbc_unit).equals(getResources().getString(R.string.common_cbc_unit_3)) ? "X 10⁹/L" :
                                        (App.get(rbc_unit).equals(getResources().getString(R.string.common_cbc_unit_4)) ? "X 10¹²/L" :
                                                (App.get(rbc_unit).equals(getResources().getString(R.string.common_cbc_unit_5)) ? "X 10³/µL" :
                                                        (App.get(rbc_unit).equals(getResources().getString(R.string.common_cbc_unit_6)) ? "X 10⁶/µL" :
                                                                (App.get(rbc_unit).equals(getResources().getString(R.string.common_cbc_unit_7)) ? "X 10⁹/µL" : "X 10¹²/µL"))))))});
            if (wbc_value.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"WHITE BLOOD CELLS", wbc_value.getEditText().getText().toString()});
            if (wbc_unit.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"WBC COUNT RESULT UNIT", App.get(wbc_unit).equals(getResources().getString(R.string.common_cbc_unit_1)) ? "X 10³/L" :
                        (App.get(wbc_unit).equals(getResources().getString(R.string.common_cbc_unit_2)) ? "X 10⁶/L" :
                                (App.get(wbc_unit).equals(getResources().getString(R.string.common_cbc_unit_3)) ? "X 10⁹/L" :
                                        (App.get(wbc_unit).equals(getResources().getString(R.string.common_cbc_unit_4)) ? "X 10¹²/L" :
                                                (App.get(wbc_unit).equals(getResources().getString(R.string.common_cbc_unit_5)) ? "X 10³/µL" :
                                                        (App.get(wbc_unit).equals(getResources().getString(R.string.common_cbc_unit_6)) ? "X 10⁶/µL" :
                                                                (App.get(wbc_unit).equals(getResources().getString(R.string.common_cbc_unit_7)) ? "X 10⁹/µL" : "X 10¹²/µL"))))))});
            if (anc_value.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"ABSOLUTE NEUTROPHIL COUNT", anc_value.getEditText().getText().toString()});
            if (anc_unit.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"ANC RESULT UNIT", App.get(anc_unit).equals(getResources().getString(R.string.common_cbc_unit_1)) ? "X 10³/L" :
                        (App.get(anc_unit).equals(getResources().getString(R.string.common_cbc_unit_2)) ? "X 10⁶/L" :
                                (App.get(anc_unit).equals(getResources().getString(R.string.common_cbc_unit_3)) ? "X 10⁹/L" :
                                        (App.get(anc_unit).equals(getResources().getString(R.string.common_cbc_unit_4)) ? "X 10¹²/L" :
                                                (App.get(anc_unit).equals(getResources().getString(R.string.common_cbc_unit_5)) ? "X 10³/µL" :
                                                        (App.get(anc_unit).equals(getResources().getString(R.string.common_cbc_unit_6)) ? "X 10⁶/µL" :
                                                                (App.get(anc_unit).equals(getResources().getString(R.string.common_cbc_unit_7)) ? "X 10⁹/µL" :
                                                                        (App.get(anc_unit).equals(getResources().getString(R.string.common_cbc_unit_7)) ? "X 10¹²/µL" : "/100 WBCs")))))))});
            if (percent_neutrophil.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"NEUTROPHILS (%) - MICROSCOPIC EXAM", percent_neutrophil.getEditText().getText().toString()});
            if (monocytes_value.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"ABSOLUTE MONOCYTE COUNT", monocytes_value.getEditText().getText().toString()});
            if (monocytes_unit.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"MONOCYTES RESULT UNIT", App.get(monocytes_unit).equals(getResources().getString(R.string.common_cbc_unit_1)) ? "X 10³/L" :
                        (App.get(monocytes_unit).equals(getResources().getString(R.string.common_cbc_unit_2)) ? "X 10⁶/L" :
                                (App.get(monocytes_unit).equals(getResources().getString(R.string.common_cbc_unit_3)) ? "X 10⁹/L" :
                                        (App.get(monocytes_unit).equals(getResources().getString(R.string.common_cbc_unit_4)) ? "X 10¹²/L" :
                                                (App.get(monocytes_unit).equals(getResources().getString(R.string.common_cbc_unit_5)) ? "X 10³/µL" :
                                                        (App.get(monocytes_unit).equals(getResources().getString(R.string.common_cbc_unit_6)) ? "X 10⁶/µL" :
                                                                (App.get(monocytes_unit).equals(getResources().getString(R.string.common_cbc_unit_7)) ? "X 10⁹/µL" :
                                                                        (App.get(monocytes_unit).equals(getResources().getString(R.string.common_cbc_unit_7)) ? "X 10¹²/µL" : "%")))))))});
            if (lymphocytes_value.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"LYMPHOCYTE COUNT", lymphocytes_value.getEditText().getText().toString()});
            if (lymphocytes_unit.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"LYMPHOCYTES RESULT UNIT", App.get(lymphocytes_unit).equals(getResources().getString(R.string.common_cbc_unit_1)) ? "X 10³/L" :
                        (App.get(lymphocytes_unit).equals(getResources().getString(R.string.common_cbc_unit_2)) ? "X 10⁶/L" :
                                (App.get(lymphocytes_unit).equals(getResources().getString(R.string.common_cbc_unit_3)) ? "X 10⁹/L" :
                                        (App.get(lymphocytes_unit).equals(getResources().getString(R.string.common_cbc_unit_4)) ? "X 10¹²/L" :
                                                (App.get(lymphocytes_unit).equals(getResources().getString(R.string.common_cbc_unit_5)) ? "X 10³/µL" :
                                                        (App.get(lymphocytes_unit).equals(getResources().getString(R.string.common_cbc_unit_6)) ? "X 10⁶/µL" :
                                                                (App.get(lymphocytes_unit).equals(getResources().getString(R.string.common_cbc_unit_7)) ? "X 10⁹/µL" :
                                                                        (App.get(lymphocytes_unit).equals(getResources().getString(R.string.common_cbc_unit_7)) ? "X 10¹²/µL" : "%")))))))});
            if (mcv_value.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"MEAN CORPUSCULAR VOLUME", mcv_value.getEditText().getText().toString()});
            if (mcv_unit.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"MCV UNIT", App.get(mcv_unit).equals(getResources().getString(R.string.common_cbc_mcv_unit_1)) ? "fl" : ""});
            if (mch_value.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"MEAN CORPUSCULAR HEMOGLOBIN", mch_value.getEditText().getText().toString()});
            if (mch_unit.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"MEAN CORPUSCULAR HEMOGLOBIN (MCH) UNIT", App.get(mch_unit).equals(getResources().getString(R.string.common_cbc_mch_unit_1)) ? "pg" : ""});
            if (mch_concentration_value.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"MEAN CORPUSCULAR HEMOGLOBIN CONCENTRATION (MCHC) VALUE", mch_concentration_value.getEditText().getText().toString()});
            if (mch_concentration_unit.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"MEAN CORPUSCULAR HEMOGLOBIN CONCENTRATION (MCHC) UNIT", App.get(mch_concentration_unit).equals(getResources().getString(R.string.common_cbc_mch_concentration_unit_1)) ? "gm/dL" : ""});
            if (rdw_cv_value.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"RED CELL DISTRIBUTION WIDTH (RDW CV) VALUE", rdw_cv_value.getEditText().getText().toString()});
            if (rdw_cv_unit.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"RED CELL DISTRIBUTION WIDTH (RDW CV) UNIT", App.get(rdw_cv_unit).equals(getResources().getString(R.string.common_cbc_rdw_cv_unit_1)) ? "%" : ""});
            if (other_comments.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"FREE TEXT COMMENT", other_comments.getEditText().getText().toString()});
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
                        id = serverService.saveFormLocallyTesting("CBC Test Order", form, formDateCalendar, observations.toArray(new String[][]{}));

                    result = serverService.saveEncounterAndObservationTesting("CBC Test Order", form, formDateCalendar, observations.toArray(new String[][]{}), id);
                    if (!result.contains("SUCCESS"))
                        return result;

                    /*String uuidEncounter = result.split("_")[1];

                    result = serverService.saveLabTestOrder(uuidEncounter, "refer_cbc", App.get(orderId), formDateCalendar, "CBC Test Order", id, "WHOLE BLOOD SAMPLE", "WHOLE BLOOD");
                    if (!result.contains("SUCCESS"))
                        return result;

                    String uuidLabOrder = result.split("_")[1];

                    final ArrayList<String[]> newObservations = new ArrayList<String[]>();
                    newObservations.add(new String[]{"LAB ORDER UUID",uuidLabOrder});
                    result = serverService.updateEncounterAndObservationTesting(uuidEncounter, newObservations.toArray(new String[][]{}), id);
                    if (!result.contains("SUCCESS"))
                        return result;*/

                } else if (App.get(formType).equals(getResources().getString(R.string.fast_result))) {

                    String id = null;
                    if (App.getMode().equalsIgnoreCase("OFFLINE"))
                        id = serverService.saveFormLocallyTesting("CBC Test Result", form, formDateCalendar, observations.toArray(new String[][]{}));

                    String orderUuid = serverService.getObsValueByObs(App.getPatientId(), "CBC Test Order", "ORDER ID", App.get(orderIds), "LAB ORDER UUID");

                   // result = serverService.saveLabTestResult("refer_cbc", App.get(orderIds), orderUuid,  observations.toArray(new String[][]{}), id);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";

                    /*result = serverService.saveEncounterAndObservation("CBC Test Order", form, formDateCalendar, observations.toArray(new String[][]{}), false);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";*/
                }

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

        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {
            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (fo.getFormName().contains("Order")) {
                formType.getRadioGroup().getButtons().get(0).setChecked(true);
                formType.getRadioGroup().getButtons().get(1).setEnabled(false);

                if (obs[0][0].equals("TYPE OF ASSESSMENT")) {
                    for (RadioButton rb : assessment_type.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.common_cbc_assessment_type_baseline)) && obs[0][1].equals("BASELINE ASSESSMENT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.common_cbc_assessment_type_treatment)) && obs[0][1].equals("TREATMENT INITIATION")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.common_cbc_assessment_type_folowup)) && obs[0][1].equals("FOLLOW UP")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.common_cbc_assessment_type_end)) && obs[0][1].equals("END OF TREATMENT ASSESSMENT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.common_cbc_assessment_type_post)) && obs[0][1].equals("POST-TREATMENT ASSESSMENT")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                } else if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                    monthOfTreatment.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("ORDER ID")) {
                    orderId.getEditText().setKeyListener(null);
                    orderId.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
                    doctor_notes.getEditText().setText(obs[0][1]);
                }
                submitButton.setEnabled(true);

            } else {
                formType.getRadioGroup().getButtons().get(0).setChecked(false);
                formType.getRadioGroup().getButtons().get(1).setEnabled(true);

                if (obs[0][0].equals("ORDER ID")) {
                    orderIds.getSpinner().selectValue(obs[0][1]);
                    orderIds.getSpinner().setEnabled(false);
                } else if (obs[0][0].equals("SPECIMEN ID")) {
                    sampleId.getEditText().setText(obs[0][1]);
                    sampleId.getEditText().setEnabled(false);
                } else if (obs[0][0].equals("HEMOGLOBIN RESULT VALUE")) {
                    hb_value.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("HAEMOGLOBIN RESULT UNIT")) {
                    String value = obs[0][1].equals("mIU/L") ? getResources().getString(R.string.common_cbc_hemoglobin_result_unit_1) :
                            (obs[0][1].equals("mEq/L") ? getResources().getString(R.string.common_cbc_hemoglobin_result_unit_2) :
                                    (obs[0][1].equals("mg/dL") ? getResources().getString(R.string.common_cbc_hemoglobin_result_unit_3) :
                                            (obs[0][1].equals("gm/dL") ? getResources().getString(R.string.common_cbc_hemoglobin_result_unit_4) :
                                                    (obs[0][1].equals("gm/L") ? getResources().getString(R.string.common_cbc_hemoglobin_result_unit_5) :
                                                            (obs[0][1].equals("mg/L") ? getResources().getString(R.string.common_cbc_hemoglobin_result_unit_6) :
                                                                    (obs[0][1].equals("IU/L") ? getResources().getString(R.string.common_cbc_hemoglobin_result_unit_7) :
                                                                            (obs[0][1].equals("U/L") ? getResources().getString(R.string.common_cbc_hemoglobin_result_unit_8) :
                                                                                    (obs[0][1].equals("IU/mL") ? getResources().getString(R.string.common_cbc_hemoglobin_result_unit_9) :
                                                                                            (obs[0][1].equals("µL/mL") ? getResources().getString(R.string.common_cbc_hemoglobin_result_unit_10) : getResources().getString(R.string.common_cbc_hemoglobin_result_unit_11))))))))));
                    hemoglobin_result_unit.getSpinner().selectValue(value);
                } else if (obs[0][0].equals("HEMATOCRIT RESULT VALUE")) {
                    hematocrit_value.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("HEMATOCRIT RESULT UNIT")) {
                    String value = obs[0][1].equals("%") ? getResources().getString(R.string.common_cbc_hematocrit_unit_1) : "";
                    hematocrit_unit.getSpinner().selectValue(value);
                } else if (obs[0][0].equals("PLATELETS")) {
                    platelet_value.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("PLATELET COUNT RESULT UNIT")) {
                    String value = obs[0][1].equals("X 10³/L") ? getResources().getString(R.string.common_cbc_unit_1) :
                            (obs[0][1].equals("X 10⁶/L") ? getResources().getString(R.string.common_cbc_unit_2) :
                                    (obs[0][1].equals("X 10⁹/L") ? getResources().getString(R.string.common_cbc_unit_3) :
                                            (obs[0][1].equals("X 10¹²/L") ? getResources().getString(R.string.common_cbc_unit_4) :
                                                    (obs[0][1].equals("X 10³/µL") ? getResources().getString(R.string.common_cbc_unit_5) :
                                                            (obs[0][1].equals("X 10⁶/µL") ? getResources().getString(R.string.common_cbc_unit_6) :
                                                                    (obs[0][1].equals("X 10⁹/µL") ? getResources().getString(R.string.common_cbc_unit_7) : getResources().getString(R.string.common_cbc_unit_7)))))));
                    platelet_unit.getSpinner().selectValue(value);
                } else if (obs[0][0].equals("RED BLOOD CELLS")) {
                    rbc_value.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("RBC COUNT RESULT UNIT")) {
                    String value = obs[0][1].equals("X 10³/L") ? getResources().getString(R.string.common_cbc_unit_1) :
                            (obs[0][1].equals("X 10⁶/L") ? getResources().getString(R.string.common_cbc_unit_2) :
                                    (obs[0][1].equals("X 10⁹/L") ? getResources().getString(R.string.common_cbc_unit_3) :
                                            (obs[0][1].equals("X 10¹²/L") ? getResources().getString(R.string.common_cbc_unit_4) :
                                                    (obs[0][1].equals("X 10³/µL") ? getResources().getString(R.string.common_cbc_unit_5) :
                                                            (obs[0][1].equals("X 10⁶/µL") ? getResources().getString(R.string.common_cbc_unit_6) :
                                                                    (obs[0][1].equals("X 10⁹/µL") ? getResources().getString(R.string.common_cbc_unit_7) : getResources().getString(R.string.common_cbc_unit_8)))))));
                    rbc_unit.getSpinner().selectValue(value);
                } else if (obs[0][0].equals("WHITE BLOOD CELLS")) {
                    wbc_value.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("WBC COUNT RESULT UNIT")) {
                    String value = obs[0][1].equals("X 10³/L") ? getResources().getString(R.string.common_cbc_unit_1) :
                            (obs[0][1].equals("X 10⁶/L") ? getResources().getString(R.string.common_cbc_unit_2) :
                                    (obs[0][1].equals("X 10⁹/L") ? getResources().getString(R.string.common_cbc_unit_3) :
                                            (obs[0][1].equals("X 10¹²/L") ? getResources().getString(R.string.common_cbc_unit_4) :
                                                    (obs[0][1].equals("X 10³/µL") ? getResources().getString(R.string.common_cbc_unit_5) :
                                                            (obs[0][1].equals("X 10⁶/µL") ? getResources().getString(R.string.common_cbc_unit_6) :
                                                                    (obs[0][1].equals("X 10⁹/µL") ? getResources().getString(R.string.common_cbc_unit_7) : getResources().getString(R.string.common_cbc_unit_8)))))));
                    wbc_unit.getSpinner().selectValue(value);
                } else if (obs[0][0].equals("ABSOLUTE NEUTROPHIL COUNT")) {
                    anc_value.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("ANC RESULT UNIT")) {
                    String value = obs[0][1].equals("X 10³/L") ? getResources().getString(R.string.common_cbc_unit_1) :
                            (obs[0][1].equals("X 10⁶/L") ? getResources().getString(R.string.common_cbc_unit_2) :
                                    (obs[0][1].equals("X 10⁹/L") ? getResources().getString(R.string.common_cbc_unit_3) :
                                            (obs[0][1].equals("X 10¹²/L") ? getResources().getString(R.string.common_cbc_unit_4) :
                                                    (obs[0][1].equals("X 10³/µL") ? getResources().getString(R.string.common_cbc_unit_5) :
                                                            (obs[0][1].equals("X 10⁶/µL") ? getResources().getString(R.string.common_cbc_unit_6) :
                                                                    (obs[0][1].equals("X 10⁹/µL") ? getResources().getString(R.string.common_cbc_unit_7) :
                                                                            (obs[0][1].equals("X 10¹²/µL") ? getResources().getString(R.string.common_cbc_unit_8) : getResources().getString(R.string.common_cbc_unit_9))))))));
                    anc_unit.getSpinner().selectValue(value);
                } else if (obs[0][0].equals("NEUTROPHILS (%) - MICROSCOPIC EXAM")) {
                    percent_neutrophil.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("ABSOLUTE MONOCYTE COUNT")) {
                    monocytes_value.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("MONOCYTES RESULT UNIT")) {
                    String value = obs[0][1].equals("X 10³/L") ? getResources().getString(R.string.common_cbc_unit_1) :
                            (obs[0][1].equals("X 10⁶/L") ? getResources().getString(R.string.common_cbc_unit_2) :
                                    (obs[0][1].equals("X 10⁹/L") ? getResources().getString(R.string.common_cbc_unit_3) :
                                            (obs[0][1].equals("X 10¹²/L") ? getResources().getString(R.string.common_cbc_unit_4) :
                                                    (obs[0][1].equals("X 10³/µL") ? getResources().getString(R.string.common_cbc_unit_5) :
                                                            (obs[0][1].equals("X 10⁶/µL") ? getResources().getString(R.string.common_cbc_unit_6) :
                                                                    (obs[0][1].equals("X 10⁹/µL") ? getResources().getString(R.string.common_cbc_unit_7) :
                                                                            (obs[0][1].equals("X 10¹²/µL") ? getResources().getString(R.string.common_cbc_unit_8) : getResources().getString(R.string.common_cbc_unit_10))))))));
                    monocytes_unit.getSpinner().selectValue(value);
                } else if (obs[0][0].equals("LYMPHOCYTE COUNT")) {
                    lymphocytes_value.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("LYMPHOCYTES RESULT UNIT")) {
                    String value = obs[0][1].equals("X 10³/L") ? getResources().getString(R.string.common_cbc_unit_1) :
                            (obs[0][1].equals("X 10⁶/L") ? getResources().getString(R.string.common_cbc_unit_2) :
                                    (obs[0][1].equals("X 10⁹/L") ? getResources().getString(R.string.common_cbc_unit_3) :
                                            (obs[0][1].equals("X 10¹²/L") ? getResources().getString(R.string.common_cbc_unit_4) :
                                                    (obs[0][1].equals("X 10³/µL") ? getResources().getString(R.string.common_cbc_unit_5) :
                                                            (obs[0][1].equals("X 10⁶/µL") ? getResources().getString(R.string.common_cbc_unit_6) :
                                                                    (obs[0][1].equals("X 10⁹/µL") ? getResources().getString(R.string.common_cbc_unit_7) :
                                                                            (obs[0][1].equals("X 10¹²/µL") ? getResources().getString(R.string.common_cbc_unit_8) : getResources().getString(R.string.common_cbc_unit_10))))))));
                    lymphocytes_unit.getSpinner().selectValue(value);
                } else if (obs[0][0].equals("MEAN CORPUSCULAR VOLUME")) {
                    mcv_value.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("MCV UNIT")) {
                    String value = obs[0][1].equals("fl") ? getResources().getString(R.string.common_cbc_mcv_unit_1) : "";
                    mcv_unit.getSpinner().selectValue(value);
                } else if (obs[0][0].equals("MEAN CORPUSCULAR HEMOGLOBIN")) {
                    mch_value.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("MEAN CORPUSCULAR HEMOGLOBIN (MCH) UNIT")) {
                    String value = obs[0][1].equals("pg") ? getResources().getString(R.string.common_cbc_mch_unit_1) : "";
                    mch_unit.getSpinner().selectValue(value);
                } else if (obs[0][0].equals("MEAN CORPUSCULAR HEMOGLOBIN CONCENTRATION (MCHC) VALUE")) {
                    mch_concentration_value.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("MEAN CORPUSCULAR HEMOGLOBIN CONCENTRATION (MCHC) UNIT")) {
                    String value = obs[0][1].equals("gm/dL") ? getResources().getString(R.string.common_cbc_mch_concentration_unit_1) : "";
                    mch_concentration_unit.getSpinner().selectValue(value);
                } else if (obs[0][0].equals("RED CELL DISTRIBUTION WIDTH (RDW CV) VALUE")) {
                    rdw_cv_value.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("RED CELL DISTRIBUTION WIDTH (RDW CV) UNIT")) {
                    String value = obs[0][1].equals("%") ? getResources().getString(R.string.common_cbc_rdw_cv_unit_1) : "";
                    rdw_cv_unit.getSpinner().selectValue(value);
                } else if (obs[0][0].equals("FREE TEXT COMMENT")) {
                    other_comments.getEditText().setText(obs[0][1]);
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
        if (view == date_end.getButton()) {
            date_end.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", true);
//            dateChoose = true;

        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }


    @Override
    public void resetViews() {
        super.resetViews();

        formDate.setVisibility(View.GONE);
        assessment_type.setVisibility(View.GONE);
        monthOfTreatment.setVisibility(View.GONE);
        orderId.setVisibility(View.GONE);
        doctor_notes.setVisibility(View.GONE);
        orderIds.setVisibility(View.GONE);
        sampleId.setVisibility(View.GONE);
        hb_value.setVisibility(View.GONE);
        hemoglobin_result_unit.setVisibility(View.GONE);
        hematocrit_value.setVisibility(View.GONE);
        hematocrit_unit.setVisibility(View.GONE);
        platelet_value.setVisibility(View.GONE);
        platelet_unit.setVisibility(View.GONE);
        rbc_value.setVisibility(View.GONE);
        rbc_unit.setVisibility(View.GONE);
        wbc_value.setVisibility(View.GONE);
        wbc_unit.setVisibility(View.GONE);
        anc_value.setVisibility(View.GONE);
        anc_unit.setVisibility(View.GONE);
        percent_neutrophil.setVisibility(View.GONE);
        monocytes_value.setVisibility(View.GONE);
        monocytes_unit.setVisibility(View.GONE);
        lymphocytes_value.setVisibility(View.GONE);
        lymphocytes_unit.setVisibility(View.GONE);
        mcv_value.setVisibility(View.GONE);
        mcv_unit.setVisibility(View.GONE);
        mch_value.setVisibility(View.GONE);
        mch_unit.setVisibility(View.GONE);
        mch_concentration_value.setVisibility(View.GONE);
        mch_concentration_unit.setVisibility(View.GONE);
        rdw_cv_value.setVisibility(View.GONE);
        rdw_cv_unit.setVisibility(View.GONE);
        other_comments.setVisibility(View.GONE);
        date_end.setVisibility(View.GONE);

        submitButton.setEnabled(false);


        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        Boolean flag = true;


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);

                refill(formId);
                flag = false;

            } else bundle.putBoolean("save", false);

        }
    }

    public void setOrderId() {
        Date nowDate = new Date();
        orderId.getEditText().setText("CBC - " + App.getSqlDateTime(nowDate));
        orderId.getEditText().setKeyListener(null);
        orderId.getEditText().setFocusable(false);
    }

    public void updateFollowUpMonth() {


        String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "TREATMENT START DATE");
        if (treatmentDate == null) {
            treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "PET-" + "Treatment Initiation", "TREATMENT START DATE");
        }

        String format = "";
        String[] monthArray;

        if (treatmentDate == null) {
            monthArray = new String[1];
            monthArray[0] = "0";
            monthOfTreatment.getEditText().setText("" + monthArray[0]);
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
            monthOfTreatment.getEditText().setText("" + diffMonth);


        }
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == assessment_type.getRadioGroup()) {
            monthOfTreatment.setVisibility(View.GONE);
            if (assessment_type.getRadioGroup().getSelectedValue().equals(getString(R.string.common_cbc_assessment_type_folowup))) {
                monthOfTreatment.setVisibility(View.VISIBLE);
                updateFollowUpMonth();
            }
        }
        if (radioGroup == formType.getRadioGroup()) {
            if (radioGroup == formType.getRadioGroup()) {
                formDate.setVisibility(View.GONE);
                assessment_type.setVisibility(View.GONE);
                monthOfTreatment.setVisibility(View.GONE);
                orderId.setVisibility(View.GONE);
                doctor_notes.setVisibility(View.GONE);
                orderIds.setVisibility(View.GONE);
                sampleId.setVisibility(View.GONE);
                hb_value.setVisibility(View.GONE);
                hemoglobin_result_unit.setVisibility(View.GONE);
                hematocrit_value.setVisibility(View.GONE);
                hematocrit_unit.setVisibility(View.GONE);
                platelet_value.setVisibility(View.GONE);
                platelet_unit.setVisibility(View.GONE);
                rbc_value.setVisibility(View.GONE);
                rbc_unit.setVisibility(View.GONE);
                wbc_value.setVisibility(View.GONE);
                wbc_unit.setVisibility(View.GONE);
                anc_value.setVisibility(View.GONE);
                anc_unit.setVisibility(View.GONE);
                percent_neutrophil.setVisibility(View.GONE);
                monocytes_value.setVisibility(View.GONE);
                monocytes_unit.setVisibility(View.GONE);
                lymphocytes_value.setVisibility(View.GONE);
                lymphocytes_unit.setVisibility(View.GONE);
                mcv_value.setVisibility(View.GONE);
                mcv_unit.setVisibility(View.GONE);
                mch_value.setVisibility(View.GONE);
                mch_unit.setVisibility(View.GONE);
                mch_concentration_value.setVisibility(View.GONE);
                mch_concentration_unit.setVisibility(View.GONE);
                rdw_cv_value.setVisibility(View.GONE);
                rdw_cv_unit.setVisibility(View.GONE);
                other_comments.setVisibility(View.GONE);
                date_end.setVisibility(View.GONE);

                if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))) {
                    setOrderId();
                    submitButton.setEnabled(true);
                    formDate.setVisibility(View.VISIBLE);
                    assessment_type.setVisibility(View.VISIBLE);
                    if (assessment_type.getRadioGroup().getSelectedValue().equals(getString(R.string.common_cbc_assessment_type_folowup))) {
                        monthOfTreatment.setVisibility(View.VISIBLE);
                        updateFollowUpMonth();
                    }
                    orderId.setVisibility(View.VISIBLE);
                    doctor_notes.setVisibility(View.VISIBLE);
                    date_end.setVisibility(View.VISIBLE);
                } else if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_result))) {
                    submitButton.setEnabled(true);
                    formDate.setVisibility(View.VISIBLE);
                    orderIds.setVisibility(View.VISIBLE);
                    sampleId.setVisibility(View.VISIBLE);
                    hb_value.setVisibility(View.VISIBLE);
                    hemoglobin_result_unit.setVisibility(View.VISIBLE);
                    hematocrit_value.setVisibility(View.VISIBLE);
                    if (App.get(hematocrit_value).length() > 0)
                        hematocrit_unit.setVisibility(View.VISIBLE);
                    platelet_value.setVisibility(View.VISIBLE);
                    if (App.get(platelet_value).length() > 0)
                        platelet_unit.setVisibility(View.VISIBLE);
                    rbc_value.setVisibility(View.VISIBLE);
                    if (App.get(rbc_value).length() > 0)
                        rbc_unit.setVisibility(View.VISIBLE);
                    wbc_value.setVisibility(View.VISIBLE);
                    if (App.get(wbc_value).length() > 0)
                        wbc_unit.setVisibility(View.VISIBLE);
                    anc_value.setVisibility(View.VISIBLE);
                    if (App.get(anc_value).length() > 0)
                        anc_unit.setVisibility(View.VISIBLE);
                    percent_neutrophil.setVisibility(View.VISIBLE);
                    monocytes_value.setVisibility(View.VISIBLE);
                    if (App.get(monocytes_value).length() > 0)
                        monocytes_unit.setVisibility(View.VISIBLE);
                    lymphocytes_value.setVisibility(View.VISIBLE);
                    if (App.get(lymphocytes_value).length() > 0)
                        lymphocytes_unit.setVisibility(View.VISIBLE);
                    mcv_value.setVisibility(View.VISIBLE);
                    if (App.get(mcv_value).length() > 0)
                        mcv_unit.setVisibility(View.VISIBLE);
                    mch_value.setVisibility(View.VISIBLE);
                    mch_unit.setVisibility(View.VISIBLE);
                    mch_concentration_value.setVisibility(View.VISIBLE);
                    mch_concentration_unit.setVisibility(View.VISIBLE);
                    rdw_cv_value.setVisibility(View.VISIBLE);
                    rdw_cv_unit.setVisibility(View.VISIBLE);
                    other_comments.setVisibility(View.VISIBLE);
                    date_end.setVisibility(View.VISIBLE);

                    String[] testIds = serverService.getAllObsValues(App.getPatientId(), "CBC Test Order", "ORDER ID");

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
