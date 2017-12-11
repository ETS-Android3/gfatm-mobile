package com.ihsinformatics.gfatmmobile.fast;

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
 * Created by Haris on 2/8/2017.
 */

public class FastDSTOrderAndResultForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    //  public static final int THIRD_DATE_DIALOG_ID = 3;

    // protected Calendar thirdDateCalendar;
    //  protected DialogFragment thirdDateFragment;

    Context context;

    // Views...
    TitledButton formDate;
    TitledEditText testId;
    TitledRadioGroup formType;
    MyTextView dstOrder;
    MyTextView dstResult;
    TitledButton dateOfSubmission;
    TitledRadioGroup testContextStatus;
    TitledSpinner monthOfTreatment;
    TitledRadioGroup specimenType;
    TitledRadioGroup specimenSource;
    TitledEditText specimenSourceOther;
    //TitledButton dateTestResult;
    TitledSpinner dstMedium;
    TitledRadioGroup inh02Resistant;
    TitledRadioGroup inh1Resistant;
    TitledRadioGroup rifResistant;
    TitledRadioGroup etbResistant;
    TitledRadioGroup smResistant;
    TitledRadioGroup pzaResistant;
    TitledRadioGroup ofxResistant;
    TitledRadioGroup levoResistant;
    TitledRadioGroup moxi05Resistant;
    TitledRadioGroup moxi2Resistant;
    TitledRadioGroup amkResistant;
    TitledRadioGroup kmResistant;
    TitledRadioGroup cmResistant;
    TitledRadioGroup ethioResistant;
    TitledRadioGroup csResistant;
    TitledRadioGroup pasResistant;
    TitledRadioGroup bdqResistant;
    TitledRadioGroup dlmResistant;
    TitledRadioGroup lzdResistant;
    TitledRadioGroup cfzResistant;
    TitledRadioGroup otherDrugResult;
    TitledEditText otherDrugName;
  //  ImageView testIdView;
    TitledSpinner orderIds;
    TitledEditText orderId;

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
        FORM_NAME = Forms.FAST_DST_ORDER_AND_RESULT_FORM;
        FORM = Forms.fastDstOrderAndResultForm;

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

        //    thirdDateCalendar = Calendar.getInstance();
        //   thirdDateFragment = new SelectDateFragment();

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        testId = new TitledEditText(context, null, getResources().getString(R.string.fast_test_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_select_form_type), getResources().getStringArray(R.array.fast_order_and_result_list), "", App.HORIZONTAL, App.HORIZONTAL);
        dstOrder = new MyTextView(context, getResources().getString(R.string.fast_dst_order));
        dstOrder.setTypeface(null, Typeface.BOLD);

        dateOfSubmission = new TitledButton(context, null, getResources().getString(R.string.fast_date_of_submission), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);

        testContextStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_at_what_point_test_being_done), getResources().getStringArray(R.array.fast_test_being_done_list), getResources().getString(R.string.fast_baseline_new), App.VERTICAL, App.VERTICAL);

        monthOfTreatment = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_month_of_treatment), getResources().getStringArray(R.array.fast_number_list), "", App.VERTICAL);

        updateFollowUpMonth();

        specimenType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_specimen_type), getResources().getStringArray(R.array.fast_specimen_type_list), getResources().getString(R.string.fast_sputum), App.VERTICAL, App.VERTICAL);

        specimenSource = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_where_did_the_specimen_come_from), getResources().getStringArray(R.array.fast_specimen_come_from_list), getResources().getString(R.string.fast_lymph), App.VERTICAL, App.VERTICAL);

        specimenSourceOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        dstResult = new MyTextView(context, getResources().getString(R.string.fast_dst_result));
        dstResult.setTypeface(null, Typeface.BOLD);

        // dateTestResult = new TitledButton(context, null, getResources().getString(R.string.fast_date_of_result_recieved), DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);

        dstMedium = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_type_of_media_for_dst), getResources().getStringArray(R.array.fast_dst_medium_list), getResources().getString(R.string.fast_lowenstein_jensen), App.VERTICAL);

        inh02Resistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_isoniazid_0_2_ml_result), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        inh1Resistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_isoniazid_1_ml_result), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        rifResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_rifampicin), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        etbResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_ethambuthol), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        smResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_streptomycin), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        pzaResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_pyrazinamide), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        ofxResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_ofloxacin), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        levoResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_levofloxacin), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        moxi05Resistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_moxifloxacin_05), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        moxi2Resistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_moxifloxacin_2), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        amkResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_amikacin), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        kmResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_kanamycin), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        cmResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_capreomycin), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        ethioResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_ethionamide), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        csResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_cycloserine), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        pasResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_p_aminosalicylic_acid), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        bdqResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_bedaquiline), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        dlmResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_delamanid), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        lzdResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_linezolid), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        cfzResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_clofazamine), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        otherDrugName = new TitledEditText(context, null, getResources().getString(R.string.fast_other_drug_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        otherDrugResult = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_other_drug_result), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), getResources().getString(R.string.fast_susceptible), App.VERTICAL, App.VERTICAL);

        orderId = new TitledEditText(context, null, getResources().getString(R.string.order_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
      //  orderId.setLongClickable(false);
        orderIds = new TitledSpinner(context, "", getResources().getString(R.string.order_id), getResources().getStringArray(R.array.pet_empty_array), "", App.HORIZONTAL);


        // Used for reset fields...
        views = new View[]{testId.getEditText(), formType.getRadioGroup(), formDate.getButton(), dateOfSubmission.getButton(),
                testContextStatus.getRadioGroup(), monthOfTreatment.getSpinner(), specimenType.getRadioGroup(),
                specimenSource.getRadioGroup(), specimenSourceOther.getEditText(), dstMedium.getSpinner(),
                inh02Resistant.getRadioGroup(), inh1Resistant.getRadioGroup(), rifResistant.getRadioGroup(), etbResistant.getRadioGroup(),
                smResistant.getRadioGroup(), pzaResistant.getRadioGroup(), ofxResistant.getRadioGroup(), levoResistant.getRadioGroup(),
                moxi05Resistant.getRadioGroup(), moxi2Resistant.getRadioGroup(), amkResistant.getRadioGroup(), kmResistant.getRadioGroup(),
                cmResistant.getRadioGroup(), pasResistant.getRadioGroup(), bdqResistant.getRadioGroup(), dlmResistant.getRadioGroup(),
                lzdResistant.getRadioGroup(), cfzResistant.getRadioGroup(), otherDrugName.getEditText(), otherDrugResult.getRadioGroup(),
                orderId.getEditText(), orderIds.getSpinner()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formType,formDate, orderId, dstOrder, dateOfSubmission, testContextStatus, monthOfTreatment, specimenType,
                        specimenSource, specimenSourceOther, dstResult, orderIds, testId, dstMedium, inh02Resistant, inh1Resistant,
                        rifResistant, etbResistant, smResistant, pzaResistant, ofxResistant, levoResistant, moxi05Resistant, moxi2Resistant
                        , amkResistant, kmResistant, cmResistant, pasResistant, bdqResistant, dlmResistant, lzdResistant, cfzResistant,
                        otherDrugName, otherDrugResult}};

        formDate.getButton().setOnClickListener(this);
         dateOfSubmission.getButton().setOnClickListener(this);
        //dateTestResult.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        specimenSource.getRadioGroup().setOnCheckedChangeListener(this);
        testContextStatus.getRadioGroup().setOnCheckedChangeListener(this);
        specimenType.getRadioGroup().setOnCheckedChangeListener(this);
        orderIds.getSpinner().setOnItemSelectedListener(this);

        otherDrugName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(charSequence.toString().equals("")) && formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_result))) {
                    otherDrugResult.setVisibility(View.VISIBLE);
                } else {
                    otherDrugResult.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        resetViews();

    }

    public void updateFollowUpMonth() {

        String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "REGISTRATION DATE");
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
            }

            else if(diffMonth > 24){
                monthArray = new String[24];
                for (int i = 0; i < 24; i++) {
                    monthArray[i] = String.valueOf(i+1);
                }
                monthOfTreatment.getSpinner().setSpinnerData(monthArray);
            }

            else {
                monthArray = new String[diffMonth];
                for (int i = 0; i < diffMonth; i++) {
                    monthArray[i] = String.valueOf(i+1);
                }
                monthOfTreatment.getSpinner().setSpinnerData(monthArray);
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

                if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.fast_result))) {

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
                } else if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.fast_order))) {
                    String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "REGISTRATION DATE");
                    if (treatmentDate != null) {
                        treatDateCalender = App.getCalendar(App.stringToDate(treatmentDate, "yyyy-MM-dd"));
                        if (formDateCalendar.before(treatDateCalender)) {
                            formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                            snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_cannot_be_before_treatment_initiation_form), Snackbar.LENGTH_INDEFINITE);
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

            if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.fast_result))) {

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
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                dateOfSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }

          /*  else if (secondDateCalendar.before(formDateCalendar)) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_order_date_cannot_be_before_form_date), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                dateOfSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }*/


            else
                dateOfSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }
       /* if (!(dateTestResult.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString()))) {

            String formDa = dateTestResult.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (thirdDateCalendar.after(App.getCalendar(date))) {

                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                dateTestResult.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());

            }
            else if (thirdDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
                thirdDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                dateTestResult.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
            }

            else
                dateTestResult.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());
        }*/

        updateFollowUpMonth();
        formDate.getButton().setEnabled(true);
        dateOfSubmission.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = false;
        Boolean formCheck = false;

        if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order)) || formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_result))) {

        } else {
            formCheck = true;
            error = true;
        }

        if (testId.getVisibility() == View.VISIBLE && testId.getEditText().getText().toString().length() > 0 && testId.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            testId.getEditText().setError(getString(R.string.empty_field));
            testId.getEditText().requestFocus();
            error = true;
        }

        if (specimenSourceOther.getVisibility() == View.VISIBLE && specimenSourceOther.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            specimenSourceOther.getEditText().setError(getString(R.string.empty_field));
            specimenSourceOther.getEditText().requestFocus();
            error = true;
        }

        if (otherDrugName.getVisibility() == View.VISIBLE && otherDrugName.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherDrugName.getEditText().setError(getString(R.string.empty_field));
            otherDrugName.getEditText().requestFocus();
            error = true;
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


        if (orderIds.getVisibility() == View.VISIBLE && flag) {
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "DST Culture Test Result", "ORDER ID");
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
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "DST Culture Test Result", "TEST ID");
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
            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            if (formCheck) {
                alertDialog.setMessage(getString(R.string.fast_please_select_form_type));
            } else {
                alertDialog.setMessage(getString(R.string.form_error));
            }
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

        if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))) {

            observations.add(new String[]{"ORDER ID", App.get(orderId)});

            if (formDate.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"DATE TEST ORDERED", App.getSqlDateTime(formDateCalendar)});
            if (dateOfSubmission.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"SPECIMEN SUBMISSION DATE", App.getSqlDateTime(secondDateCalendar)});
            if (testContextStatus.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"TEST CONTEXT STATUS", App.get(testContextStatus).equals(getResources().getString(R.string.fast_baseline_new)) ? "BASELINE" :
                        (App.get(testContextStatus).equals(getResources().getString(R.string.fast_baseline_repeat)) ? "BASELINE REPEAT" : "REGULAR FOLLOW UP")});
            if (monthOfTreatment.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"FOLLOW-UP MONTH", monthOfTreatment.getSpinner().getSelectedItem().toString()});

            if (specimenType.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"SPECIMEN TYPE", App.get(specimenType).equals(getResources().getString(R.string.fast_sputum)) ? "SPUTUM" : "EXTRA-PULMONARY TUBERCULOSIS"});

            if (specimenSource.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"SPECIMEN SOURCE", App.get(specimenSource).equals(getResources().getString(R.string.fast_lymph)) ? "LYMPHOCYTES" :
                        (App.get(specimenSource).equals(getResources().getString(R.string.fast_pleural_fluid)) ? "PLEURAL EFFUSION" :
                                (App.get(specimenSource).equals(getResources().getString(R.string.fast_pus)) ? "PUS" : "OTHER SPECIMEN SOURCE"))});

            if (specimenSourceOther.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"OTHER SPECIMEN SOURCE", App.get(specimenSourceOther)});
        } else {

            observations.add(new String[]{"ORDER ID", App.get(orderIds)});

            if (testId.getVisibility() == View.VISIBLE && !App.get(testId).isEmpty())
                observations.add(new String[]{"TEST ID", App.get(testId)});

            observations.add(new String[]{"DATE OF  TEST RESULT RECEIVED", App.getSqlDateTime(formDateCalendar)});
            observations.add(new String[]{"CULTURE MEDIUM TYPE", App.get(dstMedium).equals(getResources().getString(R.string.fast_lowenstein_jensen)) ? "LOWENSTEIN-JENSEN MYCOBACTERIA CULTURE METHOD" :
                    (App.get(dstMedium).equals(getResources().getString(R.string.fast_mycobacteria_growth_indicator_tube)) ? "MYCOBACTERIA GROWTH INDICATOR TUBE" :
                            (App.get(dstMedium).equals(getResources().getString(R.string.fast_middlebrook_7h11s)) ? "MIDDLEBROOK 7H11S" :
                                    (App.get(dstMedium).equals(getResources().getString(R.string.fast_total_laboratory_automation)) ? "TOTAL LABORATORY AUTOMATION" :
                                            "OTHER DST MEDIUM")))});

            observations.add(new String[]{"ISONIAZID 0.2 µg/ml RESISTANT", App.get(inh02Resistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(inh02Resistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"ISONIAZID 1 µg/ml RESISTANT RESULT", App.get(inh1Resistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(inh1Resistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"RIFAMPICIN RESISTANT", App.get(rifResistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(rifResistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"ETHAMBUTOL RESISTANT", App.get(etbResistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(etbResistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"STREPTOMYCIN RESISITANT", App.get(smResistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(smResistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"PYRAZINAMIDE RESISTANT", App.get(pzaResistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(pzaResistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"OFLOAXCIN RESISTANT", App.get(ofxResistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(ofxResistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"LEVOFLOXACIN RESISTANT", App.get(levoResistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(levoResistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"MOXIFLOXACIN 0.5 µg/ml RESISTANT RESULT", App.get(moxi05Resistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(moxi05Resistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"MOXIFLOXACIN 2 µg/ml RESISTANT RESULT", App.get(moxi2Resistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(moxi2Resistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"AMIKACIN RESISTANT", App.get(amkResistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(amkResistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"KANAMYCIN RESISTANT", App.get(kmResistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(kmResistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"CAPREOMYCIN RESISTANT", App.get(cmResistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(cmResistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"ETHIONAMIDE RESISTANT Ethionamide", App.get(ethioResistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(ethioResistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"CYCLOSERINE RESISTANT", App.get(csResistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(csResistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"P AMINOSALICYLIC ACID RESISTANT", App.get(pasResistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(pasResistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"BEDAQUILINE RESISTANT", App.get(bdqResistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(bdqResistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"DELAMANID RESISTANT", App.get(dlmResistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(dlmResistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"LINEZOLID RESISTANT", App.get(lzdResistant).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                    (App.get(lzdResistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"CLOFAZAMINE RESISTANT", App.get(cfzResistant).equals(getResources().getString(R.string.fast_resistant)) ? "SUSCEPTIBLE" :
                    (App.get(cfzResistant).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});

            observations.add(new String[]{"OTHER DRUG NAME", App.get(otherDrugName)});

            if (otherDrugResult.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"OTHER DRUG RESISTANT RESULT", App.get(otherDrugResult).equals(getResources().getString(R.string.fast_susceptible)) ? "SUSCEPTIBLE" :
                        (App.get(otherDrugResult).equals(getResources().getString(R.string.fast_resistant)) ? "RESISTANT" : "INDETERMINATE")});
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

                if (App.get(formType).equals(getResources().getString(R.string.fast_order))) {
                    result = serverService.saveEncounterAndObservation(App.getProgram()+"-"+"DST Culture Test Order", FORM, formDateCalendar, observations.toArray(new String[][]{}), true);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                } else if (App.get(formType).equals(getResources().getString(R.string.fast_result))) {
                    result = serverService.saveEncounterAndObservation(App.getProgram()+"-"+"DST Culture Test Result", FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
                    if (result.contains("SUCCESS"))
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

        HashMap<String, String> formValues = new HashMap<String, String>();

      /*  formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        formValues.put(lastName.getTag(), App.get(lastName));
        formValues.put(husbandName.getTag(), App.get(husbandName));
        formValues.put(gender.getTag(), App.get(gender));

        serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);*/

        return true;
    }

    void showTestOrderOrTestResult() {
        //formDate.setVisibility(View.VISIBLE);
        if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))) {
            dstOrder.setVisibility(View.VISIBLE);
           // formDate.getQuestionView().setText(getResources().getString(R.string.fast_test_date));
            dateOfSubmission.setVisibility(View.VISIBLE);
            testContextStatus.setVisibility(View.VISIBLE);
            if (testContextStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_followup_test))) {
                monthOfTreatment.setVisibility(View.VISIBLE);
                updateFollowUpMonth();
            }
            specimenType.setVisibility(View.VISIBLE);
            if (specimenType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_extra_pulmonary))) {
                specimenSource.setVisibility(View.VISIBLE);

                if (specimenSource.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_other_title))) {
                    specimenSourceOther.setVisibility(View.VISIBLE);
                } else {
                    specimenSourceOther.setVisibility(View.GONE);
                }
            } else {
                specimenSource.setVisibility(View.GONE);
                specimenSourceOther.setVisibility(View.GONE);
            }

            dstResult.setVisibility(View.GONE);
            // dateTestResult.setVisibility(View.GONE);
            dstMedium.setVisibility(View.GONE);
            inh02Resistant.setVisibility(View.GONE);
            inh1Resistant.setVisibility(View.GONE);
            rifResistant.setVisibility(View.GONE);
            etbResistant.setVisibility(View.GONE);
            smResistant.setVisibility(View.GONE);
            pzaResistant.setVisibility(View.GONE);
            ofxResistant.setVisibility(View.GONE);
            levoResistant.setVisibility(View.GONE);
            moxi05Resistant.setVisibility(View.GONE);
            moxi2Resistant.setVisibility(View.GONE);
            amkResistant.setVisibility(View.GONE);
            kmResistant.setVisibility(View.GONE);
            cmResistant.setVisibility(View.GONE);
            ethioResistant.setVisibility(View.GONE);
            csResistant.setVisibility(View.GONE);
            pasResistant.setVisibility(View.GONE);
            bdqResistant.setVisibility(View.GONE);
            dlmResistant.setVisibility(View.GONE);
            lzdResistant.setVisibility(View.GONE);
            cfzResistant.setVisibility(View.GONE);
            otherDrugName.setVisibility(View.GONE);
            otherDrugResult.setVisibility(View.GONE);

            orderId.setVisibility(View.VISIBLE);
            Date nowDate = new Date();
            orderId.getEditText().setText(App.getSqlDateTime(nowDate));
            orderIds.setVisibility(View.GONE);
            testId.setVisibility(View.GONE);
            orderId.getEditText().setKeyListener(null);
            orderId.getEditText().setFocusable(false);

            testContextStatus.getRadioGroup().selectDefaultValue();
            monthOfTreatment.getSpinner().selectDefaultValue();
            specimenType.getRadioGroup().selectDefaultValue();
            specimenSource.getRadioGroup().selectDefaultValue();
            specimenSourceOther.getEditText().setDefaultValue();

        }
        else if(formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_result))) {
           // formDate.getQuestionView().setText(getResources().getString(R.string.fast_date_of_result_recieved));
            dstResult.setVisibility(View.VISIBLE);
            //  dateTestResult.setVisibility(View.VISIBLE);
            dstMedium.setVisibility(View.VISIBLE);
            inh02Resistant.setVisibility(View.VISIBLE);
            inh1Resistant.setVisibility(View.VISIBLE);
            rifResistant.setVisibility(View.VISIBLE);
            etbResistant.setVisibility(View.VISIBLE);
            smResistant.setVisibility(View.VISIBLE);
            pzaResistant.setVisibility(View.VISIBLE);
            ofxResistant.setVisibility(View.VISIBLE);
            levoResistant.setVisibility(View.VISIBLE);
            moxi05Resistant.setVisibility(View.VISIBLE);
            moxi2Resistant.setVisibility(View.VISIBLE);
            amkResistant.setVisibility(View.VISIBLE);
            kmResistant.setVisibility(View.VISIBLE);
            cmResistant.setVisibility(View.VISIBLE);
            ethioResistant.setVisibility(View.VISIBLE);
            csResistant.setVisibility(View.VISIBLE);
            pasResistant.setVisibility(View.VISIBLE);
            bdqResistant.setVisibility(View.VISIBLE);
            dlmResistant.setVisibility(View.VISIBLE);
            lzdResistant.setVisibility(View.VISIBLE);
            cfzResistant.setVisibility(View.VISIBLE);
            otherDrugName.setVisibility(View.VISIBLE);


            dstOrder.setVisibility(View.GONE);
            dateOfSubmission.setVisibility(View.GONE);
            testContextStatus.setVisibility(View.GONE);
            monthOfTreatment.setVisibility(View.GONE);
            specimenType.setVisibility(View.GONE);
            specimenSource.setVisibility(View.GONE);
            specimenSourceOther.setVisibility(View.GONE);

            orderId.setVisibility(View.GONE);
            orderIds.setVisibility(View.VISIBLE);
            testId.setVisibility(View.VISIBLE);

            testId.getEditText().setDefaultValue();
            dstMedium.getSpinner().selectDefaultValue();
            inh02Resistant.getRadioGroup().selectDefaultValue();
            inh1Resistant.getRadioGroup().selectDefaultValue();
            rifResistant.getRadioGroup().selectDefaultValue();
            etbResistant.getRadioGroup().selectDefaultValue();
            smResistant.getRadioGroup().selectDefaultValue();
            pzaResistant.getRadioGroup().selectDefaultValue();
            ofxResistant.getRadioGroup().selectDefaultValue();
            levoResistant.getRadioGroup().selectDefaultValue();
            moxi05Resistant.getRadioGroup().selectDefaultValue();
            moxi2Resistant.getRadioGroup().selectDefaultValue();
            amkResistant.getRadioGroup().selectDefaultValue();
            kmResistant.getRadioGroup().selectDefaultValue();
            cmResistant.getRadioGroup().selectDefaultValue();
            ethioResistant.getRadioGroup().selectDefaultValue();
            csResistant.getRadioGroup().selectDefaultValue();
            pasResistant.getRadioGroup().selectDefaultValue();
            bdqResistant.getRadioGroup().selectDefaultValue();
            dlmResistant.getRadioGroup().selectDefaultValue();
            lzdResistant.getRadioGroup().selectDefaultValue();
            cfzResistant.getRadioGroup().selectDefaultValue();
            otherDrugName.getEditText().setDefaultValue();


            String[] testIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "DST Culture Test Order", "ORDER ID");

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

            if(testIds != null) {
                orderIds.getSpinner().setSpinnerData(testIds);
            }

        }
    }


    @Override
    public void refill(int encounterId) {
        OfflineForm fo = serverService.getOfflineFormById(encounterId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();

        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());


        for (int i = 0; i < obsValue.size(); i++) {
            formDate.setVisibility(View.VISIBLE);
            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            }
            if (fo.getFormName().contains("Order")) {
                formType.getRadioGroup().getButtons().get(0).setChecked(true);
                formType.getRadioGroup().getButtons().get(1).setEnabled(false);

                if(obs[0][0].equals("ORDER ID")){
                    orderId.getEditText().setText(obs[0][1]);
                    orderId.getEditText().setKeyListener(null);
                    orderId.getEditText().setFocusable(false);
                }
                else if (obs[0][0].equals("SPECIMEN SUBMISSION DATE")) {
                    String secondDate = obs[0][1];
                    secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                    dateOfSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                    dateOfSubmission.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("TEST CONTEXT STATUS")) {
                    for (RadioButton rb : testContextStatus.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_baseline_new)) && obs[0][1].equals("BASELINE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_baseline_repeat)) && obs[0][1].equals("BASELINE REPEAT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_followup_test)) && obs[0][1].equals("REGULAR FOLLOW UP")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    testContextStatus.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                    monthOfTreatment.getSpinner().selectValue(obs[0][1]);
                } else if (obs[0][0].equals("SPECIMEN TYPE")) {
                    for (RadioButton rb : specimenType.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_sputum)) && obs[0][1].equals("SPUTUM")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_extra_pulmonary)) && obs[0][1].equals("EXTRA-PULMONARY TUBERCULOSIS")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    specimenType.setVisibility(View.VISIBLE);
                }    else if (obs[0][0].equals("SPECIMEN SOURCE")) {
                    for (RadioButton rb : specimenSource.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_lymph)) && obs[0][1].equals("LYMPHOCYTES")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_pleural_fluid)) && obs[0][1].equals("PLEURAL EFFUSION")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_pus)) && obs[0][1].equals("PUS")) {
                            rb.setChecked(true);
                            break;
                        }
                        else if (rb.getText().equals(getResources().getString(R.string.fast_other_title)) && obs[0][1].equals("OTHER SPECIMEN SOURCE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    specimenSource.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("OTHER SPECIMEN SOURCE")) {
                    specimenSourceOther.getEditText().setText(obs[0][1]);
                }
                submitButton.setEnabled(true);
            } else {
                formType.getRadioGroup().getButtons().get(1).setChecked(true);
                formType.getRadioGroup().getButtons().get(0).setEnabled(false);

                if (obs[0][0].equals("ORDER ID")) {
                    orderIds.getSpinner().selectValue(obs[0][1]);
                    orderIds.getSpinner().setEnabled(false);
                }

                else if (obs[0][0].equals("TEST ID")) {
                    testId.getEditText().setText(obs[0][1]);
                    testId.getEditText().setEnabled(false);
                }


                else if (obs[0][0].equals("CULTURE MEDIUM TYPE")) {
                    String value = obs[0][1].equals("LOWENSTEIN-JENSEN MYCOBACTERIA CULTURE METHOD") ? getResources().getString(R.string.fast_lowenstein_jensen) :
                            (obs[0][1].equals("MYCOBACTERIA GROWTH INDICATOR TUBE") ? getResources().getString(R.string.fast_mycobacteria_growth_indicator_tube) :
                                    (obs[0][1].equals("MIDDLEBROOK 7H11S") ? getResources().getString(R.string.fast_middlebrook_7h11s) :
                                            (obs[0][1].equals("TOTAL LABORATORY AUTOMATION") ? getResources().getString(R.string.fast_total_laboratory_automation) :
                                                    getResources().getString(R.string.fast_other_title))));
                    dstMedium.getSpinner().selectValue(value);

                } else if (obs[0][0].equals("ISONIAZID 0.2 µg/ml RESISTANT")) {
                    for (RadioButton rb : inh02Resistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    inh02Resistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("ISONIAZID 1 µg/ml RESISTANT RESULT")) {
                    for (RadioButton rb : inh1Resistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    inh1Resistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("RIFAMPICIN RESISTANT")) {
                    for (RadioButton rb : rifResistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    rifResistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("ETHAMBUTOL RESISTANT")) {
                    for (RadioButton rb : etbResistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    etbResistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("STREPTOMYCIN RESISITANT")) {
                    for (RadioButton rb : smResistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    smResistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("PYRAZINAMIDE RESISTANT")) {
                    for (RadioButton rb : pzaResistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    pzaResistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("OFLOAXCIN RESISTANT")) {
                    for (RadioButton rb : ofxResistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    ofxResistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("LEVOFLOXACIN RESISTANT")) {
                    for (RadioButton rb : levoResistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    levoResistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("MOXIFLOXACIN 0.5 µg/ml RESISTANT RESULT")) {
                    for (RadioButton rb : moxi05Resistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    moxi05Resistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("MOXIFLOXACIN 2 µg/ml RESISTANT RESULT")) {
                    for (RadioButton rb : moxi2Resistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    moxi2Resistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("AMIKACIN RESISTANT")) {
                    for (RadioButton rb : amkResistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    amkResistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("KANAMYCIN RESISTANT")) {
                    for (RadioButton rb : kmResistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    kmResistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("CAPREOMYCIN RESISTANT")) {
                    for (RadioButton rb : cmResistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    cmResistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("ETHIONAMIDE RESISTANT Ethionamide")) {
                    for (RadioButton rb : ethioResistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    ethioResistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("CYCLOSERINE RESISTANT")) {
                    for (RadioButton rb : csResistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    csResistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("PAMINOSALICYLIC ACID RESISTANT")) {
                    for (RadioButton rb : pasResistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    pasResistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("BEDAQUILINE RESISTANT")) {
                    for (RadioButton rb : bdqResistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    bdqResistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("DELAMANID RESISTANT")) {
                    for (RadioButton rb : dlmResistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    dlmResistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("LINEZOLID RESISTANT")) {
                    for (RadioButton rb : lzdResistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    lzdResistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("CLOFAZAMINE RESISTANT")) {
                    for (RadioButton rb : cfzResistant.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                    cfzResistant.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("OTHER DRUG NAME")) {
                    otherDrugName.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("OTHER DRUG RESISTANT RESULT")) {
                    for (RadioButton rb : otherDrugResult.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.fast_susceptible)) && obs[0][1].equals("SUSCEPTIBLE")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_resistant)) && obs[0][1].equals("RESISTANT")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.fast_indeterminate)) && obs[0][1].equals("INDETERMINATE")) {
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
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
        }

        if (view == dateOfSubmission.getButton()) {
            dateOfSubmission.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
        }

      /*  if (view == dateTestResult.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
        }*/


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
        formDate.setVisibility(View.GONE);
        dateOfSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        // dateTestResult.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString());

        testId.setVisibility(View.GONE);
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

    void goneVisibility() {
        //formDate.setVisibility(View.GONE);
        dstOrder.setVisibility(View.GONE);
        dateOfSubmission.setVisibility(View.GONE);
        testContextStatus.setVisibility(View.GONE);
        monthOfTreatment.setVisibility(View.GONE);
        specimenType.setVisibility(View.GONE);
        specimenSource.setVisibility(View.GONE);
        specimenSourceOther.setVisibility(View.GONE);
        dstResult.setVisibility(View.GONE);
        // dateTestResult.setVisibility(View.GONE);
        dstMedium.setVisibility(View.GONE);
        inh02Resistant.setVisibility(View.GONE);
        inh1Resistant.setVisibility(View.GONE);
        rifResistant.setVisibility(View.GONE);
        etbResistant.setVisibility(View.GONE);
        smResistant.setVisibility(View.GONE);
        pzaResistant.setVisibility(View.GONE);
        ofxResistant.setVisibility(View.GONE);
        levoResistant.setVisibility(View.GONE);
        moxi05Resistant.setVisibility(View.GONE);
        moxi2Resistant.setVisibility(View.GONE);
        amkResistant.setVisibility(View.GONE);
        kmResistant.setVisibility(View.GONE);
        cmResistant.setVisibility(View.GONE);
        ethioResistant.setVisibility(View.GONE);
        csResistant.setVisibility(View.GONE);
        pasResistant.setVisibility(View.GONE);
        bdqResistant.setVisibility(View.GONE);
        dlmResistant.setVisibility(View.GONE);
        lzdResistant.setVisibility(View.GONE);
        cfzResistant.setVisibility(View.GONE);
        otherDrugName.setVisibility(View.GONE);
        otherDrugResult.setVisibility(View.GONE);

        orderIds.setVisibility(View.GONE);
        orderId.setVisibility(View.GONE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;

        if (spinner == orderIds.getSpinner()) {
            updateDisplay();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == formType.getRadioGroup()) {
            if (radioGroup == formType.getRadioGroup()) {
                submitButton.setEnabled(true);
                formDate.setVisibility(View.VISIBLE);
                showTestOrderOrTestResult();
            }
        }

        if (radioGroup == testContextStatus.getRadioGroup()) {
            if (testContextStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_followup_test))) {
                monthOfTreatment.setVisibility(View.VISIBLE);
            } else {
                monthOfTreatment.setVisibility(View.GONE);
            }
        }

        if (radioGroup == specimenSource.getRadioGroup()) {
            if (specimenSource.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_other_title))) {
                specimenSourceOther.setVisibility(View.VISIBLE);
            } else {
                specimenSourceOther.setVisibility(View.GONE);
            }
        }

        if (radioGroup == specimenType.getRadioGroup()) {
            if (specimenType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_extra_pulmonary))) {
                specimenSource.setVisibility(View.VISIBLE);
                if (specimenSource.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_other_title))) {
                    specimenSourceOther.setVisibility(View.VISIBLE);
                } else {
                    specimenSourceOther.setVisibility(View.GONE);
                }
            } else {
                specimenSource.setVisibility(View.GONE);
                specimenSourceOther.setVisibility(View.GONE);
            }
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
