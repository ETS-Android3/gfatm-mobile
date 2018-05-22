package com.ihsinformatics.gfatmmobile.common;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import com.ihsinformatics.gfatmmobile.custom.MyEditText;
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
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 2/10/2017.
 */

public class ReferralAndTransferForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    // Views...
    TitledButton formDate;
    TitledRadioGroup referralTransfer;
    TitledSpinner reasonReferralTransfer;
    TitledEditText reasonReferralTransferOther;

    TitledEditText treatmentStartDate;
    TitledEditText nationalDRTBNumber;
    TitledSpinner currentTreatmentFacility;
    TitledEditText currentTreatmentFacilityOther;
    TitledEditText facilityFocalPersonName;
    LinearLayout facilityFocalPersonNumberLinearLayout;
    MyEditText facilityFocalPersonNumber1;
    MyEditText facilityFocalPersonNumber2;
    TitledEditText facilityTBCoordinatorName;
    LinearLayout facilityTBCoordinatorNumberLinearLayout;
    MyEditText facilityTBCoordinatorNumber1;
    MyEditText facilityTBCoordinatorNumber2;
    TitledEditText referringClinicianName;

    TitledSpinner referralSite;
    TitledEditText referralSiteOther;

    TitledRadioGroup treatmentInitiatedAtReferralAndTransferSite;
    TitledSpinner reasonTreatmentNotInitiated;
    TitledEditText reasonTreatmentNotInitiatedOther;
    TitledRadioGroup drConfirmation;
    TitledEditText enrsId;

    MyTextView endFollowupInstruction;
    TitledEditText contactName;
    LinearLayout mobileLinearLayout;
    MyEditText mobile1;
    MyEditText mobile2;

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

        pageCount = 3;
        formName = Forms.REFERRAL_AND_TRANSFER_FORM;
        form = Forms.referralAndTransferForm;

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

        String columnName = "";
        if (App.getProgram().equals(getResources().getString(R.string.pet)))
            columnName = "pet_location";
        else if (App.getProgram().equals(getResources().getString(R.string.fast)))
            columnName = "fast_location";
        else if (App.getProgram().equals(getResources().getString(R.string.comorbidities)))
            columnName = "comorbidities_location";
        else if (App.getProgram().equals(getResources().getString(R.string.pmdt)))
            columnName = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.childhood_tb)))
            columnName = "childhood_tb_location";

        final Object[][] locations = serverService.getAllLocationsFromLocalDB(columnName);
        String[] locationArray = new String[locations.length + 1];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][16]);
        }
        locationArray[locationArray.length - 1] = getResources().getString(R.string.fast_other_title);

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        referralTransfer = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_patient_being_referred_out_or_transferred_out), getResources().getStringArray(R.array.fast_referral_transfer_list), getResources().getString(R.string.fast_referral), App.VERTICAL, App.VERTICAL);
        reasonReferralTransfer = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_reason_for_referral_transfer), getResources().getStringArray(R.array.reason_referral_transfer_list), getResources().getString(R.string.fast_patient_choose_another_facility), App.VERTICAL);
        reasonReferralTransferOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        treatmentStartDate = new TitledEditText(context, null, getResources().getString(R.string.treatment_start_date), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        treatmentStartDate.getEditText().setOnKeyListener(null);
        nationalDRTBNumber = new TitledEditText(context, null, getResources().getString(R.string.national_drtb_number), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        nationalDRTBNumber.getEditText().setOnKeyListener(null);
        currentTreatmentFacility = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.current_treatment_facility), locationArray, "", App.VERTICAL);
        currentTreatmentFacilityOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        facilityFocalPersonName = new TitledEditText(context, null, getResources().getString(R.string.facility_focal_person_name), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        facilityFocalPersonNumberLinearLayout = new LinearLayout(context);
        facilityFocalPersonNumberLinearLayout.setOrientation(LinearLayout.VERTICAL);
        MyTextView landlineLabel = new MyTextView(context, getResources().getString(R.string.facility_focal_person_number));
        facilityFocalPersonNumberLinearLayout.addView(landlineLabel);
        LinearLayout landlineNumberPart = new LinearLayout(context);
        landlineNumberPart.setOrientation(LinearLayout.HORIZONTAL);
        facilityFocalPersonNumber1 = new MyEditText(context, "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        facilityFocalPersonNumber1.setHint("0XXX");
        landlineNumberPart.addView(facilityFocalPersonNumber1);
        MyTextView landlineNumberDash = new MyTextView(context, " - ");
        landlineNumberPart.addView(landlineNumberDash);
        facilityFocalPersonNumber2 = new MyEditText(context, "", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        facilityFocalPersonNumber2.setHint("XXXXXXX");
        landlineNumberPart.addView(facilityFocalPersonNumber2);
        facilityFocalPersonNumberLinearLayout.addView(landlineNumberPart);
        facilityTBCoordinatorName  = new TitledEditText(context, null, getResources().getString(R.string.facility_tb_cordinator_name), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        facilityTBCoordinatorNumberLinearLayout = new LinearLayout(context);
        facilityTBCoordinatorNumberLinearLayout.setOrientation(LinearLayout.VERTICAL);
        MyTextView landlineLabel1 = new MyTextView(context, getResources().getString(R.string.facility_tb_cordinator_number));
        facilityTBCoordinatorNumberLinearLayout.addView(landlineLabel1);
        LinearLayout landlineNumberPart1 = new LinearLayout(context);
        landlineNumberPart1.setOrientation(LinearLayout.HORIZONTAL);
        facilityTBCoordinatorNumber1 = new MyEditText(context, "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        facilityTBCoordinatorNumber1.setHint("0XXX");
        landlineNumberPart1.addView(facilityTBCoordinatorNumber1);
        MyTextView landlineNumberDash1 = new MyTextView(context, " - ");
        landlineNumberPart1.addView(landlineNumberDash1);
        facilityTBCoordinatorNumber2 = new MyEditText(context, "", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        facilityTBCoordinatorNumber2.setHint("XXXXXXX");
        landlineNumberPart1.addView(facilityTBCoordinatorNumber2);
        facilityTBCoordinatorNumberLinearLayout.addView(landlineNumberPart1);
        referringClinicianName  = new TitledEditText(context, null, getResources().getString(R.string.referring_clinician_name), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);

        referralSite = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_location_for_referral_transfer), locationArray, "", App.VERTICAL);
        referralSiteOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        treatmentInitiatedAtReferralAndTransferSite = new TitledRadioGroup(context, null, getResources().getString(R.string.treatment_initiated_at_referrral_trasfer_site), getResources().getStringArray(R.array.yes_no_unknown_options), getResources().getString(R.string.unknown), App.VERTICAL, App.VERTICAL);
        reasonTreatmentNotInitiated = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.reason_treatment_not_initiated), getResources().getStringArray(R.array.reason_treatment_not_initiated_list), "", App.VERTICAL, true);
        reasonTreatmentNotInitiatedOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        drConfirmation = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_dr_confirmation), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        enrsId = new TitledEditText(context, null, getResources().getString(R.string.fast_enrs_number), "", "", 20, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);

        endFollowupInstruction = new MyTextView(context, getResources().getString(R.string.fast_end_followup_instruction));
        endFollowupInstruction.setTextColor(Color.BLACK);
        endFollowupInstruction.setTypeface(null, Typeface.NORMAL);

        contactName = new TitledEditText(context, null, getResources().getString(R.string.contact_person_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);

        mobileLinearLayout = new LinearLayout(context);
        mobileLinearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout mobileQuestion = new LinearLayout(context);
        mobileQuestion.setOrientation(LinearLayout.HORIZONTAL);
        TextView mandatorySign = new TextView(context);
        mandatorySign.setText(" ");
        mandatorySign.setTextColor(Color.parseColor("#ff0000"));
        mobileQuestion.addView(mandatorySign);
        MyTextView mobileNumberLabel = new MyTextView(context, getResources().getString(R.string.contact_number));
        mobileQuestion.addView(mobileNumberLabel);
        mobileLinearLayout.addView(mobileQuestion);
        LinearLayout mobileNumberPart = new LinearLayout(context);
        mobileNumberPart.setOrientation(LinearLayout.HORIZONTAL);
        mobile1 = new MyEditText(context,"", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        mobile1.setHint("0XXX");
        mobileNumberPart.addView(mobile1);
        MyTextView mobileNumberDash = new MyTextView(context, " - ");
        mobileNumberPart.addView(mobileNumberDash);
        mobile2 = new MyEditText(context,"",  7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        mobile2.setHint("XXXXXXX");
        mobileNumberPart.addView(mobile2);
        mobileLinearLayout.addView(mobileNumberPart);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), referralTransfer.getRadioGroup(), reasonReferralTransfer.getSpinner(),
                referralSite.getSpinner(), reasonReferralTransferOther.getEditText(), referralSiteOther.getEditText(),
                treatmentInitiatedAtReferralAndTransferSite.getRadioGroup(), reasonTreatmentNotInitiated.getSpinner(),
                reasonTreatmentNotInitiatedOther.getEditText(), drConfirmation.getRadioGroup(), enrsId.getEditText(),
                contactName.getEditText(), mobile1, mobile2, treatmentStartDate.getEditText(), nationalDRTBNumber.getEditText(),
                currentTreatmentFacility.getSpinner(), facilityFocalPersonName.getEditText(), facilityFocalPersonNumber1,
                facilityFocalPersonNumber2, facilityTBCoordinatorName.getEditText(), facilityTBCoordinatorNumber1,
                facilityTBCoordinatorNumber2, referringClinicianName.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, referralTransfer, reasonReferralTransfer, reasonReferralTransferOther, treatmentStartDate, nationalDRTBNumber, currentTreatmentFacility, facilityFocalPersonName,
                        facilityFocalPersonNumberLinearLayout, facilityTBCoordinatorName, facilityTBCoordinatorNumberLinearLayout, referringClinicianName},
                        {referralSite, referralSiteOther, treatmentInitiatedAtReferralAndTransferSite, reasonTreatmentNotInitiated, reasonTreatmentNotInitiatedOther, drConfirmation, enrsId},
                        {endFollowupInstruction, contactName, mobileLinearLayout}};

        formDate.getButton().setOnClickListener(this);
        reasonReferralTransfer.getSpinner().setOnItemSelectedListener(this);
        referralSite.getSpinner().setOnItemSelectedListener(this);
        treatmentInitiatedAtReferralAndTransferSite.getRadioGroup().setOnCheckedChangeListener(this);
        drConfirmation.getRadioGroup().setOnCheckedChangeListener(this);
        reasonTreatmentNotInitiated.getSpinner().setOnItemSelectedListener(this);
        referralTransfer.getRadioGroup().setOnCheckedChangeListener(this);

        mobile2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    mobile1.requestFocus();
                    mobile1.setSelection(mobile1.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        facilityFocalPersonNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    facilityFocalPersonNumber1.requestFocus();
                    facilityFocalPersonNumber1.setSelection(facilityFocalPersonNumber1.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        facilityTBCoordinatorNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    facilityTBCoordinatorNumber1.requestFocus();
                    facilityTBCoordinatorNumber1.setSelection(facilityTBCoordinatorNumber1.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobile1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==4){
                    mobile2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        facilityFocalPersonNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==4){
                    facilityFocalPersonNumber2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        facilityTBCoordinatorNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==4){
                    facilityTBCoordinatorNumber2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        resetViews();
    }

    @Override
    public void updateDisplay() {
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
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }

        formDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = false;



        if (reasonReferralTransferOther.getVisibility() == View.VISIBLE && reasonReferralTransferOther.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reasonReferralTransferOther.getEditText().setError(getString(R.string.empty_field));
            reasonReferralTransferOther.getEditText().requestFocus();
            error = true;
        }

        if (referralSiteOther.getVisibility() == View.VISIBLE && referralSiteOther.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            referralSiteOther.getEditText().setError(getString(R.string.empty_field));
            referralSiteOther.getEditText().requestFocus();
            error = true;
        }

        if (reasonTreatmentNotInitiated.getVisibility() == View.VISIBLE && App.get(reasonTreatmentNotInitiated).equals("")) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(1);
            reasonTreatmentNotInitiated.getQuestionView().setError(getString(R.string.empty_field));
            reasonTreatmentNotInitiated.requestFocus();
            error = true;
        } else reasonTreatmentNotInitiated.getQuestionView().setError(null);


        if (reasonTreatmentNotInitiatedOther.getVisibility() == View.VISIBLE && reasonTreatmentNotInitiatedOther.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(1);
            reasonTreatmentNotInitiatedOther.getEditText().setError(getString(R.string.empty_field));
            reasonTreatmentNotInitiatedOther.getEditText().requestFocus();
            error = true;
        }

        if (enrsId.getVisibility() == View.VISIBLE && enrsId.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(1);
            enrsId.getEditText().setError(getString(R.string.empty_field));
            enrsId.getEditText().requestFocus();
            error = true;
        }

        if(App.get(mobile1).equals("") && App.get(mobile2).equals("")){

            mobile2.setError(null);
        }
        else{

            String mobile = App.get(mobile1) + App.get(mobile2);
            if(!RegexUtil.isLandlineNumber(mobile)){
                mobile2.setError(getString(R.string.ctb_invalid_number));
                mobile2.requestFocus();
                error = true;
            }
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
           // DrawableCompat.setTint(clearIcon, color);
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
        final HashMap<String, String> personAttribute = new HashMap<String, String>();
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
        if (referralTransfer.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT BEING REFEREED OUT OR TRANSFERRED OUT", App.get(referralTransfer).equals(getResources().getString(R.string.fast_referral)) ? "PATIENT REFERRED" : "PATIENT TRANSFERRED OUT"});

        if (reasonReferralTransfer.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON FOR REFERRAL OR TRANSFER", App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_patient_choose_another_facility)) ? "PATIENT CHOOSE ANOTHER FACILITY" :
                    (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_drtb_suspect)) ? "MULTI-DRUG RESISTANT TUBERCULOSIS SUSPECTED" :
                            (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_drtb)) ? "DRUG RESISTANT TUBERCULOSIS" :
                                    (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_treatment_failure)) ? "TUBERCULOSIS TREATMENT FAILURE" :
                                            (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_complicated_tb)) ? "COMPLICATED TUBERCULOSIS" :
                                                    (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_mycobacterium_other_than_tb)) ? "MYCOBACTERIUM TUBERCULOSIS" :
                                                            (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.treatment_initiation)) ? "TREATMENT INITIATION" :
                                                                    (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.infection_control)) ? "INFECTION CONTROL" :
                                                                            (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.adverse_event)) ? "ADVERSE EVENTS" :
                                                                                    (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.comrbidity)) ? "COMORBIDITY" :
                                                                                            (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.severe_clinical_condition)) ? "SEVERE CLINICAL CONDITION" :
                                                                                                    (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.surgical_operation)) ? "SURGICAL OPERATION" :
                                                                                                            (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.patient_behavior)) ? "PATIENT BEHAVIOR" :
                                                                                                                    (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.social_reason)) ? "SOCIAL REASON" :
                                                                                                                            (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.hospitalization)) ? "PATIENT CURRENTLY INPATIENT AT FACILITY" : "OTHER TRANSFER OR REFERRAL REASON" ))))))))))))))});

        if (reasonReferralTransferOther.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"OTHER TRANSFER OR REFERRAL REASON", App.get(reasonReferralTransferOther)});
        }

        if (treatmentStartDate.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"", App.get(treatmentStartDate)});

        if (nationalDRTBNumber.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"", App.get(nationalDRTBNumber)});

        if (currentTreatmentFacility.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"", App.get(currentTreatmentFacility)});

        if (facilityFocalPersonName.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"", App.get(facilityFocalPersonName)});

        if (facilityFocalPersonNumberLinearLayout.getVisibility() == View.VISIBLE) {
            if(!App.get(facilityFocalPersonNumber1).equals("")){

                String number = App.get(facilityFocalPersonNumber1)+ '-' + App.get(facilityFocalPersonNumber2);
                observations.add(new String[]{"", number});

            }
        }

        if (facilityTBCoordinatorName.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"", App.get(facilityTBCoordinatorName)});

        if (facilityTBCoordinatorNumberLinearLayout.getVisibility() == View.VISIBLE) {
            if(!App.get(facilityTBCoordinatorNumber1).equals("")){

                String number = App.get(facilityTBCoordinatorNumber1)+ '-' + App.get(facilityTBCoordinatorNumber2);
                observations.add(new String[]{"", number});

            }
        }

        if (referringClinicianName.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"", App.get(referringClinicianName)});

        if (referralSite.getVisibility() == View.VISIBLE) {
            if(App.get(referralSite).equals(getString(R.string.fast_other_title)))
                observations.add(new String[]{"REFERRING FACILITY NAME", App.get(referralSite)});
            else{
                String location = serverService.getLocationNameFromDescription(App.get(referralSite));
                observations.add(new String[]{"REFERRING FACILITY NAME", location});
            }

            personAttribute.put("Health Center",serverService.getLocationUuid(App.get(referralSite)));
        }

        if (referralSiteOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"LOCATION OF REFERRAL OR TRANSFER OTHER", App.get(referralSiteOther)});

        if(treatmentInitiatedAtReferralAndTransferSite.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT INITIATED AT REFERRAL OR TRANSFER SITE",  App.get(treatmentInitiatedAtReferralAndTransferSite).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(treatmentInitiatedAtReferralAndTransferSite).equals(getResources().getString(R.string.no)) ? "NO" : "UNKNOWN") });

        if (reasonTreatmentNotInitiated.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT NOT INITIATED AT REFERRAL OR TRANSFER SITE", App.get(reasonTreatmentNotInitiated).equals(getResources().getString(R.string.patient_could_not_contacted)) ? "PATIENT COULD NOT BE CONTACTED" :
                    (App.get(reasonTreatmentNotInitiated).equals(getResources().getString(R.string.patient_left_city)) ? "PATIENT LEFT THE CITY" :
                            (App.get(reasonTreatmentNotInitiated).equals(getResources().getString(R.string.patient_refused_treatment)) ? "REFUSAL OF TREATMENT BY PATIENT" :
                                    (App.get(reasonTreatmentNotInitiated).equals(getResources().getString(R.string.patient_died)) ? "DIED" :
                                            (App.get(reasonTreatmentNotInitiated).equals(getResources().getString(R.string.dr_not_confirmed)) ? "DR NOT CONFIRMED BY BASELINE REPEAT TEST" : "OTHER REASON FOR TREATMENT NOT INITIATED" ))))});

        if(reasonTreatmentNotInitiatedOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON FOR TREATMENT NOT INITIATED", App.get(reasonTreatmentNotInitiatedOther)});

        if(drConfirmation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DRUG RESISTANCE CONFIRMATION",  App.get(drConfirmation).equals(getResources().getString(R.string.yes)) ? "YES" : "NO" });

        if (enrsId.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"LOCATION OF REFERRAL OR TRANSFER OTHER", App.get(enrsId)});

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

                String result = serverService.saveEncounterAndObservation("Referral and Transfer Form", form, formDateCalendar, observations.toArray(new String[][]{}), false);
                if (!result.contains("SUCCESS"))
                    return result;
                else {

                    String encounterId = "";

                    if (result.contains("_")) {
                        String[] successArray = result.split("_");
                        encounterId = successArray[1];
                    }

                    result = serverService.saveMultiplePersonAttribute(personAttribute, encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

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

    @Override
    public void refill(int formId) {

        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if(obs[0][0].equals("TIME TAKEN TO FILL form")){
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("PATIENT BEING REFEREED OUT OR TRANSFERRED OUT")) {

                for (RadioButton rb : referralTransfer.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_referral)) && obs[0][1].equals("PATIENT REFERRED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_transfer)) && obs[0][1].equals("PATIENT TRANSFERRED OUT")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                referralTransfer.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REASON FOR REFERRAL OR TRANSFER")) {
                String value = obs[0][1].equals("PATIENT CHOOSE ANOTHER FACILITY") ? getResources().getString(R.string.fast_patient_choose_another_facility) :
                        (obs[0][1].equals("MULTI-DRUG RESISTANT TUBERCULOSIS SUSPECTED") ? getResources().getString(R.string.fast_drtb_suspect) :
                                (obs[0][1].equals("DRUG RESISTANT TUBERCULOSIS") ? getResources().getString(R.string.fast_drtb) :
                                        (obs[0][1].equals("TUBERCULOSIS TREATMENT FAILURE") ? getResources().getString(R.string.fast_treatment_failure) :
                                                (obs[0][1].equals("COMPLICATED TUBERCULOSIS") ? getResources().getString(R.string.fast_complicated_tb) :
                                                        (obs[0][1].equals("MYCOBACTERIUM TUBERCULOSIS") ? getResources().getString(R.string.fast_mycobacterium_other_than_tb) :
                                                                getResources().getString(R.string.fast_other_title))))));

                reasonReferralTransfer.getSpinner().selectValue(value);
                reasonReferralTransfer.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER TRANSFER OR REFERRAL REASON")) {
                reasonReferralTransferOther.getEditText().setText(obs[0][1]);
                reasonReferralTransferOther.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REFERRING FACILITY NAME")) {
                referralSite.getSpinner().selectValue(obs[0][1]);
                referralSite.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("LOCATION OF REFERRAL OR TRANSFER OTHER")) {
                referralSiteOther.getEditText().setText(obs[0][1]);
                referralSiteOther.setVisibility(View.VISIBLE);
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
        if (spinner == reasonReferralTransfer.getSpinner()) {
            reasonReferralTransferOther.setVisibility(View.GONE);
            treatmentStartDate.setVisibility(View.GONE);
            nationalDRTBNumber.setVisibility(View.GONE);
            currentTreatmentFacility.setVisibility(View.GONE);
            currentTreatmentFacilityOther.setVisibility(View.GONE);
            facilityFocalPersonName.setVisibility(View.GONE);
            facilityFocalPersonNumberLinearLayout.setVisibility(View.GONE);
            facilityTBCoordinatorName.setVisibility(View.GONE);
            facilityTBCoordinatorNumberLinearLayout.setVisibility(View.GONE);
            referringClinicianName.setVisibility(View.GONE);
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                reasonReferralTransferOther.setVisibility(View.VISIBLE);
            } else  if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_drtb))) {
                if(App.get(referralTransfer).equals(getString(R.string.fast_transfer))){

                    treatmentStartDate.setVisibility(View.VISIBLE);
                    nationalDRTBNumber.setVisibility(View.VISIBLE);
                    currentTreatmentFacility.setVisibility(View.VISIBLE);
                    if(App.get(currentTreatmentFacility).equals(getString(R.string.other)))
                        currentTreatmentFacilityOther.setVisibility(View.VISIBLE);
                    facilityFocalPersonName.setVisibility(View.VISIBLE);
                    facilityFocalPersonNumberLinearLayout.setVisibility(View.VISIBLE);
                    facilityTBCoordinatorName.setVisibility(View.VISIBLE);
                    facilityTBCoordinatorNumberLinearLayout.setVisibility(View.VISIBLE);
                    referringClinicianName.setVisibility(View.VISIBLE);

                }
            }

        } else if (spinner == referralSite.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                referralSiteOther.setVisibility(View.VISIBLE);
            } else {
                referralSiteOther.setVisibility(View.GONE);
            }
        }  else if (spinner == reasonTreatmentNotInitiated.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.other))) {
                reasonTreatmentNotInitiatedOther.setVisibility(View.VISIBLE);
            } else {
                reasonTreatmentNotInitiatedOther.setVisibility(View.GONE);
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
        reasonReferralTransferOther.setVisibility(View.GONE);
        referralSiteOther.setVisibility(View.GONE);
        reasonTreatmentNotInitiated.setVisibility(View.GONE);
        reasonTreatmentNotInitiatedOther.setVisibility(View.GONE);
        drConfirmation.setVisibility(View.GONE);
        enrsId.setVisibility(View.GONE);
        treatmentStartDate.setVisibility(View.GONE);
        nationalDRTBNumber.setVisibility(View.GONE);
        currentTreatmentFacility.setVisibility(View.GONE);
        currentTreatmentFacilityOther.setVisibility(View.GONE);
        facilityFocalPersonName.setVisibility(View.GONE);
        facilityFocalPersonNumberLinearLayout.setVisibility(View.GONE);
        facilityTBCoordinatorName.setVisibility(View.GONE);
        facilityTBCoordinatorNumberLinearLayout.setVisibility(View.GONE);
        referringClinicianName.setVisibility(View.GONE);

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
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if (radioGroup == treatmentInitiatedAtReferralAndTransferSite.getRadioGroup()) {

            reasonTreatmentNotInitiated.setVisibility(View.GONE);
            reasonTreatmentNotInitiatedOther.setVisibility(View.GONE);
            drConfirmation.setVisibility(View.GONE);
            enrsId.setVisibility(View.GONE);

            if (treatmentInitiatedAtReferralAndTransferSite.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                drConfirmation.setVisibility(View.VISIBLE);
                if(App.get(drConfirmation).equals(getResources().getString(R.string.yes)))
                    enrsId.setVisibility(View.VISIBLE);
            } else if (treatmentInitiatedAtReferralAndTransferSite.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.no))){
                reasonTreatmentNotInitiated.setVisibility(View.VISIBLE);
                if(App.get(reasonTreatmentNotInitiated).equals(getResources().getString(R.string.other)))
                    reasonTreatmentNotInitiatedOther.setVisibility(View.VISIBLE);
            }
        } else if (radioGroup == drConfirmation.getRadioGroup()){
            if(App.get(drConfirmation).equals(getResources().getString(R.string.yes)))
                enrsId.setVisibility(View.VISIBLE);
            else
                enrsId.setVisibility(View.GONE);
        } else if (radioGroup == referralTransfer.getRadioGroup()){
            if(App.get(referralTransfer).equals(getResources().getString(R.string.fast_transfer))){
                    if(App.get(reasonReferralTransfer).equals(getString(R.string.fast_drtb))){

                        treatmentStartDate.setVisibility(View.VISIBLE);
                        nationalDRTBNumber.setVisibility(View.VISIBLE);
                        currentTreatmentFacility.setVisibility(View.VISIBLE);
                        if(App.get(currentTreatmentFacility).equals(getString(R.string.other)))
                            currentTreatmentFacilityOther.setVisibility(View.VISIBLE);
                        facilityFocalPersonName.setVisibility(View.VISIBLE);
                        facilityFocalPersonNumberLinearLayout.setVisibility(View.VISIBLE);
                        facilityTBCoordinatorName.setVisibility(View.VISIBLE);
                        facilityTBCoordinatorNumberLinearLayout.setVisibility(View.VISIBLE);
                        referringClinicianName.setVisibility(View.VISIBLE);

                    }
                    else{
                        treatmentStartDate.setVisibility(View.GONE);
                        nationalDRTBNumber.setVisibility(View.GONE);
                        currentTreatmentFacility.setVisibility(View.GONE);
                        currentTreatmentFacilityOther.setVisibility(View.GONE);
                        facilityFocalPersonName.setVisibility(View.GONE);
                        facilityFocalPersonNumberLinearLayout.setVisibility(View.GONE);
                        facilityTBCoordinatorName.setVisibility(View.GONE);
                        facilityTBCoordinatorNumberLinearLayout.setVisibility(View.GONE);
                        referringClinicianName.setVisibility(View.GONE);
                    }
            } else{
                treatmentStartDate.setVisibility(View.GONE);
                nationalDRTBNumber.setVisibility(View.GONE);
                currentTreatmentFacility.setVisibility(View.GONE);
                currentTreatmentFacilityOther.setVisibility(View.GONE);
                facilityFocalPersonName.setVisibility(View.GONE);
                facilityFocalPersonNumberLinearLayout.setVisibility(View.GONE);
                facilityTBCoordinatorName.setVisibility(View.GONE);
                facilityTBCoordinatorNumberLinearLayout.setVisibility(View.GONE);
                referringClinicianName.setVisibility(View.GONE);
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
