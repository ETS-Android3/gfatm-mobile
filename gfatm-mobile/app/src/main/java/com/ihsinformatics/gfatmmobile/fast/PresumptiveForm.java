package com.ihsinformatics.gfatmmobile.fast;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
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
import android.widget.Spinner;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Haris on 1/5/2017.
 */

public class PresumptiveForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener{

    Context context;

    // Views...
    TitledButton formDate;
    TitledRadioGroup patient_attendant;
    TitledSpinner patient_consultation;
    TitledEditText patient_consultation_other;
    MyTextView patient_symptom_title;
    MyTextView patient_demographics_title;
    TitledRadioGroup productive_cough;
    TitledRadioGroup haemoptysis;
    TitledRadioGroup fever;
    TitledSpinner fever_duration;
    TitledRadioGroup night_sweats;
    TitledRadioGroup weight_loss;
    MyTextView patient_tbhistory_title;
    TitledRadioGroup tb_contact;
    TitledRadioGroup tb_history;


    TitledRadioGroup screening_location;
    TitledSpinner hospital;
    TitledRadioGroup hospital_section;
    TitledEditText hospital_section_other;
    TitledSpinner opd_ward_section;

    TitledRadioGroup age_range;
    TitledRadioGroup gender;
    TitledRadioGroup cough_twoweeks;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 3;
        FORM_NAME = Forms.FAST_PRESUMPTIVE_FORM;

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
        patient_demographics_title = new MyTextView(context, getResources().getString(R.string.fast_demographics_title));
        patient_demographics_title.setTypeface(null, Typeface.BOLD);
        patient_attendant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_is_this_patient_or_attendant), getResources().getStringArray(R.array.fast_patient_or_attendant_list), getResources().getString(R.string.fast_patient_title), App.VERTICAL, App.VERTICAL);
        patient_consultation = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_speciality_patient_consult), getResources().getStringArray(R.array.fast_patient_consultation_list), getResources().getString(R.string.fast_chesttbclinic_title), App.VERTICAL);
        patient_consultation_other = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        patient_symptom_title = new MyTextView(context, getResources().getString(R.string.fast_symptoms_title));
        patient_symptom_title.setTypeface(null, Typeface.BOLD);
        productive_cough = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_is_your_cough_productive), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        haemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_sputum_in_blood), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_do_you_have_fever), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        fever_duration = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_how_long_you_have_fever), getResources().getStringArray(R.array.fast_duration_list), getResources().getString(R.string.fast_less_than_2_weeks_title), App.VERTICAL);
        night_sweats = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_do_you_have_night_sweats), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        weight_loss = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_do_you_have_unexplained_weight_loss), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);


        patient_tbhistory_title = new MyTextView(context, getResources().getString(R.string.fast_tbhistory_title));
        patient_tbhistory_title.setTypeface(null, Typeface.BOLD);
        tb_history = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_tb_before), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        tb_contact = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_close_with_someone_diagnosed), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);


        screening_location = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_location_of_screening), getResources().getStringArray(R.array.fast_locations), getResources().getString(R.string.fast_hospital_title), App.HORIZONTAL, App.HORIZONTAL);
        hospital = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_if_hospital_specify), getResources().getStringArray(R.array.fast_sites), getResources().getString(R.string.fast_ASHKHI), App.HORIZONTAL);
        hospital_section = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_hospital_parts_title), getResources().getStringArray(R.array.fast_hospital_parts), getResources().getString(R.string.fast_opdclinicscreening_title), App.VERTICAL, App.VERTICAL);
        hospital_section_other = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        hospital_section_other.setVisibility(View.GONE);
        opd_ward_section = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_clinic_and_ward_title), getResources().getStringArray(R.array.fast_clinic_and_ward_list), getResources().getString(R.string.fast_generalmedicinefilterclinic_title), App.VERTICAL);
        age_range = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_age_range_title), getResources().getStringArray(R.array.fast_age_range_list), getResources().getString(R.string.fast_greater_title), App.HORIZONTAL, App.HORIZONTAL);
        gender = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_gender_title), getResources().getStringArray(R.array.fast_gender_list), getResources().getString(R.string.fast_male_title), App.HORIZONTAL, App.HORIZONTAL);
        cough_twoweeks = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_cough_period_title), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        tb_contact = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_close_with_someone_diagnosed), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        tb_history = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_tb_before), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), patient_attendant.getRadioGroup(), hospital.getSpinner(),
                hospital_section.getRadioGroup(), hospital_section_other.getEditText(), opd_ward_section.getSpinner(),
                patient_attendant.getRadioGroup(), age_range.getRadioGroup(), gender.getRadioGroup(), cough_twoweeks.getRadioGroup(),
                tb_contact.getRadioGroup(), tb_history.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate ,patient_demographics_title , patient_attendant, patient_consultation, patient_consultation_other},
                 {patient_symptom_title, productive_cough, haemoptysis, fever, fever_duration, night_sweats, weight_loss},
                 {patient_tbhistory_title, tb_history, tb_contact}};

        patient_attendant.getRadioGroup().setOnCheckedChangeListener(this);
        patient_consultation.getSpinner().setOnItemSelectedListener(this);
        formDate.getButton().setOnClickListener(this);
        hospital_section.getRadioGroup().setOnCheckedChangeListener(this);
        screening_location.getRadioGroup().setOnCheckedChangeListener(this);
        patient_attendant.getRadioGroup().setOnCheckedChangeListener(this);
        age_range.getRadioGroup().setOnCheckedChangeListener(this);
        gender.getRadioGroup().setOnCheckedChangeListener(this);
        cough_twoweeks.getRadioGroup().setOnCheckedChangeListener(this);
        tb_history.getRadioGroup().setOnCheckedChangeListener(this);
        tb_contact.getRadioGroup().setOnCheckedChangeListener(this);
    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
    }

    @Override
    public boolean validate() {
        Boolean error = false;

        if (hospital_section_other.getVisibility() == View.VISIBLE && App.get(hospital_section_other).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(0);
            hospital_section_other.getEditText().setError(getString(R.string.empty_field));
            hospital_section_other.getEditText().requestFocus();
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
        if (radioGroup == patient_attendant.getRadioGroup()) {
            if (patient_attendant.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_attendant_title))) {
                patient_consultation.setVisibility(View.GONE);
            } else {
                patient_consultation.setVisibility(View.VISIBLE);
            }
        } else if (radioGroup == hospital_section.getRadioGroup()) {
            if (hospital_section.getRadioGroup()
                    .getSelectedValue().equals(getResources().getString(R.string.fast_other_title))) {
                hospital_section_other.setVisibility(View.VISIBLE);
                opd_ward_section.setVisibility(View.GONE);
            } else if (hospital_section.getRadioGroup().getSelectedValue().
                    equals(getResources().getString(R.string.fast_opdclinicscreening_title))
                    || hospital_section.getRadioGroup().getSelectedValue().
                    equals(getResources().getString(R.string.fast_wardscreening_title))) {
                hospital_section_other.setVisibility(View.GONE);
                opd_ward_section.setVisibility(View.VISIBLE);
            } else if (hospital_section.getRadioGroup()
                    .getSelectedValue().equals(getResources().getString(R.string.fast_registrationdesk_title)) ||
                    hospital_section.getRadioGroup().getSelectedValue()
                            .equals(getResources().getString(R.string.fast_nexttoxrayvan_title))) {
                hospital_section_other.setVisibility(View.GONE);
                opd_ward_section.setVisibility(View.GONE);
            }
        }
    }

    //public void OnItemSelectedListener

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
