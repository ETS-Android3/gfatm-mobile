package com.ihsinformatics.gfatmmobile.childhoodtb;

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

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
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
        screeningLocation = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_screening_location), getResources().getStringArray(R.array.ctb_screening_location_list), getResources().getString(R.string.ctb_hospital), App.HORIZONTAL, App.VERTICAL, true);
        otherScreeningLocation = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);

        String columnName = "childhood_tb_location";

        final Object[][] locations = serverService.getAllLocationsFromLocalDB(columnName);
        String[] locationArray = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            Object objLoc = locations[i][1];
            locationArray[i] = objLoc.toString();
        }
        hospital = new TitledSpinner(context, null, getResources().getString(R.string.ctb_hospital_specify), locationArray, null, App.VERTICAL);
        facility_section = new TitledSpinner(context, null, getResources().getString(R.string.ctb_facility_section), getResources().getStringArray(R.array.ctb_facility_section_list), getResources().getString(R.string.ctb_empty), App.VERTICAL);
        facility_section_other = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        opd_ward_section = new TitledSpinner(context, null, getResources().getString(R.string.ctb_opd_clinic_or_ward), getResources().getStringArray(R.array.ctb_opd_ward_section_list), getResources().getString(R.string.ctb_empty), App.VERTICAL);
        motherName = new TitledEditText(context, null, getResources().getString(R.string.ctb_mother_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        fatherName = new TitledEditText(context, null, getResources().getString(R.string.ctb_father_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        patientAttendant = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_patient_attendant), getResources().getStringArray(R.array.ctb_patient_attendant_list), null, App.HORIZONTAL, App.VERTICAL, true);
        tbSymptomsInstructions = new MyTextView(context,getResources().getString(R.string.ctb_tb_symptoms_instructions));
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_cough), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true);
        coughDuration = new TitledSpinner(context, null, getResources().getString(R.string.ctb_cough_duration), getResources().getStringArray(R.array.ctb_cough_duration_list), null, App.VERTICAL, true);
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_fever_more_than_2_weeks), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true);
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_night_sweats), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true);
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_weight_loss), getResources().getStringArray(R.array.yes_no_unknown_refused_options),null, App.HORIZONTAL, App.VERTICAL, true);
        appeptite = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_child_appetite), getResources().getStringArray(R.array.ctb_appetite_list), null, App.HORIZONTAL, App.VERTICAL, true);
        lymphnodeSwelling = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_lymph_node_swelling), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true);
        jointSwellingTwoWeeks = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_joint_spine_swelling), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true);
        tbHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_before), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true);
        tbMedication = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_medication), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true);
        contactTbHistoryTwoYears = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_history_2years), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true);
        closeContactType = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_close_contact_type), getResources().getStringArray(R.array.ctb_close_contact_type_list), null, App.VERTICAL, App.VERTICAL);
        otherContactType = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_contact), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        verbalScreeningInstruction = new MyTextView(context,getResources().getString(R.string.ctb_verbal_screening_instruction));
        presumptiveTb = new TitledRadioGroup(context, "TB PRESUMPTIVE", getResources().getString(R.string.ctb_presumptive_tb), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
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
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
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

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
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
            if(App.get(patientAttendant).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                patientAttendant.getQuestionView().setError(getString(R.string.empty_field));
                patientAttendant.getRadioGroup().requestFocus();
                error = true;
                view = patientAttendant;
            }
            if(App.get(cough).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cough.getQuestionView().setError(getString(R.string.empty_field));
                cough.getRadioGroup().requestFocus();
                error = true;
                view = cough;
            }
            if(coughDuration.getVisibility()==View.VISIBLE && App.get(coughDuration).equals(getResources().getString(R.string.ctb_empty))){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                coughDuration.getQuestionView().setError(getString(R.string.empty_field));
                coughDuration.getSpinner().requestFocus();
                error = true;
                view = coughDuration;
            }
            if(App.get(fever).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                fever.getQuestionView().setError(getString(R.string.empty_field));
                fever.getRadioGroup().requestFocus();
                error = true;
                view = coughDuration;
            }
            if(App.get(nightSweats).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                nightSweats.getQuestionView().setError(getString(R.string.empty_field));
                nightSweats.getRadioGroup().requestFocus();
                error = true;
                view = nightSweats;
            }
            if(App.get(weightLoss).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                weightLoss.getQuestionView().setError(getString(R.string.empty_field));
                weightLoss.getRadioGroup().requestFocus();
                error = true;
                view = weightLoss;
            }
            if(App.get(appeptite).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                appeptite.getQuestionView().setError(getString(R.string.empty_field));
                appeptite.getRadioGroup().requestFocus();
                error = true;
                view = weightLoss;
            }
            if(App.get(lymphnodeSwelling).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                lymphnodeSwelling.getQuestionView().setError(getString(R.string.empty_field));
                lymphnodeSwelling.getRadioGroup().requestFocus();
                error = true;
                view = lymphnodeSwelling;
            }
            if(App.get(jointSwellingTwoWeeks).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                jointSwellingTwoWeeks.getQuestionView().setError(getString(R.string.empty_field));
                jointSwellingTwoWeeks.getRadioGroup().requestFocus();
                error = true;
                view = jointSwellingTwoWeeks;
            }
            if(App.get(tbHistory).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                tbHistory.getQuestionView().setError(getString(R.string.empty_field));
                tbHistory.getRadioGroup().requestFocus();
                error = true;
                view = tbHistory;
            }
            if(tbMedication.getVisibility()==View.VISIBLE && App.get(tbMedication).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                tbMedication.getQuestionView().setError(getString(R.string.empty_field));
                tbMedication.getRadioGroup().requestFocus();
                error = true;
                view = tbMedication;
            }
            if(App.get(contactTbHistoryTwoYears).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                contactTbHistoryTwoYears.getQuestionView().setError(getString(R.string.empty_field));
                contactTbHistoryTwoYears.getRadioGroup().requestFocus();
                error = true;
                view = contactTbHistoryTwoYears;
            }
            if(App.get(presumptiveTb).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                presumptiveTb.getQuestionView().setError(getString(R.string.empty_field));
                presumptiveTb.getRadioGroup().requestFocus();
                error = true;
                view = presumptiveTb;
            }

            if (App.get(fatherName).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                fatherName.getEditText().setError(getString(R.string.empty_field));
                fatherName.getEditText().requestFocus();
                error = true;
                view = null;
            }else if(App.get(fatherName).trim().length() <= 0){
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
            if(facility_section_other.getVisibility() == View.VISIBLE && App.get(facility_section_other).trim().length() <= 0) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                facility_section_other.getEditText().setError(getString(R.string.ctb_spaces_only));
                facility_section_other.getEditText().requestFocus();
                error = true;
            }
            if(facility_section.getVisibility()==View.VISIBLE) {
                if (App.get(facility_section).equals(getResources().getString(R.string.ctb_empty))) {
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    facility_section.getQuestionView().setError(getString(R.string.empty_field));
                    facility_section.getSpinner().requestFocus();
                    error = true;
                }
            }
            if(opd_ward_section.getVisibility()==View.VISIBLE && App.get(opd_ward_section).equals(getResources().getString(R.string.ctb_empty))){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                opd_ward_section.getQuestionView().setError(getString(R.string.empty_field));
                opd_ward_section.getSpinner().requestFocus();
                error = true;
            }
            if(otherScreeningLocation.getVisibility()==View.VISIBLE && App.get(otherScreeningLocation).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherScreeningLocation.getEditText().setError(getString(R.string.empty_field));
                otherScreeningLocation.getEditText().requestFocus();
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
        final ArrayList<String[]> observations = new ArrayList<String[]>();

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

        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});

        observations.add(new String[]{"SCREENING LOCATION", App.get(screeningLocation).equals(getResources().getString(R.string.ctb_hospital)) ? "HOSPITAL" :
                (App.get(screeningLocation).equals(getResources().getString(R.string.ctb_community)) ? "COMMUNITY" :
                        (App.get(screeningLocation).equals(getResources().getString(R.string.ctb_school)) ? "SCHOOL" : "OTHER SCREENING LOCATION"))});

        observations.add(new String[]{"OTHER SCREENING LOCATION", App.get(otherScreeningLocation)});

        if(hospital.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"HOSPITAL", App.get(hospital)});
        }
        if (facility_section.getVisibility() == View.VISIBLE && !App.get(facility_section).equals(getResources().getString(R.string.ctb_empty))){
            observations.add(new String[]{"HOSPITAL SECTION", App.get(facility_section).equals(getResources().getString(R.string.ctb_opd_clinic)) ? "OPD CLINIC SCREENING" :
                    (App.get(facility_section).equals(getResources().getString(R.string.ctb_ward)) ? "WARD SCREENING" :
                            (App.get(facility_section).equals(getResources().getString(R.string.ctb_registration_desk)) ? "REGISTRATION DESK" :
                                    (App.get(facility_section).equals(getResources().getString(R.string.ctb_xray_van)) ? "X-RAY VAN" : "OTHER FACILITY SECTION")))});
        }

        if (facility_section_other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FACILITY SECTION", App.get(facility_section_other)});

        if (opd_ward_section.getVisibility() == View.VISIBLE && !App.get(opd_ward_section).equals(getResources().getString(R.string.ctb_empty)))
            observations.add(new String[]{"OUTPATIENT DEPARTMENT", App.get(opd_ward_section).equals(getResources().getString(R.string.ctb_general_medicine_filter_clinic)) ? "GENERAL MEDICINE DEPARTMENT" :
                    (App.get(opd_ward_section).equals(getResources().getString(R.string.ctb_chest_tb_clinic_screening)) ? "CHEST MEDICINE DEPARTMENT" :
                            (App.get(opd_ward_section).equals(getResources().getString(R.string.ctb_paediatrics)) ? "PAEDIATRICS DEPARTMENT" :
                                    (App.get(opd_ward_section).equals(getResources().getString(R.string.ctb_gynae_obstetrics)) ? "OBSTETRICS AND GYNECOLOGY DEPARTMENT" :
                                            (App.get(opd_ward_section).equals(getResources().getString(R.string.ctb_er)) ? "EMERGENCY DEPARTMENT": "SURGICAL PROCEDURE"))))});
        final String fatherNameString = App.get(fatherName);
        observations.add(new String[]{"FATHER NAME", fatherNameString});
        personAttribute.put("Guardian Name",fatherNameString);

        final String motherNameString = App.get(motherName);
        observations.add(new String[]{"MOTHER NAME", motherNameString});
        personAttribute.put("Mother Name",motherNameString);

        observations.add(new String[]{"PERSON ATTENDING FACILITY", App.get(patientAttendant).equals(getResources().getString(R.string.ctb_patient)) ? "SELF" : "ATTENDANT" });
        observations.add(new String[]{"COUGH", App.get(cough).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(cough).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(cough).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        if (coughDuration.getVisibility() == View.VISIBLE && !App.get(coughDuration).equals(getResources().getString(R.string.ctb_empty)))
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
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_uncle)))
                closeContactString = closeContactString + "UNCLE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_aunt)))
                closeContactString = closeContactString + "AUNT" + " ; ";
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

                String id = null;
                if(App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocallyTesting("Childhood TB-Verbal Screening", form, formDateCalendar,observations.toArray(new String[][]{}));

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

        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            }else if (obs[0][0].equals("SCREENING LOCATION")) {
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
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_school)) && obs[0][1].equals("SCHOOL")) {
                        rb.setChecked(true);
                        hospital.setVisibility(View.GONE);
                        facility_section.setVisibility(View.GONE);
                        opd_ward_section.setVisibility(View.GONE);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_other_title)) && obs[0][1].equals("OTHER SCREENING LOCATION")) {
                        rb.setChecked(true);
                        hospital.setVisibility(View.GONE);
                        facility_section.setVisibility(View.GONE);
                        opd_ward_section.setVisibility(View.GONE);
                        otherScreeningLocation.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
            else if (obs[0][0].equals("OTHER SCREENING LOCATION")) {
                otherScreeningLocation.getEditText().setText(obs[0][1]);
            }
            else if (obs[0][0].equals("HOSPITAL")) {
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
                                (obs[0][1].equals("PAEDIATRICS DEPARTMENT") ? getResources().getString(R.string.ctb_paediatrics) :
                                        (obs[0][1].equals("OBSTETRICS AND GYNECOLOGY DEPARTMENT") ? getResources().getString(R.string.ctb_gynae_obstetrics) :
                                                (obs[0][1].equals("EMERGENCY DEPARTMENT") ? getResources().getString(R.string.ctb_er) :
                                                        getResources().getString(R.string.ctb_surgery)))));
                opd_ward_section.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("FATHER NAME")) {
                fatherName.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("MOTHER NAME")) {
                motherName.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("PERSON ATTENDING FACILITY")){
                for (RadioButton rb : patientAttendant.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_patient)) && obs[0][1].equals("SELF")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_attendant)) && obs[0][1].equals("ATTENDANT")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("COUGH")) {
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
            formDate.getButton().setEnabled(false);
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
