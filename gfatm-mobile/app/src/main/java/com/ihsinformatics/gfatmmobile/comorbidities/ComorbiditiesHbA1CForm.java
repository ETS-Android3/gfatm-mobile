package com.ihsinformatics.gfatmmobile.comorbidities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Fawad Jawaid on 21-Dec-16.
 */

public class ComorbiditiesHbA1CForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    public static final int THIRD_DATE_DIALOG_ID = 3;
    // Extra Views for date ...
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;
    Context context;
    // Views...
    TitledButton formDate;
    TitledRadioGroup formType;
    //Views for Test Order HbA1C
    MyTextView testOrderHba1C;
    TitledRadioGroup hba1cTestType;
    TitledSpinner hba1cFollowupMonth;
    TitledButton hba1cTestOrderDate;
    TitledEditText hba1cTestID;
    //Views for Test Result HbA1C
    MyTextView testResultHba1c;
    TitledButton hba1cTestResultDate;
    TitledEditText hba1cResult;
    TitledRadioGroup hba1cDiabetic;

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
        FORM_NAME = Forms.COMORBIDITIES_HbA1C_FORM;
        FORM = Forms.comorbidities_hbA1cForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesHbA1CForm.MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(PAGE_COUNT - 1);
        formName.setText(FORM_NAME);

        thirdDateCalendar = Calendar.getInstance();
        thirdDateFragment = new SelectDateFragment();

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
        testOrderHba1C = new MyTextView(context, getResources().getString(R.string.comorbidities_hba1c_test_order));
        testOrderHba1C.setTypeface(null, Typeface.BOLD);
        hba1cTestType = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hba1c_testtype), getResources().getStringArray(R.array.comorbidities_HbA1C_test_type), "", App.HORIZONTAL, App.VERTICAL);
        hba1cFollowupMonth = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_mth_txcomorbidities_hba1c), getResources().getStringArray(R.array.comorbidities_followup_month), "1", App.HORIZONTAL);
        showFollowupField();
        hba1cTestOrderDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_hba1cdate_test_order), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        hba1cTestID = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_hhba1c_testid), "", "", 9, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        //second page views...
        testResultHba1c = new MyTextView(context, getResources().getString(R.string.comorbidities_hba1c_test_result));
        testResultHba1c.setTypeface(null, Typeface.BOLD);
        hba1cTestResultDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_hba1c_resultdate), DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);
        //microalbuminResult = new TitledEditText(context, null, getResources().getString(R.string.hba1c_result), "", "", 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        hba1cResult = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_hba1c_result), "", getResources().getString(R.string.comorbidities_hba1c_result_range), 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        hba1cDiabetic = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_hba1c_diabetic), getResources().getStringArray(R.array.comorbidities_yes_no), "", App.VERTICAL, App.VERTICAL);
        showHba1cDiabetic();
        autopopulateHba1cDiabetic();
        goneVisibility();

        // Used for reset fields...
        views = new View[]{formDate.getButton(), hba1cTestID.getEditText(), formType.getRadioGroup(), hba1cTestType.getRadioGroup(), hba1cFollowupMonth.getSpinner(),
                hba1cTestOrderDate.getButton(), hba1cTestResultDate.getButton(), hba1cResult.getEditText(), hba1cDiabetic.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, hba1cTestID, formType, testOrderHba1C, hba1cTestType, hba1cFollowupMonth, hba1cTestOrderDate,
                        testResultHba1c, hba1cTestResultDate, hba1cResult, hba1cDiabetic}};

        formDate.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        hba1cTestType.getRadioGroup().setOnCheckedChangeListener(this);
        hba1cTestOrderDate.getButton().setOnClickListener(this);
        hba1cTestResultDate.getButton().setOnClickListener(this);

        hba1cResult.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (hba1cResult.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(hba1cResult.getEditText().getText().toString());
                        if (num < 0 || num > 20) {
                            hba1cResult.getEditText().setError(getString(R.string.comorbidities_hba1c_result_limit));
                        } else {
                            autopopulateHba1cDiabetic();
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        hba1cTestID.getEditText().addTextChangedListener(new TextWatcher() {

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
                    if (hba1cTestID.getEditText().getText().length() > 0) {
                        if (hba1cTestID.getEditText().getText().length() < 9) {
                            hba1cTestID.getEditText().setError(getString(R.string.comorbidities_hhba1c_testid_format_error1));
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
        hba1cTestOrderDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        hba1cTestResultDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
    }

    @Override
    public boolean validate() {

        Boolean error = false;

        if (hba1cResult.getVisibility() == View.VISIBLE && App.get(hba1cResult).isEmpty()) {
            gotoLastPage();
            hba1cResult.getEditText().setError(getString(R.string.empty_field));
            hba1cResult.getEditText().requestFocus();
            error = true;
        }
        else if (hba1cResult.getVisibility() == View.VISIBLE && !RegexUtil.isNumeric(App.get(hba1cResult), true)) {
            gotoLastPage();
            hba1cResult.getEditText().setError(getString(R.string.comorbidities_hba1c_not_valid_result_value));
            hba1cResult.getEditText().requestFocus();
            error = true;
        }
        else if (hba1cResult.getVisibility() == View.VISIBLE && !App.get(hba1cResult).isEmpty() && Double.parseDouble(App.get(hba1cResult)) > 20) {
            gotoLastPage();
            hba1cResult.getEditText().setError(getString(R.string.comorbidities_hba1c_result_limit));
            hba1cResult.getEditText().requestFocus();
            error = true;
        }

        if (App.get(hba1cTestID).isEmpty()) {
            gotoFirstPage();
            hba1cTestID.getEditText().setError(getString(R.string.empty_field));
            hba1cTestID.getEditText().requestFocus();
            error = true;
        }
        else if (!App.get(hba1cTestID).isEmpty() && App.get(hba1cTestID).length() < 9) {
            gotoFirstPage();
            hba1cTestID.getEditText().setError(getString(R.string.comorbidities_hhba1c_testid_format_error1));
            hba1cTestID.getEditText().requestFocus();
            error = true;
        } /*else if (!RegexUtil.isCorrectTestID(App.get(hba1cTestID))) {
            gotoLastPage();
            hba1cTestID.getEditText().setError(getString(R.string.comorbidities_hhba1c_testid_format_dasherror));
            hba1cTestID.getEditText().requestFocus();
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

        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        /*observations.add (new String[] {"LONGITUDE (DEGREES)", String.valueOf(longitude)});
        observations.add (new String[] {"LATITUDE (DEGREES)", String.valueOf(latitude)});*/
        observations.add(new String[]{"TEST CONTEXT STATUS", App.get(hba1cTestType).equals(getResources().getString(R.string.comorbidities_HbA1C_test_type_baseline)) ? "BASELINE" :
                (App.get(hba1cTestType).equals(getResources().getString(R.string.comorbidities_HbA1C_test_type_baseline)) ? "BASELINE REPEAT" : "REGULAR FOLLOW UP")});

        if(hba1cFollowupMonth.getVisibility() ==  View.VISIBLE) {
            observations.add(new String[]{"FOLLOW-UP MONTH", App.get(hba1cFollowupMonth)});
        }

        observations.add(new String[]{"DATE TEST ORDERED", App.getSqlDateTime(secondDateCalendar)});
        observations.add(new String[]{"TEST ID", App.get(hba1cTestID)});

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
        } else if (view == hba1cTestOrderDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        } else if (view == hba1cTestResultDate.getButton()) {
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

        thirdDateCalendar = Calendar.getInstance();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        hba1cTestOrderDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        hba1cTestResultDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());

        goneVisibility();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == hba1cTestType.getRadioGroup()) {
            showFollowupField();
            showHba1cDiabetic();
        }

        if (radioGroup == formType.getRadioGroup()) {
            showTestOrderOrTestResult();
        }
    }

    void showFollowupField() {
        if (hba1cTestType.getVisibility() == View.VISIBLE && hba1cTestType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_HbA1C_test_type_followup))) {
            hba1cFollowupMonth.setVisibility(View.VISIBLE);
        } else {
            hba1cFollowupMonth.setVisibility(View.GONE);
        }
    }

    void showHba1cDiabetic() {
        if (hba1cTestType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_HbA1C_test_type_followup))) {

            //Later we have to get creatinineTestType from Database
            hba1cDiabetic.setVisibility(View.GONE);
        } else {
            hba1cDiabetic.setVisibility(View.VISIBLE);
        }
    }

    void autopopulateHba1cDiabetic() {
        try {
            if (Double.parseDouble(hba1cResult.getEditText().getText().toString()) >= 6.5) {
                hba1cDiabetic.getRadioGroup().check((hba1cDiabetic.getRadioGroup().getChildAt(0)).getId());
            } else {
                hba1cDiabetic.getRadioGroup().check((hba1cDiabetic.getRadioGroup().getChildAt(1)).getId());
            }
        } catch (NumberFormatException nfe) {
            //Exception: User might be entering " " (empty) value
        }
    }

    void goneVisibility() {
        testOrderHba1C.setVisibility(View.GONE);
        hba1cTestType.setVisibility(View.GONE);
        hba1cFollowupMonth.setVisibility(View.GONE);
        hba1cTestOrderDate.setVisibility(View.GONE);

        //second page views...
        testResultHba1c.setVisibility(View.GONE);
        hba1cTestResultDate.setVisibility(View.GONE);
        hba1cResult.setVisibility(View.GONE);
        hba1cDiabetic.setVisibility(View.GONE);

    }

    void showTestOrderOrTestResult() {
        if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder))) {
            testOrderHba1C.setVisibility(View.VISIBLE);
            hba1cTestType.setVisibility(View.VISIBLE);
            hba1cFollowupMonth.setVisibility(View.VISIBLE);
            hba1cTestOrderDate.setVisibility(View.VISIBLE);
            showFollowupField();
            testResultHba1c.setVisibility(View.GONE);
            hba1cTestResultDate.setVisibility(View.GONE);
            hba1cResult.setVisibility(View.GONE);
            hba1cDiabetic.setVisibility(View.GONE);
        } else {
            testOrderHba1C.setVisibility(View.GONE);
            hba1cTestType.setVisibility(View.GONE);
            hba1cFollowupMonth.setVisibility(View.GONE);
            hba1cTestOrderDate.setVisibility(View.GONE);
            testResultHba1c.setVisibility(View.VISIBLE);
            hba1cTestResultDate.setVisibility(View.VISIBLE);
            hba1cResult.setVisibility(View.VISIBLE);
            hba1cDiabetic.setVisibility(View.VISIBLE);
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

