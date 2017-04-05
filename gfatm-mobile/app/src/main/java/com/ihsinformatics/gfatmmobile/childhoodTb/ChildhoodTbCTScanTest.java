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
import android.widget.TextView;

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

public class ChildhoodTbCTScanTest extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    public static final int THIRD_DIALOG_ID = 3;
    protected Calendar thirdDateCalender;
    protected DialogFragment thirdDateFragment;

    Context context;
    TitledButton formDate;
    TitledRadioGroup formType;
    TitledEditText testId;
    TitledButton testOrderDate;
    TitledSpinner ctScanSite;
    TitledEditText monthTreatment;

    TitledButton testResultDate;
    TitledRadioGroup ctChestTbSuggestive;
    TitledRadioGroup ctChestInterpretation;
    TitledRadioGroup ctAbdomenTbSuggestive;
    TitledRadioGroup ctAbdomenInterpretation;
    TitledRadioGroup ctBrainTbSuggestive;
    TitledRadioGroup ctBrainInterpretation;
    TitledRadioGroup ctBoneSTbSuggestive;
    TitledRadioGroup ctSpineTbSuggestive;
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
        FORM_NAME = Forms.CHILDHOODTB_CT_SCAN_TEST;
        FORM = Forms.childhoodTb_ct_scan_order_and_result;

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

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        testId = new TitledEditText(context, null, getResources().getString(R.string.ctb_test_id), "", "", 50, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_type_of_form), getResources().getStringArray(R.array.ctb_type_of_form_list), null, App.HORIZONTAL, App.VERTICAL, true);
        testOrderDate = new TitledButton(context, null, getResources().getString(R.string.ctb_test_order_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        testOrderDate.setTag("testOrderDate");
        ctScanSite = new TitledSpinner(context, null, getResources().getString(R.string.ctb_ct_scan_site), getResources().getStringArray(R.array.ctb_ct_scan_site_list), null, App.VERTICAL);
        monthTreatment = new TitledEditText(context, null, getResources().getString(R.string.ctb_month_treatment), "1", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);

        testResultDate = new TitledButton(context, null, getResources().getString(R.string.ctb_test_result_date), DateFormat.format("dd-MMM-yyyy", thirdDateCalender).toString(), App.HORIZONTAL);
        testResultDate.setTag("testResultDate");
        ctChestTbSuggestive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_chest_suggestive_tb), getResources().getStringArray(R.array.ctb_ct_chest_suggestive_tb_list), getResources().getString(R.string.ctb_adenopathy), App.VERTICAL, App.VERTICAL, true);
        ctChestInterpretation = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_chest_interpretation), getResources().getStringArray(R.array.ctb_suggestive_not_suggestive), null, App.HORIZONTAL, App.VERTICAL, true);
        ctAbdomenTbSuggestive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_abdomen_suggestive_tb), getResources().getStringArray(R.array.ctb_ct_abdomen_suggestive_tb_list), getResources().getString(R.string.ctb_ascites), App.HORIZONTAL, App.VERTICAL, true);
        ctAbdomenInterpretation = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_abdomen_interpretation), getResources().getStringArray(R.array.ctb_suggestive_not_suggestive), null, App.HORIZONTAL, App.VERTICAL, true);
        ctBrainTbSuggestive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_brain_suggestive_tb), getResources().getStringArray(R.array.ctb_meningeal_tuberculomas), null, App.HORIZONTAL, App.VERTICAL, true);
        ctBrainInterpretation = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_brain_interpretation), getResources().getStringArray(R.array.ctb_suggestive_not_suggestive), null, App.HORIZONTAL, App.VERTICAL, true);
        ctBoneSTbSuggestive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_bone_suggestive_tb), getResources().getStringArray(R.array.ctb_suggestive_not_suggestive), null, App.HORIZONTAL, App.VERTICAL, true);
        ctSpineTbSuggestive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ct_spine_suggestive_tb), getResources().getStringArray(R.array.ctb_suggestive_not_suggestive), null, App.HORIZONTAL, App.VERTICAL, true);

        views = new View[]{formDate.getButton(), formType.getRadioGroup(), testOrderDate.getButton(), ctScanSite.getSpinner(),
                testResultDate.getButton(), ctChestTbSuggestive.getRadioGroup(), ctChestInterpretation.getRadioGroup(),
                ctAbdomenTbSuggestive.getRadioGroup(), ctAbdomenInterpretation.getRadioGroup(), ctBrainTbSuggestive.getRadioGroup(),
                ctBrainInterpretation.getRadioGroup(), ctBoneSTbSuggestive.getRadioGroup(), ctSpineTbSuggestive.getRadioGroup(),testId.getEditText(),
                monthTreatment.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, testId, formType, testOrderDate, ctScanSite, monthTreatment, testResultDate,
                        ctChestTbSuggestive,
                        ctChestInterpretation,
                        ctAbdomenTbSuggestive,
                        ctAbdomenInterpretation,
                        ctBrainTbSuggestive,
                        ctBrainInterpretation,
                        ctBoneSTbSuggestive,
                        ctSpineTbSuggestive,}};

        formDate.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        testOrderDate.getButton().setOnClickListener(this);
        ctScanSite.getSpinner().setOnItemSelectedListener(this);
        testResultDate.getButton().setOnClickListener(this);
        ctChestTbSuggestive.getRadioGroup().setOnCheckedChangeListener(this);
        ctChestInterpretation.getRadioGroup().setOnCheckedChangeListener(this);
        ctAbdomenTbSuggestive.getRadioGroup().setOnCheckedChangeListener(this);
        ctAbdomenInterpretation.getRadioGroup().setOnCheckedChangeListener(this);
        ctBrainTbSuggestive.getRadioGroup().setOnCheckedChangeListener(this);
        ctBrainInterpretation.getRadioGroup().setOnCheckedChangeListener(this);
        ctBoneSTbSuggestive.getRadioGroup().setOnCheckedChangeListener(this);
        ctSpineTbSuggestive.getRadioGroup().setOnCheckedChangeListener(this);
        resetViews();
    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();


        Date date = new Date();

        if (!(formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            }else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }

        if (!testOrderDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString())) {

            //
            // +Date date = App.stringToDate(sampleSubmitDate.getButton().getText().toString(), "dd-MMM-yyyy");

            if (secondDateCalendar.after(date)) {

                secondDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                testOrderDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        }
        if (!testResultDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", thirdDateCalender).toString())) {
            if (thirdDateCalender.after(date)) {

                thirdDateCalender = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                testResultDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalender).toString());
        }
      }

    @Override
    public boolean validate() {
        boolean error = false;
        if (App.get(testId).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            testId.getEditText().setError(getString(R.string.empty_field));
            testId.getEditText().requestFocus();
            error = true;
        } else {
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
        } else {
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
        } else {
            monthTreatment.getEditText().setError(null);
        }
        if (!App.get(monthTreatment).isEmpty()) {
            if (Integer.parseInt(App.get(monthTreatment)) < 1 || Integer.parseInt(App.get(monthTreatment)) > 24) {
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
        if (view == testOrderDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        }
        if (view == testResultDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", THIRD_DIALOG_ID);
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
        MySpinner spinner = (MySpinner) parent;

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
        testOrderDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        testResultDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalender).toString());
        testOrderDate.setVisibility(View.GONE);
        monthTreatment.setVisibility(View.GONE);
        ctScanSite.setVisibility(View.GONE);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == formType.getRadioGroup()) {
            if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_order))) {
                testOrderDate.setVisibility(View.VISIBLE);
                ctScanSite.setVisibility(View.VISIBLE);
                monthTreatment.setVisibility(View.VISIBLE);
            } else {
                testOrderDate.setVisibility(View.GONE);
                ctScanSite.setVisibility(View.GONE);
                monthTreatment.setVisibility(View.GONE);
            }
            if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_result))) {
                testResultDate.setVisibility(View.VISIBLE);
                ctChestTbSuggestive.setVisibility(View.VISIBLE);
                ctChestInterpretation.setVisibility(View.VISIBLE);
                ctAbdomenTbSuggestive.setVisibility(View.VISIBLE);
                ctAbdomenInterpretation.setVisibility(View.VISIBLE);
                ctBrainTbSuggestive.setVisibility(View.VISIBLE);
                ctBrainInterpretation.setVisibility(View.VISIBLE);
                ctBoneSTbSuggestive.setVisibility(View.VISIBLE);
                ctSpineTbSuggestive.setVisibility(View.VISIBLE);

            } else {
                testResultDate.setVisibility(View.GONE);
                ctChestTbSuggestive.setVisibility(View.GONE);
                ctChestInterpretation.setVisibility(View.GONE);
                ctAbdomenTbSuggestive.setVisibility(View.GONE);
                ctAbdomenInterpretation.setVisibility(View.GONE);
                ctBrainTbSuggestive.setVisibility(View.GONE);
                ctBrainInterpretation.setVisibility(View.GONE);
                ctBoneSTbSuggestive.setVisibility(View.GONE);
                ctSpineTbSuggestive.setVisibility(View.GONE);
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
            else if (((int) view.getTag()) == THIRD_DIALOG_ID)
                thirdDateCalender.set(yy, mm, dd);

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
