package com.ihsinformatics.gfatmmobile.pmdt;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Tahira on 3/17/2017.
 */

public class PmdtNutritionalSupportForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledEditText externalId;
    TitledSpinner typeAssessment;
    TitledEditText otherAssessmentReason;
    TitledEditText treatmentMonth;

    TitledSpinner treatmentFacility;
    TitledEditText otherTreatmentFacility;

    TitledEditText nationalDrTbRegistrationNumber;

    TitledRadioGroup isRegisteredOutstationFacility;

    TitledSpinner outstationFacility;


    TitledRadioGroup referredNutritionist;      // title: Nutritionist's verification
    TitledRadioGroup diabetic;
    TitledEditText height;
    TitledEditText weight;
    TitledEditText currentBmi;
    TitledEditText baselineBmi;
    TitledRadioGroup currentBmiCategory;
    TitledRadioGroup baselineBmiCategory;

    TitledRadioGroup eligibleNutritionalSupport;        //title: Nutritional support eligibility
    TitledSpinner nutritionalSupportType;
    TitledEditText otherNutritionalSupportType;
    TitledEditText nutritionalSupportVoucherNumber;

    ScrollView scrollView;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pageCount = 3;
        formName = Forms.PMDT_NUTRITIONAL_SUPPORT;
        form = Forms.pmdtNutritionalSupport;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(pageCount - 1);
        formNameView.setText(formName);

        initViews();

        groups = new ArrayList<ViewGroup>();

        if (App.isLanguageRTL()) {
            for (int i = pageCount - 1; i >= 0; i--) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(context);
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        } else {
            for (int i = 0; i < pageCount; i++) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(context);
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }
        return mainContent;
    }

    @Override
    public void initViews() {
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pmdt_visit_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        externalId = new TitledEditText(context, null, getResources().getString(R.string.pmdt_external_id), "", "", 11, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        typeAssessment = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_assessment_type), getResources().getStringArray(R.array.pmdt_types_of_assessment), getResources().getString(R.string.pmdt_baseline_assessment), App.HORIZONTAL, true);
        otherAssessmentReason = new TitledEditText(context, null, getResources().getString(R.string.pmdt_other_assessment_reason), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        treatmentMonth = new TitledEditText(context, null, getResources().getString(R.string.pmdt_treatment_month), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        // Fetching PMDT Locations
        String program = "";
        if (App.getProgram().equals(getResources().getString(R.string.pet)))
            program = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.fast)))
            program = "fast_location";
        else if (App.getProgram().equals(getResources().getString(R.string.comorbidities)))
            program = "comorbidities_location";
        else if (App.getProgram().equals(getResources().getString(R.string.pmdt)))
            program = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.childhood_tb)))
            program = "childhood_tb_location";

        final Object[][] locations = serverService.getAllLocationsFromLocalDB(program);
        String[] locationArray = new String[locations.length + 1];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][1]);
        }
        locationArray[locationArray.length - 1] = getResources().getString(R.string.pmdt_other);

        treatmentFacility = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_treatment_facility), locationArray, "", App.VERTICAL);
        otherTreatmentFacility = new TitledEditText(context, "", getResources().getString(R.string.pmdt_other_treatment_facility), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        nationalDrTbRegistrationNumber = new TitledEditText(context, null, getResources().getString(R.string.pmdt_national_dr_tb_registration_number), "", "", 25, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        // removing 'Other' option from the facility list
        locationArray = ArrayUtils.removeElement(locationArray, locationArray[locationArray.length - 1]);
        isRegisteredOutstationFacility = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_patient_registered_outstation), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        outstationFacility = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_registered_outstation_facility), locationArray, "", App.VERTICAL);

        referredNutritionist = new TitledRadioGroup(context, getResources().getString(R.string.pmdt_title_nutritionist_verification), getResources().getString(R.string.pmdt_referred_nutritionist), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        diabetic = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_patient_diabetic), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        height = new TitledEditText(context, null, getResources().getString(R.string.pmdt_height), "", "", 6, RegexUtil.FLOAT_FILTER, InputType.TYPE_NUMBER_FLAG_DECIMAL, App.HORIZONTAL, true);
        weight = new TitledEditText(context, null, getResources().getString(R.string.pmdt_weight), "", "", 6, RegexUtil.FLOAT_FILTER, InputType.TYPE_NUMBER_FLAG_DECIMAL, App.HORIZONTAL, true);
        currentBmi = new TitledEditText(context, null, getResources().getString(R.string.pmdt_current_bmi), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_NUMBER_FLAG_DECIMAL, App.HORIZONTAL, true);
        baselineBmi = new TitledEditText(context, null, getResources().getString(R.string.pmdt_baseline_bmi), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_NUMBER_FLAG_DECIMAL, App.HORIZONTAL, true);
        currentBmiCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_current_bmi_category), getResources().getStringArray(R.array.pmdt_bmi_categories), "", App.HORIZONTAL, App.VERTICAL, true);
        baselineBmiCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_baseline_bmi_category), getResources().getStringArray(R.array.pmdt_bmi_categories), "", App.HORIZONTAL, App.VERTICAL, true);

        eligibleNutritionalSupport = new TitledRadioGroup(context, getResources().getString(R.string.pmdt_title_nutrition_eligibility), getResources().getString(R.string.pmdt_eligible_nutrition_support), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        nutritionalSupportType = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_nutrition_support_type), getResources().getStringArray(R.array.pmdt_nutrition_support_types), getResources().getString(R.string.pmdt_glucerna), App.VERTICAL, true);
        otherNutritionalSupportType = new TitledEditText(context, null, getResources().getString(R.string.pmdt_other_nutritional_support_item), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        nutritionalSupportVoucherNumber = new TitledEditText(context, null, getResources().getString(R.string.pmdt_nutritional_support_voucher_number), "", "", 20, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), externalId.getEditText(), typeAssessment.getSpinner(), otherAssessmentReason.getEditText(), treatmentMonth.getEditText(), treatmentFacility.getSpinner(), otherTreatmentFacility.getEditText(),
                nationalDrTbRegistrationNumber.getEditText(), isRegisteredOutstationFacility.getRadioGroup(), outstationFacility, referredNutritionist.getRadioGroup(), diabetic.getRadioGroup(), height.getEditText(),
                weight.getEditText(), currentBmi.getEditText(), baselineBmi.getEditText(), currentBmiCategory.getRadioGroup(), baselineBmiCategory.getRadioGroup(), eligibleNutritionalSupport.getRadioGroup(), nutritionalSupportType.getSpinner(),
                otherNutritionalSupportType.getEditText(), nutritionalSupportVoucherNumber.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, externalId, typeAssessment, otherAssessmentReason, treatmentMonth, treatmentFacility,  otherTreatmentFacility, nationalDrTbRegistrationNumber},
                        {isRegisteredOutstationFacility, outstationFacility, referredNutritionist, diabetic, height, weight, currentBmi, baselineBmi,
                                currentBmiCategory, baselineBmiCategory},
                        {eligibleNutritionalSupport, nutritionalSupportType, otherNutritionalSupportType, nutritionalSupportVoucherNumber}};

        formDate.getButton().setOnClickListener(this);
        treatmentFacility.getSpinner().setOnItemSelectedListener(this);
        typeAssessment.getSpinner().setOnItemSelectedListener(this);
        isRegisteredOutstationFacility.getRadioGroup().setOnCheckedChangeListener(this);
        referredNutritionist.getRadioGroup().setOnCheckedChangeListener(this);
        diabetic.getRadioGroup().setOnCheckedChangeListener(this);
        currentBmiCategory.getRadioGroup().setOnCheckedChangeListener(this);
        baselineBmiCategory.getRadioGroup().setOnCheckedChangeListener(this);
        eligibleNutritionalSupport.getRadioGroup().setOnCheckedChangeListener(this);
        nutritionalSupportType.getSpinner().setOnItemSelectedListener(this);
    }

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();
            personDOB = personDOB.substring(0, 10);

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }


    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public boolean submit() {
        return false;
    }

    @Override
    public boolean save() {
        return false;
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
    public void refill(int encounterId) {

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
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageCount;
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
