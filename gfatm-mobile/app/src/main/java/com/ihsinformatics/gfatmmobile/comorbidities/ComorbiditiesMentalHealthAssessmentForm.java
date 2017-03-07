package com.ihsinformatics.gfatmmobile.comorbidities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Fawad Jawaid on 10-Feb-17.
 */

public class ComorbiditiesMentalHealthAssessmentForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledRadioGroup typeOfRescreening;
    TitledEditText gpClinicCode;
    MyTextView mentalHealthScreening;
    TitledRadioGroup akuadsSleep;
    TitledRadioGroup akuadsLackOfInterest;
    TitledRadioGroup akuadsLostInterestHobbies;
    TitledRadioGroup akuadsAnxious;
    TitledRadioGroup akuadsImpendingDoom;
    TitledRadioGroup akuadsDifficultyThinkingClearly;
    TitledRadioGroup akuadsAlone;
    TitledRadioGroup akuadsUnhappy;
    TitledRadioGroup akuadsHopeless;
    TitledRadioGroup akuadsHelpless;
    TitledRadioGroup akuadsWorried;
    TitledRadioGroup akuadsCried;
    TitledRadioGroup akuadsSuicide;
    TitledRadioGroup akuadsLossOfAppetite;
    TitledRadioGroup akuadsRetrosternalBurning;
    TitledRadioGroup akuadsIndigestion;
    TitledRadioGroup akuadsNausea;
    TitledRadioGroup akuadsConstipation;
    TitledRadioGroup akuadsDifficultBreathing;
    TitledRadioGroup akuadsTremulous;
    TitledRadioGroup akuadsNumbness;
    TitledRadioGroup akuadsTension;
    TitledRadioGroup akuadsHeadaches;
    TitledRadioGroup akuadsBodyPain;
    TitledRadioGroup akuadsUrination;
    TitledEditText akuadsTotalScore;
    TitledRadioGroup akuadsSeverity;
    TitledRadioGroup continuationStatus;
    TitledRadioGroup akuadsAgree;
    TitledSpinner preferredTherapyLocationSpinner;
    TitledButton nextAppointmentDate;
    //TitledEditText otherPreferredLocation;

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
        FORM_NAME = Forms.COMORBIDITIES_ASSESSMENT_FORM_MENTAL_HEALTH;
        FORM = Forms.comorbidities_assessmentFormMH;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesMentalHealthAssessmentForm.MyAdapter());
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
        typeOfRescreening = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_assessment_form_MH_rescreening), getResources().getStringArray(R.array.comorbidities_assessment_form_MH_rescreening_options), "", App.VERTICAL, App.VERTICAL);
        mentalHealthScreening = new MyTextView(context, getResources().getString(R.string.comorbidities_akuads_Mental_Health_Screening));
        mentalHealthScreening.setTypeface(null, Typeface.BOLD);
        akuadsSleep = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_sleep), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsLackOfInterest = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_lackofinterest), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsLostInterestHobbies = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_lostinteresthobbies), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsAnxious = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_anxious), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsImpendingDoom = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_impendingdoom), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsDifficultyThinkingClearly = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_difficultythinkingclearly), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsAlone = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_alone), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsUnhappy = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_unhappy), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsHopeless = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_hopeless), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsHelpless = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_helpless), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsWorried = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_worried), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsCried = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_cried), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsSuicide = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_suicide), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsLossOfAppetite = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_lossofappetite), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsRetrosternalBurning = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_retrosternalburning), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsIndigestion = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_indigestion), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsNausea = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_nausea), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsConstipation = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_constipation), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsDifficultBreathing = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_difficultybreathing), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsTremulous = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_tremulous), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsNumbness = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_numbness), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsTension = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_tension), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsHeadaches = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_headaches), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsBodyPain = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_bodypain), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsUrination = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_urination), getResources().getStringArray(R.array.comorbidities_MH_screening_options), getResources().getString(R.string.comorbidities_MH_screening_options_never), App.VERTICAL, App.VERTICAL);
        akuadsTotalScore = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_akuads_totalscore), "0", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        akuadsTotalScore.getEditText().setText(String.valueOf(getTotalScore()));
        akuadsTotalScore.getEditText().setFocusable(false);
        akuadsSeverity = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_severity), getResources().getStringArray(R.array.comorbidities_MH_severity_level), "", App.VERTICAL, App.VERTICAL);
        setAkuadsSeverityLevel();
        for (int i = 0; i < akuadsSeverity.getRadioGroup().getChildCount(); i++) {
            akuadsSeverity.getRadioGroup().getChildAt(i).setClickable(false);
        }
        continuationStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_continuation_status), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_continuation_status_options), getResources().getString(R.string.comorbidities_treatment_followup_MH_continuation_status_options_continue), App.VERTICAL, App.VERTICAL);

        // second page views...
        akuadsAgree = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_agree_AKUADS), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);

        //For getting the comorbidities location
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
        for(int i=0; i <locations.length; i++){
            locationArray[i] = String.valueOf(locations[i][1]);
        }

        preferredTherapyLocationSpinner = new TitledSpinner(mainContent.getContext(), null, getResources().getString(R.string.comorbidities_preferredlocation_id), locationArray, "", App.VERTICAL, true);
        //reasonForDiscontinuation = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_preferredlocation_id), getResources().getStringArray(R.array.comorbidities_location), "Sehatmand Zindagi Center - Korangi", App.HORIZONTAL);
        showPreferredLocationOrNot();
        gpClinicCode = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_preferredlocation_gpcliniccode), "", getResources().getString(R.string.comorbidities_preferredlocation_gpcliniccode_range), 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        nextAppointmentDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_urinedr_nexttestdate), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        //displayAkuadsAgreeOrNot();
        //displayPreferredTherapyLocationOrNot();
        //otherPreferredLocation = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_preferredlocation_other_comorbidities), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        //otherPreferredLocation.setVisibility(View.GONE);

        // Used for reset fields...
        views = new View[]{formDate.getButton(),  typeOfRescreening.getRadioGroup(), gpClinicCode.getEditText(), akuadsSleep.getRadioGroup(), akuadsLackOfInterest.getRadioGroup(),
                akuadsLostInterestHobbies.getRadioGroup(), akuadsAnxious.getRadioGroup(),
                akuadsImpendingDoom.getRadioGroup(), akuadsDifficultyThinkingClearly.getRadioGroup(),
                akuadsAlone.getRadioGroup(), akuadsUnhappy.getRadioGroup(),
                akuadsHopeless.getRadioGroup(), akuadsHelpless.getRadioGroup(),
                akuadsWorried.getRadioGroup(), akuadsCried.getRadioGroup(),
                akuadsSuicide.getRadioGroup(), akuadsLossOfAppetite.getRadioGroup(),
                akuadsRetrosternalBurning.getRadioGroup(), akuadsIndigestion.getRadioGroup(),
                akuadsNausea.getRadioGroup(), akuadsConstipation.getRadioGroup(),
                akuadsDifficultBreathing.getRadioGroup(), akuadsTremulous.getRadioGroup(),
                akuadsNumbness.getRadioGroup(), akuadsTension.getRadioGroup(),
                akuadsHeadaches.getRadioGroup(), akuadsBodyPain.getRadioGroup(),
                akuadsUrination.getRadioGroup(), akuadsTotalScore.getEditText(), akuadsSeverity.getRadioGroup(),
                akuadsAgree.getRadioGroup(), preferredTherapyLocationSpinner.getSpinner(), nextAppointmentDate.getButton()/*, otherPreferredLocation.getEditText()*/};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, typeOfRescreening, gpClinicCode, mentalHealthScreening, akuadsSleep, akuadsLackOfInterest, akuadsLostInterestHobbies, akuadsAnxious, akuadsImpendingDoom, akuadsDifficultyThinkingClearly,
                        akuadsAlone, akuadsUnhappy, akuadsHopeless, akuadsHelpless, akuadsWorried, akuadsCried, akuadsSuicide, akuadsLossOfAppetite, akuadsRetrosternalBurning,
                        akuadsIndigestion, akuadsNausea, akuadsConstipation, akuadsDifficultBreathing, akuadsTremulous, akuadsNumbness, akuadsTension, akuadsHeadaches, akuadsBodyPain,
                        akuadsUrination, akuadsTotalScore, akuadsSeverity, continuationStatus, akuadsAgree, preferredTherapyLocationSpinner, nextAppointmentDate /*otherPreferredLocation*/}};

        formDate.getButton().setOnClickListener(this);
        akuadsSleep.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsLackOfInterest.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsLostInterestHobbies.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsAnxious.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsImpendingDoom.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsDifficultyThinkingClearly.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsAlone.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsUnhappy.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsHopeless.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsHelpless.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsWorried.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsCried.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsSuicide.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsLossOfAppetite.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsRetrosternalBurning.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsIndigestion.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsNausea.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsConstipation.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsDifficultBreathing.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsTremulous.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsNumbness.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsTension.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsHeadaches.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsBodyPain.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsUrination.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsSeverity.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsAgree.getRadioGroup().setOnCheckedChangeListener(this);
        nextAppointmentDate.getButton().setOnClickListener(this);

        /*reasonForDiscontinuation.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                showOtherPreferredLocation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });*/

        gpClinicCode.getEditText().setSingleLine(true);
        gpClinicCode.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (gpClinicCode.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(gpClinicCode.getEditText().getText().toString());
                        if (num < 1 || num > 50) {
                            gpClinicCode.getEditText().setError(getString(R.string.comorbidities_preferredlocation_gpcliniccode_limit));
                        } else {
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });
    }

    @Override
    public void updateDisplay() {

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        nextAppointmentDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
    }

    @Override
    public boolean validate() {

        Boolean error = false;

        if (!akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_MH_severity_level_normal)) && gpClinicCode.getVisibility() == View.VISIBLE && App.get(gpClinicCode).isEmpty()) {
            gotoPage(1);
            gpClinicCode.getEditText().setError(getString(R.string.empty_field));
            gpClinicCode.getEditText().requestFocus();
            error = true;
        }
        else if (gpClinicCode.getVisibility() == View.VISIBLE && !App.get(gpClinicCode).isEmpty() && (Double.parseDouble(App.get(gpClinicCode)) < 1 || Double.parseDouble(App.get(gpClinicCode)) > 50)) {
            gotoPage(1);
            gpClinicCode.getEditText().setError(getString(R.string.comorbidities_preferredlocation_gpcliniccode_limit));
            gpClinicCode.getEditText().requestFocus();
            error = true;
        }

        /*if (!akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_MH_severity_level_normal)) && otherPreferredLocation.getVisibility() == View.VISIBLE && App.get(otherPreferredLocation).isEmpty()) {
            gotoPage(1);
            otherPreferredLocation.getEditText().setError(getString(R.string.empty_field));
            otherPreferredLocation.getEditText().requestFocus();
            error = true;
        }*/

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

        /*if (validate()) {

            resetViews();
        }

        return false;*/
        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        /*observations.add (new String[] {"LONGITUDE (DEGREES)", String.valueOf(longitude)});
        observations.add (new String[] {"LATITUDE (DEGREES)", String.valueOf(latitude)});*/
        observations.add(new String[]{"FOLLOW-UP VISIT TYPE", App.get(typeOfRescreening).equals(getResources().getString(R.string.comorbidities_assessment_form_MH_rescreening_options_6th_session)) ? "SIXTH SESSION" :
                (App.get(typeOfRescreening).equals(getResources().getString(R.string.comorbidities_assessment_form_MH_rescreening_options_rescreening)) ? "REPEATED SCREENING" :
                        (App.get(typeOfRescreening).equals(getResources().getString(R.string.comorbidities_assessment_form_MH_rescreening_options_end)) ? "END OF TREATMENT ASSESSMENT" : "OTHER ASSESSMENT REASON"))});

        if(gpClinicCode.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"HEALTH CLINIC/POST", App.get(gpClinicCode)});
        }

        observations.add(new String[]{"SLEEPING LESS (AKUADS)", App.get(akuadsSleep).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsSleep).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsSleep).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"INTEREST LOSS (AKUADS)", App.get(akuadsLackOfInterest).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsLackOfInterest).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsLackOfInterest).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"INTEREST LOSS IN HOBBIES (AKUADS)", App.get(akuadsLostInterestHobbies).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsLostInterestHobbies).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsLostInterestHobbies).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"ANXIOUS (AKUADS)", App.get(akuadsAnxious).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsAnxious).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsAnxious).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"IMPENDING DOOM SENSATION (AKUADS)", App.get(akuadsImpendingDoom).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsImpendingDoom).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsImpendingDoom).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"DIFFICULTY THINKING CLEARLY (AKUADS)", App.get(akuadsDifficultyThinkingClearly).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsDifficultyThinkingClearly).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsDifficultyThinkingClearly).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"PREFER BEING ALONE (AKUADS)", App.get(akuadsAlone).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsAlone).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsAlone).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"FEELING UNHAPPY (AKUADS)", App.get(akuadsUnhappy).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsUnhappy).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsUnhappy).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"FEELING HOPELESS (AKUADS)", App.get(akuadsHopeless).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsHopeless).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsHopeless).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"FEELING HELPLESS (AKUADS)", App.get(akuadsHelpless).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsHelpless).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsHelpless).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"WORRIED (AKUADS)", App.get(akuadsWorried).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsWorried).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsWorried).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"CRIED (AKUADS)", App.get(akuadsCried).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsCried).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsCried).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"FEELING SUICIDAL (AKUADS)", App.get(akuadsSuicide).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsSuicide).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsSuicide).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"LOSS OF APPETITE (AKUADS)", App.get(akuadsLossOfAppetite).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsLossOfAppetite).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsLossOfAppetite).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"RETROSTERNAL BURNING (AKUADS)", App.get(akuadsRetrosternalBurning).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsRetrosternalBurning).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsRetrosternalBurning).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"INDIGESTION (AKUADS)", App.get(akuadsIndigestion).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsIndigestion).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsIndigestion).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"NAUSEA (AKUADS)", App.get(akuadsNausea).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsNausea).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsNausea).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"CONSTIPATION (AKUADS)", App.get(akuadsConstipation).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsConstipation).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsConstipation).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"BREATHING DIFFICULTY (AKUADS)", App.get(akuadsDifficultBreathing).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsDifficultBreathing).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsDifficultBreathing).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"TREMULOUS (AKUADS)", App.get(akuadsTremulous).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsTremulous).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsTremulous).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"NUMBNESS (AKUADS)", App.get(akuadsNumbness).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsNumbness).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsNumbness).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"TENSION IN NECK AND SHOULDERS (AKUADS)", App.get(akuadsTension).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsTension).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsTension).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"HEADACHE (AKUADS)", App.get(akuadsHeadaches).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsHeadaches).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsHeadaches).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"BODY PAIN (AKUADS)", App.get(akuadsBodyPain).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsBodyPain).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsBodyPain).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"FREQUENT URINATION (AKUADS)", App.get(akuadsUrination).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                (App.get(akuadsUrination).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsUrination).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "OTHER"))});
        observations.add(new String[]{"AKUADS SCORE", App.get(akuadsTotalScore)});
        observations.add(new String[]{"AKUADS SEVERITY", App.get(akuadsUrination).equals(getResources().getString(R.string.comorbidities_MH_severity_level_normal)) ? "NORMAL" :
                (App.get(akuadsUrination).equals(getResources().getString(R.string.comorbidities_MH_severity_level_mild)) ? "MILD" :
                        (App.get(akuadsUrination).equals(getResources().getString(R.string.comorbidities_MH_severity_level_moderate)) ? "MODERATE" : "SEVERE"))});
        observations.add(new String[]{"CONTINUATION STATUS", App.get(continuationStatus).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_continuation_status_options_continue)) ? "EXERCISE THERAPY" :
                (App.get(continuationStatus).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_continuation_status_options_last)) ? "END OF THERAPY" :
                        (App.get(continuationStatus).equals(getResources().getString(R.string.comorbidities_treatment_followup_MH_continuation_status_options_referred)) ? "PATIENT REFERRED" : "OTHER CONTINUATION STATUS"))});
        observations.add(new String[]{"THERAPY CONSENT", App.get(akuadsAgree).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if(preferredTherapyLocationSpinner.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"FACILITY REFERRED TO", App.get(preferredTherapyLocationSpinner)});
        }
        
        observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDateTime(secondDateCalendar)});

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
                result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}));
                if (result.contains("SUCCESS"))
                    return "SUCCESS";

                return result;
            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            ;

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
                    alertDialog.setMessage(getResources().getString(R.string.insert_error));
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
        //formValues.put(firstName.getTag(), App.get(firstName));

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
        else if (view == nextAppointmentDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", false);
            args.putBoolean("allowFutureDate", true);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
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

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        nextAppointmentDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        showPreferredLocationOrNot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if (radioGroup == akuadsSleep.getRadioGroup() || radioGroup == akuadsLackOfInterest.getRadioGroup() || radioGroup == akuadsLostInterestHobbies.getRadioGroup() || radioGroup == akuadsAnxious.getRadioGroup()
                || radioGroup == akuadsImpendingDoom.getRadioGroup() || radioGroup == akuadsDifficultyThinkingClearly.getRadioGroup() || radioGroup == akuadsAlone.getRadioGroup()
                || radioGroup == akuadsUnhappy.getRadioGroup() || radioGroup == akuadsHopeless.getRadioGroup() || radioGroup == akuadsHelpless.getRadioGroup()
                || radioGroup == akuadsWorried.getRadioGroup() || radioGroup == akuadsCried.getRadioGroup() || radioGroup == akuadsSuicide.getRadioGroup()
                || radioGroup == akuadsLossOfAppetite.getRadioGroup() || radioGroup == akuadsRetrosternalBurning.getRadioGroup() || radioGroup == akuadsIndigestion.getRadioGroup()
                || radioGroup == akuadsNausea.getRadioGroup() || radioGroup == akuadsConstipation.getRadioGroup() || radioGroup == akuadsDifficultBreathing.getRadioGroup()
                || radioGroup == akuadsTremulous.getRadioGroup() || radioGroup == akuadsNumbness.getRadioGroup() || radioGroup == akuadsTension.getRadioGroup()
                || radioGroup == akuadsHeadaches.getRadioGroup() || radioGroup == akuadsBodyPain.getRadioGroup() || radioGroup == akuadsUrination.getRadioGroup()) {
            akuadsTotalScore.getEditText().setText(String.valueOf(getTotalScore()));
            setAkuadsSeverityLevel();
            //displayAkuadsAgreeOrNot();
        } else if (radioGroup == akuadsSeverity.getRadioGroup()) {
            //displayAkuadsAgreeOrNot();
        } /*else if (radioGroup == akuadsAgree.getRadioGroup()) {
            //displayPreferredTherapyLocationOrNot();
        }*/ else if (radioGroup == akuadsAgree.getRadioGroup()) {
            showPreferredLocationOrNot();
        }
    }

    int getTotalScore() {

        ArrayList<String> selectedOptions = new ArrayList<String>();
        selectedOptions.add(akuadsSleep.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsLackOfInterest.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsLostInterestHobbies.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsAnxious.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsImpendingDoom.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsDifficultyThinkingClearly.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsAlone.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsUnhappy.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsHopeless.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsHelpless.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsWorried.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsCried.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsSuicide.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsLossOfAppetite.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsRetrosternalBurning.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsIndigestion.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsNausea.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsConstipation.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsDifficultBreathing.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsTremulous.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsNumbness.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsTension.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsHeadaches.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsBodyPain.getRadioGroup().getSelectedValue());
        selectedOptions.add(akuadsUrination.getRadioGroup().getSelectedValue());

        int neverFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_MH_screening_options_never));
        int sometimesFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_MH_screening_options_sometimes));
        int mostlyFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_MH_screening_options_mostly));
        int alwaysFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_MH_screening_options_always));

        int neverTotal = 0 * neverFreq;
        int sometimesTotal = sometimesFreq;
        int mostlyTotal = 2 * mostlyFreq;
        int alwaysTotal = 3 * alwaysFreq;

        return neverTotal + sometimesTotal + mostlyTotal + alwaysTotal;
    }

    void setAkuadsSeverityLevel() {
        int score = Integer.parseInt(akuadsTotalScore.getEditText().getText().toString());

        if (score >= 21 && score <= 40) {
            akuadsSeverity.getRadioGroup().check((akuadsSeverity.getRadioGroup().getChildAt(0)).getId());
        } else if (score >= 41 && score <= 60) {
            akuadsSeverity.getRadioGroup().check((akuadsSeverity.getRadioGroup().getChildAt(1)).getId());
        } else if (score >= 61 && score <= 75) {
            akuadsSeverity.getRadioGroup().check((akuadsSeverity.getRadioGroup().getChildAt(2)).getId());
        }
        else {
            akuadsSeverity.getRadioGroup().check((akuadsSeverity.getRadioGroup().getChildAt(3)).getId());
        }
    }

    void showPreferredLocationOrNot() {
        if (akuadsAgree.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.yes))) {
            preferredTherapyLocationSpinner.setVisibility(View.VISIBLE);
        }
        else {
            preferredTherapyLocationSpinner.setVisibility(View.GONE);
        }
    }

    /*void displayAkuadsAgreeOrNot() {
        if (akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_MH_severity_level_mild)) || akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_MH_severity_level_moderate))
                || akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_MH_severity_level_severe))) {
            akuadsAgree.setVisibility(View.VISIBLE);
            reasonForDiscontinuation.setVisibility(View.VISIBLE);
            numberOfSessionsConducted.setVisibility(View.VISIBLE);
        } else if (akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_MH_severity_level_normal))) {
            akuadsAgree.setVisibility(View.GONE);
            reasonForDiscontinuation.setVisibility(View.GONE);
            numberOfSessionsConducted.setVisibility(View.GONE);
        }
    }*/

    /*void displayPreferredTherapyLocationOrNot() {
        if (akuadsAgree.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.yes)) && !akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_MH_severity_level_normal))) {
            reasonForDiscontinuation.setVisibility(View.VISIBLE);
            numberOfSessionsConducted.setVisibility(View.VISIBLE);
        } else if (akuadsAgree.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.no))) {
            reasonForDiscontinuation.setVisibility(View.GONE);
            numberOfSessionsConducted.setVisibility(View.GONE);
        }
    }*/

    /*void showOtherPreferredLocation() {
        String text = reasonForDiscontinuation.getSpinner().getSelectedItem().toString();

        if (text.equalsIgnoreCase(getResources().getString(R.string.comorbidities_location_other))) {
            if(!akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_MH_severity_level_normal))) {
                otherPreferredLocation.setVisibility(View.VISIBLE);
                numberOfSessionsConducted.setVisibility(View.GONE);
            }
        } else {
            if(!akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_MH_severity_level_normal))) {
                otherPreferredLocation.setVisibility(View.GONE);
                numberOfSessionsConducted.setVisibility(View.VISIBLE);
            }
        }
    }*/

    /**
     * Goto view at given location in the pager
     */
    @Override
    protected void gotoPage(int pageNo) {

        pager.setCurrentItem(pageNo);
        navigationSeekbar.setProgress(pageNo);

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
