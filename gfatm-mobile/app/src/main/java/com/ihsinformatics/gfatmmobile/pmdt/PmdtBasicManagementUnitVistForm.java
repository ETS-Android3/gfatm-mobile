package com.ihsinformatics.gfatmmobile.pmdt;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Tahira on 2/27/2017.
 */

public class PmdtBasicManagementUnitVistForm extends AbstractFormActivity {

    Context context;
    TitledButton formDate;
    TitledSpinner cityVillage;
    TitledSpinner district;
    // province selected in preferences
    TitledEditText basicManagementUnitVisited;
    TitledEditText doctorVisitedName;
    TitledEditText numberFailureCases;
    TitledSpinner referredFacility;
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
        pageCount = 1;
        formName = Forms.PMDT_BASIC_MANAGEMENT_UNIT_VISIT;
        form = Forms.pmdtBasicManagementUnitVisit;

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

    /**
     * Initializes all views and ArrayList and Views Array
     */
    @Override
    public void initViews() {
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pmdt_visit_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        district = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_district), getResources().getStringArray(R.array.pmdt_empty_array), "", App.VERTICAL);
        cityVillage = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_city_village), getResources().getStringArray(R.array.pmdt_empty_array), "", App.VERTICAL);
        basicManagementUnitVisited = new TitledEditText(context, null, getResources().getString(R.string.pmdt_basic_management_unit_visited), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        doctorVisitedName = new TitledEditText(context, null, getResources().getString(R.string.pmdt_doctor_name_visited), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        numberFailureCases = new TitledEditText(context, null, getResources().getString(R.string.pmdt_number_failure_cases), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        referredFacility = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_facility_referred), getResources().getStringArray(R.array.pmdt_empty_array), "", App.VERTICAL);
        numberPatientsEnrolled = new TitledEditText(context, null, getResources().getString(R.string.pmdt_number_patients_enrolled), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);

        views = new View[]{formDate.getButton(), district.getSpinner(), cityVillage.getSpinner(),
                basicManagementUnitVisited.getEditText(), doctorVisitedName.getEditText(), numberFailureCases.getEditText(),
                referredFacility.getSpinner(), numberPatientsEnrolled.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, district, cityVillage, basicManagementUnitVisited, doctorVisitedName,
                        numberFailureCases, referredFacility, numberPatientsEnrolled}};

        formDate.getButton().setOnClickListener(this);
        district.getSpinner().setOnItemSelectedListener(this);
        resetViews();

    }

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();
            personDOB = personDOB.substring(0,10);

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

        MySpinner spinner = (MySpinner) parent;
        if (spinner == district.getSpinner()) {

            String[] cities = serverService.getCityList(App.get(district));
            cityVillage.getSpinner().setAdapter(null);

            ArrayAdapter<String> spinnerArrayAdapter = null;
            if (App.isLanguageRTL()) {
                spinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.custom_rtl_spinner, cities);
                cityVillage.getSpinner().setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_rtl_spinner);
                cityVillage.getSpinner().setGravity(Gravity.RIGHT);
            } else {
                spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, cities);
                cityVillage.getSpinner().setAdapter(spinnerArrayAdapter);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cityVillage.getSpinner().setGravity(Gravity.LEFT);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        // fetching districts
        String[] districts = serverService.getDistrictList(App.getProvince());
        district.getSpinner().setAdapter(null);

        ArrayAdapter<String> spinnerArrayAdapter = null;
        if (App.isLanguageRTL()) {
            spinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.custom_rtl_spinner, districts);
            district.getSpinner().setAdapter(spinnerArrayAdapter);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_rtl_spinner);
            district.getSpinner().setGravity(Gravity.RIGHT);
        } else {
            spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, districts);
            district.getSpinner().setAdapter(spinnerArrayAdapter);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            district.getSpinner().setGravity(Gravity.LEFT);
        }

        // fetching cities
        String[] cities = serverService.getCityList(App.get(district));
        cityVillage.getSpinner().setAdapter(null);

        spinnerArrayAdapter = null;
        if (App.isLanguageRTL()) {
            spinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.custom_rtl_spinner, cities);
            cityVillage.getSpinner().setAdapter(spinnerArrayAdapter);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_rtl_spinner);
            cityVillage.getSpinner().setGravity(Gravity.RIGHT);
        } else {
            spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, cities);
            cityVillage.getSpinner().setAdapter(spinnerArrayAdapter);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cityVillage.getSpinner().setGravity(Gravity.LEFT);
        }


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

        final Object[][] locations = serverService.getAllLocationsFromLocalDB(program);
        String[] locationArray = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][1]);
        }

        referredFacility.getSpinner().setAdapter(null);

        spinnerArrayAdapter = null;
        if (App.isLanguageRTL()) {
            spinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.custom_rtl_spinner, locationArray);
            referredFacility.getSpinner().setAdapter(spinnerArrayAdapter);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_rtl_spinner);
            referredFacility.getSpinner().setGravity(Gravity.RIGHT);
        } else {
            spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, locationArray);
            referredFacility.getSpinner().setAdapter(spinnerArrayAdapter);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            referredFacility.getSpinner().setGravity(Gravity.LEFT);
        }

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
