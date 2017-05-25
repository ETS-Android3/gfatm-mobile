package com.ihsinformatics.gfatmmobile.comorbidities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Fawad Jawiad on 20-Feb-17.
 */
public class ComorbiditiesPatientInformationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;

    TitledEditText indexExternalPatientId;
    TitledEditText gpClinicCode;
    //TitledEditText specifyOther;
    TitledEditText fatherName;
    TitledEditText husbandName;
    //TitledEditText nic;
    LinearLayout cnicLayout;
    TitledEditText cnic1;
    TitledEditText cnic2;
    TitledEditText cnic3;
    TitledSpinner nicOwner;
    TitledEditText otherNicOwner;
    TitledRadioGroup addressProvided;
    TitledEditText address1;
    TitledEditText address2;
    //TitledEditText town;
    //TitledEditText city;
    TitledSpinner district;
    TitledSpinner city;
    TitledRadioGroup addressType;
    TitledEditText landmark;
    LinearLayout mobileNumber1Layout;
    TitledEditText mobileNumber1a;
    TitledEditText mobileNumber1b;
    LinearLayout mobileNumber2Layout;
    TitledEditText mobileNumber2a;
    TitledEditText mobileNumber2b;
    LinearLayout landline1Layout;
    TitledEditText landline1a;
    TitledEditText landline1b;
    LinearLayout landline2Layout;
    TitledEditText landline2a;
    TitledEditText landline2b;
    TitledRadioGroup tbStatus;
    TitledRadioGroup tbCategory;
    TitledSpinner maritalStatus;
    TitledSpinner householdHeadEducationLevel;
    TitledSpinner patientEducationalLevel;

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
        FORM_NAME = Forms.COMORBIDITIES_PATIENT_INFORMATION_FORM;
        FORM = Forms.comorbidities_indexPatientRegistration;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesPatientInformationForm.MyAdapter());
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
                scrollView = new ScrollView(mainContent.getContext());
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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        indexExternalPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_external_id), "", "", 20, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        gpClinicCode = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_gp_clinic_code), "", getResources().getString(R.string.comorbidities_preferredlocation_gpcliniccode_range), 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        //specifyOther = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_location_other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        husbandName = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_husband_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        fatherName = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_father_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        //nic = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_nic), "", "", 15, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        cnicLayout = new LinearLayout(context);
        cnicLayout.setOrientation(LinearLayout.HORIZONTAL);
        cnic1 = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_nic), "", "XXXXX", 5, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLayout.addView(cnic1);
        cnic2 = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLayout.addView(cnic2);
        cnic3 = new TitledEditText(context, null, "-", "", "X", 1, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLayout.addView(cnic3);
        nicOwner = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_patient_information_cnic_owner), getResources().getStringArray(R.array.comorbidities_patient_information_nic_options), getResources().getString(R.string.comorbidities_patient_information_nic_options_self), App.VERTICAL);
        otherNicOwner = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_other_cnic_owner), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        addressProvided = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_patient_information_address_provided), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        address1 = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_address1), "", "", 10, RegexUtil.ADDRESS_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        address2 = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_address2), "", "", 50, RegexUtil.ADDRESS_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        /*town = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_town), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        city = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_city), App.getCity(), "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        city.getEditText().setKeyListener(null);*/
        district = new TitledSpinner(context, "", getResources().getString(R.string.pet_district), getResources().getStringArray(R.array.pet_empty_array), "", App.VERTICAL);
        city = new TitledSpinner(context, "", getResources().getString(R.string.comorbidities_patient_information_city), getResources().getStringArray(R.array.pet_empty_array), "", App.VERTICAL, true);
        addressType = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_patient_information_address_type), getResources().getStringArray(R.array.comorbidities_patient_information_address_type_options), getResources().getString(R.string.comorbidities_patient_information_address_type_permanent), App.HORIZONTAL, App.VERTICAL);
        landmark = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_nearest_landmark), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        //mobileNumber1 = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_mobile_number), "", "", RegexUtil.mobileNumberLength, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        mobileNumber1Layout = new LinearLayout(context);
        mobileNumber1Layout.setOrientation(LinearLayout.HORIZONTAL);
        mobileNumber1a = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_mobile_number), "", "03XX", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        mobileNumber1Layout.addView(mobileNumber1a);
        mobileNumber1b = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        mobileNumber1Layout.addView(mobileNumber1b);
        //mobileNumber2 = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_secondary_mobile), "", "", RegexUtil.mobileNumberLength, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        mobileNumber2Layout = new LinearLayout(context);
        mobileNumber2Layout.setOrientation(LinearLayout.HORIZONTAL);
        mobileNumber2a = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_secondary_mobile), "", "03XX", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        mobileNumber2Layout.addView(mobileNumber2a);
        mobileNumber2b = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        mobileNumber2Layout.addView(mobileNumber2b);
        //landline1 = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_landline), "", "", RegexUtil.mobileNumberLength, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        landline1Layout = new LinearLayout(context);
        landline1Layout.setOrientation(LinearLayout.HORIZONTAL);
        landline1a = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_landline), "", "0XXX", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        landline1Layout.addView(landline1a);
        landline1b = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        landline1Layout.addView(landline1b);
        //landline2 = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_secondary_landline), "", "", RegexUtil.mobileNumberLength, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        landline2Layout = new LinearLayout(context);
        landline2Layout.setOrientation(LinearLayout.HORIZONTAL);
        landline2a = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_secondary_landline), "", "0XXX", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        landline2Layout.addView(landline2a);
        landline2b = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        landline2Layout.addView(landline2b);
        tbStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_patient_information_tb_status), getResources().getStringArray(R.array.comorbidities_patient_information_tb_status_options), getResources().getString(R.string.comorbidities_patient_information_tb_status_positive), App.HORIZONTAL, App.VERTICAL);
        tbCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_patient_information_tb_category), getResources().getStringArray(R.array.comorbidities_patient_information_tb_category_options), getResources().getString(R.string.comorbidities_patient_information_tb_category_cat1), App.HORIZONTAL, App.VERTICAL);
        maritalStatus = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_patient_information_marital_status), getResources().getStringArray(R.array.comorbidities_patient_information_marital_status_options), getResources().getString(R.string.comorbidities_patient_information_marital_status_options_single), App.VERTICAL, true);
        householdHeadEducationLevel = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_patient_information_head_of_education), getResources().getStringArray(R.array.comorbidities_patient_information_education_options), getString(R.string.comorbidities_patient_information_education_options_ele), App.VERTICAL, true);
        patientEducationalLevel = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_patient_information_eduaction_level), getResources().getStringArray(R.array.comorbidities_patient_information_education_options), getString(R.string.comorbidities_patient_information_education_options_ele), App.VERTICAL);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), indexExternalPatientId.getEditText(), gpClinicCode.getEditText(), /*specifyOther.getEditText(),*/ husbandName.getEditText(), fatherName.getEditText(), cnic1.getEditText(), cnic2.getEditText(), cnic3.getEditText(),/*nic.getEditText(),*/ nicOwner.getSpinner(), otherNicOwner.getEditText(), addressProvided.getRadioGroup(),
                address1.getEditText(), address2.getEditText(), district.getSpinner(), city.getSpinner(), addressType.getRadioGroup(), landmark.getEditText(), mobileNumber1a.getEditText(), mobileNumber1b.getEditText(), mobileNumber2a.getEditText(), mobileNumber2b.getEditText(), landline1a.getEditText(), landline1b.getEditText(), landline2a.getEditText(), landline2b.getEditText(),/*mobileNumber1.getEditText(), mobileNumber2.getEditText(), landline1.getEditText(), landline2.getEditText(),*/
                tbStatus.getRadioGroup(), tbCategory.getRadioGroup(), maritalStatus.getSpinner(), householdHeadEducationLevel.getSpinner(), patientEducationalLevel.getSpinner()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, indexExternalPatientId, gpClinicCode, /*specifyOther,*/ husbandName, fatherName, cnicLayout,/*nic,*/ nicOwner, otherNicOwner, addressProvided,
                        address1, address2, district, city, addressType, landmark, mobileNumber1Layout, mobileNumber2Layout, landline1Layout, landline2Layout, /*mobileNumber1, mobileNumber2, landline1, landline2,*/ tbStatus, tbCategory,
                        maritalStatus, householdHeadEducationLevel, patientEducationalLevel}};

        formDate.getButton().setOnClickListener(this);
        nicOwner.getSpinner().setOnItemSelectedListener(this);
        addressProvided.getRadioGroup().setOnCheckedChangeListener(this);
        addressType.getRadioGroup().setOnCheckedChangeListener(this);
        tbStatus.getRadioGroup().setOnCheckedChangeListener(this);

        gpClinicCode.getEditText().setSingleLine(true);
        gpClinicCode.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (gpClinicCode.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(gpClinicCode.getEditText().getText().toString());
                        if (num < 1 || num > 50) {
                            gpClinicCode.getEditText().setError(getString(R.string.comorbidities_preferredlocation_gpcliniccode_limit));
                        } else {
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        district.getSpinner().setOnItemSelectedListener(this);
        city.getSpinner().setOnItemSelectedListener(this);
        maritalStatus.getSpinner().setOnItemSelectedListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {

        //formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

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
    }

    @Override
    public boolean validate() {

        Boolean error = false;

        if (!App.get(landline2a).isEmpty() || !App.get(landline2b).isEmpty()) {
            if (!RegexUtil.isContactNumber(App.get(landline2a) + App.get(landline2b))) {
                landline2b.getEditText().setError(getResources().getString(R.string.comorbidities_patient_information_landline_format_dasherror));
                landline2b.getEditText().requestFocus();
                error = true;
            }
        }

        if (!App.get(landline1a).isEmpty() || !App.get(landline1b).isEmpty()) {
            if (!RegexUtil.isContactNumber(App.get(landline1a) + App.get(landline1b))) {
                landline1b.getEditText().setError(getResources().getString(R.string.comorbidities_patient_information_landline_format_dasherror));
                landline1b.getEditText().requestFocus();
                error = true;
            }
        }

        if (!App.get(mobileNumber2a).isEmpty() || !App.get(mobileNumber2b).isEmpty()) {
            if (!RegexUtil.isMobileNumber(App.get(mobileNumber2a) + App.get(mobileNumber2b))) {
                mobileNumber2b.getEditText().setError(getResources().getString(R.string.comorbidities_patient_information_mobile_number_format_dasherror));
                mobileNumber2b.getEditText().requestFocus();
                error = true;
            }
        }

        if (App.get(mobileNumber1a).isEmpty()) {
            mobileNumber1a.getEditText().setError(getResources().getString(R.string.empty_field));
            mobileNumber1a.getEditText().requestFocus();
            error = true;
        } else if (App.get(mobileNumber1b).isEmpty()) {
            mobileNumber1b.getEditText().setError(getResources().getString(R.string.empty_field));
            mobileNumber1b.getEditText().requestFocus();
            error = true;
        } else if (!RegexUtil.isMobileNumber(App.get(mobileNumber1a) + App.get(mobileNumber1b))) {
            mobileNumber1b.getEditText().setError(getResources().getString(R.string.comorbidities_patient_information_mobile_number_format_dasherror));
            mobileNumber1b.getEditText().requestFocus();
            error = true;
        }

        /*if (App.get(city).isEmpty() && city.getVisibility() == View.VISIBLE) {
            city.getEditText().setError(getResources().getString(R.string.empty_field));
            city.getEditText().requestFocus();
            error = true;
        }*/

        if (landmark.getEditText().getText().toString().length() > 0 && landmark.getEditText().getText().toString().trim().isEmpty()) {
            landmark.getEditText().setError(getString(R.string.comorbidities_patient_information_father_name_error));
            landmark.getEditText().requestFocus();
            error = true;
        }

        if (address2.getVisibility() == View.VISIBLE && App.get(address2).trim().isEmpty()) {
            address2.getEditText().setError(getString(R.string.empty_field));
            address2.getEditText().requestFocus();
            error = true;
        }
        else if (address2.getVisibility() == View.VISIBLE && address2.getEditText().getText().toString().length() > 0 && address2.getEditText().getText().toString().trim().isEmpty()) {
            address2.getEditText().setError(getString(R.string.comorbidities_patient_information_father_name_error));
            address2.getEditText().requestFocus();
            error = true;
        }

        if (address1.getVisibility() == View.VISIBLE && App.get(address2).trim().isEmpty()) {
            address1.getEditText().setError(getString(R.string.empty_field));
            address1.getEditText().requestFocus();
            error = true;
        }
        if (address1.getVisibility() == View.VISIBLE && address1.getEditText().getText().toString().length() > 0 && address1.getEditText().getText().toString().trim().isEmpty()) {
            address1.getEditText().setError(getString(R.string.comorbidities_patient_information_father_name_error));
            address1.getEditText().requestFocus();
            error = true;
        }

        if (husbandName.getVisibility() == View.VISIBLE && husbandName.getEditText().getText().toString().length() > 0 && husbandName.getEditText().getText().toString().trim().isEmpty()) {
            husbandName.getEditText().setError(getString(R.string.comorbidities_patient_information_father_name_error));
            husbandName.getEditText().requestFocus();
            error = true;
        }

        if (otherNicOwner.getVisibility() == View.VISIBLE && App.get(otherNicOwner).trim().isEmpty()) {
            gotoPage(1);
            otherNicOwner.getEditText().setError(getString(R.string.empty_field));
            otherNicOwner.getEditText().requestFocus();
            error = true;
        }

        /*if (specifyOther.getVisibility() == View.VISIBLE && App.get(specifyOther).isEmpty()) {
            gotoPage(1);
            specifyOther.getEditText().setError(getString(R.string.empty_field));
            specifyOther.getEditText().requestFocus();
            error = true;
        }*/

        if (husbandName.getVisibility() == View.VISIBLE && husbandName.getEditText().getText().toString().length() > 0 && husbandName.getEditText().getText().toString().trim().isEmpty()) {
            husbandName.getEditText().setError(getString(R.string.comorbidities_patient_information_father_name_error));
            husbandName.getEditText().requestFocus();
            error = true;
        }
        else if (husbandName.getVisibility() == View.VISIBLE && App.get(husbandName).length() == 1) {
            husbandName.getEditText().setError(getString(R.string.fast_husband_name_cannot_be_less_than_2_characters));
            husbandName.getEditText().requestFocus();
            error = true;
        }

        if (fatherName.getEditText().getText().toString().length() > 0 && fatherName.getEditText().getText().toString().trim().isEmpty()) {
            fatherName.getEditText().setError(getString(R.string.comorbidities_patient_information_father_name_error));
            fatherName.getEditText().requestFocus();
            error = true;
        }
        else if (fatherName.getVisibility() == View.VISIBLE && App.get(fatherName).length() == 1) {
            fatherName.getEditText().setError(getString(R.string.fast_father_name_cannot_be_less_than_2_characters));
            fatherName.getEditText().requestFocus();
            error = true;
        }

        if (gpClinicCode.getVisibility() == View.VISIBLE && App.get(gpClinicCode).isEmpty()) {
            gotoPage(1);
            gpClinicCode.getEditText().setError(getString(R.string.empty_field));
            gpClinicCode.getEditText().requestFocus();
            error = true;
        } else if (gpClinicCode.getVisibility() == View.VISIBLE && !App.get(gpClinicCode).isEmpty() && (Double.parseDouble(App.get(gpClinicCode)) < 1 || Double.parseDouble(App.get(gpClinicCode)) > 50)) {
            gotoPage(1);
            gpClinicCode.getEditText().setError(getString(R.string.comorbidities_preferredlocation_gpcliniccode_limit));
            gpClinicCode.getEditText().requestFocus();
            error = true;
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
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
        if (gpClinicCode.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HEALTH CLINIC/POST", App.get(gpClinicCode)});
        observations.add(new String[]{"FATHER NAME", App.get(fatherName).trim()});
        observations.add(new String[]{"PARTNER FULL NAME", App.get(husbandName).trim()});

        if(!App.get(cnic1).isEmpty() && !App.get(cnic2).isEmpty() && !App.get(cnic3).isEmpty()) {
            final String cnic = App.get(cnic1) + "-" + App.get(cnic2) + "-" + App.get(cnic3);
            observations.add(new String[]{"NATIONAL IDENTIFICATION NUMBER", cnic});
        }

        final String ownerString = App.get(nicOwner).equals(getResources().getString(R.string.pet_self)) ? "SELF" :
                (App.get(nicOwner).equals(getResources().getString(R.string.pet_mother)) ? "MOTHER" :
                        (App.get(nicOwner).equals(getResources().getString(R.string.pet_father)) ? "FATHER" :
                                (App.get(nicOwner).equals(getResources().getString(R.string.pet_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                        (App.get(nicOwner).equals(getResources().getString(R.string.pet_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                (App.get(nicOwner).equals(getResources().getString(R.string.pet_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                        (App.get(nicOwner).equals(getResources().getString(R.string.pet_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                                (App.get(nicOwner).equals(getResources().getString(R.string.pet_brother)) ? "BROTHER" :
                                                                        (App.get(nicOwner).equals(getResources().getString(R.string.pet_sister)) ? "SISTER" :
                                                                                (App.get(nicOwner).equals(getResources().getString(R.string.pet_son)) ? "SON" :
                                                                                        (App.get(nicOwner).equals(getResources().getString(R.string.pet_daughter)) ? "DAUGHTER" :
                                                                                                (App.get(nicOwner).equals(getResources().getString(R.string.pet_spouse)) ? "SPOUSE" :
                                                                                                        (App.get(nicOwner).equals(getResources().getString(R.string.pet_aunt)) ? "AUNT" :
                                                                                                                (App.get(nicOwner).equals(getResources().getString(R.string.pet_uncle)) ? "UNCLE" : "OTHER FAMILY MEMBER")))))))))))));

        observations.add(new String[]{"COMPUTERIZED NATIONAL IDENTIFICATION OWNER", ownerString});
        if (otherNicOwner.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER", App.get(otherNicOwner)});
        observations.add(new String[]{"PATIENT PROVIDED ADDRESS", App.get(addressProvided).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"ADDRESS (TEXT)", App.get(address1).trim()});
        observations.add(new String[]{"EXTENDED ADDRESS (TEXT)", App.get(address2).trim()});
        observations.add(new String[]{"DISTRICT", App.get(district)});
        observations.add(new String[]{"VILLAGE", App.get(city)});
        observations.add(new String[]{"TYPE OF ADDRESS", App.get(addressType).equals(getResources().getString(R.string.comorbidities_patient_information_address_type_permanent)) ? "PERMANENT ADDRESS" : "TEMPORARY ADDRESS"});
        observations.add(new String[]{"NEAREST LANDMARK", App.get(landmark).trim()});

        if(!App.get(mobileNumber1a).isEmpty() && !App.get(mobileNumber1b).isEmpty()) {
            final String mobileNumber1 = App.get(mobileNumber1a) + "-" + App.get(mobileNumber1b);
            observations.add(new String[]{"CONTACT PHONE NUMBER", mobileNumber1});
        }

        if(!App.get(mobileNumber2a).isEmpty() && !App.get(mobileNumber2b).isEmpty()) {
            final String mobileNumber2 = App.get(mobileNumber2a) + "-" + App.get(mobileNumber2b);
            observations.add(new String[]{"SECONDARY MOBILE NUMBER", mobileNumber2});
        }

        if(!App.get(landline1a).isEmpty() && !App.get(landline1b).isEmpty()) {
            final String landline1 = App.get(landline1a) + "-" + App.get(landline1b);
            observations.add(new String[]{"TERTIARY CONTACT NUMBER", landline1});
        }

        if(!App.get(landline2a).isEmpty() && !App.get(landline2b).isEmpty()) {
            final String landline2 = App.get(landline2a) + "-" + App.get(landline2b);
            observations.add(new String[]{"QUATERNARY CONTACT NUMBER", landline2});
        }

        observations.add(new String[]{"TUBERCULOSIS INFECTION TYPE", App.get(tbStatus).equals(getResources().getString(R.string.comorbidities_patient_information_tb_status_positive)) ? "SMEAR POSITIVE TUBERCULOSIS INFECTION" : "SMEAR NEGATIVE TUBERCULOSIS INFECTION"});
        if (tbCategory.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"TB CATEGORY", App.get(tbCategory).equals(getResources().getString(R.string.comorbidities_patient_information_tb_category_cat1)) ? "CATEGORY I TUBERCULOSIS" :
                    (App.get(tbCategory).equals(getResources().getString(R.string.comorbidities_patient_information_tb_category_cat2)) ? "CATEGORY II TUBERCULOSIS" : "MULTI-DRUG RESISTANT TUBERCULOSIS INFECTION")});
        }
        final String maritalStatusString = App.get(maritalStatus).equals(getResources().getString(R.string.pet_single)) ? "SINGLE" :
                (App.get(maritalStatus).equals(getResources().getString(R.string.pet_engaged)) ? "ENGAGED" :
                        (App.get(maritalStatus).equals(getResources().getString(R.string.pet_married)) ? "MARRIED" :
                                (App.get(maritalStatus).equals(getResources().getString(R.string.pet_separated)) ? "SEPARATED" :
                                        (App.get(maritalStatus).equals(getResources().getString(R.string.pet_divorced)) ? "DIVORCED" :
                                                (App.get(maritalStatus).equals(getResources().getString(R.string.pet_widower)) ? "WIDOWED" :
                                                        (App.get(maritalStatus).equals(getResources().getString(R.string.pet_other)) ? "OTHER MARITAL STATUS" :
                                                                (App.get(maritalStatus).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSE")))))));
        observations.add(new String[]{"MARITAL STATUS", maritalStatusString});

        final String houseHoldEducationLevelString = App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_elementary)) ? "ELEMENTARY EDUCATION" :
                (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_primary)) ? "PRIMARY EDUCATION" :
                        (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_secondary)) ? "SECONDARY EDUCATION" :
                                (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_intermediate)) ? "INTERMEDIATE EDUCATION" :
                                        (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_undergraduate)) ? "UNDERGRADUATE EDUCATION" :
                                                (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_graduate)) ? "GRADUATE EDUCATION" :
                                                        (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_doctorate)) ? "DOCTORATE EDUCATION" :
                                                                (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_religious)) ? "RELIGIOUS EDUCATION" :
                                                                        (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_polytechnic)) ? "POLYTECHNIC EDUCATION" :
                                                                                (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_special_education)) ? "SPECIAL EDUCATION RECEIVED" :
                                                                                        (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_no_formal_education)) ? "NO FORMAL EDUCATION" :
                                                                                                (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_other)) ? "OTHER EDUCATION LEVEL" :
                                                                                                        (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSED"))))))))))));
        observations.add(new String[]{"CURRENT EDUCATION LEVEL OF HEAD OF HOUSEHOLD", houseHoldEducationLevelString});

        final String patientEducationLevelString = App.get(patientEducationalLevel).equals(getResources().getString(R.string.pet_elementary)) ? "ELEMENTARY EDUCATION" :
                (App.get(patientEducationalLevel).equals(getResources().getString(R.string.pet_primary)) ? "PRIMARY EDUCATION" :
                        (App.get(patientEducationalLevel).equals(getResources().getString(R.string.pet_secondary)) ? "SECONDARY EDUCATION" :
                                (App.get(patientEducationalLevel).equals(getResources().getString(R.string.pet_intermediate)) ? "INTERMEDIATE EDUCATION" :
                                        (App.get(patientEducationalLevel).equals(getResources().getString(R.string.pet_undergraduate)) ? "UNDERGRADUATE EDUCATION" :
                                                (App.get(patientEducationalLevel).equals(getResources().getString(R.string.pet_graduate)) ? "GRADUATE EDUCATION" :
                                                        (App.get(patientEducationalLevel).equals(getResources().getString(R.string.pet_doctorate)) ? "DOCTORATE EDUCATION" :
                                                                (App.get(patientEducationalLevel).equals(getResources().getString(R.string.pet_religious)) ? "RELIGIOUS EDUCATION" :
                                                                        (App.get(patientEducationalLevel).equals(getResources().getString(R.string.pet_polytechnic)) ? "POLYTECHNIC EDUCATION" :
                                                                                (App.get(patientEducationalLevel).equals(getResources().getString(R.string.pet_special_education)) ? "SPECIAL EDUCATION RECEIVED" :
                                                                                        (App.get(patientEducationalLevel).equals(getResources().getString(R.string.pet_no_formal_education)) ? "NO FORMAL EDUCATION" :
                                                                                                (App.get(patientEducationalLevel).equals(getResources().getString(R.string.pet_other)) ? "OTHER EDUCATION LEVEL" :
                                                                                                        (App.get(patientEducationalLevel).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSED"))))))))))));
        observations.add(new String[]{"HIGHEST EDUCATION LEVEL", patientEducationLevelString});

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
                result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
                if (!result.contains("SUCCESS"))
                    return result;
                else {

                    String encounterId = "";

                    if (result.contains("_")) {
                        String[] successArray = result.split("_");
                        encounterId = successArray[1];
                    }

                    if (!App.get(indexExternalPatientId).isEmpty() && App.hasKeyListener(indexExternalPatientId)) {
                        result = serverService.saveIdentifier("External ID", App.get(indexExternalPatientId), encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }

                    /*   result = serverService.savePersonAddress(App.get(address1), App.get(address2), App.getCity(), App.get(town), App.getCountry(), longitude, latitude, App.get(landmark), encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;*/

                    if (!(App.get(address1).equals("") && App.get(address2).equals("") && App.get(district).equals("") && App.get(landmark).equals(""))) {
                        result = serverService.savePersonAddress(App.get(address1), App.get(address2), App.get(city), App.get(district), App.getProvince(), App.getCountry(), App.getLongitude(), App.getLatitude(), App.get(landmark), encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }

                    result = serverService.savePersonAttributeType("Primary Contact", App.get(mobileNumber1a) + App.get(mobileNumber1b), encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    result = serverService.savePersonAttributeType("Secondary Contact", App.get(mobileNumber2a) + App.get(mobileNumber2b), encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    result = serverService.savePersonAttributeType("Tertiary Contact", App.get(landline1a) + App.get(landline1b), encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    result = serverService.savePersonAttributeType("Quaternary Contact", App.get(landline2a) + App.get(landline2b), encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    String[][] concept = serverService.getConceptUuidAndDataType(maritalStatusString);
                    result = serverService.savePersonAttributeType("Marital Status", concept[0][0], encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    concept = serverService.getConceptUuidAndDataType(patientEducationLevelString);
                    result = serverService.savePersonAttributeType("Education Level", concept[0][0], encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    result = serverService.saveProgramEnrollement(App.getSqlDate(formDateCalendar), encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;
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

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));

        //serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int formId) {

        OfflineForm fo = serverService.getOfflineFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {
            String[][] obs = obsValue.get(i);

            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            }

            if (obs[0][0].equals("HEALTH CLINIC/POST")) {
                gpClinicCode.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("FATHER NAME")) {
                fatherName.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("PARTNER FULL NAME")) {
                husbandName.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("NATIONAL IDENTIFICATION NUMBER")) {
                String[] cnicArray = obs[0][1].split("-");
                cnic1.getEditText().setText(cnicArray[0]);
                cnic2.getEditText().setText(cnicArray[1]);
                cnic3.getEditText().setText(cnicArray[2]);
            } else if (obs[0][0].equals("COMPUTERIZED NATIONAL IDENTIFICATION OWNER")) {
                String value = obs[0][1].equals("SELF") ? getResources().getString(R.string.pet_self) :
                        (obs[0][1].equals("MOTHER") ? getResources().getString(R.string.pet_mother) :
                                (obs[0][1].equals("FATHER") ? getResources().getString(R.string.pet_father) :
                                        (obs[0][1].equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.pet_maternal_grandmother) :
                                                (obs[0][1].equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.pet_maternal_grandfather) :
                                                        (obs[0][1].equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.pet_paternal_grandmother) :
                                                                (obs[0][1].equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.pet_paternal_grandfather) :
                                                                        (obs[0][1].equals("BROTHER") ? getResources().getString(R.string.pet_brother) :
                                                                                (obs[0][1].equals("SISTER") ? getResources().getString(R.string.pet_sister) :
                                                                                        (obs[0][1].equals("SON") ? getResources().getString(R.string.pet_son) :
                                                                                                (obs[0][1].equals("DAUGHTER") ? getResources().getString(R.string.pet_daughter) :
                                                                                                        (obs[0][1].equals("SPOUSE") ? getResources().getString(R.string.pet_spouse) :
                                                                                                                (obs[0][1].equals("AUNT") ? getResources().getString(R.string.pet_aunt) :
                                                                                                                        (obs[0][1].equals("UNCLE") ? getResources().getString(R.string.pet_uncle) : getResources().getString(R.string.pet_other))))))))))))));
                nicOwner.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER")) {
                otherNicOwner.getEditText().setText(obs[0][1]);
                otherNicOwner.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("PATIENT PROVIDED ADDRESS")) {
                for (RadioButton rb : addressProvided.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("ADDRESS (TEXT)")) {
                address1.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("EXTENDED ADDRESS (TEXT)")) {
                address2.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("DISTRICT")) {
                district.getSpinner().selectValue(obs[0][1]);
            } else if (obs[0][0].equals("VILLAGE")) {
                city.getSpinner().selectValue(obs[0][1]);
            } else if (obs[0][0].equals("TYPE OF ADDRESS")) {
                for (RadioButton rb : addressType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_patient_information_address_type_permanent)) && obs[0][1].equals("PERMANENT ADDRESS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_patient_information_address_type_temporary)) && obs[0][1].equals("TEMPORARY ADDRESS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("NEAREST LANDMARK")) {
                landmark.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("CONTACT PHONE NUMBER")) {
                String[] mobileNumberArray = obs[0][1].split("-");
                mobileNumber1a.getEditText().setText(mobileNumberArray[0]);
                mobileNumber1b.getEditText().setText(mobileNumberArray[1]);
            } else if (obs[0][0].equals("SECONDARY MOBILE NUMBER")) {
                String[] mobileNumberArray = obs[0][1].split("-");
                mobileNumber2a.getEditText().setText(mobileNumberArray[0]);
                mobileNumber2b.getEditText().setText(mobileNumberArray[1]);
            } else if (obs[0][0].equals("TERTIARY CONTACT NUMBER")) {
                String[] mobileNumberArray = obs[0][1].split("-");
                landline1a.getEditText().setText(mobileNumberArray[0]);
                landline1b.getEditText().setText(mobileNumberArray[1]);
            } else if (obs[0][0].equals("QUATERNARY CONTACT NUMBER")) {
                String[] mobileNumberArray = obs[0][1].split("-");
                landline2a.getEditText().setText(mobileNumberArray[0]);
                landline2b.getEditText().setText(mobileNumberArray[1]);
            } else if (obs[0][0].equals("TUBERCULOSIS INFECTION TYPE")) {
                for (RadioButton rb : tbStatus.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_patient_information_tb_status_positive)) && obs[0][1].equals("SMEAR POSITIVE TUBERCULOSIS INFECTION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_patient_information_tb_status_negative)) && obs[0][1].equals("SMEAR NEGATIVE TUBERCULOSIS INFECTION")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TB CATEGORY")) {
                for (RadioButton rb : tbCategory.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_patient_information_tb_category_cat1)) && obs[0][1].equals("CATEGORY I TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_patient_information_tb_category_cat2)) && obs[0][1].equals("CATEGORY II TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_patient_information_tb_category_mdr_tb)) && obs[0][1].equals("MULTI-DRUG RESISTANT TUBERCULOSIS INFECTION")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("MARITAL STATUS")) {
                String value = obs[0][1].equals("SINGLE") ? getResources().getString(R.string.pet_single) :
                        (obs[0][1].equals("ENGAGED") ? getResources().getString(R.string.pet_engaged) :
                                (obs[0][1].equals("MARRIED") ? getResources().getString(R.string.pet_married) :
                                        (obs[0][1].equals("SEPARATED") ? getResources().getString(R.string.pet_separated) :
                                                (obs[0][1].equals("DIVORCED") ? getResources().getString(R.string.pet_divorced) :
                                                        (obs[0][1].equals("WIDOWED") ? getResources().getString(R.string.pet_widower) :
                                                                (obs[0][1].equals("OTHER") ? getResources().getString(R.string.pet_other) :
                                                                        (obs[0][1].equals("UNKNOWN") ? getResources().getString(R.string.unknown) : getResources().getString(R.string.refused))))))));
                maritalStatus.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("CURRENT EDUCATION LEVEL OF HEAD OF HOUSEHOLD")) {
                String value = obs[0][1].equals("ELEMENTARY EDUCATION") ? getResources().getString(R.string.pet_elementary) :
                        (obs[0][1].equals("PRIMARY EDUCATION") ? getResources().getString(R.string.pet_primary) :
                                (obs[0][1].equals("SECONDARY EDUCATION") ? getResources().getString(R.string.pet_secondary) :
                                        (obs[0][1].equals("INTERMEDIATE EDUCATION") ? getResources().getString(R.string.pet_intermediate) :
                                                (obs[0][1].equals("UNDERGRADUATE EDUCATION") ? getResources().getString(R.string.pet_undergraduate) :
                                                        (obs[0][1].equals("GRADUATE EDUCATION") ? getResources().getString(R.string.pet_graduate) :
                                                                (obs[0][1].equals("DOCTORATE EDUCATION") ? getResources().getString(R.string.pet_doctorate) :
                                                                        (obs[0][1].equals("RELIGIOUS EDUCATION") ? getResources().getString(R.string.pet_religious) :
                                                                                (obs[0][1].equals("POLYTECHNIC EDUCATION") ? getResources().getString(R.string.pet_polytechnic) :
                                                                                        (obs[0][1].equals("SPECIAL EDUCATION RECEIVED") ? getResources().getString(R.string.pet_special_education) :
                                                                                                (obs[0][1].equals("NO FORMAL EDUCATION") ? getResources().getString(R.string.pet_no_formal_education) :
                                                                                                        (obs[0][1].equals("OTHER") ? getResources().getString(R.string.pet_other) :
                                                                                                                (obs[0][1].equals("UNKNOWN") ? getResources().getString(R.string.unknown) : getResources().getString(R.string.refused)))))))))))));
                householdHeadEducationLevel.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("HIGHEST EDUCATION LEVEL")) {
                String value = obs[0][1].equals("ELEMENTARY EDUCATION") ? getResources().getString(R.string.pet_elementary) :
                        (obs[0][1].equals("PRIMARY EDUCATION") ? getResources().getString(R.string.pet_primary) :
                                (obs[0][1].equals("SECONDARY EDUCATION") ? getResources().getString(R.string.pet_secondary) :
                                        (obs[0][1].equals("INTERMEDIATE EDUCATION") ? getResources().getString(R.string.pet_intermediate) :
                                                (obs[0][1].equals("UNDERGRADUATE EDUCATION") ? getResources().getString(R.string.pet_undergraduate) :
                                                        (obs[0][1].equals("GRADUATE EDUCATION") ? getResources().getString(R.string.pet_graduate) :
                                                                (obs[0][1].equals("DOCTORATE EDUCATION") ? getResources().getString(R.string.pet_doctorate) :
                                                                        (obs[0][1].equals("RELIGIOUS EDUCATION") ? getResources().getString(R.string.pet_religious) :
                                                                                (obs[0][1].equals("POLYTECHNIC EDUCATION") ? getResources().getString(R.string.pet_polytechnic) :
                                                                                        (obs[0][1].equals("SPECIAL EDUCATION RECEIVED") ? getResources().getString(R.string.pet_special_education) :
                                                                                                (obs[0][1].equals("NO FORMAL EDUCATION") ? getResources().getString(R.string.pet_no_formal_education) :
                                                                                                        (obs[0][1].equals("OTHER") ? getResources().getString(R.string.pet_other) :
                                                                                                                (obs[0][1].equals("UNKNOWN") ? getResources().getString(R.string.unknown) : getResources().getString(R.string.refused)))))))))))));
                patientEducationalLevel.getSpinner().selectValue(value);
            }
        }
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
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
        if (spinner == nicOwner.getSpinner()) {
            if (App.get(nicOwner).equals(getResources().getString(R.string.pet_other)))
                otherNicOwner.setVisibility(View.VISIBLE);
            else
                otherNicOwner.setVisibility(View.GONE);
        }
        else if (spinner == district.getSpinner()) {
            String[] cities = serverService.getCityList(App.get(district));
            city.getSpinner().setAdapter(null);

            ArrayAdapter<String> spinnerArrayAdapter = null;
            if (App.isLanguageRTL()) {
                spinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.custom_rtl_spinner, cities);
                city.getSpinner().setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_rtl_spinner);
                city.getSpinner().setGravity(Gravity.RIGHT);
            } else {
                spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, cities);
                city.getSpinner().setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                city.getSpinner().setGravity(Gravity.LEFT);
            }
        }
        else if(spinner == maritalStatus.getSpinner()) {
            //Log.e("Marital", App.get(maritalStatus)+"");
            //displayHusbandName();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        displayHusbandName();
        otherNicOwner.setVisibility(View.GONE);
        displayAddressOrNot();
        displayTBCategory();

        String[] districts = serverService.getDistrictList(App.getProvince());
        district.getSpinner().setSpinnerData(districts);

        String[] cities = serverService.getCityList(App.get(district));
        city.getSpinner().setSpinnerData(cities);

        displayGPClinic();

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

        if (App.get(indexExternalPatientId).equals("")) {
            String externalId = App.getPatient().getExternalId();
            if (externalId != null) {
                if (externalId.equals("")) {
                    indexExternalPatientId.getEditText().setText("");
                } else {
                    indexExternalPatientId.getEditText().setText(externalId);
                    indexExternalPatientId.getEditText().setKeyListener(null);
                }
            } else {
                indexExternalPatientId.getEditText().setText("");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    void displayHusbandName() {
        if (App.getPatient().getPerson().getGender().equalsIgnoreCase("F") /*&& !App.get(maritalStatus).equalsIgnoreCase(getResources().getString(R.string.comorbidities_patient_information_marital_status_options_single))*/) {
            husbandName.setVisibility(View.VISIBLE);
        } else {
            husbandName.setVisibility(View.GONE);
        }
    }

    void displayAddressOrNot() {
        if (App.get(addressProvided).equalsIgnoreCase(getResources().getString(R.string.yes))) {
            address1.setVisibility(View.VISIBLE);
            address2.setVisibility(View.VISIBLE);
        } else {
            address1.setVisibility(View.GONE);
            address2.setVisibility(View.GONE);
        }
    }

    void displayTBCategory() {
        if (App.get(tbStatus).equalsIgnoreCase(getResources().getString(R.string.comorbidities_patient_information_tb_status_positive))) {
            tbCategory.setVisibility(View.VISIBLE);
        } else {
            tbCategory.setVisibility(View.GONE);
        }
    }

    void displayGPClinic() {
        if(App.getLocation().equalsIgnoreCase("GP-CLINIC")) {
            gpClinicCode.setVisibility(View.VISIBLE);
        }
        else {
            gpClinicCode.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == addressProvided.getRadioGroup()) {
            displayAddressOrNot();
        }

        if (radioGroup == tbStatus.getRadioGroup()) {
            displayTBCategory();
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
