package com.ihsinformatics.gfatmmobile.childhoodTb;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbAFBSmearTest extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    public static final int THIRD_DIALOG_ID = 3;
    public static final int FOURTH_DIALOG_ID = 4;
    protected Calendar thirdDateCalender;
    protected DialogFragment thirdDateFragment;
    protected Calendar fourthDateCalender;
    protected DialogFragment fourthDateFragment;
    Context context;
    TitledButton formDate;
    TitledRadioGroup formType;
    TitledEditText testId;
    TitledButton dateSubmission;
    TitledButton testDate;
    TitledRadioGroup pointTestBeingDone;
    TitledEditText monthTreatment;
    TitledRadioGroup specimenType;
    TitledSpinner specimenComeFrom;
    TitledEditText otherSpecimentComeFrom;
    TitledButton resultRecievedDate;
    TitledSpinner smearResult;
    TitledEditText afbSeenOneField;
    Snackbar snackbar;
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
        FORM_NAME = Forms.CHILDHOODTB_AFB_SMEAR_ORDER_AND_RESULT;
        FORM = Forms.childhoodTb_afb_smear_order_and_result;

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
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(context);
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        } else {
            for (int i = 0; i < PAGE_COUNT; i++) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(context);
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
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


        thirdDateCalender = Calendar.getInstance();
        thirdDateFragment = new SelectDateFragment();
        fourthDateCalender = Calendar.getInstance();
        fourthDateFragment = new SelectDateFragment();

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        testId = new TitledEditText(context,null,getResources().getString(R.string.ctb_test_id),"","",50,RegexUtil.NUMERIC_FILTER,InputType.TYPE_CLASS_NUMBER,App.HORIZONTAL,false);
        formType = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_type_of_form),getResources().getStringArray(R.array.ctb_type_of_form_list),null,App.HORIZONTAL,App.VERTICAL,true);
        dateSubmission = new TitledButton(context, null, getResources().getString(R.string.ctb_date_submission), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        dateSubmission.setTag("dateSubmission");
        testDate = new TitledButton(context, null, getResources().getString(R.string.ctb_test_date), DateFormat.format("dd-MMM-yyyy", thirdDateCalender).toString(), App.HORIZONTAL);
        testDate.setTag("testDate");
        pointTestBeingDone = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_point_test_being_done),getResources().getStringArray(R.array.ctb_point_test_being_done_list),getResources().getString(R.string.ctb_baseline),App.VERTICAL,App.VERTICAL);
        monthTreatment= new TitledEditText(context,null,getResources().getString(R.string.ctb_month_treatment),"1","",2,RegexUtil.NUMERIC_FILTER,InputType.TYPE_CLASS_NUMBER,App.HORIZONTAL,false);
        specimenType = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_specimen_type),getResources().getStringArray(R.array.ctb_specimen_type_list),null,App.HORIZONTAL,App.VERTICAL);
        specimenComeFrom = new TitledSpinner(context,null,getResources().getString(R.string.ctb_speciment_route),getResources().getStringArray(R.array.ctb_speciment_route_list),null,App.VERTICAL);
        otherSpecimentComeFrom = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_specify),"","",50,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,false);

        resultRecievedDate = new TitledButton(context, null, getResources().getString(R.string.ctb_date_result_recieved), DateFormat.format("dd-MMM-yyyy", fourthDateCalender).toString(), App.HORIZONTAL);
        resultRecievedDate.setTag("resultRecievedDate");
        smearResult = new TitledSpinner(context,null,getResources().getString(R.string.ctb_smear_result),getResources().getStringArray(R.array.fast_smear_result_list),null,App.VERTICAL);
        afbSeenOneField = new TitledEditText(context,null,getResources().getString(R.string.ctb_afb_seen_in_one_field),"","",4,RegexUtil.NUMERIC_FILTER,InputType.TYPE_CLASS_NUMBER,App.HORIZONTAL,false);


        views = new View[]{formDate.getButton(),formType.getRadioGroup(),dateSubmission.getButton(),testDate.getButton(),pointTestBeingDone.getRadioGroup(),
                specimenType.getRadioGroup(),specimenComeFrom.getSpinner(),resultRecievedDate.getButton(),smearResult.getSpinner()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate,testId,formType,dateSubmission,testDate,pointTestBeingDone,monthTreatment,specimenType,specimenComeFrom,otherSpecimentComeFrom,resultRecievedDate,smearResult
                ,afbSeenOneField}};

        formDate.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        dateSubmission.getButton().setOnClickListener(this);
        testDate.getButton().setOnClickListener(this);
        pointTestBeingDone.getRadioGroup().setOnCheckedChangeListener(this);
        specimenType.getRadioGroup().setOnCheckedChangeListener(this);
        specimenComeFrom.getSpinner().setOnItemSelectedListener(this);
        smearResult.getSpinner().setOnItemSelectedListener(this);
        resultRecievedDate.getButton().setOnClickListener(this);

        resetViews();

    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();


        Date date = new Date();
        if (!(formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();


            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            }
        if (!dateSubmission.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString())) {

            //
            // +Date date = App.stringToDate(sampleSubmitDate.getButton().getText().toString(), "dd-MMM-yyyy");

            if (secondDateCalendar.after(date)) {

                secondDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                dateSubmission.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        }
        if (!testDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", thirdDateCalender).toString())) {
            if (thirdDateCalender.after(date)) {

                thirdDateCalender = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                testDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalender).toString());
        }
        if (!resultRecievedDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", fourthDateCalender).toString())) {
            if (fourthDateCalender.after(date)) {

                fourthDateCalender = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                resultRecievedDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", fourthDateCalender).toString());
        }
    }

    @Override
    public boolean validate() {
        boolean error=false;
        if (App.get(testId).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            testId.getEditText().setError(getString(R.string.empty_field));
            testId.getEditText().requestFocus();
            error = true;
        }
        else{
            testId.getEditText().setError(null);
        }
        if (App.get(formType).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            formType.getRadioGroup().getButtons().get(1).setError(getString(R.string.empty_field));
            formType.getRadioGroup().requestFocus();
            error = true;
        }
        else{
            formType.getRadioGroup().getButtons().get(1).setError(null);
        }
        if (monthTreatment.getVisibility() == View.VISIBLE && App.get(monthTreatment).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            monthTreatment.getEditText().setError(getString(R.string.empty_field));
            monthTreatment.getEditText().requestFocus();
            error = true;
        }
        else{
            monthTreatment.getEditText().setError(null);
        }
        if (specimenType.getVisibility() == View.VISIBLE && App.get(specimenType).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            specimenType.getRadioGroup().getButtons().get(1).setError(getString(R.string.empty_field));
            specimenType.getRadioGroup().requestFocus();
            error = true;
        }
        else{
            specimenType.getRadioGroup().getButtons().get(1).setError(null);
        }
        if (otherSpecimentComeFrom.getVisibility() == View.VISIBLE && App.get(otherSpecimentComeFrom).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherSpecimentComeFrom.getEditText().setError(getString(R.string.empty_field));
            otherSpecimentComeFrom.getEditText().requestFocus();
            error = true;
        }
        if (!App.get(monthTreatment).isEmpty()) {
            if (Integer.parseInt(App.get(monthTreatment))<1 || Integer.parseInt(App.get(monthTreatment)) > 24) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                monthTreatment.getEditText().setError(getString(R.string.ctb_range_1_to_24));
                monthTreatment.getEditText().requestFocus();
                error = true;
            } else {
                monthTreatment.getEditText().setError(null);
            }
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

        return true;
    }

    @Override
    public boolean save() {

        HashMap<String, String> formValues = new HashMap<String, String>();

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));

        serverService.saveFormLocally(FORM_NAME, FORM, "12345-5", formValues);

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
        }
        if (view == dateSubmission.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        }
        if (view == testDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", THIRD_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            thirdDateFragment.setArguments(args);
            thirdDateFragment.show(getFragmentManager(), "DatePicker");
        }
        if (view == resultRecievedDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", FOURTH_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            fourthDateFragment.setArguments(args);
            fourthDateFragment.show(getFragmentManager(), "DatePicker");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == specimenComeFrom.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                otherSpecimentComeFrom.setVisibility(View.VISIBLE);
            } else {
                otherSpecimentComeFrom.setVisibility(View.GONE);
            }
        }
        if (spinner == smearResult.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_scanty_3_24))) {
                String value = parent.getItemAtPosition(position).toString();
                afbSeenOneField.setVisibility(View.VISIBLE);
            } else {
                afbSeenOneField.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        dateSubmission.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        testDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalender).toString());
        resultRecievedDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", fourthDateCalender).toString());
        testId.getEditText().setText("");
        monthTreatment.getEditText().setText("");
        dateSubmission.setVisibility(View.GONE);
        testDate.setVisibility(View.GONE);
        pointTestBeingDone.setVisibility(View.GONE);
        monthTreatment.setVisibility(View.GONE);
        specimenType.setVisibility(View.GONE);
        specimenComeFrom.setVisibility(View.GONE);
        otherSpecimentComeFrom.setVisibility(View.GONE);
        otherSpecimentComeFrom.getEditText().setText("");
        resultRecievedDate.setVisibility(View.GONE);
        smearResult.setVisibility(View.GONE);
        afbSeenOneField.setVisibility(View.GONE);
        afbSeenOneField.getEditText().setText("");
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == formType.getRadioGroup()) {
            if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_order))) {
                dateSubmission.setVisibility(View.VISIBLE);
                testDate.setVisibility(View.VISIBLE);
                pointTestBeingDone.setVisibility(View.VISIBLE);
                specimenType.setVisibility(View.VISIBLE);
            } else {
                dateSubmission.setVisibility(View.GONE);
                testDate.setVisibility(View.GONE);
                pointTestBeingDone.setVisibility(View.GONE);
                monthTreatment.setVisibility(View.GONE);
                specimenType.setVisibility(View.GONE);
                specimenComeFrom.setVisibility(View.GONE);
                otherSpecimentComeFrom.setVisibility(View.GONE);
            }
            if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_result))) {
                resultRecievedDate.setVisibility(View.VISIBLE);
                smearResult.setVisibility(View.VISIBLE);

            } else {
                resultRecievedDate.setVisibility(View.GONE);
                smearResult.setVisibility(View.GONE);
                afbSeenOneField.setVisibility(View.GONE);
            }
        }
        if (group == specimenType.getRadioGroup()) {
            if (specimenType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_extra_pulmonary))) {
                specimenComeFrom.setVisibility(View.VISIBLE);

            } else {
                specimenComeFrom.getSpinner().setSelection(0);
                specimenComeFrom.setVisibility(View.GONE);
            }
        }
        if (group == pointTestBeingDone.getRadioGroup()) {
            if (pointTestBeingDone.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_followup))) {
                monthTreatment.setVisibility(View.VISIBLE);

            } else {
                monthTreatment.setVisibility(View.GONE);
            }
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

            else if (getArguments().getInt("type") == THIRD_DIALOG_ID)
                calendar = thirdDateCalender;

            else if (getArguments().getInt("type") ==FOURTH_DIALOG_ID)
                calendar = fourthDateCalender;
            else
                return null;

            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setTag(getArguments().getInt("type"));
            if (!getArguments().getBoolean("allowFutureDate", false))
                dialog.getDatePicker().setMaxDate(new Date().getTime());
            if (!getArguments().getBoolean("allowPastDate", false))
                dialog.getDatePicker().setMinDate(new Date().getTime());
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            if (((int) view.getTag()) == DATE_DIALOG_ID)
                formDateCalendar.set(yy, mm, dd);
            else if (((int) view.getTag()) == SECOND_DATE_DIALOG_ID)
                secondDateCalendar.set(yy, mm, dd);
            else if(((int) view.getTag()) == THIRD_DIALOG_ID)
                thirdDateCalender.set(yy, mm, dd);
            else if(((int) view.getTag()) == FOURTH_DIALOG_ID)
                fourthDateCalender.set(yy, mm, dd);
            updateDisplay();

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
}
