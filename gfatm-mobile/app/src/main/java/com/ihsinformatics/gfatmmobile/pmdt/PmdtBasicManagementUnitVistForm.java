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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;

/**
 * Created by Tahira on 2/27/2017.
 */

public class PmdtBasicManagementUnitVistForm extends AbstractFormActivity {

    Context context;
    TitledButton formDate;
    TitledButton visitDate;
    TitledSpinner district;
    TitledSpinner townTaluka;
    // city selected in preferences
    TitledEditText basicManagmentUnitVisited;
    TitledEditText doctorVisitedName;
    TitledEditText numberFailureCases;
    LinearLayout facilityLinearLayout;
    TextView referredFacilityText;
    AutoCompleteTextView referredFacilityAutoCompleteList;
    TitledEditText numberPatientsEnrolled;

    Snackbar snackbar;
    ScrollView scrollView;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PAGE_COUNT = 1;
        FORM_NAME = Forms.PMDT_BASIC_MANAGEMENT_UNIT_VISIT;
        FORM = Forms.basicManagementUnitVisit;

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
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }

        return mainContent;
    }

    /**
     * Initializes all views and ArrayList and Views Array
     */
    @Override
    public void initViews() {
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        visitDate = new TitledButton(context, null, getResources().getString(R.string.pmdt_visit_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        district = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_district), getResources().getStringArray(R.array.pmdt_towns), getResources().getString(R.string.pmdt_gulshan_e_iqbal), App.HORIZONTAL, true);
        townTaluka = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_town_taluka_tehsil), getResources().getStringArray(R.array.pmdt_towns), getResources().getString(R.string.pmdt_gulshan_e_iqbal), App.HORIZONTAL, true);
        basicManagmentUnitVisited = new TitledEditText(context, null, getResources().getString(R.string.pmdt_basic_management_unit_visited), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        doctorVisitedName = new TitledEditText(context, null, getResources().getString(R.string.pmdt_doctor_name_visited), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        numberFailureCases = new TitledEditText(context, null, getResources().getString(R.string.pmdt_number_failure_cases), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);

        // Fetching PMDT Locations
        String program = "";
        if (App.getProgram().equals(getResources().getString(R.string.pet)))
            program = "pet_location";
        else if (App.getProgram().equals(getResources().getString(R.string.fast)))
            program = "fast_location";
        else if (App.getProgram().equals(getResources().getString(R.string.comorbidities)))
            program = "comorbidities_location";
        else if (App.getProgram().equals(getResources().getString(R.string.pmdt)))
            program = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.childhood_tb)))
            program = "childhood_tb_location";

        final Object[][] locations = serverService.getAllLocations(program);
        String[] locationArray = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][1]);
        }

        referredFacilityText = new TextView(context);
        referredFacilityText.setText(getResources().getString(R.string.pmdt_facility_referred));
        LinearLayout referredFacilityLayout = new LinearLayout(context);
        MyTextView referredFacilityQuestionRequired = new MyTextView(context, "*");
        int color1 = App.getColor(context, R.attr.colorAccent);
        referredFacilityQuestionRequired.setTextColor(color1);
        referredFacilityLayout.setOrientation(LinearLayout.HORIZONTAL);
        referredFacilityLayout.addView(referredFacilityQuestionRequired);
        referredFacilityLayout.addView(referredFacilityText);
        referredFacilityAutoCompleteList = new AutoCompleteTextView(context);
        final ArrayAdapter<String> autoCompleteLocationAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, locationArray);
        referredFacilityAutoCompleteList.setAdapter(autoCompleteLocationAdapter);
        referredFacilityAutoCompleteList.setHint(getResources().getString(R.string.pmdt_facility_hint));
        facilityLinearLayout = new LinearLayout(context);
        facilityLinearLayout.setOrientation(LinearLayout.VERTICAL);
        facilityLinearLayout.addView(referredFacilityLayout);
        facilityLinearLayout.addView(referredFacilityAutoCompleteList);

        numberPatientsEnrolled = new TitledEditText(context, null, getResources().getString(R.string.pmdt_number_patients_enrolled), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);

        views = new View[]{formDate.getButton(), visitDate.getButton(), district.getSpinner(), townTaluka.getSpinner(),
                basicManagmentUnitVisited.getEditText(), doctorVisitedName.getEditText(), numberFailureCases.getEditText(),
                referredFacilityAutoCompleteList, numberPatientsEnrolled.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, visitDate, district, townTaluka, basicManagmentUnitVisited, doctorVisitedName,
                        numberFailureCases, facilityLinearLayout, numberPatientsEnrolled}};

        formDate.getButton().setOnClickListener(this);
        visitDate.getButton().setOnClickListener(this);
        resetViews();

    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        visitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
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
        } else if (view == visitDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowFutureDate", false);
            args.putBoolean("allowPastDate", true);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
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
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        visitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
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
