package com.ihsinformatics.gfatmmobile.fast;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.shared.RequestType;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 12/15/2016.
 */

public class FastScreeningForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener, TextWatcher {

    Context context;
    Boolean emptyError = false;

    // Views...
    TitledRadioGroup typeOfScreening;
    TitledRadioGroup screeningLocation;
    TitledEditText hospital;
    TitledSpinner hospitalSection;
    TitledEditText hospitalSectionOther;
    TitledEditText age;
    TitledSpinner opdWardSection;
    TitledRadioGroup patientAttendant;
    TitledRadioGroup ageRange;
    TitledRadioGroup gender;
    TitledRadioGroup coughTwoWeeks;
    /*TitledRadioGroup tbContact;
    TitledRadioGroup tbHistory;*/
    MyTextView tbSymptoms;
    MyTextView screeningInstruction;


    /**
     * CHANGE pageCount and formName Variable only...
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 2;
        formName = Forms.FAST_SCREENING_FORM;
        form = Forms.fastScreeningForm;

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
            for (int i = 0; i < pageCount; i++) {
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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        typeOfScreening = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_type_of_screen), getResources().getStringArray(R.array.fast_type_of_screening_list), "", App.HORIZONTAL, App.HORIZONTAL, false, "TYPE OF SCREENING", new String[]{"CXR Screening", "VERBAL SYMPTOM SCREENING"});
        screeningLocation = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_location_of_screening), getResources().getStringArray(R.array.fast_locations), getResources().getString(R.string.fast_hospital_title), App.HORIZONTAL, App.HORIZONTAL, false, "SCREENING_LOCATION", new String[]{"COMMUNITY", "HOSPITAL"});


        hospital = new TitledEditText(context, null, getResources().getString(R.string.fast_if_hospital_specify), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "HOSPITAL_FACILITY_NAME");
        hospital.getEditText().setKeyListener(null);
        hospital.getEditText().setFocusable(false);
        // hospital = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_if_hospital_specify), locationArray, "", App.VERTICAL);
        hospitalSection = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_hospital_parts_title), getResources().getStringArray(R.array.fast_hospital_parts_screening), "", App.VERTICAL, true, "HOSPITAL_SECTION", new String[]{"", "OPD CLINIC SCREENING", "WARD SCREENING", "REGISTRATION DESK", "X-RAY VAN", "EMERGENCY DEPARTMENT", "OTHER FACILITY SECTION"});
        hospitalSectionOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "HOSPITAL_SECTION_OTHER");
        opdWardSection = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_clinic_and_ward_title), getResources().getStringArray(R.array.fast_clinic_and_ward_screening_list), "", App.VERTICAL, true, "OPD_WARD_SECTION", getResources().getStringArray(R.array.fast_clinic_and_ward_screening_list_concept));
        patientAttendant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_patient_or_attendant_title), getResources().getStringArray(R.array.fast_patient_or_attendant_list), getResources().getString(R.string.fast_patient_title), App.HORIZONTAL, App.HORIZONTAL, true, "PATIENT_OR_ATTENDANT", new String[]{"PATIENT", "ATTENDANT"});
        age = new TitledEditText(context, null, getResources().getString(R.string.fast_age_in_year), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "AGE_IN_YEARS");
        ageRange = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_age_range_title), getResources().getStringArray(R.array.fast_age_range_list), getResources().getString(R.string.fast_greater_title), App.HORIZONTAL, App.HORIZONTAL, true, "AGE_RANGE", new String[]{"<15 YEARS OLD", ">= 15 YEARS OLD"});
        gender = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_gender_title), getResources().getStringArray(R.array.fast_gender_list), "", App.HORIZONTAL, App.HORIZONTAL, true, "GENDER", new String[]{"MALE", "FEMALE"});
        tbSymptoms = new MyTextView(context, getResources().getString(R.string.fast_tb_symptoms));
        tbSymptoms.setTypeface(null, Typeface.BOLD);
        coughTwoWeeks = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_cough_period_title), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true, "TWO_WEEKS_COUGH", getResources().getStringArray(R.array.fast_choice_list_concepts));


        // Used for reset fields...
        views = new View[]{formDate.getButton(), typeOfScreening.getRadioGroup(), screeningLocation.getRadioGroup(), hospital.getEditText(),
                hospitalSection.getSpinner(), hospitalSectionOther.getEditText(), opdWardSection.getSpinner(),
                patientAttendant.getRadioGroup(), age.getEditText(), ageRange.getRadioGroup(), gender.getRadioGroup(), coughTwoWeeks.getRadioGroup(),
                /*tbContact.getRadioGroup(), tbHistory.getRadioGroup()*/};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, typeOfScreening, screeningLocation, hospital, hospitalSection, hospitalSectionOther, opdWardSection, patientAttendant, age, ageRange, gender},
                        {tbSymptoms, coughTwoWeeks, /*tbContact, tbHistory,screeningInstruction*/}};

        formDate.getButton().setOnClickListener(this);
        hospitalSection.getSpinner().setOnItemSelectedListener(this);
        screeningLocation.getRadioGroup().setOnCheckedChangeListener(this);
        patientAttendant.getRadioGroup().setOnCheckedChangeListener(this);
        ageRange.getRadioGroup().setOnCheckedChangeListener(this);
        gender.getRadioGroup().setOnCheckedChangeListener(this);
        coughTwoWeeks.getRadioGroup().setOnCheckedChangeListener(this);
        age.getEditText().addTextChangedListener(this);
        /*tbHistory.getRadioGroup().setOnCheckedChangeListener(this);
        tbContact.getRadioGroup().setOnCheckedChangeListener(this);*/

        resetViews();
    }

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        }

        formDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = super.validate();
        emptyError = error;


        if (age.getVisibility() == View.VISIBLE) {
            if (App.get(age).isEmpty()) {
                age.getEditText().setError(getString(R.string.empty_field));
                age.getEditText().requestFocus();
                gotoLastPage();
                error = true;
            } else {
                int age = Integer.parseInt(App.get(this.age));
                if (age > 125) {
                    this.age.getEditText().setError(getResources().getString(R.string.age_exceed_error));
                    this.age.getEditText().requestFocus();
                    gotoLastPage();
                } else if (age < 15) {
                    this.age.getEditText().setError(getResources().getString(R.string.age_error));
                    this.age.getEditText().requestFocus();
                } else {
                    this.age.getEditText().clearFocus();
                    this.age.getEditText().setError(null);
                }
            }
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
            if (!emptyError)
                alertDialog.setMessage(getString(R.string.form_error));
            else
                alertDialog.setMessage(getString(R.string.fast_required_field_error));

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

        final ArrayList<String[]> observations = new ArrayList<>();

        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                Boolean flag = serverService.deleteOfflineForms(encounterId);
                if (!flag) {

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.form_does_not_exist));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    bundle.putBoolean("save", false);
                                    submit();
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.backToMainMenu();
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
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));

                    /*Toast.makeText(context, getString(R.string.form_does_not_exist),
                            Toast.LENGTH_LONG).show();*/

                    return false;
                }
            } else {
                endTime = new Date();
            }
            bundle.putBoolean("save", false);
        } else {
            endTime = new Date();
        }


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
                                    (App.get(hospitalSection).equals(getResources().getString(R.string.fast_nexttoxrayvan_title)) ? "X-RAY VAN" :
                                            (App.get(hospitalSection).equals(getResources().getString(R.string.fast_emergency_room)) ? "EMERGENCY DEPARTMENT" :
                                                    "OTHER FACILITY SECTION"))))});

        if (hospitalSectionOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HOSPITAL_SECTION_OTHER", App.get(hospitalSectionOther)});
        if (opdWardSection.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OPD_WARD_SECTION", App.get(opdWardSection).equals(getResources().getString(R.string.fast_generalmedicinefilterclinic_title)) ? "GENERAL MEDICINE DEPARTMENT" :
                    (App.get(opdWardSection).equals(getResources().getString(R.string.fast_chesttbclinic_title)) ? "CHEST MEDICINE DEPARTMENT" :
                            (App.get(opdWardSection).equals(getResources().getString(R.string.fast_gynaeobstetrics_title)) ? "OBSTETRICS AND GYNECOLOGY DEPARTMENT" :
                                    (App.get(opdWardSection).equals(getResources().getString(R.string.fast_surgery_title)) ? "GENERAL SURGERY DEPARTMENT" : "DIABETIC")))});

        if (patientAttendant.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT_OR_ATTENDANT", App.get(patientAttendant).equals(getResources().getString(R.string.fast_patient_title)) ? "PATIENT" : "ATTENDANT"});

        if (typeOfScreening.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SCREENING_TYPE", App.get(typeOfScreening).equals("CXR Screening") ? "CXR Screening" : "VERBAL SYMPTOM SCREENING"});

        if (age.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"AGE_IN_YEAR", App.get(age)});

        if (ageRange.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"AGE_RANGE", App.get(ageRange).equals(getResources().getString(R.string.fast_greater_title)) ? ">= 15 YEARS OLD" : "<15 YEARS OLD"});

        if (gender.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"GENDER", App.get(gender).equals(getResources().getString(R.string.fast_male_title)) ? "MALE" : "FEMALE"});

        if (coughTwoWeeks.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TWO_WEEKS_COUGH", App.get(coughTwoWeeks).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(coughTwoWeeks).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(coughTwoWeeks).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});
/*        if (tbContact.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOSIS_CONTACT", App.get(tbContact).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(tbContact).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(tbContact).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (tbHistory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HISTORY_OF_TUBERCULOSIS", App.get(tbHistory).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(tbHistory).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(tbHistory).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});*/


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

                String result = serverService.submitToGwtApp(RequestType.FAST_SCREENING, form, values, observations.toArray(new String[][]{}));
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
                    MainActivity.backToMainMenu();
                    try {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

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

        serverService.saveFormLocally(formName, "12345-5", formValues);*/

        return true;
    }

    @Override
    public void refill(int formId) {

        super.refill(formId);

    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar, false, true, false);
            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);*/
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
            } else {
                hospitalSectionOther.setVisibility(View.GONE);
                opdWardSection.setVisibility(View.GONE);
            }
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_opdclinicscreening_title)) || parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_wardscreening_title))) {
                hospitalSectionOther.setVisibility(View.GONE);
                opdWardSection.setVisibility(View.VISIBLE);
            }
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_registrationdesk_title)) ||
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
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        hospitalSectionOther.setVisibility(View.GONE);
        hospital.getEditText().setText(App.getLocation().toString());

        ageRange.setRadioGroupEnabled(false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);

                refill(formId);

            } else bundle.putBoolean("save", false);

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == screeningLocation.getRadioGroup()) {
            if (screeningLocation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_hospital_title))) {
                hospital.setVisibility(View.VISIBLE);
                patientAttendant.setVisibility(View.VISIBLE);
                hospitalSection.setVisibility(View.VISIBLE);
                if (hospitalSection.getSpinner().getSelectedItem().
                        equals(getResources().getString(R.string.fast_opdclinicscreening_title)) || hospitalSection.getSpinner().getSelectedItem().
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
                patientAttendant.setVisibility(View.GONE);
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String str = charSequence.toString();
        if (!StringUtils.isEmpty(str)) {
            Integer age = Integer.valueOf(str);
            if (age > 15 && age < 125) {
                ageRange.setValueByConcept(">= 15 YEARS OLD");
            } else if (age < 15) {
                ageRange.setValueByConcept("<15 YEARS OLD");
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

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