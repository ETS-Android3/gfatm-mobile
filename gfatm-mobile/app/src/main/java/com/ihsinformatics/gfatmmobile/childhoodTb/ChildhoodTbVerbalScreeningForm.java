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
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyCheckBox;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
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

public class ChildhoodTbVerbalScreeningForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledRadioGroup screeningLocation;
    TitledSpinner hospital;
    TitledSpinner facility_section;
    TitledEditText facility_section_other;
    TitledSpinner opd_ward_section;
    TitledEditText mother_name;
    TitledEditText father_name;
    TitledRadioGroup patient_attendant;
    TitledRadioGroup cough;
    TitledSpinner cough_duration;
    TitledRadioGroup fever;
    TitledRadioGroup night_sweats;
    TitledRadioGroup weight_loss;
    TitledRadioGroup appeptite;
    TitledRadioGroup lymphnode_swelling;
    TitledRadioGroup joint_swelling_2weeks;
    TitledRadioGroup tb_history;
    TitledRadioGroup tb_medication;
    TitledRadioGroup contact_tb_history_2years;
    TitledCheckBoxes close_contact_type;
    TitledEditText other_contact_type;
    TitledRadioGroup presumptive_tb;

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
        FORM_NAME = Forms.CHILDHOODTB_VERBAL_SCREENING;
        FORM = Forms.childhoodTb_verbalScreeningForm;

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
        screeningLocation = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_screening_location), getResources().getStringArray(R.array.ctb_screening_location_list), null, App.HORIZONTAL, App.VERTICAL, true);
        hospital = new TitledSpinner(context, null, getResources().getString(R.string.ctb_hospital_specify), getResources().getStringArray(R.array.ctb_hospital_list), null, App.VERTICAL);
        facility_section = new TitledSpinner(context, null, getResources().getString(R.string.ctb_facility_section), getResources().getStringArray(R.array.ctb_facility_section_list), null, App.VERTICAL);
        facility_section_other = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        opd_ward_section = new TitledSpinner(context, null, getResources().getString(R.string.ctb_opd_clinic_or_ward), getResources().getStringArray(R.array.ctb_opd_ward_section_list), null, App.VERTICAL);
        mother_name = new TitledEditText(context, null, getResources().getString(R.string.ctb_mother_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        father_name = new TitledEditText(context, null, getResources().getString(R.string.ctb_father_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        patient_attendant = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_patient_attendant), getResources().getStringArray(R.array.ctb_patient_attendant_list), getResources().getString(R.string.ctb_patient), App.HORIZONTAL, App.VERTICAL, true);
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_cough), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        cough_duration = new TitledSpinner(context, null, getResources().getString(R.string.ctb_cough_duration), getResources().getStringArray(R.array.ctb_cough_duration_list), getResources().getString(R.string.ctb_less_than_2_weeks), App.VERTICAL, true);
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_fever_more_than_2_weeks), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        night_sweats = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_night_sweats), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.ctb_no), App.HORIZONTAL, App.VERTICAL, true);
        weight_loss = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_weight_loss), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.ctb_no), App.HORIZONTAL, App.VERTICAL, true);
        appeptite = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_child_appetite), getResources().getStringArray(R.array.ctb_appetite_list), getResources().getString(R.string.ctb_ok), App.HORIZONTAL, App.VERTICAL, true);
        lymphnode_swelling = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_lymph_node_swelling), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.ctb_no), App.HORIZONTAL, App.VERTICAL, true);
        joint_swelling_2weeks = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_joint_spine_swelling), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.ctb_no), App.HORIZONTAL, App.VERTICAL, true);
        tb_history = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_before), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.ctb_no), App.HORIZONTAL, App.VERTICAL, true);
        tb_medication = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_medication), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.ctb_no), App.HORIZONTAL, App.VERTICAL, true);
        contact_tb_history_2years = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_history_2years), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.ctb_no), App.HORIZONTAL, App.VERTICAL, true);
        close_contact_type = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_close_contact_type), getResources().getStringArray(R.array.ctb_close_contact_type_list), null, App.VERTICAL, App.VERTICAL);
        other_contact_type = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_contact), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        presumptive_tb = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_presumptive_tb), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);


        views = new View[]{formDate.getButton(), screeningLocation.getRadioGroup(), hospital.getSpinner(), facility_section.getSpinner(), facility_section_other.getEditText(), opd_ward_section.getSpinner(), mother_name.getEditText(), father_name.getEditText(), patient_attendant.getRadioGroup(),
                cough.getRadioGroup(), cough_duration.getSpinner(), fever.getRadioGroup(), night_sweats.getRadioGroup(), weight_loss.getRadioGroup(), appeptite.getRadioGroup(),
                lymphnode_swelling.getRadioGroup(), joint_swelling_2weeks.getRadioGroup(), tb_history.getRadioGroup(), tb_medication.getRadioGroup(), contact_tb_history_2years.getRadioGroup(),
                close_contact_type, other_contact_type.getEditText(), presumptive_tb.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, screeningLocation, hospital, facility_section, facility_section_other, opd_ward_section, mother_name, father_name,
                        patient_attendant, cough, cough_duration, fever, night_sweats, weight_loss, appeptite, lymphnode_swelling, joint_swelling_2weeks, tb_history,
                        tb_medication, contact_tb_history_2years, close_contact_type, other_contact_type, presumptive_tb}};

        formDate.getButton().setOnClickListener(this);
        screeningLocation.getRadioGroup().setOnCheckedChangeListener(this);
        hospital.getSpinner().setOnItemSelectedListener(this);
        facility_section.getSpinner().setOnItemSelectedListener(this);
        opd_ward_section.getSpinner().setOnItemSelectedListener(this);
        patient_attendant.getRadioGroup().setOnCheckedChangeListener(this);
        cough.getRadioGroup().setOnCheckedChangeListener(this);
        cough_duration.getSpinner().setOnItemSelectedListener(this);
        fever.getRadioGroup().setOnCheckedChangeListener(this);
        night_sweats.getRadioGroup().setOnCheckedChangeListener(this);
        weight_loss.getRadioGroup().setOnCheckedChangeListener(this);
        appeptite.getRadioGroup().setOnCheckedChangeListener(this);
        lymphnode_swelling.getRadioGroup().setOnCheckedChangeListener(this);
        joint_swelling_2weeks.getRadioGroup().setOnCheckedChangeListener(this);
        tb_history.getRadioGroup().setOnCheckedChangeListener(this);
        tb_medication.getRadioGroup().setOnCheckedChangeListener(this);
        contact_tb_history_2years.getRadioGroup().setOnCheckedChangeListener(this);
        presumptive_tb.getRadioGroup().setOnCheckedChangeListener(this);

        ArrayList<MyCheckBox> checkBoxList = close_contact_type.getCheckedBoxes();
        for (CheckBox cb : close_contact_type.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

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
        boolean error = false;

        if (other_contact_type.getVisibility() == View.VISIBLE && App.get(other_contact_type).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            other_contact_type.getEditText().setError(getString(R.string.empty_field));
            other_contact_type.getEditText().requestFocus();
            error = true;
        }
        if (facility_section_other.getVisibility() == View.VISIBLE && App.get(facility_section_other).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            facility_section_other.getEditText().setError(getString(R.string.empty_field));
            facility_section_other.getEditText().requestFocus();
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
        if (spinner == facility_section.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                facility_section_other.setVisibility(View.VISIBLE);
            } else {
                facility_section_other.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_opd_clinic)) || parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_ward))) {
                opd_ward_section.setVisibility(View.VISIBLE);
            } else {
                opd_ward_section.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        for (CheckBox cb : close_contact_type.getCheckedBoxes()) {
            if (App.get(cb).equals(getResources().getString(R.string.ctb_mother))) {
                if (cb.isChecked()) {
                    for (RadioButton rb : presumptive_tb.getRadioGroup().getButtons()) {
                        String str = rb.getText().toString();
                        if (str.equals(getResources().getString(R.string.yes)))
                            rb.setChecked(true);
                        else
                            rb.setChecked(false);
                    }
                }
            }
            if (App.get(cb).equals(getResources().getString(R.string.ctb_other_title))) {
                if (cb.isChecked()) {
                    other_contact_type.setVisibility(View.VISIBLE);
                } else {
                    other_contact_type.setVisibility(View.GONE);
                }
            }

        }
    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        hospital.setVisibility(View.GONE);
        facility_section.setVisibility(View.GONE);
        facility_section_other.setVisibility(View.GONE);
        opd_ward_section.setVisibility(View.GONE);
        cough_duration.setVisibility(View.GONE);
        tb_medication.setVisibility(View.GONE);
        close_contact_type.setVisibility(View.GONE);
        other_contact_type.setVisibility(View.GONE);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == screeningLocation.getRadioGroup()) {
            if (screeningLocation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_hospital))) {
                hospital.setVisibility(View.VISIBLE);
                facility_section.setVisibility(View.VISIBLE);
            } else {
                hospital.setVisibility(View.GONE);
                facility_section.setVisibility(View.GONE);
            }
        } else if (group == cough.getRadioGroup()) {
            if (cough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                cough_duration.setVisibility(View.VISIBLE);
            } else {
                cough_duration.setVisibility(View.GONE);
            }
        } else if (group == lymphnode_swelling.getRadioGroup()) {
            if (App.get(lymphnode_swelling).equals(getResources().getString(R.string.yes))) {
                for (RadioButton rb : presumptive_tb.getRadioGroup().getButtons()) {
                    String str = rb.getText().toString();
                    if (str.equals(getResources().getString(R.string.yes)))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
            }
        } else if (group == joint_swelling_2weeks.getRadioGroup()) {
            if (App.get(joint_swelling_2weeks).equals(getResources().getString(R.string.yes))) {
                for (RadioButton rb : presumptive_tb.getRadioGroup().getButtons()) {
                    String str = rb.getText().toString();
                    if (str.equals(getResources().getString(R.string.yes)))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
            }
        } else if (group == tb_history.getRadioGroup()) {
            if (App.get(tb_history).equals(getResources().getString(R.string.yes))) {
                tb_medication.setVisibility(View.VISIBLE);
            } else {
                tb_medication.setVisibility(View.GONE);
            }
        } else if (group == contact_tb_history_2years.getRadioGroup()) {
            if (App.get(contact_tb_history_2years).equals(getResources().getString(R.string.yes))) {
                close_contact_type.setVisibility(View.VISIBLE);
            } else {
                close_contact_type.setVisibility(View.GONE);
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
