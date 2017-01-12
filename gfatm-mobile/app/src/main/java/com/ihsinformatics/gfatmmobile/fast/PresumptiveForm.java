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
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
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

public class PresumptiveForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledRadioGroup patient_attendant;
    TitledSpinner patient_consultation;
    TitledEditText patient_consultation_other;
    MyTextView patient_symptom_title;
    MyTextView patient_demographics_title;
    TitledRadioGroup cough;
    TitledSpinner cough_duration;
    TitledRadioGroup productive_cough;
    TitledRadioGroup haemoptysis;
    TitledRadioGroup fever;
    TitledSpinner fever_duration;
    TitledRadioGroup night_sweats;
    TitledRadioGroup weight_loss;
    MyTextView patient_tbhistory_title;
    TitledRadioGroup tb_contact;
    TitledRadioGroup tb_history;


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
        patient_consultation_other.setVisibility(View.GONE);

        patient_symptom_title = new MyTextView(context, getResources().getString(R.string.fast_symptoms_title));
        patient_symptom_title.setTypeface(null, Typeface.BOLD);
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_do_you_have_cough), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        cough_duration = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_duration_of_coughs), getResources().getStringArray(R.array.fast_duration_list), getResources().getString(R.string.fast_less_than_2_weeks_title), App.VERTICAL);
        productive_cough = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_is_your_cough_productive), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        haemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_sputum_in_blood), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_do_you_have_fever), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        fever_duration = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_how_long_you_have_fever), getResources().getStringArray(R.array.fast_duration_list), getResources().getString(R.string.fast_less_than_2_weeks_title), App.VERTICAL);
        fever_duration.setVisibility(View.GONE);
        night_sweats = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_do_you_have_night_sweats), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        weight_loss = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_do_you_have_unexplained_weight_loss), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);

        patient_tbhistory_title = new MyTextView(context, getResources().getString(R.string.fast_tbhistory_title));
        patient_tbhistory_title.setTypeface(null, Typeface.BOLD);
        tb_history = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_tb_before), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        tb_contact = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_close_with_someone_diagnosed), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);




        // Used for reset fields...
        views = new View[]{formDate.getButton(), patient_attendant.getRadioGroup(), patient_consultation.getSpinner(),
                patient_consultation_other.getEditText(), cough.getRadioGroup(), cough_duration.getSpinner(),
                productive_cough.getRadioGroup(), haemoptysis.getRadioGroup(), fever.getRadioGroup(), fever_duration.getSpinner(),
                tb_contact.getRadioGroup(), tb_history.getRadioGroup(), night_sweats.getRadioGroup(), weight_loss.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, patient_demographics_title, patient_attendant, patient_consultation, patient_consultation_other},
                        {patient_symptom_title, cough, cough_duration, productive_cough, haemoptysis, fever, fever_duration, night_sweats, weight_loss},
                        {patient_tbhistory_title, tb_history, tb_contact}};

        patient_attendant.getRadioGroup().setOnCheckedChangeListener(this);
        patient_consultation.getSpinner().setOnItemSelectedListener(this);
        cough.getRadioGroup().setOnCheckedChangeListener(this);
        productive_cough.getRadioGroup().setOnCheckedChangeListener(this);
        cough_duration.getSpinner().setOnItemSelectedListener(this);
        formDate.getButton().setOnClickListener(this);
        patient_attendant.getRadioGroup().setOnCheckedChangeListener(this);
        fever.getRadioGroup().setOnCheckedChangeListener(this);
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

        if (patient_consultation_other.getVisibility() == View.VISIBLE && App.get(patient_consultation_other).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(0);
            patient_consultation_other.getEditText().setError(getString(R.string.empty_field));
            patient_consultation_other.getEditText().requestFocus();
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
        MySpinner spinner = (MySpinner) parent;
        if (spinner == patient_consultation.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                patient_consultation_other.setVisibility(View.VISIBLE);
            } else {
                patient_consultation_other.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == patient_attendant.getRadioGroup()) {
            if (patient_attendant.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_attendant_title))) {
                patient_consultation.setVisibility(View.GONE);
            } else {
                patient_consultation.setVisibility(View.VISIBLE);
            }
        } else if (radioGroup == cough.getRadioGroup()) {
            if (cough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                cough_duration.setVisibility(View.VISIBLE);
                productive_cough.setVisibility(View.VISIBLE);
                if (productive_cough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                    haemoptysis.setVisibility(View.VISIBLE);
                } else {
                    haemoptysis.setVisibility(View.GONE);
                }
            } else {
                cough_duration.setVisibility(View.GONE);
                productive_cough.setVisibility(View.GONE);
                haemoptysis.setVisibility(View.GONE);
            }
        } else if (radioGroup == productive_cough.getRadioGroup()) {
            if (productive_cough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title)) && cough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                haemoptysis.setVisibility(View.VISIBLE);
            } else {
                haemoptysis.setVisibility(View.GONE);
            }
        } else if (radioGroup == fever.getRadioGroup()) {
            if (fever.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                fever_duration.setVisibility(View.VISIBLE);
            } else {
                fever_duration.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
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
