package com.ihsinformatics.gfatmmobile.comorbidities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.Barcode;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Fawad Jawaid on 12/16/2016.
 */

public class ScreeningFormMHDM extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

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
    TitledRadioGroup akuadsAgree;
    TitledSpinner preferredTherapyLocationSpinner;
    TitledEditText gpClinicCode;
    TitledEditText otherPreferredLocation;
    MyTextView dMScreening;
    TitledRadioGroup screeningFood;
    TitledEditText screeningRBS;
    TitledRadioGroup hba1cTestVoucher;

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

        PAGE_COUNT = 3;
        FORM_NAME = Forms.COMORBIDITIES_SCREENING_FORM;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new com.ihsinformatics.gfatmmobile.comorbidities.ScreeningFormMHDM.MyAdapter());
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
        mentalHealthScreening = new MyTextView(context, getResources().getString(R.string.akuads_Mental_Health_Screening));
        mentalHealthScreening.setTypeface(null, Typeface.BOLD);
        akuadsSleep = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_sleep), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsLackOfInterest = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_lackofinterest), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsLostInterestHobbies = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_lostinteresthobbies), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsAnxious = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_anxious), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsImpendingDoom = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_impendingdoom), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsDifficultyThinkingClearly = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_difficultythinkingclearly), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsAlone = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_alone), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsUnhappy = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_unhappy), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsHopeless = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_hopeless), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsHelpless = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_helpless), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsWorried = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_worried), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsCried = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_cried), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsSuicide = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_suicide), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsLossOfAppetite = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_lossofappetite), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsRetrosternalBurning = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_retrosternalburning), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsIndigestion = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_indigestion), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsNausea = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_nausea), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsConstipation = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_constipation), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsDifficultBreathing = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_difficultybreathing), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsTremulous = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_tremulous), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsNumbness = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_numbness), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsTension = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_tension), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsHeadaches = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_headaches), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsBodyPain = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_bodypain), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsUrination = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_urination), getResources().getStringArray(R.array.comorbidities_MH_screening_options), "Never", App.VERTICAL, App.VERTICAL);
        akuadsTotalScore = new TitledEditText(context, null, getResources().getString(R.string.akuads_totalscore), "", "", 3, RegexUtil.NumericFilter, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        akuadsTotalScore.getEditText().setText(String.valueOf(getTotalScore()));
        akuadsSeverity = new TitledRadioGroup(context, null, getResources().getString(R.string.akuads_severity), getResources().getStringArray(R.array.comorbidities_MH_severity_level), "", App.VERTICAL, App.VERTICAL);
        setAkuadsSeverityLevel();

        // second page views...
        akuadsAgree = new TitledRadioGroup(context, null, getResources().getString(R.string.agree_AKUADS), getResources().getStringArray(R.array.yes_no_options), "Yes", App.VERTICAL, App.VERTICAL);
        preferredTherapyLocationSpinner = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.preferredlocation_id), getResources().getStringArray(R.array.comorbidities_location), "Sehatmand Zindagi Center - Korangi", App.HORIZONTAL);
        gpClinicCode = new TitledEditText(context, null, getResources().getString(R.string.preferredlocation_gpcliniccode), "", "", 4, RegexUtil.NumericFilter, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        displayAkuadsAgreeOrNot();
        displayPreferredTherapyLocationOrNot();
        otherPreferredLocation = new TitledEditText(context, null, getResources().getString(R.string.preferredlocation_other_comorbidities), "", "", 20, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        showOtherPreferredLocation();

        // third page views...
        dMScreening = new MyTextView(context, getResources().getString(R.string.dm_screening));
        dMScreening.setTypeface(null, Typeface.BOLD);
        screeningFood = new TitledRadioGroup(context, null, getResources().getString(R.string.dmscrnng_food), getResources().getStringArray(R.array.yes_no_options), "Yes", App.VERTICAL, App.VERTICAL);
        screeningRBS = new TitledEditText(context, null, getResources().getString(R.string.scrnng_rbs), "0", "", 3, RegexUtil.NumericFilter, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        hba1cTestVoucher = new TitledRadioGroup(context, null, getResources().getString(R.string.hba1c_testvoucher), getResources().getStringArray(R.array.yes_no_options), "Yes", App.VERTICAL, App.VERTICAL);
        displayHba1cTestVoucherOrNot();

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
                akuadsUrination.getRadioGroup(), akuadsTotalScore.getEditText(), akuadsSeverity.getRadioGroup(),
                akuadsAgree.getRadioGroup(), preferredTherapyLocationSpinner.getSpinner(), gpClinicCode.getEditText(), otherPreferredLocation.getEditText(),
                screeningFood.getRadioGroup(), screeningRBS.getEditText(), hba1cTestVoucher.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, mentalHealthScreening, akuadsSleep, akuadsLackOfInterest, akuadsLostInterestHobbies, akuadsAnxious, akuadsImpendingDoom, akuadsDifficultyThinkingClearly,
                        akuadsAlone, akuadsUnhappy, akuadsHopeless, akuadsHelpless, akuadsWorried, akuadsCried, akuadsSuicide, akuadsLossOfAppetite, akuadsRetrosternalBurning,
                        akuadsIndigestion, akuadsNausea, akuadsConstipation, akuadsDifficultBreathing, akuadsTremulous, akuadsNumbness, akuadsTension, akuadsHeadaches, akuadsBodyPain,
                        akuadsUrination, akuadsTotalScore, akuadsSeverity},
                        {akuadsAgree, preferredTherapyLocationSpinner, gpClinicCode, otherPreferredLocation},
                        {dMScreening, screeningFood, screeningRBS, hba1cTestVoucher}};

        formDate.getButton().setOnClickListener(this);
        //scanQRCode.setOnClickListener(this);
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

        preferredTherapyLocationSpinner.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                showOtherPreferredLocation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        screeningRBS.getEditText().setSingleLine(true);
        screeningRBS.getEditText().addTextChangedListener(new TextWatcher() {

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
                    displayHba1cTestVoucherOrNot();
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });
    }

    @Override
    public void updateDisplay() {

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
    }

    @Override
    public boolean validate() {

        Boolean error = false;

        /*if (App.get(ernsNumber).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(5);
            else
                gotoPage(1);
            ernsNumber.getEditText().setError(getString(R.string.empty_field));
            ernsNumber.getEditText().requestFocus();
            error = true;
        }
        if (App.get(indexPatientId).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(5);
            else
                gotoPage(1);
            indexPatientId.getEditText().setError(getString(R.string.empty_field));
            indexPatientId.getEditText().requestFocus();
            error = true;
        }
        if (App.get(age).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(5);
            else
                gotoPage(1);
            age.getEditText().setError(getString(R.string.empty_field));
            age.getEditText().requestFocus();
            error = true;
        }

        if (App.get(husbandName).isEmpty()) {
            gotoFirstPage();
            husbandName.getEditText().setError(getString(R.string.empty_field));
            husbandName.getEditText().requestFocus();
            error = true;
        }
        if (App.get(lastName).isEmpty()) {
            gotoFirstPage();
            lastName.getEditText().setError(getString(R.string.empty_field));
            lastName.getEditText().requestFocus();
            error = true;
        }
        if (App.get(firstName).isEmpty()) {
            gotoFirstPage();
            firstName.getEditText().setError(getString(R.string.empty_field));
            firstName.getEditText().requestFocus();
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

        if (validate()) {

            resetViews();
        }

        return false;
    }

    @Override
    public boolean save() {

        HashMap<String, String> formValues = new HashMap<String, String>();

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        //formValues.put(firstName.getTag(), App.get(firstName));

        serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);

        return true;
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
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

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
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
            displayAkuadsAgreeOrNot();
        } else if (radioGroup == akuadsAgree.getRadioGroup()) {
            displayPreferredTherapyLocationOrNot();
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

        int neverFreq = Collections.frequency(selectedOptions, "Never");
        int sometimesFreq = Collections.frequency(selectedOptions, "Sometimes");
        int mostlyFreq = Collections.frequency(selectedOptions, "Mostly");
        int alwaysFreq = Collections.frequency(selectedOptions, "Always");

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
    }

    void displayAkuadsAgreeOrNot() {
        if (akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase("Mild") || akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase("Moderate")
                || akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase("Severe")) {
            akuadsAgree.setVisibility(View.VISIBLE);
            preferredTherapyLocationSpinner.setVisibility(View.VISIBLE);
            gpClinicCode.setVisibility(View.VISIBLE);
        } else if (akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase("Normal")) {
            akuadsAgree.setVisibility(View.GONE);
            preferredTherapyLocationSpinner.setVisibility(View.GONE);
            gpClinicCode.setVisibility(View.GONE);
            gotoLastPage();
        }
    }

    void displayPreferredTherapyLocationOrNot() {
        if (akuadsAgree.getRadioGroup().getSelectedValue().equalsIgnoreCase("Yes")) {
            preferredTherapyLocationSpinner.setVisibility(View.VISIBLE);
            gpClinicCode.setVisibility(View.VISIBLE);
        } else if (akuadsAgree.getRadioGroup().getSelectedValue().equalsIgnoreCase("No")) {
            preferredTherapyLocationSpinner.setVisibility(View.GONE);
            gpClinicCode.setVisibility(View.GONE);
        }
    }

    void showOtherPreferredLocation() {
        String text = preferredTherapyLocationSpinner.getSpinner().getSelectedItem().toString();

        if (text.equalsIgnoreCase("other")) {
            otherPreferredLocation.setVisibility(View.VISIBLE);
            gpClinicCode.setVisibility(View.GONE);
        } else {
            otherPreferredLocation.setVisibility(View.GONE);
            gpClinicCode.setVisibility(View.VISIBLE);
        }
    }

    void displayHba1cTestVoucherOrNot() {
        if (Integer.parseInt(screeningRBS.getEditText().getText().toString()) >= 200) {
            hba1cTestVoucher.setVisibility(View.VISIBLE);
        } else {
            hba1cTestVoucher.setVisibility(View.GONE);
        }
    }
}