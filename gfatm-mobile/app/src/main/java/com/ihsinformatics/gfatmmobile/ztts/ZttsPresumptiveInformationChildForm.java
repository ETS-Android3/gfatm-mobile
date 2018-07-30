package com.ihsinformatics.gfatmmobile.ztts;

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
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.Filter;
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
 * Created by Haris on 1/20/2017.
 */

public class ZttsPresumptiveInformationChildForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;
    boolean emptyError = false;

    // Views...
    TitledButton formDate;
    MyTextView building_dwelling_household_code;
    MyTextView primeryContactTextView;
    LinearLayout cnicLinearLayout;
    MyEditText cnic1;
    MyEditText cnic2;
    MyEditText cnic3;
    LinearLayout mobileLinearLayout;

    LinearLayout mobileLinearLayout1;
    MyEditText mobile11;
    MyEditText mobile21;

    LinearLayout mobileLinearLayout11;
    MyEditText mobile111;
    MyEditText mobile211;

    LinearLayout landlineLinearLayout;
    MyEditText landline1;
    MyEditText landline2;

    TitledSpinner cnicOwner;
    TitledEditText otherCnicOwner;
    TitledEditText nic_holder_name;

    MyTextView secondaryContactTextView;
    TitledEditText secondary_contact_name;
    TitledSpinner participant_relationship_with_secondary_contact;


    TitledSpinner province;
    MyLinearLayout addressLayout;
    MyTextView townTextView;
    TitledEditText addressHouse;
    AutoCompleteTextView addressStreet;
    TitledSpinner district;
    TitledSpinner city;
    TitledRadioGroup addressType;
    TitledEditText nearestLandmark;
    TitledEditText contactExternalId;

    ArrayAdapter<String> districtArrayAdapter;
    ArrayAdapter<String> cityArrayAdapter;

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
        formName = Forms.ZTTS_PRESUMPTIVE_INFORMATION_CHILD;
        form = Forms.ztts_presumptiveInformationChildForm;

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

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        building_dwelling_household_code = new MyTextView(context, getResources().getString(R.string.ztts_b_d_h_code));
        primeryContactTextView = new MyTextView(context, getResources().getString(R.string.ztts_primary_contact_info_txt_view));
        primeryContactTextView.setTypeface(null, Typeface.BOLD);
        primeryContactTextView.setPadding(0, 20, 0, 0);
        secondaryContactTextView = new MyTextView(context, getResources().getString(R.string.ztts_secondary_contact_info_txt_view));
        secondaryContactTextView.setTypeface(null, Typeface.BOLD);
        secondaryContactTextView.setPadding(0, 20, 0, 0);
        contactExternalId = new TitledEditText(context, null, getResources().getString(R.string.fast_external_id), "", "", 11, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
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

        cnicOwner = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ztts_participant_relationship_with_primary_contact), getResources().getStringArray(R.array.ztts_participant_relationship_with_primart_contact_options), null, App.VERTICAL, true);
        participant_relationship_with_secondary_contact = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ztts_participant_relationship_with_secondary_contact), getResources().getStringArray(R.array.ztts_participant_relationship_with_secondary_contact_options), null, App.VERTICAL);
        otherCnicOwner = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        nic_holder_name = new TitledEditText(context, null, getResources().getString(R.string.ztts_cnic_holder_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        secondary_contact_name = new TitledEditText(context, null, getResources().getString(R.string.ztts_secondary_contact_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        addressHouse = new TitledEditText(context, null, getResources().getString(R.string.fast_address_1), "", "", 50, RegexUtil.ADDRESS_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        addressLayout = new MyLinearLayout(context, null, App.VERTICAL);
        townTextView = new MyTextView(context, getResources().getString(R.string.fast_address_2));
        addressStreet = new AutoCompleteTextView(context);
        addressStreet.setInputType(InputType.TYPE_CLASS_TEXT);
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(20);
        addressStreet.setFilters(fArray);
        province = new TitledSpinner(context, "", getResources().getString(R.string.province), getResources().getStringArray(R.array.provinces), App.getProvince(), App.VERTICAL);
        addressLayout.addView(townTextView);
        addressLayout.addView(addressStreet);
        district = new TitledSpinner(context, "", getResources().getString(R.string.pet_district), getResources().getStringArray(R.array.pet_empty_array), "", App.VERTICAL);
        city = new TitledSpinner(context, "", getResources().getString(R.string.pet_city), getResources().getStringArray(R.array.pet_empty_array), "", App.VERTICAL,true);
        addressType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_type_of_address_is_this), getResources().getStringArray(R.array.fast_type_of_address_list), "", App.VERTICAL, App.VERTICAL);
        nearestLandmark = new TitledEditText(context, null, getResources().getString(R.string.fast_nearest_landmark), "", "", 50, RegexUtil.ADDRESS_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);


        mobileLinearLayout1 = new LinearLayout(context);
        mobileLinearLayout1.setOrientation(LinearLayout.VERTICAL);
        LinearLayout mobileQuestion1 = new LinearLayout(context);
        mobileQuestion1.setOrientation(LinearLayout.HORIZONTAL);

        MyTextView mobileNumberLabel1 = new MyTextView(context, getResources().getString(R.string.ztts_cnic_holder_mobile_number));
        mobileQuestion1.addView(mobileNumberLabel1);
        TextView mandatorySign11 = new TextView(context);
        mandatorySign11.setText("*");
        mandatorySign11.setTextColor(Color.parseColor("#ff0000"));
        mobileQuestion1.addView(mandatorySign11);
        mobileLinearLayout1.addView(mobileQuestion1);
        LinearLayout mobileNumberPart1 = new LinearLayout(context);
        mobileNumberPart1.setOrientation(LinearLayout.HORIZONTAL);
        mobile11 = new MyEditText(context, "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        mobile11.setHint("03XX");
        mobileNumberPart1.addView(mobile11);
        MyTextView mobileNumberDash1 = new MyTextView(context, " - ");
        mobileNumberPart1.addView(mobileNumberDash1);
        mobile21 = new MyEditText(context, "", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        mobile21.setHint("XXXXXXX");
        mobileNumberPart1.addView(mobile21);
        mobileLinearLayout1.addView(mobileNumberPart1);


        mobileLinearLayout11 = new LinearLayout(context);
        mobileLinearLayout11.setOrientation(LinearLayout.VERTICAL);
        LinearLayout mobileQuestion11 = new LinearLayout(context);
        mobileQuestion11.setOrientation(LinearLayout.HORIZONTAL);

        MyTextView mobileNumberLabel11 = new MyTextView(context, getResources().getString(R.string.ztts_mobile_no_secondary_contact));
        mobileQuestion11.addView(mobileNumberLabel11);
        TextView mandatorySign111 = new TextView(context);
//        mandatorySign111.setText("*");
        mandatorySign111.setTextColor(Color.parseColor("#ff0000"));
        mobileQuestion11.addView(mandatorySign111);
        mobileLinearLayout11.addView(mobileQuestion11);
        LinearLayout mobileNumberPart11 = new LinearLayout(context);
        mobileNumberPart11.setOrientation(LinearLayout.HORIZONTAL);
        mobile111 = new MyEditText(context, "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        mobile111.setHint("03XX");
        mobileNumberPart11.addView(mobile111);
        MyTextView mobileNumberDash11 = new MyTextView(context, " - ");
        mobileNumberPart11.addView(mobileNumberDash11);
        mobile211 = new MyEditText(context, "", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        mobile211.setHint("XXXXXXX");
        mobileNumberPart11.addView(mobile211);
        mobileLinearLayout11.addView(mobileNumberPart11);

        landlineLinearLayout = new LinearLayout(context);
        landlineLinearLayout.setOrientation(LinearLayout.VERTICAL);
        MyTextView landlineLabel = new MyTextView(context, getResources().getString(R.string.fast_landline_number));
        landlineLinearLayout.addView(landlineLabel);
        LinearLayout landlineNumberPart = new LinearLayout(context);
        landlineNumberPart.setOrientation(LinearLayout.HORIZONTAL);
        landline1 = new MyEditText(context, "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        landline1.setHint("0XXX");
        landlineNumberPart.addView(landline1);
        MyTextView landlineNumberDash = new MyTextView(context, " - ");
        landlineNumberPart.addView(landlineNumberDash);
        landline2 = new MyEditText(context, "", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        landline2.setHint("XXXXXXX");
        landlineNumberPart.addView(landline2);
        landlineLinearLayout.addView(landlineNumberPart);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), contactExternalId.getEditText(), cnic1, cnic2, cnic3, cnicOwner.getSpinner(), otherCnicOwner.getEditText(), nic_holder_name.getEditText(), mobile11, mobile21,
                secondary_contact_name.getEditText(), participant_relationship_with_secondary_contact.getSpinner(), mobile111, mobile211, landline1,
                landline2,
                addressHouse.getEditText(), district.getSpinner(),
                city.getSpinner(), addressType.getRadioGroup(), nearestLandmark.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, building_dwelling_household_code, primeryContactTextView, contactExternalId, cnicLinearLayout, cnicOwner, otherCnicOwner, nic_holder_name, mobileLinearLayout1
                        , secondaryContactTextView, secondary_contact_name, participant_relationship_with_secondary_contact, mobileLinearLayout11, landlineLinearLayout,
                        addressHouse, addressLayout, province, district,
                        city, addressType, nearestLandmark}};


        formDate.getButton().setOnClickListener(this);
        cnicOwner.getSpinner().setOnItemSelectedListener(this);
        province.getSpinner().setOnItemSelectedListener(this);
        district.getSpinner().setOnItemSelectedListener(this);

        resetViews();

        cnic1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 5) {
                    cnic2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String cnic = cnic1.getText().toString() + "-" + cnic2.getText().toString() + "-" + cnic3.getText().toString();
                if (RegexUtil.isValidNIC(cnic)) {
//                    cnicOwner.setVisibility(View.VISIBLE);
                    if (cnicOwner.getSpinner().getSelectedItem().equals(getResources().getString(R.string.other))) {
//                        otherCnicOwner.setVisibility(View.VISIBLE);
                    }
                } else {
//                    cnicOwner.setVisibility(View.GONE);
//                    otherCnicOwner.setVisibility(View.GONE);
                }

            }
        });

        cnic2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 7) {
                    cnic3.requestFocus();
                }

                if (s.length() == 0) {
                    cnic1.requestFocus();
                    cnic1.setSelection(cnic1.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String cnic = cnic1.getText().toString() + "-" + cnic2.getText().toString() + "-" + cnic3.getText().toString();
                if (RegexUtil.isValidNIC(cnic)) {
//                    cnicOwner.setVisibility(View.VISIBLE);
                    if (cnicOwner.getSpinner().getSelectedItem().equals(getResources().getString(R.string.other))) {
//                        otherCnicOwner.setVisibility(View.VISIBLE);
                    }
                } else {
//                    cnicOwner.setVisibility(View.GONE);
//                    otherCnicOwner.setVisibility(View.GONE);
                }

            }

        });

        cnic3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    cnic2.requestFocus();
                    cnic2.setSelection(cnic2.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                String cnic = cnic1.getText().toString() + "-" + cnic2.getText().toString() + "-" + cnic3.getText().toString();
//                if (RegexUtil.isValidNIC(cnic)) {
//                    cnicOwner.setVisibility(View.VISIBLE);
//                    if (cnicOwner.getSpinner().getSelectedItem().equals(getResources().getString(R.string.other))) {
//                        otherCnicOwner.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    cnicOwner.setVisibility(View.GONE);
//                    otherCnicOwner.setVisibility(View.GONE);
//                }

            }
        });

        mobile21.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mobile11.requestFocus();
                    mobile11.setSelection(mobile11.getText().length());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobile11.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    mobile21.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobile211.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mobile111.requestFocus();
                    mobile111.setSelection(mobile111.getText().length());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobile111.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    mobile211.requestFocus();
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
                if (s.length() == 0) {
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
                if (s.length() == 4) {
                    landline2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        contactExternalId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String blockCodeString = App.get(contactExternalId);
                String s = "";
                if (blockCodeString.length() >= 4)
                    if (contactExternalId.getEditText().getSelectionStart() >= 1 && contactExternalId.getEditText().getSelectionStart() < 5) {
                        s = String.valueOf(blockCodeString.charAt(contactExternalId.getEditText().getSelectionStart() - 1));
                    }

                if (!blockCodeString.startsWith("2717")) {
                    contactExternalId.getEditText().setText("2717" + s);
                }

                if (contactExternalId.getEditText().getSelectionStart() == 0)
                    contactExternalId.getEditText().setSelection(contactExternalId.getEditText().getText().toString().length());

                if (contactExternalId.getEditText().getText().toString().charAt(3) == '7') {
                    contactExternalId.getEditText().setSelection(contactExternalId.getEditText().getText().toString().length());

                }

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
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }

        formDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = false;

        if (contactExternalId.getEditText().getText().toString().length() < 11 || contactExternalId.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            contactExternalId.getEditText().setError(getString(R.string.invalid_value));
            contactExternalId.getEditText().requestFocus();
            error = true;
        }


        if (addressHouse.getVisibility() == View.VISIBLE && addressHouse.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            addressHouse.getEditText().setError(getString(R.string.invalid_value));
            addressHouse.getEditText().requestFocus();
            error = true;
        }
       /* if (addressStreet.getVisibility() == View.VISIBLE && addressStreet.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            addressStreet.setError(getString(R.string.invalid_value));
            addressStreet.requestFocus();
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

       /* if (!cnic1.getText().toString().trim().isEmpty() && cnic2.getText().toString().trim().isEmpty() && cnic3.getText().toString().trim().isEmpty()) {
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

        if (cnic3.getText().toString().length() > 0 && App.get(cnic3).length() != 1) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic3.setError(getString(R.string.length_message));
            cnic3.requestFocus();
            error = true;
        }*/

        if (otherCnicOwner.getVisibility() == View.VISIBLE && otherCnicOwner.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherCnicOwner.getEditText().setError(getString(R.string.empty_field));
            otherCnicOwner.getEditText().requestFocus();
            error = true;
        }
        if (nic_holder_name.getVisibility() == View.VISIBLE && nic_holder_name.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            nic_holder_name.getEditText().setError(getString(R.string.empty_field));
            nic_holder_name.getEditText().requestFocus();
            error = true;
        }


        if (mobile11.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile11.setError(getString(R.string.empty_field));
            mobile11.requestFocus();
            error = true;
        }

        if (mobile21.getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile21.setError(getString(R.string.empty_field));
            mobile21.requestFocus();
            error = true;
        }

       /*  if (secondaryMobile1.getText().toString().trim().isEmpty() && !secondaryMobile2.getText().toString().trim().isEmpty()) {
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
        }*/


        if (App.get(mobile11).length() != 4) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile11.setError(getString(R.string.length_message));
            mobile11.requestFocus();
            error = true;
        }

        if (App.get(mobile21).length() != 7) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile21.setError(getString(R.string.length_message));
            mobile21.requestFocus();
            error = true;
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

        /* if (!(secondaryMobile1.getText().toString().trim().isEmpty() && secondaryMobile2.getText().toString().trim().isEmpty()) && App.get(secondaryMobile1).length() != 4) {
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
        }*/


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


        final String mobileNumber = mobile11.getText().toString() + mobile21.getText().toString();
        final String secondaryMobileNumber = mobile111.getText().toString() + mobile211.getText().toString();
        final String landlineNumber = landline1.getText().toString() + landline2.getText().toString();


        if (!RegexUtil.isMobileNumber(mobileNumber)) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile21.setError(getString(R.string.incorrect_contact_number));
            mobile21.requestFocus();
            error = true;
        } else {
            mobile21.setError(null);
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


        /*  if (!App.get(secondaryMobile1).isEmpty() && !App.get(secondaryMobile2).isEmpty() && !RegexUtil.isMobileNumber(secondaryMobileNumber)) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryMobile2.setError(getString(R.string.incorrect_contact_number));
            secondaryMobile2.requestFocus();
            error = true;
        } else {
            secondaryMobile2.setError(null);
        }*/


        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            if (!emptyError)
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
        String ownerString = "";
        final HashMap<String, String> personAttribute = new HashMap<String, String>();
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

        String cnicNumber = cnic1.getText().toString() + "-" + cnic2.getText().toString() + "-" + cnic3.getText().toString();
        final String mobileNumber = mobile11.getText().toString() + "-" + mobile21.getText().toString();
        final String secondaryMobileNumber = mobile111.getText().toString() + "-" + mobile211.getText().toString();
        final String landlineNumber = landline1.getText().toString() + "-" + landline2.getText().toString();

        if (contactExternalId.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CONTACT EXTERNAL ID", App.get(contactExternalId)});

        if (cnicNumber.length() == 15) {
            observations.add(new String[]{"NATIONAL IDENTIFICATION NUMBER", cnicNumber});
            personAttribute.put("National ID", cnicNumber);
        }

        if (cnicOwner.getVisibility() == View.VISIBLE) {
            ownerString = App.get(cnicOwner).equals(getResources().getString(R.string.fast_mother)) ? "MOTHER" :
                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_father)) ? "FATHER" :
                            (App.get(cnicOwner).equals(getResources().getString(R.string.fast_sister)) ? "SISTER" :
                                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_brother)) ? "BROTHER" :
                                            (App.get(cnicOwner).equals(getResources().getString(R.string.fast_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.fast_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.fast_uncle)) ? "UNCLE" :
                                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_aunt)) ? "AUNT" :
                                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.ztts_guardian)) ? "GUARDIAN" : "OTHER PRIMARY CONTACT RELATIONSHIP WITH THE PARTICIPANT"))))))))));
            observations.add(new String[]{"PRIMARY CONTACT RELATIONSHIP WITH THE PARTICIPANT", ownerString});
            String[][] cnicOwnerConcept = serverService.getConceptUuidAndDataType(ownerString);
            personAttribute.put("Primary Contact Owner", cnicOwnerConcept[0][0]);
        }
        if (otherCnicOwner.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER PRIMARY CONTACT RELATIONSHIP WITH THE PARTICIPANT", App.get(otherCnicOwner)});

        if (nic_holder_name.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PRIMARY CONTACT NAME", App.get(nic_holder_name)});

        observations.add(new String[]{"CONTACT PHONE NUMBER", mobileNumber});
        personAttribute.put("Primary Contact", mobileNumber);


        if (secondary_contact_name.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SECONDARY CONTACT NAME", App.get(secondary_contact_name)});

        observations.add(new String[]{"SECONDARY MOBILE NUMBER", secondaryMobileNumber});
        personAttribute.put("Secondary Contact", secondaryMobileNumber);


        if (!(App.get(landline1).isEmpty() && App.get(landline2).isEmpty())) {
            observations.add(new String[]{"TERTIARY CONTACT NUMBER", landlineNumber});
            personAttribute.put("Tertiary Contact", landlineNumber);
        }

        if (participant_relationship_with_secondary_contact.getVisibility() == View.VISIBLE && !participant_relationship_with_secondary_contact.getSpinner().getSelectedItem().toString().isEmpty()) {
            ownerString = App.get(participant_relationship_with_secondary_contact).equals(getResources().getString(R.string.fast_mother)) ? "MOTHER" :
                    (App.get(participant_relationship_with_secondary_contact).equals(getResources().getString(R.string.fast_father)) ? "FATHER" :
                            (App.get(participant_relationship_with_secondary_contact).equals(getResources().getString(R.string.fast_sister)) ? "SISTER" :
                                    (App.get(participant_relationship_with_secondary_contact).equals(getResources().getString(R.string.fast_brother)) ? "BROTHER" :
                                            (App.get(participant_relationship_with_secondary_contact).equals(getResources().getString(R.string.fast_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                    (App.get(participant_relationship_with_secondary_contact).equals(getResources().getString(R.string.fast_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                            (App.get(participant_relationship_with_secondary_contact).equals(getResources().getString(R.string.fast_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                                    (App.get(participant_relationship_with_secondary_contact).equals(getResources().getString(R.string.fast_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                                                            (App.get(participant_relationship_with_secondary_contact).equals(getResources().getString(R.string.fast_uncle)) ? "UNCLE" :
                                                                                    (App.get(participant_relationship_with_secondary_contact).equals(getResources().getString(R.string.fast_aunt)) ? "AUNT" :
                                                                                            (App.get(participant_relationship_with_secondary_contact).equals(getResources().getString(R.string.ztts_guardian)) ? "GUARDIAN" : "OTHER"))))))))));
            observations.add(new String[]{"SECONDARY CONTACT RELATIONSHIP WITH THE PARTICIPANT", ownerString});
        }


        if (addressType.getVisibility() == View.VISIBLE && !addressType.getRadioGroup().getSelectedValue().isEmpty())
            observations.add(new String[]{"TYPE OF ADDRESS", App.get(addressType).equals(getResources().getString(R.string.fast_perminant)) ? "PERMANENT ADDRESS" : "TEMPORARY ADDRESS"});

        if (nearestLandmark.getVisibility() == View.VISIBLE && !App.get(nearestLandmark).isEmpty())
            observations.add(new String[]{"NEAREST LANDMARK", App.get(nearestLandmark)});

        if (addressHouse.getVisibility() == View.VISIBLE && !App.get(addressHouse).isEmpty())
            observations.add(new String[]{"ADDRESS (TEXT)", App.get(addressHouse)});


        if (province.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"PROVINCE", App.get(province)});
        }

        if (addressStreet.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"EXTENDED ADDRESS (TEXT)", App.get(addressStreet)});
        }

        if (district.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DISTRICT", App.get(district)});

        if (city.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"VILLAGE", App.get(city)});

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

                String id = null;
                if(App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocallyTesting(formName, form, formDateCalendar,observations.toArray(new String[][]{}));

                String result = "";
                if (!(App.get(addressHouse).equals("") && App.get(addressStreet).equals("") && App.get(district).equals("") && App.get(nearestLandmark).equals(""))) {
                    result = serverService.savePersonAddress(App.get(addressHouse), App.get(addressStreet), App.get(city), App.get(district), App.get(province), App.getCountry(), App.getLongitude(), App.getLatitude(), App.get(nearestLandmark), id);
                    if (!result.equals("SUCCESS")){
                        return result;
                    }
                }

                result = serverService.saveMultiplePersonAttribute(personAttribute, id);
                if (!result.equals("SUCCESS"))
                    return result;

                if (!App.get(contactExternalId).isEmpty() && App.hasKeyListener(contactExternalId)) {
                    if (App.getPatient().getExternalId() != null) {
                        if (!App.getPatient().getExternalId().equals("")) {
                            if (!App.getPatient().getExternalId().equalsIgnoreCase(App.get(contactExternalId))) {
                                result = serverService.saveIdentifier("External ID", App.get(contactExternalId), id);
                                if (!result.equals("SUCCESS"))
                                    return result;
                            }
                        } else {
                            result = serverService.saveIdentifier("External ID", App.get(contactExternalId), id);
                            if (!result.equals("SUCCESS"))
                                return result;
                        }
                    } else {
                        result = serverService.saveIdentifier("External ID", App.get(contactExternalId), id);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }
                }

                result = serverService.saveEncounterAndObservationTesting(formName, form, formDateCalendar, observations.toArray(new String[][]{}), id);
                if (!result.equals("SUCCESS"))
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
            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("CONTACT EXTERNAL ID")) {
                contactExternalId.getEditText().setText(obs[0][1]);
                contactExternalId.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("NATIONAL IDENTIFICATION NUMBER")) {
                String data = obs[0][1];
                cnic1.setText(data.substring(0, 5));
                cnic2.setText(data.substring(6, 13));
                cnic3.setText(data.substring(14));
            } else if (obs[0][0].equals("PRIMARY CONTACT RELATIONSHIP WITH THE PARTICIPANT")) {
                String value = obs[0][1].equals("MOTHER") ? getResources().getString(R.string.fast_mother) :
                        (obs[0][1].equals("FATHER") ? getResources().getString(R.string.fast_father) :
                                (obs[0][1].equals("SISTER") ? getResources().getString(R.string.fast_sister) :
                                        (obs[0][1].equals("BROTHER") ? getResources().getString(R.string.fast_brother) :
                                                (obs[0][1].equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.fast_paternal_grandfather) :
                                                        (obs[0][1].equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.fast_paternal_grandmother) :
                                                                (obs[0][1].equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.fast_maternal_grandfather) :
                                                                        (obs[0][1].equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.fast_maternal_grandmother) :
                                                                                (obs[0][1].equals("UNCLE") ? getResources().getString(R.string.fast_uncle) :
                                                                                        (obs[0][1].equals("AUNT") ? getResources().getString(R.string.fast_aunt) :
                                                                                                (obs[0][1].equals("GUARADIAN") ? getResources().getString(R.string.ztts_guardian) :
                                                                                                        getResources().getString(R.string.fast_other_title)))))))))));


                cnicOwner.getSpinner().selectValue(value);
                cnicOwner.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER PRIMARY CONTACT RELATIONSHIP WITH THE PARTICIPANT")) {
                otherCnicOwner.getEditText().setText(obs[0][1]);
                otherCnicOwner.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("PRIMARY CONTACT NAME")) {
                nic_holder_name.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("CONTACT PHONE NUMBER")) {
                String mobNum = obs[0][1];
                mobile11.setText(mobNum.substring(0, 4));
                mobile21.setText(mobNum.substring(5, 12));
            } else if (obs[0][0].equals("SECONDARY CONTACT NAME")) {
                secondary_contact_name.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("SECONDARY MOBILE NUMBER")) {
                String mobNum = obs[0][1];
                mobile111.setText(mobNum.substring(0, 4));
                mobile211.setText(mobNum.substring(5, 12));
            } else if (obs[0][0].equals("SECONDARY CONTACT RELATIONSHIP WITH THE PARTICIPANT")) {
                String value = obs[0][1].equals("MOTHER") ? getResources().getString(R.string.fast_mother) :
                        (obs[0][1].equals("FATHER") ? getResources().getString(R.string.fast_father) :
                                (obs[0][1].equals("SISTER") ? getResources().getString(R.string.fast_sister) :
                                        (obs[0][1].equals("BROTHER") ? getResources().getString(R.string.fast_brother) :
                                                (obs[0][1].equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.fast_paternal_grandfather) :
                                                        (obs[0][1].equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.fast_paternal_grandmother) :
                                                                (obs[0][1].equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.fast_maternal_grandfather) :
                                                                        (obs[0][1].equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.fast_maternal_grandmother) :
                                                                                (obs[0][1].equals("UNCLE") ? getResources().getString(R.string.fast_uncle) :
                                                                                        (obs[0][1].equals("AUNT") ? getResources().getString(R.string.fast_aunt) :
                                                                                                (obs[0][1].equals("GUARADIAN") ? getResources().getString(R.string.ztts_guardian) :
                                                                                                        getResources().getString(R.string.fast_other_title)))))))))));


                participant_relationship_with_secondary_contact.getSpinner().selectValue(value);
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
            }else if (obs[0][0].equals("TERTIARY CONTACT NUMBER")) {
                String landNum = obs[0][1];
                if (landNum.length() == 12) {
                    landline1.setText(landNum.substring(0, 4));
                    landline2.setText(landNum.substring(5, 12));
                } else {
                    landline1.setText(landNum.substring(0, 3));
                    landline2.setText(landNum.substring(4, 11));
                }
            } else if (obs[0][0].equals("NEAREST LANDMARK")) {
                nearestLandmark.getEditText().setText(obs[0][1]);
                nearestLandmark.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ADDRESS (TEXT)")) {
                addressHouse.getEditText().setText(obs[0][1]);
                addressHouse.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("EXTENDED ADDRESS (TEXT)")) {
                addressStreet.setText(obs[0][1]);
                addressStreet.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("PROVINCE")) {
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

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
//        cnicOwner.setVisibility(View.GONE);
//        otherCnicOwner.setVisibility(View.GONE);
        // townTextView.setVisibility(View.GONE);
        contactExternalId.getEditText().setText("2717");


        String[] districts = serverService.getDistrictList(App.getProvince());
        district.getSpinner().setSpinnerData(districts);

        String[] cities = serverService.getCityList(App.get(district));
        city.getSpinner().setSpinnerData(cities);
        Boolean flag = true;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);

                refill(formId);
                flag = false;


            } else bundle.putBoolean("save", false);

        }

        if (flag) {
            //HERE FOR AUTOPOPULATING OBS
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
                    String buildingCode = serverService.getLatestObsValue(App.getPatientId(), Forms.ZTTS_CHILD_SCREENING, "BUILDING CODE");
                    String dwellingCode = serverService.getLatestObsValue(App.getPatientId(), Forms.ZTTS_CHILD_SCREENING, "DWELLING CODE");
                    String householdCode = serverService.getLatestObsValue(App.getPatientId(), Forms.ZTTS_CHILD_SCREENING, "HOUSEHOLD CODE");

                    if (buildingCode != null)
                        if (!buildingCode.equals(""))
                            result.put("building_code", buildingCode);

                    if (dwellingCode != null)
                        if (!dwellingCode.equals(""))
                            result.put("dwelling_code", dwellingCode);

                    if (householdCode != null)
                        if (!householdCode.equals(""))
                            result.put("household_code", householdCode);


                    return result;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                }

                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();
                    String stringcountbuildings = "";
                    String stringcountDwellings = "";
                    String stringcountHouseHoldes = "";

                    try {

                        if (result.get("building_code").trim().length() <= 1) {
                            stringcountbuildings = "00" + result.get("building_code").trim();
                        } else if (result.get("building_code").trim().length() <= 2) {
                            stringcountbuildings = "0" + result.get("building_code").trim();
                        } else {
                            stringcountbuildings = "" + result.get("building_code").trim();
                        }
                    } catch (Exception e) {
                    }

                    try {

                        if (result.get("dwelling_code").trim().length() <= 1) {
                            stringcountDwellings = "00" + result.get("dwelling_code").trim();
                        } else if (result.get("dwelling_code").trim().length() <= 2) {
                            stringcountDwellings = "0" + result.get("dwelling_code").trim();
                        } else {
                            stringcountDwellings = "" + result.get("dwelling_code").trim();
                        }
                    } catch (Exception e) {
                    }
                    try {
                        if (result.get("household_code").trim().length() <= 1) {
                            stringcountHouseHoldes = "00" + result.get("household_code").trim();
                        } else if (result.get("household_code").trim().length() <= 2) {
                            stringcountHouseHoldes = "0" + result.get("household_code").trim();
                        } else {
                            stringcountHouseHoldes = "" + result.get("household_code").trim();
                        }
                    } catch (Exception e) {
                    }
                    building_dwelling_household_code.setText("Building-Dwelling-Household Code :  " + stringcountbuildings + "-" + stringcountDwellings + "-" + stringcountHouseHoldes);
                }
            };
            autopopulateFormTask.execute("");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;

        if (spinner == cnicOwner.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title)) && cnicOwner.getVisibility() == View.VISIBLE) {
                otherCnicOwner.setVisibility(View.VISIBLE);
            } else {
                otherCnicOwner.setVisibility(View.GONE);
            }
        } else if (spinner == district.getSpinner()) {

            if (district.getSpinner().getTag() == null) {

                String[] cities = serverService.getCityList(App.get(district));
                city.getSpinner().setSpinnerData(cities);
            } else city.getSpinner().setTag(null);

        } else if (spinner == province.getSpinner()) {

            if (province.getSpinner().getTag() == null) {
                String[] districts = serverService.getDistrictList(App.get(province));
                district.getSpinner().setSpinnerData(districts);
            } else province.getSpinner().setTag(null);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {


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