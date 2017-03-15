package com.ihsinformatics.gfatmmobile.fast;

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
import android.text.InputType;
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
 * Created by Haris on 1/20/2017.
 */

public class FastPresumptiveInformationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    // Views...
    TitledButton formDate;
    LinearLayout cnicLinearLayout;
    TitledEditText cnic1;
    TitledEditText cnic2;
    TitledEditText cnic3;
    LinearLayout mobileLinearLayout;
    TitledEditText mobile1;
    TitledEditText mobile2;
    LinearLayout secondaryMobileLinearLayout;
    TitledEditText secondaryMobile1;
    TitledEditText secondaryMobile2;
    LinearLayout landlineLinearLayout;
    TitledEditText landline1;
    TitledEditText landline2;
    LinearLayout secondaryLandlineLinearLayout;
    TitledEditText secondaryLandline1;
    TitledEditText secondaryLandline2;
    TitledSpinner cnicOwner;
    TitledEditText otherCnicOwner;
    TitledRadioGroup addressProvided;
    TitledEditText addressHouse;
    TitledEditText addressStreet;
    TitledSpinner addressTown;
    TitledEditText city;
    TitledRadioGroup addressType;
    TitledEditText nearestLandmark;
    TitledRadioGroup contactPermission;

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
        FORM_NAME = Forms.FAST_PRESUMPTIVE_INFORMATION_FORM;
        FORM = Forms.fastPresumptiveInformationForm;

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
        cnicLinearLayout = new LinearLayout(context);
        cnicLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        cnic1 = new TitledEditText(context, null, getResources().getString(R.string.fast_nic_number), "", "#####", 5, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        cnicLinearLayout.addView(cnic1);
        cnic2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLinearLayout.addView(cnic2);
        cnic3 = new TitledEditText(context, null, "-", "", "#", 1, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLinearLayout.addView(cnic3);
        cnicOwner = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_whose_nic_is_this), getResources().getStringArray(R.array.fast_whose_nic_is_this_list), getResources().getString(R.string.fast_self), App.VERTICAL);
        otherCnicOwner = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        addressProvided = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_patient_provided_their_address), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        addressHouse = new TitledEditText(context, null, getResources().getString(R.string.fast_address_1), "", "", 10, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        addressStreet = new TitledEditText(context, null, getResources().getString(R.string.fast_address_2), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        addressTown = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_town), getResources().getStringArray(R.array.fast_towns_list),"Default Town", App.VERTICAL);
        city = new TitledEditText(context, null, getResources().getString(R.string.fast_city), App.getCity(), "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        city.getEditText().setKeyListener(null);
        city.getEditText().setFocusable(false);
        addressType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_type_of_address_is_this), getResources().getStringArray(R.array.fast_type_of_address_list), getResources().getString(R.string.fast_perminant), App.VERTICAL, App.VERTICAL);
        nearestLandmark = new TitledEditText(context, null, getResources().getString(R.string.fast_nearest_landmark), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        contactPermission = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_can_we_call_you), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        mobileLinearLayout = new LinearLayout(context);
        mobileLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mobile1 = new TitledEditText(context, null, getResources().getString(R.string.fast_mobile_number), "", "####", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        mobileLinearLayout.addView(mobile1);
        mobile2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        mobileLinearLayout.addView(mobile2);
        secondaryMobileLinearLayout = new LinearLayout(context);
        secondaryMobileLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        secondaryMobile1 = new TitledEditText(context, null, getResources().getString(R.string.fast_secondary_mobile), "", "####", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        secondaryMobileLinearLayout.addView(secondaryMobile1);
        secondaryMobile2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        secondaryMobileLinearLayout.addView(secondaryMobile2);
        landlineLinearLayout = new LinearLayout(context);
        landlineLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        landline1 = new TitledEditText(context, null, getResources().getString(R.string.fast_landline_number), "", "####", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        landlineLinearLayout.addView(landline1);
        landline2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        landlineLinearLayout.addView(landline2);
        secondaryLandlineLinearLayout = new LinearLayout(context);
        secondaryLandlineLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        secondaryLandline1 = new TitledEditText(context, null, getResources().getString(R.string.fast_secondary_landline), "", "####", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        secondaryLandlineLinearLayout.addView(secondaryLandline1);
        secondaryLandline2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        secondaryLandlineLinearLayout.addView(secondaryLandline2);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), cnic1.getEditText(), cnic2.getEditText(), cnic3.getEditText(), cnicOwner.getSpinner(), otherCnicOwner.getEditText(),
                addressProvided.getRadioGroup(), addressHouse.getEditText(), addressStreet.getEditText(), addressTown.getSpinner(),
                city.getEditText(), addressType.getRadioGroup(), nearestLandmark.getEditText(), contactPermission.getRadioGroup()
                , mobile1.getEditText(), mobile2.getEditText(), secondaryMobile1.getEditText(), secondaryMobile2.getEditText(), landline1.getEditText(),
                landline2.getEditText(), secondaryLandline1.getEditText(), secondaryLandline2.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, cnicLinearLayout, cnicOwner, otherCnicOwner, addressProvided, addressHouse, addressStreet, addressTown,
                        city, addressType, nearestLandmark, contactPermission, mobileLinearLayout, secondaryMobileLinearLayout,
                        landlineLinearLayout, secondaryLandlineLinearLayout}};


        formDate.getButton().setOnClickListener(this);
        cnicOwner.getSpinner().setOnItemSelectedListener(this);
        addressProvided.getRadioGroup().setOnCheckedChangeListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();


            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            }else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            }
            else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        }
    }

    @Override
    public boolean validate() {
        Boolean error = false;

        if (App.get(cnic1).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic1.getEditText().setError(getString(R.string.empty_field));
            cnic1.getEditText().requestFocus();
            error = true;
        }

        if (App.get(cnic2).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic2.getEditText().setError(getString(R.string.empty_field));
            cnic2.getEditText().requestFocus();
            error = true;
        }

        if (App.get(cnic3).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic3.getEditText().setError(getString(R.string.empty_field));
            cnic3.getEditText().requestFocus();
            error = true;
        }

        if (App.get(cnic1).length() != 5) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic1.getEditText().setError(getString(R.string.length_message));
            cnic1.getEditText().requestFocus();
            error = true;
        }

        if (App.get(cnic2).length() != 7) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic2.getEditText().setError(getString(R.string.length_message));
            cnic2.getEditText().requestFocus();
            error = true;
        }

        if (App.get(cnic3).length() != 1) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic3.getEditText().setError(getString(R.string.length_message));
            cnic3.getEditText().requestFocus();
            error = true;
        }

        if (otherCnicOwner.getVisibility() == View.VISIBLE && App.get(otherCnicOwner).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherCnicOwner.getEditText().setError(getString(R.string.empty_field));
            otherCnicOwner.getEditText().requestFocus();
            error = true;
        }

        if (App.get(mobile1).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile1.getEditText().setError(getString(R.string.empty_field));
            mobile1.getEditText().requestFocus();
            error = true;
        }

        if (App.get(mobile2).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile2.getEditText().setError(getString(R.string.empty_field));
            mobile2.getEditText().requestFocus();
            error = true;
        }

        if (App.get(secondaryMobile1).isEmpty() && !App.get(secondaryMobile2).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryMobile1.getEditText().setError(getString(R.string.empty_field));
            secondaryMobile1.getEditText().requestFocus();
            error = true;
        } else {
            secondaryMobile1.getEditText().setError(null);
        }

        if (App.get(secondaryMobile2).isEmpty() && !App.get(secondaryMobile1).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryMobile2.getEditText().setError(getString(R.string.empty_field));
            secondaryMobile2.getEditText().requestFocus();
            error = true;
        } else {
            secondaryMobile2.getEditText().setError(null);
        }

        if (App.get(landline1).isEmpty() && !App.get(landline2).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            landline1.getEditText().setError(getString(R.string.empty_field));
            landline1.getEditText().requestFocus();
            error = true;
        } else {
            landline1.getEditText().setError(null);
        }

        if (App.get(landline2).isEmpty() && !App.get(landline1).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            landline2.getEditText().setError(getString(R.string.empty_field));
            landline2.getEditText().requestFocus();
            error = true;
        } else {
            landline2.getEditText().setError(null);
        }

        if (App.get(secondaryLandline1).isEmpty() && !App.get(secondaryLandline2).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryLandline1.getEditText().setError(getString(R.string.empty_field));
            secondaryLandline1.getEditText().requestFocus();
            error = true;
        } else {
            secondaryLandline1.getEditText().setError(null);
        }

        if (App.get(secondaryLandline2).isEmpty() && !App.get(secondaryLandline1).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryLandline2.getEditText().setError(getString(R.string.empty_field));
            secondaryLandline2.getEditText().requestFocus();
            error = true;
        } else {
            secondaryLandline2.getEditText().setError(null);
        }

        if (App.get(mobile1).length() != 4) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile1.getEditText().setError(getString(R.string.length_message));
            mobile1.getEditText().requestFocus();
            error = true;
        }

        if (App.get(mobile2).length() != 7) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile2.getEditText().setError(getString(R.string.length_message));
            mobile2.getEditText().requestFocus();
            error = true;
        }

        if (!(App.get(secondaryMobile1).isEmpty() && App.get(secondaryMobile2).isEmpty()) && App.get(secondaryMobile1).length() != 4) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryMobile1.getEditText().setError(getString(R.string.length_message));
            secondaryMobile1.getEditText().requestFocus();
            error = true;
        }

        if (!(App.get(secondaryMobile1).isEmpty() && App.get(secondaryMobile2).isEmpty()) && App.get(secondaryMobile2).length() != 7) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryMobile2.getEditText().setError(getString(R.string.length_message));
            secondaryMobile2.getEditText().requestFocus();
            error = true;
        }

        if (!(App.get(landline1).isEmpty() && App.get(landline2).isEmpty()) && !(App.get(landline1).length() == 3 || App.get(landline1).length() == 4)) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            landline1.getEditText().setError(getString(R.string.length_message));
            landline1.getEditText().requestFocus();
            error = true;
        }

        if (!(App.get(landline1).isEmpty() && App.get(landline2).isEmpty()) && App.get(landline2).length() != 7) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            landline2.getEditText().setError(getString(R.string.length_message));
            landline2.getEditText().requestFocus();
            error = true;
        }

        if (!(App.get(secondaryLandline1).isEmpty() && App.get(secondaryLandline2).isEmpty()) && !(App.get(secondaryLandline1).length() == 3 || App.get(secondaryLandline1).length() == 4)) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryLandline1.getEditText().setError(getString(R.string.length_message));
            secondaryLandline1.getEditText().requestFocus();
            error = true;
        }

        if (!(App.get(secondaryLandline1).isEmpty() && App.get(secondaryLandline2).isEmpty()) && App.get(secondaryLandline2).length() != 7) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryLandline2.getEditText().setError(getString(R.string.length_message));
            secondaryLandline2.getEditText().requestFocus();
            error = true;
        }

        final String mobileNumber = mobile1.getEditText().getText().toString() + mobile2.getEditText().getText().toString();
        final String secondaryMobileNumber = secondaryMobile1.getEditText().getText().toString() + secondaryMobile2.getEditText().getText().toString();
        final String landlineNumber = landline1.getEditText().getText().toString() + landline2.getEditText().getText().toString();
        final String secondaryLandlineNumber = secondaryLandline1.getEditText().getText().toString() + secondaryLandline2.getEditText().getText().toString();

        if (!RegexUtil.isMobileNumber(mobileNumber)) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile2.getEditText().setError(getString(R.string.incorrect_contact_number));
            mobile2.getEditText().requestFocus();
            error = true;
        } else {
            mobile2.getEditText().setError(null);
        }

        if (!App.get(secondaryMobile1).isEmpty() && !App.get(secondaryMobile2).isEmpty() && !RegexUtil.isMobileNumber(secondaryMobileNumber)) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryMobile2.getEditText().setError(getString(R.string.incorrect_contact_number));
            secondaryMobile2.getEditText().requestFocus();
            error = true;
        } else {
            secondaryMobile2.getEditText().setError(null);
        }

        if (!App.get(landline1).isEmpty() && !App.get(landline2).isEmpty() && !RegexUtil.isLandlineNumber(landlineNumber)) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            landline2.getEditText().setError(getString(R.string.incorrect_contact_number));
            landline2.getEditText().requestFocus();
            error = true;
        } else {
            landline2.getEditText().setError(null);
        }


        if (!App.get(secondaryLandline1).isEmpty() && !App.get(secondaryLandline2).isEmpty() && !RegexUtil.isLandlineNumber(secondaryLandlineNumber)) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryLandline2.getEditText().setError(getString(R.string.incorrect_contact_number));
            secondaryLandline2.getEditText().requestFocus();
            error = true;
        } else {
            secondaryLandline2.getEditText().setError(null);
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
      /*  observations.add (new String[] {"LONGITUDE (DEGREES)", String.valueOf(longitude)});
        observations.add (new String[] {"LATITUDE (DEGREES)", String.valueOf(latitude)});*/

        String cnicNumber = cnic1.getEditText().getText().toString() +"-"+ cnic2.getEditText().getText().toString() +"-"+ cnic3.getEditText().getText().toString();
        final String mobileNumber = mobile1.getEditText().getText().toString() +"-"+ mobile2.getEditText().getText().toString();
        final String secondaryMobileNumber = secondaryMobile1.getEditText().getText().toString() +"-"+ secondaryMobile2.getEditText().getText().toString();
        final String landlineNumber = landline1.getEditText().getText().toString() +"-"+ landline2.getEditText().getText().toString();
        final String secondaryLandlineNumber = secondaryLandline1.getEditText().getText().toString() +"-"+ secondaryLandline2.getEditText().getText().toString();


        observations.add(new String[]{"NATIONAL IDENTIFICATION NUMBER", cnicNumber});

        if (cnicOwner.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COMPUTERIZED NATIONAL IDENTIFICATION OWNER", App.get(cnicOwner).equals(getResources().getString(R.string.fast_self)) ? "SELF" :
                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_mother)) ? "MOTHER" :
                            (App.get(cnicOwner).equals(getResources().getString(R.string.fast_father)) ? "FATHER" :
                                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_sister)) ? "SISTER" :
                                            (App.get(cnicOwner).equals(getResources().getString(R.string.fast_brother)) ? "BROTHER" :
                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_spouse)) ? "SPOUSE" :
                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.fast_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.fast_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.fast_uncle)) ? "UNCLE" :
                                                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_aunt)) ? "AUNT" :
                                                                                                            (App.get(cnicOwner).equals(getResources().getString(R.string.fast_son)) ? "SON" :
                                                                                                                    (App.get(cnicOwner).equals(getResources().getString(R.string.fast_daughter)) ? "DAUGHTER" : "OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER")))))))))))))});
        if (otherCnicOwner.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER", App.get(otherCnicOwner)});

        if (addressProvided.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT PROVIDED ADDRESS", App.get(addressProvided).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});

        if (addressType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TYPE OF ADDRESS", App.get(addressType).equals(getResources().getString(R.string.fast_perminant)) ? "PERMANENT ADDRESS" : "TEMPORARY ADDRESS"});

        if (nearestLandmark.getVisibility() == View.VISIBLE && !App.get(nearestLandmark).isEmpty())
            observations.add(new String[]{"NEAREST LANDMARK", App.get(nearestLandmark)});

        if (contactPermission.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PERMISSION TO USE CONTACT NUMBER", App.get(contactPermission).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});

        observations.add(new String[]{"CONTACT PHONE NUMBER", mobileNumber});

        if (!(App.get(secondaryMobile1).isEmpty() && App.get(secondaryMobile2).isEmpty()))
        observations.add(new String[]{"SECONDARY MOBILE NUMBER", secondaryMobileNumber});

        if (!(App.get(landline1).isEmpty() && App.get(landline2).isEmpty()))
        observations.add(new String[]{"TERTIARY CONTACT NUMBER", landlineNumber});

        if (!(App.get(secondaryLandline1).isEmpty() && App.get(secondaryLandline2).isEmpty()))
        observations.add(new String[]{"QUATERNARY CONTACT NUMBER", secondaryLandlineNumber});

        if (addressHouse.getVisibility() == View.VISIBLE && !App.get(addressHouse).isEmpty())
        observations.add(new String[]{"ADDRESS (TEXT)", App.get(addressHouse)});

        if (addressStreet.getVisibility() == View.VISIBLE && !App.get(addressStreet).isEmpty())
        observations.add(new String[]{"EXTENDED ADDRESS (TEXT)", App.get(addressStreet)});

        if (addressTown.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TOWN",addressTown.getSpinner().getSelectedItem().toString()});

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

                String result = serverService.saveEncounterAndObservation("Presumptive Information", FORM, formDateCalendar, observations.toArray(new String[][]{}));
                if (!result.contains("SUCCESS"))
                    return result;
                else {

                    String encounterId = "";

                    if (result.contains("_")) {
                        String[] successArray = result.split("_");
                        encounterId = successArray[1];
                    }

                    result = serverService.savePersonAddress(App.get(addressHouse), App.get(addressStreet), App.getCity(), App.get(addressTown), "", longitude, latitude, "", encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    result = serverService.savePersonAttributeType("Primary Contact", mobileNumber, encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    if (!(App.get(secondaryMobile1).isEmpty() && App.get(secondaryMobile2).isEmpty())) {
                        result = serverService.savePersonAttributeType("Secondary Contact", secondaryMobileNumber, encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }

                    if (!(App.get(landline1).isEmpty() && App.get(landline2).isEmpty())) {
                        result = serverService.savePersonAttributeType("Tertiary Contact", landlineNumber, encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }

                    if (!(App.get(secondaryLandline1).isEmpty() && App.get(secondaryLandline2).isEmpty())) {
                        result = serverService.savePersonAttributeType("Quaternary Contact", secondaryLandlineNumber, encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }

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

      /*  formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        formValues.put(lastName.getTag(), App.get(lastName));
        formValues.put(husbandName.getTag(), App.get(husbandName));
        formValues.put(gender.getTag(), App.get(gender));

        serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);*/

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
            if (obs[0][0].equals("FORM START TIME")) {
                startTime = App.stringToDate(obs[0][1], "yyyy-MM-dd hh:mm:ss");
            } else if (obs[0][0].equals("NATIONAL IDENTIFICATION NUMBER")) {
                String data = obs[0][1];
                cnic1.getEditText().setText(data.substring(0,5));
                cnic2.getEditText().setText(data.substring(6,13));
                cnic3.getEditText().setText(data.substring(14));
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
                addressStreet.getEditText().setText(obs[0][1]);
                addressStreet.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("CityVillage")) {
                city.getEditText().setText(obs[0][1]);
                city.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("TOWN")) {
                addressTown.getSpinner().selectValue(obs[0][1]);
                addressTown.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("CONTACT PHONE NUMBER")) {
                String mobNum = obs[0][1];
                mobile1.getEditText().setText(mobNum.substring(0,4));
                mobile2.getEditText().setText(mobNum.substring(5,12));
            }
            else if (obs[0][0].equals("SECONDARY MOBILE NUMBER")) {
                String mobNum2 = obs[0][1];
                secondaryMobile1.getEditText().setText(mobNum2.substring(0,4));
                secondaryMobile2.getEditText().setText(mobNum2.substring(5,12));
            }
            else if (obs[0][0].equals("TERTIARY CONTACT NUMBER")) {
                String landNum = obs[0][1];
                if(landNum.length() == 11) {
                    landline1.getEditText().setText(landNum.substring(0, 4));
                    landline2.getEditText().setText(landNum.substring(5, 12));
                }
                else{
                    landline1.getEditText().setText(landNum.substring(0, 3));
                    landline2.getEditText().setText(landNum.substring(4, 11));
                }
            }
            else if (obs[0][0].equals("QUATERNARY CONTACT NUMBER")) {
                String landNum1 = obs[0][1];
                if(landNum1.length() == 11) {
                    secondaryLandline1.getEditText().setText(landNum1.substring(0, 4));
                    secondaryLandline2.getEditText().setText(landNum1.substring(5, 12));
                }
                else{
                    secondaryLandline1.getEditText().setText(landNum1.substring(0, 3));
                    secondaryLandline2.getEditText().setText(landNum1.substring(4, 11));
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
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        otherCnicOwner.setVisibility(View.GONE);

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;

        if (spinner == cnicOwner.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                otherCnicOwner.setVisibility(View.VISIBLE);
            } else {
                otherCnicOwner.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if (radioGroup == addressProvided.getRadioGroup()) {
            if (addressProvided.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                addressHouse.setVisibility(View.VISIBLE);
                addressStreet.setVisibility(View.VISIBLE);
            } else {
                addressHouse.setVisibility(View.GONE);
                addressStreet.setVisibility(View.GONE);
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