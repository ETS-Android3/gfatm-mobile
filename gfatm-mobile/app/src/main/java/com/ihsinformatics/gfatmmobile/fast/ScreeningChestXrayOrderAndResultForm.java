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
 * Created by Haris on 1/24/2017.
 */

public class ScreeningChestXrayOrderAndResultForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    // Views...
    TitledButton formDate;
    MyTextView cxrResultTitle;
    MyTextView cxrOrderTitle;
    TitledRadioGroup screenXrayType;
    TitledRadioGroup formType;
    TitledSpinner monthOfTreatment;
    TitledButton testDate;
    TitledEditText testId;
    TitledEditText cat4tbScore;
    TitledSpinner screenXrayDiagnosis;
    TitledEditText screenXrayDiagnosisOther;
    TitledSpinner extentOfDisease;
    TitledEditText radiologistRemarks;


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
        FORM_NAME = Forms.FAST_SCREENING_CHEST_XRAY_ORDER_AND_RESULT_FORM;

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
        testId = new TitledEditText(context, null, getResources().getString(R.string.fast_test_id), "", "", 20, RegexUtil.NumericFilter, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_select_form_type), getResources().getStringArray(R.array.fast_order_and_result_list), "", App.VERTICAL, App.VERTICAL);
        cxrOrderTitle = new MyTextView(context, getResources().getString(R.string.fast_cxr_order_title));
        cxrOrderTitle.setTypeface(null, Typeface.BOLD);
        cxrOrderTitle.setVisibility(View.GONE);
        screenXrayType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_what_type_of_xray_is_this), getResources().getStringArray(R.array.fast_type_of_xray_is_this_list), getResources().getString(R.string.fast_chest_xray_other), App.VERTICAL, App.VERTICAL);
        screenXrayType.setVisibility(View.GONE);
        monthOfTreatment = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_month_of_treatment), getResources().getStringArray(R.array.fast_number_list), getResources().getString(R.string.fast_one), App.HORIZONTAL);
        monthOfTreatment.setVisibility(View.GONE);
        testDate = new TitledButton(context, null, getResources().getString(R.string.fast_test_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        testDate.setVisibility(View.GONE);
        cxrResultTitle = new MyTextView(context, getResources().getString(R.string.fast_cxr_result_title));
        cxrResultTitle.setTypeface(null, Typeface.BOLD);
        cxrResultTitle.setVisibility(View.GONE);
        cat4tbScore = new TitledEditText(context, null, getResources().getString(R.string.fast_chest_xray_cad4tb_score), "", "", 3, RegexUtil.NumericFilter, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);
        cat4tbScore.setVisibility(View.GONE);
        screenXrayDiagnosis = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_radiologica_diagnosis), getResources().getStringArray(R.array.fast_radiologists_diagnosis_list), getResources().getString(R.string.fast_adenopathy), App.VERTICAL);
        screenXrayDiagnosis.setVisibility(View.GONE);
        screenXrayDiagnosisOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        screenXrayDiagnosisOther.setVisibility(View.GONE);
        extentOfDisease = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_extent_of_desease), getResources().getStringArray(R.array.fast_extent_of_disease_list), getResources().getString(R.string.fast_normal), App.VERTICAL);
        extentOfDisease.setVisibility(View.GONE);
        radiologistRemarks = new TitledEditText(context, null, getResources().getString(R.string.fast_radiologist_remarks), "", "", 500, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        radiologistRemarks.setVisibility(View.GONE);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), testId.getEditText(), formType.getRadioGroup(), screenXrayType.getRadioGroup(),
                monthOfTreatment.getSpinner(), testDate.getButton(), cat4tbScore.getEditText(), screenXrayDiagnosis.getSpinner(),
                screenXrayDiagnosisOther.getEditText(), extentOfDisease.getSpinner(), radiologistRemarks.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, testId, formType, cxrOrderTitle, screenXrayType, monthOfTreatment, testDate, cxrResultTitle, cat4tbScore,
                        screenXrayDiagnosis, screenXrayDiagnosisOther, extentOfDisease, radiologistRemarks}};

        formDate.getButton().setOnClickListener(this);
        testDate.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        screenXrayDiagnosis.getSpinner().setOnItemSelectedListener(this);
    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        testDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());

    }

    @Override
    public boolean validate() {
        Boolean error = false;

        if (testId.getVisibility() == View.VISIBLE && App.get(testId).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            testId.getEditText().setError(getString(R.string.empty_field));
            testId.getEditText().requestFocus();
            error = true;
        }

        if (cat4tbScore.getVisibility() == View.VISIBLE && App.get(cat4tbScore).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cat4tbScore.getEditText().setError(getString(R.string.empty_field));
            cat4tbScore.getEditText().requestFocus();
            error = true;
        }

        if (screenXrayDiagnosisOther.getVisibility() == View.VISIBLE && App.get(screenXrayDiagnosisOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            screenXrayDiagnosisOther.getEditText().setError(getString(R.string.empty_field));
            screenXrayDiagnosisOther.getEditText().requestFocus();
            error = true;
        }

        if (radiologistRemarks.getVisibility() == View.VISIBLE && App.get(radiologistRemarks).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            radiologistRemarks.getEditText().setError(getString(R.string.empty_field));
            radiologistRemarks.getEditText().requestFocus();
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

        if (view == testDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
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

        if (spinner == screenXrayDiagnosis.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_others))) {
                screenXrayDiagnosisOther.setVisibility(View.VISIBLE);
            } else {
                screenXrayDiagnosisOther.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == formType.getRadioGroup()) {
            if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))) {
                cxrOrderTitle.setVisibility(View.VISIBLE);
                screenXrayType.setVisibility(View.VISIBLE);
                monthOfTreatment.setVisibility(View.VISIBLE);
                testDate.setVisibility(View.VISIBLE);

                cxrResultTitle.setVisibility(View.GONE);
                cat4tbScore.setVisibility(View.GONE);
                screenXrayDiagnosis.setVisibility(View.GONE);
                screenXrayDiagnosisOther.setVisibility(View.GONE);
                extentOfDisease.setVisibility(View.GONE);
                radiologistRemarks.setVisibility(View.GONE);
            } else {
                cxrOrderTitle.setVisibility(View.GONE);
                screenXrayType.setVisibility(View.GONE);
                monthOfTreatment.setVisibility(View.GONE);
                testDate.setVisibility(View.GONE);

                cxrResultTitle.setVisibility(View.VISIBLE);
                cat4tbScore.setVisibility(View.VISIBLE);
                screenXrayDiagnosis.setVisibility(View.VISIBLE);
                if (screenXrayDiagnosis.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_others))) {
                    screenXrayDiagnosisOther.setVisibility(View.VISIBLE);
                }
                extentOfDisease.setVisibility(View.VISIBLE);
                radiologistRemarks.setVisibility(View.VISIBLE);
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
