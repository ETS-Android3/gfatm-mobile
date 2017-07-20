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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Fawad Jawaid on 19-Jan-17.
 */
public class ComorbiditiesDiabetesEducationForm extends AbstractFormActivity {

    Context context;

    // Views...
    TitledButton formDate;
    //TitledSpinner diabetesEducationFormFollowupMonth;
    TitledEditText diabetesEducationFormFollowupMonth;
    MyTextView diabetesEducationFormEducationalPlan;
    TitledCheckBoxes diabetesEducationFormDiabetesEducation;
    MyTextView diabetesEducationFormEducationalMaterial;
    TitledCheckBoxes diabetesEducationFormDiabetesEducationalMaterial;

    ScrollView scrollView;

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
        FORM_NAME = Forms.COMORBIDITIES_DIABETES_EDUCATION_FORM;
        FORM = Forms.comorbidities_diabetesEducationForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesDiabetesEducationForm.MyAdapter());
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
                scrollView = new ScrollView(mainContent.getContext());
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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        diabetesEducationFormEducationalPlan = new MyTextView(context, getResources().getString(R.string.comorbidities_education_form_educational_plan));
        diabetesEducationFormEducationalPlan.setTypeface(null, Typeface.BOLD);
        //diabetesEducationFormFollowupMonth = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_mth_txcomorbidities_hba1c), getResources().getStringArray(R.array.comorbidities_followup_month), "0", App.HORIZONTAL);
        diabetesEducationFormFollowupMonth = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_urinedr_month_of_treatment), "", getResources().getString(R.string.comorbidities_vitals_month_of_visit_range), 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        diabetesEducationFormDiabetesEducation = new TitledCheckBoxes(context, null, getResources().getString(R.string.comorbidities_education_form_educational_plan_text), getResources().getStringArray(R.array.comorbidities_education_form_educational_plan_text_options), new Boolean[]{true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false}, App.VERTICAL, App.VERTICAL);
        diabetesEducationFormEducationalMaterial = new MyTextView(context, getResources().getString(R.string.comorbidities_education_form_educational_material));
        diabetesEducationFormEducationalMaterial.setTypeface(null, Typeface.BOLD);
        diabetesEducationFormDiabetesEducationalMaterial = new TitledCheckBoxes(context, null, getResources().getString(R.string.comorbidities_education_form_educational_material_text), getResources().getStringArray(R.array.comorbidities_education_form_educational_material_text_options), new Boolean[]{true, false, false, false, false, false, false, false, false, false, false}, App.VERTICAL, App.VERTICAL);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), diabetesEducationFormFollowupMonth.getEditText(), diabetesEducationFormDiabetesEducation, diabetesEducationFormDiabetesEducationalMaterial};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, diabetesEducationFormFollowupMonth, diabetesEducationFormEducationalPlan, diabetesEducationFormDiabetesEducation, diabetesEducationFormEducationalMaterial, diabetesEducationFormDiabetesEducationalMaterial}};

        formDate.getButton().setOnClickListener(this);

        for (CheckBox cb : diabetesEducationFormDiabetesEducation.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        for (CheckBox cb : diabetesEducationFormDiabetesEducationalMaterial.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        diabetesEducationFormFollowupMonth.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (diabetesEducationFormFollowupMonth.getEditText().getText().length() > 0) {
                        int num = Integer.parseInt(diabetesEducationFormFollowupMonth.getEditText().getText().toString());
                        if (num < 0 || num > 24) {
                            diabetesEducationFormFollowupMonth.getEditText().setError(getString(R.string.comorbidities_vitals_month_of_visit_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
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
        formDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {

        Boolean error = false;
        View view = null;

        Boolean flag = false;
        if (diabetesEducationFormDiabetesEducationalMaterial.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : diabetesEducationFormDiabetesEducationalMaterial.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                diabetesEducationFormDiabetesEducationalMaterial.getQuestionView().setError(getString(R.string.empty_field));
                diabetesEducationFormDiabetesEducationalMaterial.getQuestionView().requestFocus();
                view = diabetesEducationFormDiabetesEducationalMaterial;
                error = true;
            }
        }

        flag = false;
        if (diabetesEducationFormDiabetesEducation.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : diabetesEducationFormDiabetesEducation.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                diabetesEducationFormDiabetesEducation.getQuestionView().setError(getString(R.string.empty_field));
                diabetesEducationFormDiabetesEducation.getQuestionView().requestFocus();
                view = diabetesEducationFormDiabetesEducation;
                error = true;
            }
        }

        if (App.get(diabetesEducationFormFollowupMonth).isEmpty()) {
            diabetesEducationFormFollowupMonth.getEditText().setError(getString(R.string.empty_field));
            diabetesEducationFormFollowupMonth.getEditText().requestFocus();
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
            final View finalView = view;
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            scrollView.post(new Runnable() {
                                public void run() {
                                    if (finalView != null) {
                                        scrollView.scrollTo(0, finalView.getTop());
                                    }
                                }
                            });
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
        observations.add(new String[]{"FOLLOW-UP MONTH", App.get(diabetesEducationFormFollowupMonth)});

        String diabetesEducationFormDiabetesEducationString = "";
        for (CheckBox cb : diabetesEducationFormDiabetesEducation.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option1)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "TYPES OF DIABETES" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option2)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "DIABETES COMPLICATIONS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option3)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "ORAL MEDICATION TIMING AND ROUTE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option4)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "SIDE EFFECTS (TEXT)" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option5)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "INSULINE TYPES AND DURATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option6)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "INSULIN INJECTING TECHNIQUE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option7)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "INJECTION SITE ROTATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option8)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "DOSE ADJUSTMENT" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option9)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "GLUCOMETER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option10)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "SELF-MONITORING BLOOD GLUCOSE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option11)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "LIFESTYLE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option12)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "PHYSICAL EXERCISE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option13)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "FASTING WITH DIABETES" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option14)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "TRAVELING" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option15)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "HYPOGLYCAEMIA" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option16)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "HYPERGLYCAEMIA" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option17)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "TREATMENT SUPPORTER EDUCATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option18)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "NOT ELIGIBLE FOR EDUCATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option19)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "FOOTCARE TIPS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option20)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "HELPLINE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option21)))
                diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "MEDICAL TESTS" + " ; ";
        }
        observations.add(new String[]{"DIABETES EDUCATION", diabetesEducationFormDiabetesEducationString});

        String diabetesEducationFormDiabetesEducationalMaterialString = "";
        for (CheckBox cb : diabetesEducationFormDiabetesEducationalMaterial.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option1)))
                diabetesEducationFormDiabetesEducationalMaterialString = diabetesEducationFormDiabetesEducationalMaterialString + "SELF-MONITORING BLOOD GLUCOSE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option2)))
                diabetesEducationFormDiabetesEducationalMaterialString = diabetesEducationFormDiabetesEducationalMaterialString + "HYPOGLYCAEMIA" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option3)))
                diabetesEducationFormDiabetesEducationalMaterialString = diabetesEducationFormDiabetesEducationalMaterialString + "HYPERGLYCAEMIA" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option4)))
                diabetesEducationFormDiabetesEducationalMaterialString = diabetesEducationFormDiabetesEducationalMaterialString + "FOOTCARE TIPS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option5)))
                diabetesEducationFormDiabetesEducationalMaterialString = diabetesEducationFormDiabetesEducationalMaterialString + "DIABETES FACT BOOKLET" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option6)))
                diabetesEducationFormDiabetesEducationalMaterialString = diabetesEducationFormDiabetesEducationalMaterialString + "INSULIN INJECTING TECHNIQUE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option7)))
                diabetesEducationFormDiabetesEducationalMaterialString = diabetesEducationFormDiabetesEducationalMaterialString + "INSULIN DOSAGE CHART" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option8)))
                diabetesEducationFormDiabetesEducationalMaterialString = diabetesEducationFormDiabetesEducationalMaterialString + "DOSE ADJUSTMENT" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option9)))
                diabetesEducationFormDiabetesEducationalMaterialString = diabetesEducationFormDiabetesEducationalMaterialString + "INJECTION SITE ROTATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option10)))
                diabetesEducationFormDiabetesEducationalMaterialString = diabetesEducationFormDiabetesEducationalMaterialString + "FASTING WITH DIABETES" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option11)))
                diabetesEducationFormDiabetesEducationalMaterialString = diabetesEducationFormDiabetesEducationalMaterialString + "HELPLINE" + " ; ";
        }
        observations.add(new String[]{"DIABETES EDUCATION MATERIAL", diabetesEducationFormDiabetesEducationalMaterialString});

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

            if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                diabetesEducationFormFollowupMonth.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("DIABETES EDUCATION")) {
                for (CheckBox cb : diabetesEducationFormDiabetesEducation.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option1)) && obs[0][1].equals("TYPES OF DIABETES")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option2)) && obs[0][1].equals("DIABETES COMPLICATIONS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option3)) && obs[0][1].equals("ORAL MEDICATION TIMING AND ROUTE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option4)) && obs[0][1].equals("SIDE EFFECTS (TEXT)")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option5)) && obs[0][1].equals("INSULINE TYPES AND DURATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option6)) && obs[0][1].equals("INSULIN INJECTING TECHNIQUE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option7)) && obs[0][1].equals("INJECTION SITE ROTATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option8)) && obs[0][1].equals("DOSE ADJUSTMENT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option9)) && obs[0][1].equals("GLUCOMETER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option10)) && obs[0][1].equals("SELF-MONITORING BLOOD GLUCOSE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option11)) && obs[0][1].equals("LIFESTYLE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option12)) && obs[0][1].equals("PHYSICAL EXERCISE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option13)) && obs[0][1].equals("FASTING WITH DIABETES")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option14)) && obs[0][1].equals("TRAVELING")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option15)) && obs[0][1].equals("HYPOGLYCAEMIA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option16)) && obs[0][1].equals("HYPERGLYCAEMIA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option17)) && obs[0][1].equals("TREATMENT SUPPORTER EDUCATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option18)) && obs[0][1].equals("NOT ELIGIBLE FOR EDUCATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option19)) && obs[0][1].equals("FOOTCARE TIPS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option20)) && obs[0][1].equals("HELPLINE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_plan_text_option21)) && obs[0][1].equals("MEDICAL TESTS")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("DIABETES EDUCATION MATERIAL")) {
                for (CheckBox cb : diabetesEducationFormDiabetesEducationalMaterial.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option1)) && obs[0][1].equals("SELF-MONITORING BLOOD GLUCOSE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option2)) && obs[0][1].equals("HYPOGLYCAEMIA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option3)) && obs[0][1].equals("HYPERGLYCAEMIA")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option4)) && obs[0][1].equals("FOOTCARE TIPS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option5)) && obs[0][1].equals("DIABETES FACT BOOKLET")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option6)) && obs[0][1].equals("INSULIN INJECTING TECHNIQUE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option7)) && obs[0][1].equals("INSULIN DOSAGE CHART")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option8)) && obs[0][1].equals("DOSE ADJUSTMENT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option9)) && obs[0][1].equals("INJECTION SITE ROTATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option10)) && obs[0][1].equals("FASTING WITH DIABETES")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_education_form_educational_material_text_option11)) && obs[0][1].equals("HELPLINE")) {
                        cb.setChecked(true);
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

        for (CheckBox cb : diabetesEducationFormDiabetesEducation.getCheckedBoxes()) {
            if (cb.isChecked()) {
                diabetesEducationFormDiabetesEducation.getQuestionView().setError(null);
                break;
            }
        }

        for (CheckBox cb : diabetesEducationFormDiabetesEducationalMaterial.getCheckedBoxes()) {
            if (cb.isChecked()) {
                diabetesEducationFormDiabetesEducationalMaterial.getQuestionView().setError(null);
                break;
            }
        }

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

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

        if(flag) {
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
                    String monthOfTreatment = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.COMORBIDITIES_DIABETES_TREATMENT_FOLLOWUP_FORM, "FOLLOW-UP MONTH");

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

                    diabetesEducationFormFollowupMonth.getEditText().setText(result.get("FOLLOW-UP MONTH"));
                }
            };
            autopopulateFormTask.execute("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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





