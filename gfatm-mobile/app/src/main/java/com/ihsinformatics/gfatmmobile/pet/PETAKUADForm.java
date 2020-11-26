package com.ihsinformatics.gfatmmobile.pet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
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
        formName = Forms.PET_AKUAD;
        form = Forms.pet_akuad;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new PETAKUADForm.MyAdapter());
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
        mentalHealthScreening = new MyTextView(context, getResources().getString(R.string.pet_akuads_Mental_Health_Screening));
        mentalHealthScreening.setTypeface(null, Typeface.BOLD);
        akuadsSleep = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_sleep), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "SLEEPING LESS (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsLackOfInterest = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_lackofinterest), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "INTEREST LOSS (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsLostInterestHobbies = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_lostinteresthobbies), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "INTEREST LOSS IN HOBBIES (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsAnxious = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_anxious), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "ANXIOUS (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsImpendingDoom = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_impendingdoom), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "IMPENDING DOOM SENSATION (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsDifficultyThinkingClearly = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_difficultythinkingclearly), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "DIFFICULTY THINKING CLEARLY (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsAlone = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_alone), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "PREFER BEING ALONE (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsUnhappy = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_unhappy), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "FEELING UNHAPPY (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsHopeless = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_hopeless), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "FEELING HOPELESS (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsHelpless = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_helpless), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "FEELING HELPLESS (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsWorried = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_worried), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "WORRIED (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsCried = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_cried), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "CRIED (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsSuicide = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_suicide), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "FEELING SUICIDAL (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsLossOfAppetite = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_lossofappetite), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "LOSS OF APPETITE (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsRetrosternalBurning = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_retrosternalburning), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "RETROSTERNAL BURNING (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsIndigestion = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_indigestion), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "INDIGESTION (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsNausea = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_nausea), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "NAUSEA (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsConstipation = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_constipation), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "CONSTIPATION (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsDifficultBreathing = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_difficultybreathing), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "BREATHING DIFFICULTY (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsTremulous = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_tremulous), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "TREMULOUS (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsNumbness = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_numbness), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "NUMBNESS (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsTension = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_tension), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "TENSION IN NECK AND SHOULDERS (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsHeadaches = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_headaches), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "HEADACHE (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsBodyPain = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_bodypain), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "BODY PAIN (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsUrination = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_urination), getResources().getStringArray(R.array.pet_MH_screening_options), getResources().getString(R.string.pet_never), App.VERTICAL, App.VERTICAL, true, "FREQUENT URINATION (AKUADS)", new String[]{"NEVER", "SOMETIMES", "MOSTLY", "ALWAYS"});
        akuadsTotalScore = new TitledEditText(context, null, getResources().getString(R.string.pet_akuads_totalscore), "0", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "AKUADS SCORE");
        akuadsTotalScore.getEditText().setText(String.valueOf(getTotalScore()));
        akuadsTotalScore.getEditText().setFocusable(false);
        akuadsSeverity = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_akuads_severity), getResources().getStringArray(R.array.pet_severity_levels), getResources().getString(R.string.pet_severity_level_normal), App.VERTICAL, App.VERTICAL, true, "AKUADS SEVERITY", new String[]{"NORMAL", "MILD", "MODERATE", "SEVERE"});
        setAkuadsSeverityLevel();
        for (int i = 0; i < akuadsSeverity.getRadioGroup().getChildCount(); i++) {
            akuadsSeverity.getRadioGroup().getChildAt(i).setClickable(false);
        }
        continuationStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_continuatioon_status), getResources().getStringArray(R.array.pet_continuatioon_statuses), getResources().getString(R.string.pet_continue_therapy), App.VERTICAL, App.VERTICAL, true, "CONTINUATION STATUS", new String[]{"EXERCISE THERAPY", "END OF THERAPY", "PATIENT REFERRED", "OTHER CONTINUATION STATUS"});
        otherContinuationStatus = new TitledEditText(context, null, getResources().getString(R.string.pet_other_detail), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER CONTINUATION STATUS");
        akuadsAgree = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_agree_AKUADS), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL, true, "THERAPY CONSENT", getResources().getStringArray(R.array.yes_no_list_concept));
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

        if (refillFlag) {
            refillFlag = true;
            return;
        }

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
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());


            if (formDateCalendar.after(secondDateCalendar)) {

                secondDateCalendar.set(formDateCalendar.get(Calendar.YEAR), formDateCalendar.get(Calendar.MONTH), formDateCalendar.get(Calendar.DAY_OF_MONTH));
                nextAppointmentDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            }

        }

        if (!(nextAppointmentDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {

            String formDa = nextAppointmentDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                nextAppointmentDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                nextAppointmentDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
        }

        formDate.getButton().setEnabled(true);
        nextAppointmentDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {

        Boolean error = super.validate();

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
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

        final ArrayList<String[]> observations = getObservations();

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

                String id = null;
                if (App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocally(formName, form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = "";

                result = serverService.saveEncounterAndObservationTesting(formName, form, formDateCalendar, observations.toArray(new String[][]{}), id);
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
        super.refill(formId);
        refillFlag = true;

        OfflineForm fo = serverService.getSavedFormById(formId);

        ArrayList<String[][]> obsValue = fo.getObsValue();

        for (int i = 0; i < obsValue.size(); i++) {
            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("RETURN VISIT DATE")) {
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
            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");*/
            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar, false, true, false);
        } else if (view == nextAppointmentDate.getButton()) {
            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putString("formDate", formDate.getButtonText());
            args.putBoolean("allowFutureDate", true);
            args.putBoolean("allowPastDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");*/

            nextAppointmentDate.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar, true, false, true);

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
        nextAppointmentDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());


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
        } else if (radioGroup == continuationStatus.getRadioGroup()) {

            if (App.get(continuationStatus).equals(getResources().getString(R.string.pet_other)))
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

        //0 - Mild
        //1 - Moderate
        //2 - Severe
        //3 - Normal

        int score = Integer.parseInt(akuadsTotalScore.getEditText().getText().toString());

        if (score >= 0 && score <= 20) {
            akuadsSeverity.getRadioGroup().check((akuadsSeverity.getRadioGroup().getChildAt(3)).getId());
        } else if (score >= 21 && score <= 40) {
            akuadsSeverity.getRadioGroup().check((akuadsSeverity.getRadioGroup().getChildAt(0)).getId());
        } else if (score >= 41 && score <= 60) {
            akuadsSeverity.getRadioGroup().check((akuadsSeverity.getRadioGroup().getChildAt(1)).getId());
        } else if (score >= 61 && score <= 75) {
            akuadsSeverity.getRadioGroup().check((akuadsSeverity.getRadioGroup().getChildAt(2)).getId());
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