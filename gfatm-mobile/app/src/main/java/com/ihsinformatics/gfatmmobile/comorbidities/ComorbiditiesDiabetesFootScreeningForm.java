package com.ihsinformatics.gfatmmobile.comorbidities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
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
        FORM =  Forms.comorbidities_diabetesTreatmentFollowupForm;

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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
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
        diabetesFootScreeningFootHygiene = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_foot_hygiene), getResources().getStringArray(R.array.comorbidities_foot_screening_foot_hygiene_options), "", App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningFootwearAppropriate = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_footwear_appropriate), getResources().getStringArray(R.array.comorbidities_yes_no), "", App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningPreviousUlcer = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_previous_ulcer), getResources().getStringArray(R.array.comorbidities_yes_no), "", App.VERTICAL, App.VERTICAL);
        diabetesFootScreeningDetailedClinicalNotes = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_foot_screening_clinical_notes), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        diabetesFootScreeningRecommendations = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_foot_screening_recommendations), getResources().getStringArray(R.array.comorbidities_foot_screening_recommendations_options), getResources().getString(R.string.comorbidities_foot_screening_recommendations_options_management), App.VERTICAL, App.VERTICAL);

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

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
    }

    @Override
    public boolean validate() {

        Boolean error = false;

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

        if (validate()) {

            resetViews();
        }

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
    public void refill(int encounterId) {

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if(radioGroup == diabetesFootScreeningRightFootExamined.getRadioGroup()) {
            allExaminationSkipLogic();
        }

        if(radioGroup == diabetesFootScreeningLeftFootExamined.getRadioGroup()) {
            allExaminationSkipLogic();
        }

    }

    void displayFootComplicationsAndAmputationHistory() {
        String text = diabetesFootScreeningMonthOfVisit.getSpinner().getSelectedItem().toString();

        if(text.equalsIgnoreCase("0")) {
            diabetesFootScreeningComplicationsOfFeet.setVisibility(View.VISIBLE);
            diabetesFootScreeningHistoryOfAmputation.setVisibility(View.VISIBLE);
        }
        else {
            diabetesFootScreeningComplicationsOfFeet.setVisibility(View.GONE);
            diabetesFootScreeningHistoryOfAmputation.setVisibility(View.GONE);
        }
    }

    void allExaminationSkipLogic() {

        if(diabetesFootScreeningRightFootExamined.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_foot_screening_foot_options_amputation))) {
            diabetesFootScreeningSkinRightFootExamination.setVisibility(View.GONE);
            diabetesFootScreeningRightFootSweating.setVisibility(View.GONE);
            diabetesFootScreeningRightFootDeformity.setVisibility(View.GONE);
            diabetesFootScreeningRightFootMonofilament.setVisibility(View.GONE);
            diabetesFootScreeningRightFootTuningFork.setVisibility(View.GONE);
            diabetesFootScreeningRightFootVascular.setVisibility(View.GONE);
        }
        else {
            diabetesFootScreeningSkinRightFootExamination.setVisibility(View.VISIBLE);
            diabetesFootScreeningRightFootSweating.setVisibility(View.VISIBLE);
            diabetesFootScreeningRightFootDeformity.setVisibility(View.VISIBLE);
            diabetesFootScreeningRightFootMonofilament.setVisibility(View.VISIBLE);
            diabetesFootScreeningRightFootTuningFork.setVisibility(View.VISIBLE);
            diabetesFootScreeningRightFootVascular.setVisibility(View.VISIBLE);
        }

        if(diabetesFootScreeningLeftFootExamined.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_foot_screening_foot_options_amputation))) {
            diabetesFootScreeningSkinLeftFootExamination.setVisibility(View.GONE);
            diabetesFootScreeningLeftFootSweating.setVisibility(View.GONE);
            diabetesFootScreeningLeftFootDeformity.setVisibility(View.GONE);
            diabetesFootScreeningLeftFootMonofilament.setVisibility(View.GONE);
            diabetesFootScreeningLeftFootTuningFork.setVisibility(View.GONE);
            diabetesFootScreeningLeftFootVascular.setVisibility(View.GONE);
        }
        else {
            diabetesFootScreeningSkinLeftFootExamination.setVisibility(View.VISIBLE);
            diabetesFootScreeningLeftFootSweating.setVisibility(View.VISIBLE);
            diabetesFootScreeningLeftFootDeformity.setVisibility(View.VISIBLE);
            diabetesFootScreeningLeftFootMonofilament.setVisibility(View.VISIBLE);
            diabetesFootScreeningLeftFootTuningFork.setVisibility(View.VISIBLE);
            diabetesFootScreeningLeftFootVascular.setVisibility(View.VISIBLE);
        }

        if(diabetesFootScreeningRightFootExamined.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_foot_screening_foot_options_amputation)) &&
                diabetesFootScreeningLeftFootExamined.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_foot_screening_foot_options_amputation))) {
            diabetesFootScreeningFootHygiene.setVisibility(View.GONE);
            diabetesFootScreeningFootwearAppropriate.setVisibility(View.GONE);
            diabetesFootScreeningPreviousUlcer.setVisibility(View.GONE);

            diabetesFootScreeningDermatologicalExamination.setVisibility(View.GONE);
            diabetesFootScreeningMusculoskeletalExamination.setVisibility(View.GONE);
            diabetesFootScreeningNeurologicalExamination.setVisibility(View.GONE);
            diabetesFootScreeningVascularExamination.setVisibility(View.GONE);
            diabetesFootScreeningOtherExamination.setVisibility(View.GONE);
        }
        else {
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




