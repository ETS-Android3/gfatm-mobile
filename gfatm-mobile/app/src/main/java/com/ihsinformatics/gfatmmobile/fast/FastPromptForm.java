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
import com.ihsinformatics.gfatmmobile.shared.Forms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Haris on 1/12/2017.
 */

public class FastPromptForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    // Views...
    TitledButton formDate;
    MyTextView testingOfPresumptivePatientsTitle;
    TitledEditText dueDateSample;
    TitledRadioGroup sputumContainerGiven;
    TitledRadioGroup sputum_sample;
    TitledRadioGroup reasonNoSputumSample;
    TitledRadioGroup freeXrayVoucher;
    TitledRadioGroup noXrayVoucher;


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
        FORM_NAME = Forms.FAST_PROMPT_FORM;

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
        testingOfPresumptivePatientsTitle = new MyTextView(context, getResources().getString(R.string.fast_testing_of_presumptive_patients));
        testingOfPresumptivePatientsTitle.setTypeface(null, Typeface.BOLD);
        sputumContainerGiven = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_patient_a_sputum_container), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        sputum_sample = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_produced_a_sputum_sample), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        reasonNoSputumSample = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_if_no_why_not), getResources().getStringArray(R.array.fast_if_no_why_not_list), getResources().getString(R.string.fast_patient_unable_to_expectorate), App.VERTICAL, App.VERTICAL);
        reasonNoSputumSample.setVisibility(View.GONE);
        dueDateSample = new TitledEditText(context, null, getResources().getString(R.string.fast_date_sputum_sample),getSputumDate(formDateCalendar), "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        dueDateSample.getEditText().setEnabled(false);
        freeXrayVoucher = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_given_free_chest_xray), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        noXrayVoucher = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_not_given_free_chest_xray), getResources().getStringArray(R.array.fast_not_given_free_list), getResources().getString(R.string.fast_presumptive_refused), App.VERTICAL, App.VERTICAL);
        noXrayVoucher.setVisibility(View.GONE);



        // Used for reset fields...
        views = new View[]{formDate.getButton(), sputumContainerGiven.getRadioGroup(), sputum_sample.getRadioGroup(),
                reasonNoSputumSample.getRadioGroup(), freeXrayVoucher.getRadioGroup(), noXrayVoucher.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, testingOfPresumptivePatientsTitle, sputumContainerGiven, sputum_sample, reasonNoSputumSample, dueDateSample, freeXrayVoucher, noXrayVoucher}};

        formDate.getButton().setOnClickListener(this);
        sputumContainerGiven.getRadioGroup().setOnCheckedChangeListener(this);
        sputum_sample.getRadioGroup().setOnCheckedChangeListener(this);
        reasonNoSputumSample.getRadioGroup().setOnCheckedChangeListener(this);
        freeXrayVoucher.getRadioGroup().setOnCheckedChangeListener(this);
        noXrayVoucher.getRadioGroup().setOnCheckedChangeListener(this);
    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        dueDateSample.getEditText().setText(getSputumDate(formDateCalendar));
    }

    private String getSputumDate(Calendar calendar){
        calendar.add(Calendar.DATE, 1);
        return DateFormat.format("dd-MMM-yyyy", calendar).toString();
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
        if (radioGroup == sputumContainerGiven.getRadioGroup()) {
            if (sputumContainerGiven.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                sputum_sample.setVisibility(View.VISIBLE);
                if (sputum_sample.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                    reasonNoSputumSample.setVisibility(View.VISIBLE);
                }
            } else {
                sputum_sample.setVisibility(View.GONE);
                reasonNoSputumSample.setVisibility(View.GONE);
            }
        } else if (radioGroup == sputum_sample.getRadioGroup()) {
            if (sputum_sample.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title)) && sputumContainerGiven.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                reasonNoSputumSample.setVisibility(View.GONE);
            } else {
                reasonNoSputumSample.setVisibility(View.VISIBLE);
            }
        } else if (radioGroup == freeXrayVoucher.getRadioGroup()) {
            if (freeXrayVoucher.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                noXrayVoucher.setVisibility(View.GONE);
            } else {
                noXrayVoucher.setVisibility(View.VISIBLE);
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
