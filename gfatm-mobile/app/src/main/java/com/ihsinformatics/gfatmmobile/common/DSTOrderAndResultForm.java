package com.ihsinformatics.gfatmmobile.common;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
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

public class DSTOrderAndResultForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    //  public static final int THIRD_DATE_DIALOG_ID = 3;

    // protected Calendar thirdDateCalendar;
    //  protected DialogFragment thirdDateFragment;

    Context context;

    // Views...

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
        formName = Forms.DST_CULTURE_TEST;
        form = Forms.dst_order_and_result;

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

        //    thirdDateCalendar = Calendar.getInstance();
        //   thirdDateFragment = new SelectDateFragment();

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        testId = new TitledEditText(context, null, getResources().getString(R.string.fast_test_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false,"TEST ID");
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_select_form_type), getResources().getStringArray(R.array.fast_order_and_result_list), "", App.HORIZONTAL, App.HORIZONTAL);

        orderId = new TitledEditText(context, null, getResources().getString(R.string.order_id), "", "", 40, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true,"ORDER ID");


        dstOrder = new MyTextView(context, getResources().getString(R.string.fast_dst_order));
        dstOrder.setTypeface(null, Typeface.BOLD);

        dateOfSubmission = new TitledButton(context, null, getResources().getString(R.string.fast_date_of_submission), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);

        testContextStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_at_what_point_test_being_done), getResources().getStringArray(R.array.fast_test_being_done_list), getResources().getString(R.string.fast_baseline_new), App.VERTICAL, App.VERTICAL,false,"TEST CONTEXT STATUS",new String[]{ "BASELINE" , "BASELINE REPEAT" , "REGULAR FOLLOW UP"});

        monthOfTreatment = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_month_of_treatment), getResources().getStringArray(R.array.fast_number_list), "", App.VERTICAL,false,"FOLLOW-UP MONTH",getResources().getStringArray(R.array.fast_number_list));

        updateFollowUpMonth();

        specimenType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_specimen_type), getResources().getStringArray(R.array.fast_specimen_type), getResources().getString(R.string.fast_sputum), App.VERTICAL, App.VERTICAL,false,"SPECIMEN TYPE", new String[]{"SPUTUM", "EXTRA-PULMONARY"});

        specimenSource = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_where_did_the_specimen_come_from), getResources().getStringArray(R.array.fast_specimen_come_from_list), getResources().getString(R.string.fast_lymph), App.VERTICAL, App.VERTICAL,false,"SPECIMEN SOURCE",new String[]{ "LYMPHOCYTES" , "PLEURAL EFFUSION" , "PUS" , "OTHER SPECIMEN SOURCE"});

        specimenSourceOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"OTHER SPECIMEN SOURCE");

        dstResult = new MyTextView(context, getResources().getString(R.string.fast_dst_result));
        dstResult.setTypeface(null, Typeface.BOLD);

        // dateTestResult = new TitledButton(context, null, getResources().getString(R.string.fast_date_of_result_recieved), DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);

        dstMedium = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_type_of_media_for_dst), getResources().getStringArray(R.array.fast_dst_medium_list), getResources().getString(R.string.fast_lowenstein_jensen), App.VERTICAL,false,"CULTURE MEDIUM TYPE",new String[]{ "LOWENSTEIN-JENSEN MYCOBACTERIA CULTURE METHOD" , "MYCOBACTERIA GROWTH INDICATOR TUBE" , "MIDDLEBROOK 7H11S" , "TOTAL LABORATORY AUTOMATION" ,"OTHER DST MEDIUM"});

        inh02Resistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_isoniazid_0_2_ml_result), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"ISONIAZID 0.2 µg/ml RESISTANT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        inh1Resistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_isoniazid_1_ml_result), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"ISONIAZID 1 µg/ml RESISTANT RESULT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        rifResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_rifampicin), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"RIFAMPICIN RESISTANT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        etbResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_ethambuthol), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"ETHAMBUTOL RESISTANT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        smResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_streptomycin), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"STREPTOMYCIN RESISITANT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        pzaResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_pyrazinamide), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"PYRAZINAMIDE RESISTANT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        ofxResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_ofloxacin), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"OFLOAXCIN RESISTANT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        levoResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_levofloxacin), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"LEVOFLOXACIN RESISTANT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        moxi05Resistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_moxifloxacin_05), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"MOXIFLOXACIN 0.5 µg/ml RESISTANT RESULT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        moxi2Resistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_moxifloxacin_2), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"MOXIFLOXACIN 2 µg/ml RESISTANT RESULT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        amkResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_amikacin), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"AMIKACIN RESISTANT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        kmResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_kanamycin), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"KANAMYCIN RESISTANT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        cmResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_capreomycin), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"CAPREOMYCIN RESISTANT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        ethioResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_ethionamide), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"ETHIONAMIDE RESISTANT Ethionamide",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        csResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_cycloserine), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"CYCLOSERINE RESISTANT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        pasResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_p_aminosalicylic_acid), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"P AMINOSALICYLIC ACID RESISTANT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        bdqResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_bedaquiline), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"BEDAQUILINE RESISTANT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        dlmResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_delamanid), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"DELAMANID RESISTANT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        lzdResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_linezolid), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"LINEZOLID RESISTANT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        cfzResistant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_clofazamine), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"CLOFAZAMINE RESISTANT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

        otherDrugName = new TitledEditText(context, null, getResources().getString(R.string.fast_other_drug_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"OTHER DRUG NAME");

        otherDrugResult = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_other_drug_result), getResources().getStringArray(R.array.fast_susceptible_resistant_indeterminate_list), "", App.VERTICAL, App.VERTICAL,false,"OTHER DRUG RESISTANT RESULT",new String[]{ "SUSCEPTIBLE" , "RESISTANT" , "INDETERMINATE"});

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
                        , amkResistant, kmResistant, cmResistant,ethioResistant,csResistant, pasResistant, bdqResistant, dlmResistant, lzdResistant, cfzResistant,
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

        String treatmentDate = serverService.getLatestEncounterDateTime(App.getPatientId(),"FAST" + "-" + "Treatment Initiation");
        if(treatmentDate == null) treatmentDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB" + "-" + "TB Treatment Initiation");
        if(treatmentDate == null) treatmentDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "PET" + "-" + "Treatment Initiation");

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
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else {
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

                if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.fast_result))) {

                    if (!App.get(orderIds).equals("")) {
                        String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), "DST Culture Test Order", "ORDER ID", App.get(orderIds));

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
                    String treatmentDate = serverService.getLatestEncounterDateTime(App.getPatientId(),"FAST" + "-" + "Treatment Initiation");
                    if(treatmentDate == null) treatmentDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB" + "-" + "Treatment Initiation");
                    if(treatmentDate == null) treatmentDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "PET" + "-" + "Treatment Initiation");

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
                    String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), "DST Culture Test Order", "ORDER ID", App.get(orderIds));

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
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
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
            }else {
                flag = true;
            }
        }


        if (orderIds.getVisibility() == View.VISIBLE && flag) {
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), "DST Culture Test Result", "ORDER ID");
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
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), "DST Culture Test Result", "TEST ID");
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
            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(),R.style.dialog).create();
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

        final ArrayList<String[]> observations = getObservations();

        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                Boolean flag = serverService.deleteOfflineForms(encounterId);
                if(!flag){

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

         //   observations.add(new String[]{"ORDER ID", App.get(orderId)});

            if (formDate.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"DATE TEST ORDERED", App.getSqlDateTime(formDateCalendar)});

            if (dateOfSubmission.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"SPECIMEN SUBMISSION DATE", App.getSqlDateTime(secondDateCalendar)});

        } else {

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

                if (App.get(formType).equals(getResources().getString(R.string.fast_order))) {
                    result = serverService.saveEncounterAndObservation("DST Culture Test Order", form, formDateCalendar, observations.toArray(new String[][]{}), true);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                } else if (App.get(formType).equals(getResources().getString(R.string.fast_result))) {
                    result = serverService.saveEncounterAndObservation("DST Culture Test Result", form, formDateCalendar, observations.toArray(new String[][]{}), false);
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

        serverService.saveFormLocally(formName, "12345-5", formValues);*/

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


            String[] testIds = serverService.getAllObsValues(App.getPatientId(), "DST Culture Test Order", "ORDER ID");

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
        super.refill(encounterId);

        OfflineForm fo = serverService.getSavedFormById(encounterId);

        ArrayList<String[][]> obsValue = fo.getObsValue();


        for (int i = 0; i < obsValue.size(); i++) {
            formDate.setVisibility(View.VISIBLE);
            String[][] obs = obsValue.get(i);

            if (fo.getFormName().contains("Order")) {
                formType.getRadioGroup().getButtons().get(0).setChecked(true);
                formType.getRadioGroup().getButtons().get(1).setEnabled(false);
                orderId.getEditText().setKeyListener(null);
                orderId.getEditText().setFocusable(false);


                if (obs[0][0].equals("SPECIMEN SUBMISSION DATE")) {
                    String secondDate = obs[0][1];
                    secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                    dateOfSubmission.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                    dateOfSubmission.setVisibility(View.VISIBLE);
                }
                submitButton.setEnabled(true);
            } else {
                formType.getRadioGroup().getButtons().get(1).setChecked(true);
                formType.getRadioGroup().getButtons().get(0).setEnabled(false);

                if (obs[0][0].equals("ORDER ID")) {
                    orderIds.getSpinner().selectValue(obs[0][1]);
                    orderIds.getSpinner().setEnabled(false);
                } else if (obs[0][0].equals("TEST ID")) {
                    testId.getEditText().setText(obs[0][1]);
                    testId.getEditText().setEnabled(false);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {

            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar,false,true, false);

            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);*/
        }

        if (view == dateOfSubmission.getButton()) {
            dateOfSubmission.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar,false,true, true);

            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);*/
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

        String[] testIds = serverService.getAllObsValues(App.getPatientId(), "DST Culture Test Order", "ORDER ID");
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
