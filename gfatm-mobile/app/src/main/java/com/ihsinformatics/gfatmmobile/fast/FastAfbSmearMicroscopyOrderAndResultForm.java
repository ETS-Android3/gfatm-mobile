package com.ihsinformatics.gfatmmobile.fast;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
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
 * Created by Haris on 1/26/2017.
 */

public class FastAfbSmearMicroscopyOrderAndResultForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    public static final int THIRD_DATE_DIALOG_ID = 3;
    public static final int FORTH_DATE_DIALOG_ID = 4;
    protected Calendar thirdDateCalendar;
    protected DialogFragment thirdDateFragment;
    protected Calendar forthDateCalendar;
    protected DialogFragment forthDateFragment;
    Context context;
    // Views...
    TitledButton formDate;
    TitledEditText testId;
    TitledRadioGroup formType;
    MyTextView afbSmearOrder;
    MyTextView afbSmearResult;
    TitledButton dateOfSubmission;
    TitledButton testDate;
    TitledRadioGroup testContextStatus;
    TitledSpinner monthOfTreatment;
    TitledRadioGroup specimenType;
    TitledSpinner specimenSource;
    TitledEditText specimenSourceOther;
    TitledButton dateTestResult;
    TitledSpinner smearResult;
    TitledEditText noAfb;


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
        FORM_NAME = Forms.FAST_AFB_SMEAR_MICROSCOPY_ORDER_AND_RESULT_FORM;
        FORM = Forms.fastAfbSmearMicroscopyOrderAndResultForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter());
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
        thirdDateCalendar = Calendar.getInstance();
        thirdDateFragment = new SelectDateFragment();

        forthDateCalendar = Calendar.getInstance();
        forthDateFragment = new SelectDateFragment();
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        testId = new TitledEditText(context, null, getResources().getString(R.string.fast_test_id), "", "", 50, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, false);
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_select_form_type), getResources().getStringArray(R.array.fast_order_and_result_list), "", App.VERTICAL, App.VERTICAL);
        afbSmearOrder = new MyTextView(context, getResources().getString(R.string.fast_afb_smear_order));
        afbSmearOrder.setTypeface(null, Typeface.BOLD);
        dateOfSubmission = new TitledButton(context, null, getResources().getString(R.string.fast_date_of_submission), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        testDate = new TitledButton(context, null, getResources().getString(R.string.fast_test_date), DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString(), App.HORIZONTAL);
        testContextStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_at_what_point_test_being_done), getResources().getStringArray(R.array.fast_test_being_done_list), getResources().getString(R.string.fast_baseline_new), App.VERTICAL, App.VERTICAL);
        monthOfTreatment = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_month_of_treatment), getResources().getStringArray(R.array.fast_number_list), getResources().getString(R.string.fast_one), App.HORIZONTAL);
        specimenType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_specimen_type), getResources().getStringArray(R.array.fast_specimen_type_list), getResources().getString(R.string.fast_sputum), App.VERTICAL, App.VERTICAL);
        specimenSource = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_where_did_the_specimen_come_from), getResources().getStringArray(R.array.fast_specimen_come_from_list), getResources().getString(R.string.fast_lymph), App.VERTICAL);
        specimenSourceOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        afbSmearResult = new MyTextView(context, getResources().getString(R.string.fast_afb_smear_result));
        afbSmearResult.setTypeface(null, Typeface.BOLD);
        dateTestResult = new TitledButton(context, null, getResources().getString(R.string.fast_date_of_result_recieved), DateFormat.format("dd-MMM-yyyy", forthDateCalendar).toString(), App.HORIZONTAL);
        smearResult = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_smear_result), getResources().getStringArray(R.array.fast_smear_result_list), getResources().getString(R.string.fast_negative), App.VERTICAL);
        noAfb = new TitledEditText(context, null, getResources().getString(R.string.fast_number_of_afb_seen_in_one_field), "", "", 10, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), testId.getEditText(), formType.getRadioGroup(), dateOfSubmission.getButton(),
                testDate.getButton(), testContextStatus.getRadioGroup(), monthOfTreatment.getSpinner(), specimenType.getRadioGroup(),
                specimenSource.getSpinner(), specimenSourceOther.getEditText(), dateTestResult.getButton(), smearResult.getSpinner(), noAfb.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, testId, formType, afbSmearOrder, dateOfSubmission, testDate, testContextStatus, monthOfTreatment, specimenType,
                        specimenSource, specimenSourceOther, afbSmearResult, dateTestResult, smearResult, noAfb}};

        formDate.getButton().setOnClickListener(this);
        dateOfSubmission.getButton().setOnClickListener(this);
        testDate.getButton().setOnClickListener(this);
        dateOfSubmission.getButton().setOnClickListener(this);
        dateTestResult.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        specimenSource.getSpinner().setOnItemSelectedListener(this);
        testContextStatus.getRadioGroup().setOnCheckedChangeListener(this);
        specimenType.getRadioGroup().setOnCheckedChangeListener(this);
        smearResult.getSpinner().setOnItemSelectedListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        dateOfSubmission.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        testDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
        dateTestResult.getButton().setText(DateFormat.format("dd-MMM-yyyy", forthDateCalendar).toString());
    }

    @Override
    public boolean validate() {
        Boolean error = false;

        if (testId.getVisibility() == View.VISIBLE && App.get(testId).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            testId.getEditText().setError(getString(R.string.empty_field));
            testId.getEditText().requestFocus();
            error = true;
        }

        if (specimenSourceOther.getVisibility() == View.VISIBLE && App.get(specimenSourceOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            specimenSourceOther.getEditText().setError(getString(R.string.empty_field));
            specimenSourceOther.getEditText().requestFocus();
            error = true;
        }

        if (noAfb.getVisibility() == View.VISIBLE && App.get(noAfb).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            noAfb.getEditText().setError(getString(R.string.empty_field));
            noAfb.getEditText().requestFocus();
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

      /*  formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        formValues.put(lastName.getTag(), App.get(lastName));
        formValues.put(husbandName.getTag(), App.get(husbandName));
        formValues.put(gender.getTag(), App.get(gender));

        serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);*/

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
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
        }

        if (view == dateOfSubmission.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        }

        if (view == testDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", THIRD_DATE_DIALOG_ID);
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");
        }

        if (view == dateTestResult.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", FORTH_DATE_DIALOG_ID);
            forthDateFragment.setArguments(args);
            forthDateFragment.show(getFragmentManager(), "DatePicker");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        dateOfSubmission.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        testDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalendar).toString());
        dateTestResult.getButton().setText(DateFormat.format("dd-MMM-yyyy", forthDateCalendar).toString());
        afbSmearOrder.setVisibility(View.GONE);
        dateOfSubmission.setVisibility(View.GONE);
        testDate.setVisibility(View.GONE);
        testContextStatus.setVisibility(View.GONE);
        monthOfTreatment.setVisibility(View.GONE);
        specimenType.setVisibility(View.GONE);
        specimenSource.setVisibility(View.GONE);
        specimenSourceOther.setVisibility(View.GONE);
        afbSmearResult.setVisibility(View.GONE);
        dateTestResult.setVisibility(View.GONE);
        smearResult.setVisibility(View.GONE);
        noAfb.setVisibility(View.GONE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;

        if (spinner == specimenSource.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                specimenSourceOther.setVisibility(View.VISIBLE);
            } else {
                specimenSourceOther.setVisibility(View.GONE);
            }
        }

        if (spinner == smearResult.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_negative))) {
                noAfb.setVisibility(View.VISIBLE);
            } else {
                noAfb.setVisibility(View.GONE);
            }
        }

      /*0  if (spinner == screenXrayDiagnosis.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_others))) {
                screenXrayDiagnosisOther.setVisibility(View.VISIBLE);
            } else {
                screenXrayDiagnosisOther.setVisibility(View.GONE);
            }
        }*/
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == formType.getRadioGroup()) {
            if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))) {
                afbSmearOrder.setVisibility(View.VISIBLE);
                dateOfSubmission.setVisibility(View.VISIBLE);
                testDate.setVisibility(View.VISIBLE);
                testContextStatus.setVisibility(View.VISIBLE);
                if (testContextStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_followup_test))) {
                    monthOfTreatment.setVisibility(View.VISIBLE);
                }
                specimenType.setVisibility(View.VISIBLE);
                if (specimenType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_extra_pulmonary))) {
                    specimenSource.setVisibility(View.VISIBLE);

                    if (specimenSource.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                        specimenSourceOther.setVisibility(View.VISIBLE);
                    }
                }

                afbSmearResult.setVisibility(View.GONE);
                dateTestResult.setVisibility(View.GONE);
                smearResult.setVisibility(View.GONE);
                noAfb.setVisibility(View.GONE);

            } else {
                afbSmearOrder.setVisibility(View.GONE);
                dateOfSubmission.setVisibility(View.GONE);
                testDate.setVisibility(View.GONE);
                testContextStatus.setVisibility(View.GONE);
                monthOfTreatment.setVisibility(View.GONE);
                specimenType.setVisibility(View.GONE);
                specimenSource.setVisibility(View.GONE);
                specimenSourceOther.setVisibility(View.GONE);

                afbSmearResult.setVisibility(View.VISIBLE);
                dateTestResult.setVisibility(View.VISIBLE);
                smearResult.setVisibility(View.VISIBLE);

                if (smearResult.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_negative))) {
                    noAfb.setVisibility(View.VISIBLE);
                }
            }
        } else if (radioGroup == testContextStatus.getRadioGroup()) {
            if (testContextStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_followup_test))) {
                monthOfTreatment.setVisibility(View.VISIBLE);
            } else {
                monthOfTreatment.setVisibility(View.GONE);
            }
        } else if (radioGroup == specimenType.getRadioGroup()) {
            if (specimenType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_extra_pulmonary))) {
                specimenSource.setVisibility(View.VISIBLE);
                if (specimenSource.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                    specimenSourceOther.setVisibility(View.VISIBLE);
                } else {
                    specimenSourceOther.setVisibility(View.GONE);
                }
            } else {
                specimenSource.setVisibility(View.GONE);
                specimenSourceOther.setVisibility(View.GONE);
            }
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

    @SuppressLint("ValidFragment")
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
            else if (getArguments().getInt("type") == FORTH_DATE_DIALOG_ID)
                calendar = forthDateCalendar;
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
            else if (((int) view.getTag()) == FORTH_DATE_DIALOG_ID)
                forthDateCalendar.set(yy, mm, dd);

            updateDisplay();
        }
    }


}
