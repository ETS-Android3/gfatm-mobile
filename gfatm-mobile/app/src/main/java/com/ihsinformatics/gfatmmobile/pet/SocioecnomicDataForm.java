package com.ihsinformatics.gfatmmobile.pet;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class SocioecnomicDataForm extends AbstractFormActivity {

    Context context;
    TitledButton formDate;
    TitledSpinner ethinicity;
    TitledEditText otherEthinicity;
    TitledSpinner contactEducationLevel;
    TitledSpinner maritalStatus;
    TitledSpinner emloyementStatus;
    TitledSpinner occupation;
    TitledEditText contactIncome;
    TitledEditText contactIncomeGroup;
    TitledSpinner householdHead;
    TitledEditText otherHouseholdHead;
    TitledSpinner householdHeadEducationLevel;
    TitledSpinner motherTongue;
    TitledEditText otherMotherTongue;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 1;
        FORM_NAME = Forms.PET_SOCIO_ECNOMICS_DATA;
        FORM = Forms.pet_socioEcnomicData;

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
        ethinicity = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_ethnicity), getResources().getStringArray(R.array.pet_ethnicities), "", App.VERTICAL, true);
        otherEthinicity = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.alphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        contactEducationLevel = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_contact_education_level), getResources().getStringArray(R.array.pet_contact_education_levels), getResources().getString(R.string.pet_intermediate), App.VERTICAL, true);
        maritalStatus = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_martial_status), getResources().getStringArray(R.array.pet_martial_statuses), getResources().getString(R.string.pet_married), App.VERTICAL, true);
        emloyementStatus = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_employement_status), getResources().getStringArray(R.array.pet_employement_statuses), getResources().getString(R.string.pet_employed), App.VERTICAL, true);
        occupation = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_occupation), getResources().getStringArray(R.array.pet_occupations), getString(R.string.pet_artist), App.VERTICAL, true);
        contactIncome = new TitledEditText(context, null, getResources().getString(R.string.pet_contact_income), "0", "", 20, RegexUtil.numericFilter, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        contactIncomeGroup = new TitledEditText(context, null, getResources().getString(R.string.pet_contact_income_group), getResources().getString(R.string.pet_none), "", 10, RegexUtil.alphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        householdHead = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_household_head), getResources().getStringArray(R.array.pet_household_heads), getString(R.string.pet_mother), App.VERTICAL, true);
        otherHouseholdHead = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.alphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        householdHeadEducationLevel = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_household_education), getResources().getStringArray(R.array.pet_contact_education_levels), getString(R.string.pet_intermediate), App.VERTICAL);
        motherTongue = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_mother_tongue), getResources().getStringArray(R.array.pet_mother_tongues), getString(R.string.urdu), App.VERTICAL);
        otherMotherTongue = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.alphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        views = new View[]{formDate.getButton(), ethinicity.getSpinner(), otherEthinicity.getEditText(), contactEducationLevel.getSpinner(), maritalStatus.getSpinner(), emloyementStatus.getSpinner(), occupation.getSpinner(), contactIncome.getEditText(), contactIncomeGroup.getEditText(),
                householdHead.getSpinner(), otherHouseholdHead.getEditText(), householdHeadEducationLevel.getSpinner(), motherTongue.getSpinner(), otherMotherTongue.getEditText()};

        viewGroups = new View[][]{{formDate, ethinicity, otherEthinicity, contactEducationLevel, maritalStatus, emloyementStatus, occupation, contactIncome, contactIncomeGroup, householdHead, otherHouseholdHead, householdHeadEducationLevel, motherTongue, otherMotherTongue}};

        contactIncome.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (App.get(contactIncome).equals(""))
                    contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_none));
                else {
                    Float income = Float.parseFloat(App.get(contactIncome));
                    if (income == 0)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_none));
                    else if (income >= 1 && income <= 7000)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_lower_class));
                    else if (income >= 7001 && income <= 35000)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_lower_middle_class));
                    else if (income >= 35001 && income <= 87000)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_middle_class));
                    else if (income >= 87001 && income <= 173000)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_upper_middle_class));
                    else
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_upper_class));
                }


            }
        });
        contactIncomeGroup.getEditText().setKeyListener(null);

        formDate.getButton().setOnClickListener(this);
        ethinicity.getSpinner().setOnItemSelectedListener(this);
        householdHead.getSpinner().setOnItemSelectedListener(this);
        motherTongue.getSpinner().setOnItemSelectedListener(this);

        otherEthinicity.setVisibility(View.GONE);
        otherHouseholdHead.setVisibility(View.GONE);
        otherMotherTongue.setVisibility(View.GONE);

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        otherEthinicity.setVisibility(View.GONE);
        otherHouseholdHead.setVisibility(View.GONE);
        otherMotherTongue.setVisibility(View.GONE);

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
    public boolean validate() {

        Boolean error = false;

        if (App.get(otherMotherTongue).isEmpty() && otherMotherTongue.getVisibility() == View.VISIBLE) {
            otherMotherTongue.getEditText().setError(getString(R.string.empty_field));
            otherMotherTongue.getEditText().requestFocus();
            error = true;
        }

        if (App.get(otherHouseholdHead).isEmpty() && otherHouseholdHead.getVisibility() == View.VISIBLE) {
            otherHouseholdHead.getEditText().setError(getString(R.string.empty_field));
            otherHouseholdHead.getEditText().requestFocus();
            error = true;
        }

        if (App.get(otherEthinicity).isEmpty() && otherEthinicity.getVisibility() == View.VISIBLE) {
            otherEthinicity.getEditText().setError(getString(R.string.empty_field));
            otherEthinicity.getEditText().requestFocus();
            error = true;
        }

        if (error) {

            final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
            alertDialog.setMessage(getResources().getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            // DrawableCompat.setTint(clearIcon, color);
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
            return false;
        }

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
        if (spinner == ethinicity.getSpinner()) {
            if (App.get(ethinicity).equals(getResources().getString(R.string.pet_other_ethnicity)))
                otherEthinicity.setVisibility(View.VISIBLE);
            else
                otherEthinicity.setVisibility(View.GONE);
        } else if (spinner == householdHead.getSpinner()) {
            if (App.get(householdHead).equals(getResources().getString(R.string.pet_other)))
                otherHouseholdHead.setVisibility(View.VISIBLE);
            else
                otherHouseholdHead.setVisibility(View.GONE);
        } else if (spinner == motherTongue.getSpinner()) {
            if (App.get(motherTongue).equals(getResources().getString(R.string.pet_other)))
                otherMotherTongue.setVisibility(View.VISIBLE);
            else
                otherMotherTongue.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

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
