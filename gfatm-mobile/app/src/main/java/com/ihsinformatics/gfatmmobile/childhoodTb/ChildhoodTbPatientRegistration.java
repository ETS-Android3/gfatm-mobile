package com.ihsinformatics.gfatmmobile.childhoodTb;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
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
    TitledEditText addressTown;
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
        FORM_NAME = Forms.CHILDHOOD_SCREENING_LOCATION;
        FORM = Forms.childhoodTb_screeningLocation;

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
        cnic1 = new TitledEditText(context, null, getResources().getString(R.string.fast_nic_number), "", "#####", 5, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLinearLayout.addView(cnic1);
        cnic2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLinearLayout.addView(cnic2);
        cnic3 = new TitledEditText(context, null, "-", "", "#", 1, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLinearLayout.addView(cnic3);
        cnicOwner = new TitledSpinner(context,null,getResources().getString(R.string.ctb_cnic_owner),getResources().getStringArray(R.array.ctb_close_contact_type_list),getResources().getString(R.string.ctb_mother),App.VERTICAL);
        cnicOwnerOther = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_specify),"","",20,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);

        addressProvided = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_address_provided),getResources().getStringArray(R.array.yes_no_options),null,App.HORIZONTAL,App.VERTICAL,true);
        address1 = new TitledEditText(context,null,getResources().getString(R.string.ctb_address1),"","",10,RegexUtil.ALPHANUMERIC_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);

        address2 = new TitledEditText(context,null,getResources().getString(R.string.ctb_address2),"","",50,RegexUtil.ALPHANUMERIC_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);

        city = new TitledSpinner(context,null,getResources().getString(R.string.city),getResources().getStringArray(R.array.ctb_city_list),null,App.VERTICAL);
        addressTown = new TitledEditText(context,null,getResources().getString(R.string.ctb_town),"","",20,RegexUtil.ALPHANUMERIC_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        addressType = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_address_type),getResources().getStringArray(R.array.ctb_address_type_list),null,App.HORIZONTAL,App.VERTICAL,true);
        nearestLandmark = new TitledEditText(context,null,getResources().getString(R.string.ctb_nearest_landmark),"","",50,RegexUtil.ALPHANUMERIC_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        mobileLinearLayout = new LinearLayout(context);
        mobileLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mobileNumber1 = new TitledEditText(context, null, getResources().getString(R.string.mobile_number), "", "####", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        mobileLinearLayout.addView(mobileNumber1);
        mobileNumber2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        mobileLinearLayout.addView(mobileNumber2);
        mobileNumberContact = new TitledSpinner(context,null,getResources().getString(R.string.ctb_whose_contact),getResources().getStringArray(R.array.ctb_close_contact_type_list),null,App.VERTICAL);
        permissionMobileNumberContact = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_permission),getResources().getStringArray(R.array.yes_no_options),null,App.HORIZONTAL,App.VERTICAL,true);
        secondaryMobileLinearLayout= new LinearLayout(context);
        secondaryMobileLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        secondaryMobileNumber1 = new TitledEditText(context, null, getResources().getString(R.string.ctb_secondary_mobile_number), "", "####", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        secondaryMobileLinearLayout.addView(secondaryMobileNumber1);
        secondaryMobileNumber2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        secondaryMobileLinearLayout.addView(secondaryMobileNumber2);
        secondaryMobileNumberContact = new TitledSpinner(context,null,getResources().getString(R.string.ctb_whose_contact),getResources().getStringArray(R.array.ctb_close_contact_type_list),null,App.VERTICAL);
        permissionSecondaryMobileNumber = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_permission),getResources().getStringArray(R.array.yes_no_options),null,App.HORIZONTAL,App.VERTICAL,true);
        landlineLayout = new LinearLayout(context);
        landlineLayout.setOrientation(LinearLayout.HORIZONTAL);
        landlineNumber1 = new TitledEditText(context, null, getResources().getString(R.string.ctb_landline_number), "", "###", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        landlineLayout.addView(landlineNumber1);
        landlineNumber2 = new TitledEditText(context, null, "-", "", "########", 8, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        landlineLayout.addView(landlineNumber2);
        landlineNumberContact = new TitledSpinner(context,null,getResources().getString(R.string.ctb_whose_contact),getResources().getStringArray(R.array.ctb_close_contact_type_list),null,App.VERTICAL);
        permissionLandlineNumber = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_permission),getResources().getStringArray(R.array.yes_no_options),null,App.HORIZONTAL,App.VERTICAL,true);
        secondaryLandlineNumber = new LinearLayout(context);
        secondaryLandlineNumber.setOrientation(LinearLayout.HORIZONTAL);
        secondaryLandlineNumber1 = new TitledEditText(context, null, getResources().getString(R.string.ctb_secondary_landline_number), "", "###", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        secondaryLandlineNumber.addView(secondaryLandlineNumber1);
        secondaryLandlineNumber2 = new TitledEditText(context, null, "-", "", "########", 8, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        secondaryLandlineNumber.addView(secondaryLandlineNumber2);
        secondaryLandlineContact = new TitledSpinner(context,null,getResources().getString(R.string.ctb_whose_contact),getResources().getStringArray(R.array.ctb_close_contact_type_list),null,App.VERTICAL);
        permissionSecondaryLandlineNumber = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_permission),getResources().getStringArray(R.array.yes_no_options),null,App.HORIZONTAL,App.VERTICAL,true);


        mobileNumber2.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mobileNumber = mobileNumber1.getEditText().getText() + s.toString();
                if(RegexUtil.isContactNumber(mobileNumber)){
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
                if(s.length()==4){
                    if(s.charAt(0)=='0' && s.charAt(1)=='3') {
                        mobileNumber2.getEditText().setError(null);
                        mobileNumberContact.setVisibility(View.VISIBLE);
                        permissionMobileNumberContact.setVisibility(View.VISIBLE);
                    }
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
                if(RegexUtil.isContactNumber(mobileNumber)){
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

                if(s.length()==4){
                    if(s.charAt(0)=='0' && s.charAt(1)=='3') {
                        secondaryMobileNumber2.getEditText().setError(null);
                        secondaryMobileNumberContact.setVisibility(View.VISIBLE);
                        permissionSecondaryMobileNumber.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    secondaryMobileNumber2.getEditText().setError("Invalid Mobile Number");
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
                if(RegexUtil.isContactNumber(landlineNumber)){
                    landlineNumberContact.setVisibility(View.VISIBLE);
                    permissionLandlineNumber.setVisibility(View.VISIBLE);
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
                if(s.length()==4){
                    if(s.charAt(0)=='0' && s.charAt(1)=='3') {
                        landlineNumber2.getEditText().setError(null);
                        landlineNumberContact.setVisibility(View.VISIBLE);
                        permissionLandlineNumber.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    landlineNumber2.getEditText().setError("Invalid Landline Number");
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
                if(RegexUtil.isContactNumber(landlineNumber)){
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
                if(s.length()==4){
                    if(s.charAt(0)=='0' && s.charAt(1)=='3') {
                        secondaryLandlineContact.setVisibility(View.VISIBLE);
                        permissionSecondaryLandlineNumber.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    secondaryLandlineNumber2.getEditText().setError("Invalid Landline Number");
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

        views = new View[]{formDate.getButton(),cnicLinearLayout,cnicOwner.getSpinner(),cnicOwnerOther.getEditText(),addressProvided.getRadioGroup(),address1.getEditText(),address2.getEditText(),city.getSpinner(),addressTown.getEditText(),addressType.getRadioGroup(),nearestLandmark.getEditText(),mobileLinearLayout,mobileNumberContact.getSpinner(),permissionMobileNumberContact.getRadioGroup(),secondaryMobileLinearLayout,secondaryMobileNumberContact.getSpinner(),permissionSecondaryMobileNumber.getRadioGroup(),landlineLayout,landlineNumberContact.getSpinner(),permissionLandlineNumber.getRadioGroup(),secondaryLandlineNumber,secondaryLandlineContact.getSpinner(),permissionSecondaryLandlineNumber.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate,cnicLinearLayout,cnicOwner,cnicOwnerOther,addressProvided,address1,address2,city,addressTown,addressType,nearestLandmark,mobileLinearLayout,mobileNumberContact,permissionMobileNumberContact,secondaryMobileLinearLayout,secondaryMobileNumberContact,permissionSecondaryMobileNumber,landlineLayout,landlineNumberContact,permissionLandlineNumber,secondaryLandlineNumber,secondaryLandlineContact,permissionSecondaryLandlineNumber}};

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
        resetViews();

    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();

        if (!formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString())) {

            Date date = App.stringToDate(formDate.getButton().getText().toString(), "dd-MMM-yyyy");

            if (formDateCalendar.after(date)) {

                formDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }



    }

    @Override
    public boolean validate() {
        boolean error=false;
        if (App.get(cnic1).isEmpty() && App.get(cnic2).isEmpty() && App.get(cnic3).isEmpty() ) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic3.getEditText().setError(getString(R.string.empty_field));
            cnic3.getEditText().requestFocus();
            error = true;
        }
        if (App.get(mobileNumber1).isEmpty() && App.get(mobileNumber2).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobileNumber2.getEditText().setError(getString(R.string.empty_field));
            mobileNumber2.getEditText().requestFocus();
            error = true;
        }

        if (App.get(landlineNumber1).isEmpty() && App.get(landlineNumber2).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            landlineNumber2.getEditText().setError(getString(R.string.empty_field));
            landlineNumber2.getEditText().requestFocus();
            error = true;
        }


        if (cnicOwnerOther.getVisibility() == View.VISIBLE && App.get(cnicOwnerOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnicOwnerOther.getEditText().setError(getString(R.string.empty_field));
            cnicOwnerOther.getEditText().requestFocus();
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

        return true;
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
        city.setVisibility(View.GONE);
        addressTown.setVisibility(View.GONE);
        mobileNumberContact.setVisibility(View.GONE);
        permissionMobileNumberContact.setVisibility(View.GONE);
        secondaryMobileNumberContact.setVisibility(View.GONE);
        permissionSecondaryMobileNumber.setVisibility(View.GONE);
        landlineNumberContact.setVisibility(View.GONE);
        permissionLandlineNumber.setVisibility(View.GONE);
        secondaryLandlineContact.setVisibility(View.GONE);
        permissionSecondaryLandlineNumber.setVisibility(View.GONE);


    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == addressProvided.getRadioGroup()) {
            if(addressProvided.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))){
                address1.setVisibility(View.VISIBLE);
                address2.setVisibility(View.VISIBLE);
                addressTown.setVisibility(View.VISIBLE);
                city.setVisibility(View.VISIBLE);
            }
            else if(addressProvided.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.no))){
                addressTown.setVisibility(View.VISIBLE);
                city.setVisibility(View.VISIBLE);
            }
            else{
                address1.setVisibility(View.GONE);
                address2.setVisibility(View.GONE);
                addressTown.setVisibility(View.GONE);
                city.setVisibility(View.GONE);
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
