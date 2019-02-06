package com.ihsinformatics.gfatmmobile.common;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.method.BaseKeyListener;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.Barcode;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyCheckBox;
import com.ihsinformatics.gfatmmobile.custom.MyEditText;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PatientInformationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;
    boolean emptyError = false;

    // Views...
    TitledButton formDate;
    TitledEditText contactExternalId;
    TitledCheckBoxes dummyRadio;
    TitledSpinner patientSource;
    TitledEditText otherPatientSource;
    TitledEditText indexId;
    TitledButton scanBarcode;
    TitledButton fetchIndexPatient;
    TitledEditText indexFirstName;
    LinearLayout cnicLinearLayout;
    MyEditText cnic1;
    MyEditText cnic2;
    MyEditText cnic3;
    LinearLayout mobileLinearLayout;
    MyEditText mobile1;
    MyEditText mobile2;
    LinearLayout secondaryMobileLinearLayout;
    MyEditText secondaryMobile1;
    MyEditText secondaryMobile2;
    LinearLayout landlineLinearLayout;
    MyEditText landline1;
    MyEditText landline2;
    LinearLayout secondaryLandlineLinearLayout;
    MyEditText secondaryLandline1;
    MyEditText secondaryLandline2;
    TitledSpinner cnicOwner;
    TitledEditText otherCnicOwner;
    TitledRadioGroup addressProvided;
    TitledSpinner province;
    MyLinearLayout addressLayout;
    MyTextView townTextView;
    TitledEditText addressHouse;
    AutoCompleteTextView addressStreet;
    TitledSpinner district;
    TitledSpinner city;
    TitledRadioGroup addressType;
    TitledEditText nearestLandmark;
    TitledRadioGroup contactPermission;
    MyTextView screeningInstruction;

    ArrayAdapter<String> districtArrayAdapter;
    ArrayAdapter<String> cityArrayAdapter;

    /**
     * CHANGE pageCount and formName Variable only...
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 1;
        formName = Forms.PATIENT_INFORMATION_FORM;
        form = Forms.patientInformationForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(pageCount - 1);
        formNameView.setText(formName);

        initViews();

        groups = new ArrayList<>();

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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        contactExternalId = new TitledEditText(context, null, getResources().getString(R.string.fast_external_id), "", "", 20, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        patientSource = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.patient_source), getResources().getStringArray(R.array.patient_source_options), "", App.VERTICAL, true);
        otherPatientSource = new TitledEditText(context, null, getResources().getString(R.string.other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        indexId = new TitledEditText(context, null, getResources().getString(R.string.index_patient_id), "", "", 7, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        scanBarcode= new TitledButton(context, null, "", getResources().getString(R.string.scan_barcode), App.HORIZONTAL);
        fetchIndexPatient= new TitledButton(context, null, "",getResources().getString(R.string.fetch_index_patient), App.HORIZONTAL);
        indexFirstName = new TitledEditText(context, null, getResources().getString(R.string.index_patient_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        cnicLinearLayout = new LinearLayout(context);
        cnicLinearLayout.setOrientation(LinearLayout.VERTICAL);
        MyTextView cnic = new MyTextView(context, getResources().getString(R.string.fast_nic_number));
        cnicLinearLayout.addView(cnic);
        LinearLayout cnicPartLayout = new LinearLayout(context);
        cnicPartLayout.setOrientation(LinearLayout.HORIZONTAL);
        cnic1 = new MyEditText(context, "", 5, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE);
        cnic1.setHint("XXXXX");
        cnicPartLayout.addView(cnic1);
        MyTextView cnicDash = new MyTextView(context, " - ");
        cnicPartLayout.addView(cnicDash);
        cnic2 = new MyEditText(context, "", 7, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE);
        cnic2.setHint("XXXXXXX");
        cnicPartLayout.addView(cnic2);
        MyTextView cnicDash2 = new MyTextView(context, " - ");
        cnicPartLayout.addView(cnicDash2);
        cnic3 = new MyEditText(context, "", 1, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE);
        cnic3.setHint("X");
        cnicPartLayout.addView(cnic3);
        cnicLinearLayout.addView(cnicPartLayout);

        cnicOwner = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_whose_nic_is_this), getResources().getStringArray(R.array.fast_whose_nic_is_this_list), getResources().getString(R.string.fast_self), App.VERTICAL);
        otherCnicOwner = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        addressProvided = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_patient_provided_their_address), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        addressHouse = new TitledEditText(context, null, getResources().getString(R.string.fast_address_1), "", "", 50, RegexUtil.ADDRESS_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        addressLayout = new MyLinearLayout(context, null, App.VERTICAL);
        townTextView = new MyTextView(context, getResources().getString(R.string.fast_address_2));
        addressStreet = new AutoCompleteTextView(context);
        addressStreet.setInputType(InputType.TYPE_CLASS_TEXT);
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(20);
        addressStreet.setFilters(fArray);
        province = new TitledSpinner(context, "", getResources().getString(R.string.province), getResources().getStringArray(R.array.provinces), App.getProvince(), App.VERTICAL);
        addressLayout.addView(townTextView);
        addressLayout.addView(addressStreet);        district = new TitledSpinner(context, "", getResources().getString(R.string.pet_district), getResources().getStringArray(R.array.pet_empty_array), "", App.VERTICAL);
        city = new TitledSpinner(context, "", getResources().getString(R.string.pet_city), getResources().getStringArray(R.array.pet_empty_array), "", App.VERTICAL);
        addressType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_type_of_address_is_this), getResources().getStringArray(R.array.fast_type_of_address_list), "", App.VERTICAL, App.VERTICAL);
        nearestLandmark = new TitledEditText(context, null, getResources().getString(R.string.fast_nearest_landmark), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        contactPermission = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_can_we_call_you), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        dummyRadio = new TitledCheckBoxes(context, null, null, getResources().getStringArray(R.array.dummy_list), null, App.HORIZONTAL, App.HORIZONTAL, false);

     
      /*  mobileLinearLayout = new LinearLayout(context);
        mobileLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mobile1 = new TitledEditText(context, null, getResources().getString(R.string.fast_mobile_number), "", "####", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        mobileLinearLayout.addView(mobile1);
        mobile2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        mobileLinearLayout.addView(mobile2);*/

        mobileLinearLayout = new LinearLayout(context);
        mobileLinearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout mobileQuestion = new LinearLayout(context);
        mobileQuestion.setOrientation(LinearLayout.HORIZONTAL);
        MyTextView mobileNumberLabel = new MyTextView(context, getResources().getString(R.string.fast_mobile_number));
        mobileQuestion.addView(mobileNumberLabel);
        TextView mandatorySign = new TextView(context);
        mandatorySign.setText("*");
        mandatorySign.setTextColor(Color.parseColor("#ff0000"));
        mobileQuestion.addView(mandatorySign);
        mobileLinearLayout.addView(mobileQuestion);
        LinearLayout mobileNumberPart = new LinearLayout(context);
        mobileNumberPart.setOrientation(LinearLayout.HORIZONTAL);
        mobile1 = new MyEditText(context,"", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        mobile1.setHint("03XX");
        mobileNumberPart.addView(mobile1);
        MyTextView mobileNumberDash = new MyTextView(context, " - ");
        mobileNumberPart.addView(mobileNumberDash);
        mobile2 = new MyEditText(context,"",  7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        mobile2.setHint("XXXXXXX");
        mobileNumberPart.addView(mobile2);
       // mobileLinearLayout.addView(mobileNumberPart);
        LinearLayout dummyLayout = new LinearLayout(context);
        dummyLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        dummyLayout.setPadding(0, 0, 0, 0);
        dummyLayout.setLayoutParams(layoutParams);
        dummyLayout.addView(mobileNumberPart);
        dummyLayout.addView(dummyRadio);
        mobileLinearLayout.addView(dummyLayout);
        
    /*    secondaryMobileLinearLayout = new LinearLayout(context);
        secondaryMobileLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        secondaryMobile1 = new TitledEditText(context, null, getResources().getString(R.string.fast_secondary_mobile), "", "####", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        secondaryMobileLinearLayout.addView(secondaryMobile1);
        secondaryMobile2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        secondaryMobileLinearLayout.addView(secondaryMobile2);*/


        secondaryMobileLinearLayout = new LinearLayout(context);
        secondaryMobileLinearLayout.setOrientation(LinearLayout.VERTICAL);
        MyTextView secondaryMobileNumberLabel = new MyTextView(context, getResources().getString(R.string.fast_secondary_mobile));
        secondaryMobileLinearLayout.addView(secondaryMobileNumberLabel);
        LinearLayout secondaryMobileNumberPart = new LinearLayout(context);
        secondaryMobileNumberPart.setOrientation(LinearLayout.HORIZONTAL);
        secondaryMobile1 = new MyEditText(context,"", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        secondaryMobile1.setHint("03XX");
        secondaryMobileNumberPart.addView(secondaryMobile1);
        MyTextView secondarysecondaryMobileNumberDash = new MyTextView(context, " - ");
        secondaryMobileNumberPart.addView(secondarysecondaryMobileNumberDash);
        secondaryMobile2 = new MyEditText(context,"",  7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        secondaryMobile2.setHint("XXXXXXX");
        secondaryMobileNumberPart.addView(secondaryMobile2);
        secondaryMobileLinearLayout.addView(secondaryMobileNumberPart);


        landlineLinearLayout = new LinearLayout(context);
        landlineLinearLayout.setOrientation(LinearLayout.VERTICAL);
        MyTextView landlineLabel = new MyTextView(context, getResources().getString(R.string.fast_landline_number));
        landlineLinearLayout.addView(landlineLabel);
        LinearLayout landlineNumberPart = new LinearLayout(context);
        landlineNumberPart.setOrientation(LinearLayout.HORIZONTAL);
        landline1 = new MyEditText(context,"", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        landline1.setHint("0XXX");
        landlineNumberPart.addView(landline1);
        MyTextView landlineNumberDash = new MyTextView(context, " - ");
        landlineNumberPart.addView(landlineNumberDash);
        landline2 = new MyEditText(context,"",  7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        landline2.setHint("XXXXXXX");
        landlineNumberPart.addView(landline2);
        landlineLinearLayout.addView(landlineNumberPart);
    
    
     /*   landlineLinearLayout = new LinearLayout(context);
        landlineLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        landline1 = new TitledEditText(context, null, getResources().getString(R.string.fast_landline_number), "", "####", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        landlineLinearLayout.addView(landline1);
        landline2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        landlineLinearLayout.addView(landline2);*/
        
      /*  secondaryLandlineLinearLayout = new LinearLayout(context);
        secondaryLandlineLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        secondaryLandline1 = new TitledEditText(context, null, getResources().getString(R.string.fast_secondary_landline), "", "####", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        secondaryLandlineLinearLayout.addView(secondaryLandline1);
        secondaryLandline2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        secondaryLandlineLinearLayout.addView(secondaryLandline2);*/

        secondaryLandlineLinearLayout = new LinearLayout(context);
        secondaryLandlineLinearLayout.setOrientation(LinearLayout.VERTICAL);
        MyTextView secondaryLandlineLabel = new MyTextView(context, getResources().getString(R.string.fast_secondary_landline));
        secondaryLandlineLinearLayout.addView(secondaryLandlineLabel);
        LinearLayout secondaryLandlineLinearLayoutPart = new LinearLayout(context);
        secondaryLandlineLinearLayoutPart.setOrientation(LinearLayout.HORIZONTAL);
        secondaryLandline1 = new MyEditText(context,"", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        secondaryLandline1.setHint("0XXX");
        secondaryLandlineLinearLayoutPart.addView(secondaryLandline1);
        MyTextView secondaryLandlineLinearLayoutDash = new MyTextView(context, " - ");
        secondaryLandlineLinearLayoutPart.addView(secondaryLandlineLinearLayoutDash);
        secondaryLandline2 = new MyEditText(context,"",  7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        secondaryLandline2.setHint("XXXXXXX");
        secondaryLandlineLinearLayoutPart.addView(secondaryLandline2);
        secondaryLandlineLinearLayout.addView(secondaryLandlineLinearLayoutPart);

        screeningInstruction = new MyTextView(context, getResources().getString(R.string.sms_instruction));
        screeningInstruction.setTextColor(Color.BLACK);
        screeningInstruction.setTypeface(null, Typeface.NORMAL);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), contactExternalId.getEditText(), patientSource.getSpinner(), otherPatientSource.getEditText(),
                indexId.getEditText(), indexFirstName.getEditText(),
                cnic1, cnic2, cnic3, cnicOwner.getSpinner(), otherCnicOwner.getEditText(),
                addressProvided.getRadioGroup(), addressHouse.getEditText(), district.getSpinner(),
                city.getSpinner(), addressType.getRadioGroup(), nearestLandmark.getEditText(), contactPermission.getRadioGroup()
                , mobile1, mobile2, secondaryMobile1, secondaryMobile2, landline1,
                landline2, secondaryLandline1, secondaryLandline2,dummyRadio};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, contactExternalId, patientSource, otherPatientSource, indexId, scanBarcode, fetchIndexPatient, indexFirstName,
                        cnicLinearLayout, cnicOwner, otherCnicOwner, addressProvided, addressHouse, addressLayout, province, district,
                        city, addressType, nearestLandmark, screeningInstruction, contactPermission, mobileLinearLayout, secondaryMobileLinearLayout,
                        landlineLinearLayout, secondaryLandlineLinearLayout}};


        formDate.getButton().setOnClickListener(this);
        scanBarcode.getButton().setOnClickListener(this);
        fetchIndexPatient.getButton().setOnClickListener(this);
        cnicOwner.getSpinner().setOnItemSelectedListener(this);
        province.getSpinner().setOnItemSelectedListener(this);
        patientSource.getSpinner().setOnItemSelectedListener(this);
        addressProvided.getRadioGroup().setOnCheckedChangeListener(this);
        district.getSpinner().setOnItemSelectedListener(this);

        ArrayList<MyCheckBox> checkBoxList = dummyRadio.getCheckedBoxes();
        for (CheckBox cb : dummyRadio.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);


        resetViews();

        cnic1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==5){
                    cnic2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String cnic = cnic1.getText().toString() + "-" + cnic2.getText().toString() + "-" + cnic3.getText().toString();
                if (RegexUtil.isValidNIC(cnic)) {
                    cnicOwner.setVisibility(View.VISIBLE);
                    if(cnicOwner.getSpinner().getSelectedItem().equals(getResources().getString(R.string.other))){
                        otherCnicOwner.setVisibility(View.VISIBLE);
                    }
                }else{
                    cnicOwner.setVisibility(View.GONE);
                    otherCnicOwner.setVisibility(View.GONE);
                }

            }
        });

        cnic2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==7){
                    cnic3.requestFocus();
                }

                if(s.length()==0){
                    cnic1.requestFocus();
                    cnic1.setSelection(cnic1.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String cnic = cnic1.getText().toString() + "-" + cnic2.getText().toString() + "-" + cnic3.getText().toString();
                if (RegexUtil.isValidNIC(cnic)) {
                    cnicOwner.setVisibility(View.VISIBLE);
                    if(cnicOwner.getSpinner().getSelectedItem().equals(getResources().getString(R.string.other))){
                        otherCnicOwner.setVisibility(View.VISIBLE);
                    }
                }else{
                    cnicOwner.setVisibility(View.GONE);
                    otherCnicOwner.setVisibility(View.GONE);
                }

            }

        });

        cnic3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    cnic2.requestFocus();
                    cnic2.setSelection(cnic2.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String cnic = cnic1.getText().toString() + "-" + cnic2.getText().toString() + "-" + cnic3.getText().toString();
                if (RegexUtil.isValidNIC(cnic)) {
                    cnicOwner.setVisibility(View.VISIBLE);
                    if(cnicOwner.getSpinner().getSelectedItem().equals(getResources().getString(R.string.other))){
                        otherCnicOwner.setVisibility(View.VISIBLE);
                    }
                }else{
                    cnicOwner.setVisibility(View.GONE);
                    otherCnicOwner.setVisibility(View.GONE);
                }

            }
        });



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

        secondaryMobile2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    secondaryMobile1.requestFocus();
                    secondaryMobile1.setSelection(secondaryMobile1.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        secondaryMobile1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==4){
                    secondaryMobile2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        landline2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    landline1.requestFocus();
                    landline1.setSelection(landline1.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        landline1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==4){
                    landline2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        secondaryLandline2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    secondaryLandline1.requestFocus();
                    secondaryLandline1.setSelection(secondaryLandline1.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        secondaryLandline1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==4){
                    secondaryLandline2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        indexId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(start == 5 && s.length()==5){
                    int i = indexId.getEditText().getSelectionStart();
                    if (i == 5){
                        indexId.getEditText().setText(indexId.getEditText().getText().toString().substring(0,4));
                        indexId.getEditText().setSelection(4);
                    }
                }
                else if(s.length()==5 && !s.toString().contains("-")){
                    indexId.getEditText().setText(s + "-");
                    indexId.getEditText().setSelection(6);
                } else if(s.length()==7 && !RegexUtil.isValidId(App.get(indexId)))
                    indexId.getEditText().setError(getString(R.string.invalid_id));
                else
                    indexId.getEditText().setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

        if(indexFirstName.getVisibility() == View.VISIBLE && App.get(indexFirstName).equals("")){

            gotoPage(0);
            indexFirstName.getEditText().setError(getString(R.string.empty_field));
            indexFirstName.getEditText().requestFocus();
            error = true;

        } else {

            indexFirstName.getEditText().setError(null);
        }

        if (contactExternalId.getEditText().getText().toString().length() > 0 && contactExternalId.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            contactExternalId.getEditText().setError(getString(R.string.invalid_value));
            contactExternalId.getEditText().requestFocus();
            error = true;
        } else {

            contactExternalId.getEditText().setError(null);
        }

        if (App.get(patientSource).equals("")) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            patientSource.getQuestionView().setError(getString(R.string.invalid_value));
            patientSource.getSpinner().requestFocus();
            error = true;
        } else {

            patientSource.getQuestionView().setError(null);
        }

        if (otherPatientSource.getVisibility() == View.VISIBLE && App.get(otherPatientSource).equals("")) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherPatientSource.getEditText().setError(getString(R.string.invalid_value));
            otherPatientSource.getEditText().requestFocus();
            error = true;
        }else {

            otherPatientSource.getEditText().setError(null);
        }

      /*  if (contactExternalId.getVisibility() == View.VISIBLE && contactExternalId.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            contactExternalId.getEditText().setError(getString(R.string.empty_field));
            contactExternalId.getEditText().requestFocus();
            error = true;
        }*/

        if (addressHouse.getEditText().getText().toString().length() > 0 && addressHouse.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            addressHouse.getEditText().setError(getString(R.string.invalid_value));
            addressHouse.getEditText().requestFocus();
            error = true;
        }else {

            addressHouse.getEditText().setError(null);
        }

     /*   if (addressStreet.getEditText().getText().toString().length() > 0 && addressStreet.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            addressStreet.getEditText().setError(getString(R.string.invalid_value));
            addressStreet.getEditText().requestFocus();
            error = true;
        }*/

        if (nearestLandmark.getEditText().getText().toString().length() > 0 && nearestLandmark.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            nearestLandmark.getEditText().setError(getString(R.string.invalid_value));
            nearestLandmark.getEditText().requestFocus();
            error = true;
        }else {

            nearestLandmark.getEditText().setError(null);
        }

      /*  if (cnic1.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic1.setError(getString(R.string.empty_field));
            cnic1.requestFocus();
            error = true;
        }


        if (cnic2.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic2.setError(getString(R.string.empty_field));
            cnic2.requestFocus();
            error = true;
        }

        if (cnic3.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic3.setError(getString(R.string.empty_field));
            cnic3.requestFocus();
            error = true;
        }*/


      /*  if (addressType.getVisibility() == View.VISIBLE && App.get(addressType).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            emptyError = true;
            error = true;
        }*/

        if (!cnic1.getText().toString().trim().isEmpty() && cnic2.getText().toString().trim().isEmpty() && cnic3.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic2.setError(getString(R.string.empty_field));
            cnic2.requestFocus();

            cnic3.setError(getString(R.string.empty_field));
            cnic3.requestFocus();
            error = true;
        }

        if (!cnic1.getText().toString().trim().isEmpty() && cnic2.getText().toString().trim().isEmpty() && !cnic3.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic2.setError(getString(R.string.empty_field));
            cnic2.requestFocus();
            error = true;
        }

        if (!cnic1.getText().toString().trim().isEmpty() && !cnic2.getText().toString().trim().isEmpty() && cnic3.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic3.setError(getString(R.string.empty_field));
            cnic3.requestFocus();
            error = true;
        }

        if (cnic1.getText().toString().trim().isEmpty() && !cnic2.getText().toString().trim().isEmpty() && cnic3.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic1.setError(getString(R.string.empty_field));
            cnic1.requestFocus();

            cnic3.setError(getString(R.string.empty_field));
            cnic3.requestFocus();
            error = true;
        }
        if (!cnic1.getText().toString().trim().isEmpty() && !cnic2.getText().toString().trim().isEmpty() && cnic3.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic3.setError(getString(R.string.empty_field));
            cnic3.requestFocus();
            error = true;
        }
        if (cnic1.getText().toString().trim().isEmpty() && !cnic2.getText().toString().trim().isEmpty() && !cnic3.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic1.setError(getString(R.string.empty_field));
            cnic1.requestFocus();
            error = true;
        }

        if (cnic1.getText().toString().trim().isEmpty() && cnic2.getText().toString().trim().isEmpty() && !cnic3.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic1.setError(getString(R.string.empty_field));
            cnic1.requestFocus();

            cnic2.setError(getString(R.string.empty_field));
            cnic2.requestFocus();
            error = true;
        }

        if (!cnic1.getText().toString().trim().isEmpty() && cnic2.getText().toString().trim().isEmpty() && !cnic3.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic2.setError(getString(R.string.empty_field));
            cnic2.requestFocus();
            error = true;
        }

        if (cnic1.getText().toString().trim().isEmpty() && !cnic2.getText().toString().trim().isEmpty() && !cnic3.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic1.setError(getString(R.string.empty_field));
            cnic1.requestFocus();
            error = true;
        }


        if (cnic1.getText().toString().length() > 0 && App.get(cnic1).length() != 5) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic1.setError(getString(R.string.length_message));
            cnic1.requestFocus();
            error = true;
        }

        if (cnic2.getText().toString().length() > 0 && App.get(cnic2).length() != 7) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic2.setError(getString(R.string.length_message));
            cnic2.requestFocus();
            error = true;
        }

        if ( cnic3.getText().toString().length() > 0  && App.get(cnic3).length() != 1) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic3.setError(getString(R.string.length_message));
            cnic3.requestFocus();
            error = true;
        }

        if (otherCnicOwner.getVisibility() == View.VISIBLE && otherCnicOwner.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherCnicOwner.getEditText().setError(getString(R.string.empty_field));
            otherCnicOwner.getEditText().requestFocus();
            error = true;
        }

        if (mobile1.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile1.setError(getString(R.string.empty_field));
            mobile1.requestFocus();
            error = true;
        }

        if (mobile2.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile2.setError(getString(R.string.empty_field));
            mobile2.requestFocus();
            error = true;
        }

        if (secondaryMobile1.getText().toString().trim().isEmpty() && !secondaryMobile2.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryMobile1.setError(getString(R.string.empty_field));
            secondaryMobile1.requestFocus();
            error = true;
        } else {
            secondaryMobile1.setError(null);
        }

        if (secondaryMobile2.getText().toString().trim().isEmpty() && !secondaryMobile1.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryMobile2.setError(getString(R.string.empty_field));
            secondaryMobile2.requestFocus();
            error = true;
        } else {
            secondaryMobile2.setError(null);
        }

        if (landline1.getText().toString().trim().isEmpty() && !landline2.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            landline1.setError(getString(R.string.empty_field));
            landline1.requestFocus();
            error = true;
        } else {
            landline1.setError(null);
        }

        if (landline2.getText().toString().trim().isEmpty() && !landline1.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            landline2.setError(getString(R.string.empty_field));
            landline2.requestFocus();
            error = true;
        } else {
            landline2.setError(null);
        }

        if (secondaryLandline1.getText().toString().trim().isEmpty() && !secondaryLandline2.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryLandline1.setError(getString(R.string.empty_field));
            secondaryLandline1.requestFocus();
            error = true;
        } else {
            secondaryLandline1.setError(null);
        }

        if (secondaryLandline2.getText().toString().trim().isEmpty() && !secondaryLandline1.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryLandline2.setError(getString(R.string.empty_field));
            secondaryLandline2.requestFocus();
            error = true;
        } else {
            secondaryLandline2.setError(null);
        }

        if (App.get(mobile1).length() != 4) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile1.setError(getString(R.string.length_message));
            mobile1.requestFocus();
            error = true;
        }

        if (App.get(mobile2).length() != 7) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile2.setError(getString(R.string.length_message));
            mobile2.requestFocus();
            error = true;
        }

        if (!(secondaryMobile1.getText().toString().trim().isEmpty() && secondaryMobile2.getText().toString().trim().isEmpty()) && App.get(secondaryMobile1).length() != 4) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryMobile1.setError(getString(R.string.length_message));
            secondaryMobile1.requestFocus();
            error = true;
        }

        if (!(secondaryMobile1.getText().toString().trim().isEmpty() && secondaryMobile2.getText().toString().trim().isEmpty()) && App.get(secondaryMobile2).length() != 7) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryMobile2.setError(getString(R.string.length_message));
            secondaryMobile2.requestFocus();
            error = true;
        }

        if (!(landline1.getText().toString().trim().isEmpty() && landline2.getText().toString().trim().isEmpty()) && !(App.get(landline1).length() == 3 || App.get(landline1).length() == 4)) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            landline1.setError(getString(R.string.length_message));
            landline1.requestFocus();
            error = true;
        }

        if (!(landline1.getText().toString().trim().isEmpty() && landline2.getText().toString().trim().isEmpty()) && App.get(landline2).length() != 7) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            landline2.setError(getString(R.string.length_message));
            landline2.requestFocus();
            error = true;
        }

        if (!(secondaryLandline1.getText().toString().trim().isEmpty() && secondaryLandline2.getText().toString().trim().isEmpty()) && !(App.get(secondaryLandline1).length() == 3 || App.get(secondaryLandline1).length() == 4)) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryLandline1.setError(getString(R.string.length_message));
            secondaryLandline1.requestFocus();
            error = true;
        }

        if (!(secondaryLandline1.getText().toString().trim().isEmpty() && secondaryLandline2.getText().toString().trim().isEmpty()) && App.get(secondaryLandline2).length() != 7) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryLandline2.setError(getString(R.string.length_message));
            secondaryLandline2.requestFocus();
            error = true;
        }

        final String mobileNumber = mobile1.getText().toString() + mobile2.getText().toString();
        final String secondaryMobileNumber = secondaryMobile1.getText().toString() + secondaryMobile2.getText().toString();
        final String landlineNumber = landline1.getText().toString() + landline2.getText().toString();
        final String secondaryLandlineLinearLayout = secondaryLandline1.getText().toString() + secondaryLandline2.getText().toString();

        if (!RegexUtil.isMobileNumber(mobileNumber)) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile2.setError(getString(R.string.incorrect_contact_number));
            mobile2.requestFocus();
            error = true;
        } else {
            mobile2.setError(null);
        }

        if (!App.get(secondaryMobile1).isEmpty() && !App.get(secondaryMobile2).isEmpty() && !RegexUtil.isMobileNumber(secondaryMobileNumber)) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryMobile2.setError(getString(R.string.incorrect_contact_number));
            secondaryMobile2.requestFocus();
            error = true;
        } else {
            secondaryMobile2.setError(null);
        }

        if (!App.get(landline1).isEmpty() && !App.get(landline2).isEmpty() && !RegexUtil.isLandlineNumber(landlineNumber)) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            landline2.setError(getString(R.string.incorrect_contact_number));
            landline2.requestFocus();
            error = true;
        } else {
            landline2.setError(null);
        }


        if (!App.get(secondaryLandline1).isEmpty() && !App.get(secondaryLandline2).isEmpty() && !RegexUtil.isLandlineNumber(secondaryLandlineLinearLayout)) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryLandline2.setError(getString(R.string.incorrect_contact_number));
            secondaryLandline2.requestFocus();
            error = true;
        } else {
            secondaryLandline2.setError(null);
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            if(!emptyError)
                alertDialog.setMessage(getString(R.string.form_error));
            else
                alertDialog.setMessage(getString(R.string.fast_required_field_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
         //   DrawableCompat.setTint(clearIcon, color);
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
        final HashMap<String, String> personAttribute = new HashMap<String, String>();

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

        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});

        final String cnicNumber = cnic1.getText().toString() + "-" + cnic2.getText().toString() + "-" + cnic3.getText().toString();
        final String mobileNumber = mobile1.getText().toString() + "-" + mobile2.getText().toString();
        final String secondaryMobileNumber = secondaryMobile1.getText().toString() + "-" + secondaryMobile2.getText().toString();
        final String landlineNumber = landline1.getText().toString() + "-" + landline2.getText().toString();
        final String secondaryLandlineLinearLayout = secondaryLandline1.getText().toString() + "-" + secondaryLandline2.getText().toString();

        if (contactExternalId.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CONTACT EXTERNAL ID", App.get(contactExternalId)});

        final String finalSource = App.get(patientSource).equals(getResources().getString(R.string.screening)) ? "IDENTIFIED PATIENT THROUGH SCREENING" :
                (App.get(patientSource).equals(getResources().getString(R.string.referred)) ? "PATIENT REFERRED" :
                        (App.get(patientSource).equals(getResources().getString(R.string.contact_patient)) ? "TUBERCULOSIS CONTACT" :
                                (App.get(patientSource).equals(getResources().getString(R.string.walkin)) ? "WALK IN" : "OTHER PATIENT SOURCE")));
        observations.add(new String[]{"PATIENT SOURCE", finalSource});

        if(otherPatientSource.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER PATIENT SOURCE", App.get(otherPatientSource)});

        if(indexId.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT ID OF INDEX CASE", App.get(indexId)});

        if(indexFirstName.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"INDEX PATIENT NAME", App.get(indexFirstName)});

        if(!cnicNumber.equals("--")){
            observations.add(new String[]{"NATIONAL IDENTIFICATION NUMBER", cnicNumber});
            personAttribute.put("National ID",cnicNumber);
        }

        String ownerString = "";
        if (cnicOwner.getVisibility() == View.VISIBLE){
            ownerString = App.get(cnicOwner).equals(getResources().getString(R.string.pet_self)) ? "SELF" :
                    (App.get(cnicOwner).equals(getResources().getString(R.string.pet_mother)) ? "MOTHER" :
                            (App.get(cnicOwner).equals(getResources().getString(R.string.pet_father)) ? "FATHER" :
                                    (App.get(cnicOwner).equals(getResources().getString(R.string.pet_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                            (App.get(cnicOwner).equals(getResources().getString(R.string.pet_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.pet_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.pet_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.pet_brother)) ? "BROTHER" :
                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.pet_sister)) ? "SISTER" :
                                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.pet_son)) ? "SON" :
                                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.pet_daughter)) ? "DAUGHTER" :
                                                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.pet_spouse)) ? "SPOUSE" :
                                                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.pet_aunt)) ? "AUNT" :
                                                                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.pet_uncle)) ? "UNCLE" : "OTHER FAMILY MEMBER")))))))))))));


            observations.add(new String[]{"COMPUTERIZED NATIONAL IDENTIFICATION OWNER", ownerString});
            String[][] cnicOwnerConcept = serverService.getConceptUuidAndDataType(ownerString);
            personAttribute.put("National ID Owner",cnicOwnerConcept[0][0]);
        }
        final String finalOwnerString = ownerString;
        if (otherCnicOwner.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER", App.get(otherCnicOwner)});

        if (addressProvided.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT PROVIDED ADDRESS", App.get(addressProvided).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});

        if (addressType.getVisibility() == View.VISIBLE && !addressType.getRadioGroup().getSelectedValue().isEmpty())
            observations.add(new String[]{"TYPE OF ADDRESS", App.get(addressType).equals(getResources().getString(R.string.fast_perminant)) ? "PERMANENT ADDRESS" : "TEMPORARY ADDRESS"});

        if (nearestLandmark.getVisibility() == View.VISIBLE && !App.get(nearestLandmark).isEmpty())
            observations.add(new String[]{"NEAREST LANDMARK", App.get(nearestLandmark)});

        if (contactPermission.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PERMISSION TO USE CONTACT NUMBER", App.get(contactPermission).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});

        observations.add(new String[]{"CONTACT PHONE NUMBER", mobileNumber});
        if(!dummyRadio.getCheckedBoxes().get(0).isChecked()){
            personAttribute.put("Primary Contact",mobileNumber);
        }

        if (!(App.get(secondaryMobile1).isEmpty() && App.get(secondaryMobile2).isEmpty())) {
            observations.add(new String[]{"SECONDARY MOBILE NUMBER", secondaryMobileNumber});
            personAttribute.put("Secondary Contact",secondaryMobileNumber);
        }

        if (!(App.get(landline1).isEmpty() && App.get(landline2).isEmpty())) {
            observations.add(new String[]{"TERTIARY CONTACT NUMBER", landlineNumber});
            personAttribute.put("Tertiary Contact",landlineNumber);
        }

        if (!(App.get(secondaryLandline1).isEmpty() && App.get(secondaryLandline2).isEmpty())) {
            observations.add(new String[]{"QUATERNARY CONTACT NUMBER", secondaryLandlineLinearLayout});
            personAttribute.put("Quaternary Contact",secondaryLandlineLinearLayout);
        }

        if (addressHouse.getVisibility() == View.VISIBLE && !App.get(addressHouse).isEmpty())
        observations.add(new String[]{"ADDRESS (TEXT)", App.get(addressHouse)});

     /*   if (addressStreet.getVisibility() == View.VISIBLE && !App.get(addressStreet).isEmpty())
        observations.add(new String[]{"EXTENDED ADDRESS (TEXT)", App.get(addressStreet)});*/

        if(province.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"PROVINCE", App.get(province)});
        }

        if(addressStreet.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"EXTENDED ADDRESS (TEXT)", App.get(addressStreet)});
        }

        if (district.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DISTRICT",App.get(district)});

        if (city.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"VILLAGE", App.get(city)});

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
                    id = serverService.saveFormLocallyTesting(Forms.PATIENT_INFORMATION_FORM, form, formDateCalendar,observations.toArray(new String[][]{}));

                String result = "";
                if (!(App.get(addressHouse).equals("") && App.get(addressStreet).equals("") && App.get(district).equals("") && App.get(nearestLandmark).equals(""))) {
                    result = serverService.savePersonAddress(App.get(addressHouse), App.get(addressStreet), App.get(city), App.get(district), App.get(province), App.getCountry(), App.getLongitude(), App.getLatitude(), App.get(nearestLandmark), id);
                    if (!result.contains("SUCCESS")){
                        return result;
                    }
                }

                result = serverService.saveMultiplePersonAttribute(personAttribute, id);
                if (!result.equals("SUCCESS"))
                    return result;

                if(!App.get(contactExternalId).equals("")) {
                    result = serverService.saveIdentifier("External ID", App.get(contactExternalId), id);
                    if (!result.contains("SUCCESS"))
                        return result;
                }

                result = serverService.saveEncounterAndObservationTesting(Forms.PATIENT_INFORMATION_FORM, form, formDateCalendar, observations.toArray(new String[][]{}), id);
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
                    serverService.addTown(addressStreet.getText().toString());
                    if(isAdded()) MainActivity.backToMainMenu();
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
            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            }
            else if (obs[0][0].equals("PATIENT SOURCE")) {
                final String source = obs[0][1].equals("IDENTIFIED PATIENT THROUGH SCREENING") ? getResources().getString(R.string.screening) :
                        (obs[0][1].equals("PATIENT REFERRED") ? getResources().getString(R.string.referred) :
                                (obs[0][1].equals("TUBERCULOSIS CONTACT") ? getResources().getString(R.string.contact_patient) :
                                        (obs[0][1].equals("WALK IN") ? getResources().getString(R.string.walkin) : getResources().getString(R.string.other))));

                patientSource.getSpinner().selectValue(source);
                patientSource.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("OTHER PATIENT SOURCE")) {
                otherPatientSource.getEditText().setText(obs[0][1]);
                otherPatientSource.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("PATIENT ID OF INDEX CASE")) {
                indexId.getEditText().setText(obs[0][1]);
                indexId.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("INDEX PATIENT NAME")) {
                indexFirstName.getEditText().setText(obs[0][1]);
                indexFirstName.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("CONTACT EXTERNAL ID")) {
                contactExternalId.getEditText().setText(obs[0][1]);
                contactExternalId.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("NATIONAL IDENTIFICATION NUMBER")) {
                String data = obs[0][1];
                cnic1.setText(data.substring(0,5));
                cnic2.setText(data.substring(6,13));
                cnic3.setText(data.substring(14));
            } else if (obs[0][0].equals("COMPUTERIZED NATIONAL IDENTIFICATION OWNER")) {
                String value = obs[0][1].equals("SELF") ? getResources().getString(R.string.fast_self) :
                        (obs[0][1].equals("MOTHER") ? getResources().getString(R.string.fast_mother) :
                                (obs[0][1].equals("FATHER") ? getResources().getString(R.string.fast_father) :
                                        (obs[0][1].equals("SISTER") ? getResources().getString(R.string.fast_sister) :
                                                (obs[0][1].equals("BROTHER") ? getResources().getString(R.string.fast_brother) :
                                                        (obs[0][1].equals("SPOUSE") ? getResources().getString(R.string.fast_spouse) :
                                                                (obs[0][1].equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.fast_paternal_grandfather) :
                                                                        (obs[0][1].equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.fast_paternal_grandmother) :
                                                                                (obs[0][1].equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.fast_maternal_grandfather) :
                                                                                        (obs[0][1].equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.fast_maternal_grandmother) :
                                                                                                (obs[0][1].equals("UNCLE") ? getResources().getString(R.string.fast_uncle) :
                                                                                                        (obs[0][1].equals("AUNT") ? getResources().getString(R.string.fast_aunt) :
                                                                                                                (obs[0][1].equals("SON") ? getResources().getString(R.string.fast_son) :
                                                                                                                        (obs[0][1].equals("DAUGHTER") ? getResources().getString(R.string.fast_daughter) :
                                                                                                                                getResources().getString(R.string.fast_other_title))))))))))))));


                cnicOwner.getSpinner().selectValue(value);
                cnicOwner.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER")) {
                otherCnicOwner.getEditText().setText(obs[0][1]);
                otherCnicOwner.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("PATIENT PROVIDED ADDRESS")) {

                for (RadioButton rb : addressProvided.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                addressProvided.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TYPE OF ADDRESS")) {

                for (RadioButton rb : addressType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_perminant)) && obs[0][1].equals("PERMANENT ADDRESS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_temporary)) && obs[0][1].equals("TEMPORARY ADDRESS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                addressType.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("NEAREST LANDMARK")) {
                nearestLandmark.getEditText().setText(obs[0][1]);
                nearestLandmark.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("PERMISSION TO USE CONTACT NUMBER")) {

                for (RadioButton rb : contactPermission.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                contactPermission.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("ADDRESS (TEXT)")) {
                addressHouse.getEditText().setText(obs[0][1]);
                addressHouse.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("EXTENDED ADDRESS (TEXT)")) {
                addressStreet.setText(obs[0][1]);
                addressStreet.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("PROVINCE")) {
                province.getSpinner().selectValue(obs[0][1]);
                province.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("DISTRICT")) {
                String[] districts = serverService.getDistrictList(App.get(province));
                district.getSpinner().setSpinnerData(districts);
                district.getSpinner().selectValue(obs[0][1]);
                district.getSpinner().setTag("selected");
                district.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("VILLAGE")) {
                String[] cities = serverService.getCityList(App.get(district));
                city.getSpinner().setSpinnerData(cities);
                city.getSpinner().selectValue(obs[0][1]);
                city.getSpinner().setTag("selected");
                city.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("CONTACT PHONE NUMBER")) {
                String mobNum = obs[0][1];
                if(mobNum.equals("0399-9999999")){
                    dummyRadio.getCheckedBoxes().get(0).setChecked(true);
                    mobile1.setText(mobNum.substring(0,4));
                    mobile1.setEnabled(false);
                    mobile2.setText(mobNum.substring(5,12));
                    mobile2.setEnabled(false);
                }else{
                    mobile1.setText(mobNum.substring(0,4));
                    mobile2.setText(mobNum.substring(5,12));
                }

            }
            else if (obs[0][0].equals("SECONDARY MOBILE NUMBER")) {
                String mobNum2 = obs[0][1];
                secondaryMobile1.setText(mobNum2.substring(0,4));
                secondaryMobile2.setText(mobNum2.substring(5,12));
            }
            else if (obs[0][0].equals("TERTIARY CONTACT NUMBER")) {
                String landNum = obs[0][1];
                if(landNum.length() == 11) {
                    landline1.setText(landNum.substring(0, 4));
                    landline2.setText(landNum.substring(5, 12));
                }
                else{
                    landline1.setText(landNum.substring(0, 3));
                    landline2.setText(landNum.substring(4, 11));
                }
            }
            else if (obs[0][0].equals("QUATERNARY CONTACT NUMBER")) {
                String landNum1 = obs[0][1];
                if(landNum1.length() == 11) {
                    secondaryLandline1.setText(landNum1.substring(0, 4));
                    secondaryLandline2.setText(landNum1.substring(5, 12));
                }
                else{
                    secondaryLandline1.setText(landNum1.substring(0, 3));
                    secondaryLandline2.setText(landNum1.substring(4, 11));
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
        } else if(view == scanBarcode.getButton()){
            try {
                Intent intent = new Intent(Barcode.BARCODE_INTENT);
                if (App.isCallable(context, intent)) {
                    intent.putExtra(Barcode.SCAN_MODE, Barcode.QR_MODE);
                    startActivityForResult(intent, Barcode.BARCODE_RESULT);
                } else {
                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getString(R.string.barcode_scanner_missing));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    //DrawableCompat.setTint(clearIcon, color);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            } catch (ActivityNotFoundException e) {
                //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage(getString(R.string.barcode_scanner_missing));
                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                //DrawableCompat.setTint(clearIcon, color);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_error));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        } else if (view == fetchIndexPatient.getButton()) {

            AsyncTask<String, String, String> submissionFormTask = new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... params) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading.setInverseBackgroundForced(true);
                            loading.setIndeterminate(true);
                            loading.setCancelable(false);
                            loading.setMessage(getResources().getString(R.string.fetch_index_patient));
                            loading.show();
                        }
                    });

                    String val =  serverService.getPatient(App.get(indexId), false);
                    if(val == null) return "CONNECTION_ERROR";
                    else return val;

                }

                @Override
                protected void onProgressUpdate(String... values) {
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    loading.dismiss();

                    if (result.equals("SUCCESS") || result.equals("OFFLINE_PATIENT")) {

                        String[][] names = serverService.getPatientNameFromLocalDB(App.get(indexId));

                        if(names[0][1].equals(""))
                            indexFirstName.getEditText().setText(names[0][0] + " " + names[0][1]);
                        else
                            indexFirstName.getEditText().setText(names[0][0]);

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

        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        for (CheckBox cb : dummyRadio.getCheckedBoxes()) {
//            KeyListener mobile1KeyListener = mobile1.getKeyListener();
//            KeyListener mobile2KeyListener = mobile2.getKeyListener();
            if (dummyRadio.getCheckedBoxes().get(0).isChecked()) {
                mobile1.setText("0399");
//                mobile1.setKeyListener(null);
                mobile1.setEnabled(false);
                mobile2.setText("9999999");
                mobile2.setEnabled(false);
//                mobile2.setOnKeyListener(null);
                secondaryMobile1.requestFocus();
            } else {
                mobile1.setDefaultValue();
                mobile2.setDefaultValue();
//                mobile1.setKeyListener(mobile1KeyListener);
//                mobile2.setKeyListener(mobile2KeyListener);
                mobile1.setEnabled(true);
                mobile2.setEnabled(true);
                mobile1.requestFocus();
            }
        }

    }

    @Override
    public void resetViews() {
        Object[][] towns = serverService.getAllTowns();
        String[] townList = new String[towns.length];

        for (int i = 0; i < towns.length; i++) {
            townList[i] = String.valueOf(towns[i][1]);
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, townList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addressStreet.setAdapter(spinnerArrayAdapter);


        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        cnicOwner.setVisibility(View.GONE);
        otherCnicOwner.setVisibility(View.GONE);
        otherPatientSource.setVisibility(View.GONE);
        indexId.setVisibility(View.GONE);
        scanBarcode.setVisibility(View.GONE);
        fetchIndexPatient.setVisibility(View.GONE);
        indexFirstName.setVisibility(View.GONE);
       // townTextView.setVisibility(View.GONE);

        String[] districts = serverService.getDistrictList(App.getProvince());
        district.getSpinner().setSpinnerData(districts);

        String[] cities = serverService.getCityList(App.get(district));
        city.getSpinner().setSpinnerData(cities);

        Bundle bundle = this.getArguments();
        Boolean autoFill = false;
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);
                autoFill = true;

                refill(formId);

            } else bundle.putBoolean("save", false);

        }

        if(!autoFill) {
            final AsyncTask<String, String, HashMap<String, String>> autopopulateFormTask = new AsyncTask<String, String, HashMap<String, String>>() {
                @Override
                protected HashMap<String, String> doInBackground(String... params) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading.setInverseBackgroundForced(true);
                            loading.setIndeterminate(true);
                            loading.setCancelable(false);
                            loading.setMessage(getResources().getString(R.string.fetching_data));
                            loading.show();
                        }
                    });

                    HashMap<String, String> result = new HashMap<String, String>();
                    String indexId = serverService.getLatestObsValue(App.getPatientId(), "PATIENT ID OF INDEX CASE");
                    String patientSource = serverService.getLatestObsValue(App.getPatientId(), "PATIENT SOURCE");

                    if (indexId != null && !indexId.equals("")) {
                        serverService.getPatient(indexId, false);
                        result.put("PATIENT ID OF INDEX CASE", indexId);
                    } else result.put("PATIENT ID OF INDEX CASE", "");

                    if (patientSource != null) {
                        result.put("PATIENT SOURCE", patientSource);
                    } else if(indexId!=null) {
                        result.put("PATIENT SOURCE", "TUBERCULOSIS CONTACT");
                    }

                    return result;

                }

                @Override
                protected void onProgressUpdate(String... values) {
                }

                ;

                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();

                    indexId.getEditText().setText(result.get("PATIENT ID OF INDEX CASE"));
                    if(!App.get(indexId).equals(""))
                        indexId.getEditText().setEnabled(false);

                    String patientSourceString = result.get("PATIENT SOURCE");
                    if(patientSourceString!=null ){
                        String val = patientSourceString.equals("IDENTIFIED PATIENT THROUGH SCREENING") ? getResources().getString(R.string.screening) :
                                patientSourceString.equals("PATIENT REFERRED") ? getResources().getString(R.string.referred) :
                                        patientSourceString.equals("TUBERCULOSIS CONTACT") ? getResources().getString(R.string.contact_patient) :
                                                (patientSourceString.equals("WALK IN") ? getResources().getString(R.string.walkin) : getResources().getString(R.string.ctb_other_title) );
                        if(val.equals(getResources().getString(R.string.ctb_other_title))){
                            otherPatientSource.setVisibility(View.VISIBLE);
                        }
                        patientSource.getSpinner().selectValue(val);
                        patientSource.getSpinner().setEnabled(false);
                    }

                }
            };
            autopopulateFormTask.execute("");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;

        if (spinner == patientSource.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.other))) {
                otherPatientSource.setVisibility(View.VISIBLE);
                indexId.setVisibility(View.GONE);
                scanBarcode.setVisibility(View.GONE);
                fetchIndexPatient.setVisibility(View.GONE);
                indexFirstName.setVisibility(View.GONE);
            } else if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.contact_patient))){
                indexId.setVisibility(View.VISIBLE);
                scanBarcode.setVisibility(View.VISIBLE);
                fetchIndexPatient.setVisibility(View.VISIBLE);
                indexFirstName.setVisibility(View.VISIBLE);
                otherPatientSource.setVisibility(View.GONE);
            } else {
                indexId.setVisibility(View.GONE);
                scanBarcode.setVisibility(View.GONE);
                fetchIndexPatient.setVisibility(View.GONE);
                indexFirstName.setVisibility(View.GONE);
                otherPatientSource.setVisibility(View.GONE);
            }
        } else if (spinner == cnicOwner.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title)) && cnicOwner.getVisibility() == View.VISIBLE) {
                otherCnicOwner.setVisibility(View.VISIBLE);
            } else {
                otherCnicOwner.setVisibility(View.GONE);
            }
        } else if (spinner == district.getSpinner()) {

            if(district.getSpinner().getTag() == null) {

                String[] cities = serverService.getCityList(App.get(district));
                city.getSpinner().setSpinnerData(cities);
            }
            else city.getSpinner().setTag(null);

        } else if (spinner == province.getSpinner()) {

            if(province.getSpinner().getTag() == null) {
                String[] districts = serverService.getDistrictList(App.get(province));
                district.getSpinner().setSpinnerData(districts);
            }
            else province.getSpinner().setTag(null);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if (radioGroup == addressProvided.getRadioGroup()) {
            if (addressProvided.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                addressHouse.setVisibility(View.VISIBLE);
                addressStreet.setVisibility(View.VISIBLE);
                townTextView.setVisibility(View.VISIBLE);
            } else {
                addressHouse.setVisibility(View.GONE);
                addressStreet.setVisibility(View.GONE);
                townTextView.setVisibility(View.GONE);
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

    public boolean isValidContactNumber(String number) {
        boolean valid = true;
        if (number.length() < 9) {
            return false;
        }
        char[] array = number.toCharArray();
        int occurrances = 0;
        // Similarity check
        for (int i = 1; i < array.length; i++) {
            if (array[i] == array[i - 1]) {
                ++occurrances;
            } else {
                occurrances = 0;
            }
            if (occurrances >= 5) {
                return false;
            }
        }
        // Series check
        for (int i = 1; i < array.length; i++) {
            if (Math.abs(array[i] - array[i - 1]) == 1) {
                ++occurrances;
            } else {
                occurrances = 0;
            }
            if (occurrances >= 5) {
                return false;
            }
        }
        return valid;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Retrieve barcode scan results
        if (requestCode == Barcode.BARCODE_RESULT) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra(Barcode.SCAN_RESULT);
                // Check for valid Id
                if (RegexUtil.isValidId(str)) {
                    indexId.getEditText().setText(str);
                    indexId.getEditText().requestFocus();
                } else {

                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);

                    indexId.getEditText().setText("");
                    indexId.getEditText().requestFocus();

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getString(R.string.invalid_scanned_id));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    //DrawableCompat.setTint(clearIcon, color);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
            } else if (resultCode == RESULT_CANCELED) {

                int color = App.getColor(context, R.attr.colorAccent);

                indexId.getEditText().setText("");
                indexId.getEditText().requestFocus();

                /*final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this).create();
                alertDialog.setMessage(getString(R.string.warning_before_clear));
                Drawable clearIcon = getResources().getDrawable(R.drawable.ic_clear);
                DrawableCompat.setTint(clearIcon, color);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_clear));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();*/

            }
            // Set the locale again, since the Barcode app restores system's
            // locale because of orientation
            Locale.setDefault(App.getCurrentLocale());
            Configuration config = new Configuration();
            config.locale = App.getCurrentLocale();
            context.getResources().updateConfiguration(config, null);
        }
    }

}