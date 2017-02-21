package com.ihsinformatics.gfatmmobile.childhoodTb;

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
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbCXRScreeningTest extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledRadioGroup typeOfXRay;
    TitledEditText monthTreatment;
    TitledButton testDate;
    TitledEditText testId;
    TitledEditText chestXRayScore;
    TitledSpinner radiologicalDiagnosis;
    TitledEditText otherRadiologicalDiagnosis;
    TitledSpinner diseaseExtent;
    TitledEditText radiologistRemarks;

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
        FORM_NAME = Forms.CHILDHOODTB_GXP_SPECIMEN_COLLECTION_FORM;
        FORM = Forms.childhoodTb_gxp_specimen_form;

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

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        typeOfXRay = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_type_of_xray),getResources().getStringArray(R.array.ctb_type_of_xray_list),getResources().getString(R.string.ctb_chest_xray_other),App.HORIZONTAL,App.VERTICAL);
        monthTreatment = new TitledEditText(context,null,getResources().getString(R.string.ctb_month_treatment),"1","",2,RegexUtil.NUMERIC_FILTER,InputType.TYPE_CLASS_NUMBER,App.HORIZONTAL,false);
        testDate = new TitledButton(context, null, getResources().getString(R.string.ctb_test_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        testDate.setTag("testDate");
        testId = new TitledEditText(context,null,getResources().getString(R.string.ctb_test_id),"","",8,RegexUtil.NUMERIC_FILTER,InputType.TYPE_CLASS_NUMBER,App.HORIZONTAL,false);
        chestXRayScore = new TitledEditText(context,null,getResources().getString(R.string.ctb_chest_xray_cad4tb_score),"","",8,RegexUtil.NUMERIC_FILTER,InputType.TYPE_CLASS_NUMBER,App.HORIZONTAL,false);
        radiologicalDiagnosis = new TitledSpinner(context,null,getResources().getString(R.string.ctb_radiological_diagnosis),getResources().getStringArray(R.array.ctb_radiological_diagnosis_list),null,App.VERTICAL);
        otherRadiologicalDiagnosis = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_specify),"","",50,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,false);
        diseaseExtent = new TitledSpinner(context,null,getResources().getString(R.string.ctb_extent_disease),getResources().getStringArray(R.array.ctb_disease_extent_list),null,App.VERTICAL);
        radiologistRemarks = new TitledEditText(context,null,getResources().getString(R.string.ctb_radiologist_remark),"","",500,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,false);

        views = new View[]{formDate.getButton(),typeOfXRay.getRadioGroup(),testDate.getButton(),radiologicalDiagnosis.getSpinner(),diseaseExtent.getSpinner()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate,typeOfXRay,monthTreatment,testDate,testId,chestXRayScore,radiologicalDiagnosis,otherRadiologicalDiagnosis,diseaseExtent
                ,radiologistRemarks}};

        formDate.getButton().setOnClickListener(this);
        testDate.getButton().setOnClickListener(this);
        radiologicalDiagnosis.getSpinner().setOnItemSelectedListener(this);
        typeOfXRay.getRadioGroup().setOnCheckedChangeListener(this);
        diseaseExtent.getSpinner().setOnItemSelectedListener(this);

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

        if (!testDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString())) {

            //
            // +Date date = App.stringToDate(sampleSubmitDate.getButton().getText().toString(), "dd-MMM-yyyy");

            if (secondDateCalendar.after(date)) {

                secondDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                testDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
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
        if (view == testDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == radiologicalDiagnosis.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                otherRadiologicalDiagnosis.setVisibility(View.VISIBLE);
            } else {
                otherRadiologicalDiagnosis.setVisibility(View.GONE);
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
        testDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        chestXRayScore.setVisibility(View.GONE);
        otherRadiologicalDiagnosis.setVisibility(View.GONE);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == typeOfXRay.getRadioGroup()) {
            if (typeOfXRay.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_chest_xray_cad4tb))) {
                chestXRayScore.setVisibility(View.VISIBLE);

            } else {
                chestXRayScore.setVisibility(View.GONE);
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
}
