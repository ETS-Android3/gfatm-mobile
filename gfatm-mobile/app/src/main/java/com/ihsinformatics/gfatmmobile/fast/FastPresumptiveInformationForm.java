package com.ihsinformatics.gfatmmobile.fast;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
 * Created by Haris on 1/20/2017.
 */

public class FastPresumptiveInformationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    // Views...
    TitledButton formDate;
    TitledEditText cnic;
    TitledSpinner cnicOwner;
    TitledEditText otherCnicOwner;
    TitledRadioGroup addressProvided;
    TitledEditText addressHouse;
    TitledEditText addressStreet;
    TitledSpinner addressTown;
    TitledSpinner city;
    TitledRadioGroup addressType;
    TitledEditText nearestLandmark;
    TitledRadioGroup contactPermission;
    TitledEditText mobileNumber;
    TitledEditText secondaryMobileNumber;
    TitledEditText landlineNumber;
    TitledEditText secondaryLandlineNumber;


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
        cnic = new TitledEditText(context, null, getResources().getString(R.string.fast_nic_number), "", "", 15, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.VERTICAL, false);
        cnicOwner = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_whose_nic_is_this), getResources().getStringArray(R.array.fast_whose_nic_is_this_list), getResources().getString(R.string.fast_self), App.VERTICAL);
        otherCnicOwner = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        otherCnicOwner.setVisibility(View.GONE);
        addressProvided = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_patient_provided_their_address), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        addressHouse = new TitledEditText(context, null, getResources().getString(R.string.fast_address_1), "", "", 10, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        addressStreet = new TitledEditText(context, null, getResources().getString(R.string.fast_address_2), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        addressTown = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_town), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL);
        city = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.city), getResources().getStringArray(R.array.fast_cities_list), getResources().getString(R.string.fast_karachi), App.VERTICAL);
        addressType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_type_of_address_is_this), getResources().getStringArray(R.array.fast_type_of_address_list), getResources().getString(R.string.fast_perminant), App.VERTICAL, App.VERTICAL);
        nearestLandmark = new TitledEditText(context, null, getResources().getString(R.string.fast_nearest_landmark), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        contactPermission = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_can_we_call_you), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        mobileNumber = new TitledEditText(context, null, getResources().getString(R.string.mobile_number), "", "", 11, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);
        secondaryMobileNumber = new TitledEditText(context, null, getResources().getString(R.string.fast_secondary_mobile), "", "", 11, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);
        landlineNumber = new TitledEditText(context, null, getResources().getString(R.string.fast_landline_number), "", "", 11, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);
        secondaryLandlineNumber = new TitledEditText(context, null, getResources().getString(R.string.fast_secondary_landline), "", "", 11, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), cnic.getEditText(), cnicOwner.getSpinner(), otherCnicOwner.getEditText(),
                addressProvided.getRadioGroup(), addressHouse.getEditText(), addressStreet.getEditText(), addressTown.getSpinner(),
                city.getSpinner(), addressType.getRadioGroup(), nearestLandmark.getEditText(), contactPermission.getRadioGroup()
                , mobileNumber.getEditText(), secondaryMobileNumber.getEditText(), landlineNumber.getEditText(), secondaryLandlineNumber.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, cnic, cnicOwner, otherCnicOwner, addressProvided, addressHouse, addressStreet, addressTown,
                        city, addressType, nearestLandmark, contactPermission, mobileNumber, secondaryMobileNumber,
                        landlineNumber, secondaryLandlineNumber}};


        formDate.getButton().setOnClickListener(this);
        cnicOwner.getSpinner().setOnItemSelectedListener(this);
        addressProvided.getRadioGroup().setOnCheckedChangeListener(this);
    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
    }

    @Override
    public boolean validate() {
        Boolean error = false;

        if (cnic.getVisibility() == View.VISIBLE && App.get(cnic).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cnic.getEditText().setError(getString(R.string.empty_field));
            cnic.getEditText().requestFocus();
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
        if (addressHouse.getVisibility() == View.VISIBLE && App.get(addressHouse).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            addressHouse.getEditText().setError(getString(R.string.empty_field));
            addressHouse.getEditText().requestFocus();
            error = true;
        }
        if (addressStreet.getVisibility() == View.VISIBLE && App.get(addressStreet).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            addressStreet.getEditText().setError(getString(R.string.empty_field));
            addressStreet.getEditText().requestFocus();
            error = true;
        }
        if (nearestLandmark.getVisibility() == View.VISIBLE && App.get(nearestLandmark).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            nearestLandmark.getEditText().setError(getString(R.string.empty_field));
            nearestLandmark.getEditText().requestFocus();
            error = true;
        }
        if (mobileNumber.getVisibility() == View.VISIBLE && App.get(mobileNumber).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobileNumber.getEditText().setError(getString(R.string.empty_field));
            mobileNumber.getEditText().requestFocus();
            error = true;
        }
        if (secondaryMobileNumber.getVisibility() == View.VISIBLE && App.get(secondaryMobileNumber).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryMobileNumber.getEditText().setError(getString(R.string.empty_field));
            secondaryMobileNumber.getEditText().requestFocus();
            error = true;
        }
        if (landlineNumber.getVisibility() == View.VISIBLE && App.get(landlineNumber).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            landlineNumber.getEditText().setError(getString(R.string.empty_field));
            landlineNumber.getEditText().requestFocus();
            error = true;
        }
        if (secondaryLandlineNumber.getVisibility() == View.VISIBLE && App.get(secondaryLandlineNumber).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            secondaryLandlineNumber.getEditText().setError(getString(R.string.empty_field));
            secondaryLandlineNumber.getEditText().requestFocus();
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

      /*  formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        formValues.put(lastName.getTag(), App.get(lastName));
        formValues.put(husbandName.getTag(), App.get(husbandName));
        formValues.put(gender.getTag(), App.get(gender));

        serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);*/

        return true;
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
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
       /* if (spinner == specimenSource.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                specimenSourceOther.setVisibility(View.VISIBLE);
            } else {
                specimenSourceOther.setVisibility(View.GONE);
            }
        } else if (spinner == reasonRejected.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                otherReasonRejected.setVisibility(View.VISIBLE);
            } else {
                otherReasonRejected.setVisibility(View.GONE);
            }
        }*/
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

    /*    if (radioGroup == testContextStatus.getRadioGroup()) {
            if (testContextStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_baseline_new))) {
                tbCategory.setVisibility(View.VISIBLE);
            } else {
                tbCategory.setVisibility(View.GONE);
            }

            if (testContextStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_baseline_repeat))) {
                baselineRepeatReason.setVisibility(View.VISIBLE);
            } else {
                baselineRepeatReason.setVisibility(View.GONE);
            }
        } else if (radioGroup == sampleType.getRadioGroup()) {
            if (sampleType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_extra_pulmonary))) {
                specimenSource.setVisibility(View.VISIBLE);
                if (specimenSource.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                    specimenSourceOther.setVisibility(View.VISIBLE);
                }
            } else {
                specimenSource.setVisibility(View.GONE);
                specimenSourceOther.setVisibility(View.GONE);
            }
        } else if (radioGroup == sampleAccepted.getRadioGroup()) {
            if (sampleAccepted.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                reasonRejected.setVisibility(View.VISIBLE);
                if (reasonRejected.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                    otherReasonRejected.setVisibility(View.VISIBLE);
                }
                cartridgeId.setVisibility(View.GONE);
            } else {
                reasonRejected.setVisibility(View.GONE);
                cartridgeId.setVisibility(View.VISIBLE);
                otherReasonRejected.setVisibility(View.GONE);
            }
        }*/

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
