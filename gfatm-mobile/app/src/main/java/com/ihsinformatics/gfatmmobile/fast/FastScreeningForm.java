package com.ihsinformatics.gfatmmobile.fast;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Haris on 12/15/2016.
 */

public class FastScreeningForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledRadioGroup screeningLocation;
    TitledSpinner hospital;
    TitledSpinner hospitalSection;
    TitledEditText hospitalSectionOther;
    TitledSpinner opdWardSection;
    TitledRadioGroup patientAttendant;
    TitledRadioGroup ageRange;
    TitledRadioGroup gender;
    TitledRadioGroup coughTwoWeeks;
    TitledRadioGroup tbContact;
    TitledRadioGroup tbHistory;

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

        PAGE_COUNT = 2;
        FORM_NAME = Forms.FAST_SCREENING_FORM;
        FORM = Forms.fastScreeningForm;

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
        screeningLocation = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_location_of_screening), getResources().getStringArray(R.array.fast_locations), getResources().getString(R.string.fast_hospital_title), App.HORIZONTAL, App.HORIZONTAL);

        String columnName = "";
        if (App.getProgram().equals(getResources().getString(R.string.pet)))
            columnName = "pet_location";
        else if (App.getProgram().equals(getResources().getString(R.string.fast)))
            columnName = "fast_location";
        else if (App.getProgram().equals(getResources().getString(R.string.comorbidities)))
            columnName = "comorbidities_location";
        else if (App.getProgram().equals(getResources().getString(R.string.pmdt)))
            columnName = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.childhood_tb)))
            columnName = "childhood_tb_location";

        final Object[][] locations = serverService.getAllLocations(columnName);
        String[] locationArray = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][1]);
        }

        hospital = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_if_hospital_specify), locationArray, "", App.VERTICAL);
        hospitalSection = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_hospital_parts_title), getResources().getStringArray(R.array.fast_hospital_parts), getResources().getString(R.string.fast_opdclinicscreening_title), App.VERTICAL);
        hospitalSectionOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        opdWardSection = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_clinic_and_ward_title), getResources().getStringArray(R.array.fast_clinic_and_ward_list), getResources().getString(R.string.fast_generalmedicinefilterclinic_title), App.VERTICAL);
        patientAttendant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_patient_or_attendant_title), getResources().getStringArray(R.array.fast_patient_or_attendant_list), getResources().getString(R.string.fast_patient_title), App.HORIZONTAL, App.HORIZONTAL);
        ageRange = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_age_range_title), getResources().getStringArray(R.array.fast_age_range_list), getResources().getString(R.string.fast_greater_title), App.HORIZONTAL, App.HORIZONTAL);
        gender = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_gender_title), getResources().getStringArray(R.array.fast_gender_list), getResources().getString(R.string.fast_male_title), App.HORIZONTAL, App.HORIZONTAL);
        coughTwoWeeks = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_cough_period_title), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        tbContact = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_close_with_someone_diagnosed), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        tbHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_tb_before), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), screeningLocation.getRadioGroup(), hospital.getSpinner(),
                hospitalSection.getSpinner(), hospitalSectionOther.getEditText(), opdWardSection.getSpinner(),
                patientAttendant.getRadioGroup(), ageRange.getRadioGroup(), gender.getRadioGroup(), coughTwoWeeks.getRadioGroup(),
                tbContact.getRadioGroup(), tbHistory.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, screeningLocation, hospital, hospitalSection, hospitalSectionOther, opdWardSection, patientAttendant, ageRange, gender},
                        {coughTwoWeeks, tbContact, tbHistory}};

        formDate.getButton().setOnClickListener(this);
        hospitalSection.getSpinner().setOnItemSelectedListener(this);
        screeningLocation.getRadioGroup().setOnCheckedChangeListener(this);
        patientAttendant.getRadioGroup().setOnCheckedChangeListener(this);
        ageRange.getRadioGroup().setOnCheckedChangeListener(this);
        gender.getRadioGroup().setOnCheckedChangeListener(this);
        coughTwoWeeks.getRadioGroup().setOnCheckedChangeListener(this);
        tbHistory.getRadioGroup().setOnCheckedChangeListener(this);
        tbContact.getRadioGroup().setOnCheckedChangeListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
    }

    @Override
    public boolean validate() {
        Boolean error = false;

        if (hospitalSectionOther.getVisibility() == View.VISIBLE && App.get(hospitalSectionOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(0);
            hospitalSectionOther.getEditText().setError(getString(R.string.empty_field));
            hospitalSectionOther.getEditText().requestFocus();
            error = true;
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
          //  DrawableCompat.setTint(clearIcon, color);
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

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        final ContentValues values = new ContentValues();

        values.put("location", App.getLocation());
        values.put("entereddate", App.getSqlDate(formDateCalendar));

        if (screeningLocation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SCREENING_LOCATION", App.get(screeningLocation).equals(getResources().getString(R.string.fast_community_title)) ? "COMMUNITY" : "HOSPITAL"});

        if (hospital.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HOSPITAL_FACILITY_NAME", App.get(hospital)});

        if (hospitalSection.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HOSPITAL_SECTION", App.get(hospitalSection).equals(getResources().getString(R.string.fast_opdclinicscreening_title)) ? "OPD CLINIC SCREENING" :
                    (App.get(hospitalSection).equals(getResources().getString(R.string.fast_wardscreening_title)) ? "WARD SCREENING" :
                            (App.get(hospitalSection).equals(getResources().getString(R.string.fast_registrationdesk_title)) ? "REGISTRATION DESK" :
                                    (App.get(hospitalSection).equals(getResources().getString(R.string.fast_nexttoxrayvan_title)) ? "X-RAY VAN" : "OTHER FACILITY SECTION")))});

        if (hospitalSectionOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HOSPITAL_SECTION_OTHER", App.get(hospitalSectionOther)});

        if (opdWardSection.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OPD_WARD_SECTION", App.get(opdWardSection).equals(getResources().getString(R.string.fast_generalmedicinefilterclinic_title)) ? "GENERAL MEDICINE DEPARTMENT" :
                    (App.get(opdWardSection).equals(getResources().getString(R.string.fast_chesttbclinic_title)) ? "CHEST MEDICINE DEPARTMENT" :
                            (App.get(opdWardSection).equals(getResources().getString(R.string.fast_gynaeobstetrics_title)) ? "OBSTETRICS AND GYNECOLOGY DEPARTMENT" :
                                    (App.get(opdWardSection).equals(getResources().getString(R.string.fast_surgery_title)) ? "GENERAL SURGERY DEPARTMENT" : "EMERGENCY DEPARTMENT")))});


        if (patientAttendant.getVisibility() == View.VISIBLE)
        observations.add(new String[]{"PATIENT_OR_ATTENDANT", App.get(patientAttendant).equals(getResources().getString(R.string.fast_patient_title)) ? "PATIENT" : "ATTENDANT"});

        if (ageRange.getVisibility() == View.VISIBLE)
        observations.add(new String[]{"AGE_RANGE", App.get(ageRange).equals(getResources().getString(R.string.fast_greater_title)) ? ">= 15 YEARS OLD" : "<15 YEARS OLD"});

        if (gender.getVisibility() == View.VISIBLE)
        observations.add(new String[]{"GENDER", App.get(gender).equals(getResources().getString(R.string.fast_male_title)) ? "MALE" : "FEMALE"});

        if (coughTwoWeeks.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TWO_WEEKS_COUGH", App.get(tbContact).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(tbContact).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(tbContact).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (tbContact.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOSIS_CONTACT", App.get(tbContact).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(tbContact).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(tbContact).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (tbHistory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HISTORY_OF_TUBERCULOSIS", App.get(tbHistory).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(tbHistory).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(tbHistory).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});



    AsyncTask<String, String, String> submissionFormTask = new AsyncTask<String, String, String>() {
        @Override
        protected String doInBackground(String... params) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading.setInverseBackgroundForced(true);
                    loading.setIndeterminate(true);
                    loading.setCancelable(false);
                    loading.setMessage(getResources().getString(R.string.submitting_form));
                    loading.show();
                }
            });

            String result = serverService.submitToGwtApp("fast_screening", values, observations.toArray(new String[][]{}));
            if (result.contains("SUCCESS"))
                return "SUCCESS";

            return result;

        }

        @Override
        protected void onProgressUpdate(String... values) {
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.dismiss();

            if (result.equals("SUCCESS")) {
                resetViews();

                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage(getResources().getString(R.string.form_submitted));
                Drawable submitIcon = getResources().getDrawable(R.drawable.ic_submit);
                alertDialog.setIcon(submitIcon);
                int color = App.getColor(context, R.attr.colorAccent);
                DrawableCompat.setTint(submitIcon, color);
                alertDialog.setTitle(getResources().getString(R.string.title_completed));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                                } catch (Exception e) {
                                    // TODO: handle exception
                                }
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else if (result.equals("CONNECTION_ERROR")) {
                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage(getResources().getString(R.string.data_connection_error) + "\n\n (" + result + ")");
                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_error));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                                } catch (Exception e) {
                                    // TODO: handle exception
                                }
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else {
                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                String message = getResources().getString(R.string.insert_error) + "\n\n (" + result + ")";
                alertDialog.setMessage(message);
                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_error));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                                } catch (Exception e) {
                                    // TODO: handle exception
                                }
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

        }
    };
    submissionFormTask.execute("");

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
    public void refill(int encounterId) {

    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == hospitalSection.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                hospitalSectionOther.setVisibility(View.VISIBLE);
                opdWardSection.setVisibility(View.GONE);
            } else if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_opdclinicscreening_title)) ||
                    parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_wardscreening_title))) {
                hospitalSectionOther.setVisibility(View.GONE);
                opdWardSection.setVisibility(View.VISIBLE);
            } else if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_registrationdesk_title)) ||
                    parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_nexttoxrayvan_title))) {
                hospitalSectionOther.setVisibility(View.GONE);
                opdWardSection.setVisibility(View.GONE);
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
        hospitalSectionOther.setVisibility(View.GONE);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == screeningLocation.getRadioGroup()) {
            if (screeningLocation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_hospital_title))) {
                hospital.setVisibility(View.VISIBLE);
                hospitalSection.setVisibility(View.VISIBLE);
                if (hospitalSection.getSpinner().getSelectedItem().
                        equals(getResources().getString(R.string.fast_opdclinicscreening_title))
                        || hospitalSection.getSpinner().getSelectedItem().
                        equals(getResources().getString(R.string.fast_wardscreening_title)))
                    opdWardSection.setVisibility(View.VISIBLE);
                if (hospitalSection.getSpinner()
                        .getSelectedItem().equals(getResources().getString(R.string.fast_other_title)))
                    hospitalSectionOther.setVisibility(View.VISIBLE);
            } else {
                hospital.setVisibility(View.GONE);
                hospitalSection.setVisibility(View.GONE);
                opdWardSection.setVisibility(View.GONE);
                hospitalSectionOther.setVisibility(View.GONE);
            }
        }

      /* else if (radioGroup == hospitalSection.getRadioGroup()) {
            if (hospitalSection.getRadioGroup()
                    .getSelectedValue().equals(getResources().getString(R.string.fast_other_title))) {
                hospitalSectionOther.setVisibility(View.VISIBLE);
                opdWardSection.setVisibility(View.GONE);
            } else if (hospitalSection.getRadioGroup().getSelectedValue().
                    equals(getResources().getString(R.string.fast_opdclinicscreening_title))
                    || hospitalSection.getRadioGroup().getSelectedValue().
                    equals(getResources().getString(R.string.fast_wardscreening_title))) {
                hospitalSectionOther.setVisibility(View.GONE);
                opdWardSection.setVisibility(View.VISIBLE);
            } else if (hospitalSection.getRadioGroup()
                    .getSelectedValue().equals(getResources().getString(R.string.fast_registrationdesk_title)) ||
                    hospitalSection.getRadioGroup().getSelectedValue()
                            .equals(getResources().getString(R.string.fast_nexttoxrayvan_title))) {
                hospitalSectionOther.setVisibility(View.GONE);
                opdWardSection.setVisibility(View.GONE);
            }
        }*/
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