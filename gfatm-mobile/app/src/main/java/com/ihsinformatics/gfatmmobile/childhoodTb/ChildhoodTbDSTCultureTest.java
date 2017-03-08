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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

public class ChildhoodTbDSTCultureTest extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    public static final int THIRD_DIALOG_ID = 3;
    protected Calendar thirdDateCalender;
    protected DialogFragment thirdDateFragment;
    Context context;
    TitledButton formDate;
    TitledRadioGroup formType;
    TitledEditText testId;
    TitledButton dateSubmission;
    TitledRadioGroup pointTestBeingDone;
    TitledEditText monthTreatment;
    TitledRadioGroup specimenType;
    TitledSpinner specimenComeFrom;
    TitledEditText otherSpecimentComeFrom;
    TitledButton resultRecievedDate;
    TitledSpinner typeOfMediaDst;
    TitledRadioGroup isoniazidPoint2;
    TitledRadioGroup isoniazid1;
    TitledRadioGroup rifampicin;
    TitledRadioGroup ethambuthol;
    TitledRadioGroup streptomycin;
    TitledRadioGroup pyrazinamide;
    TitledRadioGroup ofloxacin;
    TitledRadioGroup levofloxacin;
    TitledRadioGroup moxifloxacinPoint5;
    TitledRadioGroup moxifloxacin2;
    TitledRadioGroup amikacin;
    TitledRadioGroup kanamycin;
    TitledRadioGroup capreomycin;
    TitledRadioGroup ethionamide;
    TitledRadioGroup cycloserine;
    TitledRadioGroup pas;
    TitledRadioGroup bedaquiline;
    TitledRadioGroup delamanid;
    TitledRadioGroup linezolid;
    TitledRadioGroup clofazamine;
    TitledEditText otherDrug;
    TitledRadioGroup otherDrugResult;
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
        FORM_NAME = Forms.CHILDHOODTB_DST_CULTURE_TEST;
        FORM = Forms.childhoodTb_dst_order_and_result;

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
        dateSubmission = new TitledButton(context, null, getResources().getString(R.string.ctb_date_submission), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        dateSubmission.setTag("dateSubmission");
        pointTestBeingDone = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_point_test_being_done), getResources().getStringArray(R.array.ctb_point_test_being_done_list), getResources().getString(R.string.ctb_baseline), App.VERTICAL, App.VERTICAL);
        monthTreatment = new TitledEditText(context, null, getResources().getString(R.string.ctb_month_treatment), "1", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        specimenType = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_specimen_type), getResources().getStringArray(R.array.ctb_specimen_type_list), null, App.HORIZONTAL, App.VERTICAL);
        specimenComeFrom = new TitledSpinner(context, null, getResources().getString(R.string.ctb_speciment_route), getResources().getStringArray(R.array.ctb_speciment_route_list), null, App.VERTICAL);
        otherSpecimentComeFrom = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        resultRecievedDate = new TitledButton(context, null, getResources().getString(R.string.ctb_date_result_recieved), DateFormat.format("dd-MMM-yyyy", thirdDateCalender).toString(), App.HORIZONTAL);
        resultRecievedDate.setTag("resultRecievedDate");
        typeOfMediaDst = new TitledSpinner(context, null, getResources().getString(R.string.ctb_type_of_media_dst), getResources().getStringArray(R.array.ctb_type_of_media_dst_list), null, App.VERTICAL);
        isoniazidPoint2 = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_isoniazid_point_2_result), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        isoniazid1 = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_isoniazid_1_result), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        rifampicin = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_rifampicin), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        ethambuthol = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ethambuhol), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        streptomycin = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_streptomycin), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        pyrazinamide = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_pyrazinamide), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        ofloxacin = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ofloxacin), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        levofloxacin = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_levofloxacin), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        moxifloxacinPoint5 = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_moxifloxacin_point_5), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        moxifloxacin2 = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_moxifloxacin_2), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        amikacin = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_amikacin), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        kanamycin = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_kanamycin), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        capreomycin = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_capreomycin), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        ethionamide = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ethionamide), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        cycloserine = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_cycloserine), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        pas = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_pas), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        bedaquiline = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_bedaquiline), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        delamanid = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_delamanid), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        linezolid = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_linezolid), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        clofazamine = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_clofazamine), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);
        otherDrug = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_drug_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        otherDrugResult = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_other_drug_result), getResources().getStringArray(R.array.ctb_susceptible_resistant_indeterminate), null, App.HORIZONTAL, App.VERTICAL);

        views = new View[]{formDate.getButton(), formType.getRadioGroup(), dateSubmission.getButton(), pointTestBeingDone.getRadioGroup(),
                specimenType.getRadioGroup(), specimenComeFrom.getSpinner(), resultRecievedDate.getButton(), typeOfMediaDst.getSpinner(),
                isoniazidPoint2.getRadioGroup(), isoniazid1.getRadioGroup(), rifampicin.getRadioGroup(),
                ethambuthol.getRadioGroup(), streptomycin.getRadioGroup(), pyrazinamide.getRadioGroup(),
                ofloxacin.getRadioGroup(), levofloxacin.getRadioGroup(), moxifloxacinPoint5.getRadioGroup(),
                moxifloxacin2.getRadioGroup(), amikacin.getRadioGroup(), kanamycin.getRadioGroup(),
                capreomycin.getRadioGroup(), ethionamide.getRadioGroup(), cycloserine.getRadioGroup(),
                pas.getRadioGroup(), bedaquiline.getRadioGroup(), delamanid.getRadioGroup(), linezolid.getRadioGroup(),
                clofazamine.getRadioGroup(), otherDrugResult.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, testId, formType, dateSubmission, pointTestBeingDone, monthTreatment, specimenType, specimenComeFrom, otherSpecimentComeFrom, resultRecievedDate, typeOfMediaDst, isoniazidPoint2, isoniazid1, rifampicin,
                        ethambuthol, streptomycin, pyrazinamide,
                        ofloxacin, levofloxacin, moxifloxacinPoint5,
                        moxifloxacin2, amikacin, kanamycin,
                        capreomycin, ethionamide, cycloserine,
                        pas, bedaquiline, delamanid, linezolid,
                        clofazamine, otherDrug, otherDrugResult}};

        formDate.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        dateSubmission.getButton().setOnClickListener(this);
        pointTestBeingDone.getRadioGroup().setOnCheckedChangeListener(this);
        specimenType.getRadioGroup().setOnCheckedChangeListener(this);
        specimenComeFrom.getSpinner().setOnItemSelectedListener(this);
        resultRecievedDate.getButton().setOnClickListener(this);
        typeOfMediaDst.getSpinner().setOnItemSelectedListener(this);
        isoniazidPoint2.getRadioGroup().setOnCheckedChangeListener(this);
        isoniazid1.getRadioGroup().setOnCheckedChangeListener(this);
        rifampicin.getRadioGroup().setOnCheckedChangeListener(this);
        ethambuthol.getRadioGroup().setOnCheckedChangeListener(this);
        streptomycin.getRadioGroup().setOnCheckedChangeListener(this);
        pyrazinamide.getRadioGroup().setOnCheckedChangeListener(this);
        ofloxacin.getRadioGroup().setOnCheckedChangeListener(this);
        levofloxacin.getRadioGroup().setOnCheckedChangeListener(this);
        moxifloxacinPoint5.getRadioGroup().setOnCheckedChangeListener(this);
        moxifloxacin2.getRadioGroup().setOnCheckedChangeListener(this);
        amikacin.getRadioGroup().setOnCheckedChangeListener(this);
        kanamycin.getRadioGroup().setOnCheckedChangeListener(this);
        capreomycin.getRadioGroup().setOnCheckedChangeListener(this);
        ethionamide.getRadioGroup().setOnCheckedChangeListener(this);
        cycloserine.getRadioGroup().setOnCheckedChangeListener(this);
        pas.getRadioGroup().setOnCheckedChangeListener(this);
        bedaquiline.getRadioGroup().setOnCheckedChangeListener(this);
        delamanid.getRadioGroup().setOnCheckedChangeListener(this);
        linezolid.getRadioGroup().setOnCheckedChangeListener(this);
        clofazamine.getRadioGroup().setOnCheckedChangeListener(this);
        otherDrugResult.getRadioGroup().setOnCheckedChangeListener(this);
        otherDrug.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    otherDrugResult.setVisibility(View.VISIBLE);
                }
                else{
                    otherDrugResult.setVisibility(View.GONE);
                }
            }
        });

        resetViews();

    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();


        Date date = new Date();

        if (formDateCalendar.after(date)) {

            formDateCalendar = App.getCalendar(date);

            snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
            snackbar.show();

        } else
            formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

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
        if (!resultRecievedDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", thirdDateCalender).toString())) {
            if (thirdDateCalender.after(date)) {

                thirdDateCalender = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                resultRecievedDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalender).toString());
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
        if (specimenType.getVisibility() == View.VISIBLE && App.get(specimenType).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            specimenType.getRadioGroup().getButtons().get(1).setError(getString(R.string.empty_field));
            specimenType.getRadioGroup().requestFocus();
            error = true;
        } else {
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
        if (view == dateSubmission.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        }
        if (view == resultRecievedDate.getButton()) {
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
        if (spinner == specimenComeFrom.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                otherSpecimentComeFrom.setVisibility(View.VISIBLE);
            } else {
                otherSpecimentComeFrom.setVisibility(View.GONE);
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
        resultRecievedDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", thirdDateCalender).toString());
        testId.getEditText().setText("");
        monthTreatment.getEditText().setText("");
        dateSubmission.setVisibility(View.GONE);
        pointTestBeingDone.setVisibility(View.GONE);
        monthTreatment.setVisibility(View.GONE);
        specimenType.setVisibility(View.GONE);
        specimenComeFrom.setVisibility(View.GONE);
        otherSpecimentComeFrom.setVisibility(View.GONE);
        otherSpecimentComeFrom.getEditText().setText("");
        resultRecievedDate.setVisibility(View.GONE);
        typeOfMediaDst.setVisibility(View.GONE);
        isoniazidPoint2.setVisibility(View.GONE);
        isoniazid1.setVisibility(View.GONE);
        rifampicin.setVisibility(View.GONE);
        ethambuthol.setVisibility(View.GONE);
        streptomycin.setVisibility(View.GONE);
        pyrazinamide.setVisibility(View.GONE);
        ofloxacin.setVisibility(View.GONE);
        levofloxacin.setVisibility(View.GONE);
        moxifloxacinPoint5.setVisibility(View.GONE);
        moxifloxacin2.setVisibility(View.GONE);
        amikacin.setVisibility(View.GONE);
        kanamycin.setVisibility(View.GONE);
        capreomycin.setVisibility(View.GONE);
        ethionamide.setVisibility(View.GONE);
        cycloserine.setVisibility(View.GONE);
        pas.setVisibility(View.GONE);
        bedaquiline.setVisibility(View.GONE);
        delamanid.setVisibility(View.GONE);
        linezolid.setVisibility(View.GONE);
        clofazamine.setVisibility(View.GONE);
        otherDrug.setVisibility(View.GONE);
        otherDrugResult.setVisibility(View.GONE);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == formType.getRadioGroup()) {
            if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_order))) {
                dateSubmission.setVisibility(View.VISIBLE);
                pointTestBeingDone.setVisibility(View.VISIBLE);
                specimenType.setVisibility(View.VISIBLE);
            } else {
                dateSubmission.setVisibility(View.GONE);
                pointTestBeingDone.setVisibility(View.GONE);
                monthTreatment.setVisibility(View.GONE);
                specimenType.setVisibility(View.GONE);
                specimenComeFrom.setVisibility(View.GONE);
                otherSpecimentComeFrom.setVisibility(View.GONE);
            }
            if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_result))) {
                resultRecievedDate.setVisibility(View.VISIBLE);
                typeOfMediaDst.setVisibility(View.VISIBLE);
                isoniazidPoint2.setVisibility(View.VISIBLE);
                isoniazid1.setVisibility(View.VISIBLE);
                rifampicin.setVisibility(View.VISIBLE);
                ethambuthol.setVisibility(View.VISIBLE);
                streptomycin.setVisibility(View.VISIBLE);
                pyrazinamide.setVisibility(View.VISIBLE);
                ofloxacin.setVisibility(View.VISIBLE);
                levofloxacin.setVisibility(View.VISIBLE);
                moxifloxacinPoint5.setVisibility(View.VISIBLE);
                moxifloxacin2.setVisibility(View.VISIBLE);
                amikacin.setVisibility(View.VISIBLE);
                kanamycin.setVisibility(View.VISIBLE);
                capreomycin.setVisibility(View.VISIBLE);
                ethionamide.setVisibility(View.VISIBLE);
                cycloserine.setVisibility(View.VISIBLE);
                pas.setVisibility(View.VISIBLE);
                bedaquiline.setVisibility(View.VISIBLE);
                delamanid.setVisibility(View.VISIBLE);
                linezolid.setVisibility(View.VISIBLE);
                clofazamine.setVisibility(View.VISIBLE);
                otherDrug.setVisibility(View.VISIBLE);

            } else {
                resultRecievedDate.setVisibility(View.GONE);
                resultRecievedDate.setVisibility(View.GONE);
                typeOfMediaDst.setVisibility(View.GONE);
                isoniazidPoint2.setVisibility(View.GONE);
                isoniazid1.setVisibility(View.GONE);
                rifampicin.setVisibility(View.GONE);
                ethambuthol.setVisibility(View.GONE);
                streptomycin.setVisibility(View.GONE);
                pyrazinamide.setVisibility(View.GONE);
                ofloxacin.setVisibility(View.GONE);
                levofloxacin.setVisibility(View.GONE);
                moxifloxacinPoint5.setVisibility(View.GONE);
                moxifloxacin2.setVisibility(View.GONE);
                amikacin.setVisibility(View.GONE);
                kanamycin.setVisibility(View.GONE);
                capreomycin.setVisibility(View.GONE);
                ethionamide.setVisibility(View.GONE);
                cycloserine.setVisibility(View.GONE);
                pas.setVisibility(View.GONE);
                bedaquiline.setVisibility(View.GONE);
                delamanid.setVisibility(View.GONE);
                linezolid.setVisibility(View.GONE);
                clofazamine.setVisibility(View.GONE);
                otherDrug.setVisibility(View.GONE);
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
