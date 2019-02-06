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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Fawad Jawaid on 16-Dec-16.
 */

public class ComorbiditiesMentalHealthScreening extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    MyTextView mentalHealthScreening;
    TitledRadioGroup willbeScreened;
    TitledSpinner reasonForNotDoingMentalHealthScreening;
    TitledEditText otherReasonForNotDoingMentalHealthScreening;
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
    TitledRadioGroup akuadsAgree;
    TitledRadioGroup akuadsTreatmentFacilityConsent;
    TitledRadioGroup akuadsPhoneCounsellingConsent;
    TitledCheckBoxes majorCauseOfMentalDisturbance;
    TitledEditText otherMajorCauseOfMentalDisturbance;
    TitledEditText physicalIllnessMajorCauseOfMentalDisturbance;

    TitledSpinner preferredTherapyLocationSpinner;
    TitledButton mentalHealthNextScheduledVisit;

    //TitledEditText otherPreferredLocation;

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
        formName = Forms.COMORBIDITIES_MENTAL_HEALTH_SCREENING_FORM;
        form = Forms.comorbidities_mentalHealthScreening;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesMentalHealthScreening.MyAdapter());
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
        formDate.setTag("formDate");
        mentalHealthScreening = new MyTextView(context, getResources().getString(R.string.comorbidities_akuads_Mental_Health_Screening));
        willbeScreened = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_will_be_screened), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        mentalHealthScreening.setTypeface(null, Typeface.BOLD);
        reasonForNotDoingMentalHealthScreening = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.reason_for_not_doing_mental_health_screening), getResources().getStringArray(R.array.reason_for_not_doing_mental_health_screening_array), getResources().getString(R.string.none), App.VERTICAL, true);
        otherReasonForNotDoingMentalHealthScreening = new TitledEditText(context, null, getResources().getString(R.string.other_reason_for_not_doing_mental_health_screening), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        otherReasonForNotDoingMentalHealthScreening.setVisibility(View.GONE);

        otherMajorCauseOfMentalDisturbance = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_other_specify), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        otherMajorCauseOfMentalDisturbance.setVisibility(View.GONE);
        physicalIllnessMajorCauseOfMentalDisturbance = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_physical_illness_name), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        physicalIllnessMajorCauseOfMentalDisturbance.setVisibility(View.GONE);

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
        akuadsSeverity = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_akuads_severity), getResources().getStringArray(R.array.comorbidities_MH_severity_level), getResources().getString(R.string.comorbidities_MH_severity_level_normal), App.VERTICAL, App.VERTICAL);
        setAkuadsSeverityLevel();
        for (int i = 0; i < akuadsSeverity.getRadioGroup().getChildCount(); i++) {
            akuadsSeverity.getRadioGroup().getChildAt(i).setClickable(false);
        }


        // second page views...
        akuadsAgree = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_agree_AKUADS), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);

        //For getting the comorbidities location
        String columnName = "comorbidities_location";

        final Object[][] locations = serverService.getAllLocationsFromLocalDB(columnName);
        String[] locationArray = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][1]);
        }

        akuadsTreatmentFacilityConsent = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_treatment_facility_consent), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL);
        akuadsPhoneCounsellingConsent = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_phone_counselling_consent), getResources().getStringArray(R.array.comorbidities_phone_counselling_consent_options), "", App.VERTICAL, App.VERTICAL);
        majorCauseOfMentalDisturbance = new TitledCheckBoxes(context, null, getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance), getResources().getStringArray(R.array.comorbidities_major_cause_of_mental_disturbance_array), null, App.VERTICAL, App.VERTICAL);
        preferredTherapyLocationSpinner = new TitledSpinner(mainContent.getContext(), null, getResources().getString(R.string.comorbidities_preferredlocation_id), locationArray, "", App.VERTICAL, true);
        mentalHealthNextScheduledVisit = new TitledButton(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_next_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        displayAkuadsAgreeOrNot();

        //otherPreferredLocation = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_preferredlocation_other_comorbidities), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        //otherPreferredLocation.setVisibility(View.GONE);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), willbeScreened.getRadioGroup(), reasonForNotDoingMentalHealthScreening.getSpinner(), otherReasonForNotDoingMentalHealthScreening.getEditText(), akuadsSleep.getRadioGroup(), akuadsLackOfInterest.getRadioGroup(),
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
                akuadsUrination.getRadioGroup(), akuadsTotalScore.getEditText(), akuadsSeverity.getRadioGroup(), majorCauseOfMentalDisturbance, otherMajorCauseOfMentalDisturbance.getEditText(), physicalIllnessMajorCauseOfMentalDisturbance.getEditText(),
                akuadsAgree.getRadioGroup(), akuadsTreatmentFacilityConsent.getRadioGroup(), akuadsPhoneCounsellingConsent.getRadioGroup(),
                preferredTherapyLocationSpinner.getSpinner(), mentalHealthNextScheduledVisit.getButton()/*, otherPreferredLocation.getEditText()*/};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, mentalHealthScreening, willbeScreened, reasonForNotDoingMentalHealthScreening, otherReasonForNotDoingMentalHealthScreening, akuadsSleep, akuadsLackOfInterest, akuadsLostInterestHobbies, akuadsAnxious, akuadsImpendingDoom, akuadsDifficultyThinkingClearly,
                        akuadsAlone, akuadsUnhappy, akuadsHopeless, akuadsHelpless, akuadsWorried, akuadsCried, akuadsSuicide, akuadsLossOfAppetite, akuadsRetrosternalBurning,
                        akuadsIndigestion, akuadsNausea, akuadsConstipation, akuadsDifficultBreathing, akuadsTremulous, akuadsNumbness, akuadsTension, akuadsHeadaches, akuadsBodyPain,
                        akuadsUrination, akuadsTotalScore, akuadsSeverity, majorCauseOfMentalDisturbance, otherMajorCauseOfMentalDisturbance, physicalIllnessMajorCauseOfMentalDisturbance, akuadsAgree, akuadsTreatmentFacilityConsent, akuadsPhoneCounsellingConsent, preferredTherapyLocationSpinner,  mentalHealthNextScheduledVisit}};

        formDate.getButton().setOnClickListener(this);
        reasonForNotDoingMentalHealthScreening.getSpinner().setOnItemSelectedListener(this);
        mentalHealthNextScheduledVisit.getButton().setOnClickListener(this);
        akuadsSleep.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsLackOfInterest.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsLostInterestHobbies.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsAnxious.getRadioGroup().setOnCheckedChangeListener(this);
        willbeScreened.getRadioGroup().setOnCheckedChangeListener(this);
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
        for (CheckBox cb : majorCauseOfMentalDisturbance.getCheckedBoxes()) {
            cb.setOnCheckedChangeListener(this);
        }
        akuadsTreatmentFacilityConsent.getRadioGroup().setOnCheckedChangeListener(this);
        akuadsPhoneCounsellingConsent.getRadioGroup().setOnCheckedChangeListener(this);

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

        if (!(mentalHealthNextScheduledVisit.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = mentalHealthNextScheduledVisit.getButton().getText().toString();
            String formDa1 = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            //Date date = new Date();
            if (secondDateCalendar.before(formDateCalendar/*App.getCalendar(App.stringToDate(formDa1, "yyyy-MM-dd"))*/)) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.next_visit_date_cannot_before_form_date), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                mentalHealthNextScheduledVisit.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                mentalHealthNextScheduledVisit.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                mentalHealthNextScheduledVisit.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        }

        formDate.getButton().setEnabled(true);
        mentalHealthNextScheduledVisit.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {

        Boolean error = false;

        if (otherReasonForNotDoingMentalHealthScreening.getVisibility() == View.VISIBLE && App.get(otherReasonForNotDoingMentalHealthScreening).isEmpty()) {
            gotoPage(1);
            otherReasonForNotDoingMentalHealthScreening.getEditText().setError(getString(R.string.mandatory_field));
            otherReasonForNotDoingMentalHealthScreening.getEditText().requestFocus();
            error = true;
        }

        if (otherMajorCauseOfMentalDisturbance.getVisibility() == View.VISIBLE && App.get(otherMajorCauseOfMentalDisturbance).isEmpty()) {
            gotoPage(1);
            otherMajorCauseOfMentalDisturbance.getEditText().setError(getString(R.string.mandatory_field));
            otherMajorCauseOfMentalDisturbance.getEditText().requestFocus();
            error = true;
        }

        if (physicalIllnessMajorCauseOfMentalDisturbance.getVisibility() == View.VISIBLE && App.get(physicalIllnessMajorCauseOfMentalDisturbance).isEmpty()) {
            gotoPage(1);
            physicalIllnessMajorCauseOfMentalDisturbance.getEditText().setError(getString(R.string.mandatory_field));
            physicalIllnessMajorCauseOfMentalDisturbance.getEditText().requestFocus();
            error = true;
        }

        if (akuadsTreatmentFacilityConsent.getVisibility() == View.VISIBLE && App.get(akuadsTreatmentFacilityConsent).isEmpty()) {
            gotoPage(1);
            akuadsTreatmentFacilityConsent.getQuestionView().setError(getString(R.string.mandatory_field));
            akuadsTreatmentFacilityConsent.getQuestionView().requestFocus();
            error = true;
        }

        if (akuadsPhoneCounsellingConsent.getVisibility() == View.VISIBLE && App.get(akuadsPhoneCounsellingConsent).isEmpty()) {
            gotoPage(1);
            akuadsPhoneCounsellingConsent.getQuestionView().setError(getString(R.string.mandatory_field));
            akuadsPhoneCounsellingConsent.getQuestionView().requestFocus();
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

        if (App.get(akuadsSeverity).equalsIgnoreCase(getResources().getString(R.string.comorbidities_MH_severity_level_normal))) {

            snackbar = Snackbar.make(mainContent, getResources().getString(R.string.comorbidities_akuads_screener_instructions), Snackbar.LENGTH_INDEFINITE)
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
            textView.setMaxLines(4);
            snackbar.show();
        }

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

        observations.add(new String[]{"PATIENT SCREENED FOR MENTAL HEALTH", App.get(willbeScreened).equals(getString(R.string.yes)) ? "YES" : "NO"});

        if (App.get(willbeScreened).equals(getString(R.string.yes))) {
            observations.add(new String[]{"SLEEPING LESS (AKUADS)", App.get(akuadsSleep).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsSleep).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsSleep).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"INTEREST LOSS (AKUADS)", App.get(akuadsLackOfInterest).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsLackOfInterest).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsLackOfInterest).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"INTEREST LOSS IN HOBBIES (AKUADS)", App.get(akuadsLostInterestHobbies).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsLostInterestHobbies).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsLostInterestHobbies).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"ANXIOUS (AKUADS)", App.get(akuadsAnxious).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsAnxious).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsAnxious).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"IMPENDING DOOM SENSATION (AKUADS)", App.get(akuadsImpendingDoom).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsImpendingDoom).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsImpendingDoom).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"DIFFICULTY THINKING CLEARLY (AKUADS)", App.get(akuadsDifficultyThinkingClearly).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsDifficultyThinkingClearly).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsDifficultyThinkingClearly).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"PREFER BEING ALONE (AKUADS)", App.get(akuadsAlone).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsAlone).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsAlone).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"FEELING UNHAPPY (AKUADS)", App.get(akuadsUnhappy).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsUnhappy).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsUnhappy).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"FEELING HOPELESS (AKUADS)", App.get(akuadsHopeless).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsHopeless).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsHopeless).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"FEELING HELPLESS (AKUADS)", App.get(akuadsHelpless).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsHelpless).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsHelpless).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"WORRIED (AKUADS)", App.get(akuadsWorried).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsWorried).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsWorried).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"CRIED (AKUADS)", App.get(akuadsCried).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsCried).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsCried).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"FEELING SUICIDAL (AKUADS)", App.get(akuadsSuicide).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsSuicide).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsSuicide).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"LOSS OF APPETITE (AKUADS)", App.get(akuadsLossOfAppetite).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsLossOfAppetite).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsLossOfAppetite).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"RETROSTERNAL BURNING (AKUADS)", App.get(akuadsRetrosternalBurning).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsRetrosternalBurning).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsRetrosternalBurning).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"INDIGESTION (AKUADS)", App.get(akuadsIndigestion).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsIndigestion).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsIndigestion).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"NAUSEA (AKUADS)", App.get(akuadsNausea).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsNausea).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsNausea).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"CONSTIPATION (AKUADS)", App.get(akuadsConstipation).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsConstipation).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsConstipation).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"BREATHING DIFFICULTY (AKUADS)", App.get(akuadsDifficultBreathing).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsDifficultBreathing).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsDifficultBreathing).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"TREMULOUS (AKUADS)", App.get(akuadsTremulous).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsTremulous).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsTremulous).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"NUMBNESS (AKUADS)", App.get(akuadsNumbness).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsNumbness).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsNumbness).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"TENSION IN NECK AND SHOULDERS (AKUADS)", App.get(akuadsTension).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsTension).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsTension).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"HEADACHE (AKUADS)", App.get(akuadsHeadaches).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsHeadaches).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsHeadaches).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"BODY PAIN (AKUADS)", App.get(akuadsBodyPain).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsBodyPain).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsBodyPain).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"FREQUENT URINATION (AKUADS)", App.get(akuadsUrination).equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) ? "NEVER" :
                    (App.get(akuadsUrination).equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) ? "SOMETIMES" :
                            (App.get(akuadsUrination).equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) ? "MOSTLY" : "ALWAYS"))});
            observations.add(new String[]{"AKUADS SCORE", App.get(akuadsTotalScore)});
            observations.add(new String[]{"AKUADS SEVERITY", App.get(akuadsSeverity).equals(getResources().getString(R.string.comorbidities_MH_severity_level_normal)) ? "NORMAL" :
                    (App.get(akuadsSeverity).equals(getResources().getString(R.string.comorbidities_MH_severity_level_mild)) ? "MILD" :
                            (App.get(akuadsSeverity).equals(getResources().getString(R.string.comorbidities_MH_severity_level_moderate)) ? "MODERATE" : "SEVERE"))});
            if (akuadsAgree.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"THERAPY CONSENT", App.get(akuadsAgree).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
            }
            if (akuadsTreatmentFacilityConsent.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"COUNSELLING AT TREATMENT FACILITY CONSENT", App.get(akuadsTreatmentFacilityConsent).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
            }
            if (akuadsPhoneCounsellingConsent.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"PHONE COUNSELLING CONSENT", App.get(akuadsPhoneCounsellingConsent).equals(getResources().getString(R.string.comorbidities_phone_counselling_consent_weekly)) ? "WEEKLY" :
                        (App.get(akuadsPhoneCounsellingConsent).equals(getResources().getString(R.string.comorbidities_phone_counselling_consent_monthly)) ? "MONTHLY" :
                                (App.get(akuadsPhoneCounsellingConsent).equals(getResources().getString(R.string.comorbidities_phone_counselling_consent_both)) ? "BOTH" : "NONE"))});
            }

            if (majorCauseOfMentalDisturbance.getVisibility() == View.VISIBLE) {
                String majorCauseOfMentalDisturbanceString = "";
                for (CheckBox cb : majorCauseOfMentalDisturbance.getCheckedBoxes()) {
                    if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_physical_illness)))
                        majorCauseOfMentalDisturbanceString = majorCauseOfMentalDisturbanceString + "PHYSICAL ILLNESS" + " ; ";
                    else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_family_members)))
                        majorCauseOfMentalDisturbanceString = majorCauseOfMentalDisturbanceString + "FAMILY PROBLEM" + " ; ";
                    else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_education)))
                        majorCauseOfMentalDisturbanceString = majorCauseOfMentalDisturbanceString + "EDUCATION PROBLEMS" + " ; ";
                    else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_finances)))
                        majorCauseOfMentalDisturbanceString = majorCauseOfMentalDisturbanceString + "ECONOMIC PROBLEM" + " ; ";
                    else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_spouse)))
                        majorCauseOfMentalDisturbanceString = majorCauseOfMentalDisturbanceString + "PROBLEMS RELATED TO SPOUSE" + " ; ";
                    else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_employment)))
                        majorCauseOfMentalDisturbanceString = majorCauseOfMentalDisturbanceString + "EMPLOYMENT PROBLEM" + " ; ";
                    else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_bereavement)))
                        majorCauseOfMentalDisturbanceString = majorCauseOfMentalDisturbanceString + "BEREAVEMENT REACTION" + " ; ";
                    else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_other)))
                        majorCauseOfMentalDisturbanceString = majorCauseOfMentalDisturbanceString + "OTHER MAJOR CAUSE OF MENTAL DISTURBANCE" + " ; ";
                }
                observations.add(new String[]{"MAJOR CAUSE OF MENTAL DISTURBANCE", majorCauseOfMentalDisturbanceString});
            }

            if (otherMajorCauseOfMentalDisturbance.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"OTHER MAJOR CAUSE OF MENTAL DISTURBANCE", App.get(otherMajorCauseOfMentalDisturbance)});
            }
            if (physicalIllnessMajorCauseOfMentalDisturbance.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"PHYSICAL ILLNESS NAME", App.get(physicalIllnessMajorCauseOfMentalDisturbance)});
            }
            if (preferredTherapyLocationSpinner.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"FACILITY REFERRED TO", App.get(preferredTherapyLocationSpinner)});
            }
            if (mentalHealthNextScheduledVisit.getVisibility() == View.VISIBLE) {
                observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDateTime(secondDateCalendar)});
            }
        } else if (App.get(reasonForNotDoingMentalHealthScreening).equals(getResources().getString(R.string.patient_unwell))) {
            observations.add(new String[]{"REASON FOR NOT DOING MENTAL HEALTH SCREENING", "PATIENT UNWELL"});
            observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDateTime(secondDateCalendar)});

        } else if (App.get(reasonForNotDoingMentalHealthScreening).equals(getResources().getString(R.string.other_reason_for_not_doing_mental_health_screening))) {

            observations.add(new String[]{"REASON FOR NOT DOING MENTAL HEALTH SCREENING", "OTHER REASON FOR NOT DOING MENTAL HEALTH SCREENING"});
            observations.add(new String[]{"OTHER REASON FOR NOT DOING MENTAL HEALTH SCREENING", App.get(otherReasonForNotDoingMentalHealthScreening)});

        } else if (App.get(reasonForNotDoingMentalHealthScreening).equals(getResources().getString(R.string.language_barrier))) {
            observations.add(new String[]{"REASON FOR NOT DOING MENTAL HEALTH SCREENING", "LANGUAGE BARRIER"});

        } else if (App.get(reasonForNotDoingMentalHealthScreening).equals(getResources().getString(R.string.less_than_15_years))) {
            observations.add(new String[]{"REASON FOR NOT DOING MENTAL HEALTH SCREENING", "LESS THAN 15 YEARS"});

        } else if (App.get(reasonForNotDoingMentalHealthScreening).equals(getResources().getString(R.string.patient_refused))) {
            observations.add(new String[]{"REASON FOR NOT DOING MENTAL HEALTH SCREENING", "PATIENT REFUSED"});

        } else if (App.get(reasonForNotDoingMentalHealthScreening).equals(getResources().getString(R.string.unable_to_hear_or_speak))) {
            observations.add(new String[]{"REASON FOR NOT DOING MENTAL HEALTH SCREENING", "PATIENT UNABLE TO HEAR/SPEAK"});

        } else if (App.get(reasonForNotDoingMentalHealthScreening).equals(getResources().getString(R.string.patient_does_not_visit_facility))) {
            observations.add(new String[]{"REASON FOR NOT DOING MENTAL HEALTH SCREENING", "PATIENT DOES NOT VISIT FACILITY HIMSELF OR HERSELF"});

        } else if (App.get(reasonForNotDoingMentalHealthScreening).equals(getResources().getString(R.string.none))) {
            observations.add(new String[]{"REASON FOR NOT DOING MENTAL HEALTH SCREENING", "NONE"});
        }
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
                    id = serverService.saveFormLocallyTesting("Comorbidities-Mental Health Screening", form, formDateCalendar,observations.toArray(new String[][]{}));

                String result = serverService.saveEncounterAndObservationTesting("Comorbidities-Mental Health Screening", form, formDateCalendar, observations.toArray(new String[][]{}),id);
                if (!result.contains("SUCCESS"))
                    return result;

                return "SUCCESS";
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
        //formValues.put(firstName.getTag(), App.get(firstName));

        //serverService.saveFormLocally(formName, "12345-5", formValues);

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

            if (obs[0][0].equals("TIME TAKEN TO FILL form")) {
                timeTakeToFill = obs[0][1];
            }

            if (obs[0][0].equals("PATIENT SCREENED FOR MENTAL HEALTH")) {
                for (RadioButton rb : willbeScreened.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        setVisibility(View.VISIBLE);
                        reasonForNotDoingMentalHealthScreening.setVisibility(View.GONE);
                        otherReasonForNotDoingMentalHealthScreening.setVisibility(View.GONE);
                        mentalHealthNextScheduledVisit.setVisibility(View.GONE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        setVisibility(View.GONE);
                        reasonForNotDoingMentalHealthScreening.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }

            if (obs[0][0].equals("REASON FOR NOT DOING MENTAL HEALTH SCREENING")) {
                if (obs[0][1].equals("NONE")) {
                    reasonForNotDoingMentalHealthScreening.getSpinner().selectValue(getString(R.string.none));
                    setVisibility(View.GONE);
                } else if (obs[0][1].equals("LANGUAGE BARRIER")) {
                    reasonForNotDoingMentalHealthScreening.getSpinner().selectValue(getString(R.string.language_barrier));
                    setVisibility(View.GONE);
                } else if (obs[0][1].equals("LESS THAN 15 YEARS")) {
                    reasonForNotDoingMentalHealthScreening.getSpinner().selectValue(getString(R.string.less_than_15_years));
                    setVisibility(View.GONE);
                } else if (obs[0][1].equals("PATIENT UNWELL")) {
                    reasonForNotDoingMentalHealthScreening.getSpinner().selectValue(getString(R.string.patient_unwell));
                    setVisibility(View.GONE);
                    mentalHealthNextScheduledVisit.setVisibility(View.VISIBLE);
                } else if (obs[0][1].equals("PATIENT REFUSED")) {
                    reasonForNotDoingMentalHealthScreening.getSpinner().selectValue(getString(R.string.patient_refused));
                    setVisibility(View.GONE);
                } else if (obs[0][1].equals("PATIENT UNABLE TO HEAR/SPEAK")) {
                    reasonForNotDoingMentalHealthScreening.getSpinner().selectValue(getString(R.string.unable_to_hear_or_speak));
                    setVisibility(View.GONE);
                } else if (obs[0][1].equals("PATIENT DOES NOT VISIT FACILITY HIMSELF OR HERSELF")) {
                    reasonForNotDoingMentalHealthScreening.getSpinner().selectValue(getString(R.string.patient_does_not_visit_facility));
                    setVisibility(View.GONE);

                } else {
                    reasonForNotDoingMentalHealthScreening.getSpinner().selectValue(getString(R.string.other_reason_for_not_doing_mental_health_screening));
                    setVisibility(View.GONE);
                    otherReasonForNotDoingMentalHealthScreening.setVisibility(View.VISIBLE);
                }
            }
            if (obs[0][0].equals("OTHER REASON FOR NOT DOING MENTAL HEALTH SCREENING") && otherReasonForNotDoingMentalHealthScreening.getVisibility() == View.VISIBLE) {
                otherReasonForNotDoingMentalHealthScreening.getEditText().setText(obs[0][1]);
            }
            if (obs[0][0].equals("SLEEPING LESS (AKUADS)")) {
                for (RadioButton rb : akuadsSleep.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INTEREST LOSS (AKUADS)")) {
                for (RadioButton rb : akuadsLackOfInterest.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INTEREST LOSS IN HOBBIES (AKUADS)")) {
                for (RadioButton rb : akuadsLostInterestHobbies.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("ANXIOUS (AKUADS)")) {
                for (RadioButton rb : akuadsAnxious.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("IMPENDING DOOM SENSATION (AKUADS)")) {
                for (RadioButton rb : akuadsImpendingDoom.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("DIFFICULTY THINKING CLEARLY (AKUADS)")) {
                for (RadioButton rb : akuadsDifficultyThinkingClearly.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("PREFER BEING ALONE (AKUADS)")) {
                for (RadioButton rb : akuadsAlone.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("FEELING UNHAPPY (AKUADS)")) {
                for (RadioButton rb : akuadsUnhappy.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("FEELING HOPELESS (AKUADS)")) {
                for (RadioButton rb : akuadsHopeless.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("FEELING HELPLESS (AKUADS)")) {
                for (RadioButton rb : akuadsHelpless.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("WORRIED (AKUADS)")) {
                for (RadioButton rb : akuadsWorried.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("CRIED (AKUADS)")) {
                for (RadioButton rb : akuadsCried.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("FEELING SUICIDAL (AKUADS)")) {
                for (RadioButton rb : akuadsSuicide.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("LOSS OF APPETITE (AKUADS")) {
                for (RadioButton rb : akuadsLossOfAppetite.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("RETROSTERNAL BURNING (AKUADS)")) {
                for (RadioButton rb : akuadsRetrosternalBurning.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INDIGESTION (AKUADS)")) {
                for (RadioButton rb : akuadsIndigestion.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("NAUSEA (AKUADS)")) {
                for (RadioButton rb : akuadsNausea.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("CONSTIPATION (AKUADS)")) {
                for (RadioButton rb : akuadsConstipation.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("BREATHING DIFFICULTY (AKUADS)")) {
                for (RadioButton rb : akuadsDifficultBreathing.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TREMULOUS (AKUADS)")) {
                for (RadioButton rb : akuadsTremulous.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("NUMBNESS (AKUADS)")) {
                for (RadioButton rb : akuadsNumbness.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TENSION IN NECK AND SHOULDERS (AKUADS)")) {
                for (RadioButton rb : akuadsTension.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("HEADACHE (AKUADS)")) {
                for (RadioButton rb : akuadsHeadaches.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("BODY PAIN (AKUADS)")) {
                for (RadioButton rb : akuadsHeadaches.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("FREQUENT URINATION (AKUADS)")) {
                for (RadioButton rb : akuadsHeadaches.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_screening_options_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("AKUADS SCORE")) {
                akuadsTotalScore.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("AKUADS SEVERITY")) {
                for (RadioButton rb : akuadsSeverity.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_severity_level_normal)) && obs[0][1].equals("NORMAL")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_severity_level_mild)) && obs[0][1].equals("MILD")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_severity_level_moderate)) && obs[0][1].equals("MODERATE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_MH_severity_level_severe)) && obs[0][1].equals("SEVERE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("THERAPY CONSENT")) {
                for (RadioButton rb : akuadsAgree.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUNSELLING AT TREATMENT FACILITY CONSENT")) {
                for (RadioButton rb : akuadsTreatmentFacilityConsent.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("PHONE COUNSELLING CONSENT")) {
                for (RadioButton rb : akuadsPhoneCounsellingConsent.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.comorbidities_phone_counselling_consent_weekly)) && obs[0][1].equals("WEEKLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_phone_counselling_consent_monthly)) && obs[0][1].equals("MONTHLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_phone_counselling_consent_both)) && obs[0][1].equals("BOTH")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.comorbidities_phone_counselling_consent_none)) && obs[0][1].equals("NONE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }  else if (obs[0][0].equals("MAJOR CAUSE OF MENTAL DISTURBANCE")) {
                for (CheckBox cb : majorCauseOfMentalDisturbance.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_physical_illness)) && obs[0][1].equals("PHYSICAL ILLNESS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_family_members)) && obs[0][1].equals("FAMILY PROBLEM")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_education)) && obs[0][1].equals("EDUCATION PROBLEMS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_finances)) && obs[0][1].equals("ECONOMIC PROBLEM")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_spouse)) && obs[0][1].equals("PROBLEMS RELATED TO SPOUSE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_employment)) && obs[0][1].equals("EMPLOYMENT PROBLEM")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_bereavement)) && obs[0][1].equals("BEREAVEMENT REACTION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_other)) && obs[0][1].equals("OTHER MAJOR CAUSE OF MENTAL DISTURBANCE")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER MAJOR CAUSE OF MENTAL DISTURBANCE")) {
                otherMajorCauseOfMentalDisturbance.getEditText().setText(obs[0][1]);
                otherMajorCauseOfMentalDisturbance.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("PHYSICAL ILLNESS NAME")) {
                physicalIllnessMajorCauseOfMentalDisturbance.getEditText().setText(obs[0][1]);
                physicalIllnessMajorCauseOfMentalDisturbance.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("FACILITY REFERRED TO")) {
                preferredTherapyLocationSpinner.getSpinner().selectValue(obs[0][1]);
            } else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                mentalHealthNextScheduledVisit.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
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
        } else if (view == mentalHealthNextScheduledVisit.getButton()) {
            mentalHealthNextScheduledVisit.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", false);
            args.putBoolean("allowFutureDate", true);
            args.putString("formDate", formDate.getButtonText());
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public void setVisibility(int visibility) {
        akuadsSleep.setVisibility(visibility);
        akuadsLackOfInterest.setVisibility(visibility);
        akuadsLostInterestHobbies.setVisibility(visibility);
        akuadsAnxious.setVisibility(visibility);
        akuadsImpendingDoom.setVisibility(visibility);
        akuadsDifficultyThinkingClearly.setVisibility(visibility);
        akuadsAlone.setVisibility(visibility);
        akuadsUnhappy.setVisibility(visibility);
        akuadsHopeless.setVisibility(visibility);
        akuadsHelpless.setVisibility(visibility);
        akuadsWorried.setVisibility(visibility);
        akuadsCried.setVisibility(visibility);
        akuadsSuicide.setVisibility(visibility);
        akuadsLossOfAppetite.setVisibility(visibility);
        akuadsRetrosternalBurning.setVisibility(visibility);
        akuadsIndigestion.setVisibility(visibility);
        akuadsNausea.setVisibility(visibility);
        akuadsConstipation.setVisibility(visibility);
        akuadsDifficultBreathing.setVisibility(visibility);
        akuadsTremulous.setVisibility(visibility);
        akuadsNumbness.setVisibility(visibility);
        akuadsTension.setVisibility(visibility);
        akuadsHeadaches.setVisibility(visibility);
        akuadsBodyPain.setVisibility(visibility);
        akuadsUrination.setVisibility(visibility);
        akuadsTotalScore.setVisibility(visibility);
        akuadsSeverity.setVisibility(visibility);
        mentalHealthNextScheduledVisit.setVisibility(visibility);
        otherReasonForNotDoingMentalHealthScreening.setVisibility(visibility);
        akuadsAgree.setVisibility(View.GONE);
        akuadsTreatmentFacilityConsent.setVisibility(View.GONE);
        akuadsPhoneCounsellingConsent.setVisibility(View.GONE);
        preferredTherapyLocationSpinner.setVisibility(View.GONE);
        majorCauseOfMentalDisturbance.setVisibility(visibility);
        otherMajorCauseOfMentalDisturbance.setVisibility(visibility);
        physicalIllnessMajorCauseOfMentalDisturbance.setVisibility(visibility);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == reasonForNotDoingMentalHealthScreening.getSpinner()) {
            if (App.get(reasonForNotDoingMentalHealthScreening).equals(getResources().getString(R.string.none))) {
                setVisibility(View.GONE);
                mentalHealthNextScheduledVisit.setVisibility(View.GONE);
                otherReasonForNotDoingMentalHealthScreening.setVisibility(View.GONE);
            } else if (App.get(reasonForNotDoingMentalHealthScreening).equals(getResources().getString(R.string.patient_unwell))) {
                setVisibility(View.GONE);
                mentalHealthNextScheduledVisit.setVisibility(View.VISIBLE);
            } else if (App.get(reasonForNotDoingMentalHealthScreening).equals(getResources().getString(R.string.other_reason_for_not_doing_mental_health_screening))) {
                setVisibility(View.GONE);
                otherReasonForNotDoingMentalHealthScreening.setVisibility(View.VISIBLE);
            } else {
                setVisibility(View.GONE);
            }
        }

//        if (spinner == reasonForNotDoingMentalHealthScreening.getSpinner()) {
//            if (App.get(reasonForNotDoingMentalHealthScreening).equals(getResources().getString(R.string.none)))
//                for (View[] v : viewGroups) {
//                    for (View v1 : v) {
//                        if (v1 instanceof TitledButton) {
//                            if (App.get(v1).equals(R.string.pet_form_date)) {
//
//                            }
//                        } else if (v1 instanceof MyTextView) {
//                            if (App.get(v1).equals(R.string.comorbidities_akuads_Mental_Health_Screening)) {
//
//                            }
//                        } else if (v1 instanceof TitledSpinner) {
//                            if (App.get(v1).equals(R.string.reason_for_not_doing_mental_health_screening)) {
//
//                            }
//                        }else{
//                            v1.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }
//            else
//                for (View[] v : viewGroups) {
//                    for (View v1 : v) {
//                        if (v1 instanceof TitledButton) {
//                            if (App.get(v1).equals(R.string.pet_form_date)) {
//
//                            }
//                        } else if (v1 instanceof MyTextView) {
//                            if (App.get(v1).equals(R.string.comorbidities_akuads_Mental_Health_Screening)) {
//
//                            }
//                        } else if (v1 instanceof TitledSpinner) {
//                            if (App.get(v1).equals(R.string.reason_for_not_doing_mental_health_screening)) {
//
//                            }
//                        }else{
//                            v1.setVisibility(View.GONE);
//                        }
//                    }
//                }
//        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        for (CheckBox cb : majorCauseOfMentalDisturbance.getCheckedBoxes()) {
            if (App.get(cb).equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_physical_illness))) {
                if (cb.isChecked()) {
                    physicalIllnessMajorCauseOfMentalDisturbance.setVisibility(View.VISIBLE);
                } else {
                    physicalIllnessMajorCauseOfMentalDisturbance.setVisibility(View.GONE);
                }
            }
            if (App.get(cb).equals(getResources().getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_other))) {
                if (cb.isChecked()) {
                    otherMajorCauseOfMentalDisturbance.setVisibility(View.VISIBLE);
                } else {
                    otherMajorCauseOfMentalDisturbance.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        Boolean flag = true;

        akuadsTotalScore.getEditText().setEnabled(false);

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
            displayAkuadsAgreeOrNot();
        } else if (radioGroup == akuadsSeverity.getRadioGroup()) {
            try {

                displayAkuadsAgreeOrNot();
            } catch (Exception e) {
            }
        } else if (radioGroup == akuadsAgree.getRadioGroup()) {

                if (!akuadsSeverity.getRadioGroup().getSelectedValue().equals(getString(R.string.comorbidities_MH_severity_level_normal)) && akuadsAgree.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                    akuadsPhoneCounsellingConsent.setVisibility(View.VISIBLE);
                    preferredTherapyLocationSpinner.setVisibility(View.VISIBLE);
                    mentalHealthNextScheduledVisit.setVisibility(View.VISIBLE);
                    akuadsTreatmentFacilityConsent.setVisibility(View.VISIBLE);
                } else {
                    akuadsTreatmentFacilityConsent.setVisibility(View.GONE);
                    akuadsPhoneCounsellingConsent.setVisibility(View.GONE);
                    preferredTherapyLocationSpinner.setVisibility(View.GONE);
                    mentalHealthNextScheduledVisit.setVisibility(View.GONE);

                    if(snackbar!= null) {
                        snackbar = Snackbar.make(mainContent, getResources().getString(R.string.comorbidities_akuads_screener_instructions), Snackbar.LENGTH_INDEFINITE)
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
                        textView.setMaxLines(4);
                        snackbar.show();
                    }

                }

        } else if (radioGroup == willbeScreened.getRadioGroup()) {

            if (willbeScreened.getRadioGroup().getSelectedValue().equalsIgnoreCase(getString(R.string.yes))) {
                setVisibility(View.VISIBLE);
                reasonForNotDoingMentalHealthScreening.setVisibility(View.GONE);
                otherReasonForNotDoingMentalHealthScreening.setVisibility(View.GONE);
                mentalHealthNextScheduledVisit.setVisibility(View.GONE);

                otherMajorCauseOfMentalDisturbance.setVisibility(View.GONE);
                physicalIllnessMajorCauseOfMentalDisturbance.setVisibility(View.GONE);

                for (CheckBox cb : majorCauseOfMentalDisturbance.getCheckedBoxes()) {
                    if (cb.isChecked() && cb.getText().equals(getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_physical_illness))) {
                        physicalIllnessMajorCauseOfMentalDisturbance.setVisibility(View.VISIBLE);
                    } else if (cb.isChecked() && cb.getText().equals(getString(R.string.comorbidities_major_cause_of_mental_disturbance_options_other))) {
                        otherMajorCauseOfMentalDisturbance.setVisibility(View.VISIBLE);
                    }
                }
                if (!akuadsSeverity.getRadioGroup().getSelectedValue().equals(getString(R.string.comorbidities_MH_severity_level_normal))) {
                    akuadsAgree.setVisibility(View.VISIBLE);
                } else {
                    akuadsAgree.setVisibility(View.GONE);
                }
                if (!akuadsSeverity.getRadioGroup().getSelectedValue().equals(getString(R.string.comorbidities_MH_severity_level_normal)) && akuadsAgree.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
                    akuadsPhoneCounsellingConsent.setVisibility(View.VISIBLE);
                    preferredTherapyLocationSpinner.setVisibility(View.VISIBLE);
                    mentalHealthNextScheduledVisit.setVisibility(View.VISIBLE);
                    akuadsTreatmentFacilityConsent.setVisibility(View.VISIBLE);
                } else {
                    akuadsPhoneCounsellingConsent.setVisibility(View.GONE);
                    preferredTherapyLocationSpinner.setVisibility(View.GONE);
                    mentalHealthNextScheduledVisit.setVisibility(View.GONE);
                    akuadsTreatmentFacilityConsent.setVisibility(View.GONE);

                }

            } else {
                reasonForNotDoingMentalHealthScreening.getSpinner().selectValue(getString(R.string.none));
                setVisibility(View.GONE);
                reasonForNotDoingMentalHealthScreening.setVisibility(View.VISIBLE);

            }

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
            akuadsSeverity.getRadioGroup().check((akuadsSeverity.getRadioGroup().getChildAt(1)).getId());
        } else if (score >= 41 && score <= 60) {
            akuadsSeverity.getRadioGroup().check((akuadsSeverity.getRadioGroup().getChildAt(2)).getId());
        } else if (score >= 61 && score <= 75) {
            akuadsSeverity.getRadioGroup().check((akuadsSeverity.getRadioGroup().getChildAt(3)).getId());
        } else {
            akuadsSeverity.getRadioGroup().check((akuadsSeverity.getRadioGroup().getChildAt(0)).getId());
        }
    }

    void displayAkuadsAgreeOrNot() {
        if ((akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_MH_severity_level_mild)) || akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_MH_severity_level_moderate))
                || akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_MH_severity_level_severe))) && akuadsAgree.getRadioGroup().getSelectedValue().equals(getString(R.string.yes))) {
            akuadsAgree.setVisibility(View.VISIBLE);

            akuadsTreatmentFacilityConsent.setVisibility(View.VISIBLE);
            akuadsPhoneCounsellingConsent.setVisibility(View.VISIBLE);
            preferredTherapyLocationSpinner.setVisibility(View.VISIBLE);
            mentalHealthNextScheduledVisit.setVisibility(View.VISIBLE);

        } else if (akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_MH_severity_level_normal))) {
            akuadsAgree.setVisibility(View.GONE);
            akuadsTreatmentFacilityConsent.setVisibility(View.GONE);
            akuadsPhoneCounsellingConsent.setVisibility(View.GONE);
            preferredTherapyLocationSpinner.setVisibility(View.GONE);
            mentalHealthNextScheduledVisit.setVisibility(View.GONE);
        }
        if(!akuadsSeverity.getRadioGroup().getSelectedValue().equals(getString(R.string.comorbidities_MH_severity_level_normal))){
            akuadsAgree.setVisibility(View.VISIBLE);

        }
    }


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