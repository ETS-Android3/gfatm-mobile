package com.ihsinformatics.gfatmmobile.ztts;

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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Haris on 1/5/2017.
 */

public class ZttsScreeningForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    Boolean emptyError = false;


    // Views...
    TitledButton formDate;
    TitledEditText blockCode;
    TitledEditText buildingCode;
    TitledEditText dwellingCode;
    TitledEditText householdCode;

    TitledRadioGroup individual_consent;
    TitledRadioGroup consent_not_filled_reason;


    TitledEditText husbandName;
    TitledEditText fatherName;

    TitledRadioGroup pregnancyHistory;
    TitledRadioGroup smokeHistory;
    TitledRadioGroup diabetes;
    TitledCheckBoxes diabetes_treatmeant;

    MyTextView symptomsTextView;
    TitledRadioGroup cough;
    TitledRadioGroup cough_duration;
    TitledRadioGroup productiveCough;
    TitledRadioGroup haemoptysis;
    TitledRadioGroup fever;
    TitledRadioGroup feverDuration;
    TitledRadioGroup nightSweats;
    TitledRadioGroup weightLoss;
    MyTextView tbhistoryTextView;
    TitledRadioGroup tb_treatment_status;
    TitledRadioGroup tbHistory;
    TitledRadioGroup past_tb_treatment;
    TitledRadioGroup tbContact;
    TitledRadioGroup medical_care;
    TitledRadioGroup presumptiveTb;


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 3;
        formName = Forms.ZTTS_SCREENING;
        form = Forms.ztts_screeningForm;

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
        EventBus.getDefault().register(this);

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.getButton().setTag("date_start");
        blockCode = new TitledEditText(context, null, getResources().getString(R.string.ztts_block_code), "", getResources().getString(R.string.ztts_block_code_hint), 5, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        blockCode.getEditText().setTag("block_code");
        buildingCode = new TitledEditText(context, null, getResources().getString(R.string.ztts_building_code), "", getResources().getString(R.string.ztts_building_code), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        buildingCode.getEditText().setTag("building_code");
        dwellingCode = new TitledEditText(context, null, getResources().getString(R.string.ztts_dwellling_code), "", getResources().getString(R.string.ztts_dwellling_code), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        dwellingCode.getEditText().setTag("dwelling_code");
        householdCode = new TitledEditText(context, null, getResources().getString(R.string.ztts_household_code), "", getResources().getString(R.string.ztts_household_code), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        householdCode.getEditText().setTag("household_code");

        individual_consent = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_individual_consent), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL, false);
        consent_not_filled_reason = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_consent_not_filled_reason), getResources().getStringArray(R.array.ztts_consent_not_filled_reason_options), "", App.VERTICAL, App.VERTICAL, false);


        husbandName = new TitledEditText(context, null, getResources().getString(R.string.fast_husband_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        husbandName.getEditText().setTag("spouse_name");
        fatherName = new TitledEditText(context, null, getResources().getString(R.string.fast_father_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        fatherName.getEditText().setTag("father_name");
        pregnancyHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_is_this_patient_pregnant), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);
        pregnancyHistory.getRadioGroup().setTag("pregnancy_history");
        smokeHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_smoke_history), getResources().getStringArray(R.array.ztts_smoke_options), "", App.VERTICAL, App.VERTICAL, true);
        smokeHistory.getRadioGroup().setTag("cigarette_smoke");
        diabetes = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_diabetes_history), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);
        diabetes.getRadioGroup().setTag("diabetes");
        diabetes_treatmeant = new TitledCheckBoxes(context, null, getResources().getString(R.string.ztts_diabetes_treatment), getResources().getStringArray(R.array.ztts_diabetes_treatment_options), null, App.VERTICAL, App.VERTICAL, true);
        diabetes_treatmeant.getCheckBox(0).setTag("insulin");
        diabetes_treatmeant.getCheckBox(1).setTag("tablets");
        diabetes_treatmeant.getCheckBox(0).setTag("none");

        symptomsTextView = new MyTextView(context, getResources().getString(R.string.fast_symptoms_title));
        symptomsTextView.setTypeface(null, Typeface.BOLD);

        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_cough_history), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);
        cough.getRadioGroup().setTag("cough");
        cough_duration = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_cough_duration), getResources().getStringArray(R.array.ztts_cough_duration_options), "", App.VERTICAL, App.VERTICAL, true);
        cough_duration.getRadioGroup().setTag("cough_duration");

        productiveCough = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_cough_productive_history), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);
        productiveCough.getRadioGroup().setTag("productive_cough");
        haemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_sputum_in_blood), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);
        haemoptysis.getRadioGroup().setTag("haemoptysis");
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_do_you_have_fever), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);
        fever.getRadioGroup().setTag("fever");
        feverDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_how_long_you_have_fever), getResources().getStringArray(R.array.ztts_cough_duration_options), "", App.VERTICAL, App.VERTICAL, true);
        fever.getRadioGroup().setTag("fever");
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_do_you_have_night_sweats), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);
        nightSweats.getRadioGroup().setTag("night_sweats");
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_do_you_have_unexplained_weight_loss), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);
        weightLoss.getRadioGroup().setTag("weight_loss");

        tbhistoryTextView = new MyTextView(context, getResources().getString(R.string.fast_tbhistory_title));
        tbhistoryTextView.setTypeface(null, Typeface.BOLD);

        tb_treatment_status = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_tb_treatment_status), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);
        tb_treatment_status.getRadioGroup().setTag("tb_treatment_status");
        tbHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_tb_before), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);
        tbHistory.getRadioGroup().setTag("tb_history");
        past_tb_treatment = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_past_tb_treatment), getResources().getStringArray(R.array.ztts_past_tb_treatment_options), "", App.VERTICAL, App.VERTICAL, true);
        past_tb_treatment.getRadioGroup().setTag("past_tb_treatment");
        tbContact = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_tb_contact_past_two_years), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);
        tbContact.getRadioGroup().setTag("tb_contact_last_two_years");
        medical_care = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_tb_care), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL, true);
        medical_care.getRadioGroup().setTag("medical_care_sought");
        presumptiveTb = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_presumptive_tb), getResources().getStringArray(R.array.fast_yes_no_list), "", App.VERTICAL, App.VERTICAL, true);
        presumptiveTb.getRadioGroup().setTag("presumptive_tb");


        // Used for reset fields...
        views = new View[]{formDate.getButton(), blockCode.getEditText(), buildingCode.getEditText(), dwellingCode.getEditText(), householdCode.getEditText(), individual_consent.getRadioGroup(), consent_not_filled_reason.getRadioGroup(),
                husbandName.getEditText(), fatherName.getEditText(), pregnancyHistory.getRadioGroup(), smokeHistory.getRadioGroup(), diabetes.getRadioGroup(),
                diabetes_treatmeant, cough.getRadioGroup(), cough_duration.getRadioGroup(), productiveCough.getRadioGroup(), haemoptysis.getRadioGroup(), fever.getRadioGroup(),
                feverDuration.getRadioGroup(), nightSweats.getRadioGroup(), weightLoss.getRadioGroup(), tb_treatment_status.getRadioGroup(), tbHistory.getRadioGroup(), past_tb_treatment.getRadioGroup(), tbContact.getRadioGroup(),
                medical_care.getRadioGroup(), presumptiveTb.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, blockCode, buildingCode, dwellingCode, householdCode, individual_consent, consent_not_filled_reason, husbandName, fatherName, pregnancyHistory,
                        smokeHistory, diabetes, diabetes_treatmeant}, {symptomsTextView, cough, cough_duration, productiveCough, haemoptysis, fever, feverDuration, nightSweats, weightLoss},
                        {tbhistoryTextView, tb_treatment_status, tbHistory, past_tb_treatment, tbContact, presumptiveTb, medical_care}};


        formDate.getButton().setOnClickListener(this);
        for (CheckBox cb : diabetes_treatmeant.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        pregnancyHistory.getRadioGroup().setOnCheckedChangeListener(this);
        diabetes.getRadioGroup().setOnCheckedChangeListener(this);
        cough.getRadioGroup().setOnCheckedChangeListener(this);
        cough_duration.getRadioGroup().setOnCheckedChangeListener(this);
        productiveCough.getRadioGroup().setOnCheckedChangeListener(this);
        haemoptysis.getRadioGroup().setOnCheckedChangeListener(this);
        fever.getRadioGroup().setOnCheckedChangeListener(this);
        nightSweats.getRadioGroup().setOnCheckedChangeListener(this);
        weightLoss.getRadioGroup().setOnCheckedChangeListener(this);
        tb_treatment_status.getRadioGroup().setOnCheckedChangeListener(this);
        tbHistory.getRadioGroup().setOnCheckedChangeListener(this);
        tbContact.getRadioGroup().setOnCheckedChangeListener(this);
        presumptiveTb.getRadioGroup().setOnCheckedChangeListener(this);
        individual_consent.getRadioGroup().setOnCheckedChangeListener(this);
        blockCode.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String blockCodeString = App.get(blockCode);
                if (!String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)).equals(blockCode.toString().toUpperCase().charAt(1)) && blockCodeString.length() >= 2) {
                    StringBuilder sb = new StringBuilder(blockCodeString);
                    blockCodeString = sb.deleteCharAt(1).toString();
                }
                if (!blockCodeString.startsWith(String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)))) {
                    blockCode.getEditText().setText(String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)) + blockCodeString);
                }
                if (blockCode.getEditText().getSelectionStart() == 0)
                    blockCode.getEditText().setSelection(1);

                if (blockCode.getEditText().getText().toString().trim().length() < 4) {
                    blockCode.getEditText().setError("Length shouldn't be < 4");
                } else if (blockCode.getEditText().getText().toString().trim().length() > 5) {
                    blockCode.getEditText().setError("Length shouldn't be > 5");
                } else {
                    blockCode.getEditText().setError(null);
                }
            }
        });


        resetViews();
    }

    public void onEvent(String event) {
        // your implementation
        blockCode.getEditText().setText(String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)));
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

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        }

        formDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {

        Boolean error = false;

        if (blockCode.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            blockCode.getEditText().setError(getString(R.string.empty_field));
            blockCode.getEditText().requestFocus();
            error = true;
        } else {
            if (blockCode.getEditText().getText().toString().trim().length() < 4) {
                blockCode.getEditText().setError("Length shouldn't be < 4");
                blockCode.getEditText().requestFocus();
                error = true;
            }
        }
        if (buildingCode.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            buildingCode.getEditText().setError(getString(R.string.empty_field));
            buildingCode.getEditText().requestFocus();
            error = true;
        }
        if (dwellingCode.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            dwellingCode.getEditText().setError(getString(R.string.empty_field));
            dwellingCode.getEditText().requestFocus();
            error = true;
        }
        if (householdCode.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            householdCode.getEditText().setError(getString(R.string.empty_field));
            householdCode.getEditText().requestFocus();
            error = true;
        }
       /* if (husbandName.getEditText().getText().toString().trim().isEmpty() && husbandName.getVisibility() == View.VISIBLE) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            husbandName.getEditText().setError(getString(R.string.empty_field));
            husbandName.getEditText().requestFocus();
            error = true;
        }
        if (fatherName.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            fatherName.getEditText().setError(getString(R.string.empty_field));
            fatherName.getEditText().requestFocus();
            error = true;
        }
*/

        if (pregnancyHistory.getVisibility() == View.VISIBLE && App.get(pregnancyHistory).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            pregnancyHistory.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            pregnancyHistory.getQuestionView().setError(null);
        }
        if (smokeHistory.getVisibility() == View.VISIBLE && App.get(smokeHistory).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            smokeHistory.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            smokeHistory.getQuestionView().setError(null);
        }
        if (diabetes.getVisibility() == View.VISIBLE && App.get(diabetes).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            diabetes.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            diabetes.getQuestionView().setError(null);
        }
        boolean flag = false;
        if (diabetes_treatmeant.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : diabetes_treatmeant.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                diabetes_treatmeant.getQuestionView().setError(getString(R.string.empty_field));
                error = true;
            } else {
                diabetes_treatmeant.getQuestionView().setError(null);
            }
        }

        if (cough.getVisibility() == View.VISIBLE && App.get(cough).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(1);
            cough.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            cough.getQuestionView().setError(null);
        }
        if (cough_duration.getVisibility() == View.VISIBLE && App.get(cough_duration).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(1);
            cough_duration.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            cough_duration.getQuestionView().setError(null);
        }
        if (productiveCough.getVisibility() == View.VISIBLE && App.get(productiveCough).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(1);
            productiveCough.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            productiveCough.getQuestionView().setError(null);
        }
        if (haemoptysis.getVisibility() == View.VISIBLE && App.get(haemoptysis).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(1);
            haemoptysis.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            haemoptysis.getQuestionView().setError(null);
        }
        if (fever.getVisibility() == View.VISIBLE && App.get(fever).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(1);
            fever.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            fever.getQuestionView().setError(null);
        }
        if (feverDuration.getVisibility() == View.VISIBLE && App.get(feverDuration).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(1);
            feverDuration.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            feverDuration.getQuestionView().setError(null);
        }
        if (nightSweats.getVisibility() == View.VISIBLE && App.get(nightSweats).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(1);
            nightSweats.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            nightSweats.getQuestionView().setError(null);
        }
        if (weightLoss.getVisibility() == View.VISIBLE && App.get(weightLoss).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(1);
            else
                gotoPage(1);
            weightLoss.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            weightLoss.getQuestionView().setError(null);
        }
        if (tb_treatment_status.getVisibility() == View.VISIBLE && App.get(tb_treatment_status).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(2);
            tb_treatment_status.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            tb_treatment_status.getQuestionView().setError(null);
        }
        if (tbHistory.getVisibility() == View.VISIBLE && App.get(tbHistory).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(2);
            tbHistory.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            tbHistory.getQuestionView().setError(null);
        }
        if (tbContact.getVisibility() == View.VISIBLE && App.get(tbContact).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(2);
            tbContact.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            tbContact.getQuestionView().setError(null);
        }
        if (medical_care.getVisibility() == View.VISIBLE && App.get(medical_care).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(2);
            medical_care.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            medical_care.getQuestionView().setError(null);
        }
        if (medical_care.getVisibility() == View.VISIBLE && App.get(medical_care).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(2);
            medical_care.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            medical_care.getQuestionView().setError(null);
        }


        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            if (!emptyError)
                alertDialog.setMessage(getString(R.string.form_error));
            else
                alertDialog.setMessage(getString(R.string.fast_required_field_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            //   DrawableCompat.setTint(clearIcon, color);
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


        if (blockCode.getVisibility() == View.VISIBLE && !(App.get(blockCode).isEmpty()))
            observations.add(new String[]{"BLOCK CODE", App.get(blockCode)});

        if (buildingCode.getVisibility() == View.VISIBLE && !(App.get(buildingCode).isEmpty()))
            observations.add(new String[]{"BUILDING CODE", formatCode(App.get(buildingCode))});

        if (dwellingCode.getVisibility() == View.VISIBLE && !(App.get(dwellingCode).isEmpty()))
            observations.add(new String[]{"DWELLING CODE", formatCode(App.get(dwellingCode))});

        if (householdCode.getVisibility() == View.VISIBLE && !(App.get(householdCode).isEmpty()))
            observations.add(new String[]{"HOUSEHOLD CODE", formatCode(App.get(householdCode))});

        if (App.get(buildingCode).equals("999") && App.get(dwellingCode).equals("999") && App.get(householdCode).equals("999")) {
            observations.add(new String[]{"HOUSEHOLD STATUS", "NON RESIDENT"});
        } else {
            observations.add(new String[]{"HOUSEHOLD STATUS", "RESIDENT"});
        }

        if (individual_consent.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"INDIVIDUAL CONSENT", App.get(individual_consent).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(individual_consent).equals(getResources().getString(R.string.fast_no_title)) ? "NO" : "")});

        if (consent_not_filled_reason.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON FOR CONSENT NOT FILLED", App.get(consent_not_filled_reason).equals(getResources().getString(R.string.ztts_refused)) ? "REFUSED" :
                    (App.get(consent_not_filled_reason).equals(getResources().getString(R.string.ztts_missed_not_available)) ? "MISSED / NOT AVAILABLE" : "")});

        if (husbandName.getVisibility() == View.VISIBLE && !(App.get(husbandName).isEmpty()))
            observations.add(new String[]{"PARTNER FULL NAME", App.get(husbandName)});

        if (fatherName.getVisibility() == View.VISIBLE && !App.get(fatherName).isEmpty())
            observations.add(new String[]{"FATHER NAME", App.get(fatherName)});

        if (pregnancyHistory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PREGNANCY STATUS", App.get(pregnancyHistory).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(pregnancyHistory).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(pregnancyHistory).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (smokeHistory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CIGARETTE SMOKING", App.get(smokeHistory).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(smokeHistory).equals(getResources().getString(R.string.fast_no_title)) ? "NO" : "FORMER SMOKER")});

        if (diabetes.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DIABETES MELLITUS", App.get(diabetes).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(diabetes).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(diabetes).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (diabetes_treatmeant.getVisibility() == View.VISIBLE) {
            String diabetes_treatmeant_String = "";
            for (CheckBox cb : diabetes_treatmeant.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ztts_diabetes_treatment_insulin)))
                    diabetes_treatmeant_String = diabetes_treatmeant_String + "INSULIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ztts_diabetes_treatment_tablets)))
                    diabetes_treatmeant_String = diabetes_treatmeant_String + "TABLET" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ztts_diabetes_treatment_none)))
                    diabetes_treatmeant_String = diabetes_treatmeant_String + "NONE" + " ; ";
            }
            observations.add(new String[]{"DIABETES MELLITUS TREATMENT", diabetes_treatmeant_String});
        }

        if (cough.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUGH", App.get(cough).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(cough).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(cough).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (cough_duration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUGH DURATION", App.get(cough_duration).equals(getResources().getString(R.string.ztts_cough_duration_less_than_2_weeks)) ? "COUGH LASTING LESS THAN 2 WEEKS" :
                    (App.get(cough_duration).equals(getResources().getString(R.string.ztts_cough_duration_2_to_3_weeks)) ? "COUGH LASTING FOR 2 TO 3 WEEKS" :
                            (App.get(cough_duration).equals(getResources().getString(R.string.ztts_cough_duration_more_than_weeks)) ? "COUGH LASTING MORE THAN 3 WEEKS" :
                                    (App.get(cough_duration).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN")))});


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
            observations.add(new String[]{"FEVER DURATION", App.get(feverDuration).equals(getResources().getString(R.string.ztts_cough_duration_less_than_2_weeks)) ? "FEVER LASTING LESS THAN TWO WEEKS" :
                    (App.get(feverDuration).equals(getResources().getString(R.string.ztts_cough_duration_2_to_3_weeks)) ? "FEVER LASTING FOR 2 TO 3 WEEKS" :
                            (App.get(feverDuration).equals(getResources().getString(R.string.ztts_cough_duration_more_than_weeks)) ? "FEVER LASTING MORE THAN THREE WEEKS" :
                                    (App.get(feverDuration).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN")))});

        if (nightSweats.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"NIGHT SWEATS", App.get(nightSweats).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(nightSweats).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(nightSweats).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (weightLoss.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"WEIGHT LOSS", App.get(weightLoss).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(weightLoss).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(weightLoss).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (tb_treatment_status.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOSIS TREATMENT STATUS", App.get(tb_treatment_status).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(tb_treatment_status).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(tb_treatment_status).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (tbHistory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HISTORY OF TUBERCULOSIS", App.get(tbHistory).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(tbHistory).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(tbHistory).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});
        if (past_tb_treatment.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PAST TB TREATMENT HISTORY", App.get(past_tb_treatment).equals(getResources().getString(R.string.ztts_past_tb_treatment_less_than_two_years)) ? "LESS THAN 2 YEARS" :
                    (App.get(past_tb_treatment).equals(getResources().getString(R.string.ztts_past_tb_treatment_two_to_four_years)) ? "2 TO 4 YEARS" :
                            (App.get(past_tb_treatment).equals(getResources().getString(R.string.ztts_past_tb_treatment_more_than_four_years)) ? "MORE THAN 4 YEARS" :
                                    (App.get(past_tb_treatment).equals(getResources().getString(R.string.ztts_past_tb_treatment_not_seek_treatment)) ? "DO NOT SEEK TREATMENT" :
                                            (App.get(past_tb_treatment).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))))});

        if (tbContact.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CONTACT WITH A TB PATIENT IN LAST TWO YEARS", App.get(tbContact).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(tbContact).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(tbContact).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});


        if (medical_care.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SOUGHT MEDICAL CARE", App.get(medical_care).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});


        if (presumptiveTb.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PRESUMPTIVE TUBERCULOSIS", App.get(presumptiveTb).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});


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

                String result = serverService.saveEncounterAndObservation(App.getProgram() + "-" + "Screening", form, formDateCalendar, observations.toArray(new String[][]{}), false);
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
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("BLOCK CODE")) {
                blockCode.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("BUILDING CODE")) {
                buildingCode.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("DWELLING CODE")) {
                dwellingCode.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("HOUSEHOLD CODE")) {
                householdCode.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("INDIVIDUAL CONSENT")) {
                for (RadioButton rb : individual_consent.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }

            } else if (obs[0][0].equals("REASON FOR CONSENT NOT FILLED")) {
                for (RadioButton rb : consent_not_filled_reason.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ztts_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_missed_not_available)) && obs[0][1].equals("MISSED / NOT AVAILABLE")) {
                        rb.setChecked(true);
                        break;
                    }
                }

            } else if (obs[0][0].equals("PARTNER FULL NAME")) {
                husbandName.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("FATHER NAME")) {
                fatherName.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("PREGNANCY STATUS")) {
                for (RadioButton rb : pregnancyHistory.getRadioGroup().getButtons()) {
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

            } else if (obs[0][0].equals("CIGARETTE SMOKING")) {
                for (RadioButton rb : smokeHistory.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_ex_smoker)) && obs[0][1].equals("FORMER SMOKER")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("DIABETES MELLITUS")) {
                for (RadioButton rb : diabetes.getRadioGroup().getButtons()) {
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
            } else if (obs[0][0].equals("DIABETES MELLITUS TREATMENT")) {
                for (CheckBox cb : diabetes_treatmeant.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ztts_diabetes_treatment_insulin)) && obs[0][1].equals("INSULIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ztts_diabetes_treatment_tablets)) && obs[0][1].equals("TABLET")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ztts_diabetes_treatment_none)) && obs[0][1].equals("NONE")) {
                        cb.setChecked(true);
                        break;
                    }
                }
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
            } else if (obs[0][0].equals("COUGH DURATION")) {
                for (RadioButton rb : cough_duration.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ztts_cough_duration_less_than_2_weeks)) && obs[0][1].equals("COUGH LASTING LESS THAN 2 WEEKS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_cough_duration_2_to_3_weeks)) && obs[0][1].equals("COUGH LASTING FOR 2 TO 3 WEEKS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_cough_duration_more_than_weeks)) && obs[0][1].equals("COUGH LASTING MORE THAN 3 WEEKS")) {
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
            } else if (obs[0][0].equals("FEVER DURATION")) {
                for (RadioButton rb : feverDuration.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ztts_cough_duration_less_than_2_weeks)) && obs[0][1].equals("FEVER LASTING LESS THAN TWO WEEKS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_cough_duration_2_to_3_weeks)) && obs[0][1].equals("FEVER LASTING FOR 2 TO 3 WEEKS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_cough_duration_more_than_weeks)) && obs[0][1].equals("FEVER LASTING MORE THAN THREE WEEKS")) {
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
            } else if (obs[0][0].equals("TUBERCULOSIS TREATMENT STATUS")) {
                for (RadioButton rb : tb_treatment_status.getRadioGroup().getButtons()) {
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
            } else if (obs[0][0].equals("PAST TB TREATMENT HISTORY")) {
                for (RadioButton rb : past_tb_treatment.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ztts_past_tb_treatment_less_than_two_years)) && obs[0][1].equals("LESS THAN 2 YEARS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_past_tb_treatment_two_to_four_years)) && obs[0][1].equals("2 TO 4 YEARS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_past_tb_treatment_more_than_four_years)) && obs[0][1].equals("MORE THAN 4 YEARS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_past_tb_treatment_not_seek_treatment)) && obs[0][1].equals("DO NOT SEEK TREATMENT")) {
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
            } else if (obs[0][0].equals("CONTACT WITH A TB PATIENT IN LAST TWO YEARS")) {
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
            } else if (obs[0][0].equals("SOUGHT MEDICAL CARE")) {
                for (RadioButton rb : medical_care.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("PRESUMPTIVE TUBERCULOSIS")) {
                for (RadioButton rb : presumptiveTb.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
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

       /* if (spinner == feverDuration.getSpinner()) {
            feverDuration.getQuestionView().setError(null);
        }*/
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == individual_consent.getRadioGroup()) {
            if (individual_consent.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                consent_not_filled_reason.setVisibility(View.GONE);
            } else {
                consent_not_filled_reason.setVisibility(View.VISIBLE);
            }
        } else if (radioGroup == diabetes.getRadioGroup()) {
            if (diabetes.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                diabetes_treatmeant.setVisibility(View.VISIBLE);
            } else {
                diabetes_treatmeant.setVisibility(View.GONE);
            }
        } else if (radioGroup == cough.getRadioGroup()) {
            if (cough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                cough_duration.setVisibility(View.VISIBLE);
                productiveCough.setVisibility(View.VISIBLE);
                if (productiveCough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title)))
                    haemoptysis.setVisibility(View.VISIBLE);
            } else {
                cough_duration.setVisibility(View.GONE);
                productiveCough.setVisibility(View.GONE);
                haemoptysis.setVisibility(View.GONE);

            }
        } else if (radioGroup == productiveCough.getRadioGroup()) {
            if (productiveCough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                haemoptysis.setVisibility(View.VISIBLE);
            } else {
                haemoptysis.setVisibility(View.GONE);
            }
        } else if (radioGroup == pregnancyHistory.getRadioGroup()) {
            if (pregnancyHistory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                snackbar = Snackbar.make(mainContent, "Please collect 2 sputum sample from patient", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            } else {
                try {
                    //snackbar.dismiss();

                } catch (Exception e) {

                }
            }
        } else if (radioGroup == tbHistory.getRadioGroup()) {
            if (tbHistory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                past_tb_treatment.setVisibility(View.VISIBLE);
            } else {
                past_tb_treatment.setVisibility(View.GONE);
            }
        } else if (radioGroup == fever.getRadioGroup()) {
            if (fever.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                feverDuration.setVisibility(View.VISIBLE);
            } else {
                feverDuration.setVisibility(View.GONE);
            }
        } else if (radioGroup == presumptiveTb.getRadioGroup()) {
            if (presumptiveTb.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                snackbar = Snackbar.make(mainContent, "Please collect 2 sputum sample from patient", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            } else {
                try {
                    //snackbar.dismiss();

                } catch (Exception e) {

                }
            }
        }

        int symptomsCount = 0;

        if (cough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
            symptomsCount++;
        }
        if (fever.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
            symptomsCount++;

        }
        if (nightSweats.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
            symptomsCount++;

        }
        if (weightLoss.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
            symptomsCount++;

        }
        if (tb_treatment_status.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
            symptomsCount++;

        }
        if (tbHistory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
            symptomsCount++;

        }
        if (tbContact.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
            symptomsCount++;

        }


        /*if (symptomsCount > 0) {
            medical_care.setVisibility(View.VISIBLE);
        } else {
            medical_care.setVisibility(View.GONE);
        }
*/

        if ((cough_duration.getVisibility() == View.VISIBLE && (cough_duration.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ztts_cough_duration_2_to_3_weeks))
                || cough_duration.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ztts_cough_duration_more_than_weeks))))
                || (haemoptysis.getVisibility() == View.VISIBLE && haemoptysis.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title)))
                || nightSweats.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))
                || weightLoss.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
            presumptiveTb.getRadioGroup().getButtons().get(0).setChecked(true);
            presumptiveTb.getRadioGroup().getButtons().get(1).setChecked(false);
            medical_care.setVisibility(View.VISIBLE);

        } else {
            presumptiveTb.getRadioGroup().getButtons().get(0).setChecked(false);
            presumptiveTb.getRadioGroup().getButtons().get(1).setChecked(true);

            medical_care.setVisibility(View.GONE);


        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if ((buttonView.getText().equals(getResources().getString(R.string.ztts_diabetes_treatment_insulin)) || buttonView.getText().equals(getResources().getString(R.string.ztts_diabetes_treatment_tablets))) && isChecked) {
            diabetes_treatmeant.getCheckedBoxes().get(2).setChecked(false);
        }

        if (buttonView.getText().equals(getResources().getString(R.string.ztts_diabetes_treatment_none)) && isChecked) {
            diabetes_treatmeant.getCheckedBoxes().get(0).setChecked(false);
            diabetes_treatmeant.getCheckedBoxes().get(1).setChecked(false);
        }


    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        if (App.getPatient().getPerson().getGender().toLowerCase().equals("f")) {
            husbandName.setVisibility(View.VISIBLE);
            pregnancyHistory.setVisibility(View.VISIBLE);
        } else {
            husbandName.setVisibility(View.GONE);
            pregnancyHistory.setVisibility(View.GONE);
        }
        diabetes_treatmeant.setVisibility(View.GONE);
        past_tb_treatment.setVisibility(View.GONE);
        medical_care.setVisibility(View.GONE);
        consent_not_filled_reason.setVisibility(View.GONE);

        blockCode.getEditText().setText(String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)));
        presumptiveTb.getRadioGroup().getButtons().get(0).setClickable(false);
        presumptiveTb.getRadioGroup().getButtons().get(1).setClickable(false);


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


    public String formatCode(String code) {
        String tempCode = null;
        if (code.length() <= 1) {
            tempCode = "00" + code;
        } else if (code.length() <= 2) {
            tempCode = "0" + code;
        } else {
            tempCode = "" + code;
        }
        return tempCode;
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
