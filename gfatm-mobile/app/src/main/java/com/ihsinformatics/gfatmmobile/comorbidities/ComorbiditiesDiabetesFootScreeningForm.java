package com.ihsinformatics.gfatmmobile.comorbidities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
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
 * Created by Fawad Jawaid on 31-Jan-17.
 */

public class ComorbiditiesDiabetesFootScreeningForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledSpinner diabetesFootScreeningMonthOfVisit;
    TitledRadioGroup diabetesFootScreeningRightFootExamined;
    //TitledRadioGroup diabetesFootScreeningReasonRightFootNotExamined;
    TitledRadioGroup diabetesFootScreeningLeftFootExamined;
    //TitledRadioGroup diabetesFootScreeningReasonLeftFootNotExamined;
    TitledRadioGroup diabetesFootScreeningComplicationsOfFeet;
    TitledRadioGroup diabetesFootScreeningHistoryOfAmputation;
    MyTextView diabetesFootScreeningDermatologicalExamination;
    TitledSpinner diabetesFootScreeningSkinRightFootExamination;
    TitledSpinner diabetesFootScreeningSkinLeftFootExamination;
    TitledRadioGroup diabetesFootScreeningRightFootSweating;
    TitledRadioGroup diabetesFootScreeningLeftFootSweating;
    MyTextView diabetesFootScreeningMusculoskeletalExamination;
    TitledRadioGroup diabetesFootScreeningRightFootDeformity;
    TitledRadioGroup diabetesFootScreeningLeftFootDeformity;
    MyTextView diabetesFootScreeningNeurologicalExamination;
    TitledRadioGroup diabetesFootScreeningRightFootMonofilament;
    TitledRadioGroup diabetesFootScreeningLeftFootMonofilament;
    TitledRadioGroup diabetesFootScreeningRightFootTuningFork;
    TitledRadioGroup diabetesFootScreeningLeftFootTuningFork;
    MyTextView diabetesFootScreeningVascularExamination;
    TitledRadioGroup diabetesFootScreeningRightFootVascular;
    TitledRadioGroup diabetesFootScreeningLeftFootVascular;
    MyTextView diabetesFootScreeningOtherExamination;
    TitledRadioGroup diabetesFootScreeningFootHygiene;
    TitledRadioGroup diabetesFootScreeningFootwearAppropriate;
    TitledRadioGroup diabetesFootScreeningPreviousUlcer;
    TitledEditText diabetesFootScreeningDetailedClinicalNotes;
    TitledRadioGroup diabetesFootScreeningRecommendations;

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
        FORM_NAME = Forms.COMORBIDITIES_DIABETES_FOOT_SCREENING_FORM;
        FORM = Forms.comorbidities_diabetesFootScreeningForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesDiabetesFootScreeningForm.MyAdapter());
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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        diabetesFootScreeningMonthOfVisit = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_urinedr_month_of_treatment), getResources().getStringArray(R.array.comorbidities_followup_month), "0", App.HORIZONTAL);
        diabetesFootScreeningRightFootExamined = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_right_foot), getResources().getStringArray(R.array.comorbidities_foot_screening_foot_options), getResources().getString(R.string.comorbidities_foot_screening_foot_options_yes), App.VERTICAL, App.VERTICAL);
        //diabetesFootScreeningReasonRightFootNotExamined = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_right_foot_reason), getResources().getStringArray(R.array.comorbidities_foot_screening_foot_reason_options), getResources().getString(R.string.comorbidities_foot_screening_foot_reason_options_amputation), App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningLeftFootExamined = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_left_foot), getResources().getStringArray(R.array.comorbidities_foot_screening_foot_options), getResources().getString(R.string.comorbidities_foot_screening_foot_options_yes), App.VERTICAL, App.VERTICAL);
        //diabetesFootScreeningReasonLeftFootNotExamined = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_left_foot_reason), getResources().getStringArray(R.array.comorbidities_foot_screening_foot_reason_options), getResources().getString(R.string.comorbidities_foot_screening_foot_reason_options_amputation), App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningComplicationsOfFeet = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_complications_of_foot), getResources().getStringArray(R.array.comorbidities_foot_screening_complications_of_foot_options), getResources().getString(R.string.comorbidities_foot_screening_complications_of_foot_options_infection), App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningHistoryOfAmputation = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_history_of_amputation), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningDermatologicalExamination = new MyTextView(context, getResources().getString(R.string.comorbidities_foot_screening_dermatological_examination));
        diabetesFootScreeningDermatologicalExamination.setTypeface(null, Typeface.BOLD);
        diabetesFootScreeningSkinRightFootExamination = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_foot_screening_skin_right_foot), getResources().getStringArray(R.array.comorbidities_foot_screening_skin_foot_options), getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_normal), App.HORIZONTAL);
        diabetesFootScreeningSkinLeftFootExamination = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_foot_screening_skin_left_foot), getResources().getStringArray(R.array.comorbidities_foot_screening_skin_foot_options), getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_normal), App.HORIZONTAL);
        diabetesFootScreeningRightFootSweating = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_right_foot_sweating), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningLeftFootSweating = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_left_foot_sweating), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningMusculoskeletalExamination = new MyTextView(context, getResources().getString(R.string.comorbidities_foot_screening_musculoskeletal_examination));
        diabetesFootScreeningMusculoskeletalExamination.setTypeface(null, Typeface.BOLD);
        diabetesFootScreeningRightFootDeformity = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_right_foot_deformity), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningLeftFootDeformity = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_left_foot_deformity), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningNeurologicalExamination = new MyTextView(context, getResources().getString(R.string.comorbidities_foot_screening_neurological_examination));
        diabetesFootScreeningNeurologicalExamination.setTypeface(null, Typeface.BOLD);
        diabetesFootScreeningRightFootMonofilament = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_right_foot_monofilament), getResources().getStringArray(R.array.comorbidities_foot_screening_neurological_examination_options), getResources().getString(R.string.comorbidities_foot_screening_neurological_examination_options_detectable), App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningLeftFootMonofilament = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_left_foot_monofilament), getResources().getStringArray(R.array.comorbidities_foot_screening_neurological_examination_options), getResources().getString(R.string.comorbidities_foot_screening_neurological_examination_options_detectable), App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningRightFootTuningFork = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_right_foot_tuning_fork), getResources().getStringArray(R.array.comorbidities_foot_screening_neurological_examination_options), getResources().getString(R.string.comorbidities_foot_screening_neurological_examination_options_detectable), App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningLeftFootTuningFork = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_left_foot_tuning_fork), getResources().getStringArray(R.array.comorbidities_foot_screening_neurological_examination_options), getResources().getString(R.string.comorbidities_foot_screening_neurological_examination_options_detectable), App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningVascularExamination = new MyTextView(context, getResources().getString(R.string.comorbidities_foot_screening_vascular_examination));
        diabetesFootScreeningVascularExamination.setTypeface(null, Typeface.BOLD);
        diabetesFootScreeningRightFootVascular = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_right_foot_vascular_assesment), getResources().getStringArray(R.array.comorbidities_foot_screening_vascular_examination_options), getResources().getString(R.string.comorbidities_foot_screening_vascular_examination_options_present), App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningLeftFootVascular = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_left_foot_vascular_assesment), getResources().getStringArray(R.array.comorbidities_foot_screening_vascular_examination_options), getResources().getString(R.string.comorbidities_foot_screening_vascular_examination_options_present), App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningOtherExamination = new MyTextView(context, getResources().getString(R.string.comorbidities_foot_screening_other_examination));
        diabetesFootScreeningOtherExamination.setTypeface(null, Typeface.BOLD);
        diabetesFootScreeningFootHygiene = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_foot_hygiene), getResources().getStringArray(R.array.comorbidities_foot_screening_foot_hygiene_options), getResources().getString(R.string.comorbidities_foot_screening_foot_hygiene_options_average), App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningFootwearAppropriate = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_footwear_appropriate), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningPreviousUlcer = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_previous_ulcer), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.no), App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningDetailedClinicalNotes = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_foot_screening_clinical_notes), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        diabetesFootScreeningRecommendations = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_recommendations), getResources().getStringArray(R.array.comorbidities_foot_screening_recommendations_options), getResources().getString(R.string.comorbidities_foot_screening_recommendations_options_12_month), App.VERTICAL, App.VERTICAL);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), diabetesFootScreeningMonthOfVisit.getSpinner(), diabetesFootScreeningRightFootExamined.getRadioGroup(), /*diabetesFootScreeningReasonRightFootNotExamined.getRadioGroup(),*/
                diabetesFootScreeningLeftFootExamined.getRadioGroup(), /*diabetesFootScreeningReasonLeftFootNotExamined.getRadioGroup(),*/ diabetesFootScreeningComplicationsOfFeet.getRadioGroup(),
                diabetesFootScreeningHistoryOfAmputation.getRadioGroup(), diabetesFootScreeningDermatologicalExamination, diabetesFootScreeningSkinRightFootExamination.getSpinner(),
                diabetesFootScreeningSkinLeftFootExamination.getSpinner(), diabetesFootScreeningRightFootSweating.getRadioGroup(), diabetesFootScreeningLeftFootSweating.getRadioGroup(),
                diabetesFootScreeningMusculoskeletalExamination, diabetesFootScreeningRightFootDeformity.getRadioGroup(), diabetesFootScreeningLeftFootDeformity.getRadioGroup(),
                diabetesFootScreeningNeurologicalExamination, diabetesFootScreeningRightFootMonofilament.getRadioGroup(), diabetesFootScreeningLeftFootMonofilament.getRadioGroup(),
                diabetesFootScreeningRightFootTuningFork.getRadioGroup(), diabetesFootScreeningLeftFootTuningFork.getRadioGroup(), diabetesFootScreeningVascularExamination,
                diabetesFootScreeningRightFootVascular.getRadioGroup(), diabetesFootScreeningLeftFootVascular.getRadioGroup(), diabetesFootScreeningOtherExamination,
                diabetesFootScreeningFootHygiene.getRadioGroup(), diabetesFootScreeningFootwearAppropriate.getRadioGroup(), diabetesFootScreeningPreviousUlcer.getRadioGroup(),
                diabetesFootScreeningDetailedClinicalNotes.getEditText(), diabetesFootScreeningRecommendations.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, diabetesFootScreeningMonthOfVisit, diabetesFootScreeningRightFootExamined, /*diabetesFootScreeningReasonRightFootNotExamined,*/
                        diabetesFootScreeningLeftFootExamined, /*diabetesFootScreeningReasonLeftFootNotExamined,*/ diabetesFootScreeningComplicationsOfFeet,
                        diabetesFootScreeningHistoryOfAmputation, diabetesFootScreeningDermatologicalExamination, diabetesFootScreeningSkinRightFootExamination,
                        diabetesFootScreeningSkinLeftFootExamination, diabetesFootScreeningRightFootSweating, diabetesFootScreeningLeftFootSweating,
                        diabetesFootScreeningMusculoskeletalExamination, diabetesFootScreeningRightFootDeformity, diabetesFootScreeningLeftFootDeformity,
                        diabetesFootScreeningNeurologicalExamination, diabetesFootScreeningRightFootMonofilament, diabetesFootScreeningLeftFootMonofilament,
                        diabetesFootScreeningRightFootTuningFork, diabetesFootScreeningLeftFootTuningFork, diabetesFootScreeningVascularExamination,
                        diabetesFootScreeningRightFootVascular, diabetesFootScreeningLeftFootVascular, diabetesFootScreeningOtherExamination,
                        diabetesFootScreeningFootHygiene, diabetesFootScreeningFootwearAppropriate, diabetesFootScreeningPreviousUlcer,
                        diabetesFootScreeningDetailedClinicalNotes, diabetesFootScreeningRecommendations}};

        formDate.getButton().setOnClickListener(this);
        diabetesFootScreeningRightFootExamined.getRadioGroup().setOnCheckedChangeListener(this);
        //diabetesFootScreeningReasonRightFootNotExamined.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesFootScreeningLeftFootExamined.getRadioGroup().setOnCheckedChangeListener(this);
        //diabetesFootScreeningReasonLeftFootNotExamined.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesFootScreeningComplicationsOfFeet.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesFootScreeningHistoryOfAmputation.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesFootScreeningRightFootSweating.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesFootScreeningLeftFootSweating.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesFootScreeningRightFootDeformity.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesFootScreeningLeftFootDeformity.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesFootScreeningRightFootMonofilament.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesFootScreeningLeftFootMonofilament.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesFootScreeningRightFootTuningFork.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesFootScreeningLeftFootTuningFork.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesFootScreeningRightFootVascular.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesFootScreeningLeftFootVascular.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesFootScreeningFootHygiene.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesFootScreeningFootwearAppropriate.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesFootScreeningPreviousUlcer.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesFootScreeningRecommendations.getRadioGroup().setOnCheckedChangeListener(this);

        diabetesFootScreeningMonthOfVisit.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                displayFootComplicationsAndAmputationHistory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        resetViews();
    }

    @Override
    public void updateDisplay() {

        //formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
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

        Boolean error = false;

        if (diabetesFootScreeningDetailedClinicalNotes.getEditText().getText().toString().length() > 0 && diabetesFootScreeningDetailedClinicalNotes.getEditText().getText().toString().trim().isEmpty()) {
            diabetesFootScreeningDetailedClinicalNotes.getEditText().setError(getString(R.string.comorbidities_patient_information_father_name_error));
            diabetesFootScreeningDetailedClinicalNotes.getEditText().requestFocus();
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

        final ArrayList<String[]> observations = new ArrayList<String[]>();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
                observations.add(new String[]{"TIME TAKEN TO FILL FORM", timeTakeToFill});
            } else {
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
        observations.add(new String[]{"FOLLOW-UP MONTH", App.get(diabetesFootScreeningMonthOfVisit)});
        observations.add(new String[]{"RIGHT FOOT EXAMINATION", App.get(diabetesFootScreeningRightFootExamined).equals(getResources().getString(R.string.comorbidities_foot_screening_foot_options_yes)) ? "YES" :
                (App.get(diabetesFootScreeningRightFootExamined).equals(getResources().getString(R.string.comorbidities_foot_screening_foot_options_amputation)) ? "AMPUTATION" : "REFUSED")});
        observations.add(new String[]{"LEFT FOOT EXAMINATION", App.get(diabetesFootScreeningLeftFootExamined).equals(getResources().getString(R.string.comorbidities_foot_screening_foot_options_yes)) ? "YES" :
                (App.get(diabetesFootScreeningLeftFootExamined).equals(getResources().getString(R.string.comorbidities_foot_screening_foot_options_amputation)) ? "AMPUTATION" : "REFUSED")});

        if (diabetesFootScreeningComplicationsOfFeet.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"FOOT COMPLICATIONS", App.get(diabetesFootScreeningComplicationsOfFeet).equals(getResources().getString(R.string.comorbidities_foot_screening_complications_of_foot_options_infection)) ? "FOOT INFECTION" :
                    (App.get(diabetesFootScreeningComplicationsOfFeet).equals(getResources().getString(R.string.comorbidities_foot_screening_complications_of_foot_options_ulcers)) ? "FOOT ULCER" :
                            App.get(diabetesFootScreeningComplicationsOfFeet).equals(getResources().getString(R.string.comorbidities_foot_screening_complications_of_foot_options_ingrown)) ? "INGROWING TOENAIL" : "OTHER INFECTION")});
        }

        if (diabetesFootScreeningHistoryOfAmputation.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"HISTORY OF AMPUTATION", App.get(diabetesFootScreeningHistoryOfAmputation).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        }

        if (diabetesFootScreeningSkinRightFootExamination.getVisibility() == View.VISIBLE) {
            final String diabetesFootScreeningSkinRightFootExaminationString = App.get(diabetesFootScreeningSkinRightFootExamination).equals(getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_normal)) ? "NORMAL" :
                    (App.get(diabetesFootScreeningSkinRightFootExamination).equals(getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_thickened)) ? "THICKENING OF SKIN" :
                            (App.get(diabetesFootScreeningSkinRightFootExamination).equals(getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_dry)) ? "DRY SKIN" :
                                    (App.get(diabetesFootScreeningSkinRightFootExamination).equals(getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_cracked)) ? "CRACKED SKIN" :
                                            (App.get(diabetesFootScreeningSkinRightFootExamination).equals(getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_infection)) ? "INFECTION OF SKIN" :
                                                    (App.get(diabetesFootScreeningSkinRightFootExamination).equals(getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_ulceration)) ? "SKIN ULCER" :
                                                            (App.get(diabetesFootScreeningSkinRightFootExamination).equals(getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_callus)) ? "SKIN CALLUS" : "DISCOLORATION OF SKIN"))))));
            observations.add(new String[]{"RIGHT FOOT SKIN EXAMINATION", diabetesFootScreeningSkinRightFootExaminationString});
        }

        if (diabetesFootScreeningSkinLeftFootExamination.getVisibility() == View.VISIBLE) {
            final String diabetesFootScreeningSkinLefttFootExaminationString = App.get(diabetesFootScreeningSkinLeftFootExamination).equals(getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_normal)) ? "NORMAL" :
                    (App.get(diabetesFootScreeningSkinLeftFootExamination).equals(getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_thickened)) ? "THICKENING OF SKIN" :
                            (App.get(diabetesFootScreeningSkinLeftFootExamination).equals(getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_dry)) ? "DRY SKIN" :
                                    (App.get(diabetesFootScreeningSkinLeftFootExamination).equals(getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_cracked)) ? "CRACKED SKIN" :
                                            (App.get(diabetesFootScreeningSkinLeftFootExamination).equals(getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_infection)) ? "INFECTION OF SKIN" :
                                                    (App.get(diabetesFootScreeningSkinLeftFootExamination).equals(getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_ulceration)) ? "SKIN ULCER" :
                                                            (App.get(diabetesFootScreeningSkinLeftFootExamination).equals(getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_callus)) ? "SKIN CALLUS" : "DISCOLORATION OF SKIN"))))));
            observations.add(new String[]{"LEFT FOOT SKIN EXAMINATION", diabetesFootScreeningSkinLefttFootExaminationString});
        }

        if (diabetesFootScreeningRightFootSweating.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"RIGHT FOOT SWEATING", App.get(diabetesFootScreeningRightFootSweating).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        }

        if (diabetesFootScreeningLeftFootSweating.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"LEFT FOOT SWEATING", App.get(diabetesFootScreeningLeftFootSweating).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        }

        if (diabetesFootScreeningRightFootDeformity.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"DEFORMITY OF RIGHT FOOT", App.get(diabetesFootScreeningRightFootDeformity).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        }

        if (diabetesFootScreeningLeftFootDeformity.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"DEFORMITY OF LEFT FOOT", App.get(diabetesFootScreeningLeftFootDeformity).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        }

        if (diabetesFootScreeningRightFootMonofilament.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"RIGHT FOOT MONOFILAMENT", App.get(diabetesFootScreeningRightFootMonofilament).equals(getResources().getString(R.string.comorbidities_foot_screening_neurological_examination_options_detectable)) ? "DETECTED" : "NOT DETECTED"});
        }

        if (diabetesFootScreeningLeftFootMonofilament.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"LEFT FOOT MONOFILAMENT", App.get(diabetesFootScreeningLeftFootMonofilament).equals(getResources().getString(R.string.comorbidities_foot_screening_neurological_examination_options_detectable)) ? "DETECTED" : "NOT DETECTED"});
        }

        if (diabetesFootScreeningRightFootTuningFork.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"RIGHT FOOT TURING FORK", App.get(diabetesFootScreeningRightFootTuningFork).equals(getResources().getString(R.string.comorbidities_foot_screening_neurological_examination_options_detectable)) ? "DETECTED" : "NOT DETECTED"});
        }

        if (diabetesFootScreeningLeftFootTuningFork.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"LEFT FOOT TURING FORK", App.get(diabetesFootScreeningLeftFootTuningFork).equals(getResources().getString(R.string.comorbidities_foot_screening_neurological_examination_options_detectable)) ? "DETECTED" : "NOT DETECTED"});
        }

        if (diabetesFootScreeningRightFootVascular.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"RIGHT FOOT VASCULAR", App.get(diabetesFootScreeningRightFootVascular).equals(getResources().getString(R.string.comorbidities_foot_screening_vascular_examination_options_present)) ? "PRESENT" :
                    (App.get(diabetesFootScreeningRightFootVascular).equals(getResources().getString(R.string.comorbidities_foot_screening_vascular_examination_options_diminished)) ? "REDUCED" : "ABSENT")});
        }

        if (diabetesFootScreeningLeftFootVascular.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"LEFT FOOT VASCULAR", App.get(diabetesFootScreeningLeftFootVascular).equals(getResources().getString(R.string.comorbidities_foot_screening_vascular_examination_options_present)) ? "PRESENT" :
                    (App.get(diabetesFootScreeningLeftFootVascular).equals(getResources().getString(R.string.comorbidities_foot_screening_vascular_examination_options_diminished)) ? "REDUCED" : "ABSENT")});
        }

        if (diabetesFootScreeningFootHygiene.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"FOOT HYGENE", App.get(diabetesFootScreeningFootHygiene).equals(getResources().getString(R.string.comorbidities_foot_screening_foot_hygiene_options_poor)) ? "POOR" :
                    (App.get(diabetesFootScreeningFootHygiene).equals(getResources().getString(R.string.comorbidities_foot_screening_foot_hygiene_options_average)) ? "AVERAGE" : "GOOD")});
        }

        if (diabetesFootScreeningFootwearAppropriate.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"FOOTWEAR", App.get(diabetesFootScreeningFootwearAppropriate).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        }

        if (diabetesFootScreeningPreviousUlcer.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"ULCER HISTORY", App.get(diabetesFootScreeningPreviousUlcer).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        }

        observations.add(new String[]{"CLINICIAN NOTES (TEXT)", App.get(diabetesFootScreeningDetailedClinicalNotes).trim()});
        observations.add(new String[]{"DIABETES RECOMMENDATAION",
                App.get(diabetesFootScreeningRecommendations).equals(getResources().getString(R.string.comorbidities_foot_screening_recommendations_options_12_month)) ? "NO LOSS OF SENSATION, 12 MONTH FOLLOWUP" :
                        App.get(diabetesFootScreeningRecommendations).equals(getResources().getString(R.string.comorbidities_foot_screening_recommendations_options_3_month)) ? "LOSS OF PROTECTIVE SENSATION, 3 MONTH FOLLOWUP" :
                                App.get(diabetesFootScreeningRecommendations).equals(getResources().getString(R.string.comorbidities_foot_screening_recommendations_options_1_month)) ? "LOSS OF SENSATION IN FEET WITH HIGH PRESSURE OR CALLUS FORMATION WITH POOR CIRCULATION, 1 MONTH FOLLOWUP" : "HISTORY OF PLANTAR ULCERATION OR NEUROPATHIC FRACTURE, REFERRAL TO FOOT DOCTOR"});

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

                String result = "";
                result = serverService.saveEncounterAndObservation(App.getProgram()+"-"+FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
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

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));

        //serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int formId) {

        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            }

            if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                diabetesFootScreeningMonthOfVisit.getSpinner().selectValue(obs[0][1]);
            } else if (obs[0][0].equals("RIGHT FOOT EXAMINATION")) {
                for (RadioButton rb : diabetesFootScreeningRightFootExamined.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_foot_options_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_foot_options_amputation)) && obs[0][1].equals("AMPUTATION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_foot_options_patient_refusal)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("LEFT FOOT EXAMINATION")) {
                for (RadioButton rb : diabetesFootScreeningLeftFootExamined.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_foot_options_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_foot_options_amputation)) && obs[0][1].equals("AMPUTATION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_foot_options_patient_refusal)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("FOOT COMPLICATIONS")) {
                for (RadioButton rb : diabetesFootScreeningComplicationsOfFeet.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_complications_of_foot_options_infection)) && obs[0][1].equals("FOOT INFECTION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_complications_of_foot_options_ulcers)) && obs[0][1].equals("FOOT ULCER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_complications_of_foot_options_ingrown)) && obs[0][1].equals("INGROWING TOENAIL")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_complications_of_foot_options_other)) && obs[0][1].equals("OTHER INFECTION")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                diabetesFootScreeningComplicationsOfFeet.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("HISTORY OF AMPUTATION")) {
                for (RadioButton rb : diabetesFootScreeningHistoryOfAmputation.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                diabetesFootScreeningHistoryOfAmputation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("RIGHT FOOT SKIN EXAMINATION")) {
                String value = obs[0][1].equals("NORMAL") ? getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_normal) :
                        (obs[0][1].equals("THICKENING OF SKIN") ? getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_thickened) :
                                (obs[0][1].equals("DRY SKIN") ? getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_dry) :
                                        (obs[0][1].equals("CRACKED SKIN") ? getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_cracked) :
                                                (obs[0][1].equals("INFECTION OF SKIN") ? getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_infection) :
                                                        (obs[0][1].equals("SKIN ULCER") ? getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_ulceration) : getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_callus))))));
                diabetesFootScreeningSkinRightFootExamination.getSpinner().selectValue(value);
                diabetesFootScreeningSkinRightFootExamination.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("LEFT FOOT SKIN EXAMINATION")) {
                String value = obs[0][1].equals("NORMAL") ? getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_normal) :
                        (obs[0][1].equals("THICKENING OF SKIN") ? getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_thickened) :
                                (obs[0][1].equals("DRY SKIN") ? getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_dry) :
                                        (obs[0][1].equals("CRACKED SKIN") ? getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_cracked) :
                                                (obs[0][1].equals("INFECTION OF SKIN") ? getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_infection) :
                                                        (obs[0][1].equals("SKIN ULCER") ? getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_ulceration) : getResources().getString(R.string.comorbidities_foot_screening_skin_foot_options_callus))))));
                diabetesFootScreeningSkinLeftFootExamination.getSpinner().selectValue(value);
                diabetesFootScreeningSkinLeftFootExamination.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("RIGHT FOOT SWEATING")) {
                for (RadioButton rb : diabetesFootScreeningRightFootSweating.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                diabetesFootScreeningRightFootSweating.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("LEFT FOOT SWEATING")) {
                for (RadioButton rb : diabetesFootScreeningLeftFootSweating.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                diabetesFootScreeningLeftFootSweating.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("DEFORMITY OF RIGHT FOOT")) {
                for (RadioButton rb : diabetesFootScreeningRightFootDeformity.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                diabetesFootScreeningRightFootDeformity.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("DEFORMITY OF LEFT FOOT")) {
                for (RadioButton rb : diabetesFootScreeningLeftFootDeformity.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                diabetesFootScreeningLeftFootDeformity.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("RIGHT FOOT MONOFILAMENT")) {
                for (RadioButton rb : diabetesFootScreeningRightFootMonofilament.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_neurological_examination_options_detectable)) && obs[0][1].equals("DETECTED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_neurological_examination_options_not_detectable)) && obs[0][1].equals("NOT DETECTED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                diabetesFootScreeningRightFootMonofilament.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("LEFT FOOT MONOFILAMENT")) {
                for (RadioButton rb : diabetesFootScreeningLeftFootMonofilament.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_neurological_examination_options_detectable)) && obs[0][1].equals("DETECTED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_neurological_examination_options_not_detectable)) && obs[0][1].equals("NOT DETECTED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                diabetesFootScreeningLeftFootMonofilament.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("RIGHT FOOT TURING FORK")) {
                for (RadioButton rb : diabetesFootScreeningRightFootTuningFork.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_neurological_examination_options_detectable)) && obs[0][1].equals("DETECTED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_neurological_examination_options_not_detectable)) && obs[0][1].equals("NOT DETECTED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                diabetesFootScreeningRightFootTuningFork.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("LEFT FOOT TURING FORK")) {
                for (RadioButton rb : diabetesFootScreeningLeftFootTuningFork.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_neurological_examination_options_detectable)) && obs[0][1].equals("DETECTED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_neurological_examination_options_not_detectable)) && obs[0][1].equals("NOT DETECTED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                diabetesFootScreeningLeftFootTuningFork.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("RIGHT FOOT VASCULAR")) {
                for (RadioButton rb : diabetesFootScreeningRightFootVascular.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_vascular_examination_options_present)) && obs[0][1].equals("PRESENT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_vascular_examination_options_diminished)) && obs[0][1].equals("REDUCED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_vascular_examination_options_absent)) && obs[0][1].equals("ABSENT")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                diabetesFootScreeningRightFootVascular.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("LEFT FOOT VASCULAR")) {
                for (RadioButton rb : diabetesFootScreeningLeftFootVascular.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_vascular_examination_options_present)) && obs[0][1].equals("PRESENT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_vascular_examination_options_diminished)) && obs[0][1].equals("REDUCED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_vascular_examination_options_absent)) && obs[0][1].equals("ABSENT")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                diabetesFootScreeningLeftFootVascular.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("FOOT HYGENE")) {
                for (RadioButton rb : diabetesFootScreeningFootHygiene.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_foot_hygiene_options_poor)) && obs[0][1].equals("POOR")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_foot_hygiene_options_average)) && obs[0][1].equals("AVERAGE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_foot_hygiene_options_good)) && obs[0][1].equals("GOOD")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                diabetesFootScreeningFootHygiene.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("FOOTWEAR")) {
                for (RadioButton rb : diabetesFootScreeningFootwearAppropriate.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                diabetesFootScreeningFootwearAppropriate.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ULCER HISTORY")) {
                for (RadioButton rb : diabetesFootScreeningPreviousUlcer.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                diabetesFootScreeningPreviousUlcer.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
                diabetesFootScreeningDetailedClinicalNotes.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("DIABETES RECOMMENDATAION")) {
                for (RadioButton rb : diabetesFootScreeningRecommendations.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_recommendations_options_12_month)) && obs[0][1].equals("NO LOSS OF SENSATION, 12 MONTH FOLLOWUP")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_recommendations_options_3_month)) && obs[0][1].equals("LOSS OF PROTECTIVE SENSATION, 3 MONTH FOLLOWUP")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_recommendations_options_1_month)) && obs[0][1].equals("LOSS OF SENSATION IN FEET WITH HIGH PRESSURE OR CALLUS FORMATION WITH POOR CIRCULATION, 1 MONTH FOLLOWUP")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_foot_screening_recommendations_options_history)) && obs[0][1].equals("HISTORY OF PLANTAR ULCERATION OR NEUROPATHIC FRACTURE, REFERRAL TO FOOT DOCTOR")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }
        }
    }

    public void showInstrucrtions(int instructions) {
        try {
            snackbar = Snackbar.make(mainContent, getResources().getString(instructions).toString(), Snackbar.LENGTH_INDEFINITE)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });

            // Changing message text color
            //snackbar.setActionTextColor(Color.RED);

            //Changing Typeface of Snackbar Action text
            TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
            snackbarActionTextView.setTextSize(20);
            snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);

            // Setting Maximum lines for the textview in snackbar
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(9);
            snackbar.show();
        } catch (Exception e) {
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

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        displayFootComplicationsAndAmputationHistory();
        allExaminationSkipLogic();

        Boolean flag = true;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);

                refill(formId);
                flag = false;

            } else bundle.putBoolean("save", false);

        }

        if (flag) {
            //HERE FOR AUTOPOPULATING OBS
            final AsyncTask<String, String, HashMap<String, String>> autopopulateFormTask = new AsyncTask<String, String, HashMap<String, String>>() {
                @Override
                protected HashMap<String, String> doInBackground(String... params) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading.setInverseBackgroundForced(true);
                            loading.setIndeterminate(true);
                            loading.setCancelable(false);
                            loading.setMessage(getResources().getString(R.string.fetching_data));
                            loading.show();
                        }
                    });

                    HashMap<String, String> result = new HashMap<String, String>();
                    String monthOfTreatment = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.COMORBIDITIES_VITALS_FORM, "FOLLOW-UP MONTH");

                    if (monthOfTreatment != null && !monthOfTreatment.equals(""))
                        monthOfTreatment = monthOfTreatment.replace(".0", "");

                    if (monthOfTreatment != null)
                        if (!monthOfTreatment.equals(""))
                            result.put("FOLLOW-UP MONTH", monthOfTreatment);

                    return result;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                }

                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();

                    if (result.get("FOLLOW-UP MONTH") != null)
                        diabetesFootScreeningMonthOfVisit.getSpinner().selectValue(result.get("FOLLOW-UP MONTH"));

                }
            };
            autopopulateFormTask.execute("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if (radioGroup == diabetesFootScreeningRightFootExamined.getRadioGroup()) {
            allExaminationSkipLogic();
        }

        if (radioGroup == diabetesFootScreeningLeftFootExamined.getRadioGroup()) {
            allExaminationSkipLogic();
        }
        if (radioGroup == diabetesFootScreeningRecommendations.getRadioGroup()) {
            if (diabetesFootScreeningRecommendations.getRadioGroupSelectedValue().equals(getString(R.string.comorbidities_foot_screening_recommendations_options_12_month))) {
                showInstrucrtions(R.string.comorbidities_foot_screening_recommendations_options_instruction_12_month);
            } else if (diabetesFootScreeningRecommendations.getRadioGroupSelectedValue().equals(getString(R.string.comorbidities_foot_screening_recommendations_options_3_month))) {
                showInstrucrtions(R.string.comorbidities_foot_screening_recommendations_options_instruction_3_month);
            } else if (diabetesFootScreeningRecommendations.getRadioGroupSelectedValue().equals(getString(R.string.comorbidities_foot_screening_recommendations_options_1_month))) {
                showInstrucrtions(R.string.comorbidities_foot_screening_recommendations_options_instruction_1_month);
            } else if (diabetesFootScreeningRecommendations.getRadioGroupSelectedValue().equals(getString(R.string.comorbidities_foot_screening_recommendations_options_history))) {
                showInstrucrtions(R.string.comorbidities_foot_screening_recommendations_options_instruction_history);
            }
        }

    }

    void displayFootComplicationsAndAmputationHistory() {
        String text = diabetesFootScreeningMonthOfVisit.getSpinner().getSelectedItem().toString();

        if (text.equalsIgnoreCase("0")) {
            diabetesFootScreeningComplicationsOfFeet.setVisibility(View.VISIBLE);
            diabetesFootScreeningHistoryOfAmputation.setVisibility(View.VISIBLE);
        } else {
            diabetesFootScreeningComplicationsOfFeet.setVisibility(View.GONE);
            diabetesFootScreeningHistoryOfAmputation.setVisibility(View.GONE);
        }
    }

    void allExaminationSkipLogic() {

        if (diabetesFootScreeningRightFootExamined.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_foot_screening_foot_options_amputation))) {
            diabetesFootScreeningSkinRightFootExamination.setVisibility(View.GONE);
            diabetesFootScreeningRightFootSweating.setVisibility(View.GONE);
            diabetesFootScreeningRightFootDeformity.setVisibility(View.GONE);
            diabetesFootScreeningRightFootMonofilament.setVisibility(View.GONE);
            diabetesFootScreeningRightFootTuningFork.setVisibility(View.GONE);
            diabetesFootScreeningRightFootVascular.setVisibility(View.GONE);
        } else {
            diabetesFootScreeningSkinRightFootExamination.setVisibility(View.VISIBLE);
            diabetesFootScreeningRightFootSweating.setVisibility(View.VISIBLE);
            diabetesFootScreeningRightFootDeformity.setVisibility(View.VISIBLE);
            diabetesFootScreeningRightFootMonofilament.setVisibility(View.VISIBLE);
            diabetesFootScreeningRightFootTuningFork.setVisibility(View.VISIBLE);
            diabetesFootScreeningRightFootVascular.setVisibility(View.VISIBLE);
        }

        if (diabetesFootScreeningLeftFootExamined.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_foot_screening_foot_options_amputation))) {
            diabetesFootScreeningSkinLeftFootExamination.setVisibility(View.GONE);
            diabetesFootScreeningLeftFootSweating.setVisibility(View.GONE);
            diabetesFootScreeningLeftFootDeformity.setVisibility(View.GONE);
            diabetesFootScreeningLeftFootMonofilament.setVisibility(View.GONE);
            diabetesFootScreeningLeftFootTuningFork.setVisibility(View.GONE);
            diabetesFootScreeningLeftFootVascular.setVisibility(View.GONE);
        } else {
            diabetesFootScreeningSkinLeftFootExamination.setVisibility(View.VISIBLE);
            diabetesFootScreeningLeftFootSweating.setVisibility(View.VISIBLE);
            diabetesFootScreeningLeftFootDeformity.setVisibility(View.VISIBLE);
            diabetesFootScreeningLeftFootMonofilament.setVisibility(View.VISIBLE);
            diabetesFootScreeningLeftFootTuningFork.setVisibility(View.VISIBLE);
            diabetesFootScreeningLeftFootVascular.setVisibility(View.VISIBLE);
        }

        if (diabetesFootScreeningRightFootExamined.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_foot_screening_foot_options_amputation)) &&
                diabetesFootScreeningLeftFootExamined.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_foot_screening_foot_options_amputation))) {
            diabetesFootScreeningFootHygiene.setVisibility(View.GONE);
            diabetesFootScreeningFootwearAppropriate.setVisibility(View.GONE);
            diabetesFootScreeningPreviousUlcer.setVisibility(View.GONE);

            diabetesFootScreeningDermatologicalExamination.setVisibility(View.GONE);
            diabetesFootScreeningMusculoskeletalExamination.setVisibility(View.GONE);
            diabetesFootScreeningNeurologicalExamination.setVisibility(View.GONE);
            diabetesFootScreeningVascularExamination.setVisibility(View.GONE);
            diabetesFootScreeningOtherExamination.setVisibility(View.GONE);
        } else {
            diabetesFootScreeningFootHygiene.setVisibility(View.VISIBLE);
            diabetesFootScreeningFootwearAppropriate.setVisibility(View.VISIBLE);
            diabetesFootScreeningPreviousUlcer.setVisibility(View.VISIBLE);

            diabetesFootScreeningDermatologicalExamination.setVisibility(View.VISIBLE);
            diabetesFootScreeningMusculoskeletalExamination.setVisibility(View.VISIBLE);
            diabetesFootScreeningNeurologicalExamination.setVisibility(View.VISIBLE);
            diabetesFootScreeningVascularExamination.setVisibility(View.VISIBLE);
            diabetesFootScreeningOtherExamination.setVisibility(View.VISIBLE);
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




