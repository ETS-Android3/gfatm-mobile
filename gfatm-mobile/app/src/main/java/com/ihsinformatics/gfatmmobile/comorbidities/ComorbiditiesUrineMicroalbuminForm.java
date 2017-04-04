package com.ihsinformatics.gfatmmobile.comorbidities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Fawad Jawaid on 19-Jan-17.
 */

public class ComorbiditiesUrineMicroalbuminForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener, View.OnTouchListener {

    public static final int THIRD_DATE_DIALOG_ID = 3;
    // Extra Views for date ...
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;
    protected Calendar fourthDateCalendar;
    Context context;
    // Views...
    TitledButton formDate;
    TitledRadioGroup formType;
    //Views for Test Order Urine Microalbumin
    MyTextView testOrderMicroablbumin;
    TitledSpinner microalbuminFollowupMonth;
    TitledButton microalbuminTestOrderDate;
    TitledEditText microalbuminTestID;
    ImageView testIdView;
    //Views for Test Result Urine Microalbumin
    MyTextView testResultMicroalbumin;
    TitledButton microalbuminTestResultDate;
    TitledEditText microalbuminResult;
    TitledButton nextMicroalbuminTestDate;

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
        FORM_NAME = Forms.COMORBIDITIES_MICROALBUMIN_TEST_FORM;
        FORM = Forms.comorbidities_microalbuminTestForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesUrineMicroalbuminForm.MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(PAGE_COUNT - 1);
        formName.setText(FORM_NAME);

        thirdDateCalendar = Calendar.getInstance();
        thirdDateFragment = new ComorbiditiesUrineMicroalbuminForm.SelectDateFragment();

        fourthDateCalendar = Calendar.getInstance();

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
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_testorder_testresult_form_type), getResources().getStringArray(R.array.comorbidities_testorder_testresult_form_type_options), "", App.HORIZONTAL, App.VERTICAL);
        testOrderMicroablbumin = new MyTextView(context, getResources().getString(R.string.comorbidities_creatinine_test_order));
        testOrderMicroablbumin.setTypeface(null, Typeface.BOLD);
        microalbuminFollowupMonth = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_mth_txcomorbidities_hba1c), getResources().getStringArray(R.array.comorbidities_followup_month), "1", App.HORIZONTAL);
        microalbuminTestOrderDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_hba1cdate_test_order), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        LinearLayout linearLayout = new LinearLayout(context);
        microalbuminTestID = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_hhba1c_testid), "", "", 20, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.9f
        );
        microalbuminTestID.setLayoutParams(param);
        linearLayout.addView(microalbuminTestID);
        testIdView = new ImageView(context);
        testIdView.setImageResource(R.drawable.ic_checked);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.1f
        );
        testIdView.setLayoutParams(param1);
        testIdView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        testIdView.setPadding(0, 5, 0, 0);

        linearLayout.addView(testIdView);

        //second page views...
        testResultMicroalbumin = new MyTextView(context, getResources().getString(R.string.comorbidities_creatinine_test_result));
        testResultMicroalbumin.setTypeface(null, Typeface.BOLD);
        microalbuminTestResultDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_hba1c_resultdate), DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);
        //microalbuminResult = new TitledEditText(context, null, getResources().getString(R.string.hba1c_result), "", "", 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        microalbuminResult = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_creatinine_result), "", getResources().getString(R.string.comorbidities_vitals_bp_systolic_diastolic_range), 6, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        nextMicroalbuminTestDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_urinedr_nexttestdate), DateFormat.format("dd-MMM-yyyy", fourthDateCalendar).toString(), App.HORIZONTAL);
        //nextMicroalbuminTestDate.setVisibility(View.GONE);
        goneVisibility();

        // Used for reset fields...
        views = new View[]{formDate.getButton(), microalbuminTestID.getEditText(), formType.getRadioGroup(), microalbuminFollowupMonth.getSpinner(),
                microalbuminTestOrderDate.getButton(), microalbuminTestResultDate.getButton(), microalbuminResult.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, formType, linearLayout, testOrderMicroablbumin, microalbuminFollowupMonth, microalbuminTestOrderDate,
                        testResultMicroalbumin, microalbuminTestResultDate, microalbuminResult, nextMicroalbuminTestDate}};

        formDate.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        microalbuminTestOrderDate.getButton().setOnClickListener(this);
        microalbuminTestResultDate.getButton().setOnClickListener(this);

        microalbuminResult.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (microalbuminResult.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(microalbuminResult.getEditText().getText().toString());
                        if (num < 0 || num > 300) {
                            microalbuminResult.getEditText().setError(getString(R.string.comorbidities_vitals_bp_systolic_diastolic_limit));
                        } else {
                            //Correct value
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        microalbuminTestID.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (microalbuminTestID.getEditText().getText().length() > 0) {
                        testIdView.setVisibility(View.VISIBLE);
                        testIdView.setImageResource(R.drawable.ic_checked);
                    } else {
                        testIdView.setVisibility(View.INVISIBLE);
                    }
                    goneVisibility();
                    submitButton.setEnabled(false);
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        testIdView.setOnTouchListener(this);

        resetViews();

        /*microalbuminTestID.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (microalbuminTestID.getEditText().getText().length() > 0) {
                        if (microalbuminTestID.getEditText().getText().length() < 9) {
                            microalbuminTestID.getEditText().setError(getString(R.string.comorbidities_hhba1c_testid_format_error1));
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });*/
    }

    @Override
    public void updateDisplay() {

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        microalbuminTestOrderDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        microalbuminTestResultDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
        //fourthDateCalendar = thirdDateCalendar;
        fourthDateCalendar.set(Calendar.YEAR, secondDateCalendar.get(Calendar.YEAR));
        fourthDateCalendar.set(Calendar.MONTH, secondDateCalendar.get(Calendar.MONTH));
        fourthDateCalendar.set(Calendar.DAY_OF_MONTH, secondDateCalendar.get(Calendar.DAY_OF_MONTH));
        fourthDateCalendar.add(Calendar.MONTH, 2);
        fourthDateCalendar.add(Calendar.DAY_OF_MONTH, 20);
        nextMicroalbuminTestDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", fourthDateCalendar).toString());
    }

    @Override
    public boolean validate() {

        Boolean error = false;

        if (microalbuminResult.getVisibility() == View.VISIBLE && App.get(microalbuminResult).isEmpty()) {
            gotoLastPage();
            microalbuminResult.getEditText().setError(getString(R.string.empty_field));
            microalbuminResult.getEditText().requestFocus();
            error = true;
        }
        else if (microalbuminResult.getVisibility() == View.VISIBLE && !RegexUtil.isNumericForTwoDecimalPlaces(App.get(microalbuminResult), true)) {
            gotoLastPage();
            microalbuminResult.getEditText().setError(getString(R.string.comorbidities_upto_two_decimal_places_error));
            microalbuminResult.getEditText().requestFocus();
            error = true;
        }
        else if (microalbuminResult.getVisibility() == View.VISIBLE && !App.get(microalbuminResult).isEmpty() && Double.parseDouble(App.get(microalbuminResult)) > 300) {
            gotoLastPage();
            microalbuminResult.getEditText().setError(getString(R.string.comorbidities_creatinine_result_limit));
            microalbuminResult.getEditText().requestFocus();
            error = true;
        }

        if (App.get(microalbuminTestID).isEmpty()) {
            gotoFirstPage();
            microalbuminTestID.getEditText().setError(getString(R.string.empty_field));
            microalbuminTestID.getEditText().requestFocus();
            error = true;
        }
        /*else if (!App.get(microalbuminTestID).isEmpty() && App.get(microalbuminTestID).length() < 9) {
            gotoFirstPage();
            microalbuminTestID.getEditText().setError(getString(R.string.comorbidities_hhba1c_testid_format_error1));
            microalbuminTestID.getEditText().requestFocus();
            error = true;
        } else if (!RegexUtil.isCorrectTestID(App.get(microalbuminTestID))) {
            gotoLastPage();
            microalbuminTestID.getEditText().setError(getString(R.string.comorbidities_hhba1c_testid_format_dasherror));
            microalbuminTestID.getEditText().requestFocus();
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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
            }
            bundle.putBoolean("save", false);
        }

        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});
        observations.add(new String[]{"TEST ID", App.get(microalbuminTestID)});

        if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder))) {
            observations.add(new String[]{"FOLLOW-UP MONTH", App.get(microalbuminFollowupMonth)});
            observations.add(new String[]{"DATE TEST ORDERED", App.getSqlDateTime(secondDateCalendar)});
        } else if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testresult))) {
            observations.add(new String[]{"TEST RESULT DATE", App.getSqlDateTime(thirdDateCalendar)});
            observations.add(new String[]{"URINE MICROALBUMIN", App.get(microalbuminResult)});
            observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDateTime(fourthDateCalendar)});
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
                if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder))) {
                    result = serverService.saveEncounterAndObservation("Urine Microalbumin Test Order", FORM, formDateCalendar, observations.toArray(new String[][]{}));
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                } else if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testresult))) {
                    result = serverService.saveEncounterAndObservation("Urine Microalbumin Test Result", FORM, formDateCalendar, observations.toArray(new String[][]{}));
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                }

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

        //serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int encounterId) {

        OfflineForm fo = serverService.getOfflineFormById(encounterId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (fo.getFormName().contains("Order")) {
                formType.getRadioGroup().getButtons().get(0).setChecked(true);
                formType.getRadioGroup().getButtons().get(1).setEnabled(false);
                if (obs[0][0].equals("TEST ID")) {
                    microalbuminTestID.getEditText().setText(obs[0][1]);
                    microalbuminTestID.getEditText().setEnabled(false);
                    testIdView.setEnabled(false);
                    testIdView.setImageResource(R.drawable.ic_checked_green);
                    //checkTestId();
                } else if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                    microalbuminFollowupMonth.getSpinner().selectValue(obs[0][1]);
                    microalbuminFollowupMonth.setVisibility(View.VISIBLE);
                } else if (obs[0][0].equals("DATE TEST ORDERED")) {
                    String secondDate = obs[0][1];
                    secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                    microalbuminTestOrderDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
                    microalbuminTestOrderDate.setVisibility(View.VISIBLE);
                }
                submitButton.setEnabled(true);
            } else {
                formType.getRadioGroup().getButtons().get(1).setChecked(true);
                formType.getRadioGroup().getButtons().get(0).setEnabled(false);
                if (obs[0][0].equals("TEST ID")) {
                    microalbuminTestID.getEditText().setText(obs[0][1]);
                    checkTestId();
                    microalbuminTestID.getEditText().setEnabled(false);
                    testIdView.setEnabled(false);
                } else if (obs[0][0].equals("TEST RESULT DATE")) {
                    String secondDate = obs[0][1];
                    thirdDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                    microalbuminTestResultDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
                } else if (obs[0][0].equals("URINE MICROALBUMIN")) {
                    microalbuminResult.getEditText().setText(obs[0][1]);
                } else if (obs[0][0].equals("RETURN VISIT DATE")) {
                    String secondDate = obs[0][1];
                    fourthDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                    nextMicroalbuminTestDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", fourthDateCalendar).toString());
                }
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
        } else if (view == microalbuminTestOrderDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        } else if (view == microalbuminTestResultDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");
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

        microalbuminTestID.getEditText().setEnabled(true);
        testIdView.setEnabled(true);
        formType.getRadioGroup().getButtons().get(0).setEnabled(true);
        formType.getRadioGroup().getButtons().get(1).setEnabled(true);

        thirdDateCalendar = Calendar.getInstance();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        microalbuminTestOrderDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        microalbuminTestResultDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
        fourthDateCalendar.set(Calendar.YEAR, secondDateCalendar.get(Calendar.YEAR));
        fourthDateCalendar.set(Calendar.MONTH, secondDateCalendar.get(Calendar.MONTH));
        fourthDateCalendar.set(Calendar.DAY_OF_MONTH, secondDateCalendar.get(Calendar.DAY_OF_MONTH));
        fourthDateCalendar.add(Calendar.MONTH, 2);
        fourthDateCalendar.add(Calendar.DAY_OF_MONTH, 20);
        nextMicroalbuminTestDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", fourthDateCalendar).toString());

        submitButton.setEnabled(false);

        testIdView.setVisibility(View.GONE);
        microalbuminTestID.setVisibility(View.GONE);
        testIdView.setImageResource(R.drawable.ic_checked);

        goneVisibility();

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
        if (radioGroup == formType.getRadioGroup()) {
            //showTestOrderOrTestResult();
            formDate.setVisibility(View.VISIBLE);
            microalbuminTestID.setVisibility(View.VISIBLE);
            microalbuminTestID.getEditText().setText("");
            microalbuminTestID.getEditText().setError(null);
            goneVisibility();
            submitButton.setEnabled(false);
        }
    }

    void goneVisibility() {
        testOrderMicroablbumin.setVisibility(View.GONE);
        microalbuminFollowupMonth.setVisibility(View.GONE);
        microalbuminTestOrderDate.setVisibility(View.GONE);

        testResultMicroalbumin.setVisibility(View.GONE);
        microalbuminTestResultDate.setVisibility(View.GONE);
        microalbuminResult.setVisibility(View.GONE);
        nextMicroalbuminTestDate.setVisibility(View.GONE);
    }

    void showTestOrderOrTestResult() {
        if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder))) {
            testOrderMicroablbumin.setVisibility(View.VISIBLE);
            microalbuminFollowupMonth.setVisibility(View.VISIBLE);
            microalbuminTestOrderDate.setVisibility(View.VISIBLE);

            testResultMicroalbumin.setVisibility(View.GONE);
            microalbuminTestResultDate.setVisibility(View.GONE);
            microalbuminResult.setVisibility(View.GONE);
            nextMicroalbuminTestDate.setVisibility(View.GONE);

        } else {
            testOrderMicroablbumin.setVisibility(View.GONE);
            microalbuminFollowupMonth.setVisibility(View.GONE);
            microalbuminTestOrderDate.setVisibility(View.GONE);

            testResultMicroalbumin.setVisibility(View.VISIBLE);
            microalbuminTestResultDate.setVisibility(View.VISIBLE);
            microalbuminResult.setVisibility(View.VISIBLE);
            nextMicroalbuminTestDate.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageView view = (ImageView) v;
                //overlay is black with transparency of 0x77 (119)
                view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                view.invalidate();

                Boolean error = false;

                checkTestId();


                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                ImageView view = (ImageView) v;
                //clear the overlay
                view.getDrawable().clearColorFilter();
                view.invalidate();
                break;
            }
        }
        return true;
    }

    private void checkTestId() {
        AsyncTask<String, String, String> submissionFormTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.verifying_test_id));
                        loading.show();
                    }
                });

                String result = "";

                Object[][] testIds = serverService.getTestIdByPatientAndEncounterType(App.getPatientId(), "Comorbidities-Urine Microalbumin Test Order");

                Log.d("TEST_IDS_C", Arrays.deepToString(testIds));

                if (testIds == null || testIds.length < 1) {
                    if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder)))
                        return "SUCCESS";
                    else
                        return "";
                }

                if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder))) {
                    result = "SUCCESS";
                    for (int i = 0; i < testIds.length; i++) {
                        if (String.valueOf(testIds[i][0]).equals(App.get(microalbuminTestID))) {
                            return "";
                        }
                    }
                }

                if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testresult))) {
                    result = "";
                    for (int i = 0; i < testIds.length; i++) {
                        if (String.valueOf(testIds[i][0]).equals(App.get(microalbuminTestID))) {
                            return "SUCCESS";
                        }
                    }
                }

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

                    testIdView.setImageResource(R.drawable.ic_checked_green);
                    showTestOrderOrTestResult();
                    submitButton.setEnabled(true);

                } else {

                    if (App.get(formType).equals(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder))) {
                        microalbuminTestID.getEditText().setError("Test Id already used.");
                    } else {
                        microalbuminTestID.getEditText().setError("No order form found for the test id for patient");
                    }

                }

                try {
                    InputMethodManager imm = (InputMethodManager) mainContent.getContext().getSystemService(mainContent.getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        };
        submissionFormTask.execute("");

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

    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar;
            if (getArguments().getInt("type") == DATE_DIALOG_ID)
                calendar = formDateCalendar;
            else if (getArguments().getInt("type") == SECOND_DATE_DIALOG_ID)
                calendar = secondDateCalendar;
            else if (getArguments().getInt("type") == THIRD_DATE_DIALOG_ID)
                calendar = thirdDateCalendar;
            else
                return null;

            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setTag(getArguments().getInt("type"));
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            if (((int) view.getTag()) == DATE_DIALOG_ID)
                formDateCalendar.set(yy, mm, dd);
            else if (((int) view.getTag()) == SECOND_DATE_DIALOG_ID)
                secondDateCalendar.set(yy, mm, dd);
            else if (((int) view.getTag()) == THIRD_DATE_DIALOG_ID)
                thirdDateCalendar.set(yy, mm, dd);

            updateDisplay();
        }
    }

}




