package com.ihsinformatics.gfatmmobile.comorbidities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Fawad Jawaid on 03-Jan-17.
 */

public class ComorbiditiesDiabetesTreatmentInitiationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    MyTextView previousHistory;
    TitledRadioGroup diabetesFamilyHistory;
    TitledCheckBoxes diabetesFamilyHistorySpecify;
    TitledRadioGroup previousDiabetesTreatmentHistory;
    TitledRadioGroup selfReportedDiabetesTreatmentHistory;
    TitledRadioGroup typeDiabetes;
    TitledRadioGroup statusDiabetes;
    TitledRadioGroup hxHypoglycemia;
    TitledRadioGroup hxDiabeticRetinopathy;
    TitledRadioGroup hxDiabeticNeuropathy;
    TitledRadioGroup hxDiabeticNephropathy;
    TitledCheckBoxes hxDiabeticInfections;
    TitledRadioGroup hxDiabeticPvd;
    TitledRadioGroup hxDiabeticCad;
    TitledRadioGroup hxDiabeticHypertension;
    TitledRadioGroup hxDiabeticGestationalDiabetes;
    TitledRadioGroup hxDiabeticBirth;

    MyTextView treatmentInitiation;
    TitledCheckBoxes diabetesTreatmentInitiation;
    TitledEditText diabetesTreatmentDetail;
    TitledRadioGroup diabetesTreatmentInitiationMetformin;
    TitledEditText diabetesTreatmentInitiationInsulinN;
    TitledEditText diabetesTreatmentInitiationInsulinR;

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

        PAGE_COUNT = 7;
        FORM_NAME = Forms.COMORBIDITIES_DIABETES_TREATMENT_INITIATION;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesDiabetesTreatmentInitiationForm.MyAdapter());
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
        previousHistory = new MyTextView(context, getResources().getString(R.string.comorbidities_diabetes_previous_history));
        previousHistory.setTypeface(null, Typeface.BOLD);
        diabetesFamilyHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_diabetes_family_history), getResources().getStringArray(R.array.comorbidities_diabetes_family_history_options), "", App.HORIZONTAL, App.VERTICAL);
        diabetesFamilyHistorySpecify = new TitledCheckBoxes(context, null, getResources().getString(R.string.comorbidities_education_form_educational_plan_text), getResources().getStringArray(R.array.comorbidities_education_form_educational_plan_text_options), new Boolean[]{true, false, false, false, false}, App.VERTICAL, App.VERTICAL);
        previousDiabetesTreatmentHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_previous_diabetes_treatment_history), getResources().getStringArray(R.array.comorbidities_yes_no), "", App.HORIZONTAL, App.VERTICAL);
        showDiabetesFamilyHistorySpecify();
        selfReportedDiabetesTreatmentHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_self_reported_diabetes_treatment_history), getResources().getStringArray(R.array.comorbidities_self_reported_diabetes_treatment_history_options), getResources().getString(R.string.comorbidities_self_reported_diabetes_treatment_history_both), App.HORIZONTAL, App.VERTICAL);
        showSelfReportedDiabetesTreatmentHistory();
        typeDiabetes = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_type_diabetes), getResources().getStringArray(R.array.comorbidities_type_diabetes_types), getResources().getString(R.string.comorbidities_type_diabetes_type2), App.HORIZONTAL, App.VERTICAL);
        statusDiabetes = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_status_diabetes), getResources().getStringArray(R.array.comorbidities_status_diabetes_options), getResources().getString(R.string.comorbidities_status_diabetes_uncontrolled), App.HORIZONTAL, App.VERTICAL);
        hxHypoglycemia = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_hypoglycemia), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), "", App.HORIZONTAL, App.VERTICAL);
        hxDiabeticRetinopathy = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_retinopathy), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), "", App.HORIZONTAL, App.VERTICAL);
        hxDiabeticNeuropathy = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_neuropathy), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), "", App.HORIZONTAL, App.VERTICAL);
        hxDiabeticNephropathy = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_nephropathy), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), "", App.HORIZONTAL, App.VERTICAL);
        hxDiabeticInfections = new TitledCheckBoxes(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_infections), getResources().getStringArray(R.array.comorbidities_hx_diabetic_infections_options), new Boolean[]{true, false, false, false, false, false, false, false}, App.VERTICAL, App.VERTICAL);
        hxDiabeticPvd = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_pvd), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), "", App.HORIZONTAL, App.VERTICAL);
        hxDiabeticCad = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_cad), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), "", App.HORIZONTAL, App.VERTICAL);
        hxDiabeticHypertension = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_hypertension), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), "", App.HORIZONTAL, App.VERTICAL);
        hxDiabeticGestationalDiabetes = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_gestational_diabetes), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), "", App.HORIZONTAL, App.VERTICAL);
        hxDiabeticBirth = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hx_diabetic_birth), getResources().getStringArray(R.array.comorbidities_hx_diabetic_cemia_pathies_options), "", App.HORIZONTAL, App.VERTICAL);
        showHxDiabeticBirth();

        treatmentInitiation = new MyTextView(context, getResources().getString(R.string.comorbidities_treatment_initiation));
        treatmentInitiation.setTypeface(null, Typeface.BOLD);
        diabetesTreatmentInitiation = new TitledCheckBoxes(context, null, getResources().getString(R.string.comorbidities_diabetes_treatment_initiation), getResources().getStringArray(R.array.comorbidities_diabetes_treatment_initiation_options), new Boolean[]{true, false, false, false, false, false, false}, App.VERTICAL, App.VERTICAL);
        diabetesTreatmentDetail = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_treatment_detail), "", "", 1000, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        diabetesTreatmentInitiationMetformin = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin), getResources().getStringArray(R.array.comorbidities_diabetes_treatment_initiation_metformin_options), getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_metformin_500), App.HORIZONTAL, App.VERTICAL);
        diabetesTreatmentInitiationInsulinN = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulinN), "", "", 100, RegexUtil.NumericFilter, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        diabetesTreatmentInitiationInsulinR = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_diabetes_treatment_initiation_insulinR), "", "", 100, RegexUtil.NumericFilter, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), diabetesFamilyHistory.getRadioGroup(), diabetesFamilyHistorySpecify,
                previousDiabetesTreatmentHistory.getRadioGroup(), selfReportedDiabetesTreatmentHistory.getRadioGroup(), typeDiabetes.getRadioGroup(), statusDiabetes.getRadioGroup(),
                hxHypoglycemia.getRadioGroup(), hxDiabeticRetinopathy.getRadioGroup(), hxDiabeticNeuropathy.getRadioGroup(), hxDiabeticNephropathy.getRadioGroup(),
                hxDiabeticInfections, hxDiabeticPvd.getRadioGroup(),
                hxDiabeticCad.getRadioGroup(), hxDiabeticHypertension.getRadioGroup(), hxDiabeticGestationalDiabetes.getRadioGroup(), hxDiabeticBirth.getRadioGroup(),
                treatmentInitiation, diabetesTreatmentInitiation, diabetesTreatmentDetail.getEditText(),
                diabetesTreatmentInitiationMetformin.getRadioGroup(), diabetesTreatmentInitiationInsulinN.getEditText(), diabetesTreatmentInitiationInsulinR.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, previousHistory, diabetesFamilyHistory, diabetesFamilyHistorySpecify},
                        {previousDiabetesTreatmentHistory, selfReportedDiabetesTreatmentHistory, typeDiabetes, statusDiabetes},
                        {hxHypoglycemia, hxDiabeticRetinopathy, hxDiabeticNeuropathy, hxDiabeticNephropathy},
                        {hxDiabeticInfections, hxDiabeticPvd},
                        {hxDiabeticCad, hxDiabeticHypertension, hxDiabeticGestationalDiabetes, hxDiabeticBirth},
                        {treatmentInitiation, diabetesTreatmentInitiation, diabetesTreatmentDetail},
                        {diabetesTreatmentInitiationMetformin, diabetesTreatmentInitiationInsulinN, diabetesTreatmentInitiationInsulinR}};

        formDate.getButton().setOnClickListener(this);
        diabetesFamilyHistory.getRadioGroup().setOnCheckedChangeListener(this);
        previousDiabetesTreatmentHistory.getRadioGroup().setOnCheckedChangeListener(this);
        hxDiabeticGestationalDiabetes.getRadioGroup().setOnCheckedChangeListener(this);
    }

    @Override
    public void updateDisplay() {

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
    }

    @Override
    public boolean validate() {

        Boolean error = false;

        if (App.get(diabetesTreatmentInitiationInsulinR).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(6);
            diabetesTreatmentInitiationInsulinR.getEditText().setError(getString(R.string.empty_field));
            diabetesTreatmentInitiationInsulinR.getEditText().requestFocus();
            error = true;
        }

        if (App.get(diabetesTreatmentInitiationInsulinN).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(6);
            diabetesTreatmentInitiationInsulinN.getEditText().setError(getString(R.string.empty_field));
            diabetesTreatmentInitiationInsulinN.getEditText().requestFocus();
            error = true;
        }

        if (App.get(diabetesTreatmentDetail ).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(5);
            diabetesTreatmentDetail.getEditText().setError(getString(R.string.empty_field));
            diabetesTreatmentDetail.getEditText().requestFocus();
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

        serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == diabetesFamilyHistory.getRadioGroup()) {
            showDiabetesFamilyHistorySpecify();
        }

        if (radioGroup ==  previousDiabetesTreatmentHistory.getRadioGroup()) {
            showSelfReportedDiabetesTreatmentHistory();
        }

        if(hxDiabeticGestationalDiabetes.getVisibility() == View.VISIBLE) {
            if (radioGroup == hxDiabeticGestationalDiabetes.getRadioGroup()) {
                showHxDiabeticBirth();
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

    void showDiabetesFamilyHistorySpecify() {
        if (diabetesFamilyHistory.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_diabetes_family_history_yes))) {
            diabetesFamilyHistorySpecify.setVisibility(View.VISIBLE);
        } else {
            diabetesFamilyHistorySpecify.setVisibility(View.GONE);
        }
    }

    void showSelfReportedDiabetesTreatmentHistory(){
        if (previousDiabetesTreatmentHistory.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.yes))) {
            selfReportedDiabetesTreatmentHistory.setVisibility(View.VISIBLE);
        } else {
            selfReportedDiabetesTreatmentHistory.setVisibility(View.GONE);
        }
    }

    void showHxDiabeticBirth() {
        if (hxDiabeticGestationalDiabetes.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_hx_diabetic_cemia_pathies_yes))) {
            hxDiabeticBirth.setVisibility(View.VISIBLE);
        } else {
            hxDiabeticBirth.setVisibility(View.GONE);
        }
    }
}


