package com.ihsinformatics.gfatmmobile.childhoodTb;

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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
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
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbPatientRegistration extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledEditText cnic1;
    TitledEditText cnic2;
    TitledEditText cnic3;
    TitledSpinner cnicOwner;
    TitledEditText cnicOwnerOther;
    TitledRadioGroup addressProvided;
    TitledEditText address1;
    TitledEditText address2;
    TitledSpinner city;
    TitledSpinner district;
    TitledRadioGroup addressType;
    TitledEditText nearestLandmark;
    TitledSpinner mobileNumberContact;
    TitledRadioGroup permissionMobileNumberContact;
    TitledSpinner secondaryMobileNumberContact;
    TitledRadioGroup permissionSecondaryMobileNumber;
    TitledSpinner landlineNumberContact;
    TitledRadioGroup permissionLandlineNumber;
    TitledSpinner secondaryLandlineContact;
    TitledRadioGroup permissionSecondaryLandlineNumber;
    LinearLayout cnicLinearLayout;
    LinearLayout mobileLinearLayout;
    TitledEditText mobileNumber1;
    TitledEditText mobileNumber2;
    LinearLayout secondaryMobileLinearLayout;
    TitledEditText secondaryMobileNumber1;
    TitledEditText secondaryMobileNumber2;
    LinearLayout landlineLayout;
    TitledEditText landlineNumber1;
    TitledEditText landlineNumber2;
    LinearLayout secondaryLandlineNumber;
    TitledEditText secondaryLandlineNumber1;
    TitledEditText secondaryLandlineNumber2;



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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        cnicLinearLayout = new LinearLayout(context);
        cnicLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        cnic1 = new TitledEditText(context, null, getResources().getString(R.string.fast_nic_number), "", "#####", 5, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        cnicLinearLayout.addView(cnic1);
        cnic2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLinearLayout.addView(cnic2);
        cnic3 = new TitledEditText(context, null, "-", "", "#", 1, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLinearLayout.addView(cnic3);
        cnicOwner = new TitledSpinner(context,null,getResources().getString(R.string.ctb_cnic_owner),getResources().getStringArray(R.array.ctb_close_contact_type_list),getResources().getString(R.string.ctb_mother),App.VERTICAL);
        cnicOwnerOther = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_specify),"","",20,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);

        addressProvided = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_address_provided),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);
        address1 = new TitledEditText(context,null,getResources().getString(R.string.ctb_address1),"","",10,null,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);

        address2 = new TitledEditText(context,null,getResources().getString(R.string.ctb_address2),"","",50,null,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        district = new TitledSpinner(context, "", getResources().getString(R.string.pet_district), getResources().getStringArray(R.array.pet_empty_array), "", App.VERTICAL);
        city = new TitledSpinner(context, "", getResources().getString(R.string.pet_city), getResources().getStringArray(R.array.pet_empty_array), "", App.VERTICAL);
        addressType = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_address_type),getResources().getStringArray(R.array.ctb_address_type_list),null,App.HORIZONTAL,App.VERTICAL,true);
        nearestLandmark = new TitledEditText(context,null,getResources().getString(R.string.ctb_nearest_landmark),"","",50,null,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        mobileLinearLayout = new LinearLayout(context);
        mobileLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mobileNumber1 = new TitledEditText(context, null, getResources().getString(R.string.mobile_number), "", "####", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        mobileLinearLayout.addView(mobileNumber1);
        mobileNumber2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        mobileLinearLayout.addView(mobileNumber2);
        mobileNumberContact = new TitledSpinner(context,null,getResources().getString(R.string.ctb_whose_contact),getResources().getStringArray(R.array.ctb_close_contact_type_list),null,App.VERTICAL);
        permissionMobileNumberContact = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_permission),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);
        secondaryMobileLinearLayout= new LinearLayout(context);
        secondaryMobileLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        secondaryMobileNumber1 = new TitledEditText(context, null, getResources().getString(R.string.ctb_secondary_mobile_number), "", "####", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        secondaryMobileLinearLayout.addView(secondaryMobileNumber1);
        secondaryMobileNumber2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        secondaryMobileLinearLayout.addView(secondaryMobileNumber2);
        secondaryMobileNumberContact = new TitledSpinner(context,null,getResources().getString(R.string.ctb_whose_contact),getResources().getStringArray(R.array.ctb_close_contact_type_list),null,App.VERTICAL);
        permissionSecondaryMobileNumber = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_permission),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);
        landlineLayout = new LinearLayout(context);
        landlineLayout.setOrientation(LinearLayout.HORIZONTAL);
        landlineNumber1 = new TitledEditText(context, null, getResources().getString(R.string.ctb_landline_number), "", "###", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        landlineLayout.addView(landlineNumber1);
        landlineNumber2 = new TitledEditText(context, null, "-", "", "########", 8, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        landlineLayout.addView(landlineNumber2);
        landlineNumberContact = new TitledSpinner(context,null,getResources().getString(R.string.ctb_whose_contact),getResources().getStringArray(R.array.ctb_close_contact_type_list),null,App.VERTICAL);
        permissionLandlineNumber = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_permission),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);
        secondaryLandlineNumber = new LinearLayout(context);
        secondaryLandlineNumber.setOrientation(LinearLayout.HORIZONTAL);
        secondaryLandlineNumber1 = new TitledEditText(context, null, getResources().getString(R.string.ctb_secondary_landline_number), "", "###", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        secondaryLandlineNumber.addView(secondaryLandlineNumber1);
        secondaryLandlineNumber2 = new TitledEditText(context, null, "-", "", "########", 8, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        secondaryLandlineNumber.addView(secondaryLandlineNumber2);
        secondaryLandlineContact = new TitledSpinner(context,null,getResources().getString(R.string.ctb_whose_contact),getResources().getStringArray(R.array.ctb_close_contact_type_list),null,App.VERTICAL);
        permissionSecondaryLandlineNumber = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_permission),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);


        mobileNumber2.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mobileNumber = mobileNumber1.getEditText().getText() + s.toString();
                if(RegexUtil.isMobileNumber(mobileNumber)){
                    mobileNumber2.getEditText().setError(null);
                    mobileNumberContact.setVisibility(View.VISIBLE);
                    permissionMobileNumberContact.setVisibility(View.VISIBLE);
                }
                else{
                    mobileNumber2.getEditText().setError("Invalid Mobile Number");
                    mobileNumberContact.setVisibility(View.GONE);
                    permissionMobileNumberContact.setVisibility(View.GONE);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobileNumber1.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mobileNumber = s.toString() + mobileNumber2.getEditText().getText().toString();
                if(RegexUtil.isMobileNumber(mobileNumber)){
                        mobileNumber2.getEditText().setError(null);
                        mobileNumberContact.setVisibility(View.VISIBLE);
                        permissionMobileNumberContact.setVisibility(View.VISIBLE);
                }
                else{
                    mobileNumber2.getEditText().setError("Invalid Mobile Number");
                    mobileNumberContact.setVisibility(View.GONE);
                    permissionMobileNumberContact.setVisibility(View.GONE);
                }
                if(s.length()==0 && mobileNumber2.getEditText().getText().length()==0){
                    mobileNumber2.getEditText().setError(null);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        secondaryMobileNumber2.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mobileNumber = secondaryMobileNumber1.getEditText().getText() + s.toString();
                if(RegexUtil.isMobileNumber(mobileNumber)){
                    secondaryMobileNumber2.getEditText().setError(null);
                    secondaryMobileNumberContact.setVisibility(View.VISIBLE);
                    permissionSecondaryMobileNumber.setVisibility(View.VISIBLE);

                }
                else{
                    secondaryMobileNumber2.getEditText().setError("Invalid Phone number");
                    secondaryMobileNumberContact.setVisibility(View.GONE);
                    permissionSecondaryMobileNumber.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        secondaryMobileNumber1.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String secondaryMobileNumber = s.toString() + secondaryMobileNumber2.getEditText().getText().toString();
                if(RegexUtil.isMobileNumber(secondaryMobileNumber)){
                    secondaryMobileNumberContact.setVisibility(View.VISIBLE);
                    permissionSecondaryMobileNumber.setVisibility(View.VISIBLE);
                    secondaryMobileNumber2.getEditText().setError(null);
                }
                else{
                    secondaryMobileNumber2.getEditText().setError("Invalid Phone number");
                    secondaryMobileNumberContact.setVisibility(View.GONE);
                    permissionSecondaryMobileNumber.setVisibility(View.GONE);
                }
                if(s.length()==0 && secondaryMobileNumber2.getEditText().getText().length()==0){
                    secondaryMobileNumber2.getEditText().setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        landlineNumber2.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String landlineNumber = landlineNumber1.getEditText().getText() + s.toString();
                if(RegexUtil.isLandlineNumber(landlineNumber)){
                    landlineNumberContact.setVisibility(View.VISIBLE);
                    permissionLandlineNumber.setVisibility(View.VISIBLE);
                    landlineNumber2.getEditText().setError(null);
                }
                else{
                    landlineNumber2.getEditText().setError("Invalid Landline number");
                    landlineNumberContact.setVisibility(View.GONE);
                    permissionLandlineNumber.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        landlineNumber1.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String landlineNumber = s.toString() + landlineNumber2.getEditText().getText().toString();
                if(RegexUtil.isLandlineNumber(landlineNumber)){
                    landlineNumberContact.setVisibility(View.VISIBLE);
                    permissionLandlineNumber.setVisibility(View.VISIBLE);
                    landlineNumber2.getEditText().setError(null);
                }
                else{
                    landlineNumber2.getEditText().setError("Invalid Landline number");
                    landlineNumberContact.setVisibility(View.GONE);
                    permissionLandlineNumber.setVisibility(View.GONE);
                }
                if(s.length()==0 && landlineNumber2.getEditText().getText().length()==0){
                    landlineNumber2.getEditText().setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        secondaryLandlineNumber2.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String landlineNumber = secondaryLandlineNumber1.getEditText().getText() + s.toString();
                if(RegexUtil.isLandlineNumber(landlineNumber)){
                    secondaryLandlineNumber2.getEditText().setError(null);
                    secondaryLandlineContact.setVisibility(View.VISIBLE);
                    permissionSecondaryLandlineNumber.setVisibility(View.VISIBLE);

                }
                else{
                    secondaryLandlineNumber2.getEditText().setError("Invalid Landline number");
                    secondaryLandlineContact.setVisibility(View.GONE);
                    permissionSecondaryLandlineNumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        secondaryLandlineNumber1.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String landlineNumber = secondaryLandlineNumber2.getEditText().getText() + s.toString();
                if(RegexUtil.isLandlineNumber(landlineNumber)){
                    secondaryLandlineNumber2.getEditText().setError(null);
                    secondaryLandlineContact.setVisibility(View.VISIBLE);
                    permissionSecondaryLandlineNumber.setVisibility(View.VISIBLE);


                }
                else{
                    secondaryLandlineNumber2.getEditText().setError("Invalid Landline number");
                    secondaryLandlineContact.setVisibility(View.GONE);
                    permissionSecondaryLandlineNumber.setVisibility(View.GONE);
                }

                if(s.length()==0 && secondaryLandlineNumber2.getEditText().getText().length()==0){
                    secondaryLandlineNumber2.getEditText().setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        views = new View[]{formDate.getButton(),cnicLinearLayout,cnicOwner.getSpinner(),cnicOwnerOther.getEditText(),addressProvided.getRadioGroup(),address1.getEditText(),address2.getEditText(),city.getSpinner(),district.getSpinner(),addressType.getRadioGroup(),nearestLandmark.getEditText(),mobileLinearLayout,mobileNumberContact.getSpinner(),permissionMobileNumberContact.getRadioGroup(),secondaryMobileLinearLayout,secondaryMobileNumberContact.getSpinner(),permissionSecondaryMobileNumber.getRadioGroup(),landlineLayout,landlineNumberContact.getSpinner(),permissionLandlineNumber.getRadioGroup(),secondaryLandlineNumber,secondaryLandlineContact.getSpinner(),permissionSecondaryLandlineNumber.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate,cnicLinearLayout,cnicOwner,cnicOwnerOther,addressProvided,address1,address2,city,district,addressType,nearestLandmark,mobileLinearLayout,mobileNumberContact,permissionMobileNumberContact,secondaryMobileLinearLayout,secondaryMobileNumberContact,permissionSecondaryMobileNumber,landlineLayout,landlineNumberContact,permissionLandlineNumber,secondaryLandlineNumber,secondaryLandlineContact,permissionSecondaryLandlineNumber}};

        formDate.getButton().setOnClickListener(this);
        cnicOwner.getSpinner().setOnItemSelectedListener(this);
        addressProvided.getRadioGroup().setOnCheckedChangeListener(this);
        city.getSpinner().setOnItemSelectedListener(this);
        addressType.getRadioGroup().setOnCheckedChangeListener(this);
        mobileNumberContact.getSpinner().setOnItemSelectedListener(this);
        permissionMobileNumberContact.getRadioGroup().setOnCheckedChangeListener(this);
        secondaryMobileNumberContact.getSpinner().setOnItemSelectedListener(this);
        permissionSecondaryMobileNumber.getRadioGroup().setOnCheckedChangeListener(this);
        landlineNumberContact.getSpinner().setOnItemSelectedListener(this);
        permissionLandlineNumber.getRadioGroup().setOnCheckedChangeListener(this);
        secondaryLandlineContact.getSpinner().setOnItemSelectedListener(this);
        permissionSecondaryLandlineNumber.getRadioGroup().setOnCheckedChangeListener(this);
        district.getSpinner().setOnItemSelectedListener(this);
        resetViews();

    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }


    }

    @Override
    public boolean validate() {
        boolean error=false;
        View view = null;
        Boolean flag = false;
        if (cnicOwnerOther.getVisibility() == View.VISIBLE && App.get(cnicOwnerOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnicOwnerOther.getEditText().setError(getString(R.string.empty_field));
            cnicOwnerOther.getEditText().requestFocus();
            error = true;
            view = null;
        }
        if(!App.get(secondaryLandlineNumber1).isEmpty() || !App.get(secondaryLandlineNumber2).isEmpty()){
            String secondaryLandline = secondaryLandlineNumber1.getEditText().getText().toString() + secondaryLandlineNumber2.getEditText().getText().toString();
            if (!RegexUtil.isLandlineNumber(secondaryLandline)) {
                secondaryLandlineNumber2.getEditText().setError(getResources().getString(R.string.ctb_invalid_number));
                error = true;
                view = secondaryLandlineNumber;
            }
        }
        if (App.get(landlineNumber1).isEmpty() && App.get(landlineNumber2).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            landlineNumber2.getEditText().setError(getString(R.string.empty_field));
            error = true;
            view = landlineLayout;
        }
        else{
            String landline = landlineNumber1.getEditText().getText().toString() + landlineNumber2.getEditText().getText().toString();
            if (!RegexUtil.isLandlineNumber(landline)) {
                landlineNumber2.getEditText().setError(getResources().getString(R.string.ctb_invalid_number));
                error = true;
                view = landlineLayout;
            }
        }

        if(!App.get(secondaryMobileNumber2).isEmpty() || !App.get(secondaryMobileNumber1).isEmpty()){
            String secondaryMobileNumber = secondaryMobileNumber1.getEditText().getText().toString() + secondaryMobileNumber2.getEditText().getText().toString();
            if (!RegexUtil.isMobileNumber(secondaryMobileNumber)) {
                secondaryMobileNumber2.getEditText().setError(getResources().getString(R.string.ctb_invalid_number));
                error = true;
                view = secondaryMobileLinearLayout;
            }
        }
        if (App.get(mobileNumber1).isEmpty() && App.get(mobileNumber2).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobileNumber2.getEditText().setError(getString(R.string.empty_field));
            error = true;
            view = mobileLinearLayout;
        }
        else{
            String mobileNumber = mobileNumber1.getEditText().getText().toString() + mobileNumber2.getEditText().getText().toString();
            if (!RegexUtil.isMobileNumber(mobileNumber)) {
                mobileNumber2.getEditText().setError(getResources().getString(R.string.ctb_invalid_number));
                error = true;
                view = mobileLinearLayout;
            }
        }
        if(App.get(cnic1).isEmpty() && App.get(cnic2).isEmpty() && App.get(cnic3).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic3.getEditText().setError(getString(R.string.empty_field));
            error = true;
            view = cnicLinearLayout;
        }
        else {
            String cnic = cnic1.getEditText().getText().toString() + "-" + cnic2.getEditText().getText().toString() + "-" + cnic3.getEditText().getText().toString();
            if (!RegexUtil.isValidNIC(cnic)) {
                cnic3.getEditText().setError(getResources().getString(R.string.ctb_invalid_cnic));
                error = true;
                view = cnicLinearLayout;
            }
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
                                        cnicLinearLayout.clearFocus();
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
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
            }
            bundle.putBoolean("save", false);
        }

        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});
        String cnic = cnic1.getEditText().getText().toString() + "-" + cnic2.getEditText().getText().toString() + "-" + cnic3.getEditText().getText().toString();
        observations.add(new String[]{"NATIONAL IDENTIFICATION NUMBER", cnic});
        observations.add(new String[]{"COMPUTERIZED NATIONAL IDENTIFICATION OWNER", App.get(cnicOwner).equals(getResources().getString(R.string.ctb_mother)) ? "MOTHER" :
                (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_father)) ? "FATHER" :
                        (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_brother)) ? "BROTHER" :
                                (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_sister)) ? "SISTER" :
                                        (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_paternal_grandfather)) ? "PATERNAL GRANDFATHER":
                                                (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                        (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                                (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                                                        (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_uncle)) ? "UNCLE":
                                                                                (App.get(cnicOwner).equals(getResources().getString(R.string.ctb_aunt)) ? "AUNT" : "OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER")))))))))});

        if(cnicOwnerOther.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER", App.get(cnicOwnerOther)});
        }
        observations.add(new String[]{"PATIENT PROVIDED ADDRESS", App.get(addressProvided).toUpperCase()});
        if(address1.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"ADDRESS (TEXT)", App.get(address1)});
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

        if(App.get(addressType).equals(getResources().getString(R.string.ctb_permanent))){
            observations.add(new String[]{"TYPE OF ADDRESS", "PERMANENT ADDRESS"});
        }
        if(App.get(addressType).equals(getResources().getString(R.string.ctb_temporary))){
            observations.add(new String[]{"TYPE OF ADDRESS", "TEMPORARY ADDRESS"});
        }

        if(!App.get(nearestLandmark).isEmpty()){
            observations.add(new String[]{"NEAREST LANDMARK", App.get(nearestLandmark)});
        }
        String primaryMobile = mobileNumber1.getEditText().getText().toString() + "-" + mobileNumber2.getEditText().getText().toString();
        observations.add(new String[]{"CONTACT PHONE NUMBER", primaryMobile});

        if(mobileNumberContact.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"CONTACT OWNER", App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_mother)) ? "MOTHER" :
                    (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_father)) ? "FATHER" :
                            (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_brother)) ? "BROTHER" :
                                    (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_sister)) ? "SISTER" :
                                            (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_paternal_grandfather)) ? "PATERNAL GRANDFATHER":
                                                    (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                            (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                                    (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                                                            (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_uncle)) ? "UNCLE":
                                                                                    (App.get(mobileNumberContact).equals(getResources().getString(R.string.ctb_aunt)) ? "AUNT" : "OTHER FAMILY RELATION")))))))))});
        }

        if(permissionMobileNumberContact.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"PERMISSION TO USE CONTACT NUMBER", App.get(permissionMobileNumberContact).toUpperCase()});
        }

        String landlineNumber = landlineNumber1.getEditText().getText().toString() + "-" + landlineNumber2.getEditText().getText().toString();
        observations.add(new String[]{"TERTIARY CONTACT NUMBER", landlineNumber});

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

        if(permissionLandlineNumber.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"PERMISSION TO USE TERTIARY CONTACT", App.get(permissionLandlineNumber).toUpperCase()});
        }

        if(!App.get(secondaryMobileNumber1).isEmpty() && !App.get(secondaryMobileNumber2).isEmpty() ){
            String secondaryMobileNumber = secondaryMobileNumber1.getEditText().getText().toString() + "-" + secondaryMobileNumber2.getEditText().getText().toString();
            observations.add(new String[]{"SECONDARY MOBILE NUMBER", secondaryMobileNumber});

        }

        if(secondaryMobileNumberContact.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"SECONDARY CONTACT OWNER", App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_mother)) ? "MOTHER" :
                    (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_father)) ? "FATHER" :
                            (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_brother)) ? "BROTHER" :
                                    (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_sister)) ? "SISTER" :
                                            (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_paternal_grandfather)) ? "PATERNAL GRANDFATHER":
                                                    (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                            (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                                    (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                                                            (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_uncle)) ? "UNCLE":
                                                                                    (App.get(secondaryMobileNumberContact).equals(getResources().getString(R.string.ctb_aunt)) ? "AUNT" : "OTHER FAMILY RELATION")))))))))});
        }

        if(permissionSecondaryMobileNumber.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"PERMISSION TO USE SECONDARY CONTACT", App.get(permissionSecondaryMobileNumber).toUpperCase()});
        }

        if(!App.get(secondaryLandlineNumber1).isEmpty() && !App.get(secondaryLandlineNumber2).isEmpty() ){
            String secondaryLandlineNumber = secondaryLandlineNumber1.getEditText().getText().toString() + "-" + secondaryLandlineNumber2.getEditText().getText().toString();
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

        if(permissionSecondaryLandlineNumber.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"PERMISSION TO USE QUATERNARY CONTACT", App.get(permissionSecondaryLandlineNumber).toUpperCase()});
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

                String result = serverService.saveEncounterAndObservation("Patient Registration", FORM, formDateCalendar, observations.toArray(new String[][]{}));
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

                    if (!(App.get(address1).equals("") && App.get(address2).equals("") && App.get(district).equals("") && App.get(nearestLandmark).equals(""))) {
                        result = serverService.savePersonAddress(App.get(address1), App.get(address2), App.get(city), App.get(district), App.getProvince(), App.getCountry(), App.getLongitude(), App.getLatitude(), App.get(nearestLandmark), encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }

                    result = serverService.savePersonAttributeType("Primary Contact", App.get(mobileNumber1)+ "-" + App.get(mobileNumber2), encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    result = serverService.savePersonAttributeType("Primary Contact Owner", App.get(mobileNumberContact), encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;



                    if (!(App.get(secondaryMobileNumber1).isEmpty() && App.get(secondaryMobileNumber2).isEmpty())) {
                        result = serverService.savePersonAttributeType("Secondary Contact",  App.get(secondaryMobileNumber1)+ "-" + App.get(secondaryMobileNumber2), encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }
                    if(!(App.get(secondaryMobileNumber1).isEmpty() && App.get(secondaryMobileNumber2).isEmpty())) {
                        result = serverService.savePersonAttributeType("Secondary Contact Owner", App.get(secondaryMobileNumberContact), encounterId);
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
                    resetViews();

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

            if (obs[0][0].equals("NATIONAL IDENTIFICATION NUMBER")) {
                String[] cnicParts = obs[0][1].split("-");
                cnic1.getEditText().setText(cnicParts[0]);
                cnic2.getEditText().setText(cnicParts[1]);
                cnic3.getEditText().setText(cnicParts[2]);
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
                        address2.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("ADDRESS (TEXT)")) {
                address1.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("EXTENDED ADDRESS (TEXT)")) {
                address2.getEditText().setText(obs[0][1]);
            }  else if (obs[0][0].equals("DISTRICT")) {
                district.getSpinner().selectValue(obs[0][1]);
                district.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("VILLAGE")) {
                city.getSpinner().selectValue(obs[0][1]);
                city.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("TYPE OF ADDRESS")) {
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
                mobileNumber1.getEditText().setText(phoneNumberParts[0]);
                mobileNumber2.getEditText().setText(phoneNumberParts[1]);
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
                String[] phoneNumberParts = obs[0][1].split("-");
                landlineNumber1.getEditText().setText(phoneNumberParts[0]);
                landlineNumber2.getEditText().setText(phoneNumberParts[1]);
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
            }else if (obs[0][0].equals("PERMISSION TO USE TERTIARY CONTACT")) {
                for (RadioButton rb : permissionLandlineNumber.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("SECONDARY MOBILE NUMBER")) {
                String[] phoneNumberParts = obs[0][1].split("-");
                secondaryMobileNumber1.getEditText().setText(phoneNumberParts[0]);
                secondaryMobileNumber2.getEditText().setText(phoneNumberParts[1]);
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
            }else if (obs[0][0].equals("PERMISSION TO USE SECONDARY CONTACT")) {
                for (RadioButton rb : permissionSecondaryMobileNumber.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }
            else if (obs[0][0].equals("QUATERNARY CONTACT NUMBER")) {
                String[] phoneNumberParts = obs[0][1].split("-");
                secondaryLandlineNumber1.getEditText().setText(phoneNumberParts[0]);
                secondaryLandlineNumber2.getEditText().setText(phoneNumberParts[1]);
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
            }else if (obs[0][0].equals("PERMISSION TO USE QUATERNARY CONTACT")) {
                for (RadioButton rb : permissionSecondaryLandlineNumber.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
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
        if (spinner == cnicOwner.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                cnicOwnerOther.setVisibility(View.VISIBLE);
            }
            else{
                cnicOwnerOther.setVisibility(View.GONE);
            }
        }
        else if (spinner == district.getSpinner()) {

            String[] cities = serverService.getCityList(App.get(district));
            city.getSpinner().setSpinnerData(cities);
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

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        cnicOwnerOther.setVisibility(View.GONE);
        address1.setVisibility(View.GONE);
        address2.setVisibility(View.GONE);
        mobileNumberContact.setVisibility(View.GONE);
        permissionMobileNumberContact.setVisibility(View.GONE);
        secondaryMobileNumberContact.setVisibility(View.GONE);
        permissionSecondaryMobileNumber.setVisibility(View.GONE);
        landlineNumberContact.setVisibility(View.GONE);
        permissionLandlineNumber.setVisibility(View.GONE);
        secondaryLandlineContact.setVisibility(View.GONE);
        permissionSecondaryLandlineNumber.setVisibility(View.GONE);
        cnic1.getEditText().getText().clear();
        cnic2.getEditText().getText().clear();
        cnic3.getEditText().getText().clear();
        mobileNumber1.getEditText().getText().clear();
        mobileNumber2.getEditText().getText().clear();
        landlineNumber1.getEditText().getText().clear();
        landlineNumber2.getEditText().getText().clear();
        secondaryMobileNumber1.getEditText().getText().clear();
        secondaryMobileNumber2.getEditText().getText().clear();
        secondaryLandlineNumber1.getEditText().getText().clear();
        secondaryLandlineNumber2.getEditText().getText().clear();
        cnic3.getEditText().setError(null);
        mobileNumber2.getEditText().setError(null);
        landlineNumber2.getEditText().setError(null);
        secondaryMobileNumber2.getEditText().setError(null);
        secondaryLandlineNumber2.getEditText().setError(null);

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


    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == addressProvided.getRadioGroup()) {
            if(addressProvided.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))){
                address1.setVisibility(View.VISIBLE);
                address2.setVisibility(View.VISIBLE);
                district.setVisibility(View.VISIBLE);
                city.setVisibility(View.VISIBLE);
            }
            else if(addressProvided.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.no))){
                address1.setVisibility(View.GONE);
                address2.setVisibility(View.GONE);
                district.setVisibility(View.VISIBLE);
                city.setVisibility(View.VISIBLE);
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
