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
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Haris on 12/15/2016.
 */

public class FastScreeningForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledRadioGroup screeningLocation;
    TitledSpinner hospital;
    TitledRadioGroup hospitalSection;
    TitledEditText hospitalSectionOther;
    TitledSpinner opdWardSection;
    TitledRadioGroup patientAttendant;
    TitledRadioGroup ageRange;
    TitledRadioGroup gender;
    TitledRadioGroup coughTwoWeeks;
    TitledRadioGroup tbContact;
    TitledRadioGroup tbHistory;

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

        PAGE_COUNT = 2;
        FORM_NAME = Forms.FAST_SCREENING_FORM;

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
        screeningLocation = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_location_of_screening), getResources().getStringArray(R.array.fast_locations), getResources().getString(R.string.fast_hospital_title), App.HORIZONTAL, App.HORIZONTAL);
        hospital = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_if_hospital_specify), getResources().getStringArray(R.array.fast_sites), getResources().getString(R.string.fast_ASHKHI), App.HORIZONTAL);
        hospitalSection = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_hospital_parts_title), getResources().getStringArray(R.array.fast_hospital_parts), getResources().getString(R.string.fast_opdclinicscreening_title), App.VERTICAL, App.VERTICAL);
        hospitalSectionOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        hospitalSectionOther.setVisibility(View.GONE);
        opdWardSection = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_clinic_and_ward_title), getResources().getStringArray(R.array.fast_clinic_and_ward_list), getResources().getString(R.string.fast_generalmedicinefilterclinic_title), App.VERTICAL);
        patientAttendant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_patient_or_attendant_title), getResources().getStringArray(R.array.fast_patient_or_attendant_list), getResources().getString(R.string.fast_patient_title), App.HORIZONTAL, App.HORIZONTAL);
        ageRange = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_age_range_title), getResources().getStringArray(R.array.fast_age_range_list), getResources().getString(R.string.fast_greater_title), App.HORIZONTAL, App.HORIZONTAL);
        gender = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_gender_title), getResources().getStringArray(R.array.fast_gender_list), getResources().getString(R.string.fast_male_title), App.HORIZONTAL, App.HORIZONTAL);
        coughTwoWeeks = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_cough_period_title), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        tbContact = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_close_with_someone_diagnosed), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        tbHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_tb_before), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), screeningLocation.getRadioGroup(), hospital.getSpinner(),
                hospitalSection.getRadioGroup(), hospitalSectionOther.getEditText(), opdWardSection.getSpinner(),
                patientAttendant.getRadioGroup(), ageRange.getRadioGroup(), gender.getRadioGroup(), coughTwoWeeks.getRadioGroup(),
                tbContact.getRadioGroup(), tbHistory.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, screeningLocation, hospital, hospitalSection, hospitalSectionOther, opdWardSection, patientAttendant, ageRange, gender},
                        {coughTwoWeeks, tbContact, tbHistory}};

        formDate.getButton().setOnClickListener(this);
        hospitalSection.getRadioGroup().setOnCheckedChangeListener(this);
        screeningLocation.getRadioGroup().setOnCheckedChangeListener(this);
        patientAttendant.getRadioGroup().setOnCheckedChangeListener(this);
        ageRange.getRadioGroup().setOnCheckedChangeListener(this);
        gender.getRadioGroup().setOnCheckedChangeListener(this);
        coughTwoWeeks.getRadioGroup().setOnCheckedChangeListener(this);
        tbHistory.getRadioGroup().setOnCheckedChangeListener(this);
        tbContact.getRadioGroup().setOnCheckedChangeListener(this);
    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
    }

    @Override
    public boolean validate() {
        Boolean error = false;

        if (hospitalSectionOther.getVisibility() == View.VISIBLE && App.get(hospitalSectionOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(0);
            hospitalSectionOther.getEditText().setError(getString(R.string.empty_field));
            hospitalSectionOther.getEditText().requestFocus();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == screeningLocation.getRadioGroup()) {
            if (screeningLocation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_hospital_title))) {
                hospital.setVisibility(View.VISIBLE);
                hospitalSection.setVisibility(View.VISIBLE);
                if (hospitalSection.getRadioGroup().getSelectedValue().
                        equals(getResources().getString(R.string.fast_opdclinicscreening_title))
                        || hospitalSection.getRadioGroup().getSelectedValue().
                        equals(getResources().getString(R.string.fast_wardscreening_title)))
                    opdWardSection.setVisibility(View.VISIBLE);
                if (hospitalSection.getRadioGroup()
                        .getSelectedValue().equals(getResources().getString(R.string.fast_other_title)))
                    hospitalSectionOther.setVisibility(View.VISIBLE);
            } else {
                hospital.setVisibility(View.GONE);
                hospitalSection.setVisibility(View.GONE);
                opdWardSection.setVisibility(View.GONE);
                hospitalSectionOther.setVisibility(View.GONE);
            }
        } else if (radioGroup == hospitalSection.getRadioGroup()) {
            if (hospitalSection.getRadioGroup()
                    .getSelectedValue().equals(getResources().getString(R.string.fast_other_title))) {
                hospitalSectionOther.setVisibility(View.VISIBLE);
                opdWardSection.setVisibility(View.GONE);
            } else if (hospitalSection.getRadioGroup().getSelectedValue().
                    equals(getResources().getString(R.string.fast_opdclinicscreening_title))
                    || hospitalSection.getRadioGroup().getSelectedValue().
                    equals(getResources().getString(R.string.fast_wardscreening_title))) {
                hospitalSectionOther.setVisibility(View.GONE);
                opdWardSection.setVisibility(View.VISIBLE);
            } else if (hospitalSection.getRadioGroup()
                    .getSelectedValue().equals(getResources().getString(R.string.fast_registrationdesk_title)) ||
                    hospitalSection.getRadioGroup().getSelectedValue()
                            .equals(getResources().getString(R.string.fast_nexttoxrayvan_title))) {
                hospitalSectionOther.setVisibility(View.GONE);
                opdWardSection.setVisibility(View.GONE);
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