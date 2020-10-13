package com.ihsinformatics.gfatmmobile.childhoodtb;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
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
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyCheckBox;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
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

    TitledRadioGroup screeningLocation;
    TitledEditText otherScreeningLocation;
    TitledSpinner hospital;
    TitledSpinner facility_section;
    TitledEditText facility_section_other;
    TitledSpinner opd_ward_section;
    TitledEditText motherName;
    TitledEditText fatherName;
    TitledRadioGroup patientAttendant;
    MyTextView tbSymptomsInstructions;
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
    MyTextView verbalScreeningInstruction;
    TitledEditText otherContactType;
    TitledRadioGroup presumptiveTb;
    MyTextView screenerInstruction;

    Snackbar snackbar;
    ScrollView scrollView;

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

        pageCount = 1;
        formName = Forms.CHILDHOODTB_VERBAL_SCREENING;
        form = Forms.childhoodTb_verbalScreeningForm;

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
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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
        if (App.getPatient().getPerson().getAge() > 18) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(),R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.ctb_age_greater_than_18));
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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        screeningLocation = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_screening_location), getResources().getStringArray(R.array.ctb_screening_location_list), getResources().getString(R.string.ctb_hospital), App.HORIZONTAL, App.VERTICAL, true,"SCREENING LOCATION",new String[]{ "HOSPITAL" , "COMMUNITY" , "SCHOOL" , "OTHER SCREENING LOCATION"});
        otherScreeningLocation = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false,"OTHER SCREENING LOCATION");

        String columnName = "childhood_tb_location";

        final Object[][] locations = serverService.getAllLocationsFromLocalDB(columnName);
        String[] locationArray = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            Object objLoc = locations[i][1];
            locationArray[i] = objLoc.toString();
        }
        hospital = new TitledSpinner(context, null, getResources().getString(R.string.ctb_hospital_specify), locationArray, null, App.VERTICAL,true,"HOSPITAL",locationArray);
        facility_section = new TitledSpinner(context, null, getResources().getString(R.string.ctb_facility_section), getResources().getStringArray(R.array.ctb_facility_section_list), getResources().getString(R.string.ctb_empty), App.VERTICAL,true,"HOSPITAL SECTION",new String[]{"", "OPD CLINIC SCREENING" , "WARD SCREENING" , "REGISTRATION DESK" , "X-RAY VAN" , "OTHER FACILITY SECTION"});
        facility_section_other = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false,"OTHER FACILITY SECTION");
        opd_ward_section = new TitledSpinner(context, null, getResources().getString(R.string.ctb_opd_clinic_or_ward), getResources().getStringArray(R.array.ctb_opd_ward_section_list), getResources().getString(R.string.ctb_empty), App.VERTICAL,true,"OUTPATIENT DEPARTMENT",new String[]{"", "PAEDIATRICS DEPARTMENT" , "GENERAL MEDICINE DEPARTMENT" , "CHEST MEDICINE DEPARTMENT" , "OBSTETRICS AND GYNECOLOGY DEPARTMENT" , "SURGICAL PROCEDURE", "EMERGENCY DEPARTMENT"});
        motherName = new TitledEditText(context, null, getResources().getString(R.string.ctb_mother_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false,"MOTHER NAME");
        fatherName = new TitledEditText(context, null, getResources().getString(R.string.ctb_father_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"FATHER NAME");
        patientAttendant = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_patient_attendant), getResources().getStringArray(R.array.ctb_patient_attendant_list), null, App.HORIZONTAL, App.VERTICAL, true,"PERSON ATTENDING FACILITY", new String[]{"SELF", "ATTENDANT"});
        tbSymptomsInstructions = new MyTextView(context,getResources().getString(R.string.ctb_tb_symptoms_instructions));
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_cough), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true,"COUGH",new String[]{ "YES" , "NO" , "REFUSED" , "UNKNOWN"});
        coughDuration = new TitledSpinner(context, null, getResources().getString(R.string.ctb_cough_duration), getResources().getStringArray(R.array.ctb_cough_duration_list), null, App.VERTICAL, true,"COUGH DURATION",new String[]{ "","COUGH LASTING LESS THAN 2 WEEKS" , "COUGH LASTING MORE THAN 2 WEEKS" , "COUGH LASTING MORE THAN 3 WEEKS" , "UNKNOWN" , "REFUSED"});
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_fever_more_than_2_weeks), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true,"FEVER",new String[]{ "YES" , "NO" , "REFUSED" , "UNKNOWN"});
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_night_sweats), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true,"NIGHT SWEATS",new String[]{ "YES" , "NO" , "REFUSED" , "UNKNOWN"});
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_weight_loss), getResources().getStringArray(R.array.yes_no_unknown_refused_options),null, App.HORIZONTAL, App.VERTICAL, true,"WEIGHT LOSS",new String[]{ "YES" , "NO" , "REFUSED" , "UNKNOWN"});
        appeptite = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_child_appetite), getResources().getStringArray(R.array.ctb_appetite_list), null, App.HORIZONTAL, App.VERTICAL, true,"APPETITE",new String[]{ "YES" , "NO" , "REFUSED" , "UNKNOWN"});
        lymphnodeSwelling = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_lymph_node_swelling), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true,"LYMPH NODE SWELLING GREATER THAN 2 WEEKS",new String[]{ "YES" , "NO" , "REFUSED" , "UNKNOWN"});
        jointSwellingTwoWeeks = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_joint_spine_swelling), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true,"JOINT OR SPINE SWELLING GREATER THAN 2 WEEKS",new String[]{ "YES" , "NO" , "REFUSED" , "UNKNOWN"});
        tbHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_before), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true,"HISTORY OF TUBERCULOSIS",new String[]{ "YES" , "NO" , "REFUSED" , "UNKNOWN"});
        tbMedication = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_medication), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true,"PATIENT TAKEN TB MEDICATION BEFORE",new String[]{ "YES" , "NO" , "REFUSED" , "UNKNOWN"});
        contactTbHistoryTwoYears = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_history_2years), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true,"PATIENT IS CONTACT OF KNOWN OR SUSPECTED SUSPICIOUS CASE IN PAST 2 YEARS",new String[]{ "YES" , "NO" , "REFUSED" , "UNKNOWN"});
        closeContactType = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_close_contact_type), getResources().getStringArray(R.array.ctb_close_contact_type_list), null, App.VERTICAL, App.VERTICAL,true, "CLOSE CONTACT WITH PATIENT", new String[]{"SELF", "MOTHER", "FATHER", "BROTHER", "SISTER", "PATERNAL GRANDFATHER", "PATERNAL GRANDMOTHER", "MATERNAL GRANDFATHER", "MATERNAL GRANDMOTHER", "UNCLE", "AUNT", "OTHER CONTACT TYPE"});
        otherContactType = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_contact), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false,"OTHER CONTACT TYPE");
        verbalScreeningInstruction = new MyTextView(context,getResources().getString(R.string.ctb_verbal_screening_instruction));
        presumptiveTb = new TitledRadioGroup(context, "TB PRESUMPTIVE", getResources().getString(R.string.ctb_presumptive_tb), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true,"PRESUMPTIVE TUBERCULOSIS",getResources().getStringArray(R.array.yes_no_list_concept));
        screenerInstruction = new MyTextView(context,getResources().getString(R.string.ctb_screener_instruction));

        views = new View[]{formDate.getButton(), screeningLocation.getRadioGroup(), otherScreeningLocation.getEditText(),hospital.getSpinner(), facility_section.getSpinner(), facility_section_other.getEditText(), opd_ward_section.getSpinner(), motherName.getEditText(), fatherName.getEditText(), patientAttendant.getRadioGroup(),
                cough.getRadioGroup(), coughDuration.getSpinner(), fever.getRadioGroup(), nightSweats.getRadioGroup(), weightLoss.getRadioGroup(), appeptite.getRadioGroup(),
                lymphnodeSwelling.getRadioGroup(), jointSwellingTwoWeeks.getRadioGroup(), tbHistory.getRadioGroup(), tbMedication.getRadioGroup(), contactTbHistoryTwoYears.getRadioGroup(),
                closeContactType, otherContactType.getEditText(), presumptiveTb.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, screeningLocation, otherScreeningLocation, hospital, facility_section, facility_section_other, opd_ward_section, motherName, fatherName,
                        patientAttendant, tbSymptomsInstructions,cough, coughDuration, fever, nightSweats, weightLoss, appeptite, lymphnodeSwelling, jointSwellingTwoWeeks, tbHistory,
                        tbMedication, contactTbHistoryTwoYears, closeContactType, otherContactType, verbalScreeningInstruction,presumptiveTb,screenerInstruction}};

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

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();


            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            }
            else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            }
 else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }
        formDate.getButton().setEnabled(true);

    }

    @Override
    public boolean validate() {
        if (App.getPatient().getPerson().getAge() > 18) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(),R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.ctb_age_greater_than_18));
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

            boolean error = super.validate();
            View view = null;
            Boolean flag = false;


            if (otherContactType.getVisibility() == View.VISIBLE && App.get(otherContactType).trim().length() <= 0){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherContactType.getEditText().setError(getString(R.string.ctb_spaces_only));
                otherContactType.getEditText().requestFocus();
                error = true;
                view = null;
            }


             if(!App.get(fatherName).isEmpty() && App.get(fatherName).trim().length() <= 0){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                fatherName.getEditText().setError(getString(R.string.ctb_spaces_only));
                fatherName.getEditText().requestFocus();
                error = true;
                view = null;
            }else if(App.get(fatherName).length() <3){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                fatherName.getEditText().setError(getString(R.string.ctb_length_less_than_3));
                fatherName.getEditText().requestFocus();
                error = true;
                view = null;
            }
            if(!App.get(motherName).isEmpty()) {
                if (App.get(motherName).trim().length() <= 0) {
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    motherName.getEditText().setError(getString(R.string.ctb_spaces_only));
                    motherName.getEditText().requestFocus();
                    error = true;
                    view = null;
                } else if (App.get(motherName).length() < 3) {
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    motherName.getEditText().setError(getString(R.string.ctb_length_less_than_3));
                    motherName.getEditText().requestFocus();
                    error = true;
                    view = null;
                }
            }

            if(facility_section_other.getVisibility() == View.VISIBLE && App.get(facility_section_other).trim().length() <= 0) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                facility_section_other.getEditText().setError(getString(R.string.ctb_spaces_only));
                facility_section_other.getEditText().requestFocus();
                error = true;
            }


                if (error) {

                int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

                final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(),R.style.dialog).create();

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

        final HashMap<String, String> personAttribute = new HashMap<String, String>();
        final ArrayList<String[]> observations = getObservations();

        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                Boolean flag = serverService.deleteOfflineForms(encounterId);
                if(!flag){

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
                observations.add(new String[]{"TIME TAKEN TO FILL FORM", timeTakeToFill});
            }else {
                endTime = new Date();
                observations.add(new String[]{"TIME TAKEN TO FILL FORM", String.valueOf(App.getTimeDurationBetween(startTime, endTime))});
            }
            bundle.putBoolean("save", false);
        } else {
            endTime = new Date();
            observations.add(new String[]{"TIME TAKEN TO FILL FORM", String.valueOf(App.getTimeDurationBetween(startTime, endTime))});
        }

        personAttribute.put("Guardian Name", App.get(fatherName));

        personAttribute.put("Mother Name", App.get(motherName));

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

                String id = null;
                if(App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocally("Childhood TB-Verbal Screening", form, formDateCalendar,observations.toArray(new String[][]{}));

                String result = "";

                result = serverService.saveProgramEnrollement(App.getSqlDate(formDateCalendar), "ChildhoodTB", id);
                if (!result.equals("SUCCESS"))
                    return result;

                result = serverService.saveEncounterAndObservationTesting("Childhood TB-Verbal Screening", form, formDateCalendar, observations.toArray(new String[][]{}),id);
                if (!result.contains("SUCCESS"))
                    return result;

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

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));

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
            showDateDialog(formDateCalendar,false,true, false);
            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");*/
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
            facility_section.getQuestionView().setError(null);
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
        if (spinner == opd_ward_section.getSpinner()) {
            opd_ward_section.getQuestionView().setError(null);
        }
        if (spinner == coughDuration.getSpinner()) {
            coughDuration.getQuestionView().setError(null);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        closeContactType.getQuestionView().setError(null);
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

        hospital.getSpinner().selectValue(App.getLocation());
        hospital.getSpinner().setEnabled(false);
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        hospital.setVisibility(View.VISIBLE);
        facility_section.setVisibility(View.VISIBLE);
        if(App.get(facility_section).equals(getResources().getString(R.string.ctb_opd_clinic)) || App.get(facility_section).equals(getResources().getString(R.string.ctb_ward))) {
            opd_ward_section.setVisibility(View.VISIBLE);
        }
        else if(App.get(facility_section).equals(getResources().getString(R.string.ctb_other_title))){
            facility_section_other.setVisibility(View.VISIBLE);
        }
        coughDuration.setVisibility(View.GONE);
        tbMedication.setVisibility(View.GONE);
        closeContactType.setVisibility(View.GONE);
        otherContactType.setVisibility(View.GONE);
        presumptiveTb.getRadioGroup().setEnabled(false);
        for (RadioButton rb : presumptiveTb.getRadioGroup().getButtons()) {
            rb.setClickable(false);
        }
        setPresumptiveTb();
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
            screeningLocation.getQuestionView().setError(null);
            if (screeningLocation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_hospital))) {
                hospital.setVisibility(View.VISIBLE);
                facility_section.setVisibility(View.VISIBLE);
                if(App.get(facility_section).equals(getResources().getString(R.string.ctb_opd_clinic)) || App.get(facility_section).equals(getResources().getString(R.string.ctb_ward))) {
                    opd_ward_section.setVisibility(View.VISIBLE);
                }
                else if(App.get(facility_section).equals(getResources().getString(R.string.ctb_other_title))){
                    facility_section_other.setVisibility(View.VISIBLE);
                }
            }
            else if (screeningLocation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_other_title))) {
                opd_ward_section.setVisibility(View.GONE);
                hospital.setVisibility(View.GONE);
                facility_section.setVisibility(View.GONE);
                facility_section_other.setVisibility(View.GONE);

                otherScreeningLocation.setVisibility(View.VISIBLE);
            }
            else {
                opd_ward_section.setVisibility(View.GONE);
                hospital.setVisibility(View.GONE);
                facility_section.setVisibility(View.GONE);
                facility_section_other.setVisibility(View.GONE);

                otherScreeningLocation.setVisibility(View.GONE);
            }
        } else if (group == cough.getRadioGroup()) {
            cough.getQuestionView().setError(null);
            setPresumptiveTb();
            if (cough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                coughDuration.setVisibility(View.VISIBLE);
            } else {
                coughDuration.setVisibility(View.GONE);
            }
        } else if (group == lymphnodeSwelling.getRadioGroup()) {
            lymphnodeSwelling.getQuestionView().setError(null);
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
            jointSwellingTwoWeeks.getQuestionView().setError(null);
            if (App.get(jointSwellingTwoWeeks).equals(getResources().getString(R.string.yes))) {
                for (RadioButton rb : presumptiveTb.getRadioGroup().getButtons()) {
                    String str = rb.getText().toString();
                    if (str.equals(getResources().getString(R.string.yes)))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
            }
        }
        else if (group == fever.getRadioGroup()) {
            fever.getQuestionView().setError(null);
            setPresumptiveTb();
        }
        else if (group == nightSweats.getRadioGroup()) {
            nightSweats.getQuestionView().setError(null);
            setPresumptiveTb();
        }
        else if (group == weightLoss.getRadioGroup()) {
            weightLoss.getQuestionView().setError(null);
            setPresumptiveTb();
        }
        else if (group == appeptite.getRadioGroup()) {
            appeptite.getQuestionView().setError(null);
            setPresumptiveTb();
        }
        else if (group == tbHistory.getRadioGroup()) {
            tbHistory.getQuestionView().setError(null);
            setPresumptiveTb();
            if (App.get(tbHistory).equals(getResources().getString(R.string.yes))) {
                tbMedication.setVisibility(View.VISIBLE);
            } else {
                tbMedication.setVisibility(View.GONE);
            }
        } else if (group == contactTbHistoryTwoYears.getRadioGroup()) {
            contactTbHistoryTwoYears.getQuestionView().setError(null);
            if (App.get(contactTbHistoryTwoYears).equals(getResources().getString(R.string.yes))) {
                closeContactType.setVisibility(View.VISIBLE);
            } else {
                closeContactType.setVisibility(View.GONE);
                otherContactType.setVisibility(View.GONE);
            }
        }
        else if (group == patientAttendant.getRadioGroup()) {
            patientAttendant.getQuestionView().setError(null);
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

    public void setPresumptiveTb(){
        String presumptiveTbArray[] = {App.get(cough),App.get(fever),App.get(nightSweats),App.get(weightLoss),App.get(appeptite),App.get(tbHistory)};
        int sum=0;
        for(int i=0; i<presumptiveTbArray.length; i++){
            if(presumptiveTbArray[i].equalsIgnoreCase(getResources().getString(R.string.yes)) || presumptiveTbArray[i].equalsIgnoreCase(getResources().getString(R.string.ctb_poor))){
                sum++;
            }
        }
        if(sum>=2){
            presumptiveTb.getRadioGroup().getButtons().get(0).setChecked(true);
        }else{
            presumptiveTb.getRadioGroup().getButtons().get(1).setChecked(true);
        }
    }
}
