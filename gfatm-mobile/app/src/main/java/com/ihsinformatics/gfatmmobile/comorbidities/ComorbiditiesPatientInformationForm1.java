package com.ihsinformatics.gfatmmobile.comorbidities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import java.util.HashMap;

/**
 * Created by Fawad Jawiad on 20-Feb-17.
 */

public class ComorbiditiesPatientInformationForm1 extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;

    TitledEditText indexExternalPatientId;
    TitledEditText gpClinicCode;
    TitledEditText specifyOther;
    TitledEditText fatherName;
    TitledEditText husbandName;
    TitledEditText nic;
    TitledSpinner nicOwner;
    TitledEditText otherNicOwner;
    TitledRadioGroup addressProvided;
    TitledEditText address1;
    TitledEditText address2;
    TitledEditText town;
    TitledEditText city;
    TitledRadioGroup addressType;
    TitledEditText landmark;
    TitledEditText mobileNumber1;
    TitledEditText mobileNumber2;
    TitledEditText landline1;
    TitledEditText landline2;
    TitledRadioGroup tbStatus;
    TitledRadioGroup tbCategory;
    TitledSpinner maritalStatus;
    TitledSpinner householdHeadEducationLevel;
    TitledSpinner patientEducationalLevel;

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
        pager.setAdapter(new ComorbiditiesPatientInformationForm1.MyAdapter());
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

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        indexExternalPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_external_id), "", "", 11, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        gpClinicCode = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_gp_clinic_code), "", getResources().getString(R.string.comorbidities_preferredlocation_gpcliniccode_range), 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        specifyOther = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_location_other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        husbandName = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_husband_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        fatherName = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_father_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        nic = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_nic), "", "", 15, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        nicOwner = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_patient_information_cnic_owner), getResources().getStringArray(R.array.comorbidities_patient_information_nic_options), "", App.VERTICAL);
        otherNicOwner = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_other_cnic_owner), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        addressProvided =  new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_patient_information_address_provided), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL);
        address1 = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_address1), "", "", 10, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        address2 = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_address2), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        town = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_town), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        city = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_city), App.getCity(), "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        city.getEditText().setKeyListener(null);
        addressType = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_patient_information_address_type), getResources().getStringArray(R.array.comorbidities_patient_information_address_type_options), getResources().getString(R.string.comorbidities_patient_information_address_type_permanent), App.HORIZONTAL, App.VERTICAL);
        landmark = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_nearest_landmark), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        mobileNumber1 = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_mobile_number), "", "", RegexUtil.mobileNumberLength, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        mobileNumber2 = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_secondary_mobile), "", "", RegexUtil.mobileNumberLength, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        landline1 = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_landline), "", "", RegexUtil.mobileNumberLength, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        landline2 = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_patient_information_secondary_landline), "", "", RegexUtil.mobileNumberLength, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        tbStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_patient_information_tb_status), getResources().getStringArray(R.array.comorbidities_patient_information_tb_status_options), getResources().getString(R.string.comorbidities_patient_information_tb_status_positive), App.HORIZONTAL, App.VERTICAL);
        tbCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_patient_information_tb_category), getResources().getStringArray(R.array.comorbidities_patient_information_tb_category_options), getResources().getString(R.string.comorbidities_patient_information_tb_category_cat1),  App.HORIZONTAL, App.VERTICAL);
        maritalStatus = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_patient_information_marital_status), getResources().getStringArray(R.array.comorbidities_patient_information_marital_status_options), getResources().getString(R.string.comorbidities_patient_information_marital_status_options_single), App.VERTICAL, true);
        householdHeadEducationLevel = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_patient_information_head_of_education), getResources().getStringArray(R.array.comorbidities_patient_information_education_options), getString(R.string.comorbidities_patient_information_education_options_ele), App.VERTICAL);
        patientEducationalLevel = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_patient_information_eduaction_level), getResources().getStringArray(R.array.comorbidities_patient_information_education_options), getString(R.string.comorbidities_patient_information_education_options_ele), App.VERTICAL);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), indexExternalPatientId.getEditText(), gpClinicCode.getEditText(), specifyOther.getEditText(), husbandName.getEditText(), fatherName.getEditText(), nic.getEditText(), nicOwner.getSpinner(), otherNicOwner.getEditText(),addressProvided.getRadioGroup(),
                address1.getEditText(), address2.getEditText(), town.getEditText(), city.getEditText(), addressType.getRadioGroup(), landmark.getEditText(), mobileNumber1.getEditText(), mobileNumber2.getEditText(), landline1.getEditText(), landline2.getEditText(), tbStatus.getRadioGroup(), tbCategory.getRadioGroup(),
                maritalStatus.getSpinner(), householdHeadEducationLevel.getSpinner(), patientEducationalLevel.getSpinner()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, indexExternalPatientId, gpClinicCode, specifyOther, husbandName, fatherName, nic, nicOwner, otherNicOwner,addressProvided,
                        address1, address2, town, city, addressType, landmark, mobileNumber1, mobileNumber2, landline1, landline2, tbStatus, tbCategory,
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

        resetViews();
    }

    @Override
    public void updateDisplay() {

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

    }

    @Override
    public boolean validate() {

        Boolean error = false;

        if (App.get(city).isEmpty() && city.getVisibility() == View.VISIBLE) {
            city.getEditText().setError(getResources().getString(R.string.empty_field));
            city.getEditText().requestFocus();
            error = true;
        }

        if (specifyOther.getVisibility() == View.VISIBLE && App.get(specifyOther).isEmpty()) {
            gotoPage(1);
            specifyOther.getEditText().setError(getString(R.string.empty_field));
            specifyOther.getEditText().requestFocus();
            error = true;
        }

        if (gpClinicCode.getVisibility() == View.VISIBLE && App.get(gpClinicCode).isEmpty()) {
            gotoPage(1);
            gpClinicCode.getEditText().setError(getString(R.string.empty_field));
            gpClinicCode.getEditText().requestFocus();
            error = true;
        }
        else if (gpClinicCode.getVisibility() == View.VISIBLE && !App.get(gpClinicCode).isEmpty() && (Double.parseDouble(App.get(gpClinicCode)) < 1 || Double.parseDouble(App.get(gpClinicCode)) > 50)) {
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

        if (validate()) {

            resetViews();
        }

        //resetViews();
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
        if (spinner == nicOwner.getSpinner()) {
            if (App.get(nicOwner).equals(getResources().getString(R.string.pet_other)))
                otherNicOwner.setVisibility(View.VISIBLE);
            else
                otherNicOwner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        displayHusbandName();
        otherNicOwner.setVisibility(View.GONE);
        displayAddressOrNot();
        displayTBCategory();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    void displayHusbandName() {
        if(App.getPatient().getPerson().getGender().equalsIgnoreCase(getResources().getString(R.string.female))) {
            husbandName.setVisibility(View.VISIBLE);
        }
        else {
            husbandName.setVisibility(View.GONE);
        }
    }

    void displayAddressOrNot() {
        if(App.get(addressProvided).equalsIgnoreCase(getResources().getString(R.string.yes))) {
            address1.setVisibility(View.VISIBLE);
            address2.setVisibility(View.VISIBLE);
        }
        else {
            address1.setVisibility(View.GONE);
            address2.setVisibility(View.GONE);
        }
    }

    void displayTBCategory() {
        if(App.get(tbStatus).equalsIgnoreCase(getResources().getString(R.string.comorbidities_patient_information_tb_status_positive))){
            tbCategory.setVisibility(View.VISIBLE);
        }
        else {
            tbCategory.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if(radioGroup == addressProvided.getRadioGroup()) {
            displayAddressOrNot();
        }

        if(radioGroup == tbStatus.getRadioGroup()) {
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
