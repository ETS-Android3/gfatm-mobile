package com.ihsinformatics.gfatmmobile.common;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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

        final Object[][] locations = serverService.getAllLocationsFromLocalDB(columnName);
        String[] locationArray = new String[locations.length + 2];
        locationArray[0] = "";
        int j = 1;
        for (int i = 0; i < locations.length; i++) {
            locationArray[j] = String.valueOf(locations[i][16]);
            j++;
        }
        locationArray[j] = getResources().getString(R.string.fast_other_title);

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        referralTransfer = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_patient_being_referred_out_or_transferred_out), getResources().getStringArray(R.array.fast_referral_transfer_list), getResources().getString(R.string.fast_referral), App.VERTICAL, App.VERTICAL,false,"PATIENT BEING REFEREED OUT OR TRANSFERRED OUT", new String[]{"PATIENT REFERRED", "PATIENT TRANSFERRED OUT"});
        reasonReferralTransfer = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_reason_for_referral_transfer), getResources().getStringArray(R.array.reason_referral_transfer_list), getResources().getString(R.string.fast_patient_choose_another_facility), App.VERTICAL,false,"REASON FOR REFERRAL OR TRANSFER",new String[]{ "PATIENT CHOOSE ANOTHER FACILITY" , "DR-TB SUSPECTED" , "DRUG RESISTANT TUBERCULOSIS" , "TUBERCULOSIS TREATMENT FAILURE" , "COMPLICATED TUBERCULOSIS" , "MYCOBACTERIUM OTHER THAN TB (MOTT)" , "TREATMENT INITIATION" , "INFECTION CONTROL" , "ADVERSE EVENTS" , "COMORBIDITY" , "SEVERE CLINICAL CONDITION" , "SURGICAL OPERATION" , "PATIENT BEHAVIOR" , "SOCIAL REASON" , "PATIENT CURRENTLY INPATIENT AT FACILITY" , "OTHER TRANSFER OR REFERRAL REASON"});
        reasonReferralTransferOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"OTHER TRANSFER OR REFERRAL REASON");

        treatmentStartDate = new TitledEditText(context, null, getResources().getString(R.string.treatment_start_date), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false,"TREATMENT START DATE");
        treatmentStartDate.getEditText().setOnKeyListener(null);
        treatmentStartDate.getEditText().setFocusable(false);
        nationalDRTBNumber = new TitledEditText(context, null, getResources().getString(R.string.national_drtb_number), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false,"NATIONAL DR-TB TREATMENT REGISTRATION NUMBER");
        nationalDRTBNumber.getEditText().setKeyListener(null);
        nationalDRTBNumber.getEditText().setFocusable(false);
        currentTreatmentFacility = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.current_treatment_facility), locationArray, "", App.VERTICAL,false,"TREATMENT FACILITY",locationArray);
        currentTreatmentFacilityOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"TREATMENT FACILITY OTHER");
        facilityFocalPersonName = new TitledEditText(context, null, getResources().getString(R.string.facility_focal_person_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false,"TREATMENT FACILITY FOCAL PERSON NAME");
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
        facilityTBCoordinatorName  = new TitledEditText(context, null, getResources().getString(R.string.facility_tb_cordinator_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false,"TREATMENT FACILITY DISTRICT TUBERCULOSIS COORDINATOR NAME");
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
        referringClinicianName  = new TitledEditText(context, null, getResources().getString(R.string.referring_clinician_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false,"CLINICIAN REFERRING THE PAITENT");

        final Object[][] referrallocations = serverService.getLocationsByTag("Transfer Location");
        String[] referralLocationArray = new String[referrallocations.length + 2];
        referralLocationArray[0] = "";
        int k = 1;
        for (int i = 0; i < referrallocations.length; i++) {
            referralLocationArray[k] = String.valueOf(referrallocations[i][16]);
            k++;
        }
        referralLocationArray[k] = getResources().getString(R.string.fast_other_title);


        referralSite = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_location_for_referral_transfer), referralLocationArray, "", App.VERTICAL, true);
        referralSiteOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"LOCATION OF REFERRAL OR TRANSFER OTHER");

        treatmentInitiatedAtReferralAndTransferSite = new TitledRadioGroup(context, null, getResources().getString(R.string.treatment_initiated_at_referrral_trasfer_site), getResources().getStringArray(R.array.yes_no_unknown_options), getResources().getString(R.string.unknown), App.VERTICAL, App.VERTICAL,false,"TREATMENT INITIATED AT REFERRAL OR TRANSFER SITE",new String[]{ "YES" , "NO" , "UNKNOWN"});
        reasonTreatmentNotInitiated = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.reason_treatment_not_initiated), getResources().getStringArray(R.array.reason_treatment_not_initiated_list), "", App.VERTICAL, true,"TREATMENT NOT INITIATED AT REFERRAL OR TRANSFER SITE",new String[]{"PATIENT COULD NOT BE CONTACTED" , "PATIENT LEFT THE CITY" , "REFUSAL OF TREATMENT BY PATIENT" , "DIED" , "DR NOT CONFIRMED BY BASELINE REPEAT TEST" , "OTHER REASON FOR TREATMENT NOT INITIATED"});
        reasonTreatmentNotInitiatedOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"OTHER REASON FOR TREATMENT NOT INITIATED");
        drConfirmation = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_dr_confirmation), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL,false,"DRUG RESISTANCE CONFIRMATION",getResources().getStringArray(R.array.yes_no_list_concept));
        enrsId = new TitledEditText(context, null, getResources().getString(R.string.fast_enrs_number), "", "", 20, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);

        endFollowupInstruction = new MyTextView(context, getResources().getString(R.string.fast_end_followup_instruction));
        endFollowupInstruction.setTextColor(Color.BLACK);
        endFollowupInstruction.setTypeface(null, Typeface.NORMAL);

        contactName = new TitledEditText(context, null, getResources().getString(R.string.contact_person_name), "", "", 40, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false,"REFERRAL CONTACT FIRST NAME");

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
                facilityTBCoordinatorNumber2, referringClinicianName.getEditText(), currentTreatmentFacilityOther.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, referralTransfer, reasonReferralTransfer, reasonReferralTransferOther, treatmentStartDate, nationalDRTBNumber, currentTreatmentFacility, currentTreatmentFacilityOther, facilityFocalPersonName,
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
        currentTreatmentFacility.getSpinner().setOnItemSelectedListener(this);

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
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
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
        Boolean error = super.validate();



        if(facilityFocalPersonNumberLinearLayout.getVisibility() == View.VISIBLE) {
            if (App.get(facilityFocalPersonNumber1).equals("") && App.get(facilityFocalPersonNumber2).equals("")) {

                facilityFocalPersonNumber2.setError(null);
            } else {

                String mobile = App.get(facilityFocalPersonNumber1) + App.get(facilityFocalPersonNumber2);
                if (!RegexUtil.isLandlineNumber(mobile)) {
                    facilityFocalPersonNumber2.setError(getString(R.string.ctb_invalid_number));
                    facilityFocalPersonNumber2.requestFocus();
                    error = true;
                    gotoPage(0);
                }
            }
        }

        if(facilityTBCoordinatorNumberLinearLayout.getVisibility() == View.VISIBLE) {
            if (App.get(facilityTBCoordinatorNumber1).equals("") && App.get(facilityTBCoordinatorNumber2).equals("")) {

                facilityFocalPersonNumber2.setError(null);
            } else {

                String mobile = App.get(facilityTBCoordinatorNumber1) + App.get(facilityTBCoordinatorNumber2);
                if (!RegexUtil.isLandlineNumber(mobile)) {
                    facilityFocalPersonNumber2.setError(getString(R.string.ctb_invalid_number));
                    facilityFocalPersonNumber2.requestFocus();
                    error = true;
                    gotoPage(0);
                }
            }
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
                gotoLastPage();
            }
        }



        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(),R.style.dialog).create();
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
        final HashMap<String, String> personAttribute = new HashMap<>() ;
        final ArrayList<String[]> observations =  getObservations();

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


        if (facilityFocalPersonNumberLinearLayout.getVisibility() == View.VISIBLE) {
            if(!App.get(facilityFocalPersonNumber1).equals("")){

                String number = App.get(facilityFocalPersonNumber1)+ '-' + App.get(facilityFocalPersonNumber2);
                observations.add(new String[]{"TREATMENT FACILITY FOCAL PERSON PHONE NUMBER", number});

            }
        }

     if (facilityTBCoordinatorNumberLinearLayout.getVisibility() == View.VISIBLE) {
            if(!App.get(facilityTBCoordinatorNumber1).equals("")){

                String number = App.get(facilityTBCoordinatorNumber1)+ '-' + App.get(facilityTBCoordinatorNumber2);
                observations.add(new String[]{"TREATMENT FACILITY DISTRICT TUBERCULOSIS COORDINATOR NUMBER", number});

            }
        }


        if (referralSite.getVisibility() == View.VISIBLE) {
            if(App.get(referralSite).equals(getString(R.string.fast_other_title)))
                observations.add(new String[]{"REFERRING FACILITY NAME", App.get(referralSite)});
            else{
                String location = serverService.getLocationNameFromDescription(App.get(referralSite));
                observations.add(new String[]{"REFERRING FACILITY NAME", location});
            }

            personAttribute.put("Health Center",serverService.getLocationUuid(App.get(referralSite)));
        }


        if (mobileLinearLayout.getVisibility() == View.VISIBLE) {
            if(!App.get(mobile1).equals("")){

                String number = App.get(mobile1)+ '-' + App.get(mobile2);
                observations.add(new String[]{"REFERRAL CONTACT NUMBER", number});

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

                String id = null;
                if(App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocally(formName, form, formDateCalendar,observations.toArray(new String[][]{}));

                String result = "";

                result = serverService.saveIdentifier("ENRS", App.get(enrsId), id);
                if (!result.equals("SUCCESS"))
                    return result;

                result = serverService.saveMultiplePersonAttribute(personAttribute, id);
                if (!result.equals("SUCCESS"))
                    return result;

                result = serverService.saveEncounterAndObservationTesting(formName, form, formDateCalendar, observations.toArray(new String[][]{}), id);
                if (!result.contains("SUCCESS"))
                    return result;

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

        super.refill(formId);

        OfflineForm fo = serverService.getSavedFormById(formId);

        ArrayList<String[][]> obsValue = fo.getObsValue();


        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
          if (obs[0][0].equals("TREATMENT FACILITY FOCAL PERSON PHONE NUMBER")) {
                String[] phoneNumber = obs[0][1].split("-");
                facilityFocalPersonNumber1.setText(phoneNumber[0]);
                facilityFocalPersonNumber2.setText(phoneNumber[1]);
                facilityFocalPersonNumberLinearLayout.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT FACILITY DISTRICT TUBERCULOSIS COORDINATOR NUMBER")) {
                String[] phoneNumber = obs[0][1].split("-");
                facilityTBCoordinatorNumber1.setText(phoneNumber[0]);
                facilityTBCoordinatorNumber2.setText(phoneNumber[1]);
                facilityTBCoordinatorNumberLinearLayout.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REFERRING FACILITY NAME")) {
                if(obs[0][1].equals(getString(R.string.fast_other_title)))
                    referralSite.getSpinner().selectValue(obs[0][1]);
                else{
                    String location = serverService.getLocationDescriptionFromName(obs[0][1]);
                    referralSite.getSpinner().selectValue(obs[0][1]);
                }
                referralSite.setVisibility(View.VISIBLE);
            }   else if (obs[0][0].equals("REFERRAL CONTACT NUMBER")) {
                String[] phoneNumber = obs[0][1].split("-");
                mobile1.setText(phoneNumber[0]);
                mobile2.setText(phoneNumber[1]);
                mobileLinearLayout.setVisibility(View.VISIBLE);
            }
        }

        enrsId.getEditText().setText(App.getPatient().getEnrs());
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
                contactName.getEditText().setText("");
                mobile1.setText("");
                mobile2.setText("");
            } else {
                referralSiteOther.setVisibility(View.GONE);
                Object[][] details = serverService.getLocationContactDetails(parent.getItemAtPosition(position).toString());
                if(details != null && details.length > 0) {
                    String number = String.valueOf(details[0][0]);
                    String name = String.valueOf(details[0][1]);
                    contactName.getEditText().setText(name);
                    if(number.length()==11){
                        mobile1.setText(number.substring(0,5));
                        mobile2.setText(number.substring(4));
                    }
                }
            }
        }  else if (spinner == reasonTreatmentNotInitiated.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.other))) {
                reasonTreatmentNotInitiatedOther.setVisibility(View.VISIBLE);
            } else {
                reasonTreatmentNotInitiatedOther.setVisibility(View.GONE);
            }
        } else if (spinner == currentTreatmentFacility.getSpinner()){
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                currentTreatmentFacilityOther.setVisibility(View.VISIBLE);
            } else {
                currentTreatmentFacilityOther.setVisibility(View.GONE);
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

        String s = App.getPatient().getExternalId();
        nationalDRTBNumber.getEditText().setText(s);

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
