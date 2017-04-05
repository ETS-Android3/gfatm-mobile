package com.ihsinformatics.gfatmmobile.childhoodTb;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyCheckBox;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbVerbalScreeningForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledRadioGroup screeningLocation;
    TitledSpinner hospital;
    TitledSpinner facility_section;
    TitledEditText facility_section_other;
    TitledSpinner opd_ward_section;
    TitledEditText motherName;
    TitledEditText fatherName;
    TitledRadioGroup patientAttendant;
    TitledRadioGroup cough;
    TitledSpinner coughDuration;
    TitledRadioGroup fever;
    TitledRadioGroup nightSweats;
    TitledRadioGroup weightLoss;
    TitledRadioGroup appeptite;
    TitledRadioGroup lymphnodeSwelling;
    TitledRadioGroup jointSwellingTwoWeeks;
    TitledRadioGroup tbHistory;
    TitledRadioGroup tbMedication;
    TitledRadioGroup contactTbHistoryTwoYears;
    TitledCheckBoxes closeContactType;
    TitledEditText otherContactType;
    TitledRadioGroup presumptiveTb;

    Snackbar snackbar;
    ScrollView scrollView;

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
        FORM_NAME = Forms.CHILDHOODTB_VERBAL_SCREENING;
        FORM = Forms.childhoodTb_verbalScreeningForm;

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
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
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
        if (App.getPatient().getPerson().getAge() > 15) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.ctb_age_greater_than_15));
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
        }
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        screeningLocation = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_screening_location), getResources().getStringArray(R.array.ctb_screening_location_list), getResources().getString(R.string.ctb_hospital), App.HORIZONTAL, App.VERTICAL, true);
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
            Object objLoc = locations[i][1];
            locationArray[i] = objLoc.toString();
        }
        hospital = new TitledSpinner(context, null, getResources().getString(R.string.ctb_hospital_specify), locationArray, null, App.VERTICAL);
        facility_section = new TitledSpinner(context, null, getResources().getString(R.string.ctb_facility_section), getResources().getStringArray(R.array.ctb_facility_section_list), null, App.VERTICAL);
        facility_section_other = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        opd_ward_section = new TitledSpinner(context, null, getResources().getString(R.string.ctb_opd_clinic_or_ward), getResources().getStringArray(R.array.ctb_opd_ward_section_list), null, App.VERTICAL);
        motherName = new TitledEditText(context, null, getResources().getString(R.string.ctb_mother_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        fatherName = new TitledEditText(context, null, getResources().getString(R.string.ctb_father_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        patientAttendant = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_patient_attendant), getResources().getStringArray(R.array.ctb_patient_attendant_list), getResources().getString(R.string.ctb_patient), App.HORIZONTAL, App.VERTICAL, true);
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_cough), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        coughDuration = new TitledSpinner(context, null, getResources().getString(R.string.ctb_cough_duration), getResources().getStringArray(R.array.ctb_cough_duration_list), getResources().getString(R.string.ctb_less_than_2_weeks), App.VERTICAL, true);
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_fever_more_than_2_weeks), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_night_sweats), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.ctb_no), App.HORIZONTAL, App.VERTICAL, true);
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_weight_loss), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.ctb_no), App.HORIZONTAL, App.VERTICAL, true);
        appeptite = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_child_appetite), getResources().getStringArray(R.array.ctb_appetite_list), getResources().getString(R.string.ctb_ok), App.HORIZONTAL, App.VERTICAL, true);
        lymphnodeSwelling = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_lymph_node_swelling), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.ctb_no), App.HORIZONTAL, App.VERTICAL, true);
        jointSwellingTwoWeeks = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_joint_spine_swelling), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.ctb_no), App.HORIZONTAL, App.VERTICAL, true);
        tbHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_before), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.ctb_no), App.HORIZONTAL, App.VERTICAL, true);
        tbMedication = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_medication), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.ctb_no), App.HORIZONTAL, App.VERTICAL, true);
        contactTbHistoryTwoYears = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_history_2years), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.ctb_no), App.HORIZONTAL, App.VERTICAL, true);
        closeContactType = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_close_contact_type), getResources().getStringArray(R.array.ctb_close_contact_type_list), null, App.VERTICAL, App.VERTICAL);
        otherContactType = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_contact), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        presumptiveTb = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_presumptive_tb), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);


        views = new View[]{formDate.getButton(), screeningLocation.getRadioGroup(), hospital.getSpinner(), facility_section.getSpinner(), facility_section_other.getEditText(), opd_ward_section.getSpinner(), motherName.getEditText(), fatherName.getEditText(), patientAttendant.getRadioGroup(),
                cough.getRadioGroup(), coughDuration.getSpinner(), fever.getRadioGroup(), nightSweats.getRadioGroup(), weightLoss.getRadioGroup(), appeptite.getRadioGroup(),
                lymphnodeSwelling.getRadioGroup(), jointSwellingTwoWeeks.getRadioGroup(), tbHistory.getRadioGroup(), tbMedication.getRadioGroup(), contactTbHistoryTwoYears.getRadioGroup(),
                closeContactType, otherContactType.getEditText(), presumptiveTb.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, screeningLocation, hospital, facility_section, facility_section_other, opd_ward_section, motherName, fatherName,
                        patientAttendant, cough, coughDuration, fever, nightSweats, weightLoss, appeptite, lymphnodeSwelling, jointSwellingTwoWeeks, tbHistory,
                        tbMedication, contactTbHistoryTwoYears, closeContactType, otherContactType, presumptiveTb}};

        formDate.getButton().setOnClickListener(this);
        screeningLocation.getRadioGroup().setOnCheckedChangeListener(this);
        hospital.getSpinner().setOnItemSelectedListener(this);
        facility_section.getSpinner().setOnItemSelectedListener(this);
        opd_ward_section.getSpinner().setOnItemSelectedListener(this);
        patientAttendant.getRadioGroup().setOnCheckedChangeListener(this);
        cough.getRadioGroup().setOnCheckedChangeListener(this);
        coughDuration.getSpinner().setOnItemSelectedListener(this);
        fever.getRadioGroup().setOnCheckedChangeListener(this);
        nightSweats.getRadioGroup().setOnCheckedChangeListener(this);
        weightLoss.getRadioGroup().setOnCheckedChangeListener(this);
        appeptite.getRadioGroup().setOnCheckedChangeListener(this);
        lymphnodeSwelling.getRadioGroup().setOnCheckedChangeListener(this);
        jointSwellingTwoWeeks.getRadioGroup().setOnCheckedChangeListener(this);
        tbHistory.getRadioGroup().setOnCheckedChangeListener(this);
        tbMedication.getRadioGroup().setOnCheckedChangeListener(this);
        contactTbHistoryTwoYears.getRadioGroup().setOnCheckedChangeListener(this);
        presumptiveTb.getRadioGroup().setOnCheckedChangeListener(this);

        ArrayList<MyCheckBox> checkBoxList = closeContactType.getCheckedBoxes();
        for (CheckBox cb : closeContactType.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        resetViews();

    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();


            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            }
            else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            }
 else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }


    }

    @Override
    public boolean validate() {
        if (App.getPatient().getPerson().getAge() > 15) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.ctb_age_greater_than_15));
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
        } else {

            boolean error = false;
            View view = null;
            Boolean flag = false;
            if (otherContactType.getVisibility() == View.VISIBLE && App.get(otherContactType).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherContactType.getEditText().setError(getString(R.string.empty_field));
                otherContactType.getEditText().requestFocus();
                error = true;
                view = null;
            }
            if (closeContactType.getVisibility() == View.VISIBLE) {
                for (CheckBox cb : closeContactType.getCheckedBoxes()) {
                    if (cb.isChecked()) {
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag && closeContactType.getVisibility() == View.VISIBLE) {
                closeContactType.getQuestionView().setError(getString(R.string.empty_field));
                view = closeContactType;
                error = true;
            }
            flag = false;
            if (App.get(fatherName).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                fatherName.getEditText().setError(getString(R.string.empty_field));
                fatherName.getEditText().requestFocus();
                error = true;
                view = null;
            }
            if (App.get(motherName).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                motherName.getEditText().setError(getString(R.string.empty_field));
                motherName.getEditText().requestFocus();
                view = null;
                error = true;
            }
            if (facility_section_other.getVisibility() == View.VISIBLE && App.get(facility_section_other).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                facility_section_other.getEditText().setError(getString(R.string.empty_field));
                facility_section_other.getEditText().requestFocus();
                error = true;
                view = null;
            }
            if (error) {

                int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

                final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();

                alertDialog.setMessage(getString(R.string.form_error));
                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                DrawableCompat.setTint(clearIcon, color);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_error));
                final View finalView = view;
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                scrollView.post(new Runnable() {
                                    public void run() {
                                        if (finalView != null) {
                                            scrollView.scrollTo(0, finalView.getTop());
                                            otherContactType.clearFocus();
                                            facility_section_other.clearFocus();
                                            motherName.clearFocus();
                                            fatherName.clearFocus();
                                        }
                                    }
                                });
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
    }

    @Override
    public boolean submit() {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
            }
            bundle.putBoolean("save", false);
        }
        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});
        observations.add(new String[]{"SCREENING LOCATION", App.get(screeningLocation).toUpperCase()});

        if(hospital.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"HOSPITAL", App.get(hospital)});
        }
        if (facility_section.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"HOSPITAL SECTION", App.get(facility_section).equals(getResources().getString(R.string.ctb_opd_clinic)) ? "OPD CLINIC SCREENING" :
                    (App.get(facility_section).equals(getResources().getString(R.string.ctb_ward)) ? "WARD SCREENING" :
                            (App.get(facility_section).equals(getResources().getString(R.string.ctb_registration_desk)) ? "REGISTRATION DESK" :
                                    (App.get(facility_section).equals(getResources().getString(R.string.ctb_xray_van)) ? "X-RAY VAN" : "OTHER FACILITY SECTION")))});
        }

        if (facility_section_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FACILITY SECTION", App.get(facility_section_other)});

        if (opd_ward_section.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OUTPATIENT DEPARTMENT", App.get(opd_ward_section).equals(getResources().getString(R.string.ctb_general_medicine_filter_clinic)) ? "GENERAL MEDICINE DEPARTMENT" :
                    (App.get(opd_ward_section).equals(getResources().getString(R.string.ctb_chest_tb_clinic_screening)) ? "CHEST MEDICINE DEPARTMENT" :
                            (App.get(opd_ward_section).equals(getResources().getString(R.string.ctb_paediatrics)) ? "PEDIATRIC SURGERY DEPARTMENT" :
                                    (App.get(opd_ward_section).equals(getResources().getString(R.string.ctb_gynae_obstetrics)) ? "OBSTETRICS AND GYNECOLOGY DEPARTMENT" :
                                            (App.get(opd_ward_section).equals(getResources().getString(R.string.ctb_er)) ? "EMERGENCY DEPARTMENT": "SURGICAL PROCEDURE"))))});
        final String fatherNameString = App.get(fatherName);
        observations.add(new String[]{"FATHER NAME", fatherNameString});
        final String motherNameString = App.get(motherName);
        observations.add(new String[]{"MOTHER NAME", motherNameString});

        observations.add(new String[]{"PERSON ATTENDING FACILITY", App.get(patientAttendant).equals(getResources().getString(R.string.ctb_patient)) ? "SELF" : "ATTENDANT" });
        observations.add(new String[]{"COUGH", App.get(cough).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(cough).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(cough).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        if (coughDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUGH DURATION", App.get(coughDuration).equals(getResources().getString(R.string.ctb_less_than_2_weeks)) ? "COUGH LASTING LESS THAN 2 WEEKS" :
                    (App.get(coughDuration).equals(getResources().getString(R.string.ctb_2_to_3_weeks)) ? "COUGH LASTING MORE THAN 2 WEEKS" :
                            (App.get(coughDuration).equals(getResources().getString(R.string.ctb_more_than_3_weeks)) ? "COUGH LASTING MORE THAN 3 WEEKS" :
                                    (App.get(coughDuration).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSED")))});

        observations.add(new String[]{"FEVER", App.get(fever).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(fever).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(fever).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        observations.add(new String[]{"NIGHT SWEATS", App.get(nightSweats).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(nightSweats).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(nightSweats).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        observations.add(new String[]{"WEIGHT LOSS", App.get(weightLoss).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(weightLoss).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(weightLoss).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        observations.add(new String[]{"APPETITE", App.get(appeptite).equals(getResources().getString(R.string.ctb_poor)) ? "POOR APPETITE" :
                (App.get(appeptite).equals(getResources().getString(R.string.ctb_ok)) ? "OK" :
                        (App.get(appeptite).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        observations.add(new String[]{"LYMPH NODE SWELLING GREATER THAN 2 WEEKS", App.get(lymphnodeSwelling).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(lymphnodeSwelling).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(lymphnodeSwelling).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        observations.add(new String[]{"JOINT OR SPINE SWELLING GREATER THAN 2 WEEKS", App.get(jointSwellingTwoWeeks).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(jointSwellingTwoWeeks).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(jointSwellingTwoWeeks).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        observations.add(new String[]{"HISTORY OF TUBERCULOSIS", App.get(tbHistory).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(tbHistory).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(tbHistory).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        if(tbMedication.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"PATIENT TAKEN TB MEDICATION BEFORE", App.get(tbMedication).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(tbMedication).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(tbMedication).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        }
        observations.add(new String[]{"PATIENT IS CONTACT OF KNOWN OR SUSPECTED SUSPICIOUS CASE IN PAST 2 YEARS", App.get(contactTbHistoryTwoYears).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(contactTbHistoryTwoYears).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(contactTbHistoryTwoYears).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        String closeContactString = "";
        for (CheckBox cb : closeContactType.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_mother)))
                closeContactString = closeContactString + "MOTHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_father)))
                closeContactString = closeContactString + "FATHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_brother)))
                closeContactString = closeContactString + "BROTHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_sister)))
                closeContactString = closeContactString + "SISTER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_paternal_grandfather)))
                closeContactString = closeContactString + "PATERNAL GRANDFATHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_paternal_grandmother)))
                closeContactString = closeContactString + "PATERNAL GRANDMOTHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_maternal_grandfather)))
                closeContactString = closeContactString + "MATERNAL GRANDFATHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_maternal_grandmother)))
                closeContactString = closeContactString + "MATERNAL GRANDMOTHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_other_title)))
                closeContactString = closeContactString + "OTHER CONTACT TYPE" + " ; ";
        }
        observations.add(new String[]{"CLOSE CONTACT WITH PATIENT", closeContactString});

        if (otherContactType.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"OTHER CONTACT TYPE", App.get(otherContactType)});
        }

        observations.add(new String[]{"PRESUMPTIVE TUBERCULOSIS", App.get(presumptiveTb).toUpperCase()});

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

                String result = serverService.saveEncounterAndObservation("Verbal Screening", FORM, formDateCalendar, observations.toArray(new String[][]{}));
                if (!result.contains("SUCCESS"))
                    return result;
                else {

                    String encounterId = "";

                    if (result.contains("_")) {
                        String[] successArray = result.split("_");
                        encounterId = successArray[1];
                    }


                    //String[][] concept = serverService.getConceptUuidAndDataType(fatherNameString);
                    result = serverService.savePersonAttributeType("Guardian Name", fatherNameString, encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    //concept = serverService.getConceptUuidAndDataType(motherNameString);
                    result = serverService.savePersonAttributeType("Mother Name", motherNameString, encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                }
                return "SUCCESS";
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

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));

        serverService.saveFormLocally(FORM_NAME, FORM, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int formId) {

        OfflineForm fo = serverService.getOfflineFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("SCREENING LOCATION")) {
                for (RadioButton rb : screeningLocation.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_hospital)) && obs[0][1].equals("HOSPITAL")) {
                        rb.setChecked(true);
                        hospital.setVisibility(View.VISIBLE);
                        facility_section.setVisibility(View.VISIBLE);
                        opd_ward_section.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_community)) && obs[0][1].equals("COMMUNITY")) {
                        rb.setChecked(true);
                        hospital.setVisibility(View.GONE);
                        facility_section.setVisibility(View.GONE);
                        opd_ward_section.setVisibility(View.GONE);
                        break;
                    }
                }
            } else if (obs[0][0].equals("HOSPITAL")) {
                String value = obs[0][1].equals("Jinnah Hospital") ? getResources().getString(R.string.ctb_jinnah_hospital) : "Indus Hospital";
                hospital.getSpinner().selectValue(value);

            } else if (obs[0][0].equals("HOSPITAL SECTION")) {
                String value = obs[0][1].equals("OPD CLINIC SCREENING") ? getResources().getString(R.string.ctb_opd_clinic) :
                        (obs[0][1].equals("WARD SCREENING") ? getResources().getString(R.string.ctb_ward) :
                                (obs[0][1].equals("REGISTRATION DESK") ? getResources().getString(R.string.ctb_registration_desk) :
                                        (obs[0][1].equals("X-RAY VAN") ? getResources().getString(R.string.ctb_xray_van) :
                                                getResources().getString(R.string.ctb_other_title))));
                if (value == getResources().getString(R.string.ctb_other_title))
                    facility_section_other.setVisibility(View.VISIBLE);
                facility_section.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("OTHER FACILITY SECTION")) {
                facility_section_other.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("OUTPATIENT DEPARTMENT")) {
                String value = obs[0][1].equals("GENERAL MEDICINE DEPARTMENT") ? getResources().getString(R.string.ctb_general_medicine_filter_clinic) :
                        (obs[0][1].equals("CHEST MEDICINE DEPARTMENT") ? getResources().getString(R.string.ctb_chest_tb_clinic_screening) :
                                (obs[0][1].equals("PEDIATRIC SURGERY DEPARTMENT") ? getResources().getString(R.string.ctb_paediatrics) :
                                        (obs[0][1].equals("OBSTETRICS AND GYNECOLOGY DEPARTMENT") ? getResources().getString(R.string.ctb_gynae_obstetrics) :
                                                (obs[0][1].equals("EMERGENCY DEPARTMENT") ? getResources().getString(R.string.ctb_er) :
                                                        getResources().getString(R.string.ctb_surgery)))));
                opd_ward_section.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("FATHER NAME")) {
                fatherName.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("MOTHER NAME")) {
                motherName.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUGH")) {
                for (RadioButton rb : cough.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        coughDuration.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUGH DURATION")) {
                String value = obs[0][1].equals("COUGH LASTING LESS THAN 2 WEEKS") ? getResources().getString(R.string.ctb_less_than_2_weeks) :
                        (obs[0][1].equals("COUGH LASTING MORE THAN 2 WEEKS") ? getResources().getString(R.string.ctb_2_to_3_weeks) :
                                (obs[0][1].equals("COUGH LASTING MORE THAN 3 WEEKS") ? getResources().getString(R.string.ctb_more_than_3_weeks) :
                                        (obs[0][1].equals("UNKNOWN") ? getResources().getString(R.string.unknown) :
                                                getResources().getString(R.string.ctb_refused))));
                coughDuration.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("FEVER")) {
                for (RadioButton rb : fever.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("NIGHT SWEATS")) {
                for (RadioButton rb : nightSweats.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("WEIGHT LOSS")) {
                for (RadioButton rb : weightLoss.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("APPETITE")) {
                for (RadioButton rb : appeptite.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_poor)) && obs[0][1].equals("POOR APPETITE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_ok)) && obs[0][1].equals("OK")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("LYMPH NODE SWELLING GREATER THAN 2 WEEKS")) {
                for (RadioButton rb : lymphnodeSwelling.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("JOINT OR SPINE SWELLING GREATER THAN 2 WEEKS")) {
                for (RadioButton rb : jointSwellingTwoWeeks.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("HISTORY OF TUBERCULOSIS")) {
                for (RadioButton rb : tbHistory.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        tbMedication.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("PATIENT TAKEN TB MEDICATION BEFORE")) {
                for (RadioButton rb : tbMedication.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("PATIENT IS CONTACT OF KNOWN OR SUSPECTED SUSPICIOUS CASE IN PAST 2 YEARS")) {
                for (RadioButton rb : contactTbHistoryTwoYears.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        closeContactType.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("CLOSE CONTACT WITH PATIENT")) {
                for (CheckBox cb : closeContactType.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ctb_mother)) && obs[0][1].equals("MOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_father)) && obs[0][1].equals("FATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_brother)) && obs[0][1].equals("BROTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_sister)) && obs[0][1].equals("SISTER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_paternal_grandfather)) && obs[0][1].equals("PATERNAL GRANDFATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_paternal_grandmother)) && obs[0][1].equals("PATERNAL GRANDMOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_maternal_grandfather)) && obs[0][1].equals("MATERNAL GRANDFATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_maternal_grandmother)) && obs[0][1].equals("MATERNAL GRANDMOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_other_title)) && obs[0][1].equals("OTHER CONTACT TYPE")) {
                        cb.setChecked(true);
                        otherContactType.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
            else if (obs[0][0].equals("OTHER CONTACT TYPE")) {
                otherContactType.getEditText().setText(obs[0][1]);
            }
            else if(obs[0][0].equals("PRESUMPTIVE TUBERCULOSIS")) {
                for (RadioButton rb : presumptiveTb.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }

        }
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
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == facility_section.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                facility_section_other.setVisibility(View.VISIBLE);
            } else {
                facility_section_other.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_opd_clinic)) || parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_ward))) {
                opd_ward_section.setVisibility(View.VISIBLE);
            } else {
                opd_ward_section.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        for (CheckBox cb : closeContactType.getCheckedBoxes()) {
            if (App.get(cb).equals(getResources().getString(R.string.ctb_mother))) {
                if (cb.isChecked()) {
                    for (RadioButton rb : presumptiveTb.getRadioGroup().getButtons()) {
                        String str = rb.getText().toString();
                        if (str.equals(getResources().getString(R.string.yes)))
                            rb.setChecked(true);
                        else
                            rb.setChecked(false);
                    }
                }
            }
            if (App.get(cb).equals(getResources().getString(R.string.ctb_other_title))) {
                if (cb.isChecked()) {
                    otherContactType.setVisibility(View.VISIBLE);
                } else {
                    otherContactType.setVisibility(View.GONE);
                }
            }

        }
    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        facility_section_other.setVisibility(View.GONE);
        opd_ward_section.setVisibility(View.GONE);
        coughDuration.setVisibility(View.GONE);
        tbMedication.setVisibility(View.GONE);
        closeContactType.setVisibility(View.GONE);
        otherContactType.setVisibility(View.GONE);
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == screeningLocation.getRadioGroup()) {
            if (screeningLocation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_hospital))) {
                hospital.setVisibility(View.VISIBLE);
                facility_section.setVisibility(View.VISIBLE);
                if(App.get(facility_section).equals(getResources().getString(R.string.ctb_opd_clinic)) || App.get(facility_section).equals(getResources().getString(R.string.ctb_ward))) {
                    opd_ward_section.setVisibility(View.VISIBLE);
                }
                else if(App.get(facility_section).equals(getResources().getString(R.string.ctb_other_title))){
                    facility_section_other.setVisibility(View.VISIBLE);
                }
            } else {
                opd_ward_section.setVisibility(View.GONE);
                hospital.setVisibility(View.GONE);
                facility_section.setVisibility(View.GONE);
                facility_section_other.setVisibility(View.GONE);
            }
        } else if (group == cough.getRadioGroup()) {
            if (cough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                coughDuration.setVisibility(View.VISIBLE);
            } else {
                coughDuration.setVisibility(View.GONE);
            }
        } else if (group == lymphnodeSwelling.getRadioGroup()) {
            if (App.get(lymphnodeSwelling).equals(getResources().getString(R.string.yes))) {
                for (RadioButton rb : presumptiveTb.getRadioGroup().getButtons()) {
                    String str = rb.getText().toString();
                    if (str.equals(getResources().getString(R.string.yes)))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
            }
        } else if (group == jointSwellingTwoWeeks.getRadioGroup()) {
            if (App.get(jointSwellingTwoWeeks).equals(getResources().getString(R.string.yes))) {
                for (RadioButton rb : presumptiveTb.getRadioGroup().getButtons()) {
                    String str = rb.getText().toString();
                    if (str.equals(getResources().getString(R.string.yes)))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
            }
        } else if (group == tbHistory.getRadioGroup()) {
            if (App.get(tbHistory).equals(getResources().getString(R.string.yes))) {
                tbMedication.setVisibility(View.VISIBLE);
            } else {
                tbMedication.setVisibility(View.GONE);
            }
        } else if (group == contactTbHistoryTwoYears.getRadioGroup()) {
            if (App.get(contactTbHistoryTwoYears).equals(getResources().getString(R.string.yes))) {
                closeContactType.setVisibility(View.VISIBLE);
            } else {
                closeContactType.setVisibility(View.GONE);
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
