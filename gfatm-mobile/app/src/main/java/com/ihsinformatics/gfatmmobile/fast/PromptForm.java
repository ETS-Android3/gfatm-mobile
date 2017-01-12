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
 * Created by Haris on 1/12/2017.
 */

public class PromptForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    // Views...
    TitledButton formDate;
    MyTextView testing_of_presumptive_patients_title;
    TitledRadioGroup sputum_container_given;
    TitledRadioGroup sputum_sample;
    TitledRadioGroup reason_nosputum_sample;

    TitledRadioGroup free_xray_voucher;
    TitledRadioGroup no_xray_voucher;


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
        testing_of_presumptive_patients_title = new MyTextView(context, getResources().getString(R.string.fast_testing_of_presumptive_patients));
        testing_of_presumptive_patients_title.setTypeface(null, Typeface.BOLD);
        sputum_container_given = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_patient_a_sputum_container), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        sputum_sample = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_produced_a_sputum_sample), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        reason_nosputum_sample = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_if_no_why_not), getResources().getStringArray(R.array.fast_if_no_why_not_list), getResources().getString(R.string.fast_patient_unable_to_expectorate), App.VERTICAL, App.VERTICAL);
        reason_nosputum_sample.setVisibility(View.GONE);

        free_xray_voucher = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_given_free_chest_xray), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        no_xray_voucher = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_not_given_free_chest_xray), getResources().getStringArray(R.array.fast_not_given_free_list), getResources().getString(R.string.fast_presumptive_refused), App.VERTICAL, App.VERTICAL);
        no_xray_voucher.setVisibility(View.GONE);



        // Used for reset fields...
        views = new View[]{formDate.getButton(), sputum_container_given.getRadioGroup(), sputum_sample.getRadioGroup(),
                reason_nosputum_sample.getRadioGroup(), free_xray_voucher.getRadioGroup(), no_xray_voucher.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, testing_of_presumptive_patients_title, sputum_container_given, sputum_sample, reason_nosputum_sample, free_xray_voucher, no_xray_voucher}};

        formDate.getButton().setOnClickListener(this);
        sputum_container_given.getRadioGroup().setOnCheckedChangeListener(this);
        sputum_sample.getRadioGroup().setOnCheckedChangeListener(this);
        reason_nosputum_sample.getRadioGroup().setOnCheckedChangeListener(this);
        free_xray_voucher.getRadioGroup().setOnCheckedChangeListener(this);
        no_xray_voucher.getRadioGroup().setOnCheckedChangeListener(this);
    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
    }

    @Override
    public boolean validate() {
        Boolean error = false;

      /*  if (hospital_section_other.getVisibility() == View.VISIBLE && App.get(hospital_section_other).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(0);
            hospital_section_other.getEditText().setError(getString(R.string.empty_field));
            hospital_section_other.getEditText().requestFocus();
            error = true;
        }*/

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
        if (radioGroup == sputum_container_given.getRadioGroup()) {
            if (sputum_container_given.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                sputum_sample.setVisibility(View.VISIBLE);
                if (sputum_sample.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                    reason_nosputum_sample.setVisibility(View.VISIBLE);
                }
            } else {
                sputum_sample.setVisibility(View.GONE);
                reason_nosputum_sample.setVisibility(View.GONE);
            }
        } else if (radioGroup == sputum_sample.getRadioGroup()) {
            if (sputum_sample.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title)) && sputum_container_given.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                reason_nosputum_sample.setVisibility(View.GONE);
            } else {
                reason_nosputum_sample.setVisibility(View.VISIBLE);
            }
        } else if (radioGroup == free_xray_voucher.getRadioGroup()) {
            if (free_xray_voucher.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                no_xray_voucher.setVisibility(View.GONE);
            } else {
                no_xray_voucher.setVisibility(View.VISIBLE);
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
