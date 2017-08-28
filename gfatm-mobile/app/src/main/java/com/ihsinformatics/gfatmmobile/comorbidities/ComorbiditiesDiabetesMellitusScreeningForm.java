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

import static android.support.design.widget.Snackbar.*;

/**
 * Created by Fawad Jawaid on 09-Jan-17.
 */

public class ComorbiditiesDiabetesMellitusScreeningForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    Boolean emptyError = false;
    TitledButton formDate;
    //MyTextView dMScreening;
    MyTextView dMScreeningDiabetesDiagnosed;
    TitledRadioGroup diagnosedWithDiabetes;
    TitledCheckBoxes diabetesDiagnosedThrough;
    TitledRadioGroup hba1cSiteAvailability;
    TitledRadioGroup screeningFood;
    TitledEditText screeningRBS;
    TitledRadioGroup hba1cTestVoucher;

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
        FORM_NAME = Forms.COMORBIDITIES_DIABETES_MELLITUS_SCREENING_FORM;
        FORM = Forms.comorbidities_diabetesMellitusScreening;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesDiabetesMellitusScreeningForm.MyAdapter());
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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        //dMScreening = new MyTextView(context, getResources().getString(R.string.comorbidities_dm_screening));
        //dMScreening.setTypeface(null, Typeface.BOLD);
        dMScreeningDiabetesDiagnosed = new MyTextView(context, getResources().getString(R.string.comorbidities_dm_screening_diabetes_diagnosed));
        dMScreeningDiabetesDiagnosed.setTypeface(null, Typeface.BOLD);
        diagnosedWithDiabetes = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_dmscrnng_diagnosed_with_diabetes), getResources().getStringArray(R.array.comorbidities_yes_no), "", App.VERTICAL, App.VERTICAL, true);
        diabetesDiagnosedThrough = new TitledCheckBoxes(context, null, getResources().getString(R.string.comorbidities_dmscrnng_diagnosed_through), getResources().getStringArray(R.array.comorbidities_dmscrnng_diagnosed_through_options), new Boolean[]{false, false, false, false, false, false}, App.VERTICAL, App.VERTICAL);
        hba1cSiteAvailability = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_dmscrnng_hba1c_availability), getResources().getStringArray(R.array.comorbidities_yes_no), "", App.VERTICAL, App.VERTICAL, true);
        screeningFood = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_dmscrnng_food), getResources().getStringArray(R.array.comorbidities_yes_no), "", App.VERTICAL, App.VERTICAL, true);
        screeningRBS = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_scrnng_rbs), "", getResources().getString(R.string.comorbidities_scrnng_rbs_range), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        hba1cTestVoucher = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hba1c_testvoucher), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        displayHba1cTestVoucherOrNot();
        diabetesDiagnosedThrough.setVisibility(View.GONE);
        screeningFood.setVisibility(View.GONE);

        Log.v("SCREENING FOOD", App.get(screeningFood)+"");

        // Used for reset fields...
        views = new View[]{diagnosedWithDiabetes.getRadioGroup(), diabetesDiagnosedThrough, hba1cSiteAvailability.getRadioGroup(), screeningFood.getRadioGroup(), screeningRBS.getEditText(), hba1cTestVoucher.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, dMScreeningDiabetesDiagnosed/*dMScreening*/,diagnosedWithDiabetes, diabetesDiagnosedThrough, hba1cSiteAvailability, screeningFood, screeningRBS, hba1cTestVoucher}};

        formDate.getButton().setOnClickListener(this);
        diagnosedWithDiabetes.getRadioGroup().setOnCheckedChangeListener(this);
        hba1cSiteAvailability.getRadioGroup().setOnCheckedChangeListener(this);
        screeningFood.getRadioGroup().setOnCheckedChangeListener(this);
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
                    if (screeningRBS.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(screeningRBS.getEditText().getText().toString());
                        if (num < 1 || num > 500) {
                            screeningRBS.getEditText().setError(getString(R.string.comorbidities_scrnng_rbs_limit));
                        } else {
                            displayHba1cTestVoucherOrNot();
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

                snackbar = make(mainContent, getResources().getString(R.string.form_date_future), LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), LENGTH_INDEFINITE);
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
        if (App.get(screeningRBS).isEmpty()) {
            gotoLastPage();
            screeningRBS.getEditText().setError(getString(R.string.empty_field));
            screeningRBS.getEditText().requestFocus();
            error = true;
        }
        else if (!App.get(screeningRBS).isEmpty() && (Double.parseDouble(App.get(screeningRBS)) < 1 || Double.parseDouble(App.get(screeningRBS)) > 500)) {
            gotoLastPage();
            screeningRBS.getEditText().setError(getString(R.string.comorbidities_scrnng_rbs_limit));
            screeningRBS.getEditText().requestFocus();
            error = true;
        }

        if (screeningFood.getVisibility() == View.VISIBLE && App.get(screeningFood).isEmpty()) {
            emptyError = true;
            error = true;
        }

        if (hba1cSiteAvailability.getVisibility() == View.VISIBLE && App.get(hba1cSiteAvailability).isEmpty()) {
            emptyError = true;
            error = true;
        }

        flag = false;
        if (diabetesDiagnosedThrough.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : diabetesDiagnosedThrough.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                diabetesDiagnosedThrough.getQuestionView().setError(getString(R.string.empty_field));
                diabetesDiagnosedThrough.getQuestionView().requestFocus();
                view = diabetesDiagnosedThrough;
                error = true;
            }
        }

        if (diagnosedWithDiabetes.getVisibility() == View.VISIBLE && App.get(diagnosedWithDiabetes).isEmpty()) {
            emptyError = true;
            error = true;
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            if(!emptyError)
                alertDialog.setMessage(getString(R.string.form_error));
            else
                alertDialog.setMessage(getString(R.string.comorbidities_required_field_error));

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
                                        screeningRBS.clearFocus();
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
        observations.add(new String[]{"DIABETES DIAGNOSED", App.get(diagnosedWithDiabetes).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        if(diabetesDiagnosedThrough.getVisibility() == View.VISIBLE) {
            String diabetesEducationFormDiabetesEducationString = "";
            for (CheckBox cb : diabetesDiagnosedThrough.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_dmscrnng_diagnosed_through_hba1c)))
                    diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "HBA1C REPORT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_dmscrnng_diagnosed_through_doctors_prescription)))
                    diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "DOCTORS PRESCRIPTION" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_dmscrnng_diagnosed_through_rbs)))
                    diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "RBS REPORT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_dmscrnng_diagnosed_through_fbs)))
                    diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "FBS REPORT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_dmscrnng_diagnosed_through_ogtt)))
                    diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "OGTT REPORT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_dmscrnng_diagnosed_through_no_proof)))
                    diabetesEducationFormDiabetesEducationString = diabetesEducationFormDiabetesEducationString + "PATIENT DOESN'T HAVE PROOF OF REPORT" + " ; ";
            }
            observations.add(new String[]{"DIABETES DIAGNOSED THROUGH", diabetesEducationFormDiabetesEducationString});
        }
        observations.add(new String[]{"AVAILABILITY OF HBA1C AT SITE", App.get(hba1cSiteAvailability).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"FOOD CONSUMPTION IN PAST 2 HOURS", App.get(screeningFood).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"RANDOM BLOOD SUGAR", App.get(screeningRBS)});
        if(hba1cTestVoucher.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"VOUCHER GIVEN FOR TEST", App.get(hba1cTestVoucher).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
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

            if (obs[0][0].equals("DIABETES DIAGNOSED")) {
                for (RadioButton rb : diagnosedWithDiabetes.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }
            else if (obs[0][0].equals("DIABETES DIAGNOSED THROUGH")) {
                for (CheckBox cb : diabetesDiagnosedThrough.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.comorbidities_dmscrnng_diagnosed_through_hba1c)) && obs[0][1].equals("HBA1C REPORT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_dmscrnng_diagnosed_through_doctors_prescription)) && obs[0][1].equals("DOCTORS PRESCRIPTION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_dmscrnng_diagnosed_through_rbs)) && obs[0][1].equals("RBS REPORT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_dmscrnng_diagnosed_through_fbs)) && obs[0][1].equals("FBS REPORT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_dmscrnng_diagnosed_through_ogtt)) && obs[0][1].equals("OGTT REPORT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.comorbidities_dmscrnng_diagnosed_through_no_proof)) && obs[0][1].equals("PATIENT DOESN'T HAVE PROOF OF REPORT")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            }
            else if (obs[0][0].equals("AVAILABILITY OF HBA1C AT SITE")) {
                for (RadioButton rb : hba1cSiteAvailability.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }
            else if (obs[0][0].equals("FOOD CONSUMPTION IN PAST 2 HOURS")) {
                for (RadioButton rb : screeningFood.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("RANDOM BLOOD SUGAR")) {
                screeningRBS.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("VOUCHER GIVEN FOR TEST")) {
                for (RadioButton rb : hba1cTestVoucher.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
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

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        displayHba1cTestVoucherOrNot();

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

    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        snackbar = Snackbar.make(mainContent, getResources().getString(R.string.comorbidities_dmscrnng_screener_instructions), Snackbar.LENGTH_INDEFINITE)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });

        // Changing message text color
        //snackbar.setActionTextColor(Color.RED);

        //Changing Typeface of Snackbar Action text
        TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById( android.support.design.R.id.snackbar_action );
        snackbarActionTextView.setTextSize(20);
        snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);

        // Setting Maximum lines for the textview in snackbar
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(3);
        snackbar.show();
    }*/

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if(radioGroup == screeningFood.getRadioGroup()) {

            snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.comorbidities_dmscrnng_screener_instructions), Snackbar.LENGTH_INDEFINITE)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });

            // Changing message text color
            //snackbar.setActionTextColor(Color.RED);

            //Changing Typeface of Snackbar Action text
            TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById( android.support.design.R.id.snackbar_action );
            snackbarActionTextView.setTextSize(20);
            snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);

            // Setting Maximum lines for the textview in snackbar
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(3);

            if(App.get(screeningFood).equalsIgnoreCase(getResources().getString(R.string.yes))) {
                if(snackbar != null)
                    snackbar.show();
            }
            else if(App.get(screeningFood).equalsIgnoreCase(getResources().getString(R.string.no))) {
                if(snackbar != null)
                    snackbar.dismiss();
            }
        }
        else if(radioGroup == diagnosedWithDiabetes.getRadioGroup()) {
            displayDiagnosedThrough();
        }
        else if(radioGroup == hba1cSiteAvailability.getRadioGroup()) {
            displayScreeningFoodOrNot();
        }

    }

    @Override
    public void onPageSelected(int pageNo) {

    }

    void displayHba1cTestVoucherOrNot() {
        try {
            if (Integer.parseInt(screeningRBS.getEditText().getText().toString()) >= 200) {
                hba1cTestVoucher.setVisibility(View.VISIBLE);
            } else {
                hba1cTestVoucher.setVisibility(View.GONE);
            }
        }
        catch (NumberFormatException nfe) {
            //Exception: User might be entering " " (empty) value
            hba1cTestVoucher.setVisibility(View.GONE);
        }
    }

    void displayDiagnosedThrough() {

        snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.comorbidities_dmscrnng_screener_instructions_proof_of_Diabetes), Snackbar.LENGTH_INDEFINITE)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });

        // Changing message text color
        //snackbar.setActionTextColor(Color.RED);

        //Changing Typeface of Snackbar Action text
        TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById( android.support.design.R.id.snackbar_action );
        snackbarActionTextView.setTextSize(20);
        snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);

        // Setting Maximum lines for the textview in snackbar
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(3);

            if(App.get(diagnosedWithDiabetes).equalsIgnoreCase(getResources().getString(R.string.yes))) {
                if(snackbar != null)
                    snackbar.show();
                diabetesDiagnosedThrough.setVisibility(View.VISIBLE);
            }
            else if(App.get(diagnosedWithDiabetes).equalsIgnoreCase(getResources().getString(R.string.no))) {
                if(snackbar != null)
                    snackbar.dismiss();
                diabetesDiagnosedThrough.setVisibility(View.GONE);
            }
    }

    void displayScreeningFoodOrNot() {
        if(App.get(hba1cSiteAvailability).equalsIgnoreCase(getResources().getString(R.string.yes))) {
            screeningFood.setVisibility(View.GONE);
        }
        else if(App.get(hba1cSiteAvailability).equalsIgnoreCase(getResources().getString(R.string.no))) {
            screeningFood.setVisibility(View.VISIBLE);
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