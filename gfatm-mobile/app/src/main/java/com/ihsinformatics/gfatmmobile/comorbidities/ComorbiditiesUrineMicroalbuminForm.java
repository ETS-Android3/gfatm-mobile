package com.ihsinformatics.gfatmmobile.comorbidities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
 * Created by Fawad Jawaid on 19-Jan-17.
 */

public class ComorbiditiesUrineMicroalbuminForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

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
        microalbuminTestID = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_hhba1c_testid), "", getResources().getString(R.string.comorbidities_hhba1c_testid_format_hint1), 9, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);

        //second page views...
        testResultMicroalbumin = new MyTextView(context, getResources().getString(R.string.comorbidities_creatinine_test_result));
        testResultMicroalbumin.setTypeface(null, Typeface.BOLD);
        microalbuminTestResultDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_hba1c_resultdate), DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);
        //microalbuminResult = new TitledEditText(context, null, getResources().getString(R.string.hba1c_result), "", "", 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        microalbuminResult = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_creatinine_result), "", getResources().getString(R.string.comorbidities_vitals_bp_systolic_diastolic_range), 6, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        nextMicroalbuminTestDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_urinedr_nexttestdate), DateFormat.format("dd-MMM-yyyy", fourthDateCalendar).toString(), App.HORIZONTAL);
        nextMicroalbuminTestDate.setVisibility(View.GONE);
        goneVisibility();

        // Used for reset fields...
        views = new View[]{formDate.getButton(), microalbuminTestID.getEditText(), formType.getRadioGroup(), microalbuminFollowupMonth.getSpinner(),
                microalbuminTestOrderDate.getButton(), microalbuminTestResultDate.getButton(), microalbuminResult.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, microalbuminTestID, formType, testOrderMicroablbumin, microalbuminFollowupMonth, microalbuminTestOrderDate,
                        testResultMicroalbumin, microalbuminTestResultDate, microalbuminResult}};

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
                        if (microalbuminTestID.getEditText().getText().length() < 9) {
                            microalbuminTestID.getEditText().setError(getString(R.string.comorbidities_hhba1c_testid_format_error1));
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
        microalbuminTestOrderDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        microalbuminTestResultDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
        fourthDateCalendar = thirdDateCalendar;
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
        else if (!App.get(microalbuminTestID).isEmpty() && App.get(microalbuminTestID).length() < 9) {
            gotoFirstPage();
            microalbuminTestID.getEditText().setError(getString(R.string.comorbidities_hhba1c_testid_format_error1));
            microalbuminTestID.getEditText().requestFocus();
            error = true;
        } else if (!RegexUtil.isCorrectTestID(App.get(microalbuminTestID))) {
            gotoLastPage();
            microalbuminTestID.getEditText().setError(getString(R.string.comorbidities_hhba1c_testid_format_dasherror));
            microalbuminTestID.getEditText().requestFocus();
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

        if (validate()) {

            resetViews();
        }

        //resetViews();
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
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
        } else if (view == microalbuminTestOrderDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        } else if (view == microalbuminTestResultDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
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
        microalbuminTestOrderDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        microalbuminTestResultDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
        nextMicroalbuminTestDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", fourthDateCalendar).toString());

        goneVisibility();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == formType.getRadioGroup()) {
            showTestOrderOrTestResult();
        }
    }

    void goneVisibility() {
        testOrderMicroablbumin.setVisibility(View.GONE);
        microalbuminFollowupMonth.setVisibility(View.GONE);
        microalbuminTestOrderDate.setVisibility(View.GONE);

        testResultMicroalbumin.setVisibility(View.GONE);
        microalbuminTestResultDate.setVisibility(View.GONE);
        microalbuminResult.setVisibility(View.GONE);
    }

    void showTestOrderOrTestResult() {
        if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_testorder_testresult_form_type_testorder))) {
            testOrderMicroablbumin.setVisibility(View.VISIBLE);
            microalbuminFollowupMonth.setVisibility(View.VISIBLE);
            microalbuminTestOrderDate.setVisibility(View.VISIBLE);

            testResultMicroalbumin.setVisibility(View.GONE);
            microalbuminTestResultDate.setVisibility(View.GONE);
            microalbuminResult.setVisibility(View.GONE);

        } else {
            testOrderMicroablbumin.setVisibility(View.GONE);
            microalbuminFollowupMonth.setVisibility(View.GONE);
            microalbuminTestOrderDate.setVisibility(View.GONE);

            testResultMicroalbumin.setVisibility(View.VISIBLE);
            microalbuminTestResultDate.setVisibility(View.VISIBLE);
            microalbuminResult.setVisibility(View.VISIBLE);
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




