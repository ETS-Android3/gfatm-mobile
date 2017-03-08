package com.ihsinformatics.gfatmmobile.fast;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
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
 * Created by Haris on 1/5/2017.
 */

public class FastPresumptiveForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledEditText husbandName;
    TitledEditText fatherName;
    TitledRadioGroup patientAttendant;
    TitledSpinner patientConsultation;
    TitledEditText patientConsultationOther;
    MyTextView patientSymptomTitle;
    MyTextView patientDemographicsTitle;
    TitledRadioGroup cough;
    TitledSpinner coughDuration;
    TitledRadioGroup productiveCough;
    TitledRadioGroup haemoptysis;
    TitledRadioGroup fever;
    TitledSpinner feverDuration;
    TitledRadioGroup nightSweats;
    TitledRadioGroup weightLoss;
    MyTextView patientTbHistoryTitle;
    TitledRadioGroup tbContact;
    TitledRadioGroup tbHistory;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 3;
        FORM_NAME = Forms.FAST_PRESUMPTIVE_FORM;
        FORM = Forms.fastPresumptiveForm;

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
        if (App.getPatient().getPerson().getAge() <= 15) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.fast_patient_age_less_than_15));
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

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        patientDemographicsTitle = new MyTextView(context, getResources().getString(R.string.fast_demographics_title));
        patientDemographicsTitle.setTypeface(null, Typeface.BOLD);
        husbandName = new TitledEditText(context, null, getResources().getString(R.string.fast_husband_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        fatherName = new TitledEditText(context, null, getResources().getString(R.string.fast_father_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        patientAttendant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_is_this_patient_or_attendant), getResources().getStringArray(R.array.fast_patient_or_attendant_list), getResources().getString(R.string.fast_patient_title), App.VERTICAL, App.VERTICAL);
        patientConsultation = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_speciality_patient_consult), getResources().getStringArray(R.array.fast_patient_consultation_list), getResources().getString(R.string.fast_chesttbclinic_title), App.VERTICAL);
        patientConsultationOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        patientSymptomTitle = new MyTextView(context, getResources().getString(R.string.fast_symptoms_title));
        patientSymptomTitle.setTypeface(null, Typeface.BOLD);
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_do_you_have_cough), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        coughDuration = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_duration_of_coughs), getResources().getStringArray(R.array.fast_duration_list), getResources().getString(R.string.fast_less_than_2_weeks_title), App.VERTICAL);
        productiveCough = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_is_your_cough_productive), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        haemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_sputum_in_blood), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_do_you_have_fever), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        feverDuration = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_how_long_you_have_fever), getResources().getStringArray(R.array.fast_duration_list), getResources().getString(R.string.fast_less_than_2_weeks_title), App.VERTICAL);
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_do_you_have_night_sweats), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_do_you_have_unexplained_weight_loss), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);

        patientTbHistoryTitle = new MyTextView(context, getResources().getString(R.string.fast_tbhistory_title));
        patientTbHistoryTitle.setTypeface(null, Typeface.BOLD);
        tbHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_tb_before), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        tbContact = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_close_with_someone_diagnosed), getResources().getStringArray(R.array.fast_choice_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), husbandName.getEditText(), fatherName.getEditText(), patientAttendant.getRadioGroup(), patientConsultation.getSpinner(),
                patientConsultationOther.getEditText(), cough.getRadioGroup(), coughDuration.getSpinner(),
                productiveCough.getRadioGroup(), haemoptysis.getRadioGroup(), fever.getRadioGroup(), feverDuration.getSpinner(),
                tbContact.getRadioGroup(), tbHistory.getRadioGroup(), nightSweats.getRadioGroup(), weightLoss.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, patientDemographicsTitle, husbandName, fatherName, patientAttendant, patientConsultation, patientConsultationOther},
                        {patientSymptomTitle, cough, coughDuration, productiveCough, haemoptysis, fever, feverDuration, nightSweats, weightLoss},
                        {patientTbHistoryTitle, tbHistory, tbContact}};

        patientAttendant.getRadioGroup().setOnCheckedChangeListener(this);
        patientConsultation.getSpinner().setOnItemSelectedListener(this);
        cough.getRadioGroup().setOnCheckedChangeListener(this);
        productiveCough.getRadioGroup().setOnCheckedChangeListener(this);
        coughDuration.getSpinner().setOnItemSelectedListener(this);
        formDate.getButton().setOnClickListener(this);
        patientAttendant.getRadioGroup().setOnCheckedChangeListener(this);
        fever.getRadioGroup().setOnCheckedChangeListener(this);
        tbHistory.getRadioGroup().setOnCheckedChangeListener(this);
        tbContact.getRadioGroup().setOnCheckedChangeListener(this);

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

            }else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss"));
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

        if (App.getPatient().getPerson().getAge() <= 15) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.fast_patient_age_less_than_15_cant_submit));
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
            Boolean error = false;

            if (patientConsultationOther.getVisibility() == View.VISIBLE && App.get(patientConsultationOther).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(2);
                else
                    gotoPage(0);
                patientConsultationOther.getEditText().setError(getString(R.string.empty_field));
                patientConsultationOther.getEditText().requestFocus();
                error = true;
            }

            if (fatherName.getVisibility() == View.VISIBLE && App.get(fatherName).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(2);
                else
                    gotoPage(0);
                fatherName.getEditText().setError(getString(R.string.empty_field));
                fatherName.getEditText().requestFocus();
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
        }

        return true;
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
      /*  observations.add (new String[] {"LONGITUDE (DEGREES)", String.valueOf(longitude)});
        observations.add (new String[] {"LATITUDE (DEGREES)", String.valueOf(latitude)});*/

        if (husbandName.getVisibility() == View.VISIBLE && !(App.get(husbandName).isEmpty()))
            observations.add(new String[]{"PARTNER FULL NAME", App.get(husbandName)});

        if (fatherName.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FATHER NAME", App.get(fatherName)});

        observations.add(new String[]{"PERSON ATTENDING FACILITY", App.get(patientAttendant).equals(getResources().getString(R.string.fast_patient_title)) ? "SELF" : "ATTENDANT"});

        if (patientConsultation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT CONSULTATION DEPARTMENT", App.get(patientConsultation).equals(getResources().getString(R.string.fast_chesttbclinic_title)) ? "CHEST MEDICINE DEPARTMENT" :
                    (App.get(patientConsultation).equals(getResources().getString(R.string.fast_medicine_title)) ? "GENERAL MEDICINE DEPARTMENT" :
                            (App.get(patientConsultation).equals(getResources().getString(R.string.fast_ent_title)) ? "EAR, NOSE AND THROAT DEPARTMENT" :
                                    (App.get(patientConsultation).equals(getResources().getString(R.string.fast_gynaeobstetrics_otherthen_title)) ? "OBSTETRICS AND GYNECOLOGY DEPARTMENT" :
                                            (App.get(patientConsultation).equals(getResources().getString(R.string.fast_pregnancy_title)) ? "ANTENATAL DEPARTMENT" :
                                                    (App.get(patientConsultation).equals(getResources().getString(R.string.fast_surgery_title)) ? "GENERAL SURGERY DEPARTMENT" :
                                                            (App.get(patientConsultation).equals(getResources().getString(R.string.fast_orthopedics_title)) ? "ORTHOPEDIC DEPARTMENT" :
                                                                    (App.get(patientConsultation).equals(getResources().getString(R.string.fast_emergency_title)) ? "EMERGENCY DEPARTMENT" :
                                                                            (App.get(patientConsultation).equals(getResources().getString(R.string.fast_paediatrics_title)) ? "PEDIATRIC MEDICINE DEPARTMENT" :
                                                                                    (App.get(patientConsultation).equals(getResources().getString(R.string.fast_dermatology)) ? "DERMATOLOGY DEPARTMENT" :
                                                                                            (App.get(patientConsultation).equals(getResources().getString(R.string.fast_neurology_title)) ? "NEUROLOGY DEPARTMENT" :
                                                                                                    (App.get(patientConsultation).equals(getResources().getString(R.string.fast_cardiology_title)) ? "CARDIOLOGY DEPARTMENT" :
                                                                                                            (App.get(patientConsultation).equals(getResources().getString(R.string.fast_urology_title)) ? "UROLOGY DEPARTMENT" :
                                                                                                                    (App.get(patientConsultation).equals(getResources().getString(R.string.fast_psychiatry_title)) ? "PSYCHIATRY DEPARTMENT" :
                                                                                                                            (App.get(patientConsultation).equals(getResources().getString(R.string.fast_opthamology_title)) ? "OPHTHALMOLOGY DEPARTMENT" :
                                                                                                                                    (App.get(patientConsultation).equals(getResources().getString(R.string.fast_endocrionology_title)) ? "ENDOCRINOLOGY DEPARTMENT" : "OTHER FACILITY DEPARTMENT")))))))))))))))});
        if (patientConsultationOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FACILITY DEPARTMENT", App.get(patientConsultationOther)});

        if (cough.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUGH", App.get(cough).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(cough).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(cough).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (coughDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUGH DURATION", App.get(coughDuration).equals(getResources().getString(R.string.fast_less_than_2_weeks_title)) ? "COUGH LASTING LESS THAN 2 WEEKS" :
                    (App.get(coughDuration).equals(getResources().getString(R.string.fast_2to3_weeks)) ? "COUGH LASTING MORE THAN 2 WEEKS" :
                            (App.get(coughDuration).equals(getResources().getString(R.string.fast_morethan3weeks)) ? "COUGH LASTING MORE THAN 3 WEEKS" :
                                    (App.get(coughDuration).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN")))});

        if (productiveCough.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PRODUCTIVE COUGH", App.get(productiveCough).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(productiveCough).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(productiveCough).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});


        if (haemoptysis.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HEMOPTYSIS", App.get(haemoptysis).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(haemoptysis).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(haemoptysis).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});


        if (fever.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FEVER", App.get(fever).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(fever).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(fever).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});


        if (feverDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FEVER DURATION", App.get(feverDuration).equals(getResources().getString(R.string.fast_less_than_2_weeks_title)) ? "FEVER LASTING LESS THAN TWO WEEKS" :
                    (App.get(feverDuration).equals(getResources().getString(R.string.fast_2to3_weeks)) ? "FEVER LASTING FOR 2 TO 3 WEEKS" :
                            (App.get(feverDuration).equals(getResources().getString(R.string.fast_morethan3weeks)) ? "FEVER LASTING MORE THAN THREE WEEKS" :
                                    (App.get(feverDuration).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN")))});

        if (nightSweats.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"NIGHT SWEATS", App.get(nightSweats).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(nightSweats).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(nightSweats).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (weightLoss.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"WEIGHT LOSS", App.get(weightLoss).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(weightLoss).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(weightLoss).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (tbHistory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HISTORY OF TUBERCULOSIS", App.get(tbHistory).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(tbHistory).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(tbHistory).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (tbContact.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOSIS CONTACT", App.get(tbContact).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(tbContact).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(tbContact).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});


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

                String result = serverService.saveEncounterAndObservation("Presumptive", FORM, formDateCalendar, observations.toArray(new String[][]{}));
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
    public void refill(int formId) {

        OfflineForm fo = serverService.getOfflineFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("FORM START TIME")) {
                startTime = App.stringToDate(obs[0][1], "yyyy-MM-dd hh:mm:ss");
            }

            else if (obs[0][0].equals("PARTNER FULL NAME")) {
                husbandName.getEditText().setText(obs[0][1]);
            }

            else if (obs[0][0].equals("FATHER NAME")) {
                fatherName.getEditText().setText(obs[0][1]);
            }

            else if (obs[0][0].equals("PERSON ATTENDING FACILITY")) {

                for (RadioButton rb : patientAttendant.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_patient_title)) && obs[0][1].equals("SELF")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_attendant_title)) && obs[0][1].equals("ATTENDANT")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                patientAttendant.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("PATIENT CONSULTATION DEPARTMENT")) {
                String value = obs[0][1].equals("CHEST MEDICINE DEPARTMENT") ? getResources().getString(R.string.fast_chesttbclinic_title) :
                        (obs[0][1].equals("GENERAL MEDICINE DEPARTMENT") ? getResources().getString(R.string.fast_medicine_title) :
                                (obs[0][1].equals("EAR, NOSE AND THROAT DEPARTMENT") ? getResources().getString(R.string.fast_ent_title) :
                                        (obs[0][1].equals("OBSTETRICS AND GYNECOLOGY DEPARTMENT") ? getResources().getString(R.string.fast_gynaeobstetrics_otherthen_title) :
                                                (obs[0][1].equals("ANTENATAL DEPARTMENT") ? getResources().getString(R.string.fast_pregnancy_title) :
                                                        (obs[0][1].equals("GENERAL SURGERY DEPARTMENT") ? getResources().getString(R.string.fast_surgery_title) :
                                                                (obs[0][1].equals("ORTHOPEDIC DEPARTMENT") ? getResources().getString(R.string.fast_orthopedics_title) :
                                                                        (obs[0][1].equals("EMERGENCY DEPARTMENT") ? getResources().getString(R.string.fast_emergency_title) :
                                                                                (obs[0][1].equals("PEDIATRIC MEDICINE DEPARTMENT") ? getResources().getString(R.string.fast_paediatrics_title) :
                                                                                        (obs[0][1].equals("DERMATOLOGY DEPARTMENT") ? getResources().getString(R.string.fast_dermatology) :
                                                                                                (obs[0][1].equals("NEUROLOGY DEPARTMENT") ? getResources().getString(R.string.fast_neurology_title) :
                                                                                                        (obs[0][1].equals("CARDIOLOGY DEPARTMENT") ? getResources().getString(R.string.fast_cardiology_title) :
                                                                                                                (obs[0][1].equals("UROLOGY DEPARTMENT") ? getResources().getString(R.string.fast_urology_title) :
                                                                                                                        (obs[0][1].equals("PSYCHIATRY DEPARTMENT") ? getResources().getString(R.string.fast_psychiatry_title) :
                                                                                                                                (obs[0][1].equals("OPHTHALMOLOGY DEPARTMENT") ? getResources().getString(R.string.fast_opthamology_title) :
                                                                                                                                        (obs[0][1].equals("ENDOCRINOLOGY DEPARTMENT") ? getResources().getString(R.string.fast_endocrionology_title) :
                                                                                                                                                getResources().getString(R.string.fast_other_title))))))))))))))));


                patientConsultation.getSpinner().selectValue(value);
                patientConsultation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER FACILITY DEPARTMENT")) {
                patientConsultationOther.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUGH")) {
                for (RadioButton rb : cough.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_refused_title)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_dont_know_title)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                cough.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("COUGH DURATION")) {
                String value = obs[0][1].equals("COUGH LASTING LESS THAN 2 WEEKS") ? getResources().getString(R.string.fast_less_than_2_weeks_title) :
                        (obs[0][1].equals("COUGH LASTING MORE THAN 2 WEEKS") ? getResources().getString(R.string.fast_2to3_weeks) :
                                (obs[0][1].equals("COUGH LASTING MORE THAN 3 WEEKS") ? getResources().getString(R.string.fast_morethan3weeks) :
                                        (obs[0][1].equals("REFUSED") ? getResources().getString(R.string.fast_refused_title) :
                                                getResources().getString(R.string.fast_dont_know_title))));

                coughDuration.getSpinner().selectValue(value);
                coughDuration.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("PRODUCTIVE COUGH")) {
                for (RadioButton rb : productiveCough.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_refused_title)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_dont_know_title)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                productiveCough.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("HEMOPTYSIS")) {
                for (RadioButton rb : haemoptysis.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_refused_title)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_dont_know_title)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                haemoptysis.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("FEVER")) {
                for (RadioButton rb : fever.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_refused_title)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_dont_know_title)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                fever.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("FEVER DURATION")) {
                String value = obs[0][1].equals("FEVER LASTING LESS THAN TWO WEEKS") ? getResources().getString(R.string.fast_less_than_2_weeks_title) :
                        (obs[0][1].equals("FEVER LASTING FOR 2 TO 3 WEEKS") ? getResources().getString(R.string.fast_2to3_weeks) :
                                (obs[0][1].equals("FEVER LASTING MORE THAN THREE WEEKS") ? getResources().getString(R.string.fast_morethan3weeks) :
                                        (obs[0][1].equals("REFUSED") ? getResources().getString(R.string.fast_refused_title) :
                                                getResources().getString(R.string.fast_dont_know_title))));

                feverDuration.getSpinner().selectValue(value);
                feverDuration.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("NIGHT SWEATS")) {
                for (RadioButton rb : nightSweats.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_refused_title)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_dont_know_title)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                nightSweats.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("WEIGHT LOSS")) {
                for (RadioButton rb : weightLoss.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_refused_title)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_dont_know_title)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                weightLoss.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("HISTORY OF TUBERCULOSIS")) {
                for (RadioButton rb : tbHistory.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_refused_title)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_dont_know_title)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                tbHistory.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TUBERCULOSIS CONTACT")) {
                for (RadioButton rb : tbContact.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_refused_title)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_dont_know_title)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                tbContact.setVisibility(View.VISIBLE);
            }

        }

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
        if (spinner == patientConsultation.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                patientConsultationOther.setVisibility(View.VISIBLE);
            } else {
                patientConsultationOther.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == patientAttendant.getRadioGroup()) {
            if (patientAttendant.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_attendant_title))) {
                patientConsultation.setVisibility(View.GONE);
                patientConsultationOther.setVisibility(View.GONE);

            } else {
                patientConsultation.setVisibility(View.VISIBLE);
                if (patientConsultation.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                    patientConsultationOther.setVisibility(View.VISIBLE);
                } else {
                    patientConsultationOther.setVisibility(View.GONE);
                }
            }
        } else if (radioGroup == cough.getRadioGroup()) {
            if (cough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                coughDuration.setVisibility(View.VISIBLE);
                productiveCough.setVisibility(View.VISIBLE);
                if (productiveCough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                    haemoptysis.setVisibility(View.VISIBLE);
                } else {
                    haemoptysis.setVisibility(View.GONE);
                }
            } else {
                coughDuration.setVisibility(View.GONE);
                productiveCough.setVisibility(View.GONE);
                haemoptysis.setVisibility(View.GONE);
            }
        } else if (radioGroup == productiveCough.getRadioGroup()) {
            if (productiveCough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title)) && cough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                haemoptysis.setVisibility(View.VISIBLE);
            } else {
                haemoptysis.setVisibility(View.GONE);
            }
        } else if (radioGroup == fever.getRadioGroup()) {
            if (fever.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                feverDuration.setVisibility(View.VISIBLE);
            } else {
                feverDuration.setVisibility(View.GONE);
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
        if(App.getPatient().getPerson().getGender().equals("M")){
            husbandName.setVisibility(View.GONE);
        }
        patientConsultationOther.setVisibility(View.GONE);
        feverDuration.setVisibility(View.GONE);

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
