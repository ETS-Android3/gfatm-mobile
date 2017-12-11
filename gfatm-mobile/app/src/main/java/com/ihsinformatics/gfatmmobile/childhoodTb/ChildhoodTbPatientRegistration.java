package com.ihsinformatics.gfatmmobile.childhoodTb;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
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
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbPatientRegistration extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    MyTextView nurseInstruction;
    MyEditText cnic1;
    MyEditText cnic2;
    MyEditText cnic3;
    TitledSpinner cnicOwner;
    TitledEditText cnicOwnerOther;
    TitledRadioGroup addressProvided;
    MyLinearLayout addressLayout;
    LinearLayout addressQuestion;
    MyTextView townTextView;
    AutoCompleteTextView address2;
    TitledSpinner province;
    TitledEditText address1;
    TitledSpinner city;
    TitledSpinner district;
    TitledRadioGroup addressType;
    TitledEditText nearestLandmark;
    TitledSpinner mobileNumberContact;
    TitledRadioGroup permissionMobileNumberContact;
    TitledSpinner secondaryMobileNumberContact;
    TitledSpinner landlineNumberContact;
    TitledSpinner secondaryLandlineContact;
    LinearLayout cnicLayout;
    LinearLayout mobileLinearLayout;
    MyEditText mobileNumber1;
    MyEditText mobileNumber2;
    LinearLayout secondaryMobileLinearLayout;
    MyEditText secondaryMobileNumber1;
    MyEditText secondaryMobileNumber2;
    LinearLayout landlineLayout;
    MyEditText landlineNumber1;
    MyEditText landlineNumber2;
    LinearLayout secondaryLandlineNumber;
    MyEditText secondaryLandlineNumber1;
    MyEditText secondaryLandlineNumber2;
    TitledEditText externalID;

    String secondaryMobileNumberContactString;
    String primaryMobileNumberContactString;

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
        FORM_NAME = Forms.CHILDHOODTB_PATIENT_REGISTRATION;
        FORM = Forms.childhoodTb_patientRegistration;

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
        nurseInstruction = new MyTextView(context,getResources().getString(R.string.ctb_patient_reg_nurse_instruction));
        cnicLayout = new LinearLayout(context);
        cnicLayout.setOrientation(LinearLayout.VERTICAL);
        MyTextView cnic = new MyTextView(context, getResources().getString(R.string.pet_cnic));
        cnicLayout.addView(cnic);
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
        cnicLayout.addView(cnicPartLayout);
        cnicOwner = new TitledSpinner(context,null,getResources().getString(R.string.ctb_cnic_owner),getResources().getStringArray(R.array.ctb_close_contact_type_list),getResources().getString(R.string.ctb_mother),App.VERTICAL);
        cnicOwnerOther = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_specify),"","",20,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);

        addressProvided = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_address_provided),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);

        address1 = new TitledEditText(context,null,getResources().getString(R.string.ctb_address1),"","",50,RegexUtil.ADDRESS_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,true);

        addressLayout = new MyLinearLayout(context, null, App.VERTICAL);
        townTextView = new MyTextView(context, getResources().getString(R.string.ctb_address2));
        address2 = new AutoCompleteTextView(context);
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(20);
        address2.setFilters(fArray);

        addressQuestion = new LinearLayout(context);
        addressQuestion.setOrientation(LinearLayout.HORIZONTAL);
        addressQuestion.addView(townTextView);
        TextView mandatorySign2 = new TextView(context);
        mandatorySign2.setText("*");
        mandatorySign2.setTextColor(Color.parseColor("#ff0000"));
        addressQuestion.addView(mandatorySign2);
        addressLayout.addView(addressQuestion);
        addressLayout.addView(address2);
        province = new TitledSpinner(context, "", getResources().getString(R.string.province), getResources().getStringArray(R.array.provinces), App.getProvince(), App.VERTICAL);

        city = new TitledSpinner(context, "", getResources().getString(R.string.pet_city), getResources().getStringArray(R.array.pet_empty_array), "", App.VERTICAL,true);
        district = new TitledSpinner(context, "", getResources().getString(R.string.pet_district), getResources().getStringArray(R.array.pet_empty_array), "", App.VERTICAL);
        addressType = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_address_type),getResources().getStringArray(R.array.ctb_address_type_list),null,App.HORIZONTAL,App.VERTICAL,true);
        nearestLandmark = new TitledEditText(context,null,getResources().getString(R.string.ctb_nearest_landmark),"","",50,RegexUtil.OTHER_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        mobileLinearLayout = new LinearLayout(context);
        mobileLinearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout mobileQuestion = new LinearLayout(context);
        mobileQuestion.setOrientation(LinearLayout.HORIZONTAL);
        MyTextView mobileNumberLabel = new MyTextView(context, getResources().getString(R.string.ctb_mobile_number));
        mobileQuestion.addView(mobileNumberLabel);
        TextView mandatorySign = new TextView(context);
        mandatorySign.setText("*");
        mandatorySign.setTextColor(Color.parseColor("#ff0000"));
        mobileQuestion.addView(mandatorySign);
        mobileLinearLayout.addView(mobileQuestion);
        LinearLayout mobileNumberPart = new LinearLayout(context);
        mobileNumberPart.setOrientation(LinearLayout.HORIZONTAL);
        mobileNumber1 = new MyEditText(context,"", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        mobileNumber1.setHint("03XX");
        mobileNumberPart.addView(mobileNumber1);
        MyTextView mobileNumberDash = new MyTextView(context, " - ");
        mobileNumberPart.addView(mobileNumberDash);
        mobileNumber2 = new MyEditText(context,"",  7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        mobileNumber2.setHint("XXXXXXX");
        mobileNumberPart.addView(mobileNumber2);

        mobileLinearLayout.addView(mobileNumberPart);

        mobileNumberContact = new TitledSpinner(context,null,getResources().getString(R.string.ctb_whose_contact),getResources().getStringArray(R.array.ctb_close_contact_type_list),null,App.VERTICAL);
        permissionMobileNumberContact = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_sms_or_call),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);
        secondaryMobileLinearLayout = new LinearLayout(context);
        secondaryMobileLinearLayout.setOrientation(LinearLayout.VERTICAL);
        MyTextView secondaryMobileNumberLabel = new MyTextView(context, getResources().getString(R.string.ctb_secondary_mobile_number));
        secondaryMobileLinearLayout.addView(secondaryMobileNumberLabel);
        LinearLayout secondaryMobileNumberPart = new LinearLayout(context);
        secondaryMobileNumberPart.setOrientation(LinearLayout.HORIZONTAL);
        secondaryMobileNumber1 = new MyEditText(context,"", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        secondaryMobileNumber1.setHint("03XX");
        secondaryMobileNumberPart.addView(secondaryMobileNumber1);
        MyTextView secondarysecondaryMobileNumberDash = new MyTextView(context, " - ");
        secondaryMobileNumberPart.addView(secondarysecondaryMobileNumberDash);
        secondaryMobileNumber2 = new MyEditText(context,"",  7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        secondaryMobileNumber2.setHint("XXXXXXX");
        secondaryMobileNumberPart.addView(secondaryMobileNumber2);

        secondaryMobileLinearLayout.addView(secondaryMobileNumberPart);
        secondaryMobileNumberContact = new TitledSpinner(context,null,getResources().getString(R.string.ctb_whose_contact),getResources().getStringArray(R.array.ctb_close_contact_type_list),null,App.VERTICAL);
        landlineLayout = new LinearLayout(context);
        landlineLayout.setOrientation(LinearLayout.VERTICAL);
        MyTextView landlineLabel = new MyTextView(context, getResources().getString(R.string.ctb_landline_number));
        landlineLayout.addView(landlineLabel);
        LinearLayout landlineNumberPart = new LinearLayout(context);
        landlineNumberPart.setOrientation(LinearLayout.HORIZONTAL);
        landlineNumber1 = new MyEditText(context,"", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        landlineNumber1.setHint("0XX");
        landlineNumberPart.addView(landlineNumber1);
        MyTextView landlineNumberDash = new MyTextView(context, " - ");
        landlineNumberPart.addView(landlineNumberDash);
        landlineNumber2 = new MyEditText(context,"",  8, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        landlineNumber2.setHint("XXXXXXX");
        landlineNumberPart.addView(landlineNumber2);

        landlineLayout.addView(landlineNumberPart);
        landlineNumberContact = new TitledSpinner(context,null,getResources().getString(R.string.ctb_whose_contact),getResources().getStringArray(R.array.ctb_close_contact_type_list),null,App.VERTICAL);
        secondaryLandlineNumber = new LinearLayout(context);
        secondaryLandlineNumber.setOrientation(LinearLayout.VERTICAL);
        MyTextView secondaryLandlineLabel = new MyTextView(context, getResources().getString(R.string.ctb_secondary_landline_number));
        secondaryLandlineNumber.addView(secondaryLandlineLabel);
        LinearLayout secondaryLandlineNumberPart = new LinearLayout(context);
        secondaryLandlineNumberPart.setOrientation(LinearLayout.HORIZONTAL);
        secondaryLandlineNumber1 = new MyEditText(context,"", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        secondaryLandlineNumber1.setHint("0XX");
        secondaryLandlineNumberPart.addView(secondaryLandlineNumber1);
        MyTextView secondaryLandlineNumberDash = new MyTextView(context, " - ");
        secondaryLandlineNumberPart.addView(secondaryLandlineNumberDash);
        secondaryLandlineNumber2 = new MyEditText(context,"",  8, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        secondaryLandlineNumber2.setHint("XXXXXXX");
        secondaryLandlineNumberPart.addView(secondaryLandlineNumber2);

        secondaryLandlineNumber.addView(secondaryLandlineNumberPart);
        secondaryLandlineContact = new TitledSpinner(context,null,getResources().getString(R.string.ctb_whose_contact),getResources().getStringArray(R.array.ctb_close_contact_type_list),null,App.VERTICAL);
        externalID = new TitledEditText(context,null,getResources().getString(R.string.ctb_external_id),"","",20,RegexUtil.OTHER_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);


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
                }else{
                    cnicOwner.setVisibility(View.GONE);
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
                }
                else{
                    cnicOwner.setVisibility(View.GONE);
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
                }else{
                    cnicOwner.setVisibility(View.GONE);
                }
            }
        });

        mobileNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mobileNumber = mobileNumber1.getText() + s.toString();
                if(RegexUtil.isMobileNumber(mobileNumber)){
                    mobileNumber2.setError(null);
                    mobileNumberContact.setVisibility(View.VISIBLE);
                    permissionMobileNumberContact.setVisibility(View.VISIBLE);
                }
                else{
                    mobileNumber2.setError("Invalid Mobile Number");
                    mobileNumberContact.setVisibility(View.GONE);
                    permissionMobileNumberContact.setVisibility(View.GONE);
                }

                if(s.length()==0){
                    mobileNumber1.requestFocus();
                    mobileNumber1.setSelection(mobileNumber1.getText().length());
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobileNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mobileNumber = s.toString() + mobileNumber2.getText().toString();
                if(RegexUtil.isMobileNumber(mobileNumber)){
                        mobileNumber2.setError(null);
                        mobileNumberContact.setVisibility(View.VISIBLE);
                        permissionMobileNumberContact.setVisibility(View.VISIBLE);
                }
                else{
                    mobileNumber2.setError("Invalid Mobile Number");
                    mobileNumberContact.setVisibility(View.GONE);
                    permissionMobileNumberContact.setVisibility(View.GONE);
                }
                if(s.length()==0 && mobileNumber2.getText().length()==0){
                    mobileNumber2.setError(null);
                }

                if(s.length()==4){
                    mobileNumber2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        secondaryMobileNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mobileNumber = secondaryMobileNumber1.getText() + s.toString();
                if(RegexUtil.isMobileNumber(mobileNumber)){
                    secondaryMobileNumber2.setError(null);
                    secondaryMobileNumberContact.setVisibility(View.VISIBLE);
                }
                else{
                    secondaryMobileNumber2.setError("Invalid Phone number");
                    secondaryMobileNumberContact.setVisibility(View.GONE);
                }
                if(s.length()==0){
                    secondaryMobileNumber1.requestFocus();
                    secondaryMobileNumber1.setSelection(secondaryMobileNumber1.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        secondaryMobileNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String secondaryMobileNumber = s.toString() + secondaryMobileNumber2.getText().toString();
                if(RegexUtil.isMobileNumber(secondaryMobileNumber)){
                    secondaryMobileNumberContact.setVisibility(View.VISIBLE);
                    secondaryMobileNumber2.setError(null);
                }
                else{
                    secondaryMobileNumber2.setError("Invalid Phone number");
                    secondaryMobileNumberContact.setVisibility(View.GONE);
                }
                if(s.length()==0 && secondaryMobileNumber2.getText().length()==0){
                    secondaryMobileNumber2.setError(null);
                }

                if(s.length()==4){
                    secondaryMobileNumber2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        landlineNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String landlineNumber = landlineNumber1.getText() + s.toString();
                if(RegexUtil.isContactNumber(landlineNumber)){
                    landlineNumberContact.setVisibility(View.VISIBLE);
                    landlineNumber2.setError(null);
                }
                else{
                    landlineNumber2.setError("Invalid Landline number");
                    landlineNumberContact.setVisibility(View.GONE);
                }

                if(s.length()==0){
                    landlineNumber1.requestFocus();
                    landlineNumber1.setSelection(landlineNumber1.getText().length());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        landlineNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String landlineNumber = s.toString() + landlineNumber2.getText().toString();
                if(RegexUtil.isContactNumber(landlineNumber)){
                    landlineNumberContact.setVisibility(View.VISIBLE);
                    landlineNumber2.setError(null);
                }
                else{
                    landlineNumber2.setError("Invalid Landline number");
                    landlineNumberContact.setVisibility(View.GONE);
                }
                if(s.length()==0 && landlineNumber2.getText().length()==0){
                    landlineNumber2.setError(null);
                }

                if(s.length()==3){
                    landlineNumber2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        secondaryLandlineNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String landlineNumber = secondaryLandlineNumber1.getText() + s.toString();
                if(RegexUtil.isContactNumber(landlineNumber)){
                    secondaryLandlineNumber2.setError(null);
                    secondaryLandlineContact.setVisibility(View.VISIBLE);
                }
                else{
                    secondaryLandlineNumber2.setError("Invalid Landline number");
                    secondaryLandlineContact.setVisibility(View.GONE);
                }

                if(s.length()==0){
                    secondaryLandlineNumber1.requestFocus();
                    secondaryLandlineNumber1.setSelection(secondaryLandlineNumber1.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        secondaryLandlineNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String landlineNumber = secondaryLandlineNumber2.getText() + s.toString();
                if(RegexUtil.isContactNumber(landlineNumber)){
                    secondaryLandlineNumber2.setError(null);
                    secondaryLandlineContact.setVisibility(View.VISIBLE);
                }
                else{
                    secondaryLandlineNumber2.setError("Invalid Landline number");
                    secondaryLandlineContact.setVisibility(View.GONE);
                }

                if(s.length()==0 && secondaryLandlineNumber2.getText().length()==0){
                    secondaryLandlineNumber2.setError(null);
                }

                if(s.length()==3){
                    secondaryLandlineNumber2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        views = new View[]{formDate.getButton(),cnicLayout,cnicOwner.getSpinner(),cnicOwnerOther.getEditText(),addressProvided.getRadioGroup(), address1.getEditText(), province.getSpinner(),city.getSpinner(),district.getSpinner(),addressType.getRadioGroup(),nearestLandmark.getEditText(),mobileLinearLayout,mobileNumberContact.getSpinner(),permissionMobileNumberContact.getRadioGroup(),secondaryMobileLinearLayout,secondaryMobileNumberContact.getSpinner(),landlineLayout,landlineNumberContact.getSpinner(),secondaryLandlineNumber,secondaryLandlineContact.getSpinner(),externalID.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate,externalID,cnicLayout,cnicOwner,cnicOwnerOther,addressProvided,address1, addressLayout, province,district,city,addressType,nearestLandmark,mobileLinearLayout,mobileNumberContact,permissionMobileNumberContact,secondaryMobileLinearLayout,secondaryMobileNumberContact,landlineLayout,landlineNumberContact,secondaryLandlineNumber,secondaryLandlineContact}};

        formDate.getButton().setOnClickListener(this);
        cnicOwner.getSpinner().setOnItemSelectedListener(this);
        addressProvided.getRadioGroup().setOnCheckedChangeListener(this);
        city.getSpinner().setOnItemSelectedListener(this);
        province.getSpinner().setOnItemSelectedListener(this);
        addressType.getRadioGroup().setOnCheckedChangeListener(this);
        mobileNumberContact.getSpinner().setOnItemSelectedListener(this);
        permissionMobileNumberContact.getRadioGroup().setOnCheckedChangeListener(this);
        secondaryMobileNumberContact.getSpinner().setOnItemSelectedListener(this);
        landlineNumberContact.getSpinner().setOnItemSelectedListener(this);
        secondaryLandlineContact.getSpinner().setOnItemSelectedListener(this);
        district.getSpinner().setOnItemSelectedListener(this);
        resetViews();

    }

    @Override
    public void updateDisplay() {

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

            }  else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            }else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }
        formDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        boolean error=false;
        View view = null;
        Boolean flag = false;
        if (cnicOwnerOther.getVisibility() == View.VISIBLE) {
            if(App.get(cnicOwnerOther).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cnicOwnerOther.getEditText().setError(getString(R.string.empty_field));
                cnicOwnerOther.getEditText().requestFocus();
                error = true;
                view = null;
            }
            else if(App.get(cnicOwnerOther).trim().length() <= 0){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cnicOwnerOther.getEditText().setError(getString(R.string.ctb_spaces_only));
                cnicOwnerOther.getEditText().requestFocus();
                error = true;
                view = null;
            }
        }
        if(!App.get(secondaryLandlineNumber1).isEmpty() || !App.get(secondaryLandlineNumber2).isEmpty()){
            String secondaryLandline = secondaryLandlineNumber1.getText().toString() + secondaryLandlineNumber2.getText().toString();
            if (!RegexUtil.isContactNumber(secondaryLandline)) {
                secondaryLandlineNumber2.setError(getResources().getString(R.string.ctb_invalid_number));
                error = true;
                view = secondaryLandlineNumber;
            }
        }
        if(!App.get(landlineNumber1).isEmpty() || !App.get(landlineNumber2).isEmpty()){
            String landlineNumber = landlineNumber1.getText().toString() + landlineNumber2.getText().toString();
            if (!RegexUtil.isContactNumber(landlineNumber)) {
                landlineNumber2.setError(getResources().getString(R.string.ctb_invalid_number));
                error = true;
                view = landlineLayout;
            }
        }
        if(!App.get(secondaryMobileNumber2).isEmpty() || !App.get(secondaryMobileNumber1).isEmpty()){
            String secondaryMobileNumber = secondaryMobileNumber1.getText().toString() + secondaryMobileNumber2.getText().toString();
            if (!RegexUtil.isMobileNumber(secondaryMobileNumber)) {
                secondaryMobileNumber2.setError(getResources().getString(R.string.ctb_invalid_number));
                error = true;
                view = secondaryMobileLinearLayout;
            }
        }
        if (App.get(mobileNumber1).isEmpty() && App.get(mobileNumber2).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobileNumber2.setError(getString(R.string.empty_field));
            error = true;
            view = mobileLinearLayout;
        }
        else{
            String mobileNumber = mobileNumber1.getText().toString() + mobileNumber2.getText().toString();
            if (!RegexUtil.isMobileNumber(mobileNumber)) {
                mobileNumber2.setError(getResources().getString(R.string.ctb_invalid_number));
                error = true;
                view = mobileLinearLayout;
            }
        }if(address1.getVisibility()==View.VISIBLE){
            if(App.get(address1).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                address1.getEditText().setError(getString(R.string.empty_field));
                address1.getEditText().requestFocus();
                error = true;
                view = null;
            }
            else if(App.get(address1).trim().length() <= 0){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                address1.getEditText().setError(getString(R.string.ctb_spaces_only));
                address1.getEditText().requestFocus();
                error = true;
                view = null;
            }
        }
        if(address2.getVisibility()==View.VISIBLE){
            if(App.get(address2).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                address2.setError(getString(R.string.empty_field));
                address2.requestFocus();
                error = true;
                view = null;
            }
            else if(App.get(address1).trim().length() <= 0){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                address2.setError(getString(R.string.ctb_spaces_only));
                address2.requestFocus();
                error = true;
                view = null;
            }
        }
        if (App.get(nearestLandmark).trim().length() <= 0 && !App.get(nearestLandmark).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            nearestLandmark.getEditText().setError(getString(R.string.ctb_spaces_only));
            nearestLandmark.getEditText().requestFocus();
            error = true;
            view = null;
        }

        if(!App.get(cnic1).isEmpty() && !App.get(cnic2).isEmpty() && !App.get(cnic3).isEmpty())  {
            String cnic = cnic1.getText().toString() + "-" + cnic2.getText().toString() + "-" + cnic3.getText().toString();
            if (!RegexUtil.isValidNIC(cnic)) {
                cnic3.setError(getResources().getString(R.string.ctb_invalid_cnic));
                error = true;
                view = cnicLayout;
            }
        }
        if(addressType.getVisibility()==View.VISIBLE && App.get(addressType).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            addressType.getQuestionView().setError(getString(R.string.empty_field));
            error = true;
            view = addressType;
        }
        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            DrawableCompat.setTint(clearIcon, color);
            alertDialog.setIcon(clearIcon);
            alertDialog.setTitle(getResources().getString(R.string.title_error));
            final View finalView = view;
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            scrollView.post(new Runnable() {
                                public void run() {
                                    if (finalView != null) {
                                        scrollView.scrollTo(0, finalView.getTop());
                                        cnicOwnerOther.clearFocus();
                                        mobileLinearLayout.clearFocus();
                                        landlineLayout.clearFocus();
                                        cnicLayout.clearFocus();
                                    }
                                }
                            });
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
        String ownerString = "";
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
        if (externalID.getVisibility() == View.VISIBLE)
               observations.add(new String[]{"CONTACT EXTERNAL ID", App.get(externalID)});


        if(cnicLayout.getVisibility()==View.VISIBLE) {
            String cnicNumber = App.get(cnic1) + "-" + App.get(cnic2) + "-" + App.get(cnic3);
            if (RegexUtil.isValidNIC(cnicNumber)) {
                observations.add(new String[]{"NATIONAL IDENTIFICATION NUMBER", cnicNumber});
            }
        }

        if (cnicOwner.getVisibility() == View.VISIBLE) {
            ownerString = App.get(cnicOwner).equals(getResources().getString(R.string.ctb_mother)) ? "MOTHER" :
                    (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_father)) ? "FATHER" :
                            (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_brother)) ? "BROTHER" :
                                    (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_sister)) ? "SISTER" :
                                            (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_uncle)) ? "UNCLE" :
                                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_aunt)) ? "AUNT" : "OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER")))))))));

            observations.add(new String[]{"COMPUTERIZED NATIONAL IDENTIFICATION OWNER", ownerString});
        }
        if(cnicOwnerOther.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER", App.get(cnicOwnerOther)});
        }
        observations.add(new String[]{"PATIENT PROVIDED ADDRESS", App.get(addressProvided).toUpperCase()});
        if(address1.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"ADDRESS (TEXT)", App.get(address1)});
        }

        if(province.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"PROVINCE", App.get(province)});
        }

        if(address2.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"EXTENDED ADDRESS (TEXT)", App.get(address2)});
        }

        if(city.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"VILLAGE", App.get(city)});
        }
        if(district.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"DISTRICT", App.get(district)});
        }
        if(addressType.getVisibility()==View.VISIBLE) {
            if (App.get(addressType).equals(getResources().getString(R.string.ctb_permanent))) {
                observations.add(new String[]{"TYPE OF ADDRESS", "PERMANENT ADDRESS"});
            }
            if (App.get(addressType).equals(getResources().getString(R.string.ctb_temporary))) {
                observations.add(new String[]{"TYPE OF ADDRESS", "TEMPORARY ADDRESS"});
            }
        }

        if(!App.get(nearestLandmark).isEmpty()){
            observations.add(new String[]{"NEAREST LANDMARK", App.get(nearestLandmark)});
        }
        String primaryMobile = mobileNumber1.getText().toString() + "-" + mobileNumber2.getText().toString();
        observations.add(new String[]{"CONTACT PHONE NUMBER", primaryMobile});

        if(mobileNumberContact.getVisibility()==View.VISIBLE){
            primaryMobileNumberContactString=  App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_mother)) ? "MOTHER" :
                    (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_father)) ? "FATHER" :
                            (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_brother)) ? "BROTHER" :
                                    (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_sister)) ? "SISTER" :
                                            (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_paternal_grandfather)) ? "PATERNAL GRANDFATHER":
                                                    (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                            (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                                    (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                                                            (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_uncle)) ? "UNCLE":
                                                                                    (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_aunt)) ? "AUNT" : "OTHER FAMILY RELATION")))))))));
            observations.add(new String[]{"CONTACT OWNER",primaryMobileNumberContactString});
        }

        if(permissionMobileNumberContact.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"PERMISSION TO USE CONTACT NUMBER", App.get(permissionMobileNumberContact).toUpperCase()});
        }


        if(!App.get(landlineNumber1).isEmpty() && !App.get(landlineNumber2).isEmpty() ) {
            String landlineNumber = landlineNumber1.getText().toString() + "-" + landlineNumber2.getText().toString();
            observations.add(new String[]{"TERTIARY CONTACT NUMBER", landlineNumber});
        }

        if(landlineNumberContact.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"TERTIARY CONTACT OWNER", App.get(landlineNumberContact).equals(getResources().getString(R.string.ctb_mother)) ? "MOTHER" :
                    (App.get(landlineNumberContact).equals(getResources().getString(R.string.ctb_father)) ? "FATHER" :
                            (App.get(landlineNumberContact).equals(getResources().getString(R.string.ctb_brother)) ? "BROTHER" :
                                    (App.get(landlineNumberContact).equals(getResources().getString(R.string.ctb_sister)) ? "SISTER" :
                                            (App.get(landlineNumberContact).equals(getResources().getString(R.string.ctb_paternal_grandfather)) ? "PATERNAL GRANDFATHER":
                                                    (App.get(landlineNumberContact).equals(getResources().getString(R.string.ctb_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                            (App.get(landlineNumberContact).equals(getResources().getString(R.string.ctb_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                                    (App.get(landlineNumberContact).equals(getResources().getString(R.string.ctb_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                                                            (App.get(landlineNumberContact).equals(getResources().getString(R.string.ctb_uncle)) ? "UNCLE":
                                                                                    (App.get(landlineNumberContact).equals(getResources().getString(R.string.ctb_aunt)) ? "AUNT" : "OTHER FAMILY RELATION")))))))))});
        }

        if(!App.get(secondaryMobileNumber1).isEmpty() && !App.get(secondaryMobileNumber2).isEmpty() ){
            String secondaryMobileNumber = secondaryMobileNumber1.getText().toString() + "-" + secondaryMobileNumber2.getText().toString();
            observations.add(new String[]{"SECONDARY MOBILE NUMBER", secondaryMobileNumber});

        }

        if(secondaryMobileNumberContact.getVisibility()==View.VISIBLE){
            secondaryMobileNumberContactString =  App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_mother)) ? "MOTHER" :
                    (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_father)) ? "FATHER" :
                            (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_brother)) ? "BROTHER" :
                                    (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_sister)) ? "SISTER" :
                                            (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_paternal_grandfather)) ? "PATERNAL GRANDFATHER":
                                                    (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                            (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                                    (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                                                            (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_uncle)) ? "UNCLE":
                                                                                    (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_aunt)) ? "AUNT" : "OTHER FAMILY RELATION")))))))));

            observations.add(new String[]{"SECONDARY CONTACT OWNER",secondaryMobileNumberContactString});
        }



        if(!App.get(secondaryLandlineNumber1).isEmpty() && !App.get(secondaryLandlineNumber2).isEmpty() ){
            String secondaryLandlineNumber = secondaryLandlineNumber1.getText().toString() + "-" + secondaryLandlineNumber2.getText().toString();
            observations.add(new String[]{"QUATERNARY CONTACT NUMBER", secondaryLandlineNumber});

        }

        if(secondaryLandlineContact.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"QUATERNARY CONTACT OWNER", App.get(secondaryLandlineContact).equals(getResources().getString(R.string.ctb_mother)) ? "MOTHER" :
                    (App.get(secondaryLandlineContact).equals(getResources().getString(R.string.ctb_father)) ? "FATHER" :
                            (App.get(secondaryLandlineContact).equals(getResources().getString(R.string.ctb_brother)) ? "BROTHER" :
                                    (App.get(secondaryLandlineContact).equals(getResources().getString(R.string.ctb_sister)) ? "SISTER" :
                                            (App.get(secondaryLandlineContact).equals(getResources().getString(R.string.ctb_paternal_grandfather)) ? "PATERNAL GRANDFATHER":
                                                    (App.get(secondaryLandlineContact).equals(getResources().getString(R.string.ctb_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                            (App.get(secondaryLandlineContact).equals(getResources().getString(R.string.ctb_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                                    (App.get(secondaryLandlineContact).equals(getResources().getString(R.string.ctb_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                                                            (App.get(secondaryLandlineContact).equals(getResources().getString(R.string.ctb_uncle)) ? "UNCLE":
                                                                                    (App.get(secondaryLandlineContact).equals(getResources().getString(R.string.ctb_aunt)) ? "AUNT" : "OTHER FAMILY RELATION")))))))))});
        }

        final String finalOwnerString = ownerString;
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

                String result = serverService.saveEncounterAndObservation(App.getProgram()+"-Patient Registration", FORM, formDateCalendar, observations.toArray(new String[][]{}),false);
                if (!result.contains("SUCCESS"))
                    return result;
                else {

                    String encounterId = "";

                    if (result.contains("_")) {
                        String[] successArray = result.split("_");
                        encounterId = successArray[1];
                    }

                    result = serverService.saveProgramEnrollement(App.getSqlDate(formDateCalendar), encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    if (!App.get(externalID).isEmpty() && App.hasKeyListener(externalID)) {
                        if(App.getPatient().getExternalId() != null) {
                            if(!App.getPatient().getExternalId().equals("")) {
                                if (!App.getPatient().getExternalId().equalsIgnoreCase(App.get(externalID))) {
                                    result = serverService.saveIdentifier("External ID", App.get(externalID), encounterId);
                                    if (!result.equals("SUCCESS"))
                                        return result;
                                }
                            }
                            else {
                                result = serverService.saveIdentifier("External ID", App.get(externalID), encounterId);
                                if (!result.equals("SUCCESS"))
                                    return result;
                            }
                        } else {
                            result = serverService.saveIdentifier("External ID", App.get(externalID), encounterId);
                            if (!result.equals("SUCCESS"))
                                return result;
                        }
                    }

                    String finalCnic = App.get(cnic1)+"-"+App.get(cnic2)+"-"+App.get(cnic3);
                    if(!finalCnic.equals("")) {
                        result = serverService.savePersonAttributeType("National ID", finalCnic, encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }

                    if(!finalOwnerString.equals("")) {
                        String[][] cnicOwnerConcept = serverService.getConceptUuidAndDataType(finalOwnerString);
                        result = serverService.savePersonAttributeType("National ID Owner", cnicOwnerConcept[0][0], encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }

                    if (!(App.get(address1).equals("") && App.get(district).equals("") && App.get(nearestLandmark).equals(""))) {
                        result = serverService.savePersonAddress(App.get(address1),App.get(address2),App.get(city), App.get(district), App.get(province), App.getCountry(), App.getLongitude(), App.getLatitude(), App.get(nearestLandmark), encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }

                    result = serverService.savePersonAttributeType("Primary Contact", App.get(mobileNumber1)+ "-" + App.get(mobileNumber2), encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    String[][] concept = serverService.getConceptUuidAndDataType(primaryMobileNumberContactString);
                    result = serverService.savePersonAttributeType("Primary Contact Owner", concept[0][0], encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;



                    if (!(App.get(secondaryMobileNumber1).isEmpty() && App.get(secondaryMobileNumber2).isEmpty())) {
                        result = serverService.savePersonAttributeType("Secondary Contact",  App.get(secondaryMobileNumber1)+ "-" + App.get(secondaryMobileNumber2), encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }
                    if(!(App.get(secondaryMobileNumber1).isEmpty() && App.get(secondaryMobileNumber2).isEmpty())) {
                        String[][] concept2 = serverService.getConceptUuidAndDataType(secondaryMobileNumberContactString);

                        result = serverService.savePersonAttributeType("Secondary Contact Owner",concept2[0][0] , encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }



                    result = serverService.savePersonAttributeType("Tertiary Contact", App.get(landlineNumber1)+ "-" + App.get(landlineNumber2), encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;


                    if (!(App.get(secondaryLandlineNumber1).isEmpty() && App.get(secondaryLandlineNumber2).isEmpty())) {
                        result = serverService.savePersonAttributeType("Quaternary Contact", App.get(secondaryLandlineNumber1)+ "-" + App.get(secondaryLandlineNumber2), encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }
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

                    serverService.addTown(address2.getText().toString());

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

        serverService.saveFormLocally(FORM_NAME, FORM, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int formId) {
        OfflineForm fo = serverService.getOfflineFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            }
            else if (obs[0][0].equals("CONTACT EXTERNAL ID")) {
                externalID.getEditText().setText(obs[0][1]);
            }
            else if (obs[0][0].equals("NATIONAL IDENTIFICATION NUMBER")) {
                if(!obs[0][1].equals("--")) {
                    String[] cnicParts = obs[0][1].toString().split("-");
                    cnic1.setText(cnicParts[0]);
                    cnic2.setText(cnicParts[1]);
                    cnic3.setText(cnicParts[2]);
                }
            } else if (obs[0][0].equals("COMPUTERIZED NATIONAL IDENTIFICATION OWNER")) {
                String value = obs[0][1].equals("MOTHER") ? getResources().getString(R.string.ctb_mother) :
                        (obs[0][1].equals("FATHER") ? getResources().getString(R.string.ctb_father) :
                                (obs[0][1].equals("BROTHER") ? getResources().getString(R.string.ctb_brother) :
                                        (obs[0][1].equals("SISTER") ? getResources().getString(R.string.ctb_sister) :
                                                (obs[0][1].equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.ctb_paternal_grandfather) :
                                                        (obs[0][1].equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.ctb_paternal_grandmother) :
                                                                (obs[0][1].equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.ctb_maternal_grandfather) :
                                                                        (obs[0][1].equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.ctb_maternal_grandmother) :
                                                                                (obs[0][1].equals("UNCLE") ? getResources().getString(R.string.ctb_uncle) :
                                                                                        (obs[0][1].equals("AUNT") ? getResources().getString(R.string.ctb_aunt) :
                                                                                                getResources().getString(R.string.ctb_other_title))))))))));
                cnicOwner.getSpinner().selectValue(value);
                if(value.equals(getResources().getString(R.string.ctb_other_title))){
                    cnicOwnerOther.setVisibility(View.VISIBLE);
                }
            } else if (obs[0][0].equals("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER")) {
                cnicOwnerOther.getEditText().setText(obs[0][1]);
            }else if (obs[0][0].equals("PATIENT PROVIDED ADDRESS")) {
                for (RadioButton rb : addressProvided.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        address1.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("ADDRESS (TEXT)")) {
                address1.getEditText().setText(obs[0][1]);
            }
            else if (obs[0][0].equals("PROVINCE")) {
                province.getSpinner().selectValue(obs[0][1]);
            }
            else if (obs[0][0].equals("EXTENDED ADDRESS (TEXT)")) {
                address2.setText(obs[0][1]);
            }


            else if (obs[0][0].equals("PROVINCE")) {
                province.getSpinner().selectValue(obs[0][1]);
            } else if (obs[0][0].equals("DISTRICT")) {
                String[] districts = serverService.getDistrictList(App.get(province));
                district.getSpinner().setSpinnerData(districts);
                district.getSpinner().selectValue(obs[0][1]);
                district.getSpinner().setTag("selected");
            } else if (obs[0][0].equals("VILLAGE")) {
                String[] cities = serverService.getCityList(App.get(district));
                city.getSpinner().setSpinnerData(cities);
                city.getSpinner().selectValue(obs[0][1]);
                city.getSpinner().setTag("selected");
            } else if (obs[0][0].equals("TYPE OF ADDRESS")) {
                for (RadioButton rb : addressType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_permanent)) && obs[0][1].equals("PERMANENT ADDRESS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_temporary)) && obs[0][1].equals("TEMPORARY ADDRESS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("NEAREST LANDMARK")) {
                nearestLandmark.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("CONTACT PHONE NUMBER")) {
                    String[] phoneNumberParts = obs[0][1].split("-");
                    mobileNumber1.setText(phoneNumberParts[0]);
                    mobileNumber2.setText(phoneNumberParts[1]);
            } else if (obs[0][0].equals("CONTACT OWNER")) {
                String value = obs[0][1].equals("MOTHER") ? getResources().getString(R.string.ctb_mother) :
                        (obs[0][1].equals("FATHER") ? getResources().getString(R.string.ctb_father) :
                                (obs[0][1].equals("BROTHER") ? getResources().getString(R.string.ctb_brother) :
                                        (obs[0][1].equals("SISTER") ? getResources().getString(R.string.ctb_sister) :
                                                (obs[0][1].equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.ctb_paternal_grandfather) :
                                                        (obs[0][1].equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.ctb_paternal_grandmother) :
                                                                (obs[0][1].equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.ctb_maternal_grandfather) :
                                                                        (obs[0][1].equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.ctb_maternal_grandmother) :
                                                                                (obs[0][1].equals("UNCLE") ? getResources().getString(R.string.ctb_uncle) :
                                                                                        (obs[0][1].equals("AUNT") ? getResources().getString(R.string.ctb_aunt) :
                                                                                                getResources().getString(R.string.ctb_other_title))))))))));
                mobileNumberContact.getSpinner().selectValue(value);
            }else if (obs[0][0].equals("PERMISSION TO USE CONTACT NUMBER")) {
                for (RadioButton rb : permissionMobileNumberContact.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("TERTIARY CONTACT NUMBER")) {
                if(!obs[0][1].equals("-")) {
                    String[] phoneNumberParts = obs[0][1].split("-");
                    landlineNumber1.setText(phoneNumberParts[0]);
                    landlineNumber2.setText(phoneNumberParts[1]);
                }
            } else if (obs[0][0].equals("TERTIARY CONTACT OWNER")) {
                String value = obs[0][1].equals("MOTHER") ? getResources().getString(R.string.ctb_mother) :
                        (obs[0][1].equals("FATHER") ? getResources().getString(R.string.ctb_father) :
                                (obs[0][1].equals("BROTHER") ? getResources().getString(R.string.ctb_brother) :
                                        (obs[0][1].equals("SISTER") ? getResources().getString(R.string.ctb_sister) :
                                                (obs[0][1].equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.ctb_paternal_grandfather) :
                                                        (obs[0][1].equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.ctb_paternal_grandmother) :
                                                                (obs[0][1].equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.ctb_maternal_grandfather) :
                                                                        (obs[0][1].equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.ctb_maternal_grandmother) :
                                                                                (obs[0][1].equals("UNCLE") ? getResources().getString(R.string.ctb_uncle) :
                                                                                        (obs[0][1].equals("AUNT") ? getResources().getString(R.string.ctb_aunt) :
                                                                                                getResources().getString(R.string.ctb_other_title))))))))));
                landlineNumberContact.getSpinner().selectValue(value);
            }else if (obs[0][0].equals("SECONDARY MOBILE NUMBER")) {
                String[] phoneNumberParts = obs[0][1].split("-");
                secondaryMobileNumber1.setText(phoneNumberParts[0]);
                secondaryMobileNumber2.setText(phoneNumberParts[1]);
            } else if (obs[0][0].equals("SECONDARY CONTACT OWNER")) {
                String value = obs[0][1].equals("MOTHER") ? getResources().getString(R.string.ctb_mother) :
                        (obs[0][1].equals("FATHER") ? getResources().getString(R.string.ctb_father) :
                                (obs[0][1].equals("BROTHER") ? getResources().getString(R.string.ctb_brother) :
                                        (obs[0][1].equals("SISTER") ? getResources().getString(R.string.ctb_sister) :
                                                (obs[0][1].equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.ctb_paternal_grandfather) :
                                                        (obs[0][1].equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.ctb_paternal_grandmother) :
                                                                (obs[0][1].equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.ctb_maternal_grandfather) :
                                                                        (obs[0][1].equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.ctb_maternal_grandmother) :
                                                                                (obs[0][1].equals("UNCLE") ? getResources().getString(R.string.ctb_uncle) :
                                                                                        (obs[0][1].equals("AUNT") ? getResources().getString(R.string.ctb_aunt) :
                                                                                                getResources().getString(R.string.ctb_other_title))))))))));
                secondaryMobileNumberContact.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("QUATERNARY CONTACT NUMBER")) {
                String[] phoneNumberParts = obs[0][1].split("-");
                secondaryLandlineNumber1.setText(phoneNumberParts[0]);
                secondaryLandlineNumber2.setText(phoneNumberParts[1]);
            } else if (obs[0][0].equals("QUATERNARY CONTACT OWNER")) {
                String value = obs[0][1].equals("MOTHER") ? getResources().getString(R.string.ctb_mother) :
                        (obs[0][1].equals("FATHER") ? getResources().getString(R.string.ctb_father) :
                                (obs[0][1].equals("BROTHER") ? getResources().getString(R.string.ctb_brother) :
                                        (obs[0][1].equals("SISTER") ? getResources().getString(R.string.ctb_sister) :
                                                (obs[0][1].equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.ctb_paternal_grandfather) :
                                                        (obs[0][1].equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.ctb_paternal_grandmother) :
                                                                (obs[0][1].equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.ctb_maternal_grandfather) :
                                                                        (obs[0][1].equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.ctb_maternal_grandmother) :
                                                                                (obs[0][1].equals("UNCLE") ? getResources().getString(R.string.ctb_uncle) :
                                                                                        (obs[0][1].equals("AUNT") ? getResources().getString(R.string.ctb_aunt) :
                                                                                                getResources().getString(R.string.ctb_other_title))))))))));
                secondaryLandlineContact.getSpinner().selectValue(value);
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
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == cnicOwner.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                cnicOwnerOther.setVisibility(View.VISIBLE);
            }
            else{
                cnicOwnerOther.setVisibility(View.GONE);
            }
        }
        if (spinner == district.getSpinner()) {

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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


    }

    @Override
    public void resetViews() {

        Object[][]  towns = serverService.getAllTowns();
        String[] townList = new String[towns.length];

        for (int i = 0; i < towns.length; i++) {
            townList[i] = String.valueOf(towns[i][1]);
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, townList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        address2.setAdapter(spinnerArrayAdapter);

        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        cnicOwner.setVisibility(View.GONE);
        cnicOwnerOther.setVisibility(View.GONE);
        address1.setVisibility(View.GONE);
        addressLayout.setVisibility(View.GONE);
        townTextView.setVisibility(View.GONE);
        addressType.setVisibility(View.GONE);
        mobileNumberContact.setVisibility(View.GONE);
        permissionMobileNumberContact.setVisibility(View.GONE);
        secondaryMobileNumberContact.setVisibility(View.GONE);
        landlineNumberContact.setVisibility(View.GONE);
        secondaryLandlineContact.setVisibility(View.GONE);
        cnic1.getText().clear();
        cnic2.getText().clear();
        cnic3.getText().clear();

        mobileNumber1.getText().clear();
        mobileNumber2.getText().clear();
        landlineNumber1.getText().clear();
        landlineNumber2.getText().clear();
        secondaryMobileNumber1.getText().clear();
        secondaryMobileNumber2.getText().clear();
        secondaryLandlineNumber1.getText().clear();
        secondaryLandlineNumber2.getText().clear();
        cnic3.setError(null);
        mobileNumber2.setError(null);
        landlineNumber2.setError(null);
        secondaryMobileNumber2.setError(null);
        secondaryLandlineNumber2.setError(null);

        String[] districts = serverService.getDistrictList(App.getProvince());
        district.getSpinner().setSpinnerData(districts);

        String[] cities = serverService.getCityList(App.get(district));
        city.getSpinner().setSpinnerData(cities);

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

        if (App.get(externalID).equals("")) {
            String externalId = App.getPatient().getExternalId();
            if (externalId != null) {
                if (externalId.equals("")) {
                    externalID.getEditText().setText("");
                } else {
                    externalID.getEditText().setText(externalId);
                    //indexExternalPatientId.getEditText().setKeyListener(null);
                }
            } else {
                externalID.getEditText().setText("");
            }
        }


    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == addressProvided.getRadioGroup()) {
            if(addressProvided.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))){
                address1.setVisibility(View.VISIBLE);
                addressLayout.setVisibility(View.VISIBLE);
                addressQuestion.setVisibility(View.VISIBLE);
                    address2.setVisibility(View.VISIBLE);
                townTextView.setVisibility(View.VISIBLE);
                addressType.setVisibility(View.VISIBLE);
                district.setVisibility(View.VISIBLE);
                city.setVisibility(View.VISIBLE);
            }
            else if(addressProvided.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.no))){
                address1.setVisibility(View.GONE);
                address2.setVisibility(View.GONE);
                addressQuestion.setVisibility(View.GONE);
                address2.setVisibility(View.GONE);
                townTextView.setVisibility(View.GONE);
                district.setVisibility(View.VISIBLE);
                addressType.setVisibility(View.GONE);
                city.setVisibility(View.VISIBLE);
            }
        }
        if (group == addressType.getRadioGroup()) {
            addressType.getRadioGroup().getButtons().get(1).setError(null);
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
