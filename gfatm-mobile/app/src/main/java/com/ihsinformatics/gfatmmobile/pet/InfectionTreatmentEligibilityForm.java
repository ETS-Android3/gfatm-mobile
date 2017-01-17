package com.ihsinformatics.gfatmmobile.pet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class InfectionTreatmentEligibilityForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    TitledButton formDate;

    TitledRadioGroup pregnancyHistory;
    TitledRadioGroup pregnancyTestResult;
    TitledRadioGroup evaluationType;
    TitledRadioGroup tbDiagnosis;
    TitledRadioGroup infectionType;
    TitledRadioGroup tbReferral;
    TitledSpinner referralSite;
    TitledEditText othersSite;
    TitledRadioGroup tbRuledOut;
    TitledEditText petEligiable;
    TitledRadioGroup petConsent;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 1;
        FORM_NAME = Forms.PET_INFECTION_TREATMENT_ELIGIBILITY;
        FORM = Forms.pet_infectionTreatmenEligibility;

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
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }

        gotoFirstPage();

        return mainContent;
    }


    @Override
    public void initViews() {

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);

        MyLinearLayout linearLayout1 = new MyLinearLayout(context, getResources().getString(R.string.pet_contact_symptom_screen), App.VERTICAL);
        pregnancyHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_pregnancy_history), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        pregnancyTestResult = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_pregnancy_test_result), getResources().getStringArray(R.array.pet_pregnancy_test_results), getResources().getString(R.string.pet_positive), App.VERTICAL, App.VERTICAL, true);
        evaluationType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_evaluation_type), getResources().getStringArray(R.array.pet_evaluation_types), getResources().getString(R.string.pet_evidence_based_evaluation), App.VERTICAL, App.VERTICAL, true);
        tbDiagnosis = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_tb_diagnosed), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL, true);
        infectionType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_type_of_tb), getResources().getStringArray(R.array.pet_infection_types), "", App.HORIZONTAL, App.VERTICAL, true);
        tbReferral = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_patient_referred_fot_tb_treatment), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL, true);
        referralSite = new TitledSpinner(context, null, getResources().getString(R.string.pet_referral_site), getResources().getStringArray(R.array.pet_referral_sites), "", App.VERTICAL, true);
        othersSite = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.alphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        tbRuledOut = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_ruled_out), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL, true);
        petEligiable = new TitledEditText(context, null, getResources().getString(R.string.pet_eligible), getResources().getString(R.string.no), "", 20, RegexUtil.alphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        petConsent = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_consent), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL, true);

        linearLayout1.addView(pregnancyHistory);
        linearLayout1.addView(pregnancyTestResult);
        linearLayout1.addView(evaluationType);
        linearLayout1.addView(tbDiagnosis);
        linearLayout1.addView(infectionType);
        linearLayout1.addView(tbReferral);
        linearLayout1.addView(referralSite);
        linearLayout1.addView(othersSite);
        linearLayout1.addView(tbRuledOut);
        linearLayout1.addView(petEligiable);
        linearLayout1.addView(petConsent);

        views = new View[]{formDate.getButton(), pregnancyHistory.getRadioGroup(), pregnancyTestResult.getRadioGroup(), evaluationType.getRadioGroup(), tbDiagnosis.getRadioGroup(),
                infectionType.getRadioGroup(), tbReferral.getRadioGroup(), referralSite.getSpinner(), othersSite.getEditText(), tbRuledOut.getRadioGroup(), petEligiable.getEditText(), petConsent.getRadioGroup()};

        viewGroups = new View[][]{{formDate, linearLayout1}};

        View listenerViewer[] = new View[]{formDate, pregnancyHistory, referralSite, tbRuledOut, tbDiagnosis};
        for (View v : listenerViewer) {

            if (v instanceof TitledButton)
                ((TitledButton) v).getButton().setOnClickListener(this);
            else if (v instanceof TitledRadioGroup)
                ((TitledRadioGroup) v).getRadioGroup().setOnCheckedChangeListener(this);
            else if (v instanceof TitledSpinner)
                ((TitledSpinner) v).getSpinner().setOnItemSelectedListener(this);

        }

        pregnancyTestResult.setVisibility(View.GONE);
        othersSite.setVisibility(View.GONE);

    }

    @Override
    public void updateDisplay() {
        if (!formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString())) {

            if (formDateCalendar.after(new Date())) {

                Date date = App.stringToDate(formDate.getButton().getText().toString(), "dd-MMM-yyyy");
                formDateCalendar = App.getCalendar(date);

            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }
    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        pregnancyTestResult.setVisibility(View.GONE);
        othersSite.setVisibility(View.GONE);

    }

    @Override
    public boolean validate() {

        return true;
    }

    @Override
    public boolean submit() {
        resetViews();
        return false;
    }

    @Override
    public boolean save() {
        return false;
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
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
        MySpinner spinner = (MySpinner) parent;
        if (spinner == referralSite.getSpinner()) {
            if (App.get(referralSite).equals(getResources().getString(R.string.pet_other)))
                othersSite.setVisibility(View.VISIBLE);
            else
                othersSite.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == pregnancyHistory.getRadioGroup()) {
            if (App.get(pregnancyHistory).equals(getResources().getString(R.string.yes)))
                pregnancyTestResult.setVisibility(View.VISIBLE);
            else
                pregnancyTestResult.setVisibility(View.GONE);
        }
        if (group == tbRuledOut.getRadioGroup()) {
            if (App.get(tbRuledOut).equals(getResources().getString(R.string.yes)))
                petEligiable.getEditText().setText(getResources().getString(R.string.yes));
            else
                petEligiable.getEditText().setText(getResources().getString(R.string.no));
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
