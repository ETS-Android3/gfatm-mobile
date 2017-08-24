package com.ihsinformatics.gfatmmobile.pet;

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
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Fawad Jawaid on 16-Dec-16.
 */

public class PETAKUADForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
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
    TitledEditText otherContinuationStatus;
    TitledRadioGroup akuadsAgree;
    TitledButton nextAppointmentDate;

    Boolean refillFlag = false;

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
        FORM_NAME = Forms.PET_AKUAD;
        FORM = Forms.pet_akuad;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new PETAKUADForm.MyAdapter());
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
        mentalHealthScreening = new MyTextView(context, getResources().getString(R.string.pet_akuads_Mental_Health_Screening));
        mentalHealthScreening.setTypeface(null, Typeface.BOLD);
        akuadsSleep = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_sleep), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsLackOfInterest = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_lackofinterest), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsLostInterestHobbies = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_lostinteresthobbies), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsAnxious = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_anxious), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsImpendingDoom = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_impendingdoom), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsDifficultyThinkingClearly = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_difficultythinkingclearly), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsAlone = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_alone), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsUnhappy = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_unhappy), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsHopeless = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_hopeless), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsHelpless = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_helpless), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsWorried = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_worried), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsCried = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_cried), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsSuicide = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_suicide), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsLossOfAppetite = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_lossofappetite), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsRetrosternalBurning = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_retrosternalburning), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsIndigestion = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_indigestion), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsNausea = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_nausea), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsConstipation = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_constipation), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsDifficultBreathing = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_difficultybreathing), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsTremulous = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_tremulous), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsNumbness = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_numbness), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsTension = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_tension), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsHeadaches = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_headaches), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsBodyPain = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_bodypain), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsUrination = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_urination), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL);
        akuadsTotalScore = new TitledEditText(context, null, getResources().getString(R.string.pet_akuads_totalscore), "0", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        akuadsTotalScore.getEditText().setText(String.valueOf(getTotalScore()));
        akuadsTotalScore.getEditText().setFocusable(false);
        akuadsSeverity = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_severity), getResources().getStringArray(R.array.pet_severity_levels), getResources().getString(R.string.pet_severity_level_normal), App.VERTICAL, App.VERTICAL);
        setAkuadsSeverityLevel();
        for (int i = 0; i < akuadsSeverity.getRadioGroup().getChildCount(); i++) {
            akuadsSeverity.getRadioGroup().getChildAt(i).setClickable(false);
        }
        continuationStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_continuatioon_status), getResources().getStringArray(R.array.pet_continuatioon_statuses), getResources().getString(R.string.pet_continue_therapy), App.VERTICAL, App.VERTICAL);
        otherContinuationStatus = new TitledEditText(context, null, getResources().getString(R.string.pet_other_detail), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        akuadsAgree = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_agree_AKUADS), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        nextAppointmentDate = new TitledButton(context, null, getResources().getString(R.string.pet_next_appointment_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.VERTICAL);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), akuadsSleep.getRadioGroup(), akuadsLackOfInterest.getRadioGroup(),
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
                akuadsUrination.getRadioGroup(), akuadsTotalScore.getEditText(), akuadsSeverity.getRadioGroup(), continuationStatus.getRadioGroup(),
                akuadsAgree.getRadioGroup(), nextAppointmentDate.getButton(), otherContinuationStatus.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, mentalHealthScreening, akuadsSleep, akuadsLackOfInterest, akuadsLostInterestHobbies, akuadsAnxious, akuadsImpendingDoom, akuadsDifficultyThinkingClearly,
                        akuadsAlone, akuadsUnhappy, akuadsHopeless, akuadsHelpless, akuadsWorried, akuadsCried, akuadsSuicide, akuadsLossOfAppetite, akuadsRetrosternalBurning,
                        akuadsIndigestion, akuadsNausea, akuadsConstipation, akuadsDifficultBreathing, akuadsTremulous, akuadsNumbness, akuadsTension, akuadsHeadaches, akuadsBodyPain,
                        akuadsUrination, akuadsTotalScore, akuadsSeverity, continuationStatus, otherContinuationStatus, akuadsAgree, nextAppointmentDate}};

        formDate.getButton().setOnClickListener(this);
        nextAppointmentDate.getButton().setOnClickListener(this);
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
        continuationStatus.getRadioGroup().setOnCheckedChangeListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {

        if(refillFlag){
            refillFlag = true;
            return;
        }

        formDate.getButton().setEnabled(true);
        nextAppointmentDate.getButton().setEnabled(true);

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


            if(formDateCalendar.after(secondDateCalendar)){

                secondDateCalendar.set(formDateCalendar.get(Calendar.YEAR), formDateCalendar.get(Calendar.MONTH), formDateCalendar.get(Calendar.DAY_OF_MONTH));
                secondDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
                nextAppointmentDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }

        }

        if (!(nextAppointmentDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = nextAppointmentDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                nextAppointmentDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }

            else
                nextAppointmentDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }
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

        final ArrayList<String[]> observations = new ArrayList<String[]>();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
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
        observations.add(new String[]{"SLEEPING LESS (AKUADS)", App.get(akuadsSleep).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsSleep).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsSleep).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"INTEREST LOSS (AKUADS)", App.get(akuadsLackOfInterest).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsLackOfInterest).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsLackOfInterest).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"INTEREST LOSS IN HOBBIES (AKUADS)", App.get(akuadsLostInterestHobbies).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsLostInterestHobbies).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsLostInterestHobbies).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"ANXIOUS (AKUADS)", App.get(akuadsAnxious).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsAnxious).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsAnxious).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"IMPENDING DOOM SENSATION (AKUADS)", App.get(akuadsImpendingDoom).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsImpendingDoom).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsImpendingDoom).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"DIFFICULTY THINKING CLEARLY (AKUADS)", App.get(akuadsDifficultyThinkingClearly).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsDifficultyThinkingClearly).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsDifficultyThinkingClearly).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"PREFER BEING ALONE (AKUADS)", App.get(akuadsAlone).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsAlone).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsAlone).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"FEELING UNHAPPY (AKUADS)", App.get(akuadsUnhappy).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsUnhappy).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsUnhappy).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"FEELING HOPELESS (AKUADS)", App.get(akuadsHopeless).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsHopeless).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsHopeless).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"FEELING HELPLESS (AKUADS)", App.get(akuadsHelpless).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsHelpless).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsHelpless).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"WORRIED (AKUADS)", App.get(akuadsWorried).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsWorried).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsWorried).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"CRIED (AKUADS)", App.get(akuadsCried).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsCried).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsCried).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"FEELING SUICIDAL (AKUADS)", App.get(akuadsSuicide).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsSuicide).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsSuicide).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"LOSS OF APPETITE (AKUADS)", App.get(akuadsLossOfAppetite).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsLossOfAppetite).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsLossOfAppetite).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"RETROSTERNAL BURNING (AKUADS)", App.get(akuadsRetrosternalBurning).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsRetrosternalBurning).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsRetrosternalBurning).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"INDIGESTION (AKUADS)", App.get(akuadsIndigestion).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsIndigestion).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsIndigestion).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"NAUSEA (AKUADS)", App.get(akuadsNausea).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsNausea).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsNausea).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"CONSTIPATION (AKUADS)", App.get(akuadsConstipation).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsConstipation).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsConstipation).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"BREATHING DIFFICULTY (AKUADS)", App.get(akuadsDifficultBreathing).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsDifficultBreathing).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsDifficultBreathing).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"TREMULOUS (AKUADS)", App.get(akuadsTremulous).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsTremulous).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsTremulous).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"NUMBNESS (AKUADS)", App.get(akuadsNumbness).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsNumbness).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsNumbness).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"TENSION IN NECK AND SHOULDERS (AKUADS)", App.get(akuadsTension).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsTension).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsTension).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"HEADACHE (AKUADS)", App.get(akuadsHeadaches).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsHeadaches).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsHeadaches).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"BODY PAIN (AKUADS)", App.get(akuadsBodyPain).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsBodyPain).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsBodyPain).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"FREQUENT URINATION (AKUADS)", App.get(akuadsUrination).equals(getResources().getString(R.string.pet_never)) ? "NEVER" :
                (App.get(akuadsUrination).equals(getResources().getString(R.string.pet_sometimes)) ? "SOMETIMES" :
                        (App.get(akuadsUrination).equals(getResources().getString(R.string.pet_mostly)) ? "MOSTLY" : "ALWAYS"))});
        observations.add(new String[]{"AKUADS SCORE", App.get(akuadsTotalScore)});
        observations.add(new String[]{"AKUADS SEVERITY", App.get(akuadsSeverity).equals(getResources().getString(R.string.pet_normal)) ? "NORMAL" :
                (App.get(akuadsSeverity).equals(getResources().getString(R.string.pet_severity_level_mild)) ? "MILD" :
                        (App.get(akuadsSeverity).equals(getResources().getString(R.string.pet_severity_level_moderate)) ? "MODERATE" : "SEVERE"))});
        if(akuadsAgree.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"THERAPY CONSENT", App.get(akuadsAgree).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        }
        if(continuationStatus.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"CONTINUATION STATUS", App.get(continuationStatus).equals(getResources().getString(R.string.pet_continue_therapy)) ? "EXERCISE THERAPY" :
                    (App.get(continuationStatus).equals(getResources().getString(R.string.pet_last_session)) ? "END OF THERAPY" :
                            (App.get(continuationStatus).equals(getResources().getString(R.string.pet_referred)) ? "PATIENT REFERRED" : "OTHER CONTINUATION STATUS"))});
        }
        if(otherContinuationStatus.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"OTHER CONTINUATION STATUS", App.get(otherContinuationStatus)});
        }
        observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDate(secondDateCalendar)});

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
                result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
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

        //serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int formId) {

        refillFlag = true;

        OfflineForm fo = serverService.getOfflineFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {
            String[][] obs = obsValue.get(i);

            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            }

            if (obs[0][0].equals("SLEEPING LESS (AKUADS)")) {
                for (RadioButton rb : akuadsSleep.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INTEREST LOSS (AKUADS)")) {
                for (RadioButton rb : akuadsLackOfInterest.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INTEREST LOSS IN HOBBIES (AKUADS)")) {
                for (RadioButton rb : akuadsLostInterestHobbies.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("ANXIOUS (AKUADS)")) {
                for (RadioButton rb : akuadsAnxious.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("IMPENDING DOOM SENSATION (AKUADS)")) {
                for (RadioButton rb : akuadsImpendingDoom.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("DIFFICULTY THINKING CLEARLY (AKUADS)")) {
                for (RadioButton rb : akuadsDifficultyThinkingClearly.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("PREFER BEING ALONE (AKUADS)")) {
                for (RadioButton rb : akuadsAlone.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("FEELING UNHAPPY (AKUADS)")) {
                for (RadioButton rb : akuadsUnhappy.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("FEELING HOPELESS (AKUADS)")) {
                for (RadioButton rb : akuadsHopeless.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("FEELING HELPLESS (AKUADS)")) {
                for (RadioButton rb : akuadsHelpless.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("WORRIED (AKUADS)")) {
                for (RadioButton rb : akuadsWorried.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("CRIED (AKUADS)")) {
                for (RadioButton rb : akuadsCried.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("FEELING SUICIDAL (AKUADS)")) {
                for (RadioButton rb : akuadsSuicide.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("LOSS OF APPETITE (AKUADS")) {
                for (RadioButton rb : akuadsLossOfAppetite.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("RETROSTERNAL BURNING (AKUADS)")) {
                for (RadioButton rb : akuadsRetrosternalBurning.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INDIGESTION (AKUADS)")) {
                for (RadioButton rb : akuadsIndigestion.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("NAUSEA (AKUADS)")) {
                for (RadioButton rb : akuadsNausea.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("CONSTIPATION (AKUADS)")) {
                for (RadioButton rb : akuadsConstipation.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("BREATHING DIFFICULTY (AKUADS)")) {
                for (RadioButton rb : akuadsDifficultBreathing.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TREMULOUS (AKUADS)")) {
                for (RadioButton rb : akuadsTremulous.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("NUMBNESS (AKUADS)")) {
                for (RadioButton rb : akuadsNumbness.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TENSION IN NECK AND SHOULDERS (AKUADS)")) {
                for (RadioButton rb : akuadsTension.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("HEADACHE (AKUADS)")) {
                for (RadioButton rb : akuadsHeadaches.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("BODY PAIN (AKUADS)")) {
                for (RadioButton rb : akuadsBodyPain.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("FREQUENT URINATION (AKUADS)")) {
                for (RadioButton rb : akuadsUrination.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_never)) && obs[0][1].equals("NEVER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_sometimes)) && obs[0][1].equals("SOMETIMES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mostly)) && obs[0][1].equals("MOSTLY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_always)) && obs[0][1].equals("ALWAYS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("AKUADS SCORE")) {
                akuadsTotalScore.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("AKUADS SEVERITY")) {
                for (RadioButton rb : akuadsSeverity.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_severity_level_normal)) && obs[0][1].equals("NORMAL")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_severity_level_mild)) && obs[0][1].equals("MILD")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_severity_level_moderate)) && obs[0][1].equals("MODERATE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_severity_level_severe)) && obs[0][1].equals("SEVERE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("CONTINUATION STATUS")) {
                for (RadioButton rb : continuationStatus.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_continue_therapy)) && obs[0][1].equals("EXERCISE THERAPY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_last_session)) && obs[0][1].equals("END OF THERAPY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_referred)) && obs[0][1].equals("PATIENT REFERRED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_other)) && obs[0][1].equals("OTHER CONTINUATION STATUS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER CONTINUATION STATUS")) {
                otherContinuationStatus.getEditText().setText(obs[0][1]);
                otherContinuationStatus.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("LOSS OF APPETITE (AKUADS)")) {
                for (RadioButton rb : akuadsLossOfAppetite.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_severity_level_normal)) && obs[0][1].equals("NORMAL")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_severity_level_mild)) && obs[0][1].equals("MILD")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_severity_level_moderate)) && obs[0][1].equals("MODERATE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_severity_level_severe)) && obs[0][1].equals("SEVERE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("THERAPY CONSENT")) {
                for (RadioButton rb : akuadsAgree.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("RETURN VISIT DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                nextAppointmentDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
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
            formDate.getButton().setEnabled(false);
        }
        else if (view == nextAppointmentDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putString("formDate", formDate.getButtonText());
            args.putBoolean("allowFutureDate", true);
            args.putBoolean("allowPastDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            nextAppointmentDate.getButton().setEnabled(false);
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

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        otherContinuationStatus.setVisibility(View.GONE);

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
        }
        else if (radioGroup == continuationStatus.getRadioGroup()){

            if(App.get(continuationStatus).equals(getResources().getString(R.string.pet_other)))
                otherContinuationStatus.setVisibility(View.VISIBLE);
            else
                otherContinuationStatus.setVisibility(View.GONE);
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

        int neverFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.pet_never));
        int sometimesFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.pet_sometimes));
        int mostlyFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.pet_mostly));
        int alwaysFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.pet_always));

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
        }
        else {
            akuadsSeverity.getRadioGroup().check((akuadsSeverity.getRadioGroup().getChildAt(0)).getId());
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