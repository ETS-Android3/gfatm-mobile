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
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 1/5/2017.
 */

public class ZttsChildScreeningForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    Boolean emptyError = false;


    // Views...
    TitledButton formDate;
    TitledEditText blockCode;
    TitledEditText buildingCode;
    TitledEditText dwellingCode;
    TitledEditText householdCode;
    TitledRadioGroup household_status;
    TitledRadioGroup parents_consent;
    TitledRadioGroup parents_consent_not_given;
//    TitledEditText fatherName;

    MyTextView symptomsTextView;
    TitledRadioGroup cough;
    TitledRadioGroup fever;
    TitledRadioGroup feverDuration;
    TitledRadioGroup weightLoss;
    TitledRadioGroup failureThrive;
    TitledRadioGroup playfullnessDescrease;


    MyTextView tbhistoryTextView;
    TitledRadioGroup tb_history;
    TitledRadioGroup tbContactLastTwoYear;


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 1;
        formName = Forms.ZTTS_CHILD_SCREENING;
        form = Forms.ztts_childScreeningForm;

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
        // first page views...
        if (App.getPatient().getPerson().getAge() > 6) {
            submitButton.setEnabled(false);
            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.ztts_patient_age_greater_than_6));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            // DrawableCompat.setTint(clearIcon, color);
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
        household_status = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_household_status), getResources().getStringArray(R.array.ztts_household_status_options), "", App.VERTICAL, App.VERTICAL, true);
//        fatherName = new TitledEditText(context, null, getResources().getString(R.string.fast_father_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        parents_consent = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_parents_consent), getResources().getStringArray(R.array.yes_no_options), "", App.VERTICAL, App.VERTICAL, false);
        parents_consent_not_given = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_parents_consent_not_given), getResources().getStringArray(R.array.ztts_parents_consent_not_given_options), "", App.VERTICAL, App.VERTICAL, false);

        symptomsTextView = new MyTextView(context, getResources().getString(R.string.fast_symptoms_title));
        symptomsTextView.setTypeface(null, Typeface.BOLD);
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_child_cough), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_child_fever), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);
        feverDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_child_fever_duration), getResources().getStringArray(R.array.ztts_cough_duration_options), "", App.VERTICAL, App.VERTICAL, true);
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_child_weight_loss), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);
        failureThrive = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_child_failur_thrive), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);
        playfullnessDescrease = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_child_playfulness_decrease), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);


        tbhistoryTextView = new MyTextView(context, getResources().getString(R.string.fast_tbhistory_title));
        tbhistoryTextView.setTypeface(null, Typeface.BOLD);
        tb_history = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_child_tb_history), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);
        tbContactLastTwoYear = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_child_tb_contact_last_two_year), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), blockCode.getEditText(), buildingCode.getEditText(), dwellingCode.getEditText(), householdCode.getEditText(), parents_consent.getRadioGroup(), parents_consent_not_given.getRadioGroup(),
                cough.getRadioGroup(), fever.getRadioGroup(), feverDuration.getRadioGroup(), weightLoss.getRadioGroup(), failureThrive.getRadioGroup(), playfullnessDescrease.getRadioGroup(), tb_history.getRadioGroup(), tbContactLastTwoYear.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, blockCode, buildingCode, dwellingCode, householdCode, parents_consent, parents_consent_not_given,
                        symptomsTextView, cough, fever, feverDuration, weightLoss, failureThrive, playfullnessDescrease,
                        tbhistoryTextView, tb_history, tbContactLastTwoYear}};


        formDate.getButton().setOnClickListener(this);
        fever.getRadioGroup().setOnCheckedChangeListener(this);
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
        parents_consent.getRadioGroup().setOnCheckedChangeListener(this);


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
        if (parents_consent.getVisibility() == View.VISIBLE && App.get(parents_consent).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            parents_consent.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            parents_consent.getQuestionView().setError(null);
        }

        if (parents_consent_not_given.getVisibility() == View.VISIBLE && App.get(parents_consent_not_given).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            parents_consent_not_given.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            parents_consent_not_given.getQuestionView().setError(null);
        }
        if (cough.getVisibility() == View.VISIBLE && App.get(cough).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cough.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            cough.getQuestionView().setError(null);
        }

        if (fever.getVisibility() == View.VISIBLE && App.get(fever).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            fever.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            fever.getQuestionView().setError(null);
        }

        if (feverDuration.getVisibility() == View.VISIBLE && App.get(feverDuration).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            feverDuration.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            feverDuration.getQuestionView().setError(null);
        }

        if (weightLoss.getVisibility() == View.VISIBLE && App.get(weightLoss).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            weightLoss.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            weightLoss.getQuestionView().setError(null);
        }

        if (failureThrive.getVisibility() == View.VISIBLE && App.get(failureThrive).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            failureThrive.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            failureThrive.getQuestionView().setError(null);
        }

        if (playfullnessDescrease.getVisibility() == View.VISIBLE && App.get(playfullnessDescrease).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            playfullnessDescrease.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            playfullnessDescrease.getQuestionView().setError(null);
        }

        if (tb_history.getVisibility() == View.VISIBLE && App.get(tb_history).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            tb_history.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            tb_history.getQuestionView().setError(null);
        }

        if (tbContactLastTwoYear.getVisibility() == View.VISIBLE && App.get(tbContactLastTwoYear).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            tbContactLastTwoYear.getQuestionView().setError(getResources().getString(R.string.empty_field));
            emptyError = true;
            error = true;
        } else {
            tbContactLastTwoYear.getQuestionView().setError(null);
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
        if (parents_consent.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PARENTS CONSENT", App.get(parents_consent).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(parents_consent).equals(getResources().getString(R.string.no)) ? "NO" : "")});

        if (parents_consent_not_given.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON PARENTS NOT GIVING CONSENT", App.get(parents_consent_not_given).equals(getResources().getString(R.string.ztts_parents_refused)) ? "PARENTS REFUSED" :
                    (App.get(parents_consent_not_given).equals(getResources().getString(R.string.ztts_parents_missed)) ? "PARENTS MISSED" :
                            (App.get(parents_consent_not_given).equals(getResources().getString(R.string.ztts_parents_child_missed)) ? "CHILD MISSED / NOT AVAILABLE" : ""))});

        if (cough.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUGH", App.get(cough).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(cough).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(cough).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (fever.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FEVER", App.get(fever).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(fever).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(fever).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});


        if (feverDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FEVER DURATION", App.get(feverDuration).equals(getResources().getString(R.string.ztts_cough_duration_less_than_2_weeks)) ? "FEVER LASTING LESS THAN TWO WEEKS" :
                    (App.get(feverDuration).equals(getResources().getString(R.string.ztts_cough_duration_2_to_3_weeks)) ? "FEVER LASTING FOR 2 TO 3 WEEKS" :
                            (App.get(feverDuration).equals(getResources().getString(R.string.ztts_cough_duration_more_than_weeks)) ? "FEVER LASTING MORE THAN THREE WEEKS" :
                                    (App.get(feverDuration).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN")))});

        if (weightLoss.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"WEIGHT LOSS", App.get(weightLoss).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(weightLoss).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(weightLoss).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (failureThrive.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FTT (FAILURE TO THRIVE) IN CHILD", App.get(failureThrive).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(failureThrive).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(failureThrive).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (playfullnessDescrease.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PLAYFULNESS IN CHILD DECREASE", App.get(playfullnessDescrease).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(playfullnessDescrease).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(playfullnessDescrease).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (tb_history.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HISTORY OF TUBERCULOSIS", App.get(tb_history).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(tb_history).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(tb_history).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (tbContactLastTwoYear.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CONTACT WITH A TB PATIENT IN LAST TWO YEARS", App.get(tbContactLastTwoYear).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(tbContactLastTwoYear).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(tbContactLastTwoYear).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

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

                String result = serverService.saveEncounterAndObservation(App.getProgram() + "-" + formName, form, formDateCalendar, observations.toArray(new String[][]{}), false);
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
            } else if (obs[0][0].equals("PARENTS CONSENT")) {
                for (RadioButton rb : parents_consent.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }

            } else if (obs[0][0].equals("REASON PARENTS NOT GIVING CONSENT")) {
                for (RadioButton rb : parents_consent_not_given.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ztts_parents_refused)) && obs[0][1].equals("PARENTS REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_parents_missed)) && obs[0][1].equals("PARENTS MISSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ztts_parents_child_missed)) && obs[0][1].equals("CHILD MISSED / NOT AVAILABLE")) {
                        rb.setChecked(true);
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
            } else if (obs[0][0].equals("FTT (FAILURE TO THRIVE) IN CHILD")) {
                for (RadioButton rb : failureThrive.getRadioGroup().getButtons()) {
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
            } else if (obs[0][0].equals("PLAYFULNESS IN CHILD DECREASE")) {
                for (RadioButton rb : playfullnessDescrease.getRadioGroup().getButtons()) {
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
                for (RadioButton rb : tb_history.getRadioGroup().getButtons()) {
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
            } else if (obs[0][0].equals("CONTACT WITH A TB PATIENT IN LAST TWO YEARS")) {
                for (RadioButton rb : tbContactLastTwoYear.getRadioGroup().getButtons()) {
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
        if (radioGroup == fever.getRadioGroup()) {
            if (fever.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                feverDuration.setVisibility(View.VISIBLE);
            } else {
                feverDuration.setVisibility(View.GONE);
            }
        } else if (radioGroup == parents_consent.getRadioGroup()) {
            if (parents_consent.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                parents_consent_not_given.setVisibility(View.GONE);

                symptomsTextView.setVisibility(View.VISIBLE);
                cough.setVisibility(View.VISIBLE);
                fever.setVisibility(View.VISIBLE);
                if (fever.getRadioGroup().getSelectedValue().equals(getString(R.string.yes)))
                    feverDuration.setVisibility(View.VISIBLE);
                weightLoss.setVisibility(View.VISIBLE);
                failureThrive.setVisibility(View.VISIBLE);
                playfullnessDescrease.setVisibility(View.VISIBLE);
                tbhistoryTextView.setVisibility(View.VISIBLE);
                tb_history.setVisibility(View.VISIBLE);
                tbContactLastTwoYear.setVisibility(View.VISIBLE);

            } else {
                parents_consent_not_given.setVisibility(View.VISIBLE);
                //form end
                symptomsTextView.setVisibility(View.GONE);
                cough.setVisibility(View.GONE);
                fever.setVisibility(View.GONE);
                feverDuration.setVisibility(View.GONE);
                weightLoss.setVisibility(View.GONE);
                failureThrive.setVisibility(View.GONE);
                playfullnessDescrease.setVisibility(View.GONE);
                tbhistoryTextView.setVisibility(View.GONE);
                tb_history.setVisibility(View.GONE);
                tbContactLastTwoYear.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

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
