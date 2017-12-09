package com.ihsinformatics.gfatmmobile.pet;

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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

public class PetDSTCultureTestForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;


    TitledButton formDate;
    TitledRadioGroup formType;
    TitledEditText orderId;
    TitledButton dateSubmission;
    TitledRadioGroup pointTestBeingDone;
    TitledSpinner monthTreatment;
    TitledRadioGroup specimenType;
    TitledRadioGroup specimenComeFrom;
    TitledEditText otherSpecimentComeFrom;

    TitledSpinner orderIds;
    TitledEditText testId;
    TitledSpinner typeOfMediaDst;
    TitledRadioGroup isoniazidPoint2;
    TitledRadioGroup isoniazid1;
    TitledRadioGroup rifampicin;
    TitledRadioGroup ethambuthol;
    TitledRadioGroup streptomycin;
    TitledRadioGroup pyrazinamide;
    TitledRadioGroup ofloxacin;
    TitledRadioGroup levofloxacin;
    TitledRadioGroup moxifloxacinPoint5;
    TitledRadioGroup moxifloxacin2;
    TitledRadioGroup amikacin;
    TitledRadioGroup kanamycin;
    TitledRadioGroup capreomycin;
    TitledRadioGroup ethionamide;
    TitledRadioGroup cycloserine;
    TitledRadioGroup pas;
    TitledRadioGroup bedaquiline;
    TitledRadioGroup delamanid;
    TitledRadioGroup linezolid;
    TitledRadioGroup clofazamine;
    TitledEditText otherDrug;
    TitledRadioGroup otherDrugResult;

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
        FORM_NAME = Forms.PET_DST_CULTURE_TEST;
        FORM = Forms.pet_dst_order_and_result;

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
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_type_of_form), getResources().getStringArray(R.array.ctb_type_of_form_list), null, App.HORIZONTAL, App.VERTICAL, true);
        dateSubmission = new TitledButton(context, null, getResources().getString(R.string.ctb_date_submission), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        dateSubmission.setTag("dateSubmission");
        pointTestBeingDone = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_point_test_being_done), getResources().getStringArray(R.array.ctb_point_test_being_done_list), getResources().getString(R.string.ctb_baseline), App.VERTICAL, App.VERTICAL);
        monthTreatment = new TitledSpinner(context, null, getResources().getString(R.string.ctb_month_treatment), getResources().getStringArray(R.array.ctb_0_to_24), null, App.HORIZONTAL);
        updateFollowUpMonth();
        specimenType = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_specimen_type), getResources().getStringArray(R.array.ctb_specimen_type_list), getResources().getString(R.string.ctb_sputum), App.HORIZONTAL, App.VERTICAL);
        specimenComeFrom = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_speciment_route), getResources().getStringArray(R.array.ctb_speciment_route_list), null, App.HORIZONTAL, App.VERTICAL);
        otherSpecimentComeFrom = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);


        orderIds = new TitledSpinner(context, "", getResources().getString(R.string.order_id), getResources().getStringArray(R.array.pet_empty_array), "", App.HORIZONTAL);
        testId = new TitledEditText(context,null,getResources().getString(R.string.ctb_test_id),"","",20,RegexUtil.OTHER_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,false);
        typeOfMediaDst = new TitledSpinner(context, null, getResources().getString(R.string.ctb_type_of_media_dst), getResources().getStringArray(R.array.ctb_type_of_media_dst_list), null, App.VERTICAL);
        isoniazidPoint2 = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_isoniazid_point_2_result), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        isoniazid1 = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_isoniazid_1_result), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        rifampicin = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_rifampicin), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        ethambuthol = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ethambuhol), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        streptomycin = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_streptomycin), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        pyrazinamide = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_pyrazinamide), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        ofloxacin = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ofloxacin), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        levofloxacin = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_levofloxacin), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        moxifloxacinPoint5 = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_moxifloxacin_point_5), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        moxifloxacin2 = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_moxifloxacin_2), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        amikacin = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_amikacin), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        kanamycin = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_kanamycin), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        capreomycin = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_capreomycin), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        ethionamide = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ethionamide), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        cycloserine = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_cycloserine), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        pas = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_pas), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        bedaquiline = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_bedaquiline), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        delamanid = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_delamanid), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        linezolid = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_linezolid), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        clofazamine = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_clofazamine), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        otherDrug = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_drug_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        otherDrugResult = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_other_drug_result), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);


        views = new View[]{formDate.getButton(), formType.getRadioGroup(), dateSubmission.getButton(), pointTestBeingDone.getRadioGroup(),
                specimenType.getRadioGroup(), specimenComeFrom.getRadioGroup(), typeOfMediaDst.getSpinner(),
                isoniazidPoint2.getRadioGroup(), isoniazid1.getRadioGroup(), rifampicin.getRadioGroup(),
                ethambuthol.getRadioGroup(), streptomycin.getRadioGroup(), pyrazinamide.getRadioGroup(),
                ofloxacin.getRadioGroup(), levofloxacin.getRadioGroup(), moxifloxacinPoint5.getRadioGroup(),
                moxifloxacin2.getRadioGroup(), amikacin.getRadioGroup(), kanamycin.getRadioGroup(),
                capreomycin.getRadioGroup(), ethionamide.getRadioGroup(), cycloserine.getRadioGroup(),
                pas.getRadioGroup(), bedaquiline.getRadioGroup(), delamanid.getRadioGroup(), linezolid.getRadioGroup(),
                clofazamine.getRadioGroup(), otherDrugResult.getRadioGroup(),testId.getEditText(),
                monthTreatment.getSpinner(), otherSpecimentComeFrom.getEditText(),orderId.getEditText(),
                orderIds.getSpinner()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formType, formDate, orderId, dateSubmission, pointTestBeingDone, monthTreatment, specimenType, specimenComeFrom, otherSpecimentComeFrom,
                        orderIds,testId,
                        typeOfMediaDst, isoniazidPoint2, isoniazid1, rifampicin,
                        ethambuthol, streptomycin, pyrazinamide,
                        ofloxacin, levofloxacin, moxifloxacinPoint5,
                        moxifloxacin2, amikacin, kanamycin,
                        capreomycin, ethionamide, cycloserine,
                        pas, bedaquiline, delamanid, linezolid,
                        clofazamine, otherDrug, otherDrugResult}};

        formDate.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        dateSubmission.getButton().setOnClickListener(this);
        pointTestBeingDone.getRadioGroup().setOnCheckedChangeListener(this);
        specimenType.getRadioGroup().setOnCheckedChangeListener(this);
        specimenComeFrom.getRadioGroup().setOnCheckedChangeListener(this);
        typeOfMediaDst.getSpinner().setOnItemSelectedListener(this);
        isoniazidPoint2.getRadioGroup().setOnCheckedChangeListener(this);
        isoniazid1.getRadioGroup().setOnCheckedChangeListener(this);
        rifampicin.getRadioGroup().setOnCheckedChangeListener(this);
        ethambuthol.getRadioGroup().setOnCheckedChangeListener(this);
        streptomycin.getRadioGroup().setOnCheckedChangeListener(this);
        pyrazinamide.getRadioGroup().setOnCheckedChangeListener(this);
        ofloxacin.getRadioGroup().setOnCheckedChangeListener(this);
        levofloxacin.getRadioGroup().setOnCheckedChangeListener(this);
        moxifloxacinPoint5.getRadioGroup().setOnCheckedChangeListener(this);
        moxifloxacin2.getRadioGroup().setOnCheckedChangeListener(this);
        amikacin.getRadioGroup().setOnCheckedChangeListener(this);
        kanamycin.getRadioGroup().setOnCheckedChangeListener(this);
        capreomycin.getRadioGroup().setOnCheckedChangeListener(this);
        ethionamide.getRadioGroup().setOnCheckedChangeListener(this);
        cycloserine.getRadioGroup().setOnCheckedChangeListener(this);
        monthTreatment.getSpinner().setOnItemSelectedListener(this);
        pas.getRadioGroup().setOnCheckedChangeListener(this);
        bedaquiline.getRadioGroup().setOnCheckedChangeListener(this);
        delamanid.getRadioGroup().setOnCheckedChangeListener(this);
        linezolid.getRadioGroup().setOnCheckedChangeListener(this);
        clofazamine.getRadioGroup().setOnCheckedChangeListener(this);
        otherDrugResult.getRadioGroup().setOnCheckedChangeListener(this);
        orderIds.getSpinner().setOnItemSelectedListener(this);

        otherDrug.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    otherDrugResult.setVisibility(View.VISIBLE);
                }
                else{
                    otherDrugResult.setVisibility(View.GONE);
                }
            }
        });

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
                if(diffMonth < 0){
                    monthArray = new String[1];
                    monthArray[0] = "0";
                    monthTreatment.getSpinner().setSpinnerData(monthArray);
                }
                else {
                    monthArray = new String[diffMonth];
                    for (int i = 0; i < diffMonth; i++) {
                        monthArray[i] = String.valueOf(i + 1);
                    }
                    monthTreatment.getSpinner().setSpinnerData(monthArray);
                }
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
                        String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), App.getProgram() + "-" + "DST Culture Test Order", "ORDER ID", App.get(orderIds));

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
                    String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), App.getProgram() + "-" + "DST Culture Test Order", "ORDER ID", App.get(orderIds));

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
            Date date = new Date();
            String personDOB = App.getPatient().getPerson().getBirthdate();
            String formDa = formDate.getButton().getText().toString();
            // +Date date = App.stringToDate(sampleSubmitDate.getButton().getText().toString(), "dd-MMM-yyyy");

            if (secondDateCalendar.after(date)) {

                secondDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                dateSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }else
                dateSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }
        formDate.getButton().setEnabled(true);
        dateSubmission.getButton().setEnabled(true);
        updateFollowUpMonth();


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
        if (specimenType.getVisibility() == View.VISIBLE && App.get(specimenType).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            specimenType.getRadioGroup().getButtons().get(1).setError(getString(R.string.empty_field));
            specimenType.getRadioGroup().requestFocus();
            error = true;
        } else {
            specimenType.getRadioGroup().getButtons().get(1).setError(null);
        }

        if(otherDrug.getVisibility()==View.VISIBLE){
            if(!App.get(otherDrug).isEmpty()) {
                if (App.get(otherDrug).trim().length() <= 0) {
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    otherDrug.getEditText().setError(getString(R.string.ctb_spaces_only));
                    otherDrug.getEditText().requestFocus();
                    error = true;
                }
            }
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


        if(orderIds.getVisibility()==View.VISIBLE  && flag){
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "DST Culture Test Result", "ORDER ID");
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

        if(testId.getVisibility() == View.VISIBLE  && flag){
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "DST Culture Test Result", "TEST ID");
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
            observations.add(new String[]{"CULTURE MEDIUM TYPE", App.get(typeOfMediaDst).equals(getResources().getString(R.string.ctb_lowenstein_jensen)) ? "LOWENSTEIN-JENSEN MYCOBACTERIA CULTURE METHOD" :
                    (App.get(typeOfMediaDst).equals(getResources().getString(R.string.ctb_mycobacteria_growth_indicator)) ? "MYCOBACTERIA GROWTH INDICATOR TUBE" :
                            (App.get(typeOfMediaDst).equals(getResources().getString(R.string.ctb_middlebrook_7h11s)) ? "MIDDLEBROOK 7H11S" :
                                    (App.get(typeOfMediaDst).equals(getResources().getString(R.string.ctb_laboratory_automation)) ? "TOTAL LABORATORY AUTOMATION" :
                                            "OTHER DST MEDIUM")))});

            observations.add(new String[]{"ISONIAZID 0.2 µg/ml RESISTANT", App.get(isoniazidPoint2).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(isoniazidPoint2).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"ISONIAZID 1 µg/ml RESISTANT RESULT", App.get(isoniazid1).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(isoniazid1).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"RIFAMPICIN RESISTANT", App.get(rifampicin).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(rifampicin).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"ETHAMBUTOL RESISTANT", App.get(ethambuthol).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(ethambuthol).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"STREPTOMYCIN RESISITANT", App.get(streptomycin).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(streptomycin).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"PYRAZINAMIDE RESISTANT", App.get(pyrazinamide).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(pyrazinamide).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"OFLOAXCIN RESISTANT", App.get(ofloxacin).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(ofloxacin).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"LEVOFLOXACIN RESISTANT", App.get(levofloxacin).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(levofloxacin).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"MOXIFLOXACIN 0.5 µg/ml RESISTANT RESULT", App.get(moxifloxacinPoint5).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(moxifloxacinPoint5).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"MOXIFLOXACIN 2 µg/ml RESISTANT RESULT", App.get(moxifloxacin2).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(moxifloxacin2).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"AMIKACIN RESISTANT", App.get(amikacin).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(amikacin).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"KANAMYCIN RESISTANT", App.get(kanamycin).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(kanamycin).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"CAPREOMYCIN RESISTANT", App.get(capreomycin).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(capreomycin).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"ETHIONAMIDE RESISTANT Ethionamide", App.get(ethionamide).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(ethionamide).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"CYCLOSERINE RESISTANT", App.get(cycloserine).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(cycloserine).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"P AMINOSALICYLIC ACID RESISTANT", App.get(pas).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(pas).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"BEDAQUILINE RESISTANT", App.get(bedaquiline).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(bedaquiline).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"DELAMANID RESISTANT", App.get(delamanid).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(delamanid).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"LINEZOLID RESISTANT", App.get(linezolid).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(linezolid).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"CLOFAZAMINE RESISTANT", App.get(clofazamine).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(clofazamine).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});

            observations.add(new String[]{"OTHER DRUG NAME", App.get(otherDrug)});

            if(otherDrugResult.getVisibility()==View.VISIBLE) {
                observations.add(new String[]{"OTHER DRUG RESISTANT RESULT", App.get(otherDrugResult).equals(getResources().getString(R.string.ctb_susceptible)) ? "SUSCEPTIBLE" :
                        (App.get(otherDrugResult).equals(getResources().getString(R.string.ctb_resistant)) ? "RESISTANT":  "INDETERMINATE")});
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
                    result = serverService.saveEncounterAndObservation(App.getProgram()+"-"+"DST Culture Test Order", FORM, formDateCalendar, observations.toArray(new String[][]{}),true);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                } else if (App.get(formType).equals(getResources().getString(R.string.ctb_result))) {
                    result = serverService.saveEncounterAndObservation(App.getProgram()+"-"+"DST Culture Test Result", FORM, formDateCalendar, observations.toArray(new String[][]{}),false);
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
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());


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
                }
                if (obs[0][0].equals("SPECIMEN SUBMISSION DATE")) {
                    String secondDate = obs[0][1];
                    secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                    dateSubmission.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
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
                    for (RadioButton rb : specimenComeFrom.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_lymph)) && obs[0][1].equals("LYMPHOCYTES")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_pleural_fluid)) && obs[0][1].equals("PLEURAL EFFUSION")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_pus)) && obs[0][1].equals("PUS")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_other_title)) && obs[0][1].equals("OTHER SPECIMEN SOURCE")) {
                            rb.setChecked(true);
                            otherSpecimentComeFrom.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                } else if (obs[0][0].equals("OTHER SPECIMEN SOURCE")) {
                    otherSpecimentComeFrom.getEditText().setText(obs[0][1]);
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
                }else if (obs[0][0].equals("CULTURE MEDIUM TYPE")) {
                    String value = obs[0][1].equals("LOWENSTEIN-JENSEN MYCOBACTERIA CULTURE METHOD") ? getResources().getString(R.string.ctb_lowenstein_jensen) :
                            (obs[0][1].equals("MYCOBACTERIA GROWTH INDICATOR TUBE") ? getResources().getString(R.string.ctb_mycobacteria_growth_indicator) :
                                    (obs[0][1].equals("MIDDLEBROOK 7H11S") ? getResources().getString(R.string.ctb_middlebrook_7h11s) :
                                            (obs[0][1].equals("TOTAL LABORATORY AUTOMATION") ? getResources().getString(R.string.ctb_laboratory_automation) :
                                                    getResources().getString(R.string.ctb_other_title))));
                    typeOfMediaDst.getSpinner().selectValue(value);

                }else if (obs[0][0].equals("ISONIAZID 0.2 µg/ml RESISTANT")) {
                    for (RadioButton rb : isoniazidPoint2.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    isoniazidPoint2.setVisibility(View.VISIBLE);
                }else if (obs[0][0].equals("ISONIAZID 1 µg/ml RESISTANT RESULT")) {
                    for (RadioButton rb : isoniazid1.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    isoniazid1.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("RIFAMPICIN RESISTANT")) {
                    for (RadioButton rb : rifampicin.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    rifampicin.setVisibility(View.VISIBLE);
                }else if (obs[0][0].equals("ETHAMBUTOL RESISTANT")) {
                    for (RadioButton rb : ethambuthol.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    ethambuthol.setVisibility(View.VISIBLE);
                }else if (obs[0][0].equals("STREPTOMYCIN RESISITANT")) {
                    for (RadioButton rb : streptomycin.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    streptomycin.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("PYRAZINAMIDE RESISTANT")) {
                    for (RadioButton rb : pyrazinamide.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    pyrazinamide.setVisibility(View.VISIBLE);
                }
                else if (obs[0][0].equals("OFLOAXCIN RESISTANT")) {
                    for (RadioButton rb : ofloxacin.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    ofloxacin.setVisibility(View.VISIBLE);
                }
                else if (obs[0][0].equals("LEVOFLOXACIN RESISTANT")) {
                    for (RadioButton rb : levofloxacin.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    levofloxacin.setVisibility(View.VISIBLE);
                }
                else if (obs[0][0].equals("MOXIFLOXACIN 0.5 µg/ml RESISTANT RESULT")) {
                    for (RadioButton rb : moxifloxacinPoint5.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    moxifloxacinPoint5.setVisibility(View.VISIBLE);
                }else if (obs[0][0].equals("MOXIFLOXACIN 2 µg/ml RESISTANT RESULT")) {
                    for (RadioButton rb : moxifloxacin2.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    moxifloxacin2.setVisibility(View.VISIBLE);
                }
                else if (obs[0][0].equals("AMIKACIN RESISTANT")) {
                    for (RadioButton rb : amikacin.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    amikacin.setVisibility(View.VISIBLE);
                }
                else if (obs[0][0].equals("LEVOFLOXACIN RESISTANT")) {
                    for (RadioButton rb : levofloxacin.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    levofloxacin.setVisibility(View.VISIBLE);
                }
                else if (obs[0][0].equals("KANAMYCIN RESISTANT")) {
                    for (RadioButton rb : kanamycin.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    kanamycin.setVisibility(View.VISIBLE);
                }else if (obs[0][0].equals("CAPREOMYCIN RESISTANT")) {
                    for (RadioButton rb : capreomycin.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    capreomycin.setVisibility(View.VISIBLE);
                }
                else if (obs[0][0].equals("ETHIONAMIDE RESISTANT Ethionamide")) {
                    for (RadioButton rb : ethionamide.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    ethionamide.setVisibility(View.VISIBLE);
                }
                else if (obs[0][0].equals("CYCLOSERINE RESISTANT")) {
                    for (RadioButton rb : cycloserine.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    cycloserine.setVisibility(View.VISIBLE);
                }
                else if (obs[0][0].equals("P AMINOSALICYLIC ACID RESISTANT")) {
                    for (RadioButton rb : pas.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    pas.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("BEDAQUILINE RESISTANT")) {
                    for (RadioButton rb : bedaquiline.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    bedaquiline.setVisibility(View.VISIBLE);
                }
                else if (obs[0][0].equals("DELAMANID RESISTANT")) {
                    for (RadioButton rb : delamanid.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    delamanid.setVisibility(View.VISIBLE);
                }
                else if (obs[0][0].equals("LINEZOLID RESISTANT")) {
                    for (RadioButton rb : linezolid.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    linezolid.setVisibility(View.VISIBLE);
                }
                else if (obs[0][0].equals("CLOFAZAMINE RESISTANT")) {
                    for (RadioButton rb : clofazamine.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    clofazamine.setVisibility(View.VISIBLE);
                }

                else if (obs[0][0].equals("OTHER DRUG NAME")) {
                        otherDrug.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("OTHER DRUG RESISTANT RESULT")) {
                    for (RadioButton rb : otherDrugResult.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    otherDrugResult.setVisibility(View.VISIBLE);
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

        orderId.getEditText().setKeyListener(null);
        formType.getRadioGroup().getButtons().get(0).setEnabled(true);
        formType.getRadioGroup().getButtons().get(1).setEnabled(true);
        orderId.setVisibility(View.GONE);
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        dateSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        formDate.setVisibility(View.GONE);
        goneVisibility();
        submitButton.setEnabled(false);

        String[] testIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "DST Culture Test Order", "ORDER ID");
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
        dateSubmission.setVisibility(View.GONE);
        pointTestBeingDone.setVisibility(View.GONE);
        monthTreatment.setVisibility(View.GONE);
        specimenType.setVisibility(View.GONE);
        specimenComeFrom.setVisibility(View.GONE);
        otherSpecimentComeFrom.setVisibility(View.GONE);
        otherSpecimentComeFrom.getEditText().setText("");
        typeOfMediaDst.setVisibility(View.GONE);
        isoniazidPoint2.setVisibility(View.GONE);
        isoniazid1.setVisibility(View.GONE);
        rifampicin.setVisibility(View.GONE);
        ethambuthol.setVisibility(View.GONE);
        streptomycin.setVisibility(View.GONE);
        pyrazinamide.setVisibility(View.GONE);
        ofloxacin.setVisibility(View.GONE);
        levofloxacin.setVisibility(View.GONE);
        moxifloxacinPoint5.setVisibility(View.GONE);
        moxifloxacin2.setVisibility(View.GONE);
        amikacin.setVisibility(View.GONE);
        kanamycin.setVisibility(View.GONE);
        capreomycin.setVisibility(View.GONE);
        ethionamide.setVisibility(View.GONE);
        cycloserine.setVisibility(View.GONE);
        pas.setVisibility(View.GONE);
        bedaquiline.setVisibility(View.GONE);
        delamanid.setVisibility(View.GONE);
        linezolid.setVisibility(View.GONE);
        clofazamine.setVisibility(View.GONE);
        otherDrug.setVisibility(View.GONE);
        otherDrugResult.setVisibility(View.GONE);

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
            orderIds.setVisibility(View.GONE);
            typeOfMediaDst.setVisibility(View.GONE);
            isoniazidPoint2.setVisibility(View.GONE);
            isoniazid1.setVisibility(View.GONE);
            rifampicin.setVisibility(View.GONE);
            ethambuthol.setVisibility(View.GONE);
            streptomycin.setVisibility(View.GONE);
            pyrazinamide.setVisibility(View.GONE);
            ofloxacin.setVisibility(View.GONE);
            levofloxacin.setVisibility(View.GONE);
            moxifloxacinPoint5.setVisibility(View.GONE);
            moxifloxacin2.setVisibility(View.GONE);
            amikacin.setVisibility(View.GONE);
            kanamycin.setVisibility(View.GONE);
            capreomycin.setVisibility(View.GONE);
            ethionamide.setVisibility(View.GONE);
            cycloserine.setVisibility(View.GONE);
            pas.setVisibility(View.GONE);
            bedaquiline.setVisibility(View.GONE);
            delamanid.setVisibility(View.GONE);
            linezolid.setVisibility(View.GONE);
            clofazamine.setVisibility(View.GONE);
            otherDrug.setVisibility(View.GONE);
        }else if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.ctb_result))) {


            formDate.setVisibility(View.VISIBLE);
            formDate.setDefaultValue();

            orderId.setVisibility(View.GONE);
            dateSubmission.setVisibility(View.GONE);
            pointTestBeingDone.setVisibility(View.GONE);
            monthTreatment.setVisibility(View.GONE);
            specimenType.setVisibility(View.GONE);
            specimenComeFrom.setVisibility(View.GONE);
            otherSpecimentComeFrom.setVisibility(View.GONE);

            typeOfMediaDst.setVisibility(View.VISIBLE);
            isoniazidPoint2.setVisibility(View.VISIBLE);
            isoniazid1.setVisibility(View.VISIBLE);
            rifampicin.setVisibility(View.VISIBLE);
            ethambuthol.setVisibility(View.VISIBLE);
            streptomycin.setVisibility(View.VISIBLE);
            pyrazinamide.setVisibility(View.VISIBLE);
            ofloxacin.setVisibility(View.VISIBLE);
            levofloxacin.setVisibility(View.VISIBLE);
            moxifloxacinPoint5.setVisibility(View.VISIBLE);
            moxifloxacin2.setVisibility(View.VISIBLE);
            amikacin.setVisibility(View.VISIBLE);
            kanamycin.setVisibility(View.VISIBLE);
            capreomycin.setVisibility(View.VISIBLE);
            ethionamide.setVisibility(View.VISIBLE);
            cycloserine.setVisibility(View.VISIBLE);
            pas.setVisibility(View.VISIBLE);
            bedaquiline.setVisibility(View.VISIBLE);
            delamanid.setVisibility(View.VISIBLE);
            linezolid.setVisibility(View.VISIBLE);
            clofazamine.setVisibility(View.VISIBLE);
            otherDrug.setVisibility(View.VISIBLE);
            if(App.get(otherDrug).length()>0){
                otherDrugResult.setVisibility(View.VISIBLE);
            }
            orderIds.setVisibility(View.VISIBLE);
            testId.setVisibility(View.VISIBLE);
            testId.getEditText().setDefaultValue();

            String[] testIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "DST Culture Test Order", "ORDER ID");
            if(testIds == null || testIds.length == 0){
                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage(getResources().getString(R.string.ctb_no_dst_order_found));
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


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == formType.getRadioGroup()) {
            formDate.setVisibility(View.VISIBLE);
            submitButton.setEnabled(true);
            showTestOrderOrTestResult();
        }
        if (group == specimenType.getRadioGroup()) {
            if (specimenType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_extra_pulmonary))) {
                specimenComeFrom.setVisibility(View.VISIBLE);

            } else {
                specimenComeFrom.getRadioGroup().clearCheck();
                specimenComeFrom.setVisibility(View.GONE);
            }
        }
        if (group == pointTestBeingDone.getRadioGroup()) {
            if (pointTestBeingDone.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_followup))) {
                monthTreatment.setVisibility(View.VISIBLE);

            } else {
                monthTreatment.setVisibility(View.GONE);
            }
        }if (group == specimenComeFrom.getRadioGroup()) {
            if (specimenComeFrom.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_other_title))) {
                otherSpecimentComeFrom.setVisibility(View.VISIBLE);

            } else {
                otherSpecimentComeFrom.setVisibility(View.GONE);
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
